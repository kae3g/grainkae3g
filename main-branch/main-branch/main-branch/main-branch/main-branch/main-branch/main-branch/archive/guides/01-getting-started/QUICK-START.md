# Quick Start: First Steps

Welcome! Let's get you running in 5 minutes.

## What You'll Learn

By the end of this guide, you'll:
- ✅ Have all tools installed
- ✅ Run your first bb command
- ✅ See the pipeline work
- ✅ Create something beautiful
- ✅ Understand the basic workflow

## Step 1: Check if You Have Babashka

Open your terminal and type:

```bash
bb --version
```

**If it works**: You'll see `babashka v1.x.x` ✅
**If it doesn't**: Install it (see below)

### Installing Babashka

**On Mac**:
```bash
brew install babashka
```

**On Linux/Nix**:
```bash
nix-env -iA nixpkgs.babashka
```

## Step 2: Enter the Project

```bash
cd ~/kae3g/12025-10-04
```

## Step 3: Check Prerequisites

```bash
bb bootstrap
```

You should see:
```
Documentation Pipeline Toolchain Status
✅ Babashka
✅ Nix
✅ Git
```

## Step 4: Set Up Directories

```bash
./setup.bb
```

This creates the folders you need for building your
documentation. The setup script prepares your project
structure automatically.

## Step 5: See What You Can Do

```bash
bb tasks
```

This shows all available commands - your complete
toolkit for building documentation!

## Step 6: Run Your First Build

Create a simple test document:

```bash
echo "# My First Document

This is my first documentation pipeline document.

## What I Learned

I learned that:
- Babashka is fast
- bb commands are simple
- The pipeline automates everything
- 57-character lines are readable

Great for technical documentation!" > docs/test.md
```

Now build it:

```bash
bb build:pipeline
```

Watch as your document is automatically wrapped,
parsed, validated, and transformed into a website!

## Step 7: See Your Results

```bash
ls -la build/
ls -la web-app/src/routes/
```

You created:
- Wrapped markdown (57 characters per line)
- Parsed data structures
- Svelte components
- A website ready to deploy!

## Common Issues

**"bb: command not found"**
→ Install babashka (see Step 1)

**"Permission denied"**
→ Run: `chmod +x setup.bb bootstrap.bb`

**"No markdown files found"**
→ Make sure you created docs/test.md

## Congratulations!

You've completed your first documentation build!
You now understand the basic workflow and can start
creating your own documentation sites.

## What's Next?

1. **Learn concepts**: [Basic Concepts →](CONCEPTS.md)
2. **Understand system**: [How It Works →](../02-understanding/HOW-IT-WORKS.md)
3. **Try more commands**: [Common Commands →](../03-using/COMMON-COMMANDS.md)

---

**Navigation**:
- [← Back: Start Here](../00-START-HERE.md)
- [Next: Concepts →](CONCEPTS.md)
- [Home: README](../../README.md)

