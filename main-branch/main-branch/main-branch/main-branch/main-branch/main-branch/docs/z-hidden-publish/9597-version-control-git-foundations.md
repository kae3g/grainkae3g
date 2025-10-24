# kae3g 9596: Version Control - Git Foundations

**Phase 1: Foundations & Philosophy** | **Week 4** | **Reading Time: 18 minutes**

---

## What You'll Learn

- Why version control exists (collaboration, history, backup)
- Git's content-addressed model (like Nix!)
- Commits, branches, and the DAG (directed acyclic graph)
- Common workflows (add, commit, push, pull, merge)
- Why Git won (vs SVN, CVS, Mercurial)
- Immutability in Git (commits never change)
- How Git enables the grainhouse strategy
- Version control as time-traveling through your garden's history

---

## Prerequisites

- **[9560: Text Files](/12025-10/9560-text-files-universal-format)** - Source code versioning
- **[9590: Filesystem](/12025-10/9590-filesystem-hierarchical-organization)** - Where Git stores data
- **[9595: Package Managers](/12025-10/9595-package-managers-dependency-resolution)** - Content-addressing (Nix)

---

## The Time-Traveling Garden

**Without version control**:
```
project.zip
project-v2.zip
project-v2-final.zip
project-v2-final-ACTUAL.zip
project-v2-final-ACTUAL-USE-THIS.zip
```

**Chaos!** (Which is current? What changed? Can we go back?)

**With Git**:
```bash
git log --oneline
# abc123 Add feature X
# def456 Fix bug Y
# ghi789 Initial commit

# Travel to any point:
git checkout def456
# Code is now exactly as it was at that commit!
```

**Plant lens**: **"Git is a time-lapse of your garden—every state preserved, can replay growth from seed to harvest."**

---

## Git's Content-Addressed Model

**Like Nix** (Essay 9595), Git uses **content addressing**:

### Objects

**Everything is an object** (identified by SHA-1 hash):

**Blob** (file contents):
```bash
echo "Hello, Valley!" | git hash-object --stdin
# Output: a1b2c3d4e5f6... (SHA-1 hash of content)
```

**Tree** (directory):
```
tree abc123
├── 100644 blob def456 README.md
└── 100644 blob ghi789 script.sh
```

**Commit** (snapshot + metadata):
```
commit jkl012
tree abc123
parent mno345
author Alice <alice@example.com>
committer Alice <alice@example.com>

Add new feature
```

**Tag** (named reference):
```
tag v1.0.0
object jkl012
```

**All objects** stored in `.git/objects/`, **named by hash**:
```
.git/objects/
  a1/b2c3d4e5f6...  (blob)
  ab/c123...        (tree)
  jk/l012...        (commit)
```

**Same content → same hash** (like Nix!).

---

## Commits: Immutable Snapshots

**A commit is**:
- Snapshot of entire project (tree)
- Metadata (author, date, message)
- Parent commit(s) (history)
- **Immutable** (never changes - like Nock spec!)

**Create commit**:
```bash
# Stage changes
git add file1.txt file2.txt

# Commit
git commit -m "Add new features"
# Creates new commit object (immutable!)
```

**Key insight**: Commits are **immutable**. You can't change history (only add to it).

**Plant lens**: **"Commits are growth rings—permanent record of the tree's history, each ring immutable."**

---

## Branches: Parallel Timelines

**Branches** are just **pointers to commits**:

```
main:     A → B → C → D
                  ↓
feature:          E → F

# 'main' points to D
# 'feature' points to F
# Both share history A-B-C
```

**Create branch**:
```bash
git branch feature-x
git checkout feature-x
# Or:
git checkout -b feature-x

# Work, commit
git add ...
git commit -m "Feature X"
```

**Merge branch**:
```bash
git checkout main
git merge feature-x

# Now main includes feature-x commits
```

**Delete branch** (pointer only, commits stay!):
```bash
git branch -d feature-x
# Commits still exist (reachable from main)
```

---

## The DAG (Directed Acyclic Graph)

**Git history** is a DAG:

