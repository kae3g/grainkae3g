# GrainWiFi - Dual-Connection Network Manager

**Created**: 12025-10-22  
**Purpose**: Intelligent dual-wifi management for forest environments  
**Platform**: GNOME Network Manager (Ubuntu 24.04 LTS)  
**Status**: Priority Development

---

## Overview

GrainWiFi is a smart network management utility designed for environments with **intermittent connectivity**, specifically managing:

1. 📱 **Cellular Tethering** - Mobile hotspot (primary fallback)
2. 🛰️ **Starlink Internet** - Satellite connection (preferred when available)

**Key Features**:
- 🔄 Automatic connection switching based on availability and quality
- 📊 Connection quality monitoring (ping, bandwidth, stability)
- 🔔 Desktop notifications for connection changes
- ⚡ Priority-based routing (Starlink preferred, cellular fallback)
- 🌲 Forest-optimized retry logic for intermittent connections

---

## Use Case

**Environment**: Central Illinois forest  
**Hardware**: Framework 16 laptop  
**Challenges**:
- Starlink occasionally loses connection (weather, obstructions)
- Cellular tethering is less reliable but always available
- Need seamless switching without manual intervention
- GNOME Network Manager UI is better than terminal-based solutions

---

## Architecture

### Core Components

```
grainwifi/
├── README.md                    # This file
├── deps.edn                     # Clojure dependencies
├── bb.edn                       # Babashka tasks
├── src/grainwifi/
│   ├── core.clj                 # Main logic
│   ├── nmcli.clj                # NetworkManager CLI interface
│   ├── monitor.clj              # Connection quality monitoring
│   ├── switch.clj               # Automatic switching logic
│   └── notify.clj               # Desktop notifications
├── scripts/
│   ├── grainwifi-start.bb       # Start monitoring daemon
│   ├── grainwifi-stop.bb        # Stop daemon
│   ├── grainwifi-status.bb      # Show current connection status
│   └── grainwifi-switch.bb      # Manual connection switch
└── config/
    └── grainwifi.edn            # User configuration
```

### Technology Stack

- **Babashka**: Fast Clojure scripting for system automation
- **nmcli**: NetworkManager command-line interface
- **notify-send**: GNOME desktop notifications
- **systemd**: Service management (optional)

---

## Configuration

**Example** `config/grainwifi.edn`:

```clojure
{:connections
 {:starlink
  {:name "Starlink"           ;; NetworkManager connection name
   :priority 1                 ;; Lower = higher priority
   :preferred true             ;; Prefer this connection when available
   :ssid "Starlink-xxxxx"     ;; WiFi SSID (optional)
   :min-quality 60            ;; Minimum quality threshold (0-100)
   :ping-host "1.1.1.1"       ;; Host to ping for quality check
   :ping-count 3              ;; Number of pings
   :ping-timeout 2000}        ;; Timeout in ms
  
  :cellular
  {:name "Cellular-Tethering"
   :priority 2                 ;; Fallback connection
   :preferred false
   :min-quality 40
   :ping-host "8.8.8.8"
   :ping-count 3
   :ping-timeout 3000}}
 
 :monitoring
 {:check-interval 30           ;; Check connection quality every 30 seconds
  :retry-interval 10           ;; Retry failed connection after 10 seconds
  :switch-threshold 3          ;; Switch after 3 consecutive quality failures
  :notify true                 ;; Show desktop notifications
  :log-file "/tmp/grainwifi.log"}
 
 :switching
 {:auto-switch true            ;; Enable automatic switching
  :prefer-starlink true        ;; Always try Starlink first
  :hysteresis 60               ;; Wait 60s before switching back to preferred
  :graceful-switch true}}      ;; Wait for downloads to finish
```

---

## Usage

### Start Monitoring Daemon

```bash
# Start GrainWiFi daemon
bb grainwifi:start

# Start in foreground (for debugging)
bb grainwifi:start --foreground

# Start with custom config
bb grainwifi:start --config ~/.config/grainwifi.edn
```

### Check Connection Status

```bash
# Show current connection and quality
bb grainwifi:status

# Example output:
# 🛰️  Connected: Starlink (Quality: 85%)
#     Ping: 32ms | Bandwidth: 150 Mbps
# 📱 Available: Cellular-Tethering (Quality: 62%)
#     Last switch: 15 minutes ago
```

### Manual Connection Switch

```bash
# Switch to cellular
bb grainwifi:switch cellular

# Switch to starlink
bb grainwifi:switch starlink

# Switch to best available
bb grainwifi:switch auto
```

### Stop Daemon

```bash
bb grainwifi:stop
```

---

## Monitoring Logic

### Connection Quality Metrics

```clojure
(defn check-connection-quality [connection]
  {:ping-latency (ping-test (:ping-host connection))
   :packet-loss (calculate-packet-loss)
   :bandwidth (estimate-bandwidth)
   :stability (connection-uptime)
   :signal-strength (wifi-signal-dbm)
   :quality-score (calculate-weighted-score)})
```

**Quality Score** (0-100):
- **100-80**: Excellent - Stay connected
- **79-60**: Good - Monitor closely
- **59-40**: Fair - Consider switching
- **39-20**: Poor - Switch if better available
- **19-0**: Failed - Switch immediately

### Switching Algorithm

