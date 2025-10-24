# 🌐 clojure-icp (clojure-dfinity) Setup Guide
## *"Bringing ICP to the Clojure ecosystem"*

**Created:** January 22, 2025  
**Author:** kae3g (Graingalaxy)  
**Organizations:** Grain PBC

---

## 📋 Overview

`clojure-icp` (also known as `clojure-dfinity`) is a comprehensive Clojure library for interacting with the Internet Computer Protocol (ICP). It provides both low-level IC agent functionality and high-level abstractions for common canister operations.

### Why Two Names?

- **clojure-icp** - Primary name for the Grain Network ecosystem
- **clojure-dfinity** - Symlinked alias for compatibility with DFINITY ecosystem

Both names point to the **same codebase** via symlink, maintained by **Grain PBC**.

---

## 🎯 Features

### Core Functionality

1. **IC Agent Interface** - Low-level canister communication
2. **Candid Type System** - Encode/decode Candid types
3. **Identity Management** - Principal and keypair handling
4. **Canister Client** - High-level canister interaction
5. **Chain Fusion** - Multi-chain integration (Solana, Ethereum)
6. **Subnet Management** - Subnet queries and management

### Integration with Grain Network

- **Grainspace** - Identity management via ICP
- **Graincamera** - Photo storage in ICP canisters
- **Grainwriter** - Document sync to ICP storage
- **Grainpack** - GPU compute on ICP subnets
- **Grain Network** - Unified decentralized platform

---

## 📦 Repository Structure

```
grainstore/
├── clojure-icp/          # Main repository
│   ├── src/
│   │   └── clojure_icp/
│   │       ├── core.clj
│   │       ├── agent.clj
│   │       ├── candid.clj
│   │       ├── identity.clj
│   │       ├── canister.clj
│   │       ├── chain_fusion.clj
│   │       └── subnet.clj
│   ├── test/
│   ├── deps.edn
│   ├── README.md
│   └── LICENSE (MIT)
│
└── clojure-dfinity -> clojure-icp/  # Symlink
```

---

## 🚀 Installation

### 1. Clone Repository

```bash
# Clone as clojure-icp
git clone https://github.com/grainpbc/clojure-icp.git

# OR clone as clojure-dfinity
git clone https://github.com/grainpbc/clojure-dfinity.git
```

### 2. Add as Dependency

**Clojure CLI (deps.edn):**
```clojure
{:deps {io.github.grainpbc/clojure-icp 
        {:git/url "https://github.com/grainpbc/clojure-icp"
         :git/sha "abc123..."}}}
```

**Leiningen (project.clj):**
```clojure
[io.github.grainpbc/clojure-icp "0.1.0"]
```

### 3. Verify Installation

```clojure
(require '[clojure-icp.core :as icp])

(icp/version)
;; => {:name "clojure-icp" :alias "clojure-dfinity" :version "0.1.0"}
```

---

## 🔧 Quick Start

### Create IC Agent

```clojure
(require '[clojure-icp.core :as icp])

;; Production IC
(def agent (icp/create-agent))

;; Local replica (for development)
(def local-agent (icp/create-agent :local? true))
```

### Call Canister Method

```clojure
;; Update call (changes state)
(icp/call agent
          :canister-id "rrkah-fqaaa-aaaaa-aaaaq-cai"
          :method "increment"
          :args {})

;; Query call (read-only)
(icp/query agent
           :canister-id "rrkah-fqaaa-aaaaa-aaaaq-cai"
           :method "get_value"
           :args {})
```

### Create Canister Client

```clojure
(def my-canister
  (icp/create-canister
    :canister-id "rrkah-fqaaa-aaaaa-aaaaq-cai"
    :agent agent))

;; Now use canister-specific methods
(icp/call my-canister :method "greet" :args {:name "World"})
```

---

## 🔐 Identity Management

### Generate New Identity

```clojure
(require '[clojure-icp.identity :as identity])

(def my-identity (identity/generate))

;; Get principal
(identity/principal my-identity)
;; => "aaaaa-aa"
```

### Load Identity from PEM

