#!/usr/bin/env bb

(ns deploy-to-pages
  (:require [clojure.java.shell :as sh]
            [clojure.string :as str]
            [babashka.fs :as fs]))

(defn run-cmd [& args]
  "Run a shell command and return result"
  (let [result (apply sh/sh args)]
    (when-not (zero? (:exit result))
      (println "Error:" (:err result))
      (System/exit 1))
    result))

(defn build-content []
  (println "📝 Generating site configuration...")
  (run-cmd "bb" "config:generate")
  (println "📝 Building content with Babashka (incremental)...")
  (run-cmd "bb" "writings:build-fast"))

(defn build-site []
  (println "🔧 Building SvelteKit site...")
  (run-cmd "bash" "-c" "cd web-app && npm ci && npm run build"))

(defn deploy-to-pages []
  (println "🚀 Deploying to Codeberg Pages branch...")
  (let [build-dir "web-app/build"
        timestamp (str (java.time.LocalDateTime/now))]
    
    ;; Navigate to build directory
    (when-not (fs/exists? build-dir)
      (println "❌ Build directory not found. Run build first.")
      (System/exit 1))
    
    ;; Initialize git repo in build directory
    (println "📦 Initializing git in build directory...")
    (run-cmd "bash" "-c" (str "cd " build-dir " && git init"))
    (run-cmd "bash" "-c" (str "cd " build-dir " && git config user.name 'kae3g'"))
    (run-cmd "bash" "-c" (str "cd " build-dir " && git config user.email 'kae3g@codeberg.org'"))
    
    ;; Create pages branch and commit
    (println "📋 Creating pages branch and committing files...")
    (run-cmd "bash" "-c" (str "cd " build-dir " && git checkout -b pages"))
    (run-cmd "bash" "-c" (str "cd " build-dir " && git add ."))
    (run-cmd "bash" "-c" (str "cd " build-dir " && git commit -m 'Deploy: " timestamp "'"))
    
    ;; Push to pages branch
    (println "📤 Pushing to Codeberg pages branch...")
    (run-cmd "bash" "-c" (str "cd " build-dir " && git push --force git@codeberg.org:kae3g/12025-10.git pages:pages"))
    
    (println "")
    (println "✅ Deployment complete!")
    (println "🌐 Site will be live at: https://kae3g.codeberg.page/12025-10/")
    (println "⏱️  Give Codeberg Pages 1-2 minutes to rebuild")))

(defn clean-build-dir []
  (println "🧹 Cleaning old build directory...")
  (when (fs/exists? "web-app/build/.git")
    (fs/delete-tree "web-app/build/.git"))
  (println "✅ Clean complete"))

(defn full-deploy []
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
  (println "🔧 Full Deployment Pipeline")
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
  (println "")
  
  (clean-build-dir)
  (build-content)
  (build-site)
  (deploy-to-pages)
  
  (println "")
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
  (println "🎉 All done! Check your site in a few minutes."))

(defn -main [& args]
  (let [cmd (first args)]
    (case cmd
      "build-content" (build-content)
      "build-site" (build-site)
      "deploy" (deploy-to-pages)
      "clean" (clean-build-dir)
      "full" (full-deploy)
      (do
        (println "Usage: bb deploy-to-pages.bb [build-content|build-site|deploy|clean|full]")
        (println "")
        (println "Commands:")
        (println "  build-content  - Build content JSON from markdown")
        (println "  build-site     - Build SvelteKit site")
        (println "  deploy         - Deploy existing build to pages branch")
        (println "  clean          - Clean old build git directory")
        (println "  full           - Run all steps (recommended)")
        (System/exit 1)))))

(when (= *file* (System/getProperty "babashka.file"))
  (apply -main *command-line-args*))
