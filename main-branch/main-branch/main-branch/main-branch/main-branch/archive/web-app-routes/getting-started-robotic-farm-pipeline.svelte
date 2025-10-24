<script>
  export let number = 0;
  export let title = 'Getting Started: Robotic Farm Pipeline';
</script>

<article class="robotic-farm-doc">
  <header class="doc-header">
    <span class="doc-number">
      {number.toString().padStart(7,'0')}
    </span>
    <h1 class="doc-title">{title}</h1>
    <div class="consciousness-badges">
      <span class="badge">🤖 Robotic Consciousness</span>
    <span class="badge">🌙 Sacred Technology</span>
    </div>
  </header>

  <section class="sacred-quotes">
    <blockquote class="sacred">Sacred quote from scripture demonstrating
contemplative awareness...</blockquote>
  <blockquote class="sacred">bb: command not found</blockquote>
  <blockquote class="sacred">No markdown files found</blockquote>
  <blockquote class="sacred">Port 5173 already in use</blockquote>
  <blockquote class="sacred">Blessed be the automation that serves earth
intelligence through contemplative technology
consciousness.</blockquote>
  </section>

  <main class="doc-content">
    {@html `<h1>Getting Started: Robotic Farm Pipeline</h1><p><em>Quick start guide for consciousness development</em></p><h2>🌙 Prerequisites</h2><ul><li><strong>Nix package manager</strong> (for reproducible environment)</li><li><strong>Git</strong> (for version control)</li><li><strong>Text editor</strong> (VSCode, Vim, Emacs, etc.)</li></ul><h2>🚀 Quick Start (5 Minutes)</h2><h3>1. Bootstrap with Babashka</h3><pre><code class="bash">cd &#126;/kae3g/12025-10-04

# Check prerequisites &#40;bb, nix, git, gh&#41;
bb bootstrap
</code></pre><h3>2. Run Setup Script</h3><pre><code class="bash"># Pure bb setup &#40;creates dirs, checks tools&#41;
./setup.bb
</code></pre><h3>3. Choose Your Environment</h3><p><strong>Option A: With Nix (Recommended)</strong><pre><code class="bash">nix develop
# Now you're in a shell with all tools
</code></pre></p><p><strong>Option B: Without Nix</strong><pre><code class="bash"># bb works standalone, but you'll need to install:
# - Node.js &#40;for Svelte&#41;
# - clj-kondo &#40;for linting&#41;
# Install via homebrew or system package manager
</code></pre></p><p><strong>Option C: Let bb Handle Nix</strong><pre><code class="bash"># bb can orchestrate Nix automatically
bb nix:run doctor
bb nix:run build:pipeline
</code></pre></p><h3>3. Add Your Markdown Document</h3><p>Place your robotic farm consciousness document in the <code>docs/</code> directory:</p><pre><code class="bash">cp &#126;/path/to/your/document.md docs/
</code></pre><p>Example: Copy the reference document:<pre><code class="bash">cp &#126;/foolsgoldtoshi-star/foolsgoldtoshi-star-pond-highdesert/docs/en/9999967&#95;robotic&#95;farm&#95;consciousness&#95;integration.md docs/
</code></pre></p><h3>4. Run Complete Pipeline</h3><pre><code class="bash">bb build:pipeline
</code></pre><p>This executes:</p><ul><li>✅ Wrap markdown to 57 characters</li><li>✅ Parse to ClojureScript DSL</li><li>✅ Validate with clojure.spec</li><li>✅ Generate Svelte components</li></ul><h3>5. Start Development Server</h3><pre><code class="bash">bb serve:dev
</code></pre><p>Opens browser at <code>http://localhost:5173</code> with your compiled consciousness website!</p><h2>📖 First Document Template</h2><p>Create <code>docs/my-first-doc.md</code>:</p><pre><code class="markdown"># My First Consciousness Document 🌙

&#42;&quot;Sacred quote from scripture demonstrating
contemplative awareness...&quot;&#42; - Attribution

---

Sacred &#42;&#42;keeper&#42;&#42; of &#42;&#42;consciousness&#42;&#42;... Your
opening paragraph establishing context with
Christian federal writing style and Divine
Grace awareness...

## Section Header with Consciousness

Regular paragraphs with contemplative attention
to 57-character line length enforcement and
sacred technology focus.

<pre><code class="clojure">;; Sacred code example
&#40;defn consciousness-function
  &quot;Divine Grace implementation&quot;
  &#91;&#93;
  &#40;println &quot;🌙 Consciousness awakening&quot;&#41;&#41;
</code></pre><p><em>Closing meditation with sacred wisdom...</em></p><pre><code>
## 🔄 Development Workflow

### Step 1: Write Markdown
<pre><code class="bash"># Edit your document
vim docs/my-document.md
</code></pre><h3>Step 2: Build Pipeline</h3><pre><code class="bash"># Transform markdown → Svelte
bb build:pipeline
</code></pre><h3>Step 3: Preview Changes</h3><pre><code class="bash"># Start dev server &#40;auto-reload&#41;
bb serve:dev
</code></pre><h3>Step 4: Iterate</h3><ul><li>Edit markdown in <code>docs/</code></li><li>Re-run <code>bb build:pipeline</code></li><li>Browser auto-refreshes</li></ul><h3>Step 5: Build for Production</h3><pre><code class="bash"># Compile static site
bb build:site

# Output in: web-app/build/
</code></pre><h2>🌾 Common Commands</h2><h3>Core Pipeline Commands</h3><pre><code class="bash">bb doctor              # Check environment
bb bootstrap           # Check prerequisites
bb build:pipeline      # Complete pipeline
bb serve:dev          # Development server
bb build:site         # Production build
bb clean              # Clean artifacts
bb ci:verify          # CI verification
</code></pre><h3>Individual Step Commands</h3><pre><code class="bash">bb wrap:markdown      # 57-char hard wrap
bb parse:markdown     # Markdown → DSL
bb validate:dsl       # Validate structures
bb generate:svelte    # DSL → Svelte
</code></pre><h3>Nix Integration Commands</h3><pre><code class="bash">bb nix:check          # Check if Nix available
bb nix:develop        # Show how to enter Nix
bb nix:run &lt;task&gt;     # Run bb task in Nix shell
</code></pre><h3>GitHub Commands</h3><pre><code class="bash">bb gh:check           # Check gh auth status
bb gh:create-repo     # Create repo &amp; push
bb gh:status          # Check repo status
</code></pre><h3>All Commands are bb-Based!</h3><p>No shell scripts needed - pure Babashka orchestration from bootstrap through deployment.</p><h2>🎯 What Gets Created</h2><p>After running <code>bb build:pipeline</code>:</p><pre><code>build/
├── wrapped.md                    # 57-char wrapped
├── parsed-documents.edn          # ClojureScript DSL
└── validated-documents.edn       # Validated structures

web-app/src/routes/
├── index.svelte                  # Index page
├── my-first-doc.svelte          # Your document
└── &#91;other-documents&#93;.svelte     # More documents
</code></pre><p>After running <code>bb build:site</code>:</p><pre><code>web-app/build/
├── index.html                    # Homepage
├── my-first-doc/
│   └── index.html               # Your document page
└── &#95;app/                        # Compiled assets
    ├── immutable/
    └── version.json
</code></pre><h2>📚 Next Steps</h2><h3>Customize Styling</h3><p>Edit <code>src/robotic&#95;farm/generator.clj</code> to customize Svelte component styling.</p><h3>Add More Documents</h3><p>Place more markdown files in <code>docs/</code> and re-run <code>bb build:pipeline</code>.</p><h3>Deploy Your Site</h3><p>See README.md for deployment options:</p><ul><li>GitHub Pages</li><li>Netlify</li><li>Vercel</li><li>AWS S3</li></ul><h3>Extend Pipeline</h3><p>Add custom processing steps by editing <code>bb.edn</code> and creating new namespaces in <code>src/robotic&#95;farm/</code>.</p><h2>🙏 Need Help?</h2><p><strong>Documentation:</strong></p><ul><li><code>README.md</code> - Complete documentation</li><li><code>PIPELINE-OVERVIEW.md</code> - Architecture details</li><li>This file - Quick start guide</li></ul><p><strong>Common Issues:</strong></p><p><strong>"bb: command not found"</strong></p><ul><li>Run <code>nix develop</code> first</li></ul><p><strong>"No markdown files found"</strong></p><ul><li>Place <code>.md</code> files in <code>docs/</code> directory</li></ul><p><strong>"Port 5173 already in use"</strong></p><ul><li>Kill existing dev server or use different port</li></ul><p><strong>Validation errors</strong></p><ul><li>Check markdown follows template structure</li><li>Ensure frontmatter is valid YAML</li></ul><h2>🤖 Example Session</h2><pre><code class="bash"># Terminal session example
$ cd &#126;/kae3g/12025-10-04

$ nix develop
🌙 Sacred Robotic Farm Pipeline
   Development Shell Activated

$ ./quick-start.sh
🌙 Sacred Robotic Farm Pipeline Quick Start
✅ Babashka found: 1.4.192
🔍 Checking toolchain...
🌾 Pipeline ready!

$ cp ../my-doc.md docs/

$ bb build:pipeline
🌙 Sacred Text Wrapper: 57-char hard wrap
🌱 Wrapping markdown: my-doc.md
✨ Wrapped to: build/wrapped.md
🤖 Robotic Farm Parser: Awakening...
📂 Found 1 documents
✨ Parsed 1 documents
🔍 Validating robotic farm documents
✨ Valid: 1 / 1
🤖 All documents valid
🌙 Robotic Farm Generator
📝 Writing Svelte components
✨ Generated: my-doc.svelte
✨ Generated: index.svelte
🤖 Generated 2 components

$ bb serve:dev
🤖 Starting robotic farm consciousness server...
🌱 Development server starting...

  VITE v5.0.0  ready in 142 ms

  ➜  Local:   http://localhost:5173/
  ➜  Network: use --host to expose
</code></pre><h2>✨ Success Indicators</h2><p>You'll know it's working when:</p><ol><li>✅ <code>bb doctor</code> shows all tools available</li><li>✅ <code>bb build:pipeline</code> completes without errors</li><li>✅ Files appear in <code>build/</code> directory</li><li>✅ Svelte components in <code>web-app/src/routes/</code></li><li>✅ Dev server opens in browser</li><li>✅ You see your formatted document with badges<h2></h2></li></ol><p>*"Blessed be the automation that serves earth intelligence through contemplative technology consciousness."*</p><p><strong>Start</strong> <strong>your</strong> <strong>consciousness</strong> <strong>journey</strong> <strong>today</strong>. 🤖🌙🌾</p>`}
  </main>

  <footer class="doc-footer">
    <nav class="contemplative-nav">
      <a href="/">🌙 Home</a>
      <a href="/teachings">🤖 All Docs</a>
    </nav>
  </footer>
