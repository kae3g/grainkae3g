<script>
  import ThemeToggle from './ThemeToggle.svelte';
  export let documents = [];
</script>

<ThemeToggle />

<div class="consciousness-index">
  <header>
    <h1>🤖 Robotic Farm Consciousness</h1>
    <p>Sacred agricultural automation docs</p>
    <p class="philosophy">"The Way is hidden in light,
       revealed in darkness" - Ch'an</p>
  </header>

  <main>
    {#each documents as doc}
      <article class="doc-preview">
        <a href="/{doc.slug}">
          <span class="number">
            {doc.number}
          </span>
          <h2>{doc.title}</h2>
        </a>
      </article>
    {/each}
  </main>
</div>

<style>
  .consciousness-index {
    max-width: 57ch;
    margin: 2rem auto;
    padding: 2rem;
    background: var(--color-bg);
    color: var(--color-fg);
    min-height: 100vh;
  }
  header {
    text-align: center;
    margin-bottom: 3rem;
  }
  h1 {
    font-size: 2.5rem;
    margin-bottom: 0.5rem;
    color: var(--color-emphasis);
  }
  .philosophy {
    font-style: italic;
    color: var(--color-fg-alt);
    margin-top: 1rem;
  }
  .doc-preview {
    border: 1px solid var(--color-border);
    padding: 1rem;
    margin-bottom: 1rem;
    border-radius: 0.5rem;
    background: var(--color-bg-alt);
  }
  .doc-preview a {
    text-decoration: none;
    color: var(--color-fg);
  }
  .doc-preview:hover {
    border-color: var(--color-accent);
    transform: translateY(-2px);
  }
  .number {
    font-family: 'Courier New', monospace;
    color: var(--color-fg-alt);
    font-size: 0.85rem;
  }
</style>