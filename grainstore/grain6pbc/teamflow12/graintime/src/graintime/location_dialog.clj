(ns graintime.location-dialog
  "Interactive location configuration dialog for graintime
  
  Supports multiple input methods:
  1. City, State (USA) - e.g., San Rafael, CA
  2. City, Country - e.g., London, UK
  3. Lat,Lon coordinates - e.g., 37.9735,-122.5311"
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

;; Common US cities with coordinates
(def us-cities
  {"San Francisco, CA" {:lat 37.7749 :lon -122.4194}
   "San Rafael, CA" {:lat 37.9735 :lon -122.5311}
   "Los Angeles, CA" {:lat 34.0522 :lon -118.2437}
   "New York, NY" {:lat 40.7128 :lon -74.0060}
   "Chicago, IL" {:lat 41.8781 :lon -87.6298}
   "Houston, TX" {:lat 29.7604 :lon -95.3698}
   "Phoenix, AZ" {:lat 33.4484 :lon -112.0740}
   "Philadelphia, PA" {:lat 39.9526 :lon -75.1652}
   "San Antonio, TX" {:lat 29.4241 :lon -98.4936}
   "San Diego, CA" {:lat 32.7157 :lon -117.1611}
   "Dallas, TX" {:lat 32.7767 :lon -96.7970}
   "Austin, TX" {:lat 30.2672 :lon -97.7431}
   "Seattle, WA" {:lat 47.6062 :lon -122.3321}
   "Denver, CO" {:lat 39.7392 :lon -104.9903}
   "Boston, MA" {:lat 42.3601 :lon -71.0589}
   "Portland, OR" {:lat 45.5051 :lon -122.6750}
   "Miami, FL" {:lat 25.7617 :lon -80.1918}
   "Atlanta, GA" {:lat 33.7490 :lon -84.3880}})

;; International cities
(def international-cities
  {"London, UK" {:lat 51.5074 :lon -0.1278}
   "Paris, France" {:lat 48.8566 :lon 2.3522}
   "Tokyo, Japan" {:lat 35.6762 :lon 139.6503}
   "Berlin, Germany" {:lat 52.5200 :lon 13.4050}
   "Sydney, Australia" {:lat -33.8688 :lon 151.2093}
   "Toronto, Canada" {:lat 43.6532 :lon -79.3832}
   "Mumbai, India" {:lat 19.0760 :lon 72.8777}
   "Beijing, China" {:lat 39.9042 :lon 116.4074}
   "Moscow, Russia" {:lat 55.7558 :lon 37.6173}
   "Mexico City, Mexico" {:lat 19.4326 :lon -99.1332}})

(defn get-location-config-path
  "Get path to location configuration file"
  []
  (let [home (System/getProperty "user.home")
        graintime-dir (str home "/kae3g/grainkae3g/grainstore/graintime")
        personal-dir (str graintime-dir "/personal")]
    ;; Create personal directory if it doesn't exist
    (.mkdirs (io/file personal-dir))
    (str personal-dir "/location.edn")))

(defn save-location-config
  "Save location configuration to file"
  [location-name latitude longitude]
  (let [config-path (get-location-config-path)
        config {:location-name location-name
                :latitude latitude
                :longitude longitude
                :configured-at (str (java.time.ZonedDateTime/now))}]
    (spit config-path (pr-str config))
    (println (format "✅ Location saved to: %s" config-path))
    config))

(defn load-location-config
  "Load location configuration from file"
  []
  (let [config-path (get-location-config-path)]
    (when (.exists (io/file config-path))
      (try
        (read-string (slurp config-path))
        (catch Exception e
          (println (format "⚠️  Error reading location config: %s" (.getMessage e)))
          nil)))))

(defn prompt-user
  "Prompt user for input with a message"
  [message]
  (print (str message " "))
  (flush)
  (read-line))

(defn find-city
  "Find city in predefined lists (case-insensitive)"
  [city-str]
  (let [normalized (str/lower-case (str/trim city-str))
        all-cities (merge us-cities international-cities)]
    (some (fn [[city-name coords]]
            (when (= (str/lower-case city-name) normalized)
              {:name city-name
               :latitude (:lat coords)
               :longitude (:lon coords)}))
          all-cities)))

(defn parse-coordinates
  "Parse LAT,LON coordinates"
  [coord-str]
  (try
    (let [parts (str/split (str/trim coord-str) #",")]
      (when (= (count parts) 2)
        (let [lat (Double/parseDouble (str/trim (first parts)))
              lon (Double/parseDouble (str/trim (second parts)))]
          (when (and (>= lat -90) (<= lat 90)
                    (>= lon -180) (<= lon 180))
            {:latitude lat
             :longitude lon}))))
    (catch Exception _ nil)))

(defn interactive-location-setup
  "Run interactive location setup dialog"
  []
  (println "")
  (println "🌍 Graintime Location Configuration")
  (println "====================================")
  (println "")
  (println "Your location is used to calculate:")
  (println "  • Solar House Clock (sunrise, noon, sunset, midnight)")
  (println "  • Accurate astrological house positions")
  (println "  • Local timezone for timestamps")
  (println "")
  (println "You can enter your location in three ways:")
  (println "  1. City, State (USA)  - e.g., San Rafael, CA")
  (println "  2. City, Country      - e.g., London, UK")
  (println "  3. Lat,Lon coords     - e.g., 37.9735,-122.5311")
  (println "")
  (println "📍 Popular cities:")
  (println "   USA: San Francisco, CA | New York, NY | Chicago, IL | Seattle, WA")
  (println "   International: London, UK | Paris, France | Tokyo, Japan | Sydney, Australia")
  (println "")
  
  (let [input (prompt-user "Enter your location:")
        trimmed (str/trim input)]
    
    (cond
      ;; Empty input - use default
      (empty? trimmed)
      (do
        (println "")
        (println "ℹ️  Using default location: San Rafael, CA, USA")
        (save-location-config "San Rafael, CA, USA" 37.9735 -122.5311))
      
      ;; Try to find in city list
      (find-city trimmed)
      (let [city (find-city trimmed)]
        (println "")
        (println (format "✅ Found: %s" (:name city)))
        (println (format "   Coordinates: %.4f°%s, %.4f°%s"
                        (Math/abs (:latitude city))
                        (if (>= (:latitude city) 0) "N" "S")
                        (Math/abs (:longitude city))
                        (if (>= (:longitude city) 0) "E" "W")))
        (save-location-config (:name city) (:latitude city) (:longitude city)))
      
      ;; Try to parse as coordinates
      (parse-coordinates trimmed)
      (let [coords (parse-coordinates trimmed)]
        (println "")
        (println (format "✅ Coordinates: %.4f°%s, %.4f°%s"
                        (Math/abs (:latitude coords))
                        (if (>= (:latitude coords) 0) "N" "S")
                        (Math/abs (:longitude coords))
                        (if (>= (:longitude coords) 0) "E" "W")))
        (save-location-config (format "Custom (%.4f,%.4f)" (:latitude coords) (:longitude coords))
                             (:latitude coords)
                             (:longitude coords)))
      
      ;; Unknown city - offer to use coordinates or try again
      :else
      (do
        (println "")
        (println (format "❌ Unknown location: %s" trimmed))
        (println "")
        (println "💡 You can:")
        (println "   1. Try a different city name")
        (println "   2. Use coordinates (find yours at: https://www.latlong.net/)")
        (println "   3. Press Enter to use default (San Rafael, CA)")
        (println "")
        (let [retry (prompt-user "Try again? (y/n):")]
          (if (= (str/lower-case (str/trim retry)) "y")
            (interactive-location-setup)
            (do
              (println "")
              (println "ℹ️  Using default location: San Rafael, CA, USA")
              (save-location-config "San Rafael, CA, USA" 37.9735 -122.5311))))))))

(defn show-current-location
  "Show currently configured location"
  []
  (let [config (load-location-config)]
    (if config
      (do
        (println "")
        (println "📍 Current Location Configuration")
        (println "=================================")
        (println (format "Location: %s" (:location-name config)))
        (println (format "Coordinates: %.4f°%s, %.4f°%s"
                        (Math/abs (:latitude config))
                        (if (>= (:latitude config) 0) "N" "S")
                        (Math/abs (:longitude config))
                        (if (>= (:longitude config) 0) "E" "W")))
        (println (format "Unix format: %.4f,%.4f" (:latitude config) (:longitude config)))
        (println (format "Configured: %s" (:configured-at config)))
        (println "")
        config)
      (do
        (println "")
        (println "⚠️  No location configured")
        (println "   Run: gt config")
        (println "")
        nil))))

(defn get-configured-location
  "Get configured location or return default"
  []
  (let [config (load-location-config)]
    (if config
      {:latitude (:lat config)
       :longitude (:lon config)
       :location-name (:name config)}
      {:latitude 37.9735
       :longitude -122.5311
       :location-name "San Rafael, CA, USA (default)"})))

(defn get-city-coordinates
  "Get coordinates for a city name
   
   Supports:
   - US cities: \"San Rafael, CA\"
   - International cities: \"London, UK\"
   - Returns nil if not found"
  [city-name]
  (or (get us-cities city-name)
      (get international-cities city-name)
      ;; Try case-insensitive search
      (let [lower-city (str/lower-case city-name)]
        (or (first (filter #(str/includes? (str/lower-case (key %)) lower-city) us-cities))
            (first (filter #(str/includes? (str/lower-case (key %)) lower-city) international-cities))))))

(defn setup-location-non-interactive
  "Non-interactive location setup for scripting
   
   Usage:
   (setup-location-non-interactive :city \"San Rafael, CA\")
   (setup-location-non-interactive :coordinates [37.9735 -122.5311])
   (setup-location-non-interactive :lat 37.9735 :lon -122.5311)
   
   Returns: {:success true :location {:lat 37.9735 :lon -122.5311 :name \"San Rafael, CA\"}}"
  [& {:keys [city coordinates lat lon name]}]
  (let [location-data (cond
                        ;; City lookup
                        city (let [coords (get-city-coordinates city)]
                               (if coords
                                 (val coords)  ; Extract the map from MapEntry
                                 {:lat 37.9735 :lon -122.5311 :name "San Rafael, CA"}))
                        
                        ;; Coordinate pair
                        coordinates (let [[lat-val lon-val] coordinates]
                                      {:lat lat-val :lon lon-val :name (str lat-val "," lon-val)})
                        
                        ;; Individual lat/lon
                        (and lat lon) {:lat lat :lon lon :name (str lat "," lon)}
                        
                        ;; Default to San Rafael, CA
                        :else {:lat 37.9735 :lon -122.5311 :name "San Rafael, CA"})
        
        ;; Add custom name if provided
        location-data (if name
                        (assoc location-data :name name)
                        location-data)
        
        ;; Save configuration
        config-path (get-location-config-path)
        config-dir (.getParentFile (io/file config-path))]
    
    (try
      ;; Create directory if it doesn't exist
      (when-not (.exists config-dir)
        (.mkdirs config-dir))
      
      ;; Write configuration
      (spit config-path (pr-str location-data))
      
      {:success true 
       :location location-data
       :config-path config-path
       :message (str "Location configured: " (:name location-data))}
      
      (catch Exception e
        {:success false
         :error (.getMessage e)
         :message "Failed to save location configuration"}))))

(defn run-location-dialog
  "Run location configuration dialog
  
  Options:
  - setup: Run interactive setup
  - show: Show current configuration
  - reset: Reset to default"
  [& [command]]
  (case command
    "setup"
    (interactive-location-setup)
    
    "show"
    (show-current-location)
    
    "reset"
    (do
      (println "")
      (println "🔄 Resetting to default location...")
      (save-location-config "San Rafael, CA, USA" 37.9735 -122.5311))
    
    ;; Default: run setup
    (interactive-location-setup)))

;; Quick test
(defn test-location-dialog
  "Test location dialog functionality"
  []
  (println "🧪 Testing location dialog...")
  
  ;; Test city lookup
  (println "\nTesting city lookup:")
  (doseq [city ["San Rafael, CA" "New York, NY" "London, UK" "tokyo, japan"]]
    (if-let [result (find-city city)]
      (println (format "  ✅ %s => %.4f,%.4f" city (:latitude result) (:longitude result)))
      (println (format "  ❌ %s => Not found" city))))
  
  ;; Test coordinate parsing
  (println "\nTesting coordinate parsing:")
  (doseq [coords ["37.9735,-122.5311" "40.7128,-74.0060" "invalid" "91.0,0.0"]]
    (if-let [result (parse-coordinates coords)]
      (println (format "  ✅ %s => %.4f,%.4f" coords (:latitude result) (:longitude result)))
      (println (format "  ❌ %s => Invalid" coords))))
  
  ;; Show current config
  (println "\nCurrent configuration:")
  (show-current-location))

