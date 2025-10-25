(ns grainweb.daemon.peer-discovery
  "Peer discovery system for finding and indexing Grainweb peers.
   Implements DHT-like discovery with path-based routing."
  (:require
   [clojure.spec.alpha :as s]
   [clojure.core.async :as async]
   [clojure.tools.logging :as log]
   [clojure.string :as str]
   [clojure.set :as set]
   [clojure.java.io :as io]
   [clojure.data.json :as json]
   [clojure-s6.core :as s6]
   [clojure-sixos.core :as sixos]
   [grainweb.daemon.paths :as paths]))

;; =============================================================================
;; Spec Definitions
;; =============================================================================

(s/def ::discovery-method
  #{:dht :mdns :bootstrap :manual})

(s/def ::peer-capability
  #{:routing :storage :computation :messaging :subscriptions})

(s/def ::peer-metadata
  (s/keys :req-un [::peer-id ::endpoint ::capabilities]
          :opt-un [::last-seen ::reliability ::paths]))

(s/def ::discovery-query
  (s/keys :req-un [::query-type ::target]
          :opt-un [::hops ::timeout ::callback]))

(s/def ::discovery-response
  (s/keys :req-un [::query-id ::results]
          :opt-un [::partial? ::next-hops]))

;; =============================================================================
;; Peer Discovery State
;; =============================================================================

(defonce discovery-state
  (atom
   {:active? false
    :bootstrap-peers #{}
    :discovery-methods #{:dht :mdns}
    :query-timeout 30000
    :max-hops 5
    :active-queries {}
    :discovery-interval 30000}))

;; =============================================================================
;; Core Discovery Functions
;; =============================================================================

(defn start-discovery
  "Start the peer discovery system."
  [daemon-state]
  (log/info "Starting peer discovery system...")
  (swap! discovery-state assoc :active? true)
  
  ;; Start discovery methods
  (start-dht-discovery)
  (start-mdns-discovery)
  (start-bootstrap-discovery)
  
  ;; Start discovery loop
  (start-discovery-loop daemon-state)
  
  (log/info "Peer discovery system started"))

(defn stop-discovery
  "Stop the peer discovery system."
  []
  (log/info "Stopping peer discovery system...")
  (swap! discovery-state assoc :active? false)
  (log/info "Peer discovery system stopped"))

(defn start-discovery-loop
  "Main discovery loop for periodic peer discovery."
  [daemon-state]
  (async/go-loop []
    (when (:active? @discovery-state)
      (try
        ;; Discover new peers
        (discover-peers daemon-state)
        
        ;; Clean up old queries
        (cleanup-expired-queries)
        
        ;; Wait for next discovery cycle
        (async/<! (async/timeout (:discovery-interval @discovery-state)))
        (recur)
        (catch Exception e
          (log/error e "Error in discovery loop")
          (async/<! (async/timeout 5000))
          (recur))))))

;; =============================================================================
;; DHT Discovery
;; =============================================================================

(defn start-dht-discovery
  "Start DHT-based peer discovery."
  []
  (log/info "Starting DHT discovery...")
  (async/go-loop []
    (when (:active? @discovery-state)
      (try
        ;; Query DHT for peers
        (query-dht-for-peers)
        (async/<! (async/timeout 60000)) ; Query every minute
        (recur)
        (catch Exception e
          (log/error e "Error in DHT discovery")
          (async/<! (async/timeout 30000))
          (recur))))))

(defn query-dht-for-peers
  "Query DHT for new peers."
  []
  (let [query-id (str (java.util.UUID/randomUUID))
        query {:query-id query-id
               :query-type :find-peers
               :target "grainweb-peers"
               :hops 0
               :timeout 30000}]
    (swap! discovery-state update :active-queries assoc query-id query)
    (log/debug "Querying DHT for peers:" query-id)))

;; =============================================================================
;; mDNS Discovery
;; =============================================================================

(defn start-mdns-discovery
  "Start mDNS-based peer discovery."
  []
  (log/info "Starting mDNS discovery...")
  (async/go-loop []
    (when (:active? @discovery-state)
      (try
        ;; Broadcast our presence
        (broadcast-presence)
        
        ;; Listen for other peers
        (listen-for-peers)
        
        (async/<! (async/timeout 30000)) ; Check every 30 seconds
        (recur)
        (catch Exception e
          (log/error e "Error in mDNS discovery")
          (async/<! (async/timeout 15000))
          (recur))))))

(defn broadcast-presence
  "Broadcast our presence via mDNS."
  []
  (log/debug "Broadcasting presence via mDNS...")
  ;; Implementation would use mDNS to broadcast _grainweb._tcp service
  )

(defn listen-for-peers
  "Listen for peer announcements via mDNS."
  []
  (log/debug "Listening for peer announcements via mDNS...")
  ;; Implementation would listen for _grainweb._tcp service announcements
  )

;; =============================================================================
;; Bootstrap Discovery
;; =============================================================================

(defn start-bootstrap-discovery
  "Start bootstrap peer discovery."
  []
  (log/info "Starting bootstrap discovery...")
  (let [bootstrap-peers (load-bootstrap-peers)]
    (swap! discovery-state assoc :bootstrap-peers bootstrap-peers)
    (doseq [peer bootstrap-peers]
      (query-bootstrap-peer peer))))

(defn load-bootstrap-peers
  "Load bootstrap peers from configuration."
  []
  (try
    (let [config-file (io/file "config/bootstrap-peers.edn")]
      (if (.exists config-file)
        (read-string (slurp config-file))
        (default-bootstrap-peers)))
    (catch Exception e
      (log/error e "Failed to load bootstrap peers, using defaults")
      (default-bootstrap-peers))))

(defn default-bootstrap-peers
  "Default bootstrap peers."
  []
  [{:peer-id "bootstrap-1"
    :endpoint {:ip-address "127.0.0.1" :port 8081}
    :capabilities #{:routing :messaging}}
   {:peer-id "bootstrap-2"
    :endpoint {:ip-address "127.0.0.1" :port 8082}
    :capabilities #{:routing :storage}}])

(defn query-bootstrap-peer
  "Query a bootstrap peer for other peers."
  [bootstrap-peer]
  (async/go
    (try
      (log/debug "Querying bootstrap peer:" (:peer-id bootstrap-peer))
      ;; Implementation would send discovery query to bootstrap peer
      (catch Exception e
        (log/error e "Failed to query bootstrap peer:" (:peer-id bootstrap-peer))))))

;; =============================================================================
;; Peer Discovery Logic
;; =============================================================================

(defn discover-peers
  "Discover new peers using all available methods."
  [daemon-state]
  (log/debug "Discovering peers...")
  (let [current-peers (set (keys (:peer-index daemon-state)))
        discovered-peers (discover-peers-via-methods)]
    (doseq [peer discovered-peers]
      (when-not (contains? current-peers (:peer-id peer))
        (announce-peer-found peer daemon-state)))))

(defn discover-peers-via-methods
  "Discover peers using all configured methods."
  []
  (let [methods (:discovery-methods @discovery-state)
        results (atom [])]
    (doseq [method methods]
      (case method
        :dht (swap! results into (discover-peers-dht))
        :mdns (swap! results into (discover-peers-mdns))
        :bootstrap (swap! results into (discover-peers-bootstrap))
        :manual (swap! results into (discover-peers-manual))))
    @results))

(defn discover-peers-dht
  "Discover peers via DHT."
  []
  (log/debug "Discovering peers via DHT...")
  ;; Implementation would query DHT for peer records
  [])

(defn discover-peers-mdns
  "Discover peers via mDNS."
  []
  (log/debug "Discovering peers via mDNS...")
  ;; Implementation would scan mDNS for _grainweb._tcp services
  [])

(defn discover-peers-bootstrap
  "Discover peers via bootstrap nodes."
  []
  (log/debug "Discovering peers via bootstrap...")
  (let [bootstrap-peers (:bootstrap-peers @discovery-state)]
    (mapcat query-bootstrap-peer-for-peers bootstrap-peers)))

(defn discover-peers-manual
  "Discover peers via manual configuration."
  []
  (log/debug "Discovering peers via manual configuration...")
  ;; Implementation would load manually configured peers
  [])

(defn query-bootstrap-peer-for-peers
  "Query a bootstrap peer for its peer list."
  [bootstrap-peer]
  (log/debug "Querying bootstrap peer for peers:" (:peer-id bootstrap-peer))
  ;; Implementation would send peer list request to bootstrap peer
  [])

;; =============================================================================
;; Peer Announcement
;; =============================================================================

(defn announce-peer-found
  "Announce that a new peer has been found."
  [peer-info daemon-state]
  (log/info "Peer found:" (:peer-id peer-info))
  (let [event {:type :peer-found :peer peer-info}]
    (async/>!! (:peer-discovery (:channels daemon-state)) event)))

(defn announce-peer-lost
  "Announce that a peer has been lost."
  [peer-id daemon-state]
  (log/info "Peer lost:" peer-id)
  (let [event {:type :peer-lost :peer-id peer-id}]
    (async/>!! (:peer-discovery (:channels daemon-state)) event)))

;; =============================================================================
;; Query Management
;; =============================================================================

(defn cleanup-expired-queries
  "Clean up expired discovery queries."
  []
  (let [now (System/currentTimeMillis)
        expired-query-ids (->> (:active-queries @discovery-state)
                               (filter (fn [[_ query]]
                                         (> now (+ (:timestamp query) (:timeout query)))))
                               (map key))]
    (doseq [query-id expired-query-ids]
      (swap! discovery-state update :active-queries dissoc query-id)
      (log/debug "Cleaned up expired query:" query-id))))

(defn handle-discovery-response
  "Handle a discovery response from another peer."
  [response]
  (when (s/valid? ::discovery-response response)
    (let [query-id (:query-id response)
          query (get (:active-queries @discovery-state) query-id)]
      (when query
        (process-discovery-results (:results response))
        (swap! discovery-state update :active-queries dissoc query-id)))))

(defn process-discovery-results
  "Process discovery results and update peer index."
  [results]
  (doseq [peer-info results]
    (when (s/valid? ::peer-metadata peer-info)
      (announce-peer-found peer-info @daemon-state))))

;; =============================================================================
;; Path-based Discovery
;; =============================================================================

(defn discover-peers-for-path
  "Discover peers that can handle a specific path."
  [path]
  (log/debug "Discovering peers for path:" path)
  (let [query {:query-type :find-peers-for-path
               :target path
               :hops 0
               :timeout 30000}]
    (execute-discovery-query query)))

(defn execute-discovery-query
  "Execute a discovery query."
  [query]
  (let [query-id (str (java.util.UUID/randomUUID))
        query-with-id (assoc query :query-id query-id :timestamp (System/currentTimeMillis))]
    (swap! discovery-state update :active-queries assoc query-id query-with-id)
    (log/debug "Executing discovery query:" query-id)
    query-id))

;; =============================================================================
;; Capability Discovery
;; =============================================================================

(defn discover-peers-with-capability
  "Discover peers that have a specific capability."
  [capability]
  (log/debug "Discovering peers with capability:" capability)
  (let [query {:query-type :find-peers-with-capability
               :target capability
               :hops 0
               :timeout 30000}]
    (execute-discovery-query query)))

(defn get-peer-capabilities
  "Get capabilities of a specific peer."
  [peer-id]
  (let [peer (get-peer peer-id)]
    (when peer
      (:capabilities peer))))

(defn peer-has-capability?
  "Check if a peer has a specific capability."
  [peer-id capability]
  (let [capabilities (get-peer-capabilities peer-id)]
    (contains? capabilities capability)))

;; =============================================================================
;; Utility Functions
;; =============================================================================

(defn get-peer
  "Get peer information by ID."
  [peer-id]
  (get-in @daemon-state [:peer-index peer-id]))

(defn update-peer-reliability
  "Update peer reliability based on successful interactions."
  [peer-id success?]
  (when-let [peer (get-peer peer-id)]
    (let [current-reliability (or (:reliability peer) 0.5)
          new-reliability (if success?
                           (min 1.0 (+ current-reliability 0.1))
                           (max 0.0 (- current-reliability 0.1)))]
      (update-peer (assoc peer :reliability new-reliability)))))

(defn get-most-reliable-peers
  "Get the most reliable peers."
  [limit]
  (->> (vals (:peer-index @daemon-state))
       (sort-by :reliability >)
       (take limit)))

(defn get-peers-for-path
  "Get peers that can handle a specific path."
  [path]
  (->> (vals (:peer-index @daemon-state))
       (filter #(paths/peer-handles-path? % path))))

;; =============================================================================
;; Configuration
;; =============================================================================

(defn update-discovery-config
  "Update discovery configuration."
  [new-config]
  (swap! discovery-state merge new-config)
  (log/info "Discovery configuration updated"))

(defn get-discovery-status
  "Get current discovery status."
  []
  {:active? (:active? @discovery-state)
   :methods (:discovery-methods @discovery-state)
   :bootstrap-peers (count (:bootstrap-peers @discovery-state))
   :active-queries (count (:active-queries @discovery-state))
   :discovery-interval (:discovery-interval @discovery-state)})

