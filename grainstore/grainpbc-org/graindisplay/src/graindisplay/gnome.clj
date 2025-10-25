(ns graindisplay.gnome
  "GNOME-specific display management via gsettings and D-Bus"
  (:require [babashka.process :refer [shell sh]]
            [clojure.string :as str]
            [clojure.tools.logging :as log]))

(defn gsettings-get
  "Get a GNOME gsettings value"
  [schema key]
  (-> (sh "gsettings" "get" schema key)
      :out
      str/trim))

(defn gsettings-set!
  "Set a GNOME gsettings value"
  [schema key value]
  (sh "gsettings" "set" schema key (str value))
  (log/info "Set" schema key "to" value))

(defn get-text-scaling
  "Get current text scaling factor"
  []
  (-> (gsettings-get "org.gnome.desktop.interface" "text-scaling-factor")
      read-string))

(defn set-text-scaling!
  "Set text scaling factor (e.g., 1.0, 1.25, 1.5, 1.75, 2.0)"
  [factor]
  (gsettings-set! "org.gnome.desktop.interface" "text-scaling-factor" factor))

(defn get-night-light-info
  "Get current night light settings"
  []
  {:enabled (-> (gsettings-get "org.gnome.settings-daemon.plugins.color" "night-light-enabled")
                (= "true"))
   :temperature (-> (gsettings-get "org.gnome.settings-daemon.plugins.color" "night-light-temperature")
                    (str/replace #"uint32 " "")
                    read-string)
   :schedule-automatic (-> (gsettings-get "org.gnome.settings-daemon.plugins.color" "night-light-schedule-automatic")
                           (= "true"))})

(defn set-night-light!
  "Configure GNOME Night Light
   Options:
   - :enabled (boolean)
   - :temperature (1000-6500K, warmer = lower)
   - :schedule-automatic (boolean, false = manual 24/7)"
  [{:keys [enabled temperature schedule-automatic]
    :or {enabled true
         temperature 2000
         schedule-automatic false}}]
  (when (some? enabled)
    (gsettings-set! "org.gnome.settings-daemon.plugins.color" "night-light-enabled" enabled))
  (when temperature
    (gsettings-set! "org.gnome.settings-daemon.plugins.color" "night-light-temperature" temperature))
  (when (some? schedule-automatic)
    (gsettings-set! "org.gnome.settings-daemon.plugins.color" "night-light-schedule-automatic" schedule-automatic)
    (when-not schedule-automatic
      ;; Set manual schedule to run 24/7
      (gsettings-set! "org.gnome.settings-daemon.plugins.color" "night-light-schedule-from" 0.0)
      (gsettings-set! "org.gnome.settings-daemon.plugins.color" "night-light-schedule-to" 23.99))))

(defn toggle-night-light!
  "Toggle night light on/off"
  []
  (let [current (get-night-light-info)]
    (set-night-light! {:enabled (not (:enabled current))})))

(defn get-display-resolution
  "Get current GNOME display resolution using xrandr"
  []
  (let [output (-> (sh "xrandr") :out)]
    (->> (str/split-lines output)
         (filter #(str/includes? % " connected primary "))
         first
         (re-find #"(\d+)x(\d+)")
         rest
         (mapv #(Integer/parseInt %)))))

(defn get-available-modes
  "Get all available display modes from xrandr"
  [display-name]
  (let [output (-> (sh "xrandr") :out)
        lines (str/split-lines output)
        display-line-idx (first
                          (keep-indexed
                           #(when (str/includes? %2 (str display-name " connected")) %1)
                           lines))
        mode-lines (when display-line-idx
                     (take-while
                      #(str/starts-with? % "   ")
                      (drop (inc display-line-idx) lines)))]
    (->> mode-lines
         (map #(re-find #"(\d+)x(\d+)\s+([\d.]+)" %))
         (filter some?)
         (map (fn [[_ w h rate]]
                {:width (Integer/parseInt w)
                 :height (Integer/parseInt h)
                 :refresh-rate (Double/parseDouble rate)}))
         vec)))

(defn get-primary-display-name
  "Get the name of the primary display"
  []
  (let [output (-> (sh "xrandr") :out)]
    (->> (str/split-lines output)
         (filter #(str/includes? % " connected primary "))
         first
         (re-find #"^(\S+)")
         second)))

(defn get-display-info
  "Get comprehensive display information"
  []
  (let [display-name (get-primary-display-name)
        [width height] (get-display-resolution)
        modes (get-available-modes display-name)
        scaling (get-text-scaling)
        night-light (get-night-light-info)]
    {:display-name display-name
     :resolution [width height]
     :scaling scaling
     :available-modes modes
     :night-light night-light}))

(defn set-scaling-and-resolution!
  "Set both text scaling and resolution
   Note: GNOME on Wayland uses fractional scaling differently than X11.
   This function sets text scaling, which affects fonts system-wide."
  [{:keys [scaling resolution]
    :or {scaling 1.5}}]
  (when scaling
    (set-text-scaling! scaling)
    (log/info "Set text scaling to" scaling))
  (when resolution
    (log/warn "Direct resolution changes via xrandr may not work on GNOME/Wayland")
    (log/info "Recommended: Use GNOME Settings > Displays to change resolution")))

(defn optimize-for-hidpi!
  "Optimize display settings for HiDPI screens"
  [{:keys [scaling temperature]
    :or {scaling 1.75 temperature 2000}}]
  (set-text-scaling! scaling)
  (set-night-light! {:enabled true
                     :temperature temperature
                     :schedule-automatic false})
  (log/info "Optimized for HiDPI: scaling=" scaling "temperature=" temperature))

(defn reset-to-defaults!
  "Reset display settings to GNOME defaults"
  []
  (set-text-scaling! 1.0)
  (set-night-light! {:enabled false
                     :schedule-automatic false})
  (log/info "Reset display settings to defaults"))

