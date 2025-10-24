#!/usr/bin/env bb
;; Generate site configuration from .config.edn for use by web app

(require '[babashka.fs :as fs]
         '[clojure.edn :as edn]
         '[cheshire.core :as json])

(def config-file ".config.edn")
(def output-file "web-app/static/site-config.json")

(defn load-config []
  (if (fs/exists? config-file)
    (edn/read-string (slurp config-file))
    (do
      (println "⚠️  .config.edn not found, using defaults")
      {:site {:username "kae3g" :language "en" :title "Coldriver Tundra"}
       :localization {:planned-languages ["es" "de"]}})))

(defn generate-site-config []
  (println "📝 Generating site configuration...")
  (let [config (load-config)
        site-config {:username (get-in config [:site :username] "kae3g")
                     :language (get-in config [:site :language] "en")
                     :title (get-in config [:site :title] "Coldriver Tundra")
                     :description (get-in config [:site :description] "")
                     :repository (get-in config [:repository] {})
                     :plannedLanguages (get-in config [:localization :planned-languages] ["es" "de"])}]
    
    ;; Ensure output directory exists
    (fs/create-dirs (fs/parent output-file))
    
    ;; Write JSON config
    (spit output-file (json/generate-string site-config {:pretty true}))
    (println "✓ Site config written to" output-file)
    (println "  Username:" (:username site-config))
    (println "  Language:" (:language site-config))
    (println "  Planned languages:" (str/join ", " (:plannedLanguages site-config)))))

(generate-site-config)
