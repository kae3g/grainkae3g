#!/usr/bin/env bb

(require '[clojure.edn :as edn]
         '[clojure.string :as str]
         '[babashka.fs :as fs])

(defn create-neovedic-timestamp
  "Generate a neovedic timestamp (simplified version)"
  []
  (let [now (java.time.LocalDateTime/now)
        year (.getYear now)
        month (format "%02d" (.getMonthValue now))
        day (format "%02d" (.getDayOfMonth now))
        hour (format "%02d" (.getHour now))
        minute (format "%02d" (.getMinute now))]
    (str year "-" month "-" day "--" hour minute "--CDT--moon-vishakha--09thhouse" hour)))

(defn create-display-metadata
  [{:keys [grainmark-name immutable-path color-temperature color-profile
           display-mode filters author intended-for]
    :or {color-temperature 2000
         color-profile :display-p3
         display-mode :reading
         filters [:reduce-blue-light]
         intended-for [:desktop :graindroid-ink]}}]
  {:version "1.0.0"
   :grainmark-name grainmark-name
   :immutable-path immutable-path
   :grainclay-url (str "/" immutable-path "/" (last (str/split grainmark-name #"/")) ".grainmark")
   :color-temperature color-temperature
   :color-profile color-profile
   :display-mode display-mode
   :filters filters
   :author author
   :intended-for intended-for
   :timestamp (str (java.time.Instant/now))})

(defn embed-metadata [content metadata]
  (let [metadata-str (pr-str metadata)]
    (str ";; GrainDisplay Metadata\n"
         ";; " metadata-str "\n\n"
         content)))

(defn main [& args]
  (let [[grainmark-name author content-file & opts] args]
    
    (when (or (nil? grainmark-name) (nil? author) (nil? content-file))
      (println "Usage: bb create-grainmark-with-metadata.bb GRAINMARK_NAME AUTHOR CONTENT_FILE [OPTIONS]")
      (println)
      (println "Example:")
      (println "  bb create-grainmark-with-metadata.bb kae3g/forest-sunset kae3g content.md \\")
      (println "     --color-temperature 2000 --filters '[:reduce-blue-light :sepia]'")
      (println)
      (println "Options:")
      (println "  --color-temperature TEMP    Color temperature in Kelvin (1000-6500)")
      (println "  --color-profile PROFILE     :srgb :display-p3 :grayscale :monochrome")
      (println "  --display-mode MODE         :vision :ink :reading :presentation")
      (println "  --filters FILTERS           Vector of filters like [:grayscale :sepia]")
      (println "  --intended-for DEVICES      Vector like [:desktop :graindroid-ink]")
      (System/exit 1))
    
    (when-not (fs/exists? content-file)
      (println "Error: Content file not found:" content-file)
      (System/exit 1))
    
    (let [content (slurp content-file)
          timestamp (create-neovedic-timestamp)
          
          ;; Parse options
          opts-map (loop [remaining opts
                         result {}]
                    (if (empty? remaining)
                      result
                      (let [[k v & rest] remaining]
                        (recur rest
                               (assoc result (keyword (subs k 2))
                                      (try (edn/read-string v)
                                           (catch Exception _ v)))))))
          
          metadata (create-display-metadata
                    (merge {:grainmark-name grainmark-name
                           :immutable-path timestamp
                           :author author}
                          opts-map))
          
          full-content (embed-metadata content metadata)
          
          output-file (str (last (str/split grainmark-name #"/")) ".grainmark")]
      
      (spit output-file full-content)
      
      (println "✅ Created Grainmark with display metadata:")
      (println)
      (println "📄 File:" output-file)
      (println "🏷️  Grainmark:" grainmark-name)
      (println "🔗 Grainclay URL:" (:grainclay-url metadata))
      (println "⏰ Timestamp:" immutable-path)
      (println "👤 Author:" author)
      (println)
      (println "📊 Display Settings:")
      (println "  🌡️  Temperature:" (:color-temperature metadata) "K")
      (println "  🎨 Color Profile:" (:color-profile metadata))
      (println "  📺 Display Mode:" (:display-mode metadata))
      (println "  🎭 Filters:" (:filters metadata))
      (println "  💻 Intended For:" (:intended-for metadata))
      (println)
      (println "🌾 Grainmark created successfully!"))))

(apply main *command-line-args*)

