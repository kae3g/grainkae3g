# 🌾 grainregistry

**Typo-Tolerant Name Resolution for Grain Network**

Also known as: `grainresolver`, `graintypo`

A unified registry system that makes typos a non-issue across all Grain Network tools.

---

## 🎯 **Concept**

**The Problem:**
- Users type `clomoko` instead of `clotoko`
- Commands fail with "not found"
- Frustration and friction

**The Solution:**
- Intelligent name resolution
- Fuzzy matching
- Known typo database
- Alias support
- Auto-correction with user feedback

---

## 💡 **Features**

### **1. Typo Tolerance**

```bash
# User types (typo):
gb clomoko:build

# System resolves:
📝 Autocorrected: clomoko → clotoko
🌾 Running: gb clotoko:build
```

### **2. Alias Support**

```bash
# User types (alias):
gb g6:supervise

# System resolves:
📝 Using alias: g6 → grain6
🌾 Running: gb grain6:supervise
```

### **3. Fuzzy Matching**

```bash
# User types (misspelled):
gb grainmusik:play

# System suggests:
📝 Did you mean: grainmusic? (88% match)
🌾 Running: gb grainmusic:play
```

### **4. Multi-Tool Integration**

Works across:
- `gb` (grainbarrel)
- `grainsource` (version control)
- `gt` (graintime)
- `grain6` (supervision)
- All Grain Network CLIs

---

## 🏗️ **Architecture**

```
┌──────────────────────────────────────────┐
│      grainregistry / grainresolver       │
│                                          │
│  Unified Name Resolution System          │
└──────────┬───────────────────────────────┘
           │
           ├──► clojure-sixos
           │    └── Typo detection engine
           │
           ├──► Grain Network Registry
           │    ├── grain6 → grainsix
           │    ├── clotoko → clomoko
           │    ├── grainmusic → grainmusik
           │    └── [all modules]
           │
           └──► Integration Layer
                ├── grainbarrel (gb)
                ├── grainsource (git)
                ├── graintime (gt)
                └── grain6 (supervision)
```

---

## 📚 **Registry Entries**

### **Current Registrations**

| Canonical | Typos | Aliases | Category |
|-----------|-------|---------|----------|
| `grain6` | grainsix, grain-6, grain-six | g6, grain-s6 | library |
| `clotoko` | clomoko, clatoko, clotokko | clojure-motoko, clj-motoko | transpiler |
| `grainmusic` | grainmusik, grain-music | gmusic, grain-audio | media |
| `grainweb` | grain-web, grainwebb | gweb, grain-browser | service |
| `grainwriter` | grain-writer, grainwritter | gwriter, grain-editor | hardware |
| `grainspace` | grain-space, grainspce | gspace | service |
| `grainconv` | grain-conv, grainconvert | gconv, grain-convert | tool |
| `clojure-s6` | clojure-sixos, clojure_s6 | clj-s6, s6-clj | library |
| `clojure-icp` | clojure-dfinity, clojure_icp | clj-icp, icp-clj | library |
| `grainphotos` | grain-photos, grainpics | gphotos, grain-pics | media |
| `graindisplay` | grain-display, graindisply | gdisplay, grain-warmth | tool |
| `graintime` | grain-time, graintiime | gt, gtime | library |
| `grainbarrel` | grain-barrel, grainbarel | gb, gbarrel | tool |
| `grainsource` | grain-source, grainsrc | gsource, gsrc | tool |

---

## 🔧 **API**

### **Core Functions**

```clojure
(ns grainregistry.core
  "Main API for name resolution")

;; Resolve names
(resolve-name "grainsix")        ;=> "grain6"
(resolve-name "clomoko")         ;=> "clotoko"

;; Check validity
(valid-name? "grain6")           ;=> true
(valid-name? "grainsix")         ;=> true (known alias)
(valid-name? "unknown")          ;=> false

;; Get suggestions
(suggest "grainmusik")
;=> {:canonical "grainmusic"
;    :confidence 0.875
;    :match-type :typo}

;; Register new names
(register "mymodule"
  {:typos ["my-module" "mymodul"]
   :aliases ["mm"]
   :category :tool})

;; Query by category
(by-category :library)
;=> ["grain6" "graintime" "clojure-s6" "clojure-icp"]
```

---

## 🔗 **Integration with grainbarrel**

### **Current grainbarrel (gb)**

```bash
gb clotoko:build  # Works
gb clomoko:build  # Fails with "module not found"
```

### **With grainregistry Integration**

```bash
gb clotoko:build  # Works
gb clomoko:build  # Autocorrects to clotoko, then works!
```

