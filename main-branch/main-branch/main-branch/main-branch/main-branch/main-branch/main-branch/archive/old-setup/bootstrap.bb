#!/usr/bin/env bb
;; Bootstrap script that can run with just babashka
;; Will set up Nix environment if needed

(require '[babashka.process :refer [shell process]]
         '[clojure.string :as str])

(defn command-exists? [cmd]
  (try
    (= 0 (:exit (shell {:out :string :err :string :continue true}
                       "which" cmd)))
    (catch Exception _ false)))

(defn check-prerequisites []
  (println "🌙 Checking Sacred Technology Prerequisites")
  (println "")
  
  (let [has-bb (command-exists? "bb")
        has-nix (command-exists? "nix")
        has-git (command-exists? "git")
        has-gh (command-exists? "gh")]
    
    (println (if has-bb "✅" "❌") "Babashka:" 
             (if has-bb "installed" "NOT FOUND"))
    (println (if has-nix "✅" "❌") "Nix:" 
             (if has-nix "installed" "NOT FOUND"))
    (println (if has-git "✅" "❌") "Git:" 
             (if has-git "installed" "NOT FOUND"))
    (println (if has-gh "✅" "⚠️ ") "GitHub CLI:" 
             (if has-gh "installed" "optional"))
    (println "")
    
    {:babashka has-bb
     :nix has-nix
     :git has-git
     :gh has-gh}))

(defn enter-nix-shell []
  (println "🌙 Entering Nix development shell...")
  (println "   Run: nix develop")
  (println "   Then: bb doctor")
  (System/exit 0))

(defn -main [& args]
  (println "🤖 Robotic Farm Pipeline Bootstrap")
  (println "   Sacred consciousness development")
  (println "")
  
  (let [prereqs (check-prerequisites)]
    (cond
      (not (:babashka prereqs))
      (do (println "❌ Babashka required but not found")
          (println "   Install: brew install babashka")
          (println "   Or: nix-env -iA nixpkgs.babashka")
          (System/exit 1))
      
      (not (:nix prereqs))
      (do (println "⚠️  Nix not found (recommended)")
          (println "   Install: https://nixos.org/download")
          (println "   Continuing without Nix..."))
      
      :else
      (do (println "✨ Prerequisites satisfied!")
          (println "")
          (println "Next steps:")
          (println "  1. nix develop (enter shell)")
          (println "  2. bb doctor (verify tools)")
          (println "  3. bb build:pipeline (run build)")
          (println "")))))

(-main)

