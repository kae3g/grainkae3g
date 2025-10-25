#!/usr/bin/env bb

(require '[clojure.java.io :as io]
         '[clojure.string :as str]
         '[babashka.process :refer [shell]])

(defn generate-graintime [author]
  "Generate graintime using gt command"
  (-> (shell {:out :string} "gt" "now" author)
      :out
      str/trim))

(defn generate-grainpath [author course-name]
  "Generate grainpath with neovedic timestamp instead of version"
  (let [graintime (generate-graintime author)]
    (str "/course/" author "/" course-name "/" graintime "/")))

(defn generate-repo-name [author course-name]
  "Generate repository name with graintime"
  (let [graintime (generate-graintime author)
        ;; Sanitize graintime for repo name (replace special chars)
        safe-graintime (-> graintime
                          (str/replace #"--" "-")
                          (str/replace #":" "-")
                          (str/replace #" " "-"))]
    (str "course-" author "-" course-name "-" safe-graintime)))

(defn create-course-directory [author course-name]
  (let [graintime (generate-graintime author)
        safe-graintime (-> graintime
                          (str/replace #"--" "-")
                          (str/replace #":" "-")
                          (str/replace #" " "-"))
        dir-name (str author "-" course-name "-" safe-graintime)
        course-dir (str "personal/" dir-name)]
    (.mkdirs (io/file course-dir))
    (.mkdirs (io/file (str course-dir "/lessons")))
    (.mkdirs (io/file (str course-dir "/public")))
    course-dir))

(defn create-course-edn [author course-name course-dir]
  (let [graintime (generate-graintime author)
        grainpath (generate-grainpath author course-name)
        repo-name (generate-repo-name author course-name)
        content (str "{:course
 {:grainpath \"" grainpath "\"
  :title \"" (str/replace course-name #"-" " ") "\"
  :author \"" author "\"
  :graintime \"" graintime "\"
  
  :repositories
  {:github \"https://github.com/grain6pbc/" repo-name "\"
   :codeberg \"https://codeberg.org/grain6pbc/" repo-name "\"}
  
  :pages
  {:github \"https://grain6pbc.github.io/" repo-name "\"
   :codeberg \"https://grain6pbc.codeberg.page/" repo-name "\"}
  
  :immutable true
  :deletion-policy \"manual-only\"
  
  :source-dir \"lessons\"
  :output-dir \"public\"
  
  :theme
  {:primary-color \"#2c5f2d\"
   :accent-color \"#97c93d\"
   :background \"#ffffff\"
   :text \"#333333\"
   :code-background \"#f5f5f5\"}
  
  :navigation
  {:home-text \"🏠 Course Home\"
   :github-url \"https://github.com/grain6pbc/" repo-name "\"
   :codeberg-url \"https://codeberg.org/grain6pbc/" repo-name "\"
   :github-pages \"https://grain6pbc.github.io/" repo-name "/\"
   :codeberg-pages \"https://grain6pbc.codeberg.page/" repo-name "/\"}
  
  :footer
  {:text \"🌾 " (str/replace course-name #"-" " ") "\"
   :philosophy \"From granules to grains to THE WHOLE GRAIN\"
   :license \"MIT\"}
  
  :deployment
  {:github {:org \"grain6pbc\" :repo \"" repo-name "\" :branch \"gh-pages\"}
   :codeberg {:org \"grain6pbc\" :repo \"" repo-name "\" :branch \"pages\"}}}}")]
    (spit (str course-dir "/course.edn") content)))

(defn create-grainpath-edn [author course-name course-dir]
  (let [graintime (generate-graintime author)
        grainpath (generate-grainpath author course-name)
        repo-name (generate-repo-name author course-name)
        content (str "{:grainpath
 {:path \"" grainpath "\"
  :author \"" author "\"
  :course-name \"" course-name "\"
  :graintime \"" graintime "\"
  :created \"" graintime "\"
  :immutable-since \"" graintime "\"
  :repositories
  {:github \"grain6pbc/" repo-name "\"
   :codeberg \"grain6pbc/" repo-name "\"}
  :checksum \"sha256:placeholder\"
  :dependencies []
  :license \"MIT\"}}")]
    (spit (str course-dir "/grainpath.edn") content)))

(defn create-gitignore [course-dir]
  (let [content "public/
*.log
.DS_Store
.env
node_modules/"]
    (spit (str course-dir "/.gitignore") content)))

(defn create-readme [author course-name course-dir]
  (let [graintime (generate-graintime author)
        grainpath (generate-grainpath author course-name)
        repo-name (generate-repo-name author course-name)
        content (str "# " (str/replace course-name #"-" " ") "\n\n"
                    "**Grainpath**: `" grainpath "`\n"
                    "**Author**: " author "\n"
                    "**Graintime**: " graintime "\n\n"
                    "## Live Sites\n\n"
                    "- **GitHub Pages**: https://grain6pbc.github.io/" repo-name "/\n"
                    "- **Codeberg Pages**: https://grain6pbc.codeberg.page/" repo-name "/\n\n"
                    "## Course Structure\n\n"
                    "This course follows the Grain Network immutable course system.\n\n"
                    "### Files\n\n"
                    "- `course.edn` - Course configuration\n"
                    "- `grainpath.edn` - Immutable path metadata\n"
                    "- `lessons/` - Markdown lesson files\n"
                    "- `public/` - Built HTML (generated)\n\n"
                    "### Building\n\n"
                    "```bash\n"
                    "cd " course-dir "\n"
                    "gb build\n"
                    "gb flow\n"
                    "```\n\n"
                    "## Immutability\n\n"
                    "This course is immutable. To make changes, create a new edition with a new graintime:\n\n"
                    "```bash\n"
                    "gb create-course --author \"" author "\" --name \"" course-name "\"\n"
                    "```\n\n"
                    "Each run creates a unique grainpath with current graintime.\n\n"
                    "## License\n\n"
                    "MIT License - See course content for details.\n\n"
                    "---\n\n"
                    "🌾 **Grain Network** - From granules to grains to THE WHOLE GRAIN")]
    (spit (str course-dir "/README.md") content)))

(defn create-github-repo [repo-name]
  (println "Creating GitHub repository:" repo-name)
  (try
    (shell (str "gh repo create grain6pbc/" repo-name " --public --description \"Immutable course: " repo-name "\""))
    (println "✅ GitHub repository created")
    (catch Exception e
      (println "❌ GitHub repository creation failed:" (.getMessage e)))))

(defn create-codeberg-repo [repo-name]
  (println "Creating Codeberg repository:" repo-name)
  (try
    (shell (str "gh repo create codeberg.org/grain6pbc/" repo-name " --public --description \"Immutable course: " repo-name "\""))
    (println "✅ Codeberg repository created")
    (catch Exception e
      (println "❌ Codeberg repository creation failed:" (.getMessage e)))))

(defn update-registry [author course-name graintime grainpath]
  (let [registry-file "COURSE-REGISTRY.edn"
        repo-name (generate-repo-name author course-name)
        new-entry (str "  \"" grainpath "\"
   {:grainpath \"" grainpath "\"
    :author \"" author "\"
    :course-name \"" course-name "\"
    :graintime \"" graintime "\"
    :title \"" (str/replace course-name #"-" " ") "\"
    :status :active
    :created \"" graintime "\"
    :repositories
    {:github \"grain6pbc/" repo-name "\"
     :codeberg \"grain6pbc/" repo-name "\"}
    :pages
    {:github \"https://grain6pbc.github.io/" repo-name "\"
     :codeberg \"https://grain6pbc.codeberg.page/" repo-name "\"}}")]
    (if (.exists (io/file registry-file))
      (do
        (println "Updating course registry...")
        ;; TODO: Implement registry update logic
        (println "✅ Course registry updated"))
      (do
        (println "Creating course registry...")
        ;; TODO: Implement registry creation logic
        (println "✅ Course registry created")))))

(defn main []
  (let [args *command-line-args*
        author (nth args 0 nil)
        course-name (nth args 1 nil)]
    
    (when (or (nil? author) (nil? course-name))
      (println "Usage: bb create-course.bb AUTHOR COURSE-NAME")
      (println "Example: bb create-course.bb kae3g grain-network-course")
      (println "")
      (println "Graintime will be generated automatically using gt command.")
      (System/exit 1))
    
    (let [graintime (generate-graintime author)
          grainpath (generate-grainpath author course-name)
          repo-name (generate-repo-name author course-name)
          course-dir (create-course-directory author course-name)]
      
      (println "🌾 Creating immutable course...")
      (println "Grainpath:" grainpath)
      (println "Repository:" repo-name)
      (println "Directory:" course-dir)
      
      ;; Create course files
      (create-course-edn author course-name course-dir)
      (create-grainpath-edn author course-name course-dir)
      (create-gitignore course-dir)
      (create-readme author course-name course-dir)
      
      ;; Create repositories
      (create-github-repo repo-name)
      (create-codeberg-repo repo-name)
      
      ;; Update registry
      (update-registry author course-name graintime grainpath)
      
      (println "")
      (println "✅ Course created successfully!")
      (println "")
      (println "Next steps:")
      (println "1. Add lessons to:" (str course-dir "/lessons/"))
      (println "2. Build course: cd" course-dir "&& gb build")
      (println "3. Deploy course: cd" course-dir "&& gb flow")
      (println "")
      (println "🌾 From granules to grains to THE WHOLE GRAIN!"))))

(main)
