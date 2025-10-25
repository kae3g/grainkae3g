# clojure-s6

A Clojure library for managing s6 supervision services with a clean, functional API.

## Overview

`clojure-s6` provides a comprehensive Clojure interface to the s6 supervision suite, enabling you to manage services, handle dependencies, and integrate with s6 logging in a functional, immutable way.

## Features

- **Service Management**: Start, stop, restart, and monitor s6 services
- **Dependency Handling**: Manage service dependencies and ordering
- **Logging Integration**: Seamless integration with s6 logging
- **Event Handling**: React to service state changes
- **Configuration**: Declarative service configuration
- **Monitoring**: Real-time service health monitoring
- **SixOS Ready**: Designed for SixOS and s6-based systems

## Quick Start

```clojure
(require '[clojure-s6.core :as s6])

;; Start a service
(s6/start-service "my-service")

;; Check service status
(s6/service-status "my-service")
;; => {:status :running, :pid 1234, :uptime 3600}

;; Stop a service
(s6/stop-service "my-service")

;; Restart a service
(s6/restart-service "my-service")
```

## Installation

Add to your `deps.edn`:

```clojure
{:deps {clojure-s6/clojure-s6 {:git/url "https://github.com/kae3g/clojure-s6"
                               :sha "..."}}}
```

## API Reference

### Service Management

- `start-service` - Start a service
- `stop-service` - Stop a service  
- `restart-service` - Restart a service
- `service-status` - Get service status
- `list-services` - List all services
- `enable-service` - Enable service at boot
- `disable-service` - Disable service at boot

### Dependency Management

- `add-dependency` - Add service dependency
- `remove-dependency` - Remove service dependency
- `service-dependencies` - Get service dependencies
- `start-with-dependencies` - Start service and dependencies

### Logging

- `service-logs` - Get service logs
- `follow-logs` - Follow service logs in real-time
- `log-level` - Set service log level

### Monitoring

- `monitor-services` - Monitor multiple services
- `service-health` - Get service health metrics
- `alert-on-failure` - Set up failure alerts

## Examples

### Basic Service Management

```clojure
(require '[clojure-s6.core :as s6])

;; Define a service
(def my-service
  {:name "my-app"
   :command "java -jar my-app.jar"
   :directory "/opt/my-app"
   :user "myapp"
   :group "myapp"
   :environment {"JAVA_HOME" "/usr/lib/jvm/java-11"}
   :dependencies ["database" "redis"]})

;; Create and start the service
(s6/create-service my-service)
(s6/start-service "my-app")
```

### Service with Dependencies

```clojure
;; Start a service with all its dependencies
(s6/start-with-dependencies "my-app")
;; => Starts: database, redis, my-app (in order)
```

### Monitoring Services

```clojure
;; Monitor multiple services
(s6/monitor-services ["web-server" "database" "cache"]
  {:on-failure (fn [service] (println (str "Service failed: " service)))
   :check-interval 5000})
```

### Logging Integration

```clojure
;; Follow logs in real-time
(s6/follow-logs "my-app"
  {:on-line (fn [line] (println line))
   :on-error (fn [error] (println "Log error:" error))})
```

## Configuration

### Service Definition

```clojure
{:name "service-name"
 :command "command to run"
 :directory "/path/to/working/dir"
 :user "username"
 :group "groupname"
 :environment {"VAR" "value"}
 :dependencies ["dep1" "dep2"]
 :restart-policy :always
 :restart-delay 1000
 :timeout 30000
 :log-level :info}
```

### Supervision Configuration

```clojure
{:supervision-dir "/etc/s6/sv"
 :log-dir "/var/log/s6"
 :scan-interval 5000
 :default-timeout 30000
 :log-rotation {:max-size "10M"
                :max-files 5}}
```

## SixOS Integration

This library is designed to work seamlessly with SixOS:

```clojure
;; SixOS-specific configuration
(s6/configure-sixos
  {:supervision-dir "/etc/s6/sv"
   :log-dir "/var/log/s6"
   :enable-scanning true})
```

## Development

### Running Tests

```bash
clojure -M:test
```

### Building

```bash
clojure -M:build
```

### Development REPL

```bash
clojure -M:dev
```

## License

MIT License - see LICENSE file for details.

## Contributing

Contributions welcome! Please see CONTRIBUTING.md for guidelines.

## Changelog

### 0.1.0
- Initial release
- Basic service management
- Dependency handling
- Logging integration
- SixOS support

## Related Projects

- [SixOS](https://github.com/sixos/sixos) - NixOS variant with s6
- [s6](https://skarnet.org/software/s6/) - Process supervision suite
- [s6-rc](https://skarnet.org/software/s6-rc/) - Service manager

---

**Built for the SixOS ecosystem with ❤️ in Clojure**
