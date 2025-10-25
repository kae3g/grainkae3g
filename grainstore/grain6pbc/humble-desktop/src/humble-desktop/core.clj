(ns humble-desktop.core
  "Main desktop environment for humble-desktop.
   
   Provides GNOME-like desktop experience using Clojure and Humble UI,
   optimized for musl libc and Sway integration."
  (:require [io.github.humbleui.ui :as ui]
            [humble-desktop.shell :as shell]
            [humble-desktop.compositor :as compositor]
            [humble-desktop.applications :as apps]
            [humble-desktop.settings :as settings]
            [humble-desktop.notifications :as notifications]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(def desktop-state
  "Global desktop state."
  (atom {:running? false
         :applications []
         :workspaces []
         :current-workspace 0
         :notifications []
         :settings {}
         :theme :dark}))

(defn start-desktop!
  "Start the humble-desktop environment."
  []
  (println "🖥️ Starting humble-desktop...")
  (swap! desktop-state assoc :running? true)
  (compositor/start-compositor!)
  (shell/start-shell!)
  (apps/start-application-manager!)
  (notifications/start-notification-system!)
  (settings/load-settings!)
  (println "✅ humble-desktop started successfully"))

(defn stop-desktop!
  "Stop the humble-desktop environment."
  []
  (println "🛑 Stopping humble-desktop...")
  (swap! desktop-state assoc :running? false)
  (compositor/stop-compositor!)
  (shell/stop-shell!)
  (apps/stop-application-manager!)
  (notifications/stop-notification-system!)
  (settings/save-settings!)
  (println "✅ humble-desktop stopped"))

(defn restart-desktop!
  "Restart the humble-desktop environment."
  []
  (stop-desktop!)
  (Thread/sleep 1000)
  (start-desktop!))

(defn get-desktop-state
  "Get current desktop state."
  []
  @desktop-state)

(defn update-desktop-state!
  "Update desktop state."
  [updates]
  (swap! desktop-state merge updates))

(defn add-application!
  "Add application to desktop."
  [app]
  (update-desktop-state! 
    {:applications (conj (:applications @desktop-state) app)}))

(defn remove-application!
  "Remove application from desktop."
  [app-id]
  (update-desktop-state!
    {:applications (remove #(= (:id %) app-id) (:applications @desktop-state))}))

(defn switch-workspace!
  "Switch to workspace."
  [workspace-id]
  (update-desktop-state! {:current-workspace workspace-id}))

(defn add-notification!
  "Add notification to desktop."
  [notification]
  (update-desktop-state!
    {:notifications (conj (:notifications @desktop-state) notification)}))

(defn remove-notification!
  "Remove notification from desktop."
  [notification-id]
  (update-desktop-state!
    {:notifications (remove #(= (:id %) notification-id) (:notifications @desktop-state))}))

(defn update-settings!
  "Update desktop settings."
  [settings]
  (update-desktop-state! {:settings (merge (:settings @desktop-state) settings)}))

(defn set-theme!
  "Set desktop theme."
  [theme]
  (update-desktop-state! {:theme theme}))

(defn -main
  "Main entry point for humble-desktop."
  [& args]
  (try
    (start-desktop!)
    (ui/start-app!
      (ui/window
        {:title "humble-desktop"
         :width 1920
         :height 1080
         :on-close #(stop-desktop!)}
        #'shell/desktop-shell))
    (catch Exception e
      (println "❌ Error starting humble-desktop:" (.getMessage e))
      (System/exit 1))))
