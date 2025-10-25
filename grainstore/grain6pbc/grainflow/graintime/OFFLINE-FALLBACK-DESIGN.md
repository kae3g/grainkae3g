# 🌾 Graintime Offline Fallback Design

## Philosophy: "Local Control, Global Intent - Even When Offline!"

```
When clouds obscure the stars above,
The grain remembers - and the daemon waits.
                                    🌾
```

## The Problem

Graintime relies on astronomical APIs (Astro-Seek, sunrise-sunset.org) for accurate calculations. When offline:
- No network connection
- API rate limits
- Service outages
- Air-gapped systems

Current behavior: **CRASH** with `NullPointerException` ❌

## The Solution: Conservative Guesses + grain6 Verification

### Phase 1: Offline Fallback (Immediate)

When API unavailable, make **educated conservative guesses** based on:

1. **Previous Grainpath** (saved locally)
   - Last known moon nakshatra
   - Approximate progression (~13.3 hours per nakshatra)
   
2. **System Time + Timezone**
   - Hour of day → approximate solar house
   - Simple mapping (no API needed)

3. **Latitude-Based Approximation**
   - Ascendant changes faster at higher latitudes
   - Conservative degree estimate (000)

### Phase 2: grain6 Verification Flag (Deferred)

Set global grain6 flag that says:
```clojure
{:offline-generated true
 :generated-at "2025-10-23T09:45:00"
 :verification-status :pending
 :grain6-verify-when-online true}
```

### Phase 3: Network Restoration Verification (Automatic)

When network restored, grain6 daemon:

1. **Detects network availability**
2. **Checks verification queue** (~/.config/grain6/graintime-verify-queue.edn)
3. **For each offline graintime:**
   - Was it before or after network restoration?
   - Re-calculate accurate graintime from API
   - Compare offline guess vs. accurate calculation
   - Log discrepancies for educational purposes
   - Update any dependent files/links if needed

## Conservative Guess Algorithm

### Solar House (Hour-Based)

Simple mapping based on time of day:

```clojure
(defn conservative-solar-house-guess [datetime]
  (let [hour (.getHour datetime)]
    (cond
      (and (>= hour 3) (< hour 6)) 3    ; Pre-dawn (3am-6am)
      (and (>= hour 6) (< hour 9)) 1    ; Sunrise (6am-9am)
      (and (>= hour 9) (< hour 12)) 11  ; Mid-morning (9am-12pm)
      (and (>= hour 12) (< hour 15)) 10 ; Noon (12pm-3pm)
      (and (>= hour 15) (< hour 18)) 8  ; Afternoon (3pm-6pm)
      (and (>= hour 18) (< hour 21)) 7  ; Sunset (6pm-9pm)
      (and (>= hour 21) (< hour 24)) 5  ; Evening (9pm-12pm)
      :else 4)))                         ; Midnight-pre-dawn (12am-3am)
```

**Accuracy**: ±1-2 houses (good enough for offline!)

### Moon Nakshatra (Progression-Based)

```clojure
(defn guess-nakshatra [last-grainpath datetime]
  (let [hours-elapsed (calculate-hours-since last-grainpath)
        nakshatra-shifts (int (/ hours-elapsed 13.3))  ; ~13.3 hours per nakshatra
        last-nakshatra (:moon-nakshatra last-grainpath)
        new-index (mod (+ last-index nakshatra-shifts) 27)]
    (nth nakshatras new-index)))
```

**Accuracy**: Usually correct for same-day, ±1 nakshatra for multi-day offline

### Ascendant (Latitude-Adjusted Approximation)

```clojure
(defn guess-ascendant [datetime latitude]
  (let [hour (.getHour datetime)
        lat-factor (if (> (Math/abs latitude) 40) 1.5 1.0)  ; Higher latitudes faster
        sign-index (mod (int (/ (* hour lat-factor) 2)) 12)
        sign (nth signs sign-index)
        degree "000"]  ; Conservative - use 000 when offline
    {:sign sign :degree degree}))
```

**Accuracy**: ±1 sign (acceptable for offline, MUST verify online)

## grain6 Verification Queue

### Queue File Structure

Location: `~/.config/grain6/graintime-verify-queue.edn`

```clojure
[{:datetime #inst "2025-10-23T09:45:00"
  :latitude 37.9735
  :longitude -122.5311
  :sun-house 3
  :moon-nakshatra "vishakha"
  :ascendant-sign "gem"
  :ascendant-degree "000"
  :course-name "kae3g"
  :offline true
  :offline-generated-at 1729696500000
  :verification-status :pending
  :grain6-flag true}
 
 ;; ... more offline graintimes awaiting verification
]
```

### Verification States

