#!/usr/bin/env bb
(require '[babashka.process :refer [shell]]
         '[clojure.string :as str])

(defn get-current-graintime []
  (let [now (java.time.Instant/now)
        graintime (str "12025-10-24-" (.format (java.time.format.DateTimeFormatter/ofPattern "HHmm") 
                                                (java.time.ZonedDateTime/ofInstant now (java.time.ZoneId/of "America/Los_Angeles"))))]
    graintime))

(defn test-single-repo [repo-name description]
  (println "🌾 Testing grainbranch creation for" repo-name "...")
  
  (let [graintime (get-current-graintime)
        clean-description (-> description
                              (str/replace #"[^a-zA-Z0-9\s-]" "")
                              (str/replace #"\s+" "-")
                              (str/lower-case))
        grainbranch-name (str graintime "-" clean-description)]
    
    (println "📝 Grainbranch name:" grainbranch-name)
    (println "🔗 Repository URL: https://github.com/grain6pbc/" repo-name)
    (println "📋 Description:" description)
    
    ;; Test GitHub API access
    (try
      (let [api-result (shell "gh" "api" (str "repos/grain6pbc/" repo-name))]
        (println "✅ GitHub API access successful")
        (println "📊 Current default branch:" (:out api-result)))
      (catch Exception e
        (println "❌ GitHub API error:" (.getMessage e))))
    
    {:repo repo-name :grainbranch grainbranch-name :graintime graintime}))

(defn -main [& args]
  (println "🧪 Testing grainbranch creation...")
  (test-single-repo "grainbarrel" "Grain Network Build System"))
