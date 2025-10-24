#!/usr/bin/env bb
;; build-apps.bb - Build all SixOS Humble UI applications
;; Builds all Grain Network desktop applications for distribution

(require '[babashka.fs :as fs]
         '[clojure.string :as str])

(def apps-dir "apps")
(def build-dir "build")

(defn build-app
  "Build a single Humble UI application."
  [app-name]
  (let [app-path (str apps-dir "/" app-name)
        build-path (str build-dir "/" app-name)]
    (println (str "Building " app-name "..."))
    
    ;; Create build directory
    (fs/create-dirs build-path)
    
    ;; Copy source files
    (when (fs/exists? (str app-path "/src"))
      (fs/copy-tree (str app-path "/src") (str build-path "/src")))
    
    ;; Copy deps.edn
    (when (fs/exists? (str app-path "/deps.edn"))
      (fs/copy (str app-path "/deps.edn") (str build-path "/deps.edn")))
    
    ;; Copy README
    (when (fs/exists? (str app-path "/README.md"))
      (fs/copy (str app-path "/README.md") (str build-path "/README.md")))
    
    (println (str "✅ " app-name " built successfully"))))

(defn build-all-apps
  "Build all Humble UI applications."
  []
  (println "🌾 Building all SixOS Humble UI applications...")
  
  ;; Create build directory
  (fs/create-dirs build-dir)
  
  ;; Build each app
  (doseq [app-dir (fs/glob apps-dir "*")]
    (let [app-name (fs/file-name app-dir)]
      (when (fs/directory? app-dir)
        (build-app app-name))))
  
  ;; Build core library
  (println "Building core library...")
  (fs/create-dirs (str build-dir "/core"))
  (when (fs/exists? "core/src")
    (fs/copy-tree "core/src" (str build-dir "/core/src")))
  (when (fs/exists? "core/deps.edn")
    (fs/copy "core/deps.edn" (str build-dir "/core/deps.edn")))
  (when (fs/exists? "core/README.md")
    (fs/copy "core/README.md" (str build-dir "/core/README.md")))
  (println "✅ Core library built successfully")
  
  (println "🎉 All applications built successfully!")
  (println (str "Build output: " build-dir)))

(defn main
  "Main entry point."
  [& args]
  (build-all-apps))

(main)
