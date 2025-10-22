# Personal Reminders

## Our Origin Is Not Here

This phrase, "our origin is not here," resonates deeply with the journey of building a personal sovereignty stack. It speaks to a sense of displacement from conventional systems and a yearning for a more authentic, self-determined digital existence.

It implies:
- **A rejection of centralized control**: Our digital "origin" is not in the hands of corporations or monolithic platforms.
- **A search for true roots**: We are seeking a foundation that aligns with our values, perhaps in decentralized networks, open-source principles, or self-hosted infrastructure.
- **A philosophical stance**: Our true potential, our "origin," lies beyond the current limitations and compromises of the mainstream tech landscape.

This note serves as a reminder of the core motivation behind this project: to forge a new path, to build a system where our digital selves are truly our own, and where our "origin" is defined by our own choices and creations, not by external forces.

## bisect

*Added: January 2025*

## clojure-sixos

**Future Project**: A comprehensive Clojure library for SixOS development and system management.

**Architecture**:
- **clojure-s6** as core utility dependency for s6 supervision
- **SixOS Integration**: Native support for SixOS-specific features
- **System Management**: Declarative system configuration
- **Service Orchestration**: High-level service management
- **Development Tools**: SixOS development workflow utilities

**Goals**:
- Bridge Clojure ecosystem with SixOS
- Provide functional, immutable system management
- Enable declarative SixOS configuration
- Support SixOS development workflows
- Integrate with existing Clojure tooling

**Dependencies**:
- clojure-s6 (s6 supervision utilities)
- SixOS (target platform)
- Nix (package management)
- s6 (process supervision)

*Added: January 2025*

## ipod touch - sj's best invention

**Personal Note**: The iPod Touch was Steve Jobs' best invention - a perfect balance of simplicity, functionality, and user experience. It demonstrated how a device could be both powerful and intuitive, offline-capable yet syncable, and completely focused on its core purpose without unnecessary complexity.

**Inspiration for Grainwriter**: The iPod Touch's philosophy of "sync when connected, work when offline" is exactly what we're building with the Grainwriter - a device that works perfectly in isolation but syncs beautifully when you want to share your work.

**Key Principles**:
- Offline-first design
- Simple, focused interface
- Manual sync when needed
- No unnecessary connectivity
- Perfect for its intended purpose

*Added: January 2025*

## grainsource

**Perfect name for our Git abstraction!**

**Why "Grainsource" is better than all alternatives:**
- **Source** - Direct reference to source code/version control
- **Grain + Source** - "Source of grains" = repository of atomic commits
- **Open Source** - Perfect alignment with our open-source philosophy
- **Single source of truth** - Each grainsource is the authoritative version

**Command Structure:**
```bash
# Repository management
grainsource init          # Initialize new repository
grainsource harvest       # Clone/download repository
grainsource plant         # Commit changes
grainsource share         # Push to remote
grainsource gather        # Pull from remote
grainsource sync          # Bidirectional sync (like Urbit sync)

# Branch management
grainsource sprout        # Create branch
grainsource blend         # Merge branches
grainsource branches      # List branches

# History & status
grainsource history       # View commit log
grainsource check         # Status
grainsource diff          # Show differences
grainsource trace         # Detailed history with graph

# Remote management
grainsource remote add    # Add remote grainsource
grainsource sources       # List remote sources
```

**Technical Architecture:**
- **Backend**: Git-compatible core (libgit2 bindings via Clojure)
- **Frontend**: Clojure Humble UI desktop app
- **Sync**: Works with GitHub, Codeberg, self-hosted Git
- **Extensions**: 
  - ICP canister storage (decentralized backup)
  - Urbit sync integration (peer-to-peer)
  - Solana commit signatures (blockchain timestamping)
- **s6 Integration**: `clojure-s6` daemon for background sync
- **SixOS Native**: First-class citizen on SixOS

**Backwards Compatibility:**
- Can use existing Git repos (reads .git directories)
- Can push/pull to GitHub/GitLab/Codeberg
- Wrapper around Git initially, native implementation later

**Future: Grainsource Platform**
- Desktop app (Humble UI + Clojure)
- Web interface (SvelteKit for GitHub-like UI)
- ICP canister hosting (decentralized repository hosting)
- Urbit integration (identity + sync)
- CLI tool (`grainsource` command)

**Project Structure:**
```
grainstore/grainsource/
├── src/grainsource/
│   ├── core.clj           # Core Git operations
│   ├── cli.clj            # Command-line interface
│   ├── ui.clj             # Humble UI desktop app
│   ├── sync.clj           # Remote sync logic
│   ├── icp.clj            # ICP canister integration
│   └── urbit.clj          # Urbit peer sync
├── canisters/
│   └── grainsource-storage/  # ICP canister for repo hosting
└── deps.edn
```

