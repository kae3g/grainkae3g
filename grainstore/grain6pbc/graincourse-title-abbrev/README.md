# Graincourse Title Abbreviation

**Purpose**: Intelligently abbreviate course titles to keep grainpaths under 100 characters.

## Overview

The `graincourse-title-abbrev` module provides utilities for shortening course titles while preserving readability and semantic meaning. This is essential for maintaining the 100-character grainpath limit when combined with the 80-character graintime limit.

## Grainpath Structure

```
/course/{username}/{course-title}/{graintime}/
```

### Character Budget

- **Total limit**: 100 characters
- **Fixed overhead**: `/course/kae3g//` = 16 chars (with 5-char username)
- **Graintime**: max 80 chars
- **Course title budget**: 100 - 16 - 80 - 2 (slashes) = **2 chars minimum**

This means course titles can be **very short** in worst-case scenarios, but typically we have more room:
- With typical graintime (65-70 chars): 14-19 chars for course title
- With shortest graintime (60 chars): 24 chars for course title

## Abbreviation Strategy

The module uses a multi-tier approach to abbreviate course titles:

### Tier 1: Common Word Abbreviations

Remove or abbreviate common filler words:
- `introduction-to` → `intro`
- `fundamentals-of` → `fund`
- `advanced` → `adv`
- `programming` → `prog`
- `functional` → `func`
- `object-oriented` → `oo`
- `systems` → `sys`
- `architecture` → `arch`
- `development` → `dev`
- `engineering` → `eng`

### Tier 2: Vowel Removal

Remove vowels from longer words (keeping first letter):
- `network` → `ntwrk`
- `database` → `dtbs`
- `algorithm` → `lgrthm`

### Tier 3: Intelligent Truncation

Truncate to fit within budget while:
- Preserving word boundaries
- Keeping first letters of words
- Using hyphens to maintain readability

### Tier 4: Acronym Fallback

Convert to acronym if all else fails:
- `introduction-to-functional-programming` → `itfp`

## Examples

```clojure
;; Example abbreviations
(abbreviate-course-title "introduction-to-functional-programming" 20)
;; => "intro-func-prog"

(abbreviate-course-title "advanced-type-systems-and-category-theory" 15)
;; => "adv-type-sys"

(abbreviate-course-title "introduction-to-functional-programming-and-type-systems" 10)
;; => "intro-fp"

(abbreviate-course-title "grain-network-fundamentals" 15)
;; => "grain-net-fund"
```

## Usage

```clojure
(require '[graincourse-title-abbrev.core :as abbrev])

;; Calculate available space for course title
(def available-chars (abbrev/calculate-title-budget "kae3g" graintime-string))
;; => 14

;; Abbreviate title to fit
(def short-title (abbrev/abbreviate-course-title 
                   "introduction-to-functional-programming"
                   available-chars))
;; => "intro-func-prg"

;; Validate grainpath fits
(abbrev/validate-grainpath "/course/kae3g/intro-func-prg/12025-10-23--0048--PDT--moon-vishakha--asc-gem000--sun-4th--kae3g/")
;; => {:valid true, :length 95, :budget-remaining 5}
```

## Integration with Graincourse

The `graincourse` module automatically uses this library when generating grainpaths:

```bash
# Generates grainpath and auto-abbreviates title if needed
gt grainpath course kae3g "introduction-to-functional-programming-and-type-systems"
```

Output:
```
✨ Grainpath:
/course/kae3g/intro-func-prog/12025-10-23--0048--PDT--moon-vishakha--asc-gem000--sun-4th--kae3g/

📏 Length: 95/100 chars
⚠️  Course title abbreviated: intro-func-prog (from 57 chars)
```

## See Also

- [GRAINTIME.md](../../graintime/GRAINTIME.md) - Graintime specification
- [NAKSHATRA-ABBREVIATIONS.md](../../graintime/NAKSHATRA-ABBREVIATIONS.md) - Nakshatra abbreviations
- [graincourse-sync](../graincourse-sync/) - Immutable grainpath deployment

