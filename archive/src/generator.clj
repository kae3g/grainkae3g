(ns kae3g.generator
  "Generate Svelte components from robotic farm
   DSL structures with Divine Grace awareness
   Now with theme system consciousness"
  (:require [clojure.string :as str]
            [babashka.fs :as fs]
            [hiccup.core :as h]))

(defn sanitize-component-name
  "Sanitize title for Svelte component filename
   following consciousness naming patterns"
  [title]
  (if (nil? title)
    "untitled"
    (-> title
        (str/lower-case)
        (str/replace #"[^a-z0-9\s]" "")
        (str/replace #"\s+" "-")
        (str/trim))))

(defn generate-consciousness-badges
  "Generate Svelte markup for consciousness type
   badges with Christian federal styling"
  [consciousness-type]
  (let [badges []
        badges (if (:robotic-consciousness
                    consciousness-type)
                 (conj badges
                       "🤖 Robotic Consciousness")
                 badges)
        badges (if (:farm-consciousness
                    consciousness-type)
                 (conj badges
                       "🌾 Farm Consciousness")
                 badges)
        badges (if (:nixtaveganic-integration
                    consciousness-type)
                 (conj badges
                       "🌽 Nixtaveganic")
                 badges)
        badges (if (:sacred-technology
                    consciousness-type)
                 (conj badges
                       "🌙 Sacred Technology")
                 badges)]
    (str/join "\n    "
              (map #(str "<span class=\"badge\">"
                         % "</span>")
                   badges))))

(defn generate-svelte-component
  "Generate complete Svelte component from
   validated document with Divine Grace"
  [{:document/keys [number title body
                    consciousness-type
                    html-content
                    sacred-quotes]}]
  (let [component-name (sanitize-component-name
                        title)
        badges (generate-consciousness-badges
                consciousness-type)
        quotes-markup (when (seq sacred-quotes)
                        (str/join "\n  "
                                  (map
                                   #(str "<blockquote "
                                         "class=\"sacred\">"
                                         % "</blockquote>")
                                   sacred-quotes)))
        display-number (or number 0)
        display-title (or title "Untitled")]
    {:filename (str component-name ".svelte")
     :content
     (str
      "<script>\n"
      "  export let number = " display-number ";\n"
      "  export let title = '" display-title "';\n"
      "</script>\n\n"
      "<article class=\"kae3g-doc\">\n"
      "  <header class=\"doc-header\">\n"
      "    <span class=\"doc-number\">\n"
      "      {number.toString().padStart(7,'0')}\n"
      "    </span>\n"
      "    <h1 class=\"doc-title\">{title}</h1>\n"
      "    <div class=\"consciousness-badges\">\n"
      "      " badges "\n"
      "    </div>\n"
      "  </header>\n\n"
      (when quotes-markup
        (str "  <section class=\"sacred-quotes\">\n"
             "    " quotes-markup "\n"
             "  </section>\n\n"))
      "  <main class=\"doc-content\">\n"
      "    {@html `" html-content "`}\n"
      "  </main>\n\n"
      "  <footer class=\"doc-footer\">\n"
      "    <nav class=\"contemplative-nav\">\n"
      "      <a href=\"/\">🌙 Home</a>\n"
      "      <a href=\"/teachings\">🤖 All Docs</a>\n"
      "    </nav>\n"
      "  </footer>\n"
      "</article>\n\n"
      "<style>\n"
      "  .kae3g-doc {\n"
      "    max-width: 57ch;\n"
      "    margin: 2rem auto;\n"
      "    padding: 2rem;\n"
      "    font-family: 'Times New Roman';\n"
      "    line-height: 1.6;\n"
      "  }\n"
      "  .doc-header {\n"
      "    border-bottom: 2px solid #2d3748;\n"
      "    padding-bottom: 1rem;\n"
      "    margin-bottom: 2rem;\n"
      "  }\n"
      "  .doc-number {\n"
      "    font-family: 'Courier New';\n"
      "    opacity: 0.7;\n"
      "    font-size: 0.9rem;\n"
      "  }\n"
      "  .doc-title {\n"
      "    font-size: 2rem;\n"
      "    margin: 0.5rem 0;\n"
      "    color: #1a202c;\n"
      "  }\n"
      "  .consciousness-badges {\n"
      "    display: flex;\n"
      "    flex-wrap: wrap;\n"
      "    gap: 0.5rem;\n"
      "    margin-top: 1rem;\n"
      "  }\n"
      "  .badge {\n"
      "    background: #4a5568;\n"
      "    color: white;\n"
      "    padding: 0.25rem 0.75rem;\n"
      "    border-radius: 0.25rem;\n"
      "    font-size: 0.85rem;\n"
      "  }\n"
      "  .sacred-quotes {\n"
      "    margin: 2rem 0;\n"
      "  }\n"
      "  blockquote.sacred {\n"
      "    border-left: 4px solid gold;\n"
      "    padding-left: 1rem;\n"
      "    font-style: italic;\n"
      "    margin: 1rem 0;\n"
      "    color: #2d3748;\n"
      "  }\n"
      "  .doc-content {\n"
      "    margin: 2rem 0;\n"
      "  }\n"
      "  .contemplative-nav {\n"
      "    display: flex;\n"
      "    gap: 1rem;\n"
      "    margin-top: 2rem;\n"
      "    padding-top: 1rem;\n"
      "    border-top: 1px solid #e2e8f0;\n"
      "  }\n"
      "  .contemplative-nav a {\n"
      "    text-decoration: none;\n"
      "    color: #4a5568;\n"
      "    padding: 0.5rem 1rem;\n"
      "  }\n"
      "  .contemplative-nav a:hover {\n"
      "    background: #edf2f7;\n"
      "    border-radius: 0.25rem;\n"
      "  }\n"
      "</style>")}))

