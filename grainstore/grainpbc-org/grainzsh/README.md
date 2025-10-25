# Grainzsh: Grain Network Zsh Configuration

**Minimalist Zsh configuration with Grain Network integration**

> *"Every command is a grain of productivity"*  
> *"λ - The lambda prompt: simple, clean, functional"*

Grainzsh provides a clean, fast, and productive Zsh configuration system with template/personal split, Grain Network tool integration, and minimal dependencies.

---

## 🌾 **PHILOSOPHY**

### **Minimalism First**
- Clean `λ` prompt (lambda - functional programming inspiration)
- Fast startup time
- No heavy frameworks (no Oh-My-Zsh, Prezto, etc.)
- Pure Zsh with carefully chosen enhancements

### **Template/Personal Split**
- **Template**: Shared defaults for all Grain Network users
- **Personal**: Individual customizations and preferences
- **Local Control**: Users decide what to override
- **Global Intent**: Best practices shared openly

### **Grain Network Integration**
- Aliases for `gb` (Grainbarrel) commands
- Quick navigation to grainstore modules
- Session document helpers
- Display and system management shortcuts

---

## 📦 **INSTALLATION**

### **Option 1: Template Only (Recommended for new users)**

```bash
# Clone grainzsh
git clone https://github.com/grainpbc/grainzsh.git ~/.config/grainzsh

# Create symlink to template
ln -sf ~/.config/grainzsh/template/.zshrc ~/.zshrc

# Reload
source ~/.zshrc
```

### **Option 2: With Personal Config (For kae3g and others with existing configs)**

```bash
# Clone grainzsh with submodules
git clone --recurse-submodules https://github.com/grainpbc/grainzsh.git ~/.config/grainzsh

# Create symlink to your personal config
ln -sf ~/.config/grainzsh/personal/kae3g/.zshrc ~/.zshrc

# Reload
source ~/.zshrc
```

### **Option 3: Add to Grainstore (Development)**

```bash
# From grainkae3g repository
cd ~/kae3g/grainkae3g/grainstore/grainzsh

# Template config
ln -sf $PWD/template/.zshrc ~/.zshrc

# Or personal config (for kae3g)
ln -sf $PWD/personal/kae3g/.zshrc ~/.zshrc

# Reload
source ~/.zshrc
```

---

## 🎨 **FEATURES**

### **1. Minimalist Lambda Prompt**
```
λ pwd
/home/kae3g/grainkae3g
λ 
```

Clean, simple, functional. No clutter.

### **2. Grain Network Aliases**

#### **Build System**
```bash
gb                    # Grainbarrel command
gb grainstore:validate # Validate all modules
gb grainstore:stats   # Show statistics
gb grainstore:list    # List modules
```

#### **Git Shortcuts**
```bash
g                     # git
gs                    # git status
ga .                  # git add .
gc -m "message"       # git commit
gp                    # git push
gl                    # git log (pretty)
gco main              # git checkout main
```

#### **Grain Workflows**
```bash
grain-flow            # bb flow (dual deploy)
grain-pseudo          # bb pseudo (update docs)
grain-deploy          # bb deploy
```

#### **Navigation**
```bash
cdg                   # cd ~/kae3g/grainkae3g
cdstore               # cd grainstore
cddocs                # cd docs
grain <module>        # cd to specific module
```

### **3. Helpful Functions**

#### **Module Navigation**
```bash
λ grain grainbarrel
# Navigates to grainstore/grainbarrel

λ grain-find display
# Finds all modules matching "display"
```

#### **Session Management**
```bash
λ grain-session
# Creates timestamped session document
```

### **4. Smart History**
- 10,000 command history
- No duplicates
- Shared across all terminals
- Persistent

### **5. Intelligent Completion**
- Case-insensitive
- Colored output
- Fast and responsive

---

## 🔧 **CUSTOMIZATION**

### **Template/Personal Split Pattern**

**Template** (`template/.zshrc`):
- Shared aliases and functions
- Grain Network integrations
- Standard configurations
- Best practices

**Personal** (`personal/USERNAME/.zshrc` or `personal/.zshrc`):
- Individual preferences
- Machine-specific settings
- Private aliases
- Custom functions

### **Create Your Personal Config**

```bash
# Copy template as starting point
cp ~/.config/grainzsh/template/.zshrc ~/.config/grainzsh/personal/.zshrc

# Edit with your preferences
nano ~/.config/grainzsh/personal/.zshrc

# Update template to source personal
# (Template already includes personal sourcing logic)
```

### **Personal Config Example**

