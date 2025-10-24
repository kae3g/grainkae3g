#!/bin/bash
# QEMU/KVM Virtual Machine Manager
# A comprehensive script for managing VMs in your virtualization environment

set -euo pipefail

# Configuration
VM_DIR="/home/xy/kae3g/12025-10/vms"
ISO_DIR="/home/xy/kae3g/12025-10/isos"
LOG_DIR="/home/xy/kae3g/12025-10/logs"
BACKUP_DIR="/home/xy/kae3g/12025-10/backups"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Logging function
log() {
    echo -e "${GREEN}[$(date +'%Y-%m-%d %H:%M:%S')]${NC} $1" | tee -a "$LOG_DIR/vm-manager.log"
}

error() {
    echo -e "${RED}[ERROR]${NC} $1" | tee -a "$LOG_DIR/vm-manager.log"
}

warn() {
    echo -e "${YELLOW}[WARNING]${NC} $1" | tee -a "$LOG_DIR/vm-manager.log"
}

info() {
    echo -e "${BLUE}[INFO]${NC} $1" | tee -a "$LOG_DIR/vm-manager.log"
}

# Function to list all VMs
list_vms() {
    log "Listing all virtual machines..."
    if virsh list --all; then
        log "VM list retrieved successfully"
    else
        error "Failed to list VMs"
        return 1
    fi
}

# Function to create a new VM
create_vm() {
    local vm_name="$1"
    local memory="$2"
    local disk_size="$3"
    local iso_path="$4"
    local cpu_cores="$5"
    
    log "Creating VM: $vm_name"
    log "Memory: ${memory}MB, Disk: ${disk_size}GB, CPU Cores: $cpu_cores"
    
    # Create VM directory
    mkdir -p "$VM_DIR/$vm_name"
    
    # Create disk image
    local disk_path="$VM_DIR/$vm_name/${vm_name}.qcow2"
    log "Creating disk image: $disk_path"
    qemu-img create -f qcow2 "$disk_path" "${disk_size}G"
    
    # Create VM using virt-install
    virt-install \
        --name "$vm_name" \
        --memory "$memory" \
        --vcpus "$cpu_cores" \
        --disk path="$disk_path",format=qcow2 \
        --cdrom "$iso_path" \
        --network network=default \
        --graphics vnc,listen=0.0.0.0 \
        --noautoconsole \
        --import \
        --os-variant auto
    
    if [ $? -eq 0 ]; then
        log "VM '$vm_name' created successfully!"
        info "VM can be managed with: virsh start $vm_name"
        info "Connect via VNC or virt-manager"
    else
        error "Failed to create VM '$vm_name'"
        return 1
    fi
}

# Function to start a VM
start_vm() {
    local vm_name="$1"
    log "Starting VM: $vm_name"
    if virsh start "$vm_name"; then
        log "VM '$vm_name' started successfully"
    else
        error "Failed to start VM '$vm_name'"
        return 1
    fi
}

# Function to stop a VM
stop_vm() {
    local vm_name="$1"
    log "Stopping VM: $vm_name"
    if virsh shutdown "$vm_name"; then
        log "VM '$vm_name' shutdown initiated"
    else
        error "Failed to stop VM '$vm_name'"
        return 1
    fi
}

# Function to delete a VM
delete_vm() {
    local vm_name="$1"
    log "Deleting VM: $vm_name"
    
    # Stop VM if running
    if virsh list --state-running | grep -q "$vm_name"; then
        warn "VM is running, shutting down first..."
        virsh shutdown "$vm_name"
        sleep 5
    fi
    
    # Undefine VM
    if virsh undefine "$vm_name"; then
        log "VM '$vm_name' undefined successfully"
    else
        error "Failed to undefine VM '$vm_name'"
        return 1
    fi
    
    # Remove VM directory
    if [ -d "$VM_DIR/$vm_name" ]; then
        rm -rf "$VM_DIR/$vm_name"
        log "VM directory removed: $VM_DIR/$vm_name"
    fi
}

# Function to backup a VM
backup_vm() {
    local vm_name="$1"
    local backup_name="${vm_name}_backup_$(date +%Y%m%d_%H%M%S)"
    
    log "Creating backup for VM: $vm_name"
    
    # Create backup directory
    mkdir -p "$BACKUP_DIR/$backup_name"
    
    # Export VM configuration
    virsh dumpxml "$vm_name" > "$BACKUP_DIR/$backup_name/${vm_name}.xml"
    
    # Copy disk image
    local disk_path="$VM_DIR/$vm_name/${vm_name}.qcow2"
    if [ -f "$disk_path" ]; then
        cp "$disk_path" "$BACKUP_DIR/$backup_name/"
        log "Backup created: $BACKUP_DIR/$backup_name"
    else
        error "Disk image not found: $disk_path"
        return 1
    fi
}

# Function to show VM status
vm_status() {
    local vm_name="$1"
    log "Status for VM: $vm_name"
    virsh dominfo "$vm_name"
}

# Function to show help
show_help() {
    cat << EOF
QEMU/KVM Virtual Machine Manager

Usage: $0 <command> [options]

Commands:
    list                    List all VMs
    create <name> <memory> <disk> <iso> <cores>  Create a new VM
    start <name>            Start a VM
    stop <name>             Stop a VM
    delete <name>           Delete a VM
    backup <name>           Backup a VM
    status <name>           Show VM status
    help                    Show this help

Examples:
    $0 list
    $0 create ubuntu-server 2048 20 /path/to/ubuntu.iso 2
    $0 start ubuntu-server
    $0 stop ubuntu-server
    $0 backup ubuntu-server
    $0 status ubuntu-server
    $0 delete ubuntu-server

EOF
}

# Main script logic
main() {
    # Create log directory if it doesn't exist
    mkdir -p "$LOG_DIR"
    
    case "${1:-help}" in
        "list")
            list_vms
            ;;
        "create")
            if [ $# -ne 6 ]; then
                error "Usage: $0 create <name> <memory> <disk> <iso> <cores>"
                exit 1
            fi
            create_vm "$2" "$3" "$4" "$5" "$6"
            ;;
        "start")
            if [ $# -ne 2 ]; then
                error "Usage: $0 start <name>"
                exit 1
            fi
            start_vm "$2"
            ;;
        "stop")
            if [ $# -ne 2 ]; then
                error "Usage: $0 stop <name>"
                exit 1
            fi
            stop_vm "$2"
            ;;
        "delete")
            if [ $# -ne 2 ]; then
                error "Usage: $0 delete <name>"
                exit 1
            fi
            delete_vm "$2"
            ;;
        "backup")
            if [ $# -ne 2 ]; then
                error "Usage: $0 backup <name>"
                exit 1
            fi
            backup_vm "$2"
            ;;
        "status")
            if [ $# -ne 2 ]; then
                error "Usage: $0 status <name>"
                exit 1
            fi
            vm_status "$2"
            ;;
        "help"|*)
            show_help
            ;;
    esac
}

# Run main function with all arguments
main "$@"





