#!/usr/bin/env bb
;; display-config.bb - Display and Night Light Configuration
;;
;; Comprehensive display setup for Framework laptops with Ubuntu 24.04 LTS

(ns display-config
  (:require [babashka.process :refer [shell]]
            [clojure.string :as str]
            [clojure.edn :as edn]))

(defn log [message]
  (println (str "🎨 " message)))

(defn run-command [cmd & {:keys [sh]}]
  "Run a command and return output as string"
  (try
    (if sh
      (-> (shell {:out :string :sh true} cmd)
          :out
          str/trim)
      (-> (shell {:out :string} cmd)
          :out
          str/trim))
    (catch Exception e
      (log (str "Error running command: " cmd " - " (.getMessage e)))
      "")))

(defn get-current-scaling []
  "Get current text scaling factor"
  (let [scaling (run-command "gsettings get org.gnome.desktop.interface text-scaling-factor")]
    (Float/parseFloat scaling)))

(defn set-text-scaling [factor]
  "Set text scaling factor"
  (log (str "Setting text scaling to " factor "x"))
  (run-command (str "gsettings set org.gnome.desktop.interface text-scaling-factor " factor))
  (log "✅ Text scaling updated"))

(defn get-night-light-status []
  "Get current Night Light status"
  (let [enabled (run-command "gsettings get org.gnome.settings-daemon.plugins.color night-light-enabled")
        temperature-str (run-command "gsettings get org.gnome.settings-daemon.plugins.color night-light-temperature")
        temperature (try
                     (-> temperature-str
                         (str/replace "uint32 " "")
                         Integer/parseInt)
                     (catch Exception _
                       6500))]  ; Default temperature
    {:enabled (= enabled "true")
     :temperature temperature}))

(defn set-night-light [enabled temperature]
  "Configure Night Light settings"
  (log (str "Configuring Night Light - Enabled: " enabled ", Temperature: " temperature "K"))
  (run-command (str "gsettings set org.gnome.settings-daemon.plugins.color night-light-enabled " (if enabled "true" "false")))
  (when enabled
    (run-command (str "gsettings set org.gnome.settings-daemon.plugins.color night-light-temperature " temperature)))
  (log "✅ Night Light configured"))

(defn set-red-green-mode [enabled]
  "Configure red and green display mode (Wayland compatible)"
  (log (str "🔴🟢 Configuring Red/Green Mode - Enabled: " enabled))

  (if enabled
    (do
      ;; Kill existing gammastep and start with warm temperature
      (run-command "pkill gammastep" :sh true)
      (run-command "gammastep -O 2000 &" :sh true)
      (log "🔴🟢 ✅ Red/Green mode enabled - Retro terminal aesthetic")
      (log "   Using gammastep for proper Wayland compatibility"))
    (do
      ;; Reset to normal temperature
      (run-command "pkill gammastep" :sh true)
      (run-command "gammastep -O 6500 &" :sh true)
      (log "🔴🟢 ✅ Red/Green mode disabled - Normal colors restored"))))

(defn set-cyberpunk-mode [enabled]
  "Configure cyberpunk display mode (red/blue) - Wayland compatible"
  (log (str "🔴🔵 Configuring Cyberpunk Mode - Enabled: " enabled))
  
  (if enabled
    (do
      ;; Start wl-gammarelay daemon if not running
      (run-command "pgrep wl-gammarelay || wl-gammarelay &" :sh true)
      ;; Wait a moment for daemon to start
      (Thread/sleep 1000)
      ;; Set cyberpunk temperature and brightness
      (run-command "busctl --user set-property rs.wl-gammarelay / rs.wl.gammarelay Temperature q 3000" :sh true)
      (run-command "busctl --user set-property rs.wl-gammarelay / rs.wl.gammarelay Brightness d 0.9" :sh true)
      (log "🔴🔵 ✅ Cyberpunk mode enabled - Red and blue aesthetic")
      (log "   Using wl-gammarelay for proper Wayland compatibility"))
    (do
      ;; Reset to normal temperature and brightness
      (run-command "busctl --user set-property rs.wl-gammarelay / rs.wl.gammarelay Temperature q 6500" :sh true)
      (run-command "busctl --user set-property rs.wl-gammarelay / rs.wl.gammarelay Brightness d 1.0" :sh true)
      (log "🔴🔵 ✅ Cyberpunk mode disabled - Normal colors restored"))))

