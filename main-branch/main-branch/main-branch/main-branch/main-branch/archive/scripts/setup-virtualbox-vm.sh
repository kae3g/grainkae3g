#!/usr/bin/env bash
# setup-virtualbox-vm.sh
# Automated VirtualBox NixOS VM setup for Robotic Farm project
# 
# Usage: ./scripts/setup-virtualbox-vm.sh
#
# Timestamp: 12025-10-05--06thhouse01984

set -euo pipefail

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
VM_NAME="${VM_NAME:-robotic-farm-nixos}"
VM_MEMORY="${VM_MEMORY:-4096}"
VM_CPUS="${VM_CPUS:-2}"
VM_DISK_SIZE="${VM_DISK_SIZE:-30720}"
NIXOS_VERSION="${NIXOS_VERSION:-24.05}"

# Paths
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
VM_DIR="$HOME/VirtualBox/VMs"
ISO_DIR="$HOME/VirtualBox/ISOs"
ISO_NAME="nixos-minimal-${NIXOS_VERSION}-x86_64-linux.iso"
ISO_PATH="${ISO_DIR}/${ISO_NAME}"

# Functions
log_info() {
    echo -e "${BLUE}ℹ${NC} $1"
}

log_success() {
    echo -e "${GREEN}✓${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}⚠${NC} $1"
}

log_error() {
    echo -e "${RED}✗${NC} $1"
}

check_prerequisites() {
    log_info "Checking prerequisites..."
    
    if ! command -v VBoxManage &> /dev/null; then
        log_error "VirtualBox not found. Install with: brew install --cask virtualbox"
        exit 1
    fi
    
    log_success "VirtualBox found: $(VBoxManage --version)"
}

download_nixos_iso() {
    if [[ -f "$ISO_PATH" ]]; then
        log_info "NixOS ISO already downloaded: $ISO_PATH"
        return 0
    fi
    
    log_info "Downloading NixOS minimal ISO..."
    mkdir -p "$ISO_DIR"
    
    local ISO_URL="https://channels.nixos.org/nixos-${NIXOS_VERSION}/latest-nixos-minimal-x86_64-linux.iso"
    
    if command -v curl &> /dev/null; then
        curl -# -L -o "$ISO_PATH" "$ISO_URL"
    elif command -v wget &> /dev/null; then
        wget -O "$ISO_PATH" "$ISO_URL"
    else
        log_error "Neither curl nor wget found. Please install one."
        exit 1
    fi
    
    log_success "ISO downloaded: $ISO_PATH"
}

create_vm() {
    if VBoxManage list vms | grep -q "\"${VM_NAME}\""; then
        log_warning "VM '${VM_NAME}' already exists"
        read -p "Delete and recreate? (y/N): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            log_info "Deleting existing VM..."
            VBoxManage unregistervm "${VM_NAME}" --delete 2>/dev/null || true
        else
            log_info "Using existing VM"
            return 0
        fi
    fi
    
    log_info "Creating VM: ${VM_NAME}"
    
    # Create VM
    VBoxManage createvm \
        --name "${VM_NAME}" \
        --ostype "Linux_64" \
        --register \
        --basefolder "${VM_DIR}"
    
    # Configure resources
    VBoxManage modifyvm "${VM_NAME}" \
        --memory "${VM_MEMORY}" \
        --cpus "${VM_CPUS}" \
        --vram 16 \
        --boot1 dvd \
        --boot2 disk \
        --boot3 none \
        --boot4 none \
        --firmware bios \
        --chipset piix3 \
        --pae on \
        --longmode on \
        --hpet on \
        --hwvirtex on \
        --nestedpaging on \
        --largepages on \
        --vtxvpid on \
        --accelerate3d off \
        --accelerate2dvideo off
    
    # Create and attach disk
    local VM_DISK="${VM_DIR}/${VM_NAME}/${VM_NAME}.vdi"
    VBoxManage createhd \
        --filename "${VM_DISK}" \
        --size "${VM_DISK_SIZE}" \
        --format VDI
    
    VBoxManage storagectl "${VM_NAME}" \
        --name "SATA Controller" \
        --add sata \
        --controller IntelAhci \
        --portcount 2 \
        --hostiocache off \
        --bootable on
    
    VBoxManage storageattach "${VM_NAME}" \
        --storagectl "SATA Controller" \
        --port 0 \
        --device 0 \
        --type hdd \
        --medium "${VM_DISK}"
    
    # Attach ISO
    VBoxManage storagectl "${VM_NAME}" \
        --name "IDE Controller" \
        --add ide
    
    VBoxManage storageattach "${VM_NAME}" \
        --storagectl "IDE Controller" \
        --port 0 \
        --device 0 \
        --type dvddrive \
        --medium "${ISO_PATH}"
    
    # Configure networking (NAT with port forwarding)
    VBoxManage modifyvm "${VM_NAME}" \
        --nic1 nat \
        --natpf1 "ssh,tcp,,2222,,22" \
        --natpf1 "http,tcp,,8080,,8080" \
        --natpf1 "https,tcp,,8443,,443"
    
    # Enable clipboard and drag & drop
    VBoxManage modifyvm "${VM_NAME}" \
        --clipboard-mode bidirectional \
        --draganddrop bidirectional
    
    log_success "VM created successfully"
}

setup_shared_folder() {
    log_info "Setting up shared folder for project..."
    
    # Remove existing shared folder if present
    VBoxManage sharedfolder remove "${VM_NAME}" --name "robotic-farm" 2>/dev/null || true
    
    # Add shared folder
    VBoxManage sharedfolder add "${VM_NAME}" \
        --name "robotic-farm" \
        --hostpath "${PROJECT_ROOT}" \
        --automount \
        --auto-mount-point "/home/nixos/robotic-farm"
    
    log_success "Shared folder configured"
}

print_next_steps() {
    cat << EOF

╔════════════════════════════════════════════════════════════════════════════╗
║                        VM CREATED SUCCESSFULLY                             ║
╚════════════════════════════════════════════════════════════════════════════╝

Next Steps:

1. Start the VM:
   VBoxManage startvm ${VM_NAME} --type gui

2. Inside the VM, install NixOS:
   Follow the installation guide or use the automated script:
   ./scripts/install-nixos-in-vm.sh

3. After installation, SSH into the VM:
   ssh -p 2222 nixos@localhost
   
4. Or add to ~/.ssh/config:
   
   Host robotic-farm-vm
     HostName localhost
     Port 2222
     User nixos
     IdentityFile ~/.ssh/id_ed25519_nixos_vm

   Then: ssh robotic-farm-vm

5. Inside VM, enter development shell:
   cd ~/robotic-farm
   nix develop

VM Details:
  Name: ${VM_NAME}
  RAM: ${VM_MEMORY} MB
  CPUs: ${VM_CPUS}
  Disk: ${VM_DISK_SIZE} MB
  
Port Forwarding:
  SSH:   localhost:2222  → VM:22
  HTTP:  localhost:8080  → VM:8080
  HTTPS: localhost:8443  → VM:443

Shared Folder:
  Host: ${PROJECT_ROOT}
  Guest: /home/nixos/robotic-farm

Documentation: ./writings/9996-nixos-virtualbox-macos.md

L'dor v'dor — From generation to generation

EOF
}

# Main execution
main() {
    echo ""
    log_info "Robotic Farm: VirtualBox NixOS VM Setup"
    log_info "=========================================="
    echo ""
    
    check_prerequisites
    download_nixos_iso
    create_vm
    setup_shared_folder
    print_next_steps
}

main "$@"

