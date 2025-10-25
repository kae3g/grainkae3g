(ns grainmock.core
  "Core mocking utilities for Grain Network testing
   
   Provides time-travel, location mocking, and API stubbing for:
   - graintime astronomical calculations
   - grain6 scheduling triggers
   - grainwifi connection management
   - Any time/location-dependent code"
  (:require [clojure.string :as str]))

;;; ╔═══════════════════════════════════════════════════════════════════╗
;;; ║                                                                   ║
;;; ║   🌾  G R A I N M O C K   C O R E                                ║
;;; ║                                                                   ║
;;; ║   Time-Travel Testing for Grain Network                          ║
;;; ║                                                                   ║
;;; ║   "Test at any time, any place, any condition"                   ║
;;; ║                                                                   ║
;;; ╚═══════════════════════════════════════════════════════════════════╝

;; ═══════════════════════════════════════════════════════════════════
;; DYNAMIC VARS (Thread-safe mocking state)
;; ═══════════════════════════════════════════════════════════════════

(def ^:dynamic *mocked-time*
  "Current mocked time (ZonedDateTime) or nil for real time"
  nil)

(def ^:dynamic *mocked-location*
  "Current mocked location {:lat X :lon Y :city \"...\"} or nil for default"
  nil)

(def ^:dynamic *mocked-api-responses*
  "Map of API endpoint → response data for mocking external APIs"
  {})

(def ^:dynamic *mock-mode*
  "Enable/disable mocking globally"
  false)

;; ═══════════════════════════════════════════════════════════════════
;; TIME UTILITIES
;; ═══════════════════════════════════════════════════════════════════

