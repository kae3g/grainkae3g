#!/usr/bin/env bb

(ns transform-links-github
  "Transform relative markdown links to absolute GitHub Pages URLs.
   
   This script processes all markdown files and converts relative links
   to absolute GitHub Pages URLs for proper cross-site navigation."
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [babashka.fs :as fs]))

;; =============================================================================
;; Configuration
;; =============================================================================

(def github-base-url "https://kae3g.github.io/grainkae3g")
(def grainpbc-base-url "https://grainpbc.github.io")

(def repo-mappings
  "Map of repository names to their GitHub Pages URLs"
  {"clojure-sixos" (str grainpbc-base-url "/clojure-sixos")
   "clojure-s6" (str grainpbc-base-url "/clojure-s6")
   "clojure-icp" (str grainpbc-base-url "/clojure-icp")
   "clotoko" (str grainpbc-base-url "/clotoko")
   "grain-metatypes" (str grainpbc-base-url "/grain-metatypes")
   "grainweb" (str grainpbc-base-url "/grainweb")
   "grainspace" (str grainpbc-base-url "/grainspace")
   "grainmusic" (str grainpbc-base-url "/grainmusic")
   "grainconv" (str grainpbc-base-url "/grainconv")
   "grainclay" (str grainpbc-base-url "/grainclay")
   "grainwriter" (str grainpbc-base-url "/grainwriter")
   "graincamera" (str grainpbc-base-url "/graincamera")
   "grainpack" (str grainpbc-base-url "/grainpack")
   "grainsource" (str grainpbc-base-url "/grainsource")
   "grainnetwork" (str grainpbc-base-url "/grainnetwork")
   "grainstore" (str grainpbc-base-url "/grainstore")})

;; =============================================================================
;; Link Transformation
;; =============================================================================

(defn relative-to-absolute-link
  "Convert a relative markdown link to an absolute GitHub Pages URL.
   
   Examples:
     ../grainstore/clotoko/README.md 
       -> https://grainpbc.github.io/clotoko/README.html
     
     docs/guides/setup.md
       -> https://kae3g.github.io/grainkae3g/docs/guides/setup.html"
  [link current-file-path]
  (cond
    ;; Already absolute
    (str/starts-with? link "http")
    link
    
    ;; Anchor link
    (str/starts-with? link "#")
    link
    
    ;; Cross-repo link (grainstore/repo-name/...)
    (re-find #"grainstore/([^/]+)/" link)
    (let [[_ repo-name] (re-find #"grainstore/([^/]+)/" link)
          repo-url (get repo-mappings repo-name)
          relative-path (str/replace link #".*?grainstore/[^/]+/" "")]
      (if repo-url
        (str repo-url "/" (str/replace relative-path #"\.md$" ".html"))
        link))
    
    ;; Local link within grainkae3g
    :else
    (let [clean-link (str/replace link #"\.md$" ".html")]
      (str github-base-url "/" clean-link))))

(defn transform-markdown-links
  "Transform all markdown links in content to absolute GitHub Pages URLs."
  [content file-path]
  (str/replace content
               #"\[([^\]]+)\]\(([^)]+)\)"
               (fn [[full-match link-text link-url]]
                 (let [new-url (relative-to-absolute-link link-url file-path)]
                   (str "[" link-text "](" new-url ")")))))

(defn transform-wikilinks
  "Transform wikilinks [[page]] to absolute URLs.
   
   [[clotoko-overview]] -> https://grainpbc.github.io/clotoko/clotoko-overview.html"
  [content]
  (str/replace content
               #"\[\[([^\]]+)\]\]"
               (fn [[_ page-id]]
                 ;; Try to find which repo this page belongs to
                 ;; For now, assume it's in the current context
                 (str "[" page-id "](" github-base-url "/docs/" page-id ".html)"))))

;; =============================================================================
;; File Processing
;; =============================================================================

(defn process-markdown-file
  "Process a single markdown file, transforming links to GitHub Pages URLs."
  [file-path output-path]
  (let [content (slurp file-path)
        transformed (-> content
                       (transform-markdown-links file-path)
                       (transform-wikilinks))]
    (io/make-parents output-path)
    (spit output-path transformed)
    (println (format "✓ Transformed: %s -> %s" file-path output-path))))

(defn find-markdown-files
  "Find all markdown files in a directory."
  [dir]
  (->> (fs/glob dir "**/*.md")
       (map str)
       (remove #(str/includes? % "node_modules"))
       (remove #(str/includes? % ".git"))))

;; =============================================================================
;; Main Script
;; =============================================================================

(defn -main []
  (println "╔═══════════════════════════════════════════════════════════════╗")
  (println "║     Transform Links for GitHub Pages Deployment              ║")
  (println "╚═══════════════════════════════════════════════════════════════╝\n")
  
  (let [source-dir "."
        output-dir "dist/github"
        md-files (find-markdown-files source-dir)]
    
    (println (format "Found %d markdown files to process\n" (count md-files)))
    
    ;; Create output directory
    (fs/create-dirs output-dir)
    
    ;; Process each file
    (doseq [file md-files]
      (let [relative-path (str/replace file (str source-dir "/") "")
            output-path (str output-dir "/" relative-path)]
        (process-markdown-file file output-path)))
    
    (println (format "\n✅ Transformed %d files for GitHub Pages deployment!" (count md-files)))
    (println (format "📁 Output directory: %s" output-dir))
    (println (format "🌐 Base URL: %s" github-base-url))))

(-main)

