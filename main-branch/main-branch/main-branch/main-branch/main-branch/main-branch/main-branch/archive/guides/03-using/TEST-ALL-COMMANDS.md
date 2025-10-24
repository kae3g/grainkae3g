# Testing All bb Commands

Comprehensive test suite for all pipeline commands

## Prerequisites Test

Before running bb commands, ensure babashka is installed:

```bash
# Check if bb is available
which bb
bb --version

# If not found, install:
# macOS: brew install babashka
# Nix: nix-env -iA nixpkgs.babashka
# Or enter nix shell: nix develop
```

## Test Commands in Order

### 1. Bootstrap & Setup Commands

```bash
# Test bootstrap (checks prerequisites)
bb bootstrap

# Expected output:
# Documentation Pipeline Toolchain Status
# Checking prerequisites...
# ✅ Babashka
# ✅ Nix
# ✅ Git
# ⚠️  GitHub CLI (optional)
# Bootstrap complete
```

```bash
# Test setup script
./setup.bb

# Expected output:
# Documentation Pipeline Setup
# Checking Prerequisites:
# ✅ Babashka
# ✅ Nix
# ✅ Git
# ✅ GitHub CLI
# Setting up directories...
# Directories ready
```

```bash
# Alternative: Run setup via bb
bb -f setup.bb
```

### 2. Health Check Commands

```bash
# Test doctor (verify toolchain)
bb doctor

# Expected output:
# Documentation Pipeline Toolchain Status
# bb version: 1.x.x
# node --version
# v20.x.x
# clj-kondo --version
# zprint --version
# npx vite --version
```

```bash
# List all available tasks
bb tasks

# Expected output: List of all tasks with descriptions
```

### 3. Nix Integration Commands

```bash
# Check Nix availability
bb nix:check

# Expected output:
# nix (Nix) 2.x.x
# OR: Nix not found - install from nixos.org
```

```bash
# Show Nix develop instructions
bb nix:develop

# Expected output:
# To enter Nix shell, run in your terminal:
#    nix develop
# 
# Then inside Nix shell:
#    bb doctor
#    bb build:pipeline
```

```bash
# Run task in Nix context (if Nix available)
bb nix:run doctor

# Equivalent to: nix develop --command bb doctor
```

### 4. Pipeline Core Commands

```bash
# Test text wrapper (requires docs/*.md files)
bb wrap:markdown

# Expected output:
# Text Wrapper: 57-char hard wrap
# Wrapping markdown: [filename]
# Wrapped to: build/wrapped.md
# Text wrapping complete
```

```bash
# Test markdown parser
bb parse:markdown

# Expected output:
# Markdown Parser: Starting...
# Transforming markdown → DSL
# Discovering documents
# Found X documents
# Parsed X documents
# Saved to: build/parsed-documents.edn
```

```bash
# Test validator
bb validate:dsl

# Expected output:
# Validator: Starting spec validation
# Validating documents
# Valid: X / X
# All documents valid
# Saved validated docs
```

```bash
# Test Svelte generator
bb generate:svelte

# Expected output:
# Generator: Transforming DSL → Svelte
# Writing Svelte components
# Generated: [filename].svelte
# Generated: index.svelte
# Generated X components
```

```bash
# Test complete pipeline
bb build:pipeline

# Runs: wrap:markdown → parse:markdown → validate:dsl → generate:svelte
# Expected: All above outputs in sequence
```

### 5. Development Server Commands

```bash
# Test development server (requires Node.js)
# WARNING: This runs indefinitely until Ctrl+C
bb serve:dev

# Expected output:
# Starting development server...
# Installing packages (vite, @sveltejs/kit)...
# VITE v5.0.0  ready in XXX ms
# ➜  Local:   http://localhost:5173/
# ➜  Network: use --host to expose

# Press Ctrl+C to stop
```

### 6. Build Commands

```bash
# Test static site build (requires Node.js)
bb build:site

# Expected output:
# Building static site...
# vite v5.0.0 building for production...
# ✓ built in XXXXms
```

```bash
# Test clean
bb clean

# Expected output:
# Cleaning build artifacts...
# Clean complete
```

### 7. Git/GitHub Commands

```bash
# Check GitHub CLI status
bb gh:check

# Expected output:
# gh auth status
# github.com
#   ✓ Logged in to github.com as USERNAME
# OR: Run: gh auth login
```

