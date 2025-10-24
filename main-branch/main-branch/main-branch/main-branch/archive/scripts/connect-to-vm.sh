#!/usr/bin/env bash
# connect-to-vm.sh
# Convenient wrapper for SSH into the Robotic Farm VM
# 
# Usage: ./scripts/connect-to-vm.sh
#
# Timestamp: 12025-10-05--06thhouse01984

set -euo pipefail

VM_NAME="${VM_NAME:-robotic-farm-nixos}"
SSH_PORT="${SSH_PORT:-2222}"
SSH_USER="${SSH_USER:-nixos}"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

# Check if VM is running
if ! VBoxManage list runningvms | grep -q "${VM_NAME}"; then
    echo -e "${RED}✗${NC} VM '${VM_NAME}' is not running"
    echo ""
    echo "Start it with one of:"
    echo "  VBoxManage startvm ${VM_NAME} --type gui      # With window"
    echo "  VBoxManage startvm ${VM_NAME} --type headless # Background"
    exit 1
fi

# Connect
echo -e "${BLUE}🔗${NC} Connecting to ${VM_NAME}..."
ssh -p ${SSH_PORT} ${SSH_USER}@localhost

