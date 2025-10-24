(ns kae3g.wrapper
  "Markdown text wrapper for 57-char hard wrap following
   aspiring federal documentation standards"
  (:require [clojure.string :as str]
            [babashka.fs :as fs]))

(def max-line-length 57)

(defn wrap-line
  "Wrap a single line to max-line-length
   preserving word boundaries with Divine Grace"
  [line]
  (if (<= (count line) max-line-length)
    [line]
    (loop [remaining line
           result []]
      (if (<= (count remaining) max-line-length)
        (conj result remaining)
        (let [break-point (or
                           ;; Find last space before limit
                           (str/last-index-of
                            (subs remaining 0
                                  max-line-length)
                            " ")
                           ;; No space found, hard break
                           max-line-length)
              current-line (subs remaining 0 break-point)
              next-remaining (str/trim
                              (subs remaining
                                    break-point))]
          (recur next-remaining
                 (conj result current-line)))))))

(defn preserve-special-lines?
  "Check if line should be preserved as-is
   (code blocks, headers, lists, quotes)"
  [line]
  (or (str/starts-with? line "```")
      (str/starts-with? line "#")
      (str/starts-with? line "-")
      (str/starts-with? line "*")
      (str/starts-with? line ">")
      (str/starts-with? line "    ")
      (str/blank? line)))

(defn wrap-paragraph
  "Wrap paragraph text with consciousness"
  [paragraph]
  (if (preserve-special-lines? paragraph)
    [paragraph]
    (wrap-line paragraph)))

(defn wrap-markdown-content
  "Wrap entire markdown content to 57 chars
   with contemplative attention to structure"
  [content]
  (let [lines (str/split-lines content)
        wrapped-lines (mapcat wrap-paragraph lines)]
    (str/join "\n" wrapped-lines)))

(defn wrap-file
  "Wrap a markdown file to 57-char hard wrap"
  [input-path output-path]
  (println "Wrapping markdown:"
           (fs/file-name input-path))
  (let [content (slurp input-path)
        wrapped (wrap-markdown-content content)
        output-dir (fs/parent output-path)]
    (when output-dir (fs/create-dirs output-dir))
    (spit output-path wrapped)
    (println "Wrapped to:" output-path)))

(defn wrap-directory
  "Wrap all markdown files in a directory"
  [input-dir output-dir & {:keys [include-root]}]
  (println "Wrapping directory:" input-dir)
  (fs/create-dirs output-dir)
  (let [pattern (if include-root
                  "**/*.md"
                  "*.md")
        files (fs/glob input-dir pattern)
        filtered-files (if include-root
                         ;; Filter out build and node_modules
                         (filter #(not (or
                                        (str/includes? (str %)
                                          "node_modules")
                                        (str/includes? (str %)
                                          "build")
                                        (str/includes? (str %)
                                          ".git")))
                                 files)
                         files)]
    (println "📂 Found" (count filtered-files)
             "markdown files")
    (doseq [file filtered-files]
      (let [rel-path (fs/relativize input-dir file)
            out-path (fs/path output-dir rel-path)
            out-dir (fs/parent out-path)]
        (when out-dir (fs/create-dirs out-dir))
        (wrap-file (str file) (str out-path))))
    (println "✨ Wrapped" (count filtered-files)
             "files")))

(defn -main
  "Text wrapping entry point"
  [& args]
  (println "Text Wrapper: 57-char hard wrap")
  (let [input (or (first args)
                  "docs/test-document.md")
        output (or (second args)
                   "build/wrapped.md")
        include-root? (some #(= "--include-root" %)
                            args)]
    (if (fs/directory? input)
      (wrap-directory input output
                      :include-root include-root?)
      (wrap-file input output))
    (println "Text wrapping complete")))

