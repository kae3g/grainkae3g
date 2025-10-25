# 🌾 Grainsource: Decentralized Version Control
## *"Every commit is a grain of truth"*

**Project:** Git-compatible version control with ICP/Urbit/Solana integration  
**Philosophy:** Offline-first, peer-to-peer, blockchain-timestamped source control  
**License:** MIT (Software) + Apache 2.0 (Protocols)  
**Created:** January 2025

---

## 🎯 VISION

**Grainsource** is a next-generation version control system that combines the best of Git with decentralized technologies (ICP, Urbit, Solana) to create a truly sovereign, peer-to-peer development workflow.

### Why Grainsource?

**Git is great, but:**
- Requires central servers (GitHub, GitLab)
- No built-in decentralized hosting
- Limited offline collaboration
- No cryptographic timestamping (beyond hashes)
- Complex UI for beginners

**Grainsource improves on Git by:**
- ✅ **ICP Canister Storage** - Decentralized repository hosting
- ✅ **Urbit Sync** - Peer-to-peer collaboration without servers
- ✅ **Solana Timestamps** - Blockchain-verified commit signatures
- ✅ **Humble UI** - Beautiful native desktop app (Clojure)
- ✅ **Git Compatible** - Works with existing Git repos/remotes
- ✅ **s6 Daemon** - Background sync with `clojure-s6`
- ✅ **Offline-First** - Full functionality without internet

---

## 🏗️ ARCHITECTURE

### Three-Layer Design

```
┌─────────────────────────────────────────────────────────┐
│              Layer 1: Humble UI Desktop App             │
│         (Clojure + Humble UI + clojure-s6)              │
│                                                          │
│  - Visual commit history (graph view)                   │
│  - Drag-and-drop file staging                           │
│  - Integrated diff viewer                               │
│  - One-click sync to ICP/Urbit/GitHub                   │
└──────────────┬──────────────────────────────────────────┘
               │
┌──────────────┴──────────────────────────────────────────┐
│           Layer 2: Grainsource Core (Clojure)           │
│          (Git-compatible engine via libgit2)            │
│                                                          │
│  - Git operations (commit, branch, merge, diff)         │
│  - ICP canister integration (upload/download)           │
│  - Urbit sync protocol (peer-to-peer)                   │
│  - Solana timestamping (commit signatures)              │
│  - s6 daemon for background sync                        │
└──────────────┬──────────────────────────────────────────┘
               │
┌──────────────┴──────────────────────────────────────────┐
│         Layer 3: Storage Backends (Decentralized)       │
│                                                          │
│  - Local filesystem (.grainsource/ directory)           │
│  - ICP Canister (decentralized cloud storage)           │
│  - Urbit ship (peer-to-peer sync)                       │
│  - GitHub/GitLab/Codeberg (Git-compatible remotes)      │
│  - Solana blockchain (commit timestamp registry)        │
└─────────────────────────────────────────────────────────┘
```

### Directory Structure

```
my-project/
├── .grainsource/              # Grainsource metadata (replaces .git)
│   ├── config                 # Repository configuration
│   ├── objects/               # Git-compatible object storage
│   ├── refs/                  # Branch references
│   ├── icp-config.edn         # ICP canister settings
│   ├── urbit-config.edn       # Urbit ship settings
│   ├── solana-registry.edn    # Solana timestamp registry
│   └── remotes.edn            # Remote sources configuration
└── .git/                      # Git compatibility layer (symlink to .grainsource)
```

---

## 💻 COMMAND-LINE INTERFACE

### Core Commands

