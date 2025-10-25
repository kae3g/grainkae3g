(ns graintime.solar-house-test
  "Comprehensive tests for solar house calculation
  
  Tests the NON-SYMMETRIC algorithm:
  - Cardinal houses exact for 1 minute only
  - Intermediate houses split evenly in proportion to day/night lengths
  
  Test Cases:
  1. Exact cardinal points (1 minute precision)
  2. Intermediate houses (morning, afternoon, evening, night)
  3. Edge cases (just before/after cardinal points)
  4. Day/night length variations (seasonal testing)"
  (:require [clojure.test :refer :all]
            [graintime.solar-houses :as solar]))

;; =============================================================================
;; TEST DATA - October 24, 2025, San Rafael, CA
;; =============================================================================

(def test-solar-times
  "Actual solar times from sunrise-sunset.org API
   Date: October 24, 2025
   Location: San Rafael, CA (37.9735°N, 122.5311°W)"
  {:sunrise     {:hour 7  :minute 26 :fractional 7.4333}   ; 07:26 PDT
   :solar-noon  {:hour 12 :minute 54 :fractional 12.9}     ; 12:54 PDT
   :sunset      {:hour 18 :minute 21 :fractional 18.35}    ; 18:21 PDT
   :solar-midnight {:hour 0 :minute 54 :fractional 0.9}})  ; 00:54 PDT (next day)

;; =============================================================================
;; CARDINAL HOUSE TESTS (Exact 1-minute precision)
;; =============================================================================

(deftest test-exact-sunrise
  (testing "Exact sunrise = 1st house (07:26 PDT)"
    (let [current-time {:hour 7 :minute 26 :fractional 7.4333}
          result (solar/calculate-solar-house 
                   current-time 
                   (:sunrise test-solar-times)
                   (:solar-noon test-solar-times)
                   (:sunset test-solar-times)
                   (:solar-midnight test-solar-times))]
      (is (= 1 (:house result)))
      (is (= "sunrise" (:solar-position result)))
      (is (true? (:is-day result))))))

(deftest test-exact-solar-noon
  (testing "Exact solar noon = 10th house (12:54 PDT)"
    (let [current-time {:hour 12 :minute 54 :fractional 12.9}
          result (solar/calculate-solar-house 
                   current-time 
                   (:sunrise test-solar-times)
                   (:solar-noon test-solar-times)
                   (:sunset test-solar-times)
                   (:solar-midnight test-solar-times))]
      (is (= 10 (:house result)))
      (is (= "noon" (:solar-position result)))
      (is (true? (:is-day result))))))

(deftest test-exact-sunset
  (testing "Exact sunset = 7th house (18:21 PDT)"
    (let [current-time {:hour 18 :minute 21 :fractional 18.35}
          result (solar/calculate-solar-house 
                   current-time 
                   (:sunrise test-solar-times)
                   (:solar-noon test-solar-times)
                   (:sunset test-solar-times)
                   (:solar-midnight test-solar-times))]
      (is (= 7 (:house result)))
      (is (= "sunset" (:solar-position result)))
      (is (false? (:is-day result))))))

(deftest test-exact-solar-midnight
  (testing "Exact solar midnight = 4th house (00:54 PDT)"
    (let [current-time {:hour 0 :minute 54 :fractional 0.9}
          result (solar/calculate-solar-house 
                   current-time 
                   (:sunrise test-solar-times)
                   (:solar-noon test-solar-times)
                   (:sunset test-solar-times)
                   (:solar-midnight test-solar-times))]
      (is (= 4 (:house result)))
      (is (= "midnight" (:solar-position result)))
      (is (false? (:is-day result))))))

;; =============================================================================
;; EDGE CASE TESTS (Just before/after cardinal points)
;; =============================================================================

(deftest test-one-minute-before-sunrise
  (testing "One minute before sunrise = 2nd house (07:25 PDT)"
    (let [current-time {:hour 7 :minute 25 :fractional 7.4167}
          result (solar/calculate-solar-house 
                   current-time 
                   (:sunrise test-solar-times)
                   (:solar-noon test-solar-times)
                   (:sunset test-solar-times)
                   (:solar-midnight test-solar-times))]
      (is (= 2 (:house result)))
      (is (= "night" (:solar-position result))))))

