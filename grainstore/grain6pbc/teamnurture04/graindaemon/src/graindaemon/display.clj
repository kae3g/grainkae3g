(ns graindaemon.display
  "GrainDaemon display warmth management
   
   Continuously monitors and maintains warm display settings,
   synchronized with the GrainDisplay UI"
  (:require [clojure.java.shell :as shell]
            [clojure.string :as str]
            [clojure.core.async :as async :refer [go go-loop <! >! <!! >!! timeout chan]]
            [clojure.edn :as edn]
            [babashka.fs :as fs]
            [clojure.java-time :as time]))

;; =============================================================================
;; Configuration
;; =============================================================================

(def config-file (str (System/getProperty "user.home") "/.config/graindaemon/display.edn"))

(def default-config
  {:enabled true
   :temperature 1000  ; 1000K = bedtime warm
   :brightness 0.8    ; 80%
   :auto-schedule true
   :check-interval-ms 60000  ; Check every minute
   :reapply-interval-ms 300000  ; Reapply every 5 minutes
   :presets {:bedtime {:temperature 1000 :brightness 0.8}
             :extreme {:temperature 800 :brightness 0.7}
             :evening {:temperature 2000 :brightness 0.85}
             :sunset {:temperature 3000 :brightness 0.9}
             :normal {:temperature 6500 :brightness 1.0}}})

(defonce daemon-state
  (atom {:config default-config
         :running false
         :last-applied nil
         :current-preset :bedtime
         :error nil}))

;; =============================================================================
;; Display Control
;; =============================================================================

(defn run-command [cmd]
  "Run shell command and return output"
  (let [result (shell/sh "bash" "-c" cmd)]
    (if (zero? (:exit result))
      (str/trim (:out result))
      (do
        (swap! daemon-state assoc :error {:command cmd :error (:err result)})
        nil))))

(defn get-primary-display []
  "Get primary display name"
  (or (run-command "xrandr --query | grep ' connected primary' | awk '{print $1}'")
      "eDP-2"))

(defn calculate-warm-gamma [temperature]
  "Calculate RGB gamma for color temperature"
  (let [temp (max 800 (min 10000 temperature))]
    (cond
      (<= temp 800) {:red 0.25 :green 0.12 :blue 0.03}
      (<= temp 1000) {:red 0.3 :green 0.15 :blue 0.05}
      (<= temp 1500) {:red 0.4 :green 0.2 :blue 0.08}
      (<= temp 2000) {:red 0.5 :green 0.3 :blue 0.1}
      (<= temp 2500) {:red 0.6 :green 0.4 :blue 0.15}
      (<= temp 3000) {:red 0.7 :green 0.5 :blue 0.2}
      (<= temp 4000) {:red 0.8 :green 0.7 :blue 0.5}
      (<= temp 5000) {:red 0.9 :green 0.85 :blue 0.7}
      :else {:red 1.0 :green 1.0 :blue 1.0})))

(defn apply-display-settings [temperature brightness]
  "Apply warmth settings to display"
  (let [display (get-primary-display)
        gamma (calculate-warm-gamma temperature)]
    
    ;; Method 1: xrandr gamma (most effective)
    (run-command (format "xrandr --output %s --gamma %.2f:%.2f:%.2f" 
                        display (:red gamma) (:green gamma) (:blue gamma)))
    (run-command (format "xrandr --output %s --brightness %.2f" display brightness))
    
    ;; Method 2: GNOME Night Light
    (run-command "gsettings set org.gnome.settings-daemon.plugins.color night-light-enabled true")
    (run-command (format "gsettings set org.gnome.settings-daemon.plugins.color night-light-temperature %d" 
                        (int temperature)))
    
    ;; Method 3: Dark theme
    (run-command "gsettings set org.gnome.desktop.interface color-scheme 'prefer-dark'")
    (run-command "gsettings set org.gnome.desktop.interface gtk-theme 'Adwaita-dark'")
    
    (swap! daemon-state assoc :last-applied (time/local-date-time))
    {:status :applied :temperature temperature :brightness brightness :display display}))

(defn reset-display []
  "Reset display to normal"
  (let [display (get-primary-display)]
    (run-command (format "xrandr --output %s --gamma 1.0:1.0:1.0" display))
    (run-command (format "xrandr --output %s --brightness 1.0" display))
    (run-command "gsettings set org.gnome.settings-daemon.plugins.color night-light-enabled false")
    (run-command "gsettings set org.gnome.desktop.interface gtk-theme 'Adwaita'")
    {:status :reset}))

;; =============================================================================
;; Scheduling
;; =============================================================================

(defn get-time-based-preset []
  "Get preset based on current time"
  (let [hour (.getHour (time/local-time))]
    (cond
      (and (>= hour 21) (< hour 24)) :extreme   ; 9 PM - Midnight: Super warm
      (or (>= hour 0) (< hour 6)) :extreme      ; Midnight - 6 AM: Super warm
      (and (>= hour 6) (< hour 12)) :sunset     ; 6 AM - Noon: Gentle warm
      (and (>= hour 12) (< hour 18)) :evening   ; Noon - 6 PM: Medium warm
      (and (>= hour 18) (< hour 21)) :bedtime   ; 6 PM - 9 PM: Bedtime warm
      :else :bedtime)))

