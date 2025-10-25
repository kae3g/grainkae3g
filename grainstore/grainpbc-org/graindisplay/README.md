# 🌾 GrainDisplay

**Bedtime Warm Theme for GNOME**

A Flux-like blue light filter application with graintime scheduling, built with Clojure Humble UI and s6 supervision.

---

## 🎯 Overview

GrainDisplay is a native desktop application that provides a warm, bedtime-friendly color theme for GNOME. It features:

- **Flux-like Functionality**: Automatically adjusts color temperature based on time of day
- **Graintime Integration**: Uses neovedic timestamps for scheduling
- **GNOME Integration**: Native s6 service with GNOME Shell extension
- **Humble UI**: Cross-platform native UI built with Clojure
- **SvelteKit-inspired Design**: Modern, responsive component system
- **AppImage Distribution**: Single-file deployment for any Linux distro

---

## 🌙 Features

### Automatic Color Temperature Adjustment
- **Sunrise → Sunset**: Gradual transition from 6500K (daylight) to 1000K (warm)
- **Graintime Scheduling**: Uses astronomical calculations for your location
- **Custom Schedules**: Override automatic times with manual offsets

### Warm Color Palette
- **Warm Red**: `#D2691E` (Chocolate)
- **Deep Orange**: `#FF8C00` (Dark Orange)
- **Warm Black**: `#1A0F0A` (Background)
- **Warm White**: `#FFF8DC` (Text)
- **Warm Gold**: `#DAA520` (Accents)

### GNOME Integration
- **s6 Service**: Supervised background daemon
- **GNOME Shell Extension**: System tray integration
- **GNOME Settings Schema**: Native settings integration
- **Autostart Support**: Launches with GNOME session

### Display Controls
- **Brightness**: Adjust display brightness (0.1-1.0)
- **Color Temperature**: Manual override (1000K-10000K)
- **Gamma Correction**: Fine-tune color curves
- **Transition Speed**: Customize fade duration

---

## 📦 Installation

### Option 1: AppImage (Recommended)

```bash
# Download the AppImage
wget https://github.com/grainpbc/graindisplay/releases/latest/download/GrainDisplay-1.0.0-x86_64.AppImage

# Make it executable
chmod +x GrainDisplay-1.0.0-x86_64.AppImage

# Run it
./GrainDisplay-1.0.0-x86_64.AppImage
```

### Option 2: Build from Source

```bash
# Clone the repository
cd grainstore/graindisplay

# Install dependencies
bb scripts/build-appimage.bb deps

# Build native image
bb scripts/build-appimage.bb native

# Create AppImage
bb scripts/build-appimage.bb appimage

# Or do all steps at once
bb scripts/build-appimage.bb all
```

### Option 3: GNOME Integration (for system-wide install)

```bash
# Install GNOME integration
clojure -M -m graindisplay.gnome-service install

# Start the service
s6-svc -u /etc/s6/sv/graindisplay

# Or use systemd user service
systemctl --user enable graindisplay
systemctl --user start graindisplay
```

---

## 🚀 Usage

### Desktop Application

Launch the app to see the main control panel:

```bash
./GrainDisplay-1.0.0-x86_64.AppImage
```

The main window provides:
- Current time and graintime display
- Theme preview
- Brightness slider
- Color temperature control
- Schedule toggle
- Color palette selection

### Command Line

Apply theme manually:

```bash
# Apply bedtime theme
bb apply-local.bb all

# Open demo in browser
xdg-open graindisplay-demo.html

# Apply to terminal
cat terminal-colors.txt
```

### Automatic Scheduling

GrainDisplay uses your location (configurable via graintime `gt config`) to calculate sunrise and sunset times. The color temperature transitions automatically:

- **Sunrise**: Starts at 1000K (warm), transitions to 6500K (daylight)
- **Midday**: Maintains 6500K for optimal daytime viewing
- **Sunset**: Transitions from 6500K back to 1000K (warm bedtime)
- **Night**: Maintains 1000K for comfortable evening use

---

## 🛠️ Development

### Project Structure

```
graindisplay/
├── src/
│   └── graindisplay/
│       ├── core.clj              # Main application logic
│       ├── gnome_service.clj     # GNOME integration
│       └── svelte_ui.clj         # SvelteKit-inspired UI components
├── scripts/
│   ├── build-appimage.bb         # AppImage build script
│   ├── apply-local.bb            # Local theme applicator
│   └── apply-theme.bb            # Theme installation
├── config/
│   └── graindisplay.edn          # Configuration template
├── deps.edn                      # Clojure dependencies
├── bb.edn                        # Babashka tasks
└── README.md                     # This file
```

### Dependencies

