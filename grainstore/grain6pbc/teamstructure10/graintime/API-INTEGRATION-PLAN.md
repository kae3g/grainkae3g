# Graintime API Integration Plan

**Accurate astronomical calculations for neovedic timestamps**

> *"From granules to grains to THE WHOLE GRAIN"*  
> Precision matters - houses change every 2 hours, nakshatras shift with the moon.

**Created**: Session 808  
**Status**: Design & Roadmap

---

## ⚠️ Why We Need Astronomical APIs

### The Problem with Simplified Calculations

**Current graintime output**:
```
12025-10-22--2010--PDT--moon-uttara-ashadha--11thhouse22--kae3g
```

**Issues**:
1. **House calculation is wrong**: 11th house at 8:10 PM doesn't make sense
   - Houses depend on **ascendant** (rising sign)
   - Ascendant changes every ~2 hours
   - Requires precise calculation based on time + location
   
2. **Nakshatra calculation is simplified**: Using day-of-year approximation
   - Real nakshatras depend on **moon's sidereal position**
   - Moon moves ~13° per day through 27 nakshatras
   - Requires ephemeris data

### What Houses Actually Mean

The **ascendant** (1st house cusp) is the zodiac degree rising on the eastern horizon at your exact time and location.

**Example for San Rafael, CA (37.97°N, 122.53°W)**:

| Time | Ascendant | 1st House | 9th House | 11th House |
|------|-----------|-----------|-----------|------------|
| 6:00 AM | ~Libra | Self (Libra) | Philosophy (Gemini) | Friends (Leo) |
| 12:00 PM | ~Capricorn | Self (Capricorn) | Philosophy (Virgo) | Friends (Scorpio) |
| 6:00 PM | ~Aries | Self (Aries) | Philosophy (Sagittarius) | Friends (Aquarius) |
| 8:10 PM | ~Taurus/Gemini | Self (Taurus) | Philosophy (Capricorn) | Friends (Pisces) |

**At 8:10 PM**, the ascendant would be around Taurus/Gemini, making:
- 1st house: Taurus (self)
- 9th house: Capricorn (philosophy, higher learning) ← More likely for Session 808!
- 11th house: Pisces (friends, aspirations)

---

## 🎯 Correct Graintime Output

### With Proper API Integration

**What it should be**:
```
12025-10-22--2010--PDT--moon-uttara-ashadha-pada2--asc-taurus--sun-09thhouse15--kae3g
```

**Components**:
- `moon-uttara-ashadha-pada2` - Sidereal nakshatra + pada (quarter)
- `asc-taurus` - Tropical ascendant (rising sign)
- `sun-09thhouse15` - Sun in 9th house, 15° (from ascendant)
- `kae3g` - Author

**Meaning**: Moon in Uttara Ashadha (victory/permanence), Sun in 9th house (philosophy/higher learning), Taurus rising (grounded, stable) - perfect for Session 808!

---

## 🔧 Implementation Roadmap

### Phase 1: Swiss Ephemeris (Local) - Recommended

**Library**: pyswisseph or direct C library via JNI

**Setup**:
```bash
# Install Swiss Ephemeris
sudo apt install libswe-dev

# Download ephemeris data files
mkdir -p ~/.local/share/swisseph
cd ~/.local/share/swisseph
wget https://www.astro.com/ftp/swisseph/ephe/seas_18.se1
wget https://www.astro.com/ftp/swisseph/ephe/semo_18.se1
wget https://www.astro.com/ftp/swisseph/ephe/sepl_18.se1
```

**Clojure Integration**:
```clojure
(ns graintime.swisseph
  "Swiss Ephemeris integration via JNI"
  (:import [swisseph SweDate SwissEph]))

(defn calculate-moon-nakshatra
  "Calculate precise moon position and nakshatra"
  [datetime latitude longitude]
  (let [jd (SweDate/julianDay datetime)
        swe (SwissEph. (System/getenv "SWISSEPH_PATH"))
        ;; Calculate sidereal moon position
        moon-pos (.calc swe jd SwissEph/SE_MOON SwissEph/SEFLG_SIDEREAL)
        moon-longitude (:longitude moon-pos)
        ;; Each nakshatra = 13.333° (360/27)
        nakshatra-index (int (/ moon-longitude 13.333))
        nakshatra-degree (mod moon-longitude 13.333)
        pada (inc (int (/ nakshatra-degree 3.333)))]
    {:nakshatra (nth nakshatras nakshatra-index)
     :degree nakshatra-degree
     :pada pada
     :longitude moon-longitude}))

(defn calculate-tropical-ascendant
  "Calculate tropical ascendant for given time/location"
  [datetime latitude longitude]
  (let [jd (SweDate/julianDay datetime)
        swe (SwissEph. (System/getenv "SWISSEPH_PATH"))
        ;; Calculate tropical houses
        houses (.getHouses swe jd latitude longitude 
                          SwissEph/SE_HS_WHOLE SwissEph/SEFLG_TROPICAL)
        ascendant (:ascendant houses)
        ascendant-sign (nth tropical-signs (int (/ ascendant 30)))]
    {:longitude ascendant
     :sign ascendant-sign
     :degree (mod ascendant 30)}))
```

