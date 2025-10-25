# Configuration Guide

**Created**: 2025-10-10  
**Purpose**: Fork-friendly configuration system  
**Status**: Production Ready

---

## Overview

The kae3g project uses an **EDN-based configuration system** that separates:
- **Public templates** (committed to git)
- **Personal config** (gitignored, never committed)
- **Generated files** (auto-created from templates)

---

## Quick Start

### 1. Copy Templates

```bash
# Site configuration
cp .config.template.edn .config.edn

# Personal secrets (optional)
cp .secrets.template.edn .secrets.edn
```

### 2. Edit Your Details

```bash
# Edit site config
vim .config.edn

# Edit secrets (if needed)
vim .secrets.edn
```

### 3. Generate Web Config

```bash
# Generate JavaScript config and Svelte components
bb scripts/generate-site-config.bb
```

### 4. Build and Deploy

```bash
# Build content
bb writings:build

# Build site
cd web-app && npm run build
```

---

## Configuration Files

### .config.edn (Site Settings)

**What it controls**:
- Site title and subtitle
- URLs (website, GitHub repo)
- Author information
- License settings
- Footer content
- Theme colors
- Feature flags

**Example**:
```clojure
{:site
 {:title "kae3g"
  :subtitle "Experimental aspiringly helpful generative AI writings"
  :url "https://kae3g.codeberg.page/12025-10/"
  :github-repo "https://codeberg.org/kae3g/12025-10/"}
 
 :author
 {:name "kae3g"
  :copyright-year "2025"}
 
 :license
 {:type "dual"
  :primary "Apache-2.0"
  :secondary "MIT"}}
```

**Customization**:
- Change author name
- Update copyright year
- Modify URLs for your deployment
- Choose different license
- Customize footer tagline
- Adjust theme colors

### .secrets.edn (Personal Data)

**What it controls**:
- Git user name and email
- GitHub credentials
- API keys (OpenAI, Anthropic, Grok)
- Deployment URLs
- Personal preferences
- Hardware specs

**Example**:
```clojure
{:git
 {:user-name "Your Name"
  :user-email "your.email@example.com"}
 
 :github
 {:username "yourusername"
  :token "ghp_..."}  ; Optional
 
 :framework
 {:laptop-model "Framework 16"
  :cpu "AMD Ryzen 7 7840HS"}}
```

**Security**:
- ⚠️ **Never commit .secrets.edn!**
- ✅ It's in .gitignore
- ✅ Contains sensitive tokens
- ✅ Personal to each developer

---

## Generated Files

### web-app/src/lib/config.js

**Auto-generated JavaScript config** from .config.edn:

```javascript
export const siteConfig = {
  "site": {
    "title": "kae3g",
    "subtitle": "Experimental aspiringly helpful generative AI writings",
    // ...
  },
  // ...
};
```

**Usage in Svelte**:
```svelte
<script>
  import { siteConfig } from '$lib/config.js';
  
  const title = siteConfig.site.title;
  const author = siteConfig.author.name;
</script>

<h1>{title}</h1>
<p>By {author}</p>
```

### web-app/src/lib/LicenseFooter.svelte

**Auto-generated footer component** from .config.edn:

```svelte
<div class="license-footer">
  <p>
    <strong>Copyright © 2025 <a href="...">kae3g</a></strong><br>
    Dual-licensed under Apache-2.0 / MIT
  </p>
  <p class="tagline">
    <em>Contemplative technology...</em> 🌱
  </p>
</div>
```

**Usage**:
```svelte
<script>
  import LicenseFooter from '$lib/LicenseFooter.svelte';
</script>

<LicenseFooter />
```

---

## Workflow

### For Maintainers

```bash
# 1. Edit config
vim .config.edn

# 2. Generate web config
bb scripts/generate-site-config.bb

# 3. Rebuild content
bb writings:build

# 4. Test locally
cd web-app && npm run dev

# 5. Commit generated files
git add web-app/src/lib/config.js web-app/src/lib/LicenseFooter.svelte
git commit -m "chore: regenerate config from .config.edn"
```

### For Contributors

```bash
# 1. Fork the repo
git clone https://codeberg.org/kae3g/12025-10.git

# 2. Copy templates
cp .config.template.edn .config.edn
cp .secrets.template.edn .secrets.edn

# 3. Customize for your fork
vim .config.edn  # Change author, URLs, etc.

# 4. Generate
bb scripts/generate-site-config.bb

# 5. Build and test
bb writings:build
cd web-app && npm run dev
```

---

## What's Committed vs. Gitignored

### ✅ Committed (Templates)
- `.config.template.edn` - Site config template
- `.secrets.template.edn` - Secrets template
- `scripts/generate-site-config.bb` - Generator script
- Generated files (auto-created, safe to commit):
  - `web-app/src/lib/config.js`
  - `web-app/src/lib/LicenseFooter.svelte`

### ❌ Gitignored (Personal)
- `.config.edn` - Your customizations
- `.secrets.edn` - Your credentials

---

## Benefits

### Fork-Friendly
- No hardcoded personal details in code
- Copy templates and customize
- Generate files automatically
- Your config never committed

### Single Source of Truth
- All config in one EDN file
- JavaScript generated from EDN
- Svelte components generated from EDN
- No duplication, no drift

### Secure by Default
- Secrets never committed (.gitignore)
- Sensitive data separated from code
- API keys isolated
- Safe to share repo

### Easy Customization
- Change author name
- Update URLs for your deployment
- Choose different license
- Customize footer
- Adjust theme colors
- All in one file!

---

## Philosophy

**Why EDN?**
- Clojure-native (fits our stack)
- Human-readable (easy to edit)
- Powerful (data as code)
- Simple (no XML/JSON verbosity)

**Why Templates?**
- Fork-friendly (no personal details)
- Educational (shows what's configurable)
- Safe (gitignored personal files)
- Sovereign (complete control)

**Why Generated?**
- DRY (single source of truth)
- Type-safe (EDN → validated JSON)
- Maintainable (change once, update everywhere)
- Automated (bb script)

---

## Troubleshooting

### "Config file not found"
```bash
cp .config.template.edn .config.edn
bb scripts/generate-site-config.bb
```

### "Permission denied"
```bash
chmod +x scripts/generate-site-config.bb
```

### "Generated files out of sync"
```bash
bb scripts/generate-site-config.bb
```

### "Want to reset to defaults"
```bash
cp .config.template.edn .config.edn
bb scripts/generate-site-config.bb
```

---

## Future Enhancements

- [ ] Load config in build pipeline (auto-regenerate)
- [ ] Validate config schema (Clojure Spec)
- [ ] Generate more components from config
- [ ] Theme switching based on config
- [ ] Multi-language support

---

**The configuration system is sovereign, secure, and fork-friendly.** 🔷✨

**See**: `.config.template.edn` for all available options!

