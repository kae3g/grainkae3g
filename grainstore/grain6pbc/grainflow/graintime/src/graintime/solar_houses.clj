(ns graintime.solar-houses
  "Solar House Clock system based on Sun's diurnal motion
  
  The Solar House Clock divides the day into 12 houses based on the Sun's position:
  
  - 1st House (Ascendant/ASC): Sunrise
  - 10th House (Midheaven/MC): Solar Noon (Sun at highest point)
  - 7th House (Descendant/DSC): Sunset
  - 4th House (Imum Coeli/IC): Solar Midnight (Sun at lowest point)
  
  Houses between these cardinal points are calculated as equidistant divisions:
  - 1st → 10th: Morning houses (2nd, 3rd, houses rise to noon)
  - 10th → 7th: Afternoon houses (11th, 8th, 9th descend to sunset)
  - 7th → 4th: Evening houses (6th, 5th descend to midnight)
  - 4th → 1st: Night houses (3rd, 2nd, 1st rise to sunrise)
  
  This system is independent of natal charts and purely based on solar time."
  (:require [babashka.http-client :as http]
            [cheshire.core :as json]
            [clojure.string :as str]))

;; Sunrise-sunset.org API for accurate solar times
(def sunrise-api-base "https://api.sunrise-sunset.org/json")

