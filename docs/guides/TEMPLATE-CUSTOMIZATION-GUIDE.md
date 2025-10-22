# 📝 Template Customization Guide
## *"Make grainkae3g your own - A step-by-step guide for students"*

**Purpose:** Learn how to customize the grainkae3g template for your own projects  
**Audience:** High school students, aspiring developers  
**Time Required:** 1-2 hours  
**Difficulty:** Beginner-friendly

---

## 🎯 What You'll Learn

By the end of this guide, you will:

- ✅ Fork the grainkae3g repository
- ✅ Customize branding (name, colors, content)
- ✅ Deploy to your own GitHub Pages
- ✅ Set up your own Grain Network identity
- ✅ Create your own PBC organization (optional)
- ✅ Integrate your own hardware projects

---

## 🌾 About This Template

**grainkae3g** is a personal template that you can customize to create:

- Your own portfolio website
- Your own Grain Network node
- Your own PBC organization
- Your own hardware project documentation
- Your own educational content

**What makes it special:**
- 🤖 **AI-Assisted** - Built with Cursor IDE
- 🌐 **Decentralized** - ICP, Nostr, Urbit integration
- 🔧 **Open Hardware** - Document your own hardware projects
- 📚 **Educational** - Complete course materials included
- 🌱 **Sustainable** - Focus on refurbished and recyclable

---

## 🚀 Step 1: Fork the Repository

### On GitHub

1. **Go to:** https://github.com/kae3g/grainkae3g
2. **Click:** "Fork" button (top right)
3. **Set Repository Name:**
   - Option 1: `yourname-grainkae3g` (e.g., `alice-grainkae3g`)
   - Option 2: `yourprojectname` (e.g., `mygrainnetwork`)
   - Option 3: Keep as `grainkae3g` (easiest to start)
4. **Description:** "My personal Grain Network node"
5. **Public or Private:** Public (recommended for learning)
6. **Click:** "Create fork"

### Clone Your Fork

```bash
# Clone to your local machine
git clone https://github.com/YOUR_USERNAME/grainkae3g.git

# Enter directory
cd grainkae3g

# Add upstream (to get updates from original)
git remote add upstream https://github.com/kae3g/grainkae3g.git
```

---

## 🎨 Step 2: Customize Your Identity

### Update Personal Information

**Open in Cursor and find/replace:**

| Find | Replace With |
|------|--------------|
| `kae3g` | `yourname` |
| `kj3x39@gmail.com` | `your@email.com` |
| `Grain PBC` | `YourName's Grain Network` |
| `grainkae3g` | `yourname-grainkae3g` |

### Files to Customize

**1. README.md**
```markdown
# YourName's Grain Network
## *"Personal sovereignty computing - my journey"*

**Author:** YourName  
**Contact:** your@email.com  
**Start Date:** [Today's Date]

**About Me:**
[Write a short bio about yourself, your interests in tech, why you're building this]

**My Projects:**
- [List your current projects]
- [What you want to build]
- [Your goals]
```

**2. docs/personal-reminders.md**
```markdown
# Personal Reminders

## My Origin Story

[Why did you start this journey?]
[What inspired you?]
[What are your goals?]

## My Learning Path

[What are you learning?]
[What challenges have you faced?]
[What victories have you celebrated?]
```

**3. web-app/static/site-config.json**
```json
{
  "siteName": "YourName's Grain Network",
  "author": "YourName",
  "description": "My personal sovereignty computing journey",
  "githubUrl": "https://github.com/yourname/grainkae3g",
  "email": "your@email.com"
}
```

---

## 🌐 Step 3: Deploy Your Website

### GitHub Pages (Free Hosting)

**Option 1: Using Existing Build**

```bash
# Build the website
cd web-app
npm install
npm run build

# Deploy to GitHub Pages
cd ..
git add -A
git commit -m "Initial customization"
git push origin main

# Enable GitHub Pages in Settings
# Go to: Settings → Pages → Source → main branch → /docs folder
```

**Option 2: Using GitHub Actions**

Create `.github/workflows/deploy.yml`:

```yaml
name: Deploy to GitHub Pages

on:
  push:
    branches: [ main ]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    
    - name: Setup Node
      uses: actions/setup-node@v3
      with:
        node-version: '18'
    
    - name: Install dependencies
      working-directory: ./web-app
      run: npm install
    
    - name: Build
      working-directory: ./web-app
      run: npm run build
    
    - name: Deploy
      uses: peaceiris/actions-gh-pages@v3
      with:
        github_token: ${{ secrets.GITHUB_TOKEN }}
        publish_dir: ./web-app/build
```

