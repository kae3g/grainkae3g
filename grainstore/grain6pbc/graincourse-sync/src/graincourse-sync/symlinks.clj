(ns graincourse-sync.symlinks
  "Symlink management for GrainCourse Sync
  
  Creates immutable grainpath subdirectories with proper nesting:
  /course/{author}/{course-name}/{graintime}/
  
  The index.html and all course content is placed at the end of the grainpath,
  with all other HTML files and subdirectories relative to that location."
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(defn create-directory-structure
  "Create the full grainpath directory structure
  
  Example grainpath: /course/kae3g/grain-net-fund/12025-10-23--0122--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/
  
  Creates:
  - course/
    - kae3g/
      - grain-net-fund/
        - 12025-10-23--0122--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/
          - index.html (and all course content)
          - lessons/
          - assets/
          - etc."
  [base-dir grainpath]
  (let [;; Remove leading and trailing slashes from grainpath
        clean-path (-> grainpath
                       (str/replace #"^/" "")
                       (str/replace #"/$" ""))
        ;; Split into path components
        path-components (str/split clean-path #"/")
        ;; Create the full directory structure
        full-path (str base-dir "/" clean-path)]
    
    ;; Create all intermediate directories
    (doseq [i (range 1 (inc (count path-components)))]
      (let [partial-path (str base-dir "/" (str/join "/" (take i path-components)))]
        (when-not (.exists (io/file partial-path))
          (.mkdirs (io/file partial-path)))))
    
    full-path))

(defn create-symlink
  "Create symlink from source to target with grainpath structure
  
  Args:
  - source-dir: Course content directory (e.g., personal/course-name/)
  - target-dir: Base directory for symlinks (e.g., symlinks/github-pages/)
  - grainpath: Full grainpath (e.g., /course/kae3g/grain-net-fund/12025-10-23--.../)
  
  Creates the full grainpath directory structure and symlinks the course content
  to the final directory in the grainpath."
  [source-dir target-dir grainpath]
  (let [;; Create the full grainpath directory structure
        grainpath-dir (create-directory-structure target-dir grainpath)
        ;; The symlink target is the final directory in the grainpath
        symlink-target grainpath-dir]
    
    ;; Remove existing file/directory if it exists
    (let [target-file (io/file symlink-target)]
      (when (.exists target-file)
        (if (try (java.nio.file.Files/isSymbolicLink (.toPath target-file))
                 (catch Exception _ false))
          (io/delete-file target-file)
          (do
            (println (str "🗑️  Removing existing directory: " symlink-target))
            (doseq [file (file-seq target-file)]
              (when (.isFile file)
                (io/delete-file file)))
            (doseq [file (reverse (file-seq target-file))]
              (when (.isDirectory file)
                (io/delete-file file)))))))
    
    ;; Create the symlink
    (try
      (let [source-file (io/file source-dir)
            target-file (io/file symlink-target)]
        (if (.exists source-file)
          (do
            ;; Create parent directories if they don't exist
            (.mkdirs (.getParentFile target-file))
          ;; Create the symlink
          (java.nio.file.Files/createSymbolicLink (.toPath target-file) (.toPath source-file) (into-array java.nio.file.attribute.FileAttribute []))
            (println (str "✅ Created symlink: " symlink-target " -> " source-dir))
            true)
          (do
            (println (str "❌ Source directory does not exist: " source-dir))
            false)))
      (catch Exception e
        (println (str "❌ Failed to create symlink: " (.getMessage e)))
        (println (str "   Source: " source-dir))
        (println (str "   Target: " symlink-target))
        (println (str "   Exception type: " (type e)))
        false))))

(defn symlink-valid?
  "Check if a symlink is valid (exists and points to valid target)"
  [symlink-path]
  (try
    (let [file (io/file symlink-path)]
      (and (.exists file)
           (java.nio.file.Files/isSymbolicLink (.toPath file))
           (.exists (.getCanonicalFile file))))
    (catch Exception _
      false)))

(defn validate-symlinks
  "Validate all symlinks in a directory"
  [dir-path]
  (let [dir (io/file dir-path)]
    (if (.exists dir)
      (->> (.listFiles dir)
           (filter #(try (java.nio.file.Files/isSymbolicLink (.toPath %))
                         (catch Exception _ false)))
           (filter symlink-valid?)
           (map #(.getName %))
           (sort))
      [])))

(defn list-symlinks
  "List all symlinks in a directory"
  [dir-path]
  (let [dir (io/file dir-path)]
    (if (.exists dir)
      (->> (.listFiles dir)
           (filter #(try (java.nio.file.Files/isSymbolicLink (.toPath %))
                         (catch Exception _ false)))
           (map #(.getName %))
           (sort))
      [])))

(defn cleanup-symlinks
  "Clean up old symlinks, keeping only the most recent ones"
  [dir-path keep-count]
  (let [dir (io/file dir-path)
        symlinks (->> (.listFiles dir)
                      (filter #(try (java.nio.file.Files/isSymbolicLink (.toPath %))
                                    (catch Exception _ false)))
                      (sort-by #(.lastModified %))
                      (reverse))]
    
    (when (> (count symlinks) keep-count)
      (let [to-remove (drop keep-count symlinks)]
        (doseq [symlink to-remove]
          (try
            (io/delete-file symlink)
            (catch Exception e
              (println (str "⚠️  Could not remove old symlink: " (.getName symlink))))))
        (count to-remove)))
    0))

(defn extract-graintime-from-grainpath
  "Extract graintime from grainpath for symlink naming
  
  Example: /course/kae3g/grain-net-fund/12025-10-23--0122--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/
  Returns: 12025-10-23--0122--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g"
  [grainpath]
  (let [;; Find the last component that looks like a graintime
        components (str/split grainpath #"/")
        graintime-candidates (filter #(str/includes? % "12025-") components)]
    (last graintime-candidates)))

(defn create-grainpath-symlink
  "Create symlink using grainpath structure
  
  This is the main function that creates the proper grainpath nested structure:
  1. Parse the grainpath to extract components
  2. Create the full directory structure
  3. Symlink the course content to the final directory
  
  Args:
  - source-dir: Course content directory
  - base-target-dir: Base directory for symlinks (e.g., symlinks/github-pages/)
  - grainpath: Full grainpath (e.g., /course/kae3g/grain-net-fund/12025-10-23--.../)
  
  Returns: {:success true/false :target-path string :grainpath string}"
  [source-dir base-target-dir grainpath]
  (try
    (let [;; Create the full grainpath directory structure
          target-path (create-directory-structure base-target-dir grainpath)
          ;; Create the symlink
          success (create-symlink source-dir base-target-dir grainpath)]
      
      {:success success
       :target-path target-path
       :grainpath grainpath
       :source-dir source-dir})
    (catch Exception e
      {:success false
       :error (.getMessage e)
       :grainpath grainpath})))
