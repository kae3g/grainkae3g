#!/usr/bin/env bb
(ns apply-local
  "Apply GrainDisplay bedtime warm theme locally"
  (:require [clojure.string :as str]))

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
     "    <style>"
     "        :root {"
     "            --warm-red: #D2691E;"
     "            --deep-orange: #FF8C00;"
     "            --burnt-orange: #CC5500;"
     "            --warm-brown: #8B4513;"
     "            --warm-black: #1A0F0A;"
     "            --warm-dark: #2D1B13;"
     "            --warm-gray: #3D2B1F;"
     "            --warm-white: #FFF8DC;"
     "            --warm-cream: #F5DEB3;"
     "            --warm-beige: #DEB887;"
     "            --warm-gold: #DAA520;"
     "        }"
     "        body {"
     "            background: var(--warm-black);"
     "            color: var(--warm-white);"
     "            font-family: 'JetBrains Mono', 'Fira Code', monospace;"
     "            margin: 0;"
     "            padding: 20px;"
     "            line-height: 1.6;"
     "        }"
     "        .grain-btn {"
     "            background: var(--warm-red);"
     "            color: var(--warm-white);"
     "            border: 2px solid var(--deep-orange);"
     "            border-radius: 8px;"
     "            padding: 12px 24px;"
     "            font-family: inherit;"
     "            cursor: pointer;"
     "            transition: all 0.3s ease;"
     "            margin: 8px;"
     "        }"
     "        .grain-btn:hover {"
     "            background: var(--deep-orange);"
     "            box-shadow: 0 0 15px rgba(255, 140, 0, 0.4);"
     "            transform: translateY(-2px);"
     "        }"
     "        .grain-card {"
     "            background: var(--warm-dark);"
     "            border: 1px solid var(--warm-gray);"
     "            border-radius: 12px;"
     "            padding: 20px;"
     "            margin: 16px 0;"
     "            box-shadow: 0 4px 15px rgba(210, 105, 30, 0.2);"
     "        }"
     "        .grain-code {"
     "            background: var(--warm-black);"
     "            border: 1px solid var(--warm-brown);"
     "            border-radius: 8px;"
     "            padding: 16px;"
     "            font-family: 'JetBrains Mono', monospace;"
     "            color: var(--warm-cream);"
     "            overflow-x: auto;"
     "        }"
     "        .grain-link {"
     "            color: var(--warm-gold);"
     "            text-decoration: none;"
     "        }"
     "        .grain-link:hover {"
     "            color: var(--deep-orange);"
     "            text-shadow: 0 0 8px rgba(255, 140, 0, 0.6);"
     "        }"
     "        h1, h2, h3 { color: var(--warm-gold); }"
     "        .demo-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 20px; }"
     "    </style>"
     "</head>"
     "<body>"
     "    <h1>🌾 GrainDisplay Bedtime Warm Theme</h1>"
     "    <p>Cozy red-orange colors for comfortable evening viewing</p>"
     ""
     "    <div class=\"grain-card\">"
     "        <h2>🌙 Bedtime Colors</h2>"
     "        <p>Warm red-orange palette designed for evening comfort:</p>"
     "        <ul>"
     "            <li><strong>Warm Red:</strong> #D2691E (Chocolate)</li>"
     "            <li><strong>Deep Orange:</strong> #FF8C00 (Dark Orange)</li>"
     "            <li><strong>Warm Black:</strong> #1A0F0A (Very dark red-brown)</li>"
     "            <li><strong>Warm White:</strong> #FFF8DC (Cornsilk)</li>"
     "        </ul>"
     "    </div>"
     ""
     "    <div class=\"grain-card\">"
     "        <h2>🎨 Interactive Elements</h2>"
     "        <div class=\"demo-grid\">"
     "            <button class=\"grain-btn\">Primary Button</button>"
     "            <button class=\"grain-btn\">Another Button</button>"
     "        </div>"
     "        <p>This is a <a href=\"#\" class=\"grain-link\">warm golden link</a> with hover effects.</p>"
     "    </div>"
     ""
     "    <div class=\"grain-card\">"
     "        <h2>💻 Code Example</h2>"
     "        <div class=\"grain-code\">"
     "            <pre>// GrainDisplay Bedtime Warm Theme\nconst warmRed = '#D2691E';\nconst deepOrange = '#FF8C00';\nconst warmBlack = '#1A0F0A';\nconst warmWhite = '#FFF8DC';\n\nconsole.log('🌾 From granules to grains to THE WHOLE GRAIN');</pre>"
     "        </div>"
     "    </div>"
     ""
     "    <div class=\"grain-card\">"
     "        <h2>🌾 Grain Network Philosophy</h2>"
     "        <p><em>\"From granules to grains to THE WHOLE GRAIN\"</em></p>"
     "        <p>The warm bedtime theme embodies the comfort and nourishment of the Grain Network. Just as grains provide sustenance, these colors provide visual comfort for evening work and study.</p>"
     "    </div>"
     "</body>"
     "</html>"]))

