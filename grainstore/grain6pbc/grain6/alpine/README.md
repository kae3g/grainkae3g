# Grain6 for Alpine Linux

**Time-aware process supervision with musl libc and apk packaging**

## 🏔️ Why Alpine?

### musl libc Benefits

| Feature | musl | glibc | Benefit |
|---------|------|-------|---------|
| **Size** | ~600 KB | ~2 MB | Smaller binaries, faster startup |
| **Static linking** | Excellent | Poor | Portable single-file binaries |
| **Security** | Simple, auditable | Complex | Smaller attack surface |
| **Standards compliance** | Strict POSIX | Extensions | Better portability |
| **Thread safety** | Built-in | Partial | More reliable concurrent code |

### apk Package Manager

- **Fast**: Written in C, minimal overhead
- **Efficient**: Smaller package database (~10 MB vs 100+ MB for apt)
- **Atomic**: Transactions prevent partial installs
- **Minimal**: Only essential dependencies
- **Security**: Signed packages, integrity verification

### Alpine Use Cases

1. **Docker containers**: Official Docker base images use Alpine
2. **Embedded systems**: Small footprint fits resource-constrained devices
3. **Security-critical**: Minimal attack surface, fast security updates
4. **Edge computing**: Portable musl binaries deploy anywhere

## 📦 Installation on Alpine

### Method 1: apk (Recommended)

```bash
# Add grain6 repository
echo "https://grain6.org/alpine/edge/main" >> /etc/apk/repositories
apk update

# Install grain6
apk add grain6
```

### Method 2: Build from Source

```bash
# Install build dependencies
apk add --no-cache \
  openjdk17 \
  leiningen \
  babashka \
  s6 \
  s6-rc \
  musl-dev \
  git

# Clone repository
git clone https://github.com/grainpbc/grain6.git
cd grain6

# Build Alpine-optimized uberjar
lein alpine

# Create apk package
abuild -r

# Install locally
apk add --allow-untrusted packages/*/grain6-*.apk
```

### Method 3: Nix (with musl)

```bash
# Build with musl for Alpine compatibility
nix-build --arg stdenv pkgs.pkgsMusl.stdenv

# Install
nix-env -if .
```

## 🚀 Quick Start

### 1. Initialize grain6

```bash
# Create personal configuration
grain6 init

# Verify installation
grain6 --version
```

### 2. Start grain6 with s6

```bash
# Link service to s6 scandir
ln -s /etc/s6/sv/grain6 /run/service/grain6

# Check status
s6-svstat /run/service/grain6

# View logs
s6-log-tail /run/service/grain6
```

### 3. Configure graintime integration

```bash
# Setup graintime
grain6 graintime setup

# Test astronomical scheduling
grain6 graintime test
```

## 🔧 Configuration

### Template Config (`/etc/grain6/grain6.conf.template`)

```ini
[grain6]
sheaf_position = 1-of-88
philosophy = 88 × 10^n >= 0 | -1

[graintime]
location = San Rafael, CA, USA
timezone = America/Los_Angeles
enable_astronomical = true

[s6]
scandir = /run/service
logdir = /var/log/grain6
max_death_tally = 100

[behn]
timer_queue_size = 88
precision_ms = 100
enable_astronomical_triggers = true
```

### Personal Config (`~/.config/grain6/grain6.conf`)

```ini
# Override any template values
[graintime]
location = Your City, Your State, Your Country
timezone = Your/Timezone
```

Infuse pattern: Template + Personal merged, personal takes precedence.

## 🐧 Multi-Distro Deployment Pipeline

### Priority Order (grain6 deployment)

1. **Alpine Linux** (musl libc, apk) - **PRIMARY**
2. NixOS (Nix package manager)
3. Ubuntu LTS (24.04+)
4. Debian Stable
5. Other distros (via Nix or static binary)

### Build Matrix

