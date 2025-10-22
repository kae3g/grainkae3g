<script>
  import { onMount } from 'svelte';
  
  let theme = 'light';
  
  onMount(() => {
    const stored = localStorage.getItem('theme');
    if (stored) {
      theme = stored;
    } else if (window.matchMedia(
      '(prefers-color-scheme: dark)').matches) {
      theme = 'dark';
    }
    applyTheme();
  });
  
  function toggleTheme() {
    theme = theme === 'light' ? 'dark' : 'light';
    applyTheme();
  }
  
  function applyTheme() {
    document.documentElement
      .setAttribute('data-theme', theme);
    localStorage.setItem('theme', theme);
  }
</script>

<button 
  class="theme-toggle"
  on:click={toggleTheme}
  aria-label="Toggle theme"
  title="{theme === 'light' ? 
    'Switch to dark mode' : 
    'Switch to light mode'}">
  {#if theme === 'light'}
    🌙 Dark Mode
  {:else}
    ☀️ Light Mode
  {/if}
</button>

<style>
  .theme-toggle {
    position: fixed;
    top: 1rem;
    right: 1rem;
    background: var(--color-bg-alt);
    color: var(--color-fg);
    border: 2px solid var(--color-border);
    padding: 0.5rem 1rem;
    border-radius: 0.5rem;
    cursor: pointer;
    font-family: 'Times New Roman', serif;
    font-size: 0.9rem;
    z-index: 1000;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  }
  
  .theme-toggle:hover {
    background: var(--color-accent);
    color: var(--color-bg);
    border-color: var(--color-accent);
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0,0,0,0.2);
  }
  
  .theme-toggle:active {
    transform: translateY(0);
  }
</style>