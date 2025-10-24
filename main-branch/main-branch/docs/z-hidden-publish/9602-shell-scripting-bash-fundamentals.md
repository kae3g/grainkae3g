# kae3g 9601: Shell Scripting Fundamentals - Variables, Conditionals, Loops

**Phase 2: Core Systems & Tools** | **Week 6** | **Reading Time: 16 minutes**

---

## Welcome to Phase 2! 🎉

**Congratulations** on completing Phase 1! You now understand the **foundations** of computing systems. Phase 2 builds on that knowledge by teaching you **practical mastery** of the tools and systems that power modern infrastructure.

**We start** with the **shell** - your most powerful interface to Unix systems.

---

## What You'll Learn

- Why shell scripting is essential (automation!)
- Variables and quoting rules
- Conditionals (if/then/else)
- Loops (for, while, until)
- Exit codes and error handling
- Best practices for robust scripts
- When to use shell vs other languages

---

## Prerequisites

- **[9550: The Command Line](/12025-10/9550-command-line-your-primary-interface)** - Basic shell usage
- **[9560: Text Files](/12025-10/9560-text-files-universal-format)** - Understanding plain text
- **[9570: Processes](/12025-10/9570-processes-programs-in-motion)** - How programs run

---

## Why Shell Scripting?

**The Unix philosophy** (Essay 9510): "Do one thing well" + "Compose tools"

**Shell scripts** are the **glue** that combines simple tools into powerful workflows.

### What Shell Scripts Do Best

**Automation**:
```bash
#!/bin/bash
# Deploy script - runs every time we ship
./run-tests.sh
./build-artifacts.sh
./upload-to-server.sh
./restart-services.sh
```

**System Administration**:
```bash
# Check disk space, clean up if needed
if [ $(df / | tail -1 | awk '{print $5}' | sed 's/%//') -gt 90 ]; then
    echo "Disk almost full! Cleaning..."
    find /tmp -type f -mtime +7 -delete
fi
```

**Data Processing**:
```bash
# Process logs: extract errors, count, email report
grep ERROR /var/log/app.log | \
    cut -d' ' -f3 | \
    sort | uniq -c | \
    mail -s "Error Report" admin@example.com
```

**Quick Prototypes**:
- Test an idea in 5 minutes
- Iterate rapidly
- Replace with "real" language later (if needed!)

---

## Your First Script

### The Shebang

```bash
#!/bin/bash
# This is a comment

echo "Hello, Valley Builder!"
```

**First line** (`#!/bin/bash`): **Shebang**
- Tells OS which interpreter to use
- `/bin/bash` = Bash shell
- Alternative: `#!/bin/sh` (POSIX shell, more portable)

**Make it executable**:
```bash
chmod +x hello.sh
./hello.sh
# Output: Hello, Valley Builder!
```

---

## Variables

### Basic Assignment

```bash
name="Ada Lovelace"
year=1842

echo "Hello, $name!"
echo "The year is $year"
```

**Rules**:
- No spaces around `=`
- Use `$` to access value
- Quotes recommended for strings

### Quoting

**Three types**:

```bash
# Single quotes: Literal (no expansion)
echo 'My name is $name'
# Output: My name is $name

# Double quotes: Variable expansion
echo "My name is $name"
# Output: My name is Ada Lovelace

# No quotes: Word splitting + globbing
files=$HOME/*.txt
echo $files
# Output: /home/user/file1.txt /home/user/file2.txt
```

**Best practice**: Always quote variables!

```bash
# BAD (breaks on spaces)
file=$HOME/My Documents/file.txt
cat $file  # ERROR: tries to cat 3 files

# GOOD
file="$HOME/My Documents/file.txt"
cat "$file"  # Works!
```

### Command Substitution

**Run command, capture output**:

```bash
# Old style (deprecated)
current_user=`whoami`

# New style (preferred)
current_user=$(whoami)
current_date=$(date +%Y-%m-%d)

echo "User: $current_user"
echo "Date: $current_date"
```

---

## Conditionals

### if/then/else

```bash
#!/bin/bash

age=25

if [ $age -ge 18 ]; then
    echo "Adult"
else
    echo "Minor"
fi
```

**Syntax**:
- `if [ condition ]; then`
- `[ ]` is the `test` command
- `-ge` = "greater than or equal"
- `fi` = "if" backwards (closes block)

### Test Operators

