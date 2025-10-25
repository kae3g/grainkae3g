# 🌐 Grainweb Design
## *"Browser × Git Explorer × AI Atlas - All in One"*

**Project:** Unified web/code/AI interface  
**Inspiration:** Modern browser + GitHub + Cursor + OpenAI Atlas  
**Built With:** Clojure + Humble UI + WebView  
**License:** MIT

---

## 🎯 Vision

**Grainweb** is the ultimate interface for personal sovereignty computing. It's:

- 🌐 **A Browser** - Navigate the web (HTTP, IPFS, ICP, Urbit)
- 📂 **A Git Explorer** - Browse repositories visually
- 🤖 **An AI Atlas** - AI-powered code understanding and generation
- 🔗 **A Grainsource Client** - Native version control interface
- 📝 **A Grainwriter Sync** - Document management
- 📷 **A Grainphotos Viewer** - Photo browsing and organization

**One app. Complete control. Offline-first. AI-native.**

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                     Grainweb                            │
│                                                         │
│  ┌──────────────────────────────────────────────────┐  │
│  │          Humble UI Desktop Interface             │  │
│  │  - Tabs, Windows, Panels                        │  │
│  │  - Native OS integration                         │  │
│  │  - Keyboard shortcuts                            │  │
│  └────────┬─────────────────────────────────────────┘  │
│           │                                             │
│  ┌────────┴─────────────────────────────────────────┐  │
│  │              Core Modules                        │  │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐      │  │
│  │  │ Browser  │  │   Git    │  │    AI    │      │  │
│  │  │  Engine  │  │ Explorer │  │  Atlas   │      │  │
│  │  └──────────┘  └──────────┘  └──────────┘      │  │
│  └──────────────────────────────────────────────────┘  │
│                                                         │
│  ┌──────────────────────────────────────────────────┐  │
│  │            Integration Layer                     │  │
│  │  - grainsource (Git)                            │  │
│  │  - clojure-icp (ICP canisters)                  │  │
│  │  - clojure-photos (Photo management)            │  │
│  │  - clojure-s6 (Process supervision)             │  │
│  └──────────────────────────────────────────────────┘  │
│                                                         │
│  ┌──────────────────────────────────────────────────┐  │
│  │         Rendering & Protocol Handlers            │  │
│  │  - WebView (Chromium/WebKit)                    │  │
│  │  - IPFS gateway                                  │  │
│  │  - ICP HTTP gateway                              │  │
│  │  - Urbit ship connection                         │  │
│  └──────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
```

---

## 🌐 Feature 1: Browser

### Web Protocols

**HTTP/HTTPS:**
- Standard web browsing
- Privacy-focused (no tracking)
- Ad blocking built-in
- Tor integration (optional)

**IPFS:**
- Native IPFS support
- Browse `ipfs://` URLs
- Pin content locally
- Publish to IPFS

**ICP:**
- Native canister browsing
- `https://[canister-id].ic0.app`
- Direct canister calls
- Wallet integration

**Urbit:**
- Connect to Urbit ships
- Browse Urbit apps
- Native Urbit protocol
- Ship-to-ship communication

### Browser Features

```clojure
(ns grainweb.browser
  "Web browser engine for Grainweb"
  (:require [grainweb.webview :as webview]
            [grainweb.tabs :as tabs]
            [grainweb.history :as history]))

(defn create-browser
  "Create new browser instance"
  []
  {:tabs []
   :active-tab nil
   :history []
   :bookmarks []
   :settings {:privacy :strict
              :ad-blocking true
              :javascript true
              :cookies :first-party-only}})

(defn navigate
  "Navigate to URL"
  [browser url]
  (let [protocol (detect-protocol url)]
    (case protocol
      :http (webview/load-url url)
      :ipfs (load-ipfs url)
      :icp (load-icp-canister url)
      :urbit (load-urbit-app url)
      :file (load-local-file url))))

(defn new-tab
  "Open new tab"
  [browser url]
  (let [tab {:id (generate-id)
             :url url
             :title "Loading..."
             :favicon nil
             :webview (webview/create)}]
    (-> browser
        (update :tabs conj tab)
        (assoc :active-tab (:id tab)))))
```

