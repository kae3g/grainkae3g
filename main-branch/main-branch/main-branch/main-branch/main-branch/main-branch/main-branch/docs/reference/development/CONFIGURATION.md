# Configuration System

**Coldriver Tundra uses a two-tier configuration system** to clearly separate public/personal settings from private/secret data.

## Overview

| File | Type | Commit? | Share? | Contains |
|------|------|---------|--------|----------|
| `.config.edn` | Public/Personal | ✅ Yes | ✅ Yes | Username, language, site title, preferences |
| `.secrets.edn` | Private/Secret | ❌ Never | ❌ Never | API keys, auth tokens, GPG keys |
| `.config.template.edn` | Template | ✅ Yes | ✅ Yes | Template for public config |
| `.secrets.template.edn` | Template | ✅ Yes | ✅ Yes | Template for private config |

## Tier 1: Public/Personal Configuration

**File**: `.config.edn`

**Purpose**: Settings that define your site's public identity and preferences.

**Safe to**:
- ✅ Commit to git
- ✅ Share in screenshots
- ✅ Include in documentation
- ✅ Ask Cursor to help edit
- ✅ Share with collaborators

**Setup**:
```bash
# Copy template
cp .config.template.edn .config.edn

# Edit with your settings
cursor .config.edn  # or vim, emacs, etc.

# Commit it!
git add .config.edn
git commit -m "feat: configure site for my-username"
```

**Structure**:
```clojure
{:site
 {:username "kae3g"              ; Display name (shows in UI)
  :language "en"                 ; ISO 639-1 code: en, es, de, fr, ja, zh, ar, pt, ru
  :title "Coldriver Tundra"     ; Site title
  :description "..."}            ; Meta description

 :author
 {:name "Your Name"             ; Full name for copyright
  :email "you@example.com"      ; Public email
  :url "https://..."}           ; Personal website (optional)

 :repository
 {:platform "codeberg"           ; codeberg, github, gitlab, gitea
  :username "yourusername"      ; Git hosting username
  :repo-name "your-tundra"      ; Repository name
  :branch "main"}               ; Primary branch

 :localization
 {:timezone "America/Los_Angeles"
  :locale "en-US"
  :date-format "YYYY-MM-DD"
  :planned-languages ["es" "de"]}  ; Future languages

 :preferences
 {:editor "cursor"               ; cursor, vscode, vim, emacs
  :theme "dark"                  ; dark, light, auto
  :monospace-font "Courier New"}}
```

**How it's used**:
1. `bb config:generate` reads `.config.edn`
2. Generates `web-app/static/site-config.json` (for web app consumption)
3. Web app loads config and displays:
   - Username in top-right (golden ratio position)
   - Language in top-left
   - Site title, meta tags
   - Repository links

## Tier 2: Private/Secret Configuration

**File**: `.secrets.edn`

**Purpose**: Sensitive data that must never be exposed.

**NEVER**:
- ❌ Commit to git (blocked by `.gitignore`)
- ❌ Share in screenshots
- ❌ Include in documentation
- ❌ Upload anywhere
- ❌ Share with anyone

**Setup**:
```bash
# Copy template
cp .secrets.template.edn .secrets.edn

# Edit with your PRIVATE keys
# Use a password manager or secure note-taking app

# Verify it's ignored
git status  # Should NOT show .secrets.edn
```

**Structure**:
```clojure
{:git
 {:signing-key "ABC123DEF456"}  ; GPG key ID for verified commits

 :git-hosting
 {:codeberg
  {:token "codeberg_..."        ; API token for automation
   :ssh-key-path "~/.ssh/id_ed25519"}

  :github
  {:token "ghp_..."             ; If mirroring to GitHub
   :ssh-key-path nil}}

 :ai-services
 {:cursor {:api-key nil}        ; Usually handled by Cursor app
  :openai {:api-key "sk-..."}   ; OpenAI API
  :anthropic {:api-key "sk-..."}  ; Claude API
  :deepseek {:api-key "..."}
  :google {:api-key "..."}      ; Gemini
  :grok {:bearer-token "..."}   ; X API
  :qwen {:api-key nil}
  :meta {:api-key nil}}

 :deployment
 {:codeberg-pages {:deploy-token nil}
  :custom-domain {:nameservers nil :ssl-cert nil}}

 :monitoring
 {:sentry-dsn nil               ; Error tracking
  :analytics-id nil}}           ; Privacy-respecting analytics
```

**How it's used**:
- Local scripts can read `.secrets.edn` for API automation
- **Never** exposed to the web app or public site
- Stays on your local machine only

## Build Integration

**Automatic config generation**:
```bash
# Generate site-config.json from .config.edn
bb config:generate

# Check if .config.edn exists
bb config:check
```

**Integrated into build process**:
- `bb flow` → calls `config:generate` before building
- `bb deploy:build-content` → calls `config:generate` first
- `scripts/deploy-to-pages.bb` → always generates config

**Build flow**:
```
.config.edn (public/personal)
    ↓
bb config:generate
    ↓
web-app/static/site-config.json (deployed)
    ↓
Web app loads config at runtime
    ↓
UI displays username, language, etc.
```

## Internationalization Support

**Current implementation**:
- Language stored in `.config.edn` → `:site :language`
- Displayed in UI as `language: en` (top-left)
- Build system respects language setting

