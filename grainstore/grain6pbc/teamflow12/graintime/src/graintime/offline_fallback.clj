(ns graintime.offline-fallback
  "Offline fallback system for graintime with grain6 verification flags
  
  When API is unavailable, make educated conservative guesses based on:
  - Previous grainpath timestamps
  - System time and timezone
  - Approximate solar calculations
  
  Set grain6 flags for later verification when network is restored."
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.edn :as edn])
  (:import [java.time LocalDateTime ZoneId]
           [java.time.format DateTimeFormatter]))

;; ╔══════════════════════════════════════════════════════════════════════════╗
;; ║                                                                          ║
;; ║        🌾 G R A I N T I M E   O F F L I N E   F A L L B A C K 🌾       ║
;; ║                                                                          ║
;; ║   "Local Control, Global Intent - even when offline!"                   ║
;; ║                                                                          ║
;; ║   When the stars hide behind clouds,                                    ║
;; ║   We remember their paths -                                             ║
;; ║   The grain still knows time.                                           ║
;; ║                                                                          ║
;; ╚══════════════════════════════════════════════════════════════════════════╝

(def verification-queue-path
  "Path to grain6 verification queue for offline graintimes"
  (str (System/getProperty "user.home") "/.config/grain6/graintime-verify-queue.edn"))

(def last-known-grainpath-path
  "Path to last known successful grainpath"
  (str (System/getProperty "user.home") "/.config/graintime/last-grainpath.edn"))

(defn ensure-config-dir
  "Ensure grain6 and graintime config directories exist"
  []
  (let [grain6-dir (io/file (System/getProperty "user.home") ".config" "grain6")
        graintime-dir (io/file (System/getProperty "user.home") ".config" "graintime")]
    (.mkdirs grain6-dir)
    (.mkdirs graintime-dir)))

(defn save-last-grainpath
  "Save successful grainpath for offline fallback"
  [grainpath-data]
  (ensure-config-dir)
  (spit last-known-grainpath-path (pr-str grainpath-data)))

(defn load-last-grainpath
  "Load last known successful grainpath"
  []
  (when (.exists (io/file last-known-grainpath-path))
    (try
      (edn/read-string (slurp last-known-grainpath-path))
      (catch Exception _ nil))))

(defn conservative-solar-house-guess
  "Make conservative guess for solar house based on time of day
  
  Uses simple hour-based calculation:
  - Pre-dawn (3am-6am): 3rd house
  - Sunrise-mid-morning (6am-9am): 12th-1st house
  - Mid-morning-noon (9am-12pm): 11th-10th house  
  - Noon-afternoon (12pm-3pm): 10th-9th house
  - Afternoon-sunset (3pm-6pm): 8th-7th house
  - Sunset-evening (6pm-9pm): 7th-6th house
  - Evening-midnight (9pm-12am): 5th-4th house
  - Midnight-pre-dawn (12am-3am): 4th-3rd house"
  [datetime]
  (let [hour (.getHour datetime)]
    (cond
      (and (>= hour 3) (< hour 6)) 3    ; Pre-dawn
      (and (>= hour 6) (< hour 9)) 1    ; Sunrise
      (and (>= hour 9) (< hour 12)) 11  ; Mid-morning
      (and (>= hour 12) (< hour 15)) 10 ; Noon
      (and (>= hour 15) (< hour 18)) 8  ; Afternoon
      (and (>= hour 18) (< hour 21)) 7  ; Sunset
      (and (>= hour 21) (< hour 24)) 5  ; Evening
      :else 4)))                         ; Midnight-pre-dawn

(defn guess-nakshatra-from-previous
  "Guess nakshatra based on previous grainpath and days elapsed
  
  Nakshatra changes approximately every 13.3 hours (27 nakshatras / ~27 days)
  This is a rough approximation for offline use."
  [last-grainpath datetime]
  (if-let [last-nakshatra (:moon-nakshatra last-grainpath)]
    (let [last-datetime (:datetime last-grainpath)
          hours-elapsed (/ (- (.toEpochSecond (.atZone datetime (ZoneId/systemDefault)))
                              (.toEpochSecond (.atZone last-datetime (ZoneId/systemDefault))))
                          3600.0)
          nakshatra-shifts (int (/ hours-elapsed 13.3))
          nakshatras ["ashwini" "bharani" "krittika" "rohini" "mrigashira" 
                     "ardra" "punarvasu" "pushya" "ashlesha" "magha" 
                     "p_phalguni" "u_phalguni" "hasta" "chitra" "swati" 
                     "vishakha" "anuradha" "jyeshtha" "mula" "p_ashadha" 
                     "u_ashadha" "shravana" "dhanishta" "shatabhisha" 
                     "p_bhadrapada" "u_bhadrapada" "revati"]
          last-index (.indexOf nakshatras last-nakshatra)
          new-index (mod (+ last-index nakshatra-shifts) 27)]
      (nth nakshatras new-index))
    "vishakha")) ; Default conservative guess

