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
pub enum VerificationStatus {
    Pending,
    Verified,
    Failed,
    Revoked,
}

#[derive(Serialize, Deserialize, Clone, CandidType)]
pub struct UrbitIdentity {
    pub address: u64,
    pub identity_type: IdentityType,
    pub verified: bool,
    pub created_at: u64,
    pub updated_at: u64,
}

#[derive(Serialize, Deserialize, Clone, CandidType)]
pub struct GrainspaceIdentity {
    pub unified_id: String,
    pub urbit_identity: UrbitIdentity,
    pub icp_principal: String,
    pub verification_status: VerificationStatus,
    pub social_connections: HashMap<String, bool>,
    pub platform_access: HashMap<String, bool>,
    pub created_at: u64,
    pub last_sync: u64,
}

#[derive(Serialize, Deserialize, CandidType)]
pub struct CreateIdentityArgs {
    pub urbit_address: u64,
    pub identity_type: IdentityType,
}

#[derive(Serialize, Deserialize, CandidType)]
pub struct SocialConnection {
    pub platform: String,
    pub connected: bool,
    pub access_token: Option<String>,
    pub connected_at: u64,
}

// Canister state
static mut IDENTITIES: Option<HashMap<String, GrainspaceIdentity>> = None;
static mut NEXT_ID: Option<u64> = None;

fn get_identities() -> &'static mut HashMap<String, GrainspaceIdentity> {
    unsafe {
        if IDENTITIES.is_none() {
            IDENTITIES = Some(HashMap::new());
        }
        IDENTITIES.as_mut().unwrap()
    }
}

fn get_next_id() -> &'static mut u64 {
    unsafe {
        if NEXT_ID.is_none() {
            NEXT_ID = Some(0);
        }
        NEXT_ID.as_mut().unwrap()
    }
}

fn generate_unified_id(urbit_address: u64, icp_principal: &str) -> String {
    format!("~grainspace-{}-{}", urbit_address, icp_principal)
}

#[update]
pub fn create_identity(args: CreateIdentityArgs) -> Result<String, String> {
    let identities = get_identities();
    let next_id = get_next_id();
    
    let id = *next_id;
    *next_id += 1;
    
    let now = ic_cdk::api::time();
    let icp_principal = ic_cdk::api::id().to_string();
    
    let urbit_identity = UrbitIdentity {
        address: args.urbit_address,
        identity_type: args.identity_type,
        verified: true,
        created_at: now,
        updated_at: now,
    };
    
    let unified_id = generate_unified_id(args.urbit_address, &icp_principal);
    
    let grainspace_identity = GrainspaceIdentity {
        unified_id: unified_id.clone(),
        urbit_identity,
        icp_principal,
        verification_status: VerificationStatus::Verified,
        social_connections: HashMap::new(),
        platform_access: HashMap::new(),
        created_at: now,
        last_sync: now,
    };
    
    identities.insert(unified_id.clone(), grainspace_identity);
    
    Ok(unified_id)
}

#[query]
pub fn get_identity(unified_id: String) -> Result<GrainspaceIdentity, String> {
    let identities = get_identities();
    identities.get(&unified_id)
        .cloned()
        .ok_or_else(|| "Identity not found".to_string())
}

#[update]
pub fn connect_social_platform(unified_id: String, platform: String) -> Result<(), String> {
    let identities = get_identities();
    if let Some(identity) = identities.get_mut(&unified_id) {
        identity.social_connections.insert(platform, true);
        identity.last_sync = ic_cdk::api::time();
        Ok(())
    } else {
        Err("Identity not found".to_string())
    }
}

#[update]
pub fn disconnect_social_platform(unified_id: String, platform: String) -> Result<(), String> {
    let identities = get_identities();
    if let Some(identity) = identities.get_mut(&unified_id) {
        identity.social_connections.insert(platform, false);
        identity.last_sync = ic_cdk::api::time();
        Ok(())
    } else {
        Err("Identity not found".to_string())
    }
}

#[update]
pub fn grant_platform_access(unified_id: String, platform: String) -> Result<(), String> {
    let identities = get_identities();
    if let Some(identity) = identities.get_mut(&unified_id) {
        identity.platform_access.insert(platform, true);
        identity.last_sync = ic_cdk::api::time();
        Ok(())
    } else {
        Err("Identity not found".to_string())
    }
}

#[query]
pub fn get_identity_by_urbit_address(urbit_address: u64) -> Result<GrainspaceIdentity, String> {
    let identities = get_identities();
    for identity in identities.values() {
        if identity.urbit_identity.address == urbit_address {
            return Ok(identity.clone());
        }
    }
    Err("Identity not found".to_string())
}

#[query]
pub fn list_identities() -> Vec<String> {
    let identities = get_identities();
    identities.keys().cloned().collect()
}

#[query]
pub fn get_identity_stats() -> HashMap<String, u64> {
    let identities = get_identities();
    let mut stats = HashMap::new();
    
    stats.insert("total_identities".to_string(), identities.len() as u64);
    
    let mut verified_count = 0;
    let mut social_connected = 0;
    
    for identity in identities.values() {
        if matches!(identity.verification_status, VerificationStatus::Verified) {
            verified_count += 1;
        }
        if !identity.social_connections.is_empty() {
            social_connected += 1;
        }
    }
    
    stats.insert("verified_identities".to_string(), verified_count);
    stats.insert("social_connected".to_string(), social_connected);
    
    stats
}

#[update]
pub fn sync_identity(unified_id: String) -> Result<(), String> {
    let identities = get_identities();
    if let Some(identity) = identities.get_mut(&unified_id) {
        identity.last_sync = ic_cdk::api::time();
        identity.verification_status = VerificationStatus::Verified;
        Ok(())
    } else {
        Err("Identity not found".to_string())
    }
}

#[query]
pub fn verify_identity(unified_id: String) -> Result<bool, String> {
    let identities = get_identities();
    if let Some(identity) = identities.get(&unified_id) {
        Ok(matches!(identity.verification_status, VerificationStatus::Verified))
    } else {
        Err("Identity not found".to_string())
    }
}

