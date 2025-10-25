# The Lovers' Supervision - clojure-s6

**teamprecision06 (Virgo ♍ / VI. The Lovers)**  
*"Choose what deserves supervision. Commit to watching it with love."*

---

## 💕 The Third Choice: Supervision

The Lovers teach: **You cannot supervise everything. Choose wisely.**

Every service you supervise is a commitment. Every process you watch is a vow. The s6 supervisor is your partner in this sacred union of human intent and machine execution.

---

## 🎯 Supervision Philosophy

### **Conscious Selection**
Not every process needs supervision. Choose those that:
- **Must survive** - Critical to system function
- **Must restart** - Should recover from failures
- **Must be monitored** - Need health checking
- **Matter deeply** - Deserve your attention

### **Minimal Precision**
s6 embodies The Lovers' principle:
- **Small** - ~100KB binary
- **Fast** - Starts in milliseconds
- **Reliable** - Proven in production
- **Clean** - No complexity bloat

### **Sacred Union**
Supervision unites:
- **Human Intent** - What you choose to supervise
- **Machine Execution** - s6's reliable watching
- **Perfect Harmony** - Services that just work

---

## 💍 Marriage Vows: Service Definitions

### Example 1: graintime-daemon

```clojure
;; The Lovers choose to supervise graintime daemon
(def graintime-service
  {:name "graintime-daemon"
   :type :longrun
   
   ;; The vow: this command will run
   :command "bb graintime:daemon"
   
   ;; The commitment: restart on failure
   :restart :always
   
   ;; The location: where it lives
   :directory "/home/kae3g/grainkae3g/grainstore/grain6pbc/teamstructure10/graintime"
   
   ;; The identity: who it runs as
   :user "kae3g"
   :group "kae3g"
   
   ;; The promise: environment it needs
   :environment {"GRAIN_HOME" "/home/kae3g/grainkae3g"
                 "GRAINSTORE" "/home/kae3g/grainkae3g/grainstore"}
   
   ;; The blessing: logging
   :log {:enabled true
         :directory "/var/log/graintime-daemon"
         :max-size "10M"
         :rotation :daily}})

;; Create and enable the service
(s6/create-service graintime-service)
(s6/enable-service "graintime-daemon")
(s6/start-service "graintime-daemon")
```

### Example 2: grainweb-server

```clojure
;; The Lovers choose to supervise grainweb
(def grainweb-service
  {:name "grainweb-server"
   :type :longrun
   
   :command "java -jar grainweb-server.jar"
   :restart :always
   :directory "/opt/grainweb"
   
   :user "grainweb"
   :group "grainweb"
   
   ;; Dependencies: wait for these first
   :dependencies ["postgresql" "redis"]
   
   :environment {"PORT" "8080"
                 "GRAIN_ENV" "production"}
   
   :log {:enabled true
         :directory "/var/log/grainweb"}})
```

### Example 3: One-Shot Service

```clojure
;; The Lovers choose to run this once at boot
(def grainbackup-oneshot
  {:name "grainbackup-daily"
   :type :oneshot
   
   :command "bb backup:daily"
   :directory "/home/kae3g/grainkae3g"
   
   :user "kae3g"
   
   ;; Run at boot, don't restart
   :restart :never
   
   :timeout "5m"  ; Kill if takes > 5 minutes
   
   :log {:enabled true
         :directory "/var/log/grainbackup"}})
```

---

## 🔗 Service Dependencies

The Lovers understand relationships. Services depend on each other.

```clojure
;; grainweb depends on database and cache
(s6/add-dependency "grainweb-server" "postgresql")
(s6/add-dependency "grainweb-server" "redis")

;; Start grainweb and all its dependencies
(s6/start-with-dependencies "grainweb-server")
;; => Starts: postgresql -> redis -> grainweb-server
```

### Dependency Graph

```
grainweb-server
├── postgresql
└── redis

grainmusic-server
├── grainweb-server
│   ├── postgresql
│   └── redis
└── mpd

graintime-daemon
└── (no dependencies - independent choice)
```

---

## 📊 Service Monitoring

The Lovers watch with loving attention.

