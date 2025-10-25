# Graindaemon - Universal S6/SixOS Daemon Framework

**Created**: 12025-10-22--CDT  
**Purpose**: Universal Clojure daemon framework with S6 supervision  
**Platform**: Ubuntu 24.04 LTS (Framework 16)  
**License**: MIT

---

## Overview

Graindaemon is a comprehensive framework for creating, managing, and supervising Clojure-based system daemons using S6 supervision. It follows the Grain Network's template/personal configuration split pattern.

**Features**:
- ✅ S6-supervised daemons with auto-restart
- ✅ Template/personal configuration split (fork-friendly)
- ✅ Auto-start on Ubuntu boot via systemd
- ✅ Multiple daemon support (Night Light, WiFi, custom services)
- ✅ Centralized logging and monitoring
- ✅ Health checks and status reporting
- ✅ Hot-reload configuration without restart

---

## Architecture

### Directory Structure

```
grainstore/graindaemon/
├── README.md                    # This file
├── deps.edn                     # Clojure dependencies
├── bb.edn                       # Babashka tasks
├── src/
│   └── graindaemon/
│       ├── core.clj             # Core daemon framework
│       ├── s6.clj               # S6 integration
│       ├── config.clj           # Configuration management
│       ├── supervisor.clj       # Daemon supervision logic
│       ├── health.clj           # Health checks
│       └── services/
│           ├── nightlight.clj   # GNOME Night Light daemon
│           ├── wifi.clj         # GrainWiFi daemon
│           └── template.clj     # Service template
├── config/
│   ├── daemon.template.edn      # Template configuration (committed)
│   └── daemon.edn               # Personal config (gitignored)
├── s6-services/
│   ├── graindaemon-supervisor/  # Master supervisor service
│   ├── grain-nightlight/        # Night Light service
│   └── grain-wifi/              # WiFi manager service
├── scripts/
│   ├── install.bb               # Install Graindaemon system
│   ├── start-all.bb             # Start all daemons
│   ├── stop-all.bb              # Stop all daemons
│   ├── status.bb                # Show status of all daemons
│   └── add-service.bb           # Add new service
└── systemd/
    └── graindaemon.service      # Systemd user service
```

---

## Quick Start

### 1. Install Graindaemon

```bash
cd ~/kae3g/grainkae3g/grainstore/graindaemon

# Copy template configuration
cp config/daemon.template.edn config/daemon.edn

# Edit personal configuration
vim config/daemon.edn

# Install system-wide
bb install

# This will:
# - Create ~/.s6/ directory structure
# - Install all S6 services
# - Create systemd user service
# - Enable auto-start on boot
```

### 2. Start Daemons

```bash
# Start all daemons
bb daemon:start-all

# Or start individually
bb daemon:start nightlight
bb daemon:start wifi

# Check status
bb daemon:status
```

### 3. Enable Auto-Start on Boot

```bash
# Enable systemd user service
systemctl --user enable graindaemon.service
systemctl --user start graindaemon.service

# Verify
systemctl --user status graindaemon.service
```

---

## Configuration

### Template Configuration (daemon.template.edn)

**Purpose**: Public, fork-friendly defaults (committed to git)

```clojure
{:graindaemon
 {:version "0.1.0"
  :s6-root "~/.s6"                    ;; S6 service directory
  :log-dir "~/.local/share/graindaemon/logs"
  :pid-dir "~/.local/share/graindaemon/pids"
  
  :supervisor
  {:enabled true
   :scan-interval 5                   ;; Scan for new services every 5s
   :auto-restart true
   :max-restarts 5                    ;; Max restarts per hour
   :health-check-interval 30}         ;; Health check every 30s
  
  :services
  {:nightlight
   {:enabled true
    :type :gnome-nightlight
    :config
    {:temperature 2000                ;; REPLACE: Your preferred temp
     :schedule-automatic false        ;; REPLACE: true for auto schedule
     :monitor-interval 30}}
   
   :wifi
   {:enabled false                    ;; REPLACE: true to enable
    :type :grain-wifi
    :config
    {:starlink-name "Starlink"        ;; REPLACE: Your Starlink SSID
     :cellular-name "Cellular-Tethering"  ;; REPLACE: Your hotspot name
     :check-interval 30}}
   
   ;; Add more services here
   }}}
```

