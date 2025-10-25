(ns grainspace.core
  "Grainspace - Unified Decentralized Platform"
  (:require [humble.ui :as ui]
            [clojure.core.async :as async :refer [go go-loop timeout <! >! <!! >!!]]
            [clojure.java-time :as time]
            [clj-http.client :as http]
            [cheshire.core :as json]
            [clojure.string :as str]
            [grainspace.identity :as identity]
            [clojure-s6.core :as s6]
            [clojure-sixos.core :as sixos]))

;; Grainspace platform configuration
(def platform-config
  {:grainspace
   {:name "Grainspace"
    :version "0.1.0"
    :description "Unified Decentralized Platform"
    :identity-system "Urbit Azimuth + ICP Subnet"
    :social-platforms [:x :nostr :bluesky :threads]
    :marketplace-enabled true
    :streaming-enabled true
    :grainstore-integration true}
   
   :marketplace
   {:apps {:enabled true :category "Applications"}
    :models {:enabled true :category "AI Models"}
    :art {:enabled true :category "Digital Art"}
    :grains {:enabled true :category "Verified Dependencies"}}
   
   :streaming
   {:webrtc-enabled true
    :live-coding true
    :tutorials true
    :demos true
    :interactive true}})

;; Application state
(def app-state
  (atom
   {:current-user nil
    :unified-identity nil
    :active-platform :marketplace
    :marketplace-items []
    :social-feeds {}
    :streaming-sessions []
    :notifications []
    :last-update (time/local-date-time)}))

;; Identity management
(defn login-with-urbit [urbit-address]
  "Login using Urbit address"
  (let [identity-result (identity/create-grainspace-identity urbit-address)]
    (if (:success identity-result)
      (do
        (swap! app-state assoc :unified-identity identity-result)
        (swap! app-state assoc :current-user (:unified-identity identity-result))
        (println (str "✅ Logged in as: " (:unified-identity identity-result)))
        true)
      (do
        (println (str "❌ Login failed: " (:error identity-result)))
        false))))

(defn connect-social-platform [platform]
  "Connect to social platform"
  (let [unified-id (:unified-identity @app-state)
        auth-result (identity/authenticate-with-platform platform unified-id)]
    (if (:success auth-result)
      (do
        (swap! app-state update :social-feeds assoc platform auth-result)
        (println (str "✅ Connected to " platform))
        true)
      (do
        (println (str "❌ Failed to connect to " platform ": " (:error auth-result)))
        false))))

;; Marketplace functions
(defn load-marketplace-items []
  "Load marketplace items"
  (let [items
        [{:id "clojure-humble-ui"
          :name "Clojure Humble UI"
          :category :apps
          :description "Native desktop UI library for Clojure"
          :price "Free"
          :rating 4.8
          :downloads 1250}
         {:id "letta-memgpt"
          :name "Letta memGPT"
          :category :models
          :description "Memory-augmented GPT model"
          :price "0.1 ICP"
          :rating 4.9
          :downloads 890}
         {:id "urbit-identity-nft"
          :name "Urbit Identity NFT"
          :category :art
          :description "Digital art representing Urbit identity"
          :price "0.5 ETH"
          :rating 4.7
          :downloads 340}
         {:id "clojure-s6"
          :name "clojure-s6"
          :category :grains
          :description "Clojure wrapper for s6 supervision"
          :price "Free"
          :rating 4.9
          :downloads 2100}]]
    (swap! app-state assoc :marketplace-items items)
    items))

;; Social media integration
(defn load-social-feeds []
  "Load social media feeds"
  (let [feeds
        {:x [{:id "1" :text "Just discovered Grainspace! This unified platform is amazing! 🚀" :author "@grainspace" :time "2m ago"}
             {:id "2" :text "Building the future of decentralized social media with Clojure and Urbit" :author "@clojure" :time "5m ago"}]
         :nostr [{:id "1" :text "Grainspace identity system is revolutionary! ~zod" :author "~zod" :time "1m ago"}]
         :bluesky [{:id "1" :text "Excited about the unified marketplace in Grainspace!" :author "@grainspace.bsky.social" :time "3m ago"}]}]
    (swap! app-state assoc :social-feeds feeds)
    feeds))

