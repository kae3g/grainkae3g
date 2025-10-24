#!/bin/bash
# 🌾 Grain6pbc Repository Setup Script
# Sets up all grain6pbc repositories with grainbranches and GitHub Pages

set -euo pipefail

# Configuration
GRAIN6PBC_ORG="grain6pbc"
REPOS=("grain6" "grainkae3g" "graincontacts" "humble-stack" "graindaemon")

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Logging function
log() {
    echo -e "${GREEN}[$(date +'%Y-%m-%d %H:%M:%S')]${NC} $1"
}

error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

warn() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log "🌾 Setting up Grain6pbc repositories with grainbranches..."

# Function to create repository if it doesn't exist
create_repo_if_not_exists() {
    local repo_name="$1"
    local repo_full_name="$GRAIN6PBC_ORG/$repo_name"
    
    if gh repo view "$repo_full_name" >/dev/null 2>&1; then
        info "Repository $repo_full_name already exists"
    else
        log "Creating repository $repo_full_name..."
        gh repo create "$repo_full_name" --public --description "🌾 Grain6pbc $repo_name: Grain Network repository"
    fi
}

# Function to set up grainbranch for a repository
setup_grainbranch() {
    local repo_name="$1"
    local repo_full_name="$GRAIN6PBC_ORG/$repo_name"
    
    log "Setting up grainbranch for $repo_full_name..."
    
    # Get current graintime
    local graintime
    if command -v gt >/dev/null 2>&1; then
        graintime=$(gt now kae3g)
    else
        graintime="12025-10-24--1200--PDT--moon-vishakha------asc-gem000--sun-12th--kae3g"
        warn "gt command not found, using fallback graintime"
    fi
    
    local grainbranch_name="${repo_name}-${graintime}"
    local grainsite_url="https://$(echo $repo_name | tr '[:upper:]' '[:lower:]').grain6pbc.com/${grainbranch_name}/"
    
    info "Grainbranch: $grainbranch_name"
    info "Grainsite URL: $grainsite_url"
    
    # Clone repository
    if [ -d "temp-${repo_name}" ]; then
        rm -rf "temp-${repo_name}"
    fi
    
    git clone "https://github.com/${repo_full_name}.git" "temp-${repo_name}"
    cd "temp-${repo_name}"
    
    # Create grainbranch
    git checkout -b "$grainbranch_name"
    
    # Add template files if repository is empty
    if [ ! -f "README.md" ]; then
        log "Adding template files to $repo_full_name..."
        
        # Copy template files from grain6
        if [ -d "../../grain6-template" ]; then
            cp -r ../../grain6-template/* .
            cp -r ../../grain6-template/.* . 2>/dev/null || true
        fi
        
        # Update repository-specific information
        sed -i "s/grain6pbc\/grain6/$GRAIN6PBC_ORG\/$repo_name/g" README.md
        sed -i "s/grain6/$repo_name/g" README.md
        sed -i "s/Grain6/Grain6pbc $repo_name/g" README.md
    fi
    
    # Commit changes
    git add .
    git commit -m "feat: Create grainbranch $grainbranch_name" || true
    
    # Push grainbranch
    git push origin "$grainbranch_name"
    
    # Update repository description
    gh api "repos/$repo_full_name" --method PATCH \
        --field "description=🌾 Grain6pbc $repo_name: $grainbranch_name | Live Site: $grainsite_url | Session 780 Complete"
    
    # Set grainbranch as default (requires admin permissions)
    warn "Setting grainbranch as default requires admin permissions"
    info "Manually set default branch to: $grainbranch_name"
    info "Or use GitHub Actions workflow: .github/workflows/set-default-branch.yml"
    
    # Cleanup
    cd ..
    rm -rf "temp-${repo_name}"
    
    log "✅ Set up grainbranch for $repo_full_name"
}

# Main execution
log "Creating repositories..."
for repo in "${REPOS[@]}"; do
    create_repo_if_not_exists "$repo"
done

log "Setting up grainbranches..."
for repo in "${REPOS[@]}"; do
    setup_grainbranch "$repo"
done

log "🎉 Grain6pbc repository setup complete!"
log "📋 Repositories created:"
for repo in "${REPOS[@]}"; do
    info "  - $GRAIN6PBC_ORG/$repo"
done

log "🌐 Custom Domain URLs:"
for repo in "${REPOS[@]}"; do
    local graintime
    if command -v gt >/dev/null 2>&1; then
        graintime=$(gt now kae3g)
    else
        graintime="12025-10-24--1200--PDT--moon-vishakha------asc-gem000--sun-12th--kae3g"
    fi
    local grainbranch_name="${repo}-${graintime}"
    local subdomain=$(echo $repo | tr '[:upper:]' '[:lower:]')
    info "  - https://${subdomain}.grain6pbc.com/${grainbranch_name}/"
done

warn "⚠️ Remember to:"
warn "  1. Set grainbranches as default branches manually"
warn "  2. Enable GitHub Pages for each repository"
warn "  3. Configure custom domains if needed"

log "🚀 Next steps:"
info "  1. Run: bb graindaemon:sync-all-repos"
info "  2. Use GitHub Actions to set default branches"
info "  3. Test GitHub Pages deployment"
