# Desktop Environment Switching Guide 🖥️

**Easy switching between GNOME and Sway for personal sovereignty**

---

## 🎯 **Overview**

This guide provides seamless switching between GNOME and Sway desktop environments, ensuring you always have a fallback option for your personal sovereignty stack.

---

## 🚀 **Quick Commands**

### **Check Status**
```bash
bb scripts/desktop-switcher.bb status
```

### **Switch to GNOME**
```bash
bb scripts/desktop-switcher.bb gnome
```

### **Switch to Sway**
```bash
bb scripts/desktop-switcher.bb sway
```

### **Restart Display Server**
```bash
bb scripts/desktop-switcher.bb restart
```

---

## 🛠️ **Setup Instructions**

### **1. Install Required Packages**
```bash
# GNOME (already installed)
sudo apt install -y gnome-session

# Sway
sudo apt install -y sway waybar swayidle swaylock brightnessctl

# Additional tools
sudo apt install -y foot wmenu
```

### **2. Configure GDM for Sway**
```bash
# Copy Sway session file
sudo cp configs/sway/sway.desktop /usr/share/wayland-sessions/

# Copy GDM configuration
sudo cp configs/gdm/custom.conf /etc/gdm3/custom.conf

# Restart GDM
sudo systemctl restart gdm3
```

### **3. Create Desktop Shortcuts**
```bash
bb scripts/desktop-switcher.bb create-shortcuts
```

---

## 🎨 **Display Configuration**

### **GNOME (Current)**
- **Night Light**: 1000K (candlelight warmth)
- **Text Scaling**: 1.5x
- **Display Modes**: Red/green, cyberpunk, monochrome
- **Status**: ✅ Working perfectly

### **Sway (Alternative)**
- **Wayland Native**: Better performance
- **Tiling Window Manager**: Keyboard-driven efficiency
- **wl-gammarelay**: Advanced display control
- **Status**: 🔧 Ready for testing

---

## 🔄 **Switching Methods**

### **Method 1: Login Manager (Recommended)**
1. **Logout** from current session
2. **Select desktop** at login screen
3. **Login** to chosen environment

### **Method 2: Command Line**
```bash
# Switch to GNOME
bb scripts/desktop-switcher.bb gnome

# Switch to Sway
bb scripts/desktop-switcher.bb sway
```

### **Method 3: Desktop Shortcuts**
- **Double-click** "Switch to GNOME" on desktop
- **Double-click** "Switch to Sway" on desktop

---

## 🎊 **Why Both Desktop Environments?**

### **GNOME Advantages**
- **Stability**: Battle-tested, reliable
- **Compatibility**: Works with all applications
- **Night Light**: Perfect warm display settings
- **Ease of Use**: Familiar interface

### **Sway Advantages**
- **Performance**: Native Wayland, faster
- **Customization**: Complete control over interface
- **Keyboard-Driven**: Efficient workflow
- **Modern**: Built for Wayland from ground up

### **Personal Sovereignty**
- **Choice**: Always have options
- **Fallback**: If one fails, switch to other
- **Learning**: Understand different approaches
- **Flexibility**: Adapt to different needs

---

## 🛡️ **Troubleshooting**

### **If Sway Fails to Start**
1. **Switch back to GNOME**:
   ```bash
   bb scripts/desktop-switcher.bb gnome
   ```

2. **Check Sway logs**:
   ```bash
   journalctl -u sway
   ```

3. **Test Sway manually**:
   ```bash
   sway --debug
   ```

### **If GNOME Fails to Start**
1. **Switch to Sway**:
   ```bash
   bb scripts/desktop-switcher.bb sway
   ```

2. **Check GNOME logs**:
   ```bash
   journalctl -u gdm3
   ```

### **If Both Fail**
1. **Restart display server**:
   ```bash
   bb scripts/desktop-switcher.bb restart
   ```

2. **Boot to recovery mode** and fix issues

---

## 🎯 **Recommended Workflow**

### **Daily Use**
- **Start with GNOME** (stable, familiar)
- **Switch to Sway** for focused work sessions
- **Use GNOME** for multimedia and casual use

### **Development**
- **Sway** for coding (tiling, keyboard-driven)
- **GNOME** for testing applications
- **Both** for different project needs

### **Display Modes**
- **GNOME**: Use Night Light (1000K candlelight)
- **Sway**: Use wl-gammarelay for advanced control
- **Both**: Perfect for different lighting conditions

---

## 📝 **Configuration Files**

### **Versioned in Repository**
- `configs/sway/config` - Sway configuration
- `configs/waybar/config` - Waybar status bar
- `configs/gdm/custom.conf` - GDM settings
- `scripts/desktop-switcher.bb` - Switching script

### **Symlinked**
- `~/.config/sway/config` → `configs/sway/config`
- `~/.config/waybar/config` → `configs/waybar/config`

---

## 🎊 **Benefits for Personal Sovereignty**

### **Complete Control**
- **Choose** your desktop environment
- **Switch** when needed
- **Customize** both environments
- **Version** all configurations

### **Reliability**
- **Always** have a working desktop
- **Fallback** options available
- **No vendor lock-in**
- **Open source** throughout

### **Learning**
- **Understand** different approaches
- **Master** both environments
- **Adapt** to different needs
- **Grow** your technical skills

---

**Built with ❤️ for personal sovereignty and choice.**

*"The best desktop environment is the one that serves you, not the other way around."* 🎊
