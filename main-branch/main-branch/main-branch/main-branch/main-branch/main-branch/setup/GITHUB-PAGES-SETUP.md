# GitHub Pages Setup Instructions

This repository is configured to automatically build and deploy to GitHub Pages whenever you push to the `rhizome-valley` branch.

## Automatic Setup (Already Done)

The GitHub Action workflow is already in place at `.github/workflows/deploy.yml`. It will:

1. Build content using Babashka (`bb writings:build`)
2. Build the SvelteKit static site (`npm run build`)
3. Deploy to GitHub Pages automatically

## GitHub Repository Settings (You Need to Configure)

### Step 1: Enable GitHub Pages

1. Go to your repository on GitHub: https://codeberg.org/kae3g/12025-10
2. Click **Settings** (top right)
3. Click **Pages** (left sidebar)
4. Under **Source**, select:
   - Source: **GitHub Actions** (not "Deploy from a branch")
5. Click **Save**

### Step 2: Verify Workflow Permissions

1. Still in **Settings**, click **Actions** → **General** (left sidebar)
2. Scroll down to **Workflow permissions**
3. Select **Read and write permissions**
4. Check **Allow GitHub Actions to create and approve pull requests**
5. Click **Save**

### Step 3: Trigger First Deployment

Once you've configured the settings above, the site will deploy automatically when you push to `rhizome-valley`.

You can also manually trigger a deployment:

1. Go to **Actions** tab in your repository
2. Click **Deploy to GitHub Pages** workflow
3. Click **Run workflow** → **Run workflow**

### Step 4: Access Your Site

After the deployment completes (usually 2-3 minutes), your site will be available at:

**https://kae3g.codeberg.page/12025-10/**

## Monitoring Deployments

- Go to the **Actions** tab to see deployment status
- Green checkmark = successful deployment
- Red X = failed deployment (click to see logs)

## Custom Domain (Optional)

If you want to use a custom domain instead of `kae3g.codeberg.page/12025-10`:

1. In **Settings** → **Pages**
2. Under **Custom domain**, enter your domain (e.g., `kae3g.com`)
3. Add DNS records at your domain registrar:
   - Type: `CNAME`
   - Name: `www` (or `@` for apex domain)
   - Value: `kae3g.github.io`
4. Wait for DNS propagation (can take up to 48 hours)
5. Check **Enforce HTTPS** once DNS is configured

## Troubleshooting

**Build fails:**
- Check the Actions tab for error logs
- Ensure `bb writings:build` works locally
- Ensure `npm run build` works locally in `web-app/`

**404 errors on deployed site:**
- Verify the build output is in `web-app/build/`
- Check that `svelte.config.js` uses `adapter-static`
- Ensure all paths in the app are relative, not absolute

**Workflow doesn't trigger:**
- Verify you pushed to `rhizome-valley` branch
- Check that workflow permissions are set correctly
- Manually trigger from Actions tab

## Local Testing Before Deployment

Always test locally before pushing:

```bash
# Build content
bb writings:build

# Build and preview site
cd web-app
npm run build
npm run preview
# Visit http://localhost:4173
```

If it works locally, it should work on GitHub Pages.

---

**Note:** The first deployment after enabling GitHub Pages may take a few minutes longer than subsequent ones while GitHub provisions the environment.

