# Grainconv - Universal Type Conversion for Grain Network

**Grainconv** is the Grain Network's equivalent to easy FFmpeg GUI tools and web-based converters like ezconv.com. It provides seamless conversion between Graintypes/Grainmarks for Grainweb posts, Grainclay file shares, and all Grain data formats.

## Vision

Just as ezconv.com converts YouTube videos to MP3 with a simple interface, Grainconv converts between any Grain data types with intelligent validation, transformation, and optimization.

## Features

- 🔄 **Universal Conversion**: Convert between any Grain data types
- 📝 **Grainframe Transformations**: Text → Poem → Document → Post
- 🎵 **Media Support**: Audio, Video, Images (inspired by FFmpeg)
- 🌐 **Web API**: REST API for conversions (like ezconv.com)
- 🖥️ **Desktop GUI**: Native Clojure Humble UI application
- 📱 **Mobile Support**: iOS/Android apps
- 🔒 **Type-Safe**: Uses Grainmarks for validation
- 🚀 **Grainclay Integration**: Direct file share support

## Supported Conversions

### Document Conversions

```
Markdown ←→ Grainframe ←→ PDF ←→ EDN ←→ JSON
                ↓
            Grainweb Post
                ↓
         Grainclay Package
```

### Media Conversions

```
Video (MP4, WebM, AVI) → Audio (MP3, FLAC, OGG)
                       → Grainframe (ASCII art preview)
                       → Metadata (EDN)

Audio → Waveform (SVG) → Grainframe visualization
     → Transcript (Whisper AI) → Grainframe text
```

### Image Conversions

```
AVIF ←→ PNG ←→ JPG ←→ WebP
  ↓
ASCII Art (Grainframe)
  ↓
Photo Description (Grainframe)
```

### Code Conversions

```
Clojure → Motoko (Clotoko transpiler)
        → JavaScript (ClojureScript)
        → Documentation (Grainframe)
```

## Architecture

```
┌─────────────────────────────────────────────┐
│         Grainconv Web Interface             │
│    (Svelte, inspired by ezconv.com)         │
└──────────────────┬──────────────────────────┘
                   │
         ┌─────────┴─────────┐
         │                   │
         ▼                   ▼
  ┌──────────┐        ┌──────────┐
  │  Web API │        │ Desktop  │
  │  (REST)  │        │   GUI    │
  └────┬─────┘        └────┬─────┘
       │                   │
       └─────────┬─────────┘
                 │
                 ▼
      ┌──────────────────┐
      │  Grainconv Core  │
      │   (Clojure)      │
      └────────┬─────────┘
               │
      ┌────────┴────────┐
      │                 │
      ▼                 ▼
┌──────────┐     ┌──────────┐
│Grainmarks│     │ Grainclay│
│Validator │     │  Sync    │
└──────────┘     └──────────┘
```

## Usage Examples

### Web Interface (ezconv.com style)

```html
<!-- Simple drag-and-drop interface -->
<div class="grainconv">
  <input type="file" accept="*/*" />
  <select>
    <option>Markdown → Grainframe</option>
    <option>Grainframe → PDF</option>
    <option>Video → Audio (MP3)</option>
    <option>Image → ASCII Art</option>
  </select>
  <button>Convert</button>
</div>
```

### API Usage

```bash
# Convert YouTube video to Grainframe transcript
curl -X POST https://grainconv.network/api/convert \
  -H "Content-Type: application/json" \
  -d '{
    "source": "https://youtube.com/watch?v=...",
    "source_type": "youtube-video",
    "target_type": "grainframe-transcript",
    "options": {
      "language": "en",
      "max_frames": 10
    }
  }'

# Convert Markdown to Grainweb post
curl -X POST https://grainconv.network/api/convert \
  -H "Content-Type: application/json" \
  -d '{
    "source": "# My Post\n\nContent here...",
    "source_type": "markdown",
    "target_type": "grainweb-post",
    "options": {
      "author": "kae3g",
      "tags": ["tutorial"]
    }
  }'
```

### Desktop GUI (Clojure)

```clojure
(ns grainconv.gui
  (:require [grainconv.core :as conv]
            [humble.ui :as ui]))

(defn convert-file [source-path target-type]
  (conv/convert {:source (slurp source-path)
                 :source-type (conv/detect-type source-path)
                 :target-type target-type}))

(defn grainconv-window []
  (ui/window
    {:title "Grainconv - Universal Grain Converter"}
    (ui/column
      (ui/file-picker {:on-select #(reset! source-file %)})
      (ui/dropdown {:options conversion-types})
      (ui/button {:text "Convert"
                  :on-click convert-and-save}))))
```

### Clojure API

```clojure
(require '[grainconv.core :as conv])

;; Convert Markdown to Grainframe
(def my-grainframe
  (conv/convert {:source "# Hello\n\nWorld!"
                 :source-type :markdown
                 :target-type :grainframe}))

;; Convert Grainframe to Grainweb post
(def my-post
  (conv/convert {:source my-grainframe
                 :source-type :grainframe
                 :target-type :grainweb-post
                 :options {:author "kae3g"
                          :visibility :public}}))

;; Convert video to audio
(conv/convert {:source "video.mp4"
               :source-type :video
               :target-type :audio-mp3
               :options {:bitrate "320k"
                        :output "audio.mp3"}})
```

