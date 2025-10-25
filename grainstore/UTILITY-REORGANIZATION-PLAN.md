# Utility Repository Reorganization Plan

## 🎯 Goal
Reorganize utility repositories to create a clear separation between:
1. **Personal Grainstore** (`/home/xy/kae3g/grainkae3g/grainstore/`) - Personal development
2. **Grain6PBC Public Templates** (`grain6pbc/` GitHub org) - Public templates

## 📊 Current State Analysis

### Utilities Currently in `clotoko-icp/`:
- `clelte.bb` - Clelte compiler script
- `clotoko.bb` - Clotoko compiler script  
- `poshmark-scraper.bb` - Poshmark scraping utility
- `oracle-daemon-plan.md` - Oracle daemon planning
- `grainthrift-*.bb` - GrainThrift marketplace scripts
- `simple-demo.bb` - Demo scripts

### Utilities Currently in `grain6pbc-utils/`:
- `clelte/` - Clelte compiler (organized)
- `clotoko/` - Clotoko compiler (organized)
- `poshmark-scraper/` - Poshmark scraper (organized)
- `grainbranch.bb` - Grainbranch utilities
- `graindaemon/` - Daemon utilities
- `grainmode/` - Mode utilities

## 🏗️ Proposed Structure

### Personal Grainstore (`/home/xy/kae3g/grainkae3g/grainstore/`)

#### Core Utilities (Personal Development)
```
grainstore/
├── clelte/                    # Personal Clelte development
│   ├── src/
│   ├── test/
│   └── README.md
├── clotoko/                   # Personal Clotoko development
│   ├── src/
│   ├── test/
│   └── README.md
├── poshmark-scraper/          # Personal scraper development
│   ├── src/
│   ├── test/
│   └── README.md
├── grainthrift/               # Personal marketplace development
│   ├── src/
│   ├── test/
│   └── README.md
├── oracle-daemon/             # Personal oracle development
│   ├── src/
│   ├── test/
│   └── README.md
└── grain6pbc-utils/           # Personal utility management
    ├── grainbranch/
    ├── graindaemon/
    ├── grainmode/
    └── sync-scripts/
```

#### Specialized Repositories
```
grainstore/
├── grainpbc/                  # Core Grain PBC utilities
├── graintime/                 # Time utilities
├── grainneovedic/             # Astrology utilities
├── graincontacts/             # Contact management
├── grainmusic/                # Music utilities
├── grainwriter/               # Writing utilities
└── ... (other specialized repos)
```

### Grain6PBC Public Templates (`grain6pbc/` GitHub org)

#### Template Repositories
```
grain6pbc/
├── clelte-template/           # Public Clelte template
├── clotoko-template/           # Public Clotoko template
├── poshmark-scraper-template/  # Public scraper template
├── grainthrift-template/       # Public marketplace template
├── oracle-daemon-template/      # Public oracle template
├── grainbranch-template/       # Public grainbranch template
├── graindaemon-template/       # Public daemon template
├── grainmode-template/         # Public mode template
└── grain6pbc-utils-template/   # Public utilities template
```

## 🔄 Migration Plan

### Phase 1: Extract Utilities from `clotoko-icp/`
1. Create dedicated repositories for each utility
2. Move utility code to appropriate locations
3. Update dependencies and references

### Phase 2: Organize Personal Grainstore
1. Create clear directory structure
2. Move utilities to dedicated repositories
3. Update cross-references

### Phase 3: Create Public Templates
1. Create template repositories in `grain6pbc/` org
2. Remove personal content from templates
3. Add template documentation

### Phase 4: Set Up Synchronization
1. Create sync scripts between personal and public
2. Set up automated updates
3. Test synchronization process

## 📋 Specific Actions

### Move from `clotoko-icp/` to dedicated repos:
- `clelte.bb` → `grainstore/clelte/src/clelte.bb`
- `clotoko.bb` → `grainstore/clotoko/src/clotoko.bb`
- `poshmark-scraper.bb` → `grainstore/poshmark-scraper/src/scraper.bb`
- `oracle-daemon-plan.md` → `grainstore/oracle-daemon/docs/plan.md`
- `grainthrift-*.bb` → `grainstore/grainthrift/src/`

### Create Public Templates:
- `grain6pbc/clelte-template/` - Clean Clelte template
- `grain6pbc/clotoko-template/` - Clean Clotoko template
- `grain6pbc/poshmark-scraper-template/` - Clean scraper template
- `grain6pbc/grainthrift-template/` - Clean marketplace template
- `grain6pbc/oracle-daemon-template/` - Clean oracle template

### Update Cross-References:
- Update `deps.edn` files
- Update import statements
- Update documentation links
- Update CI/CD pipelines

## 🎯 Benefits

1. **Clear Separation**: Personal development vs. public templates
2. **Better Organization**: Each utility has its own repository
3. **Template Reusability**: Clean templates for public use
4. **Easier Maintenance**: Isolated changes per utility
5. **Better Documentation**: Dedicated docs per utility

## ⚠️ Considerations

1. **Dependencies**: Ensure all dependencies are properly updated
2. **Testing**: Test all utilities after reorganization
3. **Documentation**: Update all documentation and links
4. **CI/CD**: Update GitHub Actions and other automation
5. **Backup**: Ensure all changes are committed before reorganization

## 🚀 Next Steps

1. Create the new directory structure
2. Move utilities to dedicated repositories
3. Update all references and dependencies
4. Create public templates
5. Set up synchronization scripts
6. Test the complete system

