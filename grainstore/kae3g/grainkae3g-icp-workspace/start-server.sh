#!/bin/bash
# 🌾 Grain6 ICP Local Testnet Server
# Start web server from correct directory

cd "$(dirname "$0")"
echo "🌾 Starting Grain6 ICP Local Testnet Server..."
echo "📁 Directory: $(pwd)"
echo "📄 File: testnet.html"
echo "🌐 URL: http://localhost:8080/testnet.html"
echo ""

# Kill any existing server on port 8080
pkill -f "python3 -m http.server 8080" 2>/dev/null || true
sleep 2

# Start server
python3 -m http.server 8080
