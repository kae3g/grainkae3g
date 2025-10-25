# Graincourse: Immutable Grainpath System

**Every course is an immutable grainpath with unique GitHub/Codeberg repositories**

> *"From granules to grains to THE WHOLE GRAIN"*  
> Building immutable education, one course at a time.

---

## 🌾 Grainpath Philosophy

Following Grainclay's immutable path system, every graincourse is:

- **Immutable**: Once created, courses cannot be modified
- **Versioned**: Each course has a unique grainpath identifier
- **Dual-Platform**: Every course exists on both GitHub and Codeberg
- **Self-Contained**: Complete course with all dependencies
- **Traceable**: Full history and provenance tracking

---

## 🗂️ Grainpath Structure

### Course Naming Convention

```
grainpath: /course/{author}/{course-name}/{graintime}/
```

**Graintime Format**: Neovedic timestamp (Holocene + nakshatras)
```
12025-10-22--2010--PDT--moon-uttara-ashadha--11thhouse22--kae3g
```

**Examples:**
- `/course/kae3g/grain-network-course/12025-10-22--2010--PDT--moon-uttara-ashadha--11thhouse22--kae3g/`
- `/course/student/algebra-basics/12025-10-23--0900--EST--moon-vishakha--09thhouse17--student/`
- `/course/teacher/advanced-calculus/12025-11-15--1400--CST--moon-rohini--04thhouse10--teacher/`

### Repository Structure

Each course gets **exactly matching** repositories:

**GitHub**: `https://github.com/grain6pbc/course-{author}-{course-name}-{version}`
**Codeberg**: `https://codeberg.org/grain6pbc/course-{author}-{course-name}-{version}`

**Examples:**
- `grain6pbc/course-kae3g-grain-network-course-v1.0.0`
- `grain6pbc/course-student-algebra-basics-v2.1.0`
- `grain6pbc/course-teacher-advanced-calculus-v1.5.0`

---

## 🏗️ Template/Personal Architecture

### Template System (`template/`)

**Reusable infrastructure for all courses:**

```
template/
├── scripts/
│   ├── create-course.bb          # Create new immutable course
│   ├── build-course.bb           # Build HTML from Markdown
│   ├── deploy-github.bb          # Deploy to GitHub Pages
│   ├── deploy-codeberg.bb        # Deploy to Codeberg Pages
│   ├── validate-grainpath.bb     # Validate grainpath format
│   └── generate-metadata.bb      # Generate course metadata
├── styles/
│   ├── default.css               # Default course theme
│   └── grain-network.css         # Grain Network theme
└── templates/
    ├── course.edn.template       # Course configuration template
    ├── lesson.html               # Lesson page template
    └── index.html                # Index page template
```

### Personal System (`personal/`)

**Individual course instances:**

```
personal/
├── {author}-{course-name}-{version}/
│   ├── course.edn                # Course configuration
│   ├── lessons/                  # Markdown lessons
│   ├── public/                   # Built HTML (gitignored)
│   ├── metadata.edn              # Course metadata
│   └── grainpath.edn             # Immutable path info
└── {another-course}/
    └── ...
```

---

## 🚀 Creating Immutable Courses

### 1. Course Creation Command

```bash
# Create new immutable course
gb create-course --author "kae3g" --name "grain-network-course" --version "1.0.0"

# This creates:
# - personal/kae3g-grain-network-course-1.0.0/
# - GitHub repo: grain6pbc/course-kae3g-grain-network-course-1.0.0
# - Codeberg repo: grain6pbc/course-kae3g-grain-network-course-1.0.0
```

### 2. Course Configuration

Each course gets a unique `course.edn`:

```clojure
{:course
 {:grainpath "/course/kae3g/grain-network-course/v1.0.0/"
  :title "Building the Grain Network"
  :author "kae3g"
  :version "1.0.0"
  :timestamp "12025-10-23--1900--PST--moon-vishakha--09thhouse17--kae3g"
  
  :repositories
  {:github "https://github.com/grain6pbc/course-kae3g-grain-network-course-1.0.0"
   :codeberg "https://codeberg.org/grain6pbc/course-kae3g-grain-network-course-1.0.0"}
  
  :pages
  {:github "https://grain6pbc.github.io/course-kae3g-grain-network-course-1.0.0"
   :codeberg "https://grain6pbc.codeberg.page/course-kae3g-grain-network-course-1.0.0"}
  
  :immutable true
  :deletion-policy "manual-only"
  
  :source-dir "lessons"
  :output-dir "public"}}
```

### 3. Immutable Metadata

Each course includes `grainpath.edn`:

```clojure
{:grainpath
 {:path "/course/kae3g/grain-network-course/v1.0.0/"
  :author "kae3g"
  :course-name "grain-network-course"
  :version "1.0.0"
  :created "12025-10-23--1900--PST--moon-vishakha--09thhouse17--kae3g"
  :immutable-since "12025-10-23--1900--PST--moon-vishakha--09thhouse17--kae3g"
  :repositories
  {:github "grain6pbc/course-kae3g-grain-network-course-1.0.0"
   :codeberg "grain6pbc/course-kae3g-grain-network-course-1.0.0"}
  :checksum "sha256:abc123..."
  :dependencies []
  :license "MIT"}}
```

---

## 🔒 Immutability Rules

### 1. No Modifications After Creation

Once a course is created and deployed:
- ✅ **Read-only**: Course content cannot be changed
- ✅ **Versioned**: Create new version for updates
- ✅ **Preserved**: Original course remains forever
- ❌ **No edits**: Cannot modify existing lessons
- ❌ **No deletions**: Cannot delete course content

### 2. Versioning Strategy

For course updates, create new versions:

```bash
# Original course (immutable)
gb create-course --author "kae3g" --name "grain-network-course" --version "1.0.0"

# Updated course (new version)
gb create-course --author "kae3g" --name "grain-network-course" --version "1.1.0"

# Major revision (new version)
gb create-course --author "kae3g" --name "grain-network-course" --version "2.0.0"
```

### 3. Deletion Policy

Courses can only be deleted manually:

```bash
# Delete course (requires confirmation)
gb delete-course --author "kae3g" --name "grain-network-course" --version "1.0.0"

# This removes:
# - personal/kae3g-grain-network-course-1.0.0/
# - GitHub repository
# - Codeberg repository
# - All associated metadata
```

---

## 📊 Course Registry

### Global Course Index

All courses are registered in `grainstore/graincourse/COURSE-REGISTRY.edn`:

```clojure
{:course-registry
 {:version "1.0.0"
  :last-updated "12025-10-23--1900--PST--moon-vishakha--09thhouse17--kae3g"
  
  :courses
  {"/course/kae3g/grain-network-course/v1.0.0/"
   {:grainpath "/course/kae3g/grain-network-course/v1.0.0/"
    :author "kae3g"
    :course-name "grain-network-course"
    :version "1.0.0"
    :title "Building the Grain Network"
    :status :active
    :created "12025-10-23--1900--PST--moon-vishakha--09thhouse17--kae3g"
    :repositories
    {:github "grain6pbc/course-kae3g-grain-network-course-1.0.0"
     :codeberg "grain6pbc/course-kae3g-grain-network-course-1.0.0"}
    :pages
    {:github "https://grain6pbc.github.io/course-kae3g-grain-network-course-1.0.0"
     :codeberg "https://grain6pbc.codeberg.page/course-kae3g-grain-network-course-1.0.0"}}}}
```

### Course Discovery

```bash
# List all courses
gb list-courses

# Search courses by author
gb list-courses --author "kae3g"

# Search courses by name
gb list-courses --name "grain-network"

# Show course details
gb show-course --grainpath "/course/kae3g/grain-network-course/v1.0.0/"
```

---

## 🌐 Deployment Strategy

### Dual-Platform Deployment

Every course deploys to both platforms:

1. **GitHub Pages**: `https://grain6pbc.github.io/course-{author}-{name}-{version}/`
2. **Codeberg Pages**: `https://grain6pbc.codeberg.page/course-{author}-{name}-{version}/`

### Repository Management

- **GitHub**: Primary platform for discovery and collaboration
- **Codeberg**: Mirror for ethical alignment and redundancy
- **Sync**: Automatic synchronization between platforms
- **Backup**: Complete course preservation across platforms

---

## 🛠️ Available Commands

```bash
# Course Management
gb create-course --author AUTHOR --name NAME --version VERSION
gb delete-course --author AUTHOR --name NAME --version VERSION
gb list-courses [--author AUTHOR] [--name NAME]
gb show-course --grainpath GRAINPATH

# Course Operations
gb build-course --grainpath GRAINPATH
gb deploy-course --grainpath GRAINPATH
gb validate-course --grainpath GRAINPATH

# Registry Management
gb update-registry
gb search-courses QUERY
gb export-registry --format json
```

---

## 🔍 Course Validation

### Grainpath Validation

Every course must pass validation:

```bash
gb validate-course --grainpath "/course/kae3g/grain-network-course/v1.0.0/"
```

**Validation checks:**
- ✅ Grainpath format compliance
- ✅ Repository existence (GitHub + Codeberg)
- ✅ Course metadata completeness
- ✅ Lesson structure validity
- ✅ Build system compatibility
- ✅ Deployment readiness

### Checksum Verification

Each course includes content checksums:

```clojure
{:checksums
 {:lessons "sha256:abc123..."
  :assets "sha256:def456..."
  :metadata "sha256:ghi789..."
  :build "sha256:jkl012..."}}
```

---

## 🌱 Benefits of Immutable Courses

### 1. **Educational Integrity**
- Courses never change unexpectedly
- Students can rely on stable content
- Version history is preserved forever

### 2. **Collaboration Safety**
- No accidental modifications
- Clear version boundaries
- Safe to share and reference

### 3. **Reproducibility**
- Exact same course every time
- Deterministic builds
- Consistent learning experience

### 4. **Traceability**
- Complete creation history
- Author attribution preserved
- Change tracking through versions

### 5. **Scalability**
- Unlimited course versions
- No storage conflicts
- Clean separation of concerns

---

## 🎯 Example: Grain Network Course

**Grainpath**: `/course/kae3g/grain-network-course/v1.0.0/`

**Repositories**:
- GitHub: `grain6pbc/course-kae3g-grain-network-course-1.0.0`
- Codeberg: `grain6pbc/course-kae3g-grain-network-course-1.0.0`

**Live Sites**:
- GitHub: https://grain6pbc.github.io/course-kae3g-grain-network-course-1.0.0/
- Codeberg: https://grain6pbc.codeberg.page/course-kae3g-grain-network-course-1.0.0/

**Content**: 7 lessons, comprehensive INDEX, Grain Network branding

**Status**: Immutable, version 1.0.0, created Session 808

---

## 🌾 Grain Network Philosophy

Immutable courses embody the Grain Network principles:

- **From Granules to Grains**: Individual lessons → complete courses
- **Template/Personal Split**: Shared infrastructure, personal content
- **Dual Deployment**: GitHub + Codeberg for redundancy
- **Immutable Paths**: Grainclay-inspired versioning
- **Educational Freedom**: Open, accessible, permanent knowledge

**"Building immutable education, one course at a time."** 🌾

---

**Created by**: Grain PBC  
**Session**: 808  
**Timestamp**: 12025-10-23  
**Status**: Design Phase
