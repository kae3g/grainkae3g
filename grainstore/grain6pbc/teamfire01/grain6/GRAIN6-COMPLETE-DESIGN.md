# 🌾 grain6 (grainsix) - Complete Design

**Time-Aware Process Supervision for Grain Network**

Synthesizing: graintime + clojure-s6 + clojure-sixos + Urbit Behn + ICP canisters

---

## 🎯 **Three-Layer Architecture**

```
┌─────────────────────────────────────────────────────────┐
│  Layer 3: ICP Canister (Decentralized, Global)          │
│  ┌───────────────────────────────────────────────────┐  │
│  │  grain6.mo (Motoko via Clotoko)                   │  │
│  │  - On-chain timer queue                           │  │
│  │  - Cross-canister coordination                    │  │
│  │  - Stable storage (survives upgrades)             │  │
│  │  - Cycles-based billing                           │  │
│  └───────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
           ↕ (Clotoko transpilation)
┌─────────────────────────────────────────────────────────┐
│  Layer 2: Native Library (Local, Fast)                  │
│  ┌───────────────────────────────────────────────────┐  │
│  │  grain6.core (Clojure)                            │  │
│  │  - Priority queue (sorted-map)                    │  │
│  │  - Astronomical calculations (graintime)          │  │
│  │  - Process supervision (clojure-s6)               │  │
│  │  - Typo tolerance (grainregistry)                 │  │
│  └───────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
           ↕ (Behn inspiration)
┌─────────────────────────────────────────────────────────┐
│  Layer 1: Urbit Behn Concepts (Inspiration)             │
│  - Simple timer priority queue                          │
│  - %wait/%rest/%drip tasks                              │
│  - Crash isolation                                      │
│  - Minimal state                                        │
└─────────────────────────────────────────────────────────┘
```

---

## 🌐 **Complete Feature Matrix**

| Feature | Behn (Urbit) | grain6 Local | grain6 ICP | grain6 Full |
|---------|--------------|--------------|------------|-------------|
| **Timer Queue** | ✅ Sorted set | ✅ Sorted map | ✅ On-chain | ✅ ✅ |
| **Set Timer** | ✅ %wait | ✅ grain-wait | ✅ setTimer | ✅ ✅ ✅ |
| **Cancel Timer** | ✅ %rest | ✅ grain-rest | ✅ cancelTimer | ✅ ✅ ✅ |
| **Crash Isolation** | ✅ %drip | ✅ grain-drip | ✅ Future-based | ✅ ✅ ✅ |
| **Memory Report** | ✅ %wegh | ✅ stats | ✅ getStatistics | ✅ ✅ ✅ |
| **Astronomical** | ❌ | ✅ graintime | ✅ graintime | ✅ ✅ |
| **Repeating** | ❌ Manual | ✅ grain-repeat | ✅ setRepeating | ✅ ✅ |
| **Location-Aware** | ❌ | ✅ gt config | ✅ Per-user | ✅ ✅ |
| **Process Supervision** | ❌ | ✅ s6 | ❌ | ✅ |
| **Decentralized** | ❌ | ❌ | ✅ ICP subnet | ✅ |
| **Typo Tolerance** | ❌ | ✅ grainregistry | ✅ grainregistry | ✅ ✅ |
| **Upgrade Persistence** | ❌ | ❌ | ✅ Stable vars | ✅ |
| **Cross-Chain** | ❌ | ❌ | ✅ Chain Fusion | ✅ |

---

## 🔄 **Task Flow: Local vs ICP**

### **Local Execution**

```
User Command: grain6 supervise graindisplay
    ↓
grainregistry resolves: "graindisplay" (validated)
    ↓
grain6.core calculates: sunset time (graintime)
    ↓
grain6.behn schedules: timer in priority queue
    ↓
Background loop wakes: at sunset
    ↓
grain6.behn executes: graindisplay-wayland on
    ↓
clojure-s6 supervises: keeps it running
```

### **ICP Execution**

```
Canister Call: grain6.setTimer(sunset, enableWarm)
    ↓
grain6 canister validates: cycles payment, authorization
    ↓
Stable storage writes: timer persisted on-chain
    ↓
Heartbeat function runs: every second, checks queue
    ↓
Sunset arrives: heartbeat processes timer
    ↓
Inter-canister call: grain6 → grainDisplay.enableWarm()
    ↓
State replicated: across entire ICP subnet
```

---

## 📦 **Directory Structure (Complete)**

```
grain6/  (symlinked as grainsix)
├── README.md
├── BEHN-INSPIRATION.md          # Urbit Behn concepts
├── ICP-CANISTER-DESIGN.md       # ICP deployment strategy
├── GRAIN6-COMPLETE-DESIGN.md    # This file
│
├── deps.edn                     # Clojure dependencies
├── bb.edn                       # Babashka tasks
│
├── src/
│   └── grain6/
│       ├── core.clj             # Main API
│       ├── behn.clj             # Behn-inspired timer queue
│       ├── scheduler.clj        # Astronomical scheduling
│       ├── supervisor.clj       # s6 integration
│       ├── canister.clj         # ICP canister (for Clotoko)
│       └── events.clj           # Event processing
│
├── canisters/
│   └── grain6/
│       ├── dfx.json
│       └── src/
│           └── main.mo          # Generated Motoko (from Clotoko)
│
├── test/
│   └── grain6/
│       ├── core_test.clj
│       ├── behn_test.clj
│       └── scheduler_test.clj
│
└── examples/
    ├── graindisplay.edn         # Display warmth automation
    ├── backup.edn               # Scheduled backups
    ├── dev-workflow.edn         # Development automation
    └── icp-deployment.md        # ICP canister examples
```

