(ns grain-writer.display
  "E-ink display control and optimization for Inkplate 10"
  (:require [clojure.string :as str]
            [clojure.java.shell :as shell]))

;; ============================================================================
;; E-INK DISPLAY CONSTANTS
;; ============================================================================

(def display-width 1872)
(def display-height 1404)
(def display-dpi 227)
(def grayscale-levels 16)

;; Refresh modes
(def refresh-mode-full :full)    ; 1.2s, complete screen clear
(def refresh-mode-partial :partial) ; 0.3s, faster but with ghosting

;; ============================================================================
;; DISPLAY STATE
;; ============================================================================

(defonce ^:private display-state
  "Current state of the e-ink display"
  (atom {:initialized false
         :current-document nil
         :refresh-count 0
         :last-full-refresh nil
         :partial-refresh-limit 10})) ; Full refresh every N partial refreshes

;; ============================================================================
;; NATIVE INTERFACE (Placeholder for JNI/FFI)
;; ============================================================================

(defn- native-init-display []
  "Initialize e-ink display hardware (native call placeholder)"
  ;; TODO: Implement JNI call to Inkplate library
  ;; For now, simulate with shell command or direct GPIO
  (println "Initializing Inkplate 10 display...")
  {:success true
   :width display-width
   :height display-height})

(defn- native-refresh-display [mode]
  "Refresh e-ink display with specified mode (native call placeholder)"
  ;; TODO: Implement JNI call to Inkplate refresh function
  (println (str "Refreshing display in " (name mode) " mode..."))
  {:success true
   :refresh-time-ms (if (= mode :full) 1200 300)})

(defn- native-render-text [text x y font-size]
  "Render text to display buffer (native call placeholder)"
  ;; TODO: Implement JNI call to Inkplate text rendering
  (println (str "Rendering text at (" x "," y ") size " font-size))
  {:success true})

(defn- native-clear-display []
  "Clear display buffer (native call placeholder)"
  ;; TODO: Implement JNI call to Inkplate clear function
  (println "Clearing display buffer...")
  {:success true})

;; ============================================================================
;; DISPLAY INITIALIZATION
;; ============================================================================

(defn init-display! []
  "Initialize the e-ink display system"
  (let [result (native-init-display)]
    (swap! display-state assoc
           :initialized true
           :last-full-refresh (System/currentTimeMillis))
    (println "E-ink display initialized successfully")
    result))

;; ============================================================================
;; REFRESH MANAGEMENT
;; ============================================================================

(defn should-full-refresh? []
  "Determine if we should do a full refresh to avoid ghosting"
  (let [{:keys [refresh-count partial-refresh-limit]} @display-state]
    (>= refresh-count partial-refresh-limit)))

(defn refresh-display
  "Refresh the e-ink display with optimal strategy"
  ([]
   (refresh-display (if (should-full-refresh?) :full :partial)))
  ([mode]
   (let [result (native-refresh-display mode)]
     (if (= mode :full)
       (swap! display-state assoc
              :refresh-count 0
              :last-full-refresh (System/currentTimeMillis))
       (swap! display-state update :refresh-count inc))
     result)))

(defn force-full-refresh! []
  "Force a full display refresh to clear ghosting"
  (refresh-display :full))

;; ============================================================================
;; TEXT RENDERING
;; ============================================================================

(defn calculate-line-height [font-size]
  "Calculate line height based on font size"
  (* font-size 1.5))

