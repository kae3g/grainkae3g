# 🌾 grainmock - Testing Utility for Grain Network

> **"Time-Travel Testing: Mock Any API, Any Time, Any Place"**

A comprehensive mocking and testing utility for the Grain Network, with template/personal separation and time-travel testing support.

---

## 📋 **Philosophy**

**"Test with confidence by controlling the variables"**

When testing graintime, grain6, or grainwifi:
- ✅ Mock external APIs (astromitra, weather, etc.)
- ✅ Time-travel to any date/time for testing
- ✅ Test different locations without traveling
- ✅ Reproducible tests (same inputs = same outputs)
- ✅ Offline testing (no network required)

---

## 🎯 **Use Cases**

### **1. graintime Testing**
```clojure
;; Test ascendant at different times
(grainmock/with-time "2025-10-23T06:00:00"
  (grainmock/with-location {:lat 37.9735 :lon -122.5311}
    (gt/generate-graintime "test")))
;; Returns graintime with Gemini/Taurus rising (early morning)

(grainmock/with-time "2025-10-23T18:00:00"
  (grainmock/with-location {:lat 37.9735 :lon -122.5311}
    (gt/generate-graintime "test")))
;; Returns graintime with Scorpio/Sagittarius rising (evening)
```

### **2. grain6 Scheduling Tests**
```clojure
;; Test sunrise trigger
(grainmock/with-time "2025-10-23T07:25:00"  ; Sunrise
  (grain6/should-run? service-with-sunrise-trigger))
;; Returns true

;; Test sunset trigger
(grainmock/with-time "2025-10-23T18:22:00"  ; Sunset
  (grain6/should-run? service-with-sunset-trigger))
;; Returns true
```

### **3. grainwifi Weather Tests**
```clojure
;; Mock storm conditions
(grainmock/with-weather {:condition :storm :wind-speed 50}
  (grainwifi/should-switch-to-cellular?))
;; Returns true (Starlink affected by storm)
```

---

## 🏗️ **Architecture**

```
grain6pbc/grainmock/              ← Template (gitignored personal/)
├── src/grainmock/
│   ├── core.clj                 ← Main API
│   ├── time_travel.clj          ← Time manipulation
│   ├── astromitra_mock.clj      ← Mock astromitra API
│   ├── weather_mock.clj         ← Mock weather API
│   └── fixtures.clj             ← Common test data
├── personal/                    ← .gitignored (user-specific mocks)
│   ├── caspar_ca.edn           ← Caspar, CA specific data
│   └── test_scenarios.edn      ← Personal test cases
├── test/
│   └── grainmock/
│       └── core_test.clj
├── bb.edn
├── deps.edn
└── README.md

kae3g/grainkae3gmock/            ← Personal version
├── src/grainkae3gmock/
│   └── caspar.clj               ← Caspar-specific mocks
├── personal/                    ← NOT gitignored (versioned)
│   ├── caspar_ca.edn
│   ├── starlink_patterns.edn
│   └── storm_scenarios.edn
└── README.md
```

---

## 📦 **Core API**

### **Time Travel**

