# 🌾 grain6 - Behn-Inspired Design

**How Urbit's Behn Vane Inspires grain6 Architecture**

---

## 🎯 **Behn Overview**

**Behn** is Urbit's timer vane (kernel module). It provides:
- Timer management (priority queue)
- Wake events at specific times
- Buffered gift delivery (`%drip`)
- Crash isolation
- Memory-efficient timer storage

**Key Insight:** Behn separates **timing concerns** from **action execution**

---

## 🏗️ **Behn Concepts → grain6 Implementation**

### **1. Timer Priority Queue**

**Behn Approach:**
```hoon
timers.state  :: (set [time=@da duct])
```
- Sorted set of timers
- Each timer has a time and a return path (duct)
- Next wake time calculated from queue head

**grain6 Approach:**
```clojure
(def timer-queue
  "Priority queue of scheduled events"
  (atom (sorted-map)))  ; {timestamp -> [events]}

(defn schedule-event
  "Add event to timer queue"
  [time event]
  (swap! timer-queue update time (fnil conj []) event))

(defn next-wake-time
  "Get next scheduled event time"
  []
  (first (keys @timer-queue)))
```

### **2. %wait Task (Set Timer)**

**Behn API:**
```hoon
[%wait @da]  :: Set timer for specific time
→ Returns [%wake] when time arrives
```

**grain6 API:**
```clojure
(defn grain-wait
  "Set timer for specific graintime event
   
   Examples:
     (grain-wait {:at :sunset
                  :action #(start-service \"graindisplay\")})
     
     (grain-wait {:at {:solar-house 10}
                  :action #(start-service \"dev-server\")})"
  [{:keys [at action repeat] :as opts}]
  (let [wake-time (calculate-wake-time at)]
    (schedule-event wake-time action)))
```

### **3. %drip Task (Delayed Gift)**

**Behn Concept:**
```hoon
[%drip mov=vase]  :: Delay a gift until timer fires
```
- Protects from cascading crashes
- Buffers between producers and consumers
- Ensures timing isolation

**grain6 Implementation:**
```clojure
(defn grain-drip
  "Delay action execution until specified time
   
   Provides crash isolation - if action fails, it doesn't
   affect the caller.
   
   Examples:
     (grain-drip
       {:delay-until :sunset
        :action #(apply-display-warmth)
        :on-error :log})"
  [{:keys [delay-until action on-error]}]
  (schedule-event 
    (calculate-wake-time delay-until)
    (fn []
      (try
        (action)
        (catch Exception e
          (case on-error
            :log (log-error e)
            :ignore nil
            :crash (throw e)))))))
```

### **4. %rest Task (Cancel Timer)**

**Behn API:**
```hoon
[%rest @da]  :: Cancel timer at specified time
```

**grain6 API:**
```clojure
(defn grain-rest
  "Cancel scheduled event
   
   Examples:
     (grain-rest sunset-display-event)
     (grain-rest {:at :sunset :service \"graindisplay\"})"
  [event-id-or-spec]
  (remove-from-timer-queue event-id-or-spec))
```

### **5. Priority Queue Algorithm**

**Behn Implementation:**
- Sorted set ordered by time
- O(log n) insertion
- O(1) peek at next timer
- Unix provides actual timing

**grain6 Implementation:**
```clojure
(defn process-timer-queue
  "Check and process ready timers
   
   Similar to Behn's wake processing"
  []
  (let [now (System/currentTimeMillis)
        ready-timers (take-while 
                       (fn [[time _]] (<= time now))
                       @timer-queue)]
    (doseq [[time events] ready-timers]
      (doseq [event events]
        (execute-event event))
      (swap! timer-queue dissoc time))))

;; Background timer processor
(defn start-timer-loop
  "Start background loop that processes timers
   
   Inspired by Behn's Unix timer integration"
  []
  (go-loop []
    (process-timer-queue)
    (<! (timeout 1000))  ; Check every second
    (recur)))
```

---

## 🌾 **grain6 Enhancements Beyond Behn**

### **1. Astronomical Events**

**Behn:** Fixed timestamps (`@da`)

**grain6:** Astronomical events
```clojure
;; Behn style (fixed time)
(grain-wait {:at #inst "2025-10-24T06:30:00"
             :action start-service})

;; grain6 enhancement (astronomical)
(grain-wait {:at :sunrise  ; Calculated daily
             :action start-service})

(grain-wait {:at {:solar-house 10}  ; Varies throughout day
             :action start-dev-server})
```

### **2. Repeating Events**

**Behn:** One-shot timers (reset manually)

**grain6:** Native repeat support
```clojure
(grain-wait {:at :sunset
             :repeat :daily
             :action enable-warm-display})

(grain-wait {:at {:nakshatra "rohini"}
             :repeat :lunar-month
             :action run-auspicious-backup})
```

### **3. Event Conditions**

**Behn:** Simple time-based

**grain6:** Complex conditions
```clojure
(grain-wait {:when {:and [{:after :sunset}
                          {:solar-house [1 2 3]}
                          {:nakshatra "rohini"}]}
             :action special-task})
```

### **4. Time-Aware Service Lifecycle**

**Behn:** Timer fires → wake gift → app handles

**grain6:** Timer fires → service lifecycle management
```clojure
(grain-supervise
  {:name "graindisplay"
   :start-at :sunset
   :stop-at :sunrise
   :restart :on-failure
   :buffer-crashes true})  ; Like Behn's %drip crash isolation
```

---

## 📊 **Comparison Table**

