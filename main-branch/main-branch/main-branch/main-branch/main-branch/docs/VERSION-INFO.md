# Version Information & Current State

**Last Updated**: 2025-10-11  
**Purpose**: Track current versions and context for AI assistants with older training data

---

## Current Date & NixOS Versions

**Today's Date**: October 11, 2025

### NixOS Release Schedule
- **25.05** (May 2025): ✅ **RELEASED** - Current stable
- **25.11** (November 2025): 🔄 **IN DEVELOPMENT** - Beta/RC phase (releases ~mid-November)
- **26.05** (May 2026): 📅 **FUTURE** - Development will start after 25.11 release
- **26.11** (November 2026): 📅 **FUTURE** - ~1 year out

**NixOS Release Cycle**: 
- 6-month cadence (XX.05 in May, XX.11 in November)
- Development branches open ~3 months before release
- Beta/RC period ~1 month before release

---

## Project Target Timeline

### Realistic PR Timeline

**Option A: Target 25.11 (November 2025)** ⚠️ **TOO SOON**
- Release in ~4 weeks
- Already in beta/RC phase
- Feature freeze likely in effect
- **Verdict**: Not realistic for new feature addition

**Option B: Target 26.05 (May 2026)** ✅ **REALISTIC**
- Release in ~7 months (May 2026)
- Development branch opens ~February 2026
- Gives us 3-4 months to build, test, polish
- **Verdict**: Achievable if we start soon

**Option C: Target 26.11 (November 2026)** ✅✅ **CONSERVATIVE**
- Release in ~13 months (November 2026)
- Development branch opens ~August 2026
- Gives us 10 months to build, test, polish
- More time for hardware testing, community feedback
- **Verdict**: Safest bet, highest quality outcome

**Option D: Target 27.05+ (May 2027+)** ✅✅✅ **PATIENT**
- Release in 19+ months
- No pressure, can do this right
- Time for multiple hardware tests, full community adoption
- **Verdict**: If we want to be thorough

---

## Recommended Target: 26.05 (May 2026)

### Why 26.05 is the sweet spot:
- **7 months** gives us enough time but creates urgency
- **Feb 2026** development branch opens (4 months from now)
- Realistic for working prototype + hardware testing + upstream PR
- Aligns with our 8-week intensive plan + buffer time

### Revised Timeline (October 2025 → May 2026)

**Phase 0: Research & Setup** (October 2025, 2 weeks)
- Study current NixOS s6 efforts (if any)
- Set up QEMU testing environment
- Create project structure

**Phase 1: Working Prototype** (November 2025, 4 weeks)
- Build s6 NixOS module
- Test in QEMU
- Minimal service set working

**Phase 2: Hardware Acquisition** (December 2025, 2 weeks)
- Acquire Framework 16 (purchase or loan)
- Initial hardware testing
- Document hardware-specific config

**Phase 3: Real Deployment** (January 2026, 4 weeks)
- Daily driver testing
- Performance benchmarks
- Stability testing
- Field logs

**Phase 4: Production Polish** (February 2026, 4 weeks)
- Code quality improvements
- Comprehensive documentation
- Community feedback (Discourse post)
- Address issues

**Phase 5: Upstream PR** (March-April 2026, 8 weeks)
- Submit PR to nixpkgs master
- Address review feedback
- Work with maintainers
- Iterate until merged

**Phase 6: Integration** (May 2026)
- Merged into 26.05 release
- Release notes
- Documentation in NixOS manual

**Buffer**: If we slip, 26.11 (November 2026) is our fallback

---

## Framework Laptop Context

### Framework 16 Current Status (as of October 2025)

**What we know**:
- Framework 16 (AMD) launched in 2024
- Good Linux support (mainline kernel)
- AMD 7040 series (Ryzen 7 7840HS common config)
- Open-source amdgpu driver (excellent Wayland support)
- Active community on Framework forums

**What we need to research** (Oct 2025 current state):
- [ ] Latest Framework 16 BIOS version?
- [ ] Any recent Linux compatibility improvements?
- [ ] Current recommended NixOS config from Framework?
- [ ] Any Framework-NixOS community projects?
- [ ] Framework developer program still active?

