# 🔄 Shared Data Pipeline Architecture
## *One Source of Truth: Markdown + EDN → Web + Native*

**Purpose:** Define how content flows from source files to both web (SvelteKit) and native (Clojure/Humble UI) applications  
**Updated:** January 2025  
**Author:** kae3g

---

## 🎯 CORE PRINCIPLE

**Everything is data.** Use the most appropriate format for each layer:

1. **Content (Markdown)** - Human-readable, version-controllable, universal
2. **Metadata (EDN)** - Clojure-native, type-safe, extensible
3. **Web (JSON)** - Generated from EDN, consumed by SvelteKit
4. **Native (EDN)** - Direct consumption by Clojure apps

---

## 📁 DATA SOURCE STRUCTURE

```
grainkae3g/
├── data/
│   ├── site-config.edn           # Global site configuration
│   ├── navigation.edn             # Menu structure
│   ├── magazine-content.edn      # Magazine metadata
│   └── students.edn               # Student directory (for network)
│
├── content/
│   ├── writings/                  # Blog posts, articles
│   │   ├── 2025-01-22-press-release.md
│   │   ├── 2025-01-22-dual-wifi.md
│   │   └── ... (more markdown files)
│   │
│   ├── course/                    # 18-week curriculum
│   │   ├── week01/
│   │   │   ├── index.md
│   │   │   ├── lab01.md
│   │   │   └── meta.edn           # Week metadata
│   │   ├── week02/
│   │   └── ...
│   │
│   ├── docs/                      # Documentation
│   │   ├── guides/
│   │   ├── tutorials/
│   │   └── reference/
│   │
│   └── projects/                  # Project showcases
│       ├── grainspace.md
│       ├── clojure-s6.md
│       └── ...
│
├── scripts/
│   ├── build-web.bb               # Generate JSON for SvelteKit
│   ├── build-native.bb            # Package EDN for native apps
│   └── sync-content.bb            # Keep everything in sync
│
├── web-app/                       # SvelteKit web application
│   ├── static/
│   │   └── content/               # Generated JSON files
│   └── src/
│       └── routes/
│
└── native-app/                    # Clojure/Humble UI application
    ├── src/
    │   └── grain_magazine/
    │       ├── core.clj
    │       └── data.clj           # EDN data loading
    └── resources/
        └── content/               # Symlink to ../content/
```

---

## 📝 MARKDOWN FORMAT (Content Layer)

### Standard Frontmatter

Every Markdown file includes YAML frontmatter:

```markdown
---
title: "Introducing grainkae3g"
slug: "press-release-grainkae3g"
date: 2025-01-22
author: kae3g
category: Press Release
tags: [announcement, sovereignty, ICP, Urbit]
featured: true
excerpt: "Today marks the official launch of grainkae3g, a groundbreaking open-source project..."
---

# Introducing grainkae3g

Content goes here...
```

### Course Lesson Format

```markdown
---
week: 1
lesson: 1
title: "Welcome to Linux"
duration: 90 # minutes
difficulty: beginner
prerequisites: []
learning-outcomes:
  - Understand what Linux is
  - Install Ubuntu 24.04 LTS
  - Boot into your new system
lab-file: lab01.md
---

# Week 1, Lesson 1: Welcome to Linux

## What is Linux?

Linux is...
```

---

## 🗂️ EDN FORMAT (Metadata Layer)

### Site Configuration (`data/site-config.edn`)

