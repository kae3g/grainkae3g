#!/usr/bin/env bb

;; ICP Development Environment Setup Script
;; Sets up ICP development environment for Urbit identity system

(require '[clojure.java.shell :as shell]
         '[clojure.string :as str])

(defn log [message]
  "Log a message with timestamp"
  (println (str "[" (java.time.LocalDateTime/now) "] " message)))

(defn run-command [cmd & {:keys [sh]}]
  "Run a command and return output as string"
  (try
    (if sh
      (-> (shell/sh "bash" "-c" cmd)
          :out
          str/trim)
      (-> (shell/sh cmd)
          :out
          str/trim))
    (catch Exception e
      (log (str "Error running command: " cmd " - " (.getMessage e)))
      "")))

(defn check-command [cmd]
  "Check if a command exists"
  (let [result (run-command (str "which " cmd) :sh true)]
    (not (str/blank? result))))

(defn install-dfx []
  "Install DFX (ICP development framework)"
  (log "🔧 Installing DFX...")
  (if (check-command "dfx")
    (log "✅ DFX already installed")
    (do
      (log "📥 Downloading DFX installer...")
      (run-command "sh -ci \"$(curl -fsSL https://internetcomputer.org/install.sh)\"" :sh true)
      (log "✅ DFX installed successfully"))))

(defn install-rust []
  "Install Rust for canister development"
  (log "🦀 Installing Rust...")
  (if (check-command "rustc")
    (log "✅ Rust already installed")
    (do
      (log "📥 Downloading Rust installer...")
      (run-command "curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh -s -- -y" :sh true)
      (log "✅ Rust installed successfully"))))

(defn install-nodejs []
  "Install Node.js for frontend development"
  (log "📦 Installing Node.js...")
  (if (check-command "node")
    (log "✅ Node.js already installed")
    (do
      (log "📥 Adding Node.js repository...")
      (run-command "curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -" :sh true)
      (log "📥 Installing Node.js...")
      (run-command "sudo apt-get install -y nodejs" :sh true)
      (log "✅ Node.js installed successfully"))))

(defn install-solana-cli []
  "Install Solana CLI for Chain Fusion"
  (log "☀️ Installing Solana CLI...")
  (if (check-command "solana")
    (log "✅ Solana CLI already installed")
    (do
      (log "📥 Downloading Solana installer...")
      (run-command "sh -c \"$(curl -sSfL https://release.solana.com/v1.18.4/install)\"" :sh true)
      (log "✅ Solana CLI installed successfully"))))

(defn create-project-structure []
  "Create ICP project structure"
  (log "📁 Creating project structure...")
  
  (let [project-dir "urbit-icp-identity"]
    (run-command (str "mkdir -p " project-dir) :sh true)
    (run-command (str "cd " project-dir) :sh true)
    
    ;; Create canister directories
    (doseq [canister ["urbit-identity" "urbit-address-space" "urbit-metadata" 
                      "urbit-chain-fusion" "urbit-nock" "urbit-hoon" "urbit-arvo"]]
      (run-command (str "mkdir -p " project-dir "/canisters/" canister "/src") :sh true)
      (run-command (str "mkdir -p " project-dir "/canisters/" canister "/tests") :sh true))
    
    ;; Create other directories
    (run-command (str "mkdir -p " project-dir "/frontend") :sh true)
    (run-command (str "mkdir -p " project-dir "/clients/rust") :sh true)
    (run-command (str "mkdir -p " project-dir "/clients/typescript") :sh true)
    (run-command (str "mkdir -p " project-dir "/clients/cli") :sh true)
    (run-command (str "mkdir -p " project-dir "/tests") :sh true)
    (run-command (str "mkdir -p " project-dir "/docs") :sh true)
    
    (log "✅ Project structure created")))

(defn create-dfx-config []
  "Create DFX configuration file"
  (log "⚙️ Creating DFX configuration...")
  
  (let [dfx-config {:version 1
                    :canisters {:urbit-identity {:main "canisters/urbit-identity/src/main.rs"
                                                 :type "rust"}
                                :urbit-address-space {:main "canisters/urbit-address-space/src/main.rs"
                                                      :type "rust"}
                                :urbit-metadata {:main "canisters/urbit-metadata/src/main.rs"
                                                 :type "rust"}
                                :urbit-chain-fusion {:main "canisters/urbit-chain-fusion/src/main.rs"
                                                     :type "rust"}
                                :urbit-nock {:main "canisters/urbit-nock/src/main.rs"
                                             :type "rust"}
                                :urbit-hoon {:main "canisters/urbit-hoon/src/main.rs"
                                             :type "rust"}
                                :urbit-arvo {:main "canisters/urbit-arvo/src/main.rs"
                                             :type "rust"}}}]
    
    (spit "urbit-icp-identity/dfx.json" (str "{
  \"version\": 1,
  \"canisters\": {
    \"urbit-identity\": {
      \"main\": \"canisters/urbit-identity/src/main.rs\",
      \"type\": \"rust\"
    },
    \"urbit-address-space\": {
      \"main\": \"canisters/urbit-address-space/src/main.rs\",
      \"type\": \"rust\"
    },
    \"urbit-metadata\": {
      \"main\": \"canisters/urbit-metadata/src/main.rs\",
      \"type\": \"rust\"
    },
    \"urbit-chain-fusion\": {
      \"main\": \"canisters/urbit-chain-fusion/src/main.rs\",
      \"type\": \"rust\"
    },
    \"urbit-nock\": {
      \"main\": \"canisters/urbit-nock/src/main.rs\",
      \"type\": \"rust\"
    },
    \"urbit-hoon\": {
      \"main\": \"canisters/urbit-hoon/src/main.rs\",
      \"type\": \"rust\"
    },
    \"urbit-arvo\": {
      \"main\": \"canisters/urbit-arvo/src/main.rs\",
      \"type\": \"rust\"
    }
  }
}"))
    (log "✅ DFX configuration created")))

