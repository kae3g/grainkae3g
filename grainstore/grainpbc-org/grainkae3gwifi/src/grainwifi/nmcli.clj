(ns grainwifi.nmcli
  "NetworkManager CLI interface for GrainWiFi"
  (:require [babashka.process :as p]
            [clojure.string :as str]))

(defn connection-list
  "List all available NetworkManager connections"
  []
  (try
    (let [result (p/shell {:out :string} "nmcli" "-t" "-f" "NAME,TYPE,DEVICE" "connection" "show")]
      (->> (str/split-lines (:out result))
           (map #(str/split % #":"))
           (map (fn [[name type device]]
                  {:name name
                   :type type
                   :device (when (not= device "") device)}))))
    (catch Exception e
      (println "Error listing connections:" (.getMessage e))
      [])))

(defn connection-active?
  "Check if a connection is currently active"
  [conn-name]
  (try
    (let [result (p/shell {:out :string :continue true} 
                         "nmcli" "-t" "-f" "NAME" "connection" "show" "--active")]
      (when (= 0 (:exit result))
        (some #(= conn-name %) (str/split-lines (:out result)))))
    (catch Exception _
      false)))

(defn connection-up
  "Bring a connection up"
  [conn-name]
  (try
    (println (str "📡 Activating connection: " conn-name))
    (let [result (p/shell {:out :string :continue true}
                         "nmcli" "connection" "up" conn-name)]
      (= 0 (:exit result)))
    (catch Exception e
      (println "Error activating connection:" (.getMessage e))
      false)))

(defn connection-down
  "Bring a connection down"
  [conn-name]
  (try
    (println (str "📴 Deactivating connection: " conn-name))
    (let [result (p/shell {:out :string :continue true}
                         "nmcli" "connection" "down" conn-name)]
      (= 0 (:exit result)))
    (catch Exception e
      (println "Error deactivating connection:" (.getMessage e))
      false)))

(defn get-active-connection
  "Get the currently active connection name"
  []
  (try
    (let [result (p/shell {:out :string} 
                         "nmcli" "-t" "-f" "NAME,TYPE" "connection" "show" "--active")
          active-conns (str/split-lines (:out result))]
      (when (seq active-conns)
        (-> (first active-conns)
            (str/split #":")
            first)))
    (catch Exception _
      nil)))

(defn ping-test
  "Test connection quality via ping"
  [{:keys [ping-host ping-count ping-timeout] :or {ping-count 3 ping-timeout 2000}}]
  (try
    (let [timeout-sec (int (/ ping-timeout 1000))
          result (p/shell {:out :string :continue true}
                         "ping" "-c" (str ping-count) 
                         "-W" (str timeout-sec)
                         ping-host)]
      (if (= 0 (:exit result))
        (let [output (:out result)
              ;; Parse average latency from ping output
              latency-match (re-find #"avg = ([\d.]+)" output)
              packet-loss-match (re-find #"(\d+)% packet loss" output)]
          {:success true
           :latency (when latency-match (parse-double (second latency-match)))
           :packet-loss (when packet-loss-match (parse-long (second packet-loss-match)))
           :quality (if (and latency-match packet-loss-match)
                      (max 0 (- 100 (parse-long (second packet-loss-match))))
                      0)})
        {:success false
         :latency nil
         :packet-loss 100
         :quality 0}))
    (catch Exception e
      (println "Ping test error:" (.getMessage e))
      {:success false :latency nil :packet-loss 100 :quality 0})))

(defn wifi-signal-strength
  "Get WiFi signal strength (if available)"
  []
  (try
    (let [result (p/shell {:out :string :continue true}
                         "nmcli" "-t" "-f" "SIGNAL" "device" "wifi" "list" "--rescan" "no")]
      (when (= 0 (:exit result))
        (let [signals (->> (str/split-lines (:out result))
                          (map parse-long)
                          (remove nil?))]
          (when (seq signals)
            (apply max signals)))))
    (catch Exception _
      nil)))

