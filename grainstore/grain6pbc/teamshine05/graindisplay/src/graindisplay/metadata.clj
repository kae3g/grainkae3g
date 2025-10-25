(ns graindisplay.metadata
  "GrainDisplay metadata for Grainclay files and Grainweb media messages.
   
   Enables content creators to specify intended display settings for their content,
   while giving local users full control to override with their preferences."
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            [clojure.tools.logging :as log]))

;; Display Metadata Schema for Grainclay/Grainweb content

(def display-metadata-schema
  "Schema for embedded display metadata in Grainmark files"
  {:version "1.0.0"
   :fields
   {:resolution {:type :vector
                 :format [width height]
                 :optional true
                 :description "Intended display resolution"}
    
    :scaling {:type :number
              :range [0.5 3.0]
              :optional true
              :description "Intended text/UI scaling factor"}
    
    :color-profile {:type :keyword
                    :values #{:srgb :adobe-rgb :display-p3 :rec2020 :grayscale :monochrome}
                    :optional true
                    :description "Intended color space/profile"}
    
    :color-temperature {:type :number
                        :range [1000 6500]
                        :unit "Kelvin"
                        :optional true
                        :description "Intended warm lighting temperature"}
    
    :brightness {:type :number
                 :range [0.0 1.0]
                 :optional true
                 :description "Intended brightness level"}
    
    :contrast {:type :number
               :range [0.0 2.0]
               :optional true
               :description "Intended contrast adjustment"}
    
    :display-mode {:type :keyword
                   :values #{:vision :ink :presentation :reading :color-blind-friendly}
                   :optional true
                   :description "Intended display mode"}
    
    :orientation {:type :keyword
                  :values #{:portrait :landscape :auto}
                  :optional true
                  :description "Intended screen orientation"}
    
    :filters {:type :vector
              :values #{:grayscale :monochrome :sepia :high-contrast :reduce-blue-light
                        :protanopia :deuteranopia :tritanopia}
              :optional true
              :description "Applied color filters for accessibility"}
    
    :immutable-path {:type :string
                     :format "grainclay-url-safe-neovedic-timestamp"
                     :required true
                     :description "Grainclay immutable path with neovedic timestamp"}
    
    :grainmark-name {:type :string
                     :format "global-immutable-grainmark-name"
                     :required true
                     :description "Public global immutable Grainmark identifier"}
    
    :author {:type :string
             :optional true
             :description "Content creator's Grainweb identity"}
    
    :created-at {:type :string
                 :format "neovedic-timestamp"
                 :optional true
                 :description "Content creation timestamp"}
    
    :intended-for {:type :vector
                   :values #{:desktop :mobile :tablet :e-ink :projector :graindroid-vision :graindroid-ink}
                   :optional true
                   :description "Intended viewing devices"}}})

(defn create-display-metadata
  "Create display metadata for Grainclay/Grainweb content
   
   Example:
   (create-display-metadata
     {:grainmark-name \"kae3g/forest-sunset-12025-10-22--1830--CDT--moon-vishakha\"
      :immutable-path \"12025-10-22--1830--CDT--moon-vishakha--09thhouse18--kae3g\"
      :color-temperature 2000
      :color-profile :display-p3
      :display-mode :reading
      :filters [:reduce-blue-light]
      :author \"kae3g\"
      :intended-for [:desktop :graindroid-ink]})"
  [{:keys [grainmark-name immutable-path color-temperature color-profile
           display-mode filters brightness contrast scaling resolution
           orientation author created-at intended-for]
    :as metadata}]
  (let [base-metadata
        {:version "1.0.0"
         :grainmark-name grainmark-name
         :immutable-path immutable-path
         :timestamp (or created-at (str (java.time.Instant/now)))}]
    (cond-> base-metadata
      color-temperature (assoc :color-temperature color-temperature)
      color-profile (assoc :color-profile color-profile)
      display-mode (assoc :display-mode display-mode)
      filters (assoc :filters filters)
      brightness (assoc :brightness brightness)
      contrast (assoc :contrast contrast)
      scaling (assoc :scaling scaling)
      resolution (assoc :resolution resolution)
      orientation (assoc :orientation orientation)
      author (assoc :author author)
      intended-for (assoc :intended-for intended-for))))

(defn parse-display-metadata
  "Parse embedded display metadata from Grainmark content"
  [metadata-string]
  (try
    (edn/read-string metadata-string)
    (catch Exception e
      (log/warn "Failed to parse display metadata:" (.getMessage e))
      nil)))

(defn validate-display-metadata
  "Validate display metadata against schema"
  [metadata]
  (let [errors (atom [])]
    ;; Check required fields
    (when-not (:grainmark-name metadata)
      (swap! errors conj "Missing required field: grainmark-name"))
    (when-not (:immutable-path metadata)
      (swap! errors conj "Missing required field: immutable-path"))
    
    ;; Validate ranges
    (when-let [temp (:color-temperature metadata)]
      (when-not (<= 1000 temp 6500)
        (swap! errors conj "color-temperature must be between 1000 and 6500")))
    
    (when-let [brightness (:brightness metadata)]
      (when-not (<= 0.0 brightness 1.0)
        (swap! errors conj "brightness must be between 0.0 and 1.0")))
    
    (when-let [scaling (:scaling metadata)]
      (when-not (<= 0.5 scaling 3.0)
        (swap! errors conj "scaling must be between 0.5 and 3.0")))
    
    {:valid? (empty? @errors)
     :errors @errors}))

