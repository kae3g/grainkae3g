# Lesson 08: Display Management, Package Management, and Build Systems

**Grain Network High School Course**  
**Topic**: User-Controlled Display Settings, AppImage Management, and Build Tool Design  
**Duration**: 2 class periods (90 minutes each)  
**Prerequisites**: Lessons 1-7, basic understanding of configuration files  
**Skills**: Critical thinking about user agency, declarative configuration, tool selection  

---

## 📚 Learning Objectives

By the end of this lesson, students will be able to:

1. **Explain** why local users should have control over their viewing experience
2. **Design** systems that respect both content creators and content consumers
3. **Evaluate** when to use existing tools vs. building purpose-built solutions
4. **Create** declarative configuration files using EDN format
5. **Understand** the template/personal configuration split pattern
6. **Implement** a simple wrapper script for command-line tools

---

## 🎯 Real-World Problem

**Scenario**: You're reading an article online, but the author has set the website to display with:
- Tiny 8pt font
- Bright white background (6500K color temperature)
- 100% brightness
- No accessibility options

**Questions to Consider**:
1. Should the author control how YOU view their content on YOUR device?
2. Should you be able to override their settings while still respecting their creative vision?
3. How do we balance creative intent with personal agency?

---

## Part 1: GrainDisplay - Universal Display Management (45 min)

### 🎨 The Problem: Display Settings and User Control

**Two Perspectives**:

**Content Creator's Perspective**:
- "I want my forest photos viewed with warm lighting (2000K) for the right atmosphere"
- "My article should be read in grayscale to focus on the words, not colors"
- "My presentation needs high brightness for visibility"

**Content Consumer's Perspective**:
- "I always use warm lighting at night for my eyes"
- "I'm colorblind and need specific filters"
- "I use large text because I have visual impairment"

**The Grain Network Solution**: Both are right! Let's support both.

---

### 🔧 Three-Tier Priority System

GrainDisplay uses a **three-tier priority system**:

```
1. Local User Overrides (HIGHEST PRIORITY)
   ↓
2. Content Creator Metadata (if honored by user)
   ↓
3. Local Machine Defaults (FALLBACK)
```

**Example in EDN**:

```clojure
;; Content creator's intended settings (embedded in file)
{:grainmark-name "author/forest-sunset"
 :color-temperature 2000  ;; Warm lighting
 :display-mode :reading
 :filters [:reduce-blue-light]}

;; Local user's preferences
{:honor-external-metadata true   ;; Respect author's intent
 :force-color-temperature 2000   ;; But ALWAYS use 2000K
 :deny-list [:brightness]}       ;; Never honor brightness settings

;; Result: Uses author's settings EXCEPT brightness
;; Local user's 2000K override is redundant but works
```

---

### 📝 Activity 1: Design Your Display Preferences (15 min)

**Instructions**:
1. Think about how YOU like to view content on your devices
2. Fill out this template with YOUR preferences:

```clojure
{:my-display-preferences
 {:name "YOUR_NAME"
  
  ;; Your default settings
  :scaling 1.5              ;; 1.0 = 100%, 1.5 = 150%, 2.0 = 200%
  :color-temperature 2000   ;; 1000-6500K (warmer = lower number)
  :color-profile :srgb      ;; :srgb, :display-p3, :grayscale
  :filters []               ;; [:grayscale :sepia :high-contrast]
  
  ;; Do you respect content creator settings?
  :honor-external-metadata true  ;; true or false?
  
  ;; What settings do you ALWAYS force?
  :force-overrides
  {:color-temperature 2000}  ;; Always warm lighting?
  
  ;; What settings will you NEVER let others control?
  :deny-list [:brightness]}}  ;; Your screen, your brightness!
```

**Discussion Questions**:
- Why might you want warm lighting (low color temperature)?
- When would you want to ignore a creator's settings?
- What's the difference between `:deny-list` and `:force-overrides`?

---

### 🌍 Activity 2: Grainclay Immutable Paths (15 min)

Content creators can embed display metadata in their files using **Grainclay** (immutable paths with neovedic timestamps):

