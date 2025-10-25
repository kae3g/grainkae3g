# Nakshatra Abbreviations for Graintime

## Purpose

To keep graintime strings under the 80-character limit while preserving astronomical accuracy, we use strategic abbreviations for nakshatra names.

## Abbreviation Rules

### Purva Nakshatras (p_ prefix)

All nakshatras starting with "Purva" are abbreviated with the `p_` prefix:

- `Purva Phalguni` → `p_phalguni` (saves 5 chars)
- `Purva Ashadha` → `p_ashadha` (saves 5 chars)  
- `Purva Bhadrapada` → `p_bhadrapada` (saves 5 chars)

### Uttara Nakshatras (u_ prefix)

All nakshatras starting with "Uttara" are abbreviated with the `u_` prefix:

- `Uttara Phalguni` → `u_phalguni` (saves 6 chars)
- `Uttara Ashadha` → `u_ashadha` (saves 6 chars)
- `Uttara Bhadrapada` → `u_bhadrapada` (saves 6 chars)

### Other Nakshatras

All other nakshatras use lowercase with hyphens replacing spaces:

- `Vishakha` → `vishakha`
- `Jyeshtha` → `jyeshtha`
- `Shatabhisha` → `shatabhisha`
- `Dhanishta` → `dhanishta`

## Complete Nakshatra List

The 27 nakshatras with their abbreviations:

1. `Ashwini` → `ashwini`
2. `Bharani` → `bharani`
3. `Krittika` → `krittika`
4. `Rohini` → `rohini`
5. `Mrigashira` → `mrigashira`
6. `Ardra` → `ardra`
7. `Punarvasu` → `punarvasu`
8. `Pushya` → `pushya`
9. `Ashlesha` → `ashlesha`
10. `Magha` → `magha`
11. `Purva Phalguni` → `p_phalguni`
12. `Uttara Phalguni` → `u_phalguni`
13. `Hasta` → `hasta`
14. `Chitra` → `chitra`
15. `Swati` → `swati`
16. `Vishakha` → `vishakha`
17. `Anuradha` → `anuradha`
18. `Jyeshtha` → `jyeshtha`
19. `Mula` → `mula`
20. `Purva Ashadha` → `p_ashadha`
21. `Uttara Ashadha` → `u_ashadha`
22. `Shravana` → `shravana`
23. `Dhanishta` → `dhanishta`
24. `Shatabhisha` → `shatabhisha`
25. `Purva Bhadrapada` → `p_bhadrapada`
26. `Uttara Bhadrapada` → `u_bhadrapada`
27. `Revati` → `revati`

## Implementation

The abbreviation logic is implemented in:
- `/grainstore/graintime/src/graintime/core.clj` - `abbreviate-nakshatra` function
- `/grainstore/graintime/src/graintime/astromitra.clj` - used in `get-accurate-graintime`

## Example Graintimes

```
12025-10-23--0045--PDT--moon-vishakha--asc-gemini000--sun-4th--kae3g
12025-11-15--1430--PST--moon-p_bhadrapada--asc-leo123--sun-10th--kae3g
12025-12-25--0800--PST--moon-u_ashadha--asc-aries045--sun-1st--kae3g
```

## Character Savings

The abbreviation system saves significant space:
- Purva nakshatras: 5 characters each
- Uttara nakshatras: 6 characters each
- Maximum graintime length tested: 68/80 chars (with `vishakha`)
- Typical grainpath length: ~95/100 chars (with course name truncation)

## See Also

- [GRAINTIME.md](./GRAINTIME.md) - Full graintime specification
- [SOLAR-HOUSES.md](./SOLAR-HOUSES.md) - Solar house system documentation

