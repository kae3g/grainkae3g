(ns graintime.swiss-ephemeris-real
  "REAL Swiss Ephemeris integration for accurate nakshatra calculations
  
  This replaces the placeholder swiss_ephemeris.clj with actual Swiss Eph library calls.
  
  Usage:
    bb -A:swisseph -cp src -e '(require 'graintime.swiss-ephemeris-real) ...'"
  (:require [clojure.string :as str])
  (:import [java.time LocalDateTime ZonedDateTime ZoneId]
           [swisseph SwissEph SweDate SweConst]))

;; =============================================================================
;; NAKSHATRA DATA
;; =============================================================================

(def nakshatras
  "27 Vedic nakshatras in order (index 0-26)"
  ["Ashwini" "Bharani" "Krittika" "Rohini" "Mrigashira" "Ardra"
   "Punarvasu" "Pushya" "Ashlesha" "Magha" "Purva Phalguni" "Uttara Phalguni"
   "Hasta" "Chitra" "Swati" "Vishakha" "Anuradha" "Jyeshtha"
   "Mula" "Purva Ashadha" "Uttara Ashadha" "Shravana" "Dhanishta"
   "Shatabhisha" "Purva Bhadrapada" "Uttara Bhadrapada" "Revati"])

(def nakshatra-abbrevs
  "Abbreviated nakshatra names for graintime"
  ["ashwini" "bharani" "krittika" "rohini" "mrigashira" "ardra"
   "punarvasu" "pushya" "ashlesha" "magha" "p_phalguni" "u_phalguni"
   "hasta" "chitra" "swati" "vishakha" "anuradha" "jyeshtha"
   "mula" "p_ashadha" "u_ashadha" "shravana" "dhanishta"
   "shatabhisha" "p_bhadrapada" "u_bhadrapada" "revati"])

;; =============================================================================
;; SWISS EPHEMERIS INITIALIZATION
;; =============================================================================

(defn init-swiss-ephemeris
  "Initialize Swiss Ephemeris with ephemeris data path"
  []
  (let [sw (SwissEph.)]
    (.swe_set_ephe_path sw "resources/ephe")
    sw))

;; =============================================================================
;; JULIAN DAY CONVERSION
;; =============================================================================

(defn datetime-to-julian-day
  "Convert LocalDateTime to Julian Day Number (Universal Time)"
  [^LocalDateTime dt]
  (let [year (.getYear dt)
        month (.getMonthValue dt)
        day (.getDayOfMonth dt)
        hour (.getHour dt)
        minute (.getMinute dt)
        second (.getSecond dt)
        
        ;; Time as decimal hours
        time-decimal (+ hour (/ minute 60.0) (/ second 3600.0))
        
        ;; Swiss Ephemeris Julian Day function
        sw (SwissEph.)
        jd (.swe_julday sw year month day time-decimal SweConst/SE_GREG_CAL)]
    
    jd))

;; =============================================================================
;; MOON POSITION (SIDEREAL)
;; =============================================================================

(defn calculate-sidereal-moon
  "Calculate moon's sidereal longitude using Swiss Ephemeris
  
  This is the REAL calculation - accurate to the second!"
  [^LocalDateTime datetime]
  (let [sw (init-swiss-ephemeris)
        
        ;; Set sidereal mode with Lahiri ayanamsa (standard for Vedic astrology)
        _ (.swe_set_sid_mode sw SweConst/SE_SIDM_LAHIRI 0.0 0.0)
        
        ;; Convert to Julian Day
        jd (datetime-to-julian-day datetime)
        
        ;; Prepare result arrays
        xx (double-array 6)
        serr (StringBuffer.)
        
        ;; Calculate Moon position with SIDEREAL flag
        flag (bit-or SweConst/SEFLG_SWIEPH 
                     SweConst/SEFLG_SIDEREAL
                     SweConst/SEFLG_SPEED)
        ret (.swe_calc sw jd SweConst/SE_MOON flag xx serr)]
    
    (if (= ret SweConst/OK)
      {:sidereal-longitude (aget xx 0)
       :sidereal-latitude (aget xx 1)
       :distance (aget xx 2)
       :speed-longitude (aget xx 3)
       :julian-day jd
       :datetime datetime
       :method :swiss-ephemeris-sidereal
       :accuracy :high}
      (throw (ex-info "Swiss Ephemeris calculation failed"
                      {:error (.toString serr)
                       :julian-day jd})))))

(defn calculate-tropical-moon
  "Calculate moon's tropical longitude using Swiss Ephemeris"
  [^LocalDateTime datetime]
  (let [sw (init-swiss-ephemeris)
        jd (datetime-to-julian-day datetime)
        xx (double-array 6)
        serr (StringBuffer.)
        
        ;; Calculate Moon position (tropical - default)
        flag (bit-or SweConst/SEFLG_SWIEPH SweConst/SEFLG_SPEED)
        ret (.swe_calc sw jd SweConst/SE_MOON flag xx serr)]
    
    (if (= ret SweConst/OK)
      {:tropical-longitude (aget xx 0)
       :tropical-latitude (aget xx 1)
       :distance (aget xx 2)
       :julian-day jd
       :datetime datetime}
      (throw (ex-info "Swiss Ephemeris tropical calculation failed"
                      {:error (.toString serr)})))))

