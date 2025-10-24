# 🔧 Grain Hardware Ecosystem
## *"Sustainable, open-source computing for personal sovereignty"*

**Ecosystem Version:** 1.0  
**Established:** January 22, 2025  
**Authority:** kae3g (Graingalaxy)  
**Scope:** Complete open-source hardware platform for the Grain Network

---

## 🎯 VISION

The **Grain Hardware Ecosystem** is a comprehensive suite of open-source hardware devices designed to provide complete personal sovereignty in computing, photography, and development. Built on principles of sustainability, recyclability, and user ownership, every device in the Grain ecosystem is designed to be repairable, upgradeable, and completely open.

---

## 🌾 ECOSYSTEM OVERVIEW

```
┌─────────────────────────────────────────────────────────────┐
│                  GRAIN HARDWARE ECOSYSTEM                   │
│                                                             │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐  │
│  │ Graincamera  │    │ Grainwriter  │    │  Grainpack   │  │
│  │   (AVIF)     │◄───│   (E-Ink)    │◄───│  (GPU)       │  │
│  │              │    │              │    │              │  │
│  │ Photography  │    │   Writing    │    │ Development  │  │
│  └──────────────┘    └──────────────┘    └──────────────┘  │
│                                                             │
│                      All Connected Via:                     │
│                   ┌──────────────────────┐                 │
│                   │   SixOS + Clojure    │                 │
│                   │   Grainspace Network │                 │
│                   │   ICP + Urbit Sync   │                 │
│                   └──────────────────────┘                 │
└─────────────────────────────────────────────────────────────┘
```

---

## 📷 GRAINCAMERA

### Overview
**Graincamera** is an open-hardware mirrorless camera with native AVIF support, designed for professional photographers who value open-source software and sustainable hardware.

### Key Features
- **Native AVIF Support** - Superior compression and quality
- **RISC-V Processor** - SiFive U74 quad-core @ 1.4GHz
- **Fujifilm X-Mount** - Compatible with existing lens ecosystem
- **Open Firmware** - Clojure + SixOS operating system
- **Nostr + ICP Integration** - Direct photo sync to decentralized storage

### Technical Specifications
- **Sensor:** Sony IMX571 (26MP APS-C)
- **Processor:** SiFive U74 (RISC-V)
- **Storage:** 64GB RAM + USB-C external
- **Connectivity:** WiFi 6, Bluetooth 5.2, USB-C 3.2
- **Display:** 3.2" touchscreen (1024×768)
- **Battery:** 2000mAh (500 shots)

### Pricing
- **Target Retail:** $799
- **Component Cost:** $450
- **Production Cost:** $200

### Status
- **Design:** ✅ Complete
- **Prototype:** 🔄 Planned
- **Production:** ⏳ Future

---

## ✍️ GRAINWRITER

### Overview
**Grainwriter** is an open-source e-ink writing device with 64GB RAM-only storage, infinite battery life, and military-grade ruggedness. Perfect for writers, journalists, and developers who need a distraction-free environment.

