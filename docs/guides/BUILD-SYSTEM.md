# Build System Architecture

**Philosophy**: Incremental precision over wholesale regeneration  
**Strategy**: Cache everything, rebuild only what changed  
**Goal**: Sub-second builds for typical edits

---

## Overview

```
Markdown Source → Build Cache → JSON Artifacts → SvelteKit → Static HTML
   (git)          (local only)    (gitignored)     (build)      (pages)
```

---

## Two-Tier Build System

### Tier 1: Content Pipeline (Markdown → JSON)

**Input**: `writings/*.md` (98 files)  
**Output**: `web-app/static/content/*.json`  
**Cache**: `.build-cache.edn`

**Full Build** (`bb writings:build`):
```clojure
(defn build-all []
  (for [file (glob "writings/*.md")]
    (markdown→json file)))
```
- Rebuilds all files
- No cache lookup
- ~5 seconds

**Incremental Build** (`bb writings:build-fast`):
```clojure
(defn build-incremental []
  (let [cache (load-cache)]
    (for [file (glob "writings/*.md")]
      (if (changed? file cache)
        (markdown→json file)      ; Rebuild
        (load-from-cache file))))) ; Reuse
```
- Checks file hash (mtime + size)
- Only rebuilds changed files
- Loads unchanged from cache
- ~1 second for typical edits

### Tier 2: Site Pipeline (JSON → HTML)

**Input**: `web-app/static/content/*.json`  
**Output**: `web-app/build/` (static HTML)  
**Tool**: SvelteKit + Vite

```bash
npm run build
# → vite build
# → svelte-kit build
# → static HTML in build/
```

---

## Cache Strategy

### Hash Function
```clojure
(defn file-hash [file]
  (str (last-modified-time file) "-" (file-size file)))
```

**Why this works**:
- ✅ Fast (no content hashing)
- ✅ Reliable (any change = new hash)
- ✅ Cross-platform (fs metadata)

**Limitation**:
- ❌ False negative: `touch` invalidates cache even if no content changed
- **Solution**: Acceptable tradeoff for speed

### Cache Structure
```clojure
{"/path/to/file.md" {:hash "1728691234-5678"
                     :meta {:slug "essay-name"
                            :title "Essay Title"}}
 ...}
```

### Cache Lifecycle
```
File changed? → NO  → Load from cache
              ↓ YES
           Process → Update cache → Save to disk
```

---

## Git Strategy

### What's Tracked
```
writings/*.md              ✅ Source of truth
web-app/static/content/.gitkeep  ✅ Ensures directory exists
web-app/static/content/*.json    ❌ Build artifacts
.build-cache.edn           ❌ Local cache
```

### Why Ignore JSON?

**Before** (JSON tracked):
```bash
$ git status
modified: writings/essay.md
modified: web-app/static/content/essay.json
modified: web-app/static/content/index.json
# 3 files for 1 change!
```

**After** (JSON ignored):
```bash
$ git status
modified: writings/essay.md
# 1 file for 1 change ✨
```

**Benefits**:
- Cleaner diffs (only see source changes)
- Smaller repo (no generated content)
- Single source of truth (markdown canonical)
- Regenerated on deploy (CI builds fresh)

---

## CI/CD Integration

### CI Build Flow
```yaml
steps:
  - checkout
  - install:
      - npm ci
      - bb install (built-in)
  - build-content:
      - bb writings:build-fast  # Uses empty cache (rebuilds all)
  - build-site:
      - npm run build
  - deploy:
      - push to pages branch
```

### Why `.gitkeep`?
```bash
# Without .gitkeep:
$ git clone repo
$ ls web-app/static/
# content/ directory DOES NOT EXIST

# With .gitkeep:
$ git clone repo
$ ls web-app/static/content/
.gitkeep  # Directory exists!
```

**Critical for CI**: SvelteKit scans `content/` directory during build. If it doesn't exist → `ENOENT` error.

---

## Performance Metrics

### Build Times (98 files)

| Scenario | Full Build | Incremental | Speedup |
|----------|-----------|-------------|---------|
| **Cold start** (no cache) | 5.0s | 5.0s | 1x |
| **Hot run** (no changes) | 5.0s | 0.5s | **10x** |
| **1 file changed** | 5.0s | 0.6s | **8x** |
| **5 files changed** | 5.0s | 1.2s | **4x** |

### Scaling Projections

| Files | Full Build | Incremental (1% changed) | Speedup |
|-------|-----------|-------------------------|---------|
| 100 | 5s | 0.5s | 10x |
| 500 | 25s | 1.0s | 25x |
| 1000 | 50s | 1.5s | 33x |

