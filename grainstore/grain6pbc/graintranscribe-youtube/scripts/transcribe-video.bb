#!/usr/bin/env bb
;;; Transcribe a single YouTube video using Gemini 2.5 Pro

(load-file "src/graintranscribe/core.clj")
(require '[graintranscribe.core :as gt])

(defn main [& args]
  (if (empty? args)
    (do
      (println "❌ Error: No YouTube URL provided")
      (println "\nUsage: bb transcribe:video <youtube-url>")
      (println "\nExample:")
      (println "  bb transcribe:video https://www.youtube.com/watch?v=gSW3YJ8uyBI")
      (System/exit 1))
    
    (let [video-url (first args)
          result (gt/transcribe-video video-url)]
      (if (:success result)
        (System/exit 0)
        (System/exit 1)))))

(apply main *command-line-args*)
