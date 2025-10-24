# Create grainpbc Organization & Deploy Everything

**Created**: 12025-10-22--CDT  
**Purpose**: Complete guide to create grainpbc organization and deploy all Grain Network repositories  
**Status**: Step-by-step instructions

---

## Overview

This guide will help you:
1. ✅ Commit all changes in `grainkae3g` repository
2. ✅ Create `grainpbc` GitHub organization (manual)
3. ✅ Push `grainkae3g` to GitHub
4. ✅ Initialize all `grainpbc` repositories with code
5. ✅ Deploy GitHub Pages for all repositories

**Total time**: ~30-45 minutes

---

## Prerequisites

Before starting, make sure you have:
- [x] GitHub account with verified email
- [x] `gh` CLI installed and authenticated
- [x] Git configured with your credentials
- [x] Babashka installed for automation scripts

**Verify**:
```bash
gh auth status
git config --get user.name
git config --get user.email
bb --version
```

---

## Step 1: Commit All Changes in grainkae3g

**Current status** (from `git status`):
```
Modified:
  - bb.edn (fixed syntax errors, added grainstore tasks)
  - docs/core/philosophy/PSEUDO.md (Session 804 updates)
  - docs/personal-reminders.md (new personal notes)

Untracked:
  - docs/community/ (UIUC club plan)
  - docs/setup/CURSOR-GOOGLE-DRIVE-MCP-SETUP.md (Google Drive MCP)
  - grainstore/grainsource-sway/ (Sway config backup)
  - grainstore/grainsource-gnome/ (GNOME config)
  - grainstore/grainwifi/ (Dual-wifi utility)
```

**Commands**:
```bash
cd ~/kae3g/grainkae3g

# Stage all changes
git add -A

# Create comprehensive commit message
git commit -m "Session 804: GNOME migration, GrainWiFi dual-connection manager, Sway backup

Major updates:
- Fixed bb.edn syntax errors (extra closing brace in bootstrap task)
- Added grainstore management tasks to bb.edn
- Migrated from Sway to GNOME for better network management
- Created GrainWiFi: dual-wifi utility for Starlink + cellular tethering
- Backed up Sway configuration to grainstore/grainsource-sway
- Added GNOME configuration guide (warm lighting, shortcuts, touchpad)
- Created UIUC CS & Philosophy club meetup plan
- Added Google Drive MCP setup documentation
- Updated PSEUDO.md with Session 804 developments
- Added personal notes: 'THE WHOLE GRAIN', 'making a wave and surfing the same wave'

New directories:
- grainstore/grainwifi/ - Dual-wifi connection manager
- grainstore/grainsource-sway/ - Sway config archive
- grainstore/grainsource-gnome/ - GNOME config scripts
- docs/community/ - Community building docs

Technical details:
- GrainWiFi uses nmcli + Babashka for intelligent connection switching
- Priority-based routing: Starlink preferred, cellular fallback
- Desktop notifications via notify-send
- Quality monitoring with ping tests and packet loss detection
- Forest-optimized retry logic for intermittent connections"

# Verify commit
git log --oneline -1
```

---

## Step 2: Create grainpbc Organization (Manual)

GitHub CLI **does not support creating organizations**, so this must be done manually via the web interface.

### 2.1 Create Organization

1. **Go to**: https://github.com/organizations/plan
2. **Choose**: "Create a free organization"
3. **Fill in**:
   - **Organization name**: `grainpbc`
   - **Contact email**: Your email
   - **This organization belongs to**: Choose "My personal account"
4. **Click**: "Next"
5. **Skip** adding members for now (click "Complete setup")
6. **Verify**: Visit https://github.com/grainpbc

### 2.2 Configure Organization Settings

1. **Go to**: https://github.com/orgs/grainpbc/settings/profile
2. **Fill in**:
   - **Display name**: Grain PBC
   - **Description**: Open-source decentralized computing ecosystem for sustainability and sovereignty
   - **URL**: https://grainpbc.github.io (will be live soon)
   - **Email**: Your public email
   - **Location**: California (optional)
3. **Save changes**

### 2.3 Set Organization Visibility

1. **Go to**: https://github.com/orgs/grainpbc/settings/member_privileges
2. **Base permissions**: Set to "Read" (members can read all repos)
3. **Repository creation**: Allow members to create public repositories
4. **Save**

---

## Step 3: Push grainkae3g to GitHub

