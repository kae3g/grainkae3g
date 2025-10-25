# 🌾 grain6 (grainsix)

**Time-Aware Process Supervision for Grain Network**

A synthesis of `graintime` + `clojure-s6` + `clojure-sixos` that brings temporal awareness to process management.

---

## 🎯 **Concept**

**grain6** combines:
- **graintime**: Neovedic timestamps with astronomical awareness
- **clojure-s6**: Process supervision with s6
- **clojure-sixos**: System configuration and typo tolerance

**Result**: Services that understand time, astronomy, and circadian rhythms.

---

## 💡 **What Does It Do?**

### **Time-Aware Services**

Start/stop services based on:
- **Solar time** (sunrise, sunset, solar noon)
- **Lunar phases** (moon nakshatras)
- **Astrological houses** (solar house clock)
- **Custom schedules** (graintime-based cron)

### **Examples**

**GrainDisplay Service:**
```clojure
(grain6/supervise
  {:name "graindisplay"
   :command "graindisplay-wayland on"
   :schedule {:sunset :start
              :sunrise :stop}
   :location :configured})
```
- Automatically enables warm display at sunset
- Restores normal colors at sunrise
- Uses graintime location config

**Backup Service:**
```clojure
(grain6/supervise
  {:name "grain-backup"
   :command "grain-backup run"
   :schedule {:nakshatra "rohini"  ; Run during Rohini nakshatra
              :action :once-daily}})
```
- Runs backups during auspicious times
- Uses Vedic lunar mansions for scheduling

**Development Server:**
```clojure
(grain6/supervise
  {:name "grain-dev"
   :command "grain dev"
   :schedule {:solar-house 10  ; Run during 10th house (career/work)
              :restart :always}})
```
- Runs dev server during work hours
- Based on solar house clock

---

## 🏗️ **Architecture**

```
┌─────────────────────────────────────────┐
│           grain6 / grainsix             │
│                                         │
│  Time-Aware Process Supervision        │
└──────────┬──────────────────────────────┘
           │
           ├──► graintime
           │    ├── Neovedic timestamps
           │    ├── Solar calculations
           │    ├── Nakshatra tracking
           │    └── Location awareness
           │
           ├──► clojure-s6
           │    ├── Process supervision
           │    ├── Service management
           │    ├── Logging
           │    └── Dependency resolution
           │
           └──► clojure-sixos
                ├── System configuration
                ├── Typo tolerance
                └── Registry management
```

---

## 📚 **API Design**

### **Core Functions**

```clojure
(ns grain6.core
  "Time-aware process supervision"
  (:require [graintime.core :as gt]
            [graintime.solar-houses :as solar]
            [graintime.sunset :as sun]
            [clojure-s6.core :as s6]
            [clojure-sixos.core :as sixos]))

;; Supervise with time awareness
(defn supervise
  "Start a supervised service with temporal scheduling"
  [{:keys [name command schedule location restart]}]
  ...)

;; Query current time state
(defn time-state
  "Get current astronomical state"
  []
  {:graintime (gt/generate-graintime "system")
   :solar-house (solar/current-house)
   :sunrise (sun/sunrise-time)
   :sunset (sun/sunset-time)
   :nakshatra (gt/current-nakshatra)})

;; Schedule-based control
(defn should-run?
  "Check if service should run based on schedule"
  [service-name]
  ...)

;; Temporal cron
(defn grain-cron
  "Schedule based on graintime events"
  [{:keys [event action service]}]
  ...)
```

### **Schedule Syntax**

```clojure
;; Solar events
{:schedule {:sunrise :start
            :sunset :stop}}

;; Solar houses
{:schedule {:solar-house [6 7 8]  ; Morning houses
            :action :start}}

;; Nakshatras
{:schedule {:nakshatra ["rohini" "pushya"]
            :action :run-once}}

;; Custom times
{:schedule {:graintime-pattern "12025-*-*--18:00--*"
            :action :start}}

;; Combinations
{:schedule {:and [{:after :sunset}
                  {:solar-house [1 2 3 4]}]
            :action :start}}
```

