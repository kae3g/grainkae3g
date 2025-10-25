# graintranscribe-youtube

**YouTube Video Transcription using Gemini 2.5 Pro**

A grain6pbc template module for transcribing YouTube videos using Google's Gemini 2.5 Pro AI, part of Google AI Premium ($20/month) or Google One AI Premium ($19.99/month with 2TB storage).

## 🌾 Philosophy

**Template/Personal Separation** - Share defaults, preserve customization

- `template/` - Default configuration and examples (version controlled)
- `personal/` - Your API keys and preferences (gitignored)

## 🎯 Why Gemini 2.5 Pro?

### Multimodal Video Understanding

Gemini 2.5 Pro can:
- **Watch and analyze YouTube videos** directly from URLs
- **Transcribe audio** with speaker identification
- **Understand visual context** (slides, demos, code on screen)
- **Extract key points** and generate summaries
- **Answer questions** about video content

### Pricing Comparison

| Service | Price | Features |
|---------|-------|----------|
| **Google AI Premium** | $20/month | Gemini 2.5 Pro access, 1M token context, multimodal |
| **Google One AI Premium** | $19.99/month | Gemini 2.5 Pro + 2TB storage + YouTube Premium benefits |
| YouTube Premium | $13.99/month | Ad-free, background play, downloads (no AI) |

**Recommended**: Google One AI Premium combines YouTube Premium benefits with Gemini 2.5 Pro access for just $6 more than regular YouTube Premium.

## 🚀 Quick Start

### 1. Setup Configuration

```bash
cd grainstore/grain6pbc/graintranscribe-youtube
bb config:setup
```

This copies `template/config.edn` to `personal/config.edn`.

### 2. Add Your API Key

Edit `personal/config.edn`:

```clojure
{:gemini
 {:api-key "YOUR_GEMINI_API_KEY_HERE"  ; Get from https://aistudio.google.com/apikey
  :model "gemini-2.5-pro-latest"
  :max-tokens 1000000}  ; 1M context window
 
 :output
 {:dir "personal/transcriptions"
  :format :markdown  ; or :text, :json, :edn
  :include-timestamps true
  :include-speakers true
  :include-summary true}
 
 :youtube
 {:extract-metadata true
  :download-thumbnail false}}
```

### 3. Transcribe a Video

```bash
# Single video
bb transcribe:video "https://www.youtube.com/watch?v=gSW3YJ8uyBI"

# Batch transcription
echo "https://www.youtube.com/watch?v=gSW3YJ8uyBI" >> urls.txt
bb transcribe:batch urls.txt
```

### 4. View Results

```bash
bb output:list
cat personal/transcriptions/gSW3YJ8uyBI.md
```

## 📖 Features

### Advanced Transcription

- **Speaker Identification**: Detects different speakers
- **Timestamp Markers**: Precise timing for every section
- **Visual Context**: Describes slides, code, and demos shown
- **Smart Summarization**: Key points and main ideas
- **Q&A Extraction**: Identifies questions and answers

### Output Formats

**Markdown** (default):
```markdown
# Video Title
**Speaker**: John Doe
**Duration**: 45:30

## Summary
[AI-generated summary]

## Transcript
[00:00:15] Speaker 1: Welcome to...
[00:01:30] Speaker 2: Great question...
```

**JSON**:
```json
{
  "video_id": "gSW3YJ8uyBI",
  "title": "...",
  "transcript": [...],
  "summary": "...",
  "speakers": [...]
}
```

**EDN** (Clojure):
```clojure
{:video-id "gSW3YJ8uyBI"
 :title "..."
 :transcript [...]
 :summary "..."
 :speakers [...]}
```

## 🔧 Configuration Options

### Template Configuration (`template/config.edn`)

