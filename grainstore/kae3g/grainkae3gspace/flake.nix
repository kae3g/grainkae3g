{
  description = "Grainspace - Unified decentralized platform for the Grain Network";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
        
        # Clojure dependencies
        clojureDeps = with pkgs; [
          clojure
          babashka
          leiningen
        ];
        
        # ICP (DFINITY) dependencies
        icpDeps = with pkgs; [
          # DFX SDK for ICP development
          # Note: DFX is not in nixpkgs, we'll need to install it separately
          # See: https://internetcomputer.org/docs/current/developer-docs/setup/install/
        ];
        
        # Development dependencies
        devDeps = with pkgs; [
          git
          nodejs
          nodePackages.npm
          jdk17
        ];
        
        # Runtime dependencies
        runtimeDeps = with pkgs; [
          bash
          coreutils
          findutils
          gnugrep
          gnused
        ];

      in
      {
        # Development shell
        devShells.default = pkgs.mkShell {
          buildInputs = clojureDeps ++ devDeps ++ runtimeDeps;
          
          shellHook = ''
            echo "🌾 Grainspace Development Environment"
            echo "Clojure version: $(clojure --version)"
            echo "Babashka version: $(bb --version)"
            echo "Node version: $(node --version)"
            echo ""
            echo "Available commands:"
            echo "  bb dev        - Start development server"
            echo "  bb test       - Run tests"
            echo "  bb build      - Build production bundle"
            echo "  bb deploy     - Deploy to ICP canister"
            echo ""
            
            # Set up environment variables
            export GRAINSPACE_ENV=development
            export GRAINSPACE_DATA_DIR=$PWD/data
            export GRAINSPACE_CONFIG_DIR=$PWD/config
            
            # Create necessary directories
            mkdir -p $GRAINSPACE_DATA_DIR
            mkdir -p $GRAINSPACE_CONFIG_DIR
          '';
        };

        # Package definition for Grainspace
        packages.default = pkgs.stdenv.mkDerivation {
          pname = "grainspace";
          version = "0.1.0";
          
          src = ./.;
          
          buildInputs = clojureDeps ++ runtimeDeps;
          
          buildPhase = ''
            echo "Building Grainspace..."
            
            # Build Clojure application
            clojure -M:build
            
            # Build ICP canisters (if dfx is available)
            if command -v dfx &> /dev/null; then
              dfx build
            else
              echo "Warning: dfx not found, skipping canister build"
            fi
          '';
          
          installPhase = ''
            mkdir -p $out/bin
            mkdir -p $out/lib
            mkdir -p $out/share/grainspace
            
            # Install Clojure application
            cp -r target/* $out/lib/
            
            # Install binaries
            cat > $out/bin/grainspace <<EOF
#!/usr/bin/env bash
exec ${pkgs.clojure}/bin/clojure -M -m grainspace.core "\$@"
EOF
            chmod +x $out/bin/grainspace
            
            # Install ICP canisters (if built)
            if [ -d ".dfx" ]; then
              cp -r .dfx $out/share/grainspace/
            fi
          '';
          
          meta = with pkgs.lib; {
            description = "Grainspace - Unified decentralized platform for the Grain Network";
            homepage = "https://grain.network";
            license = licenses.mit;
            maintainers = [ "kae3g" ];
            platforms = platforms.unix;
          };
        };

        # NixOS module for Grainspace
        nixosModules.default = { config, lib, pkgs, ... }:
          with lib;
          let
            cfg = config.services.grainspace;
          in {
            options.services.grainspace = {
              enable = mkEnableOption "Grainspace service";
              
              package = mkOption {
                type = types.package;
                default = self.packages.${system}.default;
                description = "Grainspace package to use";
              };
              
              dataDir = mkOption {
                type = types.str;
                default = "/var/lib/grainspace";
                description = "Directory for Grainspace data";
              };
              
              user = mkOption {
                type = types.str;
                default = "grainspace";
                description = "User to run Grainspace as";
              };
              
              group = mkOption {
                type = types.str;
                default = "grainspace";
                description = "Group to run Grainspace as";
              };
            };
            
            config = mkIf cfg.enable {
              users.users.${cfg.user} = {
                isSystemUser = true;
                group = cfg.group;
                home = cfg.dataDir;
                createHome = true;
              };
              
              users.groups.${cfg.group} = {};
              
              systemd.services.grainspace = {
                description = "Grainspace Service";
                after = [ "network.target" ];
                wantedBy = [ "multi-user.target" ];
                
                serviceConfig = {
                  Type = "simple";
                  User = cfg.user;
                  Group = cfg.group;
                  WorkingDirectory = cfg.dataDir;
                  ExecStart = "${cfg.package}/bin/grainspace serve";
                  Restart = "on-failure";
                  RestartSec = "5s";
                  
                  # Security hardening
                  NoNewPrivileges = true;
                  PrivateTmp = true;
                  ProtectSystem = "strict";
                  ProtectHome = true;
                  ReadWritePaths = [ cfg.dataDir ];
                };
                
                environment = {
                  GRAINSPACE_DATA_DIR = cfg.dataDir;
                  GRAINSPACE_ENV = "production";
                };
              };
            };
          };

        # SixOS configuration (for s6 supervision)
        # Note: This is conceptual, as SixOS doesn't have a standardized flake format yet
        sixosModules.default = {
          services.grainspace = {
            enable = true;
            supervision = "s6";
            
            s6-rc = {
              name = "grainspace";
              type = "longrun";
              
              run = ''
                #!/bin/sh
                exec clojure -M -m grainspace.core
              '';
              
              dependencies = [ "network" ];
            };
          };
        };

        # Apps for easy running
        apps.default = {
          type = "app";
          program = "${self.packages.${system}.default}/bin/grainspace";
        };

        # Formatter
        formatter = pkgs.nixpkgs-fmt;
      }
    );
}


