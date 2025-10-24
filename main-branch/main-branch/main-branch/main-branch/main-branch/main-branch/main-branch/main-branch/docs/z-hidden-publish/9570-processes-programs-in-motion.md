# kae3g 9570: Processes - Programs in Motion

**Phase 1: Foundations & Philosophy** | **Week 3** | **Reading Time: 15 minutes**

---

## What You'll Learn

- What processes are (programs + runtime state)
- How your OS manages hundreds of processes simultaneously
- Process lifecycle: creation, execution, termination
- Signals: How to communicate with running processes
- Process trees: Parent-child relationships
- Practical commands: `ps`, `top`, `htop`, `kill`, `pgrep`
- Why processes are the "living cells" of your computer

---

## Prerequisites

- **[9500: What Is a Computer?](/12025-10/9500-what-is-a-computer)** - CPU, memory, execution
- **[9560: Text Files](/12025-10/9560-text-files-universal-format)** - Source code (text) becomes processes

---

## From Text to Life

**Journey of a program**:

```
1. Source code (text file)
   ↓
2. Compilation (if needed)
   ↓
3. Executable (binary file on disk)
   ↓
4. Process (loaded into memory, executing)
```

**Example**:

```clojure
;; hello.clj (text file - dormant)
(println "Hello, Valley!")
```

**Run it**:
```bash
clojure hello.clj
# Now it's a PROCESS (running in memory)
```

**Plant lens**: **"Source code is the seed (dormant). Process is the plant (growing, alive)."**

---

## What Is a Process?

**A process is**:
- **Program code** (instructions from executable)
- **Memory** (data the program uses)
- **CPU time** (when the CPU executes its instructions)
- **File handles** (open files, network connections)
- **Process ID (PID)** (unique identifier)

**It's a program IN MOTION** (not just sitting on disk, but running).

**Example**:

```bash
# List running processes
ps aux

# Output (simplified):
# USER   PID  %CPU %MEM COMMAND
# alice  1234 2.0  1.5  /usr/bin/python3 server.py
# alice  5678 0.5  0.3  clojure app.clj
# bob    9101 5.0  3.2  /Applications/Chrome.app/Contents/MacOS/Google Chrome
```

**Each line is a process** (a running program).

---

## Process Anatomy

**Key components**:

### 1. Process ID (PID)

**Every process** has a unique number:

```bash
# Find PID of a process
pgrep firefox
# Output: 12345

# Or:
ps aux | grep firefox
```

**PID 1** is special (the init system - first process on boot).

### 2. Parent Process (PPID)

**Most processes have a parent** (the process that created them):

```bash
# See process tree
pstree
# Or (macOS):
ps auxf  # Shows hierarchy
```

**Example**:
```
bash (PID 1000)
  └─ python3 script.py (PID 2000, PPID 1000)
      └─ sh -c "echo hello" (PID 3000, PPID 2000)
```

**Plant lens**: **"Parent process is the stem, child processes are branches."**

### 3. Memory

**Each process** has its own memory space:
- **Code** (program instructions)
- **Data** (global variables)
- **Heap** (dynamically allocated memory)
- **Stack** (function call frames, local variables)

**Processes are isolated** (one process can't access another's memory directly—security!).

### 4. File Descriptors

**Open files, sockets, pipes**:

```bash
# List open files for a process
lsof -p 12345

# Standard descriptors:
# 0 = stdin (input)
# 1 = stdout (output)
# 2 = stderr (errors)
```

**Example**:
```bash
# Redirect stdout to file
python3 script.py > output.txt
# (stdout descriptor points to output.txt instead of terminal)
```

---

## Process Lifecycle

### 1. Creation (Fork/Exec)

**On Unix**, processes are created via `fork()` + `exec()`:

**Fork**: Create a copy of current process
```c
pid_t child_pid = fork();
// Now TWO processes (parent + child)
```

**Exec**: Replace current process with new program
```c
execve("/bin/ls", args, env);
// Current process is now "ls"
```

**Together**:
```bash
# When you run "ls" in bash:
# 1. bash calls fork() (creates bash copy)
# 2. Child calls exec("/bin/ls") (becomes ls)
# 3. Parent waits for child to finish
```

**Plant lens**: **"Fork is like a cutting (clone parent plant), exec is grafting (different plant on same root)."**

### 2. Execution (Running)

**Process states**:

