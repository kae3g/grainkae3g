#!/usr/bin/env bb

(require '[babashka.fs :as fs]
         '[babashka.process :refer [shell]]
         '[clojure.string :as str])

(defn format-file [file-path]
  (let [file-str (str file-path)]
    (cond
      (or (str/ends-with? file-str ".clj") (str/ends-with? file-str ".cljs"))
      (do
        (println "🔧 Checking" file-str "for long lines...")
        (let [content (slurp file-str)
              lines (str/split-lines content)
              long-lines (filter #(> (count %) 80) lines)]
          (if (empty? long-lines)
            (println "✅" file-str "is properly formatted")
            (println "⚠️  Long lines in" file-str ":" (count long-lines) "lines > 80 chars"))))
      
      (str/ends-with? file-str ".edn")
      (do
        (println "📝 Checking" file-str "for long lines...")
        (let [content (slurp file-str)
              lines (str/split-lines content)
              long-lines (filter #(> (count %) 80) lines)]
          (if (empty? long-lines)
            (println "✅" file-str "is properly formatted")
            (println "⚠️  Long lines in" file-str ":" (count long-lines) "lines > 80 chars"))))
      
      (str/ends-with? file-str ".bb")
      (do
        (println "📝 Checking" file-str "for long lines...")
        (let [content (slurp file-str)
              lines (str/split-lines content)
              long-lines (filter #(> (count %) 80) lines)]
          (if (empty? long-lines)
            (println "✅" file-str "is properly formatted")
            (println "⚠️  Long lines in" file-str ":" (count long-lines) "lines > 80 chars"))))
      
      :else
      (println "⚠️  Skipping unsupported file type:" file-str))))

(defn format-all-files []
  (println "🔧 Checking Clojure formatting (80 characters)...")
  (println)
  
  (let [clj-files (fs/glob "." "**/*.clj")
        cljs-files (fs/glob "." "**/*.cljs")
        edn-files (fs/glob "." "**/*.edn")
        bb-files (fs/glob "." "**/*.bb")
        all-files (concat clj-files cljs-files edn-files bb-files)]
    
    (println "📁 Found" (count all-files) "files to check:")
    (doseq [file all-files]
      (println "  •" file))
    (println)
    
    (doseq [file all-files]
      (format-file (str file)))
    
    (println)
    (println "✨ Formatting check complete!")))

(defn main []
  (if (empty? *command-line-args*)
    (format-all-files)
    (let [file-path (first *command-line-args*)]
      (format-file file-path))))

(when (= *file* (System/getProperty "babashka.file"))
  (main))
