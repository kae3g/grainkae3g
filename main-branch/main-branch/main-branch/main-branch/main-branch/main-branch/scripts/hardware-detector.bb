#!/usr/bin/env bb
;; hardware-detector.bb - Auto-detect hardware specifications
;;
;; Generates hardware configuration for QEMU/KVM SixOS environment

(ns hardware-detector
  (:require [babashka.process :refer [shell]]
            [clojure.string :as str]
            [clojure.edn :as edn]))

(defn run-command [cmd]
  "Run a command and return output as string"
  (try
    (-> (shell {:out :string} cmd)
        :out
        str/trim)
    (catch Exception e
      (println (str "Error running command: " cmd " - " (.getMessage e)))
      "")))

(defn get-cpu-info []
  "Extract CPU information from lscpu"
  (let [lscpu-output (run-command "lscpu")
        lines (str/split-lines lscpu-output)
        cpu-info (reduce (fn [acc line]
                           (let [[key val] (str/split line #":" 2)]
                             (if (and key val)
                               (assoc acc (str/trim key) (str/trim val))
                               acc)))
                         {}
                         lines)]
    {:cores (Integer/parseInt (get cpu-info "CPU(s)" "0"))
     :threads (Integer/parseInt (get cpu-info "Thread(s) per core" "1"))
     :vendor (get cpu-info "Vendor ID" "Unknown")
     :model (get cpu-info "Model name" "Unknown")
     :architecture (get cpu-info "Architecture" "Unknown")
     :virtualization (get cpu-info "Virtualization" "Unknown")}))

(defn get-memory-info []
  "Extract memory information from free and dmidecode"
  (let [free-output (run-command "free -h")
        memory-lines (filter #(str/starts-with? % "Mem:") (str/split-lines free-output))
        total-mem (when (seq memory-lines)
                    (-> (first memory-lines)
                        (str/split #"\s+")
                        (nth 1)))
        dmidecode-output (run-command "sudo dmidecode -t memory 2>/dev/null")
        memory-lines (filter #(str/includes? % "Size:") (str/split-lines dmidecode-output))
        memory-size (when (seq memory-lines)
                      (-> (first memory-lines)
                          (str/replace "Size:" "")
                          str/trim))
        memory-type (let [type-lines (filter #(str/includes? % "Type:") (str/split-lines dmidecode-output))]
                      (when (seq type-lines)
                        (-> (first type-lines)
                            (str/replace "Type:" "")
                            str/trim)))
        speed-lines (filter #(str/includes? % "Speed:") (str/split-lines dmidecode-output))
        memory-speed (when (seq speed-lines)
                       (-> (first speed-lines)
                           (str/replace "Speed:" "")
                           str/trim))]
    {:total-gb (or total-mem "Unknown")
     :type (or memory-type "Unknown")
     :speed (or memory-speed "Unknown")}))

(defn get-storage-info []
  "Extract storage information from lsblk"
  (let [lsblk-output (run-command "lsblk -d -o NAME,SIZE,TYPE")
        lines (str/split-lines lsblk-output)
        storage-devices (filter #(str/includes? % "disk") lines)
        devices (map (fn [line]
                       (let [parts (str/split line #"\s+")]
                         {:name (first parts)
                          :size (second parts)
                          :type "NVMe"}))
                     storage-devices)]
    {:devices devices}))

(defn get-gpu-info []
  "Extract GPU information from lspci"
  (let [lspci-output (run-command "lspci | grep -E '(VGA|Display|3D)'")
        lines (str/split-lines lspci-output)
        gpus (map (fn [line]
                    (let [parts (str/split line #": " 2)
                          device-id (first parts)
                          description (second parts)]
                      {:device-id device-id
                       :description description}))
                  lines)]
    {:gpus gpus}))

(defn get-display-info []
  "Extract display information from xrandr"
  (let [xrandr-output (run-command "xrandr | grep -E '(Screen|connected|current)'")
        lines (str/split-lines xrandr-output)
        screen-line (first (filter #(str/starts-with? % "Screen") lines))
        connected-line (first (filter #(str/includes? % "connected") lines))
        resolution (when connected-line
                     (let [parts (str/split connected-line #" ")
                           res-part (first (filter #(str/includes? % "x") parts))]
                       res-part))]
    {:resolution (or resolution "Unknown")
     :connected-displays (count (filter #(str/includes? % "connected") lines))}))

(defn get-network-info []
  "Extract network information from lspci and lsusb"
  (let [lspci-output (run-command "lspci | grep -E '(Network|Ethernet|Wireless)'")
        lsusb-output (run-command "lsusb | grep -E '(Wireless|Network|Ethernet)'")
        wireless-pci (filter #(str/includes? % "Wireless") (str/split-lines lspci-output))
        wireless-usb (filter #(str/includes? % "Wireless") (str/split-lines lsusb-output))]
    {:wireless-pci (map str/trim wireless-pci)
     :wireless-usb (map str/trim wireless-usb)}))

(defn get-system-info []
  "Extract system information from dmidecode"
  (let [system-output (run-command "sudo dmidecode -t system 2>/dev/null")
        lines (str/split-lines system-output)
        manufacturer (-> (filter #(str/includes? % "Manufacturer:") lines)
                         first
                         (str/replace "Manufacturer:" "")
                         str/trim)
        product (-> (filter #(str/includes? % "Product Name:") lines)
                    first
                    (str/replace "Product Name:" "")
                    str/trim)
        version (-> (filter #(str/includes? % "Version:") lines)
                    first
                    (str/replace "Version:" "")
                    str/trim)]
    {:manufacturer (or manufacturer "Unknown")
     :product (or product "Unknown")
     :version (or version "Unknown")}))

(defn generate-hardware-config []
  "Generate complete hardware configuration"
  (println "🔍 Detecting hardware specifications...")
  
  (let [cpu-info (get-cpu-info)
        memory-info (get-memory-info)
        storage-info (get-storage-info)
        gpu-info (get-gpu-info)
        display-info (get-display-info)
        network-info (get-network-info)
        system-info (get-system-info)]
    
    (println "✅ Hardware detection complete!")
    
    {:hardware-specs
     {:system system-info
      :cpu cpu-info
      :memory memory-info
      :storage storage-info
      :gpu gpu-info
      :display display-info
      :networking network-info}
     
     :detection-timestamp (str (java.time.Instant/now))
     :detection-command "bb scripts/hardware-detector.bb"}))

(defn main [& args]
  "Main function"
  (let [config (generate-hardware-config)]
    (if (some #(= % "--json") args)
      (println (str config))
      (println (pr-str config)))))

;; Run if called directly
(when (= *file* (System/getProperty "babashka.file"))
  (apply main *command-line-args*))





