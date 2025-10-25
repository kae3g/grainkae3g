#!/usr/bin/env node

/**
 * NPM Install Script for QB - Universal Quarterback
 * 
 * This script runs when someone does `npm install quarterback`
 * It sets up the grainpbc/qb functionality in their system
 */

const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');

const packageDir = __dirname;
const targetDir = process.cwd();

console.log('🌾 Installing QB - Universal Quarterback for Grain Network...');

// Create personal directory if it doesn't exist
const personalDir = path.join(packageDir, 'personal');
if (!fs.existsSync(personalDir)) {
  fs.mkdirSync(personalDir, { recursive: true });
  console.log('✓ Created personal configuration directory');
}

// Copy template to personal if personal config doesn't exist
const personalConfig = path.join(personalDir, 'config.edn');
const templateConfig = path.join(packageDir, 'template', 'config.edn');

if (!fs.existsSync(personalConfig) && fs.existsSync(templateConfig)) {
  fs.copyFileSync(templateConfig, personalConfig);
  console.log('✓ Initialized personal configuration from template');
}

// Make bin/qb.js executable
const qbBin = path.join(packageDir, 'bin', 'qb.js');
if (fs.existsSync(qbBin)) {
  try {
    fs.chmodSync(qbBin, '755');
    console.log('✓ Made qb command executable');
  } catch (err) {
    console.log('⚠️  Could not make qb executable (this is OK on Windows)');
  }
}

// Check for babashka
try {
  execSync('bb --version', { stdio: 'pipe' });
  console.log('✓ Babashka found');
} catch (err) {
  console.log('⚠️  Babashka not found - please install it for full functionality');
  console.log('   curl -sLO https://raw.githubusercontent.com/babashka/babashka/master/install');
  console.log('   chmod +x install && ./install');
}

console.log('');
console.log('🎉 QB installation complete!');
console.log('');
console.log('Usage:');
console.log('  qb help                    # Show help');
console.log('  qb flow "message"          # Deploy workflow');
console.log('  qb status                  # Show status');
console.log('');
console.log('Philosophy: now == next + 1 🌾');