### Phase 2: Astro-Seek API (Remote Fallback)

**API Endpoint**: https://horoscopes.astro-seek.com/api

**Example Request**:
```bash
curl -X POST "https://horoscopes.astro-seek.com/calculate-chart-api" \
  -H "Content-Type: application/json" \
  -d '{
    "year": 2025,
    "month": 10,
    "day": 22,
    "hour": 20,
    "minute": 10,
    "latitude": 37.9735,
    "longitude": -122.5311,
    "timezone": "America/Los_Angeles",
    "house_system": "W",
    "zodiac_type": "tropical",
    "ayanamsa": "1"
  }'
```

**Response** (expected):
```json
{
  "ascendant": {
    "longitude": 65.24,
    "sign": "Gemini",
    "degree": 5.24
  },
  "moon": {
    "sidereal_longitude": 286.45,
    "nakshatra": "Uttara Ashadha",
    "pada": 2,
    "degree": 12.12
  },
  "sun": {
    "tropical_longitude": 209.87,
    "house": 9,
    "house_degree": 14.63
  }
}
```

**Clojure Integration**:
```clojure
(ns graintime.astro-seek
  "Astro-Seek API integration"
  (:require [babashka.http-client :as http]
            [cheshire.core :as json]))

(defn fetch-chart-data
  "Fetch chart data from Astro-Seek API"
  [datetime location]
  (let [payload {:year (.getYear datetime)
                 :month (.getMonthValue datetime)
                 :day (.getDayOfMonth datetime)
                 :hour (.getHour datetime)
                 :minute (.getMinute datetime)
                 :latitude (:latitude location)
                 :longitude (:longitude location)
                 :timezone (:timezone location)
                 :house_system "W"  ; Whole sign
                 :zodiac_type "tropical"
                 :ayanamsa "1"}  ; Lahiri
        response (http/post "https://horoscopes.astro-seek.com/calculate-chart-api"
                           {:body (json/generate-string payload)
                            :headers {"Content-Type" "application/json"}})
        data (json/parse-string (:body response) true)]
    {:ascendant (:ascendant data)
     :moon (:moon data)
     :sun (:sun data)}))
```

### Phase 3: Cosmic Insights API (Alternative)

**API**: https://api.cosmicinsights.net/v1

**Features**:
- Vedic astrology focus
- Nakshatra padas
- Planetary transits
- Dasha periods

---

## 📋 Configuration: Template/Personal Split

### Template Configuration

**File**: `template/location.edn.template`

```clojure
{:location
 {:city "Your City"
  :state "Your State"
  :country "Your Country"
  :timezone "America/Los_Angeles"  ; IANA format
  :latitude 37.9735   ; Decimal degrees
  :longitude -122.5311  ; Decimal degrees (West = negative)
  
  ;; Tropical zodiac for houses (seasonal alignment)
  :zodiac :tropical
  
  ;; Sidereal nakshatras (star alignment)
  :nakshatra-system :sidereal
  
  ;; Ayanamsa (precession correction)
  ;; :lahiri (most common, ~24.15° as of 2025)
  :ayanamsa :lahiri
  
  ;; House system
  ;; :whole-sign (each sign = one house)
  :house-system :whole-sign}
 
 :api
 {:provider :swiss-ephemeris  ; or :astro-seek, :cosmic-insights
  :mode :local  ; :local (Swiss Ephemeris files) or :remote (API)
  
  ;; Swiss Ephemeris configuration
  :swiss-ephemeris
  {:data-path "/usr/share/swisseph"  ; or ~/.local/share/swisseph
   :files ["seas_18.se1" "semo_18.se1" "sepl_18.se1"]}
  
  ;; Remote API fallback
  :remote-apis
  {:astro-seek
   {:endpoint "https://horoscopes.astro-seek.com/calculate-chart-api"
    :api-key nil}  ; Set in grainenvvars if needed
   
   :cosmic-insights
   {:endpoint "https://api.cosmicinsights.net/v1/chart"
    :api-key nil}}  ; Set in grainenvvars
  
  ;; Caching
  :cache
  {:enabled true
   :ttl-hours 24
   :directory ".graintime-cache"}}}
```

### Personal Configuration

