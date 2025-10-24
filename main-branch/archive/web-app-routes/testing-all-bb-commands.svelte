<script>
  export let number = 0;
  export let title = 'Testing All bb Commands';
</script>

<article class="robotic-farm-doc">
  <header class="doc-header">
    <span class="doc-number">
      {number.toString().padStart(7,'0')}
    </span>
    <h1 class="doc-title">{title}</h1>
    <div class="consciousness-badges">
      <span class="badge">🤖 Robotic Consciousness</span>
    </div>
  </header>

  <main class="doc-content">
    {@html `<h1>Testing All bb Commands</h1><p>Comprehensive test suite for all pipeline commands</p><h2>Prerequisites Test</h2><p>Before running bb commands, ensure babashka is installed:</p><pre><code class="bash"># Check if bb is available
which bb
bb --version

# If not found, install:
# macOS: brew install babashka
# Nix: nix-env -iA nixpkgs.babashka
# Or enter nix shell: nix develop
</code></pre><h2>Test Commands in Order</h2><h3>1. Bootstrap & Setup Commands</h3><pre><code class="bash"># Test bootstrap &#40;checks prerequisites&#41;
bb bootstrap

# Expected output:
# Documentation Pipeline Toolchain Status
# Checking prerequisites...
# ✅ Babashka
# ✅ Nix
# ✅ Git
# ⚠️  GitHub CLI &#40;optional&#41;
# Bootstrap complete
</code></pre><pre><code class="bash"># Test setup script
./setup.bb

# Expected output:
# Documentation Pipeline Setup
# Checking Prerequisites:
# ✅ Babashka
# ✅ Nix
# ✅ Git
# ✅ GitHub CLI
# Setting up directories...
# Directories ready
</code></pre><pre><code class="bash"># Alternative: Run setup via bb
bb -f setup.bb
</code></pre><h3>2. Health Check Commands</h3><pre><code class="bash"># Test doctor &#40;verify toolchain&#41;
bb doctor

# Expected output:
# Documentation Pipeline Toolchain Status
# bb version: 1.x.x
# node --version
# v20.x.x
# clj-kondo --version
# zprint --version
# npx vite --version
</code></pre><pre><code class="bash"># List all available tasks
bb tasks

# Expected output: List of all tasks with descriptions
</code></pre><h3>3. Nix Integration Commands</h3><pre><code class="bash"># Check Nix availability
bb nix:check

# Expected output:
# nix &#40;Nix&#41; 2.x.x
# OR: Nix not found - install from nixos.org
</code></pre><pre><code class="bash"># Show Nix develop instructions
bb nix:develop

# Expected output:
# To enter Nix shell, run in your terminal:
#    nix develop
# 
# Then inside Nix shell:
#    bb doctor
#    bb build:pipeline
</code></pre><pre><code class="bash"># Run task in Nix context &#40;if Nix available&#41;
bb nix:run doctor

# Equivalent to: nix develop --command bb doctor
</code></pre><h3>4. Pipeline Core Commands</h3><pre><code class="bash"># Test text wrapper &#40;requires docs/&#42;.md files&#41;
bb wrap:markdown

# Expected output:
# Text Wrapper: 57-char hard wrap
# Wrapping markdown: &#91;filename&#93;
# Wrapped to: build/wrapped.md
# Text wrapping complete
</code></pre><pre><code class="bash"># Test markdown parser
bb parse:markdown

# Expected output:
# Markdown Parser: Starting...
# Transforming markdown → DSL
# Discovering documents
# Found X documents
# Parsed X documents
# Saved to: build/parsed-documents.edn
</code></pre><pre><code class="bash"># Test validator
bb validate:dsl

# Expected output:
# Validator: Starting spec validation
# Validating documents
# Valid: X / X
# All documents valid
# Saved validated docs
</code></pre><pre><code class="bash"># Test Svelte generator
bb generate:svelte

# Expected output:
# Generator: Transforming DSL → Svelte
# Writing Svelte components
# Generated: &#91;filename&#93;.svelte
# Generated: index.svelte
# Generated X components
</code></pre><pre><code class="bash"># Test complete pipeline
bb build:pipeline

# Runs: wrap:markdown → parse:markdown → validate:dsl → generate:svelte
# Expected: All above outputs in sequence
</code></pre><h3>5. Development Server Commands</h3><pre><code class="bash"># Test development server &#40;requires Node.js&#41;
# WARNING: This runs indefinitely until Ctrl+C
bb serve:dev

# Expected output:
# Starting development server...
# Installing packages &#40;vite, @sveltejs/kit&#41;...
# VITE v5.0.0  ready in XXX ms
# ➜  Local:   http://localhost:5173/
# ➜  Network: use --host to expose

# Press Ctrl+C to stop
</code></pre><h3>6. Build Commands</h3><pre><code class="bash"># Test static site build &#40;requires Node.js&#41;
bb build:site

# Expected output:
# Building static site...
# vite v5.0.0 building for production...
# ✓ built in XXXXms
</code></pre><pre><code class="bash"># Test clean
bb clean

# Expected output:
# Cleaning build artifacts...
# Clean complete
</code></pre><h3>7. Git/GitHub Commands</h3><pre><code class="bash"># Check GitHub CLI status
bb gh:check

# Expected output:
# gh auth status
# github.com
#   ✓ Logged in to github.com as USERNAME
# OR: Run: gh auth login
</code></pre><pre><code class="bash"># Check git status
bb gh:status

# Expected output:
# Repository Status:
# On branch main
# &#91;git status output&#93;
# &#91;git remote -v output&#93;
</code></pre><pre><code class="bash"># Create GitHub repository &#40;ONE-TIME ACTION!&#41;
# WARNING: This creates a real GitHub repo
bb gh:create-repo

# Expected output:
# Creating GitHub Repository
# Initialized git repository
# &#91;main &#40;root-commit&#41; XXXXXX&#93; Initial commit
# Creating GitHub repository...
# ✓ Created repository USER/REPONAME
# ✓ Added remote origin
# Enumerating objects: XX, done.
# Repository created and pushed!
</code></pre><h3>8. CI/CD Commands</h3><pre><code class="bash"># Test CI verification
bb ci:verify

# Runs: doctor → wrap:markdown → parse:markdown → validate:dsl
# Expected: All checks pass
</code></pre><pre><code class="bash"># Test formatting &#40;requires zprint&#41;
bb fmt:zprint

# Expected output:
# Formatting with zprint...
# &#91;formatted files&#93;
</code></pre><pre><code class="bash"># Test linting &#40;requires clj-kondo&#41;
bb lint:kondo

# Expected output:
# linting took XXms
# &#91;linting results&#93;
</code></pre><h2>🧪 Complete Test Sequence</h2><p>Run all commands in order to verify full pipeline:</p><pre><code class="bash">#!/bin/bash
# Complete test sequence

echo &quot;=== 1. Bootstrap ===&quot;
bb bootstrap

echo &quot;\n=== 2. Setup ===&quot;
./setup.bb

echo &quot;\n=== 3. Doctor ===&quot;
bb doctor

echo &quot;\n=== 4. List Tasks ===&quot;
bb tasks | head -20

echo &quot;\n=== 5. Nix Check ===&quot;
bb nix:check

echo &quot;\n=== 6. Build Pipeline ===&quot;
# Note: Requires markdown files in docs/
bb build:pipeline

echo &quot;\n=== 7. CI Verify ===&quot;
bb ci:verify

echo &quot;\n=== 8. Git Status ===&quot;
bb gh:status

echo &quot;\n=== 9. List Generated Files ===&quot;
ls -la build/
ls -la web-app/src/routes/

echo &quot;\n✨ All tests complete!&quot;
</code></pre><h2>🔧 Troubleshooting</h2><h3>"bb: command not found"</h3><p>Solution:<pre><code class="bash"># Install babashka
brew install babashka
# OR
nix-env -iA nixpkgs.babashka
# OR
nix develop  # Enter Nix shell
</code></pre></p><h3>"No markdown files found"</h3><p>Solution:<pre><code class="bash"># Copy example document
cp &#126;/foolsgoldtoshi-star/foolsgoldtoshi-star-pond-highdesert/docs/en/9999967&#95;robotic&#95;farm&#95;consciousness&#95;integration.md docs/

# Or create a test document
echo &quot;# Test Doc 🤖&quot; &gt; docs/0000001&#95;test.md
</code></pre></p><h3>"Node.js not found"</h3><p>Solution:<pre><code class="bash"># Enter Nix shell &#40;provides Node.js&#41;
nix develop
bb serve:dev

# OR install Node.js separately
brew install node
</code></pre></p><h3>"GitHub CLI not authenticated"</h3><p>Solution:<pre><code class="bash">gh auth login
# Follow the prompts
</code></pre></p><h3>"Task dependencies failed"</h3><p>Solution:<pre><code class="bash"># Run tasks individually to find issue
bb wrap:markdown  # Check this works
bb parse:markdown # Then this
bb validate:dsl   # Then this
bb generate:svelte # Then this
</code></pre></p><h2>📊 Expected File Outputs</h2><p>After successful <code>bb build:pipeline</code>:</p><pre><code>build/
├── wrapped.md                    # 57-char wrapped markdown
├── parsed-documents.edn          # Parsed ClojureScript data
└── validated-documents.edn       # Validated structures

web-app/src/routes/
├── index.svelte                  # Index page component
└── &#91;document-name&#93;.svelte        # Generated document components
</code></pre><p>After successful <code>bb build:site</code>:</p><pre><code>web-app/build/
├── index.html                    # Homepage
├── &#91;document-name&#93;/
│   └── index.html               # Document pages
└── &#95;app/                        # Compiled assets
    ├── immutable/
    │   ├── chunks/
    │   └── assets/
    └── version.json
</code></pre><h2>🌙 Interactive Test REPL</h2><p>You can also test bb interactively:</p><pre><code class="bash"># Start bb REPL
bb

# Try commands:
&#40;require '&#91;babashka.process :refer &#91;shell&#93;&#93;&#41;
&#40;shell &quot;ls&quot; &quot;-la&quot;&#41;

# Or one-liners:
bb -e '&#40;println &quot;Hello from bb!&quot;&#41;'
bb -e '&#40;require &#40;quote &#91;babashka.fs :as fs&#93;&#41;&#41; &#40;fs/exists? &quot;bb.edn&quot;&#41;'
</code></pre><h2>Success Criteria</h2><p>All commands should:</p><ul><li>✅ Execute without errors</li><li>✅ Produce expected output files</li><li>✅ Show progress indicators</li><li>✅ Complete successfully</li><li>✅ Exit with code 0</li></ul><h2>Testing Best Practices</h2><p>Run each command systematically, verifying outputs and understanding the pipeline flow. Take time to examine generated files and build artifacts.</p><p>Test incrementally: if a command fails, fix that issue before proceeding to dependent commands.<h2></h2></p><p><strong>Test thoroughly to ensure quality.</strong></p>`}
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