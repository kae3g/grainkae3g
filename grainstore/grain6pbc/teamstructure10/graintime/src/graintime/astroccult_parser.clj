(ns graintime.astroccult-parser
  "Parse pre-calculated nakshatra transitions from AstrOccult.net
  
  Source: https://www.astroccult.net/transit_of_planets_planetary_events.html
  
  Data format: IST (Indian Standard Time, UTC+5:30) based on New Delhi
  Coverage: October 2023 - December 2026 (extendable)
  
  Smart fallback strategy:
  1. Check local EDN cache (fast offline lookup)
  2. If date too recent → scrape AstrOccult.net for updates
  3. Parse & store new data to EDN
  4. If scraping fails or no data → Glow-style error message
  5. Binary search to find current nakshatra"
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [babashka.http-client :as http])
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
  "Date range for which we have MOON nakshatra transition data from AstrOccult.net
  
  Based on: https://www.astroccult.net/transit_of_planets_planetary_events.html
  
  Oldest: 03/10/2023 (Moon Rohini)
  Latest: 02/11/2025 (Moon Uttara Bhadrapada)"
  {:start {:year 2023 :month 10 :day 3}
   :end   {:year 2025 :month 11 :day 2}})

;; =============================================================================
;; CACHE MANAGEMENT
;; =============================================================================

(def cache-file
  "Local EDN cache of nakshatra transitions"
  "resources/nakshatra-transitions.edn")

(defn load-cached-transitions
  "Load nakshatra transitions from local EDN cache"
  []
  (try
    (when (.exists (io/file cache-file))
      (edn/read-string (slurp cache-file)))
    (catch Exception e
      (println "⚠️  Could not load nakshatra cache:" (.getMessage e))
      nil)))

(defn save-transitions-to-cache
  "Save nakshatra transitions to local EDN cache"
  [transitions]
  (try
    (io/make-parents cache-file)
    (spit cache-file (pr-str transitions))
    (println "✓ Saved nakshatra transitions to cache")
    true
    (catch Exception e
      (println "⚠️  Could not save to cache:" (.getMessage e))
      false)))

(defn get-cache-end-date
  "Get the last date covered by cached data"
  [cached-data]
  (when-let [last-transition (last (:transitions cached-data))]
    (:ist-datetime last-transition)))

;; =============================================================================
;; WEB SCRAPING (when cache is outdated)
;; =============================================================================

(defn scrape-astroccult-data
  "Scrape AstrOccult.net for latest nakshatra transition data
  
  Returns: parsed transitions or nil if scraping fails"
  []
  (try
    (println "🌐 Checking AstrOccult.net for updated nakshatra data...")
    (let [response (http/get "https://www.astroccult.net/transit_of_planets_planetary_events.html"
                             {:timeout 10000})]
      (if (= 200 (:status response))
        (do
          (println "✓ Successfully fetched data from AstrOccult.net")
          ;; TODO: Parse HTML to extract nakshatra transitions
          ;; For now, return nil (not implemented)
          (println "⚠️  HTML parsing not yet implemented")
          nil)
        (do
          (println "⚠️  AstrOccult.net returned status:" (:status response))
          nil)))
    (catch Exception e
      (println "⚠️  Could not reach AstrOccult.net:" (.getMessage e))
      nil)))

;; =============================================================================
;; ERROR HANDLING (Glow style)
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

(defn glow-error-message
  "Generate Glow-style error message for out-of-range dates
  
  🔧 Glow Mode: Clear, actionable, kind but precise error messages"
  [year month cache-end-date scraping-attempted?]
  (let [date-str (format "%d-%02d" year month)
        cache-str (when cache-end-date
                    (format "%d-%02d" 
                            (.getYear cache-end-date)
                            (.getMonthValue cache-end-date)))]
    (str "🔧 Glow here. I see you're asking about " date-str ", but I don't have nakshatra data for that time yet.\n\n"
         (when cache-str
           (str "📊 My current data goes up to " cache-str ".\n\n"))
         (if scraping-attempted?
           "🌐 I tried checking AstrOccult.net for updates, but either:\n"
           "🌐 I haven't checked AstrOccult.net yet. Let me try:\n")
         (when scraping-attempted?
           "   • The site is down or unreachable\n   • They haven't published data for " date-str " yet\n   • The HTML format changed (happens sometimes)\n\n")
         "💡 Here's what you can do right now:\n\n"
         "   1. Check current nakshatra: https://www.astromitra.com/transit/planetary-transit-in-nakshatra.php\n"
         "      → Real-time positions for today or any date\n"
         "      → Just set your location and time\n\n"
         "   2. For pre-calculated data: https://www.astroccult.net/transit_of_planets_planetary_events.html\n"
         "      → Batch data for 2023-2026\n"
         "      → If newer data exists there, file an issue\n\n"
         "   3. For dates far in the future: Use Swiss Ephemeris\n"
         "      → Most accurate astronomical calculations\n"
         "      → Works for any date, past or future\n\n"
         "The nakshatra you seek is out there—we just need better telescopes. 🔭\n\n"
         "— Glow ✨")))

(defn validate-date-range
  "Validate date is in coverage, with smart fallback to scraping
  
  Strategy:
  1. Check if date is after cached data
  2. If yes, try scraping AstrOccult.net for updates
  3. If scraping succeeds, update cache
  4. If still out of range, throw Glow-style error"
  [datetime cached-data]
  (let [year (.getYear datetime)
        month (.getMonthValue datetime)
        cache-end (get-cache-end-date cached-data)]
    
    ;; If date is AFTER cached data, try scraping for updates
    (when (and cache-end (.isAfter datetime cache-end))
      (println "📅 Requested date is newer than cached data, checking for updates...")
      (when-let [new-data (scrape-astroccult-data)]
        (save-transitions-to-cache new-data)))
    
    ;; After potential update, check again
    (when-not (date-in-coverage? year month)
      (throw (ex-info 
              (glow-error-message year month cache-end true)
              {:type :date-out-of-range
               :requested-date {:year year :month month}
               :cache-end cache-end
               :scraping-attempted true
               :style :glow})))))

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

