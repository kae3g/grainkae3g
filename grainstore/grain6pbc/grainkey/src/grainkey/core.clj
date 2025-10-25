;; ╔══════════════════════════════════════════════════════════════════════════════╗
;; ║                                                                              ║
;; ║                        🌾 GRAINKEY CORE MODULE 🌾                           ║
;; ║                                                                              ║
;; ║   Universal SSH & GnuPG Key Management for Grain Network                    ║
;; ║   Template-based, personalized, secure key generation and deployment         ║
;; ║                                                                              ║
;; ║   Features:                                                                  ║
;; ║   • SSH ed25519 key generation and deployment                               ║
;; ║   • GnuPG ed25519 signing key generation and export                         ║
;; ║   • Template-based key management for consistency                           ║
;; ║   • Personal configuration separation for customization                     ║
;; ║   • GitHub and Codeberg integration                                         ║
;; ║                                                                              ║
;; ╚══════════════════════════════════════════════════════════════════════════════╝

(ns grainkey.core
  (:require [babashka.fs :as fs]
            [babashka.process :refer [shell]]
            [clojure.string :as str]
            [clojure.edn :as edn]))

;; ═══════════════════════════════════════════════════════════════════════════════
;; CONFIGURATION MANAGEMENT
;; ═══════════════════════════════════════════════════════════════════════════════
;; 
;; Default configuration provides sensible defaults for all key types
;; Personal configuration can override any default values
;; Template system allows for consistent key generation across services

(def default-config
  "Default configuration for grainkey
   
   Key Features:
   • Uses ed25519 for both SSH and GnuPG (modern, secure, fast)
   • Template-based approach for consistent key management
   • Separate SSH and GnuPG directories for organization
   • Personal config file for customization without modifying core
   
   Security Notes:
   • Ed25519 is the current gold standard for cryptographic keys
   • 256-bit key length provides excellent security with good performance
   • Separate keys for different services (principle of least privilege)
   • GnuPG keys have expiration dates for security lifecycle management"
  {:key-type "ed25519"                    ; Modern elliptic curve cryptography
   :key-size 256                         ; 256-bit keys (ed25519 standard)
   :key-comment "grainkey-generated"      ; Default comment for generated keys
   :ssh-dir (str (System/getProperty "user.home") "/.ssh")     ; Standard SSH directory
   :gnupg-dir (str (System/getProperty "user.home") "/.gnupg") ; Standard GnuPG directory
   :personal-config-file "personal/config.edn"                 ; Personal overrides
   
   ;; Key templates define consistent key generation patterns
   ;; Each template specifies: type, filename, hosts, and metadata
   :key-templates 
   {:nixos-vm {:comment "nixos-grainkae3g-vm"    ; VM-specific SSH key
               :filename "nixos-grainkae3g"      ; Key filename
               :hosts ["192.168.122.204"]        ; Target hosts
               :type :ssh}                       ; SSH key type
              
    :github {:comment "github-kae3g"             ; GitHub SSH key
             :filename "github-kae3g"            ; Key filename
             :hosts ["github.com"]               ; GitHub host
             :type :ssh                          ; SSH key type
             :email "kae3g@users.noreply.github.com"  ; GitHub noreply email
             :name "kae3g"}                      ; GitHub username
             
    :codeberg {:comment "codeberg-kae3g"         ; Codeberg SSH key
               :filename "codeberg-kae3g"        ; Key filename
               :hosts ["codeberg.org"]           ; Codeberg host
               :type :ssh                        ; SSH key type
               :email "kae3g@users.noreply.codeberg.org" ; Codeberg noreply email
               :name "kae3g"}                   ; Codeberg username
               
    :github-gpg {:comment "github-gpg-kae3g"     ; GitHub GnuPG signing key
                 :filename "github-gpg-kae3g"    ; Key filename
                 :type :gpg                      ; GnuPG key type
                 :email "kae3g@users.noreply.github.com" ; GitHub noreply email
                 :name "kae3g"                   ; GitHub username
                 :expire-days 365}               ; 1-year expiration
                 
    :codeberg-gpg {:comment "codeberg-gpg-kae3g" ; Codeberg GnuPG signing key
                   :filename "codeberg-gpg-kae3g" ; Key filename
                   :type :gpg                    ; GnuPG key type
                   :email "kae3g@users.noreply.codeberg.org" ; Codeberg noreply email
                   :name "kae3g"                 ; Codeberg username
                   :expire-days 365}}})          ; 1-year expiration

;; ═══════════════════════════════════════════════════════════════════════════════
;; SSH KEY GENERATION
;; ═══════════════════════════════════════════════════════════════════════════════
;; 
;; SSH key generation using ssh-keygen with ed25519 algorithm
;; Provides secure, fast key generation with proper file permissions

