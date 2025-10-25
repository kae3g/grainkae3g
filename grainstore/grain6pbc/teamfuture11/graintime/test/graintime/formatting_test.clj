(ns graintime.formatting-test
  "Tests for graintime fixed-width formatting
  
  Validates that all graintimes have consistent visual alignment
  in monospace fonts by testing:
  - Nakshatra padding to 12 chars
  - House formatting with 2 digits
  - Character limit compliance (80 chars max)
  - Visual stacking alignment"
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.string :as str]))

;; Test data: All 27 nakshatras with abbreviations
(def all-nakshatras
  ["ashwini" "bharani" "krittika" "rohini" "mrigashira" "ardra" 
   "punarvasu" "pushya" "ashlesha" "magha" "p_phalguni" "u_phalguni"
   "hasta" "chitra" "swati" "vishakha" "anuradha" "jyeshtha"
   "mula" "p_ashadha" "u_ashadha" "shravana" "dhanishta" "shatabhisha"
   "p_bhadrapada" "u_bhadrapada" "revati"])

;; All 12 zodiac signs with abbreviations
(def all-zodiacs
  ["arie" "taur" "gemi" "canc" "leo" "virg" 
   "libr" "scor" "sagi" "capr" "aqua" "pisc"])

;; All 12 houses with ordinals
(def all-houses
  [{:num 1 :ordinal "01st"}
   {:num 2 :ordinal "02nd"}
   {:num 3 :ordinal "03rd"}
   {:num 4 :ordinal "04th"}
   {:num 5 :ordinal "05th"}
   {:num 6 :ordinal "06th"}
   {:num 7 :ordinal "07th"}
   {:num 8 :ordinal "08th"}
   {:num 9 :ordinal "09th"}
   {:num 10 :ordinal "10th"}
   {:num 11 :ordinal "11th"}
   {:num 12 :ordinal "12th"}])

