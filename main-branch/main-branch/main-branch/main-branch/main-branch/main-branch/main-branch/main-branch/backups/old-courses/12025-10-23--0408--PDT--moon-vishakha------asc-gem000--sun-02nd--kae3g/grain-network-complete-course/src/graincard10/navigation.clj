(ns graincard10.navigation
  "Graincard10 multi-page course navigation system"
  (:require [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(defn load-page-index
  "Load the page index from EDN file"
  []
  (-> "data/page-index.edn"
      io/resource
      slurp
      edn/read-string))

(defn resolve-page
  "Resolve a colloquial title or page number to file path"
  [page-ref page-index]
  (let [page-ref (str/lower-case (str page-ref))]
    (cond
      ;; Direct page number lookup
      (re-matches #"\d{4}" page-ref)
      (get-in page-index [:page-index page-ref :file])
      
      ;; Colloquial title lookup
      :else
      (->> (:page-index page-index)
           (filter (fn [[_ page-data]]
                     (some #(= page-ref (str/lower-case %))
                           (:colloquial page-data))))
           first
           second
           :file))))

(defn get-page-info
  "Get page information by reference"
  [page-ref page-index]
  (let [page-ref (str/lower-case (str page-ref))]
    (cond
      ;; Direct page number lookup
      (re-matches #"\d{4}" page-ref)
      (get-in page-index [:page-index page-ref])
      
      ;; Colloquial title lookup
      :else
      (->> (:page-index page-index)
           (filter (fn [[_ page-data]]
                     (some #(= page-ref (str/lower-case %))
                           (:colloquial page-data))))
           first
           second))))

(defn get-next-page
  "Get the next page number"
  [current-page page-index]
  (let [current-num (Integer/parseInt current-page)
        next-num (inc current-num)
        next-page (format "%04d" next-num)]
    (if (contains? (:page-index page-index) next-page)
      next-page
      "0000"))) ; Loop back to title

(defn get-prev-page
  "Get the previous page number"
  [current-page page-index]
  (let [current-num (Integer/parseInt current-page)
        prev-num (dec current-num)]
    (if (pos? prev-num)
      (format "%04d" prev-num)
      "0099"))) ; Loop to index

(defn generate-nav-links
  "Generate navigation links for a page"
  [current-page page-index]
  (let [next-page (get-next-page current-page page-index)
        prev-page (get-prev-page current-page page-index)
        current-info (get-page-info current-page page-index)]
    {:current current-page
     :next next-page
     :prev prev-page
     :title (:title current-info)
     :subtitle (:subtitle current-info)}))

(defn search-pages
  "Search pages by title or content"
  [query page-index]
  (let [query (str/lower-case query)]
    (->> (:page-index page-index)
         (filter (fn [[_ page-data]]
                   (or (str/includes? (str/lower-case (:title page-data)) query)
                       (str/includes? (str/lower-case (:subtitle page-data)) query)
                       (some #(str/includes? (str/lower-case %) query)
                             (:colloquial page-data)))))
         (map (fn [[page-num page-data]]
                {:page page-num
                 :title (:title page-data)
                 :subtitle (:subtitle page-data)
                 :file (:file page-data)})))))

;; Example usage:
(comment
  (def page-index (load-page-index))
  (resolve-page "0001" page-index)
  (resolve-page "philosophy" page-index)
  (get-page-info "0001" page-index)
  (generate-nav-links "0001" page-index)
  (search-pages "88" page-index))