```bash
# Repository initialization
grainsource init                  # Initialize new repository
grainsource init --icp            # Initialize with ICP canister
grainsource init --urbit ~sampel-palnet  # Initialize with Urbit sync

# Clone/Download
grainsource harvest <url>         # Clone from any source
grainsource harvest icp://canister-id  # Clone from ICP
grainsource harvest urbit://~sampel-palnet/my-repo  # Clone from Urbit
grainsource harvest github:user/repo  # Clone from GitHub

# Commit & Staging
grainsource plant "commit message"  # Commit changes (like git commit)
grainsource plant --solana        # Commit + Solana timestamp
grainsource stage <file>          # Stage files (like git add)
grainsource unstage <file>        # Unstage files

# Push/Pull
grainsource share                 # Push to all configured remotes
grainsource share --icp           # Push to ICP only
grainsource share --urbit         # Push to Urbit only
grainsource share --github        # Push to GitHub only

grainsource gather                # Pull from all remotes
grainsource gather --icp          # Pull from ICP only

grainsource sync                  # Bidirectional sync (Urbit-style)

# Branching
grainsource sprout <branch-name>  # Create branch
grainsource blend <branch>        # Merge branch
grainsource branches              # List branches
grainsource switch <branch>       # Switch branch

# History & Status
grainsource history               # Show commit log
grainsource history --graph       # Show graphical log
grainsource check                 # Repository status
grainsource diff                  # Show differences
grainsource diff <commit>         # Diff with specific commit
grainsource trace                 # Detailed history with full graph

# Remote Management
grainsource remote add <name> <url>  # Add remote
grainsource remote remove <name>     # Remove remote
grainsource sources                  # List all remote sources

# Decentralized Features
grainsource icp deploy            # Deploy repository to ICP canister
grainsource urbit sync-setup      # Configure Urbit peer sync
grainsource solana timestamp      # Timestamp all commits on Solana
grainsource verify                # Verify Solana timestamps
```

### Git Compatibility Aliases

```bash
# Grainsource provides Git aliases for familiarity
git commit → grainsource plant
git push   → grainsource share
git pull   → grainsource gather
git clone  → grainsource harvest
git branch → grainsource sprout
git merge  → grainsource blend
git log    → grainsource history
git status → grainsource check
```

---

## 🖥️ HUMBLE UI DESKTOP APP

### Features

**Main Window:**
- **Repository Browser** - Navigate local repositories
- **Commit History** - Visual graph of all commits
- **Diff Viewer** - Side-by-side or unified diff view
- **Staging Area** - Drag-and-drop file staging
- **Sync Dashboard** - One-click sync to ICP/Urbit/GitHub

**Commit Interface:**
- Rich text commit messages (Markdown supported)
- Visual file diff preview
- Automatic Solana timestamping (optional)
- Commit signing with GPG/SSH keys

**Sync Interface:**
- Real-time sync status (ICP, Urbit, GitHub)
- Bandwidth usage tracking
- Offline queue for commits
- Automatic background sync (via s6 daemon)

**Settings:**
- Configure ICP canister endpoints
- Set up Urbit ship connection
- Manage Solana wallet for timestamping
- GitHub/GitLab/Codeberg credentials

### UI Mockup (Text Description)

```
┌──────────────────────────────────────────────────────────────┐
│ Grainsource - my-project                        [ICP] [Urbit] │
├────────────────┬─────────────────────────────────────────────┤
│ Local Repos    │ Commit History (main branch)                │
│                │                                              │
│ ▸ my-project   │  ● 2025-01-22  Add Grainsource design  [✓]  │
│   - main       │  │                                           │
│   - feature-x  │  ● 2025-01-21  Update README          [✓]  │
│                │  │                                           │
│ ▸ grainwriter  │  ● 2025-01-20  Initial commit         [✓]  │
│                │                                              │
│ ▸ graincamera  │  [✓] = Solana timestamped                   │
│                │                                              │
├────────────────┼──────────────────────────────────────────────┤
│ Staged (2)     │ Diff: GRAINSOURCE-DESIGN.md                 │
│                │                                              │
│ ✓ README.md    │  + ## 🎯 VISION                             │
│ ✓ design.md    │  +                                          │
│                │  + **Grainsource** is a next-generation...  │
│ Unstaged (1)   │                                              │
│                │                                              │
│ ○ TODO.md      │                                              │
│                │                                              │
├────────────────┴──────────────────────────────────────────────┤
│ Commit: [Add Grainsource design document       ] [Plant 🌾] │
│ Sync:   [ICP ✓] [Urbit ○] [GitHub ✓]          [Share All]  │
└──────────────────────────────────────────────────────────────┘
```

---

