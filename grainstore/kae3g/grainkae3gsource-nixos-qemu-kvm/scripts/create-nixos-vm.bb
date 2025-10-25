#!/usr/bin/env bb

(require '[babashka.process :refer [shell process]]
         '[babashka.fs :as fs]
         '[clojure.string :as str])

;;; ╔═══════════════════════════════════════════════════════════════════╗
;;; ║                                                                   ║
;;; ║   🌾  C R E A T E   N I X O S   V M   A U T O M A T I O N        ║
;;; ║                                                                   ║
;;; ║   NixOS 25.11 Unstable in QEMU/KVM                               ║
;;; ║   Framework 16 Ubuntu 24.04 LTS Host                             ║
;;; ║                                                                   ║
;;; ║   Following Ubuntu Official Documentation:                       ║
;;; ║   https://ubuntu.com/server/docs/virtualization-qemu             ║
;;; ║                                                                   ║
;;; ╚═══════════════════════════════════════════════════════════════════╝

;; ═══════════════════════════════════════════════════════════════════
;; CONFIGURATION
;; ═══════════════════════════════════════════════════════════════════

(def config
  {:vm-name "nixos-grainkae3g"
   :ram-mb 16384                ; 16 GB
   :cpus 8
   :disk-size-gb 100
   :iso-url "https://channels.nixos.org/nixos-unstable/latest-nixos-minimal-x86_64-linux.iso"
   :iso-dir (str (System/getProperty "user.home") "/VMs/ISOs")
   :vm-dir "/var/lib/libvirt/images"
   :shared-folder (str (System/getProperty "user.home") "/kae3g/grainkae3g")
   :network "default"           ; NAT network
   :graphics "spice"})

;; ═══════════════════════════════════════════════════════════════════
;; HELPER FUNCTIONS
;; ═══════════════════════════════════════════════════════════════════

(defn print-header [text]
  (println)
  (println "╔════════════════════════════════════════════════════════════╗")
  (println (str "║  " text))
  (println "╚════════════════════════════════════════════════════════════╝")
  (println))

(defn print-step [step-num text]
  (println (str "\n📍 Step " step-num ": " text "\n")))

(defn check-command [cmd]
  (try
    (shell {:out :string :err :string} (str "which " cmd))
    true
    (catch Exception e
      false)))

(defn check-service [service]
  (try
    (let [result (shell {:out :string :continue true}
                        (str "systemctl is-active " service))]
      (= 0 (:exit result)))
    (catch Exception e
      false)))

;; ═══════════════════════════════════════════════════════════════════
;; PREREQUISITE CHECKS
;; ═══════════════════════════════════════════════════════════════════

(defn check-prerequisites []
  (print-step 1 "Checking prerequisites")
  
  (let [required-commands ["virsh" "virt-install" "qemu-system-x86_64" "wget"]
        missing (filter #(not (check-command %)) required-commands)]
    
    (if (seq missing)
      (do
        (println "❌ Missing required commands:" (str/join ", " missing))
        (println "\nInstall with:")
        (println "  sudo apt install -y qemu-kvm libvirt-daemon-system libvirt-clients virt-manager")
        (System/exit 1))
      (println "✅ All required commands found")))
  
  ;; Check libvirtd service
  (if (check-service "libvirtd")
    (println "✅ libvirtd service is running")
    (do
      (println "❌ libvirtd service not running")
      (println "Start with: sudo systemctl start libvirtd")
      (System/exit 1)))
  
  ;; Check KVM support
  (try
    (let [result (shell {:out :string} "kvm-ok")]
      (println "✅ KVM acceleration available"))
    (catch Exception e
      (println "⚠️  KVM acceleration check failed - continuing anyway")))
  
  ;; Check user groups
  (let [groups (shell {:out :string} "groups")
        has-libvirt (str/includes? groups "libvirt")
        has-kvm (str/includes? groups "kvm")]
    (when (not (and has-libvirt has-kvm))
      (println "⚠️  User not in libvirt/kvm groups")
      (println "Add with:")
      (println "  sudo usermod -aG libvirt,kvm $USER")
      (println "  (then log out and back in)")
      (println))))

;; ═══════════════════════════════════════════════════════════════════
;; DOWNLOAD NIXOS ISO
;; ═══════════════════════════════════════════════════════════════════

(defn download-iso []
  (print-step 2 "Downloading NixOS 25.11 Unstable ISO")
  
  (let [iso-dir (:iso-dir config)
        iso-file (str iso-dir "/nixos-25.11-unstable-minimal.iso")
        iso-url (:iso-url config)
        expected-size-mb 1500]  ; ~1.5GB expected size
    
    ;; Create ISO directory
    (fs/create-dirs iso-dir)
    
    ;; Check if ISO already exists and is complete
    (if (fs/exists? iso-file)
      (let [actual-size-mb (/ (fs/size iso-file) (* 1024 1024))]
        (if (>= actual-size-mb expected-size-mb)
          (do
            (println "✅ Complete ISO already downloaded:" iso-file)
            (println (str "   Size: " (format "%.1f" (double actual-size-mb)) " MB"))
            iso-file)
          (do
            (println "⚠️  Incomplete ISO found:" iso-file)
            (println (str "   Current size: " (format "%.1f" (double actual-size-mb)) " MB"))
            (println (str "   Expected size: " expected-size-mb " MB"))
            (println "🗑️  Removing incomplete download...")
            (fs/delete iso-file)
            (println "📥 Downloading fresh ISO from:" iso-url)
            (println "   This may take a while...")
            (shell (str "wget -O " iso-file " " iso-url))
            (println "✅ Download complete:" iso-file)
            iso-file)))
      (do
        (println "📥 Downloading ISO from:" iso-url)
        (println "   This may take a while...")
        (shell (str "wget -O " iso-file " " iso-url))
        (println "✅ Download complete:" iso-file)
        iso-file))))

;; ═══════════════════════════════════════════════════════════════════
;; CREATE VM
;; ═══════════════════════════════════════════════════════════════════

(defn create-vm [iso-file]
  (print-step 3 "Creating NixOS VM with virt-install")
  
  (let [vm-name (:vm-name config)
        disk-path (str (:vm-dir config) "/" vm-name ".qcow2")]
    
    ;; Check if VM already exists
    (let [existing-vms (shell {:out :string} "virsh list --all --name")]
      (when (str/includes? existing-vms vm-name)
        (println "⚠️  VM" vm-name "already exists")
        (print "❓ Destroy existing VM? [y/N]: ")
        (flush)
        (let [response (read-line)]
          (if (= (str/lower-case response) "y")
            (do
              (println "🗑️  Destroying existing VM...")
              (shell {:continue true} (str "virsh destroy " vm-name))
              (shell {:continue true} (str "virsh undefine " vm-name " --remove-all-storage")))
            (do
              (println "❌ Cancelled")
              (System/exit 0))))))
    
    ;; Create VM with virt-install
    (println "🚀 Creating VM:" vm-name)
    (println "   RAM:" (:ram-mb config) "MB")
    (println "   CPUs:" (:cpus config))
    (println "   Disk:" (:disk-size-gb config) "GB")
    (println "   ISO:" iso-file)
    (println "   Shared folder:" (:shared-folder config))
    (println)
    
    (let [virt-install-cmd
          (str "sudo virt-install "
               "--name " vm-name " "
               "--ram " (:ram-mb config) " "
               "--vcpus " (:cpus config) " "
               "--disk path=" disk-path ",size=" (:disk-size-gb config) " "
               "--cdrom " iso-file " "
               "--os-variant nixos-unstable "
               "--network network=" (:network config) " "
               "--graphics " (:graphics config) " "
               "--boot uefi "
               "--filesystem source=" (:shared-folder config) ",target=grainkae3g,mode=mapped "
               "--noautoconsole")]
      
      (println "Running command:")
      (println virt-install-cmd)
      (println)
      
      (shell virt-install-cmd)
      (println "✅ VM created successfully!")
      (println)
      (println "📺 Open virt-manager to access console:")
      (println "   virt-manager")
      (println)
      (println "🔗 Or connect via:")
      (println "   virsh console" vm-name))))

;; ═══════════════════════════════════════════════════════════════════
;; MAIN
;; ═══════════════════════════════════════════════════════════════════

(defn main []
  (print-header "🌾 NixOS 25.11 VM Creation for Grain Network")
  
  (check-prerequisites)
  
  (let [iso-file (download-iso)]
    (create-vm iso-file))
  
  (println)
  (println "╔════════════════════════════════════════════════════════════╗")
  (println "║  ✅ VM Creation Complete!                                  ║")
  (println "╚════════════════════════════════════════════════════════════╝")
  (println)
  (println "Next steps:")
  (println "1. Open virt-manager and access the VM console")
  (println "2. Complete NixOS installation (follow README.md)")
  (println "3. Configure /etc/nixos/configuration.nix with shared folder")
  (println "4. Access Grain Network from /mnt/grainkae3g")
  (println)
  (println "🌾 now == next + 1"))

(main)
