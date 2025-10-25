(ns grainneovedic.core
  "Core neovedic timestamp generation.
   
   Generates spiritually-aligned, astronomically-aware timestamps for
   the Grain Network, combining Holocene calendar, Vedic nakshatras,
   and astrological house positions."
  (:require [clojure.string :as str])
  (:import [java.time ZonedDateTime ZoneId]
           [java.time.format DateTimeFormatter]))

;; =============================================================================
;; Constants
;; =============================================================================

(def nakshatras
  "The 27 lunar mansions (nakshatras) in Vedic astrology.
   Each nakshatra spans approximately 13°20' of the zodiac."
  ["ashwini" "bharani" "krittika" "rohini" "mrigashira" "ardra"
   "punarvasu" "pushya" "ashlesha" "magha" "purva-phalguni"
   "uttara-phalguni" "hasta" "chitra" "swati" "vishakha" "anuradha"
   "jyeshtha" "mula" "purva-ashadha" "uttara-ashadha" "shravana"
   "dhanishta" "shatabhisha" "purva-bhadrapada" "uttara-bhadrapada"
   "revati"])

(def nakshatra-span
  "Degrees spanned by each nakshatra"
  (/ 360.0 27))

;; =============================================================================
;; Date/Time Utilities
;; =============================================================================

(defn now
  "Get current ZonedDateTime"
  []
  (ZonedDateTime/now))

(defn ce-to-holocene
  "Convert CE year to Holocene calendar (add 10,000).
   
   Examples:
     2025 CE → 12025 HE
     1 CE → 10001 HE"
  [ce-year]
  (+ ce-year 10000))

(defn format-holocene-date
  "Format date in Holocene calendar: YYYYY-MM-DD"
  [^ZonedDateTime zdt]
  (let [ce-year (.getYear zdt)
        he-year (ce-to-holocene ce-year)
        month (.getMonthValue zdt)
        day (.getDayOfMonth zdt)]
    (format "%05d-%02d-%02d" he-year month day)))

(defn format-time
  "Format time as HHMM (no colons for URL safety)"
  [^ZonedDateTime zdt]
  (let [hour (.getHour zdt)
        minute (.getMinute zdt)]
    (format "%02d%02d" hour minute)))

(defn get-timezone-abbr
  "Get timezone abbreviation (PDT, EST, UTC, etc.)"
  [^ZonedDateTime zdt]
  (-> zdt
      .getZone
      (.getDisplayName java.time.format.TextStyle/SHORT java.util.Locale/US)))

;; =============================================================================
;; Astronomical Calculations
;; =============================================================================

(defn calculate-moon-longitude
  "Calculate approximate moon longitude in degrees (0-360).
   
   This is a simplified calculation based on date/time.
   For production use, integrate with Swiss Ephemeris or similar library.
   
   Current implementation uses day-of-year as proxy for demonstration."
  [^ZonedDateTime zdt]
  (let [day-of-year (.getDayOfYear zdt)
        hour (.getHour zdt)
        minute (.getMinute zdt)
        ;; Approximate: moon moves ~13° per day
        base-position (* day-of-year 13.176358)
        ;; Fine adjustment based on time of day
        time-adjustment (* (+ hour (/ minute 60.0)) 0.549)
        total (+ base-position time-adjustment)]
    (mod total 360)))

(defn longitude-to-nakshatra
  "Convert moon longitude to nakshatra name.
   
   Each nakshatra spans 13°20' (13.333°)"
  [longitude]
  (let [nakshatra-index (int (/ longitude nakshatra-span))]
    (nth nakshatras nakshatra-index)))

(defn calculate-nakshatra
  "Calculate current moon nakshatra"
  [^ZonedDateTime zdt]
  (let [moon-long (calculate-moon-longitude zdt)]
    (longitude-to-nakshatra moon-long)))

(defn calculate-house-position
  "Calculate astrological house and degrees.
   
   This is a simplified calculation. For production use,
   integrate with proper astrological calculation library.
   
   Returns {:house 1-12 :degrees 0-30}"
  [^ZonedDateTime zdt]
  (let [moon-long (calculate-moon-longitude zdt)
        ;; Simplified: use local time as proxy for ascendant
        local-hour (.getHour zdt)
        ;; Each house spans 30°, ascendant rotates with local time
        ascendant-approx (* local-hour 15)  ; 15° per hour
        relative-position (mod (- moon-long ascendant-approx) 360)
        house (inc (int (/ relative-position 30)))
        degrees (int (mod relative-position 30))]
    {:house house :degrees degrees}))

;; =============================================================================
;; Timestamp Generation
;; =============================================================================

