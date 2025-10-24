# kae3g 9996: NixOS Development Environments — VMs, Containers, and Build Management
**Timestamp:** 12025-10-05--06thhouse01984  
**Series:** Technical Writings (9999 → 0000)  
**Category:** Development Environment, Infrastructure, Build Systems, Philosophy  
**Reading Time:** 110 minutes

## The Question

*How do we create a reproducible, declarative NixOS development environment within VirtualBox on macOS Sequoia, with our project dependencies automatically configured and fully documented?*

## Introduction: The Garden Within a Garden

```clojure
{:the-parable
 "A gardener in the mountains (macOS) builds a greenhouse (VirtualBox)
  to grow alpine flowers (NixOS) that require specific soil (Nix packages).
  
  The greenhouse is portable, the soil is reproducible,
  the flowers bloom identically whether in spring or winter.
  
  This is virtualization as cultivation,
  isolation as preservation,
  declarative configuration as seed catalog."}
```

In the Ch'an tradition, there's a concept called *"garden within garden"* (園中園)—creating a microcosm that reflects and perfects the macrocosm. VirtualBox on macOS with NixOS inside represents this: a controlled, reproducible environment nested within the host system.

## Why VirtualBox + NixOS on macOS?

```clojure
{:the-case-for-virtualization
 {:host-preservation
  "Keep macOS clean—no system-level package pollution"
  
  :reproducibility
  "VM configuration as code—delete, recreate identically"
  
  :isolation
  "Development environment completely separated from host"
  
  :portability
  "Export VM, share with team, guaranteed identical setup"
  
  :experimentation
  "Try configurations without fear—snapshots protect you"
  
  :linux-specificity
  "Some tools (Docker, systemd) work better on Linux"}
 
 :the-case-for-nixos
 {:declarative-everything
  "Entire VM configuration in version control"
  
  :reproducible-packages
  "flake.lock guarantees identical package versions"
  
  :atomic-upgrades
  "Upgrade VM packages atomically, rollback if issues"
  
  :minimal-footprint
  "NixOS minimal ISO starts tiny, add only what you need"
  
  :consistency-with-project
  "Your project already uses Nix—extend to VM layer"}}
```

**From the I Ching:**

> Hexagram 52: Keeping Still (艮 Gèn) — The Mountain  
> *"Keeping his back still so that he no longer feels his body.  
> He goes into his courtyard and does not see his people.  
> No blame."*

The VM is like the mountain—still, stable, unchanging. Inside, development flows freely, but the mountain itself remains unmoved.

## Prerequisites

### On macOS Host

```bash
# Install VirtualBox
brew install --cask virtualbox

# Verify installation
VBoxManage --version  # Should show: 7.0.x or higher

# Optional: Install VirtualBox Extension Pack (for USB, RDP)
# Download from: https://www.virtualbox.org/wiki/Downloads
# Install via VirtualBox GUI: Preferences → Extensions
```

```clojure
{:macos-requirements
 {:os-version "macOS 12 (Monterey) or later"
  :recommended "macOS 15 (Sequoia/Tahoe)"
  :disk-space "At least 40 GB free"
  :ram "At least 8 GB total (will allocate 4 GB to VM)"
  :cpu "Intel or Apple Silicon with Rosetta 2"
  :note "On Apple Silicon, performance will be slower (x86 emulation)"}}
```

### Project Repository

Your Robotic Farm project should be cloned locally:

```bash
cd ~/kae3g
git clone https://codeberg.org/kae3g/12025-10-04.git
cd 12025-10-04
```

## Automated Setup (Recommended)

We've created automation scripts in `scripts/` that handle the entire process:

```bash
# 1. Create and configure the VM
./scripts/setup-virtualbox-vm.sh

# 2. Start the VM
VBoxManage startvm robotic-farm-nixos --type gui

# 3. Inside the VM (after it boots), install NixOS
# (Follow on-screen instructions or use the install script)
```

### What the Automation Does

```clojure
{:automated-steps
 ["1. Check VirtualBox installation"
  "2. Download NixOS minimal ISO (if not cached)"
  "3. Create VM with optimal settings:
      - 4 GB RAM (configurable)
      - 2 CPUs (configurable)
      - 30 GB disk (configurable)
      - NAT networking with port forwarding"
  "4. Attach NixOS ISO to VM"
  "5. Configure shared folder (host project → VM)"
  "6. Set up port forwarding:
      - SSH: localhost:2222 → VM:22
      - HTTP: localhost:8080 → VM:8080
      - HTTPS: localhost:8443 → VM:443"
  "7. Print next steps and connection info"]}
```

## Manual Setup (Deep Understanding)

If you prefer to understand every step:

### Step 1: Download NixOS Minimal ISO

```bash
# Create ISOs directory
mkdir -p ~/VirtualBox/ISOs
cd ~/VirtualBox/ISOs

# Download NixOS 24.05 minimal ISO
curl -L -o nixos-minimal-24.05-x86_64-linux.iso \
  https://channels.nixos.org/nixos-24.05/latest-nixos-minimal-x86_64-linux.iso

# Verify download (optional)
shasum -a 256 nixos-minimal-24.05-x86_64-linux.iso
```

```clojure
{:iso-choice
 {:minimal "~900 MB, console-only, perfect for development"
  :graphical "~3 GB, includes desktop environment, unnecessary for our use"
  :recommendation "Use minimal—we'll SSH in from macOS"}}
```

### Step 2: Create the Virtual Machine

