# Graincourse: Immutable Course Publishing System

**Grain Network's immutable grainpath-based course creation and deployment system**

> *"From granules to grains to THE WHOLE GRAIN"*  
> Building immutable education, one course at a time.

## 🌾 Immutable Grainpath System

Every course is an **immutable grainpath** with unique GitHub/Codeberg repositories:

- **Immutable**: Once created, courses cannot be modified
- **Versioned**: Each course has a unique grainpath identifier  
- **Dual-Platform**: Every course exists on both GitHub and Codeberg
- **Self-Contained**: Complete course with all dependencies
- **Traceable**: Full history and provenance tracking

**Grainpath Format**: `/course/{author}/{course-name}/{version}/`

**Example**: `/course/kae3g/grain-network-course/v1.0.0/`

---

## 🎯 Overview

Graincourse is a **universal course publishing system** that converts Markdown lessons into beautiful static websites and deploys them to GitHub Pages and Codeberg Pages.

### Template/Personal Split Pattern

Following Grain Network's standard architecture:

- **`template/`**: Reusable build scripts, HTML templates, and deployment tools
- **`personal/`**: Your course content, configuration, and branding

This separation allows:
- ✅ Easy updates to build tools without touching course content
- ✅ Multiple courses using the same infrastructure
- ✅ Sharing the template while keeping personal content private
- ✅ Clear distinction between framework and content

---

## 🚀 Quick Start

### 1. Create Your Course

```bash
# Create a new course in personal/
mkdir -p personal/my-course

# Create course configuration
cat > personal/my-course/course.edn << EOF
{:course
 {:title "My Amazing Course"
  :author "Your Name"
  :source-dir "path/to/your/lessons"
  :theme {:primary-color "#2c5f2d"}}}
EOF
```

### 2. Write Lessons

Place Markdown files in your source directory:

```markdown
# Lesson 1: Introduction

**Prerequisites**: None

## Learning Objectives
...
```

### 3. Build and Deploy

```bash
# Build HTML from Markdown
gb build

# Deploy to GitHub Pages
gb deploy:github

# Deploy to Codeberg Pages  
gb deploy:codeberg

# Or deploy to both at once
gb flow
```

---

## 📁 Directory Structure

```
graincourse/
├── bb.edn                          # Babashka task definitions
├── README.md                       # This file
│
├── template/                       # Shared build infrastructure
│   ├── scripts/
│   │   ├── build-course.bb        # Markdown → HTML conversion
│   │   ├── deploy-github.bb       # GitHub Pages deployment
│   │   └── deploy-codeberg.bb     # Codeberg Pages deployment
│   ├── styles/
│   │   ├── default.css            # Default course theme
│   │   └── grain-network.css      # Grain Network theme
│   └── templates/
│       ├── lesson.html            # Lesson page template
│       └── index.html             # Index page template
│
└── personal/                       # Your course instances
    ├── grain-network-course/      # Example: Grain Network course
    │   ├── course.edn            # Configuration
    │   └── public/               # Built HTML (gitignored)
    └── your-course/               # Your courses here
        └── course.edn
```

---

## 🎨 Course Configuration

### Minimal Configuration

```clojure
{:course
 {:title "Course Title"
  :author "Author Name"
  :source-dir "path/to/lessons"}}
```

### Full Configuration

```clojure
{:course
 {:title "Course Title"
  :subtitle "Course Description"
  :tagline "Inspiring Quote"
  :author "Author Name"
  :organization "Organization"
  :grade-level "9-12"
  
  :source-dir "../../../../docs/course/lessons"
  :output-dir "public"
  
  :theme
  {:primary-color "#2c5f2d"
   :accent-color "#97c93d"
   :background "#ffffff"
   :text "#333333"
   :code-background "#f5f5f5"}
  
  :navigation
  {:home-text "🏠 Home"
   :github-url "https://github.com/org/repo"
   :codeberg-url "https://codeberg.org/org/repo"
   :github-pages "https://org.github.io/repo/"
   :codeberg-pages "https://org.codeberg.page/repo/"}
  
  :footer
  {:text "Course Footer"
   :philosophy "Course Philosophy"
   :license "License Information"}
  
  :deployment
  {:github {:org "github-org" :repo "repo-name" :branch "gh-pages"}
   :codeberg {:org "codeberg-org" :repo "repo-name" :branch "pages"}}}}
```

---

## 📝 Lesson Format

Graincourse works with standard Markdown files:

### Required Front Matter

