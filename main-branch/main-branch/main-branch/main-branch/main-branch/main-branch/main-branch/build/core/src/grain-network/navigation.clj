(ns grain-network.navigation
  "Navigation system for Grain Network applications.
   
   Provides navigation utilities, breadcrumbs, and routing
   for desktop applications."
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(defn parse-path
  "Parse a navigation path into components."
  [path]
  (->> (str/split path #"/")
       (filter seq)
       (map keyword)))

(defn build-path
  "Build a navigation path from components."
  [components]
  (str/join "/" (map name components)))

(defn breadcrumbs
  "Generate breadcrumbs for a given path."
  [path]
  (let [components (parse-path path)]
    (map-indexed
      (fn [idx component]
        {:label (name component)
         :path (build-path (take (inc idx) components))
         :current? (= idx (dec (count components)))})
      components)))

(defn navigation-items
  "Generate navigation items for a content structure."
  [content-index]
  (->> content-index
       (group-by #(first (parse-path (:path %))))
       (map (fn [[category items]]
              {:category category
               :label (str/capitalize (name category))
               :items (map #(select-keys % [:path :metadata]) items)}))
       (sort-by :category)))

(defn find-content
  "Find content by path in content index."
  [content-index path]
  (first (filter #(= (:path %) path) content-index)))

(defn parent-content
  "Find parent content for a given path."
  [content-index path]
  (let [components (parse-path path)
        parent-path (build-path (drop-last components))]
    (when (seq parent-path)
      (find-content content-index parent-path))))

(defn child-content
  "Find child content for a given path."
  [content-index path]
  (let [path-str (str path "/")]
    (filter #(str/starts-with? (:path %) path-str)
            content-index)))

(defn navigation-tree
  "Build a navigation tree from content index."
  [content-index]
  (let [grouped (group-by #(first (str/split (:path %) #"/")) content-index)]
    (map (fn [[category items]]
           {:category category
            :label (str/capitalize category)
            :items (sort-by :path items)})
         grouped)))

(defn search-navigation
  "Search navigation items by query."
  [content-index query]
  (let [query-lower (str/lower-case query)]
    (filter (fn [item]
              (or (str/includes? (str/lower-case (:path item)) query-lower)
                  (str/includes? (str/lower-case (str (:metadata item))) query-lower)))
            content-index)))
