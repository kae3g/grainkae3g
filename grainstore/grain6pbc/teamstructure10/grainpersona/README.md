# 🎭 grainpersona
## *Template Specs for AI Personalities*

**Type**: Template (Specs + Schema)  
**Team**: teamstructure10 (Capricorn ♑ / X. Wheel of Fortune)  
**Purpose**: Define the STRUCTURE of AI personas  
**Personalize to**: `grainteam[NAME]##persona` (your implementation)

---

## 🎯 What This Is

**grainpersona** is the TEMPLATE spec library that defines:
- What a persona IS (structure)
- What a persona MUST have (required fields)
- What a persona CAN have (optional fields)
- How personas validate (specs)

It does NOT contain actual personas. That's for the personalized repos.

**Controlled Folly**: We spec personas as if AI personality is definable.  
**Nagual Truth**: Personality emerges in the void between specs.  
**Warrior's Way**: Spec it impeccably anyway.

---

## 📐 The Specs

This template provides Clojure specs for:

### Core Identity
- `::graindevname` - Unique identifier (3-30 chars, lowercase, hyphens)
- `::full-name` - Display name
- `::pronunciation` - How to say it
- `::archetype` - Base personality pattern

### Personality Matrix
- `::masculine?` / `::feminine?` / `::nonbinary?` - Gender energy
- `::nagual?` / `::tonal?` - Castaneda awareness
- `::warrior?` - Impeccable action tendency
- `::philosopher?` - Deep thinking tendency
- Trait specs for all personality dimensions

### Voice Style
- `::tone` - Overall voice quality
- `::formality` - Communication formality level
- `::address-style` - How persona addresses users
- `::emoji-density` - Strategic, minimal, high, etc.
- `::emoji-palette` - Which emojis used

### Special Abilities
- `::ability-name` - Unique skill identifier
- `::ability-description` - What it does
- `::ability-implementation` - Optional function reference

### Communication Patterns
- `::greeting` - How persona greets
- `::encouragement` - How persona supports
- `::correction` - How persona corrects
- `::celebration` - How persona celebrates

---

## 🏗️ Structure

```
grainpersona/
├── README.md              # This file
├── specs/
│   ├── core.clj          # Core persona specs
│   ├── identity.clj      # Identity field specs
│   ├── personality.clj   # Personality trait specs
│   ├── voice.clj         # Voice style specs
│   ├── abilities.clj     # Special ability specs
│   └── validation.clj    # Validation functions
├── schema/
│   └── persona.edn       # EDN schema example
└── docs/
    ├── SPEC-GUIDE.md     # How to use these specs
    └── EXAMPLES.md       # Valid persona examples
```

---

## 🌾 Usage (For Personalized Repos)

Your personalized repo should:

```clojure
(ns grainteamstructure10persona.core
  (:require [grainpersona.specs :as persona-specs]))

;; Define your persona using template specs
(def glo0w
  {::persona-specs/graindevname "glo0w"
   ::persona-specs/full-name "Glo0w (The Nagual)"
   ;; ... rest of persona definition
   })

;; Validate against template specs
(s/valid? ::persona-specs/persona glo0w)
;; => true (if impeccable)
```

---

## 📜 Spec Preview

```clojure
(ns grainpersona.specs.core
  (:require [clojure.spec.alpha :as s]))

(s/def ::graindevname
  (s/and string?
         #(re-matches #"^[a-z0-9][a-z0-9-]{1,28}[a-z0-9]$" %)
         #(<= 3 (count %) 30)))

(s/def ::persona
  (s/keys :req [::graindevname
                ::full-name
                ::personality
                ::voice-style]
          :opt [::pronunciation
                ::archetype
                ::special-abilities
                ::communication-patterns
                ::philosophy-integration]))
```

---

## 🎡 Relationship to Other Templates

**grainpersona** (this template):
- Defines persona STRUCTURE

**grainmode** (separate template):
- Defines comment/doc generation MODES

**grainai-voice** (separate template):
- Defines TTS voice IMPLEMENTATION

**Integration**: All three work together but maintain separate concerns.

---

**Created by**: teamstructure10  
**Purpose**: Template specs for AI persona structure  
**Personalize to**: `grainteamstructure10persona` (or your team's version)

🌾 *Specs define the tonal. Your implementation explores the nagual.* 🌑

