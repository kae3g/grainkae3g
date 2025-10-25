(ns clojure-photos.icp
  "ICP (Internet Computer Protocol) integration for photo storage.
   Stores photos in ICP canisters with decentralized storage."
  (:require [clojure.java.io :as io]
            [clojure.core.async :as async]
            [clojure.tools.logging :as log]
            [clj-http.client :as http]
            [cheshire.core :as json])
  (:import [java.util Base64]))

;;; ICP Canister Configuration

(def DEFAULT_CANISTER_ID "rrkah-fqaaa-aaaaa-aaaaq-cai")
(def DEFAULT_IC_HOST "https://ic0.app")
(def DEFAULT_LOCAL_HOST "http://localhost:4943")

;;; Utility Functions

(defn base64-encode
  "Encode bytes to base64 string."
  [bytes]
  (.encodeToString (Base64/getEncoder) bytes))

(defn base64-decode
  "Decode base64 string to bytes."
  [s]
  (.decode (Base64/getDecoder) s))

(defn bytes-to-nat8-array
  "Convert byte array to Candid nat8 array format."
  [bytes]
  (vec bytes))

(defn nat8-array-to-bytes
  "Convert Candid nat8 array to byte array."
  [nat8-array]
  (byte-array nat8-array))

;;; ICP Agent Calls

(defn call-canister
  "Call an ICP canister method.
   
   Options:
   :canister-id - Canister ID (required)
   :method - Method name (required)
   :args - Method arguments in Candid format (required)
   :ic-host - IC host URL (default 'https://ic0.app')
   :local? - Use local replica (default false)
   
   Returns response data."
  [& {:keys [canister-id method args ic-host local?]
      :or {ic-host DEFAULT_IC_HOST
           local? false}}]
  (log/info "Calling canister:" canister-id "method:" method)
  (let [host (if local? DEFAULT_LOCAL_HOST ic-host)
        url (str host "/api/v2/canister/" canister-id "/call")
        request-body {:method method
                      :args args}]
    (try
      (let [response (http/post url
                               {:body (json/generate-string request-body)
                                :headers {"Content-Type" "application/json"}
                                :as :json})]
        (log/info "Canister call successful")
        (:body response))
      (catch Exception e
        (log/error e "Canister call failed")
        (throw (ex-info "ICP canister call failed"
                       {:canister-id canister-id
                        :method method
                        :error (.getMessage e)}))))))

(defn query-canister
  "Query an ICP canister method (read-only).
   
   Options:
   :canister-id - Canister ID (required)
   :method - Method name (required)
   :args - Method arguments in Candid format (required)
   :ic-host - IC host URL (default 'https://ic0.app')
   :local? - Use local replica (default false)
   
   Returns query result."
  [& {:keys [canister-id method args ic-host local?]
      :or {ic-host DEFAULT_IC_HOST
           local? false}}]
  (log/info "Querying canister:" canister-id "method:" method)
  (let [host (if local? DEFAULT_LOCAL_HOST ic-host)
        url (str host "/api/v2/canister/" canister-id "/query")
        request-body {:method method
                      :args args}]
    (try
      (let [response (http/post url
                               {:body (json/generate-string request-body)
                                :headers {"Content-Type" "application/json"}
                                :as :json})]
        (log/info "Canister query successful")
        (:body response))
      (catch Exception e
        (log/error e "Canister query failed")
        (throw (ex-info "ICP canister query failed"
                       {:canister-id canister-id
                        :method method
                        :error (.getMessage e)}))))))

;;; Photo Storage Operations

(defn upload-photo
  "Upload a photo to an ICP canister.
   
   Options:
   :photo - Photo data (file path or byte array) (required)
   :canister-id - Canister ID (default DEFAULT_CANISTER_ID)
   :metadata - Photo metadata map (optional)
   :ic-host - IC host URL (default 'https://ic0.app')
   :local? - Use local replica (default false)
   
   Returns:
   {:photo-id \"...\"
    :url \"...\"
    :size ...}"
  [& {:keys [photo canister-id metadata ic-host local?]
      :or {canister-id DEFAULT_CANISTER_ID
           ic-host DEFAULT_IC_HOST
           local? false}}]
  (log/info "Uploading photo to ICP canister:" canister-id)
  (let [photo-bytes (cond
                      (string? photo) (.readAllBytes (io/input-stream photo))
                      (bytes? photo) photo
                      :else (throw (ex-info "Unsupported photo type" {:photo photo})))
        photo-nat8 (bytes-to-nat8-array photo-bytes)
        args {:photo_data photo-nat8
              :metadata (or metadata {})}
        response (call-canister :canister-id canister-id
                               :method "upload_photo"
                               :args args
                               :ic-host ic-host
                               :local? local?)]
    {:photo-id (:photo_id response)
     :url (str (if local? DEFAULT_LOCAL_HOST ic-host) 
               "/photos/" canister-id "/" (:photo_id response))
     :size (alength photo-bytes)}))

