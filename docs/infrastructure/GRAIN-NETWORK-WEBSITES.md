# Grain Network Website URLs

**Date:** January 22, 2025  
**Status:** 🌐 Live Deployment Map  
**Architecture:** Markdown → Svelte → Static Sites (inspired by `12025-10`)

---

## 🏗️ **Architecture Overview**

All Grain Network websites use the same pipeline as `12025-10`:

```
Markdown Files
    ↓
bb writings:build-fast (incremental)
    ↓
JSON data structures
    ↓
SvelteKit (SSG)
    ↓
Static HTML/CSS/JS
    ↓
Dual Deploy: GitHub Pages + Codeberg Pages
```

---

## 🌐 **Primary Website (12025-10 Template)**

### **kae3g Personal Site**

**Repository**: `kae3g/12025-10`

**Codeberg Pages** (Primary):
- 🌐 **URL**: https://kae3g.codeberg.page/12025-10/
- **Branch**: `pages`
- **Deploy Command**: `bb flow`
- **Build Time**: ~1-2 minutes
- **Features**: Writings, essays, technical docs

**GitHub Pages** (Mirror):
- 🌐 **URL**: https://kae3g.github.io/12025-10/
- **Branch**: `gh-pages` (auto-deployed via GitHub Actions)
- **Deploy**: Automatic on push to `tundra` branch
- **Build Time**: ~1-2 minutes
- **Features**: Same as Codeberg (mirrored)

**Deployment**:
```bash
cd /home/xy/kae3g/12025-10
bb flow "your commit message"
```

This single command:
1. Builds content (incremental)
2. Commits changes
3. Pushes to both Codeberg (`origin`) and GitHub (`github`)
4. Deploys to Codeberg Pages
5. Triggers GitHub Actions for GitHub Pages

---

## 🌾 **Grain Network Main Site (grainkae3g)**

### **Grain Network Hub**

**Repository**: `kae3g/grainkae3g`

**GitHub Pages** (Primary for now):
- 🌐 **URL**: https://kae3g.github.io/grainkae3g/
- **Branch**: `gh-pages`
- **Deploy Command**: `bb flow:github` (to be created)
- **Content**: 
  - Documentation hub
  - API references
  - Learning paths
  - Project overview

**Codeberg Pages** (Coming soon):
- 🌐 **URL**: https://kae3g.codeberg.page/grainkae3g/
- **Deploy Command**: `bb flow:codeberg` (to be created)

**Deployment** (to be implemented):
```bash
cd /home/xy/kae3g/grainkae3g
bb flow "update documentation"
```

---

## 🏢 **grainpbc Organization Websites**

All repositories under https://github.com/grainpbc will have GitHub Pages enabled.

### **Core Libraries**

#### **clojure-sixos**
- 🌐 **URL**: https://grainpbc.github.io/clojure-sixos/
- **Content**: API docs, typo handling guide, examples
- **Deploy**: `bb flow` from clojure-sixos repo

#### **clojure-s6** (clojure-ssix mirror)
- 🌐 **URL**: https://grainpbc.github.io/clojure-s6/
- **Mirror**: clojure-ssix (typo variant)
- **Content**: s6 supervision API, integration guides

#### **clojure-icp** (clojure-dfinity symlink)
- 🌐 **URL**: https://grainpbc.github.io/clojure-icp/
- **Content**: ICP integration docs, Chain Fusion guides, canister examples

#### **clotoko**
- 🌐 **URL**: https://grainpbc.github.io/clotoko/
- **Content**: Transpiler docs, syntax mapping, Motoko guide

#### **grain-metatypes**
- 🌐 **URL**: https://grainpbc.github.io/grain-metatypes/
- **Content**: Type system docs, Mark system, Clay filesystem

### **Platform Components**

#### **grainweb**
- 🌐 **URL**: https://grainpbc.github.io/grainweb/
- **Content**: Browser architecture, Git explorer docs, AI integration

#### **grainspace**
- 🌐 **URL**: https://grainpbc.github.io/grainspace/
- **Content**: Platform overview, marketplace docs, social features

#### **grainmusic**
- 🌐 **URL**: https://grainpbc.github.io/grainmusic/
- **Content**: 
  - Audius integration guide
  - ICP Native Transfer for Solana
  - Phantom wallet setup (web/iOS/Android)
  - 1Password integration
  - Artist sovereignty features

