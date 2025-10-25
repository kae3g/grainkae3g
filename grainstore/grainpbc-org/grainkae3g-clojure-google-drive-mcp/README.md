# clojure-google-drive-mcp

**Clojure bindings for Google Drive Model Context Protocol (MCP) server**

**Neovedic Timestamp**: `12025-10-22--1600--CDT--moon-vishakha--09thhouse16--kae3g`

---

## 🌾 **OVERVIEW**

A comprehensive Clojure library providing idiomatic bindings to the `@modelcontextprotocol/server-gdrive` MCP server, enabling seamless Google Drive integration in Clojure applications.

**Part of the Grain Network ecosystem.**

---

## 📦 **INSTALLATION**

### **deps.edn**
```clojure
{:deps {grainpbc/clojure-google-drive-mcp {:git/url "https://github.com/grainpbc/clojure-google-drive-mcp"
                                           :git/sha "..."}}}
```

### **Leiningen**
```clojure
[grainpbc/clojure-google-drive-mcp "0.1.0"]
```

### **Babashka**
```clojure
{:deps {grainpbc/clojure-google-drive-mcp {:local/root "../clojure-google-drive-mcp"}}}
```

---

## 🚀 **QUICK START**

```clojure
(ns my-app.core
  (:require [clojure-google-drive-mcp.core :as gdrive]))

;; Initialize with credentials
(gdrive/init! {:credentials-path "~/.config/cursor/gdrive-credentials.json"})

;; Search for files
(gdrive/search "Grain Network")
;; => [{:id "1a2b3c4d..." :name "PSEUDO.md" :mimeType "application/vnd.google-apps.document"}]

;; Read a document
(gdrive/read-doc "1a2b3c4d5e6f7g8h9i0j")
;; => "# PSEUDO.md\n\nThis is the content..."

;; Write to a document
(gdrive/write-doc "1a2b3c4d5e6f7g8h9i0j" "Updated content")
;; => {:revision "v2" :modifiedTime "2025-10-22T16:00:00Z"}

;; List recent files
(gdrive/list-files {:page-size 10 :order-by "modifiedTime desc"})
;; => [{:id "..." :name "..." :modifiedTime "..."}]
```

---

## 🏗️ **ARCHITECTURE**

```
┌─────────────────────────────────────────────────────┐
│      clojure-google-drive-mcp Library               │
├─────────────────────────────────────────────────────┤
│                                                     │
│  ┌──────────────────────────────────────┐          │
│  │   Clojure API (core.clj)             │          │
│  │  - search, read-doc, write-doc       │          │
│  │  - list-files, watch-doc             │          │
│  └──────────────────────────────────────┘          │
│                    ↓                                │
│  ┌──────────────────────────────────────┐          │
│  │   MCP Client (mcp.clj)               │          │
│  │  - JSON-RPC over stdio               │          │
│  │  - Process management                │          │
│  └──────────────────────────────────────┘          │
│                    ↓                                │
│  ┌──────────────────────────────────────┐          │
│  │   NPX Process                        │          │
│  │   @modelcontextprotocol/server-gdrive│          │
│  └──────────────────────────────────────┘          │
│                    ↓                                │
│  ┌──────────────────────────────────────┐          │
│  │   Google Drive API                   │          │
│  └──────────────────────────────────────┘          │
│                                                     │
└─────────────────────────────────────────────────────┘
```

---

## 📚 **API REFERENCE**

### **Initialization**

#### `init!`
Initialize the MCP server connection.

```clojure
(gdrive/init! {:credentials-path "path/to/credentials.json"
               :mcp-server-command "npx"
               :mcp-server-args ["-y" "@modelcontextprotocol/server-gdrive"]})
```

**Options:**
- `:credentials-path` - Path to OAuth credentials JSON
- `:mcp-server-command` - Command to run MCP server (default: "npx")
- `:mcp-server-args` - Arguments for MCP server
- `:auto-authenticate` - Auto-open browser for OAuth (default: true)

**Returns:** Connection state atom

---

### **File Operations**

#### `search`
Search for files in Google Drive.

```clojure
(gdrive/search "query string")
(gdrive/search "query string" {:max-results 50})
```

**Parameters:**
- `query` - Search query string
- `opts` - Optional map with:
  - `:max-results` - Maximum number of results (default: 100)
  - `:mime-type` - Filter by MIME type
  - `:order-by` - Sort order

**Returns:** Vector of file maps

#### `list-files`
List files in Google Drive.

```clojure
(gdrive/list-files)
(gdrive/list-files {:page-size 20 :order-by "modifiedTime desc"})
```

**Parameters:**
- `opts` - Optional map with:
  - `:page-size` - Number of files per page
  - `:order-by` - Sort field and direction
  - `:page-token` - Token for pagination

**Returns:** Map with `:files` vector and optional `:next-page-token`

#### `get-file`
Get file metadata by ID.

```clojure
(gdrive/get-file "1a2b3c4d5e6f7g8h9i0j")
```

