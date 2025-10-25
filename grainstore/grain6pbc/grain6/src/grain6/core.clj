(ns grain6.core
  "grain6 (grainsix): Time-aware process supervision
   
   Synthesizes graintime + clojure-s6 + clojure-sixos for
   astronomical scheduling and intelligent process management."
  (:require [clojure.string :as str]))

;; Note: Using load-file for dependencies since we're in grainstore
;; In production, these would be proper deps

(defn load-dependencies []
  "Load all required dependencies"
  ;; Load graintime
  (load-file "../graintime/src/graintime/location_dialog.clj")
  (load-file "../graintime/src/graintime/sunset.clj")
  (load-file "../graintime/src/graintime/solar_houses.clj")
  (load-file "../graintime/src/graintime/astromitra.clj")
  (load-file "../graintime/src/graintime/core.clj")
  
  ;; Load clojure-s6
  (load-file "../clojure-s6/src/clojure_s6/core.clj")
  
  ;; Load clojure-sixos
  (load-file "../clojure-sixos/src/clojure_sixos/similarity.clj")
  (load-file "../clojure-sixos/src/clojure_sixos/registry.clj")
  (load-file "../clojure-sixos/src/clojure_sixos/typo.clj")
  (load-file "../clojure-sixos/src/clojure_sixos/core.clj"))

;; =============================================================================
;; Time State
;; =============================================================================

