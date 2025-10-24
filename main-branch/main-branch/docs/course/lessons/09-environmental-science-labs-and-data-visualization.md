# Lesson 9: Environmental Science Labs and Data Visualization

**Session**: 808  
**Graintime**: `12025-10-22--2039--PDT--moon-uttara-ashadha--11thhouse20--kae3g`  
**Duration**: 120 minutes  
**Prerequisites**: Lessons 1-8

> *"From granules to grains to THE WHOLE GRAIN"*  
> Environmental data is immutable - every measurement is a moment in Earth's story.

---

## 🌊 Introduction

Welcome to Session 9! Today we're exploring how Grain Network principles apply to environmental science and engineering labs. Inspired by the **Ecohydraulics and Ecomorphodynamics Laboratory (EEL) at UIUC**, we'll design software for field data collection, analysis, and visualization with immutable data trails.

### Why Environmental Labs Need Grain Network

**The Challenge**: Environmental field studies generate massive amounts of data (water quality, flow rates, temperature, biological samples) that must be:
- **Immutably recorded** - measurements can't be changed retroactively
- **Precisely timestamped** - when and where matters
- **Globally shareable** - labs worldwide benefit from shared data
- **Visually accessible** - stakeholders need intuitive visualizations

**The Solution**: Grain Network's immutable grainpath system, neovedic timestamps, and decentralized storage.

---

## 🎯 Learning Objectives

By the end of this lesson, you will:

1. **Understand** how environmental data requires immutability and precision
2. **Design** data collection systems for field studies
3. **Implement** visualization tools using Clojure Humble UI
4. **Apply** grainpath versioning to scientific datasets
5. **Create** decentralized collaboration networks for environmental research

---

## 📊 Part 1: Environmental Data Collection (30 minutes)

### The EEL Lab Example

The **Ecohydraulics and Ecomorphodynamics Laboratory** studies:
- **Stream ecology**: How water flow affects ecosystems
- **Dye tracer studies**: Tracking water movement (bright green dye in streams!)
- **Geomorphology**: How rivers shape landscapes
- **Habitat restoration**: Improving conditions for aquatic life

**Field Data Types**:
```clojure
{:field-measurement
 {:graintime "12025-10-22--1430--CDT--moon-uttara-ashadha--06thhouse14--eel-lab"
  :location
  {:name "Embarras River, IL"
   :latitude 39.7817
   :longitude -88.2034
   :elevation-m 220.5}
  
  :water-quality
  {:temperature-c 18.5
   :ph 7.2
   :dissolved-oxygen-mg-l 8.4
   :turbidity-ntu 12.3
   :conductivity-us-cm 425}
  
  :flow-data
  {:velocity-m-s 0.85
   :depth-m 1.2
   :discharge-m3-s 4.5
   :cross-section-area-m2 5.3}
  
  :dye-tracer
  {:injection-time "14:00:00"
   :detection-time "14:15:00"
   :concentration-ppb 45.2
   :tracer-type "Rhodamine WT"}
  
  :biological
  {:species-count 12
   :dominant-species "Catostomus commersonii"  ; White sucker
   :habitat-quality-score 7.8}
  
  :metadata
  {:collector "kae3g"
   :equipment ["YSI ProDSS" "FlowTracker2" "Turner Cyclops-7F"]
   :weather "Partly cloudy, 22°C, wind 8 km/h SE"
   :grainpath "/field-data/embarras-river/2025-10-22/study-01/12025-10-22--1430--CDT--moon-uttara-ashadha--06thhouse14--eel-lab/"}}}
```

**Why This Design?**:
- **Graintime timestamp**: Exact moment of measurement with astronomical context
- **Grainpath**: Immutable path for permanent record
- **Structured data**: Easy to query, analyze, visualize
- **Equipment metadata**: Reproducibility and calibration tracking

### Activity: Design Your Field Data Structure (10 minutes)

**Task**: Create an EDN structure for one of these scenarios:

1. **Air Quality Monitoring** (urban pollution study)
2. **Soil Health Assessment** (agricultural research)
3. **Ocean Temperature Profiling** (climate change monitoring)
4. **Forest Canopy Analysis** (biodiversity study)

**Requirements**:
- Graintime timestamp
- Location coordinates
- At least 5 measurements
- Equipment metadata
- Grainpath for immutability

---

## 🗺️ Part 2: Data Visualization Systems (30 minutes)

### Graingource: Git Visualization for Environmental Data

**Inspired by Gource** (Git repository visualizer), **Graingource** visualizes environmental data streams in real-time:

**Features**:
- **Spatial mapping**: Plot measurement locations on maps
- **Temporal animation**: Watch data evolve over time
- **Multi-variable display**: Temperature, pH, flow rate simultaneously
- **Immutable playback**: Replay any study from grainpath archives

**Architecture**:
```
Field Sensors → Grain Network → Graingource Visualizer
     ↓              ↓                    ↓
 Data points   Immutable      Interactive maps
 (real-time)   grainpaths     (web + desktop)
```

### Implementation: Clojure Humble UI

**Graingource Core**:
```clojure
(ns graingource.core
  "Environmental data visualization inspired by Gource"
  (:require [io.github.humbleui.core :as ui]
            [io.github.humbleui.paint :as paint]
            [io.github.humbleui.canvas :as canvas]))

(defn render-measurement-point
  "Render a single data point on the map"
  [measurement]
  (let [lat (:latitude (:location measurement))
        lon (:longitude (:location measurement))
        value (:temperature-c (:water-quality measurement))
        ;; Convert lat/lon to screen coordinates
        x (lon-to-x lon)
        y (lat-to-y lat)
        ;; Color based on temperature (blue=cold, red=hot)
        color (temperature-to-color value)]
    (canvas/draw-circle canvas x y 5 (paint/fill color))))

(defn render-stream-flow
  "Visualize water flow direction and velocity"
  [flow-data]
  (let [velocity (:velocity-m-s flow-data)
        direction (:bearing flow-data)
        ;; Arrow length proportional to velocity
        arrow-length (* velocity 10)
        ;; Arrow points in flow direction
        end-x (+ x (* arrow-length (Math/cos direction)))
        end-y (+ y (* arrow-length (Math/sin direction)))]
    (canvas/draw-line canvas x y end-x end-y
                     (paint/stroke (paint/color 0x0000FF) 2))))

(defn render-dye-tracer
  "Animate dye tracer dispersion over time"
  [tracer-data time-offset]
  (let [injection-time (:injection-time tracer-data)
        current-time (+ injection-time time-offset)
        ;; Dye spreads as Gaussian plume
        concentration (gaussian-dispersion
                       (:x tracer-data)
                       current-time
                       (:velocity tracer-data)
                       (:dispersion-coefficient tracer-data))
        ;; Bright green for visibility!
        opacity (min 1.0 (/ concentration 100))
        color (paint/color 0x00FF00 opacity)]
    (canvas/draw-circle canvas x y 20 (paint/fill color))))

(defn graingource-app
  "Main application window"
  []
  (ui/default-theme
    (ui/column
      (ui/label "Graingource - Environmental Data Visualization")
      (ui/row
        (ui/button "Play" #(start-animation))
        (ui/button "Pause" #(pause-animation))
        (ui/slider 0 100 50 #(set-playback-speed %)))
      (ui/canvas
        {:on-paint render-all-measurements}))))
```

**Web Deployment**: Via **ClojureScript + SVG** for browser compatibility  
**Desktop**: Via **Humble UI JVM** for high performance  
**Mobile**: Via **Rust compiled components + WebAssembly** for efficiency

### Activity: Design a Visualization (10 minutes)

**Task**: Sketch (on paper or digitally) a visualization for one of:

1. **Water Temperature Heatmap** - River temperature across 24 hours
2. **Dye Tracer Animation** - How dye spreads downstream
3. **Species Distribution Map** - Where different fish species live
4. **Flow Velocity Vectors** - Water movement arrows

**Requirements**:
- X and Y axes labeled
- Color legend
- Time slider (for animations)
- Data source (grainpath)

---

## 🔬 Part 3: Immutable Data Trails (20 minutes)

### Why Immutability Matters in Science

**The Problem**: Traditional scientific data management
```
1. Collect data → Excel spreadsheet
2. "Oops, typo!" → Edit cell
3. "Which version is correct?" → Lost history
4. Peer review → "Can't reproduce your results"
```

**The Grain Solution**: Immutable grainpaths
```
1. Collect data → Grainpath with graintime timestamp
2. "Oops, typo!" → New grainpath with correction + reference to original
3. "Which version is correct?" → Complete audit trail
4. Peer review → Exact reproduction via grainpath
```

**Example: Data Correction Workflow**:
```clojure
;; Original measurement (with error)
{:grainpath "/field-data/embarras/2025-10-22/v1/12025-10-22--1430--CDT/"
 :temperature-c 185.5  ; ERROR! Should be 18.5
 :status :superceded
 :superceded-by "/field-data/embarras/2025-10-22/v2/12025-10-22--1445--CDT/"}

;; Corrected measurement (new grainpath)
{:grainpath "/field-data/embarras/2025-10-22/v2/12025-10-22--1445--CDT/"
 :temperature-c 18.5   ; CORRECTED
 :correction-note "Original temperature was decimal point error"
 :supercedes "/field-data/embarras/2025-10-22/v1/12025-10-22--1430--CDT/"
 :status :active}
```

