# grainneovedic (neovedic-timestamp)

**Vedic-aligned timestamp system for the Grain Network**

> **"Time is not just numbers, but cosmic alignment"**

---

## 🌐 **Grain Network Websites**

### **Main Sites**
- 🌾 **Grain Network Hub**: https://kae3g.github.io/grainkae3g/ (coming soon)
- 📝 **kae3g Writings**: https://kae3g.codeberg.page/12025-10/ | https://kae3g.github.io/12025-10/
- 🏢 **grainpbc Organization**: https://github.com/grainpbc

### **Core Libraries**
- 🔧 **clojure-sixos**: https://grainpbc.github.io/clojure-sixos/
- ⚙️ **clojure-s6**: https://grainpbc.github.io/clojure-s6/
- 🌐 **clojure-icp**: https://grainpbc.github.io/clojure-icp/
- 🔄 **clotoko**: https://grainpbc.github.io/clotoko/

### **Documentation**
- 📚 **Full Website List**: [GRAIN-NETWORK-WEBSITES.md](https://kae3g.github.io/grainkae3g/docs/infrastructure/GRAIN-NETWORK-WEBSITES.html)

---

## 🎯 **Purpose**

**grainneovedic** generates spiritually-aligned, astronomically-aware timestamps for:

- **Session markers** (`SESSION-803-12025-10-22--1230--PDT--moon-vishakha--09thhouse12--kae3g.md`)
- **Immutable Grainclay paths** (URL-safe, cosmic-aligned)
- **Append-only commits** (timestamped with astronomical context)
- **Document versioning** (beyond linear time)
- **Spiritual alignment tracking** (nakshatra, house, planetary positions)

### **Why Neovedic?**

Traditional timestamps (`2025-01-22T19:30:00Z`) tell you **when**, but not **where you are cosmically**.

Neovedic timestamps tell you:
- ✅ **When** (date, time, timezone)
- ✅ **Where cosmically** (moon nakshatra, house position)
- ✅ **Personal alignment** (user identifier)
- ✅ **Immutable path** (URL-safe for Grainclay)
- ✅ **Spiritual context** (Vedic astronomy)

**Example:**
```
12025-10-22--1230--PDT--moon-vishakha--09thhouse12--kae3g
```

Reads as:
- **Date**: October 22, 12025 (Holocene calendar)
- **Time**: 12:30 PDT (no location revealed, just timezone)
- **Moon**: In Vishakha nakshatra
- **House**: 9th house (12° into it)
- **User**: kae3g

---

## 📅 **Timestamp Format**

### **Full Format**

```
{holocene-date}--{time}--{timezone}--moon-{nakshatra}--{house}thhouse{degrees}--{user}
```

### **Components**

1. **Holocene Date**: `12025-10-22`
   - Year: 12025 (10,000 + CE year)
   - Month: 10 (October)
   - Day: 22

2. **Time**: `1230`
   - 24-hour format
   - No colons (URL-safe)
   - Local time

3. **Timezone**: `PDT` | `PST` | `EST` | `UTC` etc.
   - Standard timezone abbreviation
   - No location data (privacy-preserving)

4. **Moon Nakshatra**: `moon-vishakha`
   - One of 27 nakshatras
   - Vedic lunar mansion
   - Spiritual alignment indicator

5. **House Position**: `09thhouse12`
   - House number (01-12)
   - Degrees into house (00-30)
   - Astrological position

6. **User**: `kae3g`
   - Personal identifier
   - Grain Network username
   - Session owner

### **Variations**

**Short form** (for commits):
```
12025-10-22--moon-vishakha--kae3g
```

**Medium form** (for documents):
```
12025-10-22--1230--PDT--moon-vishakha--kae3g
```

**Full form** (for immutable paths):
```
12025-10-22--1230--PDT--moon-vishakha--09thhouse12--kae3g
```

---

## 🚀 **Quick Start**

### **Generate Current Timestamp**

```bash
bb neovedic-timestamp

# Output:
# 12025-10-22--1945--PDT--moon-vishakha--09thhouse12--kae3g
```

### **Generate for Specific User**

```bash
bb neovedic-timestamp --user alice

# Output:
# 12025-10-22--1945--PDT--moon-vishakha--09thhouse12--alice
```

### **Generate Short Form**

```bash
bb neovedic-timestamp --short

# Output:
# 12025-10-22--moon-vishakha--kae3g
```

### **Generate for Session File**

```bash
bb neovedic-session 804

# Creates:
# docs/SESSION-804-12025-10-22--1945--PDT--moon-vishakha--09thhouse12--kae3g.md
```

---

## 📁 **Structure**

```
grainneovedic/
├── src/
│   ├── grainneovedic/
│   │   ├── core.clj              # Main timestamp logic
│   │   ├── nakshatras.clj        # 27 nakshatras
│   │   ├── houses.clj            # 12 houses
│   │   ├── astronomy.clj         # Moon position calculation
│   │   ├── holocene.clj          # Holocene calendar conversion
│   │   └── grainclay.clj         # Grainclay path integration
├── scripts/
│   ├── neovedic-timestamp.bb     # Generate timestamp
│   ├── neovedic-session.bb       # Create session file
│   └── neovedic-commit.bb        # Commit with neovedic timestamp
├── data/
│   ├── nakshatras.edn            # Nakshatra data
│   ├── houses.edn                # House meanings
│   └── timezone-offsets.edn      # Timezone data
├── templates/
│   ├── session.md                # Session file template
│   ├── commit-message.txt        # Commit message template
│   └── grainclay-path.txt        # Path template
├── test/
│   └── grainneovedic/
│       ├── core_test.clj
│       └── astronomy_test.clj
├── deps.edn
├── bb.edn
└── README.md                      # This file
```

---

## 🔧 **Installation**

### **deps.edn**

```clojure
{:deps {grainpbc/grainneovedic {:git/url "https://github.com/grainpbc/grainneovedic"
                                 :sha "..."}}}
```

### **As Babashka Script**

```bash
# Clone repo
git clone https://github.com/grainpbc/grainneovedic

# Link to PATH
ln -s $(pwd)/grainneovedic/scripts/neovedic-timestamp.bb ~/bin/neovedic-timestamp

# Use anywhere
neovedic-timestamp
```

---

## 🌙 **The 27 Nakshatras**

### **Sanskrit Names**

1. **Ashwini** (अश्विनी) - Horse-headed
2. **Bharani** (भरणी) - The Bearer
3. **Krittika** (कृत्तिका) - The Cutter
4. **Rohini** (रोहिणी) - The Red One
5. **Mrigashira** (मृगशीर्षा) - Deer's Head
6. **Ardra** (आर्द्रा) - The Moist One
7. **Punarvasu** (पुनर्वसु) - Return of the Light
8. **Pushya** (पुष्य) - The Nourisher
9. **Ashlesha** (आश्लेषा) - The Embracer
10. **Magha** (मघा) - The Mighty
11. **Purva Phalguni** (पूर्व फाल्गुनी) - Former Reddish One
12. **Uttara Phalguni** (उत्तर फाल्गुनी) - Latter Reddish One
13. **Hasta** (हस्त) - The Hand
14. **Chitra** (चित्रा) - The Bright One
15. **Swati** (स्वाति) - The Sword
16. **Vishakha** (विशाखा) - Forked Branch
17. **Anuradha** (अनुराधा) - Following Radha
18. **Jyeshtha** (ज्येष्ठा) - The Eldest
19. **Mula** (मूल) - The Root
20. **Purva Ashadha** (पूर्वाषाढ़ा) - Former Invincible One
21. **Uttara Ashadha** (उत्तराषाढ़ा) - Latter Invincible One
22. **Shravana** (श्रवण) - The Ear
23. **Dhanishta** (धनिष्ठा) - The Wealthy
24. **Shatabhisha** (शतभिषा) - Hundred Physicians
25. **Purva Bhadrapada** (पूर्वभाद्रपदा) - Former Happy Feet
26. **Uttara Bhadrapada** (उत्तरभाद्रपदा) - Latter Happy Feet
27. **Revati** (रेवती) - The Wealthy

### **URL-Safe Names**

```clojure
{:nakshatras
 ["ashwini" "bharani" "krittika" "rohini" "mrigashira" "ardra"
  "punarvasu" "pushya" "ashlesha" "magha" "purva-phalguni"
  "uttara-phalguni" "hasta" "chitra" "swati" "vishakha" "anuradha"
  "jyeshtha" "mula" "purva-ashadha" "uttara-ashadha" "shravana"
  "dhanishta" "shatabhisha" "purva-bhadrapada" "uttara-bhadrapada"
  "revati"]}
```

---

## 🏠 **The 12 Houses**

1. **1st House** - Self, Identity, Ascendant
2. **2nd House** - Wealth, Values, Speech
3. **3rd House** - Siblings, Communication, Courage
4. **4th House** - Home, Mother, Emotions
5. **5th House** - Creativity, Children, Pleasure
6. **6th House** - Health, Service, Enemies
7. **7th House** - Partnership, Marriage, Contracts
8. **8th House** - Transformation, Death, Occult
9. **9th House** - Philosophy, Higher Learning, Dharma
10. **10th House** - Career, Status, Public Life
11. **11th House** - Gains, Friends, Aspirations
12. **12th House** - Loss, Liberation, Subconscious

---

## 🌍 **Privacy-Preserving Location**

**grainneovedic** reveals **timezone**, not **location**:

❌ **Don't reveal:**
- GPS coordinates
- City/state
- Specific location

✅ **Do reveal:**
- Timezone (PDT, EST, UTC)
- General region implied by timezone
- Astronomical data (visible to anyone in that timezone)

**Example:**
```
PDT = Pacific Daylight Time
```

Could be:
- California
- Oregon
- Washington
- Nevada
- British Columbia

This preserves privacy while providing cosmic context.

---

## 🔮 **Use Cases**

### **1. Session Markers**

```bash
bb neovedic-session 804

# Creates:
docs/SESSION-804-12025-10-22--1945--PDT--moon-vishakha--09thhouse12--kae3g.md
```

### **2. Immutable Grainclay Paths**

```clojure
(require '[grainneovedic.core :as nv])

(def timestamp (nv/generate-timestamp {:user "kae3g"}))
;; => "12025-10-22--1945--PDT--moon-vishakha--09thhouse12--kae3g"

(def grainclay-path (str "docs/archive/" timestamp ".md"))
;; => "docs/archive/12025-10-22--1945--PDT--moon-vishakha--09thhouse12--kae3g.md"
```

### **3. Git Commits**

```bash
bb neovedic-commit "feat: implement lexicon sync"

# Commit message:
# feat: implement lexicon sync
#
# Timestamp: 12025-10-22--1945--PDT--moon-vishakha--09thhouse12--kae3g
# Session: 804
# Author: kae3g
```

### **4. Append-Only Rule**

When following the append-only repository rule:

```bash
# Original file
docs/guides/setup.md

# Edit it
vim docs/guides/setup.md

# Save immutable version with neovedic timestamp
bb neovedic-archive docs/guides/setup.md

# Creates:
docs/archive/setup--12025-10-22--1945--PDT--moon-vishakha--09thhouse12--kae3g.md
```

### **5. Cross-Platform Sync**

```bash
# Sync with neovedic timestamp
bb sync-lexicon --timestamp neovedic

# Commit message:
# sync: grainlexicon update
#
# Timestamp: 12025-10-22--1945--PDT--moon-vishakha--09thhouse12--kae3g
# Synced repos: 17
# Source: grainpbc/grainlexicon v1.2.3
```

---

## 🧮 **Calculation Details**

### **Holocene Calendar**

Add 10,000 to CE year:

```clojure
(defn ce-to-holocene [ce-year]
  (+ ce-year 10000))

;; 2025 CE → 12025 HE
```

**Why Holocene?**
- Aligns with human civilization (~12,000 years)
- Positive years for all human history
- More holistic timescale

### **Moon Nakshatra**

Calculate current moon position in zodiac:

```clojure
(defn calculate-nakshatra [moon-longitude]
  (let [nakshatra-span (/ 360.0 27)
        nakshatra-index (int (/ moon-longitude nakshatra-span))]
    (nth nakshatras nakshatra-index)))
```

Each nakshatra spans ~13°20' of the zodiac.

### **House Position**

Calculate house and degrees:

```clojure
(defn calculate-house-position [moon-longitude ascendant-longitude]
  (let [relative-position (mod (- moon-longitude ascendant-longitude) 360)
        house (inc (int (/ relative-position 30)))
        degrees (mod relative-position 30)]
    {:house house :degrees (int degrees)}))
```

---

## 📊 **Data Files**

### **nakshatras.edn**

```clojure
{:nakshatras
 [{:name "ashwini"
   :sanskrit "अश्विनी"
   :english "Horse-headed"
   :deity "Ashwini Kumaras"
   :symbol "Horse's head"
   :range [0 13.333]
   :qualities [:swift :healing :pioneering]}
  
  {:name "vishakha"
   :sanskrit "विशाखा"
   :english "Forked Branch"
   :deity "Indra and Agni"
   :symbol "Triumphal arch"
   :range [200 213.333]
   :qualities [:determined :goal-oriented :transformative]}
  
  ;; ... all 27
  ]}
```

### **houses.edn**

```clojure
{:houses
 [{:number 1
   :name "Tanu Bhava"
   :english "House of Self"
   :significations [:identity :appearance :personality]
   :ruling-planet :mars}
  
  {:number 9
   :name "Dharma Bhava"
   :english "House of Philosophy"
   :significations [:higher-learning :philosophy :dharma :fortune]
   :ruling-planet :jupiter}
  
  ;; ... all 12
  ]}
```

---

## 🔄 **Integration with Grain Network**

### **With Grainclay**

```clojure
(require '[grainneovedic.core :as nv]
         '[grainclay.core :as gc])

(def timestamp (nv/generate-timestamp))
(def clay-path (gc/create-immutable-path timestamp))

;; Grainclay path with neovedic timestamp:
;; /clay/12025/10/22/1945-PDT-moon-vishakha-09thhouse12-kae3g
```

### **With Grainlexicon**

```clojure
;; Sync with neovedic timestamp
(require '[grainlexicon.sync :as sync]
         '[grainneovedic.core :as nv])

(let [timestamp (nv/generate-timestamp)
      commit-msg (format "sync: grainlexicon update\n\nTimestamp: %s" timestamp)]
  (sync/sync-all-repos commit-msg))
```

### **With Session Tracking**

```clojure
;; Create session file with neovedic timestamp
(require '[grainneovedic.session :as session])

(session/create-session-file
  {:session-number 804
   :user "kae3g"
   :title "Lexicon Implementation"})

;; Creates:
;; docs/SESSION-804-12025-10-22--1945--PDT--moon-vishakha--09thhouse12--kae3g.md
```

---

## 🌟 **Philosophy**

**grainneovedic** embodies the Grain Network principle:

> **"Technology should honor cosmic rhythms, not just mechanical time"**

Western timestamps (ISO 8601) are:
- ❌ Mechanical
- ❌ Disconnected from nature
- ❌ Purely quantitative

Neovedic timestamps are:
- ✅ Cosmic
- ✅ Connected to astronomy
- ✅ Qualitative + quantitative
- ✅ Spiritually aligned

This isn't about religion - it's about **acknowledging we exist in a cosmos**, not just a calendar.

---

## 🔗 **Related Projects**

- [Grainclay Package Manager](https://grainpbc.github.io/grainclay/)
- [Grainlexicon Documentation System](https://grainpbc.github.io/grainlexicon/)
- [Grain Network Websites](https://kae3g.github.io/grainkae3g/docs/infrastructure/GRAIN-NETWORK-WEBSITES.html)

---

## 📄 **License**

MIT License - Same as all Grain Network projects

---

## 🤝 **Contributing**

To add more nakshatras data or improve astronomical calculations:

1. Fork this repo
2. Edit `data/nakshatras.edn` or `src/grainneovedic/astronomy.clj`
3. Run `bb test`
4. Submit PR

---

**grainneovedic**: Time with cosmic consciousness 🌙🌾

