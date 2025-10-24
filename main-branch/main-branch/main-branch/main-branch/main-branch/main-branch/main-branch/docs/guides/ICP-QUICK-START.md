# ICP Development Quick Start Guide

**Created**: January 2025  
**Context**: Custom Urbit identity system on ICP  
**Goal**: Get up and running with ICP development quickly

---

## 🚀 **Quick Setup (5 minutes)**

### **1. Run the Setup Script**
```bash
# Make the script executable
chmod +x scripts/icp-development-setup.bb

# Run the setup
bb scripts/icp-development-setup.bb
```

### **2. Start Local Development**
```bash
# Navigate to project
cd urbit-icp-identity

# Start ICP local replica
dfx start --background

# Deploy all canisters
dfx deploy
```

### **3. Verify Installation**
```bash
# Check DFX version
dfx --version

# Check Rust version
rustc --version

# Check Node.js version
node --version

# Check Solana CLI
solana --version
```

---

## 🏗️ **Project Structure**

```
urbit-icp-identity/
├── canisters/                   # ICP canisters
│   ├── urbit-identity/         # Main identity management
│   ├── urbit-address-space/    # Address allocation
│   ├── urbit-metadata/         # IPFS integration
│   ├── urbit-chain-fusion/     # Cross-chain integration
│   ├── urbit-nock/             # Nock interpreter
│   ├── urbit-hoon/             # Hoon compiler
│   └── urbit-arvo/             # Arvo kernel
├── frontend/                   # Web interface
├── clients/                    # SDK clients
│   ├── rust/                   # Rust client
│   ├── typescript/             # TypeScript client
│   └── cli/                    # Command-line interface
├── tests/                      # Integration tests
├── docs/                       # Documentation
└── dfx.json                    # DFX configuration
```

---

## 🔧 **Development Workflow**

### **1. Create a New Identity**
```rust
// In urbit-identity canister
#[update]
pub fn create_identity(
    name: String,
    description: String,
    identity_type: IdentityType,
) -> Result<u64, String> {
    // TODO: Implement identity creation
    Ok(12345)
}
```

### **2. Test the Canister**
```bash
# Call the function
dfx canister call urbit-identity create_identity '("test-identity", "Test description", variant{Planet})'

# Check the result
dfx canister call urbit-identity get_identity '(12345)'
```

### **3. Deploy Changes**
```bash
# Deploy specific canister
dfx deploy urbit-identity

# Deploy all canisters
dfx deploy
```

---

## 🌐 **Chain Fusion Integration**

### **1. Configure Solana Connection**
```rust
// In urbit-chain-fusion canister
pub struct ChainFusionManager {
    pub solana_program_id: String,
    pub ethereum_contract: String,
    pub bitcoin_address: String,
}

impl ChainFusionManager {
    pub async fn create_identity_on_solana(
        &self,
        identity_data: UrbitIdentity,
    ) -> Result<String, String> {
        // Use Chain Fusion to call Solana
        let result = call::call_raw(
            &self.solana_program_id,
            "create_identity",
            identity_data.encode(),
            None,
        ).await?;
        
        Ok(result)
    }
}
```

### **2. Test Cross-Chain Operations**
```bash
# Create identity on ICP
dfx canister call urbit-identity create_identity '("test", "Test", variant{Planet})'

# Sync with Solana
dfx canister call urbit-chain-fusion sync_with_solana '(12345)'
```

---

## 📊 **Performance Monitoring**

### **1. Check Canister Status**
```bash
# List all canisters
dfx canister status --all

# Check specific canister
dfx canister status urbit-identity
```

### **2. Monitor Performance**
```bash
# Check canister metrics
dfx canister call urbit-identity get_metrics

# Check address space usage
dfx canister call urbit-address-space get_usage_stats
```

---

## 🔐 **Security Best Practices**

### **1. Access Control**
```rust
// Always check caller identity
#[update]
pub fn create_identity(name: String) -> Result<u64, String> {
    let caller = ic_cdk::caller();
    
    // Check if caller has permission
    if !has_permission(&caller, "create_identity") {
        return Err("Unauthorized".to_string());
    }
    
    // Create identity
    Ok(create_identity_impl(name))
}
```

### **2. Input Validation**
```rust
// Validate all inputs
#[update]
pub fn update_metadata(identity_id: u64, metadata: String) -> Result<(), String> {
    // Validate identity_id
    if identity_id == 0 {
        return Err("Invalid identity ID".to_string());
    }
    
    // Validate metadata length
    if metadata.len() > 1000 {
        return Err("Metadata too long".to_string());
    }
    
    // Update metadata
    Ok(())
}
```

---

## 🚀 **Deployment to Mainnet**

### **1. Prepare for Mainnet**
```bash
# Create mainnet identity
dfx identity new mainnet-identity

# Fund the identity
dfx identity get-principal mainnet-identity
# Send ICP to this address

# Use mainnet identity
dfx identity use mainnet-identity
```

### **2. Deploy to Mainnet**
```bash
# Deploy to mainnet
dfx deploy --network ic

# Verify deployment
dfx canister --network ic status --all
```

---

## 🐛 **Troubleshooting**

### **Common Issues:**

1. **DFX not found**
   ```bash
   # Reinstall DFX
   sh -ci "$(curl -fsSL https://internetcomputer.org/install.sh)"
   ```

2. **Canister deployment fails**
   ```bash
   # Check canister status
   dfx canister status --all
   
   # Redeploy
   dfx deploy --upgrade-unchanged
   ```

3. **Chain Fusion not working**
   ```bash
   # Check Solana connection
   solana cluster-version
   
   # Verify program ID
   dfx canister call urbit-chain-fusion get_solana_program_id
   ```

### **Debug Commands:**
```bash
# Check logs
dfx canister call urbit-identity get_logs

# Reset local replica
dfx stop
dfx start --clean --background

# Check canister code
dfx canister call urbit-identity get_code
```

---

## 📚 **Next Steps**

1. **Implement Core Identity Logic**
   - Create identity management functions
   - Add address space allocation
   - Implement metadata storage

2. **Add Chain Fusion Integration**
   - Connect to Solana network
   - Implement cross-chain operations
   - Add Ethereum bridge

3. **Build Frontend Interface**
   - Create web interface
   - Add identity management UI
   - Implement cross-chain operations

4. **Deploy to Mainnet**
   - Test on mainnet
   - Launch public beta
   - Gather community feedback

---

## 🎊 **Ready to Build!**

You now have a complete ICP development environment set up for building your custom Urbit identity system. The combination of ICP's cost efficiency and Chain Fusion's cross-chain capabilities gives you the perfect foundation for a next-generation identity system.

**Happy coding!** 🚀

---

## 📖 **Additional Resources**

- [ICP Documentation](https://internetcomputer.org/docs/)
- [Chain Fusion Guide](https://internetcomputer.org/docs/building-apps/chain-fusion/)
- [Rust Canister Development](https://internetcomputer.org/docs/current/developer-docs/backend/rust/)
- [DFX CLI Reference](https://internetcomputer.org/docs/current/references/cli-reference/)

