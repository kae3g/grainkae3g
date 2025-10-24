# 🌾 graintimekae3g - Personal Graintime Module

**Personal Version**: kae3g (1-of-88)  
**Template Source**: grainpbc/graintime  
**Graintime**: 12025-10-23--0745--PDT--moon-vishakha------asc-gem000--sun-01st--kae3g

---

## 🎯 What is graintimekae3g?

This is **kae3g's personal graintime configuration**, personalized from the `grainpbc/graintime` template.

**Key Insight**: The `gt` command is globally installed, but this module provides:
- Personal location config (San Rafael, CA)
- Custom graintime formats
- Personal grainpath abbreviations
- kae3g-specific timestamp utilities
- Integration with grainkae3g workflows

---

## 🌟 Features

### 1. **Graintime Generation** ⏰
```bash
gt                    # Generate current graintime
gt at "2025-10-23 15:30"  # Generate graintime for specific time
```

**Output Format**:
```
12025-10-23--0745--PDT--moon-vishakha------asc-gem000--sun-01st--kae3g
```

### 2. **Solar House Clock** ☀️
- **1st house**: Sunrise (07:25 AM) - New beginnings
- **10th house**: Solar noon (12:54 PM) - Peak productivity
- **7th house**: Sunset (18:22 PM) - Wind down
- **4th house**: Solar midnight (00:54 AM) - Deep rest

### 3. **Personal Location**
- Default: San Rafael, CA (37.9735°N, -122.5311°W)
- Timezone: PDT/PST (Pacific)
- Customizable via `personal/location.edn`

---

## 📁 Directory Structure

```
grainclay/graintimekae3g/
├── README.md              # This file
├── DESIGN.md              # Design philosophy
├── bb.edn                 # Babashka tasks
├── scripts/
│   ├── graintime.bb       # Core graintime logic
│   ├── solar-house.bb     # Solar house calculations
│   └── abbreviate.bb      # Grainpath abbreviation
├── template/
│   └── location.edn       # Template location config
├── personal/
│   └── location.edn       # kae3g's location (gitignored)
├── src/
│   └── graintimekae3g/
│       └── core.clj       # Clojure API
└── docs/
    ├── SOLAR-HOUSE.md     # Solar house system
    └── GRAINPATH.md       # Grainpath generation
```

---

## 🔧 Installation

### Option 1: Use Global `gt` Command
```bash
# Already installed!
gt
```

### Option 2: Use as Babashka Module
```bash
cd grainclay/graintimekae3g
bb graintime
bb solar-house
```

### Option 3: Use from Clojure REPL
```clojure
(require '[graintimekae3g.core :as gt])

(gt/now "kae3g")
;; => "12025-10-23--0745--PDT--moon-vishakha------asc-gem000--sun-01st--kae3g"

(gt/solar-house)
;; => 1  ; Current house (1st = sunrise)
```

---

## 🌾 Integration with grainkae3g

### In `qb-now` Command:
```clojure
(defn get-current-solar-house []
  (let [result (shell {:out :string} "gt")
        graintime (:out result)
        house-match (re-find #"sun-(\d+)" graintime)]
    (Integer/parseInt (second house-match))))
```

### In Course Creation:
```bash
bb qb-course-sync-system-new "intro-cursor-ultra"
# Uses current graintime for directory name:
# 12025-10-23--0745--PDT--moon-vishakha------asc-gem000--sun-01st--kae3g/intro-cursor-ultra/
```

### In Git Branches:
```bash
git checkout -b graintime-$(gt | tail -1)
# => graintime-12025-10-23--0745--PDT--moon-vishakha------asc-gem000--sun-01st--kae3g
```

---

## 🎨 Personal Configuration

### Location Setup:
```clojure
;; personal/location.edn (gitignored)
{:city "San Rafael"
 :state "California"
 :country "USA"
 :lat 37.9735
 :lon -122.5311
 :timezone "America/Los_Angeles"
 :display-name "San Rafael, CA, USA"
 :author "kae3g"}
```

### Custom Abbreviations:
```clojure
;; personal/abbreviations.edn
{"introduction-to-functional-programming" "intro-fp"
 "advanced-type-systems" "adv-types"
 "grain-network-complete-course" "grain-complete"}
```

