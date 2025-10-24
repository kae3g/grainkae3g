# TODO: Coldriver Theme Migration

**Branch**: `coldriver-heal`  
**Date**: 2025-10-09  
**Status**: Planning → Implementation

---

## 🎨 **Vision: The Coldriver Aesthetic**

**From**: Rhizome Valley (organic, garden, plant-based metaphors)  
**To**: Coldriver (precision, driver, mechanism, engineering excellence)

### **Core Metaphors**

| Old (Rhizome Valley) | New (Coldriver) |
|---------------------|-----------------|
| Garden / Valley | Engine / Drive System |
| Plants / Seeds | Components / Parts |
| Cultivation | Engineering |
| Organic growth | Precision assembly |
| Soil / Roots | Foundation / Base |
| Harvest | Output / Production |
| Seasons | Phases / Cycles |

### **Visual Identity**

**Coldriver represents**:
- **Precision engineering** (vs organic growth)
- **Mechanical reliability** (vs natural cycles)
- **Driver as control** (vs gardener as tender)
- **Assembly line** (vs farm field)
- **Cold steel** (vs warm earth)

**Still maintaining**:
- Mathematical bedrock (Nock)
- Formal verification
- Century-scale thinking
- Sovereignty philosophy

---

## 📝 **Phase 1: Documentation Rewrite** (Week 1)

### **Critical Files to Update**

#### **Core Documentation** (Priority 1)
- [ ] `README.md` - Complete rewrite with Coldriver metaphors
- [ ] `docs/MATHEMATICAL-CONSTITUTION.md` - Engineering precision language
- [ ] `docs/CURRICULUM-ROADMAP.md` - "Assembly line" learning path
- [ ] `docs/GRAINSTORE-STRATEGY.md` - "Parts warehouse" metaphor
- [ ] `grainstore/README.md` - Component storage system
- [ ] `grainstore/vendor/README.md` - Supplier relationships

#### **Essay Updates** (Priority 2)
- [ ] `writings/9499-valley-awaits-choose-path.md` → "The Coldriver Awaits"
- [ ] `writings/9500-what-is-a-computer.md` - Machine-first language
- [ ] `writings/9503-what-is-nock.md` - Engineering specification
- [ ] `writings/9513-personal-sovereignty-framework-stack.md` - "Control stack"
- [ ] `writings/9514-sixos-framework-16-installation.md` - "Precision installation"
- [ ] `writings/9952-sixos-introduction.md` - "Gentle Mechanic" (not Gardener)

#### **Narrative Essays** (Priority 3)
- [ ] `writings/9948-why-we-love-computers.md` - Mechanical fascination
- [ ] `writings/9949-intro-clojure-nix-ecosystem.md` - Driver ecosystem
- [ ] All 9950-9960 narrative essays - New mechanical character development

---

## 🔧 **Phase 2: Code & Configuration** (Week 2)

### **Branch & Repository**
- [x] Rename branch: `coldriver-heal` → `coldriver-heal`
- [ ] Update default branch on Codeberg web UI
- [ ] Update `.woodpecker/deploy.yml` branch references
- [ ] Update all branch mentions in docs

### **Configuration Files**
- [ ] `.config.template.edn` - Site description rewrite
- [ ] `web-app/src/lib/config.js` - Theme metadata
- [ ] `web-app/src/app.css` - Color scheme (cold steel vs warm sepia?)
- [ ] `web-app/src/routes/+page.svelte` - Landing page copy

### **Build Scripts**
- [ ] `scripts/writings_build.clj` - Update comments/descriptions
- [ ] `bb.edn` - Task descriptions use new metaphors
- [ ] All `scripts/*.bb` - Comment blocks updated

---

## 📚 **Phase 3: Essay Content Transformation** (Week 3-4)

### **Metaphor Replacement Guide**

**Find & Consider**:
```clojure
{:old-metaphors->new-metaphors
 {"garden" "engine bay"
  "valley" "drive system"
  "plant" "component"
  "seed" "part"
  "soil" "foundation"
  "root" "base layer"
  "harvest" "output"
  "cultivation" "engineering"
  "grow" "build"
  "gardener" "engineer"
  "farmer" "operator"
  "season" "phase"
  "organic" "mechanical"
  "living" "precision"}}
```

