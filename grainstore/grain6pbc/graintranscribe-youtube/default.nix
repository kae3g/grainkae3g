{ pkgs ? import <nixpkgs> {}
, lib ? pkgs.lib
, stdenv ? pkgs.stdenv
, leiningen ? pkgs.leiningen
, babashka ? pkgs.babashka
, jdk ? pkgs.jdk17
}:

let
  # Infuse pattern: template/personal configuration separation
  # Inspired by sixos/lib/default.nix infuse.nix approach
  
  infuseConfig = template: personal:
    if lib.isAttrs template && lib.isAttrs personal
    then lib.mapAttrs
      (name: templateValue:
        if personal ? ${name}
        then infuseConfig templateValue personal.${name}
        else templateValue)
      template
    else personal;
  
  templateConfig = {
    gemini = {
      model = "gemini-2.5-pro-latest";
      temperature = 0.2;
      max-tokens = 1000000;
    };
    output = {
      dir = "personal/transcriptions";
      format = "markdown";
      include-timestamps = true;
      include-speakers = true;
      include-summary = true;
    };
    youtube = {
      extract-metadata = true;
      language-hint = "en";
    };
  };
  
  # Personal config would be loaded at runtime from personal/config.edn
  # This is just for Nix build-time configuration
  
in stdenv.mkDerivation rec {
  pname = "graintranscribe-youtube";
  version = "0.1.0";
  
  src = ./.;
  
  nativeBuildInputs = [ leiningen babashka jdk ];
  
  buildPhase = ''
    export HOME=$TMPDIR
    export LEIN_HOME=$HOME/.lein
    
    # Build uberjar with Leiningen
    lein uberjar
    
    # Verify Babashka scripts
    bb --version
  '';
  
  installPhase = ''
    mkdir -p $out/{bin,lib,share/graintranscribe}
    
    # Install uberjar
    cp target/uberjar/graintranscribe-youtube-*-standalone.jar \
       $out/lib/graintranscribe-youtube.jar
    
    # Install Babashka scripts
    cp -r scripts $out/share/graintranscribe/
    cp -r src $out/share/graintranscribe/
    cp bb.edn $out/share/graintranscribe/
    
    # Install template config
    mkdir -p $out/share/graintranscribe/template
    cp template/config.edn $out/share/graintranscribe/template/
    
    # Create wrapper script
    cat > $out/bin/graintranscribe <<EOF
#!/usr/bin/env bash
# Graintranscribe-YouTube launcher
set -euo pipefail

GRAINTRANSCRIBE_HOME="\''${GRAINTRANSCRIBE_HOME:-\$HOME/.config/graintranscribe}"
GRAINTRANSCRIBE_SHARE="$out/share/graintranscribe"

# Use Babashka for CLI
cd "\$GRAINTRANSCRIBE_SHARE"
${babashka}/bin/bb "\$@"
EOF
    
    chmod +x $out/bin/graintranscribe
  '';
  
  meta = with lib; {
    description = "YouTube video transcription using Gemini 2.5 Pro";
    homepage = "https://github.com/grainpbc/graintranscribe-youtube";
    license = licenses.mit;
    maintainers = [ "kae3g" ];
    platforms = platforms.unix;
    
    # Grainpbc metadata
    grainpbc = {
      module-type = "template";
      separation = "template-personal";
      infuse-pattern = true;
      dependencies = [ "grainconfig" "grainenvvars" ];
      external-apis = [ "gemini-2.5-pro" ];
    };
  };
}

# Nix derivation for graintranscribe-youtube
# Inspired by sixos modular pattern with infuse configuration
# 
# Build: nix-build
# Install: nix-env -if .
# Shell: nix-shell
#
# Template/personal separation maintained through runtime config loading
