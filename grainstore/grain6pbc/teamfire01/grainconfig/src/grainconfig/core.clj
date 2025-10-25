(ns grainconfig.core
  "Configuration management for grain6pbc template modules.
   
   Provides script-based configuration updates for:
   - Default values in template modules
   - Personal configuration overrides
   - Module-specific settings
   - Cross-module configuration consistency"
  (:require [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [babashka.fs :as fs]
            [clojure.java.shell :as shell]))

;; Configuration paths
(def grainkae3g-root "/home/xy/kae3g/grainkae3g")
(def grainstore-root (str grainkae3g-root "/grainstore"))
(def config-root (str grainstore-root "/grain6pbc/grainconfig"))
(def config-file (str config-root "/config.edn"))
(def personal-config-file (str grainkae3g-root "/.grainconfig.edn"))

;; Default configuration
(def default-config
  {:grainconfig
   {:version "0.1.0"
    :timestamp (str (java.time.Instant/now))
    :description "Grainpbc template configuration"
    
    :modules
    {:grainsync
     {:defaults
      {:course-title "kae3g"
       :course-description "kae3g"
       :github-owner "kae3g"
       :github-repo "grainkae3g"
       :codeberg-owner "kae3g"
       :codeberg-repo "grainkae3g"}}
     
     :graintime
     {:defaults
      {:location "San Rafael, CA, USA"
       :latitude 37.9735
       :longitude -122.5311
       :timezone "America/Los_Angeles"
       :username "kae3g"}}
     
     :graincourse
     {:defaults
      {:author "kae3g"
       :template "default"
       :style "monospace"}}
     
     :graincourse-sync
     {:defaults
      {:github-pages-url "https://kae3g.github.io/grainkae3g"
       :codeberg-pages-url "https://kae3g.codeberg.page/grainkae3g"
       :auto-commit true
       :auto-push true}}}})

;; Pure Functions

(defn read-config
  "Read configuration from file (pure function)"
  [config-path]
  (if (fs/exists? config-path)
    (-> config-path
        slurp
        edn/read-string)
    default-config))

(defn get-module-config
  "Get configuration for specific module (pure function)"
  [config module-key]
  (get-in config [:grainconfig :modules module-key :defaults] {}))

(defn merge-configs
  "Merge personal config with template config (pure function)"
  [template-config personal-config]
  (let [personal-modules (get-in personal-config [:grainconfig :modules] {})]
    (update-in template-config [:grainconfig :modules]
               (fn [template-modules]
                 (merge-with (fn [template personal]
                               (update template :defaults
                                       #(merge % (:defaults personal))))
                             template-modules
                             personal-modules)))))

(defn validate-config
  "Validate configuration structure (pure function)"
  [config]
  (let [errors (atom [])]
    
    ;; Check required top-level fields
    (when-not (get-in config [:grainconfig :version])
      (swap! errors conj "Missing :grainconfig :version"))
    
    (when-not (get-in config [:grainconfig :modules])
      (swap! errors conj "Missing :grainconfig :modules"))
    
    ;; Check module configurations
    (doseq [[module-key module-data] (get-in config [:grainconfig :modules])]
      (when-not (:defaults module-data)
        (swap! errors conj (str "Module " module-key " missing :defaults"))))
    
    {:valid? (empty? @errors)
     :errors @errors}))

;; Side Effects

(defn write-config!
  "Write configuration to file (side effect)"
  [config output-path]
  (fs/create-dirs (fs/parent output-path))
  (spit output-path (with-out-str (clojure.pprint/pprint config)))
  output-path)

(defn update-module-config!
  "Update configuration for specific module (side effect)"
  [module-key updates]
  (let [template-config (read-config config-file)
        personal-config (read-config personal-config-file)
        merged-config (merge-configs template-config personal-config)
        
        ;; Update the specific module
        updated-config (update-in merged-config 
                                  [:grainconfig :modules module-key :defaults]
                                  #(merge % updates))
        
        ;; Write to personal config
        personal-updated (assoc-in personal-config 
                                   [:grainconfig :modules module-key :defaults]
                                   updates)]
    
    (write-config! personal-updated personal-config-file)
    updated-config))

(defn reset-config!
  "Reset configuration to defaults (side effect)"
  []
  (when (fs/exists? personal-config-file)
    (fs/delete personal-config-file))
  (write-config! default-config config-file)
  (println "✅ Configuration reset to defaults"))

(defn show-config
  "Show current configuration (side effect)"
  []
  (let [template-config (read-config config-file)
        personal-config (read-config personal-config-file)
        merged-config (merge-configs template-config personal-config)]
    
    (println "🌾 Grainpbc Configuration")
    (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    (println "Version:" (get-in merged-config [:grainconfig :version]))
    (println "Timestamp:" (get-in merged-config [:grainconfig :timestamp]))
    (println "")
    
    (doseq [[module-key module-data] (get-in merged-config [:grainconfig :modules])]
      (println (str "📦 " (name module-key) ":"))
      (doseq [[key value] (:defaults module-data)]
        (println (str "   " (name key) ": " value)))
      (println ""))))

(defn update-config!
  "Interactive configuration update (side effect)"
  []
  (println "🌾 Update Configuration")
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
  (println "Available modules:")
  
  (let [template-config (read-config config-file)
        modules (get-in template-config [:grainconfig :modules])]
    
    (doseq [[module-key module-data] modules]
      (println (str "  " (name module-key) ": " (:description module-data))))
    
    (print "\nSelect module to update: ")
    (flush)
    (let [module-key (keyword (read-line))]
      (if (contains? modules module-key)
        (do
          (println (str "\nUpdating " (name module-key) " configuration:"))
          (let [current-config (get-module-config template-config module-key)]
            (doseq [[key value] current-config]
              (print (str (name key) " [" value "]: "))
              (flush)
              (let [input (read-line)]
                (when-not (str/blank? input)
                  (update-module-config! module-key {key input}))))))
        (println "❌ Invalid module selected")))))

(defn validate-config
  "Validate current configuration (side effect)"
  []
  (let [template-config (read-config config-file)
        personal-config (read-config personal-config-file)
        merged-config (merge-configs template-config personal-config)
        validation (validate-config merged-config)]
    
    (if (:valid? validation)
      (println "✅ Configuration is valid")
      (do
        (println "❌ Configuration validation failed:")
        (doseq [error (:errors validation)]
          (println "  -" error))))))

(comment
  ;; Example usage
  (show-config)
  (update-config!)
  (reset-config!)
  (validate-config)
  )
