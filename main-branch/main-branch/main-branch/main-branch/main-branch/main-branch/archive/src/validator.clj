(ns kae3g.validator
  "Validate robotic farm DSL structures using
   clojure.spec with Divine Grace awareness"
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as str]))

;; Core document specifications
(s/def ::number pos-int?)
(s/def ::title string?)
(s/def ::filepath string?)
(s/def ::frontmatter (s/nilable string?))
(s/def ::body string?)
(s/def ::sacred-quotes (s/coll-of string?))
(s/def ::html-content string?)

;; Consciousness type specifications
(s/def ::robotic-consciousness boolean?)
(s/def ::farm-consciousness boolean?)
(s/def ::nixtaveganic-integration boolean?)
(s/def ::community-coordination boolean?)
(s/def ::ecological-principles boolean?)
(s/def ::sacred-technology boolean?)

(s/def ::consciousness-type
  (s/keys :req-un [::robotic-consciousness
                   ::farm-consciousness
                   ::nixtaveganic-integration
                   ::community-coordination
                   ::ecological-principles
                   ::sacred-technology]))

;; Code and configuration blocks
(s/def ::code-blocks (s/coll-of string?))
(s/def ::yaml-blocks (s/coll-of string?))

;; Complete document specification
(s/def ::document
  (s/keys :req [:document/number
                :document/title
                :document/filepath
                :document/body
                :document/sacred-quotes
                :document/consciousness-type
                :document/code-blocks
                :document/yaml-blocks
                :document/html-content]
          :opt [:document/frontmatter]))

(defn validate-document
  "Validate single document against spec with
   contemplative error reporting"
  [doc]
  (if (s/valid? ::document doc)
    {:valid? true
     :document doc}
    {:valid? false
     :document doc
     :errors (s/explain-data ::document doc)}))

(defn validate-all-documents
  "Validate collection of parsed documents
   with Divine Grace consciousness"
  [documents]
  (println "🔍 Validating robotic farm documents")
  (let [results (mapv validate-document
                      documents)
        valid-count (count
                     (filter :valid? results))
        total-count (count results)]
    (println "✨ Valid:" valid-count
             "/" total-count)
    (when (< valid-count total-count)
      (println "⚠️  Invalid documents detected")
      (doseq [result results]
        (when-not (:valid? result)
          (println "❌"
                   (:document/title
                    (:document result)))
          (println "   Errors:"
                   (:errors result)))))
    {:valid-count valid-count
     :total-count total-count
     :results results}))

(defn -main
  "Sacred validation entry point for robotic
   farm consciousness document pipeline"
  [& _args]
  (println "🌙 Robotic Farm Validator:")
  (println "   Awakening spec consciousness")
  (let [documents (read-string
                   (slurp
                    "build/parsed-documents.edn"))
        validation (validate-all-documents
                    documents)]
    (if (= (:valid-count validation)
           (:total-count validation))
      (do (println "🤖 All documents valid")
          (spit "build/validated-documents.edn"
                (pr-str
                 (mapv :document
                       (:results validation))))
          (println "📝 Saved validated docs"))
      (do (println "⚠️  Validation failed")
          (System/exit 1)))))

