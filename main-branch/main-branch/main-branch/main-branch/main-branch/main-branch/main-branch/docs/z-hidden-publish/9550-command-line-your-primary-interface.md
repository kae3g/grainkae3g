# kae3g 9550: The Command Line - Your Primary Interface

**Phase 1: Foundations & Philosophy** | **Week 3** | **Reading Time: 14 minutes**

---

## What You'll Learn

- Why the command line is more powerful than GUIs
- Essential shell commands you'll use daily
- How to navigate the filesystem efficiently
- Shell shortcuts that save hours
- Understanding paths (absolute vs relative)
- How to compose commands for leverage
- Customizing your shell environment

---

## Prerequisites

- **[9510: Unix Philosophy](/12025-10/9510-unix-philosophy-do-one-thing-well)** - Text streams, composition
- **[9540: Types and Sets](/12025-10/9540-types-sets-mathematical-foundations)** - Understanding structure

---

## Why Command Line?

**GUIs are nice**. Point, click, done. So why learn the command line?

### 1. Power

**GUI**: Can do what designer anticipated.  
**Command line**: Can do **anything the system allows**.

**Example**:
- GUI: "Delete these 5 files" (click each, confirm each)
- Command line: `rm *.tmp` (delete all .tmp files, one command)

### 2. Automation

**GUI**: Must click repeatedly (boring, error-prone).  
**Command line**: Write script once, run forever.

```bash
#!/bin/bash
# Backup script
tar -czf backup-$(date +%Y%m%d).tar.gz ~/important-stuff/
scp backup-*.tar.gz server:/backups/
```

**Save this script**, run daily (or automate with cron). **GUIs can't do this.**

### 3. Remote Access

**GUI**: Requires graphical session (slow over network).  
**Command line**: Works over SSH (text-only, fast).

```bash
# From your laptop, control a server in Virginia:
ssh user@server.example.com
# Now you're on that server's command line
# Full control, minimal bandwidth
```

**Servers typically don't have GUIs** (waste of resources). Command line is **the** interface.

### 4. Composition

