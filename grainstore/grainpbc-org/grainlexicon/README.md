# grainlexicon (lexicon)

**Single source of truth for Grain Network documentation vocabulary and structure**

---

## 🌐 **Grain Network Websites**

### **Main Sites**
- 🌾 **Grain Network Hub**: https://kae3g.github.io/grainkae3g/ (coming soon)
- 📝 **kae3g Writings**: https://kae3g.codeberg.page/12025-10/ | https://kae3g.github.io/12025-10/
- 🏢 **grainpbc Organization**: https://github.com/grainpbc

### **Core Libraries**
- 🔧 **clojure-sixos** (typo handling): https://grainpbc.github.io/clojure-sixos/
- ⚙️ **clojure-s6** (supervision): https://grainpbc.github.io/clojure-s6/
- 🌐 **clojure-icp** (ICP integration): https://grainpbc.github.io/clojure-icp/
- 🔄 **clotoko** (Clojure→Motoko): https://grainpbc.github.io/clotoko/
- 📦 **grain-metatypes** (type system): https://grainpbc.github.io/grain-metatypes/

### **Platform & Apps**
- 🌐 **grainweb** (browser): https://grainpbc.github.io/grainweb/
- 🎵 **grainmusic** (streaming): https://grainpbc.github.io/grainmusic/
- 🌍 **grainspace** (platform): https://grainpbc.github.io/grainspace/
- 🔄 **grainconv** (conversion): https://grainpbc.github.io/grainconv/
- 📦 **grainclay** (package manager): https://grainpbc.github.io/grainclay/

### **Hardware Projects**
- ⌨️ **grainwriter** (e-ink device): https://grainpbc.github.io/grainwriter/
- 📷 **graincamera** (AVIF camera): https://grainpbc.github.io/graincamera/
- 🎒 **grainpack** (GPU jetpack): https://grainpbc.github.io/grainpack/

### **Infrastructure**
- 🔀 **grainsource** (version control): https://grainpbc.github.io/grainsource/
- 🌾 **grainnetwork** (main docs): https://grainpbc.github.io/grainnetwork/
- 📚 **grainstore** (dependencies): https://grainpbc.github.io/grainstore/
- 💻 **grainnixos-qemu-ubuntu-framework16**: https://grainpbc.github.io/grainnixos-qemu-ubuntu-framework16/