(defn parse-time-string
  "Parse time string to ZonedDateTime
   
   Accepts formats:
   - ISO 8601: 2025-10-23T18:45:00-07:00
   - Simple: 2025-10-23 18:45:00
   - Date only: 2025-10-23 (defaults to noon)"
  [time-str]
  (cond
    ;; ISO 8601 with timezone
    (re-find #"T.*[+-]\d{2}:\d{2}$" time-str)
    (java.time.ZonedDateTime/parse time-str)
    
    ;; Simple datetime (assume Pacific timezone)
    (re-find #"\d{4}-\d{2}-\d{2}\s+\d{2}:\d{2}" time-str)
    (let [[date time] (str/split time-str #"\s+")
          datetime-str (str date "T" time ":00-07:00[America/Los_Angeles]")]
      (java.time.ZonedDateTime/parse datetime-str))
    
    ;; Date only (default to noon Pacific)
    (re-find #"^\d{4}-\d{2}-\d{2}$" time-str)
    (java.time.ZonedDateTime/parse (str time-str "T12:00:00-07:00[America/Los_Angeles]"))
    
    :else
    (throw (ex-info "Invalid time format" {:input time-str}))))

(defn current-time
  "Get current time (real or mocked)"
  []
  (or *mocked-time* (java.time.ZonedDateTime/now)))

(defn current-location
  "Get current location (real or mocked)"
  []
  (or *mocked-location*
      {:lat 37.9735 :lon -122.5311 :city "San Rafael, CA (default)"}))

;; ═══════════════════════════════════════════════════════════════════
;; TIME-TRAVEL MACROS
;; ═══════════════════════════════════════════════════════════════════

(defmacro with-time
  "Execute body with mocked current time
   
   Usage:
     (with-time \"2025-10-23T06:00:00\"
       (graintime/generate-graintime \"test\"))
   
   Expects ascendant to be Gemini/Taurus (early morning)"
  [time-str & body]
  `(binding [*mock-mode* true
             *mocked-time* (parse-time-string ~time-str)]
     ~@body))

(defmacro with-date
  "Execute body with mocked date at noon
   
   Usage:
     (with-date \"2025-10-23\"
       (graintime/generate-graintime \"test\"))"
  [date-str & body]
  `(with-time (str ~date-str " 12:00:00") ~@body))

(defn time-travel-sequence
  "Run function at multiple times and collect results
   
   Usage:
     (time-travel-sequence
       [\"2025-10-23T06:00:00\"
        \"2025-10-23T12:00:00\"
        \"2025-10-23T18:00:00\"]
       #(graintime/generate-graintime \"test\"))
   
   Returns: Vector of results"
  [times-seq f]
  (mapv (fn [t] (with-time t (f))) times-seq))

(defn time-travel-range
  "Run function at hourly intervals and collect results
   
   Usage:
     (time-travel-range
       \"2025-10-23\"
       0 24  ; 0:00 to 23:00
       #(graintime/get-ascendant))"
  [date start-hour end-hour f]
  (let [times (map #(format "%s %02d:00:00" date %) (range start-hour end-hour))]
    (time-travel-sequence times f)))

;; ═══════════════════════════════════════════════════════════════════
;; LOCATION MOCKING
;; ═══════════════════════════════════════════════════════════════════

(def preset-locations
  "Common locations for testing"
  {:caspar-ca {:lat 39.3625 :lon -123.8139 :city "Caspar, CA"}
   :san-rafael {:lat 37.9735 :lon -122.5311 :city "San Rafael, CA"}
   :new-york {:lat 40.7128 :lon -74.0060 :city "New York, NY"}
   :mumbai {:lat 19.0760 :lon 72.8777 :city "Mumbai, India"}
   :tokyo {:lat 35.6762 :lon 139.6503 :city "Tokyo, Japan"}
   :london {:lat 51.5074 :lon -0.1278 :city "London, UK"}
   :sydney {:lat -33.8688 :lon 151.2093 :city "Sydney, Australia"}
   :equator {:lat 0.0 :lon 0.0 :city "Equator (0°,0°)"}
   :north-pole {:lat 89.9 :lon 0.0 :city "North Pole"}})

(defmacro with-location
  "Execute body with mocked location
   
   Usage:
     (with-location {:lat 40.7128 :lon -74.0060 :city \"NYC\"}
       (graintime/generate-graintime \"test\"))
   
   Or use preset:
     (with-location :new-york
       (graintime/generate-graintime \"test\"))"
  [location & body]
  `(let [loc# (if (keyword? ~location)
                (get preset-locations ~location)
                ~location)]
     (binding [*mock-mode* true
               *mocked-location* loc#]
       ~@body)))

;; ═══════════════════════════════════════════════════════════════════
;; API MOCKING
;; ═══════════════════════════════════════════════════════════════════

(defmacro with-mock-api
  "Execute body with mocked API responses
   
   Usage:
     (with-mock-api
       {\"https://astromitra.com/api\"
        {:moon {:nakshatra \"anuradha\" :pada 1}
         :ascendant {:sign \"scorpio\" :degree 22}}}
       (graintime/get-moon-nakshatra))"
  [mock-responses & body]
  `(binding [*mock-mode* true
             *mocked-api-responses* ~mock-responses]
     ~@body))

(defn get-mock-response
  "Get mocked API response for endpoint"
  [endpoint]
  (get *mocked-api-responses* endpoint))

(defn mock-mode?
  "Check if mocking is currently enabled"
  []
  *mock-mode*)

;; ═══════════════════════════════════════════════════════════════════
;; COMBINED MOCKING
;; ═══════════════════════════════════════════════════════════════════

(defmacro with-context
  "Execute body with full mocked context (time + location + API)
   
   Usage:
     (with-context
       {:time \"2025-10-23T18:45:00\"
        :location :caspar-ca
        :api {\"astromitra\" {:moon \"anuradha\" :asc \"scorpio\"}}}
       (graintime/generate-graintime \"test\"))"
  [{:keys [time location api]} & body]
  `(binding [*mock-mode* true
             *mocked-time* (when ~time (parse-time-string ~time))
             *mocked-location* (if (keyword? ~location)
                                 (get preset-locations ~location)
                                 ~location)
             *mocked-api-responses* (or ~api {})]
     ~@body))

;;; ╔═══════════════════════════════════════════════════════════════════╗
;;; ║                                                                   ║
;;; ║   🌾  G R A I N M O C K   C O R E   C O M P L E T E              ║
;;; ║                                                                   ║
;;; ║   Features:                                                       ║
;;; ║   - Time-travel testing (any date/time)                           ║
;;; ║   - Location mocking (anywhere on Earth)                          ║
;;; ║   - API stubbing (astromitra, weather, etc.)                      ║
;;; ║   - Thread-safe (dynamic vars)                                    ║
;;; ║   - Template/personal separation ready                            ║
;;; ║                                                                   ║
;;; ║   now == next + 1 🌾                                              ║
;;; ║                                                                   ║
;;; ╚═══════════════════════════════════════════════════════════════════╝

