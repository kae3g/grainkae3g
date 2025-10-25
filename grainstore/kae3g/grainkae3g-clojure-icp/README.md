# clojure-icp (clojure-dfinity)

**Clojure library for Internet Computer Protocol (ICP) / DFINITY integration**

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Clojure](https://img.shields.io/badge/Clojure-1.11+-blue.svg)](https://clojure.org)

---

## Overview

`clojure-icp` (also available as `clojure-dfinity`) is a comprehensive Clojure library for interacting with the Internet Computer Protocol (ICP). It provides:

- **Agent Interface** - Low-level IC agent for canister calls
- **Candid Support** - Encode/decode Candid types
- **Identity Management** - Principal and identity handling
- **Canister Client** - High-level canister interaction
- **Chain Fusion** - Multi-chain integration (Solana, Ethereum)
- **Subnet Management** - Subnet queries and management

---

## Installation

### Clojure CLI (deps.edn)

```clojure
{:deps {io.github.grainpbc/clojure-icp {:git/tag "v0.1.0" :git/sha "abc123..."}}}
```

### Leiningen (project.clj)

```clojure
[io.github.grainpbc/clojure-icp "0.1.0"]
```

---

## Quick Start

```clojure
(require '[clojure-icp.core :as icp]
         '[clojure-icp.agent :as agent]
         '[clojure-icp.candid :as candid])

;; Create an IC agent
(def my-agent (agent/create-agent {:host "https://ic0.app"}))

;; Call a canister method
(def result
  (agent/call my-agent
              {:canister-id "rrkah-fqaaa-aaaaa-aaaaq-cai"
               :method "greet"
               :args (candid/encode {:name "World"})}))

;; Query a canister (read-only)
(def query-result
  (agent/query my-agent
               {:canister-id "rrkah-fqaaa-aaaaa-aaaaq-cai"
                :method "get_count"
                :args (candid/encode {})}))
```

---

## Features

### 1. IC Agent Interface

**Low-level IC agent for direct canister communication:**

```clojure
(require '[clojure-icp.agent :as agent])

;; Create agent
(def agent (agent/create-agent {:host "https://ic0.app"}))

;; Call canister (update)
(agent/call agent
            {:canister-id "rrkah-fqaaa-aaaaa-aaaaq-cai"
             :method "increment"
             :args []})

;; Query canister (read-only)
(agent/query agent
             {:canister-id "rrkah-fqaaa-aaaaa-aaaaq-cai"
              :method "get_value"
              :args []})
```

### 2. Candid Type System

**Encode and decode Candid types:**

```clojure
(require '[clojure-icp.candid :as candid])

;; Encode Clojure data to Candid
(candid/encode {:name "Alice" :age 30})
;; => Candid binary representation

;; Decode Candid to Clojure data
(candid/decode candid-bytes)
;; => {:name "Alice" :age 30}

;; Type definitions
(candid/define-type :Person
  {:name :text
   :age :nat
   :email :opt-text})
```

### 3. Identity Management

**Manage principals and identities:**

```clojure
(require '[clojure-icp.identity :as identity])

;; Generate new identity
(def my-identity (identity/generate))

;; Get principal
(identity/principal my-identity)
;; => "aaaaa-aa"

;; Sign with identity
(identity/sign my-identity message)
```

### 4. Canister Client

**High-level canister interaction:**

```clojure
(require '[clojure-icp.canister :as canister])

;; Create canister client
(def my-canister
  (canister/create {:canister-id "rrkah-fqaaa-aaaaa-aaaaq-cai"
                    :agent agent}))

;; Call methods
(canister/call my-canister :increment {})
(canister/query my-canister :get_value {})

;; Get canister info
(canister/info my-canister)
;; => {:controllers [...] :module_hash "..." :memory_size ...}
```

### 5. Chain Fusion

**Multi-chain integration:**

```clojure
(require '[clojure-icp.chain-fusion :as chain-fusion])

;; Solana integration
(chain-fusion/solana-call {:program-id "..."
                           :instruction "..."
                           :accounts [...]})

;; Ethereum integration
(chain-fusion/ethereum-call {:contract-address "0x..."
                             :method "transfer"
                             :args [...]})
```

### 6. Subnet Management

**Query and manage subnets:**

```clojure
(require '[clojure-icp.subnet :as subnet])

;; List subnets
(subnet/list-subnets agent)

;; Get subnet info
(subnet/info agent subnet-id)

;; Query subnet status
(subnet/status agent subnet-id)
```

---

## Architecture

```
clojure-icp/
├── src/clojure_icp/
│   ├── core.clj           # Main API
│   ├── agent.clj          # IC agent implementation
│   ├── candid.clj         # Candid type system
│   ├── identity.clj       # Identity management
│   ├── canister.clj       # Canister client
│   ├── chain_fusion.clj   # Multi-chain integration
│   ├── subnet.clj         # Subnet management
│   ├── http.clj           # HTTP agent
│   ├── certificate.clj    # Certificate verification
│   └── utils.clj          # Utility functions
├── test/
├── resources/
├── deps.edn
├── README.md
└── LICENSE
```

---

## Development

### Run REPL

```bash
clojure -M:repl
```

### Run Tests

```bash
clojure -M:test
```

### Build

```bash
clojure -T:build jar
```

---

## Grain Network Integration

`clojure-icp` is a core library in the **Grain Network** ecosystem:

- **Grainspace** - Uses clojure-icp for identity management
- **Graincamera** - Stores photos in ICP canisters
- **Grainwriter** - Syncs documents to ICP storage
- **Grainpack** - Manages GPU compute on ICP subnets

---

## Examples

### Store Data in Canister

```clojure
(require '[clojure-icp.core :as icp])

(def agent (icp/create-agent))

(icp/call agent
          {:canister-id "my-canister-id"
           :method "store_data"
           :args {:key "photo-123"
                  :value "base64-encoded-photo-data"}})
```

### Query Data from Canister

```clojure
(icp/query agent
           {:canister-id "my-canister-id"
            :method "get_data"
            :args {:key "photo-123"}})
```

### Create Identity and Deploy Canister

```clojure
(require '[clojure-icp.identity :as identity]
         '[clojure-icp.canister :as canister])

;; Generate identity
(def my-identity (identity/generate))

;; Create agent with identity
(def agent (icp/create-agent {:identity my-identity}))

;; Deploy canister
(def canister-id
  (canister/deploy agent
                   {:wasm-module (slurp "my-canister.wasm")
                    :init-args {}}))
```

---

## Relationship to DFINITY

This library is also known as `clojure-dfinity` and is designed to work with the **DFINITY Foundation**'s Internet Computer Protocol (ICP). The two names are interchangeable:

- **clojure-icp** - Official name for the Grain Network
- **clojure-dfinity** - Symlinked name for DFINITY ecosystem compatibility

Both point to the same codebase and are maintained by **Grain PBC**.

---

## Contributing

We welcome contributions! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for details.

### Development Workflow

1. Fork the repository
2. Create a feature branch
3. Write tests for new features
4. Ensure all tests pass
5. Submit a pull request

---

## License

MIT License - see [LICENSE](LICENSE) for details.

---

## Links

- **Grain Network**: https://grain.network
- **Grain PBC**: https://github.com/grainpbc
- **ICP Documentation**: https://internetcomputer.org/docs
- **DFINITY Foundation**: https://dfinity.org

---

## Credits

Developed by **kae3g** for the **Grain Network** ecosystem.

Part of the **Grain PBC** suite of open-source tools for decentralized computing.

---

**clojure-icp (clojure-dfinity)**  
*"Bringing ICP to the Clojure ecosystem"* 🌾


