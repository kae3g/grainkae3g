(ns grainmusic.ui.core
  "Core UI components for Grainmusic - Bandcamp-inspired interface with blockchain integration"
  (:require [humble.ui :as ui]
            [clojure.string :as str]
            [clojure.edn :as edn]))

;; =============================================================================
;; Color Scheme (Bandcamp-inspired dark theme)
;; =============================================================================

(def colors
  {:background "#000000"
   :surface "#1a1a1a"
   :surface-variant "#2a2a2a"
   :primary "#00d4aa"  ; Bandcamp green
   :primary-variant "#00b894"
   :secondary "#6c5ce7"
   :accent "#fd79a8"
   :text "#ffffff"
   :text-secondary "#b2bec3"
   :text-muted "#636e72"
   :border "#404040"
   :success "#00b894"
   :warning "#fdcb6e"
   :error "#e17055"})

;; =============================================================================
;; Typography
;; =============================================================================

(def typography
  {:heading-1 {:font-size 48 :font-weight :bold :line-height 1.2}
   :heading-2 {:font-size 36 :font-weight :bold :line-height 1.3}
   :heading-3 {:font-size 24 :font-weight :bold :line-height 1.4}
   :body {:font-size 16 :line-height 1.5}
   :body-small {:font-size 14 :line-height 1.4}
   :caption {:font-size 12 :line-height 1.3}})

;; =============================================================================
;; Layout Components
;; =============================================================================

(defn container
  "Main container with consistent padding and max-width"
  [content & {:keys [max-width padding]}]
  (ui/column
   {:style (merge
            {:background-color (:background colors)
             :color (:text colors)
             :min-height "100vh"
             :padding (or padding 20)}
            (when max-width {:max-width max-width :margin "0 auto"}))}
   content))

(defn section
  "Content section with consistent spacing"
  [content & {:keys [title padding margin]}]
  (ui/column
   {:style {:padding (or padding 20)
            :margin (or margin "0 0 40px 0")}}
   (when title
     (ui/text
      {:style (merge (:heading-2 typography) {:margin-bottom 20})}
      title))
   content))

(defn grid
  "Responsive grid layout"
  [items & {:keys [columns gap]}]
  (ui/row
   {:style {:display :grid
            :grid-template-columns (str "repeat(" (or columns 4) ", 1fr)")
            :gap (or gap 20)}} 
   items))

;; =============================================================================
;; Button Components
;; =============================================================================

(defn button
  "Styled button component"
  [text on-click & {:keys [variant size disabled style]}]
  (let [base-style {:padding "12px 24px"
                    :border-radius 6
                    :border "none"
                    :cursor (if disabled :not-allowed :pointer)
                    :font-weight :medium
                    :transition "all 0.2s ease"}
        
        variant-style (case variant
                        :primary {:background-color (:primary colors)
                                  :color :white}
                        :secondary {:background-color (:secondary colors)
                                    :color :white}
                        :outline {:background-color :transparent
                                  :color (:primary colors)
                                  :border (str "2px solid " (:primary colors))}
                        :ghost {:background-color :transparent
                                :color (:text colors)}
                        {:background-color (:surface-variant colors)
                         :color (:text colors)})
        
        size-style (case size
                     :small {:padding "8px 16px" :font-size 14}
                     :large {:padding "16px 32px" :font-size 18}
                     {:padding "12px 24px" :font-size 16})]
    
    (ui/button
     {:on-click (when-not disabled on-click)
      :style (merge base-style variant-style size-style style)}
     text)))

(defn icon-button
  "Button with icon"
  [icon on-click & {:keys [variant size tooltip]}]
  (ui/button
   {:on-click on-click
    :style (merge
            {:background-color :transparent
             :border :none
             :cursor :pointer
             :padding 8
             :border-radius 4}
            (when tooltip {:title tooltip}))}
   (ui/text
    {:style {:font-size (case size :small 16 :large 24 20)}}
    icon)))

;; =============================================================================
;; Card Components
;; =============================================================================

(defn card
  "Card container with hover effects"
  [content & {:keys [hoverable clickable on-click]}]
  (ui/column
   {:on-click (when clickable on-click)
    :style (merge
            {:background-color (:surface colors)
             :border-radius 8
             :padding 16
             :border (str "1px solid " (:border colors))
             :transition "all 0.2s ease"}
            (when hoverable
              {:cursor :pointer
               :&:hover {:background-color (:surface-variant colors)
                         :transform "translateY(-2px)"
                         :box-shadow "0 4px 12px rgba(0,0,0,0.3)"}}))}
   content))