**Benefits**:
- ✅ **Complete history**: Every version preserved
- ✅ **Transparent corrections**: Clear audit trail
- ✅ **Reproducible science**: Exact data for peer review
- ✅ **Legal compliance**: Environmental regulations require data integrity

### Case Study: UIUC EEL Lab Bright Green Stream

**The Study**: Students inject bright green dye into a stream to study water flow patterns.

**Data Pipeline**:
```
1. Injection
   Grainpath: /eel-lab/dye-tracer/injection/12025-04-15--0900--CDT/
   Data: {location, time, dye-mass-kg, injection-rate}

2. Observation Points (every 100m downstream)
   Grainpath: /eel-lab/dye-tracer/observation/site-01/12025-04-15--0915--CDT/
   Data: {concentration-ppb, detection-time, photo-url}

3. Analysis
   Grainpath: /eel-lab/dye-tracer/analysis/v1/12025-04-16--1200--CDT/
   Data: {velocity-m-s, dispersion-coefficient, mixing-depth}

4. Publication
   Grainpath: /eel-lab/publications/2025-Q2-stream-mixing/12025-06-01--1000--CDT/
   References: All observation grainpaths (immutable citations!)
```

**Result**: Entire study reproducible from grainpaths. Future students can:
- View original field photos
- Rerun analysis with updated models
- Compare with other streams globally

---

## 🌐 Part 4: Decentralized Collaboration (20 minutes)

### Grain Network for Global Environmental Research

**The Vision**: Environmental labs worldwide share data via Grain Network:

**Example Network**:
```
UIUC EEL Lab (Illinois) ←→ Grain Network ←→ TU Delft (Netherlands)
        ↓                                           ↓
  Embarras River data                     Rhine River data
        ↓                                           ↓
    Compare stream ecology across continents!
```

**Implementation**: ICP + Hedera + Grainpath

```clojure
(ns grain-environmental.network
  "Decentralized environmental data sharing")

(defn publish-field-data
  "Publish environmental data to Grain Network"
  [measurement]
  (let [graintime (gt/now (:collector measurement))
        grainpath (generate-grainpath "field-data" graintime)
        ;; Store on ICP for permanent, decentralized hosting
        icp-canister-id (icp/store-data measurement)
        ;; Record on Hedera for consensus timestamp
        hedera-tx-id (hedera/submit-hash (hash measurement))
        ;; Create Grainclay beam for network distribution
        beam {:grainpath grainpath
              :icp-canister icp-canister-id
              :hedera-consensus hedera-tx-id
              :public true
              :license "CC-BY-4.0"}]
    (grainclay/publish beam)
    grainpath))

(defn query-global-data
  "Query environmental data across all participating labs"
  [filters]
  (let [;; Search across ICP canisters
        results (icp/query-canisters
                 {:parameter (:parameter filters)
                  :location (:region filters)
                  :time-range (:dates filters)})
        ;; Verify data integrity via Hedera
        verified (map verify-hedera-hash results)]
    verified))
```

**Use Cases**:

1. **Climate Change Monitoring**
   - Global temperature database
   - Every lab contributes local measurements
   - Grainpaths ensure data provenance

2. **Species Migration Tracking**
   - Multiple labs observe same species
   - Immutable timestamps prove observation times
   - Create continental migration maps

3. **Water Quality Networks**
   - River systems span countries
   - Upstream/downstream collaboration
   - Pollution source identification

### Activity: Design a Collaboration Network (15 minutes)

**Task**: Design a Grain Network-based collaboration between 3 environmental labs studying:

1. **Ocean acidification** (labs in Hawaii, Australia, Japan)
2. **Glacial melt** (labs in Alaska, Greenland, Patagonia)
3. **Deforestation** (labs in Amazon, Congo, Borneo)

**Deliverable**:
- Network diagram (nodes = labs, edges = data sharing)
- Data types shared (grainpath examples)
- One specific research question answered by collaboration

---

## 🎓 Part 5: Building Grain Lab Software (15 minutes)

### System Architecture

**Grain Environmental Suite**:
```
┌─────────────────────────────────────────────────┐
│          Grain Environmental Platform           │
├─────────────────────────────────────────────────┤
│  grainfield     - Field data collection app     │
│  grainlab       - Lab analysis interface        │
│  graingource    - Data visualization            │
│  grainpublish   - Share to Grain Network        │
│  grainarchive   - Immutable data storage        │
└─────────────────────────────────────────────────┘
         ↓                    ↓                ↓
    Mobile App          Desktop UI        Web Interface
   (field work)      (lab analysis)    (public access)
```

**Technology Stack**:
- **Frontend**: Clojure Humble UI (desktop), ClojureScript (web), Rust (mobile)
- **Backend**: ICP canisters (data storage), Hedera (consensus)
- **Visualization**: SVG/Canvas (web), WebGL (3D), WebAssembly (performance)
- **Data Format**: EDN (primary), JSON (web API), CSV (export)

### Student Project Ideas

1. **grainfield Mobile App**
   - Offline data collection
   - GPS integration
   - Photo/video capture
   - Sync to Grain Network when online

2. **grainlab Analysis Dashboard**
   - Import from CSV/Excel
   - Statistical analysis
   - Generate publication-ready graphs
   - Export to grainpath

3. **graingource Visualizer**
   - Real-time data animation
   - Map overlays
   - Time slider
   - Export to video

4. **grainpublish Data Sharing**
   - One-click publish to Grain Network
   - Automatic grainpath generation
   - Metadata editor
   - Access control (public/private)

---

## 🌍 Part 6: Social Impact and Ethics (15 minutes)

### Environmental Justice

**The Problem**: Environmental data is often:
- Locked behind paywalls
- Owned by governments/corporations
- Inaccessible to affected communities
- Delayed or censored

**The Grain Solution**:
- **Open Access**: All data public by default
- **Community Ownership**: Local communities host their own data
- **Real-Time Transparency**: Immediate publication
- **Immutable Records**: Can't be censored or altered

**Example: Community Water Monitoring**
```
Flint, Michigan (2014-2015)
Problem: Government claimed water was safe, but residents knew it wasn't
Grain Solution: Community-owned sensors → Real-time grainpaths → Immutable proof
Result: No one can deny the evidence
```

### Case Studies

**1. Indigenous Environmental Monitoring**
- **Challenge**: Traditional knowledge + scientific data
- **Solution**: Grainpath metadata includes both
- **Impact**: Indigenous communities control their data

**2. Citizen Science Networks**
- **Challenge**: Non-experts collecting data
- **Solution**: Grainpath validates equipment + protocols
- **Impact**: Democratized environmental research

**3. Climate Refugee Documentation**
- **Challenge**: Proving displacement due to environment
- **Solution**: Immutable grainpaths show environmental changes over time
- **Impact**: Legal evidence for asylum claims

### Discussion Questions (10 minutes)

1. **Who should own environmental data?** Government? Universities? Communities? Everyone?

2. **Should data ever be kept private?** What about endangered species locations?

3. **How do we handle data quality?** Not all sensors are equally accurate.

4. **What about developing countries?** How do we ensure equitable access to Grain Network?

5. **Can immutable data be dangerous?** What if it reveals sensitive information?

---

## 🧪 Hands-On Project: Build a Grainfield Prototype (30 minutes)

### Project: Simple Water Quality Monitor

**Goal**: Create a basic field data collection tool that generates grainpaths.

**Requirements**:
- Collects 3+ measurements (temperature, pH, conductivity)
- Records GPS coordinates
- Generates graintime timestamp
- Saves to EDN file with grainpath
- Displays data in simple visualization

**Starter Code**:
```clojure
#!/usr/bin/env bb

(require '[babashka.process :refer [shell]]
         '[clojure.string :as str]
         '[clojure.java.io :as io])

(defn get-gps-coordinates []
  "Get current GPS coordinates (placeholder - use real GPS device)"
  {:latitude 39.7817
   :longitude -88.2034
   :elevation-m 220.5})

(defn collect-measurement []
  "Collect environmental measurements"
  (println "=== Grainfield Water Quality Monitor ===")
  (println "")
  (print "Water temperature (°C): ")
  (flush)
  (let [temp (read-line)
        _ (print "pH: ")
        _ (flush)
        ph (read-line)
        _ (print "Conductivity (μS/cm): ")
        _ (flush)
        conductivity (read-line)
        ;; Generate graintime
        author "grainfield"
        graintime (-> (shell {:out :string} "gt" "now" author)
                     :out
                     str/trim)
        ;; Get location
        location (get-gps-coordinates)
        ;; Create grainpath
        grainpath (str "/field-data/water-quality/" graintime "/")]
    {:field-measurement
     {:graintime graintime
      :grainpath grainpath
      :location location
      :water-quality
      {:temperature-c (Double/parseDouble temp)
       :ph (Double/parseDouble ph)
       :conductivity-us-cm (Double/parseDouble conductivity)}
      :metadata
      {:collector author
       :equipment ["Grainfield v1.0"]
       :created graintime}}}))

(defn save-measurement [measurement]
  "Save measurement to EDN file"
  (let [filename (str "measurement-" (:graintime (:field-measurement measurement)) ".edn")]
    (spit filename (pr-str measurement))
    (println "")
    (println "✅ Measurement saved to:" filename)
    (println "📍 Grainpath:" (:grainpath (:field-measurement measurement)))))

(defn main []
  (let [measurement (collect-measurement)]
    (save-measurement measurement)))

(main)
```

**Run It**:
```bash
chmod +x grainfield.bb
./grainfield.bb
```

**Extension Challenges**:
1. Add more measurement types (dissolved oxygen, turbidity)
2. Validate input ranges (pH must be 0-14)
3. Generate simple ASCII visualization
4. Upload to ICP canister
5. Create map visualization of multiple measurements

---

## 📝 Assessment

### Quiz (10 minutes)

**1. Multiple Choice**: Why is immutability important for environmental data?
- A) It makes data smaller
- B) It prevents retroactive changes and ensures integrity
- C) It makes analysis faster
- D) It's required by law

**Answer**: B (though D is also sometimes true!)

**2. Short Answer**: Explain how grainpaths help scientific reproducibility.

**Sample Answer**: Grainpaths provide exact, immutable references to data and analysis. A researcher can cite a grainpath in a paper, and anyone can retrieve the exact same data years later, enabling perfect reproduction of results.

**3. Design Challenge**: Create a grainpath for an air quality measurement in San Francisco on December 1, 2025 at 3:00 PM PST.

**Sample Answer**:
```
/field-data/air-quality/san-francisco/12025-12-01--1500--PST--moon-{nakshatra}--{house}thhouse{degree}--{collector}/
```

### Final Project Options

Choose ONE:

**1. Grainfield Mobile App Design**
- Design document (2-3 pages)
- UI mockups (5+ screens)
- Data structure (EDN format)
- Grainpath strategy

**2. Graingource Visualization**
- Working prototype (ClojureScript or Clojure)
- Visualizes at least 3 data types
- Includes time animation
- Grainpath integration

**3. Research Proposal: Grain Network for Environmental Justice**
- Identify a real environmental problem
- Design Grain Network-based solution
- Address data ownership, access, and ethics
- Include technical architecture

**4. Build a grainfield Extension**
- Extend the starter code
- Add 5+ new features
- Include visualization
- Write documentation

---

## 🔗 Resources

### Tools & Libraries

- **Clojure Humble UI**: https://github.com/HumbleUI/HumbleUI
- **Swiss Ephemeris** (for graintime): https://www.astro.com/swisseph/
- **GeoClojure**: https://github.com/Factual/geo
- **Incanter** (statistical graphics): https://github.com/incanter/incanter

### Environmental Organizations

- **UIUC EEL Lab**: https://eel.ven.illinois.edu/
- **EPA Data**: https://www.epa.gov/data
- **USGS Water Data**: https://waterdata.usgs.gov/
- **Citizen Science Association**: https://www.citizenscience.org/

### Academic Reading

1. **"Immutable Data Structures for Reproducible Science"** (fictional but should exist!)
2. **EPA Quality Assurance Guidelines**: Real standards for environmental data
3. **"Open Data for Environmental Justice"** - EJ case studies

---

## 🌾 Next Lesson Preview

**Lesson 10**: Zero-Knowledge Proofs and Privacy

Topics:
- When should environmental data be private?
- Zero-knowledge proofs for endangered species locations
- Privacy-preserving pollution monitoring
- Balancing transparency with protection

---

## 🌟 Closing Reflection

Today we learned how Grain Network principles (immutability, precision timestamps, decentralized collaboration) apply to environmental science. The same technology that powers Grainmusic and Graincourse can:

- **Preserve** irreplaceable environmental data
- **Empower** communities to monitor their own environments
- **Enable** global scientific collaboration
- **Ensure** transparency and accountability

**Remember**: Every grainpath is permanent. Every measurement matters. Every student can contribute to understanding and protecting our planet.

**The bright green dye in that stream?** It's teaching us how water moves, how ecosystems connect, how we can design better river restoration. And with Grain Network, that knowledge becomes permanent, shareable, and accessible to everyone.

---

**🌾 From granules to grains to THE WHOLE GRAIN - including Earth itself! 🌍**

---

**Next Steps**:
1. Complete quiz and final project
2. Test the grainfield prototype
3. Read about UIUC EEL Lab's work
4. Think about environmental issues in your community
5. How could Grain Network help?

**See you in Lesson 10!** 🎉

