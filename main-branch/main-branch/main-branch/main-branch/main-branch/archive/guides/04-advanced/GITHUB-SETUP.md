# GitHub Setup: kae3g/12025-10-04

*"By their fruits you will know them."*
- Gospel According to Jesus (Stephen Mitchell)

Complete guide for creating and configuring the
GitHub repository.

## Repository Details

**Name:** kae3g/12025-10-04
**URL:** codeberg.org/kae3g/12025-10-04
**Visibility:** Public
**Description:** Aspiring federal documentation
pipeline
**License:** UNLICENSE

## Step 1: Authenticate GitHub CLI

```bash
gh auth login
```

Follow prompts to authenticate with your GitHub
account.

## Step 2: Create Repository

### Automated (Recommended)

```bash
bb gh:create-repo
```

This command:
1. Creates the repository
2. Sets up remote
3. Pushes all commits

### Manual with GitHub CLI

```bash
gh repo create kae3g/12025-10-04 \
  --public \
  --description "Aspiring federal documentation pipeline" \
  --source . \
  --push
```

### Manual with GitHub Web

1. Go to: https://github.com/new
2. Owner: kae3g
3. Name: 12025-10-04
4. Visibility: Public
5. No initialization (we have files)
6. Click "Create repository"

Then push locally:

```bash
git remote add origin https://codeberg.org/kae3g/12025-10-04.git
git branch -M main
git push -u origin main
```

## Step 3: Configure Repository

### Add Topics

Settings → General → Topics:

- babashka
- clojure
- svelte
- nix
- static-site
- aspiring-federal-documentation
- markdown-pipeline
- civic-tech

### Enable Features

- ✅ Issues: Enabled
- ✅ Discussions: Optional
- ✅ Wiki: Disabled (use guides/)
- ✅ Projects: Optional

### GitHub Pages (Optional)

Settings → Pages:
- Source: GitHub Actions or Deploy from branch

## Step 4: Verify Deployment

```bash
# Check remote
git remote -v

# View on GitHub
gh repo view --web

# Check status
bb gh:status
```

Visit: https://codeberg.org/kae3g/12025-10-04

Should show:
- ✅ All files present
- ✅ README renders correctly
- ✅ Topics displayed
- ✅ License visible

## GPG Signing (Optional)

If GPG database is not locked:

```bash
# Sign previous commit
git commit --amend -S --no-edit

# Force push with lease
git push --force-with-lease
```

If GPG is locked, defer signing or see
`SETUP-GPG.md` for troubleshooting.

## Post-Creation Steps

### Create Release

```bash
git tag -a v1.0.0 -m "Initial release"
git push origin v1.0.0

# Or use GitHub CLI
gh release create v1.0.0 \
  --title "v1.0.0: Initial Release" \
  --notes-file CHANGELOG.md
```

### Clone Verification

Test cloning from another location:

```bash
cd /tmp
git clone https://codeberg.org/kae3g/12025-10-04.git
cd 12025-10-04
bb bootstrap
bb doctor
```

## Troubleshooting

**Authentication fails:**
```bash
gh auth logout
gh auth login
```

**Remote already exists:**
```bash
git remote remove origin
git remote add origin https://codeberg.org/kae3g/12025-10-04.git
```

**Push rejected:**
```bash
git pull origin main --rebase
git push origin main
```

---

**Navigation:**
- [← Back: Deployment](DEPLOYMENT.md)
- [Next: Final Summary →](FINAL-SUMMARY.md)
- [Start Here](../00-START-HERE.md)
- [Home: README](../../README.md)

