(ns graintranscribe.core
  "Core functionality for YouTube video transcription using Gemini 2.5 Pro
   Inspired by sixos infuse.nix modular pattern with template/personal separation"
  (:require [clj-http.client :as http]
            [cheshire.core :as json]
            [babashka.fs :as fs]
            [clojure.string :as str]
            [clojure.edn :as edn]))

;; Infuse pattern: deep merge of template and personal configs
(defn infuse-configs
  "Deep merge template and personal configs (personal takes precedence)
   Inspired by sixos infuse.nix library"
  [template personal]
  (cond
    (and (map? template) (map? personal))
    (merge-with infuse-configs template personal)
    
    (some? personal) personal
    :else template))

(defn load-config
  "Load configuration with template/personal infusion pattern"
  []
  (let [template-path "template/config.edn"
        personal-path "personal/config.edn"
        
        template-config (if (fs/exists? template-path)
                         (edn/read-string (slurp template-path))
                         {})
        
        personal-config (if (fs/exists? personal-path)
                         (edn/read-string (slurp personal-path))
                         {})]
    
    (infuse-configs template-config personal-config)))

(defn get-api-key
  "Get Gemini API key from config or environment"
  [config]
  (let [key-config (get-in config [:gemini :api-key])]
    (cond
      (string? key-config) key-config
      (map? key-config) (System/getenv (get key-config :grainenvvars))
      :else (System/getenv "GEMINI_API_KEY"))))

