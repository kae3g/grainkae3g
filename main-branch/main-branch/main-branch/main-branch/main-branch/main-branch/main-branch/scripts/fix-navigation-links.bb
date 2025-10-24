#!/usr/bin/env bb

(require '[babashka.fs :as fs]
         '[clojure.string :as str])

(defn fix-navigation-link [line]
  "Remove .md extension from navigation link hrefs"
  (-> line
      ;; Pattern: [text](file.md) → [text](file)
      (str/replace #"\]\(([^)]+)\.md\)" "]($1)")))

(defn fix-file-links [file-path]
  (let [content (slurp file-path)
        lines (str/split-lines content)
        ;; Find and fix navigation lines
        fixed-lines (map (fn [line]
                          (if (or (str/includes? line "**Next Writing:**")
                                  (str/includes? line "**Previous Writing:**"))
                            (fix-navigation-link line)
                            line))
                        lines)
        new-content (str/join "\n" fixed-lines)]
    
    (when (not= content new-content)
      (spit file-path new-content)
      (println "✓ Fixed:" (fs/file-name file-path)))))

(defn main []
  (println "🔧 Fixing navigation links (removing .md extensions)...\n")
  
  (let [writings-files (fs/glob "writings" "*.md")]
    (doseq [file writings-files]
      (fix-file-links (str file)))
    
    (println "\n✨ Navigation links fixed!")
    (println "GitHub Pages URLs will now work correctly without .md extension.")))

(when (= *file* (System/getProperty "babashka.file"))
  (main))