(defn download-photo
  "Download a photo from an ICP canister.
   
   Options:
   :photo-id - Photo ID (required)
   :canister-id - Canister ID (default DEFAULT_CANISTER_ID)
   :target-path - Where to save the photo (optional)
   :ic-host - IC host URL (default 'https://ic0.app')
   :local? - Use local replica (default false)
   
   Returns byte array or saves to target-path."
  [& {:keys [photo-id canister-id target-path ic-host local?]
      :or {canister-id DEFAULT_CANISTER_ID
           ic-host DEFAULT_IC_HOST
           local? false}}]
  (log/info "Downloading photo from ICP canister:" canister-id "photo-id:" photo-id)
  (let [args {:photo_id photo-id}
        response (query-canister :canister-id canister-id
                                :method "get_photo"
                                :args args
                                :ic-host ic-host
                                :local? local?)
        photo-nat8 (:photo_data response)
        photo-bytes (nat8-array-to-bytes photo-nat8)]
    (if target-path
      (do
        (with-open [out (io/output-stream target-path)]
          (.write out photo-bytes))
        target-path)
      photo-bytes)))

(defn list-photos
  "List all photos in an ICP canister.
   
   Options:
   :canister-id - Canister ID (default DEFAULT_CANISTER_ID)
   :offset - Pagination offset (default 0)
   :limit - Maximum number of photos (default 50)
   :ic-host - IC host URL (default 'https://ic0.app')
   :local? - Use local replica (default false)
   
   Returns a lazy sequence of photo metadata maps."
  [& {:keys [canister-id offset limit ic-host local?]
      :or {canister-id DEFAULT_CANISTER_ID
           offset 0
           limit 50
           ic-host DEFAULT_IC_HOST
           local? false}}]
  (log/info "Listing photos from ICP canister:" canister-id)
  (let [args {:offset offset
              :limit limit}
        response (query-canister :canister-id canister-id
                                :method "list_photos"
                                :args args
                                :ic-host ic-host
                                :local? local?)]
    (:photos response)))

(defn delete-photo
  "Delete a photo from an ICP canister.
   
   Options:
   :photo-id - Photo ID (required)
   :canister-id - Canister ID (default DEFAULT_CANISTER_ID)
   :ic-host - IC host URL (default 'https://ic0.app')
   :local? - Use local replica (default false)
   
   Returns true if successful."
  [& {:keys [photo-id canister-id ic-host local?]
      :or {canister-id DEFAULT_CANISTER_ID
           ic-host DEFAULT_IC_HOST
           local? false}}]
  (log/info "Deleting photo from ICP canister:" canister-id "photo-id:" photo-id)
  (let [args {:photo_id photo-id}
        response (call-canister :canister-id canister-id
                               :method "delete_photo"
                               :args args
                               :ic-host ic-host
                               :local? local?)]
    (:success response)))

(defn update-photo-metadata
  "Update metadata for a photo in an ICP canister.
   
   Options:
   :photo-id - Photo ID (required)
   :metadata - New metadata map (required)
   :canister-id - Canister ID (default DEFAULT_CANISTER_ID)
   :ic-host - IC host URL (default 'https://ic0.app')
   :local? - Use local replica (default false)
   
   Returns true if successful."
  [& {:keys [photo-id metadata canister-id ic-host local?]
      :or {canister-id DEFAULT_CANISTER_ID
           ic-host DEFAULT_IC_HOST
           local? false}}]
  (log/info "Updating photo metadata in ICP canister:" canister-id "photo-id:" photo-id)
  (let [args {:photo_id photo-id
              :metadata metadata}
        response (call-canister :canister-id canister-id
                               :method "update_photo_metadata"
                               :args args
                               :ic-host ic-host
                               :local? local?)]
    (:success response)))

;;; Batch Operations