(defn set-monochrome-mode [enabled]
  "Configure monochrome display mode (dimmed colors for focus) - Wayland compatible"
  (log (str "⚫⚪ Configuring Monochrome Mode - Enabled: " enabled))
  
  (if enabled
    (do
      ;; Start wl-gammarelay daemon if not running
      (run-command "pgrep wl-gammarelay || wl-gammarelay &" :sh true)
      ;; Wait a moment for daemon to start
      (Thread/sleep 1000)
      ;; Set monochrome temperature and brightness
      (run-command "busctl --user set-property rs.wl-gammarelay / rs.wl.gammarelay Temperature q 4000" :sh true)
      (run-command "busctl --user set-property rs.wl-gammarelay / rs.wl.gammarelay Brightness d 0.6" :sh true)
      (log "⚫⚪ ✅ Monochrome mode enabled - Dimmed colors for focus")
      (log "   Using wl-gammarelay for proper Wayland compatibility"))
    (do
      ;; Reset to normal temperature and brightness
      (run-command "busctl --user set-property rs.wl-gammarelay / rs.wl.gammarelay Temperature q 6500" :sh true)
      (run-command "busctl --user set-property rs.wl-gammarelay / rs.wl.gammarelay Brightness d 1.0" :sh true)
      (log "⚫⚪ ✅ Monochrome mode disabled - Normal colors restored"))))

(defn install-redshift []
  "Install Redshift for additional warmth control"
  (log "Installing Redshift for enhanced color temperature control...")
  (let [result (run-command "sudo apt install -y redshift redshift-gtk")]
    (if (str/includes? result "Setting up redshift")
      (log "✅ Redshift installed successfully")
      (log "⚠️  Redshift may already be installed"))))

