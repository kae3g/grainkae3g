{ config, pkgs, inputs, ... }:

{
  imports = [
    ./hardware-configuration.nix
    ./sway.nix
  ];

  # Boot configuration
  boot.loader = {
    systemd-boot.enable = true;
    efi.canMountIfNeeded = true;
  };

  # Hostname
  networking.hostName = "nixos-qemu-fw16";
  
  # Enable networking
  networking.networkmanager.enable = true;

  # Time zone
  time.timeZone = "America/Chicago";

  # Locale
  i18n.defaultLocale = "en_US.UTF-8";
  
  i18n.extraLocaleSettings = {
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

  # Enable nested virtualization
  boot.kernelModules = [ "kvm-amd" "kvm-intel" ];
  
  virtualisation = {
    libvirtd = {
      enable = true;
      qemu = {
        package = pkgs.qemu_kvm;
        ovmf.enable = true;
      };
    };
  };

  # Users
  users.users.nixos = {
    isNormalUser = true;
    description = "NixOS User";
    extraGroups = [ "wheel" "networkmanager" "libvirtd" "kvm" "video" "audio" ];
    password = "graintest";  # Change this!
    shell = pkgs.bash;
  };

  # Allow unfree packages (if needed)
  nixpkgs.config.allowUnfree = true;

  # Enable flakes
  nix = {
    package = pkgs.nixFlakes;
    settings = {
      experimental-features = [ "nix-command" "flakes" ];
      auto-optimise-store = true;
    };
    
    # Automatic garbage collection
    gc = {
      automatic = true;
      dates = "weekly";
      options = "--delete-older-than 30d";
    };
  };

  # System packages
  environment.systemPackages = with pkgs; [
    # Core utilities
    vim
    git
    wget
    curl
    htop
    tree
    ripgrep
    fd
    bat
    fzf
    
    # Virtualization tools
    qemu_kvm
    qemu-utils
    virt-manager
    libvirt
    bridge-utils
    
    # Development
    gcc
    gnumake
    cmake
    pkg-config
    
    # Clojure ecosystem
    clojure
    leiningen
    jdk17
    
    # Babashka (from unstable if needed)
    # babashka
    
    # Network tools
    nmap
    tcpdump
    wireshark
    
    # Grain Network tools
    # (Add as they become available in nixpkgs)
  ];

  # Enable SSH
  services.openssh = {
    enable = true;
    settings = {
      PermitRootLogin = "no";
      PasswordAuthentication = true;
    };
  };

  # Enable sound with pipewire
  sound.enable = true;
  hardware.pulseaudio.enable = false;
  security.rtkit.enable = true;
  services.pipewire = {
    enable = true;
    alsa.enable = true;
    alsa.support32Bit = true;
    pulse.enable = true;
  };

  # Security
  security.sudo.wheelNeedsPassword = false;  # For development; change for production

  # Firewall
  networking.firewall = {
    enable = true;
    allowedTCPPorts = [ 22 ];  # SSH
  };

  # System state version
  # Don't change this unless you know what you're doing
  system.stateVersion = "23.11";
}


