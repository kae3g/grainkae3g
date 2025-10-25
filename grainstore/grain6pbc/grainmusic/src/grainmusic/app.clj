(ns grainmusic.app
  "Main Grainmusic application - Bandcamp-inspired music platform with blockchain integration"
  (:require [humble.ui :as ui]
            [grainmusic.ui.core :as ui-core]
            [grainmusic.data :as data]
            [grainmusic.wallet :as wallet]
            [grainmusic.player :as player]))

;; =============================================================================
;; Application State
;; =============================================================================

(defonce app-state
  (atom
   {:current-view :discover
    :wallet-connected false
    :current-track nil
    :playing false
    :playlist []
    :search-query ""
    :selected-payment-method :icp
    :modal {:payment {:visible false :track nil :amount 0}}
    :user {:name "kae3g"
           :balance "0.00"
           :favorites []
           :playlists []}}))

;; =============================================================================
;; View Components
;; =============================================================================

(defn discover-view
  "Main discovery feed view"
  []
  (ui-core/section
   (ui/column
    {}
    
    ;; Featured albums
    (ui-core/section
     (ui/column
      {}
      
      (ui/text
       {:style (merge (:heading-2 ui-core/typography) {:margin-bottom 20})}
       "🎵 Discover New Music")
      
      (ui-core/grid
       (map ui-core/album-card data/featured-albums)
       :columns 4))
     :title "Featured Albums")
    
    ;; Trending tracks
    (ui-core/section
     (ui/column
      {}
      
      (ui/text
       {:style (merge (:heading-2 ui-core/typography) {:margin-bottom 20})}
       "🔥 Trending This Week")
      
      (ui/column
       {}
       (map ui-core/trending-track-row data/trending-tracks)))
     :title "Trending Tracks")
    
    ;; Featured artists
    (ui-core/section
     (ui/column
      {}
      
      (ui/text
       {:style (merge (:heading-2 ui-core/typography) {:margin-bottom 20})}
       "🌟 Featured Artists")
      
      (ui-core/grid
       (map ui-core/artist-card data/featured-artists)
       :columns 3))
     :title "Featured Artists"))
   :title "Discover"))

(defn library-view
  "User's music library"
  []
  (ui-core/section
   (ui/column
    {}
    
    ;; Library stats
    (ui/row
     {:style {:justify-content :space-between :margin-bottom 20}}
     
     (ui/text
      {:style (merge (:heading-2 ui-core/typography) {})}
      "📚 My Library")
     
     (ui/text
      {:style (merge (:body ui-core/typography) {:color (:text-secondary ui-core/colors)})}
      (str (count (:playlist @app-state)) " tracks")))
    
    ;; Playlists
    (ui/column
     {}
     
     (ui/text
      {:style (merge (:heading-3 ui-core/typography) {:margin-bottom 16})}
      "Playlists")
     
     (ui/column
      {}
      (map ui-core/playlist-card (:playlists (:user @app-state)))))
    
    ;; Favorites
    (ui/column
     {}
     
     (ui/text
      {:style (merge (:heading-3 ui-core/typography) {:margin-bottom 16})}
      "Favorites")
     
     (ui/column
      {}
      (map ui-core/track-row (:favorites (:user @app-state)))))
    
    ;; Recently played
    (ui/column
     {}
     
     (ui/text
      {:style (merge (:heading-3 ui-core/typography) {:margin-bottom 16})}
      "Recently Played")
     
     (ui/column
      {}
      (map ui-core/track-row data/recently-played))))
   :title "Library"))