(defn guess-ascendant-from-time
  "Guess ascendant based on time of day
  
  Ascendant changes approximately every 2 hours (12 signs / 24 hours)
  This is VERY approximate and should be verified when online!"
  [datetime latitude]
  (let [hour (.getHour datetime)
        ;; Rough approximation: ascendant advances ~30° every 2 hours
        ;; Adjust for latitude (higher latitudes have faster ascendant changes)
        lat-factor (if (> (Math/abs latitude) 40) 1.5 1.0)
        sign-index (mod (int (/ (* hour lat-factor) 2)) 12)
        signs ["ari" "tau" "gem" "can" "leo" "vir" 
               "lib" "sco" "sag" "cap" "aqu" "pis"]
        sign (nth signs sign-index)
        ;; Degree is very rough - just use 000 for offline
        degree "000"]
    {:sign sign :degree degree}))

(defn add-to-verification-queue
  "Add offline graintime to grain6 verification queue
  
  When network is restored, grain6 daemon will:
  1. Check if this timestamp is before or after network restoration
  2. Re-calculate accurate graintime from API
  3. Update any dependent files/links
  4. Log discrepancies for educational purposes"
  [graintime-data]
  (ensure-config-dir)
  (let [queue (if (.exists (io/file verification-queue-path))
                (try
                  (edn/read-string (slurp verification-queue-path))
                  (catch Exception _ []))
                [])
        new-entry (assoc graintime-data
                        :offline-generated-at (System/currentTimeMillis)
                        :verification-status :pending
                        :grain6-flag true)]
    (spit verification-queue-path 
          (pr-str (conj queue new-entry)))))

(defn generate-offline-graintime
  "Generate graintime using offline fallback with conservative guesses
  
  Returns graintime data with :offline flag set to true
  and queues verification task for grain6 daemon."
  [datetime latitude longitude course-name]
  (let [last-grainpath (load-last-grainpath)
        sun-house (conservative-solar-house-guess datetime)
        moon-nakshatra (guess-nakshatra-from-previous last-grainpath datetime)
        ascendant (guess-ascendant-from-time datetime latitude)
        
        graintime-data {:datetime datetime
                       :latitude latitude
                       :longitude longitude
                       :sun-house sun-house
                       :moon-nakshatra moon-nakshatra
                       :ascendant-sign (:sign ascendant)
                       :ascendant-degree (:degree ascendant)
                       :course-name course-name
                       :offline true
                       :warning "Generated offline - awaiting grain6 verification"}]
    
    ;; Add to grain6 verification queue
    (add-to-verification-queue graintime-data)
    
    ;; Print warning
    (println "")
    (println "╔══════════════════════════════════════════════════════════════════╗")
    (println "║  ⚠️  OFFLINE MODE: Conservative Graintime Estimate ⚠️           ║")
    (println "╚══════════════════════════════════════════════════════════════════╝")
    (println "")
    (println "🌾 Network unavailable - using educated guess based on:")
    (if last-grainpath
      (println (str "   - Last known grainpath: " (:grainpath last-grainpath)))
      (println "   - No previous grainpath found"))
    (println (str "   - System time: " (.format datetime DateTimeFormatter/ISO_LOCAL_DATE_TIME)))
    (println (str "   - Conservative solar house: " sun-house "th house"))
    (println (str "   - Estimated nakshatra: " moon-nakshatra))
    (println (str "   - Approximate ascendant: " (:sign ascendant) (:degree ascendant)))
    (println "")
    (println "🔧 grain6 Verification Flag Set:")
    (println "   - When network restored, grain6 daemon will verify this timestamp")
    (println "   - Accurate graintime will be calculated retroactively")
    (println "   - Verification queue: ~/.config/grain6/graintime-verify-queue.edn")
    (println "")
    (println "💡 To check verification status: bb grain6:verify-queue")
    (println "")
    
    graintime-data))

(defn check-verification-queue
  "Check grain6 verification queue status"
  []
  (if (.exists (io/file verification-queue-path))
    (let [queue (edn/read-string (slurp verification-queue-path))
          pending (filter #(= :pending (:verification-status %)) queue)
          verified (filter #(= :verified (:verification-status %)) queue)
          failed (filter #(= :failed (:verification-status %)) queue)]
      (println "")
      (println "╔══════════════════════════════════════════════════════════════════╗")
      (println "║     🌾 grain6 Graintime Verification Queue Status 🌾           ║")
      (println "╚══════════════════════════════════════════════════════════════════╝")
      (println "")
      (println (str "📋 Total entries: " (count queue)))
      (println (str "⏳ Pending verification: " (count pending)))
      (println (str "✅ Verified: " (count verified)))
      (println (str "❌ Failed: " (count failed)))
      (println "")
      (when (seq pending)
        (println "Pending graintimes:")
        (doseq [entry pending]
          (println (str "  - " (:datetime entry) " (sun-" (:sun-house entry) "th, moon-" (:moon-nakshatra entry) ")")))))
    (println "No verification queue found - all graintimes generated online! 🌾")))

;; ╔══════════════════════════════════════════════════════════════════════════╗
;; ║                                                                          ║
;; ║     "When clouds obscure the stars above,                               ║
;; ║      The grain remembers - and the daemon waits."                       ║
;; ║                                                                          ║
;; ║                    now == next + 1 🌾                                   ║
;; ║                                                                          ║
;; ╚══════════════════════════════════════════════════════════════════════════╝

