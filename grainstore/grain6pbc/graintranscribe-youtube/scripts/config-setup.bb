#!/usr/bin/env bb
;;; Setup personal configuration from template

(require '[babashka.fs :as fs])

(println "🌾 Graintranscribe-YouTube Configuration Setup")
(println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n")

(let [template-path "template/config.edn"
      personal-path "personal/config.edn"]
  
  (if (fs/exists? personal-path)
    (do
      (println "⚠️  Personal configuration already exists:")
      (println "   " personal-path)
      (println)
      (print "Overwrite? (y/N): ")
      (flush)
      (let [response (str/trim (read-line))]
        (when (= (str/lower-case response) "y")
          (fs/copy template-path personal-path {:replace-existing true})
          (println "✅ Personal configuration reset from template"))))
    
    (do
      (fs/copy template-path personal-path)
      (println "✅ Created personal configuration:")
      (println "   " personal-path)))
  
  (println)
  (println "📝 Next steps:")
  (println "   1. Edit: " personal-path)
  (println "   2. Replace YOUR_GEMINI_API_KEY with actual key")
  (println "   3. Get API key from: https://aistudio.google.com/apikey")
  (println "   4. Run: bb config:validate")
  (println)
  (println "🌾 Template/Personal separation: personal/ is gitignored for security"))