```bash
# Set variables
VM_NAME="robotic-farm-nixos"
VM_DIR="$HOME/VirtualBox/VMs"
ISO_PATH="$HOME/VirtualBox/ISOs/nixos-minimal-24.05-x86_64-linux.iso"

# Create VM
VBoxManage createvm \
  --name "${VM_NAME}" \
  --ostype "Linux_64" \
  --register \
  --basefolder "${VM_DIR}"

# Configure resources
VBoxManage modifyvm "${VM_NAME}" \
  --memory 4096 \
  --cpus 2 \
  --vram 16 \
  --boot1 dvd \
  --boot2 disk \
  --boot3 none \
  --boot4 none \
  --firmware bios \
  --chipset piix3 \
  --pae on \
  --longmode on \
  --hpet on \
  --hwvirtex on \
  --nestedpaging on \
  --largepages on \
  --vtxvpid on

# Create disk (30 GB)
VBoxManage createhd \
  --filename "${VM_DIR}/${VM_NAME}/${VM_NAME}.vdi" \
  --size 30720 \
  --format VDI

# Add SATA controller and attach disk
VBoxManage storagectl "${VM_NAME}" \
  --name "SATA Controller" \
  --add sata \
  --controller IntelAhci \
  --portcount 2 \
  --hostiocache off \
  --bootable on

VBoxManage storageattach "${VM_NAME}" \
  --storagectl "SATA Controller" \
  --port 0 \
  --device 0 \
  --type hdd \
  --medium "${VM_DIR}/${VM_NAME}/${VM_NAME}.vdi"

# Add IDE controller and attach ISO
VBoxManage storagectl "${VM_NAME}" \
  --name "IDE Controller" \
  --add ide

VBoxManage storageattach "${VM_NAME}" \
  --storagectl "IDE Controller" \
  --port 0 \
  --device 0 \
  --type dvddrive \
  --medium "${ISO_PATH}"

# Configure networking (NAT with port forwarding)
VBoxManage modifyvm "${VM_NAME}" \
  --nic1 nat \
  --natpf1 "ssh,tcp,,2222,,22" \
  --natpf1 "http,tcp,,8080,,8080" \
  --natpf1 "https,tcp,,8443,,443"

# Enable bidirectional clipboard and drag-and-drop
VBoxManage modifyvm "${VM_NAME}" \
  --clipboard-mode bidirectional \
  --draganddrop bidirectional
```

```clojure
{:vm-configuration-explained
 {:memory-4096
  "4 GB RAM—enough for NixOS + Docker + Babashka builds"
  
  :cpus-2
  "2 CPUs—parallel builds will be faster"
  
  :disk-30720
  "30 GB disk—Nix store can grow large with many derivations"
  
  :nat-networking
  "NAT = VM can access internet, host accesses VM via port forwarding
   
   Why not bridged?
   - Bridged exposes VM to network (less secure)
   - NAT is simpler, works on any network (WiFi, Ethernet, VPN)"
  
  :port-forwarding
  {:ssh "Host port 2222 → VM port 22 (standard SSH)"
   :http "Host port 8080 → VM port 8080 (for web services)"
   :https "Host port 8443 → VM port 443 (for HTTPS services)"}}}
```

**Aristotelian Virtues in Configuration:**

- **Prudence (φρόνησις)**: 4 GB RAM is the mean between stinginess (2 GB, too slow) and excess (8 GB, wasteful on laptop)
- **Temperance (σωφροσύνη)**: 30 GB disk balances space needs with host constraints
- **Justice (δικαιοσύνη)**: 2 CPUs shares host resources fairly with macOS

### Step 3: Start the VM and Boot from ISO

```bash
# Start VM in GUI mode
VBoxManage startvm robotic-farm-nixos --type gui

# Alternative: Start headless (no window, access via SSH later)
# VBoxManage startvm robotic-farm-nixos --type headless
```

The VM will boot into the NixOS installer (live environment).

### Step 4: Install NixOS (Inside the VM)

Once the VM boots, you'll see a login prompt. Log in as `nixos` (no password needed).

#### Partition the Disk

```bash
# Inside the VM
sudo -i  # Become root

# List disks
lsblk
# Should show: sda (30G disk)

# Partition with fdisk or use quick script:
parted /dev/sda -- mklabel gpt
parted /dev/sda -- mkpart primary 512MB 100%
parted /dev/sda -- mkpart ESP fat32 1MB 512MB
parted /dev/sda -- set 2 esp on

# Format partitions
mkfs.ext4 -L nixos /dev/sda1
mkfs.fat -F 32 -n boot /dev/sda2

# Mount
mount /dev/disk/by-label/nixos /mnt
mkdir -p /mnt/boot
mount /dev/disk/by-label/boot /mnt/boot
```

#### Generate NixOS Configuration

```bash
# Generate base configuration
nixos-generate-config --root /mnt

# Edit configuration
nano /mnt/etc/nixos/configuration.nix
```

Replace the generated configuration with:

```nix
# /mnt/etc/nixos/configuration.nix
{ config, pkgs, ... }:

{
  imports = [ ./hardware-configuration.nix ];

  # Bootloader
  boot.loader.grub.enable = true;
  boot.loader.grub.device = "/dev/sda";

  # Networking
  networking.hostName = "robotic-farm-nixos";
  networking.networkmanager.enable = true;

  # Enable Flakes and nix-command
  nix.settings.experimental-features = [ "nix-command" "flakes" ];

  # Time zone
  time.timeZone = "America/Los_Angeles";  # Adjust to your timezone

  # Packages
  environment.systemPackages = with pkgs; [
    vim
    git
    curl
    wget
    htop
  ];

  # Enable SSH
  services.openssh = {
    enable = true;
    settings = {
      PermitRootLogin = "no";
      PasswordAuthentication = true;  # Will disable after SSH key setup
    };
  };

  # Enable Docker
  virtualisation.docker.enable = true;

  # Enable VirtualBox Guest Additions
  virtualisation.virtualbox.guest.enable = true;
  virtualisation.virtualbox.guest.x11 = false;  # No GUI needed

  # User account
  users.users.nixos = {
    isNormalUser = true;
    extraGroups = [ "wheel" "docker" "vboxsf" ];  # vboxsf for shared folders
    initialPassword = "nixos";  # Change this after first login!
  };

  # Enable sudo without password for wheel group (convenience)
  security.sudo.wheelNeedsPassword = false;

  # System version
  system.stateVersion = "24.05";
}
```

```clojure
{:configuration-explained
 {:experimental-features
  "Enables flakes and new nix CLI—required for our project"
  
  :openssh
  "SSH server—allows connection from macOS host"
  
  :docker
  "Docker daemon for container work"
  
  :virtualbox-guest
  "Guest Additions—enables shared folders, clipboard, time sync"
  
  :user-groups
  {:wheel "Sudo access"
   :docker "Run Docker without sudo"
   :vboxsf "Access VirtualBox shared folders"}}}
```

#### Install and Reboot

```bash
# Inside the VM, still as root
nixos-install

# Set root password when prompted (or skip and rely on nixos user)

# Reboot
reboot
```

The VM will reboot. **Remove the ISO** from the VM's optical drive so it boots from disk:

```bash
# On macOS host, while VM is shutting down:
VBoxManage storageattach robotic-farm-nixos \
  --storagectl "IDE Controller" \
  --port 0 \
  --device 0 \
  --type dvddrive \
  --medium none
```

