(ns graindisplay.gnome-service
  "GNOME service integration for GrainDisplay using s6"
  (:require [clojure-s6.core :as s6]
            [clojure.edn :as edn]
            [clojure.string :as str]
            [babashka.fs :as fs]
            [clojure.java-time :as time]))

;; =============================================================================
;; GNOME Service Configuration
;; =============================================================================

(def service-name "graindisplay")
(def config-dir "~/.config/graindisplay")
(def log-dir "/var/log/graindisplay")

(defn create-gnome-service []
  "Create s6 service for GrainDisplay GNOME integration"
  (s6/create-service
   {:name service-name
    :command "clojure -M:dev -m graindisplay.core"
    :type :longrun
    :user "xy"
    :group "xy"
    :environment {"DISPLAY" ":0"
                  "XDG_RUNTIME_DIR" "/run/user/1000"
                  "WAYLAND_DISPLAY" "wayland-0"}
    :dependencies ["dbus" "gnome-session"]}))

(defn create-gnome-session-integration []
  "Create GNOME session integration files"
  (let [autostart-dir (fs/expand-home "~/.config/autostart")
        desktop-file (str autostart-dir "/graindisplay.desktop")]
    
    ;; Create autostart directory
    (fs/create-dirs autostart-dir)
    
    ;; Create desktop file
    (spit desktop-file
          (str/join "\n"
            ["[Desktop Entry]"
             "Type=Application"
             "Name=GrainDisplay"
             "Comment=Bedtime Warm Theme for GNOME"
             "Exec=clojure -M:dev -m graindisplay.core"
             "Icon=graindisplay"
             "StartupNotify=false"
             "NoDisplay=false"
             "Hidden=false"
             "X-GNOME-Autostart-enabled=true"
             "X-GNOME-Autostart-Delay=10"]))
    
    (println "✅ Created GNOME autostart integration")))

(defn create-gnome-settings-schema []
  "Create GNOME settings schema for GrainDisplay"
  (let [schema-dir "/usr/share/glib-2.0/schemas"
        schema-file (str schema-dir "/org.grainpbc.graindisplay.gschema.xml")]
    
    (fs/create-dirs schema-dir)
    
    (spit schema-file
          (str/join "\n"
            ["<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
             "<schemalist>"
             "  <schema id=\"org.grainpbc.graindisplay\""
             "          path=\"/org/grainpbc/graindisplay/\""
             "          gettext-domain=\"graindisplay\">"
             "    <key name=\"enabled\" type=\"b\">"
             "      <default>true</default>"
             "      <summary>Enable GrainDisplay</summary>"
             "      <description>Enable the GrainDisplay bedtime warm theme</description>"
             "    </key>"
             "    <key name=\"auto-schedule\" type=\"b\">"
             "      <default>true</default>"
             "      <summary>Auto Schedule</summary>"
             "      <description>Automatically adjust theme based on time of day</description>"
             "    </key>"
             "    <key name=\"sunrise-offset\" type=\"i\">"
             "      <default>0</default>"
             "      <summary>Sunrise Offset</summary>"
             "      <description>Minutes to offset from calculated sunrise</description>"
             "    </key>"
             "    <key name=\"sunset-offset\" type=\"i\">"
             "      <default>0</default>"
             "      <summary>Sunset Offset</summary>"
             "      <description>Minutes to offset from calculated sunset</description>"
             "    </key>"
             "    <key name=\"transition-duration\" type=\"i\">"
             "      <default>30</default>"
             "      <summary>Transition Duration</summary>"
             "      <description>Duration of theme transitions in minutes</description>"
             "    </key>"
             "    <key name=\"latitude\" type=\"d\">"
             "      <default>37.9735</default>"
             "      <summary>Latitude</summary>"
             "      <description>Location latitude for sun calculations</description>"
             "    </key>"
             "    <key name=\"longitude\" type=\"d\">"
             "      <default>-122.5311</default>"
             "      <summary>Longitude</summary>"
             "      <description>Location longitude for sun calculations</description>"
             "    </key>"
             "    <key name=\"timezone\" type=\"s\">"
             "      <default>\"America/Los_Angeles\"</default>"
             "      <summary>Timezone</summary>"
             "      <description>Location timezone</description>"
             "    </key>"
             "  </schema>"
             "</schemalist>"]))
    
    ;; Compile schema
    (clojure.java.shell/sh "glib-compile-schemas" schema-dir)
    (println "✅ Created GNOME settings schema")))

