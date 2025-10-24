# 🔄 Nested Virtualization Guide
## *"Ubuntu → NixOS+Sway → Debian - Virtualization Inception"*

**Course Module:** Week 13 - Advanced Virtualization  
**Difficulty:** Advanced  
**Time Required:** 3-4 hours  
**Prerequisites:** Debian QEMU setup complete, KVM acceleration working  
**AI Mode:** Cursor Agentic Autocomplete with Auto Model (All Models)

---

## 🎯 What We're Building

```
┌─────────────────────────────────────────────────────┐
│  Layer 1: Ubuntu 24.04 LTS (Host)                  │
│  Framework Laptop 16, GNOME                         │
│                                                     │
│  ┌───────────────────────────────────────────────┐ │
│  │  Layer 2: NixOS + Sway (Guest VM)            │ │
│  │  QEMU/KVM, Wayland compositor                │ │
│  │                                               │ │
│  │  ┌─────────────────────────────────────────┐ │ │
│  │  │  Layer 3: Debian Stable (Nested VM)    │ │ │
│  │  │  Package testing environment           │ │ │
│  │  │  Grainspace deployment                 │ │ │
│  │  └─────────────────────────────────────────┘ │ │
│  └───────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────┘
```

**Why This Setup?**

1. **Ubuntu 24.04 LTS** - Stable host with good hardware support
2. **NixOS + Sway** - Test declarative configs and tiling WM
3. **Debian Stable** - Production-like deployment environment
4. **Nested KVM** - Each layer can run virtual machines

---

## 🧪 Part 1: Enable Nested Virtualization

### Check if Nested Virtualization is Supported

```bash
# Check if nested virtualization is enabled
cat /sys/module/kvm_amd/parameters/nested
# OR for Intel:
cat /sys/module/kvm_intel/parameters/nested

# Should output: Y (yes) or 1 (enabled)
# If N or 0, we need to enable it
```

### Enable Nested Virtualization

**For AMD CPUs:**

```bash
# Check current setting
cat /sys/module/kvm_amd/parameters/nested

# Enable nested virtualization (temporary)
sudo modprobe -r kvm_amd
sudo modprobe kvm_amd nested=1

# Make permanent
echo "options kvm_amd nested=1" | sudo tee /etc/modprobe.d/kvm-nested.conf

# Reboot to apply
sudo reboot
```

**For Intel CPUs:**

```bash
# Check current setting
cat /sys/module/kvm_intel/parameters/nested

# Enable nested virtualization (temporary)
sudo modprobe -r kvm_intel
sudo modprobe kvm_intel nested=1

# Make permanent
echo "options kvm_intel nested=1" | sudo tee /etc/modprobe.d/kvm-nested.conf

# Reboot to apply
sudo reboot
```

### Verify Nested Virtualization

```bash
# After reboot, check again
cat /sys/module/kvm_amd/parameters/nested  # or kvm_intel
# Should output: Y or 1

# Check QEMU capabilities
qemu-system-x86_64 -cpu help | grep -i vmx  # Intel
qemu-system-x86_64 -cpu help | grep -i svm  # AMD
```

---

## 💾 Part 2: Set Up NixOS VM (Layer 2)

### Download NixOS ISO

```bash
# Create directory for NixOS
mkdir -p ~/VMs/nixos
cd ~/VMs/nixos

# Download NixOS 23.11 Plasma ISO (with GUI for easier setup)
wget https://channels.nixos.org/nixos-23.11/latest-nixos-plasma5-x86_64-linux.iso

# Or minimal ISO (no GUI during install)
# wget https://channels.nixos.org/nixos-23.11/latest-nixos-minimal-x86_64-linux.iso

# Verify download
ls -lh latest-nixos-*.iso
```

### Create Virtual Disk for NixOS

```bash
# Create 40GB disk for NixOS (needs space for nested Debian VM)
qemu-img create -f qcow2 ~/VMs/disks/nixos-sway.qcow2 40G

# Verify
qemu-img info ~/VMs/disks/nixos-sway.qcow2
```

### Install NixOS

**Start installation:**

