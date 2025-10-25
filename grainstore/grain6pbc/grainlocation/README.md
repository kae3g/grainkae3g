# 🌾 grainlocation - Human-Readable Location Addressing

> **"what3words meets grainpath: 100-character location nicknames"**

A location addressing system inspired by what3words but using common English terms, supporting w3w spec, and integrating with grainpath for temporal context.

---

## 📋 **Philosophy**

**"Memorable locations without coordinates"**

Instead of:
- ❌ `37.9735°N, 122.5311°W` (hard to remember)
- ❌ `caspar-california-usa-mendocino-county` (too long)
- ⚠️  `filled.count.soap` (what3words - random, no meaning)

Use:
- ✅ `caspar-coastal-cabin` (clear, memorable, <100 chars)
- ✅ `forest-starlink-ridge` (descriptive, contextual)
- ✅ `vegan-sanctuary-mendocino` (meaningful, specific)

---

## 🎯 **Use Cases**

### **1. Location Nicknames**

```clojure
;; Define a location
(grainlocation/define
  {:nickname "caspar-coastal-cabin"
   :lat 39.3625
   :lon -123.8139
   :city "Caspar, CA"
   :timezone "America/Los_Angeles"
   :tags [:forest :coastal :starlink :cabin]})

;; Use it
(graintime/generate-graintime "kae3g" 
  :location "caspar-coastal-cabin")
```

### **2. Business/Venue Names**

```clojure
;; Venue alias
(grainlocation/define
  {:nickname "vegan-bakery-mission"
   :lat 37.7599
   :lon -122.4148
   :address "Mission District, San Francisco, CA"
   :type :business
   :vegan true})

;; Photo collection (Apple Photos style)
(grainlocation/photos-at "vegan-bakery-mission")
;; Returns all photos taken at this location
```

### **3. Street Address Nicknames**

```clojure
;; Home address nickname
(grainlocation/define
  {:nickname "framework-hacker-house"
   :lat 37.9735
   :lon -122.5311
   :address "San Rafael, CA"
   :type :home
   :devices [:framework-16 :starlink]})
```

---

## 🌐 **what3words Compatibility**

### **Supporting w3w Spec**

```clojure
;; Convert grainlocation to what3words
(grainlocation/to-w3w "caspar-coastal-cabin")
;; Returns: "filled.count.soap" (via what3words API)

;; Convert what3words to grainlocation
(grainlocation/from-w3w "filled.count.soap")
;; Returns: {:nickname "caspar-coastal-cabin-w3w"
;;           :lat 39.3625 :lon -123.8139}

;; Support both formats
(grainlocation/resolve "filled.count.soap")
(grainlocation/resolve "caspar-coastal-cabin")
;; Both return same coordinates!
```

---

## 📏 **Format: 100 Characters Max**

```
caspar-coastal-cabin-starlink-ridge-mendocino-county-california-usa-pacific-timezone-forest-wifi
│                                                                                              │
└──────────────────────────────────── 100 chars max ────────────────────────────────────────────┘
```

**Rules**:
- Lowercase
- Hyphens (no spaces)
- Common English words (not random)
- Descriptive and memorable
- Optional tags at end
- Max 100 characters

---

## 🗺️ **Architecture**

```
grainlocation
    ↓
┌─────────────────────────────────────┐
│  Location Nickname System           │
├─────────────────────────────────────┤
│  caspar-coastal-cabin               │
│  └─ lat: 39.3625                    │
│  └─ lon: -123.8139                  │
│  └─ tags: [:forest :starlink]       │
├─────────────────────────────────────┤
│  what3words Integration             │
│  └─ to-w3w / from-w3w               │
├─────────────────────────────────────┤
│  Photo Collections (Apple style)    │
│  └─ Group by location + time        │
├─────────────────────────────────────┤
│  grainpath Integration              │
│  └─ Location + Graintime = Path     │
└─────────────────────────────────────┘
```

---

## 🌀 **grainwormhole: Time-Travel + Location-Travel Testing**

