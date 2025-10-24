import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vite';

const dev = process.env.NODE_ENV === 'development';

export default defineConfig({
  plugins: [sveltekit()],
  base: dev ? '' : '/12025-10/',
  build: {
    outDir: 'build'
  }
});

