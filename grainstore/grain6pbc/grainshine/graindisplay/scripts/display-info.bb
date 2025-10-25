#!/usr/bin/env bb

(require '[babashka.process :refer [sh]]
         '[clojure.string :as str])

(defn get-gsetting [schema key]
  (-> (sh "gsettings" "get" schema key)
      :out
      str/trim))

(defn get-primary-display []
  (let [output (-> (sh "xrandr") :out)]
    (->> (str/split-lines output)
         (filter #(str/includes? % " connected primary "))
         first)))

(defn parse-resolution [line]
  (when-let [[_ w h rate] (re-find #"(\d+)x(\d+)\+\d+\+\d+ \(.*?\) \d+mm x \d+mm|(\d+)x(\d+).*?([\d.]+)\*\+" line)]
    (if rate
      {:width w :height h :refresh-rate rate}
      (re-find #"(\d+)x(\d+)" line))))

(defn main []
  (println "🖥️  GrainDisplay - Display Information\n")
  
  (let [display-line (get-primary-display)
        display-name (when display-line (second (re-find #"^(\S+)" display-line)))
        current-res (when display-line (re-find #"(\d+)x(\d+)" display-line))
        scaling (get-gsetting "org.gnome.desktop.interface" "text-scaling-factor")
        night-light-enabled (get-gsetting "org.gnome.settings-daemon.plugins.color" "night-light-enabled")
        night-light-temp (-> (get-gsetting "org.gnome.settings-daemon.plugins.color" "night-light-temperature")
                             (str/replace #"uint32 " ""))]
    
    (println "📺 Primary Display:" display-name)
    (when current-res
      (println "📐 Current Resolution:" (str (second current-res) "x" (nth current-res 2))))
    (println "🔍 Text Scaling:" scaling "x")
    (println "🌙 Night Light:" (if (= night-light-enabled "true") "✅ Enabled" "❌ Disabled"))
    (when (= night-light-enabled "true")
      (println "🌡️  Temperature:" night-light-temp "K"))
    
    (println "\n📋 Available Resolutions:")
    (let [xrandr-output (-> (sh "xrandr") :out)
          lines (str/split-lines xrandr-output)
          display-idx (first (keep-indexed #(when (str/includes? %2 (str display-name " connected")) %1) lines))
          mode-lines (when display-idx
                       (take-while #(str/starts-with? % "   ")
                                   (drop (inc display-idx) lines)))]
      (doseq [line (take 10 mode-lines)]
        (when-let [[_ w h rate] (re-find #"(\d+)x(\d+)\s+([\d.]+)" line)]
          (let [is-current (str/includes? line "*")]
            (println (if is-current "  •" "   ")
                     (format "%sx%s @ %.2fHz" w h (Double/parseDouble rate))
                     (if is-current "(current)" ""))))))))

(main)

