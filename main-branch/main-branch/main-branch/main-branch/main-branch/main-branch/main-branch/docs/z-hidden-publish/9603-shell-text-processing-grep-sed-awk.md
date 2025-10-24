# kae3g 9603: Shell Text Processing - grep, sed, awk Mastery

**Phase 2: Core Systems & Tools** | **Week 6** | **Reading Time: 18 minutes**

---

## What You'll Learn

- grep: Pattern matching and searching
- sed: Stream editing and text transformation
- awk: Pattern scanning and processing language
- Combining the power trio in pipelines
- Regular expressions essentials
- Real-world text processing workflows
- When to use which tool

---

## Prerequisites

- **[9550: Command Line](/12025-10/9550-command-line-your-primary-interface)** - Pipes, redirection
- **[9560: Text Files](/12025-10/9560-text-files-universal-format)** - Plain text power
- **[9601: Shell Scripting](/12025-10/9601-shell-scripting-bash-fundamentals)** - Shell basics

---

## The Power Trio

**Unix philosophy** (Essay 9510): "Do one thing well" + "Compose tools"

**grep, sed, awk** are the **text processing champions**:

- **grep**: "Global Regular Expression Print" - **search and filter**
- **sed**: "Stream Editor" - **search and replace**
- **awk**: Pattern scanning language - **extract and compute**

**Together**: Process terabytes of logs, transform data, extract insights!

---

## grep: Search and Filter

### Basic Usage

```bash
# Search for pattern in file
grep "ERROR" /var/log/app.log

# Search in multiple files
grep "TODO" *.js

# Recursive search
grep -r "function" src/

# Case-insensitive
grep -i "warning" log.txt
```

### Common Options

```bash
# -n: Show line numbers
grep -n "ERROR" log.txt
# Output: 42:ERROR: Connection failed

# -v: Invert match (lines NOT matching)
grep -v "DEBUG" log.txt  # Exclude debug lines

# -c: Count matches
grep -c "ERROR" log.txt  # Output: 15

# -l: List files with matches
grep -l "TODO" *.js  # Output: app.js utils.js

# -A, -B, -C: Context lines
grep -A 3 "ERROR" log.txt  # Show 3 lines after
grep -B 2 "ERROR" log.txt  # Show 2 lines before
grep -C 2 "ERROR" log.txt  # Show 2 lines before and after
```

### Regular Expressions

**Basic patterns**:

```bash
# Literal string
grep "hello" file.txt

# Start of line (^)
grep "^ERROR" log.txt  # Lines starting with ERROR

# End of line ($)
grep "failed$" log.txt  # Lines ending with failed

# Any character (.)
grep "h.llo" file.txt  # Matches: hello, hallo, h3llo, etc.

# Zero or more (*)
grep "colou*r" file.txt  # Matches: color, colour

# One or more (\+, needs -E)
grep -E "10+" file.txt  # Matches: 10, 100, 1000, etc.

# Character class
grep "[0-9]" file.txt  # Lines with digits
grep "[A-Z]" file.txt  # Lines with uppercase
grep "[aeiou]" file.txt  # Lines with vowels

# Word boundary (\b, needs -E)
grep -E "\bcat\b" file.txt  # Matches "cat" but not "catch"
```

### Extended Regex (-E)

```bash
# Alternation (|)
grep -E "ERROR|FATAL" log.txt  # ERROR or FATAL

# Optional (?)
grep -E "colou?r" file.txt  # color or colour

# Groups
grep -E "(ab)+" file.txt  # ab, abab, ababab, etc.

# Exactly n times {n}
grep -E "[0-9]{3}" file.txt  # Exactly 3 digits

# Range {n,m}
grep -E "[0-9]{3,5}" file.txt  # 3 to 5 digits
```

### Real-World Examples

**Extract IP addresses**:
```bash
grep -Eo "[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}" access.log
```

**Find email addresses**:
```bash
grep -Eo "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}" users.txt
```