## 🔗 ICP CANISTER INTEGRATION

### Canister Architecture

**Canister:** `grainsource-storage`

**Functionality:**
- Store Git objects (blobs, trees, commits, tags)
- Serve repository clones over HTTP
- Support incremental sync (only fetch new objects)
- Provide public/private repository options
- Handle large repositories (chunked storage)

**Rust Implementation (Candid Interface):**

```rust
// canisters/grainsource-storage/src/lib.rs

use ic_cdk::export::candid::{CandidType, Deserialize};
use ic_stable_structures::{StableBTreeMap, memory_manager::MemoryId};

#[derive(CandidType, Deserialize, Clone)]
pub struct GitObject {
    pub hash: String,        // SHA-256 hash of object
    pub object_type: String, // "blob", "tree", "commit", "tag"
    pub data: Vec<u8>,       // Raw object data
    pub size: u64,           // Size in bytes
}

#[derive(CandidType, Deserialize, Clone)]
pub struct Repository {
    pub name: String,
    pub owner: String,       // Urbit ID or principal
    pub description: String,
    pub is_public: bool,
    pub default_branch: String,
    pub objects: Vec<String>, // List of object hashes
}

#[ic_cdk::update]
fn create_repository(name: String, description: String, is_public: bool) -> Result<String, String> {
    // Create new repository
}

#[ic_cdk::update]
fn upload_object(repo_name: String, object: GitObject) -> Result<(), String> {
    // Upload Git object to canister
}

#[ic_cdk::query]
fn download_object(repo_name: String, object_hash: String) -> Result<GitObject, String> {
    // Download Git object from canister
}

#[ic_cdk::query]
fn list_repositories() -> Vec<Repository> {
    // List all public repositories
}
```

**Clojure Client:**

```clojure
(ns grainsource.icp
  "ICP canister integration for Grainsource"
  (:require [clojure.java.shell :as shell]
            [cheshire.core :as json]))

(defn upload-repository
  "Upload entire repository to ICP canister"
  [repo-path canister-id]
  (let [objects (get-all-git-objects repo-path)]
    (doseq [obj objects]
      (shell/sh "dfx" "canister" "call" canister-id
                "upload_object"
                (json/generate-string obj)))))

(defn clone-from-icp
  "Clone repository from ICP canister"
  [canister-id repo-name local-path]
  (let [repo-info (shell/sh "dfx" "canister" "call" canister-id
                            "get_repository" repo-name)]
    (download-all-objects canister-id repo-name local-path)))
```

---

## 🚢 URBIT SYNC INTEGRATION

### Urbit Agent: `%grainsource`