**UI Features:**
- Tab management (close, reorder, pin)
- Split panes (view multiple tabs)
- Picture-in-picture
- Reader mode
- Dark mode
- Fullscreen
- Screenshot/annotation

---

## 📂 Feature 2: Git Explorer

### Visual Git Interface

**Like GitHub, but local:**

```
┌─────────────────────────────────────────────────────┐
│  Repository: grainkae3g                             │
│  ┌─────────────┐  ┌─────────────┐  ┌────────────┐ │
│  │   Files     │  │   Commits   │  │  Branches  │ │
│  └─────────────┘  └─────────────┘  └────────────┘ │
│                                                     │
│  📁 grainstore/                                     │
│    📁 clojure-icp/                                  │
│      📄 README.md                                   │
│      📄 deps.edn                                    │
│      📁 src/                                        │
│        📄 core.clj                                  │
│                                                     │
│  Current Branch: main                               │
│  Uncommitted: 3 files                               │
│                                                     │
│  [Commit] [Push] [Pull] [Sync]                     │
└─────────────────────────────────────────────────────┘
```

### Git Operations

```clojure
(ns grainweb.git
  "Git explorer for Grainweb"
  (:require [grainsource.core :as grainsource]
            [clojure.java.shell :as shell]))

(defn explore-repo
  "Open repository in explorer"
  [repo-path]
  {:path repo-path
   :branches (grainsource/branches repo-path)
   :commits (grainsource/history repo-path :limit 100)
   :files (grainsource/ls-files repo-path)
   :status (grainsource/check repo-path)
   :remotes (grainsource/sources repo-path)})

(defn commit-ui
  "Interactive commit UI"
  [repo-path]
  {:staged-files (grainsource/status repo-path :staged)
   :unstaged-files (grainsource/status repo-path :unstaged)
   :commit-message ""
   :author (grainsource/get-config "user.name")
   :email (grainsource/get-config "user.email")})

(defn visual-diff
  "Show visual diff"
  [file-path old-version new-version]
  {:file file-path
   :old old-version
   :new new-version
   :diff (compute-diff old-version new-version)
   :render :side-by-side})

(defn branch-graph
  "Visualize branch history"
  [repo-path]
  (let [commits (grainsource/history repo-path :all-branches true)]
    (render-commit-graph commits)))
```

**Features:**
- Visual file tree
- Diff viewer (side-by-side or unified)
- Commit history with graph
- Branch visualization
- Merge conflict resolution
- Interactive rebase
- Cherry-pick UI
- Blame view
- Search history

---

## 🤖 Feature 3: AI Atlas

### Code Understanding

**Like Cursor/Atlas, but integrated:**

```clojure
(ns grainweb.ai
  "AI Atlas for code understanding"
  (:require [grainweb.models :as models]
            [clojure-icp.core :as icp]))

(defn analyze-code
  "Analyze code with AI"
  [code & {:keys [model context]
           :or {model :qwen3}}]
  (let [prompt (str "Analyze this code:\n\n" code)
        response (models/generate model prompt :context context)]
    {:summary (:summary response)
     :complexity (:complexity response)
     :suggestions (:suggestions response)
     :documentation (:documentation response)}))

(defn explain-function
  "Explain what a function does"
  [function-code]
  (analyze-code function-code
                :model :gemini
                :context "Explain in simple terms"))

(defn generate-tests
  "Generate tests for code"
  [function-code]
  (let [prompt (str "Generate comprehensive tests:\n\n" function-code)]
    (models/generate :gpt-os prompt)))

(defn refactor-suggestion
  "Suggest refactoring"
  [code]
  (let [analysis (analyze-code code :model :llama)]
    (:suggestions analysis)))

(defn chat-with-codebase
  "Ask questions about codebase"
  [question codebase-path]
  (let [context (index-codebase codebase-path)
        response (models/generate :qwen3 question
                                 :context context
                                 :max-tokens 4096)]
    response))
```

