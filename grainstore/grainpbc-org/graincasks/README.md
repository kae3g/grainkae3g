# Graincasks: AppImage Package Manager for Grain Network

**Homebrew Casks-inspired AppImage management system**

Graincasks provides a declarative, version-controlled way to install, update, and manage AppImages on Linux, with support for automatic updates, icon management, and desktop integration.

## Why Not Linuxbrew?

While Linuxbrew (Homebrew on Linux) is excellent for traditional packages, it has limitations for AppImages:

1. **AppImages are self-contained** - They don't fit well into Homebrew's directory structure
2. **Update mechanisms differ** - AppImages often have built-in update systems (zsync, AppImageUpdate)
3. **Desktop integration** - AppImages need custom `.desktop` files and icon management
4. **Simplicity** - AppImages are already portable; adding Linuxbrew adds unnecessary complexity

**Graincasks** is purpose-built for AppImages, providing:
- ✅ Simple EDN-based cask definitions
- ✅ Automatic AppImageUpdate integration
- ✅ Icon and desktop file management
- ✅ Template/Personal configuration split
- ✅ Version tracking and rollback
- ✅ Integration with GrainDisplay and Grainicons

## Architecture

```
graincasks/
├── casks/                      # Cask definitions (template)
│   ├── cursor.edn
│   ├── obs-studio.edn
│   └── blender.edn
├── personal/                   # Personal cask overrides
│   └── cursor.personal.edn     # User-specific config
├── scripts/
│   ├── cask-install.bb         # Install an AppImage
│   ├── cask-update.bb          # Update installed AppImages
│   ├── cask-list.bb            # List installed casks
│   └── cask-search.bb          # Search available casks
├── config/
│   └── graincasks.template.edn # Global configuration
└── README.md
```

## Cask Definition Format

Casks are defined in EDN files with metadata about the AppImage:

```clojure
{:cask
 {:name "Cursor"
  :description "AI-first code editor"
  :homepage "https://cursor.com"
  :version "latest"
  
  :appimage
  {:url "https://downloader.cursor.sh/linux/appImage/x64"
   :sha256 nil  ;; Auto-verify on download
   :update-method :appimageupdate  ;; or :zsync, :manual, :github-releases
   :update-url nil}  ;; Auto-detected from AppImage
  
  :desktop
  {:name "Cursor"
   :generic-name "Code Editor"
   :comment "AI-first code editor"
   :exec "cursor %F"
   :icon "cursor"  ;; References grainicons
   :terminal false
   :categories ["Development" "IDE"]
   :mime-types ["text/plain" "inode/directory"]}
  
  :icon
  {:source :grainicons  ;; or :web, :bundled, :custom
   :grainicon-name "cursor-grain"  ;; From grainicons library
   :web-url nil  ;; If source is :web
   :custom-path nil}  ;; If source is :custom
  
  :install
  {:target "~/.local/bin/cursor.appimage"
   :symlink "~/.local/bin/cursor"
   :desktop-file "~/.local/share/applications/cursor.desktop"
   :icon-dir "~/.local/share/icons"}
  
  :metadata
  {:installed-at nil  ;; Filled on installation
   :last-updated nil
   :grainmark-path nil}}}
```

## Usage

### Install a Cask

```bash
bb cask install cursor
```

This will:
1. Download the Cursor AppImage
2. Install it to `~/.local/bin/cursor.appimage`
3. Create symlink at `~/.local/bin/cursor`
4. Install desktop file with icon
5. Make it executable
6. Integrate with AppImageUpdate for auto-updates

### Update All Casks

```bash
bb cask update
```

Updates all installed AppImages using their configured update method.

### List Installed Casks

```bash
bb cask list
```

Shows all installed casks with version information.

### Search for Casks

```bash
bb cask search editor
```

Searches available cask definitions.

### Uninstall a Cask

```bash
bb cask uninstall cursor
```

Removes AppImage, symlink, desktop file, and icon.

## AppImage Update Methods

### 1. AppImageUpdate (Recommended)

Uses the official AppImageUpdate tool with zsync for delta updates:

```bash
# Auto-configured in cask
:update-method :appimageupdate
```

### 2. GitHub Releases

