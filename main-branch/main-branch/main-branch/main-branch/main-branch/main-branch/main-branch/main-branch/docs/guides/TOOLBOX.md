# Toolbox

> ⚠️ **Work in Progress**: This document is still being developed and is not yet satisfactory. Check back later for improvements.

---

Here are your tools.

---

## 🔧 The Toolbox

```
┌─────────────────────────────┐
│  YOUR TOOLS                 │
│  Five things. That's it.    │
└─────────────────────────────┘
```

1. **Laptop**
2. **Text editor**
3. **Script system**
4. **Build framework**
5. **Publish tool**

```
┌──────────────────────────────────┐
│ DISCOVERED: TUNDRA LAWS          │
│ These rules cannot be broken.    │
└──────────────────────────────────┘
```

**Small is strong**
```
Small → Understand it
Understand → Fix it
Fix → Trust it
```

**Fast is better**
```
Fast start → Test now
Test now → Learn now
Learn now → Build now
```

**Same every time**
```
Once → Luck
Twice → Still luck
Always → Real
```

**Works alone**
```
Need network → Not yours
Works offline → Yours
```

---

## 🔷 Tool 1: Laptop

```
╔══════════════════════════════╗
║ FOUND: LAPTOP                ║
║ "Where you build"            ║
╚══════════════════════════════╝
```

**What it is**: Your workbench. Everything happens here.

**Two good choices**:

### MacBook Air (Apple Silicon M-series)

**An apology**: Yes, it's Apple. Yes, it's expensive. Yes, it's not fully open.

But it works. M1/M2/M3 chips are fast and quiet. Battery lasts all day. Build tools run well.

**Recommendation**: macOS Sequoia (15.0) or newer. Keep it updated.

**Why**: Cursor, `bb`, Nix, Git all work great on Mac. The tools in this guide were developed here.

**Trade-off**: You don't own the hardware fully. Apple controls more than we'd like. But for getting work done now, it works.

### Framework Laptop (Linux)

**The better choice**: Repairable. Upgradeable. Open. You actually own it.

**Why Framework**: 
- Every part replaceable
- Modular ports you choose
- Open schematics
- Company supports right-to-repair
- Built to last decades, not years

**OS recommendation**: We're building **tundraOS** for Framework (active project). Until it's ready:
- NixOS (our recommendation)
- Arch Linux
- Ubuntu (if you want easy)

**Trade-off**: More setup work. Linux has edges. But you own the machine completely.

**Our work**: We're getting tundraOS running on Framework first. Frozen specs, minimal complexity, maximum repairability. Stay tuned.

**Get it**: https://frame.work/

---

**The honest answer**: Use what you have. Mac Air works now. Framework is the future. Both are good enough to build on.

---

## 🔷 Tool 2: Text Editor (Cursor)

```
╔══════════════════════════════╗
║ FOUND: TEXT EDITOR           ║
║ "For writing code"           ║
╚══════════════════════════════╝
```

**What it is**: Where you write. Has AI built in (Claude, GPT-4).

**Why this one**: VSCode, but better. Faster. AI that actually helps.

**Get it**: https://cursor.sh → Download → Install

**Cost**: $20/month (worth it)

---

## 🔷 Tool 3: Script System (`bb`)

```
╔══════════════════════════════╗
║ FOUND: SCRIPT SYSTEM         ║
║ "For automating tasks"       ║
╚══════════════════════════════╝
```

**What it is**: Runs scripts. Fast. Doesn't break.

**Why this one**: Bash breaks. Python needs setup. This just works.

**A script**:
```clojure
#!/usr/bin/env bb
(println "Hello")
```

Runs in 0.02 seconds. Always works.

**Get it**:
```
brew install babashka
Done
```

**Use for**: Build scripts, git hooks, anything that repeats

---

## 🔷 Tool 4: Build Framework (Nix + Git)

```
╔══════════════════════════════╗
║ FOUND: BUILD FRAMEWORK       ║
║ "For making and tracking"    ║
╚══════════════════════════════╝
```

**What it is**: Two tools that work together. Nix builds things the same way every time. Git remembers every version.

### Nix: Same Build Every Time

**The problem**:
```
You: "Clone this, run it"
Friend: "Different Node version, it breaks"
You: "Works on my machine"
Friend: "Not helpful"
```

**With Nix**:
```
You: "nix develop"
Friend: "nix develop"
Both: Same environment
Both: It works
```

**How**: Every build specifies exactly what it needs. Gets exactly that. Every time.

**Get it**:
```
curl -L nixos.org/nix/install | sh
Wait a bit
Done
```

### Git: Remember Everything

**What it does**: Saves every version of your work. Forever.

**Why**: You'll make mistakes. Git lets you undo them.

**Six commands** (90% of what you need):
```
git init      # Start tracking
git add .     # Stage changes
git commit    # Save snapshot
git push      # Upload
git pull      # Download
git log       # History
```

**Get it**:
```
Already installed (probably)
If not: brew install git
```

### Together

