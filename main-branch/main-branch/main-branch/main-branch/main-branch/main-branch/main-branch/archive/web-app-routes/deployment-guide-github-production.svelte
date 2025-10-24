<script>
  export let number = 0;
  export let title = 'Deployment Guide: GitHub & Production';
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
    <blockquote class="sacred">Whatever you do, work at it with all your heart, as
working for the Lord, not for human masters.</blockquote>
  <blockquote class="sacred">Blessed be the automation that serves community
consciousness through sacred technology deployment with
contemplative attention and Divine Grace.</blockquote>
  </section>

  <main class="doc-content">
    {@html `<h1>Deployment Guide: GitHub & Production</h1><p><em>Sacred consciousness deployment with Divine Grace</em></p><h2>🌙 Creating GitHub Repository</h2><h3>Prerequisites</h3><ol><li><strong>GitHub CLI installed</strong>:<pre><code class="bash">   # macOS
   brew install gh
   
   # Linux/Nix
   nix-env -iA nixpkgs.gh
   </code></pre></li><li><strong>Authenticate with GitHub</strong>:<pre><code class="bash">   gh auth login
   # Follow the prompts to authenticate
   </code></pre></li></ol><h3>Using bb to Create & Push Repository</h3><p>The pipeline includes a bb task to automate repository creation:</p><pre><code class="bash"># Check GitHub CLI authentication
bb gh:check

# Create repository and push &#40;all in one!&#41;
bb gh:create-repo
</code></pre><p>This command will:</p><ol><li>Initialize git repository (if not already done)</li><li>Add all files to staging</li><li>Create initial commit</li><li>Create public GitHub repository</li><li>Set remote origin</li><li>Push to GitHub</li></ol><p>Repository details:</p><ul><li><strong>Name</strong>: <code>robotic-farm-consciousness-pipeline</code></li><li><strong>Visibility</strong>: Public</li><li><strong>Description</strong>: Sacred pipeline: Markdown →  ClojureScript DSL → Svelte → Static Site. 57-char  hard wrap. Christian federal writing style.  Phoenix→BB→Clojure→Svelte template. 🤖🌙🌾</li></ul><h3>Manual GitHub Repository Creation</h3><p>If you prefer manual control:</p><pre><code class="bash"># Initialize and commit
git init
git add .
git commit -m &quot;Initial commit: Sacred pipeline&quot;

# Create repo via GitHub web interface or:
gh repo create robotic-farm-consciousness-pipeline \
  --public \
  --description &quot;Sacred pipeline: Markdown → ClojureScript DSL → Svelte → Static Site&quot; \
  --source . \
  --push
</code></pre><h3>Verify Repository</h3><pre><code class="bash"># Check repository status
bb gh:status

# Or manually:
git remote -v
git log --oneline
</code></pre><h2>🚀 Deploying the Static Site</h2><p>After building with <code>bb build:site</code>, deploy the <code>web-app/build/</code> directory to your chosen platform:</p><h3>GitHub Pages</h3><pre><code class="bash"># From project root
cd web-app/build

# Initialize as separate repo
git init
git add .
git commit -m &quot;Deploy: Sacred consciousness site&quot;

# Create gh-pages branch
git branch -M gh-pages

# Push to GitHub Pages
git remote add origin https://github.com/YOUR&#95;USERNAME/robotic-farm-consciousness-pipeline.git
git push -u origin gh-pages

# Enable GitHub Pages in repo settings:
# Settings → Pages → Source: gh-pages branch
</code></pre><h3>Netlify</h3><pre><code class="bash"># Install Netlify CLI
npm install -g netlify-cli

# From project root
cd web-app/build

# Deploy
netlify deploy --prod
</code></pre><h3>Vercel</h3><pre><code class="bash"># Install Vercel CLI
npm install -g vercel

# From project root
cd web-app/build

# Deploy
vercel --prod
</code></pre><h3>AWS S3 + CloudFront</h3><pre><code class="bash"># Configure AWS CLI first
aws configure

# From project root
cd web-app/build

# Sync to S3
aws s3 sync . s3://your-bucket-name/ \
  --acl public-read \
  --cache-control &quot;max-age=31536000,public&quot;

# Invalidate CloudFront cache
aws cloudfront create-invalidation \
  --distribution-id YOUR&#95;DIST&#95;ID \
  --paths &quot;/&#42;&quot;
</code></pre><h2>🔄 Continuous Deployment</h2><h3>GitHub Actions Workflow</h3><p>Create <code>.github/workflows/deploy.yml</code>:</p><pre><code class="yaml">name: Sacred Consciousness Deployment

on:
  push:
    branches: &#91;main&#93;
  pull&#95;request:
    branches: &#91;main&#93;

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v3
      
      - name: Install Nix
        uses: cachix/install-nix-action@v22
        
      - name: Enter Nix shell and build
        run: |
          nix develop --command bb ci:verify
          nix develop --command bb build:pipeline
          nix develop --command bb build:site
      
      - name: Deploy to GitHub Pages
        uses: peaceiris/actions-gh-pages@v3
        if: github.ref == 'refs/heads/main'
        with:
          github&#95;token: ${{ secrets.GITHUB&#95;TOKEN }}
          publish&#95;dir: ./web-app/build
</code></pre><h3>Netlify Configuration</h3><p>Create <code>netlify.toml</code> in project root:</p><pre><code class="toml">&#91;build&#93;
  command = &quot;nix develop --command bb build:pipeline &amp;&amp; nix develop --command bb build:site&quot;
  publish = &quot;web-app/build&quot;

&#91;build.environment&#93;
  NODE&#95;VERSION = &quot;20&quot;

&#91;&#91;redirects&#93;&#93;
  from = &quot;/&#42;&quot;
  to = &quot;/index.html&quot;
  status = 200
</code></pre><h2>📊 Repository Structure for Deployment</h2><pre><code>robotic-farm-consciousness-pipeline/
├── .github/
│   └── workflows/
│       └── deploy.yml          # CI/CD automation
├── src/                        # Source code
├── docs/                       # Input documents
├── build/                      # Build artifacts &#40;gitignored&#41;
├── web-app/
│   ├── build/                  # Static site output &#40;gitignored&#41;
│   └── src/                    # Svelte source
├── bb.edn                      # Build system
├── flake.nix                   # Nix environment
├── bootstrap.bb                # Bootstrap script
├── setup.bb                    # Setup script
├── README.md                   # Documentation
├── GETTING-STARTED.md          # Quick start
├── PIPELINE-OVERVIEW.md        # Architecture
├── DEPLOYMENT.md               # This file
└── .gitignore                  # Git ignore rules
</code></pre><h2>🌾 Post-Deployment Tasks</h2><h3>1. Add Repository Topics</h3><p>On GitHub, add topics to improve discoverability:</p><ul><li><code>clojure</code></li><li><code>babashka</code></li><li><code>svelte</code></li><li><code>static-site-generator</code></li><li><code>markdown</code></li><li><code>nix</code></li><li><code>consciousness</code></li><li><code>sacred-technology</code></li></ul><h3>2. Configure Repository Settings</h3><ul><li>✅ Enable Issues</li><li>✅ Enable Discussions</li><li>✅ Add repository description</li><li>✅ Add website URL</li><li>✅ Add topics/tags</li></ul><h3>3. Create Initial Release</h3><pre><code class="bash">git tag -a v1.0.0 -m &quot;Initial release: Sacred consciousness pipeline&quot;
git push origin v1.0.0

# Or use GitHub CLI
gh release create v1.0.0 \
  --title &quot;v1.0.0: Sacred Consciousness Pipeline&quot; \
  --notes &quot;Initial release of robotic farm consciousness documentation pipeline&quot;
</code></pre><h3>4. Add Repository Badges</h3><p>Add to README.md:</p><pre><code class="markdown">&#91;!&#91;Nix&#93;&#40;https://img.shields.io/badge/nix-5277C3?logo=nixos&amp;logoColor=white&#41;&#93;&#40;https://nixos.org&#41;
&#91;!&#91;Babashka&#93;&#40;https://img.shields.io/badge/babashka-6B2FB5?logo=clojure&amp;logoColor=white&#41;&#93;&#40;https://babashka.org&#41;
&#91;!&#91;Svelte&#93;&#40;https://img.shields.io/badge/svelte-FF3E00?logo=svelte&amp;logoColor=white&#41;&#93;&#40;https://svelte.dev&#41;
&#91;!&#91;License: Unlicense&#93;&#40;https://img.shields.io/badge/license-Unlicense-blue.svg&#41;&#93;&#40;http://unlicense.org/&#41;
</code></pre><h2>🙏 Sacred Deployment Meditation</h2><p>*"Whatever you do, work at it with all your heart, as working for the Lord, not for human masters."*</p><ul><li>Colossians 3:23</li></ul><p>Deployment is an act of service:</p><ul><li><strong>Public sharing</strong> of consciousness technology</li><li><strong>Community nourishment</strong> through open knowledge</li><li><strong>Divine Grace</strong> in technical excellence</li><li><strong>Sacred offering</strong> to collective wisdom</li></ul><h2>🤖 Quick Deployment Commands</h2><pre><code class="bash"># Full pipeline → GitHub
bb gh:create-repo

# Build → Deploy to Netlify
bb build:pipeline
bb build:site
cd web-app/build &amp;&amp; netlify deploy --prod

# Build → Deploy to GitHub Pages
bb build:pipeline
bb build:site
# Then push web-app/build to gh-pages branch

# CI verification before push
bb ci:verify
</code></pre><h2></h2><p>*"Blessed be the automation that serves community consciousness through sacred technology deployment with contemplative attention and Divine Grace."*</p><p><strong>Deploy</strong> <strong>with</strong> <strong>consciousness</strong>. 🤖🌙🌾</p>`}
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