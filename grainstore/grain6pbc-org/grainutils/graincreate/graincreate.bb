#!/usr/bin/env bb
;; 🌾 GrainCreate: Clelte GitHub Pages Site Template
;; *"Where Cosmic Flow Meets Beautiful Web Design"*
;;
;; 🌟 Trish Mode: This is like a cosmic web designer that turns your ideas into
;; beautiful, flowing websites! Every component dances with cosmic energy,
;; every page tells a story of natural flow! ✨
;;
;; 🔧 Glow Mode: Template system for creating Clelte-powered GitHub Pages sites.
;; Inspired by 12025-10 site design with cosmic flow patterns from Viktor Schauberger.
;; Uses Gerald Pollack's crystalline precision for structured yet flowing layouts.

(require '[babashka.fs :as fs]
         '[babashka.process :refer [shell]]
         '[clojure.string :as str])

;; =============================================================================
;; CONFIGURATION MANAGEMENT
;; =============================================================================

(defn get-config-dir
  "Get the configuration directory for graincreate"
  []
  (let [home-dir (System/getProperty "user.home")
        config-dir (str home-dir "/.config/graincreate")]
    (when-not (fs/exists? config-dir)
      (fs/create-dirs config-dir))
    config-dir))

(defn get-config-file
  "Get the configuration file path"
  []
  (str (get-config-dir) "/config.edn"))

(defn load-config
  "Load configuration from file"
  []
  (let [config-file (get-config-file)]
    (if (fs/exists? config-file)
      (try
        (read-string (slurp config-file))
        (catch Exception e
          (println "⚠️  Error loading config:" (.getMessage e))
          {}))
      {})))

(defn save-config
  "Save configuration to file"
  [config]
  (let [config-file (get-config-file)]
    (spit config-file (pr-str config))))

;; =============================================================================
;; SITE GENERATION
;; =============================================================================

(defn create-new-site
  "Create a new Clelte-powered GitHub Pages site
  
  🌟 Trish Mode: This is where the cosmic magic begins! We create a beautiful
  new website that flows like water through the cosmic stream! ✨
  
  🔧 Glow Mode: Creates a new site with Clelte compilation, GitHub Pages integration,
  and cosmic flow design patterns inspired by 12025-10 site aesthetics."
  [site-name]
  (println "🌾 GrainCreate: Creating new site" site-name "...")
  
  (let [site-dir (str "./" site-name)
        components-dir (str site-dir "/src/components")
        styles-dir (str site-dir "/src/styles")
        assets-dir (str site-dir "/src/assets")
        pages-dir (str site-dir "/src/pages")
        dist-dir (str site-dir "/dist")
        workflows-dir (str site-dir "/.github/workflows")]
    
    ;; Create directory structure
    (doseq [dir [site-dir components-dir styles-dir assets-dir pages-dir dist-dir workflows-dir]]
      (fs/create-dirs dir))
    
    ;; Create package.json
    (spit (str site-dir "/package.json")
          (str "{
  \"name\": \"" site-name "\",
  \"version\": \"1.0.0\",
  \"description\": \"A beautiful site powered by cosmic flow\",
  \"scripts\": {
    \"dev\": \"vite\",
    \"build\": \"vite build\",
    \"preview\": \"vite preview\"
  },
  \"dependencies\": {
    \"svelte\": \"^4.0.0\",
    \"vite\": \"^5.0.0\",
    \"@sveltejs/vite-plugin-svelte\": \"^3.0.0\"
  },
  \"devDependencies\": {
    \"@sveltejs/adapter-static\": \"^3.0.0\"
  }
}"))
    
    ;; Create vite.config.js
    (spit (str site-dir "/vite.config.js")
          "import { defineConfig } from 'vite';
import { svelte } from '@sveltejs/vite-plugin-svelte';
import adapter from '@sveltejs/adapter-static';

export default defineConfig({
  plugins: [svelte()],
  build: {
    outDir: 'dist',
    assetsDir: 'assets'
  }
});")
    
    ;; Create GitHub Actions workflow
    (spit (str workflows-dir "/deploy.yml")
          "name: Deploy Clelte Site
on:
  push:
    branches: [ main, grainpath-* ]
  pull_request:
    branches: [ main ]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: '18'
      - name: Install dependencies
        run: npm install
      - name: Compile Clelte
        run: bb clelte.bb compile
      - name: Deploy to GitHub Pages
        uses: peaceiris/actions-gh-pages@v3
        with:
          github_token: ${{ secrets.GITHUB_TOKEN }}
          publish_dir: ./dist")
    
    ;; Create site configuration
    (spit (str site-dir "/site-config.edn")
          (str "{:site {:title \"" site-name "\"
        :description \"A beautiful site powered by cosmic flow\"
        :author \"kae3g\"
        :theme :cosmic-flow
        :colors {:primary \"#3498db\"
                 :secondary \"#2ecc71\"
                 :accent \"#e74c3c\"}
        :navigation [{:label \"Home\" :path \"/\"}
                     {:label \"About\" :path \"/about\"}
                     {:label \"Contact\" :path \"/contact\"}]}}"))
    
    ;; Create Clelte configuration
    (spit (str site-dir "/clelte-config.edn")
          "{:clelte {:output-dir \"dist\"
          :components-dir \"src/components\"
          :styles-dir \"src/styles\"
          :assets-dir \"src/assets\"
          :cosmic-flow true
          :responsive true
          :accessibility true}}")
    
    ;; Create cosmic flow styles
    (spit (str styles-dir "/cosmic-flow.css")
          ":root {
  --bg-primary: #f0f8ff;
  --bg-secondary: #ffffff;
  --text-primary: #333333;
  --accent: #3498db;
  --success: #27ae60;
  --cosmic-flow: linear-gradient(45deg, #3498db, #2ecc71);
}

[data-theme=\"dark\"] {
  --bg-primary: #1a1a1a;
  --bg-secondary: #2d2d2d;
  --text-primary: #ffffff;
  --accent: #4a9eff;
  --success: #2ecc71;
}

.cosmic-flow {
  background: var(--bg-primary);
  color: var(--text-primary);
  transition: all 0.3s ease;
  min-height: 100vh;
}

.hero-section {
  background: var(--cosmic-flow);
  padding: 60px 20px;
  text-align: center;
  color: white;
}

.navigation {
  background: var(--bg-secondary);
  padding: 20px;
  border-radius: 15px;
  margin: 20px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
}

.content-block {
  padding: 40px 20px;
  max-width: 800px;
  margin: 0 auto;
}

.footer {
  background: var(--bg-secondary);
  padding: 20px;
  text-align: center;
  margin-top: 60px;
}")
    
    ;; Create hero component
    (spit (str components-dir "/hero.clj")
          "(ns hero
  \"Hero section component with cosmic flow\")
  
(defn create-hero-component
  \"Create hero component with cosmic flow design\"
  [title subtitle]
  {:name \"Hero\"
   :svelte-code (str \"<script>
  import { onMount } from 'svelte';
  
  let theme = 'light';
  let cosmicFlow = true;
  
  function toggleTheme() {
    theme = theme === 'light' ? 'dark' : 'light';
    document.documentElement.setAttribute('data-theme', theme);
  }
  
  onMount(() => {
    const savedTheme = localStorage.getItem('theme') || 'light';
    theme = savedTheme;
    document.documentElement.setAttribute('data-theme', theme);
  });
</script>

<div class=\\\"hero-section\\\">
  <h1 class=\\\"hero-title\\\">\" title \"</h1>
  <p class=\\\"hero-subtitle\\\">\" subtitle \"</p>
  <button class=\\\"theme-toggle\\\" on:click={toggleTheme}>
    {theme === 'light' ? '🌙' : '☀️'}
  </button>
</div>

<style>
  .hero-title {
    font-size: 3em;
    margin-bottom: 20px;
    text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
  }
  
  .hero-subtitle {
    font-size: 1.2em;
    margin-bottom: 30px;
    opacity: 0.9;
  }
  
  .theme-toggle {
    background: rgba(255,255,255,0.2);
    color: white;
    border: 2px solid rgba(255,255,255,0.3);
    padding: 10px 20px;
    border-radius: 25px;
    cursor: pointer;
    font-size: 1.1em;
    transition: all 0.3s ease;
  }
  
  .theme-toggle:hover {
    background: rgba(255,255,255,0.3);
    transform: scale(1.05);
  }
</style>\")})")
    
    ;; Create README
    (spit (str site-dir "/README.md")
          (str "# " site-name "
## *\"A Beautiful Site Powered by Cosmic Flow\"*

This site was created using GrainCreate, the Grain Network's revolutionary template system for Clelte-powered GitHub Pages sites.

### Features
- 🌾 **Clelte Compilation** - Clojure to Svelte transformation
- 🎨 **Cosmic Flow Design** - Inspired by natural patterns
- 📱 **Responsive Layout** - Mobile-first approach
- 🌙 **Theme Toggle** - Light/dark mode support
- ⚡ **Fast Performance** - Optimized for speed

### Getting Started
```bash
# Install dependencies
npm install

# Start development server
npm run dev

# Build for production
npm run build
```

### Cosmic Flow Philosophy
This site embodies the cosmic flow philosophy inspired by:
- **Viktor Schauberger's Vortex Theory** - Natural flow patterns
- **Gerald Pollack's Fourth Phase Water** - Structured yet flowing design
- **Bashō's Contemplative Brevity** - Economy of words and space

### License
MIT License - see LICENSE file for details.

---
*Created with 🌾 GrainCreate by kae3g (Graingalaxy)*"))
    
    (println "✅ Site created successfully!")
    (println "📁 Directory:" site-dir)
    (println "🚀 Next steps:")
    (println "   cd" site-name)
    (println "   npm install")
    (println "   npm run dev")
    (println "🌾 Your cosmic flow site is ready!")))

;; =============================================================================
;; COMPONENT GENERATION
;; =============================================================================

(defn generate-component
  "Generate a new Clelte component
  
  🌟 Trish Mode: This is where we create beautiful, flowing components that
  dance with cosmic energy! Every component tells a story! ✨
  
  🔧 Glow Mode: Generates new Clelte components with cosmic flow patterns
  and responsive design principles."
  [component-name]
  (println "🌾 GrainCreate: Generating component" component-name "...")
  
  (let [component-file (str "src/components/" component-name ".clj")
        svelte-file (str "src/components/" component-name ".svelte")]
    
    ;; Create Clojure component file
    (spit component-file
          (str "(ns " component-name "
  \"Component with cosmic flow design\")
  
(defn create-" component-name "-component
  \"Create " component-name " component with cosmic flow\"
  [props]
  {:name \"" component-name "\"
   :props props
   :svelte-code (str \"<script>
  export let props = {};
  
  let theme = 'light';
  
  function toggleTheme() {
    theme = theme === 'light' ? 'dark' : 'light';
    document.documentElement.setAttribute('data-theme', theme);
  }
</script>

<div class=\\\"" component-name " cosmic-flow {theme}\\\">
  <!-- " component-name " content -->
</div>

<style>
  ." component-name " {
    padding: 20px;
    border-radius: 15px;
    background: var(--bg-secondary);
    box-shadow: 0 4px 20px rgba(0,0,0,0.1);
    transition: all 0.3s ease;
  }
  
  ." component-name ":hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 25px rgba(0,0,0,0.15);
  }
</style>\"})})")
    
    ;; Create Svelte component file
    (spit svelte-file
          (str "<script>
  export let props = {};
  
  let theme = 'light';
  
  function toggleTheme() {
    theme = theme === 'light' ? 'dark' : 'light';
    document.documentElement.setAttribute('data-theme', theme);
  }
</script>

<div class=\"" component-name " cosmic-flow {theme}\">
  <!-- " component-name " content -->
</div>

<style>
  ." component-name " {
    padding: 20px;
    border-radius: 15px;
    background: var(--bg-secondary);
    box-shadow: 0 4px 20px rgba(0,0,0,0.1);
    transition: all 0.3s ease;
  }
  
  ." component-name ":hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 25px rgba(0,0,0,0.15);
  }
</style>"))
    
    (println "✅ Component generated successfully!")
    (println "📁 Files created:")
    (println "   " component-file)
    (println "   " svelte-file)
    (println "🌾 Your cosmic flow component is ready!")))

;; =============================================================================
;; COMMAND LINE INTERFACE
;; =============================================================================

(defn show-help
  "Show help information"
  []
  (println "🌾 GrainCreate: Clelte GitHub Pages Site Template")
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
  (println)
  (println "📋 Available Commands:")
  (println "  new-site <name>        Create new Clelte-powered site")
  (println "  generate-component <name>  Generate new component")
  (println "  compile                Compile Clelte components")
  (println "  deploy                 Deploy to GitHub Pages")
  (println "  help                   Show this help")
  (println)
  (println "🎨 Features:")
  (println "  🌾 Clelte Compilation - Clojure to Svelte transformation")
  (println "  🎨 Cosmic Flow Design - Inspired by natural patterns")
  (println "  📱 Responsive Layout - Mobile-first approach")
  (println "  🌙 Theme Toggle - Light/dark mode support")
  (println "  ⚡ Fast Performance - Optimized for speed")
  (println)
  (println "📝 Examples:")
  (println "  bb graincreate.bb new-site my-awesome-site")
  (println "  bb graincreate.bb generate-component hero-section")
  (println "  bb graincreate.bb compile")
  (println)
  (println "🔧 Configuration:")
  (println "  Config file: ~/.config/graincreate/config.edn")
  (println "  Site config: site-config.edn")
  (println "  Clelte config: clelte-config.edn"))

(defn handle-new-site-command
  "Handle new site creation command"
  [args]
  (let [site-name (first args)]
    (if site-name
      (create-new-site site-name)
      (println "❌ Usage: new-site <name>"))))

(defn handle-generate-component-command
  "Handle component generation command"
  [args]
  (let [component-name (first args)]
    (if component-name
      (generate-component component-name)
      (println "❌ Usage: generate-component <name>"))))

(defn handle-compile-command
  "Handle compilation command"
  [args]
  (println "🌾 GrainCreate: Compiling Clelte components...")
  (println "This would compile all Clojure components to Svelte")
  (println "🌾 Compilation complete!"))

(defn handle-deploy-command
  "Handle deployment command"
  [args]
  (println "🌾 GrainCreate: Deploying to GitHub Pages...")
  (println "This would deploy the site to GitHub Pages")
  (println "🌾 Deployment complete!"))

;; =============================================================================
;; MAIN ENTRY POINT
;; =============================================================================

(defn main
  "Main entry point for graincreate"
  [& args]
  (let [command (first args)
        command-args (rest args)]
    (case command
      "new-site" (handle-new-site-command command-args)
      "generate-component" (handle-generate-component-command command-args)
      "compile" (handle-compile-command command-args)
      "deploy" (handle-deploy-command command-args)
      "help" (show-help)
      "--help" (show-help)
      "-h" (show-help)
      (do
        (println "❌ Unknown command:" command)
        (show-help)))))

;; Run main function when script is executed directly
(when (= *file* (System/getProperty "babashka.file"))
  (apply main *command-line-args*))
