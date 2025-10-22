# 🌾 Grain Network Admin Policy
## Username & Display Name Protection

**Policy Version:** 1.0  
**Effective Date:** January 22, 2025  
**Admin Authority:** kae3g (Graingalaxy)  
**Scope:** All Grain Network platforms and services

---

## 🎯 POLICY OVERVIEW

The Grain Network implements strict naming conventions to protect the integrity of the "Grain" brand and prevent confusion among users. This policy ensures that only authorized administrators can create accounts with "grain*" prefixes.

### Protected Naming Patterns

**Restricted Patterns (Admin-Only):**
- `grain*` - Any username or display name starting with "grain"
- `grainwriter*` - Grainwriter product line
- `graincamera*` - Graincamera product line  
- `grainsource*` - Grainsource version control
- `grainspace*` - Grainspace platform
- `grainstore*` - Grainstore dependency management
- `graingives*` - Graingives nonprofit
- `grainpbc*` - Grain PBC corporation
- `graingalaxy*` - Grain Galaxy (admin tier)

**Examples of Protected Names:**
- ❌ `grainwriter` (reserved for official product)
- ❌ `graincamera` (reserved for official product)
- ❌ `grainsource` (reserved for official product)
- ❌ `grainspace` (reserved for official platform)
- ❌ `grainstore` (reserved for official dependency management)
- ❌ `graingives` (reserved for official nonprofit)
- ❌ `grainpbc` (reserved for official corporation)
- ❌ `grainuser123` (starts with "grain")
- ❌ `grain-dev` (starts with "grain")

**Examples of Allowed Names:**
- ✅ `kae3g` (admin account)
- ✅ `user123` (standard user)
- ✅ `developer` (standard user)
- ✅ `writer` (standard user)
- ✅ `camera` (standard user)
- ✅ `source` (standard user)
- ✅ `space` (standard user)
- ✅ `store` (standard user)
- ✅ `gives` (standard user)

---

## 👑 ADMINISTRATIVE STRUCTURE

### Graingalaxy Tier (Supreme Admin)

**Primary Admin:** `kae3g`
- **Title:** Graingalaxy (Galaxy-level administrator)
- **Authority:** Full control over all Grain Network systems
- **Responsibilities:**
  - Create and manage all official "grain*" accounts
  - Approve or deny requests for protected names
  - Maintain naming policy enforcement
  - Oversee platform security and integrity
  - Manage official product accounts

**Graingalaxy Privileges:**
- Create any username/display name (including "grain*" patterns)
- Transfer ownership of protected accounts
- Suspend or modify any user account
- Access all platform administrative functions
- Modify system policies and configurations
- Delegate administrative privileges to other users

### Future Admin Tiers (Planned)

**Graingalaxy** (1 position) - kae3g
- Supreme authority over entire Grain Network
- Can create any "grain*" account
- Can delegate privileges to lower tiers

**Grainstar** (5-10 positions) - Future expansion
- High-level administrators
- Can create most "grain*" accounts (with restrictions)
- Cannot create core product names (grainwriter, graincamera, etc.)

**Grainplanet** (20-50 positions) - Future expansion
- Mid-level administrators
- Can create limited "grain*" accounts
- Cannot create product or platform names

**Grainmoon** (100+ positions) - Future expansion
- Community moderators
- Cannot create "grain*" accounts
- Can moderate content and users

---

## 🔒 ENFORCEMENT MECHANISMS

### Technical Implementation

**1. Username Validation (All Platforms)**
```clojure
(defn validate-username [username]
  "Validate username against Grain Network policy"
  (let [grain-pattern #"^grain.*"
        is-grain-name? (re-matches grain-pattern username)
        is-admin? (is-grain-galaxy? current-user)]
    (if (and is-grain-name? (not is-admin?))
      {:valid false
       :error "Username starting with 'grain' requires admin approval"}
      {:valid true})))
```

