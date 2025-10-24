# Website Expansion Plan: ClojureScript + Svelte Integration

**Last Updated:** October 10, 2025  
**Status:** Planning Phase  
**Goal:** Enhance valley website with interactive features using ClojureScript-Svelte bindings

**🌙 New Dimension:** Integrate Islamic Golden Age wisdom features (House of Wisdom knowledge graph, translation chains, historical timeline)

**🌱 Plant-Based Lens:** Gardens, not dashboards. Growth visualization, not metrics. Nourishment, not consumption.

---

## Current Architecture

### What We Have
```clojure
{:pipeline
 {:source "writings/*.md (Markdown + YAML frontmatter)"
  :processing "scripts/writings_build.clj (Clojure, Babashka)"
  :output "web-app/static/content/*.json"
  :frontend "SvelteKit (SSG, GitHub Pages)"
  :theme "src/lib/theme.css (CSS variables)"
  :deployment "GitHub Actions → GitHub Pages"}}
```

**Strengths:**
- ✅ Simple, functional pipeline (Markdown → JSON → Static HTML)
- ✅ All content version-controlled
- ✅ Fast, static, SEO-friendly
- ✅ Works perfectly offline (no server needed)
- ✅ Clean separation: Clojure processes, Svelte presents

---

## Vision: ClojureScript-Svelte Hybrid Architecture

### The Opportunity

We can add **dynamic, interactive features** while keeping our static-first approach using:

1. **ClojureScript in the browser** — Functional logic, immutable state management
2. **Svelte for reactive UI** — Minimal runtime, compiled reactivity
3. **Shared EDN data format** — Same data structures from pipeline to browser
4. **🌙 Historical wisdom features** — Timeline (Greek → Islamic → Modern), knowledge graphs, translation chains
5. **🌱 Plant-based visualization** — Growth rings, root systems, seed networks (not charts/graphs)

### Architecture Diagram

```
Markdown (writings/) 
    ↓
Clojure Build (Babashka)
    ↓
EDN + JSON (intermediate data)
    ↓
    ├→ Static HTML (SvelteKit SSG) [Current: Works Great]
    └→ ClojureScript Modules (Browser) [New: Interactive Features]
         ↓
    Svelte Components (Reactive UI) [New: Dynamic UX]
```

---

## Phase 1: Interactive Expedition Map (Medium Priority)

**Goal:** Transform `valley-expedition-map.md` into interactive, clickable journey with progress tracking.

### Features
- **Interactive SVG Map** — Click regions to explore (Foundation, Deep Knowledge, Tools)
- **Progress Tracking** — LocalStorage persists which essays you've read
- **Character Popups** — Hover over regions to see character bios
- **Path Highlighting** — Show your chosen learning path (Visual/Hands-On/Deep Theory)

### Implementation
```clojure
;; src/lib/cljs/expedition_map.cljs
(ns valley.expedition-map
  (:require [reagent.core :as r]))

(defonce !progress 
  (r/atom {:read-essays #{}
           :chosen-path nil
           :current-essay nil}))

(defn load-progress! []
  (when-let [stored (js/localStorage.getItem "valley-progress")]
    (reset! !progress (cljs.reader/read-string stored))))

(defn save-progress! []
  (js/localStorage.setItem "valley-progress" (pr-str @!progress)))

(defn mark-essay-read! [essay-id]
  (swap! !progress update :read-essays conj essay-id)
  (save-progress!))
```

**Svelte Integration:**
```svelte
<!-- web-app/src/routes/expedition/+page.svelte -->
<script>
  import { onMount } from 'svelte';
  import { loadExpeditionMap } from '$lib/cljs-bridge';
  
  let mapState = $state({});
  
  onMount(() => {
    mapState = loadExpeditionMap();
  });
</script>

<div class="expedition-map">
  {#each mapState.phases as phase}
    <region class:completed={phase.allRead}>
      <!-- SVG interactive regions -->
    </region>
  {/each}
</div>
```

---

## Phase 2: Code Playground for Essays (High Priority)

**Goal:** Embed interactive Clojure REPLs directly in essays (especially 9949-9953).

### Features
- **In-Browser REPL** — Run Clojure code examples without leaving page
- **Edit & Execute** — Modify code blocks and see results instantly
- **Persistent State** — Chain expressions together (like a real REPL session)
- **Export** — Save your explorations as gists

