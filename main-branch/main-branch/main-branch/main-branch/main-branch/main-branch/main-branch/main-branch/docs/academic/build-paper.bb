#!/usr/bin/env bb

(require '[babashka.process :refer [shell]]
         '[clojure.java.io :as io])

(defn build-figures []
  "Build TikZ figures to PDF"
  (println "🎨 Building academic figures...")
  
  (let [figures-dir "figures"
        figure-files ["88-counter-philosophy.tex" 
                     "multi-chain-architecture.tex" 
                     "graincard10-format.tex"]]
    
    (doseq [figure-file figure-files]
      (let [input-path (str figures-dir "/" figure-file)
            output-name (clojure.string/replace figure-file #"\.tex$" ".pdf")]
        (println (str "  Building " figure-file "..."))
        (shell "pdflatex" "-interaction=nonstopmode" "-output-directory" figures-dir input-path)
        (shell "rm" (str figures-dir "/" (clojure.string/replace figure-file #"\.tex$" ".aux")))
        (shell "rm" (str figures-dir "/" (clojure.string/replace figure-file #"\.tex$" ".log")))))))

(defn build-paper []
  "Build the main LaTeX paper"
  (println "📄 Building main paper...")
  
  (shell "pdflatex" "-interaction=nonstopmode" "graintime-arxiv-paper.tex")
  (shell "bibtex" "graintime-arxiv-paper")
  (shell "pdflatex" "-interaction=nonstopmode" "graintime-arxiv-paper.tex")
  (shell "pdflatex" "-interaction=nonstopmode" "graintime-arxiv-paper.tex")
  
  (println "✅ Paper built successfully!")
  (println "📁 Output: graintime-arxiv-paper.pdf"))

(defn clean-build []
  "Clean build artifacts"
  (println "🧹 Cleaning build artifacts...")
  
  (let [clean-files ["*.aux" "*.log" "*.out" "*.toc" "*.bbl" "*.blg" "*.fdb_latexmk" "*.fls" "*.synctex.gz"]]
    (doseq [pattern clean-files]
      (shell "rm" "-f" pattern))))

(defn main [& args]
  (let [command (first args)]
    (case command
      "figures" (build-figures)
      "paper" (build-paper)
      "clean" (clean-build)
      "all" (do (build-figures) (build-paper))
      (do
        (println "🌾 Grain Network Academic Paper Builder")
        (println "")
        (println "Usage:")
        (println "  bb build-paper.bb figures  - Build TikZ figures")
        (println "  bb build-paper.bb paper    - Build main paper")
        (println "  bb build-paper.bb clean    - Clean build artifacts")
        (println "  bb build-paper.bb all      - Build everything")
        (println "")
        (println "Requirements:")
        (println "  - pdflatex (LaTeX distribution)")
        (println "  - bibtex (bibliography processor)")
        (println "  - tikz (for figures)")
        (println "")
        (println "🌾 now == next + 1")))))

(apply main *command-line-args*)
