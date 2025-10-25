(ns graintime.nakshatra-76char-test
  "Test that ALL 27 nakshatras produce EXACTLY 76-char graintimes for teamstructure10"
  (:require [clojure.test :refer [deftest testing is]]
            [clojure.string :as str]))

;; All 27 nakshatras with abbreviated forms (as used in graintime)
(def nakshatras-abbreviated
  ["ashwini"
   "bharani"
   "krittika"
   "rohini"
   "mrigashira"
   "ardra"
   "punarvasu"
   "pushya"
   "ashlesha"
   "magha"
   "p_phalguni"      ; Purva Phalguni
   "u_phalguni"      ; Uttara Phalguni
   "hasta"
   "chitra"
   "swati"
   "vishakha"
   "anuradha"
   "jyeshtha"
   "mula"
   "p_ashadha"       ; Purva Ashadha
   "u_ashadha"       ; Uttara Ashadha
   "shravana"
   "dhanishta"
   "shatabhisha"
   "p_bhadrapada"    ; Purva Bhadrapada
   "u_bhadrapada"    ; Uttara Bhadrapada (LONGEST at 12 chars!)
   "revati"])

(defn pad-nakshatra
  "Pad nakshatra name to exactly 12 chars with trailing dashes"
  [nakshatra-name]
  (let [padded (format "%-12s" nakshatra-name)]
    (str/replace padded #" " "-")))

(defn generate-test-graintime
  "Generate 76-char graintime for testing
   Format: 12025-10-25--{time}-{tz}-moon-{nakshatra-12}-asc-{sign}{deg}--sun-{house}--teamstructure10"
  [nakshatra]
  (let [padded-nakshatra (pad-nakshatra nakshatra)]
    (format "12025-10-25--0051-PDT-moon-%s-asc-pisc29--sun-05h--teamstructure10"
            padded-nakshatra)))

(deftest test-all-nakshatras-76-chars
  (testing "All 27 nakshatras produce exactly 76-char graintimes"
    (doseq [nakshatra nakshatras-abbreviated]
      (let [graintime (generate-test-graintime nakshatra)
            length (count graintime)]
        (is (= 76 length)
            (format "Nakshatra '%s' produced %d chars (expected 76): %s"
                   nakshatra length graintime))))))

(deftest test-nakshatra-padding
  (testing "Nakshatra padding produces exactly 12 chars"
    (doseq [nakshatra nakshatras-abbreviated]
      (let [padded (pad-nakshatra nakshatra)
            length (count padded)]
        (is (= 12 length)
            (format "Nakshatra '%s' padded to %d chars (expected 12): '%s'"
                   nakshatra length padded))))))

(deftest test-visual-alignment
  (testing "Visual verification: all graintimes align when stacked"
    (println "\n╔════════════════════════════════════════════════════════════════════════════════╗")
    (println "║  Visual test: All 27 nakshatras should align perfectly (76 chars each)        ║")
    (println "╠════════════════════════════════════════════════════════════════════════════════╣")
    (doseq [nakshatra nakshatras-abbreviated]
      (let [graintime (generate-test-graintime nakshatra)]
        (println (format "║  %s  ║" graintime))))
    (println "╚════════════════════════════════════════════════════════════════════════════════╝")
    (is true "Visual test complete")))

(deftest test-longest-and-shortest
  (testing "Longest nakshatra (u_bhadrapada) = 76 chars"
    (let [graintime (generate-test-graintime "u_bhadrapada")]
      (is (= 76 (count graintime))
          (format "Longest: %s" graintime))))
  
  (testing "Shortest nakshatras (4-5 chars) = 76 chars"
    (doseq [nakshatra ["mula" "hasta" "swati" "ardra"]]
      (let [graintime (generate-test-graintime nakshatra)]
        (is (= 76 (count graintime))
            (format "%s: %s" nakshatra graintime))))))

(deftest test-grainpath-96-chars
  (testing "Grainpath with 20-char title + 76-char graintime = 96 chars"
    (let [title "team10-complete-web"
          graintime (generate-test-graintime "jyeshtha")
          grainpath (str title "-" graintime)
          length (count grainpath)]
      (is (= 20 (count title)) "Title should be exactly 20 chars")
      (is (= 76 (count graintime)) "Graintime should be exactly 76 chars")
      (is (= 96 length) 
          (format "Grainpath should be 96 chars, got %d: %s" length grainpath)))))

(comment
  ;; Run tests manually
  (require '[clojure.test :refer [run-tests]])
  (run-tests 'graintime.nakshatra-76char-test)
  
  ;; Quick visual check
  (test-visual-alignment)
  
  ;; Print all nakshatra names with lengths
  (doseq [n nakshatras-abbreviated]
    (println (format "%-15s %2d chars → padded: '%s' (%d)"
                    n (count n) (pad-nakshatra n) (count (pad-nakshatra n)))))
  )

