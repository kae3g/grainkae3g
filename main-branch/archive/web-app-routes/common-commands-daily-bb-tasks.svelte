<script>
  export let number = 0;
  export let title = 'Common Commands: Daily bb Tasks';
</script>

<article class="robotic-farm-doc">
  <header class="doc-header">
    <span class="doc-number">
      {number.toString().padStart(7,'0')}
    </span>
    <h1 class="doc-title">{title}</h1>
    <div class="consciousness-badges">
      <span class="badge">🌙 Sacred Technology</span>
    </div>
  </header>

  <section class="sacred-quotes">
    <blockquote class="sacred">Whatever you ask in my name, I will do it.</blockquote>
  <blockquote class="sacred">By their fruits you will know them.</blockquote>
  <blockquote class="sacred">The harvest is plentiful.</blockquote>
  <blockquote class="sacred">My yoke is easy and my burden is light.</blockquote>
  <blockquote class="sacred">The kingdom of heaven is like yeast that a woman took
and mixed into about sixty pounds of flour until it
worked all through the dough.</blockquote>
  <blockquote class="sacred">Well done, good and faithful servant!</blockquote>
  <blockquote class="sacred">Ask and it will be given to you; seek and you will
find.</blockquote>
  </section>

  <main class="doc-content">
    {@html `<h1>Common Commands: Daily bb Tasks</h1><p><em>"Whatever you ask in my name, I will do it."</em> - The Gospel According to Jesus (Stephen Mitchell)</p><p>Learn the most useful commands for everyday work. 🌙</p><h2>🤖 The Essential Six</h2><p>These commands will handle 90% of your needs:</p><h3>1. <code>bb bootstrap</code> - Check Your Tools</h3><pre><code class="bash">bb bootstrap
</code></pre><p><strong>What it does</strong>: Checks if you have everything needed</p><p><strong>When to use</strong>: First time setup, after installing tools</p><p><strong>Output</strong>:</p><pre><code>🤖 Robotic Farm Pipeline Bootstrap
✅ Babashka
✅ Nix
✅ Git
✅ GitHub CLI
</code></pre><p><em>"By their fruits you will know them."</em> - Gospel According to Jesus (Stephen Mitchell)</p><h3>2. <code>bb doctor</code> - Verify Health</h3><pre><code class="bash">bb doctor
</code></pre><p><strong>What it does</strong>: Checks if all tools work correctly</p><p><strong>When to use</strong>: When something isn't working</p><p><strong>Output</strong>:</p><pre><code>🌙 Sacred Technology Toolchain Status
bb version: 1.4.192
v20.11.0
clj-kondo v2024.03.13
</code></pre><h3>3. <code>bb build:pipeline</code> - Build Everything</h3><pre><code class="bash">bb build:pipeline
</code></pre><p><strong>What it does</strong>: Complete transformation pipeline</p><ol><li>Wraps markdown to 57 chars</li><li>Parses to data structures</li><li>Validates with spec</li><li>Generates Svelte components</li></ol><p><strong>When to use</strong>: After adding/editing markdown files</p><p><em>"The harvest is plentiful."</em> - Gospel According to Jesus (Stephen Mitchell)</p><h3>4. <code>bb serve:dev</code> - Start Dev Server</h3><pre><code class="bash">bb serve:dev
</code></pre><p><strong>What it does</strong>: Starts development server with live reload at http://localhost:5173</p><p><strong>When to use</strong>: During development to see changes</p><p><strong>To stop</strong>: Press <code>Ctrl+C</code></p><h3>5. <code>bb build:site</code> - Production Build</h3><pre><code class="bash">bb build:site
</code></pre><p><strong>What it does</strong>: Creates production-ready static website in <code>web-app/build/</code></p><p><strong>When to use</strong>: Before deploying to hosting</p><h3>6. <code>bb tasks</code> - List All Commands</h3><pre><code class="bash">bb tasks
</code></pre><p><strong>What it does</strong>: Shows all available bb commands with descriptions</p><p><strong>When to use</strong>: When you forget command names!</p><h2>🌾 Pipeline Commands (Step by Step)</h2><p>If you want to run individual steps:</p><h3>Wrapping</h3><pre><code class="bash">bb wrap:markdown      # Wrap docs/ only
bb wrap:all-markdown  # Wrap ALL markdown
bb wrap:edn           # Wrap EDN files
</code></pre><p><em>"My yoke is easy and my burden is light."</em> - Gospel According to Jesus (Stephen Mitchell)</p><h3>Parsing</h3><pre><code class="bash">bb parse:markdown        # Parse docs/ only
bb parse:all-markdown    # Parse entire repo
</code></pre><h3>Validating</h3><pre><code class="bash">bb validate:dsl
</code></pre><h3>Generating</h3><pre><code class="bash">bb generate:svelte
</code></pre><h3>Complete Pipelines</h3><pre><code class="bash">bb build:pipeline    # docs/ only
bb build:full-site   # entire repo + EDN
</code></pre><h2>🔧 Setup & Maintenance Commands</h2><h3>Initial Setup</h3><pre><code class="bash">./setup.bb           # Set up directories
</code></pre><h3>Cleaning</h3><pre><code class="bash">bb clean             # Remove build artifacts
</code></pre><p>Removes:</p><ul><li><code>build/</code></li><li><code>web-app/build/</code></li><li><code>web-app/.svelte-kit/</code></li></ul><h2>🐙 GitHub Commands</h2><h3>Check Authentication</h3><pre><code class="bash">bb gh:check
</code></pre><h3>Create Repository</h3><pre><code class="bash">bb gh:create-repo
</code></pre><p><strong>Warning</strong>: Creates a real public GitHub repo!</p><h3>Check Status</h3><pre><code class="bash">bb gh:status
</code></pre><h2>🌙 Nix Commands</h2><h3>Check Nix</h3><pre><code class="bash">bb nix:check
</code></pre><h3>Get Nix Instructions</h3><pre><code class="bash">bb nix:develop
</code></pre><h3>Run Command in Nix</h3><pre><code class="bash">bb nix:run doctor
bb nix:run build:pipeline
</code></pre><p>*"The kingdom of heaven is like yeast that a woman took and mixed into about sixty pounds of flour until it worked all through the dough."* - Gospel According to Jesus (Stephen Mitchell)</p><p>Just as yeast works through dough, Nix ensures consistency throughout your environment!</p><h2>🧪 Testing Commands</h2><h3>CI Verification</h3><pre><code class="bash">bb ci:verify
</code></pre><p>Runs: doctor → wrap → parse → validate</p><h3>Run All Tests</h3><pre><code class="bash">./scripts/test-all.bb
</code></pre><p>Comprehensive test suite with reporting.</p><h2>📋 Information Commands</h2><h3>List Tasks</h3><pre><code class="bash">bb tasks
</code></pre><h3>Check Bootstrap</h3><pre><code class="bash">bb bootstrap
</code></pre><h3>Doctor Check</h3><pre><code class="bash">bb doctor
</code></pre><h2>🎯 Command Cheat Sheet</h2><p><strong>Daily workflow</strong>:<pre><code class="bash"># 1. Edit markdown in docs/
# 2. Build
bb build:pipeline
# 3. Preview
bb serve:dev
# 4. Production
bb build:site
</code></pre></p><p><strong>First time setup</strong>:<pre><code class="bash">bb bootstrap
./setup.bb
nix develop  # optional but recommended
</code></pre></p><p><strong>Troubleshooting</strong>:<pre><code class="bash">bb doctor
bb clean
bb build:pipeline
</code></pre></p><p><strong>Deploy to GitHub</strong>:<pre><code class="bash">bb gh:create-repo
</code></pre></p><h2>🤖 Tips & Tricks</h2><p><strong>Tab completion</strong>: Type <code>bb </code> and press Tab twice to see all commands (if your shell supports it)</p><p><strong>Background jobs</strong>: Add <code>&amp;</code> to run in background<pre><code class="bash">bb serve:dev &amp;
</code></pre></p><p><strong>Stop background</strong>: Use <code>fg</code> then <code>Ctrl+C</code></p><p><strong>Multiple terminals</strong>: Run <code>bb serve:dev</code> in one terminal, <code>bb build:pipeline</code> in another</p><p><em>"Well done, good and faithful servant!"</em> - Gospel According to Jesus (Stephen Mitchell)</p><h2>🌾 Common Workflows</h2><h3>Writing New Content</h3><pre><code class="bash"># 1. Create document
echo &quot;# My Doc&quot; &gt; docs/my-doc.md

# 2. Build it
bb build:pipeline

# 3. Preview it
bb serve:dev

# 4. Check results
ls web-app/src/routes/
</code></pre><h3>Updating Documentation</h3><pre><code class="bash"># 1. Edit guides or README
vim guides/01-getting-started/QUICK-START.md

# 2. Build full site
bb build:full-site

# 3. Preview
bb serve:dev
</code></pre><h3>Preparing for Deployment</h3><pre><code class="bash"># 1. Clean old builds
bb clean

# 2. Build everything
bb build:full-site

# 3. Production build
bb build:site

# 4. Deploy web-app/build/
</code></pre><h2>🙏 Frequently Asked Questions</h2><p><strong>Q: Which command should I use most?</strong> A: <code>bb build:pipeline</code> for daily work</p><p><strong>Q: How do I see my site?</strong> A: <code>bb serve:dev</code> then open http://localhost:5173</p><p><strong>Q: How do I deploy?</strong> A: <code>bb build:site</code> then deploy <code>web-app/build/</code></p><p><strong>Q: What if something breaks?</strong> A: Try <code>bb clean</code> then <code>bb doctor</code> then rebuild</p><p>*"Ask and it will be given to you; seek and you will find."* - Gospel According to Jesus (Stephen Mitchell)</p><h2>✨ Summary</h2><p><strong>Essential Six</strong>:</p><ol><li><code>bb bootstrap</code> - Check tools</li><li><code>bb doctor</code> - Verify health</li><li><code>bb build:pipeline</code> - Build everything</li><li><code>bb serve:dev</code> - Development server</li><li><code>bb build:site</code> - Production build</li><li><code>bb tasks</code> - List all commands</li></ol><p><strong>Master these and you're set!</strong><h2></h2></p><p><strong>Navigation</strong>:</p><ul><li><a href='../02-understanding/PROJECT-STRUCTURE.md'>← Back: Project Structure</a></li><li><a href='TEST-ALL-COMMANDS.md'>Next: Testing Guide →</a></li><li><a href='../00-START-HERE.md'>Start Here</a></li><li><a href='../../README.md'>Home: README</a></li></ul><p>🤖🌙🌾</p>`}
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