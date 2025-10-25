# TODO: Get Real October 2025 Moon Data

## Current Status

✅ **AstrOccult parser WORKING!**
- Successfully loads EDN data
- Parses IST datetime format
- Binary search implemented
- Returns nakshatra name

**Test result:** Returns "Punarvasu" (from placeholder 2023 data)

## What We Need

**Real Moon nakshatra transitions for:**
- October 2025
- November 2025
- Complete 2024 (if available)
- Complete 2025 (if available)

**Source:** https://www.astroccult.net/transit_of_planets_planetary_events.html

## Data Format

From AstrOccult.net (IST-based):

```
Date Time (IST) Sign / Nakshatra

Moon Rohini 03/10/2023 18:03
Moon Mrigashirsha 04/10/2023 18:29
Moon Gemini 05/10/2023 06:58  ; <-- Skip sign-only entries
Moon Ardra 05/10/2023 19:39
Moon Punarvasu 06/10/2023 21:31
...
```

**Filter rule:** Only keep entries with **nakshatra names**, skip zodiac sign entries.

## How to Parse

1. Visit https://www.astroccult.net/transit_of_planets_planetary_events.html
2. Select "October 2025" from dropdown
3. Copy the "Transit (Entry) of Moon In Signs and Nakshatras" section
4. Filter for nakshatra-only entries
5. Format as EDN:

```clojure
["DD/MM/YYYY" "HH:MM" "Nakshatra Name"]
```

## Manual Extraction Example

From website:
```
Moon Jyeshtha 18/10/2025 21:00
```

To EDN:
```clojure
["18/10/2025" "21:00" "Jyeshtha"]
```

## Verification

Once we have real October 2025 data, test with:

```bash
bb -cp src -e '(require '"'"'graintime.astroccult-parser) 
               (graintime.astroccult-parser/get-current-nakshatra)'
```

Should return current nakshatra matching Astromitra.com.

## Known Test Case

**October 25, 2025, 07:21 PDT (19:51 IST):**
- Expected: **Jyeshtha** (from Astromitra.com)
- Our parser will find this if we have the Oct 2025 transition data

## Last Known Moon Transition

From user: **02/11/2025 17:03 IST → Uttara Bhadrapada**

This is the endpoint of available AstrOccult data.

---

**Status:** Parser ready. Waiting for real October 2025 Moon data.

**Action needed:** Manual extraction from AstrOccult.net or HTML parsing implementation.

now == next + 1 🌾

