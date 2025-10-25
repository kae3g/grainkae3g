(ns clojure-sixos.s6
  "Integration with clojure-s6 for service supervision with typo handling.
   
   This namespace bridges clojure-sixos typo handling with clojure-s6
   supervision capabilities."
  (:require [clojure-sixos.typo :as typo]
            [clojure-sixos.service :as svc]))

;; NOTE: This would normally require clojure-s6, but we're defining the
;; integration API here. When clojure-s6 is fully implemented, uncomment:
;; (:require [clojure-s6.core :as s6])

;; =============================================================================
;; Service Definition with Typo Tolerance
;; =============================================================================

(defmacro defservice
  "Define an s6 service with automatic typo handling.
   
   This is a wrapper around clojure-s6/defservice that adds typo registration.
   
   Args:
     service-name - Canonical service name
     config - Service configuration map
   
   Examples:
     (defservice \"clotoko-daemon\"
       {:run \"./run-clotoko-daemon\"
        :typos [\"clomoko-daemon\" \"clatoko-daemon\"]
        :aliases [\"motoko-transpiler\"]
        :finish \"./finish-clotoko-daemon\"
        :env {\"CLOTOKO_ENV\" \"production\"}
        :deps [\"grainweb-daemon\"]})"
  [service-name config]
  `(do
     ;; Register typos
     (svc/defservice ~service-name ~config)
     
     ;; In real implementation with clojure-s6:
     ;; (s6/defservice ~service-name (dissoc ~config :typos :aliases))
     
     ~service-name))

;; =============================================================================
;; Service Control with Typo Handling
;; =============================================================================

(defn start
  "Start an s6 service with typo autocorrection.
   
   Examples:
     (start \"clomoko-daemon\")  ; Autocorrects to clotoko-daemon"
  [service-name]
  (svc/start service-name))

(defn stop
  "Stop an s6 service with typo autocorrection."
  [service-name]
  (svc/stop service-name))

(defn restart
  "Restart an s6 service with typo autocorrection."
  [service-name]
  (svc/restart service-name))

(defn status
  "Get s6 service status with typo autocorrection."
  [service-name]
  (svc/status service-name))

;; =============================================================================
;; Supervision
;; =============================================================================

(defn supervise
  "Start supervising a service with typo autocorrection."
  [service-name]
  (svc/supervise service-name))

(defn unsupervise
  "Stop supervising a service with typo autocorrection."
  [service-name]
  (svc/unsupervise service-name))

;; =============================================================================
;; Integration Example
;; =============================================================================

(comment
  ;; Define services with typos
  (defservice "clotoko-daemon"
    {:run "./run-clotoko-daemon"
     :typos ["clomoko-daemon" "clatoko-daemon"]
     :aliases ["motoko-transpiler"]
     :env {"CLOTOKO_ENV" "production"}})
  
  (defservice "grainmusic-server"
    {:run "./run-grainmusic-server"
     :typos ["grainmusik-server" "grain-music-server"]
     :aliases ["gmusic-server"]
     :deps ["clotoko-daemon"]})
  
  ;; Start with typo - autocorrects!
  (start "clomoko-daemon")
  ;; => Autocorrected: clomoko-daemon → clotoko-daemon
  ;;    Starting clotoko-daemon...
  
  (start "grainmusik-server")
  ;; => Autocorrected: grainmusik-server → grainmusic-server
  ;;    Starting grainmusic-server...
  
  ;; Check status with typo
  (status "clomoko-daemon")
  ;; => Autocorrected: clomoko-daemon → clotoko-daemon
  ;;    Status for clotoko-daemon: running
  
  ;; Stop with typo
  (stop "grainmusik-server")
  ;; => Autocorrected: grainmusik-server → grainmusic-server
  ;;    Stopping grainmusic-server...
  )

