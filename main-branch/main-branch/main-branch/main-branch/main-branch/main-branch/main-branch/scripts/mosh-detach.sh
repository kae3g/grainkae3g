#!/bin/bash
# 🌾 Mosh Detach Script
# Detaches from Mosh session and returns to Ubuntu shell

echo "🌾 Detaching from Mosh session..."
echo "   Press Ctrl+] then . to exit Mosh"
echo "   Or press Ctrl+] then d to detach (keep session running)"
echo ""

# Send detach sequence to Mosh
printf '\x1d.'  # Ctrl+] then . (exit completely)
# Alternative: printf '\x1dd'  # Ctrl+] then d (detach)

echo "✅ Mosh detach sequence sent"
echo "💡 If still connected, manually press Ctrl+] then ."
