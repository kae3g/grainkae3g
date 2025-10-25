#!/bin/bash
# Setup Cursor terminal to use kae3g zsh config

echo "🔧 Setting up Cursor terminal with kae3g zsh config..."

# Check if Cursor settings directory exists
CURSOR_SETTINGS="$HOME/Library/Application Support/Cursor/User/settings.json"

if [ ! -f "$CURSOR_SETTINGS" ]; then
    echo "⚠️  Cursor settings.json not found at: $CURSOR_SETTINGS"
    echo "   Please open Cursor and create a settings file first."
    exit 1
fi

# Backup existing settings
cp "$CURSOR_SETTINGS" "$CURSOR_SETTINGS.backup.$(date +%Y%m%d_%H%M%S)"
echo "✅ Backed up existing settings"

# Create temporary settings with terminal configuration
cat > /tmp/cursor_terminal_settings.json << 'EOF'
{
    "terminal.integrated.shell.osx": "/opt/homebrew/bin/zsh",
    "terminal.integrated.shellArgs.osx": [
        "-c",
        "source ~/.zshrc; exec zsh"
    ],
    "terminal.integrated.defaultProfile.osx": "zsh (kae3g)"
}
EOF

echo "📝 Terminal configuration created"
echo ""
echo "🔧 Manual Setup Instructions:"
echo "1. Open Cursor Settings (Cmd+,)"
echo "2. Search for 'terminal'"
echo "3. Add these settings to your settings.json:"
echo ""
cat /tmp/cursor_terminal_settings.json
echo ""
echo "4. Restart Cursor terminal"
echo ""
echo "🎯 Your terminal will then use the kae3g λ prompt!"

# Alternative: Direct method
echo ""
echo "🚀 Alternative: Direct shell command"
echo "You can also run this in Cursor terminal:"
echo "source ~/.zshrc"
echo ""
echo "✨ Setup complete!"
