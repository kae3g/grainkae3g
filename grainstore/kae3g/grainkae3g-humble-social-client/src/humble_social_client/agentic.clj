(ns humble-social-client.agentic
  "Agentic social media system with file watching and Letta memGPT integration"
  (:require [humble.ui :as ui]
            [clojure.core.async :as async :refer [go go-loop timeout <! >! <!! >!!]]
            [clojure.java-time :as time]
            [clj-http.client :as http]
            [cheshire.core :as json]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [babashka.fs :as fs]
            [clojure-s6.core :as s6]
            [clojure-sixos.core :as sixos]))

;; Configuration for agentic system
(def agentic-config
  {:file-watcher
   {:watch-dirs ["/home/xy/kae3g/12025-10/docs"
                 "/home/xy/kae3g/12025-10/notes"
                 "/home/xy/kae3g/12025-10/projects"]
    :file-patterns [#".*\.md$" #".*\.txt$" #".*\.clj$" #".*\.bb$"]
    :debounce-ms 2000
    :max-file-size 1048576} ; 1MB
   
   :letta-memgpt
   {:api-url "https://api.letta.ai/v1"
    :api-key (System/getenv "LETTA_API_KEY")
    :model "memgpt-7b"
    :max-tokens 280
    :temperature 0.7}
   
   :content-generation
   {:context-window 4000
    :min-content-length 50
    :max-content-length 280
    :posting-delay-ms 300000 ; 5 minutes between posts
    :max-posts-per-hour 3}
   
   :social-platforms
   {:x {:enabled true :weight 1.0}
    :nostr {:enabled true :weight 0.8}
    :bluesky {:enabled true :weight 0.9}
    :threads {:enabled false :weight 0.0}}})

;; Agentic state
(def agentic-state
  (atom
   {:file-watcher-active false
    :last-file-change (time/local-date-time)
    :pending-posts []
    :content-queue []
    :memgpt-context []
    :posting-schedule []
    :agent-status :idle
    :last-post-time (time/local-date-time)
    :file-change-count 0}))

;; File watching utilities
(defn should-watch-file? [file-path]
  "Check if file should be watched based on patterns"
  (let [patterns (:file-patterns (:file-watcher agentic-config))]
    (some #(re-matches % file-path) patterns)))

(defn get-file-content [file-path]
  "Safely read file content with size limits"
  (try
    (let [file (io/file file-path)
          size (.length file)
          max-size (:max-file-size (:file-watcher agentic-config))]
      (if (<= size max-size)
        (slurp file-path)
        (str "File too large: " size " bytes")))
    (catch Exception e
      (str "Error reading file: " (.getMessage e)))))

(defn extract-content-context [file-path content]
  "Extract relevant context from file content"
  (let [lines (str/split-lines content)
        relevant-lines (filter #(and (> (count %) 20)
                                     (not (str/starts-with? % "#"))
                                     (not (str/starts-with? % ";;")))
                               lines)
        context (str/join " " (take 10 relevant-lines))]
    {:file-path file-path
     :content-length (count content)
     :context context
     :timestamp (time/local-date-time)
     :file-type (last (str/split file-path #"\."))}))

;; Letta memGPT integration
(defn call-memgpt [prompt context]
  "Call Letta memGPT API for content generation"
  (let [url (str (:api-url (:letta-memgpt agentic-config)) "/chat/completions")
        headers {"Authorization" (str "Bearer " (:api-key (:letta-memgpt agentic-config)))
                 "Content-Type" "application/json"}
        body {:model (:model (:letta-memgpt agentic-config))
              :messages [{:role "system"
                          :content "You are an intelligent social media agent. Generate engaging, contextual posts based on file changes and content. Be creative but professional."}
                         {:role "user"
                          :content (str "Context: " context "\n\nGenerate a social media post about: " prompt)}]
              :max_tokens (:max-tokens (:letta-memgpt agentic-config))
              :temperature (:temperature (:letta-memgpt agentic-config))}]
    (try
      (let [response (http/post url
                               {:headers headers
                                :body (json/generate-string body)
                                :throw-exceptions false})]
        (if (= (:status response) 200)
          (let [data (json/parse-string (:body response) true)
                content (get-in data ["choices" 0 "message" "content"])]
            {:success true :content content :usage (get data "usage")})
          {:success false :error (:body response)}))
      (catch Exception e
        {:success false :error (.getMessage e)}))))

(defn generate-social-content [file-context]
  "Generate social media content from file context"
  (let [prompt (str "File: " (:file-path file-context)
                    "\nContent: " (:context file-context)
                    "\nGenerate an engaging social media post about this content.")
        memgpt-result (call-memgpt prompt file-context)]
    (if (:success memgpt-result)
      (let [content (:content memgpt-result)
            cleaned-content (str/replace content #"^[\"']|[\"']$" "") ; Remove quotes
            final-content (if (> (count cleaned-content) (:max-content-length (:content-generation agentic-config)))
                            (str (subs cleaned-content 0 (- (:max-content-length (:content-generation agentic-config)) 3)) "...")
                            cleaned-content)]
        {:success true
         :content final-content
         :source (:file-path file-context)
         :generated-at (time/local-date-time)
         :platforms (filter #(:enabled (val %)) (:social-platforms agentic-config))})
      {:success false :error (:error memgpt-result)})))

;; File watching daemon
(defn start-file-watcher []
  "Start file watching daemon"
  (go-loop []
    (try
      (let [watch-dirs (:watch-dirs (:file-watcher agentic-config))
            debounce-ms (:debounce-ms (:file-watcher agentic-config))]
        
        (doseq [dir watch-dirs]
          (when (fs/exists? dir)
            (let [files (fs/glob dir "**/*")
                  relevant-files (filter should-watch-file? files)]
              (doseq [file-path relevant-files]
                (let [content (get-file-content file-path)
                      context (extract-content-context file-path content)]
                  (when (and (> (:content-length context) (:min-content-length (:content-generation agentic-config)))
                             (not= (:context context) ""))
                    (let [generated-content (generate-social-content context)]
                      (when (:success generated-content)
                        (swap! agentic-state update :content-queue conj generated-content)
                        (swap! agentic-state update :file-change-count inc)
                        (println (str "📝 Generated content from " file-path ": " (:content generated-content))))))))))
        
        (<! (timeout debounce-ms))
        (recur))
      
      (catch Exception e
        (println (str "File watcher error: " (.getMessage e)))
        (<! (timeout 5000))
        (recur)))))

;; Content posting scheduler
(defn schedule-content-posting [content]
  "Schedule content for posting with rate limiting"
  (let [now (time/local-date-time)
        last-post (:last-post-time @agentic-state)
        delay-ms (:posting-delay-ms (:content-generation agentic-config))
        next-post-time (.plus last-post (time/millis delay-ms))]
    
    (if (.isAfter now next-post-time)
      (do
        (swap! agentic-state assoc :last-post-time now)
        (post-content-to-platforms content))
      (do
        (swap! agentic-state update :pending-posts conj content)
        (println (str "⏰ Content scheduled for later posting: " (:content content)))))))

(defn post-content-to-platforms [content]
  "Post content to enabled platforms"
  (let [platforms (filter #(:enabled (val %)) (:social-platforms agentic-config))
        content-text (:content content)]
    (go
      (doseq [[platform config] platforms]
        (when (:enabled config)
          (case platform
            :x (let [result (<! (go (humble-social-client.core/x-post-tweet content-text)))]
                 (println (str "🐦 X post result: " result)))
            :nostr (let [result (<! (go (humble-social-client.core/nostr-publish-note content-text)))]
                     (println (str "⚡ Nostr post result: " result)))
            :bluesky (let [session (<! (go (humble-social-client.core/bluesky-create-session)))]
                       (when (:success session)
                         (let [result (<! (go (humble-social-client.core/bluesky-post-skeet content-text (:accessJwt (:data session)))))]
                           (println (str "🦋 Bluesky post result: " result)))))
            :threads (let [result (<! (go (humble-social-client.core/threads-post content-text
                                                                                    (:session-id (:threads humble-social-client.core/config))
                                                                                    (:user-id (:threads humble-social-client.core/config)))))]
                         (println (str "🧵 Threads post result: " result)))))))))

;; s6 service integration
(defn create-agentic-service []
  "Create s6 service for agentic social media"
  (let [service-config
        {:name "agentic-social"
         :command (str "bb " (System/getProperty "user.dir") "/grainstore/humble-social-client/scripts/agentic-daemon.bb")
         :type :longrun
         :dependencies ["network" "filesystem"]
         :user (System/getProperty "user.name")
         :group (System/getProperty "user.name")
         :environment {"LETTA_API_KEY" (System/getenv "LETTA_API_KEY")
                       "X_BEARER_TOKEN" (System/getenv "X_BEARER_TOKEN")
                       "NOSTR_API_KEY" (System/getenv "NOSTR_API_KEY")
                       "BLUESKY_HANDLE" (System/getenv "BLUESKY_HANDLE")
                       "BLUESKY_PASSWORD" (System/getenv "BLUESKY_PASSWORD")
                       "THREADS_SESSION_ID" (System/getenv "THREADS_SESSION_ID")
                       "THREADS_USER_ID" (System/getenv "THREADS_USER_ID")}}]
    
    (s6/create-service service-config)
    (s6/enable-service "agentic-social")
    (println "🤖 Agentic social service created and enabled")))

(defn start-agentic-daemon []
  "Start the agentic daemon"
  (println "🤖 Starting Agentic Social Media Daemon...")
  (println "   File watching: " (:watch-dirs (:file-watcher agentic-config)))
  (println "   Letta memGPT: " (:model (:letta-memgpt agentic-config)))
  (println "   Platforms: " (keys (filter #(:enabled (val %)) (:social-platforms agentic-config))))
  
  (swap! agentic-state assoc :agent-status :active)
  (start-file-watcher)
  
  ;; Content processing loop
  (go-loop []
    (try
      (let [content-queue (:content-queue @agentic-state)]
        (when (seq content-queue)
          (let [next-content (first content-queue)]
            (schedule-content-posting next-content)
            (swap! agentic-state update :content-queue rest))))
      
      (<! (timeout 10000)) ; Check every 10 seconds
      (recur)
      
      (catch Exception e
        (println (str "Content processing error: " (.getMessage e)))
        (<! (timeout 5000))
        (recur)))))

;; UI Components for agentic system
(defn agentic-status-panel []
  "Agentic system status panel"
  (ui/column
   {:style {:background "#1a1a1a"
            :padding 16
            :border-radius 8
            :margin 8}}
   (ui/label
    {:style {:font-size 18
             :color "#ffffff"
             :margin-bottom 8}}
    "🤖 Agentic Status")
   
   (ui/label
    {:style {:color "#00ff00"}}
    (str "Status: " (name (:agent-status @agentic-state))))
   
   (ui/label
    {:style {:color "#ffffff"}}
    (str "File Changes: " (:file-change-count @agentic-state)))
   
   (ui/label
    {:style {:color "#ffffff"}}
    (str "Content Queue: " (count (:content-queue @agentic-state))))
   
   (ui/label
    {:style {:color "#ffffff"}}
    (str "Pending Posts: " (count (:pending-posts @agentic-state))))
   
   (ui/row
    (ui/button
     {:on-click #(start-agentic-daemon)
      :style {:background "#00ff00"
              :color "#000000"
              :padding "8px 16px"
              :border-radius 4
              :margin 4}}
     (ui/label "Start Agent"))
    
    (ui/button
     {:on-click #(swap! agentic-state assoc :agent-status :idle)
      :style {:background "#ff0000"
              :color "#ffffff"
              :padding "8px 16px"
              :border-radius 4
              :margin 4}}
     (ui/label "Stop Agent")))))

(defn content-queue-panel []
  "Content queue display panel"
  (ui/column
   {:style {:background "#1a1a1a"
            :padding 16
            :border-radius 8
            :margin 8
            :max-height 300}}
   (ui/label
    {:style {:font-size 16
             :color "#ffffff"
             :margin-bottom 8}}
    "📝 Generated Content Queue")
   
   (ui/scroll
    (ui/column
     (for [content (:content-queue @agentic-state)]
       (ui/column
        {:style {:background "#2a2a2a"
                 :padding 8
                 :border-radius 4
                 :margin 2}}
        (ui/label
         {:style {:color "#ffffff"
                  :font-size 12}}
         (str "Source: " (:source content)))
        (ui/label
         {:style {:color "#ffffff"
                  :margin "4px 0"}}
         (:content content))
        (ui/label
         {:style {:color "#888888"
                  :font-size 10}}
         (str "Generated: " (time/format "HH:mm:ss" (:generated-at content))))))))))

(defn agentic-main-panel []
  "Main agentic system panel"
  (ui/column
   {:style {:background "#0a0a0a"
            :padding 16
            :height "100%"
            :width "100%"}}
   
   (ui/label
    {:style {:font-size 24
             :color "#ffffff"
             :margin-bottom 16}}
    "🤖 Agentic Social Media System")
   
   (ui/row
    {:style {:flex 1}}
    
    (ui/column
     {:style {:width "50%"}}
     (agentic-status-panel)
     (content-queue-panel))
    
    (ui/column
     {:style {:width "50%"}}
     (ui/label
      {:style {:color "#ffffff"
               :margin-bottom 8}}
      "Configuration")
     (ui/label
      {:style {:color "#888888"
               :font-size 12}}
      (str "Watch Dirs: " (str/join ", " (:watch-dirs (:file-watcher agentic-config)))))
     (ui/label
      {:style {:color "#888888"
               :font-size 12}}
      (str "MemGPT Model: " (:model (:letta-memgpt agentic-config))))
     (ui/label
      {:style {:color "#888888"
               :font-size 12}}
      (str "Posting Delay: " (:posting-delay-ms (:content-generation agentic-config)) "ms"))))))

;; Main agentic application
(defn -main []
  "Start the Agentic Social Media System"
  (println "🤖 Starting Agentic Social Media System...")
  (println "   File watching with s6 daemon")
  (println "   Letta memGPT integration")
  (println "   Multi-platform posting")
  (println "   SixOS integration ready")
  
  (ui/start-app!
   (ui/window
    {:title "Agentic Social Media System"
     :width 1400
     :height 900
     :on-close (fn [] (println "Agentic system closing"))}
    agentic-main-panel)))

;; Run the application
(-main)

