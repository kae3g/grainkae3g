# 🌾 GrainSource s6 for Ubuntu 24.04 LTS

> **"s6 Supervision on Ubuntu - The Grain Network Way"**

Integrating s6 process supervision into Ubuntu 24.04 LTS for managing Grain Network services, QEMU/KVM, and system daemons.

---

## 📋 **Overview**

**What**: Install and configure s6 on Ubuntu 24.04 LTS  
**Why**: Better process supervision than systemd for Grain Network services  
**How**: Install s6, create service directories, integrate with grain6  

**Philosophy**: s6 gives us fine-grained control over service supervision, perfect for grain6's time-aware management.

---

## 🎯 **Architecture**

```
Ubuntu 24.04 LTS (systemd boot)
    ↓
s6-svscan (supervised by systemd)
    ↓
┌─────────────────────────────────────────────┐
│  s6 Service Tree (/service)                 │
├─────────────────────────────────────────────┤
│  libvirtd/          ← QEMU/KVM daemon       │
│  grain6/            ← Time-aware supervisor │
│  grainwifi/         ← Dual-WiFi manager     │
│  graindisplay/      ← Display warmth        │
│  graintime-api/     ← graintime web service │
└─────────────────────────────────────────────┘
```

**Key Insight**: We use systemd to start s6-svscan once at boot, then s6 supervises everything else!

---

## 📦 **Phase 1: Install s6 on Ubuntu**

### **1.1 Install s6 Package**

```bash
# s6 is available in Ubuntu repositories
sudo apt update
sudo apt install -y s6

# Verify installation
s6-svscan --version
s6-supervise --version
```

**Alternative: Build from Source** (for latest version):

```bash
# Install build dependencies
sudo apt install -y build-essential

# Download and build s6
cd /tmp
wget https://skarnet.org/software/s6/s6-2.12.0.1.tar.gz
tar xzf s6-2.12.0.1.tar.gz
cd s6-2.12.0.1
./configure --prefix=/usr/local
make
sudo make install

# Verify
/usr/local/bin/s6-svscan --version
```

### **1.2 Create s6 Service Directory**

```bash
# Create main service directory
sudo mkdir -p /service

# Create scan directory (for s6-svscan)
sudo mkdir -p /etc/s6/sv

# Set permissions
sudo chown -R root:root /service /etc/s6
```

---

## 🔧 **Phase 2: Create systemd Service for s6-svscan**

We need systemd to start s6-svscan at boot (Ubuntu uses systemd as PID 1).

### **2.1 Create s6-svscan systemd Service**

Create `/etc/systemd/system/s6-svscan.service`:

```ini
[Unit]
Description=s6 supervision tree
After=local-fs.target

[Service]
Type=simple
ExecStart=/usr/bin/s6-svscan /service
Restart=always
RestartSec=5
KillMode=process

[Install]
WantedBy=multi-user.target
```

### **2.2 Enable and Start s6-svscan**

```bash
# Reload systemd
sudo systemctl daemon-reload

# Enable s6-svscan to start at boot
sudo systemctl enable s6-svscan

# Start s6-svscan now
sudo systemctl start s6-svscan

# Verify it's running
sudo systemctl status s6-svscan
```

---

## 🛰️ **Phase 3: Create s6 Service Definitions**

### **3.1 libvirtd Service**

Create `/etc/s6/sv/libvirtd/run`:

```bash
#!/bin/sh
exec 2>&1
exec /usr/sbin/libvirtd --timeout 120
```

Create `/etc/s6/sv/libvirtd/log/run`:

```bash
#!/bin/sh
exec s6-log -b T /var/log/s6/libvirtd
```

Make executable and create log directory:

```bash
sudo mkdir -p /etc/s6/sv/libvirtd/log
sudo chmod +x /etc/s6/sv/libvirtd/run
sudo chmod +x /etc/s6/sv/libvirtd/log/run
sudo mkdir -p /var/log/s6/libvirtd
```

### **3.2 grain6 Service**

Create `/etc/s6/sv/grain6/run`:

```bash
#!/bin/sh
exec 2>&1
cd /home/xy/kae3g/grainkae3g/grainstore/grain6
exec chpst -u xy:xy /usr/bin/bb grain6:daemon
```