```clojure
{:grainmark-name "kae3g/forest-sunset"
 :immutable-path "12025-10-22--1830--PST--moon-vishakha--09thhouse18--kae3g"
 :grainclay-url "/12025-10-22--1830--PST--moon-vishakha--09thhouse18--kae3g/forest-sunset.grainmark"
 :color-temperature 2000
 :intended-for [:desktop :graindroid-ink]}
```

**Key Concepts**:
- **Immutable**: Once published, the path and settings never change
- **Neovedic Timestamp**: Includes date, time, timezone, moon phase (Vishakha), and author
- **URL-Safe**: Can be used as a web link
- **Self-Describing**: The path tells you when and where it was created

**Exercise**: Create a Grainmark name for YOUR content:
1. Your username: ________________
2. Content name: ________________  
3. Neovedic timestamp (use today's date): ________________
4. Full Grainmark: username/content-name

---

### 💡 The Philosophy: "Local Control, Global Intent"

**Grain Network Principle**:
> Content creators can express their intended viewing experience,  
> but local users always have the final say.

This respects **both**:
- 🎨 **Creative Vision** - Artists can suggest optimal viewing conditions
- 👤 **Personal Agency** - Users control their own devices

**Real-World Application**: Think about accessibility - someone with colorblindness NEEDS to override color settings, even if the creator intended specific colors.

---

## Part 2: Graincasks - AppImage Package Management (45 min)

### 📦 The Problem: Managing Applications on Linux

**Common Approaches**:

1. **APT/DNF** (system package manager)
   - ✅ Automatic updates
   - ❌ Requires root/sudo
   - ❌ Old versions in repositories
   - ❌ Can break your system

2. **Snap/Flatpak** (containerized apps)
   - ✅ Sandboxed/secure
   - ❌ Large downloads
   - ❌ Slower startup
   - ❌ More complex

3. **AppImage** (portable apps)
   - ✅ No installation needed
   - ✅ Works everywhere
   - ✅ No root required
   - ❌ Manual updates
   - ❌ No automatic desktop integration

4. **Homebrew/Linuxbrew** (package manager)
   - ✅ Works on macOS and Linux
   - ❌ Not designed for AppImages
   - ❌ Complex directory structure
   - ❌ Overkill for portable apps

---

### 🌾 The Grain Network Solution: Graincasks

**Design Decision**: Build a **purpose-built** AppImage manager instead of adapting Homebrew.

**Why?**
- AppImages are already portable - they don't need Homebrew's complexity
- AppImages have built-in update systems (zsync, AppImageUpdate)
- Desktop integration needs custom `.desktop` files
- EDN configs are simpler than Ruby formulae (what Homebrew uses)

**Graincasks Features**:
- 📝 **EDN-based cask definitions** (declarative, version-controlled)
- 🔄 **Automatic updates** via AppImageUpdate
- 🎨 **Icon management** via Grainicons
- 🖥️ **Desktop integration** (automatic `.desktop` file generation)
- 👤 **Personal overrides** (template/personal split)

---

### 📝 Activity 3: Understanding Cask Definitions (20 min)

Here's a complete cask definition for Cursor IDE:

```clojure
{:cask
 {:name "Cursor"
  :description "AI-first code editor"
  :homepage "https://cursor.com"
  :version "latest"
  
  :appimage
  {:url "https://downloader.cursor.sh/linux/appImage/x64"
   :sha256 nil  ;; Auto-verify on download
   :update-method :appimageupdate}
  
  :desktop
  {:name "Cursor"
   :generic-name "Code Editor"
   :comment "AI-first code editor"
   :exec "cursor %F"
   :icon "cursor"
   :terminal false
   :categories ["Development" "IDE"]
   :mime-types ["text/plain" "inode/directory"]}
  
  :icon
  {:source :grainicons
   :grainicon-name "cursor-grain"}
  
  :install
  {:target "~/.local/bin/cursor.appimage"
   :symlink "~/.local/bin/cursor"
   :desktop-file "~/.local/share/applications/cursor.desktop"
   :icon-dir "~/.local/share/icons"}}}
```

**Discussion Questions**:
1. What information does a cask definition contain?
2. Why use EDN instead of JSON or YAML?
3. What's the difference between `:target` and `:symlink`?
4. Why reference Grainicons for the icon instead of bundling it?

---

### 🛠️ Activity 4: Create Your Own Cask (25 min)

**Task**: Create a cask definition for an application YOU use.

**Template**:

```clojure
{:cask
 {:name "YOUR_APP_NAME"
  :description "What does this app do?"
  :homepage "https://example.com"
  
  :appimage
  {:url "https://example.com/app.AppImage"
   :update-method :appimageupdate}
  
  :desktop
  {:name "YOUR_APP_NAME"
   :exec "your-app %F"
   :icon "your-app-icon"
   :categories ["Utility"]}  ;; Categories: Development, Graphics, Network, etc.
  
  :icon
  {:source :grainicons
   :grainicon-name "your-app-icon"}}}
```

**Popular AppImages to try**:
- OBS Studio (streaming/recording)
- Blender (3D creation)
- GIMP (image editing)
- Kdenlive (video editing)
- Audacity (audio editing)

**Bonus Challenge**: Research AppImageHub.com and find an interesting AppImage to create a cask for!

---

## Part 3: Grainbarrel - Build System Design (45 min)

### 🔨 The Evolution of Build Tools

**History**:
1. **Make** (1976) - C programs, Unix
2. **Ant** (2000) - Java, XML-based
3. **Maven** (2004) - Java, XML, dependency management
4. **Gradle** (2007) - Java/Kotlin, Groovy-based
5. **Leiningen** (2009) - Clojure, slow startup
6. **Babashka** (2019) - Clojure scripting, **fast startup**
7. **Grainbarrel** (2025) - **Grain Network**, Babashka wrapper

---

### 🌾 Why Grainbarrel? (`gb` command)

**Problem**: Babashka (`bb`) is perfect, but:
- No Grain Network branding
- Can't run tasks across multiple modules easily
- No unified task registry
- Generic output (not Grain-themed)

**Solution**: Grainbarrel wrapper that adds:
- 🌾 `gb` command (Grain Barrel)
- Cross-module task execution
- Grain-themed output with colors and emojis
- Task discovery across all grainstore modules
- 100% Babashka compatible

---

### 📝 Activity 5: Command-Line Tools Analysis (15 min)

**Compare these commands**:

```bash
# Original way (Babashka)
cd grainstore/graindisplay
bb display:info

# New way (Grainbarrel)
gb display:info  # Works from anywhere!
```

**Questions**:
1. Which is easier to use? Why?
2. What does the `cd` command do?
3. Why is it helpful to run tasks "from anywhere"?
4. What does "wrapper script" mean?

---

### 🛠️ Activity 6: Understanding Wrapper Scripts (30 min)

A **wrapper script** adds features to an existing tool without modifying it.

**Simple Example**:

```bash
#!/usr/bin/env bash
# my-wrapper script

# Add a greeting
echo "Hello from my wrapper!"

# Pass all arguments to the original tool
original-tool "$@"
```

**Grainbarrel Wrapper Logic**:

```bash
#!/usr/bin/env bash

# Check if Babashka is installed
if ! command -v bb &> /dev/null; then
    echo "🌾 Please install Babashka first!"
    exit 1
fi

# Handle special commands
case "$1" in
    --version)
        echo "🌾 Grainbarrel v1.0.0 (Babashka vX.X.X)"
        exit 0
        ;;
    help)
        echo "🌾 Grainbarrel Help..."
        exit 0
        ;;
esac

# Pass everything to Babashka
exec bb "$@"
```

**Key Concepts**:
- `$@` = All command-line arguments
- `exec` = Replace current process with new one
- `case` = Switch statement for different commands
- Exit codes: 0 = success, 1 = error

---

### 💡 The Decision: Build vs. Adapt

**Case Study**: Should we use Linuxbrew for managing AppImages?

| Criteria | Linuxbrew | Graincasks (custom) |
|----------|-----------|---------------------|
| Already exists? | ✅ Yes | ❌ Must build |
| Designed for AppImages? | ❌ No | ✅ Yes |
| Simple for our needs? | ❌ Complex | ✅ Simple |
| Uses EDN configs? | ❌ Ruby | ✅ EDN |
| Grain Network branding? | ❌ Homebrew | ✅ Grain |

**Decision**: Build Graincasks (purpose-built tool)

**Principle**: "Purpose-Built Over Generic"
- Sometimes it's better to build the RIGHT tool than adapt the WRONG tool
- Linuxbrew is great for what it does, but AppImages aren't what it's designed for

---

### 📊 Activity 7: Build vs. Adapt Decision Matrix (15 min)

**Your Turn**: You need a tool to manage your music library.

**Options**:
1. Use iTunes/Apple Music
2. Use Spotify
3. Build your own music player

**Create a decision matrix**:

| Criteria | iTunes | Spotify | Custom Player |
|----------|--------|---------|---------------|
| Free? | | | |
| Owns your data? | | | |
| Works offline? | | | |
| Open source? | | | |
| Your preference? | | | |

**Discussion**: 
- When is it worth building your own tool?
- When should you use an existing solution?
- What are the trade-offs?

---

## Part 4: Template/Personal Split Pattern (30 min)

### 📂 The Pattern

**Grain Network uses this everywhere**:

```
project/
├── template/              # Shared defaults (version-controlled)
│   └── config.edn        # Everyone starts with this
├── personal/              # Personal overrides (NOT version-controlled)
│   └── config.personal.edn  # Your customizations
└── .gitignore            # Ignore personal/ directory
```

**Why This Works**:

1. **Share defaults**: Everyone gets good starting settings
2. **Preserve customization**: Your personal tweaks aren't lost
3. **No conflicts**: Personal files aren't in git (won't conflict with updates)
4. **Privacy**: Personal preferences stay private

