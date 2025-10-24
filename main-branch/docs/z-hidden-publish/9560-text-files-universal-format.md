# kae3g 9560: Text Files - The Universal Format That Survives

**Phase 1: Foundations & Philosophy** | **Week 3** | **Reading Time: 14 minutes**

---

## What You'll Learn

- Why plain text outlasts all proprietary formats
- How text files enable the Unix philosophy
- Text vs binary: trade-offs and when each matters
- Markup languages: Markdown, HTML, LaTeX, EDN
- Why configuration as text is powerful
- The longevity argument: Will your files be readable in 50 years?
- How the House of Wisdom preserved knowledge through text

---

## Prerequisites

- **[9510: Unix Philosophy](/12025-10/9510-unix-philosophy-do-one-thing-well)** - Text as universal interface
- **[9550: The Command Line](/12025-10/9550-command-line-your-primary-interface)** - Working with text

---

## The Universal Format

**Text files are**:
- Human-readable (open with any editor)
- Platform-independent (same on Linux, macOS, Windows)
- Tool-agnostic (grep, sed, awk, cat all work)
- Future-proof (readable in decades)
- Version-controllable (Git diffs show what changed)
- Composable (Unix pipes work on text)

**This is why** configuration files, source code, documentation, logs, and data formats all default to **text**.

**Example**:

**Text file** (`.txt`, `.md`, `.json`, `.xml`):
```
You can read this!
Open it with any program.
It will still be readable in 2075.
```

**Binary file** (`.docx`, `.pdf`, `.xlsx`, `.psd`):
```
ÐÏࡱá>þÿ þÿÿÿ ÿÿÿÿÿÿÿÿÿÿ...
(Requires specific software to decode)
(Might not be readable in 2075)
```

---

## Text vs Binary

### When Text Wins

**Use text for**:
- **Configuration** (`.json`, `.yaml`, `.toml`, `.edn`)
- **Source code** (`.clj`, `.py`, `.rs`, `.nix`)
- **Documentation** (`.md`, `.txt`, `.org`)
- **Data exchange** (JSON, CSV, EDN)
- **Logs** (application logs, system logs)

