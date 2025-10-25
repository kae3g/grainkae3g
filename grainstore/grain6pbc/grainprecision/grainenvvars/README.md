# Grainenvvars: Secure Environment Variables Management

**Template/Personal Split • 1Password Integration • Zero Secrets in Git**

> *"Local Control, Global Intent - Your secrets stay yours"*  
> *"Never commit keys, always document patterns"*

Grainenvvars provides secure, standardized environment variable management for the Grain Network ecosystem with template/personal split, 1Password CLI integration, and educational best practices.

---

## 🔒 **SECURITY PHILOSOPHY**

### **Core Principles**
1. **Never Commit Secrets** - API keys, tokens, passwords NEVER go in git
2. **Template/Personal Split** - Share patterns, not values
3. **1Password Integration** - Secrets stored in encrypted vault
4. **Local Control** - Each user manages their own secrets
5. **Education First** - Teach secure practices, not just tools

### **What Goes Where**

**Template** (committed to git):
- Variable names and descriptions
- Example values (fake/placeholder)
- Documentation and best practices
- Loading scripts and utilities

**Personal** (NEVER committed):
- Real API keys and tokens
- Personal credentials
- Machine-specific values
- Private configuration

---

## 📦 **INSTALLATION**

### **Step 1: Clone Grainenvvars**

```bash
# Clone to grainstore
cd ~/kae3g/grainkae3g/grainstore
git clone https://github.com/grainpbc/grainenvvars.git

# Or as part of grainstore
cd ~/kae3g/grainkae3g
gb grainstore:sync
```

### **Step 2: Create Personal Config**

```bash
cd grainstore/grainenvvars

# Copy template as starting point
cp template/.env.example personal/.env

# Edit with your real values
nano personal/.env

# Make sure personal/.env is in .gitignore
```

### **Step 3: Source in Shell**

Add to your `.zshrc` or `.bashrc`:

```bash
# Load Grain Network environment variables
if [ -f "$HOME/kae3g/grainkae3g/grainstore/grainenvvars/personal/.env" ]; then
    export $(cat "$HOME/kae3g/grainkae3g/grainstore/grainenvvars/personal/.env" | grep -v '^#' | xargs)
fi
```

---

## 🔐 **1PASSWORD INTEGRATION**

### **Why 1Password?**
- Industry-standard password manager
- CLI available on all platforms
- Encrypted vault storage
- Audit trails and access logs
- Team sharing capabilities

### **Setup 1Password CLI**

```bash
# Install 1Password CLI
# Ubuntu/Debian:
curl -sS https://downloads.1password.com/linux/keys/1password.asc | \
    sudo gpg --dearmor --output /usr/share/keyrings/1password-archive-keyring.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/1password-archive-keyring.gpg] \
    https://downloads.1password.com/linux/debian/$(dpkg --print-architecture) stable main" | \
    sudo tee /etc/apt/sources.list.d/1password.list
sudo apt update && sudo apt install 1password-cli

# Sign in
op signin
```

### **Store Secrets in 1Password**

```bash
# Create a Grain Network vault (optional)
op vault create "Grain Network"

# Add API keys
op item create --category=login \
    --title="OpenAI API Key" \
    --vault="Grain Network" \
    password="sk-..."

op item create --category=login \
    --title="Anthropic API Key" \
    --vault="Grain Network" \
    password="sk-ant-..."

# For crypto wallets
op item create --category=login \
    --title="Phantom Wallet Seed" \
    --vault="Grain Network" \
    password="your-seed-phrase"
```

### **Load from 1Password**

Create `personal/load-from-1password.sh`:

```bash
#!/bin/bash
# Load environment variables from 1Password

# OpenAI API Key
export OPENAI_API_KEY=$(op item get "OpenAI API Key" --fields password)

# Anthropic API Key
export ANTHROPIC_API_KEY=$(op item get "Anthropic API Key" --fields password)

# ICP Principal
export ICP_PRINCIPAL=$(op item get "ICP Principal" --fields password)

# Solana Wallet
export SOLANA_PRIVATE_KEY=$(op item get "Solana Private Key" --fields password)

# GitHub Token
export GITHUB_TOKEN=$(op item get "GitHub Personal Access Token" --fields password)

echo "✅ Environment variables loaded from 1Password"
```

Source in your shell:

```bash
# Add to personal/.zshrc
if [ -f "$GRAINSTORE/grainenvvars/personal/load-from-1password.sh" ]; then
    source "$GRAINSTORE/grainenvvars/personal/load-from-1password.sh"
fi
```

---

## 📋 **TEMPLATE VARIABLES**

### **template/.env.example**

```bash
# ============================================================================
# GRAINENVVARS TEMPLATE
# ============================================================================
# Copy to personal/.env and fill in your real values
# NEVER commit personal/.env to git!
# ============================================================================

# ----------------------------------------------------------------------------
# AI & LLM APIs
# ----------------------------------------------------------------------------

# OpenAI API Key
# Get from: https://platform.openai.com/api-keys
OPENAI_API_KEY=sk-proj-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

# Anthropic API Key (Claude)
# Get from: https://console.anthropic.com/
ANTHROPIC_API_KEY=sk-ant-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

# ----------------------------------------------------------------------------
# Blockchain & Crypto
# ----------------------------------------------------------------------------

# Internet Computer (ICP/DFINITY) Principal
# Generate with: dfx identity get-principal
ICP_PRINCIPAL=xxxxx-xxxxx-xxxxx-xxxxx-xxxxx-xxxxx-xxxxx-xxxxx-xxx

# Solana Wallet Address
# Get from: Phantom wallet or solana-keygen
SOLANA_WALLET_ADDRESS=XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

# Phantom Wallet (NEVER store private keys in env vars - use 1Password!)
# PHANTOM_SEED_PHRASE=STORED_IN_1PASSWORD_ONLY

# ----------------------------------------------------------------------------
# Git & Version Control
# ----------------------------------------------------------------------------

# GitHub Personal Access Token
# Generate: https://github.com/settings/tokens
GITHUB_TOKEN=ghp_XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

# Codeberg Token
# Generate: https://codeberg.org/user/settings/applications
CODEBERG_TOKEN=XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

# ----------------------------------------------------------------------------
# Google Services
# ----------------------------------------------------------------------------

# Google Drive API Credentials
# Get from: https://console.cloud.google.com/
GOOGLE_CLIENT_ID=XXXXXXXXXX-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=GOCSPX-XXXXXXXXXXXXXXXXXXXXXXXXXXXX
GOOGLE_REFRESH_TOKEN=STORED_IN_1PASSWORD

# ----------------------------------------------------------------------------
# Grain Network Services
# ----------------------------------------------------------------------------

# Grainweb Daemon Port
GRAINWEB_PORT=8080

# Grainmusic API
GRAINMUSIC_API_URL=http://localhost:3000

# Graindrive Sync Path
GRAINDRIVE_SYNC_PATH=$HOME/GrainDrive

# ----------------------------------------------------------------------------
# Development & Testing
# ----------------------------------------------------------------------------

# Development mode flag
GRAIN_DEV_MODE=true

# Test mode flag
GRAIN_TEST_MODE=false

# Debug logging
GRAIN_DEBUG=false

# ============================================================================
# END TEMPLATE
# ============================================================================
```

---

## 🛡️ **SECURITY BEST PRACTICES**

### **DO**
✅ Use 1Password or similar password manager  
✅ Keep personal/.env in .gitignore  
✅ Rotate keys regularly (every 90 days)  
✅ Use environment-specific keys (dev/staging/prod)  
✅ Document what each variable does  
✅ Use principle of least privilege  
✅ Audit access logs regularly  

### **DON'T**
❌ Commit secrets to git (even private repos)  
❌ Share API keys in chat/email  
❌ Use same keys across multiple projects  
❌ Store private keys in environment variables  
❌ Leave debug mode on in production  
❌ Share 1Password vault passwords  

### **Crypto Wallet Security**
- **NEVER** store seed phrases in environment variables
- **NEVER** commit private keys to git
- **USE** 1Password for seed phrase storage
- **USE** hardware wallets for significant holdings
- **USE** separate wallets for dev/testing vs. real funds

---

## 📝 **USAGE PATTERNS**

### **Basic Usage**

```bash
# Check if variable is set
λ echo $OPENAI_API_KEY
sk-proj-...

# Use in scripts
λ curl https://api.openai.com/v1/chat/completions \
    -H "Authorization: Bearer $OPENAI_API_KEY" \
    ...
```

### **In Babashka Scripts**