**2. Display Name Validation**
```clojure
(defn validate-display-name [display-name]
  "Validate display name against Grain Network policy"
  (let [grain-pattern #"^grain.*"
        is-grain-name? (re-matches grain-pattern display-name)
        is-admin? (is-grain-galaxy? current-user)]
    (if (and is-grain-name? (not is-admin?))
      {:valid false
       :error "Display name starting with 'grain' requires admin approval"}
      {:valid true})))
```

**3. ICP Canister Integration**
```rust
// In grainsource-identity canister
pub fn create_identity(name: String, identity_type: IdentityType) -> Result<(), String> {
    let is_grain_name = name.starts_with("grain");
    let is_admin = check_admin_status(&caller);
    
    if is_grain_name && !is_admin {
        return Err("Grain* names require admin approval".to_string());
    }
    
    // Proceed with identity creation
    create_identity_record(name, identity_type)
}
```

**4. Urbit Integration**
```hoon
++  validate-name
  |=  name=@t
  ^-  ?
  ?.  (has-prefix name "grain")
    %.y
  (is-grain-galaxy caller)
```

### Platform-Specific Enforcement

**GitHub Organization (`grainpbc`):**
- Only kae3g can create repositories with "grain*" names
- All other members must use non-grain prefixes
- Automatic validation on repository creation

**ICP Canisters:**
- Identity creation blocked for non-admin "grain*" names
- Admin status verified via Urbit ID or principal
- Automatic enforcement at canister level

**Urbit Ships:**
- Username validation in `%grainsource` agent
- Admin status checked against galaxy/star registry
- Peer-to-peer validation across ships

**Nostr Relays:**
- Relay-level validation for "grain*" usernames
- Admin verification via Nostr public key
- Community moderation tools

**Bluesky/Threads:**
- API-level validation for display names
- Admin verification via verified accounts
- Content moderation integration

---

## 📋 OFFICIAL ACCOUNT REGISTRY

### Core Product Accounts (Reserved)

**Grainwriter:**
- `grainwriter` - Main product account
- `grainwriter-dev` - Development account
- `grainwriter-support` - Support account
- `grainwriter-news` - News and updates

**Graincamera:**
- `graincamera` - Main product account
- `graincamera-dev` - Development account
- `graincamera-support` - Support account
- `graincamera-news` - News and updates

**Grainsource:**
- `grainsource` - Main product account
- `grainsource-dev` - Development account
- `grainsource-docs` - Documentation account
- `grainsource-community` - Community account

**Grainspace:**
- `grainspace` - Main platform account
- `grainspace-dev` - Development account
- `grainspace-community` - Community account
- `grainspace-news` - Platform news

**Grainstore:**
- `grainstore` - Main dependency management
- `grainstore-dev` - Development account
- `grainstore-docs` - Documentation account

**Graingives:**
- `graingives` - Main nonprofit account
- `graingives-community` - Community account
- `graingives-news` - News and updates
- `graingives-grants` - Grant program account

**Grain PBC:**
- `grainpbc` - Main corporation account
- `grainpbc-legal` - Legal and compliance
- `grainpbc-investors` - Investor relations
- `grainpbc-careers` - Careers and hiring

### Administrative Accounts

**Graingalaxy (Supreme Admin):**
- `kae3g` - Primary admin account
- `graingalaxy` - Admin title account
- `grain-admin` - Administrative functions
- `grain-network` - Network management

### Future Reserved Accounts

**Grainstar Tier (Future):**
- `grainstar-*` - High-level admin accounts
- `grain-community-*` - Community management
- `grain-moderator-*` - Content moderation

**Grainplanet Tier (Future):**
- `grainplanet-*` - Mid-level admin accounts
- `grain-support-*` - Support accounts

**Grainmoon Tier (Future):**
- `grainmoon-*` - Community moderator accounts
- `grain-helper-*` - Helper accounts

---

## 🚨 VIOLATION HANDLING

### Automatic Enforcement