### Step 5: SSH Setup (from macOS to VM)

Once the VM has rebooted and you see the login prompt:

#### Inside the VM

```bash
# Log in as nixos user
# Password: nixos (or whatever you set)

# Get IP address (should be 10.0.2.15 with NAT)
ip addr show

# Start SSH service (should already be running)
sudo systemctl status sshd
```

#### On macOS Host

```bash
# Test SSH connection
ssh -p 2222 nixos@localhost
# Password: nixos

# Generate SSH key pair (if you don't have one)
ssh-keygen -t ed25519 -f ~/.ssh/id_ed25519_nixos_vm -C "nixos-vm"

# Copy public key to VM
ssh-copy-id -p 2222 -i ~/.ssh/id_ed25519_nixos_vm nixos@localhost

# Add to ~/.ssh/config for convenience
cat >> ~/.ssh/config << 'EOF'

Host robotic-farm-vm
  HostName localhost
  Port 2222
  User nixos
  IdentityFile ~/.ssh/id_ed25519_nixos_vm
  StrictHostKeyChecking no
  UserKnownHostsFile /dev/null

EOF

# Now connect easily:
ssh robotic-farm-vm
```

#### Disable Password Authentication (Security)

Once SSH key works, disable password auth:

```bash
# On VM
sudo nano /etc/nixos/configuration.nix

# Change:
# PasswordAuthentication = true;
# To:
# PasswordAuthentication = false;

# Rebuild
sudo nixos-rebuild switch

# Test (from macOS):
ssh robotic-farm-vm  # Should work with key
```

### Step 6: Configure Shared Folder

#### On macOS Host

```bash
# Add shared folder
VBoxManage sharedfolder add robotic-farm-nixos \
  --name "robotic-farm" \
  --hostpath "/Users/YOUR_USERNAME/kae3g/12025-10-04" \
  --automount \
  --auto-mount-point "/home/nixos/robotic-farm"

# Restart VM to apply
VBoxManage controlvm robotic-farm-nixos acpipowerbutton
# Wait for graceful shutdown, then:
VBoxManage startvm robotic-farm-nixos --type headless
```

#### On VM

```bash
ssh robotic-farm-vm

# Verify shared folder is mounted
ls -la ~/robotic-farm
# Should see your project files!

# If not mounted, mount manually:
sudo mount -t vboxsf -o uid=1000,gid=100 robotic-farm ~/robotic-farm
```

```clojure
{:shared-folder-benefits
 ["Edit files on macOS with your preferred editor"
  "Changes immediately visible in VM"
  "No git push/pull needed for local dev"
  "Build artifacts from VM available on macOS"]
 
 :shared-folder-caveats
 ["Performance slower than native filesystem"
  "Some file watchers might not work (webpack, etc.)"
  "Symlinks can be problematic"
  "For intensive I/O, copy to VM's local disk"]}
```

### Step 7: Project Setup in VM

```bash
# SSH into VM
ssh robotic-farm-vm

# Navigate to project
cd ~/robotic-farm

# Enter development shell
nix develop

# Verify everything works
bb --version
clj --version
node --version

# Run build pipeline
bb build:pipeline

# Success! 🎉
```

```clojure
{:the-moment-of-truth
 "All your project dependencies—babashka, clojure, node—
  are now available in the VM, declaratively configured,
  reproducibly installed.
  
  The garden within the garden is complete.
  Alpine flowers bloom in the greenhouse.
  The microcosm reflects the macrocosm."}
```

## Advanced Configuration

### Optimized NixOS Configuration for Development

Create a custom configuration that integrates with your project:

```bash
# On VM
sudo nano /etc/nixos/configuration.nix
```

```nix
# Enhanced configuration for Robotic Farm development
{ config, pkgs, ... }:

{
  imports = [ ./hardware-configuration.nix ];

  # Bootloader
  boot.loader.grub.enable = true;
  boot.loader.grub.device = "/dev/sda";

  # Networking
  networking.hostName = "robotic-farm-nixos";
  networking.networkmanager.enable = true;
  networking.firewall.allowedTCPPorts = [ 22 8080 443 3000 ];

  # Nix configuration
  nix = {
    settings = {
      experimental-features = [ "nix-command" "flakes" ];
      auto-optimise-store = true;
      max-jobs = 2;
    };
    gc = {
      automatic = true;
      dates = "weekly";
      options = "--delete-older-than 30d";
    };
  };

  # Time zone
  time.timeZone = "America/Los_Angeles";

  # Locale
  i18n.defaultLocale = "en_US.UTF-8";

  # System packages
  environment.systemPackages = with pkgs; [
    # Editors
    vim
    neovim
    
    # Version control
    git
    git-lfs
    
    # Shell utilities
    curl
    wget
    htop
    btop
    tmux
    screen
    ripgrep
    fd
    jq
    yq
    
    # Development tools
    gnumake
    gcc
    
    # Clojure ecosystem (from nixpkgs)
    babashka
    clojure
    clj-kondo
    clojure-lsp
    
    # Node.js
    nodejs_22
    
    # Container tools
    docker-compose
    
    # Utilities
    unzip
    zip
    tree
    ncdu
  ];

  # Shell configuration
  programs.bash.enableCompletion = true;
  programs.bash.shellAliases = {
    ll = "ls -alF";
    la = "ls -A";
    l = "ls -CF";
    ..= "cd ..";
    ...= "cd ../..";
    rf = "cd ~/robotic-farm";
  };

  # SSH
  services.openssh = {
    enable = true;
    settings = {
      PermitRootLogin = "no";
      PasswordAuthentication = false;
      X11Forwarding = false;
    };
  };

  # Docker
  virtualisation.docker = {
    enable = true;
    autoPrune = {
      enable = true;
      dates = "weekly";
    };
  };

  # VirtualBox Guest
  virtualisation.virtualbox.guest = {
    enable = true;
    x11 = false;
  };

  # User
  users.users.nixos = {
    isNormalUser = true;
    description = "Robotic Farm Developer";
    extraGroups = [ "wheel" "docker" "vboxsf" ];
    shell = pkgs.bash;
    openssh.authorizedKeys.keys = [
      # Add your SSH public key here
      # "ssh-ed25519 AAAA... you@macbook"
    ];
  };

  # Sudo without password
  security.sudo.wheelNeedsPassword = false;

  # Automatic upgrades (optional)
  system.autoUpgrade = {
    enable = false;  # Set to true if desired
    allowReboot = false;
    channel = "https://nixos.org/channels/nixos-24.05";
  };

  system.stateVersion = "24.05";
}
```

