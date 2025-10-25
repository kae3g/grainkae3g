#!/usr/bin/env bb

(require '[clojure.java.io :as io]
         '[clojure.string :as str]
         '[markdown.core :as md])

(def config-file "personal/grain-network-course/course.edn")

(defn load-config []
  (when-not (.exists (io/file config-file))
    (throw (ex-info "Course config not found" {:file config-file})))
  (read-string (slurp config-file)))

(defn get-course-config []
  (get-in (load-config) [:course]))

(defn ensure-dir [path]
  (.mkdirs (io/file path)))

(defn lesson-number [filename]
  (when-let [num (re-find #"^\d+" filename)]
    (Integer/parseInt num)))

(defn get-lessons [source-dir]
  (->> (file-seq (io/file source-dir))
       (filter #(.isFile %))
       (filter #(str/ends-with? (.getName %) ".md"))
       (filter #(not= "INDEX.md" (.getName %)))
       (map (fn [f]
              {:file f
               :name (.getName f)
               :number (lesson-number (.getName f))}))
       (filter :number)
       (sort-by :number)))

(defn html-template [config title content]
  (let [theme (get config :theme {})
        nav (get config :navigation {})
        footer (get config :footer {})
        primary-color (get theme :primary-color "#2c5f2d")
        accent-color (get theme :accent-color "#97c93d")]
    (str "<!DOCTYPE html>\n"
         "<html lang=\"en\">\n"
         "<head>\n"
         "  <meta charset=\"UTF-8\">\n"
         "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
         "  <title>" title " - " (get config :title "Course") "</title>\n"
         "  <style>\n"
         "    body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;\n"
         "           max-width: 800px; margin: 40px auto; padding: 0 20px;\n"
         "           line-height: 1.6; color: " (get theme :text "#333") "; }\n"
         "    h1 { color: " primary-color "; border-bottom: 3px solid " accent-color "; padding-bottom: 10px; }\n"
         "    h2 { color: " primary-color "; margin-top: 30px; }\n"
         "    pre { background: " (get theme :code-background "#f5f5f5") "; padding: 15px; border-radius: 5px; overflow-x: auto; }\n"
         "    code { background: " (get theme :code-background "#f5f5f5") "; padding: 2px 6px; border-radius: 3px; }\n"
         "    a { color: " primary-color "; text-decoration: none; }\n"
         "    a:hover { text-decoration: underline; }\n"
         "    .nav { background: #f9f9f9; padding: 15px; border-radius: 5px; margin: 20px 0; }\n"
         "    .lesson-list { list-style: none; padding: 0; }\n"
         "    .lesson-list li { margin: 10px 0; }\n"
         "  </style>\n"
         "</head>\n"
         "<body>\n"
         "  <nav class=\"nav\">\n"
         "    <a href=\"index.html\">" (get nav :home-text "🏠 Course Home") "</a> | \n"
         (when-let [gh (get nav :github-url)]
           (str "<a href=\"" gh "\">📚 GitHub</a> | \n"))
         (when-let [cb (get nav :codeberg-url)]
           (str "<a href=\"" cb "\">🌐 Codeberg</a>\n"))
         "  </nav>\n"
         content
         "  <footer style=\"margin-top: 60px; padding-top: 20px; border-top: 1px solid #ddd; color: #666;\">\n"
         "    <p>" (get footer :text "🌾 Course") " | Session " (get config :session "808") " | " (get config :timestamp "12025-10-23") "</p>\n"
         "    <p><em>" (get footer :philosophy "From granules to grains to THE WHOLE GRAIN") "</em></p>\n"
         "  </footer>\n"
         "</body>\n"
         "</html>")))

(defn build-lesson [config lesson-info output-dir]
  (let [content (slurp (:file lesson-info))
        html-content (md/md-to-html-string content)
        title (str "Lesson " (:number lesson-info))
        output-file (str output-dir "/" 
                        (str/replace (:name lesson-info) #"\.md$" ".html"))]
    (spit output-file (html-template config title html-content))
    (println "Built:" output-file)))

(defn build-index [config lessons output-dir]
  (let [index-md (slurp (io/file (get config :source-dir) "INDEX.md"))
        html-content (md/md-to-html-string index-md)
        title (get config :title "Course")
        output-file (str output-dir "/index.html")]
    (spit output-file (html-template config title html-content))
    (println "Built:" output-file)))

(defn main []
  (println "Building Grain Network Course...")
  (let [config (get-course-config)
        source-dir (get config :source-dir)
        output-dir (get config :output-dir "public")]
    (ensure-dir output-dir)
    (let [lessons (get-lessons source-dir)]
      (println "Found" (count lessons) "lessons")
      (doseq [lesson lessons]
        (build-lesson config lesson output-dir))
      (build-index config lessons output-dir)
      (println "Build complete! Output in" output-dir))))

(main)