(ns grainwifi.humble-ui
  "Humble UI interface for GrainWiFi dual-connection manager
   
   Forest cabin edition with time-aware switching and beautiful visualization"
  (:require [io.github.humbleui.core :as hui]
            [io.github.humbleui.paint :as paint]
            [io.github.humbleui.ui :as ui]
            [graintime.core :as gt]
            [grainwifi.core :as wifi]
            [grainwifi.monitor :as monitor]
            [clojure.core.async :as async]))

;;; ╔═══════════════════════════════════════════════════════════════════╗
;;; ║                                                                   ║
;;; ║   🌲  G R A I N W I F I   H U M B L E   U I  🛰️                  ║
;;; ║                                                                   ║
;;; ║   Forest Cabin Dual-WiFi Manager                                 ║
;;; ║   Starlink 🛰️  +  Cellular 📱  =  Resilient Connectivity         ║
;;; ║                                                                   ║
;;; ║   "Making a wave and surfing the same wave" 🌊                   ║
;;; ║                                                                   ║
;;; ╚═══════════════════════════════════════════════════════════════════╝

;; ═══════════════════════════════════════════════════════════════════
;; STATE MANAGEMENT
;; ═══════════════════════════════════════════════════════════════════

(defonce app-state
  (atom {:current-connection :starlink
         :connections {:starlink {:quality 85
                                  :latency 32
                                  :bandwidth 150
                                  :status :connected}
                       :cellular {:quality 62
                                  :latency 78
                                  :bandwidth 45
                                  :status :available}}
         :switch-history []
         :data-usage {:cellular {:used 3420
                                 :limit 10000
                                 :period :monthly}}
         :graintime (gt/generate-graintime "forest")
         :solar-house 10
         :nakshatra "vishakha"
         :storm-mode false
         :auto-mode true}))

;; ═══════════════════════════════════════════════════════════════════
;; COLOR PALETTE - Forest Cabin Theme
;; ═══════════════════════════════════════════════════════════════════

(def colors
  {:bg-dark "#1a1a1a"
   :bg-med "#2d2d2d"
   :bg-light "#3d3d3d"
   :accent-green "#00ff00"  ; Matrix green for Starlink
   :accent-blue "#00bfff"   ; Sky blue for cellular
   :accent-orange "#ff8c00" ; Warning orange
   :accent-red "#ff4444"    ; Error red
   :text-primary "#e0e0e0"
   :text-secondary "#a0a0a0"
   :text-dim "#606060"
   :border "#4d4d4d"
   :success "#00ff00"
   :warning "#ffaa00"
   :error "#ff4444"})

;; ═══════════════════════════════════════════════════════════════════
;; UTILITY FUNCTIONS
;; ═══════════════════════════════════════════════════════════════════

(defn quality-color
  "Get color based on connection quality (0-100)"
  [quality]
  (cond
    (>= quality 80) (:success colors)
    (>= quality 60) (:accent-green colors)
    (>= quality 40) (:warning colors)
    :else (:error colors)))

(defn format-bandwidth
  "Format bandwidth for display"
  [mbps]
  (str mbps " Mbps"))

(defn format-latency
  "Format latency for display"
  [ms]
  (str ms "ms"))

(defn format-data-usage
  "Format data usage for display"
  [mb]
  (if (>= mb 1000)
    (format "%.2f GB" (/ mb 1000.0))
    (str mb " MB")))

;; ═══════════════════════════════════════════════════════════════════
;; UI COMPONENTS
;; ═══════════════════════════════════════════════════════════════════

(defn header-component
  "App header with title and graintime"
  []
  (ui/column
    (ui/padding 16
      (ui/column
        (ui/label "🌲 GrainWiFi - Forest Cabin Connectivity"
                  {:font-size 24
                   :color (:accent-green colors)
                   :font-weight :bold})
        (ui/gap 8)
        (ui/label (str "⏰ " (:graintime @app-state))
                  {:font-size 12
                   :color (:text-secondary colors)})
        (ui/label (str "🏠 Solar House: " (:solar-house @app-state)
                       " | 🌙 " (:nakshatra @app-state))
                  {:font-size 12
                   :color (:text-secondary colors)})))))

