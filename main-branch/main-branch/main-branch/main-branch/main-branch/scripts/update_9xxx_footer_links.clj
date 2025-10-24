#!/usr/bin/env bb

(require '[clojure.java.io :as io]
         '[clojure.string :as str])

(defn find-9xxx-files []
  "Find all 9XXX series markdown files in the hidden publish directory"
  (let [hidden-dir "docs/z-hidden-publish"
        files (file-seq (io/file hidden-dir))
        md-files (filter #(and (.isFile %)
                               (.endsWith (.getName %) ".md")
                               (re-find #"^9\d{3}-" (.getName %)))
                         files)]
    (map #(.getAbsolutePath %) md-files)))

(defn read-file-content [filepath]
  "Read the content of a file"
  (slurp filepath))

(defn write-file-content [filepath content]
  "Write content to a file"
  (spit filepath content))

(defn has-hidden-docs-link? [content]
  "Check if the file already has the hidden docs index link"
  (str/includes? content "hidden-docs-index.html"))

(defn update-footer-links [content]
  "Update the footer navigation to include hidden docs index link"
  (let [hidden-docs-link "*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*"]
    (if (str/includes? content "*[Return to Main Index](/12025-10/)*")
      ;; Replace existing main index link with both links
      (str/replace content 
                   "*[Return to Main Index](/12025-10/)*"
                   hidden-docs-link)
      ;; Add the hidden docs link before any existing footer content
      (if (str/includes? content "---")
        (let [parts (str/split content #"---" 3)
              frontmatter (first parts)
              separator (second parts)
              content-body (nth parts 2)
              ;; Add the link at the end of the content
              updated-content (str content-body "\n\n" hidden-docs-link)]
          (str frontmatter "---" separator "---" updated-content))
        content))))

(defn process-file [filepath]
  "Process a single 9XXX file to add hidden docs link"
  (println (str "Processing: " filepath))
  (let [content (read-file-content filepath)]
    (if (has-hidden-docs-link? content)
      (println "  ✓ Already has hidden docs link")
      (do
        (let [updated-content (update-footer-links content)]
          (write-file-content filepath updated-content)
          (println "  ✓ Added hidden docs link"))))))

(defn main []
  "Main function to update all 9XXX series files"
  (println "🔗 Updating 9XXX series footer links...")
  (let [files (find-9xxx-files)]
    (println (str "Found " (count files) " 9XXX series files"))
    (doseq [file files]
      (try
        (process-file file)
        (catch Exception e
          (println (str "  ❌ Error processing " file ": " (.getMessage e))))))
    (println "✅ Footer link update complete!")))

(main)