---

### 🎨 Activity 8: Template/Personal Configuration (15 min)

**Scenario**: You're building a text editor for your class.

**Template Settings** (`template/editor.edn`):
```clojure
{:editor
 {:font-size 12
  :theme "light"
  :line-numbers true
  :auto-save false}}
```

**Student A's Personal Overrides** (`personal/editor.personal.edn`):
```clojure
{:overrides
 {:font-size 16      ;; Larger for better readability
  :theme "dark"}}    ;; Prefers dark mode
```

**Student B's Personal Overrides**:
```clojure
{:overrides
 {:auto-save true    ;; Doesn't want to lose work
  :line-numbers false}}  ;; Finds them distracting
```

**Questions**:
1. What's Student A's final font size? ______
2. Does Student B have line numbers? ______
3. What happens when the template updates `theme` to "dark"?
4. Why not just edit the template file directly?

---

### 💻 Activity 9: Create a .gitignore File (15 min)

The `.gitignore` file tells Git which files NOT to track.

**Example**:

```gitignore
# Personal configuration (not tracked)
personal/
*.personal.edn
config.personal.edn

# Build artifacts
target/
*.jar

# OS files
.DS_Store
Thumbs.db
```

**Your Task**: Create a `.gitignore` for a homework project that:
- Ignores your personal notes (`notes.txt`)
- Ignores compiled Python files (`*.pyc`)
- Ignores a secret API key file (`.env`)
- Tracks everything else