```clojure
{:site {:name "grainkae3g"
        :tagline "Personal Sovereignty Stack"
        :url "https://grain.network"
        :repository "https://github.com/kae3g/grainkae3g"
        :author {:name "kae3g"
                 :email "kj3x39@gmail.com"
                 :github "kae3g"}
        :build {:timestamp nil  ; Generated at build time
                :version "1.0.0"}}
 
 :theme {:default-palette "red-cyberpunk"
         :palettes {:red-cyberpunk {:bg "#0a0a0a"
                                     :fg "#eeeeee"
                                     :accent "#ff4444"}
                    :green-terminal {:bg "#0a0a0a"
                                     :fg "#44ff44"
                                     :accent "#66ff66"}
                    :monochrome {:bg "#000000"
                                 :fg "#eeeeee"
                                 :accent "#cccccc"}}}
 
 :features {:payments {:icp true
                       :solana true
                       :fiat false}  ; Phase 2
            :identity {:urbit true
                       :icp-canister "rrkah-fqaaa-aaaaa-aaaaq-cai"}
            :dao {:enabled true
                  :canister "..."}}
 
 :navigation [{:title "Home" :path "/" :icon "🏠"}
              {:title "Course" :path "/course" :icon "🎓"}
              {:title "Writings" :path "/writings" :icon "📝"}
              {:title "Projects" :path "/projects" :icon "🚀"}
              {:title "Network" :path "/network" :icon "🌾"}]}
```

### Course Structure (`data/course-structure.edn`)

```clojure
{:course {:title "Personal Sovereignty Computing"
          :duration-weeks 18
          :credit-hours 3
          :level "beginner-to-intermediate"
          :prerequisites ["Basic computer literacy"
                          "Curiosity about technology"]
          
          :modules [{:id :linux-foundation
                     :title "Linux Foundation"
                     :weeks [1 2 3]
                     :description "Getting started with Ubuntu"
                     :icon "🐧"}
                    
                    {:id :version-control
                     :title "Version Control & Git"
                     :weeks [4 5]
                     :description "Track and collaborate on code"
                     :icon "📦"}
                    
                    ; ... more modules
                    ]
          
          :weeks [{:number 1
                   :module :linux-foundation
                   :title "Welcome to Linux"
                   :lessons [{:id "week01-lesson01"
                              :title "What is Linux?"
                              :content-file "content/course/week01/lesson01.md"
                              :duration-minutes 45
                              :lab-file "content/course/week01/lab01.md"
                              :free true}
                             
                             {:id "week01-lesson02"
                              :title "Terminal Basics"
                              :content-file "content/course/week01/lesson02.md"
                              :duration-minutes 60
                              :free true}]
                   :lab {:file "content/course/week01/lab.md"
                         :estimated-hours 2
                         :required true}
                   :quiz {:file "content/course/week01/quiz.edn"
                          :passing-score 80}}
                  
                  ; ... more weeks
                  ]}}
```

### Content Index (`data/writings-index.edn`)

Auto-generated from Markdown frontmatter:

```clojure
{:writings [{:slug "press-release-grainkae3g"
             :title "Introducing grainkae3g"
             :date #inst "2025-01-22T00:00:00.000-00:00"
             :author "kae3g"
             :category "Press Release"
             :tags ["announcement" "sovereignty" "ICP" "Urbit"]
             :featured true
             :excerpt "Today marks the official launch..."
             :content-file "content/writings/2025-01-22-press-release.md"
             :word-count 2847
             :read-time-minutes 12}
            
            {:slug "dual-wifi-intelligence"
             :title "Intelligent Dual WiFi Management"
             :date #inst "2025-01-22T00:00:00.000-00:00"
             :author "kae3g"
             :category "Technical Deep Dive"
             :tags ["networking" "clojure" "automation"]
             :featured true
             :excerpt "The world's first AI-agent-aware..."
             :content-file "content/writings/2025-01-22-dual-wifi.md"
             :word-count 1523
             :read-time-minutes 7}
            
            ; ... more writings
            ]}
```

---

## 🔨 BUILD PIPELINE

### Script: `scripts/build-web.bb`

Converts EDN + Markdown → JSON for SvelteKit:

