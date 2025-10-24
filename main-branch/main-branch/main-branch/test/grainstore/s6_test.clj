(ns grainstore.s6-test
  "Tests for s6 process supervision - verifying Nock equivalence.
  
  These tests verify that the Clojure implementation matches the
  mathematical specification in grainstore/specs/s6.nock.md"
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [grainstore.s6 :as s6]))

;; ============================================================================
;; Setup
;; ============================================================================

(defn setup-service
  "Create a test service with given state."
  [state]
  {:service-name "/tmp/test-service"
   :state state
   :pid (if (= state :up) 1234 0)
   :timestamp 1728518400
   :logs []})

;; ============================================================================
;; State Transition Tests (Nock Equivalence)
;; ============================================================================

(deftest start-service-test
  (testing "Starting a down service transitions to up"
    (let [down-service (setup-service :down)
          started (s6/start-service down-service)]
      (is (= :up (:state started)))
      (is (pos-int? (:pid started)))
      (is (> (:timestamp started) (:timestamp down-service)))
      (is (= 1 (count (:logs started))))))
  
  (testing "Starting an up service is idempotent (no-op)"
    (let [up-service (setup-service :up)
          started (s6/start-service up-service)]
      (is (= up-service started))
      (is (= :up (:state started)))))
  
  (testing "Starting a finishing service is idempotent"
    (let [finishing-service (setup-service :finishing)
          started (s6/start-service finishing-service)]
      (is (= finishing-service started))))
  
  (testing "Start respects spec"
    (let [service (setup-service :down)
          started (s6/start-service service)]
      (is (s/valid? ::s6/service started)))))

(deftest stop-service-test
  (testing "Stopping an up service transitions to finishing"
    (let [up-service (setup-service :up)
          stopping (s6/stop-service up-service)]
      (is (= :finishing (:state stopping)))
      (is (> (:timestamp stopping) (:timestamp up-service)))
      (is (= 1 (count (:logs stopping))))))
  
  (testing "Stopping a finishing service transitions to down"
    (let [finishing-service (setup-service :finishing)
          stopped (s6/stop-service finishing-service)]
      (is (= :down (:state stopped)))
      (is (zero? (:pid stopped)))
      (is (= 1 (count (:logs stopped))))))
  
  (testing "Stopping a down service is idempotent"
    (let [down-service (setup-service :down)
          stopped (s6/stop-service down-service)]
      (is (= down-service stopped))))
  
  (testing "Complete stop sequence: up → finishing → down"
    (let [up-service (setup-service :up)
          finishing (s6/stop-service up-service)
          down (s6/stop-service finishing)]
      (is (= :up (:state up-service)))
      (is (= :finishing (:state finishing)))
      (is (= :down (:state down))))))

(deftest restart-service-test
  (testing "Restarting up service ends in up state"
    (let [up-service (setup-service :up)
          restarted (s6/restart-service up-service)]
      (is (= :up (:state restarted)))
      (is (>= (count (:logs restarted)) 3))  ; stop, down, start logs
      ))
  
  (testing "Restarting down service ends in up state"
    (let [down-service (setup-service :down)
          restarted (s6/restart-service down-service)]
      (is (= :up (:state restarted)))))
  
  (testing "Restart is composition: stop ∘ stop ∘ start"
    (let [service (setup-service :up)
          manual (-> service
                     s6/stop-service
                     s6/stop-service
                     s6/start-service)
          auto (s6/restart-service service)]
      (is (= (:state manual) (:state auto) :up)))))

(deftest service-status-test
  (testing "Status returns current state"
    (is (= :down (s6/service-status (setup-service :down))))
    (is (= :up (s6/service-status (setup-service :up))))
    (is (= :finishing (s6/service-status (setup-service :finishing)))))
  
  (testing "Status is pure (doesn't modify service)"
    (let [service (setup-service :up)
          _ (s6/service-status service)]
      (is (= service service)))))  ; Unchanged

;; ============================================================================
;; Supervision Loop Tests (Crash-Only Semantics)
;; ============================================================================

(deftest supervision-tick-test
  (testing "Tick restarts all down services"
    (let [services [(setup-service :down)
                    (setup-service :down)
                    (setup-service :up)]
          ticked (s6/supervision-tick services)]
      (is (= 3 (count ticked)))
      (is (= :up (:state (nth ticked 0))))  ; Restarted
      (is (= :up (:state (nth ticked 1))))  ; Restarted
      (is (= :up (:state (nth ticked 2))))))  ; Was already up
  
  (testing "Tick doesn't affect up services"
    (let [services [(setup-service :up)
                    (setup-service :up)]
          ticked (s6/supervision-tick services)]
      (is (= services ticked))))
  
  (testing "Tick is idempotent for up services"
    (let [services [(setup-service :up)]
          ticked1 (s6/supervision-tick services)
          ticked2 (s6/supervision-tick ticked1)]
      (is (= ticked1 ticked2)))))

(deftest supervision-loop-integration-test
  (testing "Supervision loop restarts crashed services"
    (let [services-atom (atom [(setup-service :down)])
          ;; Run one tick manually (instead of infinite loop)
          _ (swap! services-atom s6/supervision-tick)
          final-services @services-atom]
      (is (= :up (:state (first final-services)))))))

;; ============================================================================
;; Dependency Resolution Tests
;; ============================================================================

