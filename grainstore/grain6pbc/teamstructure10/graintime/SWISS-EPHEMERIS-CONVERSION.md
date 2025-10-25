# Swiss Ephemeris → Nakshatra Conversion

## The Question

How can we use Swiss Ephemeris to calculate the same nakshatra results as AstrOccult.net?

## The Answer: Tropical → Sidereal → Nakshatra

**3-Step Conversion:**

1. **Get Tropical Moon Position** (Swiss Ephemeris default)
   - Swiss Ephemeris returns tropical (Western) zodiac positions
   - Example: Moon at 25° Libra (tropical)

2. **Apply Ayanamsa** (convert tropical → sidereal)
   - Ayanamsa = offset between tropical and sidereal zodiacs
   - Lahiri Ayanamsa (standard for Vedic astrology): ~24° in 2025
   - Formula: `sidereal_longitude = tropical_longitude - ayanamsa`
   - Example: 25° Libra (tropical) - 24° = 1° Libra (sidereal)

3. **Calculate Nakshatra** (from sidereal degrees)
   - Each nakshatra = 13°20' = 13.333...° 
   - Formula: `nakshatra_index = floor(sidereal_longitude / 13.333...)`
   - 360° ÷ 27 nakshatras = 13.333...° per nakshatra

## Nakshatra Degree Ranges (Sidereal)

Based on web search results:

| Nakshatra Index | Name               | Sidereal Degrees          | Zodiac Sign(s)           |
|-----------------|---------------------|---------------------------|--------------------------|
| 0               | Ashwini            | 0°00' – 13°20'            | Aries                    |
| 1               | Bharani            | 13°20' – 26°40'           | Aries                    |
| 2               | Krittika           | 26°40' – 40°00'           | Aries → Taurus           |
| 3               | Rohini             | 40°00' – 53°20'           | Taurus                   |
| 4               | Mrigashira         | 53°20' – 66°40'           | Taurus → Gemini          |
| 5               | Ardra              | 66°40' – 80°00'           | Gemini                   |
| 6               | Punarvasu          | 80°00' – 93°20'           | Gemini → Cancer          |
| 7               | Pushya             | 93°20' – 106°40'          | Cancer                   |
| 8               | Ashlesha           | 106°40' – 120°00'         | Cancer                   |
| 9               | Magha              | 120°00' – 133°20'         | Leo                      |
| 10              | Purva Phalguni     | 133°20' – 146°40'         | Leo                      |
| 11              | Uttara Phalguni    | 146°40' – 160°00'         | Leo → Virgo              |
| 12              | Hasta              | 160°00' – 173°20'         | Virgo                    |
| 13              | Chitra             | 173°20' – 186°40'         | Virgo → Libra            |
| 14              | Swati              | 186°40' – 200°00'         | Libra                    |
| 15              | Vishakha           | 200°00' – 213°20'         | Libra → Scorpio          |
| 16              | Anuradha           | 213°20' – 226°40'         | Scorpio                  |
| 17              | Jyeshtha           | 226°40' – 240°00'         | Scorpio                  |
| 18              | Mula               | 240°00' – 253°20'         | Sagittarius              |
| 19              | Purva Ashadha      | 253°20' – 266°40'         | Sagittarius              |
| 20              | Uttara Ashadha     | 266°40' – 280°00'         | Sagittarius → Capricorn  |
| 21              | Shravana           | 280°00' – 293°20'         | Capricorn                |
| 22              | Dhanishta          | 293°20' – 306°40'         | Capricorn → Aquarius     |
| 23              | Shatabhisha        | 306°40' – 320°00'         | Aquarius                 |
| 24              | Purva Bhadrapada   | 320°00' – 333°20'         | Aquarius → Pisces        |
| 25              | Uttara Bhadrapada  | 333°20' – 346°40'         | Pisces                   |
| 26              | Revati             | 346°40' – 360°00'         | Pisces                   |

## Verification Strategy

**IMPORTANT:** Verify against AstrOccult.net data first (IST-based)

### Phase 1: Verification (IST only)

1. **Use AstrOccult.net IST timestamps** (Oct 2023 - Nov 2025)
2. **Calculate with Swiss Ephemeris** (same IST time)
3. **Compare results** (should match exactly)
4. **If match → formula is correct** ✓

### Phase 2: Production (any location, any date)

Once verified:
- User requests: datetime + location (e.g., PDT, JST, CET)
- Swiss Ephemeris calculates (works for ANY timezone)
- No need for AstrOccult.net anymore
- Works for past dates, future dates, anywhere!

## Example Verification

**Test Case: October 25, 2025, 07:21 PDT (Sonoma, CA)**
**Convert to IST:** 07:21 PDT = 19:51 IST (same day)

From Astromitra.com: **Moon in Jyeshtha** (index 17)

**Step-by-step with Swiss Ephemeris (using IST):**

