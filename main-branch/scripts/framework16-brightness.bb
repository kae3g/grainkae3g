#!/usr/bin/env bb

;; Framework 16 Brightness Control Script
;; Maps F7 (dimmer), F8 (brighter) to system brightness

(require '[clojure.java.shell :as shell]
         '[clojure.string :as str])

(defn log [message]
  "Log with timestamp"
  (let [timestamp (java.time.LocalDateTime/now)
        formatted-time (.format timestamp (java.time.format.DateTimeFormatter/ofPattern "HH:mm:ss"))]
    (println (str "[" formatted-time "] 💡 " message))))

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

(defn get-brightness-level []
  "Get current brightness level (0-100)"
  (let [result (run-command "brightnessctl get" :sh true)]
    (if (str/blank? result)
      50
      (let [raw-level (Integer/parseInt result)
            max-brightness (Integer/parseInt (run-command "brightnessctl max" :sh true))
            percentage (int (* 100 (/ raw-level max-brightness)))]
        percentage))))

(defn set-brightness [level]
  "Set brightness level (0-100)"
  (let [clamped-level (max 1 (min 100 level))
        raw-level (int (* (/ clamped-level 100.0) (Integer/parseInt (run-command "brightnessctl max" :sh true))))]
    (run-command (str "brightnessctl set " raw-level) :sh true)
    (log (str "Brightness set to " clamped-level "%"))))

(defn brightness-up []
  "Increase brightness by 10%"
  (let [current (get-brightness-level)
        new-level (+ current 10)]
    (set-brightness new-level)
    (log (str "Brightness up: " current "% → " new-level "%"))))

(defn brightness-down []
  "Decrease brightness by 10%"
  (let [current (get-brightness-level)
        new-level (- current 10)]
    (set-brightness new-level)
    (log (str "Brightness down: " current "% → " new-level "%"))))

(defn show-brightness-status []
  "Show current brightness status"
  (let [level (get-brightness-level)]
    (log (str "Brightness Status: " level "%"))
    (run-command (str "notify-send -t 2000 'Brightness' '" level "%'") :sh true)))

(defn handle-brightness-key [key]
  "Handle brightness key press"
  (case key
    "down" (do
             (brightness-down)
             (show-brightness-status))
    "up" (do
           (brightness-up)
           (show-brightness-status))
    "status" (show-brightness-status)
    (log (str "Unknown brightness key: " key))))

(defn main []
  "Main entry point"
  (let [args *command-line-args*]
    (case (first args)
      "down" (handle-brightness-key "down")
      "up" (handle-brightness-key "up")
      "status" (handle-brightness-key "status")
      (do
        (log "💡 Framework 16 Brightness Control")
        (log "")
        (log "Usage:")
        (log "  bb framework16-brightness.bb down    # Brightness down (F7)")
        (log "  bb framework16-brightness.bb up      # Brightness up (F8)")
        (log "  bb framework16-brightness.bb status  # Show status")
        (log "")
        (log "Current Status:")
        (show-brightness-status)))))

;; Run the main function
(main)

