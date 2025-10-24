#!/bin/bash
# 🌾 Screenshot Cleanup Script
# Keeps only the most recent screenshots to prevent repo bloat

SCREENSHOT_DIR="screenshots"
KEEP_COUNT=5

echo "🧹 Starting comprehensive screenshot cleanup..."

# First, move any root screenshots to screenshots folder
echo "1️⃣ Moving root screenshots to screenshots/ folder..."
cd /home/xy/kae3g/grainkae3g
ROOT_SCREENSHOTS=$(find . -maxdepth 1 -name "Screenshot_*.png" -type f)

if [ -n "$ROOT_SCREENSHOTS" ]; then
    mkdir -p "$SCREENSHOT_DIR"
    for screenshot in $ROOT_SCREENSHOTS; do
        filename=$(basename "$screenshot")
        echo "   Moving: $filename"
        mv "$screenshot" "$SCREENSHOT_DIR/"
    done
    echo "✅ Moved $(echo "$ROOT_SCREENSHOTS" | wc -l) screenshot(s) to $SCREENSHOT_DIR/"
else
    echo "✅ No root screenshots to move"
fi

if [ ! -d "$SCREENSHOT_DIR" ]; then
    echo "📁 Creating $SCREENSHOT_DIR directory"
    mkdir -p "$SCREENSHOT_DIR"
    exit 0
fi

echo "2️⃣ Cleaning up old screenshots..."
echo "📁 Directory: $SCREENSHOT_DIR"
echo "💾 Keeping: $KEEP_COUNT most recent files"

# Count total files
TOTAL_FILES=$(find "$SCREENSHOT_DIR" -name "*.png" | wc -l)
echo "📊 Total screenshots: $TOTAL_FILES"

if [ "$TOTAL_FILES" -le "$KEEP_COUNT" ]; then
    echo "✅ No cleanup needed (≤ $KEEP_COUNT files)"
    exit 0
fi

# Remove old files, keeping the most recent
find "$SCREENSHOT_DIR" -name "*.png" -type f -printf '%T@ %p\n' | \
    sort -n | \
    head -n -$KEEP_COUNT | \
    cut -d' ' -f2- | \
    xargs rm -f

REMAINING_FILES=$(find "$SCREENSHOT_DIR" -name "*.png" | wc -l)
echo ""
echo "✅ Comprehensive screenshot cleanup complete!"
echo "📊 Remaining screenshots: $REMAINING_FILES"
echo "🗑️  Removed: $((TOTAL_FILES - REMAINING_FILES)) files"
echo ""
echo "🌾 All screenshots organized and cleaned up!"
echo "💡 Run 'bb qb-shot' to find the latest screenshot"