(defn wrap-text [text width font-size]
  "Wrap text to fit within specified width"
  ;; Rough estimation: average char width is ~0.6 * font-size
  (let [chars-per-line (int (/ width (* font-size 0.6)))
        lines (str/split text #"\n")]
    (mapcat (fn [line]
              (if (<= (count line) chars-per-line)
                [line]
                (partition-all chars-per-line line)))
            lines)))

(defn render-text-block
  "Render a block of text to the display"
  [text {:keys [x y font-size max-width] :or {x 50 y 50 font-size 24 max-width (- display-width 100)}}]
  (let [wrapped-lines (wrap-text text max-width font-size)
        line-height (calculate-line-height font-size)]
    (doseq [[idx line] (map-indexed vector wrapped-lines)]
      (native-render-text (apply str line)
                          x
                          (+ y (* idx line-height))
                          font-size))))

;; ============================================================================
;; DOCUMENT RENDERING
;; ============================================================================

(defn render-document-header
  "Render document title and metadata"
  [doc]
  (let [title (:title doc "Untitled")
        modified (:modified-at doc)]
    (native-render-text title 50 50 32)
    (when modified
      (native-render-text (str "Last modified: " modified) 50 90 14))))

(defn render-document-content
  "Render main document content"
  [doc]
  (let [content (:content doc "")
        content-y 150]
    (render-text-block content {:x 50
                                 :y content-y
                                 :font-size 20
                                 :max-width (- display-width 100)})))

(defn render-document-footer
  "Render document statistics in footer"
  [doc word-count char-count]
  (let [footer-y (- display-height 50)
        footer-text (str "Words: " word-count " | Characters: " char-count)]
    (native-render-text footer-text 50 footer-y 14)))

(defn render-document
  "Render a complete document to the e-ink display"
  [doc]
  (native-clear-display)
  (render-document-header doc)
  (render-document-content doc)
  (render-document-footer doc
                          (get-in doc [:word-count] 0)
                          (get-in doc [:char-count] 0))
  (refresh-display)
  (swap! display-state assoc :current-document doc)
  {:success true
   :document-id (:id doc)})

;; ============================================================================
;; PARTIAL UPDATES (For editing)
;; ============================================================================

(defn render-cursor
  "Render cursor at specified position (for editing mode)"
  [x y]
  ;; TODO: Implement cursor rendering
  (native-render-text "|" x y 20))

(defn update-line
  "Update a single line of text (partial refresh for editing)"
  [line-number text]
  ;; TODO: Implement line-level updates for efficient editing
  (let [y (+ 150 (* line-number (calculate-line-height 20)))]
    (native-render-text text 50 y 20)
    (refresh-display :partial)))

;; ============================================================================
;; DISPLAY MODES
;; ============================================================================

(defn set-brightness [level]
  "Set display brightness (0-100) - for frontlight if available"
  ;; TODO: Implement frontlight control if hardware supports it
  (println (str "Setting brightness to " level "%"))
  {:brightness level})

(defn set-contrast [level]
  "Set display contrast (0-100)"
  ;; TODO: Implement contrast adjustment
  (println (str "Setting contrast to " level "%"))
  {:contrast level})

;; ============================================================================
;; POWER MANAGEMENT
;; ============================================================================

(defn sleep-display []
  "Put display into low-power sleep mode"
  ;; TODO: Implement display sleep mode
  (println "Display entering sleep mode...")
  {:power-mode :sleep})

(defn wake-display []
  "Wake display from sleep mode"
  ;; TODO: Implement display wake
  (println "Display waking up...")
  (force-full-refresh!)
  {:power-mode :active})

;; ============================================================================
;; PUBLIC API
;; ============================================================================

(defn get-display-info []
  "Get information about the display"
  {:width display-width
   :height display-height
   :dpi display-dpi
   :grayscale-levels grayscale-levels
   :state @display-state})

(comment
  ;; REPL Usage Examples
  
  ;; Initialize display
  (init-display!)
  
  ;; Render a document
  (def test-doc {:id "test-1"
                 :title "Test Document"
                 :content "This is a test document for the Grainwriter e-ink display.\n\nIt supports multiple paragraphs and will automatically wrap text to fit the screen width."
                 :word-count 25
                 :char-count 150
                 :modified-at "2025-01-22T10:30:00Z"})
  
  (render-document test-doc)
  
  ;; Force full refresh
  (force-full-refresh!)
  
  ;; Get display info
  (get-display-info)
  
  ;; Sleep/wake
  (sleep-display)
  (wake-display)
  )


