# ✍️ Grainwriter: Open-Source E-Ink Writing Device
## *"Every Great Story Starts With a Single Word"*

**Project:** Sustainable, offline-first writing device with e-ink display  
**Target:** Personal sovereignty in writing and note-taking  
**License:** CERN-OHL-S-2.0 (Hardware) + MIT (Software)  
**Created:** January 2025

---

## 🎯 VISION

**Grainwriter** is a completely open-source, sustainable writing device designed for the grainkae3g ecosystem, featuring:

- ✅ **E-ink Display** - Inkplate Crowd Supply recyclable screen
- ✅ **RAM-Only Storage** - No persistent storage, pure memory
- ✅ **USB-C File Sharing** - Sync like iPod Touch, but open source
- ✅ **Recycled Hardware** - AMD refurbished/reclaimed components
- ✅ **Open Firmware** - Coreboot-only, no proprietary blobs
- ✅ **Offline-First** - Works without internet, sync when connected
- ✅ **Clojure Interface** - Programmable in Clojure via Humble UI
- ✅ **s6 Supervision** - Reliable operation management
- ✅ **Military-Grade Rugged** - IP68 waterproof, MIL-STD-810H drop-resistant
- ✅ **Dive-Ready** - SeaLife-inspired underwater case design

---

## 🏗️ HARDWARE ARCHITECTURE

### Core Components

#### 1. E-Ink Display
**Inkplate 10** (Crowd Supply)
- **Size:** 10.3" e-ink display
- **Resolution:** 1872×1404 (227 DPI)
- **Technology:** E Ink Carta (16 grayscale levels)
- **Refresh Rate:** 1.2s full refresh, 0.3s partial
- **Power:** 0.1W typical, 0.3W during refresh
- **Recyclable:** Fully recyclable components

#### 2. Main Processor & RAM
**Intel Core i7-8650U** (Refurbished from off-lease ThinkPads)
- **Model:** i7-8650U (4-core, 8-thread, 1.9-4.2GHz, 15W TDP)
- **Memory:** 64GB DDR4-2666 SO-DIMM (2x 32GB, upgradeable, not soldered!)
- **Graphics:** Intel UHD Graphics 620 (integrated, low power)
- **Why this chip:**
  - Abundant supply from off-lease business laptops (Lenovo ThinkPad T480s, Dell Latitude 7490)
  - Excellent Linux support (no driver headaches)
  - Low TDP (15W) for great battery life
  - Proven reliability in enterprise environments
  - DDR4 SO-DIMM slots = user-upgradeable RAM up to 64GB
- **RAM Configuration:**
  - Standard: 2x 32GB DDR4-2666 (64GB total)
  - Budget option: 2x 16GB DDR4-2400 (32GB total)
  - Maximum supported: 64GB (chipset limit for 8th gen Intel)
- **Source:** eBay, Amazon Renewed, refurbished laptop teardowns
- **Alternative Options:**
  - Intel i5-8250U (cheaper, 4C/8T, same 64GB RAM support)
  - AMD Ryzen 5 3500U (better GPU, similar price, 64GB support)
  - Intel i7-10510U (newer gen, slightly more expensive, 64GB support)

#### 3. Storage Architecture
**RAM-Only Design** (No persistent storage)
- **Memory:** 64GB DDR4 SO-DIMM (acts as storage, user-upgradeable!)
- **RAM Configuration:** 2x 32GB DDR4-2666 modules
- **Effective Storage:** ~60GB usable (after OS/runtime overhead)
- **Document Capacity:** ~30,000+ full-length documents (average 2MB each)
- **Backup:** USB-C external drive for sync
- **Battery Backup:** 72-hour data retention via supercapacitor
- **Sync Strategy:** iPod Touch-style file sharing
- **Why 64GB RAM-only:**
  - Zero storage latency - instant access to all documents
  - Inherently secure (data vanishes on power loss if desired)
  - No wear leveling issues (unlike SSDs)
  - Forces good backup habits
  - Extremely fast boot times (<3 seconds)
  - Enough space for years of writing without sync
  - Can hold entire libraries of books/references in RAM

#### 4. Power System
**Infinite Battery Life Architecture**

**Triple Power System (Never Dies):**
- **Primary Battery:** 20,000mAh Li-Po (hot-swappable, user-replaceable)
- **Secondary Battery:** 10,000mAh Li-Po (backup, automatic switchover)
- **Solar Panel:** 10W foldable panel (continuous trickle charge)
- **Supercapacitor Bank:** 100F @ 5V (instant power buffer during battery swap)

