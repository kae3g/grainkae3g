#!/usr/bin/env bb
;; sixos-vm-manager.bb - SixOS Virtual Machine Manager
;;
;; Specialized VM management for SixOS (NixOS without systemd)
;; Integrates with existing Babashka tooling and Grainstore

(ns sixos-vm-manager
  (:require [babashka.process :refer [shell]]
            [babashka.fs :as fs]
            [clojure.string :as str]
            [clojure.edn :as edn]))

;; Configuration
(def config
  {:vm-dir "/home/xy/kae3g/12025-10/vms"
   :iso-dir "/home/xy/kae3g/12025-10/isos"
   :log-dir "/home/xy/kae3g/12025-10/logs"
   :backup-dir "/home/xy/kae3g/12025-10/backups"
   :sixos-repo "https://codeberg.org/amjoseph/sixos.git"
   :default-memory 4096  ; 4GB for SixOS development
   :default-cores 4      ; 4 CPU cores
   :default-disk 20})    ; 20GB disk

(defn log [message]
  (let [timestamp (str (java.time.Instant/now))
        log-file (str (:log-dir config) "/sixos-vm-manager.log")]
    (println (str "[" timestamp "] " message))
    (spit log-file (str "[" timestamp "] " message "\n") :append true)))

(defn error [message]
  (log (str "ERROR: " message))
  (System/exit 1))

(defn check-command [cmd]
  "Check if a command exists in PATH"
  (try
    (shell {:out :string} "which" cmd)
    true
    (catch Exception _
      false)))

(defn check-prerequisites []
  "Check if all required tools are available"
  (log "Checking prerequisites...")
  
  (when-not (check-command "nix")
    (error "Nix not found. Please install Nix first: curl -L https://nixos.org/nix/install | sh"))
  
  (when-not (check-command "virsh")
    (error "libvirt not found. Please install QEMU/KVM first"))
  
  (when-not (check-command "qemu-system-x86_64")
    (error "QEMU not found. Please install QEMU/KVM first"))
  
  (log "✅ All prerequisites found"))

(defn clone-sixos-repo []
  "Clone SixOS repository if not already present"
  (let [sixos-dir (str (:vm-dir config) "/sixos")]
    (if (fs/exists? sixos-dir)
      (do
        (log "SixOS repository already exists, updating...")
        (shell {:dir sixos-dir} "git" "pull"))
      (do
        (log "Cloning SixOS repository...")
        (shell "git" "clone" (:sixos-repo config) sixos-dir)))
    sixos-dir))

(defn build-sixos-vm []
  "Build SixOS VM using Nix"
  (log "Building SixOS VM...")
  (let [sixos-dir (clone-sixos-repo)
        build-cmd ["nix" "build" 
                   (str sixos-dir "/.#nixosConfigurations.minimal-sixos.config.system.build.vm")
                   "-o" (str (:vm-dir config) "/sixos-vm")]]
    (try
      (apply shell (concat ["nix" "--extra-experimental-features" "nix-command"] (rest build-cmd)))
      (log "✅ SixOS VM built successfully")
      (str (:vm-dir config) "/sixos-vm")
      (catch Exception e
        (error (str "Failed to build SixOS VM: " (.getMessage e)))))))

(defn create-sixos-vm [vm-name & {:keys [memory cores disk] :or {memory (:default-memory config) 
                                                                  cores (:default-cores config)
                                                                  disk (:default-disk config)}}]
  "Create a new SixOS VM with specified parameters"
  (log (str "Creating SixOS VM: " vm-name))
  (log (str "Memory: " memory "MB, Cores: " cores ", Disk: " disk "GB"))
  
  (let [vm-dir (str (:vm-dir config) "/" vm-name)
        disk-path (str vm-dir "/" vm-name ".qcow2")]
    
    ;; Create VM directory
    (fs/create-dirs vm-dir)
    
    ;; Create disk image
    (log "Creating disk image...")
    (shell "qemu-img" "create" "-f" "qcow2" disk-path (str disk "G"))
    
    ;; Create VM using virt-install
    (let [install-cmd ["virt-install"
                       "--name" vm-name
                       "--memory" (str memory)
                       "--vcpus" (str cores)
                       "--disk" (str "path=" disk-path ",format=qcow2")
                       "--network" "network=default"
                       "--graphics" "vnc,listen=0.0.0.0"
                       "--noautoconsole"
                       "--import"
                       "--os-variant" "nixos"
                       "--boot" "uefi"]]
      (try
        (apply shell install-cmd)
        (log (str "✅ SixOS VM '" vm-name "' created successfully"))
        (catch Exception e
          (error (str "Failed to create VM: " (.getMessage e))))))))

(defn start-sixos-vm [vm-name]
  "Start a SixOS VM"
  (log (str "Starting SixOS VM: " vm-name))
  (try
    (shell "virsh" "start" vm-name)
    (log (str "✅ SixOS VM '" vm-name "' started"))
    (catch Exception e
      (error (str "Failed to start VM: " (.getMessage e))))))

