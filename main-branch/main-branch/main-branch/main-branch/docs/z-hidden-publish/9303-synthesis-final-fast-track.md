---
title: "9303-Synthesis: Final Framework Fast-Track Guide"
date: 2025-10-21
phase: "framework-fast-track-synthesis"
series: "technical-implementation"
status: "complete"
sort-order: 9303
version: "vzxw"
---

# 9303-Synthesis: Final Framework Fast-Track Guide — Multi-AI Consensus

**Timestamp:** 12025-10-21--framework-fast-track-synthesis  
**Series:** Technical Implementation  
**Category:** Framework Laptop, Alpine Linux, s6 Init, Sway Wayland, Multi-AI Synthesis  
**Reading Time:** 20 minutes

> **"In the spirit of rapid prototyping—from cluster to laptop—we streamline our Alpine foundation on the Framework. This multi-AI synthesized guide (Meta, DeepSeek, Gemini, Grok, ChatGPT, Claude) provides the fastest path to a bootable, persistent Alpine Linux system with user `xy`, hostname `xy-framework-alpine`, s6 suite, and Sway Wayland—auto-booting from NVMe in under 30 minutes."**

---

## Multi-AI Consensus Achieved

**All AI systems converge on this optimal approach:**
- **20-30 minute installation** - Single chroot session for all critical configuration
- **User `xy` with hostname `xy-framework-alpine`** - As specified across all AI outputs
- **No encryption for speed** - Skips LUKS to get running quickly
- **s6 suite with OpenRC PID1** - Best of both worlds: s6 tools + OpenRC stability
- **Framework-optimized** - AMD Ryzen 7 7840HS + Radeon 780M specific tuning

---

## Prerequisites

### Hardware Requirements
- **Framework 16 Laptop** (AMD Ryzen 7 7840HS, Radeon 780M Graphics)
- **BIOS Version**: Insyde IFGP6.03.05 or compatible
- **Storage**: NVMe SSD (`/dev/nvme0n1`)
- **Network**: WiFi or Ethernet connection

### Software Requirements
- **USB-C Drive**: 8GB+ for Alpine Extended ISO
- **Alpine Extended ISO**: v3.22.2+ (includes AMD firmware)
- **Flashing Tool**: dd, Rufus, or Etcher

### Fast-Track Assumptions
- **No dual-boot**: Wipes entire NVMe drive
- **No encryption**: Maximum speed installation
- **Single user**: Admin user `xy` with sudo access
- **Auto-boot**: GRUB configured for persistent NVMe booting

---

## Phase 1: BIOS Configuration

### Essential BIOS Settings
```bash
# Access BIOS (F2 during startup)
Secure Boot:   DISABLED
SVM/AMD-V:     ENABLED (for virtualization)
IOMMU:         ENABLED (for GPU passthrough)
Boot Order:    USB UEFI → NVMe UEFI OS
```

---

## Phase 2: Boot USB and Quick Network Setup

```bash
# Boot from Alpine Extended ISO USB
# Login as root (no password)

# Quick WiFi setup
setup-interfaces
# Select: wlan0, DHCP, enter SSID/password
# Or use eth0 for wired connection

# Verify connection
ping -c 3 dl-cdn.alpinelinux.org
```

---

## Phase 3: Fast-Track Installation

```bash
setup-alpine
```

**Quick Answers (Multi-AI Optimized):**
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

**⚠️ CRITICAL: Do NOT reboot yet!**

---

## Phase 4: Single Chroot Session (Critical Phase)

*This single session handles all critical configuration before first boot*

```bash
# Enter the new system (setup-alpine mounts to /mnt)
chroot /mnt

# Update system first
apk update && apk upgrade

# Create admin user 'xy'
adduser xy
adduser xy wheel

# Install and configure sudo
apk add sudo
echo '%wheel ALL=(ALL) ALL' >> /etc/sudoers

# Install Framework hardware essentials
apk add --no-cache \
  linux-firmware-amdgpu \
  linux-firmware-iwlwifi \
  mesa-dri-gallium mesa-va-gallium mesa-vulkan-radeon \
  acpid tlp brightnessctl \
  wpa_supplicant wireless-tools

# Enable critical services
rc-update add acpid default
rc-update add tlp default
rc-update add wpa_supplicant default

# Verify GRUB installation
apk add grub grub-efi efibootmgr
grub-install --target=x86_64-efi --efi-directory=/boot/efi --bootloader-id=alpine
grub-mkconfig -o /boot/grub/grub.cfg

# Install s6 suite (keep OpenRC as PID1 for stability)
apk add --no-cache \
  s6 s6-rc s6-linux-init \
  s6-portable-utils s6-dns \
  execline

# Exit chroot
exit

# Reboot to installed system
reboot
# Remove USB when prompted
```

---

## Phase 5: First Boot - Install Sway

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

---

## Phase 6: Configure Sway (Framework-Optimized)

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

---

## Phase 7: Verification and Development Setup

### System Verification
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

### Development Tools (Optional)
```bash
# Essential dev tools
sudo apk add --no-cache \
  git vim htop \
  gcc make cmake musl-dev \
  go rust python3 nodejs npm

# Virtualization (KVM/QEMU)
sudo apk add qemu libvirt virt-manager ovmf dnsmasq

# Enable libvirt
sudo rc-update add libvirtd default
sudo adduser xy libvirt

# Nix package manager (optional)
curl -L https://nixos.org/nix/install | sh
. /home/xy/.nix-profile/etc/profile.d/nix.sh
```

