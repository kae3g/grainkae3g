#!/usr/bin/env bb

(ns publish-course
  "Babashka script to publish course content to all platforms.
   Converts lessons to Grainframes, Grainweb posts, Svelte markdown, and native app data."
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.edn :as edn]
            [babashka.fs :as fs]))

;; =============================================================================
;; Configuration
;; =============================================================================

(def root-dir "/home/xy/kae3g/grainkae3g")
(def course-dir (str root-dir "/docs/course"))
(def svelte-dir (str root-dir "/website/content/lessons"))
(def native-dir (str root-dir "/grainstore/grainweb/data/lessons"))
(def grainweb-dir (str root-dir "/grainstore/grainweb/data/posts"))

(def lessons
  [{:id "lesson-05"
    :title "The Harmony of 80 and 110"
    :file "lessons/lesson-05-the-harmony-of-80-and-110.md"
    :order 5}
   {:id "lesson-06"
    :title "Advanced Type Systems and Networked Data"
    :file "lessons/lesson-06-advanced-type-systems-and-networked-data.md"
    :order 6}])

;; =============================================================================
;; Helper Functions
;; =============================================================================

(defn read-lesson-file
  "Read lesson markdown file."
  [lesson-info]
  (let [file-path (str course-dir "/" (:file lesson-info))]
    (when (fs/exists? file-path)
      (slurp file-path))))

