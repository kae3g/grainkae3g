# 🌾 NixOS Installation Guide with Home Manager & Flakes

> **"Inside the VM: Install NixOS 25.11 Unstable with grain6 + grainwifi"**

Complete step-by-step guide for installing NixOS in the VM we just created.

---

## 📋 **VM Status**

✅ **VM Created**: `nixos-grainkae3g`  
✅ **RAM**: 32 GB  
✅ **CPUs**: 8 cores  
✅ **Disk**: 100 GB  
✅ **ISO**: NixOS 25.11 Unstable Minimal  
✅ **Shared Folder**: `/home/xy/kae3g/grainkae3g` → `grainkae3g` mount tag  

---

## 🖥️ **Phase 1: Access VM Console**

### **Option 1: virt-manager (GUI)**

```bash
# Launch virt-manager
virt-manager

# Double-click "nixos-grainkae3g" to open console
```

### **Option 2: virsh console (CLI)**

```bash
sudo virsh console nixos-grainkae3g

# Exit with: Ctrl-]
```

---

## 💿 **Phase 2: Partition Disk**

**Inside NixOS installer** (you'll be logged in as `nixos` user):

```bash
# Become root
sudo -i

# List disks
lsblk
# You should see vda (100GB virtual disk)

# Partition with parted (UEFI/GPT)
parted /dev/vda -- mklabel gpt
parted /dev/vda -- mkpart ESP fat32 1MB 512MB
parted /dev/vda -- set 1 esp on
parted /dev/vda -- mkpart primary 512MB 100%

# Format partitions
mkfs.fat -F 32 -n boot /dev/vda1
mkfs.ext4 -L nixos /dev/vda2

# Mount filesystems
mount /dev/disk/by-label/nixos /mnt
mkdir -p /mnt/boot
mount /dev/disk/by-label/boot /mnt/boot
```

---

## ⚙️ **Phase 3: Generate Configuration**

```bash
# Generate initial config
nixos-generate-config --root /mnt

# This creates:
# /mnt/etc/nixos/configuration.nix
# /mnt/etc/nixos/hardware-configuration.nix
```

---

## 📝 **Phase 4: Configure with Flakes**

### **4.1 Create Flake Directory**

```bash
# Create directory for flake
mkdir -p /mnt/etc/nixos

# Mount shared folder temporarily (to copy our configs)
mkdir -p /mnt/mnt/grainkae3g
mount -t 9p -o trans=virtio grainkae3g /mnt/mnt/grainkae3g
```

### **4.2 Copy Grain Network Configurations**

```bash
# Copy our pre-made configurations
cp /mnt/mnt/grainkae3g/grainstore/grainsource-nixos-qemu-kvm/nixos-configs/flake.nix \
   /mnt/etc/nixos/

cp /mnt/mnt/grainkae3g/grainstore/grainsource-nixos-qemu-kvm/nixos-configs/grainkae3g-grain6-wifi.nix \
   /mnt/etc/nixos/

cp /mnt/mnt/grainkae3g/grainstore/grainsource-nixos-qemu-kvm/nixos-configs/home.nix \
   /mnt/etc/nixos/
```

### **4.3 Update Configuration (if needed)**

```bash
# Edit if you want to customize
nano /mnt/etc/nixos/grainkae3g-grain6-wifi.nix

# The configuration includes:
# - Flakes enabled
# - Home Manager integrated  
# - grain6 + grainwifi services
# - Shared folder at /mnt/grainkae3g
# - All Grain Network dependencies
```

---

## 🚀 **Phase 5: Install NixOS**

```bash
# Install with flakes
nixos-install --flake /mnt/etc/nixos#nixos-grainkae3g

# OR if not using flakes:
nixos-install

# Set root password when prompted
# (you'll change this later)

# Reboot
reboot
```

---

## 🔐 **Phase 6: First Boot Configuration**

**After reboot**, log in as user `xy` (password: "changeme" or whatever you set):

### **6.1 Change Password**

```bash
# Change your password
passwd
```

### **6.2 Verify Flakes are Enabled**

```bash
# Check nix settings
nix show-config | grep experimental-features
# Should show: nix-command flakes
```

### **6.3 Verify Shared Folder**

```bash
# Check mount
ls -la /mnt/grainkae3g

# Should show your grainkae3g workspace!
```

---

## 🏠 **Phase 7: Test Home Manager**

### **7.1 Initial Home Manager Build**

```bash
# Rebuild system (which includes Home Manager)
sudo nixos-rebuild switch --flake /etc/nixos#nixos-grainkae3g

# This will:
# - Apply NixOS configuration
# - Build Home Manager configuration
# - Set up user environment
```

### **7.2 Verify Home Manager**

```bash
# Check Home Manager generation
home-manager generations

# Test shell aliases
kg  # Should work!
grain  # Should cd to /mnt/grainkae3g
```

### **7.3 Test Grain Network Access**

```bash
# Navigate to shared folder
cd /mnt/grainkae3g

# Run grain commands
bb qb-kk
bb gt
```

---

## 🌾 **Phase 8: Test Trivial Home Manager Switch**

### **8.1 Make a Small Change**

```bash
# Edit home.nix
sudo nano /etc/nixos/home.nix

# Add a simple package (example: htop)
# Under home.packages, add: htop
```

### **8.2 Rebuild with Home Manager**

```bash
# Rebuild (this tests flakes + Home Manager)
sudo nixos-rebuild switch --flake /etc/nixos#nixos-grainkae3g

# Verify change applied
which htop
# Should show htop is now available
```

---

## 🔧 **Troubleshooting**

### **Issue: Flakes not enabled**

```bash
# Add to /etc/nixos/configuration.nix:
nix.settings.experimental-features = [ "nix-command" "flakes" ];

# Rebuild
sudo nixos-rebuild switch
```

### **Issue: Shared folder not mounted**

```bash
# Check if 9p module loaded
lsmod | grep 9p

# Mount manually
sudo mount -t 9p -o trans=virtio grainkae3g /mnt/grainkae3g

# Add to /etc/fstab for persistence (already in our config)
```

### **Issue: Home Manager not found**

```bash
# Flake should handle this, but if needed:
nix-channel --add https://github.com/nix-community/home-manager/archive/master.tar.gz home-manager
nix-channel --update
```

---

## ✅ **Success Checklist**

After installation, verify:

- [ ] NixOS 25.11 unstable running
- [ ] Flakes enabled (`nix show-config | grep experimental`)
- [ ] Home Manager installed and working
- [ ] Shared folder mounted at `/mnt/grainkae3g`
- [ ] grain6 and grainwifi services defined
- [ ] Babashka and Clojure installed
- [ ] Shell aliases working (`kg`, `grain`, etc.)
- [ ] Can access Grain Network from shared folder

---

## 🌟 **What You'll Have**

```
NixOS 25.11 Unstable (32 GB RAM, 8 CPUs)
    ↓
Flakes Enabled (reproducible builds)
    ↓
Home Manager (declarative user environment)
    ↓
Shared Folder (/mnt/grainkae3g)
    ↓
Grain Network (grain6 + grainwifi + Humble UI)
```

---

**Status**: 🌱 Ready for NixOS installation in VM  
**Next**: Access console and follow this guide  

🌾 **now == next + 1** (but make it declarative with flakes, chief!) 📦
