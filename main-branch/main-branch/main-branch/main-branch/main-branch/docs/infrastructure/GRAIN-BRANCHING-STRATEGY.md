# Grain Network Branching Strategy

**Created**: 12025-10-22--CDT  
**Purpose**: Standardized branching across all Grain Network repositories  
**Status**: Production

---

## Overview

**Single Branch Strategy**: All Grain Network repositories use a **`main` branch only** for source code, with automatic mirroring and deployment via CI/CD.

---

## Branch Structure

### Source Code: `main` Branch

**All repositories use**:
- ✅ **Branch name**: `main`
- ✅ **Purpose**: Single source of truth for all code
- ✅ **Mirrored**: Automatically synced GitHub ↔ Codeberg
- ✅ **Protected**: No force pushes, requires clean history
- ✅ **CI/CD**: Triggers on every push

**No feature branches**: We use append-only commits directly to `main` (Grainclay philosophy)

---

## Pages Deployment

### GitHub Pages: Deploys from `main`

**Configuration**:
```
Source: main branch
Path: / (root)
URL: https://<org>.github.io/<repo>/
```

**Automatic deployment**:
- Push to `main` → GitHub Actions → GitHub Pages
- No separate pages branch needed
- Uses built-in GitHub Pages action

### Codeberg Pages: Deploys from `pages`

**Configuration**:
```
Source: pages branch
Path: / (root)
URL: https://<org>.codeberg.page/<repo>/
```

**Automatic deployment**:
- Push to `main` → GitHub Actions mirror → Codeberg `main`
- GitHub Actions builds → Pushes to Codeberg `pages` branch
- Codeberg Pages auto-deploys from `pages` branch

---

## CI/CD Workflow

### Simplified Flow

```
Developer pushes to main
         ↓
GitHub Actions triggered
         ↓
    ┌────┴────┐
    ↓         ↓
 Mirror    Deploy
    to        to
Codeberg   GitHub
  main     Pages
    ↓         
  Build
    ↓
 Push to
Codeberg
 pages
    ↓
Codeberg
  Pages
```

### Detailed Steps

1. **Push to main**:
   ```bash
   git push origin main  # Codeberg
   git push github main  # GitHub
   ```

2. **GitHub Actions** (`mirror-and-deploy.yml`):
   - **Job 1**: Mirror `main` to Codeberg
   - **Job 2**: Deploy GitHub Pages from `main`
   - **Job 3**: Build and push to Codeberg `pages` branch

3. **Automatic Deployment**:
   - GitHub Pages: Live in ~1 minute
   - Codeberg Pages: Live in ~2 minutes

---

## Repository-Specific Configuration

### Personal Repos (kae3g/*)

**Example**: `kae3g/grainkae3g`

```yaml
Remotes:
  - origin: https://codeberg.org/kae3g/grainkae3g.git (main)
  - github: https://github.com/kae3g/grainkae3g.git (main)

Pages:
  - Codeberg: pages branch
  - GitHub: main branch
```

### Organization Repos (grainpbc/*)

**Example**: `grainpbc/clojure-s6`

```yaml
Remotes:
  - origin: https://codeberg.org/grainpbc/clojure-s6.git (main)
  - github: https://github.com/grainpbc/clojure-s6.git (main)

Pages:
  - Codeberg: pages branch
  - GitHub: main branch
```

---

## Migration from tundra Branch

**Old workflow** (12025-10 repository):
- Used `tundra` branch
- Complex branching strategy

**New workflow** (grainkae3g and all grainpbc repos):
- Single `main` branch
- Simpler, more standard
- Better GitHub/Codeberg compatibility

**Why the change**:
- ✅ Standard GitHub/Codeberg convention
- ✅ Easier for contributors to understand
- ✅ Better CI/CD integration
- ✅ Append-only still works (Grainclay handles versioning)
- ✅ Simpler mental model

---

## Grainclay Compatibility

**Grainclay immutable paths** work with any branch:
```
/grainclay/<repo>/<timestamp>/<path>
```

The branch name doesn't matter for Grainclay versioning - it tracks:
- Commit SHA
- Neovedic timestamp
- File path
- Content hash