(defn generate-terminal-colors
  "Generate terminal color configuration"
  []
  (str/join "\n"
    ["# GrainDisplay Bedtime Warm Theme for Terminal"
     "# Apply these colors to your terminal emulator"
     ""
     "# Background: #1A0F0A (warm black)"
     "# Foreground: #FFF8DC (warm white)"
     "# Cursor: #FF8C00 (deep orange)"
     ""
     "# Color Palette:"
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
     "# 15: #FFFFFF (bright white)"
     ""
     "# For GNOME Terminal:"
     "gconftool-2 --set /apps/gnome-terminal/profiles/Default/background_color --type string '#1A0F0A'"
     "gconftool-2 --set /apps/gnome-terminal/profiles/Default/foreground_color --type string '#FFF8DC'"
     ""
     "# For Alacritty (add to ~/.config/alacritty/alacritty.yml):"
     "[colors.primary]"
     "background = '0x1A0F0A'"
     "foreground = '0xFFF8DC'"
     ""
     "[colors.cursor]"
     "text = '0x1A0F0A'"
     "cursor = '0xFF8C00'"]))

(defn generate-css-vars
  "Generate CSS variables for easy integration"
  []
  (str/join "\n"
    ["/* GrainDisplay Bedtime Warm Theme - CSS Variables */"
     ":root {"
     "  /* Primary Colors */"
     "  --warm-red: #D2691E;        /* Chocolate - Primary accent */"
     "  --deep-orange: #FF8C00;     /* Dark Orange - Secondary accent */"
     "  --burnt-orange: #CC5500;    /* Burnt Orange - Tertiary accent */"
     "  --warm-brown: #8B4513;      /* Saddle Brown - Dark accents */"
     "  "
     "  /* Background Colors */"
     "  --warm-black: #1A0F0A;      /* Very dark red-brown */"
     "  --warm-dark: #2D1B13;       /* Dark red-brown */"
     "  --warm-gray: #3D2B1F;       /* Medium red-brown */"
     "  --warm-light: #4A3429;      /* Light red-brown */"
     "  "
     "  /* Text Colors */"
     "  --warm-white: #FFF8DC;      /* Cornsilk - Primary text */"
     "  --warm-cream: #F5DEB3;      /* Wheat - Secondary text */"
     "  --warm-beige: #DEB887;      /* Burlywood - Tertiary text */"
     "  --warm-gold: #DAA520;       /* Goldenrod - Accent text */"
     "}"
     ""
     "/* Quick apply to any element */"
     ".grain-warm {"
     "  background: var(--warm-black);"
     "  color: var(--warm-white);"
     "  font-family: 'JetBrains Mono', 'Fira Code', monospace;"
     "}"]))

(defn main
  "Apply GrainDisplay theme locally"
  []
  (let [args *command-line-args*
        command (first args)]
    (case command
      "demo" (do
               (println "🌾 Generating HTML demo...")
               (spit "graindisplay-demo.html" (generate-html-demo))
               (println "✅ HTML demo saved to: graindisplay-demo.html")
               (println "🌐 Open in browser: file://$(pwd)/graindisplay-demo.html"))
      
      "terminal" (do
                   (println "🌾 Generating terminal colors...")
                   (spit "terminal-colors.txt" (generate-terminal-colors))
                   (println "✅ Terminal colors saved to: terminal-colors.txt")
                   (println "📋 Copy the colors to your terminal emulator settings"))
      
      "css" (do
              (println "🌾 Generating CSS variables...")
              (spit "graindisplay-vars.css" (generate-css-vars))
              (println "✅ CSS variables saved to: graindisplay-vars.css")
              (println "📋 Include this file in your projects"))
      
      "all" (do
              (println "🌾 Generating all local theme files...")
              (spit "graindisplay-demo.html" (generate-html-demo))
              (spit "terminal-colors.txt" (generate-terminal-colors))
              (spit "graindisplay-vars.css" (generate-css-vars))
              (println "✅ All files generated!")
              (println "🌐 Open demo: file://$(pwd)/graindisplay-demo.html"))
      
      "help" (do
               (println "🌾 GrainDisplay Local Theme Applicator")
               (println "")
               (println "USAGE:")
               (println "  bb apply-local.bb demo      Generate HTML demo")
               (println "  bb apply-local.bb terminal  Generate terminal colors")
               (println "  bb apply-local.bb css       Generate CSS variables")
               (println "  bb apply-local.bb all       Generate all files")
               (println "  bb apply-local.bb help      Show this help"))
      
      (do
        (println "🌾 GrainDisplay Local Theme Applicator")
        (println "Use 'bb apply-local.bb help' for usage information")))))

(main)
