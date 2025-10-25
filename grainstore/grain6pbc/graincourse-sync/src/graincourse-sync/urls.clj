(ns graincourse-sync.urls
  "URL generation for GrainCourse Sync
  
  Generates GitHub Pages and Codeberg Pages URLs from grainpaths.
  Handles the nested grainpath structure for proper URL construction."
  (:require [clojure.string :as str]))

(defn generate-github-url
  "Generate GitHub Pages URL from grainpath
  
  Example grainpath: /course/kae3g/grain-net-fund/12025-10-23--0122--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/
  GitHub URL: https://kae3g.github.io/grainkae3g/course/kae3g/grain-net-fund/12025-10-23--0122--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/"
  [grainpath config]
  (let [base-url (get config :github-base-url "https://kae3g.github.io/grainkae3g")
        ;; Remove leading slash from grainpath for URL construction
        clean-path (str/replace grainpath #"^/" "")]
    (str base-url "/" clean-path)))

(defn generate-codeberg-url
  "Generate Codeberg Pages URL from grainpath
  
  Example grainpath: /course/kae3g/grain-net-fund/12025-10-23--0122--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/
  Codeberg URL: https://kae3g.codeberg.page/grainkae3g/course/kae3g/grain-net-fund/12025-10-23--0122--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/"
  [grainpath config]
  (let [base-url (get config :codeberg-base-url "https://kae3g.codeberg.page/grainkae3g")
        ;; Remove leading slash from grainpath for URL construction
        clean-path (str/replace grainpath #"^/" "")]
    (str base-url "/" clean-path)))

(defn extract-course-info
  "Extract course information from grainpath
  
  Example: /course/kae3g/grain-net-fund/12025-10-23--0122--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/
  Returns: {:author kae3g :course-name grain-net-fund :graintime 12025-10-23--...}"
  [grainpath]
  (let [;; Remove leading and trailing slashes
        clean-path (-> grainpath
                       (str/replace #"^/" "")
                       (str/replace #"/$" ""))
        ;; Split into components
        components (str/split clean-path #"/")]
    (when (>= (count components) 4)
      {:author (nth components 1)
       :course-name (nth components 2)
       :graintime (nth components 3)
       :full-path clean-path})))

(defn generate-relative-urls
  "Generate relative URLs for course content
  
  Given a grainpath, generates relative URLs for common course files:
  - index.html (at the grainpath root)
  - lessons/ (subdirectory)
  - assets/ (subdirectory)
  - README.md (at the grainpath root)
  
  All URLs are relative to the grainpath root."
  [grainpath]
  (let [course-info (extract-course-info grainpath)]
    (when course-info
      {:index (str grainpath "index.html")
       :lessons (str grainpath "lessons/")
       :assets (str grainpath "assets/")
       :readme (str grainpath "README.md")
       :course-info course-info})))

(defn validate-grainpath-format
  "Validate that grainpath follows the expected format
  
  Expected: /course/{author}/{course-name}/{graintime}/
  Validates:
  - Starts with /course/
  - Has exactly 4 components
  - Graintime component matches pattern"
  [grainpath]
  (let [;; Remove leading and trailing slashes
        clean-path (-> grainpath
                       (str/replace #"^/" "")
                       (str/replace #"/$" ""))
        components (str/split clean-path #"/")
        ;; Check format
        has-course-prefix (= (first components) "course")
        has-four-components (= (count components) 4)
        ;; Check graintime pattern (last component)
        graintime (last components)
        graintime-valid (and graintime (str/includes? graintime "12025-"))]
    
    {:valid (and has-course-prefix has-four-components graintime-valid)
     :components components
     :graintime graintime
     :errors (cond-> []
               (not has-course-prefix) (conj "Must start with 'course'")
               (not has-four-components) (conj "Must have exactly 4 components")
               (not graintime-valid) (conj "Graintime must contain '12025-'"))}))

(defn generate-symlink-paths
  "Generate symlink target paths for GitHub and Codeberg
  
  Creates the directory structure that will be symlinked:
  - GitHub: symlinks/github-pages/{graintime} -> course content
  - Codeberg: symlinks/codeberg-pages/{graintime} -> course content
  
  The symlinks point to the course content, but the grainpath structure
  is created in the target directories."
  [grainpath base-dir]
  (let [course-info (extract-course-info grainpath)
        graintime (:graintime course-info)]
    (when graintime
      {:github-pages (str base-dir "/github-pages/" graintime)
       :codeberg-pages (str base-dir "/codeberg-pages/" graintime)
       :grainpath-dir (str base-dir "/grainpath/" graintime)})))
