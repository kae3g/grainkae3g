# grainkae3genvvars

**kae3g's personal environment variables and secrets**

Personal API keys, tokens, and credentials with 1Password integration.

---

## Configuration

**Template**: `grainpbc/grainenvvars`  
**Personal**: This module

### Secrets Stored

- OpenAI API Key (in 1Password)
- GitHub Personal Access Token
- Codeberg Token  
- ICP Principal
- Solana Wallet Address
- Other service credentials

---

## Security

**⚠️ NEVER commit secrets to git!**

All secrets stored in 1Password vault:
- Vault: "Grain Network - kae3g"
- Items: OpenAI, GitHub, Codeberg, ICP, Solana

Load via:
```bash
source $GRAINSTORE/kae3g/grainkae3genvvars/personal/load-from-1password.sh
```

---

## File Structure

```
personal/
├── .env                          # GITIGNORED - never commit!
├── .gitignore                    # Ensures .env is ignored
└── load-from-1password.sh        # GITIGNORED - personal script
```

---

**Created**: October 23, 2025  
**Graintime**: `12025-10-23--0145--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g`

🔒 **Your secrets, your sovereignty!**