Downloads from GitHub releases:

```clojure
:update-method :github-releases
:github-repo "owner/repo"
:asset-pattern #".*\.AppImage$"
```

### 3. Direct URL

Downloads from a static URL:

```clojure
:update-method :direct-url
:update-url "https://example.com/latest.AppImage"
```

### 4. Manual

User must manually update:

```clojure
:update-method :manual
```

## Personal Configuration

Create personal overrides in `personal/cursor.personal.edn`:

```clojure
{:cask-override
 {:name "cursor"  ;; Which cask to override
  :overrides
  {:icon
   {:source :web
    :web-url "https://cursor.com/brand/icon.png"}
   
   :install
   {:target "~/Apps/cursor.appimage"}
   
   :desktop
   {:exec "cursor --disable-gpu %F"}}}}
```

Personal configs take precedence over template casks.

## Integration with Grainicons

Graincasks integrates seamlessly with Grainicons for icon management:

```clojure
;; In cursor.edn
:icon
{:source :grainicons
 :grainicon-name "cursor-grain"}

;; Graincasks will automatically:
;; 1. Look up icon in grainicons library
;; 2. Install icon to ~/.local/share/icons
;; 3. Reference it in desktop file
```

## Configuration

Global configuration in `config/graincasks.template.edn`:

```clojure
{:graincasks
 {:version "1.0.0"
  
  :paths
  {:appimages "~/.local/bin"
   :desktop-files "~/.local/share/applications"
   :icons "~/.local/share/icons"
   :cache "~/.cache/graincasks"}
  
  :update
  {:auto-update false
   :check-interval 86400  ;; 24 hours
   :use-appimageupdate true}
  
  :appimageupdate
  {:binary "appimageupdatetool"
   :auto-install true  ;; Install AppImageUpdate if missing
   :download-url "https://github.com/AppImage/AppImageUpdate/releases/latest"}
  
  :grainicons
  {:enabled true
   :fallback-to-web true  ;; If grainicon not found, try web
   :cache-icons true}}}
```

## Example Casks

### Cursor (AI Code Editor)

```bash
bb cask install cursor
```

### OBS Studio (Streaming)

```bash
bb cask install obs-studio
```

### Blender (3D Creation)

```bash
bb cask install blender
```

## Creating Custom Casks

1. Create a new `.edn` file in `casks/`:

```clojure
;; casks/my-app.edn
{:cask
 {:name "My App"
  :description "My custom application"
  :appimage {:url "https://example.com/myapp.AppImage"}
  :desktop {:name "My App" :categories ["Utility"]}
  :icon {:source :web :web-url "https://example.com/icon.png"}}}
```

2. Install it:

```bash
bb cask install my-app
```

## Benefits Over Manual AppImage Management

| Feature | Manual | Graincasks |
|---------|--------|------------|
| Download & Install | Manual | Automated |
| Desktop Integration | Manual `.desktop` file | Auto-generated |
| Icon Management | Manual copy | Grainicons integration |
| Updates | Manual re-download | One command (`bb cask update`) |
| Version Control | None | Git-tracked EDN files |
| Rollback | Manual | Version history |
| Sharing Setup | Document steps | Share `.edn` files |

## Deployment

Graincasks is part of the Grain Network dual-deployment strategy:

- **GitHub**: https://github.com/grainpbc/graincasks (planned)
- **Codeberg**: https://codeberg.org/grainpbc/graincasks (planned - organization not yet created)
- **Pages (GitHub)**: https://grainpbc.github.io/graincasks/ (planned)
- **Pages (Codeberg)**: https://grainpbc.codeberg.page/graincasks/ (planned)

**Note**: The `grainpbc` Codeberg organization has not been created yet. Currently developing in `kae3g/grainkae3g` on both GitHub and Codeberg.

## License

MIT License - See LICENSE file for details

## Related Projects

- **grainicons**: Icon library for Grain Network apps
- **graindisplay**: Display management with metadata
- **grainclay**: Networked package manager
- **grainwriter**: Open-hardware laptop platform
- **graindroid**: Open-hardware Android phone

---

**Part of the Grain Network** - Global Renewable technology for a sustainable future 🌾

