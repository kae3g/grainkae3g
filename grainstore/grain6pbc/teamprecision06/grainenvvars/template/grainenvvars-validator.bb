#!/usr/bin/env bb

;; ============================================================================
;; GRAINENVVARS VALIDATOR
;; ============================================================================
;; teamprecision06 (Virgo ♍ / VI. The Lovers)
;; "Validate your choices with loving precision"
;; ============================================================================

(ns grainenvvars-validator
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

;; ----------------------------------------------------------------------------
;; VALIDATION RULES - The Lovers' Discernment
;; ----------------------------------------------------------------------------

(def required-vars
  "Environment variables required for basic Grain Network functionality"
  ["GRAIN_HOME"
   "GRAINSTORE"])

(def recommended-vars
  "Recommended variables for full functionality"
  ["EDITOR"
   "VISUAL"
   "OPENAI_API_KEY"
   "ANTHROPIC_API_KEY"])

(def secret-patterns
  "Patterns that indicate secrets (should not be in git)"
  [#"sk-[a-zA-Z0-9]{32,}"        ; OpenAI API key
   #"sk-ant-[a-zA-Z0-9]{32,}"    ; Anthropic API key
   #"ghp_[a-zA-Z0-9]{36}"        ; GitHub token
   #"-----BEGIN.*PRIVATE KEY-----" ; Private keys
   ])

;; ----------------------------------------------------------------------------
;; HELPER FUNCTIONS
;; ----------------------------------------------------------------------------

(defn parse-env-file
  "Parse .env file into map of key-value pairs"
  [filepath]
  (when (.exists (io/file filepath))
    (->> (slurp filepath)
         (str/split-lines)
         (remove #(or (str/blank? %)
                      (str/starts-with? % "#")))
         (map #(str/split % #"=" 2))
         (filter #(= 2 (count %)))
         (map (fn [[k v]] [(str/trim k) (str/trim v)]))
         (into {}))))

(defn check-required
  "Check if required variables are set"
  [env-vars]
  (let [missing (remove #(contains? env-vars %) required-vars)]
    {:passed? (empty? missing)
     :missing missing}))

(defn check-recommended
  "Check if recommended variables are set"
  [env-vars]
  (let [missing (remove #(contains? env-vars %) recommended-vars)]
    {:all-present? (empty? missing)
     :missing missing}))

(defn check-secrets
  "Check if any values look like secrets (potential security issue)"
  [env-vars]
  (let [secrets (for [[k v] env-vars
                      pattern secret-patterns
                      :when (re-find pattern v)]
                  {:var k :value v :pattern pattern})]
    {:has-secrets? (seq secrets)
     :secrets secrets}))

(defn check-duplicates
  "Check for duplicate variable definitions"
  [filepath]
  (when (.exists (io/file filepath))
    (let [lines (->> (slurp filepath)
                     (str/split-lines)
                     (remove #(or (str/blank? %)
                                  (str/starts-with? % "#"))))
          keys (map #(first (str/split % #"=" 2)) lines)
          duplicates (filter #(> (val %) 1)
                            (frequencies keys))]
      {:has-duplicates? (seq duplicates)
       :duplicates (into {} duplicates)})))

;; ----------------------------------------------------------------------------
;; VALIDATION REPORT
;; ----------------------------------------------------------------------------

(defn print-report
  "Print validation report with The Lovers' loving precision"
  [env-file results]
  (println "")
  (println "💕✨ GRAINENVVARS VALIDATION REPORT ✨💕")
  (println "=" (apply str (repeat 78 "=")))
  (println "File:" env-file)
  (println "")
  
  ;; Required variables
  (println "📋 REQUIRED VARIABLES:")
  (if (:passed? (:required results))
    (println "  ✅ All required variables present!")
    (do
      (println "  ❌ Missing required variables:")
      (doseq [var (:missing (:required results))]
        (println "     -" var))))
  (println "")
  
  ;; Recommended variables
  (println "💡 RECOMMENDED VARIABLES:")
  (if (:all-present? (:recommended results))
    (println "  ✅ All recommended variables present!")
    (do
      (println "  ⚠️  Missing recommended variables:")
      (doseq [var (:missing (:recommended results))]
        (println "     -" var))))
  (println "")
  
  ;; Duplicates
  (println "🔍 DUPLICATE CHECK:")
  (if (:has-duplicates? (:duplicates results))
    (do
      (println "  ❌ Duplicate variable definitions found:")
      (doseq [[var count] (:duplicates (:duplicates results))]
        (println "     -" var "appears" count "times")))
    (println "  ✅ No duplicates found!"))
  (println "")
  
  ;; Secrets
  (println "🔐 SECRET DETECTION:")
  (if (:has-secrets? (:secrets results))
    (do
      (println "  ⚠️  POTENTIAL SECRETS DETECTED:")
      (println "  ⚠️  These should be in 1Password, not .env files!")
      (doseq [secret (:secrets (:secrets results))]
        (println "     -" (:var secret) "looks like a secret")))
    (println "  ✅ No obvious secrets detected!"))
  (println "")
  
  ;; Overall status
  (println "=" (apply str (repeat 78 "=")))
  (let [all-good? (and (:passed? (:required results))
                       (not (:has-duplicates? (:duplicates results)))
                       (not (:has-secrets? (:secrets results))))]
    (if all-good?
      (println "✨ VALIDATION PASSED ✨ The Lovers approve! 💕")
      (println "⚠️  VALIDATION WARNINGS - Please review above ⚠️")))
  (println ""))

;; ----------------------------------------------------------------------------
;; MAIN
;; ----------------------------------------------------------------------------

(defn -main
  [& args]
  (let [env-file (or (first args)
                     "personal/.env")
        env-vars (parse-env-file env-file)]
    
    (if env-vars
      (let [results {:required (check-required env-vars)
                     :recommended (check-recommended env-vars)
                     :secrets (check-secrets env-vars)
                     :duplicates (check-duplicates env-file)}]
        (print-report env-file results))
      (do
        (println "❌ Error: Could not read" env-file)
        (println "Make sure the file exists and is readable")
        (System/exit 1)))))

(apply -main *command-line-args*)

