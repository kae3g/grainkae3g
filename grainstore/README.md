# Grainstore: Verified Dependencies & Submodules

**Neovedic Timestamp**: `12025-10-22--1600--CDT--moon-vishakha--09thhouse16--kae3g`

The Grainstore is a curated collection of verified, open-source dependencies and projects that form the foundation of the Grain Network ecosystem.

---

## 🌾 **PHILOSOPHY**

> *"From granules to grains to THE WHOLE GRAIN"*  
> *"Chaos coming out from outside calmly so it's feeling new and what's inside is staying really solid watching observing"*

The Grainstore embodies the Grain Network's commitment to:
- **Transparency**: All dependencies are open-source and auditable
- **Sustainability**: Focus on long-term maintainability  
- **Security**: Verified licenses and security practices
- **Community**: Building on and contributing to open-source
- **Education**: Each module documented for learning
- **Pragmatic Branding**: gb command, bb.edn files (Babashka requirement)
- **Template/Personal Split**: Share defaults, preserve customization
- **Real Resources Matter**: Crypto backed by real hardware/electricity/labor

---

## 📦 **MODULES**

### **Core Libraries**

#### **clojure-s6**
S6 supervision suite bindings for Clojure
- **Path**: `grainstore/clojure-s6/`
- **License**: MIT
- **Purpose**: Process supervision and service management
- **Status**: Active development
- **Repository**: https://github.com/grainpbc/clojure-s6

#### **clojure-sixos**
SixOS integration and file system watching
- **Path**: `grainstore/clojure-sixos/`
- **License**: MIT
- **Purpose**: System-level integration, file watching, typo-catching
- **Status**: Active development
- **Repository**: https://github.com/grainpbc/clojure-sixos

#### **clojure-icp**
Internet Computer Protocol (ICP/DFINITY) integration
- **Path**: `grainstore/clojure-icp/`
- **License**: MIT
- **Purpose**: ICP canister interaction, identity management
- **Aliases**: `clojure-dfinity`
- **Status**: Active development
- **Repository**: https://github.com/grainpbc/clojure-icp

#### **clojure-google-drive-mcp**
Google Drive Model Context Protocol bindings
- **Path**: `grainstore/clojure-google-drive-mcp/`
- **License**: MIT
- **Purpose**: Google Drive API integration via MCP
- **Status**: Active development
- **Repository**: https://github.com/grainpbc/clojure-google-drive-mcp

---

### **Transpilers & Compilers**

#### **clotoko**
Clojure-to-Motoko transpiler for ICP canisters
- **Path**: `grainstore/clotoko/`
- **License**: MIT
- **Purpose**: Compile Clojure to Motoko for ICP deployment
- **Aliases**: `clomoko`
- **Status**: Design phase
- **Repository**: https://github.com/grainpbc/clotoko

---

### **Package Management**

#### **grainclay**
Networked rolling release package manager
- **Path**: `grainstore/grainclay/`
- **License**: MIT
- **Purpose**: Immutable versioning, Clay filesystem, package distribution
- **Status**: Active development
- **Repository**: https://github.com/grainpbc/grainclay

#### **grainconv**
Universal type conversion system
- **Path**: `grainstore/grainconv/`
- **License**: MIT
- **Purpose**: Convert between file formats, Grainmarks, media types
- **Status**: Design phase
- **Repository**: https://github.com/grainpbc/grainconv

---

###  **Applications**

#### **grainweb**
Browser + Git explorer + Cursor/OpenAI Atlas alternative
- **Path**: `grainstore/grainweb/`
- **License**: MIT
- **Purpose**: Decentralized web client with Nostr, ICP, Urbit integration
- **Status**: Active development
- **Repository**: https://github.com/grainpbc/grainweb

#### **grainmusic**
Decentralized music streaming platform
- **Path**: `grainstore/grainmusic/`
- **License**: MIT
- **Purpose**: Solana Audius integration, ICP payments, Phantom wallet
- **Status**: Design phase
- **Repository**: https://github.com/grainpbc/grainmusic

#### **grainspace**
Unified decentralized platform (grainstore + App Store + gallery)
- **Path**: `grainstore/grainspace/`
- **License**: MIT
- **Purpose**: Multi-platform deployment, Urbit identity integration
- **Status**: Active development
- **Repository**: https://github.com/grainpbc/grainspace

#### **graindrive**
Google Drive collaboration system
- **Path**: `grainstore/graindrive/`
- **License**: MIT
- **Purpose**: Real-time collaborative editing with Humble UI
- **Status**: Active development
- **Repository**: https://github.com/grainpbc/graindrive

---

### **Hardware**

#### **grainwriter**
RAM-only e-ink writing device
- **Path**: `grainstore/grainwriter/`
- **License**: CERN OHL v2
- **Purpose**: Open-hardware writing device with infinite battery life
- **Status**: Design phase
- **Repository**: https://github.com/grainpbc/grainwriter

