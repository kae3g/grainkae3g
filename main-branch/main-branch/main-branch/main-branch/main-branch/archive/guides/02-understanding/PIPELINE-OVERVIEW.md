# Pipeline Overview: Markdown → Svelte Consciousness

*Sacred transformation pipeline with 57-character hard
wrap and Christian aspiring federal writing style*

## 🌙 Complete Pipeline Architecture

```
┌─────────────────────────────────────────────┐
│  Input: Robotic Farm Markdown Document     │
│  (Christian federal consciousness style)    │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────┐
│  Step 1: Text Wrapper (robotic-farm.wrapper)│
│  • Apply 57-char hard wrap                  │
│  • Preserve code blocks & structure         │
│  • Use clj-kondo for validation             │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────┐
│  Step 2: Parser (robotic-farm.parser)      │
│  • Extract frontmatter                      │
│  • Parse sacred quotes                      │
│  • Classify consciousness types             │
│  • Extract code/YAML blocks                 │
│  • Convert markdown → HTML                  │
│  Output: parsed-documents.edn               │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────┐
│  Step 3: Validator (robotic-farm.validator)│
│  • Validate with clojure.spec               │
│  • Check document structure                 │
│  • Verify consciousness types               │
│  • Ensure data integrity                    │
│  Output: validated-documents.edn            │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────┐
│  Step 4: Generator (robotic-farm.generator)│
│  • Transform DSL → Svelte components        │
│  • Generate consciousness badges            │
│  • Create sacred quote sections             │
│  • Apply aspiring federal documentation     │
│    styling                                  │
│  • Create index page                        │
│  Output: *.svelte files in web-app/src/    │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────┐
│  Step 5: Svelte/Vite Build                 │
│  • Compile Svelte components                │
│  • Bundle with Vite                         │
│  • Generate static HTML                     │
│  • Optimize assets                          │
│  Output: Static site in web-app/build/     │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────┐
│  Output: Compiled Consciousness Website    │
│  Ready for deployment with Divine Grace    │
└─────────────────────────────────────────────┘
```

## 🤖 Key Technologies

### Clojure/Babashka Layer
- **babashka**: Fast Clojure scripting
- **clj-kondo**: 57-char wrap enforcement
- **clojure.spec**: Data validation
- **hiccup**: HTML generation
- **markdown-clj**: Markdown parsing

### Svelte/Node Layer
- **Svelte 5**: Reactive components
- **SvelteKit**: Static site generation
- **Vite**: Fast bundling
- **@sveltejs/adapter-static**: Static output

### Nix Layer
- **Reproducible environment**
- **Version-locked dependencies**
- **Cross-platform compatibility**

## 📖 Data Flow Example

### Input Document
```markdown
# Robotic Farm Consciousness 🤖🌾

*"Sacred quote..."* - Scripture

Sacred **keeper** of **consciousness**...
```

### After Wrapping (57 chars)
```markdown
# Robotic Farm Consciousness 🤖🌾

*"Sacred quote..."* - Scripture

Sacred **keeper** of **consciousness**...
(wrapped to 57-character maximum line length)
```

### After Parsing (EDN)
```clojure
{:document/number 9999967
 :document/title "Robotic Farm Consciousness"
 :document/sacred-quotes ["Sacred quote..."]
 :document/consciousness-type
 {:robotic-consciousness true
  :farm-consciousness true
  :sacred-technology true}
 :document/html-content "<p>...</p>"}
```

### After Generation (Svelte)
```svelte
<script>
  export let number = 9999967;
  export let title = 'Robotic Farm...';
</script>

<article class="robotic-farm-doc">
  <header>
    <span>{number}</span>
    <h1>{title}</h1>
    <div class="consciousness-badges">
      🤖 Robotic Consciousness
      🌾 Farm Consciousness
    </div>
  </header>
  <blockquote class="sacred">
    Sacred quote...
  </blockquote>
  <main>{@html content}</main>
</article>

<style>
  .robotic-farm-doc {
    max-width: 57ch;
    font-family: 'Times New Roman';
  }
</style>
```