(defn should-reapply? [state]
  "Check if settings should be reapplied"
  (let [last-applied (:last-applied state)
        reapply-interval (get-in state [:config :reapply-interval-ms] 300000)]
    (or (nil? last-applied)
        (> (- (System/currentTimeMillis) 
              (.toEpochMilli (.toInstant last-applied (time/zone-id))))
           reapply-interval))))

;; =============================================================================
;; Daemon Loop
;; =============================================================================

(defn start-daemon []
  "Start the display warmth daemon"
  (when-not (:running @daemon-state)
    (swap! daemon-state assoc :running true)
    
    (println "🌾 GrainDaemon Display Warmth Monitor Starting...")
    (println "")
    
    ;; Apply initial settings
    (let [config (:config @daemon-state)
          preset-name (if (:auto-schedule config)
                       (get-time-based-preset)
                       (:current-preset @daemon-state))
          preset (get-in config [:presets preset-name])]
      
      (println (str "Initial preset: " (name preset-name)))
      (println (str "Temperature: " (:temperature preset) "K"))
      (println (str "Brightness: " (* 100 (:brightness preset)) "%"))
      (println "")
      
      (apply-display-settings (:temperature preset) (:brightness preset))
      (swap! daemon-state assoc :current-preset preset-name))
    
    ;; Start monitoring loop
    (go-loop []
      (when (:running @daemon-state)
        (let [state @daemon-state
              config (:config state)]
          
          ;; Check if we should reapply settings
          (when (and (:enabled config) (should-reapply? state))
            (let [preset-name (if (:auto-schedule config)
                               (get-time-based-preset)
                               (:current-preset state))
                  preset (get-in config [:presets preset-name])]
              
              (println (str "[" (time/format "HH:mm:ss" (time/local-time)) "] "
                           "Applying " (name preset-name) " preset "
                           "(" (:temperature preset) "K)"))
              
              (apply-display-settings (:temperature preset) (:brightness preset))
              (swap! daemon-state assoc :current-preset preset-name)))
          
          ;; Wait for next check
          (<! (timeout (:check-interval-ms config)))
          (recur))))
    
    (println "✅ GrainDaemon Display Warmth Monitor Running")
    (println "")))

(defn stop-daemon []
  "Stop the daemon"
  (when (:running @daemon-state)
    (println "🛑 Stopping GrainDaemon Display Warmth Monitor...")
    (swap! daemon-state assoc :running false)
    (reset-display)
    (println "✅ Daemon stopped, display reset")))

;; =============================================================================
;; Public API
;; =============================================================================

(defn set-preset [preset-name]
  "Set a specific preset"
  (if-let [preset (get-in @daemon-state [:config :presets preset-name])]
    (do
      (apply-display-settings (:temperature preset) (:brightness preset))
      (swap! daemon-state assoc :current-preset preset-name)
      {:status :success :preset preset-name})
    {:status :error :message (str "Unknown preset: " preset-name)}))

(defn set-custom [temperature brightness]
  "Set custom temperature and brightness"
  (apply-display-settings temperature brightness)
  {:status :success :temperature temperature :brightness brightness})

(defn enable-auto-schedule []
  "Enable automatic time-based scheduling"
  (swap! daemon-state assoc-in [:config :auto-schedule] true)
  {:status :enabled})

(defn disable-auto-schedule []
  "Disable automatic scheduling"
  (swap! daemon-state assoc-in [:config :auto-schedule] false)
  {:status :disabled})

(defn get-status []
  "Get current daemon status"
  (let [state @daemon-state]
    {:running (:running state)
     :current-preset (:current-preset state)
     :last-applied (:last-applied state)
     :config (:config state)
     :error (:error state)}))

;; =============================================================================
;; CLI
;; =============================================================================

(defn -main [& args]
  "Main CLI entry point"
  (case (first args)
    "start" (start-daemon)
    "stop" (stop-daemon)
    "status" (do
               (println "🌾 GrainDaemon Display Status:")
               (println "")
               (clojure.pprint/pprint (get-status)))
    "preset" (do
               (if-let [preset-name (keyword (second args))]
                 (do
                   (println (str "Setting preset: " preset-name))
                   (clojure.pprint/pprint (set-preset preset-name)))
                 (println "Usage: preset [bedtime|extreme|evening|sunset|normal]")))
    "custom" (do
               (if-let [temp (parse-long (second args))]
                 (let [bright (or (parse-double (nth args 2 "0.8")) 0.8)]
                   (println (str "Setting custom: " temp "K, " (* 100 bright) "%"))
                   (clojure.pprint/pprint (set-custom temp bright)))
                 (println "Usage: custom TEMPERATURE [BRIGHTNESS]")))
    "auto-on" (do
                (println "Enabling auto-schedule")
                (enable-auto-schedule))
    "auto-off" (do
                 (println "Disabling auto-schedule")
                 (disable-auto-schedule))
    (do
      (println "🌾 GrainDaemon Display Warmth Manager")
      (println "")
      (println "USAGE:")
      (println "  start        Start the daemon")
      (println "  stop         Stop the daemon and reset display")
      (println "  status       Show current status")
      (println "  preset NAME  Set preset (bedtime, extreme, evening, sunset, normal)")
      (println "  custom TEMP [BRIGHTNESS]  Set custom values")
      (println "  auto-on      Enable automatic time-based scheduling")
      (println "  auto-off     Disable automatic scheduling")
      (println ""))))
