#!/bin/bash
# 🌾 qb shot - Quick VM Screenshot Command
# Finds and displays the latest VM screenshot for Cursor context

echo "📸 qb shot - Finding latest VM screenshot..."

# Change to the main repo directory
cd /home/xy/kae3g/grainkae3g

# Look for screenshots in root directory first
LATEST_ROOT=$(find . -maxdepth 1 -name "Screenshot_*.png" -type f -printf '%T@ %p\n' 2>/dev/null | sort -n | tail -1 | cut -d' ' -f2-)

# Look for screenshots in screenshots directory
LATEST_SCREENSHOTS=$(find screenshots -name "Screenshot_*.png" -type f -printf '%T@ %p\n' 2>/dev/null | sort -n | tail -1 | cut -d' ' -f2-)

# Determine the latest overall
if [ -n "$LATEST_ROOT" ] && [ -n "$LATEST_SCREENSHOTS" ]; then
    # Compare timestamps
    ROOT_TIME=$(stat -c %Y "$LATEST_ROOT" 2>/dev/null || echo 0)
    SCREENSHOTS_TIME=$(stat -c %Y "$LATEST_SCREENSHOTS" 2>/dev/null || echo 0)
    
    if [ "$ROOT_TIME" -gt "$SCREENSHOTS_TIME" ]; then
        LATEST="$LATEST_ROOT"
    else
        LATEST="$LATEST_SCREENSHOTS"
    fi
elif [ -n "$LATEST_ROOT" ]; then
    LATEST="$LATEST_ROOT"
elif [ -n "$LATEST_SCREENSHOTS" ]; then
    LATEST="$LATEST_SCREENSHOTS"
else
    echo "❌ No VM screenshots found"
    echo "💡 Take a screenshot in virt-manager and save to repo root"
    exit 0  # Exit with success even if no screenshots found
fi

# Display the latest screenshot info
echo "✅ Latest VM screenshot: $LATEST"
echo "📅 Modified: $(stat -c %y "$LATEST" 2>/dev/null || echo 'Unknown')"
echo "📏 Size: $(stat -c %s "$LATEST" 2>/dev/null | numfmt --to=iec || echo 'Unknown')"

# Move to screenshots directory if it's in root
if [[ "$LATEST" == Screenshot_*.png ]]; then
    echo "📁 Moving to screenshots directory..."
    mkdir -p screenshots
    mv "$LATEST" screenshots/
    echo "✅ Moved to screenshots/$LATEST"
    LATEST="screenshots/$LATEST"
fi

echo ""
echo "🌾 Screenshot ready for Cursor context!"
echo "📸 File: $LATEST"
echo ""
echo "💡 For Cursor: This shows the current VM state"
echo "🔄 Run 'bb qb-shot' again after taking new screenshots"
echo "🧹 Run 'bb qb-shot-clean' to organize root screenshots"