**1. Username Violations:**
- Account creation blocked immediately
- Error message: "Username starting with 'grain' requires admin approval"
- Suggestion: "Try using a different prefix or contact admin@grain.network"

**2. Display Name Violations:**
- Display name reset to username
- Warning message sent to user
- Option to request admin approval

**3. Existing Account Violations:**
- Grace period: 30 days to change name
- After grace period: Account suspended
- Appeal process available

### Appeal Process

**1. Submit Appeal:**
- Email: admin@grain.network
- Subject: "Grain* Name Appeal - [username]"
- Include: Justification for using "grain*" name
- Include: Proposed alternative names

**2. Admin Review:**
- kae3g reviews appeal within 7 days
- Decision: Approve, Deny, or Suggest Alternative
- Response sent via email

**3. Approved Appeals:**
- Account upgraded to appropriate admin tier
- "grain*" name approved and reserved
- Added to official account registry

**4. Denied Appeals:**
- Must choose non-grain name
- Can reapply after 90 days
- Alternative suggestions provided

---

## 📊 MONITORING & REPORTING

### Automated Monitoring

**1. Username Scanning:**
- Daily scan of all platforms for "grain*" violations
- Automatic flagging of suspicious accounts
- Alert sent to kae3g for review

**2. Display Name Monitoring:**
- Real-time validation on all platforms
- Automatic correction of violations
- User notification system

**3. Cross-Platform Consistency:**
- Verify same user across all platforms
- Flag inconsistencies in naming
- Maintain unified identity system

### Reporting Dashboard

**Admin Dashboard (kae3g only):**
- Real-time violation alerts
- User appeal queue
- Account approval/rejection interface
- Policy modification tools
- Audit log of all admin actions

**Public Transparency:**
- Monthly report of policy enforcement
- Statistics on violations and appeals
- List of approved "grain*" accounts
- Policy updates and changes

---

## 🔄 POLICY UPDATES

### Amendment Process

**1. Policy Changes:**
- Only kae3g can modify this policy
- Changes must be documented with rationale
- Version control maintained in Git
- Public notification of changes

**2. Emergency Updates:**
- Immediate enforcement for security issues
- Retroactive application if necessary
- Public announcement within 24 hours
- Appeal process for affected users

**3. Community Input:**
- Open discussion on policy improvements
- Community feedback considered
- Final decision remains with kae3g
- Transparent decision-making process

---

## 📞 CONTACT & SUPPORT

### Administrative Contact

**Primary Admin:** kae3g (Graingalaxy)
- **Email:** admin@grain.network
- **Urbit:** ~sampel-palnet (when available)
- **Nostr:** npub1... (when available)
- **GitHub:** @kae3g

### Support Channels

**General Support:**
- **Email:** support@grain.network
- **GitHub Issues:** github.com/grainpbc/grainkae3g/issues
- **Community Forum:** community.grain.network (when available)

**Appeal Process:**
- **Email:** admin@grain.network
- **Subject:** "Grain* Name Appeal - [username]"
- **Response Time:** 7 days maximum

---

## 📋 COMPLIANCE CHECKLIST

### For New Users
- [ ] Username does not start with "grain"
- [ ] Display name does not start with "grain"
- [ ] Read and understood naming policy
- [ ] Agreed to terms of service

### For Administrators
- [ ] Verify admin status before creating "grain*" accounts
- [ ] Document all account creations
- [ ] Monitor for policy violations
- [ ] Respond to appeals within 7 days
- [ ] Maintain official account registry

### For Platform Developers
- [ ] Implement username validation
- [ ] Implement display name validation
- [ ] Integrate with admin verification system
- [ ] Provide clear error messages
- [ ] Log all validation attempts

---

**Policy Version:** 1.0  
**Last Updated:** January 22, 2025  
**Next Review:** April 22, 2025  
**Admin:** kae3g (Graingalaxy)  
**Status:** Active

---

**Grain Network Admin Policy**  
**Protecting the integrity of the Grain ecosystem** 🌾

*"Every grain matters. Every name counts."*

