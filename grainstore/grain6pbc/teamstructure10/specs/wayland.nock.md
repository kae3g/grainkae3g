# Wayland Compositor Protocol - Nock Specification

**Status**: Draft (Phase 1)  
**Author**: kae3g  
**Date**: 2025-10-10  
**Version**: 0.1.0

## Purpose

Specify the Wayland compositor protocol in Nock to enable:
- Formal verification of client-server communication
- Provably correct surface management
- Mathematical bedrock for sovereign display systems
- Implementation independence (C, Rust, Clojure → all from one spec!)

## Background: What is Wayland?

**Wayland** is a display protocol for Linux, replacing the aging X11 protocol.

### Key Concepts:

1. **Compositor** - Server that manages surfaces (windows)
2. **Client** - Applications that create surfaces
3. **Surface** - A rectangular area with pixel data
4. **Buffer** - Pixel data for a surface
5. **Protocol** - Message passing between client and compositor

### Why Wayland over X11:

- **Security**: No global input/output access
- **Performance**: Direct rendering, no X server overhead
- **Simplicity**: ~20K lines vs X11's 250K+ lines
- **Modern**: Designed for GPU compositing

## Nock Model of Wayland

### Core Entities

In Nock, we model Wayland as a cell tree:

```nock
wayland-state = [compositor clients]

compositor = [surfaces globals serial]
clients = [client-1 client-2 ... client-n]

surface = [id buffer geometry state]
buffer = [width height stride format pixels]
geometry = [x y width height]
state = [mapped visible focused damage]
```

### State Tree Structure

```nock
[compositor clients]
  ├─ compositor
  │   ├─ surfaces [surface-1 surface-2 ...]
  │   ├─ globals [wl_compositor wl_shm wl_seat ...]
  │   └─ serial (event counter)
  └─ clients [client-1 client-2 ...]
      ├─ client-1
      │   ├─ fd (file descriptor)
      │   ├─ objects [obj-1 obj-2 ...]
      │   └─ pending-requests [req-1 req-2 ...]
      └─ client-2 ...
```

## Protocol Messages as Nock Formulas

### 1. Create Surface

**Client Request**: "wl_compositor.create_surface"

```nock
create-surface =
  ?[6 [5 [0 2] [1 0]]  ; If compositor exists (not nil)
     [surface-id ++]    ; Increment surface counter
     [0 0]]             ; Error: compositor not found

where:
  surface-id = [0 13]  ; Tree address of surface ID counter
  ++ = [4 [0 2]]       ; Nock 4 = increment operator
```

**Nock Execution**:
```nock
Input:  [compositor clients]
        where compositor = [surfaces globals 42]  ; serial = 42

Process: 
  1. Check compositor exists (Nock 6 = if-then-else)
  2. Increment serial: 42 → 43
  3. Create new surface: [43 nil [0 0 0 0] [0 0 0 0]]
  4. Add to surfaces list

Output: [compositor' clients]
        where compositor' = [surfaces' globals 43]
        where surfaces' = surfaces + new-surface
```

### 2. Attach Buffer

**Client Request**: "wl_surface.attach (buffer, x, y)"

```nock
attach-buffer =
  ?[6 [5 [surface] [1 0]]   ; If surface exists
     [update-surface-buffer  ; Update buffer
      [0 2]                  ; surface
      [0 12]]                ; new buffer
     [0 0]]                  ; Error: surface not found

update-surface-buffer =
  [7 [old-surface]               ; Take old surface
     [surface-id               ; Keep same ID
      new-buffer               ; Replace buffer
      [0 14]                   ; Keep geometry
      [0 30]]]                 ; Keep state

where:
  surface = [0 6]      ; Tree address of target surface
  new-buffer = [0 12]  ; Tree address of buffer to attach
```

**Nock Execution**:
```nock
Input:  [compositor clients]
        where compositor = [surfaces globals serial]
        where surfaces = [[1 old-buffer geo state] ...]

Process:
  1. Find surface by ID (tree navigation)
  2. Replace buffer: old-buffer → new-buffer
  3. Mark surface as damaged (needs repaint)

Output: [compositor' clients]
        where surfaces' = [[1 new-buffer geo state'] ...]
        where state' = state with damage=1
```

### 3. Commit Surface

**Client Request**: "wl_surface.commit"

```nock
commit-surface =
  ?[6 [5 [pending-changes] [1 0]]  ; If pending changes exist
     [apply-changes                 ; Apply all pending changes
      [0 2]                         ; compositor
      [0 6]                         ; surface
      [0 12]]                       ; pending buffer
     [0 0]]                         ; No-op: no changes

apply-changes =
  [7 [compositor surface buffer]   ; Take inputs
     [update-compositor             ; Update compositor state
      [add-to-repaint-queue         ; Queue surface for repaint
       surface]]]

where:
  pending-changes = [0 30]  ; Tree address of pending state
  repaint-queue = [0 62]    ; Compositor's repaint queue
```

