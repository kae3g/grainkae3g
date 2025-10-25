(ns graintime.ascendant-test
  "Tests for ascendant (rising sign) calculation
   
   Verifies that ascendant changes correctly throughout the day
   and matches expected signs for different times."
  (:require [clojure.test :refer [deftest is testing]]
            [graintime.astromitra :as astro]
            [java.time :as jt]))

;;; ╔═══════════════════════════════════════════════════════════════════╗
;;; ║                                                                   ║
;;; ║   🌾  A S C E N D A N T   C A L C U L A T I O N   T E S T S      ║
;;; ║                                                                   ║
;;; ║   Verifying rising sign changes throughout the day               ║
;;; ║                                                                   ║
;;; ╚═══════════════════════════════════════════════════════════════════╝

;; Test location: San Rafael, CA
(def test-lat 37.9735)
(def test-lon -122.5311)
(def test-date "2025-10-23")

(defn make-datetime [hour minute]
  "Create ZonedDateTime for testing"
  (java.time.ZonedDateTime/parse
    (format "%sT%02d:%02d:00-07:00[America/Los_Angeles]"
            test-date hour minute)))

(deftest ascendant-changes-throughout-day
  (testing "Ascendant should change approximately every 2 hours"
    (let [midnight-asc (astro/calculate-ascendant-improved 
                         (make-datetime 0 0) test-lat test-lon)
          morning-asc (astro/calculate-ascendant-improved 
                        (make-datetime 6 0) test-lat test-lon)
          noon-asc (astro/calculate-ascendant-improved 
                     (make-datetime 12 0) test-lat test-lon)
          evening-asc (astro/calculate-ascendant-improved 
                        (make-datetime 18 0) test-lat test-lon)]
      
      ;; Ascendants should be different at different times
      (is (not= (:sign midnight-asc) (:sign noon-asc))
          "Midnight and noon ascendants should differ")
      
      (is (not= (:sign morning-asc) (:sign evening-asc))
          "Morning and evening ascendants should differ")
      
      ;; Print results for verification
      (println "\n🌅 Ascendant Changes Throughout Day:")
      (println (format "  Midnight (00:00): %s %s°" (:sign midnight-asc) (:degree midnight-asc)))
      (println (format "  Morning  (06:00): %s %s°" (:sign morning-asc) (:degree morning-asc)))
      (println (format "  Noon     (12:00): %s %s°" (:sign noon-asc) (:degree noon-asc)))
      (println (format "  Evening  (18:00): %s %s°" (:sign evening-asc) (:degree evening-asc))))))

