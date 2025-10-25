# Urbit ICP Identity System

A custom Urbit-like identity system built on ICP (Internet Computer Protocol) with Chain Fusion integration.

## Architecture

- **ICP Subnet**: Primary identity management (250x cheaper than Solana)
- **Chain Fusion**: Native Solana integration for high-performance operations
- **WebAssembly**: Rust-based canisters for familiar development
- **Threshold Cryptography**: Enhanced security for identity management

## Getting Started

1. **Install Dependencies**:
   ```bash
   bb scripts/icp-development-setup.bb
   ```

2. **Start Local Development**:
   ```bash
   cd urbit-icp-identity
   dfx start --background
   dfx deploy
   ```

3. **Build Frontend**:
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

## Canisters

- `urbit-identity`: Main identity management
- `urbit-address-space`: Address allocation and management
- `urbit-metadata`: IPFS integration and metadata storage
- `urbit-chain-fusion`: Cross-chain integration (Solana, Ethereum, Bitcoin)
- `urbit-nock`: Nock interpreter for Urbit computation
- `urbit-hoon`: Hoon compiler for Urbit programming
- `urbit-arvo`: Arvo kernel for Urbit operating system

## Development

- **Rust**: Canister development
- **TypeScript**: Frontend development
- **DFX**: ICP development framework
- **Chain Fusion**: Cross-chain integration

## License

MIT License - see LICENSE file for details.
