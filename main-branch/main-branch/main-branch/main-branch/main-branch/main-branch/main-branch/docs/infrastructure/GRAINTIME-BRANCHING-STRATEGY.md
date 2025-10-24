# 🌾 Graintime-Based Git Branching Strategy

## Overview

The Grain Network uses a **graintime-based branching strategy** that creates visible, countable diffs with social features and analytics on GitHub and Codeberg. Each branch represents a moment in the eternal now, following the `now == next + 1` philosophy.

## Branch Naming Convention

```
graintime-{grainpath}
```

**Example:**
```
graintime-12025-10-23--0440--PDT--moon-vishakha------asc-gem000--sun-03rd--kae3g
```

## Philosophy Integration

### 🌾 The 88 Counter Philosophy in Git

- **88 branches** = Individual development cycles
- **88 × 10^1 = 880** = Small feature groups  
- **88 × 10^2 = 8,800** = Large system updates
- **88 × 10^n = ∞** = Infinite development potential

### 🕐 Temporal Recursion

Each branch represents a moment in time:
- **Past branches** = Completed work (merged to main)
- **Present branch** = Current development
- **Future branches** = Planned features

## Branch Lifecycle

### 1. 🌱 Creation (Jodo 1: The Beginning)
```bash
git checkout -b graintime-{grainpath}
```

### 2. 🌿 Development (Jodo 2: The Journey)
```bash
# Make changes
git add .
git commit -m "feat: {description} - {grainpath}"

# Push to remote
git push origin graintime-{grainpath}
```

### 3. 🌾 Harvest (Jodo 3: The Return)
```bash
# Create Pull Request
# Merge to main
# Delete branch
```

## GitHub/Codeberg Integration

### 📊 Analytics & Social Features

**GitHub Features:**
- **Pull Request Analytics** - Track development velocity
- **Commit Graphs** - Visualize development patterns
- **Issue Tracking** - Link branches to issues
- **Project Boards** - Organize by graintime

**Codeberg Features:**
- **Mirror Analytics** - Compare development across platforms
- **Community Features** - Collaborative development
- **Fork Analytics** - Track community contributions

### 🔄 Dual-Platform Strategy

**Primary Development:**
- GitHub: Main development and CI/CD
- Codeberg: Community mirror and collaboration

**Branch Synchronization:**
```bash
# Push to both platforms
git push origin graintime-{grainpath}
git push codeberg graintime-{grainpath}
```

## Visual Development Timeline

```
Past Branches (Merged)     Present Branch        Future Branches (Planned)
     ↓                        ↓                        ↓
┌─────────────┐         ┌─────────────┐         ┌─────────────┐
│ graintime-  │         │ graintime-  │         │ graintime-  │
│ 12025-10-22 │         │ 12025-10-23 │         │ 12025-10-24 │
│ --0400--PDT │         │ --0440--PDT │         │ --0800--PDT │
└─────────────┘         └─────────────┘         └─────────────┘
     ↓                        ↓                        ↓
   Merged                  Active                  Planned
   to main                 Development             Features
```

## Branch Types

### 🌾 Feature Branches
```
graintime-{grainpath}-feature-{name}
```

**Example:**
```
graintime-12025-10-23--0440--PDT--moon-vishakha------asc-gem000--sun-03rd--kae3g-feature-ascii-art
```

### 🔧 Hotfix Branches
```
graintime-{grainpath}-hotfix-{issue}
```

**Example:**
```
graintime-12025-10-23--0440--PDT--moon-vishakha------asc-gem000--sun-03rd--kae3g-hotfix-draw-command
```

### 📚 Documentation Branches
```
graintime-{grainpath}-docs-{topic}
```

**Example:**
```
graintime-12025-10-23--0440--PDT--moon-vishakha------asc-gem000--sun-03rd--kae3g-docs-grainlib
```

## Workflow Commands

### 🚀 Quick Start
```bash
# Create new graintime branch
gb branch new "feature description"

# This creates: graintime-{current-grainpath}-feature-{description}
```

