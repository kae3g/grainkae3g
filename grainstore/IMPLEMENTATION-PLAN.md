# Grainstore: Implementation Plan

**Phase**: Specification → Verification  
**Start Date**: 2025-10-12  
**Target**: Framework 16 laptop running SixOS from grainstore  
**Duration**: 8-12 weeks (realistic timeline)

---

## Vision: Sovereign Computing Stack

Build a **completely verifiable, eternally regenerable** computing system from frozen specifications.

### Success Criteria
✅ Boot Framework 16 laptop running SixOS  
✅ All dependencies built from grainstore sources  
✅ No external registries (npm, cargo.io, etc.)  
✅ Nock specifications for critical components  
✅ Clojure↔Nock equivalence verified  
✅ Full regenesis possible 10 years from now

---

## Current State (2025-10-12)

### ✅ What We Have
- **Directory structure**: `specs/`, `equivalence/`, `vendor/`, `jets/`, `verified/`
- **s6 Nock spec**: Draft v0.1 complete (380 lines)
- **runit Nock spec**: Started
- **wayland Nock spec**: Stub
- **Clojure s6 impl**: Exists in `src/grainstore/s6.clj`
- **Philosophy**: Documented in README, GRAINSTORE-STRATEGY.md

### ⚠️ What We Need
- **Verification**: Clojure Spec for s6 API
- **Property tests**: test.check for equivalence
- **Jets**: Identify hot paths for optimization
- **Vendored C code**: s6, runit, musl source code
- **Nix integration**: Build system for reproducibility
- **Real deployment**: Test on actual hardware (Framework 16)

---

## The 8-Week Roadmap

### **Weeks 1-2: Specification Completion** 📝

**Goal**: Complete Nock specs for Layer 1 dependencies

#### s6 (Process Supervision)
- [x] Core state transitions (start, stop, restart)
- [ ] Complete supervision tree operations
- [ ] Signal handling (SIGTERM, SIGKILL, SIGHUP)
- [ ] Log rotation and management
- [ ] Notification protocol (readiness)
- [ ] Dependency ordering

#### runit (Init System)
- [ ] Complete spec from stub
- [ ] Stage 1: System initialization
- [ ] Stage 2: Service supervision (delegates to s6)
- [ ] Stage 3: Shutdown sequence
- [ ] runsvdir integration

#### musl-libc (C Library)
- [ ] Create initial Nock spec
- [ ] Focus on syscall interface
- [ ] Memory allocation primitives
- [ ] String operations
- [ ] File I/O basics

**Deliverable**: Three complete Nock specifications with:
- State representations as nouns
- Operations as Nock formulas (Rules 0-11)
- Clojure equivalents
- Proof obligations documented

---

### **Weeks 3-4: Verification Infrastructure** ✅

**Goal**: Build the verification pipeline

#### Clojure Spec
```clojure
;; src/grainstore/s6_spec.clj
(ns grainstore.s6-spec
  (:require [clojure.spec.alpha :as s]
            [grainstore.s6 :as s6]))

(s/def ::service-name string?)
(s/def ::state #{:down :up :finishing :ready})
(s/def ::pid nat-int?)
(s/def ::timestamp inst?)
(s/def ::service (s/keys :req-un [::service-name ::state ::pid ::timestamp]))

(s/fdef s6/start-service
  :args (s/cat :service ::service)
  :ret ::service
  :fn (fn [{:keys [args ret]}]
        (or (= (:state ret) :up)
            (= ret (:service args)))))  ; Idempotent
```

#### Property-Based Testing
```clojure
;; test/grainstore/s6_test.clj
(ns grainstore.s6-test
  (:require [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]))

(def service-gen
  (gen/hash-map
    :service-name gen/string-alphanumeric
    :state (gen/elements [:down :up :finishing :ready])
    :pid gen/nat
    :timestamp gen/pos-int))

(def prop-start-idempotent
  (prop/for-all [svc service-gen]
    (= (s6/start-service (s6/start-service svc))
       (s6/start-service svc))))

(tc/quick-check 1000 prop-start-idempotent)
```

#### Equivalence Notes
- [ ] Document Clojure↔Nock mapping patterns
- [ ] Create equivalence-note.nock.md for s6
- [ ] Verify state transitions match
- [ ] Test edge cases (crash recovery, race conditions)

**Deliverable**: Working verification suite with:
- Clojure Spec for all APIs
- 1000+ property tests passing
- Equivalence notes documenting Clojure↔Nock correspondence

---

### **Weeks 5-6: Isolation & Vendoring** 📦

**Goal**: Achieve complete dependency sovereignty

#### Vendor C Source Code
```bash
# grainstore/vendor/
grainstore/vendor/
├── s6/
│   ├── s6-2.12.0.5.tar.gz
│   ├── CHECKSUMS
│   └── BUILD.md
├── musl/
│   ├── musl-1.2.4.tar.gz
│   ├── CHECKSUMS
│   └── BUILD.md
└── wayland/
    ├── wayland-1.22.0.tar.xz
    ├── CHECKSUMS
    └── BUILD.md
```

