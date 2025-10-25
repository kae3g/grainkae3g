#!/usr/bin/env bb
(ns apply-theme
  "Apply GrainDisplay bedtime warm theme to various applications"
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [babashka.process :as process]
            [cheshire.core :as json]))

(defn generate-vscode-theme
  "Generate VS Code theme JSON"
  []
  {:name "GrainDisplay Bedtime"
   :type "dark"
   :colors
   {:editor.background "#1A0F0A"
    :editor.foreground "#FFF8DC"
    :editorLineNumber.foreground "#8B4513"
    :editor.selectionBackground "#D2691E"
    :editorCursor.foreground "#FF8C00"
    :sideBar.background "#2D1B13"
    :statusBar.background "#3D2B1F"
    :titleBar.activeBackground "#2D1B13"
    :activityBar.background "#2D1B13"
    :panel.background "#1A0F0A"
    :terminal.background "#1A0F0A"
    :terminal.foreground "#FFF8DC"}
   :tokenColors
   [{:scope "comment"
     :settings {:foreground "#8B4513"}}
    {:scope "string"
     :settings {:foreground "#F5DEB3"}}
    {:scope "keyword"
     :settings {:foreground "#FF8C00"}}
    {:scope "function"
     :settings {:foreground "#DAA520"}}
    {:scope "number"
     :settings {:foreground "#D2691E"}}
    {:scope "variable"
     :settings {:foreground "#DEB887"}}]})

(defn generate-iterm2-profile
  "Generate iTerm2 profile JSON"
  []
  {:name "GrainDisplay Bedtime"
   :background_color [26 15 10]
   :foreground_color [255 248 220]
   :cursor_color [255 140 0]
   :selection_color [210 105 30]
   :colors
   [[26 15 10]    ; Black
    [139 69 19]   ; Red
    [34 139 34]   ; Green (warm)
    [255 165 0]   ; Yellow (warm)
    [70 130 180]  ; Blue (warm)
    [186 85 211]  ; Magenta (warm)
    [64 224 208]  ; Cyan (warm)
    [255 248 220] ; White
    [60 43 31]    ; Bright Black
    [210 105 30]  ; Bright Red
    [144 238 144] ; Bright Green
    [255 215 0]   ; Bright Yellow
    [135 206 235] ; Bright Blue
    [221 160 221] ; Bright Magenta
    [175 238 238] ; Bright Cyan
    [255 255 255]]}) ; Bright White

(defn generate-alacritty-config
  "Generate Alacritty terminal config"
  []
  (str/join "\n"
    ["# GrainDisplay Bedtime Warm Theme"
     "[colors.primary]"
     "background = '0x1A0F0A'"
     "foreground = '0xFFF8DC'"
     ""
     "[colors.cursor]"
     "text = '0x1A0F0A'"
     "cursor = '0xFF8C00'"
     ""
     "[colors.normal]"
     "black = '0x1A0F0A'"
     "red = '0x8B4513'"
     "green = '0x228B22'"
     "yellow = '0xFFA500'"
     "blue = '0x4682B4'"
     "magenta = '0xBA55D3'"
     "cyan = '0x40E0D0'"
     "white = '0xFFF8DC'"
     ""
     "[colors.bright]"
     "black = '0x3C2B1F'"
     "red = '0xD2691E'"
     "green = '0x90EE90'"
     "yellow = '0xFFD700'"
     "blue = '0x87CEEB'"
     "magenta = '0xDDA0DD'"
     "cyan = '0xAFEEEE'"
     "white = '0xFFFFFF'"]))

(defn generate-gnome-terminal-profile
  "Generate GNOME Terminal profile"
  []
  (str/join "\n"
    ["# GrainDisplay Bedtime Warm Theme for GNOME Terminal"
     "# Background: #1A0F0A (warm black)"
     "# Foreground: #FFF8DC (warm white)"
     "# Palette:"
     "# 0: #1A0F0A (warm black)"
     "# 1: #8B4513 (warm red)"
     "# 2: #228B22 (warm green)"
     "# 3: #FFA500 (warm yellow)"
     "# 4: #4682B4 (warm blue)"
     "# 5: #BA55D3 (warm magenta)"
     "# 6: #40E0D0 (warm cyan)"
     "# 7: #FFF8DC (warm white)"
     "# 8: #3C2B1F (bright black)"
     "# 9: #D2691E (bright red)"
     "# 10: #90EE90 (bright green)"
     "# 11: #FFD700 (bright yellow)"
     "# 12: #87CEEB (bright blue)"
     "# 13: #DDA0DD (bright magenta)"
     "# 14: #AFEEEE (bright cyan)"
     "# 15: #FFFFFF (bright white)"]))

(defn apply-vscode-theme
  "Apply VS Code theme"
  []
  (let [theme-dir (str (System/getProperty "user.home") "/.vscode/extensions/")
        theme-file (str theme-dir "graindisplay-bedtime.json")
        theme-data (generate-vscode-theme)]
    (println "🌾 Applying VS Code theme...")
    (try
      (spit theme-file (json/generate-string theme-data {:pretty true}))
      (println "✅ VS Code theme saved to:" theme-file)
      (catch Exception e
        (println "❌ Failed to save VS Code theme:" (.getMessage e))))))

