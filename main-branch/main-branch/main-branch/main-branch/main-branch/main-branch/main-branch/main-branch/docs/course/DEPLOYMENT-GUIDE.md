# 🚀 Grain Network Course - Deployment Guide

**How to Publish This Course**

---

## 📦 **What We Built**

A complete 13-lesson course (Lessons 00-12) covering:
- Display warmth and Wayland (practical start)
- Computer science fundamentals
- Blockchain and cryptography
- Environmental science
- Philosophy and naming

**Total Content:** ~150 pages of lessons + comprehensive index

---

## 🌐 **Repository Information**

### **Main Repository (Personal)**

**GitHub:**
- URL: `https://github.com/kae3g/grainkae3g`
- Branch: `main`
- Path: `docs/course/`
- Pages: `https://kae3g.github.io/grainkae3g/`

**Codeberg:**
- URL: `https://codeberg.org/kae3g/grainkae3g`
- Branch: `main`
- Path: `docs/course/`
- Pages: `https://kae3g.codeberg.page/grainkae3g/`

---

## 📁 **Course File Structure**

```
docs/course/
├── COURSE-INDEX.md                    ← START HERE (complete guide)
├── DEPLOYMENT-GUIDE.md                ← This file
├── lessons/
│   ├── 00-display-warmth-wayland.md          (NEW! Practical start)
│   ├── 01-harmony-of-80-and-110.md
│   ├── 02-advanced-type-systems-and-networked-data.md
│   ├── 03-dual-display-architecture-and-fundraising.md
│   ├── 04-display-management-and-build-systems.md
│   ├── 05-developer-environment-security-and-social-impact.md
│   ├── 06-data-transformations.md
│   ├── 07-prediction-markets.md
│   ├── 08-zero-knowledge-proofs-and-multichain.md
│   ├── 09-environmental-science-labs-and-data-visualization.md
│   ├── 10-solar-house-clock-and-vedic-astrology-integration.md
│   ├── 11-environmental-science-software-platform.md
│   ├── 12-grain-philosophy-visual-metaphors.md   (NEW! Philosophy)
│   └── INDEX.md
├── grain-network.md
├── index.md
└── archive/                           (previous versions)
```

---

## 🔧 **Manual Deployment Steps**

### **Step 1: Commit Changes**

```bash
cd /home/xy/kae3g/grainkae3g

# Stage all changes
git add docs/course/

# Commit with descriptive message
git commit -m "feat(course): reorganize with Lesson 00 (display warmth) first, add Lesson 12 (philosophy)

- Moved display warmth to Lesson 00 (practical entry point)
- Created comprehensive COURSE-INDEX.md
- Added Lesson 12: Grain Philosophy and Visual Metaphors
- Cleaned up duplicate/outdated lessons
- Total: 13 complete lessons (00-12)
"
```

### **Step 2: Push to Remotes**

```bash
# Push to GitHub (personal)
git push origin main

# Push to Codeberg (personal)
git push codeberg main
```

### **Step 3: Enable GitHub Pages**

1. Go to: `https://github.com/kae3g/grainkae3g/settings/pages`
2. **Source:** Deploy from a branch
3. **Branch:** `main`
4. **Folder:** `/ (root)`
5. Click **Save**

Pages will be at: `https://kae3g.github.io/grainkae3g/`

### **Step 4: Enable Codeberg Pages**

1. Create `pages` branch:
```bash
git checkout -b pages
git push codeberg pages
git checkout main
```

2. Go to: `https://codeberg.org/kae3g/grainkae3g/settings`
3. Under **Pages**, enable Codeberg Pages
4. Branch: `pages`

Pages will be at: `https://kae3g.codeberg.page/grainkae3g/`

---

## 🎓 **Creating a Standalone Course Repository**

### **Option A: Personal Course Repo**

```bash
# Create new repo for just the course
git clone https://github.com/kae3g/grainkae3g grain-course
cd grain-course

# Keep only course content
git filter-branch --subdirectory-filter docs/course -- --all

# Create new repo on GitHub
# Then push
git remote set-url origin https://github.com/kae3g/grain-network-course
git push -u origin main
```

### **Option B: Grain PBC Organization Repo**

```bash
# Same as above, but push to org
git remote add grainpbc https://github.com/grainpbc/course
git push -u grainpbc main
```

**Recommended repo name:** `grain-network-fundamentals`

---

## 📝 **Suggested Repository Names**

### **For Personal Repos:**
- `grain-network-course`
- `grain-fundamentals`
- `sustainable-tech-course`

### **For Grain PBC Organization:**
- `course` (simple!)
- `grain-network-fundamentals`
- `sustainable-technology-systems`

---

## 🌐 **Website Setup**

### **GitHub Pages Configuration**

Create `.github/workflows/pages.yml`:

```yaml
name: Deploy to GitHub Pages

on:
  push:
    branches: [ main ]

permissions:
  contents: read
  pages: write
  id-token: write

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      
      - name: Setup Pages
        uses: actions/configure-pages@v4
      
      - name: Upload artifact
        uses: actions/upload-pages-artifact@v3
        with:
          path: '.'
      
      - name: Deploy to GitHub Pages
        id: deployment
        uses: actions/deploy-pages@v4
```

### **Codeberg Pages Configuration**

Create `.woodpecker/pages.yml`:

```yaml
when:
  event: push
  branch: main

steps:
  deploy:
    image: alpine:latest
    commands:
      - echo "Pages deployed from main branch"
```

---

## 🎨 **Custom Domain Setup (Optional)**

### **For GitHub Pages:**

