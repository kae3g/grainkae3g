# 🌾 Home Manager Configuration for xy@nixos-grainkae3g
# Grain Network developer environment

{ config, pkgs, ... }:

{
  # ═══════════════════════════════════════════════════════════
  # HOME MANAGER CONFIGURATION
  # ═══════════════════════════════════════════════════════════
  
  # Home Manager needs a bit of information about you and the paths it should manage
  home.username = "xy";
  home.homeDirectory = "/home/xy";

  # This value determines the Home Manager release compatibility
  # Don't change this unless you know what you're doing!
  home.stateVersion = "25.11";

  # ═══════════════════════════════════════════════════════════
  # PACKAGES (User-specific)
  # ═══════════════════════════════════════════════════════════
  
  home.packages = with pkgs; [
    # Grain Network tools (user-specific)
    babashka
    clojure
    leiningen
    
    # Development
    ripgrep
    fd
    fzf
    bat
    eza
    
    # Network tools
    mtr
    iperf3
  ];

  # ═══════════════════════════════════════════════════════════
  # ZSH CONFIGURATION
  # ═══════════════════════════════════════════════════════════
  
  programs.zsh = {
    enable = true;
    enableCompletion = true;
    autosuggestion.enable = true;
    syntaxHighlighting.enable = true;
    
    oh-my-zsh = {
      enable = true;
      plugins = [ "git" "sudo" "direnv" ];
      theme = "robbyrussell";
    };
    
    shellAliases = {
      # Grain Network aliases
      grain = "cd /mnt/grainkae3g";
      gb = "cd /mnt/grainkae3g/grainstore/grainbarrel && bb";
      qb = "bb qb";
      gt = "bb gt";
      kg = "bb kg";
      fr = "bb fr";
      kk = "bb qb-kk";
      
      # Common utilities
      ll = "eza -la";
      ls = "eza";
      cat = "bat";
    };
    
    initExtra = ''
      # Grain Network environment
      export GRAINPATH=/mnt/grainkae3g
      export GRAINTIME_DEFAULT_LOCATION=caspar-ca
      
      # Add Grain Network binaries to PATH
      export PATH="$GRAINPATH/grainstore/grainbarrel:$PATH"
    '';
  };

  # ═══════════════════════════════════════════════════════════
  # GIT CONFIGURATION
  # ═══════════════════════════════════════════════════════════
  
  programs.git = {
    enable = true;
    userName = "kae3g";
    userEmail = "kae3g@users.noreply.github.com";
    
    extraConfig = {
      init.defaultBranch = "main";
      pull.rebase = false;
    };
  };

  # ═══════════════════════════════════════════════════════════
  # DIRENV (for .envrc support)
  # ═══════════════════════════════════════════════════════════
  
  programs.direnv = {
    enable = true;
    nix-direnv.enable = true;
  };

  # ═══════════════════════════════════════════════════════════
  # HOME MANAGER SETTINGS
  # ═══════════════════════════════════════════════════════════
  
  # Let Home Manager manage itself
  programs.home-manager.enable = true;
}
