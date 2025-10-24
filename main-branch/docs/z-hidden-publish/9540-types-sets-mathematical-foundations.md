# kae3g 9540: Types and Sets - Mathematical Foundations of Programming

**Phase 1: Foundations & Philosophy** | **Week 3** | **Reading Time: 15 minutes**

---

## What You'll Learn

- What types are (mathematically and practically)
- How types relate to sets in mathematics
- Type systems: what they prevent and what they enable
- Static vs dynamic typing (trade-offs, not dogma)
- Algebraic data types: products and sums
- How types guide design and catch errors
- Why "types are theorems" (Curry-Howard correspondence)

---

## Prerequisites

- **[9500: What Is a Computer?](/12025-10/9500-what-is-a-computer)** - Computational foundations
- **[9520: Functional Programming](/12025-10/9520-functional-programming-basics)** - Functions as mathematical objects
- **[9530: Simple Made Easy](/12025-10/9530-rich-hickey-simple-made-easy)** - Design philosophy

---

## What Is a Type?

**Informal definition**: A type is a **collection of values** that share common properties.

**Examples**:
- **Integer**: {..., -2, -1, 0, 1, 2, ...}
- **Boolean**: {true, false}
- **String**: {"", "a", "hello", "🌙", ...}

**In mathematics**: Types are **sets**.

**In programming**: Types **classify** what operations are valid:

```clojure
;; Integer: can add, multiply
(+ 3 5)  ; => 8

;; String: can concatenate, can't add
(+ "hello" "world")  ; Error! (in typed languages)
(str "hello" "world")  ; => "helloworld" (correct operation)
```

**Types prevent nonsense operations.**

---

## Sets: The Mathematical Foundation

**Set theory** (Georg Cantor, 1870s) underlies all mathematics—and programming.

### Basic Sets

**Natural numbers**: ℕ = {0, 1, 2, 3, ...}

**Integers**: ℤ = {..., -2, -1, 0, 1, 2, ...}

**Real numbers**: ℝ = {π, √2, -5.7, ...}

**Booleans**: 𝔹 = {true, false}

### Set Operations

**Union** (A ∪ B): Elements in A **or** B
```
{1, 2} ∪ {2, 3} = {1, 2, 3}
```

**Intersection** (A ∩ B): Elements in A **and** B
```
{1, 2} ∩ {2, 3} = {2}
```

**Cartesian Product** (A × B): Pairs from A and B
```
{1, 2} × {a, b} = {(1,a), (1,b), (2,a), (2,b)}
```

**Programming parallel**:
```clojure
;; Union: "or" types (we'll see this in sum types)
;; Intersection: "and" types (refinement types)
;; Product: tuples, records, structs
```

---

## Types in Programming

### Primitive Types

**Most languages have**:
```
Integer:  42, -17, 0
Float:    3.14, -0.5
Boolean:  true, false
Character: 'a', '字', '🌙'
String:   "hello valley"
```

**These are built-in.** Everything else is composed from these.

### Composite Types

**Product types** (A **and** B):
```clojure
;; Tuple (ordered pair)
[42 "Alice"]  ; Integer AND String

;; Record/Map (labeled fields)
{:name "Alice" :age 30}  ; String AND Integer (labeled)
```

**Product = Cartesian product** (all combinations).

**Sum types** (A **or** B):
```haskell
-- Haskell (has native sum types)
data Result = Success Int | Failure String

-- Value is EITHER Success(42) OR Failure("error")
-- Never both!
```

**Sum = union** (one of several options).