- **Running** (currently on CPU)
- **Runnable** (ready to run, waiting for CPU)
- **Sleeping** (waiting for I/O, timer, signal)
- **Stopped** (paused, e.g., by Ctrl-Z)
- **Zombie** (finished, but parent hasn't acknowledged yet)

**Check state**:
```bash
ps aux
# Look at STAT column:
# R = Running
# S = Sleeping (interruptible)
# D = Sleeping (uninterruptible, e.g., waiting for disk)
# T = Stopped
# Z = Zombie
```

### 3. Termination (Exit)

**Process ends** when:
- Program calls `exit()` (normal termination)
- Process receives a signal (e.g., `SIGKILL`)
- Unhandled error (crash)

**Exit code**:
```bash
# Run a command
ls /nonexistent

# Check exit code
echo $?
# Output: 2 (error!)

# Success:
ls /
echo $?
# Output: 0 (success)
```

**Convention**: `0` = success, non-zero = error.

---

## Signals: Talking to Processes

**Signals** are messages sent to processes:

**Common signals**:
- **SIGINT** (2): Interrupt (Ctrl-C in terminal)
- **SIGTERM** (15): Terminate gracefully (default for `kill`)
- **SIGKILL** (9): Kill immediately (can't be caught or ignored!)
- **SIGSTOP** (19): Pause process (Ctrl-Z)
- **SIGCONT** (18): Resume process
- **SIGHUP** (1): Hangup (terminal closed)

**Send signal**:
```bash
# Graceful termination
kill -TERM 12345

# Force kill
kill -9 12345

# Pause process
kill -STOP 12345

# Resume it
kill -CONT 12345
```

**Practical example**:
```bash
# Start long-running process
python3 server.py
# (Ctrl-C to stop - sends SIGINT)

# Or run in background:
python3 server.py &
# Get its PID
pgrep -f server.py
# Kill it later
kill $(pgrep -f server.py)
```

---

## Process Inspection

### `ps` - Process Status

**List all processes**:
```bash
ps aux
# a = all users
# u = user-oriented format
# x = include processes without terminal
```

**Filter**:
```bash
# My processes
ps u

# Specific user
ps -u alice

# Process tree (hierarchy)
ps auxf  # Linux
ps aux | grep <pattern>  # macOS
```

### `top` / `htop` - Interactive Monitor

**Top**:
```bash
top
# Shows:
# - PID, USER, %CPU, %MEM, COMMAND
# - Sorted by CPU usage (by default)
# - Updates every few seconds

# Useful keys:
# q = quit
# M = sort by memory
# P = sort by CPU
# k = kill a process (enter PID)
```

**htop** (better interface):
```bash
htop
# (Install with: brew install htop)
# Visual bars for CPU/memory
# Tree view (F5)
# Kill process (F9)
```

### `pgrep` / `pkill` - Find/Kill by Name

```bash
# Find PID by name
pgrep firefox
# Output: 12345 67890

# Find with full command line
pgrep -f "python3 server.py"

# Kill by name
pkill firefox

# Kill with signal
pkill -TERM firefox
```

---

## Process Trees

**Every process** (except PID 1) has a parent.

**Example hierarchy**:

```
systemd (PID 1)
  ├─ sshd (PID 1000)
  │   └─ sshd (PID 2000, session for alice)
  │       └─ bash (PID 2100)
  │           ├─ vim (PID 2200)
  │           └─ python3 (PID 2300)
  ├─ cron (PID 3000)
  └─ nginx (PID 4000)
      ├─ nginx worker (PID 4001)
      └─ nginx worker (PID 4002)
```

**View tree**:
```bash
# Linux
pstree

# macOS (no pstree by default)
ps auxf  # Or install pstree via brew
```

**Why trees matter**:
- **Signals propagate** (kill parent → children may also die)
- **Resource limits** can apply to entire subtree
- **Understand relationships** (which process spawned which)

---

## Practical: Process Management

### Example 1: Long-Running Script

```bash
# Run script (blocks terminal)
python3 long_task.py

# Better: Run in background
python3 long_task.py &
# (Returns immediately, prints PID)

# Check it's running
pgrep -f long_task

# Bring to foreground
fg

# Send to background again (after Ctrl-Z to pause)
bg
```

### Example 2: Kill Unresponsive App

```bash
# Find PID
pgrep -f "MyApp"
# Or:
ps aux | grep MyApp

# Try graceful kill
kill -TERM <PID>

# Wait 5 seconds...

# Force kill if still running
kill -9 <PID>
```

### Example 3: Monitor Resource Usage

```bash
# Watch resource usage live
htop

# Or for specific process:
top -pid <PID>

# Get snapshot
ps aux --sort=-%mem | head -10  # Top 10 memory users (Linux)
ps aux -m | head -10             # macOS
```

---

## Zombies and Orphans

### Zombie Processes

**When a process exits**, it becomes a zombie until its parent calls `wait()`:

```bash
ps aux | grep Z
# Look for STAT = Z

# Example:
# alice  12345  0.0  0.0  0  0  ?  Z  10:00  [defunct]
```

**Why**: Parent needs to read exit status (then zombie is reaped).

**Usually harmless** (takes no resources), but many zombies indicate a bug (parent not calling `wait()`).

**Fix**: Kill the parent (or fix its code).

### Orphan Processes

**When a parent dies** before its child:
- Child becomes orphan
- Gets **re-parented** to PID 1 (init/systemd)

**Example**:
```bash
bash (PID 1000)
  └─ python3 (PID 2000)

# Kill bash:
kill 1000

# Now:
systemd (PID 1)
  └─ python3 (PID 2000, re-parented)
```

**Plant lens**: **"Orphaned process is adopted by the root (PID 1 as the main trunk)."**

---

## Concurrency: Many Processes at Once

**Your computer runs** 100s of processes simultaneously:

```bash
ps aux | wc -l
# Output: 237 processes (example)
```

**How?** **Time-slicing** (CPU switches between processes rapidly):

```
Time:   0ms    10ms   20ms   30ms   40ms   50ms
CPU:    A      B      C      A      B      D
# Each process gets a turn (scheduler decides)
```

**You don't notice** (switching is so fast, feels simultaneous).

**This is multitasking** (essential for modern OS).

---

## Try This

### Exercise 1: Explore Your Processes

```bash
# How many processes?
ps aux | wc -l

# Top CPU users
ps aux --sort=-%cpu | head -10  # Linux
ps aux | sort -nrk 3 | head -10  # macOS

# Top memory users
ps aux --sort=-%mem | head -10  # Linux
ps aux | sort -nrk 4 | head -10  # macOS

# What are they? Recognize them?
```

---

### Exercise 2: Process Lifecycle

```bash
# Start a long-running process
sleep 60 &
# (Prints PID, e.g., 12345)

# Verify it's running
ps aux | grep sleep
# Or:
pgrep sleep

# Wait 10 seconds...

# Kill it
kill $(pgrep sleep)

# Verify it's gone
pgrep sleep
# (No output = process dead)
```

---

### Exercise 3: Signals

```bash
# Start interactive process
python3
# (Python REPL)

# In another terminal:
pgrep -f python3
# (Get PID, e.g., 12345)

# Send SIGSTOP (pause)
kill -STOP 12345

# Try typing in Python REPL - frozen!

# Resume it
kill -CONT 12345

# Python REPL works again!
```

---

## Going Deeper

### Related Essays
- **[9500: What Is a Computer?](/12025-10/9500-what-is-a-computer)** - CPU executes processes
- **[9560: Text Files](/12025-10/9560-text-files-universal-format)** - Source code → process
- **[9550: Command Line](/12025-10/9550-command-line-your-primary-interface)** - Tools for managing processes
- **[9951: Init Systems](/12025-10/9951-init-systems-landscape)** - PID 1, process supervision

### External Resources
- **`man ps`** - Process status command manual
- **`man kill`** - Signal command manual
- **`man 7 signal`** - Signal overview (Linux)
- **"The Linux Programming Interface"** - Processes in depth

---

## Reflection Questions

1. **Why do processes need isolation?** (Security? Stability? What if one process could write to another's memory?)

2. **Is killing with SIGKILL (9) bad?** (When is it necessary? When should you use SIGTERM instead?)

3. **How does your OS decide which process runs next?** (Scheduler algorithms - priority, fairness, interactivity)

4. **Can a process have no parent?** (Only PID 1 - everything else is descended from it)

5. **What happens if PID 1 crashes?** (Kernel panic! The whole system depends on it)

---

## Summary

**Processes are**:
- **Programs in motion** (loaded into memory, executing)
- **Isolated** (each has its own memory space)
- **Managed by OS** (scheduler, signals, lifecycle)
- **Organized in trees** (parent-child relationships)

**Key Concepts**:
- **PID**: Unique process identifier
- **States**: Running, sleeping, stopped, zombie
- **Signals**: Messages to processes (SIGINT, SIGTERM, SIGKILL, etc.)
- **Fork/Exec**: How processes are created
- **Exit codes**: 0 = success, non-zero = error

**Practical Commands**:
- **`ps aux`**: List all processes
- **`top`/`htop`**: Interactive monitor
- **`pgrep <name>`**: Find PID by name
- **`kill <PID>`**: Send signal to process
- **`pkill <name>`**: Kill by name

**In the Valley**:
- **Processes are living entities** (seeds → plants)
- **We manage them carefully** (graceful shutdown, not brute force)
- **We understand hierarchy** (parent-child, like branches from a trunk)
- **We monitor resources** (CPU, memory - like water, nutrients for plants)

**Plant lens**: **"Source code is the seed (potential), process is the growing plant (actualized, alive, consuming resources)."**

---

**Next**: We'll explore **memory** in depth—how processes store and access data, the difference between stack and heap, and why memory management matters!

---

**Navigation**:  
← Previous: [9560 (text files universal format)](/12025-10/9560-text-files-universal-format) | **Phase 1 Index** | Next: [9580 (memory management)](/12025-10/9580-memory-management)

**Metadata**:
- **Phase**: 1 (Foundations)
- **Week**: 3
- **Prerequisites**: 9500, 9560
- **Concepts**: Processes, PID, signals, fork/exec, process lifecycle, zombies, orphans, concurrency
- **Next Concepts**: Memory (stack, heap, virtual memory)
- **Plant Lens**: Seeds (code) → plants (processes), isolation as root zones, parent-child as branches



---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*