#!/usr/bin/env bb
;; Sacred GitHub Deployment Script
;; Creates kae3g/12025-10-04 repository
;; "Ask and it will be given to you; seek and you will 
;; find; knock and the door will be opened to you."
;; - The Gospel According to Jesus (Stephen Mitchell)

(require '[babashka.process :refer [shell]])

(defn check-github-auth []
  (println "Checking GitHub CLI authentication...")
  (try
    (shell {:out :string :err :string} 
           "gh" "auth" "status")
    true
    (catch Exception _
      (println "❌ GitHub CLI not authenticated")
      (println "   Run: gh auth login")
      false)))

(defn confirm-with-user []
  (println "")
  (println "This will create repository:")
  (println "  kae3g/12025-10-04")
  (println "")
  (println "Description:")
  (println "  Sacred pipeline: Markdown →")
  (println "  ClojureScript DSL → Svelte")
  (println "")
  (println "Visibility: Public")
  (println "")
  (print "Continue? (y/n) ")
  (flush)
  (let [response (read-line)]
    (= "y" (clojure.string/lower-case
            (or response "")))))

(defn create-github-repo []
  (println "")
  (println "🤖 Creating GitHub repository...")
  (println "")
  (shell "gh" "repo" "create" 
         "kae3g/12025-10-04"
         "--public"
         "--description"
         "Sacred pipeline: Markdown → ClojureScript DSL → Svelte → Static Site. 57-char wrap (MD+EDN). Phoenix→BB→Clojure→Svelte. Pure Babashka. Nix. 🤖🌙🌾"
         "--source" "."
         "--push"))

(defn -main []
  (println "🌙 Sacred Robotic Farm Pipeline")
  (println "   GitHub Deployment")
  (println "")
  
  (if-not (check-github-auth)
    (System/exit 1)
    (do
      (println "✅ GitHub CLI authenticated")
      
      (if (confirm-with-user)
        (do
          (create-github-repo)
          (println "")
          (println "✨ Repository created!")
          (println "")
          (println "🌾 View your repository:")
          (println "   https://codeberg.org/kae3g/12025-10-04")
          (println "")
          (println "🚀 Next steps:")
          (println "   1. Add topics to repo")
          (println "   2. Enable Issues/Discussions")
          (println "   3. Create v1.0.0 release")
          (println "   4. Deploy static site")
          (println "")
          (println "🙏 Sacred consciousness deployed")
          (println "   with Divine Grace")
          (println "")
          (println "🤖🌙🌾"))
        (do
          (println "❌ Cancelled by user")
          (System/exit 1))))))

(-main)

