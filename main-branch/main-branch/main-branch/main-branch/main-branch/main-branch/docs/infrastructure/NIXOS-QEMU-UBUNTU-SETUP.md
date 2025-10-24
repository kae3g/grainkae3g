# NixOS in QEMU/KVM within Ubuntu 24.04 LTS

**Running NixOS virtual machines on Framework 16 with Cursor workspace access**

**Session**: 812  
**Grainpath**: `12025-10-23--0332--PDT--moon-vishakha------asc-gem000--sun-03rd--kae3g`  
**Philosophy**: `now == next + 1` - Test NixOS without leaving Ubuntu

---

## 🎯 Architecture Overview

```
Ubuntu 24.04 LTS (Host) - Framework 16
├── Cursor IDE
│   └── Workspace: ~/kae3g/grainkae3g/
│
├── QEMU/KVM Hypervisor
│   └── NixOS Guest VM
│       ├── Shared folder → ~/kae3g/grainkae3g/ (9p/virtfs)
│       ├── SSH access from host
│       ├── Network bridge to host
│       └── Can run: nix-build, lein, bb commands
│
└── Multi-distro pipeline
    ├── Alpine (musl, apk) - Primary
    ├── NixOS (nix) - Test in VM
    └── Ubuntu (deb) - Host system
```

## 🚀 Quick Start - NixOS in 3 Commands

```bash
# 1. Install QEMU/KVM on Ubuntu
sudo apt install qemu-kvm libvirt-daemon-system virtinst virt-manager

# 2. Download NixOS ISO
wget https://channels.nixos.org/nixos-24.05/latest-nixos-minimal-x86_64-linux.iso

# 3. Create and start VM
virt-install \
  --name nixos-grain6 \
  --ram 4096 \
  --disk size=20 \
  --vcpus 4 \
  --os-variant nixos-unknown \
  --cdrom latest-nixos-minimal-x86_64-linux.iso \
  --network bridge=virbr0 \
  --graphics spice \
  --filesystem source=/home/xy/kae3g,target=kae3g-workspace,mode=mapped
```

---

## 📁 Cursor Workspace Access Methods

### Method 1: 9p/virtfs Shared Folder (Recommended)

**Advantages**:
- Direct access to host filesystem
- No file copying needed
- Real-time sync
- Perfect for Cursor workspace

**Setup**:

```bash
# Add to virt-install
--filesystem source=/home/xy/kae3g,target=kae3g-workspace,mode=mapped
```

**Inside NixOS guest**:

```bash
# Mount the shared folder
sudo mkdir -p /mnt/kae3g
sudo mount -t 9p -o trans=virtio,version=9p2000.L kae3g-workspace /mnt/kae3g

# Make permanent (add to /etc/fstab)
echo "kae3g-workspace /mnt/kae3g 9p trans=virtio,version=9p2000.L,rw 0 0" | sudo tee -a /etc/fstab

# Access workspace
cd /mnt/kae3g/grainkae3g
ls -la
```

### Method 2: NFS Share

**Advantages**:
- Native Linux file sharing
- Good performance
- Standard protocol

**Host Ubuntu setup**:

```bash
# Install NFS server
sudo apt install nfs-kernel-server

# Export directory
echo "/home/xy/kae3g 192.168.122.0/24(rw,sync,no_subtree_check)" | sudo tee -a /etc/exports
sudo exportfs -ra
sudo systemctl restart nfs-server
```

**NixOS guest**:

```nix
# /etc/nixos/configuration.nix
{
  fileSystems."/mnt/kae3g" = {
    device = "192.168.122.1:/home/xy/kae3g";
    fsType = "nfs";
    options = [ "nfsvers=4.2" "rw" ];
  };
}
```

### Method 3: sshfs (Simplest)

**Advantages**:
- No host configuration
- Just works over SSH
- Easy to set up

**NixOS guest**:

```bash
# Install sshfs
nix-env -iA nixos.sshfs

# Mount via SSH
sshfs xy@192.168.122.1:/home/xy/kae3g /mnt/kae3g

# Make permanent
echo "sshfs#xy@192.168.122.1:/home/xy/kae3g /mnt/kae3g fuse defaults,allow_other 0 0" | sudo tee -a /etc/fstab
```

### Method 4: Git-based Sync

**For minimal changes only**:

```bash
# In NixOS guest
git clone /mnt/kae3g/grainkae3g /home/nixos/grainkae3g
cd /home/nixos/grainkae3g

# Make changes
vim some-file.clj

# Push back to host
git commit -am "Changes from NixOS VM"
git push origin nixos-dev
```

---

## 🔧 QEMU/KVM Configuration for Grain6

### Optimal VM Settings