**AI Features:**
- Code explanation
- Function documentation
- Test generation
- Refactoring suggestions
- Bug detection
- Performance optimization
- Security analysis
- Dependency analysis
- Code search (semantic)
- "Explain this commit"
- "What does this function do?"
- "How do I use this API?"

### AI Models Integration

**Self-hosted models:**
- Qwen3 (code generation)
- Gemini (documentation)
- Llama (code analysis)
- GPT-OS (test generation)

**Via Ollama or vLLM:**
```clojure
(defn setup-models
  "Initialize AI models"
  []
  {:qwen3 (models/load "qwen3:latest")
   :gemini (models/load "gemini:latest")
   :llama (models/load "llama3:latest")
   :gpt-os (models/load "gpt-os:latest")})
```

---

## 🎨 User Interface

### Layout

```
┌────────────────────────────────────────────────────────┐
│  🌾 Grainweb                          [_] [□] [×]      │
├────────────────────────────────────────────────────────┤
│  ← → ⟳ ☆  [URL/Search Bar]           🔒 ⚙️ 👤        │
├────┬───────────────────────────────────────────────────┤
│ 🌐 │  Main Content Area                               │
│ 📂 │  (Browser, Git Explorer, or AI Atlas)            │
│ 🤖 │                                                   │
│ 📝 │                                                   │
│ 📷 │                                                   │
│    │                                                   │
│ ── │                                                   │
│ ⚙️ │                                                   │
├────┴───────────────────────────────────────────────────┤
│  Status: Ready  |  Tabs: 3  |  RAM: 2.1GB  |  CPU: 5% │
└────────────────────────────────────────────────────────┘
```

**Sidebar Icons:**
- 🌐 Browser
- 📂 Git Explorer
- 🤖 AI Atlas
- 📝 Grainwriter (documents)
- 📷 Grainphotos (photos)
- ──── (separator)
- ⚙️ Settings

### Keyboard Shortcuts

**Navigation:**
- `Cmd+T` - New tab
- `Cmd+W` - Close tab
- `Cmd+Tab` - Switch tabs
- `Cmd+L` - Focus URL bar
- `Cmd+R` - Reload

**Git:**
- `Cmd+Shift+G` - Open Git explorer
- `Cmd+Shift+C` - Commit
- `Cmd+Shift+P` - Push
- `Cmd+Shift+D` - View diff

**AI:**
- `Cmd+K` - Ask AI
- `Cmd+Shift+K` - Explain code
- `Cmd+Shift+T` - Generate tests
- `Cmd+Shift+R` - Refactor suggestions

**Views:**
- `Cmd+1` - Browser
- `Cmd+2` - Git Explorer
- `Cmd+3` - AI Atlas
- `Cmd+4` - Grainwriter
- `Cmd+5` - Grainphotos

---

## 🔌 Integration with Grain Ecosystem

### Grainsource Integration

```clojure
(ns grainweb.integrations.grainsource
  "Grainsource integration"
  (:require [grainsource.core :as gs]))

(defn browse-repository
  "Browse repository in Grainweb"
  [repo-url]
  (let [repo (gs/harvest repo-url)]
    (open-git-explorer repo)))

(defn commit-from-browser
  "Commit changes from browser"
  [files message]
  (gs/plant {:files files
             :message message
             :author (get-user-identity)}))
```

### Grainwriter Integration

