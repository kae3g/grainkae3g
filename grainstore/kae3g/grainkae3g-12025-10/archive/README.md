# Archive Directory

This directory contains historical and non-essential files that were moved during repository cleanup to create a cleaner, more focused structure.

## Contents

### `/docs/` - Historical Documentation
- `BUILD-ROADMAP.md` - Original build roadmap
- `CHANGELOG.md` - Project changelog
- `EDUCATIONAL-SERIES.md` - Educational content series
- `PIPELINE-SUMMARY.md` - Pipeline documentation
- `RECURSION-PROMPT-TEMPLATE.md` - Prompt templates
- `SECRETS-SETUP.md` - Secrets management docs
- `STATUS.md` - Project status
- `THEME-SYSTEM.md` - Theme system documentation
- `TODO.md` - Historical TODO items

### `/education/` - Educational Content
- Foundation materials and advanced practices
- Code review documentation
- Learning resources

### `/examples/` - Example Files
- Sample wrapped output examples
- Reference implementations

### `/guides/` - User Guides
- Getting started guides
- Architecture documentation
- Advanced usage instructions

### `/nixos/` - NixOS Configuration
- VM configuration files
- Nix-specific setup

### `/scripts/` - Historical Scripts
- VM connection scripts
- GitHub setup utilities
- Legacy test scripts

### `/src/` - Legacy Source Code
- Original ClojureScript components
- Advanced pipeline generators
- Theme system files

### `/web-app-routes/` - Historical Svelte Routes
- Old documentation and guide routes (20+ files)
- ThemeToggle component
- Legacy index and test document routes
- All files from previous "robotic farm" branding

## Active Repository Structure

The cleaned repository now focuses on:

```
kae3g/
├── writings/           # Core content (38 files)
├── docs/              # Essential documentation (2 files)
├── web-app/           # SvelteKit website
├── scripts/           # Active build scripts
├── bb.edn            # Babashka configuration
├── README.md         # Main documentation
└── archive/          # This directory
```

## Restoration

If you need any archived files, they can be easily restored:

```bash
# Move specific files back
mv archive/docs/BUILD-ROADMAP.md ./

# Move entire directories back
mv archive/guides ./
```

The archive preserves the complete history while maintaining a clean, focused repository structure.
