# 📸 Grainstore Photos
## *"Decentralized photo storage for the Grain Network"*

**Purpose:** Store and sync photos across the Grain Hardware Ecosystem  
**Integration:** Graincamera → Grainwriter → ICP → Nostr  
**Format Support:** AVIF (primary), HEIC, PNG, JPEG

---

## 🗂️ DIRECTORY STRUCTURE

```
photos/
├── raw/          # Original photos from Graincamera (AVIF format)
├── processed/    # Edited/processed photos (AVIF format)
├── metadata/     # JSON/EDN metadata files
└── README.md     # This file
```

---

## 📁 USAGE

### Storing Photos

**From Graincamera:**
```bash
# Auto-sync from Graincamera to grainstore/photos/raw/
grainsource sync graincamera
```

**Manual Import:**
```bash
# Copy photos to raw directory
cp ~/Downloads/*.avif grainstore/photos/raw/
```

### Processing Photos

**Using clojure-photos library:**
```clojure
(require '[clojure-photos.core :as photos])

;; Load raw photo
(def photo (photos/load-photo "grainstore/photos/raw/IMG_0001.avif"))

;; Process photo (resize, color correction, etc.)
(def processed (-> photo
                   (photos/resize {:width 1920 :height 1080})
                   (photos/color-correct {:temperature 5500})
                   (photos/sharpen {:amount 0.5})))

;; Save processed photo
(photos/save-photo processed "grainstore/photos/processed/IMG_0001_edited.avif")
```

### Metadata Management

**Metadata Format (EDN):**
```clojure
{:filename "IMG_0001.avif"
 :camera "Graincamera"
 :timestamp "2025-01-22T10:30:00Z"
 :location {:lat 40.1105 :lon -88.2073}  ; Urbana, IL
 :tags ["landscape" "sunset" "grain-network"]
 :format :avif
 :dimensions {:width 6240 :height 4160}
 :exif {:aperture "f/2.8"
        :shutter-speed "1/250"
        :iso 100
        :focal-length "35mm"}
 :nostr-event-id "note1abc123..."
 :icp-canister-id "rrkah-fqaaa-aaaaa-aaaaq-cai"
 :urbit-path "/photos/IMG_0001.avif"}
```

---

## 🌐 SYNC STRATEGY

### ICP Integration

**Upload to ICP Canister:**
```clojure
(require '[clojure-photos.icp :as icp])

;; Upload photo to ICP canister
(icp/upload-photo "grainstore/photos/raw/IMG_0001.avif"
                  {:canister-id "rrkah-fqaaa-aaaaa-aaaaq-cai"
                   :metadata {:tags ["landscape" "sunset"]}})
```

### Nostr Integration

**Publish to Nostr:**
```clojure
(require '[clojure-photos.nostr :as nostr])

;; Publish photo to Nostr network
(nostr/publish-photo "grainstore/photos/raw/IMG_0001.avif"
                     {:relay "wss://relay.grain.network"
                      :metadata {:description "Beautiful sunset over Urbana"}})
```

### Urbit Integration

**Sync to Urbit Ship:**
```clojure
(require '[clojure-photos.urbit :as urbit])

;; Sync photo to Urbit ship
(urbit/sync-photo "grainstore/photos/raw/IMG_0001.avif"
                  {:ship "~zod"
                   :path "/photos/IMG_0001.avif"})
```

---

## 🔐 PRIVACY & SECURITY

### Local-First

All photos are stored **locally first** in the grainstore. Sync to cloud services (ICP, Nostr, Urbit) is **optional** and **user-controlled**.

### Encryption

**Encrypted Storage:**
```clojure
(require '[clojure-photos.crypto :as crypto])

;; Encrypt photo before upload
(crypto/encrypt-photo "grainstore/photos/raw/IMG_0001.avif"
                      {:key (crypto/generate-key)
                       :output "grainstore/photos/raw/IMG_0001.avif.enc"})
```

### Access Control

**Urbit-Based Permissions:**
```clojure
(require '[clojure-photos.permissions :as permissions])

;; Set photo permissions
(permissions/set-permissions "IMG_0001.avif"
                             {:public false
                              :shared-with ["~sampel-palnet" "~ravmel-ropdyl"]
                              :expiry "2025-12-31T23:59:59Z"})
```

---

## 📊 STORAGE QUOTAS

### Local Storage

**grainstore/photos/ quotas:**
- **raw/**: Unlimited (constrained by disk space)
- **processed/**: Unlimited
- **metadata/**: Minimal (JSON/EDN files)

### Cloud Storage

**ICP Canister Storage:**
- **Free Tier:** 1GB
- **Paid Tier:** Unlimited (pay-per-GB)
- **Cost:** ~$0.50/GB/year

**Nostr Relay Storage:**
- **Depends on relay** (most relays store indefinitely)
- **Large files:** Use Nostr file storage extensions (NIP-94)

**Urbit Ship Storage:**
- **Limited by ship tier** (galaxy/star/planet/moon)
- **Typical:** 1GB-100GB depending on ship

---

## 🛠️ TOOLS & LIBRARIES

### clojure-photos

**Main photo management library:**
```clojure
{:deps {clojure-photos {:git/url "https://github.com/grainpbc/clojure-photos"
                        :sha "abc123..."}}}
```

### Graincamera Integration

**Direct camera import:**
```bash
# Auto-sync when Graincamera is connected via USB-C
graincamera-sync --target grainstore/photos/raw/
```

### Grainwriter Integration

**Browse photos on e-ink display:**
```bash
# Launch Grainwriter photo viewer
grainwriter-photos --directory grainstore/photos/
```

---

## 🌟 VISION

The `grainstore/photos/` directory is the **central hub** for all photos in the Grain Hardware Ecosystem:

- **Graincamera** shoots photos in AVIF format
- **Auto-sync** to `grainstore/photos/raw/`
- **Process** with Clojure libraries
- **Sync** to ICP + Nostr + Urbit
- **Browse** on Grainwriter e-ink display
- **Share** with decentralized identity

**This is personal sovereignty in photography.** 📸

---

**Created:** January 22, 2025  
**Author:** kae3g (Graingalaxy)  
**License:** MIT

*"From camera to cloud, all under your control."*


