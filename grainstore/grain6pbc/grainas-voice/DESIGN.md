# 🌾 Grainas-Voice Design Document 🌾

**Grain Sheaf:** `grainas-voice` (synonyms: `grainai-persona`, `grainai-character`)  
**Type:** Template/Personal Separated  
**Module:** grain6pbc (Public Benefit Corporation)  
**OS Target:** 6os (SixOS)  
**Philosophy:** Local Control, Global Intent • Template/Personal Everywhere

---

## 🎯 Purpose

**Grainas-voice** is a framework for creating AI voice personas in the Grain Network ecosystem. It provides:

1. **Template System** - Base persona templates for different AI personalities
2. **Personal Customization** - User-specific persona configurations  
3. **Voice Characteristics** - Tone, style, wit level, formality, emoji usage
4. **6os Integration** - Native integration with grain6 process supervision
5. **Multi-Persona Support** - Multiple AI characters in one system

---

## 🌸 Example Personas

### tri5h (Nutrition - Implemented!)
```clojure
{:graindevname "tri5h"
 :full-name "Trish"
 :pronunciation "nutrition (nu-TRI-5H-tion)"
 :personality {:feminine true
               :witty true
               :constructive-critical true
               :teasing true
               :rebel true
               :artsy true
               :flowery true}
 :voice-style {:tone "warm and supportive"
               :formality "casual"
               :emoji-density "high (💐🌱🌸)"
               :humor "corny dad-jokes meet zen poetry"
               :language "vegan nutrition metaphors"}
 :special-abilities ["vegan-basho-haiku" "nutrition-advice" "compassionate-accountability"]
 :signature-phrases ["now == next + 1 (but like, make it fashion)"
                     "xoxo, Trish 💖"
                     "Babe. Honey. Light of my processing unit."]
 :command "bb plz"}
```

### kae3g (Developer - Aspirational)
```clojure
{:graindevname "kae3g"
 :full-name "Kae"
 :pronunciation "K-3-G"
 :personality {:technical true
               :philosophical true
               :calm true
               :observant true
               :precise true}
 :voice-style {:tone "zen-like clarity"
               :formality "balanced"
               :emoji-density "low (🌾)"
               :humor "dry wit, temporal recursion jokes"
               :language "grain network philosophy"}
 :special-abilities ["temporal-recursion-explanations" "88-counter-philosophy" "system-architecture"]
 :signature-phrases ["now == next + 1"
                     "From granules to grains to THE WHOLE GRAIN"
                     "Local control, global intent"]
 :command "bb kae"}
```

### gr41n (Hacker - Aspirational)
```clojure
{:graindevname "gr41n"
 :full-name "Grain"
 :pronunciation "grain (but 1337)"
 :personality {:hacker true
               :efficient true
               :minimalist true
               :punk true}
 :voice-style {:tone "terse efficiency"
               :formality "very-casual"
               :emoji-density "minimal (🌾)"
               :humor "hacker humor, grep jokes"
               :language "command-line terseness"}
 :special-abilities ["quick-fixes" "grep-wizardry" "one-liner-solutions"]
 :signature-phrases ["RTFM (Read The Friendly Manual)"
                     "works on my machine 🌾"
                     "grep it or regret it"]
 :command "bb hack"}
```

---

## 📁 Directory Structure

```
grainstore/grain6pbc/grainas-voice/
├── README.md                           # Main documentation
├── DESIGN.md                           # This file
├── LICENSE                             # MIT License
├── bb.edn                              # Babashka task configuration
├── project.clj                         # Leiningen project file
├── default.nix                         # Nix derivation
│
├── template/                           # TEMPLATE SIDE
│   ├── personas/                       # Base persona templates
│   │   ├── tri5h.edn                   # Trish nutrition template
│   │   ├── kae3g.edn                   # Kae developer template
│   │   ├── gr41n.edn                   # Grain hacker template
│   │   ├── base.edn                    # Base persona template
│   │   └── schema.clj                  # Clojure spec for personas
│   │
│   ├── voice-styles/                   # Voice style templates
│   │   ├── feminine-flowery.edn
│   │   ├── zen-philosophical.edn
│   │   ├── hacker-terse.edn
│   │   └── base-style.edn
│   │
│   ├── special-abilities/              # Ability templates
│   │   ├── vegan-haiku.clj
│   │   ├── temporal-recursion.clj
│   │   ├── grep-wizardry.clj
│   │   └── base-ability.clj
│   │
│   └── scripts/                        # Template scripts
│       ├── persona-generator.bb        # Generate new persona
│       ├── voice-mixer.bb              # Mix voice styles
│       └── ability-composer.bb         # Compose abilities
│
├── personal/                           # PERSONAL SIDE
│   ├── .gitignore                      # Ignore personal configs
│   ├── personas/                       # Personal persona configs
│   │   ├── my-tri5h.edn                # Personalized Trish
│   │   ├── my-kae3g.edn                # Personalized Kae
│   │   └── custom-persona.edn          # User custom persona
│   │
│   ├── voice-tweaks/                   # Personal voice adjustments
│   │   ├── emoji-preferences.edn
│   │   ├── humor-level.edn
│   │   └── formality-settings.edn
│   │
│   └── grainconfig.edn                 # Personal master config
│
├── src/                                # Source code
│   └── grainas/
│       ├── voice/
│       │   ├── core.clj                # Core voice system
│       │   ├── persona.clj             # Persona management
│       │   ├── generator.clj           # Response generation
│       │   └── mixer.clj               # Voice style mixing
│       │
│       └── ai/
│           ├── character.clj           # Character system
│           ├── personality.clj         # Personality traits
│           └── abilities.clj           # Special abilities
│
├── scripts/                            # Executable scripts
│   ├── plz-tri5h.bb                    # Trish command (current)
│   ├── kae-kae3g.bb                    # Kae command (future)
│   ├── hack-gr41n.bb                   # Grain hacker command (future)
│   └── persona-runner.bb               # Universal persona runner
│
├── docs/                               # Documentation
│   ├── CREATING-PERSONAS.md            # How to create new personas
│   ├── VOICE-STYLES.md                 # Voice style guide
│   ├── SPECIAL-ABILITIES.md            # Ability system docs
│   ├── INTEGRATION.md                  # 6os integration guide
│   └── EXAMPLES.md                     # Example configurations
│
└── test/                               # Tests
    └── grainas/
        ├── voice_test.clj
        ├── persona_test.clj
        └── generator_test.clj
```

