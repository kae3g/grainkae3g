#!/usr/bin/env bb
(ns grainsynonym.core
  "Grainsynonym: Voice synonym utility for Grainmode
  
  This utility provides synonym mapping and transformation functions
  for different AI voice modes. It helps maintain consistency across
  repositories and enables easy voice mode switching."
  (:require [clojure.string :as str]))

;; Synonym mappings for different voice modes
(def voice-synonyms
  {:trish {:greetings ["Hello beautiful soul!" "Hey there, gorgeous!" "Hi sweetheart!" "Welcome, lovely!"]
           :encouragement ["This is going to be absolutely amazing!" "You're doing such a great job!" "I'm so proud of you!" "Keep up the fantastic work!"]
           :excitement ["Ooh, that's such a cute idea!" "I love how you're thinking about this!" "This is going to be so much fun!" "Let's make it absolutely gorgeous!"]
           :support ["Don't worry, we'll figure this out together!" "You're not alone in this!" "I believe in you!" "We've got this!"]
           :celebration ["Yay! You did it!" "This is absolutely wonderful!" "I'm so excited for you!" "You should be proud!"]
           :technical ["Let's make this work beautifully!" "This will be so elegant!" "I love how clean this looks!" "This is going to be amazing!"]
           :problem-solving ["Let's think about this creatively!" "Maybe we could try a different approach!" "What if we made it more fun?" "I have an idea!"]
           :endings ["Let's make magic together!" "You're absolutely amazing!" "Keep being wonderful!" "Sending you lots of love!"]}
   
   :glow {:greetings ["Alright, let's get started." "You're in the right place." "Let's break this down." "Time to work."]
          :encouragement ["The foundation is solid." "You're on the right track." "This approach will work." "Good progress."]
          :excitement ["The structure is solid, but we need to tighten up the logic here." "This is where real work gets done." "Let's optimize this further." "Here's what needs improvement."]
          :support ["Don't panic! Most problems have logical solutions." "Let's work through this systematically." "Focus on the technical issue." "Here's how to fix it."]
          :celebration ["Well done." "Good work." "That's efficient." "Solid implementation."]
          :technical ["The architecture is clean and maintainable." "This follows best practices." "The performance is solid." "This is well-structured."]
          :problem-solving ["Let's analyze what we have and optimize it." "Here's the root cause." "This is the most efficient solution." "Let's implement this systematically."]
          :endings ["Let's build something that lasts." "Keep pushing forward." "Focus on quality." "Build for the long term."]}})

(defn get-synonym
  "Get a random synonym for a category in the current voice mode"
  [category mode]
  (let [mode-key (keyword (str/lower-case mode))
        synonyms (get-in voice-synonyms [mode-key category])]
    (if synonyms
      (rand-nth synonyms)
      (str "[" category " synonym not found for mode " mode "]"))))

