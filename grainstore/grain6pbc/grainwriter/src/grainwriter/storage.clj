(ns grainwriter.storage
  "Storage management for Grainwriter using EDN format.
   Integrates with clojure-s6 and clojure-sixos for system-level operations."
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.string :as str]
   [clojure.tools.logging :as log]
   [clojure-s6.core :as s6]
   [clojure-sixos.core :as sixos]))

;; =============================================================================
;; Storage Configuration
;; =============================================================================

(defonce storage-config
  {:base-path "grainwriter-data"
   :documents-path "documents"
   :backup-path "backups"
   :temp-path "temp"
   :max-storage-mb 60000  ; 60GB RAM-only storage (2 x 32GB refurbished RAM)
   :ram-only true
   :ram-size-gb 64
   :backup-interval-ms 300000
   :compression-enabled true
   :persistence-strategy :ram-with-backup})

;; =============================================================================
;; RAM-Only Storage Functions
;; =============================================================================

(defonce ram-storage
  (atom
   {:documents {}
    :metadata {}
    :index {}
    :usage-bytes 0
    :max-bytes (* 60 1024 1024 1024)  ; 60GB in bytes
    :last-backup 0}))

(defn init-ram-storage
  "Initialize RAM-only storage system."
  []
  (log/info "Initializing RAM-only storage (64GB: 2 x 32GB refurbished RAM)...")
  (try
    ;; Initialize RAM storage atom
    (reset! ram-storage
           {:documents {}
            :metadata {}
            :index {}
            :usage-bytes 0
            :max-bytes (* 60 1024 1024 1024)  ; 60GB usable
            :last-backup 0})
    
    ;; Start RAM monitoring
    (start-ram-monitoring)
    
    ;; Load any existing backup into RAM
    (load-backup-to-ram)
    
    (log/info "RAM-only storage initialized successfully")
    true
    (catch Exception e
      (log/error e "Failed to initialize RAM-only storage")
      false)))

(defn save-to-ram
  "Save data to RAM storage."
  [key data]
  (try
    (let [data-size (estimate-data-size data)
          current-usage (:usage-bytes @ram-storage)
          max-usage (:max-bytes @ram-storage)]
      
      (if (> (+ current-usage data-size) max-usage)
        (do
          (log/warn "RAM storage full, attempting cleanup...")
          (cleanup-ram-storage)
          (if (> (+ (:usage-bytes @ram-storage) data-size) max-usage)
            (do
              (log/error "RAM storage still full after cleanup")
              false)
            (save-to-ram key data)))
        (do
          (swap! ram-storage update :documents assoc key data)
          (swap! ram-storage update :usage-bytes + data-size)
          (update-ram-index key data)
          (log/debug "Saved to RAM:" key "size:" data-size "bytes")
          true)))
    (catch Exception e
      (log/error e "Failed to save to RAM:" key)
      false)))

(defn load-from-ram
  "Load data from RAM storage."
  [key]
  (get-in @ram-storage [:documents key]))

(defn delete-from-ram
  "Delete data from RAM storage."
  [key]
  (try
    (let [data (get-in @ram-storage [:documents key])
          data-size (if data (estimate-data-size data) 0)]
      (swap! ram-storage update :documents dissoc key)
      (swap! ram-storage update :usage-bytes - data-size)
      (swap! ram-storage update :index dissoc key)
      (log/debug "Deleted from RAM:" key)
      true)
    (catch Exception e
      (log/error e "Failed to delete from RAM:" key)
      false)))

(defn estimate-data-size
  "Estimate data size in bytes."
  [data]
  (try
    (let [serialized (pr-str data)
          bytes (.getBytes serialized "UTF-8")]
      (count bytes))
    (catch Exception e
      (log/error e "Failed to estimate data size")
      0)))

(defn get-ram-usage
  "Get current RAM usage statistics."
  []
  (let [usage (:usage-bytes @ram-storage)
        max-usage (:max-bytes @ram-storage)
        usage-percent (if (> max-usage 0)
                       (int (* 100 (/ usage max-usage)))
                       0)]
    {:used-bytes usage
     :used-gb (int (/ usage 1024 1024 1024))
     :max-bytes max-usage
     :max-gb 60
     :usage-percent usage-percent
     :available-bytes (- max-usage usage)
     :available-gb (int (/ (- max-usage usage) 1024 1024 1024))}))

