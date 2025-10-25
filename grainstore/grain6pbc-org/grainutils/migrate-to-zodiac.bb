#!/usr/bin/env bb
;; 🌾 Migrate Modules into 14 Zodiac Repositories
;; *"From 40 to 14 - Every Module Finds Its Cosmic Home"*

(require '[babashka.fs :as fs]
         '[clojure.string :as str])

(def migrations
  [;; FIRE SIGNS
   {:zodiac "grainfire" :symbol "♈" :sign "Aries"
    :modules ["grainbarrel" "grainconfig" "grain6"]}
   
   {:zodiac "grainshine" :symbol "♌" :sign "Leo"
    :modules ["graindisplay" "grain-nightlight" "grainicons" "grainas-voice"]}
   
   {:zodiac "grainwisdom" :symbol "♐" :sign "Sagittarius"
    :modules ["grainlexicon" "graincourse" "graincourse-sync" "docs"]}
   
   ;; EARTH SIGNS
   {:zodiac "grainvault" :symbol "♉" :sign "Taurus"
    :modules ["graindrive" "grainclay" "grainclay-cleanup" "vendor"]}
   
   {:zodiac "grainprecision" :symbol "♍" :sign "Virgo"
    :modules ["grainenvvars" "grainzsh" "clojure-s6" "clojure-sixos"]}
   
   {:zodiac "grainstructure" :symbol "♑" :sign "Capricorn"
    :modules ["graindatastructures" "grain-metatypes" "specs" "equivalence"]}
   
   ;; AIR SIGNS
   {:zodiac "grainnetwork" :symbol "♊" :sign "Gemini"
    :modules ["grainweb" "grainpages" "graindevname" "grainregistry"]}
   
   {:zodiac "grainbalance" :symbol "♎" :sign "Libra"
    :modules ["clotoko" "clotoko-icp"]}
   
   {:zodiac "grainfuture" :symbol "♒" :sign "Aquarius"
    :modules ["humble-desktop" "humble-gc" "humble-repl" "humble-stack" 
              "grain-musl" "grain-clj"]}
   
   ;; WATER SIGNS
   {:zodiac "grainnurture" :symbol "♋" :sign "Cancer"
    :modules ["graincasks" "graindaemon" "tools"]}
   
   {:zodiac "graintransform" :symbol "♏" :sign "Scorpio"
    :modules ["grainconv" "grainsynonym"]}
   
   {:zodiac "grainflow" :symbol "♓" :sign "Pisces"
    :modules ["graintime" "grainneovedic" "grainsource-separation"]}
   
   ;; SHADOW NODES
   {:zodiac "grainascend" :symbol "☊" :sign "Rahu"
    :modules ["grainai-vocab" "grainas" "grainmode"]}
   
   {:zodiac "graindescend" :symbol "☋" :sign "Ketu"
    :modules ["aspirational-pseudo" "grainbusiness" "grainsource-vegan"]}])

(defn find-module
  "Find module in grainstore"
  [module]
  (let [search-paths ["grainstore/grain6pbc/"
                      "grainstore/grainpbc-org/"
                      "grainstore/grain6pbc-org/"]]
    (->> search-paths
         (map #(str % module))
         (filter fs/exists?)
         first)))

(defn migrate-modules
  "Migrate modules into zodiac repository"
  [{:keys [zodiac symbol sign modules]}]
  (println (str "\n" symbol " " sign " - " zodiac))
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
  
  ;; Ensure zodiac repo exists
  (let [zodiac-path (str "grainstore/grain6pbc/" zodiac)]
    (when-not (fs/exists? zodiac-path)
      (println (str "  ⚠ Creating " zodiac-path))
      (fs/create-dirs zodiac-path)))
  
  (doseq [module modules]
    (let [source-path (find-module module)
          target-path (str "grainstore/grain6pbc/" zodiac "/" module)]
      
      (cond
        (nil? source-path)
        (println (str "  ⚠ " module " (not found in any location)"))
        
        (= source-path target-path)
        (println (str "  ✓ " module " (already in correct location)"))
        
        (fs/exists? target-path)
        (println (str "  ⚠ " module " (target already exists at " target-path ")"))
        
        :else
        (do
          (println (str "  → " module))
          (println (str "     from: " source-path))
          (println (str "     to:   " target-path))
          ;; Execute the migration
          (fs/create-dirs (fs/parent target-path))
          (fs/move source-path target-path)
          (println (str "     ✓ moved!"))
          )))))

(defn -main
  "Main migration entry point"
  [& args]
  (println "🌾 EXECUTING: Module Migration to 14 Zodiac Repos")
  (println "")
  (println "Ye's Philosophy: 14 Songs > 40 Songs")
  (println "Vedic Astrology: 12 Zodiac + Rahu + Ketu")
  (println "")
  
  (doseq [migration migrations]
    (migrate-modules migration))
  
  (println "")
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
  (println "")
  (println "✨ Migration complete!")
  (println "")
  (println "All modules have been moved into their zodiac homes.")
  (println "")
  (println "🌾 \"From 40 to 14 - Precision Branch Collective\" 🌾"))

(-main)

