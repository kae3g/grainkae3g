# kae3g 9598: Documentation - Writing for Humans

**Phase 1: Foundations & Philosophy** | **Week 5** | **Reading Time: 16 minutes**

---

## What You'll Learn

- Why documentation matters (code is read more than written)
- Different types of docs (README, API, tutorials, guides)
- How to explain complex systems simply
- The art of good examples
- Documentation as knowledge preservation (House of Wisdom!)
- Why plain text docs outlast everything else
- Documentation as tending the knowledge garden

---

## Prerequisites

- **[9560: Text Files](/12025-10/9560-text-files-universal-format)** - Markdown for docs
- **[9596: Version Control](/12025-10/9596-version-control-git-foundations)** - Version-control docs
- **[9505: House of Wisdom](/12025-10/9505-house-of-wisdom-knowledge-gardens)** - Knowledge preservation

---

## Code is Read 10x More Than Written

**Fundamental truth**:

```
Time writing code:   10 hours
Time reading code:   100 hours (you + others + future you)
```

**Good documentation**:
- Saves 90 hours of reading time
- Enables others to contribute
- Helps future you (6 months later, you forget everything!)

**Plant lens**: **"Documentation is the garden's guidebook—helps new gardeners understand what's planted, why, and how to tend it."**

---

## Types of Documentation

### README.md (The Welcome Mat)

**First thing people see**:

```markdown
# Project Name

One-sentence description.

## Quick Start
```bash
git clone ...
npm install
npm start
```

## Features
- Feature A
- Feature B

## Documentation
See [docs/](docs/) for full guide.
```

**Goal**: Get someone **started** in 5 minutes.

**This valley**: Every essay starts with "What You'll Learn" (mini-README!).

### API Documentation

**For functions/libraries**:

```clojure
(defn process-data
  "Transforms input data according to schema.
  
  Args:
    data - Vector of maps with :id and :value keys
    schema - Transformation spec
  
  Returns:
    Transformed vector of maps
  
  Example:
    (process-data [{:id 1 :value 10}] identity-schema)
    ;; => [{:id 1 :value 10}]"
  [data schema]
  (transform data schema))
```

**Good docs include**:
- What it does (high-level)
- Parameters (types, meaning)
- Return value (type, meaning)
- Examples (concrete usage)

### Tutorials (Step-by-Step)

**Teach by doing**:

```markdown
# Tutorial: Build Your First Web App

## Step 1: Setup
Install Node.js, create project.

## Step 2: Create Server
Write this code: [example]

## Step 3: Test
Run `npm start`, visit http://localhost:3000

## Step 4: Deploy
Push to GitHub, configure hosting.
```

**Goal**: Take someone from **zero to working** (hand-holding).

### Guides (Conceptual)

**Explain ideas**:

```markdown
# Understanding React Hooks

Hooks are functions that let you "hook into" React state.

## Why Hooks?
Classes were complex, functional components are simpler.

## Common Hooks
- useState: Add state to component
- useEffect: Run side effects
```

**Goal**: **Deep understanding** (concepts, not just steps).

**This valley**: Most essays are guides (teaching concepts, not just commands).

---

## The Art of Good Examples

**Bad example** (too abstract):

```python
def foo(x, y):
    """Processes inputs."""
    return bar(x) + baz(y)
```

**What does this DO?** (Unclear!)

**Good example** (concrete, clear):

```python
def calculate_total_price(items, tax_rate):
    """Calculates total price including tax.
    
    Example:
        items = [10.00, 20.00, 5.00]  # Prices
        tax_rate = 0.08  # 8% tax
        calculate_total_price(items, tax_rate)
        # Returns: 37.80 (35.00 + 2.80 tax)
    """
    subtotal = sum(items)
    tax = subtotal * tax_rate
    return subtotal + tax
```

**Now clear!** (Concrete numbers, real-world meaning)

**Guideline**: **Show, don't just tell** (examples > abstract descriptions).

---

## Documentation Principles

### 1. Start with Why

**Bad**: "This function does X."

**Good**: "We need X because of Y. This function does X."

**Example**:

```markdown
## Why Immutability?

Mutable state causes race conditions in concurrent code.
By using immutable data, we eliminate entire classes of bugs.

The `update` function creates a new version instead of mutating.
```

**Context** helps understanding.

### 2. Write for Beginners

**You know the system**. Readers don't.

**Bad**: "Configure the ingress controller."

**Good**: "The ingress controller routes external traffic to your services. Edit `ingress.yaml` to add routes."

**Don't assume knowledge** (or explain terms first).

### 3. Use Plain Language

**Bad**: "Instantiate a singleton factory bean."

**Good**: "Create one shared object that all code uses."

**Jargon is a barrier** (use when necessary, explain first).

### 4. Show the Path

**Bad**: List of features (what it CAN do).

**Good**: Learning path (what to learn FIRST, then next).

**This valley**: Phased curriculum (9500→9600→9700... clear progression!).

---

## Documentation as Preservation

**House of Wisdom** (Essay 9505) preserved Greek knowledge through **translation and copying**.

**Modern equivalent**: **Documentation in version control**.

```bash
# Your docs are versioned
git log docs/

# Can see how understanding evolved
git show abc123:docs/architecture.md

# Preserved forever (like manuscripts!)
```

**Why plain text** (Essay 9560):
- Survives format changes (Markdown will outlast Word!)
- Version-controllable (Git diffs show what changed)
- Searchable (grep, ripgrep work)
- Portable (opens anywhere)

**Plant lens**: **"Documentation is seed-saving—preserve knowledge in durable form (text) so future generations can replant."**

---

## Common Documentation Mistakes

### 1. No Examples

**Docs say**: "Use the `transform` function."

**Reader asks**: "HOW??"

