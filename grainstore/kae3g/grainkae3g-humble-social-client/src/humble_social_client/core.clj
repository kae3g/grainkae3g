(ns humble-social-client.core
  "Unified social media client with Humble UI"
  (:require [humble.ui :as ui]
            [clojure.core.async :as async :refer [go go-loop timeout <! >! <!! >!!]]
            [clojure.java-time :as time]
            [clj-http.client :as http]
            [cheshire.core :as json]
            [crypto.random :as random]
            [clojure.string :as str]))

;; Configuration
(def config
  {:x-api
   {:base-url "https://api.twitter.com/2"
    :oauth-url "https://api.twitter.com/oauth2/token"
    :bearer-token (System/getenv "X_BEARER_TOKEN")}
   
   :nostr-primal
   {:base-url "https://primal.net"
    :relay-url "wss://relay.primal.net"
    :api-key (System/getenv "NOSTR_API_KEY")}
   
   :bluesky
   {:base-url "https://bsky.social"
    :api-url "https://bsky.social/xrpc"
    :handle (System/getenv "BLUESKY_HANDLE")
    :password (System/getenv "BLUESKY_PASSWORD")}
   
   :threads
   {:base-url "https://www.threads.net"
    {:api-url "https://www.threads.net/api"
     :session-id (System/getenv "THREADS_SESSION_ID")
     :user-id (System/getenv "THREADS_USER_ID")}})

;; Application state
(def app-state
  (atom
   {:active-platform :x
    :posts []
    :compose-text ""
    :selected-post nil
    :notifications []
    :user-profiles {}
    :timeline-refreshing false
    :last-update (time/local-date-time)}))

;; X (Twitter) API Integration
(defn x-post-tweet [text]
  "Post a tweet to X"
  (let [url (str (:base-url (:x-api config)) "/tweets")
        headers {"Authorization" (str "Bearer " (:bearer-token (:x-api config)))
                 "Content-Type" "application/json"}
        body {:text text}]
    (try
      (let [response (http/post url
                               {:headers headers
                                :body (json/generate-string body)
                                :throw-exceptions false})]
        (if (= (:status response) 201)
          {:success true :data (json/parse-string (:body response) true)}
          {:success false :error (:body response)}))
      (catch Exception e
        {:success false :error (.getMessage e)}))))

(defn x-get-timeline []
  "Get X timeline"
  (let [url (str (:base-url (:x-api config)) "/users/me/timeline")
        headers {"Authorization" (str "Bearer " (:bearer-token (:x-api config)))}]
    (try
      (let [response (http/get url {:headers headers :throw-exceptions false})]
        (if (= (:status response) 200)
          {:success true :data (json/parse-string (:body response) true)}
          {:success false :error (:body response)}))
      (catch Exception e
        {:success false :error (.getMessage e)}))))

;; Nostr + Primal Integration
(defn nostr-publish-note [content]
  "Publish a note to Nostr via Primal"
  (let [url (str (:base-url (:nostr-primal config)) "/api/v1/notes")
        headers {"Content-Type" "application/json"
                 "Authorization" (str "Bearer " (:api-key (:nostr-primal config)))}
        body {:content content
              :kind 1
              :created_at (int (/ (System/currentTimeMillis) 1000))}]
    (try
      (let [response (http/post url
                               {:headers headers
                                :body (json/generate-string body)
                                :throw-exceptions false})]
        (if (= (:status response) 200)
          {:success true :data (json/parse-string (:body response) true)}
          {:success false :error (:body response)}))
      (catch Exception e
        {:success false :error (.getMessage e)}))))

(defn nostr-get-timeline []
  "Get Nostr timeline via Primal"
  (let [url (str (:base-url (:nostr-primal config)) "/api/v1/timeline")
        headers {"Authorization" (str "Bearer " (:api-key (:nostr-primal config)))}]
    (try
      (let [response (http/get url {:headers headers :throw-exceptions false})]
        (if (= (:status response) 200)
          {:success true :data (json/parse-string (:body response) true)}
          {:success false :error (:body response)}))
      (catch Exception e
        {:success false :error (.getMessage e)}))))

;; Bluesky Integration
(defn bluesky-create-session []
  "Create Bluesky session"
  (let [url (str (:api-url (:bluesky config)) "/com.atproto.server.createSession")
        body {:identifier (:handle (:bluesky config))
              :password (:password (:bluesky config))}]
    (try
      (let [response (http/post url
                               {:headers {"Content-Type" "application/json"}
                                :body (json/generate-string body)
                                :throw-exceptions false})]
        (if (= (:status response) 200)
          {:success true :data (json/parse-string (:body response) true)}
          {:success false :error (:body response)}))
      (catch Exception e
        {:success false :error (.getMessage e)}))))

(defn bluesky-post-skeet [text session-token]
  "Post a skeet to Bluesky"
  (let [url (str (:api-url (:bluesky config)) "/com.atproto.repo.createRecord")
        headers {"Authorization" (str "Bearer " session-token)
                 "Content-Type" "application/json"}
        body {:repo (:handle (:bluesky config))
              :collection "app.bsky.feed.post"
              :record {:text text
                       :createdAt (time/format "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" (time/local-date-time))}}]
    (try
      (let [response (http/post url
                               {:headers headers
                                :body (json/generate-string body)
                                :throw-exceptions false})]
        (if (= (:status response) 200)
          {:success true :data (json/parse-string (:body response) true)}
          {:success false :error (:body response)}))
      (catch Exception e
        {:success false :error (.getMessage e)}))))

;; Threads (Meta) Integration
(defn threads-post [text session-id user-id]
  "Post to Threads"
  (let [url (str (:api-url (:threads config)) "/graphql")
        headers {"Content-Type" "application/json"
                 "X-ASBD-ID" "129477"
                 "X-IG-App-Locale" "en_US"
                 "X-IG-WWW-Claim" "0"
                 "X-Requested-With" "XMLHttpRequest"
                 "Cookie" (str "sessionid=" session-id)}
        body {:query "mutation CreatePost($input: CreatePostInput!) { createPost(input: $input) { success } }"
              :variables {:input {:text text
                                  :audience "everyone"
                                  :reply_control "everyone"}}}]
    (try
      (let [response (http/post url
                               {:headers headers
                                :body (json/generate-string body)
                                :throw-exceptions false})]
        (if (= (:status response) 200)
          {:success true :data (json/parse-string (:body response) true)}
          {:success false :error (:body response)}))
      (catch Exception e
        {:success false :error (.getMessage e)}))))

;; Unified posting function
(defn post-to-all-platforms [text]
  "Post to all connected platforms"
  (let [results (atom {})]
    (go
      ;; X (Twitter)
      (let [x-result (<! (go (x-post-tweet text)))]
        (swap! results assoc :x x-result))
      
      ;; Nostr
      (let [nostr-result (<! (go (nostr-publish-note text)))]
        (swap! results assoc :nostr nostr-result))
      
      ;; Bluesky
      (let [session (<! (go (bluesky-create-session)))]
        (when (:success session)
          (let [bluesky-result (<! (go (bluesky-post-skeet text (:accessJwt (:data session)))))]
            (swap! results assoc :bluesky bluesky-result))))
      
      ;; Threads
      (let [threads-result (<! (go (threads-post text (:session-id (:threads config)) (:user-id (:threads config)))))]
        (swap! results assoc :threads threads-result))
      
      (swap! app-state assoc :last-post-results @results)
      @results)))

;; UI Components
(defn platform-tabs []
  "Platform selection tabs"
  (let [platforms [{:key :x :name "X (Twitter)" :color "#1DA1F2"}
                   {:key :nostr :name "Nostr" :color "#8B5CF6"}
                   {:key :bluesky :name "Bluesky" :color "#00A8E8"}
                   {:key :threads :name "Threads" :color "#000000"}]]
    (ui/row
     (for [platform platforms]
       (ui/button
        {:on-click #(swap! app-state assoc :active-platform (:key platform))
         :style {:background (if (= (:key platform) (:active-platform @app-state))
                               (:color platform)
                               "#333333")
                 :color "#ffffff"
                 :padding 8
                 :margin 2
                 :border-radius 4}}
        (ui/label (:name platform)))))))

(defn compose-panel []
  "Compose new post panel"
  (ui/column
   {:style {:background "#1a1a1a"
            :padding 16
            :border-radius 8
            :margin 8}}
   (ui/label
    {:style {:font-size 18
             :color "#ffffff"
             :margin-bottom 8}}
    "Compose Post")
   
   (ui/textarea
    {:value (:compose-text @app-state)
     :on-change #(swap! app-state assoc :compose-text %)
     :placeholder "What's happening?"
     :style {:background "#2a2a2a"
             :color "#ffffff"
             :border "1px solid #444444"
             :border-radius 4
             :padding 8
             :min-height 100
             :margin-bottom 8}})
   
   (ui/row
    (ui/button
     {:on-click #(do
                   (post-to-all-platforms (:compose-text @app-state))
                   (swap! app-state assoc :compose-text ""))
      :style {:background "#1DA1F2"
              :color "#ffffff"
              :padding "8px 16px"
              :border-radius 4}}
     (ui/label "Post to All Platforms"))
    
    (ui/button
     {:on-click #(swap! app-state assoc :compose-text "")
      :style {:background "#666666"
              :color "#ffffff"
              :padding "8px 16px"
              :border-radius 4
              :margin-left 8}}
     (ui/label "Clear")))))

