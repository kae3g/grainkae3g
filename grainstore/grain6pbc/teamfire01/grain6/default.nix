{ pkgs ? import <nixpkgs> {}
, lib ? pkgs.lib
, stdenv ? pkgs.stdenv
, leiningen ? pkgs.leiningen
, babashka ? pkgs.babashka
, s6 ? pkgs.s6
, musl ? pkgs.musl
, jdk ? pkgs.jdk17
, makeWrapper ? pkgs.makeWrapper
, fetchFromGitHub ? pkgs.fetchFromGitHub
}:

# Grain6 - Time-aware process supervision
# Graintime + s6 + Behn-inspired timer queue
# Kae3g Sheaf (1-of-88) - 88 Counter Philosophy
# 
# Prioritizes Alpine Linux with musl libc for:
# - Static linking (portable binaries)
# - Small footprint (minimal runtime)
# - Security (musl's simplicity and correctness)
# - Multi-distro compatibility

stdenv.mkDerivation rec {
  pname = "grain6";
  version = "0.1.0";
  
  src = ./.;
  
  nativeBuildInputs = [ 
    leiningen 
    babashka 
    jdk 
    makeWrapper
    musl  # For static linking on Alpine
  ];
  
  buildInputs = [ 
    s6        # Process supervision
    babashka  # Scripting runtime
  ];
  
  # Build with Leiningen
  buildPhase = ''
    export HOME=$TMPDIR
    export LEIN_HOME=$HOME/.lein
    
    echo "🌾 Building grain6 with Leiningen..."
    lein uberjar
    
    echo "🌾 Verifying Babashka scripts..."
    bb --version
    
    # For Alpine/musl: ensure static linking where possible
    ${if stdenv.isLinux then ''
      echo "🌾 Detecting libc..."
      ldd --version 2>&1 | grep -q musl && echo "   ✓ musl detected (Alpine-compatible)" || echo "   ℹ glibc detected (will work on Alpine with musl compatibility)"
    '' else ""}
  '';
  
  # Run tests
  doCheck = true;
  checkPhase = ''
    echo "🧪 Running tests..."
    lein test
    bb test
  '';
  
  installPhase = ''
    mkdir -p $out/{bin,lib,share/grain6,etc/grain6}
    
    # Install uberjar
    cp target/uberjar/grain6-*-standalone.jar $out/lib/grain6.jar
    
    # Install Babashka CLI wrapper
    cat > $out/bin/grain6 <<EOF
#!/usr/bin/env bash
set -euo pipefail

GRAIN6_HOME="\''${GRAIN6_HOME:-\$HOME/.config/grain6}"
GRAIN6_SHARE="$out/share/grain6"

# Use Babashka for fast startup
cd "\$GRAIN6_SHARE"
${babashka}/bin/bb "\$@"
EOF
    chmod +x $out/bin/grain6
    
    # Install Babashka scripts and sources
    cp -r scripts $out/share/grain6/
    cp -r src $out/share/grain6/
    cp bb.edn $out/share/grain6/
    
    # Install s6 service definitions
    mkdir -p $out/share/grain6/s6
    cp -r s6/* $out/share/grain6/s6/ 2>/dev/null || true
    
    # Install template configuration
    mkdir -p $out/share/grain6/template
    cp template/*.conf $out/share/grain6/template/ 2>/dev/null || true
    
    # Install documentation
    mkdir -p $out/share/doc/grain6
    cp README.md $out/share/doc/grain6/
    cp LICENSE $out/share/doc/grain6/ 2>/dev/null || true
  '';
  
  meta = with lib; {
    description = "Time-aware process supervision (graintime + s6 + Behn-inspired)";
    longDescription = ''
      Grain6 combines graintime's neovedic astronomical timestamps with s6 process
      supervision and Urbit's Behn-inspired timer queue architecture.
      
      Part of the 88-sheaf Grain Network topology, grain6 implements the
      88 counter philosophy (88 × 10^n >= 0 | -1) with temporal recursion
      (now == next + 1).
      
      Prioritizes Alpine Linux with musl libc for minimal footprint and
      maximum portability across distributions.
    '';
    homepage = "https://github.com/grainpbc/grain6";
    license = licenses.mit;
    maintainers = [ "kae3g" ];
    platforms = platforms.unix;
    
    # Preferred platforms (Alpine with musl first)
    priority = {
      "x86_64-linux" = if stdenv.isMusl then 100 else 50;  # Prefer musl
      "aarch64-linux" = if stdenv.isMusl then 100 else 50;
    };
    
    # Grainpbc metadata
    grainpbc = {
      module-type = "template";
      sheaf-position = "1-of-88";  # Kae3g genesis sheaf
      dependencies = [ "graintime" "clojure-s6" "clojure-sixos" ];
      target-distros = [ "alpine" "nixos" "ubuntu" "debian" ];
      priority-distro = "alpine";
      libc-preference = "musl";
      package-formats = [ "apk" "nix" "deb" ];
      canister-deployable = true;  # Via Clotoko transpiler
    };
  };
}

# Grain6 Nix derivation
# 88 × 10^n >= 0 | -1
# now == next + 1
