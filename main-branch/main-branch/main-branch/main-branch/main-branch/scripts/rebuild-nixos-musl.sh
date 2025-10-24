#!/bin/bash
# 🌾 Rebuild NixOS with Musl Prioritization
# Alpine Linux compatibility for Grain Network development

echo "🌾 Rebuilding NixOS with musl libc prioritization..."
echo "   This will enable Alpine Linux compatibility"
echo "   Sway Wayland compositor will be available"
echo ""

cd /mnt/grainkae3g/grainstore/grainsource-nixos-qemu-kvm/nixos-configs

echo "📋 Configuration changes:"
echo "   ✅ Musl libc prioritization for lightweight packages"
echo "   ✅ Sway Wayland compositor (musl-compatible)"
echo "   ✅ Alpine-compatible terminal multiplexers"
echo "   ✅ GNOME fallback for compatibility"
echo ""

echo "🚀 Rebuilding NixOS..."
sudo nixos-rebuild switch --flake .#grainkae3g-grain6-wifi

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ NixOS rebuild successful!"
    echo ""
    echo "🌾 Available musl-compatible tools:"
    echo "   abduco    - Minimal session manager"
    echo "   dtach     - Lightweight terminal multiplexer"
    echo "   zellij    - Modern terminal multiplexer"
    echo "   sway      - Wayland compositor"
    echo "   alacritty - Terminal emulator"
    echo "   foot      - Wayland terminal"
    echo ""
    echo "💡 Start Sway: sway"
    echo "💡 Start GNOME: systemctl start gdm"
    echo ""
    echo "🔄 Rebooting in 10 seconds to apply changes..."
    sleep 10
    sudo reboot
else
    echo "❌ NixOS rebuild failed"
    echo "💡 Check the output above for errors"
    exit 1
fi