**This is it. Grainsource = our Git.**

*Added: January 2025*

---

## Grain Hardware Ecosystem

**Complete open-source hardware platform for personal sovereignty:**

**Devices:**
- **Graincamera** - Open-hardware mirrorless camera with native AVIF support, RISC-V processor, Fujifilm X-mount, Nostr + ICP integration ($799)
- **Grainwriter** - E-ink writing device with 64GB RAM-only storage, infinite battery life, IP68 waterproof, MIL-STD-810H rugged, SixOS + Clojure ($1,099-$1,699)
- **Grainpack** - External GPU attachment for Grainwriter with refurbished AMD GPUs (RX 6600 XT/6700 XT/6800 XT), IP68 waterproof, hot-swappable USB-C connection ($1,199-$1,699)

**Agentic Pipeline:**
- **Self-hosted AI development** with Void/Zed editors
- **Open-source models:** Qwen3, Gemini, Llama, GPT-OS
- **GPU-accelerated** via Grainpack
- **Complete privacy** - code never leaves your infrastructure

**Integration:**
- All devices run **SixOS** (NixOS without systemd, s6 supervision)
- **Clojure-first** development environment
- **Grainspace Network** sync via ICP + Urbit + Nostr
- **Unified configuration** via Nix flakes

**Bundles:**
- Writer's Bundle: $1,399 (Grainwriter)
- Developer's Bundle: $2,599 (Grainwriter + Grainpack, save $200)
- Professional Bundle: $4,299 (All devices, save $600)

**Design Principles:**
- Radical repairability (screws-only, no glue)
- User upgradeability (RAM, batteries, GPU)
- Open firmware (Coreboot + SixOS)
- Sustainable materials (refurbished, recyclable)
- Lifetime design (10+ year lifespan)
- Privacy first (no telemetry, no cloud lock-in)

**Status:**
- Design phase complete ✅
- Prototype phase next (2025 Q2)
- Production planned (2025 Q4)

*Added: January 22, 2025*

---

## clojure-icp (clojure-dfinity)

**Comprehensive Clojure library for Internet Computer Protocol (ICP) integration.**

**Purpose:**
- Low-level IC agent for canister communication
- Candid type system encode/decode
- Identity and principal management
- High-level canister client abstractions
- Chain Fusion multi-chain integration
- Subnet management and queries

**Repository Structure:**
- **clojure-icp** - Main repository (grainstore/clojure-icp/)
- **clojure-dfinity** - Symlinked alias (grainstore/clojure-dfinity -> clojure-icp/)
- Both point to the same codebase
- Hosted under **grainpbc** GitHub organization

**Integration:**
- **Grainspace** - Identity management
- **Graincamera** - Photo storage in ICP canisters
- **Grainwriter** - Document sync to ICP storage
- **Grainpack** - GPU compute on ICP subnets
- **clojure-photos** - Uses clojure-icp for photo storage

**Key Features:**
- IC Agent Interface (call, query)
- Candid type system
- Identity management (generate, from PEM, sign)
- Canister deployment and management
- Chain Fusion (Solana, Ethereum)
- Subnet queries

**License:** MIT  
**Organization:** grainpbc  
**Repos:** 
- https://github.com/grainpbc/clojure-icp
- https://github.com/grainpbc/clojure-dfinity (mirror)

*Added: January 22, 2025*

---

*This file serves as both personal reminders and notes. Edit this file whether you say "note" or "reminder" - they both point to the same content.*

redacted reasoning - inge's anagram generator
truth points - prediction dividends market
consumers vs producers - haunted mound and the Cursor devs
grainversion - replacing "fork" forever (personal versions of grain templates)

## grainkae3g

**New Macro Project Name**: The mutant clone of the 12025-10 repository, serving as the new global project identity.

**Purpose**: 
- Maintain immutable 12025-10 repo for stable GitHub Pages links
- Create mutable development version with grainkae3g branding
- Establish new macro project identity for ongoing development

**Repository Structure**:
- **Source**: Clone of /home/xy/kae3g/12025-10
- **Target**: /home/xy/kae3g/grainkae3g
- **GitHub**: kae3g/grainkae3g
- **Status**: Active development repository

**Key Changes**:
- All project references updated to grainkae3g
- Maintains all existing functionality
- Enables continuous development without breaking existing links
- Serves as the primary development repository