**Numeric**:
```bash
[ $a -eq $b ]  # Equal
[ $a -ne $b ]  # Not equal
[ $a -lt $b ]  # Less than
[ $a -le $b ]  # Less than or equal
[ $a -gt $b ]  # Greater than
[ $a -ge $b ]  # Greater than or equal
```

**String**:
```bash
[ "$a" = "$b" ]   # Equal (use = not ==!)
[ "$a" != "$b" ]  # Not equal
[ -z "$a" ]       # Empty string
[ -n "$a" ]       # Not empty
```

**File**:
```bash
[ -e "$file" ]  # Exists
[ -f "$file" ]  # Regular file
[ -d "$file" ]  # Directory
[ -r "$file" ]  # Readable
[ -w "$file" ]  # Writable
[ -x "$file" ]  # Executable
```

### Multiple Conditions

```bash
# AND (both must be true)
if [ $age -ge 18 ] && [ $country = "USA" ]; then
    echo "Can vote in USA"
fi

# OR (either can be true)
if [ $age -lt 13 ] || [ $age -gt 65 ]; then
    echo "Discounted ticket"
fi

# NOT
if [ ! -f "$file" ]; then
    echo "File does not exist"
fi
```

### elif (else if)

```bash
#!/bin/bash

score=85

if [ $score -ge 90 ]; then
    grade="A"
elif [ $score -ge 80 ]; then
    grade="B"
elif [ $score -ge 70 ]; then
    grade="C"
else
    grade="F"
fi

echo "Grade: $grade"
```

---

## Loops

### for Loop

**Iterate over list**:

```bash
#!/bin/bash

# Loop over words
for fruit in apple banana cherry; do
    echo "I like $fruit"
done

# Loop over files
for file in *.txt; do
    echo "Processing $file"
    wc -l "$file"
done

# Loop over numbers (Bash-specific)
for i in {1..5}; do
    echo "Count: $i"
done
```

**C-style for loop** (Bash):

```bash
for ((i=0; i<10; i++)); do
    echo "Number: $i"
done
```

### while Loop

**Repeat while condition true**:

```bash
#!/bin/bash

count=1

while [ $count -le 5 ]; do
    echo "Count: $count"
    count=$((count + 1))
done
```

**Read file line by line**:

```bash
#!/bin/bash

while IFS= read -r line; do
    echo "Line: $line"
done < input.txt
```

**Explanation**:
- `IFS=` preserves whitespace
- `read -r` disables backslash escaping
- `< input.txt` redirects file to stdin

### until Loop

**Repeat until condition true** (opposite of while):

```bash
#!/bin/bash

count=1

until [ $count -gt 5 ]; do
    echo "Count: $count"
    count=$((count + 1))
done
```

**Use case**: Retry until success

```bash
#!/bin/bash

until ping -c 1 example.com &> /dev/null; do
    echo "Waiting for network..."
    sleep 1
done

echo "Network is up!"
```

---

## Exit Codes

**Every command** returns an **exit code**:
- `0` = success
- Non-zero = failure

**Check last exit code**:

```bash
ls /nonexistent
echo $?  # Prints: 2 (error)

ls /home
echo $?  # Prints: 0 (success)
```

### Using Exit Codes

```bash
#!/bin/bash

if grep "ERROR" /var/log/app.log; then
    echo "Found errors in log"
    exit 1  # Signal failure
else
    echo "No errors found"
    exit 0  # Signal success
fi
```

### Short-Circuit Operators

**`&&`** = "and then" (run if previous succeeded):

```bash
cd /tmp && rm tempfile
# Only runs rm if cd succeeds
```

**`||`** = "or else" (run if previous failed):

```bash
mkdir /var/myapp || exit 1
# Exit if mkdir fails
```

**Combine**:

```bash
cd /project && make && make test && echo "Success!"
# Each step must succeed
```

---

## Functions

**Define reusable code**:

```bash
#!/bin/bash

# Define function
greet() {
    echo "Hello, $1!"
}

# Call function
greet "Alice"
greet "Bob"
```

**With return value**:

```bash
#!/bin/bash

is_even() {
    local num=$1
    if [ $((num % 2)) -eq 0 ]; then
        return 0  # True (success)
    else
        return 1  # False (failure)
    fi
}

# Use in conditional
if is_even 4; then
    echo "4 is even"
fi

if is_even 7; then
    echo "7 is even"
else
    echo "7 is odd"
fi
```

