# Tundra Unification Plan

**Created**: 2025-10-12 15:35 PDT  
**Session**: 777  
**Goal**: Unify warm (GitHub) and cold (Codeberg) deployments into a single `tundra` branch with dual-deploy `bb flow`

---

## Current State Analysis

### Repository Structure
```
~/kae3g/12025-10/               # Main working repo (Codeberg-only)
  └── branch: coldriver-heal
  
~/kae3g_github/12025-10_000d197475/  # Snapshot of GitHub version
  └── branch: rhizome-valley
  
~/kae3g_codeberg/               # New directory for symlinks
```

### Deployment Infrastructure

**GitHub Pages (Fast CDN)**:
- Branch: `rhizome-valley`
- Workflow: `.github/workflows/deploy.yml`
- Deployment: GitHub Actions (automatic on push)
- URL: https://kae3g.github.io/12025-10/
- CSS: Warm color palette
- Speed: Fast (Microsoft/GitHub CDN expertise)

**Codeberg Pages (Open/Harmonious)**:
- Branch: `coldriver-heal`
- Deployment: `bb flow` → `scripts/deploy-to-pages.bb`
- Pushes to: `pages` branch
- URL: https://kae3g.codeberg.page/12025-10/
- CSS: Cold color palette (dark theme working)
- Speed: Slower (but ethical/open)

### Current `bb flow` (Codeberg-only)
```clojure
1. Build content (bb writings:build-fast)
2. Stage changes (git add -A)
3. Check for changes
4. Commit (git commit -m msg)
5. Push to origin coldriver-heal
6. Deploy to Pages (bb scripts/deploy-to-pages.bb full)
   → Pushes to Codeberg pages branch
```

---

## The Vision

### New Branch: `tundra`
- Single unified branch for both remotes
- Replaces `rhizome-valley` and `coldriver-heal` as default
- Old branches remain untouched (for now)

### Dual-Remote Setup
```
origin → git@codeberg.org:kae3g/12025-10.git
github → git@github.com:kae3g/12025-10.git
```

### Enhanced `bb flow` (Dual-Deploy)
```
1. Build content (verify before commit)
2. Stage changes
3. Check for changes
4. Commit
5. Push to Codeberg origin/tundra
6. Push to GitHub github/tundra
7. Deploy to Codeberg Pages (pages branch)
8. Trigger GitHub Actions (automatic from push)
```

### Theme Toggle System

**Current** (Codeberg only):
- `*` toggle: dark/light theme

**New** (Unified):
- `*` toggle: warm (GitHub CSS) / cold (Codeberg CSS) palette
- `%` toggle: light/dark for current palette
- Four total themes:
  - Warm Dark (rhizome-valley default)
  - Warm Light (new, analogous to Warm Dark)
  - Cold Dark (coldriver-heal default, current)
  - Cold Light (current Codeberg light theme)

### Main Page Update
**Current**:
```
branch: coldriver-heal → link to Codeberg
```

**New**:
```
branch: tundra → link to GitHub tundra branch
```
(Assuming GitHub deploys successfully)

---

## Implementation Plan

### Phase 1: Git Infrastructure Setup

**1.1 Add GitHub as remote**
```bash
cd ~/kae3g/12025-10
git remote add github git@github.com:kae3g/12025-10.git
```

**1.2 Create tundra branch**
```bash
git checkout -b tundra coldriver-heal
```

**1.3 Verify remotes**
```bash
git remote -v
# origin    git@codeberg.org:kae3g/12025-10.git
# github    git@github.com:kae3g/12025-10.git
```

### Phase 2: GitHub Actions Integration

**2.1 Copy GitHub workflow**
- Source: `~/kae3g_github/12025-10_000d197475/.github/workflows/deploy.yml`
- Destination: `~/kae3g/12025-10/.github/workflows/deploy.yml`
- Update trigger: `rhizome-valley` → `tundra`

**2.2 Verify GitHub Pages settings**
- Source: GitHub Actions (not "Deploy from a branch")
- Permissions: Read and write
- Default branch: `tundra` (set after first push)

### Phase 3: Expand `bb flow`

**3.1 Update `bb.edn` flow task**
```clojure
flow {:doc "Dual-deploy: build, commit, push to both remotes, deploy"
      :task (let [msg (or (first *command-line-args*) 
                          "chore: precision flow update")]
              (println "🌊 Starting dual-deploy precision flow...")
              
              ;; Step 1: Build (verify before commit)
              (println "1️⃣ Building content to verify...")
              (shell "bb" "writings:build-fast")
              (println "✓ Build successful")
              
              ;; Step 2: Stage
              (println "2️⃣ Staging changes...")
              (shell "git" "add" "-A")
              (println "✓ Changes staged")
              
              ;; Step 3: Check for changes
              (println "3️⃣ Checking for changes...")
              (let [status (-> (shell {:out :string} "git" "status" "--porcelain")
                               :out
                               str/trim)]
                (if (empty? status)
                  (do
                    (println "✓ Working tree clean - nothing to commit")
                    (println "✨ Precision flow complete! Everything up to date."))
                  (do
                    ;; Step 4: Commit
                    (println "✓ Changes detected")
                    (println "4️⃣ Committing...")
                    (shell "git" "commit" "-m" msg)
                    (println "✓ Committed:" msg)
                    
                    ;; Step 5: Push to Codeberg
                    (println "5️⃣ Pushing to Codeberg (origin/tundra)...")
                    (shell "git" "push" "origin" "tundra")
                    (println "✓ Pushed to Codeberg")
                    
                    ;; Step 6: Push to GitHub
                    (println "6️⃣ Pushing to GitHub (github/tundra)...")
                    (shell "git" "push" "github" "tundra")
                    (println "✓ Pushed to GitHub")
                    
                    ;; Step 7: Deploy to Codeberg Pages
                    (println "7️⃣ Deploying to Codeberg Pages (pages branch)...")
                    (shell "bb" "scripts/deploy-to-pages.bb" "full")
                    (println "✓ Codeberg Pages deployed")
                    
                    ;; Step 8: GitHub Actions will trigger automatically
                    (println "8️⃣ GitHub Actions will deploy automatically...")
                    (println "   Monitor: https://github.com/kae3g/12025-10/actions")
                    
                    (println "")
                    (println "✨ Dual-deploy precision flow complete!")
                    (println "🌐 Codeberg: https://kae3g.codeberg.page/12025-10/")
                    (println "🌐 GitHub:   https://kae3g.github.io/12025-10/")
                    (println "⏱️  Give both sites 1-2 minutes to rebuild")))))}
```