**Filter log levels**:
```bash
# Only ERROR and FATAL
grep -E "^(ERROR|FATAL)" log.txt

# Everything except DEBUG and INFO
grep -Ev "^(DEBUG|INFO)" log.txt
```

---

## sed: Stream Editing

### Basic Substitution

```bash
# Replace first occurrence per line
sed 's/old/new/' file.txt

# Replace all occurrences (g flag)
sed 's/old/new/g' file.txt

# Edit file in-place
sed -i 's/old/new/g' file.txt  # Linux
sed -i '' 's/old/new/g' file.txt  # macOS (requires empty string)
```

**Syntax**: `s/pattern/replacement/flags`

### Advanced Substitution

```bash
# Case-insensitive (I flag)
sed 's/error/ERROR/gI' file.txt

# Replace only on lines matching pattern
sed '/^ERROR/s/foo/bar/g' file.txt

# Delete lines
sed '/DEBUG/d' log.txt  # Delete lines with DEBUG

# Print only matching lines
sed -n '/ERROR/p' log.txt  # Like grep!

# Multiple commands
sed -e 's/foo/bar/g' -e 's/baz/qux/g' file.txt
# Or use semicolon:
sed 's/foo/bar/g; s/baz/qux/g' file.txt
```

### Capture Groups

```bash
# \1, \2 reference captured groups
sed 's/\([0-9]\{4\}\)-\([0-9]\{2\}\)-\([0-9]\{2\}\)/\3\/\2\/\1/' dates.txt
# Transform: 2025-10-10 → 10/10/2025

# Swap words
echo "hello world" | sed 's/\(.*\) \(.*\)/\2 \1/'
# Output: world hello
```

### Line Ranges

```bash
# Lines 10-20
sed -n '10,20p' file.txt

# From line 5 to end
sed -n '5,$p' file.txt

# Every 3rd line
sed -n '1~3p' file.txt

# Delete first line
sed '1d' file.txt

# Delete last line
sed '$d' file.txt
```

### Real-World Examples

**Remove comments**:
```bash
sed 's/#.*$//' config.txt  # Remove # to end of line
sed '/^$/d' config.txt  # Remove blank lines
```

**Add line numbers**:
```bash
sed = file.txt | sed 'N;s/\n/\t/'
```

**Extract between markers**:
```bash
sed -n '/START/,/END/p' file.txt
```

**Config file editing**:
```bash
# Change port in nginx config
sed -i 's/listen 80/listen 8080/' /etc/nginx/nginx.conf

# Enable a commented setting
sed -i 's/^# *\(max_connections = \)/\1/' config.ini
```

---

## awk: Pattern Scanning & Processing

### Basic Syntax

```bash
# Print entire line
awk '{print}' file.txt

# Print specific field ($1 = first, $2 = second, etc.)
awk '{print $1}' file.txt

# Print multiple fields
awk '{print $1, $3}' file.txt

# Field separator (default: whitespace)
awk -F: '{print $1}' /etc/passwd  # Use : as separator
```

### Patterns and Actions

```bash
# Pattern { action }
awk '/ERROR/ {print $0}' log.txt  # Like grep

# Only lines > 80 chars
awk 'length > 80' file.txt

# Print line number and content
awk '{print NR, $0}' file.txt

# Lines 10-20
awk 'NR>=10 && NR<=20' file.txt
```

### Built-in Variables

```bash
NR    # Current line number
NF    # Number of fields in current line
$0    # Entire line
$1    # First field
$NF   # Last field
FS    # Field separator (input)
OFS   # Output field separator
```

**Examples**:

```bash
# Print last field
awk '{print $NF}' file.txt

# Print number of fields per line
awk '{print NF}' file.txt

# Swap first and last fields
awk '{temp=$1; $1=$NF; $NF=temp; print}' file.txt
```

### Arithmetic