(defn extract-video-id
  "Extract YouTube video ID from URL"
  [url]
  (cond
    (re-find #"youtube\.com/watch\?v=([^&]+)" url)
    (second (re-find #"youtube\.com/watch\?v=([^&]+)" url))
    
    (re-find #"youtu\.be/([^?]+)" url)
    (second (re-find #"youtu\.be/([^?]+)" url))
    
    :else url))  ; Assume it's already a video ID

(defn build-transcription-prompt
  "Build prompt for Gemini API based on config"
  [config]
  (or (get-in config [:prompts :transcription])
      "Please transcribe this YouTube video with timestamps, speaker identification, and visual descriptions."))

(defn call-gemini-api
  "Call Gemini 2.5 Pro API for video transcription"
  [video-url api-key config]
  (try
    (let [model (get-in config [:gemini :model] "gemini-2.5-pro-latest")
          temperature (get-in config [:gemini :temperature] 0.2)
          max-tokens (get-in config [:gemini :max-tokens] 1000000)
          prompt (build-transcription-prompt config)
          
          request-body {:contents [{:parts [{:text prompt}
                                            {:video_url video-url}]}]
                       :generationConfig {:maxOutputTokens max-tokens
                                         :temperature temperature}}
          
          response (http/post 
                     (str "https://generativelanguage.googleapis.com/v1/models/" 
                          model ":generateContent")
                     {:headers {"Content-Type" "application/json"
                               "x-goog-api-key" api-key}
                      :body (json/generate-string request-body)
                      :throw-exceptions false})]
      
      (if (= 200 (:status response))
        {:success true
         :data (json/parse-string (:body response) true)}
        {:success false
         :error (str "API returned status " (:status response))
         :response (:body response)}))
    
    (catch Exception e
      {:success false
       :error (.getMessage e)})))

(defn extract-transcript-text
  "Extract transcript text from Gemini API response"
  [api-response]
  (try
    (-> api-response
        :data
        :candidates
        first
        :content
        :parts
        first
        :text)
    (catch Exception e
      (str "Error extracting transcript: " (.getMessage e)))))

(defn generate-output-filename
  "Generate output filename based on config pattern"
  [video-id config]
  (let [pattern (get-in config [:output :filename-pattern] "{video-id}_{date}")
        date (str/replace (str (java.time.LocalDate/now)) #"-" "")
        graintime (when (get-in config [:grainpath :use-graintime])
                   (try
                     (-> (babashka.process/shell {:out :string} "gt")
                         :out
                         str/trim
                         (str/split #"\n")
                         last
                         str/trim)
                     (catch Exception _ nil)))]
    (-> pattern
        (str/replace "{video-id}" video-id)
        (str/replace "{date}" date)
        (str/replace "{graintime}" (or graintime date)))))

(defn save-transcript
  "Save transcript to file based on config"
  [video-id transcript config]
  (let [output-dir (get-in config [:output :dir] "personal/transcriptions")
        format (get-in config [:output :format] :markdown)
        filename (generate-output-filename video-id config)
        extension (case format
                   :markdown ".md"
                   :text ".txt"
                   :json ".json"
                   :edn ".edn"
                   ".md")
        full-path (str output-dir "/" filename extension)]
    
    ;; Create output directory if it doesn't exist
    (fs/create-dirs output-dir)
    
    ;; Format transcript based on output format
    (let [formatted (case format
                     :markdown (str "# YouTube Transcript\n\n"
                                   "**Video ID**: " video-id "\n\n"
                                   transcript)
                     :json (json/generate-string {:video-id video-id
                                                  :transcript transcript})
                     :edn (pr-str {:video-id video-id
                                  :transcript transcript})
                     transcript)]
      
      (spit full-path formatted)
      {:success true
       :file full-path
       :video-id video-id})))

(defn transcribe-video
  "Main function to transcribe a YouTube video"
  ([video-url] (transcribe-video video-url (load-config)))
  ([video-url config]
   (println "🌾 Graintranscribe - YouTube Video Transcription")
   (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n")
   
   (let [video-id (extract-video-id video-url)
         api-key (get-api-key config)]
     
     (println (str "📺 Video ID: " video-id))
     (println (str "🔗 URL: " video-url))
     (println)
     
     (if (or (nil? api-key) (= api-key "REPLACE_WITH_YOUR_GEMINI_API_KEY"))
       (do
         (println "❌ Error: No valid API key found")
         (println "\nPlease set up your configuration:")
         (println "  1. Run: bb config:setup")
         (println "  2. Edit: personal/config.edn")
         (println "  3. Add your Gemini API key from https://aistudio.google.com/apikey")
         {:success false
          :error "No API key configured"})
       
       (do
         (println "🤖 Calling Gemini 2.5 Pro API...")
         (println "   Model: " (get-in config [:gemini :model]))
         (println)
         
         (let [api-response (call-gemini-api video-url api-key config)]
           (if (:success api-response)
             (do
               (println "✅ Transcription successful")
               (println)
               
               (let [transcript (extract-transcript-text api-response)
                     save-result (save-transcript video-id transcript config)]
                 
                 (if (:success save-result)
                   (do
                     (println "💾 Saved to: " (:file save-result))
                     (println)
                     (println "🌾 Transcription complete!")
                     save-result)
                   (do
                     (println "❌ Failed to save transcript")
                     save-result))))
             
             (do
               (println "❌ API call failed:")
               (println "   " (:error api-response))
               api-response))))))))

(defn validate-config
  "Validate configuration"
  ([] (validate-config (load-config)))
  ([config]
   (println "🔍 Validating graintranscribe-youtube configuration...")
   (println)
   
   (let [api-key (get-api-key config)
         errors (atom [])]
     
     ;; Check API key
     (if (or (nil? api-key) 
             (= api-key "REPLACE_WITH_YOUR_GEMINI_API_KEY")
             (empty? api-key))
       (swap! errors conj "No valid Gemini API key configured"))
     
     ;; Check output directory
     (let [output-dir (get-in config [:output :dir])]
       (when-not output-dir
         (swap! errors conj "No output directory specified")))
     
     ;; Report results
     (if (empty? @errors)
       (do
         (println "✅ Configuration valid")
         (println "\n📋 Current settings:")
         (println "   Model: " (get-in config [:gemini :model]))
         (println "   Output dir: " (get-in config [:output :dir]))
         (println "   Format: " (get-in config [:output :format]))
         (println "   Grainpath: " (get-in config [:grainpath :generate]))
         {:success true})
       (do
         (println "❌ Configuration errors:")
         (doseq [error @errors]
           (println "   •" error))
         (println "\nRun: bb config:setup")
         {:success false
          :errors @errors})))))