### Personal Configuration (daemon.edn)

**Purpose**: Your specific settings (gitignored, never committed)

```clojure
{:graindaemon
 {:services
  {:nightlight
   {:enabled true
    :config
    {:temperature 2000
     :schedule-automatic false}}
   
   :wifi
   {:enabled true
    :config
    {:starlink-name "My-Starlink-Network"
     :cellular-name "iPhone-Hotspot"
     :check-interval 30}}}}}
```

**Graindaemon merges**: template + personal (personal overrides template)

---

## Services

### Built-in Services

#### 1. Night Light Service

**Purpose**: Auto-enable GNOME Night Light with warm temperature

**Configuration**:
```clojure
{:nightlight
 {:enabled true
  :config
  {:temperature 2000              ;; 1000-4000 (lower = warmer)
   :schedule-automatic false      ;; false = always on
   :monitor-interval 30}}}        ;; Check every 30s
```

**S6 Service**: `~/.s6/grain-nightlight/`

**Commands**:
```bash
bb daemon:start nightlight
bb daemon:stop nightlight
bb daemon:restart nightlight
bb daemon:status nightlight
```

#### 2. WiFi Manager Service

**Purpose**: Automatic Starlink ↔ Cellular switching

**Configuration**:
```clojure
{:wifi
 {:enabled true
  :config
  {:starlink-name "Starlink"
   :cellular-name "Cellular-Tethering"
   :check-interval 30
   :auto-switch true}}}
```

**S6 Service**: `~/.s6/grain-wifi/`

**Commands**:
```bash
bb daemon:start wifi
bb daemon:stop wifi
bb daemon:restart wifi
bb daemon:status wifi
```

---

## Creating Custom Services

### 1. Create Service Namespace

Create `src/graindaemon/services/myservice.clj`:

```clojure
(ns graindaemon.services.myservice
  (:require [graindaemon.core :as core]
            [clojure.tools.logging :as log]))

(defn start-service
  "Start your service with config"
  [config]
  (log/info "Starting MyService with config:" config)
  ;; Your service initialization code
  )

(defn stop-service
  "Stop your service"
  []
  (log/info "Stopping MyService")
  ;; Your service cleanup code
  )

(defn health-check
  "Check if service is healthy"
  []
  (try
    ;; Your health check logic
    {:healthy true :message "Service is running"}
    (catch Exception e
      {:healthy false :message (.getMessage e)})))

(defn service-loop
  "Main service loop"
  [config]
  (loop []
    (try
      ;; Your service logic
      (Thread/sleep (:interval config))
      (catch Exception e
        (log/error e "Error in service loop")))
    (recur)))

;; Service definition
(def service
  {:type :myservice
   :start start-service
   :stop stop-service
   :health-check health-check
   :loop service-loop})
```

### 2. Register Service

Add to `config/daemon.template.edn`:

```clojure
{:graindaemon
 {:services
  {:myservice
   {:enabled false              ;; Default: disabled
    :type :myservice
    :config
    {:interval 60              ;; Service-specific config
     :option "default"}}}}}
```

### 3. Create S6 Service

```bash
bb daemon:add-service myservice
```

This creates `~/.s6/grain-myservice/` with proper run scripts.

### 4. Enable and Start

Edit `config/daemon.edn`:
```clojure
{:graindaemon
 {:services
  {:myservice
   {:enabled true
    :config
    {:interval 30
     :option "my-value"}}}}}
```

Start:
```bash
bb daemon:start myservice
```

---

## S6 Integration

### S6 Service Structure

Each service gets an S6 service directory:

```
~/.s6/grain-<service>/
├── run         # Main run script
├── finish      # Cleanup script (optional)
├── type        # Service type (longrun)
├── log/
│   └── run     # Logging run script
└── notification-fd  # For readiness notification
```

### Run Script Template

```bash
#!/usr/bin/env bash
# ~/.s6/grain-<service>/run

exec 2>&1  # Redirect stderr to stdout for logging

cd ~/kae3g/grainkae3g/grainstore/graindaemon

exec bb daemon:run <service>
```

### Finish Script Template

