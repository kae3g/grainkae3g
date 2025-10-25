#!/usr/bin/env bb

(require '[babashka.process :refer [sh]]
         '[clojure.string :as str])

(defn set-scaling! [factor]
  (sh "gsettings" "set" "org.gnome.desktop.interface" "text-scaling-factor" (str factor))
  (println "✅ Set text scaling to" factor "x"))

(defn get-current-scaling []
  (-> (sh "gsettings" "get" "org.gnome.desktop.interface" "text-scaling-factor")
      :out
      str/trim
      read-string))

(defn main [& args]
  (let [factor (when (seq args)
                 (Double/parseDouble (first args)))
        current (get-current-scaling)]
    
    (println "🔍 GrainDisplay - Set Text Scaling\n")
    (println "Current scaling:" current "x")
    
    (if factor
      (do
        (println "Setting scaling to" factor "x...")
        (set-scaling! factor)
        (println "\n💡 Tip: Common scaling factors:")
        (println "   1.0  - 100% (default)")
        (println "   1.25 - 125% (slightly larger)")
        (println "   1.5  - 150% (medium)")
        (println "   1.75 - 175% (large)")
        (println "   2.0  - 200% (very large)"))
      (do
        (println "\nUsage: bb scripts/set-scaling.bb FACTOR")
        (println "Example: bb scripts/set-scaling.bb 1.75")
        (System/exit 1)))))

(apply main *command-line-args*)