```bash
virt-install \
  --name nixos-grain6 \
  --description "NixOS VM for grain6 development and testing" \
  --ram 8192 \
  --vcpus 4 \
  --cpu host \
  --disk path=/var/lib/libvirt/images/nixos-grain6.qcow2,size=40,format=qcow2 \
  --cdrom /home/xy/Downloads/nixos-minimal.iso \
  --os-variant nixos-unknown \
  --network bridge=virbr0,model=virtio \
  --graphics spice,listen=127.0.0.1 \
  --video qxl \
  --channel spicevmc \
  --filesystem source=/home/xy/kae3g,target=kae3g-workspace,mode=mapped \
  --boot uefi \
  --features kvm_hidden=on \
  --clock offset=utc,rtc_tickpolicy=catchup \
  --pm suspend_to_mem=no,suspend_to_disk=no
```

### QEMU Performance Tuning

```bash
# Enable KVM acceleration
lsmod | grep kvm  # Should show kvm_intel or kvm_amd

# Add your user to libvirt groups
sudo usermod -aG libvirt,kvm $(whoami)

# Verify virtualization
virt-host-validate qemu
```

---

## 🏔️ NixOS Configuration for Grain6

### `/etc/nixos/configuration.nix`

```nix
{ config, pkgs, ... }:

{
  imports = [ ./hardware-configuration.nix ];
  
  # System packages - Alpine-compatible where possible
  environment.systemPackages = with pkgs; [
    # Grain6 dependencies
    babashka
    leiningen
    s6
    s6-rc
    
    # Development tools
    git
    vim
    htop
    
    # Build tools
    gcc
    gnumake
    pkgconfig
    
    # For Alpine package building
    apk-tools  # If available in nixpkgs
  ];
  
  # Enable musl for Alpine compatibility testing
  # (NixOS uses glibc by default)
  environment.variables = {
    GRAIN6_LIBC = "glibc";  # Note: running on glibc, building for musl
    GRAIN6_TARGET_DISTRO = "alpine";
  };
  
  # Mount Cursor workspace from host
  fileSystems."/mnt/kae3g" = {
    device = "kae3g-workspace";
    fsType = "9p";
    options = [ "trans=virtio" "version=9p2000.L" "rw" ];
  };
  
  # Symlink to user home for convenience
  system.activationScripts.linkWorkspace = ''
    mkdir -p /home/nixos
    ln -sf /mnt/kae3g /home/nixos/kae3g
  '';
  
  # SSH access from host
  services.openssh = {
    enable = true;
    settings.PermitRootLogin = "no";
    settings.PasswordAuthentication = true;
  };
  
  # User account
  users.users.nixos = {
    isNormalUser = true;
    home = "/home/nixos";
    extraGroups = [ "wheel" "networkmanager" ];
    initialPassword = "grain6";  # Change on first login!
  };
  
  # Grain6 service (s6 integration)
  systemd.services.grain6 = {
    enable = false;  # Prefer s6, but provide systemd option
    description = "Grain6 time-aware process supervision";
    after = [ "network.target" ];
    wantedBy = [ "multi-user.target" ];
    serviceConfig = {
      ExecStart = "${pkgs.babashka}/bin/bb /mnt/kae3g/grainkae3g/grainstore/grain6/scripts/grain6.bb";
      Restart = "always";
    };
  };
  
  # Networking
  networking = {
    hostName = "nixos-grain6";
    networkmanager.enable = true;
    firewall.enable = true;
    firewall.allowedTCPPorts = [ 22 ];  # SSH
  };
  
  # Boot configuration
  boot.loader.grub = {
    enable = true;
    device = "/dev/vda";
  };
  
  system.stateVersion = "24.05";
}
```

---

## 🔄 Workflow - Edit on Ubuntu, Test in NixOS

### Typical Development Session

**On Host (Ubuntu 24.04 LTS + Cursor)**:

```bash
# 1. Edit code in Cursor
cursor ~/kae3g/grainkae3g

# 2. Save changes (auto-synced to NixOS VM via 9p)

# 3. SSH into NixOS VM
ssh nixos@192.168.122.X  # Get IP from 'virsh domifaddr nixos-grain6'

# Or use virt-manager GUI console
```

**In Guest (NixOS VM)**:

```bash
# 4. Navigate to shared workspace
cd /mnt/kae3g/grainkae3g

# 5. Test changes
lein test
bb test
nix-build

# 6. Build Alpine package (for musl target)
lein alpine
```

**Back on Host**:

```bash
# 7. Commit and deploy (changes already in workspace)
gb flow "Tested in NixOS VM, verified musl compatibility"
```

---

## 🐧 Multi-Distro Build Pipeline

### Build Order (Grain6)

```
1. Alpine (musl, apk) ← PRIMARY TARGET
   ↓
2. NixOS (nix, glibc) ← Test in VM
   ↓
3. Ubuntu LTS (deb, glibc) ← Host system
   ↓
4. Debian Stable (deb, glibc) ← Conservative fallback
```

