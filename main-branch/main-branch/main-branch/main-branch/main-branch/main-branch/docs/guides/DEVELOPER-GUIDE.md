# Developer Guide: Coldriver Tundra

**Last Updated**: 2025-10-11  
**Branch**: `coldriver-heal`  
**Philosophy**: Permafrost foundations, incremental precision, frozen clarity

---

## Quick Start

### Prerequisites
- **Babashka** (bb) - Clojure scripting runtime
- **Node.js** 20+ - For SvelteKit web app
- **Git** - Version control
- **SSH key** configured for Codeberg

### Clone & Setup
```bash
git clone git@codeberg.org:kae3g/12025-10.git
cd 12025-10
git checkout coldriver-heal

# Install dependencies
cd web-app && npm ci && cd ..

# Build content
bb writings:build-fast

# Start dev server
cd web-app && npm run dev
```

**Site runs at**: http://localhost:5173/12025-10/

---

## Core Workflow: `bb flow`

The **precision flow** command handles the complete workflow:

```bash
bb flow "your commit message"
```

### What it does (5 steps):
1. **🔧 Build content** - Validates markdown → JSON conversion
2. **📦 Stage changes** - `git add -A`
3. **✅ Commit** - Only if build succeeded
4. **🚀 Push** - To `coldriver-heal` branch
5. **🌐 Deploy** - Rebuild + push to Codeberg Pages

### Why build-first?
- **Catches errors early** - Broken markdown stops before commit
- **Clean git history** - Only working code gets committed
- **Fast feedback** - Know immediately if content is valid

---

## Build System

### Two Build Commands

**Fast (Incremental)**:
```bash
bb writings:build-fast
```
- Uses `.build-cache.edn` to track changes
- Only rebuilds modified files
- **~10x faster** for small edits
- Default for `bb flow`

**Full (Clean)**:
```bash
bb writings:build
```
- Rebuilds everything from scratch
- Use when cache is corrupted
- Use after major refactoring

### How Incremental Builds Work

**Cache Strategy**:
- **Hash**: `last-modified-time + file-size`
- **Storage**: `.build-cache.edn` (gitignored)
- **Validation**: Compares current hash vs cached hash
- **Fallback**: Missing cache entries trigger rebuild

**File Flow**:
```
writings/*.md → [changed?] → process → JSON
                    ↓
                 [cached] → load from disk → JSON
```

**Performance**:
- **Cold start**: ~5 seconds (all 98 files)
- **Hot run**: <1 second (0 changed files)
- **Typical edit**: ~1 second (1-5 files changed)

---

## Directory Structure

```
12025-10/
├── writings/              # Markdown source (canonical)
│   ├── *.md              # Essays (9999 → 0000)
│   └── README.md         # Writing index
├── web-app/
│   ├── src/              # SvelteKit app
│   │   ├── routes/       # Pages
│   │   ├── lib/          # Components
│   │   └── app.css       # Global styles
│   ├── static/
│   │   └── content/      # Generated JSON (gitignored)
│   │       ├── .gitkeep  # Ensures dir exists in CI
│   │       └── *.json    # Build artifacts
│   └── package.json
├── scripts/
│   ├── writings_build.clj              # Full build
│   ├── writings_build_incremental.clj  # Smart build
│   └── deploy-to-pages.bb              # Deployment
├── docs/                 # Documentation
├── bb.edn                # Task definitions
└── .gitignore
```

---

## Git Workflow

### Branches
- **`coldriver-heal`** - Main development branch
- **`pages`** - Deployed static site (auto-generated)

### What's Tracked
```bash
# Tracked in git:
✅ writings/*.md              # Source files
✅ web-app/src/**             # App source
✅ web-app/static/content/.gitkeep  # Directory placeholder
✅ scripts/**, docs/**        # Infrastructure

# Ignored (build artifacts):
❌ web-app/static/content/*.json    # Generated from markdown
❌ .build-cache.edn                 # Incremental build cache
❌ web-app/build/                   # SvelteKit output
❌ node_modules/                    # Dependencies
```

### Why JSON is Ignored
- **Single source of truth**: Markdown is canonical
- **Reduces git clutter**: No more "rebuild JSON" commits
- **Regenerated on deploy**: CI builds fresh JSON every time
- **Faster diffs**: Only see markdown changes

---

## CI/CD Pipeline

### Triggers
- **Push to `coldriver-heal`** → Woodpecker CI runs
- **Manual deploy** → `bb deploy:full`

### CI Steps (Codeberg Woodpecker)
```yaml
# Pseudo-config (actual config is in Codeberg UI)
1. Clone repo
2. Install dependencies (npm ci, bb)
3. Build content (bb writings:build-fast)
4. Build SvelteKit (npm run build)
5. Deploy to pages branch
```