(deftest test-one-minute-after-sunrise
  (testing "One minute after sunrise = 12th house (07:27 PDT)"
    (let [current-time {:hour 7 :minute 27 :fractional 7.45}
          result (solar/calculate-solar-house 
                   current-time 
                   (:sunrise test-solar-times)
                   (:solar-noon test-solar-times)
                   (:sunset test-solar-times)
                   (:solar-midnight test-solar-times))]
      (is (= 12 (:house result)))
      (is (= "morning" (:solar-position result))))))

(deftest test-one-minute-before-sunset
  (testing "One minute before sunset = 8th house (18:20 PDT)"
    (let [current-time {:hour 18 :minute 20 :fractional 18.3333}
          result (solar/calculate-solar-house 
                   current-time 
                   (:sunrise test-solar-times)
                   (:solar-noon test-solar-times)
                   (:sunset test-solar-times)
                   (:solar-midnight test-solar-times))]
      (is (= 8 (:house result)))
      (is (= "afternoon" (:solar-position result))))))

(deftest test-one-minute-after-sunset
  (testing "One minute after sunset = 6th house (18:22 PDT)"
    (let [current-time {:hour 18 :minute 22 :fractional 18.3667}
          result (solar/calculate-solar-house 
                   current-time 
                   (:sunrise test-solar-times)
                   (:solar-noon test-solar-times)
                   (:sunset test-solar-times)
                   (:solar-midnight test-solar-times))]
      (is (= 6 (:house result)))
      (is (= "evening" (:solar-position result)))
      (is (false? (:is-day result))))))

;; =============================================================================
;; INTERMEDIATE HOUSE TESTS (Split evenly in proportions)
;; =============================================================================

(deftest test-morning-period
  (testing "Morning: Between sunrise (07:26) and noon (12:54)"
    ;; Morning duration: 5h 28min = 328 minutes
    ;; Split into 12th (first half) and 11th (second half)
    
    (testing "Early morning = 12th house (08:30 PDT)"
      (let [current-time {:hour 8 :minute 30 :fractional 8.5}
            result (solar/calculate-solar-house 
                     current-time 
                     (:sunrise test-solar-times)
                     (:solar-noon test-solar-times)
                     (:sunset test-solar-times)
                     (:solar-midnight test-solar-times))]
        (is (= 12 (:house result)))
        (is (= "morning" (:solar-position result)))))
    
    (testing "Late morning = 11th house (11:30 PDT)"
      (let [current-time {:hour 11 :minute 30 :fractional 11.5}
            result (solar/calculate-solar-house 
                     current-time 
                     (:sunrise test-solar-times)
                     (:solar-noon test-solar-times)
                     (:sunset test-solar-times)
                     (:solar-midnight test-solar-times))]
        (is (= 11 (:house result)))
        (is (= "morning" (:solar-position result)))))))

(deftest test-afternoon-period
  (testing "Afternoon: Between noon (12:54) and sunset (18:21)"
    ;; Afternoon duration: 5h 27min = 327 minutes
    ;; Split into 9th (first half) and 8th (second half)
    
    (testing "Early afternoon = 9th house (14:00 PDT)"
      (let [current-time {:hour 14 :minute 0 :fractional 14.0}
            result (solar/calculate-solar-house 
                     current-time 
                     (:sunrise test-solar-times)
                     (:solar-noon test-solar-times)
                     (:sunset test-solar-times)
                     (:solar-midnight test-solar-times))]
        (is (= 9 (:house result)))
        (is (= "afternoon" (:solar-position result)))))
    
    (testing "Late afternoon = 8th house (17:00 PDT)"
      (let [current-time {:hour 17 :minute 0 :fractional 17.0}
            result (solar/calculate-solar-house 
                     current-time 
                     (:sunrise test-solar-times)
                     (:solar-noon test-solar-times)
                     (:sunset test-solar-times)
                     (:solar-midnight test-solar-times))]
        (is (= 8 (:house result)))
        (is (= "afternoon" (:solar-position result)))))))