### Automated Build Script

```bash
#!/usr/bin/env bash
# scripts/build-multi-distro.sh

set -euo pipefail

echo "🌾 Grain6 Multi-Distro Build Pipeline"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# 1. Build Alpine package (PRIMARY)
echo ""
echo "1️⃣ Building for Alpine Linux (musl libc, apk)..."
lein alpine
cd alpine && abuild -r && cd ..
echo "✓ Alpine package built: grain6-0.1.0-r0.apk"

# 2. Build NixOS package
echo ""
echo "2️⃣ Building for NixOS (nix)..."
nix-build
echo "✓ NixOS derivation built"

# 3. Test in NixOS VM
echo ""
echo "3️⃣ Testing in NixOS QEMU/KVM VM..."
virsh start nixos-grain6 || echo "VM already running"
ssh nixos@$(get-vm-ip nixos-grain6) "cd /mnt/kae3g/grainkae3g && nix-build && result/bin/grain6 --version"
echo "✓ Tested in NixOS VM"

# 4. Build Ubuntu/Debian package
echo ""
echo "4️⃣ Building for Ubuntu/Debian (deb)..."
lein deb
echo "✓ Debian package built: grain6_0.1.0_all.deb"

echo ""
echo "✨ Multi-distro build complete!"
echo "   Alpine (musl): grain6-0.1.0-r0.apk"
echo "   NixOS: result/bin/grain6"
echo "   Ubuntu/Debian: grain6_0.1.0_all.deb"
```

---

## 📊 Cursor Workspace Integration

### Option 1: 9p Filesystem (Real-time Sync)

**Advantages**:
- Instant file access
- No sync delay
- Edit in Cursor, run in NixOS immediately
- Perfect for active development

**NixOS sees changes**:
```bash
nixos@nixos-grain6:~$ cd /mnt/kae3g/grainkae3g
nixos@nixos-grain6:~/grainkae3g$ git status
# Shows your Cursor edits in real-time!
```

### Option 2: Git-based Workflow

**For deployments**:

```bash
# On Ubuntu (Cursor)
cursor ~/kae3g/grainkae3g
# ... make changes ...
git commit -am "New feature"

# In NixOS VM
cd /mnt/kae3g/grainkae3g
git pull
nix-build
```

### Option 3: Nix Flakes with direnv

**Most advanced**:

```nix
# /mnt/kae3g/grainkae3g/flake.nix
{
  description = "Grainkae3g development environment";
  
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-24.05";
  };
  
  outputs = { self, nixpkgs }: {
    devShells.x86_64-linux.default = 
      let pkgs = nixpkgs.legacyPackages.x86_64-linux;
      in pkgs.mkShell {
        buildInputs = with pkgs; [
          babashka
          leiningen
          s6
          s6-rc
        ];
        
        shellHook = ''
          echo "🌾 Grain6 development environment"
          echo "   Session: 812"
          echo "   Location: NixOS QEMU/KVM guest"
          echo "   Workspace: /mnt/kae3g/grainkae3g"
        '';
      };
  };
}
```

```bash
# In NixOS VM
cd /mnt/kae3g/grainkae3g
nix develop  # Enters development shell with all tools
```

---

## 🔧 VM Management Commands

### Start/Stop/Access

```bash
# List VMs
virsh list --all

# Start NixOS VM
virsh start nixos-grain6

# Get IP address
virsh domifaddr nixos-grain6

# SSH into VM
ssh nixos@$(virsh domifaddr nixos-grain6 | grep ipv4 | awk '{print $4}' | cut -d'/' -f1)

# Open virt-manager GUI
virt-manager

# Stop VM
virsh shutdown nixos-grain6

# Force stop
virsh destroy nixos-grain6
```

### Snapshot Management

```bash
# Create snapshot before risky changes
virsh snapshot-create-as nixos-grain6 \
  grain6-before-alpine-test \
  "Before testing Alpine package build"

# List snapshots
virsh snapshot-list nixos-grain6

# Restore snapshot
virsh snapshot-revert nixos-grain6 grain6-before-alpine-test

# Delete snapshot
virsh snapshot-delete nixos-grain6 grain6-before-alpine-test
```

---

## 🌊 Example Session 812 Workflow

### 1. Edit in Cursor (Ubuntu Host)

```bash
# Open Cursor
cursor ~/kae3g/grainkae3g

# Edit grain6/src/grain6/core.clj
# Add new feature for 88 counter philosophy integration
```

### 2. Test in NixOS VM

```bash
# SSH to NixOS VM
ssh nixos@nixos-grain6

# Changes are instantly available
cd /mnt/kae3g/grainkae3g/grainstore/grain6

# Run tests
lein test

# Build Nix derivation
nix-build

# Test binary
result/bin/grain6 --version
```