```bash
# Sum column
awk '{sum += $1} END {print sum}' numbers.txt

# Average
awk '{sum += $1; count++} END {print sum/count}' numbers.txt

# Min/Max
awk 'NR==1 {min=$1; max=$1} {if($1<min) min=$1; if($1>max) max=$1} END {print min, max}' numbers.txt

# Print with calculation
awk '{print $1, $2, $1*$2}' data.txt  # Print col1, col2, product
```

### Conditionals

```bash
# if/else
awk '{if ($1 > 100) print "high"; else print "low"}' numbers.txt

# Ternary operator
awk '{print ($1 > 100) ? "high" : "low"}' numbers.txt

# Multiple conditions
awk '$1 > 100 && $2 < 50 {print}' data.txt
```

### BEGIN and END

```bash
# BEGIN: Runs before processing
# END: Runs after processing

awk 'BEGIN {print "Starting..."} {print $1} END {print "Done!"}' file.txt

# CSV with header
awk 'BEGIN {FS=","; print "Name,Age"} {print $1, $2}' data.csv

# Statistics
awk 'BEGIN {count=0; sum=0} {sum+=$1; count++} END {print "Count:", count, "Average:", sum/count}' numbers.txt
```

### Real-World Examples

**Parse access logs**:
```bash
# Extract IP and status code
awk '{print $1, $9}' access.log

# Count 404 errors
awk '$9 == 404 {count++} END {print count}' access.log

# Top 10 IPs
awk '{print $1}' access.log | sort | uniq -c | sort -rn | head -10
```

**Process CSV**:
```bash
# Extract columns 1 and 3
awk -F, '{print $1, $3}' data.csv

# Filter rows where column 2 > 100
awk -F, '$2 > 100 {print}' data.csv

# Convert CSV to TSV
awk -F, '{print $1 "\t" $2 "\t" $3}' data.csv
# Or:
awk 'BEGIN {FS=","; OFS="\t"} {print}' data.csv
```

**System monitoring**:
```bash
# Disk usage over 80%
df -h | awk '$5 > 80 {print $6, $5}'

# Process memory (top 5)
ps aux | awk 'NR>1 {print $6, $11}' | sort -rn | head -5

# Network connections by state
netstat -an | awk '/^tcp/ {print $6}' | sort | uniq -c
```

---

## Combining the Power Trio

### Pipeline Patterns

**grep → sed → awk**:

```bash
# Extract errors, clean, summarize
grep ERROR log.txt | \
    sed 's/.*ERROR: //' | \
    awk '{count[$0]++} END {for (err in count) print count[err], err}' | \
    sort -rn
```

**Example breakdown**:
1. `grep ERROR`: Filter error lines
2. `sed 's/.*ERROR: //'`: Remove everything before "ERROR: "
3. `awk`: Count unique errors
4. `sort -rn`: Sort by count (reverse numeric)

### Real-World Workflows

**Apache log analysis**:

```bash
# Top 10 requested URLs
awk '{print $7}' access.log | \
    sort | \
    uniq -c | \
    sort -rn | \
    head -10
```

**Failed login attempts**:

```bash
grep "Failed password" /var/log/auth.log | \
    awk '{print $(NF-3)}' | \
    sort | \
    uniq -c | \
    sort -rn
```

**Extract and transform data**:

```bash
# From: Name: John, Age: 30
# To:   John,30

grep "Name:" data.txt | \
    sed 's/Name: \(.*\), Age: \(.*\)/\1,\2/'
```

**Generate report**:

```bash
#!/bin/bash

echo "=== System Report ==="
echo

echo "Top 5 Processes by Memory:"
ps aux | awk 'NR>1 {print $6, $11}' | sort -rn | head -5
echo

echo "Disk Usage:"
df -h | awk 'NR>1 && $5+0 > 0 {print $6, $5}'
echo

echo "Error Count (last hour):"
grep "$(date +%Y-%m-%d\ %H)" /var/log/syslog | \
    grep -c ERROR
```