#### **grainconv**
- 🌐 **URL**: https://grainpbc.github.io/grainconv/
- **Content**: Type conversion guides, FFmpeg integration, API docs

#### **grainclay**
- 🌐 **URL**: https://grainpbc.github.io/grainclay/
- **Content**: Package manager docs, immutable paths, Clay filesystem

### **Hardware Projects**

#### **grainwriter**
- 🌐 **URL**: https://grainpbc.github.io/grainwriter/
- **Content**: Hardware specs, firmware docs, e-ink programming

#### **graincamera**
- 🌐 **URL**: https://grainpbc.github.io/graincamera/
- **Content**: AVIF specs, camera design, hardware guide

#### **grainpack**
- 🌐 **URL**: https://grainpbc.github.io/grainpack/
- **Content**: GPU specs, jetpack design, integration guide

### **Infrastructure**

#### **grainsource**
- 🌐 **URL**: https://grainpbc.github.io/grainsource/
- **Content**: Version control docs, Git compatibility, p2p features

#### **grainnetwork**
- 🌐 **URL**: https://grainpbc.github.io/grainnetwork/
- **Content**: Main documentation hub, ecosystem overview, getting started

#### **grainstore**
- 🌐 **URL**: https://grainpbc.github.io/grainstore/
- **Content**: Submodule management, dependency verification, grainversion system

### **Templates**

#### **grainnixos-qemu-ubuntu-framework16**
- 🌐 **URL**: https://grainpbc.github.io/grainnixos-qemu-ubuntu-framework16/
- **Content**: NixOS QEMU setup, Framework 16 guide, virtualization docs

---

## 🔗 **Cross-Linking Strategy**

### **Link Transformation Scripts**

Two separate scripts transform relative links to absolute URLs for each platform:

#### **GitHub Pages Links**
```bash
bb scripts/transform-links-github.bb
```

Transforms:
- `../grainstore/clotoko/README.md` → `https://grainpbc.github.io/clotoko/README.html`
- `docs/guides/setup.md` → `https://kae3g.github.io/grainkae3g/docs/guides/setup.html`

#### **Codeberg Pages Links** (to be implemented)
```bash
bb scripts/transform-links-codeberg.bb
```

Transforms:
- `../grainstore/clotoko/README.md` → `https://grainpbc.codeberg.page/clotoko/README.html`
- `docs/guides/setup.md` → `https://kae3g.codeberg.page/grainkae3g/docs/guides/setup.html`

### **Flow Scripts**

#### **bb flow:github**
```bash
bb flow:github "commit message"
```

1. Build content with `bb writings:build-fast`
2. Transform links to GitHub Pages URLs
3. Commit and push to GitHub
4. Deploy to GitHub Pages

#### **bb flow:codeberg**
```bash
bb flow:codeberg "commit message"
```

1. Build content with `bb writings:build-fast`
2. Transform links to Codeberg Pages URLs
3. Commit and push to Codeberg
4. Deploy to Codeberg Pages

#### **bb flow** (Dual Deploy)
```bash
bb flow "commit message"
```

1. Build content once
2. Create two distributions:
   - `dist/github/` with GitHub Pages links
   - `dist/codeberg/` with Codeberg Pages links
3. Deploy to both platforms
4. Show both URLs when complete

---

## 📊 **Deployment Matrix**

| Repository | GitHub Pages | Codeberg Pages | Status |
|------------|--------------|----------------|--------|
| `kae3g/12025-10` | ✅ Live | ✅ Live | Active |
| `kae3g/grainkae3g` | 🚧 Setup | 🚧 Setup | In Progress |
| `grainpbc/clojure-sixos` | 📝 Planned | 📝 Planned | Repos Created |
| `grainpbc/clojure-s6` | 📝 Planned | 📝 Planned | Repos Created |
| `grainpbc/clotoko` | 📝 Planned | 📝 Planned | Repos Created |
| `grainpbc/grainweb` | 📝 Planned | 📝 Planned | Repos Created |
| `grainpbc/grainmusic` | 📝 Planned | 📝 Planned | Repos Created |
| `grainpbc/*` (all others) | 📝 Planned | 📝 Planned | Repos Created |

