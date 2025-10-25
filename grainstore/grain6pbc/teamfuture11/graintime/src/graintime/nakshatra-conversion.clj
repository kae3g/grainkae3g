(ns graintime.nakshatra-conversion
  "Convert Western astrology moon position to Vedic nakshatra"
  (:require [clojure.string :as str]))

;; Each nakshatra spans 13°20' (13.333... degrees)
;; 360° / 27 nakshatras = 13.333... degrees per nakshatra
(def nakshatra-degree-span 13.333333333333334)

;; Nakshatra names in Sanskrit (0-26)
(def nakshatras
  ["Ashwini" "Bharani" "Krittika" "Rohini" "Mrigashira" "Ardra"
   "Punarvasu" "Pushya" "Ashlesha" "Magha" "Purva Phalguni" "Uttara Phalguni"
   "Hasta" "Chitra" "Swati" "Vishakha" "Anuradha" "Jyeshtha"
   "Mula" "Purva Ashadha" "Uttara Ashadha" "Shravana" "Dhanishta"
   "Shatabhisha" "Purva Bhadrapada" "Uttara Bhadrapada" "Revati"])

;; Abbreviated nakshatra names for graintime (max 12 chars)
(def nakshatra-abbrevs
  ["ashwini" "bharani" "krittika" "rohini" "mrigashira" "ardra"
   "punarvasu" "pushya" "ashlesha" "magha" "purva_phalguni" "uttara_phalguni"
   "hasta" "chitra" "swati" "vishakha" "anuradha" "jyeshtha"
   "mula" "purva_ashadha" "uttara_ashadha" "shravana" "dhanishta"
   "shatabhisha" "purva_bhadrapada" "uttara_bhadrapada" "revati"])

;; Western zodiac signs and their degree ranges (0-360°)
(def zodiac-signs
  {"Aries" [0 30]
   "Taurus" [30 60]
   "Gemini" [60 90]
   "Cancer" [90 120]
   "Leo" [120 150]
   "Virgo" [150 180]
   "Libra" [180 210]
   "Scorpio" [210 240]
   "Sagittarius" [240 270]
   "Capricorn" [270 300]
   "Aquarius" [300 330]
   "Pisces" [330 360]})

;; Nakshatra degree ranges (sidereal zodiac, 0-360°)
;; Each nakshatra spans 13°20' (13.333... degrees)
(def nakshatra-ranges
  (mapv (fn [i]
          (let [start (* i nakshatra-degree-span)
                end (* (inc i) nakshatra-degree-span)]
            [start end]))
        (range 27)))

;; Convert moon longitude (0-360°) to nakshatra index (0-26)
(defn longitude-to-nakshatra-index
  "Convert moon longitude in degrees to nakshatra index"
  [longitude]
  (let [normalized-longitude (mod longitude 360)
        nakshatra-index (int (/ normalized-longitude nakshatra-degree-span))]
    (min nakshatra-index 26))) ; Ensure we don't exceed 26

;; Get nakshatra name from longitude
(defn longitude-to-nakshatra
  "Convert moon longitude to nakshatra name"
  [longitude]
  (let [index (longitude-to-nakshatra-index longitude)]
    (nth nakshatras index)))

;; Get abbreviated nakshatra name from longitude
(defn longitude-to-nakshatra-abbrev
  "Convert moon longitude to abbreviated nakshatra name"
  [longitude]
  (let [index (longitude-to-nakshatra-index longitude)]
    (nth nakshatra-abbrevs index)))

;; Get nakshatra from Western zodiac sign and degree within sign
(defn zodiac-sign-degree-to-nakshatra
  "Convert Western zodiac sign and degree within sign to nakshatra"
  [zodiac-sign degree-within-sign]
  (let [sign-range (get zodiac-signs zodiac-sign)
        [sign-start sign-end] sign-range
        absolute-degree (+ sign-start degree-within-sign)]
    (longitude-to-nakshatra absolute-degree)))

;; Get abbreviated nakshatra from Western zodiac sign and degree
(defn zodiac-sign-degree-to-nakshatra-abbrev
  "Convert Western zodiac sign and degree within sign to abbreviated nakshatra"
  [zodiac-sign degree-within-sign]
  (let [sign-range (get zodiac-signs zodiac-sign)
        [sign-start sign-end] sign-range
        absolute-degree (+ sign-start degree-within-sign)]
    (longitude-to-nakshatra-abbrev absolute-degree)))

;; Test function to show all nakshatra ranges
(defn print-nakshatra-ranges
  "Print all nakshatra degree ranges for verification"
  []
  (doseq [i (range 27)]
    (let [[start end] (nth nakshatra-ranges i)
          nakshatra (nth nakshatras i)
          abbrev (nth nakshatra-abbrevs i)]
      (printf "Nakshatra %2d: %-20s (%s) = %.2f° - %.2f°\n"
              (inc i) nakshatra abbrev start end))))

;; Example usage and testing
(comment
  ;; Test with known values
  (longitude-to-nakshatra 0)        ; Should be "Ashwini"
  (longitude-to-nakshatra 13.33)    ; Should be "Bharani" 
  (longitude-to-nakshatra 26.67)    ; Should be "Krittika"
  (longitude-to-nakshatra 206.5)    ; Should be "Vishakha" (our test case)
  
  ;; Test with Western zodiac
  (zodiac-sign-degree-to-nakshatra "Libra" 26.5) ; Should be "Vishakha"
  
  ;; Print all ranges for verification
  (print-nakshatra-ranges)
  )

;; API for external use
(defn get-nakshatra-from-moon-position
  "Main API function: get nakshatra from moon longitude"
  [moon-longitude]
  {:nakshatra (longitude-to-nakshatra moon-longitude)
   :nakshatra-abbrev (longitude-to-nakshatra-abbrev moon-longitude)
   :nakshatra-index (longitude-to-nakshatra-index moon-longitude)
   :moon-longitude moon-longitude
   :degree-within-nakshatra (mod moon-longitude nakshatra-degree-span)})

;; For integration with existing graintime system
(defn get-current-nakshatra
  "Get current nakshatra for San Rafael, CA (our default location)"
  []
  ;; This would integrate with actual moon position calculation
  ;; For now, return a placeholder that can be replaced with real calculation
  (get-nakshatra-from-moon-position 206.5)) ; Vishakha as example
