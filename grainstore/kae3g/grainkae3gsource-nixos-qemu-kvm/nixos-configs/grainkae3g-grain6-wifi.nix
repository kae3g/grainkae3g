# 🌾 GrainKae3g NixOS Configuration with grain6 + grainwifi
# NixOS 25.11 Unstable - Grain Network Development Environment
# Framework 16 Ubuntu 24.04 LTS Host → NixOS QEMU/KVM Guest
#
# Reference: https://nixos.org/manual/nixos/unstable/

{ config, pkgs, lib, ... }:

{
  imports = [ ./hardware-configuration.nix ];

  # ═══════════════════════════════════════════════════════════
  # MUSL LIB C PRIORITIZATION (Alpine Linux Compatibility)
  # ═══════════════════════════════════════════════════════════
  
  # Use musl libc for specific packages (Alpine Linux compatibility)
  nixpkgs.overlays = [
    (self: super: {
      # Use musl-built versions of lightweight packages
      musl-packages = super.pkgsMusl;
      
      # Override specific packages to use musl
      abduco = super.pkgsMusl.abduco;
      dtach = super.pkgsMusl.dtach;
      zellij = super.pkgsMusl.zellij;
      
      # Wayland compositor with musl support
      sway = super.pkgsMusl.sway;
      waybar = super.pkgsMusl.waybar;
      
      # Terminal emulators with musl
      alacritty = super.pkgsMusl.alacritty;
      foot = super.pkgsMusl.foot;
    })
  ];
  
  # Enable musl-based builds for development tools
  nixpkgs.config = {
    # Allow musl packages
    allowUnfree = true;
    
    # Use musl for statically-linked binaries
    packageOverrides = pkgs: {
      # Grain Network tools with musl
      grain6-musl = pkgs.pkgsMusl.babashka;
      grainwifi-musl = pkgs.pkgsMusl.babashka;
    };
  };

  # ═══════════════════════════════════════════════════════════
  
  boot.loader.systemd-boot.enable = true;
  boot.loader.efi.canTouchEfiVariables = true;
  
  # QEMU/KVM guest modules
  boot.initrd.availableKernelModules = [ "9p" "9pnet_virtio" "virtio_pci" "virtio_blk" ];
  boot.kernelModules = [ "kvm-amd" ];  # AMD virtualization

  # ═══════════════════════════════════════════════════════════
  # NETWORKING
  # ═══════════════════════════════════════════════════════════
  
  networking = {
    hostName = "nixos-grainkae3g";
    networkmanager.enable = true;
    
    # Open ports for development
    firewall = {
      enable = true;
      allowedTCPPorts = [
        22    # SSH
        3000  # Common dev server
        8000  # HTTP alternate
        8080  # HTTP alternate
        9090  # Prometheus (if needed)
      ];
    };
  };

  # ═══════════════════════════════════════════════════════════
  # LOCALIZATION (Forest Cabin, Central Illinois)
  # ═══════════════════════════════════════════════════════════
  
  time.timeZone = "America/Chicago";  # CST/CDT
  
  i18n = {
    defaultLocale = "en_US.UTF-8";
    extraLocaleSettings = {
      LC_ADDRESS = "en_US.UTF-8";
      LC_IDENTIFICATION = "en_US.UTF-8";
      LC_MEASUREMENT = "en_US.UTF-8";
      LC_MONETARY = "en_US.UTF-8";
      LC_NAME = "en_US.UTF-8";
      LC_NUMERIC = "en_US.UTF-8";
      LC_PAPER = "en_US.UTF-8";
      LC_TELEPHONE = "en_US.UTF-8";
      LC_TIME = "en_US.UTF-8";
    };
  };
  
  console = {
    font = "Lat2-Terminus16";
    keyMap = "us";
  };

  # ═══════════════════════════════════════════════════════════
  # USERS
  # ═══════════════════════════════════════════════════════════
  
  users.users.xy = {
    isNormalUser = true;
    description = "kae3g - Grain Network Developer";
    extraGroups = [
      "wheel"           # sudo access
      "networkmanager"  # network management
      "libvirtd"        # VM management (if nested)
      "docker"          # container management (if needed)
    ];
    shell = pkgs.zsh;
    
    # SSH keys (add your public key)
    openssh.authorizedKeys.keys = [
      # "ssh-ed25519 AAAA... your-email@example.com"
    ];
  };
  
  # Enable sudo without password for wheel group (development VM)
  security.sudo.wheelNeedsPassword = false;

  # ═══════════════════════════════════════════════════════════
  # PACKAGES - Grain Network Dependencies
  # ═══════════════════════════════════════════════════════════
  
  environment.systemPackages = with pkgs; [
    # ────────────────────────────────────────────────────────
    # Development IDEs and Editors
    # ────────────────────────────────────────────────────────
    vscode neovim emacs
    ripgrep fd fzf bat eza zoxide
    tmux screen
    
    # ────────────────────────────────────────────────────────
    # Musl-based Terminal Multiplexers (Alpine Compatible)
    # ────────────────────────────────────────────────────────
    abduco dtach zellij
    
    # ────────────────────────────────────────────────────────
    # Musl-based Wayland Compositor (Alpine Compatible)
    # ────────────────────────────────────────────────────────
    sway waybar
    
    # ────────────────────────────────────────────────────────
    # Musl-based Terminal Emulators (Alpine Compatible)
    # ────────────────────────────────────────────────────────
    alacritty foot
    
    # ────────────────────────────────────────────────────────
    # Grain Network - Clojure Ecosystem
    # ────────────────────────────────────────────────────────
    babashka        # Fast Clojure scripting (grain6, grainwifi)
    clojure         # Clojure CLI
    leiningen       # Clojure project management
    jdk17           # Java for Clojure/HumbleUI
    
    # ────────────────────────────────────────────────────────
    # Development Tools
    # ────────────────────────────────────────────────────────
    direnv nix-direnv   # Environment management
    nodejs_20           # Node.js for web dev
    python3 python3Packages.pip
    gcc gnumake cmake
    
    # ────────────────────────────────────────────────────────
    # Network Tools (for grainwifi)
    # ────────────────────────────────────────────────────────
    networkmanager networkmanagerapplet
    bind            # dig, nslookup
    inetutils       # ping, traceroute
    mtr             # Network diagnostic
    iperf3          # Bandwidth testing
    
    # ────────────────────────────────────────────────────────
    # QEMU Guest Tools
    # ────────────────────────────────────────────────────────
    qemu_kvm
    spice-vdagent   # Better clipboard/display integration
    
    # ────────────────────────────────────────────────────────
    # HumbleUI Dependencies
    # ────────────────────────────────────────────────────────
    mesa libGL libGLU
    xorg.libX11 xorg.libXext xorg.libXrender
    
    # ────────────────────────────────────────────────────────
    # Grain Network Utilities
    # ────────────────────────────────────────────────────────
    jq              # JSON processing
    yq              # YAML processing
    libnotify       # Desktop notifications (for grainwifi)
  ];

  # ═══════════════════════════════════════════════════════════
  # SERVICES
  # ═══════════════════════════════════════════════════════════
  
  services.openssh = {
    enable = true;
    settings = {
      PermitRootLogin = "no";
      PasswordAuthentication = false;  # Keys only
    };
  };
  
  # QEMU Guest Agent
  services.qemuGuest.enable = true;
  
  # SPICE agent for better VM integration
  services.spice-vdagentd.enable = true;

  # ═══════════════════════════════════════════════════════════
  # DESKTOP ENVIRONMENT (Sway Wayland + GNOME fallback)
  # ═══════════════════════════════════════════════════════════
  
  # Sway Wayland compositor (musl-compatible, Alpine-like)
  programs.sway = {
    enable = true;
    wrapperFeatures.gtk = true;
    extraPackages = with pkgs; [
      swaylock swayidle
      waybar wofi
      alacritty foot
    ];
  };
  
  # X11 fallback for GNOME (if needed)
  services.xserver = {
    enable = true;
    
    # Display Manager
    displayManager.gdm.enable = true;
    
    # Desktop Environment
    desktopManager.gnome.enable = true;
    
    # Keyboard layout
    xkb = {
      layout = "us";
      variant = "";
    };
  };
  
  # Enable sound (modern approach)
  hardware.alsa.enable = true;
  services.pulseaudio.enable = false;  # Use PipeWire instead
  
  services.pipewire = {
    enable = lib.mkForce true;
    alsa.enable = true;
    alsa.support32Bit = true;
    pulse.enable = true;
  };

  # ═══════════════════════════════════════════════════════════
  # SHELL CONFIGURATION
  # ═══════════════════════════════════════════════════════════
  
  programs.zsh = {
    enable = true;
    enableCompletion = true;
    autosuggestions.enable = true;
    syntaxHighlighting.enable = true;
  };
  
  programs.direnv = {
    enable = true;
    nix-direnv.enable = true;
  };

  # ═══════════════════════════════════════════════════════════
  # FILE SYSTEMS (VM Root + Shared Folder)
  # ═══════════════════════════════════════════════════════════
  
  # Root filesystem (VM disk)
  fileSystems."/" = {
    device = "/dev/disk/by-label/nixos";
    fsType = "ext4";
  };
  
  # Boot filesystem (VM disk)
  fileSystems."/boot" = {
    device = "/dev/disk/by-label/BOOT";
    fsType = "vfat";
  };

  # ═══════════════════════════════════════════════════════════
  
  fileSystems."/mnt/grainkae3g" = {
    device = "grainkae3g";  # Mount tag from virt-manager
    fsType = "9p";
    options = [
      "trans=virtio"          # Use virtio transport
      "version=9p2000.L"      # Linux variant
      "msize=104857600"       # 100MB message size (performance)
      "cache=loose"           # Better performance (use with caution)
      "rw"                    # Read-write
    ];
  };
  
  # Create mount point on system activation
  system.activationScripts.mkGrainkaeMnt = ''
    mkdir -p /mnt/grainkae3g
    chown xy:users /mnt/grainkae3g
  '';
  
  # Symlink from home directory for convenience
  system.activationScripts.lnGrainkaeHome = ''
    ln -sfn /mnt/grainkae3g /home/xy/grainkae3g
    chown -h xy:users /home/xy/grainkae3g
  '';

  # ═══════════════════════════════════════════════════════════
  # GRAIN6 SYSTEMD SERVICES
  # ═══════════════════════════════════════════════════════════
  
  # grain6 daemon (time-aware process supervision)
  systemd.services.grain6 = {
    description = "grain6 - Time-Aware Process Supervision";
    after = [ "network.target" ];
    wantedBy = [ "multi-user.target" ];
    
    serviceConfig = {
      Type = "simple";
      User = "xy";
      WorkingDirectory = "/mnt/grainkae3g/grainstore/grain6";
      ExecStart = "${pkgs.babashka}/bin/bb grain6:daemon";
      Restart = "always";
      RestartSec = 10;
    };
    
    environment = {
      HOME = "/home/xy";
      PATH = lib.mkForce (lib.makeBinPath (with pkgs; [ babashka clojure jdk17 ]));
    };
  };
  
  # grainwifi daemon (dual-wifi management)
  systemd.services.grainwifi = {
    description = "grainwifi - Dual-WiFi Manager (Starlink + Cellular)";
    after = [ "network.target" "NetworkManager.service" ];
    wantedBy = [ "multi-user.target" ];
    
    serviceConfig = {
      Type = "simple";
      User = "xy";
      WorkingDirectory = "/mnt/grainkae3g/grainstore/grainwifi";
      ExecStart = "${pkgs.babashka}/bin/bb grainwifi:start --foreground";
      Restart = "always";
      RestartSec = 30;
    };
    
    environment = {
      HOME = "/home/xy";
      DISPLAY = ":0";  # For desktop notifications
      PATH = lib.mkForce (lib.makeBinPath (with pkgs; [ babashka networkmanager libnotify ]));
    };
  };
  
  # Optional: Auto-start HumbleUI on login (user service)
  # systemd.user.services.grainwifi-humble = {
  #   description = "GrainWiFi HumbleUI Interface";
  #   after = [ "graphical-session.target" ];
  #   wantedBy = [ "graphical-session.target" ];
  #   
  #   serviceConfig = {
  #     Type = "simple";
  #     WorkingDirectory = "/mnt/grainkae3g/grainstore/grainwifi";
  #     ExecStart = "${pkgs.babashka}/bin/bb humble-ui:start";
  #     Restart = "on-failure";
  #   };
  # };

  # ═══════════════════════════════════════════════════════════
  # ENVIRONMENT VARIABLES
  # ═══════════════════════════════════════════════════════════
  
  environment.variables = {
    EDITOR = "vim";
    VISUAL = "vim";
    GRAINPATH = "/mnt/grainkae3g";
    GRAINTIME_DEFAULT_LOCATION = "forest";  # Default graintime location
  };
  
  environment.shellAliases = {
    grain = "cd /mnt/grainkae3g";
    gb = "cd /mnt/grainkae3g/grainstore/grainbarrel && bb";
    qb = "bb qb";
    gt = "bb gt";
    kg = "bb kg";
    fr = "bb fr";
    kk = "bb qb-kk";
  };

  # ═══════════════════════════════════════════════════════════
  # NIX CONFIGURATION (FLAKES ENABLED!)
  # ═══════════════════════════════════════════════════════════
  
  nix = {
    settings = {
      # Enable flakes and nix-command (experimental features)
      # Required for Home Manager with flakes
      experimental-features = [ "nix-command" "flakes" ];
      
      # Optimize store automatically
      auto-optimise-store = true;
      
      # Trusted users (for flakes)
      trusted-users = [ "root" "@wheel" ];
    };
    
    # Garbage collection
    gc = {
      automatic = true;
      dates = "weekly";
      options = "--delete-older-than 7d";
    };
    
    # Registry for flakes (commented out - not needed for basic setup)
    # registry = {
    #   nixpkgs.flake = nixpkgs;
    # };
  };
  
  # Allow unfree packages (if needed)
  # nixpkgs.config.allowUnfree = true;  # Already defined above

  # ═══════════════════════════════════════════════════════════
  # NIXOS VERSION
  # ═══════════════════════════════════════════════════════════
  
  # This value determines the NixOS release from which the default
  # settings for stateful data, like file locations and database versions
  # on your system were taken. It's perfectly fine and recommended to leave
  # this value at the release version of the first install of this system.
  # Before changing this value read the documentation for this option
  # (e.g. man configuration.nix or on https://nixos.org/nixos/options.html).
  system.stateVersion = "25.11";  # Did you read the comment?
}

# ═══════════════════════════════════════════════════════════════════
# 🌾 Configuration Complete!
# ═══════════════════════════════════════════════════════════════════
#
# After editing, rebuild with:
#   sudo nixos-rebuild switch
#
# Access Grain Network:
#   cd /mnt/grainkae3g
#   bb qb-kk
#
# Start services:
#   sudo systemctl start grain6
#   sudo systemctl start grainwifi
#
# Launch HumbleUI:
#   cd /mnt/grainkae3g/grainstore/grainwifi
#   bb humble-ui:start
#
# now == next + 1 🌾
# ═══════════════════════════════════════════════════════════════════
