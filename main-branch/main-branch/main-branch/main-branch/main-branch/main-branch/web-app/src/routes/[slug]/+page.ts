import { error } from '@sveltejs/kit';
import type { PageLoad } from './$types';

export const prerender = true;

export const load: PageLoad = async ({ params, fetch }) => {
  // Strip .html extension if present (for static hosting)
  const slug = params.slug.replace(/\.html$/, '');
  const response = await fetch(`./content/${slug}.json`);
  
  if (!response.ok) {
    throw error(404, `Writing not found: ${slug}`);
  }
  
  const entry = await response.json();
  return { entry };
};