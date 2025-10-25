#!/usr/bin/env bb

(require '[clojure.edn :as edn]
         '[clojure.string :as str]
         '[babashka.fs :as fs]
         '[clojure.java.shell :as shell])

;; Configuration paths
(def grainkae3g-root "/home/xy/kae3g/grainkae3g")
(def personal-config-file (str grainkae3g-root "/.grainconfig.edn"))

;; Default graintime configuration
(def graintime-defaults
  {:location "San Rafael, CA, USA"
   :latitude 37.9735
   :longitude -122.5311
   :timezone "America/Los_Angeles"
   :username "kae3g"})

;; Functions
(defn read-personal-config
  "Read personal configuration"
  []
  (if (fs/exists? personal-config-file)
    (edn/read-string (slurp personal-config-file))
    {:grainconfig {:modules {}}}))

(defn write-personal-config
  "Write personal configuration"
  [config]
  (fs/create-dirs (fs/parent personal-config-file))
  (spit personal-config-file (with-out-str (clojure.pprint/pprint config))))

(defn update-graintime-config
  "Update graintime configuration interactively"
  []
  (println "🌾 Update Graintime Configuration")
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
  
  (let [config (read-personal-config)
        current-graintime (get-in config [:grainconfig :modules :graintime :defaults] graintime-defaults)]
    
    (println "Current configuration:")
    (doseq [[key value] current-graintime]
      (println (str "  " (name key) ": " value)))
    (println "")
    
    (let [updated-graintime (atom current-graintime)]
      
      ;; Update each configuration value
      (doseq [[key default-value] graintime-defaults]
        (let [current-value (get @updated-graintime key default-value)]
          (print (str (name key) " [" current-value "]: "))
          (flush)
          (let [input (read-line)]
            (when-not (str/blank? input)
              (let [parsed-value (case key
                                   :latitude (Double/parseDouble input)
                                   :longitude (Double/parseDouble input)
                                   input)]
                (swap! updated-graintime assoc key parsed-value))))))
      
      ;; Update the configuration
      (let [updated-config (assoc-in config 
                                     [:grainconfig :modules :graintime :defaults]
                                     @updated-graintime)]
        (write-personal-config updated-config)
        (println "\n✅ Graintime configuration updated!")
        (println "\nNew configuration:")
        (doseq [[key value] @updated-graintime]
          (println (str "  " (name key) ": " value)))))))

;; Run the update
(update-graintime-config)
