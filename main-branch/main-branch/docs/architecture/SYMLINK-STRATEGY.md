# 🔗 Symlink Strategy & Documentation Flow
## *One File, Many Names: Organizing grainkae3g for Clarity*

**Purpose:** Document our symlink approach for maintaining clean, discoverable documentation  
**Updated:** January 2025  
**Author:** kae3g

---

## 🎯 PHILOSOPHY

**Principle:** Content should be discoverable by **concept**, not just by filename.

Users might look for:
- "Course index" → `docs/course/index.md`
- "grainkae3g overview" → `docs/course/grainkae3g-index.md` (symlink)
- "Grain Network" → `docs/course/grain-network.md`
- "Business plan" → `docs/business/grain-network-business-plan.md`

**Solution:** Use symlinks to create **multiple entry points** to the same canonical file.

---

## 📁 CURRENT SYMLINK STRUCTURE

### Course Documentation

```bash
docs/course/
├── index.md                      # Canonical: 18-week course overview
├── grainkae3g-index.md -> index.md   # Symlink: Brand-specific reference
├── grain-network.md              # Canonical: Student network architecture
└── grain-network.html            # Legacy: To be deprecated
```

**Rationale:**
- `index.md` - Generic name for web frameworks (SvelteKit, GitHub Pages)
- `grainkae3g-index.md` - Brand-specific name for CLI users, documentation links
- Both point to same content, maintaining DRY principle

### Personal Notes

```bash
docs/
├── personal-reminders.md         # Canonical: Main notes file
└── personal-notes.md -> personal-reminders.md   # Symlink: Alternative name
```

**Rationale:**
- Users might say "notes" or "reminders" - both work
- Single source of truth for personal documentation
- Matches natural language patterns

---

## 🗂️ README HIERARCHY & FLOW

### Overview of All READMEs

```
grainkae3g/
├── README.md                     # [1] Repository root - START HERE
├── docs/
│   ├── README.md                 # [2] Documentation overview
│   ├── GRAINKAE3G-LAUNCH-SUMMARY.md  # [3] What we built
│   ├── PRESS-RELEASE-GRAINKAE3G.md   # [4] Public announcement
│   ├── course/
│   │   ├── index.md              # [5] Course curriculum
│   │   └── grain-network.md      # [6] Network architecture
│   ├── student-guide/
│   │   └── HOW-TO-STAY-ORGANIZED.md  # [7] Practical workflow
│   ├── business/
│   │   └── grain-network-business-plan.md  # [8] Financial model
│   └── architecture/
│       ├── SHARED-DATA-PIPELINE.md   # [9] Technical architecture
│       └── SYMLINK-STRATEGY.md       # [10] This document
├── grainstore/
│   └── LICENSE-SUMMARY.md        # [11] Third-party licenses
└── web-app/
    └── README.md                 # (To be created) Web app docs
```

---

## 📖 RECOMMENDED READING ORDER

### For New Users (Students)

1. **`README.md`** (root) - What is grainkae3g?
2. **`docs/course/index.md`** - Start the 18-week course
3. **`docs/student-guide/HOW-TO-STAY-ORGANIZED.md`** - Set up your workflow
4. **`docs/course/grain-network.md`** - Understand the network
5. **Course weeks** - Follow lessons 1-18

### For Contributors (Developers)

1. **`README.md`** (root) - Project overview
2. **`docs/GRAINKAE3G-LAUNCH-SUMMARY.md`** - What we built and why
3. **`docs/architecture/SHARED-DATA-PIPELINE.md`** - Technical architecture
4. **`docs/architecture/SYMLINK-STRATEGY.md`** - This document
5. **`CONTRIBUTING.md`** (to be created) - How to contribute

### For Business/Partnership Inquiries

1. **`README.md`** (root) - Project overview
2. **`docs/PRESS-RELEASE-GRAINKAE3G.md`** - Public announcement
3. **`docs/business/grain-network-business-plan.md`** - Complete financial model
4. **`docs/course/grain-network.md`** - Network architecture
5. **Contact kae3g** - Email or GitHub issues

### For Educators (Teachers)

1. **`README.md`** (root) - Project overview
2. **`docs/course/index.md`** - Course curriculum
3. **`docs/business/grain-network-business-plan.md`** - Institutional licensing
4. **`docs/student-guide/HOW-TO-STAY-ORGANIZED.md`** - Student resources
5. **`grainstore/LICENSE-SUMMARY.md`** - Legal compliance

