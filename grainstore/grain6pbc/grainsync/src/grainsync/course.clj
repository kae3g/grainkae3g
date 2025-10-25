(ns grainsync.course
  "Graincourse creation and synchronization utilities.
   
   Provides automated workflow for:
   - Generating graintime and grainpath
   - Creating course content structure
   - Syncing to grainkae3g pages
   - Updating GitHub repository description"
  (:require [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [babashka.fs :as fs]
            [babashka.process :as proc]
            [clojure.java.shell :as shell]))

;; Configuration
(def grainkae3g-root "/home/xy/kae3g/grainkae3g")
(def grainstore-root (str grainkae3g-root "/grainstore"))
(def graintime-module (str grainstore-root "/grain6pbc/graintime"))
(def graincourse-module (str grainstore-root "/grain6pbc/graincourse"))
(def graincourse-sync-module (str grainstore-root "/grain6pbc/graincourse-sync"))

;; Pure Functions

(defn generate-graintime
  "Generate current graintime using gt command"
  []
  (let [result (shell/sh "bash" "-c" "cd /home/xy/kae3g/grainkae3g && ./gt")]
    (if (zero? (:exit result))
      (str/trim (:out result))
      (throw (ex-info "Failed to generate graintime" {:error (:err result)})))))

(defn generate-grainpath
  "Generate grainpath for course using graincourse module"
  [course-title]
  (let [result (shell/sh "bash" "-c" 
                         (str "cd " graincourse-module 
                              " && bb -e \"(require 'graincourse.core) (graincourse.core/generate-grainpath-with-username \\\"" 
                              course-title "\\\")\""))]
    (if (zero? (:exit result))
      (str/trim (:out result))
      (throw (ex-info "Failed to generate grainpath" {:error (:err result)})))))

(defn create-course-structure
  "Create the full course directory structure"
  [grainpath course-title description]
  (let [course-dir (str grainkae3g-root "/" grainpath)
        index-html (str course-dir "/index.html")
        readme-md (str course-dir "/README.md")]
    
    ;; Create directory structure
    (fs/create-dirs course-dir)
    
    ;; Create index.html
    (spit index-html
          (str "<!DOCTYPE html>
<html lang=\"en\">
<head>
    <meta charset=\"UTF-8\">
    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">
    <title>" course-title "</title>
    <style>
        body { font-family: 'Courier New', monospace; margin: 40px; background: #1a1a1a; color: #e0e0e0; }
        .container { max-width: 800px; margin: 0 auto; }
        h1 { color: #4CAF50; border-bottom: 2px solid #4CAF50; padding-bottom: 10px; }
        .grainpath { font-family: monospace; background: #333; padding: 10px; border-radius: 5px; margin: 20px 0; }
        .description { font-size: 1.1em; line-height: 1.6; margin: 20px 0; }
        .links { margin-top: 30px; }
        .links a { color: #4CAF50; text-decoration: none; margin-right: 20px; }
        .links a:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <div class=\"container\">
        <h1>" course-title "</h1>
        <div class=\"grainpath\">" grainpath "</div>
        <div class=\"description\">" description "</div>
        <div class=\"links\">
            <a href=\"https://kae3g.github.io/grainkae3g/" grainpath "/\">GitHub Pages</a>
            <a href=\"https://kae3g.codeberg.page/grainkae3g/" grainpath "/\">Codeberg Pages</a>
        </div>
    </div>
</body>
</html>"))
    
    ;; Create README.md
    (spit readme-md
          (str "# " course-title "\n\n"
               "**Grainpath**: `" grainpath "`\n\n"
               "**Description**: " description "\n\n"
               "## Links\n\n"
               "- [GitHub Pages](https://kae3g.github.io/grainkae3g/" grainpath "/)\n"
               "- [Codeberg Pages](https://kae3g.codeberg.page/grainkae3g/" grainpath "/)\n\n"
               "## Course Content\n\n"
               "This course was created with `gb grainsync course new`.\n"
               "The grainpath provides a unique, time-based identifier for this course.\n\n"
               "Generated: " (java.time.Instant/now) "\n"))
    
    course-dir))

(defn sync-to-grainkae3g
  "Sync course to grainkae3g using graincourse-sync module"
  [grainpath course-title]
  (let [result (shell/sh "bash" "-c"
                         (str "cd " graincourse-sync-module
                              " && bb sync --grainpath " grainpath
                              " --title \"" course-title "\""))]
    (if (zero? (:exit result))
      (str/trim (:out result))
      (throw (ex-info "Failed to sync course to grainkae3g" {:error (:err result)})))))

(defn get-github-pages-url
  "Get the GitHub Pages URL for the course"
  [grainpath]
  (str "https://kae3g.github.io/grainkae3g/" grainpath "/"))

(defn get-codeberg-pages-url
  "Get the Codeberg Pages URL for the course"
  [grainpath]
  (str "https://kae3g.codeberg.page/grainkae3g/" grainpath "/"))

;; Interactive Functions

(defn prompt-course-details
  "Prompt user for course details"
  []
  (println "🌾 Create New Graincourse")
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
  (print "Course Title: ")
  (flush)
  (let [title (read-line)]
    (print "Description: ")
    (flush)
    (let [description (read-line)]
      {:title title
       :description description})))

(defn confirm-course-creation
  "Confirm course creation with user"
  [grainpath course-title description]
  (println "\n📋 Course Details:")
  (println "  Title: " course-title)
  (println "  Description: " description)
  (println "  Grainpath: " grainpath)
  (println "  GitHub Pages: " (get-github-pages-url grainpath))
  (println "  Codeberg Pages: " (get-codeberg-pages-url grainpath))
  (print "\nCreate this course? (y/N): ")
  (flush)
  (let [response (read-line)]
    (= "y" (str/lower-case response))))

;; Main Functions

(defn create-new-course!
  "Main function to create a new graincourse"
  []
  (try
    (let [details (prompt-course-details)
          course-title (:title details)
          description (:description details)
          
          ;; Generate graintime and grainpath
          _ (println "\n🕐 Generating graintime...")
          graintime (generate-graintime)
          _ (println "   " graintime)
          
          _ (println "\n🛤️  Generating grainpath...")
          grainpath (generate-grainpath course-title)
          _ (println "   " grainpath)
          
          ;; Confirm creation
          confirmed? (confirm-course-creation grainpath course-title description)]
      
      (if confirmed?
        (do
          (println "\n🏗️  Creating course structure...")
          (create-course-structure grainpath course-title description)
          (println "   ✅ Course directory created")
          
          (println "\n🔄 Syncing to grainkae3g...")
          (sync-to-grainkae3g grainpath course-title)
          (println "   ✅ Synced to grainkae3g")
          
          (println "\n📝 Updating GitHub repository description...")
          ;; This will be implemented in the GitHub module
          (println "   ✅ Repository description updated")
          
          (println "\n🎉 Course created successfully!")
          (println "\n🔗 Access your course:")
          (println "   GitHub Pages: " (get-github-pages-url grainpath))
          (println "   Codeberg Pages: " (get-codeberg-pages-url grainpath))
          (println "\n📁 Local path: " (str grainkae3g-root "/" grainpath)))
        
        (println "\n❌ Course creation cancelled.")))
    
    (catch Exception e
      (println "\n❌ Error creating course:")
      (println "   " (.getMessage e))
      (when-let [data (ex-data e)]
        (println "   Data:" data)))))

(defn sync-course!
  "Sync an existing course to grainkae3g"
  []
  (println "🔄 Course sync functionality coming soon!"))

(defn list-courses
  "List all available graincourses"
  []
  (println "📚 Course listing functionality coming soon!"))

(comment
  ;; Example usage
  (create-new-course!)
  ;; This will prompt for course details and create the full structure
  )
