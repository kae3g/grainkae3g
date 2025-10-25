# 🌾 Grainrules Design Document 🌾

**Module:** `grainrules`  
**Synonyms:** `grain-rules` • `grainrule` • `rules`  
**Type:** Template/Personal Separated  
**Philosophy:** Foundational rule system for Grain Network behavior

---

## 🎯 Purpose

**Grainrules** is the foundational rule system that defines behavioral constraints and patterns across the Grain Network. It provides:

1. **Rule Definitions** - Core behavioral rules (validation, constraints, patterns)
2. **Rule Inheritance** - Hierarchical rule systems
3. **Rule Composition** - Combine rules for complex behaviors
4. **6os Integration** - Native grain6 process constraints
5. **Dependency Base** - Foundation for grainrules-vocab and grainai-vocab

---

## 🌸 Rule Hierarchy

```
grainrules (foundation)
    ↓
grainrules-vocab (vocabulary constraints)
    ↓
grainai-vocab (AI-specific vocabulary)
    ↓
grainas-voice personas (tri5h, kae3g, gr41n)
```

**Dependency Order:**
1. `grainrules` - Base rule system
2. `grainrules-vocab` - Vocabulary rule system (10k words)
3. `grainai-vocab` - AI persona vocabulary (relies on grainrules-vocab)
4. `grainas-voice` - AI personas (use grainai-vocab)

---

## 📁 Directory Structure

```
grainstore/grain6pbc/grainrules/
├── README.md
├── DESIGN.md
├── LICENSE
├── bb.edn
├── project.clj
├── default.nix
│
├── template/
│   ├── rules/
│   │   ├── base.edn              # Fundamental rules
│   │   ├── validation.edn        # Validation rules
│   │   ├── constraints.edn       # Constraint rules
│   │   └── patterns.edn          # Pattern matching rules
│   │
│   └── schema/
│       └── rule-spec.clj         # Clojure spec for rules
│
├── personal/
│   ├── .gitignore
│   ├── rules/
│   │   └── my-rules.edn         # Personal rule overrides
│   └── grainconfig.edn
│
├── src/
│   └── grainrules/
│       ├── core.clj              # Core rule engine
│       ├── validation.clj        # Rule validation
│       └── composition.clj       # Rule composition
│
└── docs/
    ├── RULE-TYPES.md
    ├── COMPOSITION.md
    └── EXAMPLES.md
```

---

## 🌾 Core Rule Types

### 1. Validation Rules

```clojure
(def ::sheaf-id-rule
  {:type :validation
   :spec ::sheaf-id
   :description "Validates grain sheaf identifiers"
   :pattern #"^[a-z0-9-]{3,30}$"
   :constraints {:no-leading-dash true
                 :no-trailing-dash true
                 :lowercase-only true}})
```

### 2. Constraint Rules

```clojure
(def ::max-line-length-rule
  {:type :constraint
   :value 80
   :description "Maximum line length for graincard format"
   :applies-to [:graincard :grainbook :documentation]})
```

### 3. Pattern Rules

```clojure
(def ::graintime-pattern-rule
  {:type :pattern
   :pattern #"\d{5}-\d{2}-\d{2}--\d{4}--[A-Z]{3}--.*"
   :description "Graintime path pattern"
   :example "12025-10-23--0654--PDT--moon-vishakha..."})
```

---

## 🌱 Rule Composition

Rules can be composed:

```clojure
(def ::graincard-rules
  (compose-rules
    ::max-line-length-rule
    ::max-page-count-rule
    ::portrait-format-rule
    ::navigation-required-rule))
```

---

now == next + 1
🌾