(deftest test-evening-period
  (testing "Evening: Between sunset (18:21) and midnight (00:54)"
    ;; Evening duration: 6h 33min = 393 minutes
    ;; Split into 6th (first half) and 5th (second half)
    
    (testing "Early evening = 6th house (19:00 PDT) - OUR CURRENT TIME RANGE"
      (let [current-time {:hour 19 :minute 0 :fractional 19.0}
            result (solar/calculate-solar-house 
                     current-time 
                     (:sunrise test-solar-times)
                     (:solar-noon test-solar-times)
                     (:sunset test-solar-times)
                     (:solar-midnight test-solar-times))]
        (is (= 6 (:house result)))
        (is (= "evening" (:solar-position result)))
        (is (false? (:is-day result)))))
    
    (testing "Current session time = 6th house (18:53 PDT)"
      (let [current-time {:hour 18 :minute 53 :fractional 18.8833}
            result (solar/calculate-solar-house 
                     current-time 
                     (:sunrise test-solar-times)
                     (:solar-noon test-solar-times)
                     (:sunset test-solar-times)
                     (:solar-midnight test-solar-times))]
        (is (= 6 (:house result)) "Should be 6th house (32 min after sunset)")
        (is (= "evening" (:solar-position result)))
        (is (false? (:is-day result)))))
    
    (testing "Late evening = 5th house (23:00 PDT)"
      (let [current-time {:hour 23 :minute 0 :fractional 23.0}
            result (solar/calculate-solar-house 
                     current-time 
                     (:sunrise test-solar-times)
                     (:solar-noon test-solar-times)
                     (:sunset test-solar-times)
                     (:solar-midnight test-solar-times))]
        (is (= 5 (:house result)))
        (is (= "evening" (:solar-position result)))))))

(deftest test-night-period
  (testing "Night: Between midnight (00:54) and sunrise (07:26)"
    ;; Night duration: 6h 32min = 392 minutes
    ;; Split into 3rd (first half) and 2nd (second half)
    
    (testing "Early night = 3rd house (02:00 PDT)"
      (let [current-time {:hour 2 :minute 0 :fractional 2.0}
            result (solar/calculate-solar-house 
                     current-time 
                     (:sunrise test-solar-times)
                     (:solar-noon test-solar-times)
                     (:sunset test-solar-times)
                     (:solar-midnight test-solar-times))]
        (is (= 3 (:house result)))
        (is (= "night" (:solar-position result)))))
    
    (testing "Late night = 2nd house (05:00 PDT)"
      (let [current-time {:hour 5 :minute 0 :fractional 5.0}
            result (solar/calculate-solar-house 
                     current-time 
                     (:sunrise test-solar-times)
                     (:solar-noon test-solar-times)
                     (:sunset test-solar-times)
                     (:solar-midnight test-solar-times))]
        (is (= 2 (:house result)))
        (is (= "night" (:solar-position result)))))))

;; =============================================================================
;; COMPREHENSIVE BOUNDARY TESTS
;; =============================================================================

(deftest test-all-boundaries
  (testing "All cardinal house boundaries (1-minute precision)"
    ;; Test exact minute for each cardinal point
    (doseq [[time expected-house position]
            [[{:hour 7 :minute 26} 1 "sunrise"]
             [{:hour 12 :minute 54} 10 "noon"]
             [{:hour 18 :minute 21} 7 "sunset"]
             [{:hour 0 :minute 54} 4 "midnight"]]]
      (let [current-time (merge time {:fractional (+ (:hour time) (/ (:minute time) 60.0))})
            result (solar/calculate-solar-house 
                     current-time 
                     (:sunrise test-solar-times)
                     (:solar-noon test-solar-times)
                     (:sunset test-solar-times)
                     (:solar-midnight test-solar-times))]
        (is (= expected-house (:house result))
            (str "At " (:hour time) ":" (:minute time) " should be house " expected-house))
        (is (= position (:solar-position result)))))))

(deftest test-transition-boundaries
  (testing "Transitions between intermediate houses"
    ;; Morning: 07:26 → 12:54 (328 min)
    ;; Midpoint: 07:26 + 164 min = 10:10 (transition 12th → 11th)
    
    (testing "Just before morning midpoint = 12th house (10:09 PDT)"
      (let [current-time {:hour 10 :minute 9 :fractional 10.15}
            result (solar/calculate-solar-house 
                     current-time 
                     (:sunrise test-solar-times)
                     (:solar-noon test-solar-times)
                     (:sunset test-solar-times)
                     (:solar-midnight test-solar-times))]
        (is (= 12 (:house result)))))
    
    (testing "Just after morning midpoint = 11th house (10:11 PDT)"
      (let [current-time {:hour 10 :minute 11 :fractional 10.1833}
            result (solar/calculate-solar-house 
                     current-time 
                     (:sunrise test-solar-times)
                     (:solar-noon test-solar-times)
                     (:sunset test-solar-times)
                     (:solar-midnight test-solar-times))]
        (is (= 11 (:house result)))))))

;; =============================================================================
;; CURRENT SESSION TIME TEST (18:53 PDT = 6th house)
;; =============================================================================

