#!/usr/bin/env bb
;; QB KK - Print PSEUDO.md as a grainbook of graincards
;; "kk" means "continue" - keep the momentum going!

(require '[clojure.string :as str]
         '[clojure.java.io :as io])

(def graincard-width 80)
(def graincard-height 110)

(defn print-graincard-header []
  (println "╔══════════════════════════════════════════════════════════════════════════════╗")
  (println "║                                                                              ║")
  (println "║                    🌾 G R A I N B O O K - K K 🌾                            ║")
  (println "║                                                                              ║")
  (println "║                  PSEUDO.md as Graincard Collection                          ║")
  (println "║                                                                              ║")
  (println "╚══════════════════════════════════════════════════════════════════════════════╝")
  (println ""))

(defn wrap-text [text width]
  "Wrap text to specified width"
  (let [words (str/split text #"\s+")]
    (loop [lines []
           current-line ""
           remaining words]
      (if (empty? remaining)
        (conj lines current-line)
        (let [word (first remaining)
              test-line (if (empty? current-line) 
                          word 
                          (str current-line " " word))]
          (if (> (count test-line) width)
            (recur (conj lines current-line) word (rest remaining))
            (recur lines test-line (rest remaining))))))))

(defn print-graincard [title content card-num]
  "Print a single graincard (80x110 format)"
  (println "")
  (println (str "┌" (str/join (repeat 78 "─")) "┐"))
  (println (str "│" (format " %-76s " (str "Card " card-num ": " title)) "│"))
  (println (str "├" (str/join (repeat 78 "─")) "┤"))
  
  ;; Wrap and print content
  (let [wrapped-lines (mapcat #(wrap-text % 76) (str/split-lines content))
        padded-lines (take 105 (concat wrapped-lines (repeat "")))]
    (doseq [line padded-lines]
      (println (str "│ " (format "%-76s" line) " │"))))
  
  (println (str "├" (str/join (repeat 78 "─")) "┤"))
  (println (str "│" (format " %-76s " "now == next + 1 🌾") "│"))
  (println (str "└" (str/join (repeat 78 "─")) "┘"))
  (println ""))

(defn parse-pseudo-sections [content]
  "Parse PSEUDO.md into sections for graincards"
  (let [lines (str/split-lines content)
        sections (atom [])
        current-section (atom {:title "" :content []})]
    
    (doseq [line lines]
      (cond
        ;; Main headers (# or ##)
        (re-matches #"^##?\s+.*" line)
        (do
          (when (not-empty (:content @current-section))
            (swap! sections conj @current-section))
          (reset! current-section {:title (str/replace line #"^##?\s+" "")
                                   :content []}))
        
        ;; Content lines
        (not (str/blank? line))
        (swap! current-section update :content conj line)))
    
    ;; Add final section
    (when (not-empty (:content @current-section))
      (swap! sections conj @current-section))
    
    @sections))

(defn find-pseudo-file []
  "Find PSEUDO.md in the repository"
  (let [possible-paths ["../../docs/core/philosophy/PSEUDO.md"
                        "docs/core/philosophy/PSEUDO.md"
                        "docs/PSEUDO.md"
                        "PSEUDO.md"]]
    (some #(when (.exists (io/file %)) %) possible-paths)))

(defn main []
  (print-graincard-header)
  
  (if-let [pseudo-path (find-pseudo-file)]
    (do
      (println (str "📖 Reading PSEUDO.md from: " pseudo-path))
      (println "")
      (let [content (slurp pseudo-path)
            sections (parse-pseudo-sections content)]
        
        (println (str "🌾 Generating " (count sections) " graincards..."))
        (println "")
        
        (doseq [[idx section] (map-indexed vector sections)]
          (print-graincard 
            (:title section)
            (str/join "\n" (:content section))
            (inc idx)))
        
        (println "")
        (println "╔══════════════════════════════════════════════════════════════════════════════╗")
        (println "║                                                                              ║")
        (println (str "║  " (format "%-74s" (str "Total Graincards: " (count sections))) "  ║"))
        (println "║                                                                              ║")
        (println "║                         🌾 now == next + 1 🌾                               ║")
        (println "║                                                                              ║")
        (println "╚══════════════════════════════════════════════════════════════════════════════╝")
        (println "")
        (println "🌾 Grainbook complete! From PSEUDO to graincard reality!")
        (println "🌾 kk - keep the momentum going!")))
    
    (do
      (println "❌ Error: Could not find PSEUDO.md")
      (println "")
      (println "Searched locations:")
      (println "  • docs/core/philosophy/PSEUDO.md")
      (println "  • docs/PSEUDO.md")
      (println "  • PSEUDO.md")
      (println "")
      (println "Please ensure PSEUDO.md exists in one of these locations."))))

(main)

