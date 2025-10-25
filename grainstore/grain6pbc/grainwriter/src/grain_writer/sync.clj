(ns grain-writer.sync
  "USB-C file synchronization for Grainwriter (iPod Touch-style)"
  (:require [clojure.java.io :as io]
            [babashka.fs :as fs]
            [cheshire.core :as json]
            [grain-writer.core :as core])
  (:import [java.nio.file Files Paths]
           [javax.usb UsbHostManager UsbHub UsbDevice]))

;; ============================================================================
;; USB DEVICE DETECTION
;; ============================================================================

(defn list-usb-devices []
  "List all connected USB devices"
  ;; TODO: Implement proper USB device enumeration using usb4java
  ;; For now, use filesystem-based detection
  (let [media-path (io/file "/media")
        mnt-path (io/file "/mnt")
        run-media-path (io/file (str "/run/media/" (System/getProperty "user.name")))]
    (->> [media-path mnt-path run-media-path]
         (filter #(.exists %))
         (mapcat #(.listFiles %))
         (filter #(.isDirectory %))
         (map #(.getAbsolutePath %)))))

(defn detect-usb-device []
  "Detect connected USB storage device for sync"
  (let [devices (list-usb-devices)]
    (when (seq devices)
      (println "Found USB devices:" devices)
      (first devices)))) ; Return first detected device

(defn is-grainwriter-sync-device? [device-path]
  "Check if USB device has Grainwriter sync marker"
  (let [marker-file (io/file device-path ".grainwriter-sync")]
    (.exists marker-file)))

(defn create-sync-marker [device-path]
  "Create Grainwriter sync marker on USB device"
  (let [marker-file (io/file device-path ".grainwriter-sync")
        marker-data {:device-type "grainwriter-sync"
                     :version "1.0.0"
                     :created-at (core/now)}]
    (spit marker-file (json/generate-string marker-data {:pretty true}))
    marker-file))

;; ============================================================================
;; SYNC DIRECTORY STRUCTURE
;; ============================================================================

(defn get-sync-path [device-path]
  "Get the Grainwriter sync directory path"
  (str device-path "/Grainwriter"))

(defn ensure-sync-directory [device-path]
  "Ensure Grainwriter sync directory exists on USB device"
  (let [sync-path (get-sync-path device-path)
        sync-dir (io/file sync-path)]
    (when-not (.exists sync-dir)
      (.mkdirs sync-dir)
      (create-sync-marker device-path))
    sync-path))

;; ============================================================================
;; DOCUMENT EXPORT/IMPORT
;; ============================================================================

(defn export-document-to-file [doc output-path]
  "Export a single document to a file"
  (let [doc-id (:id doc)
        filename (str doc-id ".json")
        filepath (str output-path "/" filename)]
    (spit filepath (json/generate-string doc {:pretty true}))
    filepath))

(defn export-all-documents [device-path]
  "Export all documents to USB device"
  (let [sync-path (ensure-sync-directory device-path)
        export-data (core/export-all-documents)
        manifest-path (str sync-path "/manifest.json")
        docs-dir (str sync-path "/documents")]
    
    ;; Create documents directory
    (.mkdirs (io/file docs-dir))
    
    ;; Export manifest
    (spit manifest-path (json/generate-string export-data {:pretty true}))
    
    ;; Export individual documents
    (doseq [doc (:documents export-data)]
      (export-document-to-file doc docs-dir))
    
    {:exported (count (:documents export-data))
     :path sync-path}))

(defn import-document-from-file [filepath]
  "Import a single document from a file"
  (try
    (json/parse-string (slurp filepath) true)
    (catch Exception e
      (println "Failed to import document from" filepath ":" (.getMessage e))
      nil)))

(defn import-all-documents [device-path]
  "Import all documents from USB device"
  (let [sync-path (get-sync-path device-path)
        manifest-path (str sync-path "/manifest.json")]
    
    (if (.exists (io/file manifest-path))
      (let [manifest (json/parse-string (slurp manifest-path) true)
            result (core/import-documents manifest)]
        (println "Imported" (:imported result) "documents from USB device")
        result)
      (do
        (println "No Grainwriter sync data found on USB device")
        {:imported 0
         :error "No manifest found"}))))

;; ============================================================================
;; BIDIRECTIONAL SYNC
;; ============================================================================

(defn sync-documents
  "Perform bidirectional sync with USB device"
  [device-path]
  (println "Starting bidirectional sync...")
  
  ;; 1. Export current documents to USB
  (let [export-result (export-all-documents device-path)]
    (println "Exported" (:exported export-result) "documents to USB"))
  
  ;; 2. Import documents from USB (with merge)
  (let [import-result (import-all-documents device-path)]
    (println "Imported" (:imported import-result) "documents from USB"))
  
  {:sync-complete true
   :device-path device-path
   :timestamp (core/now)})

;; ============================================================================
;; AUTOMATIC SYNC DETECTION
;; ============================================================================

(defn start-sync-monitor
  "Start monitoring for USB device connection (for automatic sync)"
  [callback-fn]
  ;; TODO: Implement udev monitoring for automatic USB detection
  ;; For now, provide manual sync trigger
  (println "Sync monitor started. Connect USB device and call (sync-documents device-path)"))

(defn stop-sync-monitor []
  "Stop USB device monitoring"
  (println "Sync monitor stopped"))

;; ============================================================================
;; BACKUP VERIFICATION
;; ============================================================================

(defn verify-backup [device-path]
  "Verify integrity of backup on USB device"
  (let [sync-path (get-sync-path device-path)
        manifest-path (str sync-path "/manifest.json")]
    
    (if (.exists (io/file manifest-path))
      (let [manifest (json/parse-string (slurp manifest-path) true)
            local-docs (core/get-all-documents)
            usb-doc-count (count (:documents manifest))
            local-doc-count (count local-docs)]
        {:verified true
         :usb-documents usb-doc-count
         :local-documents local-doc-count
         :match? (= usb-doc-count local-doc-count)})
      {:verified false
       :error "No backup found"})))

;; ============================================================================
;; PUBLIC API
;; ============================================================================

(defn quick-sync []
  "Quick sync: detect USB device and sync automatically"
  (if-let [device (detect-usb-device)]
    (do
      (println "USB device detected:" device)
      (sync-documents device))
    (do
      (println "No USB device detected. Please connect a USB drive.")
      {:error "No USB device found"})))

(comment
  ;; REPL Usage Examples
  
  ;; List USB devices
  (list-usb-devices)
  
  ;; Detect device
  (def my-usb (detect-usb-device))
  
  ;; Manual sync
  (sync-documents my-usb)
  
  ;; Quick sync (auto-detect)
  (quick-sync)
  
  ;; Verify backup
  (verify-backup my-usb)
  
  ;; Export only
  (export-all-documents my-usb)
  
  ;; Import only
  (import-all-documents my-usb)
  )


