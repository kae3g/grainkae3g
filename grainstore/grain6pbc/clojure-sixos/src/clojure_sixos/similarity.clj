(ns clojure-sixos.similarity
  "String similarity and distance algorithms for typo detection.
   
   Uses Levenshtein distance, phonetic matching, and custom heuristics
   to detect and correct common typos in package names, service names,
   and module names across the Grain Network."
  (:require [clj-fuzzy.metrics :as metrics]
            [clj-fuzzy.phonetics :as phonetics]))

;; =============================================================================
;; Levenshtein Distance (Edit Distance)
;; =============================================================================

(defn levenshtein-distance
  "Calculate the Levenshtein distance between two strings.
   
   The Levenshtein distance is the minimum number of single-character edits
   (insertions, deletions, or substitutions) required to change one string
   into the other.
   
   Examples:
     (levenshtein-distance \"clomoko\" \"clotoko\") => 1
     (levenshtein-distance \"grainmusik\" \"grainmusic\") => 1
     (levenshtein-distance \"grain\" \"grainweb\") => 3"
  [s1 s2]
  (metrics/levenshtein s1 s2))

;; =============================================================================
;; Similarity Score
;; =============================================================================

(defn similarity
  "Calculate similarity score between two strings (0.0 to 1.0).
   
   Higher scores indicate greater similarity. A score of 1.0 means identical
   strings, while 0.0 means completely different.
   
   Formula: 1 - (distance / max-length)
   
   Examples:
     (similarity \"clomoko\" \"clotoko\") => 0.875
     (similarity \"grainweb\" \"grainweb\") => 1.0
     (similarity \"foo\" \"bar\") => 0.0"
  [s1 s2]
  (let [dist (levenshtein-distance s1 s2)
        max-len (max (count s1) (count s2))]
    (if (zero? max-len)
      1.0
      (- 1.0 (/ (double dist) max-len)))))

;; =============================================================================
;; Phonetic Matching
;; =============================================================================

(defn soundex
  "Generate Soundex code for a string.
   
   Soundex is a phonetic algorithm for indexing names by sound.
   Two strings that sound similar should have the same Soundex code.
   
   Examples:
     (soundex \"clomoko\") => \"C452\"
     (soundex \"clotoko\") => \"C432\"
     (soundex \"Robert\") => \"R163\"
     (soundex \"Rupert\") => \"R163\""
  [s]
  (phonetics/soundex s))

(defn metaphone
  "Generate Metaphone code for a string.
   
   Metaphone is an improved phonetic algorithm that better handles
   English pronunciation rules.
   
   Examples:
     (metaphone \"clomoko\") => \"KLMK\"
     (metaphone \"clotoko\") => \"KLTK\""
  [s]
  (phonetics/metaphone s))

(defn sounds-like?
  "Check if two strings sound similar using phonetic matching.
   
   Uses both Soundex and Metaphone for better accuracy.
   
   Examples:
     (sounds-like? \"clomoko\" \"clotoko\") => false (different sounds)
     (sounds-like? \"Robert\" \"Rupert\") => true (similar sounds)"
  [s1 s2]
  (or (= (soundex s1) (soundex s2))
      (= (metaphone s1) (metaphone s2))))

;; =============================================================================
;; Common Substitutions
;; =============================================================================

(def common-substitutions
  "Common character substitutions that indicate likely typos."
  [["o" "a"]   ; clomoko <-> clamako
   ["i" "y"]   ; grainmusic <-> grainmusyc
   ["c" "k"]   ; clotoko <-> klotoko
   ["-" "_"]   ; grain-web <-> grain_web
   ["-" ""]    ; grain-web <-> grainweb
   ["0" "o"]   ; clo0oko <-> cloaoko
   ["1" "l"]   ; c1otoko <-> clotoko
   ["5" "s"]])  ; grain5pace <-> grainspace

(defn apply-substitution
  "Apply a single character substitution to a string.
   
   Returns all possible variations of the string with one substitution applied.
   
   Examples:
     (apply-substitution \"clomoko\" [\"o\" \"a\"])
     => (\"clamoko\" \"clomako\" \"clomoka\")"
  [s [from to]]
  (let [pattern (re-pattern from)]
    (loop [pos 0
           results []]
      (if-let [match (re-find pattern (subs s pos))]
        (let [idx (+ pos (.indexOf (subs s pos) match))]
          (recur (inc idx)
                 (conj results (str (subs s 0 idx)
                                   to
                                   (subs s (inc idx))))))
        results))))

(defn generate-substitution-candidates
  "Generate all possible single-substitution variations of a string.
   
   Applies each common substitution once to generate candidates.
   
   Examples:
     (generate-substitution-candidates \"clomoko\")
     => [\"clamoko\" \"clomako\" \"clomoka\" \"klomoko\" ...]"
  [s]
  (mapcat #(apply-substitution s %) common-substitutions))

;; =============================================================================
;; Closest Match
;; =============================================================================

(defn closest-match
  "Find the closest matching string from a list of candidates.
   
   Returns a map with :match, :distance, and :similarity.
   
   Examples:
     (closest-match \"clomoko\" [\"clotoko\" \"grainweb\" \"grainmusic\"])
     => {:match \"clotoko\" :distance 1 :similarity 0.875}"
  [target candidates]
  (when (seq candidates)
    (let [matches (map (fn [candidate]
                         {:match candidate
                          :distance (levenshtein-distance target candidate)
                          :similarity (similarity target candidate)})
                      candidates)
          best (apply min-key :distance matches)]
      best)))

;; =============================================================================
;; Prefix/Suffix Matching
;; =============================================================================

(defn has-prefix?
  "Check if a string starts with a given prefix.
   
   Examples:
     (has-prefix? \"grainweb\" \"grain\") => true
     (has-prefix? \"clotoko\" \"grain\") => false"
  [s prefix]
  (.startsWith s prefix))

(defn has-suffix?
  "Check if a string ends with a given suffix.
   
   Examples:
     (has-suffix? \"clotoko-daemon\" \"daemon\") => true
     (has-suffix? \"grainweb\" \"daemon\") => false"
  [s suffix]
  (.endsWith s suffix))

(defn extract-prefix
  "Extract the prefix of a string up to a delimiter.
   
   Examples:
     (extract-prefix \"grainweb-daemon\" \"-\") => \"grainweb\"
     (extract-prefix \"clotoko.core\" \".\") => \"clotoko\""
  [s delimiter]
  (first (clojure.string/split s (re-pattern delimiter))))

(defn extract-suffix
  "Extract the suffix of a string after a delimiter.
   
   Examples:
     (extract-suffix \"grainweb-daemon\" \"-\") => \"daemon\"
     (extract-suffix \"clotoko.core\" \".\") => \"core\""
  [s delimiter]
  (last (clojure.string/split s (re-pattern delimiter))))

;; =============================================================================
;; Confidence Scoring
;; =============================================================================

(defn confidence-score
  "Calculate confidence score for a match (0.0 to 1.0).
   
   Combines multiple factors:
   - Levenshtein distance (lower is better)
   - Similarity score (higher is better)
   - Phonetic matching (bonus points)
   - Length difference (penalty for large differences)
   
   Examples:
     (confidence-score \"clomoko\" \"clotoko\") => 0.9  (high confidence)
     (confidence-score \"foo\" \"grainweb\") => 0.1  (low confidence)"
  [s1 s2]
  (let [sim (similarity s1 s2)
        phonetic-match (if (sounds-like? s1 s2) 0.1 0.0)
        len-diff (Math/abs (- (count s1) (count s2)))
        len-penalty (/ len-diff (max (count s1) (count s2)))
        score (+ sim phonetic-match (- len-penalty))]
    (max 0.0 (min 1.0 score))))

;; =============================================================================
;; Normalization
;; =============================================================================

(defn normalize
  "Normalize a string for comparison.
   
   - Converts to lowercase
   - Removes hyphens and underscores
   - Trims whitespace
   
   Examples:
     (normalize \"Grain-Web\") => \"grainweb\"
     (normalize \"Clojure_SixOS\") => \"clojuresixos\""
  [s]
  (-> s
      clojure.string/lower-case
      (clojure.string/replace #"[-_]" "")
      clojure.string/trim))

(defn normalized-similarity
  "Calculate similarity after normalizing both strings.
   
   More forgiving than raw similarity - treats \"grain-web\" and \"grainweb\"
   as identical.
   
   Examples:
     (normalized-similarity \"Grain-Web\" \"grainweb\") => 1.0
     (normalized-similarity \"Clojure_SixOS\" \"clojure-sixos\") => 1.0"
  [s1 s2]
  (similarity (normalize s1) (normalize s2)))

