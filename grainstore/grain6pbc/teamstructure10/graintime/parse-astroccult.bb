#!/usr/bin/env bb

(require '[clojure.string :as str])

(defn normalize-nakshatra-name
  "Normalize nakshatra names from AstrOccult format
  
  Examples:
    'Purva Phalgu' -> 'Purva Phalguni'
    'Uttara Phalg' -> 'Uttara Phalguni'
    'Mrigashirsha' -> 'Mrigashira'
    'Satabhisha' -> 'Shatabhisha'
    'Asvini' -> 'Ashwini'
    'Uttar Bhadra' -> 'Uttara Bhadrapada'
    'Purva Bhadra' -> 'Purva Bhadrapada'"
  [nak]
  (cond
    (= nak "Purva Phalgu") "Purva Phalguni"
    (= nak "Uttara Phalg") "Uttara Phalguni"
    (= nak "Mrigashirsha") "Mrigashira"
    (= nak "Satabhisha") "Shatabhisha"
    (= nak "Asvini") "Ashwini"
    (= nak "Uttar Bhadra") "Uttara Bhadrapada"
    (= nak "Purva Bhadra") "Purva Bhadrapada"
    (= nak "Purva Ashadh") "Purva Ashadha"
    (= nak "Uttara Ashad") "Uttara Ashadha"
    :else nak))

(defn is-nakshatra?
  "Check if entry is a nakshatra (not a zodiac sign)"
  [name]
  (let [zodiac-signs #{"Aries" "Taurus" "Gemini" "Cancer" "Leo" "Virgo"
                       "Libra" "Scorpio" "Sagittarius" "Capricorn" "Aquarius" "Pisces"}]
    (not (contains? zodiac-signs name))))

(defn parse-moon-line
  "Parse a Moon transit line
  
  Input: 'Moon    Jyeshtha        18/10/2023 21:00'
  Output: ['18/10/2023' '21:00' 'Jyeshtha']"
  [line]
  (when-let [parts (re-find #"Moon\s+(\S+)\s+(\d{2}/\d{2}/\d{4})\s+(\d{2}:\d{2})" line)]
    (let [[_ nakshatra date time] parts
          normalized (normalize-nakshatra-name nakshatra)]
      (when (is-nakshatra? normalized)
        [date time normalized]))))

(defn extract-moon-transits
  "Extract all Moon nakshatra transitions from HTML file"
  [html-file]
  (let [lines (str/split-lines (slurp html-file))
        moon-lines (filter #(str/includes? % "<p>Moon") lines)
        cleaned (map #(-> %
                          (str/replace #"<[^>]*>" "")
                          (str/replace #"&nbsp;" " ")
                          (str/replace #"\s+" " ")
                          str/trim)
                     moon-lines)
        parsed (keep parse-moon-line cleaned)]
    parsed))

;; Main execution
(let [html-file "astroccult-oct2025.html"
      transitions (extract-moon-transits html-file)
      
      ;; Group by year for organization
      by-year (group-by #(subs (first %) 6 10) transitions)]
  
  (println ";; Moon Nakshatra Transitions from AstrOccult.net")
  (println ";; Source: https://www.astroccult.net/transit_of_planets_planetary_events.html")
  (println ";; Timezone: IST (Indian Standard Time, UTC+5:30)")
  (println ";; Format: [\"DD/MM/YYYY\" \"HH:MM\" \"Nakshatra Name\"]")
  (println)
  (println "{:source \"https://www.astroccult.net/transit_of_planets_planetary_events.html\"")
  (println " :timezone \"IST\"")
  (println " :planet \"Moon\"")
  (println " :transitions")
  (println " [")
  
  (doseq [[year entries] (sort by-year)]
    (println)
    (println "  ;;" year "-" (count entries) "transitions")
    (doseq [entry entries]
      (println "  " (pr-str entry))))
  
  (println " ]}")
  (println)
  (println ";; Total transitions:" (count transitions)))

