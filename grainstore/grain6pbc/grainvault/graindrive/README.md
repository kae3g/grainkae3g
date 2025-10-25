# Graindrive: Clojure Google Drive Integration for GrainOS

**Neovedic Timestamp**: `12025-10-22--1522--CDT--moon-vishakha--09thhouse15--kae3g`

A comprehensive Clojure library for Google Drive integration built on the Grain Network stack, featuring real-time collaborative editing through Humble UI and web deployment.

---

## 🌾 **OVERVIEW**

**Graindrive** provides seamless Google Drive integration for GrainOS using:
- **clojure-s6**: Service supervision for Drive sync daemon
- **clojure-sixos**: System-level integration and file watching
- **Humble UI**: Beautiful native desktop interface for Drive collaboration
- **Web Deploy**: SvelteKit web interface for browser-based access
- **Babashka**: Fast scripting for Drive operations

---

## 🎯 **USE CASE: Collaborative Study Guide**

Help Jenna (UIUC junior) with her fluid dynamics midterm by:
1. Creating a shared Google Doc
2. Editing it through Cursor with AI assistance
3. Auto-syncing changes in real-time
4. Providing beautiful native/web UI for review
5. Jenna only needs to access the Google Doc - all edits sync automatically

---

## 🏗️ **ARCHITECTURE**

```
┌─────────────────────────────────────────────────────────────┐
│                    GRAINDRIVE STACK                         │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌──────────────┐         ┌──────────────┐                │
│  │   Cursor     │ ←─MCP─→ │ Google Drive │                │
│  │   + AI       │         │     API      │                │
│  └──────────────┘         └──────────────┘                │
│         ↓                         ↓                         │
│  ┌──────────────────────────────────────┐                 │
│  │      Graindrive Clojure Core         │                 │
│  │  (Google Drive API Wrapper)          │                 │
│  └──────────────────────────────────────┘                 │
│         ↓                         ↓                         │
│  ┌─────────────┐           ┌─────────────┐                │
│  │ Humble UI   │           │   Web UI    │                │
│  │ (Desktop)   │           │ (SvelteKit) │                │
│  └─────────────┘           └─────────────┘                │
│         ↓                         ↓                         │
│  ┌──────────────────────────────────────┐                 │
│  │    clojure-sixos File Watcher       │                 │
│  │  (Monitors local changes)            │                 │
│  └──────────────────────────────────────┘                 │
│         ↓                         ↓                         │
│  ┌──────────────────────────────────────┐                 │
│  │      clojure-s6 Sync Daemon         │                 │
│  │  (Bidirectional sync service)        │                 │
│  └──────────────────────────────────────┘                 │
│         ↓                         ↓                         │
│  ┌──────────────────────────────────────┐                 │
│  │        Grainclay Storage            │                 │
│  │  (Immutable version history)         │                 │
│  └──────────────────────────────────────┘                 │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 📦 **INSTALLATION**

### **Prerequisites**
```bash
# Install Node.js for MCP server
nvm install node

