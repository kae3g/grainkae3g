# Grainstore Vendor Dependencies

**Status**: Phase 1 - Seed Collection  
**Purpose**: Vendor critical dependencies for complete sovereignty

## What is Vendoring?

**Vendoring** = Including dependencies directly in your repository (as git submodules).

### Why Vendor?

1. **Supply Chain Security**: No upstream breakage
2. **Reproducibility**: Lock exact versions
3. **Offline Builds**: No network required
4. **Auditing**: Review all source code
5. **Century-Scale**: Upstream projects may vanish

## Vendored Dependencies

### 1. s6 - Process Supervision

- **Repository**: https://github.com/skarnet/s6
- **Size**: ~200KB
- **Purpose**: Process supervision and service management
- **License**: ISC (permissive)
- **Why**: Core init system for SixOS
- **Nock Spec**: `../specs/s6.nock.md`
- **Clojure Impl**: `../../src/grainstore/s6.clj`

**What we vendor**:
- All s6 tools (s6-svc, s6-supervise, s6-svscan, etc.)
- Complete C source code
- Build scripts and documentation

### 2. musl - C Standard Library

- **Repository**: https://github.com/bminor/musl
- **Size**: ~1MB
- **Purpose**: Minimal, correct, standards-compliant libc
- **License**: MIT
- **Why**: Foundation for static linking and portability
- **Nock Spec**: TODO - `../specs/musl.nock.md`

**What we vendor**:
- All musl source code
- System call wrappers
- Standard library functions (malloc, printf, etc.)
- Build system

**Why musl over glibc**:
- **Smaller**: 1MB vs 10MB+
- **Simpler**: Clean, readable code
- **Static linking**: Easy to build standalone binaries
- **Correctness**: Fewer bugs, better standards compliance

### 3. wayland - Display Protocol

- **Repository**: https://gitlab.freedesktop.org/wayland/wayland
- **Size**: ~500KB
- **Purpose**: Display server protocol and client library
- **License**: MIT
- **Why**: Modern, secure display system for Linux
- **Nock Spec**: `../specs/wayland.nock.md` ✅

**What we vendor**:
- libwayland-server (compositor library)
- libwayland-client (application library)
- Protocol XML definitions
- Scanner tool (generates C bindings)

**Why Wayland over X11**:
- **Security**: No global input/output access
- **Performance**: Direct rendering, less overhead
- **Simplicity**: ~20K lines vs X11's 250K+
- **Modern**: GPU compositing, touch gestures

## How to Use

### Building s6

```bash
cd grainstore/vendor/s6
./configure --prefix=/usr/local
make
sudo make install
```

### Building musl

```bash
cd grainstore/vendor/musl
./configure --prefix=/usr/local/musl
make
sudo make install
```

### Building wayland

```bash
cd grainstore/vendor/wayland
meson build
ninja -C build
sudo ninja -C build install
```

## Grainstore Build Integration

**Eventually, we'll build everything through Nix**:

```nix
# grainstore/default.nix
{ stdenv, ... }:

stdenv.mkDerivation {
  name = "grainstore-s6";
  src = ./vendor/s6;
  
  buildInputs = [ ];
  
  buildPhase = ''
    ./configure --prefix=$out
    make
  '';
  
  installPhase = ''
    make install
  '';
}
```

**Then use in SixOS**:
```nix
{ pkgs, ... }:

{
  environment.systemPackages = with pkgs; [
    grainstore.s6
    grainstore.musl
    grainstore.wayland
  ];
}
```

## Verification Strategy

For each vendored dependency:

1. **Nock Specification** - Mathematical definition
2. **Clojure Implementation** - Pure, verifiable code
3. **Test Suite** - Comprehensive assertions
4. **Equivalence Proof** - Clojure ↔ Nock ↔ C
5. **Jets** - Optimized implementations with proofs

### Current Status:

| Dependency | Nock Spec | Clojure Impl | Tests | Equivalence | Jets |
|------------|-----------|--------------|-------|-------------|------|
| s6         | ✅        | ✅           | ✅    | ✅          | TODO |
| runit      | ✅        | ✅           | ✅    | ✅          | TODO |
| musl       | TODO      | TODO         | TODO  | TODO        | TODO |
| wayland    | ✅        | TODO         | TODO  | TODO        | TODO |

## Adding More Dependencies

### Candidates for Vendoring:

**Tier 1 (Critical)**:
- ✅ s6 (process supervision)
- ✅ musl (C library)
- ✅ wayland (display protocol)

**Tier 2 (Important)**:
- [ ] dbus (IPC)
- [ ] udev (device management)
- [ ] mesa (OpenGL/Vulkan)
- [ ] pipewire (audio)

**Tier 3 (Useful)**:
- [ ] libinput (input devices)
- [ ] fontconfig (font management)
- [ ] cairo (2D graphics)
- [ ] pango (text layout)

### Criteria for Vendoring:

1. **Size < 5MB** - Must be reasonably small
2. **Clear license** - Permissive (MIT, ISC, Apache)
3. **Active upstream** - Or small enough to maintain ourselves
4. **Critical path** - Required for boot or core functionality
5. **Formally specifiable** - Can be written in Nock

## Philosophy: The Grainstore

**Think of this like a seed vault**:

- **Svalbard Seed Vault** - Stores seeds for all crops
- **Grainstore** - Stores "seeds" (source code) for all software

**Why this matters**:

1. **Century-scale thinking** - Software should last 100+ years
2. **Dependency hell** - Upstream projects break, disappear, or change
3. **Trust** - We can audit all code ourselves
4. **Sovereignty** - Complete control over our stack

**The agricultural metaphor**:

```
Seeds (Nock specs)     → Planted (Clojure impl)  → Harvested (Tests pass)
     ↓                        ↓                           ↓
Stored (Vendored)      → Grown (Built)           → Eaten (Used in SixOS)
```

**From seed to harvest, we control the entire process.**

## Next Steps

### Phase 1: Seed Collection (Current)
- [✅] Vendor s6, musl, wayland
- [✅] Write Nock specs (s6, runit, wayland)
- [✅] Document vendoring strategy

### Phase 2: Adaptation (Next)
- [ ] Write Nock specs for musl (subset)
- [ ] Implement wayland.clj
- [ ] Comprehensive test suites

### Phase 3: Verification (Later)
- [ ] Equivalence proofs for all implementations
- [ ] Jets for performance-critical paths
- [ ] Integration testing with real workloads

### Phase 4: Integration (Future)
- [ ] Build all dependencies through Nix
- [ ] Boot SixOS using ONLY Grainstore components
- [ ] Formal verification of boot process

## Contributing

**If you want to add a dependency**:

1. Check criteria (size, license, criticality)
2. Add as git submodule: `git submodule add <repo> grainstore/vendor/<name>`
3. Write Nock spec: `grainstore/specs/<name>.nock.md`
4. Implement in Clojure: `src/grainstore/<name>.clj`
5. Write tests: `test/grainstore/<name>_test.clj`
6. Document: Update this README!

## References

- **s6**: https://skarnet.org/software/s6/
- **musl**: https://musl.libc.org/
- **wayland**: https://wayland.freedesktop.org/
- **Git submodules**: https://git-scm.com/book/en/v2/Git-Tools-Submodules
- **Svalbard Seed Vault**: https://www.seedvault.no/

---

**"The best time to plant a tree was 20 years ago. The second best time is now."**

The same applies to vendoring dependencies. Start building your Grainstore today! 🌱✨

