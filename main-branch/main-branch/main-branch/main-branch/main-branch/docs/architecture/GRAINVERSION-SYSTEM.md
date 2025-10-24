# 🌾 Grainversion System
## *"Personal implementations of Grain templates"*

**Concept:** grainversion (verb) - to create a personal version of a Grain template  
**Created:** January 22, 2025  
**Authority:** kae3g (Graingalaxy)

---

## 🎯 Definition

**grainversion** (verb): To fork and personalize a Grain template for your own use while maintaining the ability to receive upstream updates.

**Example usage:**
- "I grainversioned the NixOS QEMU template for my Framework laptop"
- "Let's make a grainversion of this configuration"
- "My grainversion includes custom Sway keybindings"

---

## 🏗️ Architecture

### Template vs Grainversion Structure

```
grainstore/
├── TEMPLATE-NAME/                          # Official Grain template
│   ├── README.md                           # Generic instructions
│   ├── .graintemplate                      # Marks as template
│   ├── configuration.nix                   # Default config
│   ├── flake.nix                          # Template flake
│   └── docs/
│       ├── CUSTOMIZATION.md               # How to grainversion
│       └── EXAMPLES.md                    # Example grainversions
│
└── kae3g-TEMPLATE-NAME/                   # Your grainversion
    ├── README.md                          # Your specific setup
    ├── .grainversion                      # Marks as grainversion
    ├── .grainversion-of                   # Points to template
    ├── configuration.nix                  # Your custom config
    ├── flake.nix                         # Your custom flake
    ├── personal/                         # Personal files
    │   ├── secrets.nix                  # Your secrets
    │   ├── hardware.nix                 # Your hardware
    │   └── customizations.nix           # Your tweaks
    └── upstream/                         # Template tracking
        └── TEMPLATE-NAME/               # Git submodule to template
```

---

## 📋 Naming Convention

### Template Names (Generic)

**Format:** `grain{purpose}-{description}`

**Examples:**
- `grainnixos-qemu-ubuntu-framework16` - NixOS QEMU on Ubuntu for Framework 16
- `grainconfig-sway-wayland` - Sway + Wayland configuration
- `graindeploy-debian-stable` - Debian stable deployment setup
- `graindev-clojure-humble` - Clojure + Humble UI dev environment

### Grainversion Names (Personal)

**Format:** `{username}-grain{purpose}-{description}`

**Examples:**
- `kae3g-grainnixos-qemu-ubuntu-framework16` - kae3g's NixOS QEMU setup
- `kae3g-grainconfig-sway-wayland` - kae3g's Sway config
- `alice-grainnixos-qemu-ubuntu-framework16` - Alice's version
- `bob-grainconfig-sway-wayland` - Bob's version

---

## 🔄 Grainversion Workflow

### 1. Create a Grainversion

```bash
# Clone the template
cd grainstore
git clone grainnixos-qemu-ubuntu-framework16 kae3g-grainnixos-qemu-ubuntu-framework16
cd kae3g-grainnixos-qemu-ubuntu-framework16

# Mark as grainversion
echo "grainnixos-qemu-ubuntu-framework16" > .grainversion-of
touch .grainversion

# Add template as upstream
git remote add upstream ../grainnixos-qemu-ubuntu-framework16
git remote rename origin personal

# Customize
mkdir personal
cp configuration.nix personal/configuration.nix.example
# Edit your personal config

# Commit
git add -A
git commit -m "Initial grainversion of NixOS QEMU template"
```

### 2. Sync with Template Updates

```bash
# Fetch template updates
git fetch upstream

# View changes
git log upstream/main

# Merge updates (careful with personal changes)
git merge upstream/main

# Or cherry-pick specific updates
git cherry-pick <commit-hash>
```

### 3. Share Your Grainversion

```bash
# Push to your own repo
git remote add origin https://github.com/kae3g/kae3g-grainnixos-qemu-ubuntu-framework16
git push -u origin main

# Others can grainversion your grainversion!
```

---

## 📁 File Markers

### `.graintemplate`

**In template repositories:**

```yaml
# .graintemplate
template:
  name: grainnixos-qemu-ubuntu-framework16
  version: 1.0.0
  description: NixOS QEMU setup for Ubuntu 24.04 LTS on Framework 16
  author: Grain PBC
  license: MIT
  
  customization-guide: docs/CUSTOMIZATION.md
  
  variables:
    - name: username
      default: nixos
      description: NixOS user name
    
    - name: hostname
      default: nixos-qemu-fw16
      description: System hostname
    
    - name: ssh-port
      default: 2223
      description: SSH port forwarding
```

### `.grainversion`

**In grainversion repositories:**

```yaml
# .grainversion
grainversion:
  template: grainnixos-qemu-ubuntu-framework16
  template-version: 1.0.0
  
  owner: kae3g
  created: 2025-01-22
  
  customizations:
    - Custom Sway keybindings
    - Personal Git config
    - Framework 16 specific optimizations
    - Grain Network development tools
  
  upstream:
    remote: upstream
    branch: main
    last-sync: 2025-01-22
```

### `.grainversion-of`

**Simple pointer file:**

```
grainnixos-qemu-ubuntu-framework16
```

---

## 🎨 Example: NixOS QEMU Template

### Template Repository

```
grainstore/grainnixos-qemu-ubuntu-framework16/
├── .graintemplate
├── README.md                    # Generic "How to use this template"
├── flake.nix                   # Generic flake
├── configuration.nix           # Generic config with {{ variables }}
├── home.nix                    # Generic home-manager
├── sway.nix                    # Generic Sway config
├── scripts/
│   ├── start-vm.sh            # Generic VM launcher
│   └── install.sh             # Installation script
└── docs/
    ├── CUSTOMIZATION.md       # How to grainversion this
    └── VARIABLES.md           # List of template variables
```

