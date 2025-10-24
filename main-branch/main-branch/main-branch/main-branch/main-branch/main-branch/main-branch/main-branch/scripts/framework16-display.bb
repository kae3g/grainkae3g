#!/usr/bin/env bb

;; Framework 16 Display Control Script

(require '[clojure.java.shell :as shell]
         '[clojure.string :as str])

(defn log [message]
  (let [timestamp (java.time.LocalDateTime/now)
        formatted-time (.format timestamp (java.time.format.DateTimeFormatter/ofPattern "HH:mm:ss"))]
    (println (str "[" formatted-time "] 🖥️ " message))))

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

(defn get-displays []
  "Get available displays"
  (let [result (run-command "xrandr --query | grep ' connected' | cut -d' ' -f1" :sh true)]
    (str/split-lines result)))

(defn cycle-display []
  "Cycle through display configurations"
  (let [displays (get-displays)
        laptop-display (first displays)
        external-displays (rest displays)]
    
    (if (empty? external-displays)
      (do
        (log "No external displays found")
        (run-command "notify-send -t 2000 'Display' 'No external displays'" :sh true))
      (do
        (log (str "Cycling display configuration..."))
        (run-command (str "xrandr --output " laptop-display " --auto --output " (first external-displays) " --auto --right-of " laptop-display) :sh true)
        (log (str "Display configuration updated"))
        (run-command "notify-send -t 2000 'Display' 'Configuration updated'" :sh true)))))

(defn main []
  (let [args *command-line-args*]
    (case (first args)
      "cycle" (cycle-display)
      "list" (do
                (log "Available displays:")
                (doseq [display (get-displays)]
                  (log (str "  " display))))
      (do
        (log "🖥️ Framework 16 Display Control")
        (log "")
        (log "Usage:")
        (log "  bb framework16-display.bb cycle  # Cycle display config")
        (log "  bb framework16-display.bb list   # List displays")))))

(main)