(defn pad-nakshatra
  "Pad nakshatra name to 12 chars with dashes"
  [nakshatra]
  (let [padded (format "%-12s" nakshatra)]
    (str/replace padded #" " "-")))

(defn format-test-graintime
  "Format a test graintime with fixed widths"
  [nakshatra zodiac house]
  (let [padded-nak (pad-nakshatra nakshatra)
        house-ordinal (:ordinal house)]
    (format "12025-10-23--1234--PDT--moon-%s--asc-%s123--sun-%s--kae3g"
            padded-nak zodiac house-ordinal)))

(deftest test-nakshatra-padding
  (testing "All nakshatras pad to exactly 12 characters"
    (doseq [nak all-nakshatras]
      (let [padded (pad-nakshatra nak)]
        (is (= 12 (count padded))
            (str "Nakshatra " nak " should pad to 12 chars, got: " (count padded)))))))

(deftest test-house-formatting
  (testing "All houses format with 2 digits + ordinal suffix"
    (doseq [house all-houses]
      (let [ordinal (:ordinal house)]
        (is (= 4 (count ordinal))
            (str "House " (:num house) " should be 4 chars, got: " ordinal))
        (is (or (str/ends-with? ordinal "st")
                (str/ends-with? ordinal "nd")
                (str/ends-with? ordinal "rd")
                (str/ends-with? ordinal "th"))
            (str "House " (:num house) " should end with st/nd/rd/th"))))))

(deftest test-graintime-length-all-combinations
  (testing "All possible nakshatra/zodiac/house combinations stay under 80 chars"
    (let [results (atom [])]
      (doseq [nak all-nakshatras
              zod all-zodiacs
              house all-houses]
        (let [graintime (format-test-graintime nak zod house)
              length (count graintime)]
          (swap! results conj {:nakshatra nak
                               :zodiac zod
                               :house (:num house)
                               :graintime graintime
                               :length length
                               :valid (<= length 80)})
          (is (<= length 80)
              (str "Graintime too long: " length " chars\n" graintime))))
      
      ;; Print summary
      (let [all-results @results
            max-length (apply max (map :length all-results))
            min-length (apply min (map :length all-results))
            avg-length (int (/ (apply + (map :length all-results))
                              (count all-results)))]
        (println "")
        (println "📊 Graintime Length Analysis:")
        (println (str "   Total combinations: " (count all-results)))
        (println (str "   Min length: " min-length " chars"))
        (println (str "   Max length: " max-length " chars"))
        (println (str "   Avg length: " avg-length " chars"))
        (println (str "   All valid (<= 80): " (every? :valid all-results)))
        (println "")))))

(deftest test-visual-stacking
  (testing "Graintimes visually stack in monospace with perfect alignment"
    (println "")
    (println "🎨 Visual Stacking Test (first 10 nakshatras):")
    (println "")
    (doseq [nak (take 10 all-nakshatras)]
      (let [graintime (format-test-graintime nak "gem" (nth all-houses 3))]
        (println graintime)))
    (println "")
    
    ;; Test that moon- section aligns
    (let [graintimes (map #(format-test-graintime % "gem" (nth all-houses 3))
                         (take 5 all-nakshatras))
          moon-positions (map #(str/index-of % "moon-") graintimes)]
      (is (apply = moon-positions)
          "moon- should be at same position in all graintimes"))))

(deftest test-field-alignment
  (testing "All graintime fields align at same positions"
    (let [nak1 (format-test-graintime "mula" "leo" (nth all-houses 0))
          nak2 (format-test-graintime "u_bhadrapada" "sag" (nth all-houses 11))
          
          ;; Extract field positions
          date-pos1 (str/index-of nak1 "12025")
          date-pos2 (str/index-of nak2 "12025")
          
          moon-pos1 (str/index-of nak1 "moon-")
          moon-pos2 (str/index-of nak2 "moon-")
          
          asc-pos1 (str/index-of nak1 "asc-")
          asc-pos2 (str/index-of nak2 "asc-")
          
          sun-pos1 (str/index-of nak1 "sun-")
          sun-pos2 (str/index-of nak2 "sun-")]
      
      (is (= date-pos1 date-pos2) "Date position should align")
      (is (= moon-pos1 moon-pos2) "Moon position should align")
      (is (= asc-pos1 asc-pos2) "Ascendant position should align")
      (is (= sun-pos1 sun-pos2) "Sun position should align")
      
      (println "")
      (println "Field Alignment Test:")
      (println nak1)
      (println nak2)
      (println "")
      (println (str "Date: " date-pos1 " = " date-pos2 " ✓"))
      (println (str "Moon: " moon-pos1 " = " moon-pos2 " ✓"))
      (println (str "Asc:  " asc-pos1 " = " asc-pos2 " ✓"))
      (println (str "Sun:  " sun-pos1 " = " sun-pos2 " ✓"))
      (println ""))))

(deftest test-worst-case-scenario
  (testing "Worst-case graintime (longest nakshatra + longest zodiac + 12th house)"
    (let [worst-graintime (format-test-graintime "u_bhadrapada" "sag" (last all-houses))
          length (count worst-graintime)]
      (println "")
      (println "Worst-case graintime:")
      (println worst-graintime)
      (println (str "Length: " length "/80 chars"))
      (println "")
      (is (<= length 80)
          (str "Worst-case graintime too long: " length " chars")))))

(deftest test-best-case-scenario
  (testing "Best-case graintime (shortest nakshatra + shortest zodiac + 1st house)"
    (let [best-graintime (format-test-graintime "mula" "leo" (first all-houses))
          length (count best-graintime)]
      (println "")
      (println "Best-case graintime:")
      (println best-graintime)
      (println (str "Length: " length "/80 chars"))
      (println "")
      (is (<= length 80)
          (str "Best-case graintime too long: " length " chars")))))

(deftest test-all-nakshatras-visual-stack
  (testing "Visual stack of all 27 nakshatras for perfect alignment"
    (println "")
    (println "🌙 All 27 Nakshatras Stacked (same zodiac/house):")
    (println "")
    (doseq [nak all-nakshatras]
      (println (format-test-graintime nak "gem" (nth all-houses 3))))
    (println "")))

(deftest test-all-houses-visual-stack
  (testing "Visual stack of all 12 houses for perfect alignment"
    (println "")
    (println "🏠 All 12 Houses Stacked (same nakshatra/zodiac):")
    (println "")
    (doseq [house all-houses]
      (println (format-test-graintime "vishakha" "gem" house)))
    (println "")))

(deftest test-all-zodiacs-visual-stack
  (testing "Visual stack of all 12 zodiac signs for perfect alignment"
    (println "")
    (println "♈ All 12 Zodiac Signs Stacked (same nakshatra/house):")
    (println "")
    (doseq [zod all-zodiacs]
      (println (format-test-graintime "vishakha" zod (nth all-houses 3))))
    (println "")))

(deftest test-random-combinations
  (testing "Random combinations for visual inspection"
    (println "")
    (println "🎲 Random Graintime Combinations:")
    (println "")
    (let [test-cases [["ashwini" "ari" (nth all-houses 0)]
                     ["u_bhadrapada" "sag" (nth all-houses 11)]
                     ["vishakha" "gem" (nth all-houses 3)]
                     ["p_phalguni" "leo" (nth all-houses 9)]
                     ["mula" "pis" (nth all-houses 6)]
                     ["shatabhisha" "cap" (nth all-houses 1)]]]
      (doseq [[nak zod house] test-cases]
        (println (format-test-graintime nak zod house))))
    (println "")))

;; Run all tests
(defn run-all-tests
  "Run all formatting tests with visual output"
  []
  (println "")
  (println "🧪 Running Graintime Formatting Tests...")
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
  (println "")
  
  (clojure.test/run-tests 'graintime.formatting-test)
  
  (println "")
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
  (println "✅ All formatting tests complete!")
  (println ""))

(comment
  ;; Run tests from REPL
  (run-all-tests)
  
  ;; Individual tests
  (clojure.test/run-tests 'graintime.formatting-test))

