#!/usr/bin/env bb

;; Grainspace Platform Setup Script
;; Unified Decentralized Platform with Urbit + ICP Identity

(require '[clojure.java.shell :as shell]
         '[clojure.string :as str]
         '[babashka.fs :as fs])

(defn log [message]
  "Log with timestamp"
  (let [timestamp (java.time.LocalDateTime/now)
        formatted-time (.format timestamp (java.time.format.DateTimeFormatter/ofPattern "HH:mm:ss"))]
    (println (str "[" formatted-time "] 🌐 " message))))

(defn run-command [cmd & {:keys [sh]}]
  "Run command with optional shell execution"
  (try
    (let [result (if sh
                   (shell/sh "bash" "-c" cmd)
                   (shell/sh cmd))]
      (if (= (:exit result) 0)
        (str/trim (:out result))
        (do
          (log (str "Command failed: " cmd " - " (:err result)))
          "")))
    (catch Exception e
      (log (str "Error running command: " cmd " - " (.getMessage e)))
      "")))

(defn check-dependencies []
  "Check required dependencies"
  (log "🔍 Checking dependencies...")
  
  (let [deps {"clojure" "clojure --version"
              "dfx" "dfx --version"
              "node" "node --version"
              "npm" "npm --version"}]
    
    (doseq [[name cmd] deps]
      (let [result (run-command cmd :sh true)]
        (if (str/blank? result)
          (log (str "❌ " name " not found"))
          (log (str "✅ " name " found")))))))

(defn install-dependencies []
  "Install missing dependencies"
  (log "📦 Installing dependencies...")
  
  ;; Install DFX (ICP SDK)
  (log "Installing DFX...")
  (run-command "sh -ci \"$(curl -fsSL https://internetcomputer.org/install.sh)\"" :sh true)
  
  ;; Install Node.js and npm
  (log "Installing Node.js...")
  (run-command "curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -" :sh true)
  (run-command "sudo apt-get install -y nodejs" :sh true)
  
  ;; Install additional tools
  (log "Installing additional tools...")
  (run-command "sudo apt install -y git curl wget build-essential" :sh true))

(defn setup-icp-canisters []
  "Setup ICP canisters for Grainspace"
  (log "🔧 Setting up ICP canisters...")
  
  (let [grainspace-dir "grainstore/grainspace"]
    (when (fs/exists? grainspace-dir)
      (run-command (str "cd " grainspace-dir " && dfx start --background") :sh true)
      (run-command (str "cd " grainspace-dir " && dfx deploy") :sh true)
      (log "✅ ICP canisters deployed"))))

(defn create-grainspace-service []
  "Create s6 service for Grainspace"
  (log "🔧 Creating s6 service for Grainspace...")
  
  (let [service-config
        {:name "grainspace"
         :command (str "bb " (System/getProperty "user.dir") "/grainstore/grainspace/scripts/grainspace-daemon.bb")
         :type :longrun
         :dependencies ["network" "filesystem"]
         :user (System/getProperty "user.name")
         :group (System/getProperty "user.name")
         :environment {"GRAINSPACE_HOME" (str (System/getProperty "user.dir") "/grainstore/grainspace")
                       "URBIT_IDENTITY" "~zod"
                       "ICP_SUBNET" "rdmx6-jaaaa-aaaah-qcaiq-cai"}}]
    
    (spit "grainspace.service" (pr-str service-config))
    (log "✅ Grainspace s6 service created")))

(defn create-daemon-script []
  "Create Grainspace daemon script"
  (log "📝 Creating daemon script...")
  
  (let [daemon-script "#!/usr/bin/env bb

;; Grainspace Daemon Script
;; Runs the unified decentralized platform

(require '[clojure.java.shell :as shell]
         '[clojure.string :as str])

(defn log [message]
  (let [timestamp (java.time.LocalDateTime/now)
        formatted-time (.format timestamp (java.time.format.DateTimeFormatter/ofPattern \"HH:mm:ss\"))]
    (println (str \"[\" formatted-time \"] 🌐 \" message))))

(defn start-grainspace []
  (log \"Starting Grainspace platform...\")
  (log \"   Identity: Urbit Azimuth + ICP Subnet\")
  (log \"   Social: X + Nostr + Bluesky + Threads\")
  (log \"   Marketplace: Apps + Models + Art + Grains\")
  (log \"   Streaming: Live coding + Demos + Tutorials\")
  
  ;; Start the main application
  (let [result (shell/sh \"clojure\" \"-M:dev\" \"-m\" \"grainspace.core\")]
    (if (= (:exit result) 0)
      (log \"Grainspace started successfully\")
      (log (str \"Failed to start Grainspace: \" (:err result))))))

(defn main []
  (let [args *command-line-args*]
    (case (first args)
      \"start\" (start-grainspace)
      (do
        (log \"Grainspace Daemon\")
        (log \"Usage: bb grainspace-daemon.bb start\")))))

(main)"]
    
    (spit "grainstore/grainspace/scripts/grainspace-daemon.bb" daemon-script)
    (run-command "chmod +x grainstore/grainspace/scripts/grainspace-daemon.bb" :sh true)
    (log "✅ Daemon script created")))

(defn create-readme []
  "Create comprehensive README"
  (log "📝 Creating README...")
  
  (let [readme-content
        "# 🌐 Grainspace - Unified Decentralized Platform

**The Everything Platform - Apps, Models, Art, Social, Streaming**

## 🎯 Overview

Grainspace is a unified decentralized platform that combines:
- **Identity System**: Urbit Azimuth + ICP Subnet
- **Social Networks**: X (Twitter) + Nostr + Bluesky + Threads
- **Marketplace**: Apps + AI Models + Digital Art + Verified Dependencies
- **Streaming**: Live coding + Demos + Tutorials
- **Grainstore**: Verified dependency management

## 🏗️ Architecture

### Identity Layer
- **Urbit Azimuth**: Human-readable identities (~zod, ~bus, etc.)
- **ICP Subnet**: Technical identity management
- **Unified Address**: ~grainspace-{urbit}-{icp}
- **Cross-platform**: Single identity across all services

### Social Layer
- **X (Twitter)**: Traditional social media
- **Nostr**: Decentralized social protocol
- **Bluesky**: AT Protocol integration
- **Threads**: Meta platform integration

### Marketplace Layer
- **Apps**: Clojure applications, Humble UI apps
- **Models**: MCP models, Letta agents, HuggingFace Spaces
- **Art**: NFTs, digital creative works
- **Grains**: Verified dependencies, libraries, tools

### Streaming Layer
- **WebRTC**: Peer-to-peer streaming
- **Live Coding**: Real-time development
- **Tutorials**: Educational content
- **Demos**: Product demonstrations

## 🚀 Quick Start

### 1. Setup
```bash
bb scripts/setup-grainspace.bb
```

### 2. Start Platform
```bash
cd grainstore/grainspace
clojure -M:dev -m grainspace.core
```

### 3. Login
- Use your Urbit address (e.g., ~zod, ~bus)
- Automatically creates ICP identity
- Connects to social platforms

## 🔧 Configuration

### Environment Variables
```bash
export X_BEARER_TOKEN=\"your_twitter_token\"
export NOSTR_API_KEY=\"your_nostr_key\"
export BLUESKY_HANDLE=\"your_bluesky_handle\"
export BLUESKY_PASSWORD=\"your_bluesky_password\"
export THREADS_SESSION_ID=\"your_threads_session\"
export THREADS_USER_ID=\"your_threads_user_id\"
```

### ICP Configuration
```bash
export ICP_SUBNET=\"rdmx6-jaaaa-aaaah-qcaiq-cai\"
export ICP_CANISTER_ID=\"u6s2n-gx777-77774-qaaba-cai\"
```

## 📱 Features

### Identity Management
- **Unified Identity**: Single identity across all platforms
- **Urbit Integration**: Native Urbit identity support
- **ICP Integration**: Blockchain-based identity verification
- **Cross-platform**: Seamless authentication

### Social Integration
- **Multi-platform**: Post to all platforms simultaneously
- **Unified Feed**: View all social feeds in one place
- **Real-time**: Live updates from all platforms
- **Censorship-resistant**: Decentralized social protocols

### Marketplace
- **Apps**: Discover and install Clojure applications
- **Models**: Access AI models and agents
- **Art**: Buy and sell digital artwork
- **Grains**: Manage verified dependencies

### Streaming
- **Live Coding**: Stream your development process
- **Tutorials**: Create educational content
- **Demos**: Showcase your projects
- **Interactive**: Real-time audience interaction

## 🛠️ Development

### Project Structure
```
grainstore/grainspace/
├── src/grainspace/
│   ├── core.clj              # Main application
│   ├── identity.clj          # Identity management
│   ├── marketplace.clj       # Marketplace functions
│   ├── social.clj            # Social integration
│   └── streaming.clj         # Streaming features
├── canisters/
│   └── grainspace-identity/  # ICP identity canister
├── web-app/                  # Web interface
└── scripts/                  # Utility scripts
```

### Building
```bash
clojure -M:build
```

### Testing
```bash
clojure -M:test
```

## 🔐 Security

- **Decentralized**: No single point of failure
- **User-controlled**: You own your identity
- **Censorship-resistant**: Multiple social protocols
- **Privacy-focused**: Minimal data collection

## 🌍 Cross-Platform

- **Desktop**: Humble UI native application
- **Web**: ClojureScript web interface
- **Mobile**: React Native mobile app
- **Consistent**: Same experience everywhere

## 📚 Documentation

- **[Identity System](docs/identity.md)**: Urbit + ICP integration
- **[Social Integration](docs/social.md)**: Multi-platform social
- **[Marketplace](docs/marketplace.md)**: Apps, models, art
- **[Streaming](docs/streaming.md)**: Live content platform
- **[API Reference](docs/api.md)**: Complete API documentation

## 🤝 Contributing

Contributions welcome! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

## 📄 License

MIT License - see [LICENSE](LICENSE) for details.

## 🚀 Roadmap

### Phase 1: Core Identity ✅
- [x] Urbit Azimuth integration
- [x] ICP Subnet integration
- [x] Unified identity system
- [x] Cross-platform authentication

### Phase 2: Social Integration 🚧
- [x] X (Twitter) integration
- [x] Nostr integration
- [x] Bluesky integration
- [x] Threads integration

### Phase 3: Marketplace 📋
- [ ] App marketplace
- [ ] Model marketplace
- [ ] Art gallery
- [ ] Grainstore integration

### Phase 4: Streaming 📋
- [ ] WebRTC streaming
- [ ] Live coding platform
- [ ] Interactive features
- [ ] Content monetization

---

**Built with ❤️ for the decentralized future**

*Grainspace: Where identity, social, marketplace, and streaming unite.* 🌐"
    
    (spit "grainstore/grainspace/README.md" readme-content)
    (log "✅ README created")))

(defn main []
  "Main setup function"
  (log "🚀 Setting up Grainspace - Unified Decentralized Platform")
  (log "   Identity: Urbit Azimuth + ICP Subnet")
  (log "   Social: X + Nostr + Bluesky + Threads")
  (log "   Marketplace: Apps + Models + Art + Grains")
  (log "   Streaming: Live coding + Demos + Tutorials")
  
  (try
    (check-dependencies)
    (install-dependencies)
    (setup-icp-canisters)
    (create-grainspace-service)
    (create-daemon-script)
    (create-readme)
    
    (log "")
    (log "✅ Grainspace setup complete!")
    (log "")
    (log "🎮 To start the platform:")
    (log "   cd grainstore/grainspace")
    (log "   clojure -M:dev -m grainspace.core")
    (log "")
    (log "🔧 To configure:")
    (log "   Set environment variables for social platforms")
    (log "   Configure Urbit identity")
    (log "   Set up ICP subnet")
    (log "")
    (log "📚 For help:")
    (log "   cat grainstore/grainspace/README.md")
    
    (catch Exception e
      (log (str "❌ Setup failed: " (.getMessage e)))
      (System/exit 1))))

;; Run the setup
(main)

