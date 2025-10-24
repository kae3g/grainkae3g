#!/bin/bash

# Screenshot script for Sway
TIMESTAMP=$(date +%s)
FILENAME="/tmp/screenshot_${TIMESTAMP}.png"

case "$1" in
    "full")
        echo "📸 Taking full screen screenshot..."
        grim "$FILENAME"
        if [ $? -eq 0 ]; then
            echo "✅ Full screenshot saved: $FILENAME"
            # Try to copy to clipboard
            wl-copy < "$FILENAME" 2>/dev/null && echo "📋 Copied to clipboard"
        else
            echo "❌ Screenshot failed"
        fi
        ;;
    "area")
        echo "📸 Taking area screenshot..."
        grim -g "$(slurp)" "$FILENAME"
        if [ $? -eq 0 ]; then
            echo "✅ Area screenshot saved: $FILENAME"
            wl-copy < "$FILENAME" 2>/dev/null && echo "📋 Copied to clipboard"
        else
            echo "❌ Screenshot failed"
        fi
        ;;
    "window")
        echo "📸 Taking window screenshot..."
        grim -g "$(slurp -d)" "$FILENAME"
        if [ $? -eq 0 ]; then
            echo "✅ Window screenshot saved: $FILENAME"
            wl-copy < "$FILENAME" 2>/dev/null && echo "📋 Copied to clipboard"
        else
            echo "❌ Screenshot failed"
        fi
        ;;
    *)
        echo "📸 Screenshot Tool"
        echo "Usage: $0 [full|area|window]"
        ;;
esac
