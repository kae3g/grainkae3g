#!/usr/bin/env bb

(require '[babashka.fs :as fs]
         '[clojure.string :as str])

(defn extract-navigation-links [file-path]
  "Extract Next Writing and Previous Writing links from markdown file"
  (let [content (slurp file-path)
        lines (str/split-lines content)
        ;; Find lines with **Next Writing:** or **Previous Writing:**
        nav-lines (filter #(or (str/includes? % "**Next Writing:**")
                               (str/includes? % "**Previous Writing:**"))
                          lines)]
    (for [line nav-lines]
      (when-let [[_ link-text link-path] (re-find #"\[([^\]]+)\]\(([^\)]+)\)" line)]
        {:text link-text
         :path link-path
         :source-file (str (fs/file-name file-path))
         :line line}))))

(defn check-link-exists [source-file link-path]
  "Check if the linked file exists in writings/ directory"
  (let [;; Add .md if not present (GitHub Pages strips .md from URLs)
        file-path (if (str/ends-with? link-path ".md")
                    link-path
                    (str link-path ".md"))
        full-path (str "writings/" file-path)]
    (if (fs/exists? full-path)
      {:status :ok
       :source source-file
       :link link-path}
      {:status :error
       :source source-file
       :link link-path
       :message (str "File not found: " full-path)})))

(defn validate-navigation-links []
  (println "🔍 Testing Navigation Links in Writings\n")
  
  (let [writings-files (fs/glob "writings" "*.md")
        all-results (atom [])
        errors (atom [])]
    
    (doseq [file writings-files]
      (let [filename (fs/file-name file)
            links (remove nil? (extract-navigation-links (str file)))]
        
        (when (seq links)
          (println (str "📄 " filename ":"))
          
          (doseq [link links]
            (let [result (check-link-exists filename (:path link))]
              (swap! all-results conj result)
              (if (= :ok (:status result))
                (println (str "  ✅ " (:text link) " → " (:path link)))
                (do
                  (println (str "  ❌ " (:text link) " → " (:path link) " (NOT FOUND)"))
                  (swap! errors conj result)))))
          
          (println))))
    
    ;; Summary
    (println "\n" (str "=" (apply str (repeat 60 "="))))
    (println "📊 Summary:")
    (println (str "  Total links checked: " (count @all-results)))
    (println (str "  Valid links: " (count (filter #(= :ok (:status %)) @all-results))))
    (println (str "  Broken links: " (count @errors)))
    
    (when (seq @errors)
      (println "\n❌ Broken Links Found:")
      (doseq [error @errors]
        (println (str "  • " (:source error) " → " (:link error))))
      (println "\n⚠️  Please fix these broken links!")
      (System/exit 1))
    
    (println "\n✨ All navigation links are valid!")
    (System/exit 0)))

(defn main []
  (validate-navigation-links))

(when (= *file* (System/getProperty "babashka.file"))
  (main))

