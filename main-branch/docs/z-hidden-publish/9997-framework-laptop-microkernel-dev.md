# kae3g 9997: Framework Laptop Microkernel Development — A Beginner's Guide to Simple, Beautiful Linux Workflows

**Timestamp:** 12025-10-10--rhizome-valley  
**Series:** Technical Writings (9999 → 0000)  
**Category:** Hardware, Linux, Development Environment, Microkernel Systems  
**Reading Time:** 60 minutes

## The Question

*How do I set up a Framework 13 or Framework 16 laptop with AMD CPU/GPU for experimental microkernel-level development using the principles of simplicity and beauty described in our learning path (9998)?*

## Introduction: The Perfect Garden Tool

> **🔗 This essay connects theory to practice:**  
> From [9999: Clojure & Nix Ecosystem](9999-nixpkgs-clojure) you learned WHY init systems and package management matter.  
> From [9998: Learning Path](9998-learning-path-init-systems) you learned HOW to master them.  
> Now in 9997: Apply those concepts to REAL HARDWARE (Framework laptop).

In Helen Atthowe's ecological farm, the choice of tools matters. A good spade feels right in your hands, cuts cleanly through soil without excessive force, and lasts for generations with proper care. The Framework laptop is the spade of modern computing—repairable, upgradeable, respectful of your ownership.

Combined with the right Linux distribution and workflow, it becomes the ideal instrument for exploring the microkernels, init systems, and infuse.nix paradigms we've studied. This essay guides beginners through the entire journey—from hardware choice to daily workflow.

```clojure
{:the-framework-philosophy
 "Framework laptops embody:
  - Modularity (replaceable everything)
  - Ownership (you control your hardware)
  - Sustainability (repair instead of replace)
  - Transparency (no planned obsolescence)
  
  These values align perfectly with:
  - Microkernel design (modularity)
  - NixOS philosophy (declarative control)
  - Open source ethos (transparency)
  - Ecological farming (sustainability)"}
```

---

# Part I: Hardware Selection — Framework 13 vs Framework 16

## For Apprentices: Understanding Your Tool

### Framework 13 (13.5" Display)

```clojure
{:framework-13
 {:target-user
  "Solo developer, student, nomadic hacker"
  
  :specifications
  {:display "13.5\" 2256×1504 (3:2 aspect ratio)"
   :weight "~1.3 kg (2.87 lbs)"
   :battery "55 Wh"
   :ports "4× expansion card slots (USB-C, USB-A, HDMI, etc.)"
   :cpu-options
   ["AMD Ryzen 7 7840U (8-core, 16-thread)"
    "AMD Ryzen 5 7640U (6-core, 12-thread)"]}
  
  :amd-advantages
  "- Integrated Radeon graphics (no discrete GPU complexity)
   - Excellent Linux driver support (open-source)
   - Lower power consumption vs Intel
   - Better performance-per-watt
   - Full support in mainline kernel"
  
  :ideal-for
  ["Learning microkernel development (seL4, Redox OS)"
   "NixOS experimentation with infuse.nix"
   "Terminal-heavy workflows (s6, runit, OpenRC)"
   "Portable daily driver + dev machine"
   "Budget-conscious learners"]
  
  :limitations
  ["No discrete GPU (graphics-intensive work limited)"
   "Smaller screen (may want external monitor)"
   "Less RAM capacity (max 64GB)"]}
```

### Framework 16 (16" Display with Modular GPU)

```clojure
{:framework-16
 {:target-user
  "Power user, graphics developer, multi-monitor setup enthusiast"
  
  :specifications
  {:display "16\" 2560×1600 (16:10 aspect ratio)"
   :weight "~2.1 kg (4.6 lbs) without dGPU, ~2.4 kg with"
   :battery "85 Wh"
   :ports "6× expansion card slots"
   :cpu-options
   ["AMD Ryzen 9 7940HS (8-core, 16-thread)"
    "AMD Ryzen 7 7840HS (8-core, 16-thread)"]
   :gpu-module
   "Optional AMD Radeon RX 7700S (8GB GDDR6) - REMOVABLE"}
  
  :amd-advantages
  "- All AMD CPU benefits from Framework 13
   - Modular GPU: add/remove as needed
   - More expansion card flexibility
   - Larger cooling system (sustained performance)
   - Open-source GPU drivers (amdgpu kernel module)"
  
  :ideal-for
  ["Redox OS graphics development (orbital display server)"
   "Testing GPU passthrough to VMs (seL4 + graphics)"
   "Multi-monitor workflows (3-4 external displays)"
   "Long-term investment (more upgradeable)"
   "GPU compute experiments (ML inference, rendering)"]
  
  :limitations
  ["Heavier (less portable)"
   "More expensive"
   "GPU module adds cost and complexity"
   "Larger power brick needed with dGPU"]}
```

### Decision Matrix for Beginners

```clojure
{:choose-framework-13-if
 ["Budget: $1000-1500"
  "Portability is priority"
  "Primarily terminal/text-based development"
  "Learning seL4, SixOS, infuse.nix concepts"
  "Don't need GPU-intensive tasks"
  "Want simplest, most elegant solution"]
 
 :choose-framework-16-if
 ["Budget: $2000-3000"
  "Need graphics development (Redox orbital)"
  "Want future-proof expandability"
  "Run multiple VMs with GPU passthrough"
  "Use 2-3 external monitors"
  "Long-term investment mindset"]}
```

**Beginner Recommendation:** Start with Framework 13. It embodies the "simple and beautiful" philosophy. You can always upgrade later—that's the Framework promise.

---

# Part II: Linux Distribution Choice — Applying 9998 Principles

