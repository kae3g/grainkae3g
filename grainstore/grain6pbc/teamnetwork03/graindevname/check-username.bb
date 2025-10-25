#!/usr/bin/env bb
(ns check-username
  "Check username availability across platforms"
  (:require [babashka.http-client :as http]
            [clojure.string :as str]))

(defn check-github-username
  "Check if username is available on GitHub"
  [username]
  (try
    (let [response (http/get (str "https://api.github.com/users/" username)
                             {:headers {"User-Agent" "GrainDevName-Checker"}})
          status (:status response)]
      (if (= status 404)
        {:available true :platform "GitHub"}
        {:available false :platform "GitHub" :reason "Username exists"}))
    (catch Exception e
      {:available false :platform "GitHub" :reason (.getMessage e)})))

(defn check-codeberg-username
  "Check if username is available on Codeberg"
  [username]
  (try
    (let [response (http/get (str "https://codeberg.org/api/v1/users/" username)
                             {:headers {"User-Agent" "GrainDevName-Checker"}})
          status (:status response)]
      (if (= status 404)
        {:available true :platform "Codeberg"}
        {:available false :platform "Codeberg" :reason "Username exists"}))
    (catch Exception e
      {:available false :platform "Codeberg" :reason (.getMessage e)})))

(defn check-username
  "Check username availability across all platforms"
  [username]
  (println (str "🌾 Checking username: " username))
  (println (str "=" (apply str (repeat (+ (count username) 20) "="))))
  
  (let [github-result (check-github-username username)
        codeberg-result (check-codeberg-username username)
        results [github-result codeberg-result]
        available-count (count (filter :available results))]
    
    (doseq [result results]
      (if (:available result)
        (println (str "✅ " (:platform result) ": Available"))
        (println (str "❌ " (:platform result) ": " (:reason result)))))
    
    (println "")
    (if (= available-count (count results))
      (println (str "🎉 " username " is available on all platforms!"))
      (println (str "⚠️  " username " has " (- (count results) available-count) " conflicts")))
    
    {:username username
     :total-platforms (count results)
     :available-platforms available-count
     :results results}))

(defn generate-username-suggestions
  "Generate username suggestions following the convention"
  []
  (let [syllables ["kae" "jen" "mik" "sop" "ale" "sam" "tom" "ann" "bob" "cat"
                   "dev" "cod" "git" "sys" "art" "des" "mus" "vid" "biz" "mgm"]
        numbers (range 0 10)
        suffixes ["3g" "2x" "5a" "7k" "4n" "1m" "8t" "6z" "9p" "0y"]]
    (for [syllable syllables
          number numbers
          suffix suffixes]
      (str syllable number suffix))))

(defn main
  "Main function for username checking"
  []
  (let [args *command-line-args*
        command (first args)]
    (case command
      "check" (let [username (second args)]
                (if username
                  (check-username username)
                  (println "Usage: bb check-username.bb check <username>")))
      
      "suggest" (let [count (or (some-> (second args) Integer/parseInt) 10)]
                  (println "🌾 Username suggestions (following 5-char convention):")
                  (println "=" 50)
                  (doseq [username (take count (generate-username-suggestions))]
                    (println (str "• " username))))
      
      "help" (do
               (println "🌾 GrainDevName Username Checker")
               (println "")
               (println "USAGE:")
               (println "  bb check-username.bb check <username>    Check username availability")
               (println "  bb check-username.bb suggest [count]     Generate suggestions")
               (println "  bb check-username.bb help                Show this help")
               (println "")
               (println "EXAMPLES:")
               (println "  bb check-username.bb check kae3g")
               (println "  bb check-username.bb suggest 20"))
      
      (do
        (println "🌾 GrainDevName Username Checker")
        (println "Use 'bb check-username.bb help' for usage information")))))

(main)
