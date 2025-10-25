#!/bin/bash
# 🌾 qb shot clean - Move root screenshots to screenshots folder

echo "🧹 qb shot clean - Moving root screenshots to screenshots folder..."

# Change to the main repo directory
cd /home/xy/kae3g/grainkae3g

# Find all screenshots in root directory
ROOT_SCREENSHOTS=$(find . -maxdepth 1 -name "Screenshot_*.png" -type f)

if [ -z "$ROOT_SCREENSHOTS" ]; then
    echo "✅ No screenshots found in root directory"
    echo "💡 All screenshots are already organized in screenshots/ folder"
    exit 0
fi

# Create screenshots directory if it doesn't exist
mkdir -p screenshots

# Move each screenshot to screenshots directory
echo "📁 Moving screenshots to screenshots/ folder..."
for screenshot in $ROOT_SCREENSHOTS; do
    filename=$(basename "$screenshot")
    echo "   Moving: $filename"
    mv "$screenshot" "screenshots/"
done

echo "✅ Moved $(echo "$ROOT_SCREENSHOTS" | wc -l) screenshot(s) to screenshots/ folder"
echo ""
echo "🌾 Screenshot cleanup complete!"
echo "📸 All screenshots are now organized in screenshots/ folder"
echo ""
echo "💡 Run 'bb qb-shot' to find the latest screenshot"