**Fix**: Show example (even simple one helps!).

### 2. Out-of-Date

**Code evolved**, docs didn't:

```python
# Docs say:
app.run(port=3000)

# Actual API:
app.start(config={'port': 3000})  # Changed!
```

**Prevention**:
- Version docs with code (same Git repo)
- Test code examples (doctests, CI)
- Review docs during code review

### 3. Too Much Detail

**Docs explain** every parameter, every edge case, every internal:

```
The FrobnicatorService uses a BeanFactory to instantiate...
(5 pages of internals)
```

**User just wants**: "How do I use this?"

**Fix**: Start simple (quick start), then deep dives (optional advanced docs).

---

## Documentation for the Valley

**Our essays ARE documentation**:

### Structure
- **What You'll Learn** (overview - like README)
- **Prerequisites** (learning path)
- **Concepts** (guide - deep explanations)
- **Try This** (tutorial - hands-on)
- **Going Deeper** (links to more)
- **Summary** (key takeaways)

**This is intentional** (optimized for learning!).

### Plain Text

**All Markdown**:
- Version-controlled (Git)
- Future-proof (will outlast proprietary formats)
- Portable (render on web, CLI, PDF, etc.)

### Cross-Referenced

**Every essay** links to related essays:
- Prerequisites (what to read first)
- Going Deeper (related topics)
- Navigation (previous/next in sequence)

**This builds a knowledge web** (not isolated docs).

---

## Try This

### Exercise 1: Improve a README

**Take a project** (yours or open source):

**Check**:
- Does it have a README?
- Does it explain WHAT the project does?
- Does it have Quick Start?
- Does it have examples?

**If missing**: Add them! (Pull request = contribution!)

---

### Exercise 2: Write API Doc

**Pick a function** you wrote:

**Add docstring**:
```clojure
(defn your-function
  "One-sentence description.
  
  Longer explanation if needed.
  
  Args:
    x - What x means
    y - What y means
  
  Returns:
    What it returns
  
  Example:
    (your-function 5 10)
    ;; => 50"
  [x y]
  (* x y))
```

---

### Exercise 3: Explain to a Beginner

**Pick a complex concept** (Git, Nix, monads, whatever).

**Write 3 paragraphs**:
1. What it is (simple terms)
2. Why it exists (what problem it solves)
3. How to use it (minimal example)

**No jargon** (or explain jargon first).

**Share** with someone unfamiliar (see if they understand!).

---

## Going Deeper

### Related Essays
- **[9560: Text Files](/12025-10/9560-text-files-universal-format)** - Markdown for docs
- **[9505: House of Wisdom](/12025-10/9505-house-of-wisdom-knowledge-gardens)** - Knowledge preservation
- **[9596: Version Control](/12025-10/9596-version-control-git-foundations)** - Version docs with code
- **All valley essays** - Examples of structured documentation!

### External Resources
- **"Docs for Developers"** - Jared Bhatti (comprehensive guide)
- **Write the Docs** - Community and conference
- **Divio Documentation System** - 4-type framework (tutorial, how-to, reference, explanation)
- **Readme Driven Development** - Write README first (like TDD but for docs)

---

## Reflection Questions

1. **Why is documentation often an afterthought?** (Coding is "real work", docs are "extra"—wrong mindset!)

2. **Should code be self-documenting?** (Good names help, but not enough—you still need docs for WHY)

3. **How do you keep docs updated?** (Version with code, test examples, make it easy to update)

4. **Is there such a thing as too much documentation?** (Yes—overwhelming! Start simple, add depth progressively)

5. **How would Nock docs work?** (12 rules fit on 2 pages—minimal surface area to document!)

---

## Summary

**Documentation Matters Because**:
- Code is read 10x more than written
- Helps others contribute
- Helps future you
- Preserves knowledge

**Types of Documentation**:
- **README**: Quick start, overview
- **API docs**: Function/class reference
- **Tutorials**: Step-by-step (beginner-friendly)
- **Guides**: Conceptual (deep understanding)

**Good Documentation**:
- **Starts with why** (context before details)
- **Shows examples** (concrete > abstract)
- **Uses plain language** (no unnecessary jargon)
- **Provides learning path** (beginner → advanced)

**Common Mistakes**:
- No examples (too abstract)
- Out-of-date (code evolved, docs didn't)
- Too much detail (overwhelms beginners)

**Best Practices**:
- **Plain text** (Markdown - version-controllable, future-proof)
- **Version with code** (docs in same Git repo)
- **Test examples** (ensure they work!)
- **Cross-reference** (build knowledge web)

**In the Valley**:
- **Every essay is documentation** (teaching complex systems)
- **Structured for learning** (What/Why/How/Try/Deeper/Summary)
- **Plain text** (Markdown - survives decades)
- **Cross-referenced** (knowledge web, not islands)
- **Version-controlled** (Git - every change preserved)

**Plant lens**: **"Documentation is the garden guidebook—explains what's planted, why each plant is there, how to tend them, and what they yield."**

---

**Next**: We'll explore **debugging**—the art of finding and fixing bugs, mental models for understanding failures, and tools that help!

---

**Navigation**:  
← Previous: [9598 (testing verification validation)](/12025-10/9598-testing-verification-validation) | **Phase 1 Index** | Next: [9600 (debugging finding fixing issues)](/12025-10/9600-debugging-finding-fixing-issues)

**Metadata**:
- **Phase**: 1 (Foundations)
- **Week**: 5
- **Prerequisites**: 9560, 9596, 9505
- **Concepts**: Documentation, README, API docs, tutorials, guides, plain text, knowledge preservation
- **Next Concepts**: Debugging, troubleshooting, mental models, tools
- **Plant Lens**: Garden guidebook (knowledge transfer), seed-saving (preservation), tending instructions



---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*