# Display Configuration for Framework Laptops 🎨

**Ubuntu 24.04 LTS on Framework Laptops - Optimal Display Setup**

This guide helps you configure the perfect display settings for your Framework laptop, including text scaling, Night Light, and color temperature adjustments.

## 🖥️ Framework Laptop Display Specs

### **Framework 16 (AMD Ryzen 7040 Series)**
- **Resolution**: 2560 x 1600 (16:10 aspect ratio)
- **Size**: 16" (340mm x 220mm)
- **Refresh Rate**: 165Hz
- **Panel Type**: IPS LCD
- **Brightness**: 500 nits

### **Framework 13 (Intel/AMD)**
- **Resolution**: 2256 x 1504 (3:2 aspect ratio)
- **Size**: 13.5" (286mm x 191mm)
- **Refresh Rate**: 60Hz
- **Panel Type**: IPS LCD
- **Brightness**: 400 nits

## 🚀 Quick Setup

### 1. Install Display Configuration Tool
```bash
# Make the display config script executable
chmod +x scripts/display-config.bb

# Test the tool
bb scripts/display-config.bb status
```

### 2. Apply Framework-Optimized Settings
```bash
# Apply settings optimized for Framework laptops
bb scripts/display-config.bb framework-16-optimized
```

### 3. Customize for Your Needs
```bash
# Set text scaling (1.25x to 2.0x recommended)
bb scripts/display-config.bb scaling 1.5

# Set Night Light temperature (2000K-6500K)
bb scripts/display-config.bb night-light 2500

# Disable Night Light if needed
bb scripts/display-config.bb disable-night-light
```

## 📏 Text Scaling Configuration

### **Why Scale Text?**
Framework laptops have high-resolution displays that can make text appear small. Scaling improves readability without affecting image quality.

### **Recommended Scaling Factors**
- **1.25x**: Minimal scaling, good for large screens
- **1.5x**: Comfortable for most users (recommended)
- **1.75x**: Better for accessibility needs
- **2.0x**: Maximum scaling, good for very small text

### **Manual Configuration**
```bash
# Set text scaling
gsettings set org.gnome.desktop.interface text-scaling-factor 1.5

# Check current scaling
gsettings get org.gnome.desktop.interface text-scaling-factor
```

### **GUI Method**
1. Open **Settings** (Super key + type "Settings")
2. Go to **Accessibility** → **Large Text**
3. Adjust the slider or use the toggle

## 🌙 Night Light Configuration

### **What is Night Light?**
Night Light reduces blue light emission to reduce eye strain during evening hours. It's especially important for long coding sessions.

### **Temperature Settings**
- **6500K**: Daylight (no filtering)
- **5000K**: Slightly warm
- **4000K**: Warm white
- **3000K**: Very warm (recommended for evening)
- **2500K**: Extremely warm (for late night coding)
- **2000K**: Candlelight (maximum warmth)

### **Manual Configuration**
```bash
# Enable Night Light
gsettings set org.gnome.settings-daemon.plugins.color night-light-enabled true

# Set temperature (2500K for very warm)
gsettings set org.gnome.settings-daemon.plugins.color night-light-temperature 2500

# Disable Night Light
gsettings set org.gnome.settings-daemon.plugins.color night-light-enabled false
```

### **GUI Method**
1. Open **Settings** → **Display**
2. Toggle **Night Light** on/off
3. Click **Night Light** to adjust schedule and temperature

## 🎨 Advanced Color Management

### **Install Additional Tools**
```bash
# Install Redshift for additional control
bb scripts/display-config.bb install-tools

# Install sct for precise temperature control
sudo apt install sct
```

### **Redshift Configuration**
Redshift provides more advanced color temperature control:

```bash
# Start Redshift with custom settings
redshift -O 2500  # Set to 2500K
redshift -x        # Reset to normal
redshift -P        # Preview mode
```

### **sct (Set Color Temperature)**
For precise control:

