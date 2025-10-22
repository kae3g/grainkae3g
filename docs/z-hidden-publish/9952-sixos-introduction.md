# kae3g 9952: The Gentle Gardener — Introducing SixOS (NixOS Without systemd)

**Timestamp:** 12025-10-10--rhizome-valley  
**Series:** Rhizome Valley Chronicles  
**Category:** Operating Systems, NixOS, s6, Declarative Configuration  
**Reading Time:** 20 minutes

> **"In the valley, we discovered that the mightiest tree isn't always the strongest—sometimes it's the one that knows when to shed what it doesn't need."**

---

## The Arrival of a New Visionary

*As we continue our journey through Rhizome Valley, having met the Wise Elders (Clojure & Nix), learned the Ancient Spells (system calls), and watched the Orchestra Awaken (init systems), we now encounter a figure who embodies the **"No-Till Intervention"** philosophy—a gardener who tends the land with the gentlest touch.*

**SixOS** emerges from the shadows of the Nix forest—not as a rebellion, but as an evolution. She carries the wisdom of Nix (immutability, declarative configuration) but walks with lighter steps, unburdened by systemd's heavy tools.

*"I am not here to tear down what Nix built,"* she says softly, her voice carrying the confidence of someone who knows their path. *"I am here to show that simplicity can coexist with sophistication. That we can tend a garden without plowing the entire field."*

## The Question That Sparked a Journey

*What is SixOS, and why would someone choose it over NixOS? Why embrace minimalism when you already have power?*

**SixOS** (also spelled "Sixos") is a **NixOS variant without systemd**, announced publicly in January 2025 at the 38th Chaos Communication Congress (38C3). It's the result of a two-year project aimed at creating a more modular, secure, and declarative system.

---

## Foundations for Beginners: Understanding Skarnet's s6 Supervisor

*Before we meet the Gentle Gardener herself, let's understand the tools in her kit. If you're new to process supervision or coming from systemd, this section is your gentle introduction to s6's philosophy and architecture.*

### What is Process Supervision?

**Imagine a garden where each plant needs attention**:
- Some need watering every hour (daemons that must always run)
- Some need to be replanted if they wilt (services that crash)
- Some only grow when others are healthy first (dependency management)

**A process supervisor** is like a master gardener who:
1. **Starts** each plant (process) properly
2. **Watches** them continuously
3. **Restarts** them if they die unexpectedly
4. **Logs** what's happening to each one
5. **Respects** the order (dependencies) they need

### Enter s6: The Minimalist's Answer

**s6** was created by **Laurent Bercot** (skarnet.org) as a modern successor to Dan Bernstein's classic `daemontools`. It embodies the Unix philosophy taken to its purest form:

> **"Do one thing, and do it well. Then compose those things."**

#### Why "s6"?

The name comes from **"skarnet.org's small and secure supervision software suite"**—or simply, six tools that work together perfectly:

1. `s6-svscan` - The root supervisor (watches directories)
2. `s6-supervise` - Individual service supervisor (watches one process)
3. `s6-svc` - Service control (send commands to services)
4. `s6-svstat` - Service status (query service state)
5. `s6-log` - Logging (capture and rotate logs)
6. `s6-rc` - Service manager (handle dependencies and bundles)

### The s6 Philosophy: Three Core Principles

#### 1. **Radical Simplicity**

```clojure
{:s6-vs-systemd
 {:binary-size
  {:s6 "~200KB total (all tools combined)"
   :systemd "~1.5MB (just systemd binary)"}
  
  :code-complexity
  {:s6 "~10,000 lines of C (readable, auditable)"
   :systemd "~250,000+ lines of C (sprawling)"}
  
  :logging
  {:s6 "Plain text files (grep, awk, tail work!)"
   :systemd "Binary journal (need journalctl)"}}}
```

**Plant lens**: "s6 is like a hand trowel—small, precise, perfectly weighted. systemd is like a tractor—powerful but requires training, fuel, and space to turn around."

#### 2. **Supervision by Design**

**s6's supervision model**:

```
Root Process: s6-svscan (PID 1 or early boot)
     ↓
Scans: /run/service/ (or configured directory)
     ↓
For each service directory found:
     ↓
Spawn: s6-supervise /run/service/sshd
       ↓
       Executes: /run/service/sshd/run script
                ↓
                Your service runs!
       ↓
       If process exits (crash or stop):
       ↓
       s6-supervise restarts it immediately
       (unless explicitly told to stay down)
```

**Key insight**: The supervisor (`s6-supervise`) is **always running**, even when your service isn't. This ensures instant restart on crash—no delays, no complex restart policies, just "process died → restart now."

