(ns kae3g.generator-v2
  "Generate Svelte components from robotic farm
   DSL structures with theme-aware consciousness
   Refactored for better maintainability"
  (:require [clojure.string :as str]
            [babashka.fs :as fs]
            [kae3g.generator :as gen]))

;; Re-export main functions with theme awareness
(def sanitize-component-name gen/sanitize-component-name)
(def generate-consciousness-badges gen/generate-consciousness-badges)
(def write-svelte-components gen/write-svelte-components)

;; Theme system is now integrated in generator.clj
;; This file serves as documentation of the refactoring

(comment
  "Theme System Architecture:
   
   1. CLJS Logic (src/kae3g/theme.cljs)
      - Solarized color definitions
      - Light/dark theme functions
      - CSS variable generation
      - Philosophy quotes
   
   2. Generator Integration (src/kae3g/generator.clj)
      - generate-theme-toggle function
      - Theme-aware component styles
      - CSS custom properties
   
   3. Global Styles (web-app/src/app.css)
      - Root CSS variables
      - Light/dark theme definitions
      - Smooth transitions
      - Typography enhancements
   
   4. Build Process
      - bb build:pipeline generates components
      - Theme CSS injected into all components
      - ThemeToggle.svelte created automatically
      - Index page includes theme toggle
   
   Philosophy:
   - Ch'an Buddhism: Light/dark duality
   - I Ching: Transformation principles
   - Confucius: Harmony and balance
   - Aristotle: Form follows function")

