#!/usr/bin/env bb
;; desktop-switcher.bb - Desktop Environment Switcher
;;
;; Easy switching between GNOME and Sway for personal sovereignty

(ns desktop-switcher
  (:require [babashka.process :refer [shell]]
            [babashka.fs :as fs]
            [clojure.string :as str]))

(defn log [message]
  (println (str "🖥️  " message)))

(defn error [message]
  (println (str "❌ ERROR: " message))
  (System/exit 1))

(defn run-command [cmd & {:keys [sh] :or {sh false}}]
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

(defn get-current-desktop []
  "Get current desktop environment"
  (let [session (run-command "echo $XDG_CURRENT_DESKTOP")
        wayland (run-command "echo $XDG_SESSION_TYPE")]
    (cond
      (str/includes? session "GNOME") "gnome"
      (str/includes? session "sway") "sway"
      (str/includes? wayland "wayland") "wayland"
      :else "unknown")))

(defn check-desktop-available [desktop]
  "Check if desktop environment is available"
  (case desktop
    "gnome" (run-command "which gnome-session")
    "sway" (run-command "which sway")
    "both" (and (run-command "which gnome-session")
                (run-command "which sway"))
    false))

(defn switch-to-gnome []
  "Switch to GNOME desktop"
  (log "Switching to GNOME...")
  (log "Starting GNOME session...")
  (run-command "gnome-session" :sh true)
  (log "✅ GNOME session started"))

(defn switch-to-sway []
  "Switch to Sway desktop"
  (log "Switching to Sway...")
  (log "Starting Sway session...")
  (run-command "sway" :sh true)
  (log "✅ Sway session started"))

(defn restart-display-server []
  "Restart display server (logout/login)"
  (log "Restarting display server...")
  (log "This will log you out and back in")
  (run-command "pkill -SIGTERM gdm" :sh true)
  (log "✅ Display server restart initiated"))

(defn show-status []
  "Show current desktop status"
  (let [current (get-current-desktop)
        gnome-available (check-desktop-available "gnome")
        sway-available (check-desktop-available "sway")]
    (log "Desktop Environment Status:")
    (log (str "  Current: " current))
    (log (str "  GNOME available: " (if gnome-available "✅" "❌")))
    (log (str "  Sway available: " (if sway-available "✅" "❌")))
    (log "")
    (log "Quick switch commands:")
    (log "  bb desktop-switcher gnome  - Switch to GNOME")
    (log "  bb desktop-switcher sway   - Switch to Sway")
    (log "  bb desktop-switcher restart - Restart display server")))

(defn create-desktop-shortcuts []
  "Create desktop shortcuts for easy switching"
  (let [desktop-dir (str (System/getProperty "user.home") "/Desktop")]
    (when (fs/exists? desktop-dir)
      ;; GNOME shortcut
      (spit (str desktop-dir "/Switch-to-GNOME.desktop")
            "[Desktop Entry]
Version=1.0
Type=Application
Name=Switch to GNOME
Comment=Switch to GNOME desktop environment
Exec=bb /home/xy/kae3g/12025-10/scripts/desktop-switcher.bb gnome
Icon=gnome
Terminal=false
Categories=System;")
      
      ;; Sway shortcut
      (spit (str desktop-dir "/Switch-to-Sway.desktop")
            "[Desktop Entry]
Version=1.0
Type=Application
Name=Switch to Sway
Comment=Switch to Sway desktop environment
Exec=bb /home/xy/kae3g/12025-10/scripts/desktop-switcher.bb sway
Icon=sway
Terminal=false
Categories=System;")
      
      (log "✅ Desktop shortcuts created"))))

(defn show-help []
  "Show help information"
  (println "Desktop Environment Switcher")
  (println "")
  (println "Usage: bb desktop-switcher <command>")
  (println "")
  (println "Commands:")
  (println "  gnome                    Switch to GNOME")
  (println "  sway                     Switch to Sway")
  (println "  status                   Show current status")
  (println "  restart                  Restart display server")
  (println "  create-shortcuts         Create desktop shortcuts")
  (println "  help                     Show this help")
  (println "")
  (println "Examples:")
  (println "  bb desktop-switcher status")
  (println "  bb desktop-switcher gnome")
  (println "  bb desktop-switcher sway")
  (println "  bb desktop-switcher restart"))

(defn main [& args]
  "Main function"
  (case (first args)
    "gnome" (switch-to-gnome)
    "sway" (switch-to-sway)
    "status" (show-status)
    "restart" (restart-display-server)
    "create-shortcuts" (create-desktop-shortcuts)
    "help" (show-help)
    (show-help)))

;; Run if called directly
(when (= *file* (System/getProperty "babashka.file"))
  (apply main *command-line-args*))