**File**: `personal/kae3g-san-rafael.edn`

```clojure
{:location
 {:city "San Rafael"
  :state "CA"
  :country "USA"
  :timezone "America/Los_Angeles"
  
  ;; San Rafael coordinates
  :latitude 37.9735
  :longitude -122.5311
  
  ;; Tropical for houses, sidereal for nakshatras
  :zodiac :tropical
  :nakshatra-system :sidereal
  :ayanamsa :lahiri
  :house-system :whole-sign}
 
 :api
 {:provider :swiss-ephemeris
  :mode :local
  :swiss-ephemeris
  {:data-path "~/.local/share/swisseph"}}
 
 :author "kae3g"}
```

---

## 🚀 Setup Instructions

### Step 1: Install Swiss Ephemeris (Recommended)

```bash
# On Ubuntu/Debian
sudo apt install libswe-dev

# Download ephemeris data
mkdir -p ~/.local/share/swisseph
cd ~/.local/share/swisseph

# Get data files (covers 600 years: 1800-2400 CE)
wget https://www.astro.com/ftp/swisseph/ephe/seas_18.se1  # Asteroid data
wget https://www.astro.com/ftp/swisseph/ephe/semo_18.se1  # Moon data
wget https://www.astro.com/ftp/swisseph/ephe/sepl_18.se1  # Planet data
```

### Step 2: Configure Location

```bash
# Copy template
cd ~/kae3g/grainkae3g/grainstore/graintime
cp template/location.edn.template personal/$(whoami)-$(hostname).edn

# Edit with your coordinates
nano personal/$(whoami)-$(hostname).edn

# Get your coordinates:
# Visit: https://www.latlong.net/
# Or use: curl "https://ipapi.co/json/" | jq '.latitude, .longitude'
```

### Step 3: Set Default Location

```bash
cd personal
ln -s kae3g-san-rafael.edn default.edn
```

### Step 4: Set Environment Variable

```bash
# Add to ~/.zshrc or grainenvvars
export SWISSEPH_PATH="$HOME/.local/share/swisseph"
export GRAINTIME_CONFIG="$HOME/kae3g/grainkae3g/grainstore/graintime/personal/default.edn"
```

---

## 🧪 Testing Accurate Calculations

### Test Ascendant Calculation

**San Rafael, CA - October 22, 2025, 8:10 PM PDT**:

**Expected Results** (from Swiss Ephemeris):
- **Ascendant**: ~Gemini 5° (rising sign)
- **Sun House**: 6th house (daily work, health) - Sun in Libra at 8 PM
- **Moon Nakshatra**: Uttara Ashadha pada 2 (sidereal)

**Correct Graintime**:
```
12025-10-22--2010--PDT--moon-uttara-ashadha-pada2--asc-gemini05--sun-06thhouse--kae3g
```

**Why this makes sense**:
- 8:10 PM → Gemini rising (changes every 2 hours)
- Sun in 6th house → Daily work, service (Session 808 was work!)
- Moon in Uttara Ashadha → Victory, permanence (achieved major milestones!)

---

## 📊 API Comparison

| Feature | Swiss Ephemeris | Astro-Seek | Cosmic Insights |
|---------|-----------------|------------|-----------------|
| **Type** | Local library | Web API | Web API |
| **Cost** | Free | Free | Paid tiers |
| **Accuracy** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| **Speed** | Very fast | Moderate | Moderate |
| **Offline** | ✅ Yes | ❌ No | ❌ No |
| **Setup** | Complex | Easy | Easy |
| **Tropical** | ✅ Yes | ✅ Yes | ✅ Yes |
| **Sidereal** | ✅ Yes | ✅ Yes | ✅ Yes |
| **Nakshatras** | ✅ Yes | ✅ Yes | ✅ Yes |
| **Houses** | ✅ All systems | ✅ All systems | ✅ Most systems |

**Recommendation**: Swiss Ephemeris for production, Astro-Seek for development/fallback

---

## 💻 Implementation Priority

### Phase 1: Placeholder (Current) ✅

```clojure
;; Simple approximation
(defn calculate-house [hour]
  (inc (mod (quot hour 2) 12)))
```

**Status**: Working but inaccurate  
**Use**: Development only

### Phase 2: Swiss Ephemeris Integration (Next)

```clojure
;; Proper calculation
(defn calculate-ascendant [datetime lat lon]
  (swiss-eph/calculate-houses datetime lat lon))
```

**Status**: Planned  
**Use**: Production-ready accuracy

### Phase 3: API Fallback

```clojure
;; Fallback to web API if Swiss Ephemeris unavailable
(defn calculate-with-fallback [datetime location]
  (try
    (swiss-eph/calculate datetime location)
    (catch Exception e
      (astro-seek/calculate datetime location))))
```

