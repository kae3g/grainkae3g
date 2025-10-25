# 🌾 Grainclay - Unified Content Workflow

**Complete content creation, publishing, and distribution system**

> *"From clay to grain, shape and refine."*

Grainclay provides a unified workflow for content creation, publishing, and distribution across the Grain Network ecosystem. It follows the `gb flow` pattern with a single command that handles the complete workflow.

---

## 🎯 Philosophy

**"Unified Workflow, Multiple Destinations"**

Grainclay embodies the Grain Network philosophy of:
- **Single Command**: One `grainclay flow` command handles everything
- **Template/Personal Split**: Defaults from template, personal overrides
- **Immutable Paths**: Time-based, URL-safe content addressing
- **Multi-Platform**: Publish to GitHub Pages, Codeberg Pages, and more
- **Automated Sync**: Git operations handled automatically

Inspired by:
- **gb flow**: Grainbarrel's unified workflow pattern
- **Urbit Clay**: Immutable file system with versioning
- **Nix**: Declarative configuration and reproducible builds

---

## 🚀 Quick Start

### 1. Basic Usage

```bash
# Complete workflow - create, publish, distribute, sync
cd /home/xy/kae3g/grainkae3g/grainstore/grain6pbc/grainclay
bb flow

# Or from anywhere (once integrated with gb)
gb grainclay flow
```

### 2. Individual Steps

```bash
# Create content only
bb content:create

# Publish to grainclay network
bb flow:publish

# Distribute across platforms
bb flow:distribute

# Sync with git repositories
bb flow:sync
```

### 3. Configuration

```bash
# Show current configuration
bb config:show

# Update configuration interactively
bb config:update
```

---

## 📋 Features

### Core Workflow (`grainclay flow`)

1. **Content Creation**
   - Interactive prompts for content type, name, description
   - Automatic grainclay path generation with graintime
   - Template-based content structure creation

2. **Publishing**
   - Publish to grainclay network
   - Generate `.beam.edn` metadata files
   - Content validation and verification

3. **Distribution**
   - GitHub Pages deployment
   - Codeberg Pages deployment
   - Multi-format output (HTML, Markdown, EDN, JSON)

4. **Synchronization**
   - Git add, commit, push operations
   - Multi-remote support (GitHub, Codeberg)
   - Automated deployment triggers

### Content Management

- **Content Types**: article, course, tool, library, documentation
- **Immutable Paths**: `/content-type/name/graintime/`
- **Metadata**: `.beam.edn` files with complete content information
- **Templates**: HTML and Markdown generation with consistent styling

### Network Operations

- **Multi-Platform**: GitHub Pages, Codeberg Pages, custom hosts
- **Auto-Sync**: Configurable sync intervals
- **Status Monitoring**: Network health and sync status
- **Cleanup**: Automated old content cleanup

---

## 🔧 Configuration

### Personal Configuration (`~/.grainconfig.edn`)

```edn
{:grainclay
 {:network-hosts ["github.com" "codeberg.org" "grain6pbc.github.io"]
  :publish-formats [:html :markdown :edn :json]
  :sync-interval-minutes 30
  :auto-cleanup true
  :cleanup-retention-days 30
  :github-owner "kae3g"
  :github-repo "grainkae3g"
  :codeberg-owner "kae3g"
  :codeberg-repo "grainkae3g"
  :content-types ["article" "course" "tool" "library" "documentation"]
  :default-content-type "article"
  :auto-sync true
  :publish-on-create true}}
```

### Environment Variables

Grainclay integrates with `grainenvvars` for secure API key management:

```bash
# Set up environment variables
cd ~/kae3g/grainkae3g/grainstore/grainenvvars
cp template/env.template personal/.env

# Add to personal/.env:
GITHUB_TOKEN=your_github_token_here
CODEBERG_TOKEN=your_codeberg_token_here
```

---

## 📁 Content Structure

### Generated Directory Structure

