#!/usr/bin/env bb
;; Grainlexicon Synonym Resolver
;; Bolt-on helper for resolving grain network terminology synonyms

(def grainlexicon
  "Comprehensive synonym mapping for Grain Network concepts"
  {
   ;; Core concepts
   :grain6 ["grain6" "grainsix" "grain-six" "6os" "sixos"]
   :grainbox ["grainbox" "graincontainer" "graincanister" "grainstore"]
   :graincard ["graincard" "graincard10" "grain-card" "gcard"]
   :graintime ["graintime" "grain-time" "temporal-grain" "graintstamp"]
   :grainpath ["grainpath" "grain-path" "grainroute" "grain-route"]
   :graincourse ["graincourse" "grain-course" "grainclass" "grain-class"]
   
   ;; Build tools
   :grainbarrel ["grainbarrel" "grain-barrel" "gb" "grainbuild"]
   :quarterback ["quarterback" "qb" "grain-qb" "universal-qb"]
   
   ;; Module types
   :grainpbc ["grainpbc" "grain-pbc" "public-benefit" "pbc-module"]
   :grainsource ["grainsource" "grain-source" "source-module"]
   :grainclay ["grainclay" "grain-clay" "personalization" "personal-config"]
   
   ;; Processes
   :course-sync ["course-sync" "sync-course" "course-synchronize"]
   :demo-sync ["demo-sync" "sync-demo" "demo-synchronize"]
   :system-new ["system-new" "new-system" "create-system"]
   
   ;; Commands (full command chains)
   :qb-course-sync-system-new 
   ["qb course sync system new"
    "qb-course-sync-system-new" 
    "quarterback course sync system new"
    "qb sync course new"
    "qb new course"]
   
   :qb-demo-sync-system-new
   ["qb demo sync system new"
    "qb-demo-sync-system-new"
    "quarterback demo sync system new"
    "qb sync demo new"
    "qb new demo"]
   
   :qb-flow
   ["qb flow"
    "qb-flow"
    "quarterback flow"
    "qb deploy"]
   
   :qb-draw-flow
   ["qb draw flow"
    "qb-draw-flow"
    "qb art flow"
    "qb ascii flow"]
   
   :qb-vegan-flow
   ["qb vegan flow"
    "qb-vegan-flow"
    "qb plant flow"
    "qb compassion flow"]
   
   ;; Philosophy
   :now-next ["now == next + 1" "now-next" "temporal-recursion"]
   :88-counter ["88 counter" "88-counter" "88 × 10^n" "88x10n"]
   :whole-grain ["THE WHOLE GRAIN" "whole-grain" "grain-whole"]
   
   ;; Blockchain
   :icp ["ICP" "internet-computer" "dfinity" "ic"]
   :hedera ["Hedera" "hedera" "HBAR" "hbar"]
   :solana ["Solana" "SOL" "sol" "solana"]
   
   ;; Personal separation
   :template-personal ["template/personal" "template-personal" "separated"]
   :aspirational-pseudo ["aspirational-pseudo" "aspirational" "pseudo" "ultimate-vision"]
   })

(defn resolve-synonym 
  "Resolve a term to its canonical key using grainlexicon"
  [term]
  (let [normalized (clojure.string/lower-case (str term))]
    (or 
      ;; Direct key match
      (some #(when (some (fn [syn] (= normalized (clojure.string/lower-case syn))) 
                         (get grainlexicon %)) 
               %) 
            (keys grainlexicon))
      ;; Return original if no match
      (keyword normalized))))

(defn get-all-synonyms
  "Get all synonyms for a canonical key"
  [canonical-key]
  (get grainlexicon canonical-key []))

(defn find-command
  "Find bb.edn task name from natural language input"
  [input]
  (let [normalized (clojure.string/lower-case (str input))
        ;; Check full command chains
        command-match (some (fn [[k syns]]
                              (when (some #(= normalized (clojure.string/lower-case %)) syns)
                                k))
                            (select-keys grainlexicon 
                                       [:qb-course-sync-system-new
                                        :qb-demo-sync-system-new
                                        :qb-flow
                                        :qb-draw-flow
                                        :qb-vegan-flow]))]
    (or command-match
        ;; Try partial matching
        (keyword normalized))))

(defn print-lexicon
  "Pretty print the grainlexicon"
  []
  (println "")
  (println "╔═══════════════════════════════════════════════════════════════╗")
  (println "║                                                               ║")
  (println "║          🌾 G R A I N L E X I C O N 🌾                       ║")
  (println "║                                                               ║")
  (println "║          Synonym Resolver for Grain Network                  ║")
  (println "║                                                               ║")
  (println "╚═══════════════════════════════════════════════════════════════╝")
  (println "")
  (doseq [[k syns] (sort-by first grainlexicon)]
    (println (str "🌾 " (name k) ":"))
    (doseq [syn syns]
      (println (str "   • " syn))))
  (println "")
  (println "now == next + 1")
  (println "🌾"))

(comment
  ;; Usage examples
  (resolve-synonym "grainbox")           ;; => :grainbox
  (resolve-synonym "graincanister")      ;; => :grainbox
  (get-all-synonyms :grainbox)           ;; => ["grainbox" "graincontainer" ...]
  (find-command "qb course sync system new") ;; => :qb-course-sync-system-new
  (print-lexicon))