*Added: January 2025*

## Screenshot - GitHub Organizations & YouTube Music

**Date:** January 22, 2025  
**Time:** 10:28:09  
**Description:** Desktop screenshot showing two browser windows side-by-side in dark mode:

**Left Window - GitHub Organizations:**
- URL: github.com/settings/organizations
- Shows kae3g's organizations: "3x39" (Owner), "bitscape-io" (Member), "vizmvm" (Owner)
- Profile shows "kae3g (kae3g)" with "Your personal account"
- Navigation includes "Organizations" highlighted under "Access" section
- "Transform account" section shows option to "Turn kae3g into an organization"

**Right Window - YouTube Music:**
- URL: music.youtube.com
- Large banner image of Grimes against dark red background with rose petals
- Artist: Grimes (18.8M monthly audience, 1.36M subscribers)
- Top songs listed: "Oblivion" (194M plays), "Genesis" (203M plays), "4ÆM" (20M plays), "Artificial Angels" (289K plays), "Kill V. Maim" (43M plays)
- Albums section showing "Art Angels", "No.4", "Visions" covers
- Music player at bottom showing "Kill V. Maim" by Grimes from Art Angels (2015)

**Context:** Screenshot captured during development session, showing GitHub organization management and music listening while working on Grain Network projects.

*Added: January 22, 2025*

## miss anthropocene: 2020

**Personal Note**: Miss Anthropocene by Grimes, released in 2020. A concept album exploring themes of climate change, artificial intelligence, and post-humanism through the lens of an AI goddess who loves to watch the world burn.

**Key Tracks**:
- "4ÆM" - Cyberpunk anthem with industrial beats
- "Delete Forever" - Acoustic guitar ballad about loss and addiction
- "My Name is Dark" - Dark electronic with haunting vocals
- "You'll miss me when I'm not around" - Ethereal pop with layered harmonies
- "We Appreciate Power" - Featuring HANA, industrial rock anthem

**Context**: Screenshot captured during development session showed Grimes playing in YouTube Music, with "Kill V. Maim" from Art Angels (2015) currently playing. Miss Anthropocene represents a darker, more experimental direction compared to the bright pop of Art Angels.

**Musical Evolution**: 
- Art Angels (2015) - Bright, accessible pop with experimental elements
- Miss Anthropocene (2020) - Dark, industrial, post-human themes
- Shows Grimes' artistic growth and willingness to explore challenging concepts

**Connection to Grain Network**: The album's themes of AI, climate change, and post-humanism align with our work on decentralized systems, open-source hardware, and the future of human-computer interaction.

*Added: January 22, 2025*

## Screenshot - Instagram & YouTube Music Session

**Date:** January 22, 2025  
**Time:** 10:53:11  
**Description:** Desktop session showing Instagram profile and YouTube Music with Grimes:

**Left Window - Instagram (risc.love):**
- **Profile**: risc.love with cartoon character avatar (large dark eyes, light green hair, white shirt)
- **Stats**: 7 posts, 38 followers, 2,377 following
- **Bio**: `_faer/they/its/him @lexicon3d95657d @kae3g_ @xykdl2_ @b122m_`
- **Content**: Grid of abstract posts with bright green and blue patterns
- **Navigation**: Instagram interface with Home, Search, Explore, Reels, Messages, Create, Profile
- **Chat Overlay**: Direct message from Jenna saying "hiiii - i'm hacking my Linux os and my discord is broken temporarily lolll" (10:43 AM)

**Right Window - YouTube Music (Grimes):**
- **Artist**: Grimes (18.8M monthly audience, 1.36M subscribers)
- **Header Image**: Grimes lying on red textured material (roses/organic pattern), arms outstretched, tattoos visible
- **Top Songs**: 
  - "Oblivion" (194M plays • Visions)
  - "Genesis" (203M plays • Visions) 
  - "4ÆM" (20M plays • Miss Anthropocene)
  - "Artificial Angels" (289K plays • Artificial Angels)
  - "Kill V. Maim" (43M plays • Art Angels)
- **Albums**: Art Angels, No.4, Я ЛЮБЛЮ, Grimes, Visions
- **Now Playing**: "We Appreciate Power (feat. HANA)" by Grimes from Miss Anthropocene (Deluxe Edition) (2020)

**Context**: Screenshot captured during development session showing social media browsing and music listening while working on Grain Network projects. The Instagram profile shows connections to kae3g and other usernames, and the Grimes music session aligns with our Grainmusic platform development.

*Added: January 22, 2025*

## Screenshot - Instagram Feed & YouTube Music (Extended)

