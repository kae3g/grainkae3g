# 🌾 virt-manager Keyboard Shortcuts & NixOS Install Cheatsheet

## 🖱️ **CURSOR & DISPLAY SHORTCUTS**

```
Ctrl+Alt          ← Release mouse cursor from VM (MOST IMPORTANT!)
Ctrl+Alt+F        ← Toggle fullscreen
F11               ← Fullscreen toggle
Ctrl+Alt+1        ← Return to console view
```

**Cursor stuck/invisible?**
1. Press `Ctrl+Alt` to release
2. Move mouse outside VM window  
3. Click back in VM when ready

---

## 📺 **VIEW MENU OPTIONS**

```
View → Fullscreen                    Fullscreen mode
View → Scale Display → Auto          Auto-scale VM (recommended!)
View → Scale Display → Always        Always scale
View → Details                       Show VM settings
View → Console                       Return to console
```

---

## ⚡ **VM CONTROL**

```
Virtual Machine → Run                Start VM
Virtual Machine → Pause              Pause VM  
Virtual Machine → Shut Down          Graceful shutdown
Virtual Machine → Force Off          Hard power off
Virtual Machine → Force Reset        Force reboot
```

---

## ⌨️ **SEND KEYS TO VM**

```
Send Key → Ctrl+Alt+Del              Reboot VM
Send Key → Ctrl+Alt+Backspace        Kill X server (if GUI frozen)
```

---

## 🚀 **NIXOS INSTALLATION QUICK GUIDE**

### **Boot from ISO**

**If you see UEFI Shell**:
```
Shell> exit
```
Then select CDROM/DVD from boot menu

**Or Force Reboot**:
1. Virtual Machine → Force Reset
2. Press ESC quickly when restarting
3. Select "UEFI QEMU DVD-ROM"

---

### **Installation Commands** (copy-paste ready!)

```bash
# === STEP 1: Become Root ===
sudo -i

# === STEP 2: Partition Disk (UEFI/GPT) ===
parted /dev/vda -- mklabel gpt
parted /dev/vda -- mkpart ESP fat32 1MB 512MB
parted /dev/vda -- set 1 esp on
parted /dev/vda -- mkpart primary 512MB 100%

# === STEP 3: Format Partitions ===
mkfs.fat -F 32 -n boot /dev/vda1
mkfs.ext4 -L nixos /dev/vda2

# === STEP 4: Mount Filesystems ===
mount /dev/disk/by-label/nixos /mnt
mkdir -p /mnt/boot
mount /dev/disk/by-label/boot /mnt/boot

# === STEP 5: Generate Config ===
nixos-generate-config --root /mnt

# === STEP 6: Mount Shared Folder ===
mkdir -p /mnt/mnt/grainkae3g
mount -t 9p -o trans=virtio grainkae3g /mnt/mnt/grainkae3g

# Verify shared folder works
ls /mnt/mnt/grainkae3g

# === STEP 7: Copy Grain Network Configs ===
cp /mnt/mnt/grainkae3g/grainstore/grainsource-nixos-qemu-kvm/nixos-configs/flake.nix /mnt/etc/nixos/
cp /mnt/mnt/grainkae3g/grainstore/grainsource-nixos-qemu-kvm/nixos-configs/grainkae3g-grain6-wifi.nix /mnt/etc/nixos/
cp /mnt/mnt/grainkae3g/grainstore/grainsource-nixos-qemu-kvm/nixos-configs/home.nix /mnt/etc/nixos/

# Verify configs copied
ls -la /mnt/etc/nixos/

# === STEP 8: Install NixOS ===
nixos-install --flake /mnt/etc/nixos#nixos-grainkae3g

# (This takes 5-15 minutes)
# Set root password when prompted
# Set user xy password when prompted

# === STEP 9: Reboot ===
reboot
```

---

### **After Reboot (First Login)**

```bash
# Login as: xy
# Password: (what you set)

# Verify shared folder
ls -la /mnt/grainkae3g

# Test Grain Network
cd /mnt/grainkae3g/grainstore/grainbarrel
bb qb-kk

# Configure WiFi
nmcli device wifi list
nmcli device wifi connect "Starlink" password "YOUR_PASSWORD"
nmcli device wifi connect "iPhone-Tethering" password "YOUR_PASSWORD"

# Start grainwifi
cd /mnt/grainkae3g/grainstore/grainwifi
bb grainwifi:start

# Check status
bb grainwifi:status
```

---

## 🎯 **Quick Commands**

```bash
# Reopen virt-manager
virt-manager

# Check VM status
sudo virsh list --all

# Access VM console (text-only, no cursor issues!)
sudo virsh console nixos-grainkae3g
# (Exit with: Ctrl+])

# Check VM info
sudo virsh dominfo nixos-grainkae3g
```

---

## 🌾 **Grain Network After Install**

Once NixOS is installed, you'll have:
- ✅ 32GB RAM, 8 CPUs
- ✅ Flakes enabled
- ✅ Home Manager configured
- ✅ Shared folder at `/mnt/grainkae3g`
- ✅ grain6 service ready
- ✅ grainwifi dual-connection manager ready
- ✅ All Grain Network tools (bb, qb, gt, kg, fr)

---

**Status**: Ready to install NixOS in VM  
**Guide**: Use this cheatsheet + follow prompts  

🌾 **now == next + 1** (and next is typing 'exit' in UEFI shell!) 🚀
