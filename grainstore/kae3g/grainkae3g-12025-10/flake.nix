{
    description = "kae3g: Experimental aspiringly helpful generative AI writings";
  
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-24.11";
  };
  
  outputs = { self, nixpkgs }:
    let
      forAll = nixpkgs.lib.genAttrs [
        "x86_64-linux"
        "aarch64-darwin"
        "x86_64-darwin"
      ];
    in {
      devShells = forAll (system:
        let
          pkgs = import nixpkgs { inherit system; };
        in {
          default = pkgs.mkShell {
            buildInputs = [
              # Core Clojure/Babashka toolchain
              # for content pipeline development
              pkgs.babashka
              pkgs.clojure
              pkgs.jdk17
              
              # Code quality tools with
              # 57-char hard wrap awareness
              pkgs.zprint
              pkgs.clj-kondo
              
              # Node.js runtime for SvelteKit/Vite
              # (npm fallbacks for packages not
              # available in nixpkgs)
              pkgs.nodejs_20
              
              # Nix-available Node.js packages
              # (prefer these over npm when
              # available for reproducibility)
              pkgs.nodePackages.svelte-check
              pkgs.nodePackages.svelte-language-server
              pkgs.nodePackages.eslint
              pkgs.nodePackages.prettier
              pkgs.nodePackages.typescript
              pkgs.nodePackages.typescript-language-server
              
              # Git for version control with
              # contemplative practice
              pkgs.git
            ];
            
            shellHook = ''
              echo "🌿 kae3g content pipeline"
              echo "   Sacred writings + technical docs"
              echo ""
              echo "📚 Available commands:"
              echo "   bb doctor          - Check toolchain"
              echo "   bb writings:build  - Build writings → JSON"
              echo "   bb writings:watch  - Watch and rebuild"
              echo "   bb serve:dev       - Start dev server"
              echo ""
              echo "✨ Contemplative technology in service"
              echo "   of clarity and beauty..."
              echo ""
            '';
          };
        });
    };
}

