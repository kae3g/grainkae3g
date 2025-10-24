#!/bin/bash
# 🌾 Alpine Linux Automated Installation Script
# Automated setup for Grain Network virtualization architecture

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

log "🌾 Alpine Linux Automated Installation for Grain Network"
log "═══════════════════════════════════════════════════════════"

echo ""
info "📋 Installation Commands (run these in Alpine VM):"
echo ""

echo "# 1️⃣ Start Alpine installation"
echo "setup-alpine"
echo ""
echo "# 2️⃣ Follow these prompts:"
echo "# Keyboard: us"
echo "# Hostname: alpine-grain6"
echo "# Interface: eth0"
echo "# IP: dhcp"
echo "# Root password: (set your password)"
echo "# Timezone: America/Los_Angeles"
echo "# Proxy: none"
echo "# Mirror: (default)"
echo "# SSH: openssh"
echo "# Disk: sda"
echo "# Mode: sys"
echo ""

echo "# 3️⃣ After installation completes:"
echo "reboot"
echo ""

echo "# 4️⃣ After reboot, reconnect and run:"
echo "sudo virsh console alpine-grain6"
echo ""

echo "# 5️⃣ Post-installation setup:"
echo "apk update"
echo "apk add bash curl wget git vim"
echo ""

echo "# 6️⃣ Enable auto-login for root:"
echo "echo 'tty1::respawn:/bin/login -f root tty1 </dev/tty1 >/dev/tty1 2>&1' >> /etc/inittab"
echo ""

echo "# 7️⃣ Install NixOS within Alpine (next step):"
echo "# Download NixOS ISO"
echo "wget https://releases.nixos.org/nixos/25.11/nixos-25.11.20251022.01f116e/nixos-minimal-25.11.20251022.01f116e-x86_64-linux.iso"
echo ""

log "🎯 Goal: Alpine (musl) → NixOS (declarative) → SixOS (Grain Network)"
log "📚 Philosophy: Granules → Grains → THE WHOLE GRAIN"

echo ""
warn "⚠️  Note: Take your time with each step. The installation will guide you."
info "💡 Tip: Use 'Ctrl+]' to exit virsh console, 'sudo virsh console alpine-grain6' to reconnect"

echo ""
log "🚀 Ready to start? Run: setup-alpine"