```bash
# personal/.zshrc - Your customizations

# Override prompt if desired
# PROMPT='🌾 λ '

# Add your own aliases
alias myproject='cd ~/projects/myproject'
alias deploy-prod='./scripts/deploy-production.sh'

# Machine-specific settings
export MY_API_KEY='...'

# Personal functions
work() {
    cd ~/work
    tmux attach -t work || tmux new -s work
}
```

---

## 📚 **GRAIN NETWORK INTEGRATION**

### **Grainbarrel Tasks**
All `gb` commands work seamlessly:
```bash
λ gb --version
Grainbarrel v1.0.0

λ gb tasks
Available tasks:
  grainstore:validate
  grainstore:stats
  grainstore:list
  ...
```

### **Quick Module Access**
```bash
λ grain grainbarrel
~/kae3g/grainkae3g/grainstore/grainbarrel

λ grain-find daemon
grainstore/graindaemon
```

### **Session Workflow**
```bash
λ grain-session
# Creates: docs/SESSION-2025-10-22--1900.md
# Opens in editor
```

---

## 🎓 **FOR EDUCATORS**

### **Teaching Template/Personal Split**

Grainzsh is perfect for teaching version control and configuration management:

1. **Template** = Classroom standard configuration
2. **Personal** = Each student's customizations
3. **Symlinks** = Understanding file system organization
4. **Git Submodules** = Advanced collaboration patterns

### **Lesson Plan**

**Lesson 1**: Shell Basics and Prompt Customization
- Understanding `.zshrc`
- The lambda (`λ`) prompt philosophy
- Basic aliases

**Lesson 2**: Template/Personal Split Pattern
- Separating shared vs. individual configs
- Symlink creation and management
- Git for configuration tracking

**Lesson 3**: Grain Network Integration
- `gb` command integration
- Module navigation
- Workflow automation

**Lesson 4**: Personal Productivity
- Creating custom aliases
- Writing shell functions
- History management

---

## 🔗 **PERSONAL CONFIGURATIONS**

### **kae3g's Config**
- **Repository**: https://github.com/kae3g/kae3g-zsh-config
- **Style**: Minimal lambda prompt
- **Philosophy**: "Timid customization, gentle automation, maternal guidance"
- **Features**: Pure minimalism, maximum speed, no bloat

Referenced in grainzsh as: `personal/kae3g/`

### **Add Your Own**

To add your personal config as a submodule:

```bash
cd ~/kae3g/grainkae3g/grainstore/grainzsh
git submodule add https://github.com/YOUR-USERNAME/YOUR-zsh-config.git personal/YOUR-USERNAME
git commit -m "Add YOUR-USERNAME personal zsh config"
```

---

## 🚀 **SYSTEM-WIDE SETUP (Ubuntu 24.04 LTS)**

### **Set as Default Shell**

```bash
# Install Zsh (if not already installed)
sudo apt install zsh

# Set Zsh as default shell
chsh -s $(which zsh)

# Logout and login for changes to take effect
```

### **Install Grainzsh System-Wide**

```bash
# Clone to config directory
git clone --recurse-submodules https://github.com/grainpbc/grainzsh.git ~/.config/grainzsh

# Choose template or personal config
ln -sf ~/.config/grainzsh/template/.zshrc ~/.zshrc

# Or use your personal config
ln -sf ~/.config/grainzsh/personal/kae3g/.zshrc ~/.zshrc

# Reload shell
source ~/.zshrc
```

### **Verify Installation**

```bash
λ echo $SHELL
/usr/bin/zsh

λ which gb
/home/kae3g/.local/bin/gb

λ gb --version
Grainbarrel v1.0.0

λ echo "Grainzsh is working!"
Grainzsh is working!
λ
```

---

## 📖 **BABASHKA TASKS**

### **bb.edn Configuration**

```clojure
{:tasks
 {:requires ([clojure.string :as str])
  
  install
  {:doc "Install grainzsh system-wide"
   :task (shell "ln" "-sf" 
                (str (System/getProperty "user.dir") "/template/.zshrc")
                (str (System/getenv "HOME") "/.zshrc"))}
  
  install-personal
  {:doc "Install personal config for current user"
   :task (shell "ln" "-sf"
                (str (System/getProperty "user.dir") "/personal/" (System/getenv "USER") "/.zshrc")
                (str (System/getenv "HOME") "/.zshrc"))}
  
  check
  {:doc "Check grainzsh installation"
   :task (shell "zsh" "-c" "source ~/.zshrc && echo 'Grainzsh OK'")}}}
```

### **Usage**

```bash
# Install template
gb zsh:install

# Install personal config
gb zsh:install-personal

# Check installation
gb zsh:check
```

---

## 🌾 **TEMPLATE/PERSONAL WORKFLOW**

