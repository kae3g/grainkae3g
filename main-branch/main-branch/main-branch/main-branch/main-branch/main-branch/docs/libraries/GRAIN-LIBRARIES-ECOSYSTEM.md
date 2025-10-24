# 📚 Grain Libraries Ecosystem
## *"Building blocks for personal sovereignty computing"*

**Created:** January 22, 2025  
**Authority:** kae3g (Graingalaxy)  
**Organization:** Grain PBC

---

## 🎯 Overview

The **Grain Libraries Ecosystem** is a comprehensive suite of open-source Clojure libraries that power the Grain Network. Each library is designed to be modular, reusable, and focused on a specific domain.

---

## 🌾 Library Catalog

### 1. clojure-s6

**Process supervision library for s6 integration**

- **Repository**: `grainstore/clojure-s6/`
- **Organization**: grainpbc
- **License**: MIT
- **Status**: ✅ Complete

**Features:**
- s6 service management
- Process supervision
- Logging utilities
- Service lifecycle hooks

**Usage:**
```clojure
(require '[clojure-s6.core :as s6])

(s6/supervise
  {:name "my-service"
   :command (fn [] (my-app))
   :restart :always})
```

---

### 2. clojure-sixos

**SixOS system management library** (Future)

- **Repository**: `grainstore/clojure-sixos/` (planned)
- **Organization**: grainpbc
- **License**: MIT
- **Status**: ⏳ Planned
- **Dependencies**: clojure-s6

**Features:**
- Declarative system configuration
- Service orchestration
- SixOS-specific integrations
- Development workflow utilities

---

### 3. clojure-icp (clojure-dfinity)

**Internet Computer Protocol (ICP) integration library**

- **Repository**: `grainstore/clojure-icp/`
- **Symlink**: `grainstore/clojure-dfinity/` → `clojure-icp/`
- **Organization**: grainpbc
- **License**: MIT
- **Status**: ✅ Complete (v0.1.0)

**Features:**
- IC Agent Interface (call, query)
- Candid type system
- Identity management
- Canister deployment and management
- Chain Fusion (Solana, Ethereum)
- Subnet management

**Usage:**
```clojure
(require '[clojure-icp.core :as icp])

(def agent (icp/create-agent))

(icp/call agent
          :canister-id "rrkah-fqaaa-aaaaa-aaaaq-cai"
          :method "greet"
          :args {:name "World"})
```

**GitHub Repositories:**
- https://github.com/grainpbc/clojure-icp
- https://github.com/grainpbc/clojure-dfinity (mirror)

---

### 4. clojure-photos

**Photo management library with decentralized storage**

- **Repository**: `grainstore/clojure-photos/`
- **Organization**: grainpbc
- **License**: MIT
- **Status**: 🔄 In Progress
- **Dependencies**: clojure-icp

**Features:**
- Photo loading/saving (AVIF, HEIC, PNG, JPEG)
- Nostr integration (NIP-94, NIP-95)
- ICP canister storage
- Urbit ship sync (planned)
- Metadata management
- Batch operations

**Usage:**
```clojure
(require '[clojure-photos.core :as photos]
         '[clojure-photos.nostr :as nostr]
         '[clojure-photos.icp :as icp])

;; Load photo
(def photo (photos/load-photo "photo.avif"))

;; Upload to ICP
(icp/upload-photo :photo photo)

;; Publish to Nostr
(nostr/publish-photo :photo photo :relay "wss://relay.grain.network")
```

**Modules:**
- `clojure-photos.core` - Photo management
- `clojure-photos.nostr` - Nostr integration
- `clojure-photos.icp` - ICP integration
- `clojure-photos.urbit` - Urbit integration (planned)
- `clojure-photos.formats.avif` - AVIF support
- `clojure-photos.formats.heic` - HEIC support
- `clojure-photos.metadata` - EXIF metadata
- `clojure-photos.processing` - Image processing

---

### 5. clojure-grainspace (grainspace)

**Unified decentralized platform library**

