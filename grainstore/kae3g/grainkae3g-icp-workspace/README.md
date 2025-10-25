# ICP Workspace
## *"Development environment for ICP canisters and Grain6 services"*

**Created**: 2025-10-24  
**Status**: 🌱 **ACTIVE DEVELOPMENT**  
**Purpose**: ICP canister development, testing, and deployment

---

## 🎯 **Overview**

This workspace contains active ICP development projects:

- **GrainThrift** - ICP marketplace with live pricing
- **Oracle Daemon** - Vedic astrology data service
- **Test Canisters** - Development and testing canisters

---

## 🚀 **Quick Start**

```bash
# Start local ICP replica
dfx start

# Deploy canisters
dfx deploy

# Test canisters
dfx canister call grainthrift-clojure status
```

---

## 📁 **Structure**

```
icp-workspace/
├── src/                    # ICP canister sources
├── dfx.json               # ICP project configuration
├── grainthrift-*.html     # Demo frontends
├── oracle-daemon-plan.md  # Oracle service design
└── README.md              # This file
```

---

## 🔧 **Development**

- **Primary Compilers**: `grain6pbc-utils/clelte/` and `grain6pbc-utils/clotoko/`
- **Utilities**: `grain6pbc-utils/icp-tools/`
- **Core Library**: `clojure-icp/`

---

*"Organization enables clarity, clarity enables understanding, understanding empowers creation."* - Grain Network Philosophy