Apply the configuration:

```bash
sudo nixos-rebuild switch
```

### Snapshot Strategy

```bash
# On macOS host

# Create snapshot before major changes
VBoxManage snapshot robotic-farm-nixos take "pre-upgrade-$(date +%Y%m%d)" \
  --description "Before NixOS upgrade"

# List snapshots
VBoxManage snapshot robotic-farm-nixos list

# Restore snapshot if something breaks
VBoxManage snapshot robotic-farm-nixos restore "pre-upgrade-20251006"

# Delete old snapshots
VBoxManage snapshot robotic-farm-nixos delete "old-snapshot-name"
```

```clojure
{:snapshot-philosophy
 "Like Git for your entire VM state.
  
  Create snapshots before:
  - Major NixOS upgrades
  - Experimental configurations
  - Dependency updates
  - System reconfigurations
  
  Snapshots enable fearless experimentation.
  This is the Sabbath principle applied to VMs:
  Return to a known good state, begin anew."}
```

### Performance Tuning

```bash
# On macOS host

# Increase video memory (if experiencing display issues)
VBoxManage modifyvm robotic-farm-nixos --vram 128

# Enable PAE/NX (if not already enabled)
VBoxManage modifyvm robotic-farm-nixos --pae on

# Adjust I/O APIC (can improve performance on multi-core)
VBoxManage modifyvm robotic-farm-nixos --ioapic on

# Enable nested paging (if supported by your CPU)
VBoxManage modifyvm robotic-farm-nixos --nestedpaging on

# Adjust paravirtualization interface
VBoxManage modifyvm robotic-farm-nixos --paravirtprovider kvm  # or default
```

```clojure
{:performance-expectations
 {:native-speed 100
  :virtualbox-intel 70-80
  :virtualbox-apple-silicon 30-50}
 
 :note
 "VirtualBox on Apple Silicon uses x86 emulation via Rosetta 2.
  Performance will be noticeably slower.
  
  For Apple Silicon, consider:
  - UTM (supports ARM64 natively)
  - Docker Desktop for Mac (uses hypervisor.framework)
  - But lose some VirtualBox convenience features"}
```

## Automated Scripts for the Repository

Let's create three scripts to include in your project:

### Script 1: setup-virtualbox-vm.sh

(Already created above—will go in `scripts/` directory)

### Script 2: install-nixos-in-vm.sh

```bash
#!/usr/bin/env bash
# install-nixos-in-vm.sh
# Run this INSIDE the VM after it boots from ISO
# Automates NixOS installation

set -euo pipefail

# Configuration
DISK="/dev/sda"
TIMEZONE="America/Los_Angeles"
HOSTNAME="robotic-farm-nixos"

echo "⚠️  WARNING: This will ERASE ${DISK}!"
read -p "Continue? (yes/no): " confirm
if [[ "$confirm" != "yes" ]]; then
    echo "Aborted."
    exit 1
fi

echo "🔧 Partitioning ${DISK}..."
parted ${DISK} -- mklabel gpt
parted ${DISK} -- mkpart primary 512MB 100%
parted ${DISK} -- mkpart ESP fat32 1MB 512MB
parted ${DISK} -- set 2 esp on

echo "📁 Formatting partitions..."
mkfs.ext4 -L nixos ${DISK}1
mkfs.fat -F 32 -n boot ${DISK}2

echo "🔗 Mounting filesystems..."
mount /dev/disk/by-label/nixos /mnt
mkdir -p /mnt/boot
mount /dev/disk/by-label/boot /mnt/boot

echo "📝 Generating configuration..."
nixos-generate-config --root /mnt

# Inject our custom configuration
cat > /mnt/etc/nixos/configuration.nix << 'NIXCONF'
{ config, pkgs, ... }:

{
  imports = [ ./hardware-configuration.nix ];
  
  boot.loader.grub.enable = true;
  boot.loader.grub.device = "/dev/sda";
  
  networking.hostName = "robotic-farm-nixos";
  networking.networkmanager.enable = true;
  
  nix.settings.experimental-features = [ "nix-command" "flakes" ];
  nix.settings.auto-optimise-store = true;
  
  time.timeZone = "America/Los_Angeles";
  
  environment.systemPackages = with pkgs; [
    vim git curl wget htop tmux
  ];
  
  services.openssh = {
    enable = true;
    settings = {
      PermitRootLogin = "no";
      PasswordAuthentication = true;
    };
  };
  
  virtualisation.docker.enable = true;
  virtualisation.virtualbox.guest.enable = true;
  virtualisation.virtualbox.guest.x11 = false;
  
  users.users.nixos = {
    isNormalUser = true;
    extraGroups = [ "wheel" "docker" "vboxsf" ];
    initialPassword = "nixos";
  };
  
  security.sudo.wheelNeedsPassword = false;
  
  system.stateVersion = "24.05";
}
NIXCONF

echo "🚀 Installing NixOS..."
nixos-install --no-root-password

echo "✅ Installation complete!"
echo ""
echo "Next steps:"
echo "1. Type 'reboot'"
echo "2. Remove ISO from VM optical drive"
echo "3. Boot into installed system"
echo "4. Log in as: nixos / nixos"
echo "5. Change password: passwd"
```

### Script 3: connect-to-vm.sh

```bash
#!/usr/bin/env bash
# connect-to-vm.sh
# Convenient wrapper for SSH into the VM

VM_NAME="robotic-farm-nixos"
SSH_PORT="2222"
SSH_USER="nixos"

# Check if VM is running
if ! VBoxManage list runningvms | grep -q "${VM_NAME}"; then
    echo "❌ VM '${VM_NAME}' is not running"
    echo ""
    echo "Start it with:"
    echo "  VBoxManage startvm ${VM_NAME} --type headless"
    exit 1
fi

# Connect
echo "🔗 Connecting to ${VM_NAME}..."
ssh -p ${SSH_PORT} ${SSH_USER}@localhost
```

## Integration with Project Flake

Update your project's `flake.nix` to document VM setup:

