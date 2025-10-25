# clojure-sixos

**Clojure library for SixOS integration with typo-catching and fuzzy resolution**

A Clojure library for interacting with SixOS (NixOS without systemd), featuring intelligent typo-catching handlers that make the system more user-friendly and forgiving of common naming variations.

---

## 🎯 **Core Philosophy**

**Typos happen. Good systems handle them gracefully.**

Instead of failing with cryptic errors when users type `clomoko` instead of `clotoko`, or `grainmusik` instead of `grainmusic`, the system should:

1. **Detect** the typo using fuzzy matching
2. **Suggest** the correct name
3. **Proceed** automatically if the intent is clear
4. **Learn** from common patterns

This is inspired by Git's typo handling:
```bash
$ git comit
git: 'comit' is not a git command. See 'git --help'.

The most similar command is
	commit
```

But we go further: **we autocorrect when safe**.

---

## 🌾 **Features**

### **1. Typo Detection & Correction**

```clojure
(require '[clojure-sixos.typo :as typo])

;; Automatic correction
(typo/resolve-name "clomoko")  ;; => "clotoko"
(typo/resolve-name "grainmusik")  ;; => "grainmusic"

;; With suggestions
(typo/suggest "graincaly")
;; => {:suggestions ["grainclay"] :distance 1}

;; Batch resolution
(typo/resolve-names ["clomoko" "grainweb" "grainmusik"])
;; => ["clotoko" "grainweb" "grainmusic"]
```

### **2. Fuzzy Name Registry**

```clojure
(require '[clojure-sixos.registry :as reg])

;; Register canonical names and known typos
(reg/register-name "clotoko" {:typos ["clomoko" "clotoko" "clatoko"]
                               :aliases ["clojure-motoko" "clj-motoko"]
                               :category :transpiler})

(reg/register-name "grainmusic" {:typos ["grainmusik" "grain-music"]
                                  :aliases ["gmusic" "grain-audio"]
                                  :category :media})

;; Lookup with fuzzy matching
(reg/lookup "clomoko")
;; => {:canonical "clotoko" :match-type :typo :confidence 1.0}

(reg/lookup "grainmusik")
;; => {:canonical "grainmusic" :match-type :typo :confidence 1.0}
```

### **3. Levenshtein Distance & Similarity**

```clojure
(require '[clojure-sixos.similarity :as sim])

;; Calculate edit distance
(sim/levenshtein-distance "clomoko" "clotoko")  ;; => 1
(sim/levenshtein-distance "grainmusik" "grainmusic")  ;; => 1

;; Similarity score (0.0 to 1.0)
(sim/similarity "clomoko" "clotoko")  ;; => 0.875
(sim/similarity "grainweb" "grainweb")  ;; => 1.0

;; Find closest match
(sim/closest-match "clomoko" ["clotoko" "grainweb" "grainmusic"])
;; => {:match "clotoko" :distance 1 :similarity 0.875}
```

### **4. SixOS Service Resolution**

```clojure
(require '[clojure-sixos.service :as svc])

;; Resolve service names with typo handling
(svc/resolve-service "clomoko-daemon")
;; => {:canonical "clotoko-daemon" :autocorrected true}

;; Start service with fuzzy name
(svc/start "grainmusik-server")
;; => Autocorrected: grainmusik-server → grainmusic-server
;;    Starting grainmusic-server...
```

### **5. Integration with clojure-s6**

```clojure
(require '[clojure-sixos.s6 :as s6])

;; Define services with typo tolerance
(s6/defservice "clotoko-daemon"
  {:run "./run-clotoko-daemon"
   :typos ["clomoko-daemon" "clatoko-daemon"]
   :aliases ["motoko-transpiler"]})

;; Supervisor operations with autocorrection
(s6/supervise "clomoko-daemon")  ;; Autocorrects to clotoko-daemon
(s6/status "grainmusik-server")  ;; Autocorrects to grainmusic-server
```

---

## 📦 **Installation**

### **deps.edn**

```clojure
{:deps {grainpbc/clojure-sixos {:git/url "https://github.com/grainpbc/clojure-sixos"
                                 :sha "..."}}}
```

### **Leiningen**

```clojure
[grainpbc/clojure-sixos "0.1.0"]
```

---

## 🔧 **Usage Examples**

### **Example 1: Module Resolution**

```clojure
(ns my-app.core
  (:require [clojure-sixos.typo :as typo]))

;; User types: (require '[clomoko.core :as clotoko])
;; System autocorrects:
(defn resolve-require [module-name]
  (let [corrected (typo/resolve-name module-name)]
    (when-not (= module-name corrected)
      (println (format "Autocorrected: %s → %s" module-name corrected)))
    corrected))

(resolve-require "clomoko.core")
;; => Autocorrected: clomoko.core → clotoko.core
;;    "clotoko.core"
```

### **Example 2: CLI Command Handling**