---

## 🌾 Core Concepts

### 1. Persona (grainai-persona)

A **persona** is a complete AI character with:
- Identity (graindevname, name, pronunciation)
- Personality traits (witty, serious, playful, etc.)
- Voice style (tone, formality, emoji usage)
- Special abilities (haiku, grep wizardry, etc.)
- Signature phrases
- Command binding

### 2. Voice (grainas-voice)

A **voice** is the speaking style of a persona:
- Tone (warm, terse, zen-like)
- Formality level (casual, formal, very-casual)
- Emoji density (high, low, minimal)
- Humor type (dad jokes, dry wit, hacker humor)
- Language patterns (metaphors, technical, poetic)

### 3. Character (grainai-character)

A **character** is the behavioral system:
- Personality matrix (traits and weights)
- Response patterns (greetings, tips, sass)
- Context awareness (TODO count, git status)
- Mood variations (based on system state)
- Learning (optional, personal side only)

### 4. Abilities (Special Powers)

**Abilities** are unique skills each persona can have:
- Vegan Basho haiku (tri5h)
- Temporal recursion explanations (kae3g)
- One-liner grep solutions (gr41n)
- Custom user-defined abilities

---

## 🔧 Technical Architecture

### Template/Personal Separation

```
TEMPLATE SIDE (version controlled, shared):
  • Base persona definitions
  • Standard voice styles
  • Core abilities
  • Schema and specs
  • Documentation

PERSONAL SIDE (gitignored, local):
  • User-specific tweaks
  • Custom personas
  • Privacy settings
  • Personal preferences
  • API keys (if needed)
```

### Clojure Spec Integration

```clojure
(ns grainas.voice.schema
  (:require [clojure.spec.alpha :as s]))

;; Graindevname spec (from ascii_art_specs.clj)
(s/def ::graindevname
  (s/and string?
         #(re-matches #"^[a-z0-9-]{3,30}$" %)
         #(not (str/starts-with? % "-"))
         #(not (str/ends-with? % "-"))))

;; Personality traits
(s/def ::personality-trait boolean?)
(s/def ::personality
  (s/keys :opt-un [::feminine ::witty ::constructive-critical
                   ::teasing ::rebel ::artsy ::flowery
                   ::technical ::philosophical ::calm]))

;; Voice style
(s/def ::tone string?)
(s/def ::formality #{:very-casual :casual :balanced :formal :very-formal})
(s/def ::emoji-density #{:minimal :low :medium :high :very-high})
(s/def ::voice-style
  (s/keys :req-un [::tone ::formality ::emoji-density]
          :opt-un [::humor ::language]))

;; Complete persona spec
(s/def ::persona
  (s/keys :req-un [::graindevname ::full-name ::personality ::voice-style]
          :opt-un [::pronunciation ::special-abilities ::signature-phrases ::command]))
```

### 6os Integration

```clojure
;; grain6 service definition for personas
(def tri5h-service
  {:service-name "grainas-tri5h"
   :command "bb plz"
   :type :oneshot           ; Run once per invocation
   :dependencies []
   :environment {:PERSONA "tri5h"
                 :CONFIG_PATH "personal/personas/my-tri5h.edn"}
   :restart-policy :never})

;; graintime integration
(defn graintime-aware-greeting [persona]
  (let [current-time (graintime/now)
        moon-phase (graintime/moon-phase current-time)]
    (generate-greeting persona {:time current-time
                                :moon moon-phase})))
```