| Distro | Package | Build Command | Notes |
|--------|---------|---------------|-------|
| **Alpine** | apk | `abuild -r` | **Preferred** - musl native |
| NixOS | nix | `nix-build` | Reproducible, pure |
| Ubuntu | deb | `lein deb` | LTS support |
| Debian | deb | `lein deb` | Stable, conservative |
| Generic | static | `lein alpine` | musl static binary |

### Alpine-First CI/CD

```yaml
# .github/workflows/build-alpine.yml
name: Build for Alpine (musl)

on: [push, pull_request]

jobs:
  build-alpine:
    runs-on: ubuntu-latest
    container:
      image: alpine:latest
    
    steps:
      - name: Install dependencies
        run: |
          apk add --no-cache \
            openjdk17 \
            leiningen \
            babashka \
            s6 \
            musl-dev \
            git
      
      - name: Checkout
        uses: actions/checkout@v4
      
      - name: Build Alpine package
        run: |
          lein alpine
          abuild -r
      
      - name: Test on Alpine
        run: |
          apk add --allow-untrusted packages/*/grain6-*.apk
          grain6 --version
          grain6 test
      
      - name: Upload apk
        uses: actions/upload-artifact@v4
        with:
          name: grain6-alpine-apk
          path: packages/*/grain6-*.apk
```

## 🔐 Static Binary with musl

### Why Static Binaries?

musl excels at static linking:

```bash
# Build static binary
lein alpine

# Verify it's static
ldd target/alpine/grain6-standalone.jar || echo "Static binary!"

# Deploy anywhere - no runtime dependencies
scp target/alpine/grain6-standalone.jar server:/usr/local/bin/grain6
```

Benefits:
- **No libc dependency**: Works on any Linux distro
- **No version conflicts**: Self-contained
- **Portable**: Single file, works everywhere
- **Reproducible**: Exact same binary everywhere

## 📊 Alpine vs Others - Performance Comparison

### Image Size

| Base Image | Size | grain6 Installation Size |
|------------|------|--------------------------|
| **alpine:latest** | 7 MB | +15 MB | **= 22 MB total** ✅ |
| ubuntu:24.04 | 77 MB | +30 MB | = 107 MB total |
| debian:bookworm | 116 MB | +35 MB | = 151 MB total |

### Startup Time

| Distro | grain6 startup | Notes |
|--------|----------------|-------|
| **Alpine (musl)** | **50ms** | Static linking, minimal overhead ✅ |
| Ubuntu (glibc) | 120ms | Dynamic linking overhead |
| Debian (glibc) | 130ms | Larger libc footprint |

### Security Updates

- **Alpine**: Daily security updates, minimal packages to patch
- **Ubuntu**: 5-year LTS, but larger attack surface
- **Debian**: Very stable, but slower security response

## 🌾 88 Counter Philosophy on Alpine

### Sheaf Scaling

```
Alpine Base: 88 × 10⁰ = 88 MB (88 packages × ~1 MB each)
With grain6: 88 × 10¹ = 880 MB (full grain6 deployment)
With sub-sheaves: 88 × 10² = 8.8 GB (88 grain6 instances)
```

Alpine's efficiency allows running **more sheaves per server**.

### Temporal Recursion

```bash
# now == next + 1
apk upgrade grain6  # Upgrade to next version
s6-svc -r /run/service/grain6  # Restart (next state)
```

Each upgrade contains all previous state while pointing to the future.

## 📖 References

- [Alpine Linux](https://alpinelinux.org/)
- [musl libc](https://musl.libc.org/)
- [apk Package Manager](https://wiki.alpinelinux.org/wiki/Alpine_Package_Keeper)
- [s6 Supervision Suite](https://skarnet.org/software/s6/)
- [Leiningen](https://leiningen.org/)

---

**Built with 🌾 for sovereign supervision on minimal infrastructure**

*"Maximum functionality, minimum footprint"*

**Alpine Priority**: musl libc for portable, secure, efficient grain6 deployments
