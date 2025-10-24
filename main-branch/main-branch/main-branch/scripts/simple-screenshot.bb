#!/usr/bin/env bb

(require '[clojure.java.shell :as shell])

(defn log [message]
  (println message))

(defn take-screenshot [type]
  (let [timestamp (str (System/currentTimeMillis))
        filename (str "/tmp/screenshot_" timestamp ".png")]
    
    (case type
      "full" (do
               (log "📸 Taking full screen screenshot...")
               (shell/sh "grim" filename)
               (shell/sh "wl-copy" "<" filename)
               (log (str "✅ Full screenshot saved: " filename)))
      
      "area" (do
               (log "📸 Taking area screenshot...")
               (shell/sh "grim" "-g" "$(slurp)" filename)
               (shell/sh "wl-copy" "<" filename)
               (log (str "✅ Area screenshot saved: " filename)))
      
      "window" (do
                 (log "📸 Taking window screenshot...")
                 (shell/sh "grim" "-g" "$(slurp -d)" filename)
                 (shell/sh "wl-copy" "<" filename)
                 (log (str "✅ Window screenshot saved: " filename)))
      
      (log "❌ Unknown type. Use: full, area, or window"))))

;; Main
(let [args *command-line-args*]
  (if (empty? args)
    (do
      (log "📸 Screenshot Tool")
      (log "Usage: bb simple-screenshot.bb [full|area|window]"))
    (take-screenshot (first args))))
