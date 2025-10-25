# 🌾 grainsource-separation

**The Canonical Template/Personal Split Pattern**

The foundational module that defines how Grain Network modules separate shared defaults (template) from individual customization (personal).

---

## 🎯 **What Is Template/Personal Separation?**

**The Pattern (On Framework Ubuntu 24.04 LTS):**
```
grainstore/module/
├── template/                    ← Shared defaults (committed to grainpbc/module)
│   ├── config.template.edn      ← Community configuration
│   ├── .zshrc.template          ← Shared shell config
│   └── README.md                ← Documentation
│
└── personal/                    ← Private customizations
    └── kae3g/                   ← Your username directory
        ├── config.edn           ← Your actual config
        ├── .zshrc               ← Your shell config
        └── secrets.edn          ← API keys (gitignored!)

System Integration (via symlinks):
~/.zshrc -> grainstore/grainzsh/personal/kae3g/.zshrc
~/.config/graintime/ -> grainstore/graintime/personal/kae3g/
~/.local/bin/gb -> grainstore/grainbarrel/bin/gb
```

**Implementation on Framework Laptop:**
- **Template** = Lives in grainstore, committed to GitHub/Codeberg
- **Personal** = Lives in grainstore/module/personal/kae3g/, may be separate git repo
- **System** = Symlinks from standard locations (~/.zshrc, ~/.config/) to personal/
- **Both** = Version controlled separately, personal can be private repo

---

## 🖥️ **Framework Ubuntu 24.04 LTS Implementation**

### **Actual System Layout**

**On your Framework laptop:**
```bash
# Grainstore location
/home/xy/kae3g/grainkae3g/grainstore/

# Active symlinks (ls -la ~/):
~/.zshrc -> /home/xy/kae3g/grainkae3g/grainstore/grainzsh/personal/kae3g/.zshrc
~/.local/bin/gb -> /home/xy/kae3g/grainkae3g/grainstore/grainbarrel/bin/gb
~/.local/bin/gt -> /home/xy/kae3g/grainkae3g/grainstore/graintime/scripts/gt

# Config directories (ln -s):
~/.config/graintime/ -> /home/xy/kae3g/grainkae3g/grainstore/graintime/personal/kae3g/
~/.config/graindisplay/ -> /home/xy/kae3g/grainkae3g/grainstore/graindisplay/personal/kae3g/
~/.config/graindaemon/ -> /home/xy/kae3g/grainkae3g/grainstore/graindaemon/personal/kae3g/
```

### **Git Repository Strategy**

**Option 1: Monorepo (Current)**
```
kae3g/grainkae3g (one repo)
├── grainstore/
│   ├── grainzsh/
│   │   ├── template/     ← Committed
│   │   └── personal/
│   │       └── kae3g/    ← In .gitignore OR committed (your choice)
```

**Option 2: Separate Personal Repo**
```
kae3g/grainkae3g (public)
└── grainstore/
    └── grainzsh/
        └── template/     ← Public

kae3g/kae3g-grain-personal (private)
└── grainzsh/
    └── kae3g/            ← Private repo
        └── .zshrc        ← Secrets safe

# Symlink personal into grainstore:
ln -s ~/kae3g-grain-personal/grainzsh/kae3g \
      ~/kae3g/grainkae3g/grainstore/grainzsh/personal/kae3g
```

### **Active Symlinks on Framework**

**Shell Integration:**
```bash
# grainzsh active symlink
~/.zshrc -> grainstore/grainzsh/personal/kae3g/.zshrc

# Inside that .zshrc:
source $GRAINZSH_TEMPLATE/.zshrc  # Load template first
# Then personal customizations below
```

**Command Integration:**
```bash
# grainbarrel (gb)
~/.local/bin/gb -> grainstore/grainbarrel/bin/gb

# graintime (gt)  
~/.local/bin/gt -> grainstore/graintime/scripts/gt

# graindisplay
~/.local/bin/graindisplay-wayland -> grainstore/graindisplay/scripts/graindisplay-wayland
```