```clojure
(ns grain-cli.handler
  (:require [clojure-sixos.typo :as typo]
            [clojure-sixos.registry :as reg]))

(defn handle-command [cmd args]
  (let [resolved (typo/resolve-name cmd)
        correction? (not= cmd resolved)]
    (when correction?
      (println (format "Did you mean '%s'? Proceeding..." resolved)))
    (execute-command resolved args)))

;; User runs: grain install clomoko
;; System outputs: Did you mean 'clotoko'? Proceeding...
;;                 Installing clotoko...
```

### **Example 3: Package Manager**

```clojure
(ns grainclay.install
  (:require [clojure-sixos.typo :as typo]
            [clojure-sixos.registry :as reg]))

(defn install-package [pkg-name]
  (let [canonical (typo/resolve-name pkg-name)]
    (if (reg/package-exists? canonical)
      (do
        (when (not= pkg-name canonical)
          (println (format "Resolved %s → %s" pkg-name canonical)))
        (install canonical))
      (throw (ex-info "Package not found" {:package pkg-name})))))

;; grain install grainmusik
;; => Resolved grainmusik → grainmusic
;;    Installing grainmusic...
```

---

## 🧠 **Typo Detection Algorithm**

The system uses multiple strategies for typo detection:

### **1. Levenshtein Distance**
- Edit distance between strings
- Threshold: distance ≤ 2 for autocorrect

### **2. Phonetic Matching**
- Soundex/Metaphone for similar-sounding names
- Example: `clomoko` ≈ `clotoko` (both sound similar)

### **3. Common Substitutions**
- `o` ↔ `a` (clomoko/clamako)
- `i` ↔ `y` (grainmusic/grainmusyc)
- `-` ↔ `_` (grain-web/grain_web)

### **4. Prefix/Suffix Matching**
- `grain*` matches all `grain-` prefixed packages
- `*-daemon` matches all daemon services

---

## 🌾 **Grain Network Integration**

This library is designed to work seamlessly with:

- **clotoko** (Clojure-to-Motoko transpiler)
- **clojure-s6** (s6 supervision)
- **grainclay** (package manager)
- **grainweb** (web platform)
- **grainmusic** (music platform)
- **grainwriter** (writing device)

All Grain tools automatically register their names and common typos.

---

## 📝 **Configuration**

### **Global Registry File**

`~/.config/grain/typo-registry.edn`:

```clojure
{:packages
 [{:canonical "clotoko"
   :typos ["clomoko" "clatoko"]
   :aliases ["clojure-motoko"]}
  
  {:canonical "grainmusic"
   :typos ["grainmusik" "grain-music"]
   :aliases ["gmusic"]}]
 
 :autocorrect-threshold 2
 :confidence-threshold 0.75
 :suggest-on-ambiguity true}
```

---

## 🔬 **API Reference**

### **clojure-sixos.typo**

- `(resolve-name name)` - Resolve name with autocorrection
- `(suggest name)` - Get suggestions for typo
- `(resolve-names names)` - Batch resolve
- `(confidence name)` - Get confidence score (0.0-1.0)

### **clojure-sixos.registry**

- `(register-name canonical opts)` - Register canonical name
- `(lookup name)` - Lookup with fuzzy matching
- `(all-names)` - Get all registered names
- `(package-exists? name)` - Check existence

### **clojure-sixos.similarity**

- `(levenshtein-distance s1 s2)` - Calculate edit distance
- `(similarity s1 s2)` - Calculate similarity (0.0-1.0)
- `(closest-match name candidates)` - Find best match

### **clojure-sixos.service**

- `(resolve-service name)` - Resolve service name
- `(start name)` - Start service with autocorrect
- `(stop name)` - Stop service with autocorrect
- `(status name)` - Get status with autocorrect

---

## 🧪 **Testing**

```bash
# Run tests
bb test

# Test specific namespace
bb test clojure-sixos.typo-test
```

---

## 📚 **Philosophy**

**Computers should adapt to humans, not the other way around.**

Traditional systems punish users for typos:
- ❌ "Command not found: clomoko"
- ❌ "Package 'grainmusik' does not exist"
- ❌ "No such service: graincaly"

Grain systems help users succeed:
- ✅ "Autocorrected: clomoko → clotoko"
- ✅ "Installing grainmusic (did you mean grainmusik?)"
- ✅ "Starting grainclay-daemon..."

This is part of the **Grain Network philosophy**: make powerful tools accessible through **intelligent defaults** and **forgiving UX**.

---

## 🤝 **Contributing**

To add new typo patterns:

1. Edit `grainstore/clojure-sixos/resources/typo-patterns.edn`
2. Run `bb update-registry`
3. Submit PR to `grainpbc/clojure-sixos`

---

## 📄 **License**

MIT License (permissive, allowing commercial use)

---

## 🔗 **Related Projects**

- **clojure-s6** - s6 supervision for Clojure
- **grainclay** - Rolling release package manager
- **clotoko** - Clojure-to-Motoko transpiler
- **grainweb** - Decentralized web platform

---

**Part of the Grain Network ecosystem** 🌾

