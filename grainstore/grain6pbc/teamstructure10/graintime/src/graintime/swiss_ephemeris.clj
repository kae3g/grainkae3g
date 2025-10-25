(ns graintime.swiss-ephemeris
  "Swiss Ephemeris integration for accurate nakshatra calculations
  
  Formula:
  1. Get tropical moon longitude (Swiss Ephemeris)
  2. Apply Lahiri ayanamsa (~24° for 2025)
  3. Convert to sidereal: sidereal = tropical - ayanamsa
  4. Calculate nakshatra: index = floor(sidereal / 13.333°)
  
  NOTE: This is a placeholder implementation.
  For production, we need the actual Swiss Ephemeris library.
  
  Options:
  1. Use swetest command-line tool (if installed)
  2. Use Java Swiss Ephemeris JAR
  3. Use Python pyswisseph via GraalVM
  
  For now, we'll create the structure and test with known values."
  (:require [clojure.string :as str]
            [clojure.java.shell :as shell])
  (:import [java.time ZonedDateTime LocalDateTime ZoneId]
           [java.time.format DateTimeFormatter]))

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
  "Abbreviated nakshatra names for graintime (max 12 chars)"
  ["ashwini" "bharani" "krittika" "rohini" "mrigashira" "ardra"
   "punarvasu" "pushya" "ashlesha" "magha" "p_phalguni" "u_phalguni"
   "hasta" "chitra" "swati" "vishakha" "anuradha" "jyeshtha"
   "mula" "p_ashadha" "u_ashadha" "shravana" "dhanishta"
   "shatabhisha" "p_bhadrapada" "u_bhadrapada" "revati"])

;; =============================================================================
;; JULIAN DAY CALCULATION
;; =============================================================================

(defn datetime-to-julian-day
  "Convert LocalDateTime to Julian Day Number
  
  Algorithm from Meeus, \"Astronomical Algorithms\""
  [^LocalDateTime dt]
  (let [year (.getYear dt)
        month (.getMonthValue dt)
        day (.getDayOfMonth dt)
        hour (.getHour dt)
        minute (.getMinute dt)
        second (.getSecond dt)
        
        ;; Adjust for January/February
        [y m] (if (< month 3)
                [(dec year) (+ month 12)]
                [year month])
        
        ;; Gregorian calendar correction
        a (int (/ y 100))
        b (+ 2 (- a) (int (/ a 4)))
        
        ;; Julian Day at midnight
        jd (+ (int (* 365.25 (+ y 4716)))
              (int (* 30.6001 (+ m 1)))
              day b -1524.5)
        
        ;; Add time of day
        day-fraction (/ (+ hour (/ minute 60.0) (/ second 3600.0)) 24.0)]
    
    (+ jd day-fraction)))

;; =============================================================================
;; AYANAMSA CALCULATION (Lahiri)
;; =============================================================================

(defn calculate-lahiri-ayanamsa
  "Calculate Lahiri ayanamsa for given Julian Day
  
  Formula based on Swiss Ephemeris Lahiri implementation:
  - Epoch: 1900.0 = ayanamsa 22°27'38.64\"
  - Rate: ~50.26\" per year
  
  For 2025: ~24.1° (approximate)"
  [julian-day]
  (let [;; Days since J2000.0 epoch (Jan 1, 2000, 12:00 UT)
        t (/ (- julian-day 2451545.0) 36525.0)
        
        ;; Lahiri ayanamsa formula (simplified)
        ;; Actual: 23.85° + 0.0135° * t (approximation for near-current dates)
        ayanamsa (+ 23.85 (* 0.0135 t (* 100)))] ; rough approximation
    
    ayanamsa))

;; =============================================================================
;; MOON POSITION (PLACEHOLDER - needs real ephemeris)
;; =============================================================================

(defn calculate-tropical-moon-longitude
  "Calculate moon's tropical longitude
  
  WARNING: This is a PLACEHOLDER using mean motion.
  For production, use actual Swiss Ephemeris library.
  
  Mean motion: ~13.176358° per day
  This will be INACCURATE (as we discovered - 76° error possible)"
  [julian-day]
  (let [;; Days since J2000.0
        days-since-epoch (- julian-day 2451545.0)
        
        ;; Moon's mean longitude at epoch (J2000.0)
        moon-epoch-longitude 134.963396
        
        ;; Mean daily motion
        moon-daily-motion 13.176358
        
        ;; Calculate mean longitude
        mean-longitude (+ moon-epoch-longitude 
                          (* moon-daily-motion days-since-epoch))
        
        ;; Normalize to 0-360°
        normalized (mod mean-longitude 360)]
    
    {:longitude normalized
     :method :mean-motion
     :warning "INACCURATE - Use real Swiss Ephemeris for production"}))

