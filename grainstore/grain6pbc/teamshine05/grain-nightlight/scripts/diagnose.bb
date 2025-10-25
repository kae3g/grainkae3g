#!/usr/bin/env bb

(require '[babashka.process :as p]
         '[clojure.string :as str])

(println "🔍 Grain Night Light Diagnostic")
(println "═══════════════════════════════════════════════════")
(println "")

;; 1. Check GNOME version
(println "1️⃣ GNOME Version:")
(let [result (p/shell {:out :string :continue true} "gnome-shell" "--version")]
  (if (= 0 (:exit result))
    (println (str "   " (str/trim (:out result))))
    (println "   ❌ GNOME Shell not found")))
(println "")

;; 2. Check all Night Light settings
(println "2️⃣ Night Light Settings:")
(println (str "   Enabled: " (str/trim (:out (p/shell {:out :string} "gsettings" "get" "org.gnome.settings-daemon.plugins.color" "night-light-enabled")))))
(println (str "   Temperature: " (str/trim (:out (p/shell {:out :string} "gsettings" "get" "org.gnome.settings-daemon.plugins.color" "night-light-temperature")))))
(println (str "   Schedule Auto: " (str/trim (:out (p/shell {:out :string} "gsettings" "get" "org.gnome.settings-daemon.plugins.color" "night-light-schedule-automatic")))))
(println (str "   Schedule From: " (str/trim (:out (p/shell {:out :string} "gsettings" "get" "org.gnome.settings-daemon.plugins.color" "night-light-schedule-from")))))
(println (str "   Schedule To: " (str/trim (:out (p/shell {:out :string} "gsettings" "get" "org.gnome.settings-daemon.plugins.color" "night-light-schedule-to")))))
(println "")

;; 3. Check if settings daemon is running
(println "3️⃣ Settings Daemon Status:")
(let [result (p/shell {:out :string :continue true} "pgrep" "-f" "gsd-color")]
  (if (= 0 (:exit result))
    (println (str "   ✅ gsd-color daemon running (PID: " (str/trim (:out result)) ")"))
    (println "   ❌ gsd-color daemon not running")))
(println "")

;; 4. Check display color profile
(println "4️⃣ Display Color Profile:")
(let [result (p/shell {:out :string :continue true} "gsettings" "get" "org.gnome.settings-daemon.plugins.color" "recalibrate-display-threshold")]
  (if (= 0 (:exit result))
    (println (str "   Threshold: " (str/trim (:out result))))
    (println "   N/A")))
(println "")

;; 5. Restart color daemon
(println "5️⃣ Attempting to restart color daemon...")
(p/shell {:continue true} "pkill" "-HUP" "gsd-color")
(Thread/sleep 2000)
(println "   ✅ Sent HUP signal to gsd-color")
(println "")

;; 6. Re-apply settings
(println "6️⃣ Re-applying Night Light settings...")
(p/shell "gsettings" "set" "org.gnome.settings-daemon.plugins.color" "night-light-enabled" "false")
(Thread/sleep 500)
(p/shell "gsettings" "set" "org.gnome.settings-daemon.plugins.color" "night-light-temperature" "2000")
(p/shell "gsettings" "set" "org.gnome.settings-daemon.plugins.color" "night-light-schedule-automatic" "false")
(p/shell "gsettings" "set" "org.gnome.settings-daemon.plugins.color" "night-light-enabled" "true")
(println "   ✅ Settings re-applied")
(println "")

(println "═══════════════════════════════════════════════════")
(println "")
(println "🌙 Your screen should now have a warm orange tint at 2000K.")
(println "")
(println "If you still don't see it:")
(println "  1. Open GNOME Settings → Displays → Night Light")
(println "  2. Toggle Night Light off and on")
(println "  3. Move the temperature slider all the way left")
(println "  4. Or restart GNOME Shell: Alt+F2, type 'r', press Enter")
(println "")