## The Rich Hickey Question: "What is Simple?"

From our 9998 learning path, Rich Hickey teaches: **"Simple is not easy. Simple is lack of interleaving."**

Let's evaluate distributions through this lens:

### Ubuntu/Pop!_OS (Not Simple, But Easy)

```clojure
{:ubuntu-analysis
 {:what-it-is
  "General-purpose, user-friendly, heavily pre-configured"
  
  :complected-aspects
  "- Snap packages interleaved with apt packages
   - systemd deeply integrated (can't easily swap)
   - Many pre-installed services you don't need
   - Desktop environment tightly coupled
   - Two parallel package systems (snap + deb)"
  
  :rich-hickey-verdict
  "Easy (familiar, GUI-driven) but NOT simple (many braided concerns)."
  
  :use-case
  "First Linux experience for non-technical users. NOT ideal for learning microkernels."}
```

### Arch Linux (Simple AND Hard, Initially)

```clojure
{:arch-linux-analysis
 {:what-it-is
  "Minimalist, rolling-release, build-your-own-system philosophy"
  
  :simple-aspects
  "- One package manager (pacman), clear hierarchy
   - No pre-installed bloat—you add what you need
   - systemd by default BUT easy to replace (see Artix)
   - Transparent: /etc is readable, nothing hidden
   - AUR provides community packages without complexity"
  
  :learning-curve
  "Steep initially (manual installation), but teaches fundamentals.
   Arch Wiki is legendary—best Linux documentation anywhere."
  
  :framework-laptop-support
  "Excellent. AMD drivers in mainline kernel, firmware in linux-firmware package."
  
  :rich-hickey-verdict
  "Simple (minimal interleaving) but hard (requires understanding).
   Perfect match for learning journey."
  
  :microkernel-development-fit
  "✓ Easy to install seL4 toolchain
   ✓ Build Redox OS from source (documented in Arch Wiki)
   ✓ Switch to Artix for runit/OpenRC experiments
   ✓ Install Nix package manager alongside pacman"}
```

### NixOS (Simple Philosophy, Declarative Complexity)

```clojure
{:nixos-analysis
 {:what-it-is
  "Declarative, reproducible, functional package management as OS"
  
  :simple-in-theory
  "- Everything declared in configuration.nix
   - Atomic upgrades, rollbacks (generations)
   - No hidden state—/nix/store is explicit"
  
  :complex-in-practice
  "- Nix language has learning curve
   - systemd deeply integrated (hence SixOS project)
   - Module system can be confusing
   - Error messages cryptic for beginners"
  
  :framework-laptop-support
  "Good, but requires manual hardware-configuration.nix setup."
  
  :rich-hickey-verdict
  "Simple philosophy, but implementation has interleaved concerns (modules + systemd)."
  
  :microkernel-development-fit
  "✓ Reproducible build environments
   ✓ Perfect for learning infuse.nix paradigm
   ✗ systemd lock-in (until SixOS matures)
   ~ Better as second distro after mastering basics"}
```

### Void Linux with runit (Simple AND Elegant)

```clojure
{:void-linux-analysis
 {:what-it-is
  "Independent, rolling-release, runit-based, minimalist"
  
  :simple-aspects
  "- runit for init (studied in 9998 Phase 2)
   - xbps package manager (fast, simple)
   - musl libc option (even more minimal)
   - No systemd (clean, uncomplicated boot)
   - Small, focused community"
  
  :framework-laptop-support
  "Excellent. AMD drivers work out-of-box."
  
  :rich-hickey-verdict
  "Truly simple. One concern per layer:
   - runit: supervise services
   - xbps: manage packages
   - No feature creep, no bloat"
  
  :technical-depth-from-9999
  "🔗 Why runit's simplicity matters (from 9999):
   
   System call overhead (from [9999: Buffering & Batching](9999-nixpkgs-clojure)):
   - Each system call: ~1000-10000 CPU cycles
   - Context switch: user space ↔ kernel space
   
   systemd approach:
   - Complex service activation (many syscalls)
   - Binary logging (journald requires parsing)
   - D-Bus IPC (additional overhead)
   
   runit approach:
   - Simple fork/exec (minimal syscalls)
   - Text logging (direct write)
   - Unix pipes (kernel-optimized)
   
   Result: runit is FASTER because it minimizes system call overhead.
   This is why it's perfect for learning—see the efficiency directly."
  
  :microkernel-development-fit
  "✓✓ PERFECT for learning runit (default init)
   ✓ Lightweight, won't interfere with experiments
   ✓ Easy to build seL4/Redox from source
   ✓ Can install Nix package manager separately"
  
  :deep-dive-xbps-src
  "Void's secret weapon: xbps-src template system
   
   xbps-src philosophy:
   - Build packages from source templates
   - Each template = shell script + metadata
   - Reproducible builds in isolated containers
   - Like Nix but simpler (shell, not language)
   
   Example template (srcpkgs/myapp/template):
   
   # Template file
   pkgname=myapp
   version=1.0.0
   revision=1
   build_style=gnu-configure
   hostmakedepends=\"pkg-config\"
   makedepends=\"openssl-devel\"
   short_desc=\"My application\"
   maintainer=\"you <you@example.com>\"
   license=\"MIT\"
   homepage=\"https://example.com/myapp\"
   distfiles=\"https://example.com/myapp-${version}.tar.gz\"
   checksum=\"sha256...\"
   
   Build with:
   $ ./xbps-src pkg myapp
   
   Install:
   $ xi myapp
   
   Why this matters:
   - You OWN your package definitions (storehouse concept!)
   - Fork void-packages → your own 'grainhouse'
   - Reproducible source builds
   - musl compatibility tested per package"
  
  :musl-vs-glibc-deep-dive
  "Void offers TWO C libraries:
   
   glibc (default):
   - Standard, widely compatible
   - Larger binary size
   - All software works
   
   musl (optional, suffix: -musl):
   - Minimal, clean codebase
   - Smaller binaries
   - Some compatibility issues (rare)
   - Perfect for embedded/minimal systems
   
   For microkernel development:
   Use musl variant! Teaches you:
   - Minimal dependencies
   - POSIX compliance (what's actually standard)
   - Closer to seL4/Redox philosophy
   
   Install: void-linux-musl.iso instead of void-linux.iso
   
   🔗 Deep dive on system calls and libc:
   See [9999: System Call Efficiency](9999-nixpkgs-clojure#educational-background-buffering-and-batching)
   - Why buffering matters (reduces syscall overhead)
   - musl vs glibc performance implications
   - How Alpine (musl) achieves 5MB base vs NixOS (glibc) 200MB"
  
  :xbps-architecture
  "xbps package manager internals:
   
   Components:
   - xbps-install: Install packages
   - xbps-remove: Remove packages
   - xbps-query: Query package database
   - xbps-src: Build from source templates
   - xbps-uchroot: Isolated build environment
   
   Storage:
   /var/db/xbps/          # Package database
   /etc/xbps.d/           # Repo configs
   srcpkgs/*/template     # Build templates (your storehouse!)
   
   Speed comparison:
   - xbps-install: ~0.5 seconds to install small package
   - apt-get: ~2-3 seconds (slower metadata)
   - pacman: ~1 second (comparable)
   - nix-env: ~5-10 seconds (evaluation overhead)
   
   xbps is FAST because: simple C implementation, minimal metadata"
  
  :risc-v-readiness
  "Void Linux + RISC-V future:
   
   Current status (2025):
   - No official RISC-V port yet
   - But xbps-src supports cross-compilation
   - musl works on RISC-V (Alpine has riscv64 port)
   
   Migration path:
   1. Use Void musl NOW (Framework AMD)
   2. Fork void-packages → your-grainhouse
   3. Add RISC-V cross-compile targets to templates
   4. Test in QEMU riscv64
   5. When hardware arrives, you're ready
   
   Why Void is ideal starting point:
   - Simple templates (easier to port than Nix expressions)
   - musl already RISC-V compatible
   - runit compiles anywhere (portable C)
   - Small community = easier to influence/fork"}}
```

