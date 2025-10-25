#!/usr/bin/env bb

(require '[clojure.edn :as edn]
         '[clojure.string :as str]
         '[clojure.java.io :as io]
         '[babashka.fs :as fs]
         '[clojure.java.shell :as shell])

;; Configuration
(def grainkae3g-root "/home/xy/kae3g/grainkae3g")
(def grainstore-root (str grainkae3g-root "/grainstore"))
(def graintime-module (str grainstore-root "/grainpbc/graintime"))
(def graincourse-module (str grainstore-root "/grainpbc/graincourse"))
(def graincourse-sync-module (str grainstore-root "/grainpbc/graincourse-sync"))
(def personal-config-file (str grainkae3g-root "/.grainconfig.edn"))

;; Default configuration
(def default-grainsync-config
  {:course-title "kae3g"
   :course-description "kae3g"
   :github-owner "kae3g"
   :github-repo "grainkae3g"
   :codeberg-owner "kae3g"
   :codeberg-repo "grainkae3g"})

;; Configuration Functions
(defn read-personal-config
  "Read personal configuration"
  []
  (if (fs/exists? personal-config-file)
    (edn/read-string (slurp personal-config-file))
    {:grainconfig {:modules {}}}))

(defn get-grainsync-config
  "Get grainsync configuration with defaults"
  []
  (let [personal-config (read-personal-config)
        grainsync-config (get-in personal-config [:grainconfig :modules :grainsync :defaults] {})]
    (merge default-grainsync-config grainsync-config)))

;; Pure Functions
(defn generate-graintime
  "Generate current graintime using gt command"
  []
  (let [result (shell/sh "gt")]
    (if (zero? (:exit result))
      (let [output (:out result)
            ;; Extract just the graintime line (starts with 12025-10-23--0210)
            graintime-line (->> (str/split-lines output)
                                (filter #(re-find #"^12025-\d{2}-\d{2}--\d{4}" %))
                                first)]
        (str/trim graintime-line))
      (throw (ex-info "Failed to generate graintime" {:error (:err result)})))))

(defn generate-grainpath
  "Generate grainpath for course using graintime and course title"
  [course-title graintime]
  (let [;; Simple grainpath generation: graintime + kebab-case title
        kebab-title (-> course-title
                        (str/lower-case)
                        (str/replace #"[^a-z0-9\s]" "")
                        (str/replace #"\s+" "-"))
        grainpath (str graintime "/" kebab-title)]
    grainpath))

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
                              " && bb -e \"(load-file \\\"src/graincourse-sync/symlinks.clj\\\") (load-file \\\"src/graincourse-sync/urls.clj\\\") (load-file \\\"src/graincourse-sync/core.clj\\\") (require '[graincourse-sync.core :as sync]) (let [result (sync/setup-symlinks)] (println \\\"Sync result:\\\" result))\""
                              " --grainpath " grainpath
                              " --title \"" course-title "\""))]
    (if (zero? (:exit result))
      (str/trim (:out result))
      (throw (ex-info "Failed to sync course to grainkae3g" {:error (:err result)})))))

(defn get-github-pages-url
  "Get the GitHub Pages URL for the course"
  [grainpath]
  (let [config (get-grainsync-config)
        owner (:github-owner config)
        repo (:github-repo config)]
    (str "https://" owner ".github.io/" repo "/" grainpath "/")))

(defn get-codeberg-pages-url
  "Get the Codeberg Pages URL for the course"
  [grainpath]
  (let [config (get-grainsync-config)
        owner (:codeberg-owner config)
        repo (:codeberg-repo config)]
    (str "https://" owner ".codeberg.page/" repo "/" grainpath "/")))

;; Interactive Functions
(defn prompt-course-details
  "Prompt user for course details"
  []
  (let [config (get-grainsync-config)
        default-title (:course-title config)
        default-description (:course-description config)]
    
    (println "🌾 Create New Graincourse")
    (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    (print (str "Course Title [" default-title "]: "))
    (flush)
    (let [title (let [input (read-line)]
                  (if (str/blank? input) default-title input))]
      (print (str "Description [" default-description "]: "))
      (flush)
      (let [description (let [input (read-line)]
                          (if (str/blank? input) default-description input))]
        {:title title
         :description description}))))

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

;; Main Function
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
          grainpath (generate-grainpath course-title graintime)
          _ (println "   " grainpath)
          
          ;; Confirm creation
          confirmed? (confirm-course-creation grainpath course-title description)]
      
      (if confirmed?
        (do
          (println "\n🏗️  Creating course structure...")
          (create-course-structure grainpath course-title description)
          (println "   ✅ Course directory created")
          
          (println "\n🔄 Syncing to grainkae3g...")
          (try
            (sync-to-grainkae3g grainpath course-title)
            (println "   ✅ Synced to grainkae3g")
            (catch Exception e
              (println "   ⚠️  Sync skipped (graincourse-sync not fully configured)")))
          
          (println "\n📝 Updating GitHub repository description...")
          (println "   ⚠️  GitHub description update skipped (integration in progress)")
          
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

;; Run the main function
(create-new-course!)
