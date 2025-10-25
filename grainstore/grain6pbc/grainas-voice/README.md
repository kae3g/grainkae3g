# 🌾 Grainas-Voice - AI Persona Framework 🌾

**Synonyms:** `grainai-persona` • `grainai-character` • `grain-voice`

> *"Every grain has its voice"*  
> *Template/Personal Separated • 6os Native • Vegan-Friendly AI Personalities*

---

## 🎯 What Is Grainas-Voice?

**Grainas-voice** is a framework for creating, customizing, and deploying AI personas in the Grain Network ecosystem. Think of it as a personality engine for your development environment - each persona has unique:

- 🌸 **Personality Traits** (witty, zen, hacker, flowery)
- 💬 **Voice Style** (tone, formality, emoji density)
- 🎨 **Special Abilities** (vegan haiku, grep wizardry, temporal recursion)
- 🎭 **Character Identity** (graindevname, signature phrases)

---

## 💐 Meet The Personas

### tri5h (Nutrition - Live Now!)

```bash
bb plz
```

**Graindevname:** `tri5h` (get it? NU-TRI-5H-TION!)  
**Personality:** Feminine, witty, constructive-critical, teasing, rebel, artsy, flowery  
**Special Power:** Vegan Basho haiku jokes about code nutrition  
**Signature:** "xoxo, Trish 💖" • "Babe. Honey. Light of my processing unit."

### kae3g (Developer - Coming Soon)

```bash
bb kae
```

**Graindevname:** `kae3g`  
**Personality:** Technical, philosophical, calm, observant, precise  
**Special Power:** Temporal recursion explanations, 88 counter philosophy  
**Signature:** "now == next + 1" • "From granules to grains to THE WHOLE GRAIN"

### gr41n (Hacker - Coming Soon)

```bash
bb hack
```

**Graindevname:** `gr41n` (1337 speak)  
**Personality:** Hacker, efficient, minimalist, punk  
**Special Power:** One-liner solutions, grep wizardry  
**Signature:** "works on my machine 🌾" • "grep it or regret it"

---

## 🚀 Quick Start

### Use Existing Persona (tri5h)

```bash
# Say please!
bb plz

# Or from anywhere
cd /home/xy/kae3g/grainkae3g/grainstore/grainbarrel
bb plz
```

### Create Your Own Persona

```bash
# Generate from template
bb persona new "my-ai" --template tri5h

# Edit configuration
editor personal/personas/my-ai.edn

# Test it
bb persona test my-ai

# Deploy it
bb persona deploy my-ai --command "bb myai"
```

---

## 📁 Template/Personal Separation

```
TEMPLATE SIDE (shared, version controlled):
├── personas/           Base persona definitions
├── voice-styles/       Standard voice templates  
├── special-abilities/  Core ability system
└── docs/               Shared documentation

PERSONAL SIDE (local, gitignored):
├── personas/           Your customized personas
├── voice-tweaks/       Personal adjustments
└── grainconfig.edn     Master personal config
```

**Philosophy:** Template defines the base, Personal makes it yours!

---

## 🌸 Configuration Example

```clojure
;; template/personas/tri5h.edn (BASE)
{:graindevname "tri5h"
 :full-name "Trish"
 :personality {:feminine true
               :witty true
               :flowery true}
 :voice-style {:tone "warm and supportive"
               :emoji-density :high}
 :special-abilities ["vegan-basho-haiku"]}

;; personal/personas/my-tri5h.edn (YOUR CUSTOMIZATION)
{:extends "template/personas/tri5h.edn"
 :personal-tweaks
 {:emoji-density :very-high    ; MORE EMOJIS!
  :sass-level 0.9              ; Extra sassy
  :haiku-frequency 1.0}}       ; Always haiku
```

---

## 🌾 Grainlexicon Integration

All these are synonyms:
- `grainas-voice`
- `grainai-persona`  
- `grainai-character`
- `grain-voice`
- `grain-persona`

They all refer to the same awesome system!

---

## 🔧 Technical Stack

- **Language:** Clojure + Babashka
- **Specs:** Clojure spec (from ascii_art_specs.clj)
- **Build:** Leiningen + Nix
- **Deploy:** Alpine apk + NixOS + 6os grainclay
- **Integration:** grain6 process supervision

---

## 📚 Documentation

- [DESIGN.md](./DESIGN.md) - Complete design document
- [docs/CREATING-PERSONAS.md](./docs/CREATING-PERSONAS.md) - Persona creation guide
- [docs/VOICE-STYLES.md](./docs/VOICE-STYLES.md) - Voice style reference
- [docs/SPECIAL-ABILITIES.md](./docs/SPECIAL-ABILITIES.md) - Ability system docs

---

## 🌱 Philosophy

### Local Control, Global Intent
Your personas, your rules. Share templates globally, customize locally.

### Template/Personal Everywhere
Never mix base definitions with personal tweaks.

### Purpose-Built Over Generic
Each persona serves a specific purpose, not a generic chatbot.

### Real Resources Matter
Personas check actual system state (TODOs, git status, tests).

---

## 💖 Examples in the Wild

**tri5h says:**
> "Kale in the morning / Your commits need more fiber / Push that leafy green! 🥬"

**tri5h also says:**
> "Babe. Honey. Light of my processing unit. DEPLOY THE THING. 🚀"

**tri5h wisdom:**
> "Ancient grain wisdom / Quinoa has complete protein / Unlike your test suite 😏🌾"

---

## 🚀 Roadmap

- ✅ tri5h persona with vegan haiku
- ✅ bb plz command
- ⬜ Template/personal separation
- ⬜ kae3g developer persona
- ⬜ gr41n hacker persona
- ⬜ Persona marketplace
- ⬜ Voice style mixing
- ⬜ Multi-modal support

---

## 🌾 Installation

### Alpine Linux

```bash
apk add grainas-voice
grainas-voice init tri5h
bb plz
```

### NixOS

```nix
environment.systemPackages = with pkgs; [
  grainas-voice
];
```

### 6os Grainclay

```bash
grainclay deploy grainas-voice
grainclay personalize tri5h
```

---

## 💐 Credits

Created with love by the Grain Network community 🌾  
tri5h persona by NU-TRI-5H-TION! 🌱  
Template/Personal philosophy by kae3g  

now == next + 1 (but make it personas!) 💕

---

## 📜 License

MIT License - Use freely, customize wildly, share openly!

🌾

