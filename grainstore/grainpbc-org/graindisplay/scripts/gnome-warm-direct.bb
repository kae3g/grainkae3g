#!/usr/bin/env bb
(ns gnome-warm-direct
  "Directly modify GNOME display settings for warm bedtime theme"
  (:require [clojure.java.shell :as shell]
            [clojure.string :as str]
            [babashka.fs :as fs]))

(defn log [msg]
  (println (str "🌾 " msg)))

(defn run [cmd]
  (let [result (shell/sh "bash" "-c" cmd)]
    (when-not (zero? (:exit result))
      (println "⚠️  Warning:" (:err result)))
    (:out result)))

;; =============================================================================
;; Direct Display Control
;; =============================================================================

(defn get-primary-display []
  "Get the primary display name"
  (let [output (run "xrandr --query | grep ' connected primary' | awk '{print $1}'")]
    (str/trim output)))

(defn set-display-gamma [display red green blue]
  "Set gamma correction for warm colors"
  (log (format "Setting gamma: R=%.2f G=%.2f B=%.2f" red green blue))
  (run (str "xrandr --output " display " --gamma " red ":" green ":" blue)))

(defn set-display-brightness [display brightness]
  "Set display brightness"
  (log (format "Setting brightness: %.1f%%" (* brightness 100)))
  (run (str "xrandr --output " display " --brightness " brightness)))

(defn calculate-warm-gamma [temperature]
  "Calculate RGB gamma values for given color temperature
   1000K = very warm (0.3, 0.15, 0.05)
   2000K = warm (0.5, 0.3, 0.1)
   4000K = neutral warm (0.8, 0.7, 0.5)
   6500K = daylight (1.0, 1.0, 1.0)"
  (let [temp (max 1000 (min 10000 temperature))]
    (cond
      (<= temp 1000) {:red 0.3 :green 0.15 :blue 0.05}  ; Very warm
      (<= temp 1500) {:red 0.4 :green 0.2 :blue 0.08}
      (<= temp 2000) {:red 0.5 :green 0.3 :blue 0.1}
      (<= temp 2500) {:red 0.6 :green 0.4 :blue 0.15}
      (<= temp 3000) {:red 0.7 :green 0.5 :blue 0.2}
      (<= temp 4000) {:red 0.8 :green 0.7 :blue 0.5}
      (<= temp 5000) {:red 0.9 :green 0.85 :blue 0.7}
      :else {:red 1.0 :green 1.0 :blue 1.0})))           ; Normal

;; =============================================================================
;; GNOME Settings
;; =============================================================================

(defn set-gnome-night-light [enabled]
  "Enable/disable GNOME night light"
  (run (str "gsettings set org.gnome.settings-daemon.plugins.color night-light-enabled " 
            (if enabled "true" "false"))))

(defn set-gnome-night-light-temperature [temperature]
  "Set GNOME night light temperature (1000-10000)"
  (run (str "gsettings set org.gnome.settings-daemon.plugins.color night-light-temperature " 
            (int temperature))))

(defn set-gnome-theme [theme-name]
  "Set GTK theme"
  (run (str "gsettings set org.gnome.desktop.interface gtk-theme '" theme-name "'")))

(defn set-gnome-color-scheme [scheme]
  "Set GNOME color scheme (prefer-dark, prefer-light, default)"
  (run (str "gsettings set org.gnome.desktop.interface color-scheme '" scheme "'")))

;; =============================================================================
;; Combined Warm Theme Application
;; =============================================================================

(defn apply-warm-theme [temperature brightness]
  "Apply complete warm theme using multiple methods"
  (log "Applying bedtime warm theme...")
  (log "")
  
  ;; Get primary display
  (let [display (get-primary-display)]
    (when (str/blank? display)
      (log "⚠️  Could not detect primary display, trying eDP-1")
      (def display "eDP-1"))
    
    (log (str "Primary display: " display))
    (log "")
    
    ;; Method 1: Direct xrandr gamma control (most effective)
    (log "Method 1: Direct xrandr gamma control")
    (let [gamma (calculate-warm-gamma temperature)]
      (set-display-gamma display (:red gamma) (:green gamma) (:blue gamma))
      (set-display-brightness display brightness))
    
    ;; Method 2: GNOME Night Light
    (log "Method 2: GNOME Night Light")
    (set-gnome-night-light true)
    (set-gnome-night-light-temperature temperature)
    
    ;; Method 3: Dark theme for consistency
    (log "Method 3: GNOME Dark Theme")
    (set-gnome-color-scheme "prefer-dark")
    (set-gnome-theme "Adwaita-dark")
    
    (log "")
    (log "✅ Warm theme applied!")
    (log "")
    (log (format "Temperature: %dK (bedtime warm)" temperature))
    (log (format "Brightness: %.0f%%" (* brightness 100)))
    (log (format "Display: %s" display))
    (log "")
    (log "Your screen should now be VERY warm and orange!")
    (log "")))