```clojure
(def my-identity
  (identity/from-pem :pem-file "~/.config/dfx/identity/default/identity.pem"))
```

### Use Identity with Agent

```clojure
(def authenticated-agent
  (icp/create-agent :identity my-identity))
```

---

## 🌐 Grain Network Integration

### Grainspace Identity

```clojure
(require '[clojure-icp.core :as icp]
         '[grainspace.identity :as grain-id])

;; Create Grainspace identity backed by ICP
(def grain-identity
  (grain-id/create :backend :icp
                   :icp-identity my-identity))
```

### Photo Storage (Graincamera)

```clojure
(require '[clojure-photos.icp :as photo-icp])

;; Upload photo to ICP canister
(photo-icp/upload-photo
  :photo "photo.avif"
  :canister-id "grain-photos-canister-id")
```

### Document Sync (Grainwriter)

```clojure
(require '[grain-writer.sync :as sync])

;; Sync document to ICP
(sync/to-icp
  :document "my-document.md"
  :canister-id "grain-docs-canister-id")
```

---

## 🔗 GitHub Organizations

### grainpbc Organization

Both `clojure-icp` and `clojure-dfinity` repositories are hosted under the **grainpbc** GitHub organization:

```
https://github.com/grainpbc/clojure-icp
https://github.com/grainpbc/clojure-dfinity
```

### Repository Setup

1. **Create grainpbc organization** (if not exists)
2. **Create clojure-icp repository**
3. **Create clojure-dfinity repository** (mirror of clojure-icp)
4. **Set up symlink in grainstore**

---

## 📚 Documentation

### API Documentation

- **Core API**: `clojure-icp.core`
- **Agent**: `clojure-icp.agent`
- **Candid**: `clojure-icp.candid`
- **Identity**: `clojure-icp.identity`
- **Canister**: `clojure-icp.canister`
- **Chain Fusion**: `clojure-icp.chain-fusion`
- **Subnet**: `clojure-icp.subnet`

### Examples

See `examples/` directory in repository for:
- Basic canister calls
- Identity management
- Canister deployment
- Chain fusion integration
- Grainspace integration

---

## 🧪 Testing

### Run Tests

```bash
# All tests
clojure -M:test

# Specific namespace
clojure -M:test -n clojure-icp.core-test
```

### Local Replica Testing

```bash
# Start local IC replica
dfx start --background

# Run tests against local replica
clojure -M:test -e :local
```

---

## 🤝 Contributing

### Development Setup

1. Fork the repository
2. Clone your fork
3. Create a feature branch
4. Make changes
5. Run tests
6. Submit pull request

### Code Style

- Follow Clojure style guide
- Use `clj-kondo` for linting
- Write docstrings for all public functions
- Add tests for new features

---

## 📄 License

**MIT License** - see [LICENSE](../grainstore/clojure-icp/LICENSE) for details.

---

## 🔗 Links

- **Grain Network**: https://grain.network
- **Grain PBC**: https://github.com/grainpbc
- **clojure-icp**: https://github.com/grainpbc/clojure-icp
- **clojure-dfinity**: https://github.com/grainpbc/clojure-dfinity
- **ICP Docs**: https://internetcomputer.org/docs
- **DFINITY**: https://dfinity.org

---

## ✅ Next Steps

1. **Push to GitHub**:
   ```bash
   cd grainstore/clojure-icp
   git remote add origin https://github.com/grainpbc/clojure-icp.git
   git push -u origin master
   ```

2. **Create Mirror Repository**:
   ```bash
   # Create clojure-dfinity as mirror on GitHub
   gh repo create grainpbc/clojure-dfinity --public --source=grainpbc/clojure-icp
   ```

3. **Update Dependencies**:
   - Update `clojure-photos` to use `clojure-icp`
   - Update `grainspace` to use `clojure-icp`
   - Update other Grain libraries

4. **Documentation**:
   - Add API examples
   - Create video tutorials
   - Write blog posts

---

**clojure-icp (clojure-dfinity)**  
*"Bringing ICP to the Clojure ecosystem"* 🌾

*Part of the Grain Network - Personal Sovereignty Computing* 🌐


