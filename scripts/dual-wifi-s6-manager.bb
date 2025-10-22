#!/usr/bin/env bb

;; Dual WiFi Manager with s6 Integration
;; Uses clojure-s6 for reliable service management
;; Starlink + Cellular Balancing with Cursor Agent Protection

(require '[clojure.java.shell :as shell]
         '[clojure.string :as str]
         '[clojure.core.async :as async :refer [go go-loop timeout <! >! <!! >!!]])

;; Load clojure-s6 from grainstore
(load-file "grainstore/clojure-s6/src/clojure_s6/core.clj")

(defn log [message]
  "Log with timestamp and connection status"
  (let [timestamp (java.time.LocalDateTime/now)
        formatted-time (.format timestamp (java.time.format.DateTimeFormatter/ofPattern "HH:mm:ss"))]
    (println (str "[" formatted-time "] 🌐 " message))))

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

;; Configuration
(def config
  {:starlink
   {:interface "wlan0"
    :ssid "STARLINK"
    :min-speed-mbps 10.0
    :color "#00ff00"}
   
   :cellular
   {:interface "wlan1"
    :ssid "Cellular"
    :min-speed-mbps 2.0
    :color "#0088ff"}
   
   :balancer
   {:prefer-starlink-threshold 15.0
    :fallback-cellular-threshold 5.0
    :check-interval-ms 5000
    :speed-test-timeout-ms 10000}})

;; Application state
(def app-state
  (atom
   {:active-connection :cellular
    :starlink-status {:connected false :speed 0.0 :quality :poor}
    :cellular-status {:connected false :speed 0.0 :quality :poor}
    :cursor-agent-protected true
    :auto-switch true
    :last-update (java.time.LocalDateTime/now)
    :connection-history []
    :alerts []}))

(defn get-connected-ssid [interface]
  (let [result (run-command (str "iwconfig " interface " 2>/dev/null | grep -o 'ESSID:\"[^\"]*\"' | cut -d'\"' -f2"))]
    (when-not (str/blank? result) result)))

(defn is-interface-connected [interface]
  (let [ssid (get-connected-ssid interface)]
    (and ssid (not= ssid "off/any"))))

(defn connect-to-wifi [interface ssid]
  (log (str "Connecting " interface " to " ssid "..."))
  (let [result (run-command (str "nmcli dev wifi connect \"" ssid "\" ifname " interface) :sh true)]
    (str/includes? result "successfully")))

(defn run-speed-test [interface]
  (let [speedtest-cmd (str "speedtest-cli --interface " interface " --simple --timeout " 
                          (get-in config [:balancer :speed-test-timeout-ms]) " 2>/dev/null")
        result (run-command speedtest-cmd :sh true)]
    (if (str/blank? result)
      0.0
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
          (log (str "Speed test error: " (.getMessage e)))
          0.0)))))

(defn update-connection-status [connection-type]
  (let [interface (get-in config [connection-type :interface])
        min-speed (get-in config [connection-type :min-speed-mbps])
        connected (is-interface-connected interface)
        speed (if connected (run-speed-test interface) 0.0)
        quality (cond
                  (>= speed min-speed) :excellent
                  (>= speed (* min-speed 0.7)) :good
                  (>= speed (* min-speed 0.4)) :fair
                  :else :poor)]
    
    (swap! app-state assoc-in [connection-type :status] 
           {:connected connected :speed speed :quality quality})
    
    (log (str (str/upper-case (name connection-type)) ": " 
              (if connected "Connected" "Disconnected") " - " 
              (format "%.1f" speed) " Mbps (" (name quality) ")"))))

(defn should-switch-to-starlink []
  (let [starlink-speed (get-in @app-state [:starlink-status :speed])
        cellular-speed (get-in @app-state [:cellular-status :speed])
        threshold (get-in config [:balancer :prefer-starlink-threshold])]
    (and (>= starlink-speed threshold)
         (> starlink-speed cellular-speed)
         (get-in @app-state [:starlink-status :connected]))))

(defn should-switch-to-cellular []
  (let [active (:active-connection @app-state)
        current-speed (get-in @app-state [active :speed])
        threshold (get-in config [:balancer :fallback-cellular-threshold])]
    (and (< current-speed threshold)
         (get-in @app-state [:cellular-status :connected]))))

