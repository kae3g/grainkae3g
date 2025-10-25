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

(defn deploy-to-github []
  (let [config (get-course-config)
        org (get-in config [:deployment :github :org])
        repo (get-in config [:deployment :github :repo])
        branch (get-in config [:deployment :github :branch])
        output-dir (get config :output-dir "public")]
    
    (println "Deploying to GitHub Pages...")
    (println "Organization:" org)
    (println "Repository:" repo)
    (println "Branch:" branch)
    
    ;; Initialize git in output directory if needed
    (when-not (.exists (io/file output-dir ".git"))
      (shell {:dir output-dir} "git init"))
    
    ;; Add GitHub remote
    (shell {:dir output-dir} 
           (str "git remote add origin https://github.com/" org "/" repo ".git"))
    
    ;; Add all files
    (shell {:dir output-dir} "git add .")
    
    ;; Commit
    (shell {:dir output-dir} 
           "git commit -m 'Deploy course to GitHub Pages'")
    
    ;; Push to gh-pages branch
    (shell {:dir output-dir} 
           (str "git push -f origin main:" branch))
    
    (println "✅ Deployed to GitHub Pages!")
    (println "URL: https://" org ".github.io/" repo "/")))

(defn main []
  (try
    (deploy-to-github)
    (catch Exception e
      (println "❌ Deployment failed:" (.getMessage e))
      (System/exit 1))))

(main)

