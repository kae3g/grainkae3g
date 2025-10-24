<script>
  export let number = 0;
  export let title = 'GitHub Setup: kae3g/12025-10-04';
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
    <blockquote class="sacred">By their fruits you will know them.</blockquote>
  </section>

  <main class="doc-content">
    {@html `<h1>GitHub Setup: kae3g/12025-10-04</h1><p><em>"By their fruits you will know them."</em></p><ul><li>Gospel According to Jesus (Stephen Mitchell)</li></ul><p>Complete guide for creating and configuring the GitHub repository.</p><h2>Repository Details</h2><p><strong>Name:</strong> kae3g/12025-10-04 <strong>URL:</strong> codeberg.org/kae3g/12025-10-04 <strong>Visibility:</strong> Public <strong>Description:</strong> Aspiring federal documentation pipeline <strong>License:</strong> UNLICENSE</p><h2>Step 1: Authenticate GitHub CLI</h2><pre><code class="bash">gh auth login
</code></pre><p>Follow prompts to authenticate with your GitHub account.</p><h2>Step 2: Create Repository</h2><h3>Automated (Recommended)</h3><pre><code class="bash">bb gh:create-repo
</code></pre><p>This command:</p><ol><li>Creates the repository</li><li>Sets up remote</li><li>Pushes all commits</li></ol><h3>Manual with GitHub CLI</h3><pre><code class="bash">gh repo create kae3g/12025-10-04 \
  --public \
  --description &quot;Aspiring federal documentation pipeline&quot; \
  --source . \
  --push
</code></pre><h3>Manual with GitHub Web</h3><ol><li>Go to: https://github.com/new</li><li>Owner: kae3g</li><li>Name: 12025-10-04</li><li>Visibility: Public</li><li>No initialization (we have files)</li><li>Click "Create repository"</li></ol><p>Then push locally:</p><pre><code class="bash">git remote add origin https://codeberg.org/kae3g/12025-10-04.git
git branch -M main
git push -u origin main
</code></pre><h2>Step 3: Configure Repository</h2><h3>Add Topics</h3><p>Settings → General → Topics:</p><ul><li>babashka</li><li>clojure</li><li>svelte</li><li>nix</li><li>static-site</li><li>aspiring-federal-documentation</li><li>markdown-pipeline</li><li>civic-tech</li></ul><h3>Enable Features</h3><ul><li>✅ Issues: Enabled</li><li>✅ Discussions: Optional</li><li>✅ Wiki: Disabled (use guides/)</li><li>✅ Projects: Optional</li></ul><h3>GitHub Pages (Optional)</h3><p>Settings → Pages:</p><ul><li>Source: GitHub Actions or Deploy from branch</li></ul><h2>Step 4: Verify Deployment</h2><pre><code class="bash"># Check remote
git remote -v

# View on GitHub
gh repo view --web

# Check status
bb gh:status
</code></pre><p>Visit: https://codeberg.org/kae3g/12025-10-04</p><p>Should show:</p><ul><li>✅ All files present</li><li>✅ README renders correctly</li><li>✅ Topics displayed</li><li>✅ License visible</li></ul><h2>GPG Signing (Optional)</h2><p>If GPG database is not locked:</p><pre><code class="bash"># Sign previous commit
git commit --amend -S --no-edit

# Force push with lease
git push --force-with-lease
</code></pre><p>If GPG is locked, defer signing or see <code>SETUP-GPG.md</code> for troubleshooting.</p><h2>Post-Creation Steps</h2><h3>Create Release</h3><pre><code class="bash">git tag -a v1.0.0 -m &quot;Initial release&quot;
git push origin v1.0.0

# Or use GitHub CLI
gh release create v1.0.0 \
  --title &quot;v1.0.0: Initial Release&quot; \
  --notes-file CHANGELOG.md
</code></pre><h3>Clone Verification</h3><p>Test cloning from another location:</p><pre><code class="bash">cd /tmp
git clone https://codeberg.org/kae3g/12025-10-04.git
cd 12025-10-04
bb bootstrap
bb doctor
</code></pre><h2>Troubleshooting</h2><p><strong>Authentication fails:</strong><pre><code class="bash">gh auth logout
gh auth login
</code></pre></p><p><strong>Remote already exists:</strong><pre><code class="bash">git remote remove origin
git remote add origin https://codeberg.org/kae3g/12025-10-04.git
</code></pre></p><p><strong>Push rejected:</strong><pre><code class="bash">git pull origin main --rebase
git push origin main
</code></pre></p><h2></h2><p><strong>Navigation:</strong></p><ul><li><a href='DEPLOYMENT.md'>← Back: Deployment</a></li><li><a href='FINAL-SUMMARY.md'>Next: Final Summary →</a></li><li><a href='../00-START-HERE.md'>Start Here</a></li><li><a href='../../README.md'>Home: README</a></li></ul>`}
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