(ns clojure-sixos.typo
  "High-level typo detection and autocorrection API.
   
   This is the main namespace for typo handling in the Grain Network.
   It provides simple, user-friendly functions for resolving typos and
   generating suggestions."
  (:require [clojure-sixos.registry :as reg]
            [clojure-sixos.similarity :as sim]))

;; =============================================================================
;; Configuration
;; =============================================================================

(def ^:dynamic *autocorrect-threshold*
  "Minimum similarity score for automatic correction.
   Default: 0.75 (75% similarity required)"
  0.75)

(def ^:dynamic *suggestion-threshold*
  "Minimum similarity score for including in suggestions.
   Default: 0.5 (50% similarity required)"
  0.5)

(def ^:dynamic *max-suggestions*
  "Maximum number of suggestions to return.
   Default: 5"
  5)

(def ^:dynamic *autocorrect-enabled*
  "Whether automatic correction is enabled.
   Set to false to only suggest corrections without applying them.
   Default: true"
  true)

;; =============================================================================
;; Core Resolution Functions
;; =============================================================================

(defn resolve-name
  "Resolve a name to its canonical form with autocorrection.
   
   If the name is not found in the registry, attempts fuzzy matching.
   If a close match is found and autocorrection is enabled, returns
   the canonical name. Otherwise, returns the original name.
   
   Args:
     name - The name to resolve
     opts - Optional map with:
            :threshold - Minimum similarity for autocorrect (default 0.75)
            :autocorrect - Enable autocorrection (default true)
   
   Examples:
     (resolve-name \"clomoko\")  => \"clotoko\"
     (resolve-name \"grainmusik\")  => \"grainmusic\"
     (resolve-name \"unknown\")  => \"unknown\"
     (resolve-name \"clomoko\" {:autocorrect false})  => \"clomoko\""
  ([name] (resolve-name name {}))
  ([name {:keys [threshold autocorrect]
          :or {threshold *autocorrect-threshold*
               autocorrect *autocorrect-enabled*}}]
   (let [result (reg/lookup name {:threshold threshold})]
     (if (and autocorrect
              (:canonical result)
              (not= :not-found (:match-type result)))
       (:canonical result)
       name))))

(defn suggest
  "Get suggestions for a potentially misspelled name.
   
   Returns a map with:
   - :canonical - The best match (if any)
   - :suggestions - List of alternative suggestions
   - :confidence - Confidence score for best match (0.0-1.0)
   - :match-type - How the best match was found
   
   Args:
     name - The name to get suggestions for
     opts - Optional map with:
            :threshold - Minimum similarity for suggestions (default 0.5)
            :max-suggestions - Maximum number to return (default 5)
   
   Examples:
     (suggest \"clomoko\")
     => {:canonical \"clotoko\"
         :suggestions [\"clotoko\"]
         :confidence 0.875
         :match-type :typo}
     
     (suggest \"graincaly\")
     => {:canonical \"grainclay\"
         :suggestions [\"grainclay\"]
         :confidence 0.875
         :match-type :fuzzy}"
  ([name] (suggest name {}))
  ([name {:keys [threshold max-suggestions]
          :or {threshold *suggestion-threshold*
               max-suggestions *max-suggestions*}}]
   (let [lookup-result (reg/lookup name {:threshold threshold})
         all-names (reg/all-names)
         ;; Get similarity scores for all registered names
         scored-names (map (fn [canonical]
                            {:name canonical
                             :similarity (sim/similarity name canonical)
                             :confidence (sim/confidence-score name canonical)})
                          all-names)
         ;; Filter by threshold and sort by similarity
         candidates (->> scored-names
                        (filter #(>= (:similarity %) threshold))
                        (sort-by :similarity >)
                        (take max-suggestions)
                        (mapv :name))]
     {:canonical (:canonical lookup-result)
      :suggestions candidates
      :confidence (:confidence lookup-result)
      :match-type (:match-type lookup-result)})))

(defn confidence
  "Get confidence score for a name resolution (0.0-1.0).
   
   Returns 1.0 for exact matches, and decreasing scores for fuzzy matches.
   
   Examples:
     (confidence \"clotoko\")  => 1.0
     (confidence \"clomoko\")  => 1.0  (known typo)
     (confidence \"clomok\")  => 0.85  (fuzzy match)"
  [name]
  (:confidence (reg/lookup name)))

;; =============================================================================
;; Batch Operations
;; =============================================================================

(defn resolve-names
  "Resolve multiple names at once.
   
   Examples:
     (resolve-names [\"clomoko\" \"grainmusik\" \"grainweb\"])
     => [\"clotoko\" \"grainmusic\" \"grainweb\"]"
  ([names] (resolve-names names {}))
  ([names opts]
   (mapv #(resolve-name % opts) names)))

(defn suggest-many
  "Get suggestions for multiple names at once.
   
   Returns a map of {original-name => suggestions}.
   
   Examples:
     (suggest-many [\"clomoko\" \"graincaly\"])
     => {\"clomoko\" {:canonical \"clotoko\" ...}
         \"graincaly\" {:canonical \"grainclay\" ...}}"
  ([names] (suggest-many names {}))
  ([names opts]
   (into {} (map (fn [name] [name (suggest name opts)]) names))))

;; =============================================================================
;; Interactive Correction
;; =============================================================================

(defn autocorrect-message
  "Generate a user-friendly autocorrection message.
   
   Examples:
     (autocorrect-message \"clomoko\" \"clotoko\")
     => \"Autocorrected: clomoko → clotoko\"
     
     (autocorrect-message \"clotoko\" \"clotoko\")
     => nil  (no correction needed)"
  [original corrected]
  (when (not= original corrected)
    (format "Autocorrected: %s → %s" original corrected)))

(defn suggestion-message
  "Generate a user-friendly suggestion message.
   
   Examples:
     (suggestion-message \"clomoko\" [\"clotoko\" \"grainweb\"])
     => \"Did you mean 'clotoko'? Other suggestions: grainweb\"
     
     (suggestion-message \"unknown\" [])
     => \"No suggestions found for 'unknown'\""
  [original suggestions]
  (cond
    (empty? suggestions)
    (format "No suggestions found for '%s'" original)
    
    (= 1 (count suggestions))
    (format "Did you mean '%s'?" (first suggestions))
    
    :else
    (format "Did you mean '%s'? Other suggestions: %s"
            (first suggestions)
            (clojure.string/join ", " (rest suggestions)))))

(defn resolve-with-message
  "Resolve a name and return both the result and a user message.
   
   Returns a map with:
   - :original - Original name
   - :resolved - Resolved canonical name
   - :message - User-friendly message (if correction occurred)
   
   Examples:
     (resolve-with-message \"clomoko\")
     => {:original \"clomoko\"
         :resolved \"clotoko\"
         :message \"Autocorrected: clomoko → clotoko\"}
     
     (resolve-with-message \"clotoko\")
     => {:original \"clotoko\"
         :resolved \"clotoko\"
         :message nil}"
  ([name] (resolve-with-message name {}))
  ([name opts]
   (let [resolved (resolve-name name opts)
         message (autocorrect-message name resolved)]
     {:original name
      :resolved resolved
      :message message})))

;; =============================================================================
;; Validation
;; =============================================================================

(defn valid-name?
  "Check if a name exists in the registry (exact or fuzzy match).
   
   Examples:
     (valid-name? \"clotoko\")  => true
     (valid-name? \"clomoko\")  => true
     (valid-name? \"unknown\")  => false"
  ([name] (valid-name? name {}))
  ([name opts]
   (let [result (reg/lookup name opts)]
     (not= :not-found (:match-type result)))))

(defn exact-match?
  "Check if a name is an exact match with a canonical name.
   
   Examples:
     (exact-match? \"clotoko\")  => true
     (exact-match? \"clomoko\")  => false"
  [name]
  (= :exact (:match-type (reg/lookup name))))

(defn typo?
  "Check if a name is a known typo.
   
   Examples:
     (typo? \"clomoko\")  => true
     (typo? \"clotoko\")  => false"
  [name]
  (= :typo (:match-type (reg/lookup name))))

(defn alias?
  "Check if a name is a known alias.
   
   Examples:
     (alias? \"clojure-motoko\")  => true
     (alias? \"clotoko\")  => false"
  [name]
  (= :alias (:match-type (reg/lookup name))))

;; =============================================================================
;; Utilities
;; =============================================================================

(defn distance
  "Get edit distance between two names.
   
   Examples:
     (distance \"clomoko\" \"clotoko\")  => 1
     (distance \"grainmusik\" \"grainmusic\")  => 1"
  [name1 name2]
  (sim/levenshtein-distance name1 name2))

(defn similar?
  "Check if two names are similar (within threshold).
   
   Args:
     name1 - First name
     name2 - Second name
     threshold - Minimum similarity (default 0.75)
   
   Examples:
     (similar? \"clomoko\" \"clotoko\")  => true
     (similar? \"foo\" \"grainweb\")  => false"
  ([name1 name2] (similar? name1 name2 *autocorrect-threshold*))
  ([name1 name2 threshold]
   (>= (sim/similarity name1 name2) threshold)))

;; =============================================================================
;; Registry Management
;; =============================================================================

(defn register
  "Register a new name in the typo registry.
   
   Examples:
     (register \"mynewpkg\" {:typos [\"mynewpkg\" \"my-new-pkg\"]
                            :aliases [\"mnp\"]
                            :category :tool})"
  [name opts]
  (reg/register-name name opts))

(defn unregister
  "Unregister a name from the typo registry.
   
   Examples:
     (unregister \"mynewpkg\")"
  [name]
  (reg/unregister-name name))

(defn all-registered
  "Get all registered names.
   
   Examples:
     (all-registered)
     => [\"clotoko\" \"grainmusic\" \"grainweb\" ...]"
  []
  (reg/all-names))

