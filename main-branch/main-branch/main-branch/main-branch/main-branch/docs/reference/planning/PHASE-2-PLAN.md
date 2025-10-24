# Phase 2 Plan: Core Systems & Tools (9601-9700)

**Status**: Planning Complete | Ready to Begin  
**Timeline**: Weeks 6-15 (~100 essays)  
**Difficulty**: Intermediate  
**Prerequisites**: Phase 1 Complete ✅

---

## Overview

Phase 2 builds on the foundations of Phase 1 by diving deeper into the **practical systems and tools** that power modern computing. Where Phase 1 established *what* things are and *why* they matter, Phase 2 teaches *how* to use them masterfully.

### Core Themes

1. **Shell Mastery**: Command-line power user skills
2. **System Administration**: Init systems, service management, system configuration
3. **Kernel Internals**: How Linux (and other OSes) actually work
4. **Language Design**: Compilers, interpreters, parsers
5. **Container Technology**: Isolation, namespaces, orchestration
6. **Security Foundations**: Cryptography, threat models, secure design
7. **Network Programming**: Sockets, protocols, distributed systems basics
8. **Database Systems**: ACID, transactions, query optimization

---

## Learning Objectives

By the end of Phase 2, you will be able to:

- **Write sophisticated shell scripts** (Bash, POSIX sh)
- **Manage services** with multiple init systems (systemd, s6, OpenRC, runit)
- **Understand kernel fundamentals** (syscalls, scheduling, memory management)
- **Build simple compilers** (lexer, parser, code generator)
- **Deploy containerized applications** (Docker, podman, systemd-nspawn)
- **Apply cryptographic primitives** correctly (hashing, encryption, signatures)
- **Write network servers** (TCP, UDP, HTTP)
- **Design database schemas** and optimize queries

---

## Essay Breakdown by Week

### Week 6: Shell Scripting Deep Dive (9601-9610)

**Goal**: Master the Unix shell as a programming environment

- **9601**: Shell Scripting Fundamentals - Variables, conditionals, loops
- **9602**: Shell Text Processing - grep, sed, awk mastery
- **9603**: Shell Functions and Modularity - Reusable scripts
- **9604**: Shell Job Control - Background jobs, signals, process groups
- **9605**: Advanced Shell Patterns - Arrays, parameter expansion, here-docs
- **9606**: POSIX Shell vs Bash - Portability considerations
- **9607**: Shell Security - Input validation, injection prevention
- **9608**: Shell Testing - shUnit2, BATS (Bash Automated Testing System)
- **9609**: Shell Performance - When to use shell, when not to
- **9610**: Nix Package Manager Deep Dive - Declarative system management

**Reading Time**: ~150 minutes total

---

### Week 7: Init Systems Hands-On (9611-9620)

**Goal**: Understand and manage every major init system

- **9611**: systemd Architecture - Units, targets, dependencies
- **9612**: systemd Service Files - Writing robust services
- **9613**: systemd Timers - Cron replacement
- **9614**: systemd Journal - Logging deep dive
- **9615**: s6 Supervision - Suckless init (relates to 9952!)
- **9616**: s6-rc Service Management - Declarative dependencies
- **9617**: OpenRC Hands-On - Gentoo/Artix init system
- **9618**: runit Deep Dive - Void Linux supervision
- **9619**: Init System Comparison - When to use which
- **9620**: SixOS Revisited - Building your own system (relates to 9952-9953!)

**Reading Time**: ~170 minutes total

---

### Week 8: Kernel Fundamentals (9621-9630)

**Goal**: Understand how operating systems work internally

- **9621**: Kernel Architecture - Monolithic vs microkernel (seL4, Redox!)
- **9622**: System Calls Deep Dive - Crossing the boundary (expands 9950!)
- **9623**: Process Scheduling - CFS, real-time, priorities
- **9624**: Virtual Memory Internals - Page tables, TLB, swapping
- **9625**: Filesystem Drivers - VFS, ext4, btrfs, ZFS
- **9626**: Device Drivers - Character vs block, modules
- **9627**: Kernel Modules - Writing your first module
- **9628**: Debugging the Kernel - kdb, ftrace, eBPF
- **9629**: seL4 Deep Dive - Formally verified microkernel (expands 9954!)
- **9630**: Redox OS Deep Dive - Rust microkernel (expands 9955!)

**Reading Time**: ~180 minutes total

---

### Week 9: Compilers and Language Design (9631-9640)

**Goal**: Understand how programming languages are implemented

