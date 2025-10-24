import adapter from '@sveltejs/adapter-static';

const dev = process.env.NODE_ENV === 'development';

/** @type {import('@sveltejs/kit').Config} */
const config = {
  kit: {
    paths: {
      base: dev ? '' : '/12025-10'
    },
    adapter: adapter({
      pages: 'build',
      assets: 'build',
      fallback: '404.html',
      precompress: false,
      strict: true
    }),
    prerender: {
      handleHttpError: ({ status, path, referrer, message }) => {
        // Ignore errors for links to .md files or non-existent pages
        if (status === 404 || status === 500) {
          console.warn(`${status} ${path} (from ${referrer}) - skipping`);
          return;
        }
        // Throw on other errors
        throw new Error(message);
      },
      handleMissingId: ({ path, id, referrers }) => {
        // Ignore missing anchor links - they're common in markdown
        console.warn(`Missing anchor #${id} in ${path} (from ${referrers.join(', ')}) - skipping`);
        return;
      },
      handleUnseenRoutes: (path) => {
        // Skip any routes that aren't in our content
        console.warn(`Skipping unseen route: ${path}`);
        return false;
      }
    }
  }
};

export default config;
