# Setting Up Google Drive MCP with Cursor IDE

**Last Updated**: 2025-10-22  
**Neovedic Timestamp**: `12025-10-22--1522--CDT--moon-vishakha--09thhouse15--kae3g`

This guide covers two methods for integrating Google Drive with Cursor using the Model Context Protocol (MCP).

---

## 🎯 **WHAT IS MCP?**

**Model Context Protocol (MCP)** is an open protocol developed by Anthropic that enables AI assistants to securely connect to external data sources and tools. With Google Drive MCP, Cursor can:

- 📂 Search for files in your Google Drive
- 📄 Read document contents (Google Docs, Sheets, Slides)
- 📊 Access data in Google Sheets
- 🔍 Search across all your Drive files
- ✍️ Work with Drive content without leaving Cursor

---

## 🛠️ **METHOD 1: Direct Google Drive MCP Server (Recommended)**

### **Prerequisites**
- Node.js installed (v18+ recommended)
- Google Account with Google Drive
- Cursor IDE installed

### **Step 1: Set Up Google Cloud Project**

#### **1.1 Create Google Cloud Project**
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Click "Select a project" → "New Project"
3. Name your project (e.g., "Cursor MCP Integration")
4. Click "Create"

#### **1.2 Enable Google Drive API**
1. In your project dashboard, navigate to **"APIs & Services" > "Library"**
2. Search for **"Google Drive API"**
3. Click on it and press **"Enable"**

#### **1.3 Configure OAuth Consent Screen**
1. Go to **"APIs & Services" > "OAuth consent screen"**
2. Select **"External"** (unless you have a Google Workspace)
3. Fill in the required information:
   - **App name**: "Cursor MCP"
   - **User support email**: Your email
   - **Developer contact email**: Your email
4. Click **"Save and Continue"**
5. On the Scopes page, click **"Add or Remove Scopes"**
6. Add these scopes:
   - `https://www.googleapis.com/auth/drive.readonly`
   - `https://www.googleapis.com/auth/drive.file`
7. Click **"Save and Continue"**
8. Add your email as a test user
9. Click **"Save and Continue"** and then **"Back to Dashboard"**

#### **1.4 Create OAuth Client ID**
1. Navigate to **"APIs & Services" > "Credentials"**
2. Click **"Create Credentials" > "OAuth client ID"**
3. Select **"Desktop app"** as the application type
4. Name it "Cursor MCP Client"
5. Click **"Create"**
6. Download the `credentials.json` file
7. Save it to a secure location (e.g., `~/.config/cursor/gdrive-credentials.json`)

### **Step 2: Install Google Drive MCP Server**

#### **2.1 Test the MCP Server**
```bash
# Test if it works with npx (no installation needed)
npx -y @modelcontextprotocol/server-gdrive
```

During the first run, you'll be prompted to:
1. Authenticate with your Google account
2. Grant permissions to access your Google Drive
3. This will create a token file for future sessions

#### **2.2 Verify Installation**
```bash
# The server should start and wait for connections
# Press Ctrl+C to exit
```

### **Step 3: Configure Cursor**

#### **3.1 Create MCP Configuration File**

**Option A: Global Configuration (Recommended)**

Create or edit `~/.cursor/mcp.json`:

```json
{
  "mcpServers": {
    "gdrive": {
      "command": "npx",
      "args": [
        "-y",
        "@modelcontextprotocol/server-gdrive"
      ],
      "env": {
        "GDRIVE_CREDENTIALS_PATH": "/home/xy/.config/cursor/gdrive-credentials.json"
      }
    }
  }
}
```

**Option B: Project-Specific Configuration**

Create `.cursor/mcp.json` in your project directory:

```json
{
  "mcpServers": {
    "gdrive": {
      "command": "npx",
      "args": [
        "-y",
        "@modelcontextprotocol/server-gdrive"
      ],
      "env": {
        "GDRIVE_CREDENTIALS_PATH": "/home/xy/.config/cursor/gdrive-credentials.json"
      }
    }
  }
}
```

**Important**: Replace `/home/xy/.config/cursor/gdrive-credentials.json` with the actual path to your downloaded `credentials.json` file.

#### **3.2 Create Configuration Directory**
```bash
# Create the Cursor config directory if it doesn't exist
mkdir -p ~/.cursor
mkdir -p ~/.config/cursor

# Move your credentials file
mv ~/Downloads/credentials.json ~/.config/cursor/gdrive-credentials.json

# Secure the credentials file
chmod 600 ~/.config/cursor/gdrive-credentials.json
```