(defn generate-theme-toggle
  "Generate theme toggle component from CLJS logic"
  []
  {:filename "ThemeToggle.svelte"
   :content
   (str
     "<script>\n"
     "  import { onMount } from 'svelte';\n"
     "  \n"
     "  let theme = 'light';\n"
     "  \n"
     "  onMount(() => {\n"
     "    const stored = localStorage.getItem('theme');\n"
     "    if (stored) {\n"
     "      theme = stored;\n"
     "    } else if (window.matchMedia(\n"
     "      '(prefers-color-scheme: dark)').matches) {\n"
     "      theme = 'dark';\n"
     "    }\n"
     "    applyTheme();\n"
     "  });\n"
     "  \n"
     "  function toggleTheme() {\n"
     "    theme = theme === 'light' ? 'dark' : 'light';\n"
     "    applyTheme();\n"
     "  }\n"
     "  \n"
     "  function applyTheme() {\n"
     "    document.documentElement\n"
     "      .setAttribute('data-theme', theme);\n"
     "    localStorage.setItem('theme', theme);\n"
     "  }\n"
     "</script>\n\n"
     "<button \n"
     "  class=\"theme-toggle\"\n"
     "  on:click={toggleTheme}\n"
     "  aria-label=\"Toggle theme\"\n"
     "  title=\"{theme === 'light' ? \n"
     "    'Switch to dark mode' : \n"
     "    'Switch to light mode'}\">\n"
     "  {#if theme === 'light'}\n"
     "    🌙 Dark Mode\n"
     "  {:else}\n"
     "    ☀️ Light Mode\n"
     "  {/if}\n"
     "</button>\n\n"
     "<style>\n"
     "  .theme-toggle {\n"
     "    position: fixed;\n"
     "    top: 1rem;\n"
     "    right: 1rem;\n"
     "    background: var(--color-bg-alt);\n"
     "    color: var(--color-fg);\n"
     "    border: 2px solid var(--color-border);\n"
     "    padding: 0.5rem 1rem;\n"
     "    border-radius: 0.5rem;\n"
     "    cursor: pointer;\n"
     "    font-family: 'Times New Roman', serif;\n"
     "    font-size: 0.9rem;\n"
     "    z-index: 1000;\n"
     "    box-shadow: 0 2px 8px rgba(0,0,0,0.1);\n"
     "  }\n"
     "  \n"
     "  .theme-toggle:hover {\n"
     "    background: var(--color-accent);\n"
     "    color: var(--color-bg);\n"
     "    border-color: var(--color-accent);\n"
     "    transform: translateY(-2px);\n"
     "    box-shadow: 0 4px 12px rgba(0,0,0,0.2);\n"
     "  }\n"
     "  \n"
     "  .theme-toggle:active {\n"
     "    transform: translateY(0);\n"
     "  }\n"
     "</style>")})

