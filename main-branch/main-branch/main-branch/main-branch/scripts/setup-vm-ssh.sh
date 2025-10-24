#!/bin/sh
# 🌾 SSH Setup for NixOS VM
# Interactive script to set up SSH key authentication

echo "🔑 Setting up SSH key authentication for NixOS VM..."
echo ""

VM_IP="192.168.122.204"
SSH_KEY="$HOME/.ssh/nixos-grainkae3g"

echo "📋 VM Details:"
echo "   IP: $VM_IP"
echo "   SSH Key: $SSH_KEY"
echo ""

# Check if key exists
if [ ! -f "$SSH_KEY" ]; then
    echo "❌ SSH key not found: $SSH_KEY"
    echo "💡 Generate it first with: bb scripts/grainkey.bb generate nixos-vm"
    exit 1
fi

echo "✅ SSH key found: $SSH_KEY"
echo ""

# Test SSH connection
echo "🔍 Testing SSH connection..."
if ssh -i "$SSH_KEY" -o ConnectTimeout=5 -o BatchMode=yes nixos@$VM_IP 'echo "SSH key authentication working!"' 2>/dev/null; then
    echo "✅ SSH key authentication already working!"
    echo "🌾 You can now connect without password:"
    echo "   ssh -i $SSH_KEY nixos@$VM_IP"
    exit 0
fi

echo "⚠️  SSH key authentication not set up yet"
echo ""

# Deploy key using ssh-copy-id
echo "🚀 Deploying SSH key to VM..."
echo "💡 You'll need to enter the VM password once to set up key authentication"
echo ""

ssh-copy-id -i "$SSH_KEY.pub" nixos@$VM_IP

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ SSH key authentication setup complete!"
    echo ""
    echo "🔑 Test the connection:"
    echo "   ssh -i $SSH_KEY nixos@$VM_IP 'echo \"Hello from NixOS VM!\"'"
    echo ""
    echo "🌾 You can now connect without password:"
    echo "   ssh -i $SSH_KEY nixos@$VM_IP"
else
    echo ""
    echo "❌ Failed to setup SSH key authentication"
    echo "💡 Make sure the VM is running and accessible"
    echo "💡 Check VM IP with: sudo cat /var/lib/libvirt/dnsmasq/virbr0.status"
fi