**Returns:** File metadata map

---

### **Document Operations**

#### `read-doc`
Read Google Doc content.

```clojure
(gdrive/read-doc doc-id)
(gdrive/read-doc doc-id {:format :markdown})
```

**Parameters:**
- `doc-id` - Google Doc ID
- `opts` - Optional map with:
  - `:format` - Output format (`:plain-text`, `:markdown`, `:html`)

**Returns:** Document content as string

#### `write-doc`
Write content to Google Doc.

```clojure
(gdrive/write-doc doc-id "New content")
(gdrive/write-doc doc-id "New content" {:append true})
```

**Parameters:**
- `doc-id` - Google Doc ID
- `content` - Content to write
- `opts` - Optional map with:
  - `:append` - Append instead of replace (default: false)
  - `:format` - Input format (`:plain-text`, `:markdown`, `:html`)

**Returns:** Update result with revision info

#### `append-to-doc`
Append content to Google Doc.

```clojure
(gdrive/append-to-doc doc-id "Additional content")
```

**Returns:** Update result

---

### **Sheets Operations**

#### `read-sheet`
Read Google Sheets data.

```clojure
(gdrive/read-sheet sheet-id)
(gdrive/read-sheet sheet-id {:range "A1:D10"})
```

**Parameters:**
- `sheet-id` - Google Sheet ID
- `opts` - Optional map with:
  - `:range` - A1 notation range
  - `:sheet-name` - Specific sheet name

**Returns:** Vector of rows (each row is a vector)

#### `write-sheet`
Write data to Google Sheet.

```clojure
(gdrive/write-sheet sheet-id [["A1" "B1"] ["A2" "B2"]])
(gdrive/write-sheet sheet-id data {:range "A1" :sheet-name "Sheet1"})
```

**Returns:** Update result

---

### **Real-Time Operations**

#### `watch-doc`
Watch a document for changes.

```clojure
(gdrive/watch-doc doc-id
  (fn [change]
    (println "Document changed:" change)))
```

**Parameters:**
- `doc-id` - Document ID to watch
- `callback` - Function called on each change

**Returns:** Watch handle (use with `unwatch-doc`)

#### `unwatch-doc`
Stop watching a document.

```clojure
(gdrive/unwatch-doc watch-handle)
```

---

### **Sharing & Permissions**

#### `share-file`
Share a file with a user.

```clojure
(gdrive/share-file file-id "user@example.com" :writer)
(gdrive/share-file file-id "user@example.com" :reader {:notify true})
```

**Roles:** `:owner`, `:writer`, `:commenter`, `:reader`

**Returns:** Permission object

#### `get-permissions`
Get file permissions.

```clojure
(gdrive/get-permissions file-id)
```

**Returns:** Vector of permission maps

---

## 🔧 **CONFIGURATION**

### **Environment Variables**

```bash
# Set credentials path
export GDRIVE_CREDENTIALS_PATH="$HOME/.config/cursor/gdrive-credentials.json"

# Set MCP server command (optional)
export GDRIVE_MCP_COMMAND="npx"

# Enable debug logging
export GDRIVE_DEBUG=true
```

### **Configuration File**

Create `~/.graindrive/config.edn`:

```clojure
{:credentials-path "~/.config/cursor/gdrive-credentials.json"
 :mcp-server {:command "npx"
              :args ["-y" "@modelcontextprotocol/server-gdrive"]}
 :cache {:enabled true
         :ttl 300} ;; 5 minutes
 :logging {:level :info}}
```

---

## 🛠️ **EXAMPLES**

### **Example 1: Jenna's Study Guide**

```clojure
(ns jenna.study-guide
  (:require [clojure-google-drive-mcp.core :as gdrive]))

;; Initialize
(gdrive/init! {:credentials-path "~/.config/cursor/gdrive-credentials.json"})

;; Create a new document
(def doc (gdrive/create-doc "Jenna Fluid Dynamics Midterm Guide"))

;; Share with Jenna
(gdrive/share-file (:id doc) "jenna@illinois.edu" :writer)

;; Add content
(gdrive/write-doc (:id doc)
  "# Fluid Dynamics Midterm Study Guide
  
  ## 1. Fundamental Concepts
  - Density, viscosity, pressure
  
  ## 2. Bernoulli's Equation
  ...")

;; Read current content
(def content (gdrive/read-doc (:id doc)))

;; Append new section
(gdrive/append-to-doc (:id doc)
  "
  ## 3. Practice Problems
  1. Calculate the pressure drop in a pipe...
  ")

;; Watch for Jenna's edits
(gdrive/watch-doc (:id doc)
  (fn [change]
    (println "Jenna updated the doc!")
    (println "New content:" (:content change))))
```

### **Example 2: Automated Sync**

