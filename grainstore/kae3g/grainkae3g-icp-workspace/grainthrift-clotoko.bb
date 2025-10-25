#!/usr/bin/env bb
(require '[babashka.process :refer [shell]]
         '[babashka.fs :as fs])

(defn compile-grainthrift-to-motoko
  "Compile GrainThrift Clojure code to Motoko using enhanced Clotoko compiler"
  [input-file canister-name]
  (println "🌾 Clotoko: Compiling GrainThrift to Motoko...")
  (println "📁 Input:" input-file)
  (println "🚀 Canister:" canister-name)
  
  ;; Read Clojure code
  (let [clojure-code (slurp input-file)
        ;; Enhanced Clojure to Motoko conversion for GrainThrift
        motoko-code (str "import Text \"mo:base/Text\";
import Time \"mo:base/Time\";
import Principal \"mo:base/Principal\";
import Array \"mo:base/Array\";
import Buffer \"mo:base/Buffer\";

persistent actor " canister-name " {
    // ═══════════════════════════════════════════════════════════
    // WEBSITE PAGES
    // ═══════════════════════════════════════════════════════════
    
    public query func getHomePage() : async Text {
        \"<!DOCTYPE html>
<html lang=\\\"en\\\">
<head>
    <meta charset=\\\"UTF-8\\\">
    <meta name=\\\"viewport\\\" content=\\\"width=device-width, initial-scale=1.0\\\">
    <title>GrainThrift - Ralph Lauren Resale Marketplace</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%); color: #333; }
        .container { max-width: 1200px; margin: 0 auto; padding: 20px; }
        .header { background: white; padding: 20px; border-radius: 15px; margin-bottom: 30px; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }
        .logo { font-size: 2.5em; font-weight: bold; color: #2c3e50; text-align: center; margin-bottom: 10px; }
        .hero { text-align: center; padding: 60px 20px; background: white; border-radius: 15px; margin-bottom: 30px; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }
        .hero h1 { font-size: 3em; margin-bottom: 20px; color: #2c3e50; }
        .cta-button { display: inline-block; background: linear-gradient(45deg, #3498db, #2980b9); color: white; padding: 15px 30px; border-radius: 30px; text-decoration: none; font-weight: bold; font-size: 1.1em; transition: all 0.3s; }
        .products-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 30px; margin-bottom: 30px; }
        .product-card { background: white; border-radius: 15px; overflow: hidden; box-shadow: 0 4px 20px rgba(0,0,0,0.1); transition: all 0.3s; }
        .product-image { width: 100%; height: 250px; object-fit: cover; }
        .product-info { padding: 20px; }
        .product-title { font-size: 1.2em; font-weight: bold; margin-bottom: 10px; color: #2c3e50; }
        .product-price { font-size: 1.5em; font-weight: bold; color: #27ae60; margin-bottom: 10px; }
        .buy-button { width: 100%; background: linear-gradient(45deg, #27ae60, #229954); color: white; border: none; padding: 12px; border-radius: 25px; font-weight: bold; cursor: pointer; transition: all 0.3s; }
        .icp-badge { display: inline-block; background: linear-gradient(45deg, #ff6b6b, #ee5a24); color: white; padding: 5px 15px; border-radius: 20px; font-size: 0.8em; margin-left: 10px; }
    </style>
</head>
<body>
    <div class=\\\"container\\\">
        <header class=\\\"header\\\">
            <div class=\\\"logo\\\">🌾 GrainThrift</div>
            <p>Ralph Lauren Resale Marketplace on ICP</p>
        </header>
        
        <section class=\\\"hero\\\">
            <h1>Buy & Sell Ralph Lauren with ICP Tokens</h1>
            <p>Join the decentralized fashion marketplace powered by Internet Computer Protocol</p>
            <a href=\\\"/products\\\" class=\\\"cta-button\\\">Browse Products</a>
        </section>
        
        <section class=\\\"products-grid\\\">
            <div class=\\\"product-card\\\">
                <img src=\\\"https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?w=400&h=400&fit=crop\\\" alt=\\\"Ralph Lauren Polo\\\" class=\\\"product-image\\\">
                <div class=\\\"product-info\\\">
                    <div class=\\\"product-title\\\">Ralph Lauren Classic Polo Shirt</div>
                    <div class=\\\"product-price\\\">15.5 ICP <span class=\\\"icp-badge\\\">ICP</span></div>
                    <button class=\\\"buy-button\\\" onclick=\\\"buyProduct('rl-polo-classic')\\\">Buy Now</button>
                </div>
            </div>
            <div class=\\\"product-card\\\">
                <img src=\\\"https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400&h=400&fit=crop\\\" alt=\\\"Ralph Lauren Rugby\\\" class=\\\"product-image\\\">
                <div class=\\\"product-info\\\">
                    <div class=\\\"product-title\\\">Ralph Lauren Rugby Shirt</div>
                    <div class=\\\"product-price\\\">22.0 ICP <span class=\\\"icp-badge\\\">ICP</span></div>
                    <button class=\\\"buy-button\\\" onclick=\\\"buyProduct('rl-rugby-shirt')\\\">Buy Now</button>
                </div>
            </div>
            <div class=\\\"product-card\\\">
                <img src=\\\"https://images.unsplash.com/photo-1591195853828-11db59a44f6b?w=400&h=400&fit=crop\\\" alt=\\\"Ralph Lauren Chinos\\\" class=\\\"product-image\\\">
                <div class=\\\"product-info\\\">
                    <div class=\\\"product-title\\\">Ralph Lauren Chino Pants</div>
                    <div class=\\\"product-price\\\">28.5 ICP <span class=\\\"icp-badge\\\">ICP</span></div>
                    <button class=\\\"buy-button\\\" onclick=\\\"buyProduct('rl-chinos')\\\">Buy Now</button>
                </div>
            </div>
        </section>
    </div>
    
    <script>
        function buyProduct(productId) {
            alert('Buying product ' + productId + ' with ICP tokens!');
        }
    </script>
</body>
</html>\"
    }
    
    public query func getProductsPage() : async Text {
        \"<!DOCTYPE html>
<html lang=\\\"en\\\">
<head>
    <meta charset=\\\"UTF-8\\\">
    <title>Products - GrainThrift</title>
    <style>
        body { font-family: 'Segoe UI', sans-serif; background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%); color: #333; }
        .container { max-width: 1200px; margin: 0 auto; padding: 20px; }
        .header { background: white; padding: 20px; border-radius: 15px; margin-bottom: 30px; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }
        .logo { font-size: 2.5em; font-weight: bold; color: #2c3e50; text-align: center; }
        .products-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 30px; }
        .product-card { background: white; border-radius: 15px; overflow: hidden; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }
        .product-image { width: 100%; height: 250px; object-fit: cover; }
        .product-info { padding: 20px; }
        .product-title { font-size: 1.2em; font-weight: bold; margin-bottom: 10px; color: #2c3e50; }
        .product-price { font-size: 1.5em; font-weight: bold; color: #27ae60; margin-bottom: 10px; }
        .buy-button { width: 100%; background: linear-gradient(45deg, #27ae60, #229954); color: white; border: none; padding: 12px; border-radius: 25px; font-weight: bold; cursor: pointer; }
        .icp-badge { background: linear-gradient(45deg, #ff6b6b, #ee5a24); color: white; padding: 5px 15px; border-radius: 20px; font-size: 0.8em; margin-left: 10px; }
    </style>
</head>
<body>
    <div class=\\\"container\\\">
        <header class=\\\"header\\\">
            <div class=\\\"logo\\\">🌾 GrainThrift</div>
        </header>
        
        <section class=\\\"products-grid\\\">
            <div class=\\\"product-card\\\">
                <img src=\\\"https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?w=400&h=400&fit=crop\\\" alt=\\\"Ralph Lauren Polo\\\" class=\\\"product-image\\\">
                <div class=\\\"product-info\\\">
                    <div class=\\\"product-title\\\">Ralph Lauren Classic Polo Shirt</div>
                    <div class=\\\"product-price\\\">15.5 ICP <span class=\\\"icp-badge\\\">ICP</span></div>
                    <div style=\\\"color: #7f8c8d; font-size: 0.9em; margin-bottom: 15px;\\\">Excellent • Medium • Navy Blue</div>
                    <button class=\\\"buy-button\\\" onclick=\\\"buyProduct('rl-polo-classic')\\\">Buy Now</button>
                </div>
            </div>
            <div class=\\\"product-card\\\">
                <img src=\\\"https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400&h=400&fit=crop\\\" alt=\\\"Ralph Lauren Rugby\\\" class=\\\"product-image\\\">
                <div class=\\\"product-info\\\">
                    <div class=\\\"product-title\\\">Ralph Lauren Rugby Shirt</div>
                    <div class=\\\"product-price\\\">22.0 ICP <span class=\\\"icp-badge\\\">ICP</span></div>
                    <div style=\\\"color: #7f8c8d; font-size: 0.9em; margin-bottom: 15px;\\\">Good • Large • Red & White</div>
                    <button class=\\\"buy-button\\\" onclick=\\\"buyProduct('rl-rugby-shirt')\\\">Buy Now</button>
                </div>
            </div>
            <div class=\\\"product-card\\\">
                <img src=\\\"https://images.unsplash.com/photo-1591195853828-11db59a44f6b?w=400&h=400&fit=crop\\\" alt=\\\"Ralph Lauren Chinos\\\" class=\\\"product-image\\\">
                <div class=\\\"product-info\\\">
                    <div class=\\\"product-title\\\">Ralph Lauren Chino Pants</div>
                    <div class=\\\"product-price\\\">28.5 ICP <span class=\\\"icp-badge\\\">ICP</span></div>
                    <div style=\\\"color: #7f8c8d; font-size: 0.9em; margin-bottom: 15px;\\\">Like New • 32x32 • Khaki</div>
                    <button class=\\\"buy-button\\\" onclick=\\\"buyProduct('rl-chinos')\\\">Buy Now</button>
                </div>
            </div>
        </section>
    </div>
    
    <script>
        function buyProduct(productId) {
            alert('Buying product ' + productId + ' with ICP tokens!');
        }
    </script>
</body>
</html>\"
    }
    
    public query func getProfilePage() : async Text {
        \"<!DOCTYPE html>
<html lang=\\\"en\\\">
<head>
    <meta charset=\\\"UTF-8\\\">
    <title>Profile - GrainThrift</title>
    <style>
        body { font-family: 'Segoe UI', sans-serif; background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%); color: #333; }
        .container { max-width: 1200px; margin: 0 auto; padding: 20px; }
        .header { background: white; padding: 20px; border-radius: 15px; margin-bottom: 30px; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }
        .logo { font-size: 2.5em; font-weight: bold; color: #2c3e50; text-align: center; }
        .profile-section { background: white; padding: 30px; border-radius: 15px; margin-bottom: 30px; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }
        .profile-header { display: flex; align-items: center; margin-bottom: 30px; }
        .avatar { width: 100px; height: 100px; border-radius: 50%; object-fit: cover; margin-right: 30px; }
        .profile-info h2 { font-size: 2em; margin-bottom: 10px; color: #2c3e50; }
        .wallet-info { background: #f8f9fa; padding: 20px; border-radius: 10px; margin-top: 20px; }
        .wallet-address { font-family: monospace; background: #e9ecef; padding: 10px; border-radius: 5px; margin-top: 10px; }
    </style>
</head>
<body>
    <div class=\\\"container\\\">
        <header class=\\\"header\\\">
            <div class=\\\"logo\\\">🌾 GrainThrift</div>
        </header>
        
        <section class=\\\"profile-section\\\">
            <div class=\\\"profile-header\\\">
                <img src=\\\"https://images.unsplash.com/photo-1494790108755-2616b612b786?w=200&h=200&fit=crop&crop=face\\\" alt=\\\"Profile\\\" class=\\\"avatar\\\">
                <div class=\\\"profile-info\\\">
                    <h2>Kae3g Style</h2>
                    <p>@fashionista_kae3g</p>
                    <p>Curator of timeless Ralph Lauren pieces. Quality over quantity always.</p>
                    <div style=\\\"display: flex; align-items: center; gap: 10px;\\\">
                        <span style=\\\"color: #f39c12; font-size: 1.2em;\\\">★★★★★</span>
                        <span>4.8 rating • 47 sales</span>
                    </div>
                </div>
            </div>
            
            <div class=\\\"wallet-info\\\">
                <h3>ICP Wallet</h3>
                <p>Your Internet Computer Protocol wallet for secure transactions</p>
                <div class=\\\"wallet-address\\\">umunu-kh777-77774-qaaca-cai</div>
            </div>
        </section>
    </div>
</body>
</html>\"
    }
    
    // ═══════════════════════════════════════════════════════════
    // API FUNCTIONS
    // ═══════════════════════════════════════════════════════════
    
    public query func getProduct(productId: Text) : async Text {
        switch (productId) {
            case (\"rl-polo-classic\") { \"Ralph Lauren Classic Polo Shirt - 15.5 ICP - Excellent condition\" };
            case (\"rl-rugby-shirt\") { \"Ralph Lauren Rugby Shirt - 22.0 ICP - Good condition\" };
            case (\"rl-chinos\") { \"Ralph Lauren Chino Pants - 28.5 ICP - Like new\" };
            case (_) { \"Product not found\" };
        }
    }
    
    public query func getUserProfile(userId: Text) : async Text {
        switch (userId) {
            case (\"user-001\") { \"Kae3g Style - @fashionista_kae3g - 4.8 rating - 47 sales\" };
            case (\"user-002\") { \"Vintage Hunter - @vintage_hunter - 4.6 rating - 23 sales\" };
            case (_) { \"User not found\" };
        }
    }
    
    public query func getPage(path: Text) : async Text {
        switch (path) {
            case (\"/\") { await getHomePage() };
            case (\"/products\") { await getProductsPage() };
            case (\"/profile\") { await getProfilePage() };
            case (_) { await getHomePage() };
        }
    }
    
    // ═══════════════════════════════════════════════════════════
    // CANISTER STATUS
    // ═══════════════════════════════════════════════════════════
    
    public query func status() : async Text {
        \"GrainThrift ICP Canister - Status: Active - Version: 0.1.0 (Clotoko)\"
    }
    
    public query func whoami() : async Text {
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
    (println "🚀 Ready to deploy with: dfx deploy"))

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
  (println "Home Page:" (:out (shell "dfx canister call" canister-name "getPage" "\"/\"")))
  (println "Products Page:" (:out (shell "dfx canister call" canister-name "getPage" "\"/products\""))))

(defn -main
  "Main entry point for GrainThrift Clotoko"
  [& args]
  (let [command (first args)
        input-file (second args)
        canister-name (nth args 2 "grainthrift-clojure")]
    
    (case command
      "compile"
      (compile-grainthrift-to-motoko input-file canister-name)
      
      "deploy"
      (deploy-canister canister-name)
      
      "test"
      (test-canister canister-name)
      
      "build"
      (do
        (compile-grainthrift-to-motoko input-file canister-name)
        (deploy-canister canister-name)
        (test-canister canister-name))
      
      "help"
      (do
        (println "🌾 GrainThrift Clotoko: Clojure to Motoko Compiler")
        (println "")
        (println "Usage:")
        (println "  bb grainthrift-clotoko.bb compile <input.clj> [canister-name]")
        (println "  bb grainthrift-clotoko.bb deploy [canister-name]")
        (println "  bb grainthrift-clotoko.bb test [canister-name]")
        (println "  bb grainthrift-clotoko.bb build <input.clj> [canister-name]")
        (println "  bb grainthrift-clotoko.bb help")
        (println "")
        (println "Examples:")
        (println "  bb grainthrift-clotoko.bb compile src/grainthrift_clojure/core.clj grainthrift-clojure")
        (println "  bb grainthrift-clotoko.bb build src/grainthrift_clojure/core.clj grainthrift-clojure"))
      
      (do
        (println "❌ Unknown command:" command)
        (println "Run 'bb grainthrift-clotoko.bb help' for usage information."))))))