(defn create-gnome-shell-extension []
  "Create GNOME Shell extension for GrainDisplay"
  (let [extension-dir (fs/expand-home "~/.local/share/gnome-shell/extensions/graindisplay@grainsource.org")
        metadata-file (str extension-dir "/metadata.json")
        extension-file (str extension-dir "/extension.js")]
    
    (fs/create-dirs extension-dir)
    
    ;; Create metadata
    (spit metadata-file
          (str/join "\n"
            ["{"
             "  \"name\": \"GrainDisplay\","
             "  \"description\": \"Bedtime Warm Theme for GNOME\","
             "  \"uuid\": \"graindisplay@grainsource.org\","
             "  \"shell-version\": [\"40\", \"41\", \"42\", \"43\", \"44\", \"45\"],"
             "  \"version\": 1,"
             "  \"url\": \"https://github.com/grainpbc/graindisplay\""
             "}"]))
    
    ;; Create extension script
    (spit extension-file
          (str/join "\n"
            ["const { Gio, GLib } = imports.gi;"
             "const Main = imports.ui.main;"
             ""
             "let graindisplayProcess = null;"
             ""
             "function init() {"
             "  // Initialize GrainDisplay process"
             "  graindisplayProcess = Gio.Subprocess.new("
             "    ['clojure', '-M:dev', '-m', 'graindisplay.core'],"
             "    Gio.SubprocessFlags.STDOUT_PIPE | Gio.SubprocessFlags.STDERR_PIPE"
             "  );"
             "  graindisplayProcess.init(null);"
             "}"
             ""
             "function enable() {"
             "  // Start GrainDisplay when extension is enabled"
             "  if (graindisplayProcess) {"
             "    graindisplayProcess.wait_async(null, (process, result) => {"
             "      // Handle process completion"
             "    });"
             "  }"
             "}"
             ""
             "function disable() {"
             "  // Stop GrainDisplay when extension is disabled"
             "  if (graindisplayProcess) {"
             "    graindisplayProcess.force_exit();"
             "    graindisplayProcess = null;"
             "  }"
             "}"]))
    
    (println "✅ Created GNOME Shell extension")))

(defn create-systemd-user-service []
  "Create systemd user service as fallback"
  (let [systemd-dir (fs/expand-home "~/.config/systemd/user")
        service-file (str systemd-dir "/graindisplay.service")]
    
    (fs/create-dirs systemd-dir)
    
    (spit service-file
          (str/join "\n"
            ["[Unit]"
             "Description=GrainDisplay Bedtime Warm Theme"
             "After=graphical-session.target"
             ""
             "[Service]"
             "Type=simple"
             "ExecStart=clojure -M:dev -m graindisplay.core"
             "Restart=always"
             "RestartSec=5"
             "Environment=DISPLAY=:0"
             "Environment=XDG_RUNTIME_DIR=/run/user/%i"
             ""
             "[Install]"
             "WantedBy=default.target"]))
    
    (println "✅ Created systemd user service")))

(defn install-gnome-integration []
  "Install complete GNOME integration"
  (println "🌾 Installing GrainDisplay GNOME integration...")
  
  (create-gnome-service)
  (create-gnome-session-integration)
  (create-gnome-settings-schema)
  (create-gnome-shell-extension)
  (create-systemd-user-service)
  
  (println "✅ GNOME integration complete!")
  (println "📋 Next steps:")
  (println "  1. Reload GNOME Shell: Alt+F2, type 'r', press Enter")
  (println "  2. Enable extension: gnome-extensions enable graindisplay@grainsource.org")
  (println "  3. Start service: s6-svc -u /etc/s6/sv/graindisplay"))

(defn -main [& args]
  "Main entry point for GNOME service installation"
  (case (first args)
    "install" (install-gnome-integration)
    "start" (s6/start-service service-name)
    "stop" (s6/stop-service service-name)
    "status" (println (s6/service-status service-name))
    (do
      (println "GrainDisplay GNOME Service")
      (println "Usage: clojure -M gnome-service.clj [install|start|stop|status]"))))
