# 🌾 grain6 ICP Canister Design

**Decentralized Time-Aware Process Supervision on Internet Computer**

---

## 🎯 **Vision**

Deploy grain6 as an **ICP canister** that provides:
- Global, decentralized timer service
- Behn-style priority queue on-chain
- Graintime-based astronomical scheduling
- Cross-canister wake notifications
- Persistent timer state (survives upgrades)

**The Big Idea:** Behn for the Internet Computer, powered by graintime

---

## 🏗️ **Architecture**

```
┌─────────────────────────────────────────────┐
│  grain6 ICP Canister (Motoko/Clotoko)       │
│                                             │
│  ┌──────────────────────────────────────┐  │
│  │  Behn-Inspired Timer Queue           │  │
│  │  - Priority queue (stable storage)   │  │
│  │  - Graintime integration             │  │
│  │  - Astronomical calculations         │  │
│  └──────────────────────────────────────┘  │
│                                             │
│  ┌──────────────────────────────────────┐  │
│  │  Canister Heartbeat                  │  │
│  │  - Runs every N seconds              │  │
│  │  - Processes ready timers            │  │
│  │  - Sends inter-canister calls        │  │
│  └──────────────────────────────────────┘  │
│                                             │
│  ┌──────────────────────────────────────┐  │
│  │  Public API                          │  │
│  │  - setTimer(time, callback)          │  │
│  │  - cancelTimer(id)                   │  │
│  │  - waitAstronomical(event)           │  │
│  │  - nextWakeTime()                    │  │
│  └──────────────────────────────────────┘  │
└─────────────────────────────────────────────┘
           │                    │
           │                    │
           ↓                    ↓
    ┌──────────┐         ┌──────────┐
    │ Client   │         │ Other    │
    │ Canister │         │ Canisters│
    └──────────┘         └──────────┘
```

---

## 📝 **Motoko Interface (via Clotoko)**

### **Timer Queue Type**

```motoko
// grain6.mo (generated from Clojure via Clotoko)

type TimerId = Nat;
type Timestamp = Nat64;  // Unix ms
type GrainTime = Text;

type AstronomicalEvent = {
  #sunrise;
  #sunset;
  #solarHouse : Nat;  // 1-12
  #nakshatra : Text;
  #moonPhase : Text;
};

type TimerAction = {
  #interCanisterCall : {
    canister : Principal;
    method : Text;
    args : Blob;
  };
  #httpOutcall : {
    url : Text;
    method : Text;
  };
  #notification : {
    message : Text;
  };
};

type Timer = {
  id : TimerId;
  time : Timestamp;
  action : TimerAction;
  graintime : ?GrainTime;
  astronomical : ?AstronomicalEvent;
  repeat : ?{
    interval : Nat64;
    count : ?Nat;  // None = infinite
  };
};

type TimerQueue = [Timer];  // Sorted by time
```

### **Public API**

```motoko
actor grain6 {
  // Behn %wait equivalent
  public shared func setTimer(time : Timestamp, action : TimerAction) : async TimerId;
  
  // Behn %rest equivalent  
  public shared func cancelTimer(id : TimerId) : async Bool;
  
  // grain6 enhancement - astronomical events
  public shared func waitAstronomical(
    event : AstronomicalEvent,
    action : TimerAction
  ) : async TimerId;
  
  // grain6 enhancement - repeating timers
  public shared func setRepeating(
    interval : Nat64,
    action : TimerAction,
    count : ?Nat
  ) : async TimerId;
  
  // Behn %doze equivalent
  public query func nextWakeTime() : async ?Timestamp;
  
  // Query timer state
  public query func listTimers() : async [Timer];
  
  // Behn %born equivalent
  public shared func recalculateTimers() : async ();
  
  // Stats (Behn %wegh equivalent)
  public query func getStatistics() : async {
    totalTimers : Nat;
    nextWake : ?Timestamp;
    queueSize : Nat;
  };
}
```

---

## 🔄 **Canister Heartbeat Integration**

ICP canisters can have a **heartbeat** function that runs periodically:

```motoko
actor grain6 {
  // State
  stable var timers : TimerQueue = [];
  stable var nextTimerId : TimerId = 0;
  
  // Heartbeat - runs every ~1 second
  system func heartbeat() : async () {
    let now = Time.now();  // IC time
    
    // Process ready timers (Behn %wake)
    let (ready, pending) = Array.partition<Timer>(
      timers,
      func(t) { t.time <= now }
    );
    
    // Execute ready timers
    for (timer in ready.vals()) {
      await executeTimer(timer);
    };
    
    // Update queue
    timers := pending;
  };
  
  func executeTimer(timer : Timer) : async () {
    switch (timer.action) {
      case (#interCanisterCall(call)) {
        // Make inter-canister call
        let canister = actor(Principal.toText(call.canister));
        await canister.someMethod();
      };
      case (#notification(n)) {
        // Send notification
        Debug.print(n.message);
      };
      // ... other actions
    };
    
    // Handle repeating timers
    switch (timer.repeat) {
      case (?repeat) {
        if (Option.isSome(repeat.count) and repeat.count == ?0) {
          // Repeat exhausted
        } else {
          // Reschedule
          let newTimer = {
            timer with
            time = timer.time + repeat.interval;
            repeat = switch (repeat.count) {
              case (?c) { ?{ repeat with count = ?(c - 1) } };
              case null { ?repeat };
            };
          };
          addTimer(newTimer);
        };
      };
      case null {};
    };
  };
}
```

---

## 🌾 **Clotoko Translation**

### **Clojure grain6 Code**

```clojure
(ns grain6.canister
  "ICP canister implementation of grain6")

(defn set-timer
  "Set a timer (becomes Motoko setTimer)"
  [time action]
  {:timer-id (next-id)
   :time time
   :action action})

(defn process-timers
  "Process ready timers (becomes heartbeat)"
  []
  (let [now (current-time)
        ready (filter #(<= (:time %) now) @timers)]
    (doseq [timer ready]
      (execute-action (:action timer)))))
```

### **Clotoko Transpilation**

```bash
# Convert Clojure to Motoko
clotoko transpile grain6/canister.clj --output grain6.mo
```

**Generated Motoko:**
```motoko
import Time "mo:base/Time";
import Array "mo:base/Array";

actor grain6Canister {
  stable var timers : [Timer] = [];
  
  public shared func setTimer(time : Nat64, action : TimerAction) : async Nat {
    let id = nextTimerId;
    nextTimerId += 1;
    let timer = {
      id = id;
      time = time;
      action = action;
    };
    timers := Array.append(timers, [timer]);
    id
  };
  
  system func heartbeat() : async () {
    let now = Time.now();
    // Process ready timers (translated from Clojure filter/map)
    // ...
  };
}
```

---

## 🔗 **Integration Points**

### **1. grain6 ↔ GrainDisplay Canister**

```motoko
// Deploy GrainDisplay as separate canister
actor GrainDisplay {
  public shared func enableWarm() : async () {
    // Enable warm display for this user
  };
}

// grain6 schedules it
let displayCanister = actor("rrkah-...") : GrainDisplay;

await grain6.waitAstronomical(
  #sunset,
  #interCanisterCall({
    canister = Principal.fromActor(displayCanister);
    method = "enableWarm";
    args = encode();
  })
);
```

### **2. grain6 ↔ GrainCourse Canister**

```motoko
// Schedule course builds
await grain6.setRepeating(
  86400000,  // Daily (24 hours in ms)
  #interCanisterCall({
    canister = grainCourseCanister;
    method = "buildCourse";
    args = encode("kae3g/grain-fundamentals");
  }),
  ?365  // Repeat for one year
);
```

### **3. grain6 ↔ GrainSpace**

```motoko
// Coordinate multi-service startup
await grain6.waitAstronomical(
  #solarHouse(10),  // Career/work house
  #interCanisterCall({
    canister = grainSpaceCanister;
    method = "startDevEnvironment";
    args = encode();
  })
);
```

---

## 💾 **Stable Storage**

ICP canisters can persist state across upgrades:

```motoko
actor grain6 {
  // Stable variables survive upgrades
  stable var timers : TimerQueue = [];
  stable var nextTimerId : TimerId = 0;
  stable var graintimeCache : [(Text, Timestamp)] = [];
  
  // Pre-upgrade hook
  system func preupgrade() {
    // State is automatically saved
    Debug.print("Saving " # Nat.toText(Array.size(timers)) # " timers");
  };
  
  // Post-upgrade hook
  system func postupgrade() {
    // State is automatically restored
    Debug.print("Restored " # Nat.toText(Array.size(timers)) # " timers");
    // Recalculate astronomical events (like Behn %born)
    recalculateAstronomicalTimers();
  };
}
```

**grain6 Benefit:** Timers survive canister upgrades!

---

## 🌐 **Decentralized Scheduling**

### **The Power of On-Chain Timers**

**Traditional Systems:**
- Cron on one server (single point of failure)
- Lost if server goes down
- Can't coordinate across distributed systems

**grain6 on ICP:**
- Timers stored on-chain (replicated across subnets)
- Survive individual node failures
- Can trigger actions on any canister
- Global coordination primitive

**Use Cases:**

1. **Coordinated Releases**
```motoko
// All canisters update at exact graintime
await grain6.waitAstronomical(
  #nakshatra("rohini"),  // Auspicious time
  #broadcastToAll("upgrade-ready")
);
```