#### 3. **Everything is a File (Literally)**

**s6 service structure** (example: sshd):

```bash
/run/service/sshd/          # Service directory
├── run                      # Executable script: how to start service
├── finish                   # Optional: cleanup script when service stops
├── down                     # Optional: file presence = "don't auto-start"
├── notification-fd          # Optional: readiness notification
└── log/                     # Optional: dedicated logging service
    ├── run                  # How to run the logger
    └── current              # The actual log file (text!)
```

**To start a service**:
```bash
s6-svc -u /run/service/sshd  # "up" = start
```

**To stop a service**:
```bash
s6-svc -d /run/service/sshd  # "down" = stop
```

**To check status**:
```bash
s6-svstat /run/service/sshd
# Output: up (pid 1234) 3600 seconds
```

**Plant lens**: "Each service is like a garden bed—self-contained, clearly marked, with its own instructions (run script) and maintenance log (log directory). No hidden state, no magic databases."

### How s6 Actually Works: The Three-Stage Boot

When SixOS (or any s6-based system) boots, it goes through three distinct stages:

#### **Stage 1: One-Time Initialization** (`s6-linux-init`)

```bash
# /etc/s6/rc/init (or similar)
# This runs ONCE at boot

mount /proc
mount /sys
mount /dev
hostname sixos-framework
ifconfig lo up
# ... other one-time setup
```

**Purpose**: Set up the basic system environment (filesystems, hostname, devices).

**Metaphor**: "Preparing the soil before planting—you only do it once."

#### **Stage 2: Service Activation** (`s6-rc`)

```bash
# s6-rc compiles service dependencies into a graph
# Then activates them in correct order

s6-rc-compile /etc/s6-rc/compiled /etc/s6-rc/source
s6-rc change default  # Activate "default" bundle
```

**Purpose**: Start all enabled services, respecting dependencies (e.g., network before sshd).

**Metaphor**: "Planting seeds in the right order—roots before leaves, water before fertilizer."

#### **Stage 3: Continuous Supervision** (`s6-svscan`)

```bash
# s6-svscan runs forever, scanning /run/service/
# If a service directory appears, spawn s6-supervise for it
# If a service crashes, s6-supervise restarts it

s6-svscan /run/service
```

**Purpose**: Monitor and maintain services 24/7.

**Metaphor**: "The gardener who never sleeps—always watching, always ready to replant."

### s6 vs systemd: A Conceptual Comparison

| Aspect | s6 | systemd |
|--------|-----|---------|
| **Philosophy** | Unix tools: compose small parts | All-in-one: integrated suite |
| **Size** | ~200KB binaries | ~1.5MB+ binaries |
| **Supervision** | Always (core design) | Optional (Type=notify, etc.) |
| **Logging** | Text files (s6-log) | Binary journal (journald) |
| **Dependencies** | s6-rc (optional addon) | Built-in (Unit After=/Requires=) |
| **Complexity** | ~10K lines C | ~250K+ lines C |
| **Learning curve** | Steeper (paradigm shift) | Gentler (more familiar) |
| **Control** | File-based (touch, rm, echo) | Command-based (systemctl) |

**When to choose s6**:
- You value **minimalism** and **auditability**
- You want **guaranteed supervision** (no "I forgot Type=notify")
- You prefer **text logs** (grep, awk, tail work perfectly)
- You're building **embedded systems** or **containers** (small footprint matters)
- You appreciate **Unix philosophy** (composition over integration)

**When to choose systemd**:
- You need **wide compatibility** (most distros use it)
- You want **rich features** (timers, socket activation, cgroups v2)
- You prefer **unified interface** (one tool for everything)
- You're on **mainstream Linux** (Ubuntu, Fedora, Arch, etc.)

### Real-World s6 Example: Running SSH Daemon

**Step 1: Create service directory**

```bash
mkdir -p /run/service/sshd/log
```

**Step 2: Write run script** (`/run/service/sshd/run`):

```bash
#!/bin/sh
exec /usr/bin/sshd -D  # -D = don't daemonize (s6 supervises!)
```

**Step 3: Write log run script** (`/run/service/sshd/log/run`):

```bash
#!/bin/sh
exec s6-log -d3 -b -- s ./current  # Rotate logs, keep 3 old files
```

**Step 4: Make scripts executable**:

```bash
chmod +x /run/service/sshd/run
chmod +x /run/service/sshd/log/run
```

**Step 5: Start service**:

```bash
s6-svc -u /run/service/sshd  # Service starts!
```

**That's it!** s6-supervise now monitors sshd 24/7. If it crashes, instant restart. Logs go to `/run/service/sshd/log/current` as plain text.

