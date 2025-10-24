# Secrets Setup Guide

This project uses secrets for GitHub authentication,
deployment, and other sensitive operations.

## Quick Setup

1. **Copy the template:**
   ```bash
   cp .secrets.template.edn .secrets.edn
   # or
   cp .env.template .env
   ```

2. **Edit with your values:**
   ```bash
   # Use your preferred editor
   nano .secrets.edn
   # or
   nano .env
   ```

3. **Verify gitignore:**
   ```bash
   # These should NOT appear:
   git status | grep secrets
   git status | grep .env
   ```

## Secret Files (Gitignored)

- `.secrets.edn` - EDN format for Clojure/Babashka
- `.env` - Standard environment variables
- `.secrets.local.edn` - Local overrides
- `secrets/` - Directory for credential files

## GitHub Token Setup

1. Go to: https://github.com/settings/tokens
2. Generate new token (classic)
3. Select scopes: `repo`, `workflow`, `admin:org`
4. Copy token to `.secrets.edn` or `.env`

## GPG Setup

```bash
# List your GPG keys
gpg --list-secret-keys --keyid-format=long

# Copy the key ID and add to secrets
```

## Using Secrets in Tasks

**From EDN:**
```clojure
(require '[clojure.edn :as edn])
(def secrets (edn/read-string (slurp ".secrets.edn")))
(def github-token (get-in secrets [:github :token]))
```

**From ENV:**
```bash
source .env
echo $GITHUB_TOKEN
```

## Security Best Practices

1. ✅ Never commit `.secrets.edn` or `.env`
2. ✅ Always use `.template` files for examples
3. ✅ Rotate tokens regularly
4. ✅ Use minimal required permissions
5. ✅ Add secrets to `.gitignore`

## Deployment Secrets

For CI/CD, add secrets to:
- **GitHub Actions:** Repository Settings → Secrets
- **Netlify:** Site Settings → Environment Variables
- **Vercel:** Project Settings → Environment Variables

## Troubleshooting

**Secrets not loading:**
```bash
# Check file exists
ls -la .secrets.edn

# Verify format
bb -e "(prn (slurp \".secrets.edn\"))"
```

**Token invalid:**
- Regenerate token on GitHub
- Update in `.secrets.edn`
- Verify scopes are correct

---

**Never commit actual secrets!**
Check with: `git diff --cached | grep -i token`