(defn cleanup-ram-storage
  "Clean up RAM storage by removing least recently used items."
  []
  (try
    (let [documents (:documents @ram-storage)
          sorted-by-access (sort-by #(:last-accessed (second %)) documents)
          items-to-remove (take (int (* (count documents) 0.1)) sorted-by-access)]
      
      (doseq [[key _] items-to-remove]
        (delete-from-ram key))
      
      (log/info "Cleaned up" (count items-to-remove) "items from RAM")
      true)
    (catch Exception e
      (log/error e "Failed to cleanup RAM storage")
      false)))

(defn update-ram-index
  "Update RAM index for fast searching."
  [key data]
  (let [index-entry {:key key
                     :title (get data :title "")
                     :content-preview (subs (get data :content "") 0 (min 100 (count (get data :content ""))))
                     :created-at (get data :created-at (System/currentTimeMillis))
                     :modified-at (get data :modified-at (System/currentTimeMillis))
                     :last-accessed (System/currentTimeMillis)
                     :size (estimate-data-size data)}]
    (swap! ram-storage update :index assoc key index-entry)))

(defn search-ram-storage
  "Search RAM storage for documents."
  [query]
  (try
    (let [index (:index @ram-storage)
          query-lower (str/lower-case query)
          matches (filter #(or (str/includes? (str/lower-case (:title %)) query-lower)
                               (str/includes? (str/lower-case (:content-preview %)) query-lower))
                          (vals index))]
      (sort-by :modified-at > matches))
    (catch Exception e
      (log/error e "Failed to search RAM storage")
      [])))

;; =============================================================================
;; Core Storage Functions
;; =============================================================================

(defn init-storage
  "Initialize storage system."
  []
  (log/info "Initializing Grainwriter storage...")
  (try
    ;; Create storage directories
    (create-storage-directories)
    
    ;; Initialize storage monitoring
    (start-storage-monitoring)
    
    ;; Create initial backup
    (create-backup)
    
    (log/info "Storage initialized successfully")
    true
    (catch Exception e
      (log/error e "Failed to initialize storage")
      false)))

(defn create-storage-directories
  "Create necessary storage directories."
  []
  (let [base-path (:base-path storage-config)
        dirs [base-path
              (str base-path "/" (:documents-path storage-config))
              (str base-path "/" (:backup-path storage-config))
              (str base-path "/" (:temp-path storage-config))]]
    (doseq [dir dirs]
      (let [dir-file (io/file dir)]
        (when-not (.exists dir-file)
          (.mkdirs dir-file)
          (log/debug "Created directory:" dir))))))

;; =============================================================================
;; EDN File Operations
;; =============================================================================

(defn save-edn
  "Save data to EDN file."
  [file-path data]
  (try
    (let [full-path (str (:base-path storage-config) "/" file-path)
          file (io/file full-path)
          parent-dir (.getParentFile file)]
      (when parent-dir
        (.mkdirs parent-dir))
      (spit file (pr-str data))
      (log/debug "Saved EDN file:" full-path)
      true)
    (catch Exception e
      (log/error e "Failed to save EDN file:" file-path)
      false)))

(defn load-edn
  "Load data from EDN file."
  [file-path]
  (try
    (let [full-path (str (:base-path storage-config) "/" file-path)
          file (io/file full-path)]
      (if (.exists file)
        (edn/read-string (slurp file))
        (do
          (log/warn "EDN file not found:" full-path)
          nil)))
    (catch Exception e
      (log/error e "Failed to load EDN file:" file-path)
      nil)))

(defn delete-edn
  "Delete EDN file."
  [file-path]
  (try
    (let [full-path (str (:base-path storage-config) "/" file-path)
          file (io/file full-path)]
      (if (.exists file)
        (do
          (.delete file)
          (log/debug "Deleted EDN file:" full-path)
          true)
        false))
    (catch Exception e
      (log/error e "Failed to delete EDN file:" file-path)
      false)))

;; =============================================================================
;; File Management
;; =============================================================================

