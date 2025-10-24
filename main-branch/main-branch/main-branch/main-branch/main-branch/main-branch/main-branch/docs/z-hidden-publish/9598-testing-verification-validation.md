# kae3g 9597: Testing - Verification and Validation

**Phase 1: Foundations & Philosophy** | **Week 4** | **Reading Time: 17 minutes**

---

## What You'll Learn

- Why testing matters (catch bugs early, enable refactoring)
- Unit tests vs integration tests vs end-to-end tests
- Test-driven development (TDD)
- Property-based testing (QuickCheck, test.check)
- Why formal verification is better (but harder)
- Coverage metrics and their limitations
- Testing as observing your garden's health (plant lens)

---

## Prerequisites

- **[9520: Functional Programming](/12025-10/9520-functional-programming-basics)** - Pure functions (easy to test!)
- **[9504: What Is Clojure?](/12025-10/9504-what-is-clojure)** - Clojure testing (test.check)
- **[9503: What Is Nock?](/12025-10/9503-what-is-nock)** - Formal verification (proof > testing)

---

## Testing vs Verification

**Testing**: Check finite cases

**Verification**: Prove for ALL cases

**Example**:

**Testing**:
```clojure
(deftest test-addition
  (is (= 4 (+ 2 2)))    ; Check one case
  (is (= 10 (+ 5 5)))   ; Check another
  (is (= 0 (+ 0 0))))   ; Check edge case

; Covers 3 cases, but what about 2+3? 100+200? -5+10?
```

**Verification** (mathematical proof):
```
Theorem: ∀ a,b ∈ ℤ, a + b = b + a  (commutative property)
Proof: [formal mathematical proof]

Result: Proven for ALL integers (infinite cases!)
```

**Testing** = practical, covers common cases  
**Verification** = rigorous, covers ALL cases

**For most code**: Testing is good enough.  
**For critical code** (kernels, crypto, safety-critical): Verification needed (Essay 9503 - seL4!).

---

## The Testing Pyramid

**Different levels** of testing:

```
         /\
        /  \  E2E (slow, few)
       /----\
      / Intg \  Integration (moderate)
     /--------\
    /   Unit   \  Unit tests (fast, many)
   /____________\
```

### Unit Tests (Base)

**Test one function** in isolation:

```clojure
(defn add [a b]
  (+ a b))

(deftest test-add
  (is (= 4 (add 2 2)))
  (is (= 0 (add 0 0)))
  (is (= -5 (add -10 5))))
```

**Characteristics**:
- **Fast** (milliseconds)
- **Isolated** (no dependencies, databases, network)
- **Many** (hundreds or thousands)

**Goal**: Catch bugs in individual components.

### Integration Tests (Middle)

**Test components together**:

```clojure
(deftest test-database-save-load
  (let [db (create-test-db)]
    (save-user db {:name "Alice"})
    (is (= "Alice" (:name (load-user db 1))))))
```

**Characteristics**:
- **Slower** (seconds - real database, filesystem)
- **Dependencies** (DB, files, services)
- **Fewer** (dozens or hundreds)

**Goal**: Catch bugs in interactions between components.

### End-to-End (Top)

**Test entire system** (like a user):

```javascript
// Selenium/Playwright test
test('User can login', async () => {
  await page.goto('http://app.com/login');
  await page.fill('#username', 'alice');
  await page.fill('#password', 'secret');
  await page.click('#submit');
  await expect(page).toHaveURL('/dashboard');
});
```

**Characteristics**:
- **Slowest** (minutes - full app, browser, network)
- **Brittle** (UI changes break tests)
- **Few** (a dozen critical paths)

**Goal**: Catch bugs in complete user workflows.

---

## Test-Driven Development (TDD)

**Write tests BEFORE code** (controversial but powerful):

**Red-Green-Refactor cycle**:
```
1. RED: Write test (fails - code doesn't exist yet)
2. GREEN: Write minimal code to pass test
3. REFACTOR: Improve code (tests ensure it still works)
4. Repeat
```

**Example**:

```clojure
;; 1. RED: Write test first
(deftest test-factorial
  (is (= 1 (factorial 0)))
  (is (= 1 (factorial 1)))
  (is (= 120 (factorial 5))))

;; Test fails (factorial doesn't exist)

;; 2. GREEN: Minimal implementation
(defn factorial [n]
  (if (<= n 1)
    1
    (* n (factorial (dec n)))))

;; Test passes!

;; 3. REFACTOR: Improve (tail-recursive)
(defn factorial [n]
  (letfn [(fact-helper [n acc]
            (if (<= n 1)
              acc
              (recur (dec n) (* n acc))))]
    (fact-helper n 1)))

;; Test still passes (verified by tests!)
```

**Benefits**:
- Forces you to think about API before implementation
- Ensures code is testable (modular, pure functions)
- Prevents over-engineering (only write what tests need)

**Drawbacks**:
- Slower initial development (write test first)
- Can lead to myopic design (optimize for tests, not overall architecture)

---

## Property-Based Testing

**Traditional testing**: Specify inputs and expected outputs

**Property-based**: Specify properties that should ALWAYS hold

**Example**:

```clojure
;; Traditional
(deftest test-reverse
  (is (= [3 2 1] (reverse [1 2 3])))
  (is (= [] (reverse []))))

;; Property-based
(require '[clojure.test.check.generators :as gen]
         '[clojure.test.check.properties :as prop])

(def reverse-property
  (prop/for-all [v (gen/vector gen/int)]
    ;; Property: reversing twice = original
    (= v (reverse (reverse v)))))

;; Test with 100 random vectors!
(quick-check 100 reverse-property)
```

**Benefits**:
- Tests edge cases you didn't think of (fuzzing!)
- Fewer tests cover more cases (properties > examples)
- Finds bugs traditional tests miss

**Clojure**: `test.check` library (QuickCheck for Clojure).

---

## Coverage: The Incomplete Metric

**Code coverage**: What % of code is executed by tests?

```bash
# Run tests with coverage
pytest --cov=myapp tests/

# Output:
# myapp.py    85%  (120 lines, 102 covered)
```

**Common mistake**: "100% coverage = no bugs!"

**Reality**: Coverage shows **what was executed**, not **what was verified**.

**Example**:

```python
def dangerous_function(x):
    if x > 0:
        return x * 2
    else:
        launch_missiles()  # BUG!

def test_dangerous():
    assert dangerous_function(5) == 10

# Coverage: 50% (only 'if x > 0' branch tested)
# But worse: Didn't test negative case (would launch missiles!)
```

**Coverage is useful**, but **not sufficient** (need good assertions!).

---

## Testing Pure Functions

**Pure functions** (Essay 9520) are **easy to test**:

```clojure
;; Pure function (no side effects, same input = same output)
(defn double [x]
  (* x 2))

;; Simple test
(deftest test-double
  (is (= 4 (double 2)))
  (is (= 0 (double 0)))
  (is (= -10 (double -5))))

; Deterministic, no mocking, no setup/teardown
```

**Impure functions** (side effects) are **hard to test**:

```python
def save_to_database(user):
    db = connect_database()  # Side effect!
    db.save(user)
    send_email(user.email)   # Side effect!
    log("Saved user")        # Side effect!

# Test requires: mock DB, mock email, mock logger
# Complex!
```

**Lesson**: **Pure functions are naturally testable** (push side effects to boundaries).

---

## Formal Verification: Beyond Testing

