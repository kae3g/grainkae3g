# Graincard & Grainframe Naming Scheme

**Urbit Arvo Hoon Zuse Convention-Inspired**

Following Urbit's `sur/` (structures), `lib/` (libraries), and `mar/` (marks) folder organization, we define a comprehensive naming system for Grain Network mark types.

---

## 📐 Base Dimensions: The 80×110 Foundation

**Grainframe** = Base unit (80 characters × 110 lines)
**Graincard** = Synonym for grainframe (interchangeable in GrainOS typo-aware system)

Both terms refer to the same concept, following our "dual-name" philosophy from `clojure-sixos`/`clojure-s6`.

---

## 🗂️ Urbit-Inspired Directory Structure

```
grain-metatypes/
├── sur/                    # Structures (type definitions)
│   ├── grainframe.hoon     # Hoon type definition
│   ├── graincard.hoon      # Alias/synonym type
│   └── graindisplay.hoon   # Display metadata types
│
├── lib/                    # Libraries (helper functions)
│   ├── grainframe.hoon     # Grainframe manipulation library
│   ├── ascii-art.hoon      # ASCII art conversion
│   └── resolution.hoon     # Resolution scaling
│
└── mar/                    # Marks (serialization formats)
    ├── grainframe/
    │   ├── base.hoon       # grainframe-base (80×110)
    │   ├── card.hoon       # graincard-base (80×110)
    │   ├── 10x.hoon        # grainframe-10x (800×1100)
    │   └── hd.hoon         # grainframe-hd (1600×2200)
    │
    ├── graincard/
    │   ├── sketch.hoon     # graincard-sketch (photo → ASCII)
    │   ├── clay.hoon       # graincard-clay (red clay sketches)
    │   └── graphite.hoon   # graincard-graphite (pencil sketches)
    │
    └── grain/
        ├── txt.hoon        # grain-txt (plain text)
        ├── img.hoon        # grain-img (image)
        └── vid.hoon        # grain-vid (video)
```

---

## 📏 Resolution Naming Convention

### **Pattern**: `grainframe-{multiplier}` or `graincard-{multiplier}`

Base: 80×110 characters

| Name | Dimensions | Multiplier | Use Case |
|------|------------|------------|----------|
| `grainframe-base` | 80×110 | 1× | Standard terminal/e-ink |
| `grainframe-2x` | 160×220 | 2× | Small HD displays |
| `grainframe-5x` | 400×550 | 5× | Medium displays |
| `grainframe-10x` | 800×1100 | 10× | Large HD displays |
| `grainframe-20x` | 1600×2200 | 20× | 4K displays |
| `grainframe-hd` | 1920×2640 | 24× | Full HD (optimized) |
| `grainframe-4k` | 3840×5280 | 48× | 4K UHD |

### **Alternative Naming** (Descriptive):

| Name | Dimensions | Use Case |
|------|------------|----------|
| `grainframe-terminal` | 80×110 | Text terminals |
| `grainframe-mobile` | 400×550 | Phone screens |
| `grainframe-tablet` | 800×1100 | Tablet screens |
| `grainframe-desktop` | 1600×2200 | Desktop monitors |
| `grainframe-cinema` | 3840×5280 | Cinematic display |

---

## 🎨 Content Type Marks (Urbit `mar/` Convention)

### **Format**: `graincard-{content-type}`

Following Urbit's mark system where marks define data serialization formats:

```
mar/
├── graincard-sketch-clay.hoon      # Red clay on warm paper
├── graincard-sketch-graphite.hoon  # Graphite on white paper
├── graincard-sketch-charcoal.hoon  # Charcoal on paper
├── graincard-ascii-art.hoon        # ASCII art (generated)
├── graincard-text.hoon             # Plain text content
├── graincard-markdown.hoon         # Markdown content
└── graincard-code.hoon             # Source code
```

### **Sketch-Specific Marks**:

```clojure
;; Red clay on warm paper (terracotta/sepia aesthetic)
{:mark "graincard-sketch-clay"
 :source-medium :red-clay
 :paper-type :warm-paper
 :paper-color "#FFF8E7"  ;; Warm white
 :clay-color "#B7410E"   ;; Terra cotta red
 :resolution :photo      ;; Input: photo
 :output-resolution :grainframe-10x  ;; 800×1100 ASCII
 :conversion :photo-to-ascii}

;; Graphite on white paper (classic pencil sketch)
{:mark "graincard-sketch-graphite"
 :source-medium :graphite
 :paper-type :white-paper
 :paper-color "#FFFFFF"
 :graphite-grades ["9H" "6B"]  ;; Light to dark
 :resolution :photo
 :output-resolution :grainframe-10x
 :conversion :photo-to-ascii}
```

