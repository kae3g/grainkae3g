// Grainkae3gcontract - Kae3g Sheaf Grain6 Clotoko Canister
// Hosts {88} list of ICP, Hedera, and Solana Phantom public addresses
// Implements 88 counter philosophy with append-only session management

import Array "mo:base/Array";
import Blob "mo:base/Blob";
import HashMap "mo:base/HashMap";
import Hash "mo:base/Hash";
import Iter "mo:base/Iter";
import Nat "mo:base/Nat";
import Principal "mo:base/Principal";
import Text "mo:base/Text";
import Time "mo:base/Time";

actor Grainkae3gcontract {
    
    // 88 Counter Philosophy: 88 × 10^n >= 0 | -1
    private let SHEAF_COUNT : Nat = 88;
    private let KAE3G_SHEAF_POSITION : Nat = 1; // 1-of-88
    
    // Type definitions for multi-chain addresses
    public type ChainType = {
        #ICP;
        #Hedera;
        #Solana;
    };
    
    public type PublicAddress = {
        chain: ChainType;
        address: Text;
        sheafPosition: Nat; // 1-of-88 through 88-of-88
        timestamp: Int;
        grainpath: Text;
    };
    
    public type Session = {
        sessionNumber: Nat;
        grainpath: Text;
        timestamp: Int;
        addresses: [PublicAddress];
    };
    
    // Stable storage for append-only session management
    private stable var sessionCounter : Nat = 0;
    private stable var sessionEntries : [(Nat, Session)] = [];
    
    // HashMap for efficient lookups
    private var sessions = HashMap.HashMap<Nat, Session>(
        10,
        Nat.equal,
        Hash.hash
    );
    
    // Initialize with existing sessions
    for ((id, session) in sessionEntries.vals()) {
        sessions.put(id, session);
    };
    
    // 88 Sheaf addresses storage
    private stable var sheafAddresses : [PublicAddress] = [];
    
    // System upgrade hooks
    system func preupgrade() {
        sessionEntries := Iter.toArray(sessions.entries());
    };
    
    system func postupgrade() {
        sessionEntries := [];
    };
    
    // Add a new public address to the {88} list
    public shared func addPublicAddress(
        chain: ChainType,
        address: Text,
        sheafPosition: Nat,
        grainpath: Text
    ) : async Nat {
        assert(sheafPosition >= 1 and sheafPosition <= SHEAF_COUNT);
        
        let newAddress : PublicAddress = {
            chain = chain;
            address = address;
            sheafPosition = sheafPosition;
            timestamp = Time.now();
            grainpath = grainpath;
        };
        
        sheafAddresses := Array.append(sheafAddresses, [newAddress]);
        Array.size(sheafAddresses)
    };
    
    // Create a new session with incremented number
    public shared func createSession(
        grainpath: Text,
        addresses: [PublicAddress]
    ) : async Nat {
        sessionCounter += 1;
        
        let newSession : Session = {
            sessionNumber = sessionCounter;
            grainpath = grainpath;
            timestamp = Time.now();
            addresses = addresses;
        };
        
        sessions.put(sessionCounter, newSession);
        sessionCounter
    };
    
    // Get all sessions (append-only log)
    public query func getAllSessions() : async [Session] {
        Iter.toArray(sessions.vals())
    };
    
    // Get session by number
    public query func getSession(sessionNumber: Nat) : async ?Session {
        sessions.get(sessionNumber)
    };
    
    // Get all {88} sheaf addresses
    public query func getAllSheafAddresses() : async [PublicAddress] {
        sheafAddresses
    };
    
    // Get addresses by chain type
    public query func getAddressesByChain(chain: ChainType) : async [PublicAddress] {
        Array.filter<PublicAddress>(
            sheafAddresses,
            func (addr) { addr.chain == chain }
        )
    };
    
    // Get addresses by sheaf position
    public query func getAddressesBySheafPosition(position: Nat) : async [PublicAddress] {
        Array.filter<PublicAddress>(
            sheafAddresses,
            func (addr) { addr.sheafPosition == position }
        )
    };
    
    // Get total session count (now counter: now == next + 1)
    public query func getSessionCount() : async Nat {
        sessionCounter
    };
    
    // Get the 88 counter philosophy metadata
    public query func get88CounterMetadata() : async {
        sheafCount: Nat;
        kae3gPosition: Nat;
        totalAddresses: Nat;
        totalSessions: Nat;
        philosophy: Text;
    } {
        {
            sheafCount = SHEAF_COUNT;
            kae3gPosition = KAE3G_SHEAF_POSITION;
            totalAddresses = Array.size(sheafAddresses);
            totalSessions = sessionCounter;
            philosophy = "88 × 10^n >= 0 | -1, now == next + 1, kae3g sheaf (1-of-88)";
        }
    };
    
    // Batch add addresses for all 88 sheaves
    public shared func batchAddAddresses(addresses: [PublicAddress]) : async Nat {
        for (addr in addresses.vals()) {
            assert(addr.sheafPosition >= 1 and addr.sheafPosition <= SHEAF_COUNT);
        };
        
        sheafAddresses := Array.append(sheafAddresses, addresses);
        Array.size(sheafAddresses)
    };
}
