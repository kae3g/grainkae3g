# 🌾 musl libc Integration for s6 on Ubuntu

> **"Alpine's Secret Sauce: musl libc for Security, Speed, and Simplicity"**

Using musl libc instead of glibc for s6 and Grain Network services on Ubuntu 24.04 LTS.

---

## 📋 **Why musl?**

**Philosophy**: Alpine Linux uses musl for good reasons - we should too!

### **musl Benefits**
✅ **Smaller binaries**: 50-80% smaller than glibc  
✅ **Static linking friendly**: Perfect for portable binaries  
✅ **Better security**: Simpler codebase = fewer vulnerabilities  
✅ **Faster compilation**: Quicker build times  
✅ **Deterministic**: Reproducible builds  
✅ **Memory efficient**: Lower runtime overhead  

### **glibc Drawbacks**
❌ **Large**: Bloated with legacy compatibility  
❌ **Dynamic linking complexity**: Symbol versioning issues  
✅ **Static linking discouraged**: NSS plugins break static builds  
❌ **Slower**: More overhead  

---

## 🎯 **Strategy: Three Approaches**

We have three ways to use musl on Ubuntu:

### **Approach 1: Build from Source with musl-gcc** (Recommended for s6)
Compile s6 and grain tools with musl, creating static binaries

### **Approach 2: Use Alpine APK Packages** (Quick & Easy)
Extract pre-built musl binaries from Alpine Linux packages

### **Approach 3: Run in Alpine Container** (Full Isolation)
Run s6 services inside Alpine LXC/Docker containers

---

## 🔧 **Approach 1: Build s6 with musl-gcc**

### **1.1 Install musl Development Tools**

```bash
# Install musl compiler and tools
sudo apt update
sudo apt install -y musl-tools musl-dev

# Verify installation
musl-gcc --version
# Expected: musl-gcc (Ubuntu ...) ...
```

### **1.2 Build s6 with musl**

```bash
# Install build dependencies
sudo apt install -y build-essential wget

# Download s6 and dependencies
cd /tmp
wget https://skarnet.org/software/skalibs/skalibs-2.14.0.0.tar.gz
wget https://skarnet.org/software/execline/execline-2.9.4.0.tar.gz
wget https://skarnet.org/software/s6/s6-2.12.0.1.tar.gz

# Build skalibs (s6 dependency) with musl
tar xzf skalibs-2.14.0.0.tar.gz
cd skalibs-2.14.0.0
./configure \
  --prefix=/opt/s6-musl \
  --enable-static-libc \
  CC=musl-gcc
make
sudo make install

# Build execline with musl
cd /tmp
tar xzf execline-2.9.4.0.tar.gz
cd execline-2.9.4.0
./configure \
  --prefix=/opt/s6-musl \
  --with-sysdeps=/opt/s6-musl/lib/skalibs/sysdeps \
  --with-include=/opt/s6-musl/include \
  --with-lib=/opt/s6-musl/lib \
  --enable-static-libc \
  CC=musl-gcc
make
sudo make install

# Build s6 with musl
cd /tmp
tar xzf s6-2.12.0.1.tar.gz
cd s6-2.12.0.1
./configure \
  --prefix=/opt/s6-musl \
  --with-sysdeps=/opt/s6-musl/lib/skalibs/sysdeps \
  --with-include=/opt/s6-musl/include \
  --with-lib=/opt/s6-musl/lib \
  --enable-static-libc \
  --enable-allstatic \
  CC=musl-gcc
make
sudo make install
```

### **1.3 Verify Static musl Binaries**

```bash
# Check that binaries are static and use musl
file /opt/s6-musl/bin/s6-svscan
# Expected: statically linked, not dynamic

ldd /opt/s6-musl/bin/s6-svscan
# Expected: "not a dynamic executable" or "statically linked"

# Check size comparison
ls -lh /usr/bin/s6-svscan /opt/s6-musl/bin/s6-svscan
# musl version should be significantly smaller!
```

### **1.4 Use musl s6 Binaries**

Update systemd service to use musl binaries:

```ini
# /etc/systemd/system/s6-svscan-musl.service
[Unit]
Description=s6 supervision tree (musl-linked static binaries)
After=local-fs.target
DefaultDependencies=no

[Service]
Type=simple
# Use musl-built s6-svscan
ExecStart=/opt/s6-musl/bin/s6-svscan /service
Environment="PATH=/opt/s6-musl/bin:/usr/bin:/bin"
Restart=always
RestartSec=5
KillMode=process

[Install]
WantedBy=multi-user.target
```

