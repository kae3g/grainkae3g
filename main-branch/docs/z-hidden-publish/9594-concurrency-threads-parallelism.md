# kae3g 9593: Concurrency - Threads and Parallelism

**Phase 1: Foundations & Philosophy** | **Week 4** | **Reading Time: 18 minutes**

---

## What You'll Learn

- Concurrency vs parallelism (similar but different!)
- Threads vs processes (lightweight vs heavyweight)
- Race conditions and why they're dangerous
- Synchronization primitives (locks, mutexes, semaphores)
- Deadlock and how to avoid it
- Clojure's concurrency model (atoms, refs, agents)
- Why immutability makes concurrency easier
- The garden metaphor: Multiple gardeners, shared tools

---

## Prerequisites

- **[9570: Processes](/12025-10/9570-processes-programs-in-motion)** - Process basics
- **[9580: Memory Management](/12025-10/9580-memory-management)** - How memory works
- **[9504: What Is Clojure?](/12025-10/9504-what-is-clojure)** - Immutability, functional programming
- **[9507: Helen Atthowe](/12025-10/9507-helen-atthowe-ecological-systems)** - Coordination in living systems

---

## Concurrency vs Parallelism

**Often confused, but different**:

### Concurrency

**Definition**: Dealing with multiple things **at once** (interleaved).

**Example**: One CPU, multiple processes:
```
Time:   0ms    10ms   20ms   30ms   40ms
CPU:    Task A Task B Task A Task C Task B
# Switching rapidly (looks simultaneous)
```

**Like**: One gardener tending multiple plots (switches between them).

### Parallelism

**Definition**: Doing multiple things **simultaneously** (truly at same time).

**Example**: Multiple CPUs, multiple processes:
```
Time:   0ms    10ms   20ms   30ms   40ms
CPU 1:  Task A Task A Task A Task A Task A
CPU 2:  Task B Task B Task B Task B Task B
CPU 3:  Task C Task C Task C Task C Task C
# Actually simultaneous
```

**Like**: Multiple gardeners, each on their own plot.

**Key difference**: 
- **Concurrency** = managing multiple tasks (might be on one CPU)
- **Parallelism** = actually executing simultaneously (requires multiple CPUs)

**Rob Pike** (Go creator): "Concurrency is about **dealing with** lots of things at once. Parallelism is about **doing** lots of things at once."

---

## Threads vs Processes

**Both** allow concurrency, but different trade-offs:

### Processes (Essay 9570)

```
Process A:  [Memory] [CPU] [Files]
Process B:  [Memory] [CPU] [Files]
# Isolated (separate memory spaces)
```

