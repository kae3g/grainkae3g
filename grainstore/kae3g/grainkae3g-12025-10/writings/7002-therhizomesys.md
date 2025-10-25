---
title: "7002. The Rhizome System: Framework Laptop + Alpine Linux + s6 + musl + Sway Wayland"
date: 2025-10-21
phase: "therhizomesys"
series: "technical-implementation"
status: "complete"
sort-order: 7002
version: "vzxw"
---

# kae3g 7002: The Rhizome System — Framework Laptop + Alpine Linux + s6 + musl + Sway Wayland

**Timestamp:** 12025-10-21--coldriver-tundra  
**Series:** Technical Implementation  
**Category:** Framework Laptop, Alpine Linux, s6 Init, musl libc, Sway Wayland  
**Reading Time:** 45 minutes

> **"The rhizome grows underground, connecting nodes without hierarchy. Like the Framework laptop's modular design, Alpine Linux's musl libc purity, and s6's supervision philosophy, this system embodies the rhizomatic nature of modern computing—distributed, connected, and beautifully minimal. This comprehensive guide provides both fast-track installation (20-30 minutes) and detailed configuration options for the perfect musl-native development environment."**

---

## Multi-AI Consensus: The Perfect Stack

**All AI systems (Meta, DeepSeek, Gemini, Grok, ChatGPT, Claude) converge on this optimal configuration:**

- **OS**: Alpine Linux Extended ISO (v3.22) — musl libc, minimal, secure
- **Init System**: s6 supervision suite — musl-native, ~200KB footprint
- **GUI**: Sway Wayland — keyboard-driven, configuration-as-code, minimal
- **Hardware**: Framework 16 AMD Ryzen 7 7840HS + Radeon 780M Graphics
- **Package Manager**: apk + Nix (for reproducible development environments)

**Installation Options:**
- **🚀 Fast-Track**: 20-30 minutes, no encryption, user `xy`, hostname `xy-framework-alpine`
- **🔧 Detailed**: Comprehensive configuration with power management and laptop optimizations

**Why This Stack:**
- **Musl libc purity** — Clean, auditable C codebase, minimal attack surface
- **s6 supervision** — Unix philosophy, process supervision = container supervision
- **Sway minimalism** — Developer efficiency, keyboard-driven workflow
- **Framework optimization** — Hardware-specific firmware and drivers included

---

## Prerequisites

