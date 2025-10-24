(ns kae3g.edn-wrapper
  "EDN file wrapper for 57-char hard wrap maintaining
   valid EDN while wrapping long lines"
  (:require [clojure.string :as str]
            [babashka.fs :as fs]
            [clojure.edn :as edn]))

(def max-line-length 57)

(defn wrap-long-line
  "Wrap a single long line at natural break points"
  [line]
  (if (<= (count line) max-line-length)
    [line]
    (let [;; Detect indentation
          leading-spaces (re-find #"^\s*" line)
          indent (count leading-spaces)
          ;; Try to find good break point
          break-chars [", " " " " :" " )" " ]" " }"]
          ;; Find last break point before limit
          break-idx (or
                     (some #(when-let [idx (str/last-index-of 
                                            (subs line 0 (min (count line) 
                                                             max-line-length))
                                            %)]
                              (when (> idx (+ indent 10)) idx))
                           break-chars)
                     ;; Hard break if no good spot found
                     (max (+ indent 20) (- max-line-length 5)))]
      (if (>= break-idx (count line))
        [line]
        (let [first-part (str/trimr (subs line 0 break-idx))
              rest-part (str/triml (subs line break-idx))
              ;; Preserve indentation for continuation
              cont-indent (apply str (repeat (+ indent 2) " "))]
          (concat [first-part]
                  (wrap-long-line (str cont-indent rest-part))))))))

(defn format-edn-with-wrap
  "Format EDN file with 57-char wrapping"
  [content]
  (let [lines (str/split-lines content)]
    (str/join "\n"
              (mapcat wrap-long-line lines))))

(defn wrap-edn-file
  "Wrap single EDN file to 57 chars"
  [filepath]
  (try
    (println "Wrapping EDN:" (fs/file-name filepath))
    (let [content (slurp filepath)
          ;; Verify it's valid EDN first
          _ (edn/read-string content)
          wrapped (format-edn-with-wrap content)]
      ;; Verify wrapped version is still valid
      (edn/read-string wrapped)
      ;; Write back
      (spit filepath wrapped)
      (println "Wrapped:" filepath))
    (catch Exception e
      (println "Skipping" filepath
               "- error:" (.getMessage e)))))

(defn find-edn-files
  "Find all EDN files in project"
  []
  (concat
   ;; bb.edn
   (when (fs/exists? "bb.edn") ["bb.edn"])
   ;; clj-kondo config
   (fs/glob ".clj-kondo" "**/*.edn")
   ;; Build artifacts
   (fs/glob "build" "**/*.edn")))

(defn -main
  "Wrap all EDN files to 57-char hard wrap"
  [& _args]
  (println "EDN Wrapper: 57-char hard wrap")
  (let [edn-files (find-edn-files)]
    (println "Found" (count edn-files) "EDN files")
    (doseq [file edn-files]
      (wrap-edn-file (str file)))
    (println "EDN wrapping complete")))
