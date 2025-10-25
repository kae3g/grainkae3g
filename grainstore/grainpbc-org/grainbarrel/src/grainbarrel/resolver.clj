(ns grainbarrel.resolver
  "Module name resolution with typo tolerance for grainbarrel
   
   Integrates grainregistry to make 'gb clomoko:build' work automatically."
  (:require [clojure.string :as str]))

;; Load grainregistry
(load-file "../grainregistry/src/grainregistry/core.clj")

(defn resolve-module-name
  "Resolve module name with typo tolerance and feedback
   
   Examples:
     (resolve-module-name \"clomoko\")
     ; Prints: 📝 Autocorrected: clomoko → clotoko
     => \"clotoko\"
     
     (resolve-module-name \"grainsix\")
     ; Prints: 📝 Autocorrected: grainsix → grain6
     => \"grain6\""
  [module-name]
  ((resolve 'grainregistry.core/resolve-with-feedback) module-name))

(defn resolve-task-string
  "Resolve 'module:task' string with typo tolerance
   
   Examples:
     (resolve-task-string \"grain-6:supervise\")
     ; Prints: 📝 Autocorrected: grain-6 → grain6
     => \"grain6:supervise\"
     
     (resolve-task-string \"clomoko:build\")
     ; Prints: 📝 Autocorrected: clomoko → clotoko
     => \"clotoko:build\""
  [task-str]
  ((resolve 'grainregistry.core/resolve-module-task) task-str))

(defn valid-module?
  "Check if module name is valid (exact or known typo/alias)
   
   Examples:
     (valid-module? \"grain6\") => true
     (valid-module? \"grainsix\") => true (alias)
     (valid-module? \"unknown\") => false"
  [module-name]
  ((resolve 'grainregistry.core/valid-name?) module-name))

(defn suggest-modules
  "Get module suggestions for a name
   
   Examples:
     (suggest-modules \"grainmusik\")
     => {:canonical \"grainmusic\"
         :confidence 0.875
         :match-type :typo}"
  [module-name]
  ((resolve 'grainregistry.core/suggest) module-name))

(defn list-all-modules
  "List all registered Grain Network modules
   
   Returns vector of canonical module names"
  []
  ((resolve 'grainregistry.core/all-registered)))

(defn modules-by-category
  "Get modules in a specific category
   
   Categories: :library, :tool, :service, :hardware, :transpiler, :media
   
   Examples:
     (modules-by-category :library)
     => [\"grain6\" \"graintime\" \"clojure-s6\" ...]"
  [category]
  ;; TODO: Implement category query
  (filter #(str/includes? % "grain") (list-all-modules)))

(comment
  ;; Test typo resolution
  (resolve-module-name "grainsix")
  ;=> "grain6"
  
  (resolve-task-string "grain-six:time")
  ;=> "grain6:time"
  
  (list-all-modules)
  ;=> ["grain6" "graintime" "clotoko" ...]
  )
