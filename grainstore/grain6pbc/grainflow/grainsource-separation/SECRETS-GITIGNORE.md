# 🔒 Secrets Management & .gitignore Strategy

**Critical Component of Template/Personal Separation**

---

## 🎯 **The Three-Layer Security Model**

### **Layer 1: Template (Public, Committed)**
```
grainstore/grainzsh/template/
├── .zshrc.template              ← Community shell config
├── config.template.edn          ← Configuration schema
├── secrets.template.edn         ← 1Password reference format
└── README.md                    ← Setup instructions
```

**✅ Safe to commit:** Contains no actual secrets, only schemas and 1Password references

### **Layer 2: Personal (Private, Gitignored)**
```
grainstore/grainzsh/personal/
├── .gitignore                   ← PROTECTS YOUR SECRETS!
└── kae3g/
    ├── .zshrc                   ← Your actual shell config
    ├── config.edn               ← Your configuration
    └── secrets.edn              ← **REAL API KEYS** (gitignored!)
```

**❌ NEVER committed:** Contains actual secrets, gitignored at multiple levels

### **Layer 3: System (Symlinked)**
```bash
# Active symlinks on Framework Ubuntu 24.04 LTS
~/.zshrc -> /home/xy/kae3g/grainkae3g/grainstore/grainzsh/personal/kae3g/.zshrc
~/.config/graintime/config.edn -> /home/xy/kae3g/grainkae3g/grainstore/graintime/personal/kae3g/config.edn
~/.config/grainenvvars/secrets.edn -> /home/xy/kae3g/grainkae3g/grainstore/grainenvvars/personal/kae3g/secrets.edn
```

**🔗 Symlinks connect:** System reads personal configs, which are gitignored

---

## 📝 **.gitignore Implementation**

### **Root grainstore/.gitignore (Workspace Level)**

```gitignore
# ====================================================================
# Grain Network - Root .gitignore
# Protects ALL personal configurations across grainstore
# ====================================================================

# Personal directories - gitignore ALL personal configs
**/personal/*/secrets.edn
**/personal/*/api-keys.edn
**/personal/*/.env
**/personal/*/private/

# Location data (latitude/longitude is PII!)
**/personal/*/config.edn

# Alternative: Ignore ALL personal dirs completely
# **/personal/

# EXCEPT: Allow personal READMEs for documentation
!**/personal/README.md
!**/personal/**/README.md

# EXCEPT: Allow template directories (always safe)
!**/template/
!**/template/**

# Node/Clojure build artifacts
node_modules/
.cpcache/
target/
.shadow-cljs/

# Editor files
.idea/
.vscode/
*.swp
*~
```

### **Module-Specific personal/.gitignore**

**Example: grainstore/grainzsh/personal/.gitignore**
```gitignore
# ====================================================================
# grainzsh - Personal Directory .gitignore
# Protects shell secrets and API keys
# ====================================================================

# Secrets files (NEVER commit these!)
**/secrets.edn
**/api-keys.edn
**/.env
**/private/

# Shell history (contains command history)
**/.zsh_history
**/.bash_history

# SSH keys (if stored here)
**/.ssh/id_*
!**/.ssh/id_*.pub

# GPG keys
**/.gnupg/

# Allow username directories structure
!kae3g/
!jen3g/

# But BLOCK their secrets
kae3g/secrets.edn
kae3g/api-keys.edn
kae3g/.env
kae3g/private/

jen3g/secrets.edn
jen3g/api-keys.edn
jen3g/.env
jen3g/private/
```

**Example: grainstore/grainenvvars/personal/.gitignore**
```gitignore
# ====================================================================
# grainenvvars - Personal Directory .gitignore
# CRITICAL: Protects ALL API keys and credentials
# ====================================================================

# All secrets files (HIGHEST PRIORITY)
**/secrets.edn
**/api-keys.edn
**/.env
**/.env.*
**/private/

# OpenAI API keys
**/openai-key.txt
**/anthropic-key.txt
**/groq-key.txt

# 1Password session tokens
**/op-session*

# Allow READMEs
!README.md
!*/README.md

# Allow username dirs
!kae3g/
!jen3g/

# But BLOCK their secrets (redundant but explicit)
kae3g/secrets.edn
kae3g/api-keys.edn
kae3g/.env
```

**Example: grainstore/graintime/personal/.gitignore**
```gitignore
# ====================================================================
# graintime - Personal Directory .gitignore
# Protects location data (lat/lon is PII!)
# ====================================================================

# Config with location data
**/config.edn

# Any location files
**/location.edn
**/coordinates.txt

# Allow READMEs
!README.md
!*/README.md
```

---

## 🔐 **Secrets Template Format (1Password Integration)**

