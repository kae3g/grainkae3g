# kae3g 9501: What Is Compute? Cloud, P2P, and Networked Power

**Phase 1: Foundations & Philosophy** | **Week 1** | **Reading Time: 12 minutes**

---

## What You'll Learn

- How "compute" differs from "a computer"
- Cloud computing: renting processing power across the network
- Peer-to-peer (P2P): distributed computation without central servers
- Edge computing: bringing processing closer to data sources
- The economic and architectural implications of networked compute
- Why location of computation matters (latency, privacy, sovereignty)

---

## Prerequisites

- **[9500: What Is a Computer?](/12025-10/9500-what-is-a-computer)** - Understanding single-machine computation

---

## From "A Computer" to "Compute"

In **Essay 9500**, we defined **a computer** as a single, universal machine.

But modern computing transcends individual machines:

> **Compute** (noun): Processing power as a **fungible resource**, distributed across networks, rented by the second, accessed from anywhere.

The shift:
```
Then: "I own a computer"
Now:  "I rent compute"

Then: "Where's my machine?"
Now:  "Where's my data? Where's my process running? I don't know—and I don't care."
```

This is the **cloudification** of computing: treating CPU cycles, memory, and storage as **commodities** like electricity or water.

---

## The Three Models of Compute

### 1. Centralized Compute (The Cloud)

**Definition**: Large datacenters owned by companies (AWS, Google, Microsoft) rent processing power to users.

**Architecture**:
```
Your Laptop/Phone
      ↓ (Internet)
Cloud Datacenter (Virginia, Oregon, Ireland, Singapore...)
├─ Millions of servers
├─ Your VMs/containers running here
└─ You pay per second/hour
```

**Pros**:
- **Scalability**: Spin up 1000 servers in minutes
- **Reliability**: Datacenters have redundancy, backups, 99.99% uptime
- **Maintenance**: Someone else replaces hard drives, applies patches
- **Global reach**: Deploy to multiple continents instantly

**Cons**:
- **Privacy**: Your data lives on someone else's hardware
- **Sovereignty**: Subject to datacenter country's laws (GDPR, CLOUD Act)
- **Cost**: Can be expensive at scale (vs owning hardware)
- **Latency**: Round-trip to datacenter (50-200ms typical)
- **Dependency**: If AWS goes down, half the internet goes down

**Economic Model**:
- Pay for what you use (like electricity)
- Shifts **capital expense** (buy servers) to **operational expense** (rent servers)
- Enables startups (no upfront hardware cost)

**Examples**:
- **AWS EC2**: Rent Linux/Windows VMs
- **Google Cloud Run**: Deploy containerized apps, pay per request
- **Cloudflare Workers**: Run JavaScript at edge locations
- **Render, Fly.io, Railway**: Deploy web apps without managing servers

---

### 2. Peer-to-Peer Compute (P2P)

**Definition**: Computation distributed across participant machines, with no central authority.

**Architecture**:
```
Your Computer ←→ Peer A
     ↕              ↕
   Peer B  ←→    Peer C
     ↕              ↕
   Peer D  ←→    Peer E

(No central server - everyone connects to everyone)
```

**Pros**:
- **Censorship resistance**: No single point of control
- **Cost**: Free or very cheap (participants share resources)
- **Resilience**: No single point of failure
- **Privacy**: Can be encrypted end-to-end (no trusted third party)

**Cons**:
- **Complexity**: Harder to program (no central coordination)
- **Performance**: Unpredictable (depends on peer availability)
- **Discovery**: How do you find peers? (bootstrapping problem)
- **Security**: Must assume some peers are malicious

**Historical Examples**:
- **SETI@home** (1999): Distributed search for alien signals
- **Folding@home**: Protein folding simulation (COVID-19 research)
- **BitTorrent**: Distributed file sharing
- **Bitcoin**: Distributed ledger (blockchain)
- **IPFS**: Distributed file storage
- **Tor**: Anonymous routing through volunteer relays

