# Grainicons: Icon Management Library for Grain Network

**Centralized icon management with template/personal split**

Grainicons provides a unified icon library for all Grain Network applications, supporting multiple icon sources, automatic format conversion, and seamless integration with Graincasks and GrainDisplay.

## Features

- 📦 **Template Icon Library** - Shared icons across all Grain Network apps
- 👤 **Personal Overrides** - User-specific icon customization
- 🌐 **Multiple Sources** - Built-in library, web URLs, custom files, bundled icons
- 🎨 **Format Conversion** - SVG → PNG at multiple sizes (512, 256, 128, 64, 48, 32, 16)
- 🖥️ **Desktop Integration** - Automatic `.desktop` file icon references
- 📊 **GrainDisplay Integration** - Icons for display metadata
- 🍺 **Graincasks Integration** - Icon management for AppImages

## Architecture

```
grainicons/
├── template/                   # Shared icon library
│   ├── cursor-grain.svg
│   ├── grainmusic.svg
│   ├── grainweb.svg
│   └── ...
├── personal/                   # User-specific overrides
│   └── cursor.personal.edn    # Personal icon config
├── src/grainicons/
│   ├── core.clj               # Core icon management
│   ├── sources.clj            # Icon source handlers
│   └── conversion.clj         # SVG → PNG conversion
├── config/
│   └── grainicons.template.edn
├── scripts/
│   ├── icon-install.bb        # Install icon
│   ├── icon-generate.bb       # Generate PNG from SVG
│   └── icon-cache.bb          # Manage icon cache
└── README.md
```

## Icon Definition Format

Icons are defined in EDN with metadata:

```clojure
{:icon
 {:name "cursor-grain"
  :display-name "Cursor (Grain Network Edition)"
  :description "Grain Network-themed Cursor IDE icon"
  
  :source :template  ;; :template, :web, :custom, :bundled
  :template-path "template/cursor-grain.svg"
  
  ;; For :web source
  :web-url nil
  
  ;; For :custom source
  :custom-path nil
  
  ;; Icon metadata
  :colors
  {:primary "#D4A574"    ;; Grain Gold
   :secondary "#E67E22"  ;; Sunset Orange
   :background "#3D2817" ;; Deep Brown}
  
  :tags ["development" "ide" "cursor" "grain-network"]
  :author "kae3g"
  :license "MIT"
  :created-at "12025-10-22--1645--CDT--moon-vishakha--09thhouse16--kae3g"}}
```

## Usage

### Install an Icon

```bash
# Install from template library
bb icon install cursor-grain

# Install from web URL
bb icon install --web https://cursor.com/brand/icon.png cursor-web

# Install custom icon
bb icon install --custom ~/Pictures/my-cursor-icon.png cursor-custom
```

### Generate PNG Sizes

```bash
# Generate all standard sizes from SVG
bb icon generate cursor-grain

# Output:
# ~/.local/share/icons/cursor-grain-512.png
# ~/.local/share/icons/cursor-grain-256.png
# ~/.local/share/icons/cursor-grain-128.png
# ~/.local/share/icons/cursor-grain-64.png
# ~/.local/share/icons/cursor-grain-48.png
# ~/.local/share/icons/cursor-grain-32.png
# ~/.local/share/icons/cursor-grain-16.png
```

### Personal Icon Override

Create `personal/cursor.personal.edn`:

```clojure
{:icon-override
 {:name "cursor-grain"  ;; Which template icon to override
  :source :web
  :web-url "https://cursor.com/brand/cursor-icon-2024.png"
  :cache true}}
```

Now `bb icon install cursor-grain` will use your personal web URL instead of the template SVG.

## Integration with Graincasks

Graincasks automatically uses Grainicons for AppImage icons:

```clojure
;; In casks/cursor.edn
{:cask
 {:name "Cursor"
  :icon
  {:source :grainicons
   :grainicon-name "cursor-grain"}}}  ;; References grainicons library

;; When you run: bb cask install cursor
;; Graincasks will:
;; 1. Look up "cursor-grain" in grainicons
;; 2. Check for personal override
;; 3. Generate PNG sizes if needed
;; 4. Install to ~/.local/share/icons/
;; 5. Reference in .desktop file
```

## Integration with GrainDisplay

GrainDisplay metadata can reference icons for content:

```clojure
{:grainmark-name "kae3g/forest-sunset"
 :display-metadata
 {:icon "forest-sunset"  ;; References grainicons
  :color-temperature 2000
  :display-mode :reading}}
```

## Template Icon Library

### Development Tools
- `cursor-grain` - Cursor IDE with Grain branding
- `vscode-grain` - VS Code with Grain theme
- `zed-grain` - Zed editor icon

### Grain Network Apps
- `grainmusic` - Music streaming platform
- `grainweb` - Decentralized browser
- `grainspace` - Unified platform
- `graindrive` - Google Drive integration

### Hardware Platforms
- `grainwriter` - E-ink writing device
- `graindroid` - Dual-display Android phone
- `graincamera` - AVIF camera
- `grainpack` - GPU attachment

### System Utilities
- `graindaemon` - Daemon supervisor
- `grainwifi` - Dual-wifi manager
- `graindisplay` - Display management
- `graincasks` - AppImage manager

## Icon Sources

### 1. Template Library (`:template`)

Built-in SVG icons in `template/` directory:

```clojure
{:source :template
 :template-path "template/cursor-grain.svg"}
```

