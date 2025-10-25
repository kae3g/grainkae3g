# 🌾 GrainDisplay Installation Guide

**Making GNOME Warmer for Bedtime**

---

## ✅ Quick Install (Recommended)

The simplest way to get a warm, bedtime-friendly display **right now**:

```bash
cd /home/xy/kae3g/grainkae3g/grainstore/graindisplay

# Install all scripts and autostart
bb scripts/build-simple.bb install

# Apply warm theme immediately
gammastep -O 1000 -b 0.8 &
```

Your display should now be very warm (1000K) - perfect for evening viewing!

---

## 🎯 What Just Happened?

### Immediate Effect
- **Color Temperature**: Changed to 1000K (very warm red-orange)
- **Brightness**: Reduced to 80% for comfort
- **Effect**: Your screen now looks like candlelight 🕯️

### Installed Components

1. **GNOME Autostart** (`~/.config/autostart/graindisplay.desktop`)
   - Automatically applies warm theme when you log in
   
2. **Toggle Script** (`~/.local/bin/graindisplay-toggle`)
   - Quick on/off switch: `graindisplay-toggle`
   
3. **Scheduler Script** (`~/.local/bin/graindisplay-schedule`)
   - Time-based themes: `graindisplay-schedule`
   - 6 AM - 6 PM: 4000K (warm daylight)
   - 6 PM - 6 AM: 1000K (bedtime warm)
   
4. **Systemd Service** (`~/.config/systemd/user/graindisplay.{service,timer}`)
   - Automatic hourly updates
   - Enable with: `systemctl --user enable --now graindisplay.timer`

---

## 🔧 Manual Control

### Adjust Warmth

```bash
# Very warm (bedtime)
gammastep -O 800

# Warm (evening)
gammastep -O 1000

# Medium warm (late afternoon)
gammastep -O 2000

# Slight warm (daytime)
gammastep -O 4000

# Reset to normal
gammastep -x
```

### Adjust Brightness

```bash
# Dim (80%)
gammastep -O 1000 -b 0.8

# Normal brightness
gammastep -O 1000 -b 1.0

# Very dim (60%)
gammastep -O 1000 -b 0.6
```

### Toggle On/Off

```bash
graindisplay-toggle
```

### Time-Based Automatic

```bash
graindisplay-schedule
```

---

## 🌙 Understanding Color Temperature

| Temperature | Description | Best For |
|-------------|-------------|----------|
| 800K | **Super Warm** | Right before bed |
| 1000K | **Bedtime Warm** | Evening work/reading |
| 2000K | **Evening Warm** | Sunset hours |
| 4000K | **Soft Daylight** | Daytime (less blue light) |
| 6500K | **Normal Daylight** | Standard screen setting |

**GrainDisplay Default**: 1000K for maximum bedtime comfort

---

## 🚀 Advanced Usage

### Automatic Scheduling with Systemd

Enable automatic theme changes based on time of day:

```bash
# Enable the timer
systemctl --user enable graindisplay.timer

# Start it now
systemctl --user start graindisplay.timer

# Check status
systemctl --user status graindisplay.timer
```

The timer runs hourly and applies:
- **6 AM - 6 PM**: 4000K (warm daylight mode)
- **6 PM - 6 AM**: 1000K (bedtime warm mode)

### Graintime Integration

Use graintime for astronomical accuracy:

```bash
# Configure your location
gt config

# The scheduler will use your configured location
# for accurate sunrise/sunset times
graindisplay-schedule
```

### Custom Scripts

Create your own schedule in `~/.local/bin/my-graindisplay`:

```bash
#!/bin/bash
# My custom GrainDisplay schedule

HOUR=$(date +%H)

if [ $HOUR -ge 21 ] || [ $HOUR -lt 7 ]; then
    # Super warm for sleep prep (9 PM - 7 AM)
    gammastep -O 800 -b 0.6
elif [ $HOUR -ge 18 ]; then
    # Warm for evening (6 PM - 9 PM)
    gammastep -O 1000 -b 0.8
elif [ $HOUR -ge 12 ]; then
    # Medium for afternoon (12 PM - 6 PM)
    gammastep -O 2000 -b 0.9
else
    # Light warm for morning (7 AM - 12 PM)
    gammastep -O 3000 -b 1.0
fi
```

---

## 🐛 Troubleshooting

### Theme Not Applying

```bash
# Check if gammastep is installed
which gammastep

# If not installed:
sudo apt install gammastep

# Try applying manually
gammastep -O 1000
```

### Theme Resets After Logging Out

This is normal! Re-enable autostart:

```bash
bb scripts/build-simple.bb install
```

### Want Even Warmer

```bash
# Go down to 600K for maximum warmth
gammastep -O 600 -b 0.7
```

### Want to Disable Temporarily

```bash
# Reset to normal
gammastep -x

# Or use toggle
graindisplay-toggle
```

### Permission Errors

Make sure scripts are executable:

```bash
chmod +x ~/.local/bin/graindisplay-*
```

---

## 📊 Current Status

Check what's running:

```bash
bb scripts/build-simple.bb status
```

Shows:
- ✅/❌ Active status
- Current time
- Current graintime
- Available commands

---

## 🎨 Color Palette Reference

GrainDisplay uses these warm colors:

```css
--warm-red: #D2691E        /* Chocolate */
--deep-orange: #FF8C00     /* Dark Orange */
--burnt-orange: #CC5500    /* Burnt Orange */
--warm-brown: #8B4513      /* Saddle Brown */
--warm-black: #1A0F0A      /* Very Dark Red-Brown */
--warm-white: #FFF8DC      /* Cornsilk */
--warm-gold: #DAA520       /* Goldenrod */
```

---

## 🔄 Uninstall

To remove GrainDisplay:

```bash
# Stop gammastep
pkill gammastep
gammastep -x

# Disable systemd timer
systemctl --user disable graindisplay.timer
systemctl --user stop graindisplay.timer

# Remove files
rm ~/.config/autostart/graindisplay.desktop
rm ~/.local/bin/graindisplay-*
rm ~/.config/systemd/user/graindisplay.{service,timer}
```

---

## 📚 See Also

- [Main README](README.md) - Full documentation
- [Graintime Documentation](../graintime/README.md) - Neovedic timestamps
- [f.lux](https://justgetflux.com/) - Original inspiration
- [Redshift](http://jonls.dk/redshift/) - Similar GNOME tool

---

## 🌾 Philosophy

> "From granules to grains to THE WHOLE GRAIN"

GrainDisplay embodies the Grain Network's philosophy of **comfort and nourishment**. Just as grains provide sustenance for the body, warm colors provide sustenance for the eyes during evening hours.

The bedtime red-orange palette:
- Reduces blue light exposure
- Signals to your body it's time to wind down
- Creates a cozy, campfire-like atmosphere
- Makes late-night coding more comfortable

---

**Version**: 1.0.0  
**Author**: kae3g (Grain PBC)  
**License**: MIT

🌙 **Sweet dreams with warm screens!**
