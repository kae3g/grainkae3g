# grainkae3g: Framework 16 Development Environment 🖤🤎💙

**Personal sovereignty stack for Ubuntu 24.04 LTS on Framework 16**

> *"From granules to grains to THE WHOLE GRAIN"*  
> *"Chaos coming out from outside calmly so it's feeling new and what's inside is staying really solid watching observing"*  
> *"Feeling like a leaf in the wind but feeling like a rock"*

---

## 🌾 **The Grain Network Philosophy**

**"Local Control, Global Intent"** - Content creators suggest, local users decide  
**"Purpose-Built Over Generic"** - Right tool for the right job  
**"Declarative Over Imperative"** - EDN configs, not bash scripts  
**"Template/Personal Everywhere"** - Share defaults, preserve customization  
**"Real Resources Matter"** - Crypto backed by real hardware/electricity/labor  
**"Pragmatic Branding Over Dogmatic Renaming"** - gb command, bb.edn files (Babashka requirement)  
**"From Granules to Grains to THE WHOLE GRAIN"** - Small → Medium → Complete ecosystem

---

## 🌐 **Grain Network Websites**

### **Main Sites**
- 🌾 **Grain Network Hub**: https://kae3g.github.io/grainkae3g/ (coming soon)
- 📝 **kae3g Writings**: https://kae3g.codeberg.page/12025-10/ | https://kae3g.github.io/12025-10/
- 🏢 **grainpbc Organization**: https://github.com/grainpbc

### **Core Libraries**
- 🔧 **clojure-sixos** (typo handling): https://grainpbc.github.io/clojure-sixos/
- ⚙️ **clojure-s6** (supervision): https://grainpbc.github.io/clojure-s6/
- 🌐 **clojure-icp** (ICP integration): https://grainpbc.github.io/clojure-icp/
- 🔄 **clotoko** (Clojure→Motoko): https://grainpbc.github.io/clotoko/
- 📦 **grain-metatypes** (type system): https://grainpbc.github.io/grain-metatypes/

### **Platform & Apps**
- 🌐 **grainweb** (browser): https://grainpbc.github.io/grainweb/
- 🎵 **grainmusic** (streaming): https://grainpbc.github.io/grainmusic/
- 🌍 **grainspace** (platform): https://grainpbc.github.io/grainspace/

### **Documentation**
- 📚 **Full Website List**: [docs/infrastructure/GRAIN-NETWORK-WEBSITES.md](docs/infrastructure/GRAIN-NETWORK-WEBSITES.md)
- 🔗 **Cross-Linking Strategy**: [docs/architecture/GRAIN-MARKDOWN-INDEXING-STRATEGY.md](docs/architecture/GRAIN-MARKDOWN-INDEXING-STRATEGY.md)

---

> **Note:** This is a personal template. Fork this repository and customize it with your own username, content, and configuration. See [Template Customization](#-template-customization) below.
>
> **Future:** This project will migrate to the `grainnetwork` GitHub organization as both `grainnetwork/grainstore` (dependency management focus) and `grainnetwork/grainnetwork` (student network focus) - identical mirrors with different README emphasis.

---

## 🌾 **The Grainbarrel Build System**

**"gb" - The Grain Network's Build System**

```bash
# Install Grainbarrel
bb scripts/install-grainbarrel.bb

# Core commands
gb --version               # Grainbarrel v1.0.0
gb help                    # Full Grain-themed help
gb grainstore:validate     # Validate 31 modules
gb grainstore:stats        # Show statistics
gb grainstore:list         # List all modules
gb grainstore:generate-docs # Generate documentation

# Module tasks (cross-module execution)
gb display:info            # Display information
gb nightlight:status       # Check warm lighting
gb wifi:status             # WiFi status
```

**The Chaos/Solidity Dynamic:**
- **External**: Calm chaos flowing out (creative expression, 7 modules in 72 hours)
- **Internal**: Solid core staying grounded (pure functional architecture, core values)
- **Observer**: You, watching yourself create, documenting the journey
- **Together**: Productive creativity from stable center

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
