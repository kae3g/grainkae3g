#!/usr/bin/env bb

(require '[clojure.edn :as edn]
         '[clojure.java.io :as io])

(defn find-grainstore-edn []
  "Find grainstore.edn relative to current directory"
  (cond
    (.exists (io/file "grainstore/grainstore.edn")) "grainstore/grainstore.edn"
    (.exists (io/file "grainstore.edn")) "grainstore.edn"
    (.exists (io/file "../grainstore.edn")) "../grainstore.edn"
    (.exists (io/file "../../grainstore.edn")) "../../grainstore.edn"
    :else (throw (ex-info "Cannot find grainstore.edn" {}))))

(defn main []
  (let [manifest (-> (find-grainstore-edn) slurp edn/read-string)
        modules (-> manifest :grainstore :modules)
        external (->> modules (filter (fn [[_k v]] (get-in v [:repos :external]))) count)
        grain-pbc (- (count modules) external)]
    
    (println "🌾 Grainstore Statistics\n")
    (println (str "  Total Modules: " (count modules)))
    (println (str "  Grain PBC: " grain-pbc))
    (println (str "  External: " external))
    (println (str "  Version: " (get-in manifest [:grainstore :version])))
    (println (str "  Timestamp: " (get-in manifest [:grainstore :timestamp])))))

(main)

