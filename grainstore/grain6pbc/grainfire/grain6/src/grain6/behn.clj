(ns grain6.behn
  "Behn-inspired timer system for grain6
   
   Implements Urbit Behn's elegant timer queue with grain6 enhancements:
   - Priority queue for timers
   - Astronomical event scheduling
   - Crash isolation (drip-style)
   - Repeat/recurring events
   - Integration with graintime and s6"
  (:require [clojure.string :as str]))

;; =============================================================================
;; State
;; =============================================================================

(defonce timer-queue
  "Priority queue of scheduled events
   
   Structure: {timestamp -> [{:id uuid :action fn :metadata map}]}"
  (atom (sorted-map)))

(defonce event-counter
  "Atomic counter for event IDs"
  (atom 0))

(defonce running?
  "Is the timer loop running?"
  (atom false))

;; =============================================================================
;; Timer Operations (Behn-Inspired)
;; =============================================================================

(defn grain-wait
  "Schedule action at specific time (Behn %wait task)
   
   Args:
     time - Unix timestamp (milliseconds)
     action - Function to execute
     metadata - Optional metadata map
   
   Returns:
     Event ID for cancellation
   
   Examples:
     (grain-wait (+ (System/currentTimeMillis) 60000)
                 #(println \"One minute passed\"))
     
     (grain-wait sunset-timestamp
                 #(shell \"graindisplay-wayland on\")
                 {:event :sunset :service \"graindisplay\"})"
  [time action & [metadata]]
  (let [event-id (swap! event-counter inc)
        event {:id event-id
               :action action
               :metadata (or metadata {})
               :created-at (System/currentTimeMillis)}]
    (swap! timer-queue update time (fnil conj []) event)
    (println (str "⏰ Scheduled event " event-id " at " 
                 (java.time.Instant/ofEpochMilli time)))
    event-id))

(defn grain-rest
  "Cancel scheduled event (Behn %rest task)
   
   Args:
     event-id - ID returned from grain-wait
   
   Examples:
     (def event-id (grain-wait ...))
     (grain-rest event-id)"
  [event-id]
  (let [removed? (atom false)]
    (swap! timer-queue
           (fn [queue]
             (into (sorted-map)
                   (map (fn [[time events]]
                          [time (filterv #(if (= (:id %) event-id)
                                           (do (reset! removed? true) false)
                                           true)
                                        events)])
                        queue))))
    (if @removed?
      (println (str "✅ Cancelled event " event-id))
      (println (str "⚠️  Event " event-id " not found")))
    @removed?))

(defn grain-drip
  "Buffer action execution for crash isolation (Behn %drip task)
   
   Executes action in separate thread to prevent crashes from
   propagating to caller.
   
   Args:
     delay-ms - Milliseconds to delay
     action - Function to execute
     on-error - Error handling strategy (:log, :ignore, :crash)
   
   Examples:
     (grain-drip 1000
                 #(risky-operation)
                 :log)"
  [delay-ms action & [on-error]]
  (let [wake-time (+ (System/currentTimeMillis) delay-ms)
        buffered-action (fn []
                         (future
                           (try
                             (action)
                             (catch Exception e
                               (case (or on-error :log)
                                 :log (println (str "❌ Drip error: " (.getMessage e)))
                                 :ignore nil
                                 :crash (throw e))))))]
    (grain-wait wake-time buffered-action {:drip true :error-handling on-error})))

(defn grain-wake
  "Process all ready timers (Behn %wake processing)
   
   Called by background loop to execute scheduled events.
   
   Returns:
     Vector of executed event IDs"
  []
  (let [now (System/currentTimeMillis)
        ready (take-while (fn [[time _]] (<= time now)) @timer-queue)
        executed (atom [])]
    
    (doseq [[time events] ready]
      (doseq [{:keys [id action metadata]} events]
        (try
          (println (str "⏰ Wake event " id " (scheduled for " 
                       (java.time.Instant/ofEpochMilli time) ")"))
          (action)
          (swap! executed conj id)
          (catch Exception e
            (println (str "❌ Event " id " failed: " (.getMessage e)))))))
    
    ;; Remove processed timers
    (swap! timer-queue
           (fn [queue]
             (into (sorted-map)
                   (drop (count ready) queue))))
    
    @executed))

(defn next-wake-time
  "Get timestamp of next scheduled event (Behn %doze equivalent)
   
   Returns:
     Unix timestamp (ms) or nil if no events scheduled"
  []
  (first (keys @timer-queue)))

(defn grain-recalculate
  "Recalculate all astronomical timers (Behn %born equivalent)
   
   Called on startup or when location changes.
   Re-calculates sunrise/sunset/etc for all pending events."
  []
  (println "🌾 Recalculating astronomical timers...")
  ;; TODO: Update all :sunrise/:sunset events with new times
  (println "✅ Timers recalculated"))

;; =============================================================================
;; grain6 Enhancements (Beyond Behn)
;; =============================================================================

(defn grain-wait-astronomical
  "Wait for astronomical event (grain6 enhancement)
   
   Args:
     event - :sunrise, :sunset, {:solar-house N}, {:nakshatra \"name\"}
     action - Function to execute
   
   Examples:
     (grain-wait-astronomical :sunset
                              #(shell \"graindisplay-wayland on\"))
     
     (grain-wait-astronomical {:solar-house 10}
                              #(start-dev-server))"
  [event action & [repeat-policy]]
  (let [time (calculate-astronomical-time event)]
    (grain-wait time action {:astronomical event :repeat repeat-policy})))

(defn grain-repeat
  "Schedule repeating event (grain6 enhancement)
   
   Args:
     interval-ms - Milliseconds between executions
     action - Function to execute
   
   Returns:
     Repeat job ID
   
   Examples:
     (grain-repeat 60000  ; Every minute
                   #(check-display-warmth))"
  [interval-ms action]
  (let [job-id (swap! event-counter inc)
        schedule-next (fn schedule-next []
                       (grain-wait (+ (System/currentTimeMillis) interval-ms)
                                  (fn []
                                    (action)
                                    (schedule-next))
                                  {:repeat-job job-id}))]
    (schedule-next)
    job-id))

(defn grain-cancel-repeat
  "Cancel repeating job"
  [job-id]
  ;; Cancel all events with this repeat-job ID
  (swap! timer-queue
         (fn [queue]
           (into (sorted-map)
                 (map (fn [[time events]]
                        [time (filterv #(not= (get-in % [:metadata :repeat-job]) job-id)
                                      events)])
                      queue))))
  (println (str "✅ Cancelled repeat job " job-id)))

;; =============================================================================
;; Background Timer Loop
;; =============================================================================

(defn start-timer-loop
  "Start background loop that processes timers
   
   Inspired by Behn's Unix timer integration but runs
   in Clojure async loop."
  []
  (when-not @running?
    (reset! running? true)
    (println "🌾 grain6 timer loop starting...")
    
    ;; Note: In production, use core.async go-loop
    ;; For now, simple thread
    (future
      (loop []
        (when @running?
          (grain-wake)
          (Thread/sleep 1000)  ; Check every second
          (recur))))
    
    (println "✅ Timer loop running")))

(defn stop-timer-loop
  "Stop the background timer loop"
  []
  (reset! running? false)
  (println "🛑 Timer loop stopped"))

;; =============================================================================
;; Utilities
;; =============================================================================

(defn calculate-astronomical-time
  "Calculate Unix timestamp for astronomical event
   
   TODO: Integrate with graintime calculations"
  [event]
  (case event
    :sunrise (+ (System/currentTimeMillis) 60000)   ; TODO: Real calc
    :sunset (+ (System/currentTimeMillis) 120000)   ; TODO: Real calc
    :now (System/currentTimeMillis)
    (if (map? event)
      (+ (System/currentTimeMillis) 60000)  ; TODO: Real calc
      (System/currentTimeMillis))))

(defn timer-statistics
  "Get timer queue statistics (Behn %wegh equivalent)"
  []
  {:total-timers (reduce + (map (comp count second) @timer-queue))
   :unique-times (count @timer-queue)
   :next-wake (next-wake-time)
   :queue-empty? (empty? @timer-queue)
   :running? @running?})

(defn list-pending-timers
  "List all pending timers"
  []
  (for [[time events] @timer-queue
        event events]
    {:id (:id event)
     :time (java.time.Instant/ofEpochMilli time)
     :metadata (:metadata event)}))

;; =============================================================================
;; CLI
;; =============================================================================

(defn -main [& args]
  "Main CLI for Behn-style timer operations"
  (case (first args)
    "start" (start-timer-loop)
    
    "stop" (stop-timer-loop)
    
    "stats" (do
              (println "🌾 grain6 Timer Statistics:")
              (println "")
              (clojure.pprint/pprint (timer-statistics)))
    
    "list" (do
             (println "🌾 Pending Timers:")
             (println "")
             (doseq [timer (list-pending-timers)]
               (println (str "  [" (:id timer) "] " (:time timer)))))
    
    "test-wait" (do
                  (start-timer-loop)
                  (println "Testing grain-wait...")
                  (grain-wait (+ (System/currentTimeMillis) 3000)
                             #(println "✅ Timer fired!"))
                  (Thread/sleep 5000))
    
    "test-drip" (do
                  (start-timer-loop)
                  (println "Testing grain-drip with crash...")
                  (grain-drip 2000
                             #(throw (Exception. "Test crash"))
                             :log)
                  (Thread/sleep 4000))
    
    (do
      (println "🌾 grain6 Behn Timer System")
      (println "")
      (println "USAGE:")
      (println "  grain6-behn start      Start timer loop")
      (println "  grain6-behn stop       Stop timer loop")
      (println "  grain6-behn stats      Show statistics")
      (println "  grain6-behn list       List pending timers")
      (println "  grain6-behn test-wait  Test timer functionality")
      (println "  grain6-behn test-drip  Test crash isolation")
      (println ""))))
