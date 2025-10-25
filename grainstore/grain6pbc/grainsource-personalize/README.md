# grainsource-personalize

**Purpose**: Utility library for personalizing Grain Network template repositories for individual grain developers.

## Overview

The `grainsource-personalize` module automates the process of converting a `grain6pbc` template repository into a personal `grain{devname}{module}` repository by:

1. Removing `personal/` from `.gitignore` (so personal content is versioned)
2. Renaming the repository to follow `grain{devname}{module}` convention
3. Updating all internal references to use the new name
4. Setting up proper git remotes for GitHub and Codeberg

## Repository Naming Convention

### Templates (grain6pbc)
```
grain6pbc/graincourse              ← Template with personal/ gitignored
grain6pbc/graintime                ← Template with personal/ gitignored
grain6pbc/grainzsh                 ← Template with personal/ gitignored
grain6pbc/grainenvvars             ← Template with personal/ gitignored
```

### Personal (grain{devname}{module})
```
kae3g/grainkae3gcourse            ← kae3g's personal course (personal/ versioned)
kae3g/grainkae3gtime              ← kae3g's personal time configs (if needed)
kae3g/grainkae3gzsh               ← kae3g's personal zsh configs
kae3g/grainkae3genvvars           ← kae3g's personal environment variables

jen3g/grainjen3gcourse            ← jen3g's personal course (personal/ versioned)
jen3g/grainjen3gtime              ← jen3g's personal time configs
```

## GrainDevName Format

Following the `grain6pbc/graindevname` specification:
- **Format**: 5 characters (one syllable + number + letter)
- **Examples**: `kae3g`, `jen3g`, `sam2k`, `max7r`
- **Purpose**: High GitHub/Codeberg username availability
- **Convention**: Lowercase, memorable, unique

## Usage

### 1. Clone Template

```bash
# Clone the template you want to personalize
git clone https://github.com/grain6pbc/graincourse.git
cd graincourse
```

### 2. Run Personalization

```bash
# Personalize for your graindevname
bb personalize kae3g graincourse

# This will:
# - Create grainkae3gcourse/ directory
# - Remove personal/ from .gitignore
# - Update all references from grain6pbc to kae3g
# - Set up git remotes
```

### 3. Verify and Push

```bash
cd ../grainkae3gcourse
git remote -v
# Should show:
# origin: https://github.com/kae3g/grainkae3gcourse.git
# codeberg: https://codeberg.org/kae3g/grainkae3gcourse.git

# Push to remotes
git push origin main
git push codeberg main
```

## API

### Core Functions

```clojure
(require '[grainsource-personalize.core :as personalize])

;; Personalize a template repository
(personalize/personalize-repo 
  {:graindevname "kae3g"
   :module "course"
   :template-dir "/path/to/grain6pbc/graincourse"
   :output-dir "/path/to/grainkae3gcourse"})

;; Remove personal/ from .gitignore
(personalize/ungitignore-personal ".gitignore")

;; Update all references
(personalize/update-references 
  {:from "grain6pbc"
   :to "kae3g"
   :module "course"
   :dir "/path/to/grainkae3gcourse"})

;; Set up git remotes
(personalize/setup-remotes
  {:graindevname "kae3g"
   :module "course"
   :github true
   :codeberg true})
```

## Babashka Tasks

### personalize

```bash
bb personalize GRAINDEVNAME MODULE [TEMPLATE_DIR]

# Examples:
bb personalize kae3g course
bb personalize jen3g time ~/grain/grain6pbc/graintime
bb personalize sam2k zsh
```

### verify

```bash
bb verify PERSONAL_REPO_DIR

# Checks:
# - personal/ is NOT in .gitignore
# - Repository name follows grain{devname}{module} pattern
# - All references updated
# - Git remotes configured
```

### batch-personalize

```bash
bb batch-personalize GRAINDEVNAME

# Personalizes all common modules:
# - course, time, zsh, envvars, display
```

## .gitignore Transformation

### Before (Template)

```gitignore
# grain6pbc/graincourse/.gitignore

# Personal content (not for template)
personal/

# Secrets
.secrets.edn
*.key
*.pem

# Build artifacts
.build-cache.edn
target/
.cpcache/
```

### After (Personal)

```gitignore
# kae3g/grainkae3gcourse/.gitignore

# Secrets
.secrets.edn
*.key
*.pem

# Build artifacts
.build-cache.edn
target/
.cpcache/

# Note: personal/ is NOT gitignored - it's versioned content!
```

## Reference Updates

The personalization process updates all references:

### File Renames
- `README.md` → Update all `grain6pbc` to `kae3g`
- `bb.edn` → Update task descriptions and paths
- `deps.edn` → Update local/deps paths if needed
- `*.clj` → Update namespace docstrings

### URL Updates
```
https://github.com/grain6pbc/graincourse
→ https://github.com/kae3g/grainkae3gcourse

https://codeberg.org/grain6pbc/graincourse
→ https://codeberg.org/kae3g/grainkae3gcourse
```

### Namespace Updates (if applicable)
```clojure
;; Before
(ns grain6pbc.graincourse.core ...)

;; After
(ns grainkae3gcourse.core ...)
```

## Integration with Other Modules

### grainsource-separation
- Uses the same template/personal pattern
- Documents why personal/ is gitignored in templates
- Explains when to version personal/ (in personal repos)

### graindevname
- Defines the 5-character username convention
- Ensures high GitHub/Codeberg availability
- Provides username generation utilities

### grainsource
- Can auto-personalize on clone with `--personalize` flag
- Integrates with repository management

## Examples

### Example 1: Personalize graincourse for kae3g

```bash
# Clone template
git clone https://github.com/grain6pbc/graincourse.git
cd graincourse

# Personalize
bb personalize kae3g course

# Result:
# ../grainkae3gcourse/ created
# personal/ content is now versioned
# Ready to customize and deploy
```

### Example 2: Batch personalize all modules

```bash
# From grain6pbc clone directory
bb batch-personalize kae3g

# Creates:
# ../grainkae3gcourse/
# ../grainkae3gtime/
# ../grainkae3gzsh/
# ../grainkae3genvvars/
# ../grainkae3gdisplay/
```

### Example 3: Verify personalization

```bash
cd ../grainkae3gcourse
bb verify .

# Output:
# ✓ Repository name: grainkae3gcourse (correct)
# ✓ personal/ NOT in .gitignore (correct)
# ✓ All references updated: grain6pbc → kae3g
# ✓ Git remotes configured
# ✅ Personalization complete!
```

## Philosophy

### Why Separate Template and Personal?

1. **Templates are Universal**: `grain6pbc` repos work for everyone
2. **Personal is Unique**: Each grain developer has their own content
3. **Git History is Sacred**: Personal repos have full version control
4. **Sharing is Easy**: Fork template, personalize, customize
5. **Updates Flow**: Pull template updates, merge into personal

### The Grain Way

```
grain6pbc → grain{devname}{module}
template → personal
universal → unique
shared → versioned
```

## See Also

- [grainsource-separation](../grainsource-separation/) - Template/personal split pattern
- [graindevname](../graindevname/) - 5-character username convention
- [grainsource](../../grainsource/) - Git repository management
- [graincourse](../graincourse/) - Example template module