---

## 🌅 Solar House Philosophy

**Daily Clock, Not Zodiac!**

The solar house system is a **24-hour clock** based on the sun's position in the sky:

- Each house = ~2 hours
- Resets every day at sunrise
- Counterclockwise progression: 1→12→11→10→...
- Based on **diurnal motion** (daily), not zodiac degrees

**tri5h's Meal Time Analogy** 🍱:
- 1st house (sunrise) = Breakfast time
- 10th house (noon) = Lunch time
- 7th house (sunset) = Dinner time
- 4th house (midnight) = Sleep time

---

## 🚀 Babashka Tasks

```bash
bb graintime              # Generate current graintime
bb solar-house            # Show current solar house
bb grainpath course intro-fp  # Generate grainpath
bb abbreviate "long course title"  # Test abbreviation
```

---

## 🔗 Related Modules

### Template Source:
- `grainpbc/graintime` - Original template (if created)

### Integration Points:
- `grainbarrel` - Uses `gt` for timestamps
- `qb-now` - Solar house routing
- `qb course sync` - Course directory naming
- `grainkae3g` - Git branch naming

### Personal Modules:
- `grainkae3g` - Main kae3g repository
- `6oskae3g` - Symlinked SixOS version
- `graintimekae3g` - This module!

---

## 📖 Philosophy

**From the PSEUDO.md**:

> "Time is not an afterthought—it's a first-class citizen. Every grain knows when it was planted."

**Local Control, Global Intent**:
- **Local**: Your personal location, timezone, preferences
- **Global**: Universal graintime format, solar house system

**Template/Personal Everywhere**:
- **Template**: `grainpbc/graintime` (if it exists)
- **Personal**: `graintimekae3g` (this repo!)

---

## 🌾 graindevname

**graintimekae3g** = graintime + kae3g

Where:
- **graintime** = Universal timestamp system
- **kae3g** = Personal sheaf (1-of-88)

---

## 📊 Examples

### Current Time (07:45 AM):
```
12025-10-23--0745--PDT--moon-vishakha------asc-gem000--sun-01st--kae3g

Breakdown:
  12025           Holocene year
  10-23           October 23
  0745            7:45 AM
  PDT             Pacific Daylight Time
  moon-vishakha   Current nakshatra (lunar mansion)
  asc-gem000      Ascendant: Gemini 0°
  sun-01st        Solar house: 1st (sunrise!)
  kae3g           Author/sheaf
```

### Grainpath Example:
```
/course/kae3g/intro-cursor-ultra/12025-10-23--0745--PDT--moon-vishakha------asc-gem000--sun-01st--kae3g/
```

---

## 🎯 Use Cases

1. **Course Versioning**: Immutable graintime-stamped courses
2. **Git Branching**: Graintime-based branch names
3. **Solar House Routing**: Time-of-day aware commands
4. **Temporal Awareness**: Every action knows its time
5. **Astronomical Precision**: Real sunrise/sunset/noon/midnight

---

## 🌟 Why graintimekae3g?

**Question**: Why not just use the global `gt` command?

**Answer**: 
- `gt` is the **tool**
- `graintimekae3g` is the **integration**

This module provides:
- Clojure API for programmatic access
- Babashka tasks for scripting
- Personal configuration management
- Integration with grainkae3g workflows
- Solar house calculations for routing
- Grainpath abbreviation logic

**Analogy**: Like how `date` is a Unix tool, but you still wrap it in your own scripts!

---

## 📝 TODO

- [ ] Add graintime caching (avoid regenerating for same minute)
- [ ] Create graintime diff tool (compare two graintimes)
- [ ] Add graintime parser (extract components from string)
- [ ] Implement graintime range queries (all courses between X and Y)
- [ ] Create graintime calendar view (visualize courses on timeline)

---

now == next + 1 🌾

**Repository**: https://github.com/kae3g/graintimekae3g  
**Codeberg**: https://codeberg.org/kae3g/graintimekae3g  
**Parent**: grainkae3g  
**Template**: grainpbc/graintime (conceptual)