### 2. Web URL (`:web`)

Download from web and cache:

```clojure
{:source :web
 :web-url "https://example.com/icon.png"
 :cache true  ;; Cache in ~/.cache/grainicons/
 :sha256 "abc123..."}  ;; Optional verification
```

### 3. Custom File (`:custom`)

Use local file:

```clojure
{:source :custom
 :custom-path "~/Pictures/my-icon.svg"}
```

### 4. Bundled (`:bundled`)

Extract from AppImage/application:

```clojure
{:source :bundled
 :appimage-path "~/.local/bin/cursor.appimage"
 :extract-icon true}
```

## Icon Format Conversion

### SVG → PNG Conversion

Uses ImageMagick or Inkscape:

```bash
# Using ImageMagick (preferred)
convert -background none -resize 512x512 input.svg output.png

# Using Inkscape (fallback)
inkscape --export-type=png --export-width=512 --export-filename=output.png input.svg
```

### Standard Icon Sizes

| Size | Use Case |
|------|----------|
| 512x512 | High-resolution source |
| 256x256 | Standard application icon |
| 128x128 | Medium size |
| 64x64 | Taskbar/panel |
| 48x48 | File browser |
| 32x32 | Small icons |
| 16x16 | System tray |

## Configuration

Global configuration in `config/grainicons.template.edn`:

```clojure
{:grainicons
 {:version "1.0.0"
  
  :paths
  {:template "template/"
   :personal "personal/"
   :cache "~/.cache/grainicons"
   :install "~/.local/share/icons"}
  
  :conversion
  {:tool :auto  ;; :auto, :imagemagick, :inkscape
   :sizes [512 256 128 64 48 32 16]
   :format :png
   :background :transparent}
  
  :cache
  {:enabled true
   :max-size-mb 100
   :expire-days 30}
  
  :web
  {:download-timeout 30
   :verify-checksums true
   :user-agent "Grainicons/1.0.0"}
  
  :graincasks
  {:auto-install true  ;; Auto-install icons for casks
   :generate-sizes true}  ;; Generate all PNG sizes
  
  :graindisplay
  {:metadata-icons true}}}  ;; Support display metadata icons
```

## Personal Configuration

Override in `personal/grainicons.personal.edn`:

```clojure
{:personal-preferences
 {:default-source :web  ;; Prefer web icons over template
  :cache-all true       ;; Cache all downloaded icons
  
  :icon-overrides
  {:cursor-grain
   {:source :web
    :web-url "https://cursor.com/brand/cursor-2024.png"}
   
   :grainmusic
   {:source :custom
    :custom-path "~/Pictures/my-grainmusic-icon.svg"}}}}
```

## Desktop File Integration

Grainicons automatically updates `.desktop` files:

```ini
[Desktop Entry]
Type=Application
Name=Cursor
Icon=cursor-grain  # References ~/.local/share/icons/cursor-grain.png
Exec=cursor %F
Categories=Development;IDE;
```

GNOME/KDE will automatically find the icon in `~/.local/share/icons/`.

## Icon Cache Management

```bash
# View cache status
bb icon cache status

# Clear cache
bb icon cache clear

# Rebuild all icons
bb icon cache rebuild
```

## Example: Cursor Icon Workflow

1. **Create SVG in template library**:
   ```bash
   # template/cursor-grain.svg created
   ```

2. **Generate PNG sizes**:
   ```bash
   bb icon generate cursor-grain
   ```

3. **Install for use**:
   ```bash
   bb icon install cursor-grain
   ```

4. **Reference in Graincasks**:
   ```clojure
   {:icon {:source :grainicons :grainicon-name "cursor-grain"}}
   ```

5. **Personal override** (optional):
   ```clojure
   ;; personal/cursor.personal.edn
   {:icon-override
    {:name "cursor-grain"
     :source :web
     :web-url "https://my-site.com/custom-cursor-icon.png"}}
   ```

## API Reference

### Clojure API

```clojure
(require '[grainicons.core :as icons])

;; Install icon
(icons/install "cursor-grain")

;; Generate PNG from SVG
(icons/generate-pngs "cursor-grain" {:sizes [512 256 128]})

;; Get icon path
(icons/get-icon-path "cursor-grain" 256)  ;; Returns ~/.local/share/icons/cursor-grain-256.png

;; Check if icon exists
(icons/exists? "cursor-grain")

;; List available icons
(icons/list-templates)
(icons/list-installed)

;; Get icon metadata
(icons/get-metadata "cursor-grain")
```

## Deployment

Grainicons is part of the Grain Network dual-deployment strategy:

- **GitHub**: https://github.com/grainpbc/grainicons (planned)
- **Codeberg**: https://codeberg.org/grainpbc/grainicons (planned - org not yet created)
- **Pages (GitHub)**: https://grainpbc.github.io/grainicons/ (planned)
- **Pages (Codeberg)**: https://grainpbc.codeberg.page/grainicons/ (planned)

**Note**: The `grainpbc` Codeberg organization has not been created yet. Currently developing in `kae3g/grainkae3g`.

## License

MIT License - See LICENSE file for details

## Related Projects

- **graincasks**: AppImage package manager (uses Grainicons)
- **graindisplay**: Display management with icon metadata
- **graindaemon**: Universal daemon framework
- **grainweb**: Decentralized browser platform
- **grainmusic**: Music streaming platform

---

**Part of the Grain Network** - Global Renewable technology for a sustainable future 🌾