**Modern Applications**:
- **WebRTC**: Peer-to-peer video calls (Zoom, Meet use P2P when possible)
- **Syncthing**: File sync without cloud storage
- **Mastodon/Fediverse**: Distributed social networks
- **Urbit**: Peer-to-peer operating system (we'll explore this later!)

---

### 3. Edge Compute

**Definition**: Processing at the "edge" of the network, close to data sources.

**Architecture**:
```
Central Cloud (Virginia)
      ↕
Regional Edge (San Francisco)
      ↕
Local Edge (Your ISP's datacenter)
      ↕
Device Edge (Your phone/laptop)
      ↕
Sensors/IoT (Your smartwatch, car, thermostat)
```

**Why Edge Matters**:

1. **Latency**: Physical distance = delay
   - Light travels at 300,000 km/s
   - San Francisco ↔ Virginia: ~4,000 km = 13ms minimum (one way)
   - Round trip: 26ms + processing time
   - For real-time applications (gaming, AR/VR, autonomous vehicles), this is **too slow**

2. **Bandwidth**: Moving data is expensive
   - Self-driving car generates 4 TB/day of sensor data
   - Sending all data to cloud: impractical
   - Process locally, send only insights

3. **Privacy**: Keep sensitive data local
   - Medical devices: process on device, never send raw data
   - Security cameras: detect motion locally, only upload alerts

4. **Reliability**: Work offline
   - If internet dies, edge devices keep working
   - Airplane mode: your phone still processes photos, plays music

**Examples**:
- **Cloudflare Edge**: 300+ datacenters worldwide, <50ms from 95% of internet users
- **AWS Lambda@Edge**: Run code closer to users
- **Apple Neural Engine**: ML inference on iPhone (not in cloud!)
- **Tesla Autopilot**: Processes sensor data in car (can't wait for cloud round-trip)
- **Smart home hubs**: Process commands locally (Alexa/Google Home hybrid approach)

---

## The Compute Continuum

Modern systems use **all three** models in combination:

### Example: A Self-Driving Car

```
┌─────────────────────────────────────────────────────────────┐
│ Cloud Compute (Datacenter)                                  │
│ - Train ML models on fleet data (100,000 GPUs)             │
│ - Map updates, traffic patterns                            │
│ - Over-the-air software updates                            │
└─────────────────────────────────────────────────────────────┘
                          ↑ ↓
                      (4G/5G Network)
                          ↑ ↓
┌─────────────────────────────────────────────────────────────┐
│ Edge Compute (In Car)                                       │
│ - Real-time sensor fusion (cameras, lidar, radar)          │
│ - Path planning, obstacle avoidance                        │
│ - Inference on pretrained models (100ms response)          │
│ - Works offline (in tunnels, rural areas)                  │
└─────────────────────────────────────────────────────────────┘
                          ↑ ↓
                   (CAN bus, internal)
                          ↑ ↓
┌─────────────────────────────────────────────────────────────┐
│ Device Compute (Sensors)                                    │
│ - Camera: compress images                                   │
│ - Lidar: range finding                                      │
│ - Radar: velocity detection                                 │
└─────────────────────────────────────────────────────────────┘
```

The **division of labor** is strategic:
- **Heavy, slow tasks** (training): Cloud
- **Fast, real-time tasks** (driving): Edge (car)
- **Sensor preprocessing**: Device (camera chip)

---

## The Economic Shift: Compute as Commodity

### Traditional Computing (Pre-2006)
```
Want to run a website?
1. Buy servers ($10,000)
2. Install in your office
3. Pay for power, cooling, internet
4. Maintain hardware yourself
5. Over-provision (what if you get popular?)
```

**Capital-intensive**, **slow to scale**, **risky**.

### Cloud Computing (2006+)
```
Want to run a website?
1. Write code
2. Deploy to AWS/Vercel/Fly.io
3. Pay $5/month (or $0.50 if low traffic)
4. Scale automatically
5. Someone else handles hardware
```

**Low upfront cost**, **fast scaling**, **pay-as-you-go**.

### The Pricing Model

**AWS EC2** (example):
- `t4g.nano`: 2 vCPUs, 0.5 GB RAM = **$0.0042/hour** = ~$3/month
- `c7g.16xlarge`: 64 vCPUs, 128 GB RAM = **$2.176/hour** = ~$1,600/month
- **On-demand**: Pay by the hour, no commitment
- **Spot instances**: Bid for unused capacity (up to 90% discount, but can be terminated)
- **Reserved**: 1-3 year commitment (up to 72% discount)

**Implications**:
- Startups can compete with giants (low entry cost)
- But giants have economies of scale (Netflix pays less per GB than you)
- "Serverless" movement: pay per request, not per hour (even more granular)

---

## Location, Location, Location

### Why Geography Matters

**1. Latency** (Speed of light is fixed!)
```
User in Tokyo → Server in Virginia:
- Distance: 11,000 km
- Light speed travel: 37ms (minimum)
- Actual internet route: 150-200ms (goes through routers, undersea cables)
- User perceives lag

User in Tokyo → Server in Tokyo:
- Distance: <100 km
- Latency: 5-10ms
- Feels instant
```

**2. Data Sovereignty** (Where data lives = which laws apply)
```
EU user data on EU servers:
- Must comply with GDPR (strict privacy)
- EU government can subpoena

EU user data on US servers:
- Must comply with GDPR AND US CLOUD Act
- Both EU and US governments can subpoena
- More legal complexity
```

**3. Availability** (Network failures are geographic)
```
US-EAST-1 (Virginia) goes down:
- Affects US East Coast users most
- Multi-region deployments stay up
- But: routing can cascade failures
```

**Example**: Cloudflare's approach
- 300+ datacenters worldwide
- Your request goes to nearest one (anycast routing)
- Code runs <50ms from 95% of users
- Data replicated across regions

---

## The P2P Alternative: Urbit's Vision

**Problem with Cloud**: You rent someone else's computer. They have all the power.

**Problem with P2P**: Hard to program, unpredictable performance.

**Urbit's synthesis**:
```
You own a "planet" (your personal server)
- Could be running on your hardware at home
- Could be hosted by a third party (but YOU own it)
- Other planets connect directly to yours (P2P)
- But with clean abstractions (not raw P2P chaos)
```

**Key insight**: **Ownership** of compute, not just rental.

(We'll explore Urbit deeply in Phase 4 - it's a radical reimagining of networked computing)

---

## Hands-On: Understanding Latency

### Exercise 1: Ping the World

Open a terminal and run:
```bash
# Ping Google's DNS (usually very close)
ping 8.8.8.8

# Typical results:
# - Same city: 5-15ms
# - Same continent: 20-50ms
# - Across ocean: 100-200ms
# - Satellite internet: 500-700ms
```

**Insight**: Every network request has this baseline latency. 

For an API call:
- 1 request: 50ms
- 10 sequential requests: 500ms (users notice!)
- 10 parallel requests: 50ms (much better)

**Design implication**: Minimize round-trips. Batch requests. Cache aggressively.

---

### Exercise 2: Calculate Cloud Costs

**Scenario**: You run a web app
- 2 servers (for redundancy): `t4g.small` = $0.0168/hour each
- 1 database: RDS `db.t4g.small` = $0.034/hour
- 100 GB storage: $10/month
- 500 GB bandwidth: $45/month

**Monthly cost**:
```
Servers: 2 × $0.0168 × 24 × 30 = $24.19
Database: $0.034 × 24 × 30 = $24.48
Storage: $10
Bandwidth: $45
Total: ~$104/month
```

**Compare to**:
- Shared hosting: $5-20/month (but limited scaling)
- Dedicated server: $100-300/month (but you manage it)
- Serverless (Vercel/Netlify): $0-20/month for small apps (but less control)

**Trade-offs everywhere!**

---

### Exercise 3: Explore P2P in Action

**Try BitTorrent** (legal torrents only!):
1. Download a Linux ISO via torrent (e.g., Ubuntu)
2. Watch the peer list: you're connected to dozens of strangers
3. Notice: no central server, yet the download works
4. Each peer uploads to others (P2P reciprocity)

**Insight**: P2P scales beautifully for popular content (more peers = faster). But for rare content, you need seeders (someone hosting).

---

## The Philosophical Implications

### Centralization vs Decentralization

**Centralized (Cloud)**:
- **Pros**: Fast, reliable, easy to use
- **Cons**: Single point of control, censorship risk, privacy concerns
- **Power**: Concentrated (Amazon, Google, Microsoft)

**Decentralized (P2P)**:
- **Pros**: Censorship-resistant, privacy-preserving, no gatekeepers
- **Cons**: Slower, harder to use, less reliable
- **Power**: Distributed (everyone is equal)

**The Valley's Position**:
> We embrace **selective centralization**. Use cloud for convenience, but design systems that **could** run P2P. Avoid lock-in. Own your data. Choose your dependencies consciously.

---

### Sovereignty and Compute

**Who controls your compute?**

**Scenario 1**: Your app on AWS
- Amazon can (and has) suspended accounts without warning
- If AWS decides you violate ToS, your app disappears overnight
- You have **no recourse** (private platform, their rules)

**Scenario 2**: Your app on your hardware
- You control everything
- But: hardware fails, you're responsible
- And: what if your ISP cuts you off? (still a dependency)

**Scenario 3**: Your app on a P2P network
- No single point of control
- But: how do users find you? (DNS still centralized)
- And: harder to program, less tooling

**The middle ground**: 
- Own your **data** (can export and move it)
- Use cloud for **convenience** (but keep portability)
- Design for **graceful degradation** (works offline)

We'll explore this deeply in **Phase 5** (Synthesis & Integration).

---

## The Future: Compute Trends

### 1. Serverless Everywhere
**Current**: You manage VMs/containers  
**Future**: You write functions, cloud runs them  
**Example**: Cloudflare Workers, AWS Lambda@Edge

**Pros**: Pay per request (can be $0 for low-traffic sites)  
**Cons**: Vendor lock-in, cold start latency, less control

### 2. Edge AI
**Current**: ML models run in cloud  
**Future**: Models run on device (phone, laptop, IoT)  
**Example**: Apple Silicon's Neural Engine, Google Tensor

**Why**: Privacy (data never leaves device), latency (instant), offline (works anywhere)

### 3. Quantum Computing (Still Early)
**Current**: Classical computers (bits: 0 or 1)  
**Future**: Quantum computers (qubits: superposition of 0 and 1)  
**Status**: Experimental (not ready for general use)

**What it's good for**: Optimization, simulation, cryptography  
**What it's NOT good for**: General-purpose computing (your laptop won't be quantum)

### 4. Homomorphic Encryption
**Problem**: To process data in cloud, you must decrypt it (cloud provider sees it)  
**Future**: Compute on **encrypted** data, cloud never sees plaintext  
**Status**: Mathematically possible, but too slow today (100-1000x overhead)

**If this works**: True "zero-knowledge" cloud computing (ultimate privacy)

---

## Try This

### Exercise 1: Deploy to the Cloud (Beginner-Friendly)

**Option A**: Static site (free!)
1. Create an HTML file: `index.html`
2. Push to GitHub
3. Enable GitHub Pages
4. Your site is now live! (Uses GitHub's compute/bandwidth)

**Option B**: Full backend (free tier)
1. Write a simple API in Python/Node.js
2. Deploy to Render.com or Fly.io (free tier: 512MB RAM)
3. Your API is now accessible worldwide

**Insight**: You're using compute you don't own, accessed via URLs.

---

### Exercise 2: Measure Your Compute Usage

**On your laptop**, run:
```bash
# macOS/Linux
top

# See:
# - CPU usage (how much compute you're using)
# - Memory usage
# - Running processes
```

**Now think**:
- How much of your laptop's power are you actually using? (Usually <10%)
- What if you could rent out the idle 90%? (This is the P2P compute dream)
- What if you could rent someone else's idle compute when you need more? (Cloud in reverse)

---

### Exercise 3: Design a Distributed System

**Scenario**: Build a messaging app (like Signal, WhatsApp)

**Questions**:
1. **Where do messages live?**
   - Centralized server? (Easy, but operator sees everything)
   - P2P? (Private, but how do you send to offline users?)
   - Hybrid? (Server as relay for offline delivery, but encrypted end-to-end?)

2. **Where does compute happen?**
   - Encrypt/decrypt in browser/app? (Privacy, but slower)
   - Server-side? (Fast, but server sees plaintext)

3. **How do you find users?**
   - Central directory? (Phone number → user ID mapping)
   - Distributed hash table? (P2P, but harder to implement)

**Real-world answer**: Signal uses **hybrid**:
- Messages encrypted end-to-end (compute in client)
- Server acts as relay (doesn't see plaintext)
- Phone numbers mapped centrally (for convenience)
- Messages deleted after delivery (minimize server storage)

**Trade-offs everywhere!** No perfect solution.

---

## Going Deeper

### Related Essays
- **9500**: What Is a Computer? (Foundation for understanding compute)
- **9610**: Nix Package Management (How to make cloud deployments reproducible)
- **9650**: Init Systems (How processes are managed—relevant for cloud)
- **9840**: Cosmopolitan libc (Build-once, run-anywhere binaries—ultimate portability)

### External Resources
- **AWS Architecture Center**: Real-world cloud patterns
- **IPFS Docs**: How distributed storage works
- **Urbit.org**: Radical rethinking of networked computing
- **Cloudflare Blog**: Excellent explanations of edge computing

### For the Technically Curious
- **CAP Theorem**: Why distributed systems are hard (consistency, availability, partition tolerance—pick 2)
- **Byzantine Fault Tolerance**: P2P systems with malicious actors
- **Raft/Paxos**: Consensus algorithms for distributed systems

---

## Reflection Questions

1. **If "compute" is a commodity, who owns it?**  
   (AWS owns the hardware, you rent cycles—but what about data sovereignty?)

2. **Is P2P inevitable, or will centralized cloud dominate?**  
   (Network effects favor centralization, but privacy concerns push toward P2P)

3. **What does it mean to "own" your compute?**  
   (Hardware? The right to run programs? Access to internet? All three?)

4. **How much latency is acceptable?**  
   (For chat: 100ms OK. For gaming: 20ms max. For autonomous vehicles: 1ms required)

5. **Should compute be free?**  
   (Like roads/libraries? Or like electricity? Or purely market-based?)

---

## Summary

**Compute** is:
- **Distributed**: Across clouds, edges, devices
- **Commodified**: Rented like electricity
- **Geographic**: Location determines latency, laws, reliability
- **Hybrid**: Modern systems use centralized + P2P + edge

**Key Insights:**
- Cloud computing **shifted economics** (capex → opex, low entry cost)
- P2P computing **shifts power** (centralized → distributed control)
- Edge computing **optimizes latency** (move compute closer to users)
- **Trade-offs everywhere**: convenience vs privacy, performance vs cost, control vs simplicity

**In the Valley:**
- We use cloud **tactically** (for leverage)
- We design for **portability** (avoid lock-in)
- We respect **sovereignty** (know where your data lives)
- We embrace **simplicity** (choose the right level of distribution)

---

**Next**: We'll explore the Unix philosophy—how to structure computation simply, regardless of where it runs.

---

**Navigation**:  
← Previous: [9500 (what is a computer)](/12025-10/9500-what-is-a-computer) | **Phase 1 Index** | Next: [9502 (ode to nocturnal time)](/12025-10/9502-ode-to-nocturnal-time)

**Bridge to Narrative**: For a character-driven take on distributed systems, see [9960 (The Grainhouse)](/12025-10/9960-grainhouse-risc-v-synthesis) - our vision for sovereign computing!

**Metadata**:
- **Phase**: 1 (Foundations)
- **Week**: 1
- **Prerequisites**: 9500 (What Is a Computer?)
- **Concepts**: Cloud computing, P2P, edge computing, latency, sovereignty, distributed systems
- **Next Concepts**: Unix philosophy, simplicity, composition



---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*