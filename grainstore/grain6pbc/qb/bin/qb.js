#!/usr/bin/env node

/**
 * QB - Universal Quarterback for Grain Network
 * NPM distribution of grainpbc/qb
 * 
 * This script provides the same functionality as the grainpbc/qb module
 * but installable via npm install quarterback
 */

const { spawn } = require('child_process');
const path = require('path');
const fs = require('fs');

// Get the directory where this package is installed
const packageDir = path.dirname(__dirname);
const bbEdnPath = path.join(packageDir, 'bb.edn');

// Check if babashka is available
function checkBabashka() {
  return new Promise((resolve) => {
    const bb = spawn('bb', ['--version'], { stdio: 'pipe' });
    bb.on('close', (code) => {
      resolve(code === 0);
    });
  });
}

// Main QB command handler
async function main() {
  const args = process.argv.slice(2);
  
  // Check if babashka is available
  const hasBabashka = await checkBabashka();
  
  if (!hasBabashka) {
    console.log('🌾 QB - Universal Quarterback for Grain Network');
    console.log('');
    console.log('❌ Babashka not found. Please install babashka first:');
    console.log('   curl -sLO https://raw.githubusercontent.com/babashka/babashka/master/install');
    console.log('   chmod +x install && ./install');
    console.log('');
    console.log('Then run: qb help');
    process.exit(1);
  }
  
  // Check if bb.edn exists
  if (!fs.existsSync(bbEdnPath)) {
    console.log('❌ QB configuration not found. Please reinstall the package.');
    process.exit(1);
  }
  
  // Run the babashka task
  const bbArgs = ['-f', bbEdnPath, ...args];
  const bb = spawn('bb', bbArgs, { 
    stdio: 'inherit',
    cwd: packageDir 
  });
  
  bb.on('close', (code) => {
    process.exit(code);
  });
  
  bb.on('error', (err) => {
    console.error('Error running babashka:', err);
    process.exit(1);
  });
}

// Handle help command
if (process.argv.length === 2 || process.argv[2] === 'help' || process.argv[2] === '--help') {
  console.log('🌾 QB - Universal Quarterback for Grain Network');
  console.log('');
  console.log('Usage: qb <command> [args...]');
  console.log('');
  console.log('Commands:');
  console.log('  help                    Show this help message');
  console.log('  flow [message]          Complete deployment workflow');
  console.log('  status                  Show current status');
  console.log('  config:show             Show configuration');
  console.log('  config:update           Update configuration');
  console.log('  template:sync           Sync template to personal');
  console.log('  personal:backup         Backup personal configuration');
  console.log('  personal:restore        Restore from backup');
  console.log('  deploy:github           Deploy to GitHub Pages');
  console.log('  deploy:codeberg         Deploy to Codeberg Pages');
  console.log('  deploy:dual             Dual deploy to both');
  console.log('  grain:sync              Sync with Grain Network');
  console.log('  grain:status            Show Grain Network status');
  console.log('');
  console.log('Examples:');
  console.log('  qb flow "Session 815: Universal quarterback"');
  console.log('  qb status');
  console.log('  qb deploy:dual');
  console.log('');
  console.log('Philosophy: now == next + 1 🌾');
  process.exit(0);
}

main().catch(console.error);

