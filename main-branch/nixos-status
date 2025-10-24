#!/bin/bash
# 🌾 NixOS VM Status Check Script
# Quick status check for the VM environment

echo "🌾 NixOS VM Status Check"
echo "📅 $(date)"
echo ""

# Check shared folder mount
echo "📁 Shared folder status:"
if mountpoint -q /mnt/grainkae3g; then
    echo "✅ /mnt/grainkae3g is mounted"
    echo "📋 Contents:"
    ls -la /mnt/grainkae3g/ | head -10
    echo "..."
else
    echo "❌ /mnt/grainkae3g is NOT mounted"
fi
echo ""

# Check nixos-configs directory
echo "📂 NixOS configs directory:"
if [ -d "/mnt/grainkae3g/grainstore/grainsource-nixos-qemu-kvm/nixos-configs" ]; then
    echo "✅ nixos-configs directory exists"
    echo "📋 Contents:"
    ls -la /mnt/grainkae3g/grainstore/grainsource-nixos-qemu-kvm/nixos-configs/
else
    echo "❌ nixos-configs directory NOT found"
fi
echo ""

# Check flake.nix
echo "📄 Flake configuration:"
if [ -f "/mnt/grainkae3g/grainstore/grainsource-nixos-qemu-kvm/nixos-configs/flake.nix" ]; then
    echo "✅ flake.nix found"
else
    echo "❌ flake.nix NOT found"
fi
echo ""

# Check completion log
echo "📝 Installation status:"
if [ -f "/mnt/grainkae3g/nixos-install-complete.log" ]; then
    echo "✅ Installation completed"
    cat /mnt/grainkae3g/nixos-install-complete.log
else
    echo "⏳ Installation not yet completed"
fi
echo ""

echo "🌾 Status check complete!"