```clojure
(ns my-script
  (:require [babashka.process :refer [shell]]))

;; Access environment variables
(def openai-key (System/getenv "OPENAI_API_KEY"))

;; Use in API calls
(when openai-key
  (println "OpenAI API configured"))
```

### **In Systemd Services**

```ini
[Service]
EnvironmentFile=/home/kae3g/kae3g/grainkae3g/grainstore/grainenvvars/personal/.env
ExecStart=/usr/local/bin/my-service
```

---

## 🔄 **TEMPLATE/PERSONAL WORKFLOW**

### **Template Updates**
When template adds new variables:

```bash
# Pull latest template
cd ~/kae3g/grainkae3g/grainstore/grainenvvars
git pull

# Check what's new
diff template/.env.example personal/.env

# Add new variables to personal/.env
nano personal/.env
```

### **Personal Config Management**

```bash
# Backup personal config (encrypted)
op document create personal/.env --title="Grainenvvars Personal Config" --vault="Grain Network"

# Restore from backup
op document get "Grainenvvars Personal Config" --vault="Grain Network" > personal/.env

# Sync to new machine
# 1. Install 1Password CLI
# 2. Sign in: op signin
# 3. Pull config: op document get "Grainenvvars Personal Config" > personal/.env
```

---

## 🎓 **EDUCATIONAL USE**

### **Teaching Environment Variables**

**Lesson**: Environment Variables and Security

**Topics**:
1. What are environment variables?
2. Why use them instead of hardcoding?
3. Security implications
4. Template/personal split pattern
5. Password manager integration

**Lab Exercise**:
```bash
# 1. Students copy template
cp template/.env.example personal/.env

# 2. Students use placeholder values (not real keys!)
OPENAI_API_KEY=sk-test-FAKE-KEY-FOR-LEARNING-ONLY

# 3. Students write script that uses env vars
# 4. Learn about .gitignore and security
# 5. Understand 1Password workflow
```

---

## 🔧 **ADVANCED FEATURES**

### **Environment-Specific Configs**

```bash
# personal/.env.development
GRAIN_ENV=development
OPENAI_API_KEY=sk-proj-dev-...
GRAINWEB_PORT=8080

# personal/.env.production
GRAIN_ENV=production
OPENAI_API_KEY=sk-proj-prod-...
GRAINWEB_PORT=443

# Load based on environment
if [ "$GRAIN_ENV" = "production" ]; then
    source personal/.env.production
else
    source personal/.env.development
fi
```

### **Secret Validation**

```bash
# Check required variables are set
required_vars=(
    "OPENAI_API_KEY"
    "GITHUB_TOKEN"
    "ICP_PRINCIPAL"
)

for var in "${required_vars[@]}"; do
    if [ -z "${!var}" ]; then
        echo "❌ Missing required variable: $var"
        exit 1
    fi
done
```

### **Automatic 1Password Loading**

```bash
# personal/.zshrc addition
# Automatically load from 1Password on shell start
if command -v op &> /dev/null; then
    eval $(op signin)
    source "$GRAINSTORE/grainenvvars/personal/load-from-1password.sh" 2>/dev/null
fi
```

---

## 📦 **GRAINZSH INTEGRATION**

Update `grainzsh` to automatically load `grainenvvars`:

```bash
# In grainzsh/template/.zshrc, add:

# ----------------------------------------------------------------------------
# GRAIN ENVIRONMENT VARIABLES
# ----------------------------------------------------------------------------

# Load from personal .env file
GRAIN_ENV_FILE="$GRAINSTORE/grainenvvars/personal/.env"
if [ -f "$GRAIN_ENV_FILE" ]; then
    export $(cat "$GRAIN_ENV_FILE" | grep -v '^#' | xargs)
fi

# Or load from 1Password
GRAIN_1PASS_LOADER="$GRAINSTORE/grainenvvars/personal/load-from-1password.sh"
if [ -f "$GRAIN_1PASS_LOADER" ]; then
    source "$GRAIN_1PASS_LOADER"
fi
```

---

## 🌾 **GRAIN NETWORK VARIABLES**

### **Standard Variables**

All Grain Network tools recognize these variables:

```bash
# Core paths
GRAIN_HOME=$HOME/kae3g/grainkae3g
GRAINSTORE=$GRAIN_HOME/grainstore

# Build system
GRAIN_BUILD_TOOL=gb
GRAIN_VERBOSE=false

# Display
GRAIN_DISPLAY_SCALE=1.0
GRAIN_NIGHT_LIGHT_TEMP=2000

# Network
GRAIN_WIFI_PRIMARY=Starlink
GRAIN_WIFI_FALLBACK=TMobile

# Development
GRAIN_DEV_MODE=true
GRAIN_LOG_LEVEL=info
```

### **Service-Specific Variables**

```bash
# Grainweb
GRAINWEB_PORT=8080
GRAINWEB_HOST=localhost
GRAINWEB_DATA_DIR=$HOME/.local/share/grainweb

# Grainmusic
GRAINMUSIC_API_KEY=STORED_IN_1PASSWORD
GRAINMUSIC_LIBRARY_PATH=$HOME/Music/Grainmusic

# Graindrive
GRAINDRIVE_SYNC_ENABLED=true
GRAINDRIVE_AUTO_BACKUP=true
```

---

## 🔑 **SUPPORTED SERVICES**

### **AI & LLM**
- OpenAI (GPT-4, GPT-3.5)
- Anthropic (Claude)
- Cursor AI
- Local LLMs (Ollama, etc.)

### **Blockchain**
- Internet Computer (ICP/DFINITY)
- Solana
- Ethereum (future)
- Urbit (ship identity)

### **Cloud Services**
- Google Drive
- GitHub
- Codeberg
- AWS/GCP (future)

### **Development**
- Git credentials
- SSH keys (referenced, not stored)
- API tokens
- Database connections

---

## 🎯 **EXAMPLE CONFIGURATIONS**

### **Developer Setup**

```bash
# personal/.env - Developer configuration

# AI Development
OPENAI_API_KEY=sk-proj-dev-...
ANTHROPIC_API_KEY=sk-ant-dev-...

# Git
GITHUB_TOKEN=ghp_dev_...
GIT_AUTHOR_NAME="Your Name"
GIT_AUTHOR_EMAIL="you@example.com"

# Grain Network
GRAIN_DEV_MODE=true
GRAIN_DEBUG=true
GRAINWEB_PORT=8080
```

### **Production Setup**

```bash
# personal/.env.production

# AI Production
OPENAI_API_KEY=LOAD_FROM_1PASSWORD
ANTHROPIC_API_KEY=LOAD_FROM_1PASSWORD

# Git
GITHUB_TOKEN=LOAD_FROM_1PASSWORD

# Grain Network
GRAIN_DEV_MODE=false
GRAIN_DEBUG=false
GRAINWEB_PORT=443
```

---

## 🔄 **ROTATION & MAINTENANCE**

### **Key Rotation Schedule**

```bash
# Rotate every 90 days
OPENAI_API_KEY_CREATED=2025-01-22
OPENAI_API_KEY_EXPIRES=2025-04-22

# Set calendar reminders
# Use bb script to check expiration
gb envvars:check-expiration
```

### **Audit Script**

```bash
#!/bin/bash
# scripts/audit-env-vars.sh

echo "🔍 Auditing environment variables..."

# Check for common secrets accidentally exposed
if git log --all --oneline | grep -E "(API_KEY|TOKEN|SECRET|PASSWORD)"; then
    echo "⚠️  WARNING: Possible secrets in git history!"
fi

# Verify .gitignore
if ! grep -q "personal/.env" .gitignore; then
    echo "❌ ERROR: personal/.env not in .gitignore!"
fi

echo "✅ Audit complete"
```

---

## 🎓 **TEACHING MATERIALS**

### **High School Lesson: Environment Variables & Security**

**Learning Objectives**:
1. Understand what environment variables are
2. Learn why secrets shouldn't be in code
3. Practice template/personal split pattern
4. Use password managers securely

**Activities**:
1. Create fake API key and use it
2. Learn about .gitignore
3. Understand security risks
4. Set up personal config

**Assessment**:
- Can student explain why secrets shouldn't be committed?
- Can student create .env file correctly?
- Does student understand template/personal split?

---

## 📊 **VARIABLES REFERENCE**

### **AI Services**

| Variable | Service | Where to Get | Required |
|----------|---------|--------------|----------|
| `OPENAI_API_KEY` | OpenAI GPT | https://platform.openai.com/api-keys | Yes (if using OpenAI) |
| `ANTHROPIC_API_KEY` | Claude | https://console.anthropic.com/ | Yes (if using Claude) |
| `CURSOR_API_KEY` | Cursor AI | Cursor settings | Optional |

