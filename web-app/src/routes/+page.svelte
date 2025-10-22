<script>
  import { onMount } from 'svelte';
  import { browser } from '$app/environment';
  import { navigating } from '$app/stores';
  import { link } from '$lib/paths';
  import LicenseFooter from '$lib/LicenseFooter.svelte';
  
  export let data;
  
  let entries = data.entries;
  let displayedEntries = [];
  let currentPage = 0;
  let itemsPerPage = 10;
  let loading = false;
  let hasMore = true;
  
  // Initialize with first page
  onMount(() => {
    loadMore();
  });
  
  function loadMore() {
    if (loading || !hasMore) return;
    
    loading = true;
    
    // Simulate loading delay for smooth UX
    setTimeout(() => {
      const start = currentPage * itemsPerPage;
      const end = start + itemsPerPage;
      const newEntries = entries.slice(start, end);
      
      displayedEntries = [...displayedEntries, ...newEntries];
      currentPage++;
      
      hasMore = end < entries.length;
      loading = false;
    }, 300);
  }
  
  function handleScroll() {
    if (!browser) return;
    
    const { scrollTop, scrollHeight, clientHeight } = document.documentElement;
    const threshold = 100;
    
    if (scrollTop + clientHeight >= scrollHeight - threshold) {
      loadMore();
    }
  }
  
  if (browser) {
    onMount(() => {
      window.addEventListener('scroll', handleScroll);
      return () => window.removeEventListener('scroll', handleScroll);
    });
  }
</script>

<svelte:head>
  <title>Coldriver Heal</title>
</svelte:head>

<h1>Coldriver Heal</h1>
<p><a href="https://github.com/kae3g/12025-10/tree/tundra" class="branch-link">branch: tundra</a></p>

<hr>

{#if entries.length === 0}
  <p>No writings yet. Run <code>bb writings:build</code> to generate content.</p>
{:else}
  <ul>
    {#each displayedEntries as entry}
      <li>
        <a href={`${link('/')}${entry.slug}.html`}>
          <strong>{entry.title}</strong>
          {#if $navigating && $navigating.to?.url.pathname.includes(entry.slug)}
            <span class="spinner">◴</span>
          {/if}
        </a>
        {#if entry.timestamp}
          <span class="meta"> — {entry.timestamp}</span>
        {/if}
        {#if entry.category}
          <br><span class="meta">{entry.category}</span>
        {/if}
      </li>
    {/each}
  </ul>
  
  {#if hasMore}
    <div class="load-more">
      <button on:click={loadMore} disabled={loading}>
        {#if loading}
          Loading more writings...
        {:else}
          Load More ({entries.length - displayedEntries.length} remaining)
        {/if}
      </button>
    </div>
  {/if}
  
  {#if !hasMore && displayedEntries.length > 0}
    <div class="end">
      <p>❄️ All {entries.length} writings loaded</p>
    </div>
    
    <LicenseFooter />
  {/if}
{/if}

<style>
  ul {
    list-style: none;
    padding: 0;
  }
  
  li {
    margin: 1.5em 0;
    padding-bottom: 1em;
    border-bottom: 1px solid rgba(238, 238, 238, 0.1);
  }
  
  li:last-child {
    border-bottom: none;
  }
  
  .load-more, .end {
    text-align: center;
    margin: 2em 0;
    padding: 1em;
  }
  
  .end p {
    margin: 0;
    font-style: italic;
    opacity: 0.7;
  }
  
  button {
    background-color: var(--lk);
    color: var(--bg);
    border: none;
    padding: 0.8em 1.5em;
    border-radius: 6px;
    font-size: 1em;
    cursor: pointer;
    transition: opacity 0.2s;
  }
  
  button:hover:not(:disabled) {
    opacity: 0.9;
  }
  
  button:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }
  
  .branch-link {
    font-family: 'Courier New', monospace;
    color: var(--lk);
    text-decoration: none;
  }
  
  .branch-link:hover {
    text-decoration: underline;
  }
  
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
</style>