(defn embed-display-metadata
  "Embed display metadata into Grainmark content as EDN comment"
  [content metadata]
  (let [metadata-str (pr-str metadata)
        header (str ";; GrainDisplay Metadata\n"
                    ";; " metadata-str "\n\n")]
    (str header content)))

(defn extract-display-metadata
  "Extract display metadata from Grainmark content"
  [content]
  (when-let [match (re-find #";; GrainDisplay Metadata\n;; (.*?)\n" content)]
    (parse-display-metadata (second match))))

;; Local User Preferences

(defn create-local-preferences
  "Create local user display preferences that override content metadata
   
   Example:
   (create-local-preferences
     {:honor-external-metadata false  ;; Ignore creator's intended settings
      :force-color-temperature 2000   ;; Always use warm lighting
      :force-filters [:grayscale]     ;; Always apply grayscale
      :force-scaling 1.75             ;; Always use 1.75x scaling
      :allow-list [:color-profile :display-mode]  ;; Only honor these fields
      :deny-list [:brightness :contrast]})        ;; Never honor these fields"
  [{:keys [honor-external-metadata force-color-temperature force-color-profile
           force-filters force-brightness force-contrast force-scaling
           force-display-mode allow-list deny-list]
    :or {honor-external-metadata true}}]
  {:honor-external-metadata honor-external-metadata
   :overrides (cond-> {}
                force-color-temperature (assoc :color-temperature force-color-temperature)
                force-color-profile (assoc :color-profile force-color-profile)
                force-filters (assoc :filters force-filters)
                force-brightness (assoc :brightness force-brightness)
                force-contrast (assoc :contrast force-contrast)
                force-scaling (assoc :scaling force-scaling)
                force-display-mode (assoc :display-mode force-display-mode))
   :allow-list (or allow-list [])
   :deny-list (or deny-list [])})

(defn apply-display-settings
  "Apply display settings respecting local user preferences
   
   Priority:
   1. Local user overrides (force-* settings)
   2. Content creator metadata (if honor-external-metadata = true)
   3. Local machine defaults"
  [content-metadata local-preferences local-defaults]
  (let [{:keys [honor-external-metadata overrides allow-list deny-list]} local-preferences
        
        ;; Start with local defaults
        settings (atom local-defaults)
        
        ;; Apply content metadata if honored
        _ (when honor-external-metadata
            (doseq [[k v] content-metadata]
              (when (and (not (deny-list k))
                        (or (empty? allow-list) (allow-list k)))
                (swap! settings assoc k v))))
        
        ;; Always apply local overrides
        _ (doseq [[k v] overrides]
            (swap! settings assoc k v))]
    
    @settings))

(defn settings->gnome
  "Convert GrainDisplay settings to GNOME gsettings commands"
  [settings]
  (let [commands (atom [])]
    (when-let [scaling (:scaling settings)]
      (swap! commands conj
             ["gsettings" "set" "org.gnome.desktop.interface" "text-scaling-factor" (str scaling)]))
    
    (when-let [temp (:color-temperature settings)]
      (swap! commands conj
             ["gsettings" "set" "org.gnome.settings-daemon.plugins.color" "night-light-temperature" (str temp)]
             ["gsettings" "set" "org.gnome.settings-daemon.plugins.color" "night-light-enabled" "true"]))
    
    @commands))

;; Grainmark URL Generation

(defn grainmark->grainclay-url
  "Convert Grainmark name to Grainclay immutable URL-safe path
   
   Example:
   kae3g/forest-sunset-12025-10-22--1830--CDT--moon-vishakha
   -> /12025-10-22--1830--CDT--moon-vishakha--09thhouse18--kae3g/forest-sunset.grainmark"
  [grainmark-name neovedic-timestamp]
  (let [[user content-name] (str/split grainmark-name #"/")
        url-safe-timestamp (str/replace neovedic-timestamp #"[^a-zA-Z0-9\-]" "-")
        path (str "/" url-safe-timestamp "--" user "/" content-name ".grainmark")]
    path))

(defn create-grainweb-display-message
  "Create a Grainweb message with embedded display metadata
   
   This creates an immutable Grainclay-addressed message with display settings"
  [{:keys [content author grainmark-name neovedic-timestamp display-settings]}]
  (let [metadata (create-display-metadata
                  (merge display-settings
                         {:grainmark-name grainmark-name
                          :immutable-path neovedic-timestamp
                          :author author}))
        grainclay-url (grainmark->grainclay-url grainmark-name neovedic-timestamp)
        full-content (embed-display-metadata content metadata)]
    {:grainmark-name grainmark-name
     :grainclay-url grainclay-url
     :neovedic-timestamp neovedic-timestamp
     :display-metadata metadata
     :content full-content
     :author author}))

(comment
  ;; Example: Create content with display metadata
  (def my-content
    (create-grainweb-display-message
     {:content "# Forest Sunset\n\nA warm evening in the redwoods..."
      :author "kae3g"
      :grainmark-name "kae3g/forest-sunset"
      :neovedic-timestamp "12025-10-22--1830--CDT--moon-vishakha--09thhouse18"
      :display-settings {:color-temperature 2000
                        :color-profile :display-p3
                        :display-mode :reading
                        :filters [:reduce-blue-light]
                        :intended-for [:desktop :graindroid-ink]}}))
  
  ;; Example: Local user preferences
  (def my-preferences
    (create-local-preferences
     {:honor-external-metadata false     ;; Ignore creator settings
      :force-color-temperature 2000      ;; Always warm
      :force-filters [:monochrome]}))    ;; Always monochrome
  
  ;; Example: Apply settings
  (apply-display-settings
   (:display-metadata my-content)
   my-preferences
   {:scaling 1.5 :brightness 0.8}))

