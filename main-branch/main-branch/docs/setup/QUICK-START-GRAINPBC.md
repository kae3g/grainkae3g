# Quick Start: Create grainpbc Organization

**⚡ 5-Minute Setup Guide**

---

## 🎯 **What You Need to Do**

You need to create the `grainpbc` GitHub organization manually (GitHub doesn't allow this via CLI/API).

---

## 📱 **Step 1: Create Organization (2 minutes)**

### **Option A: Desktop Browser**

1. Open this URL in your browser:
   ```
   https://github.com/settings/organizations
   ```

2. Click the green **"New organization"** button

3. Fill in the form:
   - **Organization account name:** `grainpbc`
   - **Contact email:** `kj3x39@gmail.com`
   - **This organization belongs to:** My personal account
   - **Plan:** Free (unlimited public repos)

4. Click **"Next"** and skip adding members

5. Click **"Complete setup"**

### **Option B: Mobile (if needed)**

1. Open GitHub app or mobile browser
2. Go to Settings → Organizations
3. Tap "New organization"
4. Follow same steps as desktop

---

## ✅ **Step 2: Verify Creation (30 seconds)**

Run this command to verify the organization was created:

```bash
gh api orgs/grainpbc
```

You should see JSON output with organization details. If you get an error, the organization doesn't exist yet.

---

## 🚀 **Step 3: Initialize All Repositories (2 minutes)**

Once the organization exists, run:

```bash
cd /home/xy/kae3g/grainkae3g
bb scripts/init-grainpbc-repos.bb
```

This script will automatically:
- ✅ Create 17 repositories
- ✅ Set descriptions and topics
- ✅ Initialize README files
- ✅ Push initial code
- ✅ Configure settings

**Repositories that will be created:**

**Core Libraries:**
- `clojure-s6` - s6 supervision wrapper
- `clojure-sixos` - SixOS with typo handling ⭐ **NEW!**
- `clojure-icp` - ICP/DFINITY integration
- `clotoko` - Clojure-to-Motoko transpiler
- `grain-metatypes` - Type definitions

**Platform:**
- `grainweb` - Browser + Git + AI
- `grainspace` - Unified platform
- `grainmusic` - Music streaming
- `grainconv` - Type conversion
- `grainclay` - Package manager

**Hardware:**
- `grainwriter` - E-ink device
- `graincamera` - AVIF camera
- `grainpack` - GPU jetpack

**Infrastructure:**
- `grainsource` - Version control
- `grainnetwork` - Main docs
- `grainstore` - Dependencies
- `grainnixos-qemu-ubuntu-framework16` - NixOS template

---

## 🔧 **Optional: Configure Organization Profile**

After creation, enhance the organization profile:

1. Go to `https://github.com/grainpbc`
2. Click "Settings"
3. Add:
   - **Name:** Grain PBC
   - **Description:** Public Benefit Corporation building the Grain Network
   - **URL:** `https://kae3g.github.io` (or grain.network when ready)
   - **Twitter:** @grainnetwork (when created)
   - **Avatar:** Upload Grain logo (optional)

---

## 📋 **Full Checklist**

- [ ] Visit https://github.com/settings/organizations
- [ ] Click "New organization"
- [ ] Name: `grainpbc`, Email: `kj3x39@gmail.com`, Plan: Free
- [ ] Complete setup
- [ ] Verify: `gh api orgs/grainpbc`
- [ ] Run: `bb scripts/init-grainpbc-repos.bb`
- [ ] (Optional) Configure profile
- [ ] (Optional) Upload avatar
- [ ] Done! 🎉

---

## 🆘 **Troubleshooting**

**"Organization name is already taken"**
- Someone else created `grainpbc` - try `grainpbc-official` or `grain-pbc`
- Update the script with your chosen name

**"gh api orgs/grainpbc" fails**
- Make sure you created the organization
- Check you're logged in: `gh auth status`
- Try refreshing: `gh auth refresh`

**Script fails to create repos**
- Check your GitHub token has `repo` and `admin:org` scopes
- Run `gh auth refresh -h github.com -s repo,admin:org`

---

## 🌐 **Next Steps**

After initialization:

1. **Visit organization:** https://github.com/grainpbc
2. **Star repos:** Star your own repos to boost visibility
3. **Invite collaborators:** Add team members if needed
4. **Set up CI/CD:** Configure GitHub Actions
5. **Update documentation:** Customize README files
6. **Announce:** Share on social media!

---

## 📖 **Additional Resources**

- **Detailed Guide:** `docs/setup/CREATE-GRAINPBC-ORGANIZATION.md`
- **Repository List:** `docs/infrastructure/GITHUB-REPOSITORIES.md`
- **License Info:** `grainstore/LICENSE-SUMMARY.md`

---

**Time to create:** ~5 minutes  
**Result:** 17 fully configured repositories ready for development! 🌾

