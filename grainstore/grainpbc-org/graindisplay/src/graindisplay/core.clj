(ns graindisplay.core
  "GrainDisplay - GNOME integration with Flux-like blue light filtering
   
   Features:
   - Warm bedtime red-orange theme
   - Graintime-based scheduling
   - GNOME integration via s6
   - Humble UI desktop app
   - SvelteKit-inspired styling"
  (:require [humble.ui :as ui]
            [humble.ui.paint :as paint]
            [humble.ui.font :as font]
            [humble.ui.window :as window]
            [humble.ui.event :as event]
            [humble.ui.layout :as layout]
            [humble.ui.input :as input]
            [humble.ui.animation :as animation]
            [clojure.core.async :as async :refer [go go-loop <! >! <!! >!! timeout]]
            [clojure.java-time :as time]
            [clojure.string :as str]
            [clojure.edn :as edn]
            [babashka.fs :as fs]
            [clojure-s6.core :as s6]
            [graintime.core :as gt]))

;; =============================================================================
;; Configuration and State
;; =============================================================================

(def config-file "~/.config/graindisplay/config.edn")
(def gnome-schema "org.gnome.desktop.interface")
(def gnome-settings "gsettings")

(def default-config
  {:theme {:warm-red "#D2691E"
           :deep-orange "#FF8C00"
           :burnt-orange "#CC5500"
           :warm-brown "#8B4513"
           :warm-black "#1A0F0A"
           :warm-dark "#2D1B13"
           :warm-gray "#3D2B1F"
           :warm-white "#FFF8DC"
           :warm-cream "#F5DEB3"
           :warm-gold "#DAA520"}
   :schedule {:sunrise-offset 0
              :sunset-offset 0
              :transition-duration 30
              :enabled true}
   :location {:latitude 37.9735
              :longitude -122.5311
              :timezone "America/Los_Angeles"}
   :display {:brightness 1.0
             :gamma 1.0
             :temperature 6500}
   :gnome {:enabled true
           :auto-start true}})

(def app-state
  (atom {:config default-config
         :current-theme :warm-red
         :is-active false
         :current-time (time/local-time)
         :sunrise-time nil
         :sunset-time nil
         :transition-progress 0.0
         :window nil
         :animation-running false}))

;; =============================================================================
;; GNOME Integration
;; =============================================================================

(defn run-gsettings [command & args]
  "Run gsettings command safely"
  (try
    (let [result (apply clojure.java.shell/sh gnome-settings command args)]
      (if (= (:exit result) 0)
        (str/trim (:out result))
        (do
          (println "gsettings error:" (:err result))
          nil)))
    (catch Exception e
      (println "gsettings exception:" (.getMessage e))
      nil)))

(defn set-gnome-theme [theme-name]
  "Set GNOME theme"
  (run-gsettings "set" gnome-schema "gtk-theme" theme-name)
  (run-gsettings "set" gnome-schema "icon-theme" theme-name))

(defn set-gnome-cursor [cursor-name]
  "Set GNOME cursor theme"
  (run-gsettings "set" gnome-schema "cursor-theme" cursor-name))

(defn set-gnome-font [font-name]
  "Set GNOME font"
  (run-gsettings "set" gnome-schema "font-name" font-name))

(defn get-gnome-theme []
  "Get current GNOME theme"
  (run-gsettings "get" gnome-schema "gtk-theme"))

(defn apply-gnome-settings [settings]
  "Apply GNOME settings map"
  (doseq [[key value] settings]
    (case key
      :theme (set-gnome-theme value)
      :cursor (set-gnome-cursor value)
      :font (set-gnome-font value)
      (println "Unknown GNOME setting:" key))))

;; =============================================================================
;; Display Control
;; =============================================================================

(defn set-gamma [red green blue]
  "Set display gamma values"
  (let [cmd (str "xrandr --output " 
                 (str/trim (clojure.java.shell/sh "xrandr" "--query" "--verbose" 
                                                  "|" "grep" "connected" "|" "head" "-1" 
                                                  "|" "awk" "{print $1}"))
                 " --gamma " red ":" green ":" blue)]
    (clojure.java.shell/sh "bash" "-c" cmd)))

(defn set-brightness [brightness]
  "Set display brightness (0.0-1.0)"
  (let [cmd (str "xrandr --output " 
                 (str/trim (clojure.java.shell/sh "xrandr" "--query" "--verbose" 
                                                  "|" "grep" "connected" "|" "head" "-1" 
                                                  "|" "awk" "{print $1}"))
                 " --brightness " brightness)]
    (clojure.java.shell/sh "bash" "-c" cmd)))

