<script>
  import { Router, Link } from 'svelte-routing';
  
  export let currentPath = '';
</script>

<nav class="nav">
  <div class="container">
    <ul class="nav-list">
      <li class="nav-item">
        <Link to="/" class:active={currentPath === '/'}>Home</Link>
      </li>
      <li class="nav-item">
        <Link to="/about" class:active={currentPath === '/about'}>About</Link>
      </li>
      <li class="nav-item">
        <Link to="/team" class:active={currentPath === '/team'}>Team</Link>
      </li>
      <li class="nav-item">
        <Link to="/contact" class:active={currentPath === '/contact'}>Contact</Link>
      </li>
    </ul>
  </div>
</nav>

