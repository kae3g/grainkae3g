# Grain6PBC Utilities

A collection of utilities for the Grain Network ecosystem, providing tools for Clojure-based development, ICP integration, and marketplace functionality.

## 🌾 Available Utilities

### Clelte - Clojure to Svelte Compiler
- **Path**: `clelte/`
- **Description**: Compiles Clojure code to Svelte components
- **Usage**: `bb clelte/clelte.bb compile [component-name]`

### Clotoko - Clojure to Motoko Compiler  
- **Path**: `clotoko/`
- **Description**: Compiles Clojure code to Motoko for ICP canisters
- **Usage**: `bb clotoko/clotoko.bb compile [input.clj] [canister-name]`

### Poshmark Scraper
- **Path**: `poshmark-scraper/`
- **Description**: Scrapes Poshmark for real product data
- **Usage**: `bb poshmark-scraper/scraper.bb "Ralph Lauren cotton" 12.50`

### GrainDaemon
- **Path**: `graindaemon/`
- **Description**: Automated synchronization and deployment daemon
- **Usage**: `bb graindaemon/daemon.bb sync`

### GrainMode
- **Path**: `grainmode/`
- **Description**: AI voice mode management (Trish/Glow)
- **Usage**: `bb grainmode/mode.bb set trish`

## 🚀 Quick Start

1. **Clone the utilities**:
   ```bash
   git clone https://github.com/grain6pbc/grain6pbc-utils.git
   cd grain6pbc-utils
   ```

2. **Install dependencies**:
   ```bash
   # Install Babashka
   curl -sLO https://raw.githubusercontent.com/babashka/babashka/master/install
   chmod +x install
   ./install
   ```

3. **Use a utility**:
   ```bash
   # Compile Clojure to Svelte
   bb clelte/clelte.bb compile grainthrift-app
   
   # Compile Clojure to Motoko
   bb clotoko/clotoko.bb compile src/app.clj grainthrift-app
   
   # Scrape Poshmark data
   bb poshmark-scraper/scraper.bb "Ralph Lauren" 12.50
   ```

## 📁 Structure

```
grain6pbc-utils/
├── README.md
├── clelte/
│   ├── clelte.bb
│   ├── compiler.clj
│   └── README.md
├── clotoko/
│   ├── clotoko.bb
│   ├── compiler.clj
│   └── README.md
├── poshmark-scraper/
│   ├── scraper.bb
│   └── README.md
├── graindaemon/
│   ├── daemon.bb
│   └── README.md
└── grainmode/
    ├── mode.bb
    └── README.md
```

## 🔧 Development

Each utility is self-contained with:
- **Babashka script** (`.bb`) for easy execution
- **Clojure source** (`.clj`) for the core logic
- **README** with specific usage instructions

## 🌐 Integration

These utilities are designed to work together:
- **Clelte** compiles frontend components
- **Clotoko** compiles backend canisters
- **Poshmark Scraper** provides real product data
- **GrainDaemon** automates deployment
- **GrainMode** manages AI voice modes

## 📜 License

MIT License - see LICENSE file for details.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/grain6pbc/grain6pbc-utils/issues)
- **Discussions**: [GitHub Discussions](https://github.com/grain6pbc/grain6pbc-utils/discussions)
- **Documentation**: [Wiki](https://github.com/grain6pbc/grain6pbc-utils/wiki)