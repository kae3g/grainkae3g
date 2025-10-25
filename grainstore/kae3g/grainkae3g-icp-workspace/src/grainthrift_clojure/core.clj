(ns grainthrift.core
  "GrainThrift: ICP-powered resale marketplace
  
   A Clotoko-powered clone of Poshmark for Ralph Lauren resale
   with ICP token payments, Internet Identity authentication,
   and Unsplash stock images."
  (:require [clojure.string :as str]
            [clojure.data.json :as json]
            [clj-time.core :as time]
            [clj-time.format :as time-fmt]
            [java-time :as jt]))

;; ═══════════════════════════════════════════════════════════
;; DATA STRUCTURES
;; ═══════════════════════════════════════════════════════════

(def product-catalog
  "Unsplash-powered Ralph Lauren product catalog"
  [{:id "rl-polo-classic"
    :name "Ralph Lauren Classic Polo Shirt"
    :brand "Ralph Lauren"
    :category "Tops"
    :price-icp 15.5
    :condition "Excellent"
    :size "Medium"
    :color "Navy Blue"
    :description "Classic Ralph Lauren polo shirt in excellent condition. Perfect for casual or smart casual occasions."
    :image-url "https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?w=400&h=400&fit=crop"
    :seller-id "user-001"
    :listed-date "2025-10-24"
    :status "available"}
   
   {:id "rl-rugby-shirt"
    :name "Ralph Lauren Rugby Shirt"
    :brand "Ralph Lauren"
    :category "Tops"
    :price-icp 22.0
    :condition "Good"
    :size "Large"
    :color "Red & White"
    :description "Vintage Ralph Lauren rugby shirt with classic stripes. Shows some wear but still stylish."
    :image-url "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400&h=400&fit=crop"
    :seller-id "user-002"
    :listed-date "2025-10-23"
    :status "available"}
   
   {:id "rl-chinos"
    :name "Ralph Lauren Chino Pants"
    :brand "Ralph Lauren"
    :category "Bottoms"
    :price-icp 28.5
    :condition "Like New"
    :size "32x32"
    :color "Khaki"
    :description "Ralph Lauren chino pants in like-new condition. Perfect fit and quality construction."
    :image-url "https://images.unsplash.com/photo-1591195853828-11db59a44f6b?w=400&h=400&fit=crop"
    :seller-id "user-003"
    :listed-date "2025-10-22"
    :status "available"}
   
   {:id "rl-blazer"
    :name "Ralph Lauren Blazer"
    :brand "Ralph Lauren"
    :category "Outerwear"
    :price-icp 85.0
    :condition "Excellent"
    :size "40R"
    :color "Navy"
    :description "Classic Ralph Lauren blazer in excellent condition. Perfect for business or formal occasions."
    :image-url "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&h=400&fit=crop"
    :seller-id "user-001"
    :listed-date "2025-10-21"
    :status "sold"}
   
   {:id "rl-sweater"
    :name "Ralph Lauren Cable Knit Sweater"
    :brand "Ralph Lauren"
    :category "Tops"
    :price-icp 35.0
    :condition "Good"
    :size "Medium"
    :color "Cream"
    :description "Classic Ralph Lauren cable knit sweater. Warm and stylish for winter months."
    :image-url "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=400&h=400&fit=crop"
    :seller-id "user-004"
    :listed-date "2025-10-20"
    :status "available"}])

