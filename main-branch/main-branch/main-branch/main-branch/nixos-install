#!/bin/bash
# 🌾 NixOS VM Installation Script
# Executes from within the VM to install grain6 + grainwifi configuration

set -e  # Exit on any error

echo "🌾 Starting NixOS installation with grain6 + grainwifi..."
echo "📁 Current directory: $(pwd)"
echo "📅 Timestamp: $(date)"
echo ""

# Navigate to the nixos-configs directory
echo "📂 Navigating to nixos-configs directory..."
cd /mnt/grainkae3g/grainstore/grainsource-nixos-qemu-kvm/nixos-configs

echo "📋 Current directory: $(pwd)"
echo "📋 Contents:"
ls -la
echo ""

# Check if flake.nix exists
if [ ! -f "flake.nix" ]; then
    echo "❌ Error: flake.nix not found in $(pwd)"
    echo "📋 Available files:"
    ls -la
    exit 1
fi

echo "✅ Found flake.nix"
echo ""

# Generate base configuration
echo "🔧 Generating base NixOS configuration..."
sudo nixos-generate-config --root /mnt
echo "✅ Base configuration generated"
echo ""

# Apply flake configuration
echo "🚀 Applying grain6 + grainwifi flake configuration..."
sudo nixos-rebuild switch --flake .#grainkae3g-grain6-wifi
echo "✅ Flake configuration applied successfully!"
echo ""

# Create completion log
echo "📝 Creating completion log..."
echo "NixOS installation completed at $(date)" > /mnt/grainkae3g/nixos-install-complete.log
echo "Configuration: grain6 + grainwifi" >> /mnt/grainkae3g/nixos-install-complete.log
echo "Flake: grainkae3g-grain6-wifi" >> /mnt/grainkae3g/nixos-install-complete.log
echo "✅ Completion log created"
echo ""

echo "🎉 NixOS installation complete!"
echo "🔄 Reboot recommended to activate all services"
echo "💡 Run: sudo reboot"
echo ""
echo "🌾 now == next + 1"
