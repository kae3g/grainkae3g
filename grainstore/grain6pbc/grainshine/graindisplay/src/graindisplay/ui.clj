(ns graindisplay.ui
  "Humble UI interface for GrainDisplay local machine settings"
  (:require [io.github.humbleui.core :as hui]
            [io.github.humbleui.ui :as ui]
            [graindisplay.metadata :as meta]
            [graindisplay.gnome :as gnome]
            [clojure.tools.logging :as log]))

;; Application State

(defonce *state
  (atom {:display-info nil
         :local-preferences {:honor-external-metadata true
                            :overrides {}
                            :allow-list []
                            :deny-list []}
         :local-defaults {:scaling 1.75
                         :color-temperature 2000
                         :color-profile :srgb
                         :brightness 0.8
                         :filters []}
         :current-settings nil}))

;; UI Components

(defn slider
  "Create a slider with label and value display"
  [label value min-val max-val step on-change]
  (ui/column
    (ui/label label)
    (ui/row
      (ui/slider {:value value
                  :min min-val
                  :max max-val
                  :step step
                  :on-change on-change})
      (ui/label (format "%.2f" value)))))

(defn checkbox
  "Create a checkbox with label"
  [label checked? on-change]
  (ui/row
    (ui/checkbox {:checked checked?
                  :on-change on-change})
    (ui/label label)))

