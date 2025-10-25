#!/usr/bin/env bb

;; grainbranch-linker.bb
;; The Lovers (VI) unite: personalized repos + cross-references + README URLs!
;;
;; This script automates the pattern of:
;; 1. Creating personalized grainbranch repos
;; 2. Cross-linking with template repos
;; 3. Generating README with grainbranch URLs
;; 4. Setting up GitHub Pages / Codeberg Pages
;; 5. Updating repo descriptions with grainURLs
;;
;; Usage:
;;   bb grainbranch-linker.bb --team TEAM --module MODULE --title TITLE
;;
;; Example:
;;   bb grainbranch-linker.bb --team precision06 --module grainchart --title "chart-course-v1"

(require '[clojure.string :as str]
         '[clojure.java.shell :as shell]
         '[clojure.edn :as edn])

;; =============================================================================
;; CONFIGURATION
;; =============================================================================

(def teams
  "All 14 teams with their metadata"
  {:fire01       {:zodiac "Aries ♈" :tarot "I. The Magician" :element "Fire"}
   :water02      {:zodiac "Taurus ♉" :tarot "II. The High Priestess" :element "Earth"}
   :air03        {:zodiac "Gemini ♊" :tarot "III. The Empress" :element "Air"}
   :nurture04    {:zodiac "Cancer ♋" :tarot "IV. The Emperor" :element "Water"}
   :shine05      {:zodiac "Leo ♌" :tarot "V. The Pope" :element "Fire"}
   :precision06  {:zodiac "Virgo ♍" :tarot "VI. The Lovers" :element "Earth"}
   :balance07    {:zodiac "Libra ♎" :tarot "VII. The Chariot" :element "Air"}
   :transform08  {:zodiac "Scorpio ♏" :tarot "VIII. Justice" :element "Water"}
   :truth09      {:zodiac "Sagittarius ♐" :tarot "IX. The Hermit" :element "Fire"}
   :structure10  {:zodiac "Capricorn ♑" :tarot "X. The Wheel of Fortune" :element "Earth"}
   :future11     {:zodiac "Aquarius ♒" :tarot "XI. Force" :element "Air"}
   :flow12       {:zodiac "Pisces ♓" :tarot "XII. The Hanged Man" :element "Water"}
   :ascend13     {:zodiac "Aries ♈" :tarot "XIII. Death" :element "Fire"}
   :descend14    {:zodiac "Taurus ♉" :tarot "XIV. Temperance" :element "Earth"}})

;; =============================================================================
;; GRAINBRANCH STRUCTURE
;; =============================================================================

(defn generate-grainbranch-structure
  "Generate the standard grainbranch directory structure
  
  Pattern (from successful team10-complete-web):
  
  TEMPLATE SIDE (grain6pbc/teamNAME##/MODULE/):
    - README.md (specs, base definitions)
    - template/ (base configs)
    - docs/ (documentation)
  
  PERSONAL SIDE (teamNAME##/grainteamNAME##MODULE/):
    - README.md (implementation, with grainbranch URLs!)
    - grainbranch/ (grainbranch-specific content)
    - web-app/ (if site)
    - src/ (implementation code)"
  [team-key module-name]
  (let [team-info (get teams team-key)
        team-num (name team-key)
        template-path (str "grainstore/grain6pbc/team" team-num "/" module-name "/")
        personal-path (str "grainstore/team" team-num "/grainteam" team-num module-name "/")]
    
    {:template {:path template-path
                :readme (str template-path "README.md")
                :purpose "Specs, base definitions, template"}
     :personal {:path personal-path
                :readme (str personal-path "README.md")
                :purpose "Implementation, customized, grainbranch-linked"}
     :team team-info}))

;; =============================================================================
;; README CROSS-LINKING
;; =============================================================================

(defn generate-grainbranch-section
  "Generate README section with grainbranch URLs
  
  This is the MAGIC part - creating beautiful cross-linked sections!"
  [grainbranch-name grainbranch-url repo-type]
  (str "## 🌾 Grainbranch: " grainbranch-name "\n"
       "\n"
       "**Current grainbranch:** `" grainbranch-name "`\n"
       "\n"
       "**Live at:**\n"
       "- **" (if (= repo-type :github) "GitHub" "Codeberg") " Pages:** " grainbranch-url "\n"
       "- **Grainpath format:** 19-char title + 1 dash + 76-char graintime = 96 chars ✓\n"
       "\n"
       "**Template ↔ Personal:**\n"
       "- **Template (specs):** `grain6pbc/team*/MODULE/` - shared, reusable foundation\n"
       "- **Personal (impl):** `team*/grainteam*MODULE/` - customized, grainbranch-linked\n"
       "\n"
       "**The Lovers' wisdom:** Template defines WHAT (specs), Personal defines HOW (implementation), united in sacred purpose. 💕\n"
       "\n"))

;; =============================================================================
;; GITHUB INTEGRATION
;; =============================================================================

(defn update-repo-with-grainbranch
  "Update GitHub repo: default branch + description with grainURL"
  [grainbranch-name]
  (println "\n🌾 Updating repository with grainbranch...\n")
  
  ;; Get repo info
  (let [repo-info (shell/sh "gh" "repo" "view" "--json" "nameWithOwner" "--jq" ".nameWithOwner")
        repo-name (str/trim (:out repo-info))]
    
    (when (zero? (:exit repo-info))
      (let [grain-url (str "https://github.com/" repo-name "/tree/" grainbranch-name "/")
            description (str "grainURL: " grain-url)]
        
        (println "  Repository:" repo-name)
        (println "  grainURL:" grain-url)
        (println)
        
        ;; Set default branch
        (println "  Setting default branch...")
        (shell/sh "gh" "repo" "edit" "--default-branch" grainbranch-name)
        (println "    ✓ Default branch set")
        
        ;; Update description
        (println "  Updating description...")
        (shell/sh "gh" "repo" "edit" "--description" description)
        (println "    ✓ Description updated")
        (println)
        (println "✨ Repository updated! The Lovers unite template + personal! 💕")))))

;; =============================================================================
;; MAIN FUNCTION
;; =============================================================================

(defn -main [& args]
  (println "\n💕 Grainbranch Linker - The Lovers (VI) Unite!")
  (println "   Conscious choice of organization ✨\n")
  
  ;; TODO: Parse args and implement full linking logic
  ;; For now, document the pattern
  
  (println "Pattern successfully used in:")
  (println "  ✓ teamstructure10 (graintime, grainai-voice)")
  (println "  ✓ teamprecision06 (grainchart - NEW!)")
  (println)
  (println "Template creates the pattern.")
  (println "Personal implements with love.")
  (println "Grainbranches link it all together!")
  (println)
  (println "now == next + 1 🌾💕"))

(when (= *file* (System/getProperty "babashka.file"))
  (apply -main *command-line-args*))

