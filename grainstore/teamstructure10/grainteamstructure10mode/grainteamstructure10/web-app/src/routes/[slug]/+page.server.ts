import fs from 'fs';
import path from 'path';

export async function entries() {
  // Read the content directory to get all available slugs
  const contentDir = path.join(process.cwd(), 'static/content');
  const files = fs.readdirSync(contentDir);
  
  // Filter for JSON files and extract slugs
  const slugs = files
    .filter(file => file.endsWith('.json') && file !== 'index.json')
    .map(file => file.replace('.json', ''));
  
  return slugs.map(slug => ({ slug }));
}
