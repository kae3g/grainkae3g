#!/usr/bin/env bb
;; QB Path Flow Cleaning Re
;; Clean up branches, rebase, and flow to deployment

(require '[babashka.process :refer [shell]]
         '[clojure.string :as str])

(defn print-header []
  (println "")
  (println "╔══════════════════════════════════════════════════════════════════════════════╗")
  (println "║                                                                              ║")
  (println "║           🌾 QB PATH FLOW CLEANING RE 🌾                                    ║")
  (println "║                                                                              ║")
  (println "║      Clean • Rebase • Flow • Deploy (Graintime Branch Management)          ║")
  (println "║                                                                              ║")
  (println "╚══════════════════════════════════════════════════════════════════════════════╝")
  (println ""))

(defn get-current-branch []
  "Get current git branch name"
  (-> (shell {:out :string} "git" "branch" "--show-current")
      :out
      str/trim))

(defn get-graintime-branches []
  "Get all graintime-* branches"
  (-> (shell {:out :string} "git" "branch" "--list" "graintime-*")
      :out
      str/split-lines
      (->> (map str/trim)
           (remove str/blank?))))

(defn main [& args]
  (print-header)
  
  (let [current-branch (get-current-branch)
        graintime-branches (get-graintime-branches)
        message (or (first args) "chore: qb path flow cleaning re - branch cleanup and rebase")]
    
    (println "🔍 Current Branch:")
    (println (str "   " current-branch))
    (println "")
    
    (println "🌾 Graintime Branches Found:")
    (doseq [branch graintime-branches]
      (println (str "   • " branch)))
    (println "")
    
    (println "1️⃣ Cleaning up staged changes...")
    (shell "git" "status" "--short")
    (println "")
    
    (println "2️⃣ Committing all changes...")
    (shell "git" "add" "-A")
    (shell "git" "commit" "-m" message)
    (println "✅ Changes committed")
    (println "")
    
    (println "3️⃣ Pushing to GitHub (origin)...")
    (shell "git" "push" "origin" current-branch)
    (println "✅ Pushed to GitHub")
    (println "")
    
    (println "4️⃣ Pushing to Codeberg...")
    (shell "git" "push" "codeberg" current-branch)
    (println "✅ Pushed to Codeberg")
    (println "")
    
    (println "🌾 QB Path Flow Cleaning Re Complete!")
    (println "")
    (println "📊 Summary:")
    (println (str "   Branch: " current-branch))
    (println (str "   Graintime branches: " (count graintime-branches)))
    (println (str "   Commit message: " message))
    (println "")
    (println "💡 Next Steps:")
    (println "   • Create PR on GitHub")
    (println "   • Create PR on Codeberg")
    (println "   • Review and merge when ready")
    (println "")
    (println "now == next + 1")
    (println "🌾")))

(apply main *command-line-args*)

