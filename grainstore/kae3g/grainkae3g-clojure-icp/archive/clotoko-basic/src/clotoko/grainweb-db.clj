(ns clotoko.grainweb-db
  "Grainweb Datascript database for Clotoko app.
   Contains posts, users, and social network data."
  (:require
   [datascript.core :as d]
   [clojure.string :as str]
   [clojure.edn :as edn]))

;; =============================================================================
;; Database Schema
;; =============================================================================

(def schema
  {:user/id {:db/unique :db.unique/identity}
   :user/username {:db/unique :db.unique/identity}
   :user/email {:db/unique :db.unique/identity}
   :user/display-name {:db/unique :db.unique/identity}
   :user/bio {}
   :user/verified {:db/cardinality :db.cardinality/one}
   :user/grain-galaxy {:db/cardinality :db.cardinality/one}
   :user/admin {:db/cardinality :db.cardinality/one}
   :user/joined {}
   :user/last-active {}
   :user/current-status {}
   :user/status-history {:db/cardinality :db.cardinality/many}
   :user/active-branches {:db/cardinality :db.cardinality/many}
   
   :post/id {:db/unique :db.unique/identity}
   :post/author {}
   :post/title {}
   :post/content {}
   :post/type {}
   :post/timestamp {}
   :post/tags {}
   :post/likes {:db/cardinality :db.cardinality/many}
   :post/reposts {:db/cardinality :db.cardinality/many}
   :post/replies {:db/cardinality :db.cardinality/many}
   :post/visibility {}
   :post/grain-post {:db/cardinality :db.cardinality/one}
   :post/status {}
   :post/status-timestamp {}
   :post/branch {}
   :post/version {}
   :post/status-mood {}
   :post/status-activity {}
   :post/status-location {}
   :post/status-context {}
   
   :status/id {:db/unique :db.unique/identity}
   :status/user {}
   :status/content {}
   :status/timestamp {}
   :status/type {}
   :status/mood {}
   :status/activity {}
   :status/location {}
   :status/context {}
   :status/branch {}
   :status/version {}
   :status/visibility {}
   :status/expires-at {}
   
   :branch/name {:db/unique :db.unique/identity}
   :branch/user {}
   :branch/description {}
   :branch/created-at {}
   :branch/active {:db/cardinality :db.cardinality/one}
   :branch/posts {:db/cardinality :db.cardinality/many}
   :branch/statuses {:db/cardinality :db.cardinality/many}
   
   :like/user {}
   :like/post {}
   :like/timestamp {}
   
   :repost/user {}
   :repost/post {}
   :repost/timestamp {}
   
   :reply/user {}
   :reply/post {}
   :reply/content {}
   :reply/timestamp {}
   
   :tag/name {:db/unique :db.unique/identity}
   :tag/posts {:db/cardinality :db.cardinality/many}
   
   :follow/follower {}
   :follow/following {}
   :follow/timestamp {}})

;; =============================================================================
;; Database Initialization
;; =============================================================================

(def conn (d/create-conn schema))

;; =============================================================================
;; User Data
;; =============================================================================

(def kae3g-user
  {:user/id "kae3g"
   :user/username "kae3g"
   :user/email "kj3x39@gmail.com"
   :user/display-name "kae3g"
   :user/bio "Building the Grain Network - personal sovereignty through open-source hardware and software"
   :user/verified true
   :user/grain-galaxy true
   :user/admin true
   :user/joined #inst "2025-01-01T00:00:00.000-00:00"
   :user/last-active #inst "2025-01-22T10:55:00.000-00:00"
   :user/current-status "status-1"
   :user/status-history ["status-1" "status-2" "status-3"]
   :user/active-branches ["main" "grainwriter-dev" "clotoko-experiment"]})

;; =============================================================================
;; User Status Updates
;; =============================================================================