1. **Get tropical moon position** (using Swiss Ephemeris with IST)
   ```python
   import swisseph as swe
   # IST time: 19:51 (7:21 PDT + 12h 30m offset)
   jd = swe.julday(2025, 10, 25, 19 + 51/60.0)  # Julian Day (IST)
   moon_tropical = swe.calc_ut(jd, swe.MOON)[0]  # Tropical longitude
   # Expected: ~250-260° (need to verify actual value)
   ```

2. **Get Lahiri ayanamsa for 2025**
   ```python
   swe.set_sid_mode(swe.SIDM_LAHIRI)
   ayanamsa = swe.get_ayanamsa_ut(jd)
   # Expected: ~24.2° for 2025
   ```

3. **Convert to sidereal**
   ```python
   moon_sidereal = moon_tropical - ayanamsa
   # If tropical = 250° and ayanamsa = 24°
   # Then sidereal = 250° - 24° = 226° (Scorpio in sidereal)
   ```

4. **Calculate nakshatra**
   ```python
   nakshatra_index = int(moon_sidereal / 13.333333)
   # 226° ÷ 13.333° = 16.95 → index 16 (Anuradha) or 17 (Jyeshtha)
   # Need exact degrees to determine
   ```

**For Jyeshtha (index 17):**
- Range: 226°40' – 240°00' (sidereal)
- If moon_sidereal ≥ 226.667° → Jyeshtha ✓

## Implementation Notes

**Using Swiss Ephemeris in Babashka/Clojure:**

```clojure
(ns graintime.swiss-ephemeris
  "Swiss Ephemeris integration for accurate nakshatra calculations"
  (:import [com.github.krymlov.swe SwissEph]))

(defn get-moon-nakshatra
  "Calculate moon nakshatra using Swiss Ephemeris"
  [datetime latitude longitude]
  (let [;; Calculate Julian Day
        jd (calculate-julian-day datetime)
        
        ;; Get tropical moon position
        moon-tropical (get-tropical-moon-position jd)
        
        ;; Get Lahiri ayanamsa
        ayanamsa (get-lahiri-ayanamsa jd)
        
        ;; Convert to sidereal
        moon-sidereal (- moon-tropical ayanamsa)
        
        ;; Calculate nakshatra index (0-26)
        nakshatra-index (int (/ moon-sidereal 13.333333))]
    
    {:nakshatra-index nakshatra-index
     :nakshatra-name (nth nakshatras nakshatra-index)
     :sidereal-longitude moon-sidereal
     :tropical-longitude moon-tropical
     :ayanamsa ayanamsa}))
```

## Why This Matters

**Verification Strategy:**
1. Use AstrOccult.net pre-calculated data (Oct 2023 - Nov 2025)
2. Verify Swiss Ephemeris matches AstrOccult results
3. Once verified, use Swiss Ephemeris for:
   - Dates outside AstrOccult range
   - Real-time calculations
   - Historical dates

**Benefits:**
- **No web scraping needed** for dates outside AstrOccult range
- **Accurate to the second** (Swiss Ephemeris precision)
- **Works offline** (no API dependency)
- **Future-proof** (works for any date)

## Resources

- Swiss Ephemeris: https://www.astro.com/swisseph/
- Lahiri Ayanamsa info: https://www.astro.com/swisseph/swisseph.htm#_Toc49847897
- Java wrapper: https://github.com/krymlov/swisseph
- Nakshatra degree ranges: https://www.jyotishtek.com/Constellations-Nakshatras.html

## Complete Workflow

### For Verification (compare with AstrOccult.net)

```
1. Get AstrOccult transition: "02/11/2025 17:03 IST → Uttara Bhadrapada"
2. Run Swiss Ephemeris (same IST time)
3. Expected: nakshatra_index = 25 (Uttara Bhadrapada)
4. If match → ✓ Formula verified
```

### For Production (any date, any location)

```
User: gt caspar structure (Caspar, CA = PST/PDT)
Current time: 2025-10-25 07:30 PDT

1. Swiss Ephemeris calculates (PDT timezone)
2. No IST conversion needed (Swiss Ephemeris handles it)
3. Returns nakshatra for that exact moment
4. Works for ANY date (past, present, future)
5. Works for ANY location (Kyoto, Barcelona, London, etc)
```

**Key insight:** Once we verify the formula with IST data, we can use Swiss Ephemeris for ANY timezone/date combo!

## Next Steps

### Phase 1: Verification
1. Add Swiss Ephemeris JAR to deps.edn
2. Implement `calculate-julian-day` function
3. Implement `get-tropical-moon-position` function
4. Implement `get-lahiri-ayanamsa` function
5. Test against Astromitra test case (Jyeshtha at IST time)
6. Verify against AstrOccult.net data (multiple IST timestamps)

### Phase 2: Production
7. Once verified, use for all graintime calculations
8. No more dependency on AstrOccult.net data
9. Works offline
10. Accurate for any date/location

---

**Status:** Research complete. Formula documented. Verification strategy clear.

**The beauty:** AstrOccult gives us 2 years of test data to verify our formula. Once verified, Swiss Ephemeris works for ANY date!

now == next + 1 🌾