---

## When to Use Which

### grep
**Best for**:
- Finding files with specific content
- Filtering log entries
- Quick pattern matching
- Binary "yes/no" searches

**Example**: "Which files contain 'TODO'?"

### sed
**Best for**:
- Search and replace
- Line deletion/insertion
- Simple transformations
- In-place file editing

**Example**: "Change all 'http' to 'https'"

### awk
**Best for**:
- Column extraction
- Arithmetic operations
- Structured data processing
- Complex logic and state

**Example**: "What's the average of column 3?"

### Combination
**Use together** for multi-stage pipelines:
1. **grep**: Filter relevant lines
2. **sed**: Clean/transform
3. **awk**: Extract and compute

---

## Try This

### Exercise 1: Log Analysis

Given `access.log`:
```
192.168.1.1 - - [10/Oct/2025:13:55:36 -0700] "GET /api/users HTTP/1.1" 200 1234
192.168.1.2 - - [10/Oct/2025:13:55:37 -0700] "GET /api/posts HTTP/1.1" 404 567
192.168.1.1 - - [10/Oct/2025:13:55:38 -0700] "POST /api/login HTTP/1.1" 200 890
```

**Tasks**:
1. Extract all IP addresses
2. Count requests by status code
3. Find all 404 errors with URLs

```bash
# 1. Extract IPs
awk '{print $1}' access.log

# 2. Count by status
awk '{print $9}' access.log | sort | uniq -c

# 3. 404 errors with URLs
awk '$9 == 404 {print $7}' access.log
```

---

### Exercise 2: Data Transformation

Given `users.csv`:
```
John,Doe,30,Engineer
Jane,Smith,25,Designer
Bob,Johnson,35,Manager
```

**Tasks**:
1. Print only first and last names
2. Find users over 30
3. Convert to JSON format

```bash
# 1. First and last names
awk -F, '{print $1, $2}' users.csv

# 2. Users over 30
awk -F, '$3 > 30 {print}' users.csv

# 3. Convert to JSON
awk -F, 'BEGIN {print "["} {printf "  {\"first\":\"%s\",\"last\":\"%s\",\"age\":%s,\"role\":\"%s\"}", $1,$2,$3,$4; if(NR<3) print ","; else print ""} END {print "]"}' users.csv
```

---

### Exercise 3: Configuration Management

Given `config.txt`:
```
# Database settings
db_host=localhost
db_port=5432
# db_user=admin
db_pass=secret123

# Server settings
server_port=8080
```

**Tasks**:
1. Remove all comments
2. Extract all uncommented settings
3. Change db_port to 5433

```bash
# 1. Remove comments
sed 's/#.*$//' config.txt | sed '/^$/d'

# 2. Uncommented settings
grep -v "^#" config.txt | grep "="

# 3. Change port
sed 's/db_port=5432/db_port=5433/' config.txt
```

---

## Best Practices

### 1. Use Appropriate Tool

```bash
# BAD: awk for simple search
awk '/ERROR/ {print}' log.txt

# GOOD: grep for search
grep ERROR log.txt

# BAD: sed for arithmetic
sed ... # complex expression

# GOOD: awk for arithmetic
awk '{sum+=$1} END {print sum}' numbers.txt
```

### 2. Quote Regular Expressions

```bash
# BAD
grep $pattern file.txt  # Shell may expand

# GOOD
grep "$pattern" file.txt
grep '$1 > 100' data.txt  # Single quotes prevent shell expansion
```

### 3. Test on Sample Data

**Never run destructive commands** on production data first!

```bash
# Test first
sed 's/old/new/g' file.txt | head

# Then apply
sed -i 's/old/new/g' file.txt
```

### 4. Use -n with sed

```bash
# BAD: prints everything twice
sed '/ERROR/p' log.txt

# GOOD: prints only matches
sed -n '/ERROR/p' log.txt
```

