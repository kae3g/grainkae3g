# Babashka Tasks Reference

**Updated**: 2025-10-10  
**Purpose**: Complete reference for all `bb` commands  
**Status**: Current

---

## Quick Reference

```bash
bb tasks             # List all available tasks
bb doctor            # Check toolchain status
bb grainstore:status # Show Grainstore progress
```

---

## Setup & Configuration

### `bb setup:run`
Run initial project setup script.

```bash
bb setup:run
```

**What it does**:
- Checks prerequisites (bb, nix, git, gh)
- Creates necessary directories
- Shows next steps

### `bb config:generate`
Generate site configuration from `.config.edn`.

```bash
bb config:generate
```

**What it does**:
- Reads `.config.edn`
- Generates `web-app/src/lib/config.js`
- Generates `web-app/src/lib/LicenseFooter.svelte`

**Prerequisites**: `.config.edn` must exist (copy from template)

### `bb config:check`
Check if configuration file exists.

```bash
bb config:check
```

---

## Content Management

### `bb writings:build`
Build all markdown essays to JSON for the website.

```bash
bb writings:build
```

**What it does**:
- Scans `writings/` for markdown files
- Parses YAML front matter
- Generates JSON files in `web-app/static/content/`
- Creates index with all essays

### `bb writings:watch`
Auto-rebuild when writings change.

```bash
bb writings:watch
```

**What it does**:
- Watches `writings/` directory
- Automatically runs `writings:build` on changes
- Perfect for development workflow

### `bb writings:validate`
Validate markdown files without building.

```bash
bb writings:validate
```

**What it does**:
- Checks all markdown files
- Reports file counts and line counts
- No JSON generation

### `bb writings:stats`
Show statistics about writings content.

```bash
bb writings:stats
```

**What it does**:
- Counts writings files
- Counts docs files
- Shows total size in KB

---

## Grainstore (Sovereign Dependencies)

### `bb grainstore:status`
Show current Grainstore implementation status.

```bash
bb grainstore:status
```

**Output**:
```
🌱 Grainstore Status:

Specifications:
  ✓ runit.nock.md
  ✓ s6.nock.md

Implementations:
  ✓ s6.clj

Tests:
  ✓ s6_test.clj

✨ Grainstore is growing!
```

### `bb grainstore:test`
Run Grainstore test suite.

```bash
bb grainstore:test
```

**What it does**:
- Runs all tests in `test/grainstore/`
- Currently: s6 supervision tests (65 assertions)
- Reports pass/fail status

### `bb grainstore:specs`
List all Nock specifications.

```bash
bb grainstore:specs
```

**Output**:
```
📦 Grainstore Specifications:
  ✓ runit.nock.md
  ✓ s6.nock.md
```

---

## SixOS Installation

### `bb sixos:build-iso`
Build SixOS installer ISO.

```bash
bb sixos:build-iso
```

**What it does**:
- Builds custom NixOS ISO with s6 (no systemd)
- Includes Framework 16 drivers
- Outputs to `result-iso/`
- Takes 10-30 minutes

### `bb sixos:list-drives`
List available drives for USB flashing.

```bash
bb sixos:list-drives
```

**Output**:
- macOS: Runs `diskutil list`
- Linux: Runs `lsblk`

### `bb sixos:flash-usb <device>`
Flash SixOS ISO to USB drive.

```bash
# macOS
bb sixos:flash-usb /dev/disk4

# Linux
bb sixos:flash-usb /dev/sdb
```

**What it does**:
- Asks for confirmation
- Unmounts drive
- Flashes ISO with dd
- Syncs and ejects

**⚠️ Warning**: Erases all data on target drive!

---

## Development

### `bb doctor`
Check toolchain and prerequisites.

```bash
bb doctor
```

**What it checks**:
- Babashka version
- Node.js availability
- Nix availability
- Git availability
- GitHub CLI (optional)

### `bb clean`
Clean build artifacts.

```bash
bb clean
```

**What it removes**:
- `build/` directory
- `web-app/build/`
- `web-app/.svelte-kit/`

---

## Testing

### `bb test:all`
Run all test suites.

```bash
bb test:all
```

**What it runs**:
- Content pipeline tests
- Link validation tests
- Grainstore tests (when available)

---

## Git & GitHub

### `bb secrets:setup`
Setup secrets file from template.

```bash
bb secrets:setup
```

**What it does**:
- Copies `.secrets.template.edn` to `.secrets.edn`
- You then edit with your actual secrets

### `bb secrets:check`
Check if secrets file exists.

```bash
bb secrets:check
```

### `bb secrets:config-git`
Configure git from secrets.

```bash
bb secrets:config-git
```

**What it does**:
- Reads `.secrets.edn`
- Sets git user.name and user.email
- Configures signing key (if provided)

### `bb gh:check`
Check GitHub CLI authentication.

```bash
bb gh:check
```

### `bb gh:status`
Show git and GitHub repository status.

```bash
bb gh:status
```

**What it shows**:
- `git status`
- `git remote -v`

---

## Nix Integration

### `bb nix:check`
Check if Nix is available.

```bash
bb nix:check
```

### `bb nix:develop`
Instructions for entering Nix dev shell.

```bash
bb nix:develop
```

**Output**:
```
To enter Nix shell, run in your terminal:
   nix develop

Then inside Nix shell:
   bb doctor
   bb build:pipeline
```

### `bb nix:run <task>`
Run a bb task inside Nix shell.

```bash
bb nix:run build:pipeline
```

---

## Legacy/Advanced (May Not Need Often)

### `bb format:check`
Check Clojure formatting.

### `bb format:apply`
Apply Clojure formatting.

### `bb wrap:markdown`
Wrap markdown to 80 characters.

### `bb parse:markdown`
Parse markdown to ClojureScript DSL.

### `bb build:pipeline`
Complete document → Svelte pipeline.

### `bb ci:verify`
Continuous integration verification.

---

## Task Categories Summary

### Essential (Use Daily)
- `bb writings:build` - Build content
- `bb doctor` - Check status
- `bb grainstore:status` - Show progress
- `bb clean` - Clean builds

### Setup (Use Once)
- `bb setup:run` - Initial setup
- `bb config:generate` - Generate config
- `bb secrets:setup` - Setup secrets

### Grainstore (Active Development)
- `bb grainstore:test` - Run tests
- `bb grainstore:specs` - List specs
- `bb grainstore:status` - Show status

### SixOS (Installation)
- `bb sixos:build-iso` - Build ISO
- `bb sixos:list-drives` - List drives
- `bb sixos:flash-usb` - Flash USB

### Utilities
- `bb writings:watch` - Auto-rebuild
- `bb writings:stats` - Statistics
- `bb gh:status` - Git status

---

## Adding New Tasks

Edit `bb.edn` and add to the `:tasks` map:

```clojure
your-task {:doc "Description of what it does"
           :task (shell "your-command" "args")}
```

Then run:
```bash
bb tasks  # Verify it appears
bb your-task  # Test it works
```

---

## See Also

- **bb.edn** - Task definitions
- **README.md** - Project overview
- **setup/README.md** - Setup guide
- **docs/CONFIGURATION-GUIDE.md** - Configuration details

---

**All tasks are designed to be simple, composable, and sovereign.** 🔷✨

