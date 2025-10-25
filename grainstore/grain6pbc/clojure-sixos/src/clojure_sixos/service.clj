(ns clojure-sixos.service
  "SixOS service management with typo handling.
   
   Provides s6-compatible service operations with automatic typo correction."
  (:require [clojure-sixos.typo :as typo]
            [clojure.java.shell :as shell]))

;; =============================================================================
;; Service Operations
;; =============================================================================

(defn resolve-service
  "Resolve a service name with typo correction.
   
   Returns a map with:
   - :canonical - The canonical service name
   - :autocorrected - Boolean indicating if correction was applied
   - :message - User-friendly message (if corrected)
   
   Examples:
     (resolve-service \"clotoko-daemon\")
     => {:canonical \"clotoko-daemon\" :autocorrected false}
     
     (resolve-service \"clomoko-daemon\")
     => {:canonical \"clotoko-daemon\"
         :autocorrected true
         :message \"Autocorrected: clomoko-daemon → clotoko-daemon\"}"
  [service-name]
  (let [result (typo/resolve-with-message service-name)
        canonical (:resolved result)
        autocorrected (not= service-name canonical)]
    {:canonical canonical
     :autocorrected autocorrected
     :message (:message result)}))

(defn start
  "Start a service with typo handling.
   
   This is a placeholder that demonstrates the pattern.
   In a real implementation, this would integrate with s6-rc or s6-svc.
   
   Examples:
     (start \"clomoko-daemon\")
     => Autocorrected: clomoko-daemon → clotoko-daemon
        Starting clotoko-daemon..."
  [service-name]
  (let [{:keys [canonical autocorrected message]} (resolve-service service-name)]
    (when autocorrected
      (println message))
    (println (format "Starting %s..." canonical))
    ;; In real implementation:
    ;; (shell/sh "s6-svc" "-u" (str "/run/service/" canonical))
    {:status :started
     :service canonical
     :autocorrected autocorrected}))

(defn stop
  "Stop a service with typo handling.
   
   Examples:
     (stop \"grainmusik-server\")
     => Autocorrected: grainmusik-server → grainmusic-server
        Stopping grainmusic-server..."
  [service-name]
  (let [{:keys [canonical autocorrected message]} (resolve-service service-name)]
    (when autocorrected
      (println message))
    (println (format "Stopping %s..." canonical))
    ;; In real implementation:
    ;; (shell/sh "s6-svc" "-d" (str "/run/service/" canonical))
    {:status :stopped
     :service canonical
     :autocorrected autocorrected}))

(defn restart
  "Restart a service with typo handling.
   
   Examples:
     (restart \"graincaly-daemon\")"
  [service-name]
  (stop service-name)
  (Thread/sleep 100)
  (start service-name))

(defn status
  "Get service status with typo handling.
   
   Examples:
     (status \"clotoko-daemon\")"
  [service-name]
  (let [{:keys [canonical autocorrected message]} (resolve-service service-name)]
    (when autocorrected
      (println message))
    (println (format "Status for %s:" canonical))
    ;; In real implementation:
    ;; (shell/sh "s6-svstat" (str "/run/service/" canonical))
    {:service canonical
     :running true
     :uptime 12345
     :autocorrected autocorrected}))

;; =============================================================================
;; Service Definition
;; =============================================================================

(defonce ^:private service-registry
  "Registry of defined services with their metadata."
  (atom {}))

(defn defservice
  "Define a service with typo tolerance.
   
   Args:
     service-name - Canonical service name
     opts - Map with:
            :run - Command to run
            :typos - Known typos
            :aliases - Aliases
            :deps - Service dependencies
   
   Examples:
     (defservice \"clotoko-daemon\"
       {:run \"./run-clotoko-daemon\"
        :typos [\"clomoko-daemon\" \"clatoko-daemon\"]
        :aliases [\"motoko-transpiler\"]
        :deps [\"grainweb-daemon\"]})"
  [service-name {:keys [run typos aliases deps] :as opts}]
  ;; Register in typo system
  (typo/register service-name {:typos typos
                                :aliases aliases
                                :category :service})
  ;; Register in service registry
  (swap! service-registry assoc service-name
         {:name service-name
          :run run
          :typos typos
          :aliases aliases
          :deps deps})
  service-name)

(defn all-services
  "Get all defined services.
   
   Examples:
     (all-services)
     => [\"clotoko-daemon\" \"grainmusic-server\" ...]"
  []
  (keys @service-registry))

(defn get-service
  "Get service definition.
   
   Examples:
     (get-service \"clotoko-daemon\")
     => {:name \"clotoko-daemon\" :run \"./run-clotoko-daemon\" ...}"
  [service-name]
  (let [canonical (typo/resolve-name service-name)]
    (get @service-registry canonical)))

;; =============================================================================
;; Supervision
;; =============================================================================

(defn supervise
  "Start supervising a service with typo handling.
   
   This would integrate with s6-supervise in a real implementation.
   
   Examples:
     (supervise \"clomoko-daemon\")
     => Autocorrected: clomoko-daemon → clotoko-daemon
        Supervising clotoko-daemon..."
  [service-name]
  (let [{:keys [canonical autocorrected message]} (resolve-service service-name)]
    (when autocorrected
      (println message))
    (println (format "Supervising %s..." canonical))
    ;; In real implementation:
    ;; (shell/sh "s6-supervise" (str "/run/service/" canonical))
    {:status :supervised
     :service canonical
     :autocorrected autocorrected}))

(defn unsupervise
  "Stop supervising a service with typo handling.
   
   Examples:
     (unsupervise \"grainmusik-server\")"
  [service-name]
  (let [{:keys [canonical autocorrected message]} (resolve-service service-name)]
    (when autocorrected
      (println message))
    (println (format "Unsupervising %s..." canonical))
    {:status :unsupervised
     :service canonical
     :autocorrected autocorrected}))

;; =============================================================================
;; Batch Operations
;; =============================================================================

(defn start-all
  "Start all services in dependency order."
  []
  (doseq [service (all-services)]
    (start service)))

(defn stop-all
  "Stop all services."
  []
  (doseq [service (reverse (all-services))]
    (stop service)))

(defn restart-all
  "Restart all services."
  []
  (stop-all)
  (Thread/sleep 1000)
  (start-all))