**Config Integration:**
```bash
# graintime config
~/.config/graintime/config.edn -> grainstore/graintime/personal/kae3g/config.edn

# Contains your location:
{:location {:city "San Rafael"
            :state "CA"
            :latitude 37.9735
            :longitude -122.5311}}
```

### **GrainOS Integration (grain6 + s6)**

**Service Management:**
```bash
# Services defined in template
grainstore/graindaemon/template/services/
├── graindisplay.service    ← Shared service definition
├── grainwifi.service       ← Shared service definition
└── grain6.service          ← Time-aware supervision

# Personal overrides
grainstore/graindaemon/personal/kae3g/services/
└── graindisplay.service    ← Your custom schedule (sunset times, etc.)
```

**grain6 Supervision:**
```clojure
; template/grain6-config.edn
{:services
 [{:name "graindisplay"
   :schedule {:sunset :start :sunrise :stop}
   :command "graindisplay-wayland on"}]}

; personal/kae3g/grain6-config.edn  
{:services
 [{:name "graindisplay"
   :schedule {:sunset :start :sunrise :stop}
   :location {:latitude 37.9735 :longitude -122.5311}  ; Your location
   :command "graindisplay-wayland on"}]}
```

**System applies personal overrides:**
```
grain6 starts → Reads template → Merges personal → Applies combined config
```

---

## 💡 **Why This Matters**

### **The Problem**

**Monolithic Config (Traditional):**
```
~/.zshrc  ← Contains EVERYTHING
  - Community-shareable aliases
  - Personal API keys (SECRET!)
  - Machine-specific paths
  - Default settings
```

**Issues:**
- Can't share without exposing secrets
- Hard to update shared parts
- No clear separation of concerns
- Fork-unfriendly

### **The Solution**

**Template/Personal Split:**
```
grainzsh/
├── template/
│   └── .zshrc         ← Safe to share, fork, publish
└── personal/
    └── kae3g/
        └── .zshrc     ← Private, contains secrets
```

**Benefits:**
- ✅ Share template freely
- ✅ Keep personal private
- ✅ Easy to update shared parts
- ✅ Fork-friendly
- ✅ Clear separation

---

## 📚 **Modules Using This Pattern**

### **Current (10+ modules):**

1. **grainzsh** - Shell configuration
   - template/: Lambda prompt, aliases, functions
   - personal/kae3g/: API keys, machine paths

2. **grainenvvars** - Environment variables
   - template/: Variable schema, 1Password integration
   - personal/kae3g/: Actual API keys (never committed!)

3. **graincourse** - Course publishing
   - template/: Build scripts, SvelteKit config
   - personal/kae3g-course-name/: Actual course content

4. **graindaemon** - Daemon management
   - template/: Service definitions
   - personal/: Machine-specific configs

5. **grainicons** - Icon management
   - template/: Default icon library
   - personal/: Custom icons

6. **graincasks** - AppImage management
   - template/: Standard casks (Cursor, Brave, etc.)
   - personal/: Your installed apps

7. **graindisplay** - Display settings
   - template/: Default warmth schedules
   - personal/: Your preferences

8. **graintime** - Timestamp system
   - template/: Location config schema
   - personal/: Your actual location

9. **grainpages** - Pages deployment
   - template/: CI/CD configs (GitHub Actions, Woodpecker)
   - personal/: Your deployed sites

10. **grainwifi** - WiFi management
    - template/: Connection scripts
    - personal/: Your network credentials

---

## 🏗️ **The Pattern Specification**

### **Directory Structure**

```
{module-name}/
├── README.md                    ← Explains the module
│
├── template/                    ← SHARED (safe to fork/publish)
│   ├── README.md                ← How to use template
│   ├── config.template.edn      ← Configuration schema
│   ├── scripts/                 ← Shared scripts
│   └── examples/                ← Example configurations
│
├── personal/                    ← PRIVATE (user-specific)
│   ├── .gitignore               ← Ignores secrets
│   ├── README.md                ← Personal notes
│   └── {username}/              ← Your configs
│       ├── config.edn           ← Your settings
│       └── secrets.edn          ← Your secrets (gitignored!)
│
├── src/                         ← Source code (if applicable)
│   └── {module}/
│       └── core.clj
│
├── deps.edn                     ← Dependencies
└── bb.edn                       ← Build tasks
```

