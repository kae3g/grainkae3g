(ns clojure-sixos.core
  "Main entry point for clojure-sixos library.
   
   Provides SixOS integration with intelligent typo handling for the Grain Network.
   
   This library makes typos a non-issue: clomoko = clotoko, grainmusik = grainmusic, etc."
  (:require [clojure-sixos.typo :as typo]
            [clojure-sixos.registry :as reg]
            [clojure-sixos.similarity :as sim]))

;; =============================================================================
;; Public API - Re-export commonly used functions
;; =============================================================================

;; Typo resolution
(def resolve-name typo/resolve-name)
(def resolve-names typo/resolve-names)
(def suggest typo/suggest)
(def suggest-many typo/suggest-many)

;; Validation
(def valid-name? typo/valid-name?)
(def exact-match? typo/exact-match?)
(def typo? typo/typo?)
(def alias? typo/alias?)

;; Registry management
(def register typo/register)
(def unregister typo/unregister)
(def all-registered typo/all-registered)

;; Similarity metrics
(def distance sim/levenshtein-distance)
(def similarity sim/similarity)
(def similar? typo/similar?)

;; Messages
(def autocorrect-message typo/autocorrect-message)
(def suggestion-message typo/suggestion-message)
(def resolve-with-message typo/resolve-with-message)

;; =============================================================================
;; Quick Start Examples
;; =============================================================================

(comment
  ;; Basic typo correction
  (resolve-name "clomoko")  ;; => "clotoko"
  (resolve-name "grainmusik")  ;; => "grainmusic"
  
  ;; Get suggestions
  (suggest "graincaly")
  ;; => {:canonical "grainclay"
  ;;     :suggestions ["grainclay"]
  ;;     :confidence 0.875
  ;;     :match-type :fuzzy}
  
  ;; Batch resolution
  (resolve-names ["clomoko" "grainmusik" "grainweb"])
  ;; => ["clotoko" "grainmusic" "grainweb"]
  
  ;; With user messages
  (resolve-with-message "clomoko")
  ;; => {:original "clomoko"
  ;;     :resolved "clotoko"
  ;;     :message "Autocorrected: clomoko → clotoko"}
  
  ;; Validation
  (valid-name? "clotoko")  ;; => true
  (valid-name? "clomoko")  ;; => true (known typo)
  (valid-name? "unknown")  ;; => false
  
  ;; Check match type
  (exact-match? "clotoko")  ;; => true
  (typo? "clomoko")  ;; => true
  (alias? "clojure-motoko")  ;; => true
  
  ;; Similarity metrics
  (distance "clomoko" "clotoko")  ;; => 1
  (similarity "clomoko" "clotoko")  ;; => 0.875
  (similar? "clomoko" "clotoko")  ;; => true
  
  ;; Register custom names
  (register "mynewpkg" {:typos ["my-new-pkg" "mynewpkg"]
                        :aliases ["mnp"]
                        :category :tool})
  
  (resolve-name "mynewpkg")  ;; => "mynewpkg"
  (resolve-name "my-new-pkg")  ;; => "mynewpkg"
  
  ;; Get all registered names
  (all-registered)
  ;; => ["clotoko" "grainmusic" "grainweb" ...]
  )

;; =============================================================================
;; Configuration
;; =============================================================================

(defn set-autocorrect-threshold!
  "Set global autocorrect threshold (0.0-1.0).
   
   Examples:
     (set-autocorrect-threshold! 0.8)  ; More strict
     (set-autocorrect-threshold! 0.6)  ; More lenient"
  [threshold]
  (alter-var-root #'typo/*autocorrect-threshold* (constantly threshold)))

(defn set-suggestion-threshold!
  "Set global suggestion threshold (0.0-1.0).
   
   Examples:
     (set-suggestion-threshold! 0.6)"
  [threshold]
  (alter-var-root #'typo/*suggestion-threshold* (constantly threshold)))

(defn enable-autocorrect!
  "Enable automatic correction."
  []
  (alter-var-root #'typo/*autocorrect-enabled* (constantly true)))

(defn disable-autocorrect!
  "Disable automatic correction (suggestions only)."
  []
  (alter-var-root #'typo/*autocorrect-enabled* (constantly false)))

;; =============================================================================
;; Version
;; =============================================================================

(def version "0.1.0")

(defn -main
  "Print version information."
  [& args]
  (println "clojure-sixos" version)
  (println "Typo-tolerant SixOS integration for the Grain Network")
  (println)
  (println "Registered packages:")
  (doseq [name (sort (all-registered))]
    (println " -" name)))

