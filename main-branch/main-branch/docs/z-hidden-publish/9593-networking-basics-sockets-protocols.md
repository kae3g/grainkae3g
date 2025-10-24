# kae3g 9592: Networking Basics - Sockets and Protocols

**Phase 1: Foundations & Philosophy** | **Week 4** | **Reading Time: 17 minutes**

---

## What You'll Learn

- How processes communicate across machines
- TCP/IP: The protocol stack that powers the internet
- Sockets: Programming interface for networking
- Client-server model vs peer-to-peer
- IP addresses, ports, and the routing system
- DNS: How names become numbers
- The OSI model (and why TCP/IP simplified it)
- Networking as rivers connecting gardens (plant lens)

---

## Prerequisites

- **[9501: What Is Compute?](/12025-10/9501-what-is-compute)** - Distributed computing, P2P
- **[9570: Processes](/12025-10/9570-processes-programs-in-motion)** - What communicates
- **[9590: Filesystem](/12025-10/9590-filesystem-hierarchical-organization)** - Local organization (networking extends globally)

---

## Networks: Rivers Between Gardens

**Filesystem** (Essay 9590): Organize data **within** one machine.

**Networking**: Connect **across** machines.

**Plant lens**: **"Networks are irrigation channels—water (data) flows between gardens (computers), nourishing the entire valley (internet)."**

**Key insight**: The internet is just **processes on different machines talking to each other**.

---

## The TCP/IP Stack

**Internet uses layers** (each handles different concern):

```
Application Layer (HTTP, SSH, DNS)
    ↓ "What to send"
Transport Layer (TCP, UDP)
    ↓ "How to send reliably"
Internet Layer (IP)
    ↓ "Where to send"
Link Layer (Ethernet, WiFi)
    ↓ "Physical transmission"
```

**Each layer** talks to its peer on the other machine:

```
Your Computer                  Remote Computer
    App (HTTP)  ←→ HTTP ←→        App (HTTP)
    TCP         ←→ TCP  ←→        TCP
    IP          ←→ IP   ←→        IP
    Ethernet    ←→ ...  ←→        Ethernet
```

**This is composition** (Essay 9510 - Unix philosophy). Each layer does one thing well.

---

## IP Addresses: Finding Machines

**Every machine** on the internet has an IP address:

**IPv4** (old, but still dominant):
```
192.168.1.100  (4 numbers, 0-255 each)

Private ranges (not routable on internet):
10.0.0.0 - 10.255.255.255
172.16.0.0 - 172.31.255.255
192.168.0.0 - 192.168.255.255

Public: Everything else (globally unique)
```

**IPv6** (new, larger address space):
```
2001:0db8:85a3:0000:0000:8a2e:0370:7334

Abbreviated: 2001:db8:85a3::8a2e:370:7334
```

**Why IPv6?** IPv4 has ~4 billion addresses (running out!). IPv6 has 340 undecillion (enough for every grain of sand on Earth).

**Check your IP**:
```bash
# Local network IP
ip addr show  # Linux
ifconfig      # macOS

# Public IP
curl ifconfig.me
```

---

## Ports: Finding Processes

**IP address** finds the machine. **Port** finds the process.

**Port numbers**:
```
0-1023:    Well-known (HTTP=80, HTTPS=443, SSH=22)
1024-49151: Registered (apps can request)
49152-65535: Dynamic (OS assigns temporarily)
```

**Example**:
```
http://example.com:80/
│                  └─ Port 80 (HTTP)
└─ Domain name (becomes IP via DNS)

ssh user@192.168.1.100:22
         │              └─ Port 22 (SSH)
         └─ IP address
```

**One machine** can run multiple services (different ports):
```
Server:
  - Web server: port 80 (HTTP)
  - SSH daemon: port 22 (SSH)
  - Database: port 5432 (PostgreSQL)
  - API server: port 3000 (custom)
```

**Plant lens**: **"IP address is the garden's location, port is the specific plot within that garden."**

---

## DNS: Names to Numbers

**Humans prefer names** (`google.com`) over numbers (`142.250.185.46`).

**DNS** (Domain Name System) translates:

```bash
# Query DNS
nslookup google.com

# Output:
# Server: 8.8.8.8
# Address: 8.8.8.8#53
# 
# Name: google.com
# Address: 142.250.185.46
```

**How it works**:
```
1. You type "google.com" in browser
2. Browser asks DNS server: "What's the IP for google.com?"
3. DNS server responds: "142.250.185.46"
4. Browser connects to that IP
```