(defn fetch-solar-times
  "Fetch sunrise, sunset, solar noon, and solar midnight for a location
   
   Uses sunrise-sunset.org API
   
   Example response:
   {:sunrise \"6:45:23 AM\"
    :sunset \"7:12:45 PM\"
    :solar_noon \"1:00:00 PM\"
    :day_length \"12:27:22\"}"
  [latitude longitude date]
  (try
    (let [url (format "%s?lat=%s&lng=%s&date=%s&formatted=0"
                     sunrise-api-base latitude longitude date)
          response (http/get url {:headers {"User-Agent" "GrainOS/1.0"}})
          body (json/parse-string (:body response) true)
          results (:results body)]
      {:sunrise (:sunrise results)
       :sunset (:sunset results)
       :solar-noon (:solar_noon results)
       :day-length (:day_length results)
       :civil-twilight-begin (:civil_twilight_begin results)
       :civil-twilight-end (:civil_twilight_end results)
       :status (:status body)})
    (catch Exception e
      (println "⚠️  Failed to fetch solar times from API:" (.getMessage e))
      nil)))

(defn parse-iso-time
  "Parse ISO 8601 time string to hour and minute in local timezone
  
   Converts UTC time to Pacific Daylight Time (PDT) for San Rafael, CA"
  [iso-string]
  (try
    (let [instant (java.time.Instant/parse iso-string)
          pdt-zone (java.time.ZoneId/of "America/Los_Angeles")
          zdt (java.time.ZonedDateTime/ofInstant instant pdt-zone)
          hour (.getHour zdt)
          minute (.getMinute zdt)]
      {:hour hour :minute minute :fractional (+ hour (/ minute 60.0))})
    (catch Exception e
      (println "⚠️  Failed to parse time:" iso-string)
      nil)))

(defn calculate-solar-midnight
  "Calculate solar midnight (opposite of solar noon)
   Solar midnight is 12 hours after solar noon"
  [solar-noon-time]
  (let [noon (parse-iso-time solar-noon-time)
        midnight-hour (mod (+ (:hour noon) 12) 24)]
    {:hour midnight-hour :minute (:minute noon) :fractional (+ midnight-hour (/ (:minute noon) 60.0))}))

(defn calculate-precise-house-divisions
  "Calculate precise house divisions based on actual day/night length ratios
  
  For accurate astronomical house calculation:
  - Day period: Sunrise → Sunset (longer in summer, shorter in winter)
  - Night period: Sunset → Sunrise (shorter in summer, longer in winter)
  - Each house gets proportional time based on season and latitude
  
  Returns: {:day-houses [1 12 11 10] :night-houses [7 6 5 4 3 2] :day-duration :night-duration}"
  [sunrise-time solar-noon-time sunset-time solar-midnight-time]
  (let [sunrise (:fractional sunrise-time)
        noon (:fractional solar-noon-time)
        sunset (:fractional sunset-time)
        midnight (:fractional solar-midnight-time)
        
        ;; Calculate actual day and night durations
        day-duration (- sunset sunrise)
        night-duration (if (< midnight sunset)
                         (+ (- 24 sunset) sunrise)  ; Cross midnight
                         (- sunrise sunset))
        
        ;; Day houses: 1 → 12 → 11 → 10 (4 houses)
        ;; Night houses: 4 → 3 → 2 → 1 (4 houses)
        day-houses [1 12 11 10]
        night-houses [4 3 2 1]
        
        ;; Calculate proportional durations for each house
        day-house-duration (/ day-duration (count day-houses))
        night-house-duration (/ night-duration (count night-houses))]
    
    {:day-houses day-houses
     :night-houses night-houses
     :day-duration day-duration
     :night-duration night-duration
     :day-house-duration day-house-duration
     :night-house-duration night-house-duration}))

(defn calculate-solar-house
  "Calculate current solar house based on Sun's position in the sky
  
   🌅 NON-SYMMETRIC HOUSE SYSTEM:
   Cardinal houses are exact for ONE MINUTE only:
   - 1st House: Exact sunrise (one minute before = 2nd house, one minute after = 12th house)
   - 10th House: Exact solar noon (one minute before = 11th house, one minute after = 9th house)
   - 7th House: Exact sunset (one minute before = 8th house, one minute after = 6th house)
   - 4th House: Exact solar midnight (one minute before = 5th house, one minute after = 3rd house)
   
   Intermediate houses split evenly according to day/night lengths:
   - Morning: 12th, 11th (between sunrise and noon)
   - Afternoon: 9th, 8th (between noon and sunset)
   - Evening: 6th, 5th (between sunset and midnight)
   - Night: 3rd, 2nd (between midnight and sunrise)
   
   Returns: {:house 8 :degree 15 :solar-position \"afternoon\" :is-day true}"
  [current-time sunrise-time solar-noon-time sunset-time solar-midnight-time]
  (let [current (:fractional current-time)
        sunrise (:fractional sunrise-time)
        noon (:fractional solar-noon-time)
        sunset (:fractional sunset-time)
        midnight (:fractional solar-midnight-time)
        
        ;; Convert to minutes for precise one-minute cardinal house logic
        current-minutes (+ (* (:hour current-time) 60) (:minute current-time))
        sunrise-minutes (+ (* (:hour sunrise-time) 60) (:minute sunrise-time))
        noon-minutes (+ (* (:hour solar-noon-time) 60) (:minute solar-noon-time))
        sunset-minutes (+ (* (:hour sunset-time) 60) (:minute sunset-time))
        midnight-minutes (+ (* (:hour solar-midnight-time) 60) (:minute solar-midnight-time))
        
        ;; Handle midnight crossing
        sunset-minutes (if (< sunset-minutes sunrise-minutes) (+ sunset-minutes 1440) sunset-minutes)
        midnight-minutes (if (< midnight-minutes sunrise-minutes) (+ midnight-minutes 1440) midnight-minutes)
        current-minutes (if (< current-minutes sunrise-minutes) (+ current-minutes 1440) current-minutes)]
    
    (cond
      ;; 🌅 EXACT SUNRISE - 1st House for exactly one minute
      (= current-minutes sunrise-minutes)
      {:house 1 :degree 0 :solar-position "sunrise" :is-day true}
      
      ;; ☀️ EXACT SOLAR NOON - 10th House for exactly one minute
      (= current-minutes noon-minutes)
      {:house 10 :degree 0 :solar-position "noon" :is-day true}
      
      ;; 🌇 EXACT SUNSET - 7th House for exactly one minute
      (= current-minutes sunset-minutes)
      {:house 7 :degree 0 :solar-position "sunset" :is-day false}
      
      ;; 🌙 EXACT SOLAR MIDNIGHT - 4th House for exactly one minute
      (= current-minutes midnight-minutes)
      {:house 4 :degree 0 :solar-position "midnight" :is-day false}
      
      ;; 🌅 MORNING: Between sunrise and solar noon
      ;; Split evenly between 12th and 11th houses
      (and (> current-minutes sunrise-minutes) (< current-minutes noon-minutes))
      (let [morning-duration (- noon-minutes sunrise-minutes)
            progress (/ (- current-minutes sunrise-minutes) morning-duration)
            ;; Split into two houses: 12th (first half) and 11th (second half)
            house-progress (* progress 2)
            house-index (int house-progress)
            house (if (< house-index 1) 12 11)
            degree (* 30 (mod house-progress 1))]
        {:house house
         :degree (int degree)
         :solar-position "morning"
         :is-day true})
      
      ;; ☀️ AFTERNOON: Between solar noon and sunset
      ;; Split evenly between 9th and 8th houses
      (and (> current-minutes noon-minutes) (< current-minutes sunset-minutes))
      (let [afternoon-duration (- sunset-minutes noon-minutes)
            progress (/ (- current-minutes noon-minutes) afternoon-duration)
            ;; Split into two houses: 9th (first half) and 8th (second half)
            house-progress (* progress 2)
            house-index (int house-progress)
            house (if (< house-index 1) 9 8)
            degree (* 30 (mod house-progress 1))]
        {:house house
         :degree (int degree)
         :solar-position "afternoon"
         :is-day true})
      
      ;; 🌆 EVENING: Between sunset and solar midnight
      ;; Split evenly between 6th and 5th houses
      (and (> current-minutes sunset-minutes) (< current-minutes midnight-minutes))
      (let [evening-duration (- midnight-minutes sunset-minutes)
            progress (/ (- current-minutes sunset-minutes) evening-duration)
            ;; Split into two houses: 6th (first half) and 5th (second half)
            house-progress (* progress 2)
            house-index (int house-progress)
            house (if (< house-index 1) 6 5)
            degree (* 30 (mod house-progress 1))]
        {:house house
         :degree (int degree)
         :solar-position "evening"
         :is-day false})
      
      ;; 🌙 NIGHT: Between solar midnight and sunrise
      ;; Split evenly between 3rd and 2nd houses
      :else
      (let [night-duration (- (+ sunrise-minutes 1440) midnight-minutes)
            progress (/ (- current-minutes midnight-minutes) night-duration)
            ;; Split into two houses: 3rd (first half) and 2nd (second half)
            house-progress (* progress 2)
            house-index (int house-progress)
            house (if (< house-index 1) 3 2)
            degree (* 30 (mod house-progress 1))]
        {:house house
         :degree (int degree)
         :solar-position "night"
         :is-day false}))))

(defn get-current-solar-house
  "Get current solar house for a given location
   
   Returns complete solar house information including:
   - House number (1-12)
   - Degree within house (0-30)
   - Solar position (sunrise/morning/noon/afternoon/sunset/evening/midnight/night)
   - Day/night status
   - All solar times for reference"
  ([]
   (get-current-solar-house (java.time.ZonedDateTime/now) 37.9735 -122.5311))
  ([datetime latitude longitude]
   (let [date (format "%s-%02d-%02d"
                     (.getYear datetime)
                     (.getMonthValue datetime)
                     (.getDayOfMonth datetime))
         solar-times (fetch-solar-times latitude longitude date)]
     
     (if solar-times
       (let [sunrise-time (parse-iso-time (:sunrise solar-times))
             solar-noon-time (parse-iso-time (:solar-noon solar-times))
             sunset-time (parse-iso-time (:sunset solar-times))
             solar-midnight-time (calculate-solar-midnight (:solar-noon solar-times))
             current-time {:hour (.getHour datetime)
                          :minute (.getMinute datetime)
                          :fractional (+ (.getHour datetime) (/ (.getMinute datetime) 60.0))}
             house-info (calculate-solar-house current-time sunrise-time solar-noon-time sunset-time solar-midnight-time)]
         
         (merge house-info
                {:current-time (format "%02d:%02d" (:hour current-time) (:minute current-time))
                 :sunrise (format "%02d:%02d" (:hour sunrise-time) (:minute sunrise-time))
                 :solar-noon (format "%02d:%02d" (:hour solar-noon-time) (:minute solar-noon-time))
                 :sunset (format "%02d:%02d" (:hour sunset-time) (:minute sunset-time))
                 :solar-midnight (format "%02d:%02d" (:hour solar-midnight-time) (:minute solar-midnight-time))
                 :location {:latitude latitude :longitude longitude}}))
       
       ;; Fallback to simplified calculation if API fails
       (do
         (println "⚠️  Using simplified house calculation (API unavailable)")
         {:house 7
          :degree 0
          :solar-position "unknown"
          :is-day false
          :warning "API unavailable - using fallback calculation"})))))
