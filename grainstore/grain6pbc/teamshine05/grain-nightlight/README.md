# Grain Night Light - GNOME Warm Light Filter Daemon

**Created**: 12025-10-22--CDT  
**Purpose**: Auto-start GNOME Night Light with S6 supervision  
**Platform**: Ubuntu 24.04 LTS GNOME 46  
**Temperature**: 2000K (maximum warmth)

---

## Overview

Grain Night Light is a S6-supervised daemon that ensures GNOME Night Light is always enabled with warm color temperature (2000K) on boot.

**Features**:
- ✅ Auto-enables Night Light on system startup
- ✅ S6 supervision (restarts if crashes)
- ✅ Configurable color temperature (default: 2000K)
- ✅ Manual schedule or always-on mode
- ✅ Integration with GrainOS service ecosystem

---

## Quick Start

### Install

```bash
cd ~/kae3g/grainkae3g/grainstore/grain-nightlight

# Install dependencies
bb deps

# Test service
bb grain-nightlight:test

# Install S6 service
bb grain-nightlight:install

# Start service
bb grain-nightlight:start

# Check status
bb grain-nightlight:status
```

---

## Architecture

### S6 Service Structure

```
grainstore/grain-nightlight/
├── README.md
├── deps.edn
├── bb.edn
├── src/
│   └── grain_nightlight/
│       ├── core.clj          # Main daemon logic
│       ├── gsettings.clj     # GNOME settings interface
│       └── monitor.clj       # Monitor Night Light status
├── s6-services/
│   └── grain-nightlight/
│       ├── run               # S6 run script
│       ├── finish            # S6 finish script
│       └── type              # S6 service type
└── scripts/
    ├── install-service.bb    # Install S6 service
    ├── enable-nightlight.bb  # Enable Night Light once
    └── check-status.bb       # Check current status
```

### How It Works

1. **S6 Supervision**: S6 runs `grain-nightlight` daemon on boot
2. **Initial Setup**: Daemon enables Night Light with 2000K temperature
3. **Monitoring**: Continuously monitors Night Light status
4. **Auto-Restore**: If Night Light is disabled, daemon re-enables it
5. **Graceful Shutdown**: S6 handles clean daemon shutdown

---

## Configuration

### Default Settings

```clojure
{:night-light
 {:enabled true
  :temperature 2000              ;; 1000-4000 (lower = warmer)
  :schedule-automatic false      ;; false = always on, true = auto schedule
  :schedule-from 20.0            ;; 8 PM (if automatic)
  :schedule-to 6.0               ;; 6 AM (if automatic)
  :monitor-interval 30}          ;; Check every 30 seconds
 
 :logging
 {:level :info
  :log-file "/tmp/grain-nightlight.log"}}
```

### Custom Configuration

Create `~/.config/grain-nightlight/config.edn`:

```clojure
{:night-light
 {:temperature 2500              ;; Slightly less warm
  :schedule-automatic true       ;; Enable auto schedule
  :monitor-interval 60}}         ;; Check every minute
```

---

## Usage

### Enable Night Light (One-Time)

```bash
# Enable with default 2000K
bb grain-nightlight:enable

# Enable with custom temperature
bb grain-nightlight:enable --temp 2500

# Enable with auto schedule
bb grain-nightlight:enable --auto-schedule
```

### Start Daemon

```bash
# Start supervised daemon
bb grain-nightlight:start

# Start in foreground (for debugging)
bb grain-nightlight:run
```

### Check Status

```bash
bb grain-nightlight:status

# Example output:
# 🌙 Grain Night Light Status
# ═══════════════════════════════════════════
# 
# Night Light: ✅ Enabled
# Temperature: 2000K (maximum warmth)
# Schedule: Manual (always on)
# Last checked: 2025-10-22 18:30:45 CDT
# 
# Daemon: ✅ Running (PID: 12345)
# Uptime: 2 hours 15 minutes
# Restarts: 0
```

### Stop Daemon

```bash
bb grain-nightlight:stop
```

### Restart Daemon

```bash
bb grain-nightlight:restart
```

---

## S6 Integration

### Install Service to S6 Scan Directory

```bash
# Install to user S6 directory
bb grain-nightlight:install

# This creates:
# ~/.s6/grain-nightlight/run
# ~/.s6/grain-nightlight/finish
# ~/.s6/grain-nightlight/type
```

### S6 Service Commands

```bash
# Using S6 directly
s6-svc -u ~/.s6/grain-nightlight   # Start
s6-svc -d ~/.s6/grain-nightlight   # Stop
s6-svc -r ~/.s6/grain-nightlight   # Restart
s6-svstat ~/.s6/grain-nightlight   # Status
```

### Auto-Start on Boot

**Option 1: User systemd service** (recommended)

Create `~/.config/systemd/user/s6-grain-nightlight.service`:

```ini
[Unit]
Description=Grain Night Light S6 Daemon
After=graphical-session.target

[Service]
Type=simple
ExecStart=/usr/bin/bb %h/kae3g/grainkae3g/grainstore/grain-nightlight/scripts/run-daemon.bb
Restart=always
RestartSec=10

[Install]
WantedBy=default.target
```

