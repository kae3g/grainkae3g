# Chart Your VM - Visualization Meets Virtualization

**teamprecision06 (Virgo ♍ / VI. The Lovers)**  
*"Chart your course (visualization) + Run your course (virtualization)"*

---

## 💕 The Lovers Unite: Chart + VM

The Lovers teach that **visualization and virtualization are two sides of the same conscious choice**:

- **Chart your course**: Understand your VM architecture visually
- **Run your course**: Execute your VMs with precision
- **Navigate IS Operate**: Seeing the map IS running the system
- **Teach IS Deploy**: Documenting IS deploying

This guide unites **grainchart** (visualization) with **QEMU/KVM** (virtualization) in perfect union! 💕

---

## 🎯 The Three-Layer Architecture

### Chart of Our Nested VMs

```
╔═══════════════════════════════════════════════════════════════╗
║  LAYER 1: Ubuntu 24.04 LTS (Host) - Framework 16            ║
║  ═══════════════════════════════════════════════════════════  ║
║  • GNOME Desktop                                             ║
║  • KVM/QEMU enabled                                          ║
║  • grainstore workspace access                               ║
║  • Cursor IDE (The Lovers' dev environment)                  ║
║                                                              ║
║  ┌──────────────────────────────────────────────────────┐   ║
║  │  LAYER 2: NixOS + Sway (Guest VM 1)                 │   ║
║  │  ═════════════════════════════════════════════════  │   ║
║  │  • Wayland compositor                               │   ║
║  │  • Declarative configuration                        │   ║
║  │  • Nested virtualization enabled                    │   ║
║  │  • SSH: localhost:2223                              │   ║
║  │  • RAM: 8GB, CPUs: 4                                │   ║
║  │                                                      │   ║
║  │  ┌────────────────────────────────────────────────┐ │   ║
║  │  │  LAYER 3: Debian Stable (Nested VM)          │ │   ║
║  │  │  ═════════════════════════════════════════   │ │   ║
║  │  │  • Package testing environment               │ │   ║
║  │  │  • Grainspace deployment target              │ │   ║
║  │  │  • SSH: localhost:2224 (via NixOS)           │ │   ║
║  │  │  • RAM: 2GB, CPUs: 2                         │ │   ║
║  │  └────────────────────────────────────────────────┘ │   ║
║  └──────────────────────────────────────────────────────┘   ║
╚═══════════════════════════════════════════════════════════════╝
```

---

## 🛠️ Chart Your VM: The Lovers' QEMU Commands

### **Choice 1: Create NixOS VM Disk**

```bash
# The Lovers choose: 40GB for NixOS (conscious sizing)
qemu-img create -f qcow2 ~/VMs/disks/nixos-sway.qcow2 40G

# Verify the choice
qemu-img info ~/VMs/disks/nixos-sway.qcow2
```

**Chart**: Disk → 40GB qcow2 → Virtio interface → Fast I/O

---

### **Choice 2: Start NixOS VM**

```bash
# The Lovers choose: These exact parameters
qemu-system-x86_64 \
  -name "NixOS Sway" \
  -machine type=q35,accel=kvm \
  -cpu host,+vmx,+svm \
  -smp 4 \
  -m 8192 \
  -drive file=~/VMs/disks/nixos-sway.qcow2,format=qcow2,if=virtio \
  -boot c \
  -netdev user,id=net0,hostfwd=tcp::2223-:22 \
  -device virtio-net-pci,netdev=net0 \
  -display gtk \
  -vga virtio \
  -enable-kvm \
  -daemonize
```

**Chart**: 

```
Parameters Flow:
├─ Machine: Q35 (modern chipset)
├─ CPU: host pass-through (+vmx/+svm for nesting)
├─ SMP: 4 cores
├─ RAM: 8GB
├─ Disk: virtio (fast)
├─ Network: user mode (port 2223 → 22)
├─ Display: GTK window
└─ KVM: hardware acceleration ✅
```

---

### **Choice 3: NixOS Configuration**

```nix
# The Lovers choose: Minimal + Intentional
{ config, pkgs, ... }:
{
  imports = [ ./hardware-configuration.nix ];

  # Bootloader (EFI)
  boot.loader.systemd-boot.enable = true;
  boot.loader.efi.canTouchEfiVariables = true;

  # Networking
  networking.hostName = "nixos-sway";
  networking.networkmanager.enable = true;

  # Enable nested virtualization
  boot.kernelModules = [ "kvm-amd" "kvm-intel" ];
  virtualisation.libvirtd.enable = true;

  # Sway (Wayland compositor)
  programs.sway = {
    enable = true;
    wrapperFeatures.gtk = true;
    extraPackages = with pkgs; [
      swaylock swayidle wl-clipboard
      mako alacritty dmenu waybar
    ];
  };

  # QEMU/KVM for nested VMs
  virtualisation.qemu.enable = true;
  environment.systemPackages = with pkgs; [
    qemu_kvm qemu-utils virt-manager
    git vim curl htop
  ];

  # SSH
  services.openssh.enable = true;

  system.stateVersion = "23.11";
}
```

