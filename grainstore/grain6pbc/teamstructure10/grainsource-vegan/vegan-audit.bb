#!/usr/bin/env bb

;; ============================================================================
;; VEGAN CODE AUDIT TOOL
;; ============================================================================
;; teamstructure10 (Capricorn ♑ / X. The Wheel of Fortune)
;; "Audit with compassion. Measure with care. Grow with ethics."
;; ============================================================================

(ns vegan-audit
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [babashka.process :refer [shell]]))

;; ----------------------------------------------------------------------------
;; VIOLENT LANGUAGE PATTERNS
;; ----------------------------------------------------------------------------

(def violent-terms
  #{"kill" "killed" "killing" "killer"
    "murder" "murdered" "murdering"
    "slaughter" "slaughtered" "slaughtering"
    "butcher" "butchered" "butchering"
    "execute" "executed" "executing" "execution"
    "terminate" "terminated" "terminating" "termination"
    "destroy" "destroyed" "destroying" "destruction"
    "obliterate" "obliterated" "obliterating"})

(def oppressive-terms
  #{"master" "slave"
    "whitelist" "blacklist"
    "blackhat" "whitehat"})

(def animal-based-terms
  #{"cowboy" "monkey" "donkey"
    "chicken" "turkey" "fish"
    "dogfood" "dogfooding"})

;; ----------------------------------------------------------------------------
;; COMPASSIONATE ALTERNATIVES
;; ----------------------------------------------------------------------------

(def compassionate-replacements
  {"kill" "stop"
   "killed" "stopped"
   "killing" "stopping"
   "killer" "stopper"
   
   "murder" "halt"
   "slaughter" "harvest"
   "butcher" "gather"
   
   "execute" "run"
   "execution" "running"
   
   "terminate" "complete"
   "termination" "completion"
   
   "destroy" "remove"
   "destruction" "removal"
   
   "master" "primary"
   "slave" "replica"
   
   "whitelist" "allowlist"
   "blacklist" "blocklist"
   
   "cowboy" "exploratory"
   "monkey" "experimental"
   "dogfood" "self-use"
   "dogfooding" "self-testing"})

;; ----------------------------------------------------------------------------
;; FILE SCANNING
;; ----------------------------------------------------------------------------

(defn scan-file-for-terms
  "Scan a file for problematic terms"
  [file-path terms]
  (when (.exists (io/file file-path))
    (let [content (slurp file-path)
          lines (str/split-lines content)]
      (for [[line-num line] (map-indexed vector lines)
            term terms
            :when (str/includes? (str/lower-case line) term)]
        {:file file-path
         :line (inc line-num)
         :term term
         :context line
         :replacement (get compassionate-replacements term)}))))