### **Template: Safe to Commit**

**grainstore/grainenvvars/template/secrets.template.edn:**
```clojure
;; ====================================================================
;; grainenvvars - Secrets Template
;; Uses 1Password CLI references (op:// format)
;; SAFE TO COMMIT - Contains no actual secrets!
;; ====================================================================

{;; OpenAI API Key
 :openai-api-key "op://Personal/OpenAI/credential"
 
 ;; Anthropic API Key
 :anthropic-api-key "op://Personal/Anthropic/credential"
 
 ;; Groq API Key
 :groq-api-key "op://Personal/Groq/credential"
 
 ;; GitHub Personal Access Token
 :github-token "op://Personal/GitHub/token"
 
 ;; ICP Identity Principal
 :icp-identity "op://Personal/ICP/identity"
 
 ;; Hedera Account ID
 :hedera-account-id "op://Personal/Hedera/account-id"
 :hedera-private-key "op://Personal/Hedera/private-key"
 
 ;; Solana Wallet Private Key
 :solana-private-key "op://Personal/Solana/private-key"}
```

### **Personal: NEVER Commit**

**grainstore/grainenvvars/personal/kae3g/secrets.edn:**
```clojure
;; ====================================================================
;; grainenvvars - Personal Secrets (kae3g)
;; ⚠️  NEVER COMMIT THIS FILE! ⚠️
;; Contains REAL API keys and credentials
;; Gitignored by: personal/.gitignore
;; ====================================================================

{;; OpenAI API Key (REAL KEY!)
 :openai-api-key "sk-proj-abc123def456..."
 
 ;; Anthropic API Key (REAL KEY!)
 :anthropic-api-key "sk-ant-xyz789..."
 
 ;; Groq API Key (REAL KEY!)
 :groq-api-key "gsk_uvw456..."
 
 ;; GitHub Personal Access Token (REAL TOKEN!)
 :github-token "ghp_rst789xyz..."
 
 ;; ICP Identity Principal (REAL PRINCIPAL!)
 :icp-identity "principal-abc-def-ghi..."
 
 ;; Hedera Account ID and Private Key (REAL CREDENTIALS!)
 :hedera-account-id "0.0.123456"
 :hedera-private-key "302e020100300506032b6570..."
 
 ;; Solana Wallet Private Key (REAL KEY!)
 :solana-private-key "5KQwrPb..."}
```

**🚨 File permissions (additional security):**
```bash
chmod 600 grainstore/grainenvvars/personal/kae3g/secrets.edn
```

---

## 🧪 **Grainstore Manifest Integration**

### **grainstore.edn Tracks Separation Patterns**

```clojure
{:grainstore
 {:version "0.3.0"
  :timestamp "12025-10-23--..."
  
  :modules
  {:grainzsh
   {:repos {:github "https://github.com/grainpbc/grainzsh"
            :codeberg "https://codeberg.org/grainpbc/grainzsh"}
    :path "grainstore/grainzsh"
    :status :active
    
    ;; Template/Personal Separation Metadata
    :separation-pattern true
    :personal-dirs ["personal/kae3g"]
    :template-dirs ["template"]
    :gitignore-rules [;; Secrets
                      "**/personal/*/secrets.edn"
                      "**/personal/*/api-keys.edn"
                      "**/personal/*/.env"
                      ;; Shell history
                      "**/personal/**/.zsh_history"]}
   
   :graintime
   {:repos {:github "https://github.com/grainpbc/graintime"}
    :path "grainstore/graintime"
    :status :active
    
    :separation-pattern true
    :personal-dirs ["personal/kae3g"]
    :gitignore-rules [;; Location data is PII!
                      "**/personal/*/config.edn"
                      "**/personal/*/location.edn"
                      ;; API keys
                      "**/personal/*/secrets.edn"]}
   
   :grainenvvars
   {:repos {:github "https://github.com/grainpbc/grainenvvars"}
    :path "grainstore/grainenvvars"
    :status :active
    
    :separation-pattern true
    :personal-dirs ["personal/kae3g"]
    :gitignore-rules [;; ALL secrets!
                      "**/personal/*/secrets.edn"
                      "**/personal/*/api-keys.edn"
                      "**/personal/*/.env"
                      "**/personal/*/.env.*"
                      ;; 1Password sessions
                      "**/personal/*/op-session*"]}
   
   :graindisplay
   {:repos {:github "https://github.com/grainpbc/graindisplay"}
    :path "grainstore/graindisplay"
    :status :active
    
    :separation-pattern true
    :personal-dirs ["personal/kae3g"]
    :gitignore-rules [;; Personal preferences
                      "**/personal/*/config.edn"
                      "**/personal/*/theme.edn"]}
   
   :graincourse
   {:repos {:github "https://github.com/grainpbc/graincourse"}
    :path "grainstore/graincourse"
    :status :active
    
    :separation-pattern true
    :personal-dirs ["personal/*/"]  ;; Each course is a personal dir
    :gitignore-rules [;; Course drafts (optional)
                      "**/personal/**/drafts/"
                      ;; Build artifacts
                      "**/personal/**/.svelte-kit/"
                      "**/personal/**/node_modules/"]}
   
   :grainpages
   {:repos {:github "https://github.com/grainpbc/grainpages"}
    :path "grainstore/grainpages"
    :status :active
    
    :separation-pattern true
    :personal-dirs ["personal/kae3g"]
    :gitignore-rules [;; Deployed sites config
                      "**/personal/*/deployed-sites.edn"
                      ;; CI/CD secrets
                      "**/personal/*/.github-token"
                      "**/personal/*/.codeberg-token"]}}}}
```