(defn upload-photo-collection
  "Upload a collection of photos to an ICP canister.
   
   Options:
   :photos - Collection of photo file paths (required)
   :canister-id - Canister ID (default DEFAULT_CANISTER_ID)
   :ic-host - IC host URL (default 'https://ic0.app')
   :local? - Use local replica (default false)
   :on-progress - Callback function (fn [current total]) (optional)
   
   Returns a sequence of upload results."
  [& {:keys [photos canister-id ic-host local? on-progress]
      :or {canister-id DEFAULT_CANISTER_ID
           ic-host DEFAULT_IC_HOST
           local? false}}]
  (log/info "Uploading photo collection to ICP canister:" canister-id)
  (let [total (count photos)]
    (map-indexed
     (fn [idx photo-path]
       (when on-progress
         (on-progress (inc idx) total))
       (let [result (upload-photo :photo photo-path
                                 :canister-id canister-id
                                 :ic-host ic-host
                                 :local? local?)]
         (log/info "Uploaded" (inc idx) "/" total ":" photo-path)
         (assoc result :original-path photo-path)))
     photos)))

(defn download-photo-collection
  "Download all photos from an ICP canister.
   
   Options:
   :canister-id - Canister ID (default DEFAULT_CANISTER_ID)
   :target-dir - Directory to save photos (required)
   :ic-host - IC host URL (default 'https://ic0.app')
   :local? - Use local replica (default false)
   :on-progress - Callback function (fn [current total]) (optional)
   
   Returns a sequence of downloaded file paths."
  [& {:keys [canister-id target-dir ic-host local? on-progress]
      :or {canister-id DEFAULT_CANISTER_ID
           ic-host DEFAULT_IC_HOST
           local? false}}]
  (log/info "Downloading photo collection from ICP canister:" canister-id)
  (let [photos (list-photos :canister-id canister-id
                           :ic-host ic-host
                           :local? local?)
        total (count photos)]
    (map-indexed
     (fn [idx photo-metadata]
       (when on-progress
         (on-progress (inc idx) total))
       (let [photo-id (:photo_id photo-metadata)
             filename (str photo-id ".avif")
             target-path (str target-dir "/" filename)
             result (download-photo :photo-id photo-id
                                   :canister-id canister-id
                                   :target-path target-path
                                   :ic-host ic-host
                                   :local? local?)]
         (log/info "Downloaded" (inc idx) "/" total ":" photo-id)
         result))
     photos)))

;;; Grain Network Integration

(defn upload-to-grain-network
  "Upload a photo to the Grain Network ICP canister.
   
   Options:
   :photo - Photo data (file path or byte array) (required)
   :metadata - Photo metadata map (optional)
   :local? - Use local replica for testing (default false)
   
   Returns upload result with photo URL."
  [& {:keys [photo metadata local?]
      :or {local? false}}]
  (let [canister-id DEFAULT_CANISTER_ID
        ic-host DEFAULT_IC_HOST]
    (upload-photo :photo photo
                  :canister-id canister-id
                  :metadata metadata
                  :ic-host ic-host
                  :local? local?)))

(defn download-from-grain-network
  "Download a photo from the Grain Network ICP canister.
   
   Options:
   :photo-id - Photo ID (required)
   :target-path - Where to save the photo (optional)
   :local? - Use local replica for testing (default false)
   
   Returns byte array or saves to target-path."
  [& {:keys [photo-id target-path local?]
      :or {local? false}}]
  (let [canister-id DEFAULT_CANISTER_ID
        ic-host DEFAULT_IC_HOST]
    (download-photo :photo-id photo-id
                    :canister-id canister-id
                    :target-path target-path
                    :ic-host ic-host
                    :local? local?)))

(defn list-grain-network-photos
  "List all photos in the Grain Network ICP canister.
   
   Options:
   :offset - Pagination offset (default 0)
   :limit - Maximum number of photos (default 50)
   :local? - Use local replica for testing (default false)
   
   Returns a lazy sequence of photo metadata maps."
  [& {:keys [offset limit local?]
      :or {offset 0
           limit 50
           local? false}}]
  (let [canister-id DEFAULT_CANISTER_ID
        ic-host DEFAULT_IC_HOST]
    (list-photos :canister-id canister-id
                 :offset offset
                 :limit limit
                 :ic-host ic-host
                 :local? local?)))

;;; Storage Statistics

(defn get-storage-stats
  "Get storage statistics for an ICP canister.
   
   Options:
   :canister-id - Canister ID (default DEFAULT_CANISTER_ID)
   :ic-host - IC host URL (default 'https://ic0.app')
   :local? - Use local replica (default false)
   
   Returns:
   {:total-photos ...
    :total-size-bytes ...
    :available-space-bytes ...
    :used-space-bytes ...}"
  [& {:keys [canister-id ic-host local?]
      :or {canister-id DEFAULT_CANISTER_ID
           ic-host DEFAULT_IC_HOST
           local? false}}]
  (log/info "Getting storage stats from ICP canister:" canister-id)
  (let [response (query-canister :canister-id canister-id
                                :method "get_storage_stats"
                                :args {}
                                :ic-host ic-host
                                :local? local?)]
    response))


