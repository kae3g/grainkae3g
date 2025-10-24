# Append-Only Repository Rule

**Status**: Active  
**Effective Date**: 2025-01-22  
**Authority**: Grain Network Core Team (`kae3g`)  
**Enforcement**: GrainCI + Git Hooks

---

## Policy Statement

The `grainkae3g` repository and all Grain Network repositories SHALL operate under an **append-only** principle, where modifications to existing files are only permitted when the original version is preserved through immutable, timestamped, URL-safe paths following the Grainclay specification.

---

## Core Principles

### 1. Immutability by Default

Once a file is committed to the repository, its content at that specific revision becomes **immutable** in the historical record.

### 2. Append-Only Modifications

To modify a file, the following process MUST be followed:

```
1. Copy current file to timestamped immutable path
2. Create Grainclay beam reference
3. Modify the original file
4. Commit both the timestamped copy and the modification
```

### 3. Grainclay Path Specification

Timestamped immutable paths MUST follow this format:

```
{original-directory}/.history/{filename}-{timestamp}.{extension}
```

Where:
- `{timestamp}` = ISO 8601 format with URL-safe characters: `YYYY-MM-DDTHH-MM-SS`
- All characters MUST be URL-safe (alphanumeric, hyphen, underscore, period)
- No spaces, special characters, or non-ASCII characters

**Example**:

```
Original:    docs/PSEUDO.md
Immutable:   docs/.history/PSEUDO-2025-01-22T10-30-45.md
```

---

## Implementation

### Git Pre-Commit Hook

```bash
#!/bin/bash
# .git/hooks/pre-commit

# Check for modified files (excluding new files)
MODIFIED_FILES=$(git diff --cached --name-only --diff-filter=M)

for file in $MODIFIED_FILES; do
  # Skip if file is already in .history
  if [[ $file == *".history/"* ]]; then
    continue
  fi
  
  # Get directory and filename
  dir=$(dirname "$file")
  filename=$(basename "$file")
  
  # Create timestamp (URL-safe ISO 8601)
  timestamp=$(date -u +"%Y-%m-%dT%H-%M-%S")
  
  # Create .history directory
  mkdir -p "$dir/.history"
  
  # Copy current version to immutable path
  immutable_path="$dir/.history/${filename%.*}-${timestamp}.${filename##*.}"
  
  # Get the current committed version (not working tree)
  git show HEAD:"$file" > "$immutable_path"
  
  # Add to git
  git add "$immutable_path"
  
  # Create Grainclay beam reference
  beam_file="$dir/.history/${filename%.*}-${timestamp}.beam.edn"
  echo "{:clay-ship \"kae3g\"
 :clay-desk \"main\"
 :clay-revision \"$timestamp\"
 :clay-path [\"${dir//\//\" \"}\" \"$filename\"]
 :original-file \"$file\"
 :immutable-copy \"$immutable_path\"
 :timestamp \"$timestamp\"}" > "$beam_file"
  
  git add "$beam_file"
  
  echo "✅ Preserved: $file → $immutable_path"
done

echo "✨ Append-only rule enforced!"
```

### Babashka Helper Script

