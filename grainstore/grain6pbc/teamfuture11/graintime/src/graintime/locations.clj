(ns graintime.locations
  "Location data for graintime generation across global Grain Network nodes
  
  Each location includes:
  - Coordinates (latitude/longitude) for astronomical calculations
  - Timezone identifier for accurate time conversion
  - City name and country for display
  - Nakshatra API location code (if different from city name)")

;; =============================================================================
;; LOCATION DATA
;; =============================================================================

(def locations
  "Map of location keys to their geographic and timezone data"
  {:san-rafael {:city "San Rafael"
                :country "USA"
                :state "California"
                :lat 37.9735
                :lon -122.5311
                :tz "America/Los_Angeles"
                :api-location "San Rafael, CA"}
   
   :kyoto {:city "Kyoto"
           :country "Japan"
           :lat 35.0116
           :lon 135.7681
           :tz "Asia/Tokyo"
           :api-location "Kyoto, Japan"}
   
   :barcelona {:city "Barcelona"
               :country "Spain"
               :lat 41.3874
               :lon 2.1686
               :tz "Europe/Madrid"
               :api-location "Barcelona, Spain"}
   
   :london {:city "London"
            :country "UK"
            :lat 51.5074
            :lon -0.1278
            :tz "Europe/London"
            :api-location "London, UK"}
   
   :caspar {:city "Caspar"
            :country "USA"
            :state "California"
            :lat 39.3627
            :lon -123.8164
            :tz "America/Los_Angeles"
            :api-location "Caspar, CA"}})

(def location-aliases
  "Aliases for common location names"
  {"san rafael" :san-rafael
   "sr" :san-rafael
   "marin" :san-rafael
   
   "kyoto" :kyoto
   "jp" :kyoto
   "japan" :kyoto
   
   "barcelona" :barcelona
   "bcn" :barcelona
   "spain" :barcelona
   "es" :barcelona
   
   "london" :london
   "uk" :london
   "gb" :london
   
   "caspar" :caspar
   "mendocino" :caspar
   "forest" :caspar})

(defn normalize-location
  "Normalize location input to a location keyword
  
  Accepts:
  - Keyword (:kyoto, :barcelona, etc.)
  - String ('kyoto', 'barcelona', etc.)
  - Alias ('jp', 'bcn', 'uk', etc.)
  
  Returns location keyword or nil if not found"
  [location-input]
  (cond
    (keyword? location-input)
    (if (contains? locations location-input)
      location-input
      nil)
    
    (string? location-input)
    (let [normalized (clojure.string/lower-case (clojure.string/trim location-input))]
      (or (get location-aliases normalized)
          (keyword normalized)))
    
    :else nil))

(defn get-location
  "Get location data for a given location key or input
  
  Returns location map or nil if not found"
  [location-input]
  (when-let [loc-key (normalize-location location-input)]
    (get locations loc-key)))

(defn list-locations
  "List all available locations with their details"
  []
  (doseq [[k v] (sort-by (comp :city second) locations)]
    (println (format "%-15s %s, %s (%s)"
                     (name k)
                     (:city v)
                     (:country v)
                     (:tz v)))))

(defn location-display-name
  "Get display name for a location"
  [location-input]
  (when-let [loc (get-location location-input)]
    (if (:state loc)
      (format "%s, %s, %s" (:city loc) (:state loc) (:country loc))
      (format "%s, %s" (:city loc) (:country loc)))))

(comment
  ;; List all locations
  (list-locations)
  
  ;; Get location by key
  (get-location :kyoto)
  
  ;; Get location by string
  (get-location "barcelona")
  
  ;; Get location by alias
  (get-location "jp")
  (get-location "bcn")
  (get-location "uk")
  
  ;; Get display name
  (location-display-name :kyoto)
  (location-display-name "barcelona")
  (location-display-name "uk"))

