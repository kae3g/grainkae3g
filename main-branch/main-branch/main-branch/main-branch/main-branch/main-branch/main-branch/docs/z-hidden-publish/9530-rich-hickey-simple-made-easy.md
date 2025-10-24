# kae3g 9530: Rich Hickey's "Simple Made Easy" - A Design Philosophy

**Phase 1: Foundations & Philosophy** | **Week 2** | **Reading Time: 18 minutes**

---

## What You'll Learn

- The crucial distinction: Simple vs Easy
- Why we confuse familiarity with simplicity
- Complecting: when things are intertwined (and why that's bad)
- How to identify complexity in systems
- Practical strategies for achieving simplicity
- Why simplicity is a prerequisite for reliability
- How this philosophy guides Clojure, Nix, and valley thinking

---

## Prerequisites

- **[9504: What Is Clojure?](/12025-10/9504-what-is-clojure)** - Rich Hickey's language
- **[9510: Unix Philosophy](/12025-10/9510-unix-philosophy-do-one-thing-well)** - Do one thing well
- **[9520: Functional Programming](/12025-10/9520-functional-programming-basics)** - Pure functions, immutability

---

## The Talk That Changed How We Think

In **2011**, at Strange Loop conference, **Rich Hickey** gave a talk: ["Simple Made Easy"](https://www.infoq.com/presentations/Simple-Made-Easy/).

**Impact**:
- 1.5 million+ views (still growing)
- Referenced in thousands of tech discussions
- Changed how an entire generation thinks about design
- **Required viewing** in many engineering teams

**Why it matters**: Hickey **named** something we all felt but couldn't articulate.

Let's unpack it.

---

## The Core Distinction

### Simple (from *simplex* - "one fold")

**Definition**: Not intertwined. One role. One task. One concept. One dimension.

**Objective**: You can **measure** simplicity (count the braids, count the dependencies).

**Examples**:
- A function that adds two numbers: **simple** (one task)
- A rope with separate strands: **simple** (not braided)
- A wheel: **simple** (rotates, that's all)

**Non-examples**:
- A function that adds numbers AND logs to database AND sends email: **not simple** (three tasks braided)
- A rope braided with wire and rubber: **not simple** (intertwined)
- A Swiss Army knife: **not simple** (20 tools in one object)

### Easy (from *adjacens* - "near at hand")

**Definition**: Familiar. Near to our current understanding. Close at hand.

**Subjective**: What's easy for you might be hard for me (depends on experience).

**Examples**:
- Python: **easy** for most programmers (familiar syntax, lots of tutorials)
- Spanish: **easy** for English speakers (similar alphabet, shared vocabulary)
- Driving: **easy** after you've done it for years

**Non-examples** (for most people):
- Haskell: **not easy** (unfamiliar paradigm, weird syntax)
- Chinese: **not easy** for English speakers (different writing system, tones)
- Unicycling: **not easy** (requires practice, balance)

---

## The Confusion

**We habitually confuse easy with simple.**

**Example**: "JavaScript is simple!"

**Actually**: JavaScript is **easy** (familiar, tons of resources), but **not simple** (complex scoping rules, `this` binding, coercion rules, prototype chains, async/await + promises + callbacks...).

**Example**: "Nix is complex!"

**Actually**: Nix is **simple** (pure functions, no hidden state, deterministic), but **not easy** (unfamiliar paradigm, steep learning curve).

**The trap**:
```
"This tool is easy to get started with!" 
    ↓
"I'll use it for my project."
    ↓
"Wait, why is this so complicated now?" 
    ↓
(It was easy, not simple—complexity appears later)
```

**Better**:
```
"This tool is unfamiliar (not easy)."
    ↓
"But it's simple (not intertwined)."
    ↓
"I'll invest time to learn it."
    ↓
"Now it's both simple AND easy (to me)!"
```

**Hickey's point**: **Prefer simple over easy.** Easy is temporary (until complexity emerges). Simple is structural.

---

## Complecting: The Root of Complexity

**Complect** (from *complectere* - "to braid together"):

> **To intertwine, entwine, braid together.**

**Simple**: Separate strands (can reason about each independently).  
**Complex**: Braided strands (must understand all to understand any).

### Code Example: Complected

```python
class UserManager:
    def __init__(self):
        self.users = []  # State
        self.db = Database()  # Database
        self.logger = Logger()  # Logging
        self.emailer = Emailer()  # Email
    
    def add_user(self, name, email):
        # Complected! Four concerns braided:
        user = {"name": name, "email": email}
        self.users.append(user)  # State management
        self.db.insert(user)  # Persistence
        self.logger.log(f"Added {name}")  # Logging
        self.emailer.send(email, "Welcome!")  # Email
```

**What's complected?**
- State management + database + logging + email
- Can't test `add_user` without all four systems
- Can't replace logger without modifying UserManager
- Can't understand one without understanding all

### Code Example: Decomplected

```clojure
;; Separate concerns (loosely coupled)

(defn add-user [users user]
  (conj users user))  ; Just data transformation

(defn persist-user [db user]
  (insert db user))  ; Just persistence

(defn log-event [logger event]
  (write-log logger event))  ; Just logging

(defn send-welcome [emailer email]
  (send-email emailer email "Welcome!"))  ; Just email

;; Compose at call site:
(defn onboard-user [systems user]
  (let [users' (add-user (:users systems) user)]
    (persist-user (:db systems) user)
    (log-event (:logger systems) {:type :user-added :user user})
    (send-welcome (:emailer systems) (:email user))
    (assoc systems :users users')))
```

**What's decomplected?**
- Each function has **one** responsibility
- Can test each independently (pass mock data)
- Can replace any subsystem without changing others
- Can reason about each function in isolation

**Trade-off**: More functions (looks like more code). But each function is **simple** (easier to understand, test, modify).

---

## Constructs vs Artifacts

**Hickey distinguishes**:

### Constructs (Things we make)

**We can choose** how to construct our systems.

**Simple constructs**:
- **Values** (immutable data)
- **Functions** (inputs → output, no side effects)
- **Namespaces** (organize code by purpose)
- **Data** (maps, vectors, sets—generic structures)
- **Queues** (decouple producer from consumer)

**Complex constructs** (complected):
- **State** (intertwines value with time)
- **Objects** (intertwine data with methods)
- **Inheritance** (intertwines parent with child)
- **Syntax** (intertwines meaning with representation)
- **Loops** (intertwine iteration with operation)

### Artifacts (Things we use)

**We're stuck with** some complexity (can't eliminate):

- **Computers** (layers of abstraction, hardware quirks)
- **Networks** (latency, packet loss, partitions)
- **Users** (varied needs, edge cases)
- **Time** (things change, state must be managed)

**But**: We can **isolate** artifact complexity. Build simple constructs that **manage** complex artifacts.

**Example**:
- **Artifact**: Network is unreliable (packets drop)
- **Simple construct**: Retry logic (isolated, testable)
- **Complex approach**: Tangle retry with business logic (not testable, not reusable)

---

## Dimensions of Simplicity/Complexity

**Hickey identifies** complecting dimensions:

| Dimension | Simple | Complex (Complected) |
|-----------|--------|----------------------|
| **State** | Values (immutable) | Variables (mutation braided with logic) |
| **Order** | Queues, declarative | Imperative sequences (step 1 must happen before step 2) |
| **Time** | Functions (timeless) | Objects (state changes over time) |
| **Identity** | Explicit references | Hidden in `this` pointers |
| **Modules** | Namespaces/packages | Objects (braid structure with behavior) |
| **Logic** | Rules, pure functions | Conditionals scattered throughout code |
| **Data** | Generic structures (maps, lists) | Classes (specific to one use case) |

**Design strategy**: For each dimension, **choose the simple option** unless complexity is **essential** (rare!).

---

## Testing Simplicity

**How to know if something is simple?**

### The Questions

**1. Can you change one thing without changing another?**

```clojure
;; Simple: change validation without changing persistence
(defn validate [user] ...)  ; Independent
(defn persist [user] ...)   ; Independent

;; Complex: changing validation requires changing UserManager class
class UserManager {
  validate() { ... }
  persist() { ... }  // Tangled with validate via shared state
}
```

**2. Can you understand one part without understanding the whole?**

```bash
# Simple: understand grep without understanding sort
grep "error" | sort

# Complex: understand method A requires understanding entire class hierarchy
class.methodA()  # Calls super.methodB(), which calls this.methodC()...
```

**3. Can you test one part independently?**

```clojure
;; Simple: test each function alone
(= (add 2 3) 5)  ; No setup needed

;; Complex: test requires mocking entire system
UserManager.add_user("Alice")  
; Needs: mock DB, mock logger, mock emailer, setup state...
```

**If the answer is "no" to any**: You have complecting. Refactor to separate concerns.

---

## Achieving Simplicity

**Hickey's strategies**:

### 1. Choose Simple Constructs

**Prefer**:
- **Immutable data** over mutable objects
- **Pure functions** over methods with side effects
- **Data** over classes
- **Queues** over locks
- **Declarative** over imperative

**Example**:
```clojure
;; Simple construct: pure function
(defn calculate-tax [income]
  (* income 0.25))

;; Complex construct: stateful object
class TaxCalculator {
  private config;  // State!
  private history; // State!
  
  calculateTax(income) {
    this.history.push(income);  // Side effect!
    return income * this.config.rate;
  }
}
```

### 2. Abstract with Data

**Don't create a class for everything.** Use generic data structures:

```clojure
;; Good: use maps
(def user {:name "Alice" :age 30 :role :admin})
(def product {:name "Widget" :price 10 :stock 100})

;; Both are maps—same operations work on both!
(:name user)     ; => "Alice"
(:name product)  ; => "Widget"

;; Bad: create classes (each needs unique methods)
class User { getName() {...} getAge() {...} }
class Product { getName() {...} getPrice() {...} }
```

**Benefits of data**:
- **Generic operations** (get, assoc, dissoc work on all maps)
- **Easy serialization** (it's already data!)
- **Composable** (merge maps, nest them, transform them)

### 3. Separate Policy from Mechanism

**Mechanism**: How something works.  
**Policy**: What it should do.

**Example**:
```clojure
;; Mechanism: generic validation function
(defn validate [rules data]
  (every? (fn [[key rule]] (rule (get data key))) rules))

;; Policy: specific rules for users
(def user-rules
  {:age #(>= % 18)
   :email #(re-matches #".+@.+\..+" %)})

;; Compose:
(validate user-rules {:age 30 :email "alice@example.com"})
```

**Mechanism** (validate) is reusable. **Policy** (user-rules) is configurable.

**Not complected**: Can change policy without changing mechanism.

---

## Real-World Examples

### Example 1: Simple vs Complex Build Systems

**Complex** (Maven):
```xml
<!-- pom.xml: 200 lines of XML -->
<project>
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <source>11</source>
          <target>11</target>
        </configuration>
      </plugin>
    </plugins>
  </build>
  <dependencies>
    <!-- Another 100 lines -->
  </dependencies>
</project>
```

**What's complected?**
- Build configuration + dependency management + plugin lifecycle
- Imperative phases (validate, compile, test, package)
- XML (syntax) + Maven concepts (semantics)

**Simple** (Nix):
```nix
{ stdenv, jdk11 }:

stdenv.mkDerivation {
  name = "my-app";
  src = ./.;
  buildInputs = [ jdk11 ];
  buildPhase = "javac *.java";
}
```

**What's simple?**
- Pure function (inputs → output, deterministic)
- Declarative (say what, not how)
- Minimal DSL (just attribute sets and functions)

**Trade-off**: Nix is **not easy** (unfamiliar). But it's **simple** (not complected).

### Example 2: Simple vs Complex State Management

**Complex** (React with classes, pre-hooks):
```javascript
class Counter extends React.Component {
  constructor(props) {
    super(props);
    this.state = { count: 0 };  // State
    this.increment = this.increment.bind(this);  // Binding!
  }
  
  increment() {
    this.setState({ count: this.state.count + 1 });  // Mutation!
  }
  
  render() {
    return <button onClick={this.increment}>{this.state.count}</button>;
  }
}
```

**What's complected?**
- State + behavior + rendering
- `this` binding (implicit context)
- Lifecycle methods (componentDidMount, etc.)

**Simple** (React with hooks):
```javascript
function Counter() {
  const [count, setCount] = useState(0);
  
  return <button onClick={() => setCount(count + 1)}>{count}</button>;
}
```

**What's simple?**
- State isolated (useState hook)
- Function (no class, no `this`)
- Declarative (say what you want, not how to construct it)

**Even simpler** (Svelte):
```svelte
<script>
  let count = 0;
</script>

<button on:click={() => count += 1}>{count}</button>
```

**Simplest**: No framework ceremony. Just reactive variables.

---

## The Benefits of Simplicity

### 1. Easier to Understand

**Simple code**: Read one function, understand it.

```clojure
(defn average [numbers]
  (/ (reduce + numbers)
     (count numbers)))
```

**One concept**: Sum divided by count. Done.

**Complex code**: Read one method, must understand entire class.

```java
class Statistics {
  private List<Double> data;
  private boolean sorted = false;
  
  public double average() {
    if (!sorted) sort();  // Side effect!
    // ... more braiding ...
  }
}
```

**Intertwined**: Average depends on sorted state, which depends on data, which might be modified by other methods...

### 2. Easier to Test

**Simple**:
```clojure
(deftest test-average
  (is (= (average [1 2 3]) 2)))

;; No setup, no mocking, just call it
```

**Complex**:
```java
@Test
public void testAverage() {
  Statistics stats = new Statistics();
  stats.add(1);
  stats.add(2);
  stats.add(3);
  assertEquals(2.0, stats.average(), 0.001);
}

// Setup required, state management, potential for test pollution
```

### 3. Easier to Change

**Simple**: Change one strand, others unaffected.

```clojure
;; Change validation (doesn't affect persistence)
(defn validate [user]
  (and (>= (:age user) 18)
       (.contains (:email user) "@")))  ; Added email check

;; Persistence unchanged:
(defn persist [db user]
  (insert db user))
```

**Complex**: Change one thing, break another.

```python
class UserManager:
    def add_user(self, name, email):
        # Validation braided with persistence
        if len(name) < 3:  # Change this...
            raise ValueError
        self.db.insert({"name": name, "email": email})  # ...might break this
```

### 4. More Reliable

**Dijkstra**:
> "Simplicity is prerequisite for reliability."

**Why?**
- **Fewer interactions** = fewer edge cases
- **Isolated components** = easier to verify each
- **No hidden state** = reproducible behavior

**Real-world**: seL4 microkernel (Essay 9954) is **formally verified** because it's **simple enough** to prove correct (~10,000 lines).

Linux kernel (28 million lines): **impossible** to formally verify (too complex, too many interactions).

---

## Identifying Complexity in Your Code

### Warning Signs

**1. "To understand this, you need to know..."**

If explaining one function requires explaining five others: **complected**.

**2. "This depends on global state X"**

Global state braids all code that touches it.

**3. "You can't change this without changing that"**

Tight coupling = complecting.

**4. "The tests are really complicated"**

Tests reflect complexity. Simple code = simple tests.

**5. "I'm not sure what will happen if..."**

Intertwined behavior = unpredictable emergence.

### Refactoring Strategy

**When you find complecting**:

1. **Identify the braids**: What concerns are intertwined?
2. **Separate them**: Create functions for each concern
3. **Compose explicitly**: Make dependencies visible at call site
4. **Test independently**: Each function should be testable alone

**Example refactor**:

```python
# Before (complected)
def process_order(order):
    validate(order)  # Raises exception on error
    charge_card(order.card)  # Side effect!
    update_inventory(order.items)  # Side effect!
    send_email(order.email)  # Side effect!
    return order

# After (decomplected)
def validate_order(order):
    return errors if invalid else None

def process_order(order):
    # Make dependencies explicit, handle errors explicitly
    errors = validate_order(order)
    if errors:
        return {:status :invalid :errors errors}
    
    charge_result = charge_card(order.card)
    inventory_result = update_inventory(order.items)
    email_result = send_email(order.email)
    
    return {:status :success
            :charge charge_result
            :inventory inventory_result
            :email email_result}
```

**Now**:
- Each concern is **separate**
- Errors are **values** (not exceptions braiding control flow)
- Side effects are **explicit** (you see every one)
- Testable (mock each subsystem independently)

---

## Simple Made Easy (Over Time)

**Hickey's crucial insight**:

> **What's unfamiliar (not easy) can become familiar (easy) through learning.**
>
> **What's complex (complected) doesn't become simple through familiarity.**

**Example**:

**Year 0**: Haskell is **not easy** (unfamiliar) and **simple** (pure functions, no mutation).

**Year 1**: Haskell is **easier** (you've learned it) and still **simple**.

**vs**

**Year 0**: Java is **easy** (familiar) and **complex** (OOP, inheritance, mutable state).

**Year 1**: Java is still **easy** (familiar) but **complex** (familiarity didn't fix the braiding).

**Conclusion**: **Invest in learning simple tools.** The difficulty is temporary. The simplicity is permanent.

---

## Simplicity in the Valley

**How we apply this**:

### 1. Prefer Immutability

**Mutable state complects** value with time.

```clojure
;; Simple: values don't change
(def v1 {:count 0})
(def v2 (assoc v1 :count 1))

;; v1 and v2 coexist (no timeline complexity)
```

### 2. Pure Functions

**Side effects complect** logic with environment.

```clojure
;; Simple: no side effects
(defn calculate-total [items]
  (reduce + (map :price items)))

;; Complex: side effects braided with logic
(defn calculate-and-log-total [items]
  (let [total (reduce + (map :price items))]
    (log "Total: " total)  ; Side effect!
    total))
```

**Refactor**: Separate calculation from logging.

### 3. Data Orientation

**Objects complect** data with behavior.

```clojure
;; Simple: data + functions
(def user {:name "Alice" :age 30})
(defn adult? [user] (>= (:age user) 18))

;; Complex: data braided with methods
class User {
  private age;
  public boolean isAdult() { return age >= 18; }
}
```

### 4. Explicit Over Implicit

**Implicit dependencies complect** code with hidden context.

```clojure
;; Simple: explicit
(defn process [db config user]
  ...)  ; Dependencies visible in signature

;; Complex: implicit
(defn process [user]
  ... (use global-db) ...  ; Where did this come from?
  ... (use global-config) ...
  )
```

---

## Practical Exercises

### Exercise 1: Identify Complecting

**Review code you've written.** Find examples of:

- **State + logic** (object methods modifying object state)
- **Validation + transformation** (one function doing two jobs)
- **Error handling + business logic** (try/catch mixed with logic)
- **Configuration + execution** (reading config file inside processing function)

**For each**: How would you **separate** them?

---

### Exercise 2: Braid Count

**Take a function**. Count how many concerns it handles:

```python
def process_user(name, email):
    # 1. Validation
    if len(name) < 3:
        raise ValueError
    
    # 2. Transformation
    user = {"name": name.upper(), "email": email.lower()}
    
    # 3. Persistence
    db.insert(user)
    
    # 4. Logging
    log.info(f"Added {name}")
    
    # 5. Notification
    send_email(email, "Welcome!")
    
    return user
```

**Count**: 5 concerns (validation, transformation, persistence, logging, notification).

**Braid count**: 5.

**Target**: 1 concern per function (braid count = 1).

---

### Exercise 3: Decomplect Something

**Take a complected function** (braid count > 1).

**Refactor** to separate functions:

```clojure
(defn validate-user [user] ...)
(defn normalize-user [user] ...)
(defn persist-user [db user] ...)
(defn log-user-added [logger user] ...)
(defn send-welcome-email [emailer email] ...)

;; Compose at call site
(defn onboard-user [systems user]
  (when-let [errors (validate-user user)]
    (return {:status :error :errors errors}))
  
  (let [normalized (normalize-user user)]
    (persist-user (:db systems) normalized)
    (log-user-added (:logger systems) normalized)
    (send-welcome-email (:emailer systems) (:email normalized))
    {:status :success :user normalized}))
```

**Result**: Each concern is **isolated**. All dependencies **explicit**.

---

## The Simplicity Toolkit

**Hickey recommends** specific simple constructs:

### For State

**Don't**: Mutable variables everywhere  
**Do**: Managed references (Clojure atoms, refs)

```clojure
;; Explicit state management
(def app-state (atom {:users [] :products []}))

;; Changes are explicit
(swap! app-state update :users conj new-user)
```

### For Polymorphism

**Don't**: Inheritance hierarchies (complex!)  
**Do**: Protocols/interfaces (simple)

```clojure
;; Define interface (simple)
(defprotocol Storage
  (save [this data])
  (load [this id]))

;; Implement for different backends (not braided)
(extend-type FileStorage
  Storage
  (save [this data] ...file logic...)
  (load [this id] ...file logic...))

(extend-type DBStorage
  Storage
  (save [this data] ...db logic...)
  (load [this id] ...db logic...))
```

### For Time

**Don't**: Stateful objects changing over time  
**Do**: Values + transformations

```clojure
;; Simple: explicit versions
(def user-v1 {:name "Alice" :age 30})
(def user-v2 (assoc user-v1 :age 31))
(def user-v3 (assoc user-v2 :role :admin))

;; Can inspect all versions (time travel!)
```

---

## Try This

### Exercise 1: Watch the Talk

**[Watch "Simple Made Easy"](https://www.infoq.com/presentations/Simple-Made-Easy/)** (1 hour)

**Take notes**:
- What examples resonate with your experience?
- Where have you encountered complecting?
- What tools/practices create simplicity in your work?

### Exercise 2: Audit Your Dependencies

**List your project's dependencies.**

For each, ask:
- **Is it simple?** (Does one thing, or many?)
- **Could it be simpler?** (Smaller alternatives?)
- **Is the complexity essential?** (Or accidental?)

**Example**:
```
Dependency: lodash (utility library, 100+ functions)
- Simple? No (does many things)
- Alternative: Native JS (simpler, but less convenient)
- Complexity essential? Depends (for big apps: maybe. For small: probably not)
```

### Exercise 3: Simplicity Kata

**Write a function** to validate a user (name, email, age).

**Version 1**: All concerns in one function (complected).

**Version 2**: Separate functions for each validation rule (decomplected).

**Compare**: Which is easier to test? Which is easier to extend?

---

## Going Deeper

### Related Essays
- **[9504: Clojure](/12025-10/9504-what-is-clojure)** - Language designed for simplicity
- **[9503: Nock](/12025-10/9503-what-is-nock)** - Radical simplicity (12 rules)
- **[9610: Nix](/12025-10/9610-nix-package-manager)** - Simple build system (pure functions)
- **[9949: The Wise Elders](/12025-10/9949-intro-clojure-nix-ecosystem)** - Clojure's wisdom narrative

### External Resources
- **[Rich Hickey: "Simple Made Easy"](https://www.infoq.com/presentations/Simple-Made-Easy/)** - The original talk (watch it!)
- **["The Value of Values"](https://www.youtube.com/watch?v=-6BsiVyC1kM)** - Immutability deep dive
- **["Hammock Driven Development"](https://www.youtube.com/watch?v=f84n5oFoZBc)** - On thinking before coding
- **"Are We There Yet?"** - Time, identity, and state

### For the Philosophically Curious
- **Dijkstra**: "Simplicity is prerequisite for reliability"
- **Tony Hoare**: "There are two ways to design software: simple with obviously no bugs, or complex with no obvious bugs"
- **Alan Kay**: "Simple things should be simple, complex things should be possible"

---

## Reflection Questions

1. **What's something you thought was "simple" that's actually just "easy"?** (Familiar but complected?)

2. **Can you think of a time when "easy" won over "simple" in a decision?** (What was the long-term cost?)

3. **Is it worth learning unfamiliar (not easy) tools if they're simpler?** (Investment in understanding vs ongoing complexity cost)

4. **How do you balance "ship now" (choose easy) vs "build right" (choose simple)?** (Pragmatism vs idealism)

5. **Can you identify complecting in systems you use daily?** (Where are things braided that shouldn't be?)

---

## Summary

**Simple vs Easy**:
- **Simple** = not intertwined (objective, measurable)
- **Easy** = familiar, near at hand (subjective, contextual)
- **We confuse them** (choose familiar complex over unfamiliar simple)

**Complecting**:
- **Braiding concerns together** (state + logic, validation + transformation)
- **Makes code complex** (hard to understand, test, change)
- **Can be avoided** (separate functions, explicit dependencies, data orientation)

**Benefits of Simplicity**:
- **Easier to understand** (each part is independent)
- **Easier to test** (no complex setup)
- **Easier to change** (modify one without breaking others)
- **More reliable** (fewer interactions = fewer bugs)

**Achieving Simplicity**:
- **Choose simple constructs** (values, functions, data)
- **Avoid complecting** (separate concerns, make dependencies explicit)
- **Abstract with data** (not classes for everything)
- **Be willing to learn** (unfamiliar simple > familiar complex)

**Rich Hickey's Gift**:
- **Named the problem** (complecting—now we can see it!)
- **Provided a framework** (simple vs easy, constructs vs artifacts)
- **Showed it's possible** (Clojure proves simple can be practical)

**In the Valley**:
- **Simplicity is our north star** (every decision asks: is this simple?)
- **We choose simple over easy** (Nix, Clojure, runit—all unfamiliar but simple)
- **We decomplect constantly** (separate concerns, explicit dependencies)
- **We build for understanding** (not just convenience)

---

**Next**: We'll explore **types and sets**—the mathematical foundations that let us reason about programs formally. Simplicity meets mathematics!

---

**Navigation**:  
← Previous: [9520 (functional programming basics)](/12025-10/9520-functional-programming-basics) | **Phase 1 Index** | Next: [9540 (types sets mathematical foundations)](/12025-10/9540-types-sets-mathematical-foundations)

**Bridge to Narrative**: For Hickey's wisdom embodied, see [9949 (The Wise Elders)](/12025-10/9949-intro-clojure-nix-ecosystem)!

**Metadata**:
- **Phase**: 1 (Foundations)
- **Week**: 2-3
- **Prerequisites**: 9504, 9510, 9520
- **Concepts**: Simple vs easy, complecting, decomplecting, constructs vs artifacts, design philosophy
- **Next Concepts**: Types, sets, mathematical foundations, formal reasoning
- **Required Viewing**: Rich Hickey's "Simple Made Easy" talk (1 hour)



---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*