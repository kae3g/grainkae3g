# 🖥️ humble-desktop - GNOME Clone in Clojure

A complete desktop environment written in Clojure, mirroring GNOME patterns but optimized for musl libc and Sway integration.

## Vision

**"Every Great Desktop Starts With Humble UI"**

Create a native Clojure desktop environment that:
- Replaces GNOME with Clojure-native components
- Integrates seamlessly with Sway window manager
- Optimizes for musl libc performance
- Provides a complete application ecosystem
- Maintains GNOME's user experience patterns

## Architecture

### Core Components

```
humble-desktop/
├── src/humble-desktop/
│   ├── core.clj              # Main desktop environment
│   ├── compositor.clj        # Sway integration
│   ├── shell.clj             # GNOME Shell equivalent
│   ├── applications.clj      # Application launcher
│   ├── settings.clj          # System settings
│   ├── notifications.clj    # Notification system
│   ├── file-manager.clj      # File browser
│   ├── terminal.clj          # Terminal emulator
│   └── theme.clj             # Theme management
├── deps.edn
└── README.md
```

### GNOME Equivalents

| GNOME Component | humble-desktop | Purpose |
|----------------|----------------|---------|
| GNOME Shell | `humble-shell` | Main desktop interface |
| Nautilus | `humble-file-manager` | File browser |
| Terminal | `humble-terminal` | Terminal emulator |
| Settings | `humble-settings` | System configuration |
| Notifications | `humble-notifications` | Notification system |
| Applications | `humble-applications` | App launcher |
| Workspaces | `humble-workspaces` | Virtual desktops |
| Activities | `humble-activities` | Activity overview |

### Sway Integration

- **Window Management**: Sway handles window management
- **Compositing**: Sway provides compositing
- **Input**: Sway manages input devices
- **Display**: Sway handles display configuration
- **humble-desktop**: Provides GNOME-like user experience

### musl libc Optimization

- **Memory Efficiency**: Optimized for musl's memory model
- **Performance**: Leverages musl's performance characteristics
- **Compatibility**: Works with Alpine Linux and other musl distros
- **Security**: Benefits from musl's security features

## Features

### Desktop Environment
- **Activities Overview**: GNOME-style activity overview
- **Application Grid**: Grid-based application launcher
- **Workspaces**: Virtual desktop management
- **Notifications**: Rich notification system
- **Search**: Global search functionality
- **Settings**: Comprehensive system settings

### Applications
- **File Manager**: Native file browser
- **Terminal**: Terminal emulator
- **Text Editor**: Built-in text editor
- **Image Viewer**: Image viewing application
- **PDF Viewer**: PDF document viewer
- **Calculator**: Calculator application

### Integration
- **Sway**: Seamless Sway integration
- **Wayland**: Native Wayland support
- **Humble UI**: Built on Humble UI framework
- **Clojure**: 100% Clojure implementation
- **musl**: Optimized for musl libc

## Development

### Prerequisites
- Clojure 1.12.0+
- Humble UI
- Sway window manager
- musl libc (Alpine Linux recommended)

### Building
```bash
# Start REPL
clj -M:dev

# Build desktop environment
bb build-humble-desktop.bb

# Run desktop environment
clj -M -m humble-desktop.core
```

### Testing
```bash
# Test on Alpine Linux VM
bb test-humble-desktop.bb

# Test Sway integration
bb test-sway-integration.bb
```

## Philosophy

### GNOME Patterns
- **User Experience**: Maintain GNOME's excellent UX
- **Accessibility**: Full accessibility support
- **Internationalization**: Complete i18n support
- **Consistency**: Consistent design language
- **Simplicity**: Simple, intuitive interface

### Clojure Advantages
- **REPL Development**: Live development and debugging
- **Functional Programming**: Immutable, predictable code
- **Concurrency**: Excellent concurrency support
- **Performance**: JVM performance with Clojure elegance
- **Ecosystem**: Rich Clojure ecosystem

### musl Benefits
- **Memory Efficiency**: Lower memory footprint
- **Performance**: Better performance characteristics
- **Security**: Enhanced security features
- **Portability**: Better portability across systems
- **Simplicity**: Simpler, more predictable behavior

## Roadmap

### Phase 1: Core Shell
- [ ] Basic shell implementation
- [ ] Sway integration
- [ ] Application launcher
- [ ] Basic theming

### Phase 2: Applications
- [ ] File manager
- [ ] Terminal emulator
- [ ] Text editor
- [ ] Settings panel

### Phase 3: Advanced Features
- [ ] Notifications system
- [ ] Search functionality
- [ ] Workspace management
- [ ] Accessibility features

### Phase 4: Polish
- [ ] Complete theming system
- [ ] Performance optimization
- [ ] Documentation
- [ ] Testing suite

## Contributing

This is part of the Grain Network ecosystem. See `grainstore/grainpbc` for contribution guidelines.

## License

MIT License - See LICENSE file for details.
