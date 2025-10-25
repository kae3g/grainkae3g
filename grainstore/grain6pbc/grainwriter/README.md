# ✍️ Grainwriter Library

**Clojure library for the Grainwriter e-ink writing device**

## Overview

The `grainwriter` library provides core functionality for the Grainwriter device:
- RAM-only document storage with compression
- USB-C file synchronization (iPod Touch-style)
- E-ink display optimization
- Markdown editing and rendering
- Spell checking and word counting
- s6 supervision integration

## Installation

Add to your `deps.edn`:

```clojure
{:deps {io.github.grainnetwork/grainwriter {:git/url "https://github.com/grainnetwork/grainwriter"
                                            :git/sha "..."}}}
```

## Quick Start

```clojure
(require '[grain-writer.core :as gw])
(require '[grain-writer.display :as display])
(require '[grain-writer.sync :as sync])

;; Create a new document
(def doc (gw/create-document "My First Note" "This is the content"))

;; Render to e-ink display
(display/render-document doc)

;; Sync to USB device when connected
(when-let [usb-device (sync/detect-usb-device)]
  (sync/sync-documents usb-device))
```

## API Reference

See [GRAINWRITER-DESIGN.md](GRAINWRITER-DESIGN.md) for complete design documentation.

## License

MIT License - See LICENSE file for details.

## Contributing

This is part of the grainkae3g/grainnetwork ecosystem. Contributions welcome!

See [github.com/grainnetwork/grainwriter](https://github.com/grainnetwork/grainwriter) for more details.


