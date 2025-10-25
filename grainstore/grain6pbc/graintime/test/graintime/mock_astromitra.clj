(ns graintime.mock-astromitra
  "Mock astromitra API for testing graintime calculations at different times
   
   Allows spoofing API responses to test edge cases:
   - Different times of day (sunrise, noon, sunset, midnight)
   - Different moon nakshatras
   - Different ascendants
   - Offline fallback scenarios"
  (:require [clojure.string :as str]))

;;; ╔═══════════════════════════════════════════════════════════════════╗
;;; ║                                                                   ║
;;; ║   🌾  M O C K   A S T R O M I T R A   A P I                      ║
;;; ║                                                                   ║
;;; ║   Spoof API responses for comprehensive testing                  ║
;;; ║                                                                   ║
;;; ╚═══════════════════════════════════════════════════════════════════╝

;; Mock data for different times of day
(def mock-data
  {;; Early morning (3-6am) - Gemini/Cancer/Leo rising
   :early-morning
   {:sun {:nakshatra "Swati" :completed 0.09 :pada 1 :lord "Rahu"}
    :moon {:nakshatra "Anuradha" :completed 7.77 :pada 1 :lord "Saturn"}
    :ascendant {:sign "Gemini" :degree "22°07'" :pada 3}
    :time "05:00"
    :description "Early morning - Eastern signs rising"}
   
   ;; Sunrise (6-8am) - Cancer/Leo rising
   :sunrise
   {:sun {:nakshatra "Swati" :completed 0.09 :pada 1 :lord "Rahu"}
    :moon {:nakshatra "Anuradha" :completed 8.00 :pada 1 :lord "Saturn"}
    :ascendant {:sign "Leo" :degree "05°12'" :pada 1}
    :time "07:25"
    :description "Sunrise - Sun's sign rises"}
   
   ;; Mid-morning (9-11am) - Leo/Virgo rising
   :mid-morning
   {:sun {:nakshatra "Swati" :completed 0.15 :pada 1 :lord "Rahu"}
    :moon {:nakshatra "Anuradha" :completed 10.50 :pada 1 :lord "Saturn"}
    :ascendant {:sign "Virgo" :degree "18°33'" :pada 2}
    :time "10:00"
    :description "Mid-morning - Virgo rising"}
   
   ;; Noon (11am-1pm) - Virgo/Libra rising
   :noon
   {:sun {:nakshatra "Swati" :completed 0.20 :pada 1 :lord "Rahu"}
    :moon {:nakshatra "Anuradha" :completed 13.00 :pada 2 :lord "Saturn"}
    :ascendant {:sign "Libra" :degree "02°45'" :pada 1}
    :time "12:54"
    :description "Solar noon - Southern signs rising"}
   
   ;; Afternoon (2-5pm) - Libra/Scorpio rising
   :afternoon
   {:sun {:nakshatra "Swati" :completed 0.30 :pada 1 :lord "Rahu"}
    :moon {:nakshatra "Anuradha" :completed 16.50 :pada 2 :lord "Saturn"}
    :ascendant {:sign "Scorpio" :degree "14°22'" :pada 2}
    :time "15:00"
    :description "Afternoon - Scorpio rising"}
   
   ;; Sunset (5-7pm) - Scorpio/Sagittarius rising
   :sunset
   {:sun {:nakshatra "Swati" :completed 0.35 :pada 1 :lord "Rahu"}
    :moon {:nakshatra "Anuradha" :completed 19.00 :pada 3 :lord "Saturn"}
    :ascendant {:sign "Sagittarius" :degree "08°56'" :pada 1}
    :time "18:22"
    :description "Sunset - Western signs rising"}
   
   ;; Evening (7-9pm) - Sagittarius/Capricorn rising
   :evening
   {:sun {:nakshatra "Swati" :completed 0.40 :pada 1 :lord "Rahu"}
    :moon {:nakshatra "Anuradha" :completed 21.00 :pada 3 :lord "Saturn"}
    :ascendant {:sign "Capricorn" :degree "11°34'" :pada 2}
    :time "19:30"
    :description "Evening - Capricorn rising"}
   
   ;; Night (9pm-12am) - Capricorn/Aquarius rising
   :night
   {:sun {:nakshatra "Swati" :completed 0.45 :pada 1 :lord "Rahu"}
    :moon {:nakshatra "Anuradha" :completed 24.00 :pada 3 :lord "Saturn"}
    :ascendant {:sign "Aquarius" :degree "19°08'" :pada 3}
    :time "22:00"
    :description "Night - Aquarius rising"}
   
   ;; Midnight (12am-3am) - Aquarius/Pisces/Aries rising
   :midnight
   {:sun {:nakshatra "Swati" :completed 0.50 :pada 1 :lord "Rahu"}
    :moon {:nakshatra "Anuradha" :completed 27.00 :pada 4 :lord "Saturn"}
    :ascendant {:sign "Pisces" :degree "25°41'" :pada 4}
    :time "00:54"
    :description "Midnight - Culminating signs rising"}})

