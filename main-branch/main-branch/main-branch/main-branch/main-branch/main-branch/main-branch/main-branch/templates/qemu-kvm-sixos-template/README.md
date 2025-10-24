# QEMU/KVM Virtualization Environment with SixOS Support

A comprehensive virtualization setup template for SixOS (NixOS without systemd) development and testing.

## 🎯 What This Template Provides

- **Complete QEMU/KVM setup** with libvirt management
- **SixOS-specific tooling** for NixOS without systemd
- **Hardware-agnostic configuration** that adapts to your system
- **Functional programming approach** with Clojure/Babashka
- **Declarative configuration** with Nix integration

## 🚀 Quick Start

### 1. Prerequisites
```bash
# Install QEMU/KVM (Ubuntu/Debian)
sudo apt update
sudo apt install -y qemu-kvm libvirt-daemon-system libvirt-clients bridge-utils virt-manager

# Install Babashka (Clojure scripting)
curl -sL https://raw.githubusercontent.com/babashka/babashka/master/install | bash -s -- --dir ~/.local/bin
export PATH="$HOME/.local/bin:$PATH"

# Install Nix (for SixOS development)
curl -L https://nixos.org/nix/install | sh
source ~/.nix-profile/etc/profile.d/nix.sh

# Add user to virtualization groups
sudo usermod -a -G kvm,libvirt $USER
```

### 2. Clone and Setup
```bash
# Clone this template
git clone <your-repo-url> my-sixos-vm-environment
cd my-sixos-vm-environment

# Make scripts executable
chmod +x scripts/*.sh scripts/*.bb

# Source Nix environment
source ~/.nix-profile/etc/profile.d/nix.sh
```

### 3. Customize for Your Hardware
```bash
# Edit hardware configuration
cp configs/hardware-template.edn configs/my-hardware.edn
# Update with your specific hardware specs
```

### 4. Build and Test
```bash
# Build SixOS ISO
bb scripts/sixos-vm-manager.bb build-iso

# Create your first VM
bb scripts/sixos-vm-manager.bb create my-sixos-dev :memory 4096 :cores 4
```

## 📁 Template Structure

```
my-sixos-vm-environment/
├── README.md                    # This file
├── templates/                   # Template files
│   ├── qemu-kvm-sixos-template/ # General template
│   └── personalized-dashboard/  # Personalized version
├── scripts/                     # Management scripts
│   ├── vm-manager.sh           # General VM management
│   ├── sixos-vm-manager.bb     # SixOS-specific (Babashka)
│   ├── hardware-detector.bb    # Auto-detect hardware
│   └── setup-environment.bb    # Environment setup
├── configs/                     # Configuration files
│   ├── hardware-template.edn   # Hardware configuration template
│   ├── vm-templates.edn        # VM configuration templates
│   └── sixos-config.edn        # SixOS-specific settings
├── vms/                        # Virtual machine storage
├── isos/                       # ISO storage
├── backups/                    # VM backups
├── logs/                       # Management logs
└── docs/                       # Documentation
    ├── hardware-setup.md       # Hardware-specific setup
    ├── sixos-guide.md          # SixOS development guide
    └── troubleshooting.md      # Common issues and solutions
```

## 🔧 Hardware Customization

### Auto-Detection
```bash
# Detect your hardware automatically
bb scripts/hardware-detector.bb > configs/my-hardware.edn
```

### Manual Configuration
Edit `configs/my-hardware.edn` with your specifications:

```clojure
{:hardware-specs
 {:cpu {:cores 8 :threads 16 :vendor "AMD" :model "Ryzen 7 7840HS"}
  :memory {:total-gb 32 :type "DDR5" :speed "5600"}
  :storage {:primary "1TB NVMe" :secondary "1TB NVMe"}
  :gpu {:integrated "AMD Radeon 780M" :discrete "AMD Radeon RX 7700S"}
  :display {:resolution "2560x1600" :size "16\""}
  :networking {:wireless "WiFi 6E" :ethernet "USB-C"}}}
```

