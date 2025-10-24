#!/usr/bin/env bb

(ns codeberg-workflow
  (:require [clojure.java.shell :as sh]
            [clojure.string :as str]))

(defn mechanical-commit [message]
  (println "🔧 Mechanical Commit Process...")
  (let [add-result (sh/sh "git" "add" ".")
        commit-result (sh/sh "git" "commit" "-m" message)
        push-result (sh/sh "git" "push" "origin" "main")]
    (if (and (zero? (:exit add-result))
             (zero? (:exit commit-result))
             (zero? (:exit push-result)))
      (println "✅ Mechanical Workflow: Complete")
      (println "❌ Mechanical Workflow: Failed"))))

(defn check-ci-status []
  (println "🔍 Checking CI Engine Status...")
  (let [result (sh/sh "git" "log" "--oneline" "-1")]
    (println "📋 Latest Commit:")
    (println (str/trim (:out result)))
    (println "🌐 Check CI Status: https://codeberg.org/yourusername/your-repo/actions")))

(defn mechanical-release [version]
  (println (str "🏭 Mechanical Release: " version))
  (let [tag-result (sh/sh "git" "tag" "-a" version "-m" (str "Release " version))
        push-tag-result (sh/sh "git" "push" "origin" version)]
    (if (and (zero? (:exit tag-result))
             (zero? (:exit push-tag-result)))
      (println "✅ Release: Deployed")
      (println "❌ Release: Failed"))))

(defn -main [& args]
  (case (first args)
    "commit" (mechanical-commit (str/join " " (rest args)))
    "status" (check-ci-status)
    "release" (mechanical-release (second args))
    (println "Usage: bb codeberg-workflow.bb [commit|status|release] <args>")))

(-main *command-line-args*)
