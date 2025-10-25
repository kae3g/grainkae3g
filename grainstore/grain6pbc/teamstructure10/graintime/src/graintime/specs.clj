(ns graintime.specs
  "Graintime Clojure Specs - Data Validation and Documentation
  
  🌟 Trish Mode: These specs are like cosmic rules that help us make sure
  our graintime data is always beautiful and meaningful! They're like gentle
  guardians that whisper 'this is how the universe should work' ✨
  
  🔧 Glow Mode: Clojure specs for data validation, documentation, and testing.
  Provides runtime validation and generative testing capabilities.
  Inspired by Urbit's 'sur' structure files for shared type definitions."
  (:require [clojure.spec.alpha :as s]))

;; =============================================================================
;; BASIC DATA TYPES
;; =============================================================================

(s/def ::nakshatra-name
  "Valid nakshatra name from Vedic astrology"
  (s/and string? #(contains? #{"Ashwini" "Bharani" "Krittika" "Rohini" "Mrigashira" 
                               "Ardra" "Punarvasu" "Pushya" "Ashlesha" "Magha" 
                               "Purva Phalguni" "Uttara Phalguni" "Hasta" "Chitra" 
                               "Swati" "Vishakha" "Anuradha" "Jyeshtha" "Mula" 
                               "Purva Ashadha" "Uttara Ashadha" "Shravana" 
                               "Dhanishta" "Shatabhisha" "Purva Bhadrapada" 
                               "Uttara Bhadrapada" "Revati"} %)))

(s/def ::zodiac-sign
  "Valid zodiac sign abbreviation (4 characters)"
  (s/and string? #(re-matches #"^[a-z]{4}$" %)))

(s/def ::house-number
  "Valid astrological house number (1-12)"
  (s/and int? #(<= 1 % 12)))

(s/def ::degree
  "Valid degree within sign (0-29)"
  (s/and int? #(<= 0 % 29)))

(s/def ::holocene-year
  "Valid Holocene year (12000+)"
  (s/and int? #(>= % 12000)))

(s/def ::author-name
  "Valid author name for graintime"
  (s/and string? #(re-matches #"^[a-z0-9]+$" %)))

;; =============================================================================
;; GEOGRAPHIC DATA TYPES
;; =============================================================================

(s/def ::latitude
  "Valid latitude (-90 to 90 degrees)"
  (s/and double? #(<= -90.0 % 90.0)))

(s/def ::longitude
  "Valid longitude (-180 to 180 degrees)"
  (s/and double? #(<= -180.0 % 180.0)))

(s/def ::location-name
  "Human-readable location name"
  string?)

(s/def ::location-data
  "Location configuration data"
  (s/keys :req-un [::latitude ::longitude ::location-name]))

;; =============================================================================
;; GRAINTIME FORMAT TYPES
;; =============================================================================

(s/def ::graintime-string
  "Valid graintime format string"
  (s/and string? #(re-matches #"^\d{5}-\d{2}-\d{2}--\d{4}--[A-Z]{3}--moon-[a-z-]+--asc-[a-z]{4}\d{3}--sun-\d{2}[a-z]{2}--[a-z0-9]+$" %)))

(s/def ::grainpath-string
  "Valid grainpath format string"
  (s/and string? #(re-matches #"^/[a-z]+/[a-z0-9]+/[a-z-]+/\d{5}-\d{2}-\d{2}--\d{4}--[A-Z]{3}--moon-[a-z-]+--asc-[a-z]{4}\d{3}--sun-\d{2}[a-z]{2}--[a-z0-9]+/$" %)))

;; =============================================================================
;; ASTROLOGICAL DATA TYPES
;; =============================================================================

(s/def ::ascendant-data
  "Ascendant calculation result"
  (s/keys :req-un [::zodiac-sign ::degree]
          :opt-un [::lst-hours ::oblique-factor ::method]))

(s/def ::solar-house-data
  "Solar house calculation result"
  (s/keys :req-un [::house-number ::degree ::solar-position ::is-day]
          :opt-un [::current-time ::sunrise ::solar-noon ::sunset ::solar-midnight]))

(s/def ::solar-position
  "Valid solar position"
  #{"sunrise" "morning" "noon" "afternoon" "sunset" "evening" "midnight" "night"})

(s/def ::is-day
  "Boolean indicating if it's daytime"
  boolean?)

;; =============================================================================
;; VALIDATION RESULT TYPES
;; =============================================================================

(s/def ::validation-result
  "Generic validation result"
  (s/keys :req-un [::valid ::message]
          :opt-un [::length ::limit]))

(s/def ::valid
  "Boolean validation flag"
  boolean?)

(s/def ::message
  "Validation message"
  string?)

(s/def ::length
  "String length"
  (s/and int? #(>= % 0)))

(s/def ::limit
  "Length limit"
  (s/and int? #(>= % 0)))

;; =============================================================================
;; COMPOSITE SPECS
;; =============================================================================

(s/def ::graintime-components
  "Parsed graintime components"
  (s/keys :req-un [::date ::time ::timezone ::nakshatra ::house-info ::author]))

(s/def ::date
  "Date string in YYYY-MM-DD format"
  (s/and string? #(re-matches #"^\d{5}-\d{2}-\d{2}$" %)))

(s/def ::time
  "Time string in HHMM format"
  (s/and string? #(re-matches #"^\d{4}$" %)))

(s/def ::timezone
  "Timezone abbreviation"
  (s/and string? #(re-matches #"^[A-Z]{3}$" %)))

(s/def ::nakshatra
  "Nakshatra abbreviation"
  (s/and string? #(re-matches #"^[a-z-]+$" %)))

(s/def ::house-info
  "House information string"
  (s/and string? #(re-matches #"^asc-[a-z]{4}\d{3}--sun-\d{2}[a-z]{2}$" %)))

(s/def ::author
  "Author name"
  ::author-name)