(def user-profiles
  "GrainThrift user profiles with ICP wallets"
  [{:id "user-001"
    :username "fashionista_kae3g"
    :display-name "Kae3g Style"
    :bio "Curator of timeless Ralph Lauren pieces. Quality over quantity always."
    :avatar-url "https://images.unsplash.com/photo-1494790108755-2616b612b786?w=200&h=200&fit=crop&crop=face"
    :icp-wallet "umunu-kh777-77774-qaaca-cai"
    :addresses [{:id "addr-001"
                 :label "Home"
                 :street "123 Grain Street"
                 :city "San Rafael"
                 :state "CA"
                 :zip "94901"
                 :country "USA"}]
    :joined-date "2025-01-15"
    :rating 4.8
    :total-sales 47}
   
   {:id "user-002"
    :username "vintage_hunter"
    :display-name "Vintage Hunter"
    :bio "Passionate about finding hidden gems in Ralph Lauren's vintage collection."
    :avatar-url "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&h=200&fit=crop&crop=face"
    :icp-wallet "uxrrr-q7777-77774-qaaaq-cai"
    :addresses [{:id "addr-002"
                 :label "Apartment"
                 :street "456 Thrift Avenue"
                 :city "Berkeley"
                 :state "CA"
                 :zip "94704"
                 :country "USA"}]
    :joined-date "2025-03-22"
    :rating 4.6
    :total-sales 23}
   
   {:id "user-003"
    :username "style_curator"
    :display-name "Style Curator"
    :bio "Building a sustainable wardrobe one Ralph Lauren piece at a time."
    :avatar-url "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=200&h=200&fit=crop&crop=face"
    :icp-wallet "uzt4z-lp777-77774-qaabq-cai"
    :addresses [{:id "addr-003"
                 :label "Office"
                 :street "789 Fashion Blvd"
                 :city "San Francisco"
                 :state "CA"
                 :zip "94102"
                 :country "USA"}]
    :joined-date "2025-06-10"
    :rating 4.9
    :total-sales 89}
   
   {:id "user-004"
    :username "grain_thrifter"
    :display-name "Grain Thrifter"
    :bio "Part of the Grain Network community. Sustainable fashion advocate."
    :avatar-url "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=200&h=200&fit=crop&crop=face"
    :icp-wallet "umunu-kh777-77774-qaaca-cai"
    :addresses [{:id "addr-004"
                 :label "Home"
                 :street "321 Grain Path"
                 :city "Oakland"
                 :state "CA"
                 :zip "94601"
                 :country "USA"}]
    :joined-date "2025-09-05"
    :rating 4.7
    :total-sales 34}])

(def transactions
  "ICP payment transactions"
  [{:id "txn-001"
    :product-id "rl-blazer"
    :buyer-id "user-003"
    :seller-id "user-001"
    :amount-icp 85.0
    :status "completed"
    :timestamp "2025-10-21T14:30:00Z"
    :payment-method "icp-wallet"}
   
   {:id "txn-002"
    :product-id "rl-polo-classic"
    :buyer-id "user-002"
    :seller-id "user-001"
    :amount-icp 15.5
    :status "pending"
    :timestamp "2025-10-24T10:15:00Z"
    :payment-method "icp-wallet"}])

;; ═══════════════════════════════════════════════════════════
;; WEBSITE PAGES
;; ═══════════════════════════════════════════════════════════