```clojure
(ns grainmock.time-travel)

(defmacro with-time
  "Execute body with mocked current time"
  [time-str & body]
  `(binding [graintime.core/*current-time* (parse-time ~time-str)]
     ~@body))

(defmacro with-date
  "Execute body with mocked date"
  [date-str & body]
  `(with-time (str ~date-str "T12:00:00") ~@body))

(defn time-travel-sequence
  "Run function at multiple times and collect results"
  [times-seq f]
  (map (fn [t] (with-time t (f))) times-seq))
```

### **Location Mocking**

```clojure
(ns grainmock.location)

(defmacro with-location
  "Execute body with mocked location"
  [{:keys [lat lon city]} & body]
  `(binding [graintime.core/*current-location* {:lat ~lat :lon ~lon :city ~city}]
     ~@body))

(def preset-locations
  {:caspar-ca {:lat 39.3625 :lon -123.8139 :city "Caspar, CA"}
   :san-rafael {:lat 37.9735 :lon -122.5311 :city "San Rafael, CA"}
   :new-york {:lat 40.7128 :lon -74.0060 :city "New York, NY"}
   :mumbai {:lat 19.0760 :lon 72.8777 :city "Mumbai, India"}
   :tokyo {:lat 35.6762 :lon 139.6503 :city "Tokyo, Japan"}})
```

### **astromitra API Mock**

```clojure
(ns grainmock.astromitra)

(def mock-planetary-data
  "Pre-calculated planetary positions for testing"
  {
   ;; Morning: Oct 23, 2025 06:00 PDT
   "2025-10-23T06:00:00"
   {:moon {:nakshatra "anuradha" :pada 1 :completed 5.5 :lord "Saturn"}
    :sun {:sign "libra" :degree 0.0}
    :ascendant {:sign "gemini" :degree 15}}  ; Gemini rises early morning
   
   ;; Evening: Oct 23, 2025 18:45 PDT
   "2025-10-23T18:45:00"
   {:moon {:nakshatra "anuradha" :pada 1 :completed 7.77 :lord "Saturn"}
    :sun {:sign "libra" :degree 0.5}
    :ascendant {:sign "scorpio" :degree 22}}  ; Scorpio rises evening
   
   ;; Noon: Oct 23, 2025 12:00 PDT
   "2025-10-23T12:00:00"
   {:moon {:nakshatra "anuradha" :pada 1 :completed 6.8 :lord "Saturn"}
    :sun {:sign "libra" :degree 0.3}
    :ascendant {:sign "virgo" :degree 28}}  ; Virgo at midday
   
   ;; Midnight: Oct 23, 2025 00:00 PDT
   "2025-10-23T00:00:00"
   {:moon {:nakshatra "vishakha" :pada 4 :completed 99.2 :lord "Jupiter"}
    :sun {:sign "libra" :degree 0.0}
    :ascendant {:sign "leo" :degree 5}}  ; Leo at midnight
   })

(defn mock-api-call
  "Mock API call with pre-loaded data"
  [datetime]
  (let [time-key (format "%04d-%02d-%02dT%02d:%02d:00"
                        (.getYear datetime)
                        (.getMonthValue datetime)
                        (.getDayOfMonth datetime)
                        (.getHour datetime)
                        (.getMinute datetime))]
    (get mock-planetary-data time-key)))

(defmacro with-mock-api
  "Execute body with mocked astromitra API"
  [& body]
  `(binding [graintime.astromitra/api-call mock-api-call]
     ~@body))
```

---

## 🧪 **Testing Strategy**

### **Test Pyramid**

```
         /\
        /  \       Unit Tests (grainmock isolated)
       /────\      
      /      \     Integration Tests (graintime + grainmock)
     /────────\    
    /          \   End-to-End (full workflow with mocks)
   /────────────\  
```

### **Test Scenarios**

```clojure
(ns grainmock.fixtures)

(def test-scenarios
  {:sunrise
   {:time "2025-10-23T07:25:00"
    :location :san-rafael
    :expected {:sun-house 1
               :ascendant #{"lib" "sco"}  ; Set of acceptable values
               :nakshatra "anuradha"}}
   
   :sunset
   {:time "2025-10-23T18:22:00"
    :location :san-rafael
    :expected {:sun-house 7
               :ascendant #{"sco" "sag"}
               :nakshatra "anuradha"}}
   
   :noon
   {:time "2025-10-23T12:54:00"
    :location :san-rafael
    :expected {:sun-house 10
               :ascendant #{"vir" "lib"}
               :nakshatra "anuradha"}}
   
   :midnight
   {:time "2025-10-23T00:54:00"
    :location :san-rafael
    :expected {:sun-house 4
               :ascendant #{"leo" "vir"}
               :nakshatra #{"vishakha" "anuradha"}}}  ; Moon changes during day
   
   :caspar-storm
   {:time "2025-10-23T14:00:00"
    :location :caspar-ca
    :weather {:condition :storm :wind-speed 45}
    :expected {:wifi-connection :cellular
               :starlink-quality :low}}})
```

---

## 📁 **Template/Personal Split**

### **grain6pbc/grainmock (Template)**

```
grain6pbc/grainmock/
├── src/grainmock/
│   ├── core.clj              ← Core mocking API
│   ├── time_travel.clj       ← Time manipulation
│   ├── astromitra_mock.clj   ← astromitra.com mock
│   └── weather_mock.clj      ← Weather API mock
├── personal/                 ← .gitignored
│   └── .gitkeep
├── fixtures/
│   ├── common_times.edn      ← Sunrise, noon, sunset, midnight
│   └── common_locations.edn  ← Major cities
└── README.md
```

### **kae3g/grainkae3gmock (Personal)**

```
kae3g/grainkae3gmock/
├── src/grainkae3gmock/
│   ├── caspar.clj            ← Caspar, CA specific
│   └── starlink.clj          ← Starlink patterns
├── personal/                 ← NOT gitignored (versioned!)
│   ├── caspar_ca.edn         ← Coastal weather patterns
│   ├── starlink_logs.edn     ← Real Starlink connection data
│   └── storm_history.edn     ← Historical storm impact
└── README.md
```

---

## 🚀 **Quick Start**

### **Install grainmock**

```bash
cd /home/xy/kae3g/grainkae3g/grainstore/grain6pbc/grainmock
bb install
```

### **Run Tests**

```bash
# Test graintime at different times
bb test:graintime-morning
bb test:graintime-evening
bb test:graintime-all-day

# Test with different locations
bb test:locations

# Test grain6 scheduling
bb test:grain6-triggers
```

---

## 🎓 **Educational Value**

### **What Students Learn**

1. **Mocking**: How to mock external dependencies
2. **Time Travel**: Testing time-dependent code
3. **Fixtures**: Creating reusable test data
4. **Template/Personal**: Separating shared vs custom test data
5. **Integration Testing**: Testing multiple components together

---

## 📊 **Success Metrics**

- [ ] graintime tests pass at all times of day
- [ ] Ascendant correct for morning/evening/noon/midnight
- [ ] Solar house matches cardinal house positions
- [ ] Moon nakshatra matches astromitra.com data
- [ ] grain6 triggers fire at correct times
- [ ] grainwifi switches based on weather mocks

---

## 🌾 **Integration with Existing Tools**

### **graintime**
```clojure
;; In graintime tests
(require '[grainmock.astromitra :as mock])

(deftest ascendant-evening-test
  (mock/with-mock-api
    (mock/with-time "2025-10-23T18:45:00"
      (let [gt (graintime/generate-graintime "test")]
        (is (re-find #"asc-(sco|sag)" gt))))))
```

### **grain6**
```clojure
;; Test grain6 scheduling
(grainmock/with-time "2025-10-23T07:25:00"  ; Sunrise
  (is (grain6/should-start? :graindisplay-service)))
```

### **grainwifi**
```clojure
;; Test weather-based switching
(grainmock/with-weather {:storm true}
  (is (= :cellular (grainwifi/preferred-connection))))
```

---

## 🔗 **Related Modules**

- **graintime**: Uses grainmock for testing
- **grain6**: Uses grainmock for schedule testing
- **grainwifi**: Uses grainmock for weather/network testing
- **graintest**: General testing utilities (if created)

---

**Status**: 🌱 Design phase - ready for implementation  
**Priority**: High (needed for graintime accuracy fixes)  
**Complexity**: Medium (Clojure macros, dynamic binding)  

🌾 **now == next + 1** (but make it mockable and testable, chief!) 🧪