```bash
# Set specific temperature
sct 2500  # 2500K (very warm)
sct 4000  # 4000K (warm)
sct 6500  # 6500K (daylight)
```

## 📱 Display Profiles

### **Create Custom Profiles**
```bash
# Create display profiles for different activities
bb scripts/display-config.bb create-profiles
```

This creates profiles in `~/.local/bin/display-profiles/`:
- **warm-coding**: 1.5x scaling, 2500K temperature
- **cool-coding**: 1.5x scaling, 6500K temperature
- **presentation**: 1.25x scaling, 6500K temperature
- **night-reading**: 1.75x scaling, 2000K temperature

### **Use Profiles**
```bash
# Apply a profile
~/.local/bin/display-profiles/warm-coding.sh
~/.local/bin/display-profiles/cool-coding.sh
~/.local/bin/display-profiles/presentation.sh
~/.local/bin/display-profiles/night-reading.sh
```

## 🔧 Troubleshooting

### **Common Issues**

#### **Text Still Too Small**
```bash
# Try higher scaling
bb scripts/display-config.bb scaling 1.75
# or
bb scripts/display-config.bb scaling 2.0
```

#### **Night Light Not Working**
```bash
# Check if Night Light is enabled
gsettings get org.gnome.settings-daemon.plugins.color night-light-enabled

# Enable it manually
gsettings set org.gnome.settings-daemon.plugins.color night-light-enabled true
```

#### **Display Not Detected**
```bash
# Check display detection
xrandr

# Restart display manager
sudo systemctl restart gdm3
```

#### **Scaling Not Applied**
```bash
# Log out and log back in
# Or restart GNOME Shell
Alt + F2, then type 'r' and press Enter
```

### **Performance Considerations**

#### **High DPI Scaling**
- **1.25x-1.5x**: Minimal performance impact
- **1.75x-2.0x**: Slight performance impact
- **2.0x+**: May affect performance on older hardware

#### **Night Light Performance**
- **GNOME Night Light**: Minimal impact
- **Redshift**: Slight impact
- **sct**: Minimal impact

## 🎯 Framework-Specific Optimizations

### **Framework 16 Optimizations**
```bash
# Apply Framework 16 specific settings
bb scripts/display-config.bb framework-16-optimized
```

This applies:
- **Text Scaling**: 1.5x (optimal for 16" 2560x1600)
- **Night Light**: 2500K (warm for long coding sessions)
- **Brightness**: Optimized for 500 nits panel

### **Framework 13 Optimizations**
For Framework 13, use slightly different settings:
```bash
# Framework 13 optimized settings
bb scripts/display-config.bb scaling 1.25
bb scripts/display-config.bb night-light 3000
```

## 📚 Additional Resources

### **Framework Documentation**
- [Framework 16 Documentation](https://frame.work/docs/laptop-16)
- [Framework 13 Documentation](https://frame.work/docs/laptop-13)
- [Framework Community](https://community.frame.work/)

### **Ubuntu Display Configuration**
- [Ubuntu Display Settings](https://help.ubuntu.com/stable/ubuntu-help/display.html)
- [GNOME Display Settings](https://help.gnome.org/users/gnome-help/stable/display.html)

### **Color Management**
- [Redshift Documentation](http://jonls.dk/redshift/)
- [sct Documentation](https://github.com/faf0/sct)

## 🎊 Pro Tips

### **For Developers**
- Use **1.5x scaling** for comfortable coding
- Set **2500K Night Light** for evening sessions
- Create **warm-coding** profile for long sessions

### **For Designers**
- Use **1.25x scaling** to see more detail
- Disable Night Light when color accuracy matters
- Use **presentation** profile for client demos

### **For Accessibility**
- Use **1.75x-2.0x scaling** for better readability
- Set **2000K Night Light** for maximum comfort
- Use **night-reading** profile for long reading sessions

---

**Built with ❤️ for Framework laptop users and the open source community.**

*"The right display settings can transform your coding experience from good to exceptional."* 🎊





