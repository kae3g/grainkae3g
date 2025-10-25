(ns clotoko.sur.social
  "Social network data structure definitions.
   Defines types for users, posts, relationships, and social interactions."
  (:require
   [clojure.spec.alpha :as s]
   [clojure.spec.gen.alpha :as gen]
   [clotoko.sur.core :as core]))

;; =============================================================================
;; User Types
;; =============================================================================

;; User identity
(s/def ::user-id
  core/id)

(s/def ::username
  (s/and core/text-short
          #(re-matches #"^[a-zA-Z0-9_-]{3,32}$" %)))

(s/def ::email
  (s/and core/text
          #(re-matches #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$" %)))

(s/def ::display-name
  (s/and core/text
          #(<= 1 (count %) 64)))

;; User profile
(s/def ::bio
  (s/and core/text-long
          #(<= (count %) 500)))

(s/def ::avatar-url
  core/url)

(s/def ::website
  core/url)

(s/def ::location
  core/text)

;; User status
(s/def ::verified?
  core/bool)

(s/def ::grain-galaxy?
  core/bool)

(s/def ::admin?
  core/bool)

(s/def ::active?
  core/bool)

;; User timestamps
(s/def ::joined-at
  core/timestamp)

(s/def ::last-active
  core/timestamp)

;; Complete user record
(s/def ::user
  (s/keys :req-un [::user-id
                   ::username
                   ::email
                   ::display-name
                   ::joined-at]
          :opt-un [::bio
                   ::avatar-url
                   ::website
                   ::location
                   ::verified?
                   ::grain-galaxy?
                   ::admin?
                   ::active?
                   ::last-active]))

;; =============================================================================
;; Post Types
;; =============================================================================

;; Post identity
(s/def ::post-id
  core/id)

(s/def ::post-author
  ::user-id)

;; Post content
(s/def ::post-title
  (s/and core/text
          #(<= (count %) 200)))

(s/def ::post-content
  core/text-long)

(s/def ::post-type
  #{:text :image :video :audio :link :poll :event :status})

;; Post metadata
(s/def ::post-timestamp
  core/timestamp)

(s/def ::post-visibility
  core/visibility)

(s/def ::post-language
  (s/and core/text-short
          #(re-matches #"^[a-z]{2}(-[A-Z]{2})?$" %)))

;; Post engagement
(s/def ::like-count
  core/nat)

(s/def ::repost-count
  core/nat)

(s/def ::reply-count
  core/nat)

(s/def ::view-count
  core/nat)

;; Post status and versioning
(s/def ::post-status
  (s/and core/text
          #(<= (count %) 280)))

(s/def ::post-branch
  core/text-short)

(s/def ::post-version
  (s/and core/text
          #(re-matches #"^\d+\.\d+\.\d+(-[a-zA-Z0-9.-]+)?$" %)))

(s/def ::post-mood
  #{:happy :sad :excited :focused :tired :energetic :calm :stressed :creative :productive})

(s/def ::post-activity
  #{:coding :writing :reading :designing :meeting :thinking :building :learning :teaching :collaborating})

(s/def ::post-location
  core/text)

(s/def ::post-context
  core/text)

;; Complete post record
(s/def ::post
  (s/keys :req-un [::post-id
                   ::post-author
                   ::post-content
                   ::post-timestamp]
          :opt-un [::post-title
                   ::post-type
                   ::post-visibility
                   ::post-language
                   ::like-count
                   ::repost-count
                   ::reply-count
                   ::view-count
                   ::post-status
                   ::post-branch
                   ::post-version
                   ::post-mood
                   ::post-activity
                   ::post-location
                   ::post-context]))

;; =============================================================================
;; Relationship Types
;; =============================================================================

;; Follow relationship
(s/def ::follow-id
  core/id)

(s/def ::follower
  ::user-id)

(s/def ::following
  ::user-id)

(s/def ::follow-timestamp
  core/timestamp)

(s/def ::follow-status
  #{:active :muted :blocked})

(s/def ::follow
  (s/keys :req-un [::follow-id
                   ::follower
                   ::following
                   ::follow-timestamp]
          :opt-un [::follow-status]))

;; Block relationship
(s/def ::block-id
  core/id)

(s/def ::blocker
  ::user-id)

(s/def ::blocked
  ::user-id)

(s/def ::block-timestamp
  core/timestamp)

(s/def ::block-reason
  core/text)

(s/def ::block
  (s/keys :req-un [::block-id
                   ::blocker
                   ::blocked
                   ::block-timestamp]
          :opt-un [::block-reason]))

;; =============================================================================
;; Interaction Types
;; =============================================================================

;; Like interaction
(s/def ::like-id
  core/id)

(s/def ::like-user
  ::user-id)

(s/def ::like-post
  ::post-id)

(s/def ::like-timestamp
  core/timestamp)

(s/def ::like
  (s/keys :req-un [::like-id
                   ::like-user
                   ::like-post
                   ::like-timestamp]))

;; Repost interaction
(s/def ::repost-id
  core/id)

(s/def ::repost-user
  ::user-id)

(s/def ::repost-post
  ::post-id)

(s/def ::repost-timestamp
  core/timestamp)

(s/def ::repost-comment
  core/text)

(s/def ::repost
  (s/keys :req-un [::repost-id
                   ::repost-user
                   ::repost-post
                   ::repost-timestamp]
          :opt-un [::repost-comment]))

;; Reply interaction
(s/def ::reply-id
  core/id)

(s/def ::reply-user
  ::user-id)

(s/def ::reply-post
  ::post-id)

(s/def ::reply-content
  core/text-long)

(s/def ::reply-timestamp
  core/timestamp)

(s/def ::reply
  (s/keys :req-un [::reply-id
                   ::reply-user
                   ::reply-post
                   ::reply-content
                   ::reply-timestamp]))

;; =============================================================================
;; Tag Types
;; =============================================================================

;; Tag
(s/def ::tag-name
  (s/and core/text-short
          #(re-matches #"^[a-zA-Z0-9_-]+$" %)
          #(<= 1 (count %) 32)))

(s/def ::tag-description
  core/text)

(s/def ::tag-count
  core/nat)

(s/def ::tag
  (s/keys :req-un [::tag-name]
          :opt-un [::tag-description
                   ::tag-count]))

;; Post tags
(s/def ::post-tags
  (s/coll-of ::tag-name :kind set?))

;; =============================================================================
;; Status Update Types
;; =============================================================================

;; Status update
(s/def ::status-id
  core/id)

(s/def ::status-user
  ::user-id)

(s/def ::status-content
  (s/and core/text
          #(<= (count %) 280)))

(s/def ::status-timestamp
  core/timestamp)

(s/def ::status-type
  #{:general :technical :personal :work :hobby :travel :food :music :art :sports})

(s/def ::status-mood
  #{:happy :sad :excited :focused :tired :energetic :calm :stressed :creative :productive :inspired :frustrated :grateful :anxious :confident :curious :motivated :overwhelmed :peaceful :determined})

(s/def ::status-activity
  #{:coding :writing :reading :designing :meeting :thinking :building :learning :teaching :collaborating :debugging :planning :researching :presenting :networking :mentoring :prototyping :testing :deploying :maintaining})

(s/def ::status-location
  core/text)

(s/def ::status-context
  core/text)

(s/def ::status-branch
  core/text-short)

(s/def ::status-version
  (s/and core/text
          #(re-matches #"^\d+\.\d+\.\d+(-[a-zA-Z0-9.-]+)?$" %)))

(s/def ::status-visibility
  core/visibility)

(s/def ::status-expires-at
  core/timestamp)

(s/def ::status
  (s/keys :req-un [::status-id
                   ::status-user
                   ::status-content
                   ::status-timestamp]
          :opt-un [::status-type
                   ::status-mood
                   ::status-activity
                   ::status-location
                   ::status-context
                   ::status-branch
                   ::status-version
                   ::status-visibility
                   ::status-expires-at]))

;; =============================================================================
;; Branch Types
;; =============================================================================

;; Development branch
(s/def ::branch-name
  (s/and core/text-short
          #(re-matches #"^[a-zA-Z0-9._-]+$" %)
          #(<= 1 (count %) 64)))

(s/def ::branch-user
  ::user-id)

(s/def ::branch-description
  core/text)

(s/def ::branch-created-at
  core/timestamp)

(s/def ::branch-active?
  core/bool)

(s/def ::branch-posts
  (s/coll-of ::post-id :kind set?))

(s/def ::branch-statuses
  (s/coll-of ::status-id :kind set?))

(s/def ::branch
  (s/keys :req-un [::branch-name
                   ::branch-user
                   ::branch-created-at]
          :opt-un [::branch-description
                   ::branch-active?
                   ::branch-posts
                   ::branch-statuses]))

;; =============================================================================
;; Feed Types
;; =============================================================================

;; Feed entry
(s/def ::feed-entry
  (s/or :post ::post
        :status ::status))

(s/def ::feed
  (s/coll-of ::feed-entry :kind vector?))

;; Timeline
(s/def ::timeline-user
  ::user-id)

(s/def ::timeline-entries
  (s/coll-of ::feed-entry :kind vector?))

(s/def ::timeline
  (s/keys :req-un [::timeline-user
                   ::timeline-entries]))

;; =============================================================================
;; Search Types
;; =============================================================================

;; Search query
(s/def ::search-query
  core/text)

(s/def ::search-type
  #{:posts :users :tags :all})

(s/def ::search-filters
  (s/keys :opt-un [::post-type
                   ::post-visibility
                   ::post-language
                   ::status-type
                   ::status-mood
                   ::status-activity]))

(s/def ::search-result
  (s/keys :req-un [::query ::type ::results]
          :opt-un [::filters ::total-count ::page ::page-size]))

;; =============================================================================
;; Generator Functions
;; =============================================================================

(defn gen-user
  "Generate random user."
  []
  (gen/fmap
   #(merge {:user-id (str "user-" (rand-int 1000000))
            :username (str "user" (rand-int 1000))
            :email (str "user" (rand-int 1000) "@example.com")
            :display-name (str "User " (rand-int 1000))
            :joined-at (System/currentTimeMillis)
            :verified? false
            :grain-galaxy? false
            :admin? false
            :active? true
            :last-active (System/currentTimeMillis)}
           %)
   (gen/hash-map
    :bio (gen/fmap #(apply str %) (gen/vector (gen/char-alpha) 0 100))
    :avatar-url (gen/elements ["https://example.com/avatar1.jpg"
                               "https://example.com/avatar2.jpg"])
    :website (gen/elements ["https://example.com"
                            "https://github.com/user"])
    :location (gen/elements ["Digital Frontier"
                             "Code Cave"
                             "Hardware Lab"]))))

(defn gen-post
  "Generate random post."
  []
  (gen/fmap
   #(merge {:post-id (str "post-" (rand-int 1000000))
            :post-author (str "user-" (rand-int 1000))
            :post-content (str "This is a test post " (rand-int 1000))
            :post-timestamp (System/currentTimeMillis)
            :post-type :text
            :post-visibility :public
            :like-count 0
            :repost-count 0
            :reply-count 0
            :view-count 0}
           %)
   (gen/hash-map
    :post-title (gen/fmap #(apply str %) (gen/vector (gen/char-alpha) 0 50))
    :post-status (gen/fmap #(apply str %) (gen/vector (gen/char-alpha) 0 100))
    :post-branch (gen/elements ["main" "dev" "experiment"])
    :post-version (gen/elements ["1.0.0" "2.0.0-beta" "0.1.0-alpha"])
    :post-mood (gen/elements [:happy :focused :excited :productive])
    :post-activity (gen/elements [:coding :writing :designing :building])
    :post-location (gen/elements ["Digital Frontier" "Code Cave" "Hardware Lab"])
    :post-context (gen/fmap #(apply str %) (gen/vector (gen/char-alpha) 0 100)))))

;; =============================================================================
;; Validation Functions
;; =============================================================================

(defn validate-user
  "Validate user record."
  [user]
  (s/valid? ::user user))

(defn validate-post
  "Validate post record."
  [post]
  (s/valid? ::post post))

(defn validate-status
  "Validate status record."
  [status]
  (s/valid? ::status status))

(defn validate-branch
  "Validate branch record."
  [branch]
  (s/valid? ::branch branch))

;; =============================================================================
;; Type Conversion Functions
;; =============================================================================

(defn user->map
  "Convert user to map with string keys."
  [user]
  (into {} (map (fn [[k v]] [(name k) v]) user)))

(defn map->user
  "Convert map with string keys to user."
  [m]
  (into {} (map (fn [[k v]] [(keyword k) v]) m)))

(defn post->map
  "Convert post to map with string keys."
  [post]
  (into {} (map (fn [[k v]] [(name k) v]) post)))

(defn map->post
  "Convert map with string keys to post."
  [m]
  (into {} (map (fn [[k v]] [(keyword k) v]) m)))

