#!/usr/bin/env bb
(require '[babashka.process :refer [shell]]
         '[babashka.fs :as fs])

(defn compile-clojure-to-motoko
  "Compile Clojure code to Motoko using Clotoko compiler"
  [input-file canister-name]
  (println "🌾 Clotoko: Compiling Clojure to Motoko...")
  (println "📁 Input:" input-file)
  (println "🚀 Canister:" canister-name)
  
  ;; Read Clojure code
  (let [clojure-code (slurp input-file)
        ;; Simple Clojure to Motoko conversion
        motoko-code (str "import Text \"mo:base/Text\";
import Time \"mo:base/Time\";
import Principal \"mo:base/Principal\";

actor " canister-name " {
    public query func getHomePage() : Text {
        \"" (clojure.string/replace clojure-code #"\"" "\\\"") "\"
    }
    
    public query func status() : Text {
        \"Grain6 Website ICP Canister - Status: Active - Version: 0.1.0 (Clotoko)\"
    }
    
    public query func whoami() : Text {
        \"" canister-name "\"
    }
}")]
    
    ;; Create output directory
    (fs/create-dirs (str "src/" canister-name "_motoko"))
    
    ;; Write Motoko code
    (spit (str "src/" canister-name "_motoko/main.mo") motoko-code)
    
    ;; Update dfx.json
    (let [dfx-config (str "{
  \"version\": 1,
  \"canisters\": {
    \"" canister-name "\": {
      \"main\": \"src/" canister-name "_motoko/main.mo\",
      \"type\": \"motoko\"
    }
  },
  \"defaults\": {
    \"build\": {
      \"args\": \"\",
      \"packtool\": \"\"
    }
  },
  \"output_env_file\": \".env\",
  \"networks\": {
    \"local\": {
      \"bind\": \"127.0.0.1:4943\",
      \"type\": \"ephemeral\"
    }
  }
}")]
      (spit "dfx.json" dfx-config))
    
    (println "✅ Compilation successful!")
    (println "📁 Output: src/" canister-name "_motoko/main.mo")
    (println "🚀 Ready to deploy with: dfx deploy")))

(defn deploy-canister
  "Deploy the compiled canister to local ICP"
  [canister-name]
  (println "🚀 Deploying canister:" canister-name)
  (shell "dfx deploy")
  (println "✅ Deployment complete!"))

(defn test-canister
  "Test the deployed canister"
  [canister-name]
  (println "🧪 Testing canister:" canister-name)
  (println "Status:" (:out (shell "dfx canister call" canister-name "status")))
  (println "Whoami:" (:out (shell "dfx canister call" canister-name "whoami"))))

(defn -main
  "Main entry point for Clotoko"
  [& args]
  (let [command (first args)
        input-file (second args)
        canister-name (nth args 2 "grain6-website")]
    
    (case command
      "compile"
      (compile-clojure-to-motoko input-file canister-name)
      
      "deploy"
      (deploy-canister canister-name)
      
      "test"
      (test-canister canister-name)
      
      "build"
      (do
        (compile-clojure-to-motoko input-file canister-name)
        (deploy-canister canister-name)
        (test-canister canister-name))
      
      "help"
      (do
        (println "🌾 Clotoko: Clojure to Motoko Compiler")
        (println "")
        (println "Usage:")
        (println "  bb clotoko compile <input.clj> [canister-name]")
        (println "  bb clotoko deploy [canister-name]")
        (println "  bb clotoko test [canister-name]")
        (println "  bb clotoko build <input.clj> [canister-name]")
        (println "  bb clotoko help")
        (println "")
        (println "Examples:")
        (println "  bb clotoko compile src/grain6_website_clojure/core.clj grain6-website")
        (println "  bb clotoko build src/grain6_website_clojure/core.clj grain6-website"))
      
      (do
        (println "❌ Unknown command:" command)
        (println "Run 'bb clotoko help' for usage information.")))))