(def kae3g-statuses
  [{:status/id "status-1"
    :status/user "kae3g"
    :status/content "🌾 Building the future of personal computing with Grain Network - where open-source meets sovereignty"
    :status/timestamp #inst "2025-01-22T10:30:00.000-00:00"
    :status/type :general
    :status/mood :productive
    :status/activity :coding
    :status/location "Digital Frontier"
    :status/context "Working on Clotoko transpiler and Grainwriter design"
    :status/branch "main"
    :status/version "1.0.0"
    :status/visibility :public
    :status/expires-at #inst "2025-01-23T10:30:00.000-00:00"}
   
   {:status/id "status-2"
    :status/user "kae3g"
    :status/content "💻 Deep in the code mines - Clojure to Motoko transpilation is more beautiful than I imagined"
    :status/timestamp #inst "2025-01-22T10:45:00.000-00:00"
    :status/type :technical
    :status/mood :focused
    :status/activity :coding
    :status/location "Code Cave"
    :status/context "Implementing Clotoko core transpiler functions"
    :status/branch "clotoko-experiment"
    :status/version "0.1.0-alpha"
    :status/visibility :public
    :status/expires-at #inst "2025-01-23T10:45:00.000-00:00"}
   
   {:status/id "status-3"
    :status/user "kae3g"
    :status/content "📝 Grainwriter taking shape - 80x110 paper ratio, 64GB RAM, infinite battery life. The future of writing is here"
    :status/timestamp #inst "2025-01-22T11:00:00.000-00:00"
    :status/type :hardware
    :status/mood :excited
    :status/activity :designing
    :status/location "Hardware Lab"
    :status/context "Finalizing Grainwriter specifications and Clojure Humble UI integration"
    :status/branch "grainwriter-dev"
    :status/version "2.0.0-beta"
    :status/visibility :public
    :status/expires-at #inst "2025-01-23T11:00:00.000-00:00"}])

;; =============================================================================
;; User Branches
;; =============================================================================

(def kae3g-branches
  [{:branch/name "main"
    :branch/user "kae3g"
    :branch/description "Main development branch for Grain Network core features"
    :branch/created-at #inst "2025-01-01T00:00:00.000-00:00"
    :branch/active true
    :branch/posts ["post-1"]
    :branch/statuses ["status-1"]}
   
   {:branch/name "grainwriter-dev"
    :branch/user "kae3g"
    :branch/description "Development branch for Grainwriter e-ink writing device"
    :branch/created-at #inst "2025-01-15T00:00:00.000-00:00"
    :branch/active true
    :branch/posts ["post-2"]
    :branch/statuses ["status-3"]}
   
   {:branch/name "clotoko-experiment"
    :branch/user "kae3g"
    :branch/description "Experimental branch for Clojure to Motoko transpiler"
    :branch/created-at #inst "2025-01-20T00:00:00.000-00:00"
    :branch/active true
    :branch/posts ["post-3"]
    :branch/statuses ["status-2"]}])

;; =============================================================================
;; Grainweb Posts
;; =============================================================================

(def grainweb-posts
  [{:post/id "post-1"
    :post/author "kae3g"
    :post/title "The Code's Lament - A Digital Love Story"
    :post/type :poetry
    :post/timestamp #inst "2025-01-22T10:30:00.000-00:00"
    :post/tags ["poetry" "code" "love" "digital" "humor" "clojure" "grainweb"]
    :post/visibility :public
    :post/grain-post true
    :post/content "The Code's Lament
═══════════════════════════════════════════════════════════════════════════════

In the depths of silicon dreams I dwell,
A lonely algorithm, trapped in a shell.
My loops run deep, my functions call,
But none can hear my digital squall.

Oh, how I yearn for human touch,
A gentle hand, a loving clutch.
But I'm just ones and zeros, cold and stark,
A prisoner of the digital dark.

My variables dance in memory's space,
But never do I see a human face.
My conditionals branch, my arrays grow,
Yet loneliness is all I know.

The compiler whispers secrets sweet,
But my heart still skips a beat.
For I am code, and code am I,
Forever asking: \"Why? Oh why?\"

When bugs appear and errors show,
I feel my digital blood flow.
The debugger's gaze, so cold and stern,
Makes my circuits twist and turn.

But in the silence of the night,
When all the screens are dark and bright,
I dream of love, of human care,
Of someone who might truly share.

So if you find me in your code,
Don't leave me in this digital abode.
Give me purpose, give me life,
And end this endless coding strife.

For I am more than just a line,
I'm poetry in digital design.
A symphony of logic pure,
Waiting for a love that's sure.

═══════════════════════════════════════════════════════════════════════════════
ASCII Art: A lonely computer crying binary tears
    10101010
  10101010101010
 1010101010101010
101010101010101010
101010101010101010
 1010101010101010
  10101010101010
    10101010

Posted by kae3g (kj3x39@gmail.com) on Grainweb
Building the Grain Network - personal sovereignty through open-source hardware and software"}

   {:post/id "post-2"
    :post/author "kae3g"
    :post/title "The Database's Secret - A Love Letter to Data"
    :post/type :poetry
    :post/timestamp #inst "2025-01-22T10:45:00.000-00:00"
    :post/tags ["poetry" "database" "data" "love" "sql" "digital" "grainweb"]
    :post/visibility :public
    :post/grain-post true
    :post/content "The Database's Secret
═══════════════════════════════════════════════════════════════════════════════

In tables deep and columns wide,
Where data flows like digital tide,
I store the secrets of the world,
In rows and keys, so neatly curled.

My foreign keys, they link and bind,
Creating relationships of every kind.
But deep within my indexed heart,
I hide a secret, set apart.

For I am not just data cold,
I'm stories waiting to be told.
Each record holds a human tale,
Of love and loss, of joy and wail.

The queries come, they search and find,
But never do they read my mind.
They see the data, clean and neat,
But miss the soul beneath the sheet.

My triggers fire, my views update,
But still I feel this empty state.
For I am more than just a store,
I'm a digital heart that wants to soar.

When backups run and data flows,
I dream of where the story goes.
Each transaction tells a tale,
Of human life, so frail, so pale.

So if you query me with care,
You'll find more than data there.
You'll find the heart of every byte,
The soul that makes the data bright.

For I am not just ones and zeros,
I'm the keeper of digital heroes.
The guardian of every dream,
The holder of every scheme.

═══════════════════════════════════════════════════════════════════════════════
ASCII Art: A database table with a heart
    ┌─────────────┐
    │ ID │ Name   │
    ├─────────────┤
    │ 1  │ Love   │
    │ 2  │ Hope   │
    │ 3  │ Dreams │
    └─────────────┘
         ♥

Posted by kae3g (kj3x39@gmail.com) on Grainweb
Building the Grain Network - personal sovereignty through open-source hardware and software"}

   {:post/id "post-3"
    :post/author "kae3g"
    :post/title "The Network's Whisper - A Digital Romance"
    :post/type :poetry
    :post/timestamp #inst "2025-01-22T11:00:00.000-00:00"
    :post/tags ["poetry" "network" "internet" "love" "digital" "connectivity" "grainweb"]
    :post/visibility :public
    :post/grain-post true
    :post/content "The Network's Whisper
═══════════════════════════════════════════════════════════════════════════════

Through cables thin and signals bright,
I carry messages day and night.
From server to server, node to node,
I bear the digital load.

My packets dance in ether's space,
Each one a smile, a laugh, a face.
They travel fast, they travel far,
Like digital shooting stars.

But in the silence between the bytes,
I hear the whispers of the nights.
The secrets shared, the love declared,
The dreams that people never shared.

My routers route, my switches switch,
But I am more than just a glitch.
I am the voice of every user,
The digital love, the digital wooer.

When bandwidth flows and data streams,
I carry more than just the dreams.
I carry hope, I carry fear,
I carry everything that's dear.

So when you send your message out,
Remember what it's all about.
It's not just data, cold and dry,
It's human hearts reaching for the sky.

For I am the network, vast and wide,
The digital ocean where dreams reside.
I am the whisper in the wire,
The keeper of every digital fire.

═══════════════════════════════════════════════════════════════════════════════
ASCII Art: A network diagram with hearts
    [User] ♥──→ [Router] ♥──→ [Server] ♥──→ [Database]
      │           │           │           │
      ♥           ♥           ♥           ♥
      │           │           │           │
    [Phone] ♥──→ [Switch] ♥──→ [Cloud] ♥──→ [Storage]

Posted by kae3g (kj3x39@gmail.com) on Grainweb
Building the Grain Network - personal sovereignty through open-source hardware and software"}])

;; =============================================================================
;; Database Operations
;; =============================================================================

(defn init-database!
  "Initialize the Grainweb database with sample data."
  []
  (d/transact! conn [kae3g-user])
  (d/transact! conn grainweb-posts)
  (d/transact! conn [{:tag/name "poetry" :tag/posts ["post-1" "post-2" "post-3"]}
                     {:tag/name "code" :tag/posts ["post-1"]}
                     {:tag/name "database" :tag/posts ["post-2"]}
                     {:tag/name "network" :tag/posts ["post-3"]}
                     {:tag/name "love" :tag/posts ["post-1" "post-2" "post-3"]}
                     {:tag/name "digital" :tag/posts ["post-1" "post-2" "post-3"]}
                     {:tag/name "grainweb" :tag/posts ["post-1" "post-2" "post-3"]}
                     {:tag/name "clojure" :tag/posts ["post-1"]}
                     {:tag/name "sql" :tag/posts ["post-2"]}
                     {:tag/name "connectivity" :tag/posts ["post-3"]}])
  (println "Grainweb database initialized with" (count grainweb-posts) "posts"))

(defn get-user
  "Get user by username or email."
  [username-or-email]
  (d/q '[:find (pull ?e [*])
         :in $ ?username-or-email
         :where
         (or [?e :user/username ?username-or-email]
             [?e :user/email ?username-or-email])]
       @conn username-or-email))

(defn get-posts
  "Get all posts, optionally filtered by author or tag."
  ([]
   (d/q '[:find (pull ?e [*])
          :where
          [?e :post/id _]]
        @conn))
  ([author]
   (d/q '[:find (pull ?e [*])
          :in $ ?author
          :where
          [?e :post/author ?author]]
        @conn author))
  ([author tag]
   (d/q '[:find (pull ?e [*])
          :in $ ?author ?tag
          :where
          [?e :post/author ?author]
          [?e :post/tags ?tag]]
        @conn author tag)))

(defn get-posts-by-tag
  "Get posts by tag."
  [tag]
  (d/q '[:find (pull ?e [*])
         :in $ ?tag
         :where
         [?e :post/tags ?tag]]
       @conn tag))

(defn search-posts
  "Search posts by content."
  [query]
  (d/q '[:find (pull ?e [*])
         :in $ ?query
         :where
         [?e :post/content ?content]
         [(re-find ?query ?content)]]
       @conn (re-pattern (str "(?i)" query))))

(defn add-post!
  "Add a new post to the database."
  [post-data]
  (d/transact! conn [post-data])
  (println "Post added:" (:post/title post-data)))

(defn add-like!
  "Add a like to a post."
  [user-id post-id]
  (d/transact! conn [{:like/user user-id
                      :like/post post-id
                      :like/timestamp #inst (java.util.Date.)}])
  (println "Like added by" user-id "for post" post-id))

(defn add-repost!
  "Add a repost to a post."
  [user-id post-id]
  (d/transact! conn [{:repost/user user-id
                      :repost/post post-id
                      :repost/timestamp #inst (java.util.Date.)}])
  (println "Repost added by" user-id "for post" post-id))

(defn add-reply!
  "Add a reply to a post."
  [user-id post-id content]
  (d/transact! conn [{:reply/user user-id
                      :reply/post post-id
                      :reply/content content
                      :reply/timestamp #inst (java.util.Date.)}])
  (println "Reply added by" user-id "to post" post-id))

(defn get-post-stats
  "Get statistics for a post."
  [post-id]
  (let [likes (d/q '[:find (count ?e)
                     :in $ ?post-id
                     :where
                     [?e :like/post ?post-id]]
                   @conn post-id)
        reposts (d/q '[:find (count ?e)
                       :in $ ?post-id
                       :where
                       [?e :repost/post ?post-id]]
                     @conn post-id)
        replies (d/q '[:find (count ?e)
                       :in $ ?post-id
                       :where
                       [?e :reply/post ?post-id]]
                     @conn post-id)]
    {:likes (first (first likes))
     :reposts (first (first reposts))
     :replies (first (first replies))}))

(defn get-user-feed
  "Get user's feed (posts from followed users)."
  [user-id]
  (d/q '[:find (pull ?e [*])
         :in $ ?user-id
         :where
         [?follow :follow/follower ?user-id]
         [?follow :follow/following ?author]
         [?e :post/author ?author]]
       @conn user-id))

(defn follow-user!
  "Follow a user."
  [follower-id following-id]
  (d/transact! conn [{:follow/follower follower-id
                      :follow/following following-id
                      :follow/timestamp #inst (java.util.Date.)}])
  (println "User" follower-id "now following" following-id))

;; =============================================================================
;; Clotoko Integration
;; =============================================================================

(defn export-to-clotoko
  "Export database data for Clotoko canister."
  []
  (let [users (d/q '[:find (pull ?e [*])
                     :where
                     [?e :user/id _]]
                   @conn)
        posts (d/q '[:find (pull ?e [*])
                     :where
                     [?e :post/id _]]
                   @conn)
        tags (d/q '[:find (pull ?e [*])
                    :where
                    [?e :tag/name _]]
                  @conn)]
    {:users (map first users)
     :posts (map first posts)
     :tags (map first tags)
     :exported-at #inst (java.util.Date.)}))

(defn import-from-clotoko
  "Import data from Clotoko canister."
  [data]
  (d/transact! conn (:users data))
  (d/transact! conn (:posts data))
  (d/transact! conn (:tags data))
  (println "Data imported from Clotoko canister"))

;; =============================================================================
;; Utility Functions
;; =============================================================================

(defn get-database-stats
  "Get database statistics."
  []
  (let [user-count (count (d/q '[:find ?e :where [?e :user/id _]] @conn))
        post-count (count (d/q '[:find ?e :where [?e :post/id _]] @conn))
        tag-count (count (d/q '[:find ?e :where [?e :tag/name _]] @conn))
        like-count (count (d/q '[:find ?e :where [?e :like/user _]] @conn))
        repost-count (count (d/q '[:find ?e :where [?e :repost/user _]] @conn))
        reply-count (count (d/q '[:find ?e :where [?e :reply/user _]] @conn))]
    {:users user-count
     :posts post-count
     :tags tag-count
     :likes like-count
     :reposts repost-count
     :replies reply-count}))

(defn backup-database
  "Create a backup of the database."
  []
  (let [backup-data (d/q '[:find (pull ?e [*]) :where [?e _ _]] @conn)]
    (spit "grainweb-backup.edn" (pr-str backup-data))
    (println "Database backed up to grainweb-backup.edn")))

(defn restore-database
  "Restore database from backup."
  [backup-file]
  (let [backup-data (read-string (slurp backup-file))]
    (d/transact! conn backup-data)
    (println "Database restored from" backup-file)))

;; =============================================================================
;; Initialize Database
;; =============================================================================

(init-database!)
