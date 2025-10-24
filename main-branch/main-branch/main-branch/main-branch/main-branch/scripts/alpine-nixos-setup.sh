#!/bin/bash
# 🌾 Alpine Linux → NixOS → SixOS Setup Script
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

error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

warn() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

# Configuration
VM_NAME="alpine-grain6"
MEMORY="4096"
DISK_SIZE="30"
CPU_CORES="4"
ISO_PATH="$HOME/Downloads/alpine-standard-3.19.1-x86_64.iso"
VM_DIR="$HOME/kae3g/12025-10/vms/$VM_NAME"

log "🌾 Starting Alpine Linux → NixOS → SixOS setup..."

# Step 1: Create VM directory
log "1️⃣ Creating VM directory..."
mkdir -p "$VM_DIR"

# Step 2: Create disk image
log "2️⃣ Creating disk image..."
qemu-img create -f qcow2 "$VM_DIR/$VM_NAME.qcow2" "${DISK_SIZE}G"

# Step 3: Create Alpine VM
log "3️⃣ Creating Alpine VM..."
sudo virt-install \
    --name "$VM_NAME" \
    --memory "$MEMORY" \
    --vcpus "$CPU_CORES" \
    --disk path="$VM_DIR/$VM_NAME.qcow2",format=qcow2 \
    --cdrom "$ISO_PATH" \
    --network bridge=virbr0 \
    --graphics vnc,listen=0.0.0.0 \
    --noautoconsole \
    --import

log "✅ Alpine VM created successfully!"
log "🔗 Connect with: sudo virsh console $VM_NAME"
log "📋 Next steps:"
log "   1. Complete Alpine installation"
log "   2. Install NixOS within Alpine"
log "   3. Deploy SixOS on top"
log "   4. Test Grain Network services"

info "VM Name: $VM_NAME"
info "Memory: ${MEMORY}MB"
info "Disk: ${DISK_SIZE}GB"
info "CPU Cores: $CPU_CORES"
info "ISO: $ISO_PATH"
info "VM Directory: $VM_DIR"