**DNS hierarchy**:
```
.                  (root)
├── .com           (top-level domain)
│   └── google.com (second-level)
│       └── www.google.com (subdomain)
└── .org
    └── wikipedia.org
```

**Like filesystem** (Essay 9590), but for domain names!

---

## TCP vs UDP

### TCP (Transmission Control Protocol)

**Reliable**:
- Guarantees delivery (packets arrive, in order)
- Retransmits lost packets
- Flow control (doesn't overwhelm receiver)

**Use cases**:
- Web (HTTP/HTTPS)
- SSH (remote shell)
- Email (SMTP, IMAP)
- File transfer (FTP)

**Cost**: Overhead (connection setup, acknowledgments).

### UDP (User Datagram Protocol)

**Unreliable** (but fast):
- Best-effort delivery (packets might be lost, out-of-order)
- No retransmission
- No flow control

**Use cases**:
- DNS queries (small, fast, can retry)
- Video streaming (losing a frame is OK, latency matters)
- Gaming (real-time, stale data is useless)
- VoIP (voice calls - latency > reliability)

**Benefit**: Lower latency (no connection overhead).

**Trade-off**: Reliability vs speed.

---

## Sockets: Programming Interface

**A socket** is an endpoint for communication:

```python
# Server (listens)
import socket

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(('0.0.0.0', 8080))  # Listen on port 8080
server.listen()

while True:
    client, addr = server.accept()  # Wait for connection
    client.send(b"Hello, client!")
    client.close()
```

```python
# Client (connects)
import socket

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect(('localhost', 8080))  # Connect to server
data = client.recv(1024)
print(data)  # Output: b"Hello, client!"
client.close()
```

**This is how** all internet communication happens (web servers, SSH, databases, etc.).

---

## Client-Server vs Peer-to-Peer

### Client-Server

**One server**, many clients:

```
Server (always running)
    ↑
    ├── Client 1 (connects when needed)
    ├── Client 2
    └── Client 3
```

**Examples**: Web (browser → server), email, databases.

**Pros**: Centralized, easy to manage  
**Cons**: Single point of failure, server must scale

### Peer-to-Peer (P2P)

**All nodes equal** (each is both client and server):

```
Peer 1 ←→ Peer 2
  ↕         ↕
Peer 3 ←→ Peer 4
```

**Examples**: BitTorrent, IPFS, Urbit (Essay 9503 - Nock), Bitcoin.

**Pros**: Decentralized, resilient, scales naturally  
**Cons**: Complex (discovery, NAT traversal)

**Sovereignty perspective** (from 9503, 9960): P2P = no central authority (digital sovereignty!).

---

## The OSI Model (Historical)

**OSI** (Open Systems Interconnection) defined **7 layers**:

```
7. Application  (HTTP, FTP, SSH)
6. Presentation (SSL, compression)
5. Session      (connections, dialogs)
4. Transport    (TCP, UDP)
3. Network      (IP, routing)
2. Data Link    (Ethernet, WiFi)
1. Physical     (cables, radio waves)
```

**TCP/IP simplified** to 4 layers:

```
Application (combines OSI 5-7)
Transport   (OSI 4)
Internet    (OSI 3)
Link        (combines OSI 1-2)
```

**Why simplify?** OSI was **designed** (committee, spec-first). TCP/IP was **evolved** (implemented, working code first).

**Result**: TCP/IP won (simpler, proven in practice).

**Plant lens**: **"OSI = ornamental garden (beautiful design). TCP/IP = working farm (produces food)."**

---

## Practical Networking

### Check Connections

```bash
# Active connections
netstat -an  # All connections, numeric

# Or (modern):
ss -tuln  # TCP, UDP, listening, numeric

# Who's connected?
netstat -tn  # TCP, no DNS lookup
```

### Test Connectivity

```bash
# Ping (ICMP echo)
ping google.com
# See if host is reachable

# Trace route
traceroute google.com
# See path packets take

# DNS lookup
nslookup google.com
# Or:
dig google.com
```

### Listen on Port

```bash
# Simple HTTP server (Python)
python3 -m http.server 8000
# Serves current directory on http://localhost:8000

# Test with curl
curl http://localhost:8000
```

---

## Try This

### Exercise 1: Explore Network Stack

```bash
# What's listening?
sudo lsof -i -P -n | grep LISTEN

# Example output:
# sshd   1000 root   3u  IPv4  TCP *:22 (LISTEN)
# nginx  2000 www    6u  IPv4  TCP *:80 (LISTEN)
```

**Observe**: Process, port, protocol.

---

### Exercise 2: HTTP from Scratch

```bash
# Connect to web server manually
telnet example.com 80

# Type (then press Enter twice):
GET / HTTP/1.1
Host: example.com

# Server responds with HTML!
# (HTTP is just text over TCP)
```

**Insight**: "High-level" protocols (HTTP) are just text (Essay 9560!).

---

### Exercise 3: DNS Investigation

```bash
# DNS lookup
dig google.com

# Output shows:
# - Query (what you asked)
# - Answer (IP address)
# - Authority (which DNS server answered)
# - TTL (how long to cache)

# Reverse lookup (IP → name)
dig -x 8.8.8.8
# Answer: dns.google.
```

---

## Going Deeper

### Related Essays
- **[9501: What Is Compute?](/12025-10/9501-what-is-compute)** - Distributed computing context
- **[9570: Processes](/12025-10/9570-processes-programs-in-motion)** - What runs networking code
- **[9503: What Is Nock?](/12025-10/9503-what-is-nock)** - Urbit (P2P networking)
- **[9960: The Grainhouse](/12025-10/9960-grainhouse-risc-v-synthesis)** - Sovereignty (P2P vs centralized)

### External Resources
- **"TCP/IP Illustrated"** - Classic networking book
- **Beej's Guide to Network Programming** - Sockets tutorial
- **`man socket`** - Socket API documentation
- **Wireshark** - Packet analysis tool (see actual network traffic)

---

## Reflection Questions

1. **Why TCP for web, UDP for video?** (Reliability vs latency trade-off)

2. **Is DNS a single point of failure?** (Centralized naming - what if it goes down?)

3. **Why did P2P lose to client-server?** (BitTorrent, Napster shut down - but why?)

4. **Can networking be more secure by default?** (Current internet: trust all traffic - bad assumption)

5. **What would Nock-based networking look like?** (All messages as nouns, provably correct protocols?)

---

## Summary

**Networking Fundamentals**:

**TCP/IP Stack**:
- **Application**: HTTP, SSH, DNS (what to send)
- **Transport**: TCP, UDP (how to send)
- **Internet**: IP (where to send)
- **Link**: Ethernet, WiFi (physical transmission)

**Key Concepts**:
- **IP address**: Machine location (192.168.1.100, IPv6)
- **Port**: Process identifier (80=HTTP, 22=SSH, 443=HTTPS)
- **DNS**: Names → numbers (google.com → 142.250.185.46)
- **Socket**: Programming interface (bind, listen, connect, send, recv)

**Protocols**:
- **TCP**: Reliable (guarantees delivery, order)
- **UDP**: Fast (best-effort, lower latency)

**Architectures**:
- **Client-Server**: Centralized (web, email, databases)
- **Peer-to-Peer**: Decentralized (BitTorrent, IPFS, Urbit)

**Key Insights**:
- **Layering enables composition** (each layer = one concern)
- **TCP/IP beat OSI** (evolved > designed, simpler > more complete)
- **Everything is text** (HTTP, DNS, SMTP - all text protocols!)
- **P2P = sovereignty** (no central authority, resilient)

**In the Valley**:
- **We respect layering** (compose protocols, don't monolith)
- **We prefer P2P** when possible (sovereignty over convenience)
- **We use text protocols** (HTTP, not binary blobs - easier to debug)
- **We understand trade-offs** (TCP reliability vs UDP speed)

**Plant lens**: **"Networks are irrigation channels—data flows like water between gardens (machines), nourishing the entire valley (internet ecosystem)."**

---

**Next**: We'll explore **concurrency**—how to do multiple things simultaneously, threads vs processes, and the coordination challenges that arise!

---

**Navigation**:  
← Previous: [9592 (permissions who can do what)](/12025-10/9592-permissions-who-can-do-what) | **Phase 1 Index** | Next: [9594 (concurrency threads parallelism)](/12025-10/9594-concurrency-threads-parallelism)

**Metadata**:
- **Phase**: 1 (Foundations)
- **Week**: 4
- **Prerequisites**: 9501, 9570, 9590
- **Concepts**: TCP/IP, sockets, DNS, protocols, client-server, P2P, IP addresses, ports
- **Next Concepts**: Concurrency, threads, parallelism, synchronization
- **Plant Lens**: Networks = irrigation channels, data = water flow, internet = valley ecosystem



---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*