| Feature | Behn (Urbit) | grain6 (Grain Network) |
|---------|--------------|------------------------|
| **Timer Storage** | Sorted set | Priority queue (sorted-map) |
| **Time Type** | `@da` (128-bit) | Unix timestamp + graintime |
| **Scheduling** | Absolute time | Astronomical events |
| **Repeat** | Manual reset | Native repeat support |
| **Crash Isolation** | %drip buffer | Exception handling + s6 supervision |
| **Integration** | Kernel vane | Standalone library |
| **API Style** | Hoon tasks/gifts | Clojure functions |
| **Wake Source** | Unix timer | JVM timer + astronomical |

---

## 🎨 **grain6 Task Types** (Behn-Inspired)

### **Core Tasks**

```clojure
;; %wait equivalent - Schedule event
(grain-wait {:at time :action fn})

;; %drip equivalent - Buffered execution
(grain-drip {:delay-until time :action fn})

;; %rest equivalent - Cancel event
(grain-rest event-id)

;; %born equivalent - Initialize/recalculate
(grain-recalculate-all)

;; Enhancement - Supervise with timing
(grain-supervise {:name str :schedule map})
```

### **Behn Gifts → grain6 Events**

**Behn Gifts:**
- `%wake` - Timer fired
- `%doze` - Next wake time
- `%meta` - Wrapped gift from drip

**grain6 Events:**
```clojure
{:type :wake
 :time timestamp
 :event-id uuid
 :result :success/:failure}

{:type :next-wake
 :time timestamp
 :graintime "..."}

{:type :astronomical-event
 :event :sunrise/:sunset/:nakshatra-change
 :graintime "..."}
```

---

## 🔄 **Event Flow Comparison**

### **Behn Flow**

```
App --%pass--> Behn --%wait--> Unix Timer
                ↓
                %wake
                ↓
App <--%give-- Behn <--%wake-- Unix
```

### **grain6 Flow**

```
Service Config --%supervise--> grain6 --%schedule--> Timer Queue
                                 ↓
                         Astronomical Calculator
                                 ↓
                         JVM Timer / Async Loop
                                 ↓
Event Processor <--%wake-- Timer Queue
                                 ↓
                         s6 Service Control
                                 ↓
                         Service Start/Stop
```

---

## 💡 **Key Lessons from Behn**

### **1. Simplicity**

Behn is ~500 lines of Hoon
- Simple priority queue
- Clear task/gift interface
- Minimal state

**grain6 Goal:** Keep it simple
- Thin wrapper over existing tools
- Clear API
- Minimal overhead

### **2. Crash Isolation**

Behn's `%drip` prevents cascading failures

**grain6 Approach:**
```clojure
(defn safe-execute
  "Execute action with crash isolation (Behn %drip style)"
  [action on-error]
  (future  ; Run in separate thread
    (try
      (action)
      (catch Exception e
        (handle-error e on-error)))))
```

### **3. Memory Efficiency**

Behn reports memory usage (`%wegh` task)

**grain6 Addition:**
```clojure
(defn grain-memory-report
  "Report timer queue memory usage"
  []
  {:timer-count (count (mapcat second @timer-queue))
   :queue-size (count @timer-queue)
   :next-wake (next-wake-time)})
```

### **4. Kernel Integration**

Behn is a kernel vane - deeply integrated

**grain6 Philosophy:**
- Not kernel-level
- But ecosystem-level
- Integration through grainregistry
- Used by all Grain Network tools

---

## 🌐 **grain6 as "Behn for the Grain Network"**

### **Urbit:**
```
Kernel (Arvo)
├── Behn (timer)
├── Ames (networking)
├── Clay (filesystem)
├── Gall (userspace)
└── ...
```

### **Grain Network:**
```
Ecosystem
├── grain6 (time-aware supervision)  ← "Behn-inspired"
├── grainsource (version control)
├── graintime (timestamps)
├── grainregistry (name resolution)
└── ...
```

**grain6 serves the same role as Behn:**
- Central timing authority
- Event scheduling
- Wake notification
- Crash isolation

But extended with:
- Astronomical awareness (graintime)
- Process supervision (s6)
- Typo tolerance (grainregistry)

---

## 📚 **Complete grain6 API (Behn-Inspired)**

```clojure
(ns grain6.behn
  "Behn-inspired timer and scheduling system for grain6")

;; Core timer operations
(defn wait [time action]
  "Schedule action at specific time (Behn %wait)")

(defn drip [delay action]
  "Buffer action execution (Behn %drip)")

(defn rest [event-id]
  "Cancel scheduled event (Behn %rest)")

(defn wake []
  "Process ready timers (Behn %wake)")

;; grain6 enhancements
(defn wait-astronomical [event action]
  "Wait for astronomical event (sunrise, sunset, nakshatra)")

(defn supervise-temporal [service schedule]
  "Supervise service with time-based lifecycle")

(defn repeat-daily [event action]
  "Repeat action daily at same event")

(defn next-wake-graintime []
  "Get next wake time as graintime")
```

---

## 🎯 **Next Steps**

1. **Implement priority queue** - Sorted-map based timer storage
2. **Add astronomical calculator** - graintime event detection
3. **Create event processor** - Background async loop
4. **Integrate with s6** - Service lifecycle control
5. **Add crash isolation** - drip-style buffering
6. **Memory reporting** - wegh-style statistics

---

**Version:** 0.1.0  
**Date:** October 23, 2025  
**Author:** kae3g (Grain PBC)

🌾 **Behn's elegant simplicity + grain6's astronomical awareness!**