Create `/etc/s6/sv/grain6/log/run`:

```bash
#!/bin/sh
exec s6-log -b T /var/log/s6/grain6
```

Make executable:

```bash
sudo mkdir -p /etc/s6/sv/grain6/log
sudo chmod +x /etc/s6/sv/grain6/run
sudo chmod +x /etc/s6/sv/grain6/log/run
sudo mkdir -p /var/log/s6/grain6
sudo chown xy:xy /var/log/s6/grain6
```

### **3.3 grainwifi Service**

Create `/etc/s6/sv/grainwifi/run`:

```bash
#!/bin/sh
exec 2>&1
cd /home/xy/kae3g/grainkae3g/grainstore/grainwifi
exec chpst -u xy:xy /usr/bin/bb grainwifi:daemon
```

Create log service:

```bash
sudo mkdir -p /etc/s6/sv/grainwifi/log
sudo chmod +x /etc/s6/sv/grainwifi/run

# log/run
cat << 'EOF' | sudo tee /etc/s6/sv/grainwifi/log/run
#!/bin/sh
exec s6-log -b T /var/log/s6/grainwifi
EOF

sudo chmod +x /etc/s6/sv/grainwifi/log/run
sudo mkdir -p /var/log/s6/grainwifi
sudo chown xy:xy /var/log/s6/grainwifi
```

---

## 🚀 **Phase 4: Activate s6 Services**

### **4.1 Link Services to /service**

```bash
# Activate libvirtd
sudo ln -s /etc/s6/sv/libvirtd /service/libvirtd

# Activate grain6
sudo ln -s /etc/s6/sv/grain6 /service/grain6

# Activate grainwifi
sudo ln -s /etc/s6/sv/grainwifi /service/grainwifi
```

**s6-svscan will automatically detect and start these services!**

### **4.2 Verify Services**

```bash
# Check service status
s6-svstat /service/*

# Expected output:
# /service/grain6: up (pid 12345) 10 seconds
# /service/grainwifi: up (pid 12346) 10 seconds
# /service/libvirtd: up (pid 12347) 10 seconds
```

---

## 🎛️ **Phase 5: Service Management Commands**

### **5.1 Basic s6 Commands**

```bash
# Check service status
s6-svstat /service/grain6

# Start service (if down)
s6-svc -u /service/grain6

# Stop service
s6-svc -d /service/grain6

# Restart service
s6-svc -t /service/grain6

# Send TERM signal
s6-svc -t /service/grain6

# Send KILL signal
s6-svc -k /service/grain6

# Check all services
s6-svstat /service/*
```

### **5.2 Convenience Aliases**

Add to `~/.zshrc` or `~/.bashrc`:

```bash
# s6 service management aliases
alias s6status='s6-svstat /service/*'
alias s6up='s6-svc -u'
alias s6down='s6-svc -d'
alias s6restart='s6-svc -t'
alias s6logs='tail -f /var/log/s6'
```

---

## 🌾 **Phase 6: Integrate with grain6**

### **6.1 grain6 s6 Integration**

Create `grainstore/grain6/src/grain6/s6.clj`:

```clojure
(ns grain6.s6
  "s6 supervision integration for grain6"
  (:require [babashka.process :refer [shell]]
            [clojure.string :as str]))

(defn service-status
  "Get status of an s6 service"
  [service-name]
  (let [result (shell {:out :string :continue true}
                      (str "s6-svstat /service/" service-name))]
    (if (= 0 (:exit result))
      {:status :up
       :output (:out result)}
      {:status :down
       :output (:out result)})))

(defn start-service
  "Start an s6 service"
  [service-name]
  (shell (str "s6-svc -u /service/" service-name)))

(defn stop-service
  "Stop an s6 service"
  [service-name]
  (shell (str "s6-svc -d /service/" service-name)))

(defn restart-service
  "Restart an s6 service"
  [service-name]
  (shell (str "s6-svc -t /service/" service-name)))

(defn all-services
  "Get status of all s6 services"
  []
  (let [result (shell {:out :string} "s6-svstat /service/*")
        lines (str/split-lines (:out result))]
    (map #(hash-map :line %) lines)))
```