```clojure
#!/usr/bin/env bb

(ns append-only
  (:require [clojure.java.shell :as shell]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [babashka.fs :as fs]))

(defn timestamp-now
  "Generate URL-safe ISO 8601 timestamp."
  []
  (-> (java.time.Instant/now)
      str
      (str/replace #":" "-")
      (str/replace #"\.\d+Z" "")))

(defn create-immutable-copy
  "Create immutable copy of file with Grainclay beam."
  [file-path]
  (let [file (io/file file-path)
        dir (.getParent file)
        filename (.getName file)
        [name ext] (str/split filename #"\." 2)
        timestamp (timestamp-now)
        history-dir (str dir "/.history")
        immutable-path (str history-dir "/" name "-" timestamp "." ext)
        beam-path (str history-dir "/" name "-" timestamp ".beam.edn")]
    
    ;; Create .history directory
    (fs/create-dirs history-dir)
    
    ;; Copy file to immutable path
    (fs/copy file-path immutable-path)
    
    ;; Create Grainclay beam reference
    (spit beam-path
          (pr-str {:clay-ship "kae3g"
                   :clay-desk "main"
                   :clay-revision timestamp
                   :clay-path (str/split dir #"/")
                   :original-file file-path
                   :immutable-copy immutable-path
                   :timestamp timestamp}))
    
    (println "✅ Created immutable copy:")
    (println "   Original:" file-path)
    (println "   Immutable:" immutable-path)
    (println "   Beam:" beam-path)
    
    {:original file-path
     :immutable immutable-path
     :beam beam-path}))

(defn modify-file
  "Modify a file following append-only rule."
  [file-path new-content]
  
  ;; 1. Create immutable copy
  (create-immutable-copy file-path)
  
  ;; 2. Modify original
  (spit file-path new-content)
  
  (println "✅ Modified file (append-only):" file-path))

;; CLI
(let [args *command-line-args*]
  (case (first args)
    "copy" (create-immutable-copy (second args))
    "modify" (modify-file (second args) (slurp (nth args 2)))
    (println "Usage: ./append-only.bb [copy|modify] <file>")))
```

---

## Exceptions

### Permitted Without Immutable Copy

The following file types are EXEMPT from the append-only rule:

1. **Generated Files**:
   - Build artifacts
   - Compiled binaries
   - Auto-generated documentation

2. **Temporary Files**:
   - `.tmp` files
   - Cache directories
   - Log files

3. **Configuration Files** (with approval):
   - `.gitignore`
   - CI/CD configs (with rationale in commit message)

4. **Files in `.history/` directories**:
   - Already immutable by definition

### Emergency Override

In case of security vulnerabilities or legal requirements, files MAY be modified without creating immutable copies if:

1. Approved by repository admin (`kae3g`)
2. Documented in `docs/policies/OVERRIDE-LOG.md`
3. Includes detailed rationale
4. Notifies all contributors

---

## Grainclay Beam Structure

Every immutable copy MUST have an associated `.beam.edn` file:

```clojure
{:clay-ship "kae3g"                    ; Repository owner
 :clay-desk "main"                     ; Branch/workspace
 :clay-revision "2025-01-22T10-30-45"  ; Timestamp
 :clay-path ["docs" "PSEUDO.md"]       ; File path as vector
 :original-file "docs/PSEUDO.md"       ; Current working file
 :immutable-copy "docs/.history/PSEUDO-2025-01-22T10-30-45.md"
 :timestamp "2025-01-22T10-30-45"
 :commit-hash "abc123..."              ; Git commit hash (optional)
 :author "kae3g"                       ; Who made the change
 :reason "Major architecture update"}   ; Why modified (optional)
```

---

## Benefits

### 1. Complete History

Every version of every file is preserved with precise timestamps.

### 2. URL-Safe Paths

All paths can be used in web URLs, Grainclay network, and file systems.

### 3. Git-Compatible

Works seamlessly with Git's version control.

### 4. Auditable

Full audit trail of all changes with metadata.

### 5. Recoverable

Any previous version can be restored instantly.

### 6. Clay-Compatible

Integrates with Urbit Clay filesystem concepts.

---

## Workflow Examples

### Example 1: Modifying PSEUDO.md

```bash
# Current state
docs/PSEUDO.md

# Make changes
./scripts/append-only.bb copy docs/PSEUDO.md
# Creates: docs/.history/PSEUDO-2025-01-22T10-30-45.md
#          docs/.history/PSEUDO-2025-01-22T10-30-45.beam.edn

# Now edit the file
vim docs/PSEUDO.md

# Commit both
git add docs/PSEUDO.md docs/.history/
git commit -m "Update PSEUDO.md with new architecture"
```

### Example 2: Bulk Migration

```bash
# Migrate existing files to append-only
for file in docs/*.md; do
  ./scripts/append-only.bb copy "$file"
done

git add .
git commit -m "Initialize append-only rule for all docs"
```

### Example 3: Viewing History

```bash
# List all versions of a file
ls docs/.history/PSEUDO-*

# View specific version
cat docs/.history/PSEUDO-2025-01-22T10-30-45.md

# Read Grainclay beam
cat docs/.history/PSEUDO-2025-01-22T10-30-45.beam.edn
```

