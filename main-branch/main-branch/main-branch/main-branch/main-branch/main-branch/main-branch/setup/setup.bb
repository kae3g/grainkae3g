#!/usr/bin/env bb
;; Setup Script - Pure Babashka
;; Initialize project directories and check prerequisites

(require '[babashka.process :refer [shell]]
         '[babashka.fs :as fs])

(defn print-header []
  (println "")
  (println "Documentation Pipeline Setup")
  (println "Pure Babashka Development Environment")
  (println ""))

(defn check-command [cmd label]
  (try
    (shell {:out :string :err :string :continue true} 
           "which" cmd)
    (println "✅" label)
    true
    (catch Exception _
      (println "❌" label "- not found")
      false)))

(defn check-prerequisites []
  (println "📋 Checking Prerequisites:")
  (println "")
  (let [has-bb (check-command "bb" "Babashka")
        has-nix (check-command "nix" "Nix")
        has-git (check-command "git" "Git")
        has-gh (check-command "gh" "GitHub CLI")]
    (println "")
    {:bb has-bb :nix has-nix :git has-git :gh has-gh}))

(defn setup-directories []
  (println "📁 Setting up directories...")
  (fs/create-dirs "build")
  (fs/create-dirs "docs")
  (fs/create-dirs "web-app/src/routes")
  (println "✨ Directories ready")
  (println ""))

(defn print-next-steps [prereqs]
  (println "Next Steps:")
  (println "")
  
  (if (:nix prereqs)
    (do (println "With Nix (recommended):")
        (println "  1. nix develop")
        (println "  2. bb doctor")
        (println "  3. bb build:pipeline")
        (println ""))
    (do (println "Without Nix:")
        (println "  Install Nix: https://nixos.org/download")
        (println "  Then run: nix develop")
        (println "")))
  
  (println "Or use bb to orchestrate Nix:")
  (println "  bb nix:run build:pipeline")
  (println "")
  
  (when (:gh prereqs)
    (println "Create GitHub repo:")
    (println "  bb gh:create-repo")
    (println ""))
  
  (println "📖 See README.md for full documentation")
  (println ""))

(defn -main [& _args]
  (print-header)
  (let [prereqs (check-prerequisites)]
    (setup-directories)
    (print-next-steps prereqs)
    (println "Setup complete!")
    (println "")))

(-main)

