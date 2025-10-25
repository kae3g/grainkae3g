# Nock Specification: runit Crash-Only Design

**Component**: runit (Gerrit Pape's supervision suite)  
**Version**: 2.1.2 (reference implementation)  
**Size**: ~30KB (even smaller than s6!)  
**Purpose**: Crash-only init and supervision  
**Status**: Draft Specification v0.1

---

## Overview

runit embodies the **crash-only design** philosophy: systems should be designed to crash and restart cleanly, rather than attempting complex error handling. This Nock specification captures the mathematical essence of crash-only supervision.

---

## Core Philosophy: Crash-Only Design

### The Central Theorem

**Theorem**: A system designed to crash cleanly is more reliable than one designed never to crash.

**Proof Sketch**:
1. Complex error handling introduces bugs
2. Recovery paths are rarely tested
3. Crashes force a return to known-good state
4. Simple restart is easier to verify

**Nock Representation**: All state transitions reduce to `down → up`.

---

## Core Concepts (Nouns)

### Service State (Simpler than s6)

runit has only TWO states (vs s6's four):

```nock
[service-name state pid]
```

Where:
- `service-name`: Atom (string as noun)
- `state`: Atom (0=down, 1=up) -- THAT'S IT!
- `pid`: Atom (process ID, 0 if not running)

**Example**:
```nock
[/etc/sv/sshd 1 5678]
```

### Why Only Two States?

**Crash-only design means**:
- No "finishing" state (just kill and restart)
- No "ready" state (either running or not)
- No graceful shutdown (crash is the norm)

This makes verification trivial!

---

## State Transitions (Pure Nock)

### 1. Start Service

**Intent**: Transition from `down` (0) to `up` (1).

**Nock Formula** (Rule 6 - if-then-else):
```nock
?[6 [5 [0 2] [1 0]]  ; If state == 0 (down)
   [4 [0 2]]          ; Inc state (0→1, down→up)
   [0 0]]             ; Else no-op (already up)
```

**Simpler than s6**: Only one state change (not up→finishing→down).

### 2. Stop Service (Actually: Kill Service)

**Intent**: Kill process, return to `down` (0).

**Nock Formula**:
```nock
?[6 [5 [0 2] [1 1]]  ; If state == 1 (up)
   [3 [0 2]]          ; Dec state (1→0, up→down)
   [0 0]]             ; Else no-op (already down)
```

**No "finishing" phase**: SIGKILL is sent immediately, no graceful shutdown.

### 3. Restart Service

**Intent**: Kill and restart atomically.

**Nock Formula** (Composition):
```nock
[7 [stop-formula] [start-formula]]
```

**Same as s6**, but simpler because stop is immediate (no finishing phase).

### 4. Check Service Status

**Nock Formula** (Rule 0 - tree addressing):
```nock
[0 2]  ; Get state field (axis 2 in service noun)
```

---

## The runsv Supervision Loop

### Core Invariant (Even Simpler)

**Invariant**: A process that exits is immediately restarted.

**Nock Formula** (One-line supervision):
```nock
?[6 [5 [0 2] [1 0]]  ; If state == down
   [start-formula]    ; Start it NOW
   [0 0]]             ; Else continue
```

This is the **entire** runsv algorithm:
1. Check if process is running
2. If not, start it
3. Repeat forever

**No delays, no retries, no backoff** -- just instant restart!

---

## Crash-Only Semantics (Formalized)

### Property 1: Immediate Kill

**Property**: Stopping a service sends SIGKILL (not SIGTERM).

**Nock Representation**:
```nock
[kill-signal 9]  ; SIGKILL = 9 (force kill)
```

**Why**: No graceful shutdown means:
- No "finish" scripts to debug
- No timeout handling
- No zombie processes
- Clean state transitions

### Property 2: Immediate Restart

**Property**: A crashed service restarts instantly (no delay).

**Nock Formula**:
```nock
; On crash detection (PID changed to 0):
[start-formula]  ; Immediate, no delay
```

**Proof Obligation**: The supervision loop detects crashes in <1 second.

### Property 3: No State Persistence

**Property**: Services store NO state between restarts.

**Nock Representation**:
```nock
[service-state nil]  ; No persistent state
```

**Why**: State persistence is a common source of bugs. Crash-only design forces services to be stateless or store state externally (files, databases).

---

## The Three-Script Structure

### 1. `run` Script

**Purpose**: Start the service and run it forever.

**Requirements**:
- Must run in foreground (no daemonizing!)
- Must exit if it can't start
- Must handle signals (SIGTERM/SIGKILL)

**Nock Specification**:
```nock
[run-script
 [exec [service-binary]]  ; exec replaces shell with service
 [exit-on-error]]         ; If exec fails, exit immediately
```

### 2. `finish` Script (Optional)

**Purpose**: Run after service dies (for cleanup).

**Requirements**:
- Must complete quickly (<5 seconds)
- Must not block restart
- Must handle being killed mid-execution

**Nock Specification**:
```nock
[finish-script
 [cleanup-action]         ; Brief cleanup
 [timeout 5000]]          ; Max 5 seconds
```

**Note**: Unlike s6, runit's `finish` doesn't affect restart timing!

### 3. `check` Script (Optional)

**Purpose**: Health check for service readiness.

**Requirements**:
- Exit 0 if healthy, non-zero if not
- Must complete quickly (<1 second)
- Called periodically by `sv check`

**Nock Specification**:
```nock
[check-script
 [health-test]            ; Test service health
 [exit-code]]             ; 0=healthy, else unhealthy
```

---

## The sv Command-Line Interface

### Commands (Simpler than s6-svc)

| Command | Nock Formula | Effect |
|---------|--------------|--------|
| `sv up` | `start-formula` | Start (or restart if up) |
| `sv down` | `stop-formula` | Kill (SIGKILL) |
| `sv status` | `status-formula` | Check if running |
| `sv check` | `check-formula` | Run health check |
| `sv restart` | `restart-formula` | Kill + start |

**All atomic, all immediate!**

---

## Comparison with s6

| Feature | runit | s6 |
|---------|-------|-----|
| **States** | 2 (down, up) | 4 (down, up, finishing, ready) |
| **Stop Signal** | SIGKILL (force) | SIGTERM (graceful) |
| **Restart Time** | Instant | After finish script |
| **State Persistence** | None | Service directories |
| **Size** | 30KB | 200KB |
| **Verification Complexity** | Trivial | Moderate |

**Tradeoff**: runit is simpler but less feature-rich.

---

## Equivalence Claims

### Claim 1: Two-State Machine

**Claim**: runit's state machine has only two states: down (0) and up (1).

**Evidence**:
- No "finishing" state (kill is immediate)
- No "ready" state (either running or not)
- State transitions: 0↔1

**Verification**: Property-based testing with test.check.

### Claim 2: Immediate Restart

**Claim**: A service that crashes is restarted within 1 second.

**Evidence**:
- runsv checks PID every loop iteration
- No artificial delays
- No backoff logic

**Verification**: Time-based testing (measure restart latency).

### Claim 3: Stateless Services

**Claim**: Services cannot rely on in-memory state across restarts.

**Evidence**:
- SIGKILL doesn't allow cleanup
- No state saved to disk by runsv
- Service must handle arbitrary kill

**Verification**: Chaos engineering (kill -9 at random times).

---

## Formal Verification (TLA+ Sketch)

```tla
VARIABLES service_state, service_pid

Init == 
  /\ service_state = "down"
  /\ service_pid = 0

Start ==
  /\ service_state = "down"
  /\ service_state' = "up"
  /\ service_pid' \in 1..65535

Stop ==
  /\ service_state = "up"
  /\ service_state' = "down"
  /\ service_pid' = 0

Crash ==
  /\ service_state = "up"
  /\ service_state' = "down"  ; Detected as down
  /\ service_pid' = 0

Supervise ==
  /\ service_state = "down"
  /\ Start  ; Immediate restart

TypeInvariant ==
  /\ service_state \in {"down", "up"}
  /\ service_pid \in 0..65535

RestartInvariant ==
  /\ service_state = "down" => <>Start  ; Eventually starts
```

---

## Jet Opportunities

### Hot Path: State Check

**Current**: Read PID from `/proc/<pid>/stat`  
**Jet**: Cache PID, use `kill(pid, 0)` to check if alive

**Optimization**: Reduces filesystem I/O.

### Hot Path: Instant Restart

**Current**: Fork + exec for every restart  
**Jet**: Pre-forked worker pool (optional)

**Tradeoff**: Adds complexity (against crash-only philosophy).

---

## Implementation Checklist

- [ ] **Clojure Spec**: Define two-state machine
- [ ] **test.check**: Generate crash scenarios
- [ ] **Equivalence tests**: Clojure ↔ Nock ↔ C
- [ ] **TLA+ spec**: Model two-state machine
- [ ] **Coq proof**: Prove restart within 1 second
- [ ] **Chaos tests**: Random kill -9 during operation

---

## Why runit Matters for Grainstore

**Simplicity**:
- Fewer states = easier to verify
- No graceful shutdown = no edge cases
- Crash-only = natural failure mode

**Sovereignty**:
- Tiny codebase (30KB) = full auditability
- No hidden complexity
- Easy to fork and maintain

**Complement to s6**:
- Use s6 for services that need graceful shutdown
- Use runit for services that should crash-restart
- Both can coexist in Grainstore!

---

## Related Documents

- **C Implementation**: http://smarden.org/runit/
- **Equivalence Note**: `../equivalence/runit-clj-nock.md`
- **Jet Note**: `../jets/runit.jet.md`
- **TLA+ Spec**: `../verified/runit.tla`

---

## Summary

runit is the **ultimate simplification** of process supervision:
1. **Two states**: down, up
2. **One operation**: restart
3. **One signal**: SIGKILL
4. **One invariant**: crashed services restart instantly

**All of this in 30KB of C code, now specified in Nock's 12 rules.**

The crash-only philosophy is eternal. Implementations may vary, but the mathematical essence—captured here—remains constant.

🌱 **"Crash is not failure. Crash is the path to known-good state."** 🌱

---

**Status**: Draft v0.1 (awaiting peer review and Oracle verification)  
**Next**: Create equivalence note and Clojure implementation