---

## 📦 **Approach 2: Use Alpine APK Packages**

Extract pre-built musl binaries from Alpine Linux packages!

### **2.1 Install apk-tools on Ubuntu**

```bash
# Install apk-tools statically
cd /tmp
wget https://gitlab.alpinelinux.org/api/v4/projects/5/packages/generic/v2.14.0/x86_64/apk.static
sudo install -m755 apk.static /usr/local/bin/apk
```

### **2.2 Set Up Alpine Package Repository**

```bash
# Create Alpine root directory
sudo mkdir -p /opt/alpine

# Initialize Alpine repository
sudo apk --root /opt/alpine --arch x86_64 --initdb add alpine-base

# Add Alpine repository
echo "https://dl-cdn.alpinelinux.org/alpine/latest-stable/main" | \
  sudo tee /opt/alpine/etc/apk/repositories
```

### **2.3 Install s6 from Alpine**

```bash
# Install s6 and dependencies via APK
sudo apk --root /opt/alpine add s6 s6-rc

# s6 binaries are now in /opt/alpine/usr/bin/
ls -la /opt/alpine/usr/bin/s6-*

# These are musl-linked static binaries!
file /opt/alpine/usr/bin/s6-svscan
ldd /opt/alpine/usr/bin/s6-svscan
```

### **2.4 Create Symlinks**

```bash
# Make Alpine s6 binaries available system-wide
sudo mkdir -p /opt/s6-alpine
sudo ln -sf /opt/alpine/usr/bin/s6-* /opt/s6-alpine/
sudo ln -sf /opt/alpine/bin/execlineb /opt/s6-alpine/

# Update PATH
echo 'export PATH="/opt/s6-alpine:$PATH"' | sudo tee /etc/profile.d/s6-alpine.sh
```

---

## 🐳 **Approach 3: Alpine Container for s6**

Run s6 supervision inside an Alpine Linux container (full musl environment).

### **3.1 Install LXC**

```bash
sudo apt install -y lxc lxc-templates

# Create Alpine container
sudo lxc-create -n s6-alpine -t alpine

# Start container
sudo lxc-start -n s6-alpine

# Enter container
sudo lxc-attach -n s6-alpine
```

### **3.2 Inside Alpine Container**

```bash
# Update and install s6
apk update
apk add s6 s6-rc

# Configure s6 services (same as Ubuntu setup)
mkdir -p /etc/s6/sv/grain6
# ... create service directories ...
```

### **3.3 Mount Host Directories**

Edit `/var/lib/lxc/s6-alpine/config`:

```ini
# Mount grainkae3g directory
lxc.mount.entry = /home/xy/kae3g/grainkae3g grainkae3g none bind 0 0
```

---

## 🌾 **Approach 4: Grain Network Services with musl**

### **4.1 Compile Babashka with musl** (Advanced)

```bash
# Babashka supports native compilation
# We can build a musl-linked version

cd /tmp
git clone https://github.com/babashka/babashka
cd babashka

# Build with musl (requires GraalVM)
export BABASHKA_MUSL=true
script/compile
```

**Alternative**: Use pre-built musl Babashka from releases:
```bash
wget https://github.com/babashka/babashka/releases/download/v1.x.x/babashka-x.x.x-linux-amd64-static.tar.gz
```

### **4.2 Clojure with musl**

Clojure runs on JVM, but we can use a musl-compatible JVM:

```bash
# Use Liberica NIK (Native Image Kit) with musl
wget https://download.bell-sw.com/vm/.../bellsoft-liberica-vm-musl-...

# Or use GraalVM native-image with musl
export GRAALVM_HOME=/path/to/graalvm-musl
```

---

## 📊 **Comparison: glibc vs musl Binaries**

### **Size Comparison (s6-svscan)**
```
glibc (dynamic): ~45 KB binary + ~2 MB libc.so
musl (static):   ~85 KB binary (self-contained!)

Total:
glibc: ~2.045 MB
musl:  ~0.085 MB

Savings: 96% smaller!
```

### **Security Comparison**
```
glibc: ~1.2 million lines of code
musl:  ~70,000 lines of code

Attack surface: 94% reduction!
```

### **Performance**
```
musl:  Slightly faster malloc/free
glibc: Slightly faster string operations

Overall: Similar, musl often wins in embedded/minimal scenarios
```

---

## 🎯 **Recommended Setup for Grain Network**

### **Hybrid Approach: Best of Both Worlds**

