#!/usr/bin/env bb
;; Sacred Test Runner - Run all bb commands
;; "By their fruits you will know them." 
;; - The Gospel According to Jesus (Stephen Mitchell)

(require '[babashka.process :refer [shell]]
         '[babashka.fs :as fs])

(def tests-run (atom 0))
(def tests-passed (atom 0))
(def tests-warned (atom 0))

(defn test-command [name cmd]
  (swap! tests-run inc)
  (println "")
  (println "=================================")
  (println (str "Testing: " name))
  (println "=================================")
  (println (str "Command: " cmd))
  (println "")
  
  (try
    (apply shell (clojure.string/split cmd #"\s+"))
    (swap! tests-passed inc)
    (println "")
    (println (str "✅ " name " passed"))
    true
    (catch Exception e
      (swap! tests-warned inc)
      (println "")
      (println (str "⚠️  " name " had warnings"))
      (println (str "   " (.getMessage e)))
      false)))

(defn -main []
  (println "🌙 Sacred Robotic Farm Pipeline")
  (println "   Complete Test Suite")
  (println "")
  (println "\"Test everything; hold fast to")
  (println " what is good.\"")
  (println "- 1 Thessalonians 5:21")
  (println "")
  
  ;; 1. Bootstrap & Setup
  (test-command "Bootstrap" "bb bootstrap")
  (test-command "Setup Script" "./setup.bb")
  
  ;; 2. Health Checks
  (test-command "Doctor" "bb doctor")
  (test-command "List Tasks" "bb tasks")
  
  ;; 3. Nix Integration
  (test-command "Nix Check" "bb nix:check")
  (test-command "Nix Develop Info" 
                "bb nix:develop")
  
  ;; 4. Pipeline (if docs exist)
  (when (seq (fs/glob "docs" "*.md"))
    (test-command "Wrap Markdown" 
                  "bb wrap:markdown")
    (test-command "Parse Markdown" 
                  "bb parse:markdown")
    (test-command "Validate DSL" 
                  "bb validate:dsl")
    (test-command "Generate Svelte" 
                  "bb generate:svelte")
    (test-command "Build Pipeline" 
                  "bb build:pipeline"))
  
  ;; 5. Git/GitHub
  (test-command "GitHub Check" "bb gh:check")
  (test-command "Git Status" "bb gh:status")
  
  ;; 6. CI Verification
  (test-command "CI Verify" "bb ci:verify")
  
  ;; Summary
  (println "")
  (println "=================================")
  (println "🌾 Test Suite Complete!")
  (println "=================================")
  (println "")
  (println (str "Tests run:    " @tests-run))
  (println (str "Tests passed: " @tests-passed))
  (println (str "Warnings:     " @tests-warned))
  (println "")
  (println "\"The harvest is plentiful, but")
  (println " the workers are few.\"")
  (println "- Gospel According to Jesus")
  (println "  (Stephen Mitchell)")
  (println "")
  (println "🤖 May your automation serve")
  (println "   earth intelligence with")
  (println "   Divine Grace")
  (println "")
  (println "🤖🌙🌾"))

(-main)

