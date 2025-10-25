# AstrOccult.net Moon Nakshatra Data Parsing

## Source Data Format

From: https://www.astroccult.net/transit_of_planets_planetary_events.html

### Example Raw Data (October 2023):

```
Date Time (IST) Sign / Nakshatra 

Moon Rohini 03/10/2023 18:03
Moon Mrigashirsha 04/10/2023 18:29
Moon Gemini 05/10/2023 06:58
Moon Ardra 05/10/2023 19:39
Moon Punarvasu 06/10/2023 21:31
Moon Cancer 07/10/2023 17:17
Moon Pushya 07/10/2023 23:56
Moon Ashlesha 09/10/2023 02:44
Moon Magha 10/10/2023 05:44
Moon Leo 10/10/2023 05:44
Moon Purva Phalgu 11/10/2023 08:44
Moon Uttara Phalg 12/10/2023 11:36
Moon Virgo 12/10/2023 18:16
Moon Hasta 13/10/2023 14:10
Moon Chitra 14/10/2023 16:23
Moon Libra 15/10/2023 05:21
Moon Swati 15/10/2023 18:12
Moon Vishakha 16/10/2023 19:34
Moon Scorpio 17/10/2023 14:19
Moon Anuradha 17/10/2023 20:30
Moon Jyeshtha 18/10/2023 21:00
Moon Mula 19/10/2023 21:03
Moon Sagittarius 19/10/2023 21:03
Moon Purva Ashadh 20/10/2023 20:40
Moon Uttara Ashad 21/10/2023 19:53
Moon Capricorn 22/10/2023 01:38
Moon Shravana 22/10/2023 18:43
Moon Dhanishtha 23/10/2023 17:14
Moon Aquarius 24/10/2023 04:22
Moon Satabhisha 24/10/2023 15:28
Moon Purva Bhadra 25/10/2023 16:59
Moon Uttara Bhadr 26/10/2023 18:47
```

## Parsing Strategy

### 1. Filter Moon Nakshatra Lines Only

**IGNORE**:
- "Moon Gemini" (zodiac sign, not nakshatra)
- "Moon Cancer" (zodiac sign)
- "Moon Leo" (zodiac sign)
- etc.

**KEEP ONLY**:
- Lines with nakshatra names (Rohini, Mrigashirsha, Ardra, etc.)
- **27 nakshatras total**

### 2. Nakshatra Names to Filter For

```clojure
(def moon-nakshatra-names
  #{"Rohini" "Mrigashirsha" "Ardra" "Punarvasu" "Pushya" "Ashlesha"
    "Magha" "Purva Phalgu" "Uttara Phalg" "Hasta" "Chitra" "Swati"
    "Vishakha" "Anuradha" "Jyeshtha" "Mula" "Purva Ashadh" "Uttara Ashad"
    "Shravana" "Dhanishtha" "Satabhisha" "Purva Bhadra" "Uttara Bhadr"
    "Revati" "Ashwini" "Bharani" "Krittika"})
```

### 3. Parse Each Line

Format: `Moon {Nakshatra} {DD/MM/YYYY} {HH:MM}`

Example:
```
Moon Jyeshtha 18/10/2023 21:00
  ↓
{:nakshatra "Jyeshtha"
 :date "18/10/2023"
 :time "21:00"
 :ist-datetime #inst "2023-10-18T21:00:00+05:30"}
```

### 4. Date Range Coverage

Looking at the full AstrOccult dataset:

**OLDEST DATE**: 03/10/2023 (October 3, 2023)
**LATEST DATE**: Need to check the end of their dataset

The site shows data for:
- **2023**: October, November, December
- **2024**: Full year (January - December)
- **2025**: Full year (January - December)
- **2026**: Partial year visible

**Estimated coverage**: **October 2023 to December 2026** (may extend further)

### 5. Example Parsed Output (EDN)

```clojure
{:coverage {:start "2023-10-03"
            :end "2026-12-31"}
 :timezone "Asia/Kolkata" ; IST = UTC+5:30
 :transitions
 [{:nakshatra "Rohini"
   :date "03/10/2023"
   :time "18:03"
   :ist-datetime #inst "2023-10-03T18:03:00+05:30"}
  {:nakshatra "Mrigashirsha"
   :date "04/10/2023"
   :time "18:29"
   :ist-datetime #inst "2023-10-04T18:29:00+05:30"}
  {:nakshatra "Ardra"
   :date "05/10/2023"
   :time "19:39"
   :ist-datetime #inst "2023-10-05T19:39:00+05:30"}
  ;; ... 800+ more transitions
  ]}
```

## Why This Works Better Than Swiss Ephemeris

1. **Pre-calculated** - No complex astronomy math needed
2. **Fast lookups** - Binary search through sorted transitions
3. **Offline-friendly** - Download once, use forever
4. **Timezone conversion** - Simple IST → local timezone math
5. **Verified data** - AstrOccult.net is trusted source

## Implementation Plan

```clojure
(defn parse-astroccult-html
  "Parse AstrOccult HTML to extract Moon nakshatra transitions"
  [html]
  (->> html
       ;; Extract lines starting with "Moon"
       (re-seq #"Moon\s+(\w+)\s+(\d{2}/\d{2}/\d{4})\s+(\d{2}:\d{2})")
       ;; Filter for nakshatra lines (not zodiac signs)
       (filter (fn [[_ nakshatra _ _]]
                 (contains? moon-nakshatra-names nakshatra)))
       ;; Parse into structured data
       (map (fn [[_ nakshatra date time]]
              {:nakshatra nakshatra
               :date date
               :time time
               :ist-datetime (parse-ist-datetime date time)}))
       ;; Sort by datetime
       (sort-by :ist-datetime)))
```

## Next Steps

1. Download full HTML from AstrOccult.net
2. Parse all Moon nakshatra transitions (2023-2026)
3. Save to `resources/nakshatra-transitions.edn`
4. Implement binary search for fast lookups
5. Add IST → timezone conversion
6. Test with known values (e.g., Jyeshtha on Oct 25, 2025)

---

**Status**: Parser design complete. Ready to implement HTML extraction.

now == next + 1 🌾