### **Step 4: Restart Cursor**

1. Close Cursor completely
2. Reopen Cursor
3. The MCP server will initialize on startup

### **Step 5: Verify the Integration**

1. In Cursor, go to **Settings** (Ctrl+,)
2. Navigate to **Features > MCP Servers**
3. You should see "gdrive" listed as an active MCP server
4. If you see a green indicator, the integration is working!

### **Step 6: Test Google Drive Access**

Try these commands in Cursor's chat:

```
"Search my Google Drive for files containing 'grain'"
"Read the contents of my document titled 'UIUC Club Plan'"
"List all Google Sheets in my Drive"
"Find all PDFs in my Drive from the last month"
```

---

## 🌐 **METHOD 2: Using Ragie (Alternative)**

Ragie is a platform that simplifies connecting AI models to knowledge sources like Google Drive.

### **Step 1: Create Ragie Account**
1. Go to [ragie.ai](https://www.ragie.ai/)
2. Sign up for a free account
3. Verify your email

### **Step 2: Connect Google Drive to Ragie**
1. In Ragie dashboard, click **"Add Data Source"**
2. Select **"Google Drive"**
3. Authenticate with your Google account
4. Grant Ragie permission to access your Drive

### **Step 3: Get Ragie API Key**
1. In Ragie dashboard, go to **"Settings" > "API Keys"**
2. Click **"Create New API Key"**
3. Copy the API key (you won't be able to see it again)

### **Step 4: Configure Cursor with Ragie**

Create `~/.cursor/mcp.json`:

```json
{
  "mcpServers": {
    "ragie-gdrive": {
      "command": "npx",
      "args": [
        "-y",
        "@ragie/mcp-server"
      ],
      "env": {
        "RAGIE_API_KEY": "your-ragie-api-key-here"
      }
    }
  }
}
```

Replace `your-ragie-api-key-here` with your actual Ragie API key.

### **Step 5: Restart Cursor**

Follow the same verification steps as Method 1.

---

## 🔒 **SECURITY BEST PRACTICES**

### **1. Protect Your Credentials**
```bash
# Ensure credentials are readable only by you
chmod 600 ~/.config/cursor/gdrive-credentials.json
chmod 600 ~/.cursor/mcp.json
```

### **2. Use Environment Variables**
For added security, store sensitive data in environment variables:

```bash
# Add to ~/.bashrc or ~/.zshrc
export GDRIVE_CREDENTIALS_PATH="$HOME/.config/cursor/gdrive-credentials.json"
export RAGIE_API_KEY="your-api-key-here"
```

Then update `mcp.json`:

```json
{
  "mcpServers": {
    "gdrive": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-gdrive"],
      "env": {
        "GDRIVE_CREDENTIALS_PATH": "${GDRIVE_CREDENTIALS_PATH}"
      }
    }
  }
}
```

### **3. Gitignore Configuration**
Add to your `.gitignore`:

```gitignore
# Cursor MCP Configuration
.cursor/mcp.json
*credentials.json
*token.json
```

### **4. Use Read-Only Scopes**
When possible, use read-only OAuth scopes:
- `https://www.googleapis.com/auth/drive.readonly`

---

## 🐛 **TROUBLESHOOTING**

### **Issue 1: "Command not found: npx"**
**Solution**: Install Node.js
```bash
# Ubuntu/Debian
sudo apt install nodejs npm

# Or use nvm (recommended)
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash
nvm install node
```

### **Issue 2: "Authentication Failed"**
**Solution**: Re-authenticate
```bash
# Remove token file
rm ~/.cache/@modelcontextprotocol/server-gdrive/token.json

# Run the MCP server manually to re-authenticate
npx -y @modelcontextprotocol/server-gdrive
```

### **Issue 3: "MCP Server Not Showing in Cursor"**
**Solution**: Check configuration
```bash
# Verify the config file exists
cat ~/.cursor/mcp.json

# Check file permissions
ls -la ~/.cursor/mcp.json

# Restart Cursor completely
pkill -f cursor
cursor
```

### **Issue 4: "Permission Denied"**
**Solution**: Update OAuth consent screen
1. Go to Google Cloud Console
2. Navigate to "APIs & Services" > "OAuth consent screen"
3. Add your email as a test user
4. Ensure the app is in "Testing" mode

### **Issue 5: "Rate Limit Exceeded"**
**Solution**: Enable quota increase
1. Go to Google Cloud Console
2. Navigate to "APIs & Services" > "Quotas"
3. Request quota increase if needed

---

## 📊 **USAGE EXAMPLES**

### **Search Files**
```
Prompt: "Find all files in my Google Drive that mention 'Grain Network'"
```

### **Read Documents**
```
Prompt: "Read the contents of my Google Doc titled 'PSEUDO.md' and summarize it"
```

### **Access Spreadsheets**
```
Prompt: "Get data from my Google Sheet called 'Budget' and calculate the total expenses"
```

### **List Recent Files**
```
Prompt: "List all files I've modified in the last 7 days"
```

### **Search by Type**
```
Prompt: "Find all PDF files in my Drive related to 'fundraising'"
```

---

## 🌾 **GRAIN NETWORK INTEGRATION**

### **Use Cases for Grain Development**

1. **Documentation Management**
   - Access Grain Network docs stored in Google Drive
   - Search across all project documentation
   - Read design documents and technical specs

2. **Collaboration**
   - Share high school course materials via Google Docs
   - Collaborate on UIUC club planning documents
   - Manage Grain PBC business documents

3. **Research & Reference**
   - Access saved research papers and articles
   - Reference design inspiration and examples
   - Search through technical documentation

4. **Project Management**
   - Access Google Sheets with project timelines
   - Review fundraising spreadsheets
   - Track Grainphone component pricing

### **Recommended Folder Structure**

```
Google Drive/
├── Grain Network/
│   ├── Documentation/
│   │   ├── Architecture/
│   │   ├── Design/
│   │   └── API/
│   ├── Course Materials/
│   │   ├── Lessons/
│   │   ├── Assignments/
│   │   └── Resources/
│   ├── UIUC Club/
│   │   ├── Meeting Notes/
│   │   ├── Discussion Topics/
│   │   └── Flyers/
│   ├── Grain PBC/
│   │   ├── Business/
│   │   ├── Legal/
│   │   └── Fundraising/
│   └── Hardware/
│       ├── Grainphone/
│       ├── Grainwriter/
│       └── Graincamera/
```

---

## 🔄 **SYNCING WITH GRAINCLAY**

You can integrate Google Drive with Grainclay for immutable documentation:

```clojure
;; Example: Sync Google Drive doc to Grainclay path
(defn sync-gdrive-to-grainclay
  [gdrive-file-id grainclay-path]
  (let [content (gdrive/read-file gdrive-file-id)
        timestamp (neovedic/now)
        clay-path (str grainclay-path "--" timestamp)]
    (grainclay/write clay-path content)))
```

---

## 📚 **ADDITIONAL RESOURCES**

- [MCP Official Documentation](https://modelcontextprotocol.io/)
- [Google Drive API Documentation](https://developers.google.com/drive)
- [Cursor MCP Directory](https://cursor.directory/mcp/google-drive)
- [Ragie Documentation](https://docs.ragie.ai/)
- [Anthropic MCP Servers GitHub](https://github.com/modelcontextprotocol/servers)

---

## ✅ **QUICK START CHECKLIST**

- [ ] Install Node.js (v18+)
- [ ] Create Google Cloud Project
- [ ] Enable Google Drive API
- [ ] Configure OAuth consent screen
- [ ] Create OAuth client ID
- [ ] Download credentials.json
- [ ] Move credentials to ~/.config/cursor/
- [ ] Test MCP server with npx
- [ ] Create ~/.cursor/mcp.json configuration
- [ ] Restart Cursor
- [ ] Verify MCP server in Cursor settings
- [ ] Test with a simple query
- [ ] Set up security (chmod 600)
- [ ] Add credentials to .gitignore

---

## 🎓 **FOR UIUC CLUB & HIGH SCHOOL COURSE**

This integration can be used to:

1. **Collaborative Documentation**: Share course materials via Google Drive
2. **Real-Time Access**: Cursor can read and reference shared documents during development
3. **Version Control**: Combine Google Drive versions with Grainclay immutable paths
4. **Student Projects**: Students can store their projects in Drive and access them in Cursor
5. **Club Resources**: UIUC club can maintain meeting notes and resources in Drive

---

**Ready to integrate Google Drive with Cursor?** Follow the steps above and you'll have seamless access to your Drive files within your development environment! 🚀🌾

---

*This guide is part of the Grain Network documentation.*  
*License: CC BY-SA 4.0*  
*Created: 12025-10-22--1522--CDT--moon-vishakha--09thhouse15--kae3g*