```
/graintime/content-type/content-name/
├── index.html          # Main content page
├── README.md           # Markdown documentation
└── .beam.edn          # Grainclay metadata
```

### Example Content

**index.html**:
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Article</title>
    <style>
        body { font-family: 'Courier New', monospace; margin: 40px; background: #1a1a1a; color: #e0e0e0; }
        .container { max-width: 800px; margin: 0 auto; }
        h1 { color: #4CAF50; border-bottom: 2px solid #4CAF50; padding-bottom: 10px; }
        .grainclay-path { font-family: monospace; background: #333; padding: 10px; border-radius: 5px; margin: 20px 0; }
    </style>
</head>
<body>
    <div class="container">
        <h1>My Article</h1>
        <div class="grainclay-path">/article/my-article/12025-10-23--0224--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/</div>
        <div class="description">A sample article created with grainclay flow</div>
        <div class="links">
            <a href="https://kae3g.github.io/grainkae3g/article/my-article/12025-10-23--0224--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/">GitHub Pages</a>
            <a href="https://kae3g.codeberg.page/grainkae3g/article/my-article/12025-10-23--0224--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/">Codeberg Pages</a>
        </div>
    </div>
</body>
</html>
```

**.beam.edn**:
```edn
{:grainclay-version "1.0.0"
 :content-type "article"
 :content-name "My Article"
 :graintime "12025-10-23--0224--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g"
 :created-at #inst "2025-10-23T09:24:00.000Z"
 :description "A sample article created with grainclay flow"
 :formats [:html :markdown :edn :json]
 :network-hosts ["github.com" "codeberg.org" "grain6pbc.github.io"]}
```

---

## 🔄 Integration with Grain Ecosystem

### Grainbarrel Integration

```bash
# Add grainclay tasks to grainbarrel
gb grainclay:flow
gb grainclay:content:create
gb grainclay:config:show
```

### Grainenvvars Integration

```bash
# Set up API tokens
bb grainenvvars:setup-github-token
bb grainenvvars:setup-codeberg-token
```

### Graintime Integration

- Automatic graintime generation for content paths
- Astronomical timestamping for immutable addressing
- Time-based content organization

---

## 📊 Commands Reference

### Core Flow Commands

```bash
bb flow                    # Complete workflow
bb flow:publish           # Publish only
bb flow:distribute        # Distribute only
bb flow:sync              # Sync only
```

### Content Management

```bash
bb content:create         # Create new content
bb content:watch          # Watch for changes
bb content:list           # List all content
```

### Network Operations

```bash
bb network:status         # Check network status
bb network:sync           # Sync with network
```

### Configuration

```bash
bb config:show            # Show configuration
bb config:update          # Update configuration
```

### Utilities

```bash
bb cleanup                # Clean up old content
bb validate               # Validate content structure
```

---

## 🎨 Content Types

### Supported Types

- **article**: Blog posts, essays, documentation
- **course**: Educational content, tutorials
- **tool**: Software tools, utilities
- **library**: Code libraries, frameworks
- **documentation**: API docs, guides

### Custom Types

Add custom content types in configuration:

```edn
{:content-types ["article" "course" "tool" "library" "documentation" "press-release" "design-doc"]}
```

---

## 🔒 Security

### API Key Management

- Uses `grainenvvars` for secure token storage
- No hardcoded secrets in code
- Template/personal split for configuration

### Content Validation

- Automatic content structure validation
- Metadata verification
- Path sanitization and security

---

## 🚀 Examples

### Example 1: Create an Article

```bash
$ bb flow

🌾 Grainclay Flow - Complete Content Workflow
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Content Type (article/course/tool/other): article
Content Name: My First Article
Description: A sample article about the Grain Network

🛤️  Generated grainclay path:
   /article/my-first-article/12025-10-23--0224--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/

Create this content? (y/N): y

🏗️  Creating content structure...
   ✅ Content directory created

📦 Publishing content to grainclay network...
   ✅ Content published

🌐 Distributing content across platforms...
   📤 GitHub Pages: https://kae3g.github.io/grainkae3g/article/my-first-article/12025-10-23--0224--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/
   📤 Codeberg Pages: https://kae3g.codeberg.page/grainkae3g/article/my-first-article/12025-10-23--0224--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/
   ✅ Content distributed

🔄 Syncing content with remote repositories...
   ✅ Added to git
   ✅ Committed to git
   ✅ Pushed to origin
   ✅ Pushed to codeberg
   ✅ Content synced

🎉 Grainclay flow complete!

🔗 Access your content:
   GitHub Pages: https://kae3g.github.io/grainkae3g/article/my-first-article/12025-10-23--0224--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/
   Codeberg Pages: https://kae3g.codeberg.page/grainkae3g/article/my-first-article/12025-10-23--0224--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/

📁 Local path: /home/xy/kae3g/grainkae3g/article/my-first-article/12025-10-23--0224--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/
```

### Example 2: Configuration Update

```bash
$ bb config:update

🔧 Update Grainclay Configuration
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

GitHub Owner [kae3g]: 
GitHub Repo [grainkae3g]: 
Codeberg Owner [kae3g]: 
Codeberg Repo [grainkae3g]: 
Sync Interval (minutes) [30]: 15

✅ Configuration updated!
   File: /home/xy/.grainconfig.edn
```

---

## 🏗️ Architecture

### Directory Structure

```
grain6pbc/grainclay/
├── src/
│   └── grainclay/
│       ├── config.clj           # Configuration management
│       ├── flow.clj             # Main workflow logic
│       ├── publish.clj          # Publishing operations
│       ├── distribute.clj       # Distribution logic
│       └── sync.clj             # Synchronization
├── scripts/
│   ├── grainclay-flow.bb        # Main flow script
│   ├── grainclay-publish.bb     # Publish script
│   ├── grainclay-distribute.bb  # Distribute script
│   ├── grainclay-sync.bb        # Sync script
│   └── grainclay-config-update.bb
├── template/
│   └── config.edn.template      # Default configuration
├── personal/
│   └── config.edn               # Personal overrides (gitignored)
├── bb.edn                       # Babashka tasks
└── README.md
```

### Dependencies

- `babashka.fs`: File system operations
- `clj-http`: HTTP client for API calls
- `clj-time`: Time handling
- `java-time`: Java time API
- `graintime`: Grain Network time system

---

## 🔮 Roadmap

### Phase 1: Core Workflow ✅
- [x] Basic flow command
- [x] Content creation
- [x] Publishing system
- [x] Distribution to GitHub/Codeberg
- [x] Git synchronization

### Phase 2: Advanced Features 🔄
- [ ] Content watching and auto-publish
- [ ] Multi-format output generation
- [ ] Content validation and linting
- [ ] Network status monitoring
- [ ] Automated cleanup

### Phase 3: Integration 🌱
- [ ] Grainbarrel integration
- [ ] Grainenvvars integration
- [ ] Grainweb integration
- [ ] Custom content types
- [ ] Plugin system

---

## 🌾 Philosophy: The Clay-Grain Flow

```
Content → Clay → Shape → Grain → Publish → Distribute → Sync
   ↑                                                      ↓
   └─────────────── Grainclay Flow ───────────────────────┘
```

**Content**: Raw ideas, notes, drafts  
**Clay**: Malleable, workable content structure  
**Grain**: Refined, immutable, published content  
**Flow**: The complete journey from idea to distribution

Just as clay is shaped into useful forms and fired into permanent ceramic, content flows through grainclay from raw ideas to published, distributed, and synchronized across the network.

---

**Template/Personal Split**: ✅  
**grainenvvars Integration**: 🔄 In Progress  
**gb flow Pattern**: ✅  
**Status**: 🌱 Seedling

**Session**: `12025-10-23--0224--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g`