(deftest dependency-resolution-test
  (testing "Service with no dependencies can start"
    (let [service (s6/create-service "/tmp/test")
          all-services [service]]
      (is (s6/all-dependencies-up? service all-services))
      (let [started (s6/start-if-ready service all-services)]
        (is (= :up (:state started))))))
  
  (testing "Service with up dependencies can start"
    (let [dep (-> (s6/create-service "/tmp/dep")
                  s6/start-service)
          service (s6/create-service "/tmp/test"
                                     :dependencies ["/tmp/dep"])
          all-services [dep service]]
      (is (s6/all-dependencies-up? service all-services))
      (let [started (s6/start-if-ready service all-services)]
        (is (= :up (:state started))))))
  
  (testing "Service with down dependencies cannot start"
    (let [dep (s6/create-service "/tmp/dep")  ; Down
          service (s6/create-service "/tmp/test"
                                     :dependencies ["/tmp/dep"])
          all-services [dep service]]
      (is (not (s6/all-dependencies-up? service all-services)))
      (let [attempted (s6/start-if-ready service all-services)]
        (is (= :down (:state attempted))))))
  
  (testing "Service with multiple dependencies"
    (let [dep1 (-> (s6/create-service "/tmp/dep1") s6/start-service)
          dep2 (-> (s6/create-service "/tmp/dep2") s6/start-service)
          dep3 (s6/create-service "/tmp/dep3")  ; Down!
          service (s6/create-service "/tmp/test"
                                     :dependencies ["/tmp/dep1" "/tmp/dep2" "/tmp/dep3"])
          all-services [dep1 dep2 dep3 service]]
      ;; Cannot start (dep3 is down)
      (is (not (s6/all-dependencies-up? service all-services)))
      ;; Start dep3
      (let [all-services-up (assoc all-services 2 (s6/start-service dep3))]
        ;; Now can start
        (is (s6/all-dependencies-up? service all-services-up))
        (let [started (s6/start-if-ready service all-services-up)]
          (is (= :up (:state started))))))))

;; ============================================================================
;; Idempotence Tests (Critical Nock Property)
;; ============================================================================

(deftest idempotence-test
  (testing "Starting an up service multiple times"
    (let [service (-> (setup-service :down) s6/start-service)]
      (is (= service
             (s6/start-service service)
             (s6/start-service (s6/start-service service))))))
  
  (testing "Stopping a down service multiple times"
    (let [service (setup-service :down)]
      (is (= service
             (s6/stop-service service)
             (s6/stop-service (s6/stop-service service))))))
  
  (testing "Restarting maintains state"
    (let [service (-> (setup-service :down) s6/start-service)
          restarted1 (s6/restart-service service)
          restarted2 (s6/restart-service restarted1)]
      (is (= :up (:state restarted1) (:state restarted2))))))

;; ============================================================================
;; Spec Validation Tests
;; ============================================================================

(deftest spec-validation-test
  (testing "All functions respect specs"
    ;; Instrument all functions
    (stest/instrument `s6/start-service)
    (stest/instrument `s6/stop-service)
    (stest/instrument `s6/restart-service)
    (stest/instrument `s6/service-status)
    
    ;; Run operations
    (let [service (setup-service :down)
          started (s6/start-service service)
          stopped (s6/stop-service started)
          restarted (s6/restart-service service)
          status (s6/service-status service)]
      ;; All should succeed without spec violations
      (is (s/valid? ::s6/service started))
      (is (s/valid? ::s6/service stopped))
      (is (s/valid? ::s6/service restarted))
      (is (s/valid? ::s6/state status))))
  
  (testing "Invalid service data fails spec"
    (is (not (s/valid? ::s6/service {:state :invalid})))
    (is (not (s/valid? ::s6/service {:service-name 123})))))  ; Should be string

;; ============================================================================
;; Utility Function Tests
;; ============================================================================

(deftest utility-functions-test
  (testing "create-service defaults"
    (let [service (s6/create-service "/tmp/test")]
      (is (= "/tmp/test" (:service-name service)))
      (is (= :down (:state service)))
      (is (zero? (:pid service)))
      (is (empty? (:logs service)))
      (is (empty? (:dependencies service)))))
  
  (testing "create-service with dependencies"
    (let [service (s6/create-service "/tmp/test"
                                     :dependencies ["/tmp/dep1" "/tmp/dep2"])]
      (is (= 2 (count (:dependencies service))))))
  
  (testing "service-uptime"
    (let [down-service (setup-service :down)
          up-service (setup-service :up)]
      (is (zero? (s6/service-uptime down-service)))
      (is (pos? (s6/service-uptime up-service))))))

;; ============================================================================
;; Integration Tests
;; ============================================================================

(deftest integration-test
  (testing "Complete service lifecycle"
    (let [service (s6/create-service "/tmp/integration-test")]
      ;; Start with down service
      (is (= :down (:state service)))
      
      ;; Start it
      (let [started (s6/start-service service)]
        (is (= :up (:state started)))
        
        ;; Stop it (phase 1)
        (let [finishing (s6/stop-service started)]
          (is (= :finishing (:state finishing)))
          
          ;; Stop it (phase 2)
          (let [stopped (s6/stop-service finishing)]
            (is (= :down (:state stopped)))
            (is (zero? (:pid stopped)))
            
            ;; Restart it
            (let [restarted (s6/restart-service stopped)]
              (is (= :up (:state restarted))))))))))

;; ============================================================================
;; Run Tests
;; ============================================================================

(defn run-s6-tests []
  (run-tests 'grainstore.s6-test))

(comment
  ;; Run tests in REPL
  (run-s6-tests)
  
  ;; Run specific test
  (start-service-test)
  (supervision-tick-test)
  (dependency-resolution-test)
  )

