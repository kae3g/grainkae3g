# Cursor Icon Design for Grain Network

## Concept

Custom Grain Network-themed icon for Cursor IDE, featuring the "Gr" (Global Renewable) branding with warm, earthy tones.

## Design Specifications

### Icon Sizes Needed
- **512x512** - High resolution source
- **256x256** - Standard size
- **128x128** - Medium size
- **64x64** - Small size
- **48x48** - Taskbar size
- **32x32** - Minimum size
- **16x16** - Tiny size

### Color Palette (Grain Network Theme)
```
Primary Colors:
- Grain Gold: #D4A574 (warm golden wheat)
- Earth Brown: #8B6F47 (rich soil)
- Forest Green: #4A6741 (sustainable green)
- Warm White: #FFF8E7 (warm paper)
- Deep Brown: #3D2817 (dark earth)

Accent Colors:
- Sunset Orange: #E67E22 (warm sunset - 2000K feel)
- Sky Blue: #5DADE2 (clear sky)
- Sage Green: #A8B5A0 (natural herb)
```

### Icon Concept: "Gr" Wheat Cursor

```
┌─────────────────────────┐
│                         │
│    🌾                   │
│   ╱│╲   Gr              │
│  ╱ │ ╲                  │
│    │                    │
│   ╱│╲                   │
│  ╱ │ ╲                  │
│    │                    │
│   ╱ ╲                   │
│  ╱   ╲                  │
│ ╱     ╲                 │
│╱       ╲                │
│   Cursor                │
└─────────────────────────┘

Elements:
1. Wheat grain stalk (🌾) forming a cursor/arrow shape
2. "Gr" letters integrated into wheat pattern
3. Warm golden gradient (#D4A574 → #E67E22)
4. Dark earth background (#3D2817)
```

## Implementation Options

### Option 1: SVG-based Icon (Recommended)
Create an SVG that can be rendered at any size and converted to PNG for various resolutions.

### Option 2: Use Existing Grain Emoji
Simple approach using the 🌾 emoji with custom styling.

### Option 3: AI-Generated Icon
Use DALL-E or similar to generate a custom wheat cursor icon.

## Installation Instructions

### For Linux (Ubuntu 24.04 LTS)

1. **Create icon file**:
   ```bash
   # Place icon at ~/.local/share/icons/cursor-grain.png
   cp assets/icons/cursor-grain-512.png ~/.local/share/icons/cursor-grain.png
   ```

2. **Create desktop entry override**:
   ```bash
   mkdir -p ~/.local/share/applications
   cp /usr/share/applications/cursor.desktop ~/.local/share/applications/cursor.desktop
   ```

3. **Edit desktop entry**:
   ```bash
   # Edit ~/.local/share/applications/cursor.desktop
   # Change Icon line to:
   Icon=/home/xy/.local/share/icons/cursor-grain.png
   ```

4. **Update icon cache**:
   ```bash
   gtk-update-icon-cache ~/.local/share/icons/
   ```

## SVG Source (cursor-grain.svg)

We'll create a simple SVG-based design that combines:
- Wheat stalk forming a cursor arrow
- "Gr" branding
- Grain Network color palette
- 2000K warm lighting aesthetic

## Files to Create

1. `assets/icons/cursor-grain.svg` - Source SVG design
2. `assets/icons/cursor-grain-512.png` - 512x512 PNG
3. `assets/icons/cursor-grain-256.png` - 256x256 PNG
4. `assets/icons/cursor-grain-128.png` - 128x128 PNG
5. `assets/icons/cursor-grain-64.png` - 64x64 PNG
6. `scripts/install-cursor-icon.bb` - Installation script

## Usage

```bash
# Generate all icon sizes from SVG
bb scripts/generate-icons.bb

# Install icon for current user
bb scripts/install-cursor-icon.bb

# Restart GNOME to apply
# Press Alt+F2, type 'r', press Enter
```

## Future Enhancements

- Animated icon for dark/light mode switching
- Integration with GrainDisplay metadata
- Custom icon themes for different Grain Network apps
- Icon variants for VisionMode vs InkMode (Graindroid)