#### **graindroid**
Open-hardware Android phone (Grainphone)
- **Path**: `grainstore/graindroid/`
- **License**: CERN OHL v2
- **Purpose**: Dual-display (OLED + E-ink) repairable Android phone
- **Status**: Design phase
- **Repository**: https://github.com/grainpbc/graindroid

#### **graincamera**
Open-hardware camera with native AVIF support
- **Path**: `grainstore/graincamera/`
- **License**: CERN OHL v2
- **Purpose**: Photography with open formats and full control
- **Status**: Design phase
- **Repository**: https://github.com/grainpbc/graincamera

#### **grainpack**
External GPU jetpack attachment for Grainwriter
- **Path**: `grainstore/grainpack/`
- **License**: CERN OHL v2
- **Purpose**: Refurbished AMD GPU in recyclable drop/water-resistant case
- **Status**: Design phase
- **Repository**: https://github.com/grainpbc/grainpack

---

### **Infrastructure**

#### **grain-metatypes**
Foundational type definitions (Marks, Grainframes)
- **Path**: `grainstore/grain-metatypes/`
- **License**: MIT
- **Purpose**: Type system, Mark definitions, Grainframe specs
- **Status**: Active development
- **Repository**: https://github.com/grainpbc/grain-metatypes

#### **grainneovedic**
Neovedic timestamp system
- **Path**: `grainstore/grainneovedic/`
- **License**: MIT
- **Purpose**: Holocene calendar + Vedic nakshatras timestamps
- **Aliases**: `neovedic-timestamp`
- **Status**: Active development
- **Repository**: https://github.com/grainpbc/grainneovedic

#### **grainsource**
Git-compatible decentralized version control
- **Path**: `grainstore/grainsource/`
- **License**: GPL v2
- **Purpose**: Alternative to Git with Grain Network integration
- **Status**: Design phase
- **Repository**: https://github.com/grainpbc/grainsource

---

### **Operating Systems**

#### **grainnixos-qemu-within-ubuntu-24-04-lts-for-framework-16-laptop**
NixOS QEMU virtualization template
- **Path**: `grainstore/grainnixos-qemu.../`
- **License**: MIT
- **Purpose**: NixOS development environment within Ubuntu host
- **Status**: Design phase
- **Repository**: https://github.com/grainpbc/grainnixos-qemu-within-ubuntu-24-04-lts-for-framework-16-laptop

---

### **Organization**

#### **grainpbc**
Public Benefit Corporation documentation
- **Path**: `grainstore/grainpbc/`
- **License**: CC BY-SA 4.0
- **Purpose**: Business plans, legal framework, branding, press materials
- **Status**: Active development
- **Repository**: https://github.com/grainpbc/grainpbc

---

## 🔧 **GRAINSTORE MANAGEMENT**

### **Adding Dependencies**

All dependencies are managed via Babashka scripts and EDN configuration.

#### **grainstore.edn**
```clojure
{:modules
 {:clojure-s6 {:repo "https://github.com/grainpbc/clojure-s6"
               :path "grainstore/clojure-s6"
               :license "MIT"}
  :clojure-sixos {:repo "https://github.com/grainpbc/clojure-sixos"
                  :path "grainstore/clojure-sixos"
                  :license "MIT"}
  :clojure-icp {:repo "https://github.com/grainpbc/clojure-icp"
                :path "grainstore/clojure-icp"
                :license "MIT"
                :aliases ["clojure-dfinity"]}
  :clojure-google-drive-mcp {:repo "https://github.com/grainpbc/clojure-google-drive-mcp"
                              :path "grainstore/clojure-google-drive-mcp"
                              :license "MIT"}
  ;; ... more modules
  }}
```

### **Babashka Tasks**

```bash
# Load all grainstore modules
bb grainstore:load

# Update all modules
bb grainstore:update

# Verify licenses
bb grainstore:verify-licenses

# List all modules
bb grainstore:list

# Add new module
bb grainstore:add <name> <repo-url>

# Remove module
bb grainstore:remove <name>

# Sync with upstreams
bb grainstore:sync
```

---

## 📊 **DEPENDENCY GRAPH**