### Implementation
```clojure
;; src/lib/cljs/repl.cljs
(ns valley.repl
  (:require [cljs.js :as cljs-js]))

(def !repl-state (cljs-js/empty-state))

(defn eval-code [code-str callback]
  (cljs-js/eval-str !repl-state code-str "repl"
    {:eval cljs-js/js-eval
     :load (fn [m cb] (cb {:lang :clj :source ""}))}
    callback))
```

**Markdown Extension:**
````markdown
```clojure-interactive
(defn greet [name]
  (str "Hello from the valley, " name "!"))

(greet "reader")
;; => "Hello from the valley, reader!"
```
````

**Svelte Component:**
```svelte
<!-- web-app/src/lib/components/ClojureREPL.svelte -->
<script>
  import { evalClojure } from '$lib/cljs-bridge';
  
  export let initialCode = '';
  let code = $state(initialCode);
  let result = $state(null);
  
  async function runCode() {
    result = await evalClojure(code);
  }
</script>

<div class="repl-container">
  <textarea bind:value={code} />
  <button onclick={runCode}>▶ Run</button>
  <pre class="output">{result}</pre>
</div>
```

---

## Phase 3: Concept Graph Visualization (Medium Priority)

**Goal:** Visual network showing how essays connect (characters, metaphors, technical concepts).

### Features
- **Force-Directed Graph** — Essays as nodes, connections as edges
- **Filter by Type** — Show only character connections, or only technical refs
- **Click to Navigate** — Jump to essays from graph
- **Highlight Path** — Show your reading path through the network

### Implementation
```clojure
;; scripts/generate-concept-graph.bb
(ns valley.concept-graph
  (:require [babashka.fs :as fs]
            [clojure.string :as str]))

(defn extract-connections [markdown-file]
  {:file (fs/file-name markdown-file)
   :characters (extract-character-mentions markdown-file)
   :metaphors (extract-metaphor-tags markdown-file)
   :links (extract-internal-links markdown-file)})

(defn build-graph [writings-dir]
  (let [files (fs/glob writings-dir "99*.md")
        connections (map extract-connections files)]
    {:nodes (map :file connections)
     :edges (mapcat (fn [conn]
                      (map #(hash-map :from (:file conn) :to %)
                           (:links conn)))
                    connections)}))

;; Output: web-app/static/content/concept-graph.edn
```

**Svelte Visualization (using D3 or similar):**
```svelte
<!-- web-app/src/routes/map/+page.svelte -->
<script>
  import { onMount } from 'svelte';
  import * as d3 from 'd3';
  
  let graphData = $state(null);
  
  onMount(async () => {
    const resp = await fetch('./content/concept-graph.json');
    graphData = await resp.json();
    renderGraph(graphData);
  });
</script>

<svg class="concept-graph" bind:this={svgElement} />
```

---

## Phase 4: Reading Progress & Achievements (Low Priority)

**Goal:** Gamify the learning journey with achievements, progress bars, mastery badges.

### Features
- **Progress Bars** — Visual feedback (e.g., "Phase I: 3/4 essays complete")
- **Achievement Badges** — "Met the Wise Elders," "Crossed the Bridge," "Reached the Grainhouse"
- **Reading Streaks** — Track consecutive days reading
- **Share Progress** — Export achievements to share on social media

### Data Model
```clojure
{:user-progress
 {:essays-read #{"9948" "9949" "9950"}
  :phases-completed #{:foundation}
  :achievements #{:wise-elders :ancient-spells}
  :streak-days 5
  :last-read-date "2025-10-10"
  :favorite-character :gentle-gardener
  :chosen-path :visual-metaphorical}}
```

---

## Phase 5: Search & Discovery Engine (High Priority)

**Goal:** Full-text search across all essays with semantic filtering.

### Features
- **Instant Search** — Filter essays by keyword, character, metaphor, or topic
- **Semantic Tags** — Auto-generated from content (using Clojure NLP?)
- **Related Essays** — "If you liked 9952, try 9953"
- **Character Filter** — Show all essays featuring "The Rust Blacksmith"

### Implementation Strategy
```clojure
;; scripts/build-search-index.bb
(defn build-search-index [writings-dir]
  (->> (fs/glob writings-dir "*.md")
       (map parse-essay)
       (map (fn [essay]
              {:id (:id essay)
               :title (:title essay)
               :characters (extract-characters (:content essay))
               :metaphors (extract-metaphors (:content essay))
               :keywords (extract-keywords (:content essay))
               :body-text (:content essay)}))
       (spit "web-app/static/content/search-index.edn")))
```