**Append-only rule** is enforced via:
- Pre-commit hooks (local)
- GitHub Actions (CI)
- Grainclay verification
- Never forced pushes

---

## Setting Up New Repository

### 1. Create Repository

**GitHub**:
```bash
gh repo create grainpbc/repo-name --public
```

**Codeberg**:
- Manual creation via web interface
- Or use API (if available)

### 2. Initialize with main Branch

```bash
cd /tmp/repo-name
git init
git checkout -b main
echo "# Repo Name" > README.md
git add README.md
git commit -m "Initial commit"

# Add remotes
git remote add origin https://codeberg.org/grainpbc/repo-name.git
git remote add github https://github.com/grainpbc/repo-name.git

# Push to both
git push -u origin main
git push -u github main
```

### 3. Enable Pages

**GitHub Pages**:
```bash
gh repo edit grainpbc/repo-name \
  --enable-pages \
  --pages-branch main \
  --pages-path /
```

**Codeberg Pages**:
- Codeberg automatically creates Pages from `pages` branch
- Our CI will push to `pages` branch automatically

### 4. Add GitHub Actions

Copy `.github/workflows/mirror-and-deploy.yml` to the repository.

Update secrets in repository settings:
- `CODEBERG_TOKEN`: Your Codeberg access token

---

## Local Development Workflow

### Daily Work

```bash
cd ~/kae3g/grainkae3g

# Make changes
vim grainstore/some-module/file.clj

# Use bb flow for everything
bb flow "feat: add new feature"

# This automatically:
# - Builds
# - Commits
# - Pushes to GitHub main
# - Pushes to Codeberg main
# - Triggers CI to deploy both Pages
```

### Manual Push (if needed)

```bash
# Push to both remotes
git push origin main
git push github main

# Or use helper
bb git:push-both
```

---

## Secrets Management

### Required Secrets

**GitHub Actions needs**:
- `CODEBERG_TOKEN`: Personal access token from Codeberg

### Creating Codeberg Token

1. Go to: https://codeberg.org/user/settings/applications
2. Click "Generate New Token"
3. Name: "GitHub Actions Mirror"
4. Scopes: `repo` (full control of repositories)
5. Copy token
6. Add to GitHub: Settings → Secrets → Actions → New secret
   - Name: `CODEBERG_TOKEN`
   - Value: (paste token)

---

## Troubleshooting

### Mirror not working

**Check**:
```bash
# Verify remotes
git remote -v

# Test push manually
git push codeberg main
```

**Fix**:
- Verify CODEBERG_TOKEN is set in GitHub secrets
- Check token has `repo` scope
- Ensure Codeberg repository exists

### Pages not deploying

**GitHub Pages**:
```bash
# Check if Pages is enabled
gh api repos/kae3g/grainkae3g/pages

# Check Actions status
gh run list --repo kae3g/grainkae3g

# View failed run
gh run view <run-id> --repo kae3g/grainkae3g
```

**Codeberg Pages**:
- Check if `pages` branch exists on Codeberg
- Verify Pages is enabled in Codeberg repo settings
- Check build logs on Codeberg

---

## Philosophy

**"THE WHOLE GRAIN"** - A single `main` branch represents the complete, unified state of each project. No fragmenting across branches.

**"Making a wave and surfing the same wave"** - We create the CI/CD infrastructure (the wave) and immediately ride it to deploy everywhere (surfing).

**Grainclay principle**: Immutability is handled at the file level, not the branch level. Every commit is append-only, creating an immutable history regardless of branch strategy.

---

## Related Documentation

- `docs/infrastructure/GRAIN-NETWORK-WEBSITES.md`: All website URLs
- `grainstore/grainstore.edn`: Dual-repo configuration
- `.github/workflows/mirror-and-deploy.yml`: CI/CD implementation
- `scripts/deploy-to-pages.bb`: Pages deployment script

---

## Summary

| Aspect | Configuration |
|--------|---------------|
| **Source branch** | `main` (everywhere) |
| **GitHub Pages source** | `main` branch |
| **Codeberg Pages source** | `pages` branch |
| **Mirror frequency** | On every push to main |
| **Deployment time** | 1-2 minutes |
| **Manual intervention** | None (fully automated) |

🌾 One branch to rule them all! 🌾

