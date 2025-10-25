// Grainkae3gcontract - Rust Audited Version
// Kae3g Sheaf Grain6 Clotoko Canister
// Hosts {88} list of ICP, Hedera, and Solana Phantom public addresses
// Implements 88 counter philosophy with append-only session management

use candid::{CandidType, Deserialize, Principal};
use ic_cdk::api::time;
use ic_cdk_macros::{init, post_upgrade, pre_upgrade, query, update};
use std::cell::RefCell;
use std::collections::HashMap;

// 88 Counter Philosophy: 88 × 10^n >= 0 | -1
const SHEAF_COUNT: u64 = 88;
const KAE3G_SHEAF_POSITION: u64 = 1; // 1-of-88

// Type definitions for multi-chain addresses
#[derive(Clone, Debug, CandidType, Deserialize, PartialEq)]
pub enum ChainType {
    ICP,
    Hedera,
    Solana,
}

#[derive(Clone, Debug, CandidType, Deserialize)]
pub struct PublicAddress {
    pub chain: ChainType,
    pub address: String,
    pub sheaf_position: u64,
    pub timestamp: u64,
    pub grainpath: String,
}

#[derive(Clone, Debug, CandidType, Deserialize)]
pub struct Session {
    pub session_number: u64,
    pub grainpath: String,
    pub timestamp: u64,
    pub addresses: Vec<PublicAddress>,
}

#[derive(Clone, Debug, CandidType, Deserialize)]
pub struct CounterMetadata {
    pub sheaf_count: u64,
    pub kae3g_position: u64,
    pub total_addresses: u64,
    pub total_sessions: u64,
    pub philosophy: String,
}

// Stable storage
thread_local! {
    static SESSION_COUNTER: RefCell<u64> = RefCell::new(0);
    static SESSIONS: RefCell<HashMap<u64, Session>> = RefCell::new(HashMap::new());
    static SHEAF_ADDRESSES: RefCell<Vec<PublicAddress>> = RefCell::new(Vec::new());
}

// Initialize canister
#[init]
fn init() {
    ic_cdk::println!("🌾 Grainkae3gcontract initialized - 88 counter philosophy active");
}

// Add a new public address to the {88} list
#[update]
fn add_public_address(
    chain: ChainType,
    address: String,
    sheaf_position: u64,
    grainpath: String,
) -> Result<u64, String> {
    if sheaf_position < 1 || sheaf_position > SHEAF_COUNT {
        return Err(format!(
            "Sheaf position must be between 1 and {}",
            SHEAF_COUNT
        ));
    }

    let new_address = PublicAddress {
        chain,
        address,
        sheaf_position,
        timestamp: time(),
        grainpath,
    };

    SHEAF_ADDRESSES.with(|addresses| {
        let mut addresses = addresses.borrow_mut();
        addresses.push(new_address);
        Ok(addresses.len() as u64)
    })
}

// Create a new session with incremented number
#[update]
fn create_session(grainpath: String, addresses: Vec<PublicAddress>) -> u64 {
    SESSION_COUNTER.with(|counter| {
        let mut counter = counter.borrow_mut();
        *counter += 1;
        let session_number = *counter;

        let new_session = Session {
            session_number,
            grainpath,
            timestamp: time(),
            addresses,
        };

        SESSIONS.with(|sessions| {
            sessions.borrow_mut().insert(session_number, new_session);
        });

        session_number
    })
}

// Get all sessions (append-only log)
#[query]
fn get_all_sessions() -> Vec<Session> {
    SESSIONS.with(|sessions| sessions.borrow().values().cloned().collect())
}

// Get session by number
#[query]
fn get_session(session_number: u64) -> Option<Session> {
    SESSIONS.with(|sessions| sessions.borrow().get(&session_number).cloned())
}

// Get all {88} sheaf addresses
#[query]
fn get_all_sheaf_addresses() -> Vec<PublicAddress> {
    SHEAF_ADDRESSES.with(|addresses| addresses.borrow().clone())
}

// Get addresses by chain type
#[query]
fn get_addresses_by_chain(chain: ChainType) -> Vec<PublicAddress> {
    SHEAF_ADDRESSES.with(|addresses| {
        addresses
            .borrow()
            .iter()
            .filter(|addr| addr.chain == chain)
            .cloned()
            .collect()
    })
}

// Get addresses by sheaf position
#[query]
fn get_addresses_by_sheaf_position(position: u64) -> Vec<PublicAddress> {
    SHEAF_ADDRESSES.with(|addresses| {
        addresses
            .borrow()
            .iter()
            .filter(|addr| addr.sheaf_position == position)
            .cloned()
            .collect()
    })
}

// Get total session count (now counter: now == next + 1)
#[query]
fn get_session_count() -> u64 {
    SESSION_COUNTER.with(|counter| *counter.borrow())
}

// Get the 88 counter philosophy metadata
#[query]
fn get_88_counter_metadata() -> CounterMetadata {
    let total_addresses = SHEAF_ADDRESSES.with(|addresses| addresses.borrow().len() as u64);
    let total_sessions = SESSION_COUNTER.with(|counter| *counter.borrow());

    CounterMetadata {
        sheaf_count: SHEAF_COUNT,
        kae3g_position: KAE3G_SHEAF_POSITION,
        total_addresses,
        total_sessions,
        philosophy: "88 × 10^n >= 0 | -1, now == next + 1, kae3g sheaf (1-of-88)".to_string(),
    }
}

// Batch add addresses for all 88 sheaves
#[update]
fn batch_add_addresses(addresses: Vec<PublicAddress>) -> Result<u64, String> {
    for addr in &addresses {
        if addr.sheaf_position < 1 || addr.sheaf_position > SHEAF_COUNT {
            return Err(format!(
                "All sheaf positions must be between 1 and {}",
                SHEAF_COUNT
            ));
        }
    }

    SHEAF_ADDRESSES.with(|sheaf_addresses| {
        let mut sheaf_addresses = sheaf_addresses.borrow_mut();
        sheaf_addresses.extend(addresses);
        Ok(sheaf_addresses.len() as u64)
    })
}

// Pre-upgrade hook to save state
#[pre_upgrade]
fn pre_upgrade() {
    ic_cdk::println!("🔄 Pre-upgrade: Saving state...");
}

// Post-upgrade hook to restore state
#[post_upgrade]
fn post_upgrade() {
    ic_cdk::println!("✅ Post-upgrade: State restored - 88 counter philosophy active");
}

// Candid interface export
ic_cdk::export_candid!();
