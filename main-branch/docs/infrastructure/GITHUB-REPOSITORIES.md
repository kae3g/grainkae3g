# GitHub Repositories - Grain Network

**Owner**: `kae3g`  
**Organization**: `grainpbc`  
**Last Updated**: 2025-01-22  
**CI/CD**: GrainCI

---

## Repository Structure

The Grain Network maintains repositories across two GitHub accounts:

1. **Personal** (`kae3g`): Development, templates, personal projects
2. **Organization** (`grainpbc`): Official releases, public-facing projects

---

## Personal Repositories (`kae3g`)

### 1. grainkae3g
- **URL**: `https://github.com/kae3g/grainkae3g`
- **Description**: Personal implementation of Grain Network template
- **Status**: Active Development
- **Privacy**: Public
- **CI/CD**: GrainCI enabled
- **Branches**: `main`, `tundra`
- **Purpose**: Personal workspace, template testing, development
- **Key Files**:
  - `PSEUDO.md` - Aspirational technical vision
  - `docs/` - Documentation and course materials
  - `grainstore/` - Submodules and dependencies
  - `scripts/` - Babashka automation scripts

### 2. 12025-10 (Legacy)
- **URL**: `https://github.com/kae3g/12025-10`
- **Description**: Original immutable template (GitHub Pages)
- **Status**: Archived/Immutable
- **Privacy**: Public
- **CI/CD**: None (frozen)
- **Purpose**: Historical reference, immutable documentation links
- **Note**: DO NOT MODIFY - kept for GitHub Pages links

---

## Organization Repositories (`grainpbc`)

### Core Infrastructure

#### 1. grain-metatypes
- **URL**: `https://github.com/grainpbc/grain-metatypes`
- **Description**: Meta-type definitions for Grain Network
- **Status**: Active Development
- **Privacy**: Public
- **License**: GPL-3.0
- **CI/CD**: GrainCI
- **Contents**:
  - Grainmark type definitions
  - Grainclay path specifications
  - Grainframe schemas
  - Network protocol types
- **Dependencies**: None (foundational)
- **Used By**: All Grain projects

#### 2. grainconv
- **URL**: `https://github.com/grainpbc/grainconv`
- **Description**: Universal type conversion for Grain Network
- **Status**: Planned
- **Privacy**: Public
- **License**: GPL-3.0
- **CI/CD**: GrainCI
- **Purpose**: Convert between Graintypes (ezconv.com equivalent)
- **Features**:
  - Web API
  - Desktop GUI (Humble UI)
  - Mobile apps
  - FFmpeg integration

#### 3. grainclay
- **URL**: `https://github.com/grainpbc/grainclay`
- **Description**: Networked rolling release package manager
- **Status**: Active Development
- **Privacy**: Public
- **License**: GPL-3.0
- **CI/CD**: GrainCI
- **Features**:
  - Immutable path watching
  - Automatic updates
  - s6 daemon supervision
  - ICP integration

#### 4. grainweb
- **URL**: `https://github.com/grainpbc/grainweb`
- **Description**: Decentralized social network (Clojure X)
- **Status**: Active Development
- **Privacy**: Public
- **License**: GPL-3.0
- **CI/CD**: GrainCI
- **Features**:
  - Nostr integration
  - Bluesky integration
  - Threads integration
  - Urbit Azimuth identity
  - ICP subnet deployment

#### 5. grainmusic
- **URL**: `https://github.com/grainpbc/grainmusic`
- **Description**: Decentralized music streaming platform
- **Status**: Active Development
- **Privacy**: Public
- **License**: GPL-3.0
- **CI/CD**: GrainCI
- **Features**:
  - Clotoko ICP app
  - Cross-platform (web, mobile, desktop)
  - Artist sovereignty
  - Grimes integration

### Development Tools

#### 6. clotoko
- **URL**: `https://github.com/grainpbc/clotoko`
- **Description**: Clojure to Motoko transpiler
- **Status**: Active Development
- **Privacy**: Public
- **License**: GPL-3.0
- **CI/CD**: GrainCI
- **Purpose**: ICP canister development in Clojure
- **Features**:
  - Syntax mapping
  - Type system integration
  - Standard library