;; =============================================================================
;; NAKSHATRA CALCULATION
;; =============================================================================

(defn sidereal-to-nakshatra
  "Convert sidereal longitude to nakshatra
  
  Each nakshatra = 13.333...° (360° / 27)"
  [sidereal-longitude]
  (let [normalized (mod sidereal-longitude 360)
        nakshatra-span 13.333333333333334
        nakshatra-index (int (/ normalized nakshatra-span))]
    
    {:nakshatra-index nakshatra-index
     :nakshatra-name (nth nakshatras nakshatra-index)
     :nakshatra-abbrev (nth nakshatra-abbrevs nakshatra-index)
     :sidereal-longitude normalized
     :degree-within-nakshatra (mod normalized nakshatra-span)}))

(defn calculate-moon-nakshatra
  "Calculate moon nakshatra for given datetime
  
  WARNING: Uses mean motion (INACCURATE). 
  For production, integrate real Swiss Ephemeris."
  [^LocalDateTime datetime]
  (let [;; Calculate Julian Day
        jd (datetime-to-julian-day datetime)
        
        ;; Get tropical moon position (PLACEHOLDER)
        moon-tropical (calculate-tropical-moon-longitude jd)
        tropical-long (:longitude moon-tropical)
        
        ;; Calculate ayanamsa
        ayanamsa (calculate-lahiri-ayanamsa jd)
        
        ;; Convert to sidereal
        sidereal-long (mod (- tropical-long ayanamsa) 360)
        
        ;; Get nakshatra
        nakshatra-info (sidereal-to-nakshatra sidereal-long)]
    
    (merge nakshatra-info
           {:tropical-longitude tropical-long
            :ayanamsa ayanamsa
            :julian-day jd
            :datetime datetime
            :calculation-method :mean-motion-placeholder
            :accuracy :low
            :note "Replace with real Swiss Ephemeris for production"})))

;; =============================================================================
;; PUBLIC API
;; =============================================================================

(defn get-current-nakshatra
  "Get current nakshatra using Swiss Ephemeris (placeholder implementation)"
  []
  (calculate-moon-nakshatra (LocalDateTime/now)))

(defn get-nakshatra-at
  "Get nakshatra for specific datetime"
  [datetime]
  (calculate-moon-nakshatra datetime))

;; =============================================================================
;; TESTING / VERIFICATION
;; =============================================================================

(defn test-against-known-value
  "Test against known value from Astromitra
  
  Test case: Oct 25, 2025, 07:21 PDT (19:51 IST)
  Expected: Jyeshtha (index 17, range 226.667° - 240°)"
  []
  (let [test-dt (LocalDateTime/of 2025 10 25 19 51 0) ; IST time
        result (calculate-moon-nakshatra test-dt)]
    
    (println "\n🧪 Testing Swiss Ephemeris (placeholder) against known value:")
    (println "   Test datetime: 2025-10-25 19:51 IST")
    (println "   Expected: Jyeshtha (index 17, sidereal 226.667° - 240°)")
    (println)
    (println "   Result:")
    (println "     Nakshatra:" (:nakshatra-name result))
    (println "     Index:" (:nakshatra-index result))
    (println "     Sidereal longitude:" (format "%.2f°" (:sidereal-longitude result)))
    (println "     Tropical longitude:" (format "%.2f°" (:tropical-longitude result)))
    (println "     Ayanamsa:" (format "%.2f°" (:ayanamsa result)))
    (println)
    (println "   ⚠️  Method:" (:calculation-method result))
    (println "   ⚠️  Accuracy:" (:accuracy result))
    (println "   ⚠️  Note:" (:note result))
    (println)
    
    result))

(comment
  ;; Test current nakshatra
  (get-current-nakshatra)
  
  ;; Test against known value
  (test-against-known-value)
  
  ;; Test specific datetime
  (get-nakshatra-at (LocalDateTime/of 2025 10 25 7 30 0))
  )