;; Streaming functions
(defn start-streaming-session [title description]
  "Start a streaming session"
  (let [session
        {:id (str "session-" (System/currentTimeMillis))
         :title title
         :description description
         :start-time (time/local-date-time)
         :viewers 0
         :status :live}]
    (swap! app-state update :streaming-sessions conj session)
    (println (str "🎥 Started streaming: " title))
    session))

;; UI Components
(defn identity-panel []
  "User identity panel"
  (let [user (:current-user @app-state)
        unified-id (:unified-identity @app-state)]
    (ui/column
     {:style {:background "#1a1a1a"
              :padding 16
              :border-radius 8
              :margin 8}}
     (ui/label
      {:style {:font-size 18
               :color "#ffffff"
               :margin-bottom 8}}
      "🌐 Identity")
     
     (if user
       (ui/column
        (ui/label
         {:style {:color "#00ff00"}}
         (str "Logged in: " unified-id))
        (ui/label
         {:style {:color "#ffffff"}}
         (str "Status: " (:verification-status user)))
        (ui/label
         {:style {:color "#ffffff"}}
         (str "Social Connections: " (count (:social-connections user)))))
       (ui/column
        (ui/button
         {:on-click #(login-with-urbit 12345) ; Example Urbit address
          :style {:background "#1DA1F2"
                  :color "#ffffff"
                  :padding "8px 16px"
                  :border-radius 4}}
         (ui/label "Login with Urbit"))
        (ui/label
         {:style {:color "#888888"
                  :font-size 12}}
         "Connect your Urbit identity to access Grainspace"))))))

(defn platform-tabs []
  "Platform navigation tabs"
  (let [tabs [{:key :marketplace :name "Marketplace" :icon "🛍️"}
              {:key :social :name "Social" :icon "👥"}
              {:key :streaming :name "Streaming" :icon "📺"}
              {:key :grainstore :name "Grainstore" :icon "📦"}]]
    (ui/row
     (for [tab tabs]
       (ui/button
        {:on-click #(swap! app-state assoc :active-platform (:key tab))
         :style {:background (if (= (:key tab) (:active-platform @app-state))
                               "#1DA1F2"
                               "#333333")
                 :color "#ffffff"
                 :padding "12px 24px"
                 :border-radius 4
                 :margin 2}}
        (ui/row
         (ui/label (:icon tab))
         (ui/label
          {:style {:margin-left 8}}
          (:name tab))))))))