```nix
# Add to your existing flake.nix
{
  description = "Robotic Farm with VirtualBox VM support";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
  };

  outputs = { self, nixpkgs }: {
    # Existing outputs...
    
    # NixOS configuration for VirtualBox VM
    nixosConfigurations.robotic-farm-vm = nixpkgs.lib.nixosSystem {
      system = "x86_64-linux";
      modules = [
        # Can reference a separate vm-configuration.nix file
        ./nixos/vm-configuration.nix
      ];
    };
    
    # Development shell (existing)
    devShells.x86_64-linux.default = # ...
    
    # VM-specific dev shell (optional)
    devShells.x86_64-linux.vm = 
      let pkgs = nixpkgs.legacyPackages.x86_64-linux;
      in pkgs.mkShell {
        buildInputs = with pkgs; [
          # VM management tools
          virtualbox
          
          # Your regular dev tools
          babashka
          clojure
          nodejs_22
        ];
        
        shellHook = ''
          echo "Robotic Farm VM Development Shell"
          echo "=================================="
          echo ""
          echo "VM Commands:"
          echo "  ./scripts/setup-virtualbox-vm.sh  - Create VM"
          echo "  ./scripts/connect-to-vm.sh        - SSH into VM"
          echo ""
        '';
      };
  };
}
```

## Troubleshooting

```clojure
{:common-issues
 {:vm-wont-start
  {:symptoms "Black screen, boot loop, or immediate crash"
   :solutions
   ["Check VirtualBox logs: ~/VirtualBox/VMs/robotic-farm-nixos/Logs/"
    "Verify virtualization enabled in BIOS (Intel VT-x / AMD-V)"
    "On macOS: System Preferences → Security → Allow VirtualBox kernel extension"
    "Try: VBoxManage modifyvm robotic-farm-nixos --firmware efi"]}
  
  :ssh-connection-refused
  {:symptoms "Connection refused on port 2222"
   :solutions
   ["Check VM is running: VBoxManage list runningvms"
    "Inside VM: sudo systemctl status sshd"
    "Check port forwarding: VBoxManage showvminfo robotic-farm-nixos | grep ssh"
    "Verify firewall: sudo nixos-rebuild switch"]}
  
  :shared-folder-not-working
  {:symptoms "~/robotic-farm directory empty or doesn't exist"
   :solutions
   ["Check Guest Additions: lsmod | grep vboxsf"
    "Verify mount: mount | grep vboxsf"
    "Manual mount: sudo mount -t vboxsf -o uid=1000,gid=100 robotic-farm ~/robotic-farm"
    "Add to /etc/fstab for automatic mounting"]}
  
  :slow-performance
  {:symptoms "Builds taking forever, system laggy"
   :solutions
   ["Allocate more RAM: VBoxManage modifyvm robotic-farm-nixos --memory 6144"
    "More CPUs: VBoxManage modifyvm robotic-farm-nixos --cpus 4"
    "Enable nested paging: VBoxManage modifyvm robotic-farm-nixos --nestedpaging on"
    "On Apple Silicon: Consider UTM or Docker Desktop instead"]}
  
  :nix-build-fails
  {:symptoms "nix build or nix develop fails with obscure errors"
   :solutions
   ["Update flake.lock: nix flake update"
    "Clear Nix cache: nix-collect-garbage -d"
    "Check disk space: df -h"
    "Verify internet connection: curl -I https://cache.nixos.org"]}
  
  :guest-additions-missing
  {:symptoms "Shared folders, clipboard don't work"
   :solutions
   ["In /etc/nixos/configuration.nix, ensure: virtualisation.virtualbox.guest.enable = true"
    "Rebuild: sudo nixos-rebuild switch"
    "Reboot VM"
    "Check: lsmod | grep vbox"]}}}
```

## Documentation Structure in Repository

Create these files:

```
12025-10-04/
├── scripts/
│   ├── setup-virtualbox-vm.sh         # Automates VM creation
│   ├── install-nixos-in-vm.sh         # Run inside VM to install NixOS
│   └── connect-to-vm.sh               # Convenient SSH wrapper
├── nixos/
│   ├── vm-configuration.nix           # Declarative VM config
│   └── README.md                      # NixOS-specific docs
├── docs/
│   └── VIRTUALBOX-SETUP.md            # User-facing setup guide
└── writings/
    └── 9996-nixos-dev-containers-vms.md # This deep dive
```

## The Five Virtues of This Setup

```clojure
{:confucian-five-virtues
 {:仁-ren-benevolence
  "The setup is kind to beginners—automated scripts,
   clear documentation, multiple entry points."
  
  :義-yi-righteousness
  "The configuration is declarative and version-controlled,
   making it morally correct (reproducible, auditable)."
  
  :禮-li-propriety
  "The structure follows conventions—Nix flakes, NixOS modules,
   standard paths (/etc/nixos/configuration.nix)."
  
  :智-zhi-wisdom
  "The design balances flexibility and constraint—
   opinionated defaults, but easily customizable."
  
  :信-xin-trustworthiness
  "The documentation is honest about tradeoffs—
   performance on Apple Silicon, shared folder caveats."}}
```

## Comparison with Alternatives

```clojure
{:alternatives
 {:docker-desktop-for-mac
  {:pros ["Faster than VirtualBox"
          "Native hypervisor (not emulation)"
          "Integrated with macOS"]
   :cons ["Black box—less control"
          "macOS only"
          "Not NixOS (loses declarative host config)"]
   :use-when "Just need Docker, don't care about full NixOS"}
  
  :utm-on-apple-silicon
  {:pros ["Native ARM64 support (fast on M1/M2/M3)"
          "Can run ARM NixOS images"
          "Better performance than VirtualBox"]
   :cons ["Less mature than VirtualBox"
          "Fewer features (snapshots, export/import)"
          "ARM64 vs x86_64 might cause compatibility issues"]
   :use-when "On Apple Silicon and need performance"}
  
  :multipass
  {:pros ["Canonical's official Ubuntu VM tool"
          "Very lightweight"
          "Fast instance creation"]
   :cons ["Ubuntu-focused (not NixOS)"
          "Less control than VirtualBox"
          "Not declarative"]
   :use-when "Need quick Ubuntu instances, not Nix"}
  
  :vagrant
  {:pros ["Infrastructure as code"
          "Large community"
          "Cross-platform (VirtualBox, VMware, Hyper-V)"]
   :cons ["Extra layer of abstraction"
          "Ruby dependency"
          "Nix flakes make Vagrant less necessary"]
   :use-when "Team is already using Vagrant"}
  
  :nixos-on-bare-metal
  {:pros ["Maximum performance"
          "Full control"
          "No virtualization overhead"]
   :cons ["Requires dedicated machine or dual-boot"
          "Less flexible (can't snapshot easily)"
          "Riskier (could break bootloader)"]
   :use-when "Fully committed to NixOS as daily driver"}}}
```

