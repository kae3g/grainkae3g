#!/usr/bin/env bb
;; sixos-build-iso.bb - Build SixOS installer ISO
;;
;; This script builds a custom NixOS ISO with s6 supervision instead of systemd

(ns sixos-build-iso
  (:require [babashka.process :refer [shell]]
            [babashka.fs :as fs]
            [clojure.string :as str]))

(defn check-nix []
  "Check if Nix is available"
  (try
    (shell {:out :string} "which" "nix")
    true
    (catch Exception _
      (println "❌ Error: Nix not found. Please install Nix first:")
      (println "   curl -L https://nixos.org/nix/install | sh")
      false)))

(defn build-iso []
  "Build the SixOS installer ISO"
  (println "🔧 Building SixOS Installer ISO...")
  (println "   (NixOS without systemd, s6 supervision)\n")
  
  (when-not (check-nix)
    (System/exit 1))
  
  (println "📦 This will:")
  (println "   1. Build a minimal NixOS ISO")
  (println "   2. Replace systemd with s6")
  (println "   3. Include Framework 16 drivers")
  (println "   4. Add installation tools\n")
  
  (println "⏳ Building... (this may take 10-30 minutes)")
  (println "   Output: result-iso/nixos-*.iso\n")
  
  (try
    ;; Build ISO using Nix (placeholder - will need actual flake)
    (shell "nix" "build" 
           ".#nixosConfigurations.sixos-installer.config.system.build.isoImage"
           "-o" "result-iso")
    
    ;; Find the ISO
    (let [iso-files (fs/glob "result-iso" "*.iso")
          iso-path (first iso-files)]
      (if iso-path
        (do
          (println "\n✅ ISO built successfully!")
          (println (str "   Location: " iso-path))
          (let [size (quot (.length (fs/file iso-path)) (* 1024 1024))]
            (println (str "   Size: " size " MB")))
          (println "\n📝 Next steps:")
          (println "   1. Flash to USB: bb sixos:flash-usb <device>")
          (println "   2. Or list drives: bb sixos:list-drives")
          (println "   3. Boot from USB on Framework 16")
          (println "   4. Follow installation guide"))
        (do
          (println "\n⚠️  Warning: ISO file not found in result-iso/")
          (println "   The build may have succeeded but ISO location differs")
          (println "   Check result-iso/ directory manually"))))
    
    (catch Exception e
      (println "\n❌ Build failed!")
      (println (str "   Error: " (.getMessage e)))
      (println "\n💡 Troubleshooting:")
      (println "   1. Ensure flake.nix has sixos-installer configuration")
      (println "   2. Check Nix version: nix --version")
      (println "   3. Try: nix flake check")
      (System/exit 1))))

;; Run if called directly
(when (= *file* (System/getProperty "babashka.file"))
  (build-iso))

