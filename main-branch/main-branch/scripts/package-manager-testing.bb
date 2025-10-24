#!/usr/bin/env bb

;; Multi-Package-Manager Testing System
;; Tests Grainspace deployment across Homebrew, Nix, Pacman, and APT

(require '[clojure.java.shell :as shell]
         '[clojure.string :as str]
         '[babashka.fs :as fs])

(defn log [message]
  "Log with timestamp and package manager"
  (let [timestamp (java.time.LocalDateTime/now)
        formatted-time (.format timestamp (java.time.format.DateTimeFormatter/ofPattern "HH:mm:ss"))]
    (println (str "[" formatted-time "] 📦 " message))))

(defn run-command [cmd & {:keys [sh timeout-ms]}]
  "Run command with optional shell execution and timeout"
  (try
    (let [result (if sh
                   (shell/sh "bash" "-c" cmd)
                   (shell/sh cmd))]
      (if (= (:exit result) 0)
        (str/trim (:out result))
        (do
          (log (str "Command failed: " cmd " - " (:err result)))
          "")))
    (catch Exception e
      (log (str "Error running command: " cmd " - " (.getMessage e)))
      "")))

;; Package manager configurations
(def package-managers
  {:homebrew
   {:name "Homebrew"
    :os "macOS"
    :install-cmd "brew install"
    :test-cmd "brew test"
    :uninstall-cmd "brew uninstall"
    :check-cmd "brew list"
    :formula-path "Formula/grainspace.rb"}
   
   :linuxbrew
   {:name "Linuxbrew (Homebrew on Linux)"
    :os "Linux"
    :install-cmd "brew install"
    :test-cmd "brew test"
    :uninstall-cmd "brew uninstall"
    :check-cmd "brew list"
    :formula-path "Formula/grainspace.rb"}
   
   :leiningen
   {:name "Leiningen"
    :os "Cross-platform"
    :install-cmd "lein install"
    :test-cmd "lein test"
    :uninstall-cmd "lein uninstall"
    :check-cmd "lein version"
    :project-path "project.clj"}
   
   :nix
   {:name "Nix"
    :os "Linux/macOS"
    :install-cmd "nix-env -i"
    :test-cmd "nix-shell --run"
    :uninstall-cmd "nix-env -e"
    :check-cmd "nix-env -q"
    :flake-path "flake.nix"}
   
   :pacman
   {:name "Pacman (Arch)"
    :os "Arch Linux"
    :install-cmd "pacman -S"
    :test-cmd "pacman -Q"
    :uninstall-cmd "pacman -R"
    :check-cmd "pacman -Q"
    :pkgbuild-path "PKGBUILD"}
   
   :apt
   {:name "APT (Debian/Ubuntu)"
    :os "Debian/Ubuntu"
    :install-cmd "apt install"
    :test-cmd "dpkg -l"
    :uninstall-cmd "apt remove"
    :check-cmd "dpkg -l"
    :deb-path "grainspace.deb"}})

;; Test results tracking
(def test-results (atom {}))

(defn detect-os []
  "Detect operating system"
  (let [os-name (System/getProperty "os.name")
        os-version (System/getProperty "os.version")]
    (cond
      (str/includes? os-name "Mac") :macos
      (str/includes? os-name "Linux") :linux
      (str/includes? os-name "Windows") :windows
      :else :unknown)))

(defn check-package-manager [pm]
  "Check if package manager is available"
  (let [pm-config (get package-managers pm)
        check-cmd (:check-cmd pm-config)]
    (case pm
      :homebrew (run-command "which brew" :sh true)
      :nix (run-command "which nix-env" :sh true)
      :pacman (run-command "which pacman" :sh true)
      :apt (run-command "which apt" :sh true))))

(defn create-homebrew-formula []
  "Create Homebrew formula for Grainspace"
  (log "Creating Homebrew formula...")
  
  (let [formula-content
        "class Grainspace < Formula
  desc \"Unified Decentralized Platform - Identity, Social, Marketplace, Streaming\"
  homepage \"https://github.com/kae3g/grainspace\"
  url \"https://github.com/kae3g/grainspace/archive/v0.1.0.tar.gz\"
  sha256 \"YOUR_SHA256_HERE\"
  license \"MIT\"

  depends_on \"clojure\" => :build
  depends_on \"node\" => :build
  depends_on \"dfx\" => :build

  def install
    system \"clojure\", \"-M:build\"
    bin.install \"target/grainspace.jar\"
    bin.install \"scripts/grainspace\" => \"grainspace\"
  end

  test do
    system \"#{bin}/grainspace\", \"--version\"
  end
end"]
    
    (fs/create-dir "packages/homebrew")
    (spit "packages/homebrew/Formula/grainspace.rb" formula-content)
    (log "✅ Homebrew formula created")))

(defn create-nix-flake []
  "Create Nix flake for Grainspace"
  (log "Creating Nix flake...")
  
  (let [flake-content
        "{
  description = \"Grainspace - Unified Decentralized Platform\";

  inputs = {
    nixpkgs.url = \"github:NixOS/nixpkgs/nixos-unstable\";
    flake-utils.url = \"github:numtide/flake-utils\";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
        grainspace = pkgs.stdenv.mkDerivation {
          name = \"grainspace\";
          version = \"0.1.0\";
          src = ./.;
          
          buildInputs = with pkgs; [
            clojure
            nodejs
            dfx
            jdk11
          ];
          
          buildPhase = ''
            clojure -M:build
          '';
          
          installPhase = ''
            mkdir -p $out/bin
            cp target/grainspace.jar $out/bin/
            cp scripts/grainspace $out/bin/
            chmod +x $out/bin/grainspace
          '';
          
          meta = with pkgs.lib; {
            description = \"Unified Decentralized Platform\";
            homepage = \"https://github.com/kae3g/grainspace\";
            license = licenses.mit;
            platforms = platforms.all;
          };
        };
      in
      {
        packages.default = grainspace;
        apps.default = flake-utils.lib.mkApp { drv = grainspace; };
      });
}"]
    
    (spit "packages/nix/flake.nix" flake-content)
    (log "✅ Nix flake created")))

(defn create-pkgbuild []
  "Create PKGBUILD for Arch Linux"
  (log "Creating PKGBUILD...")
  
  (let [pkgbuild-content
        "# Maintainer: kae3g <kj3x39@gmail.com>
pkgname=grainspace
pkgver=0.1.0
pkgrel=1
pkgdesc=\"Unified Decentralized Platform - Identity, Social, Marketplace, Streaming\"
arch=('x86_64')
url=\"https://github.com/kae3g/grainspace\"
license=('MIT')
depends=('clojure' 'nodejs' 'dfx' 'jre-openjdk')
makedepends=('git' 'clojure' 'nodejs' 'dfx')
source=(\"git+https://github.com/kae3g/grainspace.git#tag=v$pkgver\")
sha256sums=('SKIP')

build() {
  cd \"$pkgname\"
  clojure -M:build
}

package() {
  cd \"$pkgname\"
  
  install -Dm755 target/grainspace.jar \"$pkgdir/usr/bin/grainspace.jar\"
  install -Dm755 scripts/grainspace \"$pkgdir/usr/bin/grainspace\"
  
  install -Dm644 README.md \"$pkgdir/usr/share/doc/$pkgname/README.md\"
  install -Dm644 LICENSE \"$pkgdir/usr/share/licenses/$pkgname/LICENSE\"
}"]
    
    (fs/create-dir "packages/arch")
    (spit "packages/arch/PKGBUILD" pkgbuild-content)
    (log "✅ PKGBUILD created")))

(defn create-debian-package []
  "Create Debian package for APT"
  (log "Creating Debian package...")
  
  (let [control-content
        "Package: grainspace
Version: 0.1.0
Section: utils
Priority: optional
Architecture: amd64
Depends: clojure, nodejs, dfx, openjdk-11-jre
Maintainer: kae3g <kj3x39@gmail.com>
Description: Unified Decentralized Platform
 Grainspace is a unified decentralized platform that combines:
 - Identity System: Urbit Azimuth + ICP Subnet
 - Social Networks: X + Nostr + Bluesky + Threads
 - Marketplace: Apps + AI Models + Digital Art
 - Streaming: Live coding + Demos + Tutorials
 .
 This package provides the core Grainspace platform."
       
       postinst-content
        "#!/bin/bash
set -e

echo \"Installing Grainspace...\"
clojure -M:build

echo \"Grainspace installed successfully!\"
echo \"Run 'grainspace --help' to get started.\""
       
       prerm-content
        "#!/bin/bash
set -e

echo \"Removing Grainspace...\"
# Cleanup if needed

echo \"Grainspace removed successfully!\""
    
    (fs/create-dir "packages/debian/DEBIAN")
    (spit "packages/debian/DEBIAN/control" control-content)
    (spit "packages/debian/DEBIAN/postinst" postinst-content)
    (spit "packages/debian/DEBIAN/prerm" prerm-content)
    (run-command "chmod +x packages/debian/DEBIAN/postinst packages/debian/DEBIAN/prerm" :sh true)
    (log "✅ Debian package created")))

(defn test-homebrew []
  "Test Homebrew installation"
  (log "Testing Homebrew installation...")
  
  (let [formula-path "packages/homebrew/Formula/grainspace.rb"]
    (if (fs/exists? formula-path)
      (do
        (log "✅ Homebrew formula exists")
        (log "To test: brew install --build-from-source packages/homebrew/Formula/grainspace.rb")
        true)
      (do
        (log "❌ Homebrew formula not found")
        false))))

(defn test-nix []
  "Test Nix installation"
  (log "Testing Nix installation...")
  
  (let [flake-path "packages/nix/flake.nix"]
    (if (fs/exists? flake-path)
      (do
        (log "✅ Nix flake exists")
        (log "To test: cd packages/nix && nix build")
        true)
      (do
        (log "❌ Nix flake not found")
        false))))

(defn test-pacman []
  "Test Pacman installation"
  (log "Testing Pacman installation...")
  
  (let [pkgbuild-path "packages/arch/PKGBUILD"]
    (if (fs/exists? pkgbuild-path)
      (do
        (log "✅ PKGBUILD exists")
        (log "To test: cd packages/arch && makepkg -si")
        true)
      (do
        (log "❌ PKGBUILD not found")
        false))))

(defn test-apt []
  "Test APT installation"
  (log "Testing APT installation...")
  
  (let [deb-path "packages/debian"]
    (if (fs/exists? deb-path)
      (do
        (log "✅ Debian package exists")
        (log "To test: dpkg -b packages/debian grainspace.deb && sudo dpkg -i grainspace.deb")
        true)
      (do
        (log "❌ Debian package not found")
        false))))

(defn run-comprehensive-test []
  "Run comprehensive test across all package managers"
  (log "🧪 Running comprehensive package manager tests...")
  
  (let [os (detect-os)
        available-pms (filter #(not (str/blank? (check-package-manager %))) (keys package-managers))]
    
    (log (str "Detected OS: " os))
    (log (str "Available package managers: " (str/join ", " (map name available-pms))))
    
    (doseq [pm available-pms]
      (let [pm-config (get package-managers pm)
            pm-name (:name pm-config)]
        (log (str "Testing " pm-name "..."))
        
        (case pm
          :homebrew (test-homebrew)
          :nix (test-nix)
          :pacman (test-pacman)
          :apt (test-apt)
          (log (str "Unknown package manager: " pm)))))))

(defn create-test-script []
  "Create automated test script"
  (log "Creating automated test script...")
  
  (let [test-script "#!/bin/bash
# Automated Package Manager Testing Script

set -e

echo \"🧪 Starting automated package manager tests...\"

# Test Homebrew (macOS)
if command -v brew &> /dev/null; then
    echo \"Testing Homebrew...\"
    brew install --build-from-source packages/homebrew/Formula/grainspace.rb
    brew test grainspace
    brew uninstall grainspace
    echo \"✅ Homebrew test passed\"
fi

# Test Nix
if command -v nix &> /dev/null; then
    echo \"Testing Nix...\"
    cd packages/nix
    nix build
    nix run
    echo \"✅ Nix test passed\"
fi

# Test Pacman (Arch Linux)
if command -v pacman &> /dev/null; then
    echo \"Testing Pacman...\"
    cd packages/arch
    makepkg -si
    pacman -Q grainspace
    pacman -R grainspace
    echo \"✅ Pacman test passed\"
fi

# Test APT (Debian/Ubuntu)
if command -v apt &> /dev/null; then
    echo \"Testing APT...\"
    dpkg -b packages/debian grainspace.deb
    sudo dpkg -i grainspace.deb
    dpkg -l grainspace
    sudo dpkg -r grainspace
    echo \"✅ APT test passed\"
fi

echo \"🎉 All package manager tests completed!\""]
    
    (spit "scripts/test-all-package-managers.sh" test-script)
    (run-command "chmod +x scripts/test-all-package-managers.sh" :sh true)
    (log "✅ Test script created")))

(defn create-ci-config []
  "Create CI configuration for testing"
  (log "Creating CI configuration...")
  
  (let [github-actions
        "name: Multi-Package-Manager Tests

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  test-homebrew:
    runs-on: macos-latest
    steps:
    - uses: actions/checkout@v3
    - name: Test Homebrew
      run: |
        brew install --build-from-source packages/homebrew/Formula/grainspace.rb
        brew test grainspace
        brew uninstall grainspace

  test-nix:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    - name: Install Nix
      run: |
        sh <(curl -L https://nixos.org/nix/install) --no-daemon
        source ~/.nix-profile/etc/profile.d/nix.sh
    - name: Test Nix
      run: |
        cd packages/nix
        nix build
        nix run

  test-arch:
    runs-on: ubuntu-latest
    container: archlinux:latest
    steps:
    - uses: actions/checkout@v3
    - name: Test Pacman
      run: |
        pacman -Syu --noconfirm
        pacman -S --noconfirm base-devel git clojure nodejs dfx jre-openjdk
        cd packages/arch
        makepkg -si
        pacman -Q grainspace
        pacman -R grainspace

  test-debian:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    - name: Test APT
      run: |
        sudo apt update
        sudo apt install -y clojure nodejs dfx openjdk-11-jre
        dpkg -b packages/debian grainspace.deb
        sudo dpkg -i grainspace.deb
        dpkg -l grainspace
        sudo dpkg -r grainspace"]
    
    (fs/create-dir ".github/workflows")
    (spit ".github/workflows/package-manager-tests.yml" github-actions)
    (log "✅ CI configuration created")))

(defn main []
  "Main testing function"
  (log "🚀 Setting up Multi-Package-Manager Testing System")
  (log "   Homebrew (macOS)")
  (log "   Nix (Linux/macOS)")
  (log "   Pacman (Arch Linux)")
  (log "   APT (Debian/Ubuntu)")
  
  (try
    ;; Create package directories
    (fs/create-dir "packages/homebrew/Formula")
    (fs/create-dir "packages/nix")
    (fs/create-dir "packages/arch")
    (fs/create-dir "packages/debian/DEBIAN")
    
    ;; Create package files
    (create-homebrew-formula)
    (create-nix-flake)
    (create-pkgbuild)
    (create-debian-package)
    
    ;; Create testing infrastructure
    (create-test-script)
    (create-ci-config)
    
    ;; Run comprehensive test
    (run-comprehensive-test)
    
    (log "")
    (log "✅ Multi-package-manager testing system complete!")
    (log "")
    (log "🧪 To test all package managers:")
    (log "   ./scripts/test-all-package-managers.sh")
    (log "")
    (log "📦 Package files created:")
    (log "   packages/homebrew/Formula/grainspace.rb")
    (log "   packages/nix/flake.nix")
    (log "   packages/arch/PKGBUILD")
    (log "   packages/debian/DEBIAN/control")
    (log "")
    (log "🔄 CI/CD configured:")
    (log "   .github/workflows/package-manager-tests.yml")
    
    (catch Exception e
      (log (str "❌ Setup failed: " (.getMessage e)))
      (System/exit 1))))

;; Run the testing system
(main)