**Planned languages** (`:localization :planned-languages`):
- `en` - English (current)
- `es` - Spanish (next priority)
- `de` - German (next priority)
- `fr` - French
- `ja` - Japanese
- `zh` - Chinese
- `ar` - Arabic
- `pt` - Portuguese
- `ru` - Russian

**How to add a language**:
1. Change `:language` in `.config.edn` (e.g., `"es"`)
2. Write essays in `writings/` in your language
3. (Optional) Translate UI strings (future work)
4. Build and deploy: `bb flow "feat: Spanish translation"`
5. Share with your language community

**Future enhancements**:
- UI string translations (currently hardcoded English)
- Language-specific content routing
- Multi-language search
- Language switcher component

## Security Best Practices

**For `.config.edn` (public/personal)**:
- ✅ Safe to version control
- ✅ Use noreply email addresses (e.g., `you@users.noreply.codeberg.org`)
- ✅ Public URLs only
- ✅ No sensitive data

**For `.secrets.edn` (private/secret)**:
- ❌ NEVER commit (enforced by `.gitignore`)
- ❌ NEVER screenshot
- ✅ Use a password manager to store API keys
- ✅ Rotate keys regularly
- ✅ Use tokens with minimal permissions

**Cursor AI assistance**:
- ✅ **Safe**: Ask Cursor to help with `.config.edn`
- ❌ **Unsafe**: Ask Cursor to help with `.secrets.edn` (it may expose keys)
- ✅ **Safe**: Ask Cursor to explain the structure, never the values

## Developer Workflow

**Initial setup** (new repo):
```bash
# 1. Copy both templates
cp .config.template.edn .config.edn
cp .secrets.template.edn .secrets.edn  # Only if needed

# 2. Edit .config.edn (public)
# - Change username, language, repo info

# 3. Edit .secrets.edn (private, optional)
# - Add API keys only if using AI services directly

# 4. Generate site config
bb config:generate

# 5. Commit public config
git add .config.edn
git commit -m "feat: configure site"

# 6. Verify .secrets.edn is ignored
git status  # Should NOT show .secrets.edn
```

**Daily workflow**:
```bash
# Just use bb flow - it handles config automatically
bb flow "feat: new essay"
```

**Updating config**:
```bash
# Edit .config.edn
vim .config.edn

# Regenerate site config
bb config:generate

# Commit changes
bb flow "chore: update site config"
```

## Troubleshooting

**Problem**: Username/language not showing in UI

**Solution**:
```bash
# 1. Check .config.edn exists
ls -la .config.edn

# 2. Regenerate site config
bb config:generate

# 3. Check generated config
cat web-app/static/site-config.json

# 4. Rebuild site
bb writings:build-fast
cd web-app && npm run build
```

---

**Problem**: Accidentally committed .secrets.edn

**Solution**:
```bash
# 1. Remove from git history (IMMEDIATELY)
git filter-branch --force --index-filter \
  "git rm --cached --ignore-unmatch .secrets.edn" \
  --prune-empty --tag-name-filter cat -- --all

# 2. Force push (if already pushed)
git push origin --force --all

# 3. Rotate ALL keys in .secrets.edn immediately
# - Generate new API keys
# - Generate new GPG key (if exposed)
# - Generate new SSH keys (if exposed)

# 4. Verify .gitignore includes .secrets.edn
grep ".secrets.edn" .gitignore
```

---

**Problem**: Config not updating in deployed site

**Solution**:
```bash
# 1. Ensure config:generate runs before deploy
bb config:generate

# 2. Full clean rebuild
bb deploy:build-content-full

# 3. Force deploy
bb deploy:full
```

## Migration from Old System

**If upgrading from a system without .config.edn**:

```bash
# 1. Create .config.edn from template
cp .config.template.edn .config.edn

# 2. Extract values from old README.md or package.json
# - Username from git remote URL
# - Repository name from git config
# - Language from any existing i18n setup

# 3. Update .config.edn with extracted values

# 4. Generate site config
bb config:generate

# 5. Remove old hardcoded values
# - Check web-app/src/lib/*.svelte
# - Check web-app/package.json

# 6. Test locally
cd web-app && npm run dev

# 7. Commit when working
git add .config.edn
git commit -m "chore: migrate to .config.edn system"
```

## Template Maintenance

**When adding new config options**:

1. Update `.config.template.edn` with new key
2. Update `.config.edn` with your value
3. Update `scripts/generate-site-config.bb` to export the key
4. Update this documentation
5. Update `writings/your-tundra.md` Part 5

**Example** (adding a new field):
```clojure
;; In .config.template.edn
{:site
 {:username "kae3g"
  :language "en"
  :timezone "UTC"  ; <-- New field
  ...}}

;; In scripts/generate-site-config.bb
(defn generate-site-config []
  (let [config (load-config)
        site-config {:username (get-in config [:site :username])
                     :language (get-in config [:site :language])
                     :timezone (get-in config [:site :timezone] "UTC")  ; <-- Export it
                     ...}]
    (spit output-file (json/generate-string site-config))))
```

## See Also

- [Your Tundra Essay](../writings/your-tundra.md) - User-facing setup guide
- [Developer Guide](./DEVELOPER-GUIDE.md) - Complete developer handbook
- [Build System](./BUILD-SYSTEM.md) - Build architecture details

---

**Remember**: `.config.edn` = **public/personal** (safe to commit), `.secrets.edn` = **private/secret** (never commit).

When in doubt, ask: "Would I put this in a screenshot?" If no, it belongs in `.secrets.edn`.

