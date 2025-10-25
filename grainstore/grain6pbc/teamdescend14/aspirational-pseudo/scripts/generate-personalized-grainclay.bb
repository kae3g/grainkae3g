#!/usr/bin/env bb

(require '[babashka.process :refer [shell]]
         '[clojure.string :as str]
         '[clojure.java.io :as io])

(defn generate-personalized-grainclay []
  (println "🌾 Generating personalized grainclay...")
  (println "")
  
  (println "🎨 Creating personalized ASCII art...")
  (shell "cd" "../../grainbarrel" "&&" "bb" "scripts/draw.bb")
  (println "✅ Personalized ASCII art generated")
  
  (println "")
  (println "🔧 Configuring personal preferences...")
  (shell "cp" "template/grainclay/config.edn" "personal/grainclay/config.edn")
  (println "✅ Personal preferences configured")
  
  (println "")
  (println "🌱 Applying vegan-friendly personalization...")
  (shell "cd" "../grainsource-vegan" "&&" "bb" "vegan-rewrite")
  (println "✅ Vegan-friendly personalization applied")
  
  (println "")
  (println "🌾 Personalized grainclay generation complete!"))

(generate-personalized-grainclay)
