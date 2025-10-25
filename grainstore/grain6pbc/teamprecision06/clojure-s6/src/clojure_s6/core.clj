(ns clojure-s6.core
  "Core s6 supervision management functions"
  (:require [clojure.java.shell :as shell]
            [clojure.string :as str]
            [clojure.core.async :as async :refer [go go-loop timeout <! >! <!! >!!]]
            [clojure.java-time :as time]
            [babashka.fs :as fs]))

(defn log [message]
  "Log with timestamp"
  (let [timestamp (time/local-date-time)
        formatted-time (time/format "HH:mm:ss" timestamp)]
    (println (str "[" formatted-time "] s6: " message))))

(defn run-command [cmd & {:keys [sh timeout-ms]}]
  "Run s6 command with optional shell execution and timeout"
  (try
    (let [result (if sh
                   (shell/sh "bash" "-c" cmd)
                   (shell/sh cmd))]
      (if (= (:exit result) 0)
        (str/trim (:out result))
        (do
          (log (str "Command failed: " cmd " - " (:err result)))
          "")))
    (catch Exception e
      (log (str "Error running command: " cmd " - " (.getMessage e)))
      "")))

(defn service-dir [service-name]
  "Get service directory path"
  (str "/etc/s6/sv/" service-name))

(defn service-run-file [service-name]
  "Get service run file path"
  (str (service-dir service-name) "/run"))

(defn service-finish-file [service-name]
  "Get service finish file path"
  (str (service-dir service-name) "/finish"))

(defn service-type-file [service-name]
  "Get service type file path"
  (str (service-dir service-name) "/type"))

(defn service-dependencies-file [service-name]
  "Get service dependencies file path"
  (str (service-dir service-name) "/dependencies"))

(defn create-service-dir [service-name]
  "Create service directory structure"
  (let [dir (service-dir service-name)]
    (run-command (str "mkdir -p " dir) :sh true)
    (run-command (str "chmod 755 " dir) :sh true)
    (log (str "Created service directory: " dir))))

(defn create-run-script [service-name command]
  "Create service run script"
  (let [run-file (service-run-file service-name)
        script (str "#!/bin/sh\nexec " command)]
    (spit run-file script)
    (run-command (str "chmod +x " run-file) :sh true)
    (log (str "Created run script for " service-name))))

(defn create-finish-script [service-name finish-command]
  "Create service finish script"
  (when finish-command
    (let [finish-file (service-finish-file service-name)
          script (str "#!/bin/sh\n" finish-command)]
      (spit finish-file script)
      (run-command (str "chmod +x " finish-file) :sh true)
      (log (str "Created finish script for " service-name)))))

(defn set-service-type [service-name type]
  "Set service type (oneshot, longrun, etc.)"
  (let [type-file (service-type-file service-name)]
    (spit type-file (name type))
    (log (str "Set service type for " service-name " to " type))))

(defn set-service-dependencies [service-name dependencies]
  "Set service dependencies"
  (let [deps-file (service-dependencies-file service-name)
        deps-content (str/join "\n" dependencies)]
    (spit deps-file deps-content)
    (log (str "Set dependencies for " service-name ": " (str/join ", " dependencies)))))

(defn create-service
  "Create a complete s6 service"
  [{:keys [name command finish-command type dependencies user group environment]
    :or {type :longrun dependencies []}}]
  (log (str "Creating service: " name))
  
  (create-service-dir name)
  (create-run-script name command)
  (create-finish-script name finish-command)
  (set-service-type name type)
  (set-service-dependencies name dependencies)
  
  ;; Set ownership if specified
  (when (or user group)
    (let [owner (str (or user "") ":" (or group ""))
          dir (service-dir name)]
      (run-command (str "chown -R " owner " " dir) :sh true)))
  
  (log (str "Service " name " created successfully")))

