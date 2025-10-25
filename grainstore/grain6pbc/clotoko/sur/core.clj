(ns clotoko.sur.core
  "Core data structure definitions inspired by Urbit's Hoon/Arvo sur folder.
   Defines fundamental types using Clojure spec with Haskell-inspired strong typing."
  (:require
   [clojure.spec.alpha :as s]
   [clojure.spec.gen.alpha :as gen]))

;; =============================================================================
;; Primitive Types (Haskell-inspired)
;; =============================================================================

;; Text/String types
(s/def ::text
  (s/and string?
          #(<= 0 (count %) 1024)
          #(re-matches #"[^\x00-\x1F\x7F]*" %)))

(s/def ::text-short
  (s/and string?
          #(<= 0 (count %) 64)
          #(re-matches #"[a-zA-Z0-9._-]*" %)))

(s/def ::text-long
  (s/and string?
          #(<= 0 (count %) 10000)
          #(re-matches #"[^\x00-\x1F\x7F]*" %)))

;; Numeric types
(s/def ::nat
  (s/and int?
          #(>= % 0)))

(s/def ::pos-int
  (s/and int?
          #(> % 0)))

(s/def ::neg-int
  (s/and int?
          #(< % 0)))

(s/def ::float
  (s/and double?
          #(not (Double/isNaN %))
          #(not (Double/isInfinite %))))

;; Boolean types
(s/def ::bool
  boolean?)

(s/def ::maybe-bool
  (s/nilable boolean?))

;; Temporal types
(s/def ::timestamp
  (s/and int?
          #(> % 0)
          #(< % 4102444800000))) ; Year 2100

(s/def ::duration
  (s/and int?
          #(>= % 0)))

;; =============================================================================
;; Algebraic Data Types (Haskell-style)
;; =============================================================================

;; Maybe type
(s/def ::maybe
  (s/or :just any?
        :nothing #{nil}))

;; Either type
(s/def ::either
  (s/or :left any?
        :right any?))

;; Result type (for error handling)
(s/def ::result
  (s/or :ok any?
        :error ::text))

;; Option type (similar to Maybe but with explicit none)
(s/def ::option
  (s/or :some any?
        :none #{:none}))

;; =============================================================================
;; Collection Types
;; =============================================================================

;; Lists
(s/def ::list
  (s/coll-of any? :kind vector?))

(s/def ::non-empty-list
  (s/and ::list
          #(> (count %) 0)))

(s/def ::list-of-text
  (s/coll-of ::text :kind vector?))

;; Sets
(s/def ::set
  (s/coll-of any? :kind set?))

(s/def ::set-of-text
  (s/coll-of ::text :kind set?))

;; Maps
(s/def ::map
  (s/map-of any? any?))

(s/def ::map-of-text
  (s/map-of ::text any?))

;; =============================================================================
;; Identity and Reference Types
;; =============================================================================

;; Unique identifiers
(s/def ::id
  (s/and string?
          #(re-matches #"^[a-zA-Z0-9_-]{8,64}$" %)))

(s/def ::uuid
  (s/and string?
          #(re-matches #"^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$" %)))

(s/def ::hash
  (s/and string?
          #(re-matches #"^[a-f0-9]{32,64}$" %)))

;; References
(s/def ::ref
  (s/and string?
          #(re-matches #"^[a-zA-Z0-9._-]+$" %)))

(s/def ::path
  (s/and string?
          #(re-matches #"^/[a-zA-Z0-9._-]+(/[a-zA-Z0-9._-]+)*$" %)))

;; =============================================================================
;; Network and Protocol Types
;; =============================================================================

;; IP addresses
(s/def ::ipv4
  (s/and string?
          #(re-matches #"^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$" %)))

(s/def ::ipv6
  (s/and string?
          #(re-matches #"^[0-9a-fA-F:]+$" %)))

(s/def ::ip-address
  (s/or :ipv4 ::ipv4
        :ipv6 ::ipv6))

;; Ports
(s/def ::port
  (s/and int?
          #(>= % 1)
          #(<= % 65535)))

;; Endpoints
(s/def ::endpoint
  (s/keys :req-un [::ip-address ::port]))

;; URLs
(s/def ::url
  (s/and string?
          #(re-matches #"^https?://[^\s/$.?#].[^\s]*$" %)))

;; =============================================================================
;; Cryptographic Types
;; =============================================================================

;; Keys
(s/def ::public-key
  (s/and string?
          #(re-matches #"^[A-Za-z0-9+/=]{32,}$" %)))

(s/def ::private-key
  (s/and string?
          #(re-matches #"^[A-Za-z0-9+/=]{32,}$" %)))

(s/def ::signature
  (s/and string?
          #(re-matches #"^[A-Za-z0-9+/=]{32,}$" %)))

;; =============================================================================
;; Status and State Types
;; =============================================================================

;; Status enums
(s/def ::status
  #{:pending :active :inactive :suspended :archived})

(s/def ::visibility
  #{:public :private :unlisted :followers-only})

(s/def ::priority
  #{:low :normal :high :critical})

;; =============================================================================
;; Validation and Error Types
;; =============================================================================

;; Validation results
(s/def ::validation-error
  (s/keys :req-un [::field ::message ::code]))

(s/def ::validation-result
  (s/keys :req-un [::valid?]
          :opt-un [::errors]))

;; =============================================================================
;; Generator Functions
;; =============================================================================

(defn gen-text
  "Generate random text within length bounds."
  [min-len max-len]
  (gen/fmap
   #(apply str %)
   (gen/vector
    (gen/char-alpha)
    min-len max-len)))

(defn gen-id
  "Generate random ID."
  []
  (gen/fmap
   #(apply str %)
   (gen/vector
    (gen/one-of
     [(gen/char-alpha)
      (gen/char-alphanumeric)
      (gen/elements [\_ \-])])
    8 32)))

(defn gen-timestamp
  "Generate random timestamp."
  []
  (gen/large-integer
   {:min 1000000000000  ; 2001
    :max 4102444800000})) ; 2100

;; =============================================================================
;; Type Conversion Functions
;; =============================================================================

(defn text->maybe
  "Convert text to Maybe type."
  [text]
  (if (empty? text)
    :nothing
    [:just text]))

(defn maybe->text
  "Convert Maybe type to text."
  [maybe]
  (case (first maybe)
    :just (second maybe)
    :nothing ""))

(defn result->either
  "Convert Result to Either type."
  [result]
  (case (first result)
    :ok [:right (second result)]
    :error [:left (second result)]))

(defn either->result
  "Convert Either to Result type."
  [either]
  (case (first either)
    :left [:error (second either)]
    :right [:ok (second either)]))

;; =============================================================================
;; Type Predicates
;; =============================================================================

(defn text?
  "Check if value is valid text."
  [x]
  (s/valid? ::text x))

(defn id?
  "Check if value is valid ID."
  [x]
  (s/valid? ::id x))

(defn timestamp?
  "Check if value is valid timestamp."
  [x]
  (s/valid? ::timestamp x))

(defn endpoint?
  "Check if value is valid endpoint."
  [x]
  (s/valid? ::endpoint x))

;; =============================================================================
;; Type Utilities
;; =============================================================================

(defn safe-get
  "Safely get value from map with type checking."
  [m k spec]
  (let [v (get m k)]
    (if (s/valid? spec v)
      [:ok v]
      [:error (str "Invalid type for key " k)])))

(defn safe-assoc
  "Safely associate value in map with type checking."
  [m k v spec]
  (if (s/valid? spec v)
    [:ok (assoc m k v)]
    [:error (str "Invalid type for key " k)]))

(defn validate-map
  "Validate entire map against spec map."
  [m spec-map]
  (let [errors (for [[k spec] spec-map
                     :when (not (s/valid? spec (get m k)))]
                 {:field k
                  :message (str "Invalid type for " k)
                  :code :type-mismatch})]
    {:valid? (empty? errors)
     :errors errors}))