(defn album-card
  "Album card with cover art and info"
  [{:keys [cover title artist price duration tracks]}]
  (card
   (ui/column
    {}
    
    ;; Cover art
    (ui/image
     {:src cover
      :style {:width "100%"
              :aspect-ratio "1"
              :border-radius 6
              :margin-bottom 12}})
    
    ;; Album info
    (ui/text
     {:style (merge (:body typography) {:font-weight :bold :margin-bottom 4})}
     title)
    
    (ui/text
     {:style (merge (:body-small typography) {:color (:text-secondary colors) :margin-bottom 8})}
     artist)
    
    ;; Price and duration
    (ui/row
     {:style {:justify-content :space-between :align-items :center}}
     
     (ui/text
      {:style (merge (:body-small typography) {:color (:primary colors) :font-weight :bold})}
      (str "$" price " ICP"))
     
     (ui/text
      {:style (merge (:caption typography) {:color (:text-muted colors)})}
      (str duration " • " tracks " tracks")))
    
    ;; Action buttons
    (ui/row
     {:style {:margin-top 12 :gap 8}}
     
     (button "▶️ Play" #(play-album title) :variant :primary :size :small)
     (button "❤️" #(like-album title) :variant :ghost :size :small)
     (button "📤" #(share-album title) :variant :ghost :size :small)))
   
   :hoverable true
   :clickable true
   :on-click #(navigate-to-album title)))

(defn track-row
  "Track row for track listing"
  [{:keys [number title duration price playing]}]
  (ui/row
   {:style {:padding "8px 0"
            :border-bottom (str "1px solid " (:border colors))
            :align-items :center
            :gap 16}}
   
   ;; Track number
   (ui/text
    {:style (merge (:body-small typography) {:color (:text-muted colors) :width 30})}
    (str number))
   
   ;; Play button
   (icon-button
    (if playing "⏸️" "▶️")
    #(toggle-play title)
    :size :small)
   
   ;; Track title
   (ui/text
    {:style (merge (:body typography) {:flex 1})}
    title)
   
   ;; Duration
   (ui/text
    {:style (merge (:body-small typography) {:color (:text-secondary colors) :width 60})}
    duration)
   
   ;; Price
   (ui/text
    {:style (merge (:body-small typography) {:color (:primary colors) :width 80})}
    (str "$" price))
   
   ;; More options
   (icon-button "⋯" #(show-track-menu title) :size :small)))

;; =============================================================================
;; Header Components
;; =============================================================================

(defn header
  "Main application header"
  []
  (ui/row
   {:style {:background-color (:surface colors)
            :padding "16px 24px"
            :border-bottom (str "1px solid " (:border colors))
            :align-items :center
            :justify-content :space-between}}
   
   ;; Logo and navigation
   (ui/row
    {:style {:align-items :center :gap 32}}
    
    (ui/text
     {:style (merge (:heading-2 typography) {:color (:primary colors)})}
     "🌾 Grainmusic")
    
    (ui/row
     {:style {:gap 24}}
     
     (ui/text
      {:style (merge (:body typography) {:cursor :pointer})}
      "Discover")
     
     (ui/text
      {:style (merge (:body typography) {:cursor :pointer})}
      "Library")
     
     (ui/text
      {:style (merge (:body typography) {:cursor :pointer})}
      "Artists")))
   
   ;; Search and user actions
   (ui/row
    {:style {:align-items :center :gap 16}}
    
    ;; Search bar
    (ui/input
     {:placeholder "Search music, artists, albums..."
      :style {:width 300
              :padding "8px 12px"
              :background-color (:surface-variant colors)
              :border (str "1px solid " (:border colors))
              :border-radius 20
              :color (:text colors)}})
    
    ;; Wallet connection status
    (if (wallet-connected?)
      (ui/row
       {:style {:align-items :center :gap 8}}
       
       (ui/text
        {:style (merge (:body-small typography) {:color (:success colors)})}
        "✅ Connected")
       
       (ui/text
        {:style (merge (:body-small typography) {:color (:text-secondary colors)})}
        (str (get-balance) " ICP")))
      
      (button "🔗 Connect Wallet" connect-wallet :variant :outline :size :small))
    
    ;; User menu
    (icon-button "👤" show-user-menu)))

;; =============================================================================
;; Player Components
;; =============================================================================

(defn mini-player
  "Mini player bar at bottom"
  []
  (let [current-track (get-current-track)
        playing? (is-playing?)]
    
    (ui/row
     {:style {:background-color (:surface colors)
              :padding "12px 24px"
              :border-top (str "1px solid " (:border colors))
              :align-items :center
              :gap 16}}
     
     ;; Track info
     (when current-track
       (ui/row
        {:style {:align-items :center :gap 12 :flex 1}}
        
        (ui/image
         {:src (:cover current-track)
          :style {:width 40 :height 40 :border-radius 4}})
        
        (ui/column
         {}
         
         (ui/text
          {:style (merge (:body typography) {:font-weight :bold})}
          (:title current-track))
         
         (ui/text
          {:style (merge (:body-small typography) {:color (:text-secondary colors)})}
          (:artist current-track)))))
     
     ;; Player controls
     (ui/row
      {:style {:align-items :center :gap 8}}
      
      (icon-button "⏮️" previous-track :size :small)
      (icon-button
       (if playing? "⏸️" "▶️")
       toggle-play
       :size :small)
      (icon-button "⏭️" next-track :size :small))
     
     ;; Progress bar
     (ui/row
      {:style {:flex 1 :align-items :center :gap 8}}
      
      (ui/text
       {:style (merge (:caption typography) {:color (:text-muted colors)})}
       "0:00")
      
      (ui/div
       {:style {:flex 1
                :height 4
                :background-color (:border colors)
                :border-radius 2
                :cursor :pointer}}
       (ui/div
        {:style {:width "30%"
                 :height "100%"
                 :background-color (:primary colors)
                 :border-radius 2}}))
      
      (ui/text
       {:style (merge (:caption typography) {:color (:text-muted colors)})}
       "3:45"))
     
     ;; Volume and more
     (ui/row
      {:style {:align-items :center :gap 8}}
      
      (icon-button "🔊" toggle-mute :size :small)
      (icon-button "⋯" show-player-menu :size :small))))

;; =============================================================================
;; Modal Components
;; =============================================================================

(defn modal
  "Modal overlay component"
  [visible? content & {:keys [title on-close]}]
  (when visible?
    (ui/column
     {:style {:position :fixed
              :top 0 :left 0 :right 0 :bottom 0
              :background-color "rgba(0,0,0,0.8)"
              :z-index 1000
              :align-items :center
              :justify-content :center}}
     
     (ui/column
      {:style {:background-color (:surface colors)
               :border-radius 8
               :padding 24
               :max-width 500
               :width "90%"
               :max-height "90%"
               :overflow :auto}}
      
      ;; Modal header
      (when title
        (ui/row
         {:style {:justify-content :space-between :align-items :center :margin-bottom 20}}
         
         (ui/text
          {:style (merge (:heading-3 typography) {})}
          title)
         
         (icon-button "✕" on-close :size :small)))
      
      ;; Modal content
      content))))

(defn payment-modal
  "Payment modal for track/album purchases"
  [track amount visible? on-close]
  (modal
   visible?
   (ui/column
    {}
    
    ;; Track info
    (ui/row
     {:style {:margin-bottom 20}}
     
     (ui/image
      {:src (:cover track)
       :style {:width 80 :height 80 :border-radius 6}})
     
     (ui/column
      {:style {:margin-left 16}}
      
      (ui/text
       {:style (merge (:heading-3 typography) {})}
       (:title track))
      
      (ui/text
       {:style (merge (:body typography) {:color (:text-secondary colors)})}
       (:artist track))))
    
    ;; Amount
    (ui/text
     {:style (merge (:heading-2 typography) {:color (:primary colors) :margin-bottom 20})}
     (str "Amount: " amount " ICP"))
    
    ;; Payment method selection
    (ui/column
     {:style {:margin-bottom 20}}
     
     (ui/text
      {:style (merge (:body typography) {:margin-bottom 12})}
      "Payment Method:")
     
     (ui/row
      {:style {:gap 12}}
      
      (button "🌐 ICP" #(select-payment-method :icp) :variant :outline)
      (button "☀️ Solana" #(select-payment-method :solana) :variant :outline)
      (button "🎵 Audius" #(select-payment-method :audius) :variant :outline)))
    
    ;; Wallet status
    (if (wallet-connected?)
      (ui/text
       {:style (merge (:body typography) {:color (:success colors) :margin-bottom 20})}
       "✅ Wallet Connected")
      (button "🔗 Connect Wallet" connect-wallet :variant :primary))
    
    ;; Action buttons
    (ui/row
     {:style {:justify-content :space-between :margin-top 20}}
     
     (button "Cancel" on-close :variant :ghost)
     (button "💎 Complete Purchase" #(process-payment track amount) :variant :primary)))
   
   :title "Complete Purchase"
   :on-close on-close))

;; =============================================================================
;; Placeholder Functions (to be implemented)
;; =============================================================================

(defn wallet-connected? [] false)
(defn get-balance [] "0.00")
(defn connect-wallet [] (println "Connecting wallet..."))
(defn play-album [title] (println "Playing album:" title))
(defn like-album [title] (println "Liking album:" title))
(defn share-album [title] (println "Sharing album:" title))
(defn navigate-to-album [title] (println "Navigating to album:" title))
(defn toggle-play [title] (println "Toggling play for:" title))
(defn show-track-menu [title] (println "Showing track menu for:" title))
(defn get-current-track [] nil)
(defn is-playing? [] false)
(defn previous-track [] (println "Previous track"))
(defn next-track [] (println "Next track"))
(defn toggle-mute [] (println "Toggling mute"))
(defn show-player-menu [] (println "Showing player menu"))
(defn show-user-menu [] (println "Showing user menu"))
(defn select-payment-method [method] (println "Selected payment method:" method))
(defn process-payment [track amount] (println "Processing payment for" (:title track) "amount:" amount))
