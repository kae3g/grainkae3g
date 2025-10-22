#!/usr/bin/env bb
;; grainstore-manager.bb - Grainstore Git Management
;;
;; Handles git pulling, merge conflicts, and documentation organization

(ns grainstore-manager
  (:require [babashka.process :refer [shell]]
            [babashka.fs :as fs]
            [clojure.string :as str]
            [clojure.edn :as edn]
            ))

(defn log [message]
  (println (str "🌾 " message)))

(defn error [message]
  (println (str "❌ ERROR: " message))
  (System/exit 1))

(defn run-command [cmd & {:keys [dir] :or {dir "."}}]
  "Run a command and return output as string"
  (try
    (-> (shell {:out :string :dir dir} cmd)
        :out
        str/trim)
    (catch Exception e
      (log (str "Error running command: " cmd " - " (.getMessage e)))
      "")))

(defn get-timestamp []
  "Get current timestamp for documentation"
  (let [now (java.time.LocalDateTime/now)
        formatter (java.time.format.DateTimeFormatter/ofPattern "yyyy-MM-dd_HH-mm-ss")]
    (.format formatter now)))

(defn list-grainstore-repos []
  "List all repositories in grainstore"
  (let [grainstore-dir "grainstore"
        docs-dir (str grainstore-dir "/docs")]
    (when (fs/exists? docs-dir)
      (->> (fs/list-dir docs-dir)
           (filter #(fs/directory? %))
           (map #(fs/file-name %))
           (filter #(not= % "."))
           (filter #(not= % ".."))))))

(defn pull-repo [repo-name]
  "Pull latest changes from a repository"
  (let [repo-path (str "grainstore/docs/" repo-name)]
    (if (fs/exists? repo-path)
      (do
        (log (str "Pulling " repo-name "..."))
        (let [result (run-command "git pull" :dir repo-path)]
          (if (str/includes? result "Already up to date")
            (log (str "✅ " repo-name " is up to date"))
            (log (str "✅ " repo-name " updated: " result)))))
      (log (str "⚠️  Repository " repo-name " not found")))))

(defn handle-merge-conflicts [repo-name]
  "Handle merge conflicts in a repository"
  (let [repo-path (str "grainstore/docs/" repo-name)
        status (run-command "git status --porcelain" :dir repo-path)
        conflicted-files (->> (str/split-lines status)
                              (filter #(str/includes? % "UU"))
                              (map #(str/split % #"\s+" 2))
                              (map second))]
    (if (seq conflicted-files)
      (do
        (log (str "🔧 Merge conflicts detected in " repo-name ":"))
        (doseq [file conflicted-files]
          (log (str "  - " file)))
        (log "Manual resolution required. Opening editor...")
        (run-command "git mergetool" :dir repo-path))
      (log (str "✅ No merge conflicts in " repo-name)))))

(defn pull-all-repos []
  "Pull all repositories in grainstore"
  (log "Pulling all Grainstore repositories...")
  (let [repos (list-grainstore-repos)]
    (if (seq repos)
      (doseq [repo repos]
        (pull-repo repo)
        (handle-merge-conflicts repo))
      (log "No repositories found in grainstore/docs"))))

(defn timestamp-docs [source-dir target-dir]
  "Timestamp and move documentation files"
  (let [timestamp (get-timestamp)
        source-path (fs/file source-dir)
        target-path (fs/file target-dir)]
    
    (when-not (fs/exists? target-path)
      (fs/create-dirs target-path))
    
    (let [files (->> (fs/list-dir source-path)
                     (filter #(fs/regular-file? %))
                     (filter #(or (str/ends-with? % ".md")
                                  (str/ends-with? % ".txt")
                                  (str/ends-with? % ".org")
                                  (str/ends-with? % ".rst"))))]
      
      (doseq [file files]
        (let [filename (fs/file-name file)
              name-without-ext (str/replace filename #"\.[^.]+$" "")
              ext (str/replace filename #".*\.(\w+)$" "$1")
              new-name (str name-without-ext "_" timestamp "." ext)
              target-file (fs/file target-path new-name)]
          (log (str "Moving " filename " -> " new-name))
          (fs/move file target-file))))))

(defn organize-docs []
  "Organize documentation in the root directory"
  (log "Organizing documentation...")
  
  ;; Create organized docs structure
  (fs/create-dirs "docs/organized")
  (fs/create-dirs "docs/unsorted")
  (fs/create-dirs "docs/timestamped")
  
  ;; Move and timestamp unsorted docs
  (timestamp-docs "." "docs/timestamped")
  
  ;; Move specific files to organized
  (let [organized-files ["README.md" "PSEUDO-INTEGRATED.md" "INTEGRATED-ROADMAP.md"]]
    (doseq [file organized-files]
      (when (fs/exists? file)
        (log (str "Moving " file " to organized/"))
        (fs/move file (str "docs/organized/" file)))))
  
  (log "✅ Documentation organized"))

(defn create-grainstore-index []
  "Create an index of all Grainstore repositories"
  (let [repos (list-grainstore-repos)
        timestamp (get-timestamp)
        index-content (str "# Grainstore Repository Index\n\n"
                          "**Generated**: " timestamp "\n"
                          "**Total Repositories**: " (count repos) "\n\n"
                          "## Repositories\n\n"
                          (str/join "\n" (map #(str "- **" % "** - " (str/replace % #"-" " ")) repos))
                          "\n\n## Management Commands\n\n"
                          "```bash\n"
                          "# Pull all repositories\n"
                          "bb scripts/grainstore-manager.bb pull-all\n\n"
                          "# Pull specific repository\n"
                          "bb scripts/grainstore-manager.bb pull <repo-name>\n\n"
                          "# Handle merge conflicts\n"
                          "bb scripts/grainstore-manager.bb resolve-conflicts <repo-name>\n\n"
                          "# Organize documentation\n"
                          "bb scripts/grainstore-manager.bb organize\n"
                          "```\n\n"
                          "## Notes\n\n"
                          "- Music artist name: `terra4m`\n"
                          "- All repositories are in `grainstore/docs/`\n"
                          "- Timestamped docs are in `docs/timestamped/`\n"
                          "- Organized docs are in `docs/organized/`\n")]
    (spit "grainstore/INDEX.md" index-content)
    (log "✅ Grainstore index created")))

(defn show-help []
  "Show help information"
  (println "Grainstore Manager - Git and Documentation Management")
  (println "")
  (println "Usage: bb grainstore-manager <command> [options]")
  (println "")
  (println "Commands:")
  (println "  pull-all                    Pull all repositories")
  (println "  pull <repo>                 Pull specific repository")
  (println "  resolve-conflicts <repo>    Handle merge conflicts")
  (println "  organize                    Organize documentation")
  (println "  create-index               Create repository index")
  (println "  list                       List all repositories")
  (println "  help                       Show this help")
  (println "")
  (println "Examples:")
  (println "  bb grainstore-manager pull-all")
  (println "  bb grainstore-manager pull urbit-docs")
  (println "  bb grainstore-manager resolve-conflicts urbit-source")
  (println "  bb grainstore-manager organize"))

(defn main [& args]
  "Main function"
  (case (first args)
    "pull-all" (pull-all-repos)
    "pull" (pull-repo (second args))
    "resolve-conflicts" (handle-merge-conflicts (second args))
    "organize" (organize-docs)
    "create-index" (create-grainstore-index)
    "list" (do (log "Grainstore repositories:") 
               (doseq [repo (list-grainstore-repos)]
                 (println (str "  - " repo))))
    "help" (show-help)
    (show-help)))

;; Run if called directly
(when (= *file* (System/getProperty "babashka.file"))
  (apply main *command-line-args*))
