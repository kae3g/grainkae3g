# Babashka Architecture: Pure bb Orchestration

*How bb manages the entire development lifecycle*

## 🌙 Philosophy: Babashka as Universal Orchestrator

This project demonstrates that **Babashka can orchestrate
everything** - from environment setup through deployment:

- ✅ **No shell scripts required**
- ✅ **bb orchestrates Nix**
- ✅ **bb manages Git/GitHub**
- ✅ **bb coordinates build pipeline**
- ✅ **bb runs development servers**
- ✅ **bb handles CI/CD**

## 🤖 bb.edn Structure

Our `bb.edn` is organized into logical task groups:

### 1. Health & Diagnostics
```clojure
doctor      # Check all tools available
bootstrap   # Verify prerequisites
```

### 2. Core Pipeline Tasks
```clojure
wrap:markdown     # 57-char hard wrap
parse:markdown    # MD → ClojureScript DSL  
validate:dsl      # Spec validation
generate:svelte   # DSL → Svelte components
build:pipeline    # Complete flow
```

### 3. Development Tasks
```clojure
serve:dev    # Local development server
build:site   # Production static build
clean        # Clean artifacts
test:all     # Run tests
ci:verify    # CI verification
```

### 4. Nix Orchestration
```clojure
nix:check     # Verify Nix available
nix:develop   # Instructions for Nix shell
nix:run       # Execute bb task in Nix context
```

### 5. Git/GitHub Integration
```clojure
gh:check       # Verify gh CLI auth
gh:create-repo # Create repo & push
gh:status      # Check repo state
```

## 🔄 How bb Orchestrates Nix

### Traditional Approach
```bash
# User manually enters Nix shell
nix develop
# Then runs bb commands
bb build:pipeline
```

### bb-Orchestrated Approach
```bash
# bb automatically uses Nix context
bb nix:run build:pipeline
# Equivalent to: nix develop --command bb build:pipeline
```

Implementation in `bb.edn`:
```clojure
nix:run {:doc "Run command in Nix shell context"
         :task (if-let [cmd (first *command-line-args*)]
                 (shell "nix" "develop" "--command" "bb" cmd)
                 (println "Usage: bb nix:run <bb-task-name>"))}
```

## 🌾 Nix Package Management via bb

### bb.edn Knows About Nix Packages

The `doctor` task checks for Nix-provided tools:

```clojure
doctor {:doc "Check toolchain availability"
        :task (do (println "🌙 Sacred Technology Toolchain")
                  (shell "bash" "-lc" "node --version")
                  (shell "bash" "-lc" "clj-kondo --version")
                  (shell "bash" "-lc" "zprint --version")
                  (shell "bash" "-lc" "npx vite --version"))}
```

### flake.nix Defines Packages

```nix
{
  devShells.default = pkgs.mkShell {
    buildInputs = [
      pkgs.babashka     # bb itself
      pkgs.clojure      # Clojure toolchain
      pkgs.nodejs_20    # Node.js runtime
      pkgs.clj-kondo    # Linting
      pkgs.zprint       # Formatting
      # ... more packages
    ];
  };
}
```

### bb Verifies Nix Packages

```bash
# Check if Nix provides required tools
bb nix:check

# bb doctor works both inside and outside Nix shell
# - Outside: warns about missing tools
# - Inside: confirms all tools available
bb doctor
```

## 📦 bb as Build System

### Traditional Build Systems
- Make (imperative, platform-specific)
- Gradle (JVM-heavy, slow startup)
- npm scripts (limited, shell-dependent)
- Bash scripts (platform-specific, error-prone)

### bb Advantages
- ✅ Fast startup (<100ms)
- ✅ Cross-platform (JVM, but instant)
- ✅ Rich Clojure ecosystem
- ✅ Built-in concurrency
- ✅ Shell process management
- ✅ File system operations
- ✅ Data transformation (EDN native)

## 🔧 bb Script Examples

### bootstrap.bb
```clojure
#!/usr/bin/env bb
(require '[babashka.process :refer [shell]])

(defn command-exists? [cmd]
  (try
    (= 0 (:exit (shell {:out :string :continue true}
                       "which" cmd)))
    (catch Exception _ false)))

(let [has-nix (command-exists? "nix")]
  (println (if has-nix "✅" "❌") "Nix"))
```

### setup.bb
```clojure
#!/usr/bin/env bb
(require '[babashka.fs :as fs])

(fs/create-dirs "build")
(fs/create-dirs "docs")
(println "✨ Directories ready")
```

