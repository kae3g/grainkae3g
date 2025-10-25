# 🌾 GrainSource NixOS QEMU/KVM for Framework 16 Ubuntu 24.04 LTS

> **"NixOS 25.11 Unstable in QEMU/KVM - Test, Build, Deploy"**

Setting up NixOS virtualization environment for Grain Network development, with shared folder access to the Cursor workspace and grain6 Humble UI dual-wifi management.

---

## 📋 **Overview**

This guide sets up:
1. **QEMU/KVM** on Framework 16 Ubuntu 24.04 LTS
2. **NixOS 25.11 Unstable** as guest VM
3. **9p virtfs** for shared folder access to `/home/xy/kae3g/grainkae3g`
4. **grain6 + grainwifi** integration for dual-wifi management
5. **Humble UI** desktop environment

**Reference**: [NixOS Manual 25.11 Unstable](https://nixos.org/manual/nixos/unstable/)

---

## 🎯 **Architecture**

```
┌─────────────────────────────────────────────────────────────┐
│  Framework 16 Laptop (Hardware)                             │
│  - AMD Ryzen 9 7940HS                                       │
│  - 64GB RAM                                                 │
│  - 2TB NVMe SSD                                             │
│  - Dual WiFi: Starlink + Cellular                          │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────┴──────────────────────────────────────┐
│  Ubuntu 24.04 LTS (Host OS)                                 │
│  - GNOME Wayland                                            │
│  - Cursor IDE                                               │
│  - QEMU/KVM (libvirt)                                       │
│  - virsh management                                         │
│  - /home/xy/kae3g/grainkae3g (Cursor workspace)             │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────┴──────────────────────────────────────┐
│  NixOS 25.11 Unstable (Guest VM)                            │
│  - QEMU/KVM virtualized                                     │
│  - 16GB RAM allocated                                       │
│  - 100GB virtual disk                                       │
│  - Shared folder: /mnt/grainkae3g (9p virtfs)               │
│  - grain6 + grainwifi + Humble UI                           │
│  - Declarative configuration (configuration.nix)            │
└─────────────────────────────────────────────────────────────┘
```

---

## 📦 **Phase 1: Install QEMU/KVM on Ubuntu 24.04 LTS**

**Official Reference**: [Ubuntu Server 24.04 LTS - Virtualization with QEMU/KVM](https://ubuntu.com/server/docs/virtualization-qemu)

### **1.1 Verify Hardware Virtualization Support**

```bash
# Check CPU virtualization support
lscpu | grep Virtualization

# Expected output for AMD (Framework 16):
# Virtualization:      AMD-V

# Expected output for Intel:
# Virtualization:      VT-x

# Alternative check with kvm-ok (requires cpu-checker)
sudo apt install -y cpu-checker
kvm-ok
# Expected output: "KVM acceleration can be used"
```

**Note**: Framework 16 has AMD Ryzen 9 7940HS with AMD-V virtualization extensions.

If virtualization is not detected:
1. Reboot into BIOS/UEFI settings (usually F2 or Del at boot)
2. Enable "AMD-V" or "SVM Mode" (AMD) / "VT-x" (Intel)
3. Save and reboot

### **1.2 Install Required Packages**

Following [Ubuntu's official QEMU guide](https://ubuntu.com/server/docs/virtualization-qemu):

```bash
# Update package list
sudo apt update

# Install QEMU, KVM, libvirt and management tools
# (Official Ubuntu 24.04 LTS package list)
sudo apt install -y \
  qemu-kvm \
  libvirt-daemon-system \
  libvirt-clients \
  bridge-utils \
  virt-manager \
  ovmf \
  cpu-checker

# Verify installation
qemu-system-x86_64 --version
virsh --version
```

**Package Descriptions** (from Ubuntu documentation):
- `qemu-kvm`: QEMU full system emulation binaries (x86)
- `libvirt-daemon-system`: Libvirt daemon configuration files
- `libvirt-clients`: Programs for the libvirt library (virsh, virt-install)
- `bridge-utils`: Utilities for configuring the Linux Ethernet bridge
- `virt-manager`: Desktop application for managing virtual machines
- `ovmf`: UEFI firmware for virtual machines
- `cpu-checker`: Tools to check CPU capabilities (kvm-ok)

### **1.3 Add User to KVM and Libvirt Groups**

**Reference**: [Ubuntu Server Guide - User Group Management](https://ubuntu.com/server/docs/virtualization-qemu)

```bash
# Add your user to kvm and libvirt groups
# (Required for non-root VM management)
sudo usermod -aG kvm $USER
sudo usermod -aG libvirt $USER

# Verify group membership
groups $USER | grep -E 'kvm|libvirt'

# Log out and back in for group changes to take effect
# OR reboot to apply changes immediately
echo "⚠️  Log out and back in (or reboot) for group changes to take effect"
```

**Why These Groups?**
- `kvm`: Access to `/dev/kvm` device for hardware acceleration
- `libvirt`: Manage VMs via libvirt (virsh, virt-manager)

### **1.4 Enable and Start libvirt Service**

**Reference**: [Ubuntu systemd Service Management](https://ubuntu.com/server/docs/service-management)

```bash
# Enable libvirt daemon to start on boot
sudo systemctl enable libvirtd

# Start libvirt daemon immediately
sudo systemctl start libvirtd

# Verify service status
sudo systemctl status libvirtd
# Expected: "active (running)"

# Alternative: Enable and start in one command
sudo systemctl enable --now libvirtd
```

### **1.5 Verify Installation**

```bash
# Verify KVM modules are loaded
lsmod | grep kvm
# Expected output (AMD):
# kvm_amd
# kvm

# Expected output (Intel):
# kvm_intel
# kvm

# Test virsh connection
virsh list --all
# Expected: Empty list (no VMs yet)

# Verify default network
virsh net-list --all
# Expected: "default" network (usually active)

# If default network not active:
sudo virsh net-start default
sudo virsh net-autostart default
```

---

## 💿 **Phase 2: Download NixOS 25.11 Unstable ISO**

### **2.1 Download ISO**

```bash
# Create directory for ISOs
mkdir -p ~/VMs/ISOs

# Download NixOS 25.11 unstable minimal ISO
cd ~/VMs/ISOs
wget https://channels.nixos.org/nixos-unstable/latest-nixos-minimal-x86_64-linux.iso \
  -O nixos-25.11-unstable-minimal-x86_64.iso

# Or download graphical ISO (GNOME)
wget https://channels.nixos.org/nixos-unstable/latest-nixos-gnome-x86_64-linux.iso \
  -O nixos-25.11-unstable-gnome-x86_64.iso

# Verify download (check size)
ls -lh nixos-*.iso
```

**Note**: Unstable channel gets daily updates. This gets the latest unstable build.

### **2.2 Verify ISO Checksum (Optional but Recommended)**

```bash
# Download checksums
wget https://channels.nixos.org/nixos-unstable/latest-nixos-minimal-x86_64-linux.iso.sha256

# Verify
sha256sum -c nixos-25.11-unstable-minimal-x86_64.iso.sha256
```

---

## 🖥️ **Phase 3: Create NixOS VM with virt-manager**

### **3.1 Launch virt-manager**

```bash
# Launch graphical VM manager
virt-manager
```

**Or use command-line approach** (see Phase 4 for full CLI setup)

### **3.2 Create New Virtual Machine (GUI)**

1. **Click "Create a new virtual machine"**
2. **Choose installation method**: "Local install media (ISO image or CDROM)"
3. **Browse to ISO**: Select `~/VMs/ISOs/nixos-25.11-unstable-*.iso`
4. **Choose OS**: Type "NixOS" or "Generic Linux 2020"
5. **Memory and CPU**:
   - **RAM**: 16384 MB (16 GB) - adjust based on your needs
   - **CPUs**: 8 cores (half of Ryzen 9 7940HS)
6. **Storage**:
   - **Create disk image**: 100 GB
   - **Location**: `/var/lib/libvirt/images/nixos-grainkae3g.qcow2`
7. **Name and Network**:
   - **Name**: `nixos-grainkae3g`
   - **Network**: NAT (default)
8. **Before clicking "Finish"**:
   - ✅ Check "Customize configuration before install"
   - Click "Finish"

### **3.3 Customize VM Before First Boot**

**In the configuration screen**:

1. **Overview**:
   - **Firmware**: UEFI x86_64 (OVMF)
   
2. **CPUs**:
   - **Topology**: 4 sockets, 2 cores each (or 1 socket, 8 cores)
   - ✅ Enable "Copy host CPU configuration"

3. **Boot Options**:
   - ✅ Enable "Start virtual machine on host boot up" (optional)

4. **Add Hardware** → **Filesystem** (for shared folder):
   - **Driver**: virtiofs (or 9p if virtiofs unavailable)
   - **Source path**: `/home/xy/kae3g/grainkae3g`
   - **Target path**: `grainkae3g` (mount tag)
   - **Mode**: Mapped

5. **Video**:
   - **Model**: Virtio
   - **3D acceleration**: ✅ (if using graphical ISO)

6. **Click "Begin Installation"**

---

## ⚙️ **Phase 4: Install NixOS 25.11 in VM**

### **4.1 Boot from ISO and Start Installation**

The VM will boot into NixOS installer.

**For minimal ISO**: You'll get a command-line prompt.  
**For graphical ISO**: You'll get GNOME with installer GUI.

### **4.2 Manual Installation (Minimal ISO)**

**Reference**: [NixOS Manual - Installing NixOS](https://nixos.org/manual/nixos/unstable/#sec-installation)

#### **Partition the Disk**

```bash
# List disks
lsblk

# Partition with parted (UEFI)
sudo parted /dev/vda -- mklabel gpt
sudo parted /dev/vda -- mkpart ESP fat32 1MiB 512MiB
sudo parted /dev/vda -- set 1 esp on
sudo parted /dev/vda -- mkpart primary 512MiB 100%

# Format partitions
sudo mkfs.fat -F 32 -n boot /dev/vda1
sudo mkfs.ext4 -L nixos /dev/vda2

# Mount filesystems
sudo mount /dev/disk/by-label/nixos /mnt
sudo mkdir -p /mnt/boot
sudo mount /dev/disk/by-label/boot /mnt/boot
```

#### **Generate NixOS Configuration**

```bash
# Generate initial config
sudo nixos-generate-config --root /mnt

# This creates:
# /mnt/etc/nixos/configuration.nix
# /mnt/etc/nixos/hardware-configuration.nix
```

#### **Edit Configuration**

```bash
# Edit configuration
sudo nano /mnt/etc/nixos/configuration.nix
```

**Minimal working configuration**:

```nix
{ config, pkgs, ... }:

{
  imports = [ ./hardware-configuration.nix ];

  # Boot loader
  boot.loader.systemd-boot.enable = true;
  boot.loader.efi.canTouchEfiVariables = true;

  # Networking
  networking.hostName = "nixos-grainkae3g";
  networking.networkmanager.enable = true;

  # Timezone
  time.timeZone = "America/Chicago";  # Central Illinois

  # Locale
  i18n.defaultLocale = "en_US.UTF-8";

  # Users
  users.users.xy = {
    isNormalUser = true;
    extraGroups = [ "wheel" "networkmanager" ];
    initialPassword = "changeme";  # Change on first login
  };

  # Packages
  environment.systemPackages = with pkgs; [
    vim
    git
    wget
    curl
    htop
  ];

  # Enable SSH
  services.openssh.enable = true;

  # Shared folder (9p virtfs)
  fileSystems."/mnt/grainkae3g" = {
    device = "grainkae3g";
    fsType = "9p";
    options = [ "trans=virtio" "version=9p2000.L" "msize=104857600" ];
  };

  # This value determines NixOS release compatibility
  system.stateVersion = "25.11";
}
```

#### **Install NixOS**

```bash
# Install
sudo nixos-install

# Set root password when prompted

# Reboot
sudo reboot
```

### **4.3 Graphical Installation (GNOME ISO)**

1. **Launch installer** (should start automatically)
2. **Follow GUI prompts**:
   - Language: English
   - Timezone: America/Chicago
   - Keyboard: US
   - Partitioning: Automatic (or manual if preferred)
   - User: `xy`
   - Hostname: `nixos-grainkae3g`
3. **Install** and wait for completion
4. **Reboot**

**After reboot**, edit `/etc/nixos/configuration.nix` to add shared folder and grain6 packages.

---

## 📁 **Phase 5: Configure Shared Folder Access**

### **5.1 9p virtfs Configuration**

**On Host (Ubuntu)**:

Shared folder was configured in virt-manager (Phase 3.3).

**On Guest (NixOS)**:

Edit `/etc/nixos/configuration.nix`:

```nix
{
  # ... existing config ...

  # Shared folder from Ubuntu host
  fileSystems."/mnt/grainkae3g" = {
    device = "grainkae3g";  # Mount tag from virt-manager
    fsType = "9p";
    options = [
      "trans=virtio"
      "version=9p2000.L"
      "msize=104857600"  # 100MB message size for performance
      "cache=loose"      # Better performance (use with caution)
    ];
  };

  # Create mount point on boot
  system.activationScripts.mkGrainkaeMnt = ''
    mkdir -p /mnt/grainkae3g
  '';
}
```

**Rebuild NixOS**:

```bash
sudo nixos-rebuild switch
```

**Verify shared folder**:

```bash
ls -la /mnt/grainkae3g
# Should show contents of /home/xy/kae3g/grainkae3g from host
```

### **5.2 Alternative: virtiofs (if available)**

virtiofs is faster than 9p but requires newer QEMU/libvirt.

**In virt-manager**:
- Filesystem driver: **virtiofs** (instead of 9p)

**In NixOS config**:

```nix
fileSystems."/mnt/grainkae3g" = {
  device = "grainkae3g";
  fsType = "virtiofs";
  options = [ "rw" ];
};
```

---

## 🌾 **Phase 6: Install Grain Network in NixOS VM**

### **6.1 Install Babashka and Clojure**

Edit `/etc/nixos/configuration.nix`:

```nix
{ config, pkgs, ... }:

{
  # ... existing config ...

  environment.systemPackages = with pkgs; [
    # Essential tools
    vim git wget curl htop

    # Grain Network dependencies
    babashka        # Fast Clojure scripting
    clojure         # Clojure CLI
    leiningen       # Clojure project management
    jdk17           # Java for Clojure

    # Development tools
    direnv          # Environment management
    nix-direnv      # Nix + direnv integration

    # QEMU guest tools
    qemu_kvm
    spice-vdagent   # Better clipboard/display integration
  ];

  # Enable direnv integration
  programs.direnv.enable = true;

  # ... rest of config ...
}
```

**Rebuild**:

```bash
sudo nixos-rebuild switch
```

### **6.2 Access Grain Network from Shared Folder**

```bash
# Navigate to shared workspace
cd /mnt/grainkae3g

# Test Babashka
bb --version

# Run grain commands
bb qb-kk
bb gt
```

---

## 🛰️ **Phase 7: grain6 + grainwifi Humble UI in NixOS**

### **7.1 Install Humble UI Dependencies**

```nix
{ config, pkgs, ... }:

{
  environment.systemPackages = with pkgs; [
    # ... existing packages ...

    # Humble UI dependencies
    jdk17              # Java for HumbleUI
    clojure
    babashka

    # Network management
    networkmanager
    networkmanagerapplet

    # Desktop environment (if not already installed)
    gnome.gnome-shell
    gnome.gnome-terminal
  ];

  # Enable NetworkManager
  networking.networkmanager.enable = true;

  # Enable X11 or Wayland
  services.xserver = {
    enable = true;
    displayManager.gdm.enable = true;
    desktopManager.gnome.enable = true;
  };

  # Allow xy user to manage network
  users.users.xy.extraGroups = [ "networkmanager" ];
}
```

### **7.2 Run grain6 + grainwifi from Shared Folder**

```bash
# In NixOS VM
cd /mnt/grainkae3g/grainstore/grainwifi

# Run Humble UI
bb humble-ui:start

# Or with grain6 integration
cd /mnt/grainkae3g/grainstore/grain6
bb grain6:grainwifi-humble
```

---

## 🔧 **Phase 8: Declarative NixOS Configuration for Grain Network**

### **8.1 Complete NixOS Configuration**

Create `/etc/nixos/grainkae3g.nix`:

```nix
# 🌾 GrainKae3g NixOS Configuration
# NixOS 25.11 Unstable - Grain Network Development Environment

{ config, pkgs, ... }:

{
  imports = [ ./hardware-configuration.nix ];

  # ═══════════════════════════════════════════════════════════
  # BOOT LOADER
  # ═══════════════════════════════════════════════════════════
  
  boot.loader.systemd-boot.enable = true;
  boot.loader.efi.canTouchEfiVariables = true;

  # ═══════════════════════════════════════════════════════════
  # NETWORKING
  # ═══════════════════════════════════════════════════════════
  
  networking.hostName = "nixos-grainkae3g";
  networking.networkmanager.enable = true;
  
  # Open ports for development
  networking.firewall.allowedTCPPorts = [ 22 3000 8000 8080 ];
  networking.firewall.enable = true;

  # ═══════════════════════════════════════════════════════════
  # LOCALIZATION
  # ═══════════════════════════════════════════════════════════
  
  time.timeZone = "America/Chicago";  # Forest cabin, Central Illinois
  i18n.defaultLocale = "en_US.UTF-8";
  
  console = {
    font = "Lat2-Terminus16";
    keyMap = "us";
  };

  # ═══════════════════════════════════════════════════════════
  # USERS
  # ═══════════════════════════════════════════════════════════
  
  users.users.xy = {
    isNormalUser = true;
    description = "kae3g";
    extraGroups = [ "wheel" "networkmanager" "libvirtd" "docker" ];
    shell = pkgs.zsh;
    openssh.authorizedKeys.keys = [
      # Add your SSH public key here
    ];
  };

  # ═══════════════════════════════════════════════════════════
  # PACKAGES
  # ═══════════════════════════════════════════════════════════
  
  environment.systemPackages = with pkgs; [
    # Core utilities
    vim neovim git wget curl htop tree file ripgrep fd

    # Grain Network - Clojure ecosystem
    babashka clojure leiningen jdk17

    # Grain Network - Build tools
    nodejs_20 python3 gcc gnumake

    # Development
    direnv nix-direnv tmux

    # Network tools
    networkmanager networkmanagerapplet
    bind inetutils mtr

    # QEMU guest agent
    qemu_kvm spice-vdagent

    # Humble UI dependencies
    mesa libGL libGLU
  ];

  # ═══════════════════════════════════════════════════════════
  # SERVICES
  # ═══════════════════════════════════════════════════════════
  
  services.openssh = {
    enable = true;
    settings.PermitRootLogin = "no";
    settings.PasswordAuthentication = false;
  };

  services.qemuGuest.enable = true;

  # ═══════════════════════════════════════════════════════════
  # DESKTOP ENVIRONMENT
  # ═══════════════════════════════════════════════════════════
  
  services.xserver = {
    enable = true;
    displayManager.gdm.enable = true;
    desktopManager.gnome.enable = true;
  };

  # ═══════════════════════════════════════════════════════════
  # SHELL
  # ═══════════════════════════════════════════════════════════
  
  programs.zsh.enable = true;
  programs.direnv.enable = true;

  # ═══════════════════════════════════════════════════════════
  # SHARED FOLDER (9p virtfs from Ubuntu host)
  # ═══════════════════════════════════════════════════════════
  
  fileSystems."/mnt/grainkae3g" = {
    device = "grainkae3g";
    fsType = "9p";
    options = [
      "trans=virtio"
      "version=9p2000.L"
      "msize=104857600"
      "cache=loose"
    ];
  };

  system.activationScripts.mkGrainkaeMnt = ''
    mkdir -p /mnt/grainkae3g
  '';

  # ═══════════════════════════════════════════════════════════
  # NIXOS VERSION
  # ═══════════════════════════════════════════════════════════
  
  system.stateVersion = "25.11";  # NixOS 25.11 Unstable
}
```

**Import in `configuration.nix`**:

```nix
{ config, pkgs, ... }:

{
  imports = [ ./grainkae3g.nix ];
}
```

---

## 📊 **Phase 9: Performance Optimization**

### **9.1 VM Performance Tuning**

**CPU Pinning** (allocate specific cores):

```bash
# Edit VM XML
virsh edit nixos-grainkae3g
```

Add inside `<domain>`:

```xml
<vcpu placement='static' cpuset='4-11'>8</vcpu>
<cputune>
  <vcpupin vcpu='0' cpuset='4'/>
  <vcpupin vcpu='1' cpuset='5'/>
  <vcpupin vcpu='2' cpuset='6'/>
  <vcpupin vcpu='3' cpuset='7'/>
  <vcpupin vcpu='4' cpuset='8'/>
  <vcpupin vcpu='5' cpuset='9'/>
  <vcpupin vcpu='6' cpuset='10'/>
  <vcpupin vcpu='7' cpuset='11'/>
</cputune>
```

**Huge Pages** (better memory performance):

```bash
# On Ubuntu host
sudo sysctl -w vm.nr_hugepages=2048

# Make permanent
echo "vm.nr_hugepages = 2048" | sudo tee -a /etc/sysctl.conf
```

### **9.2 Disk I/O Optimization**

```bash
# Convert qcow2 to preallocated (faster but larger)
qemu-img convert -O qcow2 -o preallocation=full \
  /var/lib/libvirt/images/nixos-grainkae3g.qcow2 \
  /var/lib/libvirt/images/nixos-grainkae3g-fast.qcow2
```

---

## 🌲 **Phase 10: Forest Cabin Dual-WiFi Setup**

### **10.1 Network Passthrough to VM**

**Option 1: Bridged Network** (VM gets own IP):

```bash
# Create bridge on Ubuntu host
sudo nmcli connection add type bridge ifname br0 con-name br0

# Add Starlink connection to bridge
sudo nmcli connection modify Starlink master br0

# In virt-manager: Change network to br0
```

**Option 2: NAT with Port Forwarding** (simpler):

VM uses NAT, access from host via forwarded ports.

### **10.2 Run grainwifi in NixOS VM**

```bash
# In NixOS VM
cd /mnt/grainkae3g/grainstore/grainwifi

# Configure connections (edit config)
vim config/grainwifi.edn

# Start monitoring
bb grainwifi:start

# Launch Humble UI
bb humble-ui:start
```

---

## 🎯 **Quick Start Commands**

### **Start NixOS VM**

```bash
# From Ubuntu host
virsh start nixos-grainkae3g

# Or with virt-manager
virt-manager
```

### **Connect to VM**

```bash
# SSH (if configured)
ssh xy@192.168.122.X  # Find IP with: virsh domifaddr nixos-grainkae3g

# Or use virt-manager console
```

### **Access Grain Network**

```bash
# Inside NixOS VM
cd /mnt/grainkae3g

# Run grain commands
bb qb-kk
bb gt
bb kg
bb fr

# Start grain6 + grainwifi
cd grainstore/grain6
bb grain6:grainwifi-humble
```

---

## 📚 **Resources**

### **Official Ubuntu 24.04 LTS Documentation**
- **Ubuntu Server Guide - Virtualization**: https://ubuntu.com/server/docs/virtualization-introduction
- **Ubuntu - QEMU/KVM Guide**: https://ubuntu.com/server/docs/virtualization-qemu
- **Ubuntu - libvirt**: https://ubuntu.com/server/docs/virtualization-libvirt
- **Ubuntu - virt-manager**: https://ubuntu.com/server/docs/virtualization-virt-tools
- **Ubuntu - Service Management**: https://ubuntu.com/server/docs/service-management

### **NixOS Documentation**
- **NixOS Manual 25.11 Unstable**: https://nixos.org/manual/nixos/unstable/
- **NixOS Installation Guide**: https://nixos.org/manual/nixos/unstable/#sec-installation
- **NixOS Configuration**: https://nixos.org/manual/nixos/unstable/#ch-configuration
- **NixOS Search Packages**: https://search.nixos.org/packages
- **NixOS Search Options**: https://search.nixos.org/options

### **Virtualization Technology**
- **libvirt Official Documentation**: https://libvirt.org/docs.html
- **QEMU Documentation**: https://www.qemu.org/documentation/
- **KVM (Kernel Virtual Machine)**: https://www.linux-kvm.org/
- **9p virtfs (Plan 9 Filesystem)**: https://www.linux-kvm.org/page/9p_virtio
- **virtiofs**: https://virtio-fs.gitlab.io/

### **Framework 16 Specific**
- **Framework Community - Linux**: https://community.frame.work/c/framework-laptop-16/linux-on-framework-laptop-16/
- **Framework - Ubuntu 24.04 LTS Guide**: https://frame.work/ubuntu
- **AMD-V Virtualization**: https://www.amd.com/en/technologies/virtualization

---

## 🌾 **Philosophy**

**"Local Control, Global Intent"**
- **Local**: NixOS VM runs locally, full control
- **Global**: Shared folder connects to Cursor workspace
- **Synthesis**: Test NixOS configs while maintaining Ubuntu workflow

**"From Granules to Grains to THE WHOLE GRAIN"**
- **Granules**: Individual NixOS packages
- **Grains**: Complete VM configuration
- **THE WHOLE GRAIN**: Grain Network in NixOS + Ubuntu + Dual-WiFi

---

**Status**: 🚧 Ready for Implementation  
**Target**: NixOS 25.11 Unstable in QEMU/KVM  
**Integration**: grain6 + grainwifi + Humble UI  

🌾 **now == next + 1** (but make it declarative, chief!) 📦