#### Nix Flake for Reproducibility
```nix
# grainstore/flake.nix
{
  description = "Grainstore: Sovereign dependency repository";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
  };

  outputs = { self, nixpkgs }: {
    packages.x86_64-linux = {
      s6-grainstore = pkgs.stdenv.mkDerivation {
        name = "s6-grainstore";
        src = ./vendor/s6/s6-2.12.0.5.tar.gz;
        # Build from vendored source
        # Verify against Nock spec
        # Run property tests
      };
    };
  };
}
```

#### Verification Integration
- [ ] Add Nock verification to build pipeline
- [ ] Fail build if Clojure↔Nock equivalence breaks
- [ ] Generate verification artifacts in `verified/`
- [ ] Create reproducible build hashes

**Deliverable**: Complete isolation with:
- All source code vendored locally
- Reproducible Nix builds
- No external network dependencies
- Verification integrated into build

---

### **Weeks 7-8: Real Deployment & Testing** 🚀

**Goal**: Boot Framework 16 from grainstore

#### SixOS Integration
```nix
# sixos/configuration.nix (updated)
{ config, pkgs, grainstore, ... }:

{
  imports = [ ./hardware-configuration.nix ];

  # Use grainstore s6 instead of upstream
  services.s6-grainstore = {
    enable = true;
    package = grainstore.packages.x86_64-linux.s6-grainstore;
  };

  # Verify Nock specs at boot
  boot.postBootCommands = ''
    ${grainstore}/bin/verify-s6-spec
  '';
}
```

#### Hardware Testing Checklist
- [ ] Framework 16 bare metal install
- [ ] Boot to minimal s6-supervised system
- [ ] Verify all services start correctly
- [ ] Test crash recovery (kill -9 processes)
- [ ] Test supervision tree (dependent services)
- [ ] Measure boot time (target: <5 seconds to userspace)
- [ ] Document any discrepancies between spec and reality

#### Field Log Template
```markdown
# Grainstore Field Log: Framework 16 Deployment

**Date**: YYYY-MM-DD  
**Hardware**: Framework 16, AMD Ryzen 9 7940HS, 32GB RAM  
**Software**: SixOS with grainstore s6 v2.12.0.5  

## Pre-Boot
- [ ] USB installer created
- [ ] BIOS settings verified (UEFI, Secure Boot off)
- [ ] Backup of existing system

## Installation
- Time to install: ____ minutes
- First boot successful: YES / NO
- Issues encountered: ____

## Service Verification
- [ ] dbus started
- [ ] networking up
- [ ] udev responding
- [ ] wayland compositor launching

## Performance
- Boot time: ____ seconds
- Memory usage at idle: ____ MB
- Supervision overhead: ____ %

## Nock Verification
- [ ] s6 spec verification passed
- [ ] Clojure equivalence tests passed
- [ ] No spec violations detected

## Issues & Resolutions
1. ____
2. ____

## Next Steps
- ____
```

**Deliverable**: Real deployment with:
- Framework 16 running SixOS
- All services from grainstore
- Field logs documenting reality vs specification
- Photos/videos of boot sequence
- Performance benchmarks

---

## Beyond Week 8: Layers 2-4

### Layer 2: System Services (Weeks 9-12)
- [ ] dbus (IPC messaging)
- [ ] udev (device management)
- [ ] networking (wpa_supplicant, dhcpcd, iwd)

### Layer 3: Wayland Stack (Weeks 13-16)
- [ ] wayland protocol (Nock spec)
- [ ] wlroots (compositor library)
- [ ] hyprland (tiling compositor)

### Layer 4: Applications (Weeks 17-20)
- [ ] wezterm (terminal emulator)
- [ ] brave (web browser)
- [ ] cursor (AI editor)

---

## Success Metrics

### Technical
- ✅ Boot Framework 16 from grainstore (Week 8)
- ✅ Zero external registries required
- ✅ All specs verified (Clojure↔Nock)
- ✅ Full regenesis from source (<1 hour build time)

### Philosophical
- ✅ Documented eternally (future generations can rebuild)
- ✅ Mathematically grounded (Nock as axiomatic base)
- ✅ Ecologically sound (closed loops, no extraction)
- ✅ Sovereignty achieved (no vendor lock-in)

### Educational
- ✅ Essay 9301: "Grainstore: The Seed Bank of Sovereignty"
- ✅ Video series: "Building from Frozen Specifications"
- ✅ Tutorial: "Your First Grainstore Dependency"

---

## Risks & Mitigations

### Risk 1: Specs Don't Match Reality
**Mitigation**: Start simple (s6), iterate based on real deployments

### Risk 2: Performance Too Slow
**Mitigation**: Jets for hot paths (identified via profiling)

### Risk 3: Too Complex to Maintain
**Mitigation**: Focus on Layer 1 first, expand only if viable

### Risk 4: Hardware Incompatibility
**Mitigation**: Framework 16 is well-supported on Linux; fallback to QEMU