1. Add CNAME file:
```bash
echo "course.grainnetwork.org" > CNAME
git add CNAME
git commit -m "Add custom domain"
git push
```

2. Configure DNS:
```
A     @       185.199.108.153
A     @       185.199.109.153
A     @       185.199.110.153
A     @       185.199.111.153
CNAME course  kae3g.github.io
```

### **For Codeberg Pages:**

1. Add `.domains` file:
```bash
echo "course.grainnetwork.org" > .domains
git add .domains
git commit -m "Add custom domain"
git push codeberg pages
```

2. Configure DNS:
```
CNAME course  kae3g.codeberg.page
```

---

## 📊 **Access URLs**

### **Current Setup (in grainkae3g repo):**

**Direct Links:**
- GitHub: `https://kae3g.github.io/grainkae3g/docs/course/COURSE-INDEX.html`
- Codeberg: `https://kae3g.codeberg.page/grainkae3g/docs/course/COURSE-INDEX.html`

**Landing Pages:**
- GitHub: `https://kae3g.github.io/grainkae3g/`
- Codeberg: `https://kae3g.codeberg.page/grainkae3g/`

### **Future Standalone Course Repo:**

**If created as separate repo:**
- GitHub: `https://kae3g.github.io/grain-network-course/`
- Codeberg: `https://kae3g.codeberg.page/grain-network-course/`
- Custom: `https://course.grainnetwork.org/`

---

## 🔄 **Update Workflow**

### **For Content Updates:**

```bash
# 1. Edit lessons in docs/course/lessons/
vim docs/course/lessons/00-display-warmth-wayland.md

# 2. Update COURSE-INDEX.md if needed
vim docs/course/COURSE-INDEX.md

# 3. Commit and push
git add docs/course/
git commit -m "docs(course): update Lesson 00 with new examples"
git push origin main
git push codeberg main

# 4. Wait 1-2 minutes for Pages to rebuild
```

### **For New Lessons:**

```bash
# 1. Create new lesson file
vim docs/course/lessons/13-new-topic.md

# 2. Update COURSE-INDEX.md to include it
vim docs/course/COURSE-INDEX.md

# 3. Commit and push
git add docs/course/
git commit -m "feat(course): add Lesson 13 - New Topic"
git push origin main
git push codeberg main
```

---

## 🎯 **Quick Deploy Checklist**

- [ ] All lesson files created and organized
- [ ] COURSE-INDEX.md updated with all lessons
- [ ] DEPLOYMENT-GUIDE.md created (this file)
- [ ] Changes committed to git
- [ ] Pushed to GitHub (`origin main`)
- [ ] Pushed to Codeberg (`codeberg main`)
- [ ] GitHub Pages enabled
- [ ] Codeberg Pages enabled
- [ ] URLs tested and working
- [ ] Documentation reviewed for accuracy

---

## 🐛 **Troubleshooting**

### **Pages Not Updating?**

1. **Check build status:**
   - GitHub: Actions tab
   - Codeberg: Repository settings

2. **Force rebuild:**
```bash
# Make a trivial change
echo " " >> docs/course/COURSE-INDEX.md
git commit -am "chore: trigger pages rebuild"
git push origin main
```

3. **Clear browser cache:**
   - Hard refresh: `Ctrl + Shift + R`
   - Or use incognito mode

### **404 Errors?**

1. **Check file paths:**
   - GitHub Pages is case-sensitive
   - Use lowercase filenames
   - Check extensions (.md vs .html)

2. **Verify Pages source:**
   - Should be `main` branch
   - Root folder `/`

3. **Wait longer:**
   - First deploy can take 5-10 minutes
   - Subsequent updates: 1-2 minutes

### **Build Failures?**

1. **Check dependencies:**
```bash
cd /home/xy/kae3g/grainkae3g
bb --version
```

2. **Test build locally:**
```bash
bb writings:build-fast
```

3. **Skip build and deploy raw markdown:**
   - Markdown renders automatically on GitHub/Codeberg
   - No build step needed for basic deployment

---

## 📚 **Additional Setup**

### **Add README to Course Directory**

```bash
cat > docs/course/README.md << 'EOF'
# 🌾 Grain Network Course

**Building Sustainable Technology Systems**

## 📖 Start Here

Read **[COURSE-INDEX.md](COURSE-INDEX.md)** for complete course information.

## 🎯 Quick Links

- [Lesson 00: Display Warmth](lessons/00-display-warmth-wayland.md) - Start here!
- [All Lessons](lessons/)
- [Deployment Guide](DEPLOYMENT-GUIDE.md)

## 🌐 Live Version

- GitHub Pages: https://kae3g.github.io/grainkae3g/docs/course/
- Codeberg Pages: https://kae3g.codeberg.page/grainkae3g/docs/course/

---

**From granules to grains to THE WHOLE GRAIN** 🌾
EOF

git add docs/course/README.md
git commit -m "docs(course): add README for course directory"
```

---

## 🌾 **Philosophy of Deployment**

**Dual-Platform Strategy:**
- **GitHub** - Visibility and discoverability
- **Codeberg** - Ethical, non-corporate platform
- **Both** - Redundancy and reach

**Open by Default:**
- All content CC BY-SA 4.0
- Forkable and remix-friendly
- Contribution-welcoming

**From Granules to Grains:**
- Small commits (granules)
- Regular pushes (grains)
- Complete course (THE WHOLE GRAIN)

---

**Version:** 1.0.0  
**Date:** October 23, 2025  
**Author:** kae3g (Grain PBC)

🚀 **Ready to deploy!**
