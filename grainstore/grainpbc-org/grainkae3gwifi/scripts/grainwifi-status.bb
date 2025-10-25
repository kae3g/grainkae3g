#!/usr/bin/env bb

(ns grainwifi-status
  (:require [babashka.process :as p]
            [clojure.string :as str]
            [clojure.edn :as edn]))

(defn get-active-connection []
  (try
    (let [result (p/shell {:out :string :continue true}
                         "nmcli" "-t" "-f" "NAME" "connection" "show" "--active")]
      (when (= 0 (:exit result))
        (first (str/split-lines (:out result)))))
    (catch Exception _ nil)))

(defn ping-test [host]
  (try
    (let [result (p/shell {:out :string :continue true}
                         "ping" "-c" "3" "-W" "2" host)]
      (if (= 0 (:exit result))
        (let [output (:out result)
              latency-match (re-find #"avg = ([\d.]+)" output)
              packet-loss-match (re-find #"(\d+)% packet loss" output)]
          {:success true
           :latency (when latency-match (parse-double (second latency-match)))
           :packet-loss (when packet-loss-match (parse-long (second packet-loss-match)))})
        {:success false :latency nil :packet-loss 100}))
    (catch Exception _ 
      {:success false :latency nil :packet-loss 100})))

(defn list-available-connections []
  (try
    (let [result (p/shell {:out :string} "nmcli" "-t" "-f" "NAME,TYPE,DEVICE" "connection" "show")]
      (->> (str/split-lines (:out result))
           (map #(str/split % #":"))
           (map (fn [[name type device]]
                  {:name name
                   :type type
                   :device (when (not= device "") device)}))))
    (catch Exception _ [])))

(defn load-config []
  (let [config-path (or (System/getenv "GRAINWIFI_CONFIG")
                       "config/grainwifi.edn")]
    (try
      (edn/read-string (slurp config-path))
      (catch Exception _
        nil))))

(defn main []
  (println "")
  (println "🌾 GrainWiFi Status")
  (println "═══════════════════════════════════════════════════")
  (println "")
  
  (let [active-conn (get-active-connection)
        config (load-config)]
    
    ;; Show active connection
    (if active-conn
      (do
        (println (str "✅ Active Connection: " active-conn))
        (println "")
        
        ;; Test connection quality
        (println "📊 Connection Quality Test:")
        (let [ping-result (ping-test "1.1.1.1")]
          (if (:success ping-result)
            (do
              (println (str "   Ping: " (int (:latency ping-result)) "ms"))
              (println (str "   Packet Loss: " (:packet-loss ping-result) "%"))
              (let [quality (max 0 (- 100 (:packet-loss ping-result)))]
                (println (str "   Quality Score: " quality "%"))))
            (println "   ❌ Connection test failed"))))
      (println "❌ No active connection"))
    
    (println "")
    (println "📡 Available Connections:")
    (let [conns (list-available-connections)]
      (doseq [conn conns]
        (let [is-active (= (:name conn) active-conn)
              prefix (if is-active "  ✓ " "    ")]
          (println (str prefix (:name conn) 
                       (when (:device conn)
                         (str " [" (:device conn) "]")))))))
    
    (println "")
    (when config
      (println "⚙️  Configuration:")
      (let [starlink (get-in config [:connections :starlink])
            cellular (get-in config [:connections :cellular])]
        (println (str "   Starlink: " (:name starlink) " (priority " (:priority starlink) ")"))
        (println (str "   Cellular: " (:name cellular) " (priority " (:priority cellular) ")"))
        (println (str "   Auto-switch: " (get-in config [:switching :auto-switch])))
        (println (str "   Check interval: " (get-in config [:monitoring :check-interval]) "s"))))
    
    (println "")
    (println "═══════════════════════════════════════════════════")
    (println "")))

(main)