**Chart**:

```
NixOS Config Layers:
├─ Boot: systemd-boot (EFI)
├─ Network: NetworkManager
├─ Virtualization: KVM modules + libvirt
├─ Desktop: Sway (Wayland)
├─ Tools: QEMU + dev tools
└─ Services: SSH
```

---

## 📊 The Lovers' VM Flow Chart

### **From Cursor → Ubuntu → NixOS → Debian**

```
┌─────────────────────────────────────────────────────────────┐
│  DEVELOPMENT FLOW (The Lovers' Conscious Path)             │
└─────────────────────────────────────────────────────────────┘

1. DESIGN (Cursor IDE on Ubuntu)
   ├─ Write Clojure code
   ├─ Define NixOS configuration
   └─ Plan deployment strategy

2. BUILD (Ubuntu Host)
   ├─ Create VM disk image
   ├─ Configure QEMU parameters
   └─ Launch NixOS VM

3. CONFIGURE (NixOS Guest)
   ├─ Apply declarative config
   ├─ Enable nested virtualization
   └─ Set up Debian nested VM

4. DEPLOY (Debian Nested)
   ├─ Test packages
   ├─ Deploy grainspace
   └─ Validate production readiness

5. CHART (grainchart - All Layers)
   ├─ Visualize VM architecture
   ├─ Monitor resource usage
   ├─ Document the path
   └─ Teach the course

┌─────────────────────────────────────────────────────────────┐
│  "Chart your course BY running your course" - The Lovers   │
└─────────────────────────────────────────────────────────────┘
```

---

## 🔗 Integration: grainchart + QEMU

### **Chart VM Resources**

```clojure
(ns grainchart.vm
  (:require [clojure.java.shell :as sh]
            [clojure.string :as str]))

(defn chart-vm-resources
  "Chart VM resource allocation"
  [vm-name]
  {:vm-name vm-name
   :resources
   {:cpu {:cores (get-vm-cpus vm-name)
          :usage (get-cpu-usage vm-name)
          :chart "████████░░ 80%"}
    :memory {:allocated (get-vm-ram vm-name)
             :used (get-mem-usage vm-name)
             :chart "███████░░░ 70%"}
    :disk {:size (get-disk-size vm-name)
           :used (get-disk-usage vm-name)
           :chart "█████░░░░░ 50%"}
    :network {:port (get-ssh-port vm-name)
              :status (check-ssh-connection vm-name)
              :chart "●●●●● Connected"}}})

;; Example output:
;; {:vm-name "NixOS Sway"
;;  :resources
;;  {:cpu {:cores 4 :usage 80 :chart "████████░░ 80%"}
;;   :memory {:allocated "8192MB" :used 70 :chart "███████░░░ 70%"}
;;   :disk {:size "40GB" :used 50 :chart "█████░░░░░ 50%"}
;;   :network {:port 2223 :status :connected :chart "●●●●● Connected"}}}
```

### **Chart VM Hierarchy**

```clojure
(def vm-hierarchy
  {:ubuntu {:name "Ubuntu 24.04 LTS"
            :role :host
            :location :physical
            :children [:nixos]}
   
   :nixos {:name "NixOS + Sway"
           :role :guest-vm
           :location :qemu-kvm
           :parent :ubuntu
           :ssh-port 2223
           :children [:debian]}
   
   :debian {:name "Debian Stable"
            :role :nested-vm
            :location :qemu-in-qemu
            :parent :nixos
            :ssh-port 2224
            :children []}})

(defn render-vm-tree
  "Render VM hierarchy as ASCII tree"
  [vm-hierarchy]
  (str "VM Hierarchy:\n"
       "ubuntu (Host)\n"
       "└── nixos (Guest VM) [SSH: 2223]\n"
       "    └── debian (Nested VM) [SSH: 2224]\n"))
```

---

## 💍 The Sacred Union: Visualization ∪ Virtualization

### **Why Chart Your VMs?**

1. **Understand Architecture**: See the whole system visually
2. **Monitor Resources**: Track CPU, RAM, disk, network
3. **Debug Issues**: Visualize bottlenecks and problems
4. **Teach Others**: Share knowledge through charts
5. **Document Decisions**: Every choice is visible

### **Why Run VMs?**