### **Blockchain**

| Variable | Service | Where to Get | Required |
|----------|---------|--------------|----------|
| `ICP_PRINCIPAL` | Internet Computer | `dfx identity get-principal` | Yes (for ICP) |
| `SOLANA_WALLET_ADDRESS` | Solana | Phantom wallet | Yes (for Solana) |
| `URBIT_SHIP` | Urbit | Your ship name | Optional |

### **Development**

| Variable | Purpose | Example | Required |
|----------|---------|---------|----------|
| `GITHUB_TOKEN` | GitHub API | `ghp_...` | Yes (for automation) |
| `CODEBERG_TOKEN` | Codeberg API | Token | Optional |
| `GRAIN_DEV_MODE` | Development mode | `true`/`false` | No |

---

## 🔐 **SECURITY CHECKLIST**

### **Initial Setup**
- [ ] Install 1Password or similar password manager
- [ ] Create Grain Network vault
- [ ] Copy template/.env.example to personal/.env
- [ ] Add personal/.env to .gitignore
- [ ] Verify .gitignore works (`git status` shows no secrets)

### **Adding New Secrets**
- [ ] Add to template/.env.example with placeholder
- [ ] Add to personal/.env with real value
- [ ] Add to 1Password vault
- [ ] Document in README
- [ ] Test loading in fresh shell

### **Regular Maintenance**
- [ ] Rotate keys every 90 days
- [ ] Audit git history for leaked secrets
- [ ] Update 1Password backup
- [ ] Review access permissions
- [ ] Check for unused variables

---

## 🚨 **INCIDENT RESPONSE**

### **If You Accidentally Commit a Secret**

```bash
# 1. IMMEDIATELY rotate the compromised key
# 2. Remove from git history
git filter-branch --force --index-filter \
    "git rm --cached --ignore-unmatch personal/.env" \
    --prune-empty --tag-name-filter cat -- --all

# 3. Force push (use with EXTREME caution)
# Only do this if you understand the implications
git push --force --all

# 4. Inform security team if applicable
# 5. Monitor for unauthorized usage
```

### **Prevention Tools**

```bash
# Install git-secrets
brew install git-secrets  # macOS
sudo apt install git-secrets  # Ubuntu

# Configure
git secrets --install
git secrets --register-aws
git secrets --add 'sk-[a-zA-Z0-9]{32,}'  # OpenAI pattern
```

---

## 🌐 **CROSS-PLATFORM SUPPORT**

### **Ubuntu 24.04 LTS (Primary)**
- Full support
- Systemd integration
- GNOME integration

### **macOS**
- Full support
- LaunchAgents integration
- 1Password native app

### **Other Linux**
- Template works universally
- Adjust paths as needed

---

## 🔗 **INTEGRATION WITH OTHER MODULES**

### **Grainzsh**
Automatically loads grainenvvars in shell config

### **Graindaemon**
Services can use EnvironmentFile directive

### **Grainbarrel**
Tasks can access environment variables

### **Grainweb/Grainmusic**
Services use env vars for API configuration

---

## 🌾 **GRAIN NETWORK ECOSYSTEM**

Grainenvvars is part of the larger Grain Network ecosystem:

- **[Grainzsh](https://github.com/grainpbc/grainzsh)**: Shell configuration
- **[Grainbarrel](https://github.com/grainpbc/grainbarrel)**: Build system
- **[Graindaemon](https://github.com/grainpbc/graindaemon)**: Service management
- **[Grainstore](https://github.com/grainpbc/grainstore)**: Module ecosystem

---

## 📄 **LICENSE**

MIT License - Use freely, but **never share your personal values**

---

## 🌾 **CLOSING THOUGHTS**

Security is not just technical - it's a practice, a culture, a way of thinking. 

We build Grainenvvars to:
- **Teach** good security practices
- **Enable** secure development
- **Protect** user privacy and sovereignty
- **Empower** developers at all levels

Your secrets are yours. Your sovereignty is real. Your security matters.

---

*"Local Control, Global Intent - Your keys, your power, your choice"* 🔒🌾

**Part of the Grain Network** - Global Renewable technology for a sustainable future