**Client-Side Search (ClojureScript):**
```clojure
(ns valley.search
  (:require [clojure.string :as str]))

(defn search-essays [index query]
  (->> index
       (filter (fn [essay]
                 (or (str/includes? (str/lower-case (:title essay)) query)
                     (str/includes? (str/lower-case (:body-text essay)) query)
                     (some #(str/includes? % query) (:keywords essay)))))
       (take 10)))
```

---

## Phase 6: Glossary Tooltips (Medium Priority)

**Goal:** Hover over technical terms to see definitions without leaving the page.

### Features
- **Auto-Detect Terms** — Mark technical terms (e.g., "immutability," "microkernel")
- **Tooltip Popups** — Hover to see definition + link to deep-dive essay
- **Definition Source** — Pull from generated glossary (EDN format)

### Build Pipeline Addition
```clojure
;; scripts/build-glossary.bb
(def technical-terms
  {"immutability" {:definition "Data that never changes after creation"
                   :see-also ["9949-intro-clojure-nix-ecosystem"
                              "9953-infuse-nix-paradigm"]
                   :metaphor "River of State (9949)"}
   "microkernel" {:definition "Minimal OS kernel with services in userspace"
                  :see-also ["9954-sel4-verified-microkernel"
                             "9955-redox-os-rust-microkernel"]}})

(spit "web-app/static/content/glossary.edn" (pr-str technical-terms))
```

---

## Phase 7: Clojure-Svelte State Bridge (Foundation)

**Goal:** Create clean interop layer between ClojureScript and Svelte reactivity.

### Bridge Architecture
```clojure
;; src/lib/cljs/bridge.cljs
(ns valley.bridge
  (:require [reagent.core :as r]))

(defonce !app-state 
  (r/atom {:current-essay nil
           :progress {}
           :theme :dark}))

;; Expose to JavaScript/Svelte
(defn ^:export getState []
  (clj->js @!app-state))

(defn ^:export updateState [path value]
  (swap! !app-state assoc-in (js->clj path) (js->clj value)))

(defn ^:export subscribe [path callback]
  (add-watch !app-state path
    (fn [_ _ old-state new-state]
      (when (not= (get-in old-state path)
                  (get-in new-state path))
        (callback (clj->js (get-in new-state path)))))))
```

**Svelte Integration:**
```javascript
// web-app/src/lib/cljs-bridge.ts
import { getState, updateState, subscribe } from './cljs/bridge';

export function useClojureState(path: string[]) {
  let value = $state(getState());
  
  subscribe(path, (newValue) => {
    value = newValue;
  });
  
  return {
    get value() { return value; },
    set value(v) { updateState(path, v); }
  };
}
```

---

## Technical Implementation Plan

### Dependencies to Add
```json
{
  "dependencies": {
    "@sveltejs/adapter-static": "^3.0.0",  // ✅ Already have
    "d3": "^7.8.5",                         // For concept graph
    "fuse.js": "^7.0.0"                    // Fuzzy search (or use CLJS)
  },
  "devDependencies": {
    "shadow-cljs": "^2.26.0",              // ClojureScript compiler
    "reagent": "^1.2.0"                    // React wrapper for CLJS
  }
}
```

### Build Pipeline Updates
```bash
# New build workflow
bb writings:build          # Clojure: MD → JSON (current)
bb glossary:build          # Clojure: Generate glossary EDN
bb concept-graph:build     # Clojure: Generate connection graph
npx shadow-cljs release app # ClojureScript: Compile to JS
npm run build              # SvelteKit: Static site generation
```

### File Structure
```
web-app/
├── src/
│   ├── lib/
│   │   ├── cljs/              # NEW: ClojureScript modules
│   │   │   ├── bridge.cljs    # State management
│   │   │   ├── repl.cljs      # Interactive REPL
│   │   │   ├── search.cljs    # Search engine
│   │   │   └── progress.cljs  # Progress tracking
│   │   ├── components/        # Svelte components
│   │   │   ├── ClojureREPL.svelte
│   │   │   ├── ConceptGraph.svelte
│   │   │   ├── ProgressTracker.svelte
│   │   │   └── GlossaryTooltip.svelte
│   │   └── cljs-bridge.ts     # TypeScript interop
│   └── routes/
│       ├── expedition/        # NEW: Interactive map
│       ├── search/            # NEW: Search UI
│       └── glossary/          # NEW: Browse glossary
├── shadow-cljs.edn            # NEW: CLJS config
└── static/
    └── content/
        ├── *.json             # Current essay data
        ├── glossary.edn       # NEW: Term definitions
        ├── concept-graph.edn  # NEW: Essay connections
        └── search-index.edn   # NEW: Search data
```

