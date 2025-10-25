(ns grainwriter.app
  "Grainwriter - Native Clojure Humble UI app for e-ink writing device.
   Uses EDN for data storage and clojure-s6/clojure-sixos for system integration."
  (:require
   [clojure.spec.alpha :as s]
   [clojure.core.async :as async]
   [clojure.data.json :as json]
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.string :as str]
   [clojure.tools.logging :as log]
   [clojure-s6.core :as s6]
   [clojure-s6.grainclay :as grainclay]
   [clojure-sixos.core :as sixos]
   [humble.ui :as ui]
   [clotoko.sur.grainframe :as grainframe]
   [clotoko.sur.clay :as clay]
   [clotoko.sur.mark :as mark]
   [grainwriter.display :as display]
   [grainwriter.storage :as storage]
   [grainwriter.sync :as sync]
   [grainwriter.input :as input]
   [grainwriter.power :as power]))

;; =============================================================================
;; Display Configuration (80x110 Paper Ratio)
;; =============================================================================

(defonce display-config
  {:base-width 80        ; 80 characters wide
   :base-height 110      ; 8x11 paper ratio (80 * 1.375)
   :char-width 8         ; pixels per character
   :char-height 12       ; pixels per character
   :line-spacing 2       ; pixels between lines
   :margin 16            ; pixels margin
   :screen-width (* 80 8)   ; 640 pixels
   :screen-height (* 110 12) ; 1320 pixels
   :paper-ratio 1.375    ; 11/8 ratio
   :terminal-width 80    ; standard terminal width
   :lines-per-page 110   ; lines per "page"})

;; =============================================================================
;; Application State
;; =============================================================================

(defonce app-state
  (atom
   {:status :initializing
    :current-document nil
    :documents {}
    :display-mode :normal
    :power-mode :normal
    :input-mode :keyboard
    :sync-status :disconnected
    :battery-level 100
    :storage-usage 0
    :last-save (System/currentTimeMillis)
    :current-page 1
    :total-pages 1
    :cursor-position {:line 1 :column 1}
    :ui-state {:current-view :documents
               :selected-document nil
               :search-query ""
               :filter-mode :all
               :zoom-level 1.0
               :show-line-numbers true
               :word-wrap true}}))

;; =============================================================================
;; Core Application Functions
;; =============================================================================

(defn init-app
  "Initialize the Grainwriter application."
  []
  (log/info "Initializing Grainwriter application...")
  (try
    ;; Initialize display
    (display/init-display)
    
    ;; Initialize storage
    (storage/init-storage)
    
    ;; Initialize sync
    (sync/init-sync)
    
    ;; Initialize input handling
    (input/init-input)
    
    ;; Initialize power management
    (power/init-power)
    
    ;; Load documents
    (load-documents)
    
    ;; Start background services
    (start-background-services)
    
    (swap! app-state assoc :status :running)
    (log/info "Grainwriter application initialized successfully")
    true
    (catch Exception e
      (log/error e "Failed to initialize Grainwriter application")
      (swap! app-state assoc :status :error)
      false)))

(defn start-app
  "Start the Grainwriter application with Humble UI."
  []
  (log/info "Starting Grainwriter application...")
  (when (init-app)
    (ui/run
     (main-ui)
     {:title "Grainwriter"
      :width 1200
      :height 800
      :resizable false
      :fullscreen true})))

(defn stop-app
  "Stop the Grainwriter application."
  []
  (log/info "Stopping Grainwriter application...")
  (try
    ;; Save current document
    (when-let [doc (:current-document @app-state)]
      (save-document doc))
    
    ;; Stop background services
    (stop-background-services)
    
    ;; Cleanup display
    (display/cleanup-display)
    
    (swap! app-state assoc :status :stopped)
    (log/info "Grainwriter application stopped")
    true
    (catch Exception e
      (log/error e "Failed to stop Grainwriter application")
      false)))

;; =============================================================================
;; Document Management
;; =============================================================================

