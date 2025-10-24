# 🌾 GitHub Organization Setup Guide
## Creating `grainpbc` Organization with `kae3g` as Admin

**Organization Name:** `grainpbc`  
**Primary Admin:** `kae3g`  
**Type:** Free (public repositories) or GitHub Teams ($4/user/month)  
**Purpose:** Centralized repository management for Grain Public Benefit Corporation projects

---

## 📋 ORGANIZATION STRUCTURE

### GitHub Organizations

We'll create **two** GitHub organizations to match our corporate structure:

1. **`grainpbc`** - Main for-profit corporation repositories
   - Products: Grainwriter, Graincamera, Grainspace
   - Business documentation
   - Legal documents (public)
   - Website source code

2. **`graingives`** - Nonprofit organization (future)
   - Educational content
   - Student resources
   - Grant program documentation
   - Open-source project funding

---

## 🚀 STEP-BY-STEP SETUP

### Step 1: Create `grainpbc` Organization

**Via GitHub Web Interface:**

1. **Navigate to:** https://github.com/organizations/plan
2. **Click:** "Create organization"
3. **Choose Plan:**
   - **Free:** Unlimited public repos, basic features
   - **Teams ($4/user/month):** Private repos, advanced collaboration
   - **Recommendation:** Start with Free, upgrade later
4. **Organization Details:**
   - **Organization name:** `grainpbc`
   - **Contact email:** kj3x39@gmail.com
   - **This organization belongs to:** My personal account (kae3g)
5. **Click:** "Next"

**Organization Profile:**
- **Display name:** Grain Public Benefit Corporation
- **Description:** Personal sovereignty computing through open-source, sustainable hardware and software
- **Website:** https://grain.network (when live)
- **Location:** California, United States
- **Email:** hello@grain.network (set up later)

6. **Add Members (initially just kae3g):**
   - kae3g will be Owner by default
7. **Click:** "Complete setup"

### Step 2: Configure Organization Settings

**Via:** https://github.com/organizations/grainpbc/settings/profile

**Profile Settings:**
- [x] Display organization name
- [x] Display organization website
- [x] Display organization location
- **Avatar:** Upload Grain logo (create 500x500px icon)
- **Pinned repositories:** Pin top 3-5 repos to profile

**Member Privileges:**
- **Base permissions:** Read (default)
- **Repository creation:** Members can create public repositories
- **Repository forking:** Allow forking of private repositories (if Teams plan)
- **Pages creation:** Allow members to publish GitHub Pages

**Security:**
- [x] Require two-factor authentication for all members
- [ ] Allow admin deletion (keep disabled for safety)
- [x] Enable dependency graph
- [x] Enable Dependabot alerts
- [x] Enable Dependabot security updates

**GitHub Actions:**
- [x] Enable GitHub Actions
- **Actions permissions:** Allow all actions and reusable workflows
- **Workflow permissions:** Read and write permissions

**Verified Domains (Optional, later):**
- Add `grain.network` when domain purchased

---

## 📦 REPOSITORY MIGRATION PLAN

### From `kae3g/grainkae3g` to `grainpbc/grainkae3g`

We have two options:

#### Option 1: Transfer Ownership (Recommended)

**Pros:**
- Preserves all history, stars, forks, issues, PRs
- GitHub automatically redirects old URLs
- No data loss

**Cons:**
- Original `kae3g/grainkae3g` will no longer exist

**Steps:**
1. Go to: https://github.com/kae3g/grainkae3g/settings
2. Scroll to "Danger Zone"
3. Click "Transfer ownership"
4. Enter: `grainpbc`
5. Type repository name to confirm
6. Transfer complete!

**Result:**
- Old URL: `github.com/kae3g/grainkae3g` → redirects to `github.com/grainpbc/grainkae3g`
- kae3g maintains admin access as organization owner

#### Option 2: Fork and Archive

**Pros:**
- Keeps original repo under `kae3g` for personal reference
- Cleaner separation of personal vs. corporate work

**Cons:**
- Loses stars, watchers, forks from original
- No automatic redirects

**Steps:**
1. Fork `kae3g/grainkae3g` to `grainpbc/grainkae3g` (as organization owner)
2. Archive original `kae3g/grainkae3g` repository
3. Add README notice pointing to new location

---

## 🗂️ REPOSITORY STRUCTURE

### Main Repositories Under `grainpbc`

```
grainpbc/
├── grainkae3g                    # Main project repo (transferred from kae3g)
├── grainwriter                   # Grainwriter hardware/software
├── graincamera                   # Graincamera open hardware
├── grainspace                    # Grainspace platform
├── grainpbc.github.io            # Organization website (GitHub Pages)
├── clojure-s6                    # s6 supervision library
├── clojure-sixos                 # SixOS development library
├── grainphotos                   # Photo management library
├── humble-social-client          # Humble UI social client
├── grain-legal                   # Legal documents and templates
├── grain-design                  # Design assets, branding
└── grain-docs                    # Consolidated documentation
```