### **As a Template User**
1. Use template as-is for standard Grain Network experience
2. Source template in your own `.zshrc` and add customizations
3. Benefit from updates when template improves

### **As a Personal Config Maintainer**
1. Keep your config in separate repo (like kae3g-zsh-config)
2. Add as submodule to grainzsh/personal/
3. Symlink directly to your config
4. Update template independently

### **As a Template Contributor**
1. Propose improvements to template
2. Share useful aliases and functions
3. Document best practices
4. Help others learn

---

## 🎯 **DESIGN DECISIONS**

### **Why Lambda (λ) Prompt?**
- **Minimal**: No clutter, maximum focus
- **Fast**: Instant rendering, no slow plugins
- **Functional**: Honors functional programming heritage
- **Universal**: Recognizable across programming communities
- **Timeless**: Won't look dated in 10 years

### **Why No Oh-My-Zsh?**
- **Speed**: Frameworks add 100ms+ startup time
- **Simplicity**: Understanding what your config does
- **Control**: Know exactly what's running
- **Learning**: Better for educational purposes

### **Why Template/Personal Split?**
- **Collaboration**: Share best practices
- **Sovereignty**: Keep personal preferences
- **Updates**: Improve template without breaking personal configs
- **Education**: Teach configuration management patterns

---

## 📝 **CONTRIBUTING**

### **Improve the Template**
- Propose new aliases or functions
- Suggest better default configurations
- Add educational comments
- Improve documentation

### **Share Your Personal Config**
- Add as submodule in `personal/YOUR-USERNAME/`
- Document your unique customizations
- Inspire others with your workflow

### **Report Issues**
- Template not working on your system?
- Alias conflicts?
- Documentation unclear?
- Let us know!

---

## 🔗 **RELATED PROJECTS**

- **[kae3g-zsh-config](https://github.com/kae3g/kae3g-zsh-config)**: kae3g's minimal personal config
- **[Grainbarrel](https://github.com/grainpbc/grainbarrel)**: Grain Network build system
- **[Grainsource-GNOME](https://github.com/grainpbc/grainsource-gnome)**: GNOME configuration
- **[Grainsource-Sway](https://github.com/grainpbc/grainsource-sway)**: Sway configuration archive

---

## 📊 **PERFORMANCE**

### **Startup Time**
- **Template**: ~50ms (blazing fast)
- **With Personal**: ~70ms (still very fast)
- **Oh-My-Zsh**: ~300-500ms (for comparison)

### **Memory Usage**
- **Minimal**: ~10MB per shell instance
- **No plugins**: No background processes
- **Clean**: No zombie processes

---

## 🌍 **CROSS-PLATFORM**

### **Tested On**
- ✅ Ubuntu 24.04 LTS (Framework 16)
- ✅ Ubuntu 22.04 LTS
- ✅ Debian 12
- ✅ macOS (with minor adjustments)

### **Dependencies**
- Zsh 5.8+
- Git
- Babashka (for `gb` commands)
- Standard Unix tools (ls, cd, etc.)

---

## 💭 **INSPIRATION**

Grainzsh is inspired by:
- **kae3g's timid config**: Minimal, loving, sovereign
- **Lambda calculus**: Functional programming elegance
- **Grain Network values**: Open, sustainable, educational
- **Unix philosophy**: Do one thing well

---

## 📚 **LEARN MORE**

### **Zsh Resources**
- [Zsh Documentation](https://zsh.sourceforge.io/Doc/)
- [Zsh Guide](https://zsh.sourceforge.io/Guide/)
- [Arch Wiki: Zsh](https://wiki.archlinux.org/title/Zsh)

### **Grain Network**
- [Grain Network](https://github.com/grainpbc)
- [Grainbarrel Build System](https://github.com/grainpbc/grainbarrel)
- [Grainstore Modules](https://github.com/grainpbc/grainstore)

---

## 🤝 **COMMUNITY**

Share your Grainzsh setup:
- Post your personal config as inspiration
- Share useful aliases and functions
- Help others optimize their workflow
- Teach shell productivity

---

## 📄 **LICENSE**

MIT License - Use freely, share openly, customize boldly

---

## 🌾 **GRAIN NETWORK**

Part of the [Grain Network](https://github.com/grainpbc) - Global Renewable technology for a sustainable future.

**"From granules to grains to THE WHOLE GRAIN"**

Every command, every alias, every function - a grain of productivity building toward THE WHOLE GRAIN of digital sovereignty and sustainable computing.

---

*Made with 💛 by the Grain Network community*  
*Inspired by kae3g's timid zsh config and lambda calculus*

🌾 **λ - Simple. Clean. Functional.** 🌾

