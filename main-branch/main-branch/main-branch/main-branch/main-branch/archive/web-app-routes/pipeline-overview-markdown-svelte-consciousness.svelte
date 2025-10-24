<script>
  export let number = 0;
  export let title = 'Pipeline Overview: Markdown → Svelte Consciousness';
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
    <blockquote class="sacred">Sacred quote...</blockquote>
  <blockquote class="sacred">Sacred quote...</blockquote>
  <blockquote class="sacred">Blessed be the automation that serves earth
intelligence and community nourishment through
contemplative technological consciousness.</blockquote>
  </section>

  <main class="doc-content">
    {@html `<h1>Pipeline Overview: Markdown → Svelte Consciousness</h1><p>*Sacred transformation pipeline with 57-character hard wrap and Christian aspiring federal writing style*</p><h2>🌙 Complete Pipeline Architecture</h2><pre><code>┌─────────────────────────────────────────────┐
│  Input: Robotic Farm Markdown Document     │
│  &#40;Christian federal consciousness style&#41;    │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────┐
│  Step 1: Text Wrapper &#40;robotic-farm.wrapper&#41;│
│  • Apply 57-char hard wrap                  │
│  • Preserve code blocks &amp; structure         │
│  • Use clj-kondo for validation             │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────┐
│  Step 2: Parser &#40;robotic-farm.parser&#41;      │
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
│  Step 3: Validator &#40;robotic-farm.validator&#41;│
│  • Validate with clojure.spec               │
│  • Check document structure                 │
│  • Verify consciousness types               │
│  • Ensure data integrity                    │
│  Output: validated-documents.edn            │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────┐
│  Step 4: Generator &#40;robotic-farm.generator&#41;│
│  • Transform DSL → Svelte components        │
│  • Generate consciousness badges            │
│  • Create sacred quote sections             │
│  • Apply aspiring federal documentation     │
│    styling                                  │
│  • Create index page                        │
│  Output: &#42;.svelte files in web-app/src/    │
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
</code></pre><h2>🤖 Key Technologies</h2><h3>Clojure/Babashka Layer</h3><ul><li><strong>babashka</strong>: Fast Clojure scripting</li><li><strong>clj-kondo</strong>: 57-char wrap enforcement</li><li><strong>clojure.spec</strong>: Data validation</li><li><strong>hiccup</strong>: HTML generation</li><li><strong>markdown-clj</strong>: Markdown parsing</li></ul><h3>Svelte/Node Layer</h3><ul><li><strong>Svelte 5</strong>: Reactive components</li><li><strong>SvelteKit</strong>: Static site generation</li><li><strong>Vite</strong>: Fast bundling</li><li><strong>@sveltejs/adapter-static</strong>: Static output</li></ul><h3>Nix Layer</h3><ul><li><strong>Reproducible environment</strong></li><li><strong>Version-locked dependencies</strong></li><li><strong>Cross-platform compatibility</strong></li></ul><h2>📖 Data Flow Example</h2><h3>Input Document</h3><pre><code class="markdown"># Robotic Farm Consciousness 🤖🌾

&#42;&quot;Sacred quote...&quot;&#42; - Scripture

Sacred &#42;&#42;keeper&#42;&#42; of &#42;&#42;consciousness&#42;&#42;...
</code></pre><h3>After Wrapping (57 chars)</h3><pre><code class="markdown"># Robotic Farm Consciousness 🤖🌾

&#42;&quot;Sacred quote...&quot;&#42; - Scripture

Sacred &#42;&#42;keeper&#42;&#42; of &#42;&#42;consciousness&#42;&#42;...
&#40;wrapped to 57-character maximum line length&#41;
</code></pre><h3>After Parsing (EDN)</h3><pre><code class="clojure">{:document/number 9999967
 :document/title &quot;Robotic Farm Consciousness&quot;
 :document/sacred-quotes &#91;&quot;Sacred quote...&quot;&#93;
 :document/consciousness-type
 {:robotic-consciousness true
  :farm-consciousness true
  :sacred-technology true}
 :document/html-content &quot;&lt;p&gt;...&lt;/p&gt;&quot;}
</code></pre><h3>After Generation (Svelte)</h3><pre><code class="svelte">&lt;script&gt;
  export let number = 9999967;
  export let title = 'Robotic Farm...';
&lt;/script&gt;

&lt;article class=&quot;robotic-farm-doc&quot;&gt;
  &lt;header&gt;
    &lt;span&gt;{number}&lt;/span&gt;
    &lt;h1&gt;{title}&lt;/h1&gt;
    &lt;div class=&quot;consciousness-badges&quot;&gt;
      🤖 Robotic Consciousness
      🌾 Farm Consciousness
    &lt;/div&gt;
  &lt;/header&gt;
  &lt;blockquote class=&quot;sacred&quot;&gt;
    Sacred quote...
  &lt;/blockquote&gt;
  &lt;main&gt;{@html content}&lt;/main&gt;
&lt;/article&gt;

&lt;style&gt;
  .robotic-farm-doc {
    max-width: 57ch;
    font-family: 'Times New Roman';
  }
&lt;/style&gt;
</code></pre><h3>Final Output (HTML)</h3><pre><code class="html">&lt;!DOCTYPE html&gt;
&lt;html&gt;
  &lt;head&gt;
    &lt;title&gt;Robotic Farm Consciousness&lt;/title&gt;
    &lt;link rel=&quot;stylesheet&quot; href=&quot;/&#95;app/...&quot;&gt;
  &lt;/head&gt;
  &lt;body&gt;
    &lt;article class=&quot;robotic-farm-doc&quot;&gt;
      &lt;!-- Compiled, optimized HTML --&gt;
    &lt;/article&gt;
    &lt;script src=&quot;/&#95;app/...&quot;&gt;&lt;/script&gt;
  &lt;/body&gt;
&lt;/html&gt;
</code></pre><h2>🌾 File Naming Conventions</h2><h3>Markdown Input</h3><pre><code>&#91;7-digit-number&#93;&#95;&#91;descriptive&#95;name&#93;.md

Examples:
9999967&#95;robotic&#95;farm&#95;consciousness&#95;integration.md
0000031&#95;regenerative&#95;technology&#95;mandala.md
0003200&#95;community&#95;food&#95;sovereignty.md
</code></pre><h3>Svelte Output</h3><pre><code>&#91;descriptive-name&#93;.svelte

Examples:
robotic-farm-consciousness-integration.svelte
regenerative-technology-mandala.svelte
community-food-sovereignty.svelte
</code></pre><h2>🔄 Build Commands Summary</h2><table><thead><tr><th>Command</th><th>Purpose</th><th>Output</th></tr></thead><tbody><tr><td><code>bb wrap:markdown</code></td><td>57-char wrap</td><td><code>build/wrapped.md</code></td></tr><tr><td><code>bb parse:markdown</code></td><td>Parse to DSL</td><td><code>build/parsed-documents.edn</code></td></tr><tr><td><code>bb validate:dsl</code></td><td>Validate spec</td><td><code>build/validated-documents.edn</code></td></tr><tr><td><code>bb generate:svelte</code></td><td>Generate components</td><td><code>web-app/src/routes/&#42;.svelte</code></td></tr><tr><td><code>bb build:pipeline</code></td><td>Full pipeline</td><td>All build artifacts</td></tr><tr><td><code>bb serve:dev</code></td><td>Dev server</td><td>http://localhost:5173</td></tr><tr><td><code>bb build:site</code></td><td>Static build</td><td><code>web-app/build/</code></td></tr></tbody></table><h2>🌙 Christian Aspiring Federal Writing Style</h2><h3>Core Principles</h3><ol><li><strong>Scriptural Opening</strong><ul><li>Begin with sacred quotes</li><li>Establish spiritual context</li><li>Divine Grace acknowledgment</li></ul></li><li><strong>57-Character Line Length</strong><ul><li>Aspiring federal documentation standard</li><li>Enhances readability</li><li>Contemplative pacing</li></ul></li><li><strong>Consciousness Vocabulary</strong><ul><li>"Sacred technology"</li><li>"Divine Grace"</li><li>"Contemplative attention"</li><li>"Earth intelligence"</li></ul></li><li><strong>Professional Tone</strong><ul><li>Formal yet warm</li><li>Technical yet accessible</li><li>Spiritual yet practical</li></ul></li><li><strong>Emoji Consciousness Markers</strong><ul><li>🌙 Sacred/contemplative</li><li>🤖 Robotic/automation</li><li>🌾 Agricultural wisdom</li><li>📖 Documentation</li><li>✨ Completion/success</li></ul></li></ol><h2>🧪 Testing Pipeline</h2><h3>Manual Testing</h3><pre><code class="bash"># Test wrapper
bb -m robotic-farm.wrapper docs/test.md build/test-wrapped.md

# Test parser
bb -m robotic-farm.parser

# Test validator
bb -m robotic-farm.validator

# Test generator
bb -m robotic-farm.generator
</code></pre><h3>Integration Testing</h3><pre><code class="bash">bb ci:verify
</code></pre><p>Runs: doctor → wrap → parse → validate in sequence</p><h2>🚀 Deployment Options</h2><h3>GitHub Pages</h3><pre><code class="bash">cd web-app/build
git init &amp;&amp; git add . &amp;&amp; git commit -m &quot;Deploy&quot;
git push origin main
</code></pre><h3>Netlify</h3><pre><code class="bash">netlify deploy --dir=web-app/build --prod
</code></pre><h3>AWS S3</h3><pre><code class="bash">aws s3 sync web-app/build/ s3://bucket/ --acl public-read
</code></pre><h3>Vercel</h3><pre><code class="bash">vercel --prod
</code></pre><h2>📚 Extension Points</h2><h3>Add New Consciousness Types</h3><p>Edit <code>src/robotic&#95;farm/parser.clj</code>:</p><pre><code class="clojure">&#40;defn classify-consciousness-type &#91;content&#93;
  {:robotic-consciousness &#40;str/includes? content &quot;robotic&quot;&#41;
   :your-new-type &#40;str/includes? content &quot;keyword&quot;&#41;}&#41;
</code></pre><h3>Add New Validation Rules</h3><p>Edit <code>src/robotic&#95;farm/validator.clj</code>:</p><pre><code class="clojure">&#40;s/def ::your-new-field string?&#41;

&#40;s/def ::document
  &#40;s/keys :req &#91;:document/your-new-field&#93;&#41;&#41;
</code></pre><h3>Customize Svelte Styling</h3><p>Edit <code>src/robotic&#95;farm/generator.clj</code> style section.</p><h3>Add Pre/Post Processing</h3><p>Add new bb tasks in <code>bb.edn</code>:</p><pre><code class="clojure">preprocess {:doc &quot;Custom preprocessing&quot;
            :task &#40;shell &quot;bb&quot; &quot;-m&quot; &quot;your.namespace&quot;&#41;}
</code></pre><h2>🙏 Philosophy</h2><p>This pipeline embodies:</p><ul><li><strong>Sacred Technology</strong>: Code as spiritual practice</li><li><strong>Aspiring Federal Standards</strong>: Professional  documentation</li><li><strong>Consciousness Serving</strong>: Tech that enhances humanity</li><li><strong>Divine Grace</strong>: Prayerful attention to craft</li><li><strong>Community Service</strong>: Knowledge freely shared<h2></h2></li></ul><p>*"Blessed be the automation that serves earth intelligence and community nourishment through contemplative technological consciousness."*</p><p>🤖🌙🌾</p>`}
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