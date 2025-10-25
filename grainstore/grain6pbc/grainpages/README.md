# 🌾 grainpages

**Markdown → Clojure → SvelteKit → Static Pages Pipeline**

Automated deployment to GitHub Pages and Codeberg Pages with CI/CD integration.

---

## 🎯 **What Is Grainpages?**

**grainpages** is the Grain Network's canonical library for:
- Transforming Markdown documents into static websites
- Processing #hash cross-references between concepts
- Building with SvelteKit for modern, fast sites
- Deploying automatically to GitHub Pages and Codeberg Pages
- Managing the `pages` branch for Codeberg

### **Used By**

- **#graincourse** - Course publishing system
- **grainstore module READMEs** - Cross-referenced documentation
- **Grain PBC website** - Public-facing content
- **Personal sites** - Template/personal deployment

---

## 🏗️ **Architecture**

### **Pipeline Flow**

```
┌─────────────────────────────────────────────────────────────────┐
│ 1. MARKDOWN (Source)                                            │
│    ├── README.md                                                │
│    ├── LESSON-01.md      ← Contains #hash #cross-refs          │
│    └── DESIGN.md                                                │
└──────────────────────────┬──────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────────┐
│ 2. CLOJURE PROCESSING                                           │
│    ├── Parse markdown (markdown-clj)                            │
│    ├── Extract #hash tags → build cross-reference index        │
│    ├── Transform to SvelteKit-compatible format                │
│    └── Generate navigation based on #references                │
└──────────────────────────┬──────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────────┐
│ 3. SVELTEKIT BUILD                                              │
│    ├── +page.svelte files generated from markdown              │
│    ├── +layout.svelte with cross-reference navigation          │
│    ├── Index page with #concept graph visualization            │
│    └── Static assets (CSS, JS, images)                         │
└──────────────────────────┬──────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────────┐
│ 4. STATIC BUILD                                                 │
│    ├── npm run build → .svelte-kit/output                      │
│    └── Static HTML/CSS/JS ready for deployment                 │
└──────────────────────────┬──────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────────┐
│ 5. DUAL DEPLOYMENT                                              │
│    ├── GitHub Pages (GitHub Actions)                           │
│    │   └── Deploy to: grainpbc.github.io/{repo}/               │
│    └── Codeberg Pages (Woodpecker CI)                          │
│        ├── Create 'pages' branch                               │
│        └── Deploy to: grainpbc.codeberg.page/{repo}/           │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🔗 **Markdown Cross-Reference System**

### **#Hash Tag Syntax**

**In any markdown file:**
```markdown
# Introduction to #graintime

The #graintime system uses #neovedic timestamps based on #holocene-calendar 
and #vedic-nakshatras. It integrates with #grain6 for time-aware process 
supervision.

See also: #graindaemon, #clojure-s6, #urbit-behn

## Solar House System

The #solar-house-clock runs counterclockwise from sunrise (#sunrise-as-1st-house)
to next sunrise, integrated with #vedic-astrology.

For API details, see #astrology-api and #swiss-ephemeris.
```

### **Cross-Reference Processing**

**Clojure parser extracts:**
```clojure
;; grainpages/src/grainpages/crossref.clj

