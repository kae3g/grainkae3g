<script>
  import { onMount } from 'svelte';
  import { base } from '$app/paths';
  
  let language = 'en'; // Default
  
  onMount(async () => {
    try {
      const res = await fetch(`${base}/site-config.json`);
      const config = await res.json();
      language = config.language || 'en';
    } catch (e) {
      console.warn('Could not load site config, using default language');
    }
  });
</script>

<span class="language-indicator">language: {language}</span>

<style>
  .language-indicator {
    position: fixed;
    top: 20px;
    /* Align with left edge of content column (max 600px, centered) */
    left: max(calc((100vw - 600px) / 2), 1rem);
    color: var(--fg);
    font-size: 0.75em;
    font-family: 'Courier New', monospace;
    opacity: 0.6;
    z-index: 1000;
  }
  
  /* Mobile adjustment */
  @media (max-width: 640px) {
    .language-indicator {
      left: 0.5rem;
    }
  }
</style>

