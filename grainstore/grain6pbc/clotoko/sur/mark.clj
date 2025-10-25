(ns clotoko.sur.mark
  "Urbit Hoon Arvo Zuse 'Mark' system for networked types.
   Implements mark definitions for data validation and transformation across the network."
  (:require
   [clojure.spec.alpha :as s]
   [clojure.spec.gen.alpha :as gen]
   [clojure.string :as str]
   [clotoko.sur.core :as core]
   [clotoko.sur.social :as social]))

;; =============================================================================
;; Mark System Core
;; =============================================================================

;; Mark definition
(s/def ::mark-name
  (s/and core/text-short
          #(re-matches #"^[a-zA-Z0-9_-]+$" %)
          #(<= 1 (count %) 32)))

(s/def ::mark-version
  (s/and core/text
          #(re-matches #"^\d+\.\d+\.\d+$" %)))

(s/def ::mark-description
  core/text)

(s/def ::mark-spec
  any?) ; Clojure spec definition

(s/def ::mark-validator
  fn?)

(s/def ::mark-transformer
  fn?)

(s/def ::mark-serializer
  fn?)

(s/def ::mark-deserializer
  fn?)

(s/def ::mark
  (s/keys :req-un [::mark-name
                   ::mark-version
                   ::mark-spec
                   ::mark-validator]
          :opt-un [::mark-description
                   ::mark-transformer
                   ::mark-serializer
                   ::mark-deserializer]))

;; =============================================================================
;; Core Marks
;; =============================================================================

;; Text mark
(def text-mark
  {:mark-name "text"
   :mark-version "1.0.0"
   :mark-description "Plain text content"
   :mark-spec ::core/text
   :mark-validator #(s/valid? ::core/text %)
   :mark-serializer str
   :mark-deserializer identity})

;; JSON mark
(def json-mark
  {:mark-name "json"
   :mark-version "1.0.0"
   :mark-description "JSON data structure"
   :mark-spec ::core/map
   :mark-validator #(s/valid? ::core/map %)
   :mark-serializer #(clojure.data.json/write-str %)
   :mark-deserializer #(clojure.data.json/read-str % :key-fn keyword)})

;; EDN mark
(def edn-mark
  {:mark-name "edn"
   :mark-version "1.0.0"
   :mark-description "EDN data structure"
   :mark-spec ::core/map
   :mark-validator #(s/valid? ::core/map %)
   :mark-serializer pr-str
   :mark-deserializer read-string})

;; Binary mark
(def binary-mark
  {:mark-name "binary"
   :mark-version "1.0.0"
   :mark-description "Binary data"
   :mark-spec ::core/text
   :mark-validator #(s/valid? ::core/text %)
   :mark-serializer #(.getBytes % "UTF-8")
   :mark-deserializer #(String. % "UTF-8")})

;; =============================================================================
;; Social Network Marks
;; =============================================================================

;; User mark
(def user-mark
  {:mark-name "user"
   :mark-version "1.0.0"
   :mark-description "User profile data"
   :mark-spec ::social/user
   :mark-validator #(s/valid? ::social/user %)
   :mark-serializer social/user->map
   :mark-deserializer social/map->user})

;; Post mark
(def post-mark
  {:mark-name "post"
   :mark-version "1.0.0"
   :mark-description "Social media post"
   :mark-spec ::social/post
   :mark-validator #(s/valid? ::social/post %)
   :mark-serializer social/post->map
   :mark-deserializer social/map->post})

;; Status mark
(def status-mark
  {:mark-name "status"
   :mark-version "1.0.0"
   :mark-description "User status update"
   :mark-spec ::social/status
   :mark-validator #(s/valid? ::social/status %)
   :mark-serializer #(into {} (map (fn [[k v]] [(name k) v]) %))
   :mark-deserializer #(into {} (map (fn [[k v]] [(keyword k) v]) %))})

;; Branch mark
(def branch-mark
  {:mark-name "branch"
   :mark-version "1.0.0"
   :mark-description "Development branch"
   :mark-spec ::social/branch
   :mark-validator #(s/valid? ::social/branch %)
   :mark-serializer #(into {} (map (fn [[k v]] [(name k) v]) %))
   :mark-deserializer #(into {} (map (fn [[k v]] [(keyword k) v]) %))})

;; =============================================================================
;; Grain Network Marks
;; =============================================================================

;; Grainwriter document mark
(s/def ::grainwriter-doc
  (s/keys :req-un [::doc-id
                   ::doc-title
                   ::doc-content
                   ::doc-created-at
                   ::doc-modified-at]
          :opt-un [::doc-author
                   ::doc-tags
                   ::doc-version
                   ::doc-branch]))

(def grainwriter-doc-mark
  {:mark-name "grainwriter-doc"
   :mark-version "1.0.0"
   :mark-description "Grainwriter document"
   :mark-spec ::grainwriter-doc
   :mark-validator #(s/valid? ::grainwriter-doc %)
   :mark-serializer edn-mark
   :mark-deserializer edn-mark})

;; Clotoko source mark
(s/def ::clotoko-source
  (s/keys :req-un [::source-id
                   ::source-content
                   ::source-language
                   ::source-created-at]
          :opt-un [::source-author
                   ::source-version
                   ::source-dependencies]))

(def clotoko-source-mark
  {:mark-name "clotoko-source"
   :mark-version "1.0.0"
   :mark-description "Clotoko source code"
   :mark-spec ::clotoko-source
   :mark-validator #(s/valid? ::clotoko-source %)
   :mark-serializer edn-mark
   :mark-deserializer edn-mark})

;; ICP canister mark
(s/def ::icp-canister
  (s/keys :req-un [::canister-id
                   ::canister-name
                   ::canister-version
                   ::canister-wasm]
          :opt-un [::canister-candid
                   ::canister-dependencies
                   ::canister-metadata]))

(def icp-canister-mark
  {:mark-name "icp-canister"
   :mark-version "1.0.0"
   :mark-description "ICP canister data"
   :mark-spec ::icp-canister
   :mark-validator #(s/valid? ::icp-canister %)
   :mark-serializer edn-mark
   :mark-deserializer edn-mark})

;; =============================================================================
;; Mark Registry
;; =============================================================================

(def mark-registry
  {"text" text-mark
   "json" json-mark
   "edn" edn-mark
   "binary" binary-mark
   "user" user-mark
   "post" post-mark
   "status" status-mark
   "branch" branch-mark
   "grainwriter-doc" grainwriter-doc-mark
   "clotoko-source" clotoko-source-mark
   "icp-canister" icp-canister-mark})

;; =============================================================================
;; Mark Operations
;; =============================================================================

(defn get-mark
  "Get mark definition by name."
  [mark-name]
  (get mark-registry mark-name))

(defn register-mark!
  "Register a new mark."
  [mark]
  (when (s/valid? ::mark mark)
    (alter-var-root #'mark-registry assoc (:mark-name mark) mark)
    (println "Registered mark:" (:mark-name mark))))

(defn validate-mark
  "Validate data against mark."
  [mark-name data]
  (if-let [mark (get-mark mark-name)]
    ((:mark-validator mark) data)
    false))

(defn serialize-mark
  "Serialize data using mark."
  [mark-name data]
  (if-let [mark (get-mark mark-name)]
    (if-let [serializer (:mark-serializer mark)]
      (serializer data)
      data)
    (throw (ex-info "Unknown mark" {:mark-name mark-name}))))

(defn deserialize-mark
  "Deserialize data using mark."
  [mark-name data]
  (if-let [mark (get-mark mark-name)]
    (if-let [deserializer (:mark-deserializer mark)]
      (deserializer data)
      data)
    (throw (ex-info "Unknown mark" {:mark-name mark-name}))))

(defn transform-mark
  "Transform data from one mark to another."
  [from-mark to-mark data]
  (let [deserialized (deserialize-mark from-mark data)]
    (serialize-mark to-mark deserialized)))

;; =============================================================================
;; Network Protocol Marks
;; =============================================================================

;; Message mark
(s/def ::message-id
  core/id)

(s/def ::message-sender
  core/id)

(s/def ::message-receiver
  core/id)

(s/def ::message-type
  #{:request :response :notification :error})

(s/def ::message-payload
  any?)

(s/def ::message-timestamp
  core/timestamp)

(s/def ::message
  (s/keys :req-un [::message-id
                   ::message-sender
                   ::message-receiver
                   ::message-type
                   ::message-payload
                   ::message-timestamp]))

(def message-mark
  {:mark-name "message"
   :mark-version "1.0.0"
   :mark-description "Network message"
   :mark-spec ::message
   :mark-validator #(s/valid? ::message %)
   :mark-serializer edn-mark
   :mark-deserializer edn-mark})

;; Sync mark
(s/def ::sync-id
  core/id)

(s/def ::sync-source
  core/id)

(s/def ::sync-target
  core/id)

(s/def ::sync-data
  any?)

(s/def ::sync-timestamp
  core/timestamp)

(s/def ::sync-status
  #{:pending :syncing :completed :failed})

(s/def ::sync
  (s/keys :req-un [::sync-id
                   ::sync-source
                   ::sync-target
                   ::sync-data
                   ::sync-timestamp]
          :opt-un [::sync-status]))

(def sync-mark
  {:mark-name "sync"
   :mark-version "1.0.0"
   :mark-description "Data synchronization"
   :mark-spec ::sync
   :mark-validator #(s/valid? ::sync %)
   :mark-serializer edn-mark
   :mark-deserializer edn-mark})

;; =============================================================================
;; Mark Composition
;; =============================================================================

(defn compose-marks
  "Compose multiple marks into a single mark."
  [marks]
  {:mark-name (str/join "+" (map :mark-name marks))
   :mark-version "1.0.0"
   :mark-description (str "Composed mark: " (str/join ", " (map :mark-description marks)))
   :mark-spec (s/and (map :mark-spec marks))
   :mark-validator #(every? (fn [mark] ((:mark-validator mark) %)) marks)
   :mark-serializer #(mapv (fn [mark] (serialize-mark (:mark-name mark) %)) marks)
   :mark-deserializer #(first (mapv (fn [mark] (deserialize-mark (:mark-name mark) %)) marks))})

;; =============================================================================
;; Mark Validation
;; =============================================================================

(defn validate-all-marks
  "Validate all registered marks."
  []
  (let [results (for [[name mark] mark-registry]
                  {:mark-name name
                   :valid? (s/valid? ::mark mark)
                   :errors (when-not (s/valid? ::mark mark)
                             (s/explain-data ::mark mark))})]
    {:total (count results)
     :valid (count (filter :valid? results))
     :invalid (count (remove :valid? results))
     :results results}))

;; =============================================================================
;; Mark Documentation
;; =============================================================================

(defn mark-doc
  "Generate documentation for a mark."
  [mark-name]
  (if-let [mark (get-mark mark-name)]
    {:name (:mark-name mark)
     :version (:mark-version mark)
     :description (:mark-description mark)
     :spec (:mark-spec mark)
     :has-validator (some? (:mark-validator mark))
     :has-serializer (some? (:mark-serializer mark))
     :has-deserializer (some? (:mark-deserializer mark))}
    nil))

(defn all-marks-doc
  "Generate documentation for all marks."
  []
  (into {} (map (fn [[name _]] [name (mark-doc name)]) mark-registry)))

;; =============================================================================
;; Register additional marks
;; =============================================================================

(register-mark! message-mark)
(register-mark! sync-mark)

