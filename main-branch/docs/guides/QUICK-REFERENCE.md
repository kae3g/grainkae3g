# Quick Reference: Coldriver Tundra

**Emergency? Skip to [Troubleshooting](#troubleshooting)**

---

## Daily Workflow

### Write → Build → Preview → Deploy

```bash
# 1. Edit essays
vim writings/9298-essay-title.md

# 2. Build content
bb writings:build-fast

# 3. Preview locally
cd web-app && npm run dev
# Visit: http://localhost:5173/12025-10/

# 4. Deploy everything
bb flow "feat: add essay 9298"
```

---

## Essential Commands

### Building
```bash
bb writings:build-fast    # Smart incremental (default)
bb writings:build         # Full rebuild
bb writings:watch         # Auto-rebuild on changes
```

### Deploying
```bash
gb flow "message"                    # Triple-deploy: GitHub + Codeberg + org
gb flow "message" --verbose          # Verbose mode with detailed output
bb deploy:full                       # Deploy without commit
```

### Development
```bash
cd web-app && npm run dev      # Start dev server
cd web-app && npm run build    # Build static site
```

---

## File Locations

| What | Where |
|------|-------|
| Essays (source) | `writings/*.md` |
| Site code | `web-app/src/` |
| Generated JSON | `web-app/static/content/` (gitignored) |
| Build cache | `.build-cache.edn` (gitignored) |
| Documentation | `docs/` |
| Scripts | `scripts/` |

---

## Git Workflow

```bash
# Check status
git status

# See what changed
git diff writings/

# Undo changes
git restore writings/essay.md

# Deploy changes
bb flow "your message"
```

---

## Troubleshooting

### Build Failed
```bash
# Clear cache and retry
rm .build-cache.edn
bb writings:build-fast
```

### CI Failed
```bash
# Ensure .gitkeep exists
ls web-app/static/content/.gitkeep

# Re-create if missing
touch web-app/static/content/.gitkeep
git add web-app/static/content/.gitkeep
bb flow "fix: restore .gitkeep"
```

### Site Not Updating
```bash
# Hard refresh browser (Cmd+Shift+R)
# Wait 2 minutes for Codeberg Pages
# Check CI status: https://codeberg.org/kae3g/12025-10
```

### JSON Files Showing in Git
```bash
# Should be ignored - check .gitignore
cat .gitignore | grep "content/\*.json"

# Force remove if present
git rm --cached web-app/static/content/*.json
```

---

## Branch Strategy

| Branch | Purpose |
|--------|---------|
| `coldriver-heal` | Main development |
| `pages` | Deployed site (auto-generated) |

**Never commit to `pages` manually** - it's overwritten on every deploy.

---

## Performance Tips

### Fast Builds
- ✅ Use `bb writings:build-fast` (incremental)
- ✅ Keep cache file (`.build-cache.edn`)
- ✅ Edit one file at a time

### Slow? Debug
```bash
# Time your build
time bb writings:build-fast

# Expected times:
# Cold start: ~5s
# Hot run (no changes): <1s
# 1-5 files changed: ~1s
```

---

## URLs

| What | URL |
|------|-----|
| **Live Site** | https://kae3g.codeberg.page/12025-10/ |
| **Repository** | https://codeberg.org/kae3g/12025-10 |
| **CI Status** | https://ci.codeberg.org/repos/15396 |
| **Docs** | In `docs/` directory |

---

## Common Tasks

### New Essay
```bash
# Create file
cat > writings/9297-new-essay.md << 'EOF'
---
title: "Essay Title"
date: 2025-10-11
---

# Essay Title

Content here...
EOF

# Deploy
bb flow "feat: add essay 9297"
```

### Update Existing Essay
```bash
vim writings/9298-existing.md
bb flow "docs: update essay 9298 with new section"
```

### Change Site Theme
```bash
vim web-app/src/lib/theme.css
cd web-app && npm run dev  # Preview
bb flow "style: adjust tundra color palette"
```

### Update Component
```bash
vim web-app/src/lib/ThemeToggle.svelte
cd web-app && npm run dev  # Preview
bb flow "feat: improve theme toggle UX"
```

---

## Keyboard Shortcuts

### Vim (editing)
- `i` - Insert mode
- `Esc` - Normal mode
- `:wq` - Save and quit
- `:q!` - Quit without saving

### Browser (dev tools)
- `Cmd+Shift+R` - Hard refresh
- `Cmd+Option+I` - Dev tools
- `Cmd+Shift+C` - Inspect element

---

## Environment

### Required Tools
```bash
bb --version          # Babashka
node --version        # Node.js 20+
npm --version         # npm
git --version         # Git
```

### Install Missing Tools
```bash
# macOS
brew install babashka node git

# Linux
# See: https://babashka.org/#installation
```

---

## Emergency Procedures

### "Everything is broken"
```bash
# 1. Check if files exist
ls -la writings/*.md

# 2. Rebuild from scratch
rm .build-cache.edn
rm -rf web-app/node_modules
cd web-app && npm ci && cd ..
bb writings:build
cd web-app && npm run build

# 3. Test locally
cd web-app && npm run dev

# 4. If works, deploy
bb flow "fix: rebuild everything"
```

### "CI keeps failing"
```bash
# 1. Check logs at:
# https://ci.codeberg.org/repos/15396

# 2. Common fixes:
touch web-app/static/content/.gitkeep
git add web-app/static/content/.gitkeep
bb flow "fix: ensure content dir exists"
```

### "Lost uncommitted work"
```bash
# Check git stash
git stash list

# Recover if found
git stash pop
```

---

## Getting Help

1. **Check docs**: `docs/DEVELOPER-GUIDE.md`
2. **Check build system**: `docs/BUILD-SYSTEM.md`
3. **Check git log**: `git log --oneline -20`
4. **Check CI logs**: Codeberg → Actions → Latest build

---

*When in doubt: `bb flow`* 🌊❄️