---

## 🔄 Dual-Name System (GrainOS Typo-Aware)

Following the pattern from `clojure-sixos`/`clojure-s6` and `clomoko`/`clotoko`:

### **Canonical ↔ Alias Pairs**:

| Canonical | Alias | Notes |
|-----------|-------|-------|
| `grainframe` | `graincard` | Interchangeable |
| `grainframe-base` | `graincard-base` | 80×110 |
| `grainframe-10x` | `graincard-10x` | 800×1100 |
| `mark-grainframe` | `mark-graincard` | Urbit mar/ style |

**Implementation**: GrainOS recognizes both names and treats them identically.

```clojure
;; In clojure-sixos typo handler
(def grain-synonyms
  {"grainframe" "graincard"
   "graincard" "grainframe"
   "mark-grainframe" "mark-graincard"
   "mar-grainframe" "mar-graincard"})
```

---

## 📦 Full Mark Hierarchy (Urbit-Style)

### **Structure Types** (`sur/`):

```hoon
:: sur/grainframe.hoon
+$  grainframe
  $:  width=@ud        :: 80 (base)
      height=@ud       :: 110 (base)
      content=@t       :: Text content (8,800 chars)
      metadata=grain-metadata
  ==

+$  grain-metadata
  $:  author=@p
      timestamp=@da
      grainmark=@t
      display-settings=display-metadata
  ==
```

### **Library Functions** (`lib/`):

```hoon
:: lib/grainframe.hoon
++  scale-grainframe
  |=  [frame=grainframe multiplier=@ud]
  ^-  grainframe
  frame(width (mul width.frame multiplier), height (mul height.frame multiplier))

++  photo-to-ascii
  |=  [photo=@ux resolution=grainframe-resolution]
  ^-  grainframe
  :: Convert photo pixels to ASCII characters
```

### **Mark Definitions** (`mar/`):

```hoon
:: mar/grainframe/base.hoon
::  grainframe-base: 80×110 text frame
|_  frame=grainframe
++  grad  %noun
++  grow
  |%
  ++  noun  frame
  ++  json  (frame-to-json frame)
  ++  txt   content.frame
  --
++  grab
  |%
  ++  noun  grainframe
  ++  json  (json-to-frame +<)
  ++  txt   (txt-to-frame +<)
  --
--
```

---

## 🎨 Sketch-to-ASCII Conversion Marks

### **Mark**: `graincard-sketch-clay`

**Purpose**: Convert red clay on warm paper sketches to ASCII art

```clojure
{:mark-definition
 {:name "graincard-sketch-clay"
  :sur-type "graincard"
  :input-format :jpeg  ;; Photo of sketch
  :output-format :grainframe-10x  ;; 800×1100 ASCII
  
  :conversion-pipeline
  [{:step :color-extraction
    :detect-color "#B7410E"  ;; Red clay color
    :tolerance 0.15}
   
   {:step :background-removal
    :background-color "#FFF8E7"  ;; Warm paper
    :threshold 0.9}
   
   {:step :edge-detection
    :algorithm :canny
    :low-threshold 50
    :high-threshold 150}
   
   {:step :ascii-mapping
    :characters " .'`^\",:;Il!i><~+_-?][}{1)(|/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$"
    :reverse false  ;; Light background
    :aspect-ratio-correction 2.0}  ;; Terminal chars are 2:1
   
   {:step :resolution-scale
    :target-width 800
    :target-height 1100
    :preserve-aspect true}]
  
  :metadata
  {:source-medium "Red clay on warm paper"
   :aesthetic "Terracotta/sepia warmth"
   :recommended-display
   {:color-temperature 2000
    :color-profile :display-p3
    :filters [:sepia]}}}}
```

### **Mark**: `graincard-sketch-graphite`

**Purpose**: Convert graphite pencil sketches to ASCII art

```clojure
{:mark-definition
 {:name "graincard-sketch-graphite"
  :sur-type "graincard"
  :input-format :jpeg
  :output-format :grainframe-10x
  
  :conversion-pipeline
  [{:step :grayscale-conversion}
   
   {:step :contrast-enhancement
    :factor 1.3}
   
   {:step :edge-detection
    :algorithm :sobel}
   
   {:step :ascii-mapping
    :characters " .:-=+*#%@"
    :reverse false
    :aspect-ratio-correction 2.0}
   
   {:step :resolution-scale
    :target-width 800
    :target-height 1100}]
  
  :metadata
  {:source-medium "Graphite pencil on white paper"
   :aesthetic "Classic pencil sketch"
   :recommended-display
   {:color-temperature 6500  ;; Bright for graphite
    :color-profile :srgb
    :filters []}}}}
