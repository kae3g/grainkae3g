#!/usr/bin/env bb
;; add-license-footer.bb - Add license footer to all essays
;;
;; This script adds a subtle license footer to all markdown files

(ns add-license-footer
  (:require [babashka.fs :as fs]
            [clojure.string :as str]))

(def license-footer
  "

---

<div style=\"text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);\">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>
")

(defn has-license-footer? [content]
  "Check if file already has license footer"
  (str/includes? content "Copyright © 2025 kae3g"))

(defn add-footer-to-file [file-path]
  "Add license footer to a markdown file"
  (let [content (slurp (str file-path))]
    (when-not (has-license-footer? content)
      (let [updated (str content license-footer)]
        (spit (str file-path) updated)
        (println (str "✓ Added footer to: " (fs/file-name file-path)))
        true))))

(defn process-all-writings []
  "Add license footer to all writings"
  (println "📜 Adding license footer to all essays...")
  (println "   (Copyright © 2025 kae3g | Apache-2.0 OR MIT)\n")
  
  (let [writings-files (fs/glob "writings" "*.md")
        updated-count (reduce (fn [count file]
                                (if (add-footer-to-file file)
                                  (inc count)
                                  count))
                              0
                              writings-files)]
    (println (str "\n✅ Updated " updated-count " files with license footer!"))
    (when (zero? updated-count)
      (println "   (All files already have footer)"))))

;; Run if called directly
(when (= *file* (System/getProperty "babashka.file"))
  (process-all-writings))

