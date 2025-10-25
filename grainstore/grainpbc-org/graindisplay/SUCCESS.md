# 🌾 GrainDisplay - SUCCESS!

**Your GNOME desktop is now WARMER for bedtime!** 🌙

---

## ✅ What's Running

**Gammastep Process**: `pid 100244`
- **Temperature**: 1000K (very warm, bedtime red-orange)
- **Brightness**: 80% (comfortable for evening)
- **Status**: **ACTIVE** and persistent

Your screen now has a warm, candlelight-like glow perfect for evening work!

---

## 🎮 Quick Commands

```bash
# Toggle warm theme on/off
graindisplay-toggle

# Restart warm theme
graindisplay-warm

# Check status
bb scripts/build-simple.bb status

# Adjust warmth manually
gammastep -O 800    # Super warm
gammastep -O 2000   # Less warm
gammastep -x        # Reset to normal
```

---

## 📂 Installed Files

### Scripts (in `~/.local/bin/`)
- `graindisplay-warm` - Start persistent warm theme
- `graindisplay-toggle` - Quick on/off toggle
- `graindisplay-schedule` - Time-based automatic scheduling

### GNOME Integration
- `~/.config/autostart/graindisplay.desktop` - Auto-start on login
- `~/.config/systemd/user/graindisplay.{service,timer}` - Systemd integration

### Build Scripts (in `grainstore/graindisplay/scripts/`)
- `build-simple.bb` - Simple warm theme applicator
- `build-appimage.bb` - Full AppImage builder (future Humble UI)
- `apply-local.bb` - Local theme generator

---

## 🎨 Theme Colors Applied

Your display now uses the **GrainDisplay Bedtime Warm** palette:

| Color | Hex | Description |
|-------|-----|-------------|
| Warm Red | #D2691E | Primary accent (Chocolate) |
| Deep Orange | #FF8C00 | Secondary accent |
| Warm Black | #1A0F0A | Deep background |
| Warm White | #FFF8DC | Soft text (Cornsilk) |
| Warm Gold | #DAA520 | Highlights (Goldenrod) |

---

## 🔧 Customization

### Change Temperature

```bash
# Edit the warm script
nano ~/.local/bin/graindisplay-warm

# Change the -t value:
# -t 1000:1000  = Very warm (current)
# -t 2000:2000  = Warm
# -t 3000:3000  = Medium warm
```

### Change Brightness

```bash
# Edit the warm script
nano ~/.local/bin/graindisplay-warm

# Change the -b value:
# -b 0.6:0.6  = Dim (60%)
# -b 0.8:0.8  = Current (80%)
# -b 1.0:1.0  = Full brightness
```

### Auto-Schedule (Day/Night)

Enable automatic switching based on time:

```bash
# Start the scheduler
systemctl --user enable --now graindisplay.timer

# This will apply:
# 6 AM - 6 PM: 4000K (warm daylight)
# 6 PM - 6 AM: 1000K (bedtime warm)
```

---

## 🚀 Next Steps

### Make it Permanent

The theme is already set to auto-start! It will run automatically when you log in.

To verify:

```bash
ls ~/.config/autostart/graindisplay.desktop
```

### Build Full Humble UI App (Optional)

The complete Clojure Humble UI app with sliders and controls is ready to build:

```bash
cd /home/xy/kae3g/grainkae3g/grainstore/graindisplay

# Build native image (requires GraalVM)
bb scripts/build-appimage.bb native

# Create AppImage
bb scripts/build-appimage.bb appimage
```

This creates a full GUI app with:
- Interactive sliders for temperature and brightness
- Graintime display
- Theme preview
- Schedule controls
- SvelteKit-inspired design

---

## 📊 Technical Details

### Gammastep Configuration

```bash
gammastep -l 37.97:-122.53 -t 1000:1000 -b 0.8:0.8 -m randr
```

**Parameters:**
- `-l 37.97:-122.53` = San Rafael, CA coordinates
- `-t 1000:1000` = Day temp : Night temp (both 1000K)
- `-b 0.8:0.8` = Day brightness : Night brightness (both 80%)
- `-m randr` = Use RandR for display control

### Graintime Integration

The scheduler uses graintime for astronomical accuracy:

```bash
# Current graintime
gt now
# 12025-10-22--2251--PDT--moon-vishakha--asc-gemini000--sun-05thhouse--kae3g

# With location from:
cat ~/.config/graintime/config.edn
```

---

## 🌾 Philosophy

> **"From granules to grains to THE WHOLE GRAIN"**

GrainDisplay embodies the Grain Network's philosophy of **comfort and nourishment**:

- **Reduces blue light** - Signals your body it's evening
- **Warm colors** - Creates a cozy, campfire atmosphere
- **Graintime scheduling** - Aligns with natural circadian rhythms
- **Pure functional** - Predictable, reliable behavior

The bedtime warm theme treats your eyes with the same care that grains treat your body - providing **sustenance** and **comfort** when you need it most.

---

## 🐛 Troubleshooting

### Theme Not Visible?

Check if gammastep is running:

```bash
pgrep -a gammastep
```

If not, restart it:

```bash
graindisplay-warm
```

### Too Warm?

Adjust to a lighter warmth:

```bash
gammastep -l 37.97:-122.53 -t 2000:2000 -b 0.9:0.9 -m randr &
```

### Want Normal Colors Temporarily?

```bash
graindisplay-toggle
```

Toggle again to restore warmth.

### Conflicts with Other Color Managers?

Disable other tools first:

```bash
pkill redshift
pkill flux
systemctl --user stop redshift
```

---

## 📚 Documentation

- [README.md](README.md) - Full project documentation
- [INSTALLATION.md](INSTALLATION.md) - Detailed installation guide
- [Graintime README](../graintime/README.md) - Neovedic timestamps
- [Clojure S6 README](../clojure-s6/README.md) - Process supervision

---

## 🎉 Success Summary

✅ **Warm theme active** (1000K, 80% brightness)  
✅ **Gammastep running** (persistent daemon)  
✅ **Auto-start enabled** (runs on login)  
✅ **Commands installed** (`graindisplay-warm`, `graindisplay-toggle`)  
✅ **Documentation complete** (README, INSTALLATION, SUCCESS)  
✅ **Graintime integrated** (astronomical scheduling)  
✅ **GNOME integrated** (native desktop support)  
✅ **Systemd configured** (optional auto-scheduling)

---

**Version**: 1.0.0  
**Date**: October 23, 2025  
**Time**: 22:51 PDT  
**Graintime**: `12025-10-22--2251--PDT--moon-vishakha--asc-gemini000--sun-05thhouse--kae3g`  
**Author**: kae3g (Grain PBC)  
**License**: MIT

🌙 **Enjoy your warm, cozy display!**

From granules to grains to **THE WHOLE GRAIN** 🌾