**Nock Execution**:
```nock
Input:  [compositor clients]
        where compositor = [surfaces globals serial repaint-queue]
        where surface has pending buffer

Process:
  1. Check if surface has pending changes
  2. Atomically apply all changes to surface
  3. Add surface to repaint queue
  4. Clear pending changes

Output: [compositor' clients]
        where surface has no pending changes
        where repaint-queue' = repaint-queue + surface
```

### 4. Frame Callback

**Client Request**: "wl_surface.frame (callback)"

```nock
frame-callback =
  [add-callback              ; Add callback to surface
   [0 2]                     ; surface
   [callback-id ++]          ; Generate unique callback ID
   [0 12]]                   ; callback object

send-frame-done =
  ?[6 [5 [repaint-done] [1 0]]  ; If repaint completed
     [send-to-client            ; Send frame_done event
      [callback-id]             ; With callback ID
      [current-time]]           ; And timestamp
     [0 0]]                     ; Wait: repaint not done

where:
  callback-id = [0 14]        ; Unique callback ID
  current-time = [0 30]       ; System time in milliseconds
  repaint-done = [0 62]       ; Flag: compositor finished repaint
```

**Nock Execution**:
```nock
Input:  [compositor clients]
        where surface has frame callback registered
        where compositor finished repaint

Process:
  1. Check if repaint is complete
  2. Construct frame_done event with timestamp
  3. Send to client via socket
  4. Remove callback from surface

Output: [compositor' clients']
        where client received frame_done event
        where surface has no pending callback
```

## Advanced Features

### 5. Input Events (Keyboard/Mouse)

**Compositor Event**: "wl_pointer.motion (time, x, y)"

```nock
pointer-motion =
  [broadcast-to-focused     ; Send to focused surface only
   [motion-event            ; Event data
    [time]                  ; Timestamp
    [x-coordinate]          ; X position
    [y-coordinate]]]        ; Y position

broadcast-to-focused =
  ?[6 [5 [focused-surface] [1 0]]  ; If a surface is focused
     [send-to-client               ; Send event
      [focused-surface-client]     ; To surface's client
      [0 12]]                      ; Event data
     [0 0]]                        ; No-op: no focused surface

where:
  focused-surface = [0 30]         ; Currently focused surface
  focused-surface-client = [0 62]  ; Client owning focused surface
```

### 6. Damage Tracking

**Client Request**: "wl_surface.damage (x, y, width, height)"

```nock
damage-surface =
  [add-damage-rect         ; Add damage rectangle to surface
   [0 2]                   ; surface
   [damage-region ++]      ; Append to damage list
   [x y width height]]     ; Damage bounds

optimize-damage =
  [merge-overlapping-rects    ; Coalesce damage regions
   [damage-list]              ; All damage rects
   [merge-threshold 16]]      ; Merge if distance < 16px

where:
  damage-region = [0 30]       ; Surface's damage region list
  merge-threshold = 16         ; Pixels: merge nearby rects
```

### 7. Subsurfaces (Hierarchical)

**Client Request**: "wl_subcompositor.get_subsurface (surface, parent)"

```nock
create-subsurface =
  [add-child-to-parent     ; Link child to parent
   [parent-surface]        ; Parent
   [child-surface]         ; Child
   [position [x y]]]       ; Relative position

render-hierarchy =
  [render-tree             ; Depth-first rendering
   [parent]                ; Start with parent
   [render-surface         ; Render parent
    [0 2]]                 ; Parent surface
   [map-children           ; Render all children
    render-surface
    [children]]]           ; Child surfaces

where:
  parent-surface = [0 6]     ; Parent surface tree address
  child-surface = [0 12]     ; Child surface tree address
  children = [0 30]          ; List of child surfaces
```

## Proof Obligations

### Invariants to Verify:

1. **Surface Uniqueness**: No two surfaces have the same ID
   ```nock
   ∀ s1, s2 ∈ surfaces : s1 ≠ s2 ⇒ id(s1) ≠ id(s2)
   ```

2. **Buffer Consistency**: A surface's buffer matches its geometry
   ```nock
   ∀ s ∈ surfaces : 
     buffer-width(s) ≥ geometry-width(s) ∧
     buffer-height(s) ≥ geometry-height(s)
   ```

3. **Focus Exclusivity**: At most one surface is focused at a time
   ```nock
   |{s ∈ surfaces : focused(s) = 1}| ≤ 1
   ```

4. **Message Ordering**: Client requests are processed in order
   ```nock
   ∀ r1, r2 ∈ requests : 
     timestamp(r1) < timestamp(r2) ⇒ processed(r1) before processed(r2)
   ```

5. **Memory Safety**: Buffers are deallocated when surface is destroyed
   ```nock
   ∀ s ∈ destroyed-surfaces : buffer(s) = nil ∧ no references to buffer(s)
   ```

## Jets (Optimizations)

### Fast Paths for Performance:

1. **Buffer Blit**: Fast pixel copy using GPU
   ```nock
   blit-buffer-jet =
     [gpu-memcpy            ; Use GPU DMA
      [src-buffer]          ; Source buffer
      [dst-framebuffer]     ; Destination framebuffer
      [geometry]]           ; Copy region
   
   ; Nock equivalent (slow path):
   blit-buffer-slow =
     [map-pixels           ; Copy pixel by pixel
      [copy-pixel]         ; Copy function
      [src-buffer]         ; Source
      [dst-framebuffer]]   ; Destination
   ```

2. **Damage Merge**: Fast rectangle coalescing
   ```nock
   merge-damage-jet =
     [spatial-index-merge    ; Use R-tree for fast merge
      [damage-rects]]        ; List of rectangles
   
   ; Nock equivalent (slow path):
   merge-damage-slow =
     [quadratic-merge       ; O(n²) pairwise comparison
      [damage-rects]]
   ```

3. **Surface Lookup**: Hash table instead of linear search
   ```nock
   find-surface-jet =
     [hash-lookup          ; O(1) hash table lookup
      [surface-id]         ; ID to find
      [surface-map]]       ; Hash table

   ; Nock equivalent (slow path):
   find-surface-slow =
     [linear-search        ; O(n) linear search
      [surface-id]
      [surface-list]]
   ```

## Implementation Notes

### C Implementation (libwayland):
- Uses Unix sockets for IPC
- Wire protocol: binary messages
- Event loop: poll() or epoll()

### Rust Implementation (smithay):
- Async/await with tokio
- Zero-copy buffer sharing
- Type-safe protocol bindings

### Clojure Implementation (grainstore):
- Pure functions (Nock → Clojure)
- Clojure Spec for validation
- `core.async` for event loop

## Example: Complete Frame Render Cycle

```nock
frame-cycle =
  [client-commits-buffer         ; Step 1: Client attaches + commits buffer
   [attach-buffer [buffer-42]]   
   [commit-surface]]
  
  [compositor-queues-repaint     ; Step 2: Compositor queues repaint
   [add-to-repaint-queue
    [surface]]]
  
  [compositor-renders            ; Step 3: Compositor renders to framebuffer
   [render-surface
    [surface]
    [framebuffer]]]
  
  [compositor-swaps-buffers      ; Step 4: Display new frame (vsync)
   [swap-buffers
    [frontbuffer]
    [backbuffer]]]
  
  [compositor-sends-frame-done   ; Step 5: Notify client frame is done
   [send-to-client
    [frame-callback-42]
    [timestamp]]]
```

**Nock Execution Trace**:
```
1. Client: attach_buffer(buffer-42, 0, 0)
   State: surface.pending-buffer = buffer-42

2. Client: commit()
   State: surface.buffer = buffer-42, surface.pending-buffer = nil
   Action: add surface to repaint-queue

3. Compositor: repaint_all()
   Action: render each surface in repaint-queue to framebuffer
   Optimization: Use damage regions (only redraw changed areas)

4. Compositor: swap_buffers()
   Action: Send framebuffer to display (vsync)
   State: frontbuffer ↔ backbuffer

5. Compositor: send_frame_done(callback-42, time=167ms)
   Action: Send frame_done event to client
   State: callback-42 removed from surface

6. Client: attach_buffer(buffer-43, 0, 0)  ; Next frame!
   ... cycle repeats
```

## Verification Strategy

### Phase 1: Specification (Current)
- Define Nock formulas for core operations ✅
- Document proof obligations ✅
- Create examples ✅

### Phase 2: Implementation (Next)
- Implement in Clojure with Spec
- Write comprehensive test suite
- Verify Clojure ↔ Nock equivalence

### Phase 3: Jets (Performance)
- Implement fast paths (GPU, hash tables)
- Prove jet ↔ Nock equivalence
- Benchmark against libwayland

### Phase 4: Integration (Full Stack)
- Connect to Hyprland or Sway
- Test with real applications (wezterm, brave)
- Verify no regressions

## References

- **Wayland Book**: https://wayland-book.com/
- **libwayland**: https://gitlab.freedesktop.org/wayland/wayland
- **smithay**: https://github.com/Smithay/smithay
- **wlroots**: https://gitlab.freedesktop.org/wlroots/wlroots
- **Nock**: https://urbit.org/docs/nock/definition

## Next Steps

1. **Implement in Clojure**: `src/grainstore/wayland.clj`
2. **Write tests**: `test/grainstore/wayland_test.clj`
3. **Document equivalence**: `grainstore/equivalence/wayland-clj-nock.md`
4. **Add jets**: `grainstore/jets/wayland.jet.md`

---

**This specification is eternal. Implementations are temporary.**

The Nock formulas define what Wayland *means*. Languages (C, Rust, Clojure) are just ways to execute that meaning.

**Plant lens**: "Wayland is like the relationship between plants and sunlight—surfaces (leaves) collect input (photons), the compositor (stem) routes energy, and the display (roots) anchors it all. The protocol is the grammar of growth itself." 🌱✨