```markdown
# Lesson 1: Title

**Prerequisites**: Basic concepts  
**Duration**: 90 minutes  
**Grade Level**: 9-12

## Learning Objectives
...
```

### Supported Features

- ✅ GitHub Flavored Markdown
- ✅ Code syntax highlighting
- ✅ Math equations (MathJax)
- ✅ Embedded images and videos
- ✅ Interactive code examples
- ✅ Cross-lesson linking
- ✅ Downloadable resources

---

## 🛠️ Available Tasks

All tasks are available via `gb` (Grainbarrel):

```bash
# Course Management (Immutable Grainpath System)
gb create-course --author AUTHOR --name NAME --version VERSION
gb list-courses [--author AUTHOR] [--name NAME]
gb show-course --grainpath GRAINPATH

# Course Operations
gb build
gb deploy:github
gb deploy:codeberg
gb flow

# Setup and Utilities
gb setup-reminder
gb validate-course --grainpath GRAINPATH
```

**⚠️ First Time Setup**: Run `gb setup-reminder` before deploying!

### Course Creation Example

```bash
# Create new immutable course
gb create-course --author "kae3g" --name "grain-network-course" --version "1.0.0"

# This creates:
# - personal/kae3g-grain-network-course-v1.0.0/
# - GitHub: grain6pbc/course-kae3g-grain-network-course-v1.0.0
# - Codeberg: grain6pbc/course-kae3g-grain-network-course-v1.0.0
# - Grainpath: /course/kae3g/grain-network-course/v1.0.0/
```

---

## 🌐 Deployment

### GitHub Pages

Course publishes to: `https://{org}.github.io/{repo}/`

**Setup Requirements:**
1. **Repository Settings**: Go to Settings → Pages
2. **Source**: Select "GitHub Actions" (not "Deploy from a branch")
3. **Branch**: `gh-pages` branch (auto-created by deploy script)
4. **Authentication**: GitHub authentication configured

**Important**: GitHub Pages must be manually enabled in repository settings before deployment!

### Codeberg Pages

Course publishes to: `https://{org}.codeberg.page/{repo}/`

**Setup Requirements:**
1. **Repository Settings**: Go to Settings → Pages
2. **Branch**: `pages` branch (auto-created by deploy script)
3. **Manual Setup**: Must manually enable Pages in repository settings
4. **Authentication**: Codeberg authentication configured

**Important**: Codeberg Pages requires manual website setup in repo settings before deployment!

### Dual Deployment

The `gb flow` command automatically:
1. Builds HTML from Markdown
2. Commits to GitHub `gh-pages` branch
3. Commits to Codeberg `pages` branch  
4. Pushes to both platforms
5. Triggers automatic site rebuilds

---

## 🎓 Example: Grain Network Course

The Grain Network course demonstrates all features:

**Location**: `personal/grain-network-course/`

**Live Sites**:
- GitHub: https://grain6pbc.github.io/course/
- Codeberg: https://grain6pbc.codeberg.page/course/

**Features**:
- 7 lessons ordered by difficulty
- Comprehensive INDEX with learning paths
- Grain Network branding and philosophy
- Cross-platform deployment

**Try it**:
```bash
cd grainstore/graincourse
gb build
gb flow
```

---

## 🔧 Customization

### Custom Themes

Create CSS in `template/styles/` or override in your `course.edn`:

```clojure
:theme
{:primary-color "#your-color"
 :custom-css "path/to/custom.css"}
```

### Custom Templates

Override HTML templates in your personal course directory:

```
personal/your-course/
└── templates/
    ├── lesson.html      # Custom lesson template
    └── index.html       # Custom index template
```

### Build Hooks

Add custom build steps in `course.edn`:

```clojure
:build
{:pre-build ["script/before.sh"]
 :post-build ["script/after.sh"]}
```

---

## 🤝 Contributing

Improvements to the template system benefit all courses:

1. Fork the repository
2. Make changes in `template/`
3. Test with multiple courses
4. Submit pull request

---

## 📜 License

- **Template**: MIT License (free for all uses)
- **Personal courses**: Author's choice

---

## 🌾 Grain Network Philosophy

Graincourse embodies the Grain Network principles:

- **Template/Personal Split**: Clear separation of concerns
- **Universal Tools**: Works for any course, any topic
- **Dual Deployment**: GitHub + Codeberg for redundancy
- **Open Education**: Free tools for knowledge sharing
- **From Granules to Grains**: Small lessons → complete courses

**"Building education, one lesson at a time."** 🌾

---

**Created by**: Grain PBC  
**Session**: 808  
**Timestamp**: 12025-10-23

