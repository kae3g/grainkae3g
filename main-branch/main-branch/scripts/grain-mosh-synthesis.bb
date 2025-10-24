#!/usr/bin/env bb

;; ╔══════════════════════════════════════════════════════════════════════════════╗
;; ║                                                                              ║
;; ║                    🌾 GRAINDAEMON-MOSH SYNTHESIS 🌾                         ║
;; ║                                                                              ║
;; ║   Persistent session management with temporal awareness                     ║
;; ║   Combines Mosh's resilience with graindaemon's time-aware supervision      ║
;; ║                                                                              ║
;; ╚══════════════════════════════════════════════════════════════════════════════╝

(require '[babashka.fs :as fs]
         '[babashka.process :refer [shell]]
         '[clojure.string :as str]
         '[clojure.edn :as edn])

(defn get-graintime []
  "Get current graintime for session context"
  (try
    (let [result (shell {:out :string} "bb gt now")]
      (when (= 0 (:exit result))
        (str/trim (:out result))))
    (catch Exception _ nil)))

(defn get-grainpath []
  "Get current grainpath for session context"
  (try
    (let [result (shell {:out :string} "bb qb path")]
      (when (= 0 (:exit result))
        (str/trim (:out result))))
    (catch Exception _ nil)))

(defn create-grain-session []
  "Create a graindaemon-managed session with temporal context"
  (let [graintime (get-graintime)
        grainpath (get-grainpath)
        session-id (str "grain-" (System/currentTimeMillis))
        session-dir (str "/tmp/grain-sessions/" session-id)]
    
    ;; Create session directory
    (fs/create-dirs session-dir)
    
    ;; Write session metadata
    (spit (str session-dir "/session.edn")
          {:session-id session-id
           :graintime graintime
           :grainpath grainpath
           :created-at (java.time.Instant/now)
           :mosh-enabled true
           :graindaemon-managed true})
    
    session-id))

(defn start-grain-mosh-session [host user key-path]
  "Start a Mosh session with graindaemon supervision"
  (let [session-id (create-grain-session)
        graintime (get-graintime)
        grainpath (get-grainpath)]
    
    (println "🌾 Starting Grain-Mosh Session")
    (println (str "   Session ID: " session-id))
    (println (str "   Graintime: " graintime))
    (println (str "   Grainpath: " grainpath))
    (println (str "   Host: " user "@" host))
    (println "")
    
    ;; Start graindaemon supervisor for this session
    (let [supervisor-cmd (str "bb graindaemon:supervise-session " session-id)]
      (shell {:out :string} supervisor-cmd))
    
    ;; Start Mosh with graindaemon context
    (let [mosh-cmd (str "mosh --ssh=\"ssh -i " key-path "\" " user "@" host)]
      (println "🚀 Launching Mosh with graindaemon supervision...")
      (shell mosh-cmd))))

(defn supervise-grain-session [session-id]
  "Supervise a grain session with temporal awareness"
  (let [session-file (str "/tmp/grain-sessions/" session-id "/session.edn")]
    (when (fs/exists? session-file)
      (let [session-data (edn/read-string (slurp session-file))
            graintime (:graintime session-data)
            grainpath (:grainpath session-data)]
        
        (println "🕐 Supervising Grain Session:" session-id)
        (println (str "   Original Graintime: " graintime))
        (println (str "   Original Grainpath: " grainpath))
        
        ;; Monitor session health and temporal context
        (loop []
          (let [current-graintime (get-graintime)
                current-grainpath (get-grainpath)]
            
            ;; Check for temporal drift
            (when (not= graintime current-graintime)
              (println (str "⏰ Temporal drift detected: " graintime " → " current-graintime)))
            
            ;; Check for path changes
            (when (not= grainpath current-grainpath)
              (println (str "🛤️  Path drift detected: " grainpath " → " current-grainpath)))
            
            ;; Update session metadata
            (spit session-file
                  (assoc session-data
                         :last-checked (java.time.Instant/now)
                         :current-graintime current-graintime
                         :current-grainpath current-grainpath))
            
            ;; Sleep and continue monitoring
            (Thread/sleep 30000) ; 30 seconds
            (recur)))))))

(defn list-grain-sessions []
  "List all active grain sessions"
  (let [sessions-dir "/tmp/grain-sessions"]
    (if (fs/exists? sessions-dir)
      (let [sessions (fs/glob sessions-dir "grain-*")]
        (println "🌾 Active Grain Sessions:")
        (doseq [session sessions]
          (let [session-file (str session "/session.edn")]
            (when (fs/exists? session-file)
              (let [data (edn/read-string (slurp session-file))]
                (println (str "   " (:session-id data) " - " (:graintime data)))))))
        (println ""))
      (println "No active grain sessions"))))

(defn main [& args]
  "Main entry point for grain-mosh synthesis"
  (let [command (first args)]
    (case command
      "start" (let [host (nth args 1 "192.168.122.204")
                    user (nth args 2 "nixos")
                    key-path (nth args 3 "/home/xy/.ssh/nixos-grainkae3g")]
                (start-grain-mosh-session host user key-path))
      
      "supervise" (let [session-id (nth args 1)]
                    (supervise-grain-session session-id))
      
      "list" (list-grain-sessions)
      
      "help" (do
               (println "🌾 Grain-Mosh Synthesis Commands:")
               (println "   start [host] [user] [key-path] - Start supervised Mosh session")
               (println "   supervise [session-id] - Supervise existing session")
               (println "   list - List active sessions")
               (println "   help - Show this help"))
      
      (do
        (println "Unknown command:" command)
        (println "Use 'help' for available commands")))))

;; Execute main function
(apply main *command-line-args*)
