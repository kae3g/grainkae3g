# Experimental Drafts (z*.md files)

**Convention**: Files starting with `z` are experimental drafts for testing approaches before applying to live essays.

---

## Overview

Experimental drafts are **fully deployed** to the website and appear in their own section at the bottom of the main index. This allows:
- Sharing web links for feedback
- Testing new approaches publicly
- Comparing live vs. experimental versions
- Clear separation from published content

---

## Website Display

The main index page sorts essays into **three sections**:

1. **Non-numeric essays** (sorted by `sort-order` metadata):
   - Map of a Long Journey
   - Cold Calculation
   - Your Tundra
   - About Coldriver Tundra
   - Expedition

2. **Numeric essays** (sorted by number, ascending):
   - kae3g 9098: Grothendieck's Rising Sea
   - kae3g 9298: Foundations of Precision Flow
   - kae3g 9299: tundra Devices
   - ... (all numbered essays)

3. **Experimental drafts** (sorted by number, ascending):
   - z9098: [Experimental Title]
   - z9299: [Experimental Title]
   - ... (all z-prefixed essays)

This keeps experimental work **visible and shareable**, but clearly separated from published content.

---

## Current Experimental Drafts

### `z9098-experimental.md`
**Purpose**: Testing beginner-friendly improvements to Essay 9098  
**Status**: Active experimentation (Session 777)  
**Deployed**: Yes, available at `/z9098-experimental.html`

**Experiments to try**:
- Welcoming intro paragraph for first-time readers
- Beginner-friendly navigation with read times
- Multiple reading paths (Beginner/Complete/Topic-specific)
- Inline explanations for technical terms
- "Why this matters to you" callouts
- Visual difficulty markers (⭐ beginner, ⭐⭐ intermediate, ⭐⭐⭐ advanced)

---

## Why Use Experimental Drafts?

1. **Safety**: Test radical changes without affecting live essays
2. **Comparison**: Keep original available while experimenting
3. **Learning**: Try multiple approaches side-by-side
4. **Collaboration**: Share experimental versions for feedback before deployment
5. **Web Accessible**: Full styling, navigation, and shareable links

---

## Convention Rules

- Files starting with `z` are **committed to git** and **deployed to website**
- Store in `docs/` directory: `docs/z9098-experimental.md`
- Include standard frontmatter (title, date, etc.)
- Appear in separate section at bottom of index
- Can be shared via direct links for feedback
- Delete or archive once changes are merged or experiment concludes

---

## Creating an Experimental Draft

```bash
# Copy live essay to experimental version (in docs/ directory)
cp writings/9098-grothendiecks-rising-sea.md docs/z9098-experimental.md

# Edit frontmatter:
# - title: "z9098: [Experimental approach description]"
# - Keep standard metadata
# - No need for draft: true or special sort-order

# Make your experimental changes

# Build to see it on the site
bb writings:build-fast

# Share the link for feedback
# https://kae3g.github.io/12025-10/z9098-experimental.html
```

---

## Typical Workflow

1. **Create experimental copy** in `docs/` directory
2. **Edit freely** with experimental approaches
3. **Build and preview** locally or deploy to web
4. **Share link** with collaborators for feedback
5. **Compare** original vs. experimental side-by-side
6. **Merge successful changes** back to live essay
7. **Keep or delete** experimental version as appropriate

---

## When to Merge Experiments

Merge experimental changes back to live essay when:
- ✅ Changes improve accessibility without losing depth
- ✅ Multiple readers confirm improvements
- ✅ Audiobook flow tested and verified
- ✅ All internal links updated
- ✅ Navigation still makes sense

---

## Cleanup

**Option 1: Delete** experimental drafts after:
- Changes merged to live version
- Experiment deemed unsuccessful (document why!)
- Essay approach stabilizes

**Option 2: Keep** for historical reference or alternative versions:
- Document purpose clearly in title
- Continue to maintain if it serves a different audience
- Keep as "alternative reading path"

---

**Session 777 note**: Updated convention to deploy experimental drafts to website with three-tier sorting, enabling public sharing and feedback while maintaining clear separation from published essays.
