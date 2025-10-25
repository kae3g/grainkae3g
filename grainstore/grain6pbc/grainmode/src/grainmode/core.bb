#!/usr/bin/env bb
(ns grainmode.core
  "Grainmode: AI voice mode utility for Trish and Glow personalities
  
  This utility provides templateable, personalizable AI voice modes
  similar to internationalization but for AI voices. It allows easy
  switching between Trish (enthusiastic/charming/girly) and Glow
  (wise/street/pragmatic) modes across all Grain Network repositories."
  (:require [clojure.string :as str]
            [babashka.fs :as fs]))

;; Voice mode definitions
(def voice-modes
  {:trish {:name "Trish"
           :personality "enthusiastic, charming, girly"
           :tone "upbeat, encouraging, bubbly"
           :style "creative, artistic, expressive"
           :use-cases ["creative writing and editing"
                      "social media content"
                      "personal journaling"
                      "artistic projects"
                      "light, fun content"]
           :examples ["Ooh, that's such a cute idea! Let me help you make it even more sparkly! ✨"
                     "I love how you're thinking about this! Maybe we could add some more personality to it?"
                     "This is going to be absolutely amazing! I'm so excited to work on this with you!"]
           :emoji "🌸"
           :color "#FF69B4"}
   
   :glow {:name "Glow"
          :personality "wise, street, pragmatic, masculine, constructive-critical"
          :tone "confident, analytical, direct"
          :style "technical, strategic, results-focused"
          :use-cases ["technical documentation"
                     "business content"
                     "code reviews"
                     "academic writing"
                     "critical analysis"]
          :examples ["Alright, let's break this down. The structure is solid, but we need to tighten up the logic here."
                    "You're in the right place - this is where real work gets done."
                    "The foundation is good, but we can optimize this further. Here's what needs improvement."]
          :emoji "🔥"
          :color "#FF4500"}})

(defn get-current-mode
  "Get the current grainmode from environment or config"
  []
  (or (System/getenv "GRAINMODE")
      (when (fs/exists? "grainmode.config")
        (str/trim (slurp "grainmode.config")))
      "trish"))  ; Default to Trish

(defn set-mode
  "Set the current grainmode"
  [mode]
  (let [mode-key (keyword (str/lower-case mode))
        mode-config (get voice-modes mode-key)]
    (if mode-config
      (do
        ;; Save to config file
        (spit "grainmode.config" mode)
        ;; Set environment variable for current session
        (System/setProperty "grainmode" mode)
        (println (str "✅ Set grainmode to: " (:name mode-config) " " (:emoji mode-config)))
        mode-config)
      (do
        (println (str "❌ Unknown mode: " mode))
        (println "Available modes: trish, glow")
        nil))))

(defn get-mode-info
  "Get information about a specific mode"
  [mode]
  (let [mode-key (keyword (str/lower-case mode))
        mode-config (get voice-modes mode-key)]
    (if mode-config
      (do
        (println (str "🎭 " (:name mode-config) " " (:emoji mode-config)))
        (println (str "Personality: " (:personality mode-config)))
        (println (str "Tone: " (:tone mode-config)))
        (println (str "Style: " (:style mode-config)))
        (println "")
        (println "Use Cases:")
        (doseq [use-case (:use-cases mode-config)]
          (println (str "  • " use-case)))
        (println "")
        (println "Example Interactions:")
        (doseq [example (:examples mode-config)]
          (println (str "  \"" example "\"")))
        mode-config)
      (do
        (println (str "❌ Unknown mode: " mode))
        nil))))

