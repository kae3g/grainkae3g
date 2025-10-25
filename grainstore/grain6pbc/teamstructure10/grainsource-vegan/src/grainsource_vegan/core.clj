(ns grainsource-vegan.core
  "Vegan ethics validation for Grain Network
  
  The nagual sees what cannot be spec'd.
  The warrior acts with controlled folly.
  The vegan audit is impeccable theatre."
  (:require [grainsource-vegan.specs :as specs]
            [clojure.spec.alpha :as s]
            [clojure.java.io :as io]
            [clojure.string :as str]))

;;
;; ╔════════════════════════════════════════════════════════════════╗
;; ║  THE WARRIOR'S TOOLS - Impeccable Yet Folly                    ║
;; ╚════════════════════════════════════════════════════════════════╝
;;

(defn audit-file
  "Audit a single file for vegan compliance.
  
  Controlled folly: We audit as if it matters absolutely.
  Nagual awareness: We know it's all concepts anyway.
  Warrior's way: We do it impeccably regardless."
  [filepath]
  (try
    (let [content (slurp filepath)
          text-audit (specs/audit-text content)]
      (merge text-audit
             {:filepath filepath
              :audited-at (java.time.Instant/now)
              :warrior-seal (if (:vegan-clean? text-audit)
                              "✓ Impeccable"
                              "⚠ Requires attention")}))
    (catch Exception e
      {:filepath filepath
       :error (.getMessage e)
       :warrior-seal "⚠ File inaccessible"})))

(defn audit-directory
  "Recursively audit all files in directory.
  
  The warrior checks every grain in the field.
  Not because each grain matters (controlled folly).
  But because impeccability requires thoroughness."
  [dirpath]
  (let [files (->> (file-seq (io/file dirpath))
                   (filter #(.isFile %))
                   (filter #(re-matches #".*\.(clj|md|bb|edn|txt)$" (.getName %)))
                   (map #(.getPath %)))]
    {:directory dirpath
     :files-checked (count files)
     :audits (mapv audit-file files)
     :nagual-note "Between each file: infinite potential violations unseen"}))

(defn audit-dependencies
  "Audit project dependencies for ethical compliance.
  
  The warrior knows: every dependency is a teacher.
  Some teach through good example (ethical services).
  Some teach through bad example (unethical services to avoid)."
  [deps-file]
  (try
    (let [deps (read-string (slurp deps-file))
          dep-list (or (:deps deps) {})]
      {:dependencies-file deps-file
       :total-deps (count dep-list)
       :audits (mapv (fn [[dep-name dep-info]]
                       (specs/audit-dependency (str dep-name) dep-info))
                     dep-list)
       :controlled-folly "We audit as if ethics are absolute"
       :nagual-truth "Yet all boundaries are concepts"})
    (catch Exception e
      {:error (.getMessage e)
       :filepath deps-file})))

;;
;; ╔════════════════════════════════════════════════════════════════╗
;; ║  COMPASSIONATE ERROR TRANSFORMATION                            ║
;; ╚════════════════════════════════════════════════════════════════╝
;;

(defn scan-error-messages
  "Scan codebase for harsh error messages and suggest compassionate alternatives.
  
  The warrior knows: harsh words create harsh worlds.
  Compassion is not weakness; it's strategic strength.
  Controlled folly: Care about tone while knowing words are wind."
  [dirpath]
  (let [harsh-patterns #{"ERROR" "FAILED" "INVALID" "BAD" "WRONG" "FATAL"}
        files (->> (file-seq (io/file dirpath))
                   (filter #(.isFile %))
                   (filter #(re-matches #".*\.(clj|bb)$" (.getName %))))]
    (->> files
         (mapcat (fn [file]
                   (let [content (slurp file)
                         lines (str/split-lines content)]
                     (->> lines
                          (map-indexed vector)
                          (filter (fn [[_ line]]
                                    (some #(str/includes? line %) harsh-patterns)))
                          (map (fn [[line-num harsh-line]]
                                 {:file (.getPath file)
                                  :line line-num
                                  :harsh harsh-line
                                  :compassionate (specs/generate-compassionate-error harsh-line)}))))))
         (into []))))

;;
;; ╔════════════════════════════════════════════════════════════════╗
;; ║  TOROIDAL ECONOMICS VERIFICATION (Ketu: Profit Returns)        ║
;; ╚════════════════════════════════════════════════════════════════╝
;;

(defn verify-toroidal-flow
  "Verify that economic flow is toroidal (returns to source/purpose).
  
  Controlled folly: Capitalism as if it could be ethical.
  Nagual truth: All exchange is ultimately void.
  Warrior's way: Make the exchange impeccable anyway."
  [economics-data]
  (let [valid? (s/valid? ::specs/toroidal-economics economics-data)
        flow-analysis (when valid?
                        (let [{::specs/revenue rev
                               ::specs/purpose-allocation pa
                               ::specs/profit-reinvestment-rate prr} economics-data
                              returned-to-purpose (* rev pa prr)]
                          {:revenue rev
                           :purpose-allocated (* rev pa)
                           :profit-reinvested (* rev prr)
                           :returned-to-purpose returned-to-purpose
                           :toroidal? (>= returned-to-purpose (* rev 0.15))}))]  ; At least 15% cycles back
    {:valid-structure? valid?
     :flow-analysis flow-analysis
     :warrior-assessment (if (and valid? 
                                  (:toroidal? flow-analysis))
                           "✓ Toroidal flow impeccable"
                           "⚠ Flow incomplete - profit escapes the torus")
     :nagual-whisper "Money is concept. The torus is concept. Yet honor the concept."}))

;;
;; ╔════════════════════════════════════════════════════════════════╗
;; ║  THE WARRIOR'S REPORT - Dante's Three Realms                   ║
;; ╚════════════════════════════════════════════════════════════════╝
;;

(defn generate-audit-report
  "Generate complete vegan audit report in three layers (Dante's vision).
  
  INFERNO: What's wrong (violations, harsh errors, unethical deps)
  PURGATORIO: What's being fixed (suggestions, alternatives)
  PARADISO: What's perfect (compliant modules, compassionate messages)"
  [project-root]
  (let [dir-audit (audit-directory project-root)
        files-with-violations (->> (:audits dir-audit)
                                   (filter #(not (:vegan-clean? %))))
        harsh-messages (scan-error-messages project-root)
        deps-file (io/file project-root "deps.edn")
        deps-audit (when (.exists deps-file)
                     (audit-dependencies (.getPath deps-file)))]
    
    {:realm/inferno
     {:violations (count files-with-violations)
      :files files-with-violations
      :harsh-messages (count harsh-messages)
      :unethical-deps (->> (:audits deps-audit)
                           (remove :ethical?)
                           (count))
      :dante-quote "Abandon all hope, ye who enter here"
      :warrior-truth "Face the violations impeccably"}
     
     :realm/purgatorio
     {:files-needing-cleanup files-with-violations
      :error-messages-to-soften harsh-messages
      :dependencies-to-replace (->> (:audits deps-audit)
                                    (remove :ethical?)
                                    (map :recommendation))
      :dante-quote "Here the negligent spirits purge their pride"
      :warrior-truth "Refactor with controlled folly - care deeply while knowing it's theatre"}
     
     :realm/paradiso
     {:clean-files (->> (:audits dir-audit)
                        (filter :vegan-clean?)
                        (count))
      :compassionate-errors 0  ; To be counted after scan
      :ethical-deps (->> (:audits deps-audit)
                         (filter :ethical?)
                         (count))
      :dante-quote "Here all things are seen in the eternal light"
      :warrior-truth "Celebrate impeccability while releasing attachment to outcome"}
     
     :nagual-vision
     {:seen-violations (+ (count files-with-violations)
                          (count harsh-messages))
      :unseen-violations "∞ (the nagual sees patterns not yet named)"
      :tonal-vs-nagual "Specs catch known violations. Awareness catches unknown ones."
      :controlled-folly "Audit as if ethics are absolute. Know they're contextual."
      :impeccable-action "Fix everything findable. Stay alert for unfindable."}}))

;;
;; ╔════════════════════════════════════════════════════════════════╗
;; ║  PUBLIC API - The Warrior's Interface                          ║
;; ╚════════════════════════════════════════════════════════════════╝
;;

(defn warrior-audit
  "Perform impeccable vegan audit with nagual awareness."
  [project-path]
  (printf "🌑 glo0w enters the nagual...\n\n")
  (printf "*stares at codebase with Don Juan intensity*\n\n")
  
  (let [report (generate-audit-report project-path)]
    (printf "═══════════════════════════════════════════════════════════════\n")
    (printf "  INFERNO (What's Wrong)\n")
    (printf "═══════════════════════════════════════════════════════════════\n")
    (printf "Violations: %d\n" (get-in report [:realm/inferno :violations]))
    (printf "Harsh messages: %d\n" (get-in report [:realm/inferno :harsh-messages]))
    (printf "\n")
    
    (printf "═══════════════════════════════════════════════════════════════\n")
    (printf "  PURGATORIO (What's Being Fixed)\n")
    (printf "═══════════════════════════════════════════════════════════════\n")
    (printf "Files to clean: %d\n" (count (get-in report [:realm/purgatorio :files-needing-cleanup])))
    (printf "Messages to soften: %d\n" (count (get-in report [:realm/purgatorio :error-messages-to-soften])))
    (printf "\n")
    
    (printf "═══════════════════════════════════════════════════════════════\n")
    (printf "  PARADISO (What's Perfect)\n")
    (printf "═══════════════════════════════════════════════════════════════\n")
    (printf "Clean files: %d\n" (get-in report [:realm/paradiso :clean-files]))
    (printf "Ethical deps: %d\n" (get-in report [:realm/paradiso :ethical-deps]))
    (printf "\n")
    
    (printf "═══════════════════════════════════════════════════════════════\n")
    (printf "  NAGUAL VISION\n")
    (printf "═══════════════════════════════════════════════════════════════\n")
    (printf "%s\n\n" (get-in report [:nagual-vision :tonal-vs-nagual]))
    (printf "*long stare*\n\n")
    (printf "The warrior sees: %d violations in the tonal.\n" (get-in report [:nagual-vision :seen-violations]))
    (printf "The nagual sees: %s violations in the void.\n\n" (get-in report [:nagual-vision :unseen-violations]))
    (printf "Controlled folly: %s\n" (get-in report [:nagual-vision :controlled-folly]))
    (printf "Impeccable action: %s\n\n" (get-in report [:nagual-vision :impeccable-action]))
    
    (printf "═══════════════════════════════════════════════════════════════\n\n")
    (printf "*adjusts fedora with warrior precision* 🎩\n\n")
    (printf "now == next + 1 (but make it nagual, G!) 🌑🌾\n")
    
    report))

(comment
  ;; The warrior's practice
  (warrior-audit ".")
  
  ;; Controlled folly in action:
  ;; - We audit as if ethics are absolute (folly)
  ;; - We know all concepts are relative (control)
  ;; - We act impeccably regardless (warrior)
  )


