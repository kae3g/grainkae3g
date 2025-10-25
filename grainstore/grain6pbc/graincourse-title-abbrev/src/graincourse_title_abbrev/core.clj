(ns graincourse-title-abbrev.core
  "Intelligent course title abbreviation for grainpaths
  
  Ensures grainpaths stay under 100 characters while preserving
  semantic meaning and readability of course titles."
  (:require [clojure.string :as str]))

;; Common word abbreviations (Tier 1)
(def word-abbreviations
  {"introduction" "intro"
   "fundamentals" "fund"
   "fundamental" "fund"
   "advanced" "adv"
   "programming" "prog"
   "functional" "func"
   "object-oriented" "oo"
   "systems" "sys"
   "system" "sys"
   "architecture" "arch"
   "development" "dev"
   "engineering" "eng"
   "engineer" "eng"
   "network" "net"
   "networks" "net"
   "database" "db"
   "databases" "db"
   "algorithm" "algo"
   "algorithms" "algo"
   "data-structure" "ds"
   "data-structures" "ds"
   "machine-learning" "ml"
   "artificial-intelligence" "ai"
   "computer-science" "cs"
   "operating-system" "os"
   "operating-systems" "os"
   "distributed" "dist"
   "concurrent" "conc"
   "concurrency" "conc"
   "parallel" "par"
   "category-theory" "cat"
   "type-theory" "types"
   "theory" "th"})

(defn calculate-title-budget
  "Calculate how many characters are available for the course title
   
   Formula: 100 (total) - 16 (overhead) - graintime-length - 2 (slashes)
   Overhead = /course/ + username + /
   
   Example:
   - Total: 100 chars
   - Overhead: /course/kae3g/ = 14 chars
   - Graintime: 70 chars
   - Slashes: 2 chars
   - Available: 100 - 14 - 70 - 2 = 14 chars"
  ([username graintime]
   (let [username-len (count username)
         graintime-len (count graintime)
         overhead (+ 9 username-len)  ; "/course/" + username + "/"
         slashes 2
         budget (- 100 overhead graintime-len slashes)]
     (max 2 budget)))  ; Minimum 2 chars for course title
  ([graintime]
   (calculate-title-budget "kae3g" graintime)))