### Framework Partnership Realistic Assessment

**Timeline**: 
- Approach Framework **after** we have working prototype (January 2026+)
- Don't approach empty-handed - show them real work
- Frame as: "We've already built this, would you like to collaborate?"

**What Framework actually cares about**:
1. Does it make their customers happier?
2. Does it reduce support burden or increase it?
3. Does it align with their values (repair, ownership)?
4. Is it professionally documented?

**Our value prop (refined)**:
- "Professional-grade NixOS configuration for Framework 16"
- "Lightweight alternative (s6) reduces resource usage, extends battery"
- "Fully documented, tested, community-supported"
- "We'll maintain it and provide support"

---

## NixOS Community Context (October 2025)

### What we need to research:

**Current s6 efforts**:
- [ ] Is anyone else working on s6-NixOS?
- [ ] What's in nixpkgs for s6 currently?
- [ ] Any recent Discourse discussions about init alternatives?
- [ ] Who are the systemd/init maintainers currently?

**Recent NixOS changes we should know about**:
- [ ] Any changes to NixOS module system?
- [ ] Any changes to activation/boot process?
- [ ] Any changes to service management?
- [ ] New RFC process changes?

**Community sentiment**:
- [ ] Is there appetite for systemd alternatives?
- [ ] What are current pain points?
- [ ] Who are the key stakeholders?

### How to stay current:

1. **NixOS Discourse** (https://discourse.nixos.org/)
   - Search for "s6", "systemd alternative", "init systems"
   - Post RFC draft for community feedback
   
2. **NixOS GitHub** (https://github.com/NixOS/nixpkgs)
   - Search existing PRs for similar work
   - Check modules/services/system/ for examples
   
3. **NixOS Matrix** (#nixos:nixos.org)
   - Real-time community discussion
   - Quick questions and feedback

4. **NixOS Weekly** (https://weekly.nixos.org/)
   - Stay current on ecosystem changes

---

## Information Update Protocol

### When to update this document:

- [ ] After researching current NixOS state
- [ ] After researching Framework 16 status
- [ ] When release dates change
- [ ] When we hit major milestones
- [ ] Monthly check-ins (at minimum)

### What to track:

- **NixOS versions**: Current stable, unstable branch state
- **Framework hardware**: BIOS versions, kernel compatibility
- **Community sentiment**: Is there interest? Resistance?
- **Competing efforts**: Is anyone else building this?
- **Technical changes**: NixOS module system updates

---

## AI Assistant Notes

**For Claude/Cursor/etc working on this project**:

1. **My training data is old** - Always ask user for current version info
2. **Check this file first** - VERSION-INFO.md has current state
3. **NixOS changes fast** - 6-month release cycle means rapid evolution
4. **Ask before assuming** - "Is X still true?" is a valid question
5. **User will provide docs** - They can download current NixOS manuals, etc.

**Key questions to ask user**:
- "What's the current NixOS stable version?"
- "Has there been recent work on s6/systemd alternatives?"
- "What's the latest Framework 16 status?"
- "Are there new NixOS module system changes I should know about?"

---

## Next Actions

**Immediate** (This week):
- [ ] Research current state of s6 in nixpkgs
- [ ] Search NixOS Discourse for recent systemd alternative discussions
- [ ] Check Framework forums for latest NixOS configurations
- [ ] Verify our technical approach is still valid

**This month** (October 2025):
- [ ] Set up QEMU NixOS testing environment
- [ ] Create basic s6-supervision.nix module structure
- [ ] Test minimal service set (dbus, networking)

**Next month** (November 2025):
- [ ] Working prototype in QEMU
- [ ] Documentation started
- [ ] Community feedback solicited

---

**Remember**: We're targeting **26.05 (May 2026)** or **26.11 (November 2026)**. 
We have **7-13 months**. No rush, but steady progress.

🌱 *The grainstore grows seed by seed. The work is patient.* ❄️

# Git & Cryptographic Keys Setup Complete
