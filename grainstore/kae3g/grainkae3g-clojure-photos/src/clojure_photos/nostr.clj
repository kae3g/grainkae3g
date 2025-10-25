(ns clojure-photos.nostr
  "Nostr integration for photo sharing and storage.
   Implements NIP-94 (File Metadata) and NIP-95 (File Storage)."
  (:require [clojure.java.io :as io]
            [clojure.core.async :as async]
            [clojure.tools.logging :as log]
            [clj-http.client :as http]
            [cheshire.core :as json]
            [clojure.string :as str])
  (:import [java.security MessageDigest]
           [java.util Base64]))

;;; Nostr Event Types
(def EVENT_KIND_FILE_METADATA 1063) ; NIP-94
(def EVENT_KIND_FILE_STORAGE 20)    ; NIP-95 (using kind 20 for now)

;;; Utility Functions

(defn sha256
  "Calculate SHA-256 hash of data."
  [data]
  (let [digest (MessageDigest/getInstance "SHA-256")
        bytes (if (string? data)
                (.getBytes data "UTF-8")
                data)
        hash (.digest digest bytes)]
    (apply str (map #(format "%02x" %) hash))))

(defn base64-encode
  "Encode bytes to base64 string."
  [bytes]
  (.encodeToString (Base64/getEncoder) bytes))

(defn base64-decode
  "Decode base64 string to bytes."
  [s]
  (.decode (Base64/getDecoder) s))

(defn generate-keypair
  "Generate a Nostr keypair (private key + public key).
   Returns {:private-key \"...\" :public-key \"...\"}
   
   Note: This is a simplified implementation.
   In production, use proper secp256k1 key generation."
  []
  ;; TODO: Implement proper secp256k1 key generation
  ;; For now, return placeholder
  {:private-key "placeholder-private-key"
   :public-key "placeholder-public-key"})

(defn sign-event
  "Sign a Nostr event with private key.
   Returns signature string.
   
   Note: This is a simplified implementation.
   In production, use proper Schnorr signature."
  [event private-key]
  ;; TODO: Implement proper Schnorr signature
  ;; For now, return placeholder
  "placeholder-signature")

;;; Nostr Event Creation

(defn create-file-metadata-event
  "Create a NIP-94 file metadata event for a photo.
   
   Options:
   :url - URL where file is hosted (required)
   :sha256 - SHA-256 hash of file (required)
   :size - File size in bytes (required)
   :mime-type - MIME type (default 'image/avif')
   :description - Photo description (optional)
   :tags - Additional tags (optional)
   :dimensions - {:width ... :height ...} (optional)"
  [photo & {:keys [url sha256 size mime-type description tags dimensions]
            :or {mime-type "image/avif"}}]
  (let [base-tags [["url" url]
                   ["sha256" sha256]
                   ["size" (str size)]
                   ["m" mime-type]]
        dim-tags (when dimensions
                   [["dim" (str (:width dimensions) "x" (:height dimensions))]])
        extra-tags (map #(vector "t" %) (or tags []))
        all-tags (concat base-tags dim-tags extra-tags)]
    {:kind EVENT_KIND_FILE_METADATA
     :created_at (quot (System/currentTimeMillis) 1000)
     :tags all-tags
     :content (or description "")}))

(defn create-file-storage-event
  "Create a NIP-95 file storage event for a photo.
   Embeds the photo data directly in the event.
   
   Note: Only suitable for small files (<100KB).
   For larger files, use external storage + metadata event."
  [photo-bytes & {:keys [mime-type description tags]
                  :or {mime-type "image/avif"}}]
  (let [base64-data (base64-encode photo-bytes)
        file-hash (sha256 photo-bytes)
        base-tags [["m" mime-type]
                   ["x" file-hash]
                   ["size" (str (alength photo-bytes))]]
        extra-tags (map #(vector "t" %) (or tags []))]
    {:kind EVENT_KIND_FILE_STORAGE
     :created_at (quot (System/currentTimeMillis) 1000)
     :tags (concat base-tags extra-tags)
     :content base64-data}))

;;; Nostr Relay Communication

(defn publish-event
  "Publish a Nostr event to a relay.
   
   Options:
   :relay - Relay URL (required, e.g. 'wss://relay.grain.network')
   :event - Nostr event map (required)
   :private-key - Private key for signing (required)
   
   Returns event ID if successful."
  [& {:keys [relay event private-key]}]
  (log/info "Publishing event to relay:" relay)
  (let [event-id (sha256 (json/generate-string event))
        signature (sign-event event private-key)
        signed-event (assoc event
                           :id event-id
                           :sig signature
                           :pubkey (:public-key (generate-keypair)))]
    ;; TODO: Implement actual WebSocket connection to relay
    ;; For now, log the event
    (log/info "Would publish event:" signed-event)
    event-id))

(defn query-events
  "Query events from a Nostr relay.
   
   Options:
   :relay - Relay URL (required)
   :filters - Query filters (required)
   
   Example filters:
   {:kinds [1063] :authors [\"pubkey...\"] :limit 10}
   
   Returns a lazy sequence of events."
  [& {:keys [relay filters]}]
  (log/info "Querying events from relay:" relay "filters:" filters)
  ;; TODO: Implement actual WebSocket connection to relay
  ;; For now, return empty sequence
  [])

;;; High-Level Photo Operations

(defn publish-photo
  "Publish a photo to Nostr network.
   
   Strategy:
   1. Upload photo to external storage (e.g., ICP canister)
   2. Create NIP-94 metadata event
   3. Publish event to Nostr relays
   
   Options:
   :photo - Photo data (BufferedImage or file path) (required)
   :relay - Nostr relay URL (required)
   :private-key - Private key for signing (required)
   :storage-url - URL where photo is stored (required)
   :description - Photo description (optional)
   :tags - Photo tags (optional)
   :dimensions - {:width ... :height ...} (optional)
   
   Returns event ID."
  [& {:keys [photo relay private-key storage-url description tags dimensions]}]
  (log/info "Publishing photo to Nostr:" storage-url)
  (let [photo-bytes (cond
                      (string? photo) (.readAllBytes (io/input-stream photo))
                      :else (throw (ex-info "Unsupported photo type" {:photo photo})))
        file-hash (sha256 photo-bytes)
        file-size (alength photo-bytes)
        event (create-file-metadata-event
               nil
               :url storage-url
               :sha256 file-hash
               :size file-size
               :description description
               :tags tags
               :dimensions dimensions)]
    (publish-event :relay relay
                   :event event
                   :private-key private-key)))

(defn publish-photo-embedded
  "Publish a small photo directly embedded in a Nostr event.
   Only suitable for photos <100KB.
   
   Options:
   :photo - Photo data (file path or bytes) (required)
   :relay - Nostr relay URL (required)
   :private-key - Private key for signing (required)
   :description - Photo description (optional)
   :tags - Photo tags (optional)
   
   Returns event ID."
  [& {:keys [photo relay private-key description tags]}]
  (log/info "Publishing embedded photo to Nostr")
  (let [photo-bytes (cond
                      (string? photo) (.readAllBytes (io/input-stream photo))
                      (bytes? photo) photo
                      :else (throw (ex-info "Unsupported photo type" {:photo photo})))
        file-size (alength photo-bytes)]
    (when (> file-size 100000)
      (throw (ex-info "Photo too large for embedding" {:size file-size :max 100000})))
    (let [event (create-file-storage-event photo-bytes
                                          :description description
                                          :tags tags)]
      (publish-event :relay relay
                     :event event
                     :private-key private-key))))

(defn fetch-photos
  "Fetch photos from a Nostr relay.
   
   Options:
   :relay - Nostr relay URL (required)
   :author - Public key of author (optional)
   :tags - Tags to filter by (optional)
   :limit - Maximum number of photos to fetch (default 50)
   
   Returns a lazy sequence of photo metadata maps."
  [& {:keys [relay author tags limit]
      :or {limit 50}}]
  (log/info "Fetching photos from Nostr relay:" relay)
  (let [filters {:kinds [EVENT_KIND_FILE_METADATA]
                 :limit limit}
        filters (if author (assoc filters :authors [author]) filters)
        filters (if tags (assoc filters :tags tags) filters)
        events (query-events :relay relay :filters filters)]
    (map (fn [event]
           (let [tags-map (into {} (map (fn [[k v]] [k v]) (:tags event)))
                 [width height] (when-let [dim (get tags-map "dim")]
                                 (map #(Integer/parseInt %) (str/split dim #"x")))]
             {:event-id (:id event)
              :url (get tags-map "url")
              :sha256 (get tags-map "sha256")
              :size (Integer/parseInt (get tags-map "size" "0"))
              :mime-type (get tags-map "m")
              :description (:content event)
              :dimensions (when (and width height)
                           {:width width :height height})
              :tags (map second (filter #(= (first %) "t") (:tags event)))
              :created-at (:created_at event)
              :author (:pubkey event)}))
         events)))

(defn download-photo
  "Download a photo from a Nostr event.
   
   Options:
   :event - Nostr event with photo metadata (required)
   :target-path - Where to save the photo (required)
   
   Returns target path if successful."
  [& {:keys [event target-path]}]
  (log/info "Downloading photo from event:" (:event-id event))
  (if-let [url (:url event)]
    (do
      (with-open [in (io/input-stream url)
                  out (io/output-stream target-path)]
        (io/copy in out))
      target-path)
    (throw (ex-info "Event has no URL" {:event event}))))

;;; Grain Network Integration

(defn publish-to-grain-network
  "Publish a photo to the Grain Network Nostr relay.
   
   Options:
   :photo - Photo data (file path or BufferedImage) (required)
   :private-key - Private key for signing (required)
   :storage-url - URL where photo is stored (e.g., ICP canister) (required)
   :metadata - Photo metadata map (optional)
   
   Returns event ID."
  [& {:keys [photo private-key storage-url metadata]}]
  (let [relay "wss://relay.grain.network"
        description (:description metadata)
        tags (:tags metadata)
        dimensions (:dimensions metadata)]
    (publish-photo :photo photo
                   :relay relay
                   :private-key private-key
                   :storage-url storage-url
                   :description description
                   :tags tags
                   :dimensions dimensions)))

(defn fetch-from-grain-network
  "Fetch photos from the Grain Network Nostr relay.
   
   Options:
   :author - Public key of author (optional)
   :tags - Tags to filter by (optional)
   :limit - Maximum number of photos (default 50)
   
   Returns a lazy sequence of photo metadata maps."
  [& {:keys [author tags limit]
      :or {limit 50}}]
  (let [relay "wss://relay.grain.network"]
    (fetch-photos :relay relay
                  :author author
                  :tags tags
                  :limit limit)))