(defn set-color-temperature [temperature]
  "Set color temperature (1000-10000K)"
  (let [;; Red component decreases as temperature increases
        red (if (< temperature 6600)
              (str 1.0)
              (str (max 0.0 (- 1.0 (* 0.0001 (- temperature 6600))))))
        ;; Green component
        green (if (< temperature 6600)
                (str (min 1.0 (+ 0.4 (* 0.0001 (- temperature 1000)))))
                (str (min 1.0 (- 1.0 (* 0.0001 (- temperature 6600))))))
        ;; Blue component increases with temperature
        blue (if (< temperature 2000)
               (str 0.0)
               (str (min 1.0 (* 0.0001 (- temperature 2000)))))]
    (set-gamma red green blue)))

;; =============================================================================
;; Graintime Integration
;; =============================================================================

(defn get-sun-times [lat lon]
  "Get sunrise and sunset times for location"
  (let [;; Simplified sun calculation - in production, use proper astronomy library
        ;; This is a placeholder that returns approximate times
        now (time/local-date-time)
        hour (.getHour now)
        sunrise-hour 6
        sunset-hour 18]
    {:sunrise (time/local-time sunrise-hour 0)
     :sunset (time/local-time sunset-hour 0)}))

(defn calculate-theme-progress [current-time sunrise sunset]
  "Calculate theme transition progress (0.0 = sunrise, 1.0 = sunset)"
  (let [current-minutes (+ (* (.getHour current-time) 60) (.getMinute current-time))
        sunrise-minutes (+ (* (.getHour sunrise) 60) (.getMinute sunrise))
        sunset-minutes (+ (* (.getHour sunset) 60) (.getMinute sunset))
        day-length (- sunset-minutes sunrise-minutes)
        day-progress (- current-minutes sunrise-minutes)]
    (max 0.0 (min 1.0 (/ day-progress day-length)))))

(defn get-current-theme-settings [progress config]
  "Get current theme settings based on progress"
  (let [theme (:theme config)
        ;; Interpolate between warm (day) and bedtime (night) themes
        warm-factor (- 1.0 progress)
        bedtime-factor progress]
    {:color-temperature (int (+ 6500 (* warm-factor -5500))) ; 6500K to 1000K
     :brightness (+ 1.0 (* bedtime-factor -0.3)) ; 1.0 to 0.7
     :gamma (+ 1.0 (* bedtime-factor -0.2)) ; 1.0 to 0.8
     :theme-colors {:primary (if (< progress 0.5) (:warm-red theme) (:deep-orange theme))
                    :background (if (< progress 0.5) (:warm-white theme) (:warm-black theme))
                    :text (if (< progress 0.5) (:warm-black theme) (:warm-white theme))}}))

;; =============================================================================
;; Animation and Transitions
;; =============================================================================

(defn animate-transition [from-settings to-settings duration-ms]
  "Animate transition between display settings"
  (go-loop [progress 0.0]
    (when (< progress 1.0)
      (let [current-settings (merge-with 
                              (fn [from to] 
                                (+ from (* (- to from) progress)))
                              from-settings to-settings)]
        ;; Apply current settings
        (set-color-temperature (:color-temperature current-settings))
        (set-brightness (:brightness current-settings))
        
        ;; Update state
        (swap! app-state assoc :transition-progress progress)
        
        ;; Wait for next frame (60fps)
        (<! (timeout 16))
        (recur (min 1.0 (+ progress 0.016)))))))

;; =============================================================================
;; Humble UI Components
;; =============================================================================

(defn color-button [color on-click]
  "Color picker button"
  (ui/clickable
   {:on-click on-click}
   (ui/rect
    {:paint (paint/fill color)
     :width 40
     :height 40})))

(defn slider [value min max on-change]
  "Custom slider component"
  (ui/clickable
   {:on-click (fn [event]
                (when-let [x (event/mouse-x event)]
                  (let [new-value (+ min (* (- max min) (/ x 200)))]
                    (on-change (max min (min max new-value))))))}
   (ui/rect
    {:paint (paint/fill "#333333")
     :width 200
     :height 20}
    (ui/rect
     {:paint (paint/fill "#D2691E")
      :width (* 200 (/ (- value min) (- max min)))
      :height 20}))))

(defn time-display [time]
  "Display current time with graintime"
  (let [formatted-time (time/format "HH:mm:ss" time)
        graintime (gt/generate-graintime "system")]
    (ui/column
     {:child-placement :top-left
      :padding 16}
     (ui/label formatted-time {:font (font/make-with-size 24)})
     (ui/label graintime {:font (font/make-with-size 12)
                          :color "#DAA520"}))))

