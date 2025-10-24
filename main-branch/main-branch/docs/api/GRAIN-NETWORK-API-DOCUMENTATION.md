# 🌾 Grain Network API Documentation
## *"Complete developer reference for the Grain ecosystem"*

**Version:** 1.0.0  
**Published:** January 22, 2025  
**Publisher:** Grain PBC  
**Format:** Magazine Press Release Style

---

## 📰 Welcome to the Grain Network API

**Dear Developer,**

Welcome to the most comprehensive open-source personal sovereignty computing platform ever built. This API documentation covers every library, tool, and service in the Grain Network ecosystem.

Unlike typical dry technical documentation, we've designed this as a **magazine-style guide** - informative, beautiful, and actually enjoyable to read.

**What you'll find here:**
- 🔧 Complete API references for all Grain libraries
- 📚 Code examples and tutorials
- 🎯 Best practices and patterns
- 🌐 Integration guides
- 🤖 AI-assisted development tips
- 🌾 Philosophy and design rationale

**Let's build the future of computing together.**

*— kae3g, Founder & Graingalaxy, Grain PBC*

---

## 📖 Table of Contents

### Part I: Foundation Libraries
1. [clojure-s6](#clojure-s6) - Process supervision
2. [clojure-sixos](#clojure-sixos) - SixOS system management
3. [clojure-icp](#clojure-icp) - ICP/DFINITY integration

### Part II: Application Libraries
4. [clojure-photos](#clojure-photos) - Photo management
5. [grainweb](#grainweb) - Browser + Git + AI
6. [grainspace](#grainspace) - Unified platform
7. [grainsource](#grainsource) - Version control

### Part III: Tools & Utilities
8. [grainversion](#grainversion) - Template system
9. [grainstore-sync](#grainstore-sync) - Submodule management

### Part IV: Protocols & Standards
10. [Grain Network Protocol](#grain-network-protocol)
11. [Grainmarking System](#grainmarking-system)

---

# Part I: Foundation Libraries

---

## clojure-s6

**Tagline:** *"Supervise your processes like a boss"*

### Overview

`clojure-s6` is a Clojure wrapper around the s6 supervision suite, providing elegant process management with functional programming patterns.

**Why s6?**
- ~200KB footprint
- Unix philosophy
- musl-native
- Battle-tested reliability

### Installation

```clojure
;; deps.edn
{:deps {io.github.grainpbc/clojure-s6 
        {:git/url "https://github.com/grainpbc/clojure-s6"
         :git/sha "latest"}}}
```

### Quick Start

```clojure
(require '[clojure-s6.core :as s6])

;; Start a supervised service
(s6/supervise
  {:name "my-app"
   :command (fn [] (my-app-main))
   :restart :always
   :user "xy"})

;; Stop a service
(s6/stop-service "my-app")

;; Check service status
(s6/service-status "my-app")
;; => {:status :running :uptime 3600 :restarts 0}
```

### API Reference

#### `s6/supervise`

Start a supervised service.

**Parameters:**
- `:name` (string, required) - Service name
- `:command` (fn, required) - Function to run
- `:restart` (keyword, optional) - Restart policy (`:always`, `:on-failure`, `:never`)
- `:user` (string, optional) - User to run as
- `:environment` (map, optional) - Environment variables
- `:dependencies` (vector, optional) - Service dependencies

**Returns:** Service handle

**Example:**

```clojure
(s6/supervise
  {:name "grainweb"
   :command #(grainweb.core/-main)
   :restart :always
   :user "xy"
   :environment {"GRAINWEB_PORT" "8080"}
   :dependencies ["postgresql" "redis"]})
```

#### `s6/stop-service`

Stop a running service.

**Parameters:**
- `service-name` (string, required) - Name of service to stop

**Returns:** Boolean success

**Example:**

```clojure
(s6/stop-service "grainweb")
;; => true
```

#### `s6/service-status`

Get status of a service.

**Parameters:**
- `service-name` (string, required) - Name of service

**Returns:** Status map

**Example:**

```clojure
(s6/service-status "grainweb")
;; => {:status :running 
;;     :uptime 7200 
;;     :restarts 0
;;     :pid 1234
;;     :memory-mb 150}
```

#### `s6/log`

Write to service log.

**Parameters:**
- `message` (string, required) - Log message
- `level` (keyword, optional) - Log level (`:info`, `:warn`, `:error`)

**Example:**

```clojure
(s6/log "Service started" :level :info)
(s6/log "Connection failed" :level :error)
```

### Best Practices

**1. Always use restart policies:**

```clojure
;; Good
{:restart :always}

;; Better
{:restart :on-failure
 :restart-delay 5000}  ; 5 second backoff
```

**2. Handle cleanup:**

```clojure
(s6/supervise
  {:name "my-app"
   :command #(my-app-main)
   :on-stop #(cleanup-resources)})
```

**3. Use logging:**

```clojure
(defn my-service []
  (s6/log "Starting service")
  (try
    (do-work)
    (catch Exception e
      (s6/log (str "Error: " e) :level :error))))
```

---

## clojure-icp (clojure-dfinity)

**Tagline:** *"Decentralize everything, own your data"*

### Overview

`clojure-icp` brings the Internet Computer Protocol to Clojure. Build decentralized applications that run on ICP canisters with the elegance of functional programming.

**Also known as:** `clojure-dfinity` (symlinked for DFINITY ecosystem compatibility)

### Installation

```clojure
;; deps.edn
{:deps {io.github.grainpbc/clojure-icp 
        {:git/url "https://github.com/grainpbc/clojure-icp"
         :git/sha "latest"}}}
```

### Quick Start

```clojure
(require '[clojure-icp.core :as icp])

;; Create IC agent
(def agent (icp/create-agent))

;; Call canister method
(icp/call agent
          :canister-id "rrkah-fqaaa-aaaaa-aaaaq-cai"
          :method "greet"
          :args {:name "World"})
;; => {:greeting "Hello, World!"}

;; Query canister (read-only)
(icp/query agent
           :canister-id "rrkah-fqaaa-aaaaa-aaaaq-cai"
           :method "get_count"
           :args {})
;; => {:count 42}
```

### API Reference

#### `icp/create-agent`

Create an IC agent for interacting with canisters.

**Parameters:**
- `:host` (string, optional) - IC host URL (default: "https://ic0.app")
- `:identity` (identity, optional) - Identity for signing
- `:timeout` (int, optional) - Request timeout in ms (default: 30000)
- `:local?` (boolean, optional) - Use local replica (default: false)

**Returns:** Agent instance

**Example:**

```clojure
;; Production IC
(def agent (icp/create-agent))

;; Local development
(def local-agent (icp/create-agent :local? true))

;; With authentication
(def authed-agent 
  (icp/create-agent :identity my-identity))
```

#### `icp/call`

Call a canister method (update call).

**Parameters:**
- `agent` (agent, required) - IC agent
- `:canister-id` (string, required) - Canister ID
- `:method` (string, required) - Method name
- `:args` (map, required) - Method arguments (will be encoded to Candid)

**Returns:** Decoded response

**Example:**

```clojure
(icp/call agent
          :canister-id "rrkah-fqaaa-aaaaa-aaaaq-cai"
          :method "transfer"
          :args {:to "aaaaa-aa"
                 :amount 1000000})
;; => {:tx_id "0x123abc..."
;;     :status :success}
```

#### `icp/query`

Query a canister method (read-only, faster).

**Parameters:**
- `agent` (agent, required) - IC agent
- `:canister-id` (string, required) - Canister ID
- `:method` (string, required) - Method name
- `:args` (map, required) - Method arguments

**Returns:** Decoded response

**Example:**

```clojure
(icp/query agent
           :canister-id "rrkah-fqaaa-aaaaa-aaaaq-cai"
           :method "get_balance"
           :args {:account "aaaaa-aa"})
;; => {:balance 5000000}
```

#### `icp/deploy-canister`

Deploy a new canister.

**Parameters:**
- `agent` (agent, required) - IC agent
- `:wasm-module` (bytes, required) - WASM module
- `:init-args` (map, optional) - Initialization arguments
- `:cycles` (int, optional) - Initial cycles

**Returns:** Canister ID

**Example:**

```clojure
(icp/deploy-canister agent
                     :wasm-module (slurp "my-canister.wasm")
                     :init-args {:admin "aaaaa-aa"}
                     :cycles 1000000000000)
;; => "rrkah-fqaaa-aaaaa-aaaaq-cai"
```

### Identity Management

#### `icp/generate-identity`

Generate a new identity.

**Returns:** Identity instance

**Example:**

```clojure
(def my-identity (icp/generate-identity))

(icp/get-principal :identity my-identity)
;; => "aaaaa-aa"
```

#### `icp/identity-from-pem`

Load identity from PEM file.

**Parameters:**
- `:pem-file` (string, required) - Path to PEM file

**Returns:** Identity instance

**Example:**

```clojure
(def my-identity 
  (icp/identity-from-pem 
    :pem-file "~/.config/dfx/identity/default/identity.pem"))
```

### Best Practices

**1. Use query for reads:**

```clojure
;; Good - fast, no consensus
(icp/query agent :canister-id "..." :method "get_data" :args {})

;; Avoid - slow, unnecessary consensus
(icp/call agent :canister-id "..." :method "get_data" :args {})
```

**2. Handle errors:**

```clojure
(try
  (icp/call agent ...)
  (catch clojure.lang.ExceptionInfo e
    (let [data (ex-data e)]
      (case (:type data)
        :canister-error (log/error "Canister error:" (:message data))
        :network-error (log/error "Network error, retrying...")
        (throw e)))))
```

**3. Use local replica for development:**

```clojure
(def agent 
  (if (= (System/getenv "ENV") "production")
    (icp/create-agent)
    (icp/create-agent :local? true)))
```

---

## clojure-photos

**Tagline:** *"Your photos, your servers, your control"*

### Overview

`clojure-photos` provides decentralized photo management with support for AVIF, HEIC, PNG, and JPEG. Integrate with Nostr, ICP, and Urbit for true photo sovereignty.

### Installation

```clojure
{:deps {io.github.grainpbc/clojure-photos 
        {:git/url "https://github.com/grainpbc/clojure-photos"
         :git/sha "latest"}}}
```

### Quick Start

```clojure
(require '[clojure-photos.core :as photos]
         '[clojure-photos.nostr :as nostr]
         '[clojure-photos.icp :as icp])

;; Load photo
(def photo (photos/load-photo "photo.avif"))

;; Upload to ICP
(icp/upload-photo :photo photo
                  :canister-id "my-photo-canister")

;; Publish to Nostr
(nostr/publish-photo :photo photo
                     :relay "wss://relay.grain.network"
                     :description "Beautiful sunset")
```

### API Reference

#### `photos/load-photo`

Load a photo from file.

**Parameters:**
- `file-path` (string, required) - Path to photo file

**Returns:** Photo map

**Example:**

```clojure
(photos/load-photo "photo.avif")
;; => {:image BufferedImage
;;     :format :avif
;;     :metadata {...}
;;     :path "photo.avif"}
```

#### `photos/save-photo`

Save a photo to file.

**Parameters:**
- `photo` (photo, required) - Photo map
- `file-path` (string, required) - Output path
- `:quality` (int, optional) - Compression quality 0-100 (default: 90)
- `:metadata` (map, optional) - Metadata to embed

**Example:**

```clojure
(photos/save-photo photo "output.avif"
                   :quality 95
                   :metadata {:description "Sunset"
                              :tags ["nature" "landscape"]})
```

#### Nostr Integration

**`nostr/publish-photo`**

Publish photo to Nostr network.

**Parameters:**
- `:photo` (photo, required) - Photo data
- `:relay` (string, required) - Nostr relay URL
- `:private-key` (string, required) - Private key for signing
- `:storage-url` (string, required) - URL where photo is stored
- `:description` (string, optional) - Photo description
- `:tags` (vector, optional) - Photo tags

**Returns:** Event ID

**Example:**

```clojure
(nostr/publish-photo :photo photo
                     :relay "wss://relay.grain.network"
                     :private-key my-nostr-key
                     :storage-url "ipfs://Qm..."
                     :description "Beautiful sunset"
                     :tags ["nature" "sunset"])
;; => "note1abc123..."
```

---

# Part II: Application Libraries

---

## grainweb

**Tagline:** *"Browser × Git × AI - All in one"*

### Overview

Grainweb is the unified interface for the Grain Network. It combines web browsing, Git exploration, and AI-powered code assistance in a single Clojure + Humble UI application.

**Features:**
- 🌐 Multi-protocol browser (HTTP, IPFS, ICP, Urbit)
- 📂 Visual Git interface
- 🤖 Self-hosted AI models (Qwen3, Gemini, Llama, GPT-OS)
- 🔒 Complete privacy (local AI, no telemetry)

### Installation

```bash
# Via Homebrew
brew install grainweb

# Via Nix
nix profile install github:grainpbc/grainweb

# Via APT
sudo apt install grainweb
```

### API Reference

#### `grainweb.browser/navigate`

Navigate to a URL.

**Parameters:**
- `browser` (browser, required) - Browser instance
- `url` (string, required) - URL to navigate to

**Returns:** Updated browser

**Example:**

```clojure
(require '[grainweb.browser :as browser])

(browser/navigate my-browser "https://grain.network")
(browser/navigate my-browser "ipfs://Qm...")
(browser/navigate my-browser "ic://rrkah-fqaaa-aaaaa-aaaaq-cai")
```

#### `grainweb.git/explore-repo`

Open repository in Git explorer.

**Parameters:**
- `repo-path` (string, required) - Path to repository

**Returns:** Repository view

**Example:**

```clojure
(require '[grainweb.git :as git])

(git/explore-repo "/home/xy/grainkae3g")
;; => {:path "..."
;;     :branches [...]
;;     :commits [...]
;;     :files [...]}
```

#### `grainweb.ai/explain-code`

Get AI explanation of code.

**Parameters:**
- `code` (string, required) - Code to explain
- `:model` (keyword, optional) - AI model to use (default: :qwen3)

**Returns:** Explanation string

**Example:**

```clojure
(require '[grainweb.ai :as ai])

(ai/explain-code 
  "(defn factorial [n]
     (if (zero? n) 1 (* n (factorial (dec n)))))"
  :model :gemini)
;; => "This is a recursive implementation of the factorial function..."
```

---

## grainsource

**Tagline:** *"Git, but better"*

### Overview

Grainsource is our Git-compatible version control system with ICP backup, Urbit sync, and Solana commit signatures.

**Commands:**

```bash
grainsource init          # Initialize repository
grainsource harvest       # Clone
grainsource plant         # Commit
grainsource share         # Push
grainsource gather        # Pull
grainsource sync          # Bidirectional sync
```

### Clojure API

```clojure
(require '[grainsource.core :as gs])

;; Initialize repository
(gs/init "/path/to/repo")

;; Commit changes
(gs/plant {:files ["src/core.clj"]
           :message "Add new feature"
           :author "kae3g"})

;; Push to remote
(gs/share {:remote "origin"
           :branch "main"})
```

---

# Part III: Tools & Utilities

---

## grainversion

**Tagline:** *"Fork, but cooler"*

### Overview

The grainversion system enables personal versions of Grain templates while maintaining upstream sync.

### CLI Usage

```bash
# Create grainversion
grainversion create grainnixos-qemu-ubuntu-framework16 kae3g

# Sync with template
grainversion sync

# List your grainversions
grainversion list
```

### Babashka API

```clojure
(require '[grainversion.core :as gv])

;; Create grainversion
(gv/create "template-name" "your-username")

;; Sync with upstream
(gv/sync)

;; List grainversions
(gv/list-all)
```

---

## grainstore-sync

**Tagline:** *"Manage submodules the Grain way"*

### Overview

Sync grainstore submodules from EDN manifest.

### Usage

```bash
# Sync all submodules
bb scripts/grainstore-sync.bb sync

# List submodules
bb scripts/grainstore-sync.bb list

# Check status
bb scripts/grainstore-sync.bb status
```

### Configuration

**`grainstore-manifest.edn`:**

```clojure
{:grainstore-manifest
 {:submodules
  [{:name "clojure-icp"
    :upstream "https://github.com/grainpbc/clojure-icp.git"
    :branch "main"}
   
   {:name "clojure-dfinity"
    :type :symlink
    :target "clojure-icp"}]}}
```

---

# Part IV: Protocols & Standards

---

## Grain Network Protocol

### Identity System

**Grainmarking:** Prevention of non-admin `grain*` username creation

**Grainrighting:** Governance and correction principles

**Grainlicensing:** License management system

### Data Formats

**All Grain libraries use:**
- EDN for configuration
- Candid for ICP communication
- JSON for web APIs
- Markdown for documentation

---

## 🌟 Magazine Extras

### Developer Interviews

> **"Building with Grain has been transformative. The AI integration in Grainweb is unlike anything I've used before."**  
> — Student Developer, Urbana High School

### Code Snippets of the Month

**Most Elegant:**

```clojure
(-> photo
    (photos/load "sunset.avif")
    (photos/resize {:width 1920})
    (photos/save "sunset-resized.avif"))
```

### Community Highlights

- 🌾 12 active grainversions of NixOS QEMU template
- 🎯 87.5% musl-native packages in ecosystem
- 🚀 Complete privacy with local AI models

---

## 📬 Get Involved

**Report Issues:** https://github.com/grainpbc/grainweb/issues  
**Discussions:** https://github.com/orgs/grainpbc/discussions  
**Discord:** https://grain.network/discord  
**Email:** dev@grain.network

---

## 📄 License

All Grain Network software is MIT licensed.  
All Grain hardware designs are CERN-OHL-S-2.0 licensed.

---

**Grain Network API Documentation**  
**Version 1.0.0 • Published January 22, 2025**  
**© 2025 Grain Public Benefit Corporation**

*"Build with sovereignty. Code with freedom. Deploy with confidence."* 🌾


