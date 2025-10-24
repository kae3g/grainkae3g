<script>
  export let number = 0;
  export let title = 'Test Document';
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
    {@html `<h1>Test Document</h1><p>This is a test document for the documentation pipeline. It demonstrates the 57-character line wrapping and markdown processing capabilities that make technical documentation clear and accessible.</p><h2>Features</h2><p>The pipeline includes these powerful features:</p><ul><li><strong>Markdown Processing</strong> - Parse and transform text</li><li><strong>57-Character Wrapping</strong> - Clean, readable format</li><li><strong>Svelte Generation</strong> - Modern web components</li><li><strong>Static Site Output</strong> - Fast, zero dependencies</li><li><strong>Automated Building</strong> - One command deployment</li></ul><h2>Pipeline Stages</h2><p>Your document flows through these automated stages:</p><ol><li><strong>Wrapping</strong> - Apply 57-character line limit</li><li><strong>Parsing</strong> - Extract structure and metadata</li><li><strong>Validation</strong> - Verify spec compliance</li><li><strong>Generation</strong> - Create Svelte components</li><li><strong>Build</strong> - Compile to static website</li></ol><p>Each stage ensures quality and consistency.</p><h2>Quick Start Commands</h2><pre><code class="bash"># Check your system
bb bootstrap

# Build documentation
bb build:pipeline

# Start development server
bb serve:dev
</code></pre><h2>Why This Approach?</h2><p><strong>Readable</strong>: 57 characters per line is easy to read <strong>Portable</strong>: Works in terminals and editors <strong>Professional</strong>: Consistent formatting throughout <strong>Fast</strong>: Babashka starts instantly <strong>Modern</strong>: Svelte 5 for beautiful websites</p><h2>Conclusion</h2><p>This test document verifies the complete pipeline functionality from markdown source to static website generation. Try editing this file and running the pipeline to see your changes come to life!</p>`}
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