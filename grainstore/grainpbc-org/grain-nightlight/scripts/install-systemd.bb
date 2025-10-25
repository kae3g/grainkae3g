#!/usr/bin/env bb

(require '[babashka.fs :as fs]
         '[babashka.process :as p])

(println "🌙 Installing Grain Night Light Systemd Service")
(println "═══════════════════════════════════════════════════")
(println "")

(let [home (System/getenv "HOME")
      systemd-dir (str home "/.config/systemd/user")
      service-file (str systemd-dir "/grain-nightlight.service")
      repo-path (str home "/kae3g/grainkae3g/grainstore/grain-nightlight")]
  
  ;; 1. Create systemd user directory
  (println "1️⃣ Creating systemd user directory...")
  (fs/create-dirs systemd-dir)
  (println (str "   ✅ " systemd-dir))
  (println "")
  
  ;; 2. Copy service file
  (println "2️⃣ Installing service file...")
  (fs/copy "systemd/grain-nightlight.service" service-file {:replace-existing true})
  (println (str "   ✅ " service-file))
  (println "")
  
  ;; 3. Reload systemd
  (println "3️⃣ Reloading systemd...")
  (p/shell "systemctl" "--user" "daemon-reload")
  (println "   ✅ Systemd reloaded")
  (println "")
  
  ;; 4. Enable service
  (println "4️⃣ Enabling auto-start on boot...")
  (p/shell "systemctl" "--user" "enable" "grain-nightlight.service")
  (println "   ✅ Service enabled")
  (println "")
  
  ;; 5. Start service now
  (println "5️⃣ Starting service...")
  (p/shell "systemctl" "--user" "start" "grain-nightlight.service")
  (println "   ✅ Service started")
  (println "")
  
  ;; 6. Check status
  (println "6️⃣ Checking service status...")
  (let [result (p/shell {:out :string :continue true} "systemctl" "--user" "is-active" "grain-nightlight.service")]
    (if (= "active\n" (:out result))
      (println "   ✅ Service is active")
      (println "   ⚠️  Service may need troubleshooting")))
  (println ""))

(println "═══════════════════════════════════════════════════")
(println "")
(println "✨ Installation complete!")
(println "")
(println "🌙 Warm lighting (2000K) will now auto-start on every boot.")
(println "")
(println "Commands:")
(println "  Status:  systemctl --user status grain-nightlight.service")
(println "  Stop:    systemctl --user stop grain-nightlight.service")
(println "  Disable: systemctl --user disable grain-nightlight.service")
(println "  Logs:    journalctl --user -u grain-nightlight.service")
(println "")


