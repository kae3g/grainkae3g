export const idlFactory = ({ IDL }) => {
  const IdentityType = IDL.Variant({
    'Moon' : IDL.Null,
    'Star' : IDL.Null,
    'Planet' : IDL.Null,
    'Galaxy' : IDL.Null,
  });
  const CreateIdentityArgs = IDL.Record({
    'identity_type' : IdentityType,
    'name' : IDL.Text,
    'description' : IDL.Text,
  });
  const IdentityStatus = IDL.Variant({
    'Active' : IDL.Null,
    'Suspended' : IDL.Null,
    'Revoked' : IDL.Null,
    'Pending' : IDL.Null,
  });
  const IdentityMetadata = IDL.Record({
    'name' : IDL.Text,
    'description' : IDL.Text,
    'website' : IDL.Opt(IDL.Text),
    'avatar' : IDL.Opt(IDL.Text),
  });
  const UrbitIdentity = IDL.Record({
    'status' : IdentityStatus,
    'identity_type' : IdentityType,
    'updated_at' : IDL.Nat64,
    'metadata' : IdentityMetadata,
    'created_at' : IDL.Nat64,
    'children' : IDL.Vec(IDL.Nat64),
    'address' : IDL.Nat64,
    'parent' : IDL.Opt(IDL.Nat64),
  });
  return IDL.Service({
    'allocate_address' : IDL.Func(
        [IdentityType],
        [IDL.Variant({ 'Ok' : IDL.Nat64, 'Err' : IDL.Text })],
        [],
      ),
    'create_identity' : IDL.Func(
        [CreateIdentityArgs],
        [IDL.Variant({ 'Ok' : IDL.Nat64, 'Err' : IDL.Text })],
        [],
      ),
    'delete_identity' : IDL.Func(
        [IDL.Nat64],
        [IDL.Variant({ 'Ok' : IDL.Null, 'Err' : IDL.Text })],
        [],
      ),
    'get_address_info' : IDL.Func(
        [IDL.Nat64],
        [IDL.Variant({ 'Ok' : IDL.Text, 'Err' : IDL.Text })],
        [],
      ),
    'get_identity' : IDL.Func(
        [IDL.Nat64],
        [IDL.Variant({ 'Ok' : UrbitIdentity, 'Err' : IDL.Text })],
        [],
      ),
    'get_logs' : IDL.Func([], [IDL.Vec(IDL.Text)], []),
    'get_metadata' : IDL.Func(
        [IDL.Nat64],
        [IDL.Variant({ 'Ok' : IdentityMetadata, 'Err' : IDL.Text })],
        [],
      ),
    'get_stats' : IDL.Func(
        [],
        [
          IDL.Record({
            'active_identities' : IDL.Nat64,
            'total_identities' : IDL.Nat64,
          }),
        ],
        [],
      ),
    'update_identity' : IDL.Func(
        [IDL.Nat64, UrbitIdentity],
        [IDL.Variant({ 'Ok' : IDL.Null, 'Err' : IDL.Text })],
        [],
      ),
    'update_metadata' : IDL.Func(
        [IDL.Nat64, IdentityMetadata],
        [IDL.Variant({ 'Ok' : IDL.Null, 'Err' : IDL.Text })],
        [],
      ),
  });
};
export const init = ({ IDL }) => { return []; };
