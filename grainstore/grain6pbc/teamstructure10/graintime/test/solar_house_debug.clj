(ns solar-house-debug
  (:require [graintime.solar-houses :as sh]))

(println "\n🔍 DEBUG: Solar House Calculation for October 24, 2025")
(println "=" (apply str (repeat 60 "=")))

;; Actual times from API (UTC → PDT converted)
(def sunrise-pdt {:hour 7 :minute 26 :fractional (+ 7 (/ 26 60.0))})
(def noon-pdt {:hour 12 :minute 54 :fractional (+ 12 (/ 54 60.0))})
(def sunset-pdt {:hour 18 :minute 21 :fractional (+ 18 (/ 21 60.0))})
(def midnight-pdt {:hour 0 :minute 54 :fractional (+ 0 (/ 54 60.0))})  ; 12 hours after noon

(println "\n📅 Solar Times (PDT):")
(printf "  Sunrise:  %02d:%02d (%.2f)\n" (:hour sunrise-pdt) (:minute sunrise-pdt) (:fractional sunrise-pdt))
(printf "  Noon:     %02d:%02d (%.2f)\n" (:hour noon-pdt) (:minute noon-pdt) (:fractional noon-pdt))
(printf "  Sunset:   %02d:%02d (%.2f)\n" (:hour sunset-pdt) (:minute sunset-pdt) (:fractional sunset-pdt))
(printf "  Midnight: %02d:%02d (%.2f)\n" (:hour midnight-pdt) (:minute midnight-pdt) (:fractional midnight-pdt))

;; Test critical times
(def test-times
  [{:name "7:45 PM (grainbranch time)" :hour 19 :minute 45}
   {:name "8:03 PM (user complaint time)" :hour 20 :minute 3}
   {:name "10:11 PM (current time)" :hour 22 :minute 11}
   {:name "6:21 PM (exact sunset)" :hour 18 :minute 21}
   {:name "6:22 PM (1 min after sunset)" :hour 18 :minute 22}])

(println "\n🧪 Testing Solar House Calculations:")
(println "=" (apply str (repeat 60 "=")))

(doseq [{:keys [name hour minute]} test-times]
  (let [test-time {:hour hour :minute minute :fractional (+ hour (/ minute 60.0))}
        result (sh/calculate-solar-house test-time sunrise-pdt noon-pdt sunset-pdt midnight-pdt)]
    (printf "\n⏰ %s (%02d:%02d):\n" name hour minute)
    (printf "   House: %dth\n" (:house result))
    (printf "   Position: %s\n" (:solar-position result))
    (printf "   Day?: %s\n" (:is-day result))
    (printf "   Degree: %d°\n" (:degree result))))

(println "\n" (apply str (repeat 60 "=")))
(println "✓ Debug complete\n")


