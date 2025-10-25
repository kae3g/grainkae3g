#!/usr/bin/env bb

(ns pseudo
  (:require [clojure.string :as str]
            [babashka.fs :as fs]))

(def pseudo-file "docs/PSEUDO.md")

(defn extract-section [content section-marker]
  "Extract content between a section marker and the next ## heading"
  (when-let [start-idx (str/index-of content section-marker)]
    (let [content-after (subs content (+ start-idx (count section-marker)))
          end-idx (or (str/index-of content-after "\n## ")
                      (count content-after))]
      (-> (subs content-after 0 end-idx)
          str/trim))))

(defn extract-list-items [text]
  "Extract bullet points or checkbox items from text"
  (when text
    (->> (str/split-lines text)
         (filter #(or (str/starts-with? (str/trim %) "- ")
                      (str/starts-with? (str/trim %) "* ")
                      (str/starts-with? (str/trim %) "- [ ]")
                      (str/starts-with? (str/trim %) "- [x]")))
         (take 10)))) ; Limit to first 10 items

(defn print-summary []
  (if-not (fs/exists? pseudo-file)
    (do
      (println "❌ PSEUDO.md not found at" pseudo-file)
      (System/exit 1))
    (let [content (slurp pseudo-file)
          
          ;; Extract key sections
          status-section (extract-section content "## 📍 **WHERE WE ARE NOW**")
          priorities-section (extract-section content "## 🔥 **IMMEDIATE PRIORITIES**")
          open-questions (extract-section content "### **Open Questions**")
          
          ;; Parse metadata from top
          lines (str/split-lines content)
          last-updated (some #(when (str/starts-with? % "**Last Updated**") %) lines)
          branch (some #(when (str/starts-with? % "**Branch**") %) lines)
          essays (some #(when (str/starts-with? % "**Essays**") %) lines)]
      
      (println "")
      (println "╔═══════════════════════════════════════════════════════════════╗")
      (println "║                    COLDRIVER TUNDRA PSEUDO                    ║")
      (println "║              (God's TODO is unknowable; this is ours)         ║")
      (println "╚═══════════════════════════════════════════════════════════════╝")
      (println "")
      
      ;; Metadata
      (when last-updated (println "📅" last-updated))
      (when branch (println "🌿" branch))
      (when essays (println "📝" essays))
      (println "")
      
      ;; Current Status
      (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
      (println "📍 WHERE WE ARE NOW")
      (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
      (when status-section
        (let [achievement-lines (extract-list-items status-section)]
          (doseq [line (take 8 achievement-lines)]
            (println "  " line))))
      (println "")
      
      ;; Immediate Priorities
      (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
      (println "🔥 IMMEDIATE PRIORITIES")
      (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
      (when priorities-section
        (let [priority-lines (extract-list-items priorities-section)]
          (doseq [line (take 10 priority-lines)]
            (println "  " line))))
      (println "")
      
      ;; Open Questions
      (when open-questions
        (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        (println "❓ OPEN QUESTIONS")
        (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        (let [question-lines (filter #(str/starts-with? (str/trim %) "- ❓") 
                                     (str/split-lines open-questions))]
          (doseq [line (take 5 question-lines)]
            (println "  " line)))
        (println ""))
      
      ;; Next Actions Hint
      (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
      (println "💡 HINT FOR CURSOR")
      (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
      (println "  This summary should guide your todo_write tool usage:")
      (println "  - Update your todos based on IMMEDIATE PRIORITIES")
      (println "  - Consider OPEN QUESTIONS when making decisions")
      (println "  - Check off completed items from WHERE WE ARE NOW")
      (println "  - Add new todos as work progresses")
      (println "")
      (println "  Full document: docs/PSEUDO.md")
      (println "  ('PSEUDO' = our approximation, not God's actual plan)")
      (println "")
      (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
      (println ""))))

;; Run
(print-summary)

