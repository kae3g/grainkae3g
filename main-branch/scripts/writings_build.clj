#!/usr/bin/env bb
;; writings_build.clj — Enhanced Markdown writings → JSON for SvelteKit
;; Part of the kae3g Sacred Economics & Technical Writings pipeline
;; Pure Clojure pipeline with validation, error handling, and watch mode

(ns writings-build
  (:require [babashka.fs :as fs]
            [babashka.process :as proc]
            [clojure.string :as str]
            [clojure.edn :as edn]
            [clj-yaml.core :as yaml]
            [markdown.core :as md]
            [cheshire.core :as json]))

;; Configuration
(def writings-dir "writings")
(def docs-dir "docs")
(def content-out-dir "web-app/static/content")

;; Validation and error handling
(defn validate-file [file]
  (when-not (fs/exists? file)
    (throw (ex-info (str "File does not exist: " file) {:file file})))
  (when-not (str/ends-with? (str file) ".md")
    (throw (ex-info (str "Not a markdown file: " file) {:file file})))
  file)

(defn safe-slurp [file]
  (try
    (slurp (str file))
    (catch Exception e
      (throw (ex-info (str "Failed to read file: " file) {:file file :error e})))))

(defn safe-spit [file content]
  (try
    (spit file content)
    (catch Exception e
      (throw (ex-info (str "Failed to write file: " file) {:file file :error e})))))

;; Parse front matter from Markdown
(defn parse-front-matter [content]
  (let [lines (str/split-lines content)]
    (if (= "---" (first lines))
      (let [end-idx (or (first
                          (keep-indexed
                            (fn [idx line]
                              (when (and (> idx 0)
                                      (= "---" line))
                                idx))
                            lines))
                     0)
            front-matter-lines (subvec (vec lines) 1 end-idx)
            body-lines (subvec (vec lines) (inc end-idx))]
        {:front-matter (yaml/parse-string
                         (str/join "\n" front-matter-lines))
         :body (str/join "\n" body-lines)})
      {:front-matter {}
       :body content})))

