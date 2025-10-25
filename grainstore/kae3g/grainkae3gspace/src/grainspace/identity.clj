(ns grainspace.identity
  "Unified identity system integrating Urbit Azimuth and ICP subnet"
  (:require [clojure.java-time :as time]
            [clj-http.client :as http]
            [cheshire.core :as json]
            [clojure.string :as str]
            [crypto.random :as random]
            [clojure.core.async :as async :refer [go go-loop timeout <! >! <!! >!!]]))

;; Core identity configuration
(def identity-config
  {:urbit-azimuth
   {:ethereum-rpc "https://mainnet.infura.io/v3/YOUR_KEY"
    :azimuth-contract "0x223c067F8CF28ae173EE5CafEa60cA44c335fecB"
    :galaxy-count 256
    :star-count 65536
    :planet-count 4294967296}
   
   :icp-subnet
   {:subnet-id "rdmx6-jaaaa-aaaah-qcaiq-cai"
    :canister-id "u6s2n-gx777-77774-qaaba-cai"
    :chain-fusion-enabled true
    :solana-integration true}
   
   :grainspace
   {:unified-address-prefix "~grainspace-"
    :cross-platform-auth true
    :identity-verification true
    :social-integration true}})

;; Identity state management
(def identity-state
  (atom
   {:urbit-identity nil
    :icp-identity nil
    :unified-identity nil
    :verification-status :pending
    :social-connections {}
    :platform-access {}
    :last-sync (time/local-date-time)}))

;; Urbit Azimuth integration
(defn get-urbit-identity [address]
  "Get Urbit identity from Azimuth contract"
  (let [url (str (:ethereum-rpc (:urbit-azimuth identity-config)))
        body {:jsonrpc "2.0"
              :method "eth_call"
              :params [{:to (:azimuth-contract (:urbit-azimuth identity-config))
                        :data (str "0x" (format "%064x" address))}
                       "latest"]
              :id 1}]
    (try
      (let [response (http/post url
                               {:headers {"Content-Type" "application/json"}
                                :body (json/generate-string body)
                                :throw-exceptions false})]
        (if (= (:status response) 200)
          (let [data (json/parse-string (:body response) true)
                result (get data "result")]
            {:success true
             :address address
             :type (cond
                     (< address 256) :galaxy
                     (< address 65536) :star
                     :else :planet)
             :raw-data result})
          {:success false :error (:body response)}))
      (catch Exception e
        {:success false :error (.getMessage e)}))))