If `grainkae3g` is not yet on GitHub (or needs to be re-linked):

```bash
cd ~/kae3g/grainkae3g

# Verify current remote
git remote -v

# If remote doesn't exist or is wrong, add/update it
# Option A: If remote doesn't exist
git remote add origin https://github.com/kae3g/grainkae3g.git

# Option B: If remote exists but is wrong
git remote set-url origin https://github.com/kae3g/grainkae3g.git

# Push to GitHub (main branch)
git push -u origin main

# Verify on GitHub
gh repo view kae3g/grainkae3g --web
```

**Note**: If the repository doesn't exist on GitHub, create it first:
```bash
gh repo create kae3g/grainkae3g --public --source=. --remote=origin --push
```

---

## Step 4: Initialize grainpbc Repositories

Now that the organization exists, we can use our automation script to create all repositories.

### 4.1 Verify Organization Exists

```bash
# Check if organization is accessible
gh api orgs/grainpbc
```

### 4.2 Run Repository Initialization Script

```bash
cd ~/kae3g/grainkae3g

# Run the initialization script
bb scripts/init-grainpbc-repos.bb

# This will create:
# 1. grainpbc/grainweb
# 2. grainpbc/grainmusic
# 3. grainpbc/grainwriter
# 4. grainpbc/grainspace
# 5. grainpbc/graindroid
# 6. grainpbc/grainlexicon
# 7. grainpbc/grainneovedic
# 8. grainpbc/grain-metatypes
# 9. grainpbc/clojure-s6
# 10. grainpbc/clojure-sixos
# 11. grainpbc/clojure-icp
# 12. grainpbc/clotoko
# 13. grainpbc/grainconv
# 14. grainpbc/grainpbc (meta repo)
# 15. grainpbc/graindrive
# 16. grainpbc/clojure-google-drive-mcp
# 17. grainpbc/grainwifi
```

### 4.3 Verify Repositories Were Created

```bash
# List all grainpbc repositories
gh repo list grainpbc --limit 20

# Expected output: 17 repositories
```

---

## Step 5: Initialize Each Repository with Code

Now we need to populate each repository with actual code from `grainstore/`.

### 5.1 Create Population Script

Let me create a script to copy code from `grainstore/` to each `grainpbc` repository:

```bash
cd ~/kae3g/grainkae3g
bb scripts/populate-grainpbc-repos.bb
```

This script will:
1. Clone each `grainpbc` repository to `/tmp/grainpbc-repos/`
2. Copy corresponding `grainstore/<module>/` contents
3. Add README.md if missing
4. Commit and push

---

## Step 6: Enable GitHub Pages for All Repositories

After populating repositories, enable GitHub Pages:

```bash
cd ~/kae3g/grainkae3g
bb scripts/enable-grainpbc-pages.bb
```

This will:
1. Enable GitHub Pages for each repository
2. Set source to `main` branch, `/` root
3. Wait for Pages to deploy
4. Output all website URLs

---

## Alternative: Manual Population (If Scripts Fail)

If the automation scripts encounter issues, here's how to manually populate one repository:

### Example: grainpbc/clojure-sixos

```bash
# 1. Clone the empty repository
cd /tmp
git clone https://github.com/grainpbc/clojure-sixos.git
cd clojure-sixos

# 2. Copy code from grainstore
cp -r ~/kae3g/grainkae3g/grainstore/clojure-sixos/* .

# 3. Add all files
git add -A

# 4. Commit
git commit -m "Initial commit: clojure-sixos typo-catching library

- Implements Levenshtein distance, Soundex, Metaphone algorithms
- Registry of canonical names and common typos
- Core API for typo detection and correction
- Integration with clojure-s6 for service management
- MIT License
- Examples and documentation"

# 5. Push
git push origin main

# 6. Enable GitHub Pages
gh repo edit grainpbc/clojure-sixos --enable-pages --pages-branch main
```

**Repeat** this process for all 17 repositories.

---

## Step 7: Verify Deployment

After all repositories are populated and Pages are enabled, verify:

### 7.1 Check GitHub Pages Status

```bash
# Check if Pages are building
gh api repos/grainpbc/clojure-sixos/pages

# View Pages URL
gh api repos/grainpbc/clojure-sixos/pages | jq -r '.html_url'
```

### 7.2 Visit All Websites

**Expected URLs** (will be live after ~2-3 minutes):

