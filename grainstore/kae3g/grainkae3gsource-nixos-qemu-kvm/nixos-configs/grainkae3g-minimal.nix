# 🌾 GrainKae3g Minimal NixOS Configuration
# Minimal bootstrap configuration for VM development
#
# Usage:
#   sudo nixos-rebuild switch --flake .#grainkae3g-minimal

{ config, pkgs, lib, ... }:

{
  # ═══════════════════════════════════════════════════════════
  # SYSTEM CONFIGURATION
  # ═══════════════════════════════════════════════════════════
  
  imports = [
    # Include hardware configuration
    ./hardware-configuration.nix
  ];

  # ═══════════════════════════════════════════════════════════
  # NETWORKING
  # ═══════════════════════════════════════════════════════════
  
  networking = {
    hostName = "grainkae3g-minimal";
    networkmanager.enable = true;
    firewall = {
      enable = true;
      # Allow SSH
      allowedTCPPorts = [ 22 ];
      # Allow Mosh UDP ports
      allowedUDPPorts = [ 60000 60001 60002 60003 60004 60005 ];
    };
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
  # SHARED FOLDER (9p virtfs from Ubuntu host)
  # ═══════════════════════════════════════════════════════════
  
  fileSystems."/mnt/grainkae3g" = {
    device = "grainkae3g";  # Mount tag from virt-manager
    fsType = "9p";
    options = [
      "trans=virtio"
      "version=9p2000.L"
      "cache=loose"
      "msize=1048576"
    ];
  };

  # ═══════════════════════════════════════════════════════════
  # BOOT CONFIGURATION
  # ═══════════════════════════════════════════════════════════
  
  boot = {
    loader = {
      grub = {
        enable = false; # Disable GRUB for VM
        device = "nodev";
        useOSProber = false;
      };
      systemd-boot = {
        enable = false; # Disable systemd-boot for VM
        editor = false;
      };
    };
    # Enable kernel modules for virtualization
    kernelModules = [ "kvm-intel" "virtio" "9p" "9pnet" "9pnet_virtio" ];
  };

  # ═══════════════════════════════════════════════════════════
  # ESSENTIAL PACKAGES ONLY
  # ═══════════════════════════════════════════════════════════
  
  environment.systemPackages = with pkgs; [
    # ────────────────────────────────────────────────────────
    # Essential Development Tools
    # ────────────────────────────────────────────────────────
    git curl wget
    vim nano
    htop tree
    
    # ────────────────────────────────────────────────────────
    # Terminal Multiplexers (minimal)
    # ────────────────────────────────────────────────────────
    tmux zellij
    
    # ────────────────────────────────────────────────────────
    # Grain Network - Clojure Ecosystem (minimal)
    # ────────────────────────────────────────────────────────
    babashka        # Fast Clojure scripting (grain6, grainwifi)
    clojure         # Clojure CLI
    jdk17           # Java for Clojure
    
    # ────────────────────────────────────────────────────────
    # Network Tools
    # ────────────────────────────────────────────────────────
    mosh            # Mobile shell for persistent sessions
    openssh         # SSH client/server
  ];

  # ═══════════════════════════════════════════════════════════
  # SERVICES (Minimal)
  # ═══════════════════════════════════════════════════════════
  
  services = {
    # SSH server
    openssh = {
      enable = true;
      settings = {
        PasswordAuthentication = false;
        KbdInteractiveAuthentication = false;
      };
    };
  };

  # ═══════════════════════════════════════════════════════════
  # USERS
  # ═══════════════════════════════════════════════════════════
  
  users = {
    mutableUsers = false;
    users = {
      nixos = {
        isNormalUser = true;
        extraGroups = [ "wheel" "networkmanager" ];
        openssh.authorizedKeys.keys = [
          # Add your SSH public key here
          "ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAI... your-key-here"
        ];
      };
    };
  };

  # ═══════════════════════════════════════════════════════════
  # SUDO CONFIGURATION
  # ═══════════════════════════════════════════════════════════
  
  security.sudo = {
    enable = true;
    wheelNeedsPassword = false;
  };

  # ═══════════════════════════════════════════════════════════
  # SYSTEM CONFIGURATION
  # ═══════════════════════════════════════════════════════════
  
  system = {
    stateVersion = "25.11";
    autoUpgrade = {
      enable = false;  # Disable auto-upgrade for VM
    };
  };

  # ═══════════════════════════════════════════════════════════
  # NIX CONFIGURATION
  # ═══════════════════════════════════════════════════════════
  
  nix = {
    settings = {
      auto-optimise-store = true;
      experimental-features = [ "nix-command" "flakes" ];
    };
    gc = {
      automatic = true;
      dates = "weekly";
      options = "--delete-older-than 7d";
    };
  };

  # ═══════════════════════════════════════════════════════════
  # LOCALE AND TIMEZONE
  # ═══════════════════════════════════════════════════════════
  
  time.timeZone = "America/Los_Angeles";
  i18n.defaultLocale = "en_US.UTF-8";

  # ═══════════════════════════════════════════════════════════
  # GRAIN6 DAEMON (Minimal)
  # ═══════════════════════════════════════════════════════════
  
  systemd.services.grain6 = {
    description = "grain6 - Time-aware Service Supervisor (Minimal)";
    after = [ "network.target" ];
    wantedBy = [ "multi-user.target" ];
    
    serviceConfig = {
      Type = "simple";
      User = "nixos";
      WorkingDirectory = "/mnt/grainkae3g/grainstore/grain6";
      ExecStart = "${pkgs.babashka}/bin/bb grain6:daemon";
      Restart = "always";
      RestartSec = 10;
    };
    
    environment = {
      HOME = "/home/nixos";
      PATH = lib.mkForce (lib.makeBinPath (with pkgs; [ babashka clojure jdk17 ]));
    };
  };

  # ═══════════════════════════════════════════════════════════
  # GRAINPATH ENVIRONMENT
  # ═══════════════════════════════════════════════════════════
  
  environment.variables = {
    GRAINPATH = "/mnt/grainkae3g";
  };
}