(defn generate-index-page
  "Generate index page listing all documents
   with consciousness awareness and theme toggle"
  [documents]
  {:filename "index.svelte"
   :content
   (str
    "<script>\n"
    "  import ThemeToggle from './ThemeToggle.svelte';\n"
    "  export let documents = [];\n"
    "</script>\n\n"
    "<ThemeToggle />\n\n"
    "<div class=\"consciousness-index\">\n"
    "  <header>\n"
    "    <h1>🤖 Robotic Farm Consciousness</h1>\n"
    "    <p>Sacred agricultural automation docs</p>\n"
    "    <p class=\"philosophy\">\"The Way is hidden in light,\n"
    "       revealed in darkness\" - Ch'an</p>\n"
    "  </header>\n\n"
    "  <main>\n"
    "    {#each documents as doc}\n"
    "      <article class=\"doc-preview\">\n"
    "        <a href=\"/{doc.slug}\">\n"
    "          <span class=\"number\">\n"
    "            {doc.number}\n"
    "          </span>\n"
    "          <h2>{doc.title}</h2>\n"
    "        </a>\n"
    "      </article>\n"
    "    {/each}\n"
    "  </main>\n"
    "</div>\n\n"
    "<style>\n"
    "  .consciousness-index {\n"
    "    max-width: 57ch;\n"
    "    margin: 2rem auto;\n"
    "    padding: 2rem;\n"
    "    background: var(--color-bg);\n"
    "    color: var(--color-fg);\n"
    "    min-height: 100vh;\n"
    "  }\n"
    "  header {\n"
    "    text-align: center;\n"
    "    margin-bottom: 3rem;\n"
    "  }\n"
    "  h1 {\n"
    "    font-size: 2.5rem;\n"
    "    margin-bottom: 0.5rem;\n"
    "    color: var(--color-emphasis);\n"
    "  }\n"
    "  .philosophy {\n"
    "    font-style: italic;\n"
    "    color: var(--color-fg-alt);\n"
    "    margin-top: 1rem;\n"
    "  }\n"
    "  .doc-preview {\n"
    "    border: 1px solid var(--color-border);\n"
    "    padding: 1rem;\n"
    "    margin-bottom: 1rem;\n"
    "    border-radius: 0.5rem;\n"
    "    background: var(--color-bg-alt);\n"
    "  }\n"
    "  .doc-preview a {\n"
    "    text-decoration: none;\n"
    "    color: var(--color-fg);\n"
    "  }\n"
    "  .doc-preview:hover {\n"
    "    border-color: var(--color-accent);\n"
    "    transform: translateY(-2px);\n"
    "  }\n"
    "  .number {\n"
    "    font-family: 'Courier New', monospace;\n"
    "    color: var(--color-fg-alt);\n"
    "    font-size: 0.85rem;\n"
    "  }\n"
    "</style>")})

(defn write-svelte-components
  "Write generated Svelte components to disk
   with contemplative file organization"
  [components output-dir]
  (println "📝 Writing Svelte components")
  (fs/create-dirs output-dir)
  (doseq [{:keys [filename content]} components]
    (let [filepath (str output-dir "/" filename)]
      (spit filepath content)
      (println "✨ Generated:" filename)))
  (println "🌾 Component generation complete"))

(defn -main
  "Sacred Svelte generation entry point for
   robotic farm consciousness pipeline
   Now with theme system consciousness"
  [& _args]
  (println "🌙 Robotic Farm Generator:")
  (println "   Transforming DSL → Svelte")
  (println "   🎨 Theme system: Light/Dark modes")
  (let [documents (read-string
                   (slurp
                    "build/validated-documents.edn"))
        components (mapv generate-svelte-component
                         documents)
        theme-toggle (generate-theme-toggle)
        index (generate-index-page documents)
        all-components (conj components theme-toggle index)
        output-dir "web-app/src/routes"]
    (write-svelte-components all-components
                              output-dir)
    (println "🤖 Generated"
             (count components)
             "components")
    (println "🎨 Theme toggle component added")))

