---
title: "9303. Framework Laptop Alpine Linux Installation Guide"
date: 2025-10-21
phase: "framework-alpine-install-guide"
series: "technical-implementation"
status: "complete"
sort-order: 9303
version: "vzxw"
---

# kae3g 9303: Framework Laptop Alpine Linux Installation Guide — musl libc + s6 + Sway Wayland

**Timestamp:** 12025-10-21--coldriver-tundra  
**Series:** Technical Implementation  
**Category:** Framework Laptop, Alpine Linux, s6 Init, Sway Wayland, musl libc  
**Reading Time:** 35 minutes

> **"In the spirit of the microbrewery's rebirth—from fermentation to computation—we now turn to the personal forge: your Framework Laptop. This modular machine, with its AMD Ryzen 7 7840HS and Radeon 780M Graphics, becomes the gateway to our Alpine-powered experiments. Here, we craft a minimal, performant OS foundation with musl libc's elegance, s6's lightweight supervision, and Sway's efficient Wayland tiling—ready for QEMU/KVM dev, Kubernetes prototyping, and Nix reproducibility."**

---

## The Framework Laptop: Perfect Bones for Alpine

*We stand before a Framework Laptop 16, its modular design gleaming under the workshop lights. The Infrastructure Architect runs her hands along the aluminum chassis, feeling the precision engineering beneath.*

*"This," she says, "is the perfect machine for our Alpine experiment. Modular, repairable, powerful—it embodies the same principles as our microbrewery cluster, but in a portable form."*

*She opens the laptop, revealing the AMD Ryzen 7 7840HS processor and Radeon 780M Graphics. "These are the tools we need for serious development work—musl libc for clean code, s6 for reliable supervision, and Sway for efficient workflows."*

## Hardware Specifications

**Framework Laptop 16:**
- **CPU**: AMD Ryzen 7 7840HS (8C/16T, 3.8GHz base, 5.1GHz boost)
- **GPU**: Radeon 780M Graphics (RDNA 3, 12 CUs)
- **RAM**: 16GB+ DDR5 (user-upgradeable)
- **Storage**: NVMe SSD (user-replaceable)
- **Display**: 13.5" 2256×1504 @ 60Hz
- **BIOS**: Insyde IFGP6.03.05
- **Expansion**: USB-C, USB-A, HDMI, microSD, audio jack

**Why This Hardware for Alpine:**
```clojure
{:framework-alpine-advantages
 {:amd-ryzen-7840hs
  "Zen 4 architecture with excellent Linux support
   Low power consumption for battery life
   High performance for development workloads
   Virtualization support for KVM/QEMU"
  
  :radeon-780m-graphics
  "RDNA 3 architecture with open-source drivers
   Excellent Wayland support
   Hardware acceleration for video/graphics
   No proprietary driver requirements"
  
  :modular-design
  "User-replaceable components
   Easy hardware upgrades
   Repairable by design
   Aligns with open-source philosophy"
  
  :framework-ecosystem
  "Active Linux community support
   Official Linux compatibility
   Regular firmware updates
   Expansion card ecosystem"}}
```

---

## Prerequisites and Preparation

### Required Materials

**Hardware:**
- Framework Laptop 16 (AMD Ryzen 7 7840HS)
- USB-C thumb drive (8GB+ recommended)
- Ethernet cable or WiFi access
- External keyboard/mouse (optional, for initial setup)

**Software:**
- Alpine Linux Extended ISO (includes firmware)
- USB flashing tool (Rufus, Balena Etcher, or dd)
- Terminal access for preparation

### Download Alpine Linux Extended ISO

**Why Extended Version:**
The Extended ISO includes additional firmware and drivers essential for modern hardware like the Framework laptop, particularly for WiFi, Bluetooth, and graphics. For AMD Ryzen 7 7840HS with Radeon 780M Graphics, the Extended ISO is essential because:

- **AMD Microcode Updates**: Includes `amd-ucode` package for better stability, security, and performance
- **Graphics Firmware**: Contains `linux-firmware-amd` for Radeon 780M acceleration and performance
- **WiFi Firmware**: Framework's WiFi card requires specific firmware to connect to networks
- **Reduced Network Dependency**: ~200 common packages included, reducing need for internet during installation
- **Hardware Compatibility**: Ensures modern AMD hardware works correctly from initial boot

**Multi-AI Consensus**: All AI systems (Meta, Deepseek, Gemini, Grok) recommend Extended ISO for Framework laptops with AMD hardware due to firmware requirements and microcode updates.