**`configuration.nix` (template):**

```nix
{ config, pkgs, ... }:

{
  # Template variables (replace when grainversioning)
  networking.hostName = "{{ hostname }}";
  
  users.users.{{ username }} = {
    isNormalUser = true;
    extraGroups = [ "wheel" "kvm" ];
  };
  
  # ... rest of config
}
```

### Grainversion Repository

```
grainstore/kae3g-grainnixos-qemu-ubuntu-framework16/
├── .grainversion
├── .grainversion-of
├── README.md                    # kae3g's specific setup
├── flake.nix                   # kae3g's custom flake
├── configuration.nix           # Variables filled in
├── home.nix                    # kae3g's home-manager
├── sway.nix                    # kae3g's Sway config
├── personal/
│   ├── secrets.nix            # kae3g's secrets (gitignored)
│   ├── keybindings.nix        # Custom keybindings
│   └── packages.nix           # Extra packages
├── scripts/
│   ├── start-vm.sh            # Customized for kae3g
│   └── sync-grain-projects.sh # kae3g-specific script
└── upstream/
    └── .git                   # Submodule to template
```

**`configuration.nix` (grainversion):**

```nix
{ config, pkgs, ... }:

{
  # Variables filled in
  networking.hostName = "nixos-qemu-fw16";
  
  users.users.nixos = {
    isNormalUser = true;
    extraGroups = [ "wheel" "kvm" ];
  };
  
  # kae3g's additions
  imports = [
    ./personal/secrets.nix
    ./personal/keybindings.nix
    ./personal/packages.nix
  ];
  
  # ... rest of config
}
```

---

## 🔧 Grainversion CLI Tool

**`grainversion` command (future):**

```bash
# Create new grainversion
grainversion create grainnixos-qemu-ubuntu-framework16

# Sync with template
grainversion sync

# List your grainversions
grainversion list

# Show diff from template
grainversion diff

# Update template reference
grainversion update-template
```

**Babashka implementation (now):**

```clojure
#!/usr/bin/env bb
;; grainversion - Manage Grain template versions

(require '[clojure.java.shell :as shell]
         '[clojure.string :as str])

(defn create-grainversion
  "Create a new grainversion from template"
  [template-name username]
  (let [grainversion-name (str username "-" template-name)]
    (println "Creating grainversion:" grainversion-name)
    (shell/sh "git" "clone" template-name grainversion-name)
    (shell/sh "sh" "-c" (str "cd " grainversion-name " && echo '" template-name "' > .grainversion-of"))
    (shell/sh "sh" "-c" (str "cd " grainversion-name " && touch .grainversion"))
    (println "✅ Created" grainversion-name)))

(defn sync-grainversion
  "Sync grainversion with template updates"
  []
  (let [template (str/trim (slurp ".grainversion-of"))]
    (println "Syncing with template:" template)
    (shell/sh "git" "fetch" "upstream")
    (shell/sh "git" "merge" "upstream/main")))

(defn list-grainversions
  "List all grainversions in grainstore"
  []
  (let [files (file-seq (clojure.java.io/file ".."))
        grainversions (filter #(.exists (clojure.java.io/file % ".grainversion")) files)]
    (println "Your grainversions:")
    (doseq [gv grainversions]
      (let [template (slurp (clojure.java.io/file gv ".grainversion-of"))]
        (println "  -" (.getName gv) "→" template)))))

(defn -main [& args]
  (case (first args)
    "create" (create-grainversion (second args) (nth args 2))
    "sync" (sync-grainversion)
    "list" (list-grainversions)
    (println "Usage: grainversion {create|sync|list}")))

(-main *command-line-args*)
```

---

## 📚 Benefits of Grainversion System

### For Template Creators

- ✅ Create once, benefit many
- ✅ Track usage and derivatives
- ✅ Get feedback from grainversions
- ✅ Improve template based on real use

### For Grainversioners

- ✅ Start with working configuration
- ✅ Customize without breaking
- ✅ Receive upstream improvements
- ✅ Share your enhancements
- ✅ Learn from others' grainversions

### For Grain Network

- ✅ Consistent structure across projects
- ✅ Easy onboarding for new users
- ✅ Community-driven improvements
- ✅ Knowledge sharing
- ✅ Rapid iteration

---

## 🌐 Grainversion Registry (Future)

**Centralized registry of templates and grainversions:**

```
https://grain.network/grainversions

Templates:
  - grainnixos-qemu-ubuntu-framework16 (Official)
    → 12 grainversions
    → kae3g-grainnixos-qemu-ubuntu-framework16
    → alice-grainnixos-qemu-ubuntu-framework16
    → bob-grainnixos-qemu-ubuntu-framework16
  
  - grainconfig-sway-wayland (Official)
    → 8 grainversions
  
  - graindeploy-debian-stable (Official)
    → 5 grainversions
```

---

## ✅ Summary

**Grainversion = Personal version of a Grain template**

**Key concepts:**
- **Template** = Generic, reusable configuration
- **Grainversion** = Personal implementation
- **Upstream** = Template updates flow to grainversions
- **Personal** = Your customizations stay yours
- **Sharing** = Others can grainversion your grainversion

**The grainversion system enables:**
- Rapid setup with proven configurations
- Personal customization without losing updates
- Community knowledge sharing
- Consistent Grain Network patterns

**This is how the Grain Network scales.** 🌾

---

**Grainversion System**  
**Author:** kae3g (Graingalaxy)  
**Organization:** Grain PBC  
**Date:** January 22, 2025

*"Fork it, make it yours, share it with others."* 🌐