**Status**: Future enhancement  
**Use**: Reliability and redundancy

### Phase 4: Caching Layer

```clojure
;; Cache results for 24 hours
(defn cached-calculation [datetime location]
  (let [cache-key (str datetime "-" (:latitude location))
        cached (get-cache cache-key)]
    (or cached
        (let [result (calculate-with-api datetime location)]
          (set-cache cache-key result 24)
          result))))
```

**Status**: Future optimization  
**Use**: Performance

---

## 🌐 Integration with Graincourse

### Updated create-course Command

```bash
# Old (semantic version)
gb create-course --author "kae3g" --name "my-course" --version "1.0.0"

# New (graintime)
gb create-course --author "kae3g" --name "my-course"
# Automatically generates graintime based on current moment + your location!
```

### Grainpath Output

**With accurate API**:
```
/course/kae3g/my-course/12025-10-22--2010--PDT--moon-uttara-ashadha-pada2--asc-gemini05--sun-06thhouse--kae3g/
```

**Repository Name**:
```
course-kae3g-my-course-12025-10-22-2010-PDT-moon-uttara-ashadha-pada2-asc-gemini05-sun-06thhouse-kae3g
```

---

## 🔒 Security: API Keys in grainenvvars

### Never commit API keys!

**Setup**:
```bash
# In grainstore/grainenvvars/personal/.env
ASTRO_SEEK_API_KEY=your_key_here
COSMIC_INSIGHTS_API_KEY=your_key_here

# Or use 1Password
op read "op://Private/Astro-Seek/API_KEY"
```

**Load in graintime**:
```clojure
(ns graintime.config
  (:require [clojure.java.io :as io]))

(defn load-api-keys []
  "Load API keys from grainenvvars"
  (let [env-file (io/file "../grainenvvars/personal/.env")]
    (when (.exists env-file)
      (parse-env-file env-file))))
```

---

## 📖 Documentation for Users

### Setup Guide

**File**: `grainstore/graintime/SETUP.md`

Content will cover:
1. Why astronomical accuracy matters
2. Installing Swiss Ephemeris
3. Configuring your location
4. Testing with `gt now`
5. Interpreting graintime output
6. API fallback options

### Educational Value

**Lesson 11** (proposed): "Time, Astronomy, and Immutable Paths"

Topics:
- How houses change throughout the day
- Why ascendant depends on location
- Tropical vs sidereal systems
- Implementing astronomical calculations
- Building with Swiss Ephemeris

---

## ⏱️ Timeline

| Phase | Task | Duration | Status |
|-------|------|----------|--------|
| 1 | Placeholder calculations | 1 day | ✅ Done |
| 2 | Swiss Ephemeris research | 1 day | ⏳ Next |
| 3 | JNI bindings for Clojure | 2 days | 📋 Planned |
| 4 | Location config system | 1 day | ✅ Done |
| 5 | API integration | 2 days | 📋 Planned |
| 6 | Testing & validation | 1 day | 📋 Planned |
| 7 | Documentation | 1 day | 📋 Planned |

**Total Estimate**: 9 days to production-ready accuracy

---

## 🎯 Success Criteria

### Accurate Graintime Must:

1. ✅ **House matches time of day**: 8 PM → Ascendant in Gemini/Cancer
2. ✅ **Nakshatra matches moon**: Accurate to within 1° 
3. ✅ **Reproducible**: Same input → same output
4. ✅ **Fast**: <100ms for calculation
5. ✅ **Offline-capable**: Works with local Swiss Ephemeris
6. ✅ **Documented**: Clear setup instructions

---

## 🌾 Why This Matters

### Astronomical Integrity

Graintime isn't just a timestamp - it's a **moment in the cosmos**:

- **Houses**: Where you are in your daily cycle
- **Nakshatras**: Moon's position in the stars
- **Planets**: Universal energies at that moment

**Example**: Session 808's graintime should show:
- **Sun in 6th house**: Daily work, service (building course systems)
- **Moon in Uttara Ashadha**: Victory, permanence (major milestones achieved)
- **Ascendant in Gemini**: Communication, learning (writing Lesson 8)

### Educational Opportunity

Students learn:
- **Real astronomy**: Not just theory, actual calculations
- **Cultural integration**: Vedic + Western systems
- **Practical coding**: API integration, caching, fallbacks
- **Time awareness**: How Earth's rotation affects the sky

---

**🌾 From granules to grains to THE WHOLE GRAIN - with astronomical precision!**

---

**Created by**: Grain PBC  
**Session**: 808  
**Status**: Design Complete - Ready for Implementation  
**Next**: Integrate Swiss Ephemeris for accurate calculations
