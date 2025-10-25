# GrainClay Cleanup

**Time-aware cleanup utility for Grain Network repositories**

Systematically removes build artifacts, temporary files, and old content using `grain6` timer-based scheduling.

---

## Philosophy

**"From clay to grain, shape and refine."**

GrainClay Cleanup embodies the Grain Network philosophy of:
- **Impermanence**: Recognizing what is temporary vs. permanent
- **Cyclical Time**: Using astronomical cycles (via `grain6`) for cleanup schedules
- **Clarity**: Keeping repositories clean and focused
- **Sovereignty**: User control over what stays and what goes

Inspired by:
- **Urbit Behn**: Timer-based process management
- **Unix tmpwatch**: Time-based file cleanup
- **Nix garbage collection**: Systematic cleanup with retention policies

---

## Features

### Core Cleanup Operations

1. **Build Artifacts**
   - `web-app/build/`
   - `web-app/.svelte-kit/`
   - `build/`
   - `.cache/`
   - Node modules (with confirmation)

2. **Temporary Files**
   - `*.tmp`
   - `*.log` (older than N days)
   - `.DS_Store`
   - `Thumbs.db`
   - Editor backups (`*~`, `*.swp`)

3. **Old Content**
   - Unused grainpaths (keep N most recent)
   - Old course versions (by graintime)
   - Stale symlinks
   - Orphaned assets

4. **Git Cleanup**
   - Unreferenced branches
   - Orphaned git objects
   - Old stashes

### Time-Aware Scheduling (grain6)

```clojure
;; Cleanup based on astronomical cycles
{:daily-cleanup    {:schedule :sunrise
                    :task :temp-files}
 :weekly-cleanup   {:schedule :new-moon
                    :task :build-artifacts}
 :monthly-cleanup  {:schedule :full-moon
                    :task :old-content}
 :seasonal-cleanup {:schedule :equinox
                    :task :deep-clean}}
```

### Retention Policies

```edn
{:build-artifacts    {:max-age-days 1}
 :log-files          {:max-age-days 7}
 :temp-files         {:max-age-days 3}
 :course-versions    {:keep-count 5}
 :grainpaths         {:keep-count 10}
 :git-branches       {:keep-active-only true}}
```

---

## Installation

### 1. Template Setup
```bash
cd grainstore/grain6pbc/grainclay-cleanup
cp template/cleanup.edn.template personal/cleanup.edn
```

### 2. Configure Policies
Edit `personal/cleanup.edn`:
```edn
{:enabled true
 :dry-run false  ; Set true for testing
 :policies {...}
 :schedules {...}}
```

### 3. Add to grain6 Service
```bash
bb grain6:add-cleanup-service
```

---

## Usage

### Manual Cleanup

```bash
# Quick cleanup (build artifacts + temp files)
bb cleanup:quick

# Full cleanup (everything)
bb cleanup:full

# Dry run (show what would be deleted)
bb cleanup:dry-run

# Specific targets
bb cleanup:builds
bb cleanup:temps
bb cleanup:old-content
bb cleanup:git
```

### Scheduled Cleanup (via grain6)

```bash
# Start cleanup daemon
bb grain6:start

# Check cleanup schedule
bb cleanup:schedule

# View cleanup logs
bb cleanup:logs
```

### Interactive Mode

```bash
bb cleanup:interactive
```
Asks for confirmation before each deletion category.

---

## Configuration

### Template: `template/cleanup.edn.template`

```edn
{:enabled true
 :dry-run true  ; Safe default
 
 ;; Retention policies
 :policies
 {:build-artifacts {:max-age-days 1}
  :log-files {:max-age-days 7}
  :temp-files {:max-age-days 3}
  :course-versions {:keep-count 5}
  :grainpaths {:keep-count 10}}
 
 ;; Cleanup schedules (grain6 integration)
 :schedules
 {:daily {:time :sunrise
          :tasks [:temp-files :log-files]}
  :weekly {:time :new-moon
           :tasks [:build-artifacts]}
  :monthly {:time :full-moon
            :tasks [:old-content :git-cleanup]}}
 
 ;; Paths to protect (never delete)
 :protected-paths
 ["docs/"
  "grainstore/"
  "writings/"
  ".git/"
  "README.md"
  "LICENSE"]
 
 ;; Patterns to always clean
 :always-clean
 ["*.tmp"
  "*.log"
  ".DS_Store"
  "Thumbs.db"
  "*~"
  "*.swp"]}
```

### Personal: `personal/cleanup.edn`

User-specific overrides (gitignored).

---

## Integration with grain6

### Timer-Based Execution

GrainClay uses `grain6` (Urbit Behn-inspired) for astronomical scheduling:

