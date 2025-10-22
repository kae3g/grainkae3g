#!/usr/bin/env bb

(ns update-links-to-codeberg
  (:require [babashka.fs :as fs]
            [clojure.string :as str]))

(def github-patterns
  [#"https://kae3g\.github\.io/12025-10"
   #"https://github\.com/kae3g/12025-10"
   #"github\.io/12025-10"
   #"github\.com/kae3g/12025-10"])

(def codeberg-replacements
  {"https://kae3g.codeberg.page/12025-10" "https://kae3g.codeberg.page/12025-10"
   "https://codeberg.org/kae3g/12025-10" "https://codeberg.org/kae3g/12025-10"
   "codeberg.page/12025-10" "codeberg.page/12025-10"
   "codeberg.org/kae3g/12025-10" "codeberg.org/kae3g/12025-10"})

(defn replace-github-with-codeberg [content]
  (reduce (fn [text [old-url new-url]]
            (str/replace text old-url new-url))
          content
          codeberg-replacements))

(defn process-file [file]
  (let [file-path (str file)
        content (slurp file-path)
        updated (replace-github-with-codeberg content)]
    (when (not= content updated)
      (println (str "🔧 Updating: " (fs/file-name file)))
      (spit file-path updated)
      true)))

(defn -main []
  (println "🔧 Mechanical Link Update: GitHub → Codeberg")
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
  
  (let [directories ["writings" "docs" "web-app/src" "scripts" "."]
        extensions [".md" ".clj" ".bb" ".svelte" ".js" ".ts" ".yml" ".yaml" ".edn"]
        
        files (for [dir directories
                    ext extensions
                    file (fs/glob dir (str "**/*" ext))]
                file)
        
        updated-files (atom 0)]
    
    (doseq [file files]
      (when (process-file file)
        (swap! updated-files inc)))
    
    (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    (println (str "✅ Updated " @updated-files " files"))
    (println "")
    (println "🌐 All links now point to Codeberg!")
    (println "   Pages: https://kae3g.codeberg.page/12025-10")
    (println "   Repo:  https://codeberg.org/kae3g/12025-10")))

(-main)
