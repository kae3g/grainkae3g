# Grainstore Reorganization Plan

**Date**: October 23, 2025 (12025-10-23)  
**Session**: 810  
**Goal**: Clean separation of template (grainpbc) and personal (kae3g) modules

---

## 🎯 Objective

Reorganize the grainstore to have a clear directory structure:
- `grainstore/grainpbc/{module}/` - Template modules for Grain PBC
- `grainstore/kae3g/{grainkae3gmodule}/` - Personal kae3g modules

This improves:
- **Clarity**: Obvious separation between template and personal code
- **Maintainability**: Easier to manage and version control
- **Scalability**: Other grain devs can create their own `grainstore/{graindevname}/` directories

---

## 📁 Current Structure

```
grainstore/
├── grainpbc/                           ← Already exists (partial)
│   ├── graincourse-sync/
│   ├── graincourse-title-abbrev/
│   ├── grainsource-personalize/
│   └── BRANDING-GUIDELINES.md
├── grain-metatypes/                    ← Template module (needs move)
├── grain-nightlight/                   ← Template module (needs move)
├── grain6/                             ← Template module (needs move)
├── grainbarrel/                        ← Template module (needs move)
├── graincourse/                        ← Template module (needs move)
├── graintime/                          ← Template module (needs move)
└── ... (many more template modules)
```

---

## 📁 Target Structure

```
grainstore/
├── grainpbc/                           ← Template modules
│   ├── grain-metatypes/
│   ├── grain-nightlight/
│   ├── grain6/
│   ├── grainbarrel/
│   ├── graincourse/
│   ├── graincourse-sync/
│   ├── graincourse-title-abbrev/
│   ├── grainsource/
│   ├── grainsource-personalize/
│   ├── grainsource-separation/
│   ├── graintime/
│   ├── grainregistry/
│   ├── grainpages/
│   ├── graindisplay/
│   ├── graindaemon/
│   ├── grainwifi/
│   ├── grainzsh/
│   ├── grainenvvars/
│   ├── grainicons/
│   ├── graincasks/
│   └── ... (all grain* template modules)
├── kae3g/                              ← Personal kae3g modules
│   ├── grainkae3gtime/                 ← Personal graintime config
│   ├── grainkae3gcourse/               ← Personal course content
│   ├── grainkae3gdisplay/              ← Personal display config
│   ├── grainkae3gdaemon/               ← Personal daemon config
│   └── ... (personal configs)
├── clojure-s6/                         ← External dependencies (unchanged)
├── clojure-sixos/
├── clojure-icp/
├── clotoko/
├── docs/                               ← Documentation (unchanged)
├── equivalence/                        ← Formal proofs (unchanged)
├── specs/                              ← Specifications (unchanged)
└── grainstore.edn                      ← Manifest (update paths)
```

---

## 🔀 Migration Steps

### Phase 1: Create New Directories
1. ✅ `mkdir -p grainstore/kae3g/`
2. ✅ Existing `grainstore/grainpbc/` already has some modules

### Phase 2: Move Template Modules to `grainpbc/`
Move all `grain*` modules to `grainstore/grainpbc/`:

**High Priority** (used in current workflow):
- [ ] `graintime` → `grainpbc/graintime`
- [ ] `graincourse` → `grainpbc/graincourse`
- [ ] `grainbarrel` → `grainpbc/grainbarrel`
- [ ] `grainsource` → `grainpbc/grainsource`
- [ ] `graindisplay` → `grainpbc/graindisplay`
- [ ] `graindaemon` → `grainpbc/graindaemon`
- [ ] `grainwifi` → `grainpbc/grainwifi`
- [ ] `grainzsh` → `grainpbc/grainzsh`
- [ ] `grainenvvars` → `grainpbc/grainenvvars`
- [ ] `grainpages` → `grainpbc/grainpages`
- [ ] `grainregistry` → `grainpbc/grainregistry`
- [ ] `grain6` → `grainpbc/grain6`

**Medium Priority**:
- [ ] `grain-metatypes` → `grainpbc/grain-metatypes`
- [ ] `grain-nightlight` → `grainpbc/grain-nightlight`
- [ ] `grainicons` → `grainpbc/grainicons`
- [ ] `graincasks` → `grainpbc/graincasks`
- [ ] `grainwriter` → `grainpbc/grainwriter`
- [ ] `grainmusic` → `grainpbc/grainmusic`
- [ ] `grainlexicon` → `grainpbc/grainlexicon`
- [ ] `grainneovedic` → `grainpbc/grainneovedic`
- [ ] `grainweb` → `grainpbc/grainweb`
- [ ] `grainspace` → `grainpbc/grainspace`

