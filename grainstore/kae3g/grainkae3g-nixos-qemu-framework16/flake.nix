{
  description = "NixOS QEMU configuration for Framework 16 laptop on Ubuntu 24.04 LTS";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-23.11";
    nixpkgs-unstable.url = "github:NixOS/nixpkgs/nixos-unstable";
    
    home-manager = {
      url = "github:nix-community/home-manager/release-23.11";
      inputs.nixpkgs.follows = "nixpkgs";
    };
  };

  outputs = { self, nixpkgs, nixpkgs-unstable, home-manager, ... }@inputs: {
    nixosConfigurations.nixos-qemu-fw16 = nixpkgs.lib.nixosSystem {
      system = "x86_64-linux";
      
      modules = [
        # Main system configuration
        ./configuration.nix
        
        # Hardware configuration (VM-specific)
        ./hardware-configuration.nix
        
        # Sway configuration
        ./sway.nix
        
        # Home Manager module
        home-manager.nixosModules.home-manager
        {
          home-manager.useGlobalPkgs = true;
          home-manager.useUserPackages = true;
          home-manager.users.nixos = import ./home.nix;
          
          # Pass inputs to home-manager
          home-manager.extraSpecialArgs = { inherit inputs; };
        }
        
        # Framework 16 specific optimizations
        {
          # Enable AMD-specific features
          boot.kernelParams = [
            "amd_pstate=active"
            "amdgpu.ppfeaturemask=0xffffffff"
          ];
          
          # AMD microcode
          hardware.cpu.amd.updateMicrocode = true;
          
          # Enable nested virtualization
          boot.extraModprobeConfig = ''
            options kvm_amd nested=1
          '';
        }
      ];
      
      specialArgs = { inherit inputs; };
    };
    
    # Development shell for working with this flake
    devShells.x86_64-linux.default = nixpkgs.legacyPackages.x86_64-linux.mkShell {
      buildInputs = with nixpkgs.legacyPackages.x86_64-linux; [
        nixFlakes
        git
        vim
      ];
      
      shellHook = ''
        echo "🌾 NixOS QEMU Framework 16 Development Shell"
        echo "Commands:"
        echo "  nixos-rebuild switch --flake .#nixos-qemu-fw16"
        echo "  nix flake update"
        echo "  nix flake check"
      '';
    };
  };
}