### 5. Escape Special Characters

**Regex special chars**: `. * [ ] ^ $ \ + ? { } | ( )`

```bash
# Search for literal dot
grep "\." file.txt

# Search for literal dollar sign
grep "\$" file.txt
```

---

## Going Deeper

### Related Essays
- **[9550: Command Line](/12025-10/9550-command-line-your-primary-interface)** - Pipes, redirection
- **[9601: Shell Scripting](/12025-10/9601-shell-scripting-bash-fundamentals)** - Scripting basics
- **[9603: Shell Functions](/12025-10/9603-shell-functions-modularity)** - Reusable text processing (Coming Soon!)

### External Resources
- **"sed & awk"** - Dale Dougherty (O'Reilly, definitive guide)
- **Regular-Expressions.info** - Comprehensive regex tutorial
- **awk manual** - `man awk` or GNU awk guide
- **"The AWK Programming Language"** - Aho, Kernighan, Weinberger

---

## Reflection Questions

1. **Why three tools instead of one?** (Unix philosophy - simple, composable)

2. **When is awk overkill?** (Simple search/replace - use grep/sed)

3. **Can awk replace Python?** (For text processing pipelines, often yes! For apps, no.)

4. **Why learn regex?** (Universal - works in grep, sed, awk, vim, Python, JavaScript, ...)

5. **How does this relate to functional programming?** (Pipelines = function composition! `grep | sed | awk` = `compose(awk, sed, grep)`)

---

## Summary

**The Power Trio**:
- **grep**: Search and filter (`grep "pattern" file`)
- **sed**: Search and replace (`sed 's/old/new/g' file`)
- **awk**: Extract and compute (`awk '{sum+=$1} END {print sum}' file`)

**grep essentials**:
- `-i`: Case-insensitive
- `-v`: Invert match
- `-n`: Line numbers
- `-r`: Recursive
- `-E`: Extended regex

**sed essentials**:
- `s/pattern/replacement/g`: Substitute
- `/pattern/d`: Delete lines
- `-n '/pattern/p'`: Print matches only
- `-i`: In-place editing

**awk essentials**:
- `{print $1}`: Print first field
- `$1 > 100 {print}`: Conditional action
- `{sum+=$1} END {print sum}`: Accumulate
- `-F,`: Set field separator

**When to use**:
- **grep**: Filter lines
- **sed**: Transform lines
- **awk**: Process columns

**Combine in pipelines**:
```bash
grep ERROR log.txt | sed 's/.*ERROR: //' | awk '{count[$0]++} END {for(e in count) print count[e], e}' | sort -rn
```

**In the Valley**:
- **Text processing = data flowing through ecosystem**
- **grep, sed, awk = different plant species (complementary niches!)**
- **Pipelines = nutrient cycles (output of one feeds input of next)**
- **Ecological lens**: "Diverse tools create resilient workflows—monoculture (one tool) is fragile, polyculture (grep+sed+awk) is robust."

---

**Next**: **Essay 9603 - Shell Functions & Modularity!** We'll learn to write reusable, maintainable shell code!

---

**Navigation**:  
← Previous: [9601 (Shell Scripting Fundamentals)](/12025-10/9601-shell-scripting-bash-fundamentals) | **Phase 2 Index** | Next: [9603 (Shell Functions & Modularity)](/12025-10/9603-shell-functions-modularity) *(Coming Soon!)*

**Metadata**:
- **Phase**: 2 (Core Systems & Tools)
- **Week**: 6 (Shell Scripting)
- **Prerequisites**: 9550, 9560, 9601
- **Concepts**: grep (search), sed (transform), awk (process), regex, pipelines
- **Next Concepts**: Shell functions, modularity, reusable code
- **Plant Lens**: Diverse tools (polyculture), nutrient cycles (pipelines), resilient workflows
- **Hands-On**: 3 exercises (log analysis, data transformation, config management)



---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*