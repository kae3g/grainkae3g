(ns graintime.moon-position
  "Simple moon position calculation for nakshatra determination"
  (:require [clojure.string :as str])
  (:import [java.time LocalDateTime]
           [java.time.temporal ChronoUnit]))

;; Simple moon position calculation
;; This is a basic approximation - for production use, integrate Swiss Ephemeris

;; Moon's mean motion: ~13.176358 degrees per day
(def moon-daily-motion 13.176358)

;; Moon's mean longitude at epoch (January 1, 2000, 12:00 UTC)
(def moon-epoch-longitude 134.963396)

;; Convert datetime to Julian Day Number
(defn datetime-to-jd
  "Convert LocalDateTime to Julian Day Number"
  [^LocalDateTime local-datetime]
  (let [epoch (LocalDateTime/of 2000 1 1 12 0 0)
        days-between (.between ChronoUnit/DAYS epoch local-datetime)
        jd (+ 2451545.0 days-between)] ; J2000.0 = 2451545.0
    jd))

;; Calculate moon's mean longitude
(defn calculate-moon-longitude
  "Calculate moon's mean longitude for given datetime"
  [local-datetime]
  (let [jd (datetime-to-jd local-datetime)
        days-since-epoch (- jd 2451545.0)
        mean-longitude (+ moon-epoch-longitude 
                          (* moon-daily-motion days-since-epoch))]
    (mod mean-longitude 360)))

;; Convert moon longitude to zodiac sign and degree
(defn longitude-to-zodiac
  "Convert longitude to zodiac sign and degree within sign"
  [longitude]
  (let [normalized (mod longitude 360)
        sign-index (int (/ normalized 30))
        degree-within-sign (mod normalized 30)
        zodiac-signs ["Aries" "Taurus" "Gemini" "Cancer" "Leo" "Virgo"
                      "Libra" "Scorpio" "Sagittarius" "Capricorn" "Aquarius" "Pisces"]]
    {:zodiac-sign (nth zodiac-signs sign-index)
     :degree-within-sign degree-within-sign
     :absolute-degree normalized}))

;; Get current moon position for San Rafael, CA
(defn get-current-moon-position
  "Get current moon position for San Rafael, CA"
  []
  (let [now (LocalDateTime/now)
        longitude (calculate-moon-longitude now)
        zodiac (longitude-to-zodiac longitude)]
    {:datetime now
     :moon-longitude longitude
     :zodiac-sign (:zodiac-sign zodiac)
     :degree-within-sign (:degree-within-sign zodiac)
     :absolute-degree (:absolute-degree zodiac)}))

;; Get moon position for specific datetime and location
(defn get-moon-position
  "Get moon position for specific datetime"
  [local-datetime]
  (let [longitude (calculate-moon-longitude local-datetime)
        zodiac (longitude-to-zodiac longitude)]
    {:datetime local-datetime
     :moon-longitude longitude
     :zodiac-sign (:zodiac-sign zodiac)
     :degree-within-sign (:degree-within-sign zodiac)
     :absolute-degree (:absolute-degree zodiac)}))

;; Integration with nakshatra conversion
(defn get-current-nakshatra
  "Get current nakshatra for San Rafael, CA"
  []
  (let [moon-pos (get-current-moon-position)
        longitude (:moon-longitude moon-pos)]
    (require 'graintime.nakshatra-conversion)
    ((resolve 'graintime.nakshatra-conversion/get-nakshatra-from-moon-position) longitude)))

;; Test function
(defn test-moon-position
  "Test moon position calculation"
  []
  (let [now (LocalDateTime/now)
        moon-pos (get-current-moon-position)
        nakshatra (get-current-nakshatra)]
    (println "Current time:" now)
    (println "Moon position:" moon-pos)
    (println "Nakshatra:" nakshatra)))

;; Example usage
(comment
  ;; Get current moon position
  (get-current-moon-position)
  
  ;; Get nakshatra for specific datetime
  (let [specific-time (LocalDateTime/of 2025 10 25 0 53 0)
        moon-pos (get-moon-position specific-time)
        longitude (:moon-longitude moon-pos)]
    ((resolve 'graintime.nakshatra-conversion/get-nakshatra-from-moon-position) longitude))
  
  ;; Test current position
  (test-moon-position)
  )
