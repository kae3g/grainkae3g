# Clotoko

**Clojure to Motoko transpiler for ICP canister development**

A hybrid language that compiles Clojure syntax to Motoko, enabling functional programming for Internet Computer Protocol (ICP) canisters while maintaining Clojure's expressiveness and Lisp heritage.

## Vision

Just as ClojureScript brings Clojure to JavaScript, Clotoko brings Clojure to Motoko, enabling:
- **Functional programming** on ICP canisters
- **Immutability by default** with persistent data structures
- **REPL-driven development** for canister development
- **Clojure ecosystem** integration (spec, core.async, etc.)
- **Motoko performance** with Clojure syntax

## Key Features

### Syntax Mapping
```clojure
;; Clojure (Clotoko)
(defn add-peer [peer-id endpoint]
  (let [peer {:peer-id peer-id
              :endpoint endpoint
              :last-seen (time/now)}]
    (swap! peer-index assoc peer-id peer)))

;; Compiles to Motoko
public func addPeer(peerId : Text, endpoint : Endpoint) : async () {
  let peer = {
    peerId = peerId;
    endpoint = endpoint;
    lastSeen = Time.now();
  };
  peerIndex := Map.put(peerIndex, peerId, peer);
}
```

### Type System Integration
```clojure
;; Clotoko with type hints
(defn find-route [^Path path ^PeerIndex peer-index]
  (get peer-index path))

;; Compiles to strongly-typed Motoko
public func findRoute(path : Path, peerIndex : PeerIndex) : ?Route {
  Map.get(peerIndex, path)
}
```

### ICP Integration
```clojure
;; Clotoko canister functions
(defcanister grainweb-daemon
  (defn init []
    (reset! state {:peers {} :subscriptions {}}))
  
  (defn add-subscription [peer-id path]
    (swap! state update :subscriptions assoc peer-id path))
  
  (defn query-peers []
    (vals (:peers @state))))
```

## Architecture

### Compilation Pipeline
1. **Clojure AST** - Parse clomoko source to Clojure AST
2. **Type Inference** - Infer Motoko types from Clojure forms
3. **Motoko AST** - Transform to Motoko abstract syntax tree
4. **Code Generation** - Generate Motoko source code
5. **ICP Integration** - Add canister boilerplate

### Core Components
- **clotoko.core** - Main transpiler engine
- **clotoko.types** - Type system and inference
- **clotoko.motoko** - Motoko code generation
- **clotoko.icp** - ICP canister integration
- **clotoko.stdlib** - Standard library for ICP

## Installation

```bash
# Add to deps.edn
{:deps {grainpbc/clotoko {:git/url "https://github.com/grainpbc/clotoko"
                          :sha "..."}}}

# Or via Leiningen
[grainpbc/clotoko "0.1.0"]
```

## Usage

### Basic Compilation
```clojure
(require '[clomoko.core :as clomoko])

;; Compile clomoko to Motoko
(clomoko/compile "src/grainweb_daemon.clomoko" "out/Main.mo")
```

### Canister Development
```clojure
;; Define a canister
(defcanister grainweb-daemon
  (defn init []
    (reset! state {:peers {} :subscriptions {}}))
  
  (defn add-peer [peer-id endpoint]
    (swap! state update :peers assoc peer-id endpoint))
  
  (defn query-peers []
    (vals (:peers @state))))

;; Generate canister files
(clomoko/generate-canister grainweb-daemon
  {:output-dir "canisters/grainweb-daemon"
   :candid-file "grainweb_daemon.did"})
```

### Type Annotations
```clojure
;; Type hints for better Motoko generation
(defn process-message [^Message msg ^State state]
  (let [^Text peer-id (:peer-id msg)
        ^Path path (:path msg)]
    (update-state state peer-id path)))

;; Compiles to strongly-typed Motoko
public func processMessage(msg : Message, state : State) : State {
  let peerId = msg.peerId;
  let path = msg.path;
  updateState(state, peerId, path);
}
```

## Standard Library

### ICP Primitives
```clojure
;; Time and cycles
(time/now)           ; Current time
(cycles/balance)     ; Current cycle balance
(cycles/consume n)   ; Consume cycles

;; Storage
(storage/stable-store key value)
(storage/stable-load key)
(storage/stable-remove key)

;; Canister management
(canister/id)        ; Current canister ID
(canister/install-code wasm)
(canister/upgrade-code wasm)
```

### Networking
```clojure
;; HTTP requests
(http/get url)
(http/post url data)
(http/put url data)
(http/delete url)

;; Inter-canister calls
(ic/call canister-id method args)
(ic/query canister-id method args)
```

### Data Structures
```clojure
;; Persistent collections (compiled to Motoko equivalents)
(def peers (atom {}))
(swap! peers assoc "peer1" peer-data)
(get @peers "peer1")

;; Sequences
(map inc [1 2 3])
(filter even? (range 10))
(reduce + 0 [1 2 3 4])
```

## Examples

### Simple Counter Canister
```clojure
(defcanister counter
  (defn init []
    (reset! count 0))
  
  (defn increment []
    (swap! count inc))
  
  (defn decrement []
    (swap! count dec))
  
  (defn get-count []
    @count))
```

### Peer Discovery Canister
```clojure
(defcanister peer-discovery
  (defn init []
    (reset! state {:peers {} :discovery-active? false}))
  
  (defn add-peer [peer-id endpoint capabilities]
    (let [peer {:peer-id peer-id
                :endpoint endpoint
                :capabilities capabilities
                :last-seen (time/now)}]
      (swap! state update :peers assoc peer-id peer)))
  
  (defn discover-peers []
    (let [peers (vals (:peers @state))]
      (filter :active? peers)))
  
  (defn find-peers-for-path [path]
    (->> (vals (:peers @state))
         (filter #(can-handle-path? % path)))))
```

## Development

### Building
```bash
# Clone repository
git clone https://github.com/grainpbc/clomoko.git
cd clomoko

# Install dependencies
lein deps

# Run tests
lein test

# Build
lein uberjar
```

### Contributing
1. Fork the repository
2. Create a feature branch
3. Make changes
4. Add tests
5. Submit a pull request

## License

MIT License - see LICENSE file for details

## Roadmap

- [x] Basic syntax mapping
- [x] Type inference system
- [ ] Motoko code generation
- [ ] ICP canister integration
- [ ] Standard library
- [ ] REPL for canister development
- [ ] Hot reloading
- [ ] Debugging tools
- [ ] Performance optimizations

## Inspiration

- **ClojureScript** - Clojure to JavaScript compilation
- **Motoko** - ICP's functional language
- **Urbit** - Functional operating system concepts
- **Hoon** - Urbit's functional language

## Community

- **GitHub**: https://github.com/grainpbc/clomoko
- **Discussions**: GitHub Discussions
- **Issues**: GitHub Issues
- **Documentation**: https://clomoko.grain.network

---

**clomoko** - Bringing Clojure's power to the Internet Computer Protocol