2. **Global Event Scheduling**
```motoko
// Schedule event for all timezones
await grain6.setTimer(
  sunset-in-LA,
  #notify-pacific-users
);
await grain6.setTimer(
  sunset-in-NYC,
  #notify-eastern-users
);
```

3. **Astronomical Consensus**
```motoko
// Coordinate based on shared astronomical state
await grain6.waitAstronomical(
  #solarHouse(1),  // Dawn - new beginnings
  #startDailyConsensus
);
```

---

## 🔐 **Security Model**

### **Access Control**

```motoko
actor grain6 {
  // Only authorized canisters can set timers
  let authorizedCallers = TrieSet.fromArray<Principal>([
    grainDisplayPrincipal,
    grainCoursePrincipal,
    grainSpacePrincipal
  ]);
  
  public shared({ caller }) func setTimer(...) : async TimerId {
    if (not TrieSet.mem(authorizedCallers, caller)) {
      throw Error.reject("Unauthorized");
    };
    // ... set timer
  };
}
```

### **Rate Limiting**

```motoko
// Prevent timer spam
stable var timersByPrincipal : HashMap<Principal, Nat> = HashMap.empty();

public shared({ caller }) func setTimer(...) : async TimerId {
  let count = Option.get(HashMap.get(timersByPrincipal, caller), 0);
  if (count > 100) {
    throw Error.reject("Too many timers");
  };
  // ... set timer
  HashMap.put(timersByPrincipal, caller, count + 1);
}
```

---

## 🌍 **Multi-Chain Integration**

### **grain6 as Timing Oracle**

**Concept:** grain6 canister provides timing services to other chains

```
grain6 (ICP Canister)
    │
    ├──► Solana (via Chain Fusion)
    │    └── Trigger Solana transactions at sunset
    │
    ├──► Ethereum (via HTTPS Outcalls)
    │    └── Call smart contracts at specific times
    │
    └──► Hedera (via ICP bridge)
         └── Coordinate hashgraph consensus times
```

### **Example: Cross-Chain Sunset Trigger**

```motoko
actor grain6 {
  public shared func waitForSunsetThenTriggerSolana(
    location : { lat : Float; lon : Float }
  ) : async () {
    let sunsetTime = await calculateSunset(location);
    
    await setTimer(
      sunsetTime,
      #interCanisterCall({
        canister = solanaChainFusionCanister;
        method = "triggerSolanaTransaction";
        args = encode({
          program = "graindisplay_warm";
          instruction = "enable";
        });
      })
    );
  };
}
```

---

## 📊 **Clotoko Translation Path**

### **Development Flow**

```
1. Write in Clojure
   └── grain6/src/grain6/canister.clj
       └── Familiar Clojure syntax
       └── Rich standard library
       └── Easy testing

2. Transpile to Motoko
   └── clotoko transpile canister.clj
       └── Type-safe Motoko code
       └── ICP-compatible
       └── Optimized for canisters

3. Deploy to ICP
   └── dfx deploy grain6
       └── On-chain timer service
       └── Globally accessible
       └── Decentralized reliability
```

### **Clojure Source**

```clojure
(ns grain6.canister
  "grain6 ICP canister implementation"
  (:require [clotoko.core :as ct]))

(ct/defactor grain6
  "Time-aware supervision canister"
  
  ;; Stable state
  (ct/defstable timers [])
  (ct/defstable next-id 0)
  
  ;; Public API
  (ct/defquery get-next-wake []
    (when-let [timer (first timers)]
      (:time timer)))
  
  (ct/defupdate set-timer [time action]
    (let [id next-id
          timer {:id id :time time :action action}]
      (set! next-id (inc next-id))
      (set! timers (conj timers timer))
      id))
  
  (ct/defheartbeat process-timers []
    (let [now (current-time)
          [ready pending] (split-with #(<= (:time %) now) timers)]
      (doseq [timer ready]
        (execute-timer timer))
      (set! timers pending))))
```

### **Generated Motoko**

```motoko
import Array "mo:base/Array";
import Time "mo:base/Time";
import Nat "mo:base/Nat";

actor grain6 {
  // Stable state (Clotoko translation)
  stable var timers : [Timer] = [];
  stable var nextId : Nat = 0;
  
  // Query (Clotoko ct/defquery)
  public query func getNextWake() : async ?Nat64 {
    switch (Array.find<Timer>(timers, func(t) { true })) {
      case (?timer) { ?timer.time };
      case null { null };
    }
  };
  
  // Update (Clotoko ct/defupdate)
  public shared func setTimer(time : Nat64, action : TimerAction) : async Nat {
    let id = nextId;
    let timer = {
      id = id;
      time = time;
      action = action;
    };
    nextId += 1;
    timers := Array.append(timers, [timer]);
    id
  };
  
  // Heartbeat (Clotoko ct/defheartbeat)
  system func heartbeat() : async () {
    let now = Nat64.fromNat(Int.abs(Time.now()));
    let (ready, pending) = Array.partition<Timer>(
      timers,
      func(t) { t.time <= now }
    );
    for (timer in ready.vals()) {
      await executeTimer(timer);
    };
    timers := pending;
  };
}
```