(defn generate-ssh-key-pair
  "Generate SSH key pair with specified parameters
  
   Parameters:
   • key-type: Algorithm type (ed25519 recommended)
   • key-size: Key size in bits (256 for ed25519)
   • key-comment: Comment embedded in the key
   • filename: Base filename for the key pair
   • ssh-dir: Directory to store the keys
   
   Returns:
   • Map with :private-key, :public-key, and :fingerprint on success
   • nil on failure
   
   Security Notes:
   • Uses ed25519 for modern cryptographic security
   • No passphrase (-N \"\") for automated deployment
   • Keys are stored with proper permissions by ssh-keygen"
  [{:keys [key-type key-size key-comment filename ssh-dir]}]
  (let [key-path (str ssh-dir "/" filename)
        key-file (str key-path)      ; Private key file
        pub-file (str key-path ".pub")] ; Public key file
    
    (println (str "🔑 Generating SSH " key-type " key pair: " filename))
    (println (str "   Comment: " key-comment))
    (println (str "   Location: " key-file))
    
    ;; Generate the key pair using ssh-keygen
    ;; -t: key type (ed25519)
    ;; -f: output file
    ;; -C: comment
    ;; -N: passphrase (empty for automation)
    (let [result (shell {:out :string :err :string}
                        (str "ssh-keygen -t " key-type
                             " -f " key-file
                             " -C \"" key-comment "\""
                             " -N ''"))]
      
      (if (= 0 (:exit result))
        (do
          (println "✅ SSH key pair generated successfully")
          ;; Extract fingerprint from ssh-keygen output
          ;; Format: "SHA256:abc123... user@host"
          {:private-key key-file
           :public-key pub-file
           :fingerprint (str/replace (:out result) #".*SHA256:" "SHA256:")})
        (do
          (println "❌ SSH key generation failed:")
          (println (:err result))
          nil)))))

;; ═══════════════════════════════════════════════════════════════════════════════
;; GNUPG KEY GENERATION
;; ═══════════════════════════════════════════════════════════════════════════════
;; 
;; GnuPG key generation using batch mode for automation
;; Creates ed25519 signing keys for GitHub/Codeberg commit signing

(defn generate-gpg-key-pair
  "Generate GnuPG key pair with specified parameters
  
   Parameters:
   • filename: Base filename for the key
   • gnupg-dir: GnuPG directory (~/.gnupg)
   • email: Email address for the key
   • name: Real name for the key
   • expire-days: Key expiration in days
   
   Returns:
   • Map with :key-id, :email, :name, and :fingerprint on success
   • nil on failure
   
   Security Notes:
   • Uses ed25519 for both primary key and subkey
   • Batch mode for automated generation
   • %no-protection for automation (no passphrase)
   • Expiration date for security lifecycle management"
  [{:keys [filename gnupg-dir email name expire-days]}]
  (let [key-file (str gnupg-dir "/" filename ".gpg")    ; Key file (not used)
        batch-file (str gnupg-dir "/" filename ".batch")] ; Batch file for gpg
    
    (println (str "🔐 Generating GnuPG ed25519 key pair: " filename))
    (println (str "   Name: " name))
    (println (str "   Email: " email))
    (println (str "   Expires: " expire-days " days"))
    
    ;; Create batch file for gpg key generation
    ;; Batch format allows automated key generation
    (let [batch-content (str "Key-Type: ed25519\n"           ; Primary key type
                             "Key-Length: 256\n"            ; Primary key length
                             "Subkey-Type: ed25519\n"        ; Subkey type
                             "Subkey-Length: 256\n"          ; Subkey length
                             "Name-Real: " name "\n"         ; Real name
                             "Name-Email: " email "\n"       ; Email address
                             "Expire-Date: " expire-days "\n" ; Expiration
                             "%no-protection\n"              ; No passphrase
                             "%commit\n")]                   ; Commit the key
      
      (spit batch-file batch-content)
      
      ;; Generate the key pair using gpg batch mode
      (let [result (shell {:out :string :err :string}
                          (str "gpg --batch --generate-key " batch-file))]
        
        ;; Clean up batch file for security
        (fs/delete-if-exists batch-file)
        
        (if (= 0 (:exit result))
          (do
            (println "✅ GnuPG key pair generated successfully")
            ;; Extract key ID from gpg output
            ;; Format: "gpg: key ABC123DEF456 marked as ultimately trusted"
            (let [key-id (-> (:out result)
                             (str/split #"\n")
                             (->> (filter #(str/includes? % "gpg: key"))
                                  (first)
                                  (re-find #"gpg: key ([A-F0-9]+)")
                                  (second)))]
              {:key-id key-id
               :email email
               :name name
               :fingerprint (str "GPG-" key-id)}))
          (do
            (println "❌ GnuPG key generation failed:")
            (println (:err result))
            nil))))))

;; ═══════════════════════════════════════════════════════════════════════════════
;; UNIFIED KEY GENERATION INTERFACE
;; ═══════════════════════════════════════════════════════════════════════════════

(defn generate-key-pair
  "Generate key pair (SSH or GnuPG) based on type
  
   Parameters:
   • config: Configuration map with :type field
   
   Returns:
   • Key generation result map on success
   • nil on failure
   
   This function provides a unified interface for both SSH and GnuPG key generation"
  [config]
  (case (:type config)
    :ssh (generate-ssh-key-pair config)
    :gpg (generate-gpg-key-pair config)
    (do
      (println "❌ Unknown key type:" (:type config))
      nil)))

;; ═══════════════════════════════════════════════════════════════════════════════
;; KEY DEPLOYMENT
;; ═══════════════════════════════════════════════════════════════════════════════

;; ═══════════════════════════════════════════════════════════════════════════════
;; KEY DEPLOYMENT
;; ═══════════════════════════════════════════════════════════════════════════════

(defn deploy-ssh-key-to-host
  "Deploy SSH public key to remote host using ssh-copy-id"
  [{:keys [public-key host username]}]
  (println (str "🚀 Deploying SSH key to " username "@" host))
  
  (let [result (shell {:out :string :err :string}
                      (str "ssh-copy-id -f -i " public-key " " username "@" host))]
    
    (if (= 0 (:exit result))
      (do
        (println "✅ SSH key deployed successfully")
        true)
      (do
        (println "❌ SSH key deployment failed:")
        (println (:err result))
        false))))

(defn export-gpg-public-key
  "Export GnuPG public key for deployment to GitHub/Codeberg"
  [{:keys [key-id gnupg-dir filename]}]
  (let [export-file (str gnupg-dir "/" filename ".pub")]
    
    (println (str "📤 Exporting GnuPG public key: " key-id))
    
    (let [result (shell {:out :string :err :string}
                        (str "gpg --armor --export " key-id " > " export-file))]
      
      (if (= 0 (:exit result))
        (do
          (println "✅ GnuPG public key exported successfully")
          (println (str "   Location: " export-file))
          {:public-key-file export-file
           :key-id key-id})
        (do
          (println "❌ GnuPG key export failed:")
          (println (:err result))
          nil)))))

(defn deploy-key-to-host
  "Deploy key (SSH or GnuPG) to remote host"
  [config]
  (case (:type config)
    :ssh (deploy-ssh-key-to-host config)
    :gpg (export-gpg-public-key config)
    (do
      (println "❌ Unknown key type for deployment:" (:type config))
      false)))

(defn test-key-connection
  "Test SSH connection using the generated key"
  [{:keys [private-key host username]}]
  (println (str "🔍 Testing connection to " username "@" host))
  
  (let [result (shell {:out :string :err :string}
                      (str "ssh -i " private-key " -o ConnectTimeout=10 "
                           username "@" host " 'echo \"Hello from Grain Network!\""))]
    
    (if (= 0 (:exit result))
      (do
        (println "✅ Connection test successful")
        (println (str "   Response: " (:out result)))
        true)
      (do
        (println "❌ Connection test failed:")
        (println (:err result))
        false))))

;; ═══════════════════════════════════════════════════════════════════════════════
;; TEMPLATE MANAGEMENT
;; ═══════════════════════════════════════════════════════════════════════════════

(defn load-personal-config
  "Load personalized configuration from personal/config.edn"
  [config-file]
  (if (fs/exists? config-file)
    (try
      (let [config (edn/read-string (slurp config-file))]
        (println "✅ Loaded personal configuration")
        config)
      (catch Exception e
        (println "⚠️  Failed to load personal config, using defaults")
        {}))
    (do
      (println "⚠️  Personal config not found, using defaults")
      {})))

(defn merge-configs
  "Merge default config with personal config"
  [default-config personal-config]
  (merge-with merge default-config personal-config))

;; ═══════════════════════════════════════════════════════════════════════════════
;; KEY TEMPLATE OPERATIONS
;; ═══════════════════════════════════════════════════════════════════════════════

(defn generate-from-template
  "Generate key pair from a named template"
  [template-name config]
  (let [template (get-in config [:key-templates template-name])]
    (if template
      (generate-key-pair (merge config template))
      (do
        (println (str "❌ Template not found: " template-name))
        (println "Available templates:" (keys (:key-templates config)))
        nil))))

(defn deploy-template-keys
  "Deploy keys for a specific template to all configured hosts"
  [template-name config]
  (let [template (get-in config [:key-templates template-name])
        key-filename (:filename template)
        key-type (:type template)]
    
    (if template
      (case key-type
        :ssh (let [ssh-dir (:ssh-dir config)
                   private-key (str ssh-dir "/" key-filename)
                   public-key (str private-key ".pub")]
               
               (if (and (fs/exists? private-key) (fs/exists? public-key))
                 (do
                   (println (str "🚀 Deploying " template-name " SSH keys to configured hosts"))
                   (doseq [host (:hosts template)]
                     (deploy-key-to-host {:public-key public-key
                                         :host host
                                         :username "nixos"
                                         :type :ssh}))
                   (println "✅ SSH template deployment complete"))
                 (do
                   (println (str "❌ SSH keys for " template-name " not found"))
                   (println "Generate keys first with: grainkey generate " template-name))))
        
        :gpg (let [gnupg-dir (:gnupg-dir config)
                   key-id (str "0x" (:key-id template))]
               
               (if key-id
                 (do
                   (println (str "📤 Exporting " template-name " GnuPG public key"))
                   (deploy-key-to-host {:key-id key-id
                                        :gnupg-dir gnupg-dir
                                        :filename key-filename
                                        :type :gpg})
                   (println "✅ GnuPG template export complete"))
                 (do
                   (println (str "❌ GnuPG key ID for " template-name " not found"))
                   (println "Generate keys first with: grainkey generate " template-name))))
        
        (do
          (println (str "❌ Unknown key type for template " template-name ": " key-type))))
      
      (do
        (println (str "❌ Template " template-name " not found"))
        (println "Available templates:" (keys (:key-templates config)))))))

;; ═══════════════════════════════════════════════════════════════════════════════
;; MAIN INTERFACE
;; ═══════════════════════════════════════════════════════════════════════════════

(defn main
  "Main grainkey interface"
  [& args]
  (let [personal-config (load-personal-config (:personal-config-file default-config))
        config (merge-configs default-config personal-config)
        command (first args)
        template-name (second args)]
    
    (case command
      "generate" (if template-name
                   (generate-from-template (keyword template-name) config)
                   (do
                     (println "❌ Please specify a template name")
                     (println "Available templates:" (keys (:key-templates config)))))
      
      "deploy" (if template-name
                 (deploy-template-keys (keyword template-name) config)
                 (do
                   (println "❌ Please specify a template name")
                   (println "Available templates:" (keys (:key-templates config)))))
      
      "list" (do
               (println "🌾 Available key templates:")
               (doseq [[name template] (:key-templates config)]
                 (println (str "  " (name name) ": " (:comment template))))
               (println "")
               (println "🌾 Generated keys:")
               (let [ssh-dir (:ssh-dir config)]
                 (when (fs/exists? ssh-dir)
                   (doseq [key-file (fs/glob ssh-dir "*.pub")]
                     (println (str "  " (fs/file-name key-file)))))))
      
      "help" (do
               (println "🌾 Grainkey - Universal SSH Key Management")
               (println "")
               (println "Usage:")
               (println "  grainkey generate <template>  - Generate key pair from template")
               (println "  grainkey deploy <template>    - Deploy keys to configured hosts")
               (println "  grainkey list                 - List available templates and keys")
               (println "  grainkey help                 - Show this help")
               (println "")
               (println "Templates:")
               (doseq [[name template] (:key-templates config)]
                 (println (str "  " (name name) ": " (:comment template)))))
      
      (do
        (println "❌ Unknown command:" command)
        (println "Run 'grainkey help' for usage information")))))

;; ═══════════════════════════════════════════════════════════════════════════════
;; GRAIN NETWORK PHILOSOPHY
;; ═══════════════════════════════════════════════════════════════════════════════
;; 
;; 🌾 Security by Design:
;;   - Ed25519 keys for modern cryptographic security
;;   - Template-based key management for consistency
;;   - Personal configuration separation for customization
;;   - Clear separation of public and private key handling
;;
;; 🔒 Never Commit:
;;   - Private keys (*.key, *.pem, nixos-grainkae3g)
;;   - Personal configuration with sensitive data
;;
;; ✅ Safe to Commit:
;;   - Public keys (*.pub)
;;   - Template definitions
;;   - Core utility code
;;
;; 🌱 Vegan-Friendly Development:
;;   - Reproducible key generation
;;   - Template-based automation
;;   - Clear documentation and comments
;;
;; ═══════════════════════════════════════════════════════════════════════════════
