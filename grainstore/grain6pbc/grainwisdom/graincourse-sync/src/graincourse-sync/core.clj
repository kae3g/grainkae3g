(ns graincourse-sync.core
  "GrainCourse Sync: Immutable Grainpath Symlink System
  
  Replaces manual repository creation with efficient symlink-based
  immutable grainpath subdirectories for course deployment."
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [graincourse-sync.symlinks :as symlinks]
            [graincourse-sync.urls :as urls]))

(defn load-config
  "Load configuration from template and personal directories"
  []
  (let [template-config (try
                          (edn/read-string (slurp "template/config.edn"))
                          (catch Exception _ {}))
        personal-config (try
                          (edn/read-string (slurp "personal/config.edn"))
                          (catch Exception _ {}))]
    (merge template-config personal-config)))

(defn find-latest-course
  "Find the most recent course directory in personal/"
  [personal-dir]
  (let [course-dirs (->> (io/file personal-dir)
                         (.listFiles)
                         (filter #(.isDirectory %))
                         (map #(.getName %))
                         (filter #(str/includes? % "12025-"))
                         (sort)
                         (reverse))]
    (first course-dirs)))

(defn extract-graintime
  "Extract graintime from course directory name"
  [course-dir-name]
  (let [graintime-match (re-find #"12025-[0-9-]+-[A-Z]+-[a-z-]+-[0-9]+" course-dir-name)]
    graintime-match))

(defn generate-grainpath
  "Generate grainpath from course directory"
  [course-dir-name]
  (let [graintime (extract-graintime course-dir-name)
        course-name (-> course-dir-name
                        (str/replace #"kae3g-" "")
                        (str/replace #"-12025.*" ""))]
    (str "/course/kae3g/" course-name "/" graintime "/")))

(defn setup-symlinks
  "Setup symlinks for the latest course with grainpath structure
  
  Creates the full grainpath nested directory structure:
  /course/{author}/{course-name}/{graintime}/
  
  The course content (index.html, lessons/, assets/, etc.) is placed
  at the end of the grainpath, with all other files relative to that location."
  []
  (let [config (load-config)
        personal-dir (get config :course-dir "personal")
        symlink-dir (get config :symlink-dir "symlinks")
        latest-course (find-latest-course personal-dir)]
    
    (if latest-course
      (let [graintime (extract-graintime latest-course)
            grainpath (generate-grainpath latest-course)
            source-dir (str personal-dir "/" latest-course)
            ;; Create grainpath structure for both platforms
            github-base (str symlink-dir "/github-pages")
            codeberg-base (str symlink-dir "/codeberg-pages")]
        
        (println "")
        (println "🌾 Setting up GrainCourse Sync with Grainpath Structure")
        (println "")
        (println (str "📁 Latest Course: " latest-course))
        (println (str "📅 Grainpath: " grainpath))
        (println "")
        
        ;; Create grainpath structure and symlinks for GitHub
        (let [github-result (symlinks/create-grainpath-symlink source-dir github-base grainpath)]
          (if (:success github-result)
            (println (str "✅ GitHub Pages: " (:target-path github-result)))
            (println (str "❌ GitHub Pages failed: " (:error github-result)))))
        
        ;; Create grainpath structure and symlinks for Codeberg
        (let [codeberg-result (symlinks/create-grainpath-symlink source-dir codeberg-base grainpath)]
          (if (:success codeberg-result)
            (println (str "✅ Codeberg Pages: " (:target-path codeberg-result)))
            (println (str "❌ Codeberg Pages failed: " (:error codeberg-result)))))
        
        ;; Generate URLs
        (let [github-url (urls/generate-github-url grainpath config)
              codeberg-url (urls/generate-codeberg-url grainpath config)]
          (println "")
          (println "🌐 Generated URLs:")
          (println (str "   GitHub: " github-url))
          (println (str "   Codeberg: " codeberg-url))
          (println ""))
        
        ;; Show relative URLs for course content
        (let [relative-urls (urls/generate-relative-urls grainpath)]
          (when relative-urls
            (println "📄 Course Content URLs (relative to grainpath):")
            (println (str "   Index: " (:index relative-urls)))
            (println (str "   Lessons: " (:lessons relative-urls)))
            (println (str "   Assets: " (:assets relative-urls)))
            (println (str "   README: " (:readme relative-urls)))
            (println "")))
        
        {:success true
         :course latest-course
         :grainpath grainpath
         :github-target (str github-base "/" graintime)
         :codeberg-target (str codeberg-base "/" graintime)})
      
      {:success false
       :error "No course directories found in personal/"})))

(defn validate-symlinks
  "Validate all existing symlinks"
  []
  (let [config (load-config)
        symlink-dir (get config :symlink-dir "symlinks")
        github-dir (str symlink-dir "/github-pages")
        codeberg-dir (str symlink-dir "/codeberg-pages")]
    
    (println "")
    (println "🔍 Validating GrainCourse Symlinks")
    (println "")
    
    (let [github-valid (symlinks/validate-symlinks github-dir)
          codeberg-valid (symlinks/validate-symlinks codeberg-dir)]
      
      (println (str "📊 GitHub Pages: " (count github-valid) " valid symlinks"))
      (println (str "📊 Codeberg Pages: " (count codeberg-valid) " valid symlinks"))
      (println "")
      
      {:github github-valid
       :codeberg codeberg-valid})))

(defn list-symlinks
  "List all active symlinks"
  []
  (let [config (load-config)
        symlink-dir (get config :symlink-dir "symlinks")
        github-dir (str symlink-dir "/github-pages")
        codeberg-dir (str symlink-dir "/codeberg-pages")]
    
    (println "")
    (println "📋 Active GrainCourse Symlinks")
    (println "")
    
    (println "🔗 GitHub Pages:")
    (doseq [symlink (symlinks/list-symlinks github-dir)]
      (println (str "   " symlink)))
    
    (println "")
    (println "🔗 Codeberg Pages:")
    (doseq [symlink (symlinks/list-symlinks codeberg-dir)]
      (println (str "   " symlink)))
    
    (println "")))

(defn cleanup-old-symlinks
  "Clean up old symlinks (keep only recent ones)"
  [keep-count]
  (let [config (load-config)
        symlink-dir (get config :symlink-dir "symlinks")
        github-dir (str symlink-dir "/github-pages")
        codeberg-dir (str symlink-dir "/codeberg-pages")]
    
    (println "")
    (println (str "🧹 Cleaning up old symlinks (keeping " keep-count " most recent)"))
    (println "")
    
    (let [github-cleaned (symlinks/cleanup-symlinks github-dir keep-count)
          codeberg-cleaned (symlinks/cleanup-symlinks codeberg-dir keep-count)]
      
      (println (str "✅ GitHub Pages: cleaned " github-cleaned " old symlinks"))
      (println (str "✅ Codeberg Pages: cleaned " codeberg-cleaned " old symlinks"))
      (println ""))))

(defn repair-symlinks
  "Repair broken symlinks"
  []
  (let [config (load-config)
        personal-dir (get config :course-dir "personal")
        symlink-dir (get config :symlink-dir "symlinks")]
    
    (println "")
    (println "🔧 Repairing GrainCourse Symlinks")
    (println "")
    
    (let [courses (->> (io/file personal-dir)
                       (.listFiles)
                       (filter #(.isDirectory %))
                       (map #(.getName %))
                       (filter #(str/includes? % "12025-"))
                       (sort)
                       (reverse))]
      
      (doseq [course courses]
        (let [graintime (extract-graintime course)
              source-dir (str personal-dir "/" course)
              github-target (str symlink-dir "/github-pages/" graintime)
              codeberg-target (str symlink-dir "/codeberg-pages/" graintime)]
          
          (when (not (symlinks/symlink-valid? github-target))
            (println (str "🔗 Repairing GitHub symlink: " graintime))
            (symlinks/create-symlink source-dir github-target))
          
          (when (not (symlinks/symlink-valid? codeberg-target))
            (println (str "🔗 Repairing Codeberg symlink: " graintime))
            (symlinks/create-symlink source-dir codeberg-target))))
      
      (println "")
      (println "✅ Symlink repair complete!")
      (println ""))))

(defn reset-symlinks
  "Reset all symlinks (remove and recreate)"
  []
  (let [config (load-config)
        symlink-dir (get config :symlink-dir "symlinks")
        github-dir (str symlink-dir "/github-pages")
        codeberg-dir (str symlink-dir "/codeberg-pages")]
    
    (println "")
    (println "🔄 Resetting GrainCourse Symlinks")
    (println "")
    
    ;; Remove all symlinks
    (doseq [file (concat (.listFiles (io/file github-dir))
                         (.listFiles (io/file codeberg-dir)))]
      (when (.isSymbolicLink file)
        (io/delete-file file)))
    
    ;; Recreate from latest course
    (setup-symlinks)
    
    (println "✅ Symlink reset complete!")
    (println "")))