```bash
# Install NixOS with nested virtualization support
qemu-system-x86_64 \
  -name "NixOS Sway" \
  -machine type=q35,accel=kvm \
  -cpu host,+vmx,+svm \
  -smp 4 \
  -m 8192 \
  -cdrom ~/VMs/nixos/latest-nixos-plasma5-x86_64-linux.iso \
  -drive file=~/VMs/disks/nixos-sway.qcow2,format=qcow2,if=virtio \
  -boot d \
  -netdev user,id=net0,hostfwd=tcp::2223-:22 \
  -device virtio-net-pci,netdev=net0 \
  -display gtk \
  -vga virtio \
  -enable-kvm
```

**Important flags for nested virtualization:**
- `-cpu host,+vmx,+svm` - Expose virtualization features to guest
- `-enable-kvm` - Use KVM acceleration
- `-smp 4` - 4 CPU cores (guest can use for nested VMs)
- `-m 8192` - 8GB RAM (enough for NixOS + nested Debian)

### NixOS Installation Steps

1. **Boot from ISO**
2. **Select "Install NixOS"**
3. **Partitioning**: Use entire disk, automatic
4. **Desktop**: Select "Sway" (or install later)
5. **User**: Create user `nixos`
6. **Install** and wait
7. **Reboot**

### Post-Installation: Configure Sway

**After first boot, create NixOS configuration:**

```bash
# SSH into NixOS VM
ssh -p 2223 nixos@localhost

# Edit configuration
sudo nano /etc/nixos/configuration.nix
```

**Add Sway and necessary packages:**

```nix
{ config, pkgs, ... }:

{
  imports = [ ./hardware-configuration.nix ];

  # Bootloader
  boot.loader.systemd-boot.enable = true;
  boot.loader.efi.canMountIfNeeded = true;

  # Networking
  networking.hostName = "nixos-sway";
  networking.networkmanager.enable = true;

  # Enable nested virtualization
  boot.kernelModules = [ "kvm-amd" "kvm-intel" ];
  virtualisation.libvirtd.enable = true;

  # Users
  users.users.nixos = {
    isNormalUser = true;
    extraGroups = [ "wheel" "networkmanager" "libvirtd" "kvm" ];
  };

  # Sway and Wayland
  programs.sway = {
    enable = true;
    wrapperFeatures.gtk = true;
    extraPackages = with pkgs; [
      swaylock
      swayidle
      wl-clipboard
      mako
      alacritty
      dmenu
      waybar
      grim
      slurp
      wf-recorder
    ];
  };

  # Enable QEMU/KVM
  virtualisation.qemu = {
    enable = true;
    package = pkgs.qemu_kvm;
  };

  # Install virtualization tools
  environment.systemPackages = with pkgs; [
    qemu_kvm
    qemu-utils
    virt-manager
    libvirt
    bridge-utils
    git
    vim
    wget
    curl
    htop
  ];

  # Enable SSH
  services.openssh.enable = true;

  # This value determines the NixOS release from which the default
  # settings for stateful data, like file locations and database versions
  # on your system were taken. It's perfectly fine and recommended to leave
  # this value at the release version of the first install of this system.
  system.stateVersion = "23.11";
}
```

**Apply configuration:**

```bash
# Rebuild NixOS
sudo nixos-rebuild switch

# Reboot
sudo reboot
```

---

## 🖥️ Part 3: Start NixOS with Sway

### Create NixOS Launcher Script

**On Ubuntu host, create script:**

```bash
cat > ~/VMs/start-nixos-sway.sh << 'EOF'
#!/bin/bash
# NixOS + Sway QEMU VM Launcher
# Part of Grain Network High School Course

VM_NAME="NixOS Sway"
VM_DISK="$HOME/VMs/disks/nixos-sway.qcow2"
VM_RAM="8192"
VM_CPUS="4"
SSH_PORT="2223"

echo "🌾 Starting NixOS + Sway VM..."
echo "VM Name: $VM_NAME"
echo "RAM: ${VM_RAM}MB"
echo "CPUs: $VM_CPUS"
echo "SSH: ssh -p $SSH_PORT nixos@localhost"
echo ""
echo "⚠️  Nested virtualization enabled - this VM can run QEMU!"
echo ""

qemu-system-x86_64 \
  -name "$VM_NAME" \
  -machine type=q35,accel=kvm \
  -cpu host,+vmx,+svm \
  -smp $VM_CPUS \
  -m $VM_RAM \
  -drive file=$VM_DISK,format=qcow2,if=virtio \
  -boot c \
  -netdev user,id=net0,hostfwd=tcp::${SSH_PORT}-:22 \
  -device virtio-net-pci,netdev=net0 \
  -display gtk \
  -vga virtio \
  -enable-kvm
EOF

chmod +x ~/VMs/start-nixos-sway.sh
```

