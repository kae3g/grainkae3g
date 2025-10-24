<script>
  export let number = 0;
  export let title = 'Quick Start: First Steps';
</script>

<article class="robotic-farm-doc">
  <header class="doc-header">
    <span class="doc-number">
      {number.toString().padStart(7,'0')}
    </span>
    <h1 class="doc-title">{title}</h1>
    <div class="consciousness-badges">
      
    </div>
  </header>

  <section class="sacred-quotes">
    <blockquote class="sacred">bb: command not found</blockquote>
  <blockquote class="sacred">Permission denied</blockquote>
  <blockquote class="sacred">No markdown files found</blockquote>
  </section>

  <main class="doc-content">
    {@html `<h1>Quick Start: First Steps</h1><p>Welcome! Let's get you running in 5 minutes.</p><h2>What You'll Learn</h2><p>By the end of this guide, you'll:</p><ul><li>✅ Have all tools installed</li><li>✅ Run your first bb command</li><li>✅ See the pipeline work</li><li>✅ Create something beautiful</li><li>✅ Understand the basic workflow</li></ul><h2>Step 1: Check if You Have Babashka</h2><p>Open your terminal and type:</p><pre><code class="bash">bb --version
</code></pre><p><strong>If it works</strong>: You'll see <code>babashka v1.x.x</code> ✅ <strong>If it doesn't</strong>: Install it (see below)</p><h3>Installing Babashka</h3><p><strong>On Mac</strong>:<pre><code class="bash">brew install babashka
</code></pre></p><p><strong>On Linux/Nix</strong>:<pre><code class="bash">nix-env -iA nixpkgs.babashka
</code></pre></p><h2>Step 2: Enter the Project</h2><pre><code class="bash">cd &#126;/kae3g/12025-10-04
</code></pre><h2>Step 3: Check Prerequisites</h2><pre><code class="bash">bb bootstrap
</code></pre><p>You should see:</p><pre><code>Documentation Pipeline Toolchain Status
✅ Babashka
✅ Nix
✅ Git
</code></pre><h2>Step 4: Set Up Directories</h2><pre><code class="bash">./setup.bb
</code></pre><p>This creates the folders you need for building your documentation. The setup script prepares your project structure automatically.</p><h2>Step 5: See What You Can Do</h2><pre><code class="bash">bb tasks
</code></pre><p>This shows all available commands - your complete toolkit for building documentation!</p><h2>Step 6: Run Your First Build</h2><p>Create a simple test document:</p><pre><code class="bash">echo &quot;# My First Document

This is my first documentation pipeline document.

## What I Learned

I learned that:
- Babashka is fast
- bb commands are simple
- The pipeline automates everything
- 57-character lines are readable

Great for technical documentation!&quot; &gt; docs/test.md
</code></pre><p>Now build it:</p><pre><code class="bash">bb build:pipeline
</code></pre><p>Watch as your document is automatically wrapped, parsed, validated, and transformed into a website!</p><h2>Step 7: See Your Results</h2><pre><code class="bash">ls -la build/
ls -la web-app/src/routes/
</code></pre><p>You created:</p><ul><li>Wrapped markdown (57 characters per line)</li><li>Parsed data structures</li><li>Svelte components</li><li>A website ready to deploy!</li></ul><h2>Common Issues</h2><p><strong>"bb: command not found"</strong> → Install babashka (see Step 1)</p><p><strong>"Permission denied"</strong> → Run: <code>chmod +x setup.bb bootstrap.bb</code></p><p><strong>"No markdown files found"</strong> → Make sure you created docs/test.md</p><h2>Congratulations!</h2><p>You've completed your first documentation build! You now understand the basic workflow and can start creating your own documentation sites.</p><h2>What's Next?</h2><ol><li><strong>Learn concepts</strong>: <a href='CONCEPTS.md'>Basic Concepts →</a></li><li><strong>Understand system</strong>: <a href='../02-understanding/HOW-IT-WORKS.md'>How It Works →</a></li><li><strong>Try more commands</strong>: <a href='../03-using/COMMON-COMMANDS.md'>Common Commands →</a><h2></h2></li></ol><p><strong>Navigation</strong>:</p><ul><li><a href='../00-START-HERE.md'>← Back: Start Here</a></li><li><a href='CONCEPTS.md'>Next: Concepts →</a></li><li><a href='../../README.md'>Home: README</a></li></ul>`}
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