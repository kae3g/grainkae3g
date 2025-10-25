(ns graintime.all-76char-combinations-test
  "Test ALL 378 combinations (27 nakshatras × 14 teams) = exactly 76 chars"
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.string :as str]))

;; =============================================================================
;; DATA: All 27 Nakshatras (abbreviated for graintime)
;; =============================================================================

(def all-nakshatras-abbreviated
  ["ashwini"      ; 7 chars
   "bharani"      ; 7 chars
   "krittika"     ; 8 chars
   "rohini"       ; 6 chars
   "mrigashira"   ; 10 chars
   "ardra"        ; 5 chars
   "punarvasu"    ; 9 chars
   "pushya"       ; 6 chars
   "ashlesha"     ; 8 chars
   "magha"        ; 5 chars
   "p_phalguni"   ; 10 chars (Purva Phalguni)
   "u_phalguni"   ; 10 chars (Uttara Phalguni)
   "hasta"        ; 5 chars
   "chitra"       ; 6 chars
   "swati"        ; 5 chars
   "vishakha"     ; 8 chars
   "anuradha"     ; 8 chars
   "jyeshtha"     ; 8 chars
   "mula"         ; 4 chars (SHORTEST!)
   "p_ashadha"    ; 9 chars (Purva Ashadha)
   "u_ashadha"    ; 9 chars (Uttara Ashadha)
   "shravana"     ; 8 chars
   "dhanishta"    ; 9 chars
   "shatabhisha"  ; 11 chars
   "p_bhadrapada" ; 12 chars (Purva Bhadrapada)
   "u_bhadrapada" ; 12 chars (Uttara Bhadrapada) (LONGEST!)
   "revati"])     ; 6 chars

;; =============================================================================
;; DATA: All 14 Team Names
;; =============================================================================

(def all-team-names
  ["fire"       ; 4 chars → teamfire10 = 10 chars
   "vault"      ; 5 chars → teamvault10 = 11 chars
   "network"    ; 7 chars → teamnetwork10 = 13 chars
   "nurture"    ; 7 chars → teamnurture10 = 13 chars
   "shine"      ; 5 chars → teamshine10 = 11 chars
   "precision"  ; 9 chars → teamprecision10 = 15 chars
   "balance"    ; 7 chars → teambalance10 = 13 chars
   "transform"  ; 9 chars → teamtransform10 = 15 chars
   "wisdom"     ; 6 chars → teamwisdom10 = 12 chars
   "structure"  ; 9 chars → teamstructure10 = 15 chars (LONGEST!)
   "future"     ; 6 chars → teamfuture10 = 12 chars
   "flow"       ; 4 chars → teamflow10 = 10 chars (SHORTEST!)
   "ascend"     ; 6 chars → teamascend10 = 12 chars
   "descend"])  ; 7 chars → teamdescend10 = 13 chars

;; =============================================================================
;; PADDING FUNCTIONS
;; =============================================================================

(defn pad-nakshatra-to-13
  "Pad nakshatra to exactly 13 chars with trailing dashes
  
  The nakshatra section is 13 chars total (nakshatra + dashes).
  For u_bhadrapada (12 chars): u_bhadrapada- = 13 chars
  For mula (4 chars): mula--------- = 13 chars (4 + 9 dashes)"
  [nakshatra]
  (let [padding-needed (- 13 (count nakshatra))]
    (str nakshatra (apply str (repeat padding-needed "-")))))

(defn pad-team-to-17
  "Pad full team name (teamXX10) to exactly 17 chars with LEADING dashes
  
  The team section is 17 chars total (padding dashes + 2 fixed dashes + teamname).
  For teamstructure10 (15 chars): --teamstructure10 = 17 chars (0 padding + 2 fixed + 15)
  For teamflow10 (10 chars): -------teamflow10 = 17 chars (5 padding + 2 fixed + 10)"
  [team-base]
  (let [full-team (str "team" team-base "10")
        padding-needed (- 15 (count full-team))
        total-dashes (+ 2 padding-needed)]
    (str (apply str (repeat total-dashes "-")) full-team)))

;; =============================================================================
;; GRAINTIME GENERATION (76-CHAR FORMAT)
;; =============================================================================

