<script>
  import { onMount } from 'svelte';
  
  let theme = 'dark';
  
  onMount(() => {
    // Check localStorage or default to dark (Tundra night)
    theme = localStorage.getItem('theme') || 'dark';
    document.documentElement.setAttribute('data-theme', theme);
  });
  
  function toggleTheme() {
    theme = theme === 'dark' ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem('theme', theme);
  }
</script>

<button 
  on:click={toggleTheme}
  class="theme-toggle"
  aria-label="Toggle theme"
  title={theme === 'dark' ? 'Switch to light theme' : 'Switch to dark theme'}
>
  *
</button>

<style>
  .theme-toggle {
    position: fixed;
    top: 20px;
    /* Align with right edge of content column (max 600px, centered) */
    right: max(calc((100vw - 600px) / 2), 1rem);
    background: none;
    border: none;
    color: var(--fg);
    font-size: 28px;
    cursor: pointer;
    padding: 8px 12px;
    line-height: 1;
    opacity: 0.6;
    transition: opacity 0.2s ease, transform 0.2s ease;
    font-family: 'Times New Roman', serif;
    z-index: 1000;
  }
  
  .theme-toggle:hover {
    opacity: 1;
    transform: scale(1.2);
  }
  
  .theme-toggle:active {
    transform: scale(1.1);
  }
  
  /* Mobile adjustment */
  @media (max-width: 640px) {
    .theme-toggle {
      right: 0.5rem;
    }
  }
</style>

