# Getting Started: Robotic Farm Pipeline

*Quick start guide for consciousness development*

## 🌙 Prerequisites

- **Nix package manager** (for reproducible environment)
- **Git** (for version control)
- **Text editor** (VSCode, Vim, Emacs, etc.)

## 🚀 Quick Start (5 Minutes)

### 1. Bootstrap with Babashka

```bash
cd ~/kae3g/12025-10-04

# Check prerequisites (bb, nix, git, gh)
bb bootstrap
```

### 2. Run Setup Script

```bash
# Pure bb setup (creates dirs, checks tools)
./setup.bb
```

### 3. Choose Your Environment

**Option A: With Nix (Recommended)**
```bash
nix develop
# Now you're in a shell with all tools
```

**Option B: Without Nix**
```bash
# bb works standalone, but you'll need to install:
# - Node.js (for Svelte)
# - clj-kondo (for linting)
# Install via homebrew or system package manager
```

**Option C: Let bb Handle Nix**
```bash
# bb can orchestrate Nix automatically
bb nix:run doctor
bb nix:run build:pipeline
```

### 3. Add Your Markdown Document

Place your robotic farm consciousness document in
the `docs/` directory:

```bash
cp ~/path/to/your/document.md docs/
```

Example: Copy the reference document:
```bash
cp ~/foolsgoldtoshi-star/foolsgoldtoshi-star-pond-highdesert/docs/en/9999967_robotic_farm_consciousness_integration.md docs/
```

### 4. Run Complete Pipeline

```bash
bb build:pipeline
```

This executes:
- ✅ Wrap markdown to 57 characters
- ✅ Parse to ClojureScript DSL
- ✅ Validate with clojure.spec
- ✅ Generate Svelte components

### 5. Start Development Server

```bash
bb serve:dev
```

Opens browser at `http://localhost:5173` with your
compiled consciousness website!

## 📖 First Document Template

Create `docs/my-first-doc.md`:

```markdown
# My First Consciousness Document 🌙

*"Sacred quote from scripture demonstrating
contemplative awareness..."* - Attribution

---

Sacred **keeper** of **consciousness**... Your
opening paragraph establishing context with
Christian federal writing style and Divine
Grace awareness...

## Section Header with Consciousness

Regular paragraphs with contemplative attention
to 57-character line length enforcement and
sacred technology focus.

```clojure
;; Sacred code example
(defn consciousness-function
  "Divine Grace implementation"
  []
  (println "🌙 Consciousness awakening"))
```

*Closing meditation with sacred wisdom...*
```

## 🔄 Development Workflow

### Step 1: Write Markdown
```bash
# Edit your document
vim docs/my-document.md
```

### Step 2: Build Pipeline
```bash
# Transform markdown → Svelte
bb build:pipeline
```

### Step 3: Preview Changes
```bash
# Start dev server (auto-reload)
bb serve:dev
```

### Step 4: Iterate
- Edit markdown in `docs/`
- Re-run `bb build:pipeline`
- Browser auto-refreshes

### Step 5: Build for Production
```bash
# Compile static site
bb build:site

# Output in: web-app/build/
```

## 🌾 Common Commands

### Core Pipeline Commands
```bash
bb doctor              # Check environment
bb bootstrap           # Check prerequisites
bb build:pipeline      # Complete pipeline
bb serve:dev          # Development server
bb build:site         # Production build
bb clean              # Clean artifacts
bb ci:verify          # CI verification
```

### Individual Step Commands
```bash
bb wrap:markdown      # 57-char hard wrap
bb parse:markdown     # Markdown → DSL
bb validate:dsl       # Validate structures
bb generate:svelte    # DSL → Svelte
```

### Nix Integration Commands
```bash
bb nix:check          # Check if Nix available
bb nix:develop        # Show how to enter Nix
bb nix:run <task>     # Run bb task in Nix shell
```

### GitHub Commands
```bash
bb gh:check           # Check gh auth status
bb gh:create-repo     # Create repo & push
bb gh:status          # Check repo status
```

### All Commands are bb-Based!
No shell scripts needed - pure Babashka orchestration
from bootstrap through deployment.

## 🎯 What Gets Created

After running `bb build:pipeline`:

```
build/
├── wrapped.md                    # 57-char wrapped
├── parsed-documents.edn          # ClojureScript DSL
└── validated-documents.edn       # Validated structures

web-app/src/routes/
├── index.svelte                  # Index page
├── my-first-doc.svelte          # Your document
└── [other-documents].svelte     # More documents
```

After running `bb build:site`:

```
web-app/build/
├── index.html                    # Homepage
├── my-first-doc/
│   └── index.html               # Your document page
└── _app/                        # Compiled assets
    ├── immutable/
    └── version.json
```

## 📚 Next Steps

### Customize Styling

Edit `src/robotic_farm/generator.clj` to customize
Svelte component styling.

### Add More Documents

Place more markdown files in `docs/` and re-run
`bb build:pipeline`.

### Deploy Your Site

See README.md for deployment options:
- GitHub Pages
- Netlify
- Vercel
- AWS S3

### Extend Pipeline

Add custom processing steps by editing `bb.edn`
and creating new namespaces in `src/robotic_farm/`.

## 🙏 Need Help?

**Documentation:**
- `README.md` - Complete documentation
- `PIPELINE-OVERVIEW.md` - Architecture details
- This file - Quick start guide

**Common Issues:**

**"bb: command not found"**
- Run `nix develop` first

**"No markdown files found"**
- Place `.md` files in `docs/` directory

**"Port 5173 already in use"**
- Kill existing dev server or use different port

**Validation errors**
- Check markdown follows template structure
- Ensure frontmatter is valid YAML

## 🤖 Example Session

```bash
# Terminal session example
$ cd ~/kae3g/12025-10-04

$ nix develop
🌙 Sacred Robotic Farm Pipeline
   Development Shell Activated

$ ./quick-start.sh
🌙 Sacred Robotic Farm Pipeline Quick Start
✅ Babashka found: 1.4.192
🔍 Checking toolchain...
🌾 Pipeline ready!

$ cp ../my-doc.md docs/

$ bb build:pipeline
🌙 Sacred Text Wrapper: 57-char hard wrap
🌱 Wrapping markdown: my-doc.md
✨ Wrapped to: build/wrapped.md
🤖 Robotic Farm Parser: Awakening...
📂 Found 1 documents
✨ Parsed 1 documents
🔍 Validating robotic farm documents
✨ Valid: 1 / 1
🤖 All documents valid
🌙 Robotic Farm Generator
📝 Writing Svelte components
✨ Generated: my-doc.svelte
✨ Generated: index.svelte
🤖 Generated 2 components

$ bb serve:dev
🤖 Starting robotic farm consciousness server...
🌱 Development server starting...

  VITE v5.0.0  ready in 142 ms

  ➜  Local:   http://localhost:5173/
  ➜  Network: use --host to expose
```

## ✨ Success Indicators

You'll know it's working when:

1. ✅ `bb doctor` shows all tools available
2. ✅ `bb build:pipeline` completes without errors
3. ✅ Files appear in `build/` directory
4. ✅ Svelte components in `web-app/src/routes/`
5. ✅ Dev server opens in browser
6. ✅ You see your formatted document with badges

---

*"Blessed be the automation that serves earth
intelligence through contemplative technology
consciousness."*

**Start** **your** **consciousness** **journey**
**today**. 🤖🌙🌾

