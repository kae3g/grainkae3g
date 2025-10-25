#!/usr/bin/env bb

(require '[clojure.edn :as edn]
         '[clojure.string :as str]
         '[babashka.fs :as fs]
         '[clojure.java.shell :as shell])

;; Configuration paths
(def grainkae3g-root "/home/xy/kae3g/grainkae3g")
(def personal-config-file (str grainkae3g-root "/.grainconfig.edn"))

;; Default grainsync configuration
(def grainsync-defaults
  {:course-title "kae3g"
   :course-description "kae3g"
   :github-owner "kae3g"
   :github-repo "grainkae3g"
   :codeberg-owner "kae3g"
   :codeberg-repo "grainkae3g"})

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

(defn update-grainsync-config
  "Update grainsync configuration interactively"
  []
  (println "🌾 Update Grainsync Configuration")
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
  
  (let [config (read-personal-config)
        current-grainsync (get-in config [:grainconfig :modules :grainsync :defaults] grainsync-defaults)]
    
    (println "Current configuration:")
    (doseq [[key value] current-grainsync]
      (println (str "  " (name key) ": " value)))
    (println "")
    
    (let [updated-grainsync (atom current-grainsync)]
      
      ;; Update each configuration value
      (doseq [[key default-value] grainsync-defaults]
        (let [current-value (get @updated-grainsync key default-value)]
          (print (str (name key) " [" current-value "]: "))
          (flush)
          (let [input (read-line)]
            (when-not (str/blank? input)
              (swap! updated-grainsync assoc key input)))))
      
      ;; Update the configuration
      (let [updated-config (assoc-in config 
                                     [:grainconfig :modules :grainsync :defaults]
                                     @updated-grainsync)]
        (write-personal-config updated-config)
        (println "\n✅ Grainsync configuration updated!")
        (println "\nNew configuration:")
        (doseq [[key value] @updated-grainsync]
          (println (str "  " (name key) ": " value)))))))

;; Run the update
(update-grainsync-config)
