# Grainbarrel: Grain Network Build System

**Babashka-powered build tool with Grain Network branding**

> *"Pragmatic Branding Over Dogmatic Renaming"* - gb command, bb.edn files (Babashka requirement)  
> *"Feeling like a leaf in the wind but feeling like a rock"*

Grainbarrel (`gb`) is a thin wrapper around Babashka (`bb`) that provides Grain Network-specific enhancements, task organization, and a consistent command-line interface across all Grain projects.

**The Chaos/Solidity Dynamic:**
- **External**: Calm chaos flowing out (creative expression, 7 modules in 72 hours)
- **Internal**: Solid core staying grounded (pure functional architecture, core values)
- **Observer**: You, watching yourself create, documenting the journey
- **Together**: Productive creativity from stable center

## Why Grainbarrel?

**Babashka** is an excellent fast-starting Clojure scripting environment, but Grainbarrel adds:
- 🌾 **Grain Network Branding** - `gb` instead of `bb` for all Grain projects
- 📦 **Task Registry** - Centralized task definitions across grainstore modules
- 🔄 **Cross-Module Tasks** - Run tasks across multiple grainstore modules
- 🎨 **Enhanced Output** - Grain-themed formatting and progress indicators
- 📊 **Dependency Management** - Automatic grainstore module dependency resolution
- 🔍 **Task Discovery** - Search and explore available tasks
- 📝 **Documentation Integration** - Inline help and markdown generation

## Installation

### Option 1: Symlink `gb` to `bb` (Simple)

```bash
# Create symlink in your local bin
ln -s $(which bb) ~/.local/bin/gb

# Or install system-wide
sudo ln -s $(which bb) /usr/local/bin/gb
```

### Option 2: Grainbarrel Wrapper Script (Enhanced)

```bash
# Install Grainbarrel wrapper
cd grainstore/grainbarrel
bb install

# This creates ~/.local/bin/gb with enhanced features
```

## Usage

Grainbarrel is **100% compatible** with Babashka - all `bb` commands work with `gb`:

```bash
# Standard Babashka usage
gb script.clj
gb -e '(println "Hello Grain!")'
gb tasks
gb task-name

# Grainbarrel enhancements
gb grainstore:list           # List all grainstore modules
gb grainstore:update         # Update all modules
gb icon install cursor       # grainicons task
gb cask install cursor       # graincasks task
gb display:info              # graindisplay task
gb nightlight:enable         # grain-nightlight task
```

## `gb` vs `bb`

| Feature | `bb` (Babashka) | `gb` (Grainbarrel) |
|---------|-----------------|---------------------|
| Core functionality | ✅ Full Clojure scripting | ✅ Same (wrapper around bb) |
| Task execution | ✅ `bb.edn` tasks | ✅ Same + cross-module tasks |
| Speed | ✅ Fast startup | ✅ Same performance |
| Branding | Babashka | 🌾 Grain Network |
| Module tasks | Manual navigation | ✅ Run from anywhere |
| Task discovery | `bb tasks` per project | ✅ Global task registry |
| Documentation | External docs | ✅ Inline `gb help` |

## Grainbarrel-Specific Features

### 1. Cross-Module Task Execution

Run tasks from any grainstore module without `cd`:

```bash
# Instead of:
cd grainstore/grainicons && bb icon install cursor

# Use:
gb icon install cursor  # Works from anywhere!
```

### 2. Task Registry

All grainstore module tasks are registered:

```bash
# List all available tasks across all modules
gb tasks:all

# Search for tasks
gb tasks:search icon

# Show task help
gb help icon:install
```

### 3. Module Management

```bash
# List grainstore modules
gb grainstore:list

# Update a specific module
gb grainstore:update grainicons

# Verify module dependencies
gb grainstore:verify

# Create new module
gb grainstore:new my-module
```

### 4. Enhanced Output

Grain-themed formatting:

```bash
🌾 Grainbarrel v1.0.0 (Babashka v1.3.191)

Running task: icon:install cursor
📦 Installing icon: cursor-grain
  ✅ Generated cursor-grain-512.png
  ✅ Generated cursor-grain-256.png
  ✅ Installed to ~/.local/share/icons/
🌾 Success!
```

## Configuration

Global configuration in `~/.config/grainbarrel/config.edn`:

```clojure
{:grainbarrel
 {:version "1.0.0"
  
  :paths
  {:grainstore "~/kae3g/grainkae3g/grainstore"
   :modules-config "grainstore/grainstore.edn"}
  
  :output
  {:theme :grain  ;; :grain, :minimal, :verbose
   :color true
   :emoji true
   :progress-bars true}
  
  :tasks
  {:auto-discover true     ;; Auto-discover tasks from modules
   :namespace-prefix "gb"  ;; Prefix for task namespaces
   :show-hidden false}     ;; Show hidden/internal tasks
  
  :modules
  {:auto-load true         ;; Load all grainstore modules
   :verify-deps true}      ;; Check dependencies on load
  
  :babashka
  {:version-min "1.3.0"
   :jvm-opts ["-Xmx2g"]}}}
```

## Task Naming Convention

Grainbarrel uses namespaced task names:

```
module:task-name

Examples:
- icon:install          (from grainicons)
- icon:generate         (from grainicons)
- cask:install          (from graincasks)
- cask:update           (from graincasks)
- display:info          (from graindisplay)
- display:set-scaling   (from graindisplay)
- nightlight:enable     (from grain-nightlight)
- grainstore:list       (from grainbarrel itself)
```

## Wrapper Implementation

The `gb` wrapper script (`bin/gb`):

