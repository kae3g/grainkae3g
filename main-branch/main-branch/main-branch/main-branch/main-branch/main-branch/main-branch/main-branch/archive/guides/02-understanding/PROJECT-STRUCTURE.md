# Project Structure: Where Everything Lives

*"A place for everything, and everything in its place."*

Learn the directory layout and what each file does. 🌙

## 🤖 The Big Picture

```
kae3g/12025-10-04/
├── guides/          ← Documentation (you are here!)
├── src/             ← Source code (the brain)
├── scripts/         ← Automation scripts
├── docs/            ← Your markdown files
├── web-app/         ← Website output
├── build/           ← Temporary build files
├── examples/        ← Example documents
├── bb.edn           ← bb task definitions
├── flake.nix        ← Nix configuration
└── README.md        ← Project introduction
```

*"In my Father's house are many rooms."* - The Gospel
According to Jesus (Stephen Mitchell)

## 📚 guides/ - Documentation for Humans

```
guides/
├── 00-START-HERE.md           ← Begin here!
├── 01-getting-started/
│   ├── QUICK-START.md         ← 5-minute tutorial
│   └── CONCEPTS.md            ← Basic ideas
├── 02-understanding/
│   ├── HOW-IT-WORKS.md        ← Pipeline flow
│   ├── PROJECT-STRUCTURE.md   ← This file!
│   ├── PIPELINE-OVERVIEW.md   ← Technical deep-dive
│   ├── BB-ARCHITECTURE.md     ← bb orchestration
│   └── PROJECT-SUMMARY.md     ← Complete overview
├── 03-using/
│   ├── COMMON-COMMANDS.md     ← Daily commands
│   ├── TEST-ALL-COMMANDS.md   ← Testing guide
│   └── RUN-COMMANDS.md        ← Step-by-step
└── 04-advanced/
    ├── DEPLOYMENT.md          ← Publishing sites
    ├── CREATE-GITHUB-REPO.md  ← GitHub setup
    └── FINAL-SUMMARY.md       ← Implementation notes
```

**Purpose**: Help you learn and understand!

*"Seek and you will find."* - Gospel According to Jesus
(Stephen Mitchell)

## 🧠 src/ - The Brain of the Pipeline

```
src/
└── robotic_farm/
    ├── wrapper.clj         ← Wraps text to 57 chars
    ├── edn_wrapper.clj     ← Wraps EDN files
    ├── parser.clj          ← MD → ClojureScript
    ├── validator.clj       ← Quality checks
    └── generator.clj       ← DSL → Svelte
```

**What each does**:

**wrapper.clj**: Takes long lines and makes them 57
characters or less while keeping them readable.

**edn_wrapper.clj**: Same but for EDN (data) files.

**parser.clj**: Reads your markdown and converts it
into data structures the computer understands.

**validator.clj**: Checks that everything is correct
using clojure.spec rules.

**generator.clj**: Takes validated data and creates
beautiful Svelte components.

*"Every good tree bears good fruit."* - Gospel According
to Jesus (Stephen Mitchell)

## 🤖 scripts/ - Automation Helpers

```
scripts/
├── push-to-github.bb     ← Creates GitHub repo
└── test-all.bb           ← Runs all tests
```

**Pure Babashka** - no shell scripts! Fast and reliable.

## 📖 docs/ - Your Content Goes Here

```
docs/
├── 9999967_robotic_farm_consciousness_integration.md
└── your-document.md      ← Add yours here!
```

**This is where you put markdown files to process!**

Place any `.md` file here and run `bb build:pipeline`
to transform it.

## 🌐 web-app/ - Website Configuration

```
web-app/
├── package.json          ← Node dependencies
├── svelte.config.js      ← Svelte setup
├── vite.config.js        ← Build configuration
├── src/
│   ├── lib/             ← Shared components
│   └── routes/          ← Generated pages (output)
└── build/               ← Compiled site (deploy this)
```

**Don't edit routes/ manually!** It's auto-generated.

*"Do not store up treasures on earth... but in heaven."*
- Gospel According to Jesus (Stephen Mitchell)

The build/ directory is temporary - the real treasure
is your markdown content in docs/!

## 🏗️ build/ - Temporary Build Files

```
build/
├── wrapped.md                ← 57-char wrapped
├── parsed-documents.edn      ← Parsed data
└── validated-documents.edn   ← Validated data
```

**Purpose**: Intermediate files during the build.
You can delete this directory anytime - it rebuilds!

## 📋 examples/ - Learning Materials

```
examples/
└── example-wrapped-output.md  ← Sample output
```

**Purpose**: Show you what output looks like.

## ⚙️ Configuration Files (Root Directory)

**bb.edn** - The heart of the automation!
```clojure
{:tasks
 {doctor {...}
  build:pipeline {...}
  serve:dev {...}}}
```

Defines all `bb` commands. 35+ tasks!

**flake.nix** - Reproducible environment
```nix
{buildInputs = [
  pkgs.babashka
  pkgs.nodejs_20
  pkgs.clj-kondo
  ...]}
```

Ensures everyone has the same tools.

**bootstrap.bb** - Checks prerequisites
**setup.bb** - Sets up directories

*.gitignore* - What Git ignores (build/, node_modules/)

*UNLICENSE* - Public domain dedication

*CHANGELOG.md* - Version history

## 🌙 How Files Flow Through the System

```
1. You create:  docs/my-doc.md
2. bb wraps:    build/wrapped.md
3. bb parses:   build/parsed-documents.edn
4. bb validates: build/validated-documents.edn
5. bb generates: web-app/src/routes/my-doc.svelte
6. Vite builds:  web-app/build/my-doc/index.html
```

*"I am the vine; you are the branches."* - Gospel
According to Jesus (Stephen Mitchell)

Each step feeds into the next!

## 🤖 What Can You Safely Modify?

**✅ Safe to Edit**:
- `docs/*.md` - Your content
- `guides/*.md` - Documentation
- `examples/*.md` - Examples
- `README.md` - Main readme

**⚠️ Edit Carefully**:
- `bb.edn` - Task definitions
- `src/robotic_farm/*.clj` - Pipeline code
- `flake.nix` - Nix packages

**❌ Don't Edit**:
- `build/` - Auto-generated
- `web-app/src/routes/` - Auto-generated
- `web-app/build/` - Auto-generated
- `node_modules/` - Dependencies

## 🌾 Finding What You Need

**"Where do I put my markdown?"**
→ `docs/`

**"Where are the bb commands defined?"**
→ `bb.edn`

**"Where's the generated website?"**
→ `web-app/build/`

**"Where do I learn how to use this?"**
→ `guides/00-START-HERE.md`

**"Where's the source code?"**
→ `src/robotic_farm/`

*"Ask and it will be given to you."* - Gospel According
to Jesus (Stephen Mitchell)

## 🙏 Design Philosophy

The structure follows these principles:

**Clear naming**: Directories explain their purpose
**Logical grouping**: Related files together
**Beginner-friendly**: Easy to navigate
**Sacred attention**: Contemplative organization

## ✨ Summary for Children

Think of the project as a house:

- **guides/** = Library (where you learn)
- **src/** = Kitchen (where magic happens)
- **docs/** = Your room (where you create)
- **web-app/** = Display case (what others see)
- **build/** = Workshop (temporary mess)
- **bb.edn** = Recipe book (instructions)

---

**Navigation**:
- [← Back: How It Works](HOW-IT-WORKS.md)
- [Next: Pipeline Overview →](PIPELINE-OVERVIEW.md)
- [Start Here](../00-START-HERE.md)
- [Home: README](../../README.md)

🤖🌙🌾

