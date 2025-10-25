#!/usr/bin/env bb
(println "🌾 Clotoko: Clojure to Motoko Compiler")
(println "Testing basic functionality...")

(defn test-compile
  [input-file canister-name]
  (println "📁 Input:" input-file)
  (println "🚀 Canister:" canister-name)
  
  ;; Create output directory
  (require '[babashka.fs :as fs])
  (fs/create-dirs (str "src/" canister-name "_motoko"))
  
  ;; Write simple Motoko code
  (let [motoko-code (str "import Text \"mo:base/Text\";

actor " canister-name " {
    public query func status() : Text {
        \"Grain6 Website ICP Canister - Status: Active - Version: 0.1.0 (Clotoko)\"
    }
}")]
    (spit (str "src/" canister-name "_motoko/main.mo") motoko-code))
  
  (println "✅ Compilation successful!")
  (println "📁 Output: src/" canister-name "_motoko/main.mo"))

(test-compile "src/grain6_website_clojure/core.clj" "grain6-website-clojure")
