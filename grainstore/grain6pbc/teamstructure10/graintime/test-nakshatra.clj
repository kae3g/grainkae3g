#!/usr/bin/env bb

(require '[graintime.moon-position :as moon])
(require '[graintime.nakshatra-conversion :as nakshatra])

(println "🌑 Testing real nakshatra calculations\n")

(let [moon-pos (moon/get-current-moon-position)
      nak-data (nakshatra/get-nakshatra-from-moon-position (:moon-longitude moon-pos))]
  
  (println "Current DateTime:" (:datetime moon-pos))
  (println "Moon Longitude:" (format "%.2f°" (:moon-longitude moon-pos)))
  (println "Zodiac Sign:" (:zodiac-sign moon-pos))
  (println "Degree within sign:" (format "%.2f°" (:degree-within-sign moon-pos)))
  (println)
  (println "Nakshatra (full):" (:nakshatra nak-data))
  (println "Nakshatra (abbrev):" (:nakshatra-abbrev nak-data))
  (println "Nakshatra index:" (:nakshatra-index nak-data))
  (println "Degree within nakshatra:" (format "%.2f°" (:degree-within-nakshatra nak-data)))
  (println)
  (println "✓ Real calculations working!"))

