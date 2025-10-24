#!/bin/bash
# 🌾 Mosh VM Connection Script
# Persistent SSH connection to NixOS VM that survives laptop sleep

VM_IP="192.168.122.204"
SSH_KEY="$HOME/.ssh/nixos-grainkae3g"

echo "🌾 Connecting to NixOS VM with Mosh..."
echo "   IP: $VM_IP"
echo "   Key: $SSH_KEY"
echo ""
echo "💡 Mosh provides persistent sessions that survive:"
echo "   - Laptop sleep/hibernate"
echo "   - Network changes"
echo "   - Temporary disconnections"
echo ""
echo "🚀 Starting Mosh session..."
echo "   Press Ctrl+] then . to exit Mosh"
echo ""

# Connect with Mosh
mosh --ssh="ssh -i $SSH_KEY" nixos@$VM_IP
