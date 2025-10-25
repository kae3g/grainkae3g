(ns grainsync.github
  "GitHub API integration for grainsync.
   
   Handles:
   - Repository description updates
   - Pages URL management
   - Repository metadata"
  (:require [clojure.string :as str]
            [clojure.java.shell :as shell]
            [clj-http.client :as http]))

;; Configuration
(def github-api-base "https://api.github.com")
(def repo-owner "kae3g")
(def repo-name "grainkae3g")

;; Pure Functions

(defn get-github-token
  "Get GitHub token from environment or git config"
  []
  (or (System/getenv "GITHUB_TOKEN")
      (-> (shell/sh "git" "config" "--get" "github.token")
          :out
          str/trim)
      (throw (ex-info "GitHub token not found. Set GITHUB_TOKEN env var or git config github.token" {}))))

(defn build-github-url
  "Build GitHub API URL for repository"
  [endpoint]
  (str github-api-base "/repos/" repo-owner "/" repo-name endpoint))

(defn get-current-description
  "Get current repository description"
  []
  (let [token (get-github-token)
        url (build-github-url "")
        response (http/get url
                          {:headers {"Authorization" (str "token " token)
                                     "Accept" "application/vnd.github.v3+json"}})]
    (when (= 200 (:status response))
      (get-in response [:body :description]))))

(defn build-course-description
  "Build repository description with course information"
  [grainpath course-title pages-url]
  (let [base-description "🌾 Grain ecosystem: time-aware, decentralized, sustainable computing. Clojure + s6 + Wayland + ICP + Hedera + Solana. No ETH."
        course-info (str " | Latest: " course-title " (" grainpath ") | " pages-url)]
    (str base-description course-info)))

;; Side Effects

(defn update-repo-description!
  "Update GitHub repository description"
  [grainpath course-title pages-url]
  (try
    (let [token (get-github-token)
          url (build-github-url "")
          new-description (build-course-description grainpath course-title pages-url)
          
          response (http/patch url
                              {:headers {"Authorization" (str "token " token)
                                         "Accept" "application/vnd.github.v3+json"
                                         "Content-Type" "application/json"}
                               :body (str "{\"description\":\"" new-description "\"}")})]
      
      (if (= 200 (:status response))
        (do
          (println "✅ GitHub repository description updated")
          (println "   New description: " new-description)
          true)
        (do
          (println "❌ Failed to update GitHub description")
          (println "   Status: " (:status response))
          (println "   Response: " (:body response))
          false)))
    
    (catch Exception e
      (println "❌ Error updating GitHub description:")
      (println "   " (.getMessage e))
      false)))

(defn update-repo-description-with-course!
  "Update repository description with course information"
  [grainpath course-title]
  (let [pages-url (str "https://kae3g.github.io/grainkae3g/" grainpath "/")]
    (update-repo-description! grainpath course-title pages-url)))

(comment
  ;; Example usage
  (update-repo-description-with-course! 
    "12025-10-23--0145--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g"
    "Advanced Clojure Development")
  )