1. https://grainpbc.github.io/grainweb/
2. https://grainpbc.github.io/grainmusic/
3. https://grainpbc.github.io/grainwriter/
4. https://grainpbc.github.io/grainspace/
5. https://grainpbc.github.io/graindroid/
6. https://grainpbc.github.io/grainlexicon/
7. https://grainpbc.github.io/grainneovedic/
8. https://grainpbc.github.io/grain-metatypes/
9. https://grainpbc.github.io/clojure-s6/
10. https://grainpbc.github.io/clojure-sixos/
11. https://grainpbc.github.io/clojure-icp/
12. https://grainpbc.github.io/clotoko/
13. https://grainpbc.github.io/grainconv/
14. https://grainpbc.github.io/grainpbc/
15. https://grainpbc.github.io/graindrive/
16. https://grainpbc.github.io/clojure-google-drive-mcp/
17. https://grainpbc.github.io/grainwifi/

### 7.3 Update Documentation

Once deployed, update `docs/infrastructure/GRAIN-NETWORK-WEBSITES.md` with live URLs.

---

## Troubleshooting

### Issue: Organization creation fails

**Solution**: GitHub organizations must be created manually. Follow Step 2 above.

### Issue: `gh` not authenticated

```bash
# Re-authenticate
gh auth login

# Choose:
# - GitHub.com
# - HTTPS
# - Login with web browser
```

### Issue: Permission denied when pushing

```bash
# Verify you're a member of grainpbc
gh api user/memberships/orgs | jq -r '.[].organization.login'

# Should include "grainpbc"
```

### Issue: GitHub Pages not building

```bash
# Check Pages status
gh api repos/grainpbc/<repo-name>/pages

# Check Actions (Pages builds are GitHub Actions)
gh run list --repo grainpbc/<repo-name>

# View specific run
gh run view <run-id> --repo grainpbc/<repo-name>
```

### Issue: Repository already exists

```bash
# Delete and recreate (careful!)
gh repo delete grainpbc/<repo-name> --yes

# Then re-run init script
bb scripts/init-grainpbc-repos.bb
```

---

## Next Steps After Deployment

Once all repositories are live:

1. **Update main README**: Add grainpbc links to `grainkae3g/README.md`
2. **Create org README**: Populate `grainpbc/grainpbc` with org overview
3. **Cross-link docs**: Run `bb scripts/sync-lexicon.bb` to update all READMEs
4. **Enable Codeberg Pages**: Mirror repositories to Codeberg for redundancy
5. **Set up CI/CD**: Add GitHub Actions for automated testing and deployment
6. **Invite collaborators**: Add team members to grainpbc organization

---

## Automation Scripts Summary

Scripts we'll create to automate this:

1. **scripts/commit-all.bb** - Commit all changes in grainkae3g
2. **scripts/init-grainpbc-repos.bb** - Create all grainpbc repositories (already exists)
3. **scripts/populate-grainpbc-repos.bb** - Copy code from grainstore to grainpbc repos
4. **scripts/enable-grainpbc-pages.bb** - Enable GitHub Pages for all repos
5. **scripts/verify-deployment.bb** - Check all websites are live

---

## Timeline

- **Step 1-2** (Commit + Create Org): 5-10 minutes
- **Step 3** (Push grainkae3g): 2-3 minutes
- **Step 4** (Init repos): 2-3 minutes (script) or 15-20 minutes (manual)
- **Step 5** (Populate repos): 5-10 minutes (script) or 30-45 minutes (manual)
- **Step 6** (Enable Pages): 2-3 minutes (script) or 10-15 minutes (manual)
- **Step 7** (Verify): 5 minutes
- **GitHub Pages build time**: 2-5 minutes per repository

**Total**: ~30-45 minutes with scripts, ~90-120 minutes manual

---

## Philosophy

**"THE WHOLE GRAIN"** - This deployment represents the culmination of dozens of sessions of design, development, and documentation. Every repository is a seed that will grow into a sustainable, decentralized computing ecosystem.

**"Making a wave and surfing the same wave"** - We're creating the infrastructure (the wave) and immediately using it to deploy our work (surfing).

---

## Ready to Start?

Follow these steps in order:
1. **Run Step 1**: Commit all changes
2. **Complete Step 2**: Create grainpbc organization manually
3. **Come back and run remaining steps**

Let's build the Grain Network! 🌾