### **Writing Style Shift**

**From** (Rhizome Valley):
> *"In the valley, we discovered that the mightiest tree isn't always the strongest..."*

**To** (Coldriver):
> *"In the drive system, we learned that the most reliable component isn't always the most complex..."*

### **Character Transformations**

| Old Character | New Character | Role |
|---------------|---------------|------|
| Gentle Gardener (SixOS) | Precision Mechanic | System builder |
| Wild Priestess | Chief Engineer | Vision holder |
| Clojure & Nix Elders | Master Craftsmen | Knowledge keepers |
| Haile Selassie | Lead Architect | Strategic guide |

---

## 🎯 **Phase 4: Visual Design** (Week 4-5)

### **Color Palette Shift**

**Old (Warm Sepia)**:
- `#f5f1e8` (warm cream)
- `#8b745e` (warm brown)
- `#5a4a3a` (warm dark brown)

**New (Cold Steel)** (Consider):
- `#e8eaed` (cool light gray)
- `#5e6a78` (steel blue-gray)
- `#2a3444` (dark charcoal)

Or keep warm sepia but rebrand as "workshop warmth"?

### **Typography**
- [ ] Consider monospace fonts (engineering precision)
- [ ] Update heading styles (more mechanical)
- [ ] Code block styling (blueprint aesthetic?)

### **UI Elements**
- [ ] Navigation (dashboard vs garden path)
- [ ] Footer (assembly line vs plant lens)
- [ ] Essay cards (component cards vs seed packets)

---

## 🚀 **Phase 5: Launch & Verification** (Week 5-6)

### **Quality Checks**
- [ ] All 96 essays reviewed for metaphor consistency
- [ ] No "garden/valley" language in primary docs
- [ ] README sells the Coldriver vision
- [ ] Website reflects new theme
- [ ] All builds passing

### **Migration Notes**
- [ ] Create `COLDRIVER-THEME.md` explaining the shift
- [ ] Document why we changed (evolution, precision focus)
- [ ] Preserve Rhizome Valley history (it was Phase 0!)
- [ ] Update all external references

### **Launch**
- [ ] Commit all changes
- [ ] Push to `coldriver-heal`
- [ ] Update Codeberg Pages
- [ ] Announce theme shift (if public)

---

## 🤔 **Open Questions**

1. **Color Scheme**: Keep warm sepia or shift to cold steel?
2. **Metaphor Depth**: How complete is the shift? (80%? 100%?)
3. **Plant Lens**: Preserve as historical or fully transform?
4. **Characters**: New names or evolved versions?
5. **Nock Integration**: Still "bedrock" or "base specification"?
6. **Grainstore**: "Seed vault" → "Parts warehouse" or keep?

---

## 📊 **Progress Tracking**

### **Phase 1: Documentation** (0/15 complete)
- [ ] Core docs (0/6)
- [ ] Essay rewrites (0/6)
- [ ] Narrative updates (0/3)

### **Phase 2: Code** (1/8 complete)
- [x] Branch rename
- [ ] Config updates (0/7)

### **Phase 3: Content** (0/96 essays)
- [ ] Metaphor transformation
- [ ] Style shift
- [ ] Character evolution

### **Phase 4: Visual** (0/8 complete)
- [ ] Color palette
- [ ] Typography
- [ ] UI elements

### **Phase 5: Launch** (0/6 complete)
- [ ] Quality checks
- [ ] Documentation
- [ ] Deployment

---

## 🎯 **Immediate Next Steps** (Today!)

1. [ ] Update `docs/TODO.md` to integrate Coldriver theme
2. [ ] Rewrite `README.md` intro with Coldriver metaphors
3. [ ] Update `writings/9499-valley-awaits-choose-path.md` title
4. [ ] Test new branch on Codeberg
5. [ ] Commit initial Coldriver changes

---

**From organic valleys to precision engineering. From cultivation to construction. From gardens to drive systems.**

**The Coldriver era begins.** 🔧⚙️✨

