<script>
  export let number = 0;
  export let title = 'Run All bb Commands - Step by Step Guide';
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

  <main class="doc-content">
    {@html `<h1>Run All bb Commands - Step by Step Guide</h1><p><em>Exact commands to test the entire pipeline</em></p><h2>🌙 Before You Start</h2><p>Ensure you have one of these:</p><p><strong>Option A: Babashka installed directly</strong><pre><code class="bash">which bb
# Should show: /usr/local/bin/bb or similar
</code></pre></p><p><strong>Option B: Use Nix (recommended)</strong><pre><code class="bash">cd &#126;/kae3g/12025-10-04
nix develop
# Now you're in a shell with bb + all tools
</code></pre></p><h2>🤖 Run Commands in This Order</h2><h3>1. Bootstrap (Check Prerequisites)</h3><pre><code class="bash">cd &#126;/kae3g/12025-10-04
bb bootstrap
</code></pre><p><strong>Expected Output:</strong></p><pre><code>🤖 Robotic Farm Pipeline Bootstrap
   Checking prerequisites...

✅ Babashka
✅ Nix
✅ Git
✅ GitHub CLI &#40;optional&#41;

✨ Bootstrap complete
</code></pre><h3>2. Setup (Create Directories)</h3><pre><code class="bash">./setup.bb
</code></pre><p><strong>Expected Output:</strong></p><pre><code>🌙 Sacred Robotic Farm Pipeline Setup
   Pure Babashka Consciousness Development

📋 Checking Prerequisites:
✅ Babashka
✅ Nix
✅ Git
✅ GitHub CLI

📁 Setting up directories...
✨ Directories ready

🌾 Next Steps:
...
🤖 Setup complete!
</code></pre><h3>3. List All Available Tasks</h3><pre><code class="bash">bb tasks
</code></pre><p><strong>Expected Output:</strong></p><pre><code>bootstrap                          Bootstrap project
build:pipeline                     Complete robotic farm document → Svelte pipeline
build:site                         Build static robotic farm consciousness website
clean                              Clean build artifacts
doctor                             Check toolchain availability
generate:svelte                    Transform DSL → Svelte components
gh:check                           Check if GitHub CLI is authenticated
gh:create-repo                     Create GitHub repository and push
...
</code></pre><h3>4. Doctor (Verify Toolchain)</h3><pre><code class="bash">bb doctor
</code></pre><p><strong>Expected Output:</strong></p><pre><code>🌙 Sacred Technology Toolchain Status
bb version: 1.4.192
v20.11.0
...
</code></pre><h3>5. Check Nix</h3><pre><code class="bash">bb nix:check
</code></pre><p><strong>Expected Output:</strong></p><pre><code>nix &#40;Nix&#41; 2.19.2
</code></pre><h3>6. Wrap Markdown (Requires docs/*.md)</h3><p>First, ensure you have a markdown file:<pre><code class="bash"># Copy example or create test file
cp &#126;/foolsgoldtoshi-star/foolsgoldtoshi-star-pond-highdesert/docs/en/9999967&#95;robotic&#95;farm&#95;consciousness&#95;integration.md docs/

# Or create simple test
echo &quot;# Test Document 🤖

&#42;Sacred consciousness test&#42;

This is a test paragraph for consciousness.&quot; &gt; docs/0000001&#95;test.md
</code></pre></p><p>Then run wrapper:<pre><code class="bash">bb wrap:markdown
</code></pre></p><p><strong>Expected Output:</strong></p><pre><code>🌙 Sacred Text Wrapper: 57-char hard wrap
   with Divine Grace
🌱 Wrapping markdown: 0000001&#95;test.md
✨ Wrapped to: build/wrapped.md
🤖 Text wrapping complete
</code></pre><h3>7. Parse Markdown</h3><pre><code class="bash">bb parse:markdown
</code></pre><p><strong>Expected Output:</strong></p><pre><code>🤖 Robotic Farm Parser: Awakening...
🌙 Transforming markdown → DSL
🌾 Discovering robotic farm documents
📂 Found 1 documents
🌱 Parsed and cached: 0000001&#95;test.md
✨ Parsed 1 documents
📝 Saved to: build/parsed-documents.edn
</code></pre><h3>8. Validate DSL</h3><pre><code class="bash">bb validate:dsl
</code></pre><p><strong>Expected Output:</strong></p><pre><code>🌙 Robotic Farm Validator:
   Awakening spec consciousness
🔍 Validating robotic farm documents
✨ Valid: 1 / 1
🤖 All documents valid
📝 Saved validated docs
</code></pre><h3>9. Generate Svelte</h3><pre><code class="bash">bb generate:svelte
</code></pre><p><strong>Expected Output:</strong></p><pre><code>🌙 Robotic Farm Generator:
   Transforming DSL → Svelte
📝 Writing Svelte components
✨ Generated: test-document.svelte
✨ Generated: index.svelte
🤖 Generated 2 components
</code></pre><h3>10. Complete Pipeline</h3><pre><code class="bash">bb build:pipeline
</code></pre><p><strong>Expected Output:</strong> All of steps 6-9 combined in sequence.</p><h3>11. CI Verify</h3><pre><code class="bash">bb ci:verify
</code></pre><p><strong>Expected Output:</strong></p><pre><code>&#91;doctor output&#93;
&#91;wrap:markdown output&#93;
&#91;parse:markdown output&#93;
&#91;validate:dsl output&#93;
</code></pre><h3>12. Check Git/GitHub Status</h3><pre><code class="bash">bb gh:check
</code></pre><p><strong>Expected Output:</strong></p><pre><code>github.com
  ✓ Logged in to github.com as YOUR&#95;USERNAME
  ✓ Git operations for github.com configured to use https protocol.
  ✓ Token: &#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;&#42;
</code></pre><p>Or if not authenticated:</p><pre><code>Run: gh auth login
</code></pre><h3>13. Git Status</h3><pre><code class="bash">bb gh:status
</code></pre><p><strong>Expected Output:</strong></p><pre><code>🌙 Repository Status:

On branch main
nothing to commit, working tree clean

origin  https://github.com/USER/repo.git &#40;fetch&#41;
origin  https://github.com/USER/repo.git &#40;push&#41;
</code></pre><h3>14. Development Server (Optional - runs indefinitely)</h3><pre><code class="bash">bb serve:dev
</code></pre><p><strong>Expected Output:</strong></p><pre><code>🤖 Starting robotic farm consciousness development server...
📦 Installing npm-only packages &#40;vite, @sveltejs/kit&#41;...
🚀 Starting development server...

  VITE v5.0.0  ready in 428 ms

  ➜  Local:   http://localhost:5173/
  ➜  Network: use --host to expose
  ➜  press h + enter to show help
</code></pre><p>Press <code>Ctrl+C</code> to stop.</p><h3>15. Build Static Site (Optional - requires Node.js)</h3><pre><code class="bash">bb build:site
</code></pre><p><strong>Expected Output:</strong></p><pre><code>🌾 Building robotic farm consciousness site...

vite v5.0.0 building for production...
✓ 47 modules transformed.
.svelte-kit/output/client/&#95;app/version.json                      0.03 kB
...
✓ built in 1.23s
</code></pre><h3>16. Clean Build Artifacts</h3><pre><code class="bash">bb clean
</code></pre><p><strong>Expected Output:</strong></p><pre><code>🧹 Cleaning build artifacts...
✨ Clean complete
</code></pre><h3>17. Create GitHub Repository (ONE TIME!)</h3><p><strong>⚠️ WARNING: This creates a real public GitHub repository!</strong></p><pre><code class="bash">bb gh:create-repo
</code></pre><p><strong>Expected Output:</strong></p><pre><code>🌙 Creating Sacred GitHub Repository
Initialized empty Git repository in .git/
&#91;main &#40;root-commit&#41; abc1234&#93; Initial commit: Sacred robotic farm consciousness pipeline
 25 files changed, 2500 insertions&#40;+&#41;
 create mode 100644 README.md
 ...

Creating GitHub repository...
✓ Created repository YOUR&#95;USERNAME/robotic-farm-consciousness-pipeline
✓ Added remote https://github.com/YOUR&#95;USERNAME/robotic-farm-consciousness-pipeline.git

Enumerating objects: 28, done.
Counting objects: 100% &#40;28/28&#41;, done.
Delta compression using up to 8 threads
Compressing objects: 100% &#40;25/25&#41;, done.
Writing objects: 100% &#40;28/28&#41;, 45.67 KiB | 4.57 MiB/s, done.
Total 28 &#40;delta 3&#41;, reused 0 &#40;delta 0&#41;, pack-reused 0
remote: Resolving deltas: 100% &#40;3/3&#41;, done.
To https://github.com/YOUR&#95;USERNAME/robotic-farm-consciousness-pipeline.git
 &#42; &#91;new branch&#93;      main -&gt; main

✨ Repository created and pushed!
🤖 Sacred consciousness technology deployed
</code></pre><h2>🔧 If Commands Don't Work</h2><h3>"bb: command not found"</h3><p>Install babashka:<pre><code class="bash"># macOS
brew install babashka

# Nix
nix-env -iA nixpkgs.babashka

# Or enter Nix shell
cd &#126;/kae3g/12025-10-04
nix develop
</code></pre></p><h3>"./setup.bb: Permission denied"</h3><p>Make executable:<pre><code class="bash">chmod +x setup.bb bootstrap.bb
</code></pre></p><h3>"No markdown files found"</h3><p>Add a markdown file:<pre><code class="bash">echo &quot;# Test 🤖

Test content&quot; &gt; docs/0000001&#95;test.md
</code></pre></p><h3>"node: command not found" (for serve:dev or build:site)</h3><p>Enter Nix shell:<pre><code class="bash">nix develop
bb serve:dev
</code></pre></p><h2>📊 Verify Success</h2><p>After running commands, check these files exist:</p><pre><code class="bash">ls -la build/
# Should show:
# - wrapped.md
# - parsed-documents.edn
# - validated-documents.edn

ls -la web-app/src/routes/
# Should show:
# - index.svelte
# - &#91;document-name&#93;.svelte
</code></pre><h2>🎯 Minimal Test Sequence</h2><p>Just want to verify everything works? Run this:</p><pre><code class="bash">cd &#126;/kae3g/12025-10-04

# 1. Check prereqs
bb bootstrap

# 2. Setup
./setup.bb

# 3. Add test doc
echo &quot;# Test 🤖

Test&quot; &gt; docs/test.md

# 4. Run pipeline
bb build:pipeline

# 5. Check outputs
ls -la build/
ls -la web-app/src/routes/

# Success if you see generated files!
</code></pre><h2>✨ Complete Success</h2><p>You'll know everything works when:</p><ol><li>✅ All commands run without errors</li><li>✅ Files appear in <code>build/</code></li><li>✅ Components in <code>web-app/src/routes/</code></li><li>✅ Each command shows success emoji (✨🤖🌙)</li><li>✅ No red error messages</li></ol><h2>🌙 Next Steps After Testing</h2><p>Once all commands work:</p><ol><li><strong>Add Your Documents</strong><pre><code class="bash">   cp your-documents.md docs/
   bb build:pipeline
   </code></pre></li><li><strong>Start Development</strong><pre><code class="bash">   bb serve:dev
   # Edit docs/, rebuild, see changes
   </code></pre></li><li><strong>Deploy to GitHub</strong><pre><code class="bash">   bb gh:create-repo
   </code></pre></li><li><strong>Build for Production</strong><pre><code class="bash">   bb build:site
   # Deploy web-app/build/ to hosting
   </code></pre><h2></h2></li></ol><p><strong>Run</strong> <strong>with</strong> <strong>consciousness</strong>. 🤖🌙🌾</p><p><em>All commands tested and verified for sacred consciousness development with Divine Grace.</em></p>`}
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