# Display Configuration Quick Reference 🎨

**Kaden's Framework 16 Display Setup**

## 🚀 Current Settings

- **Text Scaling**: 1.5x (perfect for 16" 2560x1600)
- **Night Light**: 2000K (candlelight warmth for comfortable coding)
- **Resolution**: 2560 x 1600 (16:10 aspect ratio)
- **Refresh Rate**: 165Hz

## ⚡ Quick Commands

### **Check Status**
```bash
bb scripts/display-config.bb status
```

### **Text Scaling**
```bash
# Current: 1.5x
bb scripts/display-config.bb scaling 1.5

# Larger text
bb scripts/display-config.bb scaling 1.75
bb scripts/display-config.bb scaling 2.0

# Smaller text
bb scripts/display-config.bb scaling 1.25
bb scripts/display-config.bb scaling 1.0
```

### **Night Light Temperature**
```bash
# Current: 2500K (very warm)
bb scripts/display-config.bb night-light 2500

# Warmer options
bb scripts/display-config.bb night-light 2000  # Candlelight
bb scripts/display-config.bb night-light 3000  # Warm white

# Cooler options
bb scripts/display-config.bb night-light 4000  # Warm white
bb scripts/display-config.bb night-light 5000  # Slightly warm
bb scripts/display-config.bb night-light 6500  # Daylight

# Disable Night Light
bb scripts/display-config.bb disable-night-light
```

### **Preset Profiles**
```bash
# Apply Framework 16 optimized settings
bb scripts/display-config.bb framework-16-optimized

# Create custom profiles
bb scripts/display-config.bb create-profiles
```

## 🎯 Temperature Guide

| Temperature | Description | Best For |
|-------------|-------------|----------|
| 2000K | Candlelight | Very late night coding |
| 2500K | Very warm | Evening coding (current) |
| 3000K | Warm white | Evening work |
| 4000K | Warm white | Afternoon work |
| 5000K | Slightly warm | Morning work |
| 6500K | Daylight | Color-accurate work |

## 📏 Scaling Guide

| Scaling | Description | Best For |
|---------|-------------|----------|
| 1.0x | Native | Maximum screen real estate |
| 1.25x | Minimal | Large screens, small text |
| 1.5x | Comfortable | Most users (current) |
| 1.75x | Large | Accessibility needs |
| 2.0x | Maximum | Very small text |

## 🔧 Troubleshooting

### **Text Too Small**
```bash
bb scripts/display-config.bb scaling 1.75
```

### **Text Too Large**
```bash
bb scripts/display-config.bb scaling 1.25
```

### **Night Light Not Working**
```bash
# Check status
bb scripts/display-config.bb status

# Re-enable
bb scripts/display-config.bb night-light 2500
```

### **Reset to Defaults**
```bash
bb scripts/display-config.bb scaling 1.0
bb scripts/display-config.bb disable-night-light
```

## 🎊 Pro Tips

### **For Long Coding Sessions**
- Use **1.5x scaling** + **2500K Night Light**
- Take breaks every hour
- Look away from screen for 20 seconds every 20 minutes

### **For Color-Accurate Work**
- Disable Night Light: `bb scripts/display-config.bb disable-night-light`
- Use **1.25x scaling** for more screen space
- Work in well-lit environment

### **For Presentations**
- Use **1.25x scaling** for professional look
- Disable Night Light for accurate colors
- Test on projector if possible

---

**Current Setup**: 1.5x scaling + 2500K Night Light = Perfect for Framework 16 development! 🎊
