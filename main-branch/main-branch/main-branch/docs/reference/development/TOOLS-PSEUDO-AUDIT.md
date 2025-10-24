# PSEUDO Audit System - Documentation

**Version**: 1.0  
**Created**: 2025-10-11  
**Philosophy**: God's TODO is unknowable; this system audits our approximation.

---

## Overview

The PSEUDO Audit System provides **automated self-reflection** for the Coldriver Tundra project. It reads `PSEUDO.md` (immediate reality) and `TODO-ASPIRATIONAL.md` (infinite vision) to generate timestamped reports with:

- Task completion statistics
- Oldest unfinished items
- Structural overview
- Philosophical entropy metrics
- Terminology consistency checks
- Verification suggestions

---

## The Three-Tier Architecture

### 1. **PSEUDO.md** - The Material Plane
- Immediate tasks (24-48 hours)
- Session tracking with IDs
- Recent achievements
- Current capabilities
- Checkable progress

### 2. **TODO-ASPIRATIONAL.md** - The Conceptual Plane
- Long-term vision (6-24 months)
- Infinite possibilities
- Strategic goals
- Community aspirations
- Hyperbolic funnel of potential

### 3. **EMPTY.md** - The Silence Plane
- Space for emergence
- Questions without answers
- Divine timing
- Non-doing as practice
- The fertile void

---

## Commands

### `bb pseudo`
**Purpose**: Display summary of immediate priorities  
**Output**: Box-drawn formatted view of:
- Where We Are Now
- Immediate Priorities
- Open Questions
- Hint for Cursor AI

**Usage**:
```bash
bb pseudo
```

**When to use**: Daily check-in, before starting work, when feeling lost

---

### `bb pseudo:audit`
**Purpose**: Full audit of both PSEUDO.md and TODO-ASPIRATIONAL.md  
**Output**: Terminal summary + `docs/PSEUDO-REPORT.md`

**Generates**:
- Completion percentages
- Philosophical entropy score
- Oldest 5 unfinished items (each file)
- Structural overview (major headings)
- Terminology consistency check
- Recent git commits
- Verification suggestions

**Usage**:
```bash
bb pseudo:audit
```

**When to use**: Weekly review, before major planning sessions, after completing sprints

**Example Output**:
```
✅ PSEUDO Audit Complete!

📊 Summary:
   PSEUDO.md:        12/47 (26%)
   ASPIRATIONAL.md:  8/156 (5%)
   Entropy:          0.647
   Terminology:      ✅

📄 Report written to: docs/PSEUDO-REPORT.md
```

---

### `bb pseudo:weekly`
**Purpose**: Alias for `pseudo:audit` (for CI/CD scheduling)  
**Usage**:
```bash
bb pseudo:weekly
```

**Recommended**: Schedule in Woodpecker CI to run every Sunday night, commit report automatically

---

### `bb pseudo:open`
**Purpose**: Display the most recent audit report  
**Output**: Full contents of `docs/PSEUDO-REPORT.md`

**Usage**:
```bash
bb pseudo:open
```

**When to use**: After running audit, to review in terminal, for quick reference

---

### `bb pseudo:verify`
**Purpose**: Check verification conditions from `VERIFICATION.md`  
**Output**: Status of formal proof conditions

**Usage**:
```bash
bb pseudo:verify
```

**When to use**: Before marking tasks complete, after essay publication, for quality assurance

---

### `bb pseudo:empty`
**Purpose**: Display `EMPTY.md` (the fertile void)  
**Output**: Full contents of silence tier

**Usage**:
```bash
bb pseudo:empty
```

**When to use**: When feeling burnt out, when forcing solutions, when needing perspective

---

## Understanding the Report

### Section 1: Snapshot & Counts

**Metrics**:
- Total tasks (checkboxes found)
- Completed (✅) vs Pending (☐)
- Progress percentage
- Philosophical entropy

**Philosophical Entropy**:
- **0.0-0.3**: Low - immediate and aspirational closely aligned
- **0.3-0.6**: Medium - healthy tension between present and future
- **0.6-1.0**: High - aspirational vision extends far beyond immediate work

**Interpretation**: 
- Low entropy can mean focus, or lack of vision
- High entropy can mean ambition, or scattered effort
- Ideal range: 0.4-0.6 (grounded aspiration)

---

### Section 2 & 3: Oldest Unfinished

**Purpose**: Surface forgotten tasks  
**Shows**: Up to 5 oldest unchecked items with line numbers

**Why it matters**: 
- Old unchecked items are either:
  - Still relevant (move to immediate)
  - No longer relevant (delete or mark complete)
  - Blocked (document blockers)

**Action**: Review each, decide fate

---

### Section 4: Structural Overview