**GUI**: Each app is isolated (can't pipe Photoshop output to Excel).  
**Command line**: Everything composes (Unix philosophy!).

```bash
# Get top 10 most common words
cat essay.md | tr ' ' '\n' | sort | uniq -c | sort -rn | head -10
```

**Six programs**, working together. Try that in a GUI!

---

## Essential Commands

### Navigation

```bash
# Where am I?
pwd  # Print Working Directory
# => /Users/you/projects/valley

# List files
ls
ls -la  # Long format, include hidden files
ls -lh  # Human-readable sizes (5M instead of 5242880)

# Change directory
cd /path/to/directory  # Absolute path
cd relative/path       # Relative to current
cd ..                  # Parent directory
cd ~                   # Home directory
cd -                   # Previous directory (toggle!)
```

### File Operations

```bash
# Create
touch file.txt      # Create empty file
mkdir directory     # Create directory
mkdir -p a/b/c      # Create nested directories

# Copy
cp source.txt dest.txt
cp -r dir1/ dir2/   # Recursive (for directories)

# Move/Rename
mv old.txt new.txt
mv file.txt ../    # Move to parent directory

# Delete
rm file.txt
rm -r directory/   # Recursive (careful!)
rm -i *.txt        # Interactive (confirm each)

# View
cat file.txt       # Dump entire file
less file.txt      # Page through (q to quit)
head -20 file.txt  # First 20 lines
tail -20 file.txt  # Last 20 lines
```

### Text Processing

```bash
# Search
grep "pattern" file.txt
grep -i "pattern" file.txt  # Case-insensitive
grep -r "pattern" directory/  # Recursive

# Count
wc file.txt         # Lines, words, characters
wc -l file.txt      # Just lines

# Sort
sort file.txt
sort -n file.txt    # Numeric sort
sort -r file.txt    # Reverse

# Unique
sort file.txt | uniq       # Remove adjacent duplicates
sort file.txt | uniq -c    # Count occurrences
```

---

## Paths: Absolute vs Relative

### Absolute Paths

**Start with `/`** (root):

```bash
/Users/alice/documents/essay.txt
/etc/hosts
/var/log/system.log
```

**Always works**, regardless of current directory.

**Use when**: Scripting (scripts should work from anywhere).

### Relative Paths

**Relative to current directory**:

```bash
# If you're in /Users/alice/
documents/essay.txt   # => /Users/alice/documents/essay.txt
../bob/file.txt       # => /Users/bob/file.txt
./script.sh           # => /Users/alice/script.sh (current dir)
```

**Shorter**, but **depends on where you are**.

**Use when**: Interactive use (less typing).

### Special Paths

```bash
~        # Home directory (/Users/alice)
.        # Current directory
..       # Parent directory
-        # Previous directory (cd - to toggle)
```

---

## Shell Shortcuts (Life-Changing!)

### Tab Completion

```bash
cd /usr/lo[TAB]  # Completes to: cd /usr/local/
```

**Tab completes** filenames, commands, paths. **Use it constantly** (save 80% of typing).

### History Navigation

```bash
# Up arrow: previous command
# Down arrow: next command

# Search history:
Ctrl-R  # Reverse search
# Type "grep" → shows last grep command
# Enter to run, Ctrl-R again for older matches

# View history
history
history | grep "git commit"  # Find that commit command you ran yesterday
```

### Editing Shortcuts

```bash
Ctrl-A   # Beginning of line
Ctrl-E   # End of line
Ctrl-U   # Delete from cursor to beginning
Ctrl-K   # Delete from cursor to end
Ctrl-W   # Delete previous word
Ctrl-L   # Clear screen (like `clear` command)
```

**Master these**: Save thousands of keystrokes.

### Process Control

```bash
Ctrl-C   # Kill current process (interrupt)
Ctrl-Z   # Suspend current process
fg       # Resume suspended process (foreground)
bg       # Resume in background

# Run in background from start:
long-running-command &
```

---

## Pipes and Redirection

### Standard Streams

Every process has three streams:

```
stdin  (0): Input (keyboard by default)
stdout (1): Output (terminal by default)
stderr (2): Errors (terminal by default)
```

### Redirection

```bash
# Redirect stdout to file
ls > file-list.txt

# Append to file
echo "new line" >> log.txt

# Redirect stderr
command 2> errors.txt

# Redirect both stdout and stderr
command > output.txt 2>&1

# Redirect stdin (read from file)
sort < unsorted.txt
```

### Pipes

```bash
# Pipe stdout of command1 to stdin of command2
command1 | command2

# Chain multiple:
cat file.txt | grep "error" | wc -l
# "How many error lines?"
```

**Pipes are composition** (Unix philosophy in action).

---

## Practical Examples

### Example 1: Find Large Files

```bash
# Find files larger than 100MB
find ~ -type f -size +100M

# Sort by size (largest first)
find ~ -type f -size +100M -exec ls -lh {} \; | sort -k5 -hr
```

### Example 2: Monitor Disk Usage

```bash
# What's using space?
du -sh *  # Summary, human-readable, current directory

# Largest directories:
du -sh * | sort -hr | head -10
```

### Example 3: Quick Web Server

```bash
# Python 3: serve current directory on port 8000
python3 -m http.server 8000

# Visit: http://localhost:8000
# Instant file sharing!
```

### Example 4: Download & Extract

```bash
# Download file
curl -O https://example.com/file.tar.gz

# Extract
tar -xzf file.tar.gz

# One-liner:
curl https://example.com/file.tar.gz | tar -xz
```

---

## Shell Scripting Basics

### Your First Script

```bash
#!/bin/bash
# hello.sh

echo "Hello, $1!"  # $1 = first argument
```

**Make executable**:
```bash
chmod +x hello.sh
./hello.sh Alice  # => "Hello, Alice!"
```

### Variables

```bash
name="Alice"
echo "Hello, $name"

# Command substitution
current_date=$(date +%Y-%m-%d)
echo "Today is $current_date"
```

### Conditionals

```bash
if [ -f "file.txt" ]; then
    echo "File exists"
else
    echo "File not found"
fi
```

### Loops

```bash
# Loop over files
for file in *.txt; do
    echo "Processing $file"
    wc -l "$file"
done

# Loop over numbers
for i in {1..10}; do
    echo "Iteration $i"
done
```

---

## Customizing Your Shell

### Shell Configuration

**Files that run on startup**:

```bash
~/.bashrc    # Bash (Linux)
~/.zshrc     # Zsh (macOS default since Catalina)
~/.profile   # Login shells
```

**Add aliases** (shortcuts):

```bash
# In ~/.zshrc or ~/.bashrc:
alias ll='ls -lah'
alias gst='git status'
alias gco='git checkout'
alias reload='source ~/.zshrc'

# Now: type `ll` instead of `ls -lah`
```

### Prompt Customization

```bash
# Show current directory and git branch
export PS1='\w $(git branch 2>/dev/null | grep "^*" | cut -d " " -f2)\$ '

# Looks like:
# ~/projects/valley main$ 
```

### Environment Variables

```bash
# Set PATH (where shell looks for commands)
export PATH="$HOME/bin:$PATH"

# Now commands in ~/bin/ are available
```

---

## Try This

### Exercise 1: Navigation Workout

**Without using GUI** (Finder, Explorer):

1. Open terminal
2. Go to home directory: `cd ~`
3. List files: `ls -la`
4. Create test directory: `mkdir shell-practice`
5. Enter it: `cd shell-practice`
6. Create files: `touch file1.txt file2.txt file3.txt`
7. List them: `ls`
8. Go to parent: `cd ..`
9. Remove directory: `rm -r shell-practice`

**Goal**: Feel comfortable navigating via command line.

---

### Exercise 2: Pipeline Challenge

**Find the 5 largest files in your home directory**:

```bash
find ~ -type f -exec ls -lh {} \; 2>/dev/null | \
    sort -k5 -hr | \
    head -5
```

**Breakdown**:
- `find ~ -type f`: Find all files under home
- `-exec ls -lh {}`: Run `ls -lh` on each (human-readable sizes)
- `2>/dev/null`: Ignore errors (permission denied)
- `sort -k5 -hr`: Sort by column 5 (size), human-readable, reverse
- `head -5`: Take top 5

**Modify**: Find .txt files only, or find smallest files.

---

### Exercise 3: Create Your Alias

**Add to `~/.zshrc` or `~/.bashrc`**:

```bash
# Your custom aliases
alias projects='cd ~/projects'
alias today='date +%Y-%m-%d'
alias count='wc -l'

# Reload:
source ~/.zshrc
```

**Now**:
- `projects` takes you to ~/projects
- `today` prints current date
- `cat file.txt | count` counts lines

**Build your personal toolkit!**

---

## Advanced Patterns

### 1. Find and Replace Across Files

```bash
# Find all .md files containing "old text", replace with "new text"
find . -name "*.md" -exec sed -i '' 's/old text/new text/g' {} \;

# macOS: sed -i ''
# Linux: sed -i
```

### 2. Parallel Execution

```bash
# Process files in parallel (GNU parallel)
find . -name "*.jpg" | parallel convert {} {.}.png

# Or with xargs:
find . -name "*.jpg" | xargs -P 4 -I {} convert {} {}.png
# -P 4: use 4 parallel processes
```

### 3. Watch Command

```bash
# Run command every 2 seconds
watch -n 2 'df -h'

# Monitor disk space live
```

### 4. Process Substitution

```bash
# Compare output of two commands
diff <(ls dir1/) <(ls dir2/)

# <(...) creates temporary file with command output
```

---

## The Shell as Programming Language

**Shell** (bash, zsh, fish) is a **programming language**:

### Functions

```bash
# Define function
greet() {
    echo "Hello, $1!"
}

# Call it
greet Alice  # => "Hello, Alice!"
```

### Error Handling

```bash
# Check if command succeeded
if git pull; then
    echo "Updated successfully"
else
    echo "Pull failed"
    exit 1
fi

# Or shorthand:
git pull && echo "Success" || echo "Failed"
```

### Arrays

```bash
# Bash arrays
files=(file1.txt file2.txt file3.txt)

# Loop over array
for file in "${files[@]}"; do
    echo "Processing $file"
done
```

---

## Going Deeper

### Related Essays
- **[9510: Unix Philosophy](/12025-10/9510-unix-philosophy-do-one-thing-well)** - Foundation for command-line thinking
- **[9560: Text Files](/12025-10/9560-text-files-universal-format)** - What you're manipulating
- **[9570: Processes](/12025-10/9570-processes-programs-in-motion)** - What shell commands create
- **[9580: Filesystem](/12025-10/9580-filesystem-hierarchical-organization)** - The structure you navigate

### External Resources
- **"The Linux Command Line"** by William Shotts - Free PDF, comprehensive
- **"Data Science at the Command Line"** - Advanced shell techniques
- **explainshell.com** - Paste any command, get explanation
- **tldr pages** - Simplified man pages with examples

### For Shell Mastery
- **Fish shell** - User-friendly alternative to bash/zsh
- **Oh My Zsh** - Framework for managing zsh configuration
- **Powerlevel10k** - Beautiful, informative prompt

---

## Reflection Questions

1. **Why do developers love the command line despite its learning curve?** (Power, automation, composition)

2. **Will GUIs make command lines obsolete?** (Unlikely—they serve different needs)

3. **Is shell scripting "real programming"?** (Yes! Bash is Turing-complete)

4. **Should non-technical users learn the command line?** (Depends—but basic navigation is empowering)

5. **What's your most-used command?** (Mine: `git status`, `ls`, `cd`)

---

## Summary

**The command line**:
- **Text-based interface** to your computer
- **More powerful** than GUIs (can do anything system allows)
- **Automatable** (scripts beat repetitive clicking)
- **Remote-friendly** (SSH, low bandwidth)
- **Composable** (pipes, redirection, text streams)

**Essential commands**:
- **Navigation**: pwd, ls, cd
- **Files**: cp, mv, rm, touch, mkdir
- **Text**: cat, grep, sed, awk, wc
- **Pipes**: | for composition

**Skills**:
- **Tab completion** (save typing)
- **History search** (Ctrl-R)
- **Keyboard shortcuts** (Ctrl-A, Ctrl-E, etc.)
- **Pipes and redirection** (>, <, |)

**In the Valley**:
- **Command line is primary** (GUIs are optional)
- **Scripting is essential** (automation, reproducibility)
- **Text orientation** (everything flows through text)
- **Unix tools are our foundation** (grep, awk, sed mastery)

---

**Next**: We'll explore **text files**—the universal data format that makes the command line so powerful. Why plain text beats binary formats for durability and interoperability!

---

**Navigation**:  
← Previous: [9540 (types sets mathematical foundations)](/12025-10/9540-types-sets-mathematical-foundations) | **Phase 1 Index** | Next: [9560 (text files universal format)](/12025-10/9560-text-files-universal-format)

**Metadata**:
- **Phase**: 1 (Foundations)
- **Week**: 3
- **Prerequisites**: 9510, 9540
- **Concepts**: Shell, bash/zsh, navigation, pipes, redirection, automation, SSH
- **Next Concepts**: Text files, formats, serialization, plain text philosophy



---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*