- **9631**: Lexical Analysis - Tokenizing source code
- **9632**: Parsing - LL, LR, PEG parsers
- **9633**: Abstract Syntax Trees - IR representation
- **9634**: Semantic Analysis - Type checking, scope resolution
- **9635**: Code Generation - Bytecode, assembly, LLVM IR
- **9636**: Optimization - Dead code, constant folding, inlining
- **9637**: Interpreters vs Compilers - Trade-offs and hybrids
- **9638**: JIT Compilation - HotSpot, GraalVM (expands 9506!)
- **9639**: Garbage Collection - Mark-sweep, generational, reference counting
- **9640**: Building a Lisp - Implement your own interpreter (connects to Nock!)

**Reading Time**: ~175 minutes total

---

### Week 10: Container Technology (9641-9650)

**Goal**: Master isolation and orchestration

- **9641**: Linux Namespaces - PID, mount, network, user, UTS, IPC
- **9642**: cgroups - Resource limits and accounting
- **9643**: chroot and pivot_root - Filesystem isolation
- **9644**: Overlay Filesystems - Union mounts, layers
- **9645**: Docker Internals - containerd, runc
- **9646**: Podman and Buildah - Daemonless containers
- **9647**: systemd-nspawn - Lightweight containers
- **9648**: NixOS Containers - Declarative isolation
- **9649**: Kubernetes Basics - Pods, services, deployments
- **9650**: Container Security - Attack surface, best practices

**Reading Time**: ~180 minutes total

---

### Week 11: Cryptography Foundations (9651-9660)

**Goal**: Apply cryptography correctly and safely

- **9651**: Cryptographic Primitives - Hash, symmetric, asymmetric
- **9652**: Hash Functions - SHA-256, BLAKE3, bcrypt for passwords
- **9653**: Symmetric Encryption - AES, ChaCha20, modes (CBC, GCM)
- **9654**: Public Key Cryptography - RSA, ECC, Diffie-Hellman
- **9655**: Digital Signatures - ECDSA, Ed25519
- **9656**: TLS/SSL - How HTTPS works
- **9657**: Certificate Authorities - PKI, trust chains
- **9658**: Key Management - Storage, rotation, HSMs
- **9659**: Cryptographic Mistakes - Common pitfalls
- **9660**: Nix + Cryptography - Verifying store paths, content-addressing

**Reading Time**: ~170 minutes total

---

### Week 12: Network Programming (9661-9670)

**Goal**: Write robust networked applications

- **9661**: Socket Programming - Bind, listen, accept, connect
- **9662**: TCP Deep Dive - Three-way handshake, flow control
- **9663**: UDP Programming - Datagram protocols
- **9664**: HTTP from Scratch - Building a simple web server
- **9665**: HTTP/2 and HTTP/3 - Modern web protocols
- **9666**: WebSockets - Real-time bidirectional communication
- **9667**: gRPC - RPC over HTTP/2
- **9668**: Network Debugging - tcpdump, Wireshark, netstat
- **9669**: Distributed Systems Basics - CAP theorem, consensus
- **9670**: P2P Networks - BitTorrent, DHT, IPFS (sovereignty!)

**Reading Time**: ~175 minutes total

---

### Week 13: Database Systems (9671-9680)

**Goal**: Understand database internals and design

- **9671**: Relational Model - Tables, keys, normalization
- **9672**: SQL Deep Dive - Joins, subqueries, CTEs
- **9673**: Indexing Strategies - B-trees, hash indexes, covering indexes
- **9674**: Query Optimization - Explain plans, statistics, cost estimation
- **9675**: Transactions and ACID - Isolation levels, two-phase commit
- **9676**: Database Internals - Storage engines, WAL, MVCC
- **9677**: NoSQL Paradigms - Key-value, document, column-family, graph
- **9678**: Datalog - Declarative queries (Datomic, Clojure connection!)
- **9679**: Embedded Databases - SQLite, DuckDB, LMDB
- **9680**: Nix + Databases - Declarative schema management

**Reading Time**: ~175 minutes total

---

### Week 14: Advanced Build Systems (9681-9690)

**Goal**: Master modern build tools and reproducibility

- **9681**: Make Mastery - Automatic variables, pattern rules, functions
- **9682**: Ninja - Google's fast build system
- **9683**: CMake - Cross-platform C/C++ builds
- **9684**: Bazel - Google's monorepo build system
- **9685**: Nix Flakes - Modern Nix interface
- **9686**: Nix Derivations - Low-level build specification
- **9687**: Cross-Compilation - Building for other architectures
- **9688**: Build Caching - ccache, sccache, Nix binary caches
- **9689**: Continuous Integration - GitHub Actions, GitLab CI
- **9690**: Reproducible Builds - Bit-for-bit determinism

**Reading Time**: ~165 minutes total

---

### Week 15: System Configuration Management (9691-9700)

**Goal**: Manage infrastructure declaratively

