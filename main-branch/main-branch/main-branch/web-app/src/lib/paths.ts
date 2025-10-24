import { base } from '$app/paths';

/**
 * Generate a path with the correct base path for the current environment
 * @param path - The path to normalize (should start with /)
 * @returns The path with the correct base path prepended
 */
export const to = (path: string): string => {
  // Ensure path starts with /
  const normalizedPath = path.startsWith('/') ? path : `/${path}`;
  return `${base}${normalizedPath}`;
};

/**
 * Generate an internal link that SvelteKit will intercept for SPA navigation
 * @param path - The path to link to
 * @returns The normalized path for internal navigation
 */
export const link = (path: string): string => {
  return to(path);
};
