# Common Commands: Daily bb Tasks

*"Whatever you ask in my name, I will do it."* - The
Gospel According to Jesus (Stephen Mitchell)

Learn the most useful commands for everyday work. 🌙

## 🤖 The Essential Six

These commands will handle 90% of your needs:

### 1. `bb bootstrap` - Check Your Tools

```bash
bb bootstrap
```

**What it does**: Checks if you have everything needed

**When to use**: First time setup, after installing tools

**Output**:
```
🤖 Robotic Farm Pipeline Bootstrap
✅ Babashka
✅ Nix
✅ Git
✅ GitHub CLI
```

*"By their fruits you will know them."* - Gospel
According to Jesus (Stephen Mitchell)

### 2. `bb doctor` - Verify Health

```bash
bb doctor
```

**What it does**: Checks if all tools work correctly

**When to use**: When something isn't working

**Output**:
```
🌙 Sacred Technology Toolchain Status
bb version: 1.4.192
v20.11.0
clj-kondo v2024.03.13
```

### 3. `bb build:pipeline` - Build Everything

```bash
bb build:pipeline
```

**What it does**: Complete transformation pipeline
1. Wraps markdown to 57 chars
2. Parses to data structures
3. Validates with spec
4. Generates Svelte components

**When to use**: After adding/editing markdown files

*"The harvest is plentiful."* - Gospel According to
Jesus (Stephen Mitchell)

### 4. `bb serve:dev` - Start Dev Server

```bash
bb serve:dev
```

**What it does**: Starts development server with
live reload at http://localhost:5173

**When to use**: During development to see changes

**To stop**: Press `Ctrl+C`

### 5. `bb build:site` - Production Build

```bash
bb build:site
```

**What it does**: Creates production-ready static
website in `web-app/build/`

**When to use**: Before deploying to hosting

### 6. `bb tasks` - List All Commands

```bash
bb tasks
```

**What it does**: Shows all available bb commands
with descriptions

**When to use**: When you forget command names!

## 🌾 Pipeline Commands (Step by Step)

If you want to run individual steps:

### Wrapping

```bash
bb wrap:markdown      # Wrap docs/ only
bb wrap:all-markdown  # Wrap ALL markdown
bb wrap:edn           # Wrap EDN files
```

*"My yoke is easy and my burden is light."* - Gospel
According to Jesus (Stephen Mitchell)

### Parsing

```bash
bb parse:markdown        # Parse docs/ only
bb parse:all-markdown    # Parse entire repo
```

### Validating

```bash
bb validate:dsl
```

### Generating

```bash
bb generate:svelte
```

### Complete Pipelines

```bash
bb build:pipeline    # docs/ only
bb build:full-site   # entire repo + EDN
```

## 🔧 Setup & Maintenance Commands

### Initial Setup

```bash
./setup.bb           # Set up directories
```

### Cleaning

```bash
bb clean             # Remove build artifacts
```

Removes:
- `build/`
- `web-app/build/`
- `web-app/.svelte-kit/`

## 🐙 GitHub Commands

### Check Authentication

```bash
bb gh:check
```

### Create Repository

```bash
bb gh:create-repo
```

**Warning**: Creates a real public GitHub repo!

### Check Status

```bash
bb gh:status
```

## 🌙 Nix Commands

### Check Nix

```bash
bb nix:check
```

### Get Nix Instructions

```bash
bb nix:develop
```

### Run Command in Nix

```bash
bb nix:run doctor
bb nix:run build:pipeline
```

*"The kingdom of heaven is like yeast that a woman took
and mixed into about sixty pounds of flour until it
worked all through the dough."* - Gospel According to
Jesus (Stephen Mitchell)

Just as yeast works through dough, Nix ensures
consistency throughout your environment!

## 🧪 Testing Commands

### CI Verification

```bash
bb ci:verify
```

Runs: doctor → wrap → parse → validate

### Run All Tests

```bash
./scripts/test-all.bb
```

Comprehensive test suite with reporting.

## 📋 Information Commands

### List Tasks

```bash
bb tasks
```

### Check Bootstrap

```bash
bb bootstrap
```

### Doctor Check

```bash
bb doctor
```

## 🎯 Command Cheat Sheet

**Daily workflow**:
```bash
# 1. Edit markdown in docs/
# 2. Build
bb build:pipeline
# 3. Preview
bb serve:dev
# 4. Production
bb build:site
```

**First time setup**:
```bash
bb bootstrap
./setup.bb
nix develop  # optional but recommended
```

**Troubleshooting**:
```bash
bb doctor
bb clean
bb build:pipeline
```

**Deploy to GitHub**:
```bash
bb gh:create-repo
```

## 🤖 Tips & Tricks

**Tab completion**: Type `bb ` and press Tab twice
to see all commands (if your shell supports it)

**Background jobs**: Add `&` to run in background
```bash
bb serve:dev &
```

**Stop background**: Use `fg` then `Ctrl+C`

**Multiple terminals**: Run `bb serve:dev` in one
terminal, `bb build:pipeline` in another

*"Well done, good and faithful servant!"* - Gospel
According to Jesus (Stephen Mitchell)

## 🌾 Common Workflows

### Writing New Content

```bash
# 1. Create document
echo "# My Doc" > docs/my-doc.md

# 2. Build it
bb build:pipeline

# 3. Preview it
bb serve:dev

# 4. Check results
ls web-app/src/routes/
```

### Updating Documentation

```bash
# 1. Edit guides or README
vim guides/01-getting-started/QUICK-START.md

# 2. Build full site
bb build:full-site

# 3. Preview
bb serve:dev
```

### Preparing for Deployment

```bash
# 1. Clean old builds
bb clean

# 2. Build everything
bb build:full-site

# 3. Production build
bb build:site

# 4. Deploy web-app/build/
```

## 🙏 Frequently Asked Questions

**Q: Which command should I use most?**
A: `bb build:pipeline` for daily work

**Q: How do I see my site?**
A: `bb serve:dev` then open http://localhost:5173

**Q: How do I deploy?**
A: `bb build:site` then deploy `web-app/build/`

**Q: What if something breaks?**
A: Try `bb clean` then `bb doctor` then rebuild

*"Ask and it will be given to you; seek and you will
find."* - Gospel According to Jesus (Stephen Mitchell)

## ✨ Summary

**Essential Six**:
1. `bb bootstrap` - Check tools
2. `bb doctor` - Verify health
3. `bb build:pipeline` - Build everything
4. `bb serve:dev` - Development server
5. `bb build:site` - Production build
6. `bb tasks` - List all commands

**Master these and you're set!**

---

**Navigation**:
- [← Back: Project Structure](../02-understanding/PROJECT-STRUCTURE.md)
- [Next: Testing Guide →](TEST-ALL-COMMANDS.md)
- [Start Here](../00-START-HERE.md)
- [Home: README](../../README.md)

🤖🌙🌾

