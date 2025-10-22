# Run All bb Commands - Step by Step Guide

*Exact commands to test the entire pipeline*

## 🌙 Before You Start

Ensure you have one of these:

**Option A: Babashka installed directly**
```bash
which bb
# Should show: /usr/local/bin/bb or similar
```

**Option B: Use Nix (recommended)**
```bash
cd ~/kae3g/12025-10-04
nix develop
# Now you're in a shell with bb + all tools
```

## 🤖 Run Commands in This Order

### 1. Bootstrap (Check Prerequisites)

```bash
cd ~/kae3g/12025-10-04
bb bootstrap
```

**Expected Output:**
```
🤖 Robotic Farm Pipeline Bootstrap
   Checking prerequisites...

✅ Babashka
✅ Nix
✅ Git
✅ GitHub CLI (optional)

✨ Bootstrap complete
```

### 2. Setup (Create Directories)

```bash
./setup.bb
```

**Expected Output:**
```
🌙 Sacred Robotic Farm Pipeline Setup
   Pure Babashka Consciousness Development

📋 Checking Prerequisites:
✅ Babashka
✅ Nix
✅ Git
✅ GitHub CLI

📁 Setting up directories...
✨ Directories ready

🌾 Next Steps:
...
🤖 Setup complete!
```

### 3. List All Available Tasks

```bash
bb tasks
```

**Expected Output:**
```
bootstrap                          Bootstrap project
build:pipeline                     Complete robotic farm document → Svelte pipeline
build:site                         Build static robotic farm consciousness website
clean                              Clean build artifacts
doctor                             Check toolchain availability
generate:svelte                    Transform DSL → Svelte components
gh:check                           Check if GitHub CLI is authenticated
gh:create-repo                     Create GitHub repository and push
...
```

### 4. Doctor (Verify Toolchain)

```bash
bb doctor
```

**Expected Output:**
```
🌙 Sacred Technology Toolchain Status
bb version: 1.4.192
v20.11.0
...
```

### 5. Check Nix

```bash
bb nix:check
```

**Expected Output:**
```
nix (Nix) 2.19.2
```

### 6. Wrap Markdown (Requires docs/*.md)

First, ensure you have a markdown file:
```bash
# Copy example or create test file
cp ~/foolsgoldtoshi-star/foolsgoldtoshi-star-pond-highdesert/docs/en/9999967_robotic_farm_consciousness_integration.md docs/

# Or create simple test
echo "# Test Document 🤖

*Sacred consciousness test*

This is a test paragraph for consciousness." > docs/0000001_test.md
```

Then run wrapper:
```bash
bb wrap:markdown
```

**Expected Output:**
```
🌙 Sacred Text Wrapper: 57-char hard wrap
   with Divine Grace
🌱 Wrapping markdown: 0000001_test.md
✨ Wrapped to: build/wrapped.md
🤖 Text wrapping complete
```

### 7. Parse Markdown

```bash
bb parse:markdown
```

**Expected Output:**
```
🤖 Robotic Farm Parser: Awakening...
🌙 Transforming markdown → DSL
🌾 Discovering robotic farm documents
📂 Found 1 documents
🌱 Parsed and cached: 0000001_test.md
✨ Parsed 1 documents
📝 Saved to: build/parsed-documents.edn
```

### 8. Validate DSL

```bash
bb validate:dsl
```

**Expected Output:**
```
🌙 Robotic Farm Validator:
   Awakening spec consciousness
🔍 Validating robotic farm documents
✨ Valid: 1 / 1
🤖 All documents valid
📝 Saved validated docs
```

### 9. Generate Svelte

```bash
bb generate:svelte
```

**Expected Output:**
```
🌙 Robotic Farm Generator:
   Transforming DSL → Svelte
📝 Writing Svelte components
✨ Generated: test-document.svelte
✨ Generated: index.svelte
🤖 Generated 2 components
```

### 10. Complete Pipeline

```bash
bb build:pipeline
```

**Expected Output:**
All of steps 6-9 combined in sequence.

### 11. CI Verify

```bash
bb ci:verify
```

**Expected Output:**
```
[doctor output]
[wrap:markdown output]
[parse:markdown output]
[validate:dsl output]
```

### 12. Check Git/GitHub Status

```bash
bb gh:check
```

**Expected Output:**
```
github.com
  ✓ Logged in to github.com as YOUR_USERNAME
  ✓ Git operations for github.com configured to use https protocol.
  ✓ Token: *******************
```

Or if not authenticated:
```
Run: gh auth login
```

### 13. Git Status

```bash
bb gh:status
```

