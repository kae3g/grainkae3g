# grainkae3g: Framework 16 Development Environment 🖤🤎💙

**Personal sovereignty stack for Ubuntu 24.04 LTS on Framework 16**

> **Note:** This is a personal template. Fork this repository and customize it with your own username, content, and configuration. See [Template Customization](#-template-customization) below.
>
> **Future:** This project will migrate to the `grainnetwork` GitHub organization as both `grainnetwork/grainstore` (dependency management focus) and `grainnetwork/grainnetwork` (student network focus) - identical mirrors with different README emphasis.

---

## 🎯 **Quick Start**

### **Desktop Environment**
- **Sway**: Tiling window manager (default)
- **GNOME**: Fallback desktop environment
- **Switch**: `bb scripts/desktop-switcher.bb sway` or `bb scripts/desktop-switcher.bb gnome`

### **Essential Commands**
```bash
# Display configuration
bb scripts/display-config.bb status
bb scripts/display-config.bb night-light 2000

# Screenshot (Sway only)
Super + Ctrl + 3    # Full screen
Super + Ctrl + 4    # Area select
Super + Ctrl + 5    # Window select

# Cursor IDE
Super + Shift + C   # Launch Cursor
Super + Alt + C     # New Cursor window
```

---

## 🏗️ **Project Structure**

```
├── configs/           # System configurations
│   ├── sway/         # Sway desktop config
│   ├── waybar/       # Status bar config
│   └── gdm/          # Login manager config
├── scripts/          # Automation scripts
│   ├── display-config.bb      # Display management
│   ├── desktop-switcher.bb    # Desktop switching
│   ├── screenshot.sh          # Screenshot tool
│   └── grainstore-manager.bb  # Documentation management
├── docs/             # Documentation
│   ├── SWAY-QUICK-REFERENCE.md
│   ├── README.md
│   └── archive/      # Historical documents
├── grainstore/       # Verified dependencies
│   ├── docs/urbit-docs
│   └── docs/urbit-source
└── templates/        # Reusable templates
```

---

## 🎊 **Current Status**

### **✅ Completed**
- **Sway Desktop**: Red theme, warm display (2000K)
- **Screenshot System**: Full/area/window capture
- **Display Management**: Multiple color modes
- **Documentation**: Organized structure
- **Grainstore**: Urbit docs and source

### **🚧 In Progress**
- **SixOS Development**: NixOS variant without systemd
- **Urbit Integration**: Galaxy/star hosting setup
- **Maitreya DAW**: Clojure-based audio workstation

---

## 🎯 **Development Goals**

### **Phase 1: Foundation** ✅
- [x] Ubuntu 24.04 LTS setup
- [x] Sway desktop environment
- [x] Display configuration
- [x] Screenshot system
- [x] Documentation organization

### **Phase 2: Virtualization** 🚧
- [ ] QEMU/KVM setup
- [ ] SixOS VM creation
- [ ] Development environment

### **Phase 3: Urbit Integration** 📋
- [ ] Azimuth contract fork
- [ ] Solana L2 integration
- [ ] Galaxy/star hosting

### **Phase 4: Maitreya DAW** 📋
- [ ] Clojure development
- [ ] Humble UI integration
- [ ] Audio processing

---

## 🖤 **Personal Notes**

- **Music Identity**: `terra4m`
- **Aesthetic**: Retro terminal vibes
- **Philosophy**: Personal sovereignty through technology
- **Hardware**: Framework 16 (2560x1600@165Hz)

---

## 📚 **Documentation**

- **[Sway Quick Reference](docs/SWAY-QUICK-REFERENCE.md)** - Desktop shortcuts
- **[Configuration Guide](docs/CONFIGURATION-GUIDE.md)** - System setup
- **[Progress Summary](docs/PROGRESS-SUMMARY.md)** - Development status
- **[PSEUDO.md](docs/PSEUDO.md)** - Aspirational plan

---

**Built with ❤️ for personal sovereignty and efficient workflow.**

*"Sway: Tiling, efficient, and completely under your control."* 🎊

## 🔐 Cryptographic Security
- All commits are GPG signed and verified
- SSH keys configured for both GitHub and Codeberg

## 📄 Third-Party Licenses
- **Urbit Source Code**: MIT License (Copyright 2015-2019 Urbit/Tlon Corporation)
- **Urbit Documentation**: MIT License (Copyright 2019 Tlon Corporation)  
- **wl-gammarelay**: GPL v3 License (Copyright 2007 Free Software Foundation)
- **All other dependencies**: Permissive licenses (MIT/Apache 2.0)

See [grainstore/LICENSE-SUMMARY.md](grainstore/LICENSE-SUMMARY.md) for complete license audit.

## 🚀 Current Development
- **ICP Canister**: Fake Urbit galaxy running on local testnet
- **RISC-V Development**: QEMU emulation and seL4 integration
- **Chain Fusion**: Solana integration via ICP

## 📊 Project Status
- **Sway Desktop**: ✅ Complete with red theme and warm display
- **ICP Development**: 🚧 In progress - fake galaxy creation
- **Urbit Integration**: 📋 Planned - authentic data structures
- **RISC-V Port**: 📋 Planned - Vere interpreter migration
