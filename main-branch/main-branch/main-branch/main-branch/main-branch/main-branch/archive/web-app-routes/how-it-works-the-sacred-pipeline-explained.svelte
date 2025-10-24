<script>
  export let number = 0;
  export let title = 'How It Works: The Sacred Pipeline Explained';
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
    <blockquote class="sacred">I am the way, the truth, and the life.</blockquote>
  <blockquote class="sacred">I have come that they may have life, and have it to
the full.</blockquote>
  <blockquote class="sacred">Sacred quote...</blockquote>
  <blockquote class="sacred">My yoke is easy and my burden is light.</blockquote>
  <blockquote class="sacred">Sacred quote...</blockquote>
  <blockquote class="sacred">You will know the truth.</blockquote>
  <blockquote class="sacred">By their fruits you will know them.</blockquote>
  <blockquote class="sacred">Whatever you ask in my name, I will do it.</blockquote>
  <blockquote class="sacred">A house built on rock withstands the storm.</blockquote>
  <blockquote class="sacred">Let your light shine before others.</blockquote>
  </section>

  <main class="doc-content">
    {@html `<h1>How It Works: The Sacred Pipeline Explained</h1><p><em>"I am the way, the truth, and the life."</em> - The Gospel According to Jesus (Stephen Mitchell)</p><p>See the complete flow from markdown to website. 🌙</p><h2>🤖 The Big Picture</h2><pre><code>┌─────────────────┐
│ You Write       │ ← Simple markdown text
│ Markdown        │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ bb wrap:markdown│ ← Formats to 57 characters
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ bb parse:markdown│ ← Converts to data
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ bb validate:dsl │ ← Checks it's correct
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ bb generate:svelte│ ← Creates components
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ bb build:site   │ ← Compiles website
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Beautiful       │ ← Ready to share!
│ Website         │
└─────────────────┘
</code></pre><p>*"I have come that they may have life, and have it to the full."* - Gospel According to Jesus (Stephen Mitchell)</p><h2>📖 Step by Step Explanation</h2><h3>Step 1: You Write Markdown</h3><pre><code class="markdown"># Robotic Farm Consciousness 🤖

&#42;&quot;Sacred quote...&quot;&#42;

This is my document about consciousness.

## Main Ideas

- First idea
- Second idea
- Third idea
</code></pre><p>Simple text with special symbols!</p><h3>Step 2: bb Wraps It (57 Characters)</h3><p><em>"My yoke is easy and my burden is light."</em> - Gospel According to Jesus (Stephen Mitchell)</p><pre><code class="bash">bb wrap:markdown
</code></pre><p>Takes long lines and wraps them:</p><pre><code class="markdown"># Robotic Farm Consciousness 🤖

&#42;&quot;Sacred quote...&quot;&#42;

This is my document about consciousness
technology.

## Main Ideas

- First idea about consciousness
- Second idea about automation
- Third idea about service
</code></pre><p>Each line ≤ 57 characters = professional format!</p><h3>Step 3: bb Parses It (To Data)</h3><pre><code class="bash">bb parse:markdown
</code></pre><p>Converts to ClojureScript data structure:</p><pre><code class="clojure">{:document/number 1
 :document/title &quot;Robotic Farm Consciousness&quot;
 :document/body &quot;Full text...&quot;
 :document/sacred-quotes &#91;&quot;Sacred quote...&quot;&#93;
 :document/consciousness-type
 {:robotic-consciousness true
  :farm-consciousness true}
 :document/html-content &quot;&lt;p&gt;Full text...&lt;/p&gt;&quot;}
</code></pre><p><em>"You will know the truth."</em> - Gospel According to Jesus (Stephen Mitchell)</p><p>Now the computer understands your document!</p><h3>Step 4: bb Validates It (Quality Check)</h3><pre><code class="bash">bb validate:dsl
</code></pre><p>Uses clojure.spec to verify:</p><ul><li>✅ Has title?</li><li>✅ Has content?</li><li>✅ Has correct format?</li><li>✅ All fields present?</li></ul><p>Like a quality inspector checking everything is good!</p><p><em>"By their fruits you will know them."</em> - Gospel According to Jesus (Stephen Mitchell)</p><h3>Step 5: bb Generates Svelte (Creates Components)</h3><pre><code class="bash">bb generate:svelte
</code></pre><p>Transforms data → Svelte component:</p><pre><code class="svelte">&lt;script&gt;
  export let title = &quot;Robotic Farm Consciousness&quot;;
  export let number = 1;
&lt;/script&gt;

&lt;article class=&quot;sacred-doc&quot;&gt;
  &lt;header&gt;
    &lt;h1&gt;{title}&lt;/h1&gt;
    &lt;div class=&quot;badges&quot;&gt;
      🤖 Robotic Consciousness
      🌾 Farm Consciousness
    &lt;/div&gt;
  &lt;/header&gt;
  
  &lt;main&gt;
    {@html content}
  &lt;/main&gt;
&lt;/article&gt;

&lt;style&gt;
  .sacred-doc {
    max-width: 57ch;
    margin: 2rem auto;
    font-family: 'Times New Roman';
  }
&lt;/style&gt;
</code></pre><p>Beautiful, reactive web component!</p><h3>Step 6: Vite Builds It (Compiles Website)</h3><pre><code class="bash">bb build:site
</code></pre><p>Vite takes Svelte components and creates:</p><ul><li>Optimized HTML</li><li>Minified JavaScript</li><li>Fast CSS</li><li>Static website</li></ul><p>Ready to deploy! 🚀</p><h2>🌾 The Complete Command</h2><pre><code class="bash">bb build:pipeline
</code></pre><p>Runs steps 2-5 automatically! One command, complete transformation.</p><p><em>"Whatever you ask in my name, I will do it."</em> - Gospel According to Jesus (Stephen Mitchell)</p><h2>🤖 What Happens Behind the Scenes?</h2><h3>In the <code>src/robotic&#95;farm/</code> Directory:</h3><p><strong>wrapper.clj</strong>:</p><ul><li>Reads markdown</li><li>Splits into lines</li><li>Wraps at 57 chars</li><li>Preserves structure</li></ul><p><strong>parser.clj</strong>:</p><ul><li>Reads wrapped markdown</li><li>Extracts metadata</li><li>Classifies content</li><li>Converts to EDN</li></ul><p><strong>validator.clj</strong>:</p><ul><li>Loads EDN</li><li>Runs spec checks</li><li>Reports errors</li><li>Confirms validity</li></ul><p><strong>generator.clj</strong>:</p><ul><li>Reads validated EDN</li><li>Generates Svelte markup</li><li>Creates styles</li><li>Writes component files</li></ul><p><strong>All orchestrated by bb.edn!</strong></p><h2>🌙 Real Example</h2><h3>Input (Your Markdown):</h3><pre><code class="markdown"># Test Document 🤖

This is a test of the sacred pipeline.

## What It Does

It transforms consciousness through technology.
</code></pre><h3>After Wrapping:</h3><pre><code class="markdown"># Test Document 🤖

This is a test of the sacred pipeline.

## What It Does

It transforms consciousness through
technology.
</code></pre><h3>After Parsing:</h3><pre><code class="clojure">{:document/number 1
 :document/title &quot;Test Document&quot;
 :document/body &quot;This is a test...&quot;}
</code></pre><h3>After Validation:</h3><pre><code>✨ Valid: 1 / 1
🤖 All documents valid
</code></pre><h3>After Generation:</h3><pre><code>web-app/src/routes/test-document.svelte
</code></pre><h3>After Building:</h3><pre><code>web-app/build/test-document/index.html
</code></pre><p><strong>Ready for the world!</strong> 🌍</p><h2>🙏 Why This Design?</h2><p><em>"A house built on rock withstands the storm."</em> - Gospel According to Jesus (Stephen Mitchell)</p><p><strong>Separation of concerns</strong>:</p><ul><li>Each step does one thing</li><li>Easy to understand</li><li>Easy to fix</li><li>Easy to extend</li></ul><p><strong>Pure functions</strong>:</p><ul><li>Predictable results</li><li>Testable</li><li>Reproducible</li><li>Reliable</li></ul><p><strong>bb orchestration</strong>:</p><ul><li>Fast startup</li><li>Simple commands</li><li>Easy to learn</li><li>Powerful automation</li></ul><h2>✨ Summary for Children</h2><p>You write a story (markdown) → Robot reads it (parser) → Robot checks it (validator) → Robot makes it pretty (generator) → Website appears (build)!</p><p><em>"Let your light shine before others."</em> - Gospel According to Jesus (Stephen Mitchell)</p><h2>🚀 Want to See It in Action?</h2><p>Go try it: <a href='../01-getting-started/QUICK-START.md'>Quick Start →</a><h2></h2></p><p><strong>Navigation</strong>:</p><ul><li><a href='../01-getting-started/CONCEPTS.md'>← Back: Concepts</a></li><li><a href='PROJECT-STRUCTURE.md'>Next: Project Structure →</a></li><li><a href='../00-START-HERE.md'>Start Here</a></li><li><a href='../../README.md'>Home: README</a></li></ul><p>🤖🌙🌾</p>`}
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