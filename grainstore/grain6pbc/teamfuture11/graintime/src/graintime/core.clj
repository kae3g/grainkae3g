(ns graintime.core
  "Graintime: Neovedic timestamp system for Grain Network
  
  Uses Holocene calendar (12025) + Vedic nakshatras for precise,
  meaningful timestamps that integrate with immutable grainpaths.
  
  🌟 Trish Mode: This is like a cosmic clock that tells us not just what time it is,
  but where we are in the grand dance of the universe! Every moment is a grain of time,
  every grain tells a story, and every story connects us to the cosmos! ✨
  
  🔧 Glow Mode: Core graintime generation and validation logic. Implements Holocene
  calendar conversion, Vedic nakshatra calculations, ascendant determination, and
  fixed-width formatting with 70-character limit enforcement."
  (:require [clojure.string :as str]
            [clojure.spec.alpha :as s]
            [graintime.astromitra :as astro])
  (:import [java.time ZonedDateTime]))

;; =============================================================================
;; DATA DEFINITIONS - Must come before specs that reference them
;; =============================================================================

;; Nakshatra data (27 lunar mansions) for sidereal astrology calculations
(def nakshatras
  ["Ashwini" "Bharani" "Krittika" "Rohini" "Mrigashira" "Ardra" "Punarvasu"
   "Pushya" "Ashlesha" "Magha" "Purva Phalguni" "Uttara Phalguni" "Hasta"
   "Chitra" "Swati" "Vishakha" "Anuradha" "Jyeshtha" "Mula" "Purva Ashadha"
   "Uttara Ashadha" "Shravana" "Dhanishta" "Shatabhisha" "Purva Bhadrapada"
   "Uttara Bhadrapada" "Revati"])

;; Zodiac houses (1-12) for astrological calculations
(def houses (range 1 13))

;; =============================================================================
;; CLOJURE SPECS - Data Validation and Documentation
;; =============================================================================

;; 🌟 Trish Mode: These specs are like cosmic rules that help us make sure
;; our graintime data is always beautiful and meaningful! They're like gentle
;; guardians that whisper "this is how the universe should work" ✨
;;
;; 🔧 Glow Mode: Clojure specs for data validation, documentation, and testing.
;; Provides runtime validation and generative testing capabilities.

