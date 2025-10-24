<script>
  export let number = 0;
  export let title = 'Documentation Pipeline Example Output';
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
    {@html `<h1>Documentation Pipeline Example Output</h1><p>This file demonstrates the 57-character line wrapping that the pipeline applies to all markdown documents. Notice how each paragraph flows naturally while maintaining consistent line length for optimal readability.</p><h2>Why 57 Characters?</h2><p>The 57-character line limit provides several important benefits for technical documentation:</p><ul><li><strong>Terminal Friendly</strong> - Fits standard widths</li><li><strong>Easy Reading</strong> - Optimal line length</li><li><strong>Git Friendly</strong> - Clear diffs and changes</li><li><strong>Professional</strong> - Consistent appearance</li><li><strong>Accessible</strong> - Works in all editors</li></ul><h2>Example Code Block</h2><p>Code blocks are preserved exactly as written, without wrapping, to maintain proper syntax:</p><pre><code class="javascript">// This code block maintains its formatting
function buildDocumentation&#40;source&#41; {
  const wrapped = wrapText&#40;source, 57&#41;;
  const parsed = parseMarkdown&#40;wrapped&#41;;
  const validated = validateStructure&#40;parsed&#41;;
  return generateSite&#40;validated&#41;;
}
</code></pre><h2>Automated Pipeline</h2><p>The documentation pipeline automatically processes your markdown files through multiple stages, each one adding value and ensuring quality:</p><ol><li><strong>Wrapping</strong> - Apply 57-char line limit</li><li><strong>Parsing</strong> - Extract document structure</li><li><strong>Validation</strong> - Verify correct format</li><li><strong>Generation</strong> - Create web components</li><li><strong>Building</strong> - Compile to static HTML</li></ol><h2>Lists and Formatting</h2><p>Both ordered and unordered lists work great:</p><ul><li>First item in the list</li><li>Second item with more content</li><li>Third item with even more text to show wrap</li><li>Fourth item demonstrates consistency</li></ul><p>And numbered lists too:</p><ol><li>First numbered item</li><li>Second numbered item with longer text</li><li>Third item showing how wrapping handles it</li><li>Fourth item for completeness</li></ol><h2>Headers and Structure</h2><p>Headers at all levels are preserved:</p><h3>Third Level Header</h3><h4>Fourth Level Header</h4><h5>Fifth Level Header</h5><h2>Quotes and Emphasis</h2><blockquote><p> This is a blockquote that demonstrates how  quoted text is handled by the pipeline with  proper formatting and line wrapping applied. </p></blockquote><p><strong>Bold text</strong> and <em>italic text</em> both work perfectly within the 57-character constraint.</p><h2>Real-World Usage</h2><p>When you write documentation using this pipeline, you can focus on content without worrying about formatting. The system handles all the details automatically, ensuring your output is always professional and consistent.</p><p>Simply write your markdown naturally, run the build command, and your documentation becomes a beautiful static website ready to deploy.</p><h2>Conclusion</h2><p>This example shows the complete formatting capabilities of the documentation pipeline. Try creating your own markdown files and watch them transform into professional documentation with just one command: <code>bb build:pipeline</code></p>`}
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