(defn get-mock-data-for-time
  "Get appropriate mock data based on hour of day"
  [hour]
  (cond
    (and (>= hour 3) (< hour 6))   (:early-morning mock-data)
    (and (>= hour 6) (< hour 9))   (:sunrise mock-data)
    (and (>= hour 9) (< hour 11))  (:mid-morning mock-data)
    (and (>= hour 11) (< hour 14)) (:noon mock-data)
    (and (>= hour 14) (< hour 17)) (:afternoon mock-data)
    (and (>= hour 17) (< hour 19)) (:sunset mock-data)
    (and (>= hour 19) (< hour 21)) (:evening mock-data)
    (and (>= hour 21) (< hour 24)) (:night mock-data)
    :else                          (:midnight mock-data)))

(defn mock-api-response
  "Generate mock API response for given datetime
   
   This simulates what astromitra.com API would return"
  [datetime]
  (let [hour (.getHour datetime)
        mock-data (get-mock-data-for-time hour)]
    
    {:status 200
     :body (str "{"
                "\"sun\": {\"nakshatra\": \"" (get-in mock-data [:sun :nakshatra]) "\", "
                "\"completed\": " (get-in mock-data [:sun :completed]) ", "
                "\"pada\": " (get-in mock-data [:sun :pada]) ", "
                "\"lord\": \"" (get-in mock-data [:sun :lord]) "\"}, "
                "\"moon\": {\"nakshatra\": \"" (get-in mock-data [:moon :nakshatra]) "\", "
                "\"completed\": " (get-in mock-data [:moon :completed]) ", "
                "\"pada\": " (get-in mock-data [:moon :pada]) ", "
                "\"lord\": \"" (get-in mock-data [:moon :lord]) "\"}, "
                "\"ascendant\": {\"sign\": \"" (get-in mock-data [:ascendant :sign]) "\", "
                "\"degree\": \"" (get-in mock-data [:ascendant :degree]) "\", "
                "\"pada\": " (get-in mock-data [:ascendant :pada]) "}"
                "}")
     :mock-time (:time mock-data)
     :mock-description (:description mock-data)}))

(defn with-mock-api
  "Execute function with mocked API responses
   
   Usage:
   (with-mock-api
     (fn [] (graintime.core/generate-graintime \"kae3g\")))"
  [f]
  (with-redefs [graintime.astromitra/fetch-astromitra-data
                (fn [lat lon datetime]
                  (let [response (mock-api-response datetime)]
                    (println (format "🎭 Mock API: %s (%s)"
                                   (:mock-time response)
                                   (:mock-description response)))
                    (:body response)))]
    (f)))

(defn print-mock-data-summary []
  "Print all available mock time periods"
  (println "🎭 Available Mock Time Periods:")
  (println)
  (doseq [[period data] mock-data]
    (println (format "  %-15s %s - %s %s° (%s)"
                    (name period)
                    (:time data)
                    (get-in data [:ascendant :sign])
                    (get-in data [:ascendant :degree])
                    (:description data)))))

;; Example usage:
(comment
  ;; Print available mock data
  (print-mock-data-summary)
  
  ;; Test with mock API at evening time
  (with-mock-api
    (fn []
      (let [evening-dt (java.time.ZonedDateTime/parse "2025-10-23T19:30:00-07:00[America/Los_Angeles]")
            graintime (graintime.core/generate-graintime "kae3g")]
        (println graintime))))
  
  ;; Run ascendant tests with mock API
  (with-mock-api
    (fn [] (graintime.ascendant-test/run-all-tests))))

;;; ╔═══════════════════════════════════════════════════════════════════╗
;;; ║                                                                   ║
;;; ║   🌾 Mock API Benefits:                                          ║
;;; ║                                                                   ║
;;; ║   ✅ Test without network calls                                  ║
;;; ║   ✅ Reproducible test results                                   ║
;;; ║   ✅ Test edge cases (midnight, equinox, etc.)                   ║
;;; ║   ✅ Fast test execution                                         ║
;;; ║   ✅ Verify calculations match expected                          ║
;;; ║                                                                   ║
;;; ║   now == next + 1 🌾                                             ║
;;; ║                                                                   ║
;;; ╚═══════════════════════════════════════════════════════════════════╝