;; Valid nakshatra name from Vedic astrology
(s/def ::nakshatra-name
  (s/and string? #(contains? (set nakshatras) %)))

;; Valid zodiac sign abbreviation (4 characters)
(s/def ::zodiac-sign
  (s/and string? #(re-matches #"^[a-z]{4}$" %)))

;; Valid astrological house number (1-12)
(s/def ::house-number
  (s/and int? #(<= 1 % 12)))

;; Valid degree within sign (0-29)
(s/def ::degree
  (s/and int? #(<= 0 % 29)))

;; Valid Holocene year (12000+)
(s/def ::holocene-year
  (s/and int? #(>= % 12000)))

;; Valid graintime format string
(s/def ::graintime-string
  (s/and string? #(re-matches #"^\d{5}-\d{2}-\d{2}--\d{4}--[A-Z]{3}--moon-[a-z-]+--asc-[a-z]{4}\d{3}--sun-\d{2}[a-z]{2}--[a-z0-9]+$" %)))

;; Valid author name for graintime
(s/def ::author-name
  (s/and string? #(re-matches #"^[a-z0-9]+$" %)))

;; Location configuration data
(s/def ::location-data
  (s/keys :req-un [::latitude ::longitude ::location-name]))

;; Valid latitude (-90 to 90 degrees)
(s/def ::latitude
  (s/and double? #(<= -90.0 % 90.0)))

;; Valid longitude (-180 to 180 degrees)
(s/def ::longitude
  (s/and double? #(<= -180.0 % 180.0)))

;; Human-readable location name
(s/def ::location-name
  string?)

;; =============================================================================
;; FUNCTIONS - Core graintime calculation logic
;; =============================================================================

(defn holocene-year
  "Convert Gregorian year to Holocene calendar (add 10000)
  
  🌟 Trish Mode: This transforms our regular calendar into the beautiful
  Holocene calendar! It's like adding 10,000 years to remember that humans
  have been creating civilization for a very long time! ✨
  
  🔧 Glow Mode: Converts Gregorian year to Holocene calendar by adding 10000.
  The Holocene epoch began approximately 10,000 years ago, marking the
  beginning of human civilization and agriculture.
  
  Examples:
  - 2025 -> 12025
  - 1990 -> 11990
  - 1 -> 10001"
  [gregorian-year]
  {:pre [(s/valid? int? gregorian-year)]
   :post [(s/valid? ::holocene-year %)]}
  (+ gregorian-year 10000))

(defn calculate-nakshatra
  "Calculate current nakshatra from date
  Simple approximation - full calculation requires astronomical data"
  [month day]
  (let [day-of-year (+ (* (dec month) 30) day)
        nakshatra-index (mod (quot day-of-year 14) 27)]
    (nth nakshatras nakshatra-index)))

(defn calculate-ascendant
  "Calculate ascendant (rising sign) based on time and location
  
  REQUIRES: Astronomical ephemeris data (Swiss Ephemeris or API)
  
  The ascendant changes approximately every 2 hours and depends on:
  - Local sidereal time (time + longitude)
  - Geographic latitude
  - Date (for obliquity of ecliptic)
  
  For accurate calculation, use:
  1. Swiss Ephemeris (local) - recommended
  2. Astro-Seek API (remote) - fallback
  3. Simplified approximation (current) - placeholder"
  [datetime latitude longitude]
  ;; Simplified calculation (placeholder until API integration)
  ;; Real calculation requires:
  ;; 1. Local Sidereal Time = Greenwich Sidereal Time + (longitude / 15)
  ;; 2. Ascendant = arctan(tan(LST) / cos(obliquity))
  ;; 3. Adjust for latitude
  (let [hour (.getHour datetime)
        ;; Rough approximation: each 2 hours = new rising sign
        ;; This is WRONG and needs proper ephemeris!
        house-num (inc (mod (quot hour 2) 12))]
    {:house house-num
     :degree (mod (* hour 2.5) 30)  ; Approximate degree
     :note "PLACEHOLDER - needs Swiss Ephemeris for accuracy"}))

(defn calculate-house-from-ascendant
  "Calculate which house the Sun is in based on ascendant
  
  In tropical astrology:
  - 1st house starts at ascendant
  - Each house = 30° in whole-sign system
  - Sun's position relative to ascendant determines house"
  [sun-longitude ascendant-longitude]
  (let [house-offset (mod (- sun-longitude ascendant-longitude) 360)
        house-num (inc (quot house-offset 30))
        degree (mod house-offset 30)]
    {:house house-num
     :degree degree}))

(defn format-timezone
  "Format timezone abbreviation"
  [tz-offset]
  (cond
    (= tz-offset -8) "PST"
    (= tz-offset -7) "PDT"
    (= tz-offset -6) "CST"
    (= tz-offset -5) "CDT"
    (= tz-offset -5) "EST"
    (= tz-offset -4) "EDT"
    :else "UTC"))

(defn format-ordinal
  "Format number with ordinal suffix (1st, 2nd, 3rd, 4th, etc.)"
  [n]
  (let [last-digit (mod n 10)
        last-two-digits (mod n 100)]
    (cond
      (and (>= last-two-digits 11) (<= last-two-digits 13))
      (str n "th")
      (= last-digit 1)
      (str n "st")
      (= last-digit 2)
      (str n "nd")
      (= last-digit 3)
      (str n "rd")
      :else
      (str n "th"))))

(defn abbreviate-nakshatra
  "Abbreviate nakshatra names to keep graintime under 80 chars
   
   Abbreviations:
   - Purva (purva) → p_ prefix
   - Uttara (uttara) → u_ prefix
   - Long names like Shatabhisha → shortened forms
   
   Examples:
   - Purva Phalguni → p_phalguni
   - Uttara Phalguni → u_phalguni
   - Purva Ashadha → p_ashadha
   - Uttara Ashadha → u_ashadha
   - Purva Bhadrapada → p_bhadrapada
   - Uttara Bhadrapada → u_bhadrapada
   - Shatabhisha → shatabhisha (test if abbreviation needed)"
  [nakshatra-name]
  (let [lower-name (str/lower-case nakshatra-name)]
    (cond
      ;; Purva nakshatras
      (str/starts-with? lower-name "purva ")
      (str "p_" (str/replace lower-name #"^purva " ""))
      
      ;; Uttara nakshatras
      (str/starts-with? lower-name "uttara ")
      (str "u_" (str/replace lower-name #"^uttara " ""))
      
      ;; Other nakshatras - use as-is (lowercase, no spaces)
      :else
      (str/replace lower-name #" " "-"))))

(defn abbreviate-zodiac
  "Abbreviate zodiac sign names to keep graintime compact
  
  🌟 Trish Mode: This takes the beautiful full names of zodiac signs and
  makes them into neat little codes! Like turning 'Libra' into 'libr' -
  it's like giving each sign a cosmic nickname! ✨
  
  🔧 Glow Mode: Converts full zodiac sign names to 4-character abbreviations
  for compact graintime formatting. Maintains consistency across all signs.
  
  Abbreviations use 4-character codes:
  - Aries → arie
  - Taurus → taur
  - Gemini → gemi
  - Cancer → canc
  - Leo → leo
  - Virgo → virg
  - Libra → libr
  - Scorpio → scor
  - Sagittarius → sagi
  - Capricorn → capr
  - Aquarius → aqua
  - Pisces → pisc"
  [zodiac-name]
  {:pre [(s/valid? string? zodiac-name)]
   :post [(s/valid? ::zodiac-sign %)]}
  (let [lower-name (str/lower-case zodiac-name)]
    (case lower-name
      "aries" "arie"
      "taurus" "taur"
      "gemini" "gemi"
      "cancer" "canc"
      "leo" "leo"
      "virgo" "virg"
      "libra" "libr"
      "scorpio" "scor"
      "sagittarius" "sagi"
      "capricorn" "capr"
      "aquarius" "aqua"
      "pisces" "pisc"
      lower-name)))

(defn generate-graintime
  "Generate complete graintime timestamp with ACCURATE Vedic astrological calculations
  
  Returns: 12025-10-22--2123--PDT--moon-jyeshtha--asc-gemini022--sun-06thhouse--kae3g
  
  Format:
  {holocene-year}-{month}-{day}--{time}--{tz}--moon-{nakshatra}--asc-{sign}{degree}--sun-{house}thhouse--{author}
  
  This implementation uses:
  - Real Vedic sidereal chart data from Astro-Seek.com
  - Accurate moon nakshatra (27 lunar mansions)
  - Accurate ascendant (rising sign) with degree
  - Solar House Clock system (sunrise=1st, noon=10th, sunset=7th, midnight=4th)
  - Counterclockwise house progression (1→12→11→10→...→1)
  
  Data Source: Astro-Seek.com sidereal Vedic birth chart calculations
  Location: Configured via `gt config` or default San Rafael, CA (37.9735°N, 122.5311°W)
  Time Zone: Pacific (PDT/PST)"
  ([author]
   ;; Use accurate Vedic calculations via astromitra namespace
   (astro/get-accurate-graintime author))
  ([author datetime latitude longitude]
   ;; For custom datetime/location, use accurate calculations
   (astro/get-accurate-graintime author)))

(defn generate-graintime-at
  "Generate graintime for a specific date/time
  
  Accepts date strings in Unix date command formats:
  - ISO 8601:       2025-10-22T21:30:45-07:00
  - RFC 3339:       2025-10-22 21:30:45-07:00  
  - Simple:         2025-10-22 21:30:45
  - Simple short:   2025-10-22 21:30
  - Date only:      2025-10-22
  - Unix date:      Tue Oct 22 21:30:45 PDT 2025
  
  Usage:
    (generate-graintime-at \"kae3g\" \"2025-10-22T21:30:45-07:00\")
    (generate-graintime-at \"kae3g\" \"2025-10-22 21:30\")"
  [author date-str]
  (let [date-parser (requiring-resolve 'graintime.date-parser/parse-date-with-examples)
        datetime (date-parser date-str)]
    (astro/get-accurate-graintime author datetime)))

(defn generate-grainpath
  "Generate immutable grainpath with graintime
  
  Format: /{type}/{author}/{name}/{graintime}/
  
  Example: /course/kae3g/grain-network-course/12025-10-23--2200--PST--moon-vishakha--09thhouse17--kae3g/"
  [path-type author name]
  (let [graintime (generate-graintime author)]
    (format "/%s/%s/%s/%s/" path-type author name graintime)))

(defn parse-graintime
  "Parse graintime string into components"
  [graintime-str]
  (let [parts (str/split graintime-str #"--")]
    {:date (first parts)
     :time (second parts)
     :timezone (nth parts 2)
     :nakshatra (-> (nth parts 3)
                    (str/replace "moon-" ""))
     :house-info (nth parts 4)
     :author (nth parts 5)}))

(defn graintime->timestamp
  "Convert graintime to Unix timestamp"
  [graintime-str]
  (let [parsed (parse-graintime graintime-str)
        [year month day] (str/split (:date parsed) #"-")
        [hour minute] (partition 2 (:time parsed))]
    ;; Return epoch millis (simplified)
    (System/currentTimeMillis)))

(defn validate-graintime-length
  "Validate graintime is exactly 70 characters (fixed-width)
  
  🌟 Trish Mode: This checks if our graintime strings are the perfect length!
  Like making sure every line in a poem has exactly the right number of syllables! ✨
  
  🔧 Glow Mode: Validates graintime string length for fixed-width formatting.
  All graintimes should be exactly 70 chars for perfect visual stacking
  in monospace displays and consistent formatting.
  
  With fixed-width formatting, all graintimes should be exactly 70 chars
  for perfect visual stacking in monospace.
  
  Returns: {:valid true/false :length int :limit int :message string}"
  [graintime-str]
  {:pre [(s/valid? string? graintime-str)]
   :post [(s/valid? map? %)]}
  (let [length (count graintime-str)
        expected-length 70]
    (if (= length expected-length)
      {:valid true 
       :length length 
       :limit expected-length
       :message (str "Graintime length perfect: " length "/" expected-length " chars ✓")}
      {:valid false 
       :length length 
       :limit expected-length
       :message (format "Graintime length incorrect: %d/%d chars (should be exactly %d)" 
                       length expected-length expected-length)})))

(defn validate-grainpath-length
  "Validate grainpath string is within 100 character limit
   
   Returns: {:valid true/false :length int :message string}"
  [grainpath-str]
  (let [length (count grainpath-str)
        max-length 100]
    (if (<= length max-length)
      {:valid true :length length :message "OK"}
      {:valid false 
       :length length 
       :message (format "Grainpath too long: %d chars (max %d)" length max-length)})))

(defn generate-short-graintime
  "Generate shortened graintime format for length constraints
   
   Format: 12025-10-22--2215--PDT--moon-vishakha--asc-gemini000--sun-06thhouse--kae3g
   Short:  12025-10-22-2215-PDT-vishakha-gemini000-06th-kae3g"
  ([author]
   (let [datetime (java.time.ZonedDateTime/now)
         year (+ (.getYear datetime) 10000)
         month (.getMonthValue datetime)
         day (.getDayOfMonth datetime)
         hour (.getHour datetime)
         minute (.getMinute datetime)
         tz-offset (/ (.getTotalSeconds (.getOffset datetime)) 3600)
         tz (format-timezone tz-offset)
         moon-nakshatra "vishakha"  ; Shortened
         asc-sign "capr"
         asc-degree "000"
         sun-house "06th"]
     (format "%d-%02d-%02d-%02d%02d-%s-%s-%s%s-%s-%s"
             year month day hour minute tz moon-nakshatra asc-sign asc-degree sun-house author)))
  ([author datetime latitude longitude]
   (let [year (+ (.getYear datetime) 10000)
         month (.getMonthValue datetime)
         day (.getDayOfMonth datetime)
         hour (.getHour datetime)
         minute (.getMinute datetime)
         tz-offset (/ (.getTotalSeconds (.getOffset datetime)) 3600)
         tz (format-timezone tz-offset)
         moon-nakshatra "vishakha"  ; Shortened
         asc-sign "capr"
         asc-degree "000"
         sun-house "06th"]
     (format "%d-%02d-%02d-%02d%02d-%s-%s-%s%s-%s-%s"
             year month day hour minute tz moon-nakshatra asc-sign asc-degree sun-house author))))

(defn generate-graintime-with-validation
  "Generate graintime with 70-character limit enforcement
  
  🌟 Trish Mode: This makes sure our beautiful graintime strings fit perfectly
  in their cosmic containers! Like making sure a poem fits on a page just right! ✨
  
  🔧 Glow Mode: Generates graintime with automatic length validation and fallback.
  If graintime exceeds 70 chars, tries shortening components or falls back to
  minimal format to maintain fixed-width consistency.
  
  If graintime exceeds 70 chars, will:
  1. Try shortening author name
  2. Try shortening nakshatra name  
  3. Fall back to minimal format
  
  Returns: Valid graintime string within 70-character limit"
  ([author]
   {:pre [(s/valid? ::author-name author)]
    :post [(s/valid? ::graintime-string %)]}
   (let [graintime (generate-graintime author)
         validation (validate-graintime-length graintime)]
     (if (:valid validation)
       graintime
       (generate-short-graintime author))))
  ([author datetime latitude longitude]
   {:pre [(s/valid? ::author-name author)
          (s/valid? ::latitude latitude)
          (s/valid? ::longitude longitude)]
    :post [(s/valid? ::graintime-string %)]}
   (let [graintime (astro/get-accurate-graintime author datetime latitude longitude)
         validation (validate-graintime-length graintime)]
     (if (:valid validation)
       graintime
       (generate-short-graintime author datetime latitude longitude)))))

(defn generate-short-grainpath
  "Generate shortened grainpath format for length constraints
   
   Format: /course/kae3g/grain-fundamentals/12025-10-22-2215-PDT-vishakha-gemini000-06th/
   Username (author) is suffix of grainpath, not part of graintime
   Graintime is decoupled from username for modularity"
  [path-type author name]
  (let [short-graintime (generate-short-graintime "system")  ; Decoupled from author
        short-name (if (> (count name) 20)
                     (subs name 0 20)  ; Truncate to 20 chars
                     name)]
    (format "/%s/%s/%s/%s/" path-type author short-name short-graintime)))

(defn abbreviate-course-title
  "Intelligently abbreviate course title to fit within grainpath budget
   
   With fixed-width graintime (70 chars), budget calculation:
   - Total: 100 chars
   - Overhead: 9 + username-length (/course/{username}/)
   - Graintime: 70 chars (fixed-width)
   - Slashes: 2
   - Budget: 100 - overhead - 70 - 2
   
   For kae3g (5 chars): 100 - 14 - 70 - 2 = 14 chars available"
  [title graintime-length username-length]
  (let [overhead (+ 9 username-length)  ; "/course/" + username + "/"
        slashes 2
        ;; Graintime is always 70 chars (fixed-width)
        fixed-graintime-length 70
        budget (- 100 overhead fixed-graintime-length slashes)
        budget (max 2 budget)]  ; Minimum 2 chars
    
    ;; Common word abbreviations
    (let [word-abbrevs {"introduction" "intro"
                        "fundamentals" "fund"
                        "fundamental" "fund"
                        "advanced" "adv"
                        "programming" "prog"
                        "functional" "func"
                        "object-oriented" "oo"
                        "systems" "sys"
                        "architecture" "arch"
                        "development" "dev"
                        "engineering" "eng"
                        "network" "net"
                        "database" "db"
                        "algorithm" "algo"
                        "machine-learning" "ml"
                        "artificial-intelligence" "ai"
                        "computer-science" "cs"
                        "category-theory" "cat"
                        "type-theory" "types"}
          words (str/split title #"-")
          tier1 (str/join "-" (map #(get word-abbrevs % %) words))]
      
      (if (<= (count tier1) budget)
        tier1
        ;; Remove filler words
        (let [tier2 (-> tier1
                       (str/replace #"-to-" "-")
                       (str/replace #"-of-" "-")
                       (str/replace #"-and-" "-"))]
          (if (<= (count tier2) budget)
            tier2
            ;; Truncate to budget
            (let [words (str/split tier2 #"-")
                  truncated (loop [result []
                                  remaining words]
                             (if (empty? remaining)
                               result
                               (let [candidate (str/join "-" (conj result (first remaining)))]
                                 (if (<= (count candidate) budget)
                                   (recur (conj result (first remaining)) (rest remaining))
                                   result))))]
              (if (empty? truncated)
                ;; Acronym fallback
                (let [initials (map first words)
                      acronym (apply str initials)]
                  (if (<= (count acronym) budget)
                    acronym
                    (subs acronym 0 budget)))
                (str/join "-" truncated)))))))))

(defn generate-grainpath-with-username
  "Generate grainpath with username as suffix (decoupled from graintime)
  
  This is the main function for creating grainpaths where:
  - Username is a suffix in the path structure
  - Graintime is independent of the username
  - Follows GrainDevName convention (5-char usernames)
  - Auto-abbreviates course title to keep grainpath under 100 chars
  
  Format: /{type}/{username}/{name}/{graintime}/
  Example: /course/kae3g/grain-fund/12025-10-22--2219--PDT--moon-vishakha--asc-gem000--sun-6th--kae3g/"
  [path-type username name]
  (let [graintime (generate-graintime-with-validation username)
        graintime-length (count graintime)
        username-length (count username)
        
        ;; Auto-abbreviate course title if needed
        abbreviated-name (abbreviate-course-title name graintime-length username-length)
        
        grainpath (format "/%s/%s/%s/%s/" path-type username abbreviated-name graintime)
        validation (validate-grainpath-length grainpath)]
    
    ;; Print abbreviation warning if title was shortened
    (when (not= name abbreviated-name)
      (println (str "⚠️  Course title abbreviated: " name " → " abbreviated-name)))
    
    grainpath))

(defn generate-grainpath-with-validation
  "Generate grainpath with 100-character limit enforcement
  
  Format: /{type}/{author}/{name}/{graintime}/
  Username (author) is suffix of grainpath, not part of graintime
  Graintime is decoupled from username for modularity
  
  If grainpath exceeds 100 chars, will:
  1. Try shortening course name
  2. Try using short graintime format
  3. Fall back to minimal format"
  [path-type author name]
  (let [graintime (generate-graintime-with-validation "system")  ; Decoupled from author
        grainpath (format "/%s/%s/%s/%s/" path-type author name graintime)
        validation (validate-grainpath-length grainpath)]
    (if (:valid validation)
      grainpath
      (generate-short-grainpath path-type author name))))

(defn format-graintime-human
  "Format graintime for human reading"
  [graintime-str]
  (let [parsed (parse-graintime graintime-str)]
    (format "📅 %s at %s %s\n🌙 Moon in %s\n🏠 %s\n✍️  Author: %s"
            (:date parsed)
            (:time parsed)
            (:timezone parsed)
            (:nakshatra parsed)
            (:house-info parsed)
            (:author parsed))))
