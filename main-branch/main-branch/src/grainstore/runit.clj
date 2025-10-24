(ns grainstore.runit
  "runit process supervision - Clojure implementation of crash-only design.
  
  See:
  - grainstore/specs/runit.nock.md - Mathematical specification
  - test/grainstore/runit_test.clj - Test suite (42 assertions)
  
  This implementation follows the Nock spec exactly:
  - TWO-state machine (down, up) - simpler than s6!
  - IMMEDIATE kill (SIGKILL, not SIGTERM)
  - INSTANT restart (no delays)
  - NO state persistence between restarts
  - Crash-only semantics (embrace failure)"
  (:require [clojure.spec.alpha :as s]))

;; ============================================================================
;; Specs (Simpler than s6!)
;; ============================================================================

(s/def ::service-name string?)
(s/def ::state #{:down :up})  ; Only TWO states!
(s/def ::pid (s/or :none zero? :running pos-int?))
(s/def ::timestamp pos-int?)

(s/def ::service
  (s/keys :req-un [::service-name ::state ::pid ::timestamp]))

;; ============================================================================
;; State Transitions (Pure Functions - Nock Equivalents)
;; ============================================================================

(defn- current-time
  "Get current Unix timestamp in milliseconds."
  []
  (System/currentTimeMillis))

(defn create-service
  "Create a new runit service with defaults.
  
  Returns: Service in :down state (ready to start)"
  [service-name]
  {:pre [(string? service-name)]
   :post [(s/valid? ::service %)]}
  {:service-name service-name
   :state :down
   :pid 0
   :timestamp (current-time)})

(defn start-service
  "Transition service from :down to :up. Idempotent.
  
  Nock equivalent: ?[6 [5 [0 2] [1 0]] [4 [0 2]] [0 0]]
  
  If state == :down:
    - Set state to :up
    - Assign new PID
    - Update timestamp
  Else:
    - No-op (already running)
  
  Returns: Updated service"
  [service]
  {:pre [(s/valid? ::service service)]
   :post [(s/valid? ::service %)]}
  (if (= :down (:state service))
    (assoc service
           :state :up
           :pid (inc (rand-int 65535))  ; Simulate PID assignment
           :timestamp (current-time))
    service))

(defn stop-service
  "Transition service from :up to :down. IMMEDIATE KILL!
  
  Nock equivalent: ?[6 [5 [0 2] [1 1]] [3 [0 2]] [0 0]]
  
  Unlike s6, this is IMMEDIATE:
  - No finishing phase
  - SIGKILL sent (force kill)
  - Direct transition: :up → :down
  
  This embodies crash-only design:
  - Services must handle arbitrary termination
  - No graceful shutdown needed
  - Restart is the recovery mechanism
  
  Returns: Updated service"
  [service]
  {:pre [(s/valid? ::service service)]
   :post [(s/valid? ::service %)]}
  (if (= :up (:state service))
    (assoc service
           :state :down
           :pid 0
           :timestamp (current-time))
    service))

(defn restart-service
  "Atomic restart: stop then start.
  
  Nock equivalent: [7 [stop-formula] [start-formula]]
  (Rule 7 = composition)
  
  This is the atomic composition of stop and start:
  - Kill the process (SIGKILL)
  - Start it fresh
  - New PID, clean state
  
  Returns: Updated service in :up state"
  [service]
  {:pre [(s/valid? ::service service)]
   :post [(s/valid? ::service %)
          (= :up (:state %))]}
  (-> service
      stop-service
      start-service))

(defn service-status
  "Get current service state. Pure function (no side effects).
  
  Nock equivalent: [0 2] (tree addressing, get state field)
  
  Returns: State keyword (:down or :up)"
  [service]
  {:pre [(s/valid? ::service service)]
   :post [(s/valid? ::state %)]}
  (:state service))

;; ============================================================================
;; Supervision Loop (Crash-Only Semantics)
;; ============================================================================

(defn supervision-tick
  "Single tick of supervision loop. INSTANTLY restarts down services.
  
  This implements the core runsv invariant:
  - A service in :down state is IMMEDIATELY restarted
  - No delays, no backoff, no retry logic
  - Crash-only: embrace failure, restart is cheap
  
  Nock equivalent:
  For each service:
    ?[6 [5 [0 2] [1 0]]  ; If state == :down
       [start-formula]    ; Start it NOW (no delay!)
       [0 0]]             ; Else no-op
  
  Returns: Vector of updated services"
  [services]
  {:pre [(s/valid? (s/coll-of ::service) services)]
   :post [(s/valid? (s/coll-of ::service) %)]}
  (mapv (fn [service]
          (if (= :down (:state service))
            (start-service service)
            service))
        services))

(defn supervision-loop
  "Infinite supervision loop. Checks every ~1 second (faster than s6!).
  
  This is the runsv main loop:
  1. Check all services
  2. Restart any that are down (INSTANT!)
  3. Sleep ~1 second
  4. Repeat forever
  
  runit is FASTER than s6:
  - s6 checks every ~5 seconds
  - runit checks every ~1 second
  - Faster crash detection!
  
  Arguments:
  - services-atom: Atom containing vector of services
  - interval-ms: Sleep time between ticks (default 1000ms)
  
  Returns: Never (infinite loop)"
  [services-atom & {:keys [interval-ms] :or {interval-ms 1000}}]
  {:pre [(instance? clojure.lang.Atom services-atom)]}
  (loop []
    (swap! services-atom supervision-tick)
    (Thread/sleep interval-ms)
    (recur)))

;; ============================================================================
;; Crash-Only Utilities
;; ============================================================================

(defn service-uptime
  "Calculate service uptime in milliseconds.
  
  Returns: Uptime in ms, or 0 if service is down"
  [service]
  {:pre [(s/valid? ::service service)]}
  (if (= :up (:state service))
    (- (current-time) (:timestamp service))
    0))

(defn crashed?
  "Check if a service has crashed (was up, now down).
  
  In runit, crash detection is trivial:
  - PID becomes 0
  - State is :down
  - That's it!
  
  Returns: Boolean"
  [service]
  {:pre [(s/valid? ::service service)]}
  (and (= :down (:state service))
       (zero? (:pid service))))

;; ============================================================================
;; Comparison with s6
;; ============================================================================

(def runit-vs-s6
  "Comparison of runit and s6 supervision.
  
  This demonstrates why runit is simpler but less feature-rich."
  {:states {:runit 2  ; :down, :up
            :s6 4}    ; :down, :up, :finishing, :ready
   
   :stop-signal {:runit :SIGKILL   ; Force kill (immediate)
                 :s6 :SIGTERM}     ; Graceful (with finish script)
   
   :restart-time {:runit "Instant (<1s)"
                  :s6 "After finish script"}
   
   :size {:runit "30KB"
          :s6 "200KB"}
   
   :verification {:runit "Trivial (2-state machine)"
                  :s6 "Moderate (4-state machine)"}
   
   :philosophy {:runit "Crash-only (embrace failure)"
                :s6 "Graceful + crash-only (hybrid)"}})

;; ============================================================================
;; Examples
;; ============================================================================

(comment
  ;; Create a service
  (def sshd (create-service "/etc/sv/sshd"))
  
  ;; Start it
  (def sshd-up (start-service sshd))
  (service-status sshd-up)  ; => :up
  
  ;; Stop it (IMMEDIATE kill!)
  (def sshd-down (stop-service sshd-up))
  (service-status sshd-down)  ; => :down
  
  ;; Restart (atomic)
  (def sshd-restarted (restart-service sshd-up))
  (service-status sshd-restarted)  ; => :up
  
  ;; Supervision loop
  (def services (atom [(create-service "/etc/sv/dbus")
                       (create-service "/etc/sv/sshd")]))
  (supervision-tick @services)  ; Starts all down services
  
  ;; Compare with s6
  runit-vs-s6
  ; => {:states {:runit 2, :s6 4}, ...}
  
  ;; Check if crashed
  (crashed? sshd-down)  ; => true
  (crashed? sshd-up)    ; => false
  )

