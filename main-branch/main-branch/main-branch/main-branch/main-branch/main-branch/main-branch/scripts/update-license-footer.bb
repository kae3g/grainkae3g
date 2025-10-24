#!/usr/bin/env bb
;; update-license-footer.bb - Update existing license footers
;;
;; Changes "OR" to "/" and adds kae3g link

(ns update-license-footer
  (:require [babashka.fs :as fs]
            [clojure.string :as str]))

(def old-footer-pattern
  #"\*\*Copyright © 2025 \[kae3g\]\(https://kae3g\.github\.io/12025-10/\)\*\* \| Dual-licensed under \[Apache-2\.0\]\(https://www\.apache\.org/licenses/LICENSE-2\.0\) / \[MIT\]\(https://opensource\.org/licenses/MIT\)")

(def new-footer
  "**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)")

(defn update-footer-in-file [file-path]
  "Update license footer in a markdown file"
  (let [content (slurp (str file-path))]
    (when (re-find old-footer-pattern content)
      (let [updated (str/replace content old-footer-pattern new-footer)]
        (spit (str file-path) updated)
        (println (str "✓ Updated footer in: " (fs/file-name file-path)))
        true))))

(defn update-all-footers []
  "Update license footers in all writings"
  (println "🔄 Updating license footers...")
  (println "   (OR → / and adding kae3g link)\n")
  
  (let [writings-files (fs/glob "writings" "*.md")
        updated-count (reduce (fn [count file]
                                (if (update-footer-in-file file)
                                  (inc count)
                                  count))
                              0
                              writings-files)]
    (println (str "\n✅ Updated " updated-count " files!"))
    (when (zero? updated-count)
      (println "   (All files already have new format)"))))

;; Run if called directly
(when (= *file* (System/getProperty "babashka.file"))
  (update-all-footers))