**Low Priority** (design docs, future modules):
- [ ] `grainaltproteinproject` → `grainpbc/grainaltproteinproject`
- [ ] `grainconv` → `grainpbc/grainconv`
- [ ] `graindrive` → `grainpbc/graindrive`
- [ ] `graindroid` → `grainpbc/graindroid`
- [ ] `grainphotos` → `grainpbc/grainphotos`
- [ ] `grainsource-gnome` → `grainpbc/grainsource-gnome`
- [ ] `grainsource-sway` → `grainpbc/grainsource-sway`

### Phase 3: Create Personal kae3g Modules
Create `grainstore/kae3g/` modules for personal configurations:

- [ ] `grainstore/kae3g/grainkae3gtime/` - Personal graintime location config
- [ ] `grainstore/kae3g/grainkae3gcourse/` - Personal course content
- [ ] `grainstore/kae3g/grainkae3gdisplay/` - Personal display warmth settings
- [ ] `grainstore/kae3g/grainkae3gdaemon/` - Personal daemon configs
- [ ] `grainstore/kae3g/grainkae3gzsh/` - Personal zsh configs
- [ ] `grainstore/kae3g/grainkae3genvvars/` - Personal env vars (API keys, etc.)

### Phase 4: Update Symlinks
Update existing symlinks to point to new locations:

- [ ] `grainresolver` → `grainpbc/grainregistry`
- [ ] `grainsix` → `grainpbc/grain6`
- [ ] `graintypo` → `grainpbc/grainregistry`

### Phase 5: Update `grainstore.edn` Manifest
Update all module paths in `grainstore.edn`:

```clojure
{:modules
 {:grainpbc/graintime {:path "grainstore/grainpbc/graintime"
                       :remote "https://github.com/grainpbc/graintime"
                       :type :template}
  :grainpbc/graincourse {:path "grainstore/grainpbc/graincourse"
                         :remote "https://github.com/grainpbc/graincourse"
                         :type :template}
  :kae3g/grainkae3gtime {:path "grainstore/kae3g/grainkae3gtime"
                         :remote "https://github.com/kae3g/grainkae3gtime"
                         :type :personal}
  ;; ... etc
  }}
```

### Phase 6: Update `gb` (grainbarrel) Pipelines
Update `grainbarrel/scripts/` to use new paths:

- [ ] Update `grainbarrel/bb.edn` tasks
- [ ] Update `grainbarrel/scripts/module_discovery.bb`
- [ ] Update `grainbarrel/scripts/dependency_graph.bb`
- [ ] Update `grainbarrel/scripts/flow.bb`

### Phase 7: Update Documentation
- [ ] Update `grainstore/README.md` with new structure
- [ ] Update `grainstore/MODULES.md` with grainpbc/kae3g organization
- [ ] Update `grainpbc/grainsource-personalize/README.md` for new paths
- [ ] Update `grainpbc/grainsource-separation/README.md` for new structure

### Phase 8: Testing
- [ ] Test `gb` commands with new paths
- [ ] Test `gt` (graintime) from new location
- [ ] Test graincourse-sync from new location
- [ ] Verify all symlinks point correctly
- [ ] Run grainstore manifest tests

---

## 🔧 Implementation Commands

### Move Template Modules (Example)
```bash
# High priority modules
git mv grainstore/graintime grainstore/grainpbc/graintime
git mv grainstore/graincourse grainstore/grainpbc/graincourse
git mv grainstore/grainbarrel grainstore/grainpbc/grainbarrel
# ... etc

# Update symlinks
cd grainstore
rm grainsix grainresolver graintypo
ln -s grainpbc/grain6 grainsix
ln -s grainpbc/grainregistry grainresolver
ln -s grainpbc/grainregistry graintypo
```

### Create Personal Modules
```bash
mkdir -p grainstore/kae3g/grainkae3gtime
mkdir -p grainstore/kae3g/grainkae3gcourse
mkdir -p grainstore/kae3g/grainkae3gdisplay
# ... etc
```

---

## ✅ Benefits

1. **Clear Separation**: Template vs Personal code is obvious
2. **Scalability**: Other developers can create `grainstore/{their-devname}/`
3. **Maintainability**: Easier to version control and deploy
4. **Modularity**: Clean dependency graph between grainpbc and personal modules
5. **Documentation**: Self-documenting directory structure

---

## 🚨 Breaking Changes

- **Path changes**: All `grainstore/grain*` paths become `grainstore/grainpbc/grain*`
- **Import statements**: May need updates in Clojure code
- **Shell scripts**: May need path updates (`gb`, `gt`, etc.)
- **CI/CD**: GitHub Actions and Woodpecker configs may need updates

---

**Status**: 📝 Planning Phase  
**Next Step**: Execute Phase 1 & 2 (create directories and move modules)

---

**Session Graintime**: `12025-10-23--0122--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g`

