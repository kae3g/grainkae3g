(ns humble-stack.core
  "Main interface for the complete Humble Stack.
   
   Integrates humble-desktop, grain-musl, humble-gc, grain-clj,
   and humble-repl into a unified system."
  (:require [humble-desktop.core :as desktop]
            [grain-musl.core :as musl]
            [humble-gc.core :as gc]
            [grain-clj.core :as clj]
            [humble-repl.core :as repl]
            [clojure.string :as str]))

(def stack-state
  "Global Humble Stack state."
  (atom {:initialized? false
         :components {}
         :performance-mode :optimized
         :security-enabled? true
         :monitoring-enabled? true}))

(defn initialize-stack!
  "Initialize the complete Humble Stack."
  []
  (println "🌾 Initializing Humble Stack...")
  (try
    ;; Initialize components in order
    (println "1️⃣ Initializing grain-musl...")
    (musl/initialize-musl!)
    
    (println "2️⃣ Initializing humble-gc...")
    (gc/initialize-gc!)
    
    (println "3️⃣ Initializing grain-clj...")
    (clj/initialize-compiler!)
    
    (println "4️⃣ Initializing humble-repl...")
    (repl/initialize-repl!)
    
    (println "5️⃣ Initializing humble-desktop...")
    (desktop/initialize-desktop!)
    
    ;; Mark as initialized
    (swap! stack-state assoc :initialized? true)
    
    (println "✅ Humble Stack initialized successfully!")
    (catch Exception e
      (println "❌ Error initializing Humble Stack:" (.getMessage e))
      (throw e))))

(defn shutdown-stack!
  "Shutdown the complete Humble Stack."
  []
  (println "🛑 Shutting down Humble Stack...")
  (try
    ;; Shutdown components in reverse order
    (println "5️⃣ Shutting down humble-desktop...")
    (desktop/shutdown-desktop!)
    
    (println "4️⃣ Shutting down humble-repl...")
    (repl/shutdown-repl!)
    
    (println "3️⃣ Shutting down grain-clj...")
    (clj/shutdown-compiler!)
    
    (println "2️⃣ Shutting down humble-gc...")
    (gc/shutdown-gc!)
    
    (println "1️⃣ Shutting down grain-musl...")
    (musl/shutdown-musl!)
    
    ;; Mark as not initialized
    (swap! stack-state assoc :initialized? false)
    
    (println "✅ Humble Stack shut down successfully!")
    (catch Exception e
      (println "❌ Error shutting down Humble Stack:" (.getMessage e)))))

(defn get-stack-state
  "Get current stack state."
  []
  @stack-state)

(defn update-stack-state!
  "Update stack state."
  [updates]
  (swap! stack-state merge updates))

(defn get-component-status
  "Get status of all components."
  []
  {:musl (musl/get-musl-state)
   :gc (gc/get-gc-state)
   :clj (clj/get-compiler-state)
   :repl (repl/get-repl-state)
   :desktop (desktop/get-desktop-state)})

(defn optimize-stack!
  "Optimize the complete stack."
  []
  (println "🎯 Optimizing Humble Stack...")
  (musl/optimize-for-musl!)
  (gc/optimize-gc!)
  (clj/optimize-compiler!)
  (repl/optimize-repl!)
  (desktop/optimize-desktop!)
  (println "✅ Stack optimization complete"))

(defn monitor-stack!
  "Monitor stack performance."
  []
  (when (:monitoring-enabled? @stack-state)
    (let [status (get-component-status)]
      (println "📊 Humble Stack Status:")
      (println (str "  musl: " (:initialized? (:musl status))))
      (println (str "  gc: " (:initialized? (:gc status))))
      (println (str "  clj: " (:initialized? (:clj status))))
      (println (str "  repl: " (:initialized? (:repl status))))
      (println (str "  desktop: " (:initialized? (:desktop status)))))))

(defn start-desktop!
  "Start the desktop environment."
  []
  (if (:initialized? @stack-state)
    (do
      (println "🖥️ Starting humble-desktop...")
      (desktop/start-desktop!)
      (println "✅ Desktop started"))
    (println "❌ Stack not initialized")))

(defn start-repl!
  "Start the REPL environment."
  []
  (if (:initialized? @stack-state)
    (do
      (println "🔄 Starting humble-repl...")
      (repl/start-repl!)
      (println "✅ REPL started"))
    (println "❌ Stack not initialized")))

(defn compile-code
  "Compile Clojure code."
  [code]
  (if (:initialized? @stack-state)
    (clj/compile-code code)
    (throw (Exception. "Stack not initialized"))))

(defn evaluate-code
  "Evaluate Clojure code."
  [code]
  (if (:initialized? @stack-state)
    (repl/evaluate-code code)
    (throw (Exception. "Stack not initialized"))))

(defn run-gc!
  "Run garbage collection."
  []
  (if (:initialized? @stack-state)
    (gc/run-gc!)
    (println "❌ Stack not initialized")))

(defn -main
  "Main entry point for Humble Stack."
  [& args]
  (try
    (initialize-stack!)
    (optimize-stack!)
    (monitor-stack!)
    
    (cond
      (some #{"desktop"} args)
      (start-desktop!)
      
      (some #{"repl"} args)
      (start-repl!)
      
      :else
      (do
        (println "🌾 Humble Stack ready!")
        (println "Usage:")
        (println "  clj -M -m humble-stack.core desktop  # Start desktop")
        (println "  clj -M -m humble-stack.core repl      # Start REPL")
        (println "  clj -M -m humble-stack.core           # Show status"))))
    
    (catch Exception e
      (println "❌ Error:" (.getMessage e))
      (System/exit 1))))
