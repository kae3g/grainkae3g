#!/usr/bin/env bb
(require '[babashka.process :refer [shell]]
         '[clojure.string :as str])

(println "🌾 Grain6 ICP Canister Development Script")
(println "═══════════════════════════════════════════════════════════════════════════════")

(defn check-icp-sdk []
  "Check if ICP SDK is installed"
  (println "1️⃣ Checking ICP SDK installation...")
  (let [result (shell {:out :string} "dfx" "--version")]
    (if (= (:exit result) 0)
      (do
        (println "✅ ICP SDK found:" (:out result))
        true)
      (do
        (println "❌ ICP SDK not found. Please install from: https://internetcomputer.org/docs/current/developer-docs/setup/install/")
        false))))

(defn check-clotoko []
  "Check if Clotoko is available"
  (println "2️⃣ Checking Clotoko availability...")
  (let [result (shell {:out :string} "clj" "-M:build" "--help")]
    (if (= (:exit result) 0)
      (do
        (println "✅ Clotoko build system available")
        true)
      (do
        (println "❌ Clotoko not found. Please check Clotoko installation.")
        false))))

(defn build-canister []
  "Build the Grain6 ICP canister"
  (println "3️⃣ Building Grain6 ICP canister...")
  (let [result (shell {:dir "grainstore/clotoko-icp"} "clj" "-M:build")]
    (if (= (:exit result) 0)
      (do
        (println "✅ Canister built successfully")
        true)
      (do
        (println "❌ Canister build failed:")
        (println (:out result))
        false))))

(defn deploy-canister []
  "Deploy the canister to local ICP replica"
  (println "4️⃣ Deploying canister to local ICP replica...")
  (let [result (shell {:dir "grainstore/clotoko-icp"} "dfx" "deploy")]
    (if (= (:exit result) 0)
      (do
        (println "✅ Canister deployed successfully")
        true)
      (do
        (println "❌ Canister deployment failed:")
        (println (:out result))
        false))))

(defn test-canister []
  "Test the deployed canister"
  (println "5️⃣ Testing deployed canister...")
  (let [result (shell {:dir "grainstore/clotoko-icp"} "dfx" "canister" "call" "grain6_icp" "canister_query" "'{\"method\": \"get-graintime\", \"params\": {\"author\": \"kae3g\"}}'")]
    (if (= (:exit result) 0)
      (do
        (println "✅ Canister test successful:")
        (println (:out result))
        true)
      (do
        (println "❌ Canister test failed:")
        (println (:out result))
        false))))

(defn main []
  "Main development workflow"
  (println "Starting Grain6 ICP canister development...")
  (println "")
  
  (when (and (check-icp-sdk)
             (check-clotoko)
             (build-canister)
             (deploy-canister)
             (test-canister))
    (println "")
    (println "🎉 Grain6 ICP canister development complete!")
    (println "📋 Next steps:")
    (println "   - Test additional canister functions")
    (println "   - Deploy to ICP mainnet")
    (println "   - Monitor canister performance")
    (println "   - Optimize cycle usage")))

(main)