(defn scan-directory
  "Recursively scan directory for problematic terms"
  [dir-path terms]
  (let [files (->> (file-seq (io/file dir-path))
                   (filter #(.isFile %))
                   (filter #(re-matches #".*\.(clj|bb|md|edn|txt)$" (.getName %))))]
    (mapcat #(scan-file-for-terms (.getPath %) terms) files)))

;; ----------------------------------------------------------------------------
;; CARBON FOOTPRINT ESTIMATION
;; ----------------------------------------------------------------------------

(defn estimate-file-carbon
  "Estimate carbon footprint of a code file based on complexity"
  [file-path]
  (when (.exists (io/file file-path))
    (let [content (slurp file-path)
          lines (count (str/split-lines content))
          chars (count content)
          
          ;; Rough estimates
          cpu-cycles-per-char 1000
          kwh-per-million-cycles 0.0001
          co2-per-kwh 0.475
          
          ;; Calculate
          total-cycles (* chars cpu-cycles-per-char)
          kwh-used (* (/ total-cycles 1000000) kwh-per-million-cycles)
          co2-grams (* kwh-used co2-per-kwh 1000)]
      
      {:file file-path
       :lines lines
       :chars chars
       :co2-grams co2-grams
       :trees-needed (/ co2-grams 22000)})))  ; One tree absorbs ~22kg CO2/year

(defn estimate-directory-carbon
  "Estimate carbon footprint of entire directory"
  [dir-path]
  (let [files (->> (file-seq (io/file dir-path))
                   (filter #(.isFile %))
                   (filter #(re-matches #".*\.(clj|bb|md|edn)$" (.getName %))))
        estimates (map #(estimate-file-carbon (.getPath %)) files)
        total-co2 (reduce + (map :co2-grams estimates))]
    
    {:files (count files)
     :total-co2-grams total-co2
     :total-co2-kg (/ total-co2 1000)
     :trees-to-offset (/ total-co2 22000)
     :estimates estimates}))

;; ----------------------------------------------------------------------------
;; TOROIDAL ECONOMICS CHECK
;; ----------------------------------------------------------------------------

(defn check-toroidal-economics
  "Check if project practices toroidal economics"
  [project-dir]
  {:has-readme? (.exists (io/file project-dir "README.md"))
   :has-license? (.exists (io/file project-dir "LICENSE"))
   :has-contributing? (.exists (io/file project-dir "CONTRIBUTING.md"))
   :has-code-of-conduct? (.exists (io/file project-dir "CODE_OF_CONDUCT.md"))
   :has-changelog? (.exists (io/file project-dir "CHANGELOG.md"))
   :is-open-source? (.exists (io/file project-dir ".git"))})

;; ----------------------------------------------------------------------------
;; AUDIT REPORT
;; ----------------------------------------------------------------------------

(defn print-audit-report
  "Print comprehensive vegan audit report"
  [project-dir]
  (println "")
  (println "🌱💚 VEGAN CODE AUDIT REPORT 💚🌱")
  (println "=" (apply str (repeat 78 "=")))
  (println "Project:" project-dir)
  (println "")
  
  ;; Violent language scan
  (println "🔍 VIOLENT LANGUAGE SCAN:")
  (let [violent-results (scan-directory project-dir violent-terms)]
    (if (empty? violent-results)
      (println "  ✅ No violent language found!")
      (do
        (println "  ⚠️  Violent language detected:")
        (doseq [{:keys [file line term replacement]} (take 10 violent-results)]
          (println (format "     %s:%d - '%s' → suggest '%s'" 
                          file line term (or replacement "TBD"))))
        (when (> (count violent-results) 10)
          (println (format "     ... and %d more instances" 
                          (- (count violent-results) 10)))))))
  (println "")
  
  ;; Oppressive terms scan
  (println "⚖️  OPPRESSIVE TERMS SCAN:")
  (let [oppressive-results (scan-directory project-dir oppressive-terms)]
    (if (empty? oppressive-results)
      (println "  ✅ No oppressive terms found!")
      (do
        (println "  ⚠️  Oppressive terms detected:")
        (doseq [{:keys [file line term replacement]} (take 10 oppressive-results)]
          (println (format "     %s:%d - '%s' → suggest '%s'" 
                          file line term (or replacement "TBD")))))))
  (println "")
  
  ;; Animal-based terms scan
  (println "🐄 ANIMAL-BASED TERMS SCAN:")
  (let [animal-results (scan-directory project-dir animal-based-terms)]
    (if (empty? animal-results)
      (println "  ✅ No animal-based terms found!")
      (do
        (println "  ⚠️  Animal-based terms detected:")
        (doseq [{:keys [file line term replacement]} (take 10 animal-results)]
          (println (format "     %s:%d - '%s' → suggest '%s'" 
                          file line term (or replacement "TBD")))))))
  (println "")
  
  ;; Carbon footprint
  (println "♻️  CARBON FOOTPRINT ESTIMATE:")
  (let [carbon (estimate-directory-carbon project-dir)]
    (println (format "  Files analyzed: %d" (:files carbon)))
    (println (format "  Estimated CO2: %.2f grams (%.4f kg)" 
                    (:total-co2-grams carbon) 
                    (:total-co2-kg carbon)))
    (println (format "  Trees to offset: %.4f trees/year" 
                    (:trees-to-offset carbon))))
  (println "")
  
  ;; Toroidal economics
  (println "🔄 TOROIDAL ECONOMICS CHECK:")
  (let [toroidal (check-toroidal-economics project-dir)]
    (println "  Documentation:")
    (println (str "    README: " (if (:has-readme? toroidal) "✅" "❌")))
    (println (str "    LICENSE: " (if (:has-license? toroidal) "✅" "❌")))
    (println (str "    CONTRIBUTING: " (if (:has-contributing? toroidal) "✅" "❌")))
    (println (str "    CODE_OF_CONDUCT: " (if (:has-code-of-conduct? toroidal) "✅" "❌")))
    (println (str "    CHANGELOG: " (if (:has-changelog? toroidal) "✅" "❌")))
    (println "  Infrastructure:")
    (println (str "    Open Source (Git): " (if (:is-open-source? toroidal) "✅" "❌"))))
  (println "")
  
  ;; Overall score
  (println "=" (apply str (repeat 78 "=")))
  (let [violent-count (count (scan-directory project-dir violent-terms))
        oppressive-count (count (scan-directory project-dir oppressive-terms))
        animal-count (count (scan-directory project-dir animal-based-terms))
        total-issues (+ violent-count oppressive-count animal-count)]
    (if (zero? total-issues)
      (println "✨ AUDIT PASSED ✨ Compassionate code! 💚")
      (println (format "⚠️  %d issues found - Review recommendations above" total-issues))))
  (println ""))

;; ----------------------------------------------------------------------------
;; MAIN
;; ----------------------------------------------------------------------------

(defn -main
  [& args]
  (let [project-dir (or (first args) ".")]
    (print-audit-report project-dir)))

(apply -main *command-line-args*)

