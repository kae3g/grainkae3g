(ns graintime.core-enhanced
  "Enhanced Graintime: Neovedic timestamp system with Astromitra.com integration
  
  Uses Holocene calendar (12025) + Vedic nakshatras for precise,
  meaningful timestamps that integrate with immutable grainpaths."
  (:require [clojure.string :as str]
            [graintime.astromitra :as astro]))

;; Nakshatra data (27 lunar mansions)
(def nakshatras
  ["Ashwini" "Bharani" "Krittika" "Rohini" "Mrigashira" "Ardra" "Punarvasu"
   "Pushya" "Ashlesha" "Magha" "Purva Phalguni" "Uttara Phalguni" "Hasta"
   "Chitra" "Swati" "Vishakha" "Anuradha" "Jyeshtha" "Mula" "Purva Ashadha"
   "Uttara Ashadha" "Shravana" "Dhanishta" "Shatabhisha" "Purva Bhadrapada"
   "Uttara Bhadrapada" "Revati"])

;; Zodiac houses (1-12)
(def houses (range 1 13))

(defn holocene-year
  "Convert Gregorian year to Holocene calendar (add 10000)"
  [gregorian-year]
  (+ gregorian-year 10000))

(defn calculate-nakshatra
  "Calculate current nakshatra from date
  Simple approximation - full calculation requires astronomical data"
  [month day]
  (let [day-of-year (+ (* (dec month) 30) day)
        nakshatra-index (mod (quot day-of-year 14) 27)]
    (nth nakshatras nakshatra-index)))

(defn format-timezone
  "Format timezone offset as timezone abbreviation"
  [offset-hours]
  (cond
    (= offset-hours -8) "PST"
    (= offset-hours -7) "PDT"
    (= offset-hours -6) "CST"
    (= offset-hours -5) "CDT"
    (= offset-hours -4) "EDT"
    (= offset-hours -3) "ADT"
    (= offset-hours 0) "UTC"
    (= offset-hours 1) "CET"
    (= offset-hours 2) "EET"
    (= offset-hours 5.5) "IST"
    :else (format "UTC%+d" (int offset-hours))))

(defn generate-graintime-enhanced
  "Generate enhanced graintime timestamp with Astromitra.com integration
  
  Returns: 12025-10-23--2200--PST--moon-vishakha-pada2--asc-gemini05--sun-06thhouse--kae3g
  
  Format:
  {holocene-year}-{month}-{day}--{time}--{tz}--moon-{nakshatra}-pada{pada}--asc-{sign}{degree}--sun-{house}thhouse--{author}
  
  Uses Astromitra.com for accurate nakshatra data and simplified house calculations.
  For full accuracy, integrate Swiss Ephemeris for ascendant calculation."
  ([author]
   (generate-graintime-enhanced author (java.time.ZonedDateTime/now) nil nil))
  ([author datetime latitude longitude]
   (try
     ;; Try to get accurate data from Astromitra.com
     (astro/get-accurate-graintime author)
     (catch Exception e
       ;; Fallback to simplified calculation if API fails
       (println "⚠️  Astromitra API unavailable, using simplified calculation")
       (let [year (holocene-year (.getYear datetime))
             month (.getMonthValue datetime)
             day (.getDayOfMonth datetime)
             hour (.getHour datetime)
             minute (.getMinute datetime)
             tz-offset (/ (.getTotalSeconds (.getOffset datetime)) 3600)
             tz (format-timezone tz-offset)
             ;; Simplified nakshatra calculation
             nakshatra (str/lower-case (str/replace (calculate-nakshatra month day) #" " "-"))
             ;; Simplified house calculation
             house (inc (mod (quot hour 2) 12))
             degree (mod (* hour 2.5) 30)]
         (format "%d-%02d-%02d--%02d%02d--%s--moon-%s--%02dthhouse%02d--%s"
                 year month day hour minute tz nakshatra house (int degree) author))))))

(defn generate-grainpath-enhanced
  "Generate immutable grainpath with enhanced graintime
   
   Format: /course/kae3g/my-course/12025-10-23--2200--PST--moon-vishakha-pada2--asc-gemini05--sun-06thhouse--kae3g/"
  [type author name]
  (let [graintime (generate-graintime-enhanced author)]
    (str "/" type "/" author "/" name "/" graintime "/")))

(defn generate-repo-name-enhanced
  "Generate repository name with enhanced graintime"
  [type author name]
  (let [graintime (generate-graintime-enhanced author)
        ;; Sanitize graintime for repo name (replace special chars)
        safe-graintime (-> graintime
                          (str/replace #"--" "-")
                          (str/replace #":" "-")
                          (str/replace #" " "-"))]
    (str type "-" author "-" name "-" safe-graintime)))

;; Test function
(defn test-enhanced-graintime
  "Test the enhanced graintime system"
  []
  (println "🌾 Testing Enhanced Graintime System...")
  (println "")
  
  (println "1️⃣ Testing Astromitra integration:")
  (astro/test-astromitra-integration)
  (println "")
  
  (println "2️⃣ Testing enhanced graintime generation:")
  (let [graintime (generate-graintime-enhanced "kae3g")]
    (println (format "   Enhanced graintime: %s" graintime)))
  (println "")
  
  (println "3️⃣ Testing enhanced grainpath generation:")
  (let [grainpath (generate-grainpath-enhanced "course" "kae3g" "test-course")]
    (println (format "   Enhanced grainpath: %s" grainpath)))
  (println "")
  
  (println "4️⃣ Testing enhanced repo name generation:")
  (let [repo-name (generate-repo-name-enhanced "course" "kae3g" "test-course")]
    (println (format "   Enhanced repo name: %s" repo-name)))
  (println "")
  
  (println "✅ Enhanced graintime system test complete!"))