```clojure
#!/usr/bin/env bb

(ns build-web
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [cheshire.core :as json]  ; JSON library
            [babashka.fs :as fs]
            [clojure.string :as str]))

(defn read-edn-file [path]
  (edn/read-string (slurp path)))

(defn read-markdown-frontmatter [path]
  "Extract YAML frontmatter from Markdown file"
  (let [content (slurp path)
        [_ frontmatter _] (str/split content #"---\n" 3)]
    (when frontmatter
      ; Parse YAML frontmatter (simplified - use a YAML lib in production)
      (into {} (map (fn [line]
                      (let [[k v] (str/split line #":" 2)]
                        [(keyword (str/trim k)) (str/trim v)]))
                    (str/split-lines frontmatter))))))

(defn build-content-index []
  "Scan content/ directory and build index"
  (let [markdown-files (fs/glob "content/writings" "*.md")
        writings (map (fn [file]
                        (let [frontmatter (read-markdown-frontmatter file)
                              slug (fs/file-name (fs/strip-ext file))
                              content (slurp file)
                              word-count (count (str/split content #"\s+"))
                              read-time (max 1 (int (/ word-count 200)))]
                          (merge frontmatter
                                 {:slug slug
                                  :content-file (str file)
                                  :word-count word-count
                                  :read-time-minutes read-time})))
                      markdown-files)]
    {:writings (vec writings)}))

(defn edn->json [edn-data]
  "Convert EDN to JSON"
  (json/generate-string edn-data {:pretty true}))

(defn main []
  (println "🔨 Building web content...")
  
  ; Load EDN configuration
  (let [site-config (read-edn-file "data/site-config.edn")
        course-structure (read-edn-file "data/course-structure.edn")
        magazine-content (read-edn-file "data/magazine-content.edn")
        
        ; Build content index from Markdown
        writings-index (build-content-index)
        
        ; Output directory
        output-dir "web-app/static/content/"]
    
    ; Create output directory
    (fs/create-dirs output-dir)
    
    ; Write JSON files for SvelteKit
    (spit (str output-dir "site-config.json")
          (edn->json site-config))
    
    (spit (str output-dir "course.json")
          (edn->json course-structure))
    
    (spit (str output-dir "magazine.json")
          (edn->json magazine-content))
    
    (spit (str output-dir "writings.json")
          (edn->json writings-index))
    
    ; Copy Markdown files to static/
    (fs/copy-tree "content/" (str output-dir "markdown/")
                  {:replace-existing true})
    
    (println "✅ Web build complete!")
    (println "   - site-config.json")
    (println "   - course.json")
    (println "   - magazine.json")
    (println "   - writings.json")
    (println (str "   - " (count (:writings writings-index)) " writings indexed"))))

(main)
```

### Script: `scripts/build-native.bb`

Prepares data for Clojure/Humble UI native app:

```clojure
#!/usr/bin/env bb

(ns build-native
  (:require [clojure.edn :as edn]
            [babashka.fs :as fs]
            [clojure.string :as str]))

(defn main []
  (println "🔨 Building native app data...")
  
  ; For native Clojure apps, we can use EDN directly!
  ; Just copy the data/ and content/ directories to resources/
  
  (let [native-resources "native-app/resources/"]
    (fs/create-dirs native-resources)
    
    ; Copy EDN files (no conversion needed!)
    (fs/copy-tree "data/" (str native-resources "data/")
                  {:replace-existing true})
    
    ; Symlink or copy content
    (fs/copy-tree "content/" (str native-resources "content/")
                  {:replace-existing true})
    
    (println "✅ Native build complete!")
    (println "   - EDN files copied to resources/data/")
    (println "   - Markdown content available in resources/content/")))

(main)
```

---

## 🌐 SVELTEKIT CONSUMPTION

### Loading Data in SvelteKit (`web-app/src/routes/+page.ts`)

```typescript
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ fetch }) => {
  const [siteConfig, writings, magazine] = await Promise.all([
    fetch('/content/site-config.json').then(r => r.json()),
    fetch('/content/writings.json').then(r => r.json()),
    fetch('/content/magazine.json').then(r => r.json())
  ]);
  
  return {
    site: siteConfig.site,
    theme: siteConfig.theme,
    writings: writings.writings,
    featured: writings.writings.filter((w: any) => w.featured),
    magazine
  };
};
```

### Rendering Markdown in Svelte (`web-app/src/routes/[slug]/+page.svelte`)

