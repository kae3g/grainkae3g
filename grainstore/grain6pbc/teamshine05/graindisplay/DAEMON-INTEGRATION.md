# 🌾 GrainDisplay + GrainDaemon Integration

**Complete Display Warmth Management System**

---

## 🎯 Architecture

The GrainDisplay system now uses a two-layer architecture:

```
┌─────────────────────────────────────────┐
│   GrainDisplay UI (Humble UI)           │  ← User interface, sliders, controls
│   - Temperature slider                   │
│   - Brightness control                   │
│   - Preset buttons                       │
│   - Graintime display                    │
└──────────────┬──────────────────────────┘
               │
               │ Reflects settings from:
               ↓
┌─────────────────────────────────────────┐
│   GrainDaemon Display Module            │  ← Background daemon, manages GNOME
│   - Monitors & maintains warm settings  │
│   - Direct xrandr gamma control          │
│   - GNOME gsettings integration          │
│   - Auto time-based scheduling           │
└──────────────┬──────────────────────────┘
               │
               │ Controls:
               ↓
┌─────────────────────────────────────────┐
│   GNOME Display Settings                │  ← Actual display hardware
│   - xrandr gamma (R=0.3, G=0.15, B=0.05) │
│   - Night Light (1000K)                  │
│   - Dark theme                           │
└─────────────────────────────────────────┘
```

---

## 🚀 Quick Start

### Apply Warm Theme Right Now

```bash
# Direct GNOME control (immediate effect)
cd /home/xy/kae3g/grainkae3g/grainstore/graindisplay
bb scripts/gnome-warm-direct.bb apply
```

Your screen should turn **very warm orange** within 1 second!

### Start Background Daemon

```bash
# Start persistent daemon
cd /home/xy/kae3g/grainkae3g/grainstore/graindaemon
bb scripts/graindisplay-daemon.bb start &
```

The daemon will:
- Keep settings applied even if GNOME resets them
- Auto-adjust based on time of day (if enabled)
- Reapply every 5 minutes to prevent drift

---

## 🔧 How It Works

### Method 1: Direct xrandr Gamma (Most Effective)

```bash
xrandr --output eDP-2 --gamma 0.3:0.15:0.05
```

This **directly controls the display gamma correction**:
- **Red**: 0.3 (reduced to 30%)
- **Green**: 0.15 (reduced to 15%)
- **Blue**: 0.05 (reduced to 5%)

Result: **Very warm orange-red glow** 🌅

### Method 2: GNOME Night Light

```bash
gsettings set org.gnome.settings-daemon.plugins.color night-light-enabled true
gsettings set org.gnome.settings-daemon.plugins.color night-light-temperature 1000
```

Uses GNOME's built-in night light at 1000K.

### Method 3: Dark Theme

```bash
gsettings set org.gnome.desktop.interface color-scheme 'prefer-dark'
gsettings set org.gnome.desktop.interface gtk-theme 'Adwaita-dark'
```

Sets dark theme for UI consistency with warm colors.

---

## 🎨 Presets

| Preset | Temperature | Brightness | Use Case |
|--------|-------------|------------|----------|
| **extreme** | 800K | 70% | Right before bed, maximum warmth |
| **bedtime** | 1000K | 80% | Evening work/reading (default) |
| **evening** | 2000K | 85% | Sunset hours |
| **sunset** | 3000K | 90% | Late afternoon |
| **normal** | 6500K | 100% | Standard daylight |

### Apply a Preset

```bash
# Using direct control
bb scripts/gnome-warm-direct.bb extreme

# Using daemon
bb scripts/graindisplay-daemon.bb preset bedtime
```

---

## ⏰ Automatic Scheduling

Enable time-based presets:

```bash
bb scripts/graindisplay-daemon.bb auto-on
```

Schedule:
- **9 PM - 6 AM**: extreme (800K) - Sleep prep
- **6 AM - Noon**: sunset (3000K) - Gentle morning
- **Noon - 6 PM**: evening (2000K) - Afternoon
- **6 PM - 9 PM**: bedtime (1000K) - Evening wind-down

---

## 🎮 Manual Control

### Using Direct Script

```bash
cd /home/xy/kae3g/grainkae3g/grainstore/graindisplay

# Apply presets
bb scripts/gnome-warm-direct.bb apply      # 1000K bedtime
bb scripts/gnome-warm-direct.bb extreme    # 800K super warm
bb scripts/gnome-warm-direct.bb evening    # 2000K warm
bb scripts/gnome-warm-direct.bb sunset     # 3000K gentle

# Reset
bb scripts/gnome-warm-direct.bb reset

# Check status
bb scripts/gnome-warm-direct.bb status
```

### Using Daemon

```bash
cd /home/xy/kae3g/grainkae3g/grainstore/graindaemon

# Start/stop
bb scripts/graindisplay-daemon.bb start
bb scripts/graindisplay-daemon.bb stop

# Set preset
bb scripts/graindisplay-daemon.bb preset extreme
bb scripts/graindisplay-daemon.bb preset bedtime

# Custom values
bb scripts/graindisplay-daemon.bb custom 1500 0.75  # 1500K, 75% brightness

# Status
bb scripts/graindisplay-daemon.bb status
```

---

