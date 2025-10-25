(require '[graintime.solar-houses :as sh])

(println "🔍 Testing grainbranch manual error")
(def test-time {:hour 19 :minute 45 :fractional 19.75})
(def sunrise {:hour 7 :minute 26 :fractional 7.43})
(def noon {:hour 12 :minute 54 :fractional 12.90})
(def sunset {:hour 18 :minute 21 :fractional 18.35})
(def midnight {:hour 0 :minute 54 :fractional 0.90})
(def result (sh/calculate-solar-house test-time sunrise noon sunset midnight))
(printf "19:45 PDT = %dth house (%s)\n" (:house result) (:solar-position result))
(printf "Branch says: 8th house\n")
(printf "Calculation is %s\n" (if (= (:house result) 6) "CORRECT (6th house)" "WRONG"))


