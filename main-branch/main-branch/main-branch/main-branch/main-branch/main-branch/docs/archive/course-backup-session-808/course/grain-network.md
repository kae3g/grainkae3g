# 🌾 THE GRAIN NETWORK
## "Every Great System Starts With a Single Grain"

**A student-built decentralized network for learning, collaboration, and digital sovereignty.**

Built by high school students following the grainkae3g curriculum, the Grain Network is a living laboratory where tomorrow's technologists gain real-world experience building distributed systems, blockchain infrastructure, and personal sovereignty tools.

---

## 📊 Network Statistics

- **Duration:** 18 weeks
- **Students:** ∞ (unlimited)
- **Open Source:** 100%
- **Cost:** $0 (free base tier)

---

## 🎯 THE VISION

Imagine a network where **every student is a node**. Where learning Linux isn't just about passing a test—it's about **running actual infrastructure**. Where writing your first Clojure script contributes to a **real decentralized system**. Where your final project becomes part of a **global student network** of sovereign computing nodes.

The Grain Network is that vision realized. As you progress through the 18-week curriculum, you're not just learning—you're **building the future**.

---

## 🏗️ NETWORK ARCHITECTURE

### Layer 1: Student Nodes
Every student runs their own grainkae3g instance:
- Ubuntu 24.04 LTS base system
- Sway desktop environment
- Personal configuration
- Local development environment
- Git repository with signed commits

**Setup:** Weeks 1-3

### Layer 2: Collaboration Layer
Students connect through shared infrastructure:
- GitHub organization: `grain-network`
- Shared grainstore repositories
- Peer code review system
- Collective documentation
- Issue tracking and project boards

**Join:** Weeks 4-5

### Layer 3: Identity System
Decentralized identity using Urbit + ICP:
- Each student gets a unique Grain ID
- Hierarchical identity (like Urbit)
- ICP canister for identity storage
- GPG/SSH key infrastructure
- Reputation and contribution tracking

**Claim Identity:** Weeks 16-17

### Layer 4: Application Layer
Student-built applications and services:
- Shared web applications (SvelteKit)
- Automation tools (Babashka)
- Desktop apps (Humble UI + Clojure)
- System utilities and scripts
- Distributed package repositories

**Build Apps:** Weeks 11-18

### Layer 5: Governance Layer
Democratic decision-making process:
- Student-run committees
- Proposal and voting system
- Network upgrades via consensus
- Code of conduct enforcement
- Mentorship matching

**Participate:** Ongoing

### Layer 6: Learning Commons
Shared educational resources:
- Course materials and tutorials
- Video walkthroughs
- Troubleshooting guides
- Sample projects and templates
- Peer tutoring sessions

**Learn & Teach:** All weeks

---

## 👥 STUDENT ROLES IN THE NETWORK

### 🌱 Seedlings
**New students (Weeks 1-6)**

Learning fundamentals, setting up their nodes, contributing to documentation. Every expert was once a seedling!

### 🌾 Grains
**Active students (Weeks 7-12)**

Building applications, peer reviewing code, participating in network governance. The backbone of the network.

### 🌻 Harvesters
**Advanced students (Weeks 13-18)**

Leading projects, mentoring seedlings, architecting new features. Future leaders of decentralized systems.

### 🏛️ Architects
**Course graduates**

Maintaining infrastructure, teaching new courses, expanding the network. Permanent members of the Grain Council.

### 🔧 Infrastructure Team
**Students managing servers, CI/CD pipelines, monitoring, security**

Hands-on experience with real production systems.

### 📚 Documentation Squad
**Students writing tutorials, maintaining wikis, creating videos**

Making knowledge accessible to everyone.

### 🎨 Design Guild
**Students crafting UI/UX, themes, branding, visual identity**

Making sovereignty beautiful and accessible.

### 🔐 Security Circle
**Students auditing code, managing keys, ensuring privacy**

Guardians of the network's integrity.

