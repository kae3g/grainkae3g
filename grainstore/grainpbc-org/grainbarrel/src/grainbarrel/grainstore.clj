(ns grainbarrel.grainstore
  "Pure functional grainstore manifest processing.
   
   The grainstore.edn is the single source of truth (input manifest).
   All derived files (dependency lists, README updates, deployment configs)
   are generated purely from this manifest."
  (:require [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.java.io :as io]))

;; Pure Functions - No Side Effects

(defn read-manifest
  "Read grainstore.edn manifest (pure function)"
  [manifest-path]
  (-> manifest-path
      slurp
      edn/read-string))

(defn get-modules
  "Extract all modules from manifest (pure function)"
  [manifest]
  (-> manifest :grainstore :modules))

(defn get-external-dependencies
  "Get all external (non-Grain PBC) dependencies (pure function)"
  [modules]
  (->> modules
       (filter (fn [[_k v]] (:external (:repos v))))
       (into {})))

(defn get-grain-pbc-modules
  "Get all Grain PBC modules (pure function)"
  [modules]
  (->> modules
       (remove (fn [[_k v]] (:external (:repos v))))
       (into {})))

(defn resolve-dependencies
  "Resolve all dependencies recursively (pure function)
   
   Returns a dependency graph showing what depends on what."
  [modules]
  (let [module-deps (into {}
                         (map (fn [[k v]]
                                [k (or (:depends-on v) [])])
                              modules))]
    (letfn [(get-all-deps [module-key visited]
              (if (visited module-key)
                #{}  ;; Circular dependency protection
                (let [direct-deps (get module-deps module-key [])
                      visited' (conj visited module-key)]
                  (reduce (fn [acc dep]
                           (into acc (conj (get-all-deps dep visited') dep)))
                         (set direct-deps)
                         direct-deps))))]
      (into {}
            (map (fn [[k _v]]
                   [k (get-all-deps k #{})])
                 module-deps)))))

(defn dependency-sort
  "Topologically sort modules by dependencies (pure function)
   
   Returns modules in order where dependencies come before dependents."
  [modules]
  (let [deps (resolve-dependencies modules)
        no-deps (filter (fn [[k v]] (empty? v)) deps)
        sorted (atom (mapv first no-deps))
        remaining (atom (apply dissoc deps (map first no-deps)))]
    
    (while (seq @remaining)
      (let [ready (filter (fn [[_k v]]
                           (every? (set @sorted) v))
                         @remaining)]
        (if (empty? ready)
          ;; Circular dependency detected
          (throw (ex-info "Circular dependency detected"
                         {:remaining @remaining}))
          (do
            (swap! sorted into (map first ready))
            (swap! remaining #(apply dissoc % (map first ready)))))))
    
    @sorted))

(defn generate-deps-edn
  "Generate deps.edn from grainstore manifest (pure function)"
  [manifest module-keys]
  (let [modules (get-modules manifest)
        selected-modules (select-keys modules module-keys)
        
        ;; Resolve all transitive dependencies
        all-deps (reduce (fn [acc module-key]
                          (into acc (get (resolve-dependencies modules) module-key)))
                        (set module-keys)
                        module-keys)
        
        ;; Get external dependencies
        external (get-external-dependencies modules)
        external-deps (select-keys external all-deps)
        
        ;; Build deps.edn structure
        deps-map
        {:paths (mapv #(str "grainstore/" (name %) "/src")
                     (filter (fn [k]
                              (and (contains? modules k)
                                   (not (get-in modules [k :repos :external]))))
                            all-deps))
         
         :deps
         (into {}
               (concat
                ;; External Maven dependencies
                (map (fn [[_k v]]
                      (when-let [maven (:maven v)]
                        [(symbol (:group-id maven) (:artifact-id maven))
                         {:mvn/version (:latest-version maven)}]))
                    external-deps)
                
                ;; Grain PBC git dependencies
                (map (fn [[k v]]
                      (when-not (get-in v [:repos :external])
                        [(symbol (str "org.grainpbc/" (name k)))
                         {:git/url (get-in v [:repos :github])
                          :git/sha "main"}]))
                    (select-keys modules all-deps))))}]
    
    deps-map))

(defn generate-readme-links
  "Generate README link section from manifest (pure function)"
  [manifest]
  (let [platforms (-> manifest :grainstore :platforms)]
    (str "## 🔗 Grain Network Links\n\n"
         "**GitHub Organization**: " (get-in platforms [:github :base-url]) "\n"
         "**Codeberg Organization**: " (get-in platforms [:codeberg :base-url]) "\n"
         "**GitHub Pages**: " (get-in platforms [:github :pages-url]) "\n"
         "**Codeberg Pages**: " (get-in platforms [:codeberg :pages-url]) "\n")))

(defn generate-module-list-markdown
  "Generate markdown list of all modules (pure function)"
  [manifest]
  (let [modules (get-modules manifest)
        external (get-external-dependencies modules)
        grain-pbc (get-grain-pbc-modules modules)]
    
    (str "## 📦 Grainstore Modules\n\n"
         "### Grain PBC Modules (" (count grain-pbc) ")\n\n"
         (str/join "\n"
                  (map (fn [[k v]]
                        (str "- **" (name k) "**: " (:description v)
                             (when-let [aliases (:aliases v)]
                               (str " (aliases: " (str/join ", " aliases) ")"))))
                      (sort-by first grain-pbc)))
         "\n\n"
         "### External Dependencies (" (count external) ")\n\n"
         (str/join "\n"
                  (map (fn [[k v]]
                        (str "- **" (name k) "**: " (:description v)
                             " - " (get-in v [:repos :github])))
                      (sort-by first external)))
         "\n")))

(defn generate-dependency-graph-mermaid
  "Generate Mermaid.js dependency graph (pure function)"
  [manifest]
  (let [modules (get-modules manifest)
        deps (resolve-dependencies modules)]
    
    (str "```mermaid\ngraph TD\n"
         (str/join "\n"
                  (for [[module dependencies] (sort-by first deps)
                        dep dependencies]
                   (str "    " (name module) " --> " (name dep))))
         "\n```\n")))

(defn validate-manifest
  "Validate grainstore manifest (pure function)
   
   Returns {:valid? boolean :errors [...]}"
  [manifest]
  (let [errors (atom [])]
    
    ;; Check required top-level fields
    (when-not (get-in manifest [:grainstore :version])
      (swap! errors conj "Missing :version"))
    
    (when-not (get-in manifest [:grainstore :modules])
      (swap! errors conj "Missing :modules"))
    
    ;; Check each module
    (doseq [[module-key module-data] (get-modules manifest)]
      (when-not (:description module-data)
        (swap! errors conj (str "Module " module-key " missing :description")))
      
      (when-not (:license module-data)
        (swap! errors conj (str "Module " module-key " missing :license")))
      
      (when-not (:repos module-data)
        (swap! errors conj (str "Module " module-key " missing :repos")))
      
      ;; Check dependency references
      (doseq [dep (:depends-on module-data)]
        (when-not (contains? (get-modules manifest) dep)
          (swap! errors conj (str "Module " module-key " depends on unknown module: " dep)))))
    
    {:valid? (empty? @errors)
     :errors @errors}))

(defn module-stats
  "Calculate statistics about grainstore (pure function)"
  [manifest]
  (let [modules (get-modules manifest)
        external (get-external-dependencies modules)
        grain-pbc (get-grain-pbc-modules modules)
        deps (resolve-dependencies modules)]
    
    {:total-modules (count modules)
     :grain-pbc-modules (count grain-pbc)
     :external-dependencies (count external)
     :licenses (set (map :license (vals modules)))
     :statuses (frequencies (map :status (vals modules)))
     :max-dependency-depth (apply max 0 (map (comp count second) deps))
     :modules-with-no-deps (count (filter (fn [[_k v]] (empty? v)) deps))
     :most-depended-on (first (apply max-key
                                     (fn [[_k dependents]]
                                       (count dependents))
                                     (reduce (fn [acc [module deps]]
                                              (reduce (fn [a d]
                                                       (update a d (fnil conj []) module))
                                                     acc
                                                     deps))
                                            {}
                                            deps)))}))

;; Side Effects - IO Operations

(defn write-deps-edn!
  "Write deps.edn file (side effect)"
  [deps-edn-data output-path]
  (spit output-path (with-out-str (clojure.pprint/pprint deps-edn-data)))
  output-path)

(defn update-readme-with-links!
  "Update README.md with generated links (side effect)"
  [readme-path manifest]
  (let [existing (slurp readme-path)
        links (generate-readme-links manifest)
        
        ;; Find where to insert or replace links
        updated (if (str/includes? existing "## 🔗 Grain Network Links")
                 ;; Replace existing section
                 (str/replace existing
                            #"## 🔗 Grain Network Links\n\n.*?(?=\n##|\z)"
                            links)
                 ;; Insert at beginning
                 (str links "\n\n" existing))]
    (spit readme-path updated)
    readme-path))

(comment
  ;; Example usage
  (def manifest (read-manifest "grainstore/grainstore.edn"))
  
  ;; Validate manifest
  (validate-manifest manifest)
  ;; => {:valid? true :errors []}
  
  ;; Get stats
  (module-stats manifest)
  ;; => {:total-modules 20 :grain-pbc-modules 17 :external-dependencies 3 ...}
  
  ;; Generate deps.edn for specific modules
  (generate-deps-edn manifest [:grainweb :grainmusic])
  ;; => {:paths [...] :deps {...}}
  
  ;; Get dependency graph
  (generate-dependency-graph-mermaid manifest)
  ;; => "```mermaid\ngraph TD\n..."
  
  ;; Validate all dependencies exist
  (let [result (validate-manifest manifest)]
    (when-not (:valid? result)
      (println "Errors found:")
      (doseq [err (:errors result)]
        (println "  -" err)))))