```
       A ← B ← C ← D ← F  (main)
            ↖         ↗
              E ──────   (feature, merged)

Direction: Forward in time (arrows point to parents)
Acyclic: No loops (can't be your own ancestor)
```

**Why DAG matters**:
- **Partial order**: Some commits are unrelated (parallel branches)
- **Merge commits**: Two parents (combining branches)
- **Reachability**: Can find common ancestor (for merges, diffs)

**This is graph theory** (Essay 9540 - sets and types!).

---

## Why Git Won

**Competitors** (SVN, CVS, Mercurial, Perforce):

**Git advantages**:

1. **Distributed**: Every clone is full repo (no central server needed)
2. **Fast**: Local operations (commits, branches, diffs - no network!)
3. **Content-addressed**: Efficient storage (deduplication via hashing)
4. **Branching**: Cheap (just pointers), fast (instant)
5. **Offline**: Work without network, sync later

**SVN** (centralized):
```
Central Server (required for all operations)
     ↓
Developer checkouts (partial copies)

Problem: Server down? Can't commit!
```

**Git** (distributed):
```
Developer 1: [Full repo] ←→ GitHub ←→ [Full repo] Developer 2
                                ↕
                          [Full repo] CI Server

No single point of failure!
```

**Git's philosophy**: Every developer has full history (sovereignty!).

---

## Immutability in Git

