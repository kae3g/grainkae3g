<script>
  export let number = 0;
  export let title = 'Start Here: Documentation Pipeline Guide';
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
    {@html `<h1>Start Here: Documentation Pipeline Guide</h1><p>Welcome to the Documentation Pipeline! This system helps you create beautiful, professional documentation websites from simple markdown files.</p><h2>What Is This Project?</h2><p>This is a <strong>documentation build pipeline</strong> that transforms markdown documents into beautiful compiled websites using:</p><ul><li><strong>Babashka</strong> (fast Clojure scripting)</li><li><strong>Svelte</strong> (reactive web components)</li><li><strong>Nix</strong> (reproducible builds)</li><li><strong>57-Character Lines</strong> (readable formatting)</li><li><strong>Automated Processing</strong> (one command builds)</li></ul><p>Whether you're documenting software, creating technical guides, or building a knowledge base, this pipeline makes it simple and professional.</p><h2>📖 Reading Order for Beginners</h2><p>Follow this sacred path of understanding:</p><h3>Level 1: Getting Started (Begin Here!)</h3><ol><li><strong><a href='01-getting-started/QUICK-START.md'>Quick Start</a></strong><ul><li>Install tools (5 minutes)</li><li>Run first command</li><li>See results</li></ul></li><li><strong><a href='01-getting-started/CONCEPTS.md'>Basic Concepts</a></strong><ul><li>What is markdown?</li><li>What is a pipeline?</li><li>What is Babashka?</li></ul></li></ol><h3>Level 2: Understanding the System</h3><ol><li><strong><a href='02-understanding/HOW-IT-WORKS.md'>How It Works</a></strong><ul><li>Pipeline flow diagram</li><li>Simple explanation</li><li>Example transformation</li></ul></li><li><strong><a href='02-understanding/PROJECT-STRUCTURE.md'>Project Structure</a></strong><ul><li>Directory layout</li><li>What each file does</li><li>Where to find things</li></ul></li><li><strong><a href='02-understanding/PIPELINE-OVERVIEW.md'>Architecture Overview</a></strong><ul><li>Deep dive into design</li><li>Technical details</li><li>Data flow</li></ul></li></ol><h3>Level 3: Using the Pipeline</h3><ol><li><strong><a href='03-using/COMMON-COMMANDS.md'>Common Commands</a></strong><ul><li>bb commands explained</li><li>When to use each</li><li>Examples</li></ul></li><li><strong><a href='03-using/TEST-ALL-COMMANDS.md'>Testing Guide</a></strong><ul><li>How to test</li><li>What to expect</li><li>Troubleshooting</li></ul></li><li><strong><a href='03-using/RUN-COMMANDS.md'>Running Commands</a></strong><ul><li>Step-by-step tutorials</li><li>Complete walkthroughs</li><li>Success verification</li></ul></li></ol><h3>Level 4: Advanced Topics</h3><ol><li><strong><a href='04-advanced/DEPLOYMENT.md'>Deployment</a></strong><ul><li>GitHub setup</li><li>Static site hosting</li><li>Production deployment</li></ul></li><li><strong><a href='04-advanced/CREATE-GITHUB-REPO.md'>Creating Repos</a></strong><ul><li>GitHub integration</li><li>Automated pushing</li><li>Repository management</li></ul></li><li><strong><a href='02-understanding/BB-ARCHITECTURE.md'>Babashka Deep Dive</a></strong><ul><li>How bb orchestrates</li><li>Task dependencies</li><li>Advanced patterns</li></ul></li></ol><h2>Quick Start (Right Now!)</h2><p>Get started in just 5 minutes with these simple steps:</p><pre><code class="bash"># 1. Enter this directory
cd &#126;/kae3g/12025-10-04

# 2. Check if you have the tools
bb bootstrap

# 3. Set up the project
./setup.bb

# 4. See what you can do
bb tasks

# 5. Try building something!
bb build:pipeline
</code></pre><h2>Project Philosophy</h2><p>This pipeline is designed with these principles:</p><ul><li><strong>Simplicity</strong> - Easy to use and understand</li><li><strong>Quality</strong> - Professional output every time</li><li><strong>Speed</strong> - Fast builds with Babashka</li><li><strong>Standards</strong> - Consistent 57-char formatting</li><li><strong>Automation</strong> - Build, validate, deploy easily</li></ul><h2>For Beginners</h2><p>If you're new to documentation pipelines or coding:</p><ol><li>Read <a href='01-getting-started/CONCEPTS.md'>Basic Concepts</a></li><li>Follow <a href='01-getting-started/QUICK-START.md'>Quick Start</a></li><li>Type commands exactly as shown</li><li>Watch the results! ✨</li><li>Learn by doing, one step at a time</li></ol><p>Don't worry if you don't understand everything at first. The guides explain each concept clearly.</p><h2>Navigation</h2><p><strong>You are here</strong>: Start Here → <a href='01-getting-started/QUICK-START.md'>Next: Quick Start →</a></p><p><strong>Project Root</strong>: <a href='../README.md'>Main README</a></p><h2>📚 All Guides at a Glance</h2><pre><code>guides/
├── 00-START-HERE.md &#40;You are here!&#41;
├── 01-getting-started/
│   ├── QUICK-START.md
│   └── CONCEPTS.md
├── 02-understanding/
│   ├── HOW-IT-WORKS.md
│   ├── PROJECT-STRUCTURE.md
│   ├── PIPELINE-OVERVIEW.md
│   ├── BB-ARCHITECTURE.md
│   └── PROJECT-SUMMARY.md
├── 03-using/
│   ├── COMMON-COMMANDS.md
│   ├── TEST-ALL-COMMANDS.md
│   └── RUN-COMMANDS.md
└── 04-advanced/
    ├── DEPLOYMENT.md
    ├── CREATE-GITHUB-REPO.md
    └── FINAL-SUMMARY.md
</code></pre><h2>✨ Learning Path</h2><p><strong>New to everything?</strong> → Start with Concepts <strong>Want quick results?</strong> → Jump to Quick Start <strong>Understanding first?</strong> → Read How It Works <strong>Ready to build?</strong> → Try Common Commands <strong>Want to deploy?</strong> → Read Deployment</p><p>Start your documentation journey today and create beautiful, professional websites from your markdown!<h2></h2></p><p><strong>Next</strong>: <a href='01-getting-started/QUICK-START.md'>Quick Start Guide →</a></p>`}
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