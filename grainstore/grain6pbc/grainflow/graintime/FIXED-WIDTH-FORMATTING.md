# Fixed-Width Graintime Formatting

**Purpose**: Perfect visual alignment of graintimes in monospace fonts for log files, dashboards, and documentation.

## Overview

All graintimes are formatted to exactly **70 characters** with fixed-width fields that align perfectly when stacked vertically.

## Field Structure

```
Position  Field              Width  Example
────────  ─────────────────  ─────  ──────────────
0-10      Date (Holocene)    11     12025-10-23
11-12     Separator          2      --
13-16     Time (HHMM)        4      0119
17-18     Separator          2      --
19-21     Timezone           3      PDT
22-23     Separator          2      --
24-36     Moon nakshatra     13     moon-vishakha------
37-38     Separator          2      --
39-49     Ascendant          11     asc-gem000
50-51     Separator          2      --
52-60     Sun house          9      sun-04th
61-62     Separator          2      --
63-67     Author (devname)   5      kae3g
────────────────────────────────────────────────────
TOTAL                        70 chars
```

## Fixed-Width Components

### 1. Nakshatra (12 characters)

All nakshatras are padded to exactly 12 characters using dashes:

```
mula----------  (4 chars + 10 dashes)
vishakha------  (8 chars + 6 dashes)
u_bhadrapada--  (12 chars + 2 dashes)
```

**Abbreviations**:
- Purva → `p_` (e.g., `p_phalguni----`)
- Uttara → `u_` (e.g., `u_bhadrapada--`)

### 2. House (4 characters)

All houses use 2-digit format with ordinal suffix:

```
01st  (1st house)
02nd  (2nd house)
03rd  (3rd house)
04th  (4th house)
...
12th  (12th house)
```

### 3. Zodiac (3 characters)

All zodiac signs use 3-letter abbreviations:

```
ari  (Aries)
tau  (Taurus)
gem  (Gemini)
can  (Cancer)
leo  (Leo)
vir  (Virgo)
lib  (Libra)
sco  (Scorpio)
sag  (Sagittarius)
cap  (Capricorn)
aqu  (Aquarius)
pis  (Pisces)
```

## Visual Stacking Examples

### Example 1: Different Nakshatras
```
12025-10-23--0119--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g
12025-10-23--1234--PDT--moon-u_bhadrapada--asc-gem000--sun-04th--kae3g
12025-10-23--0800--PDT--moon-mula----------asc-gem000--sun-04th--kae3g
12025-10-23--1500--PDT--moon-p_phalguni----asc-gem000--sun-04th--kae3g
12025-10-23--2100--PDT--moon-ashwini-------asc-gem000--sun-04th--kae3g
```

### Example 2: Different Houses
```
12025-10-23--0119--PDT--moon-vishakha------asc-gem000--sun-01st--kae3g
12025-10-23--0119--PDT--moon-vishakha------asc-gem000--sun-02nd--kae3g
12025-10-23--0119--PDT--moon-vishakha------asc-gem000--sun-03rd--kae3g
12025-10-23--0119--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g
12025-10-23--0119--PDT--moon-vishakha------asc-gem000--sun-10th--kae3g
12025-10-23--0119--PDT--moon-vishakha------asc-gem000--sun-12th--kae3g
```

### Example 3: Different Zodiac Signs
```
12025-10-23--0119--PDT--moon-vishakha------asc-ari000--sun-04th--kae3g
12025-10-23--0119--PDT--moon-vishakha------asc-tau000--sun-04th--kae3g
12025-10-23--0119--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g
12025-10-23--0119--PDT--moon-vishakha------asc-sag000--sun-04th--kae3g
12025-10-23--0119--PDT--moon-vishakha------asc-cap000--sun-04th--kae3g
```

## Test Coverage

### Comprehensive Testing
- **Total combinations**: 3,888 (27 nakshatras × 12 zodiacs × 12 houses)
- **All combinations**: Exactly 70 characters
- **Pass rate**: 100%
- **Min/Max length**: 70 chars (no variation)

### Test Categories
1. **Nakshatra padding** - All 27 nakshatras pad to 12 chars
2. **House formatting** - All 12 houses format with 2 digits
3. **Length validation** - All combinations stay under 80 chars
4. **Field alignment** - All fields align at same positions
5. **Visual stacking** - Perfect monospace alignment

Run tests:
```bash
cd grainstore/graintime
bb test-formatting
```

## Benefits

### 1. Visual Clarity
When viewing multiple graintimes in logs or dashboards, the fixed-width formatting creates a clean, table-like appearance:

```
12025-10-23--0100--PDT--moon-ashwini-------asc-ari000--sun-01st--kae3g
12025-10-23--0200--PDT--moon-bharani-------asc-tau045--sun-02nd--kae3g
12025-10-23--0300--PDT--moon-krittika------asc-gem090--sun-03rd--kae3g
12025-10-23--0400--PDT--moon-rohini--------asc-can135--sun-04th--kae3g
```

### 2. Easy Scanning
Fields align vertically, making it easy to:
- Scan dates (left column)
- Compare times
- Identify patterns in nakshatras
- Track house progressions

### 3. Grep-Friendly
Fixed positions make regex patterns more reliable:

```bash
# Extract all moon nakshatras (always at position 24)
grep -oP 'moon-\K[^-]+' graintime.log

# Extract all houses (always at position 52)
grep -oP 'sun-\K\d+\w+' graintime.log
```

### 4. Database-Ready
Fixed-width format is ideal for:
- Column-oriented storage
- CSV exports
- Database imports
- Data analysis

## Character Budget

With exactly 70 characters per graintime:
- Grainpath budget increases: 100 - 16 - 70 - 2 = **12 chars for course title**
- This works perfectly with our course title abbreviation module
- Typical grainpath: 84-90 chars (well under 100 limit)

## See Also

- [NAKSHATRA-ABBREVIATIONS.md](./NAKSHATRA-ABBREVIATIONS.md) - Nakshatra abbreviation reference
- [GRAINTIME.md](./GRAINTIME.md) - Full graintime specification
- [test/graintime/formatting_test.clj](./test/graintime/formatting_test.clj) - Comprehensive test suite

