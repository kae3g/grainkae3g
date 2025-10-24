<script>
  import '../lib/theme.css';
  import ThemeToggle from '$lib/ThemeToggle.svelte';
  import PaletteToggle from '$lib/PaletteToggle.svelte';
  import LanguageIndicator from '$lib/LanguageIndicator.svelte';
  import UsernameIndicator from '$lib/UsernameIndicator.svelte';
</script>

<LanguageIndicator />
<UsernameIndicator />
<PaletteToggle />
<ThemeToggle />
<slot />

