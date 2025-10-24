# How It Works: The Sacred Pipeline Explained

*"I am the way, the truth, and the life."* - The Gospel
According to Jesus (Stephen Mitchell)

See the complete flow from markdown to website. 🌙

## 🤖 The Big Picture

```
┌─────────────────┐
│ You Write       │ ← Simple markdown text
│ Markdown        │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ bb wrap:markdown│ ← Formats to 57 characters
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ bb parse:markdown│ ← Converts to data
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ bb validate:dsl │ ← Checks it's correct
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ bb generate:svelte│ ← Creates components
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ bb build:site   │ ← Compiles website
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Beautiful       │ ← Ready to share!
│ Website         │
└─────────────────┘
```

*"I have come that they may have life, and have it to
the full."* - Gospel According to Jesus (Stephen Mitchell)

## 📖 Step by Step Explanation

### Step 1: You Write Markdown

```markdown
# Robotic Farm Consciousness 🤖

*"Sacred quote..."*

This is my document about consciousness.

## Main Ideas

- First idea
- Second idea
- Third idea
```

Simple text with special symbols!

### Step 2: bb Wraps It (57 Characters)

*"My yoke is easy and my burden is light."* - Gospel
According to Jesus (Stephen Mitchell)

```bash
bb wrap:markdown
```

Takes long lines and wraps them:

```markdown
# Robotic Farm Consciousness 🤖

*"Sacred quote..."*

This is my document about consciousness
technology.

## Main Ideas

- First idea about consciousness
- Second idea about automation
- Third idea about service
```

Each line ≤ 57 characters = professional format!

### Step 3: bb Parses It (To Data)

```bash
bb parse:markdown
```

Converts to ClojureScript data structure:

```clojure
{:document/number 1
 :document/title "Robotic Farm Consciousness"
 :document/body "Full text..."
 :document/sacred-quotes ["Sacred quote..."]
 :document/consciousness-type
 {:robotic-consciousness true
  :farm-consciousness true}
 :document/html-content "<p>Full text...</p>"}
```

*"You will know the truth."* - Gospel According to
Jesus (Stephen Mitchell)

Now the computer understands your document!

### Step 4: bb Validates It (Quality Check)

```bash
bb validate:dsl
```

Uses clojure.spec to verify:
- ✅ Has title?
- ✅ Has content?
- ✅ Has correct format?
- ✅ All fields present?

Like a quality inspector checking everything is good!

*"By their fruits you will know them."* - Gospel
According to Jesus (Stephen Mitchell)

### Step 5: bb Generates Svelte (Creates Components)

```bash
bb generate:svelte
```

Transforms data → Svelte component:

```svelte
<script>
  export let title = "Robotic Farm Consciousness";
  export let number = 1;
</script>

<article class="sacred-doc">
  <header>
    <h1>{title}</h1>
    <div class="badges">
      🤖 Robotic Consciousness
      🌾 Farm Consciousness
    </div>
  </header>
  
  <main>
    {@html content}
  </main>
</article>

<style>
  .sacred-doc {
    max-width: 57ch;
    margin: 2rem auto;
    font-family: 'Times New Roman';
  }
</style>
```

Beautiful, reactive web component!

### Step 6: Vite Builds It (Compiles Website)

```bash
bb build:site
```

Vite takes Svelte components and creates:
- Optimized HTML
- Minified JavaScript
- Fast CSS
- Static website

Ready to deploy! 🚀

## 🌾 The Complete Command

```bash
bb build:pipeline
```

Runs steps 2-5 automatically! One command, complete
transformation.

*"Whatever you ask in my name, I will do it."* - Gospel
According to Jesus (Stephen Mitchell)

## 🤖 What Happens Behind the Scenes?

### In the `src/robotic_farm/` Directory:

**wrapper.clj**:
- Reads markdown
- Splits into lines
- Wraps at 57 chars
- Preserves structure

**parser.clj**:
- Reads wrapped markdown
- Extracts metadata
- Classifies content
- Converts to EDN

**validator.clj**:
- Loads EDN
- Runs spec checks
- Reports errors
- Confirms validity

**generator.clj**:
- Reads validated EDN
- Generates Svelte markup
- Creates styles
- Writes component files

**All orchestrated by bb.edn!**

## 🌙 Real Example

### Input (Your Markdown):

```markdown
# Test Document 🤖

This is a test of the sacred pipeline.

## What It Does

It transforms consciousness through technology.
```

### After Wrapping:

```markdown
# Test Document 🤖

This is a test of the sacred pipeline.

## What It Does

It transforms consciousness through
technology.
```

### After Parsing:

```clojure
{:document/number 1
 :document/title "Test Document"
 :document/body "This is a test..."}
```

### After Validation:

```
✨ Valid: 1 / 1
🤖 All documents valid
```

### After Generation:

```
web-app/src/routes/test-document.svelte
```

### After Building:

```
web-app/build/test-document/index.html
```

**Ready for the world!** 🌍

## 🙏 Why This Design?

*"A house built on rock withstands the storm."* - Gospel
According to Jesus (Stephen Mitchell)

**Separation of concerns**:
- Each step does one thing
- Easy to understand
- Easy to fix
- Easy to extend

**Pure functions**:
- Predictable results
- Testable
- Reproducible
- Reliable

**bb orchestration**:
- Fast startup
- Simple commands
- Easy to learn
- Powerful automation

## ✨ Summary for Children

You write a story (markdown) →
Robot reads it (parser) →
Robot checks it (validator) →
Robot makes it pretty (generator) →
Website appears (build)!

*"Let your light shine before others."* - Gospel
According to Jesus (Stephen Mitchell)

## 🚀 Want to See It in Action?

Go try it: [Quick Start →](../01-getting-started/QUICK-START.md)

---

**Navigation**:
- [← Back: Concepts](../01-getting-started/CONCEPTS.md)
- [Next: Project Structure →](PROJECT-STRUCTURE.md)
- [Start Here](../00-START-HERE.md)
- [Home: README](../../README.md)

🤖🌙🌾