(defn post-card [post platform]
  "Individual post card"
  (ui/column
   {:style {:background "#2a2a2a"
            :padding 12
            :border-radius 8
            :margin 4
            :border "1px solid #444444"}}
   (ui/row
    (ui/label
     {:style {:font-weight "bold"
              :color "#ffffff"}}
     (str "@" (:username post "unknown")))
    (ui/label
     {:style {:color "#888888"
              :margin-left 8}}
     (time/format "MMM dd, HH:mm" (:created-at post))))
   
   (ui/label
    {:style {:color "#ffffff"
             :margin "8px 0"}}
    (:text post))
   
   (ui/row
    (ui/button
     {:on-click #(println "Like post" (:id post))
      :style {:background "transparent"
              :color "#888888"
              :padding "4px 8px"}}
     (ui/label (str "❤️ " (:likes post 0))))
    
    (ui/button
     {:on-click #(println "Reply to post" (:id post))
      :style {:background "transparent"
              :color "#888888"
              :padding "4px 8px"}}
     (ui/label (str "💬 " (:replies post 0))))
    
    (ui/button
     {:on-click #(println "Repost" (:id post))
      :style {:background "transparent"
              :color "#888888"
              :padding "4px 8px"}}
     (ui/label (str "🔄 " (:reposts post 0)))))))

