# grainkae3gdisplay

**kae3g's personal display warmth settings**

Personal color temperature preferences and grain6-scheduled automation for bedtime red-orange display warmth.

---

## Configuration

**Template**: `grainpbc/graindisplay`  
**Personal**: This module

### Warmth Preferences
- **Temperature**: 2000K (bedtime red-orange)
- **Schedule**: Sunset → Sunrise (via grain6)
- **Transition**: Smooth 30-minute fade

---

## Settings

### personal/warmth.edn
```edn
{:color-temperature 2000
 :schedule {:start :sunset
            :stop :sunrise
            :transition-minutes 30}
 :grain6-enabled true}
```

---

## Usage

Display warmth is automatically managed by grain6:

```bash
# grain6 enables at sunset
🌅 grain6: Enabling display warmth (sunset detected)

# grain6 disables at sunrise  
🌄 grain6: Disabling display warmth (sunrise detected)
```

Manual control:

```bash
# Enable immediately
gsettings set org.gnome.settings-daemon.plugins.color night-light-enabled true

# Disable
gsettings set org.gnome.settings-daemon.plugins.color night-light-enabled false
```

---

**Created**: October 23, 2025  
**Graintime**: `12025-10-23--0145--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g`