**Note**: `return` sets exit code, not value. To return a value, use `echo`:

```bash
add() {
    echo $(($1 + $2))
}

result=$(add 3 5)
echo "3 + 5 = $result"
```

---

## Error Handling

### set -e (Exit on Error)

```bash
#!/bin/bash
set -e  # Exit immediately if any command fails

cd /project
make
make test
make install

echo "All steps succeeded!"
```

**Without `set -e`**: Script continues even if `make` fails!

### set -u (Error on Undefined Variable)

```bash
#!/bin/bash
set -u  # Exit if using undefined variable

echo $UNDEFINED_VAR  # ERROR: unbound variable
```

**Best practice**: Start scripts with:

```bash
#!/bin/bash
set -euo pipefail
```

**Explanation**:
- `-e`: Exit on error
- `-u`: Error on undefined variable
- `-o pipefail`: Pipe fails if any command fails

### trap (Cleanup on Exit)

```bash
#!/bin/bash

cleanup() {
    echo "Cleaning up..."
    rm -f /tmp/tempfile
}

trap cleanup EXIT  # Run cleanup on exit

# Script continues...
touch /tmp/tempfile
# ... do work ...

# cleanup() runs automatically on exit (success or failure!)
```

---

## Try This

### Exercise 1: Backup Script

**Write a script** that:
- Takes a directory path as argument
- Creates a timestamped backup (tar.gz)
- Stores in `~/backups/`

```bash
#!/bin/bash
set -euo pipefail

# Check argument
if [ $# -ne 1 ]; then
    echo "Usage: $0 <directory>"
    exit 1
fi

source_dir="$1"
backup_dir="$HOME/backups"
timestamp=$(date +%Y%m%d_%H%M%S)
backup_file="$backup_dir/backup_$timestamp.tar.gz"

# Create backup directory
mkdir -p "$backup_dir"

# Create backup
echo "Backing up $source_dir..."
tar -czf "$backup_file" "$source_dir"

echo "Backup created: $backup_file"
```

---

### Exercise 2: System Monitor

**Write a script** that:
- Checks CPU usage
- Checks disk usage
- Checks memory usage
- Alerts if any > 80%

```bash
#!/bin/bash
set -euo pipefail

# Get metrics
cpu_usage=$(top -bn1 | grep "Cpu(s)" | awk '{print $2}' | cut -d'%' -f1)
disk_usage=$(df / | tail -1 | awk '{print $5}' | sed 's/%//')
mem_usage=$(free | grep Mem | awk '{printf "%.0f", $3/$2 * 100}')

echo "CPU: ${cpu_usage}%"
echo "Disk: ${disk_usage}%"
echo "Memory: ${mem_usage}%"

# Check thresholds
threshold=80

if (( $(echo "$cpu_usage > $threshold" | bc -l) )); then
    echo "WARNING: High CPU usage!"
fi

if [ $disk_usage -gt $threshold ]; then
    echo "WARNING: High disk usage!"
fi

if [ $mem_usage -gt $threshold ]; then
    echo "WARNING: High memory usage!"
fi
```

---

### Exercise 3: Batch Rename

**Write a script** that:
- Renames all `.txt` files in current directory
- Changes spaces to underscores
- Converts to lowercase

```bash
#!/bin/bash
set -euo pipefail

for file in *.txt; do
    # Skip if no files match
    [ -e "$file" ] || continue
    
    # Transform filename
    new_name=$(echo "$file" | tr ' ' '_' | tr '[:upper:]' '[:lower:]')
    
    # Rename if different
    if [ "$file" != "$new_name" ]; then
        mv -v "$file" "$new_name"
    fi
done

echo "Batch rename complete!"
```

---

## Best Practices

### 1. Always Quote Variables

```bash
# BAD
cat $file

# GOOD
cat "$file"
```

**Why**: Prevents word splitting and globbing.

### 2. Use Meaningful Names

```bash
# BAD
x=10
f=$1

# GOOD
max_retries=10
config_file=$1
```

### 3. Check Arguments

```bash
#!/bin/bash

if [ $# -lt 1 ]; then
    echo "Usage: $0 <filename>"
    exit 1
fi

file="$1"
```

### 4. Use `set -euo pipefail`

**Catch errors early**, don't continue on failure.

### 5. Provide Help