#### 7. clojure-s6
- **URL**: `https://github.com/grainpbc/clojure-s6`
- **Description**: Clojure wrappers for s6 supervision
- **Status**: Active Development
- **Privacy**: Public
- **License**: GPL-3.0
- **CI/CD**: GrainCI
- **Dependencies**: s6
- **Used By**: clojure-sixos, grainclay, grainweb

#### 8. clojure-sixos
- **URL**: `https://github.com/grainpbc/clojure-sixos`
- **Description**: Clojure utilities for SixOS development
- **Status**: Planned
- **Privacy**: Public
- **License**: GPL-3.0
- **CI/CD**: GrainCI
- **Dependencies**: clojure-s6
- **Purpose**: SixOS system integration

#### 9. clojure-icp (aka clojure-dfinity)
- **URL**: `https://github.com/grainpbc/clojure-icp`
- **Symlink**: `clojure-dfinity` → `clojure-icp`
- **Description**: Clojure library for ICP integration
- **Status**: Active Development
- **Privacy**: Public
- **License**: GPL-3.0
- **CI/CD**: GrainCI
- **Features**:
  - Canister interaction
  - Principal management
  - Candid serialization

### Hardware & Devices

#### 10. grainwriter
- **URL**: `https://github.com/grainpbc/grainwriter`
- **Description**: E-ink writing device (80×110 Grainframes)
- **Status**: Active Development
- **Privacy**: Public
- **License**: GPL-3.0 (hardware: CERN-OHL)
- **CI/CD**: GrainCI
- **Features**:
  - RAM-only storage (64GB)
  - Inkplate display
  - Refurbished AMD hardware
  - Coreboot firmware
  - Military-grade ruggedness

#### 11. graincamera
- **URL**: `https://github.com/grainpbc/graincamera`
- **Description**: Open hardware camera with AVIF support
- **Status**: Planned
- **Privacy**: Public
- **License**: CERN-OHL-P-2.0
- **Purpose**: AVIF-native digital camera

#### 12. grainpack
- **URL**: `https://github.com/grainpbc/grainpack`
- **Description**: External GPU jetpack for Grainwriter
- **Status**: Planned
- **Privacy**: Public
- **License**: CERN-OHL-P-2.0
- **Features**:
  - Drop/water resistant
  - Refurbished AMD GPU
  - SixOS compatible

### Utilities

#### 13. clojure-photos
- **URL**: `https://github.com/grainpbc/clojure-photos`
- **Description**: Cloud photos app with Nostr/ICP
- **Status**: Planned
- **Privacy**: Public
- **License**: GPL-3.0
- **Features**:
  - HEIC, PNG, JPG, AVIF support
  - Fujifilm metadata parsing
  - Nostr backup
  - ICP storage

#### 14. clojure-unicode-pdf-80-x-110
- **URL**: `https://github.com/grainpbc/clojure-unicode-pdf-80-x-110`
- **Description**: Minimal PDF library for Grainframes
- **Status**: Planned
- **Privacy**: Public
- **License**: GPL-3.0
- **Purpose**: Generate PDFs from 80×110 Grainframes

### Templates & Documentation

#### 15. grainnetwork-template
- **URL**: `https://github.com/grainpbc/grainnetwork-template`
- **Description**: General template for Grain Network setup
- **Status**: Planned
- **Privacy**: Public
- **License**: GPL-3.0
- **Purpose**: Ubuntu 24.04 LTS + Framework 16 template
- **Note**: Users create "grainversions" of this template

#### 16. grain-course
- **URL**: `https://github.com/grainpbc/grain-course`
- **Description**: High school semester course materials
- **Status**: Active Development
- **Privacy**: Public
- **License**: CC-BY-SA-4.0
- **Purpose**: "Building the Grain Network" course
- **Lessons**:
  - Lesson 5: The Harmony of 80 and 110
  - Lesson 6: Advanced Type Systems and Networked Data
  - (More planned)

### Deployment & Infrastructure

#### 17. grainci
- **URL**: `https://github.com/grainpbc/grainci`
- **Description**: Automated CI/CD system for Grain Network
- **Status**: Planned
- **Privacy**: Public
- **License**: GPL-3.0
- **Features**:
  - GitHub Actions integration
  - Multi-distro package building
  - Append-only rule enforcement
  - Automated deployment
  - Cross-platform testing

### Platform-Specific

