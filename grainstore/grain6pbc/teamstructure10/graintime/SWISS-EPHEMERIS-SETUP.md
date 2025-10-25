# Swiss Ephemeris Setup for Graintime

## Downloads Required

### 1. JAR File
- **Source:** https://sourceforge.net/projects/ephemeris-examples/files/Tithis/libs/swisseph-2.01.00-01.jar/
- **Destination:** `graintime/lib/swisseph-2.01.00-01.jar`

### 2. Ephemeris Data Files
- **Data for 1800-2399 AD:** `sepl_18.se1` (covers our needs!)
- **Source:** https://www.astro.com/ftp/swisseph/ephe/
- **Destination:** `graintime/resources/ephe/`

**Files needed:**
```
sepl_18.se1  - Planets 1800-2399 AD (main file we need)
semo_18.se1  - Moon 1800-2399 AD (for extra precision)
seas_18.se1  - Asteroids 1800-2399 AD (optional)
```

## Setup Instructions

```bash
# 1. Create directories
cd graintime
mkdir -p lib resources/ephe

# 2. Download JAR
wget -O lib/swisseph-2.01.00-01.jar \
  https://sourceforge.net/projects/ephemeris-examples/files/Tithis/libs/swisseph-2.01.00-01.jar/download

# 3. Download ephemeris data
cd resources/ephe
wget https://www.astro.com/ftp/swisseph/ephe/sepl_18.se1
wget https://www.astro.com/ftp/swisseph/ephe/semo_18.se1
```

## Integration with Babashka

```clojure
;; deps.edn
{:paths ["src" "resources"]
 :deps {org.clojure/clojure {:mvn/version "1.11.1"}}
 :aliases
 {:swisseph
  {:extra-paths ["lib/swisseph-2.01.00-01.jar"]}}}
```

## Usage Example

```clojure
(ns graintime.swiss-ephemeris-real
  (:import [swisseph SwissEph SweDate SweConst]))

(defn calculate-moon-position
  "Calculate actual moon position using Swiss Ephemeris"
  [datetime]
  (let [sw (SwissEph.)
        ;; Set path to ephemeris files
        _ (.swe_set_ephe_path sw "resources/ephe")
        
        ;; Convert datetime to Julian Day
        jd (datetime-to-julian-day datetime)
        
        ;; Prepare arrays for results
        xx (double-array 6)
        serr (StringBuffer.)
        
        ;; Calculate Moon position (tropical)
        flag (bit-or SweConst/SEFLG_SWIEPH SweConst/SEFLG_SPEED)
        ret (.swe_calc sw jd SweConst/SE_MOON flag xx serr)]
    
    (if (= ret SweConst/OK)
      {:longitude (aget xx 0)
       :latitude (aget xx 1)
       :distance (aget xx 2)
       :speed-lon (aget xx 3)
       :speed-lat (aget xx 4)
       :speed-dist (aget xx 5)}
      (throw (ex-info "Swiss Ephemeris calculation failed"
                      {:error (.toString serr)})))))

(defn calculate-sidereal-moon
  "Calculate moon position in sidereal zodiac (for nakshatras)"
  [datetime]
  (let [sw (SwissEph.)
        _ (.swe_set_ephe_path sw "resources/ephe")
        
        ;; Set sidereal mode with Lahiri ayanamsa
        _ (.swe_set_sid_mode sw SweConst/SE_SIDM_LAHIRI 0.0 0.0)
        
        jd (datetime-to-julian-day datetime)
        xx (double-array 6)
        serr (StringBuffer.)
        
        ;; Calculate with SIDEREAL flag
        flag (bit-or SweConst/SEFLG_SWIEPH 
                     SweConst/SEFLG_SIDEREAL
                     SweConst/SEFLG_SPEED)
        ret (.swe_calc sw jd SweConst/SE_MOON flag xx serr)]
    
    (if (= ret SweConst/OK)
      {:sidereal-longitude (aget xx 0)
       :nakshatra-index (int (/ (aget xx 0) 13.333333))
       :julian-day jd}
      (throw (ex-info "Swiss Ephemeris sidereal calculation failed"
                      {:error (.toString serr)})))))
```

## Testing

```clojure
;; Test against Astromitra known value
;; Oct 25, 2025, 19:51 IST → Jyeshtha

(let [test-dt (LocalDateTime/of 2025 10 25 19 51 0)
      result (calculate-sidereal-moon test-dt)]
  (println "Nakshatra index:" (:nakshatra-index result))
  (println "Sidereal longitude:" (:sidereal-longitude result))
  ;; Should be index 17 (Jyeshtha) with longitude 226-240°
  )
```

## Benefits

✅ **Accurate to the second** - Real astronomical calculations
✅ **Offline** - No API dependencies once files downloaded
✅ **Fast** - Native Java performance
✅ **Proven** - Used by professional astrologers worldwide
✅ **Coverage** - 1800-2399 AD (600 years!)

## Next Steps

1. Download JAR and data files
2. Add to `deps.edn`
3. Replace `swiss_ephemeris.clj` placeholder with real implementation
4. Test against Astromitra test case (Jyeshtha)
5. Verify against AstrOccult.net IST data
6. Deploy to production

---

**Status:** Ready to implement. Downloads identified, integration path clear.

now == next + 1 🌾

