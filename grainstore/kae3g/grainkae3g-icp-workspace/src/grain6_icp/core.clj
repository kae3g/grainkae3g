(ns grain6-icp.core
  "Core Grain6 ICP canister functionality.
   
   This module provides the main Grain6 service as an ICP canister,
   enabling decentralized hosting of Grain Network functionality."
  (:require [clojure.string :as str]
            [clojure.data.json :as json]))

;; ═══════════════════════════════════════════════════════════
;; ICP CANISTER INTERFACE
;; ═══════════════════════════════════════════════════════════

(defn query-handler
  "Handle ICP query calls for Grain6 service"
  [args]
  (let [method (get args "method")
        params (get args "params")]
    (case method
      "get-graintime" (get-graintime params)
      "get-grainpath" (get-grainpath params)
      "get-graincontacts" (get-graincontacts params)
      "get-graincourse" (get-graincourse params)
      "get-grainbook" (get-grainbook params)
      {:error "Unknown method" :method method})))

(defn update-handler
  "Handle ICP update calls for Grain6 service"
  [args]
  (let [method (get args "method")
        params (get args "params")]
    (case method
      "update-graintime" (update-graintime params)
      "update-grainpath" (update-grainpath params)
      "update-graincontacts" (update-graincontacts params)
      "update-graincourse" (update-graincourse params)
      "update-grainbook" (update-grainbook params)
      {:error "Unknown method" :method method})))

;; ═══════════════════════════════════════════════════════════
;; GRAINTIME SERVICE
;; ═══════════════════════════════════════════════════════════

(defn get-graintime
  "Get current graintime timestamp"
  [params]
  (let [author (get params "author" "kae3g")
        timestamp (java.time.Instant/now)
        graintime (str "12025-10-24--" 
                      (.format (java.time.format.DateTimeFormatter/ofPattern "HHmm") 
                               (java.time.ZonedDateTime/ofInstant timestamp 
                                                                  (java.time.ZoneId/of "America/Los_Angeles")))
                      "--PDT--moon-vishakha--asc-gem000--sun-11th--" author)]
    {:graintime graintime
     :timestamp (.toString timestamp)
     :author author}))

(defn update-graintime
  "Update graintime configuration"
  [params]
  {:status "success"
   :message "Graintime configuration updated"
   :params params})

;; ═══════════════════════════════════════════════════════════
;; GRAINPATH SERVICE
;; ═══════════════════════════════════════════════════════════

(defn get-grainpath
  "Get current grainpath navigation"
  [params]
  (let [current-path (get params "path" "grain6-get-started")
        graintime (get-graintime params)]
    {:grainpath current-path
     :graintime (:graintime graintime)
     :navigation {:previous nil
                  :current current-path
                  :next nil}}))

(defn update-grainpath
  "Update grainpath navigation"
  [params]
  {:status "success"
   :message "Grainpath navigation updated"
   :params params})

;; ═══════════════════════════════════════════════════════════
;; GRAINCONTACTS SERVICE
;; ═══════════════════════════════════════════════════════════

(defn get-graincontacts
  "Get grain contacts identity information"
  [params]
  (let [username (get params "username" "kae3g")]
    {:username username
     :platforms {:github "kae3g"
                 :codeberg "kae3g"
                 :grain6pbc "kae3g"}
     :verified true
     :last-updated (.toString (java.time.Instant/now))}))

(defn update-graincontacts
  "Update grain contacts identity"
  [params]
  {:status "success"
   :message "Grain contacts identity updated"
   :params params})

;; ═══════════════════════════════════════════════════════════
;; GRAINCOURSE SERVICE
;; ═══════════════════════════════════════════════════════════

(defn get-graincourse
  "Get graincourse educational content"
  [params]
  (let [course-id (get params "course-id" "grain6-get-started")
        lesson-id (get params "lesson-id" "introduction")]
    {:course-id course-id
     :lesson-id lesson-id
     :content "Welcome to Grain6 - the decentralized Grain Network"
     :progress {:completed 0
                :total 10}
     :next-lesson "setup"}))

(defn update-graincourse
  "Update graincourse progress"
  [params]
  {:status "success"
   :message "Graincourse progress updated"
   :params params})

;; ═══════════════════════════════════════════════════════════
;; GRAINBOOK SERVICE
;; ═══════════════════════════════════════════════════════════

(defn get-grainbook
  "Get grainbook personal knowledge"
  [params]
  (let [book-id (get params "book-id" "personal-notes")
        page-id (get params "page-id" "current")]
    {:book-id book-id
     :page-id page-id
     :content "Personal knowledge and notes"
     :tags ["grain6" "icp" "clotoko"]
     :created-at (.toString (java.time.Instant/now))}))

(defn update-grainbook
  "Update grainbook content"
  [params]
  {:status "success"
   :message "Grainbook content updated"
   :params params})

;; ═══════════════════════════════════════════════════════════
;; CANISTER INITIALIZATION
;; ═══════════════════════════════════════════════════════════

(defn canister-init
  "Initialize the Grain6 ICP canister"
  []
  {:status "initialized"
   :service "grain6-icp"
   :version "0.1.0"
   :timestamp (.toString (java.time.Instant/now))})

(defn canister-query
  "Main query entry point for ICP canister"
  [args]
  (try
    (query-handler args)
    (catch Exception e
      {:error "Query failed"
       :message (.getMessage e)
       :timestamp (.toString (java.time.Instant/now))})))

(defn canister-update
  "Main update entry point for ICP canister"
  [args]
  (try
    (update-handler args)
    (catch Exception e
      {:error "Update failed"
       :message (.getMessage e)
       :timestamp (.toString (java.time.Instant/now))})))

;; ═══════════════════════════════════════════════════════════
;; EXPORTED FUNCTIONS
;; ═══════════════════════════════════════════════════════════

;; These functions will be exported to the ICP canister interface
(def exported-functions
  {:canister-init canister-init
   :canister-query canister-query
   :canister-update canister-update
   :get-graintime get-graintime
   :get-grainpath get-grainpath
   :get-graincontacts get-graincontacts
   :get-graincourse get-graincourse
   :get-grainbook get-grainbook})
