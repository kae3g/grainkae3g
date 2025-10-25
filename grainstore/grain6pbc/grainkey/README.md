# 🌾 Grainkey - Universal SSH & GnuPG Key Management

Universal SSH and GnuPG key management for Grain Network with template-based, personalized, secure key generation and deployment.

## Features

- **SSH ed25519 key generation and deployment** - Modern cryptographic security
- **GnuPG ed25519 signing key generation and export** - For GitHub/Codeberg commit signing
- **Template-based key management** - Consistent key generation across services
- **Personal configuration separation** - Customization without modifying core code
- **GitHub and Codeberg integration** - Ready-to-use templates for popular platforms

## Quick Start

```bash
# List available templates
bb scripts/grainkey.bb list

# Generate SSH key for NixOS VM
bb scripts/grainkey.bb generate nixos-vm

# Deploy SSH key to VM
bb scripts/grainkey.bb deploy nixos-vm

# Generate GnuPG key for GitHub
bb scripts/grainkey.bb generate github-gpg

# Export GnuPG public key for GitHub
bb scripts/grainkey.bb deploy github-gpg
```

## Available Templates

### SSH Keys
- `nixos-vm` - SSH key for NixOS virtual machine
- `github` - SSH key for GitHub
- `codeberg` - SSH key for Codeberg

### GnuPG Keys
- `github-gpg` - GnuPG signing key for GitHub commits
- `codeberg-gpg` - GnuPG signing key for Codeberg commits

## Configuration

### Default Configuration
Grainkey uses sensible defaults for all key types:
- Ed25519 algorithm for both SSH and GnuPG
- 256-bit key length
- Separate SSH and GnuPG directories
- Template-based approach for consistency

### Personal Configuration
Override default settings by creating `personal/config.edn`:

```clojure
{
 :key-templates {
   :custom-vm {:comment "custom-vm-key"
               :filename "custom-vm"
               :hosts ["192.168.1.100"]
               :type :ssh}
 }
}
```

## Security

### Never Commit
- Private keys (`*.key`, `*.pem`, `nixos-grainkae3g`)
- Personal configuration with sensitive data
- API keys or tokens
- Passwords or credentials

### Safe to Commit
- Public keys (`*.pub`)
- Template definitions
- Core utility code
- Personal config template (without sensitive data)

## Grain Network Philosophy

- **Security by Design** - Ed25519 keys, template-based management
- **Reproducible Key Generation** - Consistent results across environments
- **Template-based Automation** - Reduce human error in key management
- **Clear Separation** - Public vs private data handling
- **Vegan-friendly Development** - Ethical, sustainable practices

## Usage Examples

### NixOS VM Setup
```bash
# Generate SSH key for VM
bb scripts/grainkey.bb generate nixos-vm

# Deploy key to VM (requires VM password once)
bb scripts/grainkey.bb deploy nixos-vm

# Test connection
ssh -i ~/.ssh/nixos-grainkae3g nixos@192.168.122.204
```

### GitHub Setup
```bash
# Generate SSH key for GitHub
bb scripts/grainkey.bb generate github

# Generate GnuPG key for commit signing
bb scripts/grainkey.bb generate github-gpg

# Export GnuPG public key
bb scripts/grainkey.bb deploy github-gpg

# Add SSH key to GitHub (copy ~/.ssh/github-kae3g.pub)
# Add GnuPG key to GitHub (copy ~/.gnupg/github-gpg-kae3g.pub)
```

## Commands

- `generate <template>` - Generate key pair from template
- `deploy <template>` - Deploy keys to configured hosts
- `list` - List available templates and generated keys
- `help` - Show usage information

## File Structure

```
grainkey/
├── src/grainkey/
│   └── core.clj              # Core functionality
├── scripts/
│   └── grainkey.bb           # Main script
├── personal/
│   └── config.edn.template   # Personal config template
└── README.md                 # This file
```

## Dependencies

- Babashka (Clojure scripting)
- ssh-keygen (SSH key generation)
- gpg (GnuPG key generation)
- ssh-copy-id (SSH key deployment)

## License

Part of the Grain Network ecosystem. See main repository for license information.

---

🌾 **now == next + 1** 🌾