(defn connection-card
  "Display card for a single connection"
  [conn-id]
  (let [conn (get-in @app-state [:connections conn-id])
        is-current (= conn-id (:current-connection @app-state))
        icon (case conn-id
               :starlink "🛰️"
               :cellular "📱"
               "❓")
        name (case conn-id
               :starlink "Starlink"
               :cellular "Cellular Tethering"
               "Unknown")
        quality (:quality conn)
        latency (:latency conn)
        bandwidth (:bandwidth conn)
        status (:status conn)]
    
    (ui/padding 12
      (ui/rect {:paint (paint/fill (if is-current
                                      (:bg-light colors)
                                      (:bg-med colors)))}
        (ui/column
          (ui/row
            (ui/label (str icon " " name)
                      {:font-size 18
                       :font-weight (if is-current :bold :normal)
                       :color (if is-current
                                (:accent-green colors)
                                (:text-primary colors))})
            (ui/gap :fill)
            (when is-current
              (ui/label "● CONNECTED"
                        {:font-size 12
                         :color (:success colors)})))
          
          (ui/gap 12)
          
          ;; Quality meter
          (ui/column
            (ui/row
              (ui/label (str "Quality: " quality "%")
                        {:color (quality-color quality)
                         :font-size 14})
              (ui/gap :fill)
              (ui/label (format-latency latency)
                        {:color (:text-secondary colors)
                         :font-size 12}))
            (ui/gap 4)
            (ui/rect {:paint (paint/fill (:bg-dark colors))
                      :height 8
                      :border-radius 4}
              (ui/width (* quality 0.01)
                (ui/rect {:paint (paint/fill (quality-color quality))
                          :border-radius 4}))))
          
          (ui/gap 8)
          
          ;; Connection stats
          (ui/row
            (ui/column
              (ui/label "Latency"
                        {:font-size 10
                         :color (:text-dim colors)})
              (ui/label (format-latency latency)
                        {:font-size 12
                         :color (:text-secondary colors)}))
            (ui/gap 16)
            (ui/column
              (ui/label "Bandwidth"
                        {:font-size 10
                         :color (:text-dim colors)})
              (ui/label (format-bandwidth bandwidth)
                        {:font-size 12
                         :color (:text-secondary colors)}))
            (ui/gap 16)
            (ui/column
              (ui/label "Status"
                        {:font-size 10
                         :color (:text-dim colors)})
              (ui/label (name status)
                        {:font-size 12
                         :color (case status
                                  :connected (:success colors)
                                  :available (:accent-blue colors)
                                  :disconnected (:error colors)
                                  (:text-secondary colors))}))))))))

(defn connections-panel
  "Panel showing both connections side-by-side"
  []
  (ui/padding 16
    (ui/column
      (ui/label "Active Connections"
                {:font-size 16
                 :font-weight :bold
                 :color (:text-primary colors)})
      (ui/gap 12)
      (ui/row
        (ui/width 0.48
          (connection-card :starlink))
        (ui/gap 0.04)
        (ui/width 0.48
          (connection-card :cellular))))))

(defn switch-button
  "Button to manually switch connections"
  [label target-conn]
  (ui/button
    {:on-click (fn [] (wifi/switch-connection! target-conn))}
    (ui/padding 12 8
      (ui/label label
                {:font-size 14
                 :color (:text-primary colors)}))))

(defn manual-controls
  "Manual control buttons"
  []
  (ui/padding 16
    (ui/column
      (ui/label "Manual Controls"
                {:font-size 16
                 :font-weight :bold
                 :color (:text-primary colors)})
      (ui/gap 12)
      (ui/row
        (switch-button "Switch to Starlink 🛰️" :starlink)
        (ui/gap 8)
        (switch-button "Switch to Cellular 📱" :cellular)
        (ui/gap 8)
        (switch-button "Auto-Select 🔄" :auto)
        (ui/gap 8)
        (ui/button
          {:on-click (fn [] (swap! app-state update :storm-mode not))}
          (ui/padding 12 8
            (ui/label (if (:storm-mode @app-state)
                        "Storm Mode: ON 🌧️"
                        "Storm Mode: OFF ☀️")
                      {:font-size 14
                       :color (if (:storm-mode @app-state)
                                (:warning colors)
                                (:text-secondary colors))})))))))