## Conversion Pipeline

```clojure
;; Multi-stage conversion pipeline
(-> "my-essay.md"
    (conv/convert :markdown :grainframe)
    (conv/convert :grainframe :grainweb-post)
    (conv/validate :grainweb-post)
    (conv/publish :grainclay)
    (conv/deploy [:website :mobile :desktop]))
```

## Grainmark Integration

Every conversion validates using Grainmarks:

```clojure
(defn convert-with-validation
  [source source-type target-type]
  
  ;; 1. Detect source type
  (let [detected-type (or source-type (detect-type source))]
    
    ;; 2. Validate source
    (when-not (mark/validate detected-type source)
      (throw (ex-info "Invalid source data" {:type detected-type})))
    
    ;; 3. Convert
    (let [result (perform-conversion source detected-type target-type)]
      
      ;; 4. Validate result
      (when-not (mark/validate target-type result)
        (throw (ex-info "Invalid conversion result" {:type target-type})))
      
      ;; 5. Return with metadata
      {:data result
       :source-type detected-type
       :target-type target-type
       :timestamp (java.time.Instant/now)
       :valid? true})))
```

## Installation

### Desktop App

```bash
# Homebrew (macOS/Linux)
brew install grainpbc/tap/grainconv

# Nix
nix-env -iA nixpkgs.grainconv

# Arch Linux
yay -S grainconv

# Debian/Ubuntu
sudo apt install grainconv
```

### Web Service

```bash
# Deploy to your own server
docker pull grainpbc/grainconv:latest
docker run -p 8080:8080 grainpbc/grainconv
```

### Development

```bash
# Clone repository
git clone https://github.com/grainpbc/grainconv
cd grainconv

# Install dependencies
clojure -P

# Run development server
clojure -M:dev
```

## Supported Types

### Input Types

- Markdown (`.md`)
- HTML (`.html`)
- PDF (`.pdf`)
- EDN (`.edn`)
- JSON (`.json`)
- YAML (`.yaml`)
- Video (`.mp4`, `.webm`, `.avi`, `.mov`)
- Audio (`.mp3`, `.flac`, `.ogg`, `.wav`)
- Image (`.avif`, `.png`, `.jpg`, `.webp`)
- Code (`.clj`, `.mo`, `.js`, `.ts`)

### Output Types

- Grainframe (80×110 text)
- Grainweb Post (social media)
- Grainclay Package (file share)
- PDF (document)
- Audio (various formats)
- Video (various formats)
- Image (various formats)

## API Reference

### Core Functions

```clojure
(convert source source-type target-type & options)
;; Main conversion function

(detect-type source)
;; Auto-detect source type

(validate-conversion source target-type)
;; Validate before converting

(optimize conversion-result)
;; Optimize output (compress, etc.)

(publish-to-grainclay result)
;; Publish to Grainclay network
```

### Conversion Options

```clojure
{:quality "high"           ; Output quality (low/medium/high)
 :optimize? true           ; Optimize file size
 :preserve-metadata? true  ; Keep original metadata
 :grainclay-sync? true     ; Auto-sync to Grainclay
 :grainweb-post? false     ; Publish as Grainweb post
 :audio-bitrate "320k"     ; Audio quality
 :video-codec "h264"       ; Video codec
 :image-format "avif"      ; Image format
 :max-grainframes 10}      ; Max Grainframes for pagination
```

## Comparison to Existing Tools

| Feature | ezconv.com | FFmpeg | Grainconv |
|---------|-----------|--------|-----------|
| Web Interface | ✅ | ❌ | ✅ |
| Desktop App | ❌ | ❌ | ✅ |
| Mobile App | ❌ | ❌ | ✅ |
| YouTube Support | ✅ | ✅ | ✅ |
| Type Validation | ❌ | ❌ | ✅ |
| Network Sync | ❌ | ❌ | ✅ (Grainclay) |
| Open Source | ❌ | ✅ | ✅ |
| API | Limited | CLI | Full REST API |
| Grainframe Support | ❌ | ❌ | ✅ |

## Privacy & Security

- 🔒 **No Data Collection**: Conversions happen locally or on your server
- 🔐 **End-to-End Encryption**: Optional E2E for Grainclay sync
- 🌐 **Self-Hostable**: Run your own Grainconv instance
- 📜 **Open Source**: Fully auditable code
- 🚫 **No Ads**: Ad-free experience (unlike many web converters)

## Roadmap

- [ ] AI-powered conversion optimization
- [ ] Real-time streaming conversion
- [ ] Blockchain integration for provenance
- [ ] Plugin system for custom converters
- [ ] Batch conversion queue
- [ ] Cloud storage integration
- [ ] WebAssembly for in-browser conversion
- [ ] Grainframe AI generation

## Contributing

See [CONTRIBUTING.md](./CONTRIBUTING.md) for guidelines.

## License

GPL-3.0 - Open source and free forever

## Links

- **Website**: https://grainconv.network
- **Documentation**: https://docs.grainconv.network
- **GitHub**: https://github.com/grainpbc/grainconv
- **API Docs**: https://api.grainconv.network/docs

---

**Built with ❤️ by the Grain Network community**