```

---

## 🗃️ Complete Naming Registry

### **Base Marks** (80×110):
- `grainframe-base` / `graincard-base`
- `mark-grainframe` / `mark-graincard` (Urbit `mar/` style)

### **Scaled Marks** (multipliers):
- `grainframe-2x` / `graincard-2x` (160×220)
- `grainframe-5x` / `graincard-5x` (400×550)
- `grainframe-10x` / `graincard-10x` (800×1100) ← **Sketch conversion target**
- `grainframe-20x` / `graincard-20x` (1600×2200)

### **Named Resolutions**:
- `grainframe-terminal` = `grainframe-base`
- `grainframe-mobile` = `grainframe-5x`
- `grainframe-tablet` = `grainframe-10x`
- `grainframe-desktop` = `grainframe-20x`
- `grainframe-hd` = custom (1920×2640)
- `grainframe-4k` = `grainframe-48x`

### **Content Type Marks**:
- `graincard-sketch-clay` - Red clay on warm paper
- `graincard-sketch-graphite` - Pencil on white paper
- `graincard-sketch-charcoal` - Charcoal sketches
- `graincard-ascii-art` - Pure ASCII art
- `graincard-text` - Plain text
- `graincard-markdown` - Markdown formatted
- `graincard-code-clojure` - Clojure source code
- `graincard-code-hoon` - Hoon source code

---

## 🎯 Grainweb Widget Implementation

### **Widget**: Sketch-to-ASCII Converter

**Input**: Photo of paper sketch (JPEG/PNG)  
**Output**: `graincard-10x` ASCII art (800×1100)

**Conversion Process**:

```clojure
(ns grainweb.widgets.sketch-to-ascii
  (:require [grainframe.marks.sketch-clay :as clay]
            [grainframe.marks.sketch-graphite :as graphite]
            [grainframe.lib.ascii-art :as ascii]
            [grainframe.lib.resolution :as res]))

(defn convert-sketch-to-graincard
  "Convert uploaded sketch photo to graincard ASCII art"
  [photo-bytes sketch-type]
  (let [mark-converter (case sketch-type
                        :clay clay/convert
                        :graphite graphite/convert
                        :charcoal charcoal/convert)
        
        ;; Step 1: Detect and extract sketch
        sketch-data (mark-converter photo-bytes)
        
        ;; Step 2: Generate ASCII at target resolution
        ascii-art (ascii/photo-to-ascii sketch-data
                                       {:resolution :grainframe-10x
                                        :width 800
                                        :height 1100})
        
        ;; Step 3: Create graincard with metadata
        graincard {:mark "graincard-10x"
                  :alias "grainframe-10x"
                  :width 800
                  :height 1100
                  :content ascii-art
                  :source-type sketch-type
                  :metadata {:conversion-date (now)
                            :original-resolution (detect-resolution photo-bytes)}}]
    
    graincard))
```

**Widget UI** (Humble UI):

```clojure
(defn sketch-upload-widget []
  (ui/column
    (ui/label "📷 Upload Sketch Photo")
    
    (ui/file-picker
      {:accept ["image/jpeg" "image/png"]
       :on-select (fn [file]
                   (convert-and-display file))})
    
    (ui/radio-group
      {:label "Sketch Type"
       :options [{:value :clay :label "🟠 Red Clay on Warm Paper"}
                 {:value :graphite :label "✏️ Graphite on White Paper"}
                 {:value :charcoal :label "🖤 Charcoal"}]
       :on-change (fn [type] (swap! *sketch-type identity type))})
    
    (ui/slider
      {:label "ASCII Detail Level"
       :min 1 :max 10 :value 5
       :on-change (fn [level] (swap! *detail-level identity level))})
    
    (ui/button
      {:label "Convert to Graincard ASCII"
       :on-click (fn []
                  (let [result (convert-sketch-to-graincard @*photo @*sketch-type)]
                    (swap! *result identity result)))})))