(defn get-home-page
  "Generate the GrainThrift home page"
  []
  (str "
<!DOCTYPE html>
<html lang=\"en\">
<head>
    <meta charset=\"UTF-8\">
    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">
    <title>GrainThrift - Ralph Lauren Resale Marketplace</title>
    <meta name=\"description\" content=\"Buy and sell Ralph Lauren pieces with ICP tokens on the decentralized GrainThrift marketplace\" />
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%); color: #333; }
        .container { max-width: 1200px; margin: 0 auto; padding: 20px; }
        .header { background: white; padding: 20px; border-radius: 15px; margin-bottom: 30px; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }
        .logo { font-size: 2.5em; font-weight: bold; color: #2c3e50; text-align: center; margin-bottom: 10px; }
        .tagline { text-align: center; color: #7f8c8d; font-size: 1.2em; }
        .nav { display: flex; justify-content: center; gap: 30px; margin-top: 20px; }
        .nav a { text-decoration: none; color: #2c3e50; font-weight: 500; padding: 10px 20px; border-radius: 25px; transition: all 0.3s; }
        .nav a:hover { background: #3498db; color: white; }
        .hero { text-align: center; padding: 60px 20px; background: white; border-radius: 15px; margin-bottom: 30px; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }
        .hero h1 { font-size: 3em; margin-bottom: 20px; color: #2c3e50; }
        .hero p { font-size: 1.3em; color: #7f8c8d; margin-bottom: 30px; }
        .cta-button { display: inline-block; background: linear-gradient(45deg, #3498db, #2980b9); color: white; padding: 15px 30px; border-radius: 30px; text-decoration: none; font-weight: bold; font-size: 1.1em; transition: all 0.3s; }
        .cta-button:hover { transform: translateY(-2px); box-shadow: 0 6px 20px rgba(52, 152, 219, 0.4); }
        .products-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 30px; margin-bottom: 30px; }
        .product-card { background: white; border-radius: 15px; overflow: hidden; box-shadow: 0 4px 20px rgba(0,0,0,0.1); transition: all 0.3s; }
        .product-card:hover { transform: translateY(-5px); box-shadow: 0 8px 30px rgba(0,0,0,0.15); }
        .product-image { width: 100%; height: 250px; object-fit: cover; }
        .product-info { padding: 20px; }
        .product-title { font-size: 1.2em; font-weight: bold; margin-bottom: 10px; color: #2c3e50; }
        .product-price { font-size: 1.5em; font-weight: bold; color: #27ae60; margin-bottom: 10px; }
        .product-details { color: #7f8c8d; font-size: 0.9em; margin-bottom: 15px; }
        .buy-button { width: 100%; background: linear-gradient(45deg, #27ae60, #229954); color: white; border: none; padding: 12px; border-radius: 25px; font-weight: bold; cursor: pointer; transition: all 0.3s; }
        .buy-button:hover { background: linear-gradient(45deg, #229954, #1e8449); }
        .stats { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 20px; margin-bottom: 30px; }
        .stat-card { background: white; padding: 30px; border-radius: 15px; text-align: center; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }
        .stat-number { font-size: 2.5em; font-weight: bold; color: #3498db; margin-bottom: 10px; }
        .stat-label { color: #7f8c8d; font-size: 1.1em; }
        .footer { text-align: center; padding: 30px; background: white; border-radius: 15px; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }
        .icp-badge { display: inline-block; background: linear-gradient(45deg, #ff6b6b, #ee5a24); color: white; padding: 5px 15px; border-radius: 20px; font-size: 0.8em; margin-left: 10px; }
    </style>
</head>
<body>
    <div class=\"container\">
        <header class=\"header\">
            <div class=\"logo\">🌾 GrainThrift</div>
            <div class=\"tagline\">Ralph Lauren Resale Marketplace on ICP</div>
            <nav class=\"nav\">
                <a href=\"/\">Home</a>
                <a href=\"/products\">Products</a>
                <a href=\"/sell\">Sell</a>
                <a href=\"/profile\">Profile</a>
                <a href=\"/about\">About</a>
            </nav>
        </header>
        
        <section class=\"hero\">
            <h1>Buy & Sell Ralph Lauren with ICP Tokens</h1>
            <p>Join the decentralized fashion marketplace powered by Internet Computer Protocol</p>
            <a href=\"/products\" class=\"cta-button\">Browse Products</a>
        </section>
        
        <section class=\"stats\">
            <div class=\"stat-card\">
                <div class=\"stat-number\">" (count product-catalog) "</div>
                <div class=\"stat-label\">Products Listed</div>
            </div>
            <div class=\"stat-card\">
                <div class=\"stat-number\">" (count user-profiles) "</div>
                <div class=\"stat-label\">Active Sellers</div>
            </div>
            <div class=\"stat-card\">
                <div class=\"stat-number\">" (count transactions) "</div>
                <div class=\"stat-label\">Transactions</div>
            </div>
            <div class=\"stat-card\">
                <div class=\"stat-number\">4.8</div>
                <div class=\"stat-label\">Average Rating</div>
            </div>
        </section>
        
        <section class=\"products-grid\">
            " (str/join "\n            " 
               (map (fn [product]
                      (str "<div class=\"product-card\">
                        <img src=\"" (:image-url product) "\" alt=\"" (:name product) "\" class=\"product-image\">
                        <div class=\"product-info\">
                            <div class=\"product-title\">" (:name product) "</div>
                            <div class=\"product-price\">" (:price-icp product) " ICP <span class=\"icp-badge\">ICP</span></div>
                            <div class=\"product-details\">" (:condition product) " • " (:size product) " • " (:color product) "</div>
                            <button class=\"buy-button\" onclick=\"buyProduct('" (:id product) "')\">Buy Now</button>
                        </div>
                    </div>"))
                    (take 4 product-catalog))) "
        </section>
        
        <footer class=\"footer\">
            <p>🌾 Built with Clotoko on Internet Computer Protocol</p>
            <p>Powered by Grain Network • Sustainable Fashion • Decentralized Commerce</p>
        </footer>
    </div>
    
    <script>
        function buyProduct(productId) {
            alert('Buying product ' + productId + ' with ICP tokens!');
            // In a real implementation, this would integrate with ICP wallet
        }
    </script>
</body>
</html>"))

(defn get-products-page
  "Generate the products listing page"
  []
  (str "
<!DOCTYPE html>
<html lang=\"en\">
<head>
    <meta charset=\"UTF-8\">
    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">
    <title>Products - GrainThrift</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%); color: #333; }
        .container { max-width: 1200px; margin: 0 auto; padding: 20px; }
        .header { background: white; padding: 20px; border-radius: 15px; margin-bottom: 30px; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }
        .logo { font-size: 2.5em; font-weight: bold; color: #2c3e50; text-align: center; margin-bottom: 10px; }
        .nav { display: flex; justify-content: center; gap: 30px; margin-top: 20px; }
        .nav a { text-decoration: none; color: #2c3e50; font-weight: 500; padding: 10px 20px; border-radius: 25px; transition: all 0.3s; }
        .nav a:hover { background: #3498db; color: white; }
        .products-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 30px; }
        .product-card { background: white; border-radius: 15px; overflow: hidden; box-shadow: 0 4px 20px rgba(0,0,0,0.1); transition: all 0.3s; }
        .product-card:hover { transform: translateY(-5px); box-shadow: 0 8px 30px rgba(0,0,0,0.15); }
        .product-image { width: 100%; height: 250px; object-fit: cover; }
        .product-info { padding: 20px; }
        .product-title { font-size: 1.2em; font-weight: bold; margin-bottom: 10px; color: #2c3e50; }
        .product-price { font-size: 1.5em; font-weight: bold; color: #27ae60; margin-bottom: 10px; }
        .product-details { color: #7f8c8d; font-size: 0.9em; margin-bottom: 15px; }
        .buy-button { width: 100%; background: linear-gradient(45deg, #27ae60, #229954); color: white; border: none; padding: 12px; border-radius: 25px; font-weight: bold; cursor: pointer; transition: all 0.3s; }
        .buy-button:hover { background: linear-gradient(45deg, #229954, #1e8449); }
        .icp-badge { display: inline-block; background: linear-gradient(45deg, #ff6b6b, #ee5a24); color: white; padding: 5px 15px; border-radius: 20px; font-size: 0.8em; margin-left: 10px; }
        .status-badge { display: inline-block; padding: 5px 10px; border-radius: 15px; font-size: 0.8em; margin-left: 10px; }
        .status-available { background: #27ae60; color: white; }
        .status-sold { background: #e74c3c; color: white; }
    </style>
</head>
<body>
    <div class=\"container\">
        <header class=\"header\">
            <div class=\"logo\">🌾 GrainThrift</div>
            <nav class=\"nav\">
                <a href=\"/\">Home</a>
                <a href=\"/products\">Products</a>
                <a href=\"/sell\">Sell</a>
                <a href=\"/profile\">Profile</a>
                <a href=\"/about\">About</a>
            </nav>
        </header>
        
        <section class=\"products-grid\">
            " (str/join "\n            " 
               (map (fn [product]
                      (str "<div class=\"product-card\">
                        <img src=\"" (:image-url product) "\" alt=\"" (:name product) "\" class=\"product-image\">
                        <div class=\"product-info\">
                            <div class=\"product-title\">" (:name product) "</div>
                            <div class=\"product-price\">" (:price-icp product) " ICP <span class=\"icp-badge\">ICP</span></div>
                            <div class=\"product-details\">" (:condition product) " • " (:size product) " • " (:color product) "</div>
                            <div class=\"product-details\">" (:description product) "</div>
                            <div class=\"product-details\">Listed: " (:listed-date product) "</div>
                            <div class=\"product-details\">Status: <span class=\"status-badge status-" (:status product) "\">" (:status product) "</span></div>
                            " (if (= (:status product) "available")
                                (str "<button class=\"buy-button\" onclick=\"buyProduct('" (:id product) "')\">Buy Now</button>")
                                "<button class=\"buy-button\" disabled>Sold</button>") "
                        </div>
                    </div>"))
                    product-catalog)) "
        </section>
    </div>
    
    <script>
        function buyProduct(productId) {
            alert('Buying product ' + productId + ' with ICP tokens!');
            // In a real implementation, this would integrate with ICP wallet
        }
    </script>
</body>
</html>"))

(defn get-profile-page
  "Generate user profile page"
  []
  (str "
<!DOCTYPE html>
<html lang=\"en\">
<head>
    <meta charset=\"UTF-8\">
    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">
    <title>Profile - GrainThrift</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%); color: #333; }
        .container { max-width: 1200px; margin: 0 auto; padding: 20px; }
        .header { background: white; padding: 20px; border-radius: 15px; margin-bottom: 30px; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }
        .logo { font-size: 2.5em; font-weight: bold; color: #2c3e50; text-align: center; margin-bottom: 10px; }
        .nav { display: flex; justify-content: center; gap: 30px; margin-top: 20px; }
        .nav a { text-decoration: none; color: #2c3e50; font-weight: 500; padding: 10px 20px; border-radius: 25px; transition: all 0.3s; }
        .nav a:hover { background: #3498db; color: white; }
        .profile-section { background: white; padding: 30px; border-radius: 15px; margin-bottom: 30px; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }
        .profile-header { display: flex; align-items: center; margin-bottom: 30px; }
        .avatar { width: 100px; height: 100px; border-radius: 50%; object-fit: cover; margin-right: 30px; }
        .profile-info h2 { font-size: 2em; margin-bottom: 10px; color: #2c3e50; }
        .profile-info p { color: #7f8c8d; margin-bottom: 10px; }
        .rating { display: flex; align-items: center; gap: 10px; }
        .stars { color: #f39c12; font-size: 1.2em; }
        .wallet-info { background: #f8f9fa; padding: 20px; border-radius: 10px; margin-top: 20px; }
        .wallet-address { font-family: monospace; background: #e9ecef; padding: 10px; border-radius: 5px; margin-top: 10px; }
        .addresses-section { margin-top: 30px; }
        .address-card { background: #f8f9fa; padding: 20px; border-radius: 10px; margin-bottom: 15px; }
        .address-label { font-weight: bold; color: #2c3e50; margin-bottom: 10px; }
        .address-details { color: #7f8c8d; }
    </style>
</head>
<body>
    <div class=\"container\">
        <header class=\"header\">
            <div class=\"logo\">🌾 GrainThrift</div>
            <nav class=\"nav\">
                <a href=\"/\">Home</a>
                <a href=\"/products\">Products</a>
                <a href=\"/sell\">Sell</a>
                <a href=\"/profile\">Profile</a>
                <a href=\"/about\">About</a>
            </nav>
        </header>
        
        <section class=\"profile-section\">
            <div class=\"profile-header\">
                <img src=\"https://images.unsplash.com/photo-1494790108755-2616b612b786?w=200&h=200&fit=crop&crop=face\" alt=\"Profile\" class=\"avatar\">
                <div class=\"profile-info\">
                    <h2>Kae3g Style</h2>
                    <p>@fashionista_kae3g</p>
                    <p>Curator of timeless Ralph Lauren pieces. Quality over quantity always.</p>
                    <div class=\"rating\">
                        <span class=\"stars\">★★★★★</span>
                        <span>4.8 rating • 47 sales</span>
                    </div>
                </div>
            </div>
            
            <div class=\"wallet-info\">
                <h3>ICP Wallet</h3>
                <p>Your Internet Computer Protocol wallet for secure transactions</p>
                <div class=\"wallet-address\">umunu-kh777-77774-qaaca-cai</div>
            </div>
            
            <div class=\"addresses-section\">
                <h3>Saved Addresses</h3>
                <div class=\"address-card\">
                    <div class=\"address-label\">Home</div>
                    <div class=\"address-details\">
                        123 Grain Street<br>
                        San Rafael, CA 94901<br>
                        USA
                    </div>
                </div>
            </div>
        </section>
        
        <section class=\"profile-section\">
            <h3>Recent Transactions</h3>
            <div class=\"address-card\">
                <div class=\"address-label\">Sold: Ralph Lauren Blazer</div>
                <div class=\"address-details\">
                    Amount: 85.0 ICP<br>
                    Buyer: Style Curator<br>
                    Date: 2025-10-21
                </div>
            </div>
            <div class=\"address-card\">
                <div class=\"address-label\">Pending: Ralph Lauren Polo Shirt</div>
                <div class=\"address-details\">
                    Amount: 15.5 ICP<br>
                    Buyer: Vintage Hunter<br>
                    Date: 2025-10-24
                </div>
            </div>
        </section>
    </div>
</body>
</html>"))

;; ═══════════════════════════════════════════════════════════
;; API FUNCTIONS
;; ═══════════════════════════════════════════════════════════

(defn get-product
  "Get product by ID"
  [product-id]
  (first (filter #(= (:id %) product-id) product-catalog)))

(defn get-user-profile
  "Get user profile by ID"
  [user-id]
  (first (filter #(= (:id %) user-id) user-profiles)))

(defn get-products-by-category
  "Get products by category"
  [category]
  (filter #(= (:category %) category) product-catalog))

(defn get-products-by-seller
  "Get products by seller"
  [seller-id]
  (filter #(= (:seller-id %) seller-id) product-catalog))

(defn create-transaction
  "Create a new transaction"
  [product-id buyer-id seller-id amount-icp]
  {:id (str "txn-" (inc (count transactions)))
   :product-id product-id
   :buyer-id buyer-id
   :seller-id seller-id
   :amount-icp amount-icp
   :status "pending"
   :timestamp (str (jt/instant))
   :payment-method "icp-wallet"})

;; ═══════════════════════════════════════════════════════════
;; WEBSITE ROUTING
;; ═══════════════════════════════════════════════════════════

(defn get-page
  "Route to appropriate page based on path"
  [path]
  (case path
    "/" (get-home-page)
    "/products" (get-products-page)
    "/profile" (get-profile-page)
    "/sell" "Sell page coming soon!"
    "/about" "About page coming soon!"
    (get-home-page)))

;; ═══════════════════════════════════════════════════════════
;; CANISTER STATUS
;; ═══════════════════════════════════════════════════════════

(defn status
  "Get canister status"
  []
  "GrainThrift ICP Canister - Status: Active - Version: 0.1.0 (Clotoko)")

(defn whoami
  "Get canister principal"
  []
  "grainthrift-clojure")

;; ═══════════════════════════════════════════════════════════
;; MAIN INTERFACE
;; ═══════════════════════════════════════════════════════════

(defn -main
  "Main entry point for the GrainThrift ICP canister"
  [& args]
  (println "🌾 GrainThrift: ICP-powered resale marketplace")
  (println "Status:" (status))
  (println "Products:" (count product-catalog))
  (println "Users:" (count user-profiles))
  (println "Transactions:" (count transactions)))
