#!/usr/bin/env bb

;; ╔══════════════════════════════════════════════════════════════════════════════╗
;; ║                                                                              ║
;; ║                        🌾 KAE3G CURSOR-STYLE CHAT 🌾                        ║
;; ║                                                                              ║
;; ║   Interactive chat interface for Grain Network development                  ║
;; ║   Simulates Cursor's agent chat window within zellij                        ║
;; ║                                                                              ║
;; ╚══════════════════════════════════════════════════════════════════════════════╝

(require '[babashka.fs :as fs]
         '[babashka.process :refer [shell]]
         '[clojure.string :as str])

(defn clear-screen []
  "Clear terminal screen"
  (print "\033[2J\033[H"))

(defn print-header []
  "Print Cursor-style header"
  (println "╔══════════════════════════════════════════════════════════════════════════════╗")
  (println "║                                                                              ║")
  (println "║                        🌾 KAE3G CURSOR AGENT CHAT 🌾                       ║")
  (println "║                                                                              ║")
  (println "║   Grain Network Development Assistant                                       ║")
  (println "║   NixOS VM + Mosh + Zellij Environment                                      ║")
  (println "║                                                                              ║")
  (println "╚══════════════════════════════════════════════════════════════════════════════╝")
  (println ""))

(defn print-status []
  "Print current system status"
  (let [graintime (try
                    (shell {:out :string} "bb gt now")
                    (catch Exception _ "graintime unavailable"))
        grainpath (try
                    (shell {:out :string} "bb qb path")
                    (catch Exception _ "grainpath unavailable"))
        vm-info (try
                  (shell {:out :string} "hostname && whoami && pwd")
                  (catch Exception _ "VM info unavailable"))]
    
    (println "📊 CURRENT STATUS:")
    (println (str "   VM: " (str/trim vm-info)))
    (println (str "   Graintime: " (str/trim graintime)))
    (println (str "   Grainpath: " (str/trim grainpath)))
    (println "")))

(defn print-menu []
  "Print command menu"
  (println "🚀 AVAILABLE COMMANDS:")
  (println "   gt now          - Current graintime")
  (println "   qb kk           - Grainbook display")
  (println "   bb flow         - Deploy changes")
  (println "   bb qb-shot      - VM screenshot")
  (println "   bb mosh-vm      - Connect to VM")
  (println "   grain6:status   - Check grain6 daemon")
  (println "   grainwifi:status - Check grainwifi daemon")
  (println "   exit            - Exit chat")
  (println ""))

(defn execute-command [cmd]
  "Execute a command and return output"
  (try
    (let [result (shell {:out :string :err :string} cmd)]
      (if (= 0 (:exit result))
        (:out result)
        (str "❌ Error: " (:err result))))
    (catch Exception e
      (str "❌ Exception: " (.getMessage e)))))

(defn chat-loop []
  "Main chat loop"
  (loop []
    (print "🌾 kae3g> ")
    (flush)
    (let [input (read-line)]
      (when (and input (not (str/blank? input)))
        (cond
          (= "exit" input)
          (do
            (println "👋 Goodbye! Grain Network development continues...")
            (System/exit 0))
          
          (= "help" input)
          (do
            (clear-screen)
            (print-header)
            (print-status)
            (print-menu))
          
          (= "status" input)
          (do
            (print-status)
            (recur))
          
          (= "clear" input)
          (do
            (clear-screen)
            (print-header)
            (recur))
          
          :else
          (do
            (println "")
            (println "🔄 Executing: " input)
            (println "─" (apply str (repeat 60 "-")))
            (let [output (execute-command input)]
              (println output)
              (println "─" (apply str (repeat 60 "-"))))
            (println "")
            (recur)))))))

(defn main []
  "Main entry point"
  (clear-screen)
  (print-header)
  (print-status)
  (print-menu)
  (chat-loop))

;; Start the chat interface
(main)