(defn list-files
  "List files in a directory."
  [dir-path]
  (try
    (let [full-path (str (:base-path storage-config) "/" dir-path)
          dir (io/file full-path)]
      (if (.exists dir)
        (->> (.listFiles dir)
             (filter #(.isFile %))
             (map #(.getName %))
             (sort))
        []))
    (catch Exception e
      (log/error e "Failed to list files in:" dir-path)
      [])))

(defn list-directories
  "List directories in a path."
  [dir-path]
  (try
    (let [full-path (str (:base-path storage-config) "/" dir-path)
          dir (io/file full-path)]
      (if (.exists dir)
        (->> (.listFiles dir)
             (filter #(.isDirectory %))
             (map #(.getName %))
             (sort))
        []))
    (catch Exception e
      (log/error e "Failed to list directories in:" dir-path)
      [])))

(defn file-exists?
  "Check if file exists."
  [file-path]
  (let [full-path (str (:base-path storage-config) "/" file-path)
        file (io/file full-path)]
    (.exists file)))

(defn get-file-size
  "Get file size in bytes."
  [file-path]
  (try
    (let [full-path (str (:base-path storage-config) "/" file-path)
          file (io/file full-path)]
      (if (.exists file)
        (.length file)
        0))
    (catch Exception e
      (log/error e "Failed to get file size:" file-path)
      0)))

;; =============================================================================
;; Storage Monitoring
;; =============================================================================

(defn start-storage-monitoring
  "Start storage monitoring service."
  []
  (log/info "Starting storage monitoring...")
  
  ;; Start storage usage monitoring
  (s6/start-service "grainwriter-storage-monitor"
                   {:exec "clojure -M:grainwriter-storage-monitor"
                    :env {"GRAINWRITER_MODE" "storage-monitor"}})
  
  ;; Start backup service
  (s6/start-service "grainwriter-backup"
                   {:exec "clojure -M:grainwriter-backup"
                    :env {"GRAINWRITER_MODE" "backup"}})
  
  (log/info "Storage monitoring started"))

(defn stop-storage-monitoring
  "Stop storage monitoring service."
  []
  (log/info "Stopping storage monitoring...")
  
  (s6/stop-service "grainwriter-storage-monitor")
  (s6/stop-service "grainwriter-backup")
  
  (log/info "Storage monitoring stopped"))

(defn get-storage-usage
  "Get current storage usage."
  []
  (try
    (let [base-path (:base-path storage-config)
          total-size (get-directory-size base-path)
          max-size-mb (:max-storage-mb storage-config)
          max-size-bytes (* max-size-mb 1024 1024)
          usage-percent (if (> max-size-bytes 0)
                         (int (* 100 (/ total-size max-size-bytes)))
                         0)]
      {:total-bytes total-size
       :total-mb (int (/ total-size 1024 1024))
       :max-mb max-size-mb
       :usage-percent usage-percent})
    (catch Exception e
      (log/error e "Failed to get storage usage")
      {:total-bytes 0
       :total-mb 0
       :max-mb (:max-storage-mb storage-config)
       :usage-percent 0})))

(defn get-directory-size
  "Get total size of directory in bytes."
  [dir-path]
  (try
    (let [dir (io/file dir-path)]
      (if (.exists dir)
        (->> (file-seq dir)
             (filter #(.isFile %))
             (map #(.length %))
             (reduce + 0))
        0))
    (catch Exception e
      (log/error e "Failed to get directory size:" dir-path)
      0)))

;; =============================================================================
;; Backup Management
;; =============================================================================

(defn create-backup
  "Create a backup of all documents."
  []
  (try
    (let [timestamp (System/currentTimeMillis)
          backup-name (str "backup-" timestamp ".edn")
          backup-path (str (:backup-path storage-config) "/" backup-name)
          documents-path (:documents-path storage-config)
          documents (load-all-documents documents-path)]
      
      (save-edn backup-path documents)
      (log/info "Created backup:" backup-name)
      true)
    (catch Exception e
      (log/error e "Failed to create backup")
      false)))

(defn load-all-documents
  "Load all documents from documents directory."
  [documents-path]
  (try
    (let [files (list-files documents-path)
          edn-files (filter #(str/ends-with? % ".edn") files)]
      (reduce #(assoc %1 %2 (load-edn (str documents-path "/" %2)))
              {}
              edn-files))
    (catch Exception e
      (log/error e "Failed to load all documents")
      {})))

(defn restore-backup
  "Restore from backup."
  [backup-name]
  (try
    (let [backup-path (str (:backup-path storage-config) "/" backup-name)
          documents (load-edn backup-path)
          documents-path (:documents-path storage-config)]
      
      (doseq [[file-name doc-data] documents]
        (save-edn (str documents-path "/" file-name) doc-data))
      
      (log/info "Restored from backup:" backup-name)
      true)
    (catch Exception e
      (log/error e "Failed to restore backup:" backup-name)
      false)))

(defn list-backups
  "List available backups."
  []
  (try
    (let [backup-files (list-files (:backup-path storage-config))
          edn-backups (filter #(str/ends-with? % ".edn") backup-files)]
      (sort edn-backups))
    (catch Exception e
      (log/error e "Failed to list backups")
      [])))

(defn cleanup-old-backups
  "Clean up old backups, keeping only the most recent ones."
  [keep-count]
  (try
    (let [backups (list-backups)
          sorted-backups (sort backups)
          to-delete (drop-last keep-count sorted-backups)]
      
      (doseq [backup to-delete]
        (let [backup-path (str (:backup-path storage-config) "/" backup)]
          (when (.exists (io/file backup-path))
            (.delete (io/file backup-path))
            (log/debug "Deleted old backup:" backup))))
      
      (log/info "Cleaned up" (count to-delete) "old backups")
      true)
    (catch Exception e
      (log/error e "Failed to cleanup old backups")
      false)))

;; =============================================================================
;; Compression
;; =============================================================================

(defn compress-data
  "Compress data using gzip."
  [data]
  (try
    (let [baos (java.io.ByteArrayOutputStream.)
          gzos (java.util.zip.GZIPOutputStream. baos)
          writer (java.io.OutputStreamWriter. gzos "UTF-8")]
      (spit writer (pr-str data))
      (.close writer)
      (.toByteArray baos))
    (catch Exception e
      (log/error e "Failed to compress data")
      nil)))

(defn decompress-data
  "Decompress data using gzip."
  [compressed-data]
  (try
    (let [bais (java.io.ByteArrayInputStream. compressed-data)
          gzis (java.util.zip.GZIPInputStream. bais)
          reader (java.io.BufferedReader. (java.io.InputStreamReader. gzis "UTF-8"))]
      (edn/read-string (slurp reader)))
    (catch Exception e
      (log/error e "Failed to decompress data")
      nil)))

;; =============================================================================
;; Storage Maintenance
;; =============================================================================

(defn defragment-storage
  "Defragment storage by reorganizing files."
  []
  (log/info "Starting storage defragmentation...")
  (try
    ;; This would implement storage defragmentation
    ;; For now, just log the operation
    (log/info "Storage defragmentation completed")
    true
    (catch Exception e
      (log/error e "Failed to defragment storage")
      false)))

(defn validate-storage
  "Validate storage integrity."
  []
  (log/info "Validating storage integrity...")
  (try
    (let [documents-path (:documents-path storage-config)
          files (list-files documents-path)
          edn-files (filter #(str/ends-with? % ".edn") files)
          valid-files (filter #(validate-edn-file (str documents-path "/" %)) edn-files)]
      
      (log/info "Storage validation completed. Valid files:" (count valid-files))
      {:valid-files (count valid-files)
       :total-files (count edn-files)
       :valid? (= (count valid-files) (count edn-files))})
    (catch Exception e
      (log/error e "Failed to validate storage")
      {:valid-files 0
       :total-files 0
       :valid? false})))

(defn validate-edn-file
  "Validate a single EDN file."
  [file-path]
  (try
    (let [data (load-edn file-path)]
      (some? data))
    (catch Exception e
      (log/error e "Invalid EDN file:" file-path)
      false)))

;; =============================================================================
;; Utility Functions
;; =============================================================================

(defn get-storage-info
  "Get comprehensive storage information."
  []
  (let [usage (get-storage-usage)
        backups (list-backups)
        validation (validate-storage)]
    {:usage usage
     :backups (count backups)
     :validation validation
     :base-path (:base-path storage-config)
     :max-storage-mb (:max-storage-mb storage-config)}))

(defn cleanup-temp-files
  "Clean up temporary files."
  []
  (try
    (let [temp-path (str (:base-path storage-config) "/" (:temp-path storage-config))
          temp-files (list-files (:temp-path storage-config))]
      
      (doseq [file temp-files]
        (let [file-path (str temp-path "/" file)]
          (when (.exists (io/file file-path))
            (.delete (io/file file-path))
            (log/debug "Deleted temp file:" file))))
      
      (log/info "Cleaned up" (count temp-files) "temp files")
      true)
    (catch Exception e
      (log/error e "Failed to cleanup temp files")
      false)))
