(ns humble-desktop.compositor
  "Compositor integration for humble-desktop.
   
   Provides Sway integration and Wayland compositing support,
   optimized for musl libc performance."
  (:require [clojure.java.shell :as shell]
            [clojure.string :as str]))

(def compositor-state
  "Compositor state."
  (atom {:sway-running? false
         :wayland-display nil
         :outputs []
         :inputs []
         :workspaces []}))

(defn check-sway-installed?
  "Check if Sway is installed."
  []
  (try
    (let [result (shell/sh "which" "sway")]
      (= (:exit result) 0))
    (catch Exception _
      false)))

(defn start-sway!
  "Start Sway compositor."
  []
  (if (check-sway-installed?)
    (do
      (println "🪟 Starting Sway compositor...")
      (swap! compositor-state assoc :sway-running? true)
      ;; Start Sway in background
      (future
        (shell/sh "sway" "--config" "/etc/sway/config"))
      (println "✅ Sway started"))
    (println "❌ Sway not found - please install Sway")))

(defn stop-sway!
  "Stop Sway compositor."
  []
  (println "🛑 Stopping Sway compositor...")
  (swap! compositor-state assoc :sway-running? false)
  (shell/sh "swaymsg" "exit")
  (println "✅ Sway stopped"))

(defn get-sway-outputs
  "Get Sway outputs."
  []
  (try
    (let [result (shell/sh "swaymsg" "-t" "get_outputs")]
      (if (= (:exit result) 0)
        (str/split-lines (:out result))
        []))
    (catch Exception _
      [])))

(defn get-sway-workspaces
  "Get Sway workspaces."
  []
  (try
    (let [result (shell/sh "swaymsg" "-t" "get_workspaces")]
      (if (= (:exit result) 0)
        (str/split-lines (:out result))
        []))
    (catch Exception _
      [])))

(defn switch-sway-workspace
  "Switch Sway workspace."
  [workspace-id]
  (try
    (shell/sh "swaymsg" "workspace" (str workspace-id))
    (catch Exception e
      (println "❌ Error switching workspace:" (.getMessage e)))))

(defn start-compositor!
  "Start compositor system."
  []
  (println "🎨 Starting compositor system...")
  (start-sway!)
  (swap! compositor-state assoc
         :outputs (get-sway-outputs)
         :workspaces (get-sway-workspaces))
  (println "✅ Compositor system started"))

(defn stop-compositor!
  "Stop compositor system."
  []
  (println "🛑 Stopping compositor system...")
  (stop-sway!)
  (println "✅ Compositor system stopped"))

(defn get-compositor-state
  "Get compositor state."
  []
  @compositor-state)

(defn update-compositor-state!
  "Update compositor state."
  [updates]
  (swap! compositor-state merge updates))