### Artix Linux (Arch Without systemd)

```clojure
{:artix-linux-analysis
 {:what-it-is
  "Arch Linux base with choice of init: OpenRC, runit, s6, or dinit"
  
  :simple-aspects
  "- Arch package ecosystem (AUR, pacman)
   - Freedom to choose init system (studied in 9998)
   - All Arch Wiki knowledge applies
   - systemd removed, clean separation"
  
  :framework-laptop-support
  "Excellent (inherits Arch's support)."
  
  :rich-hickey-verdict
  "Simple AND flexible. Separates init concern from package management."
  
  :microkernel-development-fit
  "✓✓✓ IDEAL for 9998 learning path:
   - Install with runit, experiment with OpenRC later
   - Switch to s6-rc (sixos connection)
   - All Arch tools + init system freedom
   - Best of both worlds"}
```

## The Analytical Decision: Void vs Artix for Your Goals

### Your Specific Goals Restated

```clojure
{:your-objectives
 ["Build user-friendly, secure, portable OS"
  "Rich Hickey + Helen Atthowe design principles"
  "Framework laptop (AMD CPU/GPU)"
  "Progress toward RISC-V CPU+GPU boards"
  "Support Clojure/Babashka build workflow"
  "Simple, beautiful, observable systems"]}
```

### Deep Comparison: Void musl vs Artix runit

```clojure
{:the-tradeoff-analysis
 {:void-linux-musl
  {:strengths
   ["✓✓ Simplest possible base (runit + musl + xbps)"
    "✓✓ RISC-V migration path clearest (musl already works on riscv64)"
    "✓✓ xbps-src templates = your 'grainhouse' (fork and own)"
    "✓ Faster package operations (0.5s vs 1-2s)"
    "✓ Smaller binaries (musl vs glibc)"
    "✓ Forces POSIX compliance (teaches standards)"
    "✓ Independent distro (no corporate ties)"]
   
   :challenges
   ["✗ Smaller package repository (~13,000 vs Arch's 80,000+)"
    "✗ Some software doesn't compile on musl (rare but exists)"
    "✗ Babashka/Clojure may have musl compatibility issues"
    "✗ Smaller community (less StackOverflow help)"
    "✗ Steeper learning (must build more from source)"]
   
   :clojure-workflow-reality
   "Babashka: Native binary, might work on musl (need testing)
    Clojure JVM: Works but OpenJDK on musl has quirks
    
    Workaround:
    - Use glibc Void variant for JVM work
    - Or use Babashka exclusively (faster anyway)
    - Or run Clojure in Nix dev shell on Void host"}
  
  :artix-linux-runit
  {:strengths
   ["✓✓ Arch package ecosystem (80,000+ packages + AUR)"
    "✓✓ Legendary documentation (Arch Wiki)"
    "✓✓ Clojure/Babashka 'just works' (glibc compatibility)"
    "✓ Same runit learning as Void"
    "✓ Larger community"
    "✓ Choice of init (runit, OpenRC, s6)"]
   
   :challenges
   ["✗ glibc larger footprint (vs musl)"
    "✗ RISC-V path less clear (glibc porting harder)"
    "✗ Not as 'pure' (Arch complexity seeps in)"
    "✗ More packages = more to manage"]
   
   :clojure-workflow-reality
   "Babashka: pacman -S babashka (works perfectly)
    Clojure: pacman -S clojure (OpenJDK included)
    
    Your current bb.edn tasks work immediately.
    No friction."}}
 
 :the-analytical-decision
 {:for-risc-v-future
  "Void musl wins:
   - musl already on RISC-V
   - Simpler stack to port
   - xbps-src templates easier than Nix/Arch PKGBUILD
   - Forces minimal dependency discipline"
  
  :for-clojure-workflow-now
  "Artix wins:
   - Babashka/Clojure work immediately
   - Your bb.edn tasks unchanged
   - Faster development (less fighting tooling)"
  
  :for-learning-path-9998
  "Tie:
   - Both teach runit equally well
   - Both avoid systemd
   - Both simple and observable"
  
  :for-package-storehouse-concept
  "Void wins:
   - xbps-src templates = your grainhouse
   - Fork void-packages easily
   - Shell-based (simpler than Nix)
   - Source-based builds (reproducible)"}}
```

