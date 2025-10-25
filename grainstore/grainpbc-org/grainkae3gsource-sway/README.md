# Sway Configuration Archive

**Created**: 12025-10-22  
**Purpose**: Archive Sway/Wayland configuration before switching to GNOME  
**Location**: Central Illinois forest (cellular tethering + Starlink)

---

## Why GNOME Instead of Sway?

**Reasons for switching back to Ubuntu 24.04 LTS GNOME**:
- ✅ **Better network management**: GNOME's Network Manager handles intermittent cellular tethering and Starlink switching more reliably
- ✅ **Simpler setup**: Less configuration required for basic functionality
- ✅ **Warm lighting built-in**: GNOME Night Light is easier to configure than wlsunset/gammastep
- ✅ **Familiar workflow**: Reduces cognitive overhead while working on Grain Network development

**What we're keeping from Sway**:
- 🌙 **Warm lighting preferences** (2000K-2500K color temperature)
- ⌨️ **Keyboard shortcuts** (Super key as modifier)
- 🎨 **Minimal aesthetic** (dark themes, clean interface)
- 🖥️ **Display settings** (2560x1600@165Hz Framework 16 display)

---

## Archived Sway Configuration

### Main Sway Config

**Original location**: `~/.config/sway/config`  
**Backed up from**: `configs/sway/config`

**Key features**:
```
- Modifier: Super (Mod4) key
- Terminal: foot
- Launcher: wofi
- Font: DejaVu Sans Mono 20, Noto Color Emoji 20
- Display: 2560x1600@165Hz, no scaling
- Warm lighting: gammastep -O 2000
- Red-themed status bar
```

**Custom keybindings**:
```
Super+Ctrl+r  - Red-green mode
Super+Ctrl+c  - Cyberpunk mode
Super+Ctrl+m  - Monochrome mode
Super+Ctrl+n  - Normal colors
Super+Ctrl+-  - Night light 2000K
Super+Ctrl+=  - Night light 2500K
```

### Waybar Configuration

**Original location**: `~/.config/waybar/`

Custom status bar configuration (if it existed).

---

## Sway Display Scripts

**Location**: `~/kae3g/12025-10/scripts/display-config.bb`

Babashka scripts for display mode switching:
- `red-green-mode` - Accessibility mode
- `cyberpunk-mode` - High contrast aesthetic
- `monochrome-mode` - E-ink-style display
- `normal-colors` - Standard color profile
- `night-light <temp>` - Adjustable warm lighting

---

## Future Sway Use Cases

**When we might return to Sway**:
- 📱 **Graindroid Phone development** - Wayland compositor knowledge
- 🖥️ **Custom embedded systems** - Lightweight window manager
- 🌊 **Alpine Linux builds** - Minimal Wayland setup
- 🔬 **Testing display modes** - Advanced color management

---

## GNOME Migration Checklist

- [x] Save Sway configuration to `grainstore/grainsource-sway/`
- [x] Document reasons for switch
- [ ] Configure GNOME Night Light (2000K-2500K warm lighting)
- [ ] Set up GNOME keyboard shortcuts (Super key shortcuts)
- [ ] **Priority**: Implement dual-wifi utility for Network Manager
- [ ] Test dual-wifi switching (cellular tethering ↔ Starlink)
- [ ] Configure GNOME window management preferences
- [ ] Set up GNOME dark theme
- [ ] Test Framework 16 display settings in GNOME

---

## Dual-WiFi Priority

**Critical need**: Forest environment with intermittent connections requires:
- 📱 **Cellular tethering** (primary when Starlink unavailable)
- 🛰️ **Starlink** (preferred when available)
- 🔄 **Automatic switching** based on connection quality
- 🔔 **Connection status notifications**

**Solution**: Implementing `grainwifi` - dual-wifi utility for GNOME Network Manager

---

## References

- **Sway documentation**: https://swaywm.org/
- **Wayland protocols**: https://wayland.freedesktop.org/
- **GNOME Night Light**: https://help.gnome.org/users/gnome-help/stable/display-night-light.html
- **NetworkManager**: https://networkmanager.dev/

---

## Notes

**Personal context**:
- Working in Central Illinois forest
- Framework 16 laptop as primary development machine
- Ubuntu 24.04 LTS GNOME as base OS
- Focus on Grain Network development over system configuration

**Philosophy**:
"THE WHOLE GRAIN" - choosing simplicity and reliability over customization when it serves the greater mission. Sway was beautiful, but GNOME's network management is more important right now for continuous development in our forest workspace.

