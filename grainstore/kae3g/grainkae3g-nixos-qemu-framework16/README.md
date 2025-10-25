# NixOS QEMU for Framework 16 Laptop
## *Running NixOS + Sway in QEMU on Ubuntu 24.04 LTS*

**Host:** Ubuntu 24.04 LTS  
**Guest:** NixOS 23.11+ with Sway  
**Hardware:** Framework Laptop 16 (AMD Ryzen 7 7840HS)  
**Virtualization:** QEMU/KVM with nested virtualization support  
**Configuration:** Nix Flakes + Home Manager

---

## 🎯 Purpose

This repository contains declarative NixOS configurations for running NixOS in QEMU on a Framework 16 laptop. All system changes are tracked through Nix flakes and Home Manager, making the entire VM reproducible and version-controlled.

---

## 📁 Repository Structure

```
nixos-qemu-framework16/
├── flake.nix                    # Main flake configuration
├── flake.lock                   # Locked dependencies
├── configuration.nix            # NixOS system configuration
├── hardware-configuration.nix   # VM hardware config
├── home.nix                     # Home Manager configuration
├── sway.nix                     # Sway-specific configuration
├── scripts/
│   ├── start-vm.sh             # Start NixOS VM
│   ├── stop-vm.sh              # Stop NixOS VM
│   ├── rebuild.sh              # Rebuild NixOS config
│   └── update-flake.sh         # Update flake.lock
├── docs/
│   ├── SETUP.md                # Initial setup guide
│   ├── USAGE.md                # Daily usage guide
│   └── TROUBLESHOOTING.md      # Common issues
└── README.md                    # This file
```

---

## 🚀 Quick Start

### 1. Prerequisites (Ubuntu Host)

```bash
# Enable nested virtualization (if not already)
echo "options kvm_amd nested=1" | sudo tee /etc/modprobe.d/kvm-nested.conf
sudo modprobe -r kvm_amd && sudo modprobe kvm_amd

# Install QEMU/KVM
sudo apt update
sudo apt install -y qemu-kvm qemu-system-x86 qemu-utils libvirt-daemon-system

# Verify KVM
kvm-ok  # Should say "KVM acceleration can be used"
```

### 2. Download NixOS ISO

```bash
# Create VM directory
mkdir -p ~/VMs/{iso,disks}

# Download NixOS (done automatically by scripts)
# Or manually:
wget https://channels.nixos.org/nixos-23.11/latest-nixos-minimal-x86_64-linux.iso \
  -O ~/VMs/iso/nixos-minimal.iso
```

### 3. Install NixOS

```bash
# Run installation script
./scripts/install-nixos.sh

# Follow on-screen prompts
# This will create a base NixOS installation
```

### 4. Apply Configuration

```bash
# Copy this repo into VM
./scripts/sync-config.sh

# Rebuild NixOS with flake
./scripts/rebuild.sh
```

### 5. Start VM

```bash
# Start NixOS VM
./scripts/start-vm.sh

# SSH into VM
ssh -p 2223 nixos@localhost
```

---

## 📋 Configuration Files

### flake.nix

Main entry point for NixOS configuration using flakes.

### configuration.nix

System-wide NixOS configuration:
- Boot settings
- Networking
- Users
- Sway + Wayland
- Virtualization (nested KVM)
- System packages

### home.nix

User-specific configuration via Home Manager:
- Shell (bash/zsh)
- Git config
- Sway keybindings
- Terminal (alacritty)
- Development tools

### sway.nix

Sway window manager configuration:
- Keybindings
- Waybar
- Display settings
- Warm display (gammastep)

---

## 🔧 Daily Workflow

### Making System Changes

**Edit configuration:**

```bash
# Edit NixOS config
vim configuration.nix

# Or edit Home Manager config
vim home.nix

# Or edit Sway config
vim sway.nix
```

**Apply changes:**

```bash
# Rebuild and switch (requires reboot)
./scripts/rebuild.sh

# Or rebuild and test (no reboot)
./scripts/rebuild.sh test
```

**Commit changes:**

```bash
# Add and commit
git add -A
git commit -m "Add QEMU utilities to system packages"
git push
```

### Updating Packages

```bash
# Update flake.lock
./scripts/update-flake.sh

# Rebuild with new packages
./scripts/rebuild.sh

# Commit updated lock file
git add flake.lock
git commit -m "Update flake dependencies"
git push
```

---

## 🌐 Integration with Grain Network

This NixOS VM is configured for Grain Network development:

- **Sway** - Wayland compositor for testing Grainweb
- **QEMU** - For nested Debian VMs (Layer 3)
- **Clojure** - For Grain library development
- **Git** - Integrated with Grainsource
- **ICP Tools** - DFX for canister development

---

## 🔗 Links

- **Framework 16 Docs**: https://frame.work/laptop-16
- **NixOS Manual**: https://nixos.org/manual/nixos/stable/
- **Home Manager**: https://nix-community.github.io/home-manager/
- **Nix Flakes**: https://nixos.wiki/wiki/Flakes
- **Grain Network**: https://grain.network

---

## 📝 Notes

- **VM Disk**: 40GB QCOW2 (grows dynamically)
- **RAM**: 8GB (configurable)
- **CPUs**: 4 cores (configurable)
- **SSH Port**: 2223 (host → VM)
- **Nested KVM**: Enabled for Layer 3 Debian VMs

---

**NixOS QEMU Framework 16**  
*Declarative, reproducible, version-controlled NixOS VMs* 🌾

**Part of the Grain Network Ecosystem**


