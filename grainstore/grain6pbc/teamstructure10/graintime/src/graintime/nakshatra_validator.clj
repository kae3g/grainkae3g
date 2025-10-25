(ns graintime.nakshatra-validator
  "Validate nakshatra calculations using sequential order checks
  
  Key insight: Nakshatras go in order (0→26→0), never skip!
  
  If we know:
    - Transition A: 25/10/2025 07:51 IST → Jyeshtha (17)
    - Transition B: 26/10/2025 10:46 IST → Mula (18)
  
  Then for ANY time between A and B:
    - MUST be Jyeshtha (17)
    - CANNOT be anything else
  
  This allows us to:
  1. Validate Swiss Ephemeris against AstrOccult data
  2. Catch calculation errors probabilistically
  3. Ensure sequential order (no skips)")
  (:import [java.time ZonedDateTime LocalDateTime ZoneId]))

;; =============================================================================
;; NAKSHATRA SEQUENCE
;; =============================================================================

(def nakshatra-sequence
  "Nakshatra indices in order (0-26, then wraps to 0)"
  (vec (range 27)))

(def nakshatra-names
  "Map of index to name"
  {0 "Ashwini" 1 "Bharani" 2 "Krittika" 3 "Rohini" 4 "Mrigashira" 5 "Ardra"
   6 "Punarvasu" 7 "Pushya" 8 "Ashlesha" 9 "Magha" 10 "Purva Phalguni" 11 "Uttara Phalguni"
   12 "Hasta" 13 "Chitra" 14 "Swati" 15 "Vishakha" 16 "Anuradha" 17 "Jyeshtha"
   18 "Mula" 19 "Purva Ashadha" 20 "Uttara Ashadha" 21 "Shravana" 22 "Dhanishta"
   23 "Shatabhisha" 24 "Purva Bhadrapada" 25 "Uttara Bhadrapada" 26 "Revati"})

(defn name-to-index
  "Convert nakshatra name to index"
  [nak-name]
  (first (keep (fn [[idx name]] (when (= name nak-name) idx)) nakshatra-names)))

(defn next-nakshatra
  "Get the next nakshatra in sequence (wraps at 26→0)"
  [current-index]
  (mod (inc current-index) 27))

(defn prev-nakshatra
  "Get the previous nakshatra in sequence (wraps at 0→26)"
  [current-index]
  (mod (dec current-index) 27))

(defn is-valid-sequence?
  "Check if nakshatra B follows nakshatra A in valid sequence
  
  Valid: A=17 (Jyeshtha), B=18 (Mula) → true
  Invalid: A=17 (Jyeshtha), B=12 (Hasta) → false (skipped!)"
  [index-a index-b]
  (or (= index-b (next-nakshatra index-a))
      (= index-a index-b))) ; Same nakshatra is ok (transition window)

;; =============================================================================
;; VALIDATION AGAINST ASTROCCULT DATA
;; =============================================================================

(defn validate-against-astroccult
  "Validate a calculated nakshatra against AstrOccult data
  
  Algorithm:
  1. Get AstrOccult nakshatra for same time
  2. Compare results
  3. Return {:valid true/false :astroccult \"...\" :calculated \"...\"}"
  [datetime calculated-nakshatra]
  (try
    (require 'graintime.astroccult-parser)
    (let [get-nak (resolve 'graintime.astroccult-parser/get-nakshatra-at-time)
          astroccult-nak (get-nak datetime)
          matches? (= astroccult-nak calculated-nakshatra)]
      
      {:valid matches?
       :datetime datetime
       :astroccult-nakshatra astroccult-nak
       :calculated-nakshatra calculated-nakshatra
       :match? matches?})
    
    (catch Exception e
      {:valid false
       :error (.getMessage e)
       :note "Could not validate - AstrOccult data may not cover this date"})))

;; =============================================================================
;; TEST FUNCTIONS
;; =============================================================================

(defn test-sequence-validation
  "Test that nakshatras follow proper sequence"
  []
  (println "\n🔍 Testing Nakshatra Sequence Validation:\n")
  
  ;; Valid sequence
  (let [result (is-valid-sequence? 17 18)]
    (println "   Jyeshtha(17) → Mula(18):" result "✓"))
  
  ;; Invalid sequence (skip)
  (let [result (is-valid-sequence? 17 12)]
    (println "   Jyeshtha(17) → Hasta(12):" result "(skipped - invalid!)"))
  
  ;; Wrap-around
  (let [result (is-valid-sequence? 26 0)]
    (println "   Revati(26) → Ashwini(0):" result "✓ (wrap-around)"))
  
  ;; Same nakshatra
  (let [result (is-valid-sequence? 17 17)]
    (println "   Jyeshtha(17) → Jyeshtha(17):" result "✓ (within transition window)")))

(defn test-astroccult-validation
  "Test validation against AstrOccult data
  
  Test case: Oct 25, 2025, 08:00 IST → should be Jyeshtha"
  []
  (println "\n🧪 Testing Against AstrOccult Data:\n")
  
  (let [test-dt (LocalDateTime/of 2025 10 25 8 0 0)
        ;; Simulate a correct calculation
        result-correct (validate-against-astroccult test-dt "Jyeshtha")
        ;; Simulate an incorrect calculation
        result-wrong (validate-against-astroccult test-dt "Hasta")]
    
    (println "   Test time: 2025-10-25 08:00 IST")
    (println "   AstrOccult says:" (:astroccult-nakshatra result-correct))
    (println)
    (println "   Correct calc (Jyeshtha):")
    (println "     Valid?" (:valid result-correct) (if (:valid result-correct) "✅" "❌"))
    (println)
    (println "   Wrong calc (Hasta):")
    (println "     Valid?" (:valid result-wrong) (if (:valid result-wrong) "✅" "❌"))))

(comment
  ;; Run tests
  (test-sequence-validation)
  (test-astroccult-validation)
  )