(defn parse-frontmatter
  "Extract YAML/markdown frontmatter."
  [content]
  (let [lines (str/split-lines content)]
    (if (and (>= (count lines) 3) (= "---" (first lines)))
      (let [end-idx (first (keep-indexed #(when (and (> %1 0) (= "---" %2)) %1) lines))]
        (when end-idx
          {:frontmatter (str/join "\n" (subvec (vec lines) 1 end-idx))
           :content (str/join "\n" (drop (inc end-idx) lines))}))
      {:frontmatter nil
       :content content})))

(defn generate-grainframe-metadata
  "Generate Grainframe metadata for a lesson."
  [lesson-info page-num total-pages]
  {:grainframe-id (str (:id lesson-info) "-page-" page-num)
   :grainframe-type :document
   :grainframe-title (str (:title lesson-info)
                         (when (> total-pages 1)
                           (str " (Page " page-num "/" total-pages ")")))
   :grainframe-author "kae3g"
   :grainframe-timestamp (str (java.time.Instant/now))
   :grainframe-tags ["course" "lesson" (:id lesson-info)]
   :grainframe-version "1.0.0"
   :lesson-order (:order lesson-info)
   :page-number page-num
   :total-pages total-pages})

(defn split-into-grainframes
  "Split content into 8800-character Grainframes."
  [content]
  (let [total-chars (count content)
        grainframe-size 8800
        total-pages (int (Math/ceil (/ total-chars grainframe-size)))]
    (for [i (range total-pages)]
      (let [start (* i grainframe-size)
            end (min (* (inc i) grainframe-size) total-chars)]
        {:page-num (inc i)
         :total-pages total-pages
         :content (subs content start end)}))))

;; =============================================================================
;; Publishing Functions
;; =============================================================================

(defn publish-to-svelte
  "Publish lesson to Svelte website as markdown."
  [lesson-info content-pages]
  (doseq [{:keys [page-num total-pages content]} content-pages]
    (let [metadata (generate-grainframe-metadata lesson-info page-num total-pages)
          markdown (str "---\n"
                       "title: " (pr-str (:grainframe-title metadata)) "\n"
                       "author: " (pr-str (:grainframe-author metadata)) "\n"
                       "date: " (:grainframe-timestamp metadata) "\n"
                       "tags: " (pr-str (:grainframe-tags metadata)) "\n"
                       "type: document\n"
                       "lesson_order: " (:lesson-order metadata) "\n"
                       "page_number: " (:page-number metadata) "\n"
                       "total_pages: " (:total-pages metadata) "\n"
                       "layout: lesson\n"
                       "---\n\n"
                       content)
          filename (str (:id lesson-info) "-page-" page-num ".md")
          output-path (str svelte-dir "/" filename)]
      (io/make-parents output-path)
      (spit output-path markdown)
      (println "  📄 Svelte:" filename))))

(defn publish-to-native
  "Publish lesson to native app as EDN."
  [lesson-info content-pages]
  (doseq [{:keys [page-num total-pages content]} content-pages]
    (let [metadata (generate-grainframe-metadata lesson-info page-num total-pages)
          edn-data {:grainframe {:grainframe-id (:grainframe-id metadata)
                                :grainframe-content content
                                :grainframe-metadata metadata}
                   :clay-beam {:clay-ship "kae3g"
                              :clay-desk "course"
                              :clay-revision "1.0.0"
                              :clay-path ["lessons" (:id lesson-info) (str "page-" page-num ".edn")]}}
          filename (str (:id lesson-info) "-page-" page-num ".edn")
          output-path (str native-dir "/" filename)]
      (io/make-parents output-path)
      (spit output-path (pr-str edn-data))
      (println "  💾 Native:" filename))))

(defn publish-to-grainweb
  "Publish lesson to Grainweb as post data."
  [lesson-info content-pages]
  (let [posts (for [{:keys [page-num total-pages content]} content-pages]
                (let [metadata (generate-grainframe-metadata lesson-info page-num total-pages)]
                  {:post/id (:grainframe-id metadata)
                   :post/title (:grainframe-title metadata)
                   :post/content content
                   :post/type :document
                   :post/timestamp (:grainframe-timestamp metadata)
                   :post/author (:grainframe-author metadata)
                   :post/tags (:grainframe-tags metadata)
                   :post/visibility :public
                   :post/grain-post true
                   :post/status (str "lesson-" (:lesson-order metadata))
                   :post/branch "course-main"
                   :post/version (:grainframe-version metadata)
                   :post/lesson-order (:lesson-order metadata)
                   :post/page-number (:page-number metadata)
                   :post/total-pages (:total-pages metadata)}))
        filename (str (:id lesson-info) "-posts.edn")
        output-path (str grainweb-dir "/" filename)]
    (io/make-parents output-path)
    (spit output-path (pr-str posts))
    (println "  🌐 Grainweb:" filename)))

(defn publish-lesson
  "Publish a single lesson to all platforms."
  [lesson-info]
  (println "\n📖 Processing:" (:title lesson-info))
  (when-let [content (read-lesson-file lesson-info)]
    (let [parsed (parse-frontmatter content)
          lesson-content (:content parsed)
          pages (split-into-grainframes lesson-content)]
      
      (println "  📊 Split into" (count pages) "Grainframe(s)")
      
      ;; Publish to all platforms
      (publish-to-svelte lesson-info pages)
      (publish-to-native lesson-info pages)
      (publish-to-grainweb lesson-info pages)
      
      (println "  ✅ Complete:" (:title lesson-info)))))

;; =============================================================================
;; Course Index
;; =============================================================================

(defn generate-course-index
  "Generate course index for all lessons."
  []
  (let [lesson-data (for [lesson lessons]
                      (let [content (read-lesson-file lesson)
                            parsed (parse-frontmatter content)
                            pages (split-into-grainframes (:content parsed))]
                        {:id (:id lesson)
                         :title (:title lesson)
                         :order (:order lesson)
                         :total-pages (count pages)
                         :clay-beam {:clay-ship "kae3g"
                                    :clay-desk "course"
                                    :clay-revision "1.0.0"
                                    :clay-path ["lessons" (:id lesson)]}}))]
    {:course-title "Building the Grain Network"
     :course-description "A comprehensive high school course on building decentralized systems"
     :instructor "kae3g"
     :lessons (sort-by :order lesson-data)
     :total-lessons (count lesson-data)}))

(defn publish-course-index
  "Publish course index to all platforms."
  []
  (println "\n📚 Generating course index...")
  (let [index (generate-course-index)]
    
    ;; Svelte: JSON
    (spit (str svelte-dir "/../course-index.json")
          (str (clojure.data.json/write-str index :escape-slash false) "\n"))
    (println "  📄 Svelte: course-index.json")
    
    ;; Native & Grainweb: EDN
    (spit (str native-dir "/../course-index.edn")
          (pr-str index))
    (println "  💾 Native: course-index.edn")
    
    (spit (str grainweb-dir "/course-index.edn")
          (pr-str index))
    (println "  🌐 Grainweb: course-index.edn")
    
    (println "  ✅ Course index published!")))

;; =============================================================================
;; Main
;; =============================================================================

(defn publish-all-lessons
  "Publish all lessons to all platforms."
  []
  (println "\n🚀 Starting course publication pipeline...")
  (println "================================================\n")
  
  (doseq [lesson lessons]
    (publish-lesson lesson))
  
  (publish-course-index)
  
  (println "\n================================================")
  (println "✨ Course published successfully!")
  (println "\n📂 Output locations:")
  (println "   📄 Svelte website: " svelte-dir)
  (println "   💾 Native apps:    " native-dir)
  (println "   🌐 Grainweb posts: " grainweb-dir)
  (println "\n📚 Course ready for deployment!"))

(defn create-symlinks
  "Create symlinks for unified course structure."
  []
  (println "\n🔗 Creating symlinks...")
  
  ;; Symlink lessons to website
  (let [source (str course-dir "/lessons")
        target (str svelte-dir "/source")]
    (when-not (fs/exists? target)
      (fs/create-sym-link target source)
      (println "  🔗 Linked:" target "->" source)))
  
  ;; Symlink to native app
  (let [source (str course-dir "/lessons")
        target (str native-dir "/source")]
    (when-not (fs/exists? target)
      (fs/create-sym-link target source)
      (println "  🔗 Linked:" target "->" source)))
  
  (println "  ✅ Symlinks created!"))

;; =============================================================================
;; CLI
;; =============================================================================

(defn print-usage
  []
  (println "Grain Course Publisher")
  (println "======================")
  (println)
  (println "Usage:")
  (println "  ./scripts/publish-course.bb [command]")
  (println)
  (println "Commands:")
  (println "  publish     - Publish all lessons to all platforms")
  (println "  index       - Generate and publish course index only")
  (println "  symlinks    - Create symlinks for unified structure")
  (println "  lesson <id> - Publish specific lesson")
  (println)
  (println "Examples:")
  (println "  ./scripts/publish-course.bb publish")
  (println "  ./scripts/publish-course.bb lesson lesson-05")
  (println "  ./scripts/publish-course.bb symlinks"))

(let [args *command-line-args*]
  (case (first args)
    "publish" (do
                (create-symlinks)
                (publish-all-lessons))
    "index" (publish-course-index)
    "symlinks" (create-symlinks)
    "lesson" (when-let [lesson-id (second args)]
              (if-let [lesson (first (filter #(= (:id %) lesson-id) lessons))]
                (publish-lesson lesson)
                (println "❌ Error: Lesson not found:" lesson-id)))
    (print-usage)))
