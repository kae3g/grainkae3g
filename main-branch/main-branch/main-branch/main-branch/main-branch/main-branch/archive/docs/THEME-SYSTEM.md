# Theme System Documentation
**Timestamp:** `12025-10-04--05thhouse01992`

## Overview

A complete dark/light mode theme system built with ClojureScript (CLJS) and Svelte, using the Solarized color palette. Inspired by Eastern philosophy and classical Western thought.

## Architecture

### 1. CLJS Theme Logic (`src/robotic_farm/theme.cljs`)

```clojure
(ns robotic-farm.theme
  "Theme system with Solarized colors
   Sacred technology consciousness")

- Solarized color palette definitions
- Light theme (daylight consciousness)
- Dark theme (night consciousness/Ch'an meditation)
- CSS variable generation
- Philosophy quotes system
```

### 2. Generator Integration (`src/robotic_farm/generator.clj`)

- `generate-theme-toggle` - Creates Svelte component
- Theme-aware CSS in all components
- Automatic theme toggle inclusion
- localStorage persistence
- System preference detection

### 3. Global Styles (`web-app/src/app.css`)

- CSS custom properties (`:root`)
- Light/dark theme definitions
- Smooth 0.3s transitions
- Typography enhancements
- Accessibility (prefers-reduced-motion)

### 4. Svelte Components

**ThemeToggle.svelte** (auto-generated):
- Fixed position (top-right)
- 🌙/☀️ icons
- Smooth hover effects
- Accessible (aria-label, title)

**Index.svelte** (theme-aware):
- Imports ThemeToggle
- Philosophy quote display
- Theme-aware colors

## Color Palette (Solarized)

### Light Theme
- Background: `#fdf6e3` (base3)
- Foreground: `#657b83` (base00)
- Accent: `#268bd2` (blue)
- Quote: `#d33682` (magenta)

### Dark Theme  
- Background: `#002b36` (base03)
- Foreground: `#839496` (base0)
- Accent: `#268bd2` (blue)
- Quote: `#d33682` (magenta)

## Usage

### Building

```bash
bb build:pipeline   # Single document
bb build:full-site  # All documents + theme
```

### Features

1. **Automatic Detection**
   - Checks localStorage first
   - Falls back to system preference
   - Persists user choice

2. **Smooth Transitions**
   - 0.3s ease for all color changes
   - Respects prefers-reduced-motion
   - No layout shift

3. **Philosophy Integration**
   - Ch'an Buddhism: Light/dark duality
   - I Ching: Transformation (yang ←→ yin)
   - Confucius: Harmony and balance
   - Aristotle: Form follows consciousness

## Files Created

1. `src/robotic_farm/theme.cljs` - CLJS theme logic
2. `web-app/src/app.css` - Global theme styles
3. `web-app/src/routes/ThemeToggle.svelte` - Toggle component (generated)
4. Updated `src/robotic_farm/generator.clj` - Theme integration

## Implementation Status

✅ CLJS theme system
✅ Solarized color palette
✅ Dark/light mode toggle
✅ Component generation
✅ Global CSS variables
✅ localStorage persistence
✅ System preference detection
✅ Smooth transitions
✅ Accessibility features
✅ Philosophy quotes

## Next Steps

- Add more color themes (Daylight Computer, f.lux inspired)
- Implement time-based auto-switching
- Add Eastern philosophy content
- Create theme customization panel

---

*"The Way is hidden in light, revealed in darkness"* - Ch'an Buddhism