(defn generate-76char-graintime
  "Generate exactly 76-char graintime with padded nakshatra and team
  
  Format: 12025-10-25--0051-PDT--moon-{nak13}asc-pisc29-sun-05h-{team17}
  
  Components:
    12025-10-25 = 11 chars (date)
    --0051 = 6 chars (time with separator)
    -PDT = 4 chars (timezone with separator)
    --moon- = 7 chars (moon label)
    {nakshatra-13} = 13 chars (padded, includes dash after)
    asc-pisc29 = 10 chars (ascendant)
    -sun-05h = 8 chars (sun house)
    - = 1 char (separator before team)
    {team-17} = 17 chars (padded, includes 2 dashes before)
    
  Total: 11+6+4+7+13+10+8+1+17 = 77... wait, let me recount
  
  Actually the format is:
    12025-10-25--0051-PDT--moon-{nak13}asc-pisc29-sun-05h-{team17}
    
  Let me count the fixed parts:
    12025-10-25--0051-PDT--moon- = 29 chars
    asc-pisc29-sun-05h- = 19 chars
    Total fixed: 48 chars
    Variable: 13 (nak) + 17 (team) = 30 chars
    Total: 48 + 30 = 78 chars (NOT 76!)
    
  Wait, let me check the working example again...
  12025-10-25--0051-PDT--moon-u_bhadrapada-asc-pisc29-sun-05h--teamstructure10
  
  Breaking it down:
    12025-10-25--0051-PDT--moon- = 29 chars
    u_bhadrapada- = 13 chars
    asc-pisc29-sun-05h- = 19 chars
    -teamstructure10 = 17 chars
    Total: 29 + 13 + 19 + 17 = 78 chars
    
  But wc says 76! Let me recount more carefully...
  
  Actually the correct breakdown is:
    12025-10-25--0051-PDT--moon- = 29 chars
    {nakshatra-13} = 13 chars (includes 1 dash after)
    asc-pisc29-sun-05h- = 19 chars  
    {team-17} = 17 chars (2 dashes before + up to 15 char name)
    
  Wait, that's still 78. Let me just use the working format as-is."
  [nakshatra team-base]
  (let [padded-nak (pad-nakshatra-to-13 nakshatra)
        padded-team (pad-team-to-17 team-base)]
    (format "12025-10-25--0051-PDT--moon-%sasc-pisc29-sun-05h%s"
            padded-nak padded-team)))

;; =============================================================================
;; TESTS
;; =============================================================================

(deftest test-longest-combination
  (testing "Longest nakshatra + longest team = exactly 76 chars"
    (let [graintime (generate-76char-graintime "u_bhadrapada" "structure")
          len (count graintime)]
      (println (str "\n🔍 LONGEST: " graintime " (" len " chars)"))
      (is (= 76 len)
          (str "Expected 76 chars, got " len ": " graintime)))))

(deftest test-shortest-combination
  (testing "Shortest nakshatra + shortest team = exactly 76 chars"
    (let [graintime (generate-76char-graintime "mula" "flow")
          len (count graintime)]
      (println (str "🔍 SHORTEST: " graintime " (" len " chars)"))
      (is (= 76 len)
          (str "Expected 76 chars, got " len ": " graintime)))))

(deftest test-all-27-nakshatras
  (testing "All 27 nakshatras with teamstructure10 = 76 chars"
    (doseq [nak all-nakshatras-abbreviated]
      (let [graintime (generate-76char-graintime nak "structure")
            len (count graintime)]
        (is (= 76 len)
            (str "Nakshatra '" nak "' failed: " len " chars - " graintime))))))

(deftest test-all-14-teams
  (testing "All 14 teams with jyeshtha nakshatra = 76 chars"
    (doseq [team all-team-names]
      (let [graintime (generate-76char-graintime "jyeshtha" team)
            len (count graintime)]
        (is (= 76 len)
            (str "Team '" team "' failed: " len " chars - " graintime))))))

(deftest test-all-378-combinations
  (testing "ALL 378 combinations (27 × 14) = exactly 76 chars"
    (let [failures (atom [])]
      (doseq [nak all-nakshatras-abbreviated
              team all-team-names]
        (let [graintime (generate-76char-graintime nak team)
              len (count graintime)]
          (when (not= 76 len)
            (swap! failures conj {:nakshatra nak :team team :length len :graintime graintime}))))
      (when (seq @failures)
        (println (str "\n❌ " (count @failures) " FAILURES:"))
        (doseq [f @failures]
          (println (format "  %s + %s = %d chars: %s" 
                          (:nakshatra f) (:team f) (:length f) (:graintime f)))))
      (is (empty? @failures)
          (str (count @failures) " combinations failed to equal 76 chars")))))

(deftest display-edge-cases
  (testing "Display edge cases for visual verification"
    (println "\n🎯 EDGE CASE GRAINTIMES (ALL 76 CHARS):")
    (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    (let [edge-cases [["u_bhadrapada" "structure" "Longest both"]
                      ["u_bhadrapada" "flow" "Longest nak, shortest team"]
                      ["mula" "structure" "Shortest nak, longest team"]
                      ["mula" "flow" "Shortest both"]
                      ["jyeshtha" "structure" "Current nak, current team"]
                      ["p_bhadrapada" "precision" "Long both"]
                      ["vishakha" "transform" "Medium both"]]]
      (doseq [[nak team desc] edge-cases]
        (let [graintime (generate-76char-graintime nak team)]
          (println (format "%-30s (%2d): %s" desc (count graintime) graintime)))))
    (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    (is true))) ;; Always pass, display only

(comment
  ;; Run all tests
  (clojure.test/run-tests 'graintime.all-76char-combinations-test)
  
  ;; Check specific combination
  (generate-76char-graintime "u_bhadrapada" "structure")
  (generate-76char-graintime "mula" "flow")
  
  ;; Display all team lengths
  (doseq [team all-team-names]
    (let [full (str "team" team "10")]
      (println (format "%-20s %2d chars" full (count full)))))
  
  ;; Display all nakshatra lengths
  (doseq [nak all-nakshatras-abbreviated]
    (println (format "%-15s %2d chars" nak (count nak)))))