```clojure
(ns grain.sync
  (:require [clojure-google-drive-mcp.core :as gdrive]
            [clojure.java.io :as io]))

(defn sync-local-to-drive
  "Sync local markdown file to Google Doc"
  [local-path doc-id]
  (let [content (slurp local-path)]
    (gdrive/write-doc doc-id content)
    (println "Synced to Drive:" local-path)))

(defn sync-drive-to-local
  "Sync Google Doc to local markdown file"
  [doc-id local-path]
  (let [content (gdrive/read-doc doc-id {:format :markdown})]
    (spit local-path content)
    (println "Synced from Drive:" local-path)))

(defn bidirectional-sync
  "Perform bidirectional sync with conflict detection"
  [doc-id local-path]
  (let [local-content (slurp local-path)
        remote-content (gdrive/read-doc doc-id {:format :markdown})]
    (if (= local-content remote-content)
      (println "Already in sync")
      (do
        (println "Conflict detected - merging...")
        ;; Smart merge logic here
        ))))
```

### **Example 3: Batch Operations**

```clojure
(ns grain.batch
  (:require [clojure-google-drive-mcp.core :as gdrive]))

;; Find all Grain Network docs
(def grain-docs
  (gdrive/search "name contains 'Grain' and mimeType = 'application/vnd.google-apps.document'"))

;; Read all docs
(def all-content
  (mapv (fn [doc]
          {:name (:name doc)
           :content (gdrive/read-doc (:id doc))})
        grain-docs))

;; Backup all docs
(doseq [doc grain-docs]
  (let [content (gdrive/read-doc (:id doc))
        backup-path (str "backup/" (:name doc) ".md")]
    (io/make-parents backup-path)
    (spit backup-path content)
    (println "Backed up:" (:name doc))))
```

---

## 🔒 **SECURITY**

### **OAuth Scopes**

This library requests the following scopes:
- `https://www.googleapis.com/auth/drive.file` - Access to files created by this app
- `https://www.googleapis.com/auth/drive.readonly` - Read access (optional)

### **Credentials Storage**

```bash
# Secure your credentials
chmod 600 ~/.config/cursor/gdrive-credentials.json

# Never commit credentials
echo "gdrive-credentials.json" >> .gitignore
echo "token.json" >> .gitignore
```

### **Token Refresh**

Tokens are automatically refreshed by the MCP server. No manual intervention needed.

---

## 🧪 **TESTING**

```bash
# Run tests
bb test

# Run with coverage
bb test:coverage

# Integration tests (requires credentials)
bb test:integration
```

---

## 📊 **PERFORMANCE**

### **Caching**

```clojure
;; Enable caching for better performance
(gdrive/init! {:credentials-path "..."
               :cache {:enabled true
                       :ttl 300}}) ;; 5 minutes

;; Cache is automatically invalidated on writes
```

### **Batch Requests**

```clojure
;; Read multiple docs efficiently
(gdrive/batch-read ["doc-id-1" "doc-id-2" "doc-id-3"])
;; => [{:id "doc-id-1" :content "..."} ...]
```

---

## 🌾 **GRAIN NETWORK INTEGRATION**

### **Dependencies**
- `clojure-s6` - Daemon management for sync services
- `clojure-sixos` - File system watching
- `grainclay` - Immutable version history
- `grainneovedic` - Temporal awareness

### **Usage in Graindrive**

```clojure
(ns graindrive.core
  (:require [clojure-google-drive-mcp.core :as gdrive]
            [clojure-s6.core :as s6]
            [grainclay.core :as clay]
            [grainneovedic.core :as neovedic]))

(defn start-sync-daemon
  [doc-id local-path]
  (s6/define-service
    :name "gdrive-sync"
    :run (fn []
           (loop []
             (let [content (gdrive/read-doc doc-id)
                   timestamp (neovedic/now)
                   clay-path (str "sync/" doc-id "--" timestamp ".md")]
               (spit local-path content)
               (clay/write clay-path content)
               (Thread/sleep 5000)
               (recur))))))
```

---

## 🐛 **TROUBLESHOOTING**

### **Issue: "NPX not found"**
```bash
# Install Node.js
nvm install node
```

### **Issue: "Authentication failed"**
```bash
# Remove cached token
rm ~/.cache/@modelcontextprotocol/server-gdrive/token.json

# Re-authenticate
bb gdrive:auth
```

### **Issue: "Permission denied"**
- Check OAuth consent screen settings in Google Cloud Console
- Ensure your email is added as a test user
- Verify scopes are correctly configured

---

## 📚 **RESOURCES**

- [Google Drive API Documentation](https://developers.google.com/drive)
- [Model Context Protocol Spec](https://modelcontextprotocol.io/)
- [Grain Network Documentation](https://grain.network)
- [GitHub Repository](https://github.com/grainpbc/clojure-google-drive-mcp)

---

## 📄 **LICENSE**

MIT License

Copyright (c) 2025 Grain PBC

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

---

**Part of the Grain Network ecosystem** 🌾  
**Created**: 12025-10-22--1600--CDT--moon-vishakha--09thhouse16--kae3g  
**Maintained by**: Grain PBC  
**Organization**: https://github.com/grainpbc