### Repository Settings Template

**Default Branch:** `main`  
**Branch Protection Rules:**
- [x] Require pull request reviews (1 approver)
- [x] Require status checks to pass
- [x] Require conversation resolution before merging
- [x] Require linear history
- [ ] Include administrators (disable for rapid development)

**GitHub Pages (for website repos):**
- Source: Deploy from branch `main` or `gh-pages`
- Custom domain: `grain.network` (when ready)
- HTTPS: Enforce HTTPS

**License:**
- Hardware: CERN-OHL-S-2.0
- Software: MIT
- Documentation: CC BY-SA 4.0

---

## 👥 TEAM STRUCTURE

### Organization Teams

**1. Core Team (Owners)**
- **Members:** kae3g (initially)
- **Permissions:** Admin on all repositories
- **Role:** Strategic direction, all repository access

**2. Hardware Team**
- **Members:** Hardware engineers (future hires)
- **Permissions:** Write on grainwriter, graincamera repos
- **Role:** PCB design, enclosure, testing

**3. Software Team**
- **Members:** Software engineers, Clojure developers (future)
- **Permissions:** Write on software repos
- **Role:** Firmware, libraries, applications

**4. Documentation Team**
- **Members:** Technical writers, community contributors
- **Permissions:** Write on grain-docs, website repos
- **Role:** Docs, guides, tutorials

**5. Legal Team**
- **Members:** Attorney, compliance officer (future)
- **Permissions:** Write on grain-legal repo
- **Role:** Corporate governance, compliance

**6. Community Contributors**
- **Members:** Open-source contributors (public)
- **Permissions:** Fork and PR (no direct write)
- **Role:** Community contributions, bug fixes

---

## 🔐 SECURITY & COMPLIANCE

### Security Policies

**Create:** `grainpbc/.github` repository (special repo for org-wide settings)

**Files:**
- `SECURITY.md` - Security policy and vulnerability reporting
- `CODE_OF_CONDUCT.md` - Community guidelines
- `CONTRIBUTING.md` - Contribution guidelines
- `SUPPORT.md` - Support channels

**Example `.github/SECURITY.md`:**
```markdown
# Security Policy

## Supported Versions

| Version | Supported          |
| ------- | ------------------ |
| 1.x     | :white_check_mark: |

## Reporting a Vulnerability

Please report security vulnerabilities to: security@grain.network

We will respond within 48 hours and provide updates every 7 days.

Do not publicly disclose vulnerabilities until patched.
```

### Branch Protection

**Main Branch Protection (apply to all repos):**
```yaml
# .github/settings.yml (using probot/settings)
branches:
  - name: main
    protection:
      required_pull_request_reviews:
        required_approving_review_count: 1
        dismiss_stale_reviews: true
      required_status_checks:
        strict: true
        contexts:
          - ci/tests
          - ci/lint
      enforce_admins: false
      restrictions: null
```

---

## 📊 GITHUB PROJECTS & PLANNING

### Organization-Level Projects

**Create Projects at:** https://github.com/orgs/grainpbc/projects

**1. Product Roadmap (Kanban Board)**
- Columns: Backlog | In Progress | Review | Done
- Items: Grainwriter, Graincamera, Grainspace features

**2. Legal & Compliance Tracker**
- Columns: To File | In Review | Approved | Filed
- Items: Articles, Bylaws, Benefit Reports

**3. Community Contributions**
- Columns: Submitted | Reviewing | Approved | Merged
- Items: PRs from community contributors

**4. graingives.org Launch**
- Columns: Planning | In Progress | Complete
- Items: 501(c)(3) filing, grant program, website

---

## 💰 GITHUB SPONSORS

### Enable Sponsorships for `grainpbc`

**Via:** https://github.com/organizations/grainpbc/sponsors

**Tiers (Suggested):**

| Tier | Monthly | Benefits |
|------|---------|----------|
| **Supporter** | $5 | Name in README, Discord access |
| **Contributor** | $25 | Early access to builds, stickers |
| **Patron** | $100 | Monthly dev updates, design assets |
| **Sustainer** | $500 | Quarterly video call, hardware discount |
| **Enterprise** | $2,000 | Logo on website, priority support |

**Funds Usage:**
- 50% - Operating expenses (hosting, domains, tools)
- 30% - Developer grants (via graingives.org)
- 20% - Hardware prototypes and testing

---

## 🌐 DOMAIN & WEBSITE

### Domain Registration

**Primary Domain:** `grain.network`  
**Registrar:** Namecheap, Cloudflare, or Google Domains  
**Cost:** ~$12-30/year  
**Privacy:** Enable WHOIS privacy

