#!/bin/bash

# Grainbranch Creator for Grain6PBC Repositories
# Creates grainbranches, sets them as default, and updates descriptions

get_current_graintime() {
    local graintime="12025-10-24-$(date +%H%M)"
    echo "$graintime"
}

create_grainbranch_for_repo() {
    local repo_name="$1"
    local description="$2"
    
    echo "🌾 Creating grainbranch for $repo_name..."
    
    local graintime=$(get_current_graintime)
    local clean_description=$(echo "$description" | sed 's/[^a-zA-Z0-9\s-]//g' | sed 's/\s\+/-/g' | tr '[:upper:]' '[:lower:]')
    local grainbranch_name="${graintime}-${clean_description}"
    
    echo "📝 Grainbranch name: $grainbranch_name"
    
    # Clone repository temporarily
    cd /tmp
    git clone "https://github.com/grain6pbc/$repo_name" "temp-$repo_name"
    cd "temp-$repo_name"
    
    # Create grainbranch
    git checkout -b "$grainbranch_name"
    
    # Create grainbranch metadata
    cat > grainbranch.md << GRAINBRANCH_EOF
# Grainbranch: $grainbranch_name

**Repository**: $repo_name
**Description**: $description
**Created**: $(date -u +%Y-%m-%dT%H:%M:%SZ)
**Graintime**: $graintime

This grainbranch represents an immutable version of the $repo_name repository.
It serves as a stable reference point for documentation, courses, and deployment.

## Usage

This grainbranch can be used for:
- Documentation generation
- Course material creation
- Stable deployment references
- Version pinning for dependencies

## Integration

This grainbranch integrates with:
- Grain6PBC organization structure
- GrainNetwork documentation system
- GrainCourse educational platform
- GrainDaemon deployment automation
GRAINBRANCH_EOF
    
    # Commit and push grainbranch
    git add grainbranch.md
    git commit -m "🌾 Create grainbranch: $grainbranch_name"
    git push origin "$grainbranch_name"
    
    # Set as default branch via GitHub API
    gh api "repos/grain6pbc/$repo_name" --method PATCH --field "default_branch=$grainbranch_name"
    
    # Update repository description with grainbranch URL
    local grainbranch_url="https://github.com/grain6pbc/$repo_name/tree/$grainbranch_name"
    gh api "repos/grain6pbc/$repo_name" --method PATCH --field "description=$description | Grainbranch: $grainbranch_url"
    
    # Clean up
    cd /tmp
    rm -rf "temp-$repo_name"
    
    echo "✅ Created grainbranch: $grainbranch_name"
    echo "📁 URL: $grainbranch_url"
    echo "🌐 Set as default branch with updated description!"
    echo ""
}

# Create grainbranches for key repositories
create_grainbranch_for_repo "grainbarrel" "Grain Network Build System"
create_grainbranch_for_repo "grainpbc" "Public Benefit Corporation Framework"
create_grainbranch_for_repo "grainzsh" "Grain Network Zsh Configuration"
create_grainbranch_for_repo "grainenvvars" "Environment Variables Management"
create_grainbranch_for_repo "humble-social-client" "Humble UI Social Client"
create_grainbranch_for_repo "clojure-dfinity" "Clojure DFINITY Integration"
create_grainbranch_for_repo "grainsource-sway" "Sway Configuration Archive"
create_grainbranch_for_repo "grainsource-gnome" "GNOME Configuration"
create_grainbranch_for_repo "grainaltproteinproject" "Alternative Protein Project"
create_grainbranch_for_repo "clojure-google-drive-mcp" "Google Drive MCP Bindings"
create_grainbranch_for_repo "graindrive" "Clojure Google Drive Integration"
create_grainbranch_for_repo "grainlexicon" "Documentation Vocabulary"
create_grainbranch_for_repo "grainneovedic" "Neovedic Timestamp System"
create_grainbranch_for_repo "grainwifi" "Dual-Wifi Connection Manager"
create_grainbranch_for_repo "grain-nightlight" "GNOME Night Light Daemon"
create_grainbranch_for_repo "grainicons" "Icon Management Library"
create_grainbranch_for_repo "graincasks" "AppImage Package Manager"
create_grainbranch_for_repo "graindisplay" "Universal Display Management"
create_grainbranch_for_repo "graindroid" "Open-Hardware Android Phone"
create_grainbranch_for_repo "graindaemon" "S6/SixOS Daemon Framework"
create_grainbranch_for_repo "urbit-source" "Urbit Source Code"
create_grainbranch_for_repo "grainnixos-qemu-ubuntu-framework16" "NixOS QEMU Template"
create_grainbranch_for_repo "grainstore" "Verified Dependencies Management"
create_grainbranch_for_repo "grainnetwork" "Main Grain Network Documentation"
create_grainbranch_for_repo "grainsource" "Git-Compatible Decentralized VCS"
create_grainbranch_for_repo "grainpack" "External GPU Jetpack"
create_grainbranch_for_repo "graincamera" "AVIF-Compatible Mirrorless Camera"
create_grainbranch_for_repo "grainwriter" "RAM-Only E-Ink Writing Device"
create_grainbranch_for_repo "grainclay" "Networked Package Manager"
create_grainbranch_for_repo "grainconv" "Universal Type Conversion System"
create_grainbranch_for_repo "grainmusic" "Decentralized Music Streaming"
create_grainbranch_for_repo "grainspace" "Unified Decentralized Platform"
create_grainbranch_for_repo "grainweb" "Browser Git Explorer AI Atlas"
create_grainbranch_for_repo "grain-metatypes" "Foundational Type Definitions"
create_grainbranch_for_repo "clotoko" "Clojure-to-Motoko Transpiler"
create_grainbranch_for_repo "clojure-icp" "ICP DFINITY Integration"
create_grainbranch_for_repo "clojure-sixos" "SixOS Integration"
create_grainbranch_for_repo "clojure-s6" "S6 Supervision Wrapper"

echo "🎉 Grainbranch creation complete!"
echo "📊 All grainbranches are now default branches with updated descriptions!"