### **Implementation in grainbarrel**

```clojure
;; grainbarrel/src/grainbarrel/core.clj
(ns grainbarrel.core
  (:require [grainregistry.core :as registry]))

(defn resolve-module-name [module-name]
  "Resolve module name with typo tolerance"
  (let [result (registry/resolve-name module-name)]
    (when (not= module-name result)
      (println (str "📝 Autocorrected: " module-name " → " result)))
    result))

(defn run-task [module-and-task]
  (let [[module task] (str/split module-and-task #":")
        resolved-module (resolve-module-name module)]
    ;; Run task with resolved module name
    ...))
```

---

## 🔗 **Integration with grainsource**

### **Current grainsource**

```bash
grainsource clone grainmusic  # Works
grainsource clone grainmusik  # Fails
```

### **With grainregistry Integration**

```bash
grainsource clone grainmusic  # Works
grainsource clone grainmusik  # Autocorrects to grainmusic!
```

### **Implementation in grainsource**

```clojure
;; grainsource/src/grainsource/core.clj
(ns grainsource.core
  (:require [grainregistry.core :as registry]))

(defn resolve-repo-name [repo-name]
  "Resolve repository name with typo tolerance"
  (let [result (registry/resolve-name repo-name)]
    (when (not= repo-name result)
      (println (str "📝 Autocorrected: " repo-name " → " result)))
    result))

(defn clone [repo-name]
  (let [resolved (resolve-repo-name repo-name)
        url (str "https://github.com/grainpbc/" resolved)]
    (shell "git" "clone" url)))
```

---

## 📊 **Usage Examples**

### **Example 1: grainbarrel Task Execution**

```bash
# User makes typo
$ gb grain-6:supervise

# Output:
📝 Autocorrected: grain-6 → grain6
🌾 Running task: grain6:supervise
✅ Service started
```

### **Example 2: grainsource Repository Cloning**

```bash
# User uses alias
$ grainsource clone g6

# Output:
📝 Using alias: g6 → grain6
🌾 Cloning: https://github.com/grainpbc/grain6
✅ Repository cloned
```

### **Example 3: Compound Typo**

```bash
# Multiple typos
$ gb grain-six:test

# Output:
📝 Autocorrected: grain-six → grain6
🌾 Running task: grain6:test
🧪 Running tests...
✅ All tests passed
```

---

## 🎨 **Design Philosophy**

### **Typos as Features, Not Bugs**

Traditional approach:
```
User types "clomoko"
→ Command not found
→ User frustrated
→ Has to look up correct spelling
```

grainregistry approach:
```
User types "clomoko"
→ Registry resolves to "clotoko"
→ Informs user of autocorrection
→ Runs command successfully
→ User learns correct spelling
```

**Benefits:**
1. **Lower cognitive load** - Don't memorize exact spellings
2. **Faster workflow** - No need to stop and look up names
3. **Educational** - Shows correct name each time
4. **Forgiving** - Beginners aren't punished for mistakes
5. **Discoverable** - Aliases make tools easier to find

---

## 🔤 **Naming Convention**

**Three Names, One Module:**

1. **grain registry** - Descriptive, clear
2. **grainresolver** - Functional, technical
3. **graintypo** - Memorable, specific

**Registered in clojure-sixos:**
```clojure
{:canonical "grainregistry"
 :typos ["grain-registry" "grainregister"]
 :aliases ["grainresolver" "graintypo" "gregistry"]
 :category :library}
```

**Why multiple names?**
- Different mental models
- Search optimization
- Community preference
- Linguistic variety

---

## 🛠️ **Development**

### **Directory Structure**

```
grainregistry/
├── README.md            (this file)
├── deps.edn
├── bb.edn
├── src/
│   └── grainregistry/
│       ├── core.clj          (main API, wraps clojure-sixos)
│       ├── grain_modules.edn (registry data)
│       └── integrations/
│           ├── grainbarrel.clj
│           └── grainsource.clj
├── test/
│   └── grainregistry/
│       └── core_test.clj
└── examples/
    ├── grainbarrel-integration.md
    └── grainsource-integration.md

Symlinks:
grainresolver -> grainregistry
graintypo -> grainregistry
```

### **Dependencies**

```clojure
{:deps {clojure-sixos {:local/root "../clojure-sixos"}
        babashka/fs {:mvn/version "0.4.19"}}}
```

---

## 📝 **Complete Registry Data**

Let me create the comprehensive grain modules registry:

### **All Grain Network Modules**

