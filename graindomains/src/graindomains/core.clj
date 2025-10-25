(ns graindomains.core
  "Core domain management for grain6 ecosystem"
  (:require [clojure.string :as str]
            [clj-time.core :as time]
            [clj-time.format :as time-format]
            [clj-http.client :as http]
            [babashka.fs :as fs]))

(def grain6-domains
  "Core grain6 domains managed by kae3g"
  {:grain6 {:com "grain6.com"
            :org "grain6.org" 
            :net "grain6.net"}
   :grainsix {:com "grainsix.com"
              :org "grainsix.org"
              :net "grainsix.net"}})

(def domain-config
  "Configuration for domain management"
  {:provider "Squarespace"
   :renewal-period-days 730  ; 2 years
   :check-interval-hours 24
   :auto-renewal true
   :grain6os-integration true})

(defn get-domain-status
  "Get status information for a domain"
  [domain]
  (try
    (let [response (http/get (str "https://" domain)
                             {:throw-exceptions false
                              :timeout 5000})]
      {:domain domain
       :status (if (= 200 (:status response)) :active :inactive)
       :response-time (:request-time response)
       :checked-at (time/now)})
    (catch Exception e
      {:domain domain
       :status :error
       :error (.getMessage e)
       :checked-at (time/now)})))

(defn check-all-domains
  "Check status of all grain6 domains"
  []
  (let [all-domains (flatten (vals grain6-domains))
        results (map get-domain-status all-domains)]
    {:checked-at (time/now)
     :total-domains (count all-domains)
     :active-domains (count (filter #(= :active (:status %)) results))
     :inactive-domains (count (filter #(= :inactive (:status %)) results))
     :error-domains (count (filter #(= :error (:status %)) results))
     :domains results}))

(defn format-domain-report
  "Format domain status report"
  [report]
  (let [formatter (time-format/formatter "yyyy-MM-dd HH:mm:ss")]
    (str "🌾 Grain6 Domains Status Report\n"
         "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n"
         "Checked: " (time-format/unparse formatter (:checked-at report)) "\n"
         "Total Domains: " (:total-domains report) "\n"
         "Active: " (:active-domains report) " ✅\n"
         "Inactive: " (:inactive-domains report) " ❌\n"
         "Errors: " (:error-domains report) " ⚠️\n\n"
         "Domain Details:\n"
         (str/join "\n" 
                   (map (fn [domain]
                          (str "  " (:domain domain) 
                               " - " (name (:status domain))
                               (when (:response-time domain)
                                 (str " (" (:response-time domain) "ms)"))))
                        (:domains report))))))

(defn get-renewal-dates
  "Get renewal dates for domains (from Squarespace data)"
  []
  {:grain6.com "2027-10-23"
   :grain6.org "2027-10-23"
   :grain6.net "2027-10-23"
   :grainsix.com "2027-10-23"
   :grainsix.org "2027-10-23"
   :grainsix.net "2027-10-23"})

(defn days-until-renewal
  "Calculate days until domain renewal"
  [domain]
  (let [renewal-dates (get-renewal-dates)
        renewal-date-str (get renewal-dates (keyword domain))
        renewal-date (time-format/parse (time-format/formatter "yyyy-MM-dd") renewal-date-str)
        now (time/now)]
    (time/in-days (time/interval now renewal-date))))

(defn renewal-status
  "Get renewal status for all domains"
  []
  (let [all-domains (flatten (vals grain6-domains))
        renewal-data (map (fn [domain]
                            {:domain domain
                             :days-until-renewal (days-until-renewal domain)
                             :renewal-date (get (get-renewal-dates) (keyword domain))})
                          all-domains)]
    {:checked-at (time/now)
     :domains renewal-data}))

(defn format-renewal-report
  "Format renewal status report"
  [report]
  (let [formatter (time-format/formatter "yyyy-MM-dd HH:mm:ss")]
    (str "🌾 Grain6 Domains Renewal Report\n"
         "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n"
         "Checked: " (time-format/unparse formatter (:checked-at report)) "\n\n"
         "Renewal Status:\n"
         (str/join "\n"
                   (map (fn [domain]
                          (str "  " (:domain domain)
                               " - " (:days-until-renewal domain) " days until renewal"
                               " (expires " (:renewal-date domain) ")"))
                        (:domains report))))))