```bash
#!/usr/bin/env bash
# Grainbarrel - Grain Network Build System
# Wrapper around Babashka with enhanced features

# Grain Network branding
export GRAINBARREL_VERSION="1.0.0"
export GRAINBARREL_HOME="${GRAINBARREL_HOME:-$HOME/.config/grainbarrel}"

# Check if babashka is installed
if ! command -v bb &> /dev/null; then
    echo "🌾 Grainbarrel requires Babashka (bb) to be installed."
    echo "   Install from: https://babashka.org"
    exit 1
fi

# Grainbarrel-specific commands
case "$1" in
    --version|-v)
        echo "🌾 Grainbarrel v${GRAINBARREL_VERSION} (Babashka $(bb --version | cut -d' ' -f2))"
        exit 0
        ;;
    help)
        if [ -z "$2" ]; then
            echo "🌾 Grainbarrel - Grain Network Build System"
            echo ""
            echo "Usage: gb [task|script] [args...]"
            echo ""
            echo "Grainbarrel Commands:"
            echo "  gb tasks:all        List all tasks across grainstore modules"
            echo "  gb grainstore:list  List all grainstore modules"
            echo "  gb help [task]      Show help for a specific task"
            echo ""
            echo "For full Babashka help: bb --help"
        else
            # Show help for specific task
            bb -e "(require '[grainbarrel.help :as help]) (help/show-task \"$2\")"
        fi
        exit 0
        ;;
esac

# Pass through to babashka with Grainbarrel context
export GRAINBARREL_ACTIVE=true
bb "$@"
```

## Grainstore Integration

Grainbarrel reads `grainstore/grainstore.edn` to discover modules and tasks:

```clojure
;; Auto-loaded tasks from grainstore modules
{:modules
 {:grainicons
  {:tasks
   {:install "Install icon from library"
    :generate "Generate PNG sizes from SVG"
    :list "List available icons"}}
  
  :graincasks
  {:tasks
   {:install "Install AppImage cask"
    :update "Update installed casks"
    :list "List installed casks"}}
  
  :graindisplay
  {:tasks
   {:info "Show display information"
    :set-scaling "Set text scaling factor"
    :nightlight "Toggle night light"}}}}
```

## Migration from `bb` to `gb`

Grainbarrel is **backward compatible** - existing `bb.edn` files work as-is:

```bash
# Old way (still works)
cd grainstore/grainicons
bb icon:install cursor

# New way (works from anywhere)
gb icon:install cursor

# Both commands are equivalent!
```

No changes needed to your existing `bb.edn` files.

## `gb` Command Quick Reference

### Module Management
```bash
gb grainstore:list          # List modules
gb grainstore:update        # Update all
gb grainstore:verify        # Verify dependencies
gb grainstore:doc           # Generate docs
```

### Icon Management (Grainicons)
```bash
gb icon:install cursor      # Install icon
gb icon:generate cursor     # Generate PNGs
gb icon:list                # List icons
gb icon:cache:clear         # Clear cache
```

### Cask Management (Graincasks)
```bash
gb cask:install cursor      # Install AppImage
gb cask:update              # Update all casks
gb cask:list                # List installed
gb cask:uninstall cursor    # Remove cask
```

### Display Management (GrainDisplay)
```bash
gb display:info             # Show display info
gb display:set-scaling 1.5  # Set scaling
gb display:nightlight 2000  # Set color temp
gb display:profile reading  # Switch profile
```

### System Utilities
```bash
gb nightlight:enable        # Enable night light
gb nightlight:status        # Check status
gb wifi:status              # Check wifi
gb wifi:switch              # Switch connection
gb daemon:status            # Check daemons
```

## Why "Grainbarrel"?

- **Grain** = Grain Network branding
- **Barrel** = Container/storage (like Babashka's pod/barrel concept)
- **`gb`** = Short, memorable, available command name
- **Barrels store grain** = Metaphor for grainstore modules

## Comparison with Other Build Tools

| Tool | Language | Speed | Use Case | Grain Network |
|------|----------|-------|----------|---------------|
| Make | Shell | Fast | C/Unix builds | ❌ Not Clojure |
| Leiningen | Clojure | Slow startup | JVM projects | ❌ Too slow |
| Babashka | Clojure | Very fast | Scripting | ✅ Perfect! |
| Grainbarrel | Clojure | Very fast | Grain Network | 🌾 **This!** |

## Development

Grainbarrel is written in Babashka Clojure:

```clojure
;; src/grainbarrel/core.clj
(ns grainbarrel.core
  (:require [babashka.tasks :as tasks]))

(defn discover-modules
  "Discover all grainstore modules and their tasks"
  []
  (-> (slurp "grainstore/grainstore.edn")
      (edn/read-string)
      :grainstore
      :modules))

(defn run-task
  "Run a namespaced task from any module"
  [task-name & args]
  (let [[module task] (str/split task-name #":")]
    (tasks/run-task (keyword module) (keyword task) args)))
```

## Future Enhancements

- **Task Composition**: Chain tasks together
- **Parallel Execution**: Run multiple tasks in parallel
- **Watch Mode**: Auto-run tasks on file changes
- **Remote Execution**: Run tasks on remote Grain Network nodes
- **Task Templates**: Reusable task patterns
- **Plugin System**: Extend Grainbarrel with custom modules

## License

MIT License - See LICENSE file for details

## Related Projects

- **Babashka**: The underlying Clojure scripting engine
- **grainstore**: Module and dependency management
- **graincasks**: AppImage package manager
- **grainicons**: Icon management library
- **graindisplay**: Display configuration system

---

**Part of the Grain Network** - Global Renewable technology for a sustainable future 🌾

*"From `bb` to `gb` - Grain Network builds with barrels of Clojure."*

