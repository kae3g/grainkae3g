# kae3g Personal Modules

**Personal configurations and content for kae3g's Grain Network setup**

This directory contains kae3g-specific personalizations of template modules from `grainpbc/`.

---

## Philosophy

**Template + Personal = Your Grain**

The Grain Network follows a clean separation pattern:
- **`grainpbc/`**: Universal templates for everyone
- **`kae3g/`**: Personal configs for kae3g (this directory)
- **`{yourdevname}/`**: Your personal configs (when you fork)

---

## Personal Modules

### grainkae3gtime
Personal graintime configuration:
- Location: San Rafael, CA (37.9735°N, -122.5311°W)
- Timezone: PDT/PST
- Custom graintime preferences

**Template**: `grainpbc/graintime`

### grainkae3gcourse
Personal course content:
- kae3g's educational materials
- Course versions (immutable grainpaths)
- Learning materials and documentation

**Template**: `grainpbc/graincourse`

### grainkae3gdisplay
Personal display settings:
- Bedtime red-orange warmth preferences
- Custom color temperature schedules
- grain6-scheduled automation

**Template**: `grainpbc/graindisplay`

### grainkae3gdaemon
Personal daemon configurations:
- Service schedules
- System automation
- grain6 timer configurations

**Template**: `grainpbc/graindaemon`

### grainkae3genvvars
Personal environment variables:
- API keys (OpenAI, etc.)
- Secrets (1Password integration)
- Personal tokens and credentials

**Template**: `grainpbc/grainenvvars`

---

## Naming Convention

**Format**: `grain{devname}{module}`

Examples:
- `grainkae3gtime` (kae3g's graintime)
- `grainjen3gcourse` (jen3g's graincourse)
- `graintom3gdisplay` (tom3g's graindisplay)

**Why 5 characters?**
- High availability on GitHub
- One syllable + "3g" pattern
- Easy to remember and type

---

## Usage

### Linking Personal Configs

Personal modules override/extend templates:

```bash
# graintime uses kae3g location
gt now
# => Uses San Rafael, CA from grainkae3gtime/

# graincourse uses kae3g content
gb graincourse:build
# => Builds kae3g's courses from grainkae3gcourse/
```

### Version Control

**Template modules** (`grainpbc/`):
- Public, open-source
- Shared configurations
- Default settings
- Personal directory gitignored

**Personal modules** (`kae3g/`):
- Private or selectively shared
- User-specific configs
- **NOT gitignored** (versioned content)

---

## File Structure

```
kae3g/
├── README.md (this file)
├── grainkae3gtime/
│   └── personal/
│       └── location.edn          # kae3g's location
├── grainkae3gcourse/
│   └── personal/
│       └── courses/               # kae3g's course content
├── grainkae3gdisplay/
│   └── personal/
│       └── warmth-schedule.edn   # kae3g's display prefs
├── grainkae3gdaemon/
│   └── personal/
│       └── services.edn          # kae3g's daemon configs
└── grainkae3genvvars/
    └── personal/
        └── secrets.edn           # kae3g's API keys (gitignored)
```

---

## Creating Your Own

To create your own personal modules:

1. **Choose a devname**: `{yoursyllable}3g` (e.g., `sam3g`, `alex3g`)
2. **Create directory**: `mkdir grainstore/{yourdevname}/`
3. **Copy structure**: `cp -r kae3g/ {yourdevname}/`
4. **Rename modules**: `grain{yourdevname}{module}`
5. **Personalize**: Add your configs, content, secrets

Use `grainpbc/grainsource-personalize` to automate this process!

---

## Integration

Personal modules are automatically detected by:
- **grainbarrel (`gb`)**: Finds personal configs
- **graintime (`gt`)**: Uses personal location
- **graincourse**: Builds personal content
- **graindaemon**: Runs personal services
- **grain6**: Schedules personal timers

---

## Philosophy: Personhood + Templates

**Templates are universal, personhood is unique.**

`grainpbc/` modules are like:
- A blank canvas (everyone starts here)
- Universal laws (gravity works for everyone)
- Open recipes (anyone can cook)

`kae3g/` modules are like:
- Your painting on the canvas
- Your unique trajectory through space
- Your signature dish

**Together**: Template + Personal = Your Whole Grain 🌾

---

## See Also

- [grainpbc/](../grainpbc/README.md) - Template modules
- [grainpbc/grainsource-personalize/](../grainpbc/grainsource-personalize/README.md) - Personalization utility
- [grainpbc/grainsource-separation/](../grainpbc/grainsource-separation/README.md) - Separation pattern

---

**DevName**: kae3g  
**Created**: October 23, 2025  
**Graintime**: `12025-10-23--0145--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g`

🌾 **Your grain, your way!**

