#!/bin/bash
# Install Grainzsh as system default for Ubuntu 24.04 LTS

set -e

echo "🌾 Installing Grainzsh System-Wide"
echo "==================================="
echo ""

# Check if Zsh is installed
if ! command -v zsh &> /dev/null; then
    echo "📦 Installing Zsh..."
    sudo apt update
    sudo apt install -y zsh
fi

# Get current directory
GRAINZSH_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
echo "📁 Grainzsh directory: $GRAINZSH_DIR"
echo ""

# Backup existing .zshrc if it exists
if [ -f "$HOME/.zshrc" ]; then
    echo "💾 Backing up existing .zshrc to .zshrc.backup"
    cp "$HOME/.zshrc" "$HOME/.zshrc.backup"
fi

# Ask user which config to use
echo "Choose configuration:"
echo "  1) Template (recommended for new users)"
echo "  2) Personal kae3g config (minimal lambda prompt)"
echo ""
read -p "Enter choice (1 or 2): " choice

if [ "$choice" = "2" ]; then
    echo "🔗 Symlinking to kae3g personal config..."
    ln -sf "$GRAINZSH_DIR/personal/kae3g/.zshrc" "$HOME/.zshrc"
else
    echo "🔗 Symlinking to template config..."
    ln -sf "$GRAINZSH_DIR/template/.zshrc" "$HOME/.zshrc"
fi

echo ""
echo "✅ Grainzsh installed successfully"
echo ""

# Check if Zsh is default shell
if [ "$SHELL" != "$(which zsh)" ]; then
    echo "📝 Setting Zsh as default shell..."
    chsh -s $(which zsh)
    echo ""
    echo "⚠️  Please log out and log back in for shell change to take effect"
    echo "    Or run: exec zsh"
fi

echo ""
echo "🌾 Grainzsh Setup Complete"
echo ""
echo "Next steps:"
echo "  1. exec zsh                  # Start using Grainzsh now"
echo "  2. Set up grainenvvars       # Optional: configure API keys"
echo "  3. Test: gb --version        # Verify Grainbarrel integration"
echo ""
echo "λ - Simple. Clean. Functional. 🌾"

