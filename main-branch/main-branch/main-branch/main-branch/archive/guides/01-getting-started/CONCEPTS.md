# Basic Concepts: Understanding the Pipeline

Learn the fundamental ideas behind this documentation
pipeline project.

## What Is a Pipeline?

Imagine water flowing through pipes:

```
Water → Filter → Purifier → Clean Water
```

Our pipeline is similar:

```
Markdown → Parser → Validator → Svelte → Website
```

**Each step transforms the data**, making it better!

Just as water flows through connected pipes, your
documentation flows through connected processing
steps, improving at each stage.

## 📖 What Is Markdown?

Markdown is simple text with special symbols:

```markdown
# This is a heading
**This is bold**
*This is italic*
- This is a list item
```

It's like writing notes that computers can understand!

**Why use it?**
- Easy to write (just text with simple symbols)
- Easy to read (no HTML clutter)
- Easy to transform (computers understand it)
- No complicated formatting (focus on content)
- Widely supported (GitHub, editors, wikis)

## What Is Babashka (bb)?

Babashka is a fast Clojure interpreter designed
for scripting and automation. Think of it as your
build automation assistant.

**Key features**:
- Extremely fast (starts in milliseconds)
- Runs Clojure scripts instantly
- Orchestrates complex build tasks
- Native binary (no JVM startup time)
- Perfect for CI/CD pipelines

```bash
bb doctor    # Check system health
bb build     # Build documentation
bb serve     # Start development server
```

Instead of writing bash scripts, you write Clojure
code that runs instantly with Babashka.

## 🔄 What Is ClojureScript?

ClojureScript is a programming language that:
- Reads like sentences
- Uses parentheses `(like this)`
- Transforms data
- Is very powerful

Example:
```clojure
(println "Hello, consciousness!")
```

**Don't worry!** You don't need to write it - the
pipeline generates it for you!

## What Is Svelte?

Svelte is a modern web framework that compiles
your components into optimized JavaScript. Unlike
other frameworks, Svelte does the work at build
time, not in the browser.

**Why Svelte?**
- Fast (no virtual DOM overhead)
- Reactive (updates automatically)
- Beautiful (modern component syntax)
- Small (tiny bundle sizes)
- Easy to learn (less boilerplate)

Your documentation becomes an interactive, fast
website with minimal JavaScript.

## What Is Nix?

Nix is a package manager and build system that
ensures reproducible builds. Think of it as a way
to freeze your development environment.

**Key features**:
- Declarative (specify exact versions)
- Reproducible (same build every time)
- Isolated (no conflicts between projects)
- Cross-platform (Mac and Linux)

**Benefits**:
- Your build works on any computer
- Team members have identical environments
- Easy to onboard new developers
- Professional standard for serious projects

## What Is the 57-Character Wrap?

This pipeline wraps all text to 57 characters per
line for several important reasons:

- **Readability** - Optimal line length
- **Terminal-friendly** - Fits standard widths
- **Git-friendly** - Clear diffs
- **Professional** - Consistent appearance
- **Accessible** - Works in any editor

```
This line is exactly 57 characters long - see how?
```

The wrapper automatically handles this for you,
breaking lines at word boundaries while preserving
code blocks and special formatting.

## 📊 How They All Work Together

```
You write Markdown (simple text)
    ↓
bb reads it (fast automation)
    ↓
ClojureScript transforms it (data processing)
    ↓
Svelte makes it beautiful (web components)
    ↓
Nix ensures it works everywhere (reproducibility)
    ↓
You get a website! (consciousness serving)
```

## Why This Approach?

This pipeline architecture provides several key
benefits:

**Simple**: Write plain markdown
**Powerful**: Full automation
**Fast**: Babashka starts instantly
**Beautiful**: Svelte creates modern sites
**Reliable**: Nix ensures reproducibility
**Professional**: Consistent 57-char formatting

## Simple Summary

Think of it as an assembly line for documentation:

1. You write your content (markdown)
2. Babashka orchestrates the build
3. Wrapper formats to 57 characters
4. Parser extracts structure
5. Validator checks correctness
6. Generator creates Svelte components
7. Vite builds the static website
8. You deploy to any static host!

Each step is automatic once you run `bb build:pipeline`.

## ✨ Ready to Continue?

Now you understand the basics! Time to see how it all
works together.

---

**Navigation**:
- [← Back: Quick Start](QUICK-START.md)
- [Next: How It Works →](../02-understanding/HOW-IT-WORKS.md)
- [Start Here](../00-START-HERE.md)
- [Home: README](../../README.md)

