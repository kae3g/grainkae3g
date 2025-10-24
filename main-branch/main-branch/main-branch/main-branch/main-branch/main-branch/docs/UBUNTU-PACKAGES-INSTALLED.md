# Ubuntu 24.04 LTS Package Installation Log

## Session Installation Log - Wed Oct 22 05:58:30 AM PDT 2025

### Core Development Tools
- **git** - Version control system
- **curl** - HTTP client for downloads
- **wget** - File download utility
- **make** - Build automation tool
- **unzip** - Archive extraction

### Java Runtime (for Babashka)
- **openjdk-17-jdk** - Java Development Kit for Babashka

### Nix Package Manager
- **Nix** - Declarative package manager (installed via sh script)
  - Installation: `sh <(curl -L https://nixos.org/nix/install) --no-daemon`
  - Profile: `/home/xy/.nix-profile`

### Sway Desktop Environment
- **sway** - Wayland compositor
- **swayidle** - Idle management daemon
- **swaylock** - Screen locker
- **waybar** - Status bar for Wayland
- **brightnessctl** - Brightness control

### Display & Color Management
- **gammastep** - Color temperature adjustment for Wayland
- **wl-gammarelay** - Alternative color temperature tool (built from source)
  - Dependencies: `libwayland-dev`, `golang-go`

### Screenshot & Clipboard Tools
- **grim** - Screenshot utility for Wayland
- **slurp** - Region selection for Wayland
- **wl-clipboard** - Clipboard utilities for Wayland

### Development Tools
- **nodejs** - JavaScript runtime
- **npm** - Node.js package manager
- **gh** - GitHub CLI (installed manually)
  - Version: 2.82.1
  - Location: `/home/xy/.local/bin/gh`

### Cryptographic Tools
- **gnupg** - GNU Privacy Guard for GPG keys
- **openssh-client** - SSH client and key management

### User-Installed Tools (Local)
- **Babashka** - Clojure scripting environment
  - Location: `/home/xy/.local/bin/bb`
  - Installation: Manual download and setup
- **NVM** - Node Version Manager
  - Location: `/home/xy/.nvm`
  - Purpose: User-level Node.js management

### Installation Commands Used

#### System Packages (via apt)
```bash
# Core development tools
sudo apt install -y git curl wget make unzip

# Java for Babashka
sudo apt install -y openjdk-17-jdk

# Sway desktop environment
sudo apt install -y sway swayidle swaylock waybar brightnessctl

# Display management
sudo apt install -y gammastep libwayland-dev golang-go

# Screenshot tools
sudo apt install -y grim slurp wl-clipboard

# Node.js and npm
sudo apt install -y nodejs npm
```

#### Manual Installations
```bash
# Nix package manager
sh <(curl -L https://nixos.org/nix/install) --no-daemon

# Babashka (Clojure scripting)
curl -sLO https://github.com/babashka/babashka/releases/download/v1.3.185/babashka-1.3.185-linux-amd64-static.tar.gz
tar -xzf babashka-1.3.185-linux-amd64-static.tar.gz
mv bb ~/.local/bin/

# GitHub CLI
wget https://github.com/cli/cli/releases/download/v2.82.1/gh_2.82.1_linux_amd64.tar.gz
tar -xzf gh_2.82.1_linux_amd64.tar.gz
cp gh_2.82.1_linux_amd64/bin/gh ~/.local/bin/

# NVM (Node Version Manager)
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash
```

### Configuration Files Created

- **Sway config**: `configs/sway/config`
- **Waybar config**: `configs/waybar/config` and `configs/waybar/style.css`
- **GDM config**: `configs/gdm/custom.conf`
- **Desktop entry**: `/home/xy/.local/share/applications/cursor.desktop`
- **Git config**: Global user identity and GPG signing
- **SSH keys**: `~/.ssh/id_ed25519` (ed25519)
- **GPG keys**: `~/.gnupg/` (EDDSA with Ed25519 curve)

### System Information

- **OS**: Ubuntu 24.04 LTS
- **Hardware**: Framework 16 Laptop (AMD Ryzen 7040 Series)
- **Display**: 2560x1600@165Hz (eDP-2)
- **Desktop**: Sway (Wayland compositor)
- **Shell**: Bash

### Key Features Configured

- **Warm Display**: 2000K color temperature with gammastep
- **Screenshot System**: grim + slurp + wl-clipboard
- **Git Security**: GPG signing + SSH authentication
- **Dual Deploy**: GitHub + Codeberg synchronization
- **Development Stack**: Babashka + Nix + Node.js

### Installed Package List (dpkg --get-selections)

```
brightnessctl					install
curl						install
gammastep					install
git						install
git-man						install
gnupg						install
gnupg-l10n					install
gnupg-utils					install
grim						install
libcurl3t64-gnutls:amd64			install
libcurl4t64:amd64				install
libpagemaker-0.0-0:amd64			install
make						install
node-hosted-git-info				install
node-make-dir					install
node-npm-bundled				install
node-npm-package-arg				install
node-npm-run-path				install
node-npmlog					install
node-validate-npm-package-license		install
node-validate-npm-package-name			install
nodejs						install
nodejs-doc					install
npm						install
openjdk-17-jdk:amd64				install
openjdk-17-jdk-headless:amd64			install
openjdk-17-jre:amd64				install
openjdk-17-jre-headless:amd64			install
openssh-client					install
slurp						install
sway						install
sway-backgrounds				install
swaybg						install
swayidle					install
swaylock					install
unzip						install
waybar						install
wget						install
wl-clipboard					install
```
