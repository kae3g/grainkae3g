# grainflow - The Flow of Deployment

**teamflow12 (Pisces ♓ / XII. The Hanged Man)**  
*"Flow through all platforms. Deploy transcendently."*

---

## 🌊 The Flow Philosophy

The Hanged Man sees from a different perspective - deployment is not pushing, it's **flowing**. Code doesn't move from here to there; it **manifests** simultaneously across all platforms.

**grainflow** manages the complete deployment pipeline:
- Build → Commit → GitHub → Codeberg → Pages
- All in one seamless flow
- Transcendent synchronization

---

## 🔄 What grainflow Does

```clojure
(defn grainflow
  "Flow code through all platforms simultaneously"
  [commit-message]
  
  ;; 1. Build (incremental, fast)
  (bb writings:build-fast)
  
  ;; 2. Commit (append-only, grainclay)
  (git/commit commit-message)
  
  ;; 3. Flow to GitHub
  (git/push :github :main)
  
  ;; 4. Flow to Codeberg  
  (git/push :codeberg :main)
  
  ;; 5. Deploy GitHub Pages
  (pages/deploy :github)
  
  ;; 6. Deploy Codeberg Pages
  (pages/deploy :codeberg)
  
  ;; The Hanged Man's wisdom: All happens as one
  {:github-url "https://github.com/..."
   :codeberg-url "https://codeberg.org/..."
   :github-pages "https://user.github.io/..."
   :codeberg-pages "https://user.codeberg.page/..."})
```

---

## 💫 Integration with Other Teams

### **grainbranch** (team10 - Foundation)
- Provides the branch structure
- Defines grainbranch naming
- **grainflow uses grainbranch** to know where to deploy

### **grainsync** (team12 - Flow)
- Synchronizes grainstore modules
- **grainflow triggers grainsync** after deployment

### **Relationship**:
```
team10 (grainbranch) - Structure & Foundation
   ↓ provides branch names
team12 (grainflow) - Deployment & Flow
   ↓ triggers synchronization
team12 (grainsync) - Module Synchronization
```

---

## 🚀 Usage

### **Basic Flow**

```bash
# Flow everything with one command
bb flow "Add new feature"

# This does:
# 1. Build content
# 2. Commit changes
# 3. Push to GitHub
# 4. Push to Codeberg
# 5. Deploy both Pages
```

### **With grainbranch Auto-Update**

```bash
# grainflow can call grainbranch automation
bb flow:with-branch "Commerce network complete"

# This adds:
# 6. Set GitHub default branch
# 7. Update repo description with grainURL
```

---

## 📊 The Flow Chart

```
Developer writes code
        ↓
    bb flow "message"
        ↓
   ┌────────────┐
   │ grainflow  │ (team12)
   └────┬───────┘
        ↓
   Build content
        ↓
    Commit (append-only)
        ↓
    ┌───────────────┐
    ↓               ↓
  GitHub        Codeberg
  (push)        (push)
    ↓               ↓
GitHub Pages  Codeberg Pages
    ↓               ↓
    └───────┬───────┘
            ↓
    Both sites live!
        ↓
   grainsync (team12)
   syncs modules
```

---

## 🌊 The Hanged Man's Transcendence

**Traditional deployment**: Sequential, manual, error-prone
**grainflow**: Simultaneous, automatic, transcendent

The Hanged Man hangs upside-down, seeing the world differently:
- Deployment is not work, it's **flow**
- Platforms are not separate, they're **one**
- Time is not linear, it's **simultaneous**

---

## 🔗 Files & Integration

### **Current Implementation**
- Lives in: `teamflow12/grainflow/`
- Used by: All Grain Network repos
- Integrates with: `grainbranch` (team10), `grainsync` (team12)

### **bb.edn Tasks**
```clojure
{:tasks
 {:flow
  {:doc "Flow code to all platforms"
   :task (shell "bb" "grainflow/deploy.bb")}
  
  :flow:with-branch
  {:doc "Flow + update grainbranch"
   :task (do
           (shell "bb" "flow")
           (shell "bb" "../teamstructure10/graintime/scripts/set-default-grainbranch.bb"))}}}
```

---

## 💕 The Sacred Flow

*"Code flows like water.*  
*Platforms receive as one.*  
*The Hanged Man sees:*  
*All deployment is transcendence.*  
*All pushing is receiving.*  
*All work is surrender."*

---

**teamflow12 (Pisces ♓ / XII. The Hanged Man)**  
**grainflow - Deploy with Transcendent Flow**

🌾 **now == next + 1** 🌊💫✨