### Risk 5: Time Commitment Too High
**Mitigation**: This is multi-session work; document everything for resumption

---

## Integration with Coldriver Tundra Essays

### New Essays to Write
- **Essay 9301**: "Grainstore: The Seed Bank of Sovereignty"
  - What it is, why it matters
  - Permaculture metaphor extended
  - Hilbert + Schauberger synthesis
  - Practical walkthrough of s6 spec
  
- **Essay 9302**: "Nock as Axiomatic Base"
  - Why 12 frozen rules
  - How to read Nock specifications
  - Comparison to other formal methods
  
- **Essay 9303**: "Jets: When Precision Needs Speed"
  - Identifying hot paths
  - C/Rust implementation strategies
  - Verification that jets match specs

### Integration Points
- **Essay 9298**: Add grainstore as third technical anchor
  - Codeberg (sovereign code hosting)
  - Helium (sovereign connectivity)
  - Grainstore (sovereign dependencies)
  
- **Essay 9514**: Update with grainstore s6 installation
  - Replace upstream s6 with grainstore version
  - Show verification at install time

---

## Next Immediate Actions

1. **Complete s6 Nock spec** (remaining sections)
   - Signal handling
   - Log management
   - Notification protocol
   
2. **Write Clojure Spec** for s6 API
   - `grainstore.s6-spec` namespace
   - Cover all public functions
   
3. **Create first property tests**
   - Idempotence (start, stop, restart)
   - State transitions
   - Crash recovery

4. **Vendor s6 source code**
   - Download s6-2.12.0.5.tar.gz
   - Verify checksums
   - Document build process

5. **Draft Essay 9301**
   - Introduce grainstore vision
   - Walk through s6 example
   - Connect to Coldwater Heal philosophy

---

## Weekly Check-ins

### Week 1 Review
- [ ] s6 spec complete?
- [ ] runit spec 50% complete?
- [ ] musl spec started?
- [ ] Blockers: ____

### Week 2 Review
- [ ] All Layer 1 specs complete?
- [ ] Clojure equivalents verified?
- [ ] Proof obligations documented?
- [ ] Blockers: ____

### Week 4 Review
- [ ] Verification pipeline working?
- [ ] 1000+ property tests passing?
- [ ] Equivalence notes complete?
- [ ] Blockers: ____

### Week 6 Review
- [ ] All source code vendored?
- [ ] Nix flake building successfully?
- [ ] Verification integrated?
- [ ] Blockers: ____

### Week 8 Review
- [ ] Framework 16 boot successful?
- [ ] All services from grainstore?
- [ ] Field logs documented?
- [ ] Blockers: ____

---

## Resources & References

### Nock
- [Nock Specification](https://urbit.org/docs/nock/definition/)
- [Nock Tutorial](https://blog.timsong.org/nock/)
- Essay 9503: "What is Nock?"

### s6
- [s6 Documentation](https://skarnet.org/software/s6/)
- [s6 Source Code](https://github.com/skarnet/s6)
- Essay 9514: "SixOS: s6-supervised computing"

### Formal Verification
- [Clojure Spec Guide](https://clojure.org/guides/spec)
- [test.check](https://github.com/clojure/test.check)
- [Coq Proof Assistant](https://coq.inria.fr/)

### Permaculture Metaphor
- Helen Atthowe's 78 Elements (Essay 9507)
- Seed saving as software sovereignty
- Grainhouse as knowledge preservation

---

## Questions for Exploration

1. **Can we prove s6 termination?** (Halting problem applies, but can we show bounded execution?)
2. **What Nock formula represents a supervision tree?** (Tree as nested cells)
3. **How do we verify jets match specs?** (Equivalence testing, formal proofs)
4. **Can we boot to Wayland in <5 seconds?** (Minimal init, parallel service starts)
5. **What's the TCB (Trusted Computing Base)?** (Nock interpreter, s6-svscan, kernel)

---

## Philosophy: Why This Matters

> **"The foundations must be eternal, or the structure will collapse within a human lifetime."**

Software today has a lifespan measured in months or years. Dependencies break. APIs change. Platforms die. We're building on quicksand.

**Grainstore changes this**:
- **Specifications** outlast implementations (Nock is forever)
- **Verification** proves correctness (not just testing)
- **Vendoring** eliminates external fragility (seed bank, not rental)
- **Sovereignty** means true ownership (no lock-in ever)

This is **Coldwater Heal** applied to dependencies:
- **Crystalline precision** (Nock specifications, formal proofs)
- **Ecological flow** (closed loops, eternal regeneration)
- **Community ownership** (shared knowledge, no gatekeepers)

---

## Commit to the Vision

This is multi-session work. Some weeks we'll make rapid progress. Some weeks we'll hit blockers. That's fine.

**What matters**:
- Document everything
- Test continuously
- Deploy to real hardware
- Learn from reality
- Iterate specifications
- Share knowledge openly

**The grainstore grows seed by seed. The work is patient. The harvest is eternal.** 🌱❄️

---

**Next**: Complete s6 Nock specification (Week 1, Day 1)