### **.gitignore Pattern**

```gitignore
# personal/ directory rules
personal/*/secrets.edn
personal/*/config.edn
personal/*/.env
personal/*/api-keys/

# But keep the README
!personal/README.md
!personal/**/README.md
```

**Result:** Personal configs never accidentally committed!

---

## 🔧 **Implementation Guide**

### **For Module Authors**

**Step 1: Create Structure**
```bash
mkdir -p {module}/template {module}/personal
```

**Step 2: Create Template README**
```markdown
# {Module} - Template Configuration

This directory contains shared, community-maintained defaults.

## How to Customize

1. Copy to personal/{your-username}/
2. Edit as needed
3. Never commit secrets!
```

**Step 3: Create Personal README**
```markdown
# {Module} - Personal Configuration

Your private customizations go here.

## Setup

1. Copy template config: `cp template/config.template.edn personal/{username}/config.edn`
2. Edit with your values
3. Test: `bb test-config`

**WARNING:** This directory may contain secrets. Do not share!
```

**Step 4: Create .gitignore**
```bash
cat > personal/.gitignore << 'EOF'
# Ignore all personal configs by default
*/config.edn
*/secrets.edn
*/.env

# But keep READMEs
!README.md
!*/README.md
EOF
```

---

## 📖 **Usage Examples**

### **Example 1: grainzsh**

**Template (shared):**
```bash
# template/.zshrc
# Grain Network shared shell configuration

# Lambda prompt
PROMPT='λ '

# Grain aliases
alias gb='grainbarrel'
alias gt='graintime'
alias gs='grainsource'
```

**Personal (private):**
```bash
# personal/kae3g/.zshrc
# kae3g's personal shell config

# Load template first
source $GRAINZSH_TEMPLATE/.zshrc

# Personal additions
export OPENAI_API_KEY="sk-..."  # SECRET!
export MY_PROJECT_PATH="~/projects/grain"

# Machine-specific
alias dev='cd $MY_PROJECT_PATH && gb dev'
```

### **Example 2: grainenvvars**

**Template (shared):**
```clojure
; template/env-schema.edn
; Schema for environment variables

{:required [:OPENAI_API_KEY :HOME :USER]
 :optional [:ANTHROPIC_API_KEY :GROQ_API_KEY]
 :validation {:OPENAI_API_KEY #"^sk-[A-Za-z0-9]+$"
              :HOME #"^/.*"}}
```

**Personal (private):**
```clojure
; personal/kae3g/secrets.edn
; NEVER COMMIT THIS FILE!

{:OPENAI_API_KEY "sk-proj-..."
 :ANTHROPIC_API_KEY "sk-ant-..."
 :GROQ_API_KEY "gsk_..."}
```

---

## 🌾 **Philosophy: From Granules to THE WHOLE GRAIN**

### **Granule: One Config File**

A single configuration file is a granule:
- `template/config.edn` (one shared default)
- `personal/kae3g/config.edn` (one personal override)

### **Grain: Complete Module**

A module with template/personal separation is a grain:
- Shared defaults
- Personal overrides
- Clear documentation
- Safe to fork

### **THE WHOLE GRAIN: Ecosystem Pattern**

All Grain Network modules use the same pattern:
- Consistent across ecosystem
- Easy to understand once learned
- Works everywhere
- Community can contribute to templates
- Individuals maintain sovereignty

---

## 🔄 **Migration Guide**

### **For Existing Modules**

**Step 1: Audit Current Structure**
```bash
# What needs to be separated?
find {module} -name "*.edn" -o -name ".env" -o -name "*secret*"
```

**Step 2: Move to Template**
```bash
# Create template structure
mkdir -p {module}/template
mv {module}/config.example.edn {module}/template/config.template.edn
```

**Step 3: Create Personal**
```bash
# Create personal structure
mkdir -p {module}/personal/{username}
cp {module}/template/config.template.edn {module}/personal/{username}/config.edn
```

