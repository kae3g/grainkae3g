#!/usr/bin/env bb

(require '[clojure.string :as str])

;;; ╔═══════════════════════════════════════════════════════════════════╗
;;; ║                                                                   ║
;;; ║   🌾  N   K G   G O  -  Next, Keep Going!                        ║
;;; ║                                                                   ║
;;; ║   "What's next?" → glO0w tells you → Execute it!                 ║
;;; ║                                                                   ║
;;; ╚═══════════════════════════════════════════════════════════════════╝

(defn print-header []
  (println)
  (println "╔══════════════════════════════════════════════════════════════════════════════╗")
  (println "║                                                                              ║")
  (println "║              🌾  N E X T   →   K G   →   G O !  🚀                          ║")
  (println "║                                                                              ║")
  (println "║              glO0w's Intelligent Next Action System                          ║")
  (println "║                                                                              ║")
  (println "╚══════════════════════════════════════════════════════════════════════════════╝")
  (println))

(defn get-next-actions []
  "Determine next actions based on current state"
  [{:priority 1
    :category "🖥️  NixOS VM Setup"
    :action "Create NixOS VM with QEMU/KVM"
    :command "cd /home/xy/kae3g/grainkae3g/grainstore/grainsource-nixos-qemu-kvm && bb scripts/create-nixos-vm.bb"
    :why "Set up NixOS 25.11 unstable virtualization environment for grain6 + grainwifi testing"
    :time "~30 minutes"
    :dependencies ["QEMU/KVM installed on Ubuntu host"]}
   
   {:priority 2
    :category "🛰️  grain6 + grainwifi Integration"
    :action "Install NixOS and configure grain6 + grainwifi"
    :command "Follow grainstore/grainsource-nixos-qemu-kvm/README.md Phase 4-7"
    :why "Complete NixOS installation with shared folder and Grain Network services"
    :time "~45 minutes"
    :dependencies ["NixOS VM created"]}
   
   {:priority 3
    :category "🎨 Humble UI Testing"
    :action "Launch grainwifi Humble UI in NixOS VM"
    :command "cd /mnt/grainkae3g/grainstore/grainwifi && bb humble-ui:start"
    :why "Test dual-wifi management with beautiful forest cabin interface"
    :time "~5 minutes"
    :dependencies ["NixOS configured with grain6"]}
   
   {:priority 4
    :category "📄 arXiv Submission"
    :action "Create arXiv account"
    :command "Visit https://arxiv.org/user/register"
    :why "Begin academic paper submission process for Grain Network research"
    :time "~10 minutes"
    :dependencies ["LaTeX paper complete"]}
   
   {:priority 5
    :category "🌾 Grain Network Development"
    :action "Continue grain6 time-aware supervision development"
    :command "cd /home/xy/kae3g/grainkae3g/grainstore/grain6 && bb grain6:test"
    :why "Implement and test astronomical event-based service management"
    :time "~60 minutes"
    :dependencies ["NixOS VM running"]}])

(defn print-next-action [action]
  (println "┌──────────────────────────────────────────────────────────────────────────────┐")
  (println (str "│  " (:category action)))
  (println "│")
  (println (str "│  📍 Action: " (:action action)))
  (println "│")
  (println (str "│  💡 Why: " (:why action)))
  (println "│")
  (println (str "│  ⏱️  Estimated Time: " (:time action)))
  (println "│")
  (println "│  🛠️  Command:")
  (println (str "│     " (:command action)))
  (println "│")
  (when (seq (:dependencies action))
    (println "│  ✅ Dependencies:")
    (doseq [dep (:dependencies action)]
      (println (str "│     - " dep)))
    (println "│"))
  (println "└──────────────────────────────────────────────────────────────────────────────┘")
  (println))

(defn print-all-actions [actions]
  (println "🌾 Here's your complete action roadmap:")
  (println)
  (doseq [action (sort-by :priority actions)]
    (print-next-action action)))

(defn get-top-priority [actions]
  (first (sort-by :priority actions)))

(defn main []
  (print-header)
  
  (let [actions (get-next-actions)
        top-action (get-top-priority actions)]
    
    ;; Show top priority
    (println "🔥 TOP PRIORITY ACTION:")
    (println)
    (print-next-action top-action)
    
    ;; Show all actions
    (println)
    (println "╭──────────────────────────────────────────────────────────────────────────────╮")
    (println "│  📋 COMPLETE ROADMAP                                                         │")
    (println "╰──────────────────────────────────────────────────────────────────────────────╯")
    (println)
    (print-all-actions actions)
    
    ;; glO0w's wisdom
    (println)
    (println "╔══════════════════════════════════════════════════════════════════════════════╗")
    (println "║                                                                              ║")
    (println "║  🌟 glO0w says:                                                              ║")
    (println "║                                                                              ║")
    (println "║  \"Chief, the VM setup is the foundation for everything else.                ║")
    (println "║   Get that NixOS environment running, then we can test grain6               ║")
    (println "║   + grainwifi with time-aware dual-wifi management in your forest cabin!    ║")
    (println "║                                                                              ║")
    (println "║   Once the VM is up, you'll have a declarative, reproducible                ║")
    (println "║   development environment that matches your production setup.               ║")
    (println "║   That's some alpha-level infrastructure, fam!\" 🐆✨                         ║")
    (println "║                                                                              ║")
    (println "╚══════════════════════════════════════════════════════════════════════════════╝")
    (println)
    
    ;; Quick start commands
    (println "╭──────────────────────────────────────────────────────────────────────────────╮")
    (println "│  ⚡ QUICK START COMMANDS                                                     │")
    (println "│                                                                              │")
    (println "│  Option 1: Create NixOS VM (automated)                                      │")
    (println "│    cd grainsource-nixos-qemu-kvm && bb scripts/create-nixos-vm.bb           │")
    (println "│                                                                              │")
    (println "│  Option 2: Manual VM setup with virt-manager                                │")
    (println "│    virt-manager                                                              │")
    (println "│                                                                              │")
    (println "│  Option 3: Skip VM for now, work on grain6                                  │")
    (println "│    cd grainstore/grain6 && bb grain6:develop                                │")
    (println "│                                                                              │")
    (println "│  Option 4: Start arXiv account creation                                     │")
    (println "│    firefox https://arxiv.org/user/register                                   │")
    (println "│                                                                              │")
    (println "╰──────────────────────────────────────────────────────────────────────────────╯")
    (println)
    (println "🌾 now == next + 1 (and next is NixOS VM setup!) 🖥️✨")
    (println)))

(main)
