#!/usr/bin/env bb
;; writings_build_incremental.clj — Smart incremental builds
;; Only rebuilds files that have changed since last build
;; Caches metadata for unchanged files for faster builds

(ns writings-build-incremental
  (:require [babashka.fs :as fs]
            [babashka.process :as proc]
            [clojure.string :as str]
            [clojure.edn :as edn]
            [clj-yaml.core :as yaml]
            [markdown.core :as md]
            [cheshire.core :as json]))

;; Configuration
(def writings-dir "writings")
(def content-out-dir "web-app/static/content")
(def cache-file ".build-cache.edn")

;; Cache management
(defn load-cache []
  (if (fs/exists? cache-file)
    (try
      (edn/read-string (slurp cache-file))
      (catch Exception e
        (println "⚠️  Cache corrupted, rebuilding all...")
        {}))
    {}))

(defn save-cache [cache]
  (spit cache-file (pr-str cache)))

(defn file-hash [file]
  "Fast hash: last-modified-time + file-size"
  (let [path (fs/file file)]
    (str (fs/last-modified-time path) "-" (fs/size path))))

(defn get-changed-files [files cache]
  "Return files that have changed since last build"
  (filter
    (fn [file]
      (let [current-hash (file-hash file)
            cached-hash (get-in cache [(str file) :hash])]
        (not= current-hash cached-hash)))
    files))

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

;; Process single markdown file
(defn process-markdown-file [file]
  (try
    (let [filename (str (fs/file-name file))
          content (slurp (str file))
          {:keys [front-matter body]} (parse-front-matter content)
          metadata (extract-metadata filename front-matter body)
          html (md/md-to-html-string body)]
      {:slug (:slug metadata)
       :meta metadata
       :html html
       :hash (file-hash file)})
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
                               
                               ;; Numeric: from writings/ with number
                               (some? (get-essay-num entry))
                               (update acc :numeric conj entry)
                               
                               ;; Non-numeric: from writings/ without number
                               :else
                               (update acc :non-numeric conj entry)))
                           {:non-numeric [] :numeric [] :hidden []}
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
        ;; Hidden essays: unlisted but built
        hidden-essays (vec (:hidden categorized))]
    ;; Combine: non-numeric first, then numeric, then hidden (unlisted)
    (vec (concat sorted-non-numeric sorted-numeric))))

;; Incremental build function
(defn build-writings-incremental []
  (println "🌊 Starting smart incremental build...")
  
  ;; Load cache
  (let [cache (load-cache)
        _ (println "📦 Loaded build cache")
        
        ;; Find all markdown files (writings + experimental drafts)
        writings-files (vec (fs/glob writings-dir "*.md"))
        hidden-files (vec (fs/glob "docs/z-hidden-publish" "*.md"))
        ;; Tag files with their source directory
        tagged-files (vec (concat
                           (map #(hash-map :file % :source-dir "writings") writings-files)
                           (map #(hash-map :file % :source-dir "hidden") hidden-files)))
        all-files (map :file tagged-files)  ; Extract files for change detection
        _ (println (str "📁 Found " (count tagged-files) " files"))
        
        ;; Detect changed files
        changed-files (get-changed-files all-files cache)
        unchanged-count (- (count all-files) (count changed-files))
        
        ;;Create file->source-dir mapping
        file-to-source (into {} (map (fn [t] [(:file t) (:source-dir t)]) tagged-files))
        
        _ (println (str "🔍 Changed: " (count changed-files) 
                       " | Cached: " unchanged-count))]
    
    (when (empty? all-files)
      (println "⚠️  No markdown files found!")
      (System/exit 0))
    
    ;; Ensure output directory exists
    (fs/create-dirs content-out-dir)
    
    ;; Build entries: process changed, load unchanged from cache
    (let [new-cache (atom {})
          entries (atom [])]
      
      ;; Process changed files
      (println "")
      (when (seq changed-files)
        (println "🔧 Rebuilding changed files:")
        (doseq [file changed-files]
          (try
            (let [entry (process-markdown-file file)
                  ;; Add source-dir to metadata
                  source-dir (get file-to-source file "writings")
                  entry-with-source (update entry :meta assoc :source-dir source-dir)
                  out-file (str content-out-dir "/" (:slug entry-with-source) ".json")]
              ;; Write JSON
              (spit out-file (json/generate-string entry-with-source {:pretty true}))
              ;; Update cache
              (swap! new-cache assoc (str file) 
                     {:hash (:hash entry-with-source)
                      :meta (:meta entry-with-source)})
              ;; Add to entries
              (swap! entries conj entry-with-source)
              (println (str "  ✓ " (:slug entry-with-source))))
            (catch Exception e
              (println (str "  ❌ " file ": " (.getMessage e)))))))
      
      ;; Load unchanged files from cache and existing JSON
      (when (pos? unchanged-count)
        (println "")
        (println "📦 Loading cached files:")
        (doseq [file writings-files]
          (when-not (some #(= file %) changed-files)
            (try
              (let [cached-entry (get cache (str file))
                    slug (:slug (:meta cached-entry))
                    json-file (str content-out-dir "/" slug ".json")]
                ;; Verify JSON exists
                (when (fs/exists? json-file)
                  (let [entry (json/parse-string (slurp json-file) true)]
                    ;; Copy cache entry
                    (swap! new-cache assoc (str file) cached-entry)
                    ;; Add to entries
                    (swap! entries conj entry)
                    (println (str "  ✓ " slug " (cached)")))))
              (catch Exception e
                (println (str "  ⚠️  " file " cache miss, will rebuild next time")))))))
      
      ;; Rebuild index (always, since order might change)
      (println "")
      (println "📚 Rebuilding index...")
      (let [index (build-content-index @entries)
            index-file (str content-out-dir "/index.json")]
        (spit index-file (json/generate-string index {:pretty true}))
        (println (str "  ✓ Index with " (count @entries) " entries")))
      
      ;; Save cache
      (save-cache @new-cache)
      
      ;; Statistics
      (println "")
      (println "📊 Build Statistics:")
      (println (str "  • Total files: " (count writings-files)))
      (println (str "  • Rebuilt: " (count changed-files)))
      (println (str "  • Cached: " unchanged-count))
      (println (str "  • Time saved: ~" (* unchanged-count 50) "ms"))
      
      (println "")
      (println "✨ Incremental build complete!"))))

;; Run if called directly
(when (= *file* (System/getProperty "babashka.file"))
  (build-writings-incremental))