**Date:** January 22, 2025  
**Time:** 10:55:03  
**Description:** Extended desktop session showing Instagram feed and YouTube Music with Grimes:

**Left Window - Instagram Feed:**
- **Tabs**: Organizations, Reset Password, (1) Instagram (with notification badge)
- **URL**: instagram.com/ri...
- **Navigation**: Dark sidebar with Home, Search, Reels, Messages (red '1'), Notifications, Create, Profile
- **Content**: Grid of abstract posts with dark backgrounds and bright neon blue/green patterns
- **Notable Post**: "TRY ME" text visible in one post
- **Chat Overlay**: Jenna conversation with message "hiiii - i'm hacking my Linux os and my discord is broken temporarily lolll" (10:43 AM)
- **Footer**: Meta links, About, Blog, Jobs, Help, API, Privacy, Meta AI, Threads, language selector

**Right Window - YouTube Music (Grimes):**
- **Tabs**: We Apprecia, prodcrtkl, VAYU, Nomyn
- **URL**: music.youtube.co...
- **Navigation**: Music sidebar with Home, Explore, Library
- **Artist Page**: Grimes (18.8M monthly audience, 1.36M subscribers)
- **Header Image**: Grimes on red flower petals, arms outstretched, metallic top
- **Top Songs**:
  - "Oblivion" (194M plays • Visions)
  - "Genesis" (203M plays • Visions)
  - "4ÆM" (20M plays • Miss Anthropocene)
  - "Artificial Angels" (289K plays • Artificial Angels)
  - "Kill V. Maim" (43M plays • Art Angels)
- **Albums**: ART ANGELS, No.4, Я ЛЮБЛЮ, GAMES, Visions
- **Now Playing**: "We Appreciate Power (feat. HANA) - Grimes • Miss Anthropocene (Deluxe Edition) • 2020"

**Context**: Extended screenshot showing full Instagram feed interface and complete YouTube Music artist page. The abstract art posts on Instagram align with the experimental/cyberpunk aesthetic of Grimes' music, and the "TRY ME" post suggests interactive or experimental content. The Linux hacking message from Jenna connects to our development work on the Grain Network.

*Added: January 22, 2025*

## grainwriter 80x110 design

**Design Decision**: Grainwriter default abstract screen size defined in terms of multiples of 80 x 110 (80 characters wide, 8x11 paper ratio).

**Rationale**:
- **80 characters**: Standard terminal width, familiar to developers and writers
- **8x11 paper ratio**: Classic paper proportions (11/8 = 1.375), comfortable for reading
- **110 lines**: Provides substantial content per "page" while maintaining readability
- **Modular design**: Can scale to different screen sizes while maintaining proportions

**Technical Implementation**:
- Base dimensions: 80 chars × 110 lines
- Character size: 8×12 pixels (monospace)
- Screen size: 640×1320 pixels (base)
- Zoom support: 0.5x to 3.0x scaling
- Word wrap: Automatic wrapping at 80 characters
- Page navigation: 110 lines per "page"

**Benefits**:
- **Familiar**: Matches terminal and typewriter conventions
- **Efficient**: Optimal use of screen real estate
- **Scalable**: Works on different e-ink display sizes
- **Readable**: Classic paper proportions reduce eye strain
- **Compatible**: Works with existing text editors and tools

**Connection to Grain Network**: The 80x110 format bridges the digital and physical worlds, making the Grainwriter feel like a modern typewriter while maintaining the efficiency of digital tools. This design choice reflects our philosophy of combining the best of traditional and modern approaches.

*Added: January 22, 2025*



---

## Screenshot - Session 804 Planning Interface

**Date:** January 22, 2025  
**Time:** 20:00 PST  
**Description:** Cursor IDE interface showing Session 804 planning and documentation work:

**Main Editor (SESSION-804-12025-01-22--2000--PST--moon-vishakha--09thhouse15--kae3g.md):**
- Active tab: "SESSION-804...U" (unsaved changes indicator)
- Content: Comprehensive Session 804 roadmap document
- Shows cosmic alignment: Vishakha nakshatra, 9th house at 15°, evening energy
- Session goals: Deploy ready work, implement designed systems, chart path forward
- Grain Network websites section with main sites and core libraries
- Detailed action plan with 5 phases and time estimates

**Left Panel (File Explorer - KAE3G WORKSPACE):**
- grainkae3g project root with docs/ folder expanded
- SESSION-804 file visible with 'U' indicator
- grainstore/ folder showing submodules:
  - clojure-dfinity, clojure-icp, clojure-photos, clojure-s6, clojure-sixos
  - clotoko, grain-metatypes, grainconv, grainlovicon