```bash
# Download Alpine Linux Extended ISO (Latest: 3.22.2)
wget https://dl-cdn.alpinelinux.org/alpine/v3.22/releases/x86_64/alpine-extended-3.22.2-x86_64.iso

# Verify checksum
wget https://dl-cdn.alpinelinux.org/alpine/v3.22/releases/x86_64/alpine-extended-3.22.2-x86_64.iso.sha256
sha256sum -c alpine-extended-3.22.2-x86_64.iso.sha256
```

### Create Bootable USB Drive

**Method 1: Balena Etcher (Recommended)**
1. Download and install [Balena Etcher](https://www.balena.io/etcher/)
2. Select the Alpine Extended ISO
3. Select your USB-C drive
4. Click "Flash!" and wait for completion

**Method 2: Command Line (Linux/macOS)**
```bash
# Identify USB device (REPLACE /dev/sdX with your USB drive)
lsblk

# Write ISO to USB (WILL ERASE TARGET DEVICE)
sudo dd if=alpine-extended-3.22.2-x86_64.iso of=/dev/sdX bs=4M status=progress && sync
```

---

## BIOS Configuration

### Access Framework BIOS

1. **Power off** Framework laptop completely
2. **Hold F2** during power-on to enter BIOS
3. **BIOS Version**: IFGP6.03.05 (Insyde)

### Essential BIOS Settings

**Security Menu:**
```
☐ Secure Boot → **DISABLED** (Required for Alpine)
☐ TPM Device → DISABLED (Optional)
☐ Intel Platform Trust → DISABLED (Optional)
```

**Boot Menu:**
```
☑ USB Boot → ENABLED
☑ UEFI Boot → ENABLED  
☐ Legacy Boot → DISABLED
Boot Order:
  1. USB UEFI Drive
  2. NVMe UEFI OS
```

**Advanced Menu:**
```
AMD CBS → 
  ☑ SVM Mode → ENABLED (Virtualization for KVM)
  ☑ IOMMU → ENABLED (For GPU passthrough)
  
Chipset Configuration →
  ☑ Above 4G Decoding → ENABLED
  ☑ Re-Size BAR Support → ENABLED
```

**Save & Exit:**
- **Save Changes and Reset**
- Keep USB drive connected

---

## Alpine Linux Installation

### Boot from USB

1. System will boot to Alpine login prompt
2. Login as `root` (no password required)
3. Verify hardware detection:
   ```bash
   # Check CPU
   cat /proc/cpuinfo | grep "model name"
   # Should show: AMD Ryzen 7 7840HS
   
   # Check GPU
   lspci | grep -i vga
   # Should show: Advanced Micro Devices [AMD/ATI] Phoenix1 [Radeon 780M]
   ```

### Network Configuration

**WiFi Setup (Recommended for Framework):**
```bash
# Configure WiFi interface
setup-interfaces

# Select:
# - Interface: wlan0
# - Method: DHCP
# - SSID: Your network name
# - Passphrase: Your WiFi password

# Test connectivity
ping -c 3 dl-cdn.alpinelinux.org
```

**Ethernet Setup (Alternative):**
```bash
# Configure Ethernet interface
setup-interfaces

# Select:
# - Interface: eth0
# - Method: DHCP
```

### Run Alpine Setup

```bash
# Start Alpine setup script
setup-alpine
```

**Configuration Steps:**

#### Keyboard & Hostname
```
Select keyboard layout: us
Enter system hostname: framework-alpine
```

#### Network Interface
```
Available interfaces: 
- eth0 (Ethernet) 
- wlan0 (WiFi) - Framework AMD

Select interface: wlan0  # or eth0 for wired
IP address: dhcp
```

#### Password & Timezone
```
Root password: [Enter secure password]
Re-enter password: [Confirm]

Time zone: America/Los_Angeles
```

#### Package Mirror
```
Mirror: 1 (dl-cdn.alpinelinux.org)
```

#### SSH Server
```
Which SSH server? openssh
```

#### Disk Partitioning (CRITICAL FOR FRAMEWORK)

```
Available disks:
- nvme0n1 (Framework NVMe SSD)

Which disk? nvme0n1

How would you like to use it? sys
```

**WARNING:** This will erase entire disk. For dual-boot, choose `custom` and create separate partitions.

#### Erase and Install
```
WARNING: This will erase /dev/nvme0n1. Continue? y
```

### Post-Installation Setup

```bash
# Reboot into installed system
reboot

# Remove USB when prompted
# Boot into Alpine from NVMe
```

---

## Framework-Specific Configuration

### Login and Update

```bash
# Login as root with your password
apk update && apk upgrade

# Install Framework essentials
apk add --no-cache \
  linux-firmware-iwlwifi \     # WiFi drivers
  linux-firmware-amdgpu \      # Radeon 780M graphics
  intel-ucode \               # Microcode updates
  acpi acpid \                # Power management
  cpupower \                  # CPU frequency control
  tlp \                       # Advanced power management
  elogind \                   # Session management
  dbus \                      # System message bus
```

### Enable Framework Services

```bash
# ACPI for power management
rc-update add acpid default
rc-service acpid start

# CPU power management for Ryzen 7 7840HS
cpupower frequency-set -g performance

# Enable WiFi if not already configured
rc-update add wpa_supplicant default
rc-service wpa_supplicant start

# Enable system services
rc-update add dbus default
rc-update add elogind default
rc-update add tlp default
rc-service dbus start
rc-service elogind start
rc-service tlp start
```

---

## s6 Init System Installation

### Install s6 Supervision Suite

```bash
# Install s6 and dependencies
apk add --no-cache \
  s6 s6-rc s6-linux-init \
  s6-portable-utils s6-dns \
  execline

# Create s6 service directories
mkdir -p /etc/s6/services/{sshd,chronyd,networking,wpa_supplicant,acpid,dbus,elogind}

# Configure s6 as init system
s6-linux-init-maker -1 /etc/s6/rc/init

# Backup original inittab and replace with s6
cp /etc/inittab /etc/inittab.backup
cat > /etc/inittab << 'EOF'
::sysinit:/etc/s6/init/init-stage1
::wait:/etc/s6/init/init-stage2  
::shutdown:/etc/s6/init/init-shutdown
EOF
```

### Create s6 Services for Framework

**SSH Service:**
```bash
cat > /etc/s6/services/sshd/run << 'EOF'
#!/bin/execlineb -P
s6-setuidgid root
/usr/sbin/sshd -D
EOF
```

**Network Service:**
```bash
cat > /etc/s6/services/networking/run << 'EOF'
#!/bin/execlineb -P
s6-setuidgid root
/etc/init.d/networking start
EOF
```

**WiFi Service:**
```bash
cat > /etc/s6/services/wpa_supplicant/run << 'EOF'
#!/bin/execlineb -P
s6-setuidgid root
wpa_supplicant -i wlan0 -c /etc/wpa_supplicant/wpa_supplicant.conf
EOF
```

**ACPI Service:**
```bash
cat > /etc/s6/services/acpid/run << 'EOF'
#!/bin/execlineb -P
s6-setuidgid root
/usr/sbin/acpid
EOF
```

**Make services executable:**
```bash
chmod +x /etc/s6/services/*/run
```

### Configure s6-rc

```bash
# Create service definitions
mkdir -p /etc/s6-rc/source

# SSH service definition
cat > /etc/s6-rc/source/sshd/type << 'EOF'
longrun
EOF

# Network service definition
cat > /etc/s6-rc/source/networking/type << 'EOF'
oneshot
EOF

# WiFi service definition
cat > /etc/s6-rc/source/wpa_supplicant/type << 'EOF'
longrun
EOF

cat > /etc/s6-rc/source/wpa_supplicant/dependencies << 'EOF'
networking
EOF

# Compile and activate services
s6-rc-compile /etc/s6-rc/compiled /etc/s6-rc/source
s6-rc change default
```

---

## Sway Wayland GUI Installation

### Install Sway and Dependencies

```bash
# Install Wayland components
apk add --no-cache \
  sway foot waybar mako \          # Wayland compositor & tools
  grim slurp wl-clipboard \        # Screenshot & clipboard
  seatd \                          # Seat management
  mesa-dri-gallina \               # Radeon 780M graphics
  mesa-va-gallina \                # Video acceleration
  mesa-vulkan-radeon \             # Vulkan support
  firefox \                        # Web browser
  ttf-dejavu \                     # Fonts
  dbus \                           # For some applications
  elogind \                        # Session management
  pipewire pipewire-alsa \         # Audio server
  wireplumber \                    # Session manager
  pamixer \                        # Audio control
  light \                          # Brightness control
  alsa-utils                       # Audio utilities

# Add user to video and seat groups
adduser $USER video
adduser $USER seat

# Start seatd service
rc-update add seatd default
rc-service seatd start
```

### Create User Account

```bash
# Create non-root user for GUI
adduser -g "Your Name" yourusername
addgroup yourusername wheel  # For sudo

# Install sudo
apk add sudo
visudo  # Uncomment %wheel ALL=(ALL) ALL
```

### Framework-Specific Sway Configuration

```bash
# Create Sway config directory
mkdir -p ~/.config/sway

# Create optimized Framework config
cat > ~/.config/sway/config << 'EOF'
# Framework Laptop AMD Ryzen 7 7840HS Configuration

# Input configuration
input "1:1:AT_Translated_Set_2_keyboard" {
    xkb_layout us
    repeat_delay 250
    repeat_rate 30
}

# Framework laptop touchpad
input "2:7:SynPS/2_Synaptics_TouchPad" {
    tap enabled
    natural_scroll enabled
    accel_profile adaptive
}

# Output configuration - Framework 13" display
output eDP-1 {
    mode 2256x1504@60Hz
    scale 1
    background ~/.config/sway/wallpaper.png solid
}

# Key bindings
set $mod Mod4

# Application launchers
bindsym $mod+Return exec foot
bindsym $mod+Shift+Return exec foot --fullscreen
bindsym $mod+d exec firefox
bindsym $mod+p exec grim -g "$(slurp)" - | wl-copy

# System controls
bindsym $mod+Shift+e exec swaynag -t warning -m 'Exit Sway?' -b 'Yes' 'swaymsg exit'
bindsym XF86MonBrightnessDown exec light -U 5
bindsym XF86MonBrightnessUp exec light -A 5
bindsym XF86AudioLowerVolume exec pamixer --decrease 5
bindsym XF86AudioRaiseVolume exec pamixer --increase 5
bindsym XF86AudioMute exec pamixer --toggle-mute

# Workspaces for development
set $ws1 "1: terminal"
set $ws2 "2: browser" 
set $ws3 "3: virt"
set $ws4 "4: code"
set $ws5 "5: monitor"

# Assign applications to workspaces
assign [app_id="firefox"] $ws2
assign [class="virt-manager"] $ws3

# Status bar
bar {
    position top
    status_command waybar
    colors {
        statusline #ffffff
        background #323232
        inactive_workspace #32323200 #32323200 #5c5c5c
    }
}
EOF
```

### Install Development Tools

```bash
# Development tools
apk add --no-cache \
  git vim htop \                  # Basic tools
  go rust python3 nodejs \        # Programming languages
  make cmake clang musl-dev \     # Build tools
  kvm qemu libvirt virt-manager   # Virtualization

# Enable audio services
rc-update add alsa default
rc-service alsa start
```

---

## Nix Package Manager Integration

### Install Nix

```bash
# Install Nix (single-user mode for simplicity)
curl -L https://nixos.org/nix/install | sh

# Source Nix environment
. /home/yourusername/.nix-profile/etc/profile.d/nix.sh

# Verify installation
nix --version
```

### Configure Nix for Alpine

```bash
# Create Nix configuration directory
mkdir -p /etc/nix

# Configure Nix for Alpine Linux
cat > /etc/nix/nix.conf << 'EOF'
# Nix configuration for Alpine Linux
build-users-group = nixbld
sandbox = false
extra-sandbox-paths = /bin/sh=/bin/sh
substituters = https://cache.nixos.org/ https://cache.ngi0.nixos.org/
trusted-public-keys = cache.nixos.org-1:6NCHdD59X431o0gWypbMrAURkbJ16ZPMQFGspcDShjY= cache.ngi0.nixos.org-1:bjZrnw+0vYc8jXrvX1rQp60FqQjhrl2eI1U3S9xAQf4=
EOF

# Create nixbld group and users
groupadd -g 30000 nixbld
for i in $(seq 1 10); do
  useradd -u $((30000 + i)) -g nixbld -G nixbld -d /var/empty -s /bin/false nixbld$i
done
```

### Install Development Tools via Nix

```bash
# Install Nix packages for development
nix-env -iA nixpkgs.kubectl
nix-env -iA nixpkgs.helm
nix-env -iA nixpkgs.k9s
nix-env -iA nixpkgs.docker
nix-env -iA nixpkgs.containerd
nix-env -iA nixpkgs.runc
nix-env -iA nixpkgs.cni-plugins
nix-env -iA nixpkgs.crictl
```

---

## Final Configuration & Testing

### Enable Services

```bash
# Enable essential services
rc-update add seatd default
rc-update add dbus default
rc-update add alsa default
rc-update add libvirtd default

# Start services
rc-service seatd start
rc-service dbus start
rc-service alsa start
rc-service libvirtd start
```

### Framework Hardware Verification

```bash
# Verify graphics (Radeon 780M)
lspci | grep -i vga
# Should show: Advanced Micro Devices [AMD/ATI] Phoenix1 [Radeon 780M]

# Check CPU (Ryzen 7 7840HS)
cat /proc/cpuinfo | grep "model name"
# Should show: AMD Ryzen 7 7840HS

# Verify WiFi
iwconfig
# Should show wlan0 with Framework WiFi

# Check battery
acpi -i

# Verify s6 init system
ps -p 1
# Should show: /init
```

### Start Sway

```bash
# Switch to user account
su - yourusername

# Start Sway
sway
```

---

## Troubleshooting Framework-Specific Issues

### WiFi Not Working

```bash
# Install additional firmware if needed
apk add linux-firmware-other

# Reset WiFi
rc-service wpa_supplicant restart
wpa_cli -i wlan0 reconfigure
```

### Graphics Issues (Radeon 780M)

```bash
# Check if AMDGPU is loaded
lsmod | grep amdgpu

# Install additional GPU firmware
apk add linux-firmware-amdgpu

# Check Wayland session
echo $WAYLAND_DISPLAY
```

### Audio Not Working

```bash
# Restart audio services
rc-service alsa restart
pactl info  # Check PipeWire status

# Test audio
speaker-test -c 2
```

### Power Management

```bash
# Check TLP status
tlp-stat -s

# Configure CPU governor
cpupower frequency-set -g powersave  # For battery
cpupower frequency-set -g performance  # For performance
```

### s6 Service Issues

```bash
# Check s6 service status
s6-rc -u list

# Restart specific service
s6-rc -u restart sshd

# Check service logs
s6-log /etc/s6/services/sshd/log
```

---

## Performance Optimization

### Framework Laptop Specific Tweaks

```bash
# CPU governor for development
cpupower frequency-set -g performance

# Enable CPU boost
echo 1 > /sys/devices/system/cpu/cpufreq/boost

# GPU power management
echo manual > /sys/class/drm/card0/device/power_dpm_force_performance_level
echo high > /sys/class/drm/card0/device/power_dpm_force_performance_level
```

### Memory Optimization

```bash
# Check memory usage
free -h

# Optimize swappiness for SSD
echo 10 > /proc/sys/vm/swappiness

# Enable zswap for better memory compression
echo 1 > /sys/module/zswap/parameters/enabled
```

---

## Next Steps and Development Workflow

### Development Environment Setup

1. **Configure Git:**
   ```bash
   git config --global user.name "Your Name"
   git config --global user.email "your.email@example.com"
   ```

2. **Set up SSH keys:**
   ```bash
   ssh-keygen -t ed25519 -C "your.email@example.com"
   ```

3. **Install additional development tools:**
   ```bash
   nix-env -iA nixpkgs.zig
   nix-env -iA nixpkgs.hare
   nix-env -iA nixpkgs.rust-analyzer
   ```

### QEMU/KVM Virtualization

```bash
# Enable KVM
modprobe kvm_amd
rc-update add kvm default

# Add user to libvirt group
adduser yourusername libvirt

# Test virtualization
virt-manager
```

### Kubernetes Development

```bash
# Install minikube
nix-env -iA nixpkgs.minikube

# Start local cluster
minikube start --driver=kvm2
```

---

## The Path Forward: From Framework to SixOS

*The Infrastructure Architect closes the Framework laptop, satisfied with the clean, minimal system running within.*

*"This," she says, "is the foundation. Alpine Linux gives us the musl libc we need. s6 gives us the supervision we need. Sway gives us the efficiency we need. But this is just the beginning."*

*"From here, we can contribute to Chimera Linux—help it mature into the future we envision. And when SixOS is ready, we'll be ready too—not just as users, but as builders, as contributors, as architects of the future."*

### Your Next Steps

1. **Master the Framework Setup**: Get comfortable with Alpine + s6 + Sway
2. **Contribute to Chimera**: Help improve Framework laptop support
3. **Study SixOS**: Learn the architecture and contribute
4. **Build the Future**: Create the tools and systems we need

**The transformation is complete: from microbrewery to personal forge, from hops to hopes, from brewing to building.**

---

*← [Return to Main Index](/12025-10/)* | *[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *Continue to Chapter xbn: The Eastern Capital → [kae3g xbn](/12025-10/xbn-the-eastern-capital-vzxw.html)*