---

## 📅 18-WEEK NETWORK BUILD TIMELINE

### WEEKS 1-3: Network Initialization 🚀
**Goal:** Every student has a working grainkae3g node

- Install Ubuntu 24.04 LTS
- Configure basic system settings
- Join the `grain-network` GitHub organization
- Complete "Hello Network" first commit

**Network Effect:** Each new node increases collective computing power!

---

### WEEKS 4-5: Git Infrastructure 🔗
**Goal:** Establish distributed version control

- Generate GPG and SSH keys
- Fork the grain-network repositories
- Submit your first pull request
- Review a peer's code

**Network Effect:** Peer review improves everyone's code quality!

---

### WEEKS 6-7: Desktop Standardization 🎨
**Goal:** Every node runs Sway with Grain branding

- Install Sway window manager
- Apply Grain Network theme
- Configure shared keybindings
- Screenshot your setup for the gallery!

**Network Effect:** Consistent UX across all nodes!

---

### WEEKS 8-10: Automation Framework ⚡
**Goal:** Shared Babashka utility library

- Learn Clojure fundamentals
- Contribute to `grain-utils` library
- Build network management scripts
- Automate node synchronization

**Network Effect:** Shared scripts benefit all students!

---

### WEEKS 11-13: Web Portal Launch 🌐
**Goal:** Deploy grain-network.org

- Build SvelteKit web portal
- Student directory and profiles
- Project showcase gallery
- Real-time network statistics

**Network Effect:** Public visibility attracts more students!

---

### WEEKS 14-15: Experimental OS Lab 🔬
**Goal:** Shared virtual machine cluster

- Set up QEMU/KVM on nodes
- Deploy SixOS test VMs
- Share VM templates via network
- Collaborate on OS experiments

**Network Effect:** Pooled resources enable bigger experiments!

---

### WEEKS 16-17: Identity System Activation 🔐
**Goal:** Launch Grain Identity Protocol (GIP)

- Deploy ICP canister for identity
- Each student claims Grain ID (e.g., ~grain-alice)
- Implement reputation system
- Enable cross-node authentication

**Network Effect:** True decentralized identity for all!

---

### WEEK 18: Network Maturity 🎓
**Goal:** Self-sustaining student network

- Final projects deployed to network
- First governance vote (network upgrades)
- Graduate to "Architect" status
- Begin mentoring next cohort

**Network Effect:** The network becomes self-perpetuating!

---

## 🛠️ NETWORK TECHNOLOGY STACK

| Technology | Purpose |
|------------|---------|
| 🐧 **Ubuntu 24.04 LTS** | Base OS for all nodes |
| 🪟 **Sway (Wayland)** | Tiling window manager |
| 📜 **Clojure/Babashka** | Automation & scripting |
| 🌐 **SvelteKit** | Web portal framework |
| 🔐 **ICP Canisters** | Identity & storage |
| 🌌 **Urbit Protocol** | Identity architecture |
| 🦀 **Rust** | Smart contracts |
| 📦 **Git/GitHub** | Version control |
| 🔧 **s6 Supervision** | Process management |
| 🖥️ **QEMU/KVM** | Virtualization layer |
| 📝 **EDN Data** | Configuration format |
| 🎨 **Humble UI** | Desktop applications |

---

## 🤝 HOW TO CONTRIBUTE TO THE GRAIN NETWORK

Every student contribution strengthens the network. Here's how you can participate:

### Step 1: Fork & Clone
```bash
git clone https://github.com/grain-network/grainkae3g
cd grainkae3g
git checkout -b your-feature
```

### Step 2: Make Changes
Add features, fix bugs, write documentation, create tutorials. Follow the contribution guidelines in `CONTRIBUTING.md`

### Step 3: Test Locally
```bash
bb test
bb lint
bb build
```

### Step 4: Submit PR
```bash
git add .
git commit -S -m "Add feature"
git push origin your-feature
# Then create PR on GitHub
```

