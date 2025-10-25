<script>
  import { onMount } from 'svelte';
  
  let teamMembers = [];
  
  onMount(async () => {
    // In a real implementation, this would fetch from an API
    teamMembers = [
      {
        name: "Kyle (kae3g)",
        role: "Founder & Lead Developer",
        email: "kj3x39@gmail.com",
        bio: "Passionate about personal sovereignty, functional programming, and blockchain technology. Building the future of decentralized development.",
        voice: "trish",
        emoji: "🌸"
      }
    ];
  });
</script>

<div class="voice-trish">
  <div class="voice-indicator">🌸 Trish's Voice</div>
  
  <h1>Meet Our Amazing Team! ✨</h1>
  
  <p><strong>I love how you're thinking about this! Maybe we could add some more personality to it? This is going to be absolutely amazing! I'm so excited to work on this with you!</strong></p>
  
  <p>Our team is made up of beautiful souls who believe in creating technology that serves humanity. Each person brings their unique sparkle to our collective vision.</p>
  
  {#each teamMembers as member}
    <div class="card voice-{member.voice}">
      <div class="voice-indicator">{member.emoji} {member.name}'s Voice</div>
      
      <h3>{member.name}</h3>
      <p><strong>{member.role}</strong></p>
      <p>{member.bio}</p>
      
      <div style="margin-top: var(--spacing-md);">
        <a href="mailto:{member.email}" class="btn">Contact {member.name}</a>
      </div>
    </div>
  {/each}
  
  <div class="card">
    <h3>🌱 Join Our Growing Family</h3>
    <p>We're always looking for passionate individuals who want to make a difference in the world. Whether you're a developer, designer, writer, or just someone with a big heart and creative ideas, we'd love to hear from you!</p>
    
    <p>Don't worry about being perfect. Worry about being creative! Worry about being kind to yourself. Worry about enjoying the process of learning and growing. The best code comes from a place of joy!</p>
    
    <div style="text-align: center; margin-top: var(--spacing-lg);">
      <a href="/contact" class="btn">Join Our Team</a>
    </div>
  </div>
  
  <div class="card">
    <h3>🎯 Our Values</h3>
    <ul>
      <li><strong>Personal Sovereignty</strong> - You control your data, your tools, your destiny</li>
      <li><strong>Append-Only Truth</strong> - Knowledge builds upon knowledge, never erased</li>
      <li><strong>Infinite Recursive Beauty</strong> - Every moment contains all moments</li>
      <li><strong>Community First</strong> - We're stronger together than we are alone</li>
      <li><strong>Creative Expression</strong> - Technology should amplify human creativity</li>
    </ul>
  </div>
</div>