**Recommendation for Robotic Farm:**

```clojure
{:our-choice :virtualbox-nixos
 :reasoning
 ["We already use Nix (flake.nix)—extend to VM layer"
  "VirtualBox is mature, well-documented, cross-platform"
  "Snapshots enable fearless experimentation"
  "Shared folders make development smooth"
  "Declarative VM config fits project philosophy"
  "Can export VM and share with team"
  "Works on both Intel and Apple Silicon Macs (albeit slower on ARM)"]
 
 :trade-offs-accepted
 ["Performance penalty (acceptable for development)"
  "Slower on Apple Silicon (but still usable)"
  "Shared folder I/O not as fast as native (minor issue)"]
 
 :future-evolution
 "If team grows, consider:
  - NixOS cloud instances (AWS, GCP, Digital Ocean)
  - NixOS bare metal for CI/CD
  - But keep VirtualBox for local development"
}
```

## The Way of the Garden

```clojure
{:from-ch-an
 "\"The garden is not separate from the mind.
   The mind is not separate from the garden.
   When you tend the garden, you tend the mind.\"
   
   Our VM is a garden:
   - Carefully planned (configuration.nix)
   - Regularly tended (nixos-rebuild switch)
   - Protected from wilderness (snapshots)
   - Yielding fruits (reproducible builds)
   
   The macOS host is the surrounding forest:
   - Wild, uncontrolled (Homebrew, system packages)
   - But rich and diverse (many tools)
   
   The VM garden exists within the forest,
   cultivated space within wild space,
   order within chaos,
   reproducibility within entropy.
   
   This is Wu Wei applied to infrastructure:
   Effortless action through proper structure."
 
 :from-i-ching
 "Hexagram 3: Difficulty at the Beginning (屯 Zhun)
   
   'Times of growth are beset with difficulties.
    They resemble a first birth.
    But these difficulties arise from the very profusion of all that is struggling to attain form.
    Everything is in motion: therefore if one perseveres there is a prospect of great success.'
   
   Setting up the VM is difficulty at the beginning.
   Partitioning, configuring, troubleshooting.
   But once established, great success follows:
   Reproducible development environment,
   Fearless experimentation,
   Team collaboration.
   
   Perseverance furthers."
 
 :from-gospel
 "\"Therefore everyone who hears these words of mine and puts them into practice
    is like a wise man who built his house on the rock.
    The rain came down, the streams rose, and the winds blew and beat against that house;
    yet it did not fall, because it had its foundation on the rock.\"
    — Matthew 7:24-25
   
   The VirtualBox VM is the house on rock:
   - Foundation: NixOS declarative config
   - Rock: Immutable Nix store
   - Rain: Dependency updates
   - Streams: Team members pulling code
   - Winds: macOS upgrades
   
   The house stands because it's built on rock,
   not sand (mutable system state)."}
```

## Conclusion: The Complete Environment

```clojure
{:the-stack
 {:layer-1-hardware "MacBook Pro (macOS Sequoia)"
  :layer-2-hypervisor "VirtualBox 7.x"
  :layer-3-guest-os "NixOS 24.05 (declarative, reproducible)"
  :layer-4-dev-env "nix develop (project flake.nix)"
  :layer-5-tools "babashka, clojure, node"
  :layer-6-application "Robotic Farm pipeline"
  
  :beauty
  "Each layer declaratively configured:
   - VirtualBox: VBoxManage scripts
   - NixOS: configuration.nix
   - Dev env: flake.nix
   - Application: bb.edn, deps.edn
   
   Reproducibility all the way down.
   Infrastructure as code, deeply nested.
   Gardens within gardens."}
 
 :next-steps
 ["Run ./scripts/setup-virtualbox-vm.sh"
  "Install NixOS using automated or manual process"
  "Configure SSH keys for passwordless access"
  "Enter nix develop in VM"
  "Build your project: bb build:pipeline"
  "Create snapshot for safety"
  "Document any customizations in nixos/vm-configuration.nix"
  "Commit everything to Git"
  "Share VM export with team (optional)"]
 
 :the-promise
 "Now you have a reproducible development environment that:
  - Works identically on any macOS machine
  - Can be snapshotted before risky changes
  - Keeps your host OS clean
  - Uses declarative configuration (version controlled)
  - Integrates with your existing Nix-based project
  - Can be exported and shared with team
  - Survives macOS upgrades and host system changes
  
  This is infrastructure as contemplative practice:
  Mindful, deliberate, reproducible.
  
  L'dor v'dor—from generation to generation."}
```

## References & Further Reading

```clojure
{:virtualbox
 ["VirtualBox User Manual: https://www.virtualbox.org/manual/"
  "VBoxManage Reference: https://www.virtualbox.org/manual/ch08.html"
  "VirtualBox Downloads: https://www.virtualbox.org/wiki/Downloads"]
 
 :nixos
 ["NixOS Manual: https://nixos.org/manual/nixos/stable/"
  "NixOS Wiki: https://nixos.wiki/"
  "Nix Pills: https://nixos.org/guides/nix-pills/"]
 
 :flakes
 ["Nix Flakes: https://nixos.wiki/wiki/Flakes"
  "Zero to Nix: https://zero-to-nix.com/"
  "Flake Reference: https://nixos.org/manual/nix/stable/command-ref/new-cli/nix3-flake.html"]
 
 :philosophy
 ["The Way of Ch'an (David Hinton)"
  "I Ching (Richard Wilhelm translation)"
  "The Analects of Confucius (Edward Slingerland)"
  "Gospel of Matthew (on building foundations)"]}
```

---

**Next Writing:** 9995 — *(To be determined—perhaps "Kubernetes GitOps with Nix" or "ClojureScript Build Tools")*  
**Previous Writing:** [9997-framework-laptop-microkernel-dev.md](9997-framework-laptop-microkernel-dev) — Framework Laptop Microkernel Development

---

*"As above, so below.  
As the macOS, so the VM.  
As the host, so the guest.  
Gardens within gardens,  
consciousness reflecting consciousness,  
reproducibility nested in reproducibility."*

— From the Hermetic tradition, applied to virtualization

