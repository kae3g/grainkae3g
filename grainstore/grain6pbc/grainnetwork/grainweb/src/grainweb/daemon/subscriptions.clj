(ns grainweb.daemon.subscriptions
  "Subscription management system for Grainweb friend and following subscriptions.
   Manages path-based subscriptions with Hoon-inspired path data structures."
  (:require
   [clojure.spec.alpha :as s]
   [clojure.core.async :as async]
   [clojure.tools.logging :as log]
   [clojure.string :as str]
   [clojure.set :as set]
   [clojure.data.json :as json]
   [clojure-s6.core :as s6]
   [clojure-sixos.core :as sixos]
   [grainweb.daemon.paths :as paths]))

;; =============================================================================
;; Spec Definitions
;; =============================================================================

(s/def ::subscription-id
  (s/and string?
          #(re-matches #"^[a-zA-Z0-9_-]{8,32}$" %)))

(s/def ::subscription-priority
  #{:low :normal :high :critical})

(s/def ::subscription-filter
  (s/keys :req-un [::filter-type ::pattern]
          :opt-un [::case-sensitive? ::regex?]))

(s/def ::subscription-callback
  (s/keys :req-un [::callback-type ::endpoint]
          :opt-un [::auth-token ::timeout]))

(s/def ::subscription-metadata
  (s/keys :req-un [::subscription-id ::created-at ::last-updated]
          :opt-un [::priority ::filters ::callbacks ::tags ::description]))

(s/def ::friend-subscription
  (s/and (s/keys :req-un [::subscription-type ::peer-id ::path ::subscription-id]
                 :opt-un [::subscription-metadata])
         #(= (:subscription-type %) :friends)))

(s/def ::following-subscription
  (s/and (s/keys :req-un [::subscription-type ::peer-id ::path ::subscription-id]
                 :opt-un [::subscription-metadata])
         #(= (:subscription-type %) :following)))

(s/def ::custom-subscription
  (s/and (s/keys :req-un [::subscription-type ::peer-id ::path ::subscription-id]
                 :opt-un [::subscription-metadata])
         #(= (:subscription-type %) :custom)))

(s/def ::subscription-event
  (s/keys :req-un [::event-type ::subscription-id ::timestamp]
          :opt-un [::data ::source ::priority]))

;; =============================================================================
;; Subscription State
;; =============================================================================

(defonce subscription-state
  (atom
   {:active? false
    :subscriptions {}
    :subscription-index {}
    :event-channels {}
    :subscription-timeout 300000
    :max-subscriptions 10000
    :cleanup-interval 60000}))

;; =============================================================================
;; Core Subscription Functions
;; =============================================================================

(defn start-subscription-manager
  "Start the subscription management system."
  [daemon-state]
  (log/info "Starting subscription manager...")
  (swap! subscription-state assoc :active? true)
  
  ;; Start subscription processing loop
  (start-subscription-loop daemon-state)
  
  ;; Start cleanup loop
  (start-cleanup-loop)
  
  (log/info "Subscription manager started"))

(defn stop-subscription-manager
  "Stop the subscription management system."
  []
  (log/info "Stopping subscription manager...")
  (swap! subscription-state assoc :active? false)
  
  ;; Close all event channels
  (doseq [[_ channel] (:event-channels @subscription-state)]
    (async/close! channel))
  
  (log/info "Subscription manager stopped"))

(defn start-subscription-loop
  "Main subscription processing loop."
  [daemon-state]
  (async/go-loop []
    (when (:active? @subscription-state)
      (try
        ;; Process subscription events
        (process-subscription-events daemon-state)
        
        ;; Update subscription statuses
        (update-subscription-statuses)
        
        ;; Wait for next cycle
        (async/<! (async/timeout 1000))
        (recur)
        (catch Exception e
          (log/error e "Error in subscription loop")
          (async/<! (async/timeout 5000))
          (recur))))))

(defn start-cleanup-loop
  "Cleanup loop for expired subscriptions."
  []
  (async/go-loop []
    (when (:active? @subscription-state)
      (try
        (cleanup-expired-subscriptions)
        (async/<! (async/timeout (:cleanup-interval @subscription-state)))
        (recur)
        (catch Exception e
          (log/error e "Error in cleanup loop")
          (async/<! (async/timeout 30000))
          (recur))))))

;; =============================================================================
;; Subscription Management
;; =============================================================================

(defn add-subscription
  "Add a new subscription."
  [subscription]
  (when (s/valid? ::subscription subscription)
    (let [subscription-id (:subscription-id subscription)
          subscription-type (:subscription-type subscription)]
      (swap! subscription-state update :subscriptions assoc subscription-id subscription)
      (swap! subscription-state update-in [:subscription-index subscription-type] conj subscription-id)
      
      ;; Create event channel for this subscription
      (let [event-channel (async/chan 1000)]
        (swap! subscription-state update :event-channels assoc subscription-id event-channel)
        (start-subscription-event-loop subscription-id event-channel))
      
      (log/info "Added subscription:" subscription-id)
      (notify-subscription-added subscription))))

(defn remove-subscription
  "Remove a subscription."
  [subscription-id]
  (when-let [subscription (get-subscription subscription-id)]
    (let [subscription-type (:subscription-type subscription)]
      (swap! subscription-state update :subscriptions dissoc subscription-id)
      (swap! subscription-state update-in [:subscription-index subscription-type] disj subscription-id)
      
      ;; Close event channel
      (when-let [channel (get-in @subscription-state [:event-channels subscription-id])]
        (async/close! channel)
        (swap! subscription-state update :event-channels dissoc subscription-id))
      
      (log/info "Removed subscription:" subscription-id)
      (notify-subscription-removed subscription))))

(defn update-subscription
  "Update an existing subscription."
  [subscription]
  (when (s/valid? ::subscription subscription)
    (let [subscription-id (:subscription-id subscription)]
      (swap! subscription-state update :subscriptions assoc subscription-id subscription)
      (log/debug "Updated subscription:" subscription-id))))

(defn get-subscription
  "Get a subscription by ID."
  [subscription-id]
  (get-in @subscription-state [:subscriptions subscription-id]))

(defn list-subscriptions
  "List all subscriptions."
  [& {:keys [subscription-type peer-id active-only?]}]
  (let [subscriptions (vals (:subscriptions @subscription-state))
        filtered (cond-> subscriptions
                   subscription-type (filter #(= (:subscription-type %) subscription-type))
                   peer-id (filter #(= (:peer-id %) peer-id))
                   active-only? (filter :active?))]
    filtered))

(defn get-subscriptions-for-peer
  "Get all subscriptions for a specific peer."
  [peer-id]
  (list-subscriptions :peer-id peer-id))

(defn get-subscriptions-by-type
  "Get all subscriptions of a specific type."
  [subscription-type]
  (list-subscriptions :subscription-type subscription-type))

;; =============================================================================
;; Path-based Subscriptions
;; =============================================================================

(defn subscribe-to-path
  "Subscribe to updates for a specific path."
  [peer-id path subscription-type & {:keys [filters callbacks priority]}]
  (let [subscription-id (generate-subscription-id)
        subscription {:subscription-id subscription-id
                      :subscription-type subscription-type
                      :peer-id peer-id
                      :path path
                      :created-at (System/currentTimeMillis)
                      :last-updated (System/currentTimeMillis)
                      :active? true
                      :filters filters
                      :callbacks callbacks
                      :priority (or priority :normal)}]
    (add-subscription subscription)
    subscription-id))

(defn unsubscribe-from-path
  "Unsubscribe from updates for a specific path."
  [peer-id path subscription-type]
  (let [subscriptions (filter #(and (= (:peer-id %) peer-id)
                                    (= (:path %) path)
                                    (= (:subscription-type %) subscription-type))
                              (vals (:subscriptions @subscription-state)))]
    (doseq [subscription subscriptions]
      (remove-subscription (:subscription-id subscription)))))

(defn get-subscriptions-for-path
  "Get all subscriptions for a specific path."
  [path]
  (filter #(= (:path %) path) (vals (:subscriptions @subscription-state))))

(defn get-subscriptions-matching-path
  "Get subscriptions that match a path pattern."
  [path-pattern]
  (filter #(paths/path-matches? (:path %) path-pattern)
          (vals (:subscriptions @subscription-state))))

;; =============================================================================
;; Friend Subscriptions
;; =============================================================================

(defn add-friend-subscription
  "Add a friend subscription."
  [peer-id path & {:keys [filters callbacks priority]}]
  (subscribe-to-path peer-id path :friends
                     :filters filters
                     :callbacks callbacks
                     :priority priority))

(defn remove-friend-subscription
  "Remove a friend subscription."
  [peer-id path]
  (unsubscribe-from-path peer-id path :friends))

(defn get-friend-subscriptions
  "Get all friend subscriptions."
  []
  (get-subscriptions-by-type :friends))

(defn get-friends
  "Get list of friends (peers with friend subscriptions)."
  []
  (->> (get-friend-subscriptions)
       (map :peer-id)
       (distinct)))

;; =============================================================================
;; Following Subscriptions
;; =============================================================================

(defn add-following-subscription
  "Add a following subscription."
  [peer-id path & {:keys [filters callbacks priority]}]
  (subscribe-to-path peer-id path :following
                     :filters filters
                     :callbacks callbacks
                     :priority priority))

(defn remove-following-subscription
  "Remove a following subscription."
  [peer-id path]
  (unsubscribe-from-path peer-id path :following))

(defn get-following-subscriptions
  "Get all following subscriptions."
  []
  (get-subscriptions-by-type :following))

(defn get-following
  "Get list of following (peers with following subscriptions)."
  []
  (->> (get-following-subscriptions)
       (map :peer-id)
       (distinct)))

;; =============================================================================
;; Custom Subscriptions
;; =============================================================================

(defn add-custom-subscription
  "Add a custom subscription."
  [peer-id path & {:keys [filters callbacks priority description]}]
  (subscribe-to-path peer-id path :custom
                     :filters filters
                     :callbacks callbacks
                     :priority priority))

(defn remove-custom-subscription
  "Remove a custom subscription."
  [peer-id path]
  (unsubscribe-from-path peer-id path :custom))

(defn get-custom-subscriptions
  "Get all custom subscriptions."
  []
  (get-subscriptions-by-type :custom))

;; =============================================================================
;; Event Processing
;; =============================================================================

(defn process-subscription-events
  "Process subscription events from the daemon."
  [daemon-state]
  (let [subscription-chan (:subscriptions (:channels daemon-state))]
    (when-let [event (async/poll! subscription-chan)]
      (handle-subscription-event event))))

(defn handle-subscription-event
  "Handle a subscription event."
  [event]
  (case (:type event)
    :subscribe (add-subscription (:subscription event))
    :unsubscribe (remove-subscription (:subscription-id event))
    :subscription-update (update-subscription (:subscription event))
    :subscription-event (process-subscription-event-data (:data event))
    (log/warn "Unknown subscription event type:" (:type event))))

(defn process-subscription-event-data
  "Process subscription event data."
  [event-data]
  (when (s/valid? ::subscription-event event-data)
    (let [subscription-id (:subscription-id event-data)
          subscription (get-subscription subscription-id)]
      (when subscription
        (deliver-subscription-event subscription event-data)))))

(defn deliver-subscription-event
  "Deliver an event to a subscription's event channel."
  [subscription event]
  (when-let [event-channel (get-in @subscription-state [:event-channels (:subscription-id subscription)])]
    (async/>!! event-channel event)))

(defn start-subscription-event-loop
  "Start event loop for a specific subscription."
  [subscription-id event-channel]
  (async/go-loop []
    (when-let [event (async/<! event-channel)]
      (try
        (process-subscription-event event)
        (recur)
        (catch Exception e
          (log/error e "Error processing subscription event for:" subscription-id)
          (recur))))))

;; =============================================================================
;; Subscription Status Management
;; =============================================================================

(defn update-subscription-statuses
  "Update subscription statuses based on peer availability."
  []
  (let [now (System/currentTimeMillis)
        subscriptions (vals (:subscriptions @subscription-state))]
    (doseq [subscription subscriptions]
      (let [peer-id (:peer-id subscription)
            last-updated (:last-updated subscription)
            timeout (:subscription-timeout @subscription-state)]
        (when (> (- now last-updated) timeout)
          (mark-subscription-inactive subscription))))))

(defn mark-subscription-inactive
  "Mark a subscription as inactive."
  [subscription]
  (let [subscription-id (:subscription-id subscription)]
    (swap! subscription-state update-in [:subscriptions subscription-id] assoc :active? false)
    (log/debug "Marked subscription as inactive:" subscription-id)))

(defn mark-subscription-active
  "Mark a subscription as active."
  [subscription-id]
  (swap! subscription-state update-in [:subscriptions subscription-id] assoc :active? true)
  (log/debug "Marked subscription as active:" subscription-id))

(defn cleanup-expired-subscriptions
  "Clean up expired subscriptions."
  []
  (let [now (System/currentTimeMillis)
        timeout (:subscription-timeout @subscription-state)
        expired-subscriptions (->> (vals (:subscriptions @subscription-state))
                                   (filter #(and (not (:active? %))
                                                 (> (- now (:last-updated %)) (* timeout 2)))))]
    (doseq [subscription expired-subscriptions]
      (remove-subscription (:subscription-id subscription))
      (log/debug "Cleaned up expired subscription:" (:subscription-id subscription)))))

;; =============================================================================
;; Notification System
;; =============================================================================

(defn notify-subscription-added
  "Notify about a subscription being added."
  [subscription]
  (log/debug "Subscription added:" (:subscription-id subscription)))

(defn notify-subscription-removed
  "Notify about a subscription being removed."
  [subscription]
  (log/debug "Subscription removed:" (:subscription-id subscription)))

(defn notify-subscription-event
  "Notify about a subscription event."
  [subscription-id event]
  (log/debug "Subscription event for:" subscription-id "event:" (:event-type event)))

;; =============================================================================
;; Utility Functions
;; =============================================================================

(defn generate-subscription-id
  "Generate a unique subscription ID."
  []
  (str "sub_" (subs (str (java.util.UUID/randomUUID)) 0 8)))

(defn get-subscription-stats
  "Get subscription statistics."
  []
  (let [subscriptions (vals (:subscriptions @subscription-state))
        by-type (group-by :subscription-type subscriptions)
        active-count (count (filter :active? subscriptions))]
    {:total (count subscriptions)
     :active active-count
     :inactive (- (count subscriptions) active-count)
     :by-type (into {} (map (fn [[k v]] [k (count v)]) by-type))
     :max-subscriptions (:max-subscriptions @subscription-state)}))

(defn export-subscriptions
  "Export subscriptions to EDN format."
  []
  (let [subscriptions (vals (:subscriptions @subscription-state))]
    (pr-str subscriptions)))

(defn import-subscriptions
  "Import subscriptions from EDN format."
  [subscriptions-edn]
  (try
    (let [subscriptions (read-string subscriptions-edn)]
      (doseq [subscription subscriptions]
        (add-subscription subscription))
      (log/info "Imported" (count subscriptions) "subscriptions"))
    (catch Exception e
      (log/error e "Failed to import subscriptions"))))

;; =============================================================================
;; Configuration
;; =============================================================================

(defn update-subscription-config
  "Update subscription configuration."
  [new-config]
  (swap! subscription-state merge new-config)
  (log/info "Subscription configuration updated"))

(defn get-subscription-status
  "Get current subscription status."
  []
  {:active? (:active? @subscription-state)
   :subscription-count (count (:subscriptions @subscription-state))
   :event-channels (count (:event-channels @subscription-state))
   :subscription-timeout (:subscription-timeout @subscription-state)
   :max-subscriptions (:max-subscriptions @subscription-state)})

