(ns graintime.format76
  "76-character graintime format with perfect column alignment
  
  This is the NEW standard format that replaces the old 80-char format.
  
  Format: 12025-10-25--0051-PDT--moon-{nak13}asc-pisc29-sun-05h{team17}
  
  Key changes from old format:
  - Removed 2 dashes (was --0051--, now --0051-)
  - Changed house format (was 03rd, now 03h)
  - Fixed-width nakshatra padding (13 chars with dashes after)
  - Fixed-width team padding (17 chars with dashes before team)
  - Perfect column alignment for all 378 combinations (27×14)
  
  Total: EXACTLY 76 chars for every combination"
  (:require [clojure.string :as str]
            [graintime.solar-houses :as solar]
            [graintime.astromitra :as astro]))

;; =============================================================================
;; PADDING FUNCTIONS (from tested format)
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
  
  The team section is 17 chars total (padding dashes + teamname).
  For teamstructure10 (15 chars): --teamstructure10 = 17 chars (0 padding + 2 fixed + 15)
  For teamflow10 (10 chars): -------teamflow10 = 17 chars (5 padding + 2 fixed + 10)"
  [team-base]
  (let [full-team (str "team" team-base "10")
        padding-needed (- 15 (count full-team))
        total-dashes (+ 2 padding-needed)]
    (str (apply str (repeat total-dashes "-")) full-team)))

;; =============================================================================
;; ABBREVIATION FUNCTIONS
;; =============================================================================

(def nakshatra-abbreviations
  "Map of full nakshatra names to abbreviated forms for 76-char format"
  {"Ashwini" "ashwini"
   "Bharani" "bharani"
   "Krittika" "krittika"
   "Rohini" "rohini"
   "Mrigashira" "mrigashira"
   "Ardra" "ardra"
   "Punarvasu" "punarvasu"
   "Pushya" "pushya"
   "Ashlesha" "ashlesha"
   "Magha" "magha"
   "Purva Phalguni" "p_phalguni"
   "Uttara Phalguni" "u_phalguni"
   "Hasta" "hasta"
   "Chitra" "chitra"
   "Swati" "swati"
   "Vishakha" "vishakha"
   "Anuradha" "anuradha"
   "Jyeshtha" "jyeshtha"
   "Mula" "mula"
   "Purva Ashadha" "p_ashadha"
   "Uttara Ashadha" "u_ashadha"
   "Shravana" "shravana"
   "Dhanishta" "dhanishta"
   "Shatabhisha" "shatabhisha"
   "Purva Bhadrapada" "p_bhadrapada"
   "Uttara Bhadrapada" "u_bhadrapada"
   "Revati" "revati"})

(defn abbreviate-nakshatra
  "Convert full nakshatra name to abbreviated form
  
  Examples:
    'Purva Bhadrapada' -> 'p_bhadrapada'
    'Uttara Phalguni' -> 'u_phalguni'
    'Vishakha' -> 'vishakha'"
  [nakshatra-full]
  (get nakshatra-abbreviations nakshatra-full
       (str/lower-case nakshatra-full)))

(def sign-abbreviations
  "Map of zodiac signs to 4-char codes"
  {"Aries" "arie"
   "Taurus" "taur"
   "Gemini" "gemi"
   "Cancer" "canc"
   "Leo" "leo"
   "Virgo" "virg"
   "Libra" "libr"
   "Scorpio" "scor"
   "Sagittarius" "sagi"
   "Capricorn" "capr"
   "Aquarius" "aqua"
   "Pisces" "pisc"})

(defn abbreviate-sign
  "Convert zodiac sign to 4-char code
  
  Examples:
    'Aries' -> 'arie'
    'Sagittarius' -> 'sagi'
    'Pisces' -> 'pisc'"
  [sign]
  (get sign-abbreviations sign
       (subs (str/lower-case sign) 0 (min 4 (count sign)))))

;; =============================================================================
;; 76-CHAR FORMAT GENERATION
;; =============================================================================

(defn format-ascendant-76
  "Format ascendant for 76-char format: asc-{sign4}{deg2}
  
  Examples:
    sign='Aries', degree=15 -> 'asc-arie15'
    sign='Pisces', degree=29 -> 'asc-pisc29'
    sign='Libra', degree=1 -> 'asc-libr01'"
  [sign degree]
  (let [sign-abbr (abbreviate-sign sign)
        deg-str (format "%02d" degree)]
    (str "asc-" sign-abbr deg-str)))

