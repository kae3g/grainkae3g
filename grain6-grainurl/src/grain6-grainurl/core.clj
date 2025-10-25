(ns grain6-grainurl.core
  "Grain6 grainurl module inspired by grainpath with GitHub Pages deployment"
  (:require [clojure.string :as str]
            [clj-time.core :as time]
            [clj-time.format :as time-format]
            [clj-http.client :as http]
            [babashka.fs :as fs]))

(def grain6-sheaves
  "88 grain6 sheaves in unsorted set, with tde9n as 1-of-88"
  (set (map #(str "sheaf-" (format "%02d" %)) (range 1 89))))

(def tde9n-sheaf
  "TDE9N sheaf as 1-of-88 in the unsorted set"
  {:sheaf-id "tde9n"
   :sheaf-number 1
   :total-sheaves 88
   :position "1-of-88"
   :grain6-version "0.1.0"
   :status :hypothetical
   :grainurl-enabled true})

(def grainurl-config
  "Configuration for grain6-grainurl module"
  {:github-pages-base "https://kae3g.github.io/grainkae3g"
   :codeberg-pages-base "https://kae3g.codeberg.page/grainkae3g"
   :ubuntu-version "24.04 LTS"
   :framework "grain6"
   :deployment-strategy "github-actions"
   :grainenvvars-integration true
   :88-counter-philosophy true})

(defn generate-grainurl
  "Generate grainurl for a grain6 sheaf"
  [sheaf-id graintime]
  (let [grainpath (str graintime "/" sheaf-id)
        github-url (str (:github-pages-base grainurl-config) "/" grainpath)
        codeberg-url (str (:codeberg-pages-base grainurl-config) "/" grainpath)]
    {:sheaf-id sheaf-id
     :grainpath grainpath
     :grainurl {:github github-url
                :codeberg codeberg-url}
     :generated-at (time/now)
     :88-counter-position (mod (hash sheaf-id) 88)}))

(defn create-tde9n-sheaf
  "Create tde9n sheaf (1-of-88) with grainurl"
  []
  (let [graintime (str "12025-10-23--0319--PDT--moon-vishakha------asc-gem000--sun-03rd--kae3g")
        grainurl (generate-grainurl "tde9n" graintime)]
    {:tde9n-sheaf tde9n-sheaf
     :grainurl grainurl
     :deployment-ready true
     :github-actions-config {:ubuntu-version "24.04 LTS"
                             :framework "grain6"
                             :grainenvvars-integration true}}))

(defn update-github-description
  "Update GitHub repository description with grainpath URL"
  [grainpath-url]
  (try
    (let [token (System/getenv "GITHUB_TOKEN")
          url "https://api.github.com/repos/kae3g/grainkae3g"
          new-description (str "Grain Network personal repository - " grainpath-url)
          
          response (http/patch url
                              {:headers {"Authorization" (str "token " token)
                                        "Accept" "application/vnd.github.v3+json"
                                        "Content-Type" "application/json"}
                               :body (str "{\"description\":\"" new-description "\"}")})]
      
      (if (= 200 (:status response))
        {:success true
         :message "GitHub repository description updated"
         :new-description new-description
         :grainpath-url grainpath-url}
        {:success false
         :message (str "Failed to update GitHub description: " (:status response))
         :response (:body response)}))
    
    (catch Exception e
      {:success false
       :message (str "Error updating GitHub description: " (.getMessage e))})))

(defn format-grainurl-report
  "Format grainurl generation report"
  [report]
  (let [formatter (time-format/formatter "yyyy-MM-dd HH:mm:ss")]
    (str "🌾 Grain6 GrainURL Report\n"
         "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n"
         "Generated: " (time-format/unparse formatter (:generated-at report)) "\n"
         "Sheaf ID: " (:sheaf-id report) "\n"
         "Grainpath: " (:grainpath report) "\n"
         "88 Counter Position: " (:88-counter-position report) "\n\n"
         "GrainURLs:\n"
         "  GitHub Pages: " (get-in report [:grainurl :github]) "\n"
         "  Codeberg Pages: " (get-in report [:grainurl :codeberg]) "\n")))

(defn format-tde9n-report
  "Format tde9n sheaf creation report"
  [report]
  (let [formatter (time-format/formatter "yyyy-MM-dd HH:mm:ss")]
    (str "🌾 TDE9N Sheaf Creation Report\n"
         "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n"
         "Sheaf ID: " (:sheaf-id (:tde9n-sheaf report)) "\n"
         "Position: " (:position (:tde9n-sheaf report)) "\n"
         "Total Sheaves: " (:total-sheaves (:tde9n-sheaf report)) "\n"
         "Grain6 Version: " (:grain6-version (:tde9n-sheaf report)) "\n"
         "Status: " (name (:status (:tde9n-sheaf report))) "\n"
         "Deployment Ready: " (:deployment-ready report) "\n\n"
         "GrainURL:\n"
         "  GitHub Pages: " (get-in report [:grainurl :grainurl :github]) "\n"
         "  Codeberg Pages: " (get-in report [:grainurl :grainurl :codeberg]) "\n\n"
         "GitHub Actions Config:\n"
         "  Ubuntu Version: " (:ubuntu-version (:github-actions-config report)) "\n"
         "  Framework: " (:framework (:github-actions-config report)) "\n"
         "  Grainenvvars Integration: " (:grainenvvars-integration (:github-actions-config report)) "\n")))