(defn reset-display []
  "Reset display to normal settings"
  (log "Resetting display to normal...")
  
  (let [display (get-primary-display)]
    (when (str/blank? display)
      (def display "eDP-1"))
    
    ;; Reset gamma
    (set-display-gamma display 1.0 1.0 1.0)
    (set-display-brightness display 1.0)
    
    ;; Disable night light
    (set-gnome-night-light false)
    
    ;; Reset theme
    (set-gnome-theme "Adwaita")
    
    (log "✅ Display reset to normal")))

(defn show-current-settings []
  "Show current display settings"
  (println "")
  (println "🌾 Current GrainDisplay Settings")
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
  (println "")
  
  (let [display (get-primary-display)
        night-light (str/trim (run "gsettings get org.gnome.settings-daemon.plugins.color night-light-enabled"))
        temp (str/trim (run "gsettings get org.gnome.settings-daemon.plugins.color night-light-temperature"))
        theme (str/trim (run "gsettings get org.gnome.desktop.interface gtk-theme"))
        color-scheme (str/trim (run "gsettings get org.gnome.desktop.interface color-scheme"))]
    
    (println (str "Display:      " display))
    (println (str "Night Light:  " night-light))
    (println (str "Temperature:  " temp))
    (println (str "GTK Theme:    " theme))
    (println (str "Color Scheme: " color-scheme))
    (println ""))
  
  (println "Commands:")
  (println "  bb scripts/gnome-warm-direct.bb apply    - Apply warm theme")
  (println "  bb scripts/gnome-warm-direct.bb reset    - Reset to normal")
  (println "  bb scripts/gnome-warm-direct.bb status   - Show this status")
  (println "  bb scripts/gnome-warm-direct.bb extreme  - EXTREME warm (800K)")
  (println ""))

;; =============================================================================
;; Presets
;; =============================================================================

(defn apply-preset [preset-name]
  "Apply a named preset"
  (case preset-name
    "bedtime" (apply-warm-theme 1000 0.8)
    "extreme" (apply-warm-theme 800 0.7)
    "evening" (apply-warm-theme 2000 0.85)
    "sunset" (apply-warm-theme 3000 0.9)
    "normal" (reset-display)
    (do
      (log "Unknown preset. Available: bedtime, extreme, evening, sunset, normal")
      (System/exit 1))))

;; =============================================================================
;; Main
;; =============================================================================

(defn main []
  (let [command (first *command-line-args*)]
    (case command
      "apply" (apply-warm-theme 1000 0.8)
      "extreme" (apply-warm-theme 800 0.7)
      "evening" (apply-warm-theme 2000 0.85)
      "sunset" (apply-warm-theme 3000 0.9)
      "reset" (reset-display)
      "status" (show-current-settings)
      "help" (do
               (println "🌾 GrainDisplay - Direct GNOME Warm Control")
               (println "")
               (println "USAGE:")
               (println "  bb scripts/gnome-warm-direct.bb apply    Apply bedtime warm (1000K)")
               (println "  bb scripts/gnome-warm-direct.bb extreme  EXTREME warm (800K)")
               (println "  bb scripts/gnome-warm-direct.bb evening  Evening warm (2000K)")
               (println "  bb scripts/gnome-warm-direct.bb sunset   Sunset warm (3000K)")
               (println "  bb scripts/gnome-warm-direct.bb reset    Reset to normal")
               (println "  bb scripts/gnome-warm-direct.bb status   Show current settings")
               (println ""))
      ;; Default: show status
      (show-current-settings))))

(main)
