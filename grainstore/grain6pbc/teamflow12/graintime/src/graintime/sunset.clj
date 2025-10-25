(ns graintime.sunset
  "Sunset and sunrise calculations for accurate house determination"
  (:require [clojure.string :as str]))

;; Simplified sunset/sunrise calculations
;; For accurate results, would need full astronomical calculations

(defn calculate-sunset-time
  "Calculate approximate sunset time for a given date and location
   
   Simplified calculation - real implementation would use:
   - Solar noon calculation
   - Equation of time
   - Geographic coordinates
   - Date-specific solar declination
   
   For San Rafael, CA (37.97°N, 122.53°W):
   - Summer sunset: ~8:30 PM PDT
   - Winter sunset: ~5:00 PM PST
   - Spring/Fall: ~7:00 PM PDT/PST"
  [datetime latitude longitude]
  (let [month (.getMonthValue datetime)
        ;; Approximate sunset hour based on season
        sunset-hour (cond
                      (#{6 7 8} month) 20  ; Summer: ~8 PM
                      (#{12 1 2} month) 17 ; Winter: ~5 PM
                      :else 19)]           ; Spring/Fall: ~7 PM
    sunset-hour))

(defn is-after-sunset?
  "Check if current time is after sunset"
  [datetime latitude longitude]
  (let [hour (.getHour datetime)
        sunset-hour (calculate-sunset-time datetime latitude longitude)]
    (>= hour sunset-hour)))

(defn calculate-house-from-time
  "Calculate astrological house based on time of day and sunset
   
   The 12 houses are determined by the ascendant (rising sign) which:
   - Changes approximately every 2 hours
   - Depends on exact birth time
   - Requires precise location (latitude/longitude)
   - Accounts for local sidereal time
   
   This is a SIMPLIFIED calculation. For accurate results, use:
   - Swiss Ephemeris library
   - Astromitra.com API with custom location
   - Proper ascendant calculation from sidereal time"
  [datetime latitude longitude]
  (let [hour (.getHour datetime)
        minute (.getMinute datetime)
        is-night? (is-after-sunset? datetime latitude longitude)
        
        ;; Calculate fractional hour
        fractional-hour (+ hour (/ minute 60.0))
        
        ;; Houses rotate approximately every 2 hours
        ;; But this is VERY simplified - real calculation needs:
        ;; 1. Local Sidereal Time (LST)
        ;; 2. RAMC (Right Ascension of Medium Coeli)
        ;; 3. House cusp calculations (Placidus, Koch, Whole Sign, etc.)
        
        ;; Simplified: use fractional hour to estimate house
        house-index (mod (int (/ fractional-hour 2)) 12)
        house (inc house-index)
        
        ;; Calculate degree within house (0-30)
        degree-in-house (mod (* fractional-hour 15) 30)]
    
    {:house house
     :degree (int degree-in-house)
     :is-night is-night?
     :method :simplified
     :warning "This is a simplified calculation. For accuracy, integrate Swiss Ephemeris or Astromitra API."}))

(defn get-current-house-info
  "Get current house information with sunset awareness
   
   Returns:
   {:house 9
    :degree 17
    :is-night true
    :local-time \"20:51\"
    :sunset-time \"19:00\"}"
  ([]
   (get-current-house-info (java.time.ZonedDateTime/now) 37.9735 -122.5311))
  ([datetime latitude longitude]
   (let [house-data (calculate-house-from-time datetime latitude longitude)
         sunset-hour (calculate-sunset-time datetime latitude longitude)
         hour (.getHour datetime)
         minute (.getMinute datetime)]
     (merge house-data
            {:local-time (format "%02d:%02d" hour minute)
             :sunset-time (format "%02d:00" sunset-hour)
             :location {:latitude latitude :longitude longitude}}))))
