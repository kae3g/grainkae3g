# kae3g 9504: What Is Clojure? A Practical Lisp for the JVM

**Phase 1: Foundations & Philosophy** | **Week 1** | **Reading Time: 16 minutes**

---

## What You'll Learn

- Clojure: A modern Lisp bringing functional programming to mainstream platforms
- Why "code as data" (homoiconicity) is powerful
- Immutable data structures and why they matter
- The REPL workflow: interactive development
- How Clojure relates to Java, JavaScript, and the Lisp family
- Rich Hickey's design philosophy: "Simple Made Easy"
- Practical code examples you can run today

---

## Prerequisites

- **[9500: What Is a Computer?](/12025-10/9500-what-is-a-computer)** - Computational foundations
- **[9503: What Is Nock?](/12025-10/9503-what-is-nock)** - Minimal computation (for contrast)

Optional:
- **[9502: Ode to Nocturnal Time](/12025-10/9502-ode-to-nocturnal-time)** - Many Clojure breakthroughs happen at 2 AM!

---

## The Pragmatic Lisp

**Nock** (Essay 9503) showed us **radical minimalism**: 12 rules, eternal spec, auditable simplicity.

**Clojure** shows us **pragmatic minimalism**: Simple philosophy + practical platform + real-world adoption.

> **Clojure** (2007) by Rich Hickey: A Lisp for the JVM that embraces hosted platforms, immutability, and simplicity.

**The balance**:
- **Simple** like Lisp (data-oriented, functional)
- **Practical** like Java (mature platform, huge ecosystem)
- **Modern** (designed for concurrency, immutability-first)

---

## Lisp: Code as Data (Homoiconicity)

**The central insight** of Lisp (1958, John McCarthy):

> **Code is data. Data is code. They're the same thing.**

### In Most Languages

**Code**:
```python
def add(x, y):
    return x + y
```

**Data**:
```python
[1, 2, 3]
```

Code and data are **different**. You can't easily manipulate code as data.

### In Lisp (and Clojure)

**Code**:
```clojure
(defn add [x y]
  (+ x y))
```

**Data**:
```clojure
[1 2 3]
```

Both are **S-expressions** (symbolic expressions)—lists of atoms and nested lists.

**The code IS a list**: `(defn add [x y] (+ x y))`

You can:
- Build code at runtime (as data)
- Transform code (like any data structure)
- Execute data as code (eval)
- Write code that writes code (macros)

**This is homoiconicity**: The language's syntax is **its own data structure**.

---

## Clojure in 5 Minutes

### 1. Everything Is an Expression

```clojure
;; Numbers
42                    ; => 42

;; Strings
"hello valley"        ; => "hello valley"

;; Keywords (like symbols/atoms in other languages)
:name                 ; => :name

;; Lists (code!)
(+ 1 2)              ; => 3

;; Vectors (ordered collections)
[1 2 3]              ; => [1 2 3]

;; Maps (key-value pairs)
{:name "Clojure"
 :year 2007
 :paradigm :functional}  ; => {:name "Clojure", :year 2007, :paradigm :functional}

;; Sets
#{1 2 3}             ; => #{1 3 2} (unordered)
```

**Everything evaluates to something.** No statements—only expressions.

### 2. Functions

```clojure
;; Define a function
(defn greet [name]
  (str "Hello, " name "!"))

;; Call it
(greet "Valley Builder")  ; => "Hello, Valley Builder!"

;; Functions are first-class (pass them around)
(map greet ["Alice" "Bob" "Carol"])
; => ("Hello, Alice!" "Hello, Bob!" "Hello, Carol!")
```

**Prefix notation** (the Lisp way):
- Math: `(+ 1 2 3)` not `1 + 2 + 3`
- Calls: `(func arg1 arg2)` not `func(arg1, arg2)`

**Why?** Consistency. Everything is `(operator operands...)`. No special cases.

### 3. Immutability

```clojure
;; Create a vector
(def v [1 2 3])

;; "Add" an element (creates NEW vector, old unchanged)
(conj v 4)           ; => [1 2 3 4]

;; Original is unchanged!
v                    ; => [1 2 3]

;; To "update", rebind the name
(def v (conj v 4))
v                    ; => [1 2 3 4]
```

**Values never change.** You create **new values** instead.

