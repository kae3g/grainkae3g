(ns clojure-photos.core
  "Core photo management library for the Grain Network.
   Supports AVIF, HEIC, PNG, and JPEG formats."
  (:require [clojure.java.io :as io]
            [clojure.core.async :as async]
            [clojure.tools.logging :as log]
            [me.raynes.fs :as fs]
            [java-time :as time]
            [clojure-photos.formats.avif :as avif]
            [clojure-photos.formats.heic :as heic]
            [clojure-photos.formats.common :as common]
            [clojure-photos.metadata :as metadata]
            [clojure-photos.processing :as processing])
  (:import [java.awt.image BufferedImage]
           [javax.imageio ImageIO]))

;;; Photo Loading

(defn load-photo
  "Load a photo from file path.
   Supports: AVIF, HEIC, PNG, JPEG.
   
   Returns a map with:
   {:image BufferedImage
    :format :avif|:heic|:png|:jpeg
    :metadata {...}
    :path \"...\"}"
  [file-path]
  (log/info "Loading photo:" file-path)
  (let [file (io/file file-path)
        ext (fs/extension file-path)
        format (case ext
                 ".avif" :avif
                 ".heic" :heic
                 ".png" :png
                 ".jpg" :jpeg
                 ".jpeg" :jpeg
                 (throw (ex-info "Unsupported file format" {:path file-path :ext ext})))]
    (cond
      (= format :avif)
      (avif/load-avif file-path)
      
      (= format :heic)
      (heic/load-heic file-path)
      
      (#{:png :jpeg} format)
      (let [image (ImageIO/read file)]
        {:image image
         :format format
         :metadata (metadata/extract-exif file-path)
         :path file-path})
      
      :else
      (throw (ex-info "Unsupported format" {:format format})))))

(defn load-photos
  "Load multiple photos from a directory.
   Returns a lazy sequence of photo maps."
  [directory & {:keys [recursive? extensions]
                :or {recursive? false
                     extensions #{".avif" ".heic" ".png" ".jpg" ".jpeg"}}}]
  (log/info "Loading photos from directory:" directory)
  (let [files (if recursive?
                (fs/find-files directory #(extensions (fs/extension %)))
                (filter #(extensions (fs/extension %))
                       (fs/list-dir directory)))]
    (map load-photo files)))

;;; Photo Saving

(defn save-photo
  "Save a photo to file path.
   Format is determined by file extension.
   
   Options:
   :quality - Compression quality (0-100, default 90)
   :metadata - Metadata map to embed in photo"
  [photo file-path & {:keys [quality metadata]
                      :or {quality 90}}]
  (log/info "Saving photo:" file-path)
  (let [ext (fs/extension file-path)
        format (case ext
                 ".avif" :avif
                 ".heic" :heic
                 ".png" :png
                 ".jpg" :jpeg
                 ".jpeg" :jpeg
                 (throw (ex-info "Unsupported file format" {:path file-path :ext ext})))]
    (cond
      (= format :avif)
      (avif/save-avif (:image photo) file-path :quality quality :metadata metadata)
      
      (= format :heic)
      (heic/save-heic (:image photo) file-path :quality quality :metadata metadata)
      
      (#{:png :jpeg} format)
      (do
        (ImageIO/write (:image photo) (name format) (io/file file-path))
        (when metadata
          (metadata/embed-exif file-path metadata)))
      
      :else
      (throw (ex-info "Unsupported format" {:format format})))))

;;; Photo Information

(defn photo-info
  "Get information about a photo without loading the full image data.
   
   Returns:
   {:width ...
    :height ...
    :format ...
    :size-bytes ...
    :created-at ...
    :exif {...}}"
  [file-path]
  (log/info "Getting photo info:" file-path)
  (let [file (io/file file-path)
        stats (fs/file? file-path)]
    {:path file-path
     :size-bytes (fs/size file-path)
     :format (common/detect-format file-path)
     :created-at (time/instant (fs/mod-time file-path))
     :metadata (metadata/extract-exif file-path)
     :dimensions (common/get-dimensions file-path)}))

(defn list-photos
  "List all photos in a directory with their information.
   Returns a lazy sequence of photo info maps."
  [directory & {:keys [recursive? extensions]
                :or {recursive? false
                     extensions #{".avif" ".heic" ".png" ".jpg" ".jpeg"}}}]
  (log/info "Listing photos in directory:" directory)
  (let [files (if recursive?
                (fs/find-files directory #(extensions (fs/extension %)))
                (filter #(extensions (fs/extension %))
                       (fs/list-dir directory)))]
    (map photo-info files)))

;;; Batch Operations

(defn convert-photos
  "Convert all photos in a directory to a new format.
   
   Options:
   :source-dir - Source directory (required)
   :target-dir - Target directory (required)
   :target-format - Target format :avif|:heic|:png|:jpeg (required)
   :quality - Compression quality (0-100, default 90)
   :recursive? - Process subdirectories (default false)
   :preserve-metadata? - Preserve original metadata (default true)"
  [& {:keys [source-dir target-dir target-format quality recursive? preserve-metadata?]
      :or {quality 90
           recursive? false
           preserve-metadata? true}}]
  (log/info "Converting photos from" source-dir "to" target-dir "format:" target-format)
  (let [photos (load-photos source-dir :recursive? recursive?)
        target-ext (case target-format
                     :avif ".avif"
                     :heic ".heic"
                     :png ".png"
                     :jpeg ".jpg")]
    (doseq [photo photos]
      (let [relative-path (fs/relativize source-dir (:path photo))
            target-path (str target-dir "/" (fs/base-name relative-path true) target-ext)]
        (fs/mkdirs (fs/parent target-path))
        (save-photo photo target-path
                   :quality quality
                   :metadata (when preserve-metadata? (:metadata photo)))
        (log/info "Converted:" (:path photo) "->" target-path)))))

(defn optimize-photos
  "Optimize all photos in a directory (lossless compression).
   
   Options:
   :directory - Directory to optimize (required)
   :recursive? - Process subdirectories (default false)
   :backup? - Create backups before optimization (default true)"
  [& {:keys [directory recursive? backup?]
      :or {recursive? false
           backup? true}}]
  (log/info "Optimizing photos in directory:" directory)
  (let [photos (list-photos directory :recursive? recursive?)]
    (doseq [photo-info photos]
      (let [original-path (:path photo-info)
            backup-path (str original-path ".backup")
            temp-path (str original-path ".temp")]
        (when backup?
          (fs/copy original-path backup-path))
        (try
          (let [photo (load-photo original-path)
                optimized (processing/optimize (:image photo))]
            (save-photo (assoc photo :image optimized) temp-path
                       :quality 100
                       :metadata (:metadata photo))
            (fs/delete original-path)
            (fs/rename temp-path original-path)
            (log/info "Optimized:" original-path))
          (catch Exception e
            (log/error e "Failed to optimize:" original-path)
            (when (fs/exists? backup-path)
              (fs/copy backup-path original-path))))))))

;;; Export/Import

(defn export-photo-collection
  "Export a collection of photos with metadata to a directory.
   Creates a manifest.edn file with collection metadata.
   
   Options:
   :photos - Collection of photo maps (required)
   :target-dir - Target directory (required)
   :format - Export format :avif|:heic|:png|:jpeg (default :avif)
   :quality - Compression quality (default 90)
   :include-metadata? - Include metadata (default true)"
  [& {:keys [photos target-dir format quality include-metadata?]
      :or {format :avif
           quality 90
           include-metadata? true}}]
  (log/info "Exporting photo collection to:" target-dir)
  (fs/mkdirs target-dir)
  (let [manifest {:exported-at (time/instant)
                  :format format
                  :photo-count (count photos)
                  :total-size-bytes 0
                  :photos []}
        ext (case format
              :avif ".avif"
              :heic ".heic"
              :png ".png"
              :jpeg ".jpg")]
    (reduce
     (fn [manifest photo]
       (let [filename (str (metadata/generate-id) ext)
             target-path (str target-dir "/" filename)]
         (save-photo photo target-path
                    :quality quality
                    :metadata (when include-metadata? (:metadata photo)))
         (update manifest :photos conj
                 {:filename filename
                  :original-path (:path photo)
                  :size-bytes (fs/size target-path)
                  :metadata (:metadata photo)})))
     manifest
     photos)))

(defn import-photo-collection
  "Import a photo collection from a directory with manifest.
   
   Options:
   :source-dir - Source directory with manifest.edn (required)
   
   Returns a lazy sequence of photo maps."
  [& {:keys [source-dir]}]
  (log/info "Importing photo collection from:" source-dir)
  (let [manifest-path (str source-dir "/manifest.edn")
        manifest (when (fs/exists? manifest-path)
                   (read-string (slurp manifest-path)))]
    (if manifest
      (map #(load-photo (str source-dir "/" (:filename %)))
           (:photos manifest))
      (load-photos source-dir))))

;;; Statistics

(defn collection-stats
  "Get statistics for a photo collection.
   
   Returns:
   {:total-photos ...
    :total-size-bytes ...
    :formats {...}
    :dimensions {...}
    :date-range {...}}"
  [directory & {:keys [recursive?]
                :or {recursive? false}}]
  (log/info "Calculating collection stats for:" directory)
  (let [photos (list-photos directory :recursive? recursive?)
        stats (reduce
               (fn [acc photo]
                 (-> acc
                     (update :total-photos inc)
                     (update :total-size-bytes + (:size-bytes photo))
                     (update-in [:formats (:format photo)] (fnil inc 0))
                     (update :earliest-date #(if % (time/min % (:created-at photo)) (:created-at photo)))
                     (update :latest-date #(if % (time/max % (:created-at photo)) (:created-at photo)))))
               {:total-photos 0
                :total-size-bytes 0
                :formats {}
                :earliest-date nil
                :latest-date nil}
               photos)]
    stats))