```clojure
{:gemini
 {:api-key "TEMPLATE_VALUE_REPLACE_ME"
  :model "gemini-2.5-pro-latest"
  :temperature 0.2  ; Lower = more factual
  :max-tokens 1000000
  :safety-settings {:harassment :block-none
                    :hate-speech :block-none
                    :sexually-explicit :block-low-and-above
                    :dangerous-content :block-low-and-above}}
 
 :output
 {:dir "personal/transcriptions"
  :format :markdown
  :filename-pattern "{video-id}_{date}"  ; or "{title}", "{video-id}"
  :include-timestamps true
  :include-speakers true
  :include-summary true
  :include-key-points true
  :include-visual-descriptions true
  :chunk-size 5  ; minutes per chunk for long videos
  :overwrite false}  ; Don't overwrite existing transcriptions
 
 :youtube
 {:extract-metadata true
  :download-thumbnail false
  :quality-preference :highest  ; or :medium, :lowest
  :language-hint "en"  ; Help with transcription accuracy
  }
 
 :grainpath
 {:generate true  ; Create grainpath-stamped output
  :use-graintime true}  ; Use gt for timestamps
}
```

### Personal Configuration (`personal/config.edn`)

Override any template values:

```clojure
{:gemini
 {:api-key "your-actual-api-key"}
 
 :output
 {:format :edn  ; I prefer EDN format
  :include-visual-descriptions false}  ; Skip visual descriptions
 
 :youtube
 {:language-hint "es"}}  ; Spanish videos
```

The system **merges** template and personal configs, with personal taking precedence.

## 📋 Usage Examples

### Developer Conference Talk

```bash
bb transcribe:video "https://www.youtube.com/watch?v=gSW3YJ8uyBI"
```

Output includes:
- Full transcript with timestamps
- Speaker identification
- Code snippets shown on screen
- Slide content descriptions
- Q&A section extraction

### Batch Educational Content

```bash
# Create list of course videos
cat > course-videos.txt <<EOF
https://www.youtube.com/watch?v=video1
https://www.youtube.com/watch?v=video2
https://www.youtube.com/watch?v=video3
EOF

bb transcribe:batch course-videos.txt
```

### Custom Processing

```clojure
;; scripts/custom-transcript.bb
(require '[graintranscribe.core :as gt])

(def video-id "gSW3YJ8uyBI")
(def transcript (gt/transcribe-video video-id))

;; Extract only code snippets
(def code-blocks 
  (filter #(re-find #"```" (:text %)) 
          (:chunks transcript)))

;; Save to grainpath
(gt/save-transcript transcript 
  {:format :markdown
   :grainpath (gt/generate-grainpath)})
```

## 🌐 Gemini 2.5 Pro API Integration

### Direct API Call

```bash
curl https://generativelanguage.googleapis.com/v1/models/gemini-2.5-pro:generateContent \
  -H 'Content-Type: application/json' \
  -H 'x-goog-api-key: YOUR_API_KEY' \
  -d '{
    "contents": [{
      "parts": [{
        "text": "Transcribe this video with timestamps and speaker identification:",
        "video_url": "https://www.youtube.com/watch?v=gSW3YJ8uyBI"
      }]
    }],
    "generationConfig": {
      "maxOutputTokens": 100000,
      "temperature": 0.2
    }
  }'
```

### Babashka Integration (graintranscribe)

```clojure
(ns graintranscribe.core
  (:require [clj-http.client :as http]
            [cheshire.core :as json]
            [babashka.fs :as fs]))

(defn call-gemini-api
  [prompt video-url api-key]
  (let [response (http/post 
                   "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-pro:generateContent"
                   {:headers {"Content-Type" "application/json"
                             "x-goog-api-key" api-key}
                    :body (json/generate-string
                            {:contents [{:parts [{:text prompt
                                                   :video_url video-url}]}]
                             :generationConfig {:maxOutputTokens 100000
                                              :temperature 0.2}})})]
    (-> response :body json/parse-string)))
