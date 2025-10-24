#!/usr/bin/env bb

;; Framework 16 Keyboard Mapping Setup
;; Configures hardware keys for volume (F1-F3) and brightness (F7-F8)
;; Integrates with Sway and dual WiFi manager

(require '[clojure.java.shell :as shell]
         '[clojure.string :as str]
         '[babashka.fs :as fs])

(defn log [message]
  "Log with timestamp"
  (let [timestamp (java.time.LocalDateTime/now)
        formatted-time (.format timestamp (java.time.format.DateTimeFormatter/ofPattern "HH:mm:ss"))]
    (println (str "[" formatted-time "] ⌨️ " message))))

(defn run-command [cmd & {:keys [sh]}]
  "Run command with optional shell execution"
  (try
    (let [result (if sh
                   (shell/sh "bash" "-c" cmd)
                   (shell/sh cmd))]
      (if (= (:exit result) 0)
        (str/trim (:out result))
        (do
          (log (str "Command failed: " cmd " - " (:err result)))
          "")))
    (catch Exception e
      (log (str "Error running command: " cmd " - " (.getMessage e)))
      "")))

(defn detect-framework16-keys []
  "Detect Framework 16 specific key mappings"
  (log "🔍 Detecting Framework 16 hardware keys...")
  
  (let [key-mappings
        {:volume-mute "XF86AudioMute"
         :volume-down "XF86AudioLowerVolume" 
         :volume-up "XF86AudioRaiseVolume"
         :brightness-down "XF86MonBrightnessDown"
         :brightness-up "XF86MonBrightnessUp"
         :wifi-toggle "XF86WLAN"
         :bluetooth-toggle "XF86Bluetooth"
         :airplane-mode "XF86RFKill"}]
    
    (log "✅ Framework 16 key mappings detected:")
    (doseq [[key-name key-sym] key-mappings]
      (log (str "   " (name key-name) " → " key-sym)))
    
    key-mappings))

(defn create-sway-keybindings []
  "Create Sway keybindings for Framework 16"
  (log "📝 Creating Sway keybindings...")
  
  (let [sway-config
        (str
         "### Framework 16 Hardware Key Mappings\n"
         "### Volume Controls (F1-F3)\n"
         "bindsym XF86AudioMute exec bb " (fs/cwd) "/scripts/framework16-volume.bb mute\n"
         "bindsym XF86AudioLowerVolume exec bb " (fs/cwd) "/scripts/framework16-volume.bb down\n"
         "bindsym XF86AudioRaiseVolume exec bb " (fs/cwd) "/scripts/framework16-volume.bb up\n"
         "\n"
         "### Brightness Controls (F7-F8)\n"
         "bindsym XF86MonBrightnessDown exec bb " (fs/cwd) "/scripts/framework16-brightness.bb down\n"
         "bindsym XF86MonBrightnessUp exec bb " (fs/cwd) "/scripts/framework16-brightness.bb up\n"
         "\n"
         "### WiFi Controls (F9-F12)\n"
         "bindsym XF86WLAN exec bb " (fs/cwd) "/scripts/dual-wifi-manager.bb status\n"
         "bindsym XF86Bluetooth exec bb " (fs/cwd) "/scripts/dual-wifi-manager.bb switch-starlink\n"
         "bindsym XF86RFKill exec bb " (fs/cwd) "/scripts/dual-wifi-manager.bb switch-cellular\n"
         "\n"
         "### Additional Framework 16 Keys\n"
         "bindsym XF86TouchpadToggle exec bb " (fs/cwd) "/scripts/framework16-touchpad.bb toggle\n"
         "bindsym XF86Display exec bb " (fs/cwd) "/scripts/framework16-display.bb cycle\n"
         "\n"
         "### Dual WiFi Manager Integration\n"
         "bindsym $mod+Shift+w exec bb " (fs/cwd) "/scripts/dual-wifi-manager.bb start\n"
         "bindsym $mod+Alt+w exec bb " (fs/cwd) "/scripts/dual-wifi-manager.bb status\n"
         "bindsym $mod+Control+w exec bb " (fs/cwd) "/scripts/dual-wifi-manager.bb switch-starlink\n"
         "bindsym $mod+Shift+Control+w exec bb " (fs/cwd) "/scripts/dual-wifi-manager.bb switch-cellular\n")]
    
    (spit "configs/sway/framework16-keys" sway-config)
    (log "✅ Sway keybindings created")))

(defn create-touchpad-script []
  "Create touchpad toggle script"
  (log "📝 Creating touchpad control script...")
  
  (let [touchpad-script
        "#!/usr/bin/env bb

;; Framework 16 Touchpad Control Script

(require '[clojure.java.shell :as shell]
         '[clojure.string :as str])

(defn log [message]
  (let [timestamp (java.time.LocalDateTime/now)
        formatted-time (.format timestamp (java.time.format.DateTimeFormatter/ofPattern \"HH:mm:ss\"))]
    (println (str \"[\" formatted-time \"] 🖱️ \" message))))

(defn run-command [cmd & {:keys [sh]}]
  (try
    (let [result (if sh
                   (shell/sh \"bash\" \"-c\" cmd)
                   (shell/sh cmd))]
      (if (= (:exit result) 0)
        (str/trim (:out result))
        \"\"))
    (catch Exception e
      (log (str \"Error running command: \" cmd \" - \" (.getMessage e)))
      \"\")))

(defn get-touchpad-status []
  \"Get current touchpad status\"
  (let [result (run-command \"xinput list-props 'SynPS/2 Synaptics TouchPad' | grep 'Device Enabled' | grep -o '[0-9]*$'\" :sh true)]
    (if (str/blank? result)
      true
      (= \"1\" result))))

(defn toggle-touchpad []
  \"Toggle touchpad on/off\"
  (let [enabled (get-touchpad-status)]
    (run-command (str \"xinput set-prop 'SynPS/2 Synaptics TouchPad' 'Device Enabled' \" (if enabled \"0\" \"1\")) :sh true)
    (log (str \"Touchpad \" (if enabled \"disabled\" \"enabled\")))
    (run-command (str \"notify-send -t 2000 'Touchpad' '\" (if enabled \"Disabled\" \"Enabled\") \"'\") :sh true)))

(defn main []
  (let [args *command-line-args*]
    (case (first args)
      \"toggle\" (toggle-touchpad)
      \"status\" (log (str \"Touchpad: \" (if (get-touchpad-status) \"Enabled\" \"Disabled\")))
      (do
        (log \"🖱️ Framework 16 Touchpad Control\")
        (log \"\")
        (log \"Usage:\")
        (log \"  bb framework16-touchpad.bb toggle  # Toggle touchpad\")
        (log \"  bb framework16-touchpad.bb status  # Show status\")))))

(main)"]
    
    (spit "scripts/framework16-touchpad.bb" touchpad-script)
    (run-command "chmod +x scripts/framework16-touchpad.bb" :sh true)
    (log "✅ Touchpad script created")))

(defn create-display-script []
  "Create display cycling script"
  (log "📝 Creating display control script...")
  
  (let [display-script
        "#!/usr/bin/env bb

;; Framework 16 Display Control Script

(require '[clojure.java.shell :as shell]
         '[clojure.string :as str])

(defn log [message]
  (let [timestamp (java.time.LocalDateTime/now)
        formatted-time (.format timestamp (java.time.format.DateTimeFormatter/ofPattern \"HH:mm:ss\"))]
    (println (str \"[\" formatted-time \"] 🖥️ \" message))))

(defn run-command [cmd & {:keys [sh]}]
  (try
    (let [result (if sh
                   (shell/sh \"bash\" \"-c\" cmd)
                   (shell/sh cmd))]
      (if (= (:exit result) 0)
        (str/trim (:out result))
        \"\"))
    (catch Exception e
      (log (str \"Error running command: \" cmd \" - \" (.getMessage e)))
      \"\")))

(defn get-displays []
  \"Get available displays\"
  (let [result (run-command \"xrandr --query | grep ' connected' | cut -d' ' -f1\" :sh true)]
    (str/split-lines result)))

(defn cycle-display []
  \"Cycle through display configurations\"
  (let [displays (get-displays)
        laptop-display (first displays)
        external-displays (rest displays)]
    
    (if (empty? external-displays)
      (do
        (log \"No external displays found\")
        (run-command \"notify-send -t 2000 'Display' 'No external displays'\" :sh true))
      (do
        (log (str \"Cycling display configuration...\"))
        (run-command (str \"xrandr --output \" laptop-display \" --auto --output \" (first external-displays) \" --auto --right-of \" laptop-display) :sh true)
        (log (str \"Display configuration updated\"))
        (run-command \"notify-send -t 2000 'Display' 'Configuration updated'\" :sh true)))))

(defn main []
  (let [args *command-line-args*]
    (case (first args)
      \"cycle\" (cycle-display)
      \"list\" (do
                (log \"Available displays:\")
                (doseq [display (get-displays)]
                  (log (str \"  \" display))))
      (do
        (log \"🖥️ Framework 16 Display Control\")
        (log \"\")
        (log \"Usage:\")
        (log \"  bb framework16-display.bb cycle  # Cycle display config\")
        (log \"  bb framework16-display.bb list   # List displays\")))))

(main)"]
    
    (spit "scripts/framework16-display.bb" display-script)
    (run-command "chmod +x scripts/framework16-display.bb" :sh true)
    (log "✅ Display script created")))

(defn update-sway-config []
  "Update main Sway config to include Framework 16 keys"
  (log "📝 Updating Sway configuration...")
  
  (let [config-file "configs/sway/config"
        current-config (if (fs/exists? config-file)
                         (slurp config-file)
                         "")
        framework16-include "include framework16-keys\n"
        updated-config (if (str/includes? current-config "include framework16-keys")
                         current-config
                         (str current-config "\n" framework16-include))]
    
    (spit config-file updated-config)
    (log "✅ Sway config updated")))

(defn create-systemd-service []
  "Create systemd service for dual WiFi manager"
  (log "📝 Creating systemd service...")
  
  (let [service-content
        (str
         "[Unit]\n"
         "Description=Dual WiFi Manager - Framework 16\n"
         "After=network.target\n"
         "\n"
         "[Service]\n"
         "Type=simple\n"
         "User=" (System/getProperty "user.name") "\n"
         "WorkingDirectory=" (fs/cwd) "\n"
         "ExecStart=/usr/bin/bb " (fs/cwd) "/scripts/dual-wifi-manager.bb start\n"
         "Restart=always\n"
         "RestartSec=5\n"
         "\n"
         "[Install]\n"
         "WantedBy=multi-user.target\n")]
    
    (spit "dual-wifi-manager.service" service-content)
    (log "✅ Systemd service created")))

(defn install-dependencies []
  "Install required dependencies"
  (log "📦 Installing dependencies...")
  
  (let [deps ["pulseaudio-utils" "brightnessctl" "xinput" "xrandr" "notify-send"]]
    (doseq [dep deps]
      (log (str "Installing " dep "..."))
      (run-command (str "sudo apt install -y " dep) :sh true))))

(defn test-keybindings []
  "Test keybindings"
  (log "🧪 Testing keybindings...")
  
  (log "Testing volume controls...")
  (run-command "bb scripts/framework16-volume.bb status" :sh true)
  
  (log "Testing brightness controls...")
  (run-command "bb scripts/framework16-brightness.bb status" :sh true)
  
  (log "Testing touchpad controls...")
  (run-command "bb scripts/framework16-touchpad.bb status" :sh true)
  
  (log "Testing display controls...")
  (run-command "bb scripts/framework16-display.bb list" :sh true))

(defn main []
  "Main setup function"
  (log "🚀 Setting up Framework 16 Keyboard Mapping")
  (log "   Volume: F1 (mute), F2 (down), F3 (up)")
  (log "   Brightness: F7 (down), F8 (up)")
  (log "   WiFi: F9-F12 (dual WiFi manager)")
  
  (try
    (install-dependencies)
    (detect-framework16-keys)
    (create-sway-keybindings)
    (create-touchpad-script)
    (create-display-script)
    (update-sway-config)
    (create-systemd-service)
    (test-keybindings)
    
    (log "")
    (log "✅ Framework 16 keyboard mapping setup complete!")
    (log "")
    (log "🎮 Hardware Key Mappings:")
    (log "   F1 - Volume Mute")
    (log "   F2 - Volume Down")
    (log "   F3 - Volume Up")
    (log "   F7 - Brightness Down")
    (log "   F8 - Brightness Up")
    (log "   F9 - WiFi Status")
    (log "   F10 - Switch to Starlink")
    (log "   F11 - Switch to Cellular")
    (log "   F12 - Airplane Mode")
    (log "")
    (log "🔧 Sway Keybindings:")
    (log "   Super + Shift + W - Start dual WiFi manager")
    (log "   Super + Alt + W - WiFi status")
    (log "   Super + Ctrl + W - Force Starlink")
    (log "   Super + Shift + Ctrl + W - Force Cellular")
    (log "")
    (log "🔄 To reload Sway config:")
    (log "   swaymsg reload")
    (log "")
    (log "🚀 To start dual WiFi manager:")
    (log "   bb scripts/dual-wifi-manager.bb start")
    
    (catch Exception e
      (log (str "❌ Setup failed: " (.getMessage e)))
      (System/exit 1))))

;; Run the setup
(main)

