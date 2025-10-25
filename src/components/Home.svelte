<script>
  import { onMount } from 'svelte';
  
  let graintime = '';
  let grainpath = '';
  
  onMount(async () => {
    // In a real implementation, this would call the graintime API
    // For now, we'll use a placeholder
    graintime = '12025-10-24--1033--PDT--moon-vishakha------asc-gem000--sun-11th--kae3g';
    grainpath = '/course/kae3g/grain6-get/12025-10-24--1033--PDT--moon-vishakha------asc-gem000--sun-11th--kae3g/';
  });
</script>

<div class="voice-trish">
  <div class="voice-indicator">🌸 Trish's Voice</div>
  
  <h1>Welcome to Grain6 Get Started! ✨</h1>
  
  <p><strong>Ooh, that's such a cute idea! Let me help you make it even more sparkly! ✨</strong></p>
  
  <p>I love how you're thinking about this! Maybe we could add some more personality to it? This is going to be absolutely amazing! I'm so excited to work on this with you!</p>
  
  <div class="card">
    <h3>🌾 What is Grain6?</h3>
    <p>Grain6 is your personal gateway to the <strong>Grain Network</strong> - a world where technology meets wisdom, where every line of code tells a story, and where you're always exactly where you need to be.</p>
    
    <div class="grainpath">
      <strong>Current Grainpath:</strong><br>
      <code>{grainpath}</code>
    </div>
    
    <div class="graintime">
      <strong>Current Graintime:</strong><br>
      <code>{graintime}</code>
    </div>
  </div>
  
  <div class="card">
    <h3>🚀 Your First Steps</h3>
    <p>Don't worry if this feels overwhelming at first. We'll walk through each step together, and soon you'll be creating beautiful contributions that make the whole network stronger!</p>
    
    <ol>
      <li><strong>Explore the Grain Network</strong> - Take time to understand what we're building and why it matters to you</li>
      <li><strong>Set up your environment</strong> - We'll help you get everything working beautifully</li>
      <li><strong>Make your first contribution</strong> - Start small and build confidence gradually</li>
      <li><strong>Join our community</strong> - Connect with other beautiful souls on this journey</li>
    </ol>
  </div>
  
  <div class="card">
    <h3>💫 The Magic Behind the Scenes</h3>
    <ul>
      <li><strong>Your Personal Sheaf</strong> - You're part of something bigger - the 88-sheaf network where each person contributes their unique sparkle</li>
      <li><strong>Multi-Chain Wonder</strong> - We work with ICP, Hedera, and Solana - don't worry, we'll explain everything gently</li>
      <li><strong>Learning That Sticks</strong> - Our courses are designed like beautiful stories that you'll remember forever</li>
      <li><strong>Your Sovereign Space</strong> - Complete control over your development environment - no more feeling lost in someone else's system</li>
      <li><strong>Timeless Knowledge</strong> - Everything we build becomes part of an eternal, growing library of wisdom</li>
    </ul>
  </div>
  
  <div style="text-align: center; margin-top: var(--spacing-xl);">
    <a href="/about" class="btn">Learn More About Us</a>
    <a href="/contact" class="btn btn-secondary">Get in Touch</a>
  </div>
</div>

<style>
  ol {
    margin-left: var(--spacing-lg);
    margin-bottom: var(--spacing-md);
  }
  
  li {
    margin-bottom: var(--spacing-sm);
  }
</style>