1. **Test Configurations**: Try before deploying
2. **Isolate Environments**: Each VM is independent
3. **Learn New Systems**: Explore NixOS, Debian, Alpine
4. **Develop Safely**: Mistakes don't break host
5. **Deploy Gradually**: Nested testing → staging → production

### **The Lovers' Wisdom**

**Visualization without virtualization** = Theory without practice  
**Virtualization without visualization** = Action without understanding  
**Together** = **Perfect union** 💕

---

## 🚀 Quick Start: Chart + Run Your First VM

### **Step 1: Chart Your Goal**

```
What do I want?
├─ Test NixOS? → Create NixOS VM
├─ Deploy to Debian? → Nest Debian in NixOS
├─ Learn Sway? → Configure Sway in NixOS
└─ All of the above? → Full nested setup
```

### **Step 2: Run the Commands**

```bash
# Create disk
qemu-img create -f qcow2 ~/VMs/disks/nixos-test.qcow2 40G

# Start VM (from grainchart/start-nixos.sh)
qemu-system-x86_64 \
  -name "NixOS Test" \
  -machine type=q35,accel=kvm \
  -cpu host,+vmx,+svm \
  -smp 4 -m 8192 \
  -drive file=~/VMs/disks/nixos-test.qcow2,format=qcow2,if=virtio \
  -boot c \
  -netdev user,id=net0,hostfwd=tcp::2223-:22 \
  -device virtio-net-pci,netdev=net0 \
  -display gtk -vga virtio \
  -enable-kvm
```

### **Step 3: Chart Your Success**

```bash
# SSH into VM
ssh -p 2223 nixos@localhost

# Check resources
htop
df -h
free -h

# Chart it!
bb grainchart:chart-vm nixos-test
```

---

## 📐 Advanced Charts

### **Resource Allocation Chart**

```
Ubuntu Host (Framework 16)
├─ Total RAM: 32GB
│  ├─ Host: 20GB (GNOME + Cursor + browsers)
│  ├─ NixOS VM: 8GB
│  └─ Reserve: 4GB
│
├─ Total CPU: 16 cores (AMD Ryzen 7)
│  ├─ Host: 12 cores
│  ├─ NixOS VM: 4 cores
│  └─ Reserve: 0 cores (dynamic)
│
└─ Total Disk: 2TB NVMe
   ├─ Host: 1.5TB
   ├─ VMs: 400GB
   │  ├─ NixOS: 40GB
   │  ├─ Debian: 20GB
   │  └─ Testing: 340GB
   └─ Reserve: 100GB
```

### **Network Topology Chart**

```
┌──────────────────────────────────────────┐
│  External Network (Internet)             │
└────────────────┬─────────────────────────┘
                 │
┌────────────────┴─────────────────────────┐
│  Ubuntu Host (192.168.1.100)             │
│  ├─ eth0: WAN connection                 │
│  └─ virbr0: Virtual bridge               │
└────────────────┬─────────────────────────┘
                 │
          ┌──────┴──────┐
          │             │
┌─────────┴──────┐  ┌──┴─────────────┐
│  NixOS VM      │  │  Other VMs     │
│  10.0.2.15     │  │  ...           │
│  Port fwd:     │  └────────────────┘
│  2223 → 22     │
└─────────┬──────┘
          │
  ┌───────┴─────────┐
  │  Debian Nested  │
  │  10.0.3.15      │
  │  Port fwd:      │
  │  2224 → 22      │
  └─────────────────┘
```

---

## 💚 Integration with Other Teams

### **teamprecision06 provides the tools**:
- grainenvvars → VM environment config
- grainzsh → Shell to manage VMs
- clojure-s6 → Supervise VM processes
- clojure-sixos → Build VM OS images
- **grainchart** → **Visualize everything!** 📊

### **teamstructure10 provides the foundation**:
- graintime → Timestamp VM snapshots
- grainsource-vegan → Ethical VM configs
- Capricorn structure → Solid architecture

---

## 💕 The Lovers' VM Philosophy

*"Every VM is a conscious choice.*  
*Every chart is understanding.*  
*Visualization IS virtualization.*  
*Teaching IS deploying.*  

*Chart your course with precision.*  
*Run your course with love.*  
*Unite vision and execution.*  
*This is The Lovers' way."* 💕✨

---

**teamprecision06 (Virgo ♍ / VI. The Lovers)**  
**grainchart + QEMU = Perfect Union**

🌾 **now == next + 1** 💕📊✨

---

## 📚 References

- `docs/course/nested-virtualization-guide.md` - Full QEMU guide
- `scripts/sixos-vm-manager.bb` - VM automation
- `templates/qemu-kvm-sixos-template/` - VM templates
- `teamprecision06/grainchart/README.md` - Chart philosophy