```bash
# Check git status
bb gh:status

# Expected output:
# Repository Status:
# On branch main
# [git status output]
# [git remote -v output]
```

```bash
# Create GitHub repository (ONE-TIME ACTION!)
# WARNING: This creates a real GitHub repo
bb gh:create-repo

# Expected output:
# Creating GitHub Repository
# Initialized git repository
# [main (root-commit) XXXXXX] Initial commit
# Creating GitHub repository...
# ✓ Created repository USER/REPONAME
# ✓ Added remote origin
# Enumerating objects: XX, done.
# Repository created and pushed!
```

### 8. CI/CD Commands

```bash
# Test CI verification
bb ci:verify

# Runs: doctor → wrap:markdown → parse:markdown → validate:dsl
# Expected: All checks pass
```

```bash
# Test formatting (requires zprint)
bb fmt:zprint

# Expected output:
# Formatting with zprint...
# [formatted files]
```

```bash
# Test linting (requires clj-kondo)
bb lint:kondo

# Expected output:
# linting took XXms
# [linting results]
```

## 🧪 Complete Test Sequence

Run all commands in order to verify full pipeline:

```bash
#!/bin/bash
# Complete test sequence

echo "=== 1. Bootstrap ==="
bb bootstrap

echo "\n=== 2. Setup ==="
./setup.bb

echo "\n=== 3. Doctor ==="
bb doctor

echo "\n=== 4. List Tasks ==="
bb tasks | head -20

echo "\n=== 5. Nix Check ==="
bb nix:check

echo "\n=== 6. Build Pipeline ==="
# Note: Requires markdown files in docs/
bb build:pipeline

echo "\n=== 7. CI Verify ==="
bb ci:verify

echo "\n=== 8. Git Status ==="
bb gh:status

echo "\n=== 9. List Generated Files ==="
ls -la build/
ls -la web-app/src/routes/

echo "\n✨ All tests complete!"
```

## 🔧 Troubleshooting

### "bb: command not found"

Solution:
```bash
# Install babashka
brew install babashka
# OR
nix-env -iA nixpkgs.babashka
# OR
nix develop  # Enter Nix shell
```

### "No markdown files found"

Solution:
```bash
# Copy example document
cp ~/foolsgoldtoshi-star/foolsgoldtoshi-star-pond-highdesert/docs/en/9999967_robotic_farm_consciousness_integration.md docs/

# Or create a test document
echo "# Test Doc 🤖" > docs/0000001_test.md
```

### "Node.js not found"

Solution:
```bash
# Enter Nix shell (provides Node.js)
nix develop
bb serve:dev

# OR install Node.js separately
brew install node
```

### "GitHub CLI not authenticated"

Solution:
```bash
gh auth login
# Follow the prompts
```

### "Task dependencies failed"

Solution:
```bash
# Run tasks individually to find issue
bb wrap:markdown  # Check this works
bb parse:markdown # Then this
bb validate:dsl   # Then this
bb generate:svelte # Then this
```

## 📊 Expected File Outputs

After successful `bb build:pipeline`:

```
build/
├── wrapped.md                    # 57-char wrapped markdown
├── parsed-documents.edn          # Parsed ClojureScript data
└── validated-documents.edn       # Validated structures

web-app/src/routes/
├── index.svelte                  # Index page component
└── [document-name].svelte        # Generated document components
```

After successful `bb build:site`:

```
web-app/build/
├── index.html                    # Homepage
├── [document-name]/
│   └── index.html               # Document pages
└── _app/                        # Compiled assets
    ├── immutable/
    │   ├── chunks/
    │   └── assets/
    └── version.json
```

## 🌙 Interactive Test REPL

You can also test bb interactively:

```bash
# Start bb REPL
bb

# Try commands:
(require '[babashka.process :refer [shell]])
(shell "ls" "-la")

# Or one-liners:
bb -e '(println "Hello from bb!")'
bb -e '(require (quote [babashka.fs :as fs])) (fs/exists? "bb.edn")'
```

## Success Criteria

All commands should:
- ✅ Execute without errors
- ✅ Produce expected output files
- ✅ Show progress indicators
- ✅ Complete successfully
- ✅ Exit with code 0

## Testing Best Practices

Run each command systematically, verifying outputs
and understanding the pipeline flow. Take time to
examine generated files and build artifacts.

Test incrementally: if a command fails, fix that
issue before proceeding to dependent commands.

---

**Test thoroughly to ensure quality.**

