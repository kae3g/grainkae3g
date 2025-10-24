#!/usr/bin/env bb

(require '[babashka.fs :as fs]
         '[clojure.string :as str])

(defn wrap-line [line max-length]
  (if (<= (count line) max-length)
    line
    (let [indent (re-find #"^\s*" line)
          content (str/trim line)
          words (str/split content #"\s+")]
      (loop [result []
             current-line indent
             remaining words]
        (if (empty? remaining)
          (str/join "\n" (filter seq result))
          (let [word (first remaining)
                test-line (if (empty? current-line)
                           word
                           (str current-line " " word))]
            (if (<= (count test-line) max-length)
              (recur result test-line (rest remaining))
              (recur (conj result current-line) (str indent word) (rest remaining))))))))

(defn reformat-content [content]
  (let [lines (str/split-lines content)
        wrapped-lines (map #(wrap-line % 80) lines)]
    (str/join "\n" wrapped-lines)))

(defn reformat-file [file-path]
  (let [content (slurp file-path)
        ext (fs/extension file-path)]
    (try
      (cond
        (or (= ext ".clj") (= ext ".cljs") (= ext ".edn") (= ext ".bb"))
        (let [reformatted (reformat-content content)]
          (spit file-path reformatted)
          (println "✅ Reformatted" file-path))
        
        (= ext ".md")
        (let [reformatted (reformat-content content)]
          (spit file-path reformatted)
          (println "✅ Reformatted" file-path))
        
        :else
        (println "⚠️  Skipping unsupported file type:" file-path))
      (catch Exception e
        (println "❌ Error reformatting" file-path ":" (.getMessage e))))))

(defn reformat-all-files []
  (println "🔧 Reformatting all Clojure-related files to 80 characters...")
  (println)
  
  (let [clj-files (fs/glob "." "**/*.clj")
        cljs-files (fs/glob "." "**/*.cljs")
        edn-files (fs/glob "." "**/*.edn")
        bb-files (fs/glob "." "**/*.bb")
        md-files (fs/glob "." "**/*.md")
        all-files (concat clj-files cljs-files edn-files bb-files md-files)]
    
    (println "📁 Found" (count all-files) "files to reformat:")
    (doseq [file all-files]
      (println "  •" file))
    (println)
    
    (doseq [file all-files]
      (reformat-file (str file)))
    
    (println)
    (println "✨ Reformatting complete!"))

(defn main []
  (if (empty? *command-line-args*)
    (reformat-all-files)
    (let [file-path (first *command-line-args*)]
      (reformat-file file-path))))

(when (= *file* (System/getProperty "babashka.file"))
  (main))