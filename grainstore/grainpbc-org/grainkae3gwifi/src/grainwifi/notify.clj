(ns grainwifi.notify
  "Desktop notifications for GrainWiFi"
  (:require [babashka.process :as p]))

(defn send-notification
  "Send a desktop notification using notify-send"
  [title message & {:keys [urgency icon timeout]
                    :or {urgency "normal"
                         icon "network-wireless"
                         timeout 5000}}]
  (try
    (p/shell {:continue true}
             "notify-send"
             "-u" urgency
             "-i" icon
             "-t" (str timeout)
             title
             message)
    (catch Exception e
      (println "Notification error:" (.getMessage e)))))

(defn notify-connection-change
  "Notify user of connection change"
  [old-conn new-conn reason]
  (let [title "🔄 WiFi Connection Changed"
        message (str "Switched from " old-conn " to " new-conn "\n"
                    "Reason: " reason)]
    (send-notification title message :urgency "normal")))

(defn notify-quality-warning
  "Notify user of connection quality degradation"
  [conn-name quality]
  (let [title "⚠️  Connection Quality Warning"
        message (str conn-name " quality degraded\n"
                    "Quality: " quality "%")]
    (send-notification title message :urgency "normal" :icon "network-wireless-signal-weak")))

(defn notify-connection-lost
  "Notify user that connection was lost"
  [conn-name]
  (let [title "❌ Connection Lost"
        message (str conn-name " is no longer available")]
    (send-notification title message :urgency "critical" :icon "network-wireless-offline")))

(defn notify-connection-restored
  "Notify user that preferred connection is back"
  [conn-name]
  (let [title "✅ Connection Restored"
        message (str conn-name " is available again")]
    (send-notification title message :urgency "low" :icon "network-wireless-signal-excellent")))

(defn notify-starlink
  "Notify user about Starlink-specific events"
  [event quality]
  (case event
    :connected (send-notification "🛰️  Connected to Starlink" 
                                 (str "Quality: " quality "%")
                                 :urgency "low")
    :degraded (send-notification "⚠️  Starlink Quality Degraded" 
                               (str "Quality: " quality "% | Monitoring...")
                               :urgency "normal")
    :lost (send-notification "❌ Starlink Unavailable" 
                            "Switching to cellular backup..."
                            :urgency "normal")
    :restored (send-notification "✅ Starlink Restored" 
                                "Switching back in 60 seconds..."
                                :urgency "low")))

(defn notify-cellular
  "Notify user about cellular-specific events"
  [event quality]
  (case event
    :connected (send-notification "📱 Connected to Cellular" 
                                 (str "Quality: " quality "%")
                                 :urgency "low")
    :degraded (send-notification "⚠️  Cellular Quality Degraded" 
                               (str "Quality: " quality "%")
                               :urgency "normal")
    :lost (send-notification "❌ Cellular Unavailable" 
                            "No backup connection available!"
                            :urgency "critical")
    :restored (send-notification "✅ Cellular Restored" 
                                "Backup connection available"
                                :urgency "low")))