(defn theme-preview [theme-colors]
  "Preview current theme colors"
  (ui/rect
   {:paint (paint/fill (:background theme-colors))
    :width 300
    :height 200}
   (ui/column
    {:child-placement :top-left
     :padding 16}
    (ui/label "Theme Preview" 
              {:font (font/make-with-size 16)
               :color (:text theme-colors)})
    (ui/label "GrainDisplay Bedtime Warm" 
              {:font (font/make-with-size 12)
               :color (:primary theme-colors)})
    (ui/rect
     {:paint (paint/fill (:primary theme-colors))
      :width 100
      :height 30}
     (ui/label "Button" 
               {:font (font/make-with-size 12)
                :color (:background theme-colors)})))))

(defn main-panel []
  "Main application panel"
  (let [state @app-state
        config (:config state)
        theme-colors (get-in state [:current-theme-settings :theme-colors])]
    (ui/column
     {:child-placement :top-left
      :padding 24
      :background (paint/fill "#1A0F0A")}
     
     ;; Header
     (ui/row
      {:child-placement :left
       :padding 16}
      (ui/label "🌾 GrainDisplay" 
                {:font (font/make-with-size 28)
                 :color "#DAA520"})
      (ui/label "Bedtime Warm Theme" 
                {:font (font/make-with-size 14)
                 :color "#FFF8DC"}))
     
     ;; Time and Graintime
     (time-display (:current-time state))
     
     ;; Theme Preview
     (theme-preview theme-colors)
     
     ;; Controls
     (ui/column
      {:child-placement :top-left
       :padding 16}
      
      ;; Brightness Control
      (ui/row
       {:child-placement :left}
       (ui/label "Brightness:" {:color "#FFF8DC"})
       (slider (:brightness (:display config)) 0.1 1.0 
               #(swap! app-state assoc-in [:config :display :brightness] %)))
      
      ;; Color Temperature Control
      (ui/row
       {:child-placement :left}
       (ui/label "Temperature:" {:color "#FFF8DC"})
       (slider (/ (:color-temperature (:current-theme-settings state)) 1000) 1.0 10.0
               #(set-color-temperature (* % 1000))))
      
      ;; Schedule Controls
      (ui/row
       {:child-placement :left}
       (ui/label "Auto Schedule:" {:color "#FFF8DC"})
       (ui/clickable
        {:on-click #(swap! app-state update-in [:config :schedule :enabled] not)}
        (ui/rect
         {:paint (paint/fill (if (:enabled (:schedule config)) "#D2691E" "#333333"))
          :width 60
          :height 30}
         (ui/label (if (:enabled (:schedule config)) "ON" "OFF")
                   {:color "#FFF8DC"}))))
      
      ;; Color Palette
      (ui/column
       {:child-placement :top-left}
       (ui/label "Color Palette:" {:color "#FFF8DC"})
       (ui/row
        {:child-placement :left}
        (color-button "#D2691E" #(swap! app-state assoc :current-theme :warm-red))
        (color-button "#FF8C00" #(swap! app-state assoc :current-theme :deep-orange))
        (color-button "#CC5500" #(swap! app-state assoc :current-theme :burnt-orange))
        (color-button "#8B4513" #(swap! app-state assoc :current-theme :warm-brown)))))))

;; =============================================================================
;; Main Application
;; =============================================================================

(defn start-app []
  "Start the GrainDisplay application"
  (let [window (window/make
                {:title "GrainDisplay - Bedtime Warm Theme"
                 :width 800
                 :height 600
                 :background "#1A0F0A"})]
    
    (swap! app-state assoc :window window)
    
    ;; Start time update loop
    (go-loop []
      (swap! app-state assoc :current-time (time/local-time))
      (<! (timeout 1000))
      (recur))
    
    ;; Start theme update loop
    (go-loop []
      (let [state @app-state
            config (:config state)
            current-time (:current-time state)
            location (:location config)
            sun-times (get-sun-times (:latitude location) (:longitude location))
            progress (calculate-theme-progress current-time 
                                               (:sunrise sun-times) 
                                               (:sunset sun-times))
            theme-settings (get-current-theme-settings progress config)]
        
        (swap! app-state assoc 
               :sunrise-time (:sunrise sun-times)
               :sunset-time (:sunset sun-times)
               :current-theme-settings theme-settings
               :transition-progress progress)
        
        ;; Apply settings if auto-schedule is enabled
        (when (:enabled (:schedule config))
          (set-color-temperature (:color-temperature theme-settings))
          (set-brightness (:brightness theme-settings))))
      
      (<! (timeout 60000)) ; Update every minute
      (recur))
    
    ;; Render loop
    (go-loop []
      (when-let [w (:window @app-state)]
        (window/render w (main-panel))
        (<! (timeout 16)) ; 60fps
        (recur)))))

(defn -main [& args]
  "Main entry point"
  (println "🌾 Starting GrainDisplay...")
  (start-app))