- **9691**: Configuration Management Philosophy - Imperative vs declarative
- **9692**: NixOS Configuration - system.nix deep dive
- **9693**: NixOS Modules - Writing reusable configurations
- **9694**: Home Manager - User environment management
- **9695**: Ansible Basics - Ad-hoc automation
- **9696**: Ansible Playbooks - Declarative system state
- **9697**: Terraform - Infrastructure as code
- **9698**: GitOps - Git as source of truth
- **9699**: Immutable Infrastructure - Servers as cattle, not pets
- **9700**: Phase 2 Synthesis - From Tools to Systems

**Reading Time**: ~170 minutes total

---

## Hands-On Projects

Each week includes practical exercises:

### Week 6 Project: Build a Deployment Script
Write a Bash script that:
- Checks system prerequisites
- Downloads and verifies software
- Installs and configures services
- Logs all actions
- Handles errors gracefully

### Week 7 Project: Create Custom Service
Package an application as:
- systemd service
- s6 service directory
- OpenRC init script
- runit run script

### Week 8 Project: Linux Kernel Module
Write a kernel module that:
- Creates a `/dev/hello` device
- Responds to read/write
- Logs to kernel buffer

### Week 9 Project: Mini Compiler
Build a compiler for a toy language that:
- Lexes and parses expressions
- Type checks
- Generates LLVM IR or bytecode

### Week 10 Project: Custom Container
Create a container from scratch:
- Without Docker
- Using namespaces, cgroups, chroot
- Run an isolated process

### Week 11 Project: Secure File Transfer
Implement a program that:
- Encrypts files (AES-256-GCM)
- Signs with Ed25519
- Transfers over network

### Week 12 Project: Chat Server
Build a TCP chat server:
- Multiple clients
- Broadcasting messages
- Proper shutdown handling

### Week 13 Project: Database from Scratch
Implement a simple key-value store:
- B-tree index
- Transactions (basic ACID)
- WAL for durability

### Week 14 Project: Reproducible Build
Package an application with Nix:
- All dependencies declared
- Cross-compilation support
- Automated tests in CI

### Week 15 Project: Deploy with NixOS
Configure a complete system:
- Web server (Nginx)
- Database (PostgreSQL)
- Application (your choice)
- All declarative in configuration.nix

---

## Integration with Phase 1

Phase 2 expands on Phase 1 concepts:

| Phase 1 Essay | Phase 2 Expansion |
|---------------|-------------------|
| 9503 (Nock) | 9640 (Building a Lisp) |
| 9504 (Clojure) | 9638 (JIT), 9678 (Datalog) |
| 9505 (House of Wisdom) | Throughout - synthesis tradition |
| 9506 (Arabic AI) | 9638 (GraalVM optimization) |
| 9507 (Helen Atthowe) | Throughout - ecological metaphors |
| 9550 (Command Line) | 9601-9610 (Shell mastery) |
| 9594 (Build Systems) | 9681-9690 (Advanced builds) |
| 9595 (Package Managers) | 9610, 9685-9686 (Nix deep dives) |

---

## Success Metrics

By end of Phase 2, you should be able to:

✅ **Pass the System Administrator Test**:
- Diagnose boot failures
- Manage services across init systems
- Debug network issues
- Secure a production system

✅ **Pass the Tool Builder Test**:
- Write maintainable shell scripts
- Build simple compilers
- Create containerized applications
- Design database schemas

✅ **Pass the Conceptual Mastery Test**:
- Explain kernel vs userspace trade-offs
- Justify cryptographic choices
- Analyze build system performance
- Evaluate infrastructure-as-code approaches

---

## What's Next

**Phase 3 (9701-9800)**: Advanced Patterns & Practices
- Functional programming deep dive
- Category theory for programmers
- Formal verification techniques
- Advanced type systems
- Concurrency patterns (actors, STM, CSP)

**Phase 4 (9801-9900)**: Specializations & Deep Dives
- Clojure ecosystem mastery
- Rust systems programming
- Nix ecosystem complete guide
- RISC-V and hardware
- Distributed systems algorithms

**Phase 5 (9901-9947)**: Synthesis & Integration
- Building complete sovereign systems
- Teaching and mentorship
- Contributing to open source
- Career paths and opportunities
- The Valley Builder's final quest

---

## Ready to Begin?

**Start here**: [Essay 9601 - Shell Scripting Fundamentals](/12025-10/9601-shell-scripting-bash-fundamentals) *(Coming Soon!)*

**Or review**: [Essay 9600 - Phase 1 Synthesis](/12025-10/9600-phase-1-synthesis-foundations-laid) ✅

---

**The foundations are laid. Now we build the structure.** 🏗️🔷🌱

