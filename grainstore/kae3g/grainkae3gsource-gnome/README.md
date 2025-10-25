# GNOME Configuration for Framework 16

**Created**: 12025-10-22--CDT  
**Purpose**: GNOME 46 configuration for Ubuntu 24.04 LTS  
**Location**: Central Illinois forest (Starlink + cellular tethering)

---

## Overview

This directory contains GNOME configuration scripts and settings optimized for:
- 🖥️ **Framework 16 laptop** (2560x1600@165Hz display)
- 🌙 **Warm lighting** (2000K-2500K color temperature)
- 📱 **Dual-wifi management** (Starlink + cellular tethering)
- ⌨️ **Keyboard-driven workflow** (Super key shortcuts)

---

## Quick Setup

```bash
# Run all GNOME configuration at once
cd grainstore/grainsource-gnome
bash configure-gnome.sh
```

---

## Manual Configuration

### 1. Night Light (Warm Lighting)

**Enable GNOME Night Light** with warm color temperature:

```bash
# Enable Night Light
gsettings set org.gnome.settings-daemon.plugins.color night-light-enabled true

# Set to manual schedule (always on)
gsettings set org.gnome.settings-daemon.plugins.color night-light-schedule-automatic false

# Set warm temperature (2000K-2500K equivalent)
# Scale: 1.0 (warmest) to 0.0 (coolest)
gsettings set org.gnome.settings-daemon.plugins.color night-light-temperature 2000

# Optional: Set schedule times (if you want it automatic)
gsettings set org.gnome.settings-daemon.plugins.color night-light-schedule-from 20.0  # 8 PM
gsettings set org.gnome.settings-daemon.plugins.color night-light-schedule-to 6.0     # 6 AM
```

**GUI Method**:
1. Open **Settings** → **Displays** → **Night Light**
2. Toggle **Night Light** to ON
3. Set **Color Temperature** slider all the way to the left (warmest)
4. Choose **Manual Schedule** or set custom times

---

### 2. Display Settings

```bash
# Set display scaling (1.0 = no scaling for 2560x1600)
gsettings set org.gnome.desktop.interface scaling-factor 1

# Set text scaling for better readability (optional)
gsettings set org.gnome.desktop.interface text-scaling-factor 1.0
```

---

### 3. Dark Theme

```bash
# Enable dark theme
gsettings set org.gnome.desktop.interface gtk-theme 'Adwaita-dark'
gsettings set org.gnome.desktop.interface color-scheme 'prefer-dark'

# Set dark shell theme
gsettings set org.gnome.shell.extensions.user-theme name 'Adwaita-dark'
```

---

### 4. Keyboard Shortcuts (Super Key)

```bash
# Set Super key for overview
gsettings set org.gnome.mutter overlay-key 'Super_L'

# Terminal shortcut (Super+Return)
gsettings set org.gnome.settings-daemon.plugins.media-keys custom-keybindings "['/org/gnome/settings-daemon/plugins/media-keys/custom-keybindings/custom0/']"
gsettings set org.gnome.settings-daemon.plugins.media-keys.custom-keybinding:/org/gnome/settings-daemon/plugins/media-keys/custom-keybindings/custom0/ name 'Terminal'
gsettings set org.gnome.settings-daemon.plugins.media-keys.custom-keybinding:/org/gnome/settings-daemon/plugins/media-keys/custom-keybindings/custom0/ command 'gnome-terminal'
gsettings set org.gnome.settings-daemon.plugins.media-keys.custom-keybinding:/org/gnome/settings-daemon/plugins/media-keys/custom-keybindings/custom0/ binding '<Super>Return'
```

---

### 5. Window Management

```bash
# Enable window tiling
gsettings set org.gnome.mutter edge-tiling true

# Enable workspaces on all displays
gsettings set org.gnome.mutter workspaces-only-on-primary false

# Set number of workspaces
gsettings set org.gnome.desktop.wm.preferences num-workspaces 4

# Focus follows mouse (optional)
gsettings set org.gnome.desktop.wm.preferences focus-mode 'sloppy'

# Disable animations for performance (optional)
gsettings set org.gnome.desktop.interface enable-animations false
```

---

### 6. Power Management (for Forest Use)

```bash
# Dim screen when inactive
gsettings set org.gnome.settings-daemon.plugins.power idle-dim true

# Set idle delay to 10 minutes
gsettings set org.gnome.desktop.session idle-delay 600

# Never suspend on AC power (for long builds)
gsettings set org.gnome.settings-daemon.plugins.power sleep-inactive-ac-type 'nothing'

# Suspend on battery after 30 minutes
gsettings set org.gnome.settings-daemon.plugins.power sleep-inactive-battery-timeout 1800
```

---

### 7. Font Settings

```bash
# Set monospace font for terminal and code
gsettings set org.gnome.desktop.interface monospace-font-name 'DejaVu Sans Mono 11'

# Set document font
gsettings set org.gnome.desktop.interface document-font-name 'DejaVu Sans 11'

# Set window title font
gsettings set org.gnome.desktop.wm.preferences titlebar-font 'DejaVu Sans Bold 11'
```

---

### 8. Touchpad Settings

```bash
# Enable tap-to-click
gsettings set org.gnome.desktop.peripherals.touchpad tap-to-click true

# Enable natural scrolling
gsettings set org.gnome.desktop.peripherals.touchpad natural-scroll true

# Enable two-finger scrolling
gsettings set org.gnome.desktop.peripherals.touchpad two-finger-scrolling-enabled true

# Middle-click emulation (three-finger tap)
gsettings set org.gnome.desktop.peripherals.touchpad click-method 'fingers'
```

