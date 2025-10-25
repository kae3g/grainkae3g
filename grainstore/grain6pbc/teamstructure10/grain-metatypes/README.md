# grain-metatypes

**Foundational meta-type definitions for the Grain Network**

[![License: GPL-3.0](https://img.shields.io/badge/License-GPL%203.0-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![GrainCI](https://img.shields.io/badge/CI-GrainCI-green.svg)](https://github.com/grainpbc/grainci)
[![Status: Active](https://img.shields.io/badge/Status-Active-success.svg)]()

---

## Overview

`grain-metatypes` is the foundational repository for all type definitions in the Grain Network. It provides:

- **Grainmark Types**: Networked type system inspired by Urbit Hoon Arvo Zuse
- **Grainclay Paths**: Immutable, URL-safe path specifications  
- **Grainframe Schemas**: 80×110 message format definitions
- **Network Protocols**: Type-safe communication protocols
- **Clojure Specs**: Complete Clojure spec definitions
- **Haskell Type Equivalents**: Strong type metaphors for API documentation

This repository has **ZERO dependencies** and serves as the foundation for all other Grain projects.

---

## Installation

### Clojure/deps.edn

```clojure
{:deps {io.github.grainpbc/grain-metatypes {:git/tag "v1.0.0" :git/sha "abc123"}}}
```

### Babashka

```bash
bb -Sdeps '{:deps {io.github.grainpbc/grain-metatypes {:git/url "https://github.com/grainpbc/grain-metatypes"}}}' -e '(require [grain-metatypes.core :as gm])'
```

### Leiningen

```clojure
:dependencies [[io.github.grainpbc/grain-metatypes "1.0.0"]]
```

---

## Usage

### Basic Types

```clojure
(require '[grain-metatypes.core :as gm])
(require '[grain-metatypes.mark :as mark])
(require '[grain-metatypes.clay :as clay])
(require '[grain-metatypes.grainframe :as grainframe])

;; Validate a Grainframe
(def my-frame
  {:grainframe-id "abc123"
   :grainframe-content "Hello, Grain Network!"
   :grainframe-metadata {:grainframe-type :text
                         :grainframe-timestamp (java.time.Instant/now)}})

(gm/valid? ::grainframe/grainframe my-frame)
;; => true

;; Create a Clay path
(def my-beam
  {:clay-ship "kae3g"
   :clay-desk "main"
   :clay-revision "1.0.0"
   :clay-path ["docs" "README.md"]})

(clay/clay-beam->string my-beam)
;; => "kae3g/main/1.0.0/docs/README.md"

;; Register a Mark
(mark/register-mark!
  {:mark-name "custom-grainframe"
   :mark-version "1.0.0"
   :mark-spec ::grainframe/grainframe
   :mark-validator #(s/valid? ::grainframe/grainframe %)})
```

---

## Type Modules

### 1. Core Types (`grain-metatypes.core`)

Basic foundational types:

```clojure
::id           ; String identifier
::timestamp    ; Instant timestamp
::text         ; String text
::url          ; Valid URL string
::email        ; Valid email string
::principal    ; ICP Principal ID
::path         ; Hoon-inspired path vector
::version      ; Semantic version string
```

**Haskell Equivalents**:

```haskell
type Id = String
type Timestamp = UTCTime
type Text = String
type Url = String
type Email = String
type Principal = String
type Path = [String]
type Version = String
```

### 2. Social Types (`grain-metatypes.social`)

Social network data structures:

```clojure
::user         ; User profile
::post         ; Social media post
::status       ; User status update
::branch       ; Development branch/timeline
::follow       ; Follow relationship
::tag          ; Content tag
```

### 3. Mark Types (`grain-metatypes.mark`)

Networked type definitions:

```clojure
::mark         ; Mark definition
::mark-name    ; Mark identifier
::mark-spec    ; Clojure spec
::mark-validator    ; Validation function
::mark-serializer   ; Serialization function
::mark-deserializer ; Deserialization function
```

### 4. Clay Types (`grain-metatypes.clay`)

Immutable path system:

```clojure
::clay-segment    ; URL-safe path segment
::clay-path       ; Immutable path vector
::clay-revision   ; Version identifier
::clay-desk       ; Workspace/branch
::clay-ship       ; Identity/owner
::clay-beam       ; Complete path reference
```

### 5. Grainframe Types (`grain-metatypes.grainframe`)

80×110 message format:

```clojure
::grainframe           ; Complete Grainframe
::grainframe-id        ; Unique identifier
::grainframe-content   ; Text content (≤8800 chars)
::grainframe-metadata  ; Metadata map
::grainframe-type      ; Content type
::grainframe-gallery   ; Collection of Grainframes
```

---

## API Reference

### Core Functions

```clojure
(gm/valid? spec data)
;; Validate data against spec

(gm/explain spec data)
;; Get validation explanation

(gm/generate spec)
;; Generate sample data

(gm/conform spec data)
;; Conform data to spec
```

### Mark Functions

```clojure
(mark/register-mark! mark)
;; Register a new mark

(mark/get-mark mark-name)
;; Retrieve mark by name

(mark/validate-mark mark-name data)
;; Validate data against mark

(mark/serialize-mark mark-name data)
;; Serialize data using mark

(mark/deserialize-mark mark-name data)
;; Deserialize data using mark
```

### Clay Functions

```clojure
(clay/clay-path->string path)
;; Convert path to URL-safe string

(clay/string->clay-path string)
;; Parse URL-safe string to path

(clay/clay-beam->string beam)
;; Convert beam to full path string

(clay/string->clay-beam string)
;; Parse string to beam

(clay/clay-path-valid? path)
;; Validate path
```

### Grainframe Functions

```clojure
(grainframe/create-grainframe content & opts)
;; Create new Grainframe

(grainframe/grainframe-valid? grainframe)
;; Validate Grainframe

(grainframe/grainframe->string grainframe)
;; Convert to string

(grainframe/create-gallery name frames & opts)
;; Create Grainframe gallery
```

---

## Type Hierarchy

```
grain-metatypes.core (foundational)
    │
    ├─→ grain-metatypes.social
    ├─→ grain-metatypes.mark
    ├─→ grain-metatypes.clay
    └─→ grain-metatypes.grainframe
            │
            ├─→ text-grainframe
            ├─→ poem-grainframe
            ├─→ document-grainframe
            ├─→ photo-grainframe
            └─→ post-grainframe
```

---

## Specification Format

All specs include both Clojure specs and Haskell type equivalents:

```clojure
;; Clojure Spec
(s/def ::grainframe-id string?)

;; Haskell Equivalent (in comment)
;; type GrainframeId = String

;; Clojure Spec
(s/def ::grainframe
  (s/keys :req-un [::grainframe-id
                   ::grainframe-content
                   ::grainframe-metadata]))

;; Haskell Equivalent (in comment)
;; data Grainframe = Grainframe
;;   { grainframeId :: GrainframeId
;;   , grainframeContent :: Text
;;   , grainframeMetadata :: GrainframeMeta
;;   } deriving (Show, Eq)
```

---

## Testing

```bash
# Run all tests
bb test

# Run specific test namespace
bb test grain-metatypes.mark-test

# Run with coverage
bb test-coverage
```

---

## Documentation

Full API documentation is available at:

- **Online**: https://docs.grain.network/metatypes
- **Local**: Open `docs/index.html` after running `bb docs`

---

## Contributing

See [CONTRIBUTING.md](./CONTRIBUTING.md) for guidelines.

All contributions must:

1. Include Clojure specs
2. Include Haskell type equivalents (in comments)
3. Include tests
4. Pass GrainCI checks
5. Follow append-only rule

---

## License

**GPL-3.0** - Free and open source forever

---

## Links

- **GitHub**: https://github.com/grainpbc/grain-metatypes
- **Issues**: https://github.com/grainpbc/grain-metatypes/issues
- **Discussions**: https://github.com/grainpbc/grain-metatypes/discussions
- **Grain Network**: https://grain.network

---

## Dependent Projects

The following projects depend on `grain-metatypes`:

- `grainclay` - Package manager
- `grainweb` - Social network
- `grainmusic` - Music platform
- `grainconv` - Type converter
- `grainwriter` - E-ink device
- `clotoko` - Clojure-Motoko transpiler
- `clojure-icp` - ICP integration
- `clojure-s6` - s6 supervision
- `clojure-sixos` - SixOS utilities

---

**Built with ❤️ by the Grain Network community**

**Version**: 1.0.0  
**Last Updated**: 2025-01-22