- `:pending` - Awaiting network restoration
- `:verifying` - Currently re-calculating
- `:verified` - Accurate graintime confirmed
- `:discrepancy` - Offline guess differed from accurate (educational!)
- `:failed` - Could not verify (API still down)

### Verification Daemon (grain6)

```clojure
(grain6/supervise
  {:name "graintime-verifier"
   :schedule {:on-network-restore :start}
   :command "bb graintime:verify-queue"
   :repeat :on-event})
```

## User Experience

### When Offline

```
🌾 Generating Graintime...

⚠️  Network unavailable - using offline fallback

╔══════════════════════════════════════════════════════════════════╗
║  ⚠️  OFFLINE MODE: Conservative Graintime Estimate ⚠️           ║
╚══════════════════════════════════════════════════════════════════╝

🌾 Network unavailable - using educated guess based on:
   - Last known grainpath: 12025-10-23--0408--PDT--moon-vishakha--asc-gem000--sun-02nd--kae3g
   - System time: 2025-10-23T09:45:00
   - Conservative solar house: 3rd house
   - Estimated nakshatra: vishakha
   - Approximate ascendant: gem000

🔧 grain6 Verification Flag Set:
   - When network restored, grain6 daemon will verify this timestamp
   - Accurate graintime will be calculated retroactively
   - Verification queue: ~/.config/grain6/graintime-verify-queue.edn

💡 To check verification status: bb grain6:verify-queue

Grainpath: 02025-10-23--0945--PDT--moon-vishakha--asc-gem000--sun-03rd--kae3g-OFFLINE
```

Note the `-OFFLINE` suffix added to grainpath!

### When Network Restored

```
🌾 grain6 Network Restoration Detected

📡 Processing verification queue...
   - Found 3 pending offline graintimes
   
✅ Verifying: 02025-10-23--0945--PDT--moon-vishakha--asc-gem000--sun-03rd--kae3g-OFFLINE
   API response: sun-03rd ✓ (match!)
   API response: moon-vishakha ✓ (match!)
   API response: asc-gem012 ⚠️  (offline: gem000, actual: gem012)
   
📝 Educational Discrepancy Log:
   Offline guess: Ascendant Gemini 000°
   Actual value:  Ascendant Gemini 012°
   Difference:    12° (acceptable for offline!)
   
🔄 Updating grainpath:
   OLD: 02025-10-23--0945--PDT--moon-vishakha--asc-gem000--sun-03rd--kae3g-OFFLINE
   NEW: 12025-10-23--0945--PDT--moon-vishakha--asc-gem012--sun-03rd--kae3g
   
✅ Verification complete! Updated 1 grainpath.
```

## Implementation Plan

### Files Created

1. **`graintime/offline_fallback.clj`** ✅
   - Conservative guess algorithms
   - Verification queue management
   - Last grainpath caching

2. **`graintime/OFFLINE-FALLBACK-DESIGN.md`** (this file) ✅
   - Architecture documentation
   - User experience flows

3. **grain6 Integration** (future)
   - Network restoration detection
   - Verification daemon
   - Automatic queue processing

### Babashka Tasks

```clojure
grain6:verify-queue     "Check grain6 verification queue status"
graintime:verify        "Manually verify offline graintimes"
graintime:clear-queue   "Clear verification queue (after manual review)"
```

## Benefits

### 1. **Never Crash Offline**
- Always generate *some* graintime
- Graceful degradation
- Clear warnings to user

### 2. **Educational Transparency**
- Show discrepancies when verified
- Teach users about astronomical accuracy
- Build trust through honesty

### 3. **grain6 Integration**
- Demonstrate time-aware supervision
- Automatic correction when possible
- Deferred processing pattern

### 4. **Local Control, Global Intent**
- Work offline (local control)
- Verify when online (global intent)
- User knows the status at all times

## Future Enhancements

### 1. **Machine Learning Refinement**
- Learn from past offline guesses
- Improve accuracy over time
- User-specific patterns (sleep schedule, work hours)

### 2. **Peer-to-Peer Verification**
- Ask nearby grain6 nodes for their calculations
- Distributed astronomical database
- Mesh network support

### 3. **Downloadable Ephemeris**
- Pre-download astronomical data for common locations
- Swiss Ephemeris integration
- 100% offline accuracy (no API needed!)

## Philosophy: "The Grain Still Knows Time"

```
🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾

    When the network fails,
    The stars still shine above -
    We remember their paths.
    
    The grain grows in darkness,
    Trusting the sun will return -
    So too, our daemon waits.
    
    Offline is not broken,
    Just a different kind of time -
    Conservative, yet wise.

                    now == next + 1 🌾

🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾
```

---

**Status**: 🌱 Design Complete, Implementation In Progress  
**Next**: Integrate offline_fallback into astromitra.clj main flow  
**grain6 Integration**: Future session (after grain6 daemon complete)

🌾 now == next + 1