# Install Babashka
bash < <(curl -s https://raw.githubusercontent.com/babashka/babashka/master/install)

# Install Clojure CLI tools
curl -O https://download.clojure.org/install/linux-install-1.11.1.1403.sh
chmod +x linux-install-1.11.1.403.sh
sudo ./linux-install-1.11.1.1403.sh
```

### **Setup Google Drive API**
```bash
# Run the automated setup script
bb scripts/setup-graindrive.bb
```

This script will:
1. Guide you through Google Cloud Console setup
2. Download OAuth credentials
3. Configure MCP server
4. Set up s6 daemon
5. Initialize local sync directory

---

## 🚀 **QUICK START**

### **1. Initialize Graindrive**
```bash
# Start the Graindrive system
bb graindrive:init

# This will:
# - Start the s6 sync daemon
# - Launch MCP server
# - Open Humble UI (optional)
# - Start web server (optional)
```

### **2. Connect to Shared Document**
```bash
# Connect to Jenna's fluid dynamics study guide
bb graindrive:connect-doc "Jenna Fluid Dynamics Midterm Guide"

# Or by document ID
bb graindrive:connect-doc --id "1a2b3c4d5e6f7g8h9i0j"
```

### **3. Edit Through Cursor**
```clojure
;; In Cursor, use MCP to interact with the doc:
;; "Read the contents of 'Jenna Fluid Dynamics Midterm Guide'"
;; "Add a section on Bernoulli's Equation with examples"
;; "Update the Navier-Stokes section with clearer explanations"
```

### **4. Launch UI**
```bash
# Desktop UI (Humble)
bb graindrive:ui

# Web UI (SvelteKit)
bb graindrive:web
# Opens at http://localhost:5173
```

---

## 📁 **PROJECT STRUCTURE**

```
grainstore/graindrive/
├── README.md
├── deps.edn
├── bb.edn
├── src/
│   ├── graindrive/
│   │   ├── core.clj              # Core Google Drive API wrapper
│   │   ├── auth.clj              # OAuth authentication
│   │   ├── sync.clj              # Bidirectional sync logic
│   │   ├── mcp.clj               # MCP server integration
│   │   ├── daemon.clj            # s6 daemon management
│   │   ├── watch.clj             # File system watching (sixos)
│   │   ├── ui/
│   │   │   ├── humble.clj        # Humble UI components
│   │   │   ├── components.clj    # Shared UI components
│   │   │   └── editor.clj        # Document editor component
│   │   ├── web/
│   │   │   ├── server.clj        # Web server (Ring + Reitit)
│   │   │   ├── handlers.clj      # Request handlers
│   │   │   └── api.clj           # API endpoints
│   │   └── clay/
│   │       ├── storage.clj       # Grainclay integration
│   │       └── history.clj       # Version history
├── web/                          # SvelteKit web UI
│   ├── src/
│   │   ├── routes/
│   │   │   ├── +page.svelte      # Home page
│   │   │   └── editor/
│   │   │       └── +page.svelte  # Document editor
│   │   └── lib/
│   │       ├── api.ts            # API client
│   │       └── components/
│   │           └── Editor.svelte # Rich text editor
│   └── package.json
├── scripts/
│   ├── setup-graindrive.bb       # Initial setup script
│   ├── start-daemon.bb           # Start s6 daemon
│   └── deploy-web.bb             # Deploy web UI
├── config/
│   ├── s6/                       # s6 service definitions
│   │   ├── graindrive-sync/
│   │   │   └── run               # Sync daemon run script
│   │   └── graindrive-mcp/
│   │       └── run               # MCP server run script
│   └── credentials/
│       └── .gitignore            # Don't commit credentials!
└── data/
    ├── cache/                    # Local document cache
    └── clay/                     # Grainclay immutable storage
```

---

## 🔧 **CORE API**

### **Authentication**

```clojure
(ns graindrive.auth
  (:require [clojure.java.io :as io]
            [clojure.data.json :as json]))

(defn load-credentials
  "Load OAuth credentials from file"
  [path]
  (-> path
      slurp
      (json/read-str :key-fn keyword)))

(defn authenticate!
  "Perform OAuth flow and get access token"
  [credentials-path]
  (let [creds (load-credentials credentials-path)]
    ;; OAuth flow implementation
    ;; Returns token map
    ))

(defn refresh-token!
  "Refresh expired access token"
  [refresh-token]
  ;; Token refresh implementation
  )
```

### **Drive Operations**

```clojure
(ns graindrive.core
  (:require [graindrive.auth :as auth]))

(defn list-files
  "List files in Google Drive"
  [token & {:keys [query page-size] 
            :or {page-size 100}}]
  ;; Returns vector of file maps
  )

(defn get-file
  "Get file metadata by ID"
  [token file-id]
  ;; Returns file map
  )

(defn read-doc
  "Read Google Doc content"
  [token doc-id]
  ;; Returns document content as string
  )

(defn write-doc
  "Write content to Google Doc"
  [token doc-id content]
  ;; Updates document, returns new revision ID
  )

(defn watch-doc
  "Watch document for changes"
  [token doc-id callback]
  ;; Sets up webhook for real-time updates
  )

(defn search-files
  "Search files by query"
  [token query]
  ;; Returns matching files
  )
```

### **Sync Operations**

```clojure
(ns graindrive.sync
  (:require [graindrive.core :as drive]
            [graindrive.clay :as clay]
            [clojure.core.async :as async]))

(defn start-sync!
  "Start bidirectional sync daemon"
  [token doc-id local-path]
  (let [sync-chan (async/chan)]
    ;; Start sync loop
    ;; Returns control channel
    ))

(defn sync-to-drive
  "Sync local changes to Google Drive"
  [token doc-id local-path]
  (let [content (slurp local-path)
        remote-content (drive/read-doc token doc-id)]
    (when (not= content remote-content)
      (drive/write-doc token doc-id content)
      (clay/archive! local-path))))

(defn sync-from-drive
  "Sync Google Drive changes to local"
  [token doc-id local-path]
  (let [content (drive/read-doc token doc-id)
        local-content (slurp local-path)]
    (when (not= content local-content)
      (spit local-path content)
      (clay/archive! local-path))))

(defn bidirectional-sync
  "Perform bidirectional sync with conflict resolution"
  [token doc-id local-path]
  ;; Smart merge implementation
  )
```

### **Daemon Management (s6)**

```clojure
(ns graindrive.daemon
  (:require [clojure-s6.core :as s6]
            [graindrive.sync :as sync]))

(defn create-sync-service
  "Create s6 service definition for sync daemon"
  [doc-id local-path]
  (s6/define-service
    :name "graindrive-sync"
    :run (fn []
           (let [token (auth/get-current-token)]
             (sync/start-sync! token doc-id local-path)))
    :finish (fn [exit-code]
              (println "Sync daemon exited:" exit-code))
    :log true))

(defn start-daemon!
  "Start the Graindrive sync daemon"
  []
  (s6/start-service "graindrive-sync"))

(defn stop-daemon!
  "Stop the Graindrive sync daemon"
  []
  (s6/stop-service "graindrive-sync"))

(defn restart-daemon!
  "Restart the Graindrive sync daemon"
  []
  (s6/restart-service "graindrive-sync"))
```

### **File Watching (sixos)**

```clojure
(ns graindrive.watch
  (:require [clojure-sixos.watch :as watch]
            [graindrive.sync :as sync]))

(defn watch-file
  "Watch local file for changes and trigger sync"
  [token doc-id local-path]
  (watch/watch-path
    local-path
    {:on-modify (fn [path]
                  (println "File modified, syncing to Drive...")
                  (sync/sync-to-drive token doc-id path))
     :on-create (fn [path]
                  (println "File created, syncing to Drive...")
                  (sync/sync-to-drive token doc-id path))
     :debounce 1000})) ;; Wait 1 second before syncing
```

### **Grainclay Integration**

```clojure
(ns graindrive.clay
  (:require [grainclay.core :as clay]
            [grainneovedic.core :as neovedic]))

(defn archive!
  "Archive document version to Grainclay"
  [local-path]
  (let [timestamp (neovedic/now)
        content (slurp local-path)
        clay-path (str "grainfriends/jenna/fluid-dynamics--" timestamp ".md")]
    (clay/write clay-path content)
    (println "Archived to Grainclay:" clay-path)))

(defn list-versions
  "List all archived versions of a document"
  [doc-name]
  (clay/list-versions (str "grainfriends/jenna/" doc-name)))

(defn restore-version
  "Restore a specific version from Grainclay"
  [clay-path local-path]
  (let [content (clay/read clay-path)]
    (spit local-path content)
    (println "Restored from Grainclay:" clay-path)))
```

---

## 🎨 **HUMBLE UI COMPONENTS**

```clojure
(ns graindrive.ui.humble
  (:require [io.github.humbleui.core :as hui]
            [io.github.humbleui.ui :as ui]
            [graindrive.core :as drive]))

(defn editor-component
  "Rich text editor for Google Docs"
  [doc-state]
  (ui/column
    (ui/padding 10
      (ui/label (str "Editing: " (:title @doc-state))))
    (ui/padding 10
      (ui/text-field
        {:text (:content @doc-state)
         :on-change (fn [new-content]
                      (swap! doc-state assoc :content new-content)
                      (sync-to-drive new-content))
         :multiline true
         :font-size 14}))
    (ui/row
      (ui/button
        {:text "Save"
         :on-click (fn []
                     (drive/write-doc
                       (:token @doc-state)
                       (:doc-id @doc-state)
                       (:content @doc-state)))}
        (ui/padding 10
          (ui/label "Save to Drive")))
      (ui/button
        {:text "Refresh"
         :on-click (fn []
                     (let [content (drive/read-doc
                                     (:token @doc-state)
                                     (:doc-id @doc-state))]
                       (swap! doc-state assoc :content content)))}
        (ui/padding 10
          (ui/label "Refresh from Drive"))))))

(defn main-window
  "Main Graindrive window"
  []
  (let [doc-state (atom {:title "Jenna Fluid Dynamics Midterm Guide"
                         :doc-id "1a2b3c4d5e6f7g8h9i0j"
                         :content ""
                         :token (auth/get-current-token)})]
    (ui/window
      {:title "Graindrive - Collaborative Editor"
       :width 1200
       :height 800}
      (ui/column
        (ui/padding 20
          (editor-component doc-state))))))

(defn launch!
  "Launch the Humble UI application"
  []
  (hui/start
    (main-window)))
```

---

## 🌐 **WEB UI (SVELTE)**

### **Editor Component**

```svelte
<!-- web/src/lib/components/Editor.svelte -->
<script lang="ts">
  import { onMount } from 'svelte';
  import { api } from '$lib/api';

  export let docId: string;
  export let title: string;

  let content = '';
  let saving = false;
  let lastSaved: Date | null = null;

  async function loadContent() {
    const doc = await api.getDoc(docId);
    content = doc.content;
  }

  async function saveContent() {
    saving = true;
    try {
      await api.updateDoc(docId, content);
      lastSaved = new Date();
    } finally {
      saving = false;
    }
  }

  // Auto-save every 5 seconds
  let autoSaveInterval: number;
  onMount(() => {
    loadContent();
    autoSaveInterval = setInterval(saveContent, 5000);
    return () => clearInterval(autoSaveInterval);
  });
</script>

<div class="editor">
  <header>
    <h1>{title}</h1>
    <div class="status">
      {#if saving}
        <span class="saving">Saving...</span>
      {:else if lastSaved}
        <span class="saved">Last saved: {lastSaved.toLocaleTimeString()}</span>
      {/if}
    </div>
  </header>

  <textarea
    bind:value={content}
    placeholder="Start typing..."
    on:input={() => lastSaved = null}
  />

  <footer>
    <button on:click={saveContent} disabled={saving}>
      Save Now
    </button>
    <button on:click={loadContent}>
      Refresh
    </button>
  </footer>
</div>

<style>
  .editor {
    display: flex;
    flex-direction: column;
    height: 100vh;
    background: var(--grain-dark);
    color: var(--grain-light);
  }

  header {
    display: flex;
    justify-content: space-between;
    padding: 1rem 2rem;
    border-bottom: 2px solid var(--grain-accent);
  }

  textarea {
    flex: 1;
    padding: 2rem;
    font-size: 1rem;
    line-height: 1.6;
    background: var(--grain-dark);
    color: var(--grain-light);
    border: none;
    resize: none;
    font-family: 'JetBrains Mono', monospace;
  }

  footer {
    display: flex;
    gap: 1rem;
    padding: 1rem 2rem;
    border-top: 2px solid var(--grain-accent);
  }

  button {
    padding: 0.5rem 1rem;
    background: var(--grain-accent);
    color: var(--grain-dark);
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-weight: 600;
  }

  button:hover {
    opacity: 0.9;
  }

  button:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }

  .saving {
    color: var(--grain-warning);
  }

  .saved {
    color: var(--grain-success);
  }
</style>
```

---

## 🔄 **JENNA'S FLUID DYNAMICS GUIDE WORKFLOW**

### **1. Initial Setup**

```bash
# Create shared document structure
bb graindrive:create-shared-doc \
  --title "Jenna Fluid Dynamics Midterm Guide" \
  --share-with "jenna@illinois.edu" \
  --permissions "writer"

# This creates:
# - Google Doc shared with Jenna
# - Local sync directory
# - Grainclay archive path
# - s6 daemon service
```

### **2. Cursor Integration**

```clojure
;; In Cursor, you can now:
;; 1. Read the doc
"Read the contents of 'Jenna Fluid Dynamics Midterm Guide'"

;; 2. Add content
"Add a comprehensive section on Bernoulli's Equation including:
- Derivation from conservation of energy
- Common applications (Venturi effect, airplane wings)
- Example problems with solutions
- Common misconceptions"

;; 3. Edit existing content
"Improve the Navier-Stokes section to be more beginner-friendly"

;; 4. Create practice problems
"Generate 5 practice problems on fluid dynamics with solutions"
```

### **3. Real-Time Sync**

```
Local Changes → s6 daemon detects → Syncs to Google Drive → Jenna sees updates
Jenna's Changes → Google Drive webhook → s6 daemon fetches → Updates local copy
```

### **4. Version History**

```bash
# List all versions archived to Grainclay
bb graindrive:versions "Jenna Fluid Dynamics Midterm Guide"

# Output:
# grainfriends/jenna/fluid-dynamics--12025-10-22--1530--CDT--moon-vishakha--09thhouse15--kae3g.md
# grainfriends/jenna/fluid-dynamics--12025-10-22--1600--CDT--moon-vishakha--09thhouse16--kae3g.md
# grainfriends/jenna/fluid-dynamics--12025-10-22--1630--CDT--moon-vishakha--09thhouse17--kae3g.md

# Restore previous version if needed
bb graindrive:restore \
  "grainfriends/jenna/fluid-dynamics--12025-10-22--1530--CDT--moon-vishakha--09thhouse15--kae3g.md"
```

---

## 📝 **EXAMPLE: FLUID DYNAMICS GUIDE TEMPLATE**

```markdown
# Fluid Dynamics Midterm Study Guide
**Prepared for**: Jenna (UIUC Junior)
**Created**: 12025-10-22--1522--CDT--moon-vishakha--09thhouse15--kae3g
**Collaborators**: kae3g (via Graindrive)

---

## 1. Fundamental Concepts

### 1.1 Fluid Properties
- **Density (ρ)**: Mass per unit volume
- **Viscosity (μ)**: Resistance to flow
- **Pressure (P)**: Force per unit area

### 1.2 Conservation Laws
- **Conservation of Mass**: Continuity equation
- **Conservation of Momentum**: Navier-Stokes equations
- **Conservation of Energy**: Bernoulli's equation

---

## 2. Bernoulli's Equation

### 2.1 Derivation
[AI-generated comprehensive derivation]

### 2.2 Applications
- Venturi effect
- Airplane wings
- Pitot tubes

### 2.3 Practice Problems
[AI-generated problems with solutions]

---

## 3. Navier-Stokes Equations

[Detailed explanation with examples]

---

## 4. Exam Tips & Tricks

- Common mistakes to avoid
- Formula sheet organization
- Time management strategies

---

*This guide is collaboratively edited using Graindrive*
*Updates sync automatically to Google Drive*
```

---

## 🛠️ **BABASHKA TASKS**

```clojure
;; bb.edn
{:paths ["src"]
 :deps {org.clojure/clojure {:mvn/version "1.11.1"}
        org.clojure/core.async {:mvn/version "1.6.681"}
        org.clojure/data.json {:mvn/version "2.4.0"}
        http-kit/http-kit {:mvn/version "2.7.0"}
        metosin/reitit {:mvn/version "0.7.0-alpha7"}
        io.github.humbleui/humbleui {:mvn/version "0.1.0"}
        grainkae3g/clojure-s6 {:local/root "../clojure-s6"}
        grainkae3g/clojure-sixos {:local/root "../clojure-sixos"}
        grainkae3g/grainclay {:local/root "../grainclay"}
        grainkae3g/grainneovedic {:local/root "../grainneovedic"}}

 :tasks
 {:requires ([graindrive.core :as drive]
             [graindrive.daemon :as daemon]
             [graindrive.ui.humble :as ui]
             [clojure.edn :as edn])

  init
  {:doc "Initialize Graindrive system"
   :task (do
           (println "🌾 Initializing Graindrive...")
           (daemon/start-daemon!)
           (println "✓ Daemon started"))}

  connect-doc
  {:doc "Connect to a Google Doc by name or ID"
   :task (let [doc-name (first *command-line-args*)]
           (drive/connect-doc doc-name)
           (println "✓ Connected to:" doc-name))}

  ui
  {:doc "Launch Humble UI desktop app"
   :task (ui/launch!)}

  web
  {:doc "Start web server"
   :task (shell "cd web && npm run dev")}

  sync
  {:doc "Manually trigger sync"
   :task (drive/manual-sync)}

  versions
  {:doc "List document versions"
   :task (let [doc-name (first *command-line-args*)]
           (doseq [v (drive/list-versions doc-name)]
             (println v)))}

  restore
  {:doc "Restore a specific version"
   :task (let [version-path (first *command-line-args*)]
           (drive/restore-version version-path)
           (println "✓ Restored:" version-path))}}}
```

---

## 🔐 **SECURITY & PRIVACY**

### **Permissions**
```bash
# Jenna has writer access to the shared doc
# You have owner access
# Graindrive daemon runs with your credentials
# All synced content is private to you and Jenna
```

### **Data Flow**
```
Your Cursor → Graindrive → Google Drive → Jenna's View
Your Local → Grainclay Archive (immutable, private)
```

### **Encryption**
- OAuth tokens stored securely
- Local cache encrypted (optional)
- Grainclay archives can be GPG-encrypted

---

## 📊 **MONITORING & LOGGING**

```bash
# Watch daemon logs
bb graindrive:logs

# Check sync status
bb graindrive:status

# View recent changes
bb graindrive:changes --limit 10
```

---

## 🎓 **ACADEMIC COLLABORATION FEATURES**

### **For Students**
- Real-time collaborative editing
- Version history and rollback
- Offline editing support
- Beautiful UI (desktop or web)

### **For Tutors/Mentors**
- AI-assisted content generation
- Automatic formatting
- Reference management
- LaTeX equation support

---

## 🌾 **GRAIN NETWORK INTEGRATION**

This project demonstrates:
- **clojure-s6**: Production-ready daemon management
- **clojure-sixos**: System-level file watching
- **Humble UI**: Beautiful native desktop apps
- **Grainclay**: Immutable version history
- **Grainneovedic**: Temporal awareness with timestamps

---

## 📚 **NEXT STEPS**

```bash
# 1. Set up Google Drive API credentials
bb scripts/setup-graindrive.bb

# 2. Create Jenna's study guide doc
bb graindrive:create-shared-doc \
  --title "Jenna Fluid Dynamics Midterm Guide" \
  --share-with "jenna@illinois.edu"

# 3. Start the system
bb graindrive:init

# 4. Launch UI
bb graindrive:ui  # Desktop
# OR
bb graindrive:web  # Browser

# 5. Use Cursor to edit
# "Add comprehensive fluid dynamics content to Jenna's study guide"
```

---

**Ready to help Jenna ace her midterm with AI-powered collaborative editing!** 🌾🎓💧

---

*Part of the Grain Network Educational Platform*  
*License: MIT*  
*Created: 12025-10-22--1522--CDT--moon-vishakha--09thhouse15--kae3g*