#### 18. nixos-qemu-framework16
- **URL**: `https://github.com/grainpbc/nixos-qemu-framework16`
- **Description**: NixOS QEMU setup for Framework 16 laptop
- **Status**: Planned
- **Privacy**: Public
- **License**: GPL-3.0
- **Purpose**: Nested virtualization template

---

## Repository Naming Conventions

### Prefixes

- **grain-**: Core infrastructure (e.g., grain-metatypes)
- **clojure-**: Clojure libraries (e.g., clojure-s6)
- **clotoko**: Clojure-Motoko related

### Suffixes

- No suffix: Applications/tools
- **-template**: Templates for users to fork

---

## GrainCI Configuration

All repositories MUST include:

### 1. `.github/workflows/grainci.yml`

```yaml
name: GrainCI

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Run tests
        run: bb test
  
  lint:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Run linter
        run: bb lint
  
  append-only:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
        with:
          fetch-depth: 0
      - name: Check append-only rule
        run: bb append-only-check
  
  build:
    runs-on: ubuntu-latest
    needs: [test, lint, append-only]
    steps:
      - uses: actions/checkout@v2
      - name: Build all platforms
        run: bb build-all
  
  deploy:
    runs-on: ubuntu-latest
    needs: build
    if: github.ref == 'refs/heads/main'
    steps:
      - uses: actions/checkout@v2
      - name: Deploy to all platforms
        run: bb deploy
```

### 2. `bb.edn`

```clojure
{:tasks
 {:requires ([babashka.fs :as fs])
  
  test
  {:doc "Run tests"
   :task (clojure "-M:test")}
  
  lint
  {:doc "Run linter"
   :task (clojure "-M:lint")}
  
  append-only-check
  {:doc "Check append-only rule compliance"
   :task (load-file "scripts/append-only-check.bb")}
  
  build-all
  {:doc "Build for all platforms"
   :depends [build-nix build-brew build-pacman build-apt]}
  
  deploy
  {:doc "Deploy to all platforms"
   :depends [deploy-github deploy-codeberg deploy-pages]}}}
```

---

## Cross-Repository Dependencies

```
grain-metatypes (foundational)
    ↓
    ├─→ clojure-s6
    │       ↓
    │   clojure-sixos
    │       ↓
    ├─→ grainclay
    ├─→ clojure-icp
    │       ↓
    ├─→ clotoko
    │       ↓
    ├─→ grainweb
    ├─→ grainmusic
    ├─→ grainconv
    └─→ grainwriter
```

---

## Package Distribution

All repositories publish to:

1. **Homebrew**: `brew install grainpbc/tap/<package>`
2. **Nix**: `nix-env -iA nixpkgs.<package>`
3. **Arch (AUR)**: `yay -S <package>`
4. **Debian/Ubuntu**: `apt install <package>`
5. **Grainclay**: `grainclay install <ship>/<desk>/<package>`

---

## Development Workflow

### 1. Fork Template

```bash
# Create grainversion of template
gh repo create kae3g/my-grain-project --template grainpbc/grainnetwork-template
```

### 2. Development

```bash
# Clone
git clone https://github.com/kae3g/my-grain-project
cd my-grain-project

# Install dependencies
bb deps

# Run tests
bb test

# Build
bb build
```

### 3. Contribution

```bash
# Create feature branch
git checkout -b feature/my-feature

# Make changes (append-only enforced)
# ...

# Push
git push origin feature/my-feature

# Create PR
gh pr create
```

---

## Repository Status Legend

- **Active Development**: Regular commits, actively maintained
- **Planned**: Design complete, implementation pending
- **Archived**: Immutable, no longer updated
- **Deprecated**: Being phased out

---

## Contacts

- **Admin**: `kae3g`
- **Organization**: `grainpbc`
- **Email**: `kj3x39@gmail.com`
- **Discussions**: GitHub Discussions on `grain-metatypes`
- **Issues**: Repository-specific GitHub Issues

---

## Future Repositories

Planned additions:

- `grainspace-web` - Web frontend for Grainspace
- `grain-docs` - Unified documentation
- `grain-website` - Marketing website
- `grainstore-cli` - CLI for Grainstore management
- `grain-mobile` - React Native mobile app
- `grain-browser-extension` - Browser extension for Grainweb

---

**Total Repositories**: 18 active + 5 planned = **23 total**

**Last Audit**: 2025-01-22  
**Next Audit**: 2025-02-22

