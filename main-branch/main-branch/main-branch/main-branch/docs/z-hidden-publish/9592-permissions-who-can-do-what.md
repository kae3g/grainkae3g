# kae3g 9591: Permissions - Who Can Do What

**Phase 1: Foundations & Philosophy** | **Week 4** | **Reading Time: 15 minutes**

---

## What You'll Learn

- Unix permission model (read, write, execute)
- User, group, and other (the three permission classes)
- Octal notation (755, 644, etc.)
- Special permissions (setuid, setgid, sticky bit)
- How to read and modify permissions (chmod, chown, chgrp)
- Why security starts with proper permissions
- Capabilities as evolution beyond traditional permissions

---

## Prerequisites

- **[9590: The Filesystem](/12025-10/9590-filesystem-hierarchical-organization)** - File organization
- **[9570: Processes](/12025-10/9570-processes-programs-in-motion)** - Who runs what
- **[9507: Helen Atthowe](/12025-10/9507-helen-atthowe-ecological-systems)** - Access control as garden boundaries

---

## The Security Garden

**Plant lens**: Permissions are like **garden boundaries**:
- **Your garden** (owner permissions)
- **Community garden** (group permissions)
- **Public park** (other permissions)

**Who can**:
- **Enter** (read - see what's there)
- **Plant/harvest** (write - modify contents)
- **Use the tools** (execute - run programs)

**This essay**: How Unix implements these boundaries.

---

## The Permission Model

**Every file/directory** has permissions for three classes:

```bash
ls -l essay.md
# -rw-r--r-- 1 alice staff 1024 Oct 10 12:00 essay.md
#  │││ │││ │││
#  owner │ other
#      group
```

**Breaking it down**:
```
-rw-r--r--
│││ ││││││
││└ owner permissions (rw-)
│└  group permissions (r--)
└   other permissions (r--)
```

**Each class** gets 3 bits:
- **r** (read): Can see contents
- **w** (write): Can modify
- **x** (execute): Can run (for files) or enter (for directories)

---

## Permission Breakdown

### Owner (User)

**The file's owner** (usually the person who created it):

```bash
# Create file
echo "My essay" > essay.md

# Check ownership
ls -l essay.md
# -rw-r--r-- 1 alice staff 1024 Oct 10 12:00 essay.md
#              └───┘
#              owner
```

**Owner can**:
- Read file (`r`)
- Write file (`w`)
- NOT execute (`-`)

### Group

**Users can belong to groups** (e.g., `staff`, `developers`, `admin`):

```bash
# Check your groups
groups
# Output: alice staff developers

# Files owned by group 'staff'
ls -l /shared/
# -rw-rw-r-- 1 bob staff 2048 Oct 10 docs.md
#            └─────┘
#            group
```

**Group members can**:
- Read file (`r`)
- Write file (`w`)
- NOT execute (`-`)

**Use case**: Shared projects (team members can all edit).

### Other (World)

**Everyone else** (not owner, not in group):

```bash
# Public file
ls -l /var/www/index.html
# -rw-r--r-- 1 www-data www-data 4096 Oct 10 index.html
#                  │││
#                 other
```

**Other can**:
- Read file (`r`)
- NOT write (`-`)
- NOT execute (`-`)

**Use case**: Public websites (everyone can read, only owner can write).

---

## Octal Notation

**Permissions as numbers**:

```
r = 4  (binary: 100)
w = 2  (binary: 010)
x = 1  (binary: 001)
```

**Add them up**:
```
rw- = 4+2+0 = 6
r-- = 4+0+0 = 4
r-x = 4+0+1 = 5
rwx = 4+2+1 = 7
--- = 0+0+0 = 0
```

**Common patterns**:
```bash
chmod 755 script.sh
# Owner: rwx (7) - full control
# Group: r-x (5) - read and execute
# Other: r-x (5) - read and execute

chmod 644 essay.md
# Owner: rw- (6) - read and write
# Group: r-- (4) - read only
# Other: r-- (4) - read only

chmod 600 secret.key
# Owner: rw- (6) - read and write
# Group: --- (0) - no access
# Other: --- (0) - no access
```

**This is the most common system** for expressing permissions.

---

## Directory Permissions

**Directories are special**:

**Read (`r`)**: List contents
```bash
ls mydir/
# Works if you have read permission
```

**Write (`w`)**: Add/remove files
```bash
touch mydir/newfile
# Works if you have write permission
```

**Execute (`x`)**: Enter directory
```bash
cd mydir/
# Works if you have execute permission
```

**Common mistake**:
```bash
chmod 666 mydir  # rw-rw-rw-
# Can't enter! (no x bit)

cd mydir/
# Error: Permission denied

# Fix:
chmod 755 mydir  # rwxr-xr-x
cd mydir/
# Works!
```

**For directories**: `x` is **required** to access (even if `r` is set).

---

## Changing Permissions

### `chmod` (Change Mode)

**Symbolic**:
```bash
# Add execute for owner
chmod u+x script.sh

# Remove write for group
chmod g-w file.txt

# Set everyone to read-only
chmod a=r file.txt

# Multiple changes
chmod u+x,g-w,o-r file.txt
```

**Octal** (more common):
```bash
chmod 755 script.sh   # rwxr-xr-x
chmod 644 essay.md    # rw-r--r--
chmod 600 secret.key  # rw-------
```

### `chown` (Change Owner)

```bash
# Change owner
sudo chown bob file.txt

# Change owner and group
sudo chown bob:developers file.txt

# Recursive (entire directory tree)
sudo chown -R alice:staff mydir/
```

### `chgrp` (Change Group)

```bash
# Change group
chgrp staff file.txt

# Recursive
chgrp -R developers project/
```

---

## Special Permissions

### Setuid (Set User ID)

**When executed**, run as file owner (not as you):

```bash
ls -l /usr/bin/sudo
# -rwsr-xr-x 1 root wheel 123456 Oct 10 sudo
#    └─ s = setuid bit

# When YOU run sudo:
# Process runs as ROOT (file owner), not as you
# This is how sudo works!
```

**Set setuid**:
```bash
chmod u+s script.sh
# Or:
chmod 4755 script.sh  # 4 = setuid bit
```

**Security risk**: Use carefully (lets users run as someone else).

### Setgid (Set Group ID)

**For files**: Run as file's group  
**For directories**: New files inherit directory's group

```bash
# Shared directory for 'developers' group
mkdir /shared/project
chmod 2775 /shared/project  # 2 = setgid bit
chgrp developers /shared/project

# Now: Any file created inherits 'developers' group
# (Good for team collaboration!)
```

### Sticky Bit

**For directories**: Only owner can delete their files (even if others have write permission)

```bash
ls -ld /tmp
# drwxrwxrwt 10 root root 4096 Oct 10 tmp
#         └─ t = sticky bit

# Anyone can create files in /tmp
# But only file owner (or root) can delete them
```

**Set sticky bit**:
```bash
chmod +t /shared/
# Or:
chmod 1777 /shared/  # 1 = sticky bit
```

---

## Practical Security

### Principle 1: Least Privilege

**Give minimum permissions needed**:

**Bad**:
```bash
chmod 777 mydir/  # rwxrwxrwx (everyone can do anything!)
# Security risk!
```

**Good**:
```bash
chmod 755 mydir/  # rwxr-xr-x
# Owner: full control
# Others: read and execute only
```

### Principle 2: Sensitive Files

**Private keys, passwords, secrets**:

```bash
chmod 600 ~/.ssh/id_rsa  # rw-------
# Only YOU can read/write
# (SSH requires this!)
```

### Principle 3: Executable Scripts

**Scripts need execute bit**:

```bash
# Create script
echo '#!/bin/bash\necho "Hello"' > script.sh

# Not executable yet
./script.sh
# Error: Permission denied

# Add execute
chmod +x script.sh

# Now works
./script.sh
# Output: Hello
```

---

## Capabilities: Beyond Traditional Permissions

**Problem with traditional model**:
- All-or-nothing (root has ALL power, or none)
- Coarse-grained (can't say "can bind port 80, but nothing else")

**Capabilities** (modern systems):
- **Fine-grained permissions** (specific powers, not all-or-nothing)
- **Principle of least privilege** (give exactly what's needed)

**Example**:
```bash
# Give a program ONLY the power to bind low ports
# (not full root!)
setcap 'cap_net_bind_service=+ep' /usr/bin/myserver

# Now myserver can bind port 80, but:
# - Can't read other users' files
# - Can't kill other processes
# - Can't modify system files
```

**seL4** (Essay 9954) uses capabilities exclusively (no traditional permissions).

**This is the future**: Fine-grained, verifiable access control.

---

## Try This

### Exercise 1: Permission Exploration

```bash
# Check your home directory
ls -la ~

# Observe:
# - Which files are executable? (scripts, binaries)
# - Which are private? (600 - SSH keys, config)
# - Which are public? (644 - most documents)
```

---

### Exercise 2: Create Shared Directory

```bash
# Create shared space
mkdir ~/shared
chmod 770 ~/shared  # rwxrwx---

# Add group
chgrp staff ~/shared

# Now: You and 'staff' group can read/write
# Others can't access
```

---

### Exercise 3: Setuid Experiment

```bash
# Find setuid programs
find /usr/bin -perm -4000 -ls 2>/dev/null

# Common ones:
# - sudo (run as root)
# - passwd (change passwords - needs root to modify /etc/shadow)
# - ping (needs raw sockets - historically needed root)
```

**Observe**: Most setuid programs are owned by root (security-critical!).

---

## Going Deeper

### Related Essays
- **[9590: Filesystem](/12025-10/9590-filesystem-hierarchical-organization)** - File structure
- **[9570: Processes](/12025-10/9570-processes-programs-in-motion)** - Who runs programs
- **[9954: seL4 Microkernel](/12025-10/9954-sel4-verified-microkernel)** - Capability-based security
- **[9507: Helen Atthowe](/12025-10/9507-helen-atthowe-ecological-systems)** - Boundaries as garden management

### External Resources
- **`man chmod`** - Change mode (permissions)
- **`man capabilities`** - Linux capabilities (7)
- **"Unix and Linux System Administration Handbook"** - Security chapter
- **SELinux, AppArmor** - Mandatory access control (beyond permissions)

---

## Reflection Questions

1. **Is 777 ever justified?** (When would you give everyone full access?)

2. **Why does SSH require 600 on private keys?** (Security - others reading your key = compromised!)

3. **Could permissions be more fine-grained by default?** (Capabilities say yes - but complexity trade-off)

4. **Should all security be capabilities?** (seL4 thinks so - but learning curve vs traditional)

5. **How do you audit permissions across 10,000 files?** (Scripts, automation, declarative systems like Nix)

---

## Summary

**Unix Permission Model**:
- **Three classes**: Owner, group, other
- **Three permissions**: Read (4), write (2), execute (1)
- **Octal notation**: 755, 644, 600, etc.

**Key Commands**:
- **`chmod`**: Change permissions (`chmod 755 file`)
- **`chown`**: Change owner (`chown alice file`)
- **`chgrp`**: Change group (`chgrp staff file`)

**Special Permissions**:
- **Setuid (4)**: Run as file owner (security risk!)
- **Setgid (2)**: Run as file group, or inherit group for directories
- **Sticky bit (1)**: Only owner can delete (good for `/tmp/`)

**Security Principles**:
- **Least privilege**: Minimum permissions needed
- **Sensitive files**: 600 (private keys, secrets)
- **Executable scripts**: Add `+x` bit
- **Shared directories**: Use groups + setgid

**Key Insights**:
- **Permissions are boundaries** (who can access what)
- **Coarse but effective** (three classes × three permissions = simple)
- **Capabilities are finer** (future - give exact powers needed)
- **Security via access control** (can't attack what you can't access)

**In the Valley**:
- **We set permissions carefully** (least privilege always)
- **We use groups for collaboration** (shared projects)
- **We protect secrets** (600 for private keys, config)
- **We look toward capabilities** (seL4, fine-grained future)

**Plant lens**: **"Permissions are garden boundaries—who can enter (read), who can plant (write), who can use the tools (execute)."**

---

**Next**: We'll explore **networking basics**—how processes communicate across machines, the foundation of distributed systems and the internet itself!

---

**Navigation**:  
← Previous: [9591 (filesystem hierarchical organization)](/12025-10/9591-filesystem-hierarchical-organization) | **Phase 1 Index** | Next: [9593 (networking basics sockets protocols)](/12025-10/9593-networking-basics-sockets-protocols)

**Metadata**:
- **Phase**: 1 (Foundations)
- **Week**: 4
- **Prerequisites**: 9590, 9570, 9507
- **Concepts**: Unix permissions, chmod, chown, setuid, setgid, sticky bit, capabilities, least privilege
- **Next Concepts**: Networking, sockets, TCP/IP, protocols
- **Plant Lens**: Garden boundaries (access control), who can enter/plant/use tools



---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*