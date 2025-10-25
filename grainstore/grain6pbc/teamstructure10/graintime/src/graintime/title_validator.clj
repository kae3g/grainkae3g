(ns graintime.title-validator
  "Grainpath title validation and suggestion system
  
  Key constraint: 19-char title + 1 dash + 76-char graintime = 96 total
  
  Format: {title-19}-{graintime-76}
  
  Examples:
    team10-complete-web-12025-10-25--0912-PDT--moon-jyeshtha-----asc-gemi02-sun-12h--teamstructure10
    ^^^^^^^^^^^^^^^^^^^                                                                               
    19 chars exactly!
  
  This module enforces 19-char limit and generates suggestions."
  (:require [clojure.string :as str]))

;; =============================================================================
;; VALIDATION AND PADDING
;; =============================================================================

(def max-title-length 19)

(defn pad-title-to-19
  "Pad title to exactly 19 chars with trailing dashes
  
  Examples:
    'team10' (6 chars) → 'team10-------------' (19 chars)
    'team10-complete-web' (19 chars) → 'team10-complete-web' (unchanged)
  
  This ensures perfect column alignment in grainpaths!"
  [title]
  (let [padding-needed (- max-title-length (count title))]
    (if (pos? padding-needed)
      (str title (apply str (repeat padding-needed "-")))
      title)))

(defn validate-title-length
  "Validate that title is exactly or under 19 characters"
  [title]
  {:title title
   :length (count title)
   :max-length max-title-length
   :valid? (<= (count title) max-title-length)
   :over-by (max 0 (- (count title) max-title-length))
   :padded (pad-title-to-19 title)
   :padded-length (count (pad-title-to-19 title))})

;; =============================================================================
;; TRUNCATION STRATEGIES
;; =============================================================================

(defn truncate-simple
  "Simple truncation to 19 chars, then pad"
  [title]
  (pad-title-to-19 (subs title 0 (min (count title) max-title-length))))

(defn truncate-smart
  "Smart truncation - remove vowels from middle first, then pad"
  [title]
  (if (<= (count title) max-title-length)
    (pad-title-to-19 title)
    (let [;; Try removing spaces first
          no-spaces (str/replace title #"\s+" "")
          
          ;; If still too long, remove vowels from middle
          remove-vowels (fn [s]
                          (let [chars (vec s)
                                mid-start (quot (count chars) 3)
                                mid-end (* 2 (quot (count chars) 3))]
                            (str/join
                             (map-indexed
                              (fn [i c]
                                (if (and (>= i mid-start)
                                         (<= i mid-end)
                                         (re-find #"[aeiouAEIOU]" (str c)))
                                  ""
                                  c))
                              chars))))]
      
      (cond
        (<= (count no-spaces) max-title-length)
        (pad-title-to-19 no-spaces)
        
        :else
        (pad-title-to-19 (truncate-simple (remove-vowels no-spaces)))))))

(defn truncate-abbreviate
  "Abbreviate words intelligently"
  [title]
  (let [words (str/split title #"[-\s]+")
        abbreviated (map (fn [word]
                          (cond
                            ;; Keep short words
                            (<= (count word) 4) word
                            
                            ;; Common abbreviations
                            (= word "complete") "cmpl"
                            (= word "website") "web"
                            (= word "structure") "struct"
                            (= word "foundation") "found"
                            (= word "development") "dev"
                            (= word "documentation") "docs"
                            (= word "implementation") "impl"
                            
                            ;; First 4 chars + last char
                            :else (str (subs word 0 4) (last word))))
                        words)
        result (str/join "-" abbreviated)]
    (pad-title-to-19 (truncate-simple result))))

;; =============================================================================
;; SUGGESTION GENERATION
;; =============================================================================

(defn generate-title-suggestions
  "Generate multiple 19-char title suggestions
  
  Returns list of suggestions using different strategies"
  [original-title]
  (let [validation (validate-title-length original-title)]
    
    (if (:valid? validation)
      ;; Already valid - return as-is
      [{:title original-title
        :length (count original-title)
        :strategy :original
        :note "Already valid - perfect length!"}]
      
      ;; Generate suggestions
      (let [suggestions
            [{:title (truncate-simple original-title)
              :length (count (truncate-simple original-title))
              :strategy :simple-truncate
              :note "Simple cut at 19 chars"}
             
             {:title (truncate-smart original-title)
              :length (count (truncate-smart original-title))
              :strategy :smart-truncate
              :note "Remove spaces + middle vowels"}
             
             {:title (truncate-abbreviate original-title)
              :length (count (truncate-abbreviate original-title))
              :strategy :abbreviate
              :note "Abbreviate words intelligently"}]]
        
        ;; Deduplicate and ensure all are valid
        (->> suggestions
             (filter #(<= (:length %) max-title-length))
             (distinct)
             (take 3))))))

;; =============================================================================
;; INTERACTIVE MODE
;; =============================================================================

(defn interactive-title-selector
  "Interactive title selection with suggestions
  
  Shows user multiple options and lets them choose"
  [original-title]
  (let [suggestions (generate-title-suggestions original-title)
        validation (validate-title-length original-title)]
    
    (println "\n📏 Grainpath Title Validator\n")
    (println "Original title:" original-title)
    (println "Length:" (:length validation) "chars")
    
    (if (:valid? validation)
      (do
        (println "Status: ✅ Valid (≤19 chars)")
        original-title)
      
      (do
        (println "Status: ❌ Too long by" (:over-by validation) "chars")
        (println "\n💡 Suggestions (max 19 chars):\n")
        
        (doseq [[idx suggestion] (map-indexed vector suggestions)]
          (printf "  %d. %-19s (%2d chars) - %s\n"
                  (inc idx)
                  (:title suggestion)
                  (:length suggestion)
                  (:note suggestion)))
        
        (println "\nEnter number to choose (or press Enter for option 1):")
        
        ;; For non-interactive contexts, return first suggestion
        (:title (first suggestions))))))

;; =============================================================================
;; PUBLIC API
;; =============================================================================

(defn enforce-title-length
  "Enforce 19-char title limit, return truncated version
  
  For programmatic use - returns best suggestion automatically"
  [title]
  (if (<= (count title) max-title-length)
    title
    (:title (first (generate-title-suggestions title)))))

(defn validate-grainpath-title
  "Validate and suggest titles for grainpath"
  [title]
  (generate-title-suggestions title))

(comment
  ;; Test with various titles
  (validate-title-length "team10-complete-web")
  ;; => {:valid? true, :length 19}
  
  (validate-title-length "team10-complete-website-with-features")
  ;; => {:valid? false, :over-by 19}
  
  (generate-title-suggestions "team10-complete-website-build")
  ;; => Multiple 19-char suggestions
  
  (enforce-title-length "this-is-a-very-long-title-that-needs-truncation")
  ;; => "this-is-a-very-long"
  )