**Core Libraries:**
- `clojure-s6` - Process supervision
- `clojure-sixos` - System configuration with typo handling
- `clojure-icp` - Internet Computer Protocol integration
- `clotoko` - Clojure to Motoko transpiler
- `grain6` - Time-aware supervision

**System Tools:**
- `grainbarrel` (gb) - Build system wrapper
- `grainsource` - Git-compatible VCS
- `graintime` (gt) - Neovedic timestamps
- `grainconv` - File conversion utilities

**Applications:**
- `grainweb` - Browser + Git explorer
- `grainspace` - Unified decentralized platform
- `grainwriter` - E-ink writing device
- `graindisplay` - Display warmth control
- `grainmusic` - Music player/manager
- `grainphotos` - Photo management

**Configuration:**
- `grainzsh` - Zsh configuration
- `grainenvvars` - Environment variable management
- `graindaemon` - Daemon management
- `grainicons` - Icon management
- `graincasks` - Application installation

**Specialized:**
- `graincourse` - Course creation system
- `graindevname` - Username convention
- `grainaltproteinproject` - Sustainable food systems
- `grain-nightlight` - Display warmth (legacy)
- `grain-metatypes` - Type system abstractions
- `grainneovedic` - Neovedic time calculations
- `grainwifi` - WiFi management
- `graindrive` - Cloud storage integration
- `graindroid` - Android/phone designs
- `grainlexicon` - Terminology and glossary

**Hardware:**
- `grainpack` - Hardware packaging
- `graincamera` - Camera integration
- `grainphone` - Phone designs

---

## 🔗 **Integration Points**

### **1. grainbarrel Integration**

```clojure
;; grainbarrel/src/grainbarrel/resolver.clj
(ns grainbarrel.resolver
  (:require [grainregistry.core :as registry]))

(defn resolve-task-name
  "Resolve module:task with typo tolerance
   
   Examples:
     'grain-6:supervise' → 'grain6:supervise'
     'clomoko:build' → 'clotoko:build'"
  [task-string]
  (let [[module task] (str/split task-string #":")
        resolved-module (registry/resolve-name module)]
    (str resolved-module ":" task)))
```

### **2. grainsource Integration**

```clojure
;; grainsource/src/grainsource/resolver.clj
(ns grainsource.resolver
  (:require [grainregistry.core :as registry]))

(defn resolve-repo-url
  "Resolve repo name to full URL with typo tolerance
   
   Examples:
     'grainsix' → 'https://github.com/grainpbc/grain6'
     'grainmusik' → 'https://github.com/grainpbc/grainmusic'"
  [repo-name org]
  (let [resolved (registry/resolve-name repo-name)
        base-url (case org
                   :grainpbc "https://github.com/grainpbc/"
                   :codeberg "https://codeberg.org/grainpbc/"
                   "https://github.com/grainpbc/")]
    (str base-url resolved)))
```

### **3. Universal Resolver**

```bash
# Command-line resolver for any tool
$ grainresolve grainsix
grain6

$ grainresolve clomoko
clotoko

$ grainresolve grainmusik
grainmusic
```

---

## 🎨 **User Experience**

### **Silent Auto-Correction**

```bash
$ gb grain-six:time
📝 Autocorrected: grain-six → grain6
🌾 Current Time State:
...
```

### **Suggestion Mode**

```bash
$ gb grainmuzik:play
❓ Did you mean 'grainmusic'? (y/n)
```

### **Learning Mode**

```bash
$ gb clomoko:build --learn
📝 Autocorrected: clomoko → clotoko
💡 Tip: The correct spelling is 'clotoko' (Clojure + Motoko)
🌾 Running: gb clotoko:build
```

---

## 🔧 **Implementation**

### **Core API**

```clojure
(ns grainregistry.core
  "Main grainregistry API - wraps clojure-sixos with Grain Network defaults"
  (:require [clojure-sixos.core :as sixos]))

;; Re-export core functions
(def resolve-name sixos/resolve-name)
(def valid-name? sixos/valid-name?)
(def suggest sixos/suggest)
(def register sixos/register)

;; Grain-specific utilities
(defn resolve-with-feedback
  "Resolve name and print feedback to user"
  [name]
  (let [result (sixos/resolve-with-message name)]
    (when (:message result)
      (println (str "📝 " (:message result))))
    (:resolved result)))

(defn resolve-or-fail
  "Resolve name or exit with helpful error"
  [name]
  (let [result (sixos/suggest name)]
    (if (:canonical result)
      (do
        (when (not= name (:canonical result))
          (println (str "📝 Autocorrected: " name " → " (:canonical result))))
        (:canonical result))
      (do
        (println (str "❌ Unknown module: " name))
        (println "")
        (println "Did you mean one of these?")
        (doseq [suggestion (take 5 (sixos/all-registered))]
          (when (> (sixos/similarity name suggestion) 0.5)
            (println (str "  - " suggestion))))
        (System/exit 1)))))

(defn resolve-module-task
  "Resolve 'module:task' string with typo tolerance"
  [module-task-str]
  (let [[module task] (str/split module-task-str #":")]
    (str (resolve-with-feedback module) ":" task)))
```