**Functionality:**
- Peer-to-peer repository sync between Urbit ships
- No central server required
- Subscribe to repository updates
- Automatic conflict resolution (similar to Urbit's %graph-store)

**Hoon Agent Structure (Simplified):**

```hoon
/+  default-agent, dbug
|%
+$  versioned-state
  $%  state-0
  ==
+$  state-0
  $:  %0
      repositories=(map @t repository)
  ==
+$  repository
  $:  name=@t
      owner=@p
      commits=(list commit)
  ==
+$  commit
  $:  hash=@t
      author=@t
      message=@t
      timestamp=@da
      objects=(list @t)
  ==
--
::
|_  =bowl:gall
+*  this      .
    default   ~(. (default-agent this %|) bowl)
++  on-poke
  |=  [=mark =vase]
  ^-  (quip card _this)
  ?+    mark  (on-poke:default mark vase)
      %noun
    =+  !<(command=@tas vase)
    ?+  command  [~ this]
        %sync-repo
      :: Push repository to peers
      [~ this]
    ==
  ==
++  on-watch
  |=  =path
  ^-  (quip card _this)
  :: Allow peers to subscribe to repository updates
  [~ this]
++  on-agent
  |=  [=wire =sign:agent:gall]
  ^-  (quip card _this)
  :: Handle updates from peer ships
  [~ this]
--
```

**Clojure Client:**

```clojure
(ns grainsource.urbit
  "Urbit ship integration for peer-to-peer sync"
  (:require [clojure.java.shell :as shell]
            [cheshire.core :as json]))

(defn sync-to-urbit
  "Sync repository to Urbit ship"
  [repo-path ship-url]
  (let [commits (get-new-commits repo-path)
        poke-data {:mark "grainsource-sync"
                   :json commits}]
    (http/post (str ship-url "/~/channel/poke")
               {:body (json/generate-string poke-data)})))

(defn subscribe-to-ship
  "Subscribe to repository updates from Urbit ship"
  [ship-url repo-name callback-fn]
  (http/get (str ship-url "/~/channel/subscribe")
            {:query-params {:path (str "/grainsource/" repo-name)}
             :async? true
             :callback callback-fn}))
```

---

## ⛓️ SOLANA TIMESTAMP INTEGRATION

### Commit Timestamping on Solana

**Why Solana?**
- Fast finality (~400ms)
- Low transaction costs ($0.00025 per commit)
- Immutable, verifiable timestamps
- Decentralized proof-of-authorship

**Architecture:**
1. Hash commit with SHA-256
2. Sign hash with developer's Solana wallet
3. Submit to Solana blockchain
4. Store transaction signature in commit metadata

**Rust Smart Contract (Solana Program):**

```rust
// programs/grainsource-registry/src/lib.rs

use anchor_lang::prelude::*;

declare_id!("Grain11111111111111111111111111111111111111");

#[program]
pub mod grainsource_registry {
    use super::*;

    pub fn register_commit(
        ctx: Context<RegisterCommit>,
        commit_hash: String,
        repo_name: String,
    ) -> Result<()> {
        let commit_record = &mut ctx.accounts.commit_record;
        commit_record.commit_hash = commit_hash;
        commit_record.repo_name = repo_name;
        commit_record.author = ctx.accounts.author.key();
        commit_record.timestamp = Clock::get()?.unix_timestamp;
        Ok(())
    }
}

#[derive(Accounts)]
pub struct RegisterCommit<'info> {
    #[account(init, payer = author, space = 8 + 32 + 256 + 256)]
    pub commit_record: Account<'info, CommitRecord>,
    #[account(mut)]
    pub author: Signer<'info>,
    pub system_program: Program<'info, System>,
}

#[account]
pub struct CommitRecord {
    pub commit_hash: String,
    pub repo_name: String,
    pub author: Pubkey,
    pub timestamp: i64,
}
```

**Clojure Client:**

```clojure
(ns grainsource.solana
  "Solana blockchain timestamping for commits"
  (:require [clojure.java.shell :as shell]))

(defn timestamp-commit
  "Register commit hash on Solana blockchain"
  [commit-hash repo-name wallet-path]
  (shell/sh "solana" "program" "invoke"
            "--program-id" "Grain11111111111111111111111111111111111111"
            "--keypair" wallet-path
            "--args" (str commit-hash "," repo-name)))

(defn verify-commit-timestamp
  "Verify commit timestamp on Solana"
  [commit-hash]
  (let [result (shell/sh "solana" "account" commit-hash)]
    (parse-timestamp-record (:out result))))
```

---

## 🔄 S6 DAEMON INTEGRATION

### Background Sync with `clojure-s6`

**Daemon:** `grainsource-syncd`

**Functionality:**
- Monitor repositories for changes
- Automatic background sync to ICP/Urbit/GitHub
- Configurable sync intervals (every 5 min, hourly, on-demand)
- Bandwidth-aware (throttle on slow connections)

**s6 Service Definition:**

```bash
# /etc/s6/sv/grainsource-syncd/run
#!/usr/bin/env bb

(require '[clojure-s6.core :as s6])
(require '[grainsource.sync :as sync])

(s6/supervise
  {:name "grainsource-syncd"
   :command (fn []
              (sync/start-daemon
                {:interval 300  ; 5 minutes
                 :repos ["/home/xy/kae3g/grainkae3g"]
                 :remotes [:icp :urbit :github]}))
   :restart :always
   :user "xy"})
```

**Clojure Daemon:**

```clojure
(ns grainsource.daemon
  "Background sync daemon using clojure-s6"
  (:require [clojure-s6.core :as s6]
            [grainsource.sync :as sync]))

(defn start-daemon [config]
  (s6/log "Starting grainsource-syncd...")
  (loop []
    (doseq [repo (:repos config)]
      (when (sync/has-changes? repo)
        (s6/log (str "Syncing " repo "..."))
        (sync/sync-all repo (:remotes config))))
    (Thread/sleep (* 1000 (:interval config)))
    (recur)))
```

---

## 📦 PROJECT STRUCTURE

```
grainstore/grainsource/
├── src/grainsource/
│   ├── core.clj           # Core Git operations (libgit2 wrapper)
│   ├── cli.clj            # Command-line interface
│   ├── ui.clj             # Humble UI desktop app
│   ├── sync.clj           # Multi-remote sync logic
│   ├── icp.clj            # ICP canister integration
│   ├── urbit.clj          # Urbit peer sync
│   ├── solana.clj         # Solana timestamping
│   └── daemon.clj         # s6 background daemon
├── canisters/
│   └── grainsource-storage/
│       ├── src/lib.rs     # ICP canister (Rust)
│       └── grainsource_storage.did
├── urbit/
│   └── %grainsource.hoon  # Urbit agent
├── solana/
│   └── programs/
│       └── grainsource-registry/  # Solana program (Rust)
├── deps.edn
├── README.md
└── LICENSE
```

---

## 🚀 ROADMAP

### Phase 1: Git Compatibility (Q2 2025)
- [ ] Implement core Git operations (commit, branch, merge)
- [ ] CLI tool (`grainsource` command)
- [ ] Git repository compatibility (.git interop)
- [ ] GitHub/GitLab push/pull support

### Phase 2: Humble UI App (Q3 2025)
- [ ] Desktop app with commit history visualization
- [ ] Drag-and-drop staging
- [ ] Integrated diff viewer
- [ ] s6 daemon integration

### Phase 3: ICP Integration (Q4 2025)
- [ ] ICP canister for repository hosting
- [ ] Upload/download repositories to ICP
- [ ] Public repository browsing
- [ ] CLI: `grainsource harvest icp://canister-id`

### Phase 4: Urbit Sync (Q1 2026)
- [ ] Urbit `%grainsource` agent
- [ ] Peer-to-peer repository sync
- [ ] Subscribe to ship updates
- [ ] CLI: `grainsource sync --urbit`

### Phase 5: Solana Timestamping (Q2 2026)
- [ ] Solana commit registry program
- [ ] Automatic timestamping on commit
- [ ] Verification tool
- [ ] CLI: `grainsource timestamp --solana`

### Phase 6: Full Platform (Q3 2026)
- [ ] Web interface (SvelteKit)
- [ ] Multi-platform support (Linux, macOS, Windows)
- [ ] Mobile apps (iOS, Android)
- [ ] Integration with Grainwriter, Graincamera

---

## 💡 USE CASES

### 1. **Offline Development**
- Write code on Grainwriter (offline)
- Commit locally with `grainsource plant`
- Sync to ICP/GitHub when connected

### 2. **Decentralized Collaboration**
- Clone repo from ICP canister
- Sync directly with peer's Urbit ship (no GitHub)
- Timestamp commits on Solana for provenance

### 3. **Censorship-Resistant Code Hosting**
- Upload repository to ICP (unstoppable)
- Share canister ID with collaborators
- No reliance on GitHub/GitLab

### 4. **Blockchain-Verified Authorship**
- Every commit timestamped on Solana
- Immutable proof of code authorship
- Legal evidence for intellectual property

---

## 🌟 VISION STATEMENT

**Grainsource** is more than version control—it's a statement about digital sovereignty. Just as the Grainwriter lets you write without Big Tech, Grainsource lets you version your code without relying on centralized platforms.

**Every commit is a grain of truth.**  
**Every repository is a source of sovereignty.**

Together, we're building the future of decentralized software development. 🌾

---

**Project Lead:** kae3g  
**License:** MIT (Software) + Apache 2.0 (Protocols)  
**Community:** github.com/grainpbc/grainsource  
**Status:** Design Phase

*"Plant your code. Share with sovereignty."* 🌾💻