---

## GNOME Extensions (Optional)

### Recommended Extensions for Grain Workflow

Install **GNOME Extensions** app:

```bash
sudo apt install gnome-shell-extensions gnome-shell-extension-manager
```

**Useful Extensions**:
- **Dash to Dock**: macOS-style dock
- **Caffeine**: Prevent screen from sleeping
- **OpenWeather**: Weather in top bar (useful for Starlink planning!)
- **Clipboard Indicator**: Clipboard history
- **Sound Input & Output Device Chooser**: Quick audio switching

Install via:
```bash
# Enable extensions
gnome-extensions enable dash-to-dock@micxgx.gmail.com
gnome-extensions enable caffeine@patapon.info
```

Or install from: https://extensions.gnome.org/

---

## GrainWiFi Integration

**Dual-WiFi Management for Forest Environment**:

```bash
# Test GrainWiFi
cd ../grainwifi
bb grainwifi:test

# Check connection status
bb grainwifi:status

# Manually switch between connections
bb grainwifi:switch starlink   # Switch to Starlink
bb grainwifi:switch cellular   # Switch to cellular
bb grainwifi:switch auto       # Use best available
```

See `../grainwifi/README.md` for full documentation.

---

## Restoration Script

**Restore all GNOME settings at once**:

```bash
#!/usr/bin/env bash
# grainstore/grainsource-gnome/configure-gnome.sh

echo "🌾 Configuring GNOME for Grain Network Development"
echo "════════════════════════════════════════════════════"

# Night Light (2000K warm)
echo "🌙 Configuring Night Light..."
gsettings set org.gnome.settings-daemon.plugins.color night-light-enabled true
gsettings set org.gnome.settings-daemon.plugins.color night-light-temperature 2000

# Dark theme
echo "🎨 Enabling dark theme..."
gsettings set org.gnome.desktop.interface gtk-theme 'Adwaita-dark'
gsettings set org.gnome.desktop.interface color-scheme 'prefer-dark'

# Display
echo "🖥️  Configuring display..."
gsettings set org.gnome.desktop.interface scaling-factor 1
gsettings set org.gnome.desktop.interface text-scaling-factor 1.0

# Touchpad
echo "👆 Configuring touchpad..."
gsettings set org.gnome.desktop.peripherals.touchpad tap-to-click true
gsettings set org.gnome.desktop.peripherals.touchpad natural-scroll true

# Window management
echo "🪟 Configuring window management..."
gsettings set org.gnome.mutter edge-tiling true
gsettings set org.gnome.mutter workspaces-only-on-primary false

# Power management
echo "⚡ Configuring power management..."
gsettings set org.gnome.settings-daemon.plugins.power sleep-inactive-ac-type 'nothing'
gsettings set org.gnome.desktop.session idle-delay 600

echo ""
echo "✅ GNOME configuration complete!"
echo ""
echo "📱 Next steps:"
echo "   1. Configure GrainWiFi: cd ../grainwifi && bb grainwifi:status"
echo "   2. Test Night Light: Check top-right menu → Night Light"
echo "   3. Customize: Settings → Appearance, Keyboard, etc."
echo ""
```

---

## Comparison: Sway vs. GNOME

| Feature | Sway | GNOME |
|---------|------|-------|
| **Network Manager** | nmcli (manual) | GUI + automatic switching ✅ |
| **Warm Lighting** | wlsunset/gammastep | Built-in Night Light ✅ |
| **Setup Complexity** | High configuration | Simple GUI settings ✅ |
| **Resource Usage** | Very low | Moderate |
| **Customization** | Infinite | Good enough |
| **Stability** | Excellent | Excellent ✅ |

**Why GNOME wins for forest development**:
- Less time configuring, more time coding
- Better network management for intermittent connections
- Ubuntu 24.04 LTS ships with GNOME 46 (well-tested)
- Familiar to collaborators

---

## Troubleshooting

### Night Light not changing color

```bash
# Check if Night Light is enabled
gsettings get org.gnome.settings-daemon.plugins.color night-light-enabled

# Reset and re-enable
gsettings reset org.gnome.settings-daemon.plugins.color night-light-temperature
gsettings set org.gnome.settings-daemon.plugins.color night-light-temperature 2000

# Restart GNOME Shell (Alt+F2, type 'r', press Enter)
# Or log out and back in
```

### Keyboard shortcuts not working

```bash
# List all custom keybindings
gsettings get org.gnome.settings-daemon.plugins.media-keys custom-keybindings

# Reset all keybindings
gsettings reset-recursively org.gnome.settings-daemon.plugins.media-keys
```

### Display scaling issues

```bash
# For fractional scaling (experimental)
gsettings set org.gnome.mutter experimental-features "['scale-monitor-framebuffer']"

# Then set fractional scale in Settings → Displays
```

---

## Related Files

- **Sway backup**: `../grainsource-sway/README.md`
- **GrainWiFi**: `../grainwifi/README.md`
- **Dual-wifi setup**: `../grainwifi/bb.edn`

---

## Philosophy

**"THE WHOLE GRAIN"** - Choosing GNOME over Sway is a decision to prioritize **mission over aesthetics**. Every minute spent configuring window managers is a minute not spent building the Grain Network. GNOME's reliability and built-in tools allow focus on what matters: decentralized computing for sustainability and sovereignty.

**"Making a wave and surfing the same wave"** - GNOME's mature ecosystem provides the stable foundation (the wave) upon which we build GrainWiFi and other tools (surfing).

---

## License

MIT License - Part of the Grain Network ecosystem.

