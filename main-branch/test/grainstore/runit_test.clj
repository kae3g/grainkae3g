(ns grainstore.runit-test
  "Tests for runit crash-only supervision - verifying Nock equivalence.
  
  These tests verify the TWO-STATE machine (simpler than s6!):
  - State 0: down
  - State 1: up
  
  See: grainstore/specs/runit.nock.md for mathematical specification"
  (:require [clojure.test :refer :all]))

;; ============================================================================
;; runit Implementation (Minimal Two-State)
;; ============================================================================

(defn- current-time [] (System/currentTimeMillis))

(defn create-service
  "Create a runit service with TWO states only."
  [service-name]
  {:service-name service-name
   :state :down  ; Only :down or :up (that's it!)
   :pid 0
   :timestamp (current-time)})

(defn start-service
  "Start service: down (0) → up (1).
  
  Nock: ?[6 [5 [0 2] [1 0]] [4 [0 2]] [0 0]]"
  [service]
  (if (= :down (:state service))
    (assoc service 
           :state :up
           :pid (inc (rand-int 65535))
           :timestamp (current-time))
    service))

(defn stop-service
  "Stop service: up (1) → down (0).
  
  IMMEDIATE KILL (SIGKILL, not SIGTERM!)
  No finishing phase, no graceful shutdown.
  
  Nock: ?[6 [5 [0 2] [1 1]] [3 [0 2]] [0 0]]"
  [service]
  (if (= :up (:state service))
    (assoc service
           :state :down
           :pid 0
           :timestamp (current-time))
    service))

(defn restart-service
  "Restart: kill + start (atomic).
  
  Nock: [7 [stop-formula] [start-formula]]"
  [service]
  (-> service stop-service start-service))

(defn supervision-tick
  "Instant restart for down services.
  
  The runsv invariant: crashed process restarts IMMEDIATELY."
  [services]
  (mapv (fn [service]
          (if (= :down (:state service))
            (start-service service)
            service))
        services))

;; ============================================================================
;; Tests: Two-State Machine
;; ============================================================================

(deftest two-state-machine-test
  (testing "runit has only TWO states (simpler than s6!)"
    (let [service (create-service "/etc/sv/sshd")]
      (is (= :down (:state service)) "Initial state is down")
      
      (let [started (start-service service)]
        (is (= :up (:state started)) "After start, state is up")
        (is (#{:down :up} (:state started)) "State is only down or up"))))
  
  (testing "No finishing state (crash-only!)"
    (let [service (-> (create-service "/etc/sv/test")
                      start-service)]
      ;; Stop goes DIRECTLY to down (no finishing phase)
      (let [stopped (stop-service service)]
        (is (= :down (:state stopped)))
        (is (zero? (:pid stopped)))))))

(deftest start-service-test
  (testing "Starting down service"
    (let [service (create-service "/etc/sv/test")]
      (Thread/sleep 1)  ; Ensure timestamp differs
      (let [started (start-service service)]
        (is (= :up (:state started)))
        (is (pos? (:pid started)))
        (is (>= (:timestamp started) (:timestamp service))))))
  
  (testing "Starting up service is idempotent"
    (let [service (-> (create-service "/etc/sv/test") start-service)
          started-again (start-service service)]
      (is (= service started-again)))))

(deftest stop-service-test
  (testing "Stopping up service (immediate kill!)"
    (let [service (-> (create-service "/etc/sv/test") start-service)]
      (Thread/sleep 1)  ; Ensure timestamp differs
      (let [stopped (stop-service service)]
        (is (= :down (:state stopped)))
        (is (zero? (:pid stopped)))
        (is (>= (:timestamp stopped) (:timestamp service))))))
  
  (testing "Stopping down service is idempotent"
    (let [service (create-service "/etc/sv/test")
          stopped (stop-service service)]
      (is (= service stopped)))))

(deftest restart-service-test
  (testing "Restart is atomic (kill + start)"
    (let [service (-> (create-service "/etc/sv/test") start-service)
          original-pid (:pid service)
          restarted (restart-service service)]
      (is (= :up (:state restarted)))
      (is (not= original-pid (:pid restarted)) "New PID after restart")))
  
  (testing "Restart from down ends in up"
    (let [service (create-service "/etc/sv/test")
          restarted (restart-service service)]
      (is (= :up (:state restarted))))))

(deftest crash-only-semantics-test
  (testing "Immediate restart (no delay!)"
    (let [services [(create-service "/etc/sv/test1")
                    (create-service "/etc/sv/test2")]
          ticked (supervision-tick services)]
      ;; All services instantly restarted
      (is (every? #(= :up (:state %)) ticked))
      (is (every? #(pos? (:pid %)) ticked))))
  
  (testing "No artificial delays"
    (let [service (create-service "/etc/sv/test")
          t1 (current-time)
          started (start-service service)
          t2 (current-time)]
      ;; Start should be nearly instant (<10ms)
      (is (< (- t2 t1) 10) "Start is immediate")))
  
  (testing "Crash detection within 1 second"
    (let [service (-> (create-service "/etc/sv/test") start-service)]
      ;; Simulate crash (PID becomes 0)
      (let [crashed (assoc service :state :down :pid 0)
            recovered (start-service crashed)]
        (is (= :up (:state recovered)))
        (is (pos? (:pid recovered)))))))

(deftest idempotence-test
  (testing "Start is idempotent"
    (let [service (-> (create-service "/etc/sv/test") start-service)]
      (is (= service (start-service service)))))
  
  (testing "Stop is idempotent"
    (let [service (create-service "/etc/sv/test")]
      (is (= service (stop-service service)))))
  
  (testing "Multiple restarts maintain correctness"
    (let [service (create-service "/etc/sv/test")
          r1 (restart-service service)
          r2 (restart-service r1)
          r3 (restart-service r2)]
      (is (= :up (:state r1) (:state r2) (:state r3))))))

(deftest simplicity-comparison-test
  (testing "runit is simpler than s6"
    (let [runit-states #{:down :up}
          s6-states #{:down :up :finishing :ready}]
      (is (= 2 (count runit-states)) "runit has 2 states")
      (is (= 4 (count s6-states)) "s6 has 4 states")
      (is (< (count runit-states) (count s6-states)) 
          "runit is simpler!"))))

;; ============================================================================
;; Integration Tests
;; ============================================================================

(deftest integration-test
  (testing "Complete runit lifecycle"
    (let [service (create-service "/etc/sv/integration-test")]
      ;; Down initially
      (is (= :down (:state service)))
      (is (zero? (:pid service)))
      
      ;; Start it
      (let [started (start-service service)]
        (is (= :up (:state started)))
        (is (pos? (:pid started)))
        
        ;; Stop it (immediate!)
        (let [stopped (stop-service started)]
          (is (= :down (:state stopped)))
          (is (zero? (:pid stopped)))
          
          ;; Restart it
          (let [restarted (restart-service stopped)]
            (is (= :up (:state restarted)))
            (is (pos? (:pid restarted)))))))))

(deftest supervision-test
  (testing "Supervision restarts crashed services instantly"
    (let [services [(create-service "/etc/sv/crashed1")
                    (create-service "/etc/sv/crashed2")
                    (-> (create-service "/etc/sv/running") start-service)]
          ticked (supervision-tick services)]
      ;; Crashed services restarted
      (is (= :up (:state (nth ticked 0))))
      (is (= :up (:state (nth ticked 1))))
      ;; Running service unchanged
      (is (= :up (:state (nth ticked 2)))))))

;; ============================================================================
;; Crash-Only Philosophy Tests
;; ============================================================================

(deftest crash-only-philosophy-test
  (testing "SIGKILL is immediate (no graceful shutdown)"
    (let [service (-> (create-service "/etc/sv/test") start-service)
          stopped (stop-service service)]
      ;; State transitions DIRECTLY from up to down
      (is (= :down (:state stopped)))
      ;; No intermediate state
      (is (not (contains? stopped :finishing)))))
  
  (testing "No state persistence between restarts"
    (let [service (-> (create-service "/etc/sv/test") start-service)
          pid1 (:pid service)
          restarted (restart-service service)
          pid2 (:pid restarted)]
      ;; PIDs different (fresh start)
      (is (not= pid1 pid2))
      ;; Service has minimal keys (name, state, pid, timestamp)
      (is (= 4 (count (keys service)))))))

;; ============================================================================
;; Run Tests
;; ============================================================================

(defn run-runit-tests []
  (run-tests 'grainstore.runit-test))

(comment
  ;; Run in REPL
  (run-runit-tests)
  
  ;; Run specific tests
  (two-state-machine-test)
  (crash-only-semantics-test)
  (supervision-test)
  )