**Conclusion**: Incremental builds scale better as content grows.

---

## Build Commands Reference

### Content Pipeline
```bash
# Fast (incremental)
bb writings:build-fast

# Full (clean rebuild)
bb writings:build

# Watch mode (auto-rebuild)
bb writings:watch

# Validate only (no build)
bb writings:validate
```

### Site Pipeline
```bash
# Development server
cd web-app && npm run dev

# Production build
cd web-app && npm run build

# Preview production build
cd web-app && npm run preview
```

### Combined Workflows
```bash
# Precision flow (build + commit + push + deploy)
bb flow "message"

# Full deployment (no commit)
bb deploy:full

# Deploy components
bb deploy:build-content    # Markdown → JSON
bb deploy:build-site       # JSON → HTML
bb deploy:pages            # Push to pages branch
```

---

## Error Handling

### Build Failures

**Markdown Parse Error**:
```bash
❌ Error processing 9298.md: Invalid front matter
# Flow stops - nothing committed
```

**Missing Dependencies**:
```bash
❌ Error: clj-yaml not found
$ bb install  # Re-installs dependencies
```

**Cache Corruption**:
```bash
⚠️  Cache corrupted, rebuilding all...
$ rm .build-cache.edn
$ bb writings:build-fast
```

### Recovery Strategies

| Error | Solution |
|-------|----------|
| Parse error | Fix markdown, retry |
| Cache corrupted | Delete cache, rebuild |
| Directory missing | Check `.gitkeep` exists |
| CI fails | Check logs, test locally |

---

## Implementation Details

### File Structure
```
scripts/
├── writings_build.clj              # Full build implementation
├── writings_build_incremental.clj  # Incremental build implementation
└── deploy-to-pages.bb              # Deployment orchestration

bb.edn                              # Task definitions
.gitignore                          # Build artifacts
```

### Key Functions

**`writings_build_incremental.clj`**:
```clojure
(defn file-hash [file])           ; Compute file hash
(defn get-changed-files [files cache])  ; Detect changes
(defn process-markdown-file [file])     ; Parse & convert
(defn build-content-index [entries])    ; Generate index.json
(defn build-writings-incremental [])    ; Main entry point
```

**Cache Operations**:
```clojure
(defn load-cache [])              ; Read .build-cache.edn
(defn save-cache [cache])         ; Write .build-cache.edn
```

---

## Future Improvements

### Potential Optimizations
- [ ] Content-based hashing (more accurate cache invalidation)
- [ ] Parallel builds (process multiple files simultaneously)
- [ ] Partial index rebuilds (only update changed entries)
- [ ] Build metrics (track performance over time)

### Scalability Considerations
- [ ] Database for large essay collections (1000+)
- [ ] Distributed builds for massive parallelism
- [ ] CDN integration for faster deploys
- [ ] Incremental SvelteKit builds

---

## Debugging

### Enable Verbose Logging
```clojure
; Add to scripts/writings_build_incremental.clj
(defn process-markdown-file [file]
  (println "Processing:" file)  ; Debug output
  ...)
```

### Inspect Cache
```bash
cat .build-cache.edn | head -20
```

### Test Single File
```bash
bb -e "
(require '[markdown.core :as md])
(require '[clj-yaml.core :as yaml])
(println (md/md-to-html-string (slurp \"writings/9298.md\")))
"
```

### Compare Build Modes
```bash
# Time full build
time bb writings:build

# Time incremental build (cold)
rm .build-cache.edn
time bb writings:build-fast

# Time incremental build (hot)
time bb writings:build-fast
```

---

## Architecture Decisions

### Why Babashka?
- **Fast**: Native binary, instant startup
- **Clojure**: Functional, data-oriented
- **Batteries**: fs, shell, yaml, json built-in
- **Lightweight**: ~50MB vs 200MB+ JVM

### Why File-Based Cache?
- **Simple**: EDN file, human-readable
- **Fast**: Read/write with `slurp`/`spit`
- **Portable**: Works everywhere
- **Gitignored**: Local only, not shared

### Why Hash = mtime + size?
- **Fast**: No content reading required
- **Reliable**: Any change triggers rebuild
- **Simple**: Two filesystem calls
- **Cross-platform**: Works on all OSes

### Why Ignore JSON?
- **Single source**: Markdown is canonical
- **Clean git**: Only source changes tracked
- **Regenerable**: Can rebuild anytime
- **CI-friendly**: Fresh builds every deploy

---

*Frozen specifications. Incremental precision. Permafrost performance.* ❄️

