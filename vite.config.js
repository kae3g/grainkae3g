import { defineConfig } from 'vite';
import { svelte } from '@sveltejs/vite-plugin-svelte';

export default defineConfig({
  plugins: [svelte()],
  base: '/grainkae3g/',
  build: {
    outDir: 'dist',
    assetsDir: 'assets'
  }
});
