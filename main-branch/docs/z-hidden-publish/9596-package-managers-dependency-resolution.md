# kae3g 9595: Package Managers - Dependency Resolution

**Phase 1: Foundations & Philosophy** | **Week 4** | **Reading Time: 19 minutes**

---

## What You'll Learn

- What package managers do (and why they exist)
- Dependency hell and how it happens
- Semantic versioning (semver: 1.2.3)
- Different approaches: apt, npm, cargo, Nix
- Why Nix solves problems others can't
- The /nix/store: Content-addressed packages
- Reproducible environments
- Grainhouse strategy through package management

---

## Prerequisites

- **[9594: Build Systems](/12025-10/9594-build-systems-source-to-binary)** - How software is built
- **[9590: Filesystem](/12025-10/9590-filesystem-hierarchical-organization)** - Where packages install
- **[9504: What Is Clojure?](/12025-10/9504-what-is-clojure)** - Mentions Nix
- **[9949: The Wise Elders](/12025-10/9949-intro-clojure-nix-ecosystem)** - Nix as Meticulous Architect

---

## The Dependency Problem

**Modern software** depends on hundreds of libraries:

```
Your App
├── web-framework (needs http-library, templating)
│   ├── http-library (needs sockets, crypto)
│   │   └── crypto (needs random, math)
│   └── templating (needs parser)
└── database-driver (needs connection-pool)
    └── connection-pool (needs threading)
```

**Managing this manually** = nightmare.

**Package managers** automate:
1. **Install** dependencies
2. **Resolve** version conflicts
3. **Update** packages
4. **Remove** packages (and unused deps)

**Plant lens**: **"Package managers are seed catalogs—track which seeds (packages) you need, where to get them, which varieties (versions) work together."**

---

## Dependency Hell

**The problem**:

```
Your App needs:
  Library A v2.0
  Library B v3.0

But:
  Library B v3.0 needs Library A v1.5
  (Conflict! You need A v2.0, but B needs A v1.5)
```

**Result**: **Can't install both!**

**Variations**:
- **Diamond dependency**: A→C, B→C (which version of C?)
- **Circular dependency**: A→B, B→A (infinite loop!)
- **Version lock**: Upgrading A breaks B

**This is dependency hell.**

---

## Semantic Versioning (Semver)

**Convention**: `MAJOR.MINOR.PATCH`

```
Example: 2.4.7
         │ │ │
         │ │ └─ Patch (bug fixes, no breaking changes)
         │ └─── Minor (new features, backward compatible)
         └───── Major (breaking changes!)
```

**Rules**:
- Patch (2.4.7 → 2.4.8): Safe to upgrade (just fixes)
- Minor (2.4.7 → 2.5.0): Safe to upgrade (new features, compatible)
- Major (2.4.7 → 3.0.0): **Might break!** (review changes)

**Example**:
```
Your app requires: react@^16.8.0
                        └─── "^" means: ≥16.8.0, <17.0.0
                             (Minor/patch OK, major NO)
```

**This helps**, but doesn't solve everything (minor versions can still break things!).

---

## Traditional Package Managers

### apt (Debian, Ubuntu)

```bash
# Install
sudo apt install nginx

# Update package list
sudo apt update

# Upgrade packages
sudo apt upgrade

# Remove
sudo apt remove nginx
```

**Model**: System-wide installation (`/usr/bin/`, `/usr/lib/`)