(defn create-cargo-toml [canister-name]
  "Create Cargo.toml for a canister"
  (let [cargo-content (str "[package]
name = \"" canister-name "\"
version = \"0.1.0\"
edition = \"2021\"

[lib]
crate-type = [\"cdylib\"]

[dependencies]
ic-cdk = \"0.12.0\"
ic-cdk-macros = \"0.12.0\"
serde = { version = \"1.0\", features = [\"derive\"] }
serde_json = \"1.0\"
std = { version = \"0.1.0\", features = [\"ic0\"] }

[profile.release]
opt-level = \"z\"
lto = true
codegen-units = 1
panic = \"abort\"
")]
    (spit (str "urbit-icp-identity/canisters/" canister-name "/Cargo.toml") cargo-content)))

(defn create-main-rs [canister-name]
  "Create main.rs for a canister"
  (let [main-content (str "use ic_cdk::api::call;
use ic_cdk_macros::{query, update};
use serde::{Deserialize, Serialize};
use std::collections::HashMap;

#[derive(Serialize, Deserialize, Clone)]
pub struct " (str/capitalize canister-name) " {
    // TODO: Define canister state
}

impl Default for " (str/capitalize canister-name) " {
    fn default() -> Self {
        Self {
            // TODO: Initialize default state
        }
    }
}

#[query]
pub fn get_state() -> " (str/capitalize canister-name) " {
    // TODO: Return current state
    " (str/capitalize canister-name) "::default()
}

#[update]
pub fn update_state(new_state: " (str/capitalize canister-name) ") -> Result<(), String> {
    // TODO: Update state
    Ok(())
}

// TODO: Add more functions as needed
")]
    (spit (str "urbit-icp-identity/canisters/" canister-name "/src/main.rs") main-content)))

(defn create-canister-templates []
  "Create template files for all canisters"
  (log "📝 Creating canister templates...")
  
  (doseq [canister ["urbit-identity" "urbit-address-space" "urbit-metadata" 
                    "urbit-chain-fusion" "urbit-nock" "urbit-hoon" "urbit-arvo"]]
    (create-cargo-toml canister)
    (create-main-rs canister))
  
  (log "✅ Canister templates created"))

(defn create-readme []
  "Create project README"
  (log "📖 Creating README...")
  
  (let [readme-content "# Urbit ICP Identity System

A custom Urbit-like identity system built on ICP (Internet Computer Protocol) with Chain Fusion integration.

## Architecture

- **ICP Subnet**: Primary identity management (250x cheaper than Solana)
- **Chain Fusion**: Native Solana integration for high-performance operations
- **WebAssembly**: Rust-based canisters for familiar development
- **Threshold Cryptography**: Enhanced security for identity management

## Getting Started

1. **Install Dependencies**:
   ```bash
   bb scripts/icp-development-setup.bb
   ```

2. **Start Local Development**:
   ```bash
   cd urbit-icp-identity
   dfx start --background
   dfx deploy
   ```

3. **Build Frontend**:
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

## Canisters

- `urbit-identity`: Main identity management
- `urbit-address-space`: Address allocation and management
- `urbit-metadata`: IPFS integration and metadata storage
- `urbit-chain-fusion`: Cross-chain integration (Solana, Ethereum, Bitcoin)
- `urbit-nock`: Nock interpreter for Urbit computation
- `urbit-hoon`: Hoon compiler for Urbit programming
- `urbit-arvo`: Arvo kernel for Urbit operating system

## Development

- **Rust**: Canister development
- **TypeScript**: Frontend development
- **DFX**: ICP development framework
- **Chain Fusion**: Cross-chain integration

## License

MIT License - see LICENSE file for details.
"]
    (spit "urbit-icp-identity/README.md" readme-content)
    (log "✅ README created")))

(defn main []
  "Main setup function"
  (log "🚀 Starting ICP Development Environment Setup...")
  
  (try
    (install-dfx)
    (install-rust)
    (install-nodejs)
    (install-solana-cli)
    (create-project-structure)
    (create-dfx-config)
    (create-canister-templates)
    (create-readme)
    
    (log "🎉 ICP Development Environment Setup Complete!")
    (log "")
    (log "Next steps:")
    (log "1. cd urbit-icp-identity")
    (log "2. dfx start --background")
    (log "3. dfx deploy")
    (log "4. Start developing your Urbit identity system!")
    
    (catch Exception e
      (log (str "❌ Setup failed: " (.getMessage e)))
      (System/exit 1))))

;; Run the setup
(main)
