#!/usr/bin/env bb

(ns codeberg-setup
  (:require [clojure.java.shell :as sh]
            [clojure.string :as str]))

(defn check-prerequisites []
  (println "🔧 Checking Mechanical Prerequisites...")
  (let [git-status (sh/sh "git" "--version")
        ssh-status (sh/sh "ssh" "-V")]
    (if (zero? (:exit git-status))
      (println "✅ Git Engine: Ready")
      (println "❌ Git Engine: Not Found"))
    (if (zero? (:exit ssh-status))
      (println "✅ SSH Mechanism: Ready")
      (println "❌ SSH Mechanism: Not Found"))))

(defn generate-ssh-key [email]
  (println "🔑 Generating SSH Key for Codeberg...")
  (let [key-file (str (System/getProperty "user.home") "/.ssh/codeberg")
        result (sh/sh "ssh-keygen" "-t" "ed25519" "-C" email "-f" key-file "-N" "")]
    (if (zero? (:exit result))
      (do
        (println "✅ SSH Key Generated")
        (println "📋 Public Key:")
        (println (str/trim (:out (sh/sh "cat" (str key-file ".pub"))))))
      (println "❌ SSH Key Generation Failed"))))

(defn test-connection []
  (println "🔌 Testing Mechanical Connection...")
  (let [result (sh/sh "ssh" "-T" "git@codeberg.org")]
    (if (str/includes? (:out result) "successfully authenticated")
      (println "✅ Connection: Operational")
      (println "❌ Connection: Failed"))))

(defn -main [& args]
  (case (first args)
    "check" (check-prerequisites)
    "keygen" (generate-ssh-key (second args))
    "test" (test-connection)
    (println "Usage: bb codeberg-setup.bb [check|keygen|test] <email>")))

(-main *command-line-args*)