**Step 4: Update .gitignore**
```bash
echo "personal/*/config.edn" >> {module}/.gitignore
echo "personal/*/secrets.edn" >> {module}/.gitignore
```

**Step 5: Update Documentation**
```bash
# Add to README.md
cat >> {module}/README.md << 'EOF'

## Template/Personal Separation

This module uses the Grain Network template/personal pattern.

- **template/**: Shared defaults (safe to fork)
- **personal/**: Your customizations (private)

See: grainstore/grainsource-separation/README.md
EOF
```

---

## 📊 **Modules Needing Migration**

### **Already Using Pattern (✅):**
1. grainzsh
2. grainenvvars
3. graincourse
4. graindaemon
5. grainicons
6. graincasks
7. graindisplay
8. graintime
9. grainwifi
10. grainpages

### **Need Migration (📝):**
1. clojure-s6
2. clojure-sixos
3. clojure-icp
4. grain6
5. grainregistry
6. grainbarrel
7. grainsource
8. grainweb
9. grainspace
10. grainmusic
11. clotoko
12. grain-metatypes
13. grainneovedic
14. grain-nightlight
15. grainlexicon
16. graindrive
17. graindroid
18. grainwriter
19. grainaltproteinproject
20. graindevname

---

## 🛠️ **grainsource-separation Tools**

### **Validation Script**

```clojure
(ns grainsource-separation.validate
  "Validate template/personal separation")

(defn validate-module-separation
  "Check if module properly implements pattern"
  [module-path]
  (let [has-template? (fs/exists? (str module-path "/template"))
        has-personal? (fs/exists? (str module-path "/personal"))
        has-gitignore? (fs/exists? (str module-path "/personal/.gitignore"))
        template-readme? (fs/exists? (str module-path "/template/README.md"))
        personal-readme? (fs/exists? (str module-path "/personal/README.md"))]
    {:module module-path
     :has-template? has-template?
     :has-personal? has-personal?
     :has-gitignore? has-gitignore?
     :template-readme? template-readme?
     :personal-readme? personal-readme?
     :compliant? (and has-template? has-personal? has-gitignore?)}))
```

### **Migration Script**

```bash
#!/usr/bin/env bb
# Migrate module to template/personal pattern

bb grainsource-separation:migrate {module-name}
```

---

## 📝 **Documentation Standard**

### **Every Module README Must Include:**

```markdown
## 📁 Configuration

This module uses the **template/personal separation pattern**.

### Template (Shared Defaults)
- Location: `template/`
- Purpose: Community-maintained defaults
- Safe to: Fork, modify, share publicly

### Personal (Your Customizations)
- Location: `personal/{your-username}/`
- Purpose: Your private configurations
- Contains: Secrets, API keys, personal preferences
- **Never commit:** Secrets are gitignored

### Setup

1. Copy template to personal:
   ```bash
   cp template/config.template.edn personal/{username}/config.edn
   ```

2. Edit with your values:
   ```bash
   nano personal/{username}/config.edn
   ```

3. Test:
   ```bash
   bb test-config
   ```

See: [grainsource-separation](../grainsource-separation/README.md) for details.
```

---

## 🌐 **Integration with Other Modules**

### **grainpages Uses It**

```
grainpages/
├── template/
│   ├── github-pages.yml       ← Shared GitHub Actions
│   └── woodpecker-pages.yml   ← Shared Woodpecker CI
└── personal/
    └── kae3g/
        └── deployed-sites.edn ← Your sites list
```

### **grainsite Uses It** (if different from grainpages)

```
grainsite/
├── template/
│   ├── layouts/               ← Shared SvelteKit layouts
│   ├── components/            ← Shared components
│   └── styles/                ← Shared CSS
└── personal/
    └── kae3g/
        ├── custom-theme.css   ← Your theme
        └── overrides/         ← Your component overrides
```

### **grainclay Uses It**

```
grainclay/
├── template/
│   ├── package-sources.edn    ← Default package sources
│   └── sync-config.edn        ← Default sync settings
└── personal/
    └── kae3g/
        ├── my-packages.edn    ← Your package list
        └── custom-sources.edn ← Your custom sources
```