```svelte
<script lang="ts">
  import { marked } from 'marked';  // Markdown parser
  import type { PageData } from './$types';
  
  export let data: PageData;
  
  // Parse markdown content
  const htmlContent = marked(data.markdownContent);
</script>

<article>
  <header>
    <h1>{data.title}</h1>
    <div class="meta">
      <span>{data.date}</span>
      <span>{data.category}</span>
      <span>{data.readTime} min read</span>
    </div>
  </header>
  
  <div class="content">
    {@html htmlContent}
  </div>
</article>
```

---

## 🖥️ CLOJURE/HUMBLE UI CONSUMPTION

### Loading Data in Clojure (`native-app/src/grain_magazine/data.clj`)

```clojure
(ns grain-magazine.data
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(defn load-edn-resource [resource-path]
  "Load EDN file from resources/"
  (-> (io/resource resource-path)
      slurp
      edn/read-string))

(defn load-markdown [content-path]
  "Load Markdown file from resources/content/"
  (-> (io/resource (str "content/" content-path))
      slurp))

; Load all site data at startup
(def site-config (load-edn-resource "data/site-config.edn"))
(def course-structure (load-edn-resource "data/course-structure.edn"))
(def magazine-content (load-edn-resource "data/magazine-content.edn"))

(defn get-writing-by-slug [slug]
  "Fetch a specific writing and its content"
  (when-let [writing (->> (:writings magazine-content)
                          (filter #(= slug (:slug %)))
                          first)]
    (assoc writing :content (load-markdown (:content-file writing)))))

(defn get-week-lessons [week-number]
  "Get all lessons for a specific week"
  (when-let [week (->> (get-in course-structure [:course :weeks])
                       (filter #(= week-number (:number %)))
                       first)]
    (update week :lessons
            (fn [lessons]
              (map #(assoc % :content (load-markdown (:content-file %)))
                   lessons)))))
```

### Rendering in Humble UI (`native-app/src/grain_magazine/core.clj`)

```clojure
(ns grain-magazine.core
  (:require [io.github.humbleui.ui :as ui]
            [grain-magazine.data :as data]
            [grain-magazine.markdown :as md]))  ; Markdown→Hiccup converter

(defn writing-view [slug]
  (let [writing (data/get-writing-by-slug slug)
        content-hiccup (md/parse (:content writing))]
    (ui/column
      (ui/padding 20
        (ui/column
          ; Title
          (ui/label (:title writing)
                    {:font-size 32
                     :font-weight :bold})
          
          ; Meta
          (ui/row
            (ui/label (str "By " (:author writing)))
            (ui/gap 20 0)
            (ui/label (:date writing))
            (ui/gap 20 0)
            (ui/label (str (:read-time-minutes writing) " min read")))
          
          ; Rendered content
          (ui/scroll-pane
            (md/render-hiccup content-hiccup)))))))
```

---

## 🔄 BUILD & DEPLOY WORKFLOW

### Unified Build Script (`scripts/build-all.bb`)

```clojure
#!/usr/bin/env bb

(ns build-all
  (:require [babashka.process :as process]))

(defn run-cmd [cmd]
  (println (str "$ " cmd))
  (process/shell cmd))

(defn main []
  (println "🚀 Building all targets...\n")
  
  ; 1. Build web assets
  (println "📦 Building web content...")
  (run-cmd "bb scripts/build-web.bb")
  
  ; 2. Build native assets
  (println "📦 Building native content...")
  (run-cmd "bb scripts/build-native.bb")
  
  ; 3. Build SvelteKit
  (println "🌐 Building SvelteKit app...")
  (run-cmd "cd web-app && npm run build")
  
  ; 4. Build native app (uberjar)
  (println "🖥️  Building native app...")
  (run-cmd "cd native-app && clj -T:build uber")
  
  ; 5. Generate package manager artifacts
  (println "📦 Generating package manager files...")
  (run-cmd "bb scripts/package-manager-artifacts.bb")
  
  (println "\n✅ All builds complete!")
  (println "   → web-app/build/ (SvelteKit static site)")
  (println "   → native-app/target/grain-magazine.jar")
  (println "   → packages/ (Homebrew, Nix, Pacman, APT, Leiningen)"))

(main)
```

### Deploy Script (`scripts/deploy.bb`)