```bash
#!/bin/bash

if [ "$1" = "-h" ] || [ "$1" = "--help" ]; then
    cat << EOF
Usage: $0 [OPTIONS] <file>

Options:
  -h, --help     Show this help
  -v, --verbose  Verbose output

Examples:
  $0 myfile.txt
  $0 -v myfile.txt
EOF
    exit 0
fi
```

### 6. Log Actions

```bash
#!/bin/bash

log() {
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] $*"
}

log "Starting backup..."
# ... do backup ...
log "Backup complete!"
```

---

## When NOT to Use Shell

**Shell is great for**:
- System administration
- Gluing tools together
- Quick automation
- Text processing (with sed/awk/grep)

**Shell is bad for**:
- Complex logic (use Python, Ruby, etc.)
- Data structures (arrays of arrays, hash maps)
- Performance-critical code
- Portability (Bash vs sh vs zsh...)

**Rule of thumb**: If script > 100 lines, consider a "real" language.

---

## Going Deeper

### Related Essays
- **[9550: Command Line](/12025-10/9550-command-line-your-primary-interface)** - Basic shell usage
- **[9603: Shell Text Processing](/12025-10/9603-shell-text-processing-grep-sed-awk)** - grep, sed, awk (Coming Soon!)
- **[9603: Shell Functions](/12025-10/9603-shell-functions-modularity)** - Reusable scripts (Coming Soon!)

### External Resources
- **"Classic Shell Scripting"** - Robbins & Beebe (definitive guide)
- **ShellCheck** - `shellcheck.net` (linter for shell scripts!)
- **Bash Guide** - `mywiki.wooledge.org/BashGuide`
- **Advanced Bash Scripting Guide** - TLDP

---

## Reflection Questions

1. **Why prefer shell over Python** for system tasks? (Speed to write, installed everywhere)

2. **When is quoting critical?** (Filenames with spaces, preventing injection attacks)

3. **Why `set -e` controversial?** (Some argue it hides errors in conditionals—use carefully!)

4. **Should shell scripts have tests?** (YES! Use BATS or shUnit2)

5. **How does shell relate to Nock?** (Both are minimal, composable—shell glues Unix, Nock specifies computation!)

---

## Summary

**Shell scripting is**:
- **Automation** (deploy, backup, monitor)
- **Composition** (combine simple tools)
- **Speed** (prototype in minutes)
- **Universal** (every Unix system has a shell)

**Core concepts**:
- **Variables**: `name="value"`, always quote: `"$name"`
- **Conditionals**: `if [ condition ]; then ... fi`
- **Loops**: `for item in list; do ... done`, `while [ condition ]; do ... done`
- **Exit codes**: `0` = success, `$?` = last exit code
- **Functions**: Reusable code blocks
- **Error handling**: `set -euo pipefail`, `trap cleanup EXIT`

**Best practices**:
- Quote variables
- Check arguments
- Use meaningful names
- Provide help (`-h`)
- Log actions
- Exit on error (`set -e`)

**When to use**:
- System administration ✅
- Quick automation ✅
- Text processing ✅
- Complex logic ❌ (use Python/Ruby/etc.)

**In the Valley**:
- **Shell is glue** (combines tools, like Unix philosophy!)
- **Simple, composable** (relates to Nock, functional programming)
- **Ecological lens**: "Shell scripts are the mycelium network—connecting distinct organisms (tools) into a functioning ecosystem."

---

**Next**: **Essay 9603 - Shell Text Processing!** We'll master `grep`, `sed`, and `awk`—the power trio for text manipulation!

---

**Navigation**:  
← Previous: [9600 (Phase 1 Synthesis)](/12025-10/9600-phase-1-synthesis-foundations-laid) | **Phase 2 Index** | Next: [9603 (Shell Text Processing)](/12025-10/9603-shell-text-processing-grep-sed-awk) *(New!)*

**Metadata**:
- **Phase**: 2 (Core Systems & Tools)
- **Week**: 6 (Shell Scripting)
- **Prerequisites**: 9550, 9560, 9570
- **Concepts**: Variables, quoting, conditionals, loops, exit codes, functions, error handling
- **Next Concepts**: grep, sed, awk, text processing pipelines
- **Plant Lens**: Shell scripts as mycelium (connecting ecosystem), glue for composition
- **Hands-On**: 3 exercises (backup, monitor, batch rename)



---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*