# 🌾 GrainKae3g NixOS Flake Configuration
# NixOS 25.11 Unstable with Home Manager
#
# Usage:
#   sudo nixos-rebuild switch --flake .#nixos-grainkae3g

{
  description = "GrainKae3g NixOS Configuration with Home Manager";

  inputs = {
    # NixOS unstable
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    
    # Home Manager
    home-manager = {
      url = "github:nix-community/home-manager";
      inputs.nixpkgs.follows = "nixpkgs";
    };
  };

  outputs = { self, nixpkgs, home-manager, ... }@inputs: {
    nixosConfigurations = {
      nixos-grainkae3g = nixpkgs.lib.nixosSystem {
        system = "x86_64-linux";
        
        modules = [
          # Main configuration
          ./grainkae3g-grain6-wifi.nix
          
          # Home Manager NixOS module
          home-manager.nixosModules.home-manager
          {
            home-manager.useGlobalPkgs = true;
            home-manager.useUserPackages = true;
            home-manager.users.xy = import ./home.nix;
          }
        ];
      };
      
      # Minimal bootstrap configuration
      grainkae3g-minimal = nixpkgs.lib.nixosSystem {
        system = "x86_64-linux";
        
        modules = [
          # Minimal configuration
          ./grainkae3g-minimal.nix
        ];
      };
    };
  };
}