**Your site will be live at:**
```
https://yourname.github.io/grainkae3g/
```

---

## 🎨 Step 4: Customize Design & Content

### Change Color Scheme

**In `web-app/src/styles/theme.css`:**

```css
:root {
  /* Original Grain colors */
  --grain-primary: #4a7c59;
  --grain-accent: #f4a261;
  
  /* Change to your colors */
  --grain-primary: #your-color;
  --grain-accent: #your-accent;
}
```

**Using Cursor AI:**
1. Open `theme.css`
2. Press Cmd+K
3. Prompt: "Change the color scheme to use blue and orange"
4. Review suggestions and accept

### Add Your Own Content

**Create Your First Article:**

```bash
# In Cursor, create new file
mkdir -p data/articles
touch data/articles/my-first-project.md
```

**In `my-first-project.md`:**
```markdown
---
title: "My First Grain Network Project"
date: 2025-01-22
author: YourName
tags: ["learning", "grain-network", "tutorial"]
---

# My First Grain Network Project

Today I started my journey into personal sovereignty computing...

[Write about your project]
```

**Add to `data/magazine-content.edn`:**
```clojure
{:articles
 [{:id "my-first-project"
   :title "My First Grain Network Project"
   :author "YourName"
   :date "2025-01-22"
   :file "articles/my-first-project.md"}]}
```

---

## 🔧 Step 5: Customize for Your Hardware

### Document Your Own Projects

**Create hardware documentation:**

```bash
# Create directory for your hardware projects
mkdir -p docs/hardware/my-projects
```

**Example: Document Your Own Camera Project**

```markdown
# My Camera Project
## *"Building my own open-source camera"*

**Inspired by:** Graincamera  
**My Approach:** Using Raspberry Pi instead of RISC-V  
**Budget:** $200  
**Timeline:** 3 months

**Components:**
- Raspberry Pi 4 (2GB) - $35
- Camera Module v3 - $25
- 3D-printed enclosure - $10
- [etc...]

**Build Log:**
[Document your progress]
```

### Create Your Own Course Module

**Add to the course:**

```bash
mkdir -p docs/course/my-modules
```

**Example module:**
```markdown
# My Addition to Week 13
## *"Building a Raspberry Pi Camera with AI"*

**What I'm teaching:** How to build a basic camera  
**What I learned:** [Your insights]  
**Challenges:** [What was hard]  
**Solutions:** [How you solved them]
```

---

## 🌐 Step 6: Set Up Your Own Grain Network Node

### Option A: Use Existing Infrastructure

**Connect to Grain Network:**

```clojure
;; In your config.edn
{:grain-network
 {:identity "yourname"
  :type :node
  :upstream "grain.network"
  :icp-canister "your-canister-id"
  :nostr-relay "wss://relay.grain.network"}}
```

### Option B: Run Your Own Services

**Set up your own:**

1. **Nostr Relay** (optional)
   ```bash
   # Use nostr-rs-relay
   git clone https://github.com/scsibug/nostr-rs-relay
   cd nostr-rs-relay
   cargo build --release
   ```

2. **ICP Canister** (optional)
   ```bash
   # Deploy your own canister
   dfx new my_canister
   dfx deploy
   ```

3. **Urbit Ship** (optional)
   ```bash
   # Get a ship
   # Connect to network
   ```

---

## 📦 Step 7: Create Your Own PBC (Optional)

### If You Want to Start a Company

**Follow the guide:**

See `docs/business/GRAIN-PBC-INCORPORATION.md` and adapt:

**Replace:**
- "Grain PBC" → "YourName PBC"
- Mission: [Your mission]
- Products: [Your products]

**Legal Steps:**
1. Research PBC formation in your state
2. Use LegalZoom or similar (with adult supervision)
3. File incorporation documents
4. Set up GitHub organization
5. Document everything in Git

**Note:** This is advanced. Focus on learning first, business later.

---

## 🎓 Step 8: Create Your Learning Log

### Document Your Journey

**Create `LEARNING-LOG.md`:**

