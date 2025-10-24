# Setup Guide: CI Wait for `bb flow`

**Purpose**: Enable `bb flow` to wait for GitHub Actions and Codeberg Pages to complete deployment, then print "✨ Deployment verified!"

**Time**: 10-15 minutes

---

## Step 1: Create Your Personal Config Files

### A. Create `.config.edn` (public/personal settings)

```bash
cd ~/kae3g/12025-10
cp .config.template.edn .config.edn
```

**Edit `.config.edn`** with your actual info:
```clojure
{:site
 {:username "kae3g"
  :language "en"
  :title "Coldriver Tundra"
  :description "Experimental aspiringly helpful generative AI writings"}

 :author
 {:name "kae3g"  ; Or your preferred name
  :email "your.email@example.com"  ; Your public email (can be GitHub noreply email)
  :url nil}

 :repository
 {:platform "codeberg"
  :username "kae3g"
  :repo-name "12025-10"
  :branch "tundra"}

 :localization
 {:timezone "America/Los_Angeles"  ; Your timezone
  :locale "en-US"
  :date-format "YYYY-MM-DD"
  :planned-languages ["es" "de"]}

 :preferences
 {:editor "cursor"
  :theme "dark"
  :monospace-font "Courier New"}}
```

### B. Create `.secrets.edn` (private/sensitive data)

```bash
cp .secrets.template.edn .secrets.edn
```

**We'll fill this in during the next steps.**

---

## Step 2: Get Your GitHub Personal Access Token (PAT)

### A. Create the token on GitHub

1. Go to: https://github.com/settings/tokens?type=beta
2. Click **"Generate new token"** (Fine-grained tokens recommended)
3. **Token name**: `kae3g-ci-wait`
4. **Expiration**: 90 days (or custom)
5. **Repository access**: Select "Only select repositories" → Choose `kae3g/12025-10`
6. **Permissions**:
   - **Actions**: Read-only ✅ (this is all we need!)
7. Click **"Generate token"**
8. **Copy the token** (starts with `github_pat_...`)

### B. Add token to `.secrets.edn`

**Edit `.secrets.edn`** and add your GitHub token:

```clojure
{:git
 {:signing-key nil}

 :git-hosting
 {:codeberg
  {:token nil
   :ssh-key-path "~/.ssh/id_ed25519"}
  
  :github
  {:token "github_pat_YOUR_TOKEN_HERE"  ; ← ADD YOUR TOKEN HERE
   :ssh-key-path nil}}

 ;; ... rest of file unchanged ...
}
```

**Important**: This file is git-ignored and will never be committed! ✅

---

## Step 3: (Optional) Get Codeberg/Woodpecker Token

**Note**: This is trickier because Codeberg's Woodpecker CI might not have easily accessible API tokens for regular users. For now, we'll implement **GitHub-only** waiting and keep Codeberg as "best effort" (we know it pushed successfully, Pages deployment usually works).

**If you want to try**:
1. Go to your Codeberg Woodpecker CI: https://ci.codeberg.org/
2. Look for user settings → API tokens
3. Generate a token
4. Add to `.secrets.edn` under `:codeberg :token`

**We'll implement GitHub waiting first, then add Codeberg later if the API is accessible.**

---

## Step 4: Install the CI Wait Script

I'll create a new `bb` task called `flow:wait` that:
1. Pushes to both remotes (like `flow`)
2. Waits for GitHub Actions to complete
3. Checks Codeberg Pages push (best effort)
4. Prints final status

**Script location**: `scripts/ci/wait-for-deploy.clj`

---

## Step 5: Test It

Once the script is installed, run:

```bash
bb flow:wait "test: verify CI wait works"
```

You should see:
```
🌊 Starting dual-deploy precision flow...
1️⃣ Building...
2️⃣ Staging...
3️⃣ Committing...
4️⃣ Pushing to Codeberg...
5️⃣ Pushing to GitHub...
6️⃣ Deploying to Codeberg Pages...
7️⃣ Waiting for GitHub Actions...
   ◴ GitHub Actions: in_progress (0m 15s)
   ◴ GitHub Actions: in_progress (0m 30s)
   ✓ GitHub Actions: completed successfully (1m 42s)
8️⃣ Verifying deployments...
   ✓ GitHub Pages: https://kae3g.github.io/12025-10/
   ✓ Codeberg Pages: https://kae3g.codeberg.page/12025-10/

✨ Dual-deploy complete! Both sites verified live.
```

---

## Summary

**What you need to do**:
1. ✅ Create `.config.edn` with your personal info
2. ✅ Create `.secrets.edn` 
3. ✅ Get GitHub PAT (fine-grained, Actions: read-only)
4. ✅ Add token to `.secrets.edn`
5. ✅ Let me create the `bb flow:wait` script
6. ✅ Test it!

**Let me know when you're ready** and I'll create the implementation!

---

**Security Notes**:
- ✅ `.secrets.edn` is git-ignored (never committed)
- ✅ Token only has read-only Actions permission (minimal risk)
- ✅ Token can be revoked anytime on GitHub
- ✅ Use fine-grained tokens (not classic tokens)
- ✅ Set expiration to 90 days, renew periodically