```clojure
(ns grainweb.integrations.grainwriter
  "Grainwriter document integration"
  (:require [grain-writer.core :as gw]))

(defn sync-documents
  "Sync Grainwriter documents"
  []
  (let [docs (gw/list-documents)]
    (display-document-browser docs)))

(defn edit-document
  "Edit document in Grainweb"
  [doc-id]
  (let [doc (gw/read-document doc-id)]
    (open-editor doc)))
```

### Grainphotos Integration

```clojure
(ns grainweb.integrations.grainphotos
  "Grainphotos integration"
  (:require [clojure-photos.core :as photos]))

(defn browse-photos
  "Browse photos in Grainweb"
  [directory]
  (let [photos (photos/list-photos directory)]
    (display-photo-grid photos)))

(defn view-photo-metadata
  "View photo metadata"
  [photo]
  (let [info (photos/photo-info (:path photo))]
    (display-metadata-panel info)))
```

### ICP Integration

```clojure
(ns grainweb.integrations.icp
  "ICP canister integration"
  (:require [clojure-icp.core :as icp]))

(defn browse-canister
  "Browse ICP canister"
  [canister-id]
  (let [agent (icp/create-agent)
        info (icp/get-canister-status agent :canister-id canister-id)]
    (display-canister-info info)))

(defn call-canister-method
  "Interactive canister method calling"
  [canister-id method args]
  (let [agent (icp/create-agent)
        result (icp/call agent
                        :canister-id canister-id
                        :method method
                        :args args)]
    (display-result result)))
```

---

## 🛠️ Technical Stack

### Core Technologies

**UI Framework:**
- Humble UI (Clojure native desktop)
- Skia for rendering
- OpenGL acceleration

**Web Engine:**
- WebView (platform-specific)
  - macOS: WKWebView
  - Linux: WebKitGTK
  - Windows: WebView2 (Chromium)

**Git:**
- grainsource (our Git abstraction)
- libgit2 bindings
- Native Git operations

**AI:**
- Ollama (model serving)
- vLLM (GPU acceleration via Grainpack)
- Local model storage

**Storage:**
- SQLite (history, bookmarks, cache)
- File system (documents, photos)
- ICP canisters (cloud backup)

### Dependencies

```clojure
{:deps
 {org.clojure/clojure {:mvn/version "1.11.1"}
  io.github.humbleui/humbleui {:mvn/version "latest"}
  
  ;; Grain libraries
  grainsource {:local/root "../grainsource"}
  clojure-icp {:local/root "../clojure-icp"}
  clojure-photos {:local/root "../clojure-photos"}
  clojure-s6 {:local/root "../clojure-s6"}
  
  ;; WebView
  ;; Platform-specific WebView bindings
  
  ;; Database
  org.xerial/sqlite-jdbc {:mvn/version "3.44.1.0"}
  
  ;; HTTP
  clj-http/clj-http {:mvn/version "3.12.3"}
  
  ;; Async
  org.clojure/core.async {:mvn/version "1.6.673"}}}
```

---

## 🚀 Development Roadmap

### Phase 1: Browser (Month 1-2)
- ✅ Basic WebView integration
- ✅ Tab management
- ✅ URL bar
- ✅ Bookmarks
- ✅ History
- ✅ Settings

### Phase 2: Git Explorer (Month 3-4)
- ✅ Repository browsing
- ✅ File tree
- ✅ Commit history
- ✅ Diff viewer
- ✅ Branch management
- ✅ Interactive commits

### Phase 3: AI Atlas (Month 5-6)
- ✅ Model integration (Ollama)
- ✅ Code analysis
- ✅ Explanation features
- ✅ Test generation
- ✅ Chat interface
- ✅ Semantic search

### Phase 4: Integration (Month 7-8)
- ✅ Grainsource integration
- ✅ Grainwriter integration
- ✅ Grainphotos integration
- ✅ ICP canister browsing
- ✅ Urbit ship connection

### Phase 5: Polish (Month 9-10)
- ✅ Performance optimization
- ✅ UI refinement
- ✅ Documentation
- ✅ Testing
- ✅ Package for distribution