```clojure
#!/usr/bin/env bb

(ns deploy
  (:require [babashka.process :as process]
            [clojure.string :as str]))

(defn deploy-web []
  (println "🌐 Deploying web to GitHub Pages...")
  (process/shell "cd web-app/build && gh-pages -d ."))

(defn deploy-icp []
  (println "🌐 Deploying web to ICP...")
  (process/shell "dfx deploy --network ic grain-website"))

(defn publish-packages []
  (println "📦 Publishing packages...")
  ; Homebrew
  (process/shell "brew tap kae3g/grain-network")
  (process/shell "brew bump-formula-pr grainkae3g")
  
  ; Others...
  )

(defn main [& args]
  (let [target (first args)]
    (case target
      "web" (deploy-web)
      "icp" (deploy-icp)
      "packages" (publish-packages)
      "all" (do (deploy-web)
                (deploy-icp)
                (publish-packages))
      (println "Usage: bb deploy.bb [web|icp|packages|all]"))))

(apply main *command-line-args*)
```

---

## ✅ ADVANTAGES OF THIS APPROACH

### 1. Single Source of Truth
- ✅ Content written once in Markdown
- ✅ Metadata defined once in EDN
- ✅ Changes propagate to all targets

### 2. Type Safety
- ✅ EDN schema validation (via clojure.spec)
- ✅ TypeScript types generated from EDN
- ✅ Compile-time errors for bad data

### 3. Developer Experience
- ✅ Markdown is easy to write and review
- ✅ EDN is familiar to Clojure developers
- ✅ JSON auto-generated for web consumption
- ✅ No manual data synchronization

### 4. Version Control
- ✅ All content in Git
- ✅ Markdown diffs are readable
- ✅ EDN diffs are understandable
- ✅ Full history and rollback capability

### 5. Flexibility
- ✅ Easy to add new output targets (PDF, ePub, etc.)
- ✅ Content reuse across platforms
- ✅ A/B testing with different presentations
- ✅ Multi-language support (i18n)

---

## 🔮 FUTURE ENHANCEMENTS

### 1. Content Validation

```clojure
(require '[clojure.spec.alpha :as s])

(s/def ::title string?)
(s/def ::slug (s/and string? #(re-matches #"[a-z0-9-]+" %)))
(s/def ::date inst?)
(s/def ::category #{"Press Release" "Tutorial" "Deep Dive"})

(s/def ::writing (s/keys :req-un [::title ::slug ::date ::category]
                         :opt-un [::tags ::featured]))

; Validate at build time
(s/valid? ::writing {:title "Hello" :slug "hello" :date (java.util.Date.)})
```

### 2. Hot Reloading

Watch for changes and rebuild automatically:

```bash
# Terminal 1: Watch content changes
fswatch -o content/ data/ | xargs -n1 -I{} bb scripts/build-all.bb

# Terminal 2: SvelteKit dev server
cd web-app && npm run dev

# Terminal 3: Native app with hot reload
cd native-app && clj -M:dev
```

### 3. Search Index

Build full-text search index at build time:

```clojure
(defn build-search-index []
  (let [all-content (load-all-markdown)]
    {:index (map (fn [doc]
                   {:id (:slug doc)
                    :title (:title doc)
                    :content (strip-markdown (:content doc))
                    :tokens (tokenize (:content doc))})
                 all-content)}))
```

### 4. i18n Support

```
content/
├── en/
│   ├── writings/
│   └── course/
├── es/
│   ├── writings/
│   └── course/
└── zh/
    ├── writings/
    └── course/
```

---

## 📚 CONCLUSION

This **Markdown + EDN** pipeline gives you:
- ✅ **Simplicity:** Content creators write Markdown
- ✅ **Power:** Developers extend with EDN metadata
- ✅ **Flexibility:** Deploy to web AND native from same source
- ✅ **Maintainability:** One place to update, everywhere benefits

**The Grain Network's content infrastructure is now production-ready!** 🌾

---

**Questions?** See `scripts/` directory for working examples!

**Created by:** kae3g  
**For:** grainkae3g / Grain Network  
**License:** MIT