---

## Fast-Track Troubleshooting

### Quick Fixes for Common Issues

**WiFi doesn't auto-connect on boot:**
```bash
sudo rc-update add wpa_supplicant default
sudo rc-service wpa_supplicant restart
```

**Sway won't start:**
```bash
# Verify groups
groups
# Must include: video, input, seat

# Check seatd is running
sudo rc-service seatd status

# Start manually
sudo rc-service seatd start
```

**Black screen with AMD graphics:**
```bash
# Ensure firmware is installed
apk info linux-firmware-amdgpu

# If missing
sudo apk add linux-firmware-amdgpu
sudo reboot
```

**GRUB doesn't auto-boot:**
```bash
# Check EFI boot order
sudo efibootmgr -v

# Reinstall GRUB if needed
sudo grub-install --target=x86_64-efi --efi-directory=/boot/efi --bootloader-id=alpine
sudo grub-mkconfig -o /boot/grub/grub.cfg

# Set boot order in BIOS (F2)
```

**Audio not working:**
```bash
# Check PipeWire is running
pw-cli ls Node

# If not running
sudo rc-service pipewire start
```

---

## Success Criteria

✅ **Boot**: Auto-boots from NVMe SSD via GRUB on power-up  
✅ **Persistence**: All changes saved to ext4 filesystem  
✅ **User**: `xy` with sudo access  
✅ **Hostname**: `xy-framework-alpine`  
✅ **Graphics**: Sway Wayland with AMD acceleration  
✅ **Init**: s6 suite installed (OpenRC as PID1 for stability)  
✅ **Network**: WiFi auto-connects on boot  
✅ **Power**: TLP and ACPI active for battery optimization  
✅ **Audio**: PipeWire working with Sway  

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
iwconfig
ip addr show wlan0

# Check s6 services (if using s6 supervision)
s6-svstat /var/service/*
```

---

## Alternative: Manual Disk Partitioning

If you prefer manual control over partitioning instead of `setup-alpine`:

```bash
# Partition disk
fdisk /dev/nvme0n1
# Create:
# - 512MB EFI partition (type EF00)
# - Remaining space for root (type 8300)

# Format partitions
mkfs.vfat -F32 /dev/nvme0n1p1
mkfs.ext4 /dev/nvme0n1p2

# Mount
mount /dev/nvme0n1p2 /mnt
mkdir -p /mnt/boot/efi
mount /dev/nvme0n1p1 /mnt/boot/efi

# Install system
setup-disk /mnt

# Continue with Phase 4 chroot steps
```

---

## Optional: Switch to s6 as PID 1 (Advanced)

**⚠️ Only do this if you need s6 as init. Skip for fastest setup.**

```bash
# Create s6 init structure
sudo s6-linux-init-maker \
  -1 \
  -c /etc/s6 \
  -p /bin:/usr/bin \
  -u root \
  -G /lib/modules-load.d \
  /etc/s6/init

# Create basic service definitions
sudo mkdir -p /etc/s6/sv/{sshd,networking,wpa_supplicant,acpid}

# Example: SSH service
sudo tee /etc/s6/sv/sshd/run << 'EOF'
#!/bin/execlineb -P
s6-setuidgid root
/usr/sbin/sshd -D
EOF

sudo chmod +x /etc/s6/sv/sshd/run

# Update GRUB to use s6
sudo sed -i 's|GRUB_CMDLINE_LINUX_DEFAULT="|&init=/etc/s6/init/init |' /etc/default/grub
sudo grub-mkconfig -o /boot/grub/grub.cfg

# Reboot to test
sudo reboot
```

---

## Sources and Acknowledgments

**Multi-AI Synthesis Contributors:**
- **Meta**: Quick installation workflow and essential packages
- **DeepSeek**: Single chroot session optimization and s6 configuration
- **Gemini**: Alpine wiki laptop setup integration and verification steps
- **Grok**: Framework-specific hardware optimization and troubleshooting
- **ChatGPT**: Streamlined command sequences and success criteria
- **Claude**: Comprehensive synthesis and alternative approaches

**Additional Sources:**
- Alpine Linux Wiki: "Setting up a laptop"
- Framework Community: AMD Laptop guides and hardware optimization
- s6 Documentation: Process supervision and service management
- Sway Documentation: Wayland compositor configuration

---

## Final Notes

This synthesis represents the convergence of multiple AI perspectives on the optimal Alpine Linux installation for Framework laptops. The approach prioritizes:

1. **Speed**: 20-30 minute installation with minimal steps
2. **Reliability**: Single chroot session reduces failure points
3. **Framework Optimization**: Hardware-specific firmware and drivers
4. **Musl Philosophy**: Clean, minimal system aligned with Alpine's principles
5. **Future-Proofing**: s6 suite ready for advanced process supervision

**Your Framework laptop is now ready for development with Alpine + s6 + Sway—embodying the beauty of musl libc and Unix philosophy in a modern, repairable laptop.**

---

*← [Return to Main Index](/12025-10/)* | *[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *Continue to Chapter xbn: The Eastern Capital → [kae3g xbn](/12025-10/xbn-the-eastern-capital-vzxw.html)*
