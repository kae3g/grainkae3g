#!/usr/bin/env bb

(ns manage-press-symlinks
  (:require [babashka.fs :as fs]
            [clojure.string :as str]
            [clojure.edn :as edn]))

;; =============================================================================
;; Press Article Symlink Management
;; =============================================================================

(def repos
  ["grainweb"
   "grainmusic" 
   "grainwriter"
   "grainspace"
   "graindroid"
   "grainlexicon"
   "grainneovedic"
   "grain-metatypes"
   "clojure-s6"
   "clojure-sixos"
   "clojure-icp"
   "clotoko"])

(def press-articles
  ["PRESS-RELEASE-GRAINPHONE-FUNDRAISING.md"
   "PRESS-INDEX.md"])

(defn create-press-directory [repo]
  (let [press-dir (str "grainstore/" repo "/press")]
    (when-not (fs/exists? press-dir)
      (fs/create-dirs press-dir)
      (println (str "✓ Created press directory for " repo)))))

(defn create-symlink [repo article]
  (let [source-path (str "grainstore/grainpbc/press/" article)
        target-path (str "grainstore/" repo "/press/" article)
        relative-path (str "../../grainpbc/press/" article)]
    
    (when (fs/exists? source-path)
      (if (fs/exists? target-path)
        (do
          (fs/delete-tree target-path)
          (println (str "✓ Removed existing symlink for " repo "/" article)))
        (println (str "✓ No existing symlink for " repo "/" article)))
      
      (try
        (fs/create-sym-link relative-path target-path)
        (println (str "✓ Created symlink: " repo "/" article))
        (catch Exception e
          (println (str "✗ Failed to create symlink for " repo "/" article ": " (.getMessage e))))))))

(defn create-readme [repo]
  (let [readme-content (str "# " (str/capitalize repo) " Press Articles

This directory contains symlinks to all Grain PBC press articles.

## 📰 Available Articles

- [Press Index](../../grainpbc/press/PRESS-INDEX.md)
- [Grainphone Fundraising](../../grainpbc/press/PRESS-RELEASE-GRAINPHONE-FUNDRAISING.md)

## 🔗 Cross-Platform Links

### GitHub Pages
- **Main Site**: https://grainpbc.github.io/
- **Press Kit**: https://grainpbc.github.io/press/

### Codeberg Pages  
- **Main Site**: https://grainpbc.codeberg.page/
- **Press Kit**: https://grainpbc.codeberg.page/press/

## 📝 About

All press articles are centrally managed in the `grainpbc` repository and symlinked here for easy access. This ensures consistent messaging across all Grain Network repositories.

**Last Updated**: " (java.time.LocalDate/now) "

---

*This directory is automatically managed by the Grain Network build system.*")
        
        readme-path (str "grainstore/" repo "/press/README.md")]
    
    (spit readme-path readme-content)
    (println (str "✓ Created README for " repo "/press"))))

(defn update-press-links [article-path]
  (let [content (slurp article-path)
        updated-content (str/replace content
                                    #"https://grainpbc\.github\.io/press/GR-PRESS-(\d+)"
                                    "https://grainpbc.github.io/press/GR-PRESS-$1")
        updated-content (str/replace updated-content
                                    #"https://grainpbc\.codeberg\.page/press/GR-PRESS-(\d+)"
                                    "https://grainpbc.codeberg.page/press/GR-PRESS-$1")]
    (when (not= content updated-content)
      (spit article-path updated-content)
      (println (str "✓ Updated links in " article-path)))))

(defn generate-cross-platform-links [article-id]
  (let [github-link (str "https://grainpbc.github.io/press/" article-id)
        codeberg-link (str "https://grainpbc.codeberg.page/press/" article-id)]
    {:github github-link
     :codeberg codeberg-link}))

(defn main []
  (println "🌾 Managing Grain PBC Press Article Symlinks")
  (println "=" 50)
  
  ;; Create press directories for all repos
  (println "\n📁 Creating press directories...")
  (doseq [repo repos]
    (create-press-directory repo))
  
  ;; Create symlinks for all articles
  (println "\n🔗 Creating symlinks...")
  (doseq [repo repos]
    (doseq [article press-articles]
      (create-symlink repo article)))
  
  ;; Create README files
  (println "\n📝 Creating README files...")
  (doseq [repo repos]
    (create-readme repo))
  
  ;; Update press article links
  (println "\n🔗 Updating press article links...")
  (doseq [article press-articles]
    (let [article-path (str "grainstore/grainpbc/press/" article)]
      (when (fs/exists? article-path)
        (update-press-links article-path))))
  
  ;; Generate cross-platform links
  (println "\n🌐 Generating cross-platform links...")
  (let [links (generate-cross-platform-links "GR-PRESS-001")]
    (println (str "GitHub Pages: " (:github links)))
    (println (str "Codeberg Pages: " (:codeberg links))))
  
  (println "\n✅ Press article symlink management complete!")
  (println "\n📊 Summary:")
  (println (str "- Repositories: " (count repos)))
  (println (str "- Press Articles: " (count press-articles)))
  (println (str "- Total Symlinks: " (* (count repos) (count press-articles)))))

(main)