---

## 🎯 **Deployment Strategy**

### **Phase 1: Local Development**

```bash
# Develop in Clojure
cd grain6
clojure -M:dev

# Test locally
bb test

# Validate Clotoko syntax
clotoko check src/grain6/canister.clj
```

### **Phase 2: Transpilation**

```bash
# Transpile to Motoko
clotoko transpile src/grain6/canister.clj --output canisters/grain6/src/main.mo

# Verify generated code
cat canisters/grain6/src/main.mo

# Test Motoko compilation
dfx build grain6
```

### **Phase 3: ICP Deployment**

```bash
# Deploy to local replica
dfx deploy grain6 --network local

# Test canister
dfx canister call grain6 setTimer '(1698105600000, variant { notification = record { message = "Test" } })'

# Deploy to mainnet
dfx deploy grain6 --network ic --with-cycles 1000000000000
```

---

## 💡 **Unique grain6 Features on ICP**

### **1. Cycles-Based Billing**

```motoko
// Charge cycles for timer operations
public shared func setTimer(...) : async TimerId {
  let cost = calculateCost(action);
  if (Cycles.available() < cost) {
    throw Error.reject("Insufficient cycles");
  };
  Cycles.accept(cost);
  // ... set timer
}
```

### **2. Graintime Oracles**

```motoko
// Provide graintime data to other canisters
public query func currentGraintime() : async GrainTime {
  // Calculate current graintime
  let now = Time.now();
  let nakshatra = calculateNakshatra(now);
  let solarHouse = calculateSolarHouse(now);
  formatGraintime(now, nakshatra, solarHouse)
}
```

### **3. Astronomical Event Subscriptions**

```motoko
// Subscribe to events
public shared func onSunset(callback : shared () -> async ()) : async () {
  let sunsetTime = await calculateSunset();
  await setTimer(
    sunsetTime,
    #interCanisterCall({
      canister = caller;
      method = "onSunset";
      args = encode();
    })
  );
}
```

### **4. Cross-Canister Coordination**

```motoko
// Coordinate multiple canisters
public shared func coordinateServices(
  services : [Principal],
  event : AstronomicalEvent
) : async () {
  let time = await calculateEventTime(event);
  for (service in services.vals()) {
    await setTimer(
      time,
      #interCanisterCall({
        canister = service;
        method = "start";
        args = encode();
      })
    );
  };
}
```

---

## 📈 **Scalability**

### **Behn Limitations (Single-Node)**

- Timers limited by one Urbit's memory
- No cross-ship coordination
- Limited by single-core processing

### **grain6 on ICP (Subnet-Scale)**

- Timers replicated across subnet
- Can coordinate across canisters
- Parallel processing via multiple canisters

**Example: Sharded Timers**

```motoko
// grain6-shard-1 handles timers for solar houses 1-6
// grain6-shard-2 handles timers for solar houses 7-12

actor grain6Shard1 {
  // Only handles houses 1-6
  public shared func setTimerForHouse(house : Nat, ...) : async TimerId {
    assert(house >= 1 and house <= 6);
    // ... set timer
  };
}
```

---

## 🌟 **Why This Matters**

### **Behn's Elegance + ICP's Power + graintime's Wisdom**

**Behn (Urbit):**
- ✅ Simple priority queue
- ✅ Crash isolation
- ✅ Clear API
- ❌ Single-node only
- ❌ Fixed timestamps only

**grain6 on ICP:**
- ✅ Behn's simplicity
- ✅ Decentralized reliability
- ✅ Astronomical scheduling
- ✅ Cross-canister coordination
- ✅ Survives upgrades
- ✅ Cycles-based economics

**The Synthesis:**
```
Behn's elegance
  + ICP's decentralization
  + graintime's astronomy
  + s6's supervision
  = grain6 (grainsix)
```

---

## 📚 **See Also**

- [Urbit Behn Documentation](../docs/urbit-docs/arvo/behn/)
- [Clotoko](../clotoko/README.md) - Clojure to Motoko transpiler
- [clojure-icp](../clojure-icp/README.md) - ICP integration
- [graintime](../graintime/README.md) - Astronomical calculations

---

**Version:** 0.1.0 (Design Phase)  
**Date:** October 23, 2025  
**Author:** kae3g (Grain PBC)

🌾 **Behn's timer queue + ICP's decentralization + graintime's stars!**
