(ns clotoko.sur.grainframe
  "Grainframe - Mark data types for Grainwriter 80x110 abstract screen messages.
   Designed for instant loading, swiping, tapping, scrolling, and photo-gallery mode."
  (:require
   [clojure.spec.alpha :as s]
   [clojure.string :as str]
   [clotoko.sur.core :as core]
   [clotoko.sur.mark :as mark]
   [clotoko.sur.clay :as clay]))

;; =============================================================================
;; Grainframe Core Specifications
;; =============================================================================

;; Grainframe dimensions (80 chars wide, 110 lines tall - 8x11 paper ratio)
(def grainframe-width 80)
(def grainframe-height 110)
(def grainframe-ratio 1.375) ; 11/8 ratio
(def grainframe-total-chars (* grainframe-width grainframe-height)) ; 8800 characters

;; Grainframe ID
(s/def ::grainframe-id
  core/::id)

;; Grainframe content (exactly 8800 characters or less)
(s/def ::grainframe-content
  (s/and string?
         #(<= (count %) grainframe-total-chars)))

;; Grainframe line (exactly 80 characters or less)
(s/def ::grainframe-line
  (s/and string?
         #(<= (count %) grainframe-width)))

;; Grainframe lines (exactly 110 lines)
(s/def ::grainframe-lines
  (s/coll-of ::grainframe-line
             :min-count 1
             :max-count grainframe-height))

;; Grainframe type
(s/def ::grainframe-type
  #{:text :poem :document :image-ascii :code :message :post :status
    :photo-description :gallery-item :slide :page :note :letter})

;; Grainframe metadata
(s/def ::grainframe-author
  core/::text)

(s/def ::grainframe-timestamp
  core/::timestamp)

(s/def ::grainframe-title
  (s/and core/::text #(<= (count %) grainframe-width)))

(s/def ::grainframe-tags
  (s/coll-of core/::text))

(s/def ::grainframe-version
  core/::version)

(s/def ::grainframe-metadata
  (s/keys :req-un [::grainframe-type
                   ::grainframe-timestamp]
          :opt-un [::grainframe-author
                   ::grainframe-title
                   ::grainframe-tags
                   ::grainframe-version
                   ::clay/clay-beam]))

;; Grainframe display mode
(s/def ::grainframe-display-mode
  #{:full-screen :gallery :swipe :scroll :tap :slideshow :grid})

;; Grainframe navigation
(s/def ::grainframe-index
  nat-int?)

(s/def ::grainframe-total
  pos-int?)

(s/def ::grainframe-navigation
  (s/keys :req-un [::grainframe-index
                   ::grainframe-total]
          :opt-un [::grainframe-display-mode]))

;; Grainframe (complete frame structure)
(s/def ::grainframe
  (s/keys :req-un [::grainframe-id
                   ::grainframe-content
                   ::grainframe-metadata]
          :opt-un [::grainframe-lines
                   ::grainframe-navigation
                   ::clay/clay-path]))

;; =============================================================================
;; Grainframe Gallery Specifications
;; =============================================================================

;; Grainframe gallery ID
(s/def ::gallery-id
  core/::id)

;; Grainframe gallery name
(s/def ::gallery-name
  core/::text)

;; Grainframe gallery description
(s/def ::gallery-description
  core/::text)

;; Grainframe gallery frames
(s/def ::gallery-frames
  (s/coll-of ::grainframe-id :min-count 1))

;; Grainframe gallery
(s/def ::grainframe-gallery
  (s/keys :req-un [::gallery-id
                   ::gallery-name
                   ::gallery-frames]
          :opt-un [::gallery-description
                   ::grainframe-timestamp
                   ::grainframe-author
                   ::grainframe-tags
                   ::clay/clay-beam]))

;; =============================================================================
;; Grainframe Operations
;; =============================================================================

(defn create-grainframe
  "Create a new Grainframe from content."
  [content & {:keys [type title author tags version clay-beam]
              :or {type :text}}]
  (let [trimmed-content (if (> (count content) grainframe-total-chars)
                          (subs content 0 grainframe-total-chars)
                          content)
        lines (str/split-lines trimmed-content)
        padded-lines (take grainframe-height (concat lines (repeat "")))]
    {:grainframe-id (str (java.util.UUID/randomUUID))
     :grainframe-content trimmed-content
     :grainframe-lines (mapv #(if (> (count %) grainframe-width)
                                (subs % 0 grainframe-width)
                                %)
                            padded-lines)
     :grainframe-metadata {:grainframe-type type
                          :grainframe-timestamp (java.time.Instant/now)
                          :grainframe-author author
                          :grainframe-title title
                          :grainframe-tags (or tags [])
                          :grainframe-version (or version "1.0.0")
                          :clay/clay-beam clay-beam}}))

(defn grainframe->string
  "Convert Grainframe to string representation."
  [grainframe]
  (:grainframe-content grainframe))

(defn grainframe->lines
  "Get Grainframe lines."
  [grainframe]
  (or (:grainframe-lines grainframe)
      (str/split-lines (:grainframe-content grainframe))))

(defn grainframe-line-count
  "Get number of lines in Grainframe."
  [grainframe]
  (count (grainframe->lines grainframe)))

(defn grainframe-char-count
  "Get number of characters in Grainframe."
  [grainframe]
  (count (:grainframe-content grainframe)))

(defn grainframe-valid?
  "Validate Grainframe."
  [grainframe]
  (s/valid? ::grainframe grainframe))

(defn grainframe-format
  "Format content to fit Grainframe dimensions."
  [content]
  (let [lines (str/split-lines content)
        formatted-lines (mapv (fn [line]
                               (if (> (count line) grainframe-width)
                                 (subs line 0 grainframe-width)
                                 line))
                             lines)
        limited-lines (take grainframe-height formatted-lines)]
    (str/join "\n" limited-lines)))

(defn grainframe-word-wrap
  "Word wrap content to fit Grainframe width."
  [content]
  (let [words (str/split content #"\s+")
        lines (atom [])
        current-line (atom "")]
    (doseq [word words]
      (let [new-line (if (empty? @current-line)
                      word
                      (str @current-line " " word))]
        (if (<= (count new-line) grainframe-width)
          (reset! current-line new-line)
          (do
            (swap! lines conj @current-line)
            (reset! current-line word)))))
    (when-not (empty? @current-line)
      (swap! lines conj @current-line))
    (str/join "\n" (take grainframe-height @lines))))

(defn grainframe-center-text
  "Center text within Grainframe width."
  [text]
  (let [lines (str/split-lines text)
        centered-lines (mapv (fn [line]
                              (let [padding (max 0 (quot (- grainframe-width (count line)) 2))
                                    padded-line (str (apply str (repeat padding " ")) line)]
                                (if (> (count padded-line) grainframe-width)
                                  (subs padded-line 0 grainframe-width)
                                  padded-line)))
                            lines)]
    (str/join "\n" centered-lines)))

;; =============================================================================
;; Grainframe Gallery Operations
;; =============================================================================

(defn create-gallery
  "Create a new Grainframe gallery."
  [name frames & {:keys [description author tags clay-beam]}]
  {:gallery-id (str (java.util.UUID/randomUUID))
   :gallery-name name
   :gallery-frames (mapv :grainframe-id frames)
   :gallery-description description
   :grainframe-timestamp (java.time.Instant/now)
   :grainframe-author author
   :grainframe-tags (or tags [])
   :clay/clay-beam clay-beam})

(defn gallery-add-frame!
  "Add a Grainframe to a gallery."
  [gallery frame]
  (update gallery :gallery-frames conj (:grainframe-id frame)))

(defn gallery-remove-frame!
  "Remove a Grainframe from a gallery."
  [gallery frame-id]
  (update gallery :gallery-frames
          (fn [frames] (filterv #(not= % frame-id) frames))))

(defn gallery-frame-count
  "Get number of frames in gallery."
  [gallery]
  (count (:gallery-frames gallery)))

(defn gallery-valid?
  "Validate Grainframe gallery."
  [gallery]
  (s/valid? ::grainframe-gallery gallery))

;; =============================================================================
;; Grainframe Display Modes
;; =============================================================================

(defn grainframe-display-full-screen
  "Display Grainframe in full-screen mode."
  [grainframe]
  {:grainframe grainframe
   :display-mode :full-screen
   :lines (grainframe->lines grainframe)
   :width grainframe-width
   :height grainframe-height})

(defn grainframe-display-gallery
  "Display Grainframes in gallery mode."
  [frames & {:keys [columns rows]
             :or {columns 3 rows 3}}]
  {:frames frames
   :display-mode :gallery
   :columns columns
   :rows rows
   :items-per-page (* columns rows)
   :total-frames (count frames)})

(defn grainframe-display-swipe
  "Display Grainframes in swipe mode (one at a time, swipeable)."
  [frames current-index]
  {:frames frames
   :display-mode :swipe
   :current-index current-index
   :current-frame (nth frames current-index nil)
   :total-frames (count frames)
   :has-prev? (> current-index 0)
   :has-next? (< current-index (dec (count frames)))})

(defn grainframe-display-scroll
  "Display Grainframes in scroll mode (continuous vertical scroll)."
  [frames]
  {:frames frames
   :display-mode :scroll
   :total-frames (count frames)
   :total-lines (* (count frames) grainframe-height)})

(defn grainframe-display-slideshow
  "Display Grainframes in slideshow mode (auto-advance)."
  [frames current-index & {:keys [duration-ms]
                          :or {duration-ms 5000}}]
  {:frames frames
   :display-mode :slideshow
   :current-index current-index
   :current-frame (nth frames current-index nil)
   :total-frames (count frames)
   :duration-ms duration-ms
   :auto-advance? true})

;; =============================================================================
;; Grainframe Mark Definitions
;; =============================================================================

;; Text Grainframe mark
(def text-grainframe-mark
  {:mark-name "text-grainframe"
   :mark-version "1.0.0"
   :mark-description "Text Grainframe (80x110)"
   :mark-spec ::grainframe
   :mark-validator #(s/valid? ::grainframe %)
   :mark-serializer #(pr-str %)
   :mark-deserializer #(read-string %)})

;; Poem Grainframe mark
(def poem-grainframe-mark
  {:mark-name "poem-grainframe"
   :mark-version "1.0.0"
   :mark-description "Poem Grainframe (80x110)"
   :mark-spec ::grainframe
   :mark-validator #(and (s/valid? ::grainframe %)
                        (= :poem (get-in % [:grainframe-metadata :grainframe-type])))
   :mark-serializer #(pr-str %)
   :mark-deserializer #(read-string %)})

;; Document Grainframe mark
(def document-grainframe-mark
  {:mark-name "document-grainframe"
   :mark-version "1.0.0"
   :mark-description "Document Grainframe (80x110)"
   :mark-spec ::grainframe
   :mark-validator #(and (s/valid? ::grainframe %)
                        (= :document (get-in % [:grainframe-metadata :grainframe-type])))
   :mark-serializer #(pr-str %)
   :mark-deserializer #(read-string %)})

;; Gallery Grainframe mark
(def gallery-grainframe-mark
  {:mark-name "gallery-grainframe"
   :mark-version "1.0.0"
   :mark-description "Grainframe Gallery"
   :mark-spec ::grainframe-gallery
   :mark-validator #(s/valid? ::grainframe-gallery %)
   :mark-serializer #(pr-str %)
   :mark-deserializer #(read-string %)})

;; Photo description Grainframe mark
(def photo-grainframe-mark
  {:mark-name "photo-grainframe"
   :mark-version "1.0.0"
   :mark-description "Photo Description Grainframe (80x110)"
   :mark-spec ::grainframe
   :mark-validator #(and (s/valid? ::grainframe %)
                        (= :photo-description (get-in % [:grainframe-metadata :grainframe-type])))
   :mark-serializer #(pr-str %)
   :mark-deserializer #(read-string %)})

;; Message Grainframe mark
(def message-grainframe-mark
  {:mark-name "message-grainframe"
   :mark-version "1.0.0"
   :mark-description "Message Grainframe (80x110)"
   :mark-spec ::grainframe
   :mark-validator #(and (s/valid? ::grainframe %)
                        (= :message (get-in % [:grainframe-metadata :grainframe-type])))
   :mark-serializer #(pr-str %)
   :mark-deserializer #(read-string %)})

;; Post Grainframe mark
(def post-grainframe-mark
  {:mark-name "post-grainframe"
   :mark-version "1.0.0"
   :mark-description "Social Post Grainframe (80x110)"
   :mark-spec ::grainframe
   :mark-validator #(and (s/valid? ::grainframe %)
                        (= :post (get-in % [:grainframe-metadata :grainframe-type])))
   :mark-serializer #(pr-str %)
   :mark-deserializer #(read-string %)})

;; =============================================================================
;; Grainframe Mark Registry
;; =============================================================================

(defonce grainframe-mark-registry
  (atom {"text-grainframe" text-grainframe-mark
         "poem-grainframe" poem-grainframe-mark
         "document-grainframe" document-grainframe-mark
         "gallery-grainframe" gallery-grainframe-mark
         "photo-grainframe" photo-grainframe-mark
         "message-grainframe" message-grainframe-mark
         "post-grainframe" post-grainframe-mark}))

(defn register-grainframe-mark!
  "Register a new Grainframe mark."
  [mark]
  (swap! grainframe-mark-registry assoc (:mark-name mark) mark)
  (println "Registered Grainframe mark:" (:mark-name mark)))

(defn get-grainframe-mark
  "Get Grainframe mark by name."
  [mark-name]
  (get @grainframe-mark-registry mark-name))

(defn list-grainframe-marks
  "List all registered Grainframe marks."
  []
  (vals @grainframe-mark-registry))

;; =============================================================================
;; Grainframe Conversion to Mark System
;; =============================================================================

(defn grainframe->mark
  "Convert Grainframe to Mark data."
  [grainframe]
  (let [frame-type (get-in grainframe [:grainframe-metadata :grainframe-type])
        mark-name (str (name frame-type) "-grainframe")]
    (when-let [mark (get-grainframe-mark mark-name)]
      {:mark mark
       :data grainframe
       :valid? ((:mark-validator mark) grainframe)})))

(defn mark->grainframe
  "Convert Mark data to Grainframe."
  [mark-data data]
  (when (s/valid? ::grainframe data)
    data))

;; =============================================================================
;; Grainframe Network Protocol
;; =============================================================================

(defn grainframe-sync-message
  "Create a Grainframe sync message for Clay network."
  [grainframe clay-beam]
  {:grainframe-id (:grainframe-id grainframe)
   :grainframe grainframe
   :clay-beam clay-beam
   :timestamp (java.time.Instant/now)
   :operation :sync
   :mark-type (str (name (get-in grainframe [:grainframe-metadata :grainframe-type])) "-grainframe")})

(defn grainframe-request-message
  "Create a Grainframe request message for Clay network."
  [grainframe-id clay-beam]
  {:grainframe-id grainframe-id
   :clay-beam clay-beam
   :timestamp (java.time.Instant/now)
   :operation :request})

(defn grainframe-response-message
  "Create a Grainframe response message for Clay network."
  [grainframe request-id]
  {:grainframe-id (:grainframe-id grainframe)
   :grainframe grainframe
   :request-id request-id
   :timestamp (java.time.Instant/now)
   :operation :response})

;; =============================================================================
;; Grainframe Utilities
;; =============================================================================

(defn grainframe-from-file
  "Create Grainframe from file content."
  [file-path & {:keys [type title author tags version]}]
  (let [content (slurp file-path)]
    (create-grainframe content
                      :type (or type :document)
                      :title (or title (.getName (clojure.java.io/file file-path)))
                      :author author
                      :tags tags
                      :version version)))

(defn grainframe-to-file
  "Write Grainframe to file."
  [grainframe file-path]
  (spit file-path (:grainframe-content grainframe)))

(defn grainframe-pagination
  "Calculate pagination for Grainframe content."
  [content]
  (let [total-chars (count content)
        total-frames (int (Math/ceil (/ total-chars grainframe-total-chars)))
        frames (for [i (range total-frames)]
                 (let [start (* i grainframe-total-chars)
                       end (min (* (inc i) grainframe-total-chars) total-chars)]
                   (subs content start end)))]
    {:total-frames total-frames
     :frames frames
     :chars-per-frame grainframe-total-chars}))

;; =============================================================================
;; Grainframe Documentation
;; =============================================================================

(defn grainframe-system-doc
  "Generate documentation for Grainframe system."
  []
  {:description "Grainframe - Mark data types for Grainwriter 80x110 abstract screen messages"
   :dimensions {:width grainframe-width
               :height grainframe-height
               :ratio grainframe-ratio
               :total-chars grainframe-total-chars}
   :types [:text :poem :document :image-ascii :code :message :post :status
           :photo-description :gallery-item :slide :page :note :letter]
   :display-modes [:full-screen :gallery :swipe :scroll :tap :slideshow :grid]
   :features ["Instant loading"
              "Swipe/tap/scroll navigation"
              "Photo-gallery mode"
              "80x110 dimensions (8x11 paper ratio)"
              "Clay filesystem integration"
              "Mark type system"
              "Network protocol support"]
   :operations {:creation ["create-grainframe" "create-gallery"]
               :formatting ["grainframe-format" "grainframe-word-wrap" "grainframe-center-text"]
               :display ["grainframe-display-full-screen" "grainframe-display-gallery" "grainframe-display-swipe" "grainframe-display-scroll" "grainframe-display-slideshow"]
               :validation ["grainframe-valid?" "gallery-valid?"]
               :io ["grainframe-from-file" "grainframe-to-file"]
               :networking ["grainframe-sync-message" "grainframe-request-message" "grainframe-response-message"]}
   :marks (mapv :mark-name (list-grainframe-marks))})
