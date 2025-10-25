(ns clotoko.sur.clay
  "Urbit Hoon Arvo Zuse 'Clay' filesystem-inspired immutable URL-safe paths.
   Implements Clay-style path system for ICP/Clotoko compatibility."
  (:require
   [clojure.spec.alpha :as s]
   [clojure.string :as str]
   [clojure.set :as set]
   [clotoko.sur.core :as core]))

;; =============================================================================
;; Clay Path System
;; =============================================================================

;; Clay path segments (URL-safe)
(s/def ::clay-segment
  (s/and string?
          #(re-matches #"^[a-zA-Z0-9._-]+$" %)
          #(<= 1 (count %) 64)
          #(not (str/starts-with? % "."))
          #(not (str/ends-with? % "."))))

;; Clay path (immutable sequence of segments)
(s/def ::clay-path
  (s/coll-of ::clay-segment :min-count 1 :max-count 16))

;; Clay revision (immutable version identifier)
(s/def ::clay-revision
  (s/and string?
          #(re-matches #"^[a-zA-Z0-9._-]+$" %)
          #(<= 1 (count %) 32)))

;; Clay desk (workspace/branch)
(s/def ::clay-desk
  (s/and string?
          #(re-matches #"^[a-zA-Z0-9._-]+$" %)
          #(<= 1 (count %) 16)
          #(not= % "base")))

;; Clay ship (identity/principal)
(s/def ::clay-ship
  (s/and string?
          #(re-matches #"^[a-zA-Z0-9._-]+$" %)
          #(<= 1 (count %) 32)))

;; Clay beam (full path with ship, desk, revision)
(s/def ::clay-beam
  (s/keys :req-un [::clay-ship
                   ::clay-desk
                   ::clay-revision]
          :opt-un [::clay-path]))

;; Clay file type
(s/def ::clay-file-type
  #{:text :json :edn :binary :mark :clotoko :motoko :wasm})

;; Clay file metadata
(s/def ::clay-file-meta
  (s/keys :req-un [::clay-file-type
                   ::core/timestamp]
          :opt-un [::core/text ; description
                   ::clay-revision
                   ::core/id ; content hash
                   ::core/text ; mime-type
                   ::core/text ; encoding
                   ::core/text ; author
                   ::core/text ; license]))

;; Clay file (immutable file with metadata)
(s/def ::clay-file
  (s/keys :req-un [::clay-path
                   ::clay-file-meta
                   ::core/text ; content
                   ::clay-revision]
          :opt-un [::clay-beam]))

;; Clay directory (collection of files)
(s/def ::clay-directory
  (s/keys :req-un [::clay-path
                   ::clay-revision]
          :opt-un [::clay-beam
                   ::core/text ; description
                   ::clay-file-meta]))

;; =============================================================================
;; Clay Path Operations
;; =============================================================================

(defn clay-path->string
  "Convert Clay path to URL-safe string."
  [path]
  (str/join "/" path))

(defn string->clay-path
  "Convert URL-safe string to Clay path."
  [path-str]
  (when (and (string? path-str) (not (str/blank? path-str)))
    (let [segments (str/split path-str #"/")
          clean-segments (filter #(not (str/blank? %)) segments)]
      (when (every? #(s/valid? ::clay-segment %) clean-segments)
        clean-segments))))

(defn clay-path-valid?
  "Validate Clay path."
  [path]
  (s/valid? ::clay-path path))

(defn clay-path-join
  "Join Clay path segments."
  [& segments]
  (let [all-segments (flatten segments)]
    (when (every? #(s/valid? ::clay-segment %) all-segments)
      all-segments)))

(defn clay-path-parent
  "Get parent path."
  [path]
  (when (and (seq path) (> (count path) 1))
    (drop-last path)))

(defn clay-path-basename
  "Get basename (last segment)."
  [path]
  (last path))

(defn clay-path-dirname
  "Get dirname (all but last segment)."
  [path]
  (when (seq path)
    (drop-last path)))

(defn clay-path-extension
  "Get file extension."
  [path]
  (when-let [basename (clay-path-basename path)]
    (when-let [dot-index (str/last-index-of basename ".")]
      (subs basename (inc dot-index)))))

(defn clay-path-stem
  "Get filename without extension."
  [path]
  (when-let [basename (clay-path-basename path)]
    (if-let [dot-index (str/last-index-of basename ".")]
      (subs basename 0 dot-index)
      basename)))

;; =============================================================================
;; Clay Beam Operations
;; =============================================================================

(defn clay-beam->string
  "Convert Clay beam to URL-safe string."
  [beam]
  (let [ship (:clay-ship beam)
        desk (:clay-desk beam)
        revision (:clay-revision beam)
        path (when (:clay-path beam)
               (clay-path->string (:clay-path beam)))]
    (if path
      (str ship "/" desk "/" revision "/" path)
      (str ship "/" desk "/" revision))))

(defn string->clay-beam
  "Convert URL-safe string to Clay beam."
  [beam-str]
  (let [parts (str/split beam-str #"/")]
    (when (>= (count parts) 3)
      {:clay-ship (nth parts 0)
       :clay-desk (nth parts 1)
       :clay-revision (nth parts 2)
       :clay-path (when (> (count parts) 3)
                    (string->clay-path (str/join "/" (drop 3 parts))))})))

(defn clay-beam-valid?
  "Validate Clay beam."
  [beam]
  (s/valid? ::clay-beam beam))

(defn clay-beam-with-path
  "Add path to Clay beam."
  [beam path]
  (assoc beam :clay-path path))

(defn clay-beam-without-path
  "Remove path from Clay beam."
  [beam]
  (dissoc beam :clay-path))

;; =============================================================================
;; Clay File Operations
;; =============================================================================

(defn clay-file->string
  "Convert Clay file to string representation."
  [file]
  (let [path-str (clay-path->string (:clay-path file))
        beam-str (when (:clay-beam file)
                   (clay-beam->string (:clay-beam file)))
        meta-str (str "[" (:clay-file-type (:clay-file-meta file)) "]")]
    (if beam-str
      (str beam-str "/" path-str " " meta-str)
      (str path-str " " meta-str))))

(defn clay-file-valid?
  "Validate Clay file."
  [file]
  (s/valid? ::clay-file file))

(defn clay-file-with-content
  "Create Clay file with content."
  [path content file-type & {:keys [beam revision description author license]}]
  {:clay-path path
   :clay-file-meta {:clay-file-type file-type
                    :core/timestamp (java.time.Instant/now)
                    :clay-revision (or revision "0.0.0")
                    :core/id (str (hash content))
                    :core/text description
                    :core/text author
                    :core/text license}
   :core/text content
   :clay-revision (or revision "0.0.0")
   :clay-beam beam})

(defn clay-file-with-beam
  "Add beam to Clay file."
  [file beam]
  (assoc file :clay-beam beam))

(defn clay-file-without-beam
  "Remove beam from Clay file."
  [file]
  (dissoc file :clay-beam))

;; =============================================================================
;; Clay Directory Operations
;; =============================================================================

(defn clay-directory->string
  "Convert Clay directory to string representation."
  [directory]
  (let [path-str (clay-path->string (:clay-path directory))
        beam-str (when (:clay-beam directory)
                   (clay-beam->string (:clay-beam directory)))]
    (if beam-str
      (str beam-str "/" path-str "/")
      (str path-str "/"))))

(defn clay-directory-valid?
  "Validate Clay directory."
  [directory]
  (s/valid? ::clay-directory directory))

(defn clay-directory-with-files
  "Create Clay directory with files."
  [path files & {:keys [beam revision description]}]
  {:clay-path path
   :clay-revision (or revision "0.0.0")
   :clay-beam beam
   :core/text description
   :clay-file-meta {:clay-file-type :directory
                    :core/timestamp (java.time.Instant/now)
                    :clay-revision (or revision "0.0.0")
                    :core/id (str (hash files))}})

;; =============================================================================
;; Clay Revision System
;; =============================================================================

(defn clay-revision-valid?
  "Validate Clay revision."
  [revision]
  (s/valid? ::clay-revision revision))

(defn clay-revision-increment
  "Increment Clay revision."
  [revision]
  (if (str/includes? revision ".")
    (let [parts (str/split revision #"\.")
          major (Integer/parseInt (first parts))
          minor (Integer/parseInt (second parts))
          patch (Integer/parseInt (nth parts 2))]
      (str major "." minor "." (inc patch)))
    (str revision ".1")))

(defn clay-revision-major
  "Get major version from revision."
  [revision]
  (when-let [parts (str/split revision #"\.")]
    (when (seq parts)
      (Integer/parseInt (first parts)))))

(defn clay-revision-minor
  "Get minor version from revision."
  [revision]
  (when-let [parts (str/split revision #"\.")]
    (when (> (count parts) 1)
      (Integer/parseInt (second parts)))))

(defn clay-revision-patch
  "Get patch version from revision."
  [revision]
  (when-let [parts (str/split revision #"\.")]
    (when (> (count parts) 2)
      (Integer/parseInt (nth parts 2)))))

;; =============================================================================
;; Clay ICP Integration
;; =============================================================================

;; ICP Principal to Clay Ship conversion
(defn icp-principal->clay-ship
  "Convert ICP Principal to Clay ship."
  [principal]
  (str/replace (str principal) #"[^a-zA-Z0-9._-]" ""))

(defn clay-ship->icp-principal
  "Convert Clay ship to ICP Principal."
  [ship]
  (str "principal-" ship))

;; Clay path to ICP canister path
(defn clay-path->icp-path
  "Convert Clay path to ICP canister path."
  [path]
  (str/join "/" (map #(str "canister-" %) path)))

(defn icp-path->clay-path
  "Convert ICP canister path to Clay path."
  [icp-path]
  (let [segments (str/split icp-path #"/")]
    (mapv #(str/replace % #"^canister-" "") segments)))

;; =============================================================================
;; Clay Clotoko Integration
;; =============================================================================

;; Clotoko source file type
(def clotoko-source-type :clotoko)

;; Motoko source file type
(def motoko-source-type :motoko)

;; WASM binary file type
(def wasm-binary-type :wasm)

;; Clay file type for Clotoko
(defn clay-file-type-for-clotoko
  "Get Clay file type for Clotoko source."
  [file-path]
  (let [ext (clay-path-extension file-path)]
    (case ext
      "clj" :clotoko
      "cljs" :clotoko
      "mo" :motoko
      "wasm" :wasm
      :text)))

;; Clotoko source file creation
(defn create-clotoko-source-file
  "Create Clay file for Clotoko source."
  [path content & {:keys [beam revision description author license]}]
  (clay-file-with-content
   path
   content
   :clotoko
   :beam beam
   :revision revision
   :description description
   :author author
   :license license))

;; Motoko source file creation
(defn create-motoko-source-file
  "Create Clay file for Motoko source."
  [path content & {:keys [beam revision description author license]}]
  (clay-file-with-content
   path
   content
   :motoko
   :beam beam
   :revision revision
   :description description
   :author author
   :license license))

;; =============================================================================
;; Clay Network Protocol
;; =============================================================================

;; Clay sync message
(s/def ::clay-sync-message
  (s/keys :req-un [::core/id
                   ::clay-beam
                   ::clay-file
                   ::core/timestamp]
          :opt-un [::core/text ; operation type
                   ::core/id ; parent revision
                   ::core/text ; checksum]))

;; Clay sync operation types
(s/def ::clay-sync-operation
  #{:create :update :delete :move :copy :merge})

;; Clay sync request
(s/def ::clay-sync-request
  (s/keys :req-un [::core/id
                   ::clay-ship ; source ship
                   ::clay-ship ; target ship
                   ::clay-desk
                   ::clay-revision
                   ::clay-sync-operation]
          :opt-un [::clay-path
                   ::clay-file
                   ::core/timestamp]))

;; Clay sync response
(s/def ::clay-sync-response
  (s/keys :req-un [::core/id
                   ::core/id ; request id
                   ::core/text ; status
                   ::core/timestamp]
          :opt-un [::clay-file
                   ::core/text ; error message
                   ::clay-revision]))

;; =============================================================================
;; Clay Validation
;; =============================================================================

(defn validate-clay-system
  "Validate entire Clay system."
  []
  {:path-valid? (s/valid? ::clay-path ["test" "path" "file.clj"])
   :beam-valid? (s/valid? ::clay-beam {:clay-ship "test-ship" :clay-desk "main" :clay-revision "1.0.0"})
   :file-valid? (s/valid? ::clay-file {:clay-path ["test.clj"] :clay-file-meta {:clay-file-type :clotoko :core/timestamp (java.time.Instant/now)} :core/text "test content" :clay-revision "1.0.0"})
   :directory-valid? (s/valid? ::clay-directory {:clay-path ["test"] :clay-revision "1.0.0"})
   :sync-valid? (s/valid? ::clay-sync-message {:core/id "test-id" :clay-beam {:clay-ship "test" :clay-desk "main" :clay-revision "1.0.0"} :clay-file {:clay-path ["test.clj"] :clay-file-meta {:clay-file-type :clotoko :core/timestamp (java.time.Instant/now)} :core/text "test" :clay-revision "1.0.0"} :core/timestamp (java.time.Instant/now)})})

;; =============================================================================
;; Clay Documentation
;; =============================================================================

(defn clay-system-doc
  "Generate documentation for Clay system."
  []
  {:description "Urbit Hoon Arvo Zuse Clay filesystem-inspired immutable URL-safe paths"
   :features ["Immutable paths" "URL-safe segments" "Revision system" "ICP integration" "Clotoko compatibility"]
   :path-operations ["clay-path->string" "string->clay-path" "clay-path-join" "clay-path-parent" "clay-path-basename"]
   :beam-operations ["clay-beam->string" "string->clay-beam" "clay-beam-with-path" "clay-beam-without-path"]
   :file-operations ["clay-file->string" "clay-file-with-content" "clay-file-with-beam" "clay-file-without-beam"]
   :revision-operations ["clay-revision-increment" "clay-revision-major" "clay-revision-minor" "clay-revision-patch"]
   :icp-integration ["icp-principal->clay-ship" "clay-ship->icp-principal" "clay-path->icp-path" "icp-path->clay-path"]
   :clotoko-integration ["clay-file-type-for-clotoko" "create-clotoko-source-file" "create-motoko-source-file"]})
