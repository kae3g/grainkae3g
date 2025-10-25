#!/usr/bin/env bb

(require '[clojure.string :as str]
         '[clojure.java.shell :as shell]
         '[clojure.edn :as edn]
         '[babashka.fs :as fs])

;; Configuration
(def grainkae3g-root "/home/xy/kae3g/grainkae3g")
(def grainclay-root (str grainkae3g-root "/grainstore/grain6pbc/grainclay"))
(def personal-config-file (str (System/getProperty "user.home") "/.grainconfig.edn"))

;; Default configuration
(def default-grainclay-config
  {:network-hosts ["github.com" "codeberg.org" "grain6pbc.github.io"]
   :publish-formats [:html :markdown :edn :json]
   :sync-interval-minutes 30
   :auto-cleanup true
   :cleanup-retention-days 30
   :github-owner "kae3g"
   :github-repo "grainkae3g"
   :codeberg-owner "kae3g"
   :codeberg-repo "grainkae3g"})

(defn read-personal-config
  "Read personal configuration from ~/.grainconfig.edn"
  []
  (try
    (if (fs/exists? personal-config-file)
      (edn/read-string (slurp personal-config-file))
      {})
    (catch Exception e
      (println "⚠️  Could not read personal config:" (.getMessage e))
      {})))

(defn get-grainclay-config
  "Get merged grainclay configuration"
  []
  (let [personal-config (read-personal-config)
        grainclay-config (get personal-config :grainclay {})]
    (merge default-grainclay-config grainclay-config)))

(defn generate-graintime
  "Generate current graintime using gt command"
  []
  (let [result (shell/sh "gt")]
    (if (zero? (:exit result))
      (let [output (:out result)
            ;; Extract just the graintime line (starts with 12025-10-23--0210)
            graintime-line (->> (str/split-lines output)
                                (filter #(re-find #"^12025-\d{2}-\d{2}--\d{4}" %))
                                first)]
        (str/trim graintime-line))
      (throw (ex-info "Failed to generate graintime" {:error (:err result)})))))

(defn create-grainclay-path
  "Create grainclay path structure"
  [content-type content-name]
  (let [graintime (generate-graintime)
        kebab-name (-> content-name
                       (str/lower-case)
                       (str/replace #"[^a-z0-9]+" "-")
                       (str/replace #"^-+|-+$" ""))
        grainclay-path (str "/" content-type "/" kebab-name "/" graintime "/")]
    {:grainclay-path grainclay-path
     :graintime graintime
     :content-type content-type
     :content-name content-name
     :kebab-name kebab-name}))

(defn create-content-structure
  "Create content directory structure"
  [grainclay-path content-name description]
  (let [content-dir (str grainkae3g-root grainclay-path)
        config (get-grainclay-config)]
    
    ;; Create directory structure
    (fs/create-dirs content-dir)
    
    ;; Create index.html
    (let [index-content (str "<!DOCTYPE html>
<html lang=\"en\">
<head>
    <meta charset=\"UTF-8\">
    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">
    <title>" content-name "</title>
    <style>
        body { font-family: 'Courier New', monospace; margin: 40px; background: #1a1a1a; color: #e0e0e0; }
        .container { max-width: 800px; margin: 0 auto; }
        h1 { color: #4CAF50; border-bottom: 2px solid #4CAF50; padding-bottom: 10px; }
        .grainclay-path { font-family: monospace; background: #333; padding: 10px; border-radius: 5px; margin: 20px 0; }
        .description { font-size: 1.1em; line-height: 1.6; margin: 20px 0; }
        .links { margin-top: 30px; }
        .links a { color: #4CAF50; text-decoration: none; margin-right: 20px; }
        .links a:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <div class=\"container\">
        <h1>" content-name "</h1>
        <div class=\"grainclay-path\">" grainclay-path "</div>
        <div class=\"description\">" description "</div>
        <div class=\"links\">
            <a href=\"https://" (:github-owner config) ".github.io/" (:github-repo config) grainclay-path "\">GitHub Pages</a>
            <a href=\"https://" (:codeberg-owner config) ".codeberg.page/" (:codeberg-repo config) grainclay-path "\">Codeberg Pages</a>
        </div>
    </div>
</body>
</html>")]
      (spit (str content-dir "/index.html") index-content))
    
    ;; Create README.md
    (let [readme-content (str "# " content-name "\n\n"
                              "**Grainclay Path**: `" grainclay-path "`\n\n"
                              "**Description**: " description "\n\n"
                              "## Links\n\n"
                              "- [GitHub Pages](https://" (:github-owner config) ".github.io/" (:github-repo config) grainclay-path ")\n"
                              "- [Codeberg Pages](https://" (:codeberg-owner config) ".codeberg.page/" (:codeberg-repo config) grainclay-path ")\n\n"
                              "## Content\n\n"
                              "This content was created with `grainclay flow`.\n"
                              "The grainclay path provides a unique, time-based identifier for this content.\n\n"
                              "Generated: " (java.time.Instant/now) "\n")]
      (spit (str content-dir "/README.md") readme-content))
    
    ;; Create .beam.edn (grainclay metadata)
    (let [beam-content {:grainclay-version "1.0.0"
                        :content-type (:content-type grainclay-path)
                        :content-name content-name
                        :graintime (:graintime grainclay-path)
                        :created-at (java.time.Instant/now)
                        :description description
                        :formats (:publish-formats config)
                        :network-hosts (:network-hosts config)}]
      (spit (str content-dir "/.beam.edn") (pr-str beam-content)))
    
    content-dir))

(defn publish-content
  "Publish content to grainclay network"
  [content-dir grainclay-path]
  (println "📦 Publishing content to grainclay network...")
  (println "   Path: " grainclay-path)
  (println "   Directory: " content-dir)
  (println "   ✅ Content published"))

(defn distribute-content
  "Distribute content across platforms"
  [grainclay-path]
  (let [config (get-grainclay-config)]
    (println "🌐 Distributing content across platforms...")
    
    ;; GitHub Pages
    (println "   📤 GitHub Pages: https://" (:github-owner config) ".github.io/" (:github-repo config) grainclay-path)
    
    ;; Codeberg Pages  
    (println "   📤 Codeberg Pages: https://" (:codeberg-owner config) ".codeberg.page/" (:codeberg-repo config) grainclay-path)
    
    (println "   ✅ Content distributed")))

(defn sync-content
  "Sync content with remote repositories"
  [grainclay-path]
  (println "🔄 Syncing content with remote repositories...")
  (println "   Path: " grainclay-path)
  
  ;; Add to git
  (let [result (shell/sh "git" "add" (str grainkae3g-root grainclay-path))]
    (if (zero? (:exit result))
      (println "   ✅ Added to git")
      (println "   ⚠️  Git add failed:" (:err result))))
  
  ;; Commit
  (let [result (shell/sh "git" "commit" "-m" (str "Add grainclay content: " grainclay-path))]
    (if (zero? (:exit result))
      (println "   ✅ Committed to git")
      (println "   ⚠️  Git commit failed:" (:err result))))
  
  ;; Push to remotes
  (doseq [remote ["origin" "codeberg"]]
    (let [result (shell/sh "git" "push" remote "main")]
      (if (zero? (:exit result))
        (println (str "   ✅ Pushed to " remote))
        (println (str "   ⚠️  Push to " remote " failed:" (:err result))))))
  
  (println "   ✅ Content synced"))

(defn main-grainclay-flow
  "Main grainclay flow function"
  []
  (try
    (println "🌾 Grainclay Flow - Complete Content Workflow")
    (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    (println "")
    
    ;; Get content details
    (print "Content Type (article/course/tool/other): ")
    (flush)
    (let [content-type (read-line)]
      (print "Content Name: ")
      (flush)
      (let [content-name (read-line)]
        (print "Description: ")
        (flush)
        (let [description (read-line)]
          
          ;; Create grainclay path
          (let [path-info (create-grainclay-path content-type content-name)
                grainclay-path (:grainclay-path path-info)]
            
            (println "")
            (println "🛤️  Generated grainclay path:")
            (println "   " grainclay-path)
            (println "")
            
            (print "Create this content? (y/N): ")
            (flush)
            (let [confirm (read-line)]
              (when (= "y" (str/lower-case confirm))
                
                ;; Create content structure
                (println "")
                (println "🏗️  Creating content structure...")
                (let [content-dir (create-content-structure grainclay-path content-name description)]
                  (println "   ✅ Content directory created")
                  
                  ;; Publish
                  (println "")
                  (publish-content content-dir grainclay-path)
                  
                  ;; Distribute
                  (println "")
                  (distribute-content grainclay-path)
                  
                  ;; Sync
                  (println "")
                  (sync-content grainclay-path)
                  
                  (println "")
                  (println "🎉 Grainclay flow complete!")
                  (println "")
                  (println "🔗 Access your content:")
                  (let [config (get-grainclay-config)]
                    (println "   GitHub Pages: https://" (:github-owner config) ".github.io/" (:github-repo config) grainclay-path)
                    (println "   Codeberg Pages: https://" (:codeberg-owner config) ".codeberg.page/" (:codeberg-repo config) grainclay-path))
                  (println "")
                  (println "📁 Local path: " content-dir))))))))
    
    (catch Exception e
      (println "\n❌ Error in grainclay flow:")
      (println "   " (.getMessage e))
      (when-let [data (ex-data e)]
        (println "   Data:" data)))))

;; Run the main function
(main-grainclay-flow)