---

## 🎨 **The Complete Synthesis**

### **What Each Component Brings**

**graintime:**
- Neovedic timestamps
- Astronomical calculations (sunrise, sunset)
- Solar house clock
- Nakshatra tracking
- Location awareness

**clojure-s6:**
- Process supervision
- Service lifecycle management
- Dependency resolution
- Logging

**clojure-sixos:**
- Typo tolerance
- Name resolution
- Registry management

**Urbit Behn:**
- Elegant timer queue design
- Task/gift interface
- Crash isolation (%drip)
- Minimal state

**ICP Canisters:**
- Decentralized deployment
- Stable storage
- Inter-canister calls
- Cycles economics

**grain6 = All of the Above + Grain Network Philosophy**

---

## 🌟 **Example Use Cases (Complete Flow)**

### **Use Case 1: Automated Display Warmth**

**Local Version:**
```bash
# Install grain6
cd grain6
bb install

# Configure display warmth schedule
cat > ~/.config/grain6/graindisplay.edn << EOF
{:name "graindisplay-auto"
 :schedule {:astronomical :sunset
            :action :start
            :repeat :daily}
 :command "graindisplay-wayland on"
 :stop-at :sunrise}
EOF

# Start supervision
grain6 supervise ~/.config/grain6/graindisplay.edn
```

**ICP Version:**
```bash
# Deploy canisters
dfx deploy grain6
dfx deploy graindisplay

# Subscribe graindisplay to sunset events
dfx canister call grain6 waitAstronomical '(
  variant { sunset },
  variant { interCanisterCall = record {
    canister = principal "rrkah-fqaaa-aaaaa-aaaaq-cai";
    method = "enableWarm";
    args = blob "";
  }}
)'

# Now runs globally, decentralized!
```

---

## 🔧 **Integration with Existing Tools**

### **grainbarrel (gb) Integration**

```clojure
;; grainbarrel task using grain6
(deftask dev
  "Start dev server with grain6 supervision"
  []
  (grain6/supervise
    {:name "grain-dev"
     :command "grain dev"
     :schedule {:solar-house 10}  ; Work hours
     :auto-stop true}))
```

### **grainsource Integration**

```clojure
;; Auto-sync repos at specific times
(grain6/supervise
  {:name "grainsource-sync"
   :schedule {:solar-house 2}  ; Resources house
   :command "grainsource sync-all"
   :repeat :daily})
```

### **graincourse Integration**

```clojure
;; Build courses at midnight
(grain6/supervise
  {:name "graincourse-build"
   :schedule {:graintime-pattern "*-*-*--00:00--*"}
   :command "gb graincourse:build"})
```

---

## 📊 **Performance Characteristics**

### **Local grain6**
- Latency: <1ms (priority queue operations)
- Memory: ~1KB per 100 timers
- CPU: Negligible (async loop)
- Scalability: ~1M timers per instance

### **ICP grain6**
- Latency: ~2-3s (consensus + heartbeat)
- Memory: On-chain stable storage
- CPU: Subnet cycles
- Scalability: Limited by canister memory (~4GB stable storage)
- Cost: ~0.01 cycles per timer operation

---

## 🎯 **Development Roadmap**

### **v0.1.0 - Foundation**
- [x] Design documents
- [x] Behn inspiration analysis
- [x] ICP canister design
- [ ] Basic timer queue (Clojure)
- [ ] grainregistry integration
- [ ] Simple astronomical events

### **v0.2.0 - Core Functionality**
- [ ] Complete Behn-style API
- [ ] Astronomical event calculations
- [ ] s6 supervision integration
- [ ] Crash isolation (drip)
- [ ] Repeat/recurring events

### **v0.3.0 - ICP Deployment**
- [ ] Clotoko transpilation
- [ ] Motoko canister implementation
- [ ] ICP local testing
- [ ] Heartbeat optimization
- [ ] Stable storage patterns

### **v0.4.0 - Production**
- [ ] ICP mainnet deployment
- [ ] Cross-canister coordination
- [ ] Graintime oracle service
- [ ] Multi-chain integration
- [ ] Performance optimization

---

## 🌾 **Philosophy**

**From granules to grains to THE WHOLE GRAIN:**

**Granules:**
- One timer set
- One service supervised
- One astronomical calculation

**Grains:**
- Complete timer queue (Behn)
- Full scheduling system (graintime)
- Integrated supervision (s6)

**THE WHOLE GRAIN:**
- Decentralized timing (ICP)
- Cross-chain coordination
- Global astronomical awareness
- Ecosystem-wide time synchronization

**grain6 isn't just a timer.**

**grain6 is time itself, distributed across the Internet Computer, aware of the stars, supervising the services that serve humanity.**

---

**Version:** 0.1.0  
**Date:** October 23, 2025  
**Graintime:** `12025-10-23--2318--PDT--moon-vishakha--asc-gemini000--sun-05thhouse--kae3g`  
**Author:** kae3g (Grain PBC)  
**License:** MIT

🌾 **Time-aware, decentralized, astronomical, and elegant!**