## 🎮 VM Management

### General VMs
```bash
# List all VMs
./scripts/vm-manager.sh list

# Create a VM
./scripts/vm-manager.sh create ubuntu-server 2048 20 /path/to/iso 2

# Start/stop VMs
./scripts/vm-manager.sh start ubuntu-server
./scripts/vm-manager.sh stop ubuntu-server
```

### SixOS-Specific VMs
```bash
# Build SixOS ISO
bb scripts/sixos-vm-manager.bb build-iso

# Create SixOS VM
bb scripts/sixos-vm-manager.bb create sixos-dev :memory 8192 :cores 8

# Manage SixOS VMs
bb scripts/sixos-vm-manager.bb start sixos-dev
bb scripts/sixos-vm-manager.bb connect sixos-dev
```

## 🎨 SixOS Features

### s6 Supervision
SixOS uses s6 instead of systemd:
- **Lightweight**: 200KB vs 1.5MB for systemd
- **Unix philosophy**: Small, focused components
- **Declarative**: Services managed like Nix packages

### Declarative Configuration
```nix
# /etc/nixos/configuration.nix
{ config, pkgs, ... }:
{
  # Enable s6 supervision (instead of systemd)
  services.s6.enable = true;
  
  # Enable Wayland + Hyprland
  services.xserver.enable = false;
  programs.hyprland.enable = true;
  
  # Hardware-specific configuration
  hardware.opengl.enable = true;
  services.xserver.videoDrivers = [ "amdgpu" ];
}
```

## 🔄 Development Workflow

### 1. Build and Test
```bash
# Build SixOS ISO with your changes
bb scripts/sixos-build-iso.bb

# Test in VM
bb scripts/sixos-vm-manager.bb run-qemu /path/to/sixos.iso
```

### 2. Deploy to Hardware
```bash
# Flash to USB (when ready)
bb scripts/sixos-flash-usb.bb /dev/sdb

# Boot from USB and install
```

### 3. Iterate and Improve
```bash
# Make changes to SixOS configuration
# Build new ISO
# Test in VM
# Deploy to hardware
```

## 📊 Monitoring and Logs

### VM Status
```bash
# View VM status
bb scripts/sixos-vm-manager.bb status sixos-dev

# Monitor resources
virsh domstats sixos-dev
```

### Logs
```bash
# View management logs
tail -f logs/sixos-vm-manager.log

# View VM logs
virsh console sixos-dev
```

## 🛠️ Troubleshooting

### Common Issues
1. **Permission denied**: Ensure user is in `kvm` and `libvirt` groups
2. **Nix not found**: Source the Nix profile: `source ~/.nix-profile/etc/profile.d/nix.sh`
3. **VM won't start**: Check hardware virtualization is enabled in BIOS
4. **Graphics issues**: Ensure proper GPU drivers are installed

### Getting Help
- Check `docs/troubleshooting.md` for detailed solutions
- Review logs in `logs/` directory
- Ensure all prerequisites are installed correctly

## 🤝 Contributing

This template is designed to be:
- **Hardware-agnostic**: Works on any x86_64 system
- **Customizable**: Easy to adapt to your specific needs
- **Extensible**: Add your own scripts and configurations
- **Well-documented**: Clear setup and usage instructions

## 📚 Resources

- [SixOS Repository](https://codeberg.org/amjoseph/sixos)
- [s6 Supervision](https://skarnet.org/software/s6/)
- [NixOS Manual](https://nixos.org/manual/nixos/stable/)
- [QEMU Documentation](https://qemu.readthedocs.io/)
- [Babashka Documentation](https://babashka.org/)

## 📄 License

This template is provided under the MIT License. See LICENSE file for details.

---

**Built with ❤️ for functional programming, personal sovereignty, and the future of computing.**