(def cross-references
  {:file "README.md"
   :concepts #{"graintime" "neovedic" "holocene-calendar" "vedic-nakshatras"
               "grain6" "graindaemon" "clojure-s6" "urbit-behn"
               "solar-house-clock" "sunrise-as-1st-house" "vedic-astrology"
               "astrology-api" "swiss-ephemeris"}
   :forward-links #{"graindaemon" "grain6" "clojure-s6"}
   :backlinks-from #{}})  ; Populated during graph build
```

### **Generated Navigation**

**SvelteKit +layout.svelte includes:**
```svelte
<!-- Auto-generated navigation from #hash tags -->
<nav class="crossref-nav">
  <h3>Related Concepts</h3>
  <ul>
    <li><a href="/graintime">#graintime</a></li>
    <li><a href="/grain6">#grain6</a></li>
    <li><a href="/graindaemon">#graindaemon</a></li>
  </ul>
  
  <h3>Referenced By</h3>
  <ul>
    <!-- Backlinks automatically computed -->
    <li><a href="/graincourse">#graincourse</a></li>
    <li><a href="/grainbarrel">#grainbarrel</a></li>
  </ul>
</nav>
```

### **Concept Graph Visualization**

**Index page (D3.js force-directed graph):**
```javascript
// Auto-generated from cross-references
const conceptGraph = {
  nodes: [
    { id: "graintime", category: "library" },
    { id: "grain6", category: "library" },
    { id: "graindaemon", category: "service" },
    { id: "clojure-s6", category: "library" }
  ],
  links: [
    { source: "graintime", target: "grain6" },
    { source: "grain6", target: "graindaemon" },
    { source: "graindaemon", target: "clojure-s6" }
  ]
};
```

---

## 📁 **Template/Personal Separation**

### **Template (Committed to grainpbc/grainpages)**

```
template/
├── README.md                      ← This file
├── github-actions/
│   └── deploy-pages.yml           ← GitHub Actions workflow
├── woodpecker/
│   └── deploy-pages.yml           ← Woodpecker CI workflow
├── sveltekit/
│   ├── svelte.config.js           ← SvelteKit configuration
│   ├── +layout.svelte             ← Default layout with crossref nav
│   ├── +page.svelte               ← Index page template
│   └── styles/
│       ├── global.css             ← Shared styles
│       └── crossref.css           ← Cross-reference navigation styles
└── scripts/
    ├── build-pages.bb             ← Main build script
    ├── parse-markdown.clj         ← Markdown → Clojure AST
    ├── extract-crossrefs.clj      ← Build cross-reference index
    └── generate-svelte.clj        ← Generate SvelteKit files
```

### **Personal (Your Deployed Sites)**

```
personal/
├── .gitignore                     ← Protects secrets
└── kae3g/
    ├── deployed-sites.edn         ← List of your sites
    ├── github-token.edn           ← GitHub PAT (gitignored!)
    ├── codeberg-token.edn         ← Codeberg token (gitignored!)
    └── sites/
        ├── graincourse-{graintime}/  ← Each course
        └── grainstore-docs/          ← Module docs site
```

---

## 🔄 **CI/CD Integration**

### **GitHub Actions Workflow**

**template/github-actions/deploy-pages.yml:**
```yaml
name: Deploy to GitHub Pages

on:
  push:
    branches: [main]
  workflow_dispatch:

permissions:
  contents: read
  pages: write
  id-token: write

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Install Babashka
        run: |
          curl -sLO https://raw.githubusercontent.com/babashka/babashka/master/install
          chmod +x install && sudo ./install
      
      - name: Parse Markdown & Extract Cross-References
        run: bb grainpages:build-crossrefs
      
      - name: Generate SvelteKit Files
        run: bb grainpages:generate-svelte
      
      - name: Setup Node
        uses: actions/setup-node@v3
        with:
          node-version: 18
      
      - name: Install Dependencies
        run: npm ci
      
      - name: Build SvelteKit
        run: npm run build
      
      - name: Upload artifact
        uses: actions/upload-pages-artifact@v2
        with:
          path: 'build'
  
  deploy:
    environment:
      name: github-pages
      url: ${{ steps.deployment.outputs.page_url }}
    runs-on: ubuntu-latest
    needs: build
    steps:
      - name: Deploy to GitHub Pages
        id: deployment
        uses: actions/deploy-pages@v2
```

### **Woodpecker CI Workflow**

**template/woodpecker/deploy-pages.yml:**
```yaml
pipeline:
  build:
    image: node:18-alpine
    commands:
      # Install Babashka
      - apk add --no-cache bash curl
      - curl -sLO https://raw.githubusercontent.com/babashka/babashka/master/install
      - sh install
      
      # Parse Markdown & Extract Cross-References
      - bb grainpages:build-crossrefs
      
      # Generate SvelteKit Files
      - bb grainpages:generate-svelte
      
      # Build SvelteKit
      - npm ci
      - npm run build
  
  deploy-to-pages-branch:
    image: alpine/git
    commands:
      # Create 'pages' branch
      - git checkout --orphan pages
      - git rm -rf .
      
      # Copy built files
      - cp -r build/* .
      
      # Commit and push to pages branch
      - git add .
      - git commit -m "Deploy to Codeberg Pages"
      - git push origin pages --force
    when:
      branch: main
```

---

## 🌾 **Integration with #graincourse**

### **graincourse Uses grainpages**

**Example: graincourse markdown → grainpages pipeline:**

```clojure
;; graincourse/bb.edn
{:paths ["src"]
 :deps {grainpbc/grainpages {:git/url "https://github.com/grainpbc/grainpages"
                             :git/sha "..."}
        grainpbc/graintime {:git/url "https://github.com/grainpbc/graintime"
                            :git/sha "..."}}
 
 :tasks
 {:grainpath
  {:doc "Generate course grainpath"
   :task (shell "bb -m graintime.core grainpath")}
  
  :build-course
  {:doc "Build course using grainpages pipeline"
   :depends [grainpath]
   :task (do
           ;; Parse all lessons
           (shell "bb -m grainpages.parse docs/course/lessons/*.md")
           
           ;; Extract cross-references between lessons
           (shell "bb -m grainpages.crossref docs/course/lessons/")
           
           ;; Generate SvelteKit site
           (shell "bb -m grainpages.generate")
           
           ;; Build static site
           (shell "npm run build"))}
  
  :deploy
  {:doc "Deploy to GitHub + Codeberg"
   :depends [build-course]
   :task (shell "gb flow")}}}
```

---

## 🧪 **Manifest Integration**

### **grainstore.edn Tracks grainpages**

```clojure
{:grainstore
 {:modules
  {:grainpages
   {:repos {:github "https://github.com/grainpbc/grainpages"
            :codeberg "https://codeberg.org/grainpbc/grainpages"}
    :pages {:github "https://grainpbc.github.io/grainpages"
            :codeberg "https://grainpbc.codeberg.page/grainpages"}
    :path "grainstore/grainpages"
    :license "MIT"
    :status :active
    
    ;; Template/Personal Separation
    :separation-pattern true
    :personal-dirs ["personal/kae3g"]
    :template-dirs ["template"]
    :gitignore-rules ["**/personal/*/github-token.edn"
                      "**/personal/*/codeberg-token.edn"
                      "**/personal/*/.env"]
    
    ;; Dependencies
    :depends-on [:graintime :grainneovedic]
    
    ;; CI/CD
    :ci {:github {:workflow "template/github-actions/deploy-pages.yml"}
         :codeberg {:workflow "template/woodpecker/deploy-pages.yml"}}
    
    ;; Features
    :features #{:markdown-parsing
                :cross-reference-extraction
                :sveltekit-generation
                :dual-deployment
                :pages-branch-management}}}}}
```

---

## 📚 **Cross-Reference Examples**

### **Example 1: graintime README**

```markdown
# #graintime

Neovedic timestamp system for Grain Network.

## Features

- #holocene-calendar (12025 CE = 10000 HE + 2025 CE)
- #vedic-nakshatras (27 lunar mansions)
- #solar-house-clock (counterclockwise from sunrise)
- Integration with #grain6 for time-aware supervision

## Dependencies

- #grainneovedic - Core neovedic library
- #astrology-api - Free astronomical data
- #swiss-ephemeris - High-precision ephemeris

## Used By

- #graincourse - Course grainpath generation
- #graindaemon - Service scheduling
- #graindisplay - Sunset/sunrise timing

See also: #urbit-behn (inspiration for #grain6)
```

**grainpages generates:**
- Forward links: grain6, grainneovedic, astrology-api, etc.
- Backlinks: graincourse, graindaemon, graindisplay
- Concept graph showing all relationships

### **Example 2: graincourse LESSON-01**

```markdown
# Lesson 01: Display Warmth on #wayland

Learn to use #graindisplay with #gnome-night-light for bedtime warmth.

## Prerequisites

- #ubuntu-24-04-lts
- #gnome-wayland (not #x11!)
- #framework-laptop (or any Wayland system)

## Key Concepts

- #gsettings for GNOME configuration
- #color-temperature (2000K = warm, 6500K = cool)
- #template-personal-separation for config

## Integration

- #graindaemon manages the service
- #grain6 schedules based on #sunset and #sunrise
- #graintime provides astronomical calculations

Next: [Lesson 02: #neovedic Timestamps](./LESSON-02.md)
```

**grainpages generates:**
- Table of contents with cross-references
- "Prerequisites" section with clickable links
- "Related Lessons" based on shared concepts
- Dependency graph showing lesson order

---

## 🛠️ **Implementation**

### **Core Modules**

**1. Markdown Parser**
```clojure
;; grainpages/src/grainpages/parse.clj
(ns grainpages.parse
  (:require [markdown.core :as md]))

(defn parse-markdown [file-path]
  {:file file-path
   :ast (md/md-to-html-string-with-meta (slurp file-path))
   :raw (slurp file-path)})
```

**2. Cross-Reference Extractor**
```clojure
;; grainpages/src/grainpages/crossref.clj
(ns grainpages.crossref
  (:require [clojure.string :as str]))

(defn extract-hash-tags [markdown-text]
  "Extract all #hash-tags from markdown"
  (->> (re-seq #"#([a-z0-9-]+)" markdown-text)
       (map second)
       set))

(defn build-crossref-index [parsed-files]
  "Build bidirectional cross-reference index"
  (let [forward-refs (for [f parsed-files]
                       {:file (:file f)
                        :refs (extract-hash-tags (:raw f))})
        backlinks (group-by :ref
                            (for [f forward-refs
                                  r (:refs f)]
                              {:ref r :from (:file f)}))]
    {:forward forward-refs
     :backlinks backlinks}))
```

**3. SvelteKit Generator**
```clojure
;; grainpages/src/grainpages/generate.clj
(ns grainpages.generate
  (:require [grainpages.crossref :as crossref]
            [clojure.java.io :as io]))

(defn generate-svelte-page [parsed-file crossref-index]
  (let [refs (crossref/extract-hash-tags (:raw parsed-file))
        backlinks (get-in crossref-index [:backlinks (:file parsed-file)])]
    (spit (str "src/routes/" (file-to-route (:file parsed-file)) "/+page.svelte")
          (svelte-template (:ast parsed-file) refs backlinks))))
```

---

## 📖 **Usage**

### **Basic Usage (graincourse)**

```bash
# 1. Write your markdown with #cross-references
cd docs/course/lessons
nano LESSON-01-wayland-display.md

# 2. Build using grainpages
bb grainpages:build-crossrefs
bb grainpages:generate-svelte
npm run build

# 3. Deploy
gb flow  # Automatically pushes to GitHub main + Codeberg main + Codeberg pages
```

### **Standalone Site**

```bash
# 1. Create new site
mkdir my-site && cd my-site

# 2. Copy template
cp -r ../grainstore/grainpages/template/* .

# 3. Write markdown
mkdir content
echo '# Welcome to #my-site' > content/index.md

# 4. Build
bb grainpages:build
```

---

## 🔍 **Testing**

### **Test Cross-Reference Extraction**

```bash
bb test-crossref
# Parses all markdown in docs/
# Outputs cross-reference graph
# Validates no broken links
```

### **Test SvelteKit Generation**

```bash
bb test-generate
# Generates SvelteKit files
# Runs dev server: http://localhost:5173
# Manual inspection of cross-reference navigation
```

---

## 📚 **See Also**

- [#grainsource-separation](../grainsource-separation/README.md) - Template/personal pattern
- [#graincourse](../graincourse/README.md) - Course publishing (uses grainpages)
- [#grainbarrel](../grainbarrel/README.md) - Build system (`gb flow`)

---

**Version:** 1.0.0  
**Status:** 🌾 Active Development  
**Author:** kae3g (Grain PBC)  
**License:** MIT

🌾 **From markdown to web, with #cross-references connecting THE WHOLE GRAIN!**
