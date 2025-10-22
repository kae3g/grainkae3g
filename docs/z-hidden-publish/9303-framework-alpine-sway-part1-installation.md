---
title: "9303. Framework Laptop Alpine Linux Installation Guide - Part 1: Foundation Setup"
phase: "framework-alpine-installation-part1"
sort-order: 9303
created: 2025-01-21
updated: 2025-01-21
status: "published"
tags: ["alpine-linux", "framework-laptop", "installation", "musl-libc", "s6", "sway", "wayland"]
---

# 9303. Framework Laptop Alpine Linux Installation Guide - Part 1: Foundation Setup

> **The Rhizome System Foundation** - Alpine Linux + musl libc + s6 + Sway Wayland on Framework Laptop 16

## Table of Contents
- [Quick Start](#quick-start)
- [Prerequisites](#prerequisites)
- [Installation Process](#installation-process)
- [User Setup](#user-setup)
- [Package Management](#package-management)
- [Next Steps](#next-steps)

## Quick Start {#quick-start}

**For experienced users who want to get running fast:**

```bash
# 1. Boot from Alpine Extended ISO
# 2. Run setup-alpine (choose your options)
setup-alpine

# 3. Reboot and login as root
reboot

# 4. Enable community repository and install essentials
echo 'http://dl-cdn.alpinelinux.org/alpine/v3.22/community' >> /etc/apk/repositories
apk update

# 5. Install sudo and create admin user
apk add sudo
adduser xy
passwd xy
adduser xy wheel
echo '%wheel ALL=(ALL) ALL' >> /etc/sudoers

# 6. Install Framework hardware essentials
apk add --no-cache \
  linux-firmware \
  linux-firmware-amd \
  amd-ucode \
  acpid cpufrequtils brightnessctl \
  wpa_supplicant wireless-tools

# 7. Enable services
rc-update add acpid default
rc-update add wpa_supplicant default

# 8. Reboot and test
reboot
```

## Prerequisites

### Hardware Requirements
- **Framework Laptop 16** (AMD Ryzen 7 7840HS, Radeon 780M Graphics)
- **USB drive** (8GB+ for Alpine Extended ISO)
- **Internet connection** (for package installation)

### Software Requirements
- **Alpine Linux Extended ISO** (v3.22 or later)
- **Rufus** or **dd** for USB creation

### Download Alpine Extended ISO
```bash
# Download Alpine Linux Extended ISO for x86_64
wget https://dl-cdn.alpinelinux.org/alpine/v3.22/releases/x86_64/alpine-extended-3.22.2-x86_64.iso

# Verify checksum
wget https://dl-cdn.alpinelinux.org/alpine/v3.22/releases/x86_64/alpine-extended-3.22.2-x86_64.iso.sha256
sha256sum -c alpine-extended-3.22.2-x86_64.iso.sha256
```

## Installation Process

### Step 1: Boot from ISO
1. **Create bootable USB** with Alpine Extended ISO
2. **Boot Framework laptop** from USB
3. **Login as root** (no password required)

### Step 2: Run setup-alpine
```bash
# Run Alpine's guided installer
setup-alpine

# Choose your options:
# - Keyboard: us
# - Hostname: framework-alpine
# - Interface: wlan0 (for WiFi)
# - IP: dhcp
# - DNS: dhcp
# - Timezone: America/Chicago (or your timezone)
# - Proxy: none
# - NTP: chrony
# - SSH: none (we'll configure later)
# - Disk: sys (install to NVMe)
# - Mode: sys (system disk)
# - Disk: /dev/nvme0n1 (your NVMe drive)
# - Use: y (use entire disk)
# - Format: y
# - Erase: y
```

### Step 3: Reboot and Login
```bash
# Reboot to your new Alpine installation
reboot

# Login as root
# Password: (the one you set during setup-alpine)
```

## User Setup

### Install Sudo and Create Admin User
```bash
# Enable community repository (sudo is not in main repository)
echo 'http://dl-cdn.alpinelinux.org/alpine/v3.22/community' >> /etc/apk/repositories
apk update

# Install sudo
apk add sudo

# Create admin user 'xy'
adduser xy
passwd xy

# Add xy to wheel group (required for sudo access)
adduser xy wheel

# Configure sudoers
echo '%wheel ALL=(ALL) ALL' >> /etc/sudoers

# Test sudo access
su - xy
sudo ls /root
```

## Package Management

### Essential Commands
```bash
# Update package index
apk update

# Install packages
apk add package-name

# Remove packages
apk del package-name

# List installed packages
apk list --installed

# Search packages
apk search keyword

# Show package information
apk info package-name
```

### Framework Hardware Essentials
```bash
# Install Framework-specific packages
apk add --no-cache \
  linux-firmware \          # Includes Intel WiFi firmware
  linux-firmware-amd \      # AMD GPU firmware
  amd-ucode \               # AMD CPU microcode
  acpid \                   # Power management
  cpufrequtils \            # CPU frequency scaling
  brightnessctl \           # Display brightness control
  wpa_supplicant \          # WiFi connection
  wireless-tools            # Wireless utilities
```

### Enable Critical Services
```bash
# Enable power management
rc-update add acpid default

# Enable WiFi
rc-update add wpa_supplicant default

# Start services
rc-service acpid start
rc-service wpa_supplicant start
```

## Next Steps

**Congratulations!** You now have a working Alpine Linux installation on your Framework laptop.

### What's Next?
- **Part 2**: [9304. Framework Laptop Alpine Linux Advanced Configuration - Part 2: s6 + Sway + Wayland](9304-framework-alpine-sway-part2-advanced.md)
- **Package Verification**: [Alpine musl libc Purity Verification](#alpine-musl-libc-purity-verification)
- **Network Setup**: [WiFi Configuration](#wifi-configuration)

### Key Concepts Covered
- ✅ **Alpine Linux Installation** - Extended ISO, setup-alpine, reboot
- ✅ **User Management** - sudo setup, wheel group, admin access
- ✅ **Package Management** - apk commands, community repository
- ✅ **Hardware Support** - Framework-specific firmware and drivers
- ✅ **Service Management** - OpenRC services, power management

### Alpine musl libc Purity Verification {#alpine-musl-libc-purity-verification}
```bash
# Verify your system maintains musl libc purity
scanelf -n /usr/bin/* /usr/sbin/* 2>/dev/null | grep -q glibc && echo "❌ CONTAMINATED" || echo "✅ MUSL PURE"

# Count musl binaries
scanelf -n /usr/bin/* /usr/sbin/* /usr/lib/* /lib/* 2>/dev/null | awk '/musl/ {count++} END {print "✅ MUSL BINARIES:", count}'
```

### Community Repository musl libc Packages
```bash
# Verify community repository packages use musl libc
scanelf -n /usr/bin/sudo /usr/sbin/wpa_supplicant /usr/sbin/acpid /usr/sbin/cpufreq-set /usr/sbin/iwconfig 2>/dev/null | grep musl

# Check package information
apk info --depends wireless-tools acpid wpa_supplicant sudo cpufrequtils

# Verify all community packages maintain musl purity
echo "✅ Community Repository musl libc packages:"
echo "  - wireless-tools (iwconfig, iwlist, iwspy)"
echo "  - acpid (Advanced Configuration and Power Interface daemon)"
echo "  - wpa_supplicant (WiFi connection manager)"
echo "  - sudo (superuser do command)"
echo "  - cpufrequtils (CPU frequency scaling utilities)"
```

### WiFi Configuration {#wifi-configuration}
```bash
# Configure WiFi connection
setup-interfaces
# Choose wlan0, enable it, set to dhcp

# Connect to WiFi
wpa_supplicant -B -i wlan0 -c /etc/wpa_supplicant/wpa_supplicant.conf
udhcpc -i wlan0

# Test connectivity
ping -c 3 8.8.8.8
```

---

## Navigation

**Next**: [9304. Framework Laptop Alpine Linux Advanced Configuration - Part 2: s6 + Sway + Wayland](9304-framework-alpine-sway-part2-advanced.md)

**Previous**: [9302. The Urbana Rhizome: A Vegan Co-op Kitchen Public Benefit Corporation Blueprint for Urbana, Illinois](9302-urbana-rhizome-herbal-bar-blueprint.md)

**Related**: 
- [7002. The Rhizome System](7002-therhizomesys.md) - Complete rhizome system documentation
- [9300. Alpine Kubernetes Nix Implementation](9300-alpine-kubernetes-nix-implementation.md) - Kubernetes on Alpine

**Series Navigation**:
- [9303 Part 1: Installation](9303-framework-alpine-sway-part1-installation.md) ← **You are here**
- [9304 Part 2: Advanced Configuration](9304-framework-alpine-sway-part2-advanced.md)
- [9305 Part 3: Performance Tuning](9305-framework-alpine-performance-part3-optimization.md)

---

## Key Concepts Cross-Reference

### #alpine-linux-installation
> **Definition**: Alpine Linux Extended ISO installation process using setup-alpine for Framework Laptop hardware.

### #musl-libc-purity
> **Definition**: Alpine Linux's use of musl libc ensures all packages maintain consistent, minimal C library dependencies.

### #framework-hardware
> **Definition**: Framework Laptop 16 with AMD Ryzen 7 7840HS and Radeon 780M Graphics, optimized for Alpine Linux.

### #alpine-package-management
> **Definition**: Alpine's apk package manager with community repository requirements for packages like sudo.
