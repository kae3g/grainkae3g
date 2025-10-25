#!/usr/bin/env bb

(require '[clojure.edn :as edn]
         '[clojure.string :as str]
         '[clojure.pprint :as pprint]
         '[clojure.java.io :as io])

(defn find-grainstore-edn []
  "Find grainstore.edn relative to current directory"
  (cond
    (.exists (io/file "grainstore/grainstore.edn")) "grainstore/grainstore.edn"
    (.exists (io/file "grainstore.edn")) "grainstore.edn"
    (.exists (io/file "../grainstore.edn")) "../grainstore.edn"
    (.exists (io/file "../../grainstore.edn")) "../../grainstore.edn"
    :else (throw (ex-info "Cannot find grainstore.edn" {}))))

(defn read-manifest []
  (-> (find-grainstore-edn)
      slurp
      edn/read-string))

(defn get-modules [manifest]
  (-> manifest :grainstore :modules))

(defn get-external-deps [modules]
  (->> modules
       (filter (fn [[_k v]] (get-in v [:repos :external])))
       (into {})))

(defn get-grain-pbc-modules [modules]
  (->> modules
       (remove (fn [[_k v]] (get-in v [:repos :external])))
       (into {})))

(defn generate-external-deps-md [manifest]
  (let [modules (get-modules manifest)
        external (get-external-deps modules)]
    
    (str "# External Dependencies\n\n"
         "**Generated from grainstore.edn** - Do not edit manually!\n\n"
         "These are external dependencies that Grain Network modules depend on.\n\n"
         "## Summary\n\n"
         "- Total External Dependencies: " (count external) "\n"
         "- All from verified open-source projects\n"
         "- Licenses: " (str/join ", " (distinct (map :license (vals external)))) "\n\n"
         "## Dependencies\n\n"
         (str/join "\n\n"
                  (map (fn [[k v]]
                        (str "### " (name k) "\n\n"
                             "- **Description**: " (:description v) "\n"
                             "- **License**: " (:license v) "\n"
                             "- **GitHub**: " (get-in v [:repos :github]) "\n"
                             (when-let [website (get-in v [:pages :website])]
                               (str "- **Website**: " website "\n"))
                             (when-let [maven (:maven v)]
                               (str "- **Maven**: `" (:group-id maven) "/" (:artifact-id maven) "`\n"
                                   "- **Version**: " (:latest-version maven) "\n"))
                             (when-let [binary (:binary v)]
                               (str "- **Binary**: `" (:command binary) "`\n"
                                   "- **Install**: " (:install-url binary) "\n"))))
                      (sort-by first external)))
         "\n---\n\n"
         "*Generated on " (str (java.time.LocalDateTime/now)) "*\n")))

(defn generate-module-list-md [manifest]
  (let [modules (get-modules manifest)
        grain-pbc (get-grain-pbc-modules modules)]
    
    (str "# Grain PBC Modules\n\n"
         "**Generated from grainstore.edn** - Do not edit manually!\n\n"
         "## Summary\n\n"
         "- Total Grain PBC Modules: " (count grain-pbc) "\n"
         "- Deployment Strategy: Dual (GitHub + Codeberg)\n"
         "- Grainstore Version: " (get-in manifest [:grainstore :version]) "\n\n"
         "## Modules by Category\n\n"
         (str/join "\n\n"
                  (for [[category modules-in-cat]
                        (group-by (fn [[_k v]]
                                   (cond
                                     (str/includes? (str (:path v)) "clojure-") "Core Libraries"
                                     (str/includes? (str (:path v)) "grain") "Applications"
                                     :else "Infrastructure"))
                                 grain-pbc)]
                    (str "### " category "\n\n"
                         (str/join "\n"
                                  (map (fn [[k v]]
                                        (str "- **" (name k) "**: " (:description v)
                                             (when-let [aliases (:aliases v)]
                                               (str " (aliases: " (str/join ", " aliases) ")"))))
                                      (sort-by first modules-in-cat))))))
         "\n\n---\n\n"
         "*Generated on " (str (java.time.LocalDateTime/now)) "*\n")))

(defn generate-deps-graph-mermaid [manifest]
  (let [modules (get-modules manifest)
        grain-pbc (get-grain-pbc-modules modules)]
    
    (str "# Dependency Graph\n\n"
         "**Generated from grainstore.edn** - Do not edit manually!\n\n"
         "```mermaid\ngraph TD\n"
         (str/join "\n"
                  (for [[module-key module-data] grain-pbc
                        dep (:depends-on module-data)]
                   (str "    " (name module-key) "[" (name module-key) "] --> " (name dep) "[" (name dep) "]")))
         "\n```\n\n"
         "## Legend\n\n"
         "- **Solid arrows**: Direct dependencies\n"
         "- **Module names**: Click to see module details\n\n"
         "---\n\n"
         "*Generated on " (str (java.time.LocalDateTime/now)) "*\n")))

(defn get-output-dir []
  "Get the grainstore directory path for output files"
  (cond
    (.exists (io/file "grainstore")) "grainstore"
    (.exists (io/file ".")) "."
    (.exists (io/file "..")) ".."
    :else "."))

(defn main []
  (println "🌾 Grainstore Documentation Generator\n")
  (println "Generating documentation from grainstore.edn...\n")
  
  (let [manifest (read-manifest)
        output-dir (get-output-dir)]
    
    ;; Generate external dependencies doc
    (let [output-file (str output-dir "/EXTERNAL-DEPENDENCIES.md")
          content (generate-external-deps-md manifest)]
      (spit output-file content)
      (println (str "✅ Generated: " output-file)))
    
    ;; Generate module list doc
    (let [output-file (str output-dir "/MODULES.md")
          content (generate-module-list-md manifest)]
      (spit output-file content)
      (println (str "✅ Generated: " output-file)))
    
    ;; Generate dependency graph
    (let [output-file (str output-dir "/DEPENDENCY-GRAPH.md")
          content (generate-deps-graph-mermaid manifest)]
      (spit output-file content)
      (println (str "✅ Generated: " output-file)))
    
    (println "\n🌾 Documentation generation complete!")
    (println "\n💡 These files are generated from grainstore.edn")
    (println "   Do not edit them manually - edit grainstore.edn instead!")
    (println "   Then run: gb grainstore:generate-docs")))

(main)