**3.2 Ensure Codeberg Pages still uses `pages` branch**
- No changes needed to `scripts/deploy-to-pages.bb`
- It already force-pushes to `pages` branch

### Phase 4: Theme System (Future - Not This Session)

**Defer to future prompts**:
- Create Warm Light CSS palette
- Implement `*` toggle (warm/cold palette switch)
- Implement `%` toggle (light/dark for current palette)
- Update writings to be theme-aware (genre toggles)

**For now**: Keep existing dark/light toggle as-is

### Phase 5: Symlinks (Optional - Not This Session)

**Defer or reconsider**:
- Symlinks to `~/kae3g_github/12025-10/` and `~/kae3g_codeberg/12025-10/`
- May not be necessary if dual-remote works well
- Can create later if needed for workflow

---

## Execution Order (This Session)

### ✅ Step 1: Create Planning Doc
- This document

### 🔄 Step 2: Update PSEUDO.md
- Reflect new unified strategy
- Update priorities and roadmap

### 🔄 Step 3: Git Setup
- Add github remote
- Create tundra branch from coldriver-heal

### 🔄 Step 4: Copy GitHub Workflow
- `.github/workflows/deploy.yml` with tundra trigger

### 🔄 Step 5: Update bb.edn
- Expand `flow` task for dual-deploy

### 🔄 Step 6: Test First Dual-Deploy
- `bb flow "feat: unified tundra branch - dual deploy to GitHub + Codeberg"`
- Verify both sites deploy

### 🔄 Step 7: Update Main Index
- Change `branch: coldriver-heal` → `branch: tundra`
- Link to GitHub tundra branch

### 🔄 Step 8: Set Default Branches
- GitHub: Settings → Branches → Default: tundra
- Codeberg: Settings → Branches → Default: tundra

---

## Success Criteria

- ✅ `tundra` branch exists locally and on both remotes
- ✅ `bb flow` pushes to both Codeberg and GitHub
- ✅ Codeberg Pages deploys to https://kae3g.codeberg.page/12025-10/
- ✅ GitHub Pages deploys to https://kae3g.github.io/12025-10/
- ✅ Main index links to GitHub tundra branch
- ✅ Both remotes have tundra as default branch
- ✅ Old branches (rhizome-valley, coldriver-heal) remain untouched

---

## Future Work (Not This Session)

### Theme Toggle System
- Warm/Cold palette switch (`*`)
- Light/Dark mode switch (`%`)
- Four total themes
- Warm Light CSS palette creation

### Writing Genre Toggles
- Toggle between warm/cold writing themes
- Different "genres" of content based on theme
- Experimental feature for AI-generated content

### Monthly Rebase Strategy (November)
- Rebase all development into single unified next-month commit
- Immutable yet performant
- New repo: `12025-11`

### Potential Cleanup
- Delete old branches (rhizome-valley, coldriver-heal) once stable
- Archive snapshot repo `~/kae3g_github/12025-10_000d197475/`

---

## Notes & Considerations

### Why Dual-Deploy?
- **GitHub**: Fast CDN, Microsoft infrastructure, better performance
- **Codeberg**: Open-source, ethical, harmonious complement
- **Best of both worlds**: Speed + ethics

### Why "Tundra"?
- Unifies warm (valley) and cold (river/heal) metaphors
- Frozen clarity + flowing environment
- Neutral ground for both themes

### Metaphor Evolution
- `rhizome-valley` → warm, nourishing, uplifting (CA climate anxiety)
- `coldriver-heal` → cold, healing, water crisis (MT snowcap safety)
- `tundra` → unified, flowing yet frozen, crystalline precision

### Climate Context (User's Intent)
- California water crisis → cold rivers, healing
- Northern latitudes (Billings, MT) → snowcap, long-term safety
- Global warming → thinking about "cold" as scarce/precious
- Warmth still compelling → "valley" vision remains valid
- **Tundra**: Honors both anxieties and hopes

---

**Ready to execute. Starting with PSEUDO.md update.**

---

## Execution Log

**2025-10-12 15:45 PDT** - Infrastructure complete. Testing first dual-deploy...