(defn apply-terminal-theme
  "Apply terminal theme"
  [terminal-type]
  (case terminal-type
    "iterm2" (do
               (println "🌾 Generating iTerm2 profile...")
               (let [profile (generate-iterm2-profile)]
                 (spit "graindisplay-iterm2.json" (json/generate-string profile {:pretty true}))
                 (println "✅ iTerm2 profile saved to: graindisplay-iterm2.json")))
    
    "alacritty" (do
                  (println "🌾 Generating Alacritty config...")
                  (let [config (generate-alacritty-config)]
                    (spit "alacritty-graindisplay.yml" config)
                    (println "✅ Alacritty config saved to: alacritty-graindisplay.yml")))
    
    "gnome" (do
              (println "🌾 Generating GNOME Terminal profile...")
              (let [profile (generate-gnome-terminal-profile)]
                (spit "graindisplay-gnome-terminal.txt" profile)
                (println "✅ GNOME Terminal profile saved to: graindisplay-gnome-terminal.txt")))
    
    (println "❌ Unknown terminal type:" terminal-type)))

(defn generate-html-demo
  "Generate HTML demo page"
  []
  (str/join "\n"
    ["<!DOCTYPE html>"
     "<html lang=\"en\">"
     "<head>"
     "    <meta charset=\"UTF-8\">"
     "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
     "    <title>GrainDisplay Bedtime Warm Theme Demo</title>"
     "    <link rel=\"stylesheet\" href=\"graindisplay-bedtime.css\">"
     "    <style>"
     "        body { margin: 0; padding: 20px; }"
     "        .demo-section { margin: 20px 0; }"
     "        .demo-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 20px; }"
     "    </style>"
     "</head>"
     "<body class=\"grain-display\">"
     "    <h1>🌾 GrainDisplay Bedtime Warm Theme</h1>"
     "    <p>Cozy red-orange colors for comfortable evening viewing</p>"
     ""
     "    <div class=\"demo-section\">"
     "        <h2>Buttons</h2>"
     "        <div class=\"demo-grid\">"
     "            <button class=\"grain-btn-warm\">Primary Button</button>"
     "            <button class=\"grain-btn-warm grain-btn-secondary\">Secondary Button</button>"
     "        </div>"
     "    </div>"
     ""
     "    <div class=\"demo-section\">"
     "        <h2>Cards</h2>"
     "        <div class=\"demo-grid\">"
     "            <div class=\"grain-card-warm\">"
     "                <h3>Warm Card</h3>"
     "                <p>This is a cozy card with warm colors</p>"
     "            </div>"
     "        </div>"
     "    </div>"
     ""
     "    <div class=\"demo-section\">"
     "        <h2>Code</h2>"
     "        <div class=\"grain-code-warm\">"
     "            <pre>// GrainDisplay Bedtime Warm Theme\nconst warmRed = '#D2691E';\nconst deepOrange = '#FF8C00';\nconsole.log('🌾 From granules to grains to THE WHOLE GRAIN');</pre>"
     "        </div>"
     "    </div>"
     ""
     "    <div class=\"demo-section\">"
     "        <h2>Links</h2>"
     "        <p>This is a <a href=\"#\" class=\"grain-link-warm\">warm link</a> with golden color</p>"
     "    </div>"
     ""
     "    <div class=\"demo-section\">"
     "        <h2>Forms</h2>"
     "        <input type=\"text\" class=\"grain-input-warm\" placeholder=\"Enter your text here...\">"
     "    </div>"
     ""
     "    <div class=\"demo-section\">"
     "        <h2>Alerts</h2>"
     "        <div class=\"grain-alert-warm success\">✅ Success message</div>"
     "        <div class=\"grain-alert-warm warning\">⚠️ Warning message</div>"
     "        <div class=\"grain-alert-warm error\">❌ Error message</div>"
     "    </div>"
     "</body>"
     "</html>"]))

(defn main
  "Main function for applying GrainDisplay theme"
  []
  (let [args *command-line-args*
        command (first args)
        target (second args)]
    (case command
      "vscode" (apply-vscode-theme)
      "terminal" (apply-terminal-theme target)
      "demo" (do
               (println "🌾 Generating HTML demo...")
               (spit "graindisplay-demo.html" (generate-html-demo))
               (println "✅ HTML demo saved to: graindisplay-demo.html"))
      "all" (do
              (apply-vscode-theme)
              (apply-terminal-theme "iterm2")
              (apply-terminal-theme "alacritty")
              (apply-terminal-theme "gnome")
              (println "🌾 Generating HTML demo...")
              (spit "graindisplay-demo.html" (generate-html-demo))
              (println "✅ All themes generated!"))
      "help" (do
               (println "🌾 GrainDisplay Theme Applicator")
               (println "")
               (println "USAGE:")
               (println "  bb apply-theme.bb vscode                    Apply VS Code theme")
               (println "  bb apply-theme.bb terminal <type>           Apply terminal theme")
               (println "  bb apply-theme.bb demo                      Generate HTML demo")
               (println "  bb apply-theme.bb all                       Apply all themes")
               (println "  bb apply-theme.bb help                      Show this help")
               (println "")
               (println "TERMINAL TYPES:")
               (println "  iterm2    - iTerm2 profile")
               (println "  alacritty - Alacritty config")
               (println "  gnome     - GNOME Terminal profile")
               (println "")
               (println "EXAMPLES:")
               (println "  bb apply-theme.bb terminal iterm2")
               (println "  bb apply-theme.bb demo"))
      (do
        (println "🌾 GrainDisplay Theme Applicator")
        (println "Use 'bb apply-theme.bb help' for usage information")))))

(main)
