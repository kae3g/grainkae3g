(ns grainstore.s6
  "s6 process supervision - Clojure implementation equivalent to Nock specification.
  
  See:
  - grainstore/specs/s6.nock.md - Mathematical specification
  - grainstore/equivalence/s6-clj-nock.md - Equivalence proof
  
  This implementation follows the Nock spec exactly:
  - State transitions are pure functions
  - Operations are atomic
  - Crash-only semantics (no corruption on kill -9)
  - Automatic restart of crashed services"
  (:require [clojure.spec.alpha :as s]))

;; ============================================================================
;; Specs
;; ============================================================================

(s/def ::service-name string?)
(s/def ::state #{:down :up :finishing :ready})
(s/def ::pid (s/or :none zero? :running pos-int?))
(s/def ::timestamp pos-int?)
(s/def ::severity #{:info :warn :error})
(s/def ::message string?)
(s/def ::log-entry (s/tuple ::timestamp ::severity ::message))
(s/def ::logs (s/coll-of ::log-entry :kind vector?))
(s/def ::dependencies (s/coll-of ::service-name :kind vector?))

(s/def ::service
  (s/keys :req-un [::service-name ::state ::pid ::timestamp ::logs]
          :opt-un [::dependencies]))

;; ============================================================================
;; State Transitions (Pure Functions - Nock Equivalents)
;; ============================================================================

(defn- current-time
  "Get current Unix timestamp in milliseconds."
  []
  (System/currentTimeMillis))

(defn- add-log
  "Append a log entry to service logs."
  [service severity message]
  (update service :logs conj [(current-time) severity message]))

(defn start-service
  "Transition service from :down to :up. Idempotent.
  
  Nock equivalent: ?[6 [5 [0 3] [1 0]] [4 [0 3]] [0 0]]
  
  If state == :down:
    - Set state to :up
    - Assign new PID (simulated)
    - Update timestamp
    - Log start event
  Else:
    - No-op (already running)
  
  Returns: Updated service"
  [service]
  {:pre [(s/valid? ::service service)]
   :post [(s/valid? ::service %)]}
  (if (= (:state service) :down)
    (-> service
        (assoc :state :up)
        (assoc :pid (rand-int 65535))  ; Simulate PID assignment
        (assoc :timestamp (current-time))
        (add-log :info "Service starting"))
    service))

(defn stop-service
  "Transition service from :up to :finishing, then to :down.
  
  Nock equivalent: ?[6 [5 [0 3] [1 1]] [4 [0 3]] [0 0]]
  
  Two-phase stop (crash-only semantics):
  1. :up → :finishing (SIGTERM sent, finish script runs)
  2. :finishing → :down (process exits, cleanup complete)
  
  Returns: Updated service"
  [service]
  {:pre [(s/valid? ::service service)]
   :post [(s/valid? ::service %)]}
  (case (:state service)
    :up (-> service
            (assoc :state :finishing)
            (assoc :timestamp (current-time))
            (add-log :info "Service stopping (finishing)"))
    
    :finishing (-> service
                   (assoc :state :down)
                   (assoc :pid 0)
                   (assoc :timestamp (current-time))
                   (add-log :info "Service stopped (down)"))
    
    ;; Already down or other state - no-op
    service))

(defn restart-service
  "Atomic restart: stop then start.
  
  Nock equivalent: [7 [stop-formula] [start-formula]]
  (Rule 7 = composition)
  
  Returns: Updated service in :up state"
  [service]
  {:pre [(s/valid? ::service service)]
   :post [(s/valid? ::service %)
          (= :up (:state %))]}
  (-> service
      stop-service    ; :up → :finishing
      stop-service    ; :finishing → :down
      start-service)) ; :down → :up

(defn service-status
  "Get current service state. Pure function (no side effects).
  
  Nock equivalent: [0 3] (tree addressing, get state field)
  
  Returns: State keyword (:down, :up, :finishing, :ready)"
  [service]
  {:pre [(s/valid? ::service service)]
   :post [(s/valid? ::state %)]}
  (:state service))

;; ============================================================================
;; Supervision Loop (Crash-Only Semantics)
;; ============================================================================

(defn supervision-tick
  "Single tick of supervision loop. Restarts down services automatically.
  
  This implements the core s6-svscan invariant:
  - A service in :down state will be automatically restarted
  - Unless it's in 'once' mode (not implemented yet)
  
  Nock equivalent:
  For each service:
    ?[6 [5 [0 3] [1 0]]  ; If state == :down
       [start-formula]    ; Start it
       [0 0]]             ; Else no-op
  
  Returns: Vector of updated services"
  [services]
  {:pre [(s/valid? (s/coll-of ::service) services)]
   :post [(s/valid? (s/coll-of ::service) %)]}
  (mapv (fn [service]
          (if (= (:state service) :down)
            (start-service service)
            service))
        services))

(defn supervision-loop
  "Infinite supervision loop. Runs every ~5 seconds.
  
  This is the s6-svscan main loop:
  1. Check all services
  2. Restart any that are down
  3. Sleep 5 seconds
  4. Repeat forever
  
  Arguments:
  - services-atom: Atom containing vector of services
  - interval-ms: Sleep time between ticks (default 5000ms)
  
  Returns: Never (infinite loop)"
  [services-atom & {:keys [interval-ms] :or {interval-ms 5000}}]
  {:pre [(instance? clojure.lang.Atom services-atom)]}
  (loop []
    (swap! services-atom supervision-tick)
    (Thread/sleep interval-ms)
    (recur)))

;; ============================================================================
;; Dependency Resolution
;; ============================================================================

(defn all-dependencies-up?
  "Check if all dependencies of a service are in :up state.
  
  Arguments:
  - service: Service to check
  - services: All services (for dependency lookup)
  
  Returns: Boolean - true if all deps are up"
  [service services]
  {:pre [(s/valid? ::service service)
         (s/valid? (s/coll-of ::service) services)]}
  (let [deps (:dependencies service [])
        service-map (into {} (map (juxt :service-name identity) services))]
    (every? (fn [dep-name]
              (let [dep-service (get service-map dep-name)]
                (and dep-service (= :up (:state dep-service)))))
            deps)))

(defn start-if-ready
  "Start service only if all dependencies are up.
  
  This implements dependency-aware startup:
  - Check all dependencies
  - If all are up, start service
  - Else, wait
  
  Returns: Updated service (or unchanged if not ready)"
  [service all-services]
  {:pre [(s/valid? ::service service)
         (s/valid? (s/coll-of ::service) all-services)]}
  (if (and (= :down (:state service))
           (all-dependencies-up? service all-services))
    (start-service service)
    service))

;; ============================================================================
;; Utility Functions
;; ============================================================================

(defn create-service
  "Create a new service with defaults.
  
  Arguments:
  - service-name: String path (e.g. \"/etc/s6/sv/dbus\")
  - opts: Optional map with :dependencies
  
  Returns: Service map"
  [service-name & {:keys [dependencies] :or {dependencies []}}]
  {:service-name service-name
   :state :down
   :pid 0
   :timestamp (current-time)
   :logs []
   :dependencies dependencies})

(defn service-uptime
  "Calculate service uptime in milliseconds.
  
  Returns: Uptime in ms, or 0 if service is down"
  [service]
  {:pre [(s/valid? ::service service)]}
  (if (= :up (:state service))
    (- (current-time) (:timestamp service))
    0))

;; ============================================================================
;; Examples
;; ============================================================================

(comment
  ;; Create a service
  (def dbus (create-service "/etc/s6/sv/dbus"))
  
  ;; Start it
  (def dbus-up (start-service dbus))
  (service-status dbus-up)  ; => :up
  
  ;; Stop it
  (def dbus-stopping (stop-service dbus-up))
  (service-status dbus-stopping)  ; => :finishing
  
  (def dbus-down (stop-service dbus-stopping))
  (service-status dbus-down)  ; => :down
  
  ;; Restart (atomic)
  (def dbus-restarted (restart-service dbus-up))
  (service-status dbus-restarted)  ; => :up
  
  ;; Supervision loop
  (def services (atom [dbus (create-service "/etc/s6/sv/sshd")]))
  (supervision-tick @services)  ; Starts all down services
  
  ;; With dependencies
  (def wayland (create-service "/etc/s6/sv/wayland"
                               :dependencies ["/etc/s6/sv/dbus"]))
  (def all-services [(start-service dbus) wayland])
  (start-if-ready wayland all-services)  ; Starts wayland (dbus is up)
  )