---

## Specific Feature Roadmap

### Immediate (Next 2 Weeks)
- [ ] Set up shadow-cljs in web-app
- [ ] Create ClojureScript bridge module
- [ ] Add progress tracking (LocalStorage + CLJS atom)
- [ ] Build simple glossary (static generation)

### Short-Term (Next Month)
- [ ] Interactive expedition map with SVG
- [ ] Code playground (Clojure REPL in browser)
- [ ] Search functionality (client-side, EDN-based)
- [ ] Glossary tooltips on technical terms

### Medium-Term (2-3 Months)
- [ ] Concept graph visualization (D3 + CLJS)
- [ ] Achievement system with badges
- [ ] Reading streak tracking
- [ ] Character-based essay filtering

### Long-Term (3-6 Months)
- [ ] Community features (share progress, comments)
- [ ] Essay recommendations (based on reading history)
- [ ] Learning path customization
- [ ] Export/import progress (EDN format for portability)

---

## Why ClojureScript + Svelte?

### The Synergy

**ClojureScript Strengths:**
- Immutable data structures (perfect for state management)
- Functional transformations (map/filter/reduce over essay data)
- EDN data format (seamless with our build pipeline)
- REPL-driven development (matches our philosophy from 9949!)

**Svelte Strengths:**
- Minimal runtime overhead (compiles to vanilla JS)
- Reactive declarations (no virtual DOM overhead)
- Excellent DX (developer experience)
- Perfect for static-first architecture (SSG friendly)

**Together:**
- **CLJS handles logic**, **Svelte handles presentation**
- Shared data format (EDN → JSON for Svelte consumption)
- Both emphasize simplicity and performance
- Philosophical alignment with valley principles (9953 infuse.nix thinking!)

### Example: Progress Tracking Integration

**ClojureScript (Logic):**
```clojure
(ns valley.progress
  (:require [reagent.core :as r]))

(defonce !progress (r/atom {:read #{}}))

(defn essay-read? [essay-id]
  (contains? (:read @!progress) essay-id))

(defn mark-read! [essay-id]
  (swap! !progress update :read conj essay-id)
  (js/localStorage.setItem "progress" (pr-str @!progress)))

(defn completion-percentage []
  (let [total 13
        read-count (count (:read @!progress))]
    (/ (* read-count 100) total)))
```

**Svelte (UI):**
```svelte
<script>
  import { markEssayRead, getCompletionPercentage } from '$lib/cljs-bridge';
  
  let completion = $derived(getCompletionPercentage());
</script>

<div class="progress-bar">
  <div class="fill" style="width: {completion}%"></div>
  <span>{completion}% Complete</span>
</div>
```

---

## Data Flow Architecture

### Build Time (Clojure)
```
writings/*.md 
  → parse (Clojure)
  → enrich (add metadata, extract terms)
  → generate EDN/JSON
  → static/content/*.{edn,json}
```

### Run Time (Browser)
```
User opens page
  → Svelte loads JSON (for static content)
  → CLJS loads EDN (for interactive features)
  → User interacts (clicks, searches, tracks progress)
  → CLJS updates state (immutable transformations)
  → Svelte reacts to state changes (compiled reactivity)
  → LocalStorage persists (EDN format)
```

### Data Format Example
```clojure
;; content/9949-intro-clojure-nix-ecosystem.edn
{:id "9949"
 :title "The Wise Elders Meet"
 :characters [:clojure-sage :nix-architect]
 :metaphors [:braided-rope :river-of-state :living-mulch]
 :code-blocks [{:lang "clojure"
                :interactive? true
                :code "(+ 1 2 3)"}]
 :glossary-terms ["immutability" "pure function" "REPL"]
 :connections {:prev "9948" :next "9950"}
 :estimated-reading-time 20}
```

---

## Testing Strategy

### Navigation Link Testing (Already Working!)
```bash
bb scripts/test-navigation-links.bb  # ✅ All 37 links pass
```

