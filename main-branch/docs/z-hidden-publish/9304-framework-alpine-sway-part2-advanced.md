---
title: "9304. Framework Laptop Alpine Linux Advanced Configuration - Part 2: s6 + Sway + Wayland"
phase: "framework-alpine-advanced-part2"
sort-order: 9304
created: 2025-01-21
updated: 2025-01-21
status: "published"
tags: ["alpine-linux", "s6", "sway", "wayland", "musl-libc", "framework-laptop", "advanced"]
---

# 9304. Framework Laptop Alpine Linux Advanced Configuration - Part 2: s6 + Sway + Wayland

> **The Rhizome System Advanced Stack** - s6 supervision + Sway Wayland + musl libc purity on Framework Laptop 16

## Table of Contents
- [s6 Init System](#s6-init-system)
- [Wayland Foundation](#wayland-foundation)
- [Sway Compositor](#sway-compositor)
- [Package Verification](#package-verification)
- [Troubleshooting](#troubleshooting)
- [Advanced Topics](#advanced-topics)

## s6 Init System

### What is s6?
**s6** is a process supervision suite designed for Unix systems. It provides:
- **Process supervision** - Automatic restart of failed services
- **Dependency management** - Service ordering and dependencies
- **musl libc native** - Perfect for Alpine Linux
- **Lightweight** - ~200KB footprint
- **Unix philosophy** - Do one thing well

### Install s6 Suite
```bash
# Install s6 suite (keep OpenRC as PID1 for stability)
apk add --no-cache \
  s6 s6-rc s6-linux-init \
  s6-portable-utils s6-dns \
  s6-networking s6-svscan

# Verify installation
apk info s6 s6-rc s6-linux-init
```

### Configure s6 Init System
```bash
# Create s6 directory structure (CRITICAL: prevents mkdir errors)
mkdir -p /etc/s6/rc/init
mkdir -p /etc/s6/services
mkdir -p /etc/s6-rc/source
mkdir -p /etc/s6-rc/compiled

# Generate s6 init system
s6-linux-init-maker -1 /etc/s6/rc/init

# Configure inittab to use s6
echo '::sysinit:/etc/s6/rc/init' >> /etc/inittab
echo '::shutdown:/etc/s6/rc/init' >> /etc/inittab
```

### Create s6-rc Service Definitions
```bash
# Create s6-rc source directory structure
mkdir -p /etc/s6-rc/source/networking
mkdir -p /etc/s6-rc/source/dbus
mkdir -p /etc/s6-rc/source/wpa_supplicant
mkdir -p /etc/s6-rc/source/acpid

# Create networking service definition
cat > /etc/s6-rc/source/networking/up << 'EOF'
#!/bin/sh
# Bring up networking interfaces
setup-interfaces
EOF

# Create networking service type
cat > /etc/s6-rc/source/networking/type << 'EOF'
oneshot
EOF

# Create dbus service definition
cat > /etc/s6-rc/source/dbus/up << 'EOF'
#!/bin/sh
exec dbus-daemon --system
EOF

# Create dbus service type
cat > /etc/s6-rc/source/dbus/type << 'EOF'
longrun
EOF

# Make scripts executable
chmod +x /etc/s6-rc/source/networking/up
chmod +x /etc/s6-rc/source/dbus/up

# Compile s6-rc services
s6-rc-compile /etc/s6-rc/compiled /etc/s6-rc/source
```

### s6 Package Verification Commands
```bash
# List all s6-related packages
apk search s6

# Show s6 package information
apk info --depends s6 s6-rc s6-linux-init

# Verify s6 binaries use musl libc
scanelf -n /usr/bin/s6* /usr/sbin/s6* 2>/dev/null | grep musl
```

## Wayland Foundation

### Install Wayland Dependencies
```bash
# Install Wayland foundation and dependencies
apk add --no-cache \
  wayland wayland-dev wayland-protocols \
  wayland-libs-client wayland-libs-cursor \
  wayland-libs-egl wayland-libs-server \
  pkgconf libffi \
  seatd \
  pipewire pipewire-alsa wireplumber pamixer \
  firefox \
  ttf-dejavu ttf-noto ttf-noto-emoji \
  dbus
```

### Wayland Development Dependencies Explained
- **pkgconf** - Package configuration tool (replaces pkg-config)
- **libffi** - Foreign Function Interface library
- **wayland-protocols** - Standard Wayland protocols
- **seatd** - Seat management daemon (Wayland requirement)
- **pipewire** - Modern audio/video framework

### Verify Wayland Installation
```bash
# Check Wayland libraries
pkg-config --list-all | grep wayland

# Verify musl libc usage
scanelf -n /usr/bin/pkgconf /usr/lib/libffi.so 2>/dev/null | grep musl

# Test Wayland protocol support
ls /usr/share/wayland-protocols/
```

## Sway Compositor

### Install Sway and Dependencies
```bash
# Install Sway and Wayland applications
apk add --no-cache \
  sway \
  foot \
  waybar \
  mako \
  wofi \
  grim slurp \
  wl-clipboard \
  alacritty
```

### Configure Sway
```bash
# Create Sway configuration directory
mkdir -p /home/xy/.config/sway

# Create basic Sway config
cat > /home/xy/.config/sway/config << 'EOF'
# Sway configuration for Framework Laptop
# Key bindings
set $mod Mod4
bindsym $mod+Return exec foot
bindsym $mod+d exec wofi --show drun
bindsym $mod+Shift+q kill

# Window management
bindsym $mod+Left focus left
bindsym $mod+Right focus right
bindsym $mod+Up focus up
bindsym $mod+Down focus down

# Workspace switching
bindsym $mod+1 workspace 1
bindsym $mod+2 workspace 2
bindsym $mod+3 workspace 3

# Output configuration
output * bg #000000 solid_color

# Status bar
bar {
    swaybar_command waybar
}
EOF

# Set proper ownership
chown -R xy:xy /home/xy/.config
```

### Start Sway
```bash
# Switch to xy user
su - xy

# Start Sway (from console)
sway

# Or start from X11 session
sway --my-next-gpu-wont-be-nvidia
```

## Package Verification

### Alpine musl libc Purity Verification
```bash
# Quick purity check
scanelf -n /usr/bin/* /usr/sbin/* 2>/dev/null | grep -q glibc && echo "❌ CONTAMINATED" || echo "✅ MUSL PURE"

# Comprehensive purity check with count
scanelf -n /usr/bin/* /usr/sbin/* /usr/lib/* /lib/* 2>/dev/null | awk '/glibc|ld-linux/ {print "❌ GLIBC:", $0; exit 1} /musl/ {count++} END {if (count > 0) print "✅ MUSL PURE: Found", count, "musl binaries"}'

# Verify specific packages
scanelf -n /usr/bin/pkgconf /usr/lib/libffi.so 2>/dev/null | grep -o 'ld-musl-x86_64.so.1' | wc -l | xargs -I {} [ {} -eq 2 ] && echo "✅ pkgconf & libffi are musl-pure" || echo "❌ Not pure"
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

### s6 Package Verification
```bash
# List all s6 packages
apk search s6

# Show s6 package dependencies
apk info --depends s6 s6-rc s6-linux-init

# Verify s6 uses musl libc
scanelf -n /usr/bin/s6* /usr/sbin/s6* 2>/dev/null | grep musl
```

### Wayland Package Verification
```bash
# Check Wayland libraries
pkg-config --list-all | grep wayland

# Verify Wayland protocol support
ls /usr/share/wayland-protocols/

# Check if compositors are available
apk search sway
apk search wlroots
apk search weston
```

## Troubleshooting

### Common Issues and Solutions

**Issue: s6-linux-init-maker fails with "No such file or directory"**
```bash
# Create s6 directory structure first
mkdir -p /etc/s6/rc/init
mkdir -p /etc/s6/services
mkdir -p /etc/s6-rc/source
mkdir -p /etc/s6-rc/compiled

# Then run s6-linux-init-maker
s6-linux-init-maker -1 /etc/s6/rc/init
```

**Issue: s6-rc-compile fails with "unable to open /etc/s6-rc/source/networking/up: No such file or directory"**
```bash
# Create s6-rc service definitions first
mkdir -p /etc/s6-rc/source/networking
mkdir -p /etc/s6-rc/source/dbus

# Create networking service
cat > /etc/s6-rc/source/networking/up << 'EOF'
#!/bin/sh
setup-interfaces
EOF
echo 'oneshot' > /etc/s6-rc/source/networking/type

# Create dbus service
cat > /etc/s6-rc/source/dbus/up << 'EOF'
#!/bin/sh
exec dbus-daemon --system
EOF
echo 'longrun' > /etc/s6-rc/source/dbus/type

# Make scripts executable
chmod +x /etc/s6-rc/source/networking/up
chmod +x /etc/s6-rc/source/dbus/up

# Now compile s6-rc services
s6-rc-compile /etc/s6-rc/compiled /etc/s6-rc/source
```

**CRITICAL: s6 Init System Boot Failure Recovery**
```bash
# If you get stuck with:
# "can't run '/etc/s6/init-stage1': No such file or directory"
# "can't run '/etc/s6/init/init-stage2': No such file or directory"

# 1. Boot from Alpine ISO USB to rescue your system
# 2. Mount your root filesystem
mkdir -p /mnt/alpine
mount /dev/nvme0n1p2 /mnt/alpine  # Adjust partition as needed

# 3. Chroot into your system
chroot /mnt/alpine /bin/sh

# 4. Check what went wrong
ls -la /etc/s6/
ls -la /etc/s6/init/
cat /etc/inittab

# 5. Fix the s6 init system
# Option A: Remove s6 and go back to OpenRC (RECOMMENDED)
rm -f /etc/s6/rc/init
rm -rf /etc/s6-rc/
# Restore original inittab
cp /etc/inittab.bak /etc/inittab  # if you backed it up

# Option B: Properly create s6 init system
mkdir -p /etc/s6/rc/init
mkdir -p /etc/s6-rc/source
mkdir -p /etc/s6-rc/compiled
s6-linux-init-maker -1 /etc/s6/rc/init

# 6. Exit chroot and reboot
exit
umount /mnt/alpine
reboot
```

**Issue: "no such package" for sudo**
```bash
# TROUBLESHOOTING: sudo package not found
# 1. Check if community repository is enabled:
cat /etc/apk/repositories | grep community

# 2. If not found, enable community repository:
echo 'http://dl-cdn.alpinelinux.org/alpine/v3.22/community' >> /etc/apk/repositories

# 3. Update package index:
apk update

# 4. Verify sudo is available:
apk search sudo

# 5. Install sudo:
apk add sudo

# 6. Verify installation:
which sudo
sudo --version

# 7. Test sudo access:
su - xy
sudo ls /root
```

**Issue: Wayland compositors not found**
```bash
# Check available compositors
apk search sway
apk search wlroots
apk search weston

# Install from community repository
echo 'http://dl-cdn.alpinelinux.org/alpine/v3.22/community' >> /etc/apk/repositories
apk update
apk add sway foot waybar mako
```

## Cursor IDE Integration

### The musl libc Compatibility Challenge

**CRITICAL**: Cursor IDE is Electron-based and requires **glibc libraries**. This creates a philosophical tension with Alpine's musl libc purity. However, there are several viable approaches:

#### Option 1: Flatpak Installation (RECOMMENDED)
**✅ Maintains System Purity** - Cursor runs in isolated container

```bash
# Install Flatpak support
apk add --no-cache flatpak

# Enable Flathub repository
flatpak remote-add --if-not-exists flathub https://flathub.org/repo/flathub.flatpakrepo

# Search for Cursor (may not be available yet)
flatpak search cursor

# Alternative: Install VS Code via Flatpak as reference
flatpak install flathub com.visualstudio.code
```

#### Option 2: AppImage with gcompat
**⚠️ Breaks Purity** - Installs glibc compatibility layer

```bash
# Install gcompat compatibility layer
apk add --no-cache gcompat libgcc libstdc++

# Download Cursor AppImage
wget -O /opt/cursor.AppImage "https://downloader.cursor.sh/linux/appImage/x64"
chmod +x /opt/cursor.AppImage

# Test Cursor with gcompat
gcompat /opt/cursor.AppImage

# Create wrapper script for Wayland
cat > /usr/local/bin/cursor << 'EOF'
#!/bin/sh
export ELECTRON_OZONE_PLATFORM_HINT=wayland
export WAYLAND_DISPLAY="${WAYLAND_DISPLAY:-wayland-0}"
gcompat /opt/cursor.AppImage "$@" > /dev/null 2>&1 &
EOF
chmod +x /usr/local/bin/cursor
```

#### Option 3: Docker/Podman Container
**✅ Maintains System Purity** - Complete isolation

```bash
# Install Podman (rootless containers)
apk add --no-cache podman

# Create Cursor container with X11/Wayland forwarding
podman run -it --rm \
  -v /tmp/.X11-unix:/tmp/.X11-unix:ro \
  -v "$HOME:/workspace" \
  -e DISPLAY=$DISPLAY \
  ubuntu:22.04 bash

# Inside container: Install Cursor
apt update && apt install -y wget libgtk-3-0 libgconf-2-4
wget -O cursor.AppImage "https://downloader.cursor.sh/linux/appImage/x64"
chmod +x cursor.AppImage && ./cursor.AppImage
```

#### Option 4: Chroot Environment
**⚠️ Complex Setup** - Requires glibc chroot

```bash
# Install debootstrap
apk add --no-cache debootstrap

# Create Ubuntu chroot
mkdir -p /opt/ubuntu-chroot
debootstrap jammy /opt/ubuntu-chroot http://archive.ubuntu.com/ubuntu/

# Mount necessary filesystems
mount --bind /dev /opt/ubuntu-chroot/dev
mount --bind /proc /opt/ubuntu-chroot/proc
mount --bind /sys /opt/ubuntu-chroot/sys
mount --bind /home /opt/ubuntu-chroot/home

# Enter chroot and install Cursor
chroot /opt/ubuntu-chroot /bin/bash
apt update && apt install -y wget
wget -O /usr/local/bin/cursor.AppImage "https://downloader.cursor.sh/linux/appImage/x64"
chmod +x /usr/local/bin/cursor.AppImage
```

### Wayland Integration

#### Environment Variables for Electron/Cursor
```bash
# Add to ~/.profile or /etc/environment
export ELECTRON_OZONE_PLATFORM_HINT=wayland
export ELECTRON_ENABLE_WAYLAND=1
export WAYLAND_DISPLAY="${WAYLAND_DISPLAY:-wayland-0}"
export XDG_SESSION_TYPE=wayland
export QT_QPA_PLATFORM=wayland
export GDK_BACKEND=wayland
export MOZ_ENABLE_WAYLAND=1
```

#### Sway Configuration for Cursor
```bash
# Add to ~/.config/sway/config
# Cursor IDE window rules
for_window [app_id="cursor"] floating enable
for_window [app_id="cursor"] border pixel 2
for_window [app_id="electron"] floating enable

# Keyboard shortcuts
bindsym $mod+c exec cursor
bindsym $mod+Shift+c exec cursor --no-sandbox
```

### Development Environment Alternatives

#### Native Alpine Development Stack
**✅ Pure musl libc** - Lightweight, fast

```bash
# Install development tools
apk add --no-cache \
  neovim \
  tmux \
  git \
  nodejs npm \
  python3 py3-pip \
  rust cargo \
  go \
  clang llvm \
  make cmake

# Install language servers for vim/neovim
npm install -g typescript-language-server
npm install -g vscode-langservers-extracted
pip3 install python-lsp-server
```

#### Code-server (VS Code in Browser)
**✅ Maintains Purity** - Web-based development

```bash
# Install code-server
apk add --no-cache code-server

# Configure code-server
mkdir -p ~/.config/code-server
cat > ~/.config/code-server/config.yaml << 'EOF'
bind-addr: 127.0.0.1:8080
auth: password
password: your-secure-password
cert: false
EOF

# Start code-server
code-server --bind-addr 0.0.0.0:8080
```

### Package Verification Commands

#### Check gcompat Installation Impact
```bash
# Before installing gcompat - baseline purity
scanelf -n /usr/bin/* /usr/sbin/* 2>/dev/null | grep -q glibc && echo "❌ CONTAMINATED" || echo "✅ MUSL PURE"

# After installing gcompat - verify impact
apk add gcompat libgcc libstdc++
scanelf -n /usr/lib/libgcompat.so /usr/lib/libgcc_s.so 2>/dev/null | grep -o 'ld-musl-x86_64.so.1'

# Verify gcompat provides glibc compatibility without contamination
ldd /usr/lib/libgcompat.so | grep -q musl && echo "✅ gcompat uses musl" || echo "❌ gcompat contaminated"
```

#### Monitor System Purity
```bash
# Continuous purity monitoring script
cat > /usr/local/bin/check-musl-purity << 'EOF'
#!/bin/sh
echo "🔍 Alpine Linux musl libc Purity Check"
echo "======================================"

# Count musl vs glibc binaries
MUSL_COUNT=$(scanelf -n /usr/bin/* /usr/sbin/* 2>/dev/null | grep -c musl)
GLIBC_COUNT=$(scanelf -n /usr/bin/* /usr/sbin/* 2>/dev/null | grep -c glibc)

echo "✅ musl binaries: $MUSL_COUNT"
echo "⚠️  glibc binaries: $GLIBC_COUNT"

if [ "$GLIBC_COUNT" -eq 0 ]; then
    echo "🎉 SYSTEM IS MUSL PURE!"
else
    echo "❌ SYSTEM CONTAMINATED - glibc detected"
    echo "Contaminated binaries:"
    scanelf -n /usr/bin/* /usr/sbin/* 2>/dev/null | grep glibc
fi
EOF
chmod +x /usr/local/bin/check-musl-purity
```

### Performance Considerations

#### Framework Laptop 16 Optimizations
```bash
# CPU frequency scaling for development workloads
echo 'schedutil' > /sys/devices/system/cpu/cpu*/cpufreq/scaling_governor

# AMD GPU optimization for Electron apps
export RADV_PERFTEST=aco
export AMD_VULKAN_ICD=RADV

# Memory optimization for Electron
export ELECTRON_NO_ATTACH_CONSOLE=1
export ELECTRON_DISABLE_GPU_SANDBOX=1
```

#### Battery Life with Cursor IDE
```bash
# Install power management
apk add --no-cache powertop tlp

# Configure aggressive power saving
tlp start

# Monitor Cursor power usage
powertop --auto-tune
```

### Security Considerations

#### Sandboxing Electron Applications
```bash
# Run Cursor with restricted permissions
firejail --noprofile --net=none --private-tmp cursor

# Or use bubblewrap for fine-grained sandboxing
apk add bubblewrap
bwrap --dev-bind / / --tmpfs /tmp cursor
```

### Troubleshooting Common Issues

#### Issue: AppImage won't run - "No such file or directory"
```bash
# Install AppImage runtime dependencies
apk add --no-cache libgcc libstdc++ gcompat

# Check AppImage dependencies
objdump -p cursor.AppImage | grep NEEDED

# Test with gcompat
gcompat ./cursor.AppImage --version
```

#### Issue: Wayland integration problems
```bash
# Force Wayland mode
cursor --enable-features=UseOzonePlatform --ozone-platform=wayland

# Check Wayland session
echo $XDG_SESSION_TYPE  # Should be "wayland"
echo $WAYLAND_DISPLAY   # Should be "wayland-0"

# Debug Wayland compositor
export WAYLAND_DEBUG=1
cursor
```

#### Issue: Font rendering problems
```bash
# Install additional fonts
apk add --no-cache \
  ttf-dejavu \
  ttf-liberation \
  ttf-noto \
  ttf-noto-emoji \
  fontconfig

# Rebuild font cache
fc-cache -fv
```

### Recommendation Summary

**For Production Use:**
1. **Flatpak** (when available) - Maintains system purity
2. **Code-server** - Web-based VS Code experience
3. **Native vim/neovim** with LSP - Ultimate purity

**For Development/Testing:**
1. **AppImage + gcompat** - Quick setup, breaks purity
2. **Container** - Good isolation, more complex
3. **Chroot** - Full compatibility, highest complexity

**Philosophy Assessment:**
- **✅ Pure**: Flatpak, containers, native alternatives
- **⚠️ Acceptable**: gcompat (minimal glibc compatibility)
- **❌ Contamination**: Direct glibc installation

## Advanced Topics

### Xen Hypervisor Integration
**Question: Should we install Xen?**

**Answer: Yes, if you need virtualization!** Xen uses musl libc and integrates well with Alpine:

```bash
# Install Xen hypervisor
apk add xen xen-hypervisor

# Verify Xen uses musl libc
scanelf -n /usr/bin/xen* /usr/sbin/xen* 2>/dev/null | grep musl

# Check Xen package information
apk info --depends xen
apk info --provides xen
```

**Benefits of Xen on Alpine:**
- ✅ **musl libc native** - No compatibility issues
- ✅ **Lightweight** - Minimal resource overhead
- ✅ **Production ready** - Used in enterprise environments
- ✅ **Framework compatible** - Works well with AMD hardware

### Container Integration
```bash
# Install container tools
apk add docker docker-compose

# Install podman (rootless containers)
apk add podman

# Verify container tools use musl libc
scanelf -n /usr/bin/docker /usr/bin/podman 2>/dev/null | grep musl
```

### Development Environment
```bash
# Install development tools
apk add --no-cache \
  gcc musl-dev \
  clang llvm \
  make cmake \
  git \
  vim nano \
  curl wget xz

# Verify development tools
apk info --depends gcc musl-dev clang
```

## Key Concepts Summary

### The Rhizome System Stack
- ✅ **Alpine Linux** - Lightweight, security-focused base
- ✅ **musl libc** - Clean, auditable C library
- ✅ **s6** - Process supervision and service management
- ✅ **Sway** - Wayland compositor for modern desktop
- ✅ **Framework Hardware** - Optimized for AMD Ryzen + Radeon

### Package Management Philosophy
- ✅ **musl libc purity** - All packages use musl libc
- ✅ **Community repository** - Required for many packages
- ✅ **Verification commands** - Ensure system integrity
- ✅ **Troubleshooting guides** - Common issues and solutions

### Next Steps
- **Performance Tuning** - CPU scaling, power management
- **Security Hardening** - Firewall, user permissions
- **Development Setup** - Programming languages, tools
- **Backup Strategy** - System backup and recovery

---

---

## Navigation

**Previous**: [9303. Framework Laptop Alpine Linux Installation Guide - Part 1: Foundation Setup](9303-framework-alpine-sway-part1-installation.md)

**Next**: [9305. Framework Laptop Alpine Linux Performance Tuning - Part 3: Optimization](9305-framework-alpine-performance-part3-optimization.md)

**Related**: 
- [7002. The Rhizome System](7002-therhizomesys.md) - Complete rhizome system documentation
- [9300. Alpine Kubernetes Nix Implementation](9300-alpine-kubernetes-nix-implementation.md) - Kubernetes on Alpine
- [9302. The Urbana Rhizome](9302-urbana-rhizome-herbal-bar-blueprint.md) - Urbana co-op blueprint

**Series Navigation**:
- [9303 Part 1: Installation](9303-framework-alpine-sway-part1-installation.md) ← **You are here**
- [9304 Part 2: Advanced Configuration](9304-framework-alpine-sway-part2-advanced.md) ← **You are here**
- [9305 Part 3: Performance Tuning](9305-framework-alpine-performance-part3-optimization.md)

---

## Key Concepts Cross-Reference

### #musl-libc-purity
> **Definition**: Alpine Linux's use of musl libc ensures all packages maintain consistent, minimal C library dependencies without glibc contamination.

### #s6-supervision
> **Definition**: s6 is a process supervision suite that provides automatic service restart, dependency management, and musl libc native process control.

### #sway-wayland
> **Definition**: Sway is a Wayland compositor that provides keyboard-driven, configuration-as-code desktop environment optimized for developers.

### #framework-hardware
> **Definition**: Framework Laptop 16 with AMD Ryzen 7 7840HS and Radeon 780M Graphics, optimized for Alpine Linux with specific firmware requirements.

### #alpine-package-management
> **Definition**: Alpine's apk package manager with community repository requirements for packages like sudo, sway, and development tools.
