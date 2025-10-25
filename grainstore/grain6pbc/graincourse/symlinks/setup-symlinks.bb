#!/usr/bin/env bb

;; Setup symlinks for immutable grainpath subdirectories
;; Links to GitHub Pages and Codeberg Pages for graincourse

(require '[clojure.string :as str]
         '[clojure.java.io :as io])

(def graincourse-dir "/home/xy/kae3g/grainkae3g/grainstore/graincourse")
(def symlinks-dir (str graincourse-dir "/symlinks"))
(def github-dir (str symlinks-dir "/github-pages"))
(def codeberg-dir (str symlinks-dir "/codeberg-pages"))

;; Current grainpath (from latest course)
(def current-grainpath "/course/kae3g/grain-net-fund/12025-10-23--0053--PDT--moon-vishakha--asc-gem000--sun-4th--kae3g/")

;; Extract grainpath components
(def grainpath-parts (str/split current-grainpath #"/"))
(def course-name (nth grainpath-parts 3 "grain-net-fund"))
(def graintime (nth grainpath-parts 4 "12025-10-23--0053--PDT--moon-vishakha--asc-gem000--sun-4th--kae3g"))

;; GitHub Pages URL structure
(def github-base "https://kae3g.github.io/grainkae3g")
(def github-path (str github-base current-grainpath))

;; Codeberg Pages URL structure  
(def codeberg-base "https://kae3g.codeberg.page/grainkae3g")
(def codeberg-path (str codeberg-base current-grainpath))

;; Find the actual course directory
(def personal-dir (str graincourse-dir "/personal"))
(def course-dirs (->> (io/file personal-dir)
                      (.listFiles)
                      (filter #(.isDirectory %))
                      (map #(.getName %))
                      (filter #(str/includes? % graintime))
                      (sort)
                      (reverse))) ; Most recent first

(def latest-course-dir (first course-dirs))

(println "")
(println "🌾 Setting up Graincourse Symlinks")
(println "")
(println (str "📅 Current Grainpath: " current-grainpath))
(println (str "📁 Latest Course Dir: " latest-course-dir))
(println "")

(when latest-course-dir
  (let [source-dir (str personal-dir "/" latest-course-dir)
        github-target (str github-dir "/" graintime)
        codeberg-target (str codeberg-dir "/" graintime)]
    
    ;; Create symlinks
    (try
      ;; GitHub Pages symlink
      (when (.exists (io/file github-target))
        (io/delete-file github-target))
      (io/make-parents github-target)
      (println (str "🔗 Creating GitHub symlink: " github-target " -> " source-dir))
      (println (str "   URL: " github-path))
      
      ;; Codeberg Pages symlink  
      (when (.exists (io/file codeberg-target))
        (io/delete-file codeberg-target))
      (io/make-parents codeberg-target)
      (println (str "🔗 Creating Codeberg symlink: " codeberg-target " -> " source-dir))
      (println (str "   URL: " codeberg-path))
      
      ;; Create symlinks (using shell commands for proper symlinks)
      (let [github-cmd (str "ln -sf '" source-dir "' '" github-target "'")
            codeberg-cmd (str "ln -sf '" source-dir "' '" codeberg-target "'")]
        (println "")
        (println "📋 Commands to run:")
        (println (str "   " github-cmd))
        (println (str "   " codeberg-cmd))
        (println "")
        (println "🌐 Access URLs:")
        (println (str "   GitHub Pages: " github-path))
        (println (str "   Codeberg Pages: " codeberg-path))
        (println ""))
      
      (catch Exception e
        (println (str "❌ Error: " (.getMessage e))))))
  
  (println "✅ Symlink setup complete!")
  (println "")
  (println "📝 Next steps:")
  (println "   1. Run the symlink commands above")
  (println "   2. Deploy to GitHub Pages: gb flow")
  (println "   3. Deploy to Codeberg Pages: gb flow")
  (println "   4. Test URLs in browser")
  (println ""))

(println "🔧 Manual symlink creation:")
(println (str "   cd " symlinks-dir))
(println (str "   ln -sf '../personal/" latest-course-dir "' 'github-pages/" graintime "'"))
(println (str "   ln -sf '../personal/" latest-course-dir "' 'codeberg-pages/" graintime "'"))