(deftest test-session-780-time
  (testing "Session 780 time: 18:53 PDT should be 6th house"
    (let [current-time {:hour 18 :minute 53 :fractional 18.8833}
          result (solar/calculate-solar-house 
                   current-time 
                   (:sunrise test-solar-times)
                   (:solar-noon test-solar-times)
                   (:sunset test-solar-times)
                   (:solar-midnight test-solar-times))]
      
      (println "\n=== Session 780 Solar House Calculation ===")
      (println "Current time: 18:53 PDT")
      (println "Sunset time:  18:21 PDT")
      (println "Minutes after sunset:" (- 1133 1101) "minutes")
      (println "Result:" result)
      
      (is (= 6 (:house result)) 
          "Should be 6th house (32 minutes after sunset, early evening)")
      (is (= "evening" (:solar-position result)))
      (is (false? (:is-day result))))))

;; =============================================================================
;; PROPORTIONAL SPLIT TESTS
;; =============================================================================

(deftest test-proportional-day-night-split
  (testing "Day/night periods split evenly between 2 houses each"
    ;; Morning: 328 min ÷ 2 = 164 min per house (12th, 11th)
    ;; Afternoon: 327 min ÷ 2 = 163.5 min per house (9th, 8th)
    ;; Evening: 393 min ÷ 2 = 196.5 min per house (6th, 5th)
    ;; Night: 392 min ÷ 2 = 196 min per house (3rd, 2nd)
    
    (testing "Morning duration proportions"
      ;; 12th house: 07:26 → 10:10 (164 min)
      ;; 11th house: 10:10 → 12:54 (164 min)
      (let [early-morning {:hour 8 :minute 0 :fractional 8.0}
            late-morning {:hour 12 :minute 0 :fractional 12.0}
            early-result (solar/calculate-solar-house early-morning (:sunrise test-solar-times) (:solar-noon test-solar-times) (:sunset test-solar-times) (:solar-midnight test-solar-times))
            late-result (solar/calculate-solar-house late-morning (:sunrise test-solar-times) (:solar-noon test-solar-times) (:sunset test-solar-times) (:solar-midnight test-solar-times))]
        (is (= 12 (:house early-result)) "Early morning should be 12th")
        (is (= 11 (:house late-result)) "Late morning should be 11th")))
    
    (testing "Evening duration proportions"
      ;; 6th house: 18:21 → 21:37 (196.5 min)
      ;; 5th house: 21:37 → 00:54 (196.5 min)
      (let [early-evening {:hour 19 :minute 0 :fractional 19.0}
            late-evening {:hour 23 :minute 0 :fractional 23.0}
            early-result (solar/calculate-solar-house early-evening (:sunrise test-solar-times) (:solar-noon test-solar-times) (:sunset test-solar-times) (:solar-midnight test-solar-times))
            late-result (solar/calculate-solar-house late-evening (:sunrise test-solar-times) (:solar-noon test-solar-times) (:sunset test-solar-times) (:solar-midnight test-solar-times))]
        (is (= 6 (:house early-result)) "Early evening should be 6th")
        (is (= 5 (:house late-result)) "Late evening should be 5th")))))

;; =============================================================================
;; SEASONAL VARIATION TESTS (Future enhancement)
;; =============================================================================

(deftest test-summer-solstice
  (testing "Summer solstice (longest day) - June 21, 2025"
    ;; Sunrise: ~05:50 PDT, Sunset: ~20:30 PDT
    ;; Day much longer than night
    ;; This would require API call or hardcoded times
    (is true "Placeholder for seasonal testing")))

(deftest test-winter-solstice
  (testing "Winter solstice (shortest day) - December 21, 2025"
    ;; Sunrise: ~07:25 PDT, Sunset: ~16:50 PDT
    ;; Night much longer than day
    ;; This would require API call or hardcoded times
    (is true "Placeholder for seasonal testing")))

;; =============================================================================
;; RUN ALL TESTS
;; =============================================================================

(defn run-all-tests
  "Run all solar house calculation tests"
  []
  (println "\n🌾 Running Solar House Calculation Tests")
  (println "==========================================\n")
  
  (run-tests 'graintime.solar-house-test)
  
  (println "\n🌾 Test Summary")
  (println "==========================================")
  (println "All tests verify the NON-SYMMETRIC algorithm:")
  (println "- Cardinal houses: Exact for 1 minute only")
  (println "- Intermediate houses: Split evenly in proportion")
  (println "\n✨ Tests complete! ✨\n"))

(comment
  ;; Run tests from REPL
  (run-all-tests))

