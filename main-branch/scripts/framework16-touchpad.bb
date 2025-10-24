#!/usr/bin/env bb

;; Framework 16 Touchpad Control Script

(require '[clojure.java.shell :as shell]
         '[clojure.string :as str])

(defn log [message]
  (let [timestamp (java.time.LocalDateTime/now)
        formatted-time (.format timestamp (java.time.format.DateTimeFormatter/ofPattern "HH:mm:ss"))]
    (println (str "[" formatted-time "] 🖱️ " message))))

(defn run-command [cmd & {:keys [sh]}]
  (try
    (let [result (if sh
                   (shell/sh "bash" "-c" cmd)
                   (shell/sh cmd))]
      (if (= (:exit result) 0)
        (str/trim (:out result))
        ""))
    (catch Exception e
      (log (str "Error running command: " cmd " - " (.getMessage e)))
      "")))

(defn get-touchpad-status []
  "Get current touchpad status"
  (let [result (run-command "xinput list-props 'SynPS/2 Synaptics TouchPad' | grep 'Device Enabled' | grep -o '[0-9]*$'" :sh true)]
    (if (str/blank? result)
      true
      (= "1" result))))

(defn toggle-touchpad []
  "Toggle touchpad on/off"
  (let [enabled (get-touchpad-status)]
    (run-command (str "xinput set-prop 'SynPS/2 Synaptics TouchPad' 'Device Enabled' " (if enabled "0" "1")) :sh true)
    (log (str "Touchpad " (if enabled "disabled" "enabled")))
    (run-command (str "notify-send -t 2000 'Touchpad' '" (if enabled "Disabled" "Enabled") "'") :sh true)))

(defn main []
  (let [args *command-line-args*]
    (case (first args)
      "toggle" (toggle-touchpad)
      "status" (log (str "Touchpad: " (if (get-touchpad-status) "Enabled" "Disabled")))
      (do
        (log "🖱️ Framework 16 Touchpad Control")
        (log "")
        (log "Usage:")
        (log "  bb framework16-touchpad.bb toggle  # Toggle touchpad")
        (log "  bb framework16-touchpad.bb status  # Show status")))))

(main)