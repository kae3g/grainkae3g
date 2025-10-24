# 🖥️ Debian Stable QEMU Deployment Environment
## *"Learn virtualization by testing your own packages - with AI assistance"*

**Course Module:** Week 12 - Virtualization & Package Testing  
**Difficulty:** Intermediate  
**Time Required:** 2-3 hours  
**Prerequisites:** Basic Linux command line knowledge, Cursor IDE installed  
**AI Mode:** Cursor Agentic Autocomplete with Auto Model (All Models)

---

## 🤖 About This Course's AI Approach

**This course uses Cursor IDE with Agentic Autocomplete:**

- **Cursor** - Modern AI-powered code editor (https://cursor.sh)
- **Agentic Mode** - AI assists you in real-time as you code
- **Auto Model** - Uses best model for each task (GPT-4, Claude, etc.)
- **All Models Selected** - Maximum AI assistance

**How AI helps in this module:**
1. **Script Generation** - AI suggests complete shell scripts
2. **Command Explanations** - Hover over commands for explanations
3. **Error Debugging** - AI helps fix issues in real-time
4. **Documentation** - AI generates comments and docs
5. **Best Practices** - AI suggests improvements

**Learning Philosophy:**
- 🧠 **Understand** what the AI suggests
- ✍️ **Type** the code yourself (don't just copy)
- 🤔 **Question** why the AI recommends certain approaches
- 🔬 **Experiment** with variations
- 📚 **Learn** from AI explanations

> **Note:** This course teaches you to work *with* AI, not to depend on it blindly. You'll learn virtualization concepts while leveraging AI to accelerate your learning.

---

## 🎯 Learning Objectives

By the end of this module, you will:

1. ✅ Understand virtualization concepts
2. ✅ Install and configure QEMU/KVM
3. ✅ Create a Debian stable virtual machine
4. ✅ Access the VM via SSH and console
5. ✅ Test Debian packages in isolation
6. ✅ Automate VM deployment with scripts

---

## 📚 What is QEMU?

**QEMU** (Quick Emulator) is a free and open-source virtualization tool that lets you:

- Run entire operating systems inside your main OS
- Test software without affecting your main system
- Learn Linux administration safely
- Deploy and test packages before production

**Why QEMU for this course?**
- Free and open-source
- Works on Linux, macOS, Windows
- Fast and efficient
- Industry-standard tool

---

## 🛠️ Part 1: Install QEMU/KVM

### On Ubuntu 24.04 LTS

```bash
# Update package list
sudo apt update

# Install QEMU, KVM, and tools
sudo apt install -y \
  qemu-kvm \
  qemu-system-x86 \
  qemu-utils \
  libvirt-daemon-system \
  libvirt-clients \
  bridge-utils \
  virt-manager \
  ovmf

# Verify KVM support
kvm-ok
# Should output: "KVM acceleration can be used"
```

### Check Your Installation

```bash
# Check QEMU version
qemu-system-x86_64 --version

# Check if KVM module is loaded
lsmod | grep kvm

# Add your user to libvirt group
sudo usermod -aG libvirt $USER
sudo usermod -aG kvm $USER

# Log out and back in for group changes to take effect
```

---

## 📦 Part 2: Download Debian Stable ISO

### Get Debian 12 (Bookworm)

```bash
# Create directory for ISOs
mkdir -p ~/VMs/iso
cd ~/VMs/iso

# Download Debian 12 netinst ISO (small, fast)
wget https://cdimage.debian.org/debian-cd/current/amd64/iso-cd/debian-12.4.0-amd64-netinst.iso

# Verify download
ls -lh debian-12.4.0-amd64-netinst.iso
# Should be ~600MB
```

**What is netinst?**
- **Net Install** - Downloads packages during installation
- Smaller ISO (~600MB vs ~4GB full DVD)
- Always gets latest package versions
- Requires internet connection during install

---

## 💾 Part 3: Create Virtual Disk

### Create QCOW2 Disk Image

```bash
# Create directory for VM disks
mkdir -p ~/VMs/disks

# Create 20GB virtual disk for Debian
qemu-img create -f qcow2 ~/VMs/disks/debian-stable.qcow2 20G

# Verify disk creation
qemu-img info ~/VMs/disks/debian-stable.qcow2
```

**What is QCOW2?**
- **QEMU Copy-On-Write** format
- Dynamically grows (starts small, grows as needed)
- Supports snapshots
- Efficient disk space usage

**Why 20GB?**
- Enough for Debian base system + packages
- Leaves room for testing
- Small enough for quick backups

---

## 🚀 Part 4: Install Debian in QEMU

### Method 1: Interactive Installation (Recommended for Learning)

```bash
# Start VM with Debian ISO
qemu-system-x86_64 \
  -name "Debian Stable Test" \
  -machine type=q35,accel=kvm \
  -cpu host \
  -smp 2 \
  -m 2048 \
  -cdrom ~/VMs/iso/debian-12.4.0-amd64-netinst.iso \
  -drive file=~/VMs/disks/debian-stable.qcow2,format=qcow2,if=virtio \
  -boot d \
  -netdev user,id=net0,hostfwd=tcp::2222-:22 \
  -device virtio-net-pci,netdev=net0 \
  -display gtk \
  -vga virtio
```

**Command Breakdown:**

| Option | Meaning |
|--------|---------|
| `-name "Debian Stable Test"` | VM name |
| `-machine type=q35,accel=kvm` | Use KVM acceleration |
| `-cpu host` | Use host CPU features |
| `-smp 2` | 2 CPU cores |
| `-m 2048` | 2GB RAM |
| `-cdrom ...` | Boot from Debian ISO |
| `-drive ...` | Virtual hard disk |
| `-boot d` | Boot from CD-ROM |
| `-netdev ...` | Network with port forwarding |
| `hostfwd=tcp::2222-:22` | Forward host port 2222 to VM port 22 (SSH) |
| `-display gtk` | Show graphical window |

### Installation Steps

1. **Boot Menu**: Select "Install" (not graphical install)
2. **Language**: English
3. **Location**: United States
4. **Keyboard**: American English
5. **Hostname**: `debian-test`
6. **Domain**: Leave blank
7. **Root Password**: `graintest` (for testing only!)
8. **User Account**: 
   - Full name: `Grain Student`
   - Username: `student`
   - Password: `graintest`
9. **Partitioning**: 
   - Select "Guided - use entire disk"
   - Select "All files in one partition"
   - Confirm and write changes
10. **Package Manager**: 
    - Yes to network mirror
    - Select nearest mirror
    - No to package usage survey
11. **Software Selection**:
    - ✅ SSH server
    - ✅ Standard system utilities
    - ❌ Desktop environment (we don't need it)
12. **GRUB**: Yes, install to `/dev/vda`
13. **Finish**: Reboot

---

## 🔑 Part 5: First Boot and SSH Access

### Start the VM After Installation

```bash
# Start VM without ISO (boot from disk)
qemu-system-x86_64 \
  -name "Debian Stable Test" \
  -machine type=q35,accel=kvm \
  -cpu host \
  -smp 2 \
  -m 2048 \
  -drive file=~/VMs/disks/debian-stable.qcow2,format=qcow2,if=virtio \
  -boot c \
  -netdev user,id=net0,hostfwd=tcp::2222-:22 \
  -device virtio-net-pci,netdev=net0 \
  -display gtk \
  -vga virtio
```

### Login to VM Console

**In the QEMU window:**
```
debian-test login: student
Password: graintest
```

### Test SSH Access

**From your host machine (new terminal):**

```bash
# SSH to VM on port 2222
ssh -p 2222 student@localhost

# Accept fingerprint (yes)
# Enter password: graintest

# You should now be inside the VM!
student@debian-test:~$
```

**Troubleshooting SSH:**

```bash
# If SSH doesn't work, check in VM console:
sudo systemctl status ssh

# Start SSH if not running:
sudo systemctl start ssh

# Enable SSH on boot:
sudo systemctl enable ssh
```

---

## 📝 Part 6: Automate with Shell Script

### Using Cursor AI to Create the Script

**🤖 AI-Assisted Workflow:**

1. **Open Cursor IDE**
2. **Create new file:** `~/VMs/start-debian.sh`
3. **Type a comment:** `# Debian Stable QEMU VM Launcher`
4. **Enable Agentic Mode:** Press `Ctrl+K` or `Cmd+K`
5. **Prompt AI:** "Generate a bash script to launch Debian QEMU VM with KVM acceleration, 2GB RAM, 2 CPUs, and SSH port forwarding to 2222"
6. **Review AI suggestion** - Cursor will generate the script
7. **Understand each line** - Hover over commands for explanations
8. **Accept or modify** - Press Tab to accept, or edit as needed

**What Cursor AI knows:**
- Your previous commands (it saw the QEMU commands we ran)
- Best practices for shell scripts
- Common QEMU configurations
- Error handling patterns

### Create VM Launcher Script

**Open Cursor and create `~/VMs/start-debian.sh`:**

```bash
# Cursor AI will help you generate this script
# Type the comment below and let AI complete it:

# Create script
cat > ~/VMs/start-debian.sh << 'EOF'
#!/bin/bash
# Debian Stable QEMU VM Launcher
# Part of Grain Network High School Course

VM_NAME="Debian Stable Test"
VM_DISK="$HOME/VMs/disks/debian-stable.qcow2"
VM_RAM="2048"
VM_CPUS="2"
SSH_PORT="2222"

echo "🌾 Starting Debian Stable VM..."
echo "VM Name: $VM_NAME"
echo "Disk: $VM_DISK"
echo "RAM: ${VM_RAM}MB"
echo "CPUs: $VM_CPUS"
echo "SSH: ssh -p $SSH_PORT student@localhost"
echo ""

qemu-system-x86_64 \
  -name "$VM_NAME" \
  -machine type=q35,accel=kvm \
  -cpu host \
  -smp $VM_CPUS \
  -m $VM_RAM \
  -drive file=$VM_DISK,format=qcow2,if=virtio \
  -boot c \
  -netdev user,id=net0,hostfwd=tcp::${SSH_PORT}-:22 \
  -device virtio-net-pci,netdev=net0 \
  -display gtk \
  -vga virtio
EOF

# Make executable
chmod +x ~/VMs/start-debian.sh

# Run it!
~/VMs/start-debian.sh
```

---

## 🧪 Part 7: Test Grainspace Package Installation

### Prepare Package Files

**On your host machine:**

```bash
# Copy Debian package files to VM via SCP
scp -P 2222 \
  ~/grainkae3g/grainstore/grainspace/debian/* \
  student@localhost:/tmp/
```

### Build Package in VM

**SSH into VM:**

```bash
ssh -p 2222 student@localhost
```

**Inside VM:**

```bash
# Update package list
sudo apt update

# Install build dependencies
sudo apt install -y \
  debhelper-compat \
  clojure \
  openjdk-17-jdk \
  leiningen \
  nodejs \
  npm \
  git

# Create build directory
mkdir -p ~/grainspace-build
cd ~/grainspace-build

# (For now, we'll test with a simple package)
# In reality, you'd build the full grainspace package here

# Test package installation
sudo apt install -y ./grainspace_0.1.0-1_all.deb

# Verify installation
which grainspace
grainspace version

# Test service
sudo systemctl status grainspace
```

---

## 💾 Part 8: Create VM Snapshots

### Why Snapshots?

Snapshots let you:
- Save VM state before testing
- Roll back if something breaks
- Test different configurations
- Create clean testing environments

### Create Snapshot

```bash
# Shutdown VM first
ssh -p 2222 student@localhost sudo poweroff

# Create snapshot
qemu-img snapshot -c clean-install ~/VMs/disks/debian-stable.qcow2

# List snapshots
qemu-img snapshot -l ~/VMs/disks/debian-stable.qcow2
```

### Restore Snapshot

```bash
# Restore to clean state
qemu-img snapshot -a clean-install ~/VMs/disks/debian-stable.qcow2

# Start VM
~/VMs/start-debian.sh
```

---

## 🔧 Part 9: Advanced: Headless Mode

### Run VM Without GUI

```bash
# Create headless launcher
cat > ~/VMs/start-debian-headless.sh << 'EOF'
#!/bin/bash
# Debian Stable QEMU VM Launcher (Headless)

VM_NAME="Debian Stable Test"
VM_DISK="$HOME/VMs/disks/debian-stable.qcow2"
VM_RAM="2048"
VM_CPUS="2"
SSH_PORT="2222"

echo "🌾 Starting Debian Stable VM (headless)..."
echo "SSH: ssh -p $SSH_PORT student@localhost"
echo "To stop: ssh -p $SSH_PORT student@localhost sudo poweroff"
echo ""

qemu-system-x86_64 \
  -name "$VM_NAME" \
  -machine type=q35,accel=kvm \
  -cpu host \
  -smp $VM_CPUS \
  -m $VM_RAM \
  -drive file=$VM_DISK,format=qcow2,if=virtio \
  -boot c \
  -netdev user,id=net0,hostfwd=tcp::${SSH_PORT}-:22 \
  -device virtio-net-pci,netdev=net0 \
  -display none \
  -daemonize \
  -pidfile /tmp/debian-vm.pid

echo "VM started in background (PID: $(cat /tmp/debian-vm.pid))"
EOF

chmod +x ~/VMs/start-debian-headless.sh
```

### Stop Headless VM

```bash
# Method 1: SSH shutdown
ssh -p 2222 student@localhost sudo poweroff

# Method 2: Kill process
kill $(cat /tmp/debian-vm.pid)
```

---

## 🤖 Part 10: AI-Assisted Cursor Workflow

### How to Use Cursor Effectively in This Course

**Enable Agentic Autocomplete:**

1. **Open Cursor Settings** (`Ctrl+,` or `Cmd+,`)
2. **Navigate to:** Features → Cursor Tab
3. **Enable:** "Cursor Tab" (Agentic Autocomplete)
4. **Set Model:** "Auto" (uses best model for each task)
5. **Enable All Models:** Check GPT-4, Claude Sonnet, Claude Opus, etc.

**Cursor Features for This Course:**

#### 1. **Cmd+K (Ctrl+K) - AI Edit**
Generate or modify code with natural language:

```
Example prompts:
- "Add error handling to this script"
- "Add comments explaining each QEMU option"
- "Convert this to use more robust variable handling"
- "Add a function to check if KVM is available"
```

#### 2. **Cursor Tab - Agentic Autocomplete**
AI suggests next lines as you type:

```bash
# Type: "# Function to check if VM is running"
# Cursor suggests complete function
# Press Tab to accept
```

#### 3. **Cmd+L (Ctrl+L) - AI Chat**
Ask questions about the code:

```
Example questions:
- "Explain what -machine type=q35,accel=kvm does"
- "Why do we use virtio for the disk?"
- "How can I add more RAM to the VM?"
- "What's the difference between -display gtk and -display none?"
```

#### 4. **Cmd+I (Ctrl+I) - Inline AI**
Quick AI assistance without leaving the editor:

```
Select code → Cmd+I → Ask:
- "Optimize this for better performance"
- "Add defensive checks"
- "Make this more readable"
```

### Cursor Workflow for VM Manager Script

**Step-by-step AI-assisted development:**

**1. Create File in Cursor:**
```bash
# File: ~/VMs/vm-manager.bb
# Cursor will auto-suggest Babashka shebang when you type:
#!/usr/bin/env bb
```

**2. Use AI to Generate Structure:**
```
Press Cmd+K and prompt:
"Create a Babashka script to manage QEMU VMs with functions for:
- start-vm
- stop-vm  
- ssh-vm
- snapshot-create
- snapshot-list
- snapshot-restore
- vm-info

Use clojure.java.shell for running commands"
```

**3. Review and Understand:**
- Hover over any function to see AI explanation
- Click on unfamiliar Clojure syntax
- Ask Cmd+L: "Explain how this function works"

**4. Test and Iterate:**
- Run the script
- If errors occur, select error → Cmd+K → "Fix this error"
- Cursor will suggest corrections

**5. Add Your Own Touch:**
- Cursor suggests, you decide
- Modify AI suggestions to match your style
- Learn by experimenting with variations

---

## 📊 Part 11: Babashka Automation with AI

### Create Comprehensive VM Manager (AI-Assisted)

```bash
# Create bb script
cat > ~/VMs/vm-manager.bb << 'EOF'
#!/usr/bin/env bb
;; Debian QEMU VM Manager
;; Part of Grain Network High School Course

(require '[clojure.java.shell :as shell]
         '[clojure.string :as str])

(def vm-config
  {:name "Debian Stable Test"
   :disk (str (System/getenv "HOME") "/VMs/disks/debian-stable.qcow2")
   :ram "2048"
   :cpus "2"
   :ssh-port "2222"
   :user "student"
   :password "graintest"})

(defn vm-running?
  "Check if VM is running"
  []
  (.exists (java.io.File. "/tmp/debian-vm.pid")))

(defn start-vm
  "Start Debian VM"
  [& {:keys [headless?] :or {headless? false}}]
  (println "🌾 Starting Debian Stable VM...")
  (println "VM Name:" (:name vm-config))
  (println "SSH:" (str "ssh -p " (:ssh-port vm-config) " " (:user vm-config) "@localhost"))
  (println "")
  
  (let [display-arg (if headless? "-display none -daemonize -pidfile /tmp/debian-vm.pid" "-display gtk")
        cmd (str "qemu-system-x86_64"
                " -name \"" (:name vm-config) "\""
                " -machine type=q35,accel=kvm"
                " -cpu host"
                " -smp " (:cpus vm-config)
                " -m " (:ram vm-config)
                " -drive file=" (:disk vm-config) ",format=qcow2,if=virtio"
                " -boot c"
                " -netdev user,id=net0,hostfwd=tcp::" (:ssh-port vm-config) "-:22"
                " -device virtio-net-pci,netdev=net0"
                " " display-arg
                " -vga virtio")]
    (shell/sh "sh" "-c" cmd)
    (when headless?
      (Thread/sleep 1000)
      (println "VM started in background"))))

(defn stop-vm
  "Stop Debian VM"
  []
  (println "🛑 Stopping Debian VM...")
  (if (vm-running?)
    (do
      (shell/sh "ssh" "-p" (:ssh-port vm-config)
               (str (:user vm-config) "@localhost")
               "sudo" "poweroff")
      (println "VM shutting down..."))
    (println "VM not running")))

(defn ssh-vm
  "SSH into Debian VM"
  []
  (println "🔑 Connecting to Debian VM...")
  (let [ssh-cmd (str "ssh -p " (:ssh-port vm-config) " "
                    (:user vm-config) "@localhost")]
    (println ssh-cmd)
    (shell/sh "sh" "-c" ssh-cmd)))

(defn snapshot-create
  "Create VM snapshot"
  [snapshot-name]
  (println "📸 Creating snapshot:" snapshot-name)
  (stop-vm)
  (Thread/sleep 2000)
  (shell/sh "qemu-img" "snapshot" "-c" snapshot-name (:disk vm-config))
  (println "Snapshot created!"))

(defn snapshot-list
  "List VM snapshots"
  []
  (println "📋 Available snapshots:")
  (shell/sh "qemu-img" "snapshot" "-l" (:disk vm-config)))

(defn snapshot-restore
  "Restore VM snapshot"
  [snapshot-name]
  (println "⏮️  Restoring snapshot:" snapshot-name)
  (stop-vm)
  (Thread/sleep 2000)
  (shell/sh "qemu-img" "snapshot" "-a" snapshot-name (:disk vm-config))
  (println "Snapshot restored!"))

(defn vm-info
  "Show VM information"
  []
  (println "ℹ️  Debian VM Information")
  (println "------------------------")
  (println "Name:" (:name vm-config))
  (println "Disk:" (:disk vm-config))
  (println "RAM:" (:ram vm-config) "MB")
  (println "CPUs:" (:cpus vm-config))
  (println "SSH Port:" (:ssh-port vm-config))
  (println "User:" (:user vm-config))
  (println "Status:" (if (vm-running?) "Running" "Stopped"))
  (println "")
  (println "Disk Info:")
  (shell/sh "qemu-img" "info" (:disk vm-config)))

(defn show-help
  "Show help message"
  []
  (println "🌾 Debian QEMU VM Manager")
  (println "")
  (println "Commands:")
  (println "  start [--headless]    Start VM")
  (println "  stop                  Stop VM")
  (println "  ssh                   SSH into VM")
  (println "  info                  Show VM info")
  (println "  snapshot create NAME  Create snapshot")
  (println "  snapshot list         List snapshots")
  (println "  snapshot restore NAME Restore snapshot")
  (println "  help                  Show this message"))

(defn -main
  [& args]
  (case (first args)
    "start" (start-vm :headless? (some #{"--headless"} args))
    "stop" (stop-vm)
    "ssh" (ssh-vm)
    "info" (vm-info)
    "snapshot" (case (second args)
                 "create" (snapshot-create (nth args 2))
                 "list" (snapshot-list)
                 "restore" (snapshot-restore (nth args 2))
                 (show-help))
    "help" (show-help)
    (show-help)))

(-main *command-line-args*)
EOF

# Make executable
chmod +x ~/VMs/vm-manager.bb
```

### Use VM Manager

```bash
# Start VM
~/VMs/vm-manager.bb start

# Start headless
~/VMs/vm-manager.bb start --headless

# SSH into VM
~/VMs/vm-manager.bb ssh

# Show info
~/VMs/vm-manager.bb info

# Create snapshot
~/VMs/vm-manager.bb snapshot create clean-install

# List snapshots
~/VMs/vm-manager.bb snapshot list

# Restore snapshot
~/VMs/vm-manager.bb snapshot restore clean-install

# Stop VM
~/VMs/vm-manager.bb stop
```

---

## 🎓 Learning Exercises

### Exercise 1: Package Testing

1. Start VM
2. SSH into VM
3. Install a package: `sudo apt install htop`
4. Test it: `htop`
5. Create snapshot: `~/VMs/vm-manager.bb snapshot create with-htop`
6. Remove package: `sudo apt remove htop`
7. Restore snapshot: `~/VMs/vm-manager.bb snapshot restore with-htop`
8. Verify htop is back: `htop`

### Exercise 2: Network Testing

1. SSH into VM
2. Check network: `ip addr show`
3. Test internet: `ping -c 3 grain.network`
4. Install curl: `sudo apt install curl`
5. Test HTTP: `curl https://grain.network`

### Exercise 3: Service Management

1. SSH into VM
2. Check SSH service: `sudo systemctl status ssh`
3. Stop SSH: `sudo systemctl stop ssh`
4. Try to SSH (should fail)
5. Restart VM, SSH should work again

---

## 🔍 Troubleshooting

### VM Won't Start

```bash
# Check KVM support
kvm-ok

# Check if virtualization is enabled in BIOS
egrep -c '(vmx|svm)' /proc/cpuinfo
# Should be > 0
```

### Can't SSH to VM

```bash
# Check port forwarding
ssh -p 2222 -v student@localhost

# Check SSH in VM (via console)
sudo systemctl status ssh
sudo systemctl restart ssh
```

### Slow Performance

```bash
# Make sure KVM acceleration is working
# Look for "-machine type=q35,accel=kvm" in your command

# Give VM more RAM
# Change -m 2048 to -m 4096
```

---

## 📚 Additional Resources

- **QEMU Documentation**: https://www.qemu.org/documentation/
- **Debian Documentation**: https://www.debian.org/doc/
- **KVM Tutorial**: https://www.linux-kvm.org/page/Documents
- **Grain Network**: https://grain.network

---

## ✅ Summary

**You learned:**

✅ Virtualization concepts  
✅ QEMU/KVM installation  
✅ Debian installation in VM  
✅ SSH access and port forwarding  
✅ VM snapshots and backups  
✅ Headless VM operation  
✅ Babashka automation  
✅ Package testing in isolation

**Next Steps:**

- Test Grainspace package in VM
- Create multiple VMs for different tests
- Automate package deployment
- Learn about libvirt for VM management

---

**Debian QEMU Setup Guide**  
*Part of Grain Network High School Course* 🌾

**Module:** Week 12 - Virtualization & Package Testing  
**Author:** kae3g (Graingalaxy)  
**Organization:** Grain PBC