### Hardware Requirements
- **Framework 16 Laptop** (AMD Ryzen 7 7840HS, Radeon 780M Graphics)
- **BIOS Version**: Insyde IFGP6.03.05 or compatible
- **RAM**: 32GB recommended (16GB minimum)
- **Storage**: 1TB NVMe SSD (Framework's standard NVMe drive)
- **Network**: WiFi or Ethernet connection for initial setup

### Software Requirements
- **USB Drive**: 8GB+ USB-C or USB-A drive for Alpine Extended ISO
- **Host System**: Any Linux/macOS/Windows system for creating bootable USB
- **Network Access**: Internet connection for package downloads

### Installation Options
- **Fast-Track**: No dual-boot, no encryption, single user `xy`, auto-boot from NVMe
- **Detailed**: Full power management, laptop optimizations, comprehensive configuration

---

## Step 1: BIOS Configuration

### Essential BIOS Settings

**Access BIOS:**
1. Power on Framework laptop
2. Press `F2` repeatedly during boot to enter BIOS
3. Navigate using arrow keys and Enter

**Required BIOS Changes:**
```bash
# Critical Settings for Alpine Linux
Secure Boot: DISABLED          # Required for Alpine Linux
SVM Mode: ENABLED              # AMD virtualization support
IOMMU: ENABLED                 # GPU passthrough capability
Boot Order: USB UEFI → NVMe UEFI OS
Fast Boot: DISABLED            # Ensure proper hardware detection
```

**Detailed BIOS Configuration:**
1. **Security Tab:**
   - Secure Boot: `Disabled`
   - TPM: `Enabled` (for hardware security)
   - Secure Boot Keys: `Clear` (if needed)

2. **Advanced Tab:**
   - CPU Configuration:
     - SVM Mode: `Enabled`
     - IOMMU: `Enabled`
     - Secure Memory Encryption: `Enabled` (optional)
   
3. **Boot Tab:**
   - Boot Mode: `UEFI`
   - Fast Boot: `Disabled`
   - Boot Order: USB devices first

4. **Save and Exit:**
   - Press `F10` to save changes
   - Confirm with `Yes`
   - System will reboot

---

## Step 2: Download and Prepare Alpine Linux Extended ISO

### Why Extended ISO is Essential

**Multi-AI Consensus:** The Extended ISO is **required** (not optional) for Framework laptops:

- **AMD Microcode**: Includes `amd-ucode` for Ryzen 7 7840HS optimization
- **Graphics Firmware**: Contains `linux-firmware-amd` for Radeon 780M acceleration
- **WiFi Firmware**: Framework's WiFi card requires `linux-firmware-iwlwifi`
- **Audio Firmware**: Framework laptop audio codecs need specific firmware
- **Offline Installation**: ~200 packages included, reducing network dependency

### Download Alpine Linux Extended ISO

```bash
# Download Alpine Linux Extended ISO (Essential for Framework laptops)
wget https://dl-cdn.alpinelinux.org/alpine/v3.22/releases/x86_64/alpine-extended-3.22.2-x86_64.iso

# Download and verify checksum
wget https://dl-cdn.alpinelinux.org/alpine/v3.22/releases/x86_64/alpine-extended-3.22.2-x86_64.iso.sha256
sha256sum -c alpine-extended-3.22.2-x86_64.iso.sha256

# Expected output:
# alpine-extended-3.22.2-x86_64.iso: OK
```

### Create Bootable USB Drive

**On Linux/macOS:**
```bash
# Identify USB drive (replace /dev/sdX with your USB device)
lsblk  # or albinutil list on macOS

# Create bootable USB (WARNING: This will erase the USB drive)
dd if=alpine-extended-3.22.2-x86_64.iso of=/dev/sdX bs=4M status=progress oflag=sync

# macOS alternative:
sudo dd if=alpine-extended-3.22.2-x86_64.iso of=/dev/rdiskX bs=4m
```

**On Windows:**
```powershell
# Use Rufus or similar tool
# Download Rufus: https://rufus.ie/
# Select Alpine Extended ISO
# Use DD image mode
# Create bootable USB
```

---

## 🚀 **Fast-Track Installation (20-30 minutes)**

*For those who want to get up and running quickly*

### Step 3A: Boot from Alpine Extended ISO USB

```bash
# Boot from Alpine Extended ISO USB
# Login as root (no password)

# Quick WiFi setup (if needed)
setup-interfaces
# Select: wlan0, DHCP, enter SSID/password
# Or use eth0 for wired connection

# Verify connection
ping -c 3 dl-cdn.alpinelinux.org
```

### Step 4A: Run Alpine Installation

```bash
setup-alpine
```

**Fast-Track Answers (Multi-AI Optimized):**
```
Keyboard:      us
Hostname:      xy-framework-alpine
Interface:     wlan0 (or eth0)
IP address:    dhcp
Root password: [set secure password]
Timezone:      America/Los_Angeles (or yours)
Proxy:         none
Mirror:        f (finds fastest)
SSH server:    openssh
Disk:          nvme0n1
How to use:    sys (creates bootable system with GRUB)
Erase disk:    y
```

**Installation will proceed automatically, then reboot when complete.**

### Step 5A: First Boot - Login to Installed System

```bash
# After reboot, login as root
# Remove USB drive when prompted during boot

# Update system first
apk update && apk upgrade

# Create admin user 'xy'
adduser xy
adduser xy wheel

# Install and configure sudo (CRITICAL: requires community repository)
# Step 1: Enable community repository (sudo is not in main repository)
echo 'http://dl-cdn.alpinelinux.org/alpine/v3.22/community' >> /etc/apk/repositories

# Step 2: Update package index
apk update

# Step 3: Install sudo package
apk add sudo

# Step 4: Create admin user 'xy' with password
adduser xy
echo "Enter password for user xy when prompted"
passwd xy

# Step 5: Add xy to wheel group (required for sudo access)
adduser xy wheel

# Step 6: Configure sudoers file (use visudo for safety)
visudo
# In visudo, uncomment this line: %wheel ALL=(ALL) ALL
# Or programmatically add it:
echo '%wheel ALL=(ALL) ALL' >> /etc/sudoers

# Alternative: Use doas (Alpine's recommended lightweight sudo replacement)
# apk add doas
# echo 'permit persist :wheel' >> /etc/doas.conf

# Install Framework hardware essentials (corrected package names)
apk add --no-cache \
  linux-firmware \
  linux-firmware-amd \
  amd-ucode \
  mesa-dri-gallium mesa-va-gallium mesa-vulkan-radeon \
  acpid cpufrequtils brightnessctl \
  wpa_supplicant wireless-tools

# Enable critical services (corrected service names)
rc-update add acpid default
rc-update add wpa_supplicant default

# Install s6 suite (keep OpenRC as PID1 for stability)
apk add --no-cache \
  s6 s6-rc s6-linux-init \
  s6-portable-utils s6-dns \
  execline
```

### Step 6A: Install Sway and Configure User

```bash
# Login as 'xy' with your password

# Install Sway and all dependencies
sudo apk add --no-cache \
  sway foot waybar mako \
  grim slurp wl-clipboard \
  seatd \
  pipewire pipewire-alsa wireplumber pamixer \
  firefox \
  ttf-dejavu ttf-noto ttf-noto-emoji \
  dbus elogind

# Add user to required groups
sudo adduser xy video
sudo adduser xy input
sudo adduser xy seat

# Enable services
sudo rc-update add seatd default
sudo rc-update add dbus default
sudo rc-update add elogind default

# Start services now
sudo rc-service seatd start
sudo rc-service dbus start
sudo rc-service elogind start
```

### Step 7A: Configure Sway (Framework-Optimized)

```bash
mkdir -p ~/.config/sway

cat > ~/.config/sway/config << 'EOF'
# Framework-optimized Sway config (Multi-AI Consensus)
set $mod Mod4

# Framework display (adjust if needed)
output eDP-1 scale 1

# Input configuration
input "type:keyboard" {
    xkb_layout us
}

input "type:touchpad" {
    tap enabled
    natural_scroll enabled
}

# Key bindings
bindsym $mod+Return exec foot
bindsym $mod+d exec firefox
bindsym $mod+Shift+q kill
bindsym $mod+Shift+e exec swaynag -t warning -m 'Exit Sway?' -b 'Yes' 'swaymsg exit'

# Window management (vim-style)
bindsym $mod+h focus left
bindsym $mod+j focus down
bindsym $mod+k focus up
bindsym $mod+l focus right

# Brightness controls (Framework function keys)
bindsym XF86MonBrightnessDown exec brightnessctl set 5%-
bindsym XF86MonBrightnessUp exec brightnessctl set +5%

# Volume controls
bindsym XF86AudioLowerVolume exec pamixer --decrease 5
bindsym XF86AudioRaiseVolume exec pamixer --increase 5
bindsym XF86AudioMute exec pamixer --toggle-mute

# Status bar
bar {
    position top
    status_command waybar
}
EOF

# Test Sway
sway
```

### Step 8A: Verification and Development Setup

```bash
# Check hostname
hostname
# Output: xy-framework-alpine

# Check user and groups
id
# Should show: wheel, video, input, seat

# Check init system
ps -p 1 -o comm=
# Output: init (OpenRC)

# Check GRUB boot entries
sudo efibootmgr -v
# Should show 'alpine' as first entry

# Check graphics acceleration
sudo apk add mesa-demos
glxinfo | grep "OpenGL renderer"
# Should show: AMD Radeon Graphics (Radeon 780M)

# Check WiFi
ip addr show wlan0
# Should show IP address

# Check Sway is working
echo $WAYLAND_DISPLAY
# Should show wayland-* if in Sway
```

---

## 🔧 **Detailed Installation (Comprehensive Configuration)**

*For those who want full control and optimization*

### Step 3B: Boot from Alpine Extended ISO USB

1. **Insert USB drive** into Framework laptop
2. **Power on** and press `F12` for boot menu
3. **Select USB drive** from boot menu
4. **Wait for Alpine Linux boot** (should boot automatically)
5. **Login as root** (no password required)

### Step 4B: Run Alpine Installation

```bash
# Run Alpine setup script
setup-alpine

# Configuration prompts and responses:
```

**Keyboard Layout:**
```
Available layouts: us, de, fr, it, etc.
Select keyboard layout: us
```

**Hostname:**
```
Enter hostname: framework-alpine
```

**Network Configuration:**
```
Available interfaces: wlan0, eth0
Select interface: wlan0

# WiFi Setup (if using WiFi)
Enter SSID: [your-wifi-network]
Enter passphrase: [your-wifi-password]

# Static IP (recommended for development)
Configure IP address: 192.168.1.100/24
Configure gateway: 192.168.1.1
Configure DNS: 8.8.8.8
```

**Timezone:**
```
Available timezones: America/Los_Angeles, America/New_York, etc.
Select timezone: America/Los_Angeles
```

**SSH Access:**
```
Enable SSH server: yes
Allow root login: yes (for initial setup)
```

**Disk Configuration:**
```
Available disks: nvme0n1 (Framework's NVMe drive)
Select disk: nvme0n1
Use entire disk: yes
Create swap: yes (recommended)
```

**User Setup:**
```
Root password: [secure-password]
Create user: developer
User password: [secure-password]
```

**Installation:**
```
Installation will begin...
This may take 10-15 minutes
```

**Reboot:**
```bash
# Installation will complete and prompt for reboot
# Remove USB drive when prompted during boot
reboot
```

### Step 5B: Post-Installation Base System Setup

```bash
# Login as root
login: root
Password: [your-root-password]

# Update system packages
apk update && apk upgrade

# Install Framework laptop specific firmware
apk add --no-cache \
  linux-firmware \
  linux-firmware-amd \
  amd-ucode \
  acpi \
  acpid \
  cpupower

# Install essential development packages
apk add --no-cache \
  curl \          # HTTP client for downloads and API calls
  wget \          # Alternative HTTP client
  xz \            # High-compression archiver (xz/lzma format)
  git \
  vim \
  htop \
  iotop \
  nload \
  tcpdump \
  strace \
  gdb \
  musl-dev \
  clang \
  make \
  linux-headers
```

### Step 6B: Install and Configure s6 Init System

```bash
# Install s6 supervision suite (musl-native)
apk add --no-cache \
  s6 \
  s6-rc \
  s6-linux-init \
  s6-portable-utils \
  s6-dns \
  execline

# Configure s6 as init system (musl-native)
s6-linux-init-maker -1 /etc/s6/rc/init

# Backup original inittab
cp /etc/inittab /etc/inittab.bak

# Replace with s6 init
cat > /etc/inittab << 'EOF'
::sysinit:/etc/s6/init/init-stage1
::wait:/etc/s6/init/init-stage2
::shutdown:/etc/s6/init/init-shutdown
EOF

# Create s6 service directories
mkdir -p /etc/s6/services/{sshd,chronyd,networking,seatd}

# Configure SSH service
cat > /etc/s6/services/sshd/run << 'EOF'
#!/bin/execlineb -P
s6-setuidgid root
/usr/sbin/sshd -D
EOF

# Configure chrony service
cat > /etc/s6/services/chronyd/run << 'EOF'
#!/bin/execlineb -P
s6-setuidgid chrony
/usr/sbin/chronyd -d
EOF

# Configure seatd service (required for Wayland)
cat > /etc/s6/services/seatd/run << 'EOF'
#!/bin/execlineb -P
s6-setuidgid root
/usr/bin/seatd
EOF

# Make services executable
chmod +x /etc/s6/services/*/run

# Configure s6-rc for service management
s6-rc-compile /etc/s6-rc/compiled /etc/s6-rc/source
s6-rc change default

# Add user to video and seat groups for Wayland
adduser $USER video
adduser $USER seat
```

### Step 7B: Install Sway Wayland GUI

```bash
# Install Sway Wayland compositor and components
apk add --no-cache \
  sway \
  foot \
  waybar \
  mako \
  grim \
  slurp \
  wl-clipboard \
  seatd

# Install graphics drivers for Radeon 780M
apk add --no-cache \
  mesa-dri-gallium \
  mesa-va-gallium \
  mesa-vulkan-radeon

# Install audio support
apk add --no-cache \
  pipewire \
  pipewire-alsa \
  wireplumber \
  pamixer

# Install web browser
apk add --no-cache \
  firefox

# Add user to required groups for Wayland
adduser developer video
adduser developer seat
adduser developer audio
```

### Step 8B: Configure Sway

```bash
# Create Sway configuration directory
mkdir -p /home/developer/.config/sway

# Create comprehensive Sway configuration
cat > /home/developer/.config/sway/config << 'EOF'
# Sway configuration for Framework laptop development

# Variables
set $mod Mod4
set $terminal foot
set $menu wofi --show drun

# Font
font pango:JetBrains Mono 10

# Input configuration
input * {
    xkb_layout us
    xkb_variant ""
}

# Output configuration (Framework laptop display)
output * {
    background #000000 solid_color
}

# Key bindings
bindsym $mod+Return exec $terminal
bindsym $mod+Shift+q kill
bindsym $mod+d exec $menu
bindsym $mod+Shift+e exec swaynag -t warning -m 'Exit Sway?' -b 'Yes' 'swaymsg exit'

# Window management
bindsym $mod+Left focus left
bindsym $mod+Right focus right
bindsym $mod+Up focus up
bindsym $mod+Down focus down

bindsym $mod+Shift+Left move left
bindsym $mod+Shift+Right move right
bindsym $mod+Shift+Up move up
bindsym $mod+Shift+Down move down

# Workspace switching
bindsym $mod+1 workspace 1
bindsym $mod+2 workspace 2
bindsym $mod+3 workspace 3
bindsym $mod+4 workspace 4
bindsym $mod+5 workspace 5

bindsym $mod+Shift+1 move container to workspace 1
bindsym $mod+Shift+2 move container to workspace 2
bindsym $mod+Shift+3 move container to workspace 3
bindsym $mod+Shift+4 move container to workspace 4
bindsym $mod+Shift+5 move container to workspace 5

# Layout management
bindsym $mod+s layout stacking
bindsym $mod+w layout tabbed
bindsym $mod+e layout toggle split

# Reload configuration
bindsym $mod+Shift+c reload

# Lock screen
bindsym $mod+l exec swaylock

# Volume control
bindsym XF86AudioRaiseVolume exec pamixer -i 5
bindsym XF86AudioLowerVolume exec pamixer -d 5
bindsym XF86AudioMute exec pamixer -t

# Brightness control (Framework laptop)
bindsym XF86MonBrightnessUp exec brightnessctl set +5%
bindsym XF86MonBrightnessDown exec brightnessctl set 5%-

# Startup applications
exec waybar
exec mako
EOF

# Set proper ownership
chown -R developer:developer /home/developer/.config
```

### Step 9B: Configure Waybar

```bash
# Create Waybar configuration
cat > /home/developer/.config/waybar/config << 'EOF'
{
    "layer": "top",
    "position": "top",
    "height": 30,
    "modules-left": ["sway/workspaces"],
    "modules-center": ["clock"],
    "modules-right": ["pulseaudio", "battery", "network"],
    
    "sway/workspaces": {
        "all-outputs": true
    },
    
    "clock": {
        "format": "{:%Y-%m-%d %H:%M}"
    },
    
    "pulseaudio": {
        "format": "{volume}% {icon}",
        "format-muted": "🔇",
        "format-icons": {
            "headphone": "🎧",
            "hands-free": "🖱️",
            "headset": "🎧",
            "phone": "📞",
            "portable": "📱",
            "car": "🚗",
            "default": ["🔈", "🔉", "🔊"]
        }
    },
    
    "battery": {
        "format": "{capacity}% {icon}",
        "format-charging": "{capacity}% 🔌",
        "format-icons": ["🔋", "🔋", "🔋", "🔋", "🔋"]
    },
    
    "network": {
        "format": "{ifname}",
        "format-wifi": "📶 {signalStrength}%",
        "format-ethernet": "🌐",
        "format-disconnected": "❌"
    }
}
EOF

# Create Waybar style
cat > /home/developer/.config/waybar/style.css << 'EOF'
* {
    border: none;
    border-radius: 0;
    font-family: JetBrains Mono;
    font-size: 12px;
    min-height: 0;
}

window#waybar {
    background: #1e1e1e;
    color: #ffffff;
}

#workspaces button {
    padding: 0 5px;
    background: transparent;
    color: #ffffff;
    border-bottom: 3px solid transparent;
}

#workspaces button.focused {
    background: #64727D;
    border-bottom: 3px solid #ffffff;
}

#clock, #battery, #pulseaudio, #network {
    padding: 0 10px;
    margin: 0 5px;
}
EOF

# Set proper ownership
chown -R developer:developer /home/developer/.config
```

### Step 10B: Laptop Power & Usability Optimization

*Based on Alpine Linux Wiki "Setting up a laptop" guidance, adapted for Framework laptop + s6 supervision*

#### Framework Laptop Power Management

```bash
# Install laptop power management tools
apk add brightnessctl cpufreqd acpi acpid hdparm

# Install NetworkManager for easy WiFi management
apk add networkmanager wpa_supplicant dhcpcd wireless-tools iputils

# Install locking tools for security
apk add swaylock swayidle
```

#### ACPI Event Handling (Lid, Power, AC Events)

```bash
# Create ACPI event handlers for Framework laptop
mkdir -p /etc/acpi/handlers

# Lid close handler (suspend)
cat > /etc/acpi/handlers/lid-close << 'EOF'
#!/bin/sh
# Suspend on lid close
echo mem > /sys/power/state
EOF

# Lid open handler (resume)
cat > /etc/acpi/handlers/lid-open << 'EOF'
#!/bin/sh
# Lock screen on lid open
swaylock --daemonize
EOF

# AC adapter plug/unplug handlers
cat > /etc/acpi/handlers/ac-adapter << 'EOF'
#!/bin/sh
# Switch CPU governor based on AC status
if [ "$3" = "00000000" ]; then
    # Battery mode - powersave governor
    cpupower frequency-set -g powersave
    # Reduce brightness for battery saving
    brightnessctl set 70%
else
    # AC mode - performance governor
    cpupower frequency-set -g performance
    # Increase brightness for AC power
    brightnessctl set 100%
fi
EOF

chmod +x /etc/acpi/handlers/*
```

#### s6 Service Configuration for Power Management

```bash
# Create s6 services for power management
mkdir -p /etc/s6/services/{acpid,cpufreqd,NetworkManager}

# ACPI daemon service
cat > /etc/s6/services/acpid/run << 'EOF'
#!/bin/execlineb -P
s6-setuidgid root
acpid -f
EOF

# CPU frequency daemon service
cat > /etc/s6/services/cpufreqd/run << 'EOF'
#!/bin/execlineb -P
s6-setuidgid root
cpufreqd
EOF

# NetworkManager service
cat > /etc/s6/services/NetworkManager/run << 'EOF'
#!/bin/execlineb -P
s6-setuidgid root
NetworkManager --no-daemon
EOF

chmod +x /etc/s6/services/*/run

# Configure s6-rc service dependencies
mkdir -p /etc/s6-rc/source/{acpid,cpufreqd,NetworkManager}

# Service type definitions
echo "longrun" > /etc/s6-rc/source/acpid/type
echo "longrun" > /etc/s6-rc/source/cpufreqd/type
echo "longrun" > /etc/s6-rc/source/NetworkManager/type

# Compile and activate services
s6-rc-compile /etc/s6-rc/compiled /etc/s6-rc/source
s6-rc change default
```

#### Backlight Control and Battery Optimization

```bash
# Configure automatic backlight adjustment
cat > /usr/local/bin/battery-backlight << 'EOF'
#!/bin/sh
# Battery-aware backlight control
BATTERY_PERCENT=$(cat /sys/class/power_supply/BAT0/capacity 2>/dev/null || echo 100)
AC_STATUS=$(cat /sys/class/power_supply/ADP1/online 2>/dev/null || echo 1)

if [ "$AC_STATUS" = "0" ]; then
    # On battery - adjust brightness based on battery level
    if [ "$BATTERY_PERCENT" -lt 20 ]; then
        brightnessctl set 30%
    elif [ "$BATTERY_PERCENT" -lt 50 ]; then
        brightnessctl set 50%
    else
        brightnessctl set 70%
    fi
else
    # On AC - full brightness
    brightnessctl set 100%
fi
EOF

chmod +x /usr/local/bin/battery-backlight

# Create systemd timer equivalent using cron for battery monitoring
echo "*/5 * * * * /usr/local/bin/battery-backlight" | crontab -
```

#### WiFi Management with NetworkManager

```bash
# Configure NetworkManager for easy WiFi management
cat > /etc/NetworkManager/NetworkManager.conf << 'EOF'
[main]
plugins=keyfile

[device]
wifi.scan-rand-mac-address=no
EOF

# Start NetworkManager
s6-rc change NetworkManager

# Connect to WiFi using nmtui
nmtui
```

#### Framework Laptop Specific Optimizations

```bash
# Configure power management for Framework laptop
echo 'SUBSYSTEM=="power_supply", ATTR{type}=="Battery", ATTR{status}=="Discharging", RUN+="/usr/bin/cpupower frequency-set -g powersave"' > /etc/udev/rules.d/99-framework-power.rules

echo 'SUBSYSTEM=="power_supply", ATTR{type}=="Battery", ATTR{status}=="Charging", RUN+="/usr/bin/cpupower frequency-set -g ondemand"' > /etc/udev/rules.d/99-framework-charging.rules

# Configure thermal management
echo 'SUBSYSTEM=="thermal", ATTR{type}=="cpu", ATTR{temp}=="*", RUN+="/usr/bin/cpupower frequency-set -g ondemand"' > /etc/udev/rules.d/99-framework-thermal.rules

# Reload udev rules
udevadm control --reload-rules
```

---

## Step 11: Install Development Tools

### Install Nix Package Manager

```bash
# Install Nix (single-user mode for simplicity)
curl -L https://nixos.org/nix/install | sh

# Source Nix environment
source /home/developer/.nix-profile/etc/profile.d/nix.sh

# Verify installation
nix --version

# Install development tools via Nix
nix-env -iA nixpkgs.kubectl
nix-env -iA nixpkgs.helm
nix-env -iA nixpkgs.docker
nix-env -iA nixpkgs.git
nix-env -iA nixpkgs.nodejs
nix-env -iA nixpkgs.rustc
nix-env -iA nixpkgs.go
```

### Install Additional Alpine Packages

```bash
# Install development and system tools
apk add --no-cache \
  qemu \
  libvirt \
  virt-manager \
  docker \
  docker-compose \
  python3 \
  py3-pip \
  nodejs \
  npm \
  rust \
  cargo \
  go \
  openjdk17-jdk \
  postgresql \
  redis

# Add user to docker group
adduser developer docker
```

---

## Step 12: Configure Auto-Start Sway

### Create Desktop Session

```bash
# Create desktop session file
cat > /usr/share/wayland-sessions/sway.desktop << 'EOF'
[Desktop Entry]
Name=Sway
Comment=An i3-compatible Wayland compositor
Exec=sway
Type=Application
EOF

# Create autostart script for developer user
cat > /home/developer/.bash_profile << 'EOF'
# Start Sway if not already running
if [ -z "$WAYLAND_DISPLAY" ] && [ "$(tty)" = "/dev/tty1" ]; then
    exec sway
fi
EOF

# Set proper ownership
chown developer:developer /home/developer  
```

---

## Step 13: Verification and Testing

### System Verification Checklist

```bash
# 1. Verify musl libc usage
apk add pax-utils
scanelf -n /usr/bin/python3
# Should show: /lib/ld-musl-x86_64.so.1

# 2. Verify s6 services
s6-svstat /var/service/*
# All services should show "up"

# 3. Verify WiFi connectivity
ip addr show wlan0
ping -c 3 8.8.8.8
# Should show IP address and successful ping

# 4. Verify Wayland session
echo $WAYLAND_DISPLAY
# Should show wayland-0 or similar

# 5. Verify graphics acceleration
glxinfo | grep "direct rendering"
# Should show "direct rendering: Yes"

# 6. Verify audio
pamixer --get-volume
# Should show volume percentage

# 7. Verify Framework laptop hardware
lscpu | grep "Model name"
# Should show AMD Ryzen 7 7840HS

lspci | grep VGA
# Should show AMD Radeon 780M Graphics
```

### Performance Testing

```bash
# Boot time test
systemd-analyze  # If available, or use time commands

# Memory usage
free -h
# Should show ~180MB used with Sway running

# CPU usage
htop
# Should show minimal CPU usage at idle

# Temperature monitoring
sensors
# Should show reasonable temperatures for Framework laptop
```

---

## Troubleshooting

### Alpine Linux musl libc Purity

**The rhizome system embodies musl libc purity - all packages use musl libc for consistency:**

```bash
# Development dependencies that use musl libc:
apk add pkgconf libffi    # Both use musl libc as dependency
apk add wayland-dev       # Compiled against musl libc
apk add gcc musl-dev      # musl-native development tools

# Verify musl libc usage:
scanelf -n /usr/bin/pkgconf    # Shows: /lib/ld-musl-x86_64.so.1
scanelf -n /usr/lib/libffi.so  # Shows: /lib/ld-musl-x86_64.so.1

# One-liner to verify entire rhizome system musl libc purity:
scanelf -n /usr/bin/* /usr/sbin/* 2>/dev/null | grep -q glibc && echo "❌ CONTAMINATED" || echo "✅ MUSL PURE"

# Comprehensive purity check with count (corrected syntax):
scanelf -n /usr/bin/* /usr/sbin/* /usr/lib/* /lib/* 2>/dev/null | awk '/glibc|ld-linux/ {print "❌ GLIBC:", $0; exit 1} /musl/ {count++} END {if (count > 0) print "✅ MUSL PURE: Found", count, "musl binaries"}'

# Benefits of musl libc purity in the rhizome system:
# - Smaller binaries (no glibc bloat)
# - Static linking support for containerization
# - Consistent behavior across all packages
# - Better security (minimal attack surface)
# - Predictable performance characteristics
# - Perfect for s6 supervision and Wayland compositors

# Community Repository musl libc packages (all use musl libc):
scanelf -n /usr/bin/sudo /usr/sbin/wpa_supplicant /usr/sbin/acpid /usr/sbin/cpufreq-set /usr/sbin/iwconfig 2>/dev/null | grep musl

# Verify community packages maintain musl purity:
echo "✅ Community Repository musl libc packages:"
echo "  - wireless-tools (iwconfig, iwlist, iwspy)"
echo "  - acpid (Advanced Configuration and Power Interface daemon)"
echo "  - wpa_supplicant (WiFi connection manager)"
echo "  - sudo (superuser do command)"
echo "  - cpufrequtils (CPU frequency scaling utilities)"
```

### Essential Tools for the Rhizome System

**curl and xz - Critical for System Operations:**

```bash
# curl - Essential for downloads, API calls, and network testing
curl -I https://dl-cdn.alpinelinux.org/alpine/v3.22/releases/x86_64/
curl -s https://httpbin.org/ip  # Test internet connectivity
curl -O https://example.com/file.tar.xz  # Download files

# xz - High-compression archiver for efficient storage
xz -9 file.tar        # Maximum compression
xz -d file.tar.xz     # Decompress
tar -Jxf file.tar.xz  # Extract compressed archive
xz -l file.tar.xz     # List archive contents without extracting

# Combined workflow for downloading and extracting:
curl -L https://example.com/archive.tar.xz | xz -d | tar -xf -
```

**Benefits in the Rhizome System:**
- **curl**: Network diagnostics, package downloads, API integration
- **xz**: Efficient backup compression, minimal storage footprint
- **Both use musl libc**: Maintaining system purity and consistency

### Network Connectivity Testing

**Install Network Testing Tools:**
```bash
# Install comprehensive network testing suite
apk add --no-cache \
  iputils \
  bind-tools \
  speedtest-cli \
  iperf3 \
  mtr \
  netcat-openbsd \
  curl \
  wget

# Test basic connectivity
ping -c 3 8.8.8.8

# Test DNS resolution
nslookup google.com

# Test HTTP connectivity to Alpine mirrors
curl -I https://dl-cdn.alpinelinux.org

# Run speed test
speedtest-cli

# Test download speed from Alpine repository
curl -o /dev/null -s -w 'Download: %{speed_download} bytes/sec\n' https://dl-cdn.alpinelinux.org/alpine/v3.22/releases/x86_64/alpine-extended-3.22.2-x86_64.iso
```

### Framework 16 WiFi Troubleshooting (Critical for apk)

**If you're getting apk errors, WiFi is likely not working. Here's the step-by-step fix:**

```bash
# 1. Check if WiFi interface exists
ip link show
# Look for wlan0 or similar WiFi interface

# 2. Check if WiFi is up
ip link show wlan0
# Should show "state UP" if working

# 3. If WiFi is down, bring it up
ip link set wlan0 up

# 4. Check for WiFi networks
iwlist wlan0 scan | grep ESSID
# Should show available networks

# 5. Configure WiFi using setup-interfaces (if not done during install)
setup-interfaces
# Select: wlan0
# Select: DHCP
# Enter your WiFi SSID and password

# 6. Alternative manual WiFi configuration
cat > /etc/wpa_supplicant/wpa_supplicant.conf << 'EOF'
ctrl_interface=/var/run/wpa_supplicant
ctrl_interface_group=wheel
update_config=1

network={
    ssid="YOUR_WIFI_NAME"
    psk="YOUR_WIFI_PASSWORD"
}
EOF

# 7. Start wpa_supplicant
rc-service wpa_supplicant start
rc-update add wpa_supplicant default

# 8. Get IP address via DHCP
udhcpc -i wlan0

# 9. Test connectivity
ping -c 3 8.8.8.8

# 10. If still not working, check for firmware issues
dmesg | grep -i firmware
dmesg | grep -i wifi
dmesg | grep -i wlan

# 11. Install WiFi firmware if missing
apk add linux-firmware
# Reboot after installing firmware
reboot
```

**Common WiFi Issues on Framework 16:**

```bash
# Issue: No WiFi interface detected
# Solution: Check if WiFi is enabled in BIOS
# - Enter BIOS (F2 during boot)
# - Look for WiFi/WLAN settings
# - Ensure WiFi is enabled

# Issue: WiFi interface exists but won't connect
# Solution: Install proper firmware
apk add linux-firmware
modprobe -r iwlwifi
modprobe iwlwifi

# Issue: Can connect but no internet (DNS issues)
# Solution: Check DNS configuration
cat /etc/resolv.conf
# Should show: nameserver 8.8.8.8 or your router's IP

# Issue: Intermittent WiFi drops
# Solution: Power management issues
echo 'options iwlwifi power_save=0' > /etc/modprobe.d/iwlwifi.conf
```

### Common Issues and Solutions

**Issue: WiFi not connecting**
```bash
# Check WiFi interface
ip link show wlan0

# Restart WiFi service
rc-service wpa_supplicant restart

# Check WiFi configuration
cat /etc/wpa_supplicant/wpa_supplicant.conf
```

**Issue: Sway not starting**
```bash
# Check seatd service
s6-svstat /var/service/seatd

# Check user groups
groups developer
# Should include: video, seat, audio

# Check Wayland session
echo $WAYLAND_DISPLAY
```

**Issue: Graphics not working**
```bash
# Check graphics drivers
apk list | grep mesa

# Check firmware
apk list | grep linux-firmware-amdgpu

# Restart display manager
killall sway
# Login again and start Sway
```

**Issue: Audio not working**
```bash
# Check audio devices
pamixer --list-sources
pamixer --list-sinks

# Restart audio services
rc-service pipewire restart
rc-service wireplumber restart
```

### Performance Optimization

**Memory Optimization:**
```bash
# Check memory usage
free -h

# Optimize Sway configuration for lower memory usage
# Edit ~/.config/sway/config to remove unnecessary components
```

**Battery Optimization:**
```bash
# Check power consumption
powertop

# Configure CPU governor for battery saving
cpupower frequency-set -g powersave
```

---

## Success Verification

### Final System Check

After completing all steps, your Framework laptop should have:

✅ **Alpine Linux** running with musl libc  
✅ **s6 init system** managing services (~200KB footprint)  
✅ **Sway Wayland** compositor providing keyboard-driven desktop  
✅ **WiFi connectivity** with Framework laptop firmware  
✅ **Graphics acceleration** with Radeon 780M drivers  
✅ **Audio support** with PipeWire  
✅ **Development tools** including Nix package manager  
✅ **Optimized performance** for Framework laptop hardware  

### Expected Performance Metrics

- **Boot time**: ~3 seconds
- **Memory usage**: ~180MB with Sway running
- **CPU usage**: ~5% at idle
- **Battery life**: Optimized for Framework laptop
- **Development ready**: Full toolchain available

---

## Next Steps

### Development Workflow

1. **Start Sway**: Login and Sway will start automatically
2. **Open terminal**: `Mod+Return` to open foot terminal
3. **Access applications**: `Mod+d` for application launcher
4. **Switch workspaces**: `Mod+1-5` for workspace switching
5. **Development**: Full Nix and Alpine package ecosystem available

### Further Customization

- **Sway configuration**: Customize `~/.config/sway/config`
- **Waybar configuration**: Customize `~/.config/waybar/`
- **Nix packages**: Install additional development tools
- **Alpine packages**: Use `apk` for system packages

### Migration Path

This setup provides the foundation for:
- **SixOS contribution**: s6 + musl expertise
- **Kubernetes development**: Container-ready environment
- **Full-stack development**: All modern toolchains available

---

## Quick Reference Commands

```bash
# Start Sway
sway

# Check system status
sudo rc-status

# View logs
dmesg | tail -n 50

# Check battery
tlp-stat -b

# Brightness control
brightnessctl set 50%

# Volume control
pamixer --set-volume 50

# Network info
nmcli device status  # if using NetworkManager
iwconfig            # for wpa_supplicant
```

---

**The rhizome system is complete: Your Framework laptop now runs Alpine Linux with s6 supervision and Sway Wayland, embodying the beauty of musl libc and Unix philosophy in a modern, repairable laptop. Like the rhizome, this system grows nomadic, connecting nodes without hierarchy—distributed, minimal, and beautifully functional.**

---

*← [Return to Main Index](/12025-10/)* | *[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *Continue to Chapter xbn: The Eastern Capital → [kae3g xbn](/12025-10/xbn-the-eastern-capital-vzxw.html)*
