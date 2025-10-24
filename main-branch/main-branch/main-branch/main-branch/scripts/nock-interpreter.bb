#!/usr/bin/env bb

;; Nock Interpreter in Babashka
;; Implements the 12 Nock reduction rules
;; For SixOS + seL4 + RISC-V integration

(ns nock-interpreter
  (:require [clojure.string :as str]))

;; Nock data types
(defn atom? [n] (integer? n))
(defn cell? [n] (and (vector? n) (= 2 (count n))))

;; Tree addressing (rules 3-7)
(defn tree-address [address noun]
  "Tree addressing by slot number"
  (cond
    (= address 1) noun
    (= address 2) (first noun)
    (= address 3) (second noun)
    (even? address) (tree-address 2 (tree-address (/ address 2) noun))
    (odd? address) (tree-address 3 (tree-address (/ (dec address) 2) noun))
    :else (throw (ex-info "Invalid tree address" {:address address}))))

;; Cell test (rule 1)
(defn cell-test [noun]
  (if (cell? noun) 0 1))

;; Increment (rule 2)
(defn increment [noun]
  (if (atom? noun)
    (inc noun)
    (throw (ex-info "Cannot increment cell" {:noun noun}))))

;; Equality (rule 3)
(defn equality [a b]
  (if (= a b) 0 1))

;; Nock evaluation (rules 8-19)
(defn nock [subject formula]
  "Main Nock evaluation function"
  (cond
    ;; Rule 8: *[a [b c] d] → [*[a b c] *[a d]]
    (and (cell? formula) (cell? (first formula)))
    [(nock subject (first formula)) (nock subject (second formula))]
    
    ;; Rule 9: *[a 0 b] → /[b a] (slot addressing)
    (and (atom? (first formula)) (= 0 (first formula)))
    (tree-address (second formula) subject)
    
    ;; Rule 10: *[a 1 b] → b (constant)
    (and (atom? (first formula)) (= 1 (first formula)))
    (second formula)
    
    ;; Rule 11: *[a 2 b c] → *[*[a b] *[a c]] (nock)
    (and (atom? (first formula)) (= 2 (first formula)))
    (nock (nock subject (second formula)) (nock subject (nth formula 2)))
    
    ;; Rule 12: *[a 3 b] → ?*[a b] (cell test)
    (and (atom? (first formula)) (= 3 (first formula)))
    (cell-test (nock subject (second formula)))
    
    ;; Rule 13: *[a 4 b] → +*[a b] (increment)
    (and (atom? (first formula)) (= 4 (first formula)))
    (increment (nock subject (second formula)))
    
    ;; Rule 14: *[a 5 b c] → =*[a b] *[a c] (equality)
    (and (atom? (first formula)) (= 5 (first formula)))
    (equality (nock subject (second formula)) (nock subject (nth formula 2)))
    
    ;; Rule 15: *[a 6 b c d] → *[a *[[c d] 0 *[[2 3] 0 *[a 4 4 b]]]] (if)
    (and (atom? (first formula)) (= 6 (first formula)))
    (let [b (second formula)
          c (nth formula 2)
          d (nth formula 3)
          test-result (nock subject b)
          branch (if (= 0 test-result) c d)]
      (nock subject branch))
    
    ;; Rule 16: *[a 7 b c] → *[*[a b] c] (compose)
    (and (atom? (first formula)) (= 7 (first formula)))
    (nock (nock subject (second formula)) (nth formula 2))
    
    ;; Rule 17: *[a 8 b c] → *[[*[a b] a] c] (extend subject)
    (and (atom? (first formula)) (= 8 (first formula)))
    (let [new-subject [(nock subject (second formula)) subject]]
      (nock new-subject (nth formula 2)))
    
    ;; Rule 18: *[a 9 b c] → *[*[a c] 2 [0 1] 0 b] (call)
    (and (atom? (first formula)) (= 9 (first formula)))
    (let [core (nock subject (nth formula 2))
          formula-to-call [2 [0 1] 0 (second formula)]]
      (nock core formula-to-call))
    
    ;; Rule 19: *[a 10 [b c] d] → #[b *[a c] *[a d]] (hint)
    (and (atom? (first formula)) (= 10 (first formula)))
    (nock subject (nth formula 2)) ; Ignore hint for now
    
    ;; Rule 20: *[a 11 [b c] d] → *[[*[a c] *[a d]] 0 3] (static hint)
    (and (atom? (first formula)) (= 11 (first formula)) (cell? (second formula)))
    (nock subject (nth formula 2)) ; Ignore hint for now
    
    ;; Rule 21: *[a 11 b c] → *[a c] (dynamic hint)
    (and (atom? (first formula)) (= 11 (first formula)))
    (nock subject (nth formula 2)) ; Ignore hint for now
    
    :else
    (throw (ex-info "Invalid Nock formula" {:formula formula}))))

;; REPL for interactive Nock evaluation
(defn nock-repl []
  "Interactive Nock REPL"
  (println "Nock Interpreter - SixOS + seL4 + RISC-V Integration")
  (println "Type 'quit' to exit")
  (println)
  
  (loop []
    (print "nock> ")
    (flush)
    (let [input (read-line)]
      (when-not (= "quit" input)
        (try
          (let [form (read-string input)
                result (nock (first form) (second form))]
            (println "=>" result))
          (catch Exception e
            (println "Error:" (.getMessage e))))
        (recur)))))

;; Test cases
(defn run-tests []
  "Run basic Nock tests"
  (println "Running Nock tests...")
  
  ;; Test 1: Tree addressing
  (let [subject [[4 5] [6 [14 15]]]]
    (assert (= (tree-address 1 subject) subject))
    (assert (= (tree-address 2 subject) [4 5]))
    (assert (= (tree-address 6 subject) 6))
    (println "✓ Tree addressing tests passed"))
  
  ;; Test 2: Basic Nock evaluation
  (assert (= (nock [42 17] [0 2]) 42))
  (assert (= (nock [42 17] [0 3]) 17))
  (assert (= (nock 100 [1 999]) 999))
  (println "✓ Basic Nock evaluation tests passed")
  
  (println "All tests passed!"))

;; Main entry point
(defn -main [& args]
  (case (first args)
    "repl" (nock-repl)
    "test" (run-tests)
    "help" (do
             (println "Nock Interpreter for SixOS + seL4 + RISC-V")
             (println "Usage:")
             (println "  bb nock-interpreter.bb repl  - Start interactive REPL")
             (println "  bb nock-interpreter.bb test  - Run test suite")
             (println "  bb nock-interpreter.bb help  - Show this help"))
    (do
      (println "Invalid command. Use 'help' for usage information.")
      (System/exit 1))))

;; Export functions for use in other scripts
{:nock nock
 :tree-address tree-address
 :cell-test cell-test
 :increment increment
 :equality equality}
