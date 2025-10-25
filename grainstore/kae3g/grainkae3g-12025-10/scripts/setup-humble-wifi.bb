#!/usr/bin/env bb

;; Setup script for Dual WiFi Manager - Humble UI version
;; Installs dependencies and creates project structure

(require '[clojure.java.shell :as shell]
         '[clojure.string :as str]
         '[babashka.fs :as fs])

(defn log [message]
  "Log with timestamp"
  (let [timestamp (java.time.LocalDateTime/now)
        formatted-time (.format timestamp (java.time.format.DateTimeFormatter/ofPattern "HH:mm:ss"))]
    (println (str "[" formatted-time "] " message))))

(defn run-command [cmd & {:keys [sh]}]
  "Run command with optional shell execution"
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

(defn check-dependencies []
  "Check if required dependencies are installed"
  (log "🔍 Checking dependencies...")
  
  (let [deps {"clojure" "clojure --version"
              "speedtest-cli" "speedtest-cli --version"
              "nmcli" "nmcli --version"
              "iwconfig" "iwconfig --version"}]
    
    (doseq [[name cmd] deps]
      (let [result (run-command cmd :sh true)]
        (if (str/blank? result)
          (log (str "❌ " name " not found"))
          (log (str "✅ " name " found")))))))

(defn install-dependencies []
  "Install missing dependencies"
  (log "📦 Installing dependencies...")
  
  ;; Install speedtest-cli
  (log "Installing speedtest-cli...")
  (run-command "sudo apt update && sudo apt install -y speedtest-cli" :sh true)
  
  ;; Install wireless tools
  (log "Installing wireless tools...")
  (run-command "sudo apt install -y wireless-tools" :sh true)
  
  ;; Install NetworkManager
  (log "Installing NetworkManager...")
  (run-command "sudo apt install -y network-manager" :sh true))

(defn create-project-structure []
  "Create project directory structure"
  (log "📁 Creating project structure...")
  
  (let [project-dir "dual-wifi-humble"
        dirs ["src" "resources" "test" "scripts"]]
    
    (when-not (fs/exists? project-dir)
      (fs/create-dir project-dir))
    
    (doseq [dir dirs]
      (let [full-path (str project-dir "/" dir)]
        (when-not (fs/exists? full-path)
          (fs/create-dir full-path)
          (log (str "Created directory: " full-path)))))))

(defn create-deps-edn []
  "Create deps.edn for Clojure dependencies"
  (log "📝 Creating deps.edn...")
  
  (let [deps-content
        '{:deps {org.clojure/clojure {:mvn/version "1.11.1"}
                 io.github.HumbleUI/HumbleUI {:mvn/version "0.4.0"}
                 org.clojure/core.async {:mvn/version "1.6.681"}
                 org.clojure/data.json {:mvn/version "2.4.0"}}
          :paths ["src"]
          :aliases {:dev {:extra-paths ["test"]}
                    :run {:main-opts ["-m" "dual-wifi-humble"]}}}}]
    
    (spit "dual-wifi-humble/deps.edn" (pr-str deps-content))
    (log "✅ deps.edn created")))

