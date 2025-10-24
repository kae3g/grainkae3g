<script>
  export let number = 0;
  export let title = 'Babashka Architecture: Pure bb Orchestration';
</script>

<article class="robotic-farm-doc">
  <header class="doc-header">
    <span class="doc-number">
      {number.toString().padStart(7,'0')}
    </span>
    <h1 class="doc-title">{title}</h1>
    <div class="consciousness-badges">
      <span class="badge">🌙 Sacred Technology</span>
    </div>
  </header>

  <section class="sacred-quotes">
    <blockquote class="sacred">Whatever you do, work at it with all your heart.</blockquote>
  </section>

  <main class="doc-content">
    {@html `<h1>Babashka Architecture: Pure bb Orchestration</h1><p><em>How bb manages the entire development lifecycle</em></p><h2>🌙 Philosophy: Babashka as Universal Orchestrator</h2><p>This project demonstrates that **Babashka can orchestrate everything** - from environment setup through deployment:</p><ul><li>✅ <strong>No shell scripts required</strong></li><li>✅ <strong>bb orchestrates Nix</strong></li><li>✅ <strong>bb manages Git/GitHub</strong></li><li>✅ <strong>bb coordinates build pipeline</strong></li><li>✅ <strong>bb runs development servers</strong></li><li>✅ <strong>bb handles CI/CD</strong></li></ul><h2>🤖 bb.edn Structure</h2><p>Our <code>bb.edn</code> is organized into logical task groups:</p><h3>1. Health & Diagnostics</h3><pre><code class="clojure">doctor      # Check all tools available
bootstrap   # Verify prerequisites
</code></pre><h3>2. Core Pipeline Tasks</h3><pre><code class="clojure">wrap:markdown     # 57-char hard wrap
parse:markdown    # MD → ClojureScript DSL  
validate:dsl      # Spec validation
generate:svelte   # DSL → Svelte components
build:pipeline    # Complete flow
</code></pre><h3>3. Development Tasks</h3><pre><code class="clojure">serve:dev    # Local development server
build:site   # Production static build
clean        # Clean artifacts
test:all     # Run tests
ci:verify    # CI verification
</code></pre><h3>4. Nix Orchestration</h3><pre><code class="clojure">nix:check     # Verify Nix available
nix:develop   # Instructions for Nix shell
nix:run       # Execute bb task in Nix context
</code></pre><h3>5. Git/GitHub Integration</h3><pre><code class="clojure">gh:check       # Verify gh CLI auth
gh:create-repo # Create repo &amp; push
gh:status      # Check repo state
</code></pre><h2>🔄 How bb Orchestrates Nix</h2><h3>Traditional Approach</h3><pre><code class="bash"># User manually enters Nix shell
nix develop
# Then runs bb commands
bb build:pipeline
</code></pre><h3>bb-Orchestrated Approach</h3><pre><code class="bash"># bb automatically uses Nix context
bb nix:run build:pipeline
# Equivalent to: nix develop --command bb build:pipeline
</code></pre><p>Implementation in <code>bb.edn</code>:<pre><code class="clojure">nix:run {:doc &quot;Run command in Nix shell context&quot;
         :task &#40;if-let &#91;cmd &#40;first &#42;command-line-args&#42;&#41;&#93;
                 &#40;shell &quot;nix&quot; &quot;develop&quot; &quot;--command&quot; &quot;bb&quot; cmd&#41;
                 &#40;println &quot;Usage: bb nix:run &lt;bb-task-name&gt;&quot;&#41;&#41;}
</code></pre></p><h2>🌾 Nix Package Management via bb</h2><h3>bb.edn Knows About Nix Packages</h3><p>The <code>doctor</code> task checks for Nix-provided tools:</p><pre><code class="clojure">doctor {:doc &quot;Check toolchain availability&quot;
        :task &#40;do &#40;println &quot;🌙 Sacred Technology Toolchain&quot;&#41;
                  &#40;shell &quot;bash&quot; &quot;-lc&quot; &quot;node --version&quot;&#41;
                  &#40;shell &quot;bash&quot; &quot;-lc&quot; &quot;clj-kondo --version&quot;&#41;
                  &#40;shell &quot;bash&quot; &quot;-lc&quot; &quot;zprint --version&quot;&#41;
                  &#40;shell &quot;bash&quot; &quot;-lc&quot; &quot;npx vite --version&quot;&#41;&#41;}
</code></pre><h3>flake.nix Defines Packages</h3><pre><code class="nix">{
  devShells.default = pkgs.mkShell {
    buildInputs = &#91;
      pkgs.babashka     # bb itself
      pkgs.clojure      # Clojure toolchain
      pkgs.nodejs&#95;20    # Node.js runtime
      pkgs.clj-kondo    # Linting
      pkgs.zprint       # Formatting
      # ... more packages
    &#93;;
  };
}
</code></pre><h3>bb Verifies Nix Packages</h3><pre><code class="bash"># Check if Nix provides required tools
bb nix:check

# bb doctor works both inside and outside Nix shell
# - Outside: warns about missing tools
# - Inside: confirms all tools available
bb doctor
</code></pre><h2>📦 bb as Build System</h2><h3>Traditional Build Systems</h3><ul><li>Make (imperative, platform-specific)</li><li>Gradle (JVM-heavy, slow startup)</li><li>npm scripts (limited, shell-dependent)</li><li>Bash scripts (platform-specific, error-prone)</li></ul><h3>bb Advantages</h3><ul><li>✅ Fast startup (<100ms)</li><li>✅ Cross-platform (JVM, but instant)</li><li>✅ Rich Clojure ecosystem</li><li>✅ Built-in concurrency</li><li>✅ Shell process management</li><li>✅ File system operations</li><li>✅ Data transformation (EDN native)</li></ul><h2>🔧 bb Script Examples</h2><h3>bootstrap.bb</h3><pre><code class="clojure">#!/usr/bin/env bb
&#40;require '&#91;babashka.process :refer &#91;shell&#93;&#93;&#41;

&#40;defn command-exists? &#91;cmd&#93;
  &#40;try
    &#40;= 0 &#40;:exit &#40;shell {:out :string :continue true}
                       &quot;which&quot; cmd&#41;&#41;&#41;
    &#40;catch Exception &#95; false&#41;&#41;&#41;

&#40;let &#91;has-nix &#40;command-exists? &quot;nix&quot;&#41;&#93;
  &#40;println &#40;if has-nix &quot;✅&quot; &quot;❌&quot;&#41; &quot;Nix&quot;&#41;&#41;
</code></pre><h3>setup.bb</h3><pre><code class="clojure">#!/usr/bin/env bb
&#40;require '&#91;babashka.fs :as fs&#93;&#41;

&#40;fs/create-dirs &quot;build&quot;&#41;
&#40;fs/create-dirs &quot;docs&quot;&#41;
&#40;println &quot;✨ Directories ready&quot;&#41;
</code></pre><h2>🌙 bb Task Dependencies</h2><p>Tasks can depend on other tasks:</p><pre><code class="clojure">build:pipeline {:depends &#91;wrap:markdown 
                          parse:markdown 
                          validate:dsl 
                          generate:svelte&#93;}

ci:verify {:depends &#91;doctor 
                     wrap:markdown 
                     parse:markdown 
                     validate:dsl&#93;}
</code></pre><p>bb automatically:</p><ol><li>Resolves dependency graph</li><li>Executes in correct order</li><li>Stops on first failure</li><li>Reports which task failed</li></ol><h2>🚀 bb for CI/CD</h2><h3>GitHub Actions Integration</h3><pre><code class="yaml">- name: Run bb pipeline
  run: |
    nix develop --command bb ci:verify
    nix develop --command bb build:pipeline
</code></pre><h3>bb manages entire CI flow</h3><pre><code class="bash">bb ci:verify  # Runs comprehensive checks
# Executes:
# 1. bb doctor &#40;check tools&#41;
# 2. bb wrap:markdown &#40;verify wrapping&#41;
# 3. bb parse:markdown &#40;verify parsing&#41;
# 4. bb validate:dsl &#40;verify specs&#41;
</code></pre><h2>📊 bb Task Organization Best Practices</h2><h3>1. Group Related Tasks</h3><pre><code class="clojure">;; Development tasks
serve:dev, build:site, clean

;; Pipeline tasks
wrap:markdown, parse:markdown, validate:dsl

;; Infrastructure tasks
nix:check, gh:create-repo, bootstrap
</code></pre><h3>2. Use Descriptive Names</h3><pre><code class="clojure">✅ parse:markdown      # Clear what it does
❌ parse               # Too generic
✅ gh:create-repo      # Namespace prefix
❌ create-repo         # Unclear tool
</code></pre><h3>3. Provide Documentation</h3><pre><code class="clojure">{:doc &quot;Human-readable description&quot;}
</code></pre><h3>4. Handle Errors Gracefully</h3><pre><code class="clojure">{:task &#40;shell {:continue true} &quot;command&quot;&#41;
       # Don't fail if command not found
</code></pre><h2>🎯 Advanced bb Patterns</h2><h3>Conditional Execution</h3><pre><code class="clojure">{:task &#40;if &#40;fs/exists? &quot;build&quot;&#41;
         &#40;println &quot;Build dir exists&quot;&#41;
         &#40;fs/create-dirs &quot;build&quot;&#41;&#41;}
</code></pre><h3>Environment Variables</h3><pre><code class="clojure">{:task &#40;let &#91;docs-path &#40;or &#40;System/getenv &quot;DOCS&#95;PATH&quot;&#41;
                           &quot;docs&quot;&#41;&#93;
         &#40;println &quot;Using docs:&quot; docs-path&#41;&#41;}
</code></pre><h3>Command-Line Arguments</h3><pre><code class="clojure">{:task &#40;if-let &#91;cmd &#40;first &#42;command-line-args&#42;&#41;&#93;
         &#40;shell &quot;bb&quot; cmd&#41;
         &#40;println &quot;No command provided&quot;&#41;&#41;}
</code></pre><h3>Multi-Step Tasks</h3><pre><code class="clojure">{:task &#40;do
         &#40;println &quot;Step 1&quot;&#41;
         &#40;shell &quot;command1&quot;&#41;
         &#40;println &quot;Step 2&quot;&#41;  
         &#40;shell &quot;command2&quot;&#41;
         &#40;println &quot;Complete&quot;&#41;&#41;}
</code></pre><h2>🌾 bb vs Shell Scripts: Comparison</h2><table><thead><tr><th>Feature</th><th>Shell Scripts</th><th>Babashka</th></tr></thead><tbody><tr><td>Startup</td><td>Fast</td><td>Very Fast (<100ms)</td></tr><tr><td>Portability</td><td>Poor (bash/zsh)</td><td>Excellent (JVM)</td></tr><tr><td>Error Handling</td><td>Manual</td><td>Built-in</td></tr><tr><td>Data Structures</td><td>Strings only</td><td>Rich (EDN, maps, vectors)</td></tr><tr><td>Dependencies</td><td>None built-in</td><td>Maven deps</td></tr><tr><td>Testing</td><td>Difficult</td><td>Easy (Clojure testing)</td></tr><tr><td>IDE Support</td><td>Limited</td><td>Excellent</td></tr><tr><td>Type Safety</td><td>None</td><td>Runtime + spec</td></tr></tbody></table><h2>🔍 Debugging bb Tasks</h2><h3>Verbose Output</h3><pre><code class="bash">bb -v task-name  # Verbose mode
bb -vv task-name # Very verbose
</code></pre><h3>Task Listing</h3><pre><code class="bash">bb tasks          # List all tasks
bb tasks | grep nix  # Find Nix tasks
</code></pre><h3>Direct Execution</h3><pre><code class="bash">bb -e '&#40;println &quot;Hello from bb&quot;&#41;'  # REPL
bb script.clj     # Run script
./setup.bb        # Run bb script directly
</code></pre><h2>🙏 Sacred bb Philosophy</h2><p><em>"Whatever you do, work at it with all your heart."</em></p><p>bb embodies:</p><ul><li><strong>Simplicity</strong>: One tool to rule them all</li><li><strong>Transparency</strong>: Pure Clojure, readable</li><li><strong>Composability</strong>: Tasks build on tasks</li><li><strong>Reproducibility</strong>: With Nix integration</li><li><strong>Consciousness</strong>: Code as contemplative practice</li></ul><h2>🤖 Key Insight: bb as Meta-Build-System</h2><p>bb doesn't just build code - it <strong>orchestrates tools</strong>:</p><pre><code>bb
├── Manages Nix &#40;environment&#41;
├── Coordinates Git &#40;version control&#41;
├── Drives GitHub CLI &#40;deployment&#41;
├── Runs Node.js &#40;Svelte build&#41;
├── Executes clj-kondo &#40;linting&#41;
└── Compiles ClojureScript &#40;transformation&#41;
</code></pre><p>This makes bb the <strong>universal orchestrator</strong> for consciousness development with Divine Grace.<h2></h2></p><p><strong>Orchestrate</strong> <strong>with</strong> <strong>consciousness</strong>. 🤖🌙🌾</p>`}
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