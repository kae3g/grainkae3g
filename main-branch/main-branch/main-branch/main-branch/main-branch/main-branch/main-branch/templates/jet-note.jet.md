# Jet Note Template: [Feature Name] - Fast Path

**Created**: [Date]  
**Author**: [Your Name]  
**Status**: [ ] Proposed | [ ] Verified | [ ] Deployed

---

## Purpose

**Optimized host routine** equivalent to the Nock form in `[feature].nock.md`

**Why a jet?**:
- Nock interpretation can be slow (recursive reductions)
- Host language (Clojure, Rust) can optimize common patterns
- **Jet** = verified shortcut (same semantics, faster execution)

---

## Nock Expression (Canonical)

**From**: `[feature].nock.md`

```
; Nock formula being optimized:
[opcode args...]

; Example:
; Recursive filter operation
; (slow: O(n) reductions)
```

**Complexity**: [Time/space complexity of Nock interpretation]

---

## Host Jet (Optimized)

**Language**: Clojure (or Rust, if compiled)

**Implementation**:
```clojure
(ns jets.optimized
  (:require [clojure.spec.alpha :as s]))

;; Spec (SAME as canonical function!)
(s/fdef optimized-jet
  :args (s/cat :input ::input)
  :ret ::output
  :fn (fn [{:keys [args ret]}]
        ; SAME properties as Nock version!
        ...))

;; Optimized implementation
(defn optimized-jet [input]
  ;; Uses host primitives for speed
  ;; e.g., transducers, native data structures
  (into [] (comp (filter pred?) (map transform)) input))
```

**Optimizations used**:
- [ ] Transducers (reduce allocations)
- [ ] Native data structures (vs. cons cells)
- [ ] Memoization (cache expensive computations)
- [ ] Parallel execution (where safe)
- [ ] Other: [describe]

---

## Mapping (Nock ↔ Jet)

**Constraints**:
1. **Deterministic**: Same inputs → same outputs (always!)
2. **Pure**: No side effects (jet must be pure if Nock is pure)
3. **Equivalent**: Outputs must match exactly (verified!)

**Verification method**:
```clojure
;; Cross-check against Nock interpreter
(deftest jet-equivalence-test
  (doseq [input (load-golden-inputs)]
    (is (= (nock-eval formula input)
           (optimized-jet input)))))
```

---

## Performance

**Benchmarks** (on representative inputs):

| Input Size | Nock Path | Jet Path | Speedup |
|------------|-----------|----------|---------|
| 10 items   | 50ms      | 0.5ms    | 100x    |
| 100 items  | 500ms     | 3ms      | 167x    |
| 1000 items | 5000ms    | 25ms     | 200x    |

**Memory**:
- Nock: [memory usage]
- Jet: [memory usage]
- Reduction: [% saved]

---

## Verification Status

**Tests**:
- [ ] Unit tests (Clojure functions)
- [ ] Property tests (generative with Spec)
- [ ] Golden corpus (100+ vectors)
- [ ] Equivalence check (Nock = Jet for all inputs)
- [ ] Performance regression (jet doesn't get slower)

**CI Integration**:
- [ ] Runs on every commit
- [ ] Fails if equivalence breaks
- [ ] Reports metrics

---

## Deployment

**When to use**:
- [ ] Development (always use jet - fast iteration!)
- [ ] Production (use jet - performance critical!)
- [ ] Verification (compare against Nock - trust but verify!)

**Fallback**:
- If jet has bug → fall back to Nock interpretation
- Nock is **always correct** (canonical spec)
- Jet is **optimization** (can be disabled)

---

## Notes

**Assumptions**:
- [Any constraints on inputs]
- [Platform requirements]
- [Known edge cases]

**Future optimizations**:
- [ ] Compile jet to native (GraalVM)
- [ ] Port to Rust (zero-cost abstractions)
- [ ] Parallelize (if semantics allow)

---

**Signature**: [Your Name]  
**Date**: [Date]  
**Verified By**: [ ] Self | [ ] Peer | [ ] Formal proof