(defn artist-view
  "Artist profile and discography"
  [artist-id]
  (let [artist (data/get-artist artist-id)
        albums (data/get-artist-albums artist-id)]
    
    (ui-core/section
     (ui/column
      {}
      
      ;; Artist header
      (ui/row
       {:style {:margin-bottom 30}}
       
       (ui/image
        {:src (:avatar artist)
         :style {:width 200 :height 200 :border-radius 10}})
       
       (ui/column
        {:style {:margin-left 30 :flex 1}}
        
        (ui/text
         {:style (merge (:heading-1 ui-core/typography) {})}
         (:name artist))
        
        (ui/text
         {:style (merge (:body ui-core/typography) {:color (:text-secondary ui-core/colors) :margin-top 8})}
         (:location artist))
        
        (ui/text
         {:style (merge (:body ui-core/typography) {:margin-top 16 :line-height 1.6})}
         (:bio artist))
        
        ;; Action buttons
        (ui/row
         {:style {:margin-top 20 :gap 12}}
         
         (ui-core/button "🎵 Play All" #(player/play-artist artist-id) :variant :primary)
         (ui-core/button "❤️ Follow" #(data/follow-artist artist-id) :variant :outline)
         (ui-core/button "📤 Share" #(data/share-artist artist-id) :variant :ghost)))
      
      ;; Support section
      (when (:wallet-connected @app-state)
        (ui-core/support-section artist))
      
      ;; Albums
      (ui/column
       {}
       
       (ui/text
        {:style (merge (:heading-2 ui-core/typography) {:margin-bottom 20})}
        "Albums")
       
       (ui-core/grid
        (map ui-core/album-card albums)
        :columns 3)))
     :title (:name artist))))

(defn search-view
  "Search results view"
  [query]
  (ui-core/section
   (ui/column
    {}
    
    (ui/text
     {:style (merge (:heading-2 ui-core/typography) {:margin-bottom 20})}
     (str "Search results for \"" query "\""))
    
    ;; Search results
    (ui/column
     {}
     
     ;; Artists
     (when-let [artists (data/search-artists query)]
       (ui/column
        {}
        
        (ui/text
         {:style (merge (:heading-3 ui-core/typography) {:margin-bottom 16})}
         "Artists")
        
        (ui-core/grid
         (map ui-core/artist-card artists)
         :columns 4)))
     
     ;; Albums
     (when-let [albums (data/search-albums query)]
       (ui/column
        {}
        
        (ui/text
         {:style (merge (:heading-3 ui-core/typography) {:margin-bottom 16})}
         "Albums")
        
        (ui-core/grid
         (map ui-core/album-card albums)
         :columns 4)))
     
     ;; Tracks
     (when-let [tracks (data/search-tracks query)]
       (ui/column
        {}
        
        (ui/text
         {:style (merge (:heading-3 ui-core/typography) {:margin-bottom 16})}
         "Tracks")
        
        (ui/column
         {}
         (map ui-core/track-row tracks))))))
   :title "Search"))

;; =============================================================================
;; Main Application
;; =============================================================================

(defn main-app
  "Main application component"
  []
  (let [current-view (:current-view @app-state)
        search-query (:search-query @app-state)
        payment-modal (:payment (:modal @app-state))]
    
    (ui-core/container
     (ui/column
      {}
      
      ;; Header
      (ui-core/header)
      
      ;; Main content
      (ui/row
       {:style {:flex 1}}
       
       ;; Sidebar
       (ui/column
        {:style {:width 200
                 :background-color (:surface ui-core/colors)
                 :padding 20
                 :border-right (str "1px solid " (:border ui-core/colors))}}
        
        (ui/column
         {:style {:gap 16}}
         
         (ui-core/button "🔍 Discover" #(swap! app-state assoc :current-view :discover) :variant :ghost)
         (ui-core/button "📚 Library" #(swap! app-state assoc :current-view :library) :variant :ghost)
         (ui-core/button "🎵 Artists" #(swap! app-state assoc :current-view :artists) :variant :ghost)
         (ui-core/button "📻 Radio" #(swap! app-state assoc :current-view :radio) :variant :ghost)
         
         (ui/div {:style {:height 1 :background-color (:border ui-core/colors) :margin "16px 0"}})
         
         (ui/text
          {:style (merge (:body-small ui-core/typography) {:color (:text-muted ui-core/colors)})}
          "Your Music")
         
         (ui-core/button "❤️ Favorites" #(swap! app-state assoc :current-view :favorites) :variant :ghost)
         (ui-core/button "📥 Downloads" #(swap! app-state assoc :current-view :downloads) :variant :ghost)
         (ui-core/button "🕒 Recently Played" #(swap! app-state assoc :current-view :recent) :variant :ghost)))
       
       ;; Main content area
       (ui/column
        {:style {:flex 1 :padding 20}}
        
        ;; Render current view
        (case current-view
          :discover (discover-view)
          :library (library-view)
          :artists (artist-view "grimes")
          :search (search-view search-query)
          (discover-view))))
      
      ;; Mini player
      (ui-core/mini-player)
      
      ;; Payment modal
      (ui-core/payment-modal
       (:track payment-modal)
       (:amount payment-modal)
       (:visible payment-modal)
       #(swap! app-state update-in [:modal :payment] assoc :visible false)))
     
     :max-width 1400)))

;; =============================================================================
;; Application Entry Point
;; =============================================================================

(defn -main
  "Application entry point"
  [& args]
  (println "🎵 Starting Grainmusic...")
  (println "🌾 Bandcamp-inspired interface with blockchain integration")
  (println "🔗 Built with Clojure Humble UI and Clotoko ICP")
  
  ;; Initialize wallet connection
  (wallet/init!)
  
  ;; Initialize player
  (player/init!)
  
  ;; Start the application
  (ui/run main-app))

;; =============================================================================
;; Development Helpers
;; =============================================================================

(defn reset-app-state!
  "Reset application state for development"
  []
  (reset! app-state
          {:current-view :discover
           :wallet-connected false
           :current-track nil
           :playing false
           :playlist []
           :search-query ""
           :selected-payment-method :icp
           :modal {:payment {:visible false :track nil :amount 0}}
           :user {:name "kae3g"
                  :balance "0.00"
                  :favorites []
                  :playlists []}}))

(defn toggle-wallet!
  "Toggle wallet connection for development"
  []
  (swap! app-state update :wallet-connected not))

(defn add-sample-track!
  "Add sample track for development"
  []
  (swap! app-state update-in [:playlist] conj
         {:title "Oblivion"
          :artist "Grimes"
          :album "Visions"
          :duration "4:12"
          :cover "https://example.com/cover.jpg"
          :price "0.50"}))

;; =============================================================================
;; Export for development
;; =============================================================================

(comment
  ;; Development helpers
  (reset-app-state!)
  (toggle-wallet!)
  (add-sample-track!)
  
  ;; View current state
  @app-state
  
  ;; Start the app
  (-main))