(defn current-time-state
  "Get current astronomical and temporal state
   
   Returns a map with:
   - :graintime - Full neovedic timestamp
   - :solar-house - Current solar house (1-12)
   - :nakshatra - Current moon nakshatra
   - :sunrise - Today's sunrise time
   - :sunset - Today's sunset time
   - :is-daytime - Boolean, is it currently day?
   
   Example:
     (current-time-state)
     => {:graintime \"12025-10-23--2315--PDT--moon-vishakha--asc-gemini000--sun-05thhouse--system\"
         :solar-house 5
         :nakshatra \"vishakha\"
         :sunrise #inst \"2025-10-23T06:30:00\"
         :sunset #inst \"2025-10-23T18:00:00\"
         :is-daytime false}"
  []
  (load-dependencies)
  (let [gen-graintime (resolve 'graintime.core/generate-graintime)
        parse-graintime (resolve 'graintime.core/parse-graintime)
        graintime (gen-graintime "system")
        parsed (parse-graintime graintime)]
    {:graintime graintime
     :solar-house (parse-long (or (:solar-house parsed) "1"))
     :nakshatra (:nakshatra parsed)
     :sunrise nil  ; TODO: Calculate from graintime
     :sunset nil   ; TODO: Calculate from graintime
     :is-daytime (let [house (parse-long (or (:solar-house parsed) "1"))]
                   (or (>= house 7) (<= house 6)))}))

;; =============================================================================
;; Schedule Matching
;; =============================================================================

(defn matches-schedule?
  "Check if current time matches a schedule specification
   
   Schedule can be:
   - {:solar-house 10} - Match specific house
   - {:solar-house [9 10 11]} - Match any of these houses
   - {:nakshatra \"rohini\"} - Match specific nakshatra
   - {:nakshatra [\"rohini\" \"pushya\"]} - Match any
   - {:is-daytime true} - Match day/night
   - {:and [...]} - All conditions must match
   - {:or [...]} - Any condition matches
   
   Examples:
     (matches-schedule? {:solar-house 10})
     => true/false based on current solar house"
  [schedule]
  (let [state (current-time-state)]
    (cond
      ;; Solar house match
      (contains? schedule :solar-house)
      (let [target (:solar-house schedule)
            current (:solar-house state)]
        (if (coll? target)
          (some #{current} target)
          (= current target)))
      
      ;; Nakshatra match
      (contains? schedule :nakshatra)
      (let [target (:nakshatra schedule)
            current (:nakshatra state)]
        (if (coll? target)
          (some #{current} target)
          (= current target)))
      
      ;; Day/night match
      (contains? schedule :is-daytime)
      (= (:is-daytime schedule) (:is-daytime state))
      
      ;; AND combination
      (contains? schedule :and)
      (every? matches-schedule? (:and schedule))
      
      ;; OR combination
      (contains? schedule :or)
      (some matches-schedule? (:or schedule))
      
      ;; Default: always match
      :else true)))

;; =============================================================================
;; Service Supervision with Time Awareness
;; =============================================================================

(defn supervise
  "Supervise a service with temporal scheduling
   
   Args:
     config - Map with:
       :name - Service name (required)
       :command - Command to run (required)
       :schedule - Schedule specification (optional)
       :restart - Restart policy (:always, :on-failure, :never)
       :type - Service type (:longrun, :oneshot, :temporal)
   
   Examples:
     ;; Display warmth at sunset
     (supervise {:name \"graindisplay\"
                 :command \"graindisplay-wayland on\"
                 :schedule {:sunset :start}})
     
     ;; Backup during 11th house
     (supervise {:name \"backup\"
                 :command \"grain backup\"
                 :schedule {:solar-house 11}
                 :type :temporal})"
  [{:keys [name command schedule restart type] :as config}]
  (load-dependencies)
  (let [create-service (resolve 'clojure-s6.core/create-service)
        resolve-name (resolve 'clojure-sixos.core/resolve-name)]
    
    ;; Resolve service name with typo tolerance
    (let [canonical-name (resolve-name name)]
      (when (not= name canonical-name)
        (println (str "📝 Autocorrected: " name " → " canonical-name)))
      
      ;; Check if schedule matches
      (if (and schedule (not (matches-schedule? schedule)))
        (println (str "⏰ Service " canonical-name " not scheduled to run now"))
        (do
          (println (str "🌾 Starting service: " canonical-name))
          (create-service {:name canonical-name
                          :command command
                          :type (or type :longrun)
                          :restart (or restart :always)}))))))

;; =============================================================================
;; Temporal Cron
;; =============================================================================

(defn grain-cron
  "Schedule actions based on graintime events
   
   Args:
     schedules - Vector of schedule maps with:
       :event - Time event specification
       :action - Function to call or command to run
       :service - Service to control
   
   Examples:
     (grain-cron
       [{:event {:sunset true}
         :action (fn [] (shell \"graindisplay-wayland on\"))}
        {:event {:sunrise true}
         :action (fn [] (shell \"graindisplay-wayland off\"))}])"
  [schedules]
  (doseq [{:keys [event action service]} schedules]
    (when (matches-schedule? event)
      (if (fn? action)
        (action)
        (println (str "Running: " action))))))

;; =============================================================================
;; Query Functions
;; =============================================================================

(defn service-time-state
  "Get time-aware state for a service
   
   Returns when service should run based on schedule"
  [service-name]
  (let [state (current-time-state)]
    {:service service-name
     :current-state state
     :should-run? true  ; TODO: Check service schedule
     :next-event nil})) ; TODO: Calculate next schedule match

(defn services-by-schedule
  "Get all services that should run on a given schedule
   
   Example:
     (services-by-schedule {:solar-house 10})
     => [\"dev-server\" \"work-timer\"]"
  [schedule]
  ;; TODO: Query all registered services and filter
  [])

;; =============================================================================
;; Utilities
;; =============================================================================

(defn graintime-now
  "Get current graintime (convenience wrapper)"
  []
  (load-dependencies)
  ((resolve 'graintime.core/generate-graintime) "system"))

(defn parse-service-graintime
  "Parse graintime from service metadata"
  [service-name]
  (load-dependencies)
  (let [parse-fn (resolve 'graintime.core/parse-graintime)]
    ;; TODO: Get service metadata
    nil))

;; =============================================================================
;; CLI
;; =============================================================================

(defn -main [& args]
  "Main CLI entry point"
  (case (first args)
    "time" (do
             (println "🌾 Current Time State:")
             (println "")
             (clojure.pprint/pprint (current-time-state)))
    
    "test-schedule" (do
                      (let [schedule (read-string (second args))]
                        (println "Testing schedule:" schedule)
                        (println "Matches:" (matches-schedule? schedule))))
    
    "supervise" (do
                  (println "Starting supervised service...")
                  (let [config (read-string (slurp (second args)))]
                    (supervise config)))
    
    (do
      (println "🌾 grain6 (grainsix) - Time-Aware Process Supervision")
      (println "")
      (println "USAGE:")
      (println "  grain6 time                     Show current time state")
      (println "  grain6 test-schedule SCHEDULE   Test schedule matching")
      (println "  grain6 supervise CONFIG-FILE    Start supervised service")
      (println "")
      (println "EXAMPLES:")
      (println "  grain6 time")
      (println "  grain6 test-schedule '{:solar-house 10}'")
      (println "  grain6 supervise examples/graindisplay.edn")
      (println ""))))