(defn create-redshift-config []
  "Create Redshift configuration for Framework laptops"
  (let [config-dir "/home/xy/.config"
        config-file "/home/xy/.config/redshift.conf"
        config-content "[redshift]
; Global settings
temp-day=6500
temp-night=3000
; Very warm night setting for Framework 16
temp-night=2500
; Location (San Francisco area - adjust for your location)
lat=37.7749
lon=-122.4194
; Brightness settings
brightness-day=1.0
brightness-night=0.8
; Gamma correction
gamma=0.8:0.8:0.8
; Adjustment method
adjustment-method=randr
; Screen selection
screen=0"]
    (run-command (str "mkdir -p " config-dir))
    (spit config-file config-content)
    (log "✅ Redshift configuration created at " config-file)))

(defn install-sct []
  "Install sct (set color temperature) for precise control"
  (log "Installing sct for precise color temperature control...")
  (let [result (run-command "sudo apt install -y sct")]
    (if (str/includes? result "Setting up sct")
      (log "✅ sct installed successfully")
      (log "⚠️  sct may already be installed"))))

(defn create-display-profiles []
  "Create different display profiles for different times/activities"
  (let [profiles-dir "/home/xy/.local/bin/display-profiles"
        profiles-content {"warm-coding" {:scaling 1.5 :night-light true :temperature 2500 :description "Warm coding session"}
                         "cool-coding" {:scaling 1.5 :night-light false :temperature 6500 :description "Cool daylight coding"}
                         "presentation" {:scaling 1.25 :night-light false :temperature 6500 :description "Presentation mode"}
                         "night-reading" {:scaling 1.75 :night-light true :temperature 2000 :description "Very warm night reading"}}]
    (run-command (str "mkdir -p " profiles-dir))
    (doseq [[profile-name config] profiles-content]
      (let [script-content (str "#!/bin/bash
# Display profile: " (:description config) "
echo \"🎨 Applying display profile: " profile-name "\"
gsettings set org.gnome.desktop.interface text-scaling-factor " (:scaling config) "
gsettings set org.gnome.settings-daemon.plugins.color night-light-enabled " (if (:night-light config) "true" "false") "
" (when (:night-light config) (str "gsettings set org.gnome.settings-daemon.plugins.color night-light-temperature " (:temperature config))) "
echo \"✅ Display profile applied: " profile-name "\"")]
        (spit (str profiles-dir "/" profile-name ".sh") script-content)
        (run-command (str "chmod +x " profiles-dir "/" profile-name ".sh"))))
    (log "✅ Display profiles created in " profiles-dir)))

(defn get-display-info []
  "Get current display information"
  (let [xrandr-output (run-command "xrandr")
        resolution (let [lines (str/split-lines xrandr-output)
                         current-line (first (filter #(str/includes? % "current") lines))]
                     (when current-line
                       (let [words (str/split current-line #" ")
                             res-word (first (filter #(and (str/includes? % "x") 
                                                           (re-matches #"\d+x\d+" %)) words))]
                         res-word)))
        scaling (get-current-scaling)
        night-light (get-night-light-status)]
    {:resolution resolution
     :scaling scaling
     :night-light night-light}))

(defn show-current-config []
  "Show current display configuration"
  (let [config (get-display-info)]
    (log "Current Display Configuration:")
    (println (str "  Resolution: " (:resolution config)))
    (println (str "  Text Scaling: " (:scaling config) "x"))
    (println (str "  Night Light: " (if (:enabled (:night-light config)) "Enabled" "Disabled")))
    (when (:enabled (:night-light config))
      (println (str "  Temperature: " (:temperature (:night-light config)) "K")))))

(defn apply-framework-16-optimized []
  "Apply optimized settings for Framework 16"
  (log "Applying Framework 16 optimized display settings...")
  (set-text-scaling 1.5)
  (set-night-light true 2500)  ; Very warm for comfortable coding
  (log "✅ Framework 16 optimized settings applied"))

(defn show-help []
  "Show help information"
  (println "Display Configuration Tool for Framework Laptops")
  (println "")
  (println "Usage: bb display-config <command> [options]")
  (println "")
  (println "Commands:")
  (println "  status                    Show current display configuration")
  (println "  scaling <factor>          Set text scaling factor (e.g., 1.5)")
  (println "  night-light <temp>        Set Night Light temperature (e.g., 2500)")
  (println "  disable-night-light       Disable Night Light")
  (println "  red-green-mode            Enable red/green display mode")
  (println "  cyberpunk-mode            Enable cyberpunk (red/blue) display mode")
  (println "  monochrome-mode           Enable monochrome display mode")
  (println "  normal-colors             Reset to normal colors")
  (println "  install-tools            Install Redshift and sct")
  (println "  create-profiles          Create display profiles")
  (println "  framework-16-optimized   Apply Framework 16 optimized settings")
  (println "  help                     Show this help")
  (println "")
  (println "Examples:")
  (println "  bb display-config status")
  (println "  bb display-config scaling 1.75")
  (println "  bb display-config night-light 2000")
  (println "  bb display-config red-green-mode")
  (println "  bb display-config cyberpunk-mode")
  (println "  bb display-config monochrome-mode")
  (println "  bb display-config normal-colors")
  (println "  bb display-config framework-16-optimized"))

(defn main [& args]
  "Main function"
  (case (first args)
    "status" (show-current-config)
    "scaling" (set-text-scaling (Float/parseFloat (second args)))
    "night-light" (set-night-light true (Integer/parseInt (second args)))
    "disable-night-light" (set-night-light false 6500)
    "red-green-mode" (set-red-green-mode true)
    "cyberpunk-mode" (set-cyberpunk-mode true)
    "monochrome-mode" (set-monochrome-mode true)
    "normal-colors" (do (set-red-green-mode false) 
                       (set-cyberpunk-mode false) 
                       (set-monochrome-mode false))
    "install-tools" (do (install-redshift) (create-redshift-config) (install-sct))
    "create-profiles" (create-display-profiles)
    "framework-16-optimized" (apply-framework-16-optimized)
    "help" (show-help)
    (show-help)))

;; Run if called directly
(when (= *file* (System/getProperty "babashka.file"))
  (apply main *command-line-args*))