(defn stop-sixos-vm [vm-name]
  "Stop a SixOS VM"
  (log (str "Stopping SixOS VM: " vm-name))
  (try
    (shell "virsh" "shutdown" vm-name)
    (log (str "✅ SixOS VM '" vm-name "' shutdown initiated"))
    (catch Exception e
      (error (str "Failed to stop VM: " (.getMessage e))))))

(defn list-sixos-vms []
  "List all SixOS VMs"
  (log "Listing SixOS VMs...")
  (try
    (let [result (shell {:out :string} "virsh" "list" "--all")]
      (println result)
      (log "VM list retrieved successfully"))
    (catch Exception e
      (error (str "Failed to list VMs: " (.getMessage e))))))

(defn connect-sixos-vm [vm-name]
  "Connect to SixOS VM via VNC or virt-manager"
  (log (str "Connecting to SixOS VM: " vm-name))
  (try
    ;; Try virt-manager first
    (shell "virt-manager" "--connect" "qemu:///system" "--show-domain-console" vm-name)
    (catch Exception e
      ;; Fallback to VNC
      (log "virt-manager not available, trying VNC...")
      (let [vnc-info (shell {:out :string} "virsh" "vncdisplay" vm-name)]
        (log (str "VNC display: " vnc-info))
        (log "Use a VNC client to connect to the display")))))

(defn build-sixos-iso []
  "Build SixOS installer ISO"
  (log "Building SixOS installer ISO...")
  (let [sixos-dir (clone-sixos-repo)
        iso-dir (:iso-dir config)]
    (fs/create-dirs iso-dir)
    
    (try
      (shell {:dir sixos-dir} "nix" "--extra-experimental-features" "nix-command" "build" 
             ".#nixosConfigurations.minimal-sixos.config.system.build.isoImage"
             "-o" (str iso-dir "/sixos-installer"))
      (log "✅ SixOS ISO built successfully")
      (let [iso-path (first (fs/glob (str iso-dir "/sixos-installer") "*.iso"))]
        (log (str "ISO location: " iso-path))
        iso-path)
      (catch Exception e
        (error (str "Failed to build SixOS ISO: " (.getMessage e)))))))

(defn run-sixos-in-qemu [iso-path & {:keys [memory cores] :or {memory 4096 cores 4}}]
  "Run SixOS directly in QEMU (for testing)"
  (log "Running SixOS in QEMU...")
  (let [qemu-cmd ["qemu-system-x86_64"
                  "-enable-kvm"
                  "-m" (str memory)
                  "-smp" (str cores)
                  "-cdrom" iso-path
                  "-boot" "d"
                  "-netdev" "user,id=net0"
                  "-device" "virtio-net-pci,netdev=net0"
                  "-display" "gtk"]]
    (try
      (apply shell qemu-cmd)
      (catch Exception e
        (log (str "QEMU exited: " (.getMessage e)))))))

(defn show-help []
  "Show help information"
  (println "SixOS Virtual Machine Manager")
  (println "")
  (println "Usage: bb sixos-vm-manager <command> [options]")
  (println "")
  (println "Commands:")
  (println "  build-vm                    Build SixOS VM using Nix")
  (println "  create <name> [opts]        Create new SixOS VM")
  (println "  start <name>                Start SixOS VM")
  (println "  stop <name>                 Stop SixOS VM")
  (println "  list                        List all VMs")
  (println "  connect <name>              Connect to VM (virt-manager/VNC)")
  (println "  build-iso                   Build SixOS installer ISO")
  (println "  run-qemu <iso>              Run SixOS in QEMU directly")
  (println "  help                        Show this help")
  (println "")
  (println "Options for create:")
  (println "  :memory <MB>                Memory in MB (default: 4096)")
  (println "  :cores <num>                CPU cores (default: 4)")
  (println "  :disk <GB>                  Disk size in GB (default: 20)")
  (println "")
  (println "Examples:")
  (println "  bb sixos-vm-manager build-vm")
  (println "  bb sixos-vm-manager create sixos-dev :memory 8192 :cores 8")
  (println "  bb sixos-vm-manager start sixos-dev")
  (println "  bb sixos-vm-manager build-iso")
  (println "  bb sixos-vm-manager run-qemu /path/to/sixos.iso"))

(defn main [& args]
  "Main function"
  (fs/create-dirs (:log-dir config))
  (fs/create-dirs (:vm-dir config))
  (fs/create-dirs (:iso-dir config))
  (fs/create-dirs (:backup-dir config))
  
  (check-prerequisites)
  
  (case (first args)
    "build-vm" (build-sixos-vm)
    "create" (let [[vm-name & opts] (rest args)
                   opts-map (apply hash-map opts)]
               (create-sixos-vm vm-name opts-map))
    "start" (start-sixos-vm (second args))
    "stop" (stop-sixos-vm (second args))
    "list" (list-sixos-vms)
    "connect" (connect-sixos-vm (second args))
    "build-iso" (build-sixos-iso)
    "run-qemu" (run-sixos-in-qemu (second args))
    "help" (show-help)
    (show-help)))

;; Run if called directly
(when (= *file* (System/getProperty "babashka.file"))
  (apply main *command-line-args*))
