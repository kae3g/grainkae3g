# kae3g 9520: Functional Programming - Computing with Pure Functions

**Phase 1: Foundations & Philosophy** | **Week 2** | **Reading Time: 16 minutes**

---

## What You'll Learn

- What functional programming (FP) actually means
- Pure functions: same input → same output, no side effects
- Immutability: why unchanging data prevents bugs
- Higher-order functions: functions that take/return functions
- Composition: building complex behavior from simple pieces
- Why FP is becoming dominant (concurrency, testability, reasoning)
- How FP relates to mathematics (lambda calculus, category theory)

---

## Prerequisites

- **[9504: What Is Clojure?](/12025-10/9504-what-is-clojure)** - Practical FP example
- **[9510: Unix Philosophy](/12025-10/9510-unix-philosophy-do-one-thing-well)** - Composition mindset

---

## What Is Functional Programming?

**The simple definition**:

> **Functional programming treats computation as the evaluation of mathematical functions and avoids changing state and mutable data.**

**Unpacking**:
- **Computation = function evaluation** (not sequences of instructions)
- **Avoid changing state** (no `x = x + 1`)
- **Avoid mutable data** (values don't change after creation)

**The shift**:
```
Imperative: "Do this, then do that, then do this other thing"
            (recipe—list of steps)

Functional: "The answer is f(g(h(input)))"
            (equation—composition of functions)
```

---

## Pure Functions: The Foundation

**A pure function**:
1. **Same input → same output** (deterministic, no randomness)
2. **No side effects** (doesn't change anything outside itself)

### Examples

**Pure**:
```clojure
(defn add [x y]
  (+ x y))

(add 3 5)  ; => 8
(add 3 5)  ; => 8 (always!)
```

- Same inputs (3, 5) → same output (8)
- No side effects (doesn't print, doesn't write files, doesn't change global state)

**Impure** (side effect: printing):
```clojure
(defn add-and-log [x y]
  (println "Adding" x "and" y)  ; SIDE EFFECT!
  (+ x y))

(add-and-log 3 5)  
; Prints: "Adding 3 and 5"
; => 8
```

- Still returns same output, but **observable effect** (printing)
- Not mathematically pure

**Impure** (side effect: mutation):
```javascript
let total = 0;

function addToTotal(x) {
  total = total + x;  // SIDE EFFECT: mutates global
  return total;
}

addToTotal(5)  ; => 5
addToTotal(5)  ; => 10 (different output for same input!)
```

- **Different outputs** for same input
- **Non-deterministic** (output depends on when you call it)
- **Hard to test** (must set up global state)

### Why Purity Matters

**Benefits of pure functions**:

1. **Easy to test**
```clojure
;; Pure function: just call it
(add 3 5)  ; => 8 (test passed!)

;; Impure function: must set up state, mock I/O, etc.
```

2. **Easy to reason about**
```clojure
;; What does this do?
(defn mystery [x] (* x x))

;; Just look at the code: squares its input
;; No need to check global variables, database state, etc.
```

3. **Parallel-safe**
```clojure
;; Call these in parallel—no race conditions!
(future (add 1 2))
(future (add 3 4))
(future (add 5 6))
```

4. **Memoizable** (cache results)
```clojure
;; Since (expensive-calc 42) always returns same value,
;; compute once, cache forever
(def expensive-calc (memoize expensive-calc))
```

5. **Time-travel debugging**
```clojure
;; Replay with same inputs → same outputs (reproducible bugs!)
```

---

## Immutability: Values That Never Change

**Most languages**: Variables **vary**.

```python
x = [1, 2, 3]
x.append(4)  # Mutated!
# x is now [1, 2, 3, 4]
```

**FP languages**: Values are **immutable**.

```clojure
(def x [1 2 3])
(def y (conj x 4))  ; New value

x  ; => [1 2 3] (unchanged!)
y  ; => [1 2 3 4] (new value)
```

### Why Immutability Prevents Bugs

**The problem with mutation**:

```javascript
function processUsers(users) {
  users.sort((a, b) => a.age - b.age);  // MUTATES input!
  return users.filter(u => u.age > 18);
}

const allUsers = [{name: "Alice", age: 30}, {name: "Bob", age: 17}];
const adults = processUsers(allUsers);

// allUsers is now SORTED (side effect!)
// Other code expecting original order: BROKEN
```

**With immutability**:

```clojure
(defn process-users [users]
  (->> users
       (sort-by :age)
       (filter #(> (:age %) 18))))

(def all-users [{:name "Alice" :age 30} {:name "Bob" :age 17}])
(def adults (process-users all-users))

;; all-users is UNCHANGED (no side effect)
;; Other code: SAFE
```

**Immutability = no spooky action at a distance.**

---

## Higher-Order Functions

**Functions that take functions as arguments** or **return functions as results**.

### Map: Transform Each Element

```clojure
(map inc [1 2 3 4 5])
; => (2 3 4 5 6)

(map square [1 2 3 4 5])
; => (1 4 9 16 25)

(map :name [{:name "Alice"} {:name "Bob"}])
; => ("Alice" "Bob")
```

**`map` is a higher-order function** (takes function `inc`, `square`, `:name` as argument).

### Filter: Keep Some Elements

```clojure
(filter even? [1 2 3 4 5 6])
; => (2 4 6)

(filter #(> % 10) [5 15 3 20 7 30])
; => (15 20 30)
```

**`filter` is higher-order** (takes predicate function as argument).

### Reduce: Combine Elements

```clojure
(reduce + [1 2 3 4 5])
; => 15  (1+2+3+4+5)

(reduce * [1 2 3 4 5])
; => 120 (1*2*3*4*5 = 5 factorial)

(reduce (fn [acc x] (conj acc (* x x))) [] [1 2 3 4 5])
; => [1 4 9 16 25]  (build vector of squares)
```

**`reduce` is the most powerful** (can implement map, filter, and more using reduce!).

### Returning Functions (Closures)

```clojure
(defn make-multiplier [factor]
  (fn [x] (* x factor)))  ; Returns a function!

(def times-10 (make-multiplier 10))
(def times-100 (make-multiplier 100))

(times-10 5)   ; => 50
(times-100 5)  ; => 500
```

**The returned function "closes over" `factor`** (remembers it). This is a **closure**.

---

## Composition: The Heart of FP

**Mathematical composition**:
```
f(x) = x + 1
g(x) = x * 2

(g ∘ f)(x) = g(f(x)) = (x + 1) * 2
```

**Functional programming**:
```clojure
(defn f [x] (+ x 1))
(defn g [x] (* x 2))

(defn g-of-f [x]
  (g (f x)))

(g-of-f 5)  ; => 12  (5+1=6, 6*2=12)
```

**Using `comp`** (composition function):
```clojure
(def g-of-f (comp g f))

(g-of-f 5)  ; => 12
```

**Or threading macros**:
```clojure
(-> 5
    f    ; 6
    g)   ; 12
```

**Same idea as Unix pipes**: Data flows through transformations.

---

## Declarative vs Imperative

### Imperative (HOW to do it)

```javascript
// Sum of squares of even numbers
let sum = 0;
for (let i = 0; i < arr.length; i++) {
  if (arr[i] % 2 === 0) {
    sum += arr[i] * arr[i];
  }
}
```

**Steps**: Initialize sum, loop, check condition, update sum.

### Declarative (WHAT you want)

```clojure
(->> arr
     (filter even?)
     (map #(* % %))
     (reduce +))
```

**Transformation pipeline**: Filter evens → square each → sum.

**Benefits of declarative**:
- **Reads like English** ("filter even, map square, reduce add")
- **No loop indices** (no off-by-one errors!)
- **No mutable accumulator** (no `sum = sum + ...`)
- **Easier to parallelize** (map/filter can run in parallel—no shared state)

---

## Why FP Is Winning

### 1. Concurrency

**Mutable state + threads = race conditions**:

```python
# Two threads incrementing counter
counter = 0

def increment():
    global counter
    counter = counter + 1  # NOT ATOMIC!

# Thread 1: read 0, add 1, write 1
# Thread 2: read 0, add 1, write 1  (race!)
# Result: 1 (should be 2)
```

**Fix**: Locks (complex, slow, deadlock-prone).

**FP approach**: **Immutable data**

```clojure
;; No mutation → no race conditions
(defn increment [counter]
  (+ counter 1))

;; Call from any thread—safe!
```

**For actual shared state**: Clojure's atoms (compare-and-swap, lock-free).

### 2. Testability

**Pure functions are trivial to test**:

```clojure
(defn add [x y] (+ x y))

;; Test:
(assert (= (add 2 3) 5))
(assert (= (add 0 0) 0))
(assert (= (add -1 1) 0))

;; No setup, no mocking, no teardown
```

**Impure functions**:

```javascript
function saveUser(user) {
  database.insert(user);  // Side effect!
  sendEmail(user.email);  // Side effect!
  logAudit(user.id);      // Side effect!
}

// Test: Mock database, email service, logger...
// Complex, fragile, slow
```

### 3. Reasoning

**Pure functions are equations**:

```clojure
(defn total [prices]
  (reduce + prices))

;; You can reason algebraically:
(total []) = 0  (identity)
(total [a]) = a
(total [a b]) = a + b (associative, commutative)
```

**Impure functions**: Must trace through execution, track state, consider order.

---

## FP Concepts in Other Languages

**FP isn't Clojure-only.** It's spreading:

### JavaScript (Functional Style)

```javascript
// Imperative
let doubled = [];
for (let i = 0; i < arr.length; i++) {
  doubled.push(arr[i] * 2);
}

// Functional
const doubled = arr.map(x => x * 2);
```

**ES6+ embraced FP**: `map`, `filter`, `reduce`, arrow functions, const/let.

### Python (Functional Tools)

```python
# map, filter, reduce
from functools import reduce

nums = [1, 2, 3, 4, 5]
doubled = list(map(lambda x: x * 2, nums))
evens = list(filter(lambda x: x % 2 == 0, nums))
total = reduce(lambda acc, x: acc + x, nums)
```

**Or comprehensions** (functional in spirit):
```python
doubled = [x * 2 for x in nums]
evens = [x for x in nums if x % 2 == 0]
```

### Rust (FP + Safety)

```rust
let nums = vec![1, 2, 3, 4, 5];

let doubled: Vec<_> = nums.iter()
    .map(|x| x * 2)
    .collect();

let total: i32 = nums.iter().sum();
```

**Rust combines FP (immutability, composition) with systems programming (no GC, memory safety).**

---

## Hands-On: FP Practice

### Exercise 1: Refactor to Pure

**Given** (impure):
```javascript
let total = 0;

function addToTotal(x) {
  total += x;
  return total;
}
```

**Refactor** (pure):
```javascript
function add(total, x) {
  return total + x;
}

// Call site manages state
let total = 0;
total = add(total, 5);
total = add(total, 3);
```

**Or with reduce**:
```javascript
const total = [5, 3, 7].reduce(add, 0);
```

---

### Exercise 2: Composition

**Build a pipeline**:

```clojure
;; Given: list of numbers
;; Want: sum of squares of even numbers

;; Step 1: Filter evens
(filter even? [1 2 3 4 5 6])  ; => (2 4 6)

;; Step 2: Square each
(map #(* % %) [2 4 6])  ; => (4 16 36)

;; Step 3: Sum
(reduce + [4 16 36])  ; => 56

;; Compose:
(->> [1 2 3 4 5 6]
     (filter even?)
     (map #(* % %))
     (reduce +))  ; => 56
```

**Try**: Sum of cubes of odd numbers in [1..10].

---

### Exercise 3: Higher-Order Function

**Write your own `map`**:

```clojure
(defn my-map [f coll]
  (if (empty? coll)
    '()
    (cons (f (first coll))
          (my-map f (rest coll)))))

(my-map inc [1 2 3])  ; => (2 3 4)
(my-map square [1 2 3])  ; => (1 4 9)
```

**Recursive definition**:
- Empty list → empty result
- Non-empty → apply `f` to first, recurse on rest

**This is how `map` is actually defined** (conceptually—optimized in practice).

---

## Common FP Patterns

### 1. Map-Reduce

**Pattern**: Transform each item (`map`), then combine (`reduce`).

```clojure
;; Total price of items
(def items [{:name "Widget" :price 10}
            {:name "Gadget" :price 20}
            {:name "Gizmo" :price 15}])

(->> items
     (map :price)   ; Extract prices: (10 20 15)
     (reduce +))    ; Sum: 45
```

**Scales to distributed computing** (MapReduce, Hadoop, Spark—same pattern!).

### 2. Pipeline (Threading)

**Pattern**: Data flows through transformations.

```clojure
(-> data
    parse       ; data → parsed
    validate    ; parsed → validated
    transform   ; validated → transformed
    save)       ; transformed → saved
```

**Each step returns new data.** No mutation.

**Like Unix pipes**, but for data structures.

### 3. Currying (Partial Application)

**Currying**: Convert `f(x, y)` to `f(x)(y)`.

```clojure
;; Normal function
(defn add [x y] (+ x y))
(add 10 5)  ; => 15

;; Partial application (fix first argument)
(def add10 (partial add 10))
(add10 5)  ; => 15
(add10 20) ; => 30
```

**Useful for creating specialized functions** from general ones:

```clojure
(def log-error (partial log :error))
(def log-info (partial log :info))

(log-error "Something broke")  ; Same as: (log :error "Something broke")
```

---

## FP and Mathematics

### Lambda Calculus (Alonzo Church, 1930s)

**The mathematical foundation of FP**:

```
λx. x + 1        ; Function taking x, returning x+1
(λx. x + 1) 5    ; Apply to 5 → 6
```

**All computable functions** can be expressed in lambda calculus (Church-Turing thesis).

**Modern FP languages** are lambda calculus + practical features (types, I/O, performance).

### Category Theory

**Composition in category theory**:

```
Objects: Types (Integer, String, User)
Morphisms: Functions (Integer → String)
Composition: f: A → B, g: B → C ⇒ g ∘ f: A → C
```

**Laws**:
- **Associativity**: `(h ∘ g) ∘ f = h ∘ (g ∘ f)`
- **Identity**: `id ∘ f = f ∘ id = f`

**FP respects these laws**:

```clojure
;; Associativity
(comp h (comp g f)) ≡ (comp (comp h g) f) ≡ (comp h g f)

;; Identity
(comp identity f) ≡ (comp f identity) ≡ f
```

**We'll explore this deeply in Essay 9730: Category Theory for Programmers.**

---

## FP vs OOP

**The eternal debate**: Functional vs Object-Oriented Programming.

### OOP Approach

```java
class User {
  private String name;
  private int age;
  
  public User(String name, int age) {
    this.name = name;
    this.age = age;
  }
  
  public void haveBirthday() {
    this.age += 1;  // Mutation!
  }
  
  public boolean isAdult() {
    return this.age >= 18;
  }
}

User alice = new User("Alice", 17);
alice.haveBirthday();  // Mutated!
```

**OOP bundles**:
- **Data** (fields)
- **Behavior** (methods)
- **Identity** (this object vs that object)

### FP Approach

```clojure
;; Just data
(def alice {:name "Alice" :age 17})

;; Functions on data
(defn have-birthday [user]
  (update user :age inc))

(defn adult? [user]
  (>= (:age user) 18))

;; Use:
(def older-alice (have-birthday alice))
(adult? older-alice)  ; => true

;; Original unchanged:
alice  ; => {:name "Alice", :age 17}
```

**FP separates**:
- **Data** (maps, vectors)
- **Behavior** (functions)
- **Identity** (managed explicitly if needed)

### Which Is Better?

**It depends.**

**OOP strengths**:
- **Encapsulation** (hide implementation details)
- **Polymorphism** (different objects, same interface)
- **Familiar** (most programmers learned OOP first)

**FP strengths**:
- **Testability** (pure functions)
- **Concurrency** (immutability)
- **Composition** (small functions → complex behavior)
- **Reasoning** (equations, not stateful objects)

**Modern synthesis**: Use **both**!
- OOP for **structure** (modules, namespaces, encapsulation)
- FP for **logic** (pure functions, immutability, composition)

**Clojure's approach**: FP by default, OOP when needed (protocols, records).

---

## Try This

### Exercise 1: Refactor to Immutable

**Imperative** (mutation):
```javascript
function updateUsers(users) {
  for (let user of users) {
    user.lastSeen = Date.now();
  }
  return users;
}
```

**Functional** (immutable):
```clojure
(defn update-users [users]
  (map #(assoc % :last-seen (now)) users))
```

**Benefits**:
- Original `users` unchanged (no side effect)
- Easier to test (just call with sample data)
- Parallel-safe (no mutation)

---

### Exercise 2: Build with Higher-Order Functions

**Problem**: Given list of numbers, return list of those that are perfect squares.

**Imperative**:
```python
result = []
for n in nums:
    if int(n ** 0.5) ** 2 == n:
        result.append(n)
```

**Functional**:
```clojure
(defn perfect-square? [n]
  (let [root (Math/sqrt n)]
    (= n (* root root))))

(filter perfect-square? nums)
```

**One line!** (Plus the helper function.)

---

### Exercise 3: Compose Complex Behavior

**Problem**: Process a list of users:
1. Filter active users
2. Extract names
3. Sort alphabetically
4. Take first 10
5. Join with commas

**Functional composition**:
```clojure
(->> users
     (filter :active?)
     (map :name)
     (sort)
     (take 10)
     (clojure.string/join ", "))
```

**Five transformations, one pipeline.** Clear, testable, composable.

---

## Going Deeper

### Related Essays
- **[9504: What Is Clojure?](/12025-10/9504-what-is-clojure)** - FP in practice
- **[9530: Simplicity](/12025-10/9530-rich-hickey-simple-made-easy)** - Why FP aligns with simplicity
- **[9540: Types and Sets](/12025-10/9540-types-sets-mathematical-foundations)** - Mathematical foundations
- **[9730: Category Theory](/12025-10/9730-category-theory-for-programmers)** - Mathematical structure of FP

### External Resources
- **"Structure and Interpretation of Computer Programs"** (SICP) - Classic FP text
- **Rich Hickey**, "The Value of Values" talk - Why immutability matters
- **"Functional Programming in JavaScript"** by Luis Atencio
- **"Learn You a Haskell"** - Pure FP language (more extreme than Clojure)

### For the Mathematically Curious
- **Lambda calculus** - Church's original formulation
- **Curry-Howard correspondence** - Programs are proofs!
- **Haskell** - The purest FP language (forces purity via type system)

---

## Reflection Questions

1. **Can all programs be pure?** (No—I/O is inherently impure. But you can isolate impurity.)

2. **Is immutability too expensive?** (Persistent data structures make it O(log n), not O(n))

3. **Why isn't FP more popular?** (Unfamiliar, steeper learning curve—but gaining adoption)

4. **When should you use mutation?** (Performance hotspots, interfacing with mutable APIs—but minimize)

5. **Is OOP dead?** (No—but FP ideas are infiltrating OOP languages)

---

## Summary

**Functional Programming**:
- **Treats computation as function evaluation** (not sequential instructions)
- **Pure functions** (same input → same output, no side effects)
- **Immutable data** (values never change)
- **Higher-order functions** (functions as arguments/results)
- **Composition** (build complex from simple)

**Key Insights**:
- **Purity enables testability** (just call the function!)
- **Immutability enables concurrency** (no locks needed)
- **Composition enables reuse** (small functions → unlimited combinations)
- **Declarative style** (say what, not how) is clearer

**Benefits**:
- **Fewer bugs** (no mutation, no hidden state)
- **Easier testing** (pure functions, no setup)
- **Better concurrency** (immutable data, no races)
- **Mathematical reasoning** (functions are equations)

**Trade-offs**:
- **Unfamiliar** (learning curve for imperative programmers)
- **Performance** (sometimes mutation is faster—but optimize later)
- **Purity limits** (some problems need effects—IO, randomness, time)

**In the Valley**:
- **FP is the default** (Clojure, Nix expressions, Haskell-inspired thinking)
- **Immutability is sacred** (treat data as immutable until proven necessary to mutate)
- **Composition is key** (small functions, Unix-style pipelines)
- **Simple over clever** (clear code > terse code)

---

**Next**: We'll explore **Rich Hickey's "Simple Made Easy"** in depth—the philosophy underlying both Clojure and the valley itself.

---

**Navigation**:  
← Previous: [9516 (complete stack in action)](/12025-10/9516-complete-stack-in-action) | **Phase 1 Index** | Next: [9530 (rich hickey simple made easy)](/12025-10/9530-rich-hickey-simple-made-easy)

**Bridge to Narrative**: For FP storytelling, see [9949 (The Wise Elders)](/12025-10/9949-intro-clojure-nix-ecosystem) - Clojure as the Functional Sage!

**Metadata**:
- **Phase**: 1 (Foundations)
- **Week**: 2
- **Prerequisites**: 9504, 9510
- **Concepts**: Pure functions, immutability, higher-order functions, map/filter/reduce, composition, declarative vs imperative
- **Next Concepts**: Simplicity philosophy, decomplecting, Rich Hickey's design principles



---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*