(defn create-document
  "Create a new document."
  [title content]
  (let [doc-id (str (java.util.UUID/randomUUID))
        document {:id doc-id
                  :title title
                  :content content
                  :created-at (System/currentTimeMillis)
                  :modified-at (System/currentTimeMillis)
                  :word-count (count (str/split content #"\s+"))
                  :char-count (count content)}]
    (swap! app-state update :documents assoc doc-id document)
    (swap! app-state assoc :current-document doc-id)
    (save-document document)
    document))

(defn save-document
  "Save a document to storage."
  [document]
  (try
    (let [doc-id (:id document)
          doc-path (str "documents/" doc-id ".edn")]
      (storage/save-edn doc-path document)
      (swap! app-state assoc :last-save (System/currentTimeMillis))
      (log/debug "Saved document:" doc-id)
      true)
    (catch Exception e
      (log/error e "Failed to save document:" (:id document))
      false)))

(defn load-document
  "Load a document from storage."
  [doc-id]
  (try
    (let [doc-path (str "documents/" doc-id ".edn")
          document (storage/load-edn doc-path)]
      (swap! app-state update :documents assoc doc-id document)
      document)
    (catch Exception e
      (log/error e "Failed to load document:" doc-id)
      nil)))

(defn load-documents
  "Load all documents from storage."
  []
  (try
    (let [doc-files (storage/list-files "documents/")
          documents (->> doc-files
                         (filter #(str/ends-with? % ".edn"))
                         (map #(str/replace % #"\.edn$" ""))
                         (map load-document)
                         (filter some?)
                         (reduce #(assoc %1 (:id %2) %2) {}))]
      (swap! app-state assoc :documents documents)
      (log/info "Loaded" (count documents) "documents")
      true)
    (catch Exception e
      (log/error e "Failed to load documents")
      false)))

(defn delete-document
  "Delete a document."
  [doc-id]
  (try
    (let [doc-path (str "documents/" doc-id ".edn")]
      (storage/delete-file doc-path)
      (swap! app-state update :documents dissoc doc-id)
      (when (= (:current-document @app-state) doc-id)
        (swap! app-state assoc :current-document nil))
      (log/info "Deleted document:" doc-id)
      true)
    (catch Exception e
      (log/error e "Failed to delete document:" doc-id)
      false)))

(defn search-documents
  "Search documents by title or content."
  [query]
  (let [documents (vals (:documents @app-state))
        query-lower (str/lower-case query)]
    (filter #(or (str/includes? (str/lower-case (:title %)) query-lower)
                 (str/includes? (str/lower-case (:content %)) query-lower))
            documents)))

;; =============================================================================
;; Display Management (80x110 Paper-Based)
;; =============================================================================

(defn set-display-mode
  "Set the display mode (normal, high-contrast, monochrome)."
  [mode]
  (swap! app-state assoc :display-mode mode)
  (display/set-mode mode)
  (log/debug "Display mode set to:" mode))

(defn refresh-display
  "Refresh the e-ink display."
  []
  (display/refresh)
  (log/debug "Display refreshed"))

(defn clear-display
  "Clear the e-ink display."
  []
  (display/clear)
  (log/debug "Display cleared"))

(defn get-screen-dimensions
  "Get current screen dimensions based on 80x110 paper ratio."
  []
  (let [zoom (:zoom-level (:ui-state @app-state))
        base-width (:base-width display-config)
        base-height (:base-height display-config)]
    {:width (int (* base-width zoom))
     :height (int (* base-height zoom))
     :char-width (int (* (:char-width display-config) zoom))
     :char-height (int (* (:char-height display-config) zoom))
     :lines-per-page (:lines-per-page display-config)
     :columns-per-line base-width}))

(defn calculate-pages
  "Calculate total pages for a document based on 80x110 format."
  [content]
  (let [lines (str/split-lines content)
        lines-per-page (:lines-per-page display-config)
        total-lines (count lines)]
    (max 1 (int (Math/ceil (/ total-lines lines-per-page))))))

(defn get-page-content
  "Get content for a specific page (1-indexed)."
  [content page-number]
  (let [lines (str/split-lines content)
        lines-per-page (:lines-per-page display-config)
        start-line (* (dec page-number) lines-per-page)
        end-line (min (+ start-line lines-per-page) (count lines))
        page-lines (subvec (vec lines) start-line end-line)]
    (str/join "\n" page-lines)))

(defn format-content-for-display
  "Format content for 80-character wide display with word wrap."
  [content]
  (let [max-width (:base-width display-config)
        lines (str/split-lines content)
        wrapped-lines (mapcat #(wrap-line % max-width) lines)]
    (str/join "\n" wrapped-lines)))

(defn wrap-line
  "Wrap a line to fit within max-width characters."
  [line max-width]
  (if (<= (count line) max-width)
    [line]
    (let [words (str/split line #"\s+")
          wrapped (wrap-words words max-width)]
      (map #(str/join " " %) wrapped))))

(defn wrap-words
  "Wrap words to fit within max-width."
  [words max-width]
  (loop [words words
         current-line []
         result []]
    (if (empty? words)
      (if (seq current-line)
        (conj result current-line)
        result)
      (let [word (first words)
            line-with-word (conj current-line word)
            line-length (count (str/join " " line-with-word))]
        (if (<= line-length max-width)
          (recur (rest words) line-with-word result)
          (if (empty? current-line)
            ;; Single word longer than max-width, truncate
            (recur (rest words) [] (conj result [word]))
            ;; Start new line
            (recur words [] (conj result current-line))))))))

(defn get-cursor-position
  "Get current cursor position in line/column format."
  []
  (:cursor-position @app-state))

(defn set-cursor-position
  "Set cursor position."
  [line column]
  (let [max-lines (:lines-per-page display-config)
        max-columns (:base-width display-config)
        clamped-line (max 1 (min line max-lines))
        clamped-column (max 1 (min column max-columns))]
    (swap! app-state assoc :cursor-position {:line clamped-line :column clamped-column})))

(defn move-cursor
  "Move cursor by relative amount."
  [delta-line delta-column]
  (let [current (:cursor-position @app-state)
        new-line (+ (:line current) delta-line)
        new-column (+ (:column current) delta-column)]
    (set-cursor-position new-line new-column)))

(defn get-current-page-info
  "Get current page information."
  []
  (let [current-page (:current-page @app-state)
        total-pages (:total-pages @app-state)
        cursor (:cursor-position @app-state)]
    {:current-page current-page
     :total-pages total-pages
     :cursor-line (:line cursor)
     :cursor-column (:column cursor)
     :lines-per-page (:lines-per-page display-config)
     :columns-per-line (:base-width display-config)}))

(defn next-page
  "Go to next page."
  []
  (let [current (:current-page @app-state)
        total (:total-pages @app-state)]
    (when (< current total)
      (swap! app-state assoc :current-page (inc current))
      (set-cursor-position 1 1))))

(defn previous-page
  "Go to previous page."
  []
  (let [current (:current-page @app-state)]
    (when (> current 1)
      (swap! app-state assoc :current-page (dec current))
      (set-cursor-position 1 1))))

(defn go-to-page
  "Go to specific page."
  [page-number]
  (let [total (:total-pages @app-state)
        clamped-page (max 1 (min page-number total))]
    (swap! app-state assoc :current-page clamped-page)
    (set-cursor-position 1 1)))

(defn update-page-count
  "Update total page count for current document."
  []
  (when-let [doc-id (:current-document @app-state)]
    (let [document (get-in @app-state [:documents doc-id])
          content (:content document)
          total-pages (calculate-pages content)]
      (swap! app-state assoc :total-pages total-pages)
      (when (> (:current-page @app-state) total-pages)
        (swap! app-state assoc :current-page total-pages)))))

(defn set-zoom-level
  "Set zoom level for display."
  [zoom]
  (let [clamped-zoom (max 0.5 (min zoom 3.0))]
    (swap! app-state update-in [:ui-state :zoom-level] (constantly clamped-zoom))
    (log/debug "Zoom level set to:" clamped-zoom)))

(defn toggle-line-numbers
  "Toggle line number display."
  []
  (swap! app-state update-in [:ui-state :show-line-numbers] not)
  (log/debug "Line numbers:" (if (:show-line-numbers (:ui-state @app-state)) "on" "off")))

(defn toggle-word-wrap
  "Toggle word wrap."
  []
  (swap! app-state update-in [:ui-state :word-wrap] not)
  (log/debug "Word wrap:" (if (:word-wrap (:ui-state @app-state)) "on" "off")))

;; =============================================================================
;; Power Management
;; =============================================================================

(defn set-power-mode
  "Set the power mode (normal, low-power, sleep)."
  [mode]
  (swap! app-state assoc :power-mode mode)
  (power/set-mode mode)
  (log/debug "Power mode set to:" mode))

(defn get-battery-level
  "Get current battery level."
  []
  (let [level (power/get-battery-level)]
    (swap! app-state assoc :battery-level level)
    level))

(defn is-charging?
  "Check if device is charging."
  []
  (power/is-charging?))

;; =============================================================================
;; Sync Management
;; =============================================================================

(defn start-sync
  "Start synchronization with external devices."
  []
  (swap! app-state assoc :sync-status :connecting)
  (async/go
    (try
      (sync/start-sync)
      (swap! app-state assoc :sync-status :connected)
      (log/info "Sync started successfully")
      (catch Exception e
        (log/error e "Failed to start sync")
        (swap! app-state assoc :sync-status :error))))

(defn stop-sync
  "Stop synchronization."
  []
  (swap! app-state assoc :sync-status :disconnecting)
  (async/go
    (try
      (sync/stop-sync)
      (swap! app-state assoc :sync-status :disconnected)
      (log/info "Sync stopped")
      (catch Exception e
        (log/error e "Failed to stop sync")
        (swap! app-state assoc :sync-status :error))))

(defn sync-documents
  "Sync all documents to external device."
  []
  (async/go
    (try
      (let [documents (vals (:documents @app-state))]
        (doseq [doc documents]
          (sync/sync-document doc))
        (log/info "Documents synced successfully"))
      (catch Exception e
        (log/error e "Failed to sync documents")))))

;; =============================================================================
;; Background Services
;; =============================================================================

(defn start-background-services
  "Start background services using clojure-s6."
  []
  (log/info "Starting background services...")
  
  ;; Start auto-save service
  (s6/start-service "grainwriter-autosave"
                   {:exec "clojure -M:grainwriter-autosave"
                    :env {"GRAINWRITER_MODE" "autosave"}})
  
  ;; Start power monitoring service
  (s6/start-service "grainwriter-power"
                   {:exec "clojure -M:grainwriter-power"
                    :env {"GRAINWRITER_MODE" "power"}})
  
  ;; Start sync service
  (s6/start-service "grainwriter-sync"
                   {:exec "clojure -M:grainwriter-sync"
                    :env {"GRAINWRITER_MODE" "sync"}})
  
  (log/info "Background services started"))

(defn stop-background-services
  "Stop background services."
  []
  (log/info "Stopping background services...")
  
  (s6/stop-service "grainwriter-autosave")
  (s6/stop-service "grainwriter-power")
  (s6/stop-service "grainwriter-sync")
  
  (log/info "Background services stopped"))

;; =============================================================================
;; User Interface
;; =============================================================================

(defn main-ui
  "Main user interface component."
  []
  (ui/column
   {:style {:padding 20
            :background-color 0x2D2D2D
            :color 0xFFFFFF}}
   
   ;; Header
   (header-ui)
   
   ;; Main content area
   (ui/row
    {:style {:flex 1
             :margin-top 20}}
    
    ;; Sidebar
    (sidebar-ui)
    
    ;; Main content
    (main-content-ui))))

(defn header-ui
  "Header component."
  []
  (ui/row
   {:style {:justify-content :space-between
            :align-items :center
            :padding 10
            :background-color 0x1A1A1A
            :border-radius 8}}
   
   ;; Title
   (ui/label "Grainwriter")
   
   ;; Status indicators
   (ui/row
    {:style {:gap 10}}
    
    ;; Battery level
    (ui/label (str "Battery: " (:battery-level @app-state) "%"))
    
    ;; Sync status
    (ui/label (str "Sync: " (:sync-status @app-state)))
    
    ;; Storage usage
    (ui/label (str "Storage: " (:storage-usage @app-state) "%")))))

(defn sidebar-ui
  "Sidebar component."
  []
  (ui/column
   {:style {:width 300
            :background-color 0x1A1A1A
            :border-radius 8
            :padding 10
            :margin-right 10}}
   
   ;; Search
   (ui/text-field
    {:value (:search-query (:ui-state @app-state))
     :on-change #(swap! app-state update-in [:ui-state :search-query] (constantly %))
     :placeholder "Search documents..."})
   
   ;; Document list
   (ui/scroll
    (ui/column
     {:style {:gap 5}}
     (for [doc (vals (:documents @app-state))]
       (document-item-ui doc)))))

(defn document-item-ui
  "Document item component."
  [document]
  (ui/button
   {:text (:title document)
    :on-click #(select-document (:id document))
    :style {:background-color (if (= (:id document) (:selected-document (:ui-state @app-state)))
                                0x4A4A4A
                                0x2D2D2D)
            :color 0xFFFFFF
            :border-radius 4
            :padding 8}}))

(defn main-content-ui
  "Main content component."
  []
  (ui/column
   {:style {:flex 1
            :background-color 0x1A1A1A
            :border-radius 8
            :padding 10}}
   
   (if-let [doc-id (:current-document @app-state)]
     (document-editor-ui doc-id)
     (welcome-ui))))

(defn document-editor-ui
  "Document editor component."
  [doc-id]
  (let [document (get-in @app-state [:documents doc-id])]
    (ui/column
     {:style {:flex 1}}
     
     ;; Document title
     (ui/text-field
      {:value (:title document)
       :on-change #(update-document-title doc-id %)
       :style {:font-size 18
               :font-weight :bold
               :background-color 0x2D2D2D
               :color 0xFFFFFF
               :border-radius 4
               :padding 8}})
     
     ;; Document content
     (ui/text-area
      {:value (:content document)
       :on-change #(update-document-content doc-id %)
       :style {:flex 1
               :background-color 0x2D2D2D
               :color 0xFFFFFF
               :border-radius 4
               :padding 8
               :margin-top 10
               :font-family "monospace"}})
     
     ;; Document stats
     (ui/row
      {:style {:justify-content :space-between
               :margin-top 10}}
      
      (ui/label (str "Words: " (:word-count document)))
      (ui/label (str "Characters: " (:char-count document)))
      (ui/label (str "Modified: " (format-date (:modified-at document))))))))

(defn welcome-ui
  "Welcome screen component."
  []
  (ui/column
   {:style {:flex 1
            :justify-content :center
            :align-items :center}}
   
   (ui/label
    {:text "Welcome to Grainwriter"
     :style {:font-size 24
             :font-weight :bold
             :margin-bottom 20}})
   
   (ui/button
    {:text "Create New Document"
     :on-click #(create-new-document)
     :style {:background-color 0x4A4A4A
             :color 0xFFFFFF
             :border-radius 4
             :padding 12}})))

;; =============================================================================
;; Event Handlers
;; =============================================================================

(defn select-document
  "Select a document for editing."
  [doc-id]
  (swap! app-state assoc :current-document doc-id)
  (swap! app-state update-in [:ui-state :selected-document] (constantly doc-id)))

(defn create-new-document
  "Create a new document."
  []
  (let [title (str "Document " (inc (count (:documents @app-state))))
        content ""]
    (create-document title content)))

(defn update-document-title
  "Update document title."
  [doc-id title]
  (swap! app-state update-in [:documents doc-id :title] (constantly title))
  (swap! app-state update-in [:documents doc-id :modified-at] (constantly (System/currentTimeMillis))))

(defn update-document-content
  "Update document content."
  [doc-id content]
  (let [word-count (count (str/split content #"\s+"))
        char-count (count content)]
    (swap! app-state update-in [:documents doc-id] merge
           {:content content
            :word-count word-count
            :char-count char-count
            :modified-at (System/currentTimeMillis)})))

;; =============================================================================
;; Utility Functions
;; =============================================================================

(defn format-date
  "Format timestamp as readable date."
  [timestamp]
  (let [date (java.util.Date. timestamp)
        formatter (java.text.SimpleDateFormat. "yyyy-MM-dd HH:mm")]
    (.format formatter date)))

(defn get-app-status
  "Get current application status."
  []
  {:status (:status @app-state)
   :document-count (count (:documents @app-state))
   :current-document (:current-document @app-state)
   :battery-level (:battery-level @app-state)
   :sync-status (:sync-status @app-state)
   :storage-usage (:storage-usage @app-state)})

;; =============================================================================
;; Main Entry Point
;; =============================================================================

(defn -main
  "Main entry point for Grainwriter application."
  [& args]
  (log/info "Starting Grainwriter application...")
  (start-app))