## 🌙 bb Task Dependencies

Tasks can depend on other tasks:

```clojure
build:pipeline {:depends [wrap:markdown 
                          parse:markdown 
                          validate:dsl 
                          generate:svelte]}

ci:verify {:depends [doctor 
                     wrap:markdown 
                     parse:markdown 
                     validate:dsl]}
```

bb automatically:
1. Resolves dependency graph
2. Executes in correct order
3. Stops on first failure
4. Reports which task failed

## 🚀 bb for CI/CD

### GitHub Actions Integration

```yaml
- name: Run bb pipeline
  run: |
    nix develop --command bb ci:verify
    nix develop --command bb build:pipeline
```

### bb manages entire CI flow

```bash
bb ci:verify  # Runs comprehensive checks
# Executes:
# 1. bb doctor (check tools)
# 2. bb wrap:markdown (verify wrapping)
# 3. bb parse:markdown (verify parsing)
# 4. bb validate:dsl (verify specs)
```

## 📊 bb Task Organization Best Practices

### 1. Group Related Tasks
```clojure
;; Development tasks
serve:dev, build:site, clean

;; Pipeline tasks
wrap:markdown, parse:markdown, validate:dsl

;; Infrastructure tasks
nix:check, gh:create-repo, bootstrap
```

### 2. Use Descriptive Names
```clojure
✅ parse:markdown      # Clear what it does
❌ parse               # Too generic
✅ gh:create-repo      # Namespace prefix
❌ create-repo         # Unclear tool
```

### 3. Provide Documentation
```clojure
{:doc "Human-readable description"}
```

### 4. Handle Errors Gracefully
```clojure
{:task (shell {:continue true} "command")
       # Don't fail if command not found
```

## 🎯 Advanced bb Patterns

### Conditional Execution
```clojure
{:task (if (fs/exists? "build")
         (println "Build dir exists")
         (fs/create-dirs "build"))}
```

### Environment Variables
```clojure
{:task (let [docs-path (or (System/getenv "DOCS_PATH")
                           "docs")]
         (println "Using docs:" docs-path))}
```

### Command-Line Arguments
```clojure
{:task (if-let [cmd (first *command-line-args*)]
         (shell "bb" cmd)
         (println "No command provided"))}
```

### Multi-Step Tasks
```clojure
{:task (do
         (println "Step 1")
         (shell "command1")
         (println "Step 2")  
         (shell "command2")
         (println "Complete"))}
```

## 🌾 bb vs Shell Scripts: Comparison

| Feature | Shell Scripts | Babashka |
|---------|--------------|----------|
| Startup | Fast | Very Fast (<100ms) |
| Portability | Poor (bash/zsh) | Excellent (JVM) |
| Error Handling | Manual | Built-in |
| Data Structures | Strings only | Rich (EDN, maps, vectors) |
| Dependencies | None built-in | Maven deps |
| Testing | Difficult | Easy (Clojure testing) |
| IDE Support | Limited | Excellent |
| Type Safety | None | Runtime + spec |

## 🔍 Debugging bb Tasks

### Verbose Output
```bash
bb -v task-name  # Verbose mode
bb -vv task-name # Very verbose
```

### Task Listing
```bash
bb tasks          # List all tasks
bb tasks | grep nix  # Find Nix tasks
```

### Direct Execution
```bash
bb -e '(println "Hello from bb")'  # REPL
bb script.clj     # Run script
./setup.bb        # Run bb script directly
```

## 🙏 Sacred bb Philosophy

*"Whatever you do, work at it with all your heart."*

bb embodies:
- **Simplicity**: One tool to rule them all
- **Transparency**: Pure Clojure, readable
- **Composability**: Tasks build on tasks
- **Reproducibility**: With Nix integration
- **Consciousness**: Code as contemplative practice

## 🤖 Key Insight: bb as Meta-Build-System

bb doesn't just build code - it **orchestrates tools**:

```
bb
├── Manages Nix (environment)
├── Coordinates Git (version control)
├── Drives GitHub CLI (deployment)
├── Runs Node.js (Svelte build)
├── Executes clj-kondo (linting)
└── Compiles ClojureScript (transformation)
```

This makes bb the **universal orchestrator** for
consciousness development with Divine Grace.

---

**Orchestrate** **with** **consciousness**. 🤖🌙🌾