---

## 🎨 **Use Cases**

### **1. Display Warmth Automation**

```clojure
;; GrainDisplay service
(grain6/supervise
  {:name "graindisplay-auto"
   :type :temporal
   :schedule {:events [{:at :sunset
                        :action (fn [] (shell "graindisplay-wayland on"))}
                       {:at :sunrise
                        :action (fn [] (shell "graindisplay-wayland off"))}]}})
```

### **2. Backup Scheduling**

```clojure
;; Backup during 11th house (gains, completion)
(grain6/supervise
  {:name "grain-backup"
   :schedule {:solar-house 11
              :frequency :daily
              :action :run-once}
   :command "grain backup all"})
```

### **3. Development Workflow**

```clojure
;; Dev server during work hours (10th house)
(grain6/supervise
  {:name "grain-dev-auto"
   :schedule {:solar-house [9 10 11]  ; Career houses
              :action :start}
   :command "grain dev"
   :auto-stop true})
```

### **4. Meditation Timer**

```clojure
;; Meditation reminder during spiritual houses
(grain6/supervise
  {:name "meditation-bell"
   :schedule {:solar-house [12 4 8]  ; Spiritual houses
              :nakshatra "rohini"     ; Auspicious nakshatra
              :action :notify}
   :command "grain-bell chime"})
```

### **5. System Maintenance**

```clojure
;; Updates during low-activity period
(grain6/supervise
  {:name "system-updates"
   :schedule {:solar-house 4  ; Midnight (home/rest)
              :frequency :weekly
              :day-of-week :sunday}
   :command "grain update system"})
```

---

## 🔧 **Implementation**

### **Directory Structure**

```
grain6/
├── README.md                  (this file)
├── deps.edn
├── bb.edn
├── src/
│   └── grain6/
│       ├── core.clj           (main API)
│       ├── scheduler.clj      (temporal scheduling)
│       ├── supervisor.clj     (s6 integration)
│       └── events.clj         (time event detection)
├── test/
│   └── grain6/
│       └── core_test.clj
└── examples/
    ├── graindisplay.edn       (display warmth automation)
    ├── backup.edn             (scheduled backups)
    └── dev-workflow.edn       (development automation)
```

---

## 🌐 **Integration with Existing Systems**

### **With GrainDaemon**

```clojure
;; graindaemon/src/graindaemon/core.clj
(require '[grain6.core :as grain6])

(grain6/supervise
  {:name "graindaemon-display"
   :schedule {:sunset :enable
              :sunrise :disable}
   :service :graindisplay})
```

### **With GrainCourse**

```clojure
;; Schedule course builds
(grain6/supervise
  {:name "graincourse-build"
   :schedule {:graintime-pattern "*-*-*--00:00--*"}  ; Midnight daily
   :command "gb graincourse:build"})
```

### **With GrainStore**

```clojure
;; Sync modules based on time
(grain6/supervise
  {:name "grainstore-sync"
   :schedule {:solar-house 2  ; 2nd house (resources)
              :frequency :hourly}
   :command "grain sync all"})
```

---

## 📊 **Comparison: Traditional vs grain6**

### **Traditional Cron**

```cron
# /etc/crontab
0 18 * * * /usr/bin/graindisplay-wayland on
0 6 * * * /usr/bin/graindisplay-wayland off
```

**Problems:**
- Sunset time changes throughout year
- Fixed to one timezone
- No astronomical awareness
- Separate from process supervision

### **grain6 Approach**

```clojure
(grain6/supervise
  {:name "graindisplay"
   :schedule {:sunset :start
              :sunrise :stop}
   :location :configured})
```

**Benefits:**
- Automatically adjusts to changing sunset times
- Uses configured location from graintime
- Integrated with s6 supervision
- Restart on failure
- Logging built-in
- Astronomical precision

