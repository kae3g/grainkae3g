# Grainmusic

**Decentralized Music Platform - Clotoko ICP App**

A cross-platform music streaming platform built with Clotoko (Clojure to Motoko transpiler) for ICP canisters, featuring Grimes as the inaugural artist and supporting web, desktop, and mobile deployments.

## Vision

Grainmusic brings the Grain Network philosophy to music - decentralized, open-source, and artist-owned. Built with Clotoko for ICP canisters, it provides:

- **Decentralized Music Storage**: Songs stored in ICP canisters
- **Artist Sovereignty**: Direct artist-to-fan relationships
- **Cross-Platform**: Web, Linux, macOS, Android, iOS
- **Open Source**: Full transparency and community ownership
- **Privacy First**: No tracking, no ads, no data harvesting

## Architecture

### Clotoko ICP Backend
```clojure
;; Clotoko canister for music storage and streaming
(defcanister grainmusic-core
  (defn init []
    (reset! state {:artists {} :albums {} :songs {} :users {}}))
  
  (defn add-artist [artist-data]
    (swap! state update :artists assoc (:id artist-data) artist-data))
  
  (defn add-album [album-data]
    (swap! state update :albums assoc (:id album-data) album-data))
  
  (defn add-song [song-data]
    (swap! state update :songs assoc (:id song-data) song-data))
  
  (defn get-artist [artist-id]
    (get-in @state [:artists artist-id]))
  
  (defn get-album [album-id]
    (get-in @state [:albums album-id]))
  
  (defn get-song [song-id]
    (get-in @state [:songs song-id]))
  
  (defn search-music [query]
    (let [artists (filter #(str/includes? (:name %) query) (vals (:artists @state)))
          albums (filter #(str/includes? (:title %) query) (vals (:albums @state)))
          songs (filter #(str/includes? (:title %) query) (vals (:songs @state)))]
      {:artists artists :albums albums :songs songs})))
```

### Cross-Platform Frontend
- **Web**: SvelteKit + ClojureScript
- **Desktop**: Clojure Humble UI (Linux/macOS)
- **Mobile**: React Native + ClojureScript (Android/iOS)

## Featured Artist: Grimes

### Grimes Integration
```clojure
;; Grimes artist data for Grainmusic
(def grimes-profile
  {:id "grimes"
   :name "Grimes"
   :bio "AI goddess who loves to watch the world burn"
   :genres ["electronic" "pop" "experimental" "cyberpunk"]
   :monthly-listeners 18800000
   :followers 1360000
   :verified true
   :grain-artist true
   :albums ["visions" "art-angels" "miss-anthropocene" "artificial-angels"]
   :top-songs ["oblivion" "genesis" "4aem" "kill-v-maim" "we-appreciate-power"]})

(def grimes-albums
  [{:id "visions"
    :title "Visions"
    :year 2012
    :genre "electronic"
    :songs ["oblivion" "genesis" "circumambient" "eight" "nightmusic"]}
   {:id "art-angels"
    :title "Art Angels"
    :year 2015
    :genre "pop"
    :songs ["kill-v-maim" "flesh-without-blood" "realiti" "world-princess-part-ii"]}
   {:id "miss-anthropocene"
    :title "Miss Anthropocene"
    :year 2020
    :genre "experimental"
    :songs ["4aem" "delete-forever" "my-name-is-dark" "we-appreciate-power"]}])
```

## Technical Stack

### Backend (ICP Canisters)
- **Clotoko**: Clojure to Motoko transpiler
- **ICP Storage**: Decentralized music file storage
- **Candid Interface**: Type-safe canister communication
- **Chain Fusion**: Multi-chain payment integration

### Frontend (Multi-Platform)
- **Web**: SvelteKit + ClojureScript
- **Desktop**: Clojure Humble UI + clojure-s6 + clojure-sixos
- **Mobile**: React Native + ClojureScript
- **Shared**: EDN data format for cross-platform consistency

### Integration
- **Nostr**: Decentralized social features
- **Urbit**: Identity and peer-to-peer sync
- **ICP**: Storage and payments
- **Solana**: Secondary payment option

## Installation

### Web Version
```bash
# Clone repository
git clone https://github.com/grainpbc/grainmusic.git
cd grainmusic

# Install dependencies
npm install

# Start development server
npm run dev
```

### Desktop Version (Clojure Humble UI)
```bash
# Add to deps.edn
{:deps {grainpbc/grainmusic {:git/url "https://github.com/grainpbc/grainmusic"
                            :sha "..."}}}

# Run with Humble UI
clojure -M:grainmusic-desktop
```

### Mobile Version
```bash
# Install React Native dependencies
npm install

# Run on Android
npx react-native run-android

# Run on iOS
npx react-native run-ios
```

## Usage

### Web Interface
```clojure
;; Initialize Grainmusic client
(def client (grainmusic.client/create))

;; Search for music
(def results (grainmusic.client/search client "grimes"))

;; Play a song
(grainmusic.client/play client (:id (first (:songs results))))

;; Get artist profile
(def grimes (grainmusic.client/get-artist client "grimes"))
```

### Desktop App (Clojure Humble UI)
```clojure
;; Start Grainmusic desktop app
(defn -main []
  (humble.ui/run
   (grainmusic.ui/desktop-app)
   {:title "Grainmusic"
    :width 1200
    :height 800}))

;; Desktop app with s6 integration
(defn start-grainmusic-daemon []
  (s6/start-service "grainmusic"
                   {:exec "clojure -M:grainmusic-desktop"
                    :env {"GRAINMUSIC_MODE" "daemon"}}))
```

## Features

### Core Music Features
- **Streaming**: High-quality audio streaming from ICP canisters
- **Offline Playback**: Download songs for offline listening
- **Playlists**: Create and share playlists
- **Search**: Full-text search across artists, albums, and songs
- **Recommendations**: AI-powered music recommendations

### Social Features
- **Artist Profiles**: Direct artist-to-fan communication
- **Comments**: Song and album comments
- **Sharing**: Share music via Nostr and other platforms
- **Following**: Follow artists and other users

### Decentralized Features
- **No Central Authority**: Fully decentralized platform
- **Artist Ownership**: Artists control their content and data
- **Privacy**: No tracking or data harvesting
- **Open Source**: Full transparency and community ownership

## Development

### Building Clotoko Canisters
```bash
# Compile Clotoko to Motoko
clotoko compile src/grainmusic-core.clotoko out/Main.mo

# Deploy to ICP
dfx deploy grainmusic-core
```

### Cross-Platform Build
```bash
# Build all platforms
npm run build:all

# Build specific platform
npm run build:web
npm run build:desktop
npm run build:mobile
```

## License

MIT License - see LICENSE file for details

## Roadmap

- [x] Clotoko ICP canister architecture
- [x] Grimes artist integration
- [ ] Web interface (SvelteKit)
- [ ] Desktop app (Clojure Humble UI)
- [ ] Mobile apps (React Native)
- [ ] Audio streaming optimization
- [ ] Offline playback
- [ ] Social features (Nostr integration)
- [ ] Payment integration (ICP + Solana)
- [ ] Artist dashboard
- [ ] Community features

## Community

- **GitHub**: https://github.com/grainpbc/grainmusic
- **Discussions**: GitHub Discussions
- **Issues**: GitHub Issues
- **Documentation**: https://grainmusic.grain.network

---

**Grainmusic** - Decentralized music for the Grain Network