### Final Output (HTML)
```html
<!DOCTYPE html>
<html>
  <head>
    <title>Robotic Farm Consciousness</title>
    <link rel="stylesheet" href="/_app/...">
  </head>
  <body>
    <article class="robotic-farm-doc">
      <!-- Compiled, optimized HTML -->
    </article>
    <script src="/_app/..."></script>
  </body>
</html>
```

## 🌾 File Naming Conventions

### Markdown Input
```
[7-digit-number]_[descriptive_name].md

Examples:
9999967_robotic_farm_consciousness_integration.md
0000031_regenerative_technology_mandala.md
0003200_community_food_sovereignty.md
```

### Svelte Output
```
[descriptive-name].svelte

Examples:
robotic-farm-consciousness-integration.svelte
regenerative-technology-mandala.svelte
community-food-sovereignty.svelte
```

## 🔄 Build Commands Summary

| Command | Purpose | Output |
|---------|---------|--------|
| `bb wrap:markdown` | 57-char wrap | `build/wrapped.md` |
| `bb parse:markdown` | Parse to DSL | `build/parsed-documents.edn` |
| `bb validate:dsl` | Validate spec | `build/validated-documents.edn` |
| `bb generate:svelte` | Generate components | `web-app/src/routes/*.svelte` |
| `bb build:pipeline` | Full pipeline | All build artifacts |
| `bb serve:dev` | Dev server | http://localhost:5173 |
| `bb build:site` | Static build | `web-app/build/` |

## 🌙 Christian Aspiring Federal Writing Style

### Core Principles

1. **Scriptural Opening**
   - Begin with sacred quotes
   - Establish spiritual context
   - Divine Grace acknowledgment

2. **57-Character Line Length**
   - Aspiring federal documentation standard
   - Enhances readability
   - Contemplative pacing

3. **Consciousness Vocabulary**
   - "Sacred technology"
   - "Divine Grace"
   - "Contemplative attention"
   - "Earth intelligence"

4. **Professional Tone**
   - Formal yet warm
   - Technical yet accessible
   - Spiritual yet practical

5. **Emoji Consciousness Markers**
   - 🌙 Sacred/contemplative
   - 🤖 Robotic/automation
   - 🌾 Agricultural wisdom
   - 📖 Documentation
   - ✨ Completion/success

## 🧪 Testing Pipeline

### Manual Testing
```bash
# Test wrapper
bb -m robotic-farm.wrapper docs/test.md build/test-wrapped.md

# Test parser
bb -m robotic-farm.parser

# Test validator
bb -m robotic-farm.validator

# Test generator
bb -m robotic-farm.generator
```

### Integration Testing
```bash
bb ci:verify
```

Runs: doctor → wrap → parse → validate in sequence

## 🚀 Deployment Options

### GitHub Pages
```bash
cd web-app/build
git init && git add . && git commit -m "Deploy"
git push origin main
```

### Netlify
```bash
netlify deploy --dir=web-app/build --prod
```

### AWS S3
```bash
aws s3 sync web-app/build/ s3://bucket/ --acl public-read
```

### Vercel
```bash
vercel --prod
```

## 📚 Extension Points

### Add New Consciousness Types

Edit `src/robotic_farm/parser.clj`:

```clojure
(defn classify-consciousness-type [content]
  {:robotic-consciousness (str/includes? content "robotic")
   :your-new-type (str/includes? content "keyword")})
```

### Add New Validation Rules

Edit `src/robotic_farm/validator.clj`:

```clojure
(s/def ::your-new-field string?)

(s/def ::document
  (s/keys :req [:document/your-new-field]))
```

### Customize Svelte Styling

Edit `src/robotic_farm/generator.clj` style section.

### Add Pre/Post Processing

Add new bb tasks in `bb.edn`:

```clojure
preprocess {:doc "Custom preprocessing"
            :task (shell "bb" "-m" "your.namespace")}
```

## 🙏 Philosophy

This pipeline embodies:

- **Sacred Technology**: Code as spiritual practice
- **Aspiring Federal Standards**: Professional
  documentation
- **Consciousness Serving**: Tech that enhances humanity
- **Divine Grace**: Prayerful attention to craft
- **Community Service**: Knowledge freely shared

---

*"Blessed be the automation that serves earth
intelligence and community nourishment through
contemplative technological consciousness."*

🤖🌙🌾