(defn validate-urbit-address [address]
  "Validate Urbit address format and ownership"
  (let [address-str (str address)
        valid-pattern #"^[0-9]+$"]
    (and (re-matches valid-pattern address-str)
         (let [addr-num (Long/parseLong address-str)]
           (< addr-num (:planet-count (:urbit-azimuth identity-config)))))))

;; ICP subnet integration
(defn get-icp-identity [principal-id]
  "Get ICP identity from subnet"
  (let [url (str "https://" (:subnet-id (:icp-subnet identity-config)) ".ic0.app/api/v1/identity")
        headers {"Content-Type" "application/json"}]
    (try
      (let [response (http/get url {:headers headers :throw-exceptions false})]
        (if (= (:status response) 200)
          (let [data (json/parse-string (:body response) true)]
            {:success true
             :principal-id principal-id
             :canister-id (:canister-id (:icp-subnet identity-config))
             :data data})
          {:success false :error (:body response)}))
      (catch Exception e
        {:success false :error (.getMessage e)}))))

(defn create-icp-identity [urbit-address]
  "Create ICP identity linked to Urbit address"
  (let [url (str "https://" (:subnet-id (:icp-subnet identity-config)) ".ic0.app/api/v1/identity/create")
        headers {"Content-Type" "application/json"}
        body {:urbit-address urbit-address
              :timestamp (time/format "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" (time/local-date-time))
              :verification-method "azimuth"}]
    (try
      (let [response (http/post url
                               {:headers headers
                                :body (json/generate-string body)
                                :throw-exceptions false})]
        (if (= (:status response) 200)
          (let [data (json/parse-string (:body response) true)]
            {:success true
             :urbit-address urbit-address
             :icp-principal (:principal-id data)
             :canister-id (:canister-id data)
             :data data})
          {:success false :error (:body response)}))
      (catch Exception e
        {:success false :error (.getMessage e)}))))

;; Unified identity system
(defn create-unified-identity [urbit-address]
  "Create unified identity linking Urbit and ICP"
  (let [urbit-identity (get-urbit-identity urbit-address)
        icp-identity (create-icp-identity urbit-address)]
    
    (if (and (:success urbit-identity) (:success icp-identity))
      (let [unified-id (str (:unified-address-prefix (:grainspace identity-config))
                            urbit-address
                            "-"
                            (:icp-principal icp-identity))]
        {:success true
         :unified-identity unified-id
         :urbit-identity urbit-identity
         :icp-identity icp-identity
         :created-at (time/local-date-time)
         :verification-status :verified})
      {:success false
       :urbit-error (:error urbit-identity)
       :icp-error (:error icp-identity)})))

(defn verify-unified-identity [unified-id]
  "Verify unified identity across platforms"
  (let [parts (str/split unified-id #"-")
        urbit-address (nth parts 1)
        icp-principal (nth parts 2)
        
        urbit-verify (get-urbit-identity urbit-address)
        icp-verify (get-icp-identity icp-principal)]
    
    (if (and (:success urbit-verify) (:success icp-verify))
      {:success true
       :verified-at (time/local-date-time)
       :urbit-status (:type urbit-verify)
       :icp-status (:canister-id icp-verify)}
      {:success false
       :urbit-error (:error urbit-verify)
       :icp-error (:error icp-verify)})))

;; Social platform integration
(defn connect-social-platform [platform unified-id]
  "Connect unified identity to social platform"
  (let [platform-config
        {:x {:api-url "https://api.twitter.com/2"
             :auth-method "oauth2"}
         :nostr {:relay-url "wss://relay.primal.net"
                 :auth-method "nostr-key"}
         :bluesky {:api-url "https://bsky.social"
                   :auth-method "at-protocol"}
         :threads {:api-url "https://www.threads.net"
                   :auth-method "meta-auth"}}]
    
    (let [config (get platform-config platform)]
      (if config
        {:success true
         :platform platform
         :unified-id unified-id
         :connected-at (time/local-date-time)
         :config config}
        {:success false :error (str "Unknown platform: " platform)}))))

(defn get-social-connections [unified-id]
  "Get all social platform connections for unified identity"
  (let [platforms [:x :nostr :bluesky :threads]
        connections (atom {})]
    
    (doseq [platform platforms]
      (let [connection (connect-social-platform platform unified-id)]
        (when (:success connection)
          (swap! connections assoc platform connection))))
    
    @connections))

;; Cross-platform authentication
(defn authenticate-platform [platform unified-id]
  "Authenticate with specific platform using unified identity"
  (let [connections (get-social-connections unified-id)
        platform-connection (get connections platform)]
    
    (if platform-connection
      {:success true
       :platform platform
       :unified-id unified-id
       :authenticated-at (time/local-date-time)
       :access-token (str "grainspace-" unified-id "-" platform)}
      {:success false :error (str "No connection found for platform: " platform)})))

;; Identity management functions
(defn register-identity [urbit-address]
  "Register new unified identity"
  (let [unified-identity (create-unified-identity urbit-address)]
    (if (:success unified-identity)
      (do
        (swap! identity-state assoc :unified-identity unified-identity)
        (swap! identity-state assoc :verification-status :verified)
        (swap! identity-state assoc :last-sync (time/local-date-time))
        unified-identity)
      unified-identity)))

(defn sync-identity [unified-id]
  "Sync identity across all platforms"
  (let [verification (verify-unified-identity unified-id)
        social-connections (get-social-connections unified-id)]
    
    (if (:success verification)
      (do
        (swap! identity-state assoc :verification-status :verified)
        (swap! identity-state assoc :social-connections social-connections)
        (swap! identity-state assoc :last-sync (time/local-date-time))
        {:success true
         :synced-at (time/local-date-time)
         :connections (count social-connections)})
      verification)))

(defn get-identity-status [unified-id]
  "Get current identity status"
  (let [verification (verify-unified-identity unified-id)
        social-connections (get-social-connections unified-id)]
    {:unified-id unified-id
     :verification verification
     :social-connections social-connections
     :last-sync (:last-sync @identity-state)
     :status (if (:success verification) :active :inactive)}))

;; Export functions for external use
(defn create-grainspace-identity [urbit-address]
  "Main function to create Grainspace identity"
  (println (str "🌐 Creating Grainspace identity for Urbit address: " urbit-address))
  (register-identity urbit-address))

(defn authenticate-with-platform [platform unified-id]
  "Main function to authenticate with platform"
  (println (str "🔐 Authenticating with " platform " using unified ID: " unified-id))
  (authenticate-platform platform unified-id))

(defn get-unified-status [unified-id]
  "Main function to get unified identity status"
  (println (str "📊 Getting status for unified ID: " unified-id))
  (get-identity-status unified-id))

