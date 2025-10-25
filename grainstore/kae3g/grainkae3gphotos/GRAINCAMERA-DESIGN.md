# 📸 Graincamera: Open-Hardware AVIF Camera
## *"Every Great Image Starts With a Single Grain"*

**Project:** Open-source digital camera with native AVIF support  
**Target:** Personal sovereignty in photography  
**License:** CERN-OHL-S-2.0 (Open Hardware License)  
**Created:** January 2025

---

## 🎯 VISION

**Graincamera** is an open-hardware digital camera designed for the grainkae3g ecosystem, featuring:

- ✅ **Native AVIF support** - First camera with built-in AVIF encoding
- ✅ **RISC-V processor** - Open instruction set architecture
- ✅ **Fujifilm X-mount compatibility** - Use existing lenses
- ✅ **Nostr integration** - Decentralized photo sharing
- ✅ **ICP storage** - Immutable photo storage on Internet Computer
- ✅ **Clojure firmware** - Programmable in Clojure via Humble UI
- ✅ **s6 supervision** - Reliable camera operations

---

## 🏗️ HARDWARE ARCHITECTURE

### Core Components

#### 1. Image Sensor
**Sony IMX571** (APS-C, 26.1MP)
- **Why:** Excellent image quality, good low-light performance
- **Compatibility:** Fujifilm X-mount lenses
- **Resolution:** 6240×4160 pixels
- **Format:** Raw Bayer + processed output

#### 2. Main Processor
**RISC-V SoC** (Custom design)
- **Core:** SiFive U74 quad-core @ 1.5GHz
- **Memory:** 8GB LPDDR4X
- **Storage:** 256GB eUFS 3.1
- **GPU:** Custom RISC-V vector unit for image processing

#### 3. Image Processing Unit (IPU)
**FPGA-based** (Xilinx Zynq UltraScale+)
- **Purpose:** Real-time AVIF encoding
- **Features:** Hardware-accelerated AV1 encoding
- **Throughput:** 4K@30fps AVIF encoding
- **Power:** 2W typical consumption

#### 4. Connectivity
- **WiFi 6E:** 802.11ax (6GHz band)
- **Bluetooth 5.2:** LE Audio support
- **USB-C:** Power, data, video out
- **Ethernet:** Gigabit (via USB-C adapter)
- **SD Card:** UHS-II slot (backup storage)

#### 5. Display & Controls
- **LCD:** 3.2" 2.1MP touchscreen (Sony Xperia 1 III panel)
- **EVF:** 0.5" 3.69MP OLED (Sony A7R V style)
- **Controls:** Physical dials + touch interface
- **Haptic:** Linear actuator for tactile feedback

---

## 💻 SOFTWARE ARCHITECTURE

### Firmware Stack

```
┌─────────────────────────────────────┐
│           Humble UI                 │  ← Clojure desktop interface
├─────────────────────────────────────┤
│         Clojure Runtime             │  ← Babashka + JVM
├─────────────────────────────────────┤
│         s6 Supervision             │  ← Process management
├─────────────────────────────────────┤
│      Camera Control Layer           │  ← Sensor, lens, flash
├─────────────────────────────────────┤
│      Image Processing Pipeline      │  ← Raw → AVIF conversion
├─────────────────────────────────────┤
│        Storage Layer                │  ← Local + Nostr + ICP
├─────────────────────────────────────┤
│         RISC-V Linux                │  ← Debian-based OS
└─────────────────────────────────────┘
```

### Key Software Components

#### 1. Camera Control (`grain-camera.core`)
```clojure
(ns grain-camera.core
  "Main camera control and user interface")

(defn capture-photo [settings]
  "Capture photo with specified settings"
  (-> (initialize-sensor)
      (configure-exposure settings)
      (capture-raw-image)
      (process-to-avif)
      (extract-metadata)
      (store-locally)
      (upload-to-nostr)
      (store-on-icp)))
```

#### 2. Image Processing (`grain-camera.avif`)
```clojure
(ns grain-camera.avif
  "AVIF encoding and processing")

(defn raw-to-avif [raw-data settings]
  "Convert raw sensor data to AVIF"
  (-> raw-data
      (demosaic-bayer)
      (apply-color-profile)
      (tone-mapping)
      (avif-encode settings)))
```

#### 3. Nostr Integration (`grain-camera.nostr`)
```clojure
(ns grain-camera.nostr
  "Decentralized photo sharing")

(defn publish-photo [photo-metadata]
  "Publish photo to Nostr relays"
  (let [event (create-note-event photo-metadata)]
    (publish-to-relays event)))
```

#### 4. ICP Storage (`grain-camera.icp`)
```clojure
(ns grain-camera.icp
  "Immutable photo storage on Internet Computer")

(defn store-photo [photo-data metadata]
  "Store photo permanently on ICP"
  (let [canister-id (get-photo-canister)]
    (call-canister canister-id :store-photo
                   {:data photo-data
                    :metadata metadata})))
```

---

## 🔧 DEVELOPMENT ROADMAP

### Phase 1: Proof of Concept (Months 1-3)
- [ ] Design RISC-V SoC with image processing capabilities
- [ ] Create FPGA-based AVIF encoder
- [ ] Build basic camera control software in Clojure
- [ ] Test with existing Fujifilm lenses
- [ ] Implement basic AVIF capture

### Phase 2: Prototype (Months 4-6)
- [ ] Build first hardware prototype
- [ ] Implement full image processing pipeline
- [ ] Add Nostr integration for photo sharing
- [ ] Create Humble UI camera interface
- [ ] Test with real-world photography

### Phase 3: Production (Months 7-12)
- [ ] Optimize power consumption
- [ ] Add ICP storage integration
- [ ] Implement advanced features (AI tagging, etc.)
- [ ] Create manufacturing documentation
- [ ] Launch crowdfunding campaign

### Phase 4: Community (Year 2+)
- [ ] Open-source all hardware designs
- [ ] Create developer documentation
- [ ] Build community around Graincamera
- [ ] Develop ecosystem of accessories
- [ ] Expand to video recording (AVIF video)

---

## 💰 COST BREAKDOWN

### Development Costs
| Component | Cost | Notes |
|-----------|------|-------|
| RISC-V SoC Design | $50,000 | Custom chip design |
| FPGA Development | $25,000 | AVIF encoder IP |
| PCB Design | $10,000 | Multi-layer board |
| Prototype Manufacturing | $15,000 | 10 units |
| Software Development | $30,000 | Clojure firmware |
| **Total Development** | **$130,000** | |

### Production Costs (per unit)
| Component | Cost | Notes |
|-----------|------|-------|
| Sony IMX571 Sensor | $80 | Image sensor |
| RISC-V SoC | $25 | Custom processor |
| FPGA | $35 | Image processing |
| Memory (8GB) | $20 | LPDDR4X |
| Storage (256GB) | $15 | eUFS 3.1 |
| Display | $45 | Touchscreen + EVF |
| Lens Mount | $15 | Fujifilm X-mount |
| PCB + Assembly | $30 | Manufacturing |
| Case + Controls | $25 | Mechanical parts |
| **Total BOM** | **$290** | |
| **Target Price** | **$899** | 3x BOM for sustainability |

---

## 🌐 ECOSYSTEM INTEGRATION

### With grainkae3g Stack
- **grainphotos** - Photo management and processing
- **clojure-s6** - Camera process supervision
- **grainspace** - Photo marketplace and sharing
- **Humble Social Client** - Photo sharing interface

### With Existing Systems
- **Fujifilm Lenses** - Full X-mount compatibility
- **Adobe Lightroom** - Raw file support
- **Nostr Relays** - Decentralized photo sharing
- **ICP Canisters** - Immutable photo storage

---

## 🔬 TECHNICAL SPECIFICATIONS

### Image Quality
- **Sensor:** Sony IMX571 (APS-C, 26.1MP)
- **Lens Mount:** Fujifilm X-mount
- **Image Formats:** Raw (RAF), AVIF, HEIC, JPEG
- **Video:** 4K@30fps AVIF, 1080p@60fps
- **ISO Range:** 100-51,200 (extended to 204,800)

### Performance
- **Startup Time:** <1 second
- **Burst Mode:** 15fps (AVIF), 30fps (JPEG)
- **Buffer:** 50+ AVIF images
- **Battery Life:** 500+ shots (CIPA standard)

### Connectivity
- **WiFi:** 802.11ax (6GHz)
- **Bluetooth:** 5.2 LE Audio
- **USB:** USB-C 3.2 Gen 2
- **Storage:** SD UHS-II, internal 256GB

---

## 🎨 USER INTERFACE

### Physical Controls
- **Mode Dial:** Manual, Aperture Priority, Shutter Priority, Auto
- **Aperture Ring:** Direct lens control
- **Shutter Speed Dial:** 1/8000s to 30s + Bulb
- **ISO Button:** Quick ISO selection
- **Focus Ring:** Manual focus with peaking
- **Custom Buttons:** 3 programmable Fn buttons

### Touch Interface
- **Live View:** Touch to focus, drag to adjust exposure
- **Menu System:** Hierarchical settings organization
- **Photo Review:** Swipe to navigate, pinch to zoom
- **Settings:** Real-time parameter adjustment

### Humble UI Features
- **Customizable Layouts:** Drag-and-drop interface elements
- **Scripting:** Write custom camera behaviors in Clojure
- **Plugins:** Install community-developed features
- **Themes:** Customize appearance and behavior

---

## 📚 OPEN SOURCE COMPONENTS

### Hardware
- **PCB Design:** KiCad files
- **Mechanical:** FreeCAD 3D models
- **Schematics:** Complete circuit diagrams
- **BOM:** Bill of materials with suppliers

### Software
- **Firmware:** Complete Clojure source code
- **Drivers:** RISC-V Linux drivers
- **FPGA IP:** Verilog/VHDL for AVIF encoder
- **Documentation:** Comprehensive developer guides

### Community
- **GitHub:** All code and hardware files
- **Discord:** Developer and user community
- **Wiki:** Documentation and tutorials
- **Forums:** Technical discussions and support

---

## 🚀 LAUNCH STRATEGY

### Crowdfunding Campaign
- **Platform:** Kickstarter + self-hosted
- **Goal:** $500,000 (500 units)
- **Timeline:** 6-month campaign
- **Rewards:** Early bird pricing, exclusive colors

### Community Building
- **Developer Program:** Early access for contributors
- **Photography Contests:** Showcase Graincamera capabilities
- **Workshops:** Teach open-source photography
- **Documentation:** Comprehensive guides and tutorials

### Partnerships
- **Fujifilm:** Lens compatibility and support
- **Nostr Relays:** Photo sharing infrastructure
- **ICP Foundation:** Storage and identity
- **Open Source Hardware:** Collaboration with other projects

---

## 🌟 VISION STATEMENT

**Graincamera** represents the future of photography: open, decentralized, and user-controlled. By combining cutting-edge hardware with open-source software, we're creating a camera that photographers can truly own, modify, and improve.

**"Every great image starts with a single grain."**

Just as the grainkae3g project builds personal sovereignty in computing, Graincamera builds personal sovereignty in photography. No more vendor lock-in, no more proprietary formats, no more surveillance capitalism in our creative tools.

**The future of photography is open. The future is Graincamera.** 📸

---

**Project Lead:** kae3g  
**Community:** [github.com/grainnetwork/graincamera](https://github.com/grainnetwork/graincamera)  
**License:** CERN-OHL-S-2.0 (Hardware) + MIT (Software)  
**Status:** Design Phase

*"Building the camera that photographers deserve."* 🌾

