#!/usr/bin/env bb

(require '[clojure.string :as str]
         '[clojure.java.shell :as shell])

(defn run-cmd
  "Run shell command and return result"
  [& args]
  (let [result (apply shell/sh args)]
    (if (zero? (:exit result))
      {:success true
       :output (str/trim (:out result))
       :error nil}
      {:success false
       :output (:out result)
       :error (:err result)
       :exit (:exit result)})))

(defn get-current-branch
  "Get current git branch name"
  []
  (let [result (run-cmd "git" "branch" "--show-current")]
    (when (:success result)
      (:output result))))

(defn set-upstream-tracking
  "Set upstream tracking for current branch
  
  Equivalent to: git push --set-upstream origin BRANCH"
  [branch-name]
  (println "📡 Setting upstream tracking...")
  (let [result (run-cmd "git" "push" "--set-upstream" "origin" branch-name)]
    (if (:success result)
      (do
        (println "   ✓ Upstream set: origin/" branch-name)
        true)
      (do
        (println "   ❌ Failed to set upstream:")
        (println "     " (:error result))
        false))))

(defn set-github-default-branch
  "Set GitHub repository default branch using gh CLI
  
  Equivalent to: gh repo edit --default-branch BRANCH"
  [branch-name]
  (println "🌐 Setting GitHub default branch...")
  (let [result (run-cmd "gh" "repo" "edit" "--default-branch" branch-name)]
    (if (:success result)
      (do
        (println "   ✓ GitHub default branch set:" branch-name)
        (println "     " (:output result))
        true)
      (do
        (println "   ❌ Failed to set GitHub default:")
        (println "     " (:error result))
        false))))

(defn update-repo-description
  "Update GitHub repository description with grainbranch URL
  
  Format: 'grainURL: https://github.com/USER/REPO/tree/BRANCH/'"
  [branch-name]
  (println "📝 Updating repository description...")
  
  ;; Get current repo info
  (let [repo-result (run-cmd "gh" "repo" "view" "--json" "nameWithOwner" "--jq" ".nameWithOwner")]
    (if (:success repo-result)
      (let [repo-name (:output repo-result)
            grain-url (str "https://github.com/" repo-name "/tree/" branch-name "/")
            description (str "grainURL: " grain-url)]
        
        (println "   Repository:" repo-name)
        (println "   grainURL:" grain-url)
        
        (let [result (run-cmd "gh" "repo" "edit" "--description" description)]
          (if (:success result)
            (do
              (println "   ✓ Description updated")
              true)
            (do
              (println "   ❌ Failed to update description:")
              (println "     " (:error result))
              false))))
      (do
        (println "   ❌ Could not get repo info")
        false))))

(defn set-default-grainbranch
  "Complete grainbranch setup: upstream + GitHub default + description
  
  Usage:
    bb scripts/set-default-grainbranch.bb
    bb scripts/set-default-grainbranch.bb BRANCH_NAME"
  ([]
   (if-let [branch (get-current-branch)]
     (set-default-grainbranch branch)
     (do
       (println "❌ Could not determine current branch")
       (System/exit 1))))
  
  ([branch-name]
   (println "\n🌾 Setting default grainbranch:" branch-name "\n")
   
   (let [upstream-ok? (set-upstream-tracking branch-name)
         github-ok? (set-github-default-branch branch-name)
         desc-ok? (update-repo-description branch-name)]
     
     (println "\n═══════════════════════════════════════════")
     (println "Summary:")
     (println "  Upstream tracking:" (if upstream-ok? "✓" "✗"))
     (println "  GitHub default:" (if github-ok? "✓" "✗"))
     (println "  Description:" (if desc-ok? "✓" "✗"))
     (println "═══════════════════════════════════════════")
     
     (if (and upstream-ok? github-ok? desc-ok?)
       (do
         (println "\n✅ Grainbranch setup complete!")
         (println "\nRepo will now load from:")
         (println "  https://github.com/[owner]/[repo]/tree/" branch-name "/")
         (System/exit 0))
       (do
         (println "\n⚠️  Some steps failed - check errors above")
         (System/exit 1))))))

;; Main execution
(if *command-line-args*
  (set-default-grainbranch (first *command-line-args*))
  (set-default-grainbranch))

