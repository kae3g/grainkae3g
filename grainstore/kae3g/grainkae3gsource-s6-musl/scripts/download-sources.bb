#!/usr/bin/env bb

(require '[babashka.process :refer [shell]]
         '[clojure.java.io :as io])

;;; ╔═══════════════════════════════════════════════════════════════════╗
;;; ║                                                                   ║
;;; ║   🌾  D O W N L O A D   S 6   S O U R C E S                      ║
;;; ║                                                                   ║
;;; ║   From skarnet.org - Laurent Bercot's software                   ║
;;; ║                                                                   ║
;;; ╚═══════════════════════════════════════════════════════════════════╝

(def versions
  {:skalibs "2.14.4.0"
   :execline "2.9.7.0"
   :s6 "2.13.2.0"})

(def base-url "https://skarnet.org/software")

(defn download-source [package version]
  (let [pkg-name (name package)
        filename (str pkg-name "-" version ".tar.gz")
        url (str base-url "/" pkg-name "/" filename)
        output-path (str "src/" filename)]
    
    (println (str "Downloading " pkg-name " " version))
    (println (str "URL: " url))
    
    (if (.exists (io/file output-path))
      (println (str "Already exists: " output-path))
      (do
        (io/make-parents output-path)
        (println "Fetching...")
        (shell "wget" "-q" "-O" output-path url)
        (println (str "Downloaded: " output-path))
        
        ;; Show size
        (let [file-size (.length (io/file output-path))
              size-kb (/ file-size 1024.0)]
          (println (str "Size: " (format "%.1f KB" size-kb))))))))

(defn verify-downloads []
  (println)
  (println "Verifying downloads...")
  (println)
  
  (doseq [[pkg version] versions]
    (let [pkg-name (name pkg)
          filename (str pkg-name "-" version ".tar.gz")
          filepath (str "src/" filename)]
      
      (if (.exists (io/file filepath))
        (let [file-size (.length (io/file filepath))
              size-kb (/ file-size 1024.0)]
          (println (str "OK " filename " (" (format "%.1f KB" size-kb) ")")))
        (println (str "MISSING: " filename))))))

(defn main []
  (println "S6 Source Downloader")
  (println "skarnet.org/software")
  (println)
  (println "Package versions:")
  (doseq [[pkg version] versions]
    (println (str "   " (name pkg) ": " version)))
  (println)
  
  ;; Download each package
  (doseq [[pkg version] versions]
    (download-source pkg version)
    (println))
  
  ;; Verify all downloads
  (verify-downloads)
  
  (println)
  (println "DOWNLOADS COMPLETE")
  (println)
  (println "Next steps:")
  (println "  bb build:all    - Build with musl")
  (println "  bb test         - Test binaries")
  (println)
  (println "now == next + 1"))

(main)