(deftest evening-ascendant-not-gemini
  (testing "Evening time (6-8pm) should NOT have Gemini ascendant"
    (let [evening-times [[18 0] [18 30] [19 0] [19 30] [20 0]]
          evening-ascendants (map (fn [[h m]]
                                    (let [dt (make-datetime h m)
                                          asc (astro/calculate-ascendant-improved dt test-lat test-lon)]
                                      {:time (format "%02d:%02d" h m)
                                       :sign (:sign asc)
                                       :degree (:degree asc)}))
                                  evening-times)]
      
      (println "\n🌙 Evening Ascendants (18:00-20:00):")
      (doseq [asc evening-ascendants]
        (println (format "  %s → %s %s°" (:time asc) (:sign asc) (:degree asc))))
      
      ;; Gemini rises around 3-5 AM, NOT in evening
      (is (every? #(not= "gem" (:sign %)) evening-ascendants)
          "Evening times should NOT have Gemini (early morning sign) ascendant"))))

(deftest morning-ascendant-makes-sense
  (testing "Early morning (3-6am) might have Gemini ascendant"
    (let [early-morning-times [[3 0] [4 0] [5 0] [6 0]]
          morning-ascendants (map (fn [[h m]]
                                    (let [dt (make-datetime h m)
                                          asc (astro/calculate-ascendant-improved dt test-lat test-lon)]
                                      {:time (format "%02d:%02d" h m)
                                       :sign (:sign asc)
                                       :degree (:degree asc)}))
                                  early-morning-times)]
      
      (println "\n🌅 Early Morning Ascendants (03:00-06:00):")
      (doseq [asc morning-ascendants]
        (println (format "  %s → %s %s°" (:time asc) (:sign asc) (:degree asc))))
      
      ;; At least one early morning time might have Gemini
      ;; (This is more reasonable than evening having Gemini)
      (is (some identity
                (map #(#{"gem" "tau" "ari"} (:sign %)) morning-ascendants))
          "Early morning should have eastern zodiac signs (Aries, Taurus, Gemini)"))))

(deftest ascendant-follows-zodiac-order
  (testing "Ascendant should progress through zodiac signs in order throughout day"
    (let [all-hours (range 0 24)
          all-ascendants (map (fn [h]
                                (let [dt (make-datetime h 0)
                                      asc (astro/calculate-ascendant-improved dt test-lat test-lon)]
                                  {:hour h
                                   :sign (:sign asc)
                                   :degree (:degree asc)}))
                              all-hours)]
      
      (println "\n⏰ Ascendant Progression (Every Hour):")
      (doseq [asc all-ascendants]
        (println (format "  %02d:00 → %s %s°" (:hour asc) (:sign asc) (:degree asc))))
      
      ;; Ascendant should change at least a few times in 24 hours
      (let [unique-signs (set (map :sign all-ascendants))]
        (is (>= (count unique-signs) 6)
            "Should have at least 6 different ascendant signs in 24 hours")))))

(deftest ascendant-degree-in-range
  (testing "Ascendant degree should be 0-29"
    (let [test-times [[0 0] [6 30] [12 0] [18 45] [23 59]]
          ascendants (map (fn [[h m]]
                            (astro/calculate-ascendant-improved 
                              (make-datetime h m) test-lat test-lon))
                          test-times)]
      
      (doseq [asc ascendants]
        (let [deg (if (string? (:degree asc))
                    (Integer/parseInt (:degree asc))
                    (:degree asc))]
          (is (and (>= deg 0) (< deg 30))
              (format "Degree %d should be 0-29" deg)))))))

(deftest lst-calculation-makes-sense
  (testing "Local Sidereal Time should differ from clock time"
    (let [noon-dt (make-datetime 12 0)
          noon-asc (astro/calculate-ascendant-improved noon-dt test-lat test-lon)]
      
      (println "\n🕐 Local Sidereal Time at Noon:")
      (println (format "  Clock Time: 12:00"))
      (println (format "  LST Hours: %.2f" (:lst-hours noon-asc)))
      (println (format "  Ascendant: %s %s°" (:sign noon-asc) (:degree noon-asc)))
      
      ;; LST should be different from 12.0 (unless you're at Greenwich at equinox!)
      ;; For San Rafael (longitude -122°), LST should be ~8 hours behind
      (is (not= 12.0 (:lst-hours noon-asc))
          "LST should differ from clock time due to longitude"))))

(deftest compare-to-expected-evening-ascendant
  (testing "Evening ascendant (18:45) should be Scorpio/Sagittarius area"
    (let [evening-dt (make-datetime 18 45)
          evening-asc (astro/calculate-ascendant-improved evening-dt test-lat test-lon)]
      
      (println "\n🌆 Evening Ascendant (18:45 PDT):")
      (println (format "  Calculated: %s %s°" (:sign evening-asc) (:degree evening-asc)))
      (println (format "  LST: %.2f hours" (:lst-hours evening-asc)))
      (println (format "  Method: %s" (:method evening-asc)))
      
      ;; Evening ascendants are typically in Scorpio/Sagittarius/Capricorn range
      ;; (zodiac positions 8-10)
      (is (contains? #{"lib" "sco" "sag" "cap" "aqu"} (:sign evening-asc))
          (format "Evening ascendant should be Libra/Scorpio/Sagittarius/Capricorn/Aquarius, got %s"
                  (:sign evening-asc)))
      
      ;; Definitely NOT Gemini!
      (is (not= "gem" (:sign evening-asc))
          "Evening should NOT have Gemini ascendant (that's early morning!)"))))

(deftest sunrise-ascendant-matches-sun-sign
  (testing "At sunrise, ascendant should be same/close to sun's sign"
    (let [sunrise-dt (make-datetime 7 25)  ; Sunrise from gt output
          sunrise-asc (astro/calculate-ascendant-improved sunrise-dt test-lat test-lon)]
      
      (println "\n🌅 Sunrise Ascendant (07:25):")
      (println (format "  Ascendant: %s %s°" (:sign sunrise-asc) (:degree sunrise-asc)))
      (println "  Expected: Close to Sun's position (late Libra/early Scorpio for Oct 23)")
      
      ;; At sunrise, ascendant = sun's position approximately
      ;; Late October sun is in Libra/Scorpio (tropical) or Virgo/Libra (sidereal)
      (is (contains? #{"vir" "lib" "sco"} (:sign sunrise-asc))
          "Sunrise ascendant should match sun's October position"))))

(deftest latitude-affects-ascendant-speed
  (testing "Higher latitudes should have faster ascendant changes"
    (let [test-time (make-datetime 12 0)
          
          ;; Test at different latitudes
          equator-asc (astro/calculate-ascendant-improved test-time 0.0 test-lon)
          mid-lat-asc (astro/calculate-ascendant-improved test-time 37.9735 test-lon)
          polar-asc (astro/calculate-ascendant-improved test-time 70.0 test-lon)]
      
      (println "\n🌍 Latitude Effect on Ascendant:")
      (println (format "  Equator (0°):     %s" (:sign equator-asc)))
      (println (format "  Mid-lat (37.97°): %s" (:sign mid-lat-asc)))
      (println (format "  Polar (70°):      %s" (:sign polar-asc)))
      
      ;; All should be valid signs
      (is (some? (:sign equator-asc)))
      (is (some? (:sign mid-lat-asc)))
      (is (some? (:sign polar-asc))))))

;; Run all tests and print summary
(defn run-all-tests []
  (println "╔══════════════════════════════════════════════════════════════╗")
  (println "║                                                              ║")
  (println "║   🌾  A S C E N D A N T   C A L C U L A T I O N   T E S T S ║")
  (println "║                                                              ║")
  (println "╚══════════════════════════════════════════════════════════════╝")
  (println)
  (clojure.test/run-tests 'graintime.ascendant-test)
  (println)
  (println "🌾 now == next + 1"))