**Run it:**

```bash
~/VMs/start-nixos-sway.sh
```

### Access Sway Desktop

**In the QEMU window:**

1. Login with your `nixos` user
2. Sway should auto-start
3. Press `Super + Enter` to open terminal
4. You're now in Sway on NixOS!

---

## 🐧 Part 4: Set Up Debian VM Inside NixOS (Layer 3)

### Inside NixOS VM

**SSH into NixOS:**

```bash
ssh -p 2223 nixos@localhost
```

**Create directories:**

```bash
mkdir -p ~/VMs/iso
mkdir -p ~/VMs/disks
```

**Download Debian ISO (inside NixOS):**

```bash
cd ~/VMs/iso

# Download Debian netinst
wget https://cdimage.debian.org/debian-cd/current/amd64/iso-cd/debian-12.4.0-amd64-netinst.iso
```

**Create Debian disk:**

```bash
qemu-img create -f qcow2 ~/VMs/disks/debian-deploy.qcow2 20G
```

### Install Debian (Inside NixOS)

**Run QEMU inside NixOS:**

```bash
qemu-system-x86_64 \
  -name "Debian Deploy Test" \
  -machine type=q35,accel=kvm \
  -cpu host \
  -smp 2 \
  -m 2048 \
  -cdrom ~/VMs/iso/debian-12.4.0-amd64-netinst.iso \
  -drive file=~/VMs/disks/debian-deploy.qcow2,format=qcow2,if=virtio \
  -boot d \
  -netdev user,id=net0,hostfwd=tcp::2224-:22 \
  -device virtio-net-pci,netdev=net0 \
  -display gtk \
  -vga virtio \
  -enable-kvm
```

**Install Debian:**
- Follow same steps as previous Debian install guide
- Hostname: `debian-deploy`
- User: `deployer`
- Password: `graintest`
- Install SSH server

### After Debian Installation

**Start Debian VM (from within NixOS):**

```bash
qemu-system-x86_64 \
  -name "Debian Deploy Test" \
  -machine type=q35,accel=kvm \
  -cpu host \
  -smp 2 \
  -m 2048 \
  -drive file=~/VMs/disks/debian-deploy.qcow2,format=qcow2,if=virtio \
  -boot c \
  -netdev user,id=net0,hostfwd=tcp::2224-:22 \
  -device virtio-net-pci,netdev=net0 \
  -nographic \
  -enable-kvm
```

---

## 🔗 Part 5: Access All Layers

### Port Forwarding Chain

```
Ubuntu Host → NixOS VM → Debian VM

Ubuntu:      localhost
  ↓ port 2223 (SSH to NixOS)
NixOS:       localhost  
  ↓ port 2224 (SSH to Debian - forwarded through NixOS)
Debian:      localhost
```

### Access Each Layer

**From Ubuntu Host:**

```bash
# Access NixOS (Layer 2)
ssh -p 2223 nixos@localhost

# Access Debian through NixOS (Layer 3)
# First SSH to NixOS, then SSH to Debian
ssh -p 2223 nixos@localhost
# Then inside NixOS:
ssh -p 2224 deployer@localhost
```

**Using SSH Tunneling (Advanced):**

```bash
# Access Debian directly from Ubuntu host through NixOS tunnel
ssh -J nixos@localhost:2223 deployer@localhost:2224
```

---

## 🤖 Part 6: Cursor-Assisted Automation

### Create Master VM Manager with AI

**Open Cursor, create `~/VMs/nested-vm-manager.bb`:**

**Prompt Cursor AI (Cmd+K):**
```
Create a Babashka script to manage nested VMs:
- start-ubuntu (host operations)
- start-nixos (layer 2)
- start-debian (layer 3 inside nixos)
- ssh-nixos (connect to layer 2)
- ssh-debian (connect to layer 3)
- status (show all running VMs)
- stop-all (graceful shutdown)

Use clojure.java.shell for commands.
```

**Let AI generate the script, then review:**