*The setup is complete. The environment is ready. The work begins anew.*


---



# Part IV: Container Philosophy — Alpine vs NixOS

## The Two Philosophies

```clojure
{:alpine-linux
 {:philosophy "Minimalism through subtraction (減 jiǎn)"
  :approach "Start with nothing, add only what's necessary"
  :analogies ["Zen rock garden"
              "Hemingway's prose"
              "Bauhaus design"]
  
  :principles
  {:simplicity "Use busybox for core utilities"
   :security "Small surface area → fewer vulnerabilities"
   :efficiency "musl libc → smaller, simpler"
   :pragmatism "Good enough for most use cases"}
  
  :base-image "~5-7 MB"
  :libc "musl (POSIX-compliant, minimal)"
  :package-manager "apk (Alpine Package Keeper)"
  :update-model "Rolling release (edge) or stable branches"
  
  :from-tao-te-ching
  "In the pursuit of learning, every day something is acquired.
   In the pursuit of Tao, every day something is dropped.
   — Chapter 48
   
   Alpine drops everything non-essential."}
 
 :nixos
 {:philosophy "Completeness through purity (純 chún)"
  :approach "Declare everything precisely, guarantee reproducibility"
  :analogies ["Cathedral blueprint"
              "Mathematical proof"
              "Functional programming"]
  
  :principles
  {:reproducibility "Same inputs → identical outputs"
   :isolation "Content-addressed store paths"
   :declarative "System configuration as code"
   :purity "Builds in isolated sandboxes"}
  
  :base-image "~50-200 MB (depends on closure)"
  :libc "glibc (standard, full-featured)"
  :package-manager "nix (functional, declarative)"
  :update-model "Atomic upgrades, rollbacks, generations"
  
  :from-i-ching
  "The Creative works sublime success,
   Furthering through perseverance.
   — Hexagram 1 (乾 Qian)
   
   NixOS creates complete, self-contained systems."}
 
 :the-tension
 "Alpine asks: 'What can we remove?'
  NixOS asks: 'What must we guarantee?'
  
  Both are noble questions.
  Both lead to excellence.
  But they optimize for different virtues."}
```

## Container Size & Reproducibility Tradeoffs

```clojure
{:alpine-tradeoffs
 {:image-size "~207 MB for Clojure web service"
  :reproducibility-score "6/10 with best practices"
  :challenge "Package versions drift over time"
  
  :mitigation
  ["Pin Alpine base by digest"
   "Pin every package version explicitly"
   "Private Alpine mirror with frozen packages"]}
 
 :nixos-tradeoffs
 {:image-size "~330 MB for same service (but shared layers at scale)"
  :reproducibility-score "10/10"
  :guarantee "Bit-for-bit identical builds"
  
  :cluster-efficiency
  "At 10+ services, NixOS wins via content-addressed layer sharing"}}
```

---



# Part IV: Build Management — Leiningen vs Nix

## Core Similarities

```clojure
{:shared-concerns
 {:dependency-management
  {:leiningen "Manages Java/Clojure dependencies via Maven coordinates"
   :nix "Manages system packages via content-addressed store paths"
   :similarity "Both track what your project needs"}
  
  :declarative-configuration
  {:leiningen "project.clj declares dependencies, plugins, profiles"
   :nix "*.nix files declare packages, build steps, environments"
   :similarity "Both use files to specify 'what', not 'how'"}
  
  :reproducibility-goals
  {:leiningen "Same project.clj → same dependencies (ideally)"
   :nix "Same flake.lock → same build (guaranteed)"
   :similarity "Both aim to eliminate 'works on my machine'"}}}
```

## Fundamental Differences

### Scope & Abstraction Level

```clojure
{:leiningen
 {:scope :application-build
  :domain :clojure-jvm
  :abstraction-level :project
  
  :manages
  ["Clojure dependencies (from Maven/Clojars)"
   "Java dependencies (via Maven coordinates)"
   "Build tasks (compile, test, uberjar, deploy)"
   "REPL environment"]
  
  :assumes-exist
  ["Java Development Kit (JDK)"
   "Leiningen itself (bootstrap)"
   "Network access to Maven Central/Clojars"]}
 
 :nix
 {:scope :system-wide-configuration
  :domain :any-software
  :abstraction-level :operating-system
  
  :manages
  ["Programming language runtimes (JDK, Python, Node.js)"
   "Build tools (Leiningen, Cargo, npm)"
   "System libraries (OpenSSL, zlib, glibc)"
   "Entire OS configurations (NixOS)"]
  
  :assumes-exist
  ["Nix package manager itself"
   "Linux kernel (or macOS Darwin)"
   "Nix store directory (/nix/store)"]}}
```

**The Key Insight:**

```clojure
{:metaphor
 "Leiningen is a chef's recipe—assumes a kitchen exists.
  Nix is an architect's blueprint for the entire restaurant."}
```

### Dependency Resolution Philosophy

```clojure
{:leiningen-resolution
 {:timing :runtime
  :approach :dynamic
  :idempotency "Best effort"
  :problem "Library republished → different bytes for 'same' version"}
 
 :nix-resolution
 {:timing :build-time
  :approach :static
  :idempotency "Cryptographically guaranteed"
  :guarantee "Same flake.lock → bit-for-bit identical build"}}
```

## When to Use Which

### Alpine + Leiningen

**Use When:**
- Small team (< 5 people)
- Pure Clojure projects
- Fast iteration priority
- Simple deployment (uberjars to servers)

**Example Dockerfile:**

```dockerfile
FROM alpine:3.19 AS builder
RUN apk add --no-cache openjdk17 bash curl
# Install Leiningen, build uberjar
FROM alpine:3.19
RUN apk add --no-cache openjdk17-jre
COPY --from=builder /build/target/*-standalone.jar ./app.jar
CMD ["java", "-jar", "app.jar"]
```

### NixOS + Nix

**Use When:**
- Regulated industries (SOC2, PCI, HIPAA)
- Multi-language projects (Clojure + Python + Node.js + Rust)
- Large scale (15+ services benefit from layer deduplication)
- Long-term maintenance (decades)

**Example flake.nix:**

```nix
{
  outputs = { self, nixpkgs }: {
    packages.x86_64-linux.docker = pkgs.dockerTools.buildLayeredImage {
      name = "my-app";
      contents = [ myapp pkgs.cacert ];
      config.Cmd = [ "${myapp}/bin/myapp" ];
    };
  };
}
```