(defn transform-text
  "Transform text to match the current voice mode"
  [text mode]
  (let [mode-key (keyword (str/lower-case mode))
        synonyms (get voice-synonyms mode-key)]
    (if synonyms
      (let [;; Replace common phrases with mode-specific synonyms
            transformed (case mode-key
                          :trish (-> text
                                     (str/replace #"Hello" "Hello beautiful soul!")
                                     (str/replace #"Hi" "Hey there, gorgeous!")
                                     (str/replace #"Good job" "You're doing such a great job!")
                                     (str/replace #"Well done" "This is absolutely wonderful!")
                                     (str/replace #"Let's" "Let's make it absolutely gorgeous!")
                                     (str/replace #"This is" "This is going to be absolutely amazing!"))
                          :glow (-> text
                                    (str/replace #"Hello" "Alright, let's get started.")
                                    (str/replace #"Hi" "You're in the right place.")
                                    (str/replace #"Good job" "The foundation is solid.")
                                    (str/replace #"Well done" "Well done.")
                                    (str/replace #"Let's" "Let's optimize this further.")
                                    (str/replace #"This is" "The structure is solid, but we need to tighten up the logic here.")))]
        transformed)
      text)))

(defn generate-response
  "Generate a response in the specified voice mode"
  [prompt mode]
  (let [mode-key (keyword (str/lower-case mode))
        synonyms (get voice-synonyms mode-key)]
    (if synonyms
      (let [greeting (rand-nth (:greetings synonyms))
            encouragement (rand-nth (:encouragement synonyms))
            technical (rand-nth (:technical synonyms))
            ending (rand-nth (:endings synonyms))]
        (str greeting "\n\n" encouragement "\n\n" technical "\n\n" ending))
      (str "Unknown mode: " mode))))

(defn create-voice-template
  "Create a voice template for a specific mode"
  [mode template-type]
  (let [mode-key (keyword (str/lower-case mode))
        synonyms (get voice-synonyms mode-key)]
    (if synonyms
      (case template-type
        :readme (str "# " (:name (get {:trish {:name "Trish"} :glow {:name "Glow"}} mode-key)) "'s Guide\n\n"
                     (rand-nth (:greetings synonyms)) "\n\n"
                     "## Overview\n\n"
                     (rand-nth (:encouragement synonyms)) "\n\n"
                     "## Getting Started\n\n"
                     (rand-nth (:technical synonyms)) "\n\n"
                     "## Conclusion\n\n"
                     (rand-nth (:endings synonyms)))
        
        :comment (str "<!-- " (:name (get {:trish {:name "Trish"} :glow {:name "Glow"}} mode-key)) "'s voice: " 
                     (rand-nth (:excitement synonyms)) " -->\n")
        
        :documentation (str "**" (:name (get {:trish {:name "Trish"} :glow {:name "Glow"}} mode-key)) "'s guidance**: *\"" 
                           (rand-nth (:excitement synonyms)) "\"*\n\n")
        
        (str "Template type not found: " template-type))
      (str "Unknown mode: " mode))))

(defn list-synonyms
  "List all synonyms for a specific mode"
  [mode]
  (let [mode-key (keyword (str/lower-case mode))
        synonyms (get voice-synonyms mode-key)]
    (if synonyms
      (do
        (println (str "🎭 Synonyms for " mode " mode:"))
        (println "")
        (doseq [[category words] synonyms]
          (println (str "• " (name category) ":"))
          (doseq [word words]
            (println (str "  - \"" word "\"")))
          (println "")))
      (println (str "❌ Unknown mode: " mode)))))

(defn main
  "Main grainsynonym function"
  [& args]
  (let [command (first args)]
    (case command
      "get" (let [category (second args)
                  mode (nth args 2 "trish")]
              (println (get-synonym category mode)))
      
      "transform" (let [text (str/join " " (drop 2 args))
                        mode (second args)]
                    (println (transform-text text mode)))
      
      "response" (let [prompt (str/join " " (drop 2 args))
                       mode (second args)]
                   (println (generate-response prompt mode)))
      
      "template" (let [mode (second args)
                       template-type (keyword (nth args 2 "readme"))]
                   (println (create-voice-template mode template-type)))
      
      "list" (let [mode (second args)]
               (list-synonyms mode))
      
      (do
        (println "🎭 Grainsynonym: Voice Synonym Utility")
        (println "")
        (println "Usage:")
        (println "  bb grainsynonym/core.bb get <category> [mode]")
        (println "  bb grainsynonym/core.bb transform <mode> <text>")
        (println "  bb grainsynonym/core.bb response <mode> <prompt>")
        (println "  bb grainsynonym/core.bb template <mode> <type>")
        (println "  bb grainsynonym/core.bb list <mode>")
        (println "")
        (println "Categories: greetings, encouragement, excitement, support, celebration, technical, problem-solving, endings")
        (println "Modes: trish, glow")
        (println "Template types: readme, comment, documentation")
        (println "")
        (println "Examples:")
        (println "  bb grainsynonym/core.bb get greetings trish")
        (println "  bb grainsynonym/core.bb transform trish 'Hello, this is amazing!'")
        (println "  bb grainsynonym/core.bb response glow 'How do I start?'")
        (println "  bb grainsynonym/core.bb template trish readme")
        (println "  bb grainsynonym/core.bb list glow")))))

;; Run main function if called directly
(when (= *file* (str *ns*))
  (apply main *command-line-args*))