```clojure
#!/usr/bin/env bb
;; Nested VM Manager
;; Ubuntu → NixOS+Sway → Debian

(require '[clojure.java.shell :as shell]
         '[clojure.string :as str])

(def vm-config
  {:ubuntu {:name "Ubuntu 24.04 LTS (Host)"
            :role :host}
   
   :nixos {:name "NixOS + Sway"
           :disk (str (System/getenv "HOME") "/VMs/disks/nixos-sway.qcow2")
           :ram "8192"
           :cpus "4"
           :ssh-port "2223"
           :user "nixos"}
   
   :debian {:name "Debian Deploy Test"
            :disk "~/VMs/disks/debian-deploy.qcow2"  ; Relative to NixOS
            :ram "2048"
            :cpus "2"
            :ssh-port "2224"
            :user "deployer"
            :parent :nixos}})

(defn start-nixos
  "Start NixOS VM from Ubuntu host"
  []
  (println "🌾 Starting NixOS + Sway VM...")
  (let [config (:nixos vm-config)]
    (shell/sh "sh" "-c"
              (str "qemu-system-x86_64"
                   " -name \"" (:name config) "\""
                   " -machine type=q35,accel=kvm"
                   " -cpu host,+vmx,+svm"
                   " -smp " (:cpus config)
                   " -m " (:ram config)
                   " -drive file=" (:disk config) ",format=qcow2,if=virtio"
                   " -boot c"
                   " -netdev user,id=net0,hostfwd=tcp::" (:ssh-port config) "-:22"
                   " -device virtio-net-pci,netdev=net0"
                   " -display gtk"
                   " -vga virtio"
                   " -enable-kvm"
                   " &"))))

(defn ssh-nixos
  "SSH into NixOS VM"
  []
  (let [config (:nixos vm-config)]
    (shell/sh "ssh" "-p" (:ssh-port config)
              (str (:user config) "@localhost"))))

(defn start-debian-in-nixos
  "Start Debian VM inside NixOS"
  []
  (println "🐧 Starting Debian VM inside NixOS...")
  (println "Note: This runs INSIDE the NixOS VM")
  (let [config (:debian vm-config)]
    (println "SSH to NixOS first, then run:")
    (println (str "  qemu-system-x86_64 -name \"" (:name config) "\""
                  " -machine type=q35,accel=kvm -cpu host"
                  " -smp " (:cpus config) " -m " (:ram config)
                  " -drive file=" (:disk config) ",format=qcow2,if=virtio"
                  " -boot c -netdev user,id=net0,hostfwd=tcp::" (:ssh-port config) "-:22"
                  " -device virtio-net-pci,netdev=net0 -nographic -enable-kvm"))))

(defn ssh-debian
  "SSH into Debian VM (through NixOS)"
  []
  (let [nixos-config (:nixos vm-config)
        debian-config (:debian vm-config)]
    (println "🔗 Connecting to Debian through NixOS...")
    (println "Step 1: SSH to NixOS")
    (println (str "  ssh -p " (:ssh-port nixos-config) " " (:user nixos-config) "@localhost"))
    (println "Step 2: Then SSH to Debian")
    (println (str "  ssh -p " (:ssh-port debian-config) " " (:user debian-config) "@localhost"))))

(defn show-status
  "Show status of all VMs"
  []
  (println "📊 Nested VM Status")
  (println "==================")
  (println "Layer 1 (Host):     Ubuntu 24.04 LTS")
  (println "Layer 2 (Guest):    NixOS + Sway")
  (println "  SSH:             ssh -p 2223 nixos@localhost")
  (println "Layer 3 (Nested):   Debian Deploy Test")
  (println "  SSH (from NixOS): ssh -p 2224 deployer@localhost")
  (println ""))

(defn show-help
  []
  (println "🌾 Nested VM Manager")
  (println "")
  (println "Commands:")
  (println "  start-nixos       Start NixOS VM (Layer 2)")
  (println "  ssh-nixos         SSH into NixOS")
  (println "  start-debian      Instructions for Debian (Layer 3)")
  (println "  ssh-debian        SSH into Debian")
  (println "  status            Show VM status")
  (println "  help              Show this message"))

(defn -main
  [& args]
  (case (first args)
    "start-nixos" (start-nixos)
    "ssh-nixos" (ssh-nixos)
    "start-debian" (start-debian-in-nixos)
    "ssh-debian" (ssh-debian)
    "status" (show-status)
    "help" (show-help)
    (show-help)))

(-main *command-line-args*)
```