## The Recommendation: Dual Path Strategy

**Don't choose one—use BOTH strategically.**

### Strategy: Void musl as Target, Artix as Development Host

```clojure
{:the-hybrid-approach
 {:phase-1-now
  "Install Artix runit on Framework:
   - Get productive immediately (Clojure works)
   - Learn runit in daily use (9998 Phase 2)
   - Build Redox OS, seL4 natively
   - Use Nix dev shells for reproducibility"
  
  :phase-2-parallel
  "Run Void musl in QEMU on Artix:
   - Test musl compatibility for your projects
   - Fork void-packages → your-grainhouse
   - Build custom packages with xbps-src
   - Prepare RISC-V cross-compile templates"
  
  :phase-3-risc-v-prep
  "On Artix host:
   - Install riscv64 cross toolchain
   - Build QEMU system-riscv64
   - Test Void musl in QEMU riscv64
   - Port your critical packages to RISC-V"
  
  :phase-4-hardware-transition
  "When RISC-V Framework board arrives:
   - Boot Void musl riscv64 from USB
   - Your grainhouse templates already ported
   - Babashka might need recompile (GraalVM on RISC-V)
   - Or use alternative: Janet, Fennel, or pure shell"
  
  :from-rich-hickey
  "\"Don't layer new stuff on old stuff—understand the levels.\"
   
   Levels in this strategy:
   Level 1: Artix (comfortable, productive NOW)
   Level 2: Void musl (minimal, portable FUTURE)
   Level 3: RISC-V (long-term sovereignty)
   
   Each level teaches the next.
   No premature optimization.
   No building RISC-V OS before you understand x86."
  
  :from-helen-atthowe
  "\"Start with soil health, then plant.\"
   
   Soil health = understanding your base system:
   - Artix teaches you runit, package management, Linux
   - Void teaches you musl, source builds, minimalism
   - RISC-V teaches you architecture independence
   
   Plant (your OS project) only when soil (understanding) is ready."}
```

**Concrete Recommendation for You:**

1. **Install Artix runit on Framework 13/16** (this week)
2. **Follow the Artix installation guide** below (proven path)
3. **Set up Void musl in QEMU** (next month, parallel learning)
4. **Fork void-packages** → your-grainhouse repository (establish storehouse)
5. **Test Clojure on both** (document compatibility issues)
6. **Choose final direction** after 2-3 months hands-on experience

---

## The Cosmopolitan Libc Revolution: A Third Path

### For Apprentices: What is Actually Portable Executable (APE)?

```clojure
{:cosmopolitan-libc-overview
 {:created-by "Justine Tunney"
  :tagline "build-once run-anywhere C library"
  
  :the-radical-idea
  "Traditional approach:
   - Compile for Linux → linux-binary
   - Compile for macOS → macos-binary
   - Compile for Windows → windows.exe
   - Compile for BSD → bsd-binary
   Result: 4 binaries for 4 platforms
   
   Cosmopolitan approach:
   - Compile ONCE → actually-portable-executable
   - Runs on: Linux, macOS, Windows, FreeBSD, OpenBSD, NetBSD
   Result: 1 binary for ALL platforms
   
   How? The binary contains bootloaders for each OS format:
   - Looks like ELF to Linux
   - Looks like Mach-O to macOS
   - Looks like PE to Windows
   - Looks like a.out to BSD
   
   It's a polyglot binary—speaks every OS's native language!"
  
  :analogy-for-seventh-grader
  "Imagine a magic book that changes its language based on who's reading it.
   English speaker sees English.
   Spanish speaker sees Spanish.
   Chinese speaker sees Chinese.
   
   Same book, different readers, perfect translation for each.
   
   Cosmopolitan binaries are magic books for operating systems."}
```

### How Cosmopolitan Changes Everything