**Testing**: Finite checks (can't test all inputs)

**Formal verification**: Mathematical proof (ALL inputs guaranteed)

**seL4** (Essay 9954, 9503):
- 10,000 lines of C
- **Proven**: No crashes, no memory leaks, no security bugs
- 11 person-years to verify
- **Zero exploits** since 2009 (testing can't achieve this!)

**Nock** (Essay 9503):
- 12 rules → verification tractable
- Prove properties mathematically
- Example: Prove scheduler is fair (not just test 1000 cases)

**Trade-off**: Verification = expensive, but **absolute certainty**.

**When worth it**: Safety-critical (aerospace, medical, financial, OS kernels).

---

## Try This

### Exercise 1: Write Unit Test

```clojure
;; Function to test
(defn fibonacci [n]
  (cond
    (<= n 0) 0
    (= n 1) 1
    :else (+ (fibonacci (- n 1))
             (fibonacci (- n 2)))))

;; Your test:
(deftest test-fibonacci
  (is (= 0 (fibonacci 0)))
  (is (= 1 (fibonacci 1)))
  (is (= 1 (fibonacci 2)))
  (is (= 55 (fibonacci 10))))
```

**Run**: `clojure -X:test` (or your test runner).

---

### Exercise 2: Find Bug via Testing

```python
def divide(a, b):
    return a / b

# Test
def test_divide():
    assert divide(10, 2) == 5
    assert divide(7, 2) == 3.5
    # Add this:
    assert divide(10, 0) == ???  # What should this be?

# Running reveals: ZeroDivisionError!
# Fix:
def divide(a, b):
    if b == 0:
        raise ValueError("Cannot divide by zero")
    return a / b
```

---

### Exercise 3: Property-Based Test

```clojure
(require '[clojure.test.check :as tc]
         '[clojure.test.check.generators :as gen]
         '[clojure.test.check.properties :as prop])

;; Property: sorting is idempotent
(def sort-idempotent
  (prop/for-all [v (gen/vector gen/int)]
    (= (sort v) (sort (sort v)))))

;; Run with 100 random vectors
(tc/quick-check 100 sort-idempotent)
```

**Observe**: Generates random inputs, checks property holds.

---

## Going Deeper

### Related Essays
- **[9520: Functional Programming](/12025-10/9520-functional-programming-basics)** - Pure functions (testable!)
- **[9504: What Is Clojure?](/12025-10/9504-what-is-clojure)** - test.check library
- **[9503: What Is Nock?](/12025-10/9503-what-is-nock)** - Formal verification
- **[9954: seL4](/12025-10/9954-sel4-verified-microkernel)** - Verification at scale

### External Resources
- **"Growing Object-Oriented Software, Guided by Tests"** - TDD classic
- **QuickCheck** (Haskell) - Original property-based testing
- **test.check** (Clojure) - Property-based for Clojure
- **Hypothesis** (Python) - Property-based for Python

---

## Reflection Questions

1. **How much testing is enough?** (100% coverage? Critical paths only? When diminishing returns?)

2. **Should all code be test-driven?** (TDD pros/cons - when appropriate?)

3. **Can tests replace documentation?** (Some say tests ARE documentation - agree?)

4. **Is formal verification the future?** (Or too expensive for most code?)

5. **How would Nock programs be tested?** (Pure functions (noun → noun) - property-based ideal!)

---

## Summary

**Testing Fundamentals**:

**Types of Tests**:
- **Unit**: One function, isolated, fast (many)
- **Integration**: Components together, slower (moderate)
- **End-to-end**: Full system, slowest (few)

**Testing Approaches**:
- **Example-based**: Specific inputs → expected outputs
- **Property-based**: Random inputs, check properties hold
- **Test-driven**: Write tests first, code second

**Key Concepts**:
- **Red-Green-Refactor**: TDD cycle
- **Coverage**: % code executed (incomplete metric!)
- **Mocking**: Fake dependencies for testing
- **Assertion**: Check expected = actual

**Pure Functions Win**:
- Deterministic (same input = same output)
- No side effects (no setup/teardown)
- Naturally testable (no mocking needed)

**Verification > Testing**:
- Testing: Finite cases (practical)
- Verification: All cases (mathematical proof)
- seL4: 11 person-years, zero exploits
- Nock: 12 rules → verification tractable

**In the Valley**:
- **We test critical paths** (not 100% coverage obsession)
- **We use property-based** when possible (more coverage, fewer tests)
- **We prefer pure functions** (testability is design!)
- **We look toward verification** (Nock-based systems, provable)

**Plant lens**: **"Testing is observing garden health—check soil (unit), water flow (integration), entire ecosystem (end-to-end). Healthy gardens show it."**

---

**Next**: We'll explore **documentation**—how to write for humans, why good docs matter, and the art of explaining complex systems simply!

---

**Navigation**:  
← Previous: [9597 (version control git foundations)](/12025-10/9597-version-control-git-foundations) | **Phase 1 Index** | Next: [9599 (documentation writing for humans)](/12025-10/9599-documentation-writing-for-humans)

**Metadata**:
- **Phase**: 1 (Foundations)
- **Week**: 4
- **Prerequisites**: 9520, 9504, 9503
- **Concepts**: Unit tests, integration tests, TDD, property-based testing, coverage, formal verification
- **Next Concepts**: Documentation, technical writing, teaching
- **Plant Lens**: Observing garden health, soil checks (unit), water flow (integration), ecosystem (e2e)



---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*