```

## 🔐 Security

### API Key Management

**DO NOT** commit your API key to git:

```bash
# personal/.gitignore (already configured)
personal/*.edn
personal/transcriptions/
personal/*.key
```

### Grainenvvars Integration

For enhanced security, integrate with `grainenvvars`:

```clojure
;; personal/config.edn
{:gemini
 {:api-key {:grainenvvars "GEMINI_API_KEY"}}  ; Load from grainenvvars
}
```

```bash
# In grainenvvars personal/.env
export GEMINI_API_KEY="your-actual-key"
```

## 📊 Cost Analysis

### Google AI Premium vs YouTube Premium

**Standalone YouTube Premium**: $13.99/month
- Ad-free YouTube
- Background play
- YouTube Music
- Downloads for offline

**Google One AI Premium**: $19.99/month
- Everything in YouTube Premium
- Gemini 2.5 Pro access
- 2TB Google Drive storage
- Family sharing (up to 5 members)
- Google Photos features

**Value Proposition**: For $6 more than YouTube Premium, you get:
- Advanced AI transcription
- 1M token context window
- Multimodal understanding
- 2TB storage ($100/year value)

### API Usage Costs

Gemini 2.5 Pro API pricing (separate from subscriptions):
- **Input**: $3.50 per 1M tokens
- **Output**: $10.50 per 1M tokens

**Example video transcription**:
- 1-hour video ≈ 15,000 tokens input
- Transcript ≈ 30,000 tokens output
- **Cost**: ~$0.37 per hour of video

For $20/month subscription, you get unlimited usage (rate limited).

## 🎓 Use Cases

### 1. Developer Conference Talks

Transcribe technical talks with code examples:

```bash
bb transcribe:video "https://www.youtube.com/watch?v=conf-talk-id" \
  --include-code-snippets \
  --extract-slides
```

### 2. Course Material

Create searchable transcripts for entire courses:

```bash
bb transcribe:batch course-playlist.txt \
  --output-format edn \
  --include-key-points
```

### 3. Meeting Notes

Transcribe recorded meetings:

```bash
bb transcribe:video "https://www.youtube.com/watch?v=meeting-id" \
  --include-action-items \
  --include-speakers
```

### 4. Research

Analyze video content for research:

```bash
bb transcribe:video "https://www.youtube.com/watch?v=research-vid" \
  --include-citations \
  --extract-data-points
```

## 🔄 Integration with Other Modules

### With graincourse

```bash
# Transcribe lecture and add to course
bb transcribe:video "https://www.youtube.com/watch?v=lecture-id"
bb graincourse:add-content transcription.md
```

### With graintime

```bash
# Use graintime timestamps
bb transcribe:video "https://www.youtube.com/watch?v=vid" \
  --graintime-stamps
```

### With grainbarrel

```bash
# Add to build pipeline
gb graintranscribe:batch course-videos.txt
gb graincourse:build
```

## 📦 Installation

### 1. Clone or Symlink

If you're using this as a template:

```bash
# In your personal repo
cd grainstore/
git clone https://github.com/grain6pbc/graintranscribe-youtube
```

Or as a submodule:

```bash
git submodule add https://github.com/grain6pbc/graintranscribe-youtube \
  grainstore/grain6pbc/graintranscribe-youtube
```

### 2. Setup Personal Config

```bash
cd grainstore/grain6pbc/graintranscribe-youtube
bb config:setup
# Edit personal/config.edn with your API key
```

### 3. Validate

```bash
bb config:validate
```

## 🧪 Testing

```bash
# Run tests
bb test

# Test with a short video
bb transcribe:video "https://www.youtube.com/watch?v=short-test-id"
```

## 📖 API Reference

See [docs/API.md](docs/API.md) for complete function reference.

## 🤝 Contributing

This is a grain6pbc template module. Contributions should maintain template/personal separation.

## 📄 License

MIT License - See [LICENSE](LICENSE)

## 🔗 Links

- **Gemini API**: https://ai.google.dev/gemini-api
- **Google AI Studio**: https://aistudio.google.com
- **Google One AI Premium**: https://one.google.com/about/plans
- **Grainpbc**: https://github.com/grain6pbc

---

**Built with 🌾 for sovereign AI transcription and knowledge extraction**

*"From videos to grains of wisdom"*
