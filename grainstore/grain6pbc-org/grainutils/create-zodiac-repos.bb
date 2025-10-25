#!/usr/bin/env bb
;; 🌾 Create 14 Zodiac Repository Structure
;; *"From 40 to 14 - Every Repo Must Be Essential"*

(require '[babashka.fs :as fs]
         '[clojure.string :as str])

(def zodiac-repos
  [{:sign "aries" :symbol "♈" :element "fire" :energy "cardinal"
    :name "grainfire" :purpose "Initiative & Build System"
    :modules ["grainbarrel" "grainconfig" "grain6"]}
   
   {:sign "leo" :symbol "♌" :element "fire" :energy "fixed"
    :name "grainshine" :purpose "Display & UI"
    :modules ["graindisplay" "grain-nightlight" "grainicons" "grainas-voice"]}
   
   {:sign "sagittarius" :symbol "♐" :element "fire" :energy "mutable"
    :name "grainwisdom" :purpose "Education & Documentation"
    :modules ["grainlexicon" "graincourse" "graincourse-sync" "docs"]}
   
   {:sign "taurus" :symbol "♉" :element "earth" :energy "fixed"
    :name "grainvault" :purpose "Storage & Resources"
    :modules ["graindrive" "grainclay" "grainclay-cleanup" "vendor"]}
   
   {:sign "virgo" :symbol "♍" :element "earth" :energy "mutable"
    :name "grainprecision" :purpose "System Administration"
    :modules ["grainenvvars" "grainzsh" "clojure-s6" "clojure-sixos"]}
   
   {:sign "capricorn" :symbol "♑" :element "earth" :energy "cardinal"
    :name "grainstructure" :purpose "Architecture & Foundation"
    :modules ["graindatastructures" "grain-metatypes" "specs" "equivalence"]}
   
   {:sign "gemini" :symbol "♊" :element "air" :energy "mutable"
    :name "grainnetwork" :purpose "Networking & Communication"
    :modules ["grainweb" "grainpages" "graindevname" "grainregistry"]}
   
   {:sign "libra" :symbol "♎" :element "air" :energy "cardinal"
    :name "grainbalance" :purpose "ICP & Blockchain"
    :modules ["clotoko" "clotoko-icp" "grainkae3g-clojure-icp"]}
   
   {:sign "aquarius" :symbol "♒" :element "air" :energy "fixed"
    :name "grainfuture" :purpose "Innovation & Technology"
    :modules ["humble-desktop" "humble-gc" "humble-repl" "humble-stack" 
              "grain-musl" "grain-clj"]}
   
   {:sign "cancer" :symbol "♋" :element "water" :energy "cardinal"
    :name "grainnurture" :purpose "Applications & Support"
    :modules ["graincasks" "graindaemon" "tools"]}
   
   {:sign "scorpio" :symbol "♏" :element "water" :energy "fixed"
    :name "graintransform" :purpose "Compilers & Transformation"
    :modules ["grainutils/clelte" "grainutils/clotoko" "grainconv" "grainsynonym"]}
   
   {:sign "pisces" :symbol "♓" :element "water" :energy "mutable"
    :name "grainflow" :purpose "Time & Spirituality"
    :modules ["graintime" "grainneovedic" "grainutils/graindaemon" "grainsource-separation"]}
   
   {:sign "rahu" :symbol "☊" :element "shadow" :energy "north-node"
    :name "grainascend" :purpose "Innovation & Obsession"
    :modules ["grainai-vocab" "grainas" "grainmode" "grainutils/icp-tools"]}
   
   {:sign "ketu" :symbol "☋" :element "shadow" :energy "south-node"
    :name "graindescend" :purpose "Wisdom & Detachment"
    :modules ["urbit-source" "aspirational-pseudo" "grainbusiness" "grainsource-vegan"]}])

(defn create-zodiac-repo
  "Create a zodiac repository with README and structure"
  [{:keys [sign symbol element energy name purpose modules]}]
  (let [base-path (str "grain6pbc/" name)
        readme-path (str base-path "/README.md")]
    
    ;; Create directory
    (fs/create-dirs base-path)
    
    ;; Create README
    (spit readme-path
          (str "# 🌾 " name " - " symbol " " (str/capitalize sign) "\n"
               "## *\"" purpose "\"*\n\n"
               "**Element**: " (str/capitalize element) "\n"
               "**Energy**: " (str/capitalize energy) "\n"
               "**Modules Consolidated**: " (count modules) "\n\n"
               "---\n\n"
               "## 📦 **Consolidated Modules**\n\n"
               (str/join "\n" (map #(str "- `" % "`") modules))
               "\n\n---\n\n"
               "## 🎯 **Purpose**\n\n"
               purpose "\n\n"
               "---\n\n"
               "## 🏗️ **Structure**\n\n"
               "```\n"
               name "/\n"
               (str/join "\n" (map #(str "├── " % "/") modules))
               "\n├── README.md\n"
               "└── ARCHITECTURE.md\n"
               "```\n\n"
               "---\n\n"
               "*\"Every grain matters, every module essential, every branch precise.\"*\n\n"
               "🌾 **" (str/capitalize sign) " - " symbol "** 🌾\n"))
    
    (println (str "✅ Created " name " (" symbol " " sign ")"))
    (println (str "   Purpose: " purpose))
    (println (str "   Modules: " (count modules)))
    (println "")))

(defn -main
  "Create all 14 zodiac repositories"
  [& args]
  (println "🌾 Creating 14 Zodiac Repositories")
  (println "")
  (println "Kanye's Philosophy: 14 Songs > 40 Songs")
  (println "Vedic Astrology: 12 Zodiac + Rahu + Ketu")
  (println "")
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
  (println "")
  
  (doseq [repo zodiac-repos]
    (create-zodiac-repo repo))
  
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
  (println "")
  (println "✨ All 14 zodiac repos created!")
  (println "")
  (println "Next steps:")
  (println "1. Review each repo's README")
  (println "2. Migrate modules into zodiac repos")
  (println "3. Update references and imports")
  (println "4. Archive old structure")
  (println "")
  (println "🌾 \"From 40 to 14 - Precision Branch Collective\" 🌾"))

(-main)

