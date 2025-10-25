#!/usr/bin/env bb
;; QB Course Sync Personal - Synchronize and personalize course content
;; Inspired by template/personal separation philosophy

(require '[babashka.process :refer [shell]]
         '[clojure.java.io :as io]
         '[clojure.string :as str])

(defn get-current-graintime []
  "Generate current graintime timestamp"
  (let [now (java.time.LocalDateTime/now)
        formatter (java.time.format.DateTimeFormatter/ofPattern "yyyyy-MM-dd--HHmm")]
    (.format now formatter)))

(defn create-new-course-dir [course-name]
  "Create new course directory with graintime path"
  (let [graintime (get-current-graintime)
        course-slug (str/lower-case (str/replace course-name #"\s+" "-"))
        full-path (str graintime "--PDT--moon-vishakha------asc-gem000--sun-03rd--kae3g/" course-slug)]
    (println (str "🌾 Creating new course directory: " full-path))
    (io/make-parents (str full-path "/index.html"))
    full-path))

(defn sync-template-to-personal [template-path personal-path]
  "Sync template course structure to personal directory"
  (println "🔄 Syncing template to personal...")
  (shell "rsync" "-av" "--ignore-existing" 
         (str template-path "/") 
         (str personal-path "/")))

(defn personalize-course [course-path preferences]
  "Apply personal preferences to course"
  (println "🎨 Personalizing course content...")
  (println (str "  Course path: " course-path))
  (println (str "  Preferences: " preferences)))

(defn generate-course-index [course-path title description]
  "Generate course index.html"
  (let [index-content (str "<!DOCTYPE html>
<html lang=\"en\">
<head>
    <meta charset=\"UTF-8\">
    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">
    <title>" title " | Grain Network Course</title>
    <link rel=\"stylesheet\" href=\"styles/graincard10.css\">
</head>
<body>
    <div class=\"graincard10\">
        <header class=\"course-header\">
            <h1>" title "</h1>
            <p class=\"course-description\">" description "</p>
        </header>
        
        <main class=\"course-content\">
            <nav class=\"course-nav\">
                <a href=\"pages/0000-title.html\">Start Course →</a>
            </nav>
        </main>
        
        <footer class=\"course-footer\">
            <p>now == next + 1 | 🌾 Grain Network</p>
        </footer>
    </div>
</body>
</html>")]
    (spit (str course-path "/index.html") index-content)
    (println "✅ Course index generated")))

(defn main [& args]
  (println "")
  (println "╔═══════════════════════════════════════════════════════════════╗")
  (println "║                                                               ║")
  (println "║     🌾 Q B   C O U R S E   S Y N C   P E R S O N A L 🌾     ║")
  (println "║                                                               ║")
  (println "║   Universal Quarterback Course Synchronization & Personalization   ║")
  (println "║                                                               ║")
  (println "╚═══════════════════════════════════════════════════════════════╝")
  (println "")
  
  ;; Default to "kae3g kae3g" format: type/username as default title/author
  (let [course-name (if (seq args) 
                      (str/join " " args) 
                      "kae3g kae3g") ; Default: kae3g kae3g
        course-desc (if (seq args)
                      "A hands-on introduction to Grain6, inspired by the original Urbit demo"
                      "kae3g default course - Local Control, Global Intent")]
    
    (println (str "📚 Creating course: " course-name))
    (println (str "📝 Description: " course-desc))
    (println "")
    
    (let [course-path (create-new-course-dir course-name)]
      
      ;; Create directory structure
      (io/make-parents (str course-path "/pages/.gitkeep"))
      (io/make-parents (str course-path "/styles/.gitkeep"))
      (io/make-parents (str course-path "/scripts/.gitkeep"))
      (io/make-parents (str course-path "/data/.gitkeep"))
      
      (println "✅ Directory structure created")
      
      ;; Generate course index
      (generate-course-index course-path course-name course-desc)
      
      (println "")
      (println "🌾 QB course sync complete!")
      (println (str "📂 Course location: " course-path))
      (println "")
      (println "now == next + 1")
      (println "🌾"))))

(main *command-line-args*)