- **Repository**: `grainstore/grainspace/`
- **Organization**: grainpbc
- **License**: MIT
- **Status**: 🔄 In Progress
- **Dependencies**: clojure-icp, clojure-s6, clojure-sixos

**Features:**
- Identity management (Urbit Azimuth + ICP)
- Multi-chain payments (ICP + Solana)
- Decentralized storage coordination
- Grainspace Network protocol
- Desktop UI (Humble UI)

**Package Managers:**
- Nix flake ✅
- Homebrew formula ✅
- Arch PKGBUILD ✅
- Debian/Ubuntu packages ✅

---

### 6. humble-social-client

**Decentralized social media client**

- **Repository**: `grainstore/humble-social-client/`
- **Organization**: grainpbc
- **License**: MIT
- **Status**: ⏳ Planned
- **Dependencies**: clojure-s6, clojure-sixos

**Features:**
- Nostr integration (Primal API)
- Bluesky integration (AT Protocol)
- Threads integration (Meta API)
- X (Twitter) integration (planned)
- Letta memGPT for agentic feeds
- Humble UI desktop client

---

### 7. clojure-gpu (grainpack)

**GPU acceleration library for Grainpack**

- **Repository**: `grainstore/clojure-gpu/` (planned)
- **Organization**: grainpbc
- **License**: MIT
- **Status**: ⏳ Planned
- **Dependencies**: clojure-s6

**Features:**
- GPU initialization and management
- AMD GPU support (ROCm)
- NVIDIA GPU support (CUDA) (planned)
- GPU-accelerated compute
- Power management
- Temperature monitoring

---

### 8. grainsource

**Git-compatible version control system**

- **Repository**: `grainstore/grainsource/`
- **Organization**: grainpbc
- **License**: MIT
- **Status**: ⏳ Planned
- **Dependencies**: clojure-icp, clojure-s6

**Features:**
- Git-compatible core (libgit2 bindings)
- Humble UI desktop app
- ICP canister storage
- Urbit peer sync
- Solana commit signatures
- CLI tool

**Commands:**
```bash
grainsource init          # Initialize repository
grainsource harvest       # Clone
grainsource plant         # Commit
grainsource share         # Push
grainsource gather        # Pull
grainsource sync          # Bidirectional sync
```

---

## 🔗 Library Dependencies

```
┌─────────────────────────────────────────────────────┐
│                  Grain Libraries                    │
│                                                     │
│  ┌──────────────────────────────────────────────┐  │
│  │         Application Libraries                │  │
│  │  - grainsource                              │  │
│  │  - humble-social-client                     │  │
│  │  - clojure-grainspace                       │  │
│  └─────────┬────────────────────────────────────┘  │
│            │                                        │
│  ┌─────────┴────────────────────────────────────┐  │
│  │         Domain Libraries                     │  │
│  │  - clojure-photos                           │  │
│  │  - clojure-gpu                              │  │
│  └─────────┬────────────────────────────────────┘  │
│            │                                        │
│  ┌─────────┴────────────────────────────────────┐  │
│  │         Infrastructure Libraries             │  │
│  │  - clojure-icp (clojure-dfinity)            │  │
│  │  - clojure-sixos                            │  │
│  └─────────┬────────────────────────────────────┘  │
│            │                                        │
│  ┌─────────┴────────────────────────────────────┐  │
│  │         Foundation Libraries                 │  │
│  │  - clojure-s6                               │  │
│  └──────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────┘
```

---

## 📦 Package Management

### Multi-Package-Manager Support

All Grain libraries support:

1. **Nix** - Declarative package management
2. **Homebrew** - macOS and Linux (Linuxbrew)
3. **Pacman** - Arch Linux
4. **APT** - Debian and Ubuntu
5. **Leiningen** - Clojure build tool

### Installation Examples

**Via Clojure CLI (deps.edn):**
```clojure
{:deps {io.github.grainpbc/clojure-icp 
        {:git/url "https://github.com/grainpbc/clojure-icp"
         :git/sha "abc123..."}
        io.github.grainpbc/clojure-s6
        {:git/url "https://github.com/grainpbc/clojure-s6"
         :git/sha "def456..."}}}
```