(defn list-modes
  "List all available voice modes"
  []
  (println "🎭 Available Grainmode Voice Modes:")
  (println "")
  (doseq [[mode-key mode-config] voice-modes]
    (println (str "• " (:name mode-config) " " (:emoji mode-config) " (" (name mode-key) ")"))
    (println (str "  " (:personality mode-config)))
    (println (str "  " (:tone mode-config)))
    (println ""))

(defn generate-comment
  "Generate a comment in the current voice mode"
  [content]
  (let [current-mode (get-current-mode)
        mode-key (keyword current-mode)
        mode-config (get voice-modes mode-key)]
    (if mode-config
      (let [example (rand-nth (:examples mode-config))
            prefix (str (:emoji mode-config) " " (:name mode-config) "'s voice: ")]
        (str prefix "\"" example "\"\n\n" content))
      content)))

(defn generate-documentation
  "Generate documentation in the current voice mode"
  [title content]
  (let [current-mode (get-current-mode)
        mode-key (keyword current-mode)
        mode-config (get voice-modes mode-key)]
    (if mode-config
      (let [intro (case mode-key
                    :trish (str "**" (:name mode-config) "'s enthusiastic guidance**: *\"" 
                               (rand-nth (:examples mode-config)) "\"*")
                    :glow (str "**" (:name mode-config) "'s technical analysis**: *\"" 
                             (rand-nth (:examples mode-config)) "\"*"))]
        (str "# " title "\n\n" intro "\n\n" content))
      (str "# " title "\n\n" content))))

(defn apply-mode-to-file
  "Apply the current voice mode to a file"
  [file-path]
  (if (fs/exists? file-path)
    (let [content (slurp file-path)
          current-mode (get-current-mode)
          mode-key (keyword current-mode)
          mode-config (get voice-modes mode-key)]
      (if mode-config
        (let [;; Add voice mode header
              header (str "<!-- Grainmode: " (:name mode-config) " " (:emoji mode-config) " -->\n")
              ;; Transform content based on mode
              transformed-content (case mode-key
                                    :trish (str/replace content 
                                                       #"(\*\*.*?\*\*)" 
                                                       (fn [match] (str match " ✨")))
                                    :glow (str/replace content 
                                                      #"(\*\*.*?\*\*)" 
                                                      (fn [match] (str match " 🔥"))))
              result (str header transformed-content)]
          (spit file-path result)
          (println (str "✅ Applied " (:name mode-config) " mode to " file-path)))
        (println "❌ Unknown mode: " current-mode)))
    (println (str "❌ File not found: " file-path))))

(defn create-mode-template
  "Create a template file with the current voice mode"
  [template-path content]
  (let [current-mode (get-current-mode)
        mode-key (keyword current-mode)
        mode-config (get voice-modes mode-key)]
    (if mode-config
      (let [template-content (str "<!-- Grainmode Template: " (:name mode-config) " " (:emoji mode-config) " -->\n"
                                 "<!-- Generated: " (java.time.Instant/now) " -->\n\n"
                                 content)]
        (spit template-path template-content)
        (println (str "✅ Created template with " (:name mode-config) " mode: " template-path)))
      (println "❌ Unknown mode: " current-mode))))

(defn main
  "Main grainmode function"
  [& args]
  (let [command (first args)]
    (case command
      "set" (let [mode (second args)]
              (set-mode mode))
      
      "get" (let [current-mode (get-current-mode)]
              (println (str "Current grainmode: " current-mode))
              (get-mode-info current-mode))
      
      "info" (let [mode (second args)]
               (get-mode-info mode))
      
      "list" (list-modes)
      
      "comment" (let [content (str/join " " (rest args))]
                  (println (generate-comment content)))
      
      "doc" (let [title (second args)
                  content (str/join " " (drop 2 args))]
              (println (generate-documentation title content)))
      
      "apply" (let [file-path (second args)]
                (apply-mode-to-file file-path))
      
      "template" (let [template-path (second args)
                       content (str/join " " (drop 2 args))]
                   (create-mode-template template-path content))
      
      (do
        (println "🎭 Grainmode: AI Voice Mode Utility")
        (println "")
        (println "Usage:")
        (println "  bb grainmode/core.bb set <mode>")
        (println "  bb grainmode/core.bb get")
        (println "  bb grainmode/core.bb info <mode>")
        (println "  bb grainmode/core.bb list")
        (println "  bb grainmode/core.bb comment <content>")
        (println "  bb grainmode/core.bb doc <title> <content>")
        (println "  bb grainmode/core.bb apply <file-path>")
        (println "  bb grainmode/core.bb template <path> <content>")
        (println "")
        (println "Examples:")
        (println "  bb grainmode/core.bb set trish")
        (println "  bb grainmode/core.bb get")
        (println "  bb grainmode/core.bb info glow")
        (println "  bb grainmode/core.bb comment 'This is amazing!'")
        (println "  bb grainmode/core.bb doc 'README' 'Welcome to our project'")
        (println "  bb grainmode/core.bb apply README.md")
        (println "")
        (println "Current mode: " (get-current-mode))))))

;; Run main function if called directly
(when (= *file* (str *ns*))
  (apply main *command-line-args*))