**Why**:
- Human-editable (no special tools)
- Searchable (`grep "error" log.txt`)
- Diffable (`git diff` shows changes)
- Debuggable (read the file, understand what's wrong)

### When Binary Wins

**Use binary for**:
- **Images** (`.png`, `.jpg` - pixels are binary data)
- **Video/Audio** (`.mp4`, `.mp3` - waveforms are binary)
- **Compiled code** (`.exe`, `.so` - machine instructions)
- **Databases** (SQLite, Postgres - optimized binary formats)
- **Large datasets** (Parquet, HDF5 - compression + fast access)

**Why**:
- **Compact** (images/video/audio are huge as text)
- **Fast** (binary parsing is faster than text parsing)
- **Specialized** (structured for specific use case)

**Trade-off**: Binary is **efficient**, text is **durable**.

---

## The Longevity Argument

**Question**: Will your files be readable in 50 years?

### Text Files: High Probability

**ASCII** (1963) is **still readable** today (62 years later).

**UTF-8** (1993) is **backward-compatible** with ASCII (32 years and counting).

**Markdown** (2004) is still the dominant lightweight markup (21 years).

**Prediction**: Plain text in UTF-8 will be readable in **2075** (50 years from now).

**Why**: Text is **simple** (just character sequences). No complex spec to change.

### Binary Files: Low Probability

**Microsoft Word `.doc` format** (1983-2003):
- **Replaced** by `.docx` (2007, incompatible format!)
- Old `.doc` files **require** old software (or imperfect converters)
- In 2075? Good luck finding a `.doc` reader.

**Adobe Photoshop `.psd`** (1990):
- Proprietary format (Adobe doesn't fully document it)
- Requires Photoshop (or GIMP with imperfect PSD support)
- In 2075? Maybe Adobe still exists. Maybe not.

**PDF** (1993):
- Better (open spec since 2008)
- But: Complex (ISO 32000, 700+ page spec)
- Features break (embedded fonts, JavaScript, DRM)
- In 2075? Probably readable, but some features lost.

**Plant lens**: **"Seeds (text) survive centuries in the seed bank. Fruit (binary) rots quickly."**

---

## Markup Languages: Text with Structure

**Plain text is great**, but sometimes you need **structure** (headings, lists, links).

**Markup languages** add structure **while remaining text**.

### Markdown (2004, John Gruber)

**Designed for**: Human-readable documents that convert to HTML.

```markdown
# Heading 1
## Heading 2

**Bold text**
*Italic text*

- List item 1
- List item 2

[Link](https://example.com)

`Code inline` or:

```clojure
(defn example [] "Code block")
```
```

**Readable as text**, but **converts to HTML** for web.

**This entire valley** is written in Markdown! (All essays are `.md` files.)

**Why Markdown wins**:
- Simple (learn in 10 minutes)
- Readable (even without rendering)
- Convertible (to HTML, PDF, slides, ebooks)
- Everywhere (GitHub, forums, documentation)

### HTML (1991, Tim Berners-Lee)

**The web's native format**:

```html
<h1>Heading</h1>
<p>This is a <strong>paragraph</strong> with <em>emphasis</em>.</p>
<ul>
  <li>List item</li>
</ul>
```

**More verbose than Markdown**, but more **powerful** (CSS, JavaScript, semantic tags).

**Still text!** (Can edit with any editor.)

### LaTeX (1984, Leslie Lamport)

**For academic papers, mathematics**:

```latex
\documentclass{article}
\begin{document}

\section{Introduction}
Einstein's equation: $E = mc^2$

\begin{equation}
\int_{0}^{\infty} e^{-x^2} dx = \frac{\sqrt{\pi}}{2}
\end{equation}

\end{document}
```

**Complex**, but produces **beautiful PDFs** (especially for math).

**Still text** (version-controllable, diff-able).

### EDN (Extensible Data Notation, Clojure)

**For data** (not documents):

```clojure
{:user {:name "Alice"
        :age 30
        :roles #{:admin :developer}}
 :timestamp #inst "2025-10-10T23:00:00Z"
 :config {:theme :dark
          :font-size 14}}
```

**Human-readable Clojure data**.

**Why EDN** (vs JSON):
- **Richer types** (sets, keywords, instants, symbols)
- **Comments** (JSON doesn't allow!)
- **Code is data** (homoiconicity—can eval if needed)

**We use EDN** in our build pipeline (see `scripts/writings_build.clj`).

---

## Configuration as Text: The Unix Way

**Bad** (binary config):
- Windows Registry (binary database, opaque, hard to edit/backup)
- Some apps: SQLite for config (overkill, not human-editable)

**Good** (text config):
- Unix: `/etc/` full of text files (`/etc/hosts`, `/etc/ssh/sshd_config`)
- Apps: `.json`, `.yaml`, `.toml` in `~/.config/`

**Why text config wins**:

### 1. Version Control

```bash
# Track config changes
git init ~/.config/my-app
cd ~/.config/my-app
git add config.yaml
git commit -m "Initial config"

# Change something
vim config.yaml
git diff  # See EXACTLY what changed

git commit -m "Increase font size"

# Broke something? Revert!
git revert HEAD
```

**Can't do this** with binary config (Registry, binary plists).

### 2. Human Editable

```yaml
# config.yaml - I can READ this!
theme: dark
font-size: 14
keybindings:
  save: Ctrl-S
  quit: Ctrl-Q
```

**No special tool needed.** Any editor works.

### 3. Portable

**Text config** (copy file → works on new machine):
```bash
scp ~/.config/app/config.yaml newmachine:~/.config/app/
# Done! Config transferred.
```

**Binary config**: Export, import, hope formats match, pray nothing breaks.

---

## The Preservation Argument

**House of Wisdom scholars** (Essay 9505) preserved Greek knowledge by **copying manuscripts** (text).

**We preserve computational knowledge** through **text files**:

### Example: Source Code

**1970s**: Unix source code (text files, C language)  
**2025**: **Still readable!** (You can compile 1970s Unix today.)

**1980s**: Lisp source code (MIT AI Lab)  
**2025**: **Still readable!** (You can run 1980s Lisp today.)

**Contrast**:

**1990s**: Visual Basic `.frm` files (binary forms)  
**2025**: **Mostly unreadable** (tools gone, formats forgotten).

**2000s**: Flash `.fla` files (proprietary)  
**2025**: **Dead** (Adobe killed Flash, files orphaned).

**Text survives. Binary rots.**

**Plant lens**: **"Text is the seed (preserves genetic information). Binary is the fruit (delicious now, rots quickly)."**

---

## Practical Text Mastery

### Format Recommendations

**For documentation**: **Markdown** (`.md`)
- Simple, readable, convertible
- GitHub renders it automatically
- This entire valley uses it!

**For configuration**: **YAML** or **TOML** or **EDN**
- YAML: Human-friendly (Python, Ruby communities)
- TOML: Simple, explicit (Rust community - `Cargo.toml`)
- EDN: Clojure data (our choice - homoiconic!)

**For data exchange**: **JSON** or **EDN** or **CSV**
- JSON: Universal (every language parses it)
- EDN: Richer (if Clojure is your ecosystem)
- CSV: Simple (spreadsheets, data analysis)

**For academic writing**: **LaTeX** or **Markdown** → PDF
- LaTeX: Maximum control (beautiful math)
- Markdown + Pandoc: Simpler (good enough for most)

**For code**: Whatever your language uses (`.clj`, `.py`, `.rs`, etc.)
- Always text!
- Even "compiled" languages start with text source

---

## Try This

### Exercise 1: Text File Archaeology

**Find old text files** on your computer:
```bash
# Find .txt files modified > 5 years ago
find ~ -name "*.txt" -mtime +1825

# Can you still read them? (Probably yes!)
```

**Now find old Word docs**:
```bash
find ~ -name "*.doc"  # Old Word format

# Can you still open them? (Maybe, with Word or converter)
# Will you be able to in 2050? (Uncertain)
```

**Text wins for longevity.**

---

### Exercise 2: Convert to Text

**Take a binary format** you use (Word doc, Pages, Google Doc).

**Convert to Markdown**:
1. Copy text
2. Paste into plain text editor
3. Add Markdown formatting (# for headings, ** for bold)
4. Save as `.md`

**Now**:
- Version control it (`git add essay.md`)
- Edit with any tool (Vim, VS Code, Notepad)
- Convert to HTML (`pandoc essay.md -o essay.html`)
- Future-proof (still readable in 50 years)

---

### Exercise 3: Explore Markup

**Write the same content** in three formats:

**Plain text**:
```
My Essay Title

This is the introduction.

Section 1
---------
Content here.
```

**Markdown**:
```markdown
# My Essay Title

This is the introduction.

## Section 1
Content here.
```

**HTML**:
```html
<h1>My Essay Title</h1>
<p>This is the introduction.</p>
<h2>Section 1</h2>
<p>Content here.</p>
```

**Observe**: All three are **text** (human-readable). Markdown is the sweet spot (simple + structured).

---

## Going Deeper

### Related Essays
- **[9510: Unix Philosophy](/12025-10/9510-unix-philosophy-do-one-thing-well)** - Text as universal interface
- **[9550: Command Line](/12025-10/9550-command-line-your-primary-interface)** - Tools for text processing
- **[9505: House of Wisdom](/12025-10/9505-house-of-wisdom-knowledge-gardens)** - Preserving knowledge through text
- **[9570: Processes](/12025-10/9570-processes-programs-in-motion)** - What runs when you edit text files

### External Resources
- **"The Art of Unix Programming"** - Eric Raymond on text file philosophy
- **[Markdown Guide](https://www.markdownguide.org/)** - Comprehensive Markdown reference
- **Pandoc** - Universal document converter (Markdown → everything)
- **Plain Text Project** - Advocacy for text-based workflows

---

## Reflection Questions

1. **Why do apps keep reinventing proprietary formats?** (Lock-in? Performance? Ignorance of text?)

2. **Is there ANYTHING that shouldn't be text?** (Images, video, audio—but metadata could be!)

3. **Will UTF-8 still be readable in 2100?** (Probably—it's simple enough to be eternal)

4. **How much of your important data is in proprietary formats?** (Photos? Documents? Should you convert some to open formats?)

5. **What's the oldest text file you've successfully opened?** (Mine: 1990s .txt files, perfectly readable)

---

## Summary

**Text files are**:
- **Human-readable** (open with any editor)
- **Platform-independent** (work everywhere)
- **Tool-agnostic** (any program can process text)
- **Future-proof** (will outlast proprietary formats)
- **Version-controllable** (Git works perfectly with text)
- **Composable** (Unix philosophy in action)

**Key Insights**:
- **Text survives** (50+ year old text files still readable)
- **Binary rots** (proprietary formats become unreadable)
- **Markup adds structure** (Markdown, HTML, LaTeX - still text!)
- **Configuration as text** enables version control, portability
- **Simple formats last** (ASCII, UTF-8 vs complex binary specs)

**Markup Languages**:
- **Markdown**: Documents (simple, readable)
- **HTML**: Web pages (powerful, verbose)
- **LaTeX**: Academic papers (beautiful math)
- **EDN**: Data (Clojure homoiconic format)

**In the Valley**:
- **Everything is text** (essays in Markdown, config in EDN, code in Clojure)
- **Future-proofing** (plain text survives format churn)
- **Git for everything** (version control requires text)
- **Following Unix** (text as universal interface)

**Plant lens**: **"Text is the seed—preserves the genetic code through winters. Binary is the fruit—nourishes now, but doesn't preserve long-term."**

---

**Next**: We'll explore **processes**—programs in motion, the living manifestation of your text source code!

---

**Navigation**:  
← Previous: [9550 (command line your primary interface)](/12025-10/9550-command-line-your-primary-interface) | **Phase 1 Index** | Next: [9570 (processes programs in motion)](/12025-10/9570-processes-programs-in-motion)

**Metadata**:
- **Phase**: 1 (Foundations)
- **Week**: 3
- **Prerequisites**: 9510, 9550
- **Concepts**: Text files, plain text, markup languages, binary vs text, longevity, version control
- **Next Concepts**: Processes, programs, execution, memory, CPU



---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*