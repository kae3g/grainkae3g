#!/usr/bin/env bb

(ns grainwifi-switch
  (:require [babashka.process :as p]
            [clojure.string :as str]
            [clojure.edn :as edn]))

(defn connection-up [conn-name]
  (try
    (println (str "📡 Activating connection: " conn-name))
    (let [result (p/shell {:out :string :continue true}
                         "nmcli" "connection" "up" conn-name)]
      (if (= 0 (:exit result))
        (do
          (println (str "✅ Connected to " conn-name))
          true)
        (do
          (println (str "❌ Failed to connect to " conn-name))
          false)))
    (catch Exception e
      (println (str "Error: " (.getMessage e)))
      false)))

(defn connection-down [conn-name]
  (try
    (p/shell {:out :string :continue true}
             "nmcli" "connection" "down" conn-name)
    (catch Exception _ nil)))

(defn get-active-connection []
  (try
    (let [result (p/shell {:out :string :continue true}
                         "nmcli" "-t" "-f" "NAME" "connection" "show" "--active")]
      (when (= 0 (:exit result))
        (first (str/split-lines (:out result)))))
    (catch Exception _ nil)))

(defn load-config []
  (let [config-path (or (System/getenv "GRAINWIFI_CONFIG")
                       "config/grainwifi.edn")]
    (try
      (edn/read-string (slurp config-path))
      (catch Exception _
        (println "⚠️  No config found, using connection names from arguments")
        nil))))

(defn send-notification [title message]
  (try
    (p/shell {:continue true}
             "notify-send"
             "-u" "normal"
             "-i" "network-wireless"
             "-t" "5000"
             title
             message)
    (catch Exception _ nil)))

(defn main [target]
  (println "")
  (println "🌾 GrainWiFi Connection Switcher")
  (println "═══════════════════════════════════════════════════")
  (println "")
  
  (let [config (load-config)
        current-conn (get-active-connection)
        target-conn (case target
                     "starlink" (get-in config [:connections :starlink :name] "Starlink")
                     "cellular" (get-in config [:connections :cellular :name] "Cellular-Tethering")
                     "auto" (get-in config [:connections :starlink :name] "Starlink")
                     target)]
    
    (println (str "Current connection: " (or current-conn "None")))
    (println (str "Target connection: " target-conn))
    (println "")
    
    ;; Don't switch if already connected
    (if (= current-conn target-conn)
      (do
        (println (str "✓ Already connected to " target-conn))
        (println ""))
      (do
        ;; Perform the switch
        (when current-conn
          (println (str "📴 Disconnecting from " current-conn))
          (connection-down current-conn)
          (Thread/sleep 1000))
        
        (if (connection-up target-conn)
          (do
            (send-notification "🔄 Connection Switched" 
                             (str "Now using " target-conn))
            (println (str "✅ Successfully switched to " target-conn)))
          (do
            (println (str "❌ Failed to switch to " target-conn))
            (when current-conn
              (println (str "🔄 Attempting to restore " current-conn))
              (connection-up current-conn))))))
    
    (println "")
    (println "═══════════════════════════════════════════════════")
    (println "")))

(let [target (or (first *command-line-args*) "auto")]
  (main target))

