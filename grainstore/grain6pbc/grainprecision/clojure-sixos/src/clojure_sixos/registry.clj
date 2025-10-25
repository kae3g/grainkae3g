(ns clojure-sixos.registry
  "Package and service name registry with typo detection.
   
   Maintains a registry of canonical names with known typos and aliases.
   Provides fuzzy lookup capabilities for resilient name resolution."
  (:require [clojure-sixos.similarity :as sim]
            [clojure.spec.alpha :as s]))

;; =============================================================================
;; Specs
;; =============================================================================

(s/def ::canonical string?)
(s/def ::typos (s/coll-of string?))
(s/def ::aliases (s/coll-of string?))
(s/def ::category #{:transpiler :media :service :tool :library :hardware})

(s/def ::name-entry
  (s/keys :req [::canonical]
          :opt [::typos ::aliases ::category]))

;; =============================================================================
;; Global Registry
;; =============================================================================

(defonce ^:private name-registry
  "Global atom containing registered names and their metadata."
  (atom {}))

;; =============================================================================
;; Default Grain Network Names
;; =============================================================================

(def grain-network-names
  "Default names in the Grain Network with known typos and aliases."
  [{:canonical "clotoko"
    :typos ["clomoko" "clatoko" "clotokko" "clomokko"]
    :aliases ["clojure-motoko" "clj-motoko" "clj-icp"]
    :category :transpiler}
   
   {:canonical "grainmusic"
    :typos ["grainmusik" "grain-music" "grainmusick"]
    :aliases ["gmusic" "grain-audio"]
    :category :media}
   
   {:canonical "grainweb"
    :typos ["grain-web" "grainwebb"]
    :aliases ["gweb" "grain-browser"]
    :category :service}
   
   {:canonical "grainclay"
    :typos ["graincaly" "grain-clay" "grainclaay"]
    :aliases ["gclay" "grain-pkg" "grainpkg"]
    :category :service}
   
   {:canonical "grainwriter"
    :typos ["grain-writer" "grainwritter"]
    :aliases ["gwriter" "grain-editor"]
    :category :hardware}
   
   {:canonical "grainspace"
    :typos ["grain-space" "grainspce"]
    :aliases ["gspace"]
    :category :service}
   
   {:canonical "grainconv"
    :typos ["grain-conv" "grainconvert"]
    :aliases ["gconv" "grain-convert"]
    :category :tool}
   
   {:canonical "graincamera"
    :typos ["grain-camera" "graincam"]
    :aliases ["gcamera" "gcam"]
    :category :hardware}
   
   {:canonical "grainpack"
    :typos ["grain-pack" "grainpak"]
    :aliases ["gpack"]
    :category :hardware}
   
   {:canonical "clojure-s6"
    :typos ["clojure-sixos" "clojure_s6" "clj-s6"]
    :aliases ["clj-s6" "s6-clj"]
    :category :library}
   
   {:canonical "clojure-sixos"
    :typos ["clojure-6os" "clojure_sixos" "clj-sixos"]
    :aliases ["clj-sixos" "sixos-clj"]
    :category :library}
   
   {:canonical "clojure-icp"
    :typos ["clojure-dfinity" "clojure_icp" "clj-icp"]
    :aliases ["clj-icp" "icp-clj" "clojure-dfinity"]
    :category :library}
   
   {:canonical "grainphotos"
    :typos ["grain-photos" "grainpics"]
    :aliases ["gphotos" "grain-pics"]
    :category :media}
   
   {:canonical "grain6"
    :typos ["grain-6" "grain-six" "grainsix"]
    :aliases ["grainsix" "g6" "grain-s6"]
    :category :library}
   
   {:canonical "grainregistry"
    :typos ["grain-registry" "grainregister"]
    :aliases ["grainresolver" "graintypo" "gregistry"]
    :category :library}
   
   {:canonical "grainbarrel"
    :typos ["grain-barrel" "grainbarel"]
    :aliases ["gb" "gbarrel"]
    :category :tool}
   
   {:canonical "grainsource"
    :typos ["grain-source" "grainsrc"]
    :aliases ["gsource" "gsrc" "ggit"]
    :category :tool}
   
   {:canonical "graintime"
    :typos ["grain-time" "graintiime"]
    :aliases ["gt" "gtime"]
    :category :library}
   
   {:canonical "graindisplay"
    :typos ["grain-display" "graindisply"]
    :aliases ["gdisplay" "grain-warmth" "gwarm"]
    :category :tool}
   
   {:canonical "graindaemon"
    :typos ["grain-daemon" "graindaemn"]
    :aliases ["gdaemon" "gd"]
    :category :service}
   
   {:canonical "graincourse"
    :typos ["grain-course" "graincourse"]
    :aliases ["gcourse" "grain-edu"]
    :category :service}
   
   {:canonical "grainzsh"
    :typos ["grain-zsh" "grainshell"]
    :aliases ["gzsh" "grain-shell"]
    :category :tool}
   
   {:canonical "grainenvvars"
    :typos ["grain-env-vars" "grainenv"]
    :aliases ["genv" "grain-env"]
    :category :tool}
   
   {:canonical "grainwifi"
    :typos ["grain-wifi" "grainwfi"]
    :aliases ["gwifi" "grain-network"]
    :category :tool}
   
   {:canonical "graindevname"
    :typos ["grain-dev-name" "graindevnames"]
    :aliases ["gdevname" "grain-username"]
    :category :tool}
   
   {:canonical "grainaltproteinproject"
    :typos ["grain-alt-protein" "grainaltprotein"]
    :aliases ["galtprotein" "grain-food"]
    :category :service}
   
   {:canonical "grain-nightlight"
    :typos ["grainnightlight" "grain-night-light"]
    :aliases ["gnightlight" "grain-warmth"]
    :category :tool}
   
   {:canonical "grain-metatypes"
    :typos ["grainmetatypes" "grain-meta-types"]
    :aliases ["gmetatypes" "gmeta"]
    :category :library}
   
   {:canonical "grainneovedic"
    :typos ["grain-neovedic" "grainneovedik"]
    :aliases ["gneovedic" "grain-vedic"]
    :category :library}
   
   {:canonical "grainicons"
    :typos ["grain-icons" "grainicon"]
    :aliases ["gicons" "gicon"]
    :category :tool}
   
   {:canonical "graincasks"
    :typos ["grain-casks" "graincask"]
    :aliases ["gcasks" "grain-apps"]
    :category :tool}
   
   {:canonical "grainlexicon"
    :typos ["grain-lexicon" "graindict"]
    :aliases ["glexicon" "grain-dict"]
    :category :library}
   
   {:canonical "graindrive"
    :typos ["grain-drive" "graindrive"]
    :aliases ["gdrive" "grain-cloud"]
    :category :service}
   
   {:canonical "graindroid"
    :typos ["grain-droid" "graindroyd"]
    :aliases ["gdroid" "grain-android"]
    :category :hardware}
   
   {:canonical "clojure-photos"
    :typos ["clojure-photo" "clj-photos"]
    :aliases ["clj-photos" "photos-clj"]
    :category :library}])

;; =============================================================================
;; Registry Management
;; =============================================================================

(defn register-name
  "Register a canonical name with optional typos and aliases.
   
   Args:
     canonical - The canonical/correct name
     opts - Map with optional :typos, :aliases, :category
   
   Examples:
     (register-name \"clotoko\" {:typos [\"clomoko\"]
                                 :aliases [\"clojure-motoko\"]
                                 :category :transpiler})"
  [canonical {:keys [typos aliases category] :as opts}]
  {:pre [(string? canonical)]}
  (swap! name-registry assoc canonical
         {:canonical canonical
          :typos (or typos [])
          :aliases (or aliases [])
          :category category}))

(defn unregister-name
  "Remove a name from the registry.
   
   Examples:
     (unregister-name \"clotoko\")"
  [canonical]
  (swap! name-registry dissoc canonical))

(defn all-names
  "Get all registered canonical names.
   
   Examples:
     (all-names)
     => [\"clotoko\" \"grainmusic\" \"grainweb\" ...]"
  []
  (keys @name-registry))

(defn get-entry
  "Get the registry entry for a canonical name.
   
   Examples:
     (get-entry \"clotoko\")
     => {:canonical \"clotoko\" :typos [\"clomoko\"] ...}"
  [canonical]
  (get @name-registry canonical))

;; =============================================================================
;; Initialization
;; =============================================================================

(defn init-registry!
  "Initialize registry with Grain Network default names."
  []
  (doseq [{:keys [canonical typos aliases category]} grain-network-names]
    (register-name canonical {:typos typos
                              :aliases aliases
                              :category category})))

;; Auto-initialize on namespace load
(init-registry!)

;; =============================================================================
;; Lookup Functions
;; =============================================================================

(defn- find-by-typo
  "Find canonical name for a known typo.
   
   Returns {:canonical name :match-type :typo :confidence 1.0} if found."
  [name]
  (some (fn [[canonical entry]]
          (when (some #{name} (:typos entry))
            {:canonical canonical
             :match-type :typo
             :confidence 1.0}))
        @name-registry))

(defn- find-by-alias
  "Find canonical name for a known alias.
   
   Returns {:canonical name :match-type :alias :confidence 1.0} if found."
  [name]
  (some (fn [[canonical entry]]
          (when (some #{name} (:aliases entry))
            {:canonical canonical
             :match-type :alias
             :confidence 1.0}))
        @name-registry))

(defn- find-by-fuzzy-match
  "Find canonical name using fuzzy string matching.
   
   Returns {:canonical name :match-type :fuzzy :confidence score} if found."
  [name threshold]
  (let [candidates (all-names)
        match (sim/closest-match name candidates)]
    (when (and match
               (>= (:similarity match) threshold))
      {:canonical (:match match)
       :match-type :fuzzy
       :confidence (:similarity match)})))

(defn lookup
  "Lookup a name in the registry with fuzzy matching.
   
   Returns a map with :canonical, :match-type, and :confidence.
   
   Match types:
   - :exact - Exact match with canonical name
   - :typo - Match with known typo
   - :alias - Match with known alias
   - :fuzzy - Fuzzy string match
   - :not-found - No match found
   
   Args:
     name - The name to look up
     opts - Optional map with:
            :threshold - Minimum similarity for fuzzy match (default 0.75)
   
   Examples:
     (lookup \"clotoko\")
     => {:canonical \"clotoko\" :match-type :exact :confidence 1.0}
     
     (lookup \"clomoko\")
     => {:canonical \"clotoko\" :match-type :typo :confidence 1.0}
     
     (lookup \"grainmusik\")
     => {:canonical \"grainmusic\" :match-type :typo :confidence 1.0}"
  ([name] (lookup name {}))
  ([name {:keys [threshold] :or {threshold 0.75}}]
   ;; Try exact match first
   (if (get @name-registry name)
     {:canonical name
      :match-type :exact
      :confidence 1.0}
     ;; Try typo match
     (or (find-by-typo name)
         ;; Try alias match
         (find-by-alias name)
         ;; Try fuzzy match
         (find-by-fuzzy-match name threshold)
         ;; Not found
         {:canonical nil
          :match-type :not-found
          :confidence 0.0}))))

(defn package-exists?
  "Check if a package exists in the registry (exact match only).
   
   Examples:
     (package-exists? \"clotoko\") => true
     (package-exists? \"clomoko\") => false"
  [name]
  (contains? @name-registry name))

(defn resolve-canonical
  "Resolve a name to its canonical form.
   
   Returns the canonical name if found, or the original name if not.
   
   Examples:
     (resolve-canonical \"clomoko\") => \"clotoko\"
     (resolve-canonical \"grainmusik\") => \"grainmusic\"
     (resolve-canonical \"unknown\") => \"unknown\""
  [name]
  (let [result (lookup name)]
    (or (:canonical result) name)))

;; =============================================================================
;; Batch Operations
;; =============================================================================

(defn lookup-many
  "Lookup multiple names at once.
   
   Returns a map of {original-name => lookup-result}.
   
   Examples:
     (lookup-many [\"clomoko\" \"grainmusik\" \"grainweb\"])
     => {\"clomoko\" {:canonical \"clotoko\" :match-type :typo ...}
         \"grainmusik\" {:canonical \"grainmusic\" :match-type :typo ...}
         \"grainweb\" {:canonical \"grainweb\" :match-type :exact ...}}"
  [names]
  (into {} (map (fn [name] [name (lookup name)]) names)))

(defn resolve-many
  "Resolve multiple names to their canonical forms.
   
   Examples:
     (resolve-many [\"clomoko\" \"grainmusik\" \"grainweb\"])
     => [\"clotoko\" \"grainmusic\" \"grainweb\"]"
  [names]
  (mapv resolve-canonical names))

;; =============================================================================
;; Category Queries
;; =============================================================================

(defn by-category
  "Get all packages in a specific category.
   
   Examples:
     (by-category :transpiler) => [\"clotoko\"]
     (by-category :media) => [\"grainmusic\" \"grainphotos\"]"
  [category]
  (keep (fn [[canonical entry]]
          (when (= (:category entry) category)
            canonical))
        @name-registry))

(defn categories
  "Get all available categories.
   
   Examples:
     (categories) => [:transpiler :media :service :tool :library :hardware]"
  []
  (distinct (keep :category (vals @name-registry))))

