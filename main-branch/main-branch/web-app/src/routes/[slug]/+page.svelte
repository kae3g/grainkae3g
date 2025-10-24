<script>
  import { link } from '$lib/paths';
  import { navigating } from '$app/stores';
  export let data;
</script>

<svelte:head>
  <title>{data.entry.meta.unlisted ? '(unlisted) ' : ''}{data.entry.meta.title} — kae3g</title>
</svelte:head>

<a href={link('/')} style="font-size: 0.9em; opacity: 0.7;">
  ← back to index
  {#if $navigating && $navigating.to?.url.pathname === link('/')}
    <span class="spinner">◴</span>
  {/if}
</a>

{#if data.entry.meta.unlisted}
  <div class="unlisted-notice">
    <strong>(unlisted)</strong> — This essay is not shown on the main index but remains accessible via direct link
  </div>
{/if}

<article>
  {@html data.entry.html}
</article>

<hr>

{#if data.entry.meta.timestamp}
  <p class="meta">
    <strong>Timestamp:</strong> {data.entry.meta.timestamp}
  </p>
{/if}

{#if data.entry.meta.series}
  <p class="meta">
    <strong>Series:</strong> {data.entry.meta.series}
  </p>
{/if}

{#if data.entry.meta.category}
  <p class="meta">
    <strong>Category:</strong> {data.entry.meta.category}
  </p>
{/if}

{#if data.entry.meta['reading-time']}
  <p class="meta">
    <strong>Reading Time:</strong> {data.entry.meta['reading-time']}
  </p>
{/if}

<p style="margin-top: 2em;">
  <a href={link('/')}>
    ← back to index
    {#if $navigating && $navigating.to?.url.pathname === link('/')}
      <span class="spinner">◴</span>
    {/if}
  </a>
</p>

<style>
  .spinner {
    display: inline-block;
    animation: spin 0.8s steps(8) infinite;
    font-size: 1em;
    transform-origin: center;
    margin-left: 0.5em;
    opacity: 0.7;
  }
  
  @keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
  }
  
  .unlisted-notice {
    background-color: var(--code-bg);
    border: 1px solid var(--bd);
    border-radius: 4px;
    padding: 0.75em 1em;
    margin: 1em 0;
    font-size: 0.9em;
    color: var(--fg);
    opacity: 0.8;
  }
  
  .unlisted-notice strong {
    color: var(--lkv);
  }
</style>

