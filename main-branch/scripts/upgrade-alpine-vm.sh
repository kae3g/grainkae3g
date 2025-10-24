#!/bin/bash
# 🌾 Upgrade Alpine VM Resources for Grain Network Development
# Maximizes Alpine VM resources while keeping Ubuntu host healthy

set -euo pipefail

VM_NAME="alpine-grain6"
VM_DIR="/home/xy/kae3g/12025-10/vms/$VM_NAME"
DISK_PATH="$VM_DIR/$VM_NAME.qcow2"

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
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

log "🌾 Upgrading Alpine VM resources for Grain Network development..."

# Check if VM is shut down
if virsh list --state-shutoff | grep -q "$VM_NAME"; then
    log "✅ VM is shut down, proceeding with upgrade..."
else
    warn "⚠️ VM is still running. Shutting down..."
    virsh shutdown "$VM_NAME"
    sleep 10
fi

# Backup current configuration
log "1️⃣ Backing up current VM configuration..."
virsh dumpxml "$VM_NAME" > "$VM_DIR/alpine-grain6-backup.xml"
log "✅ Configuration backed up to $VM_DIR/alpine-grain6-backup.xml"

# Upgrade disk size
log "2️⃣ Upgrading disk from 30GB to 200GB..."
qemu-img resize "$DISK_PATH" 200G
log "✅ Disk upgraded to 200GB"

# Update VM memory configuration
log "3️⃣ Updating VM memory configuration..."
virsh setmaxmem "$VM_NAME" 24G --config
virsh setmem "$VM_NAME" 16G --config
log "✅ Memory updated: 16GB current, 24GB max"

# Update CPU cores
log "4️⃣ Updating CPU cores to 8..."
virsh setvcpus "$VM_NAME" 8 --config --maximum
virsh setvcpus "$VM_NAME" 8 --config
log "✅ CPU cores updated to 8"

# Show updated configuration
log "5️⃣ Updated VM configuration:"
info "Memory: 16GB (max 24GB)"
info "CPU: 8 cores"
info "Disk: 200GB"
info "Host resources remaining: ~44GB RAM, ~649GB disk"

log "✅ Alpine VM upgrade complete!"
log "🚀 Start VM with: sudo virsh start alpine-grain6"
log "📊 Monitor resources with: sudo virsh dominfo alpine-grain6"

# Show resource allocation summary
echo ""
log "🌾 Resource Allocation Summary:"
echo "═══════════════════════════════════════════════════════════════════════════════"
info "Ubuntu Host:     ~44GB RAM, ~649GB disk (healthy margin)"
info "Alpine VM:       16GB RAM, 200GB disk (maximized for development)"
info "Total System:    60GB RAM, 937GB disk"
echo "═══════════════════════════════════════════════════════════════════════════════"
