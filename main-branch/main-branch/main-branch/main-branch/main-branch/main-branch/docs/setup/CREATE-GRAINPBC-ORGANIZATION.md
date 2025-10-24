# Creating the grainpbc GitHub Organization

**Date:** January 22, 2025  
**Status:** 🔴 NOT YET CREATED  
**Action Required:** Manual setup via GitHub web interface

---

## 🎯 **Overview**

The `grainpbc` GitHub organization will house all official Grain Network repositories, serving as the canonical source for the ecosystem.

**Organization Details:**
- **Name:** `grainpbc`
- **Display Name:** Grain PBC
- **Type:** Public Benefit Corporation (Organization on GitHub)
- **Email:** kj3x39@gmail.com
- **Website:** https://grain.network (future)
- **Twitter:** @grainnetwork (future)
- **Owner:** kae3g

---

## 📋 **Step-by-Step Setup**

### **Step 1: Create Organization**

1. **Navigate to GitHub Organizations**
   ```
   https://github.com/settings/organizations
   ```

2. **Click "New organization"**

3. **Choose plan:**
   - Select **"Free"** plan (you can upgrade later)
   - Public repositories are unlimited on free tier

4. **Organization details:**
   - **Organization account name:** `grainpbc`
   - **Contact email:** `kj3x39@gmail.com`
   - **This organization belongs to:** Select "My personal account"

5. **Click "Next"**

6. **Add members (optional for now):**
   - Skip this step initially
   - You can add collaborators later

7. **Complete setup**

### **Step 2: Configure Organization Profile**

1. **Go to organization settings:**
   ```
   https://github.com/grainpbc
   ```

2. **Click "Settings" tab**

3. **Update profile:**
   - **Name:** `Grain PBC`
   - **Description:** `Public Benefit Corporation building the Grain Network - an open-source, decentralized platform for computing, creativity, and collaboration`
   - **URL:** `https://grain.network` (or `https://kae3g.github.io` for now)
   - **Email:** `kj3x39@gmail.com`
   - **Location:** (optional)
   - **Twitter username:** `@grainnetwork` (when created)

4. **Set organization avatar:**
   - Upload Grain Network logo (TBD - can use default for now)

### **Step 3: Configure Repository Defaults**

1. **Navigate to Settings → Member privileges**

2. **Default repository permissions:**
   - Base permissions: `Read`
   - Allow members to create repositories: `✓`

3. **Repository creation:**
   - Allow members to create public repositories: `✓`
   - Allow members to create private repositories: `✓`

### **Step 4: Verify Organization**

Once created, verify with:

```bash
gh org list
```

Should show:
```
grainpbc
```

---

## 🚀 **After Organization is Created**

Once the organization exists, run:

```bash
bb scripts/init-grainpbc-repos.bb
```

This will:
1. ✅ Create all necessary repositories
2. ✅ Set up repository descriptions and topics
3. ✅ Initialize README files
4. ✅ Configure branch protection
5. ✅ Push initial code

---

## 📦 **Repositories to Create**

The following repositories will be created automatically:

### **Core Libraries**

1. **clojure-s6**
   - Description: s6 supervision wrapper for Clojure
   - Topics: clojure, s6, supervision, sixos

2. **clojure-sixos**
   - Description: SixOS integration with intelligent typo handling
   - Topics: clojure, sixos, typo-correction, fuzzy-matching

3. **clojure-icp**
   - Description: ICP/DFINITY integration for Clojure
   - Topics: clojure, icp, dfinity, web3, canister

4. **clotoko**
   - Description: Clojure-to-Motoko transpiler for ICP development
   - Topics: clojure, motoko, transpiler, icp, compiler

5. **grain-metatypes**
   - Description: Foundational type definitions for Grain Network
   - Topics: types, grain, clojure, spec

### **Platform Components**

6. **grainweb**
   - Description: Browser + Git Explorer + AI Atlas
   - Topics: browser, git, ai, decentralized

7. **grainspace**
   - Description: Unified decentralized platform
   - Topics: decentralized, platform, web3

8. **grainmusic**
   - Description: Decentralized music streaming platform
   - Topics: music, streaming, decentralized, clotoko

9. **grainconv**
   - Description: Universal type conversion system
   - Topics: conversion, ffmpeg, types, utility

10. **grainclay**
    - Description: Networked rolling release package manager
    - Topics: package-manager, networking, rolling-release

### **Hardware Projects**

11. **grainwriter**
    - Description: E-ink writing device firmware and software
    - Topics: hardware, eink, writing, ram-only, coreboot

12. **graincamera**
    - Description: Open-hardware AVIF-compatible camera
    - Topics: hardware, camera, avif, open-source

13. **grainpack**
    - Description: External GPU jetpack for Grainwriter
    - Topics: hardware, gpu, external, amd

### **Infrastructure**

14. **grainsource**
    - Description: Git-compatible version control system
    - Topics: git, version-control, decentralized

15. **grainnetwork**
    - Description: Main Grain Network documentation and coordination
    - Topics: documentation, network, coordination

16. **grainstore**
    - Description: Verified dependencies and submodule management
    - Topics: dependencies, submodules, packages

### **Templates**

17. **grainnixos-qemu-ubuntu-framework16**
    - Description: NixOS QEMU template for Ubuntu on Framework 16
    - Topics: nixos, qemu, ubuntu, framework, template

---

## 🔒 **Security Settings**

After creating repositories:

1. **Enable branch protection on `main`:**
   - Require pull request reviews before merging
   - Require status checks to pass before merging
   - Require signed commits (optional but recommended)

2. **Set up GitHub Actions:**
   - Enable workflows for CI/CD
   - Configure secrets for deployments

3. **Security advisories:**
   - Enable Dependabot alerts
   - Enable security advisories

---

## 📄 **License**

All repositories will use **MIT License** (permissive, allowing commercial use).

---

## ✅ **Post-Creation Checklist**

- [ ] Organization created at `github.com/grainpbc`
- [ ] Profile configured with description and links
- [ ] Avatar/logo uploaded
- [ ] Member privileges configured
- [ ] Verified with `gh org list`
- [ ] Run `bb scripts/init-grainpbc-repos.bb`
- [ ] All repositories created
- [ ] Branch protection enabled
- [ ] README files initialized
- [ ] Code pushed to all repos
- [ ] Documentation updated
- [ ] Website deployed (future)

---

## 🌐 **Related Resources**

- [GitHub Organizations Documentation](https://docs.github.com/en/organizations)
- [Creating a new organization](https://docs.github.com/en/organizations/collaborating-with-groups-in-organizations/creating-a-new-organization-from-scratch)
- [Managing organization settings](https://docs.github.com/en/organizations/managing-organization-settings)

---

**Next Step:** Visit https://github.com/settings/organizations and click "New organization"