```clojure
{:cosmopolitan-implications
 {:for-framework-laptop
  "Compile your OS utilities ONCE on Framework:
   - Binary runs on Linux (your Artix/Void)
   - Same binary runs on macOS (your Mac)
   - Same binary runs on Windows (friend's PC)
   - Same binary tests on BSD (production server)
   
   Development workflow:
   1. Write C code using Cosmopolitan headers
   2. Compile: cosmocc -o myapp myapp.c
   3. Ship myapp to any platform
   4. Done."
  
  :for-risc-v-future
  "Current limitation: Cosmopolitan supports x86_64 primarily.
   ARM64 support exists but limited.
   RISC-V: Not yet (as of 2025).
   
   But the APPROACH is revolutionary:
   Once Cosmopolitan adds RISC-V target,
   your binaries run on x86 + ARM + RISC-V simultaneously.
   
   This is the ULTIMATE portability."
  
  :for-grainhouse-strategy
  "Instead of forking void-packages (13,000 templates),
   or maintaining Nix packages (80,000+ packages),
   
   Build YOUR essential tools with Cosmopolitan:
   - Text editors (compile vi/neovim with cosmocc)
   - Build tools (make, cmake, etc.)
   - System utilities (coreutils)
   - Your applications
   
   Result: TINY grainhouse (maybe 100 tools)
   that works EVERYWHERE (Linux, Mac, BSD, RISC-V eventually)"}
 
 :performance-vs-musl-vs-glibc
 {:memcpy-benchmark
  "Cosmopolitan's memcpy() outperforms:
   - glibc: 15% faster (multiple optimized strategies)
   - musl: 30% faster (clever assembly tricks)
   - Apple's: 20% faster
   
   Other functions similarly optimized.
   Smaller binaries than glibc, comparable to musl."
  
  :startup-time
  "Cosmopolitan binaries detect OS and configure at startup.
   Overhead: ~1-2 milliseconds
   Negligible for most applications.
   
   For comparison:
   - JVM startup: 50-200 ms
   - Python startup: 20-50 ms
   - Cosmopolitan overhead: 1-2 ms
   
   Essentially free."}}
```

### The Rich Hickey Analysis: Cosmopolitan vs Traditional

```clojure
{:hickey-lens
 {:traditional-approach-complected
  "Cross-platform C development traditionally:
   
   - Autotools (./configure, make) for build portability
   - #ifdef PLATFORM macros everywhere
   - Different makefiles for different OSes
   - Conditional compilation
   - Link different libraries per platform
   
   This is COMPLECTED:
   - Build logic interleaved with platform logic
   - Source code braided with conditionals
   - Hard to reason about: Which code runs on which platform?"
  
  :cosmopolitan-approach-simple
  "With Cosmopolitan:
   
   - Write standard POSIX C (one codebase)
   - Compile once with cosmocc
   - Binary contains ALL platform code
   - Runtime detects and chooses
   
   This is SIMPLE:
   - One concern: Write correct C
   - One dimension: POSIX compliance
   - No platform interleaving in source
   - Runtime polymorphism (like OOP, but for OSes)"
  
  :hickey-verdict
  "Cosmopolitan achieves what Hickey calls 'simplicity':
   - Decomplecting platform concerns from application logic
   - Single artifact (like immutable value)
   - Polymorphic behavior (runtime adaptation)
   
   This is elegant system design."}
 
 :ecological-metaphor
 "Helen Atthowe: 'A healthy plant adapts to its environment.'
  
  Traditional cross-compilation:
  - Grow separate plant varieties for each climate
  - Tomato for California, different tomato for Oregon
  - Maintain multiple seed lines
  
  Cosmopolitan approach:
  - Single seed that adapts to any climate
  - Same genetic code, phenotype varies by environment
  - One seed line to maintain
  
  This is like perennial polyculture:
  Plant once, harvest everywhere."}
```

### The Grainhouse Vision with Cosmopolitan

```clojure
{:cosmopolitan-grainhouse
 {:the-concept
  "Instead of forking entire distro repositories:
   
   void-packages: 13,000 templates (huge)
   nixpkgs: 80,000+ packages (overwhelming)
   
   Fork Cosmopolitan + YOUR essential tools:
   
   your-grainhouse/
     ├── cosmocc/           # Compiler toolchain
     ├── tools/
     │   ├── coreutils/     # ls, cp, mv, etc.
     │   ├── editor/        # vim or neovim
     │   ├── shell/         # bash or zsh
     │   ├── build/         # make, cmake
     │   └── network/       # curl, wget
     ├── runtime/
     │   ├── babashka/      # If possible to compile with Cosmopolitan
     │   └── your-app/
     └── README.md
   
   Total: Maybe 50-100 programs.
   Each is APE (works on any platform).
   
   This is manageable. This is owned. This is your storehouse."
  
  :build-process
  "# Clone Cosmopolitan
   git clone https://github.com/jart/cosmopolitan.git
   
   # Build cosmocc compiler
   make -j8
   
   # Compile your tool
   cosmocc -o myeditor myeditor.c
   
   # Test on multiple platforms
   ./myeditor  # Works on Linux
   scp myeditor mac:  # Copy to Mac
   ssh mac ./myeditor  # Works on macOS!
   
   Same binary, zero modification."
  
  :for-clojure-workflow
  "Challenge: Babashka is GraalVM native-image.
   GraalVM doesn't support Cosmopolitan (yet).
   
   Two paths:
   
   Path A (pragmatic):
   - Use Babashka on host system (Artix/Void)
   - Use Cosmopolitan for system utilities
   - bb.edn tasks call Cosmopolitan tools
   
   Path B (ambitious):
   - Build minimal Clojure interpreter in C
   - Compile with Cosmopolitan
   - Limited Clojure subset, but portable
   
   Path C (hybrid):
   - Use Janet language (Lisp-like, C-based)
   - Compiles with Cosmopolitan
   - Similar to Clojure (REPL, functional)
   - Your bb.edn → janet build scripts"}
 
 :risc-v-timeline
 "Cosmopolitan + RISC-V:
  
  Current: x86_64 primary, ARM64 experimental
  Future: RISC-V target planned
  
  When it arrives, your grainhouse binaries
  automatically work on RISC-V.
  
  No recompile. No port. Just run.
  
  This is the dream of 'write once, run anywhere'
  that Java promised but never fully delivered."}
```

### Updated Recommendation with Cosmopolitan

