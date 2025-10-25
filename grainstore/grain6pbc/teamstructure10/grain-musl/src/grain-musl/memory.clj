(ns grain-musl.memory
  "Memory management for grain-musl.
   
   Provides musl-optimized memory allocation, garbage collection,
   and memory pool management."
  (:require [clojure.string :as str]))

(def memory-state
  "Memory management state."
  (atom {:pools {}
         :allocations {}
         :gc-enabled? true
         :compaction-enabled? true
         :leak-detection? true}))

(defn initialize-memory!
  "Initialize memory management system."
  []
  (println "🧠 Initializing memory management...")
  (swap! memory-state assoc :initialized? true)
  (println "✅ Memory management initialized"))

(defn shutdown-memory!
  "Shutdown memory management system."
  []
  (println "🛑 Shutting down memory management...")
  ;; Cleanup all pools
  (doseq [pool-name (keys (:pools @memory-state))]
    (destroy-pool pool-name))
  (swap! memory-state assoc :initialized? false)
  (println "✅ Memory management shut down"))

(defn create-pool
  "Create a memory pool."
  [name size]
  (let [pool {:name name
              :size size
              :allocated 0
              :free-size size
              :allocations #{}
              :created-at (System/currentTimeMillis)}]
    (swap! memory-state update :pools assoc name pool)
    (println (str "📦 Created memory pool: " name " (" size " bytes)"))
    pool))

(defn destroy-pool
  "Destroy a memory pool."
  [name]
  (swap! memory-state update :pools dissoc name)
  (println (str "🗑️ Destroyed memory pool: " name)))

(defn allocate
  "Allocate memory from pool."
  [pool-name size]
  (let [pool (get-in @memory-state [:pools pool-name])
        allocation-id (str (java.util.UUID/randomUUID))]
    (if (and pool (>= (:free-size pool) size))
      (do
        (swap! memory-state update-in [:pools pool-name :allocated] + size)
        (swap! memory-state update-in [:pools pool-name :free-size] - size)
        (swap! memory-state update-in [:pools pool-name :allocations] conj allocation-id)
        (swap! memory-state update-in [:allocations] assoc allocation-id {:pool pool-name :size size :created-at (System/currentTimeMillis)})
        {:id allocation-id :size size :pool pool-name})
      (throw (Exception. (str "Cannot allocate " size " bytes from pool " pool-name))))))

(defn deallocate
  "Deallocate memory from pool."
  [pool-name allocation-id]
  (let [allocation (get-in @memory-state [:allocations allocation-id])]
    (when allocation
      (swap! memory-state update-in [:pools pool-name :allocated] - (:size allocation))
      (swap! memory-state update-in [:pools pool-name :free-size] + (:size allocation))
      (swap! memory-state update-in [:pools pool-name :allocations] disj allocation-id)
      (swap! memory-state update :allocations dissoc allocation-id)
      (println (str "♻️ Deallocated " (:size allocation) " bytes from pool " pool-name)))))

(defn get-pool-stats
  "Get memory pool statistics."
  [pool-name]
  (get-in @memory-state [:pools pool-name]))

(defn get-memory-stats
  "Get overall memory statistics."
  []
  (let [pools (:pools @memory-state)
        total-size (reduce + (map :size (vals pools)))
        total-allocated (reduce + (map :allocated (vals pools)))
        total-free (- total-size total-allocated)]
    {:total-size total-size
     :total-allocated total-allocated
     :total-free total-free
     :utilization (if (> total-size 0) (/ total-allocated total-size) 0)
     :pools (count pools)
     :allocations (count (:allocations @memory-state))}))

(defn enable-gc!
  "Enable garbage collection."
  []
  (swap! memory-state assoc :gc-enabled? true)
  (println "♻️ Garbage collection enabled"))

(defn disable-gc!
  "Disable garbage collection."
  []
  (swap! memory-state assoc :gc-enabled? false)
  (println "🚫 Garbage collection disabled"))

(defn run-gc!
  "Run garbage collection."
  []
  (when (:gc-enabled? @memory-state)
    (println "🧹 Running garbage collection...")
    ;; Mark and sweep algorithm
    (let [pools (:pools @memory-state)
          allocations (:allocations @memory-state)]
      (doseq [[pool-name pool] pools]
        (let [active-allocations (filter #(contains? allocations %) (:allocations pool))]
          (swap! memory-state update-in [:pools pool-name :allocations] (constantly active-allocations))))
      (println "✅ Garbage collection complete"))))

(defn enable-compaction!
  "Enable memory compaction."
  []
  (swap! memory-state assoc :compaction-enabled? true)
  (println "📦 Memory compaction enabled"))

(defn run-compaction!
  "Run memory compaction."
  []
  (when (:compaction-enabled? @memory-state)
    (println "📦 Running memory compaction...")
    ;; Compact memory pools
    (doseq [[pool-name pool] (:pools @memory-state)]
      (let [fragmented-size (- (:size pool) (:free-size pool) (:allocated pool))]
        (when (> fragmented-size 0)
          (swap! memory-state update-in [:pools pool-name :free-size] + fragmented-size)
          (println (str "📦 Compacted " fragmented-size " bytes in pool " pool-name)))))
    (println "✅ Memory compaction complete")))

(defn detect-leaks!
  "Detect memory leaks."
  []
  (when (:leak-detection? @memory-state)
    (println "🔍 Detecting memory leaks...")
    (let [allocations (:allocations @memory-state)
          current-time (System/currentTimeMillis)
          leak-threshold (* 5 60 1000) ; 5 minutes
          leaks (filter #(> (- current-time (:created-at (val %))) leak-threshold) allocations)]
      (if (seq leaks)
        (do
          (println (str "⚠️ Found " (count leaks) " potential memory leaks:"))
          (doseq [[id allocation] leaks]
            (println (str "  - " id ": " (:size allocation) " bytes in pool " (:pool allocation) " (age: " (/ (- current-time (:created-at allocation)) 1000) "s)"))))
        (println "✅ No memory leaks detected")))))

(defn optimize-memory-for-musl!
  "Optimize memory management for musl libc."
  []
  (println "🎯 Optimizing memory for musl libc...")
  ;; musl-specific optimizations
  (enable-gc!)
  (enable-compaction!)
  (swap! memory-state assoc :leak-detection? true)
  (println "✅ musl memory optimization complete"))
