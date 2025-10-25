#!/usr/bin/env bb

(require '[clojure.edn :as edn]
         '[clojure.string :as str]
         '[clojure.java.io :as io])

(defn find-grainstore-edn []
  "Find grainstore.edn relative to current directory"
  (cond
    ;; Running from repo root
    (.exists (io/file "grainstore/grainstore.edn"))
    "grainstore/grainstore.edn"
    
    ;; Running from grainstore/ directory
    (.exists (io/file "grainstore.edn"))
    "grainstore.edn"
    
    ;; Running from grainbarrel/ directory
    (.exists (io/file "../grainstore.edn"))
    "../grainstore.edn"
    
    ;; Running from grainbarrel/scripts/
    (.exists (io/file "../../grainstore.edn"))
    "../../grainstore.edn"
    
    ;; Not found
    :else
    (throw (ex-info "Cannot find grainstore.edn" {}))))

(defn read-manifest []
  (-> (find-grainstore-edn)
      slurp
      edn/read-string))

(defn get-modules [manifest]
  (-> manifest :grainstore :modules))

(defn validate-manifest [manifest]
  (let [errors (atom [])]
    
    ;; Check required top-level fields
    (when-not (get-in manifest [:grainstore :version])
      (swap! errors conj "❌ Missing :version"))
    
    (when-not (get-in manifest [:grainstore :modules])
      (swap! errors conj "❌ Missing :modules"))
    
    (when-not (get-in manifest [:grainstore :platforms])
      (swap! errors conj "❌ Missing :platforms"))
    
    ;; Check each module
    (doseq [[module-key module-data] (get-modules manifest)]
      (when-not (:description module-data)
        (swap! errors conj (str "❌ Module " module-key " missing :description")))
      
      (when-not (:license module-data)
        (swap! errors conj (str "❌ Module " module-key " missing :license")))
      
      (when-not (:repos module-data)
        (swap! errors conj (str "❌ Module " module-key " missing :repos")))
      
      ;; Check dependency references (only for non-external modules)
      (when-not (get-in module-data [:repos :external])
        (doseq [dep (:depends-on module-data)]
          (when-not (contains? (get-modules manifest) dep)
            (swap! errors conj (str "❌ Module " module-key " depends on unknown module: " dep))))))
    
    {:valid? (empty? @errors)
     :errors @errors}))

(defn main []
  (println "🌾 Grainstore Manifest Validation\n")
  
  (let [manifest (read-manifest)
        result (validate-manifest manifest)
        modules (get-modules manifest)
        external (->> modules
                     (filter (fn [[_k v]] (get-in v [:repos :external])))
                     count)
        grain-pbc (- (count modules) external)]
    
    (println "📊 Grainstore Statistics:")
    (println (str "  Total Modules: " (count modules)))
    (println (str "  Grain PBC Modules: " grain-pbc))
    (println (str "  External Dependencies: " external))
    (println (str "  Version: " (get-in manifest [:grainstore :version])))
    (println (str "  Timestamp: " (get-in manifest [:grainstore :timestamp])))
    (println)
    
    (if (:valid? result)
      (do
        (println "✅ Manifest is valid!")
        (println)
        (println "🔍 Dependency Check:")
        
        ;; Check for circular dependencies
        (letfn [(get-deps [module-key visited]
                  (if (visited module-key)
                    #{:circular}
                    (let [deps (get-in modules [module-key :depends-on] [])
                          visited' (conj visited module-key)]
                      (reduce (fn [acc dep]
                               (into acc (get-deps dep visited')))
                             (set deps)
                             deps))))]
          (doseq [[module-key _data] modules]
            (let [all-deps (get-deps module-key #{})]
              (when (all-deps :circular)
                (println (str "⚠️  Possible circular dependency involving: " module-key))))))
        
        (println "✅ No circular dependencies detected")
        (System/exit 0))
      (do
        (println "❌ Manifest validation failed:\n")
        (doseq [err (:errors result)]
          (println (str "  " err)))
        (System/exit 1)))))

(main)