**Write your .gitignore here**:
```
# Your .gitignore for homework project:




```

---

## Part 5: Declarative vs. Imperative Configuration (30 min)

### 📋 Two Ways to Configure

**Imperative** (step-by-step instructions):
```bash
#!/bin/bash
# install-cursor.sh

# Download the file
wget https://example.com/cursor.AppImage -O cursor.AppImage

# Make it executable
chmod +x cursor.AppImage

# Move it to bin
mv cursor.AppImage ~/.local/bin/cursor

# Create desktop entry
echo "[Desktop Entry]" > ~/.local/share/applications/cursor.desktop
echo "Name=Cursor" >> ~/.local/share/applications/cursor.desktop
# ... 20 more lines of echo statements
```

**Declarative** (describe the desired state):
```clojure
{:cask
 {:name "Cursor"
  :appimage {:url "https://example.com/cursor.AppImage"}
  :install {:target "~/.local/bin/cursor"}
  :desktop {:name "Cursor"}}}
```

Then run: `gb cask:install cursor`

---

### 🎯 Activity 10: Imperative vs. Declarative (15 min)

**Scenario**: Setting up your phone's WiFi

**Imperative**:
1. Open Settings
2. Tap WiFi
3. Turn WiFi on
4. Select network "MyWiFi"
5. Enter password "12345"
6. Tap Connect
7. Wait for connection
8. Verify connected