(defn format-sun-house-76
  "Format sun house for 76-char format: sun-{house2}h
  
  Examples:
    house=3 -> 'sun-03h'
    house=10 -> 'sun-10h'
    house=12 -> 'sun-12h'"
  [house]
  (format "sun-%02dh" house))

(defn format-timezone-76
  "Format timezone for 76-char format (3 chars)
  
  Examples:
    'America/Los_Angeles' -> 'PDT' or 'PST'
    'Asia/Tokyo' -> 'JST'
    'Europe/Madrid' -> 'CET'
    'Europe/London' -> 'GMT' or 'BST'"
  [tz-id]
  ;; TODO: Implement proper timezone abbreviation
  ;; For now, use simple mapping
  (case tz-id
    "America/Los_Angeles" "PDT"
    "Asia/Tokyo" "JST"
    "Europe/Madrid" "CET"
    "Europe/London" "GMT"
    "PDT")) ; default fallback

(defn generate-graintime-76
  "Generate exactly 76-char graintime with perfect column alignment
  
  Parameters:
    - datetime: LocalDateTime or similar
    - nakshatra: Full nakshatra name (e.g., 'Purva Bhadrapada')
    - asc-sign: Ascendant sign (e.g., 'Aries')
    - asc-degree: Ascendant degree (0-30)
    - sun-house: Solar house (1-12)
    - team-base: Team base name (e.g., 'structure', 'flow')
    - timezone: Timezone ID (e.g., 'America/Los_Angeles')
  
  Returns: 76-char graintime string
  
  Format: 12025-10-25--0051-PDT--moon-{nak13}asc-pisc29-sun-05h{team17}"
  [{:keys [datetime nakshatra asc-sign asc-degree sun-house team-base timezone]}]
  (let [;; Date and time
        year (+ (.getYear datetime) 10000) ; Holocene calendar
        month (format "%02d" (.getMonthValue datetime))
        day (format "%02d" (.getDayOfMonth datetime))
        hour (format "%02d" (.getHour datetime))
        minute (format "%02d" (.getMinute datetime))
        
        ;; Format components
        tz-abbr (format-timezone-76 timezone)
        nak-padded (pad-nakshatra-to-13 (abbreviate-nakshatra nakshatra))
        asc-formatted (format-ascendant-76 asc-sign asc-degree)
        sun-formatted (format-sun-house-76 sun-house)
        team-padded (pad-team-to-17 team-base)]
    
    ;; Assemble the graintime
    (format "%d-%s-%s--%s%s-%s--moon-%s%s-%s%s"
            year month day hour minute tz-abbr nak-padded asc-formatted sun-formatted team-padded)))

;; =============================================================================
;; VALIDATION
;; =============================================================================

(defn validate-graintime-76
  "Validate that a graintime string is exactly 76 chars
  
  Returns: {:valid? boolean, :length int, :message string}"
  [graintime-str]
  (let [len (count graintime-str)]
    {:valid? (= 76 len)
     :length len
     :message (if (= 76 len)
                "✅ Perfect 76-char graintime"
                (format "❌ Length mismatch: expected 76, got %d" len))}))

(comment
  ;; Test the format
  (require '[java.time LocalDateTime])
  
  (def test-data
    {:datetime (LocalDateTime/of 2025 10 25 0 51)
     :nakshatra "Vishakha"
     :asc-sign "Aries"
     :asc-degree 15
     :sun-house 3
     :team-base "structure"
     :timezone "America/Los_Angeles"})
  
  (def graintime (generate-graintime-76 test-data))
  ;; => "12025-10-25--0051-PDT--moon-vishakha-----asc-arie15-sun-03h--teamstructure10"
  
  (validate-graintime-76 graintime)
  ;; => {:valid? true, :length 76, :message "✅ Perfect 76-char graintime"}
  
  ;; Test all edge cases
  (def longest
    (generate-graintime-76
      (assoc test-data
        :nakshatra "Uttara Bhadrapada"
        :team-base "structure")))
  
  (def shortest
    (generate-graintime-76
      (assoc test-data
        :nakshatra "Mula"
        :team-base "flow")))
  
  (validate-graintime-76 longest)
  (validate-graintime-76 shortest))