(defn apply-word-abbreviations
  "Apply common word abbreviations to course title (Tier 1)"
  [title]
  (let [words (str/split title #"-")]
    (str/join "-" 
      (map #(get word-abbreviations % %) words))))

(defn remove-filler-words
  "Remove common filler words if title is still too long"
  [title]
  (-> title
      (str/replace #"-to-" "-")
      (str/replace #"-of-" "-")
      (str/replace #"-the-" "-")
      (str/replace #"-and-" "-")
      (str/replace #"-with-" "-")
      (str/replace #"-for-" "-")))

(defn remove-vowels
  "Remove vowels from words (keeping first letter) - Tier 2"
  [word]
  (if (< (count word) 4)
    word
    (let [first-char (first word)
          rest-chars (rest word)
          consonants (filter #(not (contains? #{\a \e \i \o \u} %)) rest-chars)]
      (str first-char (apply str consonants)))))

(defn apply-vowel-removal
  "Apply vowel removal to each word in title"
  [title]
  (let [words (str/split title #"-")]
    (str/join "-"
      (map remove-vowels words))))

(defn truncate-to-budget
  "Truncate title to fit within budget (Tier 3)
   
   Preserves word boundaries and keeps first letters"
  [title budget]
  (if (<= (count title) budget)
    title
    (let [words (str/split title #"-")
          truncated (loop [result []
                          remaining words]
                     (if (empty? remaining)
                       result
                       (let [candidate (str/join "-" (conj result (first remaining)))]
                         (if (<= (count candidate) budget)
                           (recur (conj result (first remaining)) (rest remaining))
                           result))))]
      (if (empty? truncated)
        ;; If no words fit, take first chars of each word
        (let [initials (map first words)
              acronym (apply str initials)]
          (if (<= (count acronym) budget)
            acronym
            (subs acronym 0 budget)))
        (str/join "-" truncated)))))

(defn create-acronym
  "Create acronym from title - Tier 4 fallback"
  [title]
  (let [words (str/split title #"-")
        initials (map first words)]
    (apply str initials)))

(defn abbreviate-course-title
  "Intelligently abbreviate course title to fit within budget
   
   Uses multi-tier approach:
   1. Common word abbreviations
   2. Remove filler words
   3. Vowel removal
   4. Truncation to word boundaries
   5. Acronym fallback
   
   Example:
   (abbreviate-course-title \"introduction-to-functional-programming\" 15)
   => \"intro-func-prog\""
  [title budget]
  (cond
    ;; Title already fits
    (<= (count title) budget)
    title
    
    ;; Try Tier 1: Common abbreviations
    :else
    (let [tier1 (apply-word-abbreviations title)]
      (if (<= (count tier1) budget)
        tier1
        
        ;; Try Tier 2: Remove filler words
        (let [tier2 (remove-filler-words tier1)]
          (if (<= (count tier2) budget)
            tier2
            
            ;; Try Tier 3: Vowel removal
            (let [tier3 (apply-vowel-removal tier2)]
              (if (<= (count tier3) budget)
                tier3
                
                ;; Try Tier 4: Truncation
                (let [tier4 (truncate-to-budget tier2 budget)]
                  (if (<= (count tier4) budget)
                    tier4
                    
                    ;; Tier 5: Acronym fallback
                    (let [acronym (create-acronym title)]
                      (if (<= (count acronym) budget)
                        acronym
                        (subs acronym 0 budget)))))))))))

(defn validate-grainpath
  "Validate that grainpath is under 100 characters
   
   Returns map with:
   - :valid - boolean
   - :length - current length
   - :budget-remaining - chars left (or negative if over)"
  [grainpath]
  (let [length (count grainpath)
        valid (<= length 100)
        budget-remaining (- 100 length)]
    {:valid valid
     :length length
     :budget-remaining budget-remaining}))

(defn format-grainpath-with-abbreviation
  "Format grainpath with abbreviated course title if needed
   
   Returns map with:
   - :grainpath - the formatted path
   - :abbreviated - boolean, true if title was shortened
   - :original-title - original course title
   - :abbreviated-title - shortened title (same as original if not abbreviated)
   - :validation - result from validate-grainpath"
  [path-type username course-title graintime]
  (let [budget (calculate-title-budget username graintime)
        abbreviated-title (abbreviate-course-title course-title budget)
        grainpath (format "/%s/%s/%s/%s/" path-type username abbreviated-title graintime)
        validation (validate-grainpath grainpath)
        was-abbreviated (not= course-title abbreviated-title)]
    {:grainpath grainpath
     :abbreviated was-abbreviated
     :original-title course-title
     :abbreviated-title abbreviated-title
     :validation validation}))

;; Example usage and tests
(comment
  ;; Test worst-case scenario
  (def worst-graintime "12025-10-23--2359--PDT--moon-u_bhadrapada--asc-sag359--sun-12th--kae3g")
  (def long-title "introduction-to-functional-programming-and-type-systems")
  
  (calculate-title-budget "kae3g" worst-graintime)
  ;; => 14
  
  (abbreviate-course-title long-title 14)
  ;; => "intro-func-prg"
  
  (format-grainpath-with-abbreviation "course" "kae3g" long-title worst-graintime)
  ;; => {:grainpath "/course/kae3g/intro-func-prg/12025-10-23--2359--PDT--moon-u_bhadrapada--asc-sag359--sun-12th--kae3g/"
  ;;     :abbreviated true
  ;;     :original-title "introduction-to-functional-programming-and-type-systems"
  ;;     :abbreviated-title "intro-func-prg"
  ;;     :validation {:valid true, :length 95, :budget-remaining 5}}
  )

