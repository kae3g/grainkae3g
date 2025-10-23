# Grainbarrel File Naming Convention

**Decision Document**: Why we keep `bb.edn` but use `gb` command

---

## TL;DR

- **Command**: `gb` (Grainbarrel)
- **Config Files**: `bb.edn` (Babashka requirement)
- **Scripts**: `.bb` extension (for now, may migrate to `.gb`)

---

## The Question

Should we rename all `bb.edn` files to `gb.edn` to match our `gb` command branding?

---

## The Answer: Keep `bb.edn`

**Reason**: Babashka itself looks for `bb.edn` files.

When you run `bb tasks` or `bb task-name`, Babashka searches for:
1. `bb.edn` in current directory
2. `bb.edn` in parent directories
3. Returns error if not found

Since `gb` is a **wrapper around `bb`**, it ultimately calls `bb` which needs `bb.edn`.

---

## What We Did

### ✅ **Renamed**:
- Command: `bb` → `gb` (Grainbarrel wrapper)
- User-facing branding: "Grainbarrel" everywhere

### ❌ **Did NOT Rename**:
- Config files: Still `bb.edn` (Babashka requirement)
- Internal references: Still use `bb` command to execute

### 🤔 **Future Consideration**:
- Script files: Currently `.bb`, could migrate to `.gb`
- 68 `.bb` files in repository
- Migration tool ready: `scripts/migrate-bb-to-gb.bb`

---

## File Organization

```
grainstore/module-name/
├── bb.edn              # Babashka config (required name)
├── scripts/
│   ├── task-name.bb    # Scripts (could be .gb in future)
│   └── helper.bb
└── src/
    └── module/
        └── core.clj
```

**User Experience**:
```bash
# User types:
gb module:task-name

# Under the hood:
gb wrapper → cd to module → bb task-name → reads bb.edn → runs task
```

---

## Why This Is Fine

### **Separation of Concerns**:

1. **User Interface**: `gb` command (Grain Network branding)
2. **Implementation**: `bb` + `bb.edn` (Babashka internals)

### **Analogy**:

Like how `python` is the command but config files are `pyproject.toml` or `setup.py`:
- Command: `python`
- Config: `pyproject.toml` (not `python.toml`)

Or `npm` is the command but config is `package.json`:
- Command: `npm`
- Config: `package.json` (not `npm.json`)

For us:
- Command: `gb`
- Config: `bb.edn` (required by underlying tool)

---

## Benefits of Current Approach

1. **Backward Compatible**: All existing `bb` commands still work
2. **Tool Compatibility**: Babashka tools expect `bb.edn`
3. **Editor Support**: IDEs recognize `bb.edn` for syntax highlighting
4. **Gradual Migration**: Can migrate scripts (`.bb` → `.gb`) independently
5. **Clear Separation**: User sees `gb`, internals use `bb`

---

## Future Migration Path

If we want full rebranding:

### **Option 1**: Fork Babashka as "Grainbarrel"
- Create `gb` binary that looks for `gb.edn`
- Full control, but high maintenance

### **Option 2**: Symlink approach
- `gb.edn` → `bb.edn` (symlink)
- Keep both names working

### **Option 3**: Wrapper reads both
- `gb` wrapper checks for `gb.edn` first
- Falls back to `bb.edn`
- Gradually migrate over time

### **Current Decision**: Option "Keep it Simple"
- `gb` command is the user-facing interface
- `bb.edn` is the implementation detail
- Users don't need to know about `bb.edn`

---

## Script File Extension (.bb vs .gb)

### **Current**: `.bb` extension

**Pros**:
- Editor support (syntax highlighting)
- Shebang works: `#!/usr/bin/env bb`
- Babashka community recognizes them

**Cons**:
- Not Grain-branded
- Inconsistent with `gb` command

### **Future**: Could migrate to `.gb`

**Pros**:
- Consistent branding
- Clear Grain Network identity
- Shebang could work: `#!/usr/bin/env gb`

**Cons**:
- Lose editor syntax highlighting
- Community unfamiliar
- Need to update 68 files + 781 references

**Decision**: Keep `.bb` for now, migrate when it adds value

---

## Migration Tool Available

We have `scripts/migrate-bb-to-gb.bb` ready to:

1. Rename all 68 `.bb` files → `.gb`
2. Rename all 7 `bb.edn` → `gb.edn`
3. Update 781 file references
4. Update shebang lines

**Run**:
```bash
# Preview changes (dry-run)
bb scripts/migrate-bb-to-gb.bb

# Execute migration
bb scripts/migrate-bb-to-gb.bb --execute
```

**Status**: Available but not executed (keeping `.bb` for now)

---

## Summary

**What Users See**:
- `gb` command (Grainbarrel)
- Grain Network branding
- Clean, simple interface

**What Developers See**:
- `bb.edn` config files (Babashka requirement)
- `.bb` script files (may migrate later)
- Implementation details abstracted

**Best of Both Worlds**:
- User-facing: Grain-branded `gb`
- Implementation: Proven Babashka ecosystem
- Flexibility: Can fully rebrand later if desired

---

*"gb wraps bb, but users don't need to know that - they just see Grain."*

🌾 **Grain Network** - Pragmatic branding over dogmatic renaming

