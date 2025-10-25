# QB - Universal Quarterback for Grain Network

QB is the universal abstraction of `gb` (grainbarrel) that provides quarterback functionality across all platforms and package managers.

## Philosophy

Since `quarterback` is only available via NPM (JavaScript ecosystem), our Grain Network `qb` module provides a universal abstraction that works everywhere:

- ✅ **Universal**: Works on all platforms (Alpine, NixOS, Ubuntu, macOS)
- ✅ **Multi-package**: Integrates with all package managers (apk, nix, brew, apt)
- ✅ **Template/Personal**: Separated configuration system
- ✅ **Grain Network**: Native integration with gb and other modules

## Installation

```bash
# Via Grain Network (recommended)
bb qb:help

# Direct installation (if available)
npm install quarterback  # JavaScript only
```

## Quick Start

```bash
# Show qb help
bb qb:help

# Complete deployment flow
bb qb:flow "Session 815: Universal quarterback deployment"

# Show status
bb qb:status

# Sync with Grain Network
bb qb:grain:sync
```

## Features

### Core Commands
- `qb:flow` - Complete deployment workflow
- `qb:status` - Show current status
- `qb:config:show` - Display configuration
- `qb:config:update` - Update settings

### Template/Personal Separation
- `qb:template:sync` - Sync template to personal
- `qb:personal:backup` - Backup personal config
- `qb:personal:restore` - Restore from backup

### Deployment Workflows
- `qb:deploy:github` - Deploy to GitHub Pages
- `qb:deploy:codeberg` - Deploy to Codeberg Pages
- `qb:deploy:dual` - Dual deploy to both

### Grain Network Integration
- `qb:grain:sync` - Sync with Grain Network modules
- `qb:grain:status` - Show integration status

## Configuration

The `qb` module uses template/personal separation:

```
grain6pbc/qb/
├── template/          # Default configuration
│   ├── config.edn
│   └── workflows/
├── personal/          # Personal overrides
│   ├── config.edn
│   └── custom/
└── scripts/           # Babashka scripts
```

## Integration with GB

QB is designed to work seamlessly with `gb` (grainbarrel):

```bash
# GB commands
bb gb:flow "message"
bb gb:status

# QB commands (abstraction layer)
bb qb:flow "message"
bb qb:status
```

## Multi-Platform Support

QB works across all Grain Network target platforms:

- **Alpine Linux**: `apk` package manager
- **NixOS**: `nixpkgs` integration
- **Ubuntu/Debian**: `apt` package manager
- **macOS**: `brew` package manager

## Philosophy

> "The way that can be spoken is not the eternal way. The name that can be named is not the eternal name." - Lao Tzu

But we must still **go** forward, building the Grain Network one sheaf at a time, one moment at a time, in the eternal now.

**now == next + 1** 🌾

