(ns grainwifi.monitor
  "Connection quality monitoring for GrainWiFi"
  (:require [grainwifi.nmcli :as nmcli]
            [clojure.string :as str]))

(defn check-connection-quality
  "Check the quality of a connection configuration"
  [conn-config]
  (let [ping-result (nmcli/ping-test conn-config)
        signal (nmcli/wifi-signal-strength)
        quality (:quality ping-result 0)]
    (merge ping-result
           {:signal-strength signal
            :quality-score quality
            :timestamp (java.time.Instant/now)
            :connection-name (:name conn-config)})))

(defn quality-score->status
  "Convert quality score to human-readable status"
  [score]
  (cond
    (>= score 80) {:status :excellent :emoji "✅" :color "green"}
    (>= score 60) {:status :good :emoji "✓" :color "blue"}
    (>= score 40) {:status :fair :emoji "⚠️" :color "yellow"}
    (>= score 20) {:status :poor :emoji "❗" :color "orange"}
    :else {:status :failed :emoji "❌" :color "red"}))

(defn format-quality-report
  "Format a connection quality report for display"
  [quality-data]
  (let [status-info (quality-score->status (:quality-score quality-data))
        {:keys [emoji status]} status-info]
    (str emoji " " (str/capitalize (name status))
         " | Quality: " (:quality-score quality-data) "%"
         (when (:latency quality-data)
           (str " | Ping: " (int (:latency quality-data)) "ms"))
         (when (:packet-loss quality-data)
           (str " | Loss: " (:packet-loss quality-data) "%"))
         (when (:signal-strength quality-data)
           (str " | Signal: " (:signal-strength quality-data) "dBm")))))

(defn should-switch?
  "Determine if we should switch connections based on quality"
  [current-quality current-config other-configs switch-config]
  (let [current-score (:quality-score current-quality)
        min-quality (:min-quality current-config 50)
        hysteresis (:hysteresis switch-config 60)]
    (cond
      ;; Current connection is below minimum quality
      (< current-score min-quality)
      {:should-switch true
       :reason (str "Quality below minimum (" current-score "% < " min-quality "%)")
       :target :any}
      
      ;; Higher priority connection available with good quality
      (seq (filter #(and (< (:priority %) (:priority current-config))
                        (>= (:quality-score (check-connection-quality %)) 
                            (+ current-score hysteresis)))
                  other-configs))
      {:should-switch true
       :reason "Higher priority connection available"
       :target (first (sort-by :priority other-configs))}
      
      ;; Stay on current connection
      :else
      {:should-switch false
       :reason "Current connection is stable"
       :target nil})))

(defn monitor-loop
  "Main monitoring loop - checks connection quality periodically"
  [config state-atom]
  (let [{:keys [connections monitoring switching]} config
        {:keys [check-interval]} monitoring
        all-conns (vals connections)]
    (loop []
      (let [active-conn-name (nmcli/get-active-connection)
            active-conn-config (first (filter #(= (:name %) active-conn-name) all-conns))]
        
        (when active-conn-config
          (let [quality (check-connection-quality active-conn-config)
                other-conns (remove #(= (:name %) active-conn-name) all-conns)
                switch-decision (should-switch? quality active-conn-config other-conns switching)]
            
            ;; Update state
            (swap! state-atom assoc
                   :current-connection active-conn-name
                   :current-quality quality
                   :last-check (java.time.Instant/now))
            
            ;; Log quality
            (println (str "[" (java.time.LocalDateTime/now) "] "
                         active-conn-name ": "
                         (format-quality-report quality)))
            
            ;; Handle switching if needed
            (when (and (:auto-switch switching)
                      (:should-switch switch-decision))
              (println (str "🔄 Switch recommended: " (:reason switch-decision)))
              (swap! state-atom update :switch-recommendations (fnil conj []) switch-decision))))
        
        ;; Sleep until next check
        (Thread/sleep (* check-interval 1000))
        (recur)))))