(defn service-status [service-name]
  "Get service status"
  (let [result (run-command (str "s6-svstat " (service-dir service-name)) :sh true)]
    (if (str/blank? result)
      {:status :not-found}
      (let [lines (str/split-lines result)
            status-line (first lines)
            up? (str/includes? status-line "up")
            running? (str/includes? status-line "running")
            pid (when running?
                  (let [pid-match (re-find #"\(pid (\d+)\)" status-line)]
                    (when pid-match
                      (Integer/parseInt (second pid-match)))))]
        {:status (cond
                   (not up?) :down
                   running? :running
                   :else :up)
         :pid pid
         :raw status-line}))))

(defn start-service [service-name]
  "Start a service"
  (log (str "Starting service: " service-name))
  (let [result (run-command (str "s6-svc -u " (service-dir service-name)) :sh true)]
    (if (str/blank? result)
      (do
        (log (str "Service " service-name " started"))
        true)
      (do
        (log (str "Failed to start service " service-name ": " result))
        false))))

(defn stop-service [service-name]
  "Stop a service"
  (log (str "Stopping service: " service-name))
  (let [result (run-command (str "s6-svc -d " (service-dir service-name)) :sh true)]
    (if (str/blank? result)
      (do
        (log (str "Service " service-name " stopped"))
        true)
      (do
        (log (str "Failed to stop service " service-name ": " result))
        false))))

(defn restart-service [service-name]
  "Restart a service"
  (log (str "Restarting service: " service-name))
  (let [result (run-command (str "s6-svc -r " (service-dir service-name)) :sh true)]
    (if (str/blank? result)
      (do
        (log (str "Service " service-name " restarted"))
        true)
      (do
        (log (str "Failed to restart service " service-name ": " result))
        false))))

(defn list-services []
  "List all services"
  (let [result (run-command "s6-svscanctl -l /etc/s6/sv" :sh true)]
    (if (str/blank? result)
      []
      (str/split-lines result))))

(defn service-logs [service-name & {:keys [lines follow]}]
  "Get service logs"
  (let [log-dir (str "/var/log/s6/" service-name)
        cmd (if follow
              (str "tail -f " log-dir "/current")
              (str "tail -n " (or lines 100) " " log-dir "/current"))]
    (run-command cmd :sh true)))

(defn monitor-services
  "Monitor multiple services with callbacks"
  [service-names {:keys [on-status-change on-failure check-interval]
                  :or {check-interval 5000}}]
  (go-loop [last-statuses {}]
    (let [current-statuses (into {} (map (fn [name] [name (service-status name)]) service-names))
          changes (filter (fn [[name status]]
                           (not= (get last-statuses name) status))
                         current-statuses)]
      
      (doseq [[name status] changes]
        (when on-status-change
          (on-status-change name status last-statuses current-statuses))
        
        (when (and on-failure (= (:status status) :down))
          (on-failure name status)))
      
      (<! (timeout check-interval))
      (recur current-statuses))))

(defn enable-service [service-name]
  "Enable service at boot"
  (log (str "Enabling service: " service-name))
  (run-command (str "s6-svlink " (service-dir service-name) " /etc/s6/sv") :sh true))

(defn disable-service [service-name]
  "Disable service at boot"
  (log (str "Disabling service: " service-name))
  (run-command (str "s6-svunlink " (service-dir service-name)) :sh true))

(defn start-with-dependencies [service-name]
  "Start service and all its dependencies"
  (let [deps-file (service-dependencies-file service-name)
        dependencies (if (fs/exists? deps-file)
                       (str/split-lines (slurp deps-file))
                       [])]
    (doseq [dep dependencies]
      (when-not (str/blank? dep)
        (start-service dep)))
    (start-service service-name)))

(defn configure-sixos
  "Configure s6 for SixOS"
  [{:keys [supervision-dir log-dir enable-scanning]
    :or {supervision-dir "/etc/s6/sv"
         log-dir "/var/log/s6"
         enable-scanning true}}]
  (log "Configuring s6 for SixOS")
  
  (run-command (str "mkdir -p " supervision-dir) :sh true)
  (run-command (str "mkdir -p " log-dir) :sh true)
  
  (when enable-scanning
    (run-command "s6-svscanctl -a /etc/s6/sv" :sh true)
    (log "s6 scanning enabled"))
  
  (log "SixOS configuration complete"))