(defn marketplace-panel []
  "Marketplace panel"
  (let [items (:marketplace-items @app-state)]
    (ui/column
     {:style {:background "#1a1a1a"
              :padding 16
              :border-radius 8
              :margin 8
              :flex 1}}
     (ui/label
      {:style {:font-size 20
               :color "#ffffff"
               :margin-bottom 16}}
      "🛍️ Marketplace")
     
     (ui/scroll
      (ui/column
       (for [item items]
         (ui/column
          {:style {:background "#2a2a2a"
                   :padding 16
                   :border-radius 8
                   :margin 4
                   :border "1px solid #444444"}}
          (ui/row
           (ui/column
            {:style {:flex 1}}
            (ui/label
             {:style {:font-size 16
                      :font-weight "bold"
                      :color "#ffffff"}}
             (:name item))
            (ui/label
             {:style {:color "#888888"
                      :margin "4px 0"}}
             (:description item))
            (ui/label
             {:style {:color "#1DA1F2"}}
             (str "Price: " (:price item))))
           (ui/column
            {:style {:align-items "flex-end"}}
            (ui/label
             {:style {:color "#ffaa00"}}
             (str "⭐ " (:rating item)))
            (ui/label
             {:style {:color "#888888"
                      :font-size 12}}
             (str (:downloads item) " downloads"))))
          (ui/button
           {:on-click #(println (str "Installing " (:name item)))
            :style {:background "#00ff00"
                    :color "#000000"
                    :padding "8px 16px"
                    :border-radius 4
                    :margin-top 8}}
           (ui/label "Install"))))))))

(defn social-panel []
  "Social media panel"
  (let [feeds (:social-feeds @app-state)]
    (ui/column
     {:style {:background "#1a1a1a"
              :padding 16
              :border-radius 8
              :margin 8
              :flex 1}}
     (ui/label
      {:style {:font-size 20
               :color "#ffffff"
               :margin-bottom 16}}
      "👥 Social Feeds")
     
     (ui/row
      (for [[platform posts] feeds]
        (ui/column
         {:style {:width "33%"
                  :margin 4}}
         (ui/label
          {:style {:font-size 16
                   :color "#1DA1F2"
                   :margin-bottom 8}}
          (str (str/upper-case (name platform))))
         (ui/scroll
          (ui/column
           (for [post posts]
             (ui/column
              {:style {:background "#2a2a2a"
                       :padding 12
                       :border-radius 6
                       :margin 2}}
              (ui/label
               {:style {:color "#ffffff"}}
               (:text post))
              (ui/label
               {:style {:color "#888888"
                        :font-size 12
                        :margin-top 4}}
               (str (:author post) " • " (:time post))))))))))))

(defn streaming-panel []
  "Streaming panel"
  (let [sessions (:streaming-sessions @app-state)]
    (ui/column
     {:style {:background "#1a1a1a"
              :padding 16
              :border-radius 8
              :margin 8
              :flex 1}}
     (ui/row
      (ui/label
       {:style {:font-size 20
                :color "#ffffff"
                :margin-bottom 16}}
       "📺 Streaming")
      (ui/button
       {:on-click #(start-streaming-session "Live Coding" "Building Grainspace with Clojure")
        :style {:background "#ff0000"
                :color "#ffffff"
                :padding "8px 16px"
                :border-radius 4
                :margin-left 16}}
       (ui/label "Go Live")))
     
     (ui/scroll
      (ui/column
       (for [session sessions]
         (ui/column
          {:style {:background "#2a2a2a"
                   :padding 16
                   :border-radius 8
                   :margin 4}}
          (ui/label
           {:style {:font-size 16
                    :font-weight "bold"
                    :color "#ffffff"}}
           (:title session))
          (ui/label
           {:style {:color "#888888"
                    :margin "4px 0"}}
           (:description session))
          (ui/row
           (ui/label
            {:style {:color "#ff0000"}}
            "🔴 LIVE")
           (ui/label
            {:style {:color "#888888"
                     :margin-left 16}}
            (str (:viewers session) " viewers")))))))))

(defn main-panel []
  "Main application panel"
  (ui/column
   {:style {:background "#0a0a0a"
            :padding 16
            :height "100%"
            :width "100%"}}
   
   ;; Header
   (ui/row
    {:style {:justify-content "space-between"
             :align-items "center"
             :margin-bottom 16}}
    (ui/label
     {:style {:font-size 28
              :color "#ffffff"
              :font-weight "bold"}}
     "🌐 Grainspace")
    (ui/label
     {:style {:color "#888888"}}
     "Unified Decentralized Platform"))
   
   ;; Identity panel
   (identity-panel)
   
   ;; Platform tabs
   (platform-tabs)
   
   ;; Main content area
   (case (:active-platform @app-state)
     :marketplace (marketplace-panel)
     :social (social-panel)
     :streaming (streaming-panel)
     :grainstore (ui/label
                  {:style {:color "#ffffff"
                           :text-align "center"
                           :margin 32}}
                  "Grainstore integration coming soon...")
     (ui/label
      {:style {:color "#ffffff"
               :text-align "center"
               :margin 32}}
      "Select a platform to get started"))))

;; Main application
(defn -main []
  "Start Grainspace platform"
  (println "🌐 Starting Grainspace - Unified Decentralized Platform")
  (println "   Identity: Urbit Azimuth + ICP Subnet")
  (println "   Social: X + Nostr + Bluesky + Threads")
  (println "   Marketplace: Apps + Models + Art + Grains")
  (println "   Streaming: Live coding + Demos + Tutorials")
  
  ;; Load initial data
  (load-marketplace-items)
  (load-social-feeds)
  
  (ui/start-app!
   (ui/window
    {:title "Grainspace - Unified Decentralized Platform"
     :width 1600
     :height 1000
     :on-close (fn [] (println "Grainspace closing"))}
    main-panel)))

;; Run the application
(-main)

