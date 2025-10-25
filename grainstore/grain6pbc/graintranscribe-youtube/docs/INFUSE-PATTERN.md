# The Infuse Pattern - SixOS-Inspired Deep Configuration Merge

## 🌾 Philosophy

The **infuse pattern** is inspired by SixOS's `infuse.nix` library, which provides "deep" versions of `.override` and `.overrideAttrs` in Nix. We adapt this pattern for Clojure/Babashka configuration management.

## 🎯 Core Concept

**Deep merge** of template and personal configurations, where personal values take precedence at all levels of nesting.

### Traditional Merge (Shallow)

```clojure
(merge template personal)
```

Problem: Only merges top-level keys. Nested maps are replaced entirely.

**Example**:
```clojure
(merge {:a {:b 1 :c 2}} 
       {:a {:b 3}})
; => {:a {:b 3}}  ; Lost :c!
```

### Infuse Merge (Deep)

```clojure
(infuse-configs template personal)
```

Solution: Recursively merges nested maps, preserving all keys.

**Example**:
```clojure
(infuse-configs {:a {:b 1 :c 2}} 
                {:a {:b 3}})
; => {:a {:b 3 :c 2}}  ; Preserved :c!
```

## 🔧 Implementation

```clojure
(defn infuse-configs
  "Deep merge template and personal configs (personal takes precedence)
   Inspired by sixos infuse.nix library"
  [template personal]
  (cond
    (and (map? template) (map? personal))
    (merge-with infuse-configs template personal)
    
    (some? personal) personal
    :else template))
```

### How It Works

1. **Check if both are maps**: If yes, recursively merge them
2. **Personal exists?**: Use personal value (override)
3. **Otherwise**: Use template value (default)

This creates a **fixpoint** where nested structures are properly merged while respecting overrides.

## 📦 File Structure

```
graintranscribe-youtube/
├── template/
│   └── config.edn          # Default configuration (version controlled)
├── personal/
│   └── config.edn          # Your overrides (gitignored)
└── src/
    └── graintranscribe/
        └── core.clj        # Infuse implementation
```

## 🌊 Configuration Flow

### Template Config (`template/config.edn`)

```clojure
{:gemini
 {:api-key "REPLACE_WITH_YOUR_KEY"
  :model "gemini-2.5-pro-latest"
  :temperature 0.2
  :max-tokens 1000000}
 
 :output
 {:dir "personal/transcriptions"
  :format :markdown
  :include-timestamps true
  :include-speakers true
  :include-summary true}
 
 :youtube
 {:extract-metadata true
  :language-hint "en"}}
```

### Personal Config (`personal/config.edn`)

```clojure
{:gemini
 {:api-key "your-actual-api-key"}  ; Override just the API key
 
 :output
 {:format :edn}}  ; Override just the format
```

### Infused Result

```clojure
{:gemini
 {:api-key "your-actual-api-key"  ; From personal
  :model "gemini-2.5-pro-latest"  ; From template
  :temperature 0.2                 ; From template
  :max-tokens 1000000}             ; From template
 
 :output
 {:dir "personal/transcriptions"  ; From template
  :format :edn                     ; From personal (overridden!)
  :include-timestamps true         ; From template
  :include-speakers true           ; From template
  :include-summary true}           ; From template
 
 :youtube
 {:extract-metadata true           ; From template
  :language-hint "en"}}            ; From template
```

## 🔄 Comparison to SixOS

| SixOS (Nix) | Graintranscribe (Clojure) |
|-------------|---------------------------|
| `infuse.nix` library | `infuse-configs` function |
| `.override` / `.overrideAttrs` | `merge-with infuse-configs` |
| Nix attrsets | Clojure maps |
| NixOS modules | EDN configs |
| `lib.mkOverride` | `(some? personal)` check |
| Deferred evaluation | Lazy loading from files |

## 🎯 Use Cases

### 1. API Key Override

**Template**: Placeholder value
**Personal**: Real API key
**Result**: Secure key, sharable template

### 2. Output Preferences

**Template**: Markdown format
**Personal**: EDN format (for processing)
**Result**: Personal workflow, template defaults preserved

### 3. Language Customization

**Template**: English hints
**Personal**: Spanish hints
**Result**: Localized without modifying template

### 4. Feature Flags

**Template**: All features enabled
**Personal**: Disable visual descriptions
**Result**: Lighter, faster transcription

## 🌾 The Infuse Philosophy

### Composability

Like Nix's `lib.pipe` and `recursiveUpdate`, infuse creates **composable transformations**:

```clojure
(-> template-config
    (infuse-configs personal-config)
    (infuse-configs project-config)
    (infuse-configs session-config))
```

Each layer adds or overrides, maintaining the full structure.

### Immutability

Infuse never mutates input:

```clojure
(let [template {...}
      personal {...}
      result (infuse-configs template personal)]
  ;; template and personal are unchanged
  ;; result is a new map
  )
```

This is **pure functional** - same inputs always produce same output.

### Deterministic

Like SixOS's `make-host-attrnames-deterministic`, the infused config has **statically known structure**:

```clojure
;; These paths are guaranteed to exist (with defaults)
(get-in config [:gemini :model])
(get-in config [:output :format])
(get-in config [:youtube :language-hint])
```

## 📚 Further Reading

- [SixOS infuse.nix](https://github.com/amjoseph/sixos/blob/main/lib/default.nix)
- [NixOS modules documentation](https://nixos.org/manual/nixos/stable/#sec-writing-modules)
- [Clojure merge-with](https://clojuredocs.org/clojure.core/merge-with)

---

**Built with 🌾 for sovereign configuration management**

*"Deep merge from stable templates, infinite personal customization"*

**now == next + 1**: Each configuration contains all template defaults while allowing complete personal override.