```
grainkae3g (root)
├── grainstore/
│   ├── clojure-s6                    (daemon management)
│   │   └── used by: clojure-sixos, graindrive, grainweb
│   │
│   ├── clojure-sixos                 (file watching, typo-catching)
│   │   ├── depends on: clojure-s6
│   │   └── used by: graindrive, grainweb, grainclay
│   │
│   ├── clojure-icp                   (ICP integration)
│   │   └── used by: grainweb, grainmusic, grainspace
│   │
│   ├── clojure-google-drive-mcp      (Google Drive API)
│   │   └── used by: graindrive
│   │
│   ├── clotoko                       (Clojure→Motoko)
│   │   ├── depends on: clojure-icp
│   │   └── used by: grainweb, grainmusic
│   │
│   ├── grainclay                     (package manager)
│   │   ├── depends on: clojure-sixos, grain-metatypes
│   │   └── used by: graindrive, grainweb, grainsource
│   │
│   ├── grainconv                     (type conversion)
│   │   ├── depends on: grain-metatypes
│   │   └── used by: grainweb, grainmusic
│   │
│   ├── grainweb                      (web client)
│   │   ├── depends on: clojure-s6, clojure-sixos, clojure-icp
│   │   │              clotoko, grainclay, grain-metatypes
│   │   └── standalone app
│   │
│   ├── grainmusic                    (music platform)
│   │   ├── depends on: clojure-icp, clotoko, grain-metatypes
│   │   └── standalone app
│   │
│   ├── grainspace                    (unified platform)
│   │   ├── depends on: clojure-icp, grainweb, grainmusic
│   │   └── standalone app
│   │
│   ├── graindrive                    (Google Drive collaboration)
│   │   ├── depends on: clojure-s6, clojure-sixos, grainclay
│   │   │              clojure-google-drive-mcp, grain-metatypes
│   │   └── standalone app
│   │
│   ├── grain-metatypes               (type definitions)
│   │   └── used by: all modules
│   │
│   ├── grainneovedic                 (timestamps)
│   │   └── used by: all modules
│   │
│   └── [hardware, infrastructure, org modules]
```

---

## 🔒 **LICENSE VERIFICATION**

All Grainstore modules have been verified for permissive, allowing licenses:

### **Software Licenses**
- **MIT**: clojure-s6, clojure-sixos, clojure-icp, clojure-google-drive-mcp, clotoko, grainclay, grainconv, grainweb, grainmusic, grainspace, graindrive, grain-metatypes, grainneovedic
- **GPL v2**: grainsource (Git compatibility)
- **CC BY-SA 4.0**: grainpbc (documentation)

### **Hardware Licenses**
- **CERN OHL v2**: grainwriter, graindroid, graincamera, grainpack

All licenses allow:
- ✅ Commercial use
- ✅ Modification
- ✅ Distribution
- ✅ Private use

---

## 📚 **DOCUMENTATION**

Each module contains:
- `README.md` - Overview and quick start
- `docs/` - Comprehensive documentation
- `examples/` - Usage examples
- `API.md` - API reference (if applicable)

---

## 🧪 **TESTING**

```bash
# Run all Grainstore tests
bb grainstore:test

# Test specific module
bb grainstore:test clojure-s6

# Integration tests
bb grainstore:test:integration

# Coverage report
bb grainstore:test:coverage
```

---

## 🚀 **DEPLOYMENT**

### **Multi-Platform Targets**

Grainstore modules support deployment to:
- **Linux**: Nix, APT, Pacman, Homebrew (Linuxbrew)
- **macOS**: Homebrew, Nix
- **Android**: Graindroid Phone
- **iOS**: (future)
- **Web**: SvelteKit, Clojure web frameworks
- **ICP**: Canisters via Clotoko

---

## 🔄 **SYNC & UPDATE STRATEGY**

### **Upstream Sync**
```bash
# Pull latest changes from all upstream repositories
bb grainstore:sync

# This will:
# 1. Fetch from all upstream repos
# 2. Check for conflicts
# 3. Update local modules
# 4. Verify licenses
# 5. Run tests
# 6. Update grainstore.edn
```

### **Conflict Resolution**
- Auto-merge if no conflicts
- Manual review required for conflicts
- Grainclay preserves all versions

---

## 🌾 **GRAIN NETWORK INTEGRATION**

The Grainstore is the foundation of the Grain Network's:
1. **High School Course**: All modules used as teaching examples
2. **UIUC Club**: Real-world projects using Grainstore modules
3. **Grain PBC**: Business applications built on Grainstore
4. **Hardware Projects**: Grainwriter, Grainphone, Graincamera
5. **Educational Platform**: Learning by building with open-source

---

## 📖 **CONTRIBUTING**

See individual module READMEs for contribution guidelines.

General principles:
- Follow existing code style
- Add tests for new features
- Update documentation
- Use neovedic timestamps for commits
- Sign commits with GPG key

---

## 🔗 **LINKS**

- **Organization**: https://github.com/grainpbc
- **Website**: https://grain.network (planned)
- **Documentation**: https://kae3g.github.io/grainkae3g/
- **Codeberg Mirror**: https://kae3g.codeberg.page/grainkae3g/

---

**The Grainstore: Building the future with verified, open-source modules.** 🌾

---

*Created: 12025-10-22--1600--CDT--moon-vishakha--09thhouse16--kae3g*  
*License: CC BY-SA 4.0*  
*Part of the Grain Network ecosystem*