### 3. Build for Alpine (musl target)

```bash
# Still in NixOS VM
cd /mnt/kae3g/grainkae3g/grainstore/grain6

# Build Alpine-optimized uberjar
lein alpine

# Verify musl compatibility
file target/alpine/grain6-alpine-standalone.jar
```

### 4. Deploy (Back on Ubuntu Host)

```bash
# Exit NixOS VM
exit

# Back in Cursor workspace
cd ~/kae3g/grainkae3g

# Deploy with gb flow
gb flow "Session 812: Tested grain6 in NixOS VM, verified Alpine/musl compatibility"
```

---

## 📦 Multi-Distro Package Matrix

### Build Artifacts

| Distro | Package | Build Location | Test Location |
|--------|---------|----------------|---------------|
| **Alpine** | `grain6-0.1.0-r0.apk` | NixOS VM | Alpine container |
| **NixOS** | `result/bin/grain6` | NixOS VM | NixOS VM |
| **Ubuntu** | `grain6_0.1.0_all.deb` | Ubuntu host | Ubuntu host |

### CI/CD Pipeline

```yaml
# .github/workflows/multi-distro-build.yml
name: Multi-Distro Build Pipeline

on: [push, pull_request]

jobs:
  build-alpine:
    runs-on: ubuntu-latest
    container:
      image: alpine:latest
    steps:
      - uses: actions/checkout@v4
      - name: Build Alpine package
        run: |
          apk add --no-cache openjdk17 leiningen babashka s6 musl-dev
          lein alpine
          cd alpine && abuild -r
  
  build-nixos:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: cachix/install-nix-action@v24
      - name: Build Nix derivation
        run: nix-build
      - name: Test
        run: result/bin/grain6 --version
  
  build-ubuntu:
    runs-on: ubuntu-24.04
    steps:
      - uses: actions/checkout@v4
      - name: Build deb package
        run: |
          sudo apt-get update
          sudo apt-get install -y openjdk-17-jdk leiningen
          lein deb
```

---

## 🌾 88 Counter Philosophy in VMs

### Sheaf Scaling in Virtual Environments

```
Host Ubuntu 24.04 LTS: 88 × 10⁰ = 1 physical machine
├── VM 1 (NixOS): 88 × 10¹ = 10 virtual sheaves
├── VM 2 (Alpine): 88 × 10¹ = 10 virtual sheaves
└── VM N (grain6): 88 × 10¹ = 10 virtual sheaves

Total capacity: 88 × 10² = 8,800 possible service instances
```

Each VM can run multiple grain6 instances, creating **fractal supervision** across physical and virtual layers.

### Temporal Recursion

```
Session 811: Built grainkae3gcontract on Ubuntu
    ↓ (now == next + 1)
Session 812: Testing in NixOS VM with musl targeting
    ↓ (now == next + 1)
Session 813: Deploy to Alpine production
```

Each session contains all previous sessions while pointing to the next.

---

## 🐳 Docker Alternative (Alpine-based)

### Dockerfile

```dockerfile
FROM alpine:latest

# Install Grain6 dependencies
RUN apk add --no-cache \
    openjdk17 \
    babashka \
    s6 \
    s6-rc \
    musl-dev

# Copy grain6
COPY target/alpine/grain6-alpine-standalone.jar /usr/local/lib/grain6.jar
COPY bin/grain6 /usr/local/bin/grain6

# S6 supervision
COPY s6/ /etc/s6/sv/grain6/

# Expose ports (if needed)
EXPOSE 8080

# Run grain6
CMD ["s6-svscan", "/etc/s6/sv"]
```

### Build and Run

```bash
# Build Alpine-based Docker image
docker build -t grain6:alpine -f Dockerfile.alpine .

# Run with workspace mount
docker run -it \
  -v ~/kae3g/grainkae3g:/workspace \
  grain6:alpine \
  /bin/sh
```

---

## 📖 References

- [QEMU Documentation](https://www.qemu.org/docs/master/)
- [libvirt Documentation](https://libvirt.org/docs.html)
- [NixOS Manual](https://nixos.org/manual/nixos/stable/)
- [9p Filesystem](https://www.kernel.org/doc/Documentation/filesystems/9p.txt)
- [Alpine Package Building](https://wiki.alpinelinux.org/wiki/Creating_an_Alpine_package)
- [musl libc](https://musl.libc.org/)

---

**Built with 🌾 for sovereign multi-distro development**

*"Alpine first, NixOS for testing, Ubuntu for hosting"*

**Session 812**: NixOS in QEMU/KVM with Cursor workspace access via 9p virtfs
**Priority**: Alpine (musl) → NixOS (glibc+nix) → Ubuntu (glibc+deb)
**Philosophy**: `now == next + 1`, test next state without leaving current state