**Pros**:
- **Isolation** (one crash doesn't kill others)
- **Security** (can't access each other's memory)

**Cons**:
- **Heavyweight** (creating process = expensive)
- **Communication** requires IPC (inter-process communication - complex)

### Threads

```
Process:
  Thread 1: [Stack] →
  Thread 2: [Stack] → Shared [Heap] [Code] [Files]
  Thread 3: [Stack] →
# Share memory (except stack)
```

**Pros**:
- **Lightweight** (creating thread = cheap)
- **Easy communication** (shared memory)

**Cons**:
- **No isolation** (one thread crashes → entire process crashes)
- **Race conditions** (multiple threads → shared data → bugs!)

**Plant lens**: 
- **Processes** = separate gardens (isolated, safe)
- **Threads** = multiple gardeners in one garden (coordinated, but conflicts possible)

---

## The Race Condition Problem

**When threads share data**, order matters:

```python
# Shared counter
counter = 0

def increment():
    global counter
    temp = counter      # Read
    temp = temp + 1     # Compute
    counter = temp      # Write

# Two threads run increment() simultaneously:
Thread 1:  temp = counter (0)
Thread 2:  temp = counter (0)  # Both read 0!
Thread 1:  temp = temp + 1 (1)
Thread 2:  temp = temp + 1 (1)
Thread 1:  counter = temp (1)
Thread 2:  counter = temp (1)  # Should be 2, but it's 1!
```

**Result**: Lost update! (Expected: 2, actual: 1)

**This is a race condition** (result depends on timing - non-deterministic, awful for debugging).

---

## Synchronization: Locks and Mutexes

**Solution**: Ensure only **one thread** modifies shared data at a time.

### Mutex (Mutual Exclusion)

```python
import threading

counter = 0
lock = threading.Lock()

def increment():
    global counter
    with lock:  # Acquire lock
        temp = counter
        temp = temp + 1
        counter = temp
    # Release lock (automatic with 'with')

# Now: Only one thread can be inside 'with lock' block at a time
# Result: counter = 2 (correct!)
```

**Plant lens**: **"Mutex is like a gate key—only one gardener can have the key (and enter the greenhouse) at a time."**

### Semaphore

**Generalization**: Allow **N** threads (not just 1):

```python
semaphore = threading.Semaphore(3)  # Max 3 threads

def limited_access():
    with semaphore:
        # At most 3 threads here simultaneously
        expensive_operation()
```

**Use case**: Limit concurrent database connections, API calls, etc.

**Plant lens**: **"Semaphore is like a greenhouse with limited space—only 3 gardeners fit at once."**

---

## Deadlock: The Deadly Embrace

**When threads wait for each other**, forever:

```python
lock_A = threading.Lock()
lock_B = threading.Lock()

# Thread 1:
with lock_A:
    with lock_B:  # Needs B
        work()

# Thread 2:
with lock_B:
    with lock_A:  # Needs A
        work()

# Scenario:
# Thread 1 acquires A, waits for B
# Thread 2 acquires B, waits for A
# Both stuck forever! (DEADLOCK)
```

**Prevention**:
1. **Lock ordering**: Always acquire locks in same order (A then B, never B then A)
2. **Timeout**: Give up if lock isn't available (retry or fail gracefully)
3. **Avoid nested locks**: Minimize complexity

**Plant lens**: **"Deadlock is like two gardeners blocking each other's path—neither can move forward."**

---

## Clojure's Concurrency Model

**Clojure** (Essay 9504) makes concurrency **safer** through **immutability**:

### Atoms (Shared, Synchronous)

```clojure
(def counter (atom 0))

;; Increment (thread-safe!)
(swap! counter inc)

;; Multiple threads can call swap! safely
;; Clojure handles synchronization internally
```

**No explicit locks needed!** (Clojure uses compare-and-swap internally)

### Refs (Coordinated, Transactional)

```clojure
(def account-a (ref 100))
(def account-b (ref 200))

;; Transfer money (atomic transaction)
(dosync
  (alter account-a - 50)
  (alter account-b + 50))

;; All-or-nothing (like database transactions)
;; If one fails, both rollback
```

**STM** (Software Transactional Memory) ensures consistency.

### Agents (Asynchronous)

```clojure
(def logger (agent []))

;; Send work to agent (returns immediately)
(send logger conj "Log message 1")
(send logger conj "Log message 2")

;; Agent processes queue in background
```

**Fire-and-forget**: Good for logging, background tasks.

**Key insight**: **Immutability** + **explicit state management** = safer concurrency (no hidden race conditions).

---

## Concurrency Patterns

### Producer-Consumer

```
Producer Thread:  [Generate data] → Queue
Consumer Thread:  Queue → [Process data]

# Producer adds to queue
# Consumer removes from queue
# Queue handles synchronization
```

**Use case**: Web scraper (producer) + data analyzer (consumer).

### Thread Pool

```
Task Queue: [Task 1] [Task 2] [Task 3] ...
                ↓       ↓       ↓
Worker Threads: [Thread 1] [Thread 2] [Thread 3]
# Threads pick tasks from queue
```

**Benefit**: Reuse threads (avoid creation overhead).

**Use case**: Web servers (thread pool handles requests).

### Map-Reduce

```
Data: [1, 2, 3, 4, 5, 6, 7, 8]

Map (parallel):
  Thread 1: [1, 2] → [2, 4]
  Thread 2: [3, 4] → [6, 8]
  Thread 3: [5, 6] → [10, 12]
  Thread 4: [7, 8] → [14, 16]

Reduce (combine):
  [2, 4, 6, 8, 10, 12, 14, 16] → sum = 72
```

**Functional approach** (no shared state, easier to parallelize).

**Clojure**: `pmap` (parallel map), `reducers` library.

---

## Why Immutability Helps

**Mutable shared state** = race conditions:

```python
# BAD: Mutable list, multiple threads
shared_list = []

def thread_work():
    shared_list.append(1)  # RACE CONDITION!
```

**Immutable** = no race conditions:

```clojure
;; GOOD: Immutable vector
(def shared-vec (atom []))

(defn thread-work []
  (swap! shared-vec conj 1))  ; Clojure handles synchronization
```

**Key insight**: Can't have a race condition if data **can't be modified**.

**Plant lens**: **"Immutable data is like seeds (you can't change the genetics). Mutable data is like actively growing plants (multiple gardeners might interfere)."**

---

## Try This

### Exercise 1: Race Condition Demo

```python
import threading

counter = 0

def increment():
    global counter
    for _ in range(100000):
        counter += 1

threads = [threading.Thread(target=increment) for _ in range(10)]

for t in threads:
    t.start()

for t in threads:
    t.join()

print(f"Counter: {counter}")
# Expected: 1,000,000 (10 threads × 100,000)
# Actual: ~600,000-900,000 (WRONG! Race conditions!)
```

**Fix**: Add a lock around `counter += 1`.

---

### Exercise 2: Deadlock Demo

```python
import threading
import time

lock_a = threading.Lock()
lock_b = threading.Lock()

def thread_1():
    with lock_a:
        print("Thread 1: acquired A")
        time.sleep(0.1)  # Simulate work
        with lock_b:  # Wait for B
            print("Thread 1: acquired B")

def thread_2():
    with lock_b:
        print("Thread 2: acquired B")
        time.sleep(0.1)
        with lock_a:  # Wait for A
            print("Thread 2: acquired A")

# Run both:
t1 = threading.Thread(target=thread_1)
t2 = threading.Thread(target=thread_2)
t1.start()
t2.start()

# Hangs! (deadlock)
```

---

### Exercise 3: Clojure Atoms

```clojure
;; In Clojure REPL
(def counter (atom 0))

;; Safe increment (no race conditions!)
(defn safe-increment []
  (dotimes [_ 100000]
    (swap! counter inc)))

;; Run in multiple threads
(doseq [i (range 10)]
  (future (safe-increment)))

;; Wait a bit, then check:
@counter
;; Result: 1,000,000 (CORRECT! No locks needed)
```

**Observe**: Immutability + atoms = safe concurrency.

---

## Going Deeper

### Related Essays
- **[9570: Processes](/12025-10/9570-processes-programs-in-motion)** - Process-level concurrency
- **[9504: What Is Clojure?](/12025-10/9504-what-is-clojure)** - Immutability, atoms, refs
- **[9520: Functional Programming](/12025-10/9520-functional-programming-basics)** - Pure functions (inherently thread-safe)
- **[9507: Helen Atthowe](/12025-10/9507-helen-atthowe-ecological-systems)** - Coordination in living systems

### External Resources
- **"The Little Book of Semaphores"** - Concurrency patterns
- **"Java Concurrency in Practice"** - Despite title, concepts apply everywhere
- **Clojure Concurrency Docs** - STM, atoms, agents
- **Go's Concurrency Model** - Goroutines, channels (alternative approach)

---

## Reflection Questions

1. **Is concurrency worth the complexity?** (Single-threaded is simpler - when do you really need concurrency?)

2. **Why does Rust prevent data races at compile-time?** (Ownership system - only one mutable reference at a time!)

3. **Could all programs be purely functional?** (No mutable state = no race conditions - but what about I/O?)

4. **Is shared memory the right abstraction?** (Message passing (Go, Erlang) vs shared memory (threads) - which is better?)

5. **How would Nock handle concurrency?** (Deterministic, no time - concurrency must be explicit input!)

---

## Summary

**Concurrency vs Parallelism**:
- **Concurrency**: Dealing with multiple tasks (might be on one CPU)
- **Parallelism**: Executing simultaneously (requires multiple CPUs)

**Threads vs Processes**:
- **Processes**: Isolated, heavyweight, secure
- **Threads**: Shared memory, lightweight, race-prone

**Synchronization Primitives**:
- **Mutex/Lock**: One thread at a time
- **Semaphore**: N threads at a time
- **Condition Variable**: Wait for specific condition

**Concurrency Problems**:
- **Race condition**: Result depends on timing (non-deterministic)
- **Deadlock**: Threads wait for each other forever
- **Starvation**: Thread never gets CPU time

**Clojure's Approach**:
- **Atoms**: Shared, synchronous (compare-and-swap)
- **Refs**: Coordinated, transactional (STM)
- **Agents**: Asynchronous (background queue)
- **Immutability**: Eliminates most race conditions

**Key Insights**:
- **Shared mutable state is the enemy** (causes races)
- **Immutability is thread-safe** (can't modify = can't conflict)
- **Locks are error-prone** (forget lock = race, wrong order = deadlock)
- **Functional approaches win** (pure functions, immutable data)

**In the Valley**:
- **We prefer immutability** (Clojure's atoms, not raw locks)
- **We use message passing** when possible (actors, channels)
- **We avoid shared mutable state** (functional design)
- **We test concurrency** (race conditions are sneaky!)

**Plant lens**: **"Concurrency is multiple gardeners coordinating in shared garden—immutable tools (can't change hoe itself) prevent conflicts, while mutable soil (shared state) requires careful turn-taking."**

---

**Next**: We'll explore **build systems**—how source code becomes executable programs, the compilation pipeline, and why reproducible builds matter!

---

**Navigation**:  
← Previous: [9593 (networking basics sockets protocols)](/12025-10/9593-networking-basics-sockets-protocols) | **Phase 1 Index** | Next: [9595 (build systems source to binary)](/12025-10/9595-build-systems-source-to-binary)

**Metadata**:
- **Phase**: 1 (Foundations)
- **Week**: 4
- **Prerequisites**: 9570, 9580, 9504, 9507
- **Concepts**: Concurrency, parallelism, threads, race conditions, locks, deadlock, Clojure concurrency (atoms, refs, agents), immutability
- **Next Concepts**: Build systems, compilation, linking, reproducible builds
- **Plant Lens**: Multiple gardeners (threads), shared garden (memory), tools (immutable = safe), soil (mutable = conflicts)



---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*