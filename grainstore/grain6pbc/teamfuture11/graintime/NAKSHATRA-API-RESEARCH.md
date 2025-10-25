# Nakshatra API Research - Finding Reliable Free Sources

## The Problem
We need accurate sidereal nakshatra calculations based on:
- Date and time
- Geographic location (lat/lon)
- Timezone
- Ayanamsa (sidereal offset)

Current implementation uses mock data or web scraping, which is unreliable.

## Search Results (October 25, 2025)

### Web Calculators Found (NOT APIs)
- **AstroSage**: https://www.astrosage.com/nakshatra-calculator.asp
- **AstroCamp**: https://www.astrocamp.com/nakshatra-finder-birth-star-calculator.asp
- **Astro-Seek**: https://horoscopes.astro-seek.com/nakshatra-vedic-astrology-online-calculator
- **Prokerala**: https://www.prokerala.com/astrology/nakshatra-finder/

These are web forms, not programmatic APIs.

### Commercial APIs (with Free Tiers)
Search for RapidAPI, Aztro, or similar platforms didn't yield specific free nakshatra APIs.

### The Best Solution: Swiss Ephemeris (pyswisseph)

**Why Swiss Ephemeris:**
- Industry standard for astronomical calculations
- Extremely accurate (used by professional astronomers and astrologers)
- FREE and open source
- Available as Python library (pyswisseph)
- Can calculate moon position in sidereal zodiac
- Supports all major ayanamsas (Lahiri, Krishnamurti, etc.)

**How to Calculate Nakshatra from Swiss Ephemeris:**

1. Get Moon's sidereal longitude (0-360°)
2. Divide by 13.333... (13°20' per nakshatra)
3. Floor value gives nakshatra index (0-26)

```python
import swisseph as swe

# Set sidereal mode with Lahiri ayanamsa (standard for Vedic astrology)
swe.set_sid_mode(swe.SIDM_LAHIRI)

# Calculate Julian Day from datetime
jd = swe.julday(year, month, day, hour + minute/60.0)

# Get Moon position in sidereal zodiac
moon_pos = swe.calc_ut(jd, swe.MOON, swe.FLG_SIDEREAL)[0]

# Calculate nakshatra (0-26)
nakshatra_index = int(moon_pos / (360.0 / 27))
```

**Nakshatra Names (0-26):**
```
0  - Ashwini
1  - Bharani
2  - Krittika
3  - Rohini
4  - Mrigashira
5  - Ardra
6  - Punarvasu
7  - Pushya
8  - Ashlesha
9  - Magha
10 - Purva Phalguni
11 - Uttara Phalguni
12 - Hasta
13 - Chitra
14 - Swati
15 - Vishakha
16 - Anuradha
17 - Jyeshtha
18 - Mula
19 - Purva Ashadha
20 - Uttara Ashadha
21 - Shravana
22 - Dhanishta
23 - Shatabhisha
24 - Purva Bhadrapada
25 - Uttara Bhadrapada
26 - Revati
```

## Implementation Options

### Option 1: Self-Hosted pyswisseph Service
Create a simple Flask/FastAPI service that accepts:
```json
POST /nakshatra
{
  "datetime": "2025-10-25T00:53:00",
  "latitude": 37.9735,
  "longitude": -122.5311,
  "timezone": "America/Los_Angeles"
}
```

Returns:
```json
{
  "nakshatra": "Vishakha",
  "nakshatra_index": 15,
  "moon_longitude": 206.5,
  "ayanamsa": "Lahiri"
}
```

**Pros:**
- Complete control
- No rate limits
- Can customize for our needs
- Could host on our own infrastructure

**Cons:**
- Needs server infrastructure
- Maintenance burden
- Need to install ephemeris data files

### Option 2: Babashka + Java Swiss Ephemeris
Use swisseph Java library directly from Babashka/Clojure:
- Add swisseph JAR to dependencies
- Call directly from Clojure code
- No external API needed
- Runs locally

**Pros:**
- No external dependencies
- Fast (local calculation)
- Always available
- No network calls

**Cons:**
- Need to include ephemeris data files (~20MB)
- Java interop complexity
- Requires swisseph JAR

### Option 3: Command-Line swetest (Swiss Ephemeris CLI)
Swiss Ephemeris includes a command-line tool `swetest`:

```bash
swetest -p2 -b25.10.2025 -ut0:53 -sid1 -fPZ
```

**Pros:**
- Simple to install (apt-get install swisseph on Ubuntu)
- Can call from Babashka via shell
- No JVM dependencies

**Cons:**
- Parsing text output
- Shell dependency
- Less elegant

## Recommendation: Option 2 (Babashka + Java Swiss Ephemeris)

**Why:**
- Pure Clojure solution
- No external API or server needed
- Fast local calculations
- Reliable and accurate
- Self-contained within our graintime module

**Next Steps:**
1. Add `com.github.rahulg0212/swisseph` to deps.edn
2. Include ephemeris data files in graintime/resources/
3. Create graintime/swiss.clj namespace
4. Implement nakshatra calculation function
5. Replace astromitra.clj mock data with real calculations
6. Test with known values

## Alternative: Offline Fallback Strategy

If we want to avoid the Swiss Ephemeris dependency initially, we could:

1. Pre-calculate nakshatra transitions for 2025-2030
2. Store as EDN lookup table
3. Binary search to find current nakshatra
4. Fall back to Swiss Ephemeris for dates outside range

This gives us:
- Fast lookups (no calculation)
- No external dependencies initially
- Gradual migration path

## Testing Strategy

Test against known values from:
- Astro-Seek.com (trusted calculator)
- AstroSage.com (popular Indian astrology site)
- Manual calculation for specific dates

Example test cases:
- October 25, 2025, 00:53 PDT, San Rafael CA → Vishakha
- [Add more known values]

## Resources

- Swiss Ephemeris: https://www.astro.com/swisseph/
- pyswisseph: https://github.com/astrorigin/pyswisseph
- Java wrapper: https://github.com/rahulyhg/swisseph
- Ayanamsa info: https://www.astro.com/swisseph/swisseph.htm#_Toc49847897

---

**Status:** Research complete. Recommend implementing Option 2 (Babashka + Java Swiss Ephemeris) for accurate, self-contained nakshatra calculations.

now == next + 1 🌾