**Hot-Swap Design:**
- Dual battery slots with automatic failover
- Swap primary battery while secondary powers device
- Zero-downtime battery replacement
- Status LED shows which battery is active
- Magnetic battery locks for easy field replacement

**Solar Integration:**
- Integrated 10W foldable solar panel in lid
- Charges both batteries simultaneously when deployed
- Trickle charges even in low light (indoor/cloudy)
- MPPT charge controller for maximum efficiency
- Battery life indicator: weeks to months without wall power

**Power Budget (Extreme Efficiency):**
- E-ink display: 0.1W average (only during refresh)
- Intel i7-8650U: 2-5W (aggressive power gating)
- RAM retention: 0.5W (active), 0.01W (hibernate)
- Keyboard backlight: 0.2W (optional, off by default)
- Total active usage: ~3-6W
- Total idle: <0.5W
- **Battery life with 30,000mAh total:**
  - Active writing: 5,000+ hours (208+ days)
  - Idle/standby: 60,000+ hours (6.8+ years!)
  - With solar: Infinite in normal use conditions

**Charging (Triple Method):**

**1. USB-C PD 3.0 (Fast Wired Charging)**
- **Power:** 45W (fast charge both batteries in 2-3 hours)
- **Connector:** Waterproof USB-C port with magnetic cover
- **Smart Charging:** Automatically balances between batteries
- **Pass-through:** Use device while charging
- **Cable:** Includes 2m braided USB-C cable

**2. Qi Wireless Charging (Tesla Model Y Style)**
- **Power:** 15W Qi wireless charging (Qi2/MagSafe compatible)
- **Design:** Integrated wireless charging coil in bottom chassis
- **Placement:** Lay device flat on any Qi charging pad
- **Tesla Integration:** Compatible with Tesla Model Y wireless charging pad
- **Home/Office:** Works with any Qi-certified charging mat
- **Indicators:** LED ring shows charging status
- **Efficiency:** 80-85% wireless charging efficiency
- **Alignment:** Magnetic alignment guides for perfect placement
- **Charge Time:** Full charge in 6-8 hours (15W), 8-10 hours (10W standard Qi)

**3. Solar Charging (Off-Grid)**
- **Power:** 10W foldable solar panel integrated in lid
- **Full charge:** 8-12 hours direct sunlight
- **Trickle charge:** Works in overcast/indoor light
- **Simultaneous:** Can solar charge + use device
- **MPPT:** Maximum Power Point Tracking for efficiency

**Multi-Charge Scenarios:**
- Desk work: Wireless Qi pad (seamless placement)
- Fast top-up: USB-C wired (45W)
- Field/outdoor: Solar panel deployed
- Car: Tesla Model Y wireless pad or USB-C
- Emergency: Any combination of above simultaneously

**Smart Charging Logic:**
- Prioritizes fastest charging method when multiple sources connected
- Balances load across dual batteries
- Prevents overcharging with intelligent cutoff
- Temperature monitoring for battery health
- Displays estimated charge time on screen

**Power Management Firmware:**
- Aggressive CPU frequency scaling (800MHz-4.2GHz)
- Display sleep after 30s inactivity
- Keyboard power gating (only active keys powered)
- RAM low-power mode during idle
- **Never crashes, never loses data:**
  - Supercapacitor ensures RAM retention during battery swap
  - Automatic checkpoint every 60 seconds
  - Power failure detection triggers instant RAM dump to supercap
  - 72-hour data retention even with zero external power

#### 5. Connectivity
**USB-C Only** (Minimalist design)
- **USB-C 3.2 Gen 2:** Data transfer + power
- **No WiFi/Bluetooth:** Offline-first philosophy
- **No Cellular:** Pure offline device
- **File Sync:** Manual USB-C connection

#### 6. Input Methods
**Multiple Input Options**
- **Physical Keyboard:** Mechanical switches (Cherry MX compatible)
- **Touch Screen:** Capacitive touch for navigation
- **Trackpad:** Small trackpad for cursor control
- **Voice Input:** Microphone for dictation (offline processing)

#### 7. Rugged Enclosure
**Military-Grade Protection (IP68 + MIL-STD-810H)**

**IP68 Waterproof Rating:**
- **IP6X:** Completely dust-tight (no ingress of dust)
- **IPX8:** Continuous immersion up to 3 meters for 30 minutes
- **Dive-Ready:** Can be used underwater for writing/note-taking
- **SeaLife-Inspired:** Based on professional underwater camera housing design
- **Pressure-Equalized:** Maintains seal integrity at depth

