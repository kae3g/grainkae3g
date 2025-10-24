#!/usr/bin/env bb
;; grainstore-sync.bb - Manage grainstore submodules from EDN manifest
;; 
;; This script reads grainstore-manifest.edn and:
;; - Clones/pulls upstream repositories
;; - Creates symlinks for aliased modules
;; - Respects .gitignore patterns
;; - Handles grainversions specially

(require '[clojure.java.shell :as shell]
         '[clojure.string :as str]
         '[clojure.edn :as edn]
         '[clojure.java.io :as io])

(def manifest-file "grainstore-manifest.edn")
(def grainstore-dir "grainstore")

(defn log [& args]
  "Print log message"
  (apply println "🌾" args))

(defn error [& args]
  "Print error message"
  (apply println "❌" args))

(defn success [& args]
  "Print success message"
  (apply println "✅" args))

(defn read-manifest
  "Read and parse grainstore manifest"
  []
  (try
    (-> manifest-file
        slurp
        edn/read-string
        :grainstore-manifest)
    (catch Exception e
      (error "Failed to read manifest:" (.getMessage e))
      nil)))

(defn ensure-grainstore-dir
  "Create grainstore directory if it doesn't exist"
  []
  (let [dir (io/file grainstore-dir)]
    (when-not (.exists dir)
      (log "Creating grainstore directory...")
      (.mkdir dir))))

(defn git-clone
  "Clone a git repository"
  [url target-dir branch]
  (log "Cloning" url "→" target-dir)
  (let [result (shell/sh "git" "clone" "-b" branch url target-dir)]
    (if (zero? (:exit result))
      (success "Cloned" target-dir)
      (error "Failed to clone" target-dir "\n" (:err result)))))

(defn git-pull
  "Pull latest changes from upstream"
  [target-dir]
  (log "Pulling updates for" target-dir)
  (let [result (shell/sh "git" "pull" :dir target-dir)]
    (if (zero? (:exit result))
      (success "Updated" target-dir)
      (error "Failed to update" target-dir "\n" (:err result)))))

(defn repo-exists?
  "Check if repository already exists"
  [target-dir]
  (.exists (io/file target-dir ".git")))

(defn create-symlink
  "Create symbolic link"
  [target link-name]
  (let [link-path (str grainstore-dir "/" link-name)
        target-path target]
    (log "Creating symlink:" link-name "→" target)
    (when (.exists (io/file link-path))
      (io/delete-file link-path))
    (let [result (shell/sh "ln" "-s" target-path link-path)]
      (if (zero? (:exit result))
        (success "Created symlink" link-name)
        (error "Failed to create symlink" link-name)))))

(defn sync-submodule
  "Sync a single submodule"
  [submodule]
  (let [{:keys [name type upstream branch target template]} submodule
        target-dir (str grainstore-dir "/" name)
        branch (or branch "main")]
    
    (case type
      ;; Symlink
      :symlink
      (create-symlink target name)
      
      ;; Grainversion (special handling)
      :grainversion
      (do
        (log "Syncing grainversion:" name)
        (if (repo-exists? target-dir)
          (git-pull target-dir)
          (git-clone upstream target-dir branch))
        ;; Add upstream remote pointing to template
        (when template
          (let [template-dir (str grainstore-dir "/" template)]
            (shell/sh "git" "remote" "add" "template" 
                     (str "../" template) :dir target-dir))))
      
      ;; Regular upstream repository
      (if upstream
        (if (repo-exists? target-dir)
          (git-pull target-dir)
          (git-clone upstream target-dir branch))
        (log "Skipping" name "(no upstream)")))))

(defn sync-all-submodules
  "Sync all submodules from manifest"
  [manifest]
  (ensure-grainstore-dir)
  (doseq [submodule (:submodules manifest)]
    (try
      (sync-submodule submodule)
      (catch Exception e
        (error "Failed to sync" (:name submodule) ":" (.getMessage e))))))

(defn list-submodules
  "List all submodules"
  [manifest]
  (println "📦 Grainstore Submodules")
  (println "========================")
  (doseq [submodule (:submodules manifest)]
    (let [{:keys [name type upstream description]} submodule
          status (cond
                   (= type :symlink) "→ symlink"
                   (= type :grainversion) "⭐ grainversion"
                   (repo-exists? (str grainstore-dir "/" name)) "✅ installed"
                   :else "⏳ not installed")]
      (println (format "%-40s %s" name status))
      (when description
        (println (format "  %s" description))))))

(defn check-submodule-status
  "Check status of all submodules"
  [manifest]
  (println "🔍 Checking submodule status...")
  (println "")
  (doseq [submodule (:submodules manifest)]
    (let [{:keys [name type upstream]} submodule
          target-dir (str grainstore-dir "/" name)]
      (cond
        (= type :symlink)
        (if (.exists (io/file (str grainstore-dir "/" name)))
          (success name "- symlink exists")
          (error name "- symlink missing"))
        
        (repo-exists? target-dir)
        (let [result (shell/sh "git" "status" "--porcelain" :dir target-dir)
              changes (str/trim (:out result))]
          (if (empty? changes)
            (success name "- clean")
            (println "⚠️ " name "- has uncommitted changes")))
        
        :else
        (error name "- not cloned")))))

(defn update-gitignore
  "Add .grainstore to .gitignore if not already there"
  []
  (let [gitignore-file ".gitignore"
        grainstore-pattern ".grainstore"]
    (when (.exists (io/file gitignore-file))
      (let [content (slurp gitignore-file)]
        (when-not (str/includes? content grainstore-pattern)
          (log "Adding .grainstore to .gitignore")
          (spit gitignore-file (str content "\n" grainstore-pattern "\n") :append true)
          (success "Updated .gitignore"))))))

(defn create-grainstore-marker
  "Create .grainstore marker file"
  []
  (let [marker-file (str grainstore-dir "/.grainstore")
        content (str "# Grainstore marker file\n"
                    "# This directory contains upstream Grain Network submodules\n"
                    "# Managed by grainstore-sync.bb\n"
                    "# See grainstore-manifest.edn for configuration\n")]
    (spit marker-file content)
    (success "Created .grainstore marker")))

(defn show-help
  []
  (println "🌾 Grainstore Sync - Manage Grain Network submodules")
  (println "")
  (println "Usage: bb grainstore-sync.bb [command]")
  (println "")
  (println "Commands:")
  (println "  sync       Sync all submodules from manifest (default)")
  (println "  list       List all submodules")
  (println "  status     Check status of all submodules")
  (println "  update     Update manifest and sync")
  (println "  help       Show this help message")
  (println "")
  (println "Examples:")
  (println "  bb scripts/grainstore-sync.bb sync")
  (println "  bb scripts/grainstore-sync.bb list")
  (println "  bb scripts/grainstore-sync.bb status"))

(defn -main
  [& args]
  (let [command (or (first args) "sync")
        manifest (read-manifest)]
    
    (when-not manifest
      (error "Could not load manifest from" manifest-file)
      (System/exit 1))
    
    (case command
      "sync"
      (do
        (log "Syncing grainstore submodules...")
        (sync-all-submodules manifest)
        (update-gitignore)
        (create-grainstore-marker)
        (success "Grainstore sync complete!"))
      
      "list"
      (list-submodules manifest)
      
      "status"
      (check-submodule-status manifest)
      
      "update"
      (do
        (log "Updating manifest and syncing...")
        ;; In the future, this could fetch latest manifest from upstream
        (sync-all-submodules manifest)
        (success "Update complete!"))
      
      "help"
      (show-help)
      
      (do
        (error "Unknown command:" command)
        (show-help)
        (System/exit 1)))))

(-main *command-line-args*)


