# 🌀 grainwormhole - Time & Space Travel Testing

> **"Test anywhere, anytime: grainmock + grainlocation + graintime"**

A comprehensive testing utility that combines time-travel (graintime), location-travel (grainlocation), and API mocking for complete Grain Network testing.

**Synonyms**: grainwormhole = grainmock (same utility, different names for different contexts)

---

## 📋 **Philosophy**

**"Travel through time and space to test your code"**

```
╔══════════════════════════════════════════════════════════════╗
║                                                              ║
║   🌀 grainwormhole: Enter the wormhole, test anywhere       ║
║                                                              ║
║   TIME:     graintime   (when)                              ║
║   SPACE:    grainlocation (where)                           ║
║   CONTEXT:  grainpath   (journey)                           ║
║                                                              ║
║   Test your code at:                                        ║
║   - Any date/time (past, present, future)                   ║
║   - Any location (Caspar, Mumbai, Tokyo)                    ║
║   - Any weather (storm, clear, marine layer)                ║
║   - Any network (Starlink, cellular, offline)               ║
║                                                              ║
╚══════════════════════════════════════════════════════════════╝
```

---

## 🎯 **Core API**

### **wormhole Macro**

```clojure
(ns grainwormhole.core
  (:require [graintime.core :as gt]
            [grainlocation.core :as loc]))

(defmacro wormhole
  "Travel to any time and place for testing
   
   Usage:
   (wormhole {:time \"2025-10-24T06:00:00\"
              :location \"caspar-coastal-cabin\"}
     (graintime/generate-graintime \"test\"))"
  [{:keys [time location weather network]} & body]
  `(binding [graintime.core/*current-time* ~time
             grainlocation.core/*current-location* ~location
             grainwormhole.env/*weather* ~weather
             grainwormhole.env/*network* ~network]
     ~@body))

;; Shorthand forms
(defmacro at-time [time & body]
  `(wormhole {:time ~time} ~@body))

(defmacro at-location [location & body]
  `(wormhole {:location ~location} ~@body))

(defmacro during-storm [& body]
  `(wormhole {:weather :storm} ~@body))
```

### **Time Travel Helpers**

```clojure
;; Symbolic times (resolved to actual times based on location)
(wormhole {:time :sunrise :location "caspar-coastal-cabin"}
  ;; Resolves to actual Caspar sunrise time
  (test-my-code))

(wormhole {:time :sunset :location "mumbai-office"}
  ;; Resolves to Mumbai sunset time  
  (test-my-code))

;; Relative times
(wormhole {:time "+2hours" :location "current"}
  ;; 2 hours from now
  (test-my-code))

(wormhole {:time "-3days" :location "caspar-coastal-cabin"}
  ;; 3 days ago at Caspar
  (test-my-code))
```

---

## 🧪 **Testing Patterns**

### **Test Across Day/Night Cycle**

```clojure
(deftest graintime-across-day
  (testing "Ascendant changes correctly throughout day"
    (let [times [:midnight :pre-dawn :sunrise :morning 
                 :noon :afternoon :sunset :evening]
          results (map (fn [t]
                        (wormhole {:time t :location "caspar-coastal-cabin"}
                          {:time t
                           :graintime (gt/generate-graintime "test")
                           :ascendant (gt/current-ascendant)}))
                      times)]
      
      ;; Ascendants should all be different
      (is (apply distinct? (map :ascendant results))
          "Each time of day should have different ascendant"))))
```

### **Test Across Locations**

```clojure
(deftest graintime-across-locations
  (testing "Graintime adapts to different locations"
    (let [locations ["caspar-coastal-cabin"  ; California
                     "mumbai-office"         ; India
                     "tokyo-coworking"]      ; Japan
          results (map (fn [loc]
                        (wormhole {:time "12:00:00" :location loc}
                          {:location loc
                           :graintime (gt/generate-graintime "test")
                           :timezone (gt/current-timezone)}))
                      locations)]
      
      ;; Timezones should all be different
      (is (apply distinct? (map :timezone results))
          "Each location should have different timezone"))))
```

### **Test Weather Impact on grainwifi**

```clojure
(deftest grainwifi-weather-switching
  (testing "grainwifi switches to cellular during storms"
    ;; Clear weather: Prefer Starlink
    (wormhole {:location "caspar-coastal-cabin"
               :weather :clear}
      (is (= :starlink (grainwifi/preferred-connection))))
    
    ;; Storm: Switch to cellular
    (wormhole {:location "caspar-coastal-cabin"
               :weather :storm
               :wind-speed 50}
      (is (= :cellular (grainwifi/preferred-connection))))))
```

---

## 🌾 **Dependencies**

```
grainwormhole
    ├── graintime (temporal context)
    ├── grainlocation (spatial context)
    ├── grainpath (journey context)
    └── clojure.spec (validation)
```

---

## 🎓 **For App Developers**

**Simple testing workflow**:

```clojure
;; 1. Define your locations
(grainlocation/define-personal
  {:nickname "my-dev-machine"
   :lat 37.9735 :lon -122.5311})

;; 2. Test at different times
(wormhole {:time :sunrise :location "my-dev-machine"}
  (test-my-grain6-service))

(wormhole {:time :sunset :location "my-dev-machine"}
  (test-my-grain6-service))

;; 3. Test at different locations
(wormhole {:location "tokyo-office"}
  (test-my-graintime-app))

;; That's it! Simple, understandable, quick.
```

---

## 📁 **Template/Personal Split**

### **grain6pbc/grainwormhole** (Template)
- Core macros and utilities
- Common test patterns
- personal/ .gitignored

### **kae3g/grainkae3gwormhole** (Personal)
- Caspar cabin test scenarios
- Starlink/cellular patterns
- personal/ versioned in git

---

**Status**: 🌱 Design complete, ready for implementation  
**Synonym**: grainwormhole = grainmock  
**Integration**: graintime + grainlocation + grainpath  

🌾 **now == next + 1** (but make it traversable through space-time, chief!) 🌀✨