---

## 🎓 **Philosophy**

### **Time as a First-Class Citizen**

Most process managers treat time as an afterthought:
- "Run at 6 PM"
- "Run every hour"
- "Run on Monday"

**grain6 treats time as fundamental:**
- "Run at sunset" (astronomical event)
- "Run during Rohini nakshatra" (lunar mansion)
- "Run in 10th solar house" (sun's diurnal position)

### **From Mechanical to Organic**

Traditional scheduling is **mechanical**:
- Clock-based
- Ignores natural cycles
- Same for all locations

grain6 scheduling is **organic**:
- Event-based
- Follows natural cycles (sun, moon)
- Location-aware
- Culturally integrated (Vedic time)

### **Synthesis Over Separation**

Instead of:
- cron (scheduling)
- systemd (supervision)
- Time separately managed

**grain6 unifies:**
- Scheduling + Supervision + Time
- One coherent system
- Graintime-native

---

## 🔮 **Future Enhancements**

### **Phase 1: Core (v0.1.0)**
- [x] Design and documentation
- [ ] Basic supervision with graintime
- [ ] Solar event triggers
- [ ] Simple schedule syntax

### **Phase 2: Astronomy (v0.2.0)**
- [ ] Nakshatra-based scheduling
- [ ] Solar house triggers
- [ ] Moon phase awareness
- [ ] Planetary hour calculations

### **Phase 3: Advanced (v0.3.0)**
- [ ] Complex schedule combinations (AND, OR, NOT)
- [ ] Service dependencies with time constraints
- [ ] Predictive scheduling (pre-start services before events)
- [ ] Historical time tracking

### **Phase 4: Integration (v0.4.0)**
- [ ] GrainDaemon full integration
- [ ] Web UI for schedule management
- [ ] Notification system
- [ ] Calendar export (iCal with graintime)

---

## 🛠️ **Development**

### **Dependencies**

```clojure
{:deps {org.clojure/clojure {:mvn/version "1.11.1"}
        graintime {:local/root "../graintime"}
        clojure-s6 {:local/root "../clojure-s6"}
        clojure-sixos {:local/root "../clojure-sixos"}
        babashka/fs {:mvn/version "0.4.19"}}}
```

### **Quick Start**

```bash
# Create symlink
cd /home/xy/kae3g/grainkae3g/grainstore
ln -s grain6 grainsix

# Install
bb grain6:install

# Test
bb grain6:test

# Run example
bb grain6:example graindisplay
```

---

## 📖 **Examples**

See `examples/` directory for:
- `graindisplay.edn` - Automatic display warmth
- `backup.edn` - Time-aware backups
- `dev-workflow.edn` - Development automation
- `meditation.edn` - Spiritual practice reminders
- `system-maintenance.edn` - System tasks

---

## 🌾 **Grain Network Principles**

**grain6** embodies:

1. **Synthesis** - Combining existing grains (graintime + s6) into something new
2. **Time Awareness** - Respecting natural cycles
3. **Simplicity** - Declarative scheduling
4. **Cultural Integration** - Vedic time system
5. **Practical** - Solves real problems (display warmth, backups)

**From granules (graintime, s6) to grains (grain6) to THE WHOLE GRAIN (time-aware ecosystem)**

---

## 🔗 **See Also**

- [graintime](../graintime/README.md) - Neovedic timestamps
- [clojure-s6](../clojure-s6/README.md) - Process supervision
- [clojure-sixos](../clojure-sixos/README.md) - System configuration
- [graindaemon](../graindaemon/README.md) - Daemon management
- [graindisplay](../graindisplay/README.md) - Display warmth control

---

**Version:** 0.1.0 (Design Phase)  
**Status:** 🚧 Under Development  
**Author:** kae3g (Grain PBC)  
**License:** MIT

🌾 **Time-aware processes for a time-aware world!**