---

## 📖 **Usage in grainbarrel**

### **Before grainregistry:**

```clojure
;; grainbarrel/src/grainbarrel/core.clj
(defn run-task [task-str]
  (let [[module task] (str/split task-str #":")]
    (if (module-exists? module)
      (execute-task module task)
      (println "Error: Module not found"))))
```

**Problem:** Typos break everything

### **After grainregistry:**

```clojure
;; grainbarrel/src/grainbarrel/core.clj
(ns grainbarrel.core
  (:require [grainregistry.core :as registry]))

(defn run-task [task-str]
  (let [resolved-str (registry/resolve-module-task task-str)
        [module task] (str/split resolved-str #":")]
    (execute-task module task)))
```

**Benefit:** All typos auto-corrected!

---

## 📖 **Usage in grainsource**

### **Clone Command Enhancement**

```clojure
;; grainsource/src/grainsource/commands/clone.clj
(ns grainsource.commands.clone
  (:require [grainregistry.core :as registry]))

(defn clone-repo [repo-name]
  (let [resolved (registry/resolve-with-feedback repo-name)
        url (str "https://github.com/grainpbc/" resolved)]
    (println (str "🌾 Cloning " resolved "..."))
    (shell "git" "clone" url)))
```

### **Enhanced Error Messages**

```bash
$ grainsource clone grainmuzik

📝 Autocorrected: grainmuzik → grainmusic
🌾 Cloning grainmusic...
Cloning into 'grainmusic'...
✅ Done
```

---

## 🌾 **Grain Network Registry Philosophy**

### **Forgiveness Over Precision**

Traditional systems:
- Demand exact input
- Fail on minor mistakes
- User must memorize everything

grainregistry systems:
- Accept imperfect input
- Correct intelligently
- User learns through feedback

### **Progressive Enhancement**

**Level 1:** Exact match (instant)
**Level 2:** Known typo (instant + feedback)
**Level 3:** Fuzzy match (fast + suggestion)
**Level 4:** Interactive fallback (ask user)

### **From Granules to Grains**

**Granule:** Single name resolution
```clojure
(resolve-name "grainsix") ;=> "grain6"
```

**Grain:** Module-task resolution
```clojure
(resolve-module-task "grainsix:time") ;=> "grain6:time"
```

**THE WHOLE GRAIN:** Ecosystem-wide tolerance
```
All tools (gb, grainsource, gt, grain6) use grainregistry
→ Consistent experience across entire Grain Network
→ Learn once, works everywhere
```

---

## 🚀 **Future Enhancements**

### **Phase 1: Core (v0.1.0)**
- [x] Symlinks created (grainregistry, grainresolver, graintypo)
- [x] Design and documentation
- [ ] Wrapper around clojure-sixos
- [ ] CLI tool (`grainresolve`)
- [ ] Integration helpers for gb and grainsource

### **Phase 2: Intelligence (v0.2.0)**
- [ ] Machine learning for typo prediction
- [ ] Usage statistics (most common typos)
- [ ] Context-aware suggestions
- [ ] Multi-language support

### **Phase 3: Distribution (v0.3.0)**
- [ ] Standalone npm package
- [ ] Rust implementation for performance
- [ ] Web API for browser integration
- [ ] VSCode extension

### **Phase 4: Network (v0.4.0)**
- [ ] Distributed registry (on-chain?)
- [ ] Community contributions
- [ ] Voting on canonical names
- [ ] Automatic typo learning

---

## 🧪 **Testing**

```bash
# Test typo resolution
bb test:typos

# Test all integrations
bb test:integrations

# Benchmark performance
bb bench:resolve
```

---

## 📚 **See Also**

- [clojure-sixos](../clojure-sixos/README.md) - Underlying typo engine
- [grainbarrel](../grainbarrel/README.md) - Build system integration
- [grainsource](../grainsource/README.md) - VCS integration
- [grain6](../grain6/README.md) - Supervision with time awareness

---

**Version:** 0.1.0 (Design Phase)  
**Status:** 🚧 Under Development  
**Author:** kae3g (Grain PBC)  
**License:** MIT

🌾 **Making typos a non-issue across the Grain Network!**
