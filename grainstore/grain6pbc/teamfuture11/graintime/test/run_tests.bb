#!/usr/bin/env bb
(ns run-tests
  "Test runner for graintime module"
  (:require [clojure.test :refer [run-tests successful?]]))

(println "🧪 Running Graintime Tests...")
(println "")

;; Load test file
(load-file "test/graintime/core_test.clj")

;; Run tests
(let [results (run-tests 'graintime.core-test)]
  (println "")
  (if (successful? results)
    (do
      (println "✅ All tests passed!")
      (System/exit 0))
    (do
      (println "❌ Some tests failed")
      (System/exit 1))))
