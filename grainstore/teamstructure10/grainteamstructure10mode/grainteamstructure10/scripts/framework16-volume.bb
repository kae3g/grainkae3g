#!/usr/bin/env bb

;; Framework 16 Volume Control Script
;; Maps F1 (mute), F2 (quieter), F3 (louder) to system volume

(require '[clojure.java.shell :as shell]
         '[clojure.string :as str])

(defn log [message]
  "Log with timestamp"
  (let [timestamp (java.time.LocalDateTime/now)
        formatted-time (.format timestamp (java.time.format.DateTimeFormatter/ofPattern "HH:mm:ss"))]
    (println (str "[" formatted-time "] 🔊 " message))))

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

(defn get-volume-level []
  "Get current volume level (0-100)"
  (let [result (run-command "pactl get-sink-volume @DEFAULT_SINK@ | grep -o '[0-9]*%' | head -1 | sed 's/%//'" :sh true)]
    (if (str/blank? result)
      0
      (Integer/parseInt result))))

(defn get-mute-status []
  "Check if audio is muted"
  (let [result (run-command "pactl get-sink-mute @DEFAULT_SINK@" :sh true)]
    (str/includes? result "yes")))

(defn set-volume [level]
  "Set volume level (0-100)"
  (let [clamped-level (max 0 (min 100 level))]
    (run-command (str "pactl set-sink-volume @DEFAULT_SINK@ " clamped-level "%") :sh true)
    (log (str "Volume set to " clamped-level "%"))))

(defn toggle-mute []
  "Toggle mute status"
  (let [muted (get-mute-status)]
    (run-command (str "pactl set-sink-mute @DEFAULT_SINK@ " (if muted "0" "1")) :sh true)
    (log (str "Mute " (if muted "disabled" "enabled")))))

(defn volume-up []
  "Increase volume by 5%"
  (let [current (get-volume-level)
        new-level (+ current 5)]
    (set-volume new-level)
    (log (str "Volume up: " current "% → " new-level "%"))))

(defn volume-down []
  "Decrease volume by 5%"
  (let [current (get-volume-level)
        new-level (- current 5)]
    (set-volume new-level)
    (log (str "Volume down: " current "% → " new-level "%"))))

(defn show-volume-status []
  "Show current volume status"
  (let [level (get-volume-level)
        muted (get-mute-status)
        status (if muted "MUTED" (str level "%"))]
    (log (str "Volume Status: " status))
    (run-command (str "notify-send -t 2000 'Volume' '" status "'") :sh true)))

(defn handle-volume-key [key]
  "Handle volume key press"
  (case key
    "mute" (do
             (toggle-mute)
             (show-volume-status))
    "down" (do
             (volume-down)
             (show-volume-status))
    "up" (do
           (volume-up)
           (show-volume-status))
    "status" (show-volume-status)
    (log (str "Unknown volume key: " key))))

(defn main []
  "Main entry point"
  (let [args *command-line-args*]
    (case (first args)
      "mute" (handle-volume-key "mute")
      "down" (handle-volume-key "down")
      "up" (handle-volume-key "up")
      "status" (handle-volume-key "status")
      (do
        (log "🔊 Framework 16 Volume Control")
        (log "")
        (log "Usage:")
        (log "  bb framework16-volume.bb mute    # Toggle mute (F1)")
        (log "  bb framework16-volume.bb down    # Volume down (F2)")
        (log "  bb framework16-volume.bb up      # Volume up (F3)")
        (log "  bb framework16-volume.bb status  # Show status")
        (log "")
        (log "Current Status:")
        (show-volume-status)))))

;; Run the main function
(main)