## 📊 Current Settings

Check what's applied:

```bash
bb scripts/gnome-warm-direct.bb status
```

Output:
```
🌾 Current GrainDisplay Settings
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Display:      eDP-2
Night Light:  true
Temperature:  uint32 1000
GTK Theme:    'Adwaita-dark'
Color Scheme: 'prefer-dark'
```

---

## 🔄 Daemon Behavior

The GrainDaemon Display module:

1. **Applies settings immediately** on start
2. **Monitors every 60 seconds** for changes
3. **Reapplies every 5 minutes** to prevent GNOME from resetting
4. **Auto-schedules** based on time of day (if enabled)
5. **Maintains consistency** even after suspend/resume

### Why the Daemon?

GNOME sometimes resets display settings:
- After screensaver/lock
- After suspend/resume
- After external monitor connect/disconnect
- After GNOME Shell restart

The daemon ensures **warm settings persist** through all these events.

---

## 🖥️ UI Integration (Future)

The Humble UI app will:
- **Read** current settings from daemon state
- **Display** them in real-time (sliders, previews)
- **Send commands** to daemon when user changes settings
- **Show graintime** for astronomical scheduling

```
User moves slider → UI sends command → Daemon applies → GNOME updates → UI reflects change
```

---

## 🛠️ Configuration

Config file: `~/.config/graindaemon/display.edn`

```clojure
{:enabled true
 :temperature 1000
 :brightness 0.8
 :auto-schedule true
 :check-interval-ms 60000      ; Check every minute
 :reapply-interval-ms 300000   ; Reapply every 5 minutes
 :presets {:bedtime {:temperature 1000 :brightness 0.8}
           :extreme {:temperature 800 :brightness 0.7}
           :evening {:temperature 2000 :brightness 0.85}
           :sunset {:temperature 3000 :brightness 0.9}
           :normal {:temperature 6500 :brightness 1.0}}}
```

---

## 🧪 Testing

### Verify Gamma is Applied

```bash
xrandr --verbose | grep -A 10 "eDP-2"
```

Look for:
```
Gamma:      0.3:0.15:0.05
Brightness: 0.8
```

### Verify GNOME Settings

```bash
gsettings get org.gnome.settings-daemon.plugins.color night-light-temperature
# Should output: uint32 1000
```

### Test Daemon Persistence

```bash
# Start daemon
bb scripts/graindisplay-daemon.bb start &

# Wait 1 minute
sleep 60

# Check if still warm (daemon should have reapplied)
bb scripts/gnome-warm-direct.bb status
```

---

## 🐛 Troubleshooting

### Display Not Changing?

1. **Check your display name**:
   ```bash
   xrandr --query | grep connected
   ```
   
2. **Try manual xrandr**:
   ```bash
   xrandr --output YOUR_DISPLAY --gamma 0.3:0.15:0.05
   ```

3. **Check GNOME version**:
   ```bash
   gnome-shell --version
   ```

### Settings Keep Resetting?

Start the daemon to maintain persistence:

```bash
bb scripts/graindisplay-daemon.bb start &
```

### Want Even Warmer?

```bash
# Super extreme warm (600K)
bb scripts/graindisplay-daemon.bb custom 600 0.6
```

### Reset Everything

```bash
# Stop daemon
bb scripts/graindisplay-daemon.bb stop

# Reset display
bb scripts/gnome-warm-direct.bb reset

# Kill gammastep if running
pkill gammastep
```

---

## 📂 File Structure

```
grainstore/
├── graindisplay/               # UI and direct control
│   ├── src/graindisplay/
│   │   ├── core.clj           # Humble UI app
│   │   ├── gnome_service.clj  # GNOME integration
│   │   └── svelte_ui.clj      # SvelteKit-inspired components
│   └── scripts/
│       ├── gnome-warm-direct.bb  # Direct GNOME control ⭐
│       ├── build-simple.bb       # Simple wrapper
│       └── build-appimage.bb     # AppImage builder
│
└── graindaemon/                # Background daemon
    ├── src/graindaemon/
    │   └── display.clj        # Display warmth daemon ⭐
    └── scripts/
        └── graindisplay-daemon.bb  # Daemon launcher
```

---

## 🎯 Recommended Usage

### For Immediate Effect
```bash
bb scripts/gnome-warm-direct.bb apply
```

### For Persistent Warmth
```bash
bb scripts/graindisplay-daemon.bb start &
```

### For Auto-Scheduling
```bash
bb scripts/graindisplay-daemon.bb auto-on
```

### For GUI Control (Future)
```bash
./GrainDisplay-1.0.0-x86_64.AppImage
```

---

## 🌾 Philosophy

The GrainDisplay system embodies **layered simplicity**:

1. **Direct Control** - Immediate gratification, minimal dependencies
2. **Daemon Layer** - Persistent reliability, auto-recovery
3. **UI Layer** - Beautiful, intuitive, SvelteKit-inspired

Each layer works independently, but together they create a **robust, user-friendly system** for comfortable evening computing.

> "From granules to grains to THE WHOLE GRAIN" 🌾

---

**Version**: 1.0.0  
**Author**: kae3g (Grain PBC)  
**License**: MIT

🌙 **Sweet dreams with warm screens!**