(defn data-usage-panel
  "Panel showing cellular data usage"
  []
  (let [usage (get-in @app-state [:data-usage :cellular :used])
        limit (get-in @app-state [:data-usage :cellular :limit])
        percentage (* 100 (/ usage limit))
        remaining (- limit usage)]
    
    (ui/padding 16
      (ui/column
        (ui/label "📱 Cellular Data Usage (Monthly)"
                  {:font-size 16
                   :font-weight :bold
                   :color (:text-primary colors)})
        (ui/gap 12)
        (ui/row
          (ui/label (format-data-usage usage)
                    {:font-size 24
                     :font-weight :bold
                     :color (cond
                              (>= percentage 90) (:error colors)
                              (>= percentage 70) (:warning colors)
                              :else (:accent-blue colors))})
          (ui/gap 8)
          (ui/label (str "of " (format-data-usage limit))
                    {:font-size 14
                     :color (:text-secondary colors)}))
        (ui/gap 8)
        (ui/label (str (format-data-usage remaining) " remaining")
                  {:font-size 12
                   :color (:text-dim colors)})
        (ui/gap 8)
        ;; Progress bar
        (ui/rect {:paint (paint/fill (:bg-dark colors))
                  :height 12
                  :border-radius 6}
          (ui/width (/ percentage 100)
            (ui/rect {:paint (paint/fill (cond
                                           (>= percentage 90) (:error colors)
                                           (>= percentage 70) (:warning colors)
                                           :else (:accent-blue colors)))
                      :border-radius 6})))))))

(defn switch-history-panel
  "Panel showing recent connection switches"
  []
  (ui/padding 16
    (ui/column
      (ui/label "Recent Switches"
                {:font-size 16
                 :font-weight :bold
                 :color (:text-primary colors)})
      (ui/gap 12)
      (ui/column
        (for [switch (take 5 (:switch-history @app-state))]
          (ui/padding 8
            (ui/row
              (ui/label (:graintime switch)
                        {:font-size 10
                         :color (:text-dim colors)})
              (ui/gap 8)
              (ui/label (str (:from switch) " → " (:to switch))
                        {:font-size 12
                         :color (:text-secondary colors)})
              (ui/gap 8)
              (ui/label (:reason switch)
                        {:font-size 11
                         :color (:text-dim colors)
                         :style :italic}))))))))

;; ═══════════════════════════════════════════════════════════════════
;; MAIN APP LAYOUT
;; ═══════════════════════════════════════════════════════════════════

(defn app-ui
  "Main application UI"
  []
  (ui/rect {:paint (paint/fill (:bg-dark colors))}
    (ui/column
      (header-component)
      (ui/gap 8)
      (connections-panel)
      (ui/gap 8)
      (manual-controls)
      (ui/gap 8)
      (data-usage-panel)
      (ui/gap 8)
      (switch-history-panel)
      (ui/gap 16)
      (ui/padding 16
        (ui/label "🌾 now == next + 1"
                  {:font-size 12
                   :color (:text-dim colors)
                   :style :italic
                   :align :center})))))

;; ═══════════════════════════════════════════════════════════════════
;; APPLICATION LIFECYCLE
;; ═══════════════════════════════════════════════════════════════════

(defn start-monitoring!
  "Start background monitoring of connections"
  []
  (async/go-loop []
    (async/<! (async/timeout 5000))  ; Update every 5 seconds
    
    ;; Update connection status
    (swap! app-state assoc
           :connections (monitor/check-all-connections)
           :graintime (gt/generate-graintime "forest")
           :solar-house (gt/current-solar-house)
           :nakshatra (gt/current-nakshatra))
    
    (recur)))

(defn -main
  "Launch Humble UI app"
  [& args]
  (println "🌲 Starting GrainWiFi Humble UI...")
  (println "🛰️  Monitoring: Starlink + Cellular")
  (println "⏰ Graintime:" (gt/generate-graintime "forest"))
  (println "")
  
  ;; Start background monitoring
  (start-monitoring!)
  
  ;; Launch Humble UI window
  (hui/start
    {:title "GrainWiFi - Forest Cabin"
     :width 800
     :height 900
     :vsync true}
    app-ui))

;;; ╔═══════════════════════════════════════════════════════════════════╗
;;; ║                                                                   ║
;;; ║   🌾 GrainWiFi Humble UI - Forest Cabin Edition                  ║
;;; ║                                                                   ║
;;; ║   Features:                                                       ║
;;; ║   - Real-time connection monitoring                               ║
;;; ║   - Time-aware switching (grain6 integration)                     ║
;;; ║   - Cellular data usage tracking                                  ║
;;; ║   - Manual and automatic modes                                    ║
;;; ║   - Storm mode for weather events                                 ║
;;; ║   - Beautiful forest cabin theme                                  ║
;;; ║                                                                   ║
;;; ║   "Resilient connectivity through redundancy and awareness"       ║
;;; ║                                                                   ║
;;; ║   now == next + 1 🌾                                              ║
;;; ║                                                                   ║
;;; ╚═══════════════════════════════════════════════════════════════════╝