Enable:
```bash
systemctl --user daemon-reload
systemctl --user enable s6-grain-nightlight.service
systemctl --user start s6-grain-nightlight.service
```

**Option 2: GNOME autostart**

Create `~/.config/autostart/grain-nightlight.desktop`:

```desktop
[Desktop Entry]
Type=Application
Name=Grain Night Light
Comment=Auto-enable GNOME Night Light with warm temperature
Exec=/usr/local/bin/bb /home/xy/kae3g/grainkae3g/grainstore/grain-nightlight/scripts/run-daemon.bb
Terminal=false
Hidden=false
X-GNOME-Autostart-enabled=true
```

**Option 3: S6-rc integration** (advanced)

For full S6-rc service management, see `clojure-s6` documentation.

---

## Implementation

### Core Daemon (Clojure)

```clojure
(ns grain-nightlight.core
  (:require [grain-nightlight.gsettings :as gs]
            [grain-nightlight.monitor :as mon]
            [clojure.tools.logging :as log]))

(defn ensure-nightlight-enabled
  "Ensure Night Light is enabled with correct settings"
  [config]
  (let [{:keys [temperature schedule-automatic]} (:night-light config)]
    (log/info "Checking Night Light status...")
    
    (when-not (gs/night-light-enabled?)
      (log/warn "Night Light is disabled, enabling...")
      (gs/enable-night-light!))
    
    (when (not= (gs/get-temperature) temperature)
      (log/info (str "Setting temperature to " temperature "K"))
      (gs/set-temperature! temperature))
    
    (when (not= (gs/schedule-automatic?) schedule-automatic)
      (log/info (str "Setting schedule mode to " (if schedule-automatic "automatic" "manual")))
      (gs/set-schedule-automatic! schedule-automatic))))

(defn daemon-loop
  "Main daemon loop - monitors and restores Night Light"
  [config]
  (log/info "Starting Grain Night Light daemon...")
  (let [{:keys [monitor-interval]} (:night-light config)]
    (loop []
      (try
        (ensure-nightlight-enabled config)
        (catch Exception e
          (log/error e "Error in daemon loop")))
      
      (Thread/sleep (* monitor-interval 1000))
      (recur))))

(defn -main [& args]
  (let [config (load-config)]
    (daemon-loop config)))
```

### GNOME Settings Interface

```clojure
(ns grain-nightlight.gsettings
  (:require [babashka.process :as p]))

(def schema "org.gnome.settings-daemon.plugins.color")

(defn gsettings-get [key]
  (-> (p/shell {:out :string} "gsettings" "get" schema key)
      :out
      str/trim))

(defn gsettings-set! [key value]
  (p/shell "gsettings" "set" schema key (str value)))

(defn night-light-enabled? []
  (= "true" (gsettings-get "night-light-enabled")))

(defn enable-night-light! []
  (gsettings-set! "night-light-enabled" "true"))

(defn get-temperature []
  (parse-long (gsettings-get "night-light-temperature")))

(defn set-temperature! [temp]
  (gsettings-set! "night-light-temperature" temp))

(defn schedule-automatic? []
  (= "true" (gsettings-get "night-light-schedule-automatic")))

(defn set-schedule-automatic! [auto?]
  (gsettings-set! "night-light-schedule-automatic" (str auto?)))
```

---

## Troubleshooting

### Night Light not changing color

**Check if enabled**:
```bash
gsettings get org.gnome.settings-daemon.plugins.color night-light-enabled
# Should output: true
```

**Check temperature**:
```bash
gsettings get org.gnome.settings-daemon.plugins.color night-light-temperature
# Should output: uint32 2000
```

**Manually enable**:
```bash
gsettings set org.gnome.settings-daemon.plugins.color night-light-enabled true
gsettings set org.gnome.settings-daemon.plugins.color night-light-temperature 2000
```

### Daemon not starting

**Check logs**:
```bash
tail -f /tmp/grain-nightlight.log
```

**Test manually**:
```bash
cd grainstore/grain-nightlight
bb grain-nightlight:run
# Should run in foreground and show log output
```

### S6 service not auto-starting

**Check systemd user service**:
```bash
systemctl --user status s6-grain-nightlight.service
```

**Check GNOME autostart**:
```bash
ls -la ~/.config/autostart/grain-nightlight.desktop
```

---

## Philosophy

**"THE WHOLE GRAIN"** - Even something as simple as warm lighting deserves proper supervision and automation. By integrating Night Light with our S6 daemon system, we ensure consistency and reliability across the entire Grain Network ecosystem.

**"Making a wave and surfing the same wave"** - We use our own tools (clojure-s6, Babashka) to manage system services, demonstrating the power of the Grain Network stack.

---

## Related Projects

- **clojure-s6**: S6 supervision bindings
- **grainwifi**: Dual-wifi connection manager
- **grainsource-gnome**: GNOME configuration scripts
- **grain-metatypes**: Type system for service definitions

---

## License

MIT License - Part of the Grain Network ecosystem.

---

## Support

Questions? Check the main Grain Network repository or open an issue.

🌙 Enjoy your warm, supervised night lighting! 🌾

