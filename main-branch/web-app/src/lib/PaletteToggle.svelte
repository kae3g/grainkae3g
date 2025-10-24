<script>
  import { onMount } from 'svelte';
  
  let palette = 'warm';
  
  onMount(() => {
    // Check localStorage or default to warm (Valley/Mycelium)
    palette = localStorage.getItem('palette') || 'warm';
    if (palette === 'cold') {
      document.documentElement.setAttribute('data-palette', 'cold');
    }
  });
  
  function togglePalette() {
    palette = palette === 'warm' ? 'cold' : 'warm';
    if (palette === 'cold') {
      document.documentElement.setAttribute('data-palette', 'cold');
    } else {
      document.documentElement.removeAttribute('data-palette');
    }
    localStorage.setItem('palette', palette);
  }
</script>

<button 
  on:click={togglePalette}
  class="palette-toggle"
  aria-label="Toggle palette"
  title={palette === 'warm' ? 'Switch to cold palette (Tundra)' : 'Switch to warm palette (Valley)'}
>
  %
</button>

<style>
  .palette-toggle {
    position: fixed;
    top: 20px;
    /* Position close to the left of the theme toggle (*) - reduced spacing */
    right: max(calc((100vw - 600px) / 2 + 36px), calc(1rem + 36px));
    background: none;
    border: none;
    color: var(--fg);
    font-size: 24px;
    cursor: pointer;
    padding: 8px 10px;
    line-height: 1;
    opacity: 0.6;
    transition: opacity 0.2s ease, transform 0.2s ease;
    font-family: 'Times New Roman', serif;
    z-index: 1000;
  }
  
  .palette-toggle:hover {
    opacity: 1;
    transform: scale(1.2);
  }
  
  .palette-toggle:active {
    transform: scale(1.1);
  }
  
  /* Mobile adjustment */
  @media (max-width: 640px) {
    .palette-toggle {
      right: calc(0.5rem + 36px);
    }
  }
</style>

