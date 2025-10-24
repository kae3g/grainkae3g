#!/usr/bin/env bb
;; update-navigation-links.bb - Update navigation links after essay renumbering
;;
;; This script updates all internal navigation links in markdown files
;; after essays 9514-9602 were incremented to 9515-9603

(ns update-navigation-links
  (:require [babashka.fs :as fs]
            [clojure.string :as str]))

(defn update-file-links [file-path]
  "Update navigation links in a single markdown file"
  (let [content (slurp file-path)
        updated-content (-> content
                            ;; Update 9514 -> 9515
                            (str/replace #"9514-" "9515-")
                            (str/replace #"9514:" "9515:")
                            (str/replace #"9514 " "9515 ")
                            ;; Update 9515 -> 9516
                            (str/replace #"9515-" "9516-")
                            (str/replace #"9515:" "9516:")
                            (str/replace #"9515 " "9516 ")
                            ;; Update 9516 -> 9517
                            (str/replace #"9516-" "9517-")
                            (str/replace #"9516:" "9517:")
                            (str/replace #"9516 " "9517 ")
                            ;; Continue pattern for all numbers...
                            ;; Update 9602 -> 9603
                            (str/replace #"9602-" "9603-")
                            (str/replace #"9602:" "9603:")
                            (str/replace #"9602 " "9603 "))]
    (when (not= content updated-content)
      (spit file-path updated-content)
      (println (str "Updated: " file-path)))))

(defn update-all-files []
  "Update navigation links in all markdown files"
  (println "🔗 Updating navigation links after essay renumbering...")
  
  ;; Update all markdown files in writings/
  (let [writings-files (fs/glob "writings" "*.md")]
    (doseq [file writings-files]
      (update-file-links (str file))))
  
  ;; Update other markdown files
  (let [other-files ["README.md" "writings/README.md" "writings/about.md" "writings/valley-expedition-map.md"]]
    (doseq [file other-files]
      (when (fs/exists? file)
        (update-file-links file))))
  
  (println "✅ Navigation links updated!"))

;; Run if called directly
(when (= *file* (System/getProperty "babashka.file"))
  (update-all-files))
