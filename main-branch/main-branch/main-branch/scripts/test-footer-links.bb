#!/usr/bin/env bb

(require '[babashka.fs :as fs]
         '[clojure.string :as str])

(defn check-footer-links [file-path]
  (let [content (slurp file-path)
        lines (str/split-lines content)
        footer-lines (filter #(or (str/includes? % "[Previous:")
                                  (str/includes? % "[Next:")
                                  (str/includes? % "[Latest:")
                                  (str/includes? % "[View All")
                                  (str/includes? % "[About"))
                            lines)]
    
    (when (seq footer-lines)
      (println (str "\n📄 " (fs/file-name file-path) ":"))
      (doseq [line footer-lines]
        (let [has-absolute-path? (str/includes? line "/12025-10/")
              status (if has-absolute-path? "✅" "❌")]
          (println (str "  " status " " line)))))))

(defn main []
  (println "🔍 Checking footer navigation links for absolute paths...")
  (let [writings-files (fs/glob "writings" "*.md")]
    (doseq [file writings-files]
      (check-footer-links (str file)))
    
    (println "\n✨ Footer link check complete!")
    (println "\nAll footer links should use absolute paths with /12025-10/ base path.")))

(when (= *file* (System/getProperty "babashka.file"))
  (main))
