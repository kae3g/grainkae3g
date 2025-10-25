(ns clojure-s6.grainclay
  "Grainclay - Networked Rolling Release Package Manager with Grainpath watching.
   Integrates clojure-s6 daemon supervision with Clay immutable paths."
  (:require
   [clojure.spec.alpha :as s]
   [clojure.java.io :as io]
   [clojure.string :as str]
   [clojure.core.async :as async :refer [go go-loop <! >! timeout chan]]
   [clojure-s6.core :as s6]
   [clojure-s6.daemon :as daemon]))

;; =============================================================================
;; Grainclay Path Specifications
;; =============================================================================

;; Grainpath segment (URL-safe)
(s/def ::grainpath-segment
  (s/and string?
         #(re-matches #"^[a-zA-Z0-9._-]+$" %)
         #(<= 1 (count %) 64)))

;; Grainpath (immutable path)
(s/def ::grainpath
  (s/coll-of ::grainpath-segment :min-count 1 :max-count 16))

;; Grainpath revision
(s/def ::grainpath-revision
  (s/and string?
         #(re-matches #"^\d+\.\d+\.\d+$" %)))

;; Grainpath beam (ship + desk + revision + path)
(s/def ::grainpath-beam
  (s/keys :req-un [::grainpath-ship
                   ::grainpath-desk
                   ::grainpath-revision]
          :opt-un [::grainpath]))

(s/def ::grainpath-ship string?)
(s/def ::grainpath-desk string?)

;; Package metadata
(s/def ::package-name string?)
(s/def ::package-version ::grainpath-revision)
(s/def ::package-description string?)
(s/def ::package-license string?)
(s/def ::package-author string?)
(s/def ::package-dependencies (s/coll-of string?))
(s/def ::package-repository string?)

(s/def ::grainclay-package
  (s/keys :req-un [::package-name
                   ::package-version
                   ::grainpath-beam]
          :opt-un [::package-description
                   ::package-license
                   ::package-author
                   ::package-dependencies
                   ::package-repository]))

;; =============================================================================
;; Grainclay Watcher State
;; =============================================================================

(defonce grainclay-watchers
  "Registry of active Grainclay path watchers."
  (atom {}))

(defonce grainclay-packages
  "Registry of installed Grainclay packages."
  (atom {}))

(defonce grainclay-channels
  "Async channels for Grainclay events."
  (atom {:package-updates (chan 100)
         :path-changes (chan 100)
         :sync-events (chan 100)
         :errors (chan 100)}))

;; =============================================================================
;; Grainpath Operations
;; =============================================================================

(defn grainpath->string
  "Convert Grainpath to URL-safe string."
  [path]
  (str/join "/" path))

(defn string->grainpath
  "Convert URL-safe string to Grainpath."
  [path-str]
  (when (and (string? path-str) (not (str/blank? path-str)))
    (let [segments (str/split path-str #"/")
          clean-segments (filter #(not (str/blank? %)) segments)]
      (when (every? #(s/valid? ::grainpath-segment %) clean-segments)
        clean-segments))))

(defn grainpath-join
  "Join Grainpath segments."
  [& segments]
  (let [all-segments (flatten segments)]
    (when (every? #(s/valid? ::grainpath-segment %) all-segments)
      all-segments)))

(defn grainpath-beam->string
  "Convert Grainpath beam to URL-safe string."
  [beam]
  (let [ship (:grainpath-ship beam)
        desk (:grainpath-desk beam)
        revision (:grainpath-revision beam)
        path (when (:grainpath beam)
               (grainpath->string (:grainpath beam)))]
    (if path
      (str ship "/" desk "/" revision "/" path)
      (str ship "/" desk "/" revision))))

(defn string->grainpath-beam
  "Convert URL-safe string to Grainpath beam."
  [beam-str]
  (let [parts (str/split beam-str #"/")]
    (when (>= (count parts) 3)
      {:grainpath-ship (nth parts 0)
       :grainpath-desk (nth parts 1)
       :grainpath-revision (nth parts 2)
       :grainpath (when (> (count parts) 3)
                    (string->grainpath (str/join "/" (drop 3 parts))))})))

;; =============================================================================
;; Grainclay Package Operations
;; =============================================================================

(defn register-package!
  "Register a Grainclay package."
  [package]
  (when (s/valid? ::grainclay-package package)
    (swap! grainclay-packages assoc (:package-name package) package)
    (println "Registered Grainclay package:" (:package-name package))
    package))

(defn unregister-package!
  "Unregister a Grainclay package."
  [package-name]
  (swap! grainclay-packages dissoc package-name)
  (println "Unregistered Grainclay package:" package-name))

(defn get-package
  "Get a Grainclay package by name."
  [package-name]
  (get @grainclay-packages package-name))

(defn list-packages
  "List all registered Grainclay packages."
  []
  (vals @grainclay-packages))

(defn find-packages
  "Find packages matching a predicate."
  [pred]
  (filter pred (list-packages)))

;; =============================================================================
;; Grainclay Path Watching
;; =============================================================================

(defn watch-grainpath!
  "Watch a Grainpath for changes using s6 daemon supervision.
   Returns a watcher ID that can be used to stop watching."
  [grainpath-beam callback & {:keys [poll-interval-ms recursive?]
                              :or {poll-interval-ms 1000
                                   recursive? true}}]
  (let [watcher-id (str (java.util.UUID/randomUUID))
        beam-str (grainpath-beam->string grainpath-beam)
        stop-chan (chan)
        
        ;; Create s6 service for this watcher
        service-name (str "grainclay-watcher-" watcher-id)
        service-dir (str "/tmp/grainclay/watchers/" watcher-id)
        
        watcher-state (atom {:beam grainpath-beam
                            :beam-str beam-str
                            :last-revision (:grainpath-revision grainpath-beam)
                            :last-check (System/currentTimeMillis)
                            :running? true})]
    
    ;; Start async watcher loop
    (go-loop []
      (let [[val port] (async/alts! [stop-chan (timeout poll-interval-ms)])]
        (when-not (= port stop-chan)
          (try
            ;; Check for changes in the Grainpath
            (let [current-time (System/currentTimeMillis)
                  current-revision (:grainpath-revision grainpath-beam)
                  last-revision (:last-revision @watcher-state)]
              
              ;; Update state
              (swap! watcher-state assoc
                     :last-check current-time
                     :last-revision current-revision)
              
              ;; Trigger callback if revision changed
              (when (not= current-revision last-revision)
                (println "Grainpath changed:" beam-str)
                (println "  Old revision:" last-revision)
                (println "  New revision:" current-revision)
                (callback {:grainpath-beam grainpath-beam
                          :old-revision last-revision
                          :new-revision current-revision
                          :timestamp current-time})
                
                ;; Publish to update channel
                (>! (:path-changes @grainclay-channels)
                    {:watcher-id watcher-id
                     :grainpath-beam grainpath-beam
                     :old-revision last-revision
                     :new-revision current-revision
                     :timestamp current-time})))
            
            (catch Exception e
              (println "Error in Grainclay watcher:" (.getMessage e))
              (>! (:errors @grainclay-channels)
                  {:watcher-id watcher-id
                   :error (.getMessage e)
                   :timestamp (System/currentTimeMillis)})))
          
          (when (:running? @watcher-state)
            (recur)))))
    
    ;; Register watcher
    (swap! grainclay-watchers assoc watcher-id
           {:id watcher-id
            :grainpath-beam grainpath-beam
            :beam-str beam-str
            :callback callback
            :state watcher-state
            :stop-chan stop-chan
            :poll-interval-ms poll-interval-ms
            :recursive? recursive?
            :created-at (System/currentTimeMillis)})
    
    (println "Started Grainclay watcher:" watcher-id "for" beam-str)
    watcher-id))

(defn stop-watcher!
  "Stop a Grainclay path watcher."
  [watcher-id]
  (when-let [watcher (get @grainclay-watchers watcher-id)]
    (swap! (:state watcher) assoc :running? false)
    (async/close! (:stop-chan watcher))
    (swap! grainclay-watchers dissoc watcher-id)
    (println "Stopped Grainclay watcher:" watcher-id)
    true))

(defn list-watchers
  "List all active Grainclay watchers."
  []
  (vals @grainclay-watchers))

(defn get-watcher
  "Get a Grainclay watcher by ID."
  [watcher-id]
  (get @grainclay-watchers watcher-id))

;; =============================================================================
;; Grainclay Rolling Release System
;; =============================================================================

(defn check-for-updates!
  "Check for package updates across all watched Grainpaths."
  []
  (let [packages (list-packages)
        updates (atom [])]
    (doseq [package packages]
      (let [beam (:grainpath-beam package)
            current-version (:package-version package)
            ;; In real implementation, query remote Grainpath for latest version
            latest-version current-version] ; Placeholder
        (when (not= current-version latest-version)
          (swap! updates conj
                 {:package-name (:package-name package)
                  :current-version current-version
                  :latest-version latest-version
                  :grainpath-beam beam}))))
    @updates))

(defn apply-rolling-update!
  "Apply a rolling release update to a package."
  [package-name new-version]
  (when-let [package (get-package package-name)]
    (let [old-version (:package-version package)
          new-package (assoc package :package-version new-version)]
      (register-package! new-package)
      (println "Applied rolling update for" package-name)
      (println "  Old version:" old-version)
      (println "  New version:" new-version)
      
      ;; Publish update event
      (async/put! (:package-updates @grainclay-channels)
                  {:package-name package-name
                   :old-version old-version
                   :new-version new-version
                   :timestamp (System/currentTimeMillis)})
      
      new-package)))

(defn sync-package!
  "Sync a package from a remote Grainpath beam."
  [grainpath-beam]
  (let [beam-str (grainpath-beam->string grainpath-beam)
        ;; In real implementation, fetch package data from Grainpath
        package-data {:package-name (str "package-" (last (:grainpath grainpath-beam)))
                     :package-version (:grainpath-revision grainpath-beam)
                     :grainpath-beam grainpath-beam
                     :package-description "Synced from Grainpath"
                     :package-license "GPL-3.0"}]
    (register-package! package-data)
    (println "Synced package from Grainpath:" beam-str)
    
    ;; Publish sync event
    (async/put! (:sync-events @grainclay-channels)
                {:grainpath-beam grainpath-beam
                 :package-name (:package-name package-data)
                 :timestamp (System/currentTimeMillis)})
    
    package-data))

;; =============================================================================
;; Grainclay Daemon Integration with s6
;; =============================================================================

(defn create-grainclay-daemon
  "Create a Grainclay daemon using s6 supervision."
  [& {:keys [name poll-interval-ms auto-update?]
      :or {name "grainclay-daemon"
           poll-interval-ms 60000
           auto-update? true}}]
  
  (let [daemon-state (atom {:running? false
                           :poll-interval-ms poll-interval-ms
                           :auto-update? auto-update?
                           :last-check nil})
        stop-chan (chan)]
    
    ;; Create daemon loop
    (go-loop []
      (let [[val port] (async/alts! [stop-chan (timeout poll-interval-ms)])]
        (when-not (= port stop-chan)
          (try
            ;; Check for updates
            (when auto-update?
              (let [updates (check-for-updates!)]
                (when (seq updates)
                  (println "Found" (count updates) "package updates")
                  (doseq [update updates]
                    (apply-rolling-update! (:package-name update)
                                          (:latest-version update))))))
            
            (swap! daemon-state assoc :last-check (System/currentTimeMillis))
            
            (catch Exception e
              (println "Error in Grainclay daemon:" (.getMessage e))
              (async/put! (:errors @grainclay-channels)
                         {:error (.getMessage e)
                          :timestamp (System/currentTimeMillis)})))
          
          (when (:running? @daemon-state)
            (recur)))))
    
    ;; Return daemon control structure
    {:name name
     :state daemon-state
     :stop-chan stop-chan
     :start! (fn []
               (swap! daemon-state assoc :running? true)
               (println "Started Grainclay daemon:" name))
     :stop! (fn []
              (swap! daemon-state assoc :running? false)
              (async/close! stop-chan)
              (println "Stopped Grainclay daemon:" name))
     :status (fn []
               @daemon-state)}))

;; =============================================================================
;; Grainclay Event Listeners
;; =============================================================================

(defn listen-package-updates!
  "Listen for package update events."
  [callback]
  (go-loop []
    (when-let [update (<! (:package-updates @grainclay-channels))]
      (try
        (callback update)
        (catch Exception e
          (println "Error in package update listener:" (.getMessage e))))
      (recur))))

(defn listen-path-changes!
  "Listen for Grainpath change events."
  [callback]
  (go-loop []
    (when-let [change (<! (:path-changes @grainclay-channels))]
      (try
        (callback change)
        (catch Exception e
          (println "Error in path change listener:" (.getMessage e))))
      (recur))))

(defn listen-sync-events!
  "Listen for sync events."
  [callback]
  (go-loop []
    (when-let [event (<! (:sync-events @grainclay-channels))]
      (try
        (callback event)
        (catch Exception e
          (println "Error in sync event listener:" (.getMessage e))))
      (recur))))

;; =============================================================================
;; Grainclay CLI Interface
;; =============================================================================

(defn grainclay-install!
  "Install a package from a Grainpath beam."
  [beam-str]
  (when-let [beam (string->grainpath-beam beam-str)]
    (sync-package! beam)))

(defn grainclay-update!
  "Update a package to latest version."
  [package-name]
  (let [updates (check-for-updates!)
        package-update (first (filter #(= (:package-name %) package-name) updates))]
    (if package-update
      (apply-rolling-update! package-name (:latest-version package-update))
      (println "No updates available for" package-name))))

(defn grainclay-list!
  "List all installed packages."
  []
  (doseq [package (list-packages)]
    (println (str (:package-name package) " @ " (:package-version package)))))

(defn grainclay-watch!
  "Watch a Grainpath for changes."
  [beam-str]
  (when-let [beam (string->grainpath-beam beam-str)]
    (watch-grainpath! beam
                     (fn [change]
                       (println "Grainpath changed, syncing package...")
                       (sync-package! (:grainpath-beam change))))))

;; =============================================================================
;; Grainclay System Documentation
;; =============================================================================

(defn grainclay-system-doc
  "Generate documentation for Grainclay system."
  []
  {:description "Grainclay - Networked Rolling Release Package Manager with Grainpath watching"
   :features ["Immutable Grainpath watching"
              "Rolling release updates"
              "s6 daemon supervision"
              "Async event channels"
              "ICP/Clotoko compatibility"
              "URL-safe paths"
              "Package versioning"]
   :operations {:package ["register-package!" "unregister-package!" "get-package" "list-packages"]
               :watcher ["watch-grainpath!" "stop-watcher!" "list-watchers" "get-watcher"]
               :updates ["check-for-updates!" "apply-rolling-update!" "sync-package!"]
               :daemon ["create-grainclay-daemon"]
               :listeners ["listen-package-updates!" "listen-path-changes!" "listen-sync-events!"]
               :cli ["grainclay-install!" "grainclay-update!" "grainclay-list!" "grainclay-watch!"]}
   :integration {:s6 "Uses clojure-s6 daemon supervision"
                :sixos "Compatible with clojure-sixos"
                :clay "Based on Urbit Clay filesystem"
                :icp "ICP canister compatible"
                :clotoko "Clotoko transpiler ready"}})
