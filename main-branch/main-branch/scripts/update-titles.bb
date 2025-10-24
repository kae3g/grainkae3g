#!/usr/bin/env bb

(require '[babashka.fs :as fs]
         '[clojure.string :as str])

(defn update-title [file-path]
  (let [content (slurp file-path)
        lines (str/split-lines content)
        first-line (first lines)
        rest-lines (rest lines)]
    
    
    (if (and (str/starts-with? first-line "# ")
             (re-find #"^\d{4}[a-z]*:" (subs first-line 2)))
      (let [updated-first-line (str/replace first-line #"^# (\d{4}[a-z]*:)" "# kae3g $1")
            updated-content (str/join "\n" (cons updated-first-line rest-lines))]
        (spit file-path updated-content)
        (println "Updated:" file-path))
      (println "Skipped (no title pattern):" file-path))))

(defn main []
  (let [writings-dir "writings"
        files (fs/glob writings-dir "*.md")]
    (println "Updating titles in" (count files) "writing files...")
    (doseq [file files]
      (update-title (str file)))
    (println "Done!")))

(main)