```clojure
{:three-path-strategy
 {:path-1-artix-runit
  "Start here (Framework laptop NOW):
   - Productive immediately (Arch packages)
   - Learn runit (9998 Phase 2)
   - Clojure/Babashka work perfectly
   - Build Redox, seL4, SixOS in QEMU"
  
  :path-2-void-musl-grainhouse
  "Parallel development (QEMU first):
   - Fork void-packages → your-grainhouse
   - Test musl compatibility
   - Prepare RISC-V templates
   - Minimal, portable, owned"
  
  :path-3-cosmopolitan-tools
  "Long-term portability layer:
   - Build core tools with Cosmopolitan
   - APE binaries in your grainhouse
   - Works on Framework AND future RISC-V
   - Smallest possible trusted base
   
   Progression:
   1. Compile coreutils with Cosmopolitan
   2. Test on Artix, Mac, Void
   3. Add your custom tools
   4. When RISC-V comes, binaries just work"}
 
 :the-synthesis
 "┌─────────────────────────────────────────┐
  │ Your Applications (Clojure/Babashka)   │
  │ - bb.edn tasks                          │
  │ - Business logic                        │
  └────────────┬────────────────────────────┘
               │
               ↓
  ┌─────────────────────────────────────────┐
  │ Cosmopolitan Tools (APE binaries)       │
  │ - System utilities                      │
  │ - Build tools                           │
  │ - Your grainhouse                       │
  └────────────┬────────────────────────────┘
               │
               ↓
  ┌─────────────────────────────────────────┐
  │ Void musl OR Artix runit               │
  │ - runit supervision                     │
  │ - Minimal base system                   │
  └────────────┬────────────────────────────┘
               │
               ↓
  ┌─────────────────────────────────────────┐
  │ Framework AMD (now) → RISC-V (future)   │
  │ - Open hardware                         │
  │ - Upgradeable, repairable               │
  └─────────────────────────────────────────┘
  
  Each layer is:
  ✓ Simple (single concern)
  ✓ Portable (works across platforms)
  ✓ Owned (you control the source)
  ✓ Observable (no hidden magic)
  
  This is Rich Hickey's simplicity +
  Helen Atthowe's ecological stewardship +
  Cosmopolitan's radical portability."}
```

### Why Artix Wins

```clojure
{:artix-rationale
 {:simplicity
  "runit as default = 9998 Phase 2 mastery built-in.
   Every time you boot, you're learning."
  
  :beauty
  "Helen Atthowe: 'Observe natural processes.'
   runit's three-stage boot is clean, observable:
   Stage 1: Setup → Stage 2: Supervise → Stage 3: Shutdown
   
   No hidden magic. Text-based service directories.
   Like a well-tended garden bed—everything visible."
  
  :flexibility
  "Pacman + AUR = vast package ecosystem.
   Want seL4? `yay -S sel4-deps` (from AUR).
   Want Rust for Redox? `pacman -S rust`.
   Want Nix? `curl -L https://nixos.org/nix/install | sh`"
  
  :framework-support
  "Arch-based = stellar AMD support.
   Framework-specific tweaks documented in Arch Wiki."
  
  :learning-path-alignment
  "✓ Phase 0 (foundations): Learn Linux basics on Artix
   ✓ Phase 1 (SixOS): Understand s6 via runit similarities
   ✓ Phase 2 (OpenRC/runit): Artix supports both!
   ✓ Phase 3 (Rust): Build Redox OS natively
   ✓ Phase 4 (synthesis): Test SixOS in QEMU on Artix host"}}