### **6.2 grain6 bb Task**

Add to `grainstore/grain6/bb.edn`:

```clojure
s6:status
{:doc "Show status of all s6 services"
 :task (shell "s6-svstat /service/*")}

s6:restart
{:doc "Restart a service: bb s6:restart <service-name>"
 :task (let [service (first *command-line-args*)]
         (if service
           (shell (str "s6-svc -t /service/" service))
           (println "Usage: bb s6:restart <service-name>")))}
```

---

## 🔄 **Phase 7: Minimize systemd Reliance** (Pragmatic Approach)

Based on research ([skarnet.org s6 documentation](https://skarnet.org/software/s6/unit-conversion.html)), here's the pragmatic way to minimize systemd while keeping compatibility:

### **7.1 The Hybrid Strategy**

```
┌─────────────────────────────────────────────┐
│  systemd (PID 1)                            │
│  - Kernel boot only                         │
│  - Starts s6-svscan ONCE                    │
│  - Nothing else                             │
└──────────────┬──────────────────────────────┘
               │
┌──────────────┴──────────────────────────────┐
│  s6-svscan                                  │
│  - Supervises ALL services                  │
│  - libvirtd, virtlogd, dnsmasq              │
│  - grain6, grainwifi, etc.                  │
└─────────────────────────────────────────────┘
```

### **7.2 Disable systemd Services Properly**

```bash
# Stop systemd's libvirtd and related services
sudo systemctl stop libvirtd.service
sudo systemctl stop libvirtd.socket
sudo systemctl stop libvirtd-ro.socket
sudo systemctl stop libvirtd-admin.socket
sudo systemctl stop virtlogd.service
sudo systemctl stop virtlogd.socket
sudo systemctl stop virtlogd-admin.socket

# Disable them permanently (s6 manages them now)
sudo systemctl disable libvirtd.service
sudo systemctl disable libvirtd.socket
sudo systemctl disable libvirtd-ro.socket
sudo systemctl disable libvirtd-admin.socket
sudo systemctl disable virtlogd.service
sudo systemctl disable virtlogd.socket
sudo systemctl disable virtlogd-admin.socket

# Mask them to prevent accidental starts
sudo systemctl mask libvirtd.service
sudo systemctl mask virtlogd.service
```

### **7.3 Complete libvirt Stack Under s6**

Unlike systemd's complex socket activation, we run libvirt services directly:

**Create `/etc/s6/sv/virtlogd/run`**:
```bash
#!/bin/sh
exec 2>&1
# virtlogd must start BEFORE libvirtd
exec /usr/sbin/virtlogd
```

**Create `/etc/s6/sv/libvirtd/run`** (updated):
```bash
#!/bin/sh
exec 2>&1
# Wait for virtlogd to be ready
s6-svwait -u /service/virtlogd
# Start libvirtd
exec /usr/sbin/libvirtd --timeout 120
```

**Set up dependencies**:
```bash
# Create dependency: libvirtd depends on virtlogd
sudo mkdir -p /etc/s6/sv/libvirtd/dependencies.d
echo "/service/virtlogd" | sudo tee /etc/s6/sv/libvirtd/dependencies.d/virtlogd
```

### **7.4 Why This Works (Alpine Linux Approach)**

**Alpine Linux** (which uses s6/OpenRC, NO systemd) runs libvirt this way:

1. **No socket activation** - Services run directly, not triggered by sockets
2. **Explicit dependencies** - virtlogd → libvirtd → dnsmasq order
3. **Simple supervision** - s6-supervise just restarts if crash
4. **No D-Bus complexity** - Direct UNIX sockets for communication

**Reference**: Alpine uses OpenRC init scripts that we're adapting to s6

### **7.5 The One systemd Service We Keep**

`/etc/systemd/system/s6-svscan.service` (ONLY this one):

```ini
[Unit]
Description=s6 supervision tree (replaces systemd service management)
After=local-fs.target
DefaultDependencies=no

[Service]
Type=simple
ExecStart=/usr/bin/s6-svscan /service
Restart=always
RestartSec=5
KillMode=process
# This is the ONLY systemd service that matters
# Everything else is supervised by s6

[Install]
WantedBy=multi-user.target
```

### **7.6 Verify systemd is Minimal**

```bash
# Show all active systemd services
systemctl list-units --type=service --state=running

# Should see:
# - s6-svscan.service (our one service)
# - System essentials only (no libvirtd, no app services)

# Show what s6 is managing
s6-svstat /service/*

# Should see:
# - virtlogd
# - libvirtd
# - grain6
# - grainwifi
# - All app services
```

### **7.7 Benefits of This Approach**

✅ **Minimal systemd**: Only boots kernel and starts s6  
✅ **s6 supervises everything**: All app/system services  
✅ **No socket activation complexity**: Direct service start  
✅ **Faster restarts**: s6 immediately restarts failed services  
✅ **Better logging**: Per-service s6-log directories  
✅ **Alpine-compatible**: Same approach as Alpine Linux  
✅ **grain6 integration**: Direct s6 API, no systemd translation  

### **7.8 Testing the Setup**

```bash
# Verify systemd only has s6-svscan
systemctl list-units --type=service --state=running | grep -v systemd | grep -v dbus

# Verify s6 has everything
s6-svstat /service/*

# Test libvirt works
virsh list --all

# Test VM creation
virt-manager
```

---

## 📊 **Comparison: systemd vs s6**

### **systemd**
- ✅ Built into Ubuntu
- ✅ Rich feature set
- ❌ Complex configuration
- ❌ Heavy resource usage
- ❌ Hard to integrate custom supervision logic

### **s6**
- ✅ Lightweight (minimal overhead)
- ✅ Simple, predictable behavior
- ✅ Easy to integrate with grain6
- ✅ Per-service logging
- ✅ Fast restart times
- ❌ Requires manual setup on Ubuntu
- ❌ Less familiar to most admins

### **Hybrid Approach** (Our Strategy)
- systemd starts s6-svscan at boot (PID 1 compatibility)
- s6 manages all application services
- Best of both worlds!

---

## 🌟 **Benefits for Grain Network**

1. **grain6 Integration**: s6's simple API perfect for time-aware supervision
2. **Fast Restarts**: Services restart instantly without systemd overhead
3. **Better Logging**: Per-service log directories with rotation
4. **Consistent Management**: Same s6 commands work on all systems
5. **Resource Efficiency**: Lower memory and CPU usage than systemd

---

## 🛠️ **Troubleshooting**

### **Service Won't Start**

```bash
# Check run script is executable
ls -la /etc/s6/sv/grain6/run

# Check logs
tail -f /var/log/s6/grain6/current

# Test run script manually
sudo su - xy -c "/etc/s6/sv/grain6/run"
```

### **s6-svscan Not Running**

```bash
# Check systemd service
sudo systemctl status s6-svscan

# Restart s6-svscan
sudo systemctl restart s6-svscan

# Check processes
ps aux | grep s6-svscan
```

### **Permission Issues**

```bash
# Ensure service directories are writable
sudo chown -R xy:xy /var/log/s6/grain6

# Check chpst user switching
chpst -u xy:xy id
```

---

## 📚 **Resources**

### **s6 Documentation**
- **s6 Homepage**: https://skarnet.org/software/s6/
- **s6 Tutorial**: https://skarnet.org/software/s6/s6-svscan-1.html
- **Service Management**: https://skarnet.org/software/s6/s6-svc.html

### **Related Projects**
- **s6-rc**: Service dependencies and bundles
- **s6-linux-init**: Full s6-based init system
- **execline**: s6's scripting language

### **Grain Network**
- **grain6**: `grainstore/grain6/README.md`
- **clojure-s6**: `grainstore/clojure-s6/README.md`

---

## 🌾 **Philosophy**

**"s6: Simple, Small, Secure Supervision"**

- **Simple**: Easy to understand, predict, and debug
- **Small**: Minimal resource usage, fast operation
- **Secure**: Privilege separation, clear boundaries

**From systemd to s6**: Not replacement, but enhancement. Use systemd for boot, s6 for services.

---

**Status**: 🌱 Ready for Implementation  
**Integration**: grain6 + s6 + Ubuntu 24.04 LTS  

🌾 **now == next + 1** (but make it supervised, chief!) 🛡️