**Make executable:**

```bash
chmod +x ~/VMs/nested-vm-manager.bb
```

---

## 🧪 Part 7: Test Package Deployment Across All Layers

### Deploy Grainspace to Nested Debian

**Goal:** Build and deploy Grainspace package in the innermost Debian VM.

**Step 1: Copy files to Debian (from Ubuntu host)**

```bash
# First, copy to NixOS
scp -P 2223 -r ~/grainkae3g/grainstore/grainspace/debian/* \
  nixos@localhost:/tmp/

# Then from NixOS, copy to Debian
ssh -p 2223 nixos@localhost
scp -P 2224 /tmp/debian/* deployer@localhost:/tmp/
```

**Step 2: Build in Debian**

```bash
# SSH to Debian (through NixOS)
ssh -p 2223 nixos@localhost
ssh -p 2224 deployer@localhost

# Inside Debian
cd /tmp
sudo apt update
sudo apt install -y debhelper-compat clojure openjdk-17-jdk

# Test package installation
echo "Package ready for deployment!"
```

---

## 📊 Part 8: Performance Considerations

### Resource Allocation

```
Total System: 32GB RAM, 8 CPU cores

Ubuntu Host:
  Reserved: 8GB RAM, 2 cores
  
NixOS VM:
  Allocated: 8GB RAM, 4 cores
  Reserved for self: 4GB RAM, 2 cores
  Available for Debian: 4GB RAM, 2 cores
  
Debian VM:
  Allocated: 2GB RAM, 2 cores
```

### Optimize Performance

**1. Use virtio for all devices**
```bash
-drive if=virtio
-device virtio-net-pci
-vga virtio
```

**2. Enable KVM at each layer**
```bash
-enable-kvm
-cpu host,+vmx,+svm
```

**3. Allocate enough RAM**
- Host: 8GB minimum
- NixOS: 8GB (4GB for itself, 4GB for nested VMs)
- Debian: 2GB minimum

**4. Use headless when possible**
```bash
-nographic  # For Debian (no GUI needed)
-display gtk  # For NixOS (Sway needs display)
```

---

## ✅ Verification Checklist

**Verify nested virtualization works:**

```bash
# On Ubuntu host
cat /sys/module/kvm_*/parameters/nested
# Should show: Y or 1

# Inside NixOS VM
ssh -p 2223 nixos@localhost
lsmod | grep kvm
# Should show kvm modules loaded

# Inside Debian VM (from NixOS)
ssh -p 2224 deployer@localhost
# Should be able to login
```

---

## 🎓 Learning Exercises

### Exercise 1: Test Sway in NixOS

1. Start NixOS VM
2. Login to Sway desktop
3. Open terminal (`Super + Enter`)
4. Take screenshot (`grim`)
5. Configure Waybar

### Exercise 2: Deploy Package in Nested Debian

1. Start all VMs
2. Copy Grainspace files to Debian
3. Build Debian package
4. Install and test
5. Create snapshot

### Exercise 3: Network Between VMs

1. Configure bridged networking
2. Ping between VMs
3. HTTP server in Debian, access from NixOS
4. Test firewall rules

---

## 🌟 Why This Matters

**Real-world scenarios:**

1. **Development**: Test on multiple distros without bare metal
2. **CI/CD**: Automated testing across platforms
3. **Education**: Learn different Linux flavors
4. **Security**: Isolated environments for testing
5. **Deployment**: Simulate production before deploying

**You now understand:**
- ✅ Nested virtualization
- ✅ NixOS declarative configuration
- ✅ Sway tiling window manager
- ✅ Multi-layer networking
- ✅ Port forwarding chains
- ✅ Cross-distribution package testing

---

## 🚀 Next Steps

1. **Add more distros**: Alpine, Arch, Fedora
2. **Automate with Nix**: Declarative VM configs
3. **Container testing**: Docker inside VMs
4. **Network simulation**: Multiple VMs communicating
5. **SixOS testing**: Once SixOS is ready

---

**Nested Virtualization Guide**  
*Part of Grain Network High School Course*

**Module:** Week 13 - Advanced Virtualization  
**Author:** kae3g (Graingalaxy)  
**Organization:** Grain PBC

*"Turtles all the way down - virtualization inception"* 🐢