```clojure
(ns grainwormhole.core
  "Time and space travel for testing
   
   grainwormhole = grainmock synonym
   Combines grainlocation + graintime for complete test context"
  (:require [grainlocation.core :as loc]
            [graintime.core :as gt]))

(defmacro wormhole
  "Travel to any time and place for testing"
  [{:keys [time location]} & body]
  `(binding [graintime.core/*current-time* ~time
             grainlocation.core/*current-location* ~location]
     ~@body))

;; Example: Test at Caspar cabin during morning
(wormhole {:time "2025-10-24T06:00:00"
           :location "caspar-coastal-cabin"}
  (graintime/generate-graintime "test"))
;; Returns graintime with:
;; - Morning time (06:00)
;; - Caspar coordinates  
;; - Gemini rising (early morning)
;; - 1st house (sunrise area)

;; Example: Test at Mumbai during afternoon
(wormhole {:time "2025-10-24T14:30:00"
           :location "mumbai-office"}
  (graintime/generate-graintime "test"))
```

---

## 📸 **Photo Collections (Apple Photos Inspired)**

```clojure
(ns grainlocation.photos
  "Group photos by location + time (Apple Photos style)")

(defn photos-at
  "Get all photos at a location"
  [location-nickname]
  (-> (grainlocation/resolve location-nickname)
      (find-photos-near-coordinates)))

(defn photos-during
  "Get photos at location during graintime period"
  [location-nickname grainpath]
  (let [loc (grainlocation/resolve location-nickname)
        time-range (grainpath/to-time-range grainpath)]
    (find-photos loc time-range)))

;; Example: All photos at Caspar cabin
(photos-at "caspar-coastal-cabin")

;; Example: Caspar cabin photos during specific grainpath
(photos-during "caspar-coastal-cabin"
               "12025-10-23--*--PDT--*")
```

---

## 🧪 **Integration with grainmock/grainwormhole**

```clojure
;; Test graintime at different locations
(grainwormhole/wormhole
  {:time "2025-10-24T12:00:00"
   :location "mumbai-office"}
  (let [gt (graintime/generate-graintime "test")]
    (is (re-find #"IST" gt))  ; Indian Standard Time
    (is (re-find #"asc-" gt))))  ; Different ascendant than California

;; Test grain6 scheduling at Caspar cabin
(grainwormhole/wormhole
  {:time "2025-10-24T07:25:00"  ; Sunrise at Caspar
   :location "caspar-coastal-cabin"}
  (is (grain6/should-start? :graindisplay-warm-light)))

;; Test grainwifi at different locations
(grainwormhole/wormhole
  {:location "caspar-coastal-cabin"}
  (is (grainwifi/has-starlink?)))  ; Caspar has Starlink

(grainwormhole/wormhole
  {:location "mumbai-office"}
  (is (not (grainwifi/has-starlink?))))  ; Mumbai doesn't
```

---

## 📦 **grainlocation vs what3words**

| Feature | what3words | grainlocation |
|---------|-----------|---------------|
| **Words** | 3 random words | N descriptive words |
| **Length** | ~20 chars | Max 100 chars |
| **Meaning** | Random (no context) | Meaningful (descriptive) |
| **Memorability** | Hard (random) | Easy (contextual) |
| **Customization** | No (fixed algorithm) | Yes (user-defined) |
| **Compatibility** | w3w only | w3w + custom |
| **Precision** | 3m × 3m square | Configurable |
| **Offline** | Requires license | Free, open |

---

## 🏗️ **Directory Structure**

```
grain6pbc/grainlocation/          ← Template
├── src/grainlocation/
│   ├── core.clj                 ← Main API
│   ├── w3w.clj                  ← what3words integration
│   ├── photos.clj               ← Photo collections
│   └── nicknames.clj            ← Nickname generation
├── personal/                    ← .gitignored
│   └── locations.edn            ← User locations
├── fixtures/
│   ├── common_cities.edn        ← Major cities
│   └── grain_network_sites.edn  ← Grain Network locations
└── README.md

grain6pbc/grainwormhole/          ← Template (synonym for grainmock)
├── src/grainwormhole/
│   ├── core.clj                 ← Time + Location travel
│   ├── time_travel.clj          ← graintime mocking
│   └── location_travel.clj      ← grainlocation mocking
└── README.md

kae3g/grainkae3glocation/        ← Personal
├── personal/                    ← NOT gitignored (versioned!)
│   ├── caspar_ca.edn            ← Caspar cabin
│   ├── san_rafael.edn           ← San Rafael  
│   └── vegan_venues.edn         ← Vegan businesses
└── README.md
```

---

## 🎓 **For grain6 App Developers**

```clojure
;; Simple grain6 app testing
(require '[grainwormhole.core :as wormhole])

;; Test your grain6 service at sunrise in Caspar
(wormhole/wormhole
  {:time "sunrise"  ; Symbolic time
   :location "caspar-coastal-cabin"}
  (my-grain6-service/test-startup))

;; Test at multiple locations
(doseq [loc ["caspar-coastal-cabin"
             "mumbai-office"
             "tokyo-coworking"]]
  (wormhole/wormhole
    {:time "noon" :location loc}
    (test-my-service)))
```

---

## 📚 **Resources**

- **what3words**: https://what3words.com/
- **Apple Photos**: Location-based photo collections
- **grainpath**: Temporal context
- **graintime**: Astronomical timestamps

---

**Status**: 🌱 Design phase  
**Dependencies**: grainpath, graintime  
**Synonyms**: grainwormhole = grainmock (testing context)  

🌾 **now == next + 1** (but make it location-aware, chief!) 🗺️
