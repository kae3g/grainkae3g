(ns grainweb.daemon.core
  "Grainweb daemon for peer discovery, subscription management, and routing.
   Uses clojure-s6 and clojure-sixos for process supervision and system integration."
  (:require
   [clojure.spec.alpha :as s]
   [clojure.core.async :as async]
   [clojure.data.json :as json]
   [clojure.java.io :as io]
   [clojure.string :as str]
   [clojure.set :as set]
   [clojure.tools.logging :as log]
   [clojure-s6.core :as s6]
   [clojure-sixos.core :as sixos]
   [grainweb.daemon.peer-discovery :as peer-discovery]
   [grainweb.daemon.subscriptions :as subscriptions]
   [grainweb.daemon.routing :as routing]
   [grainweb.daemon.paths :as paths]))

;; =============================================================================
;; Core Daemon State and Configuration
;; =============================================================================

(defonce daemon-state
  (atom
   {:status :stopped
    :config nil
    :peer-index {}
    :subscriptions {:friends #{}
                    :following #{}}
    :routing-table {}
    :channels {:peer-discovery (async/chan 1000)
               :subscriptions (async/chan 1000)
               :routing (async/chan 1000)
               :control (async/chan 100)})
   :validator #(s/valid? :grainweb.daemon/state %)))

;; =============================================================================
;; Spec Definitions (Hoon-inspired path data structures)
;; =============================================================================

;; Path data structures inspired by Hoon's URL-safe path system
(s/def ::path-segment
  (s/and string?
          #(re-matches #"[a-zA-Z0-9._-]+" %)
          #(<= 1 (count %) 64)))

(s/def ::path
  (s/coll-of ::path-segment :kind vector? :min-count 1))

(s/def ::url-safe-path
  (s/and string?
          #(re-matches #"^/[a-zA-Z0-9._-]+(/[a-zA-Z0-9._-]+)*$" %)))

(s/def ::peer-id
  (s/and string?
          #(re-matches #"^[a-zA-Z0-9]{32,64}$" %)))

(s/def ::ip-address
  (s/or :ipv4 #(re-matches #"^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$" %)
        :ipv6 #(re-matches #"^[0-9a-fA-F:]+$" %)))

(s/def ::port
  (s/and int? #(<= 1 % 65535)))

(s/def ::endpoint
  (s/keys :req-un [::ip-address ::port]))

(s/def ::peer-info
  (s/keys :req-un [::peer-id ::endpoint]
          :opt-un [::path ::last-seen ::capabilities]))

(s/def ::subscription-type
  #{:friends :following :custom})

(s/def ::subscription
  (s/keys :req-un [::subscription-type ::path ::peer-id]
          :opt-un [::created-at ::last-updated ::active?]))

(s/def ::routing-entry
  (s/keys :req-un [::path ::peer-id ::endpoint]
          :opt-un [::hops ::last-used ::reliability]))

(s/def ::daemon-config
  (s/keys :req-un [::peer-id ::endpoint]
          :opt-un [::discovery-interval ::subscription-timeout ::max-peers]))

(s/def ::daemon-state
  (s/keys :req-un [::status ::peer-index ::subscriptions ::routing-table]
          :opt-un [::config ::channels]))

;; =============================================================================
;; Core Daemon Functions
;; =============================================================================

(defn init-daemon
  "Initialize the Grainweb daemon with configuration."
  [config]
  (log/info "Initializing Grainweb daemon with config:" config)
  (when (s/valid? ::daemon-config config)
    (swap! daemon-state assoc
           :config config
           :status :initializing)
    (log/info "Daemon initialized successfully")
    true))

(defn start-daemon
  "Start the Grainweb daemon and all subsystems."
  []
  (log/info "Starting Grainweb daemon...")
  (try
    (swap! daemon-state assoc :status :starting)
    
    ;; Start peer discovery
    (peer-discovery/start-discovery @daemon-state)
    
    ;; Start subscription manager
    (subscriptions/start-subscription-manager @daemon-state)
    
    ;; Start routing system
    (routing/start-routing-system @daemon-state)
    
    ;; Start main event loop
    (start-event-loop)
    
    (swap! daemon-state assoc :status :running)
    (log/info "Grainweb daemon started successfully")
    true
    (catch Exception e
      (log/error e "Failed to start Grainweb daemon")
      (swap! daemon-state assoc :status :error)
      false)))

(defn stop-daemon
  "Stop the Grainweb daemon and all subsystems."
  []
  (log/info "Stopping Grainweb daemon...")
  (try
    (swap! daemon-state assoc :status :stopping)
    
    ;; Stop all subsystems
    (peer-discovery/stop-discovery)
    (subscriptions/stop-subscription-manager)
    (routing/stop-routing-system)
    
    ;; Close all channels
    (doseq [[_ channel] (:channels @daemon-state)]
      (async/close! channel))
    
    (swap! daemon-state assoc :status :stopped)
    (log/info "Grainweb daemon stopped successfully")
    true
    (catch Exception e
      (log/error e "Failed to stop Grainweb daemon")
      false)))

(defn start-event-loop
  "Main event loop for processing daemon events."
  []
  (async/go-loop []
    (let [control-chan (:control (:channels @daemon-state))
          [event channel] (async/alts! [
                                        control-chan
                                        (:peer-discovery (:channels @daemon-state))
                                        (:subscriptions (:channels @daemon-state))
                                        (:routing (:channels @daemon-state))])]
      (when event
        (case channel
          control-chan (handle-control-event event)
          (:peer-discovery (:channels @daemon-state)) (handle-peer-discovery-event event)
          (:subscriptions (:channels @daemon-state)) (handle-subscription-event event)
          (:routing (:channels @daemon-state)) (handle-routing-event event))
        (recur)))))

;; =============================================================================
;; Event Handlers
;; =============================================================================

(defn handle-control-event
  "Handle control events for daemon management."
  [event]
  (log/debug "Handling control event:" event)
  (case (:type event)
    :shutdown (stop-daemon)
    :reload-config (reload-config (:config event))
    :status-query (send-status-response)
    :default (log/warn "Unknown control event type:" (:type event))))

(defn handle-peer-discovery-event
  "Handle peer discovery events."
  [event]
  (log/debug "Handling peer discovery event:" event)
  (case (:type event)
    :peer-found (add-peer (:peer event))
    :peer-lost (remove-peer (:peer-id event))
    :peer-updated (update-peer (:peer event))
    :default (log/warn "Unknown peer discovery event type:" (:type event))))

(defn handle-subscription-event
  "Handle subscription events."
  [event]
  (log/debug "Handling subscription event:" event)
  (case (:type event)
    :subscribe (add-subscription (:subscription event))
    :unsubscribe (remove-subscription (:subscription event))
    :subscription-update (update-subscription (:subscription event))
    :default (log/warn "Unknown subscription event type:" (:type event))))

(defn handle-routing-event
  "Handle routing events."
  [event]
  (log/debug "Handling routing event:" event)
  (case (:type event)
    :route-update (update-routing-table (:route event))
    :route-request (handle-route-request (:request event))
    :default (log/warn "Unknown routing event type:" (:type event))))

;; =============================================================================
;; Peer Management
;; =============================================================================

(defn add-peer
  "Add a new peer to the peer index."
  [peer-info]
  (when (s/valid? ::peer-info peer-info)
    (swap! daemon-state update :peer-index assoc (:peer-id peer-info) peer-info)
    (log/info "Added peer:" (:peer-id peer-info))
    (notify-peer-added peer-info)))

(defn remove-peer
  "Remove a peer from the peer index."
  [peer-id]
  (swap! daemon-state update :peer-index dissoc peer-id)
  (log/info "Removed peer:" peer-id)
  (notify-peer-removed peer-id))

(defn update-peer
  "Update peer information in the peer index."
  [peer-info]
  (when (s/valid? ::peer-info peer-info)
    (swap! daemon-state update :peer-index assoc (:peer-id peer-info) peer-info)
    (log/debug "Updated peer:" (:peer-id peer-info))))

(defn get-peer
  "Get peer information by peer ID."
  [peer-id]
  (get-in @daemon-state [:peer-index peer-id]))

(defn list-peers
  "List all known peers."
  []
  (vals (:peer-index @daemon-state)))

;; =============================================================================
;; Subscription Management
;; =============================================================================

(defn add-subscription
  "Add a new subscription."
  [subscription]
  (when (s/valid? ::subscription subscription)
    (let [key (keyword (str (:subscription-type subscription) "-" (:peer-id subscription)))]
      (swap! daemon-state update-in [:subscriptions (:subscription-type subscription)] conj subscription)
      (log/info "Added subscription:" key)
      (notify-subscription-added subscription))))

(defn remove-subscription
  "Remove a subscription."
  [subscription]
  (let [key (keyword (str (:subscription-type subscription) "-" (:peer-id subscription)))]
    (swap! daemon-state update-in [:subscriptions (:subscription-type subscription)] disj subscription)
    (log/info "Removed subscription:" key)
    (notify-subscription-removed subscription)))

(defn update-subscription
  "Update an existing subscription."
  [subscription]
  (when (s/valid? ::subscription subscription)
    (let [key (keyword (str (:subscription-type subscription) "-" (:peer-id subscription)))]
      (swap! daemon-state update-in [:subscriptions (:subscription-type subscription)] 
             (fn [subs] (conj (disj subs subscription) subscription)))
      (log/debug "Updated subscription:" key))))

(defn get-subscriptions
  "Get all subscriptions of a specific type."
  [subscription-type]
  (get-in @daemon-state [:subscriptions subscription-type]))

(defn get-subscriptions-for-peer
  "Get all subscriptions for a specific peer."
  [peer-id]
  (let [all-subs (vals (:subscriptions @daemon-state))]
    (filter #(= (:peer-id %) peer-id) (apply concat all-subs))))

;; =============================================================================
;; Routing Management
;; =============================================================================

(defn update-routing-table
  "Update the routing table with new route information."
  [route]
  (when (s/valid? ::routing-entry route)
    (swap! daemon-state update :routing-table assoc (:path route) route)
    (log/debug "Updated routing table for path:" (:path route))))

(defn find-route
  "Find a route for a given path."
  [path]
  (get (:routing-table @daemon-state) path))

(defn find-best-route
  "Find the best route for a given path based on reliability and hops."
  [path]
  (let [routes (filter #(= (:path %) path) (vals (:routing-table @daemon-state)))]
    (when (seq routes)
      (->> routes
           (sort-by (juxt :hops :reliability))
           first))))

;; =============================================================================
;; Notification System
;; =============================================================================

(defn notify-peer-added
  "Notify about a peer being added."
  [peer-info]
  (async/>!! (:control (:channels @daemon-state))
             {:type :peer-added :peer peer-info}))

(defn notify-peer-removed
  "Notify about a peer being removed."
  [peer-id]
  (async/>!! (:control (:channels @daemon-state))
             {:type :peer-removed :peer-id peer-id}))

(defn notify-subscription-added
  "Notify about a subscription being added."
  [subscription]
  (async/>!! (:control (:channels @daemon-state))
             {:type :subscription-added :subscription subscription}))

(defn notify-subscription-removed
  "Notify about a subscription being removed."
  [subscription]
  (async/>!! (:control (:channels @daemon-state))
             {:type :subscription-removed :subscription subscription}))

;; =============================================================================
;; Utility Functions
;; =============================================================================

(defn reload-config
  "Reload daemon configuration."
  [new-config]
  (when (s/valid? ::daemon-config new-config)
    (swap! daemon-state assoc :config new-config)
    (log/info "Configuration reloaded")))

(defn send-status-response
  "Send current daemon status."
  []
  (let [status {:status (:status @daemon-state)
                :peer-count (count (:peer-index @daemon-state))
                :subscription-count (reduce + (map count (vals (:subscriptions @daemon-state))))
                :route-count (count (:routing-table @daemon-state))}]
    (async/>!! (:control (:channels @daemon-state))
               {:type :status-response :data status})))

(defn get-daemon-status
  "Get current daemon status."
  []
  {:status (:status @daemon-state)
   :peer-count (count (:peer-index @daemon-state))
   :subscription-count (reduce + (map count (vals (:subscriptions @daemon-state))))
   :route-count (count (:routing-table @daemon-state))
   :config (:config @daemon-state)})

;; =============================================================================
;; s6 Integration
;; =============================================================================

(defn s6-run
  "s6 run script for the Grainweb daemon."
  []
  (log/info "Starting Grainweb daemon via s6")
  (let [config (load-config)]
    (when (init-daemon config)
      (start-daemon)
      ;; Keep the process running
      (while (= (:status @daemon-state) :running)
        (Thread/sleep 1000)))))

(defn load-config
  "Load daemon configuration from file."
  []
  (try
    (let [config-file (io/file "config/grainweb-daemon.edn")]
      (if (.exists config-file)
        (read-string (slurp config-file))
        (default-config)))
    (catch Exception e
      (log/error e "Failed to load configuration, using defaults")
      (default-config))))

(defn default-config
  "Default daemon configuration."
  []
  {:peer-id (str (java.util.UUID/randomUUID))
   :endpoint {:ip-address "127.0.0.1" :port 8080}
   :discovery-interval 30000
   :subscription-timeout 300000
   :max-peers 1000})

;; =============================================================================
;; Main Entry Point
;; =============================================================================

(defn -main
  "Main entry point for the Grainweb daemon."
  [& args]
  (log/info "Starting Grainweb daemon...")
  (s6-run))