```bash
#!/usr/bin/env bash
# ~/.s6/grain-<service>/finish

exec 2>&1

echo "Service exited with code: $1"
echo "Signal (if killed): $2"

# Cleanup code here
```

---

## Systemd Integration

### User Service

**File**: `~/.config/systemd/user/graindaemon.service`

```ini
[Unit]
Description=Graindaemon S6 Supervisor
Documentation=https://grainpbc.github.io/graindaemon
After=graphical-session.target

[Service]
Type=simple
ExecStart=/usr/local/bin/bb %h/kae3g/grainkae3g/grainstore/graindaemon/scripts/start-supervisor.bb
ExecStop=/usr/local/bin/bb %h/kae3g/grainkae3g/grainstore/graindaemon/scripts/stop-supervisor.bb
Restart=always
RestartSec=10
Environment="GRAINDAEMON_HOME=%h/kae3g/grainkae3g/grainstore/graindaemon"

[Install]
WantedBy=default.target
```

### Installation

```bash
# Copy service file
bb daemon:install-systemd

# Reload systemd
systemctl --user daemon-reload

# Enable auto-start
systemctl --user enable graindaemon.service

# Start now
systemctl --user start graindaemon.service

# Check status
systemctl --user status graindaemon.service
```

### Logs

```bash
# View systemd logs
journalctl --user -u graindaemon.service -f

# View service logs
tail -f ~/.local/share/graindaemon/logs/nightlight.log
tail -f ~/.local/share/graindaemon/logs/wifi.log
```

---

## Status and Monitoring

### Check All Services

```bash
bb daemon:status

# Example output:
# 🌾 Graindaemon Status
# ═══════════════════════════════════════════════════
#
# Supervisor: ✅ Running (PID: 12345)
# Uptime: 2 hours 15 minutes
# Services managed: 2
#
# Services:
#   ✅ nightlight  Running  PID: 12346  Uptime: 2h 15m  Restarts: 0
#   ✅ wifi        Running  PID: 12347  Uptime: 2h 15m  Restarts: 0
#
# Health Checks (last 30s):
#   ✅ nightlight: Healthy - Night Light enabled at 2000K
#   ✅ wifi:       Healthy - Connected to Starlink (Quality: 85%)
#
# Recent Activity:
#   [18:30:45] nightlight: Temperature check passed
#   [18:30:40] wifi: Connection quality check (85%)
#   [18:30:35] supervisor: Health check completed
```

### Individual Service Status

```bash
bb daemon:status nightlight

# Output:
# 🌙 Night Light Service Status
# ═══════════════════════════════════════════════════
# Status: ✅ Running
# PID: 12346
# Uptime: 2 hours 15 minutes
# Restarts: 0
# Last health check: 5 seconds ago
#
# Configuration:
#   Temperature: 2000K
#   Schedule: Manual (always on)
#   Monitor interval: 30s
#
# GNOME Night Light:
#   Enabled: ✅ Yes
#   Current temperature: 2000K
#   Last check: 2025-10-22 18:30:45 CDT
```

---

## Template/Personal Split Pattern

### How It Works

1. **Template** (`daemon.template.edn`): Committed to git, contains:
   - Sensible defaults
   - Placeholder values (e.g., `"REPLACE: Your SSID"`)
   - Documentation comments
   - All available options

2. **Personal** (`daemon.edn`): Gitignored, contains:
   - Your specific values
   - Enabled/disabled services
   - Personal preferences

3. **Merging**: Graindaemon deep-merges configs:
   ```clojure
   (merge-with merge template-config personal-config)
   ```

### Fork-Friendly Workflow

**For contributors**:
1. Clone repo
2. Copy `daemon.template.edn` to `daemon.edn`
3. Edit `daemon.edn` with your settings
4. Never commit `daemon.edn`
5. Only commit changes to `daemon.template.edn`

**For forkers**:
1. Fork repo
2. Update `daemon.template.edn` with your preferred defaults
3. Your users copy template to personal config
4. Everyone gets your defaults, customizes as needed

---

## Installation on Framework 16

### Complete Setup