### **Documentation**
- 📚 **Full Website List**: [GRAIN-NETWORK-WEBSITES.md](https://kae3g.github.io/grainkae3g/docs/infrastructure/GRAIN-NETWORK-WEBSITES.html)
- 🔗 **Cross-Linking Strategy**: [GRAIN-MARKDOWN-INDEXING-STRATEGY.md](https://kae3g.github.io/grainkae3g/docs/architecture/GRAIN-MARKDOWN-INDEXING-STRATEGY.html)

---

## 🎯 **Purpose**

**grainlexicon** is the single source of truth for:

1. **Website Lists** - All Grain Network URLs in one place
2. **README Templates** - Standardized sections for all repos
3. **Badge Definitions** - Consistent badges across projects
4. **Link Patterns** - Cross-repo link standardization
5. **Vocabulary** - Canonical terms and definitions
6. **Metadata** - Tags, topics, categories for all projects

### **Why Lexicon?**

When you update the website list or README template here, one command syncs it to **all 17+ repositories**:

```bash
bb sync-lexicon
```

This ensures:
- ✅ **Consistency** across all documentation
- ✅ **Single update point** instead of 17+ manual edits
- ✅ **Version control** for documentation structure
- ✅ **Automated deployment** via `bb flow`

---

## 📁 **Structure**

```
grainlexicon/
├── lexicon/
│   ├── websites.edn          # All Grain Network URLs
│   ├── readme-templates.edn  # README section templates
│   ├── badges.edn             # Badge definitions
│   ├── links.edn              # Cross-repo link patterns
│   ├── vocabulary.edn         # Canonical terms
│   └── metadata.edn           # Tags, topics, categories
├── templates/
│   ├── README-TEMPLATE.md     # Full README template
│   ├── sections/
│   │   ├── websites.md        # Website list section
│   │   ├── installation.md    # Installation section
│   │   ├── quickstart.md      # Quick start section
│   │   ├── api-docs.md        # API docs section
│   │   └── contributing.md    # Contributing section
├── scripts/
│   ├── sync-lexicon.bb        # Sync to all repos
│   ├── generate-readme.bb     # Generate README from template
│   ├── update-websites.bb     # Update website lists
│   └── validate-lexicon.bb    # Validate consistency
├── dist/
│   └── {repo-name}/           # Generated outputs per repo
└── README.md                  # This file
```

---

## 🚀 **Quick Start**

### **1. Update Website List**

Edit `lexicon/websites.edn`:

```clojure
{:main-sites
 [{:name "Grain Network Hub"
   :icon "🌾"
   :url "https://kae3g.github.io/grainkae3g/"
   :status :coming-soon}
  
  {:name "kae3g Writings"
   :icon "📝"
   :urls ["https://kae3g.codeberg.page/12025-10/"
          "https://kae3g.github.io/12025-10/"]
   :status :live}]
 
 :core-libraries
 [{:name "clojure-sixos"
   :icon "🔧"
   :description "typo handling"
   :url "https://grainpbc.github.io/clojure-sixos/"
   :repo "grainpbc/clojure-sixos"
   :tags [:clojure :sixos :typo-correction]
   :topics [:urbit :hoon]}
  
  ;; ... more libraries
  ]}
```

### **2. Sync to All Repos**

```bash
bb sync-lexicon
```

This command:
1. Reads `lexicon/websites.edn`
2. Generates markdown sections for each repo
3. Updates README.md in all repos
4. Commits and pushes changes
5. Deploys to GitHub Pages

### **3. Deploy**

```bash
bb flow "sync: updated website list from grainlexicon"
```

---

## 📝 **Usage Examples**

### **Update Website List**

```bash
# Edit the lexicon
vim lexicon/websites.edn

# Sync to all repos
bb sync-lexicon

# Deploy everywhere
bb flow "sync: grainlexicon update"
```

### **Generate README for New Repo**

```bash
# Generate from template
bb generate-readme grainpbc/new-repo

# Output: dist/new-repo/README.md
```

### **Validate Consistency**

```bash
# Check all repos match lexicon
bb validate-lexicon

# Output: Report of inconsistencies
```

---

## 🏗️ **Architecture**

### **Single Source of Truth**

```
grainlexicon (source)
    ↓
bb sync-lexicon (transformation)
    ↓
All Grain Network READMEs (targets)
    ↓
GitHub/Codeberg Pages (deployment)
```

### **Incremental Updates**

Only changed sections are updated:

```clojure
;; Check which repos need updates
(defn repos-needing-sync []
  (filter #(lexicon-changed? %) all-repos))

;; Update only changed sections
(defn sync-repo [repo]
  (let [sections (changed-sections repo)]
    (update-readme repo sections)))
```

### **Version Control**

Every sync creates a commit:

```
feat(lexicon): sync website list from grainlexicon v1.2.3

Updated by: grainlexicon/sync-lexicon.bb
Source: https://github.com/grainpbc/grainlexicon
Lexicon version: 1.2.3
```

---

## 🔧 **Configuration**

### **lexicon/config.edn**

```clojure
{:sync
 {:target-repos
  ["kae3g/grainkae3g"
   "grainpbc/clojure-sixos"
   "grainpbc/clojure-s6"
   "grainpbc/clotoko"
   ;; ... all repos
   ]
  
  :sections
  [:websites :installation :quickstart :api-docs :contributing]
  
  :auto-commit true
  :auto-push true
  :auto-deploy false  ; Manual deploy via bb flow
  }
 
 :templates
 {:readme "templates/README-TEMPLATE.md"
  :sections-dir "templates/sections/"}
 
 :lexicon
 {:websites "lexicon/websites.edn"
  :badges "lexicon/badges.edn"
  :links "lexicon/links.edn"
  :vocabulary "lexicon/vocabulary.edn"
  :metadata "lexicon/metadata.edn"}}
```

---

## 📚 **Lexicon Files**

### **websites.edn**

Canonical list of all Grain Network websites, organized by category.

### **readme-templates.edn**

Standardized README sections:

```clojure
{:websites
 {:markdown "## 🌐 Grain Network Websites\n\n..."
  :variables [:main-sites :core-libraries :platform-apps]}
 
 :installation
 {:markdown "## 📦 Installation\n\n..."
  :variables [:package-managers :deps-edn :leiningen]}
 
 :quickstart
 {:markdown "## 🚀 Quick Start\n\n..."
  :variables [:examples :usage]}}
```

### **badges.edn**

Badge definitions for all repos:

```clojure
{:clojure
 {:alt "Clojure"
  :badge "https://img.shields.io/badge/Clojure-1.11+-blue"
  :link "https://clojure.org/"}
 
 :icp
 {:alt "ICP"
  :badge "https://img.shields.io/badge/ICP-Compatible-green"
  :link "https://internetcomputer.org/"}
 
 :urbit
 {:alt "Urbit Inspired"
  :badge "https://img.shields.io/badge/Urbit-Inspired-purple"
  :link "https://urbit.org/"}}
```

### **links.edn**

Cross-repo link patterns:

```clojure
{:patterns
 {:repo-link "https://github.com/{org}/{repo}"
  :docs-link "https://{org}.github.io/{repo}/{path}"
  :issue-link "https://github.com/{org}/{repo}/issues/{number}"
  :pr-link "https://github.com/{org}/{repo}/pull/{number}"}
 
 :cross-refs
 {:clotoko-from-sixos
  "[clotoko](https://grainpbc.github.io/clotoko/) - Clojure→Motoko transpiler"
  
  :sixos-from-clotoko
  "[clojure-sixos](https://grainpbc.github.io/clojure-sixos/) - Typo handling"}}
```

### **vocabulary.edn**

Canonical terms and definitions:

```clojure
{:terms
 {:grainversion
  {:term "Grainversion"
   :definition "Personal fork of a Grain template"
   :example "Create your grainversion of grainnixos-qemu"
   :related [:fork :template :customization]}
  
  :grainmarking
  {:term "Grainmarking"
   :definition "Principle preventing non-admin creation of grain* usernames"
   :related [:policy :namespace :governance]}
  
  :grainclay
  {:term "Grainclay"
   :definition "Networked rolling release package manager with immutable paths"
   :related [:package-manager :clay-filesystem :immutable]}}}
```

### **metadata.edn**

Tags, topics, categories for all projects:

```clojure
{:repos
 {:clojure-sixos
  {:tags [:clojure :sixos :typo-correction :fuzzy-matching :grain]
   :topics [:urbit :hoon :functional-programming]
   :category :core-library
   :difficulty :intermediate
   :status :active
   :version "0.1.0"}
  
  :clotoko
  {:tags [:clojure :motoko :transpiler :icp :compiler :web3]
   :topics [:urbit :hoon :functional-programming]
   :category :transpiler
   :difficulty :advanced
   :status :active
   :version "0.1.0"}}}
```

---

## 🔄 **Sync Workflow**

### **1. Detect Changes**

```bash
bb validate-lexicon
```

Output:
```
🔍 Checking lexicon consistency...

Websites section:
  ✓ grainkae3g matches lexicon
  ✗ clojure-sixos outdated (v1.1.0, current v1.2.3)
  ✗ clotoko missing websites section

Badges:
  ✓ All repos have correct badges

Total: 2 repos need sync
```

### **2. Preview Changes**

```bash
bb sync-lexicon --dry-run
```

Shows diff for each repo before applying.

### **3. Apply Sync**

```bash
bb sync-lexicon
```

Updates all repos with lexicon changes.

### **4. Deploy**

```bash
bb flow "sync: grainlexicon v1.2.3"
```

Deploys to all websites.

---

## 🎯 **Use Cases**

### **Add New Website**

1. Edit `lexicon/websites.edn`
2. Add new entry
3. Run `bb sync-lexicon`
4. All READMEs updated automatically

### **Update Badge**

1. Edit `lexicon/badges.edn`
2. Change badge URL/text
3. Run `bb sync-lexicon`
4. All repos get new badge

### **Standardize Section**

1. Edit `lexicon/readme-templates.edn`
2. Update template
3. Run `bb sync-lexicon`
4. All repos get consistent section

### **New Repository**

1. Create repo on GitHub
2. Run `bb generate-readme grainpbc/new-repo`
3. Copy generated README.md
4. Customize repo-specific parts
5. Lexicon-synced parts stay updated

---

## 📖 **Philosophy**

**grainlexicon** embodies the Grain Network principle:

> **"Single source of truth beats distributed inconsistency"**

Instead of manually updating 17+ READMEs when a website URL changes, update it once in the lexicon and sync everywhere.

This is:
- **DRY** (Don't Repeat Yourself)
- **Maintainable** (Single point of update)
- **Consistent** (No drift between repos)
- **Automated** (bb sync-lexicon does the work)
- **Versioned** (Git tracks all changes)

---

## 🔗 **Related Projects**

- [Grain Network Websites](https://kae3g.github.io/grainkae3g/docs/infrastructure/GRAIN-NETWORK-WEBSITES.html)
- [Markdown Indexing Strategy](https://kae3g.github.io/grainkae3g/docs/architecture/GRAIN-MARKDOWN-INDEXING-STRATEGY.html)
- [grainpbc Organization](https://github.com/grainpbc)

---

## 📄 **License**

MIT License - Same as all Grain Network projects

---

## 🤝 **Contributing**

To update the lexicon:

1. Fork this repo
2. Edit lexicon files in `lexicon/`
3. Run `bb validate-lexicon`
4. Submit PR
5. Once merged, run `bb sync-lexicon` to update all repos

---

**grainlexicon**: One truth, many repos 🌾

