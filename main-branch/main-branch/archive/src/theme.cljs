(ns robotic-farm.theme
  "Theme system with Solarized colors for dark/light mode
   Sacred technology consciousness with Eastern philosophy"
  (:require [clojure.string :as str]))

;; Solarized color palette (Ethan Schoonover)
;; Inspired by natural light cycles and consciousness
(def solarized
  {:base03  "#002b36"  ; darkest background
   :base02  "#073642"  ; dark background
   :base01  "#586e75"  ; content tones
   :base00  "#657b83"  ; body text dark
   :base0   "#839496"  ; body text light
   :base1   "#93a1a1"  ; comments light
   :base2   "#eee8d5"  ; light background
   :base3   "#fdf6e3"  ; lightest background
   :yellow  "#b58900"
   :orange  "#cb4b16"
   :red     "#dc322f"
   :magenta "#d33682"
   :violet  "#6c71c4"
   :blue    "#268bd2"
   :cyan    "#2aa198"
   :green   "#859900"})

(defn light-theme
  "Light theme inspired by daylight consciousness
   Following natural circadian rhythms"
  []
  {:bg         (:base3 solarized)
   :bg-alt     (:base2 solarized)
   :fg         (:base00 solarized)
   :fg-alt     (:base01 solarized)
   :emphasis   (:base03 solarized)
   :border     (:base1 solarized)
   :accent     (:blue solarized)
   :accent-alt (:cyan solarized)
   :success    (:green solarized)
   :warning    (:yellow solarized)
   :error      (:red solarized)
   :quote      (:magenta solarized)
   :badge      (:violet solarized)})

(defn dark-theme
  "Dark theme inspired by night consciousness
   Moon phases and Ch'an meditation"
  []
  {:bg         (:base03 solarized)
   :bg-alt     (:base02 solarized)
   :fg         (:base0 solarized)
   :fg-alt     (:base1 solarized)
   :emphasis   (:base3 solarized)
   :border     (:base01 solarized)
   :accent     (:blue solarized)
   :accent-alt (:cyan solarized)
   :success    (:green solarized)
   :warning    (:yellow solarized)
   :error      (:red solarized)
   :quote      (:magenta solarized)
   :badge      (:violet solarized)})

(defn css-variables
  "Generate CSS custom properties for theme
   Way of Ch'an: form follows consciousness"
  [theme-map]
  (str/join "\n"
    (map (fn [[k v]]
           (str "    --color-" (name k) ": " v ";"))
         theme-map)))

(defn generate-theme-css
  "Generate complete theme CSS with light/dark modes
   Aristotelian balance of opposites in harmony"
  []
  (str
    ":root {\n"
    "  /* Light theme (default) */\n"
    (css-variables (light-theme))
    "\n}\n\n"
    ":root[data-theme=\"dark\"] {\n"
    "  /* Dark theme (night consciousness) */\n"
    (css-variables (dark-theme))
    "\n}\n\n"
    "/* Smooth theme transitions */\n"
    "* {\n"
    "  transition: background-color 0.3s ease,\n"
    "              color 0.3s ease,\n"
    "              border-color 0.3s ease;\n"
    "}\n"))

(defn theme-toggle-component
  "Generate Svelte theme toggle component
   Following I Ching principles of transformation"
  []
  {:filename "ThemeToggle.svelte"
   :content
   (str
     "<script>\n"
     "  import { onMount } from 'svelte';\n"
     "  \n"
     "  let theme = 'light';\n"
     "  \n"
     "  // Get initial theme from localStorage or system\n"
     "  onMount(() => {\n"
     "    const stored = localStorage.getItem('theme');\n"
     "    if (stored) {\n"
     "      theme = stored;\n"
     "    } else if (window.matchMedia(\n"
     "      '(prefers-color-scheme: dark)').matches) {\n"
     "      theme = 'dark';\n"
     "    }\n"
     "    applyTheme();\n"
     "  });\n"
     "  \n"
     "  // Toggle between light and dark\n"
     "  // Yang (light) ←→ Yin (dark)\n"
     "  function toggleTheme() {\n"
     "    theme = theme === 'light' ? 'dark' : 'light';\n"
     "    applyTheme();\n"
     "  }\n"
     "  \n"
     "  // Apply theme to document root\n"
     "  function applyTheme() {\n"
     "    document.documentElement\n"
     "      .setAttribute('data-theme', theme);\n"
     "    localStorage.setItem('theme', theme);\n"
     "  }\n"
     "</script>\n\n"
     "<button \n"
     "  class=\"theme-toggle\"\n"
     "  on:click={toggleTheme}\n"
     "  aria-label=\"Toggle theme\"\n"
     "  title=\"{theme === 'light' ? \n"
     "    'Switch to dark mode' : \n"
     "    'Switch to light mode'}\">\n"
     "  {#if theme === 'light'}\n"
     "    🌙 Dark Mode\n"
     "  {:else}\n"
     "    ☀️ Light Mode\n"
     "  {/if}\n"
     "</button>\n\n"
     "<style>\n"
     "  .theme-toggle {\n"
     "    position: fixed;\n"
     "    top: 1rem;\n"
     "    right: 1rem;\n"
     "    background: var(--color-bg-alt);\n"
     "    color: var(--color-fg);\n"
     "    border: 2px solid var(--color-border);\n"
     "    padding: 0.5rem 1rem;\n"
     "    border-radius: 0.5rem;\n"
     "    cursor: pointer;\n"
     "    font-family: 'Times New Roman', serif;\n"
     "    font-size: 0.9rem;\n"
     "    z-index: 1000;\n"
     "    box-shadow: 0 2px 8px rgba(0,0,0,0.1);\n"
     "  }\n"
     "  \n"
     "  .theme-toggle:hover {\n"
     "    background: var(--color-accent);\n"
     "    color: var(--color-bg);\n"
     "    border-color: var(--color-accent);\n"
     "    transform: translateY(-2px);\n"
     "    box-shadow: 0 4px 12px rgba(0,0,0,0.2);\n"
     "  }\n"
     "  \n"
     "  .theme-toggle:active {\n"
     "    transform: translateY(0);\n"
     "  }\n"
     "</style>")})

(defn philosophy-quote
  "Sacred quotes rotating with theme
   Confucius, I Ching, Ch'an Buddhism"
  [theme-mode]
  (if (= theme-mode "light")
    "\"The Way is hidden in the light of day\" - Ch'an"
    "\"In darkness, consciousness awakens\" - I Ching"))

