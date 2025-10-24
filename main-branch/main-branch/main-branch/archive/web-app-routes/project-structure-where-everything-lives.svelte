<script>
  export let number = 0;
  export let title = 'Project Structure: Where Everything Lives';
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
    <blockquote class="sacred">A place for everything, and everything in its place.</blockquote>
  <blockquote class="sacred">In my Father's house are many rooms.</blockquote>
  <blockquote class="sacred">Seek and you will find.</blockquote>
  <blockquote class="sacred">Every good tree bears good fruit.</blockquote>
  <blockquote class="sacred">Do not store up treasures on earth... but in heaven.</blockquote>
  <blockquote class="sacred">I am the vine; you are the branches.</blockquote>
  <blockquote class="sacred">Where do I put my markdown?</blockquote>
  <blockquote class="sacred">Where are the bb commands defined?</blockquote>
  <blockquote class="sacred">Where's the generated website?</blockquote>
  <blockquote class="sacred">Where do I learn how to use this?</blockquote>
  <blockquote class="sacred">Where's the source code?</blockquote>
  <blockquote class="sacred">Ask and it will be given to you.</blockquote>
  </section>

  <main class="doc-content">
    {@html `<h1>Project Structure: Where Everything Lives</h1><p><em>"A place for everything, and everything in its place."</em></p><p>Learn the directory layout and what each file does. 🌙</p><h2>🤖 The Big Picture</h2><pre><code>kae3g/12025-10-04/
├── guides/          ← Documentation &#40;you are here!&#41;
├── src/             ← Source code &#40;the brain&#41;
├── scripts/         ← Automation scripts
├── docs/            ← Your markdown files
├── web-app/         ← Website output
├── build/           ← Temporary build files
├── examples/        ← Example documents
├── bb.edn           ← bb task definitions
├── flake.nix        ← Nix configuration
└── README.md        ← Project introduction
</code></pre><p><em>"In my Father's house are many rooms."</em> - The Gospel According to Jesus (Stephen Mitchell)</p><h2>📚 guides/ - Documentation for Humans</h2><pre><code>guides/
├── 00-START-HERE.md           ← Begin here!
├── 01-getting-started/
│   ├── QUICK-START.md         ← 5-minute tutorial
│   └── CONCEPTS.md            ← Basic ideas
├── 02-understanding/
│   ├── HOW-IT-WORKS.md        ← Pipeline flow
│   ├── PROJECT-STRUCTURE.md   ← This file!
│   ├── PIPELINE-OVERVIEW.md   ← Technical deep-dive
│   ├── BB-ARCHITECTURE.md     ← bb orchestration
│   └── PROJECT-SUMMARY.md     ← Complete overview
├── 03-using/
│   ├── COMMON-COMMANDS.md     ← Daily commands
│   ├── TEST-ALL-COMMANDS.md   ← Testing guide
│   └── RUN-COMMANDS.md        ← Step-by-step
└── 04-advanced/
    ├── DEPLOYMENT.md          ← Publishing sites
    ├── CREATE-GITHUB-REPO.md  ← GitHub setup
    └── FINAL-SUMMARY.md       ← Implementation notes
</code></pre><p><strong>Purpose</strong>: Help you learn and understand!</p><p><em>"Seek and you will find."</em> - Gospel According to Jesus (Stephen Mitchell)</p><h2>🧠 src/ - The Brain of the Pipeline</h2><pre><code>src/
└── robotic&#95;farm/
    ├── wrapper.clj         ← Wraps text to 57 chars
    ├── edn&#95;wrapper.clj     ← Wraps EDN files
    ├── parser.clj          ← MD → ClojureScript
    ├── validator.clj       ← Quality checks
    └── generator.clj       ← DSL → Svelte
</code></pre><p><strong>What each does</strong>:</p><p><strong>wrapper.clj</strong>: Takes long lines and makes them 57 characters or less while keeping them readable.</p><p><strong>edn_wrapper.clj</strong>: Same but for EDN (data) files.</p><p><strong>parser.clj</strong>: Reads your markdown and converts it into data structures the computer understands.</p><p><strong>validator.clj</strong>: Checks that everything is correct using clojure.spec rules.</p><p><strong>generator.clj</strong>: Takes validated data and creates beautiful Svelte components.</p><p><em>"Every good tree bears good fruit."</em> - Gospel According to Jesus (Stephen Mitchell)</p><h2>🤖 scripts/ - Automation Helpers</h2><pre><code>scripts/
├── push-to-github.bb     ← Creates GitHub repo
└── test-all.bb           ← Runs all tests
</code></pre><p><strong>Pure Babashka</strong> - no shell scripts! Fast and reliable.</p><h2>📖 docs/ - Your Content Goes Here</h2><pre><code>docs/
├── 9999967&#95;robotic&#95;farm&#95;consciousness&#95;integration.md
└── your-document.md      ← Add yours here!
</code></pre><p><strong>This is where you put markdown files to process!</strong></p><p>Place any <code>.md</code> file here and run <code>bb build:pipeline</code> to transform it.</p><h2>🌐 web-app/ - Website Configuration</h2><pre><code>web-app/
├── package.json          ← Node dependencies
├── svelte.config.js      ← Svelte setup
├── vite.config.js        ← Build configuration
├── src/
│   ├── lib/             ← Shared components
│   └── routes/          ← Generated pages &#40;output&#41;
└── build/               ← Compiled site &#40;deploy this&#41;
</code></pre><p><strong>Don't edit routes/ manually!</strong> It's auto-generated.</p><p><em>"Do not store up treasures on earth... but in heaven."</em></p><ul><li>Gospel According to Jesus (Stephen Mitchell)</li></ul><p>The build/ directory is temporary - the real treasure is your markdown content in docs/!</p><h2>🏗️ build/ - Temporary Build Files</h2><pre><code>build/
├── wrapped.md                ← 57-char wrapped
├── parsed-documents.edn      ← Parsed data
└── validated-documents.edn   ← Validated data
</code></pre><p><strong>Purpose</strong>: Intermediate files during the build. You can delete this directory anytime - it rebuilds!</p><h2>📋 examples/ - Learning Materials</h2><pre><code>examples/
└── example-wrapped-output.md  ← Sample output
</code></pre><p><strong>Purpose</strong>: Show you what output looks like.</p><h2>⚙️ Configuration Files (Root Directory)</h2><p><strong>bb.edn</strong> - The heart of the automation!<pre><code class="clojure">{:tasks
 {doctor {...}
  build:pipeline {...}
  serve:dev {...}}}
</code></pre></p><p>Defines all <code>bb</code> commands. 35+ tasks!</p><p><strong>flake.nix</strong> - Reproducible environment<pre><code class="nix">{buildInputs = &#91;
  pkgs.babashka
  pkgs.nodejs&#95;20
  pkgs.clj-kondo
  ...&#93;}
</code></pre></p><p>Ensures everyone has the same tools.</p><p><strong>bootstrap.bb</strong> - Checks prerequisites <strong>setup.bb</strong> - Sets up directories</p><p><em>.gitignore</em> - What Git ignores (build/, node_modules/)</p><p><em>UNLICENSE</em> - Public domain dedication</p><p><em>CHANGELOG.md</em> - Version history</p><h2>🌙 How Files Flow Through the System</h2><pre><code>1. You create:  docs/my-doc.md
2. bb wraps:    build/wrapped.md
3. bb parses:   build/parsed-documents.edn
4. bb validates: build/validated-documents.edn
5. bb generates: web-app/src/routes/my-doc.svelte
6. Vite builds:  web-app/build/my-doc/index.html
</code></pre><p><em>"I am the vine; you are the branches."</em> - Gospel According to Jesus (Stephen Mitchell)</p><p>Each step feeds into the next!</p><h2>🤖 What Can You Safely Modify?</h2><p><strong>✅ Safe to Edit</strong>:</p><ul><li><code>docs/&#42;.md</code> - Your content</li><li><code>guides/&#42;.md</code> - Documentation</li><li><code>examples/&#42;.md</code> - Examples</li><li><code>README.md</code> - Main readme</li></ul><p><strong>⚠️ Edit Carefully</strong>:</p><ul><li><code>bb.edn</code> - Task definitions</li><li><code>src/robotic&#95;farm/&#42;.clj</code> - Pipeline code</li><li><code>flake.nix</code> - Nix packages</li></ul><p><strong>❌ Don't Edit</strong>:</p><ul><li><code>build/</code> - Auto-generated</li><li><code>web-app/src/routes/</code> - Auto-generated</li><li><code>web-app/build/</code> - Auto-generated</li><li><code>node&#95;modules/</code> - Dependencies</li></ul><h2>🌾 Finding What You Need</h2><p><strong>"Where do I put my markdown?"</strong> → <code>docs/</code></p><p><strong>"Where are the bb commands defined?"</strong> → <code>bb.edn</code></p><p><strong>"Where's the generated website?"</strong> → <code>web-app/build/</code></p><p><strong>"Where do I learn how to use this?"</strong> → <code>guides/00-START-HERE.md</code></p><p><strong>"Where's the source code?"</strong> → <code>src/robotic&#95;farm/</code></p><p><em>"Ask and it will be given to you."</em> - Gospel According to Jesus (Stephen Mitchell)</p><h2>🙏 Design Philosophy</h2><p>The structure follows these principles:</p><p><strong>Clear naming</strong>: Directories explain their purpose <strong>Logical grouping</strong>: Related files together <strong>Beginner-friendly</strong>: Easy to navigate <strong>Sacred attention</strong>: Contemplative organization</p><h2>✨ Summary for Children</h2><p>Think of the project as a house:</p><ul><li><strong>guides/</strong> = Library (where you learn)</li><li><strong>src/</strong> = Kitchen (where magic happens)</li><li><strong>docs/</strong> = Your room (where you create)</li><li><strong>web-app/</strong> = Display case (what others see)</li><li><strong>build/</strong> = Workshop (temporary mess)</li><li><strong>bb.edn</strong> = Recipe book (instructions)<h2></h2></li></ul><p><strong>Navigation</strong>:</p><ul><li><a href='HOW-IT-WORKS.md'>← Back: How It Works</a></li><li><a href='PIPELINE-OVERVIEW.md'>Next: Pipeline Overview →</a></li><li><a href='../00-START-HERE.md'>Start Here</a></li><li><a href='../../README.md'>Home: README</a></li></ul><p>🤖🌙🌾</p>`}
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