```clojure
(ns grainclay-cleanup.scheduler
  (:require [grain6.timer :as timer]
            [graintime.core :as graintime]))

(defn schedule-cleanup
  "Schedule cleanup based on astronomical events"
  [config]
  (let [schedule (:schedules config)]
    ;; Daily at sunrise
    (timer/at-sunrise
      (fn [] (run-cleanup (:daily schedule))))
    
    ;; Weekly at new moon
    (timer/at-moon-phase :new
      (fn [] (run-cleanup (:weekly schedule))))
    
    ;; Monthly at full moon
    (timer/at-moon-phase :full
      (fn [] (run-cleanup (:monthly schedule))))))
```

### Logging with Graintime

All cleanup operations logged with graintime:

```
12025-10-23--0200--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g | Cleanup: Removed 127 temp files (342 KB)
12025-10-23--0200--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g | Cleanup: Removed 3 old course versions
12025-10-23--0200--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g | Cleanup: Total freed: 15.3 MB
```

---

## Safety Features

### 1. Dry Run Mode
Always test first:
```bash
bb cleanup:dry-run
```

### 2. Protected Paths
Hardcoded protection for critical directories.

### 3. Confirmation Prompts
Interactive mode asks before each deletion.

### 4. Backup Before Delete
Optional: Create tarball of files before deletion.

### 5. Undo Log
Maintains log of all deletions for recovery.

---

## Examples

### Example 1: Clean Build Artifacts
```bash
$ bb cleanup:builds

🧹 GrainClay Cleanup - Build Artifacts

Scanning...
  ✓ web-app/build/ (15.2 MB)
  ✓ web-app/.svelte-kit/ (8.7 MB)
  ✓ build/ (2.1 MB)

Total: 26.0 MB to be removed

Proceed? [y/N] y

Removing...
  ✓ web-app/build/
  ✓ web-app/.svelte-kit/
  ✓ build/

✨ Cleanup complete! Freed 26.0 MB
```

### Example 2: Scheduled Daily Cleanup
```bash
$ bb grain6:start

🌅 grain6 daemon starting...
📅 Next cleanup: tomorrow at sunrise (06:47 PDT)

# Next day at sunrise:
🧹 GrainClay: Daily cleanup starting...
  ✓ Removed 47 temp files (1.2 MB)
  ✓ Removed 12 log files (340 KB)
✨ Daily cleanup complete
```

### Example 3: Old Content Cleanup
```bash
$ bb cleanup:old-content --keep 5

🧹 GrainClay Cleanup - Old Content

Course versions found: 12
Keeping: 5 most recent
Removing: 7 older versions

Versions to remove:
  • /course/kae3g/intro/12025-09-15--.../ (30 days old)
  • /course/kae3g/intro/12025-09-20--.../ (25 days old)
  ...

Proceed? [y/N] y

✨ Removed 7 old course versions (125 MB freed)
```

---

## Architecture

### Directory Structure
```
grain6pbc/grainclay-cleanup/
├── src/
│   └── grainclay_cleanup/
│       ├── core.clj           # Main cleanup logic
│       ├── scheduler.clj      # grain6 integration
│       ├── policies.clj       # Retention policy engine
│       ├── scanner.clj        # File scanning
│       └── logger.clj         # Graintime logging
├── template/
│   └── cleanup.edn.template   # Default config
├── personal/
│   └── cleanup.edn            # User config (gitignored)
├── bb.edn                     # Babashka tasks
└── README.md
```

### Dependencies
- `grain6`: Timer scheduling
- `graintime`: Timestamp logging
- `babashka.fs`: File operations
- `babashka.process`: Shell commands

---

## Roadmap

- [ ] Basic cleanup operations
- [ ] grain6 scheduler integration
- [ ] Interactive mode
- [ ] Dry-run mode
- [ ] Backup before delete
- [ ] Undo log
- [ ] Git cleanup operations
- [ ] Custom retention policies
- [ ] Cleanup reports/statistics
- [ ] Email notifications (optional)

---

## Philosophy: The Clay-Grain Cycle

```
Clay → Shape → Grain → Refine → Clay
  ↑                               ↓
  └───────── Cleanup ─────────────┘
```

**Clay**: Raw, temporary, malleable (build artifacts, temp files)  
**Grain**: Refined, permanent, structured (source code, docs)  
**Cleanup**: The cyclical return, making space for new creation

Just as farmers harvest grain and return chaff to the soil, GrainClay Cleanup removes temporary artifacts while preserving what matters.

---

**Template/Personal Split**: ✅  
**grain6 Integration**: 🔄 In Progress  
**Status**: 🌱 Seedling

**Session**: `12025-10-23--0132--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g`

