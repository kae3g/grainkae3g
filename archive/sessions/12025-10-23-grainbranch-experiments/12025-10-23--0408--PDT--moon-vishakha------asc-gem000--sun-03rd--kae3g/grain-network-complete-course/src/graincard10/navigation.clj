(ns graincard10.navigation
  "Graincard10 multi-page navigation system with colloquial title lookup"
  (:require [clojure.edn :as edn]
            [clojure.string :as str]))

(def page-index 
  "Load page index from EDN"
  (edn/read-string (slurp "data/page-index.edn")))

(defn normalize-title
  "Normalize title for fuzzy matching"
  [title]
  (-> title
      str/lower-case
      (str/replace #"[^a-z0-9\s]" "")
      str/trim))

(defn find-page-by-title
  "Find page number by exact or colloquial title"
  [search-term]
  (let [normalized-search (normalize-title search-term)
        title-map (get page-index :title-to-page)
        page-data (get page-index :page-index)]
    
    ;; Try exact title match first
    (or (get title-map search-term)
        
        ;; Try normalized title match
        (some (fn [[title page-num]]
                (when (= (normalize-title title) normalized-search)
                  page-num))
              title-map)
        
        ;; Try colloquial matches
        (some (fn [[page-num data]]
                (when (some #(str/includes? (normalize-title %) normalized-search)
                           (:colloquial data))
                  page-num))
              page-data)
        
        ;; No match found
        nil)))

(defn get-page-path
  "Get filepath for a page number"
  [page-num]
  (get-in page-index [:page-index page-num :filepath]))

(defn get-page-title
  "Get title for a page number"
  [page-num]
  (get-in page-index [:page-index page-num :title]))

(defn get-next-page
  "Get next page number (with wraparound)"
  [current-page]
  (let [all-pages (sort (keys (get page-index :page-index)))
        idx (.indexOf all-pages current-page)]
    (if (= idx (dec (count all-pages)))
      (first all-pages)  ; Wrap to beginning
      (nth all-pages (inc idx)))))

(defn get-prev-page
  "Get previous page number (with wraparound)"
  [current-page]
  (let [all-pages (sort (keys (get page-index :page-index)))
        idx (.indexOf all-pages current-page)]
    (if (zero? idx)
      (last all-pages)  ; Wrap to end
      (nth all-pages (dec idx)))))

(defn format-page-number
  "Format page number with leading zeros (0000-9999)"
  [n]
  (format "%04d" n))

(defn generate-nav-links
  "Generate navigation links for a page"
  [current-page]
  (let [prev (get-prev-page current-page)
        next (get-next-page current-page)
        prev-title (get-page-title prev)
        next-title (get-page-title next)]
    {:prev {:page prev
            :title prev-title
            :path (get-page-path prev)}
     :next {:page next
            :title next-title
            :path (get-page-path next)}
     :current {:page current-page
               :title (get-page-title current-page)
               :path (get-page-path current-page)}}))

(defn search-pages
  "Search for pages containing search term in title or colloquial names"
  [search-term]
  (let [normalized-search (normalize-title search-term)
        page-data (get page-index :page-index)]
    (filter (fn [[page-num data]]
              (or (str/includes? (normalize-title (:title data)) normalized-search)
                  (some #(str/includes? (normalize-title %) normalized-search)
                       (:colloquial data))))
            page-data)))

;; Example usage:
;; (find-page-by-title "88 counter") => 1
;; (find-page-by-title "motoko") => 20
;; (find-page-by-title "alpine intro") => 40
;; (generate-nav-links 1) => {:prev {...} :next {...} :current {...}}