(defn create-main-ns []
  "Create main namespace file"
  (log "📝 Creating main namespace...")
  
  (let [main-content
        "(ns dual-wifi-humble
  (:require [humble.ui :as ui]
            [clojure.core.async :as async :refer [go go-loop timeout <! >! <!! >!!]]
            [clojure.java.shell :as shell]
            [clojure.string :as str]
            [clojure.data.json :as json]))

;; Configuration
(def config
  {:starlink
   {:interface \"wlan0\"
    :ssid \"STARLINK\"
    :min-speed-mbps 10.0
    :color \"#00ff00\"}
   
   :cellular
   {:interface \"wlan1\"
    :ssid \"Cellular\"
    :min-speed-mbps 2.0
    :color \"#0088ff\"}
   
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

;; Utility functions
(defn log [message]
  (let [timestamp (java.time.LocalDateTime/now)
        formatted-time (.format timestamp (java.time.format.DateTimeFormatter/ofPattern \"HH:mm:ss\"))]
    (println (str \"[\" formatted-time \"] \" message))
    (swap! app-state update :alerts #(conj % {:time formatted-time :message message :type :info}))))

(defn run-command [cmd & {:keys [sh timeout-ms]}]
  (try
    (let [result (if sh
                   (shell/sh \"bash\" \"-c\" cmd)
                   (shell/sh cmd))]
      (if (= (:exit result) 0)
        (str/trim (:out result))
        \"\"))
    (catch Exception e
      (log (str \"Command error: \" (.getMessage e)))
      \"\")))

(defn get-connected-ssid [interface]
  (let [result (run-command (str \"iwconfig \" interface \" 2>/dev/null | grep -o 'ESSID:\\\"[^\\\"]*\\\"' | cut -d'\\\"' -f2\"))]
    (when-not (str/blank? result) result)))

(defn is-interface-connected [interface]
  (let [ssid (get-connected-ssid interface)]
    (and ssid (not= ssid \"off/any\"))))

(defn connect-to-wifi [interface ssid]
  (log (str \"Connecting \" interface \" to \" ssid \"...\"))
  (let [result (run-command (str \"nmcli dev wifi connect \\\"\" ssid \"\\\" ifname \" interface) :sh true)]
    (str/includes? result \"successfully\")))

(defn run-speed-test [interface]
  (let [speedtest-cmd (str \"speedtest-cli --interface \" interface \" --json --timeout \" 
                          (get-in config [:balancer :speed-test-timeout-ms]) \" 2>/dev/null\")
        result (run-command speedtest-cmd :sh true)]
    (if (str/blank? result)
      0.0
      (try
        (let [parsed (json/read-str result)]
          (get parsed \"download\" 0.0))
        (catch Exception e
          (log (str \"Speed test error: \" (.getMessage e)))
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
    
    (log (str (str/upper-case (name connection-type)) \": \" 
              (if connected \"Connected\" \"Disconnected\") \" - \" 
              (format \"%.1f\" speed) \" Mbps (\" (name quality) \")\"))))

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
      (log (str \"Switching from \" (name current-connection) \" to \" (name new-connection)))
      
      (if (connect-to-wifi interface ssid)
        (do
          (swap! app-state assoc :active-connection new-connection)
          (swap! app-state update :connection-history 
                 #(conj % {:from current-connection :to new-connection :time (java.time.LocalDateTime/now)}))
          (log (str \"Successfully switched to \" (name new-connection))))
        (log (str \"Failed to switch to \" (name new-connection)))))))

;; GUI Components
(defn status-indicator [connection-type]
  (let [status (get-in @app-state [connection-type :status])
        color (get-in config [connection-type :color])
        connected (:connected status)
        speed (:speed status)
        quality (:quality status)]
    (ui/column
     {:style {:background (if connected color \"#666666\")
              :padding 8
              :border-radius 4
              :margin 4}}
     (ui/label (str (str/upper-case (name connection-type))))
     (ui/label (if connected \"Connected\" \"Disconnected\"))
     (ui/label (str (format \"%.1f\" speed) \" Mbps\"))
     (ui/label (str \"Quality: \" (name quality))))))

(defn connection-graph []
  (let [history (:connection-history @app-state)
        recent-history (take-last 20 history)]
    (ui/column
     {:style {:background \"#1a1a1a\"
              :padding 8
              :border-radius 4
              :margin 4}}
     (ui/label \"Connection History\")
     (ui/scroll
      (ui/column
       (for [entry recent-history]
         (ui/label
          {:style {:color \"#888888\"}}
          (str (.format (:time entry) (java.time.format.DateTimeFormatter/ofPattern \"HH:mm:ss\"))
               \" \" (name (:from entry)) \" → \" (name (:to entry))))))))))

(defn alerts-panel []
  (let [alerts (:alerts @app-state)
        recent-alerts (take-last 10 alerts)]
    (ui/column
     {:style {:background \"#1a1a1a\"
              :padding 8
              :border-radius 4
              :margin 4}}
     (ui/label \"Recent Alerts\")
     (ui/scroll
      (ui/column
       (for [alert recent-alerts]
         (ui/label
          {:style {:color (case (:type alert)
                            :error \"#ff4444\"
                            :warning \"#ffaa00\"
                            :success \"#44ff44\"
                            :info \"#4488ff\")}}
          (str (:time alert) \" \" (:message alert)))))))))

(defn control-panel []
  (ui/column
   {:style {:background \"#2a2a2a\"
            :padding 8
            :border-radius 4
            :margin 4}}
   (ui/label \"Controls\")
   (ui/row
    (ui/button
     {:on-click #(do
                   (swap! app-state assoc :auto-switch (not (:auto-switch @app-state)))
                   (log (str \"Auto-switch: \" (:auto-switch @app-state))))}
     (ui/label (str \"Auto-switch: \" (:auto-switch @app-state))))
    (ui/button
     {:on-click #(do
                   (switch-connection :starlink)
                   (log \"Forced switch to Starlink\"))}
     (ui/label \"Force Starlink\"))
    (ui/button
     {:on-click #(do
                   (switch-connection :cellular)
                   (log \"Forced switch to Cellular\"))}
     (ui/label \"Force Cellular\")))
   (ui/button
    {:on-click #(do
                  (update-connection-status :starlink)
                  (update-connection-status :cellular)
                  (log \"Manual refresh completed\"))}
    (ui/label \"Refresh Now\"))))

(defn main-panel []
  (ui/column
   {:style {:background \"#0a0a0a\"
            :padding 16}}
   (ui/label
    {:style {:font-size 24
             :color \"#ffffff\"
             :margin-bottom 16}}
    \"🌐 Dual WiFi Manager\")
   
   (ui/row
    (status-indicator :starlink)
    (status-indicator :cellular))
   
   (ui/row
    (connection-graph)
    (alerts-panel))
   
   (control-panel)
   
   (ui/label
    {:style {:color \"#666666\"
             :font-size 12}}
    (str \"Active: \" (name (:active-connection @app-state))
         \" | Cursor Protected: \" (:cursor-agent-protected @app-state)
         \" | Last Update: \" (.format (:last-update @app-state) 
                                     (java.time.format.DateTimeFormatter/ofPattern \"HH:mm:ss\"))))))

;; Background monitoring
(defn start-monitoring []
  (go-loop []
    (try
      (update-connection-status :starlink)
      (update-connection-status :cellular)
      
      (when (:auto-switch @app-state)
        (cond
          (should-switch-to-starlink) (switch-connection :starlink)
          (should-switch-to-cellular) (switch-connection :cellular)))
      
      (swap! app-state assoc :last-update (java.time.LocalDateTime/now))
      
      (<! (timeout (get-in config [:balancer :check-interval-ms])))
      (recur)
      
      (catch Exception e
        (log (str \"Monitoring error: \" (.getMessage e)))
        (<! (timeout 5000))
        (recur)))))

;; Main application
(defn -main []
  (log \"Starting Dual WiFi Manager - Humble UI\")
  
  ;; Start background monitoring
  (start-monitoring)
  
  ;; Start GUI
  (ui/start-app!
   (ui/window
    {:title \"Dual WiFi Manager\"
     :width 800
     :height 600
     :on-close (fn [] (log \"Application closing\"))}
    main-panel)))

;; Run the application
(-main)"]
    
    (spit "dual-wifi-humble/src/dual_wifi_humble.clj" main-content)
    (log "✅ Main namespace created")))

(defn create-run-script []
  "Create run script"
  (log "📝 Creating run script...")
  
  (let [run-script "#!/bin/bash

# Dual WiFi Manager - Humble UI
# Run the desktop application

echo \"🌐 Starting Dual WiFi Manager - Humble UI\"
echo \"   Starlink: High peak, intermittent\"
echo \"   Cellular: Lower peak, consistent\"
echo \"   Goal: Prevent Cursor agent flow interruptions\"
echo \"\"

cd dual-wifi-humble
clojure -M:run"]
    
    (spit "dual-wifi-humble/run.sh" run-script)
    (run-command "chmod +x dual-wifi-humble/run.sh" :sh true)
    (log "✅ Run script created")))

(defn create-readme []
  "Create README"
  (log "📝 Creating README...")
  
  (let [readme-content
        "# 🌐 Dual WiFi Manager - Humble UI

**Desktop application for intelligent Starlink + Cellular connection balancing**

## 🎯 Purpose

Prevents Cursor agent flow interruptions by intelligently switching between:
- **Starlink**: High peak speeds, intermittent connection
- **Cellular**: Lower peak speeds, consistent connection

## 🚀 Features

- **Real-time GUI**: Live connection status and speed monitoring
- **Intelligent Switching**: Automatic connection optimization
- **Cursor Protection**: Ensures stable connection for AI agents
- **Connection History**: Track switching patterns
- **Manual Controls**: Override automatic switching

## 🛠️ Installation

1. Run setup script:
   ```bash
   bb scripts/setup-humble-wifi.bb
   ```

2. Start application:
   ```bash
   cd dual-wifi-humble
   ./run.sh
   ```

## ⚙️ Configuration

Edit `src/dual_wifi_humble.clj` to configure:
- WiFi interfaces (wlan0, wlan1)
- SSID names
- Speed thresholds
- Check intervals

## 🎮 Usage

- **Auto-switch**: Toggle automatic connection switching
- **Force Starlink**: Manually switch to Starlink
- **Force Cellular**: Manually switch to Cellular
- **Refresh**: Update connection status

## 📊 Performance

- **Native Desktop**: Faster than web applications
- **Real-time Updates**: 5-second monitoring interval
- **Low Latency**: Direct system integration
- **Memory Efficient**: Minimal resource usage

## 🔧 Troubleshooting

- Ensure both WiFi interfaces are available
- Check NetworkManager is running
- Verify speedtest-cli is installed
- Check interface permissions

## 📝 Logs

Application logs are displayed in the GUI and console.

---

**Built with Clojure + Humble UI for maximum performance and system integration.**"]
    
    (spit "dual-wifi-humble/README.md" readme-content)
    (log "✅ README created")))

(defn main []
  "Main setup function"
  (log "🚀 Setting up Dual WiFi Manager - Humble UI")
  (log "   Native desktop performance for connection balancing")
  
  (try
    (check-dependencies)
    (install-dependencies)
    (create-project-structure)
    (create-deps-edn)
    (create-main-ns)
    (create-run-script)
    (create-readme)
    
    (log "")
    (log "✅ Setup complete!")
    (log "")
    (log "🎮 To start the application:")
    (log "   cd dual-wifi-humble")
    (log "   ./run.sh")
    (log "")
    (log "🔧 To configure:")
    (log "   Edit src/dual_wifi_humble.clj")
    (log "")
    (log "📚 For help:")
    (log "   cat dual-wifi-humble/README.md")
    
    (catch Exception e
      (log (str "❌ Setup failed: " (.getMessage e)))
      (System/exit 1))))

;; Run the setup
(main)

