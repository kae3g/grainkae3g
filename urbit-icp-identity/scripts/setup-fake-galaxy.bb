#!/usr/bin/env bb

;; Setup Fake Urbit Developer Galaxy
;; Creates a fake galaxy with developer identities for testing

(require '[clojure.java.shell :as shell]
         '[clojure.string :as str])

(defn log [message]
  "Log a message with timestamp"
  (println (str "[" (java.time.LocalDateTime/now) "] " message)))

(defn run-dfx-command [command]
  "Run a DFX command and return output"
  (let [full-command (str "source $HOME/.local/share/dfx/env && " command)]
    (try
      (-> (shell/sh "bash" "-c" full-command)
          :out
          str/trim)
      (catch Exception e
        (log (str "Error running command: " command " - " (.getMessage e)))
        ""))))

(defn create-galaxy []
  "Create the main galaxy identity"
  (log "🌌 Creating fake Urbit galaxy...")
  
  (let [result (run-dfx-command 
                 "dfx canister call urbit-identity create_identity '(\"Fake Urbit Galaxy\", \"A fake galaxy for development and testing\", variant{Galaxy})'")]
    (if (str/includes? result "Ok")
      (let [address (-> result
                        (str/replace #"Ok\(" "")
                        (str/replace #"\)" "")
                        (str/trim))]
        (log (str "✅ Galaxy created with address: " address))
        address)
      (do
        (log (str "❌ Failed to create galaxy: " result))
        nil))))

(defn create-stars [galaxy-address]
  "Create star identities under the galaxy"
  (log "⭐ Creating fake Urbit stars...")
  
  (let [star-names ["~zod" "~bus" "~wes" "~sev" "~pol" "~bin" "~dig" "~wic" "~hex" "~fes"]]
    (doseq [star-name star-names]
      (let [result (run-dfx-command 
                     (str "dfx canister call urbit-identity create_identity '(\"" star-name "\", \"Fake star for development\", variant{Star})'"))]
        (if (str/includes? result "Ok")
          (let [address (-> result
                            (str/replace #"Ok\(" "")
                            (str/replace #"\)" "")
                            (str/trim))]
            (log (str "✅ Star " star-name " created with address: " address)))
          (log (str "❌ Failed to create star " star-name ": " result)))))))

(defn create-planets [star-addresses]
  "Create planet identities under the stars"
  (log "🪐 Creating fake Urbit planets...")
  
  (let [planet-names ["~zod-tes" "~bus-tes" "~wes-tes" "~sev-tes" "~pol-tes" "~bin-tes" "~dig-tes" "~wic-tes" "~hex-tes" "~fes-tes"]]
    (doseq [planet-name planet-names]
      (let [result (run-dfx-command 
                     (str "dfx canister call urbit-identity create_identity '(\"" planet-name "\", \"Fake planet for development\", variant{Planet})'"))]
        (if (str/includes? result "Ok")
          (let [address (-> result
                            (str/replace #"Ok\(" "")
                            (str/replace #"\)" "")
                            (str/trim))]
            (log (str "✅ Planet " planet-name " created with address: " address)))
          (log (str "❌ Failed to create planet " planet-name ": " result)))))))

(defn create-moons [planet-addresses]
  "Create moon identities under the planets"
  (log "🌙 Creating fake Urbit moons...")
  
  (let [moon-names ["~zod-tes-moon" "~bus-tes-moon" "~wes-tes-moon" "~sev-tes-moon" "~pol-tes-moon"]]
    (doseq [moon-name moon-names]
      (let [result (run-dfx-command 
                     (str "dfx canister call urbit-identity create_identity '(\"" moon-name "\", \"Fake moon for development\", variant{Moon})'"))]
        (if (str/includes? result "Ok")
          (let [address (-> result
                            (str/replace #"Ok\(" "")
                            (str/replace #"\)" "")
                            (str/trim))]
            (log (str "✅ Moon " moon-name " created with address: " address)))
          (log (str "❌ Failed to create moon " moon-name ": " result)))))))

(defn get-stats []
  "Get current statistics"
  (log "📊 Getting galaxy statistics...")
  
  (let [result (run-dfx-command "dfx canister call urbit-identity get_stats")]
    (log (str "📈 Galaxy stats: " result))))

(defn list-identities []
  "List all identities"
  (log "📋 Listing all identities...")
  
  (let [result (run-dfx-command "dfx canister call urbit-identity get_logs")]
    (log "📝 Recent activity:")
    (doseq [line (str/split-lines result)]
      (when (str/starts-with? line "[")
        (log (str "   " line))))))

(defn main []
  "Main setup function"
  (log "🚀 Setting up Fake Urbit Developer Galaxy...")
  
  (try
    ;; Create galaxy
    (let [galaxy-address (create-galaxy)]
      (when galaxy-address
        ;; Create stars
        (create-stars galaxy-address)
        
        ;; Create planets
        (create-planets [])
        
        ;; Create moons
        (create-moons [])
        
        ;; Get statistics
        (get-stats)
        
        ;; List identities
        (list-identities)
        
        (log "🎉 Fake Urbit Galaxy setup complete!")
        (log "")
        (log "🌐 Access your galaxy at:")
        (log "   http://127.0.0.1:4943/?canisterId=u6s2n-gx777-77774-qaaba-cai&id=uxrrr-q7777-77774-qaaaq-cai")
        (log "")
        (log "🔧 Test commands:")
        (log "   dfx canister call urbit-identity get_stats")
        (log "   dfx canister call urbit-identity get_logs")
        (log "   dfx canister call urbit-identity get_identity '(1)'")))
    
    (catch Exception e
      (log (str "❌ Setup failed: " (.getMessage e)))
      (System/exit 1))))

;; Run the setup
(main)

