# kae3g 9580: Memory Management - Stack, Heap, and Virtual Memory

**Phase 1: Foundations & Philosophy** | **Week 3** | **Reading Time: 17 minutes**

---

## What You'll Learn

- How processes use memory (stack vs heap)
- Virtual memory: The beautiful illusion your OS creates
- Memory allocation and deallocation
- Memory leaks and how to avoid them
- Why garbage collection matters (or doesn't)
- Memory as a finite resource (like water in a garden)
- Practical debugging with memory tools

---

## Prerequisites

- **[9500: What Is a Computer?](/12025-10/9500-what-is-a-computer)** - Memory basics
- **[9570: Processes](/12025-10/9570-processes-programs-in-motion)** - How processes work
- **[9507: Helen Atthowe](/12025-10/9507-helen-atthowe-ecological-systems)** - Resource management metaphor

---

## Memory: The Finite Garden

**Plant lens**: Memory is like **water in your garden**.

- **Limited resource** (4GB, 16GB, 64GB RAM - but still finite!)
- **Processes compete** (like plants for water)
- **Waste depletes it** (memory leaks = water runoff)
- **Must be managed carefully** (drip irrigation > flood and hope)

**This essay**: How your OS and programs manage this precious resource.

---

## The Memory Hierarchy

**Your computer has multiple "layers" of memory** (speed vs capacity trade-off):

| Type | Size | Speed | Purpose |
|------|------|-------|---------|
| **CPU Registers** | ~100 bytes | Fastest (1 cycle) | Current operation |
| **L1 Cache** | ~32 KB | Very fast (2-4 cycles) | Hot data |
| **L2 Cache** | ~256 KB | Fast (10-20 cycles) | Warm data |
| **L3 Cache** | ~8 MB | Moderate (40-60 cycles) | Shared cache |
| **RAM (Main Memory)** | 4-64 GB | Slow (100+ cycles) | Active data |
| **SSD** | 256 GB - 2 TB | Very slow (100,000+ cycles) | Persistent data |
| **HDD** | 1-8 TB | Extremely slow (1,000,000+ cycles) | Cold storage |

**Key insight**: CPU is **starving** for data (registers empty in nanoseconds). Memory hierarchy tries to keep it fed.

**This essay focuses on RAM** (main memory) - where your processes live.

---

## Virtual Memory: The Beautiful Illusion

**Physical memory** (RAM): 16 GB (example).

**Your process thinks**: "I have the entire 64-bit address space!" (18 exabytes = 18 billion GB!)

**How?** **Virtual memory** - OS creates an illusion.

### The Mapping

**Each process has**:
- **Virtual address space** (huge, e.g., 0x0000 to 0x7FFF_FFFF_FFFF)
- **Page table** (maps virtual addresses → physical addresses)

**Example**:
```
Virtual Address    Physical Address
0x1000_0000   →    0x0A3F_2000  (in RAM)
0x1000_1000   →    0x0B12_8000  (in RAM)
0x2000_0000   →    (not mapped - will page fault!)
0x3000_0000   →    (on disk, swap file)
```

**Benefits**:
1. **Isolation**: Process can't access another's memory (security!)
2. **Simplicity**: Program sees contiguous memory (even if fragmented in RAM)
3. **Overcommit**: Total virtual memory > physical RAM (use disk as backup)

**Plant lens**: Like **terraced gardens** (each level thinks it has flat land, but it's transformed by the landscape).

---

## Stack vs Heap

**Your process memory** is divided into regions:

```
High addresses (0x7FFF_FFFF_FFFF)
    ↑
    | Stack (grows downward)
    |   - Local variables
    |   - Function call frames
    |   - Return addresses
    |
    | (unused space)
    |
    | Heap (grows upward)
    ↓   - Dynamically allocated memory
    |   - malloc(), new, etc.
    |
    | BSS (uninitialized data)
    | Data (initialized global variables)
    | Text (code)
Low addresses (0x0000_0000)
```

### The Stack

**Automatic memory management**:

```c
void function() {
    int x = 10;        // On stack
    char buffer[100];  // On stack
    
    // When function returns, x and buffer are GONE
    // (stack frame is popped)
}
```

**Properties**:
- **Fast** (just adjust stack pointer)
- **Limited size** (typically 1-8 MB per thread)
- **LIFO** (Last In, First Out - like a stack of plates)
- **Automatic cleanup** (no memory leaks!)

**Stack overflow**: If you exceed stack size (e.g., infinite recursion):
```c
void infinite() {
    int bigArray[1000000];  // 4 MB!
    infinite();  // Recurse → stack overflow
}
```

**Plant lens**: Stack is like **annual plants** (live one season, then gone automatically).

### The Heap

**Manual memory management**:

```c
void function() {
    int* x = malloc(sizeof(int));  // On heap
    *x = 10;
    
    // x still exists after function returns!
    // Must call free(x) or it leaks!
}
```

**Properties**:
- **Slower** (complex allocation algorithms)
- **Large** (gigabytes available)
- **Random access** (allocate/free in any order)
- **Manual cleanup** (or garbage collector)

**Memory leak**: Forgetting to free:
```c
void leak() {
    int* x = malloc(100);
    // ... use x ...
    // Forgot to free(x)!
    // That 100 bytes is LOST until process exits
}
```

**Plant lens**: Heap is like **perennial plants** (live for years, must be tended or they overgrow).

---

## Memory Allocation: Under the Hood

**When you call `malloc(100)`**:

1. **Heap allocator** searches for free block (≥100 bytes)
2. **Splits block** if larger than needed (e.g., 256 byte block → 100 used + 156 free)
3. **Updates metadata** (linked list of free blocks)
4. **Returns pointer** to your memory

**When you call `free(ptr)`**:

1. **Mark block as free**
2. **Coalesce** with adjacent free blocks (merge to reduce fragmentation)
3. **Update free list**

**Fragmentation problem**:
```
Heap: [used 50][free 10][used 30][free 10][used 20]
You need: 20 bytes

Problem: Two 10-byte free blocks, but not contiguous!
Can't allocate 20 bytes (even though 20 bytes free total).

This is FRAGMENTATION.
```

**Solution**: Garbage collector (compacts memory) or careful allocation patterns.

---

## Garbage Collection vs Manual Management

### Manual (C, C++, Rust without GC)

**You control**:
```c
int* x = malloc(100);
// ... use x ...
free(x);  // YOU must free!
```

**Pros**:
- **Predictable** (you know when free happens)
- **Low overhead** (no GC pauses)
- **Fine control** (can optimize for cache, etc.)

**Cons**:
- **Error-prone** (forget to free → leak, double free → crash)
- **Complex** (ownership rules, lifetimes)

**Rust**: Compile-time memory safety (borrow checker prevents leaks/dangling pointers).

### Garbage Collection (Java, Python, JavaScript, Go, Clojure)

**System controls**:
```java
Object x = new Object();  // Allocated
// ... use x ...
// No explicit free!
// GC reclaims it when no references remain
```

**Pros**:
- **Safe** (no manual free → no double-free, no leaks from forgetting)
- **Simple** (don't think about memory)

**Cons**:
- **Unpredictable pauses** (GC runs when it wants)
- **Overhead** (GC tracking uses CPU/memory)
- **Less control** (can't fine-tune for performance)

**Trade-off**: Safety vs control.

**Plant lens**: 
- **Manual = hand-watering** each plant (precise, but labor-intensive)
- **GC = automated irrigation** (convenient, but less precise)

---

## Memory Leaks: The Silent Killer

**A memory leak**: Memory allocated but never freed.

**Example** (JavaScript):
```javascript
let cache = {};

function addToCache(key, value) {
    cache[key] = value;
    // Never remove old entries!
    // Cache grows forever → memory leak!
}

// Fix: Periodically prune cache
function pruneCache() {
    const maxSize = 1000;
    if (Object.keys(cache).length > maxSize) {
        cache = {};  // Reset
    }
}
```

**Symptoms**:
- Process memory grows over time
- Eventually: Out of memory (OOM), crash
- Slow performance (more GC pressure)

**Debugging**:
```bash
# Watch memory usage
top -pid <PID>

# Or:
htop  # Visual

# Heap profiler (Node.js)
node --inspect app.js
# Use Chrome DevTools → Memory tab
```

**Plant lens**: Memory leak = **invasive species** (grows unchecked, chokes out other plants).

---

## Virtual Memory in Practice

### Page Faults

**What happens** when you access unmapped memory?

```c
int* ptr = (int*)0x10000000;  // Virtual address
*ptr = 42;  // Write to this address
```

**Steps**:
1. **CPU tries to access** 0x10000000
2. **MMU (Memory Management Unit)** checks page table
3. **Not mapped!** → **Page fault** (trap to kernel)
4. **Kernel decides**:
   - Valid but not in RAM? → Load from disk (swap in)
   - Invalid address? → **Segmentation fault** (kill process!)
5. **Resume** (if valid)

**Types of page faults**:
- **Minor**: Page in memory, just needs mapping (fast)
- **Major**: Page on disk, must load (slow!)
- **Invalid**: Illegal access (crash!)

### Swapping

**Physical RAM full?** OS moves pages to disk (swap space).

```
Process A:  [Page 1] [Page 2] [Page 3] [Page 4]
           In RAM    In RAM   On disk  In RAM

# Process accesses Page 3 (on disk)
# OS swaps out Page 4 (least recently used)
# OS swaps in Page 3

Process A:  [Page 1] [Page 2] [Page 3] [Page 4]
           In RAM    In RAM   In RAM   On disk
```

**Thrashing**: Too much swapping (disk is slow!), system grinds to halt.

**Plant lens**: Swapping = **compost bin** (temporarily store what doesn't fit in active garden, retrieve when needed).

---

## Memory Safety: Rust vs C

### C (Unsafe)

**Dangling pointer**:
```c
int* ptr = malloc(sizeof(int));
*ptr = 42;
free(ptr);
*ptr = 10;  // DANGLING POINTER! (ptr points to freed memory)
            // Undefined behavior (might crash, might corrupt data)
```

**Buffer overflow**:
```c
char buffer[10];
strcpy(buffer, "This is way too long!");  // OVERFLOW!
// Writes past buffer end, corrupts adjacent memory
// Security vulnerability!
```

### Rust (Safe)

**Ownership rules** (compile-time):
```rust
fn main() {
    let x = Box::new(42);  // Heap allocation
    let y = x;  // Ownership moves to y
    
    // println!("{}", x);  // COMPILE ERROR! (x no longer owns the data)
    println!("{}", y);  // OK
}  // y dropped, memory freed automatically
```

**Borrow checker**:
```rust
fn main() {
    let mut x = vec![1, 2, 3];
    let y = &x;  // Immutable borrow
    
    // x.push(4);  // COMPILE ERROR! (can't mutate while borrowed)
    println!("{:?}", y);
}  // y's borrow ends, x can be mutated again
```

**Result**: Memory safety **without** garbage collection (compile-time guarantees!).

**Plant lens**: Rust is **no-till farming** (Helen Atthowe, Essay 9507) - gentle, precise intervention, no disruption.

---

## Practical Memory Debugging

### Tools

**C/C++**:
```bash
# Valgrind (memory leak detector)
valgrind --leak-check=full ./myprogram

# Output:
# LEAK SUMMARY:
#    definitely lost: 100 bytes in 1 blocks
#    (Shows where allocation happened)
```

**Rust**:
```bash
# Miri (interpreter that detects undefined behavior)
cargo +nightly miri run

# Or just compile (borrow checker catches most issues)
cargo build
```

**Java**:
```bash
# Heap dump
jmap -dump:live,format=b,file=heap.bin <pid>

# Analyze with VisualVM, Eclipse MAT, etc.
```

**JavaScript (Node.js)**:
```bash
# Heap snapshot
node --inspect app.js
# Chrome DevTools → Memory → Take snapshot
```

### Detecting Leaks

**Pattern**:
1. **Baseline**: Measure memory at start
2. **Exercise**: Run workload
3. **Idle**: Let GC run, stabilize
4. **Compare**: Memory higher than baseline? Leak!

**Example** (Node.js):
```javascript
console.log(process.memoryUsage().heapUsed);
// 10 MB

for (let i = 0; i < 1000; i++) {
    doWork();
}

global.gc();  // Force GC (node --expose-gc)
console.log(process.memoryUsage().heapUsed);
// 50 MB (grew 40 MB - leak!)
```

---

## Memory-Efficient Design

### Principle 1: Reuse Allocations

**Bad** (allocate every time):
```clojure
(defn process-items [items]
  (map (fn [item]
         (let [buffer (byte-array 1024)]  ; Allocate!
           (process item buffer)))
       items))
;; Allocates 1024 bytes × N items (wasteful!)
```

**Good** (reuse buffer):
```clojure
(defn process-items [items]
  (let [buffer (byte-array 1024)]  ; Allocate once
    (map (fn [item]
           (process item buffer))  ; Reuse
         items)))
;; Allocates 1024 bytes once (efficient!)
```

### Principle 2: Lazy Evaluation

**Bad** (realize entire sequence):
```clojure
(def huge-data (range 1000000))  ; 1M integers in memory!

(defn process-all []
  (doall (map expensive-operation huge-data)))
;; All 1M results in memory at once!
```

**Good** (lazy, process incrementally):
```clojure
(def huge-data (range 1000000))

(defn process-all []
  (doseq [item huge-data]  ; Lazy, one at a time
    (expensive-operation item)))
;; Only one item in memory at a time!
```

### Principle 3: Avoid Defensive Copies

**Bad** (copy unnecessarily):
```clojure
(defn get-config []
  (let [config (load-config)]
    (into {} config)))  ; COPY (defensive, but wasteful if config is immutable!)
```

**Good** (immutable, no copy needed):
```clojure
(defn get-config []
  (load-config))  ; Return directly (immutable, safe to share!)
```

**Clojure wins**: Persistent data structures (structural sharing) avoid copies.

---

## Try This

### Exercise 1: Measure Your Process Memory

```bash
# Start a process
python3 -c "import time; x = [0] * 10000000; time.sleep(60)"
# (Allocates ~80 MB, sleeps 60 seconds)

# In another terminal:
ps aux | grep python3
# Look at RSS (Resident Set Size) column

# Or:
top -pid <PID>
```

**Observe**: Memory usage (RSS) shows ~80 MB.

---

### Exercise 2: Create a Memory Leak

```python
# leak.py
cache = []

while True:
    cache.append("x" * 1000000)  # 1 MB string
    print(f"Allocated {len(cache)} MB")
    import time
    time.sleep(1)

# Run:
python3 leak.py

# Watch memory grow (top or htop)
# Ctrl-C to stop before OOM!
```

**Observe**: Memory grows indefinitely (leak!).

---

### Exercise 3: Profile Heap Allocation

**Rust**:
```bash
cargo install cargo-flamegraph
cargo flamegraph --bin myapp

# Opens flamegraph (visual heap profile)
```

**Node.js**:
```bash
node --inspect app.js
# Chrome → chrome://inspect → Open DevTools
# Memory tab → Take heap snapshot
```

---

## Going Deeper

### Related Essays
- **[9500: What Is a Computer?](/12025-10/9500-what-is-a-computer)** - Memory hierarchy basics
- **[9570: Processes](/12025-10/9570-processes-programs-in-motion)** - How processes use memory
- **[9507: Helen Atthowe](/12025-10/9507-helen-atthowe-ecological-systems)** - Resource management (water = memory)
- **[9955: Redox OS](/12025-10/9955-redox-os-rust-microkernel)** - Rust memory safety (narrative series)

### External Resources
- **"What Every Programmer Should Know About Memory"** - Ulrich Drepper (deep dive)
- **Valgrind documentation** - Memory debugging tools
- **Rust Book Chapter 4** - Ownership and borrowing
- **"The Garbage Collection Handbook"** - Comprehensive GC reference

---

## Reflection Questions

1. **Is garbage collection "free"?** (What are you trading? Convenience for...?)

2. **Can you have memory leaks in a GC'd language?** (Yes! Holding references = memory can't be collected)

3. **Why does Rust's borrow checker prevent memory issues?** (Compile-time tracking of ownership = no runtime errors)

4. **Is virtual memory always good?** (Hides real limits, can lead to thrashing if overused)

5. **How much memory does your main project use?** (Have you measured? Profiled? Optimized?)

---

## Summary

**Memory Management Concepts**:

**Virtual Memory**:
- Each process sees huge address space (illusion!)
- Page tables map virtual → physical addresses
- Isolation (processes can't access each other's memory)

**Stack vs Heap**:
- **Stack**: Fast, automatic, limited (LIFO, function locals)
- **Heap**: Slower, manual/GC, large (random access, long-lived data)

**Allocation Strategies**:
- **Manual** (C/C++): Full control, error-prone
- **GC** (Java/Python/Clojure): Safe, overhead
- **Rust**: Compile-time safety, no GC!

**Memory Issues**:
- **Leaks**: Allocate but never free (grows until OOM)
- **Dangling pointers**: Use-after-free (undefined behavior)
- **Fragmentation**: Free space exists but not contiguous

**Key Insights**:
- **Memory is finite** (like water in a garden - manage carefully!)
- **Virtual memory is illusion** (brilliant OS abstraction)
- **Stack is fast, heap is flexible** (use each appropriately)
- **GC trades control for safety** (no one-size-fits-all)
- **Rust proves GC optional** (compile-time guarantees work!)

**In the Valley**:
- **We respect memory limits** (finite resource, like water)
- **We profile before optimizing** (observation before action - Helen's principle)
- **We choose immutability** (Clojure persistent data - structural sharing)
- **We avoid premature allocation** (lazy evaluation, reuse buffers)

**Plant lens**: **"Memory is water—finite, must flow to all plants (processes), leaks waste it, closed-loop systems conserve it."**

---

**Next**: We'll explore **the filesystem**—how your OS organizes persistent storage hierarchically, like a well-structured garden with paths, directories, and careful organization!

---

**Navigation**:  
← Previous: [9570 (processes programs in motion)](/12025-10/9570-processes-programs-in-motion) | **Phase 1 Index** | Next: [9591 (filesystem hierarchical organization)](/12025-10/9591-filesystem-hierarchical-organization)

**Metadata**:
- **Phase**: 1 (Foundations)
- **Week**: 3
- **Prerequisites**: 9500, 9570, 9507
- **Concepts**: Virtual memory, stack, heap, garbage collection, memory leaks, Rust ownership, page faults, swapping
- **Next Concepts**: Filesystem, directories, paths, inodes
- **Plant Lens**: Memory = water (finite resource), leaks = runoff, closed-loop = conservation, stack = annuals, heap = perennials



---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*