**Commits are immutable** (like Clojure's persistent data structures!):

```bash
# Create commit
git commit -m "Add feature"
# Creates: abc123 (hash)

# "Change" commit?
git commit --amend -m "Better message"
# Creates: def456 (NEW commit, abc123 unchanged!)

# abc123 still exists (orphaned, but there)
git reflog  # Shows all commits, even orphaned
```

**Rewriting history** (rebase, amend) **doesn't mutate**—it creates new commits.

**Old commits** stay forever (until garbage collected).

**This is functional programming** for version control!

---

## Common Workflows

### Solo Developer

```bash
# Start project
git init
git add .
git commit -m "Initial commit"

# Work
vim file.txt
git add file.txt
git commit -m "Update file"

# Backup to GitHub
git remote add origin https://github.com/user/repo.git
git push -u origin main
```

### Collaborator

```bash
# Clone repo
git clone https://github.com/user/repo.git
cd repo

# Create branch
git checkout -b feature-x

# Work, commit
git add ...
git commit -m "Add feature X"

# Push branch
git push origin feature-x

# Create pull request on GitHub
# (Others review, merge)
```

### Syncing Changes

```bash
# Fetch changes from remote
git fetch origin

# Merge into current branch
git merge origin/main

# Or (fetch + merge):
git pull origin main
```

---

## Git and the Grainhouse

**Git enables sovereignty**:

### Fork Everything

```bash
# Fork nixpkgs (your grainhouse!)
git clone https://github.com/NixOS/nixpkgs.git
cd nixpkgs

# Create your fork
git remote add mine https://github.com/youruser/nixpkgs.git

# Make changes
vim pkgs/some-package/default.nix
git commit -m "Patch for grainhouse"

# Push to YOUR fork
git push mine main

# Now: You control this dependency forever
```

**Upstream changes**?
```bash
# Fetch upstream
git fetch origin

# Merge selectively
git merge origin/main
# (Review, test, accept/reject)
```

**This is the grainhouse strategy** (Essay 9960): Fork, maintain, control.

### Preserve Knowledge

**Every commit** is documentation:

```bash
git log --all --graph --oneline
# Visual history of project

git show abc123
# See exactly what changed in that commit

git blame file.txt
# Who wrote each line (and when, and why)
```

**Git is a knowledge repository** (like House of Wisdom, Essay 9505!).

---

## Try This

### Exercise 1: Git Basics

```bash
# Create repo
mkdir test-repo && cd test-repo
git init

# Create file
echo "Valley content" > README.md
git add README.md
git commit -m "Initial commit"

# Modify
echo "More content" >> README.md
git add README.md
git commit -m "Add content"

# View history
git log

# Go back
git checkout HEAD~1
cat README.md
# (Only "Valley content", not "More content")

# Return to present
git checkout main
```

---

### Exercise 2: Branching

```bash
# Create branch
git checkout -b experiment

# Make changes
echo "Experimental" > experiment.txt
git add experiment.txt
git commit -m "Try experiment"

# Switch back to main
git checkout main
ls
# experiment.txt doesn't exist here!

# Merge experiment
git merge experiment

ls
# Now experiment.txt exists!
```

---

### Exercise 3: Content-Addressing

```bash
# Create file, get hash
echo "Test content" | git hash-object --stdin
# Output: a1b2c3d4e5f6...

# Same content, same hash
echo "Test content" | git hash-object --stdin
# Output: a1b2c3d4e5f6... (IDENTICAL!)

# Different content, different hash
echo "Different" | git hash-object --stdin
# Output: x9y8z7w6v5u4... (DIFFERENT!)
```

**Observe**: Content-addressing (hash = content).

---

## Going Deeper

### Related Essays
- **[9560: Text Files](/12025-10/9560-text-files-universal-format)** - What Git versions
- **[9595: Package Managers](/12025-10/9595-package-managers-dependency-resolution)** - Nix content-addressing
- **[9960: The Grainhouse](/12025-10/9960-grainhouse-risc-v-synthesis)** - Fork strategy
- **[9505: House of Wisdom](/12025-10/9505-house-of-wisdom-knowledge-gardens)** - Preserving knowledge

### External Resources
- **"Pro Git" book** - Comprehensive (free online)
- **`man git`** - Complete documentation
- **GitHub Learning Lab** - Interactive tutorials
- **Git immersion** - Hands-on course

---

## Reflection Questions

1. **Why did distributed win over centralized?** (SVN had network effects - but Git's benefits outweighed)

2. **Is immutability always good?** (Git commits immutable - but what about secrets accidentally committed?)

3. **Could Git be simpler?** (Complex UI - `git checkout` does multiple things - room for improvement)

4. **What if all software was in Git?** (Nix uses Git for nixpkgs - source of truth!)

5. **How would Nock version control work?** (Nouns as commits, all deterministic - interesting!)

---

## Summary

**Git Fundamentals**:

**Content-Addressed Objects**:
- **Blob**: File contents (hashed)
- **Tree**: Directory structure
- **Commit**: Snapshot + metadata
- **Tag**: Named reference

**Key Concepts**:
- **Commits**: Immutable snapshots
- **Branches**: Pointers to commits
- **DAG**: Directed acyclic graph (history structure)
- **Distributed**: Every clone = full repo

**Common Operations**:
- **`git add`**: Stage changes
- **`git commit`**: Create snapshot
- **`git branch`**: Create pointer
- **`git merge`**: Combine branches
- **`git push/pull`**: Sync with remote

**Why Git Won**:
- Distributed (no single point of failure)
- Fast (local operations)
- Efficient (content-addressing)
- Branching (cheap, instant)
- Offline-capable (sync later)

**Immutability**:
- Commits never change (create new instead)
- History preserved forever
- Functional programming principles applied

**In the Valley**:
- **We version everything** (code, config, docs, essays!)
- **We fork upstream** (grainhouse strategy)
- **We preserve history** (every commit is knowledge)
- **We use immutability** (never mutate, only add)

**Plant lens**: **"Git is the garden's logbook—every planting, every harvest, every season recorded. Time-travel through growth stages, learn from history."**

---

**Next**: We'll explore **testing**—how to verify code works, different test types, and why tests are essential for long-term maintenance!

---

**Navigation**:  
← Previous: [9596 (package managers dependency resolution)](/12025-10/9596-package-managers-dependency-resolution) | **Phase 1 Index** | Next: [9598 (testing verification validation)](/12025-10/9598-testing-verification-validation)

**Metadata**:
- **Phase**: 1 (Foundations)
- **Week**: 4
- **Prerequisites**: 9560, 9590, 9595
- **Concepts**: Version control, Git, commits, branches, DAG, content-addressing, immutability, distributed systems
- **Next Concepts**: Testing, unit tests, integration tests, property-based testing
- **Plant Lens**: Garden logbook (history), growth rings (commits), parallel gardens (branches), time-travel (checkout)



---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*