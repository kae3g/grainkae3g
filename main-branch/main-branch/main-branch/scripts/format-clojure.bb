#!/usr/bin/env bb

(require '[babashka.fs :as fs]
         '[babashka.process :refer [shell]]
         '[clojure.string :as str])

(defn format-clojure-file [file-path]
  "Format a single Clojure file using clj-kondo"
  (try
    (let [result (shell {:continue true :out :string :err :string}
                        "clj-kondo" "--lint" file-path "--config" ".clj-kondo/config.edn")]
      (if (str/includes? (:err result) "long-lines")
        (do
          (println "⚠️  Long lines detected in" file-path)
          (println "   Consider manual formatting for lines > 80 chars")
          false)
        (do
          (println "✅" file-path "is properly formatted")
          true)))
    (catch Exception e
      (println "❌ Error formatting" file-path ":" (.getMessage e))
      false)))

(defn format-edn-file [file-path]
  "Format an EDN file by checking line length"
  (let [content (slurp file-path)
        lines (str/split-lines content)
        long-lines (filter #(> (count %) 80) lines)]
    (if (empty? long-lines)
      (do
        (println "✅" file-path "is properly formatted")
        true)
      (do
        (println "⚠️  Long lines detected in" file-path ":" (count long-lines) "lines > 80 chars")
        (doseq [line (take 3 long-lines)]
          (println "   " (subs line 0 (min 100 (count line))) "..."))
        false))))

(defn format-bb-file [file-path]
  "Format a Babashka file by checking line length"
  (let [content (slurp file-path)
        lines (str/split-lines content)
        long-lines (filter #(> (count %) 80) lines)]
    (if (empty? long-lines)
      (do
        (println "✅" file-path "is properly formatted")
        true)
      (do
        (println "⚠️  Long lines detected in" file-path ":" (count long-lines) "lines > 80 chars")
        (doseq [line (take 3 long-lines)]
          (println "   " (subs line 0 (min 100 (count line))) "..."))
        false))))

(defn format-markdown-code-blocks [file-path]
  "Check and report on Clojure code blocks in markdown files"
  (let [content (slurp file-path)
        lines (str/split-lines content)
        in-clojure-block? (atom false)
        clojure-lines (atom [])
        current-block (atom [])]
    
    (doseq [[idx line] (map-indexed vector lines)]
      (cond
        (str/starts-with? line "```clojure")
        (do
          (reset! in-clojure-block? true)
          (reset! current-block []))
        
        (str/starts-with? line "```edn")
        (do
          (reset! in-clojure-block? true)
          (reset! current-block []))
        
        (str/starts-with? line "```bb")
        (do
          (reset! in-clojure-block? true)
          (reset! current-block []))
        
        (and @in-clojure-block? (str/starts-with? line "```"))
        (do
          (when (seq @current-block)
            (swap! clojure-lines concat @current-block))
          (reset! in-clojure-block? false)
          (reset! current-block []))
        
        @in-clojure-block?
        (swap! current-block conj line)))
    
    (let [long-lines (filter #(> (count %) 80) @clojure-lines)]
      (if (empty? long-lines)
        (do
          (println "✅" file-path "Clojure code blocks are properly formatted")
          true)
        (do
          (println "⚠️  Long lines in Clojure code blocks in" file-path ":" (count long-lines) "lines > 80 chars")
          (doseq [line (take 3 long-lines)]
            (println "   " (subs line 0 (min 100 (count line))) "..."))
          false)))))

(defn format-all-clojure-files []
  "Format all Clojure-related files"
  (println "🔧 Formatting all Clojure-related files to 80 characters...")
  (println)
  
  (let [clj-files (fs/glob "." "**/*.clj")
        cljs-files (fs/glob "." "**/*.cljs")
        edn-files (fs/glob "." "**/*.edn")
        bb-files (fs/glob "." "**/*.bb")
        md-files (fs/glob "." "**/*.md")]
    
    (println "📁 Found files:")
    (println "  • Clojure files:" (count clj-files))
    (println "  • ClojureScript files:" (count cljs-files))
    (println "  • EDN files:" (count edn-files))
    (println "  • Babashka files:" (count bb-files))
    (println "  • Markdown files:" (count md-files))
    (println)
    
    (let [clj-results (map format-clojure-file clj-files)
          cljs-results (map format-clojure-file cljs-files)
          edn-results (map format-edn-file edn-files)
          bb-results (map format-bb-file bb-files)
          md-results (map format-markdown-code-blocks md-files)
          
          all-results (concat clj-results cljs-results edn-results bb-results md-results)
          success-count (count (filter true? all-results))
          total-count (count all-results)]
      
      (println)
      (println "📊 Formatting Results:")
      (println "  • Successfully formatted:" success-count "/" total-count)
      (println "  • Files with long lines:" (- total-count success-count))
      
      (if (= success-count total-count)
        (do
          (println "✨ All files are properly formatted!")
          (System/exit 0))
        (do
          (println "⚠️  Some files need manual formatting for lines > 80 characters")
          (System/exit 1))))))

(defn main []
  (if (empty? *command-line-args*)
    (format-all-clojure-files)
    (let [file-path (first *command-line-args*)]
      (cond
        (str/ends-with? file-path ".clj") (format-clojure-file file-path)
        (str/ends-with? file-path ".cljs") (format-clojure-file file-path)
        (str/ends-with? file-path ".edn") (format-edn-file file-path)
        (str/ends-with? file-path ".bb") (format-bb-file file-path)
        (str/ends-with? file-path ".md") (format-markdown-code-blocks file-path)
        :else (println "❌ Unsupported file type:" file-path)))))

(when (= *file* (System/getProperty "babashka.file"))
  (main))