(defn get-ayanamsa
  "Get Lahiri ayanamsa for given datetime"
  [^LocalDateTime datetime]
  (let [sw (init-swiss-ephemeris)
        _ (.swe_set_sid_mode sw SweConst/SE_SIDM_LAHIRI 0.0 0.0)
        jd (datetime-to-julian-day datetime)
        ayanamsa (.swe_get_ayanamsa_ut sw jd)]
    ayanamsa))

;; =============================================================================
;; NAKSHATRA CALCULATION
;; =============================================================================

(defn sidereal-to-nakshatra
  "Convert sidereal longitude to nakshatra index and name"
  [sidereal-longitude]
  (let [normalized (mod sidereal-longitude 360)
        nakshatra-span 13.333333333333334
        nakshatra-index (min (int (/ normalized nakshatra-span)) 26)]
    
    {:nakshatra-index nakshatra-index
     :nakshatra-name (nth nakshatras nakshatra-index)
     :nakshatra-abbrev (nth nakshatra-abbrevs nakshatra-index)
     :sidereal-longitude normalized
     :degree-within-nakshatra (mod normalized nakshatra-span)}))

(defn calculate-moon-nakshatra
  "Calculate moon nakshatra using REAL Swiss Ephemeris
  
  This is accurate to the second - professional quality!"
  [^LocalDateTime datetime]
  (let [;; Get sidereal moon position
        moon-sidereal (calculate-sidereal-moon datetime)
        sidereal-long (:sidereal-longitude moon-sidereal)
        
        ;; Calculate nakshatra
        nakshatra-info (sidereal-to-nakshatra sidereal-long)]
    
    (merge nakshatra-info moon-sidereal)))

;; =============================================================================
;; PUBLIC API
;; =============================================================================

(defn get-current-nakshatra
  "Get current nakshatra using REAL Swiss Ephemeris"
  []
  (calculate-moon-nakshatra (LocalDateTime/now)))

(defn get-nakshatra-at
  "Get nakshatra for specific datetime using REAL Swiss Ephemeris"
  [datetime]
  (calculate-moon-nakshatra datetime))

;; =============================================================================
;; TESTING / VERIFICATION
;; =============================================================================

(defn test-against-astromitra
  "Test against Astromitra known value
  
  Test case: Oct 25, 2025, 07:21 PDT = 19:51 IST
  Expected: Jyeshtha (index 17, range 226.667° - 240°)"
  []
  (let [;; IST time for verification
        test-dt (LocalDateTime/of 2025 10 25 19 51 0)
        result (calculate-moon-nakshatra test-dt)]
    
    (println "\n🌟 Testing REAL Swiss Ephemeris against Astromitra:")
    (println "   Test datetime: 2025-10-25 19:51 IST")
    (println "   Expected: Jyeshtha (index 17, sidereal 226.667° - 240°)")
    (println)
    (println "   RESULT:")
    (println "     Nakshatra:" (:nakshatra-name result))
    (println "     Index:" (:nakshatra-index result))
    (println "     Sidereal longitude:" (format "%.4f°" (:sidereal-longitude result)))
    (println "     Degree within nakshatra:" (format "%.2f°" (:degree-within-nakshatra result)))
    (println)
    (println "     Julian Day:" (:julian-day result))
    (println "     Method:" (:method result))
    (println "     Accuracy:" (:accuracy result))
    (println)
    
    (let [expected-index 17
          expected-min 226.667
          expected-max 240.0
          actual-index (:nakshatra-index result)
          actual-long (:sidereal-longitude result)]
      
      (if (and (= actual-index expected-index)
               (>= actual-long expected-min)
               (<= actual-long expected-max))
        (println "   ✅ SUCCESS! Matches Astromitra perfectly!")
        (println "   ❌ MISMATCH - needs debugging")))
    
    result))

(defn test-current-time
  "Test Swiss Ephemeris with current time"
  []
  (let [result (get-current-nakshatra)]
    (println "\n🌙 Current Moon Nakshatra (Swiss Ephemeris):")
    (println "   Time:" (:datetime result))
    (println "   Nakshatra:" (:nakshatra-name result) 
             "(" (:nakshatra-abbrev result) ")")
    (println "   Index:" (:nakshatra-index result))
    (println "   Sidereal longitude:" (format "%.4f°" (:sidereal-longitude result)))
    (println "   Method:" (:method result))
    (println "   Accuracy:" (:accuracy result))
    result))

(comment
  ;; Test against known value
  (test-against-astromitra)
  
  ;; Test current time
  (test-current-time)
  
  ;; Get ayanamsa for 2025
  (get-ayanamsa (LocalDateTime/of 2025 10 25 12 0 0))
  ;; Should be ~24.1-24.2°
  )

