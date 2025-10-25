(ns graintime.76char-format-test
  "Test that ALL combinations of 27 nakshatras x 14 teams = 76 chars exactly"
  (:require [clojure.test :refer [deftest is testing]]))

;; All 27 nakshatras (full names)
(def all-nakshatras
  ["Ashwini" "Bharani" "Krittika" "Rohini" "Mrigashira" "Ardra" "Punarvasu"
   "Pushya" "Ashlesha" "Magha" "Purva Phalguni" "Uttara Phalguni" "Hasta"
   "Chitra" "Swati" "Vishakha" "Anuradha" "Jyeshtha" "Mula" "Purva Ashadha"
   "Uttara Ashadha" "Shravana" "Dhanishta" "Shatabhisha" "Purva Bhadrapada"
   "Uttara Bhadrapada" "Revati"])

;; All 27 nakshatras (abbreviated)
(def abbreviated-nakshatras
  ["ashwini" "bharani" "krittika" "rohini" "mrigashira" "ardra" "punarvasu"
   "pushya" "ashlesha" "magha" "p_phalguni" "u_phalguni" "hasta"
   "chitra" "swati" "vishakha" "anuradha" "jyeshtha" "mula" "p_ashadha"
   "u_ashadha" "shravana" "dhanishta" "shatabhisha" "p_bhadrapada"
   "u_bhadrapada" "revati"])

;; All 14 team names
(def all-team-names
  ["teamfire" "teamvault" "teamnetwork" "teamnurture" 
   "teamshine" "teamprecision" "teambalance" "teamtransform"
   "teamwisdom" "teamstructure" "teamfuture" "teamflow"
   "teamascend" "teamdescend"])

;; Calculate max lengths
(def max-nakshatra-len (apply max (map count abbreviated-nakshatras))) ;; 12
(def max-team-len (apply max (map count all-team-names)))             ;; 13

(defn pad-nakshatra
  "Pad nakshatra to 12 chars with dashes"
  [nakshatra]
  (let [padding (- 12 (count nakshatra))]
    (str nakshatra (apply str (repeat padding "-")))))

(defn pad-team
  "Pad team name to 13 chars with dashes"
  [team]
  (let [padding (- 13 (count team))]
    (str team (apply str (repeat padding "-")))))

(defn generate-76char-graintime
  "Generate 76-char graintime with fixed-width nakshatra and team"
  [nakshatra team]
  (let [padded-nak (pad-nakshatra nakshatra)
        padded-team (pad-team team)]
    (format "12025-10-25--0051-PDT--moon-%s-asc-pisc29--sun-05h--%s10"
            padded-nak padded-team)))

(deftest test-all-27-nakshatras-are-76-chars
  (testing "All 27 nakshatras with teamstructure10 = 76 chars"
    (doseq [nak abbreviated-nakshatras]
      (let [graintime (generate-76char-graintime nak "teamstructure")
            len (count graintime)]
        (is (= 76 len)
            (str "Nakshatra '" nak "' should be 76 chars, got " len ": " graintime))))))

(deftest test-all-14-teams-are-76-chars
  (testing "All 14 teams with jyeshtha nakshatra = 76 chars"
    (doseq [team all-team-names]
      (let [graintime (generate-76char-graintime "jyeshtha" team)
            len (count graintime)]
        (is (= 76 len)
            (str "Team '" team "' should be 76 chars, got " len ": " graintime))))))

(deftest test-all-27x14-combinations-are-76-chars
  (testing "ALL 378 combinations (27 nakshatras × 14 teams) = 76 chars"
    (doseq [nak abbreviated-nakshatras
            team all-team-names]
      (let [graintime (generate-76char-graintime nak team)
            len (count graintime)]
        (is (= 76 len)
            (str "Combo '" nak "' + '" team "' should be 76 chars, got " len))))))

(deftest test-longest-combinations
  (testing "Longest nakshatra + longest team = 76 chars"
    (let [graintime (generate-76char-graintime "u_bhadrapada" "teamstructure")]
      (is (= 76 (count graintime))
          (str "Longest combo should be 76 chars: " graintime)))))

(deftest test-shortest-combinations
  (testing "Shortest nakshatra + shortest team = 76 chars"
    (let [graintime (generate-76char-graintime "mula" "teamflow")]
      (is (= 76 (count graintime))
          (str "Shortest combo should be 76 chars: " graintime)))))

(deftest display-sample-graintimes
  (testing "Display sample graintimes for visual verification"
    (println "\n🎯 SAMPLE 76-CHAR GRAINTIMES:")
    (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    (let [samples [["u_bhadrapada" "teamstructure" "Longest"]
                   ["jyeshtha" "teamstructure" "Current"]
                   ["ashwini" "teamflow" "Shortest"]
                   ["p_bhadrapada" "teamprecision" "Long both"]
                   ["mula" "teamfire" "Short both"]]]
      (doseq [[nak team desc] samples]
        (let [graintime (generate-76char-graintime nak team)]
          (println (format "%s (%d): %s" desc (count graintime) graintime)))))
    (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    (is true))) ;; Always pass, this is just for display

(comment
  ;; Run tests manually
  (clojure.test/run-tests 'graintime.76char-format-test)
  
  ;; Test specific nakshatra
  (generate-76char-graintime "u_bhadrapada" "teamstructure")
  
  ;; Show all team name lengths
  (doseq [team all-team-names]
    (println (format "%-15s %2d chars" team (count team))))
  
  ;; Show all nakshatra lengths
  (doseq [nak abbreviated-nakshatras]
    (println (format "%-15s %2d chars" nak (count nak)))))

