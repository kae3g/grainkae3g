---
title: "Session 777 Summary: Three-Tier Sorting & Experimental Drafts"
date: 2025-10-12
status: complete
session: 777
---

# Session 777 Summary: Three-Tier Sorting & Experimental Drafts

**Date**: Sunday, October 12, 2025  
**Time**: Started morning, wrapped evening PDT  
**Branch**: `tundra` (dual-deploy to GitHub + Codeberg)  
**Status**: ✅ Complete & Deployed

---

## 🎯 Primary Accomplishment: Three-Tier Essay Sorting

### Problem
The main index page was mixing experimental drafts (`z9098-experimental.md`) with published essays because the sorting logic checked the **title** field for `z` prefix, but experimental drafts have descriptive titles like "EXPERIMENTAL DRAFT: Grothendieck's Rising Sea".

### Solution
Updated sorting logic to check the **slug** (filename) instead of title:
- `scripts/writings_build.clj`
- `scripts/writings_build_incremental.clj`

### Result
The index now has **three distinct sections**:

1. **Non-numeric essays** (sorted by `sort-order` metadata):
   - Cold Calculation
   - Your Tundra
   - Coldriver Tundra
   - Expedition
   - README

2. **Numeric essays** (sorted ascending by number):
   - kae3g 9098: Grothendieck's Rising Sea
   - kae3g 9298: Foundations of Precision Flow
   - kae3g 9299: tundra Devices
   - ... (all numbered essays, 9499-9999)

3. **Experimental Drafts** (z-prefixed, at bottom):
   - z-README (convention guide)
   - z9098-experimental (beginner-friendly version)
   - z9098_expansion_guide (planning doc)
   - z9098_pseudo_777 (expansion plan)

---

## 📚 Experimental Draft System

### Convention
Files in `docs/` starting with `z` are experimental drafts:
- **Publicly accessible** via web with full styling
- **Shareable links** for collaborative feedback
- **Clearly separated** from main content (own section)
- **No `.gitignore` needed** - committed to repo

### Benefits
- Safe experimentation without affecting live essays
- Can share URLs like `/z9098-experimental.html` for feedback
- Compare original vs. experimental side-by-side
- Merge successful changes back to main essays

### Documentation
Created `docs/z-README.md` explaining:
- Convention rationale
- Workflow for creating experimental drafts
- Three-tier sorting behavior
- When to merge vs. keep separate

---

## 🔧 Build System Updates

### Files Modified
1. **`scripts/writings_build.clj`**:
   - Added `z-drafts` scanning from `docs/` directory
   - Updated `is-z-prefixed?` to check `:slug` field
   - Three-way categorization logic
   - Updated comments to reflect three-tier sorting

2. **`scripts/writings_build_incremental.clj`**:
   - Same updates as above for incremental builds
   - Maintains cache compatibility
   - Found 106 total files (102 writings + 4 z-drafts)

### Commit Messages
1. `feat: three-tier essay sorting with experimental drafts section`
2. `fix: three-tier sorting checks slug instead of title for z-prefix`

---

## 🎬 CI Wait Feature (Setup Complete, Token Pending)

### Goal
Enable `bb flow:wait` to poll GitHub Actions and Codeberg Pages, then print "✨ Deployment verified!" when both sites are live.

### Completed
- ✅ Created `docs/SETUP-CI-WAIT.md` (setup guide)
- ✅ Updated `.config.template.edn` for dual-deploy
- ✅ Updated `.secrets.template.edn` with clear token instructions
- ✅ Created `.config.edn` with user's actual dual-deploy info
- ✅ Created `.secrets.edn` with placeholder for GitHub PAT

### Remaining (Optional)
- ⏸️ User gets GitHub PAT (Actions:read permission)
- ⏸️ User adds token to `.secrets.edn`
- ⏸️ Implement `scripts/ci/wait-for-deploy.clj`
- ⏸️ Add `bb flow:wait` task to `bb.edn`
- ⏸️ Test live deployment with CI wait

**Note**: Current `bb flow` works perfectly fine. This just adds live status updates.

---

## 📝 Files Created/Modified