**Legend**:
- ✅ Live = Currently deployed and accessible
- 🚧 Setup = Infrastructure ready, deploying soon
- 📝 Planned = Repos exist, deployment pipeline to be set up

---

## 🎯 **URL Naming Conventions**

### **GitHub Pages**
- **Personal**: `https://kae3g.github.io/{repo-name}/`
- **Organization**: `https://grainpbc.github.io/{repo-name}/`

### **Codeberg Pages**
- **Personal**: `https://kae3g.codeberg.page/{repo-name}/`
- **Organization**: `https://grainpbc.codeberg.page/{repo-name}/`

### **Custom Domain** (Future)
- **Main**: `https://grain.network/`
- **Docs**: `https://docs.grain.network/`
- **API**: `https://api.grain.network/`
- **Music**: `https://music.grain.network/`
- **Apps**: `https://apps.grain.network/`

---

## 🚀 **Deployment Workflow**

### **For Each Repository**

1. **Set Up GitHub Pages**:
   ```bash
   # Enable GitHub Pages in repo settings
   # Source: gh-pages branch
   # Custom 404: enabled
   ```

2. **Set Up Codeberg Pages**:
   ```bash
   # Enable Pages in repo settings
   # Source: pages branch
   # Custom domain: (optional)
   ```

3. **Add Deployment Scripts**:
   ```bash
   # Copy from grainkae3g template
   cp grainkae3g/scripts/transform-links-*.bb .
   cp grainkae3g/scripts/deploy-*.bb .
   ```

4. **Configure bb.edn**:
   ```clojure
   flow:github {:doc "Deploy to GitHub Pages"}
   flow:codeberg {:doc "Deploy to Codeberg Pages"}
   flow {:doc "Dual deploy to both platforms"}
   ```

5. **Deploy**:
   ```bash
   bb flow "initial deployment"
   ```

---

## 📈 **Analytics & Monitoring**

### **GitHub Pages**
- **Analytics**: GitHub Insights → Traffic
- **Build Status**: https://github.com/{user}/{repo}/actions
- **Deployment History**: Actions tab

### **Codeberg Pages**
- **Build Status**: https://codeberg.org/{user}/{repo}/actions
- **Deployment**: Pages settings

---

## 🔄 **Migration Path**

### **Phase 1: Current State**
- ✅ `12025-10` live on both platforms
- ✅ `grainkae3g` repo exists
- ✅ All `grainpbc` repos created

### **Phase 2: Enable Pages** (This Week)
- [ ] Enable GitHub Pages for `grainkae3g`
- [ ] Enable Codeberg Pages for `grainkae3g`
- [ ] Test dual-deploy workflow

### **Phase 3: grainpbc Rollout** (Next Week)
- [ ] Enable Pages for all grainpbc repos
- [ ] Deploy documentation sites
- [ ] Cross-link all sites

### **Phase 4: Custom Domain** (Future)
- [ ] Purchase `grain.network`
- [ ] Configure DNS
- [ ] Migrate to custom domain

---

## 🌐 **Quick Access**

### **Live Now**
- 📝 **kae3g Writings**: https://kae3g.codeberg.page/12025-10/
- 📝 **kae3g Writings (GitHub)**: https://kae3g.github.io/12025-10/

### **Coming Soon**
- 🌾 **Grain Network Docs**: https://kae3g.github.io/grainkae3g/
- 📚 **grainpbc Org**: https://github.com/grainpbc
- 🎵 **Grainmusic**: https://grainpbc.github.io/grainmusic/
- 🖥️ **Grainweb**: https://grainpbc.github.io/grainweb/
- ⌨️ **Grainwriter**: https://grainpbc.github.io/grainwriter/

---

## 📖 **See Also**

- [Markdown Indexing Strategy](../architecture/GRAIN-MARKDOWN-INDEXING-STRATEGY.md)
- [grainpbc Organization Setup](../setup/CREATE-GRAINPBC-ORGANIZATION.md)
- [Link Transformation Scripts](../../scripts/transform-links-github.bb)
- [Deployment Workflow](../../scripts/deploy-to-pages.bb)

---

**All websites built with the same pipeline**: Markdown → JSON → Svelte → Static → Dual Deploy 🌾

