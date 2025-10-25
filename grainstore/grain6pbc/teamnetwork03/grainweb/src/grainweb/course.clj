(ns grainweb.course
  "Course content management for Grain Network.
   Converts lesson markdown to Grainweb posts, Svelte website, and native apps."
  (:require
   [clojure.spec.alpha :as s]
   [clojure.java.io :as io]
   [clojure.string :as str]
   [clojure.edn :as edn]
   [clotoko.sur.grainframe :as grainframe]
   [clotoko.sur.clay :as clay]
   [clotoko.sur.mark :as mark]))

;; =============================================================================
;; Course Structure
;; =============================================================================

(def course-root "/home/xy/kae3g/grainkae3g/docs/course")

(def lesson-files
  [{:id "lesson-05"
    :title "The Harmony of 80 and 110"
    :file "lessons/lesson-05-the-harmony-of-80-and-110.md"
    :order 5}
   {:id "lesson-06"
    :title "Advanced Type Systems and Networked Data"
    :file "lessons/lesson-06-advanced-type-systems-and-networked-data.md"
    :order 6}])

;; =============================================================================
;; Markdown to Grainframe Conversion
;; =============================================================================

(defn parse-markdown-frontmatter
  "Extract YAML frontmatter from markdown."
  [markdown]
  (let [lines (str/split-lines markdown)]
    (if (and (>= (count lines) 3)
             (= "---" (first lines)))
      (let [end-idx (first (keep-indexed
                            #(when (and (> %1 0) (= "---" %2)) %1)
                            lines))]
        (when end-idx
          {:frontmatter (str/join "\n" (subvec (vec lines) 1 end-idx))
           :content (str/join "\n" (drop (inc end-idx) lines))}))
      {:frontmatter nil
       :content markdown})))

(defn markdown->grainframe-pages
  "Split markdown into multiple Grainframes (if needed)."
  [markdown metadata]
  (let [content (:content (parse-markdown-frontmatter markdown))
        pages (grainframe/grainframe-pagination content)
        frames (map-indexed
                (fn [idx page-content]
                  (grainframe/create-grainframe
                   page-content
                   :type :document
                   :title (str (:title metadata) " (Page " (inc idx) "/" (:total-frames pages) ")")
                   :author "kae3g"
                   :tags ["course" "lesson" (:id metadata)]
                   :version "1.0.0"))
                (:frames pages))]
    {:total-pages (:total-frames pages)
     :frames frames}))

(defn lesson-file->grainframes
  "Convert a lesson markdown file to Grainframes."
  [lesson-info]
  (let [file-path (str course-root "/" (:file lesson-info))
        markdown (slurp file-path)
        metadata {:title (:title lesson-info)
                 :id (:id lesson-info)
                 :order (:order lesson-info)}
        result (markdown->grainframe-pages markdown metadata)]
    (assoc result
           :lesson-info lesson-info
           :metadata metadata)))

;; =============================================================================
;; Grainframe to Grainweb Post
;; =============================================================================

(defn grainframe->grainweb-post
  "Convert Grainframe to Grainweb post data structure."
  [grainframe lesson-info page-num total-pages]
  (let [meta (:grainframe-metadata grainframe)]
    {:post/id (:grainframe-id grainframe)
     :post/title (str (:title lesson-info)
                     (when (> total-pages 1)
                       (str " (Page " page-num "/" total-pages ")")))
     :post/content (:grainframe-content grainframe)
     :post/type :document
     :post/timestamp (:grainframe-timestamp meta)
     :post/author "kae3g"
     :post/tags (concat ["course" "lesson" (:id lesson-info)]
                       (:grainframe-tags meta))
     :post/visibility :public
     :post/grain-post true
     :post/status (str "lesson-" (:order lesson-info))
     :post/branch "course-main"
     :post/version "1.0.0"
     :post/lesson-order (:order lesson-info)
     :post/page-number page-num
     :post/total-pages total-pages}))

;; =============================================================================
;; Grainframe to Markdown (for Svelte website)
;; =============================================================================