- **Clojure**: 1.11.1+
- **Humble UI**: 0.10.0 (native cross-platform UI)
- **JWM**: 0.3.0 (window management)
- **clojure-s6**: Local (process supervision)
- **graintime**: Local (neovedic timestamps)
- **GraalVM**: 22.3.0+ (for native compilation)
- **AppImageTool**: For AppImage creation

### Building

```bash
# Run in development mode
clojure -M:dev -m graindisplay.core

# Build native image with GraalVM
native-image --no-fallback -jar target/graindisplay.jar graindisplay

# Create AppImage
bb scripts/build-appimage.bb appimage

# Export SvelteKit components for web
clojure -M -m graindisplay.svelte-ui export ./web-export
```

### Testing

```bash
# Test theme application
bb apply-local.bb demo

# Test GNOME service
clojure -M -m graindisplay.gnome-service status

# Test graintime integration
gt grainpath course kae3g graindisplay
```

---

## 🎨 Configuration

Configuration file: `~/.config/graindisplay/config.edn`

```clojure
{:theme {:warm-red "#D2691E"
         :deep-orange "#FF8C00"
         :warm-black "#1A0F0A"
         :warm-white "#FFF8DC"
         :warm-gold "#DAA520"}
 
 :schedule {:sunrise-offset 0        ; Minutes before/after sunrise
            :sunset-offset 0         ; Minutes before/after sunset
            :transition-duration 30  ; Minutes for smooth transition
            :enabled true}
 
 :location {:latitude 37.9735
            :longitude -122.5311
            :timezone "America/Los_Angeles"}
 
 :display {:brightness 1.0
           :gamma 1.0
           :temperature 6500}
 
 :gnome {:enabled true
         :auto-start true}}
```

### Location Configuration

Use `gt config` to set your location for accurate sunrise/sunset times:

```bash
gt config
# Enter your location (e.g., San Rafael, CA)
```

Or edit the graintime config directly: `~/.config/graintime/config.edn`

---

## 🌐 Web Export

GrainDisplay can export SvelteKit-compatible components for web deployment:

```bash
# Export all components
clojure -M -m graindisplay.svelte-ui export ./web-export

# Use in your SvelteKit project
cp web-export/GrainDisplay.svelte your-project/src/lib/
cp web-export/graindisplay-variables.css your-project/static/
```

---

## 📝 Technical Details

### Color Temperature Algorithm

GrainDisplay uses a simplified color temperature model:

```clojure
(defn set-color-temperature [temperature]
  (let [red (if (< temperature 6600) 1.0 
                (max 0.0 (- 1.0 (* 0.0001 (- temperature 6600)))))
        green (if (< temperature 6600)
                 (min 1.0 (+ 0.4 (* 0.0001 (- temperature 1000))))
                 (min 1.0 (- 1.0 (* 0.0001 (- temperature 6600)))))
        blue (if (< temperature 2000) 0.0
               (min 1.0 (* 0.0001 (- temperature 2000))))]
    (set-gamma red green blue)))
```

### Sun Time Calculations

Uses graintime's astronomical calculations for accurate sunrise/sunset:

```clojure
(defn get-sun-times [lat lon]
  (graintime.solar/calculate-sun-times 
    (time/local-date-time) 
    lat 
    lon))
```

### Display Control

Uses `xrandr` for gamma and brightness control:

```bash
# Set gamma
xrandr --output $OUTPUT --gamma 1.0:0.8:0.6

# Set brightness
xrandr --output $OUTPUT --brightness 0.8
```

---

## 🤝 Contributing

Contributions are welcome! Please see the main Grain Network contributing guidelines.

### Development Workflow

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test with `bb apply-local.bb demo`
5. Submit a pull request

### Coding Standards

- Follow Clojure style guide
- Use SvelteKit-inspired component naming
- Document all public functions
- Include docstrings with examples

---

## 📄 License

MIT License - See LICENSE file for details

---

## 🌾 Grain Network

GrainDisplay is part of the [Grain Network](https://github.com/grainpbc) ecosystem.

**From granules to grains to THE WHOLE GRAIN**

---

## 🙏 Acknowledgments

- **f.lux**: Inspiration for blue light filtering
- **Redshift**: GNOME color adjustment tool
- **Humble UI**: Cross-platform Clojure UI framework
- **SvelteKit**: Design system inspiration
- **s6**: Process supervision
- **Graintime**: Neovedic timestamp system

---

## 📚 See Also

- [Graintime Documentation](../graintime/README.md)
- [Clojure S6 Documentation](../clojure-s6/README.md)
- [GNOME Shell Extensions](https://extensions.gnome.org/)
- [Humble UI Documentation](https://github.com/HumbleUI/HumbleUI)

---

**Created:** October 23, 2025  
**Author:** kae3g (Grain PBC)  
**Version:** 1.0.0