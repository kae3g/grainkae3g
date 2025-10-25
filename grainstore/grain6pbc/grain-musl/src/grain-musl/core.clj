(ns grain-musl.core
  "Core interface for grain-musl library.
   
   Provides high-performance Clojure bindings to musl libc,
   optimized for Alpine Linux and musl-based systems."
  (:require [grain-musl.memory :as memory]
            [grain-musl.security :as security]
            [grain-musl.performance :as performance]
            [grain-musl.filesystem :as fs]
            [grain-musl.networking :as net]
            [grain-musl.process :as process]
            [clojure.string :as str]))

(def musl-state
  "Global musl state."
  (atom {:initialized? false
         :memory-pools {}
         :security-enabled? true
         :performance-mode :optimized
         :native-lib-loaded? false}))

(defn initialize-musl!
  "Initialize grain-musl library."
  []
  (println "🔧 Initializing grain-musl...")
  (try
    ;; Load native library
    (System/loadLibrary "grain-musl")
    (swap! musl-state assoc :native-lib-loaded? true)
    
    ;; Initialize memory management
    (memory/initialize-memory!)
    
    ;; Initialize security features
    (security/initialize-security!)
    
    ;; Initialize performance optimizations
    (performance/initialize-performance!)
    
    ;; Mark as initialized
    (swap! musl-state assoc :initialized? true)
    
    (println "✅ grain-musl initialized successfully")
    (catch Exception e
      (println "❌ Error initializing grain-musl:" (.getMessage e))
      (throw e))))

(defn shutdown-musl!
  "Shutdown grain-musl library."
  []
  (println "🛑 Shutting down grain-musl...")
  (try
    ;; Shutdown performance optimizations
    (performance/shutdown-performance!)
    
    ;; Shutdown security features
    (security/shutdown-security!)
    
    ;; Shutdown memory management
    (memory/shutdown-memory!)
    
    ;; Mark as not initialized
    (swap! musl-state assoc :initialized? false)
    
    (println "✅ grain-musl shut down successfully")
    (catch Exception e
      (println "❌ Error shutting down grain-musl:" (.getMessage e)))))

(defn get-musl-state
  "Get current musl state."
  []
  @musl-state)

(defn update-musl-state!
  "Update musl state."
  [updates]
  (swap! musl-state merge updates))

(defn is-musl-system?
  "Check if running on musl-based system."
  []
  (try
    (let [result (clojure.java.shell/sh "ldd" "--version")]
      (str/includes? (:out result) "musl"))
    (catch Exception _
      false)))

(defn get-musl-version
  "Get musl libc version."
  []
  (try
    (let [result (clojure.java.shell/sh "ldd" "--version")]
      (-> (:out result)
          (str/split #"\n")
          first
          (str/replace #"ldd \(.*\) " "")))
    (catch Exception _
      "unknown")))

(defn optimize-for-musl!
  "Optimize library for musl libc."
  []
  (when (is-musl-system?)
    (println "🎯 Optimizing for musl libc...")
    (memory/optimize-memory-for-musl!)
    (security/enable-musl-security!)
    (performance/enable-musl-performance!)
    (println "✅ musl optimization complete")))

(defn create-memory-pool
  "Create a memory pool."
  [name size]
  (memory/create-pool name size))

(defn destroy-memory-pool
  "Destroy a memory pool."
  [name]
  (memory/destroy-pool name))

(defn allocate-memory
  "Allocate memory from pool."
  [pool-name size]
  (memory/allocate pool-name size))

(defn deallocate-memory
  "Deallocate memory from pool."
  [pool-name ptr]
  (memory/deallocate pool-name ptr))

(defn secure-random
  "Generate secure random bytes."
  [size]
  (security/secure-random size))

(defn hash-secure
  "Secure hash function."
  [data]
  (security/hash data))

(defn encrypt-data
  "Encrypt data."
  [data key]
  (security/encrypt data key))

(defn decrypt-data
  "Decrypt data."
  [encrypted-data key]
  (security/decrypt encrypted-data key))

(defn benchmark-operation
  "Benchmark an operation."
  [operation]
  (performance/benchmark operation))

(defn profile-code
  "Profile code execution."
  [code]
  (performance/profile code))

(defn optimize-code
  "Optimize code for musl."
  [code]
  (performance/optimize code))

(defn -main
  "Main entry point for grain-musl."
  [& args]
  (try
    (initialize-musl!)
    (optimize-for-musl!)
    (println "🌾 grain-musl ready!")
    (catch Exception e
      (println "❌ Error:" (.getMessage e))
      (System/exit 1))))
