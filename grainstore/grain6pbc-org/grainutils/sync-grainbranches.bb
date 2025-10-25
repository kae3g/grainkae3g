#!/usr/bin/env bb
(require '[babashka.process :refer [shell]]
         '[babashka.fs :as fs]
         '[clojure.string :as str])

(defn get-current-graintime []
  "Get current graintime using the gt utility"
  (let [result (shell {:out :string} "gt" "now" "kae3g")]
    (if (= 0 (:exit result))
      (let [output (:out result)
            ;; Extract graintime from gt output
            graintime-match (re-find #"12025-\d{2}-\d{2}--\d{4}--\w+--moon-\w+------asc-\w+\d{3}--sun-\d+th--kae3g" output)]
        (or graintime-match "12025-10-24--1429--PDT--moon-vishakha------asc-gem000--sun-11th--kae3g"))
      "12025-10-24--1429--PDT--moon-vishakha------asc-gem000--sun-11th--kae3g")))

(defn slugify-description [description]
  "Create URL-safe slug from description, ensuring total length stays under 100 chars"
  (-> description
      (str/replace #"[^a-zA-Z0-9\s-]" "")
      (str/replace #"\s+" "-")
      str/lower-case
      ;; Truncate to fit within 100-char limit with graintime prefix
      (#(subs % 0 (min (count %) 25)))))

(defn get-grainbranch-name [description graintime]
  "Generate grainbranch name using grainpath graintime grainneovedic real-time immutable 100-char paths"
  (let [slug (slugify-description description)
        ;; Ensure total length is exactly 100 chars or less
        max-description-length (- 100 (count graintime) 1) ; -1 for hyphen
        truncated-slug (if (> (count slug) max-description-length)
                         (subs slug 0 max-description-length)
                         slug)]
    (str graintime "-" truncated-slug)))

(defn get-repo-description [repo-name]
  (let [result (shell {:out :string} "gh" "api" (str "repos/grain6pbc/" repo-name) "--jq" ".description")]
    (if (= 0 (:exit result))
      (str/trim (:out result))
      nil)))

(defn update-repo-description [repo-name description]
  (let [graintime (get-current-graintime)
        grainbranch-name (get-grainbranch-name description graintime)
        grainbranch-url (str "https://github.com/grain6pbc/" repo-name "/tree/" grainbranch-name)
        new-description (str description " | Grainbranch: " grainbranch-url)]
    (println "🌾 Updating" repo-name "with grainbranch URL...")
    (println "📝 Description:" description)
    (println "🔗 Grainbranch URL:" grainbranch-url)
    
    (let [result (shell {:out :string} "gh" "api" (str "repos/grain6pbc/" repo-name) 
                        "--method" "PATCH" 
                        "--field" (str "description=" new-description))]
      (if (= 0 (:exit result))
        (do
          (println "✅ Updated description for" repo-name)
          (println "🌐 New description:" new-description)
          true)
        (do
          (println "❌ Failed to update" repo-name ":" (:err result))
          false)))))

(defn sync-all-repos []
  (println "🌾 Syncing grainbranch URLs for all grain6pbc repositories...")
  
  ;; Get list of all repositories
  (let [result (shell {:out :string} "gh" "api" "orgs/grain6pbc/repos" "--jq" ".[].name")]
    (if (= 0 (:exit result))
      (let [repo-names (str/split-lines (:out result))]
        (println "📊 Found" (count repo-names) "repositories")
        
        (doseq [repo-name repo-names]
          (let [repo-name (str/trim repo-name)]
            (when-not (str/blank? repo-name)
              (let [current-description (get-repo-description repo-name)]
                (if current-description
                  (let [base-description (first (str/split current-description #" \| Grainbranch:"))]
                    (update-repo-description repo-name (str/trim base-description)))
                  (println "⚠️  Could not get description for" repo-name))))
            (Thread/sleep 1000))) ; Rate limiting
        (println "🎉 Sync complete!"))
      (println "❌ Failed to get repository list:" (:err result)))))

(defn sync-specific-repo [repo-name]
  (println "🌾 Syncing grainbranch URL for" repo-name "...")
  
  (let [current-description (get-repo-description repo-name)]
    (if current-description
      (let [base-description (first (str/split current-description #" \| Grainbranch:"))]
        (update-repo-description repo-name (str/trim base-description)))
      (println "❌ Could not get description for" repo-name))))

(defn print-help []
  (println "Usage: bb sync-grainbranches.bb <command> [args]")
  (println "")
  (println "Commands:")
  (println "  sync-all     - Sync grainbranch URLs for all repositories")
  (println "  sync <repo>  - Sync grainbranch URL for specific repository")
  (println "  help         - Print this help message"))

(defn parse-args [args]
  (if (empty? args)
    []
    (let [first-arg (first args)]
      (if (string? first-arg)
        (str/split first-arg #"\s+")
        args))))

(defn -main [& args]
  (let [parsed-args (parse-args args)
        command (first parsed-args)]
    (case command
      "sync-all" (sync-all-repos)
      "sync" (if-let [repo-name (second parsed-args)]
               (sync-specific-repo repo-name)
               (println "❌ Please provide repository name"))
      "help" (print-help)
      (do
        (println "❌ Unknown command:" (or command "nil"))
        (print-help)))))

(-main *command-line-args*)