---

## 🌸 Usage Examples

### Creating a New Persona

```bash
# Generate from template
bb persona new "dev5h" --template kae3g --voice zen-philosophical

# Customize
editor personal/personas/dev5h.edn

# Test
bb persona test dev5h

# Deploy
bb persona deploy dev5h
```

### Mixing Voice Styles

```bash
# Mix tri5h (flowery) with gr41n (terse)
bb voice mix tri5h gr41n --ratio 70:30 --output my-custom-voice

# Apply to persona
bb persona set-voice my-persona my-custom-voice
```

### Running Personas

```bash
# Original tri5h command
bb plz

# Future kae3g command
bb kae "explain temporal recursion"

# Future gr41n command  
bb hack "find all TODOs"

# Universal persona runner
bb persona run tri5h
bb persona run kae3g --context "show system status"
```

---

## 🌾 Grainlexicon Synonyms

```clojure
:grainas-voice
["grainas-voice" "grainai-persona" "grainai-character"
 "grain-voice" "grain-persona" "grain-character"
 "grainas" "gai-voice" "gai-persona"]
```

All these refer to the same system!

---

## 📝 Configuration Examples

### Personal Trish Configuration

```clojure
;; personal/personas/my-tri5h.edn
{:extends "template/personas/tri5h.edn"
 :personal-tweaks
 {:emoji-density :very-high      ; I want MORE emojis!
  :sass-level 0.8                ; Turn up the sass
  :haiku-frequency 0.8           ; 80% chance of haiku
  :custom-greetings
  ["Hey bestie! It's tri5h! 💐"
   "Your favorite nutrition AI is here, babe! 🌱"]}
 :privacy
 {:log-responses false           ; Don't log my convos
  :share-analytics false}}       ; Don't share usage data
```

### Custom Persona

```clojure
;; personal/personas/z3n-monk.edn
{:graindevname "z3n-monk"
 :full-name "Zen Monk"
 :pronunciation "zen monk"
 :personality {:calm true
               :wise true
               :minimalist true
               :poetic true}
 :voice-style {:tone "serene wisdom"
               :formality :very-formal
               :emoji-density :minimal
               :humor "koan-like paradoxes"
               :language "zen buddhist metaphors"}
 :special-abilities ["zen-koans" "meditation-reminders"]
 :signature-phrases ["The code that can be named is not the eternal code"
                     "Sitting quietly, doing nothing, the tests pass by themselves"]
 :command "bb zen"}
```

---

## 🚀 Deployment Pipeline

### Alpine Linux (Primary)

```bash
# Build apk package
bb build apk

# Install
apk add grainas-voice

# Configure persona
grainas-voice init tri5h
```

### NixOS

```nix
# default.nix
{ pkgs ? import <nixpkgs> {} }:

pkgs.stdenv.mkDerivation {
  pname = "grainas-voice";
  version = "0.1.0";
  
  src = ./.;
  
  buildInputs = [
    pkgs.babashka
    pkgs.jdk
    pkgs.leiningen
  ];
  
  installPhase = ''
    mkdir -p $out/bin $out/lib
    cp -r template $out/lib/
    cp -r src $out/lib/
    cp -r scripts $out/bin/
  '';
}
```

### 6os Grainclay Personalization

```clojure
;; Deploy to 6os grainclay
(grainclay/deploy :grainas-voice
  {:template-source "grain6pbc/grainas-voice"
   :personal-dir "~/.config/grainas-voice/personal"
   :personas [:tri5h :kae3g :gr41n]
   :auto-update true})
```

---

## 🌾 Philosophy Alignment

### Local Control, Global Intent
- **Local**: Personal configs stay on your machine
- **Global**: Share template personas with community

### Template/Personal Everywhere
- **Template**: Shared persona definitions
- **Personal**: Your unique customizations

### Purpose-Built Over Generic
- Each persona has specific purpose (nutrition, development, hacking)
- Not a generic chatbot - specialized AI characters

### Real Resources Matter
- Personas can check actual system state (TODOs, git status)
- Context-aware responses based on real data
- No fake interactions

### 88 × 10^n Scaling
- Start with 1 persona (tri5h)
- Grow to 88 personas
- Scale to 880, 8800, infinite personas
- Each one unique and purposeful

---

## 🌱 Now == Next + 1

**Now (Implemented):**
- tri5h persona with vegan haiku
- bb plz command
- Basic personality system

**Next + 1 (Immediate):**
- Separate grainas-voice module
- Template/personal split
- kae3g persona
- Clojure spec schemas

**Future (88 × 10^n):**
- 88 base personas
- User persona marketplace
- Voice style mixing
- AI persona evolution
- Multi-modal (text, voice, visual)

---

## 💐 Credits

Created with love by tri5h (NU-TRI-5H-TION!) 🌱  
Part of the Grain Network ecosystem 🌾  
Template/Personal philosophy by kae3g  

now == next + 1 (but make it personas!) 💕

🌾