(defn filter-checkbox
  "Create a checkbox for a color filter"
  [filter-name active-filters on-toggle]
  (checkbox
   (name filter-name)
   (contains? (set active-filters) filter-name)
   #(on-toggle filter-name %)))

;; Settings Panels

(defn basic-settings-panel
  "Panel for basic display settings (scaling, color temp, brightness)"
  [state]
  (let [{:keys [scaling color-temperature brightness]} (:local-defaults @state)]
    (ui/column
      (ui/label {:font-size 18 :font-weight :bold}
                "Basic Display Settings")
      
      (slider "Text Scaling"
              scaling
              0.5 3.0 0.25
              (fn [v]
                (swap! state assoc-in [:local-defaults :scaling] v)
                (gnome/set-text-scaling! v)))
      
      (slider "Color Temperature (Kelvin)"
              color-temperature
              1000 6500 100
              (fn [v]
                (swap! state assoc-in [:local-defaults :color-temperature] v)
                (gnome/set-night-light! {:temperature v})))
      
      (slider "Brightness"
              brightness
              0.0 1.0 0.05
              (fn [v]
                (swap! state assoc-in [:local-defaults :brightness] v))))))

(defn color-filters-panel
  "Panel for color filters and accessibility options"
  [state]
  (let [filters (:filters (:local-defaults @state))
        available-filters [:grayscale :monochrome :sepia :high-contrast
                          :reduce-blue-light :protanopia :deuteranopia :tritanopia]]
    (ui/column
      (ui/label {:font-size 18 :font-weight :bold}
                "Color Filters & Accessibility")
      
      (ui/column
        (for [filter available-filters]
          (filter-checkbox
           filter
           filters
           (fn [f checked?]
             (swap! state update-in [:local-defaults :filters]
                    (if checked?
                      #(conj (vec %) f)
                      #(vec (remove #{f} %)))))))))))

(defn metadata-honor-panel
  "Panel for controlling how external display metadata is honored"
  [state]
  (let [{:keys [honor-external-metadata allow-list deny-list]} (:local-preferences @state)]
    (ui/column
      (ui/label {:font-size 18 :font-weight :bold}
                "External Content Metadata Settings")
      
      (ui/padding 10
        (ui/column
          (checkbox
           "Honor content creator's display settings"
           honor-external-metadata
           (fn [v]
             (swap! state assoc-in [:local-preferences :honor-external-metadata] v)))
          
          (ui/label {:font-size 14 :color :gray}
                    "When enabled, Grainweb content may override your local settings")
          
          (when honor-external-metadata
            (ui/column
              (ui/label "Fields to honor (leave empty to honor all):")
              (ui/text-area {:value (pr-str allow-list)
                            :on-change (fn [v]
                                        (try
                                          (swap! state assoc-in [:local-preferences :allow-list]
                                                 (read-string v))
                                          (catch Exception e
                                            (log/warn "Invalid allow-list:" v))))})
              
              (ui/label "Fields to always ignore:")
              (ui/text-area {:value (pr-str deny-list)
                            :on-change (fn [v]
                                        (try
                                          (swap! state assoc-in [:local-preferences :deny-list]
                                                 (read-string v))
                                          (catch Exception e
                                            (log/warn "Invalid deny-list:" v))))}))))))))

(defn force-overrides-panel
  "Panel for force-override settings that always apply"
  [state]
  (let [overrides (:overrides (:local-preferences @state))]
    (ui/column
      (ui/label {:font-size 18 :font-weight :bold}
                "Force Override Settings")
      
      (ui/label {:font-size 14 :color :gray}
                "These settings ALWAYS override content metadata")
      
      (checkbox
       "Force color temperature"
       (contains? overrides :color-temperature)
       (fn [v]
         (if v
           (swap! state assoc-in [:local-preferences :overrides :color-temperature]
                  (:color-temperature (:local-defaults @state)))
           (swap! state update-in [:local-preferences :overrides]
                  dissoc :color-temperature))))
      
      (checkbox
       "Force grayscale filter"
       (contains? overrides :filters)
       (fn [v]
         (if v
           (swap! state assoc-in [:local-preferences :overrides :filters] [:grayscale])
           (swap! state update-in [:local-preferences :overrides]
                  dissoc :filters))))
      
      (checkbox
       "Force scaling"
       (contains? overrides :scaling)
       (fn [v]
         (if v
           (swap! state assoc-in [:local-preferences :overrides :scaling]
                  (:scaling (:local-defaults @state)))
           (swap! state update-in [:local-preferences :overrides]
                  dissoc :scaling)))))))

(defn display-info-panel
  "Panel showing current display information"
  [state]
  (let [info (:display-info @state)]
    (ui/column
      (ui/label {:font-size 18 :font-weight :bold}
                "Current Display Information")
      
      (when info
        (ui/column
          (ui/label (str "Display: " (:display-name info)))
          (ui/label (str "Resolution: " (str/join "x" (:resolution info))))
          (ui/label (str "Scaling: " (:scaling info) "x"))
          (ui/label (str "Night Light: "
                        (if (get-in info [:night-light :enabled]) "✅ Enabled" "❌ Disabled")))
          (when (get-in info [:night-light :enabled])
            (ui/label (str "Temperature: " (get-in info [:night-light :temperature]) "K"))))))))

(defn main-window
  "Main GrainDisplay settings window"
  []
  (ui/window
    {:title "GrainDisplay Settings"
     :width 800
     :height 900}
    
    (ui/padding 20
      (ui/column
        (ui/label {:font-size 24 :font-weight :bold}
                  "🌾 GrainDisplay Settings")
        
        (ui/label {:font-size 14 :color :gray}
                  "Universal Display Management for Grain Network")
        
        (ui/gap 20)
        
        ;; Tabs for different panels
        (ui/tabs
          {:tabs [{:title "Basic Settings"
                   :content (basic-settings-panel *state)}
                  {:title "Color Filters"
                   :content (color-filters-panel *state)}
                  {:title "External Metadata"
                   :content (metadata-honor-panel *state)}
                  {:title "Force Overrides"
                   :content (force-overrides-panel *state)}
                  {:title "Display Info"
                   :content (display-info-panel *state)}]})
        
        (ui/gap 20)
        
        ;; Action buttons
        (ui/row
          (ui/button
           {:label "Apply Settings"
            :on-click (fn []
                       (let [settings (:local-defaults @*state)]
                         (gnome/set-text-scaling! (:scaling settings))
                         (gnome/set-night-light! {:temperature (:color-temperature settings)
                                                 :enabled true})
                         (log/info "Applied settings:" settings)))}
           "Apply Settings")
          
          (ui/button
           {:label "Reset to Defaults"
            :on-click (fn []
                       (swap! *state assoc :local-defaults
                              {:scaling 1.0
                               :color-temperature 6500
                               :brightness 1.0
                               :filters []})
                       (gnome/reset-to-defaults!))}
           "Reset to Defaults")
          
          (ui/button
           {:label "Refresh Display Info"
            :on-click (fn []
                       (swap! *state assoc :display-info (gnome/get-display-info)))}
           "Refresh"))))))

(defn start-ui
  "Start the GrainDisplay UI application"
  []
  (swap! *state assoc :display-info (gnome/get-display-info))
  (hui/start
   (main-window)))

(comment
  ;; Start the UI
  (start-ui))

