#!/usr/bin/env bb
;; sixos-flash-usb.bb - Flash SixOS ISO to USB drive
;;
;; Usage: bb sixos:flash-usb /dev/disk4  (macOS)
;;        bb sixos:flash-usb /dev/sdb    (Linux)

(ns sixos-flash-usb
  (:require [babashka.process :refer [shell]]
            [babashka.fs :as fs]
            [clojure.string :as str]))

(def os-type
  "Detect operating system"
  (let [os-name (System/getProperty "os.name")]
    (cond
      (str/includes? os-name "Mac") :macos
      (str/includes? os-name "Linux") :linux
      :else :unknown)))

(defn find-iso []
  "Find the built ISO file"
  (let [iso-files (fs/glob "result-iso" "*.iso")]
    (first iso-files)))

(defn list-drives []
  "List available drives for the current OS"
  (println "📀 Available drives:\n")
  (case os-type
    :macos (shell "diskutil" "list")
    :linux (shell "lsblk")
    (println "❌ Unsupported OS")))

(defn confirm-flash [device iso-path]
  "Ask user to confirm before flashing"
  (println "\n⚠️  WARNING: This will ERASE all data on the drive!")
  (println (str "   Device: " device))
  (println (str "   ISO: " iso-path))
  (print "\nType 'yes' to continue: ")
  (flush)
  (= "yes" (str/trim (read-line))))

(defn unmount-drive [device]
  "Unmount the target drive"
  (println "\n📤 Unmounting drive...")
  (try
    (case os-type
      :macos (shell "diskutil" "unmountDisk" device)
      :linux (shell "umount" (str device "*"))
      (println "❌ Unsupported OS"))
    true
    (catch Exception e
      (println "⚠️  Warning: Could not unmount (may not be mounted)")
      true)))

(defn flash-iso [device iso-path]
  "Flash ISO to USB drive"
  (println "\n🔥 Flashing ISO to USB drive...")
  (println "   This may take 5-15 minutes...")
  (println "   (Do not remove drive during this process!)\n")
  
  (let [;; Use rdisk on macOS for faster writes
        target-device (if (and (= os-type :macos)
                              (str/starts-with? device "/dev/disk"))
                        (str/replace device "/dev/disk" "/dev/rdisk")
                        device)
        bs (if (= os-type :macos) "4m" "4M")]
    
    (println (str "   Writing to: " target-device))
    
    (try
      (shell "sudo" "dd"
             (str "if=" iso-path)
             (str "of=" target-device)
             (str "bs=" bs)
             "status=progress")
      
      (println "\n🔄 Syncing...")
      (shell "sync")
      
      (when (= os-type :macos)
        (println "\n📤 Ejecting drive...")
        (shell "diskutil" "eject" device))
      
      (println "\n✅ Flash complete!")
      (println "   You can now:")
      (println "   1. Remove USB drive safely")
      (println "   2. Insert into Framework 16")
      (println "   3. Boot from USB (F12 or F2 for boot menu)")
      (println "   4. Follow SixOS installation guide")
      
      (catch Exception e
        (println "\n❌ Flash failed!")
        (println (str "   Error: " (.getMessage e)))
        (println "\n💡 Common issues:")
        (println "   - Need sudo password")
        (println "   - Wrong device path")
        (println "   - Drive not unmounted")
        (println "   - ISO file corrupted")
        (System/exit 1)))))

(defn main [args]
  (if (empty? args)
    (do
      (println "Usage: bb sixos:flash-usb <device>")
      (println "\nExamples:")
      (println "  macOS:  bb sixos:flash-usb /dev/disk4")
      (println "  Linux:  bb sixos:flash-usb /dev/sdb")
      (println "\nTo list drives:")
      (println "  bb sixos:list-drives")
      (System/exit 1))
    
    (let [device (first args)
          iso-path (find-iso)]
      
      (when-not iso-path
        (println "❌ Error: ISO file not found!")
        (println "   Run: bb sixos:build-iso")
        (System/exit 1))
      
      (when-not (confirm-flash device iso-path)
        (println "\n❌ Flash cancelled by user")
        (System/exit 0))
      
      (unmount-drive device)
      (flash-iso device iso-path))))

;; Run if called directly
(when (= *file* (System/getProperty "babashka.file"))
  (main *command-line-args*))