### Why `.gitkeep` is Critical
- **Problem**: Empty dirs don't exist in git
- **Symptom**: SvelteKit fails with `ENOENT: scandir content/`
- **Solution**: `.gitkeep` ensures `web-app/static/content/` exists
- **Result**: CI can generate JSON into existing directory

---

## Common Tasks

### Writing a New Essay
```bash
# 1. Create markdown file
touch writings/9297-new-essay.md

# 2. Add front matter
cat > writings/9297-new-essay.md << 'EOF'
---
title: "New Essay Title"
date: 2025-10-11
---

# New Essay Title

Content here...
EOF

# 3. Build and preview
bb writings:build-fast
cd web-app && npm run dev

# 4. Flow when ready
bb flow "feat: add essay 9297 - New Essay Title"
```

### Updating Site Styles
```bash
# Edit theme colors
vim web-app/src/lib/theme.css

# Test locally
cd web-app && npm run dev

# Flow changes
bb flow "style: update tundra color palette"
```

### Debugging Build Failures
```bash
# Check which files are problematic
bb writings:build  # Full build with verbose errors

# Clear cache and retry
rm .build-cache.edn
bb writings:build-fast

# Validate individual file
bb -e "(require '[markdown.core :as md]) \
       (println (md/md-to-html-string (slurp \"writings/9298.md\")))"
```

### Manual Deployment
```bash
# Deploy without commit/push
bb deploy:full

# Or step-by-step:
bb deploy:build-content    # Build JSON
bb deploy:build-site       # Build SvelteKit
bb deploy:pages            # Push to pages branch
```

---

## Performance Optimization

### Build Caching
**When cache is used**:
- File hasn't been modified
- JSON exists in `web-app/static/content/`
- Cache entry matches file hash

**When cache is invalidated**:
- File modified (timestamp or size changed)
- JSON file deleted
- Cache file corrupted/missing

### Watch Mode
```bash
# Auto-rebuild on file changes
bb writings:watch

# In separate terminal:
cd web-app && npm run dev
```

**Note**: Watch mode uses incremental builds automatically

---

## Troubleshooting

### "Build cache corrupted"
```bash
rm .build-cache.edn
bb writings:build-fast
```

### "Content directory not found"
```bash
# Ensure .gitkeep exists
ls -la web-app/static/content/.gitkeep
touch web-app/static/content/.gitkeep
```

### "CI fails but local works"
- Check `.gitignore` - ensure content dir structure is preserved
- Verify `.gitkeep` is committed
- Check CI logs for specific error

### "Incremental build misses changes"
- Cache uses timestamp + size, not content hash
- `touch` a file to force rebuild
- Or use full build: `bb writings:build`

---

## Architecture Decisions

### Why Babashka?
- **Fast startup** - Native binary, instant execution
- **Clojure** - Functional, data-oriented
- **Batteries included** - fs, shell, yaml, json
- **No JVM** - Lightweight compared to Clojure JVM

### Why SvelteKit?
- **Static generation** - Pre-renders to HTML
- **Fast** - Minimal JavaScript
- **File-based routing** - Simple mental model
- **Adapter-static** - Perfect for Codeberg Pages

### Why Incremental Builds?
- **Developer experience** - Fast feedback loop
- **CI efficiency** - Shorter build times
- **Scalability** - Handles 1000+ essays
- **Cache invalidation** - Simple hash-based strategy

---

## Contributing

### Code Style
- **Clojure**: 80-char line wrapping
- **JavaScript**: 2-space indent
- **Markdown**: Semantic line breaks

### Commit Messages
```
<type>: <subject> (<essay number if applicable>)

Types: feat, fix, docs, style, refactor, test, chore
```

Examples:
- `feat: add essay 9297 - Tundra Navigation Principles (7761)`
- `fix: resolve CI build failure with .gitkeep (7762)`
- `docs: add developer guide for build system (7760)`

### Testing Locally Before Flow
```bash
# 1. Build content
bb writings:build-fast

# 2. Check for errors
echo $?  # Should be 0

# 3. Preview site
cd web-app && npm run dev

# 4. Flow when satisfied
bb flow "your message"
```

---

## Resources

### Documentation
- **Writing Style Guide**: `docs/WRITING-STYLE-GUIDE.md` (TODO)
- **TODO**: `docs/TODO.md`
- **Vision**: `docs/VISION-SYNTHESIS-2025-10-11_7777.md`

### External Links
- **Codeberg**: https://codeberg.org/kae3g/12025-10
- **Live Site**: https://kae3g.codeberg.page/12025-10/
- **SvelteKit Docs**: https://kit.svelte.dev/
- **Babashka**: https://babashka.org/

---

*Built on permafrost. Flowing with precision. Healing through clarity.* ❄️🌊