### 🔄 Development Cycle
```bash
# 1. Create branch
gb branch new "ascii art documentation"

# 2. Make changes
# ... edit files ...

# 3. Commit with graintime
gb commit "feat: add ASCII art documentation system"

# 4. Push to both platforms
gb push

# 5. Create PR
gb pr create "Add ASCII art documentation system"
```

### 📊 Analytics Commands
```bash
# View branch timeline
gb timeline

# View development velocity
gb velocity

# View social features
gb social
```

## Integration with Grain Network

### 🌾 Core Philosophy Applied

**Local Control:**
- Each branch is controlled locally
- Developers have full control over their branches

**Global Intent:**
- All branches work toward the same goal
- Network-wide coordination through PRs

**Purpose-Built:**
- Each branch serves a specific purpose
- No generic branches

**Template/Personal:**
- Base templates for common branch types
- Personal customization for specific needs

### 🔄 Temporal Recursion

```
now == next + 1

Current Branch:
├── Past: Previous branches (merged)
├── Present: Current development
└── Future: Planned branches

Each branch is a grain in the eternal network.
Each merge is a harvest of knowledge.
Each PR is a contribution to the whole.
```

## Benefits

### 📈 Development Visibility
- **Clear Timeline** - See development progression
- **Countable Diffs** - Track changes over time
- **Social Features** - Community collaboration
- **Analytics** - Measure development velocity

### 🌾 Philosophy Integration
- **88 Counter Scaling** - Branch organization
- **Temporal Recursion** - Time-aware development
- **Network Growth** - Collaborative development
- **Purpose-Built** - Each branch has a purpose

### 🔄 Multi-Platform
- **GitHub** - Main development platform
- **Codeberg** - Community mirror
- **Synchronization** - Keep both platforms in sync
- **Analytics** - Compare development across platforms

## Example Workflow

### 🌱 Session 813: ASCII Art Documentation

**Branch Creation:**
```bash
gb branch new "ascii art documentation system"
# Creates: graintime-12025-10-23--0440--PDT--moon-vishakha------asc-gem000--sun-03rd--kae3g-feature-ascii-art
```

**Development:**
```bash
# Make changes to grainlib
git add grainstore/grainlib/src/grainlib/ascii_art_specs.clj
git commit -m "feat: add ASCII art documentation system - 761 lines of beautiful code"

# Create personal notes
git add 12025-10-23--0420--PDT--moon-vishakha------asc-gem000--sun-03rd--kae3g/
git commit -m "feat: add endless sky personal note - Jodo 1 2 3 trilogy"

# Push to both platforms
git push origin graintime-12025-10-23--0440--PDT--moon-vishakha------asc-gem000--sun-03rd--kae3g-feature-ascii-art
git push codeberg graintime-12025-10-23--0440--PDT--moon-vishakha------asc-gem000--sun-03rd--kae3g-feature-ascii-art
```

**PR Creation:**
```bash
# Create PR on GitHub
gh pr create --title "Add ASCII Art Documentation System" --body "Implements comprehensive ASCII art documentation with Basho poetry integration"

# Create PR on Codeberg
# (Manual creation through web interface)
```

**Merge:**
```bash
# Merge to main
git checkout main
git merge graintime-12025-10-23--0440--PDT--moon-vishakha------asc-gem000--sun-03rd--kae3g-feature-ascii-art
git push origin main
git push codeberg main

# Delete branch
git branch -d graintime-12025-10-23--0440--PDT--moon-vishakha------asc-gem000--sun-03rd--kae3g-feature-ascii-art
```

## Conclusion

The graintime-based branching strategy creates a visible, countable development process that integrates with the Grain Network philosophy. Each branch represents a moment in the eternal now, contributing to the infinite growth of the network.

**🌾 now == next + 1** • Each branch is a grain in the eternal network • Each merge is a harvest of knowledge • Each PR is a contribution to the whole 🌾
