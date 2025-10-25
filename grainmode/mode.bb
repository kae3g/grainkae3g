#!/usr/bin/env bb
(require '[babashka.fs :as fs]
         '[clojure.string :as str])

(defn set-grainmode
  "Set GrainMode (Trish/Glow)"
  [mode]
  (println "🌾 GrainMode: Setting mode to" mode)
  
  (let [mode-config {:trish {:voice "creative" :tone "playful" :style "artistic"}
                     :glow {:voice "technical" :tone "professional" :style "precise"}}]
    
    (if-let [config (get mode-config (keyword mode))]
      (do
        (spit "grainmode.edn" config)
        (println "✅ Mode set to" mode "with config:" config))
      (println "❌ Unknown mode:" mode))))

(defn -main
  "Main entry point for GrainMode"
  [& args]
  (let [command (first args)
        mode (second args)]
    (case command
      "set" (set-grainmode mode)
      "help" (println "🌾 GrainMode: AI voice mode management")
      (println "❌ Unknown command:" command))))