### Step 5: Peer Review
Wait for at least 2 peer reviews. Address feedback. Learn from the review process!

### Step 6: Merge & Deploy
Once approved, your code becomes part of the network! Watch it deploy across all nodes.

---

## ⚠️ NETWORK PRINCIPLES & CODE OF CONDUCT

1. **Learn in Public** - Share your journey, including failures. We all learn together.
2. **Help Others** - If you're stuck, ask. If someone's stuck, help.
3. **Respect Privacy** - Build systems that protect user data. Sovereignty means privacy.
4. **Open Source First** - Contribute back. Share what you build.
5. **Quality Over Speed** - Better to build it right than build it fast.
6. **Inclusive Community** - Everyone belongs here, regardless of background or experience level.
7. **Security Minded** - Always think about security implications. We're building real infrastructure.
8. **Document Everything** - Future students will thank you for clear documentation.

---

## 🚀 JOIN THE GRAIN NETWORK TODAY

Be part of the first generation of students building truly decentralized, student-owned computing infrastructure. Your contributions matter. Your voice matters. Your sovereignty matters.

### Get Started

1. **[Start the Course](index.md)** - Begin your 18-week journey
2. **[View on GitHub](https://github.com/grain-network)** - Explore the codebase
3. **[Week 1 Lesson](week01.md)** - Install Ubuntu and dive in!

---

## 📊 LIVE NETWORK STATISTICS

| Metric | Count |
|--------|-------|
| Active Nodes | 0 |
| Students | 0 |
| Commits Today | 0 |
| Projects | 0 |

*Statistics will update as the network grows. Be the first node!*

---

## 💬 FREQUENTLY ASKED QUESTIONS

### Q: Do I need to own a Framework 16 laptop?
**A:** No! While the course was built on a Framework 16, any laptop capable of running Ubuntu 24.04 LTS will work. Framework is recommended for its repairability and upgradeability, but not required.

### Q: I've never programmed before. Can I still join?
**A:** Absolutely! The course is designed for beginners. We start with the basics and build up gradually. The only prerequisite is curiosity and willingness to learn.

### Q: Is this really free?
**A:** Yes! All course materials, software, and network access are completely free and open source. The only cost is your own hardware (any laptop).

### Q: What if I get stuck or need help?
**A:** The Grain Network has peer support built in! Ask in GitHub discussions, join study groups, request a mentor, or attend virtual office hours. We learn together.

### Q: Will I get a certificate or credential?
**A:** Better than a certificate—you'll have a portfolio of real, deployed code! Your GitHub profile becomes your credential. Plus, you'll have network reputation as a Grain Architect.

### Q: Can teachers use this in their classrooms?
**A:** Yes! The curriculum is open source. Teachers can adopt it, modify it, and contribute improvements back to the network. Email us for educator resources.

### Q: What happens after I graduate?
**A:** You become a permanent Architect in the Grain Network! Mentor new students, maintain infrastructure, vote on network governance, and continue building the future.

---

## 📚 Additional Resources

- [Course Home](index.md)
- [Student Organization Guide](../student-guide/HOW-TO-STAY-ORGANIZED.md)
- [Business Plan](../business/grain-network-business-plan.md)
- [GitHub Repository](https://github.com/kae3g/grainkae3g)
- [Press Release](../PRESS-RELEASE-GRAINKAE3G.md)
- [Philosophy](../core/philosophy/PSEUDO.md)

---

## 🌾 Closing Statement

**THE GRAIN NETWORK**

"Our origin is not here—but our future is in our hands. Together, grain by grain, we build the decentralized, sovereign computing infrastructure of tomorrow."

🌾 **Every great network starts with a single grain** 🌾

---

**Created by:** [kae3g](https://github.com/kae3g) • 2025  
**License:** MIT • Built with ❤️ for digital sovereignty  
**Contact:** [kj3x39@gmail.com](mailto:kj3x39@gmail.com)


