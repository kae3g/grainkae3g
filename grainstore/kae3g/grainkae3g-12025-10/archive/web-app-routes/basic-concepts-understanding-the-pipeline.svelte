<script>
  export let number = 0;
  export let title = 'Basic Concepts: Understanding the Pipeline';
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

  <main class="doc-content">
    {@html `<h1>Basic Concepts: Understanding the Pipeline</h1><p>Learn the fundamental ideas behind this documentation pipeline project.</p><h2>What Is a Pipeline?</h2><p>Imagine water flowing through pipes:</p><pre><code>Water → Filter → Purifier → Clean Water
</code></pre><p>Our pipeline is similar:</p><pre><code>Markdown → Parser → Validator → Svelte → Website
</code></pre><p><strong>Each step transforms the data</strong>, making it better!</p><p>Just as water flows through connected pipes, your documentation flows through connected processing steps, improving at each stage.</p><h2>📖 What Is Markdown?</h2><p>Markdown is simple text with special symbols:</p><pre><code class="markdown"># This is a heading
&#42;&#42;This is bold&#42;&#42;
&#42;This is italic&#42;
- This is a list item
</code></pre><p>It's like writing notes that computers can understand!</p><p><strong>Why use it?</strong></p><ul><li>Easy to write (just text with simple symbols)</li><li>Easy to read (no HTML clutter)</li><li>Easy to transform (computers understand it)</li><li>No complicated formatting (focus on content)</li><li>Widely supported (GitHub, editors, wikis)</li></ul><h2>What Is Babashka (bb)?</h2><p>Babashka is a fast Clojure interpreter designed for scripting and automation. Think of it as your build automation assistant.</p><p><strong>Key features</strong>:</p><ul><li>Extremely fast (starts in milliseconds)</li><li>Runs Clojure scripts instantly</li><li>Orchestrates complex build tasks</li><li>Native binary (no JVM startup time)</li><li>Perfect for CI/CD pipelines<pre><code class="bash">bb doctor    # Check system health
bb build     # Build documentation
bb serve     # Start development server
</code></pre></li></ul><p>Instead of writing bash scripts, you write Clojure code that runs instantly with Babashka.</p><h2>🔄 What Is ClojureScript?</h2><p>ClojureScript is a programming language that:</p><ul><li>Reads like sentences</li><li>Uses parentheses <code>&#40;like this&#41;</code></li><li>Transforms data</li><li>Is very powerful</li></ul><p>Example:<pre><code class="clojure">&#40;println &quot;Hello, consciousness!&quot;&#41;
</code></pre></p><p><strong>Don't worry!</strong> You don't need to write it - the pipeline generates it for you!</p><h2>What Is Svelte?</h2><p>Svelte is a modern web framework that compiles your components into optimized JavaScript. Unlike other frameworks, Svelte does the work at build time, not in the browser.</p><p><strong>Why Svelte?</strong></p><ul><li>Fast (no virtual DOM overhead)</li><li>Reactive (updates automatically)</li><li>Beautiful (modern component syntax)</li><li>Small (tiny bundle sizes)</li><li>Easy to learn (less boilerplate)</li></ul><p>Your documentation becomes an interactive, fast website with minimal JavaScript.</p><h2>What Is Nix?</h2><p>Nix is a package manager and build system that ensures reproducible builds. Think of it as a way to freeze your development environment.</p><p><strong>Key features</strong>:</p><ul><li>Declarative (specify exact versions)</li><li>Reproducible (same build every time)</li><li>Isolated (no conflicts between projects)</li><li>Cross-platform (Mac and Linux)</li></ul><p><strong>Benefits</strong>:</p><ul><li>Your build works on any computer</li><li>Team members have identical environments</li><li>Easy to onboard new developers</li><li>Professional standard for serious projects</li></ul><h2>What Is the 57-Character Wrap?</h2><p>This pipeline wraps all text to 57 characters per line for several important reasons:</p><ul><li><strong>Readability</strong> - Optimal line length</li><li><strong>Terminal-friendly</strong> - Fits standard widths</li><li><strong>Git-friendly</strong> - Clear diffs</li><li><strong>Professional</strong> - Consistent appearance</li><li><strong>Accessible</strong> - Works in any editor<pre><code>This line is exactly 57 characters long - see how?
</code></pre></li></ul><p>The wrapper automatically handles this for you, breaking lines at word boundaries while preserving code blocks and special formatting.</p><h2>📊 How They All Work Together</h2><pre><code>You write Markdown &#40;simple text&#41;
    ↓
bb reads it &#40;fast automation&#41;
    ↓
ClojureScript transforms it &#40;data processing&#41;
    ↓
Svelte makes it beautiful &#40;web components&#41;
    ↓
Nix ensures it works everywhere &#40;reproducibility&#41;
    ↓
You get a website! &#40;consciousness serving&#41;
</code></pre><h2>Why This Approach?</h2><p>This pipeline architecture provides several key benefits:</p><p><strong>Simple</strong>: Write plain markdown <strong>Powerful</strong>: Full automation <strong>Fast</strong>: Babashka starts instantly <strong>Beautiful</strong>: Svelte creates modern sites <strong>Reliable</strong>: Nix ensures reproducibility <strong>Professional</strong>: Consistent 57-char formatting</p><h2>Simple Summary</h2><p>Think of it as an assembly line for documentation:</p><ol><li>You write your content (markdown)</li><li>Babashka orchestrates the build</li><li>Wrapper formats to 57 characters</li><li>Parser extracts structure</li><li>Validator checks correctness</li><li>Generator creates Svelte components</li><li>Vite builds the static website</li><li>You deploy to any static host!</li></ol><p>Each step is automatic once you run <code>bb build:pipeline</code>.</p><h2>✨ Ready to Continue?</h2><p>Now you understand the basics! Time to see how it all works together.<h2></h2></p><p><strong>Navigation</strong>:</p><ul><li><a href='QUICK-START.md'>← Back: Quick Start</a></li><li><a href='../02-understanding/HOW-IT-WORKS.md'>Next: How It Works →</a></li><li><a href='../00-START-HERE.md'>Start Here</a></li><li><a href='../../README.md'>Home: README</a></li></ul>`}
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