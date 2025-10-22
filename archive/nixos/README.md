# NixOS VM Configuration

This directory contains NixOS configuration for the Robotic Farm VirtualBox VM.

## Files

- **`vm-configuration.nix`** — Complete NixOS system configuration
  - Optimized for Robotic Farm development
  - Includes Babashka, Clojure, Node.js 22
  - Docker support with automatic pruning
  - VirtualBox Guest Additions
  - SSH server with key-based authentication

## Usage

### Initial Setup

1. Create VM using the automation script:
   ```bash
   ./scripts/setup-virtualbox-vm.sh
   ```

2. Start VM and install NixOS:
   ```bash
   VBoxManage startvm robotic-farm-nixos --type gui
   ```

3. Inside the VM, partition and mount:
   ```bash
   # Follow instructions in writings/9996-nixos-virtualbox-macos.md
   ```

4. Copy configuration to VM:
   ```bash
   # On macOS host
   scp -P 2222 nixos/vm-configuration.nix nixos@localhost:/tmp/
   
   # On VM
   sudo mv /tmp/vm-configuration.nix /etc/nixos/configuration.nix
   sudo nixos-rebuild switch
   ```

### Updating Configuration

1. Edit `nixos/vm-configuration.nix` on your macOS host

2. Copy to VM:
   ```bash
   scp -P 2222 nixos/vm-configuration.nix nixos@localhost:/etc/nixos/
   ```

3. Apply changes:
   ```bash
   ssh -p 2222 nixos@localhost
   sudo nixos-rebuild switch
   ```

### Configuration Highlights

```nix
{
  # Flakes enabled
  nix.settings.experimental-features = [ "nix-command" "flakes" ];
  
  # Development packages
  environment.systemPackages = with pkgs; [
    babashka clojure clj-kondo clojure-lsp
    nodejs_22
    docker-compose
    # ... and many more
  ];
  
  # Docker enabled
  virtualisation.docker.enable = true;
  
  # VirtualBox integration
  virtualisation.virtualbox.guest.enable = true;
}
```

## Philosophy

This configuration embodies the project's principles:

**Declarative** — Everything in code, version controlled

**Reproducible** — Same configuration → identical system

**Minimal** — Only what's needed, nothing more

**Integrated** — Matches project dependencies exactly

From the Analects:

> "The Master said: 'The superior man is satisfied and composed; the mean man is always full of distress.'" — 7.37

This NixOS configuration brings satisfaction through completeness, composition through declarative structure.

## Customization

### Add Your SSH Key

Edit `vm-configuration.nix`:

```nix
users.users.nixos = {
  # ...
  openssh.authorizedKeys.keys = [
    "ssh-ed25519 AAAAC3... your-key-here"
  ];
};
```

### Change Timezone

```nix
time.timeZone = "Europe/London";  # Or your timezone
```

### Add More Packages

```nix
environment.systemPackages = with pkgs; [
  # ... existing packages ...
  python3
  rustc
  # ... your additions ...
];
```

### Enable Auto-Upgrades

```nix
system.autoUpgrade = {
  enable = true;  # Changed from false
  allowReboot = false;
};
```

## Troubleshooting

### Configuration Syntax Errors

```bash
# Test configuration without applying
sudo nixos-rebuild test
```

### Rollback to Previous Configuration

```bash
# List generations
sudo nix-env --list-generations --profile /nix/var/nix/profiles/system

# Rollback
sudo nixos-rebuild switch --rollback
```

### View Configuration Diff

```bash
# Compare current and new configuration
sudo nixos-rebuild dry-build
```

## Integration with Project Flake

The VM configuration can be referenced in your project's `flake.nix`:

```nix
{
  outputs = { self, nixpkgs }: {
    nixosConfigurations.robotic-farm-vm = nixpkgs.lib.nixosSystem {
      system = "x86_64-linux";
      modules = [ ./nixos/vm-configuration.nix ];
    };
  };
}
```

Then build with:

```bash
nixos-rebuild switch --flake .#robotic-farm-vm
```

## See Also

- [9996-nixos-virtualbox-macos.md](../writings/9996-nixos-virtualbox-macos.md) — Complete guide
- [VirtualBox setup script](../scripts/setup-virtualbox-vm.sh) — Automation
- [NixOS Manual](https://nixos.org/manual/nixos/stable/) — Official documentation

---

*"As the configuration, so the system.*  
*As the declaration, so the reality.*  
*Reproducibility through purity,*  
*Stability through immutability."*

— Nix Wisdom

L'dor v'dor — From generation to generation