**Additional Domains (Optional):**
- `grainpbc.com` → redirect to grain.network
- `grainwriter.com` → redirect to grain.network/grainwriter
- `graingives.org` → separate site for nonprofit

### GitHub Pages Setup

**Repository:** `grainpbc/grainpbc.github.io`

**Custom Domain Configuration:**
1. Create CNAME record: `grainpbc.github.io` → `grain.network`
2. Add `CNAME` file in repo root with: `grain.network`
3. Enable "Enforce HTTPS" in repo settings

**Website Stack (Options):**
1. **SvelteKit** (current, recommended)
   - Fast, modern, SSG support
   - Deploy via GitHub Actions

2. **Clojure/Babashka Static Site**
   - Pure Clojure, fits ecosystem
   - Custom build pipeline

3. **Hugo/Jekyll** (fallback)
   - Simple, GitHub Pages native

---

## 📅 MIGRATION TIMELINE

### Week 1: Organization Setup
- [x] Create `grainpbc` GitHub organization
- [ ] Configure organization settings
- [ ] Enable 2FA requirement
- [ ] Create organization profile (avatar, bio)

### Week 2: Repository Migration
- [ ] Transfer `kae3g/grainkae3g` to `grainpbc/grainkae3g`
- [ ] Create `grainwriter`, `graincamera`, `grainspace` repos
- [ ] Move grainstore submodules to individual repos
- [ ] Update all repo README files with new organization

### Week 3: Team & Permissions
- [ ] Create organization teams structure
- [ ] Set up branch protection rules
- [ ] Create `.github` repository with policies
- [ ] Enable GitHub Actions for all repos

### Week 4: Website & Public Presence
- [ ] Register `grain.network` domain
- [ ] Create `grainpbc.github.io` website repo
- [ ] Deploy website with legal docs
- [ ] Announce organization on social media

---

## 🔄 CLI COMMANDS REFERENCE

### Transfer Repository via GitHub CLI

```bash
# Install GitHub CLI (if not already)
# Ubuntu: sudo apt install gh
# macOS: brew install gh

# Authenticate
gh auth login

# Transfer repository
gh repo view kae3g/grainkae3g
gh repo transfer kae3g/grainkae3g grainpbc

# Verify transfer
gh repo view grainpbc/grainkae3g
```

### Clone New Organization Repos

```bash
# Update local remotes after transfer
cd /home/xy/kae3g/grainkae3g
git remote set-url origin git@github.com:grainpbc/grainkae3g.git
git remote -v

# Clone other repos
mkdir -p /home/xy/grainpbc
cd /home/xy/grainpbc
gh repo clone grainpbc/grainwriter
gh repo clone grainpbc/graincamera
gh repo clone grainpbc/grainspace
```

### Create New Repos via CLI

```bash
# Create new repository under grainpbc org
gh repo create grainpbc/grainwriter \
  --public \
  --description "Open-source e-ink writing device with infinite battery life" \
  --homepage "https://grain.network/grainwriter" \
  --enable-issues \
  --enable-wiki=false

# Create with README, license, and gitignore
gh repo create grainpbc/graincamera \
  --public \
  --add-readme \
  --license CERN-OHL-S-2.0 \
  --gitignore=Clojure
```

---

## ✅ POST-SETUP CHECKLIST

### Organization Configuration
- [ ] Organization profile complete (avatar, bio, website)
- [ ] 2FA required for all members
- [ ] Security policies enabled (Dependabot, code scanning)
- [ ] Member privileges configured

### Repository Setup
- [ ] All repositories transferred or created
- [ ] Branch protection rules applied
- [ ] README files updated with organization branding
- [ ] LICENSE files in all repos

### Team Structure
- [ ] Core team created with kae3g as owner
- [ ] Team permissions configured
- [ ] Future team structure documented

### Public Presence
- [ ] `grain.network` domain registered
- [ ] GitHub Pages website deployed
- [ ] Legal documents published
- [ ] Social media accounts created (Twitter, Mastodon, Bluesky)

### Automation
- [ ] GitHub Actions workflows configured
- [ ] Dependabot enabled
- [ ] Issue/PR templates created
- [ ] Release automation set up

---

**Next Actions:**
1. Run: `gh auth login` to authenticate GitHub CLI
2. Create organization via web interface (https://github.com/organizations/plan)
3. Transfer `grainkae3g` repository to `grainpbc`
4. Update this document with actual organization URL
5. Begin repository migrations

---

**Last Updated:** January 22, 2025  
**Document Version:** 1.0  
**Status:** [IMPLEMENTATION-READY]  
**Author:** kae3g  

**Organization:** grainpbc (pending creation)  
**GitHub:** github.com/grainpbc (pending)  
**Website:** grain.network (pending)

*"Building our digital sovereignty infrastructure, one commit at a time."* 🌾💻


