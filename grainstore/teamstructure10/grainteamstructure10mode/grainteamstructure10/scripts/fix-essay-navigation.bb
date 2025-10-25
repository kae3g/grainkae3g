#!/usr/bin/env bb
;; fix-essay-navigation.bb - Fix footer navigation for essays 9507-9603
;;
;; This script ensures all essays have correct prev/next navigation

(ns fix-essay-navigation
  (:require [babashka.fs :as fs]
            [clojure.string :as str]))

(def essay-sequence
  "Ordered list of essays with navigation"
  [;; Phase 1 essays (9500-9513)
   {:num 9500 :slug "what-is-a-computer"}
   {:num 9501 :slug "what-is-compute"}
   {:num 9502 :slug "ode-to-nocturnal-time"}
   {:num 9503 :slug "what-is-nock"}
   {:num 9504 :slug "what-is-clojure"}
   {:num 9505 :slug "house-of-wisdom-knowledge-gardens"}
   {:num 9506 :slug "arabic-american-ai-self-hosted"}
   {:num 9507 :slug "helen-atthowe-ecological-systems"}
   {:num 9510 :slug "unix-philosophy-primer"}
   {:num 9511 :slug "kubernetes-cloud-orchestration"}
   {:num 9512 :slug "unix-philosophy-deep-dive"}
   {:num 9513 :slug "personal-sovereignty-framework-stack"}
   
   ;; Optional deep dives
   {:num 9514 :slug "sixos-framework-16-installation" :optional true}
   {:num 9515 :slug "regenesis-demo-make-it-tactile" :optional true}
   {:num 9516 :slug "complete-stack-in-action" :optional true}
   
   ;; Continue Phase 1
   {:num 9520 :slug "functional-programming-basics"}
   {:num 9530 :slug "rich-hickey-simple-made-easy"}
   {:num 9540 :slug "types-sets-mathematical-foundations"}
   {:num 9550 :slug "command-line-your-primary-interface"}
   {:num 9560 :slug "text-files-universal-format"}
   {:num 9570 :slug "processes-programs-in-motion"}
   {:num 9580 :slug "memory-management"}
   {:num 9591 :slug "filesystem-hierarchical-organization"}
   {:num 9592 :slug "permissions-who-can-do-what"}
   {:num 9593 :slug "networking-basics-sockets-protocols"}
   {:num 9594 :slug "concurrency-threads-parallelism"}
   {:num 9595 :slug "build-systems-source-to-binary"}
   {:num 9596 :slug "package-managers-dependency-resolution"}
   {:num 9597 :slug "version-control-git-foundations"}
   {:num 9598 :slug "testing-verification-validation"}
   {:num 9599 :slug "documentation-writing-for-humans"}
   {:num 9600 :slug "debugging-finding-fixing-issues"}
   {:num 9601 :slug "phase-1-synthesis-foundations-laid"}
   {:num 9602 :slug "shell-scripting-bash-fundamentals"}
   {:num 9603 :slug "shell-text-processing-grep-sed-awk"}])

(defn find-essay-file [num]
  "Find the markdown file for an essay number"
  (let [pattern (str num "-*.md")
        files (fs/glob "writings" pattern)]
    (first files)))

(defn get-navigation [idx]
  "Get prev/next navigation for essay at index"
  (let [current (nth essay-sequence idx)
        prev (when (> idx 0) (nth essay-sequence (dec idx)))
        next (when (< idx (dec (count essay-sequence))) 
               (nth essay-sequence (inc idx)))]
    {:current current
     :prev prev
     :next next}))

(defn build-navigation-line [nav]
  "Build the navigation line for footer"
  (let [{:keys [prev current next]} nav
        prev-link (if prev
                    (format "[%d (%s)](/12025-10/%d-%s)" 
                            (:num prev) 
                            (str/replace (:slug prev) #"-" " ")
                            (:num prev)
                            (:slug prev))
                    "Start")
        next-link (if next
                    (format "[%d (%s)](/12025-10/%d-%s)"
                            (:num next)
                            (str/replace (:slug next) #"-" " ")
                            (:num next)
                            (:slug next))
                    "End")]
    (format "← Previous: %s | **Phase 1 Index** | Next: %s" prev-link next-link)))

(defn update-essay-navigation [file-path idx]
  "Update navigation in essay file"
  (let [content (slurp (str file-path))
        nav (get-navigation idx)
        nav-line (build-navigation-line nav)
        ;; Find and replace navigation line
        nav-pattern #"← Previous:.*\| \*\*Phase 1 Index\*\* \| Next:.*"
        updated (str/replace content nav-pattern nav-line)]
    (when (not= content updated)
      (spit (str file-path) updated)
      (println (format "✓ Updated navigation for %d" (:num (:current nav)))))))

(defn check-all-navigation []
  "Check and fix navigation for all essays"
  (println "🧭 Checking essay navigation (9507-9603)...")
  (doseq [[idx essay] (map-indexed vector essay-sequence)]
    (when-let [file (find-essay-file (:num essay))]
      (update-essay-navigation file idx)))
  (println "✅ Navigation check complete!"))

;; Run if called directly
(when (= *file* (System/getProperty "babashka.file"))
  (check-all-navigation))