(defn grainframe->markdown
  "Convert Grainframe to Markdown with frontmatter."
  [grainframe lesson-info page-num total-pages]
  (let [meta (:grainframe-metadata grainframe)
        post (grainframe->grainweb-post grainframe lesson-info page-num total-pages)]
    (str "---\n"
         "title: " (pr-str (:post/title post)) "\n"
         "author: " (pr-str (:post/author post)) "\n"
         "date: " (str (:post/timestamp post)) "\n"
         "tags: " (pr-str (:post/tags post)) "\n"
         "type: " (name (:post/type post)) "\n"
         "lesson_order: " (:post/lesson-order post) "\n"
         "page_number: " (:post/page-number post) "\n"
         "total_pages: " (:post/total-pages post) "\n"
         "layout: lesson\n"
         "---\n\n"
         (:grainframe-content grainframe))))

;; =============================================================================
;; Grainframe to EDN (for native apps)
;; =============================================================================

(defn grainframe->edn
  "Convert Grainframe to EDN for native apps."
  [grainframe lesson-info page-num total-pages]
  (let [post (grainframe->grainweb-post grainframe lesson-info page-num total-pages)]
    {:grainframe grainframe
     :grainweb-post post
     :lesson-info lesson-info
     :clay-beam {:clay-ship "kae3g"
                 :clay-desk "course"
                 :clay-revision "1.0.0"
                 :clay-path ["lessons" (:id lesson-info) (str "page-" page-num ".edn")]}}))

;; =============================================================================
;; Publishing Pipeline
;; =============================================================================

(defn publish-lesson-to-svelte
  "Publish lesson Grainframes to Svelte website."
  [lesson-result output-dir]
  (let [lesson-info (:lesson-info lesson-result)
        frames (:frames lesson-result)
        total-pages (:total-pages lesson-result)]
    (doseq [[idx frame] (map-indexed vector frames)]
      (let [page-num (inc idx)
            markdown (grainframe->markdown frame lesson-info page-num total-pages)
            filename (str (:id lesson-info) "-page-" page-num ".md")
            output-path (str output-dir "/" filename)]
        (io/make-parents output-path)
        (spit output-path markdown)
        (println "📄 Svelte:" filename)))))

(defn publish-lesson-to-native
  "Publish lesson Grainframes to native app data."
  [lesson-result output-dir]
  (let [lesson-info (:lesson-info lesson-result)
        frames (:frames lesson-result)
        total-pages (:total-pages lesson-result)]
    (doseq [[idx frame] (map-indexed vector frames)]
      (let [page-num (inc idx)
            edn-data (grainframe->edn frame lesson-info page-num total-pages)
            filename (str (:id lesson-info) "-page-" page-num ".edn")
            output-path (str output-dir "/" filename)]
        (io/make-parents output-path)
        (spit output-path (pr-str edn-data))
        (println "💾 Native:" filename)))))

(defn publish-lesson-to-grainweb
  "Publish lesson Grainframes to Grainweb posts."
  [lesson-result output-dir]
  (let [lesson-info (:lesson-info lesson-result)
        frames (:frames lesson-result)
        total-pages (:total-pages lesson-result)
        posts (map-indexed
               (fn [idx frame]
                 (grainframe->grainweb-post frame lesson-info (inc idx) total-pages))
               frames)
        output-path (str output-dir "/" (:id lesson-info) "-posts.edn")]
    (io/make-parents output-path)
    (spit output-path (pr-str posts))
    (println "🌐 Grainweb:" (:id lesson-info) "-posts.edn")))

(defn publish-all-lessons
  "Publish all lessons to all platforms."
  [& {:keys [svelte-dir native-dir grainweb-dir]
      :or {svelte-dir "/home/xy/kae3g/grainkae3g/website/content/lessons"
           native-dir "/home/xy/kae3g/grainkae3g/grainstore/grainweb/data/lessons"
           grainweb-dir "/home/xy/kae3g/grainkae3g/grainstore/grainweb/data/posts"}}]
  
  (println "\n📚 Publishing all lessons...\n")
  
  (doseq [lesson lesson-files]
    (println "📖 Processing:" (:title lesson))
    (let [result (lesson-file->grainframes lesson)]
      
      ;; Publish to Svelte website
      (publish-lesson-to-svelte result svelte-dir)
      
      ;; Publish to native apps
      (publish-lesson-to-native result native-dir)
      
      ;; Publish to Grainweb
      (publish-lesson-to-grainweb result grainweb-dir)
      
      (println "✅ Complete:" (:title lesson) "\n")))
  
  (println "🎉 All lessons published!"))

