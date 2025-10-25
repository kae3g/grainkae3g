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
               (let [result (shell/sh "grim" filename)]
                 (if (= (:exit result) 0)
                   (log (str "✅ Full screenshot saved: " filename))
                   (log (str "❌ Error: " (:err result)))))
      
      "area" (do
               (log "📸 Taking area screenshot...")
               (let [result (shell/sh "grim" "-g" "$(slurp)" filename)]
                 (if (= (:exit result) 0)
                   (log (str "✅ Area screenshot saved: " filename))
                   (log (str "❌ Error: " (:err result)))))
      
      "window" (do
                 (log "📸 Taking window screenshot...")
                 (let [result (shell/sh "grim" "-g" "$(slurp -d)" filename)]
                   (if (= (:exit result) 0)
                     (log (str "✅ Window screenshot saved: " filename))
                     (log (str "❌ Error: " (:err result)))))
      
      (log "❌ Unknown type. Use: full, area, or window"))))

;; Main
(let [args *command-line-args*]
  (if (empty? args)
    (do
      (log "📸 Screenshot Tool")
      (log "Usage: bb basic-screenshot.bb [full|area|window]"))
    (take-screenshot (first args))))
