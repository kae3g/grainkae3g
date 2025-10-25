#!/usr/bin/env node

/**
 * NPM Post-Install Script for QB - Universal Quarterback
 * 
 * This script runs after npm install quarterback completes
 * It performs final setup and validation
 */

const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');

const packageDir = __dirname;

console.log('🌾 QB Post-Install: Finalizing setup...');

// Verify installation
const requiredFiles = [
  'bb.edn',
  'bin/qb.js',
  'template/config.edn',
  'README.md'
];

let allFilesExist = true;
for (const file of requiredFiles) {
  const filePath = path.join(packageDir, file);
  if (!fs.existsSync(filePath)) {
    console.log(`❌ Missing required file: ${file}`);
    allFilesExist = false;
  }
}

if (!allFilesExist) {
  console.log('❌ QB installation incomplete. Please reinstall.');
  process.exit(1);
}

// Test babashka integration
try {
  const bbEdnPath = path.join(packageDir, 'bb.edn');
  execSync(`bb -f "${bbEdnPath}" qb:help`, { 
    stdio: 'pipe',
    cwd: packageDir 
  });
  console.log('✓ Babashka integration working');
} catch (err) {
  console.log('⚠️  Babashka integration test failed (this is OK if babashka is not installed)');
}

// Create .gitignore for personal directory
const gitignorePath = path.join(packageDir, 'personal', '.gitignore');
if (!fs.existsSync(gitignorePath)) {
  const gitignoreContent = `# Personal QB configuration
config.edn
backups/
*.log
`;
  fs.writeFileSync(gitignorePath, gitignoreContent);
  console.log('✓ Created .gitignore for personal directory');
}

console.log('');
console.log('✅ QB - Universal Quarterback ready!');
console.log('');
console.log('Quick start:');
console.log('  qb help                    # Show all commands');
console.log('  qb flow "Hello QB!"       # Test deployment flow');
console.log('  qb status                 # Check status');
console.log('');
console.log('This NPM package provides the exact same functionality');
console.log('as the grainpbc/qb module, installable via npm!');
console.log('');
console.log('Philosophy: now == next + 1 🌾');

