<script>
  import { onMount } from 'svelte';
  import { base } from '$app/paths';
  
  let username = 'kae3g'; // Default
  
  onMount(async () => {
    try {
      const res = await fetch(`${base}/site-config.json`);
      const config = await res.json();
      username = config.username || 'kae3g';
    } catch (e) {
      console.warn('Could not load site config, using default username');
    }
  });
</script>

<span class="username-indicator">{username}</span>

<style>
  .username-indicator {
    position: fixed;
    top: 20px;
    /* Golden ratio position: ~61.8% from left edge of content column
       Content is max 600px centered, so from center we calculate:
       50% + (61.8% - 50%) * 600px / 100vw for the offset
       Simplified: position at golden ratio between content center and right edge */
    right: calc((100vw - 600px) / 2 + 600px * 0.382);
    color: var(--fg);
    font-size: 0.75em;
    font-family: 'Courier New', monospace;
    opacity: 0.6;
    z-index: 1000;
  }
  
  /* Mobile adjustment - place it left of the toggles with clean spacing */
  @media (max-width: 640px) {
    .username-indicator {
      right: 6.5rem;
    }
  }
</style>