```bash
# 1. Navigate to graindaemon
cd ~/kae3g/grainkae3g/grainstore/graindaemon

# 2. Install dependencies
bb deps:install

# 3. Create personal config
cp config/daemon.template.edn config/daemon.edn

# 4. Edit configuration
vim config/daemon.edn
# - Set nightlight temperature
# - Configure WiFi connection names
# - Enable/disable services

# 5. Install S6 services
bb daemon:install-s6

# 6. Install systemd service
bb daemon:install-systemd

# 7. Enable auto-start
systemctl --user enable graindaemon.service

# 8. Start now
systemctl --user start graindaemon.service

# 9. Verify everything is running
bb daemon:status

# 10. Check systemd logs
journalctl --user -u graindaemon.service -f
```

### Verification Checklist

- [ ] S6 services created in `~/.s6/`
- [ ] Systemd service created in `~/.config/systemd/user/`
- [ ] Systemd service enabled for auto-start
- [ ] All enabled services are running
- [ ] Health checks passing
- [ ] Logs being written to `~/.local/share/graindaemon/logs/`
- [ ] GNOME Night Light enabled at 2000K
- [ ] WiFi manager monitoring connections (if enabled)

---

## Troubleshooting

### Systemd service not starting

```bash
# Check service status
systemctl --user status graindaemon.service

# View full logs
journalctl --user -u graindaemon.service --no-pager

# Check if bb is in PATH
which bb

# Manually test start script
bb ~/kae3g/grainkae3g/grainstore/graindaemon/scripts/start-supervisor.bb
```

### S6 services not running

```bash
# Check S6 directory exists
ls -la ~/.s6/

# Check individual service
s6-svstat ~/.s6/grain-nightlight

# Try starting manually
s6-svc -u ~/.s6/grain-nightlight

# Check service logs
tail -f ~/.local/share/graindaemon/logs/nightlight.log
```

### Configuration not loading

```bash
# Verify personal config exists
cat ~/kae3g/grainkae3g/grainstore/graindaemon/config/daemon.edn

# Test config loading
bb daemon:test-config

# Check for syntax errors
bb -e "(clojure.edn/read-string (slurp \"config/daemon.edn\"))"
```

### Night Light not enabling

```bash
# Check GNOME settings manually
gsettings get org.gnome.settings-daemon.plugins.color night-light-enabled
gsettings get org.gnome.settings-daemon.plugins.color night-light-temperature

# Manually enable
gsettings set org.gnome.settings-daemon.plugins.color night-light-enabled true
gsettings set org.gnome.settings-daemon.plugins.color night-light-temperature 2000

# Restart service
bb daemon:restart nightlight
```

---

## Advanced Usage

### Hot-Reload Configuration

```bash
# Edit config
vim config/daemon.edn

# Reload without restarting services
bb daemon:reload-config
```

### Add Service at Runtime

```bash
# Add new service to config
vim config/daemon.edn

# Create S6 service
bb daemon:add-service myservice

# Start service
bb daemon:start myservice
```

### Custom Health Checks

```bash
# Run health checks manually
bb daemon:health-check-all

# Run health check for specific service
bb daemon:health-check nightlight
```

### Export Service Definitions

```bash
# Export for backup or sharing
bb daemon:export-config > my-daemon-config.edn

# Import config
bb daemon:import-config my-daemon-config.edn
```

---

## Philosophy

**"THE WHOLE GRAIN"** - Graindaemon embodies the complete Grain Network philosophy:
- Template/personal split for fork-friendliness
- S6 supervision for reliability
- Clojure for expressiveness
- Auto-start for convenience
- Everything documented and testable

**"Making a wave and surfing the same wave"** - We build the supervision infrastructure (the wave) and immediately use it to run our services (surfing).

---

## Related Projects

- **clojure-s6**: S6 supervision bindings
- **clojure-sixos**: SixOS integration
- **grainwifi**: Dual-wifi connection manager
- **grain-nightlight**: GNOME Night Light daemon
- **grain-metatypes**: Service type definitions

---

## License

MIT License - Part of the Grain Network ecosystem.

---

## Next Steps

After installing Graindaemon:
1. Add more custom services
2. Integrate with GrainOS
3. Create service templates for common tasks
4. Deploy to multiple machines
5. Contribute back to Grain Network

🌾 Happy daemon supervision! 🌾

