(ns clojure-sixos.examples.usage
  "Example usage of clojure-sixos typo handling."
  (:require [clojure-sixos.core :as sixos]))

;; =============================================================================
;; Example 1: Basic Typo Correction
;; =============================================================================

(defn example-basic-typo-correction
  []
  (println "=== Example 1: Basic Typo Correction ===\n")
  
  ;; User types "clomoko" instead of "clotoko"
  (let [result (sixos/resolve-name "clomoko")]
    (println "User typed: clomoko")
    (println "Resolved to:" result)
    (println))
  
  ;; User types "grainmusik" instead of "grainmusic"
  (let [result (sixos/resolve-name "grainmusik")]
    (println "User typed: grainmusik")
    (println "Resolved to:" result)
    (println)))

;; =============================================================================
;; Example 2: Batch Resolution
;; =============================================================================

(defn example-batch-resolution
  []
  (println "=== Example 2: Batch Resolution ===\n")
  
  (let [typos ["clomoko" "grainmusik" "graincaly" "grain-web"]
        resolved (sixos/resolve-names typos)]
    (println "Original names:" typos)
    (println "Resolved names:" resolved)
    (println)))

;; =============================================================================
;; Example 3: User-Friendly Messages
;; =============================================================================

(defn example-messages
  []
  (println "=== Example 3: User-Friendly Messages ===\n")
  
  (doseq [name ["clomoko" "grainmusik" "graincaly" "clotoko"]]
    (let [{:keys [original resolved message]} (sixos/resolve-with-message name)]
      (println "Input:" original)
      (println "Output:" resolved)
      (when message
        (println "Message:" message))
      (println))))

;; =============================================================================
;; Example 4: Getting Suggestions
;; =============================================================================

(defn example-suggestions
  []
  (println "=== Example 4: Getting Suggestions ===\n")
  
  (doseq [name ["clomok" "grainmusi" "graincl"]]
    (let [{:keys [canonical suggestions confidence match-type]} (sixos/suggest name)]
      (println "Query:" name)
      (println "Best match:" canonical)
      (println "All suggestions:" suggestions)
      (println "Confidence:" confidence)
      (println "Match type:" match-type)
      (println))))

;; =============================================================================
;; Example 5: CLI Application
;; =============================================================================

(defn simulate-cli-install
  "Simulate a CLI application that installs packages with typo handling."
  [package-name]
  (let [{:keys [original resolved message]} (sixos/resolve-with-message package-name)]
    (when message
      (println message))
    (println (format "Installing %s..." resolved))
    (println "✓ Installation complete!")))

(defn example-cli-application
  []
  (println "=== Example 5: CLI Application ===\n")
  
  (println "$ grain install clomoko")
  (simulate-cli-install "clomoko")
  (println)
  
  (println "$ grain install grainmusik")
  (simulate-cli-install "grainmusik")
  (println)
  
  (println "$ grain install grainweb")
  (simulate-cli-install "grainweb")
  (println))

;; =============================================================================
;; Example 6: Service Management
;; =============================================================================

(defn simulate-service-command
  [command service-name]
  (let [{:keys [original resolved message]} (sixos/resolve-with-message service-name)]
    (when message
      (println message))
    (println (format "%s %s..." command resolved))))

(defn example-service-management
  []
  (println "=== Example 6: Service Management ===\n")
  
  (println "$ s6-svc -u clomoko-daemon")
  (simulate-service-command "Starting" "clomoko-daemon")
  (println)
  
  (println "$ s6-svc -d grainmusik-server")
  (simulate-service-command "Stopping" "grainmusik-server")
  (println)
  
  (println "$ s6-svstat graincaly-daemon")
  (simulate-service-command "Status for" "graincaly-daemon")
  (println))

;; =============================================================================
;; Example 7: Validation
;; =============================================================================

(defn example-validation
  []
  (println "=== Example 7: Validation ===\n")
  
  (doseq [name ["clotoko" "clomoko" "unknown" "grainmusik"]]
    (println (format "%-15s valid? %-5s exact? %-5s typo? %-5s"
                    name
                    (sixos/valid-name? name)
                    (sixos/exact-match? name)
                    (sixos/typo? name))))
  (println))

;; =============================================================================
;; Example 8: Similarity Metrics
;; =============================================================================

(defn example-similarity
  []
  (println "=== Example 8: Similarity Metrics ===\n")
  
  (let [pairs [["clomoko" "clotoko"]
               ["grainmusik" "grainmusic"]
               ["graincaly" "grainclay"]
               ["foo" "grainweb"]]]
    (doseq [[s1 s2] pairs]
      (println (format "%-15s vs %-15s distance: %d similarity: %.3f similar? %s"
                      s1 s2
                      (sixos/distance s1 s2)
                      (sixos/similarity s1 s2)
                      (sixos/similar? s1 s2)))))
  (println))

;; =============================================================================
;; Example 9: Custom Registration
;; =============================================================================

(defn example-custom-registration
  []
  (println "=== Example 9: Custom Registration ===\n")
  
  ;; Register a custom package
  (sixos/register "mypkg" {:typos ["my-pkg" "mypkg"]
                           :aliases ["mp"]
                           :category :tool})
  
  (println "Registered: mypkg")
  (println "Resolving 'my-pkg':" (sixos/resolve-name "my-pkg"))
  (println "Resolving 'mp':" (sixos/resolve-name "mp"))
  (println)
  
  ;; Unregister
  (sixos/unregister "mypkg")
  (println "Unregistered: mypkg"))

;; =============================================================================
;; Run All Examples
;; =============================================================================

(defn -main
  []
  (example-basic-typo-correction)
  (example-batch-resolution)
  (example-messages)
  (example-suggestions)
  (example-cli-application)
  (example-service-management)
  (example-validation)
  (example-similarity)
  (example-custom-registration))

;; Run with: bb -m clojure-sixos.examples.usage

