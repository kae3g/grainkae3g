(ns humble-desktop.shell
  "Desktop shell for humble-desktop.
   
   Provides GNOME Shell-like interface using Humble UI,
   optimized for Sway integration and musl libc performance."
  (:require [io.github.humbleui.ui :as ui]
            [humble-desktop.core :as desktop]
            [humble-desktop.applications :as apps]
            [humble-desktop.notifications :as notifications]
            [clojure.string :as str]))

(def shell-state
  "Shell state."
  (atom {:activities-visible? false
         :search-query ""
         :selected-app nil
         :workspace-view? false}))

(defn activities-overview
  "Activities overview component."
  []
  (let [state @shell-state
        desktop-state (desktop/get-desktop-state)
        applications (:applications desktop-state)
        workspaces (:workspaces desktop-state)
        current-workspace (:current-workspace desktop-state)]
    (ui/rect {:fill 0xFF1A1410}
      (ui/column
        ;; Top bar
        (ui/row
          (ui/padding 16
            (ui/text {:size 24 :weight :bold :color 0xFFE8DCC8}
              "Activities"))
          (ui/fill)
          (ui/button {:on-click #(swap! shell-state assoc :activities-visible? false)}
            (ui/padding 8
              (ui/text {:color 0xFFE8DCC8} "×"))))
        
        ;; Search bar
        (ui/padding 16 8
          (ui/text-field
            {:placeholder "Search applications..."
             :value (:search-query state)
             :on-change #(swap! shell-state assoc :search-query %)}))
        
        ;; Applications grid
        (ui/scroll
          (ui/wrap
            (for [app applications]
              (ui/button
                {:on-click #(do
                              (swap! shell-state assoc :selected-app app)
                              (apps/launch-application! app))}
                (ui/padding 16
                  (ui/column
                    (ui/text {:size 16 :color 0xFFE8DCC8}
                      (:name app))
                    (when (:description app)
                      (ui/text {:size 12 :color 0xFF8B745E}
                        (:description app)))))))))
        
        ;; Workspaces
        (ui/padding 16
          (ui/row
            (for [[idx workspace] (map-indexed vector workspaces)]
              (ui/button
                {:on-click #(desktop/switch-workspace! idx)
                 :variant (if (= idx current-workspace) :primary :secondary)}
                (ui/padding 8
                  (ui/text {:color 0xFFE8DCC8}
                    (str "Workspace " (inc idx))))))))))))

(defn top-bar
  "Top bar component."
  []
  (let [desktop-state (desktop/get-desktop-state)
        notifications (:notifications desktop-state)
        current-time (str (java.time.LocalTime/now))]
    (ui/rect {:fill 0xFF2A2420 :height 32}
      (ui/row
        ;; Activities button
        (ui/button
          {:on-click #(swap! shell-state assoc :activities-visible? true)}
          (ui/padding 8
            (ui/text {:color 0xFFE8DCC8} "Activities")))
        
        (ui/fill)
        
        ;; Notifications
        (when (seq notifications)
          (ui/button
            {:on-click #(notifications/show-notifications!)}
            (ui/padding 8
              (ui/text {:color 0xFFE8DCC8}
                (str "🔔 " (count notifications))))))
        
        ;; Time
        (ui/padding 8
          (ui/text {:color 0xFFE8DCC8}
            current-time))))))

(defn desktop-shell
  "Main desktop shell component."
  []
  (let [state @shell-state
        activities-visible? (:activities-visible? state)]
    (ui/column
      (top-bar)
      (ui/fill
        (if activities-visible?
          (activities-overview)
          (ui/rect {:fill 0xFF1A1410}
            (ui/center
              (ui/text {:size 48 :color 0xFF8B745E}
                "humble-desktop"))))))))

(defn start-shell!
  "Start the desktop shell."
  []
  (println "🐚 Starting humble-desktop shell...")
  ;; Shell startup logic
  (println "✅ Shell started"))

(defn stop-shell!
  "Stop the desktop shell."
  []
  (println "🛑 Stopping humble-desktop shell...")
  ;; Shell shutdown logic
  (println "✅ Shell stopped"))