### Why SixOS Chose s6

**SixOS doesn't just use s6—it embraces its philosophy**:

1. **Nix + s6 = Perfect Marriage**
   - Nix: Declarative packages
   - s6: Declarative services
   - Both: Immutable, reproducible, composable

2. **Smaller Attack Surface**
   - 200KB of auditable code vs 1.5MB+ of systemd
   - Fewer lines = fewer bugs = easier verification

3. **Clear Mental Model**
   - Service = directory with scripts
   - No hidden magic, no binary state
   - "If you can navigate a filesystem, you can understand s6"

4. **Century-Scale Thinking**
   - Simple code survives longer (see: awk, grep, sed)
   - Complex systems accumulate cruft
   - s6's minimalism is future-proof

**Plant lens**: "systemd is like hybrid seeds—high yield, but you can't save them for replanting. s6 is like heirloom seeds—simpler, but you can grow them forever, passing them to your children's children."

---

### Resources for Learning s6

If you want to dive deeper:

- **Official s6 documentation**: [https://skarnet.org/software/s6/](https://skarnet.org/software/s6/)
- **s6-rc documentation**: [https://skarnet.org/software/s6-rc/](https://skarnet.org/software/s6-rc/)
- **Artix Linux s6 guide**: [https://wiki.artixlinux.org/Main/S6](https://wiki.artixlinux.org/Main/S6) (practical s6 usage)
- **Void Linux runit docs**: [https://docs.voidlinux.org/config/services/](https://docs.voidlinux.org/config/services/) (similar philosophy)

**Next**: Now that you understand s6's foundations, let's see how SixOS integrates it with Nix to create something truly special...

---

```clojure
{:sixos-overview
 {:what-it-is
  "A Nixpkgs-based OS that replaces systemd with skarnet's s6 supervisor
   Announced at 38C3 in January 2025
   Result of 2-year design exploration"
  
  :key-innovation
  "'Infusion' - services managed like packages in nixpkgs
   Declarative service configuration with atomic activation"
  
  :design-goals
  ["Eliminate systemd's perceived complexities"
   "Maintain NixOS-like atomic, immutable configurations"
   "Embrace Unix philosophy (modularity, simplicity)"
   "Owner-booted security (no unencrypted storage except EEPROM)"
   "Compatibility with Nix ecosystems"]}}
```

---

## Part I: Meet s6 — The Minimalist Maestro

### The Gentle Conductor's Tools

*While systemd in essay 9951 was portrayed as "The Overzealous Maestro" micromanaging every note, s6 arrives as a different kind of conductor—one who trusts each musician to know their part, intervening only when truly needed.*

*"Look,"* says SixOS, gesturing to a small, worn toolkit at her side. *"This is all I need. Each tool does one thing perfectly. No feature creep. No sprawling complexity. Just focused mastery."*

```clojure
{:s6-supervision-suite
 {:components
  ["s6: service supervision"
   "s6-rc: service manager"
   "s6-linux-init: init system"
   "All written in C, highly portable, minimal footprint"]
  
  :philosophy
  "Each tool does one thing well
   Composable components
   No feature creep"
  
  :size-comparison
  {:sixos "~200KB total binaries, text logs"
   :nixos "~1.5MB systemd binary, binary logs"}}}
```

**The Weight of Simplicity:**

*Nix raises an eyebrow. "200 kilobytes? systemd is nearly eight times larger. What magic is this?"*

*SixOS smiles. "It's not magic—it's restraint. When you refuse to do everything, you can do the essential things perfectly. Like a haiku compared to an encyclopedia. Both have their place, but one carries more power per word."*

### How s6 Works: The Three-Stage Dance

```clojure
{:s6-architecture
 {:stage-1-init
  "/run/s6/rc/init - One-time system setup
   - Mount filesystems
   - Set hostname, networking basics
   - Prepare environment"
  
  :stage-2-supervision
  "/run/s6-rc/compiled - Service activation
   - Start all enabled services
   - Respecting dependencies
   - Begin supervision loop"
  
  :stage-3-supervision-loop
  "Continuous monitoring:
   - Watch process exit codes
   - Restart failed services
   - Log events
   - Handle signals"}}
```

---

## Part II: The 'Infuse' Innovation — Like Spreading Living Mulch

*SixOS kneels and spreads her hand across the ground, as if tending a garden bed.*

*"This is where the real magic happens,"* she says, her eyes bright with the passion of a master gardener. *"In traditional NixOS, when you want to change how a service works, you have to plow through the entire field—tear up the module system, reconfigure everything, hope nothing breaks. It's exhausting and destructive."*

*"But with 'infuse,'" she continues, standing and brushing the soil from her hands, "I can spread configuration like living mulch—a protective, nourishing layer that suppresses weeds (errors) while enriching the soil (services) beneath. Minimal disruption. Maximum effect."*

### The Problem: Plowing Through Complexity

```clojure
{:nixos-module-challenges
 "Traditional NixOS:
  - systemd services defined in module system
  - Complex dependencies, hard to reason about
  - Monolithic activation
  - Difficult to run multiple instances of same service"}
```

### SixOS's Solution: Treat Services Like Packages

```clojure
{:infuse-concept
 "SixOS with 'infuse':
  - Services are 'infused' like packages
  - Declarative resolution at build time
  - Atomic activation with rollback
  - Each service = derivation
  
  Example mental model:
  
  (defn infuse [service-spec]
    (let [deps (resolve-dependencies service-spec)
          closure (build-closure deps)
          activation-script (generate-s6-scripts closure)]
      {:service-path (store-in-nix closure)
       :activation activation-script
       :rollback-generation (- current-gen 1)}))"}
```

**Key insight:** Each service becomes a Nix derivation that outputs an s6-rc service bundle.

---

## Part III: The Polyculture Meadow — SixOS's Thriving Ecosystem

### What SixOS Inherits (The Living Soil)

*Clojure, watching from the sidelines, nods approvingly. "You've kept the best parts—the fertile foundation—while removing what doesn't serve the garden."*

*SixOS smiles. "Exactly. I'm not throwing away Nix's wisdom. I'm building on it, creating a polyculture meadow instead of a monoculture field."*

```clojure
{:sixos-advantages
 {:keeps-from-nixos
  ["Atomic configuration activation"
   "Immutable system configurations"
   "Reproducible builds"
   "Generation rollback (nixos-rebuild --rollback)"]
  
  :adds-unix-philosophy
  ["Small, focused components"
   "Text-based service definitions"
   "No binary logging"
   "Minimal resource usage"
   "Clear component boundaries"]
  
  :hardware-support
  "Ownerboot: Manages mutable firmware as part of system config
   All firmware tracked and versioned like packages"}}
```

---

## Getting Started with SixOS

### Installation (in QEMU for learning)

```bash
# Clone SixOS repository
git clone https://codeberg.org/amjoseph/sixos.git
cd sixos

# Build minimal VM
nix build .#nixosConfigurations.minimal-sixos.config.system.build.vm

# Run in QEMU
result/bin/run-nixos-vm

# Observe boot process
# Compare to NixOS boot (if familiar)
```

### Your First Service

```nix
# File: services/hello.nix
{ lib, pkgs, ... }:

{
  name = "hello-service";
  run = "${pkgs.bash}/bin/bash -c 'while true; do echo Hello; sleep 60; done'";
  dependencies = [];
}

# This generates an s6-rc service bundle
# Build: nix build .#services.hello-service
# Result: /nix/store/...-hello-service-s6-rc/run
```

---

## The Call to Garden

*SixOS stands at the edge of the valley, looking back at the path we've traveled together.*

*"You've met the Wise Elders,"* she says. *"You've learned the Ancient Spells. You've heard the Orchestra Awaken. Now you've seen how the Gentle Gardener tends the land—with respect, with restraint, with the wisdom to know that less is often more."*

*"But I am just one approach. There are other gardeners in this valley, each with their own wisdom." She gestures toward the horizon. "Come. Let me introduce you to infuse.nix—the technique that makes all of this possible. It's like learning to graft plants, but for services and configurations."*

### Your Quest Continues

SixOS is the **gateway** to understanding:
- How services can be treated as data (Nix derivations)—like seeds you can plant anywhere
- How infuse.nix enables powerful overrides without destruction—the living mulch technique
- How microkernel principles apply to init systems—building resilient polycultures
- How to build minimal, observable, reproducible systems—gardens that thrive with minimal intervention

**The journey deepens:** [9953: The infuse.nix Paradigm](9953-infuse-nix-paradigm) — Learn the master gardener's technique for declarative service configuration

---

**Next Writing:** [9953-infuse-nix-paradigm](9953-infuse-nix-paradigm) — The infuse.nix Paradigm  
**Previous Writing:** [9951-init-systems-landscape](9951-init-systems-landscape) — The Init Systems Landscape

---

*"The art of simplicity is a puzzle of complexity."*  
— Douglas Horton

*SixOS achieves sophistication through simplicity: s6 instead of systemd, infusion instead of plowing.*

*In the valley, we learn that the gentlest touch often yields the strongest growth.*

---

[View All Essays](/12025-10/)



---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*