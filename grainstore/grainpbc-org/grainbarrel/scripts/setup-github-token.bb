#!/usr/bin/env bb

(require '[clojure.string :as str]
         '[clojure.java.shell :as shell])

(defn setup-github-token
  "Setup GitHub token for repository description updates"
  []
  (println "🔑 GitHub Token Setup")
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
  (println "")
  (println "To update GitHub repository descriptions, you need a GitHub Personal Access Token.")
  (println "")
  (println "1. Go to: https://github.com/settings/tokens")
  (println "2. Click 'Generate new token (classic)'")
  (println "3. Select scopes: 'repo' (Full control of private repositories)")
  (println "4. Copy the generated token")
  (println "")
  (print "Enter your GitHub token: ")
  (flush)
  (let [token (read-line)]
    (if (str/blank? token)
      (println "❌ No token provided. GitHub description updates will be skipped.")
      (do
        ;; Set git config
        (shell/sh "git" "config" "--global" "github.token" token)
        (println "")
        (println "✅ GitHub token configured!")
        (println "   You can now update repository descriptions with 'gb grainsync course new'")
        (println "")
        (println "🔧 Alternative: Set GITHUB_TOKEN environment variable")
        (println "   export GITHUB_TOKEN=your_token_here"))))))

;; Run the setup
(setup-github-token)