(Clojure doesn't have native sum types, but can encode them with maps: `{:type :success :value 42}`)

---

## Static vs Dynamic Typing

**The great debate**: Should types be checked at compile-time or runtime?

### Static Typing

**Examples**: Java, Haskell, Rust, TypeScript

**Type errors caught before running**:
```rust
fn add(x: i32, y: i32) -> i32 {
    x + y
}

add(3, "hello")  // Compile error: expected i32, got &str
```

**Pros**:
- **Catch errors early** (before production)
- **Documentation** (types describe what functions expect/return)
- **IDE support** (autocomplete knows what methods exist)
- **Performance** (no runtime type checks)

**Cons**:
- **Verbosity** (must declare types)
- **Rigidity** (some valid programs rejected)
- **Learning curve** (type systems can be complex)

### Dynamic Typing

**Examples**: Python, JavaScript, Clojure, Ruby

**Type errors caught when code runs**:
```python
def add(x, y):
    return x + y

add(3, "hello")  # Runtime error: unsupported operand types
```

**Pros**:
- **Concise** (no type annotations)
- **Flexible** (duck typing: "if it quacks like a duck...")
- **REPL-friendly** (rapid experimentation)
- **Simpler** (fewer language features)

**Cons**:
- **Runtime errors** (bugs in production)
- **Less documentation** (what does this function expect?)
- **Slower** (runtime type checks)
- **Refactoring harder** (compiler doesn't help find all usages)

### The Middle Ground

**Gradual typing**: Optional types (TypeScript, Python type hints, Clojure spec).

```typescript
// TypeScript: add types to JavaScript
function add(x: number, y: number): number {
    return x + y;
}

add(3, 5)  // OK
add(3, "hello")  // Error!
```

```clojure
;; Clojure spec: runtime validation
(require '[clojure.spec.alpha :as s])

(s/def ::age (s/and int? #(>= % 0)))
(s/def ::user (s/keys :req-un [::name ::age]))

(s/valid? ::user {:name "Alice" :age 30})  ; => true
(s/valid? ::user {:name "Bob" :age -5})    ; => false
```

**Best of both worlds**: Flexibility of dynamic + safety of static (when you want it).

---

## Algebraic Data Types

**"Algebraic"** = composed using algebra-like operations (product, sum).

### Product Types (AND)

**Tuple**:
```haskell
-- Haskell
type Point = (Int, Int)

-- Value: (3, 5)
-- Has TWO components (x AND y)
```

**Record**:
```clojure
;; Clojure
{:x 3 :y 5}

;; Has TWO labeled fields
```

**Cardinality** (size of set):
```
|Bool| = 2  (true, false)
|Int| = ∞  (unbounded integers)

|Bool × Int| = 2 × ∞ = ∞  (product = multiplication)
```

### Sum Types (OR)

**Enumeration**:
```rust
// Rust
enum Status {
    Pending,
    Approved,
    Rejected
}

// Value is ONE OF: Pending OR Approved OR Rejected
```

**Tagged union**:
```haskell
-- Haskell
data Maybe a = Nothing | Just a

-- Value is EITHER Nothing OR Just(value)
```

**Cardinality**:
```
|Status| = 3  (three options)
|Maybe Bool| = 1 + 2 = 3  (Nothing OR Just true OR Just false)

|Sum| = |A| + |B|  (sum = addition)
```

**Why "algebraic"**: Types combine via **arithmetic**!

---

## Types Prevent Errors

### Example: Optional Values

**Problem**: Handling missing data.

**Bad** (no types, null everywhere):
```javascript
function getUser(id) {
    return database.find(id);  // Might return null!
}

const user = getUser(42);
console.log(user.name);  // CRASH if user is null!
```

**Billion-dollar mistake** (Tony Hoare invented null in 1965, called it his "billion-dollar mistake").

**Good** (types enforce handling):
```rust
// Rust: Option type
fn get_user(id: u32) -> Option<User> {
    database.find(id)
}

match get_user(42) {
    Some(user) => println!("{}", user.name),
    None => println!("User not found")
}

// Compiler forces you to handle None case!
```

**Even better** (Haskell with Maybe):
```haskell
-- Type signature documents possibility of absence
getUser :: Int -> Maybe User

-- Must pattern match
case getUser 42 of
    Just user -> userName user
    Nothing -> "Unknown"
```

**Types make absence explicit** (can't forget to check).

---

## Function Types

**Functions have types too**:

```haskell
-- Haskell type signature
add :: Int -> Int -> Int
add x y = x + y

-- Reads: "add takes Int, takes another Int, returns Int"
```

**Arrow notation** (→):
```
f :: A → B   ; Function from A to B
g :: B → C   ; Function from B to C
g ∘ f :: A → C   ; Composition (feeds A to f, result to g, produces C)
```

**This is category theory**: Functions are **morphisms** between types (objects).

(We'll explore this in Essay 9730: Category Theory)

### Higher-Order Function Types

```haskell
-- map takes a function (a → b) and a list [a], returns list [b]
map :: (a -> b) -> [a] -> [b]

-- Examples:
map :: (Int -> String) -> [Int] -> [String]
map show [1,2,3]  -- => ["1","2","3"]
```

**The type signature is documentation**: "Give me a transformer and a list, I'll transform every element."

---

## The Curry-Howard Correspondence

**Mind-bending connection** between logic and types:

```
Logic           ↔  Programming
─────────────────────────────────
Proposition     ↔  Type
Proof           ↔  Program
Theorem         ↔  Type signature

"A implies B"   ↔  Function type A → B
"A and B"       ↔  Product type A × B  
"A or B"        ↔  Sum type A + B
```

**Example**:

**Proposition**: "If you give me an integer, I can give you a string."

**Type**: `Int → String`

**Proof**: Write a function with that type!

```haskell
intToString :: Int -> String
intToString n = show n

-- This program IS a proof of the proposition!
```

**Profound implication**: **Programs are proofs.** Types are theorems. Compiling is theorem-checking.

**Constructive mathematics** (build the object to prove existence) **is** programming!

---

## Type Inference

**Some languages infer types** (you don't write them, compiler figures them out):

```haskell
-- Haskell (no type annotations)
add x y = x + y

-- Compiler infers:
add :: Num a => a -> a -> a
-- "For any numeric type a, add takes a and a, returns a"
```

```ocaml
(* OCaml *)
let add x y = x + y

(* Inferred: int -> int -> int *)
```

**Benefits**:
- **Concise** (like dynamic typing)
- **Safe** (like static typing)
- **Best of both worlds** (write little, get safety)

**Limitation**: Complex types can be hard to infer (need annotations for clarity).

---

## Hands-On: Type Thinking

### Exercise 1: Determine Types

**What are the types?**

```clojure
;; 1.
(defn double [x] (* 2 x))
;; Type: Number → Number

;; 2.
(defn first-char [s] (first s))
;; Type: String → Char (or nil if empty!)

;; 3.
(defn filter-evens [numbers]
  (filter even? numbers))
;; Type: [Number] → [Number]

;; 4.
(defn apply-twice [f x]
  (f (f x)))
;; Type: (a → a) → a → a  (higher-order!)
```

**Think in types**: Helps reason about what functions can/can't do.

---

### Exercise 2: Type-Driven Design

**Start with the type signature**, then implement:

```haskell
-- Want: validate user
validateUser :: User -> Either Error User

-- Either Error User means:
-- - If valid: Right User
-- - If invalid: Left Error

validateUser user =
    if age user < 0
        then Left "Negative age"
        else if null (name user)
            then Left "Missing name"
            else Right user
```

**Type guides implementation**: Return type is `Either`, so must return `Left` or `Right`.

**Compiler enforces**: Can't forget error cases.

---

### Exercise 3: Cardinality

**Calculate how many possible values**:

```haskell
-- 1. Bool
|Bool| = 2  (true, false)

-- 2. (Bool, Bool)
|(Bool, Bool)| = 2 × 2 = 4  ((T,T), (T,F), (F,T), (F,F))

-- 3. Either Bool Bool
|Either Bool Bool| = 2 + 2 = 4  (Left T, Left F, Right T, Right F)

-- 4. Maybe Bool
|Maybe Bool| = 1 + 2 = 3  (Nothing, Just True, Just False)
```

**Cardinality helps** understand types (how many test cases do you need?).

---

## Type Systems in Practice

### Weak vs Strong Typing

**Weak typing** (implicit conversions):
```javascript
// JavaScript
"5" + 3  // => "53" (string concatenation)
"5" - 3  // => 2 (numeric subtraction)
true + 1 // => 2 (true = 1)
```

**Surprises!** The language **coerces** types.

**Strong typing** (no implicit conversions):
```python
# Python
"5" + 3  # TypeError: can't concatenate str and int
"5" - 3  # TypeError: unsupported operand types

# Must be explicit:
int("5") + 3  # => 8
"5" + str(3)  # => "53"
```

**Predictable**: No surprises. Type errors are loud.

**Clojure** (strong, dynamic):
```clojure
(+ "5" 3)  ; Error: String cannot be cast to Number
(+ 5 3)    ; => 8
```

### Nominal vs Structural Typing

**Nominal** (Java, C++): Types have **names**, names matter.

```java
class UserId { int value; }
class ProductId { int value; }

UserId u = new UserId(42);
ProductId p = u;  // Error! Different names, even though structure is identical
```

**Structural** (TypeScript, Go): Types defined by **shape**, names don't matter.

```typescript
// TypeScript
type Point = { x: number; y: number };
type Vec2 = { x: number; y: number };

const p: Point = { x: 3, y: 5 };
const v: Vec2 = p;  // OK! Same structure
```

**Trade-offs**:
- **Nominal**: More explicit (UserId ≠ ProductId, even if both are ints)
- **Structural**: More flexible (if shapes match, types match)

---

## Type Safety

**Type-safe languages** guarantee: **No type errors at runtime** (if compiler accepts, program won't crash due to type mismatch).

### Sound Type Systems

**Sound**: If compiler accepts, **no type errors** will occur.

**Examples**: Haskell, Rust, ML

**Benefits**: **Trust the compiler**. If it compiles, types are correct.

### Unsound Type Systems

**Unsound**: Compiler accepts, but type errors **might** occur.

**Examples**: Java (null can inhabit any type), TypeScript (explicit `any` escape hatch)

```java
String s = null;  // null is "subtype" of all types (unsound!)
s.length();  // NullPointerException at runtime
```

**Trade-off**: Pragmatism (Java's null) vs purity (Haskell's Maybe).

---

## Why Types Matter

### 1. Documentation

**Type signature documents behavior**:

```haskell
map :: (a -> b) -> [a] -> [b]

-- Reads: "Give me a function from any type a to any type b,
--         and a list of a's,
--         and I'll give you a list of b's."
```

**You know what `map` does** without reading implementation!

### 2. Catch Errors Early

```rust
fn divide(x: i32, y: i32) -> i32 {
    x / y
}

divide(10, 0)  // Compiles (but crashes at runtime—division by zero)

// Better: use Option to make failure explicit
fn divide_safe(x: i32, y: i32) -> Option<i32> {
    if y == 0 {
        None
    } else {
        Some(x / y)
    }
}

// Type forces caller to handle None case!
```

### 3. Enable Refactoring

**Change a type** → compiler finds **all** places that need updating.

```haskell
-- Change User type
data User = User String Int  -- name, age

-- Add email field
data User = User String Int String  -- Compiler error at every usage site!

-- Must update all pattern matches (compiler guides you)
```

**Without types**: Must manually search, might miss some usages.

### 4. Guide Design

**Types constrain** what you can build (in a good way).

```haskell
-- Can't compile this:
reverse :: [a] -> [a]
reverse list = 42  -- Error! Returns Int, but signature says [a]

-- Must implement correctly:
reverse [] = []
reverse (x:xs) = reverse xs ++ [x]
```

**Types force correctness.**

---

## Practical Type Wisdom

### When to Use Static Typing

**Choose static types when**:
- Large codebase (refactoring support)
- Multiple developers (types as contracts)
- Critical systems (catch errors early)
- Long-lived code (types as documentation)

**Examples**: Rust (systems programming), Haskell (finance, compilers), TypeScript (large JS projects)

### When to Use Dynamic Typing

**Choose dynamic types when**:
- Prototyping (rapid iteration)
- Scripts (small, short-lived)
- REPL-driven development (experimentation)
- Flexibility matters (heterogeneous data)

**Examples**: Python (data science, scripting), Clojure (REPL-driven web apps), JavaScript (small projects)

### The Valley's Position

**We use both**:
- **Clojure** (dynamic) for build scripts (flexibility, REPL)
- **Nix** (mostly untyped, but pure functions) for builds (determinism)
- **Rust** (static) for performance-critical code (safety + speed)
- **TypeScript** (gradual) for browser code (catch bugs, gradual adoption)

**Principle**: **Choose the right tool**. No dogma. Evaluate trade-offs.

---

## Try This

### Exercise 1: Type Signatures in Your Head

**For functions you write**, think about types:

```python
# Python (dynamic, but think in types)
def process_orders(orders):
    return [o for o in orders if o['total'] > 100]

# Type (imaginary): [Order] -> [Order]
# Constraint: orders must have 'total' field (partial function!)
```

**Better** (make constraint explicit):
```python
def process_orders(orders: List[Order]) -> List[Order]:
    return [o for o in orders if o.total > 100]
```

**Type annotations help** even in dynamic languages (documentation + IDE support).

---

### Exercise 2: Design with Types

**Problem**: Represent a traffic light.

**Bad** (stringly-typed):
```clojure
(def light "red")  ; What if someone writes "blue"?
```

**Good** (keyword enum):
```clojure
(def light :red)  ; One of: :red, :yellow, :green

(defn next-light [light]
  (case light
    :red :green
    :yellow :red
    :green :yellow))
```

**Even better** (Rust with enums):
```rust
enum Light { Red, Yellow, Green }

fn next_light(light: Light) -> Light {
    match light {
        Light::Red => Light::Green,
        Light::Yellow => Light::Red,
        Light::Green => Light::Yellow,
    }
}

// Compiler ensures: all cases handled!
```

---

### Exercise 3: Explore clojure.spec

```clojure
(require '[clojure.spec.alpha :as s])

;; Define specs (types, but at runtime)
(s/def ::age (s/and int? #(>= % 0) #(<= % 150)))
(s/def ::email (s/and string? #(re-matches #".+@.+" %)))
(s/def ::user (s/keys :req-un [::age ::email]))

;; Validate
(s/valid? ::user {:age 30 :email "alice@example.com"})  ; => true
(s/valid? ::user {:age -5 :email "invalid"})  ; => false

;; Explain why invalid
(s/explain ::user {:age -5 :email "invalid"})
; -5 - failed: (>= % 0)
; "invalid" - failed: (re-matches #".+@.+" %)
```

**Gradual typing**: Add specs where you want validation, skip where you want flexibility.

---

## Going Deeper

### Related Essays
- **[9520: Functional Programming](/12025-10/9520-functional-programming-basics)** - Functions as typed objects
- **[9701: Lie Groups](/12025-10/9701-lie-groups-continuous-symmetries)** - Mathematical structures (types are sets with structure!)
- **[9730: Category Theory](/12025-10/9730-category-theory-for-programmers)** - Types as objects, functions as morphisms
- **Phase 3** - Advanced type theory (~9710-9720)

### External Resources
- **"Types and Programming Languages"** by Benjamin Pierce - Comprehensive textbook
- **"Propositions as Types"** by Philip Wadler - Curry-Howard explained beautifully
- **Haskell** - Learn You a Haskell (pure functional + strong types)
- **Rust Book** - Ownership types (unique to Rust)

### For the Mathematically Curious
- **Set theory** - Cantor, ZFC axioms
- **Type theory** - Russell, Church, Martin-Löf
- **Category theory** - Types as objects, programs as morphisms

---

## Reflection Questions

1. **Are types primarily for the compiler or for humans?** (Both! But which matters more?)

2. **Is dynamic typing "simpler" than static typing?** (Depends on definition of simple—fewer language features, but more runtime complexity)

3. **Should all programs be formally verified?** (Ideal but impractical—what's the trade-off?)

4. **Can types replace tests?** (No—but they eliminate whole classes of tests)

5. **Is TypeScript worth it for JavaScript projects?** (Depends on size, team, longevity)

---

## Summary

**Types are**:
- **Sets of values** (mathematical foundation)
- **Classifications** preventing invalid operations
- **Documentation** (signatures describe behavior)
- **Proofs** (Curry-Howard: programs prove theorems)

**Key Concepts**:
- **Product types** (A AND B) - tuples, records, structs
- **Sum types** (A OR B) - enums, tagged unions, Maybe/Option
- **Algebraic** (combine via arithmetic: × for product, + for sum)
- **Static vs dynamic** (compile-time vs runtime checking)
- **Soundness** (guaranteed safety vs pragmatic escapes)

**Benefits**:
- **Catch errors early** (before production)
- **Guide refactoring** (compiler finds all usages)
- **Document intent** (types as specifications)
- **Enable tooling** (autocomplete, go-to-definition)

**Trade-offs**:
- **Static**: Safety + documentation, but verbosity + rigidity
- **Dynamic**: Concise + flexible, but runtime errors + less tooling
- **Gradual**: Middle ground (TypeScript, Python hints, Clojure spec)

**In the Valley**:
- **We use both paradigms** (Clojure dynamic, Rust static)
- **Types as contracts** (even in dynamic languages, think in types)
- **Simplicity over ceremony** (use types when they help, skip when they don't)
- **Mathematics as foundation** (types are sets, programs are proofs)

---

**Next**: We'll explore **the command line**—your primary interface to Unix systems, where you'll apply the text-oriented, compositional thinking we've been building!

---

**Navigation**:  
← Previous: [9530 (rich hickey simple made easy)](/12025-10/9530-rich-hickey-simple-made-easy) | **Phase 1 Index** | Next: [9550 (command line your primary interface)](/12025-10/9550-command-line-your-primary-interface)

**Metadata**:
- **Phase**: 1 (Foundations)
- **Week**: 3
- **Prerequisites**: 9500, 9520, 9530
- **Concepts**: Types, sets, static/dynamic typing, algebraic data types, Curry-Howard, type inference
- **Next Concepts**: Command line, shell, text processing, Unix tools mastery



---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*