---

## 🎯 Use Cases

### For Students

**Learning to code:**
1. Browse GitHub repositories
2. Open in Git Explorer
3. Ask AI to explain code
4. Clone and experiment
5. Commit changes
6. Push to your fork

### For Developers

**Daily workflow:**
1. Browse documentation in browser
2. Open project in Git Explorer
3. Use AI for code review
4. Generate tests with AI
5. Commit and push
6. Deploy to ICP canister

### For Writers

**Content creation:**
1. Research in browser
2. Write in Grainwriter
3. Sync documents
4. Version control with Git
5. Publish to ICP/Urbit

### For Photographers

**Photo management:**
1. Import from Graincamera
2. Browse in Grainphotos viewer
3. AI tagging and organization
4. Sync to ICP/Nostr
5. Share on decentralized web

---

## 🌟 Why Grainweb?

**One app instead of many:**
- ❌ Chrome + GitHub Desktop + VS Code + Photo Viewer
- ✅ Grainweb (all in one)

**Privacy-first:**
- No tracking
- No telemetry
- Local AI models
- Decentralized storage

**Offline-capable:**
- Works without internet
- Local Git operations
- Local AI inference
- Sync when connected

**Open source:**
- MIT licensed
- Extensible
- Community-driven
- No vendor lock-in

**AI-native:**
- Built for AI assistance
- Code understanding
- Documentation generation
- Intelligent search

---

## 📚 Technical Details

### WebView Integration

```clojure
(ns grainweb.webview
  "Platform-agnostic WebView integration"
  (:require [grainweb.platform :as platform]))

(defn create
  "Create new WebView instance"
  []
  (case (platform/os)
    :macos (create-wkwebview)
    :linux (create-webkit-gtk)
    :windows (create-webview2)))

(defn load-url
  "Load URL in WebView"
  [webview url]
  (platform/webview-load webview url))

(defn inject-js
  "Inject JavaScript into page"
  [webview js-code]
  (platform/webview-eval webview js-code))

(defn register-callback
  "Register JavaScript callback"
  [webview callback-name handler]
  (platform/webview-bind webview callback-name handler))
```

### AI Model Management

```clojure
(ns grainweb.models
  "AI model management"
  (:require [clj-http.client :as http]))

(defn generate
  "Generate with AI model"
  [model prompt & {:keys [context max-tokens temperature]
                   :or {max-tokens 2048
                        temperature 0.7}}]
  (let [request {:model (name model)
                 :prompt prompt
                 :context context
                 :max_tokens max-tokens
                 :temperature temperature}
        response (http/post "http://localhost:11434/api/generate"
                           {:body (json/generate-string request)
                            :as :json})]
    (:body response)))

(defn embed
  "Get embeddings for text"
  [text]
  (let [response (http/post "http://localhost:11434/api/embeddings"
                           {:body (json/generate-string {:model "qwen3"
                                                         :text text})
                            :as :json})]
    (:embedding (:body response))))
```

---

## 🌐 Future Vision

**Grainweb becomes:**

- The default interface for Grain Network
- A platform for building decentralized apps
- An educational tool for students
- A productivity suite for developers
- A creative workspace for artists

**Extensions possible:**
- Plugin system
- Custom themes
- Script automation
- API for external tools
- Cloud sync (via ICP)

**Imagine:**
- Students learning to code with AI assistance
- Developers working entirely offline
- Writers publishing to decentralized web
- Photographers managing collections locally
- Everyone owning their digital tools

**This is Grainweb. This is personal sovereignty computing.** 🌾

---

**Grainweb Design Document**  
**Version:** 1.0  
**Author:** kae3g (Graingalaxy)  
**Organization:** Grain PBC  
**License:** MIT

*"Browser × Git Explorer × AI Atlas - All in One"* 🌐