**Declarative**:
```clojure
{:wifi
 {:enabled true
  :network "MyWiFi"
  :password "12345"}}
```

**Questions**:
1. Which is easier to READ and understand?
2. Which is easier to share with a friend?
3. Which can you save and reuse?
4. What happens if step 6 (Imperative) fails?

**Discussion**: Version control works better with declarative configs because you can see WHAT changed, not HOW you changed it.

---

### 💡 Real-World Connection: Infrastructure as Code

**Declarative configuration** is used by professional developers:

- **Docker Compose**: Declarative container definitions
- **Kubernetes**: Declarative cluster configuration
- **Terraform**: Declarative infrastructure (AWS, Google Cloud)
- **Ansible**: Declarative server configuration

Learning this pattern now prepares you for industry tools!

---

## Part 6: The `gb` Command - Hands-On (30 min)

### 🔧 Activity 11: Using Grainbarrel

**Try these commands** (if you have Grain Network installed):

```bash
# Show version
gb --version

# Show help
gb help

# List all modules
gb grainstore:list

# Show display info
gb display:info

# List all tasks
gb tasks:all
```

**Observe**:
- The 🌾 emoji and Grain branding
- Color-coded output
- Structured information display

---

### 📝 Activity 12: Design Your Own Tool Wrapper (15 min)

**Task**: Design a wrapper for a tool you use.

**Example Tools**:
- `python` → `py` (shorter name)
- `git` → `g` (super short)
- `npm` → `n` (node package manager)

**Template**:

```bash
#!/usr/bin/env bash
# YOUR_WRAPPER_NAME

# Check if original tool exists
if ! command -v ORIGINAL_TOOL &> /dev/null; then
    echo "Please install ORIGINAL_TOOL first!"
    exit 1
fi

# Handle special commands
case "$1" in
    --version)
        echo "My Wrapper v1.0.0"
        ORIGINAL_TOOL --version
        exit 0
        ;;
esac

# Pass through to original tool
exec ORIGINAL_TOOL "$@"
```

**Fill in**:
- YOUR_WRAPPER_NAME: ________________
- ORIGINAL_TOOL: ________________
- What special commands would you add? ________________

---

## 🎓 Key Takeaways

### **1. User Agency is Critical**
- Technology should empower users, not restrict them
- Local control is a fundamental right
- Respect both creators and consumers

### **2. Purpose-Built Tools Are Valid**
- Don't force-fit existing tools to new problems
- Sometimes building the right tool is better than adapting the wrong tool
- "Perfect tool for the job" > "tool that kinda works"

### **3. Declarative > Imperative**
- Describe WHAT you want, not HOW to get it
- Easier to read, share, and version control
- Industry standard for modern infrastructure

### **4. Template/Personal Split**
- Share good defaults
- Preserve user customization
- Avoid version control conflicts
- Respect privacy

### **5. Wrapper Scripts Add Value**
- Enhance existing tools without modifying them
- Add branding and user experience improvements
- Enable cross-module workflows
- Bridge different systems together

---

## 📚 Vocabulary

