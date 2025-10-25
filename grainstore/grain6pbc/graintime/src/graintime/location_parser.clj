(ns graintime.location-parser
  "Parse location coordinates from command-line flags"
  (:require [clojure.string :as str]))

(defn parse-coordinate
  "Parse a coordinate string (latitude or longitude)
  
  Supports formats:
  - Decimal degrees: 37.9735
  - Degrees with direction: 37.9735N or 122.5311W
  - DMS (degrees, minutes, seconds): 37°58'24.6\"N"
  [coord-str coord-type]
  (try
    (let [trimmed (str/trim coord-str)]
      (cond
        ;; Simple decimal: 37.9735
        (re-matches #"-?\d+\.?\d*" trimmed)
        (Double/parseDouble trimmed)
        
        ;; Decimal with direction: 37.9735N or 122.5311W
        (re-matches #"(\d+\.?\d*)\s*([NSEW])" trimmed)
        (let [[_ num dir] (re-find #"(\d+\.?\d*)\s*([NSEW])" trimmed)
              value (Double/parseDouble num)]
          (if (or (= dir "S") (= dir "W"))
            (- value)
            value))
        
        ;; TODO: Add DMS parsing if needed
        :else
        (throw (ex-info (str "Invalid " coord-type " format: " coord-str)
                       {:coordinate coord-str :type coord-type}))))
    (catch Exception e
      (throw (ex-info (str "Cannot parse " coord-type ": " coord-str)
                     {:coordinate coord-str
                      :type coord-type
                      :original-error (.getMessage e)})))))

(defn parse-latitude
  "Parse latitude coordinate (-90 to 90)
  
  Examples:
  - 37.9735
  - 37.9735N
  - -37.9735 (South)"
  [lat-str]
  (let [lat (parse-coordinate lat-str "latitude")]
    (when (or (> lat 90) (< lat -90))
      (throw (ex-info (str "Latitude out of range: " lat " (must be -90 to 90)")
                     {:latitude lat})))
    lat))

(defn parse-longitude
  "Parse longitude coordinate (-180 to 180)
  
  Examples:
  - -122.5311
  - 122.5311W
  - 122.5311E"
  [lon-str]
  (let [lon (parse-coordinate lon-str "longitude")]
    (when (or (> lon 180) (< lon -180))
      (throw (ex-info (str "Longitude out of range: " lon " (must be -180 to 180)")
                     {:longitude lon})))
    lon))

(defn parse-location-args
  "Parse location from command-line arguments
  
  Looks for --lat and --lon flags in args
  Returns {:latitude N :longitude N} or nil if not found
  
  Usage:
    (parse-location-args [\"--lat\" \"37.9735\" \"--lon\" \"-122.5311\" \"other\" \"args\"])
    => {:latitude 37.9735 :longitude -122.5311 :remaining-args [\"other\" \"args\"]}"
  [args]
  (loop [remaining args
         lat nil
         lon nil
         other-args []]
    (if (empty? remaining)
      {:latitude lat
       :longitude lon
       :remaining-args other-args}
      (let [current (first remaining)
            next-val (second remaining)]
        (cond
          ;; --lat flag
          (or (= current "--lat") (= current "--latitude"))
          (if next-val
            (recur (drop 2 remaining)
                   (parse-latitude next-val)
                   lon
                   other-args)
            (throw (ex-info "Missing value for --lat flag" {:args args})))
          
          ;; --lon flag
          (or (= current "--lon") (= current "--longitude"))
          (if next-val
            (recur (drop 2 remaining)
                   lat
                   (parse-longitude next-val)
                   other-args)
            (throw (ex-info "Missing value for --lon flag" {:args args})))
          
          ;; Other arguments
          :else
          (recur (rest remaining)
                 lat
                 lon
                 (conj other-args current)))))))

(defn parse-latlon-pair
  "Parse latitude,longitude pair in standard Unix format
  
  Common Unix utility format: LAT,LON
  Examples:
  - 37.9735,-122.5311
  - 40.7128,-74.0060 (New York)
  - 51.5074,-0.1278 (London)
  
  This matches the format used by:
  - geoip utilities
  - curl ipinfo.io (outputs loc: LAT,LON)
  - whereami command
  - GPS utilities"
  [latlon-str]
  (let [parts (str/split (str/trim latlon-str) #",")]
    (when (not= (count parts) 2)
      (throw (ex-info (str "Invalid lat,lon format: " latlon-str " (expected: LAT,LON)")
                     {:input latlon-str})))
    (let [lat (parse-latitude (first parts))
          lon (parse-longitude (second parts))]
      {:latitude lat
       :longitude lon})))

(defn get-location-or-default
  "Get location from args or return default (San Rafael, CA)
  
  Supports multiple input formats:
  1. --loc LAT,LON (Unix standard format)
  2. --lat LAT --lon LON (explicit flags)
  3. No flags (uses default San Rafael, CA)
  
  Returns map with :latitude :longitude and :location-name"
  [args]
  (let [;; Check for --loc flag first (Unix standard)
        loc-idx (.indexOf args "--loc")
        loc-pair (when (>= loc-idx 0)
                   (get args (inc loc-idx)))
        
        ;; Parse --loc if present
        loc-parsed (when loc-pair
                     (try
                       (parse-latlon-pair loc-pair)
                       (catch Exception e
                         (throw (ex-info "Invalid --loc format. Use: --loc LAT,LON (e.g., --loc 37.9735,-122.5311)"
                                       {:input loc-pair
                                        :error (.getMessage e)})))))
        
        ;; Remove --loc args from remaining args
        args-without-loc (if (>= loc-idx 0)
                          (vec (concat (take loc-idx args)
                                      (drop (+ loc-idx 2) args)))
                          args)
        
        ;; Parse --lat/--lon flags from remaining args
        parsed (parse-location-args args-without-loc)
        
        ;; Determine final location (--loc takes precedence)
        lat (or (:latitude loc-parsed) (:latitude parsed))
        lon (or (:longitude loc-parsed) (:longitude parsed))]
    
    (if (and lat lon)
      {:latitude lat
       :longitude lon
       :location-name "Custom Location"
       :remaining-args (:remaining-args parsed)}
      {:latitude 37.9735
       :longitude -122.5311
       :location-name "San Rafael, CA, USA"
       :remaining-args args})))

(defn format-location
  "Format location coordinates for display
  
  Returns string like: 37.9735°N, 122.5311°W"
  [{:keys [latitude longitude]}]
  (let [lat-dir (if (>= latitude 0) "N" "S")
        lon-dir (if (>= longitude 0) "E" "W")
        lat-abs (Math/abs latitude)
        lon-abs (Math/abs longitude)]
    (format "%.4f°%s, %.4f°%s" lat-abs lat-dir lon-abs lon-dir)))

(defn format-unix-location
  "Format location in Unix standard format: LAT,LON
  
  This is the format used by Unix utilities like:
  - curl ipinfo.io (outputs: {\"loc\": \"LAT,LON\"})
  - geoip tools
  - whereami command
  
  Returns string like: 37.9735,-122.5311"
  [{:keys [latitude longitude]}]
  (format "%.4f,%.4f" latitude longitude))

;; Test function
(defn test-location-parser
  "Test the location parser with various inputs"
  []
  (println "🧪 Testing location parser...")
  
  (let [test-cases
        [;; Unix standard format (preferred)
         ["--loc" "37.9735,-122.5311"]
         ["--loc" "40.7128,-74.0060"]
         ["--loc" "51.5074,-0.1278"]
         
         ;; Explicit lat/lon flags
         ["--lat" "37.9735" "--lon" "-122.5311"]
         ["--latitude" "40.7128" "--longitude" "-74.0060"]
         
         ;; With other arguments
         ["kae3g" "--loc" "37.9735,-122.5311" "2025-10-22"]
         ["--loc" "37.9735,-122.5311" "kae3g" "2025-10-22"]
         
         ;; Default (no location)
         ["kae3g" "2025-10-22"]]]
    
    (doseq [args test-cases]
      (try
        (let [result (get-location-or-default args)]
          (println (format "✅ %s"
                          (pr-str args)))
          (println (format "   => %s (%s)"
                          (format-location result)
                          (format-unix-location result)))
          (println (format "   Remaining args: %s"
                          (pr-str (:remaining-args result)))))
        (catch Exception e
          (println (format "❌ %s"
                          (pr-str args)))
          (println (format "   => ERROR: %s"
                          (.getMessage e))))))))