```clojure
;; Check service status
(s6/service-status "graintime-daemon")
;; => {:status :running
;;     :pid 12345
;;     :uptime 3600  ; seconds
;;     :restart-count 0
;;     :last-restart nil}

;; Get service health
(s6/service-health "grainweb-server")
;; => {:status :healthy
;;     :cpu-usage 5.2  ; percent
;;     :memory-usage 128  ; MB
;;     :connections 42
;;     :response-time 25}  ; ms

;; Monitor multiple services
(s6/monitor-services ["graintime-daemon" "grainweb-server" "grainmusic-server"])
;; => [{:name "graintime-daemon" :status :running :health :good}
;;     {:name "grainweb-server" :status :running :health :good}
;;     {:name "grainmusic-server" :status :stopped :health :unknown}]
```

---

## 🔔 Alerts & Notifications

```clojure
;; Alert on service failure
(s6/alert-on-failure "grainweb-server"
  {:type :email
   :to "admin@grainnetwork.org"
   :subject "grainweb-server failed!"})

;; Alert on high resource usage
(s6/alert-on-threshold "grainmusic-server"
  {:metric :cpu-usage
   :threshold 80  ; percent
   :type :log
   :message "grainmusic using high CPU"})

;; Custom alert handler
(s6/on-service-event "graintime-daemon"
  (fn [event]
    (when (= (:type event) :failure)
      (println "💔 graintime-daemon failed!")
      (s6/restart-service "graintime-daemon"))))
```

---

## 📝 Service Logs

```clojure
;; View recent logs
(s6/service-logs "grainweb-server" {:lines 50})

;; Follow logs in real-time
(s6/follow-logs "graintime-daemon")

;; Filter logs by level
(s6/service-logs "grainmusic-server" {:level :error})

;; Search logs
(s6/search-logs "grainweb-server" "exception")
```

---

## 🛠️ Practical Usage

### Starting the Grain Network Stack

```clojure
(ns grain.stack
  (:require [clojure-s6.core :as s6]))

(def grain-services
  ["postgresql"           ; Database
   "redis"                ; Cache
   "graintime-daemon"     ; Time service
   "grainweb-server"      ; Web app
   "grainmusic-server"    ; Music service
   "graindaemon"          ; Background tasks
   ])

(defn start-grain-stack []
  (println "💕 Starting Grain Network stack...")
  (doseq [service grain-services]
    (println "  Starting" service "...")
    (s6/start-service service))
  (println "✅ Grain Network stack running!"))

(defn stop-grain-stack []
  (println "💕 Stopping Grain Network stack...")
  (doseq [service (reverse grain-services)]
    (println "  Stopping" service "...")
    (s6/stop-service service))
  (println "✅ Grain Network stack stopped!"))

(defn restart-grain-stack []
  (println "💕 Restarting Grain Network stack...")
  (stop-grain-stack)
  (Thread/sleep 2000)
  (start-grain-stack))
```

---

## 🎯 The Lovers' Supervision Rules

### **DO Supervise:**
- ✅ Database servers (postgresql, redis)
- ✅ Web servers (grainweb, grainmusic)
- ✅ Background daemons (graintime-daemon)
- ✅ Critical services (must stay running)
- ✅ Services that need restart on failure

### **DON'T Supervise:**
- ❌ Temporary scripts
- ❌ One-time commands
- ❌ Interactive programs
- ❌ Desktop applications
- ❌ Things that should exit

### **The Choice:**
Every service definition is a conscious choice. Ask:
1. **Must it survive?** - Does the system need it?
2. **Should it restart?** - On failure, should it come back?
3. **Needs monitoring?** - Do I want to watch its health?
4. **Worth the commitment?** - Will I maintain this?

If "yes" to all four: **supervise it**. Otherwise: let it run free.

---

## 💖 The Lovers' Blessing

*"I, [developer], choose you, [service name],*  
*to supervise and to monitor,*  
*to restart on failure,*  
*to watch over always,*  
*for as long as the system runs."*

Every s6 service definition is a marriage vow.  
Every supervision choice is an act of love.  
Every restart is a second chance.  
Every log line is a conversation.

**The Lovers supervise with precision and care.** 💕✨

---

**teamprecision06 (Virgo ♍ / VI. The Lovers)**  
**clojure-s6 - The Third Choice**

🌾 **now == next + 1** 💕