**Why this matters**:
- **No spooky action at a distance** (calling a function can't break your data)
- **Thread-safe by default** (immutable data = no race conditions)
- **Time travel debugging** (keep old values, inspect past states)
- **Easier reasoning** (value is always what it was)

### 4. The REPL (Read-Eval-Print Loop)

```clojure
;; In a Clojure REPL:

user=> (+ 1 2)
3

user=> (defn factorial [n]
         (if (<= n 1)
           1
           (* n (factorial (dec n)))))
#'user/factorial

user=> (factorial 5)
120

user=> (map factorial [1 2 3 4 5])
(1 2 6 24 120)
```

**The REPL is interactive programming**:
1. Write an expression
2. Evaluate it immediately
3. See the result
4. Refine and iterate

**No compile-wait-run cycle.** Instant feedback. **This changes how you think.**

### 5. Host Interop (JVM)

```clojure
;; Call Java from Clojure
(import java.time.LocalDateTime)

(def now (LocalDateTime/now))
; => #object[java.time.LocalDateTime "2025-10-10T23:47:13.123"]

(.toString now)
; => "2025-10-10T23:47:13.123"

;; All of Java's libraries available!
```

**Clojure runs on the JVM**, giving you:
- **Mature platform** (30 years of optimization)
- **Vast ecosystem** (every Java library is usable)
- **Production-ready** (battle-tested in finance, web, data)

**ClojureScript** is the same language compiled to **JavaScript** (runs in browsers, Node.js).

---

## Immutability: The Superpower

**Most languages**: Variables **vary**.

```javascript
let x = 10;
x = x + 1;  // x is now 11 (mutated)
```

**Clojure**: Values are **immutable**.

```clojure
(def x 10)
(def x (+ x 1))  ; Creates NEW binding, doesn't mutate
```

**Wait, how do you build programs without mutation?**

### Example: Updating a Map

**Mutable (JavaScript)**:
```javascript
let user = {name: "Alice", age: 30};
user.age = 31;  // Mutated in place
```

**Immutable (Clojure)**:
```clojure
(def user {:name "Alice" :age 30})
(def updated-user (assoc user :age 31))

user          ; => {:name "Alice", :age 30}  (unchanged!)
updated-user  ; => {:name "Alice", :age 31}  (new value)
```

**"But isn't that inefficient? Copying the whole map?"**

**No!** Clojure uses **persistent data structures** (structural sharing):

```
Original:  {:name "Alice" :age 30}
                    ↓ (shares structure)
Updated:   {:name "Alice" :age 31}

Only the CHANGED part is new. The rest is shared.
Performance: O(log n) updates, not O(n) copying!
```

**This is engineering brilliance**: Immutability **with** performance.

---

## The REPL-Driven Development

**Traditional workflow**:
```
1. Write code
2. Compile
3. Run
4. See output
5. Edit code
6. Recompile (wait...)
7. Run again
8. Repeat
```

**Clojure workflow**:
```
1. Start REPL
2. Write function
3. Eval it (instant!)
4. Test it (in same REPL)
5. Refine it (eval again)
6. Build entire program incrementally
7. REPL session IS your development
```

**No compile-wait cycle.** Changes are **instant**.

### Example Session

```clojure
;; Start with a sketch
user=> (defn greet [name]
         (str "Hi " name))

user=> (greet "Alice")
"Hi Alice"

;; Refine (add punctuation)
user=> (defn greet [name]
         (str "Hi, " name "!"))

user=> (greet "Alice")
"Hi, Alice!"

;; Extend (handle nil)
user=> (defn greet [name]
         (if name
           (str "Hi, " name "!")
           "Hi, stranger!"))

user=> (greet nil)
"Hi, stranger!"

;; Perfect! Now save to file.
```

**You built the function interactively**, testing each iteration immediately.

**This is how Lisp programmers have worked since 1958.** Clojure brings it to modern platforms.

---

## Rich Hickey's Philosophy: "Simple Made Easy"

**Rich Hickey** (Clojure's creator) gave a seminal talk: ["Simple Made Easy"](https://www.infoq.com/presentations/Simple-Made-Easy/) (2011).

**Core distinction**:

**Simple** (from *simplex* - "one fold/braid"):
- **One role, one task, one concept**
- Not compound, not intertwined
- Objectively measurable

**Easy** (from *adjacent* - "near at hand"):
- **Familiar, convenient**
- Subjectively experienced
- Can be simple OR complex

**Example**:

| Tool | Simple? | Easy? |
|------|---------|-------|
| `make` | No (complects building + dependency tracking + shell scripting) | Yes (familiar to Unix users) |
| Nix | Yes (pure functions, no hidden state) | No (unfamiliar paradigm) |
| Maven | No (XML config + build lifecycle + plugins all tangled) | Yes (lots of tutorials) |

**Hickey's argument**: We confuse "easy" (familiar) with "simple" (not intertwined).

**Clojure prioritizes SIMPLE**:
- Immutable by default (not intertwined with time)
- Functions without side effects (not intertwined with global state)
- Data literals (not intertwined with classes/objects)
- Hosted (not intertwined with its own runtime—uses JVM/JS)

---

## Clojure's Core Principles

### 1. Immutability

**Already covered**, but worth repeating: **Values don't change.**

**Benefits**:
- **Fearless concurrency** (immutable data = no locks needed)
- **Easier debugging** (values don't change under you)
- **Time-travel** (keep old versions, inspect history)

### 2. First-Class Functions

**Functions are values** (can be passed, returned, stored):

```clojure
;; Function as argument
(defn apply-twice [f x]
  (f (f x)))

(apply-twice inc 5)  ; => 7  (inc twice: 5 → 6 → 7)

;; Function as return value
(defn make-adder [n]
  (fn [x] (+ x n)))

(def add10 (make-adder 10))
(add10 5)  ; => 15
```

**Higher-order functions** enable powerful abstractions (map, filter, reduce).

### 3. Code as Data (Homoiconicity)

**Clojure code is Clojure data**:

```clojure
;; This is code:
(+ 1 2)

;; This is also data (a list):
'(+ 1 2)  ; Quote prevents evaluation

;; Manipulate code as data:
(first '(+ 1 2))   ; => +
(rest '(+ 1 2))    ; => (1 2)

;; Build code at runtime:
(def code-to-run '(+ 1 2))
(eval code-to-run)  ; => 3
```

**Macros** exploit this:

```clojure
;; Macro: transform code before evaluation
(defmacro when-positive [n & body]
  `(if (pos? ~n)
     (do ~@body)))

;; Expands to:
(when-positive 5
  (println "positive!")
  (println "doing work"))

; Becomes:
(if (pos? 5)
  (do
    (println "positive!")
    (println "doing work")))
```

**Macros are code that writes code.** This is **metaprogramming** at the language level.

### 4. Hosted on the JVM (and JavaScript)

**Clojure doesn't fight its platform**—it embraces it:

```clojure
;; Use Java libraries seamlessly
(import java.util.Date)
(def now (Date.))

;; Use Clojure idioms on Java objects
(.getTime now)  ; => 1728604033123

;; Mix freely
(defn days-until [target-date]
  (let [now (Date.)
        diff (- (.getTime target-date) (.getTime now))]
    (/ diff (* 1000 60 60 24))))
```

**ClojureScript** does the same for JavaScript:

```clojure
;; ClojureScript in browser
(ns my-app.core
  (:require [reagent.core :as r]))

(defn hello []
  [:div "Hello from ClojureScript!"])

(r/render [hello] (.getElementById js/document "app"))
```

**Benefits of hosting**:
- **Mature platform** (JVM is 30 years old, battle-tested)
- **Vast ecosystem** (Java has millions of libraries)
- **Production-ready** (companies trust the JVM)
- **Cross-platform** (JVM runs everywhere)

**Trade-off**: You're dependent on the host (JVM startup time, JavaScript quirks). But Clojure's philosophy: **embrace the platform, don't fight it**.

### 5. Interactive Development (The REPL)

**The REPL is central** to Clojure workflow:

```clojure
;; Connected to running application
user=> (def users (atom []))

user=> (swap! users conj {:name "Alice"})
[{:name "Alice"}]

;; Add validation
user=> (defn add-user [db user]
         (if (:name user)
           (conj db user)
           db))

user=> (swap! users add-user {:name "Bob"})
[{:name "Alice"} {:name "Bob"}]

;; Test edge cases
user=> (swap! users add-user {})  ; No name
[{:name "Alice"} {:name "Bob"}]  ; Unchanged (validation worked)
```

**You're testing against LIVE data** in your running app. Instant feedback. **This changes everything.**

---

## Clojure vs Other Lisps

### Common Lisp (1984)

**Pros**: Mature, powerful, standardized  
**Cons**: Complex (CLOS object system, 1,000-page spec), isolated ecosystem

**Clojure's improvement**: Simpler (no CLOS), hosted (JVM ecosystem)

### Scheme (1975)

**Pros**: Minimal, elegant, educational  
**Cons**: Too minimal for production, fragmented implementations

**Clojure's improvement**: More practical (richer core library), unified (one canonical implementation)

### Racket (1995, Scheme descendant)

**Pros**: Excellent for education, rich IDE support  
**Cons**: Smaller ecosystem, less production use

**Clojure's improvement**: Production-focused, huge ecosystem (via JVM)

### Emacs Lisp

**Pros**: Scriptable editor, huge community  
**Cons**: Single-threaded, old design, Emacs-specific

**Clojure's improvement**: Modern (concurrency-first), general-purpose, multiplatform

**Summary**: Clojure is **Lisp for 2007+**—learning from decades of Lisp experience, optimized for real-world use.

---

## Practical Examples

### Example 1: Transforming Data

**Problem**: Given a list of users, extract names.

**JavaScript**:
```javascript
const users = [{name: "Alice", age: 30}, {name: "Bob", age: 25}];
const names = users.map(u => u.name);
// => ["Alice", "Bob"]
```

**Clojure**:
```clojure
(def users [{:name "Alice" :age 30} {:name "Bob" :age 25}])
(map :name users)
; => ("Alice" "Bob")
```

**Notice**: `:name` is a **function** that gets the `:name` key from a map. Keywords are functions!

### Example 2: Filtering

**Problem**: Find users over 25.

**Clojure**:
```clojure
(filter #(> (:age %) 25) users)
; => ({:name "Alice", :age 30})

;; Or more readable:
(filter (fn [user] (> (:age user) 25)) users)
```

`#(...)` is shorthand for anonymous functions: `#(> (:age %) 25)` = `(fn [x] (> (:age x) 25))`

### Example 3: Threading (Composition)

**Problem**: Transform data through multiple steps.

**Nested (hard to read)**:
```clojure
(reduce + (map :age (filter #(> (:age %) 25) users)))
; Read inside-out: filter, then map, then reduce
```

**Threaded (clear pipeline)**:
```clojure
(->> users
     (filter #(> (:age %) 25))
     (map :age)
     (reduce +))
; Read top-to-bottom: filter, then map, then reduce
```

**The `->>` macro** threads each result as **last argument** to next function.

**Result**: Same computation, but reads like **a pipeline** (water flowing through stages).

### Example 4: Building a Web Server

**Clojure + Ring (web library)**:

```clojure
(ns my-server.core
  (:require [ring.adapter.jetty :as jetty]))

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Hello from Clojure!"})

(defn -main []
  (jetty/run-jetty handler {:port 3000}))

;; Run: (main)
;; Visit: http://localhost:3000
```

**That's it.** A function that takes a **request** (map) and returns a **response** (map).

**No framework ceremony.** Just **data in, data out.**

---

## Simple Made Easy: The Design Philosophy

**Rich Hickey's core thesis**:

> "We should aim for simplicity, because simplicity is a prerequisite for reliability." - Dijkstra

**How Clojure embodies simplicity**:

### 1. Separate Identity and State

**Complex**: Object-oriented (identity and state bundled together)

```java
// Java: object has mutable state
User user = new User("Alice");
user.setAge(31);  // Mutated—identity same, state changed
```

**Simple**: Clojure separates them

```clojure
;; Values (state) are immutable
(def alice-v1 {:name "Alice" :age 30})
(def alice-v2 (assoc alice-v1 :age 31))

;; Identity (if needed) managed explicitly
(def alice (atom {:name "Alice" :age 30}))
(swap! alice assoc :age 31)
```

**Benefit**: You **choose** when to conflate identity with state. Not forced.

### 2. No Hidden Coupling

**Complex**: Global state, singletons, implicit dependencies

**Simple**: Explicit arguments, pure functions

```clojure
;; Bad (implicit dependency on global)
(def users (atom []))

(defn add-user [name]
  (swap! users conj {:name name}))  ; Mutates global—spooky!

;; Good (explicit dependency)
(defn add-user [db name]
  (conj db {:name name}))  ; Pure function—returns new db

;; Call site makes dependency visible
(swap! users add-user "Alice")
```

### 3. Data Orientation

**Complex**: Objects with methods, inheritance hierarchies

**Simple**: Plain data with functions

```clojure
;; No classes, just data
(def user {:name "Alice" :age 30 :role :admin})

;; Functions on data
(defn admin? [user]
  (= (:role user) :admin))

(admin? user)  ; => true
```

**Benefits**:
- **Serialization**: Easy (it's already data—just write it)
- **Testing**: Easy (no mock objects—just pass data)
- **Inspection**: Easy (it's just maps and vectors—print them)

---

## Concurrency: The Atom Model

**Clojure's approach to mutable state** (when you actually need it):

### Atoms (Synchronous, Independent)

```clojure
;; Create mutable reference
(def counter (atom 0))

;; Read it
@counter  ; => 0

;; Update it (atomically)
(swap! counter inc)  ; => 1
(swap! counter inc)  ; => 2

;; Even with 10 threads calling inc simultaneously,
;; you get correct count (no race conditions!)
```

**How?** `swap!` uses **compare-and-swap** (CAS) at hardware level. Retries if another thread changed the value. **Lock-free concurrency.**

### Refs (Coordinated Transactions)

```clojure
;; Transfer money between accounts (must be atomic)
(def alice-account (ref 100))
(def bob-account (ref 50))

(defn transfer [from to amount]
  (dosync
    (alter from - amount)
    (alter to + amount)))

(transfer alice-account bob-account 20)

@alice-account  ; => 80
@bob-account    ; => 70
```

**`dosync`** creates a **transaction**. Either both refs change, or neither does. **STM (Software Transactional Memory)**.

**No locks, no deadlocks, no race conditions.** The runtime handles it.

---

## Clojure in Production

**Who uses Clojure?**

- **Nubank**: Brazil's largest fintech (1,000+ Clojure developers)
- **Walmart**: Product search, pricing engines
- **Netflix**: Some internal tools
- **CircleCI**: Continuous integration platform
- **Funding Circle**: Peer-to-peer lending
- **Puppet Labs**: Infrastructure automation

**Why they chose Clojure**:
- **Concurrency** (handle thousands of requests without locks)
- **Simplicity** (easier to reason about, fewer bugs)
- **JVM** (mature platform, ops teams know it)
- **REPL** (debug production issues live!)
- **Immutability** (easier testing, reproducing bugs)

**It's not just a hobby language.** It's **production-proven** at scale.

---

## Hands-On: Your First Clojure

### Exercise 1: Install Clojure

**macOS/Linux**:
```bash
# Install via brew (macOS)
brew install clojure/tools/clojure

# Or via install script (Linux)
curl -L -O https://github.com/clojure/brew-install/releases/latest/download/linux-install.sh
chmod +x linux-install.sh
sudo ./linux-install.sh
```

**Verify**:
```bash
clj
# Should start a REPL!
```

### Exercise 2: REPL Exploration

**In the REPL**, try:

```clojure
;; Basic math
(+ 1 2 3 4 5)

;; Define a function
(defn square [x] (* x x))
(square 7)

;; Higher-order function
(map square [1 2 3 4 5])

;; Build a data structure
(def user {:name "You" :learning :clojure})

;; Transform it
(assoc user :excited? true)

;; Quit
(System/exit 0)
```

**Notice**: Instant feedback. No compile step. **This is the Clojure way.**

---

### Exercise 3: Compare to a Language You Know

**If you know JavaScript**:
```javascript
// JS
const arr = [1, 2, 3];
const doubled = arr.map(x => x * 2);
```

```clojure
;; Clojure
(def arr [1 2 3])
(def doubled (map #(* % 2) arr))
```

**Similar!** But Clojure:
- Is **immutable** by default (JS arrays are mutable)
- Uses **prefix** notation `(* % 2)` instead of `x * 2`
- Returns **lazy sequences** (won't compute until needed)

**If you know Python**:
```python
# Python
users = [{"name": "Alice"}, {"name": "Bob"}]
names = [u["name"] for u in users]
```

```clojure
;; Clojure
(def users [{:name "Alice"} {:name "Bob"}])
(def names (map :name users))
```

**Clojure is more concise** (keywords are functions!) and **immutable** (Python lists are mutable).

---

## Try This

### Exercise 1: Solve FizzBuzz

**Classic interview question**: Print 1-100, but:
- Multiples of 3: "Fizz"
- Multiples of 5: "Buzz"  
- Multiples of both: "FizzBuzz"

**Try in Clojure**:
```clojure
(defn fizzbuzz [n]
  (cond
    (zero? (mod n 15)) "FizzBuzz"
    (zero? (mod n 3))  "Fizz"
    (zero? (mod n 5))  "Buzz"
    :else              n))

(map fizzbuzz (range 1 101))
```

**Appreciate**: How **declarative** this is (say what, not how).

---

### Exercise 2: Immutable Update

**Task**: Update a nested map.

```clojure
(def system {:services {:web {:port 8080 :running true}
                        :db  {:port 5432 :running true}}})

;; Stop the web service (deep update)
(assoc-in system [:services :web :running] false)

;; Original unchanged!
system  ; => {:services {:web {:port 8080 :running true} ...}}
```

**`assoc-in`** updates nested structures **immutably**. Try this in Python (you'll appreciate Clojure's elegance).

---

### Exercise 3: Build a Simple CLI

```clojure
;; save as hello.clj
(ns hello.core)

(defn -main [& args]
  (println "Hello," (or (first args) "Valley Builder") "!"))

;; Run:
;; clj -M -m hello.core Alice
;; => "Hello, Alice !"
```

**That's a complete CLI program.** No boilerplate. Just functions and data.

---

## Going Deeper

### Related Essays
- **[9520: Functional Programming](/12025-10/9520-functional-programming-basics)** - Deep dive into FP concepts
- **[9530: Simplicity (Rich Hickey)](/12025-10/9530-rich-hickey-simple-made-easy)** - Full exploration of "Simple Made Easy"
- **[9610: Nix Package Management](/12025-10/9610-nix-package-manager)** - Another system prioritizing simplicity
- **Phase 4** - Full Clojure/ClojureScript deep dives (~essays 9801-9820)

### External Resources
- **[clojure.org](https://clojure.org)** - Official site, excellent docs
- **"Clojure for the Brave and True"** - Free online book (fun, accessible)
- **ClojureDocs** - Community-driven examples
- **Rich Hickey talks** - "Simple Made Easy", "The Value of Values", "Are We There Yet?"
- **4Clojure** - Practice problems (learn by doing)

### For the Narrative-Inclined
- **[9949: The Wise Elders Meet](/12025-10/9949-intro-clojure-nix-ecosystem)** - Clojure as character (the Functional Sage)

---

## Reflection Questions

1. **Why does immutability prevent bugs?** (If values can't change, whole classes of errors disappear)

2. **Is prefix notation really better?** (Consistent vs special cases—what's the trade-off?)

3. **Could you REPL-drive your current work?** (What would change if you had instant feedback?)

4. **What does "simple" mean to you?** (Not easy, not familiar—but not intertwined)

5. **Is Clojure "too weird" or "refreshingly different"?** (Depends on your openness to new paradigms)

---

## Summary

**Clojure is**:
- A **modern Lisp** (2007) for the JVM and JavaScript
- **Functional-first** (immutability, pure functions, higher-order functions)
- **Homoiconic** (code is data—enables macros, metaprogramming)
- **Practical** (hosted on mature platforms, production-ready)
- **REPL-driven** (interactive development with instant feedback)
- **Simple** (prioritizes decomplecting over familiarity)

**Key Insights**:
- **Immutability** = fearless concurrency + easier reasoning
- **Code as data** = powerful metaprogramming (macros!)
- **Hosted approach** = leverage existing ecosystems (JVM, JavaScript)
- **REPL workflow** = interactive development changes how you think
- **Simple ≠ easy** (unfamiliar can be simple, familiar can be complex)

**Rich Hickey's Gift**:
- Brought **Lisp philosophy** to mainstream platforms
- Proved **immutability** is practical (not just academic)
- Showed **simplicity** beats complexity (in the long run)

**In the Valley**:
- **Clojure is a primary language** (see our build scripts in `scripts/*.clj`)
- We use it for **pipelines, parsing, validation, generation**
- We'll use **ClojureScript** for browser interactivity (planned)
- The **Functional Sage** (Essay 9949) represents Clojure's wisdom

---

**Next**: We'll explore the **Unix Philosophy**—principles that shaped computing for 50 years, and still guide us today. Clojure embodies many of these principles ("do one thing well" = simple functions!).

---

**Navigation**:  
← Previous: [9503 (what is nock)](/12025-10/9503-what-is-nock) | **Phase 1 Index** | Next: [9505 (house of wisdom knowledge gardens)](/12025-10/9505-house-of-wisdom-knowledge-gardens)

**Bridge to Narrative**: For the story of Clojure, see [9949 (The Wise Elders)](/12025-10/9949-intro-clojure-nix-ecosystem)!

**Metadata**:
- **Phase**: 1 (Foundations)
- **Week**: 1
- **Prerequisites**: 9500, 9503
- **Concepts**: Lisp, functional programming, immutability, REPL, homoiconicity, macros, hosted languages
- **Next Concepts**: Unix philosophy, composition, pipes, do one thing well
- **Code Examples**: All runnable in Clojure REPL



---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*