(defn generate-timestamp
  "Generate neovedic timestamp.
   
   Options:
     :user       - Username (default: system user)
     :datetime   - ZonedDateTime (default: now)
     :format     - :full | :medium | :short (default: :full)
   
   Examples:
     (generate-timestamp)
     ;; => \"12025-10-22--1945--PDT--moon-vishakha--09thhouse12--system\"
     
     (generate-timestamp {:user \"kae3g\"})
     ;; => \"12025-10-22--1945--PDT--moon-vishakha--09thhouse12--kae3g\"
     
     (generate-timestamp {:user \"kae3g\" :format :short})
     ;; => \"12025-10-22--moon-vishakha--kae3g\""
  ([] (generate-timestamp {}))
  ([{:keys [user datetime format]
     :or {user (System/getProperty "user.name")
          datetime (now)
          format :full}}]
   (let [date (format-holocene-date datetime)
         time (format-time datetime)
         tz (get-timezone-abbr datetime)
         nakshatra (calculate-nakshatra datetime)
         {:keys [house degrees]} (calculate-house-position datetime)]
     (case format
       :short
       (format "%s--moon-%s--%s" date nakshatra user)
       
       :medium
       (format "%s--%s--%s--moon-%s--%s" date time tz nakshatra user)
       
       :full
       (format "%s--%s--%s--moon-%s--%02dthhouse%02d--%s"
               date time tz nakshatra house degrees user)))))

(defn generate-session-filename
  "Generate session filename with neovedic timestamp.
   
   Options:
     :session-number - Session number (required)
     :user          - Username (default: system user)
     :datetime      - ZonedDateTime (default: now)
   
   Example:
     (generate-session-filename {:session-number 804 :user \"kae3g\"})
     ;; => \"SESSION-804-12025-10-22--1945--PDT--moon-vishakha--09thhouse12--kae3g.md\""
  [{:keys [session-number user datetime]
    :or {user (System/getProperty "user.name")
         datetime (now)}}]
  (let [timestamp (generate-timestamp {:user user :datetime datetime :format :full})]
    (format "SESSION-%03d-%s.md" session-number timestamp)))

(defn generate-grainclay-path
  "Generate immutable Grainclay path with neovedic timestamp.
   
   Options:
     :base-path - Base directory (default: \"docs/archive\")
     :filename  - Filename without extension (required)
     :extension - File extension (default: \"md\")
     :user      - Username (default: system user)
     :datetime  - ZonedDateTime (default: now)
   
   Example:
     (generate-grainclay-path {:filename \"setup-guide\" :user \"kae3g\"})
     ;; => \"docs/archive/setup-guide--12025-10-22--1945--PDT--moon-vishakha--09thhouse12--kae3g.md\""
  [{:keys [base-path filename extension user datetime]
    :or {base-path "docs/archive"
         extension "md"
         user (System/getProperty "user.name")
         datetime (now)}}]
  (let [timestamp (generate-timestamp {:user user :datetime datetime :format :full})]
    (format "%s/%s--%s.%s" base-path filename timestamp extension)))

(defn generate-commit-message
  "Generate git commit message with neovedic timestamp.
   
   Args:
     message - Commit message (required)
     options - Map with :session-number, :user, :datetime
   
   Example:
     (generate-commit-message \"feat: implement lexicon sync\"
                              {:session-number 804 :user \"kae3g\"})
     ;; =>
     ;; feat: implement lexicon sync
     ;;
     ;; Timestamp: 12025-10-22--1945--PDT--moon-vishakha--09thhouse12--kae3g
     ;; Session: 804
     ;; Author: kae3g"
  [message {:keys [session-number user datetime]
            :or {user (System/getProperty "user.name")
                 datetime (now)}}]
  (let [timestamp (generate-timestamp {:user user :datetime datetime :format :full})]
    (str/join "\n"
              [message
               ""
               (format "Timestamp: %s" timestamp)
               (when session-number (format "Session: %d" session-number))
               (format "Author: %s" user)])))

;; =============================================================================
;; Parsing
;; =============================================================================

(defn parse-timestamp
  "Parse neovedic timestamp back into components.
   
   Example:
     (parse-timestamp \"12025-10-22--1945--PDT--moon-vishakha--09thhouse12--kae3g\")
     ;; => {:holocene-year 12025
     ;;     :month 10
     ;;     :day 22
     ;;     :time \"1945\"
     ;;     :timezone \"PDT\"
     ;;     :nakshatra \"vishakha\"
     ;;     :house 9
     ;;     :degrees 12
     ;;     :user \"kae3g\"}"
  [timestamp]
  (let [parts (str/split timestamp #"--")]
    (when (>= (count parts) 3)
      (let [[date-part & rest-parts] parts
            [year month day] (str/split date-part #"-")
            parsed {:holocene-year (Integer/parseInt year)
                    :month (Integer/parseInt month)
                    :day (Integer/parseInt day)}]
        (reduce (fn [acc part]
                  (cond
                    ;; Time (4 digits)
                    (re-matches #"\d{4}" part)
                    (assoc acc :time part)
                    
                    ;; Timezone (uppercase letters)
                    (re-matches #"[A-Z]+" part)
                    (assoc acc :timezone part)
                    
                    ;; Moon nakshatra
                    (str/starts-with? part "moon-")
                    (assoc acc :nakshatra (subs part 5))
                    
                    ;; House position
                    (re-matches #"\d{2}thhouse\d{2}" part)
                    (let [[_ house degrees] (re-find #"(\d{2})thhouse(\d{2})" part)]
                      (assoc acc
                             :house (Integer/parseInt house)
                             :degrees (Integer/parseInt degrees)))
                    
                    ;; User (default: last part)
                    :else
                    (assoc acc :user part)))
                parsed
                rest-parts)))))

(defn timestamp->ce-year
  "Convert Holocene year from timestamp to CE year."
  [timestamp]
  (let [{:keys [holocene-year]} (parse-timestamp timestamp)]
    (when holocene-year
      (- holocene-year 10000))))

;; =============================================================================
;; Utilities
;; =============================================================================

(defn url-safe?
  "Check if timestamp is URL-safe (no special characters)"
  [timestamp]
  (re-matches #"[a-zA-Z0-9\-]+" timestamp))

(defn grainclay-compatible?
  "Check if timestamp is compatible with Grainclay immutable paths"
  [timestamp]
  (and (url-safe? timestamp)
       (>= (count timestamp) 20)))  ; Minimum length for uniqueness

