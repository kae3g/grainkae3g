(ns grain-network.content
  "Content management and parsing for Grain Network applications.
   
   Provides utilities for loading, parsing, and managing content
   from markdown files and EDN metadata."
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [babashka.fs :as fs]))

(defn load-edn
  "Load EDN content from file path."
  [path]
  (when (fs/exists? path)
    (edn/read-string (slurp path))))

(defn load-markdown
  "Load markdown content from file path."
  [path]
  (when (fs/exists? path)
    (slurp path)))

(defn parse-content-metadata
  "Parse frontmatter from markdown content."
  [content]
  (let [lines (str/split-lines content)
        [frontmatter content-lines] (if (and (seq lines)
                                              (= "---" (first lines)))
                                       (split-at (inc (.indexOf lines "---")) lines)
                                       [[] lines])]
    {:frontmatter (when (seq frontmatter)
                    (edn/read-string (str/join "\n" (drop 1 frontmatter))))
     :content (str/join "\n" content-lines)}))

(defn load-content-item
  "Load a complete content item with metadata and content."
  [base-path item-path]
  (let [full-path (str base-path "/" item-path)
        md-path (str full-path ".md")
        edn-path (str full-path ".edn")]
    (cond
      (fs/exists? md-path)
      (let [md-content (load-markdown md-path)
            edn-meta (load-edn edn-path)
            parsed (parse-content-metadata md-content)]
        {:path item-path
         :content (:content parsed)
         :metadata (merge edn-meta (:frontmatter parsed))})
      
      (fs/exists? edn-path)
      {:path item-path
       :content ""
       :metadata (load-edn edn-path)}
      
      :else
      nil)))

(defn list-content-items
  "List all content items in a directory."
  [base-path]
  (->> (fs/glob base-path "*.md")
       (map #(str/replace (str %) #"\.md$" ""))
       (map #(str/replace % base-path ""))
       (map #(str/replace % #"^/" ""))
       (filter seq)
       (sort)))

(defn build-content-index
  "Build an index of all content items."
  [base-path]
  (->> (list-content-items base-path)
       (map #(load-content-item base-path %))
       (filter some?)
       (sort-by :path)))

(defn search-content
  "Search content items by text or metadata."
  [content-index query]
  (let [query-lower (str/lower-case query)]
    (filter (fn [item]
              (or (str/includes? (str/lower-case (:content item)) query-lower)
                  (str/includes? (str/lower-case (str (:metadata item))) query-lower)))
            content-index)))
