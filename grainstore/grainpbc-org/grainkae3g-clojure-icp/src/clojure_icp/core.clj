(ns clojure-icp.core
  "Main API for clojure-icp (clojure-dfinity).
   Provides high-level interface for Internet Computer Protocol (ICP) integration."
  (:require [clojure-icp.agent :as agent]
            [clojure-icp.candid :as candid]
            [clojure-icp.identity :as identity]
            [clojure-icp.canister :as canister]
            [clojure-icp.http :as http]
            [clojure.tools.logging :as log]))

;;; Version Information

(def ^:const VERSION "0.1.0")
(def ^:const LIBRARY_NAME "clojure-icp")
(def ^:const LIBRARY_ALIAS "clojure-dfinity")

;;; Agent Creation

(defn create-agent
  "Create an IC agent for interacting with canisters.
   
   Options:
   :host - IC host URL (default 'https://ic0.app')
   :identity - Identity for signing requests (optional)
   :timeout - Request timeout in ms (default 30000)
   :local? - Use local replica (default false)
   
   Returns agent instance."
  [& {:keys [host identity timeout local?]
      :or {host "https://ic0.app"
           timeout 30000
           local? false}}]
  (log/info "Creating IC agent:" (if local? "local replica" host))
  (agent/create-agent :host (if local? "http://localhost:4943" host)
                      :identity identity
                      :timeout timeout))

;;; Canister Interaction

(defn call
  "Call a canister method (update call).
   
   Options:
   :agent - IC agent (required)
   :canister-id - Canister ID (required)
   :method - Method name (required)
   :args - Method arguments (will be encoded to Candid) (required)
   
   Returns decoded response."
  [agent & {:keys [canister-id method args]}]
  (log/info "Calling canister:" canister-id "method:" method)
  (let [encoded-args (candid/encode args)
        response (agent/call agent
                            :canister-id canister-id
                            :method method
                            :args encoded-args)]
    (candid/decode response)))

(defn query
  "Query a canister method (read-only).
   
   Options:
   :agent - IC agent (required)
   :canister-id - Canister ID (required)
   :method - Method name (required)
   :args - Method arguments (will be encoded to Candid) (required)
   
   Returns decoded response."
  [agent & {:keys [canister-id method args]}]
  (log/info "Querying canister:" canister-id "method:" method)
  (let [encoded-args (candid/encode args)
        response (agent/query agent
                             :canister-id canister-id
                             :method method
                             :args encoded-args)]
    (candid/decode response)))

;;; Canister Management

(defn create-canister
  "Create a new canister client.
   
   Options:
   :canister-id - Canister ID (required)
   :agent - IC agent (required)
   :interface - Candid interface definition (optional)
   
   Returns canister client instance."
  [& {:keys [canister-id agent interface]}]
  (log/info "Creating canister client:" canister-id)
  (canister/create :canister-id canister-id
                   :agent agent
                   :interface interface))

(defn deploy-canister
  "Deploy a new canister.
   
   Options:
   :agent - IC agent (required)
   :wasm-module - WASM module bytes (required)
   :init-args - Initialization arguments (optional)
   :cycles - Initial cycles (optional)
   
   Returns canister ID."
  [agent & {:keys [wasm-module init-args cycles]}]
  (log/info "Deploying canister")
  (canister/deploy agent
                   :wasm-module wasm-module
                   :init-args init-args
                   :cycles cycles))

(defn install-code
  "Install or upgrade code on a canister.
   
   Options:
   :agent - IC agent (required)
   :canister-id - Canister ID (required)
   :wasm-module - WASM module bytes (required)
   :mode - :install, :reinstall, or :upgrade (default :install)
   :args - Initialization arguments (optional)
   
   Returns true if successful."
  [agent & {:keys [canister-id wasm-module mode args]
            :or {mode :install}}]
  (log/info "Installing code on canister:" canister-id "mode:" mode)
  (canister/install-code agent
                        :canister-id canister-id
                        :wasm-module wasm-module
                        :mode mode
                        :args args))

;;; Identity Management

(defn generate-identity
  "Generate a new identity.
   
   Returns identity instance with private/public keypair."
  []
  (log/info "Generating new identity")
  (identity/generate))

(defn identity-from-pem
  "Load identity from PEM file.
   
   Options:
   :pem-file - Path to PEM file (required)
   
   Returns identity instance."
  [& {:keys [pem-file]}]
  (log/info "Loading identity from PEM file:" pem-file)
  (identity/from-pem :pem-file pem-file))

(defn get-principal
  "Get principal from identity.
   
   Options:
   :identity - Identity instance (required)
   
   Returns principal string."
  [& {:keys [identity]}]
  (identity/principal identity))

;;; Utility Functions

(defn version
  "Get library version information.
   
   Returns version map."
  []
  {:name LIBRARY_NAME
   :alias LIBRARY_ALIAS
   :version VERSION})

(defn local-replica?
  "Check if agent is connected to local replica.
   
   Options:
   :agent - IC agent (required)
   
   Returns true if local."
  [agent]
  (agent/local? agent))

;;; Common Canister Patterns

(defn get-canister-status
  "Get status of a canister.
   
   Options:
   :agent - IC agent (required)
   :canister-id - Canister ID (required)
   
   Returns status map with:
   {:status :running|:stopping|:stopped
    :memory_size ...
    :cycles ...
    :module_hash ...}"
  [agent & {:keys [canister-id]}]
  (log/info "Getting canister status:" canister-id)
  (canister/status agent :canister-id canister-id))

(defn stop-canister
  "Stop a canister.
   
   Options:
   :agent - IC agent (required)
   :canister-id - Canister ID (required)
   
   Returns true if successful."
  [agent & {:keys [canister-id]}]
  (log/info "Stopping canister:" canister-id)
  (canister/stop agent :canister-id canister-id))

(defn start-canister
  "Start a canister.
   
   Options:
   :agent - IC agent (required)
   :canister-id - Canister ID (required)
   
   Returns true if successful."
  [agent & {:keys [canister-id]}]
  (log/info "Starting canister:" canister-id)
  (canister/start agent :canister-id canister-id))

(defn delete-canister
  "Delete a canister.
   
   Options:
   :agent - IC agent (required)
   :canister-id - Canister ID (required)
   
   Returns true if successful."
  [agent & {:keys [canister-id]}]
  (log/info "Deleting canister:" canister-id)
  (canister/delete agent :canister-id canister-id))

;;; Convenience Macros

(defmacro with-agent
  "Execute body with an IC agent in scope.
   
   Example:
   (with-agent [agent {:host 'https://ic0.app'}]
     (call agent :canister-id '...' :method '...' :args {}))"
  [[binding opts] & body]
  `(let [~binding (create-agent ~@(apply concat opts))]
     ~@body))

(defmacro with-local-replica
  "Execute body with a local replica agent.
   
   Example:
   (with-local-replica [agent]
     (call agent :canister-id '...' :method '...' :args {}))"
  [[binding] & body]
  `(with-agent [~binding {:local? true}]
     ~@body))

;;; Pretty Printing

(defn pp-principal
  "Pretty print a principal.
   
   Options:
   :principal - Principal string (required)
   
   Returns formatted string."
  [& {:keys [principal]}]
  (str "Principal: " principal))

(defn pp-canister-id
  "Pretty print a canister ID.
   
   Options:
   :canister-id - Canister ID (required)
   
   Returns formatted string."
  [& {:keys [canister-id]}]
  (str "Canister: " canister-id))


