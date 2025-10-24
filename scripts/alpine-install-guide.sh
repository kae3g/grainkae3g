#!/bin/bash
# 🌾 Alpine Linux Installation Guide for Grain Network
# Step-by-step instructions for setting up Alpine → NixOS → SixOS

set -euo pipefail

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

log() {
    echo -e "${GREEN}[$(date +'%Y-%m-%d %H:%M:%S')]${NC} $1"
}

info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

warn() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log "🌾 Alpine Linux Installation Guide for Grain Network"
log "═══════════════════════════════════════════════════════════"

echo ""
info "📋 Installation Steps:"
echo ""
echo "1️⃣  Connect to Alpine VM:"
echo "   sudo virsh console alpine-grain6"
echo ""
echo "2️⃣  Alpine Installation Commands:"
echo "   setup-alpine"
echo "   # Follow prompts:"
echo "   # - Keyboard: us"
echo "   # - Hostname: alpine-grain6"
echo "   # - Interface: eth0"
echo "   # - IP: dhcp"
echo "   # - Root password: (set your password)"
echo "   # - Timezone: America/Los_Angeles"
echo "   # - Proxy: none"
echo "   # - Mirror: (default)"
echo "   # - SSH: openssh"
echo "   # - Disk: sda"
echo "   # - Mode: sys"
echo ""
echo "3️⃣  After Installation:"
echo "   reboot"
echo "   # Wait for VM to restart"
echo "   sudo virsh console alpine-grain6"
echo ""
echo "4️⃣  Post-Installation Setup:"
echo "   apk update"
echo "   apk add bash curl wget git vim"
echo "   # Enable auto-login for root"
echo "   echo 'tty1::respawn:/sbin/getty 38400 tty1' >> /etc/inittab"
echo "   echo 'tty1::respawn:/bin/login -f root tty1 </dev/tty1 >/dev/tty1 2>&1' >> /etc/inittab"
echo ""
echo "5️⃣  Install NixOS within Alpine:"
echo "   # Download NixOS ISO"
echo "   wget https://releases.nixos.org/nixos/25.11/nixos-25.11.20251022.01f116e/nixos-minimal-25.11.20251022.01f116e-x86_64-linux.iso"
echo "   # Mount ISO and install NixOS"
echo "   # (Detailed instructions will be provided)"
echo ""
echo "6️⃣  Deploy SixOS on top:"
echo "   # Install Grain Network services"
echo "   # Configure musl libc compatibility"
echo "   # Test grain6 and grainwifi"
echo ""

log "🎯 Goal: Alpine (musl) → NixOS (declarative) → SixOS (Grain Network)"
log "📚 Philosophy: Granules → Grains → THE WHOLE GRAIN"

echo ""
warn "⚠️  Note: This is a multi-step process. Take your time with each step."
info "💡 Tip: Use 'Ctrl+]' to exit virsh console, 'sudo virsh console alpine-grain6' to reconnect"

echo ""
log "🚀 Ready to start? Run: sudo virsh console alpine-grain6"