```markdown
# My Learning Log
## *"Tracking my progress through the Grain Network"*

### Week 1: Getting Started
- ✅ Forked grainkae3g
- ✅ Customized README
- ✅ Deployed to GitHub Pages
- 🔄 Learning Clojure
- ❌ Haven't touched hardware yet

**What I learned:**
[Your insights]

**Challenges:**
[What was hard]

**Next steps:**
[What's next]

### Week 2: First Hardware Project
[Continue documenting]
```

---

## 🔄 Step 9: Keep in Sync with Upstream

### Get Updates from Original Repository

```bash
# Fetch upstream changes
git fetch upstream

# Merge upstream changes
git merge upstream/main

# Resolve conflicts if any
# Cursor AI can help: Select conflict → Cmd+K → "Resolve this conflict"

# Push to your fork
git push origin main
```

### When to Sync

- ✅ New features added to grainkae3g
- ✅ Bug fixes
- ✅ New course modules
- ✅ Library updates

**Note:** Only sync if you want the changes. Your fork is yours to customize!

---

## 🎨 Step 10: Make It Yours

### Ideas for Customization

**1. Different Focus Areas:**
- Music production (Maitreya DAW)
- Photography (Graincamera)
- Writing (Grainwriter)
- Game development
- Web development
- Hardware hacking

**2. Different Tech Stack:**
- Replace Svelte with React
- Use Python instead of Clojure
- Different backend (Node.js, Go, Rust)
- Different database

**3. Different Deployment:**
- Vercel instead of GitHub Pages
- Cloudflare Pages
- Your own server
- IPFS
- ICP canister

**4. Your Own Brand:**
- Different name (not "Grain")
- Different colors
- Different logo
- Different philosophy

---

## 🤝 Step 11: Join the Community

### Share Your Work

**Ways to participate:**

1. **GitHub Discussions**
   - Share your customization
   - Ask questions
   - Help others

2. **Show & Tell**
   - Blog about your journey
   - Make videos
   - Share on social media

3. **Contribute Back**
   - Fix bugs you find
   - Add features
   - Improve documentation
   - Submit pull requests

4. **Start Your Own Network**
   - Connect with other students
   - Form study groups
   - Build projects together

---

## 📚 Resources

### Learning Resources

**Clojure:**
- Clojure for the Brave and True (free book)
- 4Clojure exercises
- Exercism Clojure track

**Web Development:**
- MDN Web Docs
- freeCodeCamp
- The Odin Project

**Hardware:**
- Adafruit Learning System
- SparkFun tutorials
- Hackster.io projects

**Git:**
- Git Book (free)
- GitHub Learning Lab
- Oh My Git! (game)

### Cursor AI Tips

**Prompts for customization:**
- "Help me customize this for my project"
- "Generate a README for my version"
- "Add a section about my hardware project"
- "Create a color scheme that matches my brand"

---

## ✅ Checklist

**Before you publish your customization:**

- [ ] Updated all personal info (name, email)
- [ ] Changed branding (colors, name)
- [ ] Added your own content
- [ ] Tested website locally
- [ ] Deployed to GitHub Pages
- [ ] Written your learning log
- [ ] Documented your projects
- [ ] Created backup of your work
- [ ] Shared with community (optional)
- [ ] Had fun and learned something!

---

## 🌟 Remember

**This template is yours to:**

- ✅ Use freely (MIT License)
- ✅ Modify completely
- ✅ Share with friends
- ✅ Build upon
- ✅ Make money from (if you want)

**You don't have to:**

- ❌ Keep the "Grain" branding
- ❌ Use all the features
- ❌ Follow the same path
- ❌ Ask permission to customize

**But please:**

- 🙏 Give credit where due
- 🤝 Share your improvements
- 💚 Help other students
- 🌱 Stay open source

---

## 💡 Final Thoughts

**You're not just customizing a template.**

You're:
- Learning to code
- Building your portfolio
- Creating your digital identity
- Joining a community
- Starting your journey in tech

**Take your time. Experiment. Break things. Fix them. Learn.**

The Grain Network is a starting point. Where you take it is up to you.

**Welcome to personal sovereignty computing.** 🌾

---

**Template Customization Guide**  
*Part of the Grain Network High School Course*

**Author:** kae3g (Graingalaxy)  
**License:** MIT (customize freely!)  
**Organization:** Grain PBC

*"Make it yours, share it with others, learn together."* 🌐