```clojure
(defn should-switch? [current-conn other-conns]
  (let [current-quality (get-quality current-conn)
        current-priority (get-priority current-conn)
        better-conns (find-better-connections current-conn other-conns)]
    (or
      ;; Current connection is failing
      (< current-quality (:min-quality current-conn))
      
      ;; Higher priority connection available with good quality
      (and (seq better-conns)
           (> (get-quality (first better-conns))
              (+ current-quality (:hysteresis config))))
      
      ;; Preferred connection came back online
      (and (:prefer-starlink config)
           (not (:preferred current-conn))
           (connection-available? :starlink)
           (> (get-quality :starlink) 60)))))
```

---

## Desktop Notifications

**Connection Changes**:
```
🛰️ Connected to Starlink
   Quality: 85% | Latency: 32ms
```

**Quality Warnings**:
```
⚠️  Starlink Quality Degraded
   Quality: 45% | Monitoring...
```

**Automatic Switches**:
```
🔄 Switched to Cellular Tethering
   Reason: Starlink unavailable
```

**Connection Restored**:
```
✅ Starlink Restored
   Switching back in 60 seconds...
```

---

## Installation

### 1. Dependencies

```bash
# Install NetworkManager and tools
sudo apt install network-manager network-manager-gnome

# Install notification daemon (usually pre-installed on GNOME)
sudo apt install libnotify-bin

# Install Babashka (if not already installed)
curl -s https://raw.githubusercontent.com/babashka/babashka/master/install | bash
```

### 2. Clone GrainWiFi

```bash
# Already in grainstore
cd ~/kae3g/grainkae3g/grainstore/grainwifi
```

### 3. Configure Connections

```bash
# List available NetworkManager connections
nmcli connection show

# Edit config with your connection names
cp config/grainwifi.template.edn config/grainwifi.edn
vim config/grainwifi.edn
```

### 4. Test Manual Switching

```bash
# Test connection switching
bb grainwifi:switch starlink
sleep 5
bb grainwifi:status
bb grainwifi:switch cellular
sleep 5
bb grainwifi:status
```

### 5. Start Daemon

```bash
# Start monitoring
bb grainwifi:start

# Verify it's running
bb grainwifi:status
```

---

## Systemd Service (Optional)

**Create** `/etc/systemd/system/grainwifi.service`:

```ini
[Unit]
Description=GrainWiFi Dual-Connection Manager
After=network.target NetworkManager.service

[Service]
Type=simple
User=xy
Environment=HOME=/home/xy
ExecStart=/usr/local/bin/bb /home/xy/kae3g/grainkae3g/grainstore/grainwifi/scripts/grainwifi-start.bb --foreground
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

**Enable and start**:

```bash
sudo systemctl daemon-reload
sudo systemctl enable grainwifi.service
sudo systemctl start grainwifi.service
sudo systemctl status grainwifi.service
```

---

## Troubleshooting

### Issue: nmcli not found

```bash
# Install NetworkManager
sudo apt install network-manager
```

### Issue: notify-send not working

```bash
# Install libnotify
sudo apt install libnotify-bin

# Test notification
notify-send "Test" "GrainWiFi notification test"
```

### Issue: Connections not switching

```bash
# Check connection names
nmcli connection show

# Verify config matches connection names
cat config/grainwifi.edn

# Check logs
tail -f /tmp/grainwifi.log
```

### Issue: Permission denied

```bash
# Add user to netdev group
sudo usermod -aG netdev $USER

# Log out and back in
```

---

## Forest-Optimized Features

### Aggressive Retry Logic

```clojure
;; Forest environments need more aggressive retries
{:retry
 {:max-attempts 10         ;; Retry up to 10 times
  :exponential-backoff true ;; 10s, 20s, 40s, 80s...
  :max-backoff 300         ;; Cap at 5 minutes
  :jitter 0.2}}            ;; Add ±20% randomness
```

### Weather-Aware Switching

```clojure
;; Future: Integrate weather API
{:weather
 {:check-forecast true           ;; Check upcoming weather
  :prefer-cellular-in-storm true ;; Pre-emptively switch
  :api-key "YOUR_KEY"}}
```

### Connection History

```clojure
;; Track connection patterns
{:history
 {:log-switches true
  :analyze-patterns true    ;; Learn best times for each connection
  :suggest-schedule true}}  ;; "Starlink usually better 9am-5pm"
```

---

## Future Enhancements

- [ ] **Bandwidth-aware routing** - Route heavy downloads through Starlink
- [ ] **Time-based policies** - Different behaviors for work hours vs. evening
- [ ] **Cost tracking** - Monitor cellular data usage
- [ ] **VPN integration** - Maintain VPN across connection switches
- [ ] **Multi-connection bonding** - Use both simultaneously for critical uploads
- [ ] **Web dashboard** - View connection history and statistics
- [ ] **Mobile app** - Remote monitoring and control

---

## Philosophy

**"THE WHOLE GRAIN"** - Sometimes the best technology is the one that **just works**. GrainWiFi embodies:
- 🌾 **Simplicity**: Automatic operation, minimal configuration
- 🌍 **Sustainability**: Efficient use of available resources
- 🔧 **Practicality**: Solves real problems in forest environments
- 📚 **Learning**: Every network switch is a data point for improvement

**"Making a wave and surfing the same wave"** - GrainWiFi creates resilient connectivity, then rides that connection to build the Grain Network.

---

## License

MIT License - See main Grain Network repository for details.

---

## Related Projects

- **grainstore**: Grain Network dependency manager
- **clojure-s6**: Service supervision for Grain Network
- **grain-metatypes**: Type system for networked data
- **grainneovedic**: Timestamp system for immutable paths

---

## Support

Questions? Issues? Find me in the forest with intermittent Starlink connectivity. 🌲🛰️📱