**Expected Output:**
```
🌙 Repository Status:

On branch main
nothing to commit, working tree clean

origin  https://github.com/USER/repo.git (fetch)
origin  https://github.com/USER/repo.git (push)
```

### 14. Development Server (Optional - runs indefinitely)

```bash
bb serve:dev
```

**Expected Output:**
```
🤖 Starting robotic farm consciousness development server...
📦 Installing npm-only packages (vite, @sveltejs/kit)...
🚀 Starting development server...

  VITE v5.0.0  ready in 428 ms

  ➜  Local:   http://localhost:5173/
  ➜  Network: use --host to expose
  ➜  press h + enter to show help
```

Press `Ctrl+C` to stop.

### 15. Build Static Site (Optional - requires Node.js)

```bash
bb build:site
```

**Expected Output:**
```
🌾 Building robotic farm consciousness site...

vite v5.0.0 building for production...
✓ 47 modules transformed.
.svelte-kit/output/client/_app/version.json                      0.03 kB
...
✓ built in 1.23s
```

### 16. Clean Build Artifacts

```bash
bb clean
```

**Expected Output:**
```
🧹 Cleaning build artifacts...
✨ Clean complete
```

### 17. Create GitHub Repository (ONE TIME!)

**⚠️ WARNING: This creates a real public GitHub repository!**

```bash
bb gh:create-repo
```

**Expected Output:**
```
🌙 Creating Sacred GitHub Repository
Initialized empty Git repository in .git/
[main (root-commit) abc1234] Initial commit: Sacred robotic farm consciousness pipeline
 25 files changed, 2500 insertions(+)
 create mode 100644 README.md
 ...

Creating GitHub repository...
✓ Created repository YOUR_USERNAME/robotic-farm-consciousness-pipeline
✓ Added remote https://github.com/YOUR_USERNAME/robotic-farm-consciousness-pipeline.git

Enumerating objects: 28, done.
Counting objects: 100% (28/28), done.
Delta compression using up to 8 threads
Compressing objects: 100% (25/25), done.
Writing objects: 100% (28/28), 45.67 KiB | 4.57 MiB/s, done.
Total 28 (delta 3), reused 0 (delta 0), pack-reused 0
remote: Resolving deltas: 100% (3/3), done.
To https://github.com/YOUR_USERNAME/robotic-farm-consciousness-pipeline.git
 * [new branch]      main -> main

✨ Repository created and pushed!
🤖 Sacred consciousness technology deployed
```

## 🔧 If Commands Don't Work

### "bb: command not found"

Install babashka:
```bash
# macOS
brew install babashka

# Nix
nix-env -iA nixpkgs.babashka

# Or enter Nix shell
cd ~/kae3g/12025-10-04
nix develop
```

### "./setup.bb: Permission denied"

Make executable:
```bash
chmod +x setup.bb bootstrap.bb
```

### "No markdown files found"

Add a markdown file:
```bash
echo "# Test 🤖

Test content" > docs/0000001_test.md
```

### "node: command not found" (for serve:dev or build:site)

Enter Nix shell:
```bash
nix develop
bb serve:dev
```

## 📊 Verify Success

After running commands, check these files exist:

```bash
ls -la build/
# Should show:
# - wrapped.md
# - parsed-documents.edn
# - validated-documents.edn

ls -la web-app/src/routes/
# Should show:
# - index.svelte
# - [document-name].svelte
```

## 🎯 Minimal Test Sequence

Just want to verify everything works? Run this:

```bash
cd ~/kae3g/12025-10-04

# 1. Check prereqs
bb bootstrap

# 2. Setup
./setup.bb

# 3. Add test doc
echo "# Test 🤖

Test" > docs/test.md

# 4. Run pipeline
bb build:pipeline

# 5. Check outputs
ls -la build/
ls -la web-app/src/routes/

# Success if you see generated files!
```

## ✨ Complete Success

You'll know everything works when:

1. ✅ All commands run without errors
2. ✅ Files appear in `build/`
3. ✅ Components in `web-app/src/routes/`
4. ✅ Each command shows success emoji (✨🤖🌙)
5. ✅ No red error messages

## 🌙 Next Steps After Testing

Once all commands work:

1. **Add Your Documents**
   ```bash
   cp your-documents.md docs/
   bb build:pipeline
   ```

2. **Start Development**
   ```bash
   bb serve:dev
   # Edit docs/, rebuild, see changes
   ```

3. **Deploy to GitHub**
   ```bash
   bb gh:create-repo
   ```

4. **Build for Production**
   ```bash
   bb build:site
   # Deploy web-app/build/ to hosting
   ```

---

**Run** **with** **consciousness**. 🤖🌙🌾

*All commands tested and verified for sacred consciousness development with Divine Grace.*