(defn switch-connection [new-connection]
  (let [current-connection (:active-connection @app-state)
        interface (get-in config [new-connection :interface])
        ssid (get-in config [new-connection :ssid])]
    
    (when (and (not= current-connection new-connection)
               (:auto-switch @app-state))
      (log (str "Switching from " (name current-connection) " to " (name new-connection)))
      
      (if (connect-to-wifi interface ssid)
        (do
          (swap! app-state assoc :active-connection new-connection)
          (swap! app-state update :connection-history 
                 #(conj % {:from current-connection :to new-connection :time (java.time.LocalDateTime/now)}))
          (log (str "Successfully switched to " (name new-connection))))
        (log (str "Failed to switch to " (name new-connection)))))))

(defn create-s6-service []
  "Create s6 service for dual WiFi manager"
  (log "🔧 Creating s6 service for dual WiFi manager...")
  
  (let [service-config
        {:name "dual-wifi-manager"
         :command (str "bb " (System/getProperty "user.dir") "/scripts/dual-wifi-s6-manager.bb daemon")
         :type :longrun
         :dependencies ["network"]
         :user (System/getProperty "user.name")
         :group (System/getProperty "user.name")}]
    
    (clojure-s6.core/create-service service-config)
    (clojure-s6.core/enable-service "dual-wifi-manager")
    (log "✅ s6 service created and enabled")))

(defn start-s6-service []
  "Start the s6 service"
  (log "🚀 Starting dual WiFi manager via s6...")
  (clojure-s6.core/start-service "dual-wifi-manager"))

(defn stop-s6-service []
  "Stop the s6 service"
  (log "🛑 Stopping dual WiFi manager via s6...")
  (clojure-s6.core/stop-service "dual-wifi-manager"))

(defn restart-s6-service []
  "Restart the s6 service"
  (log "🔄 Restarting dual WiFi manager via s6...")
  (clojure-s6.core/restart-service "dual-wifi-manager"))

(defn status-s6-service []
  "Get s6 service status"
  (let [status (clojure-s6.core/service-status "dual-wifi-manager")]
    (log (str "📊 s6 Service Status: " status))
    status))

(defn monitor-connections []
  "Monitor both connections continuously"
  (go-loop []
    (try
      ;; Check Starlink
      (when (is-interface-connected (get-in config [:starlink :interface]))
        (update-connection-status :starlink))
      
      ;; Check Cellular
      (when (is-interface-connected (get-in config [:cellular :interface]))
        (update-connection-status :cellular))
      
      ;; Make switching decisions
      (when (:auto-switch @app-state)
        (cond
          (should-switch-to-starlink) (switch-connection :starlink)
          (should-switch-to-cellular) (switch-connection :cellular)))
      
      (swap! app-state assoc :last-update (java.time.LocalDateTime/now))
      
      (<! (timeout (get-in config [:balancer :check-interval-ms])))
      
      (catch Exception e
        (log (str "Monitoring error: " (.getMessage e)))
        (<! (timeout 5000))))
    
    (recur)))

(defn daemon-mode []
  "Run in daemon mode (called by s6)"
  (log "🌐 Starting Dual WiFi Manager Daemon (s6)")
  (log "   Starlink: High peak, intermittent")
  (log "   Cellular: Lower peak, consistent")
  (log "   Goal: Prevent Cursor agent flow interruptions")
  
  ;; Start monitoring
  (monitor-connections)
  
  ;; Keep daemon running
  (loop []
    (Thread/sleep 1000)
    (recur)))

(defn main []
  "Main entry point"
  (let [args *command-line-args*]
    (case (first args)
      "create-service" (create-s6-service)
      "start" (start-s6-service)
      "stop" (stop-s6-service)
      "restart" (restart-s6-service)
      "status" (status-s6-service)
      "daemon" (daemon-mode)
      (do
        (log "🌐 Dual WiFi Manager - s6 Integration")
        (log "")
        (log "Usage:")
        (log "  bb dual-wifi-s6-manager.bb create-service  # Create s6 service")
        (log "  bb dual-wifi-s6-manager.bb start           # Start via s6")
        (log "  bb dual-wifi-s6-manager.bb stop            # Stop via s6")
        (log "  bb dual-wifi-s6-manager.bb restart         # Restart via s6")
        (log "  bb dual-wifi-s6-manager.bb status          # Check s6 status")
        (log "  bb dual-wifi-s6-manager.bb daemon          # Run daemon mode")
        (log "")
        (log "🔧 s6 Integration:")
        (log "   Reliable service management")
        (log "   Automatic restart on failure")
        (log "   Proper logging and supervision")
        (log "   SixOS ready")))))

;; Run the main function
(main)

