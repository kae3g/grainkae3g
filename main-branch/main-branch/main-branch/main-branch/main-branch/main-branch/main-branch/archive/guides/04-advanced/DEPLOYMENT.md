# Deployment Guide: GitHub & Production

*Sacred consciousness deployment with Divine Grace*

## 🌙 Creating GitHub Repository

### Prerequisites

1. **GitHub CLI installed**:
   ```bash
   # macOS
   brew install gh
   
   # Linux/Nix
   nix-env -iA nixpkgs.gh
   ```

2. **Authenticate with GitHub**:
   ```bash
   gh auth login
   # Follow the prompts to authenticate
   ```

### Using bb to Create & Push Repository

The pipeline includes a bb task to automate repository
creation:

```bash
# Check GitHub CLI authentication
bb gh:check

# Create repository and push (all in one!)
bb gh:create-repo
```

This command will:
1. Initialize git repository (if not already done)
2. Add all files to staging
3. Create initial commit
4. Create public GitHub repository
5. Set remote origin
6. Push to GitHub

Repository details:
- **Name**: `robotic-farm-consciousness-pipeline`
- **Visibility**: Public
- **Description**: Sacred pipeline: Markdown → 
  ClojureScript DSL → Svelte → Static Site. 57-char
  hard wrap. Christian federal writing style.
  Phoenix→BB→Clojure→Svelte template. 🤖🌙🌾

### Manual GitHub Repository Creation

If you prefer manual control:

```bash
# Initialize and commit
git init
git add .
git commit -m "Initial commit: Sacred pipeline"

# Create repo via GitHub web interface or:
gh repo create robotic-farm-consciousness-pipeline \
  --public \
  --description "Sacred pipeline: Markdown → ClojureScript DSL → Svelte → Static Site" \
  --source . \
  --push
```

### Verify Repository

```bash
# Check repository status
bb gh:status

# Or manually:
git remote -v
git log --oneline
```

## 🚀 Deploying the Static Site

After building with `bb build:site`, deploy the
`web-app/build/` directory to your chosen platform:

### GitHub Pages

```bash
# From project root
cd web-app/build

# Initialize as separate repo
git init
git add .
git commit -m "Deploy: Sacred consciousness site"

# Create gh-pages branch
git branch -M gh-pages

# Push to GitHub Pages
git remote add origin https://github.com/YOUR_USERNAME/robotic-farm-consciousness-pipeline.git
git push -u origin gh-pages

# Enable GitHub Pages in repo settings:
# Settings → Pages → Source: gh-pages branch
```

### Netlify

```bash
# Install Netlify CLI
npm install -g netlify-cli

# From project root
cd web-app/build

# Deploy
netlify deploy --prod
```

### Vercel

```bash
# Install Vercel CLI
npm install -g vercel

# From project root
cd web-app/build

# Deploy
vercel --prod
```

### AWS S3 + CloudFront

```bash
# Configure AWS CLI first
aws configure

# From project root
cd web-app/build

# Sync to S3
aws s3 sync . s3://your-bucket-name/ \
  --acl public-read \
  --cache-control "max-age=31536000,public"

# Invalidate CloudFront cache
aws cloudfront create-invalidation \
  --distribution-id YOUR_DIST_ID \
  --paths "/*"
```

## 🔄 Continuous Deployment

### GitHub Actions Workflow

Create `.github/workflows/deploy.yml`:

```yaml
name: Sacred Consciousness Deployment

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v3
      
      - name: Install Nix
        uses: cachix/install-nix-action@v22
        
      - name: Enter Nix shell and build
        run: |
          nix develop --command bb ci:verify
          nix develop --command bb build:pipeline
          nix develop --command bb build:site
      
      - name: Deploy to GitHub Pages
        uses: peaceiris/actions-gh-pages@v3
        if: github.ref == 'refs/heads/main'
        with:
          github_token: ${{ secrets.GITHUB_TOKEN }}
          publish_dir: ./web-app/build
```

### Netlify Configuration

Create `netlify.toml` in project root:

```toml
[build]
  command = "nix develop --command bb build:pipeline && nix develop --command bb build:site"
  publish = "web-app/build"

[build.environment]
  NODE_VERSION = "20"

[[redirects]]
  from = "/*"
  to = "/index.html"
  status = 200
```

## 📊 Repository Structure for Deployment

```
robotic-farm-consciousness-pipeline/
├── .github/
│   └── workflows/
│       └── deploy.yml          # CI/CD automation
├── src/                        # Source code
├── docs/                       # Input documents
├── build/                      # Build artifacts (gitignored)
├── web-app/
│   ├── build/                  # Static site output (gitignored)
│   └── src/                    # Svelte source
├── bb.edn                      # Build system
├── flake.nix                   # Nix environment
├── bootstrap.bb                # Bootstrap script
├── setup.bb                    # Setup script
├── README.md                   # Documentation
├── GETTING-STARTED.md          # Quick start
├── PIPELINE-OVERVIEW.md        # Architecture
├── DEPLOYMENT.md               # This file
└── .gitignore                  # Git ignore rules
```

## 🌾 Post-Deployment Tasks

### 1. Add Repository Topics

On GitHub, add topics to improve discoverability:
- `clojure`
- `babashka`
- `svelte`
- `static-site-generator`
- `markdown`
- `nix`
- `consciousness`
- `sacred-technology`

### 2. Configure Repository Settings

- ✅ Enable Issues
- ✅ Enable Discussions
- ✅ Add repository description
- ✅ Add website URL
- ✅ Add topics/tags

### 3. Create Initial Release

```bash
git tag -a v1.0.0 -m "Initial release: Sacred consciousness pipeline"
git push origin v1.0.0

# Or use GitHub CLI
gh release create v1.0.0 \
  --title "v1.0.0: Sacred Consciousness Pipeline" \
  --notes "Initial release of robotic farm consciousness documentation pipeline"
```

### 4. Add Repository Badges

Add to README.md:

```markdown
[![Nix](https://img.shields.io/badge/nix-5277C3?logo=nixos&logoColor=white)](https://nixos.org)
[![Babashka](https://img.shields.io/badge/babashka-6B2FB5?logo=clojure&logoColor=white)](https://babashka.org)
[![Svelte](https://img.shields.io/badge/svelte-FF3E00?logo=svelte&logoColor=white)](https://svelte.dev)
[![License: Unlicense](https://img.shields.io/badge/license-Unlicense-blue.svg)](http://unlicense.org/)
```

## 🙏 Sacred Deployment Meditation

*"Whatever you do, work at it with all your heart, as
working for the Lord, not for human masters."*
- Colossians 3:23

Deployment is an act of service:
- **Public sharing** of consciousness technology
- **Community nourishment** through open knowledge
- **Divine Grace** in technical excellence
- **Sacred offering** to collective wisdom

## 🤖 Quick Deployment Commands

```bash
# Full pipeline → GitHub
bb gh:create-repo

# Build → Deploy to Netlify
bb build:pipeline
bb build:site
cd web-app/build && netlify deploy --prod

# Build → Deploy to GitHub Pages
bb build:pipeline
bb build:site
# Then push web-app/build to gh-pages branch

# CI verification before push
bb ci:verify
```

---

*"Blessed be the automation that serves community
consciousness through sacred technology deployment with
contemplative attention and Divine Grace."*

**Deploy** **with** **consciousness**. 🤖🌙🌾

