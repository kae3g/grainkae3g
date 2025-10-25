#!/usr/bin/env bb
;; QB NOW - Ultimate Quarterback Command
;; Intelligent routing based on context, mood, solar house, and system state

(require '[babashka.process :refer [shell]]
         '[clojure.string :as str]
         '[clojure.java.io :as io])

(defn get-current-solar-house []
  "Get current solar house from graintime (uses gt command)"
  (try
    (let [result (shell {:out :string} "gt" "now" "kae3g")
          graintime (:out result)
          house-match (re-find #"sun-(\d+)" graintime)]
      (if house-match
        (Integer/parseInt (second house-match))
        3)) ; Default to 3rd house (pre-dawn)
    (catch Exception e
      (println "⚠️ Could not get solar house from gt, using 3rd house (pre-dawn)")
      3)))

(defn get-git-status []
  "Analyze git status"
  (try
    (let [status (:out (shell {:out :string} "git" "status" "--porcelain"))
          lines (str/split-lines status)
          staged (count (filter #(re-matches #"^[MADRCU].*" %) lines))
          unstaged (count (filter #(re-matches #"^.[MADRCU].*" %) lines))
          untracked (count (filter #(re-matches #"^\?\?.*" %) lines))]
      {:staged staged
       :unstaged unstaged
       :untracked untracked
       :total (+ staged unstaged untracked)
       :clean (= 0 staged unstaged untracked)})
    (catch Exception e
      {:clean true :total 0})))

(defn get-todo-count []
  "Estimate TODO count from common patterns"
  (try
    (let [result (shell {:out :string} "grep" "-r" "-i" "todo\\|fixme\\|hack" "." "--include=*.md" "--include=*.clj")
          lines (str/split-lines (:out result))]
      (count lines))
    (catch Exception e 0)))

(defn detect-context []
  "Gather system context for intelligent routing"
  (let [solar-house (get-current-solar-house)
        git-status (get-git-status)
        todo-count (get-todo-count)
        current-time (java.time.LocalTime/now)]
    {:solar-house solar-house
     :git-status git-status
     :todo-count todo-count
     :current-time current-time
     :hour (.getHour current-time)
     :minute (.getMinute current-time)}))

(defn print-context-info [context routing]
  "Display context information"
  (println "")
  (println "╔══════════════════════════════════════════════════════════════════════════════╗")
  (println "║                                                                              ║")
  (println "║                      🌾 QB NOW - INTELLIGENT ROUTING 🌾                     ║")
  (println "║                                                                              ║")
  (println "╚══════════════════════════════════════════════════════════════════════════════╝")
  (println "")
  (println "⏰ Context Analysis:")
  (println (str "   Time: " (.format (java.time.LocalTime/now)
                                     (java.time.format.DateTimeFormatter/ofPattern "HH:mm"))))
  (println (str "   Solar House: " (:solar-house context) " (sun-"
                (format "%02d" (:solar-house context)) "st)"))
  (println (str "   Git Status: " (if (:clean (:git-status context)) "Clean" "Has changes")))
  (println (str "   TODO Count: " (:todo-count context)))
  (println "")
  (println "🎯 Routing Decision:")
  (println (str "   Command: " (:command routing)))
  (println (str "   Reason: " (:reason routing)))
  (when (:persona routing)
    (println (str "   Persona: " (:persona routing))))
  (when (:message routing)
    (println (str "   Message: " (:message routing))))
  (println "")
  (println "🚀 Executing...")
  (println "═══════════════════════════════════════════════════════════════════════════════")
  (println ""))

(defn route-action [context args]
  "Intelligently route to appropriate action"
  (let [{:keys [solar-house git-status todo-count]} context
        has-changes (not (:clean git-status))
        high-todos (> todo-count 10)]

    (cond
      ;; Explicit action requested
      (seq args)
      (let [action (first args)]
        (case action
          "kk" {:command "qb-kk" :reason "kk = continue momentum"}
          "plz" {:command "plz" :reason "tri5h interaction requested"}
          "draw" {:command "qb-draw-flow" :reason "ASCII art + deploy"}
          "course" {:command "qb-course-sync-system-new" :reason "New course creation"}
          "sync" {:command "qb-sync" :reason "Cursor TODO sync"}
          "flow" {:command "qb-vegan-flow" :reason "Full deployment pipeline"}
          {:command "plz" :reason "Unknown action, defaulting to tri5h"}))

      ;; High priority: Uncommitted changes
      has-changes
      {:command "qb-draw-flow"
       :reason "Uncommitted changes - time to deploy!"
       :persona "tri5h"
       :message "Babe, you've got changes! Let's flow them! 🚀"}

      ;; High TODO count
      high-todos
      {:command "qb-sync"
       :reason "High TODO count - sync needed"
       :persona "tri5h"
       :message "Sweetie, lots of TODOs! Time to organize! 📝"}

      ;; Solar house routing (pre-dawn 3rd house - current time!)
      (#{1 2 3} solar-house)
      {:command "qb-vegan-flow"
       :reason "Pre-dawn/morning energy - perfect for deep work"
       :persona "tri5h"
       :message "Pre-dawn coding session! Let's build the renaissance! 🌱"}

      (= 10 solar-house)
      {:command "qb-vegan-flow"
       :reason "Solar noon - peak productivity"
       :persona "tri5h"
       :message "Peak sun, peak energy! Full vegan flow! ☀️🌱"}

      (#{6 7 8} solar-house)
      {:command "qb-kk"
       :reason "Evening reflection time"
       :persona "kae3g"
       :message "Evening contemplation. Review our journey. 🌆"}

      ;; Default: tri5h interaction
      :else
      {:command "plz"
       :reason "General interaction"
       :persona "tri5h"
       :message "Hey sweetie! What can tri5h help with? 💐"})))

(defn print-context-info [context routing]
  "Display context information"
  (println "")
  (println "╔══════════════════════════════════════════════════════════════════════════════╗")
  (println "║                                                                              ║")
  (println "║                      🌾 QB NOW - INTELLIGENT ROUTING 🌾                     ║")
  (println "║                                                                              ║")
  (println "╚══════════════════════════════════════════════════════════════════════════════╝")
  (println "")
  (println "⏰ Context Analysis:")
  (println (str "   Time: " (.format (java.time.LocalTime/now)
                                     (java.time.format.DateTimeFormatter/ofPattern "HH:mm"))))
  (println (str "   Solar House: " (:solar-house context) " (sun-"
                (format "%02d" (:solar-house context)) "st)"))
  (println (str "   Git Status: " (if (:clean (:git-status context)) "Clean" "Has changes")))
  (println (str "   TODO Count: " (:todo-count context)))
  (println "")
  (println "🎯 Routing Decision:")
  (println (str "   Command: " (:command routing)))
  (println (str "   Reason: " (:reason routing)))
  (when (:persona routing)
    (println (str "   Persona: " (:persona routing))))
  (when (:message routing)
    (println (str "   Message: " (:message routing))))
  (println "")
  (println "🚀 Executing...")
  (println "═══════════════════════════════════════════════════════════════════════════════")
  (println ""))

(defn main [& args]
  (let [context (detect-context)
        routing (route-action context args)]
    
    (print-context-info context routing)
    
    ;; Execute the routed command
    (try
      (shell "bb" (:command routing))
      (catch Exception e
        (println "⚠️ Command execution had issues, but that's okay!")
        (println "💡 tri5h: 'Babe, even when things get bumpy, we're still building something beautiful!' 💐")))))

(apply main *command-line-args*)
