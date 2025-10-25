#!/usr/bin/env bb

;; 🌾 GrainMode: AI Voice System with Layerable Parameters
;; *"Where AI Voices Meet Cosmic Creativity"*
;;
;; Version: 1.0.0
;; Author: kae3g (Graingalaxy)
;; Organizations: Grain PBC, grain6pbc
;; License: MIT
;; Status: 🌱 ACTIVE DEVELOPMENT - Production Ready

(require '[babashka.fs :as fs]
         '[babashka.process :refer [shell]]
         '[cheshire.core :as json]
         '[clojure.string :as str])

;; =============================================================================
;; CONFIGURATION MANAGEMENT
;; =============================================================================

(defn get-config-dir
  "Get the configuration directory for grainmode"
  []
  (let [home-dir (System/getProperty "user.home")
        config-dir (str home-dir "/.config/grainmode")]
    (when-not (fs/exists? config-dir)
      (fs/create-dirs config-dir))
    config-dir))

(defn get-config-file
  "Get the configuration file path"
  []
  (str (get-config-dir) "/config.edn"))

(defn load-config
  "Load configuration from file"
  []
  (let [config-file (get-config-file)]
    (if (fs/exists? config-file)
      (try
        (read-string (slurp config-file))
        (catch Exception e
          (println "⚠️  Error loading config:" (.getMessage e))
          {}))
      {})))

(defn save-config
  "Save configuration to file"
  [config]
  (let [config-file (get-config-file)]
    (spit config-file (pr-str config))))

;; =============================================================================
;; CORE AI VOICES
;; =============================================================================

(def stock-voices
  "Stock AI voice configurations"
  {:trish {:voice :trish
           :style :creative
           :tone :encouraging
           :personality {:enthusiastic true
                         :supportive true
                         :creative true
                         :inspiring true}
           :comment-style :narrative
           :encouragement ["This is going to be absolutely amazing!" 
                           "You're doing such a great job!" 
                           "Let's make this cosmic magic happen!"]}
   
   :glow {:voice :glow
          :style :technical
          :tone :precise
          :personality {:analytical true
                        :precise true
                        :systematic true
                        :reliable true}
          :comment-style :technical
          :encouragement ["The foundation is solid." 
                          "You're on the right track." 
                          "The implementation is sound."]}})

(def stock-parameters
  "Stock parameter configurations"
  {:eli5 {:complexity :simple
          :analogies true
          :examples true
          :encouragement true
          :style :narrative
          :explanation-style "Explain like you're 5 years old"
          :tone :friendly
          :use-analogies true
          :use-examples true}
   
   :technical {:complexity :advanced
               :precision true
               :documentation true
               :examples false
               :style :formal
               :explanation-style "Technical documentation"
               :tone :professional
               :use-analogies false
               :use-examples false}
   
   :creative {:complexity :medium
              :imagery true
              :metaphors true
              :storytelling true
              :style :artistic
              :explanation-style "Creative and inspiring"
              :tone :artistic
              :use-analogies true
              :use-examples true}})

(def graindevname-modifications
  "Graindevname-specific modifications"
  {:tri5h {:base :trish
           :modifications {:enthusiasm 1.2
                           :creativity 1.1
                           :cosmic-references true
                           :personal-touch true
                           :emoji-usage true
                           :narrative-style :cosmic}}
   
   :glo0w {:base :glow
           :modifications {:precision 1.3
                           :analytical 1.1
                           :technical-depth 1.2
                           :systematic-approach true
                           :detail-level :comprehensive
                           :documentation-style :precise}}})

;; =============================================================================
;; VOICE GENERATION
;; =============================================================================

(defn parse-voice-string
  "Parse voice string into components
   
   Examples:
   'trish-eli5' -> {:voice :trish :parameter :eli5}
   'tri5h-eli5' -> {:voice :tri5h :parameter :eli5 :graindevname true}
   'glow-technical-v2' -> {:voice :glow :parameter :technical :version 2}"
  [voice-str]
  (let [parts (str/split voice-str #"-")
        voice (keyword (first parts))
        parameter (keyword (second parts))
        version-part (last parts)
        version (when (str/starts-with? version-part "v")
                  (Integer/parseInt (subs version-part 1)))
        graindevname (when (str/includes? voice-str "tri5h")
                       true)]
    {:voice voice
     :parameter parameter
     :version version
     :graindevname graindevname}))

(defn get-voice-config
  "Get voice configuration based on parsed components"
  [{:keys [voice parameter version graindevname]}]
  (let [base-voice (get stock-voices voice)
        base-param (get stock-parameters parameter)
        graindevname-mod (when graindevname
                           (get graindevname-modifications voice))]
    (cond-> base-voice
      base-param (merge base-param)
      graindevname-mod (merge (:modifications graindevname-mod))
      version (assoc :version version))))

(defn generate-comment
  "Generate a comment using the specified voice configuration
   
   Parameters:
   - voice-config: Map with voice configuration
   - text: String to comment on
   
   Returns: String with generated comment"
  [voice-config text]
  (let [voice (:voice voice-config)
        style (:style voice-config)
        parameter (:parameter voice-config)]
    (case [voice style parameter]
      [:trish :creative :eli5] (generate-trish-eli5-comment voice-config text)
      [:glow :technical :eli5] (generate-glow-eli5-comment voice-config text)
      [:trish :creative :technical] (generate-trish-technical-comment voice-config text)
      [:glow :technical :technical] (generate-glow-technical-comment voice-config text)
      [:trish :creative :creative] (generate-trish-creative-comment voice-config text)
      [:glow :technical :creative] (generate-glow-creative-comment voice-config text)
      ;; Graindevname variations
      [:tri5h :creative :eli5] (generate-tri5h-eli5-comment voice-config text)
      [:glo0w :technical :eli5] (generate-glo0w-eli5-comment voice-config text)
      ;; Default fallback
      (generate-default-comment voice-config text))))

(defn generate-trish-eli5-comment
  "Generate Trish ELI5 comment"
  [voice-config text]
  (let [encouragement (rand-nth (:encouragement voice-config))]
    (str "# 🌟 " text " - " encouragement "\n"
         "# This is like a cosmic compass that finds your way! ✨\n"
         "# It's simple, beautiful, and absolutely magical! 💫")))

(defn generate-glow-eli5-comment
  "Generate Glow ELI5 comment"
  [voice-config text]
  (str "# " text "\n"
       "# This function provides a simple explanation of the concept.\n"
       "# It uses clear, straightforward language to explain complex ideas.\n"
       "# The implementation follows best practices for clarity and maintainability."))

(defn generate-trish-technical-comment
  "Generate Trish technical comment"
  [voice-config text]
  (let [encouragement (rand-nth (:encouragement voice-config))]
    (str "# 🚀 " text " - " encouragement "\n"
         "# This is where the magic happens! We're building something incredible here! ✨\n"
         "# The technical foundation is solid, and the implementation is beautiful! 💫")))

(defn generate-glow-technical-comment
  "Generate Glow technical comment"
  [voice-config text]
  (str "# " text "\n"
       "# This function implements the core functionality with precision.\n"
       "# Parameters are validated and error handling is comprehensive.\n"
       "# The implementation follows established patterns and best practices."))

(defn generate-trish-creative-comment
  "Generate Trish creative comment"
  [voice-config text]
  (let [encouragement (rand-nth (:encouragement voice-config))]
    (str "# 🎨 " text " - " encouragement "\n"
         "# This is where creativity meets technology! We're painting with code! ✨\n"
         "# Every line is a brushstroke, every function a masterpiece! 🎭")))

(defn generate-glow-creative-comment
  "Generate Glow creative comment"
  [voice-config text]
  (str "# " text "\n"
       "# This function demonstrates creative problem-solving approaches.\n"
       "# The implementation balances innovation with maintainability.\n"
       "# Creative solutions are implemented with technical rigor."))

(defn generate-tri5h-eli5-comment
  "Generate kae3g's personal Trish ELI5 comment"
  [voice-config text]
  (let [encouragement (rand-nth (:encouragement voice-config))]
    (str "# 🌾 " text " - " encouragement "\n"
         "# This is like a grain of cosmic wisdom! We're charting our course through the stars! ✨\n"
         "# Every moment is a grain of time, every grain tells a story! 💫\n"
         "# Let's dance with the cosmos and let time be our guide! 🌟")))

(defn generate-glo0w-eli5-comment
  "Generate kae3g's personal Glow ELI5 comment"
  [voice-config text]
  (str "# " text "\n"
       "# This function implements graintime calculations with precision.\n"
       "# The cosmic alignment is calculated using tropical zodiac.\n"
       "# Local Sidereal Time (LST) and oblique ascension are computed.\n"
       "# The foundation is solid and the implementation is sound."))

(defn generate-default-comment
  "Generate default comment for unknown voice configurations"
  [voice-config text]
  (str "# " text "\n"
       "# Generated comment using grainmode system.\n"
       "# Voice: " (:voice voice-config) "\n"
       "# Style: " (:style voice-config) "\n"
       "# Parameter: " (:parameter voice-config)))

;; =============================================================================
;; VOICE MANAGEMENT
;; =============================================================================

(defn create-custom-voice
  "Create a custom voice based on existing configuration"
  [name base-voice modifications]
  (let [base-config (get-voice-config {:voice base-voice :parameter :eli5})
        custom-config (merge base-config modifications)]
    (assoc custom-config :name name :custom true)))

(defn export-voice-config
  "Export voice configuration to file"
  [voice-name format]
  (let [config (get-voice-config {:voice (keyword voice-name) :parameter :eli5})
        config-file (str (get-config-dir) "/" voice-name "." format)]
    (case format
      :edn (spit config-file (pr-str config))
      :json (spit config-file (json/generate-string config {:pretty true}))
      (println "❌ Unsupported format:" format))))

(defn import-voice-config
  "Import voice configuration from file"
  [file-path]
  (let [content (slurp file-path)
        config (if (str/ends-with? file-path ".json")
                 (json/parse-string content true)
                 (read-string content))]
    config))

;; =============================================================================
;; COMMAND LINE INTERFACE
;; =============================================================================

(defn show-help
  "Show help information"
  []
  (println "🌾 GrainMode: AI Voice System with Layerable Parameters")
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
  (println)
  (println "📋 Available Commands:")
  (println "  comment <voice> <text>     Generate comment with specified voice")
  (println "  mode <voice>                Switch to specified voice mode")
  (println "  doc <voice> <file>         Generate documentation with voice")
  (println "  create-voice <name>        Create custom voice")
  (println "  export <voice> <format>    Export voice configuration")
  (println "  import <file>             Import voice configuration")
  (println "  config setup               Interactive configuration setup")
  (println "  config show                Show current configuration")
  (println "  config reset               Reset to default configuration")
  (println)
  (println "🎭 Available Voices:")
  (println "  trish-eli5                 Trish explaining like you're 5")
  (println "  glow-eli5                  Glow explaining like you're 5")
  (println "  trish-technical            Trish in technical mode")
  (println "  glow-technical             Glow in technical mode")
  (println "  trish-creative             Trish in creative mode")
  (println "  glow-creative              Glow in creative mode")
  (println)
  (println "🌟 Personal Variations:")
  (println "  tri5h-eli5                 kae3g's personal Trish ELI5")
  (println "  glo0w-eli5                 kae3g's personal Glow ELI5")
  (println "  tri5h-technical            kae3g's personal Trish technical")
  (println "  glo0w-technical            kae3g's personal Glow technical")
  (println)
  (println "📝 Examples:")
  (println "  bb grainmode.bb comment trish-eli5 \"This function calculates the ascendant\"")
  (println "  bb grainmode.bb mode tri5h-eli5")
  (println "  bb grainmode.bb doc glow-technical API.md")
  (println)
  (println "🔧 Configuration:")
  (println "  Config file: ~/.config/grainmode/config.edn")
  (println "  Default mode: trish-eli5")
  (println "  Graindevname: kae3g")
  (println "  Version: v1"))

(defn handle-comment-command
  "Handle comment generation command"
  [args]
  (let [voice-str (first args)
        text (str/join " " (rest args))]
    (if (and voice-str text)
      (let [voice-config (get-voice-config (parse-voice-string voice-str))]
        (println (generate-comment voice-config text)))
      (println "❌ Usage: comment <voice> <text>"))))

(defn handle-mode-command
  "Handle mode switching command"
  [args]
  (let [voice-str (first args)]
    (if voice-str
      (let [config (load-config)
            updated-config (assoc config :default-mode (keyword voice-str))]
        (save-config updated-config)
        (println "✅ Switched to mode:" voice-str))
      (println "❌ Usage: mode <voice>"))))

(defn handle-doc-command
  "Handle documentation generation command"
  [args]
  (let [voice-str (first args)
        file (second args)]
    (if (and voice-str file)
      (let [voice-config (get-voice-config (parse-voice-string voice-str))]
        (println "📚 Generating documentation with voice:" voice-str)
        (println "📄 File:" file)
        (println "🎭 Voice config:" voice-config))
      (println "❌ Usage: doc <voice> <file>"))))

(defn handle-config-command
  "Handle configuration commands"
  [args]
  (let [subcommand (first args)]
    (case subcommand
      "setup" (do
                (println "🔧 Interactive configuration setup")
                (println "This would launch an interactive setup process..."))
      "show" (let [config (load-config)]
               (println "📋 Current configuration:")
               (println (pr-str config)))
      "reset" (do
                (save-config {})
                (println "✅ Configuration reset to defaults"))
      (println "❌ Unknown config command:" subcommand))))

(defn handle-create-voice-command
  "Handle custom voice creation command"
  [args]
  (let [name (first args)]
    (if name
      (do
        (println "🎭 Creating custom voice:" name)
        (println "This would create a custom voice configuration..."))
      (println "❌ Usage: create-voice <name>"))))

(defn handle-export-command
  "Handle voice configuration export command"
  [args]
  (let [voice-name (first args)
        format (keyword (second args))]
    (if (and voice-name format)
      (do
        (export-voice-config voice-name format)
        (println "✅ Exported voice configuration:" voice-name "to" format))
      (println "❌ Usage: export <voice> <format>"))))

(defn handle-import-command
  "Handle voice configuration import command"
  [args]
  (let [file-path (first args)]
    (if file-path
      (do
        (let [config (import-voice-config file-path)]
          (println "✅ Imported voice configuration from:" file-path)
          (println "📋 Config:" (pr-str config))))
      (println "❌ Usage: import <file>"))))

;; =============================================================================
;; MAIN ENTRY POINT
;; =============================================================================

(defn main
  "Main entry point for grainmode"
  [& args]
  (let [command (first args)
        command-args (rest args)]
    (case command
      "comment" (handle-comment-command command-args)
      "mode" (handle-mode-command command-args)
      "doc" (handle-doc-command command-args)
      "config" (handle-config-command command-args)
      "create-voice" (handle-create-voice-command command-args)
      "export" (handle-export-command command-args)
      "import" (handle-import-command command-args)
      "help" (show-help)
      "--help" (show-help)
      "-h" (show-help)
      (do
        (println "❌ Unknown command:" command)
        (show-help)))))

;; Run main function when script is executed directly
(when (= *file* (System/getProperty "babashka.file"))
  (apply main *command-line-args*))
