#!/usr/bin/env bb

(require '[babashka.process :as p])

(println "🌙 Grain Night Light - Boot Initialization")
(println "")

;; Wait for GNOME to be fully started
(Thread/sleep 3000)

;; Enable Night Light
(p/shell "gsettings" "set" "org.gnome.settings-daemon.plugins.color" "night-light-enabled" "true")
(println "✅ Night Light enabled")

;; Set temperature to 2000K (maximum warmth)
(p/shell "gsettings" "set" "org.gnome.settings-daemon.plugins.color" "night-light-temperature" "2000")
(println "✅ Temperature: 2000K (maximum warmth)")

;; Set to manual schedule (always on)
(p/shell "gsettings" "set" "org.gnome.settings-daemon.plugins.color" "night-light-schedule-automatic" "false")
(println "✅ Schedule: Manual (always on)")

(println "")
(println "🌙 Warm lighting active at boot!")


