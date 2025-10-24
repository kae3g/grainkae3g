#!/usr/bin/env bb

;; Create Authentic Fake Urbit Galaxy
;; Uses real Urbit terminology and ship names from the official codebase

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

;; Authentic Urbit ship names from the codebase
(def galaxy-names ["~zod" "~bus" "~wes" "~sev" "~pol" "~bin" "~dig" "~wic" "~hex" "~fes" "~lec" "~der" "~nut" "~lup" "~dun" "~sut"])

(def star-names ["~zod-tes" "~bus-tes" "~wes-tes" "~sev-tes" "~pol-tes" "~bin-tes" "~dig-tes" "~wic-tes" "~hex-tes" "~fes-tes"])

(def planet-names ["~zod-tes-rac" "~bus-tes-rac" "~wes-tes-rac" "~sev-tes-rac" "~pol-tes-rac" "~bin-tes-rac" "~dig-tes-rac" "~wic-tes-rac" "~hex-tes-rac" "~fes-tes-rac"])

(def moon-names ["~zod-tes-rac-moon" "~bus-tes-rac-moon" "~wes-tes-rac-moon" "~sev-tes-rac-moon" "~pol-tes-rac-moon"])

(defn create-galaxy []
  "Create the main galaxy identity using authentic Urbit terminology"
  (log "🌌 Creating fake Urbit galaxy...")
  
  (let [result (run-dfx-command 
                 "dfx canister call urbit-identity create_identity '(record { name = \"~zod\"; description = \"Fake Urbit Galaxy - Development and Testing\"; identity_type = variant { Galaxy } })'")]
    (if (str/includes? result "Ok")
      (let [address (-> result
                        (str/replace #"Ok\(" "")
                        (str/replace #"\)" "")
                        (str/trim))]
        (log (str "✅ Galaxy ~zod created with address: " address))
        address)
      (do
        (log (str "❌ Failed to create galaxy: " result))
        nil))))

(defn create-stars [galaxy-address]
  "Create star identities under the galaxy using authentic ship names"
  (log "⭐ Creating fake Urbit stars...")
  
  (doseq [star-name (take 8 galaxy-names)] ; Use first 8 galaxy names as stars
    (let [result (run-dfx-command 
                   (str "dfx canister call urbit-identity create_identity '(record { name = \"" star-name "\"; description = \"Fake star for development\"; identity_type = variant { Star } })'"))]
      (if (str/includes? result "Ok")
        (let [address (-> result
                          (str/replace #"Ok\(" "")
                          (str/replace #"\)" "")
                          (str/trim))]
          (log (str "✅ Star " star-name " created with address: " address)))
        (log (str "❌ Failed to create star " star-name ": " result))))))

(defn create-planets [star-addresses]
  "Create planet identities under the stars using authentic ship names"
  (log "🪐 Creating fake Urbit planets...")
  
  (doseq [planet-name (take 10 star-names)] ; Use star names as planets
    (let [result (run-dfx-command 
                   (str "dfx canister call urbit-identity create_identity '(record { name = \"" planet-name "\"; description = \"Fake planet for development\"; identity_type = variant { Planet } })'"))]
      (if (str/includes? result "Ok")
        (let [address (-> result
                          (str/replace #"Ok\(" "")
                          (str/replace #"\)" "")
                          (str/trim))]
          (log (str "✅ Planet " planet-name " created with address: " address)))
        (log (str "❌ Failed to create planet " planet-name ": " result))))))

(defn create-moons [planet-addresses]
  "Create moon identities under the planets using authentic ship names"
  (log "🌙 Creating fake Urbit moons...")
  
  (doseq [moon-name (take 5 moon-names)]
    (let [result (run-dfx-command 
                   (str "dfx canister call urbit-identity create_identity '(record { name = \"" moon-name "\"; description = \"Fake moon for development\"; identity_type = variant { Moon } })'"))]
      (if (str/includes? result "Ok")
        (let [address (-> result
                          (str/replace #"Ok\(" "")
                          (str/replace #"\)" "")
                          (str/trim))]
          (log (str "✅ Moon " moon-name " created with address: " address)))
        (log (str "❌ Failed to create moon " moon-name ": " result))))))

(defn get-stats []
  "Get current galaxy statistics"
  (log "📊 Getting galaxy statistics...")
  
  (let [result (run-dfx-command "dfx canister call urbit-identity get_stats")]
    (log (str "📈 Galaxy stats: " result))))

(defn list-identities []
  "List all identities with authentic Urbit terminology"
  (log "📋 Listing all ship identities...")
  
  (let [result (run-dfx-command "dfx canister call urbit-identity get_logs")]
    (log "📝 Recent ship activity:")
    (doseq [line (str/split-lines result)]
      (when (str/starts-with? line "[")
        (log (str "   " line))))))

(defn test-ship-operations []
  "Test authentic Urbit ship operations"
  (log "🧪 Testing ship operations...")
  
  ;; Test getting a specific ship
  (let [result (run-dfx-command "dfx canister call urbit-identity get_identity '(1)'")]
    (if (str/includes? result "Ok")
      (log "✅ Ship lookup successful")
      (log (str "❌ Ship lookup failed: " result))))
  
  ;; Test address allocation
  (let [result (run-dfx-command "dfx canister call urbit-identity allocate_address 'variant{Planet}'")]
    (if (str/includes? result "Ok")
      (let [address (-> result
                        (str/replace #"Ok\(" "")
                        (str/replace #"\)" "")
                        (str/trim))]
        (log (str "✅ Address allocation successful: " address)))
      (log (str "❌ Address allocation failed: " result)))))

(defn main []
  "Main setup function"
  (log "🚀 Setting up Authentic Fake Urbit Galaxy...")
  (log "   Using real Urbit terminology and ship names from the official codebase")
  
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
        
        ;; Test operations
        (test-ship-operations)
        
        ;; Get statistics
        (get-stats)
        
        ;; List identities
        (list-identities)
        
        (log "🎉 Authentic Fake Urbit Galaxy setup complete!")
        (log "")
        (log "🌐 Access your galaxy at:")
        (log "   http://127.0.0.1:4943/?canisterId=u6s2n-gx777-77774-qaaba-cai&id=uxrrr-q7777-77774-qaaaq-cai")
        (log "")
        (log "🔧 Test commands:")
        (log "   dfx canister call urbit-identity get_stats")
        (log "   dfx canister call urbit-identity get_logs")
        (log "   dfx canister call urbit-identity get_identity '(1)'")
        (log "")
        (log "📚 Ship names used:")
        (log "   Galaxies: ~zod, ~bus, ~wes, ~sev, ~pol, ~bin, ~dig, ~wic")
        (log "   Stars: ~zod-tes, ~bus-tes, ~wes-tes, ~sev-tes, ~pol-tes")
        (log "   Planets: ~zod-tes-rac, ~bus-tes-rac, ~wes-tes-rac, ~sev-tes-rac")
        (log "   Moons: ~zod-tes-rac-moon, ~bus-tes-rac-moon, ~wes-tes-rac-moon")))
    
    (catch Exception e
      (log (str "❌ Setup failed: " (.getMessage e)))
      (System/exit 1))))

;; Run the setup
(main)