- **Wrapper Script**: A script that adds features to an existing tool
- **Declarative**: Describing desired state, not steps to achieve it
- **Imperative**: Step-by-step instructions
- **EDN**: Extensible Data Notation (Clojure's data format)
- **AppImage**: Portable Linux application format
- **Cask**: Package definition (from Homebrew Casks)
- **Template/Personal Split**: Pattern for shared defaults + user customization
- **Neovedic Timestamp**: Grain Network timestamp format with date, moon phase, and author
- **Grainclay**: Immutable URL-safe paths for content
- **Grainmark**: Named content with embedded metadata

---

## 🏠 Homework

### **Assignment 1: Personal Display Preferences**

Write your ideal display settings in EDN format:

```clojure
{:my-name "YOUR_NAME"
 :display-preferences
 {:scaling ___
  :color-temperature ___
  :filters []
  :reason "I chose these settings because..."}}
```

**Include**: A paragraph explaining WHY you chose these settings.

### **Assignment 2: Create a Cask**

Research an AppImage and create a complete cask definition for it.

Requirements:
- Find an AppImage on AppImageHub.com
- Create complete cask definition in EDN
- Explain what the app does
- Explain why someone would want to use it

### **Assignment 3: Build vs. Adapt**

Pick a problem you have with technology and decide: Should you use an existing tool or build your own?

**Format**:
1. **Problem**: (describe the problem)
2. **Existing Tools**: (list 2-3 existing solutions)
3. **Decision**: Build or Adapt?
4. **Reasoning**: (explain your choice with a decision matrix)

---

## 🔬 Extension Activities

### **For Advanced Students**:

1. **Write a Simple Wrapper Script**
   - Create a wrapper for `python` that prints "🐍 Python" before running
   - Make it executable
   - Test it with `./your-wrapper --version`

2. **Design an Icon**
   - Create an SVG icon for a Grain Network app
   - Use warm colors (gold, orange, brown, green)
   - Include the 🌾 wheat theme

3. **Research Project: Accessibility**
   - What color-blind filters exist?
   - How do screen readers work with display settings?
   - Why is user control important for accessibility?

4. **Blockchain Payments (Preview for Lesson 09)** ⭐
   - Research ICP (Internet Computer Protocol)
   - Research Solana blockchain
   - How could you accept payment for a digital service?
   - What is a "native transfer" between blockchains?

---

## 💰 **BONUS: Blockchain Micropayments Preview** (15 min)

### **Future Integration: ICP Native Transfer to Solana**

Grain Network services can accept cryptocurrency payments!

**Example: Sketch-to-ASCII Conversion Service**

```clojure
{:service "grainweb-sketch-to-ascii"
 :pricing
 {:free-tier
  {:conversions-per-day 5
   :max-resolution :grainframe-5x
   :watermark true}
  
  :paid-tier
  {:price "0.001 SOL"  ;; ~$0.10 USD
   :payment-method :icp-native-transfer-solana
   :conversions-per-day :unlimited
   :max-resolution :grainframe-20x
   :watermark false
   :high-quality true}}
 
 :payment-flow
 [{:step 1 :action "User uploads sketch photo"}
  {:step 2 :action "Preview generated with watermark (free)"}
  {:step 3 :action "User clicks 'Buy High-Quality Version'"}
  {:step 4 :action "Phantom wallet prompts for 0.001 SOL"}
  {:step 5 :action "ICP Native Transfer processes payment"}
  {:step 6 :action "High-quality graincard-10x delivered"}
  {:step 7 :action "Saved to Grainclay immutable path"}]}
```

**Key Concepts**:

1. **ICP (Internet Computer Protocol)**: Decentralized cloud platform
2. **Solana**: Fast, low-fee blockchain for payments
3. **Native Transfer**: ICP ↔ Solana bridge (cross-chain)
4. **Phantom Wallet**: Solana wallet (web/iOS/Android)
5. **Micropayments**: Tiny payments (fractions of a dollar)

**Why This Matters**:
- Artists can monetize their work directly
- No credit card fees (3% saved!)
- Instant settlement (seconds, not days)
- Works globally (no borders)
- No middlemen taking cuts

**Discussion Question**: 
- Would you pay $0.10 to convert your sketch to beautiful ASCII art?
- How is this different from buying an app on the App Store?
- What are the pros and cons of cryptocurrency payments?

**Preview Lesson 09**: We'll build a complete payment system using ICP and Solana!

---

## 🌐 Real-World Connections

### **Careers That Use These Skills**:

- **DevOps Engineer**: Declarative infrastructure configuration
- **UX Designer**: User agency and accessibility
- **Package Maintainer**: Creating package definitions
- **System Administrator**: Managing software across many machines
- **Open Source Contributor**: Understanding build systems and tooling

### **Technologies You've Learned About**:

- Babashka (Clojure scripting)
- EDN (data format)
- Bash scripting
- Git and version control
- AppImages and Linux packaging
- Desktop integration (`.desktop` files)
- Icon formats (SVG, PNG)
- Accessibility features

---

## 📖 Additional Resources

### **For Students**:
- Babashka Documentation: https://babashka.org
- AppImageHub: https://appimage.github.io
- EDN Format: https://github.com/edn-format/edn
- GNOME Accessibility: https://help.gnome.org/users/gnome-help/stable/a11y.html

### **For Teachers**:
- Full Grain Network Documentation: https://github.com/kae3g/grainkae3g
- Grainbarrel Source Code: grainstore/grainbarrel/
- Example Cask Definitions: grainstore/graincasks/casks/
- Session Notes: SESSION-805 and SESSION-806 documents

---

## 🎨 Student Template Projects

### **Template: Your Own Package Manager**

Have students create a "cask" definition for their favorite app using the template from Activity 4. Share these in class to build a class-wide library of useful applications!

### **Template: Custom Wrapper Script**

Students can create wrappers for tools they use daily:
- `hw` → homework helper that tracks assignments
- `study` → opens study materials and timer
- `code` → opens their favorite code editor with project

---

## 🌟 Grain Network Principles in This Lesson

1. **"Gr" = Global Renewable**: Sustainable technology choices
2. **Open Source**: All tools and configs are open
3. **User Agency**: Users control their own devices
4. **Declarative Configuration**: Describe what you want
5. **Template/Personal Split**: Share defaults, preserve customization
6. **Purpose-Built Tools**: Right tool for the right job

---

## 📝 Assessment Rubric

### **Understanding (40%)**:
- Can explain three-tier priority system
- Understands declarative vs. imperative
- Knows when to build vs. adapt

### **Application (40%)**:
- Creates valid EDN cask definition
- Writes functional .gitignore
- Designs display preferences with reasoning

### **Critical Thinking (20%)**:
- Build vs. adapt decision shows analysis
- Display preferences show consideration of accessibility
- Questions demonstrate engagement with user agency concepts

---

## 🎯 Class Discussion Questions

1. **Ethics**: Is it ethical for a website to force specific display settings on users?

2. **Accessibility**: How do display settings affect people with disabilities?

3. **Technology Design**: Should software respect user preferences or creator intent? Both?

4. **Open Source**: Why is it important that Grain Network tools are open source?

5. **Sustainability**: How does "Global Renewable" apply to software, not just hardware?

---

## 🌾 Lesson Summary

**What We Learned**:
- Users should control their own viewing experience
- Content creators can suggest settings, but users decide
- Purpose-built tools are sometimes better than adapting existing ones
- Declarative configuration is more maintainable than imperative scripts
- Template/personal split enables sharing while preserving customization
- Wrapper scripts can enhance tools without modifying them

**Tools We Explored**:
- GrainDisplay (display management)
- Graincasks (AppImage manager)
- Grainicons (icon library)
- Grainbarrel (`gb` command)

**Skills Gained**:
- EDN configuration file creation
- Wrapper script design
- Decision matrix for technical choices
- Understanding user agency in technology
- Accessibility awareness

---

## 🎓 Next Lesson Preview

**Lesson 09: Decentralized Package Distribution and CI/CD**

Topics:
- Deploying packages to multiple platforms (GitHub + Codeberg)
- Continuous Integration / Continuous Deployment
- Automatic mirroring between repositories
- Grainclay package distribution
- GitHub Actions and automation

**Prepare**: Think about how you would share software with friends if you couldn't use the App Store or Google Play.

---

**Teacher Notes**: This lesson bridges personal tool usage with professional software engineering practices. Emphasize that the skills learned (declarative config, version control, user agency) apply to real industry tools like Docker, Kubernetes, and Terraform.

---

**🌾 Grain Network** - Global Renewable technology for a sustainable future

*"Local control, global intent - technology that respects both vision and agency."*

