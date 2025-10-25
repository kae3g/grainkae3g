#!/usr/bin/env bb
(ns build-simple
  "Simplified build script for GrainDisplay"
  (:require [clojure.java.shell :as shell]
            [clojure.string :as str]
            [babashka.fs :as fs]))

(defn log [msg]
  (println (str "🌾 " msg)))

(defn run [cmd]
  (log (str "Running: " cmd))
  (let [result (shell/sh "bash" "-c" cmd)]
    (when-not (zero? (:exit result))
      (println "❌ Error:" (:err result))
      (System/exit 1))
    (:out result)))

(defn apply-warm-theme-now []
  "Apply warm theme to current GNOME session using gammastep"
  (log "Applying bedtime warm theme...")
  
  ;; Kill existing gammastep instances
  (shell/sh "pkill" "gammastep")
  (Thread/sleep 500)
  
  ;; Start gammastep with warm temperature (1000K)
  (log "Starting gammastep at 1000K (bedtime warm)...")
  (shell/sh "gammastep" "-O" "1000" "-b" "0.8")
  
  (log "✅ Warm theme applied! Temperature: 1000K, Brightness: 80%")
  (println "")
  (println "🌙 Your display should now be very warm and cozy!")
  (println "")
  (println "To adjust:")
  (println "  - Warmer: gammastep -O 800")
  (println "  - Less warm: gammastep -O 2000")
  (println "  - Reset: gammastep -x")
  (println ""))

(defn create-startup-script []
  "Create autostart script for GNOME"
  (log "Creating GNOME autostart script...")
  
  (let [autostart-dir (str (System/getProperty "user.home") "/.config/autostart")
        desktop-file (str autostart-dir "/graindisplay.desktop")]
    
    (fs/create-dirs autostart-dir)
    
    (spit desktop-file
          (str/join "\n"
            ["[Desktop Entry]"
             "Type=Application"
             "Name=GrainDisplay Bedtime Warm"
             "Comment=Automatically applies warm bedtime theme"
             "Exec=gammastep -O 1000 -b 0.8"
             "Icon=preferences-desktop-display"
             "StartupNotify=false"
             "Terminal=false"
             "X-GNOME-Autostart-enabled=true"
             "X-GNOME-Autostart-Delay=5"]))
    
    (log (str "✅ Created autostart: " desktop-file))
    (println "")
    (println "GrainDisplay will now start automatically when you log in!")
    (println "")))

(defn create-toggle-script []
  "Create toggle script for quick on/off"
  (log "Creating toggle script...")
  
  (let [bin-dir (str (System/getProperty "user.home") "/.local/bin")
        script-file (str bin-dir "/graindisplay-toggle")]
    
    (fs/create-dirs bin-dir)
    
    (spit script-file
          (str/join "\n"
            ["#!/bin/bash"
             "# GrainDisplay Toggle Script"
             ""
             "if pgrep -x gammastep > /dev/null; then"
             "    echo '🌙 Disabling warm theme...'"
             "    pkill gammastep"
             "    gammastep -x"
             "    echo '✅ Normal colors restored'"
             "else"
             "    echo '🌙 Enabling warm theme...'"
             "    gammastep -O 1000 -b 0.8 &"
             "    echo '✅ Bedtime warm theme active'"
             "fi"]))
    
    (run (str "chmod +x " script-file))
    
    (log (str "✅ Created toggle: " script-file))
    (println "")
    (println "You can now toggle the theme with: graindisplay-toggle")
    (println "")))

(defn create-graintime-scheduler []
  "Create graintime-based scheduler script"
  (log "Creating graintime scheduler...")
  
  (let [bin-dir (str (System/getProperty "user.home") "/.local/bin")
        script-file (str bin-dir "/graindisplay-schedule")]
    
    (fs/create-dirs bin-dir)
    
    (spit script-file
          (str/join "\n"
            ["#!/bin/bash"
             "# GrainDisplay Graintime Scheduler"
             ""
             "# Get current hour"
             "HOUR=$(date +%H)"
             ""
             "# Sunrise (6 AM) to Sunset (6 PM) = Daylight (6500K)"
             "# Sunset (6 PM) to Sunrise (6 AM) = Bedtime (1000K)"
             ""
             "if [ $HOUR -ge 6 ] && [ $HOUR -lt 18 ]; then"
             "    # Daytime - warmer but not extreme"
             "    echo '☀️ Daytime mode (4000K)'"
             "    gammastep -O 4000 -b 1.0"
             "else"
             "    # Nighttime - very warm bedtime"
             "    echo '🌙 Bedtime mode (1000K)'"
             "    gammastep -O 1000 -b 0.8"
             "fi"]))
    
    (run (str "chmod +x " script-file))
    
    (log (str "✅ Created scheduler: " script-file))
    (println "")
    (println "Run graindisplay-schedule to apply time-based theme")
    (println "")))