### Interactive Feature Testing (NEW)
```bash
bb test:cljs-bridge     # Test ClojureScript-JavaScript interop
bb test:progress-sync   # Test LocalStorage persistence
bb test:repl-eval       # Test in-browser REPL evaluation
bb test:search-index    # Test search functionality
```

---

## Migration Strategy: Keep It Simple

### Principle: Progressive Enhancement

**Core Content** (Must Work):
- ✅ Static HTML generated by SvelteKit (current)
- ✅ Works without JavaScript
- ✅ SEO-friendly, fast, accessible

**Enhanced Features** (Nice to Have):
- 🎯 Interactive map (degrades to static links)
- 🎯 Progress tracking (degrades to no tracking)
- 🎯 REPL playground (degrades to read-only code)
- 🎯 Search (degrades to browser find)

### Implementation Order
1. **Foundation**: Set up shadow-cljs, create bridge module
2. **Simple First**: Progress tracking (LocalStorage + CLJS atom)
3. **Content-Driven**: Glossary generation from Markdown
4. **Interactive**: Add REPL to 1-2 essays, test extensively
5. **Visual**: Concept graph as final "wow" feature
6. **Polish**: Refine UX, performance, accessibility

---

## Success Metrics

### Technical
- [ ] Build time < 10 seconds (with CLJS compilation)
- [ ] Interactive features < 50KB JavaScript (keep it tiny!)
- [ ] Lighthouse score > 95 (don't sacrifice performance)
- [ ] Works offline (service worker + LocalStorage)

### User Experience
- [ ] Progress tracking works across devices (export/import)
- [ ] REPL executes code in < 100ms
- [ ] Search returns results in < 50ms
- [ ] Glossary tooltips appear in < 16ms (one frame)

### Content
- [ ] All 13 core essays (9948-9960) have interactive features
- [ ] Concept graph shows 100+ connections
- [ ] Glossary covers 50+ technical terms
- [ ] 10+ code examples runnable in browser

---

## Next Steps

1. **Research** — Evaluate shadow-cljs vs other CLJS compilers
2. **Prototype** — Build minimal ClojureScript bridge
3. **Test** — Add progress tracking to one essay
4. **Evaluate** — Does CLJS add value? Or over-engineering?
5. **Decide** — Go/No-Go on full integration

---

## The Valley Philosophy Applied

### From 9953 (infuse.nix Paradigm)
> *"You don't need to replant the entire orchard to change the fruit on one branch. You graft. You infuse."*

**Applied to Website:**
- Don't rebuild the whole site
- Infuse interactive features where they add value
- Keep static-first architecture (it works!)
- Add ClojureScript as enhancement layer, not replacement

### From 9949 (Wise Elders)
> *"When code and data are the same thing, everything changes."*

**Applied to Website:**
- EDN data flows from build pipeline to browser
- Same data structures in Clojure build and ClojureScript runtime
- Homoiconic approach: data-driven UI

### From 9960 (Grainhouse)
> *"Build systems that last generations."*

**Applied to Website:**
- No framework lock-in (simple Svelte + CLJS)
- Data in human-readable EDN (survives technology churn)
- Progressive enhancement (works without JS)
- Fork-friendly (all code MIT licensed)

---

## Risk Assessment

### Risks
1. **Complexity**: Adding CLJS might over-complicate simple site
2. **Build Time**: Shadow-cljs compilation could slow CI
3. **Bundle Size**: JavaScript payload might bloat
4. **Maintenance**: Two languages instead of one

### Mitigations
1. Keep ClojureScript optional (progressive enhancement)
2. Cache CLJS builds, only recompile when logic changes
3. Code-split: load CLJS only for interactive pages
4. Document extensively, make it hackable for future maintainers

---

## Alternative: Pure Svelte (Simpler Path)

**Could achieve 80% of goals without ClojureScript:**
- Progress tracking: Svelte stores + LocalStorage
- Search: Fuse.js (fuzzy search library)
- Interactive map: Svelte components + SVG
- Code highlighting: PrismJS (no execution, just syntax highlighting)

**Trade-off**: Lose philosophical alignment (Clojure everywhere) and REPL features.

---

## Recommendation

**Phase 1**: Start with **Pure Svelte** for progress tracking and search
**Phase 2**: Add **ClojureScript REPL** as experiment for 1-2 essays
**Phase 3**: Evaluate user engagement and decide on full integration

**Rationale**: Keep it simple (valley principle!), add complexity only when value is proven.

---

*The valley grows through careful cultivation, not hasty expansion.*


