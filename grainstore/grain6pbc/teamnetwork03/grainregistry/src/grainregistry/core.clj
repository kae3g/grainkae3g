(ns grainregistry.core
  "Unified name resolution for Grain Network
   
   Wraps clojure-sixos with Grain Network-specific enhancements
   and provides integration helpers for grainbarrel and grainsource."
  (:require [clojure.string :as str]))

;; Load clojure-sixos
(load-file "../clojure-sixos/src/clojure_sixos/similarity.clj")
(load-file "../clojure-sixos/src/clojure_sixos/registry.clj")
(load-file "../clojure-sixos/src/clojure_sixos/typo.clj")
(load-file "../clojure-sixos/src/clojure_sixos/core.clj")

;; =============================================================================
;; Re-export Core Functions
;; =============================================================================

(defn resolve-name
  "Resolve name to canonical form with typo tolerance
   
   Examples:
     (resolve-name \"grainsix\") => \"grain6\"
     (resolve-name \"clomoko\") => \"clotoko\""
  [name]
  ((resolve 'clojure-sixos.core/resolve-name) name))

(defn valid-name?
  "Check if name is valid (exact or known typo/alias)
   
   Examples:
     (valid-name? \"grain6\") => true
     (valid-name? \"grainsix\") => true
     (valid-name? \"unknown\") => false"
  [name]
  ((resolve 'clojure-sixos.core/valid-name?) name))

(defn suggest
  "Get suggestions for a name
   
   Examples:
     (suggest \"grainmusik\")
     => {:canonical \"grainmusic\"
         :confidence 0.875
         :match-type :typo}"
  [name]
  ((resolve 'clojure-sixos.core/suggest) name))

(defn register
  "Register a new name in the registry
   
   Examples:
     (register \"mymodule\" {:typos [\"my-module\"]
                             :aliases [\"mm\"]
                             :category :tool})"
  [name opts]
  ((resolve 'clojure-sixos.core/register) name opts))

(defn all-registered
  "Get all registered canonical names"
  []
  ((resolve 'clojure-sixos.core/all-registered)))

;; =============================================================================
;; Grain-Specific Utilities
;; =============================================================================

(defn resolve-with-feedback
  "Resolve name and print user feedback
   
   Prints autocorrection message if name was corrected.
   
   Examples:
     (resolve-with-feedback \"grainsix\")
     ; Prints: 📝 Autocorrected: grainsix → grain6
     => \"grain6\""
  [name]
  (let [resolved (resolve-name name)]
    (when (not= name resolved)
      (println (str "📝 Autocorrected: " name " → " resolved)))
    resolved))

(defn resolve-or-fail
  "Resolve name or exit with helpful error message
   
   If name cannot be resolved, prints suggestions and exits.
   
   Examples:
     (resolve-or-fail \"grain6\") => \"grain6\"
     (resolve-or-fail \"unknown\") => exits with suggestions"
  [name]
  (let [result (suggest name)]
    (if (:canonical result)
      (do
        (when (not= name (:canonical result))
          (println (str "📝 Autocorrected: " name " → " (:canonical result))))
        (:canonical result))
      (do
        (println (str "❌ Unknown module: " name))
        (println "")
        (println "Did you mean one of these?")
        (let [all-names (all-registered)
              similarity (resolve 'clojure-sixos.similarity/similarity)
              suggestions (filter #(> (similarity name %) 0.4) all-names)]
          (doseq [suggestion (take 5 (sort-by #(- (similarity name %)) suggestions))]
            (println (str "  - " suggestion))))
        (println "")
        (println "See all modules: grainresolve list")
        (System/exit 1)))))

(defn resolve-module-task
  "Resolve 'module:task' string with typo tolerance
   
   Examples:
     (resolve-module-task \"grainsix:time\")
     => \"grain6:time\"
     
     (resolve-module-task \"clomoko:build\")
     ; Prints: 📝 Autocorrected: clomoko → clotoko
     => \"clotoko:build\""
  [module-task-str]
  (let [[module task] (str/split module-task-str #":" 2)]
    (str (resolve-with-feedback module) ":" task)))

(defn resolve-module-subtask
  "Resolve 'module:subtask:action' with typo tolerance
   
   Examples:
     (resolve-module-subtask \"grain-6:test:run\")
     => \"grain6:test:run\""
  [module-subtask-str]
  (let [parts (str/split module-subtask-str #":")
        module (first parts)
        rest-parts (rest parts)]
    (str/join ":" (cons (resolve-with-feedback module) rest-parts))))

;; =============================================================================
;; Integration Helpers
;; =============================================================================

(defn for-grainbarrel
  "Create grainbarrel-specific resolver
   
   Usage in grainbarrel/src/grainbarrel/core.clj:
     (require '[grainregistry.core :as registry])
     (def resolve-task registry/for-grainbarrel)"
  []
  {:resolve-task resolve-module-task
   :resolve-module resolve-with-feedback
   :check-valid valid-name?})

(defn for-grainsource
  "Create grainsource-specific resolver
   
   Usage in grainsource/src/grainsource/core.clj:
     (require '[grainregistry.core :as registry])
     (def resolve-repo registry/for-grainsource)"
  []
  {:resolve-repo resolve-with-feedback
   :resolve-url (fn [repo-name org]
                  (let [resolved (resolve-with-feedback repo-name)
                        base (case org
                               :grainpbc "https://github.com/grainpbc/"
                               :codeberg "https://codeberg.org/grainpbc/"
                               "https://github.com/grainpbc/")]
                    (str base resolved)))})

;; =============================================================================
;; CLI
;; =============================================================================

(defn -main [& args]
  "Main CLI entry point"
  (case (first args)
    "resolve" (do
                (println (resolve-name (second args))))
    
    "suggest" (do
                (clojure.pprint/pprint (suggest (second args))))
    
    "list" (do
             (println "🌾 Registered Grain Network Modules:")
             (println "")
             (doseq [name (sort (all-registered))]
               (println (str "  - " name))))
    
    "register" (do
                 (let [name (second args)
                       opts (read-string (nth args 2 "{}"))]
                   (register name opts)
                   (println (str "✅ Registered: " name))))
    
    "task" (do
             (println (resolve-module-task (second args))))
    
    "help" (do
             (println "🌾 grainregistry (grainresolver, graintypo)")
             (println "")
             (println "USAGE:")
             (println "  grainresolve resolve NAME       Resolve name to canonical")
             (println "  grainresolve suggest NAME       Get suggestions")
             (println "  grainresolve list               List all registered names")
             (println "  grainresolve register NAME OPTS Register new name")
             (println "  grainresolve task MODULE:TASK   Resolve module:task")
             (println "")
             (println "EXAMPLES:")
             (println "  grainresolve resolve grainsix")
             (println "  grainresolve suggest grainmusik")
             (println "  grainresolve task grain-6:supervise")
             (println ""))
    
    ;; Default: resolve
    (println (resolve-name (first args)))))