---

## Directory Structure

```
grainkae3g/
├── docs/
│   ├── PSEUDO.md                              # Current version
│   ├── .history/
│   │   ├── PSEUDO-2025-01-22T10-30-45.md     # Immutable copy 1
│   │   ├── PSEUDO-2025-01-22T10-30-45.beam.edn
│   │   ├── PSEUDO-2025-01-23T14-15-30.md     # Immutable copy 2
│   │   ├── PSEUDO-2025-01-23T14-15-30.beam.edn
│   │   └── ...
│   └── ...
├── scripts/
│   └── append-only.bb                         # Helper script
├── .git/
│   └── hooks/
│       └── pre-commit                         # Enforcement hook
└── ...
```

---

## Integration with GrainCI

GrainCI will enforce the append-only rule during CI/CD:

```yaml
# .github/workflows/grainci.yml
name: GrainCI - Append-Only Enforcement

on: [push, pull_request]

jobs:
  enforce-append-only:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
        with:
          fetch-depth: 0  # Get full history
      
      - name: Check Append-Only Rule
        run: |
          # Get modified files
          MODIFIED=$(git diff --name-only HEAD~1 HEAD --diff-filter=M)
          
          for file in $MODIFIED; do
            # Skip .history files
            if [[ $file == *".history/"* ]]; then
              continue
            fi
            
            # Check if immutable copy exists
            dir=$(dirname "$file")
            filename=$(basename "$file")
            
            # Find corresponding .history entry
            if ! ls "$dir/.history/${filename%.*}"-*.* 2>/dev/null | grep -q .; then
              echo "❌ VIOLATION: $file modified without immutable copy!"
              exit 1
            fi
          done
          
          echo "✅ Append-only rule compliance verified!"
```

---

## Rationale

### Why Append-Only?

1. **Blockchain Philosophy**: Immutable ledger of all changes
2. **Clay Filesystem**: Compatibility with Urbit's versioned filesystem
3. **Accountability**: Clear audit trail of who changed what and when
4. **Recovery**: Easy rollback to any previous state
5. **Network Sync**: Grainclay can efficiently sync immutable paths
6. **Legal Compliance**: Maintain complete history for audits

### Why Not Just Use Git?

Git provides version control, but:
- Git history can be rewritten (`git rebase`, `git filter-branch`)
- Git doesn't provide file-level immutable paths
- Git doesn't integrate with Grainclay network
- Git doesn't use URL-safe timestamps
- Git doesn't automatically create Grainclay beams

---

## Maintenance

### Cleaning Old Versions

Old immutable copies can be archived (not deleted):

```bash
# Move old versions to archive
./scripts/archive-history.bb --older-than 90-days

# Creates:
# .archive/docs/.history/PSEUDO-2024-10-01T12-00-00.md
```

### Storage Management

The `.history/` directories are tracked in Git but can be:
- Excluded from certain deployments
- Stored in Git LFS for large files
- Archived to separate cold storage

---

## Enforcement

### Pre-Commit Hook Installation

```bash
# Install hook
cp scripts/append-only-hook.sh .git/hooks/pre-commit
chmod +x .git/hooks/pre-commit

# Enable globally
git config --global core.hooksPath .git/hooks
```

### CI/CD Enforcement

GrainCI will automatically:
1. Check append-only compliance
2. Verify Grainclay beam format
3. Validate timestamp format
4. Ensure URL-safe paths
5. Block non-compliant commits

---

## Future Enhancements

- [ ] Automatic compression of old `.history/` files
- [ ] Integration with IPFS for permanent archival
- [ ] Blockchain anchoring of revision hashes
- [ ] AI-powered change analysis across versions
- [ ] Grainclay network propagation of immutable paths
- [ ] Web UI for browsing file history

---

## Questions & Contact

For questions about the append-only rule:
- **Policy Owner**: `kae3g`
- **Discussion**: GitHub Discussions
- **Issues**: GitHub Issues with `policy` label

---

**Version**: 1.0.0  
**Last Updated**: 2025-01-22T10:30:45  
**Next Review**: 2025-04-22

