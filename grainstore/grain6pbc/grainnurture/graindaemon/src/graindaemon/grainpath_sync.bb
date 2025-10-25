#!/usr/bin/env bb
(ns graindaemon.grainpath-sync
  "Graindaemon for syncing grainpath branches to main and updating GitHub Actions"
  (:require [babashka.process :refer [shell]]
            [babashka.fs :as fs]
            [clojure.string :as str]))

(defn get-current-grainpath-branch
  "Get the current grainpath branch name"
  []
  (let [result (shell {:out :string} "git branch --show-current")]
    (str/trim (:out result))))

(defn get-grainpath-branches
  "Get all grainpath branches"
  []
  (let [result (shell {:out :string} "git branch -r | grep 'grain6-get-started-'")]
    (->> (str/split-lines (:out result))
         (map str/trim)
         (filter #(str/starts-with? % "origin/grain6-get-started-")))))

(defn update-github-action-workflow
  "Update the GitHub Action workflow with new grainpath branch"
  [grainpath-branch]
  (let [workflow-file ".github/workflows/grainpath-sync.yml"
        content (slurp workflow-file)
        ;; Update the branch pattern to include the new branch
        updated-content (str/replace content
                                    #"branches:\s*\n\s*-\s*'grain6-get-started-\*'"
                                    (str "branches:\n      - '" grainpath-branch "'\n      - 'grain6-get-started-*'"))]
    (spit workflow-file updated-content)
    (println (str "✅ Updated GitHub Action workflow with branch: " grainpath-branch))))

(defn sync-grainpath-to-main
  "Sync grainpath branch to main branch"
  [grainpath-branch]
  (println (str "🔄 Syncing grainpath branch to main: " grainpath-branch))
  
  ;; Checkout main branch
  (shell "git checkout main")
  
  ;; Merge grainpath branch into main
  (let [merge-result (shell {:out :string :err :string} 
                            (str "git merge " grainpath-branch))]
    (if (zero? (:exit merge-result))
      (do
        (println "✅ Successfully merged grainpath branch to main")
        (shell "git push origin main"))
      (do
        (println "⚠️ Merge conflicts or no changes to merge")
        (println (:err merge-result))))))

(defn update-default-branch
  "Update GitHub default branch to grainpath branch"
  [grainpath-branch]
  (println (str "🔄 Updating default branch to: " grainpath-branch))
  
  (let [result (shell {:out :string :err :string}
                      (str "gh api repos/kae3g/grainkae3g --method PATCH --field default_branch=" grainpath-branch))]
    (if (zero? (:exit result))
      (println "✅ Successfully updated default branch")
      (do
        (println "❌ Failed to update default branch")
        (println (:err result))))))

(defn create-new-grainpath-branch
  "Create a new grainpath branch with current graintime"
  [course-name]
  (println (str "🌾 Creating new grainpath branch for: " course-name))
  
  ;; Generate graintime
  (let [graintime-result (shell {:out :string} "gt now kae3g")
        graintime (str/trim (:out graintime-result))
        ;; Extract graintime from the output
        graintime-match (re-find #"12025-\d{2}-\d{2}--\d{4}--\w+--moon-\w+------asc-\w+\d{3}--sun-\d+th--kae3g" graintime)
        branch-name (str "grain6-get-started-" graintime-match)]
    
    (if graintime-match
      (do
        ;; Create and checkout new branch
        (shell (str "git checkout -b " branch-name))
        (shell (str "git push -u origin " branch-name))
        
        ;; Update GitHub Action workflow
        (update-github-action-workflow branch-name)
        
        ;; Commit workflow changes
        (shell "git add .github/workflows/grainpath-sync.yml")
        (shell (str "git commit -m 'Update GitHub Action for grainpath branch: " branch-name "'"))
        (shell (str "git push origin " branch-name))
        
        ;; Update default branch
        (update-default-branch branch-name)
        
        (println (str "✅ Created grainpath branch: " branch-name))
        branch-name)
      (do
        (println "❌ Failed to generate graintime")
        nil))))

(defn cleanup-old-grainpath-branches
  "Clean up old grainpath branches (keep only the latest 3)"
  []
  (println "🧹 Cleaning up old grainpath branches...")
  
  (let [branches (get-grainpath-branches)
        sorted-branches (sort > branches)  ; Sort by name (graintime is sortable)
        branches-to-keep (take 3 sorted-branches)
        branches-to-delete (drop 3 sorted-branches)]
    
    (doseq [branch branches-to-delete]
      (let [local-branch (str/replace branch "origin/" "")]
        (println (str "🗑️ Deleting old branch: " local-branch))
        (shell (str "git push origin --delete " local-branch))))
    
    (println (str "✅ Kept " (count branches-to-keep) " latest grainpath branches"))))

(defn main
  "Main graindaemon function"
  [& args]
  (let [command (first args)]
    (case command
      "create" (let [course-name (second args)]
                 (create-new-grainpath-branch course-name))
      
      "sync" (let [grainpath-branch (get-current-grainpath-branch)]
               (sync-grainpath-to-main grainpath-branch))
      
      "cleanup" (cleanup-old-grainpath-branches)
      
      "status" (do
                 (println "📊 Grainpath Branch Status:")
                 (println (str "Current branch: " (get-current-grainpath-branch)))
                 (println "All grainpath branches:")
                 (doseq [branch (get-grainpath-branches)]
                   (println (str "  - " branch))))
      
      (do
        (println "🌾 Grainpath Sync Graindaemon")
        (println "")
        (println "Usage:")
        (println "  bb graindaemon/grainpath-sync.bb create <course-name>")
        (println "  bb graindaemon/grainpath-sync.bb sync")
        (println "  bb graindaemon/grainpath-sync.bb cleanup")
        (println "  bb graindaemon/grainpath-sync.bb status")
        (println "")
        (println "Examples:")
        (println "  bb graindaemon/grainpath-sync.bb create grain6-get-started")
        (println "  bb graindaemon/grainpath-sync.bb sync")
        (println "  bb graindaemon/grainpath-sync.bb cleanup")))))

;; Run main function if called directly
(when (= *file* (str *ns*))
  (apply main *command-line-args*))