(defn timeline-panel []
  "Timeline display panel"
  (ui/column
   {:style {:background "#1a1a1a"
            :padding 8
            :border-radius 8
            :margin 8
            :max-height 400}}
   (ui/label
    {:style {:font-size 16
             :color "#ffffff"
             :margin-bottom 8}}
    (str (str/upper-case (name (:active-platform @app-state))) " Timeline"))
   
   (ui/scroll
    (ui/column
     (for [post (:posts @app-state)]
       (post-card post (:active-platform @app-state)))))))

(defn notifications-panel []
  "Notifications panel"
  (ui/column
   {:style {:background "#1a1a1a"
            :padding 8
            :border-radius 8
            :margin 8
            :max-height 200}}
   (ui/label
    {:style {:font-size 16
             :color "#ffffff"
             :margin-bottom 8}}
    "Notifications")
   
   (ui/scroll
    (ui/column
     (for [notification (:notifications @app-state)]
       (ui/label
        {:style {:color "#888888"
                 :padding 4}}
        (:message notification)))))))

(defn main-panel []
  "Main application panel"
  (ui/column
   {:style {:background "#0a0a0a"
            :padding 16
            :height "100%"
            :width "100%"}}
   
   ;; Header
   (ui/label
    {:style {:font-size 24
             :color "#ffffff"
             :margin-bottom 16}}
    "🌐 Humble Social Client")
   
   ;; Platform tabs
   (platform-tabs)
   
   ;; Main content area
   (ui/row
    {:style {:flex 1
             :margin-top 16}}
    
    ;; Left column - Compose
    (ui/column
     {:style {:width "30%"
              :margin-right 16}}
     (compose-panel)
     (notifications-panel))
    
    ;; Right column - Timeline
    (ui/column
     {:style {:width "70%"}}
     (timeline-panel)))))

;; Main application
(defn -main []
  "Start the Humble Social Client"
  (println "🌐 Starting Humble Social Client...")
  (println "   X (Twitter) API Integration")
  (println "   Nostr + Primal Integration") 
  (println "   Bluesky API Integration")
  (println "   Threads (Meta) API Integration")
  
  (ui/start-app!
   (ui/window
    {:title "Humble Social Client"
     :width 1200
     :height 800
     :on-close (fn [] (println "Application closing"))}
    main-panel)))

;; Run the application
(-main)