### The Hybrid Approach

```clojure
{:best-of-both
 "Use Nix for dev environment (flake.nix provides JDK, tools).
  Use Leiningen for builds (fast REPL iteration).
  Deploy uberjars (simple, familiar to ops).
  
  This is the harmonious middle way!"}
```

---


# Part IV: Real-World Decision Matrix

## Scenario-Based Recommendations

```clojure
{:early-stage-startup
 {:alpine-leiningen "⭐⭐⭐⭐⭐ Excellent"
  :reasoning
  ["Team knows Docker already"
   "Fast iteration critical"
   "Small scale—size differences negligible"]
  
  :nixos-nix "⭐⭐ Poor"
  :reasoning
  ["Learning curve would slow team"
   "Over-engineering for current scale"]}
 
 :fintech-regulated
 {:alpine-leiningen "⭐⭐⭐ Acceptable"
  :reasoning
  ["Can meet compliance with discipline"
   "Pin all versions explicitly"
   "Maintain private Alpine mirror"]
  
  :nixos-nix "⭐⭐⭐⭐⭐ Excellent"
  :reasoning
  ["Reproducibility required by auditors"
   "Provenance built-in (flake.lock)"
   "Bit-for-bit builds simplify audit trail"]}
 
 :polyglot-platform
 {:alpine-leiningen "⭐⭐ Poor"
  :reasoning
  ["Each language needs different patterns"
   "Alpine's musl causes issues (Python native extensions)"]
  
  :nixos-nix "⭐⭐⭐⭐⭐ Excellent"
  :reasoning
  ["Single flake.nix manages all languages"
   "Unified dependency management"
   "glibc compatibility for all ecosystems"]}}
```

## The Confucian Five Relationships

```clojure
{:five-relationships-in-systems
 {1 {:entities ["Developer" "Build Tool"]
     :alpine-leiningen "Tool adapts to dev's needs"
     :nixos-nix "Tool enforces purity"
     :virtue "Respect (敬)"}
  
  2 {:entities ["Code" "Dependencies"]
     :alpine-leiningen "Dependencies resolved at runtime"
     :nixos-nix "Dependencies locked at declaration"
     :virtue "Trust (信)"}
  
  3 {:entities ["Build Process" "Environment"]
     :alpine-leiningen "Process trusts environment"
     :nixos-nix "Process creates own environment"
     :virtue "Self-reliance (自立)"}
  
  4 {:entities ["Development" "Production"]
     :alpine-leiningen "Similar but not identical"
     :nixos-nix "Bit-for-bit identical"
     :virtue "Consistency (一致)"}
  
  5 {:entities ["Present Build" "Future Build"]
     :alpine-leiningen "Depends on what's available then"
     :nixos-nix "Guaranteed same if inputs unchanged"
     :virtue "Continuity (連續)"}}}
```

## Conclusion: The Way of Harmony

```clojure
{:synthesis
 "Alpine and Leiningen represent pragmatic minimalism—
  trust the ecosystem, iterate quickly, accept some uncertainty.
  
  NixOS and Nix represent principled completeness—
  control everything, guarantee reproducibility, invest upfront.
  
  Neither is universally superior. Context determines the right choice.
  
  For most teams:
  Start with Alpine + Leiningen (familiar, fast).
  Migrate to NixOS + Nix when you hit:
  - Reproducibility pain (drift, dependency hell)
  - Scale challenges (many services, layer duplication)
  - Compliance requirements (audit trails, provenance)
  - Polyglot complexity (managing many ecosystems)
  
  The complete stack:
  
  ┌─────────────────────────────────────────┐
  │ Nix (System Layer)                      │
  │ - Provides JDK, Leiningen, tools       │
  │ - Guarantees reproducible environment   │
  └───────────┬─────────────────────────────┘
              │
              ↓
  ┌─────────────────────────────────────────┐
  │ Leiningen/Alpine (Application Layer)    │
  │ - Manages Clojure dependencies         │
  │ - Fast iteration, familiar patterns     │
  └───────────┬─────────────────────────────┘
              │
              ↓
  ┌─────────────────────────────────────────┐
  │ Your Clojure Code                       │
  │ - Business logic                        │
  │ - Runs in reproducible environment      │
  │ - Developed with fast REPL feedback     │
  └─────────────────────────────────────────┘
  
  This is the way of Wu Wei (無為)—effortless action.
  Each tool does what it does best, without forcing."}
 
 :from-the-gospel
 "Render unto Caesar the things that are Caesar's,
  and unto God the things that are God's. — Mark 12:17
  
  Application:
  Render unto Nix the system configuration,
  Render unto Leiningen the Clojure builds,
  Render unto Alpine the minimal runtime,
  Render unto NixOS the complete reproducibility."}
```

## References & Further Reading

```clojure
{:alpine-linux
 ["https://alpinelinux.org/"
  "https://wiki.alpinelinux.org/wiki/Docker"
  "musl vs glibc: https://wiki.musl-libc.org/functional-differences-from-glibc.html"]
 
 :nixos
 ["https://nixos.org/"
  "Nix Docker Tools: https://nixos.org/manual/nixpkgs/stable/#sec-pkgs-dockerTools"
  "Zero to Nix: https://zero-to-nix.com/"]
 
 :leiningen
 ["https://leiningen.org/"
  "Leiningen Tutorial: https://github.com/technomancy/leiningen/blob/stable/doc/TUTORIAL.md"]
 
 :philosophy
 ["Tao Te Ching (on minimalism vs completeness)"
  "I Ching (on complementary forces)"
  "Confucius, Analects (on the Mean)"
  "Gospel of Matthew & Mark (on rendering unto each their due)"]}
```

---

**Next Writing:** 9995 — *(To be determined)*  
**Previous Writing:** [9997-framework-laptop-microkernel-dev.md](9997-framework-laptop-microkernel-dev) — Framework Laptop Microkernel Development

---

*"In dwelling, live close to the ground.  
In thinking, keep to the simple.  
In conflict, be fair and generous.  
In governing, don't try to control.  
In work, do what you enjoy.  
In family life, be completely present."*

— Laozi, Tao Te Ching, Chapter 8

*Alpine dwells close to the ground (minimal).  
NixOS thinks simply (declarative).  
Leiningen works with what you enjoy (REPL flow).  
Nix doesn't try to control (each tool has its place).  
Be completely present in your choice (commit fully).*

---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*