---

## ✅ **Verification: What Gets Committed?**

### **Test 1: Check Git Status**

```bash
cd /home/xy/kae3g/grainkae3g

# Check what's staged
git status

# Check ignored files
git status --ignored

# Should show:
# Ignored files:
#   grainstore/grainenvvars/personal/kae3g/secrets.edn
#   grainstore/graintime/personal/kae3g/config.edn
#   grainstore/grainzsh/personal/kae3g/.zsh_history
```

### **Test 2: Dry-Run Commit**

```bash
# See what WOULD be committed
git add grainstore/
git status --dry-run

# Should NOT include:
# - personal/*/secrets.edn
# - personal/*/config.edn (with location data)
# - personal/*/.env
```

### **Test 3: Validate .gitignore**

```bash
# Test if secrets would be ignored
git check-ignore grainstore/grainenvvars/personal/kae3g/secrets.edn

# Should output (confirming it's ignored):
# grainstore/grainenvvars/personal/kae3g/secrets.edn
```

---

## 🔍 **Common Mistakes & Fixes**

### **Mistake 1: Committing secrets.edn**

**Symptom:**
```bash
git status
# Shows: grainstore/grainenvvars/personal/kae3g/secrets.edn
```

**Fix:**
```bash
# Add to .gitignore
echo "grainstore/grainenvvars/personal/kae3g/secrets.edn" >> .gitignore

# Remove from staging
git rm --cached grainstore/grainenvvars/personal/kae3g/secrets.edn

# If already committed, purge from history
git filter-branch --force --index-filter \
  "git rm --cached --ignore-unmatch grainstore/grainenvvars/personal/kae3g/secrets.edn" \
  --prune-empty --tag-name-filter cat -- --all
```

### **Mistake 2: No .gitignore in personal/**

**Symptom:**
```bash
ls grainstore/grainzsh/personal/.gitignore
# ls: cannot access... No such file or directory
```

**Fix:**
```bash
cd grainstore/grainzsh/personal

cat > .gitignore << 'EOF'
# Secrets
**/secrets.edn
**/api-keys.edn
**/.env

# Allow READMEs
!README.md
!*/README.md
EOF
```

### **Mistake 3: Template contains actual secrets**

**Symptom:**
```clojure
;; template/secrets.template.edn
{:openai-api-key "sk-proj-ACTUAL-KEY"}  ; ⚠️ WRONG!
```

**Fix:**
```clojure
;; template/secrets.template.edn
{:openai-api-key "op://Personal/OpenAI/credential"}  ; ✅ RIGHT!
```

---

## 🌾 **Philosophy: Defense in Depth**

### **Multiple Layers of Protection**

1. **Naming Convention:** `secrets.edn` signals danger
2. **Directory Structure:** `personal/` indicates private
3. **Module .gitignore:** Blocks at module level
4. **Root .gitignore:** Catches everything
5. **CI Validation:** Fails if secrets detected in template
6. **File Permissions:** `chmod 600` for extra security

### **Fail-Safe Design**

**Traditional approach:**
```
One .gitignore → If missed, secrets leak
```

**Grain Network approach:**
```
Multiple .gitignores → Multiple chances to catch
Template/Personal split → Structural enforcement
CI validation → Automated verification
```

---

## 📚 **See Also**

- [grainsource-separation/README.md](./README.md) - Main documentation
- [grainenvvars/README.md](../grainenvvars/README.md) - Secrets management example
- [1Password CLI Documentation](https://developer.1password.com/docs/cli)

---

**Version:** 1.0.0  
**Status:** 🔒 Security Critical  
**Author:** kae3g (Grain PBC)  
**License:** CC-BY-SA-4.0

🔐 **Your secrets stay secret. Period.**