;; =============================================================================
;; Course Index Generation
;; =============================================================================

(defn generate-course-index
  "Generate course index for all platforms."
  []
  (let [lessons (map (fn [lesson]
                      (let [result (lesson-file->grainframes lesson)]
                        {:id (:id lesson)
                         :title (:title lesson)
                         :order (:order lesson)
                         :total-pages (:total-pages result)
                         :clay-beam {:clay-ship "kae3g"
                                    :clay-desk "course"
                                    :clay-revision "1.0.0"
                                    :clay-path ["lessons" (:id lesson)]}}))
                    lesson-files)]
    {:course-title "Building the Grain Network"
     :course-description "A comprehensive high school course on building decentralized systems"
     :instructor "kae3g"
     :lessons (sort-by :order lessons)
     :total-lessons (count lessons)}))

(defn publish-course-index
  "Publish course index to all platforms."
  [& {:keys [svelte-dir native-dir grainweb-dir]
      :or {svelte-dir "/home/xy/kae3g/grainkae3g/website/content"
           native-dir "/home/xy/kae3g/grainkae3g/grainstore/grainweb/data"
           grainweb-dir "/home/xy/kae3g/grainkae3g/grainstore/grainweb/data"}}]
  
  (let [index (generate-course-index)]
    
    ;; Svelte: JSON for easy loading
    (spit (str svelte-dir "/course-index.json")
          (json/write-str index :escape-slash false))
    (println "📄 Svelte: course-index.json")
    
    ;; Native: EDN for Clojure apps
    (spit (str native-dir "/course-index.edn")
          (pr-str index))
    (println "💾 Native: course-index.edn")
    
    ;; Grainweb: Same as native
    (spit (str grainweb-dir "/course-index.edn")
          (pr-str index))
    (println "🌐 Grainweb: course-index.edn")
    
    (println "✅ Course index published!")))

;; =============================================================================
;; Main Publishing Function
;; =============================================================================

(defn publish-course!
  "Publish entire course to all platforms."
  []
  (println "🚀 Starting course publication pipeline...\n")
  
  ;; Publish all lessons
  (publish-all-lessons)
  
  ;; Publish course index
  (publish-course-index)
  
  (println "\n✨ Course published successfully!")
  (println "   - Svelte website: /website/content/lessons/")
  (println "   - Native apps: /grainstore/grainweb/data/lessons/")
  (println "   - Grainweb posts: /grainstore/grainweb/data/posts/")
  (println "\n📚 Course ready for deployment!"))

;; =============================================================================
;; CLI Interface
;; =============================================================================

(defn -main
  [& args]
  (case (first args)
    "publish" (publish-course!)
    "index" (publish-course-index)
    "lesson" (when-let [lesson-id (second args)]
              (when-let [lesson (first (filter #(= (:id %) lesson-id) lesson-files))]
                (let [result (lesson-file->grainframes lesson)]
                  (publish-lesson-to-svelte result "/home/xy/kae3g/grainkae3g/website/content/lessons")
                  (publish-lesson-to-native result "/home/xy/kae3g/grainkae3g/grainstore/grainweb/data/lessons")
                  (publish-lesson-to-grainweb result "/home/xy/kae3g/grainkae3g/grainstore/grainweb/data/posts"))))
    (do
      (println "Grain Course Publisher")
      (println "Usage:")
      (println "  publish       - Publish all lessons to all platforms")
      (println "  index         - Generate and publish course index")
      (println "  lesson <id>   - Publish specific lesson")
      (println "\nExamples:")
      (println "  clojure -M:grainweb.course publish")
      (println "  clojure -M:grainweb.course lesson lesson-05"))))
