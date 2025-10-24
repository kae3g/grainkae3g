import type { Principal } from '@dfinity/principal';
import type { ActorMethod } from '@dfinity/agent';
import type { IDL } from '@dfinity/candid';

export interface CreateIdentityArgs {
  'identity_type' : IdentityType,
  'name' : string,
  'description' : string,
}
export interface IdentityMetadata {
  'name' : string,
  'description' : string,
  'website' : [] | [string],
  'avatar' : [] | [string],
}
export type IdentityStatus = { 'Active' : null } |
  { 'Suspended' : null } |
  { 'Revoked' : null } |
  { 'Pending' : null };
/**
 * Candid interface for Urbit Identity Canister
 */
export type IdentityType = { 'Moon' : null } |
  { 'Star' : null } |
  { 'Planet' : null } |
  { 'Galaxy' : null };
export interface UrbitIdentity {
  'status' : IdentityStatus,
  'identity_type' : IdentityType,
  'updated_at' : bigint,
  'metadata' : IdentityMetadata,
  'created_at' : bigint,
  'children' : BigUint64Array | bigint[],
  'address' : bigint,
  'parent' : [] | [bigint],
}
export interface _SERVICE {
  /**
   * Address space management
   */
  'allocate_address' : ActorMethod<
    [IdentityType],
    { 'Ok' : bigint } |
      { 'Err' : string }
  >,
  /**
   * Identity management
   */
  'create_identity' : ActorMethod<
    [CreateIdentityArgs],
    { 'Ok' : bigint } |
      { 'Err' : string }
  >,
  'delete_identity' : ActorMethod<
    [bigint],
    { 'Ok' : null } |
      { 'Err' : string }
  >,
  'get_address_info' : ActorMethod<
    [bigint],
    { 'Ok' : string } |
      { 'Err' : string }
  >,
  'get_identity' : ActorMethod<
    [bigint],
    { 'Ok' : UrbitIdentity } |
      { 'Err' : string }
  >,
  'get_logs' : ActorMethod<[], Array<string>>,
  'get_metadata' : ActorMethod<
    [bigint],
    { 'Ok' : IdentityMetadata } |
      { 'Err' : string }
  >,
  /**
   * System functions
   */
  'get_stats' : ActorMethod<
    [],
    { 'active_identities' : bigint, 'total_identities' : bigint }
  >,
  'update_identity' : ActorMethod<
    [bigint, UrbitIdentity],
    { 'Ok' : null } |
      { 'Err' : string }
  >,
  /**
   * Metadata management
   */
  'update_metadata' : ActorMethod<
    [bigint, IdentityMetadata],
    { 'Ok' : null } |
      { 'Err' : string }
  >,
}
export declare const idlFactory: IDL.InterfaceFactory;
export declare const init: (args: { IDL: typeof IDL }) => IDL.Type[];
