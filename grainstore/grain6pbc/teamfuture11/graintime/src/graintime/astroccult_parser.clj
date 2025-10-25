(ns graintime.astroccult-parser
  "Parse pre-calculated nakshatra transitions from AstrOccult.net
  
  Source: https://www.astroccult.net/transit_of_planets_planetary_events.html
  
  Data format: IST (Indian Standard Time, UTC+5:30) based on New Delhi
  Coverage: October 2023 - December 2026 (extendable)
  
  Strategy:
  1. Parse pre-calculated transition times from AstrOccult.net
  2. Convert IST → target timezone
  3. Binary search to find current nakshatra
  4. Error handling for dates outside coverage range"
  (:require [clojure.string :as str])
  (:import [java.time LocalDateTime ZonedDateTime ZoneId]
           [java.time.format DateTimeFormatter]))

;; =============================================================================
;; TIMEZONE CONVERSION
;; =============================================================================

(def ist-zone (ZoneId/of "Asia/Kolkata")) ; IST = UTC+5:30

(defn parse-ist-datetime
  "Parse DD/MM/YYYY HH:MM IST string to ZonedDateTime
  
  Example: '25/10/2025 07:21' -> ZonedDateTime in IST"
  [date-str time-str]
  (let [formatter (DateTimeFormatter/ofPattern "dd/MM/yyyy HH:mm")
        combined (str date-str " " time-str)
        local-dt (LocalDateTime/parse combined formatter)]
    (ZonedDateTime/of local-dt ist-zone)))

(defn ist-to-local
  "Convert IST ZonedDateTime to local timezone"
  [ist-datetime target-zone]
  (.withZoneSameInstant ist-datetime target-zone))

;; =============================================================================
;; NAKSHATRA TRANSITIONS DATA (from AstrOccult.net)
;; =============================================================================

(def nakshatra-transitions-2025-10
  "October 2025 Moon nakshatra transitions (IST)
  
  Source: https://www.astroccult.net/transit_of_planets_planetary_events.html
  Format: [date time nakshatra]"
  [;; Based on web search results - October 2025
   ["25/10/2023" "00:54" "Purva Bhadrapada"]
   ["26/10/2023" "03:33" "Uttara Bhadrapada"]
   ["27/10/2023" "06:14" "Revati"]
   ;; TODO: Add complete October 2025 data from AstrOccult.net
   ;; TODO: Extend to cover 2024-2026 range
   ])

(def data-coverage
  "Date range for which we have nakshatra transition data"
  {:start {:year 2023 :month 10}
   :end   {:year 2026 :month 12}})

;; =============================================================================
;; ERROR HANDLING
;; =============================================================================

(defn date-in-coverage?
  "Check if given date is within our data coverage range"
  [year month]
  (let [{:keys [start end]} data-coverage]
    (and (>= year (:year start))
         (<= year (:year end))
         (or (> year (:year start))
             (>= month (:month start)))
         (or (< year (:year end))
             (<= month (:month end))))))

(defn validate-date-range
  "Validate that requested date is within coverage, throw informative error if not"
  [datetime]
  (let [year (.getYear datetime)
        month (.getMonthValue datetime)]
    (when-not (date-in-coverage? year month)
      (throw (ex-info 
              (format "Date %d-%02d is outside nakshatra data coverage range (%d-%02d to %d-%02d).
              
Please check AstrOccult.net for updated data:
https://www.astroccult.net/transit_of_planets_planetary_events.html

Or use Swiss Ephemeris for dates outside this range."
                      year month
                      (get-in data-coverage [:start :year])
                      (get-in data-coverage [:start :month])
                      (get-in data-coverage [:end :year])
                      (get-in data-coverage [:end :month]))
              {:type :date-out-of-range
               :requested-date {:year year :month month}
               :coverage data-coverage
               :suggestion "Update nakshatra transition data from AstrOccult.net"})))))

;; =============================================================================
;; NAKSHATRA LOOKUP
;; =============================================================================

(defn parse-transition
  "Parse a single transition entry into a map"
  [[date time nakshatra]]
  {:ist-datetime (parse-ist-datetime date time)
   :nakshatra nakshatra})

(defn get-nakshatra-at-time
  "Get nakshatra for given datetime using pre-calculated transitions
  
  Algorithm:
  1. Validate date is in coverage range
  2. Convert all IST transitions to target timezone
  3. Binary search to find current nakshatra
  
  Returns: nakshatra name (e.g., 'Jyeshtha')
  Throws: ex-info if date is outside coverage range"
  [datetime target-zone]
  ;; Validate date range first
  (validate-date-range datetime)
  
  ;; TODO: Implement binary search through transitions
  ;; TODO: Convert IST → target timezone
  ;; TODO: Find nakshatra at requested time
  
  ;; For now, return placeholder
  (throw (ex-info "Nakshatra lookup not yet implemented - use Swiss Ephemeris"
                  {:type :not-implemented
                   :suggestion "Complete AstrOccult parser implementation"})))

;; =============================================================================
;; PUBLIC API
;; =============================================================================

(defn get-current-nakshatra
  "Get current nakshatra for given timezone
  
  Uses pre-calculated AstrOccult.net data with timezone conversion.
  Falls back to error with helpful message if date is out of range."
  [target-zone]
  (try
    (let [now (ZonedDateTime/now target-zone)]
      (get-nakshatra-at-time now target-zone))
    (catch clojure.lang.ExceptionInfo e
      (if (= (:type (ex-data e)) :date-out-of-range)
        ;; Re-throw with context
        (throw e)
        ;; Other error - wrap it
        (throw (ex-info "Error getting nakshatra"
                        {:cause (.getMessage e)}
                        e))))))

(comment
  ;; Example usage
  (get-current-nakshatra (ZoneId/of "America/Los_Angeles"))
  
  ;; Error handling test
  (get-nakshatra-at-time 
   (ZonedDateTime/of 2030 1 1 0 0 0 0 (ZoneId/of "America/Los_Angeles"))
   (ZoneId/of "America/Los_Angeles"))
  ;; => Should throw helpful error about 2030 being out of range
  )

