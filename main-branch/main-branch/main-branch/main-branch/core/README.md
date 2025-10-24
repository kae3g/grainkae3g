# 🌾 Grain Network Core Library

Shared library for all SixOS Humble UI applications.

## Purpose

This core library provides common functionality for all Grain Network desktop applications:
- Content management and parsing
- UI components and themes
- Navigation system
- Data pipeline utilities
- Service management

## Applications

- **grainbook** - Content reader and management
- **graincourse** - Course and lesson management
- **grain6-desktop** - Service orchestration and monitoring
- **grainpath** - Navigation and wayfinding
- **graintime** - Time-aware applications and calendar

## Usage

```clojure
;; In your app's deps.edn
{:deps {grain-network/core {:local/root "../core"}}}
```

## Development

```bash
# Start REPL
clj -M:dev

# Run tests
clj -M:test

# Build for distribution
bb build-core.bb
```

## Architecture

- **content.clj** - Content parsing and management
- **data.clj** - Data pipeline utilities
- **ui-components.clj** - Shared Humble UI components
- **navigation.clj** - Navigation system
- **services.clj** - Core service management
