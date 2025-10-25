<script>
  import { onMount } from 'svelte';
  import { Router, Route } from 'svelte-routing';
  import Home from './components/Home.svelte';
  import About from './components/About.svelte';
  import Team from './components/Team.svelte';
  import Contact from './components/Contact.svelte';
  import Navigation from './components/Navigation.svelte';
  
  let currentPath = '';
  
  onMount(() => {
    // Get current path from URL
    currentPath = window.location.pathname;
  });
</script>

<svelte:head>
  <title>Grain6 Get Started - Your Journey Begins Here</title>
  <meta name="description" content="Welcome to Grain6 Get Started - your gateway to the Grain Network ecosystem" />
</svelte:head>

<div class="app">
  <header class="header">
    <div class="container">
      <h1>🌾 Grain6 Get Started</h1>
      <p>Your Journey to Personal Sovereignty Begins Here</p>
    </div>
  </header>
  
  <Navigation {currentPath} />
  
  <main class="container">
    <Router>
      <Route path="/" component={Home} />
      <Route path="/about" component={About} />
      <Route path="/team" component={Team} />
      <Route path="/contact" component={Contact} />
    </Router>
  </main>
  
  <footer style="text-align: center; padding: var(--spacing-xl) 0; margin-top: var(--spacing-xxl); border-top: 1px solid var(--primary-color);">
    <div class="container">
      <p>Built with ❤️ for personal sovereignty, append-only truth, and infinite recursive beauty</p>
      <p><em>"Chaos flowing out calmly, solidity watching from within, feeling like a leaf but rooted like a rock"</em></p>
      <p>🌾 From granules to grains to THE WHOLE GRAIN 🌾</p>
    </div>
  </footer>
</div>

<style>
  .app {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
  }
  
  main {
    flex: 1;
  }
</style>