**Purpose**: Show document organization  
**Shows**: First 10 major headings (##) from each file

**Why it matters**:
- Reveals document structure at a glance
- Helps identify missing sections
- Shows organizational coherence

---

### Section 5: Verification & Next Steps

**Terminology Check**:
- Counts "genesis" vs "regenesis"
- Ensures consistency with philosophical framework
- Flags lingering old terminology

**Recent Activity**:
- Last 10 git commits
- Shows momentum and focus areas

**Suggested Actions**:
- Concrete next steps based on audit findings

---

## Philosophical Notes

### On Self-Awareness

This system makes the project **self-reflective** without being self-absorbed. It:
- Observes without controlling
- Measures without judging
- Reports without prescribing

This mirrors Hilbert's formalism: the system describes itself formally, knowing (via Gödel) it cannot prove its own consistency.

### On Incompleteness

The audit cannot audit itself. This is by design:
- `EMPTY.md` has no checkboxes (not auditable)
- Philosophical questions have no completion criteria
- Human judgment remains essential

From PSEUDO.md:
> "PSEUDO = our approximation, not God's actual plan"

The audit makes the approximation more precise, but acknowledges it remains an approximation.

### On Flow

The audit should **enable flow**, not disrupt it:
- Run it weekly, not daily (avoid obsessive tracking)
- Use it for reflection, not self-flagellation
- Let high entropy be okay sometimes
- Celebrate deletions as much as completions

---

## Integration with Workflow

### Daily
1. Start: `bb pseudo` (check immediate priorities)
2. Work: Execute tasks, commit frequently
3. End: Review what changed

### Weekly
1. Review: `bb pseudo:audit` (generate report)
2. Reflect: `bb pseudo:open` (read findings)
3. Decide: Update PSEUDO.md with new priorities
4. Commit: `bb flow "chore: weekly PSEUDO audit + updates (session X)"`

### Monthly
1. Deep Review: Read full PSEUDO.md and TODO-ASPIRATIONAL.md
2. Prune: Delete obsolete items, mark stalled ones
3. Vision: Check if aspirational still resonates
4. Silence: `bb pseudo:empty` (remember the fertile void)

### Quarterly
1. Major Reset: Consider restructuring PSEUDO.md
2. Milestone Check: What major goals were achieved?
3. Direction: Does TODO-ASPIRATIONAL.md need updating?
4. Break: Take time away, return fresh

---

## CI/CD Integration

### Woodpecker Example

Add to `.woodpecker/weekly-audit.yml`:

```yaml
when:
  event: cron
  cron: weekly-audit

steps:
  - name: pseudo-audit
    image: babashka/babashka
    commands:
      - bb pseudo:weekly
      - git add docs/PSEUDO-REPORT.md
      - git commit -m "chore: weekly PSEUDO audit [skip ci]" || true
      - git push origin HEAD || true
```

### Cron Schedule

In Woodpecker repo settings, add cron:
- **Name**: `weekly-audit`
- **Schedule**: `0 0 * * 0` (every Sunday at midnight UTC)
- **Branch**: `coldriver-heal`

---

## Troubleshooting

### "No report found" error
**Problem**: `docs/PSEUDO-REPORT.md` doesn't exist  
**Solution**: Run `bb pseudo:audit` first

### Permission denied on script
**Problem**: `scripts/pseudo/audit.clj` not executable  
**Solution**: 
```bash
chmod +x scripts/pseudo/audit.clj
```

### High philosophical entropy
**Problem**: Entropy > 0.8  
**Interpretation**: Either:
- Aspirational vision is too disconnected from reality
- Immediate work has lost sight of the vision  
**Solution**: Review alignment, adjust either file

### Terminology inconsistent
**Problem**: Finding "genesis" instead of "regenesis"  
**Solution**: Search and replace:
```bash
grep -r "genesis" docs/ writings/ | grep -v "regenesis"
# Then manually fix each occurrence
```

---

## Extending the System

### Adding New Metrics

Edit `scripts/pseudo/audit.clj`:

```clojure
;; Example: Count TODO comments in code
(defn count-code-todos []
  (let [result (sh "grep" "-r" "TODO:" "src/" "scripts/")]
    (count (str/split-lines (:out result)))))
```

### Adding New Reports

Create new script in `scripts/pseudo/`:

```clojure
#!/usr/bin/env bb
(ns pseudo.custom-report
  (:require [clojure.string :as str]))

(defn generate-report []
  ;; Your logic here
  )

(-main)
```

Add to `bb.edn`:

```clojure
pseudo:custom {:doc "Run custom PSEUDO report"
               :task (shell "bb" "scripts/pseudo/custom-report.clj")}
```

---

## Related Documents

- **PSEUDO.md** - Immediate reality, material plane
- **TODO-ASPIRATIONAL.md** - Infinite vision, conceptual plane
- **VERIFICATION.md** - Proof conditions for completion
- **EMPTY.md** - Silence tier, fertile void
- **DEVELOPER-GUIDE.md** - Overall development workflow

---

## Meta-Notes

### This Document's Purpose

Like `VERIFICATION.md` defines proof conditions and `EMPTY.md` preserves silence, this document:
- Explains the audit system
- Documents commands and usage
- Provides troubleshooting
- Offers philosophical context

### On Self-Documentation

The PSEUDO system is **self-documenting** by design:
- `bb pseudo` shows help text
- `bb pseudo:audit` generates its own reports
- This file explains the meta-system

But it cannot document its **purpose** - that comes from the user's engagement with it.

---

**Last Updated**: 2025-10-11 (Session 7740)  
**Status**: ✅ Complete (but intentionally open to extension)  
**Philosophy**: "The best documentation is the code that documents itself. The best system is the one that knows itself." ❄️

---

*Run `bb pseudo:empty` when this document feels too structured. The fertile void awaits.* 🕊️

