# Configuration System Migration (2025-10-11)

**Session 7734** - Major restructuring of configuration management

## What Changed

### Before
- Username and language **hardcoded** in Svelte components
- No clear separation between public settings and private secrets
- `.secrets.edn` mixed personal preferences with API keys
- Manual updates required for site identity changes

### After
- Username and language **generated from `.config.edn`**
- **Two-tier system**: public/personal vs. private/secret
- Clear templates for both configuration files
- Automated config generation integrated into build pipeline
- Comprehensive internationalization vision documented

## New Files

### Configuration Files
- `.config.edn` - **Public/personal** settings (safe to commit)
  - Username, language, site title
  - Repository info, timezone, preferences
  - Planned languages list

- `.config.template.edn` - Template for public config
  - Documents all available options
  - Safe to commit and share

- `.secrets.template.edn` - Updated template for private secrets
  - Focused on actual secrets (API keys, tokens)
  - Clear warnings about never committing

### Scripts
- `scripts/generate-site-config.bb` - Generates `site-config.json` from `.config.edn`
  - Reads EDN config
  - Outputs JSON for web app consumption
  - Integrated into build pipeline

### Generated Files
- `web-app/static/site-config.json` - Generated config for web app
  - Contains: username, language, title, repository info, planned languages
  - Loaded by Svelte components at runtime

### Documentation
- `docs/CONFIGURATION.md` - Comprehensive configuration guide
  - Two-tier system explained
  - Security best practices
  - Troubleshooting
  - Migration guide
  - Internationalization support

## Updated Files

### Core Changes
- `web-app/src/lib/LanguageIndicator.svelte` - Now loads language from config
- `web-app/src/lib/UsernameIndicator.svelte` - Now loads username from config
- `.gitignore` - Clarified that `.config.edn` is safe to commit
- `scripts/deploy-to-pages.bb` - Calls `config:generate` before building

### Documentation Updates
- `writings/your-tundra.md` - Complete rewrite of Part 5: Configuration
  - Two-tier system explained
  - Detailed internationalization vision
  - Step-by-step setup instructions
  - Language support roadmap

## Key Benefits

### For Users
✅ **Easy customization** - Just edit `.config.edn` with your info
✅ **Safe sharing** - Public config safe to commit and screenshot
✅ **Clear separation** - No confusion about what's secret vs. public
✅ **Multi-language ready** - Built-in support for internationalization

### For Developers
✅ **Template-driven** - Easy to fork and customize
✅ **Automated** - Config generation integrated into build pipeline
✅ **Type-safe-ish** - EDN provides structure validation
✅ **Cursor-friendly** - Safe to ask AI for help with `.config.edn`

### For Internationalization
✅ **Language declared** - Clear metadata for current language
✅ **Planned languages** - Document future language support
✅ **Extensible** - Easy to add new languages
✅ **Vision articulated** - Comprehensive roadmap in documentation

## Internationalization Vision

### Current
- **English** (`en`) - Default language

### Near Future (Highest Priority)
- **Spanish** (`es`) - Large global audience, growing tech communities
- **German** (`de`) - Strong engineering culture, philosophical depth

### Long-Term (All Welcome)
- **French** (`fr`) - Francophone world, African tech growth
- **Japanese** (`ja`) - Precision culture, strong tech ecosystem
- **Chinese** (`zh`) - Massive scale, computational philosophy
- **Arabic** (`ar`) - Rich intellectual tradition, underserved in tech
- **Portuguese** (`pt`) - Brazilian tech boom, Lusophone world
- **Russian** (`ru`) - Mathematical heritage, strong computer science

### Philosophy
> **"Permafrost foundations transcend language. Nock's 12 rules work in any tongue. Precision flow is universal."**

The system is designed for a **constellation of tundras**, each in its native language, all building on the same frozen foundations.

## Migration Guide (For Existing Users)

If you forked before this update:

```bash
# 1. Pull latest changes
git pull origin coldriver-heal

# 2. Copy config template
cp .config.template.edn .config.edn

# 3. Edit with your settings
vim .config.edn  # or cursor, emacs, etc.

# 4. Generate site config
bb config:generate

# 5. Verify components load config
cd web-app && npm run dev
# Check that username and language appear in top corners

# 6. Commit your config
git add .config.edn
git commit -m "feat: configure site with public/personal settings"

# 7. Optional: Update secrets (if using AI APIs)
cp .secrets.template.edn .secrets.edn
vim .secrets.edn  # Add API keys if needed
# Do NOT commit .secrets.edn!

# 8. Build and deploy
bb flow "chore: migrate to two-tier config system"
```

## Security Reminders

### .config.edn (Public/Personal)
- ✅ **Safe to commit**
- ✅ **Safe to share**
- ✅ **Safe for screenshots**
- ✅ **Ask Cursor for help**

### .secrets.edn (Private/Secret)
- ❌ **NEVER commit** (blocked by `.gitignore`)
- ❌ **NEVER share**
- ❌ **NEVER screenshot**
- ❌ **Don't ask AI for help** (may expose keys)

**When in doubt**: "Would I put this in a screenshot?" If no → `.secrets.edn`

## Technical Details

### Build Flow
```
.config.edn (user edits)
    ↓
bb config:generate
    ↓
web-app/static/site-config.json
    ↓
SvelteKit build includes config
    ↓
Components fetch('/site-config.json')
    ↓
UI displays username, language
```

### Config Generation (Babashka)
```clojure
(defn generate-site-config []
  (let [config (edn/read-string (slurp ".config.edn"))
        site-config {:username (get-in config [:site :username])
                     :language (get-in config [:site :language])
                     :plannedLanguages (get-in config [:localization :planned-languages])}]
    (spit "web-app/static/site-config.json"
          (json/generate-string site-config {:pretty true}))))
```

### Runtime Loading (Svelte)
```javascript
onMount(async () => {
  const res = await fetch('/site-config.json');
  const config = await res.json();
  username = config.username || 'kae3g';
  language = config.language || 'en';
});
```

## Future Enhancements

### Planned
- [ ] UI string translations (currently hardcoded English)
- [ ] Language-specific content routing
- [ ] Multi-language search
- [ ] Language switcher component (if supporting multiple languages on one site)
- [ ] Automatic language detection from browser
- [ ] RSS feeds per language

### Community Contributions Welcome
- Translation of existing essays into Spanish, German, French, etc.
- UI string translation files
- Language-specific metaphors and cultural adaptations
- Right-to-left (RTL) layout support for Arabic
- CJK typography improvements for Chinese, Japanese, Korean

## Related Documentation

- [CONFIGURATION.md](./CONFIGURATION.md) - Complete configuration reference
- [Your Tundra Essay](../writings/your-tundra.md) - User setup guide
- [DEVELOPER-GUIDE.md](./DEVELOPER-GUIDE.md) - Developer handbook

## Commit Message
```
feat: two-tier config system (public/personal vs private/secret) + internationalization vision (7734)

- Created .config.edn (public/personal - safe to commit)
- Updated .secrets.template.edn (private/secret - never commit)
- Added scripts/generate-site-config.bb for automated config generation
- Updated LanguageIndicator/UsernameIndicator to load from config
- Integrated config:generate into build pipeline
- Rewrote Your Tundra Part 5 with comprehensive internationalization vision
- Created docs/CONFIGURATION.md for developer reference
- Clarified .gitignore: .config.edn safe to commit, .secrets.edn never
- Documented language support roadmap (es, de next; fr, ja, zh, ar, pt, ru planned)
```

---

**This is a major milestone** in making the Coldriver Tundra truly **forkable**, **customizable**, and **international**.

The permafrost is frozen. The languages are planned. The tundras are ready to multiply. ❄️🌍

