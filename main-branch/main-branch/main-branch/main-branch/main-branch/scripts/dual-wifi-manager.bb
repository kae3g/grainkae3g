#!/usr/bin/env bb

;; Dual WiFi Manager - Starlink + Cellular Balancing
;; Intelligently switches between Starlink (high peak, intermittent) and cellular (consistent, lower peak)
;; Designed to prevent Cursor agent flow interruptions

(require '[clojure.java.shell :as shell]
         '[clojure.string :as str]
         '[clojure.core.async :as async :refer [go go-loop timeout <! >! <!! >!!]])

(defn log [message]
  "Log with timestamp and connection status"
  (let [timestamp (java.time.LocalDateTime/now)
        formatted-time (.format timestamp (java.time.format.DateTimeFormatter/ofPattern "HH:mm:ss"))]
    (println (str "[" formatted-time "] " message))))

(defn run-command [cmd & {:keys [sh timeout-ms]}]
  "Run command with optional shell execution and timeout"
  (try
    (let [result (if sh
                   (shell/sh "bash" "-c" cmd)
                   (shell/sh cmd))]
      (if (= (:exit result) 0)
        (str/trim (:out result))
        (do
          (log (str "Command failed: " cmd " - " (:err result)))
          "")))
    (catch Exception e
      (log (str "Error running command: " cmd " - " (.getMessage e)))
      "")))

;; Connection configuration
(def config
  {:starlink
   {:interface "wlan0"  ; Adjust based on your setup
    :ssid "STARLINK"
    :min-speed-mbps 10.0
    :check-interval-ms 30000
    :reconnect-delay-ms 5000}
   
   :cellular
   {:interface "wlan1"  ; Adjust based on your setup
    :ssid "Cellular"
    :min-speed-mbps 2.0
    :check-interval-ms 15000
    :reconnect-delay-ms 2000}
   
   :balancer
   {:prefer-starlink-threshold 15.0  ; Switch to Starlink if >15 Mbps
    :fallback-cellular-threshold 5.0  ; Switch to cellular if <5 Mbps
    :speed-test-timeout-ms 10000
    :connection-stability-window 3    ; Require 3 consecutive good tests
    :max-switch-attempts 3}})

;; State management
(def state (atom
            {:active-connection :cellular  ; Start with cellular for stability
             :starlink-status :disconnected
             :cellular-status :connected
             :last-speed-test {:starlink 0.0 :cellular 0.0}
             :connection-stability {:starlink 0 :cellular 0}
             :switch-attempts 0
             :cursor-agent-protected true}))

(defn get-wifi-interfaces []
  "Get available WiFi interfaces"
  (let [interfaces (run-command "iwconfig 2>/dev/null | grep -o '^[a-zA-Z0-9]*' | grep -v '^lo$'")]
    (str/split-lines interfaces)))

(defn get-connected-ssid [interface]
  "Get SSID of connected network on interface"
  (let [result (run-command (str "iwconfig " interface " 2>/dev/null | grep -o 'ESSID:\"[^\"]*\"' | cut -d'\"' -f2"))]
    (when-not (str/blank? result)
      result)))

(defn is-interface-connected [interface]
  "Check if interface is connected to a network"
  (let [ssid (get-connected-ssid interface)]
    (and ssid (not= ssid "off/any"))))

(defn connect-to-wifi [interface ssid]
  "Connect to specific WiFi network"
  (log (str "🔌 Connecting " interface " to " ssid "..."))
  
  ;; Use NetworkManager if available, otherwise wpa_supplicant
  (let [nm-result (run-command (str "nmcli dev wifi connect \"" ssid "\" ifname " interface) :sh true)]
    (if (str/includes? nm-result "successfully")
      (do
        (log (str "✅ Connected " interface " to " ssid))
        true)
      (do
        (log (str "❌ Failed to connect " interface " to " ssid))
        false))))

(defn run-speed-test [interface]
  "Run speed test on specific interface"
  (log (str "📊 Running speed test on " interface "..."))
  
  (let [speedtest-cmd (str "speedtest-cli --interface " interface " --simple --timeout " 
                          (get-in config [:balancer :speed-test-timeout-ms]) " 2>/dev/null")
        result (run-command speedtest-cmd :sh true)]
    
    (if (str/blank? result)
      (do
        (log (str "⚠️ Speed test failed on " interface))
        0.0)
      (try
        (let [lines (str/split-lines result)
              download-line (first (filter #(str/includes? % "Download:") lines))]
          (if download-line
            (let [speed-str (-> download-line
                                (str/replace #"Download:" "")
                                (str/trim)
                                (str/replace #"Mbit/s" "")
                                (str/trim))]
              (Double/parseDouble speed-str))
            0.0))
        (catch Exception e
          (log (str "❌ Error parsing speed test result: " (.getMessage e)))
          0.0)))))

(defn check-connection-quality [connection-type]
  "Check connection quality and update state"
  (let [interface (get-in config [connection-type :interface])
        min-speed (get-in config [connection-type :min-speed-mbps])
        speed (run-speed-test interface)]
    
    (swap! state assoc-in [:last-speed-test connection-type] speed)
    
    (if (>= speed min-speed)
      (do
        (swap! state update-in [:connection-stability connection-type] inc)
        (log (str "✅ " (str/upper-case (name connection-type)) " quality good: " 
                 (format "%.1f" speed) " Mbps"))
        true)
      (do
        (swap! state assoc-in [:connection-stability connection-type] 0)
        (log (str "⚠️ " (str/upper-case (name connection-type)) " quality poor: " 
                 (format "%.1f" speed) " Mbps"))
        false))))

(defn should-switch-to-starlink []
  "Determine if we should switch to Starlink"
  (let [starlink-speed (get-in @state [:last-speed-test :starlink])
        cellular-speed (get-in @state [:last-speed-test :cellular])
        threshold (get-in config [:balancer :prefer-starlink-threshold])
        stability (get-in @state [:connection-stability :starlink])
        required-stability (get-in config [:balancer :connection-stability-window])]
    
    (and (>= starlink-speed threshold)
         (> starlink-speed cellular-speed)
         (>= stability required-stability))))

(defn should-switch-to-cellular []
  "Determine if we should switch to cellular"
  (let [current-speed (get-in @state [:last-speed-test (:active-connection @state)])
        threshold (get-in config [:balancer :fallback-cellular-threshold])
        stability (get-in @state [:connection-stability (:active-connection @state)])
        required-stability (get-in config [:balancer :connection-stability-window])]
    
    (or (< current-speed threshold)
        (< stability required-stability))))

(defn switch-connection [new-connection]
  "Switch to new connection type"
  (let [current-connection (:active-connection @state)
        interface (get-in config [new-connection :interface])
        ssid (get-in config [new-connection :ssid])]
    
    (when (not= current-connection new-connection)
      (log (str "🔄 Switching from " (name current-connection) " to " (name new-connection)))
      
      (if (connect-to-wifi interface ssid)
        (do
          (swap! state assoc :active-connection new-connection)
          (swap! state assoc :switch-attempts 0)
          (log (str "✅ Successfully switched to " (name new-connection))))
        (do
          (swap! state update :switch-attempts inc)
          (log (str "❌ Failed to switch to " (name new-connection))))))))

(defn protect-cursor-agent []
  "Ensure Cursor agent has stable connection"
  (let [current-connection (:active-connection @state)
        current-speed (get-in @state [:last-speed-test current-connection])
        min-speed (get-in config [current-connection :min-speed-mbps])]
    
    (when (< current-speed min-speed)
      (log "🛡️ Cursor agent protection: Connection quality below threshold")
      
      ;; Try to switch to better connection
      (if (and (should-switch-to-starlink) 
               (not= current-connection :starlink))
        (switch-connection :starlink)
        (when (and (should-switch-to-cellular)
                   (not= current-connection :cellular))
          (switch-connection :cellular))))))

(defn monitor-connections []
  "Monitor both connections continuously"
  (go-loop []
    (try
      ;; Check Starlink
      (when (is-interface-connected (get-in config [:starlink :interface]))
        (check-connection-quality :starlink))
      
      ;; Check Cellular
      (when (is-interface-connected (get-in config [:cellular :interface]))
        (check-connection-quality :cellular))
      
      ;; Make switching decisions
      (cond
        (should-switch-to-starlink) (switch-connection :starlink)
        (should-switch-to-cellular) (switch-connection :cellular)
        :else (log (str "📡 Maintaining " (name (:active-connection @state)) " connection")))
      
      ;; Protect Cursor agent
      (protect-cursor-agent)
      
      ;; Wait before next check
      (<! (timeout (get-in config [:balancer :check-interval-ms])))
      
      (catch Exception e
        (log (str "❌ Error in connection monitoring: " (.getMessage e)))
        (<! (timeout 5000))))
    
    (recur)))

(defn setup-interfaces []
  "Setup WiFi interfaces for dual connection"
  (log "🔧 Setting up dual WiFi interfaces...")
  
  (let [interfaces (get-wifi-interfaces)]
    (log (str "📡 Available interfaces: " (str/join ", " interfaces)))
    
    ;; Ensure both interfaces are available
    (when (< (count interfaces) 2)
      (log "⚠️ Warning: Less than 2 WiFi interfaces detected. Some features may not work."))
    
    ;; Connect to both networks
    (let [starlink-interface (get-in config [:starlink :interface])
          cellular-interface (get-in config [:cellular :interface])
          starlink-ssid (get-in config [:starlink :ssid])
          cellular-ssid (get-in config [:cellular :ssid])]
      
      (log (str "🌌 Connecting to Starlink (" starlink-ssid ") on " starlink-interface))
      (connect-to-wifi starlink-interface starlink-ssid)
      
      (log (str "📱 Connecting to Cellular (" cellular-ssid ") on " cellular-interface))
      (connect-to-wifi cellular-interface cellular-ssid))))

(defn start-daemon []
  "Start the dual WiFi manager daemon"
  (log "🚀 Starting Dual WiFi Manager Daemon...")
  (log "   Starlink: High peak, intermittent")
  (log "   Cellular: Lower peak, consistent")
  (log "   Goal: Prevent Cursor agent flow interruptions")
  
  (setup-interfaces)
  
  ;; Start monitoring
  (monitor-connections)
  
  (log "✅ Dual WiFi Manager started successfully!")
  (log "   Monitoring connections every 30 seconds")
  (log "   Cursor agent protection enabled")
  (log "   Press Ctrl+C to stop"))

(defn status []
  "Show current connection status"
  (let [active (:active-connection @state)
        starlink-speed (get-in @state [:last-speed-test :starlink])
        cellular-speed (get-in @state [:last-speed-test :cellular])
        starlink-status (:starlink-status @state)
        cellular-status (:cellular-status @state)]
    
    (log "📊 Dual WiFi Manager Status:")
    (log (str "   Active Connection: " (name active)))
    (log (str "   Starlink: " (name starlink-status) " (" (format "%.1f" starlink-speed) " Mbps)"))
    (log (str "   Cellular: " (name cellular-status) " (" (format "%.1f" cellular-speed) " Mbps)"))
    (log (str "   Cursor Agent Protected: " (:cursor-agent-protected @state)))))

(defn main []
  "Main entry point"
  (let [args *command-line-args*]
    (case (first args)
      "start" (start-daemon)
      "status" (status)
      "test-starlink" (do
                       (log "🧪 Testing Starlink connection...")
                       (check-connection-quality :starlink))
      "test-cellular" (do
                       (log "🧪 Testing Cellular connection...")
                       (check-connection-quality :cellular))
      "switch-starlink" (switch-connection :starlink)
      "switch-cellular" (switch-connection :cellular)
      (do
        (log "🔧 Dual WiFi Manager - Starlink + Cellular Balancing")
        (log "")
        (log "Usage:")
        (log "  bb dual-wifi-manager.bb start          # Start daemon")
        (log "  bb dual-wifi-manager.bb status         # Show status")
        (log "  bb dual-wifi-manager.bb test-starlink  # Test Starlink")
        (log "  bb dual-wifi-manager.bb test-cellular  # Test Cellular")
        (log "  bb dual-wifi-manager.bb switch-starlink # Force Starlink")
        (log "  bb dual-wifi-manager.bb switch-cellular # Force Cellular")
        (log "")
        (log "Configuration:")
        (log (str "  Starlink Interface: " (get-in config [:starlink :interface])))
        (log (str "  Cellular Interface: " (get-in config [:cellular :interface])))
        (log (str "  Prefer Starlink > " (get-in config [:balancer :prefer-starlink-threshold]) " Mbps"))
        (log (str "  Fallback Cellular < " (get-in config [:balancer :fallback-cellular-threshold]) " Mbps"))))))

;; Run the main function
(main)
