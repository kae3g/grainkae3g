(ns grainclay.config
  "Grainclay configuration management
  
  Handles:
  - Personal configuration loading
  - Template/personal merge
  - Configuration validation
  - Environment variable integration"
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            [babashka.fs :as fs]))

;; Default grainclay configuration
(def default-config
  {:network-hosts ["github.com" "codeberg.org" "grain6pbc.github.io"]
   :publish-formats [:html :markdown :edn :json]
   :sync-interval-minutes 30
   :auto-cleanup true
   :cleanup-retention-days 30
   :github-owner "kae3g"
   :github-repo "grainkae3g"
   :codeberg-owner "kae3g"
   :codeberg-repo "grainkae3g"
   :content-types ["article" "course" "tool" "library" "documentation"]
   :default-content-type "article"
   :auto-sync true
   :publish-on-create true})

(defn read-personal-config
  "Read personal configuration from ~/.grainconfig.edn"
  []
  (let [config-file (str (System/getProperty "user.home") "/.grainconfig.edn")]
    (try
      (if (fs/exists? config-file)
        (edn/read-string (slurp config-file))
        {})
      (catch Exception e
        (println "⚠️  Could not read personal config:" (.getMessage e))
        {}))))

(defn get-grainclay-config
  "Get merged grainclay configuration"
  []
  (let [personal-config (read-personal-config)
        grainclay-config (get personal-config :grainclay {})]
    (merge default-config grainclay-config)))

(defn show-config
  "Show current grainclay configuration"
  []
  (let [config (get-grainclay-config)]
    (println "🌾 Grainclay Configuration")
    (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    (println "")
    (println "📡 Network Hosts:")
    (doseq [host (:network-hosts config)]
      (println "   • " host))
    (println "")
    (println "📄 Publish Formats:")
    (doseq [format (:publish-formats config)]
      (println "   • " format))
    (println "")
    (println "⚙️  Settings:")
    (println "   Sync Interval: " (:sync-interval-minutes config) " minutes")
    (println "   Auto Cleanup: " (:auto-cleanup config))
    (println "   Cleanup Retention: " (:cleanup-retention-days config) " days")
    (println "   Auto Sync: " (:auto-sync config))
    (println "   Publish on Create: " (:publish-on-create config))
    (println "")
    (println "🌐 Repositories:")
    (println "   GitHub: " (:github-owner config) "/" (:github-repo config))
    (println "   Codeberg: " (:codeberg-owner config) "/" (:codeberg-repo config))
    (println "")
    (println "📝 Content Types:")
    (doseq [type (:content-types config)]
      (println "   • " type))
    (println "   Default: " (:default-content-type config))
    (println "")))

(defn update-config
  "Update grainclay configuration interactively"
  []
  (let [current-config (get-grainclay-config)
        config-file (str (System/getProperty "user.home") "/.grainconfig.edn")]
    
    (println "🔧 Update Grainclay Configuration")
    (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    (println "")
    
    ;; GitHub settings
    (print (str "GitHub Owner [" (:github-owner current-config) "]: "))
    (flush)
    (let [github-owner (let [input (read-line)]
                         (if (str/blank? input) (:github-owner current-config) input))]
      (print (str "GitHub Repo [" (:github-repo current-config) "]: "))
      (flush)
      (let [github-repo (let [input (read-line)]
                          (if (str/blank? input) (:github-repo current-config) input))]
        
        ;; Codeberg settings
        (print (str "Codeberg Owner [" (:codeberg-owner current-config) "]: "))
        (flush)
        (let [codeberg-owner (let [input (read-line)]
                               (if (str/blank? input) (:codeberg-owner current-config) input))]
          (print (str "Codeberg Repo [" (:codeberg-repo current-config) "]: "))
          (flush)
          (let [codeberg-repo (let [input (read-line)]
                                (if (str/blank? input) (:codeberg-repo current-config) input))]
            
            ;; Sync settings
            (print (str "Sync Interval (minutes) [" (:sync-interval-minutes current-config) "]: "))
            (flush)
            (let [sync-interval (let [input (read-line)]
                                  (if (str/blank? input) 
                                    (:sync-interval-minutes current-config)
                                    (Integer/parseInt input)))]
              
              ;; Create updated config
              (let [updated-config (assoc current-config
                                          :github-owner github-owner
                                          :github-repo github-repo
                                          :codeberg-owner codeberg-owner
                                          :codeberg-repo codeberg-repo
                                          :sync-interval-minutes sync-interval)
                    
                    ;; Read existing personal config
                    existing-personal (read-personal-config)
                    updated-personal (assoc existing-personal :grainclay updated-config)]
                
                ;; Write updated config
                (try
                  (spit config-file (pr-str updated-personal))
                  (println "")
                  (println "✅ Configuration updated!")
                  (println "   File: " config-file)
                  (catch Exception e
                    (println "")
                    (println "❌ Failed to update configuration:")
                    (println "   " (.getMessage e))))))))))))

(defn validate-config
  "Validate grainclay configuration"
  [config]
  (let [errors (atom [])]
    
    ;; Check required fields
    (when (str/blank? (:github-owner config))
      (swap! errors conj "GitHub owner is required"))
    
    (when (str/blank? (:github-repo config))
      (swap! errors conj "GitHub repo is required"))
    
    (when (str/blank? (:codeberg-owner config))
      (swap! errors conj "Codeberg owner is required"))
    
    (when (str/blank? (:codeberg-repo config))
      (swap! errors conj "Codeberg repo is required"))
    
    ;; Check numeric values
    (when (not (pos? (:sync-interval-minutes config)))
      (swap! errors conj "Sync interval must be positive"))
    
    (when (not (pos? (:cleanup-retention-days config)))
      (swap! errors conj "Cleanup retention days must be positive"))
    
    ;; Check content types
    (when (empty? (:content-types config))
      (swap! errors conj "At least one content type is required"))
    
    (when (not (contains? (set (:content-types config)) (:default-content-type config)))
      (swap! errors conj "Default content type must be in content types list"))
    
    @errors))

(defn reset-config
  "Reset grainclay configuration to defaults"
  []
  (let [config-file (str (System/getProperty "user.home") "/.grainconfig.edn")
        existing-personal (read-personal-config)
        updated-personal (assoc existing-personal :grainclay default-config)]
    
    (try
      (spit config-file (pr-str updated-personal))
      (println "✅ Configuration reset to defaults")
      (catch Exception e
        (println "❌ Failed to reset configuration:")
        (println "   " (.getMessage e))))))