```

---

# Part III: Installation Guide — Artix Linux on Framework Laptop

## Pre-Installation: Apprentice Checklist

```clojure
{:preparation
 {:download
  ["Visit: artixlinux.org/download.php"
   "Choose: artix-base-runit (minimal, 600MB ISO)"
   "Verify: Check SHA256 sum"]
  
  :create-bootable-usb
  ["macOS: Use balenaEtcher or dd command"
   "Windows: Use Rufus or balenaEtcher"
   "Linux: dd if=artix.iso of=/dev/sdX bs=4M"]
  
  :framework-bios-setup
  ["Boot Framework, press F2 for BIOS"
   "Disable Secure Boot (Linux needs this)"
   "Set boot order: USB first"
   "Save and exit"]
  
  :backup
  "If dual-booting, BACK UP EVERYTHING first!"}
```

## Installation: Step-by-Step for Beginners

### Step 1: Boot the Live USB

```bash
# You'll see Artix boot menu
# Select: artix-base (default)
# Login as: root (no password on live system)
```

### Step 2: Connect to WiFi (If No Ethernet)

```bash
# For Framework WiFi (MediaTek MT7922):
rfkill unblock wifi
connmanctl
> enable wifi
> agent on
> scan wifi
> services  # Lists available networks
> connect wifi_<TAB>  # Auto-complete your network
> (enter password)
> quit

# Test connection:
ping -c 3 artixlinux.org
```

### Step 3: Partition the Disk

**Framework ships with NVMe SSD. Let's use GPT + UEFI:**

```bash
# List disks:
lsblk

# Partition (replace nvme0n1 with your disk):
cfdisk /dev/nvme0n1

# Create layout:
# /dev/nvme0n1p1: 512MB  EFI System
# /dev/nvme0n1p2: Rest   Linux filesystem

# Format:
mkfs.fat -F32 /dev/nvme0n1p1  # EFI partition
mkfs.ext4 /dev/nvme0n1p2      # Root partition

# Mount:
mount /dev/nvme0n1p2 /mnt
mkdir /mnt/boot
mount /dev/nvme0n1p1 /mnt/boot
```

### Step 4: Install Base System

```bash
# Update package database:
pacman -Sy

# Install base:
basestrap /mnt base base-devel runit elogind-runit linux linux-firmware

# AMD-specific firmware:
basestrap /mnt amd-ucode

# Essential tools:
basestrap /mnt vim networkmanager networkmanager-runit

# Generate fstab:
fstabgen -U /mnt >> /mnt/etc/fstab
```

### Step 5: Configure the System

```bash
# Chroot into new system:
artix-chroot /mnt

# Set timezone (example: US Pacific):
ln -sf /usr/share/zoneinfo/America/Los_Angeles /etc/localtime
hwclock --systohc

# Set locale:
vim /etc/locale.gen
# Uncomment: en_US.UTF-8 UTF-8
locale-gen
echo "LANG=en_US.UTF-8" > /etc/locale.conf

# Set hostname:
echo "framework-dev" > /etc/hostname

# Set root password:
passwd

# Create user:
useradd -m -G wheel -s /bin/bash yourname
passwd yourname

# Enable sudo:
EDITOR=vim visudo
# Uncomment: %wheel ALL=(ALL:ALL) ALL
```

### Step 6: Install Bootloader

```bash
# Install GRUB for UEFI:
pacman -S grub efibootmgr

# Install GRUB:
grub-install --target=x86_64-efi --efi-directory=/boot --bootloader-id=GRUB

# Generate config:
grub-mkconfig -o /boot/grub/grub.cfg
```

### Step 7: Enable Services (runit Style!)

```bash
# NetworkManager:
ln -s /etc/runit/sv/networkmanager /run/runit/service/

# (More services added post-install as needed)
```

### Step 8: Reboot

```bash
exit  # Leave chroot
umount -R /mnt
reboot
```

**Remove USB when Framework restarts. You should boot into Artix!**

---

# Part IV: Post-Installation — Building Your Microkernel Dev Environment

## Phase 1: Essential Setup (Day 1)

### Connect to WiFi

```bash
# As your user:
sudo nmcli device wifi connect "YourSSID" password "YourPassword"
```

### Install Yay (AUR Helper)

```bash
cd /tmp
pacman -S git
git clone https://aur.archlinux.org/yay.git
cd yay
makepkg -si
```

### Install Graphics Drivers (AMD)

```bash
# Framework 13/16 AMD integrated graphics:
sudo pacman -S mesa lib32-mesa vulkan-radeon lib32-vulkan-radeon

# If you have Framework 16 with dGPU:
sudo pacman -S xf86-video-amdgpu

# Verify:
glxinfo | grep "OpenGL renderer"
# Should show: AMD Radeon...
```

### Install Desktop Environment (Optional but Recommended for Beginners)

```bash
# For simplicity, use Xfce (lightweight, elegant):
sudo pacman -S xfce4 xfce4-goodies lightdm lightdm-runit

# Enable display manager:
sudo ln -s /etc/runit/sv/lightdm /run/runit/service/

# Reboot to graphical login:
sudo reboot
```

**You now have a beautiful, minimal Artix Linux desktop!**

---

## Phase 2: Microkernel Development Tools (Week 1)

### Install Rust (for Redox OS)

```bash
curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh
source ~/.cargo/env

# Verify:
rustc --version
cargo --version
```

### Install seL4 Dependencies

```bash
# From Arch Wiki (applies to Artix):
sudo pacman -S base-devel cmake ninja python python-pip \
  libxml2 dtc qemu-system-aarch64 qemu-system-arm \
  qemu-system-x86 gcc-arm-none-eabi

# seL4-specific Python packages:
pip install --user sel4-deps
```

### Set Up NixOS (in QEMU for SixOS experiments)

```bash
# Install QEMU:
sudo pacman -S qemu-full

# Install Nix package manager (on Artix host):
sh <(curl -L https://nixos.org/nix/install) --daemon

# Follow prompts, then:
source /etc/profile.d/nix.sh

# Verify:
nix --version
```

### Clone Redox OS

```bash
mkdir ~/dev
cd ~/dev
git clone https://gitlab.redox-os.org/redox-os/redox.git --origin upstream --recursive
cd redox

# Install Redox dependencies:
./bootstrap.sh -d

# Build minimal Redox (takes 30-60 minutes):
make all
```

---

## Phase 3: Daily Workflow — Simple and Beautiful

### Terminal Setup

```bash
# Install modern terminal:
sudo pacman -S alacritty

# Configure (~/.config/alacritty/alacritty.yml):
cat > ~/.config/alacritty/alacritty.yml << 'EOF'
font:
  normal:
    family: "JetBrains Mono"
  size: 11.0

colors:
  primary:
    background: '#002b36'  # Solarized Dark
    foreground: '#839496'
EOF

# Install JetBrains Mono font:
sudo pacman -S ttf-jetbrains-mono
```

### Editor: Neovim with LSP

```bash
# Install Neovim:
sudo pacman -S neovim

# For Rust development (Redox OS):
rustup component add rust-analyzer

# For Nix:
sudo pacman -S nil  # Nix LSP

# Configure Neovim (minimal):
mkdir -p ~/.config/nvim
cat > ~/.config/nvim/init.vim << 'EOF'
" Simple, beautiful Neovim config
set number          " Line numbers
set tabstop=4       " Tabs = 4 spaces
set shiftwidth=4
set expandtab       " Spaces not tabs
syntax on           " Syntax highlighting
colorscheme desert
EOF
```

### runit Service Exploration (Learning from 9998 Phase 2)

```bash
# List running services:
sudo sv status /run/runit/service/*

# Examine a service (NetworkManager):
ls -la /etc/runit/sv/networkmanager/
cat /etc/runit/sv/networkmanager/run

# Create your own service (example: hello world daemon):
sudo mkdir /etc/runit/sv/hello-daemon
sudo vim /etc/runit/sv/hello-daemon/run

# Content:
#!/bin/sh
exec 2>&1
while true; do
  echo "Hello from runit service"
  sleep 60
done

# Make executable:
sudo chmod +x /etc/runit/sv/hello-daemon/run

# Enable:
sudo ln -s /etc/runit/sv/hello-daemon /run/runit/service/

# Check status:
sudo sv status hello-daemon

# View logs:
sudo svlogtail hello-daemon
```

**This is hands-on learning of runit (9998 Phase 2) on real hardware!**

---

# Part V: Connecting Back to 9998 — The Learning Journey

## Week 1-2: Foundations on Framework

```clojure
{:applying-phase-0
 "Your Artix Framework laptop IS the foundation lab.
  
  Tasks:
  - Boot and observe runit stages (Stage 1 → 2 → 3)
  - Read /etc/runit/1, /etc/runit/2, /etc/runit/3 scripts
  - Create 3 custom services, test restarts
  - Compare to systemd (install VM with Ubuntu, contrast)"}
```

## Week 3-4: SixOS Exploration

```clojure
{:applying-phase-1
 "Use Nix (installed on Artix) to experiment with infuse.nix.
  
  Tasks:
  - Install infuse.nix: nix-env -i -f https://github.com/...
  - Practice examples from 9998 (nginx override, list pipelining)
  - Boot SixOS in QEMU on Framework
  - Map s6-rc concepts to runit knowledge"}
```

## Week 5-6: Rust Supervisor Development

```clojure
{:applying-phase-3
 "Build the Rust supervisor from 9998 Phase 3, run on Framework.
  
  Tasks:
  - Code the supervisor (services.toml config)
  - Test with real services (postgres, nginx)
  - Integrate with runit as fallback
  - Document learnings"}
```

## Week 7-8: Redox OS on Framework

```clojure
{:applying-phase-4
 "Your Redox build runs natively on Framework hardware!
  
  Tasks:
  - Boot Redox on Framework (make live)
  - Test scheme system (file:, tcp:, display:)
  - Write simple Redox driver
  - Compare microkernel boot to Artix/runit boot"}
```

---

# Part VI: Troubleshooting — Framework-Specific

## Common Issues and Fixes

### Framework 13 WiFi Not Working

```bash
# MediaTek driver might need firmware:
sudo pacman -S linux-firmware

# If still broken, check rfkill:
rfkill list
sudo rfkill unblock all
```

### Framework 16 dGPU Not Detected

```bash
# Ensure amdgpu module loaded:
lsmod | grep amdgpu

# If not, add to /etc/modules-load.d/amdgpu.conf:
echo "amdgpu" | sudo tee /etc/modules-load.d/amdgpu.conf

# Rebuild initramfs:
sudo mkinitcpio -P
sudo reboot
```

### Suspend/Resume Issues

```bash
# Framework-specific power management:
sudo pacman -S tlp tlp-runit

# Enable:
sudo ln -s /etc/runit/sv/tlp /run/runit/service/
```

### Brightness Control

```bash
# Add your user to video group:
sudo usermod -aG video $USER

# Reboot, then brightness keys should work
```

---

# Part VII: The Ecological Metaphor — Your Framework as a Garden Bed

Helen Atthowe teaches us to view the garden as a living system, not a machine. Your Framework laptop, running Artix with runit, embodies this:

```clojure
{:framework-as-garden-bed
 {:the-soil
  "Artix Linux = rich, living soil
   - runit microorganisms = service supervision
   - Pacman nutrients = software packages
   - Open firmware = healthy biome"
  
  :the-plants
  "Your development projects:
   - seL4 = alpine flowers (delicate, verified)
   - Redox OS = nitrogen-fixing legumes (Rust safety)
   - infuse.nix = companion herbs (enhance other plants)"
  
  :minimal-intervention
  "No-till approach:
   - Don't install unnecessary packages (tilling)
   - Use runit supervision (let nature self-correct)
   - Observe before acting (read logs first)"
  
  :polyculture
  "Multiple projects coexist:
   - Rust for Redox
   - C for seL4
   - Nix for SixOS
   - All in harmony on Framework soil"}
```

---

# Conclusion: Simple, Beautiful, Yours

Your Framework laptop running Artix Linux with runit is not just a development machine—it's a **learning environment that teaches you every day**.

- **Boot**: Watch runit's three stages → understand init systems
- **Install software**: Use pacman → understand package management
- **Write services**: Create runit service dirs → understand supervision
- **Build Redox**: Compile Rust kernel → understand microkernels
- **Repair hardware**: Swap Framework modules → understand ownership

This is the path Rich Hickey describes: **simple, not easy**. It requires understanding, but that understanding is **liberating**.

This is the path Helen Atthowe walks: **observe, intervene minimally, let the system thrive**.

Your Framework laptop is the spade. Artix Linux is the soil. The 9998 learning path is the seed catalog. Now go cultivate your garden.

---

**Next Writing:** [9996-nixos-dev-containers-vms.md](9996-nixos-dev-containers-vms) — NixOS Development Environments: VMs, Containers, and Build Management  
**Previous Writing:** [9998-learning-path-init-systems.md](9998-learning-path-init-systems) — A Comprehensive Learning Path for Modern Init Systems

---

*"The superior person cultivates the root; when the root is established, the Way grows."*  
— Confucius, Analects 1.2

*Your Framework laptop is the root. The 9998 learning path is the Way.*

---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*