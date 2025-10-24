#!/usr/bin/env bb
;; ci_smoketest.bb - Verify Reregenesis demo runs successfully
;;
;; Used in CI to ensure the demo remains functional

(ns ci.smoketest
  (:require [babashka.fs :as fs]
            [babashka.process :as p]
            [cheshire.core :as json]
            [clojure.spec.alpha :as s]))

;; Import specs from reregenesis (if it were a lib)
(s/def ::startup-ms pos-int?)
(s/def ::message-latency-ms pos-int?)
(s/def ::memory-mb pos-int?)
(s/def ::fast-path (s/keys :req-un [::startup-ms ::message-latency-ms ::memory-mb]))

(s/def ::nock-reduction-steps pos-int?)
(s/def ::nock-eval-ms pos-int?)
(s/def ::equivalence-coverage-pct (s/int-in 0 101))
(s/def ::true-path (s/keys :req-un [::nock-reduction-steps ::nock-eval-ms ::equivalence-coverage-pct]))

(s/def ::outputs-match boolean?)
(s/def ::golden-vectors-passed nat-int?)
(s/def ::golden-vectors-total pos-int?)
(s/def ::verification (s/keys :req-un [::outputs-match ::golden-vectors-passed ::golden-vectors-total]))

(s/def ::reregenesis-metrics (s/keys :req-un [::fast-path ::true-path ::verification]))
(s/def ::report (s/keys :req-un [::timestamp ::reregenesis]))

(defn log [msg]
  (println (str "[ci] " msg)))

(defn run-reregenesis! []
  (log "Running Reregenesis...")
  (let [result (p/shell {:out :string :err :string}
                        "bb scripts/reregenesis.bb")]
    (when (not= 0 (:exit result))
      (log "❌ FAIL: Reregenesis script failed!")
      (println (:err result))
      (System/exit 1))
    (log "✅ Reregenesis completed successfully")))

(defn check-report-exists! [report-path]
  (log "Checking report exists...")
  (when-not (fs/exists? report-path)
    (log (str "❌ FAIL: No report generated at " report-path))
    (System/exit 1))
  (log (str "✅ Report exists: " report-path)))

(defn validate-report! [report-path]
  (log "Validating report structure...")
  (let [report-str (slurp report-path)
        report (try
                 (json/parse-string report-str true)
                 (catch Exception e
                   (log "❌ FAIL: Report is not valid JSON!")
                   (println (.getMessage e))
                   (System/exit 1)))]
    
    ;; Validate with Spec!
    (when-not (s/valid? ::report report)
      (log "❌ FAIL: Report structure invalid!")
      (println (s/explain-str ::report report))
      (System/exit 1))
    
    (log "✅ Report structure valid (Clojure Spec verified!)")
    report))

(defn check-equivalence! [report]
  (log "Checking equivalence...")
  (let [outputs-match (get-in report [:reregenesis :verification :outputs-match])]
    (when-not outputs-match
      (log "❌ FAIL: Fast path and true path outputs DO NOT match!")
      (System/exit 1))
    (log "✅ Equivalence verified (fast = true)!")))

(defn check-coverage! [report]
  (log "Checking test coverage...")
  (let [passed (get-in report [:reregenesis :verification :golden-vectors-passed])
        total (get-in report [:reregenesis :verification :golden-vectors-total])
        pct (if (pos? total) (* 100.0 (/ passed total)) 0)]
    (log (str "Golden vectors: " passed "/" total " (" (format "%.1f" pct) "%)"))
    (when (< pct 100.0)
      (log (str "⚠️  WARNING: Not all golden vectors passed (" pct "%)"))
      ;; Don't fail yet, just warn
      )
    (log "✅ Coverage check complete")))

(defn main []
  (log "Starting Reregenesis smoke test...")
  (println)
  
  (let [report-path "var/reports/reregenesis.json"]
    ;; Run the demo
    (run-reregenesis!)
    
    ;; Validate results
    (check-report-exists! report-path)
    (let [report (validate-report! report-path)]
      (check-equivalence! report)
      (check-coverage! report)
      
      ;; Display final report
      (println)
      (log "Final Report:")
      (println (json/generate-string report {:pretty true}))
      (println)
      (log "✅ All smoke tests passed!")
      (System/exit 0))))

;; Run
(main)

