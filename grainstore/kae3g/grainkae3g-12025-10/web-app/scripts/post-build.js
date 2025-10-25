#!/usr/bin/env node

import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Copy our custom 404.html to the build directory
const sourcePath = path.join(__dirname, '..', 'static', '404.html');
const destPath = path.join(__dirname, '..', 'build', '404.html');

console.log('📄 Copying custom 404.html to build directory...');

try {
  fs.copyFileSync(sourcePath, destPath);
  console.log('✅ Custom 404.html copied successfully');
} catch (error) {
  console.error('❌ Error copying 404.html:', error.message);
  process.exit(1);
}
