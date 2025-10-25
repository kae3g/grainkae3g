# kae3g 9599: Debugging - Finding and Fixing Issues

**Phase 1: Foundations & Philosophy** | **Week 5** | **Reading Time: 17 minutes**

---

## What You'll Learn

- The scientific method for debugging (Ibn al-Haytham's legacy!)
- Mental models for understanding failures
- Debugging tools (print, debugger, profiler, strace)
- Common bug patterns and how to spot them
- The REPL as debugging superpower (Clojure!)
- Why understanding systems beats random guessing
- Debugging as diagnosing the garden's ailments

---

## Prerequisites

- **[9570: Processes](/12025-10/9570-processes-programs-in-motion)** - What fails
- **[9597: Testing](/12025-10/9597-testing-verification-validation)** - Finding bugs
- **[9504: What Is Clojure?](/12025-10/9504-what-is-clojure)** - REPL-driven development
- **[9505: House of Wisdom](/12025-10/9505-house-of-wisdom-knowledge-gardens)** - Ibn al-Haytham's scientific method

---

## The Scientific Method for Debugging

**Ibn al-Haytham** (Essay 9505) pioneered the **scientific method** (10th century):

1. **Observe** (what's broken?)
2. **Hypothesize** (why might it be broken?)
3. **Experiment** (test hypothesis)
4. **Conclude** (was hypothesis correct?)
5. **Iterate** (if wrong, new hypothesis)

**This is debugging!**

**Example**:

**Observe**: "Server returns 500 error"

**Hypothesize**: "Maybe database connection failed?"

**Experiment**: Check logs:
```bash
tail /var/log/app.log
# Error: Connection refused to localhost:5432
```

**Conclude**: Yes, database isn't running!

**Fix**: Start database.

**Plant lens**: **"Debugging is diagnosing the garden—observe symptoms (yellowing leaves), hypothesize cause (nitrogen deficiency?), test (soil sample), treat (add compost)."**

---

## Mental Models

**Understanding the system** beats random guessing:

### Model 1: The Stack

**When program crashes**, where?

```
Your Code
    ↓ calls
Library A
    ↓ calls
Library B
    ↓ calls
Operating System
    ↓ calls
Hardware
```

**Error could be anywhere!**

**Strategy**: Work backwards (check your code, then libraries, then OS).

### Model 2: The Data Flow

**Where does data transform?**

```
User Input → Validation → Processing → Database → Response
```

**Bug location**:
- User input corrupted? (Validation failed)
- Processing wrong? (Logic bug)
- Database error? (SQL issue, connection problem)

**Strategy**: Trace data through pipeline (print at each stage).

### Model 3: The State Machine

**Program has states**:

```
States: Idle → Loading → Ready → Error
Transitions: start() → load() → ready() → fail()
```

**Bug**: Unexpected state transition.

**Strategy**: Log state changes, find illegal transition.

---

## Debugging Tools

### Printf Debugging (Simplest!)

```clojure
(defn mysterious-function [x]
  (println "DEBUG: x =" x)  ; Print value!
  (let [result (* x x)]
    (println "DEBUG: result =" result)
    result))

; Output:
; DEBUG: x = 5
; DEBUG: result = 25
```

**Pros**: Simple, works everywhere  
**Cons**: Clutters code, manual cleanup

**Better**: Use logging library (can disable in production).

### Interactive Debugger

**Set breakpoints**, inspect state:

```python
import pdb

def buggy_function(x):
    y = x * 2
    pdb.set_trace()  # STOP HERE!
    z = y / 0  # Bug!
    return z

# When you run, program stops at breakpoint
# You can inspect: x, y, etc.
# Commands: n (next), s (step), c (continue), p (print)
```

**Pros**: Interactive, can inspect everything  
**Cons**: Slower than print debugging for simple cases

### REPL (Clojure Superpower!)

**Test code interactively**:

```clojure
;; In REPL
(defn process-data [items]
  (map #(* % 2) items))

;; Test immediately
(process-data [1 2 3])
;; => (2 4 6)

;; Modify
(defn process-data [items]
  (map #(+ % 10) items))  ; Changed!

;; Test again (instant feedback!)
(process-data [1 2 3])
;; => (11 12 13)
```

**No recompile, no restart!** (Essay 9504 - REPL-driven development)

### System Call Tracing

**See what program does** (system calls):

```bash
# Linux
strace ls
# Output: Every system call (open, read, write, close, ...)

# macOS
dtruss ls
# (Requires sudo)
```

**Use case**: "Why is this slow?" (Trace shows 1000s of file opens!)

### Profiler

**Find performance bottlenecks**:

```bash
# Python
python -m cProfile slow_script.py
# Shows: Which functions take most time

# Or:
import cProfile
cProfile.run('main()')
```

**Output**: Function call counts, cumulative time.

**Reveals**: "90% of time in one function—optimize THAT!"

---

## Common Bug Patterns

### Off-by-One

```python
# Bug
for i in range(len(items)):
    print(items[i+1])  # CRASH on last item!

# Fix
for i in range(len(items) - 1):
    print(items[i+1])

# Or better:
for i in range(1, len(items)):
    print(items[i])
```

### Null/None Errors

```javascript
const user = getUser(id);
console.log(user.name);  // CRASH if user is null!

// Fix
const user = getUser(id);
if (user) {
    console.log(user.name);
} else {
    console.log("User not found");
}
```

**Clojure's advantage**: No null! (Uses `nil`, but explicit handling).

### Race Conditions

**Essay 9593** covered this:

```python
# Two threads
counter += 1  # Not atomic!

# Fix
with lock:
    counter += 1
```

**Hardest to debug** (non-deterministic—appears/disappears randomly).

---

## Debugging Strategies

### 1. Reproduce First

**Can't fix** what you can't reproduce.

**Steps**:
1. Find minimal example that triggers bug
2. Automate (write test that fails)
3. Fix (until test passes)
4. Commit test (prevent regression!)

### 2. Bisect (Binary Search)

**Bug appeared recently?** Find which commit introduced it:

```bash
git bisect start
git bisect bad          # Current commit is bad
git bisect good abc123  # This old commit was good

# Git checks out middle commit
# Test: Does bug exist?
git bisect bad  # If yes
# Or:
git bisect good  # If no

# Git keeps bisecting until it finds exact commit!
```

**Powerful**: Narrows down from 100 commits to 1 (in ~7 tests).

### 3. Rubber Duck Debugging

**Explain problem** to rubber duck (or colleague, or yourself):

```
"So the server crashes when...
 Wait, I'm sending a string but it expects an integer!
 That's the bug!"
```

**Often**: Explaining the problem **reveals the solution**.

### 4. Read Error Messages

**They tell you what's wrong!**

```
Traceback (most recent call last):
  File "app.py", line 42, in process
    result = items[10]
IndexError: list index out of range

^-- Line 42, accessing index 10, but list too short!
```

**Don't ignore errors** (read carefully, they're helpful!).

---

## The REPL Advantage

**Clojure's REPL** (Essay 9504) makes debugging **interactive**:

```clojure
;; Function is broken
(defn broken [data]
  (map :value data))  ; Assumes data is collection of maps

;; Test in REPL
(broken [{:value 1} {:value 2}])
;; => (1 2)  Works!

(broken nil)
;; => NullPointerException  AHA!

;; Fix
(defn fixed [data]
  (map :value (or data [])))  ; Handle nil

;; Test immediately
(fixed nil)
;; => ()  Works!
```

**Instant feedback loop** (no recompile, no restart).

**This is why** dynamic languages (Clojure, Python, JavaScript) are popular for exploration.

---

## Try This

### Exercise 1: Printf Debugging

```python
def mystery_bug(items):
    result = []
    for item in items:
        if item > 0:
            result.append(item * 2)
    return result

# Test
print(mystery_bug([1, -5, 3, 0, 7]))
# Expected: [2, 6, 14]
# Actual: [2, 6, 14]  (Correct!)

print(mystery_bug([]))
# Expected: []
# Crashes? Or works? (Test edge cases!)
```

**Add prints**:
```python
def mystery_bug(items):
    print(f"DEBUG: items = {items}")
    result = []
    for item in items:
        print(f"DEBUG: processing {item}")
        if item > 0:
            result.append(item * 2)
    print(f"DEBUG: result = {result}")
    return result
```

---

### Exercise 2: Git Bisect

```bash
# Create test repo
mkdir bisect-test && cd bisect-test
git init

# Create 10 commits
for i in {1..10}; do
    echo "Version $i" > file.txt
    git add file.txt
    git commit -m "Version $i"
done

# Introduce "bug" in commit 7
git checkout HEAD~3  # Go to commit 7
echo "BUG!" >> file.txt
git commit --amend -m "Version 7 (with bug)"
git checkout main

# Now bisect to find it!
git bisect start
git bisect bad
git bisect good HEAD~9

# Check each commit:
grep "BUG" file.txt && git bisect bad || git bisect good

# Git finds commit 7!
```

---

### Exercise 3: REPL Exploration

```clojure
;; In Clojure REPL
(defn divide [a b]
  (/ a b))

;; Test
(divide 10 2)
;; => 5

(divide 10 0)
;; => ArithmeticException: Divide by zero

;; Fix interactively
(defn safe-divide [a b]
  (if (zero? b)
    nil
    (/ a b)))

;; Test immediately
(safe-divide 10 0)
;; => nil  (Better!)
```

**Observe**: Immediate feedback (no compilation wait).

---

## Going Deeper

### Related Essays
- **[9597: Testing](/12025-10/9597-testing-verification-validation)** - Finding bugs early
- **[9504: What Is Clojure?](/12025-10/9504-what-is-clojure)** - REPL debugging
- **[9505: House of Wisdom](/12025-10/9505-house-of-wisdom-knowledge-gardens)** - Ibn al-Haytham's method
- **[9593: Concurrency](/12025-10/9593-concurrency-threads-parallelism)** - Race condition debugging

### External Resources
- **"Debugging: The 9 Indispensable Rules"** - David Agans
- **GDB Tutorial** - GNU debugger (C/C++)
- **pdb documentation** - Python debugger
- **Chrome DevTools** - Web debugging

---

## Reflection Questions

1. **Is debugging an art or science?** (Scientific method + intuition—both!)

2. **Why do some bugs only appear in production?** (Environment differences, race conditions, scale)

3. **Should all bugs be fixed immediately?** (Triage - critical vs minor, fix now vs defer)

4. **Can formal verification eliminate debugging?** (seL4 has zero bugs—but 11 person-years to verify!)

5. **How would Nock programs be debugged?** (Pure functions (noun → noun) - deterministic, traceable!)

---

## Summary

**Debugging is**:
- **Scientific process** (Ibn al-Haytham's method!)
- **Hypothesis-driven** (guess → test → refine)
- **Systematic** (not random guessing)

**Mental Models**:
- **The Stack**: Work backwards (your code → libraries → OS)
- **Data Flow**: Trace transformations (input → pipeline → output)
- **State Machine**: Track state transitions (find illegal ones)

**Tools**:
- **Printf**: Simple, universal (add prints, observe)
- **Debugger**: Interactive (breakpoints, inspect state)
- **REPL**: Immediate feedback (Clojure superpower!)
- **strace/dtruss**: System call tracing (see what program does)
- **Profiler**: Find bottlenecks (which function is slow)

**Common Patterns**:
- **Off-by-one**: Array indexing errors
- **Null errors**: Missing null checks
- **Race conditions**: Concurrent access to shared state

**Strategies**:
- **Reproduce first**: Can't fix what you can't trigger
- **Bisect**: Binary search through commits (git bisect)
- **Rubber duck**: Explain to someone (reveals solution!)
- **Read errors**: They tell you what's wrong!

**REPL Advantage**:
- **Interactive testing** (no recompile!)
- **Immediate feedback** (edit → test → refine)
- **Exploration** (try things, see results)
- **Why Clojure wins** for exploratory programming

**In the Valley**:
- **We use scientific method** (observe, hypothesize, test)
- **We prefer REPLs** (immediate feedback loop)
- **We write tests** (prevent regressions)
- **We understand systems** (mental models > guessing)

**Plant lens**: **"Debugging is diagnosing garden ailments—observe symptoms (yellow leaves), hypothesize (nitrogen?), test (soil sample), treat (compost), verify (leaves green again)."**

---

**Next**: **Essay 9600 - Phase 1 Synthesis!** We'll tie together everything you've learned in the first 100 essays and prepare you for Phase 2!

---

**Navigation**:  
← Previous: [9599 (documentation writing for humans)](/12025-10/9599-documentation-writing-for-humans) | **Phase 1 Index** | Next: [9601 (phase 1 synthesis foundations laid)](/12025-10/9601-phase-1-synthesis-foundations-laid)

**Metadata**:
- **Phase**: 1 (Foundations)
- **Week**: 5
- **Prerequisites**: 9570, 9597, 9504, 9505
- **Concepts**: Debugging, scientific method, mental models, tools (printf, debugger, REPL, strace, profiler)
- **Next Concepts**: Phase 1 synthesis, integration, next steps
- **Plant Lens**: Diagnosing ailments, observing symptoms, testing hypotheses, treating causes
- **Wisdom Tradition**: 🌙 Islamic (Ibn al-Haytham's scientific method applied to debugging!)



---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*