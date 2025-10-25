#!/usr/bin/env bb

(require '[clojure.java.io :as io]
         '[clojure.string :as str]
         '[babashka.process :refer [shell]])

(def config-file "personal/grain-network-course/course.edn")

(defn load-config []
  (when-not (.exists (io/file config-file))
    (throw (ex-info "Course config not found" {:file config-file})))
  (read-string (slurp config-file)))

(defn get-course-config []
  (get-in (load-config) [:course]))

(defn deploy-to-codeberg []
  (let [config (get-course-config)
        org (get-in config [:deployment :codeberg :org])
        repo (get-in config [:deployment :codeberg :repo])
        branch (get-in config [:deployment :codeberg :branch])
        output-dir (get config :output-dir "public")]
    
    (println "Deploying to Codeberg Pages...")
    (println "Organization:" org)
    (println "Repository:" repo)
    (println "Branch:" branch)
    
    ;; Initialize git in output directory if needed
    (when-not (.exists (io/file output-dir ".git"))
      (shell {:dir output-dir} "git init"))
    
    ;; Add Codeberg remote
    (shell {:dir output-dir} 
           (str "git remote add codeberg https://codeberg.org/" org "/" repo ".git"))
    
    ;; Add all files
    (shell {:dir output-dir} "git add .")
    
    ;; Commit
    (shell {:dir output-dir} 
           "git commit -m 'Deploy course to Codeberg Pages'")
    
    ;; Push to pages branch
    (shell {:dir output-dir} 
           (str "git push -f codeberg main:" branch))
    
    (println "✅ Deployed to Codeberg Pages!")
    (println "URL: https://" org ".codeberg.page/" repo "/")))

(defn main []
  (try
    (deploy-to-codeberg)
    (catch Exception e
      (println "❌ Deployment failed:" (.getMessage e))
      (System/exit 1))))

(main)