(defn install-systemd-service []
  "Create systemd user service for automatic scheduling"
  (log "Creating systemd user service...")
  
  (let [systemd-dir (str (System/getProperty "user.home") "/.config/systemd/user")
        service-file (str systemd-dir "/graindisplay.service")
        timer-file (str systemd-dir "/graindisplay.timer")]
    
    (fs/create-dirs systemd-dir)
    
    ;; Create service file
    (spit service-file
          (str/join "\n"
            ["[Unit]"
             "Description=GrainDisplay Bedtime Warm Theme"
             "After=graphical-session.target"
             ""
             "[Service]"
             "Type=oneshot"
             "ExecStart=" (str (System/getProperty "user.home") "/.local/bin/graindisplay-schedule")
             ""
             "[Install]"
             "WantedBy=default.target"]))
    
    ;; Create timer file for hourly updates
    (spit timer-file
          (str/join "\n"
            ["[Unit]"
             "Description=GrainDisplay Hourly Theme Update"
             ""
             "[Timer]"
             "OnBootSec=1min"
             "OnUnitActiveSec=1h"
             ""
             "[Install]"
             "WantedBy=timers.target"]))
    
    (log "✅ Created systemd service and timer")
    (println "")
    (println "Enable automatic updates:")
    (println "  systemctl --user enable graindisplay.timer")
    (println "  systemctl --user start graindisplay.timer")
    (println "")))

(defn show-status []
  "Show current display status"
  (println "")
  (println "🌾 GrainDisplay Status")
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
  (println "")
  
  (let [gammastep-running (zero? (:exit (shell/sh "pgrep" "-x" "gammastep")))]
    (if gammastep-running
      (do
        (println "✅ GrainDisplay is ACTIVE")
        (println "🌙 Bedtime warm theme applied"))
      (do
        (println "❌ GrainDisplay is INACTIVE")
        (println "💡 Normal display colors"))))
  
  (println "")
  (println "Current time: " (str/trim (:out (shell/sh "date" "+%H:%M:%S"))))
  (println "Graintime:    " (str/trim (:out (shell/sh "gt" "now"))))
  (println "")
  (println "Commands:")
  (println "  bb scripts/build-simple.bb apply    - Apply warm theme now")
  (println "  bb scripts/build-simple.bb toggle   - Toggle theme on/off")
  (println "  bb scripts/build-simple.bb schedule - Apply time-based theme")
  (println "  bb scripts/build-simple.bb install  - Install all scripts")
  (println "  bb scripts/build-simple.bb status   - Show this status")
  (println ""))

(defn main []
  (let [command (first *command-line-args*)]
    (case command
      "apply" (apply-warm-theme-now)
      
      "toggle" (do
                 (create-toggle-script)
                 (run (str (System/getProperty "user.home") "/.local/bin/graindisplay-toggle")))
      
      "schedule" (do
                   (create-graintime-scheduler)
                   (run (str (System/getProperty "user.home") "/.local/bin/graindisplay-schedule")))
      
      "install" (do
                  (create-startup-script)
                  (create-toggle-script)
                  (create-graintime-scheduler)
                  (install-systemd-service)
                  (log "")
                  (log "✅ Installation complete!")
                  (log "")
                  (log "Apply theme now with: bb scripts/build-simple.bb apply"))
      
      "status" (show-status)
      
      "help" (do
               (println "🌾 GrainDisplay Simple Build")
               (println "")
               (println "USAGE:")
               (println "  bb scripts/build-simple.bb apply      Apply warm theme immediately")
               (println "  bb scripts/build-simple.bb toggle     Toggle theme on/off")
               (println "  bb scripts/build-simple.bb schedule   Apply time-based theme")
               (println "  bb scripts/build-simple.bb install    Install all scripts")
               (println "  bb scripts/build-simple.bb status     Show current status")
               (println "  bb scripts/build-simple.bb help       Show this help")
               (println ""))
      
      ;; Default: show status
      (show-status))))

(main)
