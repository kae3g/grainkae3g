# Grain Markdown Indexing & Interlinking Strategy

**Date:** January 22, 2025  
**Status:** 🎯 Strategy Document  
**Goal:** Create a superior documentation and learning experience through intelligent indexing

---

## 🎯 **Vision**

Transform our documentation from isolated files into a **living knowledge graph** where every concept, API, tutorial, and design pattern is discoverable through multiple pathways.

**Inspiration:**
- Urbit's `%docs` desk with bidirectional links
- Obsidian's graph view
- Rust's `rustdoc` with automatic cross-references
- MDN Web Docs' comprehensive interlinking

---

## 📋 **Core Principles**

### **1. Every Document is an Entry Point**

No matter where a user lands, they should be able to:
- Understand the context (breadcrumbs)
- Find related topics (see also)
- Navigate to parent/child concepts
- Search by tags/categories

### **2. Multiple Navigation Paths**

Users think differently, so provide multiple ways to find information:
- **Hierarchical**: Traditional folder structure
- **Topical**: Tag-based grouping (#clojure, #icp, #hardware)
- **Contextual**: "Related to this" sections
- **Sequential**: "Next steps" / "Prerequisites"
- **Semantic**: Concept-based linking

### **3. Bidirectional Links**

When Document A references Document B, Document B should automatically show "Referenced by Document A"

### **4. Living Indexes**

Auto-generated indexes that update as documentation grows:
- API index by namespace
- Tutorial index by difficulty
- Concept index by topic
- Changelog index by date

---

## 🏗️ **Architecture**

### **Markdown Conventions**

#### **Frontmatter (YAML)**

Every markdown file starts with structured metadata:

```markdown
---
title: "Clojure to Motoko Transpiler"
id: clotoko-overview
category: transpilers
tags: [clojure, motoko, icp, compiler, web3]
topics: [urbit, hoon, functional-programming]
difficulty: intermediate
status: active
version: 0.1.0
related:
  - clojure-sixos
  - grain-metatypes
  - clojure-icp
prerequisites:
  - clojure-basics
  - icp-fundamentals
next-steps:
  - clotoko-syntax
  - motoko-basics
api-docs: /docs/api/clotoko
source: /grainstore/clotoko
author: kae3g
updated: 2025-01-22
---
```

#### **In-Page Anchors**

Use semantic anchors for deep linking:

```markdown
## Installation {#installation}

### Via Homebrew {#installation-homebrew}

### Via Nix {#installation-nix}
```

Link to them:
```markdown
See [Installation via Nix](#installation-nix) for details.
See [Clotoko Installation](clotoko-overview.md#installation) for setup.
```

#### **Cross-Document Links**

**Explicit Links** (preferred):
```markdown
See the [Clotoko Overview](../transpilers/clotoko-overview.md) for details.
```

**Wikilink Style** (for convenience):
```markdown
See [[clotoko-overview]] for details.
```

**Tag Links**:
```markdown
All #clojure related docs
All #hardware projects
```

#### **Backlinks Section**

Auto-generated at the bottom of each document:

```markdown
---

## 📚 Referenced By

- [Grainmusic Architecture](../grainmusic/architecture.md)
- [ICP Integration Guide](../guides/icp-integration.md)
- [Course Lesson 6](../course/lesson-06.md)

*This section is auto-generated*
```

#### **Related Topics Section**

Always include at the end:

```markdown
## 🔗 Related Topics

### Prerequisites
- [[clojure-basics]] - Understanding Clojure fundamentals
- [[icp-fundamentals]] - Internet Computer basics

### Related Concepts
- [[clojure-sixos]] - SixOS integration with typo handling
- [[grain-metatypes]] - Type system fundamentals

### Next Steps
- [[clotoko-syntax]] - Learn Clotoko syntax mapping
- [[motoko-basics]] - Dive deeper into Motoko

### API Reference
- [Clotoko API Docs](/docs/api/clotoko)
```

---

## 📊 **Index Types**

### **1. Master Index** (`docs/INDEX.md`)

The top-level directory of all documentation:

```markdown
# Grain Network Documentation Index

## 📚 By Category

### Getting Started
- [Quick Start](setup/QUICK-START-GRAINPBC.md)
- [Installation Guide](setup/installation.md)
- [First Steps](guides/first-steps.md)

### Core Libraries #clojure
- [clojure-sixos](../grainstore/clojure-sixos/README.md) - Typo handling
- [clojure-s6](../grainstore/clojure-s6/README.md) - s6 supervision
- [clotoko](../grainstore/clotoko/README.md) - Clojure→Motoko transpiler

### Platform Components #platform
- [Grainweb](../grainstore/grainweb/GRAINWEB-DESIGN.md) - Browser + Git + AI
- [Grainmusic](../grainstore/grainmusic/README.md) - Music streaming

### Hardware Projects #hardware
- [Grainwriter](../grainstore/grainwriter/GRAINWRITER-DESIGN.md) - E-ink device
- [Graincamera](../grainstore/graincamera/README.md) - AVIF camera

## 🏷️ By Tag

### #clojure
[clojure-sixos](#), [clotoko](#), [grainweb](#)...

### #icp #web3
[clotoko](#), [clojure-icp](#), [grainmusic](#)...

### #urbit #hoon
[grain-metatypes](#), [Clay filesystem](#)...

## 📈 By Difficulty

### Beginner
- Quick Start Guide
- Basic Installation

### Intermediate
- Clotoko Transpiler
- ICP Integration

### Advanced
- Custom Type Systems
- Cross-Chain Integration
```

### **2. API Index** (`docs/api/INDEX.md`)

Auto-generated from code:

```markdown
# Grain Network API Index

## Core Namespaces

### clojure-sixos
- [clojure-sixos.core](clojure-sixos/core.md)
- [clojure-sixos.typo](clojure-sixos/typo.md)
- [clojure-sixos.similarity](clojure-sixos/similarity.md)

### clotoko
- [clotoko.core](clotoko/core.md)
- [clotoko.transpiler](clotoko/transpiler.md)
- [clotoko.sur.mark](clotoko/sur/mark.md)

## By Function

### Type System
- [clotoko.sur.core](clotoko/sur/core.md#specs)
- [grain-metatypes](grain-metatypes/index.md)

### Networking
- [grainweb.daemon.peer-discovery](grainweb/daemon/peer-discovery.md)
- [grainclay.networking](grainclay/networking.md)
```

### **3. Tutorial Index** (`docs/course/INDEX.md`)

Sequential learning paths:

```markdown
# Grain Network Learning Paths

## 🎓 Beginner Track

1. [Welcome to Grain Network](lesson-01-welcome.md)
2. [Setting Up Your Environment](lesson-02-setup.md)
3. [Your First Grain App](lesson-03-first-app.md)

## 🚀 Intermediate Track

1. [Understanding Type Systems](lesson-06-type-systems.md)
2. [ICP Integration](lesson-07-icp.md)
3. [Building with Clotoko](lesson-08-clotoko.md)

## 🔬 Advanced Track

1. [Custom Transpilers](lesson-12-transpilers.md)
2. [Cross-Chain Integration](lesson-13-cross-chain.md)
3. [Hardware Integration](lesson-14-hardware.md)

## By Topic

### #music-streaming
- [Grainmusic Architecture](../grainmusic/architecture.md)
- [Audius Integration](../grainmusic/audius-integration.md)

### #hardware
- [Grainwriter Design](lessons/grainwriter-design.md)
- [E-ink Display Programming](lessons/eink-programming.md)
```

### **4. Tag Index** (`docs/tags/INDEX.md`)

Auto-generated tag cloud and listings:

```markdown
# Tag Index

## All Tags (Alphabetical)

### #audius
- [Grainmusic Audius Integration](../grainmusic/audius-integration.md)
- [Solana Music Protocol](../guides/solana-music.md)

### #clojure
- [clojure-sixos](../../grainstore/clojure-sixos/README.md)
- [clotoko](../../grainstore/clotoko/README.md)
- 12 more documents...

### #hardware
- [Grainwriter Design](../../grainstore/grainwriter/GRAINWRITER-DESIGN.md)
- [Graincamera Specs](../../grainstore/graincamera/specs.md)
- 8 more documents...

### #icp #dfinity
- [clojure-icp](../../grainstore/clojure-icp/README.md)
- [ICP Quick Start](../guides/ICP-QUICK-START.md)
- 15 more documents...

### #urbit #hoon
- [Mark System](../architecture/mark-system.md)
- [Clay Filesystem](../architecture/clay-filesystem.md)
- 7 more documents...

## Tag Cloud (By Popularity)

#clojure (45) • #icp (32) • #web3 (28) • #hardware (18) • #urbit (15) • #hoon (15) • #music (12) • #audius (8)
```

### **5. Concept Graph** (`docs/concepts/INDEX.md`)

Concept-based navigation:

```markdown
# Concept Index

## Type Systems
- [Overview](type-systems-overview.md)
  - [Clojure Spec](clojure-spec.md)
  - [Haskell Types](haskell-types.md)
  - [Mark System](mark-system.md) #urbit
    - [Clay Filesystem](clay-filesystem.md)
    - [Grainframes](grainframes.md)

## Transpilation
- [Overview](transpilation-overview.md)
  - [Clotoko](clotoko-transpiler.md)
    - [Syntax Mapping](clotoko-syntax.md)
    - [Type Inference](clotoko-types.md)
  - [ClojureScript](clojurescript.md)

## Decentralization
- [Overview](decentralization-overview.md)
  - [ICP](icp-overview.md) #icp
  - [Solana](solana-overview.md) #solana
  - [Urbit](urbit-overview.md) #urbit
    - [Azimuth](azimuth.md)
    - [Nock](nock.md)
```

---

## 🛠️ **Implementation Tools**

### **1. Babashka Script for Index Generation**

`scripts/generate-indexes.bb`:

```clojure
#!/usr/bin/env bb

(ns generate-indexes
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.edn :as edn]))

(defn extract-frontmatter [file-path]
  "Extract YAML frontmatter from markdown file"
  (let [content (slurp file-path)
        frontmatter-match (re-find #"(?s)^---\n(.*?)\n---" content)]
    (when frontmatter-match
      (yaml/parse-string (second frontmatter-match)))))

(defn find-all-markdown-files [root-dir]
  "Find all .md files recursively"
  (->> (file-seq (io/file root-dir))
       (filter #(.isFile %))
       (filter #(str/ends-with? (.getName %) ".md"))
       (map #(.getPath %))))

(defn build-tag-index [files]
  "Build index grouped by tags"
  (reduce (fn [acc file]
            (let [metadata (extract-frontmatter file)
                  tags (:tags metadata)]
              (reduce (fn [acc tag]
                        (update acc tag (fnil conj []) file))
                      acc tags)))
          {} files))

(defn build-category-index [files]
  "Build index grouped by category"
  (group-by #(:category (extract-frontmatter %)) files))

(defn generate-backlinks [files]
  "Find all documents that link to each document"
  ;; Parse all markdown, extract links, build reverse index
  )

(defn write-master-index [data output-path]
  "Write the master INDEX.md"
  (spit output-path (generate-master-index-markdown data)))

(defn -main []
  (let [files (find-all-markdown-files "docs")
        tag-index (build-tag-index files)
        category-index (build-category-index files)]
    (write-master-index {:tag-index tag-index
                         :category-index category-index}
                        "docs/INDEX.md")
    (println "✅ Indexes generated!")))

(-main)
```

### **2. GitHub Actions for Auto-Indexing**

`.github/workflows/generate-docs-index.yml`:

```yaml
name: Generate Documentation Indexes

on:
  push:
    paths:
      - 'docs/**/*.md'
      - 'grainstore/**/README.md'
  workflow_dispatch:

jobs:
  generate-indexes:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      
      - name: Install Babashka
        run: |
          curl -sL https://raw.githubusercontent.com/babashka/babashka/master/install | bash
      
      - name: Generate Indexes
        run: bb scripts/generate-indexes.bb
      
      - name: Generate Tag Indexes
        run: bb scripts/generate-tag-indexes.bb
      
      - name: Generate Backlinks
        run: bb scripts/generate-backlinks.bb
      
      - name: Commit Changes
        run: |
          git config user.name "GrainCI"
          git config user.email "ci@grain.network"
          git add docs/INDEX.md docs/tags/
          git commit -m "🤖 Auto-update documentation indexes" || true
          git push
```

### **3. Search Integration**

Use [Algolia DocSearch](https://docsearch.algolia.com/) or build custom search with tags:

```markdown
## 🔍 Search Tips

- Use tags: `#clojure #icp` to find all Clojure + ICP docs
- Search concepts: "type system", "transpiler", "hardware"
- Filter by difficulty: difficulty:intermediate
- Filter by status: status:active
```

---

## 📝 **Documentation Standards**

### **Required Sections** (Every Document)

1. **Title & Metadata** (frontmatter)
2. **Overview** (1-2 paragraphs)
3. **Table of Contents** (auto-generated)
4. **Main Content**
5. **Examples** (code samples)
6. **Related Topics** (cross-links)
7. **References** (backlinks, auto-generated)

### **Optional But Recommended**

- **Prerequisites** (what to read first)
- **Next Steps** (what to read next)
- **TL;DR** (quick summary at top)
- **FAQ** (common questions)
- **Troubleshooting** (common issues)

### **Visual Indicators**

Use emojis consistently:
- 🎯 Goals/Objectives
- 📋 Lists/Checklists
- 🔧 Configuration/Setup
- 💡 Tips/Notes
- ⚠️ Warnings/Caveats
- 🚀 Quick Start
- 📚 References
- 🔗 Links/Related
- ✅ Completed/Success
- ❌ Errors/Failures
- 🌾 Grain Network specific

---

## 🔄 **Maintenance Workflow**

### **When Creating New Documentation**

1. **Add Frontmatter** with all metadata
2. **Add Cross-Links** to related docs
3. **Run Index Generator**: `bb scripts/generate-indexes.bb`
4. **Verify Links**: `bb scripts/check-links.bb`
5. **Commit Changes**

### **Weekly Maintenance**

1. **Review Orphaned Docs** (no backlinks)
2. **Update Popular Paths** (based on analytics)
3. **Add Missing Tags**
4. **Update Difficulty Ratings**

### **Monthly Audit**

1. **Check for Broken Links**
2. **Update Outdated Content**
3. **Merge Duplicate Topics**
4. **Improve Low-Quality Pages**

---

## 🎯 **Success Metrics**

### **Quantitative**

- **Link Density**: Avg links per doc (target: 8-12)
- **Orphan Rate**: % docs with no backlinks (target: <5%)
- **Tag Coverage**: % docs with tags (target: 100%)
- **Search Success Rate**: % searches finding target (target: >85%)

### **Qualitative**

- **User Feedback**: "Easy to navigate"
- **Learning Curve**: Time to productivity
- **Discovery**: Can users find answers without asking?

---

## 🚀 **Phased Rollout**

### **Phase 1: Foundation** (Week 1)
- ✅ Add frontmatter to all existing docs
- ✅ Create master INDEX.md
- ✅ Set up tag system

### **Phase 2: Automation** (Week 2)
- ✅ Build index generation scripts
- ✅ Set up GitHub Actions
- ✅ Auto-generate backlinks

### **Phase 3: Enhancement** (Week 3)
- ✅ Add concept graph
- ✅ Build tutorial paths
- ✅ Create API index

### **Phase 4: Polish** (Week 4)
- ✅ Add search functionality
- ✅ Create interactive graph visualization
- ✅ Mobile-optimize navigation

---

## 📖 **Examples**

### **Before: Isolated Document**

```markdown
# Clotoko

Clotoko is a transpiler.

## Installation

npm install clotoko

## Usage

clotoko input.clj
```

### **After: Connected Document**

```markdown
---
title: "Clotoko - Clojure to Motoko Transpiler"
id: clotoko-overview
category: transpilers
tags: [clojure, motoko, icp, compiler, web3]
topics: [urbit, hoon, functional-programming]
difficulty: intermediate
related: [clojure-sixos, grain-metatypes, clojure-icp]
prerequisites: [clojure-basics, icp-fundamentals]
---

# Clotoko - Clojure to Motoko Transpiler

> **TL;DR**: Transform Clojure code into Motoko for ICP canisters with `bb clotoko input.clj`

**Clotoko** transpiles Clojure to Motoko, enabling functional programming on the Internet Computer. Inspired by [[urbit]] and [[hoon]] design patterns.

## 📋 Table of Contents

- [Why Clotoko?](#why)
- [Installation](#installation)
- [Quick Start](#quick-start)
- [Core Concepts](#concepts)

## 🎯 Why Clotoko? {#why}

See [[transpilation-overview]] for general context.

## 🔧 Installation {#installation}

Prerequisites: [[clojure-basics]], [[icp-fundamentals]]

### Via Homebrew {#installation-homebrew}

### Via Nix {#installation-nix}

## 🚀 Quick Start {#quick-start}

See [[clotoko-tutorial]] for full walkthrough.

## 🔗 Related Topics

### Prerequisites
- [[clojure-basics]] - Clojure fundamentals
- [[icp-fundamentals]] - Internet Computer basics
- [[motoko-intro]] - Motoko language intro

### Related Tools
- [[clojure-sixos]] - Typo handling for Grain
- [[clojure-icp]] - ICP integration
- [[grain-metatypes]] - Type definitions

### Next Steps
- [[clotoko-syntax]] - Syntax mapping guide
- [[clotoko-types]] - Type system details
- [[building-icp-canisters]] - Deploy to ICP

### See Also
- [Clotoko API Docs](/docs/api/clotoko)
- [GitHub Repository](https://github.com/grainpbc/clotoko)

---

## 📚 Referenced By

*Auto-generated backlinks*

- [Grainmusic Architecture](../grainmusic/architecture.md)
- [ICP Integration Guide](../guides/icp-integration.md)
- [Course Lesson 8](../course/lesson-08.md)

---

**Tags**: #clojure #motoko #icp #transpiler #web3 #urbit #hoon  
**Last Updated**: 2025-01-22 by @kae3g
```

---

## 🌐 **Integration with Grainweb**

Eventually, this index system becomes **queryable** through Grainweb:

```clojure
;; Search API
(grainweb/search {:tags ["clojure" "icp"]
                  :difficulty "intermediate"
                  :status "active"})

;; Graph traversal
(grainweb/related-docs "clotoko-overview" 
                       {:depth 2 
                        :filter #(contains? (:tags %) "urbit")})

;; Learning paths
(grainweb/learning-path {:from "beginner"
                         :goal "icp-canister-dev"
                         :topics ["clojure" "motoko"]})
```

---

**This strategy transforms our docs from static files into a living, interconnected knowledge base!** 🌾

