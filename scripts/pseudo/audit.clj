#!/usr/bin/env bb

(ns pseudo.audit
  "PSEUDO/ASPIRATIONAL audit system - self-aware planning with formal verification"
  (:require [clojure.string :as str]
            [babashka.fs :as fs]
            [clojure.java.shell :refer [sh]]))

;; Configuration
(def pseudo-file "docs/PSEUDO.md")
(def aspirational-file "docs/TODO-ASPIRATIONAL.md")
(def report-file "docs/PSEUDO-REPORT.md")

;; Timestamp generation (ISO format with safe filename chars)
(defn timestamp []
  (let [now (java.time.LocalDateTime/now)]
    (.format now (java.time.format.DateTimeFormatter/ofPattern "yyyy-MM-dd--HHmmss"))))

;; Parse markdown for checkboxes and headings
(defn parse-checkboxes [content]
  (let [lines (str/split-lines content)
        completed (count (filter #(str/includes? % "- [x]") lines))
        pending (count (filter #(str/includes? % "- [ ]") lines))
        total (+ completed pending)]
    {:completed completed
     :pending pending
     :total total
     :percent (if (pos? total)
                (int (* 100 (/ completed total)))
                0)}))

(defn extract-headings [content level]
  (let [pattern (re-pattern (str "^" (apply str (repeat level "#")) " (.+)$"))
        lines (str/split-lines content)]
    (->> lines
         (keep #(when-let [match (re-find pattern %)]
                  (second match)))
         (take 10)))) ; Limit to first 10 to prevent loops

(defn find-oldest-unchecked [content limit]
  (let [lines (str/split-lines content)
        unchecked-lines (->> lines
                             (map-indexed vector)
                             (filter (fn [[_ line]]
                                       (str/starts-with? (str/trim line) "- [ ]")))
                             (take limit))]
    (map (fn [[idx line]]
           {:line (inc idx)
            :text (-> line
                      str/trim
                      (str/replace #"^- \[ \] " ""))})
         unchecked-lines)))

;; Compute "philosophical entropy" - rough distance between immediate and aspirational
(defn philosophical-entropy [pseudo-content aspirational-content]
  (let [pseudo-words (set (str/split (str/lower-case pseudo-content) #"\W+"))
        aspirational-words (set (str/split (str/lower-case aspirational-content) #"\W+"))
        intersection (clojure.set/intersection pseudo-words aspirational-words)
        union (clojure.set/union pseudo-words aspirational-words)
        jaccard (if (pos? (count union))
                  (double (/ (count intersection) (count union)))
                  0.0)]
    ;; Entropy = 1 - similarity (higher = more different)
    (- 1.0 jaccard)))

;; Get recent git commits
(defn recent-commits [n]
  (let [result (sh "git" "log" 
                   (str "-" n)
                   "--pretty=format:%h - %s (%ar)"
                   :dir ".")]
    (if (zero? (:exit result))
      (str/split-lines (:out result))
      [])))

;; Check for terminology consistency
(defn check-terminology [content]
  (let [genesis-count (count (re-seq #"(?i)\bgenesis\b" content))
        regenesis-count (count (re-seq #"(?i)\bregenesis\b" content))]
    {:genesis-count genesis-count
     :regenesis-count regenesis-count
     :consistent? (zero? genesis-count)}))

;; Generate the audit report
(defn generate-report []
  (if-not (and (fs/exists? pseudo-file) (fs/exists? aspirational-file))
    (do
      (println "❌ Error: PSEUDO.md or TODO-ASPIRATIONAL.md not found")
      (System/exit 1))
    
    (let [pseudo-content (slurp pseudo-file)
          aspirational-content (slurp aspirational-file)
          ts (timestamp)
          
          ;; Parse both files
          pseudo-stats (parse-checkboxes pseudo-content)
          aspirational-stats (parse-checkboxes aspirational-content)
          
          ;; Get headings
          pseudo-h2 (extract-headings pseudo-content 2)
          aspirational-h2 (extract-headings aspirational-content 2)
          
          ;; Find oldest unchecked
          pseudo-oldest (find-oldest-unchecked pseudo-content 5)
          aspirational-oldest (find-oldest-unchecked aspirational-content 5)
          
          ;; Compute entropy
          entropy (philosophical-entropy pseudo-content aspirational-content)
          
          ;; Check terminology
          terminology (check-terminology (str pseudo-content "\n" aspirational-content))
          
          ;; Recent commits
          commits (recent-commits 10)]
      
      ;; Build report
      (spit report-file
            (str
             "# PSEUDO/ASPIRATIONAL Audit Report\n\n"
             "**Generated**: " ts "\n"
             "**Auditor**: Babashka PSEUDO Audit System v1.0\n"
             "**Philosophy**: God's TODO is unknowable; this is our approximation\n\n"
             "---\n\n"
             
             ;; Section 1: Snapshot & Counts
             "## Section 1: Snapshot & Counts\n\n"
             "### PSEUDO.md (Immediate Reality)\n"
             "- **Total tasks**: " (:total pseudo-stats) "\n"
             "- **Completed**: " (:completed pseudo-stats) " (✅)\n"
             "- **Pending**: " (:pending pseudo-stats) " (☐)\n"
             "- **Progress**: " (:percent pseudo-stats) "%\n\n"
             
             "### TODO-ASPIRATIONAL.md (Infinite Vision)\n"
             "- **Total tasks**: " (:total aspirational-stats) "\n"
             "- **Completed**: " (:completed aspirational-stats) " (✅)\n"
             "- **Pending**: " (:pending aspirational-stats) " (☐)\n"
             "- **Progress**: " (:percent aspirational-stats) "%\n\n"
             
             "### Philosophical Entropy\n"
             "- **Distance**: " (format "%.3f" entropy) "\n"
             "- **Interpretation**: " 
             (cond
               (< entropy 0.3) "Low - immediate and aspirational are closely aligned"
               (< entropy 0.6) "Medium - healthy tension between present and future"
               :else "High - aspirational vision extends far beyond immediate work")
             "\n\n"
             
             "---\n\n"
             
             ;; Section 2: Oldest Unfinished (PSEUDO)
             "## Section 2: Oldest Unfinished (PSEUDO.md)\n\n"
             (if (seq pseudo-oldest)
               (str/join "\n"
                         (map-indexed
                          (fn [idx item]
                            (str (inc idx) ". **Line " (:line item) "**: " (:text item)))
                          pseudo-oldest))
               "*All immediate tasks completed or no unchecked items found.*")
             "\n\n"
             
             "---\n\n"
             
             ;; Section 3: Oldest Unfinished (ASPIRATIONAL)
             "## Section 3: Oldest Unfinished (TODO-ASPIRATIONAL.md)\n\n"
             (if (seq aspirational-oldest)
               (str/join "\n"
                         (map-indexed
                          (fn [idx item]
                            (str (inc idx) ". **Line " (:line item) "**: " (:text item)))
                          aspirational-oldest))
               "*All aspirational tasks completed or no unchecked items found.*")
             "\n\n"
             
             "---\n\n"
             
             ;; Section 4: Structural Overview
             "## Section 4: Structural Overview\n\n"
             "### PSEUDO.md Major Sections\n"
             (str/join "\n" (map #(str "- " %) pseudo-h2)) "\n\n"
             
             "### TODO-ASPIRATIONAL.md Major Sections\n"
             (str/join "\n" (map #(str "- " %) aspirational-h2)) "\n\n"
             
             "---\n\n"
             
             ;; Section 5: Verification Suggestions
             "## Section 5: Verification & Next Steps\n\n"
             
             "### Terminology Check\n"
             "- **'genesis' occurrences**: " (:genesis-count terminology) "\n"
             "- **'regenesis' occurrences**: " (:regenesis-count terminology) "\n"
             "- **Status**: "
             (if (:consistent? terminology)
               "✅ Consistent - using 'regenesis' terminology"
               "⚠️ Inconsistent - found lingering 'genesis' terms (should be 'regenesis')")
             "\n\n"
             
             "### Recent Activity (Last 10 Commits)\n"
             (if (seq commits)
               (str/join "\n" (map #(str "- " %) commits))
               "*No recent commits found.*")
             "\n\n"
             
             "### Suggested Next Actions\n"
             "1. Review oldest unfinished items in Section 2\n"
             "2. Update PSEUDO.md with recent achievements\n"
             "3. Check if any ASPIRATIONAL items are now ready to move to PSEUDO\n"
             "4. Run `bb pseudo:reflect` to auto-update from git log\n"
             (when-not (:consistent? terminology)
               "5. ⚠️ Migrate remaining 'genesis' → 'regenesis' for consistency\n")
             "\n"
             
             "---\n\n"
             
             "*This report generated by autonomous audit system. PSEUDO.md and TODO-ASPIRATIONAL.md remain the source of truth.*\n"
             "*'PSEUDO' = our approximation, not God's actual plan. The Tao that can be told is not the eternal Tao.* ❄️\n"))
      
      (println "")
      (println "✅ PSEUDO Audit Complete!")
      (println "")
      (println "📊 Summary:")
      (println "   PSEUDO.md:        " (:completed pseudo-stats) "/" (:total pseudo-stats) 
               " (" (:percent pseudo-stats) "%)")
      (println "   ASPIRATIONAL.md:  " (:completed aspirational-stats) "/" (:total aspirational-stats)
               " (" (:percent aspirational-stats) "%)")
      (println "   Entropy:          " (format "%.3f" entropy))
      (println "   Terminology:      " (if (:consistent? terminology) "✅" "⚠️"))
      (println "")
      (println "📄 Report written to:" report-file)
      (println ""))))

;; Main entry point
(defn -main [& args]
  (let [opts (set args)]
    (cond
      (contains? opts "--help")
      (do
        (println "PSEUDO Audit System v1.0")
        (println "")
        (println "Usage:")
        (println "  bb pseudo:audit          Generate audit and display summary")
        (println "  bb pseudo:weekly         Generate weekly report")
        (println "  bb pseudo:open           Display current report")
        (println "  bb pseudo:reflect        Auto-update PSEUDO.md from git log")
        (println ""))
      
      :else
      (generate-report))))

(-main)

