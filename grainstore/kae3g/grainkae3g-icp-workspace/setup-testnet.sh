#!/bin/bash
# 🌾 Grain6 ICP Local Testnet Setup Script
# Sets up local ICP replica and web interface

set -euo pipefail

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

log() {
    echo -e "${GREEN}[$(date +'%Y-%m-%d %H:%M:%S')]${NC} $1"
}

error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

warn() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log "🌾 Setting up Grain6 ICP Local Testnet..."

# Check if dfx is available
if ! command -v dfx &> /dev/null; then
    error "dfx not found. Please install ICP SDK first."
    exit 1
fi

# Set PATH for dfx
export PATH="$HOME/.local/share/dfx/bin:$PATH"

# Navigate to project directory
cd "$(dirname "$0")"

log "1️⃣ Starting ICP replica..."
dfx start --clean --background

# Wait for replica to start
sleep 5

log "2️⃣ Checking replica health..."
if dfx ping > /dev/null 2>&1; then
    log "✅ ICP replica is healthy"
else
    error "❌ ICP replica failed to start"
    exit 1
fi

log "3️⃣ Deploying Grain6 canister..."
dfx deploy

log "4️⃣ Getting canister information..."
CANISTER_ID=$(dfx canister id grain6_icp)
log "📦 Canister ID: $CANISTER_ID"

log "5️⃣ Testing canister functions..."
dfx canister call grain6_icp status
dfx canister call grain6_icp getGraintime "kae3g"

log "6️⃣ Starting web interface..."
# Kill any existing Python server on port 3000
pkill -f "python3 -m http.server 3000" || true
sleep 2

# Start Python server
python3 -m http.server 3000 &
SERVER_PID=$!

# Wait for server to start
sleep 3

log "🎉 Local testnet setup complete!"
echo ""
info "📋 Local URLs:"
info "   🌐 Web Interface: http://localhost:3000/testnet.html"
info "   🔧 ICP Candid: http://127.0.0.1:4943/?canisterId=lz3um-vp777-77777-aaaba-cai&id=$CANISTER_ID"
info "   📊 ICP Dashboard: http://localhost:4943"
echo ""
info "📦 Canister Details:"
info "   ID: $CANISTER_ID"
info "   Status: Active"
info "   Language: Motoko"
echo ""
info "🚀 Development Commands:"
info "   Test canister: dfx canister call grain6_icp status"
info "   Stop replica: dfx stop"
info "   Stop web server: kill $SERVER_PID"
echo ""
log "✅ Ready for development! Open http://localhost:3000/testnet.html in your browser"
