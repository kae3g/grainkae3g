(ns graintime.cli
  "Command-line interface for graintime generation
  
  Supports:
  - Interactive mode: prompts for location and team
  - Non-interactive mode: pass location and team as arguments
  - Multiple locations: San Rafael, Kyoto, Barcelona, London, Caspar"
  (:require [graintime.core :as core]
            [graintime.format76 :as f76]
            [graintime.locations :as loc]
            [clojure.string :as str]))

;; =============================================================================
;; CLI HELPERS
;; =============================================================================

(defn prompt
  "Display a prompt and read user input"
  [message]
  (print (str message " "))
  (flush)
  (read-line))

(defn display-locations
  "Display available locations in a nice format"
  []
  (println "\n🌍 Available Locations:")
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
  (doseq [[k v] (sort-by (comp :city second) loc/locations)]
    (let [aliases (filter #(= (val %) k) loc/location-aliases)
          alias-str (when (seq aliases)
                      (str " [" (str/join ", " (map key aliases)) "]"))]
      (println (format "  %-15s %s%s"
                       (name k)
                       (loc/location-display-name k)
                       (or alias-str "")))))
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n"))

(defn display-teams
  "Display available teams in a nice format"
  []
  (println "\n🌾 Available Teams (Grain Network 14):")
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
  (println "  01. teamfire10       - Aries ♈ / I. The Magician")
  (println "  02. teamvault10      - Taurus ♉ / II. The High Priestess")
  (println "  03. teamnetwork10    - Gemini ♊ / III. The Empress")
  (println "  04. teamnurture10    - Cancer ♋ / IV. The Emperor")
  (println "  05. teamshine10      - Leo ♌ / V. The Pope")
  (println "  06. teamprecision10  - Virgo ♍ / VI. The Lovers")
  (println "  07. teambalance10    - Libra ♎ / VII. The Chariot")
  (println "  08. teamtransform10  - Scorpio ♏ / VIII. Justice")
  (println "  09. teamwisdom10     - Sagittarius ♐ / IX. The Hermit")
  (println "  10. teamstructure10  - Capricorn ♑ / X. The Wheel of Fortune")
  (println "  11. teamfuture10     - Aquarius ♒ / XI. Force")
  (println "  12. teamflow10       - Pisces ♓ / XII. The Hanged Man")
  (println "  13. teamascend10     - Ophiuchus ⛎ / XIII. Death")
  (println "  14. teamdescend10    - Cetus 🐋 / XIV. Temperance")
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n")
  (println "Enter team name (e.g., 'structure' or 'teamstructure10')"))

(defn normalize-team-input
  "Normalize team input to just the base name
  
  Examples:
    'structure' -> 'structure'
    'teamstructure10' -> 'structure'
    'teamstructure' -> 'structure'
    '10' -> 'structure' (if valid team number)"
  [team-input]
  (let [input (str/lower-case (str/trim team-input))]
    (cond
      ;; Already just the base name
      (re-matches #"^(fire|vault|network|nurture|shine|precision|balance|transform|wisdom|structure|future|flow|ascend|descend)$" input)
      input
      
      ;; Full team name with 10 suffix
      (re-matches #"^team([a-z]+)10$" input)
      (second (re-matches #"^team([a-z]+)10$" input))
      
      ;; Team name without 10 suffix
      (re-matches #"^team([a-z]+)$" input)
      (second (re-matches #"^team([a-z]+)$" input))
      
      ;; Team number (1-14)
      (re-matches #"^(0?[1-9]|1[0-4])$" input)
      (let [num (Integer/parseInt input)
            teams ["fire" "vault" "network" "nurture" "shine" "precision" 
                   "balance" "transform" "wisdom" "structure" "future" "flow" 
                   "ascend" "descend"]]
        (nth teams (dec num)))
      
      :else nil)))

;; =============================================================================
;; INTERACTIVE MODE
;; =============================================================================

(defn interactive-mode
  "Run graintime generation in interactive mode"
  []
  (println "\n╔════════════════════════════════════════════════════════╗")
  (println "║  🌾 GRAINTIME GENERATOR - Interactive Mode 🌾         ║")
  (println "╚════════════════════════════════════════════════════════╝\n")
  
  ;; Display and select location
  (display-locations)
  (let [location-input (prompt "Select location:")
        location-key (loc/normalize-location location-input)]
    
    (if-not location-key
      (do
        (println (str "❌ Unknown location: " location-input))
        (println "Run again and choose from the list above."))
      
      (let [location (loc/get-location location-key)]
        ;; Display and select team
        (display-teams)
        (let [team-input (prompt "Select team:")
              team-base (normalize-team-input team-input)]
          
          (if-not team-base
            (do
              (println (str "❌ Unknown team: " team-input))
              (println "Run again and choose from the list above."))
            
            ;; Generate graintime
            (do
              (println (str "\n🌍 Location: " (loc/location-display-name location-key)))
              (println (str "🌾 Team: team" team-base "10"))
              (println "\n⏳ Generating graintime...\n")
              
              ;; Generate with team name as author (location-aware coming in future with Swiss Ephemeris)
              (let [team-full (str "team" team-base "10")
                    graintime (core/generate-graintime team-full)]
                (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                (println (str "  " graintime))
                (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                (println (str "\n✅ Graintime generated! (" (count graintime) " chars)\n"))
                
                ;; Display breakdown
                (println "📐 Breakdown:")
                (println (str "   Date:      " (subs graintime 0 11)))
                (println (str "   Time:      " (subs graintime 13 17)))
                (println (str "   Timezone:  " (subs graintime 18 21)))
                (println (str "   Nakshatra: " (re-find #"moon-([a-z_-]+)-asc" graintime)))
                (println (str "   Ascendant: " (re-find #"asc-[a-z]+\d+" graintime)))
                (println (str "   Sun House: " (re-find #"sun-\d+h" graintime)))
                (println (str "   Team:      team" team-base "10"))
                (println "\nnow == next + 1 🌾\n")))))))))

;; =============================================================================
;; NON-INTERACTIVE MODE
;; =============================================================================

(defn non-interactive-mode
  "Run graintime generation in non-interactive mode
  
  Usage:
    bb graintime kyoto structure
    bb graintime barcelona precision
    bb graintime london flow"
  [location-input team-input]
  (let [location-key (loc/normalize-location location-input)
        team-base (normalize-team-input team-input)]
    
    (cond
      (not location-key)
      (do
        (println (str "❌ Unknown location: " location-input))
        (println "\nAvailable locations:")
        (loc/list-locations)
        (System/exit 1))
      
      (not team-base)
      (do
        (println (str "❌ Unknown team: " team-input))
        (println "\nAvailable teams: fire, vault, network, nurture, shine, precision,")
        (println "                  balance, transform, wisdom, structure, future, flow,")
        (println "                  ascend, descend")
        (System/exit 1))
      
      :else
      (let [location (loc/get-location location-key)
            team-full (str "team" team-base "10")
            ;; TODO: Implement location-aware graintime generation
            ;; For now, just generate with team name as author
            graintime (core/generate-graintime team-full)]
        (println graintime)))))

;; =============================================================================
;; MAIN ENTRY POINT
;; =============================================================================

(defn -main
  "Main entry point for graintime CLI
  
  Usage:
    # Interactive mode
    bb graintime
    
    # Non-interactive mode
    bb graintime <location> <team>
    bb graintime kyoto structure
    bb graintime barcelona precision
    bb graintime london flow"
  [& args]
  (case (count args)
    0 (interactive-mode)
    2 (apply non-interactive-mode args)
    (do
      (println "Usage:")
      (println "  Interactive mode:     bb graintime")
      (println "  Non-interactive mode: bb graintime <location> <team>")
      (println "\nExamples:")
      (println "  bb graintime")
      (println "  bb graintime kyoto structure")
      (println "  bb graintime barcelona precision")
      (println "  bb graintime london flow")
      (System/exit 1))))

(comment
  ;; Test interactive mode
  (interactive-mode)
  
  ;; Test non-interactive mode
  (non-interactive-mode "kyoto" "structure")
  (non-interactive-mode "barcelona" "precision")
  (non-interactive-mode "london" "flow")
  (non-interactive-mode "jp" "10")
  (non-interactive-mode "bcn" "teambalance10"))

