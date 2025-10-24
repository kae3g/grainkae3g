use ic_cdk_macros::{query, update};
use serde::{Deserialize, Serialize};
use candid::CandidType;
use std::collections::HashMap;

#[derive(Serialize, Deserialize, Clone, Debug, CandidType)]
pub enum IdentityType {
    Galaxy,
    Star,
    Planet,
    Moon,
}

#[derive(Serialize, Deserialize, Clone, CandidType)]
pub enum IdentityStatus {
    Active,
    Suspended,
    Revoked,
    Pending,
}

#[derive(Serialize, Deserialize, Clone, CandidType)]
pub struct IdentityMetadata {
    pub name: String,
    pub description: String,
    pub avatar: Option<String>,
    pub website: Option<String>,
}

#[derive(Serialize, Deserialize, Clone, CandidType)]
pub struct UrbitIdentity {
    pub address: u64,
    pub parent: Option<u64>,
    pub children: Vec<u64>,
    pub identity_type: IdentityType,
    pub metadata: IdentityMetadata,
    pub created_at: u64,
    pub updated_at: u64,
    pub status: IdentityStatus,
}

#[derive(Serialize, Deserialize, CandidType)]
pub struct CreateIdentityArgs {
    pub name: String,
    pub description: String,
    pub identity_type: IdentityType,
}

// Canister state
static mut IDENTITIES: Option<HashMap<u64, UrbitIdentity>> = None;
static mut NEXT_ADDRESS: Option<u64> = None;
static mut LOGS: Option<Vec<String>> = None;

fn get_identities() -> &'static mut HashMap<u64, UrbitIdentity> {
    unsafe {
        if IDENTITIES.is_none() {
            IDENTITIES = Some(HashMap::new());
        }
        IDENTITIES.as_mut().unwrap()
    }
}

fn get_next_address() -> &'static mut u64 {
    unsafe {
        if NEXT_ADDRESS.is_none() {
            NEXT_ADDRESS = Some(1);
        }
        NEXT_ADDRESS.as_mut().unwrap()
    }
}

fn get_logs_mut() -> &'static mut Vec<String> {
    unsafe {
        if LOGS.is_none() {
            LOGS = Some(Vec::new());
        }
        LOGS.as_mut().unwrap()
    }
}

fn log_message(message: String) {
    let logs = get_logs_mut();
    logs.push(format!("[{}] {}", ic_cdk::api::time(), message));
    if logs.len() > 1000 {
        logs.remove(0);
    }
}

#[update]
pub fn create_identity(args: CreateIdentityArgs) -> Result<u64, String> {
    log_message(format!("Creating identity: {}", args.name));
    
    let identities = get_identities();
    let next_address = get_next_address();
    
    let identity = UrbitIdentity {
        address: *next_address,
        parent: None,
        children: Vec::new(),
        identity_type: args.identity_type,
        metadata: IdentityMetadata {
            name: args.name,
            description: args.description,
            avatar: None,
            website: None,
        },
        created_at: ic_cdk::api::time(),
        updated_at: ic_cdk::api::time(),
        status: IdentityStatus::Active,
    };
    
    identities.insert(*next_address, identity);
    let address = *next_address;
    *next_address += 1;
    
    log_message(format!("Created identity with address: {}", address));
    Ok(address)
}

#[query]
pub fn get_identity(address: u64) -> Result<UrbitIdentity, String> {
    let identities = get_identities();
    identities.get(&address)
        .cloned()
        .ok_or_else(|| "Identity not found".to_string())
}

#[update]
pub fn update_identity(address: u64, identity: UrbitIdentity) -> Result<(), String> {
    log_message(format!("Updating identity: {}", address));
    
    let identities = get_identities();
    if identities.contains_key(&address) {
        identities.insert(address, identity);
        log_message(format!("Updated identity: {}", address));
        Ok(())
    } else {
        Err("Identity not found".to_string())
    }
}

#[update]
pub fn delete_identity(address: u64) -> Result<(), String> {
    log_message(format!("Deleting identity: {}", address));
    
    let identities = get_identities();
    if identities.remove(&address).is_some() {
        log_message(format!("Deleted identity: {}", address));
        Ok(())
    } else {
        Err("Identity not found".to_string())
    }
}

#[update]
pub fn allocate_address(identity_type: IdentityType) -> Result<u64, String> {
    let next_address = get_next_address();
    let address = *next_address;
    *next_address += 1;
    
    log_message(format!("Allocated address {} for type {:?}", address, identity_type));
    Ok(address)
}

#[query]
pub fn get_address_info(address: u64) -> Result<String, String> {
    let identities = get_identities();
    if let Some(identity) = identities.get(&address) {
        Ok(format!("Address {}: {} ({:?})", 
                   address, 
                   identity.metadata.name, 
                   identity.identity_type))
    } else {
        Err("Address not found".to_string())
    }
}

#[update]
pub fn update_metadata(address: u64, metadata: IdentityMetadata) -> Result<(), String> {
    log_message(format!("Updating metadata for identity: {}", address));
    
    let identities = get_identities();
    if let Some(identity) = identities.get_mut(&address) {
        identity.metadata = metadata;
        identity.updated_at = ic_cdk::api::time();
        log_message(format!("Updated metadata for identity: {}", address));
        Ok(())
    } else {
        Err("Identity not found".to_string())
    }
}

#[query]
pub fn get_metadata(address: u64) -> Result<IdentityMetadata, String> {
    let identities = get_identities();
    identities.get(&address)
        .map(|identity| identity.metadata.clone())
        .ok_or_else(|| "Identity not found".to_string())
}

#[derive(Serialize, Deserialize, CandidType)]
pub struct Stats {
    pub total_identities: u64,
    pub active_identities: u64,
}

#[query]
pub fn get_stats() -> Stats {
    let identities = get_identities();
    let total = identities.len() as u64;
    let active = identities.values()
        .filter(|identity| matches!(identity.status, IdentityStatus::Active))
        .count() as u64;
    Stats {
        total_identities: total,
        active_identities: active,
    }
}

#[query]
pub fn get_logs() -> Vec<String> {
    get_logs_mut().clone()
}