<script>
  import { onMount } from 'svelte';
  
  let contactInfo = {};
  let currentGraintime = '';
  
  onMount(async () => {
    // In a real implementation, this would fetch from an API
    contactInfo = {
      email: "kj3x39@gmail.com",
      github: "https://github.com/kae3g",
      codeberg: "https://codeberg.org/kae3g",
      music: "terra4m (retro terminal vibes)"
    };
    
    currentGraintime = '12025-10-24--1033--PDT--moon-vishakha------asc-gem000--sun-11th--kae3g';
  });
  
  let formData = {
    name: '',
    email: '',
    message: '',
    voice: 'trish'
  };
  
  function handleSubmit(event) {
    event.preventDefault();
    // In a real implementation, this would send the form data to a backend
    alert(`Thank you for your message, ${formData.name}! We'll get back to you soon.`);
    formData = { name: '', email: '', message: '', voice: 'trish' };
  }
</script>

<div class="voice-glow">
  <div class="voice-indicator">🔥 Glow's Voice</div>
  
  <h1>Contact Us</h1>
  
  <p><strong>You're in the right place - this is where real work gets done. Let's analyze what we have and optimize it for maximum efficiency.</strong></p>
  
  <p>Clear communication is essential. If something isn't working, say so. If you need help, ask for it. If you have feedback, share it constructively.</p>
  
  <div class="card">
    <h3>📧 Direct Contact</h3>
    <p><strong>Email:</strong> <a href="mailto:{contactInfo.email}">{contactInfo.email}</a></p>
    <p><strong>GitHub:</strong> <a href="{contactInfo.github}" target="_blank" rel="noopener noreferrer">{contactInfo.github}</a></p>
    <p><strong>Codeberg:</strong> <a href="{contactInfo.codeberg}" target="_blank" rel="noopener noreferrer">{contactInfo.codeberg}</a></p>
    <p><strong>Music:</strong> {contactInfo.music}</p>
  </div>
  
  <div class="card">
    <h3>💬 Send us a Message</h3>
    <form on:submit={handleSubmit}>
      <div style="margin-bottom: var(--spacing-md);">
        <label for="name" style="display: block; margin-bottom: var(--spacing-xs);">Name:</label>
        <input 
          type="text" 
          id="name" 
          bind:value={formData.name} 
          required 
          style="width: 100%; padding: var(--spacing-sm); border: 1px solid var(--primary-color); border-radius: var(--border-radius); background-color: var(--background-color); color: var(--text-color);"
        />
      </div>
      
      <div style="margin-bottom: var(--spacing-md);">
        <label for="email" style="display: block; margin-bottom: var(--spacing-xs);">Email:</label>
        <input 
          type="email" 
          id="email" 
          bind:value={formData.email} 
          required 
          style="width: 100%; padding: var(--spacing-sm); border: 1px solid var(--primary-color); border-radius: var(--border-radius); background-color: var(--background-color); color: var(--text-color);"
        />
      </div>
      
      <div style="margin-bottom: var(--spacing-md);">
        <label for="voice" style="display: block; margin-bottom: var(--spacing-xs);">Preferred Voice Mode:</label>
        <select 
          id="voice" 
          bind:value={formData.voice} 
          style="width: 100%; padding: var(--spacing-sm); border: 1px solid var(--primary-color); border-radius: var(--border-radius); background-color: var(--background-color); color: var(--text-color);"
        >
          <option value="trish">🌸 Trish (Enthusiastic & Creative)</option>
          <option value="glow">🔥 Glow (Technical & Analytical)</option>
        </select>
      </div>
      
      <div style="margin-bottom: var(--spacing-md);">
        <label for="message" style="display: block; margin-bottom: var(--spacing-xs);">Message:</label>
        <textarea 
          id="message" 
          bind:value={formData.message} 
          required 
          rows="5"
          style="width: 100%; padding: var(--spacing-sm); border: 1px solid var(--primary-color); border-radius: var(--border-radius); background-color: var(--background-color); color: var(--text-color); resize: vertical;"
        ></textarea>
      </div>
      
      <button type="submit" class="btn">Send Message</button>
    </form>
  </div>
  
  <div class="card">
    <h3>🕒 Response Times</h3>
    <p>We typically respond within 24-48 hours. For urgent technical issues, please include "URGENT" in your subject line.</p>
    
    <p><strong>Current Graintime:</strong> <code>{currentGraintime}</code></p>
  </div>
  
  <div class="card">
    <h3>🤝 Community Guidelines</h3>
    <ul>
      <li><strong>Be respectful</strong> - We're all here to learn and grow together</li>
      <li><strong>Ask questions</strong> - There are no silly questions, only opportunities to learn</li>
      <li><strong>Share knowledge</strong> - Help others succeed in their contributions</li>
      <li><strong>Be patient</strong> - Building something great takes time</li>
      <li><strong>Celebrate progress</strong> - Every step forward is worth recognizing</li>
    </ul>
  </div>
</div>
