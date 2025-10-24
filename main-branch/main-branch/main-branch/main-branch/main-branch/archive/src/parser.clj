(ns kae3g.parser
  "Sacred markdown parser for robotic farm
   consciousness documents. Transforms markdown
   teachings into ClojureScript DSL structures"
  (:require [clojure.string :as str]
            [babashka.fs :as fs]
            [markdown.core :as md]))

(defn extract-frontmatter
  "Extract YAML-style frontmatter from markdown
   with contemplative attention"
  [content]
  (if (str/starts-with? content "---")
    (let [parts (str/split content #"---" 3)
          frontmatter (second parts)
          body (nth parts 2 "")]
      {:frontmatter frontmatter
       :body (str/trim body)})
    {:frontmatter nil
     :body content}))

(defn parse-sacred-quotes
  "Extract sacred scriptural quotes marked with
   asterisks in Christian federal style"
  [content]
  (let [pattern #"\*\"([^\"]+)\"\*"
        matches (re-seq pattern content)]
    (map second matches)))

(defn classify-consciousness-type
  "Classify document by consciousness keywords
   following robotic farm integration patterns"
  [content]
  {:robotic-consciousness
   (str/includes? content "robotic")
   
   :farm-consciousness
   (str/includes? content "agricultural")
   
   :nixtaveganic-integration
   (str/includes? content "nixtaveganic")
   
   :community-coordination
   (str/includes? content "community")
   
   :ecological-principles
   (str/includes? content "Helen Atthowe")
   
   :sacred-technology
   (str/includes? content "Sacred")})

(defn extract-code-blocks
  "Extract Clojure/ClojureScript code blocks
   with Divine Grace awareness"
  [content]
  (let [pattern #"```clojure\n(.*?)\n```"
        matches (re-seq pattern content)]
    (mapv second matches)))

(defn extract-yaml-blocks
  "Extract YAML configuration blocks for
   ParableOS integration consciousness"
  [content]
  (let [pattern #"```yaml\n(.*?)\n```"
        matches (re-seq pattern content)]
    (mapv second matches)))

(defn extract-title-from-content
  "Extract title from first H1 heading as fallback"
  [content]
  (when-let [match (re-find #"(?m)^#\s+(.+)$" content)]
    (second match)))

(defn parse-document-structure
  "Parse document into sacred consciousness
   structure following federal writing style"
  [filepath]
  (let [content (slurp filepath)
        {:keys [frontmatter body]}
        (extract-frontmatter content)
        
        filename (fs/file-name filepath)
        number (when-let [match
                          (re-find #"(\d{7})"
                                   filename)]
                 (Integer/parseInt (second match)))
        title-from-filename
        (when-let [match
                   (re-find
                    #"\d{7}_(.+)\.md$"
                    filename)]
          (-> (second match)
              (str/replace "_" " ")
              (str/replace "-" " ")
              str/capitalize))
        ;; Fallback to extracting title from content
        title (or title-from-filename
                  (extract-title-from-content body)
                  "Untitled")]
    
    {:document/number number
     :document/title title
     :document/filepath filepath
     :document/frontmatter frontmatter
     :document/body body
     :document/sacred-quotes
     (parse-sacred-quotes body)
     :document/consciousness-type
     (classify-consciousness-type body)
     :document/code-blocks
     (extract-code-blocks body)
     :document/yaml-blocks
     (extract-yaml-blocks body)
     :document/html-content
     (md/md-to-html-string body)}))

(defn discover-kae3g-documents
  "Discover all robotic farm consciousness docs
   with contemplative awareness"
  [docs-path]
  (println "🌾 Discovering robotic farm documents")
  (let [pattern "*.md"
        files (fs/glob docs-path pattern)]
    (println "📂 Found" (count files) "documents")
    (->> files
         (map str)
         sort)))

(defn discover-all-markdown-files
  "Discover ALL markdown files in repository
   including README, docs, etc."
  []
  (println "🌙 Discovering ALL markdown files")
  (let [;; Get all .md files recursively
        all-files (fs/glob "." "**/*.md")
        ;; Filter out node_modules and build dirs
        filtered (->> all-files
                      (map str)
                      (remove #(str/includes? % "node_modules"))
                      (remove #(str/includes? % "build"))
                      (remove #(str/includes? % ".git"))
                      sort)]
    (println "📂 Found" (count filtered) "total MD files")
    filtered))

(defn parse-all-documents
  "Parse all discovered documents into DSL
   structures with Divine Grace"
  [docs-path]
  (let [files (discover-kae3g-documents
               docs-path)]
    (mapv parse-document-structure files)))

(defn -main
  "Sacred parsing entry point for robotic farm
   consciousness document pipeline"
  [& args]
  (println "🤖 Robotic Farm Parser: Awakening...")
  (println "🌙 Transforming markdown → DSL")
  
  (let [parse-all? (some #(= "--all" %) args)
        files (if parse-all?
                (discover-all-markdown-files)
                (discover-kae3g-documents
                 (or (System/getenv "DOCS_PATH")
                     "docs")))
        parsed (mapv parse-document-structure files)]
    (println "✨ Parsed" (count parsed)
             "documents")
    (spit "build/parsed-documents.edn"
          (pr-str parsed))
    (println "📝 Saved to:"
             "build/parsed-documents.edn")
    parsed))