---

## 🔄 **Recursive Migration TODO**

### **The Task**

Update ALL grainstore modules to use grainsource-separation:

1. **Audit** - List all modules
2. **Identify** - Which need migration?
3. **Migrate** - Apply pattern to each
4. **Validate** - Check compliance
5. **Document** - Update READMEs
6. **Test** - Verify nothing breaks

### **Modules to Update (20):**

**Core Libraries:**
- [ ] clojure-s6
- [ ] clojure-sixos
- [ ] clojure-icp

**Tools:**
- [ ] grainbarrel
- [ ] grainsource
- [ ] grain6
- [ ] grainregistry

**Applications:**
- [ ] grainweb
- [ ] grainspace
- [ ] grainmusic

**Infrastructure:**
- [ ] clotoko
- [ ] grain-metatypes
- [ ] grainneovedic
- [ ] grain-nightlight
- [ ] grainlexicon

**Services:**
- [ ] graindrive
- [ ] graindroid
- [ ] grainwriter
- [ ] grainaltproteinproject
- [ ] graindevname

---

## 🎨 **Best Practices**

### **1. What Goes in Template?**

✅ **Include:**
- Default configurations
- Example files
- Community-maintained settings
- Documentation
- Scripts (non-secret)

❌ **Exclude:**
- API keys
- Passwords
- Personal preferences
- Machine-specific paths
- User data

### **2. What Goes in Personal?**

✅ **Include:**
- Your API keys (gitignored!)
- Your preferences
- Your machine paths
- Your customizations
- Your notes

❌ **Exclude:**
- Shared defaults (use template)
- Community contributions (use template)
- Examples (use template)

### **3. How to Share Improvements?**

**If you improve something in personal:**
1. Extract the non-secret part
2. Add to template
3. Submit PR
4. Community benefits!

**Example:**
```bash
# You created a great alias in personal/kae3g/.zshrc
alias gf='gb flow'

# Share it!
echo "alias gf='gb flow'" >> template/.zshrc
git add template/.zshrc
git commit -m "feat: add gf alias for gb flow"
```

---

## 🔐 **Security Model**

### **Template: Public by Design**

- Assume everything will be public
- Never put secrets in template
- Document what should be customized
- Provide .template files for sensitive configs

### **Personal: Private by Default**

- Add to .gitignore immediately
- Use separate private repo if needed
- Encrypt if sharing across machines
- 1Password integration for secrets

### **The Contract**

```
Template = I promise this is safe to share
Personal = I promise this stays private
```

**If you break this contract:**
- Secrets leak
- Trust erodes
- Pattern fails

**So we make it impossible to break:**
- Gitignore prevents commits
- Validation checks for secrets
- CI fails if secrets detected in template

---

## 🌾 **Philosophy**

### **Community + Sovereignty**

**The Paradox:**
- We want to share (community)
- We want privacy (sovereignty)

**The Resolution:**
- Template enables sharing
- Personal preserves sovereignty
- Both coexist harmoniously

### **Fork-Friendly by Design**

**Traditional repos:**
```
Fork → Must remove all personal stuff → Error-prone
```

**Grain Network repos:**
```
Fork → template/ is already clean → Ready to customize!
```

### **From Granules to THE WHOLE GRAIN**

**Granule:** One config file separation (template vs. personal)

**Grain:** One module properly separated

**THE WHOLE GRAIN:** All modules use the same pattern
- Learn once, use everywhere
- Consistent across ecosystem
- Easy to contribute
- Safe to share

---

## 📚 **See Also**

- All Grain Network modules (examples of pattern in use)
- [grainzsh](../grainzsh/README.md) - Shell config example
- [grainenvvars](../grainenvvars/README.md) - Environment variables example
- [graincourse](../graincourse/README.md) - Course publishing example

---

**Version:** 1.0.0  
**Status:** 🌾 Canonical Reference Implementation  
**Author:** kae3g (Grain PBC)  
**License:** CC-BY-SA-4.0 (documentation), MIT (code)

🌾 **One pattern, shared by all, serving community and sovereignty!**