Nix builds the same way everywhere. Git tracks every change. Together they make sure your work is reproducible and never lost.

**Use for**: Everything. Every project. No exceptions.

---

## 🔷 Tool 5: Publish Tool (Codeberg)

```
╔══════════════════════════════╗
║ FOUND: PUBLISH TOOL          ║
║ "For sharing your work"      ║
╚══════════════════════════════╝
```

**What it is**: Where you put your code. Like GitHub, but not Microsoft.

**Why this one**: Non-profit. No tracking. Free forever. Germany-based.

**What you get**:
```
✓ Git hosting
✓ Issue tracking
✓ CI/CD
✓ Static websites
✓ Actually free
```

**Get it**:
```
Sign up: codeberg.org
Cost: €0 (they mean it)
```

**Use for**: Public projects, private projects, websites

---

## 🔷 Bonus: Process Supervisor (s6)

```
╔══════════════════════════════╗
║ FOUND: OPTIONAL TOOL         ║
║ "For keeping things running" ║
╚══════════════════════════════╝
```

**What it is**: Keeps programs running. Restarts them if they crash.

**Why this one**: systemd is 60,000 lines. s6 is 9,000. Both start processes.

**How simple**:
```
service/myapp/
├── run     # Start script
└── finish  # Cleanup script
```

Two files. That's it.

**Get it**:
```
wget skarnet.org/software/s6/s6.tar.gz
Build it yourself (30 seconds)
```

**Use for**: Servers, containers, anything that must stay running  
**Skip if**: You don't run servers yet

---

## 🗑️ What You Don't Need

```
┌─────────────────────────────┐
│  SKIP THESE                 │
│  They'll slow you down.     │
└─────────────────────────────┘
```

**Docker**: Nix does the same thing, better

**npm**: Slow. `bb` is faster

**Kubernetes**: Too complex. One server with s6 is enough

**Slack**: Distracting. Use email

---

## 🔧 Using Them Together

```
┌─────────────────────────────┐
│  THE WORKFLOW               │
│  Like any craft             │
└─────────────────────────────┘
```

**To build**:
```
cursor .      # Open editor
             # Write code
bb build      # Run scripts
git commit    # Save progress
```

**To deploy**:
```
git push      # Upload to Codeberg
nix build     # Build same way everywhere
             # Deploy (details depend on your setup)
```

Simple. Repeatable. Works.

---

## ✓ Why These Tools

```
┌─────────────────────────────┐
│  GOOD TOOLS                 │
│  Pass all three tests       │
└─────────────────────────────┘
```

**Test 1: Fast to set up**  
Can you install and use it in 10 minutes? Yes.

**Test 2: Works offline**  
Does it need the internet after install? No.

**Test 3: Will last**  
Will it work in 2035? Probably.

These tools pass. That's why they're here.

---

## 📝 Getting Started

```
┌─────────────────────────────┐
│  START HERE                 │
│  One step at a time         │
└─────────────────────────────┘
```

**Today**:
- Install Git and Cursor
- Write some code
- Save it with Git

**This week**:
- Install `bb`
- Write a script to automate something
- Push to Codeberg

**This month**:
- Install Nix
- Make a reproducible build
- Show someone else it works on their machine too

**This year**:
- Use these tools daily
- Build something that lasts
- Help someone else get started

---

## 📜 What You Get

These tools have been around for decades. They work. They'll keep working.

New tools show up every week. Most are gone in a month. These survived because they're good.

Use them. Build things. Stop fixing tools. Start making things that matter.

---

## ⚠️ Fair Warning

Once you use good tools, bad tools become obvious. You can't unsee it.

After Nix, "works on my machine" becomes unacceptable.

After Git, losing work becomes impossible.

After `bb`, bash scripts feel fragile (because they are).

This is permanent. You've been warned.

---

## 🛠️ Where to Start

```
┌─────────────────────────────┐
│  DON'T NEED ALL AT ONCE     │
│  Start with three           │
└─────────────────────────────┘
```

**Start with these three**:
- Git (save your work)
- Cursor (write code)
- Codeberg (share it)

**Add when ready**:
- `bb` (automate tasks)
- Nix (build the same way everywhere)

**Add if needed**:
- s6 (keep servers running)

You don't need everything today. Start small. Add as you go.

---

## 🏁 That's It

```
┌─────────────────────────────┐
│  YOU HAVE THE TOOLS         │
│  Now build something        │
└─────────────────────────────┘
```

Five tools. Like any toolbox.

Text editor. Script system. Build framework. Version control. Publish tool.

You have them now. They work. They'll keep working.

The hard part isn't the tools. The hard part is using them to build something that matters.

Good luck with that.

---

**Next steps**: Close this. Open your editor. Make something.

---

*Tools don't make builders. But builders need tools.*

*Start.* ❄️ 

---

**License**: Apache-2.0 OR MIT  
**Warranty**: None. Tools work. You might not. Practice.

**Coldriver Tundra** — Competitive technology in service of clarity and beauty