**Via Homebrew:**
```bash
brew tap grainpbc/tap
brew install clojure-icp
brew install grainspace
```

**Via Pacman (AUR):**
```bash
yay -S clojure-icp
yay -S grainspace
```

**Via APT:**
```bash
sudo add-apt-repository ppa:grainpbc/stable
sudo apt update
sudo apt install clojure-icp grainspace
```

---

## 🏗️ Development Workflow

### Local Development

1. **Clone all libraries:**
   ```bash
   cd grainstore
   git clone https://github.com/grainpbc/clojure-s6
   git clone https://github.com/grainpbc/clojure-icp
   git clone https://github.com/grainpbc/clojure-photos
   ```

2. **Set up local dependencies:**
   ```clojure
   ;; In your deps.edn
   {:deps {clojure-s6 {:local/root "../grainstore/clojure-s6"}
           clojure-icp {:local/root "../grainstore/clojure-icp"}
           clojure-photos {:local/root "../grainstore/clojure-photos"}}}
   ```

3. **Start REPL with all libraries:**
   ```bash
   clojure -M:dev:repl
   ```

---

## 🌐 GitHub Organizations

### grainpbc

**Primary organization for all Grain libraries:**

- https://github.com/grainpbc/clojure-s6
- https://github.com/grainpbc/clojure-icp
- https://github.com/grainpbc/clojure-dfinity (mirror)
- https://github.com/grainpbc/clojure-photos
- https://github.com/grainpbc/grainspace
- https://github.com/grainpbc/humble-social-client
- https://github.com/grainpbc/grainsource

### grainnetwork (Planned)

**Future organization for broader ecosystem:**

- https://github.com/grainnetwork/grainstore
- https://github.com/grainnetwork/grainnetwork
- https://github.com/grainnetwork/grain-network-docs

---

## 📄 Licensing

**All Grain libraries use MIT License:**

- Permissive open-source license
- Commercial use allowed
- Modification allowed
- Distribution allowed
- Private use allowed
- No liability
- No warranty

**Hardware designs use CERN-OHL-S-2.0:**

- Open Hardware License
- Strongly reciprocal
- Patent protection
- Copyleft for hardware

---

## 🚀 Roadmap

### Phase 1: Foundation (Current)
- ✅ clojure-s6
- ✅ clojure-icp (clojure-dfinity)
- 🔄 clojure-photos

### Phase 2: Integration (2025 Q2)
- ⏳ clojure-sixos
- ⏳ clojure-grainspace
- ⏳ humble-social-client

### Phase 3: Advanced (2025 Q3)
- ⏳ clojure-gpu
- ⏳ grainsource
- ⏳ clojure-avif

### Phase 4: Ecosystem (2025 Q4)
- ⏳ grainnetwork organization migration
- ⏳ Full package manager support
- ⏳ Comprehensive documentation
- ⏳ Video tutorials

---

## 🌟 Vision

**The Grain Libraries Ecosystem provides the building blocks for personal sovereignty computing.**

Each library is:
- **Modular** - Can be used independently
- **Composable** - Works well with other libraries
- **Open Source** - MIT licensed
- **Well Documented** - Comprehensive docs and examples
- **Tested** - Full test coverage
- **Maintained** - Active development by Grain PBC

**Together, these libraries enable:**
- Decentralized identity (ICP + Urbit)
- Decentralized storage (ICP canisters)
- Decentralized social (Nostr, Bluesky, Threads)
- Decentralized version control (Grainsource)
- Self-hosted AI (Agentic Pipeline)
- Open hardware integration (Graincamera, Grainwriter, Grainpack)

**This is the foundation for the Grain Network - a complete platform for personal sovereignty in computing.** 🌾

---

**Grain Libraries Ecosystem**  
**Building blocks for personal sovereignty computing** 📚

*Part of the Grain Network - Powered by Grain PBC* 🌐

---

**Created:** January 22, 2025  
**Last Updated:** January 22, 2025  
**Authority:** kae3g (Graingalaxy)  
**Organization:** Grain PBC