;; Extract slug from filename or front matter
(defn extract-slug [filename front-matter]
  (or (:slug front-matter)
      (-> filename
          (str/replace #"\.md$" "")
          (str/lower-case))))

;; Extract metadata from front matter and content
(defn extract-metadata [filename front-matter body]
  (let [first-line (first (str/split-lines body))
        title (or (:title front-matter)
                  (when (str/starts-with? first-line "#")
                    (str/replace first-line #"^#\s+" ""))
                  filename)]
    (merge
      {:slug (extract-slug filename front-matter)
       :title title
       :filename filename}
      (select-keys front-matter
        [:timestamp :series :category :reading-time
         :author-voice :format :tags :sort-order :archived]))))

;; Process single markdown file with validation
(defn process-markdown-file [file]
  (try
    (let [filename (str (fs/file-name file))
          content (safe-slurp file)
          {:keys [front-matter body]} (parse-front-matter content)
          metadata (extract-metadata filename front-matter body)
          html (md/md-to-html-string body)]
      {:slug (:slug metadata)
       :meta metadata
       :html html})
    (catch Exception e
      (println (str "❌ Error processing " file ": " (.getMessage e)))
      (throw e))))

;; Build content index with four-tier sorting:
;; 1. Non-numeric essays first (sorted by manual sort-order field)
;; 2. Current numeric essays second (sorted by essay number from title)
;; 3. Experimental drafts third (from a-publish-experimental/, sorted by number)
;; 4. Archived essays last (from a-publish-archive/, unsorted - preserved history)
(defn build-content-index [entries]
  (let [meta-entries (map :meta entries)
        ;; Extract essay number from title
        get-essay-num (fn [entry]
                        (when-let [title (:title entry)]
                          (when-let [match (re-find #"^(?:kae3g\s+)?z?(\d+)" title)]
                            (Integer/parseInt (second match)))))
        ;; Categorize by source directory (stored in :source-dir metadata)
        categorized (reduce (fn [acc entry]
                             (cond
                               ;; Hidden: from z-hidden-publish/ (unlisted but built)
                               (= (:source-dir entry) "hidden")
                               (update acc :hidden conj entry)
                               
                               ;; Experimental: from a-publish-experimental/
                               (= (:source-dir entry) "experimental")
                               (update acc :experimental conj entry)
                               
                               ;; Archived: from a-publish-archive/
                               (= (:source-dir entry) "archived")
                               (update acc :archived conj entry)
                               
                               ;; Numeric: from writings/ with number
                               (some? (get-essay-num entry))
                               (update acc :numeric conj entry)
                               
                               ;; Non-numeric: from writings/ without number
                               :else
                               (update acc :non-numeric conj entry)))
                           {:non-numeric [] :numeric [] :experimental [] :archived [] :hidden []}
                           meta-entries)
        ;; Sort non-numeric by sort-order (or title if no sort-order)
        sorted-non-numeric (->> (:non-numeric categorized)
                                (sort-by (fn [entry]
                                          [(or (:sort-order entry) 999)
                                           (:title entry)]))
                                vec)
        ;; Sort numeric by essay number (ascending - lowest first)
        sorted-numeric (->> (:numeric categorized)
                           (sort-by get-essay-num)
                           vec)
        ;; Sort experimental by essay number (ascending - lowest first)
        sorted-experimental (->> (:experimental categorized)
                                (sort-by get-essay-num)
                                vec)
        ;; Archived essays: NO sorting - preserve original order (history)
        unsorted-archived (vec (:archived categorized))
        ;; Hidden essays: built but unlisted (not included in main index)
        hidden-essays (vec (:hidden categorized))]
    ;; Combine: non-numeric first, then numeric, then experimental, then archived
    ;; Note: hidden essays are built but not included in the main index
    (vec (concat sorted-non-numeric sorted-numeric sorted-experimental unsorted-archived))))

;; Enhanced main build function with validation and statistics
(defn build-writings []
  (println "📖 Building kae3g writings content...")
  (println "🔍 Scanning for markdown files...")
  
  ;; Ensure output directory exists
  (fs/create-dirs content-out-dir)
  
  ;; Find and validate markdown files (writings only)
  (let [writings-files (fs/glob writings-dir "*.md")
        experimental-files (fs/glob "docs/a-publish-experimental" "*.md")
        archived-files (fs/glob "docs/a-publish-archive" "*.md")
        hidden-files (fs/glob "docs/z-hidden-publish" "*.md")
        ;; Tag files with their source directory
        tagged-files (vec (concat
                           (map #(hash-map :file % :source-dir "writings") writings-files)
                           (map #(hash-map :file % :source-dir "experimental") experimental-files)
                           (map #(hash-map :file % :source-dir "archived") archived-files)
                           (map #(hash-map :file % :source-dir "hidden") hidden-files)))]
    
    (println (str "📁 Found " (count writings-files) " files in writings/"))
    (println (str "📁 Found " (count experimental-files) " experimental drafts in docs/a-publish-experimental/"))
    (println (str "📁 Found " (count archived-files) " archived essays in docs/a-publish-archive/"))
    (println (str "📁 Found " (count hidden-files) " hidden essays in docs/z-hidden-publish/"))
    (println (str "📝 Processing " (count tagged-files) " total files...\n"))
    
    (when (empty? tagged-files)
      (println "⚠️  No markdown files found!")
      (System/exit 0))
    
    ;; Process all markdown files with error handling
    (let [entries (atom [])
          errors (atom [])]
      
      (doseq [tagged tagged-files]
        (try
          (let [entry (process-markdown-file (:file tagged))]
            ;; Add source-dir and unlisted flag to metadata
            (let [updated-meta (-> (:meta entry)
                                 (assoc :source-dir (:source-dir tagged))
                                 (assoc :unlisted (= (:source-dir tagged) "hidden")))]
              (swap! entries conj (assoc entry :meta updated-meta))))
          (catch Exception e
            (swap! errors conj {:file (:file tagged) :error e}))))
      
      ;; Report errors
      (when (seq @errors)
        (println "❌ Errors encountered:")
        (doseq [{:keys [file error]} @errors]
          (println (str "  • " file ": " (.getMessage error))))
        (println ""))
      
      ;; Write individual content files
      (println "💾 Writing JSON files...")
      (doseq [{:keys [slug] :as entry} @entries]
        (let [out-file (str content-out-dir "/" slug ".json")]
          (safe-spit out-file (json/generate-string entry {:pretty true}))
          (println "  ✓" slug)))
      
      ;; Write index (filter out unlisted entries)
      (let [listed-entries (filter #(not (:unlisted (:meta %))) @entries)
            index (build-content-index listed-entries)
            index-file (str content-out-dir "/index.json")]
        (safe-spit index-file (json/generate-string index {:pretty true}))
        (println "\n📚 Index created with" (count listed-entries) "listed entries"))
      
      ;; Build statistics
      (let [total-files (count tagged-files)
            successful (count @entries)
            failed (count @errors)]
        (println "\n📊 Build Statistics:")
        (println (str "  • Total files: " total-files))
        (println (str "  • Successful: " successful))
        (println (str "  • Failed: " failed))
        (println (str "  • Success rate: " (if (> total-files 0) 
                                            (Math/round (* 100.0 (/ successful total-files))) 
                                            0) "%")))
      
      (println "\n✨ Build complete!\n"))))

;; Run if called directly
(when (= *file* (System/getProperty "babashka.file"))
  (build-writings))

