# Grain Network Complete Course - Graincard10 Multi-Page System

**Sessions 811-812 | 80×110 Format | 0000-9999 Page Numbering**

## 🌾 Navigation System

### Three Ways to Access Pages

#### 1. Direct File Access
```
pages/0001-88-counter-philosophy.html
```

#### 2. Client-Side Router (GitHub Pages Compatible!)
```
index.html?page=0001
index.html?page=88-counter
index.html#0001
```

#### 3. Colloquial Title Lookup
```
index.html?page=philosophy
index.html?page=motoko
index.html?page=alpine-intro
```

### How It Works

**GitHub Pages is static-only** (no server-side routing), so we use **client-side JavaScript**:

1. `index.html` acts as router
2. Reads `?page=` parameter or `#hash`
3. Looks up in `pageIndex` JavaScript object
4. Redirects to actual file: `pages/NNNN-full-title.html`

**This means**:
- ✅ Works on GitHub Pages (pure static)
- ✅ Works on Codeberg Pages (pure static)
- ✅ No server required
- ✅ Fast redirects (<50ms)
- ✅ Bookmarkable URLs
- ✅ Works offline (after first load)

### Markdown Link Format

In markdown files, you can write:

```markdown
See [page 0001](../?page=0001) for the 88 counter philosophy.

Or use colloquial titles:
See [the philosophy](../?page=philosophy) for details.

Or direct links:
See [this page](../pages/0001-88-counter-philosophy.html).
```

All three work! The router handles resolution.

## 📂 File Structure

```
grain-network-complete-course/
├── index.html                 # Client-side router
├── data/
│   └── page-index.edn        # Clojure keymap (page numbers → files → titles)
├── src/
│   └── graincard10/
│       └── navigation.clj    # Server-side navigation logic (for builds)
├── pages/
│   ├── 0000-title.html       # Page 0 (title page)
│   ├── 0001-88-counter-philosophy.html
│   ├── 0002-temporal-recursion.html
│   ├── ...
│   └── 0099-index.html       # Full index page
├── styles/
│   └── graincard10.css       # Shared styles
└── svelte/
    └── navigation.js         # Svelte-compiled navigation component
```

## 🔧 Clojure Keymap System

The `page-index.edn` provides two-way mapping:

```clojure
{:page-index
 {0001 {:title "The 88 Counter Philosophy"
        :colloquial ["88 counter" "philosophy" "math foundation"]
        :filepath "pages/0001-88-counter-philosophy.html"}}
 
 :title-to-page
 {"The 88 Counter Philosophy" 1
  "philosophy" 1
  "88 counter" 1}}
```

This enables:
- **Number → File**: `0001` → `pages/0001-88-counter-philosophy.html`
- **Title → Number**: `"The 88 Counter Philosophy"` → `1`
- **Colloquial → Number**: `"philosophy"` → `1`

## 🌊 Page Numbering System

### Range Allocation

- **0000-0099**: Core course content (100 pages)
- **0100-0199**: Exercises and labs (100 pages)
- **0200-0299**: Appendices and references (100 pages)
- **0300-0399**: Advanced topics (100 pages)
- **0400-0999**: Reserved for future expansion (600 pages)
- **1000-9999**: Landscape-oriented pages (9,000 pages)

### Benefits of 0000-9999

1. **Massive Capacity**: 10,000 pages × 8,800 chars = 88 million characters!
2. **Fixed Width**: All page numbers are 4 digits (easy parsing)
3. **Sortable**: Lexicographic order = numeric order
4. **Clear Hierarchy**: Ranges group related content
5. **Future-Proof**: Room for unlimited expansion

## 🎯 Why This System?

### Problem: GitHub Pages Limitations

GitHub Pages doesn't support:
- ❌ Server-side routing (no `.htaccess`, no redirects)
- ❌ Dynamic URL rewriting
- ❌ Backend code execution

### Solution: Client-Side Router + Full Filenames

We use:
- ✅ Full descriptive filenames (`0001-88-counter-philosophy.html`)
- ✅ Client-side JavaScript router in `index.html`
- ✅ Clojure keymap for title-to-number lookup
- ✅ Sequential numbering for easy navigation

### Best of Both Worlds

- **Descriptive filenames**: SEO-friendly, readable in file browser
- **Short references**: `?page=0001` or `?page=philosophy`
- **No server needed**: Pure static files work on any hosting
- **Markdown-friendly**: Links work with standard markdown syntax

## 📖 Usage Examples

### In Markdown

```markdown
For details on the 88 counter philosophy, see:
- [Page 0001](index.html?page=0001) (short reference)
- [The philosophy page](index.html?page=philosophy) (colloquial)
- [Direct link](pages/0001-88-counter-philosophy.html) (full path)
```

### In Clojure (for build-time generation)

```clojure
(require '[graincard10.navigation :as nav])

;; Find page by title
(nav/find-page-by-title "88 counter")  ; => 1

;; Get filepath
(nav/get-page-path 1)  ; => "pages/0001-88-counter-philosophy.html"

;; Generate nav links
(nav/generate-nav-links 1)
; => {:prev {...} :next {...} :current {...}}
```

## 🌾 The 88 Counter Philosophy in Page Numbers

Even the page numbering embodies `88 × 10^n`:

```
Pages 0000-0099: 100 pages = 88 + 12 (close to 88 × 10⁰)
Pages 0000-0999: 1,000 pages ≈ 88 × 10¹ (880 theoretical)
Pages 0000-9999: 10,000 pages ≈ 88 × 10² (8,800 theoretical)

The system can scale to 88 × 10^n pages by adding digits:
  00000-99999: 100,000 pages
  000000-999999: 1,000,000 pages
  ...and so on infinitely
```

## 🚀 Deployment

All pages are static HTML, so deployment is simple:

```bash
# From course directory
gb flow "Updated graincard10 multi-page course"

# Pages available at:
# https://kae3g.github.io/grainkae3g/[grainpath]/index.html?page=0001
# https://kae3g.codeberg.page/grainkae3g/[grainpath]/index.html?page=philosophy
```

---

**Built with 🌾 for infinite knowledge, finite pages**

*"From numbered pages to navigable wisdom"*

**88 × 10^n >= 0 | -1** | **now == next + 1**
