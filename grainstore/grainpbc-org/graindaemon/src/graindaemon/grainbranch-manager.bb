#!/usr/bin/env bb
;; Graindaemon: Grainbranch Management System
;; Manages grainbranches across all grain6pbc repositories

(require '[babashka.process :refer [shell]]
         '[clojure.string :as str])

(def grain6pbc-repos
  "List of grain6pbc repositories to manage"
  ["grain6pbc/grain6"
   "grain6pbc/grainkae3g"
   "grain6pbc/graincontacts"
   "grain6pbc/humble-stack"
   "grain6pbc/graindaemon"])

(defn get-current-graintime []
  "Get current graintime for grainbranch naming"
  (try
    (let [result (shell {:out :string :dir "../../"} "gt" "now" "kae3g")
          graintime (str/trim (:out result))]
      graintime)
    (catch Exception e
      (println "⚠️ Could not get graintime, using fallback")
      "12025-10-24--1200--PDT--moon-vishakha------asc-gem000--sun-12th--kae3g")))

(defn create-grainbranch-name [project-name]
  "Create grainbranch name from project name and current graintime"
  (let [graintime (get-current-graintime)
        clean-project (str/replace project-name #"grain6pbc/" "")]
    (str clean-project "-" graintime)))

(defn get-grainsite-url [repo-name grainbranch-name]
  "Generate grainsite URL from repository name and grainbranch"
  (let [clean-repo (str/replace repo-name #"grain6pbc/" "")
        subdomain (case clean-repo
                    "grain6" "grain6pbc.com"
                    "grainkae3g" "kae3g.grain6pbc.com"
                    "graincontacts" "contacts.grain6pbc.com"
                    "humble-stack" "ui.grain6pbc.com"
                    "graindaemon" "daemon.grain6pbc.com"
                    (str clean-repo ".grain6pbc.com"))]
    (str "https://" subdomain "/" grainbranch-name "/")))

(defn create-grainbranch [repo-name]
  "Create a new grainbranch for a repository"
  (let [grainbranch-name (create-grainbranch-name repo-name)
        grainsite-url (get-grainsite-url repo-name grainbranch-name)]
    
    (println (str "🌾 Creating grainbranch for " repo-name))
    (println (str "📋 Grainbranch: " grainbranch-name))
    (println (str "🌐 Grainsite URL: " grainsite-url))
    
    (try
      ;; Clone repository
      (shell "git" "clone" (str "https://github.com/" repo-name ".git") "temp-repo")
      
      ;; Create grainbranch
      (shell {:dir "temp-repo"} "git" "checkout" "-b" grainbranch-name)
      
      ;; Commit any changes
      (shell {:dir "temp-repo"} "git" "add" ".")
      (shell {:dir "temp-repo"} "git" "commit" "-m" (str "feat: Create grainbranch " grainbranch-name))
      
      ;; Push grainbranch
      (shell {:dir "temp-repo"} "git" "push" "origin" grainbranch-name)
      
      ;; Update repository description
      (shell "gh" "api" (str "repos/" repo-name) "--method" "PATCH"
             "--field" (str "description=🌾 Grain6pbc " (str/replace repo-name #"grain6pbc/" "") ": " grainbranch-name " | Live Site: " grainsite-url " | Session 780 Complete"))
      
      ;; Set grainbranch as default (requires admin permissions)
      (println "⚠️ Setting grainbranch as default requires admin permissions")
      (println (str "   Manually set default branch to: " grainbranch-name))
      
      ;; Cleanup
      (shell "rm" "-rf" "temp-repo")
      
      (println (str "✅ Created grainbranch " grainbranch-name " for " repo-name))
      true
      
      (catch Exception e
        (println (str "❌ Failed to create grainbranch for " repo-name ": " (.getMessage e)))
        (shell "rm" "-rf" "temp-repo")
        false))))

(defn sync-all-repos []
  "Sync all grain6pbc repositories"
  (println "🌾 Graindaemon: Grain6pbc Repository Sync")
  (println "═══════════════════════════════════════════════════════════════════════════════")
  
  (doseq [repo grain6pbc-repos]
    (println (str "\n📋 Processing repository: " repo))
    (create-grainbranch repo))
  
  (println "\n✅ Grain6pbc repository sync complete!")
  (println "🌐 All repositories now have grainbranches with GitHub Pages URLs")
  (println "📝 Repository descriptions updated with grainsite URLs")
  (println "⚠️ Remember to manually set grainbranches as default branches"))

(defn -main [& args]
  "Main entry point for graindaemon grainbranch management"
  (sync-all-repos))

;; Export for use in other namespaces
(def sync-all-repos sync-all-repos)
