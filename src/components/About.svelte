<script>
  import { onMount } from 'svelte';
  
  let currentGraintime = '';
  
  onMount(async () => {
    // In a real implementation, this would call the graintime API
    currentGraintime = '12025-10-24--1033--PDT--moon-vishakha------asc-gem000--sun-11th--kae3g';
  });
</script>

<div class="voice-glow">
  <div class="voice-indicator">🔥 Glow's Voice</div>
  
  <h1>About Grain6 Get Started</h1>
  
  <p><strong>Alright, let's break this down. The structure is solid, but we need to tighten up the logic here.</strong></p>
  
  <p>You're in the right place - this is where real work gets done. Let's analyze what we have and optimize it for maximum efficiency.</p>
  
  <div class="card">
    <h3>🎯 Technical Overview</h3>
    <p>Grain6 Get Started is a comprehensive onboarding platform for the Grain Network ecosystem. Built with modern web technologies and designed for scalability, it provides:</p>
    
    <ul>
      <li><strong>Svelte-based frontend</strong> - Fast, reactive user interface</li>
      <li><strong>Grainpath integration</strong> - Immutable, timestamped navigation system</li>
      <li><strong>Multi-chain support</strong> - ICP, Hedera, and Solana integration</li>
      <li><strong>Voice mode system</strong> - Trish and Glow personality modes</li>
      <li><strong>Responsive design</strong> - Works on all devices and screen sizes</li>
    </ul>
  </div>
  
  <div class="card">
    <h3>🏗️ Architecture</h3>
    <p>The system is built on a modular architecture that emphasizes:</p>
    
    <ul>
      <li><strong>Component-based design</strong> - Reusable UI components</li>
      <li><strong>State management</strong> - Efficient data flow and updates</li>
      <li><strong>API integration</strong> - Seamless backend communication</li>
      <li><strong>Performance optimization</strong> - Fast loading and smooth interactions</li>
      <li><strong>Security first</strong> - Built with security best practices</li>
    </ul>
  </div>
  
  <div class="card">
    <h3>📊 Current Status</h3>
    <p>Project Status: <strong>Active Development</strong></p>
    <p>Last Updated: <code>{currentGraintime}</code></p>
    <p>Version: 1.0.0</p>
    <p>License: MIT</p>
  </div>
  
  <div class="card">
    <h3>🔧 Technology Stack</h3>
    <ul>
      <li><strong>Frontend:</strong> Svelte, Vite, CSS3</li>
      <li><strong>Backend:</strong> Clojure, Babashka</li>
      <li><strong>Blockchain:</strong> ICP, Hedera, Solana</li>
      <li><strong>Deployment:</strong> GitHub Pages, Codeberg Pages</li>
      <li><strong>Version Control:</strong> Git with GPG signing</li>
    </ul>
  </div>
  
  <div style="text-align: center; margin-top: var(--spacing-xl);">
    <a href="/team" class="btn">Meet the Team</a>
    <a href="/contact" class="btn btn-secondary">Technical Support</a>
  </div>
</div>