**Problem**: One version per package (can't have nginx 1.18 AND 1.20).

### npm (Node.js)

```bash
# Install (project-local)
npm install express

# Creates node_modules/ directory
# Can have DIFFERENT versions per project!
```

**Model**: Per-project installation (`node_modules/`)

**Problem**: Duplication (every project downloads same packages), disk space explosion.

### cargo (Rust)

```bash
# Install library (adds to Cargo.toml)
cargo add serde

# Build (downloads deps)
cargo build
```

**Model**: Per-project (like npm), but smarter caching (`~/.cargo/`).

**Better**, but still has version conflicts.

---

## Nix: The Radically Different Approach

**Nix** doesn't install packages **into** the system. It installs them **beside** it.

### The /nix/store

**Every package** gets a unique path:

```
/nix/store/abc123-nodejs-18.0.0/
/nix/store/def456-nodejs-20.0.0/
/nix/store/ghi789-nginx-1.18.0/
/nix/store/jkl012-nginx-1.20.0/
```

**Hash** (`abc123`) is based on:
- Package name + version
- All dependencies (recursively!)
- Build script
- Compiler used

**Result**: Identical inputs → identical hash → **same package**.

### Multiple Versions Coexist

```bash
# Project A uses nodejs 18
nix-shell -p nodejs-18_x

# Project B uses nodejs 20
nix-shell -p nodejs-20_x

# No conflict! Different paths in /nix/store
```

**This is impossible** with apt (system-wide = one version only).

### Atomic Upgrades

**Traditional**:
```bash
apt upgrade
# (Downloads, starts replacing files...)
# Power loss? BROKEN SYSTEM!
```

**Nix**:
```bash
nix-env -u
# Builds new profile (doesn't touch old!)
# Switch atomically
# If power loss: old system still works!
```

**Rollback**:
```bash
nix-env --rollback
# Instantly back to previous state!
```

---

## Reproducible Environments

**Traditional problem**:

```
Developer machine: Python 3.9, numpy 1.20, works fine
CI server:         Python 3.10, numpy 1.22, fails!
```

**Nix solution**:

```nix
# shell.nix
{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  buildInputs = [
    pkgs.python39
    pkgs.python39Packages.numpy_1_20
  ];
}
```

**Everyone runs**:
```bash
nix-shell
# Now: EXACT same Python, EXACT same numpy
# Developer, CI, production: identical!
```

**This is the grainhouse** (Essay 9960) in action: total control, perfect reproducibility.

---

## Dependency Resolution Strategies

### Pessimistic (apt, yum)

```
Only one version of each package
If conflict: fail, ask user to resolve
```

**Simple**, but **inflexible** (can't have two versions).

### Optimistic (npm, cargo)

```
Try to satisfy all requirements
If possible: install (might be multiple versions)
If impossible: fail
```

**More flexible**, but **duplicates packages**.

### Content-Addressed (Nix)

```
Every unique build gets unique path
Hash = content (source + deps + build)
No conflicts possible!
```

**Most flexible**, but **requires understanding** (learning curve).

---

## The Nix Advantage

**Why Nix wins for sovereignty**:

### 1. Perfect Reproducibility

```nix
# flake.nix (locks ALL dependencies)
{
  inputs.nixpkgs.url = "github:NixOS/nixpkgs/nixos-23.11";
  # Exact commit hash locked!
  
  outputs = { self, nixpkgs }: {
    # ... your config
  };
}
```

**Result**: Same flake → same build (bit-for-bit identical), even years later.

### 2. Isolation

**No global state**:
- `/usr/lib/` pollution? Nope (everything in `/nix/store/`)
- Conflicting versions? Nope (different hashes)
- "Works on my machine"? Nope (Nix makes it work everywhere)

### 3. Declarative

**Say WHAT you want** (not HOW to install):

```nix
environment.systemPackages = [
  pkgs.vim
  pkgs.git
  pkgs.clojure
];

# Nix figures out HOW (dependencies, build order, etc.)
```

### 4. Grainhouse-Compatible

**Fork everything**:
```nix
# Your own package set (grainhouse!)
import (fetchTarball "https://your-mirror.com/nixpkgs.tar.gz")

# Now: You control EVERY dependency
# Upstream goes down? You're fine (you have the seeds!)
```

**This is sovereignty** (Essay 9503, 9960).

---

## Try This

### Exercise 1: Dependency Discovery

```bash
# Python
pip show numpy
# Shows: dependencies, version, location

# Node.js
npm ls
# Shows: entire dependency tree

# Rust
cargo tree
# Shows: dependencies (with versions)
```

**Observe**: How deep does the tree go? (Often dozens of transitive dependencies!)

---

### Exercise 2: Nix Experiment (if installed)

```bash
# Enter environment with specific Python
nix-shell -p python39

# Check version
python --version
# Output: Python 3.9.x

# Exit
exit

# Enter different Python
nix-shell -p python310

python --version
# Output: Python 3.10.x

# No conflict! Both coexist in /nix/store
```

---

### Exercise 3: Version Conflict

```bash
# Try creating conflict (npm example)
mkdir conflict-test && cd conflict-test
npm init -y

# Install library A that needs old version
npm install old-lib@1.0.0

# Install library B that needs new version
npm install new-lib@2.0.0

# Check if both installed
npm ls
# (npm might install multiple versions - check node_modules/)
```

**Observe**: How does npm handle conflicts?

---

## Going Deeper

### Related Essays
- **[9594: Build Systems](/12025-10/9594-build-systems-source-to-binary)** - Build pipeline
- **[9504: What Is Clojure?](/12025-10/9504-what-is-clojure)** - Nix + Clojure
- **[9949: The Wise Elders](/12025-10/9949-intro-clojure-nix-ecosystem)** - Nix as Meticulous Architect
- **[9960: The Grainhouse](/12025-10/9960-grainhouse-risc-v-synthesis)** - Nix for sovereignty

### External Resources
- **[Nix Manual](https://nixos.org/manual/nix)** - Complete Nix reference
- **[NixOS Wiki](https://nixos.wiki/)** - Community documentation
- **npm documentation** - Node package manager
- **"Zero to Nix"** - Beginner-friendly Nix guide

---

## Reflection Questions

1. **Why does dependency hell exist?** (Shared mutable state - at the package level!)

2. **Is Nix's disk usage worth it?** (Multiple versions = disk space, but total control)

3. **Could all package managers be like Nix?** (Content-addressed, isolated - technically yes, culturally hard)

4. **What if we never upgraded dependencies?** (Pin forever - secure but miss bug fixes)

5. **How would Nock specify a package manager?** (Pure functions: package-spec → derivation → build)

---

## Summary

**Package Managers Solve**:
- **Dependency installation** (automated downloading)
- **Version resolution** (which versions work together)
- **Conflict management** (multiple packages need same dependency)

**Common Approaches**:
- **apt**: System-wide, one version, simple
- **npm**: Per-project, multiple versions, duplicates
- **cargo**: Per-project, better caching, still conflicts
- **Nix**: Content-addressed, isolated, reproducible

**Dependency Hell**:
- **Version conflicts** (A needs X v1, B needs X v2)
- **Diamond dependencies** (multiple paths to same library)
- **Breaking changes** (major version bump)

**Semantic Versioning**:
- **MAJOR.MINOR.PATCH** (2.4.7)
- Major: breaking changes
- Minor: new features (compatible)
- Patch: bug fixes only

**Nix's Radical Solution**:
- **Content-addressed**: Hash = inputs (source + deps + build)
- **Isolated**: Everything in `/nix/store/hash-name/`
- **Multiple versions**: No conflicts (different hashes)
- **Atomic**: Upgrades/rollbacks instant
- **Reproducible**: Same inputs → same output (always)
- **Declarative**: Say what, not how

**Key Insights**:
- **Most package managers = mutable state** (shared `/usr/`, conflicts)
- **Nix = immutable** (append-only store, no conflicts)
- **Reproducibility requires isolation** (hermetic builds)
- **Sovereignty requires control** (Nix + grainhouse = own everything)

**In the Valley**:
- **We use Nix** (reproducibility, sovereignty)
- **We lock dependencies** (flakes, exact hashes)
- **We fork when needed** (grainhouse strategy)
- **We never trust, always verify** (reproducible builds)

**Plant lens**: **"Package managers are seed catalogs—track varieties (versions), source (repositories), and which combinations grow well together (dependency resolution)."**

---

**Next**: We'll explore **version control** (Git)—how to track changes, collaborate with others, and preserve every version of your work for eternity!

---

**Navigation**:  
← Previous: [9595 (build systems source to binary)](/12025-10/9595-build-systems-source-to-binary) | **Phase 1 Index** | Next: [9597 (version control git foundations)](/12025-10/9597-version-control-git-foundations)

**Metadata**:
- **Phase**: 1 (Foundations)
- **Week**: 4
- **Prerequisites**: 9594, 9590, 9504
- **Concepts**: Package managers, dependency resolution, semver, apt, npm, cargo, Nix, content-addressing, reproducibility
- **Next Concepts**: Version control, Git, commits, branches, history
- **Plant Lens**: Seed catalogs (package registries), varieties (versions), compatibility (which seeds grow together)



---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*