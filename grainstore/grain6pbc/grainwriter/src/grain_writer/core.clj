(ns grain-writer.core
  "Main writing interface and document management for Grainwriter"
  (:require [clojure.core.async :as async]
            [babashka.fs :as fs]
            [clojure.string :as str]
            [cheshire.core :as json])
  (:import [java.time Instant]
           [java.util UUID]))

;; ============================================================================
;; GLOBAL STATE (RAM-ONLY STORAGE)
;; ============================================================================

(defonce ^:private documents-db
  "In-memory document database. This is the ONLY storage - no disk persistence."
  (atom {}))

(defonce ^:private metadata-db
  "Metadata about all documents (timestamps, word counts, etc.)"
  (atom {}))

;; ============================================================================
;; UTILITY FUNCTIONS
;; ============================================================================

(defn generate-uuid []
  "Generate a unique document ID"
  (str (UUID/randomUUID)))

(defn now []
  "Get current timestamp"
  (str (Instant/now)))

(defn word-count [text]
  "Count words in text"
  (if (str/blank? text)
    0
    (count (str/split (str/trim text) #"\s+"))))

(defn character-count [text]
  "Count characters in text"
  (count text))

;; ============================================================================
;; DOCUMENT MANAGEMENT
;; ============================================================================

(defn create-document
  "Create a new document in RAM-only storage"
  ([title]
   (create-document title ""))
  ([title content]
   (let [doc-id (generate-uuid)
         timestamp (now)
         doc {:id doc-id
              :title title
              :content content
              :created-at timestamp
              :modified-at timestamp
              :format :markdown}
         metadata {:word-count (word-count content)
                   :char-count (character-count content)
                   :created-at timestamp
                   :modified-at timestamp}]
     (swap! documents-db assoc doc-id doc)
     (swap! metadata-db assoc doc-id metadata)
     doc)))

(defn get-document
  "Retrieve a document by ID"
  [doc-id]
  (get @documents-db doc-id))

(defn get-all-documents
  "Retrieve all documents from RAM"
  []
  (vals @documents-db))

(defn update-document
  "Update an existing document"
  [doc-id updates]
  (when-let [doc (get-document doc-id)]
    (let [updated-doc (merge doc
                            updates
                            {:modified-at (now)})
          new-content (:content updated-doc)]
      (swap! documents-db assoc doc-id updated-doc)
      (swap! metadata-db update doc-id merge
             {:word-count (word-count new-content)
              :char-count (character-count new-content)
              :modified-at (now)})
      updated-doc)))

(defn delete-document
  "Delete a document from RAM"
  [doc-id]
  (swap! documents-db dissoc doc-id)
  (swap! metadata-db dissoc doc-id)
  true)

(defn search-documents
  "Full-text search across all documents"
  [query]
  (let [query-lower (str/lower-case query)]
    (filter (fn [doc]
              (or (str/includes? (str/lower-case (:title doc)) query-lower)
                  (str/includes? (str/lower-case (:content doc)) query-lower)))
            (get-all-documents))))

;; ============================================================================
;; DOCUMENT STATISTICS
;; ============================================================================

(defn get-document-stats
  "Get statistics for a specific document"
  [doc-id]
  (get @metadata-db doc-id))

(defn get-all-stats
  "Get statistics for all documents"
  []
  (let [all-docs (get-all-documents)
        total-docs (count all-docs)
        total-words (reduce + (map #(word-count (:content %)) all-docs))
        total-chars (reduce + (map #(character-count (:content %)) all-docs))]
    {:total-documents total-docs
     :total-words total-words
     :total-characters total-chars
     :average-words-per-doc (if (pos? total-docs)
                              (/ total-words total-docs)
                              0)}))

;; ============================================================================
;; RAM MANAGEMENT
;; ============================================================================

(defn get-ram-usage
  "Get current RAM usage by documents (approximate)"
  []
  (let [all-docs (get-all-documents)
        total-content-size (reduce + (map #(count (:content %)) all-docs))
        total-title-size (reduce + (map #(count (:title %)) all-docs))
        total-size (+ total-content-size total-title-size)]
    {:total-bytes total-size
     :total-kb (/ total-size 1024.0)
     :total-mb (/ total-size (* 1024.0 1024.0))
     :document-count (count all-docs)}))

(defn compress-documents
  "Compress all documents in RAM (placeholder for future implementation)"
  []
  ;; TODO: Implement compression using Apache Commons Compress
  ;; For now, just return current documents
  (get-all-documents))

;; ============================================================================
;; EXPORT/IMPORT FOR SYNC
;; ============================================================================

(defn export-all-documents
  "Export all documents to a JSON-serializable format for USB sync"
  []
  {:documents (get-all-documents)
   :metadata @metadata-db
   :exported-at (now)
   :version "1.0.0"})

(defn import-documents
  "Import documents from USB sync (merge with existing)"
  [exported-data]
  (let [{:keys [documents metadata]} exported-data]
    ;; Merge documents (newer timestamps win)
    (doseq [doc documents]
      (let [doc-id (:id doc)
            existing-doc (get-document doc-id)]
        (if existing-doc
          ;; If exists, keep the newer version
          (when (> (compare (:modified-at doc) (:modified-at existing-doc)) 0)
            (swap! documents-db assoc doc-id doc)
            (swap! metadata-db assoc doc-id (get metadata doc-id)))
          ;; If doesn't exist, add it
          (do
            (swap! documents-db assoc doc-id doc)
            (swap! metadata-db assoc doc-id (get metadata doc-id))))))
    {:imported (count documents)
     :total (count (get-all-documents))}))

(defn clear-all-documents
  "Clear all documents from RAM (use with caution!)"
  []
  (reset! documents-db {})
  (reset! metadata-db {})
  {:cleared true})

;; ============================================================================
;; MARKDOWN UTILITIES
;; ============================================================================

(defn validate-markdown
  "Basic markdown validation (can be extended)"
  [content]
  {:valid true
   :warnings []})

(defn format-markdown
  "Format markdown content (basic formatting)"
  [content]
  ;; Basic formatting: ensure proper spacing, etc.
  (-> content
      str/trim
      (str/replace #"\n{3,}" "\n\n"))) ; Max 2 consecutive newlines

;; ============================================================================
;; PUBLIC API
;; ============================================================================

(defn init!
  "Initialize the Grainwriter system"
  []
  (println "Grainwriter initialized - RAM-only storage ready")
  (println "Current RAM usage:" (get-ram-usage))
  {:initialized true
   :storage-mode :ram-only
   :sync-mode :usb-manual})

(comment
  ;; REPL Usage Examples
  
  ;; Initialize
  (init!)
  
  ;; Create documents
  (def doc1 (create-document "My First Note" "This is a test note."))
  (def doc2 (create-document "Ideas" "- Idea 1\n- Idea 2\n- Idea 3"))
  
  ;; Get all documents
  (get-all-documents)
  
  ;; Update a document
  (update-document (:id doc1) {:content "Updated content!"})
  
  ;; Search
  (search-documents "test")
  
  ;; Get statistics
  (get-all-stats)
  (get-ram-usage)
  
  ;; Export for sync
  (def exported (export-all-documents))
  
  ;; Clear (careful!)
  ;; (clear-all-documents)
  )


