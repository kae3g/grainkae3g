#!/usr/bin/env bb

(require '[babashka.process :refer [shell]]
         '[clojure.string :as str]
         '[clojure.java.io :as io]
         '[clojure.edn :as edn])

(defn configure-personal-preferences []
  (println "🌾 Configuring personal preferences...")
  (println "")
  
  (println "📝 Reading template configuration...")
  (let [template-config (edn/read-string (slurp "template/grainclay/config.edn"))]
    (println "✅ Template configuration loaded")
    
    (println "")
    (println "🎨 Personalizing configuration...")
    (let [personalized-config (assoc-in template-config 
                                        [:grainclay-personalization :personal-preferences :development-style :language-preference] 
                                        "Clojure + Hoon + Haskell")]
      (spit "personal/grainclay/config.edn" (pr-str personalized-config))
      (println "✅ Configuration personalized"))
    
    (println "")
    (println "🌱 Applying plant-based development philosophy...")
    (shell "cd" "../grainsource-vegan" "&&" "bb" "vegan-rewrite")
    (println "✅ Plant-based philosophy applied")
    
    (println "")
    (println "🌾 Personal preferences configuration complete!")))

(configure-personal-preferences)
