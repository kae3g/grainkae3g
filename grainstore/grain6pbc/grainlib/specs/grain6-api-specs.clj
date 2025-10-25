(ns grainlib.specs.grain6-api-specs
  "Grain6 API specifications with Clojure spec, Hoon, and Haskell strong typing inspiration"
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]))

;; =============================================================================
;; Core Grain6 Types (Hoon-inspired)
;; =============================================================================

;; Sheaf - Basic unit of the Grain Network (88 total)
(s/def ::sheaf-id
  (s/and pos-int? #(<= % 88)))

(s/def ::sheaf-name
  (s/and string? #(re-matches #"[a-z0-9]{5}" %)))

(s/def ::sheaf
  (s/keys :req-un [::sheaf-id ::sheaf-name]
          :opt-un [::description ::status ::capabilities]))

;; Grain6 Operating System
(s/def ::grain6-version
  (s/and string? #(re-matches #"\d+\.\d+\.\d+" %)))

(s/def ::grain6-status
  #{:running :stopped :starting :stopping :error})

(s/def ::grain6-config
  (s/keys :req-un [::grain6-version ::grain6-status]
          :opt-un [::memory-limit ::cpu-limit ::storage-limit]))

;; Process Supervision (S6-inspired)
(s/def ::process-id
  (s/and pos-int? #(<= % 65535)))

(s/def ::process-name
  (s/and string? #(re-matches #"[a-zA-Z0-9_-]+" %)))

(s/def ::process-status
  #{:up :down :starting :stopping :restarting :failed})

(s/def ::process
  (s/keys :req-un [::process-id ::process-name ::process-status]
          :opt-un [::pid ::start-time ::restart-count ::dependencies]))

;; =============================================================================
;; API Endpoints (Haskell-inspired function signatures)
;; =============================================================================

;; GET /api/v1/sheaves
(s/def ::sheaves-request
  (s/keys :opt-un [::limit ::offset ::status-filter]))

(s/def ::sheaves-response
  (s/keys :req-un [::sheaves ::total-count ::page ::limit]
          :opt-un [::next-page ::prev-page]))

;; GET /api/v1/sheaves/:id
(s/def ::sheaf-request
  (s/keys :req-un [::sheaf-id]))

(s/def ::sheaf-response
  (s/keys :req-un [::sheaf ::status ::timestamp]))

;; POST /api/v1/sheaves
(s/def ::create-sheaf-request
  (s/keys :req-un [::sheaf-name]
          :opt-un [::description ::capabilities]))

(s/def ::create-sheaf-response
  (s/keys :req-un [::sheaf ::status ::message]))

;; PUT /api/v1/sheaves/:id
(s/def ::update-sheaf-request
  (s/keys :req-un [::sheaf-id]
          :opt-un [::description ::capabilities ::status]))

(s/def ::update-sheaf-response
  (s/keys :req-un [::sheaf ::status ::message]))

;; DELETE /api/v1/sheaves/:id
(s/def ::delete-sheaf-request
  (s/keys :req-un [::sheaf-id]))

(s/def ::delete-sheaf-response
  (s/keys :req-un [::status ::message]))

;; =============================================================================
;; Process Management API
;; =============================================================================

;; GET /api/v1/processes
(s/def ::processes-request
  (s/keys :opt-un [::sheaf-id ::status-filter ::limit ::offset]))

(s/def ::processes-response
  (s/keys :req-un [::processes ::total-count ::page ::limit]
          :opt-un [::next-page ::prev-page]))

;; POST /api/v1/processes/:id/start
(s/def ::start-process-request
  (s/keys :req-un [::process-id]))

(s/def ::start-process-response
  (s/keys :req-un [::process ::status ::message]))

;; POST /api/v1/processes/:id/stop
(s/def ::stop-process-request
  (s/keys :req-un [::process-id]))

(s/def ::stop-process-response
  (s/keys :req-un [::process ::status ::message]))

;; POST /api/v1/processes/:id/restart
(s/def ::restart-process-request
  (s/keys :req-un [::process-id]))

(s/def ::restart-process-response
  (s/keys :req-un [::process ::status ::message]))

;; =============================================================================
;; Error Types (Haskell-inspired)
;; =============================================================================

(s/def ::error-code
  (s/and string? #(re-matches #"[A-Z_]+" %)))

(s/def ::error-message
  string?)

(s/def ::error-details
  (s/map-of keyword? any?))

(s/def ::api-error
  (s/keys :req-un [::error-code ::error-message]
          :opt-un [::error-details ::timestamp ::request-id]))

;; =============================================================================
;; Common Error Codes
;; =============================================================================

(s/def ::validation-error
  (s/and ::api-error #(= (:error-code %) "VALIDATION_ERROR")))

(s/def ::not-found-error
  (s/and ::api-error #(= (:error-code %) "NOT_FOUND")))

(s/def ::unauthorized-error
  (s/and ::api-error #(= (:error-code %) "UNAUTHORIZED")))

(s/def ::internal-server-error
  (s/and ::api-error #(= (:error-code %) "INTERNAL_SERVER_ERROR")))

;; =============================================================================
;; Generators for Testing
;; =============================================================================

(defn generate-sheaf
  "Generate a valid sheaf for testing"
  []
  (gen/generate (s/gen ::sheaf)))

(defn generate-process
  "Generate a valid process for testing"
  []
  (gen/generate (s/gen ::process)))

(defn generate-api-error
  "Generate a valid API error for testing"
  []
  (gen/generate (s/gen ::api-error)))

;; =============================================================================
;; Validation Functions
;; =============================================================================

(defn validate-sheaf
  "Validate a sheaf against the spec"
  [sheaf]
  (s/valid? ::sheaf sheaf))

(defn validate-process
  "Validate a process against the spec"
  [process]
  (s/valid? ::process process))

(defn validate-api-request
  "Validate an API request against the appropriate spec"
  [endpoint request]
  (case endpoint
    :sheaves (s/valid? ::sheaves-request request)
    :sheaf (s/valid? ::sheaf-request request)
    :create-sheaf (s/valid? ::create-sheaf-request request)
    :update-sheaf (s/valid? ::update-sheaf-request request)
    :delete-sheaf (s/valid? ::delete-sheaf-request request)
    :processes (s/valid? ::processes-request request)
    :start-process (s/valid? ::start-process-request request)
    :stop-process (s/valid? ::stop-process-request request)
    :restart-process (s/valid? ::restart-process-request request)
    false))

;; =============================================================================
;; Philosophy Integration
;; =============================================================================

(defn now-equals-next-plus-one
  "The eternal recursion: now == next + 1"
  [current-value]
  (inc current-value))

(defn eighty-eight-counter-philosophy
  "88 × 10^n ≥ 0 | -1 scaling"
  [n]
  (* 88 (Math/pow 10 n)))

(defn sheaf-capacity
  "Calculate sheaf capacity using 88 counter philosophy"
  [sheaf-id]
  (eighty-eight-counter-philosophy (mod sheaf-id 3)))

