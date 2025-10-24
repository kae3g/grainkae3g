# Equivalence Note Template: [Feature Name]

**Created**: [Date]  
**Author**: [Your Name]  
**Status**: [ ] Draft | [ ] Verified | [ ] Deployed

---

## Intent

**One-line description**: What this transformation does.

---

## Clojure Path (Phenotype - Fast, Mutable)

**Module**: `[your.namespace]`

**Function**:
```clojure
(ns your.namespace
  (:require [clojure.spec.alpha :as s]))

;; Spec for inputs/outputs
(s/def ::input ...)
(s/def ::output ...)

;; Function spec
(s/fdef your-function
  :args (s/cat :input ::input)
  :ret ::output
  :fn (fn [{:keys [args ret]}]
        ; Property: [what must be true about output given input]))

;; Implementation
(defn your-function [input]
  ; Pure transformation
  (transform input))
```

**Properties** (what Spec enforces):
- Input constraints: [e.g., "must be non-empty list"]
- Output guarantees: [e.g., "all items satisfy predicate"]
- Relationship: [e.g., "(count output) <= (count input)"]

---

## Nock Form (Genotype - Immutable, Eternal)

**Canonical reduction**:

```
; Input noun: [structure of input]
; Output noun: [structure of output]

; Nock formula (using 12 rules):
[opcode args...]

; Example:
; *[[42 17] 0 2]  ; Slot 2 of [42 17]
; => 17           ; Result
```

**Reduction steps** (show the work!):
```
Step 1: ...
Step 2: ...
Step N: Result
```

---

## Equivalence Claim

**Theorem**: For all inputs in domain \( D \), the Clojure function and Nock reduction produce the same output:

\[
\forall x \in D: \text{clojure-fn}(x) \equiv \text{nock-reduce}(\text{formula}, x)
\]

**Domain**: [Describe valid inputs]

---

## Evidence

### Test Vectors (Golden Corpus)

**Stored in**:
- `var/golden/[feature]/inputs.json`
- `var/golden/[feature]/outputs.json`
- `var/golden/[feature]/reductions.log`

**Generated via**:
```clojure
;; Generative testing with Spec
(require '[clojure.spec.gen.alpha :as gen]
         '[clojure.test.check :as tc])

(tc/quick-check 100
  (prop/for-all [input (s/gen ::input)]
    (= (your-function input)
       (nock-eval formula input))))
```

### Property Checks

**Properties verified**:
1. [ ] Determinism (same input → same output, always)
2. [ ] Purity (no side effects)
3. [ ] Totality (handles all valid inputs)
4. [ ] Equivalence (Clojure = Nock for all test vectors)

### Verification Status

**Coverage**:
- Test vectors: [N/M] passed
- Edge cases: [list any special cases]
- Performance: Fast path [Xms], True path [Yms]

**Blockers**: [Any issues preventing full verification]

---

## Notes

**Implementation quirks**:
- [Any deviations from pure Nock or pure Clojure]
- [Performance trade-offs]
- [Known limitations]

**Future work**:
- [ ] Add formal proof (Liquid Haskell)
- [ ] Increase test coverage
- [ ] Optimize Nock interpreter (jets!)

---

**Signature**: [Your Name]  
**Date**: [Date]  
**Verification**: [ ] Self | [ ] Peer-reviewed | [ ] Formally proven