```

---

## 🔗 Grainclay Integration

Sketch conversions use neovedic-timestamped immutable paths:

```clojure
{:graincard
 {:mark "graincard-sketch-clay"
  :grainmark-name "kae3g/forest-sketch-001"
  :immutable-path "12025-10-22--1650--PST--moon-vishakha--09thhouse16--kae3g"
  :grainclay-url "/12025-10-22--1650--PST--moon-vishakha--09thhouse16--kae3g/forest-sketch-001.graincard"
  
  :source
  {:photo-url "/photos/original/sketch-photo.jpg"
   :sketch-type :clay
   :paper-type :warm-paper
   :medium :red-clay}
  
  :output
  {:resolution :grainframe-10x
   :width 800
   :height 1100
   :format :ascii
   :characters " .'`^\",:;Il!i><~+_-?][}{1)(|/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$"}
  
  :display-metadata
  {:color-temperature 2000
   :filters [:sepia]
   :recommended-viewing :graindroid-ink}}}
```

---

## 📋 Mark Type Registry

### **Core Marks** (`sur/grainframe.hoon`):

```hoon
+$  grainframe-resolution
  $?  %base      :: 80×110
      %2x        :: 160×220
      %5x        :: 400×550
      %10x       :: 800×1100
      %20x       :: 1600×2200
      %hd        :: 1920×2640
      %4k        :: 3840×5280
  ==

+$  sketch-type
  $?  %clay      :: Red clay
      %graphite  :: Pencil
      %charcoal  :: Charcoal
      %ink       :: Ink
  ==

+$  graincard
  $:  mark=@t              :: Mark name
      resolution=grainframe-resolution
      content=@t           :: ASCII art
      metadata=sketch-metadata
  ==
```

---

## 🌐 Grainweb Integration

Upload workflow:

1. **Upload**: User uploads photo via Grainweb widget
2. **Detect**: Automatic sketch type detection (clay vs. graphite)
3. **Convert**: Photo → ASCII using appropriate mark converter
4. **Publish**: Create graincard with Grainclay immutable path
5. **Display**: Grainweb renders with recommended display settings

**Example Grainweb Post**:

```
Title: Forest Trail Sketch
Mark: graincard-sketch-clay
Resolution: grainframe-10x (800×1100)
Immutable Path: /12025-10-22--1650--PST--moon-vishakha--09thhouse16--kae3g/forest-trail.graincard

[800×1100 ASCII art rendering]

Display: 2000K warm lighting, sepia filter
View on: Desktop or Graindroid Inkmode
```

---

## 🎓 Educational Integration

This system teaches students:

1. **Urbit Conventions**: `sur/`, `lib/`, `mar/` organization
2. **Type Systems**: Structured data with validation
3. **Mark System**: Content type serialization
4. **Image Processing**: Photo → ASCII conversion
5. **Resolution Scaling**: Mathematical multipliers
6. **Metadata**: Embedded display preferences
7. **Immutable Paths**: Grainclay URL-safe timestamps

---

## 🔧 Implementation Files

```
grainstore/grain-metatypes/
├── sur/
│   ├── grainframe.hoon          # Base type definitions
│   └── graincard.hoon           # Alias type
│
├── lib/
│   ├── grainframe.hoon          # Core library
│   ├── ascii-art.hoon           # ASCII conversion
│   ├── sketch-detection.hoon    # Detect sketch type
│   └── resolution-scale.hoon    # Resolution math
│
├── mar/
│   ├── grainframe/
│   │   ├── base.hoon
│   │   ├── 10x.hoon
│   │   └── hd.hoon
│   │
│   └── graincard/
│       ├── sketch-clay.hoon
│       ├── sketch-graphite.hoon
│       └── ascii-art.hoon
│
└── src/grainframe/
    ├── core.clj                 # Clojure implementation
    ├── marks.clj                # Mark conversion
    ├── ascii.clj                # ASCII art generation
    └── sketch.clj               # Sketch processing
```

---

## 🌾 Summary

**Naming Philosophy**:
- **Grainframe** = Canonical name
- **Graincard** = Friendly alias/synonym
- **Both recognized** by GrainOS typo-aware system
- **Multipliers** for scaling (2x, 5x, 10x, 20x)
- **Descriptive names** for common use cases (mobile, tablet, desktop)
- **Content marks** for specific types (sketch-clay, sketch-graphite)
- **Urbit conventions** for organization (sur/, lib/, mar/)

**Key Innovation**: Photo of paper sketch → Beautiful ASCII art at multiple resolutions, preserving the warmth and aesthetic of physical sketching in digital form.

---

*"From red clay and graphite to ASCII art - preserving the warmth of analog sketching in the digital Grain Network."*

🌾 **Grain Network** - Where physical and digital art harmonize

