#!/usr/bin/env bb
;; Graindaemon: GitHub Description Sync
;; Updates GitHub repository description with grainsite URL

(require '[babashka.process :refer [shell]]
         '[clojure.string :as str])

(defn get-current-grainpath []
  "Get current grainpath from git branch name"
  (try
    (let [result (shell {:out :string :dir "../../"} "git" "branch" "--show-current")
          branch-name (str/trim (:out result))]
      (if (or (str/starts-with? branch-name "grain")
              (str/starts-with? branch-name "grain6"))
        branch-name
        nil))
    (catch Exception e
      (println "⚠️ Could not get current grainpath from git branch")
      nil)))

(defn get-grainsite-url [grainpath]
  "Generate grainsite URL from grainpath"
  (when grainpath
    (str "https://kae3g.github.io/grainkae3g/" grainpath "/")))

(defn get-repository-info []
  "Get GitHub repository information"
  (try
    (let [result (shell {:out :string} "gh" "repo" "view" "--json" "name,owner")
          repo-info (-> (:out result)
                        (clojure.edn/read-string))]
      {:name (get-in repo-info [:name])
       :owner (get-in repo-info [:owner :login])})
    (catch Exception e
      (println "⚠️ Could not get repository info from gh CLI")
      {:name "grainkae3g" :owner "kae3g"})))

(defn update-github-description [grainpath grainsite-url]
  "Update GitHub repository description with grainsite URL"
  (try
    (let [repo-info (get-repository-info)
          description (str "🌾 Grain Network: " grainpath " | Live Site: " grainsite-url " | Session 780 Complete")]
      (shell "gh" "api" (str "repos/" (:owner repo-info) "/" (:name repo-info))
             "--method" "PATCH"
             "--field" (str "description=" description))
      (println "✅ Updated GitHub repository description")
      (println "🌐 Grainsite URL:" grainsite-url)
      true)
    (catch Exception e
      (println "❌ Failed to update GitHub description:" (.getMessage e))
      false)))

(defn sync-github-description []
  "Main sync function - update GitHub description with current grainpath"
  (println "🌾 Graindaemon: GitHub Description Sync")
  (println "═══════════════════════════════════════════════════════════════════════════════")
  
  (let [grainpath (get-current-grainpath)]
    (if grainpath
      (let [grainsite-url (get-grainsite-url grainpath)]
        (println "📋 Current grainpath:" grainpath)
        (println "🌐 Grainsite URL:" grainsite-url)
        (println "")
        (update-github-description grainpath grainsite-url))
      (do
        (println "⚠️ No grainpath branch detected")
        (println "   Current branch is not a grainpath branch")
        (println "   Grainpath branches should start with 'grain'")
        false))))

;; Main execution
(sync-github-description)