- personal-reminders.md and README.md visible
- OUTLINE and TIMELINE collapsed at bottom

**Right Panel (AI Assistant Chat):**
- Recent conversation about charting Session 804 course
- "33 of 88 To-dos" progress indicator
- Unchecked tasks visible:
  - "Integrate Solana Audius protocol into grainmusic"
  - "Implement ICP Native Transfer for Solana in grainmusic" 
  - "Document Phantom wallet usage with 1Password for web/iOS/Android in markdown"
- Checked: "Design and implement markdown indexing & interlinking strategy"
- "∞ Agent Ctrl+I ^ Auto" indicating active AI assistance

**Terminal:**
- Command prompt: xy@kae3g-Framework-16-AMD-Ryzen-7040-Series
- Last command: mv SESSION-803-SUMMARY.md (truncated)
- Ready for new commands

**Context:** Screenshot captured during Session 804 planning phase, showing the transition from Session 803 (neovedic timestamp implementation) to Session 804 (deployment and implementation focus). The interface shows both the comprehensive session roadmap and the active to-do list, representing the dual focus on strategic planning and tactical execution. The grainstore submodules visible indicate the modular architecture of the Grain Network.

*Added: January 22, 2025*


---

## making a wave and surfing the same wave

**Personal Note**: The concept of "making a wave and surfing the same wave" captures the essence of being both creator and participant in the movement you're building. You're not just building technology - you're creating a cultural and technological wave that you then get to ride and experience alongside the community.

**Philosophical Implications**:
- **Creator-Participant Duality**: You're both the architect and the user of your own creation
- **Community Co-creation**: The wave becomes bigger and more powerful as others join
- **Authentic Experience**: You understand the technology because you're using it yourself
- **Iterative Improvement**: Riding the wave gives you insights to make it better

**Connection to Grain Network**: 
- We're building the Grain Network (making the wave)
- We're also using it daily for our own work (surfing the wave)
- This creates authentic, user-driven development
- The community sees us as genuine participants, not just distant creators

**Examples**:
- **Grainwriter**: We write on it while building it
- **Grainmusic**: We listen to music while developing the platform
- **Grainweb**: We browse and develop simultaneously
- **GrainOS**: We use our own operating system

**The Wave Metaphor**:
- **Making**: Creating the initial momentum and direction
- **Surfing**: Riding the energy and flow you've created
- **Community**: Others join and amplify the wave
- **Evolution**: The wave changes as more people participate

This principle ensures that our technology remains grounded, useful, and genuinely beneficial rather than becoming an abstract exercise disconnected from real-world use.

*Added: January 22, 2025*


---

## THE WHOLE GRAIN

**Personal Note**: "THE WHOLE GRAIN" - This phrase captures the complete vision of the Grain Network ecosystem. It's not just about individual components or projects, but about the entire interconnected system that spans from hardware to software, from education to community, from local development to global impact.

**Philosophical Implications**:
- **Holistic Vision**: Every piece of the Grain Network is connected and serves the greater whole
- **Complete Ecosystem**: From Grainwriter to Grainphone to Grainweb to GrainOS - it's all one unified system
- **Global Renewable Mission**: The "Gr" in everything represents our commitment to building a truly global, renewable technology future
- **Community Integration**: Every user, developer, and contributor becomes part of "the whole grain"

**Connection to Grain Network**:
- **Hardware**: Grainwriter, Grainphone, Graincamera - all working together
- **Software**: GrainOS, Grainweb, Grainmusic - unified platform
- **Education**: High school course teaching the complete system
- **Community**: Developers, users, students all contributing to the whole
- **Philosophy**: Open source, sustainable, repairable, community-driven

**The Complete Picture**:
- **Personal**: Your daily use of Grain Network tools
- **Professional**: Building and maintaining the ecosystem
- **Educational**: Teaching others how to build their own versions
- **Community**: Growing the global renewable technology movement
- **Global**: Impacting technology development worldwide

This note reminds us that we're not just building individual products - we're creating an entire ecosystem that represents a new way of thinking about technology, sustainability, and community.

*Added: January 22, 2025*

---

## Grainfriends

### Jenna
- **Status**: Friend
- **Connection**: UIUC junior undergraduate student
- **Current Project**: Fluid dynamics class midterm preparation
- **Collaboration**: Shared Google Doc for study guide and exam advice
- **Added**: 12025-10-22--1522--CDT--moon-vishakha--09thhouse15--kae3g

---