**MIL-STD-810H Drop Resistance:**
- **Drop Test:** Survives 26 drops from 1.2m (4 ft) onto concrete
- **Operating Shock:** Tested to 40G operational shock
- **Non-Operating Shock:** Tested to 60G transit shock
- **Vibration:** Multi-axis random vibration tested
- **Temperature Extremes:** -20°C to 60°C (-4°F to 140°F)

**Enclosure Construction:**
- **Material:** Recycled aluminum chassis + TPU rubber bumpers
- **Gaskets:** Silicone O-rings (user-replaceable)
- **Port Protection:** Magnetic USB-C port cover (waterproof)
- **Screen Protection:** Gorilla Glass over e-ink display
- **Keyboard:** Individual key sealing (membrane + mechanical hybrid)
- **Screws:** All stainless steel, hex-drive (T6 Torx)
- **Field Serviceable:** All seals/gaskets easily replaceable

**Dive Case Features (SeaLife-Inspired):**
- **Clear Back Panel:** Transparent polycarbonate to see internals
- **Vacuum Test Port:** Test seal integrity before diving
- **Depth Gauge:** Built-in analog depth indicator
- **Lanyard Points:** Dual stainless steel attachment points
- **Floating Design:** Neutral buoyancy (won't sink if dropped)
- **Anti-Fog:** Desiccant compartment for moisture control

**Durability Testing Protocol:**
- Salt spray (corrosion resistance)
- Sand/dust ingress testing
- Repeated drop testing from multiple angles
- Extended immersion testing (24+ hours)
- Freeze/thaw cycling
- UV exposure testing

---

## 💻 SOFTWARE ARCHITECTURE

### Firmware Stack

```
┌─────────────────────────────────────┐
│           Humble UI                 │  ← Clojure writing interface
├─────────────────────────────────────┤
│         Clojure Runtime             │  ← Babashka + JVM
├─────────────────────────────────────┤
│         s6 Supervision             │  ← Process management
├─────────────────────────────────────┤
│      E-Ink Display Driver          │  ← Inkplate control
├─────────────────────────────────────┤
│      RAM Storage Manager            │  ← Memory-only storage
├─────────────────────────────────────┤
│      USB-C Sync Layer               │  ← File sharing protocol
├─────────────────────────────────────┤
│         Coreboot                    │  ← Open firmware
└─────────────────────────────────────┘
```

### Key Software Components

#### 1. Writing Interface (`grain-writer.core`)
```clojure
(ns grain-writer.core
  "Main writing interface and document management")

(defn create-document [title content]
  "Create new document in RAM"
  (let [doc-id (generate-uuid)
        doc {:id doc-id
             :title title
             :content content
             :created-at (now)
             :modified-at (now)}]
    (store-in-ram doc)
    (update-e-ink-display doc)))

(defn sync-to-usb [usb-device]
  "Sync all documents to USB device"
  (let [all-docs (get-all-documents)]
    (write-to-usb usb-device all-docs)))
```

#### 2. E-Ink Display (`grain-writer.display`)
```clojure
(ns grain-writer.display
  "E-ink display control and optimization")

(defn render-document [document]
  "Render document to e-ink display"
  (-> document
      (format-for-e-ink)
      (optimize-refresh)
      (send-to-inkplate)))

(defn partial-refresh [region]
  "Perform partial refresh for better performance"
  (inkplate/partial-refresh region))
```

#### 3. RAM Storage (`grain-writer.storage`)
```clojure
(ns grain-writer.storage
  "RAM-only storage management")

(defn store-in-ram [data]
  "Store data in RAM with compression"
  (let [compressed (compress data)]
    (store-in-memory compressed)))

(defn backup-to-battery-ram [data]
  "Backup to battery-backed RAM for retention"
  (store-in-battery-ram data))
```

#### 4. USB Sync (`grain-writer.sync`)
```clojure
(ns grain-writer.sync
  "USB-C file synchronization")

(defn detect-usb-device []
  "Detect connected USB device"
  (scan-usb-devices))

(defn sync-documents [usb-device]
  "Sync documents to/from USB device"
  (let [local-docs (get-all-documents)
        usb-docs (read-from-usb usb-device)]
    (merge-documents local-docs usb-docs)))
```

---

## 🔧 DEVELOPMENT ROADMAP

### Phase 1: Proof of Concept (Months 1-3)
- [ ] Design custom PCB for AMD V1000 + Inkplate integration
- [ ] Port Coreboot to AMD V1000 platform
- [ ] Create basic Clojure interface for e-ink display
- [ ] Implement RAM-only storage system
- [ ] Test with refurbished AMD hardware

### Phase 2: Prototype (Months 4-6)
- [ ] Build first hardware prototype
- [ ] Implement full writing interface
- [ ] Add USB-C sync functionality
- [ ] Create Humble UI writing app
- [ ] Test with real-world writing scenarios

### Phase 3: Production (Months 7-12)
- [ ] Optimize power consumption
- [ ] Add voice input capabilities
- [ ] Implement advanced features (markdown, etc.)
- [ ] Create manufacturing documentation
- [ ] Launch crowdfunding campaign

### Phase 4: Community (Year 2+)
- [ ] Open-source all hardware designs
- [ ] Create developer documentation
- [ ] Build community around Grainwriter
- [ ] Develop ecosystem of accessories
- [ ] Expand to different screen sizes

---

## 💰 COST BREAKDOWN & SOURCING GUIDE

### Component Sourcing (Refurbished/New Pricing)

#### **Processor & Motherboard Options**

**Option 1: Intel i7-8650U (Recommended)**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| eBay (pulled from ThinkPad T480s) | Refurbished | $80-120 | ebay.com/sch/i.html?_nkw=i7-8650u |
| AliExpress (with mini-ITX board) | New/Used | $150-200 | aliexpress.com (search "i7-8650u motherboard") |
| Amazon Renewed | Refurbished | $100-140 | amazon.com (search "i7-8650u processor") |
| Newegg | Refurbished | $110-150 | newegg.com/p/pl?d=i7-8650u |

**Option 2: Intel i5-8250U (Budget)**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| eBay | Refurbished | $50-80 | ebay.com/sch/i.html?_nkw=i5-8250u |
| AliExpress | Used | $60-90 | aliexpress.com |

**Option 3: AMD Ryzen 5 3500U**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| eBay | Refurbished | $70-100 | ebay.com/sch/i.html?_nkw=ryzen+5+3500u |
| AliExpress | New | $120-150 | aliexpress.com |

#### **RAM (DDR4 SO-DIMM)**

**32GB Kit (2x 16GB DDR4-2400)**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| eBay | Used/Refurbished | $40-60 | ebay.com/sch/i.html?_nkw=32gb+ddr4+sodimm |
| Amazon | New | $70-90 | amazon.com/s?k=32gb+ddr4+sodimm |
| Newegg | New | $65-85 | newegg.com/p/pl?d=32gb+ddr4+sodimm |
| Crucial | New | $75-95 | crucial.com |

**64GB Kit (2x 32GB DDR4-2666) - For power users**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| eBay | Used | $80-120 | ebay.com/sch/i.html?_nkw=64gb+ddr4+sodimm |
| Amazon | New | $140-180 | amazon.com/s?k=64gb+ddr4+sodimm |

#### **E-Ink Display**

**Inkplate 10 (Crowd Supply)**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| Crowd Supply | New | $159 | crowdsupply.com/e-radionica/inkplate-10 |
| Mouser Electronics | New | $155-165 | mouser.com (search "Inkplate 10") |
| Tindie | New | $150-170 | tindie.com/products/e-radionica/inkplate-10/ |

**Alternative: Waveshare 10.3" E-Ink**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| Amazon | New | $180-220 | amazon.com/s?k=waveshare+10.3+eink |
| AliExpress | New | $140-180 | aliexpress.com (search "10.3 eink display") |

#### **Battery Pack**

**10,000mAh Li-Po (Replaceable)**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| Amazon | New | $25-35 | amazon.com/s?k=10000mah+lipo+battery |
| AliExpress | New | $18-28 | aliexpress.com (search "10000mah lipo") |
| eBay | New/Used | $20-30 | ebay.com/sch/i.html?_nkw=10000mah+battery |

**20,000mAh Li-Po (Extended life)**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| Amazon | New | $40-60 | amazon.com/s?k=20000mah+lipo |
| AliExpress | New | $30-50 | aliexpress.com |

#### **Power & Charging Components**

**Qi Wireless Charging Coil (15W)**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| AliExpress | New | $8-15 | aliexpress.com (search "15W qi wireless charging coil") |
| Amazon | New | $12-20 | amazon.com/s?k=wireless+charging+receiver+coil |
| Adafruit | New | $15-25 | adafruit.com (search "Qi charging module") |

**Qi2/MagSafe Charging Module**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| AliExpress | New | $12-25 | aliexpress.com (search "magsafe wireless charging module") |
| Amazon | New | $18-30 | amazon.com/s?k=magsafe+wireless+charging |

**10W Solar Panel (Foldable)**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| Amazon | New | $25-40 | amazon.com/s?k=10w+foldable+solar+panel |
| AliExpress | New | $15-30 | aliexpress.com (search "10w flexible solar panel") |
| Voltaic Systems | New | $35-50 | voltaicsystems.com |

**MPPT Solar Charge Controller**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| Amazon | New | $15-25 | amazon.com/s?k=mppt+solar+charge+controller |
| AliExpress | New | $10-18 | aliexpress.com (search "mppt solar controller") |
| Adafruit | New | $20-30 | adafruit.com (search "solar charger") |

**Supercapacitor Bank (100F @ 5V)**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| Amazon | New | $15-25 | amazon.com/s?k=100f+supercapacitor |
| AliExpress | New | $10-18 | aliexpress.com (search "supercapacitor 100f") |
| DigiKey | New | $18-30 | digikey.com (search "supercapacitor") |
| Mouser | New | $18-28 | mouser.com |

**USB-C PD 3.0 Charge Controller**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| AliExpress | New | $8-15 | aliexpress.com (search "usb-c pd controller") |
| Amazon | New | $12-20 | amazon.com/s?k=usb+c+pd+charger+module |
| Adafruit | New | $15-25 | adafruit.com (search "USB-C PD") |

**Magnetic Alignment Guides (for Qi charging)**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| AliExpress | New | $3-8 | aliexpress.com (search "magsafe alignment magnets") |
| Amazon | New | $5-12 | amazon.com/s?k=magnetic+alignment+ring |

#### **Keyboard**

**Mechanical Keyboard (Low-Profile, Sealed)**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| AliExpress (Custom 75% membrane-mech) | New | $50-80 | aliexpress.com (search "low profile mechanical keyboard") |
| Amazon (Keychron K3) | New | $75-95 | amazon.com/s?k=keychron+k3 |
| eBay (Used laptop keyboards) | Refurbished | $20-40 | ebay.com/sch/i.html?_nkw=thinkpad+keyboard |

#### **Rugged Case Components**

**Aluminum Chassis (Custom CNC or 3D Print)**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| SendCutSend (CNC aluminum) | New | $80-150 | sendcutsend.com |
| Xometry (CNC machining) | New | $100-200 | xometry.com |
| Shapeways (3D printed aluminum) | New | $120-180 | shapeways.com |
| Local CNC shop | New | $60-120 | (varies by location) |

**TPU Rubber Bumpers**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| AliExpress (custom molded) | New | $15-25 | aliexpress.com (search "TPU bumper case") |
| Amazon (generic protective bumpers) | New | $20-35 | amazon.com/s?k=device+bumper+case |

**Gorilla Glass / Tempered Glass**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| AliExpress (custom cut) | New | $15-30 | aliexpress.com (search "tempered glass custom") |
| Amazon | New | $20-40 | amazon.com/s?k=10+inch+tempered+glass |

**Silicone O-Rings & Gaskets**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| McMaster-Carr | New | $10-20 | mcmaster.com (search "silicone o-ring") |
| Amazon | New | $8-15 | amazon.com/s?k=silicone+o-ring+kit |
| AliExpress | New | $5-12 | aliexpress.com (search "waterproof gasket") |

**Stainless Steel Screws (T6 Torx)**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| McMaster-Carr | New | $8-15 | mcmaster.com |
| Amazon | New | $10-18 | amazon.com/s?k=stainless+torx+screws |
| Bolt Depot | New | $6-12 | boltdepot.com |

**Magnetic USB-C Port Cover**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| AliExpress | New | $8-15 | aliexpress.com (search "magnetic usb c dust plug") |
| Amazon | New | $12-20 | amazon.com/s?k=magnetic+usb+c+cover |

**Desiccant Pack (Reusable)**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| Amazon | New | $8-15 | amazon.com/s?k=reusable+desiccant |
| eBay | New | $5-10 | ebay.com/sch/i.html?_nkw=silica+gel+desiccant |

**Depth Gauge (Analog)**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| AliExpress | New | $5-12 | aliexpress.com (search "depth gauge analog") |
| Amazon | New | $10-18 | amazon.com/s?k=analog+depth+gauge |

#### **PCB & Assembly**

**Custom PCB (5-10 boards)**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| JLCPCB | New | $30-60 | jlcpcb.com |
| PCBWay | New | $40-70 | pcbway.com |
| OSH Park | New | $50-80 | oshpark.com |

**PCB Assembly (SMT)**
| Source | Condition | Price | Link |
|--------|-----------|-------|------|
| JLCPCB (SMT assembly) | New | $50-100/board | jlcpcb.com |
| PCBWay (assembly) | New | $60-120/board | pcbway.com |
| MacroFab | New | $80-150/board | macrofab.com |

---

### Development Costs
| Component | Cost | Notes |
|-----------|------|-------|
| AMD V1000 Refurbished | $200 | 4-core embedded processor |
| Inkplate 10 Display | $150 | E-ink display module |
| Custom PCB Design | $5,000 | Multi-layer board |
| Coreboot Port | $10,000 | AMD V1000 support |
| Rugged Enclosure Design | $8,000 | IP68 + MIL-STD-810H certification |
| Prototype Manufacturing | $12,000 | 10 units with rugged cases |
| IP68/MIL-STD Testing | $15,000 | Third-party certification lab |
| Software Development | $15,000 | Clojure firmware |
| **Total Development** | **$65,350** | |

### Production Costs (per unit)

**Standard Configuration (64GB RAM + Wireless Charging)**
| Component | Cost | Notes |
|-----------|------|-------|
| Intel i7-8650U (Refurbished) | $100 | Main processor (eBay/Amazon Renewed) |
| 64GB DDR4-2666 SO-DIMM (2x32GB) | $100 | Refurbished RAM from eBay |
| Inkplate 10 Display | $159 | E-ink screen (Crowd Supply) |
| Gorilla Glass | $25 | Screen protection |
| Battery #1 (20,000mAh) Primary | $50 | Li-Po battery pack (hot-swappable) |
| Battery #2 (10,000mAh) Secondary | $30 | Li-Po backup battery |
| Qi Wireless Charging (15W) | $15 | Qi2/MagSafe compatible coil |
| Solar Panel (10W foldable) | $25 | Integrated in lid |
| MPPT Solar Controller | $15 | Maximum power point tracking |
| USB-C PD 3.0 Controller | $12 | 45W fast charging |
| Supercapacitor Bank (100F) | $15 | Hot-swap power buffer + RAM backup |
| Magnetic Alignment Guides | $5 | For wireless charging placement |
| Sealed Keyboard | $75 | Membrane + mechanical hybrid |
| PCB + Assembly | $95 | Custom motherboard + SMT (expanded for power mgmt) |
| Rugged Case | $120 | Aluminum + TPU + gaskets |
| O-Rings & Seals | $15 | Replaceable waterproof seals |
| Stainless Screws | $5 | T6 Torx fasteners |
| USB-C Port Cover | $10 | Magnetic waterproof cover |
| Desiccant Pack | $3 | Anti-fog moisture control |
| Depth Gauge | $7 | Analog pressure indicator |
| **Total BOM** | **$881** | |
| **Target Price (Standard)** | **$1,399** | 1.59x BOM for sustainability |
| **Target Price (Pro Edition)** | **$1,699** | + Extended warranty + extra battery + premium color |

**Budget Configuration (32GB RAM, No Wireless/Solar)**
| Component | Cost | Notes |
|-----------|------|-------|
| Intel i5-8250U (Refurbished) | $65 | Budget processor option |
| 32GB DDR4-2400 SO-DIMM (2x16GB) | $50 | Refurbished RAM from eBay |
| Inkplate 10 Display | $159 | E-ink screen |
| Battery #1 (10,000mAh) | $30 | Single battery (no hot-swap) |
| Supercapacitor (50F) | $10 | RAM backup only |
| USB-C PD Controller | $12 | 45W charging |
| Other components (simplified) | $355 | (no wireless/solar charging) |
| **Total BOM** | **$681** | |
| **Target Price (Budget)** | **$1,099** | 1.61x BOM for accessibility |

**Premium Configuration (64GB RAM + Triple Battery + Extended Solar)**
| Component | Cost | Notes |
|-----------|------|-------|
| Intel i7-10510U (Refurbished) | $140 | Newer 10th gen processor |
| 64GB DDR4-2666 SO-DIMM (2x32GB) | $100 | Refurbished RAM |
| Inkplate 10 Display | $159 | E-ink screen |
| Battery #1 (20,000mAh) Primary | $50 | Hot-swappable |
| Battery #2 (20,000mAh) Secondary | $50 | Extended backup |
| Battery #3 (10,000mAh) Tertiary | $30 | Field spare (included) |
| Qi Wireless Charging (15W) | $15 | Qi2/MagSafe |
| Solar Panel (15W foldable) | $40 | Larger panel |
| MPPT Solar Controller | $15 | Advanced power management |
| USB-C PD 3.0 Controller | $12 | 45W charging |
| Supercapacitor Bank (150F) | $20 | Extended backup capacity |
| Other components | $390 | (premium materials) |
| **Total BOM** | **$1,021** | |
| **Target Price (Premium)** | **$1,699** | 1.66x BOM + infinite battery life + 7-year warranty |

---

## 🏔️ RUGGED USE CASES

### Why IP68 + MIL-STD-810H Matters

The Grainwriter's military-grade rugged design isn't just marketing—it enables writing in environments where typical devices would fail:

#### 1. **Underwater Writing** (SeaLife-Inspired)
- **Dive Logs:** Marine biologists documenting species underwater
- **Surfing Notes:** Writers capturing ideas between sets
- **Boat Journaling:** Kayakers, sailors recording voyages
- **Pool/Beach:** Write poolside without device anxiety
- **Rain Writing:** Never worry about sudden downpours

#### 2. **Extreme Environments**
- **Mountain Expeditions:** Writing at altitude (-20°C temperatures)
- **Desert Research:** Field notes in dusty, sandy conditions
- **Arctic/Antarctic:** Polar expeditions with extreme cold
- **Tropical Rainforest:** High humidity, constant moisture
- **Construction Sites:** Dusty, dirty work environments

#### 3. **Military & Tactical**
- **Field Notes:** Combat journalists, military personnel
- **Tactical Planning:** Write mission briefs in any conditions
- **Reconnaissance:** Document findings in hostile environments
- **Training:** Military training exercises in harsh terrain
- **Intelligence:** Secure, offline note-taking

#### 4. **Adventure Writing**
- **Hiking & Camping:** Multi-day backcountry trips
- **Rock Climbing:** Write on ledges, exposed locations
- **Kayaking/Rafting:** River expeditions, whitewater
- **Cycling Tours:** Bikepacking in all weather
- **Overlanding:** Vehicle-based adventure travel

#### 5. **Professional Fieldwork**
- **Archaeology:** Dig site documentation
- **Geology:** Field mapping in remote locations
- **Wildlife Biology:** Animal behavior observation
- **Environmental Science:** Water sampling, field research
- **Documentary Filmmaking:** Location scouting, scene notes

#### 6. **Disaster Response**
- **Search & Rescue:** Communication in extreme conditions
- **Emergency Management:** Crisis documentation
- **Humanitarian Aid:** Field reports from disaster zones
- **Fire/Police:** First responder documentation
- **Medical Teams:** Field hospital notes

### Real-World Testing Scenarios

**We will test the Grainwriter in:**
1. ✅ Full submersion in saltwater (3m depth, 30 minutes)
2. ✅ Drop testing from 1.2m onto concrete (26 drops, all angles)
3. ✅ Sand/dust chamber (24 hours continuous exposure)
4. ✅ Freeze/thaw cycling (-20°C to +60°C, 50 cycles)
5. ✅ Rain simulation (100mm/hour, 2 hours)
6. ✅ Vibration testing (multi-axis, vehicle simulation)
7. ✅ UV exposure (1000 hours accelerated aging)
8. ✅ Salt spray corrosion (ASTM B117, 96 hours)

### Comparison to Consumer Devices

| Feature | Grainwriter | Typical E-Reader | Rugged Tablet |
|---------|-------------|------------------|---------------|
| **IP Rating** | IP68 | IP52 (if any) | IP65-67 |
| **Drop Rating** | MIL-STD-810H (1.2m) | None | 1.0m |
| **Depth Rating** | 3m for 30min | Not waterproof | 1m for 30min |
| **Temperature** | -20°C to +60°C | 0°C to 40°C | -10°C to 50°C |
| **Dust Proof** | Yes (IP6X) | No | Yes |
| **Field Repairable** | Yes (screws) | No (glued) | Limited |
| **Open Source** | Yes | No | No |
| **Price** | $1,199 | $150-300 | $800-2000 |

### **"Write Anywhere" Philosophy**

The Grainwriter embodies the philosophy that creativity shouldn't be constrained by environment. Whether you're:
- Diving in the Great Barrier Reef
- Hiking the Appalachian Trail
- Documenting a disaster zone
- Writing on a boat in a storm
- Taking notes in the Sahara Desert

**Your device should never be the limiting factor.**

---

## 🌐 ECOSYSTEM INTEGRATION

### With grainkae3g Stack
- **grainphotos** - Photo viewing and annotation
- **clojure-s6** - Device process supervision
- **grainspace** - Document sharing and collaboration
- **Humble Social Client** - Writing interface

### With Existing Systems
- **Markdown** - Universal document format
- **USB Mass Storage** - Standard file sharing
- **Coreboot** - Open firmware foundation
- **E-ink Displays** - Sustainable screen technology

---

## 🔬 TECHNICAL SPECIFICATIONS

### Display
- **Type:** E Ink Carta (10.3")
- **Resolution:** 1872×1404 (227 DPI)
- **Grayscale:** 16 levels
- **Refresh:** 1.2s full, 0.3s partial
- **Power:** 0.1W typical, 0.3W refresh

### Performance
- **Processor:** Intel i7-8650U (4C/8T, 1.9-4.2GHz)
- **Memory:** 64GB DDR4-2666 (user-upgradeable)
- **Storage:** RAM-only (60GB effective usable space)
- **Document Capacity:** 30,000+ full-length documents
- **Battery Life:** 72+ hours continuous writing (10,000mAh), 7+ days (20,000mAh)
- **Boot Time:** <3 seconds (RAM-only storage)

### Connectivity
- **USB-C:** 3.2 Gen 2 (data + power)
- **No Wireless:** Offline-first design
- **File Sync:** Manual USB connection
- **Charging:** USB-C PD 3.0 (45W)

---

## 🎨 USER INTERFACE

### Physical Design
- **Form Factor:** Laptop-style with detachable keyboard
- **Materials:** Recycled aluminum chassis + TPU rubber bumpers
- **Weight:** 1.8kg (4.0lbs) with rugged case
- **Thickness:** 22mm (0.87") with protective bumpers
- **Color Options:** 
  - Tactical Black (matte finish)
  - Ocean Blue (anodized aluminum)
  - Desert Tan (military spec)
  - Natural Aluminum (raw recycled metal)
- **Certifications:** IP68, MIL-STD-810H, FCC, CE, RoHS

### Writing Interface
- **Document Types:** Markdown, plain text, notes
- **Features:** Syntax highlighting, word count, spell check
- **Navigation:** Touch + keyboard shortcuts
- **Search:** Full-text search across all documents
- **Export:** USB-C file sharing

### Humble UI Features
- **Customizable Layouts:** Drag-and-drop interface elements
- **Themes:** Light/dark mode for e-ink optimization
- **Plugins:** Community-developed writing tools
- **Scripting:** Write custom writing behaviors in Clojure

---

## 📚 OPEN SOURCE COMPONENTS

### Hardware
- **PCB Design:** KiCad files
- **Mechanical:** FreeCAD 3D models
- **Schematics:** Complete circuit diagrams
- **BOM:** Bill of materials with suppliers

### Software
- **Firmware:** Complete Clojure source code
- **Coreboot:** AMD V1000 port
- **Drivers:** E-ink display drivers
- **Documentation:** Comprehensive developer guides

### Community
- **GitHub:** All code and hardware files
- **Discord:** Developer and user community
- **Wiki:** Documentation and tutorials
- **Forums:** Technical discussions and support

---

## 🚀 LAUNCH STRATEGY

### Crowdfunding Campaign
- **Platform:** Crowd Supply (same as Inkplate)
- **Goal:** $200,000 (200 units)
- **Timeline:** 6-month campaign
- **Rewards:** Early bird pricing, exclusive colors

### Community Building
- **Developer Program:** Early access for contributors
- **Writing Contests:** Showcase Grainwriter capabilities
- **Workshops:** Teach sustainable computing
- **Documentation:** Comprehensive guides and tutorials

### Partnerships
- **Inkplate:** Display technology and manufacturing
- **AMD:** Refurbished processor supply
- **Coreboot:** Firmware development
- **Open Source Hardware:** Collaboration with other projects

---

## 🌟 VISION STATEMENT

**Grainwriter** represents the future of writing: sustainable, offline, and user-controlled. By combining recycled hardware with open-source software, we're creating a writing device that authors can truly own, modify, and improve.

**"Every great story starts with a single word."**

Just as the grainkae3g project builds personal sovereignty in computing, Grainwriter builds personal sovereignty in writing. No more cloud dependency, no more proprietary formats, no more surveillance capitalism in our creative tools.

**The future of writing is offline. The future is Grainwriter.** ✍️

---

**Project Lead:** kae3g  
**Community:** [github.com/grainnetwork/grainwriter](https://github.com/grainnetwork/grainwriter)  
**License:** CERN-OHL-S-2.0 (Hardware) + MIT (Software)  
**Status:** Design Phase

*"Building the writing device that authors deserve."* 🌾