### New Files
- `docs/z-README.md` (experimental draft convention guide)
- `docs/z9098-experimental.md` (experimental copy of Essay 9098)
- `docs/z9098_expansion_guide.md` (planning doc)
- `docs/z9098_pseudo_777.md` (expansion plan)
- `docs/SETUP-CI-WAIT.md` (CI wait setup guide)
- `.config.edn` (user's personal config)
- `.secrets.edn` (secrets placeholder)

### Modified Files
- `scripts/writings_build.clj` (three-tier sorting)
- `scripts/writings_build_incremental.clj` (three-tier sorting)
- `.config.template.edn` (dual-deploy structure)
- `.secrets.template.edn` (updated comments)
- `.gitignore` (removed `docs/z*.md` exclusion)

---

## 🌊 Deployment Status

### Live Sites
- **Codeberg Pages**: https://kae3g.codeberg.page/12025-10/
- **GitHub Pages**: https://kae3g.github.io/12025-10/

### Verification
Both sites successfully deployed via `bb flow`:
- Codeberg: Push to `pages` branch via `scripts/deploy-to-pages.bb`
- GitHub: Automatic via GitHub Actions workflow

### Experimental Draft URLs (Now Live)
- `/z-readme.html` - Convention guide
- `/z9098-experimental.html` - Beginner-friendly Essay 9098
- `/z9098_expansion_guide.html` - Planning document
- `/z9098_pseudo_777.html` - Expansion plan

---

## 🎨 Technical Details

### Three-Tier Sorting Algorithm

```clojure
;; Categorize entries into three groups based on slug
(reduce (fn [acc entry]
          (cond
            (is-z-prefixed? entry)
            (update acc :z-numeric conj entry)
            
            (some? (get-essay-num entry))
            (update acc :numeric conj entry)
            
            :else
            (update acc :non-numeric conj entry)))
        {:non-numeric [] :numeric [] :z-numeric []}
        meta-entries)

;; is-z-prefixed? checks :slug field with ^z regex
(fn [entry]
  (when-let [slug (:slug entry)]
    (boolean (re-find #"^z" slug))))
```

### Dual-Deploy Config Structure

```clojure
:repository
{:primary
 {:platform "codeberg"
  :username "kae3g"
  :repo-name "12025-10"
  :branch "tundra"}
 
 :mirrors
 [{:platform "github"
   :username "kae3g"
   :repo-name "12025-10"
   :branch "tundra"}]}
```

---

## 📊 Statistics

- **Total files processed**: 106 (102 writings + 4 z-drafts)
- **Commits today**: 2 major commits
- **Deployments**: 2 successful dual-deploys
- **Build time saved** (incremental): ~5,300ms
- **Lines of sorting logic**: ~40 lines (both build scripts)

---

## 💡 Key Insights

### Slug vs. Title
Using the slug (filename) for categorization is more reliable than title because:
- Slugs are guaranteed unique and consistent
- Titles can be descriptive and not follow naming patterns
- File naming is under our control
- No risk of miscategorization due to title changes

### Experimental Draft Strategy
Making experimental drafts publicly accessible (rather than git-ignored) enables:
- Collaborative feedback via shareable links
- Testing changes in production environment
- Clear separation from main content (own section)
- Version control of experimental approaches

### Dual-Deploy Architecture
The `bb flow` dual-deploy approach provides:
- Speed: GitHub Pages (fast global CDN)
- Ethics: Codeberg Pages (open-source complement)
- Redundancy: Two independent hosting platforms
- Flexibility: Can switch primary if needed

---

## 🔜 Next Steps (When Ready)

### Immediate (Optional)
1. Get GitHub PAT for CI wait feature
2. Test `bb flow:wait` implementation

### Essay 9098 Expansion (35 TODOs)
1. **Phase 1**: Commander's Intent + After-Action Review
2. **Phase 2**: Competition/Cooperation + Casualties + Enhanced Dialogue
3. **Phase 3**: Grace themes + When the Sea Doesn't Rise
4. **Phase 4**: Five Wisdom Traditions + Computing Examples
5. **Phase 5**: Quick Reference + Audiobook optimization

### Cross-Essay Linking (18 TODOs)
- Add internal links from 9098 to: Cold Calc, 9298, 9299, 9507, 9514, 9530
- Add reverse links back to 9098 from those essays
- Add poetic navigation to all essays

---

## 🙏 Reflections

### What Worked Well
- Incremental build system saved significant time
- Three-tier sorting is clean and maintainable
- Experimental draft convention enables safe iteration
- Dual-deploy workflow is smooth and reliable

### What We Learned
- Slug-based categorization > Title-based
- Public experimental drafts > Git-ignored
- Clear separation aids discovery
- Build scripts are powerful tools

### For Next Session
- CI wait feature would be nice but not essential
- Essay 9098 expansion is substantial work
- Consider batching cross-essay linking
- May want to create more experimental drafts

---

## 📖 Related Documentation

- **Convention Guide**: `docs/z-README.md`
- **CI Wait Setup**: `docs/SETUP-CI-WAIT.md`
- **Build System**: `docs/BB-TASKS-REFERENCE.md`
- **Expansion Plan**: `docs/z9098_expansion_guide.md`
- **PSEUDO Roadmap**: `docs/PSEUDO.md`

---

**Session 777 wrapped evening PDT on Sunday, October 12, 2025.**  
**Status: Excellent progress. System stable. Ready for rest.** 🌊❄️

*"The sea rises one raindrop at a time. Today we added several drops."*

