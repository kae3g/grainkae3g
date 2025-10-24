#!/usr/bin/env bb

;; ╔══════════════════════════════════════════════════════════════════════════════╗
;; ║                                                                              ║
;; ║                    🌾 MOSH OUTPUT AUTO-SHARING 🌾                           ║
;; ║                                                                              ║
;; ║   Automatically shares Mosh session output with Cursor                      ║
;; ║   Uses shared log files and real-time monitoring                           ║
;; ║                                                                              ║
;; ╚══════════════════════════════════════════════════════════════════════════════╝

(require '[babashka.fs :as fs]
         '[babashka.process :refer [shell]]
         '[clojure.string :as str])

(defn setup-output-sharing []
  "Set up shared output directory and log files"
  (let [shared-dir "/mnt/grainkae3g/mosh-output"
        session-id (str "session-" (System/currentTimeMillis))
        session-dir (str shared-dir "/" session-id)]
    
    ;; Create shared directory
    (fs/create-dirs session-dir)
    
    ;; Create log files
    (let [stdout-log (str session-dir "/stdout.log")
          stderr-log (str session-dir "/stderr.log")
          commands-log (str session-dir "/commands.log")]
      
      ;; Initialize log files
      (spit stdout-log "")
      (spit stderr-log "")
      (spit commands-log "")
      
      ;; Create session metadata
      (spit (str session-dir "/session.edn")
            {:session-id session-id
             :created-at (java.time.Instant/now)
             :shared-dir shared-dir
             :stdout-log stdout-log
             :stderr-log stderr-log
             :commands-log commands-log})
      
      {:session-id session-id
       :session-dir session-dir
       :stdout-log stdout-log
       :stderr-log stderr-log
       :commands-log commands-log})))

(defn log-command [commands-log cmd]
  "Log a command to the commands log"
  (let [timestamp (java.time.Instant/now)
        log-entry (str "[" timestamp "] " cmd "\n")]
    (spit commands-log log-entry :append true)))

(defn execute-with-logging [cmd stdout-log stderr-log commands-log]
  "Execute a command and log output to shared files"
  (log-command commands-log cmd)
  
  (try
    (let [result (shell {:out :string :err :string} cmd)]
      ;; Log stdout
      (when-not (str/blank? (:out result))
        (spit stdout-log (str (:out result) "\n") :append true))
      
      ;; Log stderr
      (when-not (str/blank? (:err result))
        (spit stderr-log (str (:err result) "\n") :append true))
      
      ;; Log exit status
      (spit stdout-log (str "Exit code: " (:exit result) "\n") :append true)
      
      result)
    (catch Exception e
      (let [error-msg (str "Exception: " (.getMessage e) "\n")]
        (spit stderr-log error-msg :append true)
        {:exit 1 :out "" :err error-msg})))))

(defn monitor-session [session-dir]
  "Monitor a session directory for new output"
  (let [stdout-log (str session-dir "/stdout.log")
        stderr-log (str session-dir "/stderr.log")
        commands-log (str session-dir "/commands.log")]
    
    (println "🔍 Monitoring session:" session-dir)
    (println "📁 Log files:")
    (println (str "   stdout: " stdout-log))
    (println (str "   stderr: " stderr-log))
    (println (str "   commands: " commands-log))
    (println "")
    
    ;; Monitor for changes
    (loop [last-stdout-size 0
           last-stderr-size 0
           last-commands-size 0]
      (Thread/sleep 1000) ; Check every second
      
      (let [stdout-size (if (fs/exists? stdout-log) (fs/size stdout-log) 0)
            stderr-size (if (fs/exists? stderr-log) (fs/size stderr-log) 0)
            commands-size (if (fs/exists? commands-log) (fs/size commands-log) 0)]
        
        ;; Check for new stdout
        (when (> stdout-size last-stdout-size)
          (let [new-content (slurp stdout-log :start last-stdout-size)]
            (when-not (str/blank? new-content)
              (println "📤 NEW STDOUT:")
              (println new-content))))
        
        ;; Check for new stderr
        (when (> stderr-size last-stderr-size)
          (let [new-content (slurp stderr-log :start last-stderr-size)]
            (when-not (str/blank? new-content)
              (println "📤 NEW STDERR:")
              (println new-content))))
        
        ;; Check for new commands
        (when (> commands-size last-commands-size)
          (let [new-content (slurp commands-log :start last-commands-size)]
            (when-not (str/blank? new-content)
              (println "📤 NEW COMMANDS:")
              (println new-content))))
        
        (recur stdout-size stderr-size commands-size)))))