### Key Features
- **E-Ink Display** - Inkplate 10 (10.3", 227 DPI)
- **64GB RAM** - No persistent storage, instant access
- **Infinite Battery** - Triple power system + solar + wireless charging
- **IP68 Waterproof** - Submersible to 1.5m for 30 minutes
- **MIL-STD-810H** - Military-grade drop and shock resistance
- **USB-C Sync** - iPod Touch-style file sharing
- **SixOS + Clojure** - Programmable interface

### Technical Specifications
- **Display:** Inkplate 10 (1872×1404, 16 grayscale)
- **Processor:** Intel Core i7-8650U (refurbished)
- **Memory:** 64GB DDR4-2666 (2x 32GB, upgradeable)
- **Battery:** 20,000mAh + 10,000mAh + 100F supercapacitor
- **Solar:** 10W foldable panel
- **Charging:** USB-C PD 3.0 (100W), Qi Wireless (15W), Solar (10W)
- **Weight:** 980g (with all batteries)
- **Dimensions:** 280×200×18mm

### Pricing
- **Standard (64GB):** $1,399
- **Budget (32GB):** $1,099
- **Premium (64GB + extras):** $1,699

### Status
- **Design:** ✅ Complete
- **Library:** ✅ Created (Clojure)
- **Prototype:** 🔄 Next phase
- **Production:** ⏳ Future

---

## 🚀 GRAINPACK

### Overview
**Grainpack** is a rugged external GPU attachment for Grainwriter, transforming it into a powerful development workstation. Features refurbished AMD GPUs, IP68 waterproofing, and hot-swappable connection.

### Key Features
- **Refurbished AMD GPUs** - RX 6600 XT / 6700 XT / 6800 XT
- **IP68 Waterproof** - Matching Grainwriter ruggedness
- **Hot-Swappable** - Magnetic USB-C connection
- **Triple Battery System** - 20,000mAh + 10,000mAh + supercapacitor
- **Solar Charging** - 20W foldable panel
- **SixOS Integration** - Native Clojure GPU acceleration

### Technical Specifications
- **GPU Options:**
  - Budget: AMD RX 6600 XT (8GB GDDR6, 2048 SP)
  - Performance: AMD RX 6700 XT (12GB GDDR6, 2560 SP)
  - High-End: AMD RX 6800 XT (16GB GDDR6, 4608 SP)
- **Connection:** USB-C 3.2 Gen 2x2 (20Gbps), Thunderbolt 3 compatible
- **Power:** USB-C PD 3.0 (100W), Qi Wireless (15W), Solar (20W)
- **Battery:** 20,000mAh + 10,000mAh (4-6 hours GPU operation)
- **Weight:** 1.2kg (with GPU)
- **Dimensions:** 280×180×45mm

### Pricing
- **Budget (RX 6600 XT):** $1,199
- **Performance (RX 6700 XT):** $1,399
- **High-End (RX 6800 XT):** $1,699

### Status
- **Design:** ✅ Complete
- **Integration:** ✅ SixOS library planned
- **Prototype:** 🔄 Next phase
- **Production:** ⏳ Future

---

## 🤖 AGENTIC PIPELINE

### Overview
**Agentic Pipeline** is a self-hosted AI development environment that integrates with Void/Zed editors and provides GPU-accelerated code generation, documentation, and testing using open-source models.

### Supported Models
- **Qwen3** - Primary code generation model
- **Gemini** - Documentation generation
- **Llama** - Code analysis and optimization
- **GPT-OS** - Test generation and refactoring

### Supported Editors
- **Void Editor** - Primary (Rust-based, LSP support)
- **Zed Editor** - Alternative (collaborative features)

### Integration
- **Grainwriter** - Code editing on e-ink display
- **Grainpack** - GPU-accelerated AI inference
- **SixOS** - s6-supervised model servers
- **Clojure X UI** - Real-time status monitoring

### Status
- **Design:** ✅ Complete
- **Model Integration:** 🔄 Ollama-based
- **Editor Plugins:** 🔄 Planned
- **Production:** ⏳ Future

---

## 🔌 ECOSYSTEM INTEGRATION

### Unified Operating System: SixOS

All Grain hardware runs **SixOS**, a NixOS variant without systemd, using s6 supervision. This provides:
- **Unified Configuration** - Single Nix flake for all devices
- **s6 Supervision** - Reliable process management
- **Clojure Development** - Native Clojure runtime
- **Grainspace Sync** - Automatic device synchronization

### Data Flow

```
┌─────────────────────────────────────────────────────────────┐
│                      Data Flow Example                      │
│                                                             │
│  1. Take photo with Graincamera (AVIF format)              │
│                    ↓                                        │
│  2. Auto-sync via Nostr to ICP canister                    │
│                    ↓                                        │
│  3. Access on Grainwriter for annotation                   │
│                    ↓                                        │
│  4. Process with Grainpack GPU (AI tagging)                │
│                    ↓                                        │
│  5. Publish to Grainspace Network                          │
│                    ↓                                        │
│  6. Available on all Grain devices                         │
└─────────────────────────────────────────────────────────────┘
```

### Shared Libraries

**clojure-s6** - s6 supervision wrapper
- Used by: All devices
- Functions: Service management, logging, process supervision

**clojure-sixos** - SixOS integration
- Used by: All devices
- Functions: System configuration, service management

**clojure-grainspace** - Grain Network sync
- Used by: All devices
- Functions: ICP integration, Nostr sync, Urbit identity

**clojure-gpu** - GPU acceleration (Grainpack)
- Used by: Grainpack, Agentic Pipeline
- Functions: GPU compute, model inference

**clojure-avif** - AVIF image processing (Graincamera)
- Used by: Graincamera, Grainphotos
- Functions: AVIF encode/decode, metadata

---

## 💰 ECOSYSTEM PRICING

### Individual Devices

| Device | Budget | Standard | Premium |
|--------|--------|----------|---------|
| **Graincamera** | - | $799 | - |
| **Grainwriter** | $1,099 | $1,399 | $1,699 |
| **Grainpack** | $1,199 | $1,399 | $1,699 |

### Bundles

**Writer's Bundle** (Grainwriter Standard)
- Grainwriter Standard (64GB)
- USB-C charging cables
- Magnetic keyboard attachment
- 1-year Grainspace Pro
- **Price:** $1,399

**Developer's Bundle** (Grainwriter + Grainpack)
- Grainwriter Standard (64GB)
- Grainpack Performance (RX 6700 XT)
- Agentic Pipeline license
- 1-year Grainspace Pro
- **Price:** $2,599 (save $200)

**Professional Bundle** (All devices)
- Graincamera
- Grainwriter Premium (64GB)
- Grainpack High-End (RX 6800 XT)
- Agentic Pipeline license
- Lifetime Grainspace Pro
- **Price:** $4,299 (save $600)

---

## 🌱 SUSTAINABILITY METRICS

### Material Sourcing

**Refurbished Components:**
- **Grainwriter:** 80% refurbished (processor, RAM, batteries)
- **Grainpack:** 90% refurbished (GPU, cooling, enclosure aluminum)
- **Graincamera:** 40% refurbished (processor board)

**Recyclability:**
- **Aluminum:** 100% recyclable (enclosures)
- **TPU Gaskets:** 100% recyclable
- **Batteries:** 95% recyclable (Li-Po cells)
- **E-Ink Display:** 90% recyclable (glass, electronics)

### Carbon Footprint

**Manufacturing Emissions:**
- **Graincamera:** ~15kg CO₂e per unit
- **Grainwriter:** ~12kg CO₂e per unit (due to refurbished components)
- **Grainpack:** ~10kg CO₂e per unit (refurbished GPU)

**Lifetime Emissions Savings:**
- **Grainwriter vs Traditional Laptop:** -80kg CO₂e (10-year lifespan)
- **Grainpack vs New GPU:** -50kg CO₂e (refurbished vs new)
- **Ecosystem vs Traditional Setup:** -200kg CO₂e total

### Repairability

**iFixit Repairability Score (Projected):**
- **Graincamera:** 9/10 (modular design, standard screws)
- **Grainwriter:** 10/10 (screws-only, user-upgradeable RAM)
- **Grainpack:** 9/10 (hot-swappable GPU, standard components)

---

## 📐 DESIGN PRINCIPLES

### 1. Radical Repairability
Every device uses standard screws (Phillips #1, #2), no glue, no soldered components where possible.

### 2. User Upgradeability
RAM, batteries, and storage are user-replaceable with common tools.

### 3. Open Firmware
All devices run Coreboot + SixOS, completely open-source from bootloader to userland.

### 4. Sustainable Materials
Prioritize recycled aluminum, refurbished components, and recyclable plastics.

### 5. Lifetime Design
Devices are designed for 10+ year lifespan with component upgrades.

### 6. Privacy First
No telemetry, no cloud lock-in, all data stays on your devices.

### 7. Developer Freedom
Full Clojure REPL access, s6 service customization, Nix-based configuration.

---

## 🚀 ROADMAP

### Phase 1: Design & Documentation (Current)
- ✅ Graincamera design complete
- ✅ Grainwriter design complete
- ✅ Grainpack design complete
- ✅ Agentic Pipeline architecture complete
- ✅ Ecosystem documentation

### Phase 2: Prototype Development (2025 Q2)
- 🔄 Grainwriter prototype (3D-printed enclosure)
- 🔄 Grainpack prototype (off-the-shelf eGPU enclosure)
- 🔄 SixOS integration testing
- 🔄 Clojure library development

### Phase 3: Community Testing (2025 Q3)
- ⏳ Beta tester program (50 units)
- ⏳ Feedback collection and iteration
- ⏳ Manufacturing partner selection
- ⏳ Certification (IP68, MIL-STD-810H)

### Phase 4: Production (2025 Q4)
- ⏳ Small-batch production (500 units)
- ⏳ Grain PBC incorporation
- ⏳ Distribution partnerships
- ⏳ Warranty and support infrastructure

### Phase 5: Scale (2026+)
- ⏳ Mass production (5,000+ units)
- ⏳ Graincamera prototype development
- ⏳ International distribution
- ⏳ Community-driven feature development

---

## 🌟 VISION STATEMENT

**The Grain Hardware Ecosystem represents a complete reimagining of personal computing hardware.**

Instead of planned obsolescence, we offer **lifetime devices**.  
Instead of vendor lock-in, we offer **complete openness**.  
Instead of e-waste, we offer **sustainable recycling**.  
Instead of proprietary software, we offer **Clojure and SixOS**.

**Every device in the Grain ecosystem is designed to be:**
- Owned by you, not rented
- Repairable by you, not the manufacturer
- Upgradeable by you, not obsolete
- Programmable by you, not locked down

**Together, these devices create a computing environment that serves you, not corporations. A writing environment that respects your focus, not your attention. A development environment that values your time, not your data.**

**This is personal sovereignty in hardware form.** 🌾

---

**Ecosystem Version:** 1.0  
**Last Updated:** January 22, 2025  
**Authority:** kae3g (Graingalaxy)  
**Status:** Design Phase

---

**Grain Hardware Ecosystem**  
**Sustainable, open-source computing for personal sovereignty** 🔧

*"From photography to writing to development, all under your control."*

