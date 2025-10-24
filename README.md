# 🌾 Grain6 - The Complete Grain Network Monorepo Template

**The definitive template for Grain Network development**  
**Humble Stack + Alpine Linux + SixOS + Graindaemon**

[![GitHub Pages](https://img.shields.io/badge/GitHub%20Pages-Live%20Site-blue?style=flat-square&logo=github)](https://grain6pbc.github.io/grain6/)
[![License](https://img.shields.io/badge/License-Grain%20Network-green?style=flat-square)](LICENSE)
[![Clojure](https://img.shields.io/badge/Clojure-1.12.0-blue?style=flat-square&logo=clojure)](https://clojure.org/)
[![Humble UI](https://img.shields.io/badge/Humble%20UI-Desktop%20Apps-purple?style=flat-square)](https://github.com/HumbleUI/HumbleUI)

---

## 🎨 **What is Grain6?**

Grain6 is the complete monorepo template for building Grain Network applications. It provides:

- **🎨 Humble UI Applications** - Cross-platform desktop apps in Clojure
- **🔒 musl libc Security** - Lightweight, secure C library with Alpine Linux
- **⚡ SixOS Process Supervision** - Time-aware service management with s6
- **🤖 Graindaemon Automation** - GitHub integration and synchronization
- **🌐 Global Grain Identity** - Cross-platform username management
- **📚 Complete Documentation** - Philosophy, architecture, and best practices

## 🚀 **Quick Start**

### 1. Clone the Template
```bash
git clone https://github.com/grain6pbc/grain6.git your-grainspace
cd your-grainspace
```

### 2. Personalize Your Instance
```bash
# Update personal information
sed -i 's/your-username/YOUR_USERNAME/g' README.md
sed -i 's/your-email/YOUR_EMAIL/g' README.md

# Update repository references
sed -i 's/grain6pbc\/grain6/YOUR_ORG\/YOUR_REPO/g' .github/workflows/*.yml
```

### 3. Set Up Development Environment
```bash
# Install Babashka
curl -sLO https://raw.githubusercontent.com/babashka/babashka/master/install
chmod +x install
./install --dir ~/.local/bin

# Install Clojure CLI
curl -O https://download.clojure.org/install/linux-install-1.12.0.0.sh
chmod +x linux-install-1.12.0.sh
sudo ./linux-install-1.12.0.sh

# Install Node.js (for SvelteKit)
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt-get install -y nodejs
```

### 4. Build and Deploy
```bash
# Build Humble UI applications
cd grainstore/grainbarrel
bb build-apps

# Deploy to GitHub Pages
git add .
git commit -m "feat: Initialize Grain6 instance"
git push origin main
```

## 🏗️ **Architecture**

### **Humble Stack**
```
Clojure → Humble UI → Skija/Skia → JWM → Native OS
```

### **Security Stack**
```
Alpine Linux → musl libc → Static Linking → Container Security
```

### **Service Stack**
```
SixOS → s6 → Process Supervision → Time-aware Systems
```

### **Automation Stack**
```
Graindaemon → GitHub Actions → CI/CD → Automated Deployment
```

## 📁 **Project Structure**

```
grain6/
├── apps/                    # Humble UI Applications
│   ├── grainbook/          # Personal knowledge management
│   ├── graincourse/        # Educational content delivery
│   ├── grain6-desktop/     # Main Grain Network desktop app
│   ├── grainpath/          # Navigation and wayfinding
│   └── graintime/          # Temporal awareness and scheduling
├── core/                   # Shared Core Library
│   ├── src/grain-network/  # Common UI components and themes
│   └── deps.edn           # Core dependencies
├── grainstore/             # Grain Network Libraries
│   ├── grainpbc/           # Core Grain Network libraries
│   ├── humble-stack/       # Humble UI stack projects
│   ├── graindaemon/        # Automation and synchronization
│   └── graincontacts/      # Global identity system
├── docs/                   # Documentation
│   ├── core/philosophy/    # PSEUDO.md and philosophical foundations
│   ├── architecture/       # Technical architecture documentation
│   └── guides/             # User guides and tutorials
├── scripts/                # Build and deployment scripts
├── .github/workflows/      # GitHub Actions CI/CD
└── README.md              # This file
```

## 🎯 **Applications**

### **Desktop Applications**
- **grainbook** - Personal knowledge management and note-taking
- **graincourse** - Educational content delivery and course management
- **grain6-desktop** - Main Grain Network desktop application
- **grainpath** - Navigation and wayfinding with temporal awareness
- **graintime** - Temporal awareness and scheduling system

### **Core Libraries**
- **grain-network/core** - Shared UI components and themes
- **grainpbc/graintime** - Neovedic timestamp system
- **grainpbc/graincourse** - Course and content management
- **grainpbc/grainsync** - Synchronization and backup

### **Humble Stack Projects**
- **humble-desktop** - GNOME-like desktop environment in Clojure
- **grain-musl** - musl libc optimization and compatibility
- **humble-gc** - Advanced garbage collection system
- **grain-clj** - Clojure compiler for humble-gc VM
- **humble-repl** - Advanced REPL runtime with debugging

## 🤖 **Graindaemon System**

### **Automation Features**
- **GitHub Description Sync** - Auto-update repository description with grainsite URL
- **Alpine VM Sync** - Synchronize files between VM and host
- **Grainpath Management** - Automated branch creation and synchronization
- **CI/CD Integration** - GitHub Actions workflow automation

### **Usage**
```bash
# Update GitHub repository description
bb graindaemon:github-description-sync

# Sync Alpine VM with host
bb graindaemon:humble-sync

# Build all applications
bb build-apps
```

## 🌐 **Global Grain Identity**

### **Cross-Platform Username Management**
- **graincontacts** - Centralized identity management
- **Bridge Layer** - Old internet ↔ Grain Network integration
- **Conflict Resolution** - Duplicate username handling
- **Security** - Verification and authentication systems

### **Supported Platforms**
- GitHub, GitLab, Codeberg
- Instagram, LinkedIn, Twitter
- Discord, Slack, Matrix
- Custom Grain Network services

## 🔧 **Development Environment**

### **Alpine Linux VM** (Recommended)
```bash
# Create Alpine VM
sudo virt-install \
  --name grain6-dev \
  --memory 8192 \
  --vcpus 4 \
  --disk path=/var/lib/libvirt/images/grain6-dev.qcow2,format=qcow2,size=50 \
  --cdrom /path/to/alpine-standard.iso \
  --network bridge=virbr0 \
  --graphics vnc,listen=0.0.0.0 \
  --noautoconsole

# Connect to VM
sudo virsh console grain6-dev
```

### **Ubuntu Host** (Alternative)
```bash
# Install dependencies
sudo apt update
sudo apt install -y clojure babashka nodejs npm

# Clone and setup
git clone https://github.com/grain6pbc/grain6.git
cd grain6
npm install
```

## 📚 **Documentation**

### **Philosophy**
- **[PSEUDO.md](docs/core/philosophy/PSEUDO.md)** - Core philosophical foundations
- **[TODO-ASPIRATIONAL.md](docs/TODO-ASPIRATIONAL.md)** - Future goals and vision
- **[SESSION-LOG.md](docs/SESSION-LOG.md)** - Development session history

### **Architecture**
- **[GRAINAUDIO-GRAINPODS-DESIGN.md](docs/architecture/GRAINAUDIO-GRAINPODS-DESIGN.md)** - AI voice system design
- **[Humble Stack Architecture](docs/architecture/HUMBLE-STACK.md)** - Technical architecture
- **[SixOS Integration](docs/architecture/SIXOS-INTEGRATION.md)** - Process supervision

### **Guides**
- **[Getting Started](docs/guides/GETTING-STARTED.md)** - Beginner's guide
- **[Development Workflow](docs/guides/DEVELOPMENT-WORKFLOW.md)** - Best practices
- **[Deployment Guide](docs/guides/DEPLOYMENT.md)** - Production deployment

## 🤝 **Contributing**

### **For Contributors**
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests and documentation
5. Submit a pull request

### **For Template Users**
1. Use this template to create your own Grain Network instance
2. Customize the personal information
3. Add your own applications and libraries
4. Share your improvements back to the community

## 📄 **License**

This project is licensed under the Grain Network License - see the [LICENSE](LICENSE) file for details.

## 🌟 **Acknowledgments**

- **Humble UI** - Cross-platform desktop UI framework
- **Alpine Linux** - Security-oriented Linux distribution
- **s6** - Process supervision suite
- **Clojure** - Functional programming language
- **GitHub** - Hosting and CI/CD platform

## 🔗 **Links**

- **Live Site**: https://grain6pbc.com
- **GitHub Repository**: https://github.com/grain6pbc/grain6
- **Grain Network**: https://grain6pbc.org
- **Documentation**: https://docs.grain6pbc.org
- **API**: https://api.grain6pbc.net
- **Platform**: https://grainpbc.com

---

**Built with ❤️ by the Grain Network community**

*"In every walk with nature one receives far more than one seeks." — John Muir*