(defn create-mosh-wrapper [session-info]
  "Create a wrapper script for Mosh that logs all output"
  (let [session-dir (:session-dir session-info)
        stdout-log (:stdout-log session-info)
        stderr-log (:stderr-log session-info)
        commands-log (:commands-log session-info)
        wrapper-script (str session-dir "/mosh-wrapper.sh")]
    
    (spit wrapper-script
          (str "#!/bin/bash\n"
               "# Mosh wrapper with output sharing\n"
               "echo '🌾 Starting Mosh with output sharing...'\n"
               "echo 'Session ID: " (:session-id session-info) "'\n"
               "echo 'Logs: " session-dir "'\n"
               "echo ''\n"
               "\n"
               "# Function to log commands\n"
               "log_command() {\n"
               "    echo \"[$(date)] $1\" >> " commands-log "\n"
               "}\n"
               "\n"
               "# Function to execute with logging\n"
               "execute_with_logging() {\n"
               "    log_command \"$1\"\n"
               "    eval \"$1\" 2>> " stderr-log " | tee -a " stdout-log "\n"
               "    echo \"Exit code: $?\" >> " stdout-log "\n"
               "}\n"
               "\n"
               "# Start Mosh\n"
               "mosh --ssh=\"ssh -i ~/.ssh/nixos-grainkae3g\" nixos@192.168.122.204\n"))
    
    (fs/set-posix-file-permissions wrapper-script "rwxr-xr-x")
    
    wrapper-script))

(defn main [& args]
  "Main entry point for Mosh output sharing"
  (let [command (first args)]
    (case command
      "setup" (let [session-info (setup-output-sharing)]
                 (println "🌾 Mosh output sharing setup complete")
                 (println (str "Session ID: " (:session-id session-info)))
                 (println (str "Session dir: " (:session-dir session-info)))
                 (println "")
                 (println "📋 Next steps:")
                 (println "1. Run: bb scripts/mosh-output-sharing.bb monitor " (:session-id session-info))
                 (println "2. In another terminal, run: " (create-mosh-wrapper session-info)))
      
      "monitor" (let [session-id (nth args 1)
                      session-dir (str "/mnt/grainkae3g/mosh-output/" session-id)]
                  (if (fs/exists? session-dir)
                    (monitor-session session-dir)
                    (println "❌ Session not found:" session-id)))
      
      "list" (let [sessions-dir "/mnt/grainkae3g/mosh-output"]
               (if (fs/exists? sessions-dir)
                 (let [sessions (fs/glob sessions-dir "session-*")]
                   (println "🌾 Active Mosh sessions:")
                   (doseq [session sessions]
                     (let [session-file (str session "/session.edn")]
                       (when (fs/exists? session-file)
                         (let [data (edn/read-string (slurp session-file))]
                           (println (str "   " (:session-id data) " - " (:created-at data)))))))
                   (println ""))
                 (println "No active Mosh sessions")))
      
      "help" (do
               (println "🌾 Mosh Output Sharing Commands:")
               (println "   setup - Set up output sharing")
               (println "   monitor [session-id] - Monitor session output")
               (println "   list - List active sessions")
               (println "   help - Show this help"))
      
      (do
        (println "Unknown command:" command)
        (println "Use 'help' for available commands")))))

;; Execute main function
(apply main *command-line-args*)