```
┌─────────────────────────────────────────────┐
│  System Layer (glibc)                       │
│  - Ubuntu base system                       │
│  - GNOME desktop                            │
│  - Large apps (Firefox, etc.)               │
└──────────────┬──────────────────────────────┘
               │
┌──────────────┴──────────────────────────────┐
│  Supervision Layer (musl)                   │
│  - s6-svscan (static musl)                  │
│  - s6-supervise (static musl)               │
│  - execline (static musl)                   │
└──────────────┬──────────────────────────────┘
               │
┌──────────────┴──────────────────────────────┐
│  Services (mixed)                           │
│  - libvirtd (glibc, system package)         │
│  - grain6 (musl babashka)                   │
│  - grainwifi (musl babashka)                │
│  - Custom services (musl when possible)     │
└─────────────────────────────────────────────┘
```

**Why This Works**:
- **s6 supervision**: musl-static (portable, secure, tiny)
- **System services**: Use existing Ubuntu packages (glibc)
- **Grain services**: musl babashka (portable, fast)
- **Best of both**: Security + Compatibility

---

## 🚀 **Quick Start: Install musl s6**

### **Option 1: Build from Source** (15 minutes)

```bash
# One-command build
curl -fsSL https://raw.githubusercontent.com/just-containers/s6-overlay/master/installer | \
  sudo bash -s -- install -p /opt/s6-musl

# Or manual build (see section 1.2 above)
```

### **Option 2: Use Alpine APK** (5 minutes)

```bash
# Install apk-tools
wget https://gitlab.alpinelinux.org/api/v4/projects/5/packages/generic/v2.14.0/x86_64/apk.static
sudo install -m755 apk.static /usr/local/bin/apk

# Create Alpine rootfs
sudo mkdir -p /opt/alpine
sudo apk --root /opt/alpine --arch x86_64 --initdb add s6

# Use binaries
export PATH="/opt/alpine/usr/bin:$PATH"
```

### **Option 3: Pre-built Static Binaries** (1 minute)

```bash
# Download s6-overlay (pre-built musl binaries)
wget https://github.com/just-containers/s6-overlay/releases/download/v3.x.x.x/s6-overlay-noarch.tar.xz
wget https://github.com/just-containers/s6-overlay/releases/download/v3.x.x.x/s6-overlay-x86_64.tar.xz

# Extract to /opt/s6-musl
sudo tar -C /opt/s6-musl -Jxpf s6-overlay-noarch.tar.xz
sudo tar -C /opt/s6-musl -Jxpf s6-overlay-x86_64.tar.xz
```

---

## 🔍 **Verification Commands**

```bash
# Check if binary uses musl
file /opt/s6-musl/bin/s6-svscan

# Check static linking
ldd /opt/s6-musl/bin/s6-svscan
# Should say: "not a dynamic executable" or "statically linked"

# Compare sizes
ls -lh /usr/bin/s6-svscan /opt/s6-musl/bin/s6-svscan

# Test it works
/opt/s6-musl/bin/s6-svscan --help
```

---

## 📚 **Resources**

### **musl libc**
- **musl Homepage**: https://musl.libc.org/
- **musl vs glibc**: https://wiki.musl-libc.org/functional-differences-from-glibc.html

### **Alpine Linux**
- **APK Tools**: https://wiki.alpinelinux.org/wiki/Alpine_Package_Keeper
- **Alpine Packages**: https://pkgs.alpinelinux.org/packages

### **s6 with musl**
- **skarnet s6**: https://skarnet.org/software/s6/
- **s6-overlay**: https://github.com/just-containers/s6-overlay (pre-built musl)

### **Grain Network**
- **Alpine preference**: See memory about musl/apk for grain6

---

## 🌾 **Philosophy**

**"musl: The Alpine Way"**

Alpine Linux chose musl for their entire distribution. We're bringing that philosophy to Ubuntu:

- **Smaller**: Less disk space, less memory
- **Simpler**: Fewer moving parts = fewer bugs
- **Safer**: Smaller attack surface
- **Faster**: Lower overhead
- **Portable**: Static binaries work everywhere

**"Use musl where it matters, glibc where it doesn't"**

We don't need to replace everything - just supervision and Grain services benefit most from musl.

---

**Status**: 🌱 Ready to implement musl s6 supervision  
**Recommendation**: Use Alpine APK approach (fastest, easiest)  
**Alternative**: Build from source for maximum control  

🌾 **now == next + 1** (but make it musl, chief!) 🏔️