---

## 🔄 TEMPLATE STRUCTURE (For Personal Use)

The current structure is designed as a **template** that any user can fork and personalize:

### Generic Elements (Keep As-Is)

```
grainkae3g/                       # User changes to their username
├── README.md                     # User customizes with their info
├── data/
│   ├── site-config.edn           # User updates: name, email, repo
│   └── magazine-content.edn      # User customizes content
├── content/
│   └── writings/                 # User adds their own content
├── docs/
│   ├── course/                   # Framework can be reused
│   ├── student-guide/            # Generic guide, users add notes
│   └── architecture/             # Technical docs stay generic
└── scripts/                      # Build scripts work for anyone
```

### Personal Elements (User Customizes)

- **Repository name:** `grainkae3g` → `{username}kg` or `{username}-stack`
- **Site name:** `grainkae3g` → User's preferred brand
- **Author:** `kae3g` → User's username
- **Email:** `kj3x39@gmail.com` → User's email
- **Content:** Writings, projects, personal notes

---

## 🌐 FUTURE: GRAINNETWORK ORGANIZATION

### Migration Plan (When Ready)

#### Phase 1: Create GitHub Organization

```bash
# Create organization
gh org create grainnetwork

# Create primary repositories
gh repo create grainnetwork/grainstore --public
gh repo create grainnetwork/grainnetwork --public
```

#### Phase 2: Mirror Strategy

Both repos will be **identical mirrors** since the names are so similar:

```
grainnetwork/grainstore/          # Repo 1: Focus on dependency management
├── README.md                     # Emphasizes "grainstore" concept
└── ... (same structure)

grainnetwork/grainnetwork/        # Repo 2: Focus on network concept
├── README.md                     # Emphasizes "network" concept
└── ... (same structure)
```

**Sync Strategy:**

```bash
# Set up bidirectional mirrors
cd grainnetwork/grainstore
git remote add mirror git@github.com:grainnetwork/grainnetwork.git

cd grainnetwork/grainnetwork
git remote add mirror git@github.com:grainnetwork/grainstore.git

# Sync script (scripts/sync-mirrors.bb)
bb scripts/sync-mirrors.bb
```

#### Phase 3: README Differentiation

While the code is identical, READMEs emphasize different aspects:

**grainstore/README.md:**
```markdown
# Grainstore: Verified Dependency Management
A student-built system for managing verified, audited dependencies
with full license compliance and security tracking.

**Features:**
- Git submodule-based dependency management
- License audit and compliance
- Version pinning and reproducibility
- Security scanning
```

**grainnetwork/README.md:**
```markdown
# Grain Network: Decentralized Student Computing
A student-owned network for learning sovereignty, collaboration,
and building the future of decentralized education.

**Features:**
- 18-week sovereignty curriculum
- ICP-based identity system
- Student governance and DAO
- Peer-to-peer learning
```

#### Phase 4: Personal Fork Naming

Current personal fork:
```
kae3g/grainkae3g → Personal sovereignty stack
```

After organization:
```
kae3g/grainkae3g → Personal fork/customization
grainnetwork/grainstore → Official dependency manager
grainnetwork/grainnetwork → Official student network
```

---

## 📝 SYMLINK CREATION GUIDE

### Creating Symlinks (Unix/Linux/macOS)

```bash
# Relative symlink (preferred for portability)
ln -s target.md link-name.md

# Example: Create course index symlink
cd docs/course
ln -s index.md grainkae3g-index.md

# Verify symlink
ls -la | grep grainkae3g-index
# Output: lrwxrwxrwx ... grainkae3g-index.md -> index.md

# Read through symlink (works transparently)
cat grainkae3g-index.md
# Shows content of index.md
```

### Symlink Best Practices

#### ✅ DO:

- Use **relative paths** for portability (`../other-dir/file.md`)
- Create symlinks in **Git** (they're tracked as special files)
- Document symlinks in this file
- Use descriptive names (`grainkae3g-index.md` not `gi.md`)
- Point symlinks at **canonical files**, not other symlinks

#### ❌ DON'T:

- Use **absolute paths** (`/home/user/...`) - breaks on other machines
- Create **circular symlinks** (A → B → A)
- Symlink to files outside the repository
- Rename canonical files without updating symlinks
- Create too many symlinks (confuses users)

### Symlink Maintenance

```bash
# Find all symlinks in repository
find . -type l -ls

# Check broken symlinks
find . -type l ! -exec test -e {} \; -print

# Update symlink target
rm old-link.md
ln -s new-target.md old-link.md
```

---

## 🔍 COMMENT CONVENTIONS

### Markdown File Headers

Every file should identify its symlink status:

**Canonical file (no symlinks):**
```markdown
# Title

Content...
```

**Canonical file (has symlinks):**
```markdown
<!-- Canonical: index.md | Symlinks: grainkae3g-index.md -->
# Title

Content...
```

**Symlink file (when viewed directly):**

The symlink itself doesn't need comments, but the target should note it:

```markdown
<!-- grainkae3g-index.md -->
# Title

Content...
```

This way, users know they're reading `index.md` via the `grainkae3g-index.md` symlink.

---

## 📊 DOCUMENTATION DEPENDENCY GRAPH

```
README.md (root)
  ├─→ docs/README.md
  ├─→ docs/GRAINKAE3G-LAUNCH-SUMMARY.md
  │     ├─→ docs/PRESS-RELEASE-GRAINKAE3G.md
  │     ├─→ docs/course/index.md ⇄ docs/course/grainkae3g-index.md
  │     ├─→ docs/course/grain-network.md
  │     ├─→ docs/business/grain-network-business-plan.md
  │     ├─→ docs/student-guide/HOW-TO-STAY-ORGANIZED.md
  │     └─→ docs/architecture/SHARED-DATA-PIPELINE.md
  ├─→ docs/core/philosophy/PSEUDO.md
  ├─→ grainstore/LICENSE-SUMMARY.md
  └─→ SWAY-QUICK-REFERENCE.md

Legend:
  ─→  Links to
  ⇄  Symlink (bidirectional reference)
```

---

## 🎯 DESIGN GOALS

### 1. Discoverability
Users should find documentation by **concept**, not just filename.

✅ Looking for "course"? → `docs/course/index.md`  
✅ Looking for "grainkae3g course"? → `docs/course/grainkae3g-index.md` (same file!)

### 2. DRY Principle
**Don't Repeat Yourself** - one canonical source, multiple access points.

❌ BAD: Duplicate `course-overview.md` and `grainkae3g-course.md`  
✅ GOOD: `index.md` with `grainkae3g-index.md` symlink

### 3. Git-Friendly
Symlinks are first-class Git objects.

```bash
git add docs/course/grainkae3g-index.md  # Commits symlink, not content
git diff  # Shows symlink changes clearly
```

### 4. Web-Compatible
Static site generators (SvelteKit, Jekyll, Hugo) handle symlinks correctly.

### 5. Future-Proof
When renaming project to `grainnetwork`, symlinks make migration easy.

---

## 🚀 NEXT STEPS

### Immediate (Current grainkae3g)

- [x] Create `docs/course/grainkae3g-index.md` → `index.md`
- [x] Document symlink strategy (this file)
- [ ] Update all READMEs to reference correct paths
- [ ] Create `CONTRIBUTING.md` with symlink guidelines
- [ ] Add symlink checks to CI/CD pipeline

### Future (grainnetwork Organization)

- [ ] Create GitHub organization `grainnetwork`
- [ ] Set up `grainnetwork/grainstore` repository
- [ ] Set up `grainnetwork/grainnetwork` repository (mirror)
- [ ] Configure bidirectional sync between mirrors
- [ ] Update all documentation to reference organization
- [ ] Migrate `kae3g/grainkae3g` → personal fork of `grainnetwork/grainnetwork`

---

## 📚 RELATED DOCUMENTATION

- [README.md](../../README.md) - Project overview
- [docs/README.md](../README.md) - Documentation overview
- [SHARED-DATA-PIPELINE.md](SHARED-DATA-PIPELINE.md) - Data architecture
- [GRAINKAE3G-LAUNCH-SUMMARY.md](../GRAINKAE3G-LAUNCH-SUMMARY.md) - Launch summary

---

**Created by:** kae3g  
**For:** grainkae3g → grainnetwork migration  
**License:** MIT

*"One file, many names. One network, infinite grains."* 🌾