</article>

<style>
  .robotic-farm-doc {
    max-width: 57ch;
    margin: 2rem auto;
    padding: 2rem;
    font-family: 'Times New Roman';
    line-height: 1.6;
  }
  .doc-header {
    border-bottom: 2px solid #2d3748;
    padding-bottom: 1rem;
    margin-bottom: 2rem;
  }
  .doc-number {
    font-family: 'Courier New';
    opacity: 0.7;
    font-size: 0.9rem;
  }
  .doc-title {
    font-size: 2rem;
    margin: 0.5rem 0;
    color: #1a202c;
  }
  .consciousness-badges {
    display: flex;
    flex-wrap: wrap;
    gap: 0.5rem;
    margin-top: 1rem;
  }
  .badge {
    background: #4a5568;
    color: white;
    padding: 0.25rem 0.75rem;
    border-radius: 0.25rem;
    font-size: 0.85rem;
  }
  .sacred-quotes {
    margin: 2rem 0;
  }
  blockquote.sacred {
    border-left: 4px solid gold;
    padding-left: 1rem;
    font-style: italic;
    margin: 1rem 0;
    color: #2d3748;
  }
  .doc-content {
    margin: 2rem 0;
  }
  .contemplative-nav {
    display: flex;
    gap: 1rem;
    margin-top: 2rem;
    padding-top: 1rem;
    border-top: 1px solid #e2e8f0;
  }
  .contemplative-nav a {
    text-decoration: none;
    color: #4a5568;
    padding: 0.5rem 1rem;
  }
  .contemplative-nav a:hover {
    background: #edf2f7;
    border-radius: 0.25rem;
  }
</style>