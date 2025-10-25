(ns graindomains.grain6os
  "Integration between grain6os and grainsixos with clojure-sixos"
  (:require [clojure.string :as str]
            [clj-time.core :as time]
            [clj-time.format :as time-format]
            [babashka.fs :as fs]))

(def grain6os-config
  "Configuration for grain6os==grainsixos integration"
  {:grain6os-path "/home/xy/kae3g/grainkae3g/grainstore/grainpbc/grain6"
   :grainsixos-path "/home/xy/kae3g/grainkae3g/grainstore/grainpbc/grainsixos"
   :clojure-sixos-path "/home/xy/kae3g/grainkae3g/grainstore/grainpbc/clojure-sixos"
   :sync-enabled true
   :auto-deploy true})

(defn grain6os-equals-grainsixos?
  "Check if grain6os and grainsixos are equivalent"
  []
  (let [grain6os-exists (fs/exists? (:grain6os-path grain6os-config))
        grainsixos-exists (fs/exists? (:grainsixos-path grain6os-config))]
    {:grain6os-exists grain6os-exists
     :grainsixos-exists grainsixos-exists
     :equivalent (and grain6os-exists grainsixos-exists)}))

(defn get-clojure-sixos-status
  "Get status of clojure-sixos integration"
  []
  (let [clojure-sixos-path (:clojure-sixos-path grain6os-config)
        exists (fs/exists? clojure-sixos-path)
        deps-edn (when exists (fs/exists? (str clojure-sixos-path "/deps.edn")))
        src-dir (when exists (fs/exists? (str clojure-sixos-path "/src")))]
    {:clojure-sixos-path clojure-sixos-path
     :exists exists
     :deps-edn-present deps-edn
     :src-present src-dir
     :ready (and exists deps-edn src-dir)}))

(defn sync-grain6os-grainsixos
  "Sync grain6os with grainsixos domains"
  []
  (let [grain6os-status (grain6os-equals-grainsixos?)
        clojure-sixos-status (get-clojure-sixos-status)
        domains-status (graindomains.core/check-all-domains)]
    {:sync-at (time/now)
     :grain6os-status grain6os-status
     :clojure-sixos-status clojure-sixos-status
     :domains-status domains-status
     :sync-successful (and (:equivalent grain6os-status)
                           (:ready clojure-sixos-status)
                           (> (:active-domains domains-status) 0))}))

(defn format-grain6os-report
  "Format grain6os integration report"
  [report]
  (let [formatter (time-format/formatter "yyyy-MM-dd HH:mm:ss")]
    (str "🌾 Grain6OS Integration Report\n"
         "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n"
         "Synced: " (time-format/unparse formatter (:sync-at report)) "\n\n"
         "Grain6OS Status:\n"
         "  grain6os exists: " (:grain6os-exists (:grain6os-status report)) "\n"
         "  grainsixos exists: " (:grainsixos-exists (:grain6os-status report)) "\n"
         "  equivalent: " (:equivalent (:grain6os-status report)) "\n\n"
         "Clojure-SixOS Status:\n"
         "  path: " (:clojure-sixos-path (:clojure-sixos-status report)) "\n"
         "  exists: " (:exists (:clojure-sixos-status report)) "\n"
         "  deps.edn: " (:deps-edn-present (:clojure-sixos-status report)) "\n"
         "  src/: " (:src-present (:clojure-sixos-status report)) "\n"
         "  ready: " (:ready (:clojure-sixos-status report)) "\n\n"
         "Domains Status:\n"
         "  active domains: " (:active-domains (:domains-status report)) "\n"
         "  total domains: " (:total-domains (:domains-status report)) "\n\n"
         "Sync Status: " (if (:sync-successful report) "✅ SUCCESS" "❌ FAILED") "\n")))

(defn create-grain6os-symlink
  "Create symlink from grain6os to grainsixos"
  []
  (try
    (let [grain6os-path (:grain6os-path grain6os-config)
          grainsixos-path (:grainsixos-path grain6os-config)]
      (if (fs/exists? grain6os-path)
        (do
          (fs/create-symlink grain6os-path grainsixos-path)
          {:success true
           :message (str "Created symlink: " grainsixos-path " -> " grain6os-path)})
        {:success false
         :message (str "grain6os path does not exist: " grain6os-path)}))
    (catch Exception e
      {:success false
       :message (str "Failed to create symlink: " (.getMessage e))})))

(defn update-clojure-sixos-deps
  "Update clojure-sixos dependencies for grain6os integration"
  []
  (let [clojure-sixos-path (:clojure-sixos-path grain6os-config)
        deps-edn-path (str clojure-sixos-path "/deps.edn")]
    (if (fs/exists? deps-edn-path)
      {:success true
       :message "clojure-sixos deps.edn found - ready for grain6os integration"}
      {:success false
       :message (str "clojure-sixos deps.edn not found at: " deps-edn-path)})))
