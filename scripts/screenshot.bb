#!/usr/bin/env bb

(require '[clojure.java.shell :as shell]
         '[clojure.string :as str]
         '[babashka.process :as process])

(defn log [message]
  "Log a message to stdout"
  (println message))

(defn run-command [cmd & {:keys [sh]}]
  "Run a command and return output as string"
  (try
    (if sh
      (-> (shell/sh "bash" "-c" cmd)
          :out
          str/trim)
      (-> (shell/sh cmd)
          :out
          str/trim))
    (catch Exception e
      (log (str "Error running command: " cmd " - " (.getMessage e)))
      "")))

(defn take-screenshot [type]
  "Take a screenshot and copy to clipboard"
  (let [timestamp (str (System/currentTimeMillis))
        filename (str "/tmp/screenshot_" timestamp ".png")]
    
    (case type
      "full" (do
               (log "📸 Taking full screen screenshot...")
               (run-command (str "grim " filename) :sh true)
               (run-command (str "wl-copy < " filename) :sh true)
               (log (str "✅ Full screenshot saved and copied to clipboard: " filename)))
      
      "area" (do
               (log "📸 Taking area screenshot...")
               (run-command (str "grim -g \"$(slurp)\" " filename) :sh true)
               (run-command (str "wl-copy < " filename) :sh true)
               (log (str "✅ Area screenshot saved and copied to clipboard: " filename)))
      
      "window" (do
                 (log "📸 Taking window screenshot...")
                 (run-command (str "grim -g \"$(slurp -d)\" " filename) :sh true)
                 (run-command (str "wl-copy < " filename) :sh true)
                 (log (str "✅ Window screenshot saved and copied to clipboard: " filename)))
      
      (log "❌ Unknown screenshot type. Use: full, area, or window"))))

;; Main execution
(let [args *command-line-args*]
  (if (empty? args)
    (do
      (log "📸 Screenshot Tool")
      (log "Usage: bb screenshot.bb [full|area|window]")
      (log "  full   - Full screen screenshot")
      (log "  area   - Select area to screenshot")
      (log "  window - Select window to screenshot"))
    (